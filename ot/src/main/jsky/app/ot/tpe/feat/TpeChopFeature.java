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

import java.awt.Color;
import java.awt.Graphics;

import gemini.util.CoordSys;
import gemini.sp.iter.SpIterChop;

import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.fits.gui.FitsMouseEvent;

import jsky.app.ot.tpe.TpeImageFeature;
import jsky.app.ot.tpe.TpeDraggableFeature;
import jsky.app.ot.tpe.TpeImageWidget;

import gemini.util.Angle;
import orac.util.ScienceArea;

/**
 * An implementation class used to simplify the job of rotating.
 */
final class TpeChopDragObject {
}

/**
 * Draws the Chop position.
 *
 * @author Modified for JCMT OT by Martin Folger (M.Folger@roe.ac.uk),
 *         based on Gemini Science Area feature.
 */
public class TpeChopFeature extends TpeImageFeature implements
        TpeDraggableFeature {
    protected double _chopX = 0;
    protected double _chopY = 0;
    protected double _chopInnerRadius = 0;
    protected double _chopOuterRadius = 0;
    protected double _baseX = 0;
    protected double _baseY = 0;
    protected boolean _drawAsCircle = false;
    protected SpIterChop _iterChop;
    private boolean _valid = false;
    protected TpeChopDragObject _dragObject;
    protected boolean _dragging = false;
    protected int _dragX;
    protected int _dragY;

    /**
     * Construct the feature with its name and description.
     */
    public TpeChopFeature() {
        super("Chop", "Chop iterator position.");
    }

    /**
     * Reinit.
     */
    public void reinit(TpeImageWidget iw, FitsImageInfo fii) {
        super.reinit(iw, fii);
        _valid = false;
        return;
    }

    /**
     * The position angle has changed.
     */
    public void posAngleUpdate(FitsImageInfo fii) {
        _valid = false;
    }

    /**
     * Reports whether the chop pattern includes the base position.
     *
     * The default value is true.  Subclasses should override this
     * as required.
     *
     * Where this is false, the chop position is drawn as
     * offset to one side by half of the chop distance.
     */
    protected boolean isInclusiveOfBase() {
        return true;
    }

    /**
     * Calculate the polygon describing the screen location
     * of the science area.
     */
    protected boolean _calc(FitsImageInfo fii) {
        // Need the chop iterator to know what to draw.
        SpIterChop spIterChop = (SpIterChop) _iw.getBaseItem();

        if ((spIterChop == null) || (spIterChop.getSelectedIndex() < 0)) {
            return false;
        }

        // Subclasses may depend on this being set, so do so now before
        // calling isInclusiveOfBase.
        this._iterChop = spIterChop;

        int chopStepIndex = spIterChop.getSelectedIndex();
        String coordFrame = spIterChop.getCoordFrame(chopStepIndex);

        _drawAsCircle = drawAsCircle(coordFrame);

        _baseX = fii.baseScreenPos.x;
        _baseY = fii.baseScreenPos.y;

        double scale = fii.pixelsPerArcsec;
        double chopThrow = spIterChop.getThrow(chopStepIndex);
        double chopAngle = spIterChop.getAngle(chopStepIndex) * Math.PI / 180;

        double sideFactor = isInclusiveOfBase() ? 1 : 0.5;

        _chopX = _baseX - scale * sideFactor * chopThrow * Math.sin(chopAngle);
        _chopY = _baseY - scale * sideFactor * chopThrow * Math.cos(chopAngle);

        ScienceArea scienceArea = null;

        try {
            if (_iw.getInstrumentItem() != null) {
                scienceArea = ScienceArea.fromArray(
                        _iw.getInstrumentItem().getScienceArea());
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        double scienceAreaRadius =
                (scienceArea == null) ? 0 : scienceArea.getBoundingRadius();

        _chopInnerRadius = scale * (sideFactor * chopThrow - scienceAreaRadius);
        _chopOuterRadius = scale * (sideFactor * chopThrow + scienceAreaRadius);

        // Already have the current values
        if (_valid && (spIterChop == _iterChop)) {
            return true;
        }

        _valid = true;
        return true;
    }

    /**
     * Draw the feature.
     */
    public void draw(Graphics g, FitsImageInfo fii) {
        if (!_calc(fii)) {
            return;
        }

        double scale = fii.pixelsPerArcsec;

        ScienceArea scienceArea = null;
        try {
            if (_iw.getInstrumentItem() != null) {
                scienceArea = ScienceArea.fromArray(
                        _iw.getInstrumentItem().getScienceArea());
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        g.setColor(Color.magenta);

        // There is an instrument in scope then draw the science area around
        // the base and chop position.
        if (scienceArea != null) {
            scienceArea.draw(g, _drawAsCircle, _baseX, _baseY, scale);
            scienceArea.draw(g, _drawAsCircle, _chopX, _chopY, scale);
        }

        // Draw the actual chop positions (at the centre of the science areas)
        // as a small boxes.  The box at position (_chopX, _chopY),
        // i.e. the one with the label, is used for dragging the position
        // with the mouse.
        g.drawRect((int) _chopX - 2, (int) _chopY - 2, 4, 4);
        g.drawRect((int) _baseX - 2, (int) _baseY - 2, 4, 4);

        //   g.setFont(FONT);
        g.drawString(_name + " (Step " + _iterChop.getSelectedIndex() + ")",
                (int) _chopX + 3, (int) _chopY + 2);

        if (_drawAsCircle) {
            drawCircles(g);
        }
    }

    /**
     * Draw two circles around the base position based on the chop locations.
     *
     * If the exact position is not known due to the Az/El coordinate system
     * then the science area is between these two circles.
     */
    protected void drawCircles(Graphics g) {
        g.drawOval((int) (_baseX - _chopInnerRadius),
                (int) (_baseY - _chopInnerRadius),
                (int) (2 * _chopInnerRadius),
                (int) (2 * _chopInnerRadius));
        g.drawOval((int) (_baseX - _chopOuterRadius),
                (int) (_baseY - _chopOuterRadius),
                (int) (2 * _chopOuterRadius),
                (int) (2 * _chopOuterRadius));
    }

    /**
     * Start dragging the object.
     */
    public boolean dragStart(FitsMouseEvent fme, FitsImageInfo fii) {
        _dragObject = new TpeChopDragObject();

        _dragging = (_dragObject != null);

        if (_dragging) {
            _dragX = fme.xWidget;
            _dragY = fme.yWidget;
        }

        return _dragging;
    }

    /**
     * Drag to a new location.
     */
    public void drag(FitsMouseEvent fme) {
        if (_dragObject != null) {
            _dragX = fme.xWidget;
            _dragY = fme.yWidget;

            _iterChop.setAngle(getAngle(fme.xOffset, fme.yOffset),
                    _iterChop.getSelectedIndex());
            _iterChop.setThrow(getThrow(fme.xOffset, fme.yOffset),
                    _iterChop.getSelectedIndex());
        }

        _iw.repaint();

    }

    /**
     * Stop dragging.
     */
    public void dragStop(FitsMouseEvent fme) {
        if (_dragObject != null) {
            _dragging = false;
            drag(fme);
            _dragObject = null;
        }
    }

    /**
     * Return true if the chop position should be drawn as circle
     * given the coordFrame.
     */
    protected boolean drawAsCircle(String coordFrame) {
        return !(coordFrame.equals(CoordSys.COORD_SYS[CoordSys.FK5])
                || coordFrame.equals(CoordSys.COORD_SYS[CoordSys.FK4]));
    }

    protected static double getAngle(double x, double y) {
        double angle;

        double xa = Math.abs(x);
        double ya = Math.abs(y);

        if (xa == 0) {
            if (y >= 0) {
                return 0.0;
            } else {
                return 180.0;
            }
        }

        angle = (Angle.atanRadians(ya / xa) * 360) / (Math.PI * 2.0);

        if ((x > 0) && (y >= 0)) {
            return 90.0 - angle;
        } else if ((x < 0) && (y >= 0)) {
            return 270.0 + angle;
        } else if ((x < 0) && (y < 0)) {
            return 270.0 - angle;
        }

        return 90.0 + angle;
    }

    protected static double getThrow(double x, double y) {
        return Math.sqrt((x * x) + (y * y));
    }
}
