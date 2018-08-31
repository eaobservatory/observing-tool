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

package ot.ukirt.tpe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

import jsky.app.ot.tpe.TpeImageFeature;
import jsky.app.ot.tpe.TpeImageWidget;
import jsky.app.ot.tpe.TpePositionMap;

import jsky.app.ot.fits.gui.FitsImageInfo;

import gemini.sp.SpTelescopePos;
import gemini.util.Angle;
import gemini.util.PolygonD;

/**
 * Draws the WFCAM autoguider CCD footprint.
 *
 * @author Based on ot/ukirt/tpe/TpeAcqCameraFeature.java.
 *         Modified by Martin Folger (M.Folger@roe.ac.uk).
 */
public class TpeWfcamAutoGuiderFeature extends TpeImageFeature {
    /**
     * Usable width of autoguider CCD in arcseconds.
     *
     * The usable width can be smaller than the actual width
     * if the edge of the CCD should not be used for guiding.
     */
    public static final double AUTOGUIDER_WIDTH = 230.0;

    /**
     * Usable height of autoguider CCD in arcseconds.
     *
     * The usable height can be smaller than the actual height
     * if the edge of the CCD should not be used for guiding.
     */
    public static final double AUTOGUIDER_HEIGHT = 230.0;

    /**
     * Autoguider CCD angle in degrees.
     */
    public static final double AUTOGUIDER_ANGLE = 46.8;

    private PolygonD _autoguiderAreaPD = null;
    private String positionName;

    /**
     * Construct the feature with its name and description.
     */
    public TpeWfcamAutoGuiderFeature() {
        this("WFCAM AG", "WFCAM Autoguider Footprint", null);
    }

    protected TpeWfcamAutoGuiderFeature(String name, String descr, String positionName) {
        super(name, descr);
        this.positionName = positionName;
    }

    /**
     * Reinit.
     */
    public void reinit(TpeImageWidget iw, FitsImageInfo fii) {
        super.reinit(iw, fii);
    }

    /**
     * The position angle has changed.
     */
    public void posAngleUpdate(FitsImageInfo fii) {
    }

    /**
     * Calculate the polygon describing the screen location of the auto guider
     * area.
     */
    private void _calc(FitsImageInfo fii) {
        if (_autoguiderAreaPD == null) {
            _autoguiderAreaPD = new PolygonD();
            _autoguiderAreaPD.xpoints = new double[5];
            _autoguiderAreaPD.ypoints = new double[5];
            _autoguiderAreaPD.npoints = 5;
        }

        double[] xpoints = _autoguiderAreaPD.xpoints;
        double[] ypoints = _autoguiderAreaPD.ypoints;

        double x = fii.baseScreenPos.x;
        double y = fii.baseScreenPos.y;

        double w = (fii.pixelsPerArcsec * AUTOGUIDER_WIDTH) / 2.0;
        double h = (fii.pixelsPerArcsec * AUTOGUIDER_HEIGHT) / 2.0;

        xpoints[0] = x - w;
        xpoints[1] = x + w;
        ypoints[0] = y - h;
        ypoints[1] = y - h;

        xpoints[2] = x + w;
        xpoints[3] = x - w;
        ypoints[2] = y + h;
        ypoints[3] = y + h;

        xpoints[4] = xpoints[0];
        ypoints[4] = ypoints[0];

        // Rotate by AUTOGUIDER_ANGLE.
        // The skyRotate method is used for this although the rotation
        // is NOT due to sky rotation but to the way the autoguider CCD
        // is fixed in WFCAM.
        _iw.skyRotate(_autoguiderAreaPD,
                Angle.degreesToRadians(AUTOGUIDER_ANGLE));

        // If we're not plotting around the base position, apply offsets to
        // the polygon corners.  Do this by getting the position the same way
        // that TpeGuidePosFeature does and subtracting the base position.
        if (positionName != null) {
            TpePositionMap pm = TpePositionMap.getMap(_iw);
            Point2D.Double base = pm.getLocationFromTag(SpTelescopePos.BASE_TAG);
            Point2D.Double pos = null;

            if (positionName == "SKY") {
                for (String tag: SpTelescopePos.getSkyTags()) {
                    Point2D.Double thisPos = pm.getLocationFromTag(tag);
                    if (thisPos != null) {
                        pos = thisPos;
                        break;
                    }
                }
            }

            if ((pos == null) || (base == null)) {
                // Didn't find a tag: unset the polygon and abort.
                _autoguiderAreaPD = null;
                return;
            }

            double xoff = pos.x - base.x;
            double yoff = pos.y - base.y;

            for (int i = 0; i < 5; i ++) {
                _autoguiderAreaPD.xpoints[i] += xoff;
                _autoguiderAreaPD.ypoints[i] += yoff;
            }
        }
    }

    /**
     * Draw the feature.
     */
    public void draw(Graphics g, FitsImageInfo fii) {
        _calc(fii);

        if (_autoguiderAreaPD != null) {
            g.setColor(Color.magenta);
            g.drawPolygon(_autoguiderAreaPD.getAWTPolygon());
        }
    }
}
