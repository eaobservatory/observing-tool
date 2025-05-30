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

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.BevelBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.BorderLayout;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;

@SuppressWarnings("serial")
public class IterFocusObsGUI extends IterJCMTGenericGUI {
    JPanel scubaAcsisPanel = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel3 = new JLabel();
    DropDownListBoxWidgetExt axis = new DropDownListBoxWidgetExt();
    JLabel stepsLabel = new JLabel();
    TextBoxWidgetExt steps = new TextBoxWidgetExt();
    JLabel mmLabel = new JLabel();
    JLabel jLabel6 = new JLabel();
    TextBoxWidgetExt focusPoints = new TextBoxWidgetExt();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JPanel acsisPanel = new JPanel();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel1 = new JLabel();
    JCheckBox fts2_in_beam = new JCheckBox("FTS-2 in beam", false);
    JCheckBox pol2_in_beam = new JCheckBox("POL-2 in beam", false);

    public IterFocusObsGUI() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        scubaAcsisPanel.setLayout(gridBagLayout1);
        Border bevelBorder =
                BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        Border titleBorder = BorderFactory.createTitledBorder(bevelBorder,
                "Focus setup");
        scubaAcsisPanel.setBorder(titleBorder);
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setForeground(Color.black);
        jLabel3.setText("Axis");
        stepsLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        stepsLabel.setForeground(Color.black);
        stepsLabel.setText("Steps");
        steps.setColumns(8);
        mmLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        mmLabel.setForeground(Color.black);
        mmLabel.setText("(mm)");
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel6.setForeground(Color.black);
        jLabel6.setText("No of Focus Points");
        secsPerCycle.setColumns(8);
        acsisPanel.setLayout(gridBagLayout2);
        cycleReversal.setText("Cycle Reversal");
        cycleReversal.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Secs per Cycle");
        axis.setFont(new java.awt.Font("Dialog", 0, 12));
        this.add(scubaAcsisPanel, BorderLayout.CENTER);
        scubaAcsisPanel.add(jLabel3, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        scubaAcsisPanel.add(axis, new GridBagConstraints(
                1, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        scubaAcsisPanel.add(stepsLabel, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 20, 0, 0), 0, 0));
        scubaAcsisPanel.add(steps, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        scubaAcsisPanel.add(mmLabel, new GridBagConstraints(
                2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        scubaAcsisPanel.add(jLabel6, new GridBagConstraints(
                0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        scubaAcsisPanel.add(focusPoints, new GridBagConstraints(
                1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        scubaAcsisPanel.add(fts2_in_beam, new GridBagConstraints(
                0, 5, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(20, 0, 0, 0), 0, 0));
        scubaAcsisPanel.add(pol2_in_beam, new GridBagConstraints(
                0, 6, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(20, 0, 0, 0), 0, 0));
        this.add(acsisPanel, BorderLayout.SOUTH);
    }
}
