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

package gemini.sp.obsComp;

import gemini.sp.SpItem;
import gemini.sp.SpType;

/**
 * The base class for observation component items.
 */
@SuppressWarnings("serial")
public class SpObsComp extends SpItem {
    /**
     * Construct with a subtype.
     */
    public SpObsComp(SpType spType) {
        super(spType);

        // Most observation components must be unique in their scope.
        // If this isn't the case, set ".unique" to false in the subclass
        // constructor.
        _avTable.noNotifySet(".unique", "true", 0);
    }

    /**
     * Override getTitle to return the subtype instead of the type.
     */
    public String getTitle() {
        String title = type().getReadable();
        String titleAttr = getTitleAttr();

        if ((titleAttr != null) && !(titleAttr.equals(""))) {
            title += ": " + titleAttr;
        }

        return title;
    }

    /**
     * Does this observation component have to be unique in its scope?
     *
     * Most components must be unique within their scope to avoid ambiguity.
     * For instance, it would not do to have two instruments defined in the
     * same scope.
     */
    public boolean mustBeUnique() {
        return _avTable.getBool(".unique");
    }

    /**
     * Set the "uniqueness" property.
     *
     * @see #mustBeUnique
     */
    public void setMustBeUnique(boolean unique) {
        _avTable.set(".unique", true);
    }
}
