/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
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

import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.iter.SpIterSetupObs;

/**
 * Cloned from Skydip
 */
public final class EdIterSetupObs extends EdIterJCMTGeneric {
    private IterSetupObsGUI _w; // the GUI layout panel

    private SpIterSetupObs _iterObs;

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdIterSetupObs() {
        super(new IterSetupObsGUI());

        _title = "Setup";
        _presSource = _w = (IterSetupObsGUI) super._w;
        _description = "Setup Observation Mode";

        _w.currentAzimuth.addWatcher(this);
        _w.jPanel1.setVisible(false);
    }

    /**
     * Override setup to store away a reference to the Focus Iterator.
     */
    public void setup(SpItem spItem) {
        _iterObs = (SpIterSetupObs) spItem;

        super.setup(spItem);
    }

    protected void _updateWidgets() {
        _w.currentAzimuth.setValue(_iterObs.getDoAtCurrentAz());

        super._updateWidgets();
    }

    public void checkBoxAction(CheckBoxWidgetExt cbwe) {
        if (cbwe == _w.currentAzimuth) {
            _iterObs.setDoAtCurrentAz(_w.currentAzimuth.getBooleanValue());
        }
    }

    public void setInstrument(SpInstObsComp spInstObsComp) {
        super.setInstrument(spInstObsComp);
    }
}
