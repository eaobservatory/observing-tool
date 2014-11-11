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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;

import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import orac.util.OracUtilities;

import gemini.sp.SpProg;

/**
 * This is the editor for the Science Program component.
 *
 * At this point, this editor is just a simple placeholder.
 */
public final class EdProgram extends OtItemEditor implements
        TextBoxWidgetWatcher, ActionListener {

    // Attributes edited by this editor
    // changed for OMP by MFO, 7 August 2001
    private static final String KIND = "kind";
    private ProgramGUI _w; // the GUI layout panel

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdProgram() {
        _title = "Program";
        _presSource = _w = new ProgramGUI();
        _description = "General program information taken from the proposal.";
        _resizable = true;

        // group the option buttons (radio buttons)
        ButtonGroup grp = new ButtonGroup();
        grp.add(_w.queueOption);
        grp.add(_w.classicalOption);
        _w.queueOption.addActionListener(this);
        _w.classicalOption.addActionListener(this);
        _w.infoBox.setVisible(false);

        _w.propKindLabel.setVisible(false);
        _w.queueOption.setVisible(false);
        _w.classicalOption.setVisible(false);
    }

    /**
     * Do any (one time) initialization.
     */
    protected void _init() {
        TextBoxWidgetExt tbwe = _w.titleBox;
        tbwe.addWatcher(this);

        // added for OMP by MFO, 7 August 2001
        _w.piBox.addWatcher(this);
        _w.countryBox.addWatcher(this);
        _w.projectIdBox.addWatcher(this);
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order
     * to set up the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
        String val;

        // Title
        TextBoxWidgetExt tbwe = _w.titleBox;
        val = _spItem.getTitleAttr();

        if (val == null) {
            tbwe.setText("");
        } else {
            tbwe.setText(val);
        }

        // PI (changed for OMP by MFO, 7 August 2001)
        _w.piBox.setText(((SpProg) _spItem).getPI());

        // Country (changed for OMP by MFO, 7 August 2001)
        _w.countryBox.setText(((SpProg) _spItem).getCountry());

        // Project ID (added for OMP by MFO, 7 August 2001)
        _w.projectIdBox.setText(((SpProg) _spItem).getProjectID());

        _showPropKind(_avTab.get(KIND));

        double time = ((SpProg) _spItem).getElapsedTime();
        _w.estimatedTime.setText(OracUtilities.secsToHHMMSS(time, 1));
        time = ((SpProg) _spItem).getTotalTime();
        _w.totalTime.setText(OracUtilities.secsToHHMMSS(time, 1));
    }

    /**
     * Set the Prop.
     *
     * Kind option widget.
     */
    void _showPropKind(String kind) {
        OptionWidgetExt ow;

        // Proposal Kind
        if ((kind == null) || kind.equals("queue")) {
            ow = _w.queueOption;
        } else {
            ow = _w.classicalOption;
        }

        ow.setValue(true);
    }

    /**
     * Watch changes to the title text box.
     *
     * @see TextBoxWidgetWatcher
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
        if (tbwe == _w.titleBox) {
            _spItem.setTitleAttr(tbwe.getText().trim());

        } else if (tbwe == _w.piBox) {
            ((SpProg) _spItem).setPI(tbwe.getText().trim());

        } else if (tbwe == _w.countryBox) {
            ((SpProg) _spItem).setCountry(tbwe.getText().trim());

        } else if (tbwe == _w.projectIdBox) {
            ((SpProg) _spItem).setProjectID(tbwe.getText().trim());
        }
    }

    /**
     * Text box action, ignore.
     *
     * @see TextBoxWidgetWatcher
     */
    public void textBoxAction(TextBoxWidgetExt tbwe) {
    }

    /**
     * Override the <code>action()</code> method to handle user events.
     */
    public void actionPerformed(ActionEvent evt) {
        Object w = evt.getSource();

        if (w == _w.queueOption) {
            _showPropKind("queue");

        } else if (w == _w.classicalOption) {
            _showPropKind("classical");
        }
    }
}
