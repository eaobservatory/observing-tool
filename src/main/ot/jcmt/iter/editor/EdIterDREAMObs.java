/*
 * Copyright (C) 2007-2010 Science and Technology Facilities Council.
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

import java.util.Observer;
import java.util.Observable;
import java.awt.event.KeyListener;

import jsky.app.ot.gui.TextBoxWidgetExt;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.iter.SpIterDREAMObs;

public final class EdIterDREAMObs extends EdIterJCMTGeneric
        implements Observer, KeyListener {
    private IterDREAMObsGUI _w;

    public EdIterDREAMObs() {
        super(new IterDREAMObsGUI());

        _title = "DREAM";
        _presSource = _w = (IterDREAMObsGUI) super._w;
        _description = "DREAM Observation Mode";
    }

    public void setup(SpItem spItem) {
        _iterObs = (SpIterDREAMObs) spItem;

        super.setup(spItem);

        _w.secsPerObservation.addWatcher(this);
        _iterObs.setSampleTime("" + _iterObs.getSampleTime());
    }

    public void setInstrument(SpInstObsComp spInstObsComp) {
        super.setInstrument(spInstObsComp);
    }

    protected void _updateWidgets() {
        super._updateWidgets();

        _w.secsPerObservation.setText("" + _iterObs.getSampleTime());
    }

    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
        _iterObs.getAvEditFSM().deleteObserver(this);

        if (tbwe == _w.secsPerObservation) {
            String input = _w.secsPerObservation.getText();
            if (input.matches("[0-9]*[.]?[0-9]*")) {
                double value = new Double(input);

                if (value != 0.0) {
                    _iterObs.setSampleTime(input);
                }
            }
        }

        super.textBoxKeyPress(tbwe);

        _iterObs.getAvEditFSM().addObserver(this);

    }

    public void keyPressed(java.awt.event.KeyEvent e) {
    }

    public void keyTyped(java.awt.event.KeyEvent e) {
    }

    public void keyReleased(java.awt.event.KeyEvent e) {
    }

    public void update(Observable observable, Object object) {
    }
}
