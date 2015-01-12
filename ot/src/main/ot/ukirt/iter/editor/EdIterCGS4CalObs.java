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

import java.util.Vector;
import java.text.DecimalFormat;

import orac.ukirt.inst.SpInstCGS4;
import orac.ukirt.iter.SpIterCGS4CalObs;
import orac.ukirt.iter.SpCGS4CalUnitConstants;

import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;

import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jsky.app.ot.editor.OtItemEditor;

/**
 * This is the editor for the CGS4 CalUnit Observation iterator.
 */
public final class EdIterCGS4CalObs extends OtItemEditor implements
        TextBoxWidgetWatcher, DropDownListBoxWidgetWatcher, ActionListener {
    /** Identifier for a FLAT calibration. */
    public static final int FLAT = 0;

    /** Identifier for an ARC calibration. */
    public static final int ARC = 1;

    private IterCGS4CalObsGUI _w; // the GUI layout

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
    public EdIterCGS4CalObs() {
        _title = "CGS4 Cal Unit Observation";
        _presSource = _w = new IterCGS4CalObsGUI();
        _description = "Configure CGS4's Calibration Unit with this component.";

        for (int i = 0; i < 100; i++) {
            _w.repeatComboBox.addItem("" + (i + 1));
        }

        _w.calType.addActionListener(this);
        _w.lamp.addActionListener(this);
        _w.filter.addActionListener(this);
        _w.mode.addActionListener(this);
        _w.flatSampling.addActionListener(this);
        _w.repeatComboBox.addActionListener(this);
        _w.defaultValues.addActionListener(this);
    }

    /**
     */
    protected void _init() {
        _ignoreActionEvents = true;

        // Set the calibration choices
        _w.calType.setChoices(SpCGS4CalUnitConstants.CALTYPES);

        // Set the lamp choices
        _w.lamp.setChoices(SpIterCGS4CalObs.ARC_LAMPS);

        // Show the filter
        _w.filter.setChoices(SpIterCGS4CalObs.FILTERS);

        // Show the mode
        _w.mode.setChoices(SpIterCGS4CalObs.MODES);

        // Exposure time
        _w.exposureTime.addWatcher(this);

        // Coadds
        _w.coadds.addWatcher(this);

        // CVF Wavelength
        _w.cvfWavelength.addWatcher(this);

        // Flat sampling
        Vector<String> samplingChoiceVector = new Vector<String>();
        samplingChoiceVector.add("1x1");
        samplingChoiceVector.add("AS_OBJECT");
        _w.flatSampling.setChoices(samplingChoiceVector);
        _w.flatSampling.setValue("AS_OBJECT");

        super._init();

        _ignoreActionEvents = false;

    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
        _ignoreActionEvents = true;

        SpIterCGS4CalObs ico = (SpIterCGS4CalObs) _spItem;

        // Get the choices and defaults from the instrument.

        // Show the calib. type
        _w.calType.setValue(ico.getCalTypeString());

        // Show the lamp
        _updateLampChoices();

        // Make sure _ignoreActionEvents is set to true again after having
        // been set to false at the end of _updateLampChoices()
        _ignoreActionEvents = true;

        String lamp = ico.getLamp();
        _w.lamp.setValue(lamp);
        ico.setLamp(lamp);

        // Show the filter, for flats, inherit from instrument, for arcs
        // choose.
        String filter = ico.getFilter();
        _w.filter.setValue(filter);
        ico.setFilter(filter);

        // Show the mode
        String mode = ico.getMode();
        _w.mode.setValue(mode);
        ico.setMode(mode);

        // Observe repetitions
        _w.repeatComboBox.setSelectedIndex(ico.getCount() - 1);

        // Exposure time
        _w.exposureTime.setValue(ico.getExposureTime());

        // Coadds
        _w.coadds.setValue(ico.getCoadds());

        // Cvf wavelength
        _w.cvfWavelength.setValue(ico.getCvfWavelength());

        // Deal with CVF wavelength according to state of inst & caltype
        SpItem _baseItem = ico;
        SpInstCGS4 _instCgs4 = (SpInstCGS4) SpTreeMan.findInstrument(_baseItem);
        String grat = _instCgs4.getDisperser();

        if (grat.equalsIgnoreCase("echelle")) {
            if (ico.getCalType() == ARC) {
                // MFO: "ARC" is hard-wired in IterCGS4CalObsGUI (as
                // constraint strings a CardLayout).
                ((CardLayout) (_w.calTypesPanel.getLayout())).show(
                        _w.calTypesPanel, "ARC");

            } else if (ico.getCalType() == FLAT) {
                ((CardLayout) (_w.calTypesPanel.getLayout())).show(
                        _w.calTypesPanel, "FLAT");
                _w.flatCVFWavelength.setValue(ico.getCvfWavelength());
                double offset = ico.getCvfWavelength()
                        - _instCgs4.getCentralWavelength();
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(6);
                _w.flatCVFOffset.setValue(df.format(offset));
            }

        } else {
            // Deal with Flat Sampling according to state of inst & caltype
            _w.flatSampling.setValue(ico.getFlatSampling());

            if (ico.getCalType() == FLAT) {
                // MFO: "FLAT" is hard-wired in IterCGS4CalObsGUI (as
                // constraint strings a CardLayout).
                ((CardLayout) (_w.calTypesPanel.getLayout())).show(
                        _w.calTypesPanel, "FLAT");

                _w.flatSampling.setChoices(ico.getFlatSamplingChoices());
                _w.flatSampling.setValue(ico.getFlatSampling());
                _w.flatCVFWavelength.setValue("n/a");
                _w.flatCVFOffset.setValue("n/a");

            } else {
                // MFO: "EMPTY" is hard-wired in IterCGS4CalObsGUI (as
                // constraint strings a CardLayout).
                ((CardLayout) (_w.calTypesPanel.getLayout())).show(
                        _w.calTypesPanel, "EMPTY");
            }
        }

        _ignoreActionEvents = false;
    }

    /**
     * Update the lamp choices based upon the calibration type choice
     */
    private void _updateLampChoices() {
        _ignoreActionEvents = true;

        // do I need this? Does it work?
        SpIterCGS4CalObs ico = (SpIterCGS4CalObs) _spItem;

        // get value of calType, set lamp choices accordingly
        if (ico.getCalType() == FLAT) {
            _w.lamp.setChoices(SpIterCGS4CalObs.FLAT_LAMPS);
        } else {
            _w.lamp.setChoices(SpIterCGS4CalObs.ARC_LAMPS);
        }

        // _updateWidgets();

        _ignoreActionEvents = false;
    }

    /**
     * Watch changes to text box widgets.
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
        SpIterCGS4CalObs ico = (SpIterCGS4CalObs) _spItem;

        if (tbwe == _w.exposureTime) {
            ico.setExposureTime(tbwe.getText());
        } else if (tbwe == _w.coadds) {
            ico.setCoadds(tbwe.getText());
        } else if (tbwe == _w.cvfWavelength) {
            ico.setCvfWavelength(tbwe.getText());
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

            SpIterCGS4CalObs ico = (SpIterCGS4CalObs) _spItem;

            if (w == _w.calType) {
                DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
                ico.setCalType(ddlbw.getStringValue());
                ico.useDefaults();
                _updateWidgets();

            } else if (w == _w.lamp) {
                DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
                ico.setLamp(ddlbw.getStringValue());

            } else if (w == _w.filter) {
                DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
                ico.setFilter(ddlbw.getStringValue());

            } else if (w == _w.mode) {
                DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
                ico.setMode(ddlbw.getStringValue());

            } else if (w == _w.flatSampling) {
                DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;

                if (ddlbw.getItemCount() > 0) {
                    ico.setFlatSampling(ddlbw.getStringValue());
                }

            } else if (w == _w.repeatComboBox) {
                int i = _w.repeatComboBox.getSelectedIndex() + 1;
                ico.setCount(i);

            } else if (w == _w.defaultValues) {
                ico.useDefaults();
                _updateWidgets();
            }
        }
    }
}
