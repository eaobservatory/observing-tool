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

package orac.jcmt.inst;

import gemini.sp.SpType;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.iter.SpIterStep;

import orac.jcmt.SpJCMTConstants;

/**
 * A base class for JCMT instrument observation component items.
 *
 * This extends gemini.sp.obsComp.SpInstObsComp and adds JCMT-specific
 * features.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public abstract class SpJCMTInstObsComp extends SpInstObsComp implements
        SpJCMTConstants {
    /**
     * Constructor. Sets default values for attributes.
     */
    public SpJCMTInstObsComp(SpType spType) {
        super(spType);
    }

    /**
     * Generic IterationTracker for JCMT.
     *
     * @see gemini.sp.obsComp.SpInstObsComp
     */
    public class IterTrackerJCMT extends IterationTracker {
        double currentElapsedTime = 0.0;

        public void update(SpIterStep spIterStep) {
            try {
                String attribute = null;
                String value = null;

                for (int i = 0; i < spIterStep.values.length; i++) {
                    // SpIterStep.values     is an array of SpIterValue
                    // SpIterValue.values    is an array of String the first
                    //                       of which contains
                    attribute = spIterStep.values[i].attribute;
                    value = spIterStep.values[i].values[0];

                    if (attribute.equals(ATTR_ELAPSED_TIME)) {
                        currentElapsedTime = Double.valueOf(value);
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not process iteration step "
                        + spIterStep.title + " for time estimation:\n\n" + e);
            }
        }

        public double getObserveStepTime() {
            return currentElapsedTime;
        }
    }

    public IterationTracker createIterationTracker() {
        return new IterTrackerJCMT();
    }

    /**
     * Get jiggle pattern options for this instrument, given the current
     * settings.
     *
     * @return String array of jiggle pattern options.
     */
    public abstract String[] getJigglePatterns();

    /**
     * Returns instrument specific default value for scan dx.
     */
    public abstract double getDefaultScanVelocity();

    /**
     * Returns instrument specific default value for scan dx.
     */
    public abstract double getDefaultScanDy();

    /**
     * Returns a time estimate in seconds for slewing the telescope (JCMT):
     * 60 seconds.
     */
    public double getSlewTime() {
        return 0.0;
    }
}
