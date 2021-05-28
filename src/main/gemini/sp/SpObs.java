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

package gemini.sp;

import gemini.util.CoordSys;

import gemini.sp.iter.SpIterFolder;
import gemini.sp.iter.SpIterMicroStep;
import gemini.sp.iter.SpIterOffset;
import gemini.sp.iter.SpIterChop;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpMicroStepUser;
import gemini.sp.obsComp.SpTelescopeObsComp;

import java.io.FileWriter;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import gemini.util.JACLogger;

/**
 * The observation item.
 *
 * In addition to other attributes, the SpObs class contains two
 * attributes that determine whether the observation is chained to
 * the next or previous observation (if any).
 *
 * 17Apr00 AB Added standard flag to this.
 */
@SuppressWarnings("serial")
public class SpObs extends SpMSB {
    static JACLogger logger = JACLogger.getLogger(SpObs.class);

    /**
     * This attribute records if the observation is to be treated as a
     * "standard"
     */
    public static final String ATTR_STANDARD = "standard";

    /**
     * This attribute is true if the SpObs is not inside an SpMSB because in
     * that case the observation is an MSB in its own right.
     */
    public static final String ATTR_MSB = ":msb";

    /**
     * This attribute records whether the calibration observation is optional.
     */
    public static final String ATTR_OPTIONAL = ":optional";

    /** Attribute of the library version */
    public static final String ATTR_LIBRARY_VERSION = "library_version";

    /** Default library version string */
    public static final String KEYWORD_IDENTIFIER = "$";

    public static final String LIBRARY_VERSION = "Revision";

    /**
     * Default constructor.
     *
     * Initializes the Observation with required items.
     */
    protected SpObs() {
        super(SpType.OBSERVATION);

        _avTable.noNotifySet(ATTR_REMAINING, "1", 0);
        _avTable.noNotifySet(ATTR_OPTIONAL, "false", 0);
        _avTable.noNotifySet(ATTR_PRIORITY, "99", 0);
        _avTable.noNotifySet(ATTR_STANDARD, "false", 0);
        _avTable.noNotifySet(ATTR_TITLE, getTitle(), 0);
    }

    /**
     * Construct an observation with the given iterator (sequence) folder.
     */
    protected SpObs(SpIterFolder ifPrototype) {
        this();

        doInsert(ifPrototype, null);
    }

    /**
     * Override clone to erase the chained state.
     */
    protected Object clone() {
        return (SpItem) super.clone();
    }

    /**
     * Get the "standard" flag of the observation.
     *
     */
    public boolean getIsStandard() {
        return _avTable.getBool(ATTR_STANDARD);
    }

    /**
     * Set the "standard" flag of the observation.
     *
     */
    public void setIsStandard(boolean standard) {
        _avTable.set(ATTR_STANDARD, standard);
    }

    /**
     * Set the library verion to the default String.
     *
     * This would have been replaced after the library was commited to CVS,
     * but is currently set to the CVS keyword $Revision$ and not interpreted.
     */
    public void setLibraryRevision() {
        _avTable.set(ATTR_LIBRARY_VERSION,
                KEYWORD_IDENTIFIER + LIBRARY_VERSION + KEYWORD_IDENTIFIER);
    }

    /**
     * Get the version of the library.
     *
     * @return It will probably return $Revision$ as CVS keywords are no
     *         longer being interpreted.
     */
    public String getLibraryRevision() {
        return _avTable.get(ATTR_LIBRARY_VERSION);
    }

    /**
     * Override setTable to make sure that the chained states are valid.
     */
    protected void setTable(SpAvTable avTable) {
        super.setTable(avTable);
    }

    /**
     * Get the MSB flag of the observation.
     *
     * Added for OMP. <!-- MFO, 27 August 2001 -->
     */
    public boolean isMSB() {
        return _avTable.getBool(ATTR_MSB);
    }

    /**
     * Set the MSB attribute of the observation.
     *
     * Added for OMP. <!-- MFO, 27 August 2001 -->
     */
    public void updateMsbAttributes() {
        int editState = getAvEditFSM().getState();

        /*
         * Note that _avTable.set is used instead of _avTable.noNotifySet and
         * that the SpAvEditState which is set to EDITED as a consequence is
         * reset immediately (if it was UNEDITED before). The is done
         * deliberately.  If noNotifySet was used instead then the title would
         * not always be displayed/updated correctly with respect to whether
         * or not isMSB() is true or false. (Only isMSB() == true then
         * getNumberRemaining() is displayed in the tree in brackets after
         * the component title.)
         */

        // If the parent component is an MSB then this SpObs is not.
        if (parent() instanceof SpMSB || (parent() instanceof SpSurveyContainer
                && parent().parent() instanceof SpMSB)) {
            _avTable.set(ATTR_MSB, "false");

            // If this SpObs is not and MSB then it does not have a priority.
            // Remove the priority.
            _avTable.rm(SpObs.ATTR_PRIORITY);

            // If this Obs is not an MSB then remove the total time estimate
            _avTable.rm(ATTR_TOTAL_TIME);

        } else {
            _avTable.set(ATTR_MSB, "true");

            // If this SpObs is an MSB then it cannot be optional.
            setOptional(false);
        }

        // save() just means reset() in this context.
        if (editState == SpAvEditState.UNEDITED) {
            getAvEditFSM().save();
        }
    }

    public boolean isOptional() {
        return _avTable.getBool(ATTR_OPTIONAL);
    }

    /**
     * Set true if calibration observatiob is optional, false otherwise.
     *
     * Added for OMP (MFO, 22 October 2001)
     */
    public void setOptional(boolean optional) {
        _avTable.set(ATTR_OPTIONAL, optional);
    }

    public double getElapsedTime() {
        SpIterFolder iterFolder = getIterFolder();

        double acqTime = 0.0;
        SpInstObsComp obsComp = SpTreeMan.findInstrument(this);

        // Is this is null or a standard or optional, we dont need to do
        // anything.
        if (obsComp != null && !getIsStandard() && !isOptional()) {
            acqTime = obsComp.getAcqTime();
        }

        if (iterFolder != null) {
            return iterFolder.getElapsedTime() + acqTime;
        } else {
            return 0.0;
        }
    }

    public SpIterFolder getIterFolder() {
        Enumeration<SpItem> children = children();
        SpItem spItem = null;

        while (children.hasMoreElements()) {
            spItem = children.nextElement();

            if (spItem instanceof SpIterFolder) {
                return (SpIterFolder) spItem;
            }
        }

        return null;
    }
}
