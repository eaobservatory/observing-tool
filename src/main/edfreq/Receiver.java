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

import java.util.Vector;

/**
 * @author Dennis Kelly (bdk@roe.ac.uk)
 */
public class Receiver {
    public String name; // receiver name
    public double loMin; // minimum value of local oscillator (Hz)
    public double loMax; // maximum value of local oscillator (Hz)
    public double feIF; // central IF delivered (Hz)
    public double bandWidthLower; // half-bandwidth of IF (Hz) below preferred value
    public double bandWidthUpper; // half-bandwidth of IF (Hz) above preferred value
    public Vector<BandSpec> bandspecs; // list of possible band specs

    public Receiver(String name, double loMin, double loMax, double feIF,
            double bandWidthLower, double bandWidthUpper) {
        this.name = name;
        this.loMin = loMin;
        this.loMax = loMax;
        this.feIF = feIF;
        this.bandWidthLower = bandWidthLower;
        this.bandWidthUpper = bandWidthUpper;
        bandspecs = new Vector<BandSpec>();
    }

    public void setBandSpecs(Vector<BandSpec> v) {
        bandspecs = v;
    }

    public BandSpec getBandSpec(String name) {
        for (BandSpec spec: bandspecs) {
            if (spec.name.equals(name)) {
                return spec;
            }
        }

        return null;
    }

    /**
     * Find a BandSpec with no more than the given number of CMs.
     *
     * If nothing is found, the BandSpec using the fewest CMs is returned.
     */
    public BandSpec getBandSpecByCMs(int maxCMs) {
        BandSpec maxMatch = null;
        BandSpec min = null;

        for (BandSpec spec: bandspecs) {
            int numCMs = spec.getMaxNumCMs();

            if (numCMs <= maxCMs) {
                if ((maxMatch == null ) || (numCMs > maxMatch.getMaxNumCMs())) {
                    maxMatch = spec;
                }
            }

            if ((min == null ) || (numCMs < min.getMaxNumCMs())) {
                min = spec;
            }
        }

        return (maxMatch != null) ? maxMatch : min;
    }

    public String toString() {
        String rtn = "[name=" + name
                + " ; loMin=" + loMin
                + " ; loMax=" + loMax
                + " ; feIF=" + feIF
                + " ; bandWidthLower=" + bandWidthLower
                + " ; bandWidthUpper=" + bandWidthUpper
                + " ; bandSpecs=" + bandspecs + "]";
        return rtn;
    }
}
