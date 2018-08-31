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
 *
 * Copyright 2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 */

package ot.jcmt.tpe;

import java.awt.Polygon;
import java.awt.geom.Point2D;

import jsky.app.ot.tpe.TpeImageWidget;
import jsky.app.ot.tpe.TpeSciArea;
import jsky.app.ot.fits.gui.FitsImageInfo;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTelescopePosList;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.util.CoordSys;
import gemini.util.PolygonD;
import orac.jcmt.iter.SpIterRasterObs;
import gemini.util.RADec;
import orac.util.MapArea;

/**
 * Describes a Scan Area and facilitates drawing, rotating it.
 *
 * @see orac.jcmt.iter.SpIterRasterObs
 *
 * @author Martin Folger (M.Folger@roe.ac.uk),
 *         based on jsky.app.ot.tpe.TpeSciArea
 */
public class TpeScanArea extends TpeSciArea {
    private PolygonD _pd;

    public TpeScanArea() {
        _pd = new PolygonD();
        _pd.xpoints = new double[5];
        _pd.ypoints = new double[5];
        _pd.npoints = 5;
    }

    /**
     * Update the ScanArea fields, returning true iff changes were made.
     */
    public boolean update(SpInstObsComp spInst, FitsImageInfo fii) {
        throw new UnsupportedOperationException(
                "TpeScanArea.update(orac.jcmt.iter.SpIterRasterObs, "
                        + "jsky.app.ot.fits.gui.FitsImageInfo) should be used");
    }

    /**
     * Update the ScanArea fields, returning true iff changes were made.
     */
    public boolean update(SpIterRasterObs iterRaster, FitsImageInfo fii) {
        double w, h, posAngle, sky;

        w = iterRaster.getWidth() * fii.pixelsPerArcsec;
        h = iterRaster.getHeight() * fii.pixelsPerArcsec;

        posAngle = (Math.PI * iterRaster.getPosAngle()) / 180.0;
        sky = fii.theta;

        // Update the instance variables if necessary.
        if ((w != this.width) || (h != this.height)
                || (posAngle != this.posAngleRadians)
                || (sky != this.skyCorrection)) {

            this.width = w;
            this.height = h;
            this.posAngleRadians = posAngle;
            this.skyCorrection = sky;
            return true;
        }

        return false;
    }

    public PolygonD getPolygonDAt(double x, double y, TpeImageWidget _iw,
            SpIterRasterObs iterRaster) {
        if (iterRaster != null && _iw != null) {
            SpTelescopeObsComp targetList =
                    SpTreeMan.findTargetList(iterRaster);
            SpTelescopePosList list = targetList.getPosList();
            SpTelescopePos position = list.getBasePosition();

            if (position.getCoordSys() == CoordSys.GAL) {
                RADec[] positions = MapArea.createNewMapArea(
                        position.getXaxis(), position.getYaxis(), 0, 0,
                        iterRaster.getWidth(), iterRaster.getHeight(),
                        iterRaster.getPosAngle());

                PolygonD pd = _pd;
                double[] xpoints = pd.xpoints;
                double[] ypoints = pd.ypoints;

                Point2D.Double point = _iw.raDecToImageWidget(positions[0].ra,
                        positions[0].dec);
                xpoints[0] = point.x;
                ypoints[0] = point.y;

                point = _iw.raDecToImageWidget(positions[1].ra,
                        positions[1].dec);
                xpoints[1] = point.x;
                ypoints[1] = point.y;

                point = _iw.raDecToImageWidget(positions[2].ra,
                        positions[2].dec);
                xpoints[2] = point.x;
                ypoints[2] = point.y;

                point = _iw.raDecToImageWidget(positions[3].ra,
                        positions[3].dec);
                xpoints[3] = point.x;
                ypoints[3] = point.y;

                xpoints[4] = xpoints[0];
                ypoints[4] = ypoints[0];

                return new PolygonD(pd);
            }
        }
        return super.getPolygonDAt(x, y);
    }

    public Polygon getPolygonAt(double x, double y, TpeImageWidget _iw,
            SpIterRasterObs iterRaster) {
        return getPolygonDAt(x, y, _iw, iterRaster).getAWTPolygon();
    }
}
