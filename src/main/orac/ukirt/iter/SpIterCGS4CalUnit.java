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

import gemini.sp.iter.IterConfigItem;
import gemini.sp.iter.SpIterConfigObs;

/**
 * The CGS4CalUnit configuration iterator.
 */
@SuppressWarnings("serial")
public class SpIterCGS4CalUnit extends SpIterConfigObs {
    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "CGS4CalUnit",
            "CGS4 Cal Unit (Advanced)");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterCGS4CalUnit());
    }

    /**
     * Default constructor.
     */
    public SpIterCGS4CalUnit() {
        super(SP_TYPE);
    }

    /**
     * Get the name of the item being iterated over.
     *
     * Subclasses must define.
     */
    public String getItemName() {
        return "CGS4 Cal Unit";
    }

    /**
     * Get the array containing the IterConfigItems offered by the Cal Unit.
     */
    public IterConfigItem[] getAvailableItems() {
        String[] calTypes = {"Arc", "Flat"};

        IterConfigItem iciCalType = new IterConfigItem("Cal. Type",
                SpCGS4CalUnitConstants.ATTR_CALTYPE + "Iter", calTypes);
        IterConfigItem iciArcLamps = new IterConfigItem("Arc Lamp",
                SpCGS4CalUnitConstants.ATTR_LAMP + "Iter",
                SpIterCGS4CalObs.ARC_LAMPS);
        IterConfigItem iciFlatLamps = new IterConfigItem("Flat Lamp",
                SpCGS4CalUnitConstants.ATTR_LAMP + "Iter",
                SpIterCGS4CalObs.FLAT_LAMPS);
        IterConfigItem iciFilters = new IterConfigItem("Filter",
                SpCGS4CalUnitConstants.ATTR_FILTER + "Iter",
                SpIterCGS4CalObs.FILTERS);
        IterConfigItem iciModes = new IterConfigItem("Mode",
                SpCGS4CalUnitConstants.ATTR_MODE + "Iter",
                SpIterCGS4CalObs.MODES);

        IterConfigItem[] iciA = {
                iciCalType,
                iciArcLamps,
                iciFlatLamps,
                iciFilters,
                iciModes,
                getExposureTimeConfigItem(),
                getCoaddsConfigItem(),
        };

        return iciA;
    }
}
