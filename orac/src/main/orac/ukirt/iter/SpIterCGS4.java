/*
 * Copyright 1999 United Kingdom Astronomy Technology Centre, an
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

package orac.ukirt.iter;

import java.util.Hashtable;
import java.util.Vector;
import java.util.List;
import java.util.Enumeration;

import orac.ukirt.inst.SpInstCGS4;

import gemini.sp.SpFactory;
import gemini.sp.SpItem;
import gemini.sp.SpType;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.sp.SpTreeMan;
import gemini.sp.iter.IterConfigItem;

import gemini.util.ConfigWriter;
import gemini.util.TranslationUtils;

/**
 * The CGS4 configuration iterator.
 */
@SuppressWarnings("serial")
public class SpIterCGS4 extends SpIterConfigObsUKIRT
        implements SpTranslatable {
    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "instCGS4", "CGS4");
    private IterConfigItem iciInstAperL;
    private Hashtable<String, String> _myTable = null;

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterCGS4());
    }

    /**
     * Default constructor.
     */
    public SpIterCGS4() {
        super(SP_TYPE);
    }

    /**
     * Get the name of the item being iterated over.
     *
     * Subclasses must define.
     */
    public String getItemName() {
        return "CGS4";
    }

    /**
     * Override adding a configuration item to the set - to add instrument
     * aperture items.
     */
    public void addConfigItem(IterConfigItem ici, int size) {
        if (ici.attribute.equals(SpInstCGS4.ATTR_CENTRAL_WAVELENGTH + "Iter")) {
            super.addConfigItemNoDef(iciInstAperL, size);
        }

        super.addConfigItem(ici, size);
    }

    /**
     * Override deleting a configuration item to the set to remove the
     * inst aper attributes too.
     */
    public void deleteConfigItem(String attribute) {
        super.deleteConfigItem(attribute);

        if (attribute.equals(SpInstCGS4.ATTR_CENTRAL_WAVELENGTH + "Iter")) {
            super.deleteConfigItem(iciInstAperL.attribute);
        }
    }

    /**
     * Set the steps of the item iterator of the given attribute.
     *
     * Overriding the method from SpIterConfigBase to add set of the
     * inst aper info when it changes.
     */
    public void setConfigStep(String attribute, String value, int index) {
        _avTable.set(attribute, value, index);

        // If central wavelength then set L inst aper to same.
        if (attribute.equals(SpInstCGS4.ATTR_CENTRAL_WAVELENGTH + "Iter")) {
            _avTable.set(SpInstCGS4.ATTR_INSTRUMENT_APER + "LIter", value,
                    index);
        }
    }

    /**
     * Get the array containing the IterConfigItems offered by CGS4.
     */
    public IterConfigItem[] getAvailableItems() {
        IterConfigItem iciMode = new IterConfigItem("Mode",
                SpInstCGS4.ATTR_MODE + "Iter", SpInstCGS4.MODES);
        iciInstAperL = new IterConfigItem("InstAperL",
                SpInstCGS4.ATTR_INSTRUMENT_APER + "LIter", null);

        IterConfigItem[] iciA = {
                iciMode,
                getExposureTimeConfigItem(),
                getCoaddsConfigItem(),
                iciInstAperL};

        return iciA;
    }

    public void translateProlog(Vector<String> sequence)
            throws SpTranslationNotSupportedException {
    }

    public void translateEpilog(Vector<String> sequence)
            throws SpTranslationNotSupportedException {
    }

    public void translate(Vector<String> v)
            throws SpTranslationNotSupportedException {
        // Make sure we have a valid instrument
        SpInstCGS4 inst;

        try {
            inst = (SpInstCGS4) SpTreeMan.findInstrument(this);
        } catch (Exception e) {
            throw new SpTranslationNotSupportedException(
                    "No CGS4 instrument in scope");
        }

        List<String> iterList = getConfigAttribs();
        int nConfigs = getConfigSteps(iterList.get(0)).size();

        for (int i = 0; i < nConfigs; i++) {
            _myTable = inst.getConfigItems();

            for (int j = 0; j < iterList.size(); j++) {
                if (iterList.contains("exposureTimeIter")) {
                    _myTable.put("exposureTime",
                            getConfigSteps("exposureTimeIter").get(i));
                }

                if (iterList.contains("coaddsIter")) {
                    _myTable.put("coadds",
                            getConfigSteps("coaddsIter").get(i));
                }

                if (iterList.contains("acqModeIter")) {
                    _myTable.put("DAConf",
                            getConfigSteps("acqModeIter").get(i));
                }

                if (iterList.contains("instAperXIter")) {
                    _myTable.put("instAperX",
                            getConfigSteps("instAperXIter").get(i));
                }

                if (iterList.contains("instAperYIter")) {
                    _myTable.put("instAperY",
                            getConfigSteps("instAperYIter").get(i));
                }

                if (iterList.contains("instAperZIter")) {
                    _myTable.put("instAperZ",
                            getConfigSteps("instAperZIter").get(i));
                }

                if (iterList.contains("instAperLIter")) {
                    _myTable.put("instAperL",
                            getConfigSteps("instAperLIter").get(i));
                    _myTable.put("centralWavelength",
                            getConfigSteps("instAperLIter").get(i));
                }

                try {
                    ConfigWriter.getCurrentInstance().write(_myTable);
                } catch (Exception e) {
                    throw new SpTranslationNotSupportedException(
                            "Unable to write config file for CGS4 iterator:"
                            + e.getMessage());
                }

                v.add("loadConfig "
                        + ConfigWriter.getCurrentInstance().getCurrentName());

                // translate all the cildren...
                Enumeration<SpItem> e = this.children();
                TranslationUtils.recurse(e, v);
            }
        }
    }

    /**
     * Gets the hashtable created by this iterator
     */
    public Hashtable<String, String> getIterTable() {
        Hashtable<String, String> clone = null;
        if (_myTable != null) {
            clone = new Hashtable<String, String>(_myTable);
        }

        return clone;
    }
}
