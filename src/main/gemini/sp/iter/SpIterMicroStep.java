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

package gemini.sp.iter;

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpTreeMan;

import gemini.sp.iter.SpIterOffset;

import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpMicroStepUser;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import gemini.util.MathUtil;

/**
 * Microstep Iterator item.
 *
 * The microstep iterator is similar to the offset iterator. The differences
 * are:
 *
 * <ul>
 * <li> If a microstep iterator is inside an offset iterator and if it is the
 * only item inside the offset iterator then the offset commands in the exec
 * file created by the translator are net offsets from the base position, e.g.
 *
 * <pre>
 * exec offset command 1 = offset iterator position 1 + microstep position 1
 * exec offset command 2 = offset iterator position 1 + microstep position 2
 * exec offset command 3 = offset iterator position 1 + microstep position 3
 * exec offset command 4 = offset iterator position 1 + microstep position 4
 * exec offset command 5 = offset iterator position 2 + microstep position 1
 * exec offset command 6 = offset iterator position 2 + microstep position 2
 * ...
 * </pre>
 *
 * <li> In order to retain a record of the nested nature of the above net
 * offsets the translator adds FITS the following FITS headers (taken from
 * document <i>WFCAM HDS container and FITS headers</i> by Alan Pickup)
 *
 * <pre>
 * NJITTER      Number of positions in telescope jitter pattern
 * JITTER_I     Serial number in this telescope jitter pattern
 * JITTER_X     [arcsec] X (RA) offset in telescope jitter pattern
 * JITTER_Y     [arcsec] Y (Dec) offset in telescope jitter pattern
 * NUSTEP       Number of positions in microstep pattern
 * USTEP_I      Serial number in this microstep pattern
 * USTEP_X      [arcsec] X (RA) offset in microstep pattern
 * USTEP_Y      [arcsec] Y (Dec) offset in microstep pattern
 * </pre>
 *
 * <li> The microstep iterator does not put any restrictions on what a
 * microstep pattern can look like and where it comes from.
 * <p>
 * However, the microstep iterator editor classes restricts the choice offered
 * to the user to the microstep patterns defined by those instrument classes
 * that implement the SpMicroStepUser interface and provide a list of
 * meaningful instrument specific microstep patterns. For all other instruments
 * the only microstep pattern option is SpIterMicroStep.NO_PATTERN.
 * <p>
 * If the microstep pattern is set to SpIterMicroStep.NO_PATTERN then a single
 * microstep postion is added at 0.0. This means that there will be one
 * "microstep" at each of the offset positions if the microstep iterator is
 * inside an offset iterator or one "microstep" at the telescope base position
 * if the microstep iterator is not inside an offset iterator. In either case
 * the microstep iterator has no effect.
 * </ul>
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SpIterMicroStep extends SpIterOffset {

    public static String NO_PATTERN = "NONE";

    public static String ATTR_PATTERN = "pattern";

    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "microstep", "MicroStep");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterMicroStep());
    }

    /**
     * Default constructor.
     */
    public SpIterMicroStep() {
        super(SP_TYPE);

        _avTable.noNotifySet(ATTR_PATTERN, NO_PATTERN, 0);
    }

    /**
     * Get the instrument item in the scope of the base item.
     */
    public SpInstObsComp getInstrumentItem() {
        SpItem _baseItem = parent();

        return SpTreeMan.findInstrument(_baseItem);
    }

    /**
     * Override getTitle to return the observe count.
     */
    public String getTitle() {
        if (getTitleAttr() != null) {
            return super.getTitle();
        }

        return "MicroStep";
    }

    /**
     * Get the microstep pattern.
     */
    public String getPattern() {
        return _avTable.get(ATTR_PATTERN);
    }

    /**
     * Set the microstep pattern.
     */
    public void setPattern(String pattern) {
        _avTable.set(ATTR_PATTERN, pattern);
    }

    public String title_offset() {
        return "microstep";
    }

    public int getNOffsets() {
        int nOffs = 1;
        SpInstObsComp inst = SpTreeMan.findInstrument(this);

        if (inst instanceof SpMicroStepUser) {
            Hashtable<String, double[][]> microStepTable =
                    ((SpMicroStepUser) inst).getMicroStepPatterns();

            double[][] microSteps = microStepTable.get(getPattern());

            if (microSteps != null) {
                nOffs = microSteps.length;
            }
        }

        return nOffs;
    }
}
