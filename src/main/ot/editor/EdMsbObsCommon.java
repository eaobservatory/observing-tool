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

package ot.editor;

import gemini.sp.SpMSB;
import jsky.app.ot.editor.OtItemEditor;

/**
 * Base class for MSB and Observation editors.
 *
 * Common aspects of the MSB and Observation editors
 * can be extracted into this class.
 */
public abstract class EdMsbObsCommon extends OtItemEditor implements
        MsbObsCommonGUI.RemainingCountListener {
    /**
     * If true, ignore action events.
     */
    protected boolean ignoreActions = false;

    /**
     * Default constructor.
     */
    public EdMsbObsCommon() {
    }

    /**
     * Handle changes to the remaining counter.
     *
     * Does not need to check the ignoreActions flag because
     * this will not be triggered by calls to setRemainingCount.
     */
    public void remainingCountChanged(int numberRemaining) {
        SpMSB spMSB = (SpMSB) _spItem;
        spMSB.setNumberRemaining(numberRemaining);
    }

    /**
     * Hangle removing / unremoving the observations remaining.
     *
     * Does not need to check the ignoreActions flag because
     * this will not be triggered by calls to setRemainingCount.
     */
    public void remainingCountToggleRemoved() {
        SpMSB spMSB = (SpMSB) _spItem;
        spMSB.setNumberRemaining(-1 * spMSB.getNumberRemaining());
        _updateWidgets();

    }
}
