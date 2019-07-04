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

/**
 * Contains information to the various band specifications for each heterodyne
 * back end.
 *
 * @author Dennis Kelly (bdk@roe.ac.uk),
 *         modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class BandSpec {
    /** Name of the current band specification */
    public String name;

    /** Number if bands in this band specification */
    public int numBands;

    /**
     * Array of bandwidths.
     *
     * <h3>Terminology</h3>
     * <ul>
     * <li><b>subsystem vs subband</b><br>
     * A subsystem is made up of one (non-hybrid mode) or more (hybrid mode)
     * subbands.
     * <li><b>subsystem</b><br>
     * Corresponds to one row in the OT's frequency editor.
     * <li><b>subband</b><br>
     * Corresponds to the hardware.
     * <li><b>Hybrid subsystem</b><br>
     * A subsystem that is made up of more than one subbands (hybrid mode).
     * <li><b>Hybrid subband</b><br>
     * This might be a bit confusing. A "Hybrid subband" is an individual
     * subband that contributes to a hybrid subsystem (hybrid mode).
     * </ul>
     *
     * The array contains the combined bandwidths based on 0 overlap, i.e. a
     * bandwidth in this array is the sum of the bandwidths of one or more
     * individual hybrid subbands (assuming no overlap). The bandwidth of an
     * individual hybrid subband is not stored in this class. The methods
     * {@link #getBandWidths(double)} and {@link #getDefaultOverlapBandWidths()}
     * are used to calculate combined bandwidths (i.e. sums one or more
     * individual hybrid subbands) with overlaps.
     */
    public double[] bandWidths;

    /** Default overlaps asscoiated with each bandwidth. */
    public double[] defaultOverlaps;

    /** Number of channels asscoiated with each bandwidth. */
    public int[] channels;

    /** Number of hybrid subbands asscoiated with each bandwidth. */
    public int[] numHybridSubBands;

    // Added by MFO (September 5, 2002)
    /**
     * @param numHybridSubBands
     *            1 for non-hybrid, &gt; 1 for hybrid.
     */
    public BandSpec(String name, int numBands, double[] bandWidths,
            double[] defaultOverlaps, int[] channels, int[] numHybridSubBands) {
        this.name = name;
        this.numBands = numBands;
        this.bandWidths = bandWidths;
        this.channels = channels;
        this.defaultOverlaps = defaultOverlaps;
        this.numHybridSubBands = numHybridSubBands;
    }

    public String toString() {
        return name;
    }

    // Added by MFO (September 4, 2002)
    /**
     * Get the array of bandwidths narrowed according to the specified overlap.
     *
     * Each bandwidth has a number of hybrid subbands asscoiated with it. This
     * number can be 1 in which case the respective subsystem is not hybrid.
     *
     * The overlap value is used twice:
     * <ol>
     * <li>amount of overlap of two adjacent hybrid subbands
     * <li>1/2 overlap is removed on either side of the band
     * </ol>
     *
     * The latter means that even if there is only a single subband than this
     * will still be reduced by the overlap in the returned array.
     * <p>
     *
     * The formula used is <blockquote><tt>
     *   combined_bandwidth<sub>overlap</sub> =
     *     combined_bandwidth<sub>no_overlap</sub> -
     *       (number<sub>hybrid_subbands</sub> * overlap)
     * </tt></blockquote>
     *
     * Each bandwidth in the {@link #bandWidths} field of this class is used as
     * a <tt>combined_bandwidth<sub>no_overlap</sub></tt> in the above
     * calculation.
     *
     * The calculation is done for each bandwidth and the results are returned
     * as an array.
     * <p>
     *
     * <b>The resulting bandwidth values are rounded to kHz.</b>
     *
     * @see #getDefaultOverlapBandWidths()
     */
    public double[] getBandWidths(double overlap) {
        double[] result = new double[bandWidths.length];

        for (int i = 0; i < bandWidths.length; i++) {
            result[i] = bandWidths[i] - (numHybridSubBands[i] * overlap);

            result[i] = Math.rint(result[i] / 1.0E3) * 1.0E3;
        }

        return result;
    }

    /**
     * Get the array of bandwidths narrowed according to the default overlaps
     * for the corresponding bandwidths.
     *
     * The default overlap bandwidths are calculated as in
     * {@link #getBandWidths(double)} except the each calculation is based on
     * the default overlap for that band.
     *
     * @see #getBandWidths(double)
     */
    public double[] getDefaultOverlapBandWidths() {
        double[] result = new double[bandWidths.length];

        for (int i = 0; i < bandWidths.length; i++) {
            result[i] = bandWidths[i]
                    - (numHybridSubBands[i] * defaultOverlaps[i]);

            result[i] = Math.rint(result[i] / 1.0E3) * 1.0E3;
        }

        return result;
    }

    /**
     * Get the array of numbers of channels taking into account the default
     * overlaps for the corresponding bandwidths.
     *
     * @see #getDefaultOverlapBandWidths()
     */
    public int[] getDefaultOverlapChannels() {
        int[] result = new int[channels.length];
        double channelOverlap;

        for (int i = 0; i < channels.length; i++) {
            channelOverlap = channels[i] * (defaultOverlaps[i] / bandWidths[i]);
            result[i] = (int) (channels[i]
                    - (numHybridSubBands[i] * channelOverlap));
        }

        return result;
    }

    /**
     * Number of hybrid subbands for a given bandwidth choice.
     *
     * A BandSpec contains an array of bandwidths. Some of these bandwidths are
     * the result of joining a number of hybrid subbands together. This method
     * returns the number of hybrid subbands of the bandwidth whose index in the
     * bandwidth array is given as the argument <i>bandWidthIndex</i>
     */
    public int getNumHybridSubBands(int bandWidthIndex) {
        return numHybridSubBands[bandWidthIndex];
    }

    /**
     * Index of this overlap bandwidth.
     *
     * The method is useful if a bandwidth which is reduced by some overlap is
     * given and the channel number (with or without overlap) or the original
     * bandwidth are needed. Using the returned index these can be accessed via
     * other methods and fields.
     */
    public int getDefaultOverlapBandWidthIndex(double defaultOverlapBandWidth) {
        double[] allDefaultOverlapBandWidths = getDefaultOverlapBandWidths();

        for (int i = 0; i < allDefaultOverlapBandWidths.length; i++) {
            if (allDefaultOverlapBandWidths[i] == defaultOverlapBandWidth) {
                return i;
            }
        }

        throw new IllegalStateException(
                "Could not find subsystem with default overlap bandwith "
                        + defaultOverlapBandWidth);
    }

    /**
     * Index of these number of channels which are reduced according to an
     * overlap.
     *
     * The method is useful if a number of channels which is reduced by some
     * overlap is given and the bandwidth (with or without overlap) or the
     * original number of channels are needed. Using the returned index these
     * can be accessed via other methods and fields.
     */
    public int getDefaultOverlapChannelsIndex(int defaultOverlapChannels) {
        int[] allDefaultOverlapChannels = getDefaultOverlapChannels();

        for (int i = 0; i < allDefaultOverlapChannels.length; i++) {
            if (allDefaultOverlapChannels[i] == defaultOverlapChannels) {
                return i;
            }
        }

        throw new IllegalStateException(
                "Could not find subsystem with default overlap number of channels "
                        + defaultOverlapChannels);
    }
}
