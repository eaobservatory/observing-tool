/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ot.jcmt.iter.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import gemini.sp.iter.SpIterOffset;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.inst.SpInstSCUBA2;
import orac.jcmt.iter.SpIterStareObs;
import orac.jcmt.util.HeterodyneNoise;
import orac.jcmt.SpJCMTConstants;

import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;

import gemini.util.MathUtil;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

/**
 * This is the editor for the Stare Observe Mode iterator component (ACSIS).
 *
 * @author modified for JCMT by Martin Folger (M.Folger@roe.ac.uk)
 */
public final class EdIterStareObs extends EdIterJCMTGeneric implements
        ActionListener, CheckBoxWidgetWatcher, TextBoxWidgetWatcher,
        SpJCMTConstants, DropDownListBoxWidgetWatcher {
    private IterStareObsGUI _w; // the GUI layout panel
    private SpIterStareObs _iterObs;

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdIterStareObs() {
        super(new IterStareObsGUI());

        _title = "Stare";
        _presSource = _w = (IterStareObsGUI) super._w;
        _description = "Stare Observation Mode";
        _w.widePhotom.addActionListener(this);
        _w.contModeCB.addWatcher(this);

        super._w.arrayCentred.addWatcher(this);
        super._w.separateOffs.setEnabled(false);
        super._w.separateOffs.addWatcher(this);

        _w.coordSys.setChoices(SpIterStareObs.STARE_SYSTEMS);
        _w.paTextBox.addWatcher(this);
        _w.coordSys.addWatcher(this);
        _w.secsPerOffsetSample.addWatcher(this);

        _w.paTextBox.setEnabled(false);
    }

    /**
     * Override setup to store away a reference to the Scan Iterator.
     */
    public void setup(SpItem spItem) {
        _iterObs = (SpIterStareObs) spItem;

        super.setup(spItem);
    }

    protected void _updateWidgets() {
        if (_iterObs != null) {
            _w.widePhotom.setSelected(_iterObs.getWidePhotom());
            SpInstObsComp instrument = SpTreeMan.findInstrument(_iterObs);
            SpIterStareObs _iterStareObs = _iterObs;

            if (instrument instanceof SpInstHeterodyne) {
                _w.contModeCB.setSelected(_iterObs.isContinuum());

                SpInstHeterodyne heterodyne = (SpInstHeterodyne) instrument;
                boolean arrayCentredCriteria =
                        heterodyne.getFrontEnd().equals("HARP");
                super._w.arrayCentredLabel.setVisible(arrayCentredCriteria);
                super._w.arrayCentred.setVisible(arrayCentredCriteria);

                if (arrayCentredCriteria) {
                    super._w.arrayCentred.setSelected(
                            _iterStareObs.isArrayCentred());
                } else {
                    _iterStareObs.rmArrayCentred();
                }

                setCanDoMap(instrument);

            } else {
                super._w.arrayCentredLabel.setVisible(false);
                super._w.arrayCentred.setVisible(false);
            }

            updateSeparateOffs();

            _w.secsPerCycle.setText("" + _iterObs.getSecsPerCycle());
            _w.secsPerOffsetSample.setText("" + _iterObs.getSecsPerCycle());
        }

        super._updateWidgets();
    }

    private void updateSeparateOffs() {
        SpIterStareObs _iterStareObs = _iterObs;
        boolean separateOffsCriteria = false;
        boolean visibility = false;
        boolean enabled = false;

        if (SWITCHING_MODE_POSITION.equals(_iterStareObs.getSwitchingMode())) {
            visibility = true;
            enabled = _iterStareObs.insidePOL();

            if (!enabled) {
                double max_time_between_refs = 30.0;
                int secsPerCycle = _iterStareObs.getSecsPerCycle();
                double temp = Math.floor(max_time_between_refs / secsPerCycle);

                if (_iterStareObs.isContinuum()) {
                    separateOffsCriteria = true;

                } else {
                    separateOffsCriteria = !(Math.max(1.0, temp) > 1.0
                            || _iterStareObs.insideChop());
                }

                if (separateOffsCriteria) {
                    _iterStareObs.setSeparateOffs(separateOffsCriteria);
                } else {
                    _iterStareObs.rmSeparateOffs();
                }

            } else {
                if (!_iterStareObs.separateOffsExist()) {
                    _iterStareObs.setSeparateOffs(true);
                }
            }

        } else {
            _iterStareObs.rmSeparateOffs();
        }

        super._w.separateOffs.setSelected(_iterStareObs.hasSeparateOffs());
        super._w.separateOffsLabel.setVisible(visibility);
        super._w.separateOffs.setVisible(visibility);
        super._w.separateOffs.setEnabled(enabled);
    }

    public void setInstrument(SpInstObsComp spInstObsComp) {
        if (spInstObsComp == null) {
            return;
        }

        if (spInstObsComp instanceof SpInstHeterodyne) {
            _w.acsisPanel.setVisible(true);
            _w.scuba2Panel.setVisible(false);
            _w.widePhotom.setVisible(false);
            _w.widePhotom.setSelected(false);
            _w.mapPanel.setVisible(true);
            _iterObs.setupForHeterodyne();
            updateSeparateOffs();

        } else if (spInstObsComp instanceof SpInstSCUBA2) {
            _w.informationPanel.setVisible(false);
            _w.acsisPanel.setVisible(false);
            _w.scuba2Panel.setVisible(true);
            _w.widePhotom.setVisible(false);
            _w.widePhotom.setSelected(false);
            _w.mapPanel.setVisible(false);
            _iterObs.setupForSCUBA2();

        } else {
            _w.acsisPanel.setVisible(false);
            _w.widePhotom.setVisible(true);
            _w.mapPanel.setVisible(true);
        }

        super.setInstrument(spInstObsComp);
    }

    protected double calculateNoise(SpInstHeterodyne inst, double airmass,
            double tau) {
        double tSys = HeterodyneNoise.getTsys(inst.getFrontEnd(), tau, airmass,
                inst.getRestFrequency(0) / 1.0e9,
                inst.getMode().equalsIgnoreCase("ssb"));

        _noiseToolTip = "airmass = " + (Math.rint(airmass * 10) / 10)
                + ", Tsys = " + (Math.rint(tSys * 10) / 10);

        if ("acsis".equalsIgnoreCase(inst.getBackEnd())) {
            return MathUtil.round(
                    HeterodyneNoise.getHeterodyneNoise(_iterObs, inst, tSys),
                    3);
        } else {
            return -999.9;
        }
    }

    public void actionPerformed(ActionEvent e) {
        _iterObs.setWidePhotom(_w.widePhotom.isSelected());
    }

    public void checkBoxAction(CheckBoxWidgetExt cbwe) {
        if (cbwe == _w.contModeCB) {
            boolean isSelected = _w.contModeCB.isSelected();
            _iterObs.setContinuumMode(isSelected);
            updateSeparateOffs();

        } else if (cbwe == super._w.arrayCentred) {
            _iterObs.setArrayCentred(super._w.arrayCentred.isSelected());

        } else if (cbwe == super._w.separateOffs) {
            boolean isSelected = super._w.separateOffs.isSelected();
            _iterObs.setSeparateOffs(isSelected);

            if (isSelected) {
                _iterObs.setContinuumMode(false);
                _w.contModeCB.setSelected(false);
            }
        }

        super.checkBoxAction(cbwe);
    }

    public void textBoxAction(TextBoxWidgetExt tbwe) {
        textBoxKeyPress(tbwe);
    }

    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
        if (tbwe == _w.secsPerCycle) {
            String secsPerCycle = _w.secsPerCycle.getText();
            _iterObs.setSecsPerCycle(secsPerCycle);
            updateSeparateOffs();
        }

        if (tbwe == _w.secsPerOffsetSample) {
            String secsPerCycle = _w.secsPerOffsetSample.getText();
            _iterObs.setSecsPerCycle(secsPerCycle);
            updateSeparateOffs();

        } else if (tbwe == _w.paTextBox) {
            _iterObs.setPosAngle(tbwe.getValue());

        } else {
            super.textBoxKeyPress(tbwe);
        }

        super._w.noiseTextBox.setValue(calculateNoise());
    }

    public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe,
            int index, String val) {
        if (ddlbwe == _w.coordSys) {
            _iterObs.setCoordSys(val);
        } else {
            super.dropDownListBoxAction(ddlbwe, index, val);
        }
    }

    private void setCanDoMap(SpInstObsComp sioc) {
        boolean canDoMap = false;

        SpInstHeterodyne hetSetUp = null;

        if (sioc instanceof SpInstHeterodyne) {
            hetSetUp = (SpInstHeterodyne) sioc;
        }

        if (hetSetUp != null) {
            boolean harp = hetSetUp.getFrontEnd().equalsIgnoreCase("HARP");
            boolean hasOffsetIterator = false;

            SpItem parent = _iterObs.parent();

            while (parent != null) {
                if (parent instanceof SpIterOffset) {
                    hasOffsetIterator = true;
                    break;
                }

                parent = parent.parent();
            }

            canDoMap = harp && !hasOffsetIterator;
        }

        _w.paTextBox.setEnabled(canDoMap);
        _w.coordSys.setEnabled(canDoMap);

        if (!canDoMap) {
            _iterObs.rmCoordSys();
            _iterObs.rmPosAngle();
            _w.paTextBox.setValue(0.0);

        } else {
            _w.paTextBox.setValue(_iterObs.getPosAngle());
            _w.coordSys.setValue(_iterObs.getCoordSys());
        }
    }
}
