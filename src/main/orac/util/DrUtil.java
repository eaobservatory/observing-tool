/*
 * Copyright 2002 United Kingdom Astronomy Technology Centre, an
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

package orac.util;

/**
 * Utility class for calculations related to data reduction issues.
 *
 * The methods {@link #airmass(double,double)} and
 * {@link #transmission(double,double,int[])} deal with sky opacity related
 * topics and are based on the perl module JCMT::Tau.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk):        Java version
 * @author Edward Chapin (echapin@jach.hawaii.edu):   Perl version and documentation
 * @author Tim Jenness   (t.jenness@jach.hawaii.edu): Perl version and documentation
 */
public class DrUtil {
    public static final int STATUS_SUCCESSFUL = 0;

    /** Failure due to invalid parameters. */
    public static final int STATUS_INVALID_PARAMETERS = -1;

    /** Bad value because transmission out of range of fit. */
    public static final int STATUS_OUT_OF_RANGE_OF_FIT = -2;

    /**
     * Incorrect number of parameters.
     *
     * Example a mode has been specified that requires a different version of
     * the method, one with a different argument list.
     */
    public static final int STATUS_INCORRECT_ARGUMENT_LIST = -3;

    /** Calculation failed for whatever reason. */
    public static final int STATUS_FAILED = -4;

    /**
     * Calculates an airmass estimate based on dec of target and telescope
     * latitude.
     *
     * @param  dec      Dec of target in degrees
     * @param  latitude Latitude of telescope in degrees
     *
     * @return airmass estimate
     */
    public static double airmass(double dec, double latitude) {
        return 1.0 / (0.9 * Math.cos(((dec - latitude) * Math.PI) / 180.0));
    }

    /**
     * Calculates an airmass estimate based on el of target.
     *
     * @param  el Elevation of target in degrees
     *
     * @return airmass estimate
     */
    public static double airmass(double el) {
        double airmass = 0.0;

        if (el != 0.0) {
            airmass = Math.abs(1.0 / Math.cos(el));
        }

        return airmass;
    }

    /**
     * Sky transmission coefficient.
     *
     * @param airmass Airmass
     * @param tau     Sky opacity at whatever wavelength is desired.
     * @param status  integer array to hold one status integer.
     *
     * @return Atmospheric transmission coefficient at whatever wavelength the
     *         sky opacity applied to.
     *
     * {@link #STATUS_SUCCESSFUL}, {@link #STATUS_FAILED}
     */
    public static double transmission(double airmass, double tau,
            int[] status) {
        // check validity of airmass and tau
        if ((airmass < 1.0) || (tau < 0.0)) {
            status[0] = STATUS_FAILED;
            return 0.0;
        }

        // Finally, return the transmission coefficient as a function of
        // airmass and zenith sky opacity

        status[0] = STATUS_SUCCESSFUL;
        return StrictMath.exp((-tau) * airmass);
    }
}
