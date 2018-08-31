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

package gemini.sp;

/**
 * Data that describes the insertion of a set of SpItems into or after another
 * SpItem.
 *
 * The client uses the <code>result</code> together with the
 * <code>referant</code> item to know where the insertion would take place.
 * For instance a result of INS_AFTER would mean that the <code>items</code>
 * would be inserted after the <code>referant</code> item in the program.
 *
 * <p>
 * The SpInsertData is needed because new items are not always inserted
 * immediately inside or after the existing item where the insertion was
 * evaluated. For instance, when inserting an observation inside of an
 * observation folder, it must be placed <em>after</em> any existing
 * observation components.
 *
 * <pre>
 *    Observation Folder
 *       Site Quality (Observation Component)
 *   --&gt; where a new Observation would go if inserted inside of the folder
 * </pre>
 *
 * @see SpTreeMan
 */
public final class SpInsertData implements SpInsertConstants {
    /**
     * What type of insertion?
     *
     * This is one of:
     *
     * <pre>
     * SpInsertConstants.INS_AFTER  - insert after the referant as the next
     *                                  siblings of the referant
     * SpInsertConstants.INS_INSIDE - insert inside the referant as the first
     *                                  children of the referant
     * </pre>
     */
    public int result;

    /** The set of items that will be inserted. */
    public SpItem[] items;

    /** Insert relative to this item. */
    public SpItem referant;

    /**
     * The set of items that would be replaced by the insertion (if any).
     *
     * Items are replaced when a newly inserted item must be unique in its
     * scope, but is being inserted into a scope that already contains an
     * item of its type. For instance, an instrument must be unique in its
     * scope.
     */
    public SpItem[] replaceItems;

    /**
     * Construct the SpInsertData with all the fields except replaceItems.
     */
    SpInsertData(int result, SpItem[] items, SpItem referant) {
        this.result = result;
        this.items = items;
        this.referant = referant;
    }

    /**
     * Construct the SpInsertData with all the fields.
     */
    SpInsertData(int result, SpItem[] items, SpItem referant,
            SpItem[] replaceItems) {
        this.result = result;
        this.items = items;
        this.referant = referant;
        this.replaceItems = replaceItems;
    }

    /**
     * For debugging.
     */
    public String toString() {
        String posn;
        switch (result) {
            case INS_INSIDE:
                posn = "INS_INSIDE";
                break;

            case INS_AFTER:
                posn = "INS_AFTER";
                break;

            default:
                posn = "UNKNOWN (" + result + ")";
        }

        return getClass().getName() + "["
                + posn + ", "
                + "("
                + referant.getTitle() + ", "
                + referant.typeStr() + ", "
                + referant.subtypeStr()
                + "), "
                + ", replaceItems=" + replaceItems
                + "]";
    }
}
