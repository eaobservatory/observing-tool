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

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;

@SuppressWarnings("serial")
public class IterNoiseObsGUI extends IterJCMTGenericGUI {
    JPanel noisePanel = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    DropDownListBoxWidgetExt noiseSourceComboBox =
            new DropDownListBoxWidgetExt();
    CheckBoxWidgetExt currentAzimuth = new CheckBoxWidgetExt();
    JPanel azPanel = new JPanel();
    GridLayout layout = new GridLayout(1, 1);

    public IterNoiseObsGUI() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        Border bevelBorder =
                BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        Border titleBorder = BorderFactory.createTitledBorder(bevelBorder,
                "Noise setup");
        noisePanel.setBorder(titleBorder);
        noisePanel.setLayout(flowLayout1);
        secsPerCycle.setColumns(8);
        noiseSourceComboBox.setFont(new java.awt.Font("Dialog", 0, 12));
        noisePanel.add(noiseSourceComboBox, null);

        currentAzimuth.setText("Use Current Azimuth?");
        currentAzimuth.setFont(new java.awt.Font("Dialog", 0, 12));
        azPanel.setLayout(layout);
        azPanel.add(currentAzimuth);

        this.add(noisePanel, BorderLayout.CENTER);
        this.add(azPanel, BorderLayout.SOUTH);
    }
}
