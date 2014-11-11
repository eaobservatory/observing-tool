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

package ot.ukirt.iter.editor;

import orac.ukirt.iter.SpIterDarkObs;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jsky.app.ot.editor.OtItemEditor;

/**
 * This is the editor for Dark Observe iterator component.
 */
public final class EdIterDarkObs extends OtItemEditor implements
        TextBoxWidgetWatcher, ActionListener {
    private IterDarkObsGUI _w;

    /**
     * If true, ignore action events.
     */
    private boolean ignoreActions = false;

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdIterDarkObs() {
        _title = "Dark Iterator";
        _presSource = _w = new IterDarkObsGUI();
        _description = "Take the specified number of dark exposures.";

        for (int i = 0; i < 100; i++) {
            _w.repeatComboBox.addItem("" + (i + 1));
        }

        _w.repeatComboBox.addActionListener(this);
        _w.defaultAcquisition.addActionListener(this);
    }

    /**
     */
    protected void _init() {
        // Exposure time
        _w.exposureTime.addWatcher(this);

        // Coadds
        _w.coadds.addWatcher(this);

        super._init();
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
        ignoreActions = true;

        SpIterDarkObs iterObs = (SpIterDarkObs) _spItem;

        // Repetitions
        _w.repeatComboBox.setSelectedIndex(iterObs.getCount() - 1);

        // Exposure Time
        _w.exposureTime.setValue(iterObs.getExposureTime());

        // Coadds
        _w.coadds.setValue(iterObs.getCoadds());

        ignoreActions = false;
    }

    /**
     * Watch changes to text boxes
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
        SpIterDarkObs iterObs = (SpIterDarkObs) _spItem;

        if (tbwe == _w.exposureTime) {
            iterObs.setExposureTime(tbwe.getText());
        } else if (tbwe == _w.coadds) {
            iterObs.setCoadds(tbwe.getText());
        }
    }

    /**
     * Text box action.
     */
    public void textBoxAction(TextBoxWidgetExt tbwe) {
    }

    /**
     *
     */
    public void actionPerformed(ActionEvent evt) {
        if (!ignoreActions) {
            Object w = evt.getSource();

            SpIterDarkObs iterObs = (SpIterDarkObs) _spItem;

            if (w == _w.repeatComboBox) {
                int i = _w.repeatComboBox.getSelectedIndex() + 1;
                iterObs.setCount(i);

            } else if (w == _w.defaultAcquisition) {
                iterObs.useDefaultAcquisition();
                _updateWidgets();
            }
        }
    }
}
