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

import java.util.Vector;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JComboBox;

/**
 * @author Dennis Kelly (bdk@roe.ac.uk),
 *         modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class Sampler implements ItemListener {

    double centreFrequency;
    double bandWidth;

    /**
     * Sideband of this sampler ("lsb", "usb" or null).
     *
     * This is the sideband which should be used to compute the rest frequency
     * of this sampler, if no line details are known.  If line details are
     * are present, then they should be used in preference to this value.
     */
    String sideband;

    /** Front end IF. */
    double feIF;

    /** Instantaneous half-bandwidth of frontend below the central IF. */
    double feBandWidthLower;
    /** Instantaneous half-bandwidth of frontend above the central IF. */
    double feBandWidthUpper;

    /** Number of mixers selected on heterodyne panel */
    int nMixers = 1;

    /**
     * Bandwidth options for this sampler.
     *
     * The bandwidth options in this array are not necessarily the actual
     * bandwidths of the instrument. The bandwidths may have been reduced by a
     * factor (derived form the overlap). It is important that the number of
     * channels in {@link #channelsArray} is reduced by the same factor
     * in order to get valid values for the resolution.
     */
    double[] bandWidthsArray;
    int channels = 0;

    /**
     * Channel options for this sampler.
     *
     * The channel options in this array are not necessarily the actual number
     * of channels of the instrument. The channels may have been reduced by a
     * factor (derived form the overlap). It is important that the number of
     * channels is reduced by the same factor by which the bandwidths in
     * {@link #bandWidthsArray} have been reduced in order to get valid values
     * for the resolution.
     */
    int[] channelsArray;
    int row;
    Vector<SamplerWatcher> swArray = new Vector<SamplerWatcher>();
    JComboBox bandWidthChoice;

    public Sampler(double centreFrequency,
            double feBandWidthLower, double feBandWidthUpper,
            double[] bandWidthsArray, int[] channelsArray,
            JComboBox bandWidthChoice, String sideband) {
        this.centreFrequency = centreFrequency;
        this.feIF = centreFrequency;
        this.feBandWidthLower = feBandWidthLower;
        this.feBandWidthUpper = feBandWidthUpper;
        this.bandWidth = bandWidthsArray[0];
        this.bandWidthsArray = bandWidthsArray;
        this.channels = channelsArray[0];
        this.channelsArray = channelsArray;
        this.bandWidthChoice = bandWidthChoice;
        this.sideband = sideband;

        bandWidthChoice.addItemListener(this);
    }

    public void addSamplerWatcher(SamplerWatcher sw) {
        swArray.addElement(sw);
    }

    /**
     * Set centre frequency to a value in the allowed range.
     *
     * The centre frequency is adjusted if necessary so that the sideband fits
     * into the frontend bandwidth.
     */
    public void setCentreFrequency(double centreFrequency, String sideband) {
        int j;

        this.centreFrequency = centreFrequency;
        this.sideband = sideband;

        // Check whether the centreFrequency has to be adjusted in order to
        // make the new bandWidth fit into the frontend bandwidth.

        if (centreFrequency >
                    (feIF + (feBandWidthUpper - (0.5 * bandWidth)))) {
            this.centreFrequency =
                    (feIF + (feBandWidthUpper - (0.5 * bandWidth)));

        } else if (centreFrequency <
                    (feIF - (feBandWidthLower - (0.5 * bandWidth)))) {
            this.centreFrequency =
                    (feIF - (feBandWidthLower - (0.5 * bandWidth)));
        }

        if (!swArray.isEmpty()) {
            for (j = 0; j < swArray.size(); j++) {
                swArray.elementAt(j).updateSamplerValues(
                        this.centreFrequency, bandWidth, channels, sideband);
            }
        }
    }

    public double getCentreFrequency() {
        return centreFrequency;
    }

    /**
     * Sets band width, notifies SamplerWatchers but
     * does <b>not</b> change the band width choice box.
     */
    public void setBandWidth(double value) {
        bandWidth = value;

        // Find index of new bandwidth and set channels from channels array.
        for (int i = 0; i < bandWidthsArray.length; i++) {
            if (bandWidthsArray[i] == value) {
                channels = channelsArray[i];
            }
        }

        // Adjust centre frequency if necessary so that the sideband fits into
        // the frontend bandwidth.
        setCentreFrequency(getCentreFrequency(), sideband);

        bandWidthChoice.removeItemListener(this);
        bandWidthChoice.setSelectedItem("" + (Math.rint(value * 1.0E-6)));
        bandWidthChoice.addItemListener(this);
    }

    /**
     * Returns the currently selected band width of this sampler.
     */
    public double getBandWidth() {
        return bandWidth;
    }

    /**
     * Returns numbers the number of channels corresponding to the currently
     * selected band width of this sampler.
     */
    public int getChannels() {
        return channels;
    }

    /**
     * Returns an array of the band width options of this sampler.
     */
    public double[] getBandWidthOptions() {
        return bandWidthsArray;
    }

    public int getResolution() {
        return nMixers * ((int) (1.0E-3 * bandWidth / (double) channels));
    }

    public void setNumberOfMixers(int nMixers) {
        this.nMixers = nMixers;
    }

    public void itemStateChanged(ItemEvent ev) {
        setBandWidth(Double.parseDouble(
                (String) bandWidthChoice.getSelectedItem()) * 1.0E6);
    }
}
