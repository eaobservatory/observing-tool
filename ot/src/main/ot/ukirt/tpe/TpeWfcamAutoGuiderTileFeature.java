/*
 * Copyright (C) 2006-2012 Science and Technology Facilities Council.
 * All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ot.ukirt.tpe;

import java.awt.Color;
import java.awt.Graphics;

import jsky.app.ot.tpe.TpeImageFeature;
import jsky.app.ot.tpe.TpeImageWidget;

import jsky.app.ot.fits.gui.FitsImageInfo;

import gemini.util.Angle;
import gemini.util.PolygonD;

/**
 * Draws the WFCAM autoguider CCD footprint.
 *
 * @author Based on ot/ukirt/tpe/TpeAcqCameraFeature.java.
 *         Modified by Martin Folger (M.Folger@roe.ac.uk).
 */
public class TpeWfcamAutoGuiderTileFeature extends TpeImageFeature {
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
     *
     * Apparently it wasn't installed straight.
     */
    public static final double AUTOGUIDER_ANGLE = 46.8;

    private PolygonD _autoguiderAreaPD;

    /**
     * Construct the feature with its name and description.
     */
    public TpeWfcamAutoGuiderTileFeature() {
        super("WFCAM TILE AG", "WFCAM Autoguider Tile Footprint");
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
     * Calculate the polygon describing the screen location of the science area.
     */
    double[] xOffsets = {550.0, 0.0, -550.0, 0.0};

    double[] yOffsets = {0.0, 550.0, 0.0, -550.0};

    private void _calc(FitsImageInfo fii, int i) {
        if (_autoguiderAreaPD == null) {
            _autoguiderAreaPD = new PolygonD();
            _autoguiderAreaPD.xpoints = new double[5];
            _autoguiderAreaPD.ypoints = new double[5];
            _autoguiderAreaPD.npoints = 5;
        }

        double[] xpoints = _autoguiderAreaPD.xpoints;
        double[] ypoints = _autoguiderAreaPD.ypoints;

        double xOffset = xOffsets[i];
        double yOffset = yOffsets[i];
        double x = fii.baseScreenPos.x + (xOffset * fii.pixelsPerArcsec);
        double y = fii.baseScreenPos.y + (yOffset * fii.pixelsPerArcsec);

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
    }

    /**
     * Draw the feature.
     */
    public void draw(Graphics g, FitsImageInfo fii) {
        g.setColor(Color.magenta);

        for (int i = 0; i < 4; i++) {
            _calc(fii, i);
            g.drawPolygon(_autoguiderAreaPD.getAWTPolygon());
        }
    }
}
