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

import orac.ukirt.iter.SpIterUISTTargetAcq;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;

import gemini.sp.SpItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import ot.util.DialogUtil;
import jsky.app.ot.editor.OtItemEditor;

/**
 * This is the editor for UIST Spectroscopy/IFU Target Acquistion iterator
 * component.
 */
public final class EdIterUISTTargetAcq extends OtItemEditor implements
        TextBoxWidgetWatcher, ActionListener {
    private SpIterUISTTargetAcq _ita;
    private IterUISTTargetAcqGUI _w;

    /**
     * If true, ignore action events.
     */
    private boolean ignoreActions = false;

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdIterUISTTargetAcq() {
        _title = "UIST Spec/IFU Target Acquisition Iterator";
        _presSource = _w = new IterUISTTargetAcqGUI();
        _description = "Configure UIST spectroscopy/IFU camera for target"
                + " acquisition.";

        _w.defaultAcquisition.addActionListener(this);
    }

    /**
     */
    protected void _init() {
        // Source magnitude
        _w.sourceMag.addWatcher(new DropDownListBoxWidgetWatcher() {
            public void dropDownListBoxAction(DropDownListBoxWidgetExt dd,
                    int i, String val) {
                _ita.setSourceMag(val);
                _ita.useDefaultAcquisition();
                _updateWidgets();
            }
        });

        // Exposure time
        _w.exposureTime.addWatcher(this);

        // Coadds
        _w.coadds.addWatcher(this);

        super._init();
    }

    /**
     * Override setup to store away a reference to the SpInstUIST item.
     */
    public void setup(SpItem spItem) {
        _ita = (SpIterUISTTargetAcq) spItem;
        super.setup(spItem);
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
        ignoreActions = true;

        _ita.useDefaultDisperser();

        try {
            // Source magnitude
            _updateSourceMagChoices();
        } catch (NullPointerException ex) {
            DialogUtil.error(_w,
                    "Can't get source magnitude: probably because UIST"
                    + " instrument component is missing");
            return;
        }

        _updateSourceMag();

        try {
            // Exposure Time
            _w.exposureTime.setValue(_ita.getExposureTimeString());
        } catch (NullPointerException ex) {
            DialogUtil.error(_w,
                    "Can't set exposure time: probably because UIST"
                    + " instrument component is in imaging mode");
            return;
        }

        // Coadds
        _w.coadds.setValue(_ita.getCoadds());

        // Hacky fix for a bug whereby sometimes the coadds does not get
        // written out in the xml
        _ita.setCoadds(_w.coadds.getValue());

        // Disperser
        _w.disperser.setValue(_ita.getDisperser());

        ignoreActions = false;
    }

    /**
     * Update the list of source magnitude choices
     */
    private void _updateSourceMagChoices() {
        int numChoices = _ita.getSourceMagList().length;
        String choices[] = new String[numChoices];
        choices = _ita.getSourceMagList();
        _w.sourceMag.setMaximumRowCount(numChoices);
        _w.sourceMag.setChoices(choices);
    }

    /**
     * Update the source magnitude
     */
    private void _updateSourceMag() {
        String sourceMag = _ita.getSourceMag();
        _w.sourceMag.setValue(sourceMag);
    }

    /**
     * Watch changes to text boxes
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
        if (tbwe == _w.exposureTime) {
            _ita.setExposureTime(tbwe.getText());
        } else if (tbwe == _w.coadds) {
            _ita.setCoadds(tbwe.getText());
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

            if (w == _w.defaultAcquisition) {
                _ita.useDefaultSourceMag();
                _ita.useDefaultAcquisition();
                _updateWidgets();
            }
        }
    }
}
