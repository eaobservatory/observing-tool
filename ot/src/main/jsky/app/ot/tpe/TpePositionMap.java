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

package jsky.app.ot.tpe;

import jsky.app.ot.fits.gui.FitsImageWidget;
import jsky.app.ot.fits.gui.FitsPosMap;
import jsky.app.ot.fits.gui.FitsPosMapEntry;

import gemini.sp.SpTelescopePos;

import java.util.Enumeration;
import java.util.Hashtable;

import java.awt.geom.Point2D;

/**
 * An auxiliary class used to maintain a mapping between telescope positions
 * and image widget locations.
 *
 * There is one map per image widget.
 */
public class TpePositionMap extends FitsPosMap {
    private static Hashtable<TpeImageWidget, TpePositionMap> _mapTable =
            new Hashtable<TpeImageWidget, TpePositionMap>();
    private boolean _findBase = false;
    private boolean _findUserTargets = false;
    private boolean _findGuideStars = false;

    /**
     * Get the position map associated with the given image widget, creating
     * it if necessary.
     */
    public static TpePositionMap getMap(TpeImageWidget iw) {
        TpePositionMap tpm = _mapTable.get(iw);

        if (tpm == null) {
            tpm = new TpePositionMap(iw);
            _mapTable.put(iw, tpm);
        }

        return tpm;
    }

    /**
     * Get the map only if it already exists.
     */
    public static TpePositionMap getExistingMap(TpeImageWidget iw) {
        return _mapTable.get(iw);
    }

    /**
     * Remove the position map associated with the given image widget,
     * returning it if it exists.
     */
    public static TpePositionMap removeMap(TpeImageWidget iw) {
        return _mapTable.remove(iw);
    }

    /**
     * Construct with an image widget.
     */
    public TpePositionMap(FitsImageWidget iw) {
        super(iw);
    }

    /**
     * Turn on/off the ability to find the base position with a call to
     * <tt>locate</tt>.
     */
    public void setFindBase(boolean find) {
        _findBase = find;
    }

    /**
     * Turn on/off the ability to find a guide star with a call to
     * <tt>locate</tt>.
     */
    public void setFindGuideStars(boolean find) {
        _findGuideStars = find;
    }

    /**
     * Turn on/off the ability to find a user position with a call to
     * <tt>locate</tt>.
     */
    public void setFindUserTarget(boolean find) {
        _findUserTargets = find;
    }

    /**
     * Find a (visible) position under the given x,y location.
     */
    public FitsPosMapEntry locate(int x, int y) {
        Hashtable<String, FitsPosMapEntry> posTable = getPosTable();

        if (posTable == null) {
            return null;
        }

        Enumeration<FitsPosMapEntry> enumeration = posTable.elements();

        while (enumeration.hasMoreElements()) {
            FitsPosMapEntry pme = enumeration.nextElement();
            Point2D.Double p = pme.screenPos;

            if (p == null) {
                continue;
            }

            // Is this position under the mouse indicator?
            double dx = Math.abs(p.x - x);
            if (dx > MARKER_SIZE) {
                continue;
            }

            double dy = Math.abs(p.y - y);
            if (dy > MARKER_SIZE) {
                continue;
            }

            // Is this position visible?
            SpTelescopePos tp = (SpTelescopePos) pme.telescopePos;

            if (tp.isBasePosition()) {
                if (_findBase) {
                    return pme;
                } else {
                    continue;
                }

            } else if (tp.isUserPosition()) {
                if (_findUserTargets) {
                    return pme;
                } else {
                    continue;
                }

            } else if (tp.isGuidePosition()) {
                if (_findGuideStars) {
                    return pme;
                } else {
                    continue;
                }
            }
        }

        return null;
    }
}
