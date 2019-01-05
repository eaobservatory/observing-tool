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
import gemini.sp.SpType;

/**
 * This is the base class for all iterator Science Program components.
 */
@SuppressWarnings("serial")
public abstract class SpIterComp extends SpItem {
    /**
     * Construct with a subtype.
     */
    public SpIterComp(SpType type) {
        super(type);
    }

    /**
     * Override getTitle to return the subtype instead of the type.
     */
    public String getTitle() {
        String title = type().getReadable();
        String titleAttr = getTitleAttr();

        if ((titleAttr != null) && !(titleAttr.equals(""))) {
            title = title + ": " + titleAttr;
        }

        return title;
    }

    /**
     * Get an enumeration of the elements of this iterator.
     *
     * The client code uses this enumeration to step through all the values
     * that this iterator can produce.
     */
    public abstract SpIterEnumeration elements();
}
