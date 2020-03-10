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

package orac.util;

import orac.validation.SpValidation;

/**
 * Used for telescope specific features.
 *
 * The config file (ot.cfg) is used to specify plug-in like classes (such as
 * instruments etc) and other things.
 *
 * TelescopeUtil classes are used to make telescope specific settings and
 * initialize telescope specific classes such as the validation tool and
 * pre-translator. This means that users do not have be responsible for
 * maintaining the correct full class names of all these telescope specific
 * in the ot.cfg file.
 *
 * @author Martin Folger
 */
public interface TelescopeUtil {
    /**
     * Target Information Component, Tab "Chop Settings".
     *
     * Do not confuse with Telescope Position Editor features.
     */
    public static final int FEATURE_TARGET_INFO_CHOP = 0;

    /**
     * Target Information Component, Tab "Proper Motion".
     *
     * Do not confuse with Telescope Position Editor features.
     */
    public static final int FEATURE_TARGET_INFO_PROP_MOTION = 1;

    /**
     * Target Information Component, Tab "Tracking Details".
     *
     * Do not confuse with Telescope Position Editor features.
     */
    public static final int FEATURE_TARGET_INFO_TRACKING = 2;

    /**
     * Offset iterator, PA.
     */
    public static final int FEATURE_OFFSET_GRID_PA = 3;

    /**
     * "Flag as standard" option on Observation component.
     */
    public static final int FEATURE_FLAG_AS_STANDARD = 4;

    /**
     * Support for the TLE system.
     */
    public static final int FEATURE_TLE_SYSTEM = 5;

    public static final int FEATURE_OFFSET_ITER_SYSTEM_EDITABLE = 6;

    public static final String CHOP = "chop";

    /**
     * TCS radial velocity definitions.
     */
    public static final String[] TCS_RV_DEFINITIONS = {
            "radio",
            "optical",
            "redshift",
    };

    /**
     * TCS radial velocity frames.
     */
    public static final String[] TCS_RV_FRAMES = {
            "LSRK",
            "HELIOCENTRIC",
            "BARYCENTRIC",
            "GEOCENTRIC",
            "TOPOCENTRIC",
    };

    public SpValidation getValidationTool();

    /**
     * Get telescope specific base tag.
     *
     * For example "Base" for UKIRT and "Science" for JCMT.
     */
    public String getBaseTag();

    /**
     * Returns true if the user input for targetTag
     * should be interpreted as offsets.
     *
     * E.g. "reference" position for ACSIS/JCMT.
     */
    public boolean isOffsetTarget(String targetTag);

    public boolean supports(int feature);

    public void installPreTranslator() throws Exception;

    /**
     * Returns an array of default coordinates.
     *
     * To be used to target information component.
     */
    public String[] getCoordSys();

    /**
     * Returns an array of coordinate system for a given purpose.
     *
     * @param purpose E.g. Chop, Jiggle, Offset etc.
     */
    public String[] getCoordSysFor(String purpose);
}
