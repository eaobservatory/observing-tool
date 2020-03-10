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

package orac.ukirt.util;

import gemini.sp.SpTelescopePos;
import gemini.util.CoordSys;
import orac.util.TelescopeUtil;
import orac.util.SpItemDOM;
import orac.validation.SpValidation;
import orac.ukirt.validation.UkirtSpValidation;

/**
 * Used for UKIRT specific features.
 *
 * @author Martin Folger
 */
public class UkirtUtil implements TelescopeUtil {
    private static final String[] COORD_SYS_FK5_FK4 = CoordSys.COORD_SYS;

    private static final String[] CHOP_SYSTEM = {
            CoordSys.COORD_SYS[CoordSys.FK5],
    };

    private UkirtSpValidation _ukirtSpValidation = new UkirtSpValidation();

    public SpValidation getValidationTool() {
        return _ukirtSpValidation;
    }

    public String getBaseTag() {
        return SpTelescopePos.BASE_TAG;
    }

    /**
     * @return true for REFERENCE position.
     */
    public boolean isOffsetTarget(String targetTag) {
        // If REFERENCE or SKY but not REFERENCE GUIDE or SKY GUIDE
        return (((targetTag.toUpperCase().indexOf("REFERENCE") >= 0)
                        || (targetTag.toUpperCase().indexOf("SKY") >= 0))
                && ((targetTag.toUpperCase().indexOf("GUIDE") < 0)));
    }

    public boolean supports(int feature) {
        switch (feature) {
            case FEATURE_TARGET_INFO_CHOP:
                return false;

            case FEATURE_FLAG_AS_STANDARD:
                return true;

            case FEATURE_TARGET_INFO_PROP_MOTION:
                return true;

            case FEATURE_TARGET_INFO_TRACKING:
                return false;

            case FEATURE_TLE_SYSTEM:
                return true;

            case FEATURE_OFFSET_ITER_SYSTEM_EDITABLE:
                return false;

            default:
                return false;
        }
    }

    /**
     * Sets PreTranslator in SpItemDOM.
     *
     * Make sure at the time this method is called SpTelescopePos.BASE_TAG and
     * SpTelescopePos.GUIDE_TAGS[0] are set to correct values.
     */
    public void installPreTranslator() throws Exception {
        SpItemDOM.setPreTranslator(new UkirtPreTranslator(
                SpTelescopePos.BASE_TAG, SpTelescopePos.GUIDE_TAGS[0]));
    }

    public String[] getCoordSys() {
        return COORD_SYS_FK5_FK4;
    }

    public String[] getCoordSysFor(String purpose) {
        if (purpose.equals(CHOP)) {
            return CHOP_SYSTEM;
        }

        return null;
    }
}
