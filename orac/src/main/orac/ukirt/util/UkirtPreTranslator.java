/*
 * Copyright 2002 United Kingdom Astronomy Technology Centre, an
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

import orac.util.TcsPreTranslator;

/**
 * XML PreTranslator for UKIRT.
 *
 * Applies/removes UKIRT specific changes to {@link orac.util.SpItemDOM} XML.
 *
 * @author Martin Folger
 */
public class UkirtPreTranslator extends TcsPreTranslator {
    private static final String[] TCS_TARGET_TYPES = {"science", "guide"};

    public UkirtPreTranslator(String baseTag, String guideTag) throws Exception {
        super(baseTag, guideTag);
    }

    /**
     * Target types used by the JAC OCS TCS for JCMT XML.
     *
     * The target types are
     * <pre>
     *   "science" (Base position)
     *   "guide"   (Guide star position)
     * </pre>
     *
     * Note that the tag which is defined as "Base" in ot.cfg and also
     * displayed as "Base" in the OT is changed to "science" in the TCS XML
     * output to avoid confusion with the existing &lt;base&gt; element in
     * the TCS XML.
     */
    protected String[] getTcsTargetTypes() {
        return TCS_TARGET_TYPES;
    }
}
