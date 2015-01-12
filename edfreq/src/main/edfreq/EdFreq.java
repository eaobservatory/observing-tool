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
 * Utility class for frequency editor.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk),
 *         based on Dennis Kelly (bdk@roe.ac.uk)
 */
public class EdFreq {
    public static double SLIDERSCALE = 1.0E7;
    public static double LIGHTSPEED = 2.99792458E5;
    public final static int SIDE_BAND_USB = 0;
    public final static int SIDE_BAND_LSB = 1;
    public static final int DISPLAY_WIDTH = 800;

    /**
     * Calculates the rest frequency based on observe frequency and redshift.
     *
     * @param restFrequency rest frequency in GHz
     * @param redshift      redshift
     *
     * @return observe frequency in GHz
     */
    public static double getObsFrequency(double restFrequency,
            double redshift) {
        return restFrequency / (1.0 + redshift);
    }

    /**
     * Calculates the frequency in the rest frame of the source given an
     * observe frequency and redshift.
     *
     * @param obsFrequency observe frequency in GHz
     * @param redshift     redshift
     *
     * @return source frequency in GHz
     */
    public static double getRestFrequency(double obsFrequency,
            double redshift) {
        return obsFrequency * (1.0 + redshift);
    }

    /**
     * Calculates the frequency at which a source is observed.
     *
     * @param lo1        local oscillator in GHz
     * @param intermFreq IF in GHz of the sideband (of the backend) in GHz
     * @param band       lsb (lower side band) vs usb (upper side band)
     *
     * @return observe frequency in GHz
     */
    public static double getObsFrequency(double lo1, double intermFreq,
            String band) {
        if (band.equals("usb") || band.equals("best")) {
            return lo1 + intermFreq;
        } else {
            return lo1 - intermFreq;
        }
    }

    /**
     * Calculates the frequency in the rest frame of the source observed at a
     * given lo1, IF and redshift.
     *
     * @param lo1        local oscillator in GHz
     * @param intermFreq IF in GHz of the sideband (of the backend) in GHz
     * @param redshift   redshift
     * @param band       lsb (lower side band) vs usb (upper side band)
     *
     * @return source frequency in GHz
     */
    public static double getRestFrequency(double lo1, double intermFreq,
            double redshift, String band) {
        return getRestFrequency(
                getObsFrequency(lo1, intermFreq, band),
                redshift);
    }

    /**
     * Calculates local oscillator frequency in GHz.
     *
     * @param obsFrequency observe frequency in GHz
     * @param intermFreq   IF in GHz of the sideband (of the backend) in GHz
     * @param band         lsb (lower side band) vs usb (upper side band)
     */
    public static double getLO1(double obsFrequency, double intermFreq,
            String band) {
        if (band.equals("usb") || band.equals("best")) {
            return obsFrequency - intermFreq;
        } else {
            return obsFrequency + intermFreq;
        }
    }
}
