/*
 * Copyright (C) 2003-2009 Science and Technology Facilities Council.
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

public class AirmassUtilities {
    private static final double PiDividedBy180 = Math.PI / 180.0;

    /**
     * @param args
     */
    public static void main(String[] args) {
        for (double elevation = 5.0; elevation < 90.0; elevation += 0.25) {
            double airmass = elevationToAirmass(elevation);
            double newElevation = airmassToElevation(airmass);
            newElevation = gemini.util.MathUtil.round(newElevation, 3);

            if (elevation != newElevation) {
                System.out.println("Couldn't do roundtrip for " + elevation
                        + " != " + newElevation);
            }
        }
    }

    public static double elevationToAirmass(double elevation) {
        // If the elevation is < 5 degrees set the airmass to 12
        double airmass;

        if (elevation < 5.0) {
            airmass = 12.0;
        } else {
            // Convert elevation to radians
            elevation *= PiDividedBy180;
            airmass = 1.0 / Math.sin(elevation);
        }

        return airmass;
    }

    public static double airmassToElevation(double airmass) {
        if (Math.abs(airmass) < 1.0) {
            airmass = 1.0;
        }

        double elevation = Math.asin(1.0 / airmass);

        // Convert to degrees
        elevation = 180.0 * elevation / Math.PI;

        return elevation;
    }

}
