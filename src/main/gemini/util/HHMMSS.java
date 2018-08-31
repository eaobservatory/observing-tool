/*
 * NCSA Horizon Image Browser
 * Project Horizon
 * National Center for Supercomputing Applications
 * University of Illinois at Urbana-Champaign
 * 605 E. Springfield, Champaign IL 61820
 * horizon@ncsa.uiuc.edu
 *
 * Copyright (C) 1996, Board of Trustees of the University of Illinois
 *
 * NCSA Horizon software, both binary and source (hereafter, Software) is
 * copyrighted by The Board of Trustees of the University of Illinois
 * (UI), and ownership remains with the UI.
 *
 * You should have received a full statement of copyright and
 * conditions for use with this package; if not, a copy may be
 * obtained from the above address.  Please see this statement
 * for more details.
 *
 * Modifications:
 *
 * This is a modified version and not the original NCSA Horizon
 * HHMMSSCoordAxisPos / HHMMSSAxisPosFormatter class distributed by the UI.
 *
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
 *
 * Copyright (C) 2007-2008 Science and Technology Facilities Council.
 */

package gemini.util;

/**
 * Support for converting angles in hours:minutes:seconds format over the
 * circular range 0, 24 hours.
 */
public final class HHMMSS extends XXMMSS {
    public static final String MYNAME =
            "Time-Angle Coordinate Axis Position Formatter";

    /**
     * @param candidate
     * @return boolean indicating wether candidate was in a valid RA HH:MM:SS
     *         format
     */
    public static boolean validFormat(String candidate) {
        return (candidate != null
                && (candidate.matches(rapattern)
                        || candidate.matches(rapatterncolon)));
    }

    /**
     * Convert from an RA in degrees to a String in HH:MM:SS format.
     */
    public static String valStr(double degrees, int prec) {
        // Make sure the angle is between 0 (inclusive) and 360 (exclusive)
        degrees = Angle.normalizeDegrees(degrees);

        double tmp = degrees / 15.0;

        int hh = (int) tmp;
        tmp = (tmp - (double) hh) * 60.0;
        int mm = (int) tmp;
        double ss = (tmp - (double) mm) * 60.0;

        // correct for formating errors caused by rounding
        if (ss > 59.99999) {
            ss = 0;
            mm += 1;

            if (mm >= 60) {
                mm = 0;
                hh += 1;

                if (hh >= 24) {
                    hh -= 24.0;
                }
            }
        }

        StringBuffer out = new StringBuffer();

        out.append(hh);

        if (prec == -2) {
            return out.toString();
        }

        out.append(':');

        if (mm < 10) {
            out.append('0');
        }

        out.append(mm);

        if (prec == -1) {
            return out.toString();
        }

        out.append(':');

        // Ignoring prec for now.
        ss = ((double) Math.round(ss * 1000.0)) / 1000.0;

        if (ss < 10) {
            out.append('0');
        }

        out.append(ss);

        return out.toString();
    }

    /**
     * Convert from an RA in degrees to a String in HH:MM:SS format.
     */
    public static String valStr(double degrees) {
        return valStr(degrees, -3);
    }

    /**
     * Convert from an RA in HH:MM:SS string format to degrees.
     */
    public static double valueOf(String s) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException(s);
        }

        // Determine the sign from the (trimmed) string
        s = s.trim();

        if (s.length() == 0) {
            throw new NumberFormatException(s);
        }

        int sign = 1;

        if (s.charAt(0) == '-') {
            sign = -1;
            s = s.substring(1);
        }

        double[] vals = stringTodoubleTriplet(s);

        double out = sign
                * (vals[0] + vals[1] / 60.0 + vals[2] / 3600.0) * 15.0;

        return Angle.normalizeDegrees(out);
    }

    /**
     * Check wether a string is in the correct HH:MM:SS format and within
     * range.
     *
     * @param hhmmss
     * @return boolean indicating validity
     */
    public static boolean validate(String hhmmss) {
        boolean valid = true;

        if (valid = validFormat(hhmmss)) {
            double[] values = stringTodoubleTriplet(hhmmss);

            double hours = values[0];
            double minutes = values[1];
            double seconds = values[2];

            if (hours < 0 || hours >= 24
                    || minutes < 0 || minutes >= 60
                    || seconds < 0 || seconds >= 60) {
                valid = false;
            }
        }

        return valid;
    }

    /**
     * For testing.
     */
    public static void main(String args[]) {
        for (int i = 0; i < args.length; ++i) {
            double converted = HHMMSS.valueOf(args[i]);
            String back = HHMMSS.valStr(converted);

            System.out.println(args[i] + " = " + converted);
            System.out.println(converted + " = " + back);
        }
    }
}
