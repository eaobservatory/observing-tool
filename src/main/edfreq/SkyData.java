/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
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

import orac.jcmt.util.HeterodyneNoise;

/**
 * @author Dennis Kelly (bdk@roe.ac.uk)
 */
public class SkyData {
    public double[][] getTransmission(double minFreq, double maxFreq) {
        /*
         * This class previously contained its own transmission model data,
         * while the HeterodyneNoise class also had access to opacity model
         * data.  The model here approximately matched the transmission
         * given by the opacity model for 0.065 at 225GHz and an airmass of
         * 1.2.  Therefore obtain similar information from HeterodyneNoise.
         */
        double[][] atmosphere = HeterodyneNoise.getTransmissionModel(0.065, 1.2);
        if (atmosphere == null) {
            return null;
        }

        int start;
        int endj;
        int j;
        double[][] extract = null;

        minFreq = minFreq * 1.0E-9;
        maxFreq = maxFreq * 1.0E-9;
        start = -1;

        for (j = 0; j < atmosphere.length; j++) {
            if (atmosphere[j][0] > maxFreq) {
                break;
            }

            if ((atmosphere[j][0] >= minFreq) && (start == -1)) {
                start = j;
            }
        }
        endj = j - 1;

        if (endj >= start) {
            boolean interpolate_start = (start > 0);
            boolean interpolate_end = (endj < (atmosphere.length - 1));
            int start_offset = (interpolate_start ? 1 : 0);

            extract = new double[endj - start + 1 + start_offset + (interpolate_end ? 1 : 0)][2];

            if (interpolate_start) {
                extract[0][0] = minFreq * 1.0E9;
                extract[0][1] = atmosphere[start - 1][1]
                    + (atmosphere[start][1] - atmosphere[start - 1][1])
                        * (minFreq - atmosphere[start - 1][0])
                        / (atmosphere[start][0] - atmosphere[start - 1][0]);
            }

            for (j = start; j <= endj; j++) {
                extract[j + start_offset - start][0] = atmosphere[j][0] * 1.0E9;
                extract[j + start_offset - start][1] = atmosphere[j][1];
            }

            if (interpolate_end) {
                extract[extract.length - 1][0] = maxFreq * 1.0E9;
                extract[extract.length - 1][1] = atmosphere[endj][1]
                    + (atmosphere[endj + 1][1] - atmosphere[endj][1])
                        * (maxFreq - atmosphere[endj][0])
                        / (atmosphere[endj + 1][0] - atmosphere[endj][0]);
            }
        }

        return extract;
    }
}
