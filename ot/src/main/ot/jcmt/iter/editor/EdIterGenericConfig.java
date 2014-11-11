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

package ot.jcmt.iter.editor;

import gemini.sp.iter.IterConfigItem;
import gemini.util.ObservingToolUtilities;

import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;

import jsky.app.ot.gui.ListBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import orac.jcmt.iter.SpIterPOL;

public class EdIterGenericConfig extends jsky.app.ot.editor.EdIterGenericConfig
        implements TextBoxWidgetWatcher {
    MiniConfigIterGUI gui;

    public EdIterGenericConfig() {
        super();
        _title = "Configuration Iterator";
        gui = new MiniConfigIterGUI();
        _presSource = _w = gui;
        _description = "Iterate over a configuration with this component.";

        // add button action listeners
        _w.deleteTest.addActionListener(this);
        _w.addStep.addActionListener(this);
        _w.deleteStep.addActionListener(this);
        _w.top.addActionListener(this);
        _w.up.addActionListener(this);
        _w.down.addActionListener(this);
        _w.bottom.addActionListener(this);

        // JBuilder has some problems with image buttons...
        _w.top.setIcon(new ImageIcon(ObservingToolUtilities
                .resourceURL("jsky/app/ot/images/top.gif")));
        _w.up.setIcon(new ImageIcon(ObservingToolUtilities
                .resourceURL("jsky/app/ot/images/up.gif")));
        _w.bottom.setIcon(new ImageIcon(ObservingToolUtilities
                .resourceURL("jsky/app/ot/images/bottom.gif")));
        _w.down.setIcon(new ImageIcon(ObservingToolUtilities
                .resourceURL("jsky/app/ot/images/down.gif")));

        // --- XXX the code below was in _init() ---

        // Watch for selection of cells in the iterator table.
        _iterTab = _w.iterStepsTable;
        _iterTab.addWatcher(this);

        // Watch for selection of available items.
        _itemsLBW = _w.availableItems;
        _itemsLBW.addWatcher(this);

        JPanel gw;
        JLabel stw;

        // Initialize the ListBox value editor
        gw = _w.listBoxGroup;
        stw = _w.listBoxTitle;
        ListBoxWidgetExt lbw;
        lbw = _w.availableChoices;

        _listBoxVE = createICListBoxValueEditor(this, gw, stw, lbw);

        // Initialize the TextBox value editor
        gw = _w.textBoxGroup;
        stw = _w.textBoxTitle;
        TextBoxWidgetExt tbw;
        tbw = _w.textBox;
        _textBoxVE = createICTextBoxValueEditor(this, gw, stw, tbw);

        _valueEditor = _listBoxVE;

        _iterItems = new Hashtable<String, IterConfigItem>();

        gui.continuousSpinCheckBox.addActionListener(this);
        gui.calibratorInBeamCheckBox.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == gui.continuousSpinCheckBox) {
            boolean enabled = gui.continuousSpinCheckBox.getBooleanValue();
            setContinuousSpin(enabled);

            if (!enabled) {
                _updateWidgets();
            }

        } else if (event.getSource() == gui.calibratorInBeamCheckBox) {
            ((SpIterPOL) _spItem).setCalibratorInBeam(
                    gui.calibratorInBeamCheckBox.getBooleanValue());

        } else {
            super.actionPerformed(event);
        }
    }

    private void setContinuousSpin(boolean enabled) {
        gui.enableParent(!enabled);
        _iterTab.setEnabled(!enabled);
        _itemsLBW.setEnabled(!enabled);

        if (enabled) {
            deleteSelectedColumn();
        }

        ((SpIterPOL) _spItem).setContinuousSpin(enabled);
    }

    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
        textBoxAction(tbwe);
    }

    public void textBoxAction(TextBoxWidgetExt tbwe) {
    }

    protected void _updateWidgets() {
        super._updateWidgets();
        boolean usingSpin = ((SpIterPOL) _spItem).hasContinuousSpin();
        gui.continuousSpinCheckBox.setSelected(usingSpin);
        setContinuousSpin(usingSpin);

        gui.calibratorInBeamCheckBox.setSelected(
                ((SpIterPOL) _spItem).isCalibratorInBeam());
    }
}
