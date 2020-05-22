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

package orac.jcmt.util;

import gemini.util.CoordSys;
import orac.util.TelescopeUtil;
import orac.util.SpItemDOM;
import orac.validation.SpValidation;
import orac.jcmt.validation.JcmtSpValidation;
import orac.jcmt.SpJCMTConstants;

/**
 * Used for JCMT specific features.
 *
 * @author Martin Folger
 */
public class JcmtUtil implements TelescopeUtil {

    public static final String CHOP = "chop";

    // There is no proper JCMT validation class yet. Use the Generic class
    // instead.
    private JcmtSpValidation _spValidation = new JcmtSpValidation();

    private String[] _targetTags = {
            "SCIENCE",
            "REFERENCE",
    };

    public SpValidation getValidationTool() {
        return _spValidation;
    }

    public String getBaseTag() {
        return _targetTags[0];
    }

    /**
     * @return always false for UKIRT
     */
    public boolean isOffsetTarget(String targetTag) {
        return ((targetTag != null)
                && (targetTag.equalsIgnoreCase("reference")));
    }

    public boolean supports(int feature) {
        switch (feature) {
            case FEATURE_TARGET_INFO_CHOP:
                return false;
            case FEATURE_FLAG_AS_STANDARD:
                return false;
            case FEATURE_TARGET_INFO_PROP_MOTION:
                return true;
            case FEATURE_TARGET_INFO_TRACKING:
                return true;
            case FEATURE_OFFSET_GRID_PA:
                return true;
            case FEATURE_TLE_SYSTEM:
                return false;
            case FEATURE_OFFSET_ITER_SYSTEM_EDITABLE:
                return true;
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
        SpItemDOM.setPreTranslator(new JcmtPreTranslator(_targetTags[0],
                _targetTags[1]));
    }

    public String[] getCoordSys() {
        return CoordSys.COORD_SYS;
    }

    public String[] getCoordSysFor(String purpose) {
        if (purpose.equals(CHOP)) {
            return SpJCMTConstants.CHOP_SYSTEMS;
        }

        return null;
    }
}
