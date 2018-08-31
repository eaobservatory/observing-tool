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

import orac.util.TcsPreTranslator;

/**
 * XML PreTranslator for JCMT.
 *
 * Applies/removes JCMT specific changes to/from {@link orac.util.SpItemDOM}
 * XML.
 *
 * @author Martin Folger
 */
public class JcmtPreTranslator extends TcsPreTranslator {
    private static final String[] TCS_TARGET_TYPES = {
            "science",
            "reference",
    };

    public JcmtPreTranslator(String scienceTag, String referenceTag)
            throws Exception {
        super(scienceTag, referenceTag);
    }

    /**
     * Target types used by the JAC OCS TCS for JCMT XML.
     *
     * The target types are "science" and "reference".
     */
    protected String[] getTcsTargetTypes() {
        return TCS_TARGET_TYPES;
    }
}
