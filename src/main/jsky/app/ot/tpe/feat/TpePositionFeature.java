/*
 * Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jsky.app.ot.tpe.feat;

import gemini.sp.SpTelescopePosList;
import gemini.sp.SpTelescopePos;
import gemini.util.RADec;
import gemini.util.CoordSys;
import orac.util.CoordConvert;

import jsky.app.ot.fits.gui.FitsPosMapEntry;
import jsky.app.ot.fits.gui.FitsMouseEvent;

import jsky.app.ot.tpe.TpeImageFeature;
import jsky.app.ot.tpe.TpeDraggableFeature;
import jsky.app.ot.tpe.TpeEraseableFeature;
import jsky.app.ot.tpe.TpeSelectableFeature;

import jsky.app.ot.tpe.TpePositionMap;
import java.awt.geom.Point2D;

/**
 * A base class for all telescope positions.
 *
 * The <tt>draw</tt> method of TpeImageFeature and the <tt>dragStart</tt>
 * method of TpeDraggableFeature are left for subclasses.  This class
 * handles the common problem of dragging a position, once that position
 * has been located to start the drag.
 */
public abstract class TpePositionFeature extends TpeImageFeature implements
        TpeDraggableFeature, TpeEraseableFeature, TpeSelectableFeature {
    protected FitsPosMapEntry _dragObject;

    /**
     * Construct the feature with its name and description.
     */
    public TpePositionFeature(String name, String descr) {
        super(name, descr);
    }

    /**
     */
    public final SpTelescopePosList getSpTelescopePosList() {
        TpePositionMap pm = TpePositionMap.getMap(_iw);
        return (SpTelescopePosList) pm.getTelescopePosList();
    }

    /**
     */
    public boolean positionIsClose(FitsPosMapEntry pme, int x, int y) {
        Point2D.Double p = pme.screenPos;

        if (p == null) {
            return false;
        }

        double dx = Math.abs(p.x - x);

        if (dx > MARKER_SIZE) {
            return false;
        }

        double dy = Math.abs(p.y - y);

        if (dy > MARKER_SIZE) {
            return false;
        }

        return true;
    }

    /**
     */
    public void drag(FitsMouseEvent fme) {
        if (_dragObject != null) {
            _dragObject.screenPos.x = fme.xWidget;
            _dragObject.screenPos.y = fme.yWidget;
        }

        _iw.repaint();
    }

    /**
     */
    public void dragStop(FitsMouseEvent fme) {
        if (_dragObject != null) {
            dragDoPositionUpdate(fme);
            _dragObject = null;

            _iw.repaint();
        }
    }

    protected void dragDoPositionUpdate(FitsMouseEvent fme) {
        if (_dragObject != null) {
            SpTelescopePos tp = (SpTelescopePos) _dragObject.telescopePos;

            try {
                RADec raDec = CoordConvert.convert(fme.ra, fme.dec,
                        CoordSys.FK5, tp.getCoordSys());
                tp.setXY(raDec.ra, raDec.dec);

            } catch (UnsupportedOperationException e) {
                tp.setCoordSys(CoordSys.FK5);
                tp.setXY(fme.ra, fme.dec);
            }
        }
    }
}
