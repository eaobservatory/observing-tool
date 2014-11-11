/*
 * Copyright (C) 2005-2010 Science and Technology Facilities Council.
 * All Rights Reserved.
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

import gemini.sp.SpFactory;
import gemini.sp.SpItem;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.sp.SpTreeMan;
import gemini.sp.SpType;

import gemini.sp.iter.IterConfigItem;

import gemini.util.ConfigWriter;
import gemini.util.TranslationUtils;

import orac.ukirt.inst.SpInstWFS;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

@SuppressWarnings("serial")
public final class SpIterWFS extends SpIterConfigObsUKIRT implements
        SpTranslatable {
    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "iterWFS", "WFS");
    static {
        SpFactory.registerPrototype(new SpIterWFS());
    }

    /**
     * Default constructor.
     */
    public SpIterWFS() {
        super(SP_TYPE);
    }

    /**
     * Get the array containing the IterConfigItems offered by WFS.
     */
    public IterConfigItem[] getAvailableItems() {
        IterConfigItem icilensPos = new IterConfigItem("LensPos",
                SpInstWFS.ATTR_LENS_POS + "Iter", SpInstWFS.LENS_POS);

        IterConfigItem[] iciA = {
                icilensPos,
                getExposureTimeConfigItem(),
                getCoaddsConfigItem(),
        };

        return iciA;
    }

    /**
     * Get the name of the item being iterated over.
     */
    public String getItemName() {
        return "WFS";
    }

    public void translate(Vector<String> v)
            throws SpTranslationNotSupportedException {
        SpInstWFS inst;

        try {
            inst = (SpInstWFS) SpTreeMan.findInstrument(this);
        } catch (Exception e) {
            throw new SpTranslationNotSupportedException(
                    "No WFS instrument in scope");
        }

        List<String> iterList = getConfigAttribs();
        int nConfigs = getConfigSteps(iterList.get(0)).size();

        for (int i = 0; i < nConfigs; i++) {
            Hashtable<String, String> configTable = inst.getConfigItems();

            for (int j = 0; j < iterList.size(); j++) {
                if (iterList.contains("exposureTimeIter")) {
                    configTable.put("expTime",
                            getConfigSteps("exposureTimeIter").get(i));
                }

                if (iterList.contains("coaddsIter")) {
                    configTable.put("objNumExp",
                            getConfigSteps("coaddsIter").get(i));
                }

                if (iterList.contains("lensPosIter")) {
                    configTable.put("lensPos",
                            getConfigSteps("lensPosIter").get(i));
                }
            }

            try {
                ConfigWriter.getCurrentInstance().write(configTable);
            } catch (Exception e) {
                throw new SpTranslationNotSupportedException(
                        "Unable to write config file for WFS iterator:"
                        + e.getMessage());
            }

            v.add("loadConfig "
                    + ConfigWriter.getCurrentInstance().getCurrentName());

            Enumeration<SpItem> e = this.children();
            TranslationUtils.recurse(e, v);
        }
    }
}
