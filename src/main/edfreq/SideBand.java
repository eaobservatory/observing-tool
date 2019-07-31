/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
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

package edfreq;

import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import javax.swing.JScrollBar;
import java.awt.Color;

/**
 * @author Dennis Kelly (bdk@roe.ac.uk),
 *         modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class SideBand implements AdjustmentListener, SamplerWatcher {
    double lowLimit;
    double highLimit;
    double subBandWidth;
    double subBandCentre;
    Sampler sampler;
    FrequencyTable.SideBandScrollBar sideBandGui;
    AdjustmentListener _adjustmentListener = null;

    /**
     * The lineButton argument has been so that its text can be reset to "No
     * Line" when the side band JScrollBar of this Sideband is changed.
     */
    double pixratio;
    EmissionLines emissionLines;
    boolean isTopSideBand;

    /**
     * SideBand constructor.
     *
     * The lineButton argument has been so that its text can be reset to "No
     * Line" when the side band JScrollBar of this Sideband is changed.
     */
    public SideBand(double lowLimit, double highLimit, double subBandWidth,
            double subBandCentre, Sampler sampler,
            FrequencyTable.SideBandScrollBar sideBandGui,
            double pixratio, EmissionLines emissionLines,
            boolean isTopSideBand) {
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
        this.subBandWidth = subBandWidth;
        this.subBandCentre = subBandCentre;
        this.sampler = sampler;
        this.sideBandGui = sideBandGui;
        this.pixratio = pixratio;
        this.emissionLines = emissionLines;
        this.isTopSideBand = isTopSideBand;
        sideBandGui.addAdjustmentListener(this);
    }

    public double getSubBandCentre() {
        if (highLimit < 0.0) {
            subBandCentre = -sampler.getCentreFrequency();
        } else {
            subBandCentre = sampler.getCentreFrequency();
        }

        return subBandCentre;
    }

    public void setScaledCentre(int v) {
        subBandCentre = ((double) v) / pixratio + (0.5 * subBandWidth);

        String sideband = isTopSideBand
            ? sampler.sideband
            : ((highLimit > 0.0) ? "usb" : "lsb");

        sampler.setCentreFrequency(Math.abs(subBandCentre), sideband);
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        setScaledCentre(sideBandGui.getValue());

        if (_adjustmentListener != null) {
            _adjustmentListener.adjustmentValueChanged(e);
        }
    }

    public void updateSamplerValues(double centre, double width, int channels, String sideband) {
        if (highLimit < 0.0) {
            subBandCentre = -centre;
        } else {
            subBandCentre = centre;
        }

        subBandWidth = width;

        sideBandGui.removeAdjustmentListener(this);

        int sw = (int) Math.rint(pixratio * subBandWidth);

        sideBandGui.setIsInvalid(
            sw >= (pixratio * (highLimit - lowLimit)));

        sideBandGui.setIsSelected(
            (highLimit > 0.0) ^ (sideband.equalsIgnoreCase("lsb")));

        int sc = (int) Math.rint(
                (getSubBandCentre() - (0.5 * subBandWidth)) * pixratio);


        int pixTimesLow = (int) Math.rint(pixratio * lowLimit);
        int pixTimesHigh = (int) Math.rint(pixratio * highLimit);

        sideBandGui.setValues(sc, sw, pixTimesLow, pixTimesHigh);

        sideBandGui.addAdjustmentListener(this);
    }

    public void on() {
        sideBandGui.setEnabled(true);
    }

    public void off() {
        sideBandGui.setEnabled(false);
    }

    /**
     * Adds one AdjustmentListener.
     *
     * The SideBand can only have a single AdjustmentListener. The
     * AdjustmentListener is only notified of an adjustmentValueChanged if the
     * adjustment is a change of the IF (moving scroll bar) and not if it is a
     * change of bandwidth (change of width of the scroll bar). This is
     * achieved because this AdjustmentListener (the SideBand) is removed
     * from its scroll bar while updateSamplerValues is executed.
     */
    public void addAdjustmentListener(AdjustmentListener adjustmentListener) {
        _adjustmentListener = adjustmentListener;
    }
}
