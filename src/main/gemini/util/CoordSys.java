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
 * Utility class support for the coordinate systems supported by Gemini.
 *
 * <b>This class and other changes to support multiple coordinate systems have
 * not been completed.</b>
 */
public class CoordSys {
    // The possible coordinate systems.
    public static final int FK5 = 0;
    public static final int FK4 = 1;
    public static final int AZ_EL = 2;
    public static final int GAL = 3;
    public static final int HADEC = 4;

    public static final String FK5_STRING = "FK5 (J2000)";
    public static final String FK4_STRING = "FK4 (B1950)";
    public static final String AZ_EL_STRING = "Az/El";
    public static final String GAL_STRING = "Galactic";
    public static final String HADEC_STRING = "HADEC";

    public static final String FK5_SHORT_STRING = "FK5";
    public static final String FK4_SHORT_STRING = "FK4";
    public static final String AZ_EL_SHORT_STRING = "AZ";
    public static final String GAL_SHORT_STRING = "GAL";
    public static final String HADEC_SHORT_STRING = "HA";

    /**
     * Readable coordinate system strings.
     */
    public static final String[] COORD_SYS = {
            FK5_STRING,
            FK4_STRING,
            AZ_EL_STRING,
            GAL_STRING,
            HADEC_STRING,
    };
    public static final String[] SHORT_COORD_SYS = {
            FK5_SHORT_STRING,
            FK4_SHORT_STRING,
            AZ_EL_SHORT_STRING,
            GAL_SHORT_STRING,
            HADEC_SHORT_STRING,
    };

    // MFO (March 08, 2002)
    /**
     * Coordinate System x axis labels.
     */
    public static final String[] X_AXIS_LABEL = {
            "Ra",
            "Ra",
            "Az",
            "Long",
            "HA",
    };

    // MFO (March 08, 2002)
    /**
     * Coordinate System y axis labels.
     */
    public static final String[] Y_AXIS_LABEL = {
            "Dec",
            "Dec",
            "El",
            "Lat",
            "Dec",
    };

    /**
     * Get an integer representing a coordinate system from its associated
     * string.
     *
     * If the string is not recognizable, return -1.
     */
    public static int getSystem(String coordSysString) {
        for (int i = 0; i < SHORT_COORD_SYS.length; i++) {
            if (coordSysString.equals(SHORT_COORD_SYS[i])) {
                return i;
            }
        }

        for (int i = 0; i < COORD_SYS.length; i++) {
            if (coordSysString.equals(COORD_SYS[i])) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Get the string representing a coordinate system from its associated int.
     *
     * If the int is out of range, return null.
     */
    public static String getSystem(int coordSysInt) {
        if ((coordSysInt < 0) || (coordSysInt >= COORD_SYS.length)) {
            return null;
        }

        return COORD_SYS[coordSysInt];
    }

    /**
     * Get the string representing the x axis label for a given coordinate
     * system from its associated int.
     *
     * If the int is out of range, return null.
     */
    public static String getXAxisLabel(int coordSysInt) {
        if ((coordSysInt < 0) || (coordSysInt >= X_AXIS_LABEL.length)) {
            return null;
        }

        return X_AXIS_LABEL[coordSysInt];
    }

    /**
     * Get the string representing the y axis label for a given coordinate
     * system from its associated int.
     *
     * If the int is out of range, return null.
     */
    public static String getYAxisLabel(int coordSysInt) {
        if ((coordSysInt < 0) || (coordSysInt >= Y_AXIS_LABEL.length)) {
            return null;
        }

        return Y_AXIS_LABEL[coordSysInt];
    }
}
