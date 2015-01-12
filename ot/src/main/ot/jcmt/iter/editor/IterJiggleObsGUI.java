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
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class IterJiggleObsGUI extends IterJCMTGenericGUI {
    JPanel acsisPanel = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel3 = new JLabel();
    JLabel jLabel4 = new JLabel();
    JLabel jLabel5 = new JLabel();
    JLabel jLabel6 = new JLabel();
    JLabel jLabel7 = new JLabel();
    JLabel jLabel8 = new JLabel();
    JLabel jLabel9 = new JLabel();
    JLabel jLabel10 = new JLabel();
    TextBoxWidgetExt secsPerJiggle = new TextBoxWidgetExt();
    TextBoxWidgetExt secsPerObs = new TextBoxWidgetExt();
    CommandButtonWidgetExt defaultButton = new CommandButtonWidgetExt();
    JPanel jPanel1 = new JPanel();
    JLabel jLabel1 = new JLabel();
    DropDownListBoxWidgetExt jigglePattern = new DropDownListBoxWidgetExt();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JPanel jPanel2 = new JPanel();
    TitledBorder titledBorder1;
    JLabel jLabel11 = new JLabel();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    TextBoxWidgetExt paTextBox = new TextBoxWidgetExt();
    JLabel jLabel12 = new JLabel();
    JLabel jLabel13 = new JLabel();
    DropDownListBoxWidgetExt dropDownListBoxWidgetExt1 =
            new DropDownListBoxWidgetExt();
    CheckBoxWidgetExt contModeCB = new CheckBoxWidgetExt();
    TextBoxWidgetExt scaleFactor = new TextBoxWidgetExt();
    DropDownListBoxWidgetExt coordSys = new DropDownListBoxWidgetExt();
    JLabel informationLabelTop = new JLabel();
    JLabel informationLabelMiddle = new JLabel();
    JLabel informationLabelBottom = new JLabel();
    JPanel informationPanel = new JPanel();

    public IterJiggleObsGUI() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        JPanel jigglePanel = new JPanel();
        jigglePanel.setLayout(new BorderLayout());
        titledBorder1 = new TitledBorder("");
        Border bevelBorder = BorderFactory.createBevelBorder(
                BevelBorder.LOWERED);
        Border titleBorder = BorderFactory.createTitledBorder(
                bevelBorder, "Jiggle setup");
        jigglePanel.setBorder(titleBorder);
        acsisPanel.setLayout(gridBagLayout1);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setForeground(Color.black);
        jLabel2.setText("Jiggle Spacing");
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setForeground(Color.black);
        jLabel3.setText("Sample Time");
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel4.setForeground(Color.black);
        jLabel4.setText("Jiggles/Cycle");
        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setForeground(Color.black);
        jLabel5.setText("No of Cycles");
        jLabel6.setFont(new java.awt.Font("Dialog", Font.ITALIC, 12));
        jLabel6.setForeground(Color.black);
        jLabel6.setText("(arcsec)");
        scaleFactor.setColumns(6);
        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel7.setForeground(Color.black);
        jLabel7.setText("(msec)");
        sampleTime.setColumns(6);
        jigglesPerCycle.setColumns(6);
        noOfCycles.setColumns(6);
        jiggleAtReference.setText("Jiggle at Reference");
        jiggleAtReference.setFont(new java.awt.Font("Dialog", 0, 12));
        cycleReversal.setText("Cycle Reversal");
        cycleReversal.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setForeground(Color.black);
        jLabel8.setText("secs/jig posn");
        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel9.setForeground(Color.black);
        jLabel9.setText("secs/jiggle");
        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel10.setForeground(Color.black);
        jLabel10.setText("secs/obs");
        secsPerJiggle.setEditable(false);
        secsPerJiggle.setColumns(10);
        secsPerCycle.setEditable(true);
        secsPerCycle.setColumns(5);
        defaultButton.setText("Default");
        secsPerObs.setEditable(false);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Jiggle Pattern");
        jigglePattern.setFont(new java.awt.Font("Dialog", 0, 12));
        jPanel1.setLayout(gridBagLayout2);
        jPanel2.setBorder(new TitledBorder(BorderFactory.createLineBorder(
                new Color(153, 153, 153), 2), "Map"));
        jPanel2.setLayout(gridBagLayout3);
        jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel11.setForeground(Color.black);
        jLabel11.setText("PA");
        paTextBox.setColumns(5);
        jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel12.setForeground(Color.black);
        jLabel12.setText("(degrees)");
        jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel13.setForeground(Color.black);
        jLabel13.setText("System");
        contModeCB.setText("Continuum Mode");
        contModeCB.setFont(new java.awt.Font("Dialog", 0, 12));
        contModeCB.setForeground(Color.black);
        contModeCB.setHorizontalTextPosition(SwingConstants.LEFT);

        coordSys.setFont(new java.awt.Font("Dialog", 0, 12));
        coordSys.setForeground(Color.black);

        this.add(jigglePanel, BorderLayout.CENTER);
        jigglePanel.add(acsisPanel, BorderLayout.SOUTH);
        // Add Scale Factor
        acsisPanel.add(jLabel2, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        acsisPanel.add(scaleFactor, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        acsisPanel.add(jLabel6, new GridBagConstraints(
                2, 0, 1, 1, 0., 0.,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        // Add secs/cycle
        acsisPanel.add(jLabel8, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        acsisPanel.add(secsPerCycle, new GridBagConstraints(
                1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        // Add a coninuum mode checkbox
        acsisPanel.add(contModeCB, new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        // Add the default button
        acsisPanel.add(defaultButton, new GridBagConstraints(
                1, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        // Add the Map panel to the acsis panel
        acsisPanel.add(jPanel2, new GridBagConstraints(
                3, 0, 1, 3, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 0), 0, 0));

        // Add the PA info to the Map panel
        jPanel2.add(jLabel11, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 5, 0), 0, 0));
        jPanel2.add(paTextBox, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 0), 0, 0));
        jPanel2.add(jLabel12, new GridBagConstraints(
                2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        // Add the jiggle pattern box
        jPanel2.add(jLabel13, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel2.add(coordSys, new GridBagConstraints(
                1, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 0), 0, 0));

        jigglePanel.add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(jLabel1, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        jPanel1.add(jigglePattern, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));

        informationLabelTop.setText(
                "Warning: Using continuum mode will significantly increase"
                + " the duration of the observation.");
        informationLabelTop.setFont(new java.awt.Font("Dialog", 0, 12));
        informationLabelTop.setForeground(Color.black);
        informationLabelMiddle.setText(
                "Continuum mode should only be used if an accurate measure"
                + " of the continuum emission");
        informationLabelMiddle.setFont(new java.awt.Font("Dialog", 0, 12));
        informationLabelMiddle.setForeground(Color.black);
        informationLabelBottom.setText("from the source is a requirement.");
        informationLabelBottom.setFont(new java.awt.Font("Dialog", 0, 12));
        informationLabelBottom.setForeground(Color.black);

        informationPanel.setLayout(new GridLayout(3, 1));

        this.add(informationPanel, BorderLayout.SOUTH);
        informationPanel.add(informationLabelTop);
        informationPanel.add(informationLabelMiddle);
        informationPanel.add(informationLabelBottom);
    }
}
