/*
 * Copyright (C) 2020 East Asian Observatory
 * All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful,but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc.,51 Franklin
 * Street, Fifth Floor, Boston, MA  02110-1301, USA
 */

package ot.jcmt.iter.editor;

import gemini.sp.SpItem;
import orac.jcmt.iter.SpIterRawXmlObs;

public final class EdIterRawXmlObs extends EdIterJCMTGeneric {
    private IterRawXmlObsGUI _w;

    private SpIterRawXmlObs _iterObs;

    public EdIterRawXmlObs() {
        super(new IterRawXmlObsGUI());

        _title = "Raw XML Observation";
        _presSource = _w = (IterRawXmlObsGUI) super._w;
        _description = "Raw XML Observation Mode";

        _w.jPanel1.setVisible(false);
    }

    public void setup(SpItem spItem) {
        _iterObs = (SpIterRawXmlObs) spItem;

        super.setup(spItem);
    }

    protected void _updateWidgets() {
        super._updateWidgets();
    }
}
