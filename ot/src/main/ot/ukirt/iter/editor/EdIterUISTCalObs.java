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
 * modified for UIST (from Michelle) by
 *         Alan Pickup = A.Pickup              2001 Nov
 */

package ot.ukirt.iter.editor;

import orac.ukirt.iter.SpIterUISTCalObs;

import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jsky.app.ot.editor.OtItemEditor;

/**
 * This is the editor for the UIST Calibration Observation iterator.
 */
public final class EdIterUISTCalObs extends OtItemEditor implements
        TextBoxWidgetWatcher, DropDownListBoxWidgetWatcher, ActionListener {
    /** Identifier for a FLAT calibration. */
    public static final int FLAT = 0;

    /** Identifier for an ARC calibration. */
    public static final int ARC = 1;

    /** The GUI layout. */
    private IterUISTCalObsGUI _w;

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
    public EdIterUISTCalObs() {
        _title = "UIST Cal Observation";
        _presSource = _w = new IterUISTCalObsGUI();
        _description = "Configure UIST's Calibration with this component.";

        for (int i = 0; i < 100; i++) {
            _w.repeatComboBox.addItem("" + (i + 1));
        }

        _w.calType.addActionListener(this);
        _w.arc_source.addActionListener(this);
        _w.flat_source.addActionListener(this);
        _w.repeatComboBox.addActionListener(this);
        _w.useDefaults.addActionListener(this);
    }

    /**
     */
    protected void _init() {
        _ignoreActionEvents = true;

        // Exposure time
        _w.exposureTime.addWatcher(this);

        // Observation time
        // Do nothing with _w.observationTime ?
        // (there was a compiler warning here for casting it,
        // but the value was not used)

        // Coadds
        _w.coadds.addWatcher(this);

        super._init();

        _ignoreActionEvents = false;
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */

    protected void _updateWidgets() {
        _updateWidgets(null);
    }

    protected void _updateWidgets(Object source) {
        _ignoreActionEvents = true;

        SpIterUISTCalObs ico = (SpIterUISTCalObs) _spItem;

        // Get the choices and defaults from the instrument.

        // Check the calType and reset if this is Arc and UIST is now imaging
        if (ico.isImaging() && ico.getCalTypeString().equalsIgnoreCase("Arc")) {
            ico.setCalType("Flat");
            ico.useDefaults();
        }

        // Update calType selection box
        _w.calType.setChoices(ico.getCalTypeChoices());
        _w.calType.setValue(ico.getCalTypeString());

        // Observe repetitions
        _w.repeatComboBox.setValue(ico.getCount() - 1);

        // Exposure time
        if (_w.exposureTime != source) {
            String expTimeStr = ico.getExpTimeOTString();
            _w.exposureTime.setValue(expTimeStr);
        }

        if (_w.coadds != source) {
            _w.coadds.setValue(ico.getCoaddsString());
        }

        // Deal with Flat and Arc according to state of inst & caltype
        if (ico.getCalType() == FLAT) {
            // MFO: "FLAT" is hard-wired in IterCGS4CalObsGUI (as
            // constraint string for CardLayout).
            ((CardLayout) (_w.calTypesPanel.getLayout())).show(
                    _w.calTypesPanel, "FLAT");

            _w.flat_source.setChoices(ico.getFlatSourceChoices());
            _w.flat_source.setValue(ico.getFlatSource());

        } else {
            // DAP: "ARC" is hard-wired in IterCGS4CalObsGUI (as
            // constraint string for CardLayout).
            ((CardLayout) (_w.calTypesPanel.getLayout())).show(
                    _w.calTypesPanel, "ARC");

            _w.arc_source.setChoices(ico.getArcSourceChoices());
            _w.arc_source.setValue(ico.getArcSource());
        }

        // Update data acquisition config
        ico.updateDAConf();

        _w.observationTime.setValue(ico.getObservationTimeString());

        _w.filter.setValue(ico.getFilter());

        _ignoreActionEvents = false;
    }

    /**
     * Watch changes to text box widgets.
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbw) {
        SpIterUISTCalObs ico = (SpIterUISTCalObs) _spItem;

        if (tbw == _w.exposureTime) {
            try {
                String ets = tbw.getText();
                double et = Double.parseDouble(ets);

                if (et > 0.00001) {
                    ico.setExpTimeOT(ets);
                    _updateWidgets(_w.exposureTime);
                }

            } catch (Exception ex) {
                // ignore
            }

        } else if (tbw == _w.coadds) {
            try {
                String coaddsString = tbw.getText();
                int coadds = Integer.parseInt(coaddsString);

                if (coadds > 0) {
                    ico.setCoadds(coadds);
                    _updateWidgets(_w.coadds);
                }

            } catch (Exception ex) {
                // ignore
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

            SpIterUISTCalObs ico = (SpIterUISTCalObs) _spItem;

            if (w == _w.calType) {
                DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
                ico.setCalType(ddlbw.getStringValue());
                ico.useDefaults();
                _updateWidgets();

            } else if (w == _w.flat_source) {
                DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
                ico.setFlatSource(ddlbw.getStringValue());
                _updateWidgets();

            } else if (w == _w.arc_source) {
                DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
                ico.setArcSource(ddlbw.getStringValue());
                _updateWidgets();

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
