/*
 * Copyright (C) 2008 Science and Technology Facilities Council.
 * All Rights Reserved.
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

import orac.jcmt.iter.SpIterArrayTestObs;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;

public final class EdIterArrayTestObs extends EdIterJCMTGeneric {
    private IterArrayTestObsGUI _w; // the GUI layout panel

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdIterArrayTestObs() {
        super(new IterArrayTestObsGUI());

        _title = "Array Test";
        _presSource = _w = (IterArrayTestObsGUI) super._w;
        _description = "Array Test Observation Mode";

        _w.jPanel1.setVisible(false);
    }

    /**
     * Override setup to store away a reference to the Array Test.
     */
    public void setup(SpItem spItem) {
        _iterObs = (SpIterArrayTestObs) spItem;

        super.setup(spItem);
    }

    protected void _updateWidgets() {
        super._updateWidgets();
    }

    public void setInstrument(SpInstObsComp spInstObsComp) {
        _w.jPanel1.setVisible(false);

        super.setInstrument(spInstObsComp);
    }
}
