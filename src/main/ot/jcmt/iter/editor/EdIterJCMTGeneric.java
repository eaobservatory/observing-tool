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

import java.text.NumberFormat;

import jsky.app.ot.OtCfg;
import jsky.app.ot.editor.OtItemEditor;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;

import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.util.DDMMSS;
import gemini.util.CoordSys;
import gemini.util.RADec;
import gemini.sp.SpTelescopePos;

import orac.jcmt.inst.SpInstSCUBA2;
import orac.jcmt.inst.SpJCMTInstObsComp;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.iter.SpIterJCMTObs;
import orac.jcmt.iter.SpIterPointingObs;
import orac.jcmt.iter.SpIterFocusObs;
import orac.jcmt.obsComp.SpSiteQualityObsComp;
import orac.jcmt.util.Scuba2Noise;
import orac.jcmt.SpJCMTConstants;
import orac.util.CoordConvert;
import orac.util.DrUtil;
import orac.util.SpItemUtilities;
import ot.util.DialogUtil;

/**
 * This is the generic editor for JCMT iterator components.
 *
 * @author modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class EdIterJCMTGeneric extends OtItemEditor implements
        DropDownListBoxWidgetWatcher, TextBoxWidgetWatcher,
        CheckBoxWidgetWatcher, SpJCMTConstants {
    /**
     * Error code for observe modes whose noise calculation is not implemented
     * yet.
     *
     * This constant should remain distinct from the STATUS constants used in
     * {@link orac.util.DrUtil}.
     */
    protected static int NOISE_CALCULATION_STATUS_NOT_IMPLEMENTED = -5;

    protected IterJCMTGenericGUI _w; // the GUI layout panel
    protected SpIterJCMTObs _iterObs;
    protected String _noiseToolTip = "";

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdIterJCMTGeneric(IterJCMTGenericGUI w) {
        _title = "JCMT Observe";
        _presSource = _w = w;
        _description = "Iterator Component for JCMT";
        _w.switchingMode.addWatcher(this);
        _w.frequencyOffset_throw.addWatcher(this);
        _w.frequencyOffset_rate.addWatcher(this);
        _w.secsPerCycle.addWatcher(this);
        _w.cycleReversal.addWatcher(this);
        _w.continuousCal.addWatcher(this);
        _w.stepSize.addWatcher(this);
        _w.jiggleAtReference.addWatcher(this);
        _w.jigglesPerCycle.addWatcher(this);
        _w.sampleTime.addWatcher(this);
        _w.automaticTarget.addWatcher(this);
    }

    /**
     * Override setup to store away a reference to the Scan Iterator.
     */
    public void setup(SpItem spItem) {
        _iterObs = (SpIterJCMTObs) spItem;
        super.setup(spItem);

        if (! (spItem instanceof SpIterPointingObs
                || spItem instanceof SpIterFocusObs)) {
            _w.automaticTarget.setEnabled(false);
        }

        _w.switchingMode.deleteWatcher(this);
        _w.switchingMode.setChoices(_iterObs.getSwitchingModeOptions());

        _w.switchingMode.setEnabled(_w.switchingMode.getItemCount() > 1);

        _w.switchingMode.setValue(_iterObs.getSwitchingMode());

        _w.switchingMode.addWatcher(this);
    }

    public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe,
            int index, String val) {
        if (ddlbwe == _w.switchingMode) {
            if (val.equals(SWITCHING_MODE_FREQUENCY_S)) {
                _w.frequencyPanel.setVisible(true);
                _w.frequencyOffset_rate.setEnabled(false);
                _w.switchRateVisible(true);

                if (_iterObs.getSecsPerCycle() != 0) {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setMaximumFractionDigits(5);
                    _w.frequencyOffset_rate.setValue(nf.format(
                            1.0 / _iterObs.getSecsPerCycle()));

                } else {
                    _w.frequencyOffset_rate.setValue(0.0);
                }

                _iterObs.setFrequencyOffsetRate(
                        _w.frequencyOffset_rate.getValue());
                _iterObs.setFrequencyOffsetThrow(
                        _w.frequencyOffset_throw.getValue());

            } else if (val.equals(SWITCHING_MODE_FREQUENCY_F)) {
                _w.frequencyPanel.setVisible(true);
                _w.frequencyOffset_rate.setEnabled(false);
                _w.switchRateVisible(false);
                _w.frequencyPanel.setVisible(false);
                _iterObs.rmFrequencyOffsetRate();
                _iterObs.setFrequencyOffsetThrow(
                        _w.frequencyOffset_throw.getValue());

            } else {
                _iterObs.rmFrequencyOffsetValues();
            }

            _iterObs.setSwitchingMode(val);
            _updateWidgets();
        }
    }

    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
        if (tbwe == _w.frequencyOffset_throw) {
            _iterObs.setFrequencyOffsetThrow(tbwe.getValue());

        } else if (tbwe == _w.frequencyOffset_rate) {
            _iterObs.setFrequencyOffsetRate(tbwe.getValue());

        } else if (tbwe == _w.secsPerCycle) {
            _iterObs.setSecsPerCycle(_w.secsPerCycle.getValue());

            if (!_w.frequencyOffset_rate.isEnabled()
                    && _w.frequencyOffset_rate.isVisible()) {
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(5);
                _w.frequencyOffset_rate.setValue(nf.format(
                        1.0 / _iterObs.getSecsPerCycle()));
                _iterObs.setFrequencyOffsetRate(
                        _w.frequencyOffset_rate.getValue());
            }

        } else if (tbwe == _w.jigglesPerCycle) {
            _iterObs.setJigglesPerCycle(_w.jigglesPerCycle.getValue());

        } else if (tbwe == _w.stepSize) {
            _iterObs.setStepSize(_w.stepSize.getValue());

        } else if (tbwe == _w.sampleTime) {
            _iterObs.setSampleTime(_w.sampleTime.getValue());
        }
    }

    public void textBoxAction(TextBoxWidgetExt tbwe) {
    }

    public void checkBoxAction(CheckBoxWidgetExt cbwe) {
        if (cbwe == _w.cycleReversal) {
            _iterObs.setCycleReversal(_w.cycleReversal.getBooleanValue());

        } else if (cbwe == _w.jiggleAtReference) {
            _iterObs.setJiggleAtReference(
                    _w.jiggleAtReference.getBooleanValue());

        } else if (cbwe == _w.automaticTarget) {
            _iterObs.setAutomaticTarget(_w.automaticTarget.getBooleanValue());

        } else if (cbwe == _w.continuousCal) {
            _iterObs.setContinuousCal(_w.continuousCal.getBooleanValue());
        }
    }

    protected void _updateWidgets() {
        setInstrument(SpTreeMan.findInstrument(_spItem));

        String switchingMode = _iterObs.getSwitchingMode();
        _w.switchingMode.setValue(switchingMode);

        if ((switchingMode != null)
                && (SWITCHING_MODE_FREQUENCY_S.equals(switchingMode)
                        || SWITCHING_MODE_FREQUENCY_F.equals(switchingMode))) {
            _w.frequencyPanel.setVisible(true);
            _w.frequencyOffset_rate.setEnabled(false);
            _w.switchRateVisible(
                    !SWITCHING_MODE_FREQUENCY_F.equals(switchingMode));

        } else {
            _w.frequencyPanel.setVisible(false);
        }

        _w.frequencyOffset_throw.setValue(_iterObs.getFrequencyOffsetThrow());
        _w.frequencyOffset_rate.setValue(_iterObs.getFrequencyOffsetRate());
        _w.secsPerCycle.setValue(_iterObs.getSecsPerCycle());
        _w.cycleReversal.setValue(_iterObs.getCycleReversal());
        _w.stepSize.setValue(_iterObs.getStepSize());
        _w.jiggleAtReference.setValue(_iterObs.getJiggleAtReference());
        _w.jigglesPerCycle.setValue(_iterObs.getJigglesPerCycle());
        _w.sampleTime.setValue(_iterObs.getSampleTime());
        _w.automaticTarget.setValue(_iterObs.getAutomaticTarget());
        _w.noiseTextBox.setValue(calculateNoise());
        _w.noiseTextBox.setToolTipText(_noiseToolTip);
        _w.continuousCal.setValue(_iterObs.getContinuousCal());
    }

    /**
     * This method should be overridden by subclasses representing iterators
     * whose appearance is different for different instruments.
     */
    public void setInstrument(SpInstObsComp spInstObsComp) {
        if ((spInstObsComp != null)
                && (spInstObsComp instanceof SpInstHeterodyne)) {
            _w.switchingMode.setVisible(true);
            _w.switchingModeLabel.setVisible(true);
            _w.noiseUnitLabel.setText("K");

            _w.frequencyPanel.setVisible(_w.switchingMode.getValue() != null
                    && (_w.switchingMode.getValue().equals(
                                    SWITCHING_MODE_FREQUENCY_S)
                            || _w.switchingMode.getValue().equals(
                                    SWITCHING_MODE_FREQUENCY_F)));
        } else {
            _w.noiseUnitLabel.setText("mJy");
            _w.switchingMode.setVisible(false);
            _w.switchingModeLabel.setVisible(false);
            _w.frequencyPanel.setVisible(false);
        }
    }

    /**
     * Returns noise information.
     */
    protected String calculateNoise() {
        SpTelescopeObsComp telescopeObsComp =
                SpTreeMan.findTargetList(_iterObs);

        if (telescopeObsComp == null) {
            _noiseToolTip = "No target";

            return "No target";
        }

        SpJCMTInstObsComp instObsComp =
                (SpJCMTInstObsComp) SpTreeMan.findInstrument(_iterObs);

        if (instObsComp == null) {
            _noiseToolTip = "No instruments";

            return "No instrument";
        }

        SpSiteQualityObsComp siteQualityObsComp = (SpSiteQualityObsComp)
                SpItemUtilities.findSiteQuality(_iterObs);

        if (siteQualityObsComp == null) {
            _noiseToolTip = "No site quality";

            return "No site quality";
        }

        double airmass = 0.0;

        try {
            SpTelescopePos base =
                    telescopeObsComp.getPosList().getBasePosition();

            if (base.getCoordSys() == CoordSys.FK5) {
                airmass = DrUtil.airmass(base.getYaxis(),
                        DDMMSS.valueOf(OtCfg.getTelescopeLatitude()));

            } else if (base.getCoordSys() == CoordSys.AZ_EL) {
                airmass = DrUtil.airmass(base.getYaxis());

            } else {
                double yAxis = convertPosition(base.getCoordSys(),
                        base.getXaxis(), base.getYaxis());
                airmass = DrUtil.airmass(yAxis,
                        DDMMSS.valueOf(OtCfg.getTelescopeLatitude()));
            }

            double csoTau = siteQualityObsComp.getNoiseCalculationTau();

            if (instObsComp instanceof SpInstHeterodyne) {
                return "" + calculateNoise((SpInstHeterodyne) instObsComp,
                                airmass, csoTau);

            } else if (instObsComp instanceof SpInstSCUBA2) {
                double fourFifty = calculateNoise((SpInstSCUBA2) instObsComp,
                        Scuba2Noise.four50, airmass, csoTau);

                double eightFifty = calculateNoise((SpInstSCUBA2) instObsComp,
                        Scuba2Noise.eight50, airmass, csoTau);

                _noiseToolTip = "airmass = " + (Math.rint(airmass * 10) / 10)
                        + ",  (450) = " + fourFifty
                        + ",  (850) = " + eightFifty;

                return "" + fourFifty + "@450," + eightFifty + "@850";
            }

        } catch (Exception e) {
            _noiseToolTip = e.toString();

            return "Not available";
        }

        _noiseToolTip = "Not available";

        return "Not available";
    }

    /**
     * Returns noise.
     *
     * This method should be implemented by subclasses taking into account
     * the observe mode and whether length and width are needed
     * (SCAN more only).
     */
    protected double calculateNoise(double wavelength, double nefd,
            int[] status) {
        status[0] = NOISE_CALCULATION_STATUS_NOT_IMPLEMENTED;

        return 0.0;
    }

    protected double calculateNoise(SpInstHeterodyne inst, double airmass,
            double tau) {
        return 0.0;
    }

    protected double calculateNoise(SpInstSCUBA2 inst, String wavelength,
            double airmass, double tau) {
        return 0.0;
    }

    /**
     * If coordinate system is FK4 or Galactic, convert to y axis to FK5
     * otherwise return 0.
     *
     * Method is safe to pass FK5 or AzEl to, it simply returns the yAxis
     *
     * @param coordSys integer from a CoordSys object
     * @param xAxis from a base position
     * @param yAxis from a base position
     *
     * @return y axis converted to FK5 or 0.
     */
    protected double convertPosition(int coordSys, double xAxis, double yAxis) {
        RADec raDec = null;

        double dec = 0.0;

        if (coordSys == CoordSys.FK4) {
            raDec = CoordConvert.Fk45z(xAxis, yAxis);
            dec = raDec.dec;

        } else if (coordSys == CoordSys.GAL) {
            raDec = CoordConvert.gal2fk5(xAxis, yAxis);
            dec = raDec.dec;

        } else if (coordSys == CoordSys.FK5 || coordSys == CoordSys.AZ_EL) {
            dec = yAxis;

        } else {
            DialogUtil.error(_w,
                    "Coordinate system " + CoordSys.getSystem(coordSys)
                            + " not properly supported in noise calculation.");
        }

        return dec;
    }
}
