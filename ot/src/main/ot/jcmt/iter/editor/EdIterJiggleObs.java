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

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;

import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.util.MathUtil;
import orac.jcmt.SpJCMTConstants;
import orac.jcmt.inst.SpJCMTInstObsComp;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.iter.SpIterJiggleObs;
import orac.jcmt.util.HeterodyneNoise;

/**
 * This is the editor for Jiggle Observe Mode iterator component.
 *
 * @author modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public final class EdIterJiggleObs extends EdIterJCMTGeneric implements
        CommandButtonWidgetWatcher, SpJCMTConstants {
    private IterJiggleObsGUI _w; // the GUI layout panel
    private SpIterJiggleObs _iterObs;
    private String[] _noJigglePatterns = {"No Instrument in scope."};

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdIterJiggleObs() {
        super(new IterJiggleObsGUI());

        _title = "Jiggle";
        _presSource = _w = (IterJiggleObsGUI) super._w;
        _description = "Jiggle Observation Mode";

        _w.coordSys.setChoices(SpIterJiggleObs.JIGGLE_SYSTEMS);

        _w.jigglePattern.addWatcher(this);
        _w.scaleFactor.addWatcher(this);
        _w.contModeCB.addWatcher(this);
        _w.defaultButton.addWatcher(this);
        _w.paTextBox.addWatcher(this);
        _w.coordSys.addWatcher(this);

        super._w.separateOffs.addWatcher(this);
        super._w.separateOffsLabel.setVisible(true);
        super._w.separateOffs.setVisible(true);
    }

    /**
     * Override setup to store away a reference to the Focus Iterator.
     */
    public void setup(SpItem spItem) {
        _iterObs = (SpIterJiggleObs) spItem;
        super.setup(spItem);
    }

    protected void _updateWidgets() {
        SpJCMTInstObsComp instObsComp = (SpJCMTInstObsComp)
                SpTreeMan.findInstrument(_iterObs);

        if (instObsComp != null) {
            _w.jigglePattern.setChoices(instObsComp.getJigglePatterns());

            // Select jiggle pattern.
            boolean jigglePatternSet = false;
            String jigglePattern = _iterObs.getJigglePattern();

            if (jigglePattern != null) {
                for (int i = 0; i < _w.jigglePattern.getItemCount(); i++) {
                    if (jigglePattern.equals(_w.jigglePattern.getItemAt(i))) {
                        _w.jigglePattern.setValue(
                                _w.jigglePattern.getItemAt(i));
                        jigglePatternSet = true;

                        break;
                    }
                }
            }

            if (!jigglePatternSet) {
                _iterObs.setJigglePattern((String) _w.jigglePattern.getValue());
            }

            if (instObsComp instanceof SpInstHeterodyne) {
                String switchingMode = _iterObs.getSwitchingMode();

                if (switchingMode == null) {
                    _iterObs.setSwitchingMode(SWITCHING_MODE_NONE);
                }

                boolean freqSwitching = switchingMode.startsWith("Frequency");
                super._w.separateOffs.setEnabled(!freqSwitching);

                if (freqSwitching) {
                    super._w.separateOffs.setSelected(true);
                    _iterObs.setSeparateOffs(true);
                    _iterObs.setContinuumMode(false);
                    _w.contModeCB.setSelected(false);
                }

                _w.contModeCB.setSelected(_iterObs.isContinuum());
                _w.scaleFactor.setValue(_iterObs.getScaleFactor());
            }

        } else {
            _w.jigglePattern.setChoices(_noJigglePatterns);
            _iterObs.setJigglePattern("");
        }

        _w.paTextBox.setValue(_iterObs.getPosAngle());
        _w.coordSys.setValue(_iterObs.getCoordSys());

        _w.scaleFactor.setEnabled(!isHarp());

        super._w.separateOffs.setSelected(_iterObs.hasSeparateOffs());

        super._updateWidgets();
    }

    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
        if (tbwe == _w.paTextBox) {
            _iterObs.setPosAngle(tbwe.getValue());

        } else if (tbwe == _w.scaleFactor) {
            _iterObs.setScaleFactor(tbwe.getDoubleValue(1.0));

        } else {
            super.textBoxKeyPress(tbwe);
        }

        _w.noiseTextBox.setValue(calculateNoise());
        _w.noiseTextBox.setToolTipText(_noiseToolTip);
    }

    public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe,
            int index, String val) {
        if (ddlbwe == _w.jigglePattern) {
            _iterObs.setJigglePattern(val);

            if (SpTreeMan.findInstrument(_iterObs)
                    instanceof SpInstHeterodyne) {
                if (isHarp()) {
                    double scaleFactor = _iterObs.getScaleFactor();
                    String[] split = val.split("HARP");

                    if (split[1].matches("4\\w*")) {
                        scaleFactor = 7.5;
                    } else {
                        scaleFactor = 6.0;
                    }

                    _w.scaleFactor.setValue(scaleFactor);
                    _iterObs.setScaleFactor(scaleFactor);
                }
            }

            _updateWidgets();

        } else if (ddlbwe == _w.coordSys) {
            _iterObs.setCoordSys(val);

        } else {
            super.dropDownListBoxAction(ddlbwe, index, val);
        }
    }

    private boolean isHarp() {
        String pattern = _iterObs.getJigglePattern();
        boolean isHarp = false;

        if (pattern != null) {
            isHarp = pattern.startsWith("HARP");
        }

        return isHarp;
    }

    public void checkBoxAction(CheckBoxWidgetExt cbwe) {
        if (cbwe == _w.contModeCB) {
            boolean isSelected = _w.contModeCB.isSelected();
            _iterObs.setContinuumMode(isSelected);

            if (isSelected) {
                super._w.separateOffs.setSelected(true);
                _iterObs.setSeparateOffs(true);
            }

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

    public void commandButtonAction(CommandButtonWidgetExt cbwe) {
        if (cbwe == _w.defaultButton) {
            SpInstObsComp instrument = SpTreeMan.findInstrument(_iterObs);

            if (instrument instanceof SpInstHeterodyne) {
                String frontend = ((SpInstHeterodyne) instrument).getFrontEnd();

                if (frontend.equals("HARP") && !isHarp()) {
                    for (int i = 0; i < _w.jigglePattern.getItemCount(); i++) {
                        String jigglePattern = (String)
                                _w.jigglePattern.getItemAt(i);

                        if (jigglePattern.startsWith("HARP")) {
                            _w.jigglePattern.setValue(jigglePattern);
                            _iterObs.setJigglePattern(jigglePattern);
                            dropDownListBoxAction(_w.jigglePattern, i,
                                    jigglePattern);
                            break;
                        }
                    }

                } else {
                    for (int i = 0; i < _w.jigglePattern.getItemCount(); i++) {
                        String jigglePattern = (String) _w.jigglePattern
                                .getItemAt(i);

                        if (!jigglePattern.startsWith("HARP")) {
                            _w.jigglePattern.setValue(jigglePattern);
                            _iterObs.setJigglePattern(jigglePattern);
                            dropDownListBoxAction(_w.jigglePattern, i,
                                    jigglePattern);
                            break;
                        }
                    }

                    _iterObs.setAcsisDefaults();
                }
            }
        }

        _updateWidgets();
    }

    public void setInstrument(SpInstObsComp spInstObsComp) {
        super.setInstrument(spInstObsComp);

        if ((spInstObsComp != null)
                && (spInstObsComp instanceof SpInstHeterodyne)) {
            _w.switchingMode.setChoices(_iterObs.getSwitchingModeOptions());

            if (_iterObs == null) {
                _w.switchingMode.setValue(SpJCMTConstants.SWITCHING_MODE_BEAM);

            } else {
                _w.switchingMode.setValue(_iterObs.getSwitchingMode());
            }

            _w.acsisPanel.setVisible(true);

        } else {
            _w.acsisPanel.setVisible(false);
        }
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
}
