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
 *
 * author: Alan Pickup = dap@roe.ac.uk         2001 Feb
 * modified for Swing OT by
 *         Martin Folger = M.Folger@roe.ac.uk  2001 Apr
 */

package ot.ukirt.iter.editor;

import orac.ukirt.iter.SpIterMichelleCalObs;

import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jsky.app.ot.editor.OtItemEditor;

/**
 * This is the editor for the Michelle Calibration Observation iterator.
 */
public final class EdIterMichelleCalObs extends OtItemEditor implements
        TextBoxWidgetWatcher, DropDownListBoxWidgetWatcher, ActionListener {
    /** Identifier for a FLAT calibration. */
    public static final int FLAT = 0;

    /** Identifier for an ARC calibration. */
    public static final int ARC = 1;

    private IterMichelleCalObsGUI _w; // the GUI layout

    /**
     * This flag is set true while methods like _init is executed to prevent
     * actionPerformed() to do react to action events caused by
     * initializing widgets.
     */
    private boolean _ignoreActionEvents = false;

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdIterMichelleCalObs() {
        _title = "Michelle Cal Observation";
        _presSource = _w = new IterMichelleCalObsGUI();
        _description = "Configure Michelle's Calibration with this component.";

        for (int i = 0; i < 100; i++) {
            _w.repeatComboBox.addItem("" + (i + 1));
        }

        _w.calType.addActionListener(this);
        _w.flat_source.addActionListener(this);
        _w.flat_sampling.addActionListener(this);
        _w.repeatComboBox.addActionListener(this);
        _w.useDefaults.addActionListener(this);
    }

    /**
     */
    protected void _init() {
        _ignoreActionEvents = true;

        _w.exposureTime.addWatcher(this);
        _w.observationTime.addWatcher(this);

        super._init();

        _ignoreActionEvents = false;
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
        _ignoreActionEvents = true;

        SpIterMichelleCalObs ico = (SpIterMichelleCalObs) _spItem;

        // Get the choices and defaults from the instrument.

        // Show the calib. type
        _w.calType.setChoices(ico.getCalTypeChoices());
        _w.calType.setValue(ico.getCalTypeString());

        // Observe repetitions
        _w.repeatComboBox.setValue(ico.getCount() - 1);

        // Exposure time
        _w.exposureTime.setValue(ico.getExposureTime());

        // Observation time
        _w.observationTime.setValue(ico.getObservationTime());

        // Deal with Flat Sampling according to state of inst & caltype
        if (ico.getCalType() == FLAT) {
            // MFO: "FLAT" is hard-wired in IterCGS4CalObsGUI (as
            // constraint string for CardLayout).
            ((CardLayout) (_w.calTypesPanel.getLayout())).show(
                    _w.calTypesPanel, "FLAT");

            _w.flat_source.setChoices(ico.getFlatSourceChoices());
            _w.flat_source.setValue(ico.getFlatSource());

            _w.flat_sampling.setChoices(ico.getSamplingChoices());
            _w.flat_sampling.setValue(ico.getSampling());

        } else {
            // MFO: "EMPTY" is hard-wired in IterCGS4CalObsGUI (as
            // constraint string for CardLayout).
            ((CardLayout) (_w.calTypesPanel.getLayout())).show(
                    _w.calTypesPanel, "EMPTY");
        }

        // Update data acquisition config
        ico.updateDAConf();

        _ignoreActionEvents = false;
    }

    /**
     * Watch changes to text box widgets.
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbw) {
        SpIterMichelleCalObs ico = (SpIterMichelleCalObs) _spItem;

        if (tbw == _w.exposureTime) {
            try {
                String ets = tbw.getText();
                double et = Double.parseDouble(ets);

                if (et > 0.00001) {
                    ico.setExpTime(ets);
                }

            } catch (Exception ex) {
                // ignore
                ex.printStackTrace();
            }

        } else if (tbw == _w.observationTime) {
            try {
                String ots = tbw.getText();
                double ot = Double.parseDouble(ots);

                if (ot > 0.00001) {
                    ico.setObservationTime(ots);
                }

            } catch (Exception ex) {
                // ignore
                ex.printStackTrace();
            }
        }
    }

    /**
     * Text box action.
     */
    public void textBoxAction(TextBoxWidgetExt tbwe) {
    }

    /**
     * DD list box action.
     */
    public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe, int i,
            String val) {
    }

    /**
     *
     */
    public void actionPerformed(ActionEvent evt) {
        if (!_ignoreActionEvents) {
            Object w = evt.getSource();

            SpIterMichelleCalObs ico = (SpIterMichelleCalObs) _spItem;

            if (w == _w.calType) {
                DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
                ico.setCalType(ddlbw.getStringValue());
                ico.useDefaults();
                _updateWidgets();

            } else if (w == _w.flat_source) {
                DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
                ico.setFlatSource(ddlbw.getStringValue());
                _updateWidgets();

            } else if (w == _w.flat_sampling) {
                DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
                ico.setSampling(ddlbw.getStringValue());

            } else if (w == _w.repeatComboBox) {
                int i = _w.repeatComboBox.getIntegerValue() + 1;
                ico.setCount(i);

            } else if (w == _w.useDefaults) {
                ico.useDefaults();
                _updateWidgets();
            }
        }
    }
}
