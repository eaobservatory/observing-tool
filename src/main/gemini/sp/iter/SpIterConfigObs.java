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

import gemini.sp.SpType;
import gemini.sp.obsComp.SpInstConstants;
import java.util.Vector;

/**
 * A configuration iterator base class for iterators that include exposure time
 * and coadd attributes.
 */
@SuppressWarnings("serial")
public abstract class SpIterConfigObs extends SpIterConfigBase {
    /**
     * Default constructor.
     */
    public SpIterConfigObs(SpType spType) {
        super(spType);
    }

    /**
     * Get the exposure time attribute.
     */
    protected IterConfigItem getExposureTimeConfigItem() {
        return new IterConfigItem("Exp. Time",
                SpInstConstants.ATTR_EXPOSURE_TIME + "Iter");
    }

    /**
     * Get the exposure time attribute.
     */
    protected IterConfigItem getCoaddsConfigItem() {
        return new IterConfigItem("Coadds", SpInstConstants.ATTR_COADDS
                + "Iter", null);
    }

    /**
     * Get the exposure times being iterated over (if any).
     *
     * Null is returned if the exposure times are not being iterated over.
     */
    public Vector<String> getExposureTimes() {
        return _avTable.getAll(SpInstConstants.ATTR_EXPOSURE_TIME + "Iter");
    }

    /**
     * Get the coadds being iterated over (if any).
     *
     * Null is returned if the coadds are not being iterated over.
     */
    public Vector<String> getCoadds() {
        return _avTable.getAll(SpInstConstants.ATTR_COADDS + "Iter");
    }
}
