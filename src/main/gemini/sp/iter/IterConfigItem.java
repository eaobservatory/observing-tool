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

/**
 * A data object that describes an attribute that may be iterated over.
 *
 * Client code uses an IterConfigItem to present choices for iteration to the
 * user.
 */
@SuppressWarnings("serial")
public class IterConfigItem implements java.io.Serializable {
    /**
     * The displayable title that is meaningful to a human user.
     */
    public String title;

    /**
     * The attribute name stored in the science program.
     *
     * The convention is to name the iterator attribute the same as
     * the attribute being iterated over, with "Iter" appended.
     * So "filter" becomes "filterIter".
     */
    public String attribute;

    /**
     * The list of choices that this attribute may take.
     *
     * Numeric attributes like exposureTime will have a null list.
     */
    public String[] choices;

    /**
     * Constructor that should be used for numeric attributes.
     */
    public IterConfigItem(String title, String attribute) {
        this.title = title;
        this.attribute = attribute;
    }

    /**
     * Constructor that should be used for attributes that have a finite set of
     * choices, for instance a filter setting.
     */
    public IterConfigItem(String title, String attribute, String[] choices) {
        this(title, attribute);
        this.choices = choices;
    }
}
