/*
 * Copyright (C) 2007-2012 Science and Technology Facilities Council.
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

package orac.util;

import java.lang.UnsupportedOperationException;

import uk.ac.starlink.pal.Pal;
import uk.ac.starlink.pal.AngleDR;
import uk.ac.starlink.pal.Stardata;
import uk.ac.starlink.pal.Galactic;

import gemini.util.RADec;
import gemini.util.CoordSys;

public class CoordConvert {
    private static Pal pal = new Pal();

    private static int rounding = 5;

    /**
     * Convert between coordinate systems.
     *
     * @param x first coordinate in input system
     * @param y second coordinate in input system
     * @param inputSystem integer constant from CoordSys
     * @param targetSystem integer constant from CoordSys
     *
     * @return gemini.util.RADec as defined for the functions below
     *
     * @throws UnsupportedOperationException if the requested conversion
     *          has not been implemented
     */
    public static RADec convert(double x, double y, int inputSystem,
            int targetSystem) throws UnsupportedOperationException {

        if (inputSystem == targetSystem) {
            return new RADec(targetSystem, x, y);

        } else if (inputSystem == CoordSys.FK5
                && targetSystem == CoordSys.FK4) {
            return CoordConvert.Fk54z(x, y);

        } else if (inputSystem == CoordSys.FK4
                && targetSystem == CoordSys.FK5) {
            return CoordConvert.Fk45z(x, y);

        } else if (inputSystem == CoordSys.FK5
                && targetSystem == CoordSys.GAL) {
            return CoordConvert.fk52gal(x, y);

        } else if (inputSystem == CoordSys.GAL
                && targetSystem == CoordSys.FK5) {
            return CoordConvert.gal2fk5(x, y);

        } else if (inputSystem == CoordSys.GAL
                && targetSystem == CoordSys.FK4) {
            return CoordConvert.gal2fk4(x, y);

        } else if (inputSystem == CoordSys.FK4
                && targetSystem == CoordSys.GAL) {
            return CoordConvert.fk42gal(x, y);
        }

        throw new UnsupportedOperationException("Cannot convert "
                + CoordSys.COORD_SYS[inputSystem]
                + " to "
                + CoordSys.COORD_SYS[targetSystem]);
    }

    private static RADec FkXXz(double ra, double dec, int targetSystem) {
        RADec raDec = null;
        double raInRadians = Math.toRadians(ra);
        double decInRadians = Math.toRadians(dec);

        AngleDR angleDR = new AngleDR(raInRadians, decInRadians);

        if (targetSystem == CoordSys.FK4) {
            Stardata stardata = pal.Fk54z(angleDR, 1950.0);
            angleDR = stardata.getAngle();

        } else if (targetSystem == CoordSys.FK5) {
            // 1950 matches coco's output
            angleDR = pal.Fk45z(angleDR, 1950.0);

        } else {
            throw new RuntimeException("Unrecognised target system");
        }

        raInRadians = angleDR.getAlpha();
        decInRadians = angleDR.getDelta();

        ra = Math.toDegrees(raInRadians);
        dec = Math.toDegrees(decInRadians);

        raDec = new RADec(targetSystem, ra, dec);

        return raDec;
    }

    /**
     * Convert from FK5 to FK4
     *
     * @param ra
     * @param dec
     *
     * @return gemini.util.RADec
     */
    public static RADec Fk54z(double ra, double dec) {
        return FkXXz(ra, dec, CoordSys.FK4);
    }

    /**
     * Convert from FK4 to FK5
     *
     * @param ra
     * @param dec
     *
     * @return gemini.util.RADec
     */
    public static RADec Fk45z(double ra, double dec) {
        return FkXXz(ra, dec, CoordSys.FK5);
    }

    /**
     * Convert from FK5 to Galactic
     *
     * @param ra
     * @param dec
     *
     * @return gemini.util.RADec representing theta and phi
     */
    public static RADec fk52gal(double ra, double dec) {
        RADec raDec = null;

        double raInRadians = Math.toRadians(ra);
        double decInRadians = Math.toRadians(dec);

        AngleDR angleDR = new AngleDR(raInRadians, decInRadians);

        Galactic galactic = pal.Eqgal(angleDR);

        raInRadians = galactic.getLongitude();
        decInRadians = galactic.getLatitude();

        ra = Math.toDegrees(raInRadians);
        dec = Math.toDegrees(decInRadians);

        ra = round(ra, rounding);
        dec = round(dec, rounding);

        raDec = new RADec(CoordSys.GAL, ra, dec);

        return raDec;
    }

    /**
     * Convert from Galactic to FK5
     *
     * @param theta
     * @param phi
     *
     * @return gemini.util.RADec
     */
    public static RADec gal2fk5(double theta, double phi) {
        RADec raDec = null;

        double raInRadians = Math.toRadians(theta);
        double decInRadians = Math.toRadians(phi);

        Galactic galactic = new Galactic(raInRadians, decInRadians);

        AngleDR angleDR = pal.Galeq(galactic);

        raInRadians = angleDR.getAlpha();
        decInRadians = angleDR.getDelta();

        double ra = Math.toDegrees(raInRadians);
        double dec = Math.toDegrees(decInRadians);

        ra = round(ra, rounding);
        dec = round(dec, rounding);

        raDec = new RADec(CoordSys.FK5, ra, dec);

        return raDec;
    }

    /**
     * Convert from FK4 to Galactic via FK5
     *
     * @param ra
     * @param dec
     *
     * @return gemini.util.RADec representing theta and phi
     */
    public static RADec fk42gal(double ra, double dec) {
        RADec raDec = null;

        raDec = Fk45z(ra, dec);
        raDec = fk52gal(raDec.ra, raDec.dec);

        raDec = new RADec(CoordSys.GAL, raDec.ra, raDec.dec);

        return raDec;
    }

    /**
     * Convert from Galactic to FK4 via FK5
     *
     * @param ra
     * @param dec
     *
     * @return gemini.util.RADec
     */
    public static RADec gal2fk4(double theta, double phi) {
        RADec raDec = null;

        raDec = gal2fk5(theta, phi);
        raDec = Fk54z(raDec.ra, raDec.dec);

        raDec = new RADec(CoordSys.FK4, raDec.ra, raDec.dec);

        return raDec;
    }

    private static double[] spherical2Cartesian(double ra, double dec) {
        double x = 1.0 * Math.cos(ra) * Math.cos(dec);
        double y = 1.0 * Math.sin(ra) * Math.cos(dec);
        double z = 1.0 * Math.sin(dec);

        return new double[]{x, y, z};
    }

    private static double[] cartesian2Spherical(double x, double y, double z) {
        double rxy, rxy2;
        double ra, dec;

        ra = Math.atan2(y, x);
        if (ra < 0.0) {
            ra += 6.283185307179586;
        }

        rxy2 = x * x + y * y;
        rxy = Math.sqrt(rxy2);
        dec = Math.atan2(z, rxy);

        return new double[]{ra, dec};
    }

    /**
     * Round double-precision "input" to integer "precision" decimal places
     *
     * @param input
     * @param precision
     *
     * @return rounded double
     */
    public static double round(double input, int precision) {
        double multiplicand = Math.pow(10, precision);
        double intermediate = Math.rint(input * multiplicand);
        double output = intermediate / multiplicand;
        return output;
    }
}
