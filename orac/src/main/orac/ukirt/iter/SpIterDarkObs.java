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

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpMSB;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.sp.SpTreeMan;
import gemini.sp.SpType;

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import gemini.sp.obsComp.SpInstConstants;
import gemini.sp.obsComp.SpInstObsComp;

import gemini.util.ConfigWriter;
import gemini.util.TranslationUtils;

import orac.ukirt.inst.SpDRRecipe;
import orac.ukirt.inst.SpInstWFCAM;

import java.io.IOException;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Enumerater for the elements of the Observe iterator.
 */
@SuppressWarnings("serial")
class SpIterDarkObsEnumeration extends SpIterEnumeration {
    private int _curCount = 0;
    private int _maxCount;
    private SpIterValue[] _values;

    SpIterDarkObsEnumeration(SpIterDarkObs iterObserve) {
        super(iterObserve);

        _maxCount = iterObserve.getCount();
    }

    protected boolean _thisHasMoreElements() {
        return (_curCount < _maxCount);
    }

    protected SpIterStep _thisFirstElement() {
        SpIterDarkObs ibo = (SpIterDarkObs) _iterComp;
        String expTimeValue = String.valueOf(ibo.getExposureTime());
        String coaddsValue = String.valueOf(ibo.getCoadds());

        _values = new SpIterValue[2];
        _values[0] = new SpIterValue(SpInstConstants.ATTR_EXPOSURE_TIME,
                expTimeValue);
        _values[1] = new SpIterValue(SpInstConstants.ATTR_COADDS, coaddsValue);

        return _thisNextElement();
    }

    protected SpIterStep _thisNextElement() {
        return new SpIterStep("dark", _curCount++, _iterComp, _values);
    }

}

@SuppressWarnings("serial")
public class SpIterDarkObs extends SpIterObserveBase implements SpTranslatable {
    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "darkObs", "Dark");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterDarkObs());
    }

    /**
     * Default constructor.
     */
    public SpIterDarkObs() {
        super(SP_TYPE);
    }

    /**
     * Override getTitle to return the observe count.
     */
    public String getTitle() {
        if (getTitleAttr() != null) {
            return super.getTitle();
        }

        return "Dark (" + getCount() + "X)";
    }

    /**
     * Use default acquisition
     */
    public void useDefaultAcquisition() {
        _avTable.rm(ATTR_EXPOSURE_TIME);
        _avTable.rm(ATTR_COADDS);
    }

    /**
     */
    public SpIterEnumeration elements() {
        return new SpIterDarkObsEnumeration(this);
    }

    public void translateProlog(Vector<String> sequence)
            throws SpTranslationNotSupportedException {
    }

    public void translateEpilog(Vector<String> sequence)
            throws SpTranslationNotSupportedException {
    }

    public void translate(Vector<String> v)
            throws SpTranslationNotSupportedException {
        // Get the instrument to allow us to get the config information
        SpInstObsComp inst = SpTreeMan.findInstrument(this);

        if (inst == null) {
            throw new SpTranslationNotSupportedException(
                    "No instrument in scope");
        }

        Hashtable<String, String> defaultsTable = inst.getConfigItems();

        if ("CGS4".equalsIgnoreCase(defaultsTable.get("instrument"))) {
            // If we are inside a CGS4 iterator, we need to pick up its
            // hashtable
            SpItem parent = parent();

            while (parent != null) {
                if (parent instanceof SpIterCGS4) {
                    defaultsTable = ((SpIterCGS4) parent).getIterTable();
                    break;
                }

                parent = parent.parent();
            }

        } else if ("WFCAM".equalsIgnoreCase(
                defaultsTable.get("instrument"))) {
            // If we are inside a WFCAM iterator, we need to pick up its
            // hashtable
            SpItem parent = parent();

            while (parent != null) {
                if (parent instanceof SpInstWFCAM) {
                    break;
                } else if (parent instanceof SpIterWFCAM) {
                    defaultsTable = ((SpIterWFCAM) parent).getIterTable();
                    break;
                }

                parent = parent.parent();
            }
        }

        // Set the number of dark exposures
        defaultsTable.put("darkNumExp", "" + getCoadds());

        if (defaultsTable.containsKey("coadds")) {
            defaultsTable.put("coadds", "" + getCoadds());
        }
        if (defaultsTable.containsKey("expTime")) {
            defaultsTable.put("expTime", "" + getExposureTime());
        }
        if (defaultsTable.containsKey("exposureTime")) {
            defaultsTable.put("exposureTime", "" + getExposureTime());
        }
        if (defaultsTable.containsKey("chopDelay")) {
            defaultsTable.put("chopDelay", "0.0");
        }
        if (defaultsTable.containsKey("type")) {
            if ("WFCAM".equalsIgnoreCase(defaultsTable.get("instrument"))) {
                defaultsTable.put("type", "dark");
            }
        }

        // Now we need to write a config for this dark
        try {
            ConfigWriter.getCurrentInstance().write(defaultsTable);
        } catch (IOException ioe) {
            throw new SpTranslationNotSupportedException(
                    "Unable to write dark config file");
        }

        // We will also need to get the DRRecipe component to allow us
        // to set the appropriate headers.
        // Find the parent first...
        SpItem parent = parent();
        Vector<SpItem> recipes = null;

        while (parent != null) {
            if (parent instanceof SpMSB) {
                recipes = SpTreeMan.findAllItems(parent,
                        SpDRRecipe.class.getName());

                if (recipes.size() > 0) {
                    break;
                }
            }

            parent = parent.parent();
        }

        if (recipes != null && recipes.size() != 0) {
            SpDRRecipe recipe = (SpDRRecipe) recipes.get(0);
            v.add("setHeader GRPMEM " + (recipe.getDarkInGroup() ? "T" : "F"));
            v.add("setHeader RECIPE " + recipe.getDarkRecipeName());

        } else {
            logger.error("No DRRecipe Component found in dark obs");
        }

        v.add("loadConfig "
                + ConfigWriter.getCurrentInstance().getCurrentName());

        v.add(gemini.sp.SpTranslationConstants.darkString);
        v.add("do " + getCount() + " _observe");

        // Finally move the default config (always _1) down
        TranslationUtils.copyFirstLoadConfig(v, true);
    }
}
