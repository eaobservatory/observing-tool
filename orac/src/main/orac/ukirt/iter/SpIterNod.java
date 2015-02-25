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

package orac.ukirt.iter;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterComp;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import gemini.util.TranslationUtils;

import java.util.Enumeration;
import java.util.Vector;

/**
 * The Enumeration of the steps produced by the Nod iterator.
 */
@SuppressWarnings("serial")
class SpIterNodEnumeration extends SpIterEnumeration {
    private String[] _nodPattern;
    private int _curIndex = 0;

    SpIterNodEnumeration(SpIterNod iterComp) {
        super(iterComp);
        _nodPattern = iterComp.getNodPattern();
    }

    protected boolean _thisHasMoreElements() {
        return (_curIndex < _nodPattern.length);
    }

    protected SpIterStep _thisFirstElement() {
        return _thisNextElement();
    }

    protected SpIterStep _thisNextElement() {
        SpIterValue[] sivA = {new SpIterValue("chop beam",
                _nodPattern[_curIndex]),};
        return new SpIterStep("nod", _curIndex++, _iterComp, sivA);
    }

}

/**
 * Nod Iterator item.
 *
 * @author modified as Nod Iterator by Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SpIterNod extends SpIterComp implements SpTranslatable {
    public static final String ATTR_NOD_PATTERN = "nodPattern";
    public static String CHOP_BEAM_A = "A";
    public static String CHOP_BEAM_B = "B";
    public static String CHOP_BEAM_MIDDLE = "MIDDLE";
    public static String[][] NOD_PATTERNS = {
            {CHOP_BEAM_A, CHOP_BEAM_B},
            {CHOP_BEAM_A, CHOP_BEAM_B, CHOP_BEAM_B, CHOP_BEAM_A},
    };

    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "nod", "Nod");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterNod());
    }

    /**
     * Default constructor.
     */
    public SpIterNod() {
        super(SP_TYPE);
        _avTable.noNotifySetAll(ATTR_NOD_PATTERN,
                stringArrayToVector(NOD_PATTERNS[0]));
    }

    /**
     * Override getTitle to return the observe count.
     */
    public String getTitle() {
        if (getTitleAttr() != null) {
            return super.getTitle();
        }

        return "Nod";
    }

    /**
     */
    public SpIterEnumeration elements() {
        return new SpIterNodEnumeration(this);
    }

    /**
     * Get the nod pattern as String array.
     *
     * Each String in the array is one of {@link #CHOP_BEAM_A},
     * @link #CHOP_BEAM_B}.
     */
    public String[] getNodPattern() {
        String[] result = new String[getNodPatternVector().size()];
        getNodPatternVector().copyInto(result);

        return result;
    }

    /**
     * Get the nod pattern as Vector.
     */
    public Vector<String> getNodPatternVector() {
        return _avTable.getAll(ATTR_NOD_PATTERN);
    }

    /**
     * Set the nod pattern from a Vector.
     */
    public void setNodPattern(Vector<String> nodPattern) {
        _avTable.setAll(ATTR_NOD_PATTERN, nodPattern);
    }

    /**
     * Set the nod pattern from a String array.
     */
    public void setNodPattern(String[] nodPatternArray) {
        setNodPattern(stringArrayToVector(nodPatternArray));
    }

    /**
     * Returns Enumeration of nod pattern vectors.
     *
     * Convenience method. Useful for JComboBox in GUI.
     */
    public static Enumeration<Vector<String>> patterns() {
        Vector<Vector<String>> allPatterns = new Vector<Vector<String>>();
        Vector<String> onePattern;

        for (int i = 0; i < NOD_PATTERNS.length; i++) {
            onePattern = new Vector<String>();

            for (int j = 0; j < NOD_PATTERNS[i].length; j++) {
                onePattern.addElement(NOD_PATTERNS[i][j]);
            }

            allPatterns.addElement(onePattern);
        }

        return allPatterns.elements();
    }

    public static Vector<String> stringArrayToVector(String[] stringArray) {
        Vector<String> result = new Vector<String>();

        for (int i = 0; i < stringArray.length; i++) {
            result.addElement(stringArray[i]);
        }

        return result;
    }

    public void translateProlog(Vector<String> v) {
    }

    public void translateEpilog(Vector<String> v) {
    }

    public void translate(Vector<String> v)
            throws SpTranslationNotSupportedException {
        for (String beam: getNodPatternVector()) {
            // Translate contents of the nod iterator.
            TranslationUtils.recurse(this.children(), v);
        }
    }
}
