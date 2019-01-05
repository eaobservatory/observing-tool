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

package gemini.util;

/**
 * A data type representing an RA and Dec pair in degrees.
 */
public class RADec {
    /** The coordinate system in which the RA and Dec are represented. */
    public int coordSystem = CoordSys.FK5;

    /** Right ascension in degrees. */
    public double ra = 0.0;

    /** Declination in degrees. */
    public double dec = 0.0;

    /** Proper motion in right ascension. */
    public double rapm = 0.0;

    /** Proper motion in declination. */
    public double decpm = 0.0;

    /**
     * Default constructor.
     */
    public RADec() {
    }

    /**
     * Construct with most of the fields.
     */
    public RADec(int sys, double ra, double dec) {
        switch (sys) {
            case CoordSys.FK5:
            case CoordSys.FK4:
                this.coordSystem = sys;
                break;

            default:
                this.coordSystem = CoordSys.FK5;
        }

        this.ra = ra;
        this.dec = dec;
    }

    /**
     * Construct with all of the fields.
     */
    public RADec(int sys, double ra, double dec, double rapm, double decpm) {
        this(sys, ra, dec);
        this.rapm = rapm;
        this.decpm = decpm;
    }
}
