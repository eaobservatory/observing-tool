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

package orac.jcmt.iter;

import orac.ukirt.iter.SpIterConfigObsUKIRT;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.iter.IterConfigItem;

import java.util.Vector;

/**
 * The Frequency iterator.
 *
 * @author modified for JCMT by Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SpIterFrequency extends SpIterConfigObsUKIRT {

    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "frequency", "Frequency");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterFrequency());
    }

    /**
     * Default constructor.
     */
    public SpIterFrequency() {
        super(SP_TYPE);
    }

    /**
     * Override "getConfigAttribs" to fix up old programs with the wrong
     * attribute names.
     */
    public Vector<String> getConfigAttribs() {
        return new Vector<String>();
    }

    /**
     * Override adding a configuration item to the set.
     */
    public void addConfigItem(IterConfigItem ici, int size) {
    }

    /**
     * Try deleting a configuration item to the set.
     */
    public void deleteConfigItem(String attribute) {
    }

    /**
     * Try overriding the method from SpIterConfigBase to add set of the
     * instrument aperture information when it changes.  Set the steps of
     * the item iterator of the given attribute.
     */
    public void setConfigStep(String attribute, String value, int index) {
    }

    /**
     * Get the name of the item being iterated over.  Subclasses must
     * define.
     */
    public String getItemName() {
        return "Frequency";
    }

    /**
     * Get the array containing the IterConfigItems offered by IRCAM3.
     */
    public IterConfigItem[] getAvailableItems() {
        // MFO: mocked up implementation for demonstration purpose.
        String[] lineTransitionChoices = {
                "A few examples:",
                "H2O (515 - 422)",
                "H3O+ (310 - 221)",
                "CN, v = 0, 1 (3032 - 2032)",
                "HCN (4 - 3)",
                "C-13-N (3432 - 2332)",
                "CO (3 - 2)",
                "C-13-O (3 - 2)",
                "There is more to come.",
            };
        IterConfigItem lineTransitionConfigItem = new IterConfigItem(
                "Line/Transition", "lineTransition", lineTransitionChoices);
        IterConfigItem frequencyConfigItem = new IterConfigItem(
                "Frequency", "frequency", null);
        String[] sidebandChoices = {"usb", "lsb", "best"};
        IterConfigItem sidebandConfigItem = new IterConfigItem(
                "Sideband", "sideband", sidebandChoices);
        IterConfigItem[] result = {
                lineTransitionConfigItem,
                frequencyConfigItem,
                sidebandConfigItem,
        };

        return result;
    }
}
