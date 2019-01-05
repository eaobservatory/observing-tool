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

package jsky.app.ot.editor;

import java.util.List;
import java.util.Vector;
import jsky.app.ot.gui.TableWidgetExt;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

/**
 * An extension of the TableWidget to support the output of an iterator
 * enumeration.
 *
 * This table is used with the EdIteratorFolder class.
 *
 * <p>
 * <em>The iterator code, especially the "compilation" or enumeration
 * of iterators, is going to change significantly.</em>
 */
@SuppressWarnings("serial")
public class IterEnumTableWidget extends TableWidgetExt {
    /**
     * Construct and set table widget options that make sense.
     */
    public IterEnumTableWidget() {
    }

    /**
     */
    public void addSteps(Vector<SpIterStep> steps) {
        int n = steps.size();

        for (int i = 0; i < n; ++i) {
            addStep(steps.elementAt(i));
        }
    }

    /**
     */
    public void addSteps(List<SpIterStep> steps) {
        int n = steps.size();

        for (int i = 0; i < n; ++i) {
            addStep(steps.get(i));
        }
    }

    /**
     */
    public void addStep(SpIterStep sis) {
        String title = sis.title;

        if (sis.stepCount != 0) {
            title += " (" + (sis.stepCount + 1) + ")";
        }

        Vector<String> v = new Vector<String>();
        v.addElement(title);

        if (sis.values.length == 0) {
            addRow(v);

        } else {
            SpIterValue siv = sis.values[0];
            _createRow(siv, v);
            addRow(v);

            for (int i = 1; i < sis.values.length; ++i) {
                SpIterValue siv2 = sis.values[i];
                Vector<String> v2 = new Vector<String>(2);
                v2.addElement("");
                _createRow(siv2, v2);
                addRow(v2);
            }
        }
    }

    /**
     */
    private void _createRow(SpIterValue siv, Vector<String> v) {
        v.addElement(siv.attribute + " = " + siv.values[0]);
    }

    public void addRow(Vector<String> v) {
        getModel().addRow(v);
    }
}
