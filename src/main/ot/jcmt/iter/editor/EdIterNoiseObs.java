/*
 * Copyright 2002 United Kingdom Astronomy Technology Centre, an
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

import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;

import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.iter.SpIterNoiseObs;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.inst.SpInstSCUBA2;

/**
 * This is the editor for Noise Observe Mode iterator component.
 *
 * @author modified for JCMT by Martin Folger (M.Folger@roe.ac.uk)
 */
public final class EdIterNoiseObs extends EdIterJCMTGeneric {
    private IterNoiseObsGUI _w; // the GUI layout panel
    private SpIterNoiseObs _iterObs;

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdIterNoiseObs() {
        super(new IterNoiseObsGUI());

        _title = "Noise";
        _presSource = _w = (IterNoiseObsGUI) super._w;
        _description = "Noise Observation Mode";

        _w.noiseSourceComboBox.addWatcher(this);
        _w.currentAzimuth.addWatcher(this);
    }

    /**
     * Override setup to store away a reference to the Noise Iterator.
     */
    public void setup(SpItem spItem) {
        _iterObs = (SpIterNoiseObs) spItem;
        super.setup(spItem);
    }

    protected void _updateWidgets() {
        super._updateWidgets();

        _w.noiseSourceComboBox.setValue(_iterObs.getNoiseSource());
        _w.currentAzimuth.setValue(_iterObs.getDoAtCurrentAz());
    }

    public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe,
            int index, String val) {
        if (ddlbwe == _w.noiseSourceComboBox) {
            _iterObs.setNoiseSource(
                    (String) _w.noiseSourceComboBox.getSelectedItem());

        } else {
            super.dropDownListBoxAction(ddlbwe, index, val);
        }
    }

    public void checkBoxAction(CheckBoxWidgetExt cbwe) {
        if (cbwe == _w.currentAzimuth) {
            _iterObs.setDoAtCurrentAz(_w.currentAzimuth.getBooleanValue());
        }
    }

    public void setInstrument(SpInstObsComp spInstObsComp) {
        super.setInstrument(spInstObsComp);

        boolean heterodyne = spInstObsComp instanceof SpInstHeterodyne;
        boolean scuba2 = spInstObsComp instanceof SpInstSCUBA2;
        _w.noiseSourceComboBox.setChoices(_iterObs.getNoiseSources());
        _w.noiseSourceComboBox.setSelectedItem(_iterObs.getNoiseSource());
        _w.noiseSourceComboBox.setEnabled((spInstObsComp != null)
                && (heterodyne || scuba2));
    }
}
