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

import java.util.ArrayList;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class IterJCMTGenericGUI extends JPanel {
    TextBoxWidgetExt secsPerCycle = new TextBoxWidgetExt();
    TextBoxWidgetExt noOfCycles = new TextBoxWidgetExt();
    TextBoxWidgetExt stepSize = new TextBoxWidgetExt();
    TextBoxWidgetExt jigglesPerCycle = new TextBoxWidgetExt();
    TextBoxWidgetExt sampleTime = new TextBoxWidgetExt();
    CheckBoxWidgetExt cycleReversal = new CheckBoxWidgetExt();
    CheckBoxWidgetExt jiggleAtReference = new CheckBoxWidgetExt();
    CheckBoxWidgetExt automaticTarget = new CheckBoxWidgetExt();
    CheckBoxWidgetExt continuousCal = new CheckBoxWidgetExt();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel1 = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    CheckBoxWidgetExt doAtCurrentAzDDLBWE = new CheckBoxWidgetExt();
    JLabel switchingModeLabel = new JLabel();
    DropDownListBoxWidgetExt switchingMode = new DropDownListBoxWidgetExt();
    JLabel jLabel4 = new JLabel();
    TitledBorder titledBorder1;
    Border border1;
    TextBoxWidgetExt frequencyOffset_throw = new TextBoxWidgetExt();
    JPanel frequencyPanel = new JPanel();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    JLabel hertzLabel = new JLabel();
    JLabel jLabel10 = new JLabel();
    TextBoxWidgetExt frequencyOffset_rate = new TextBoxWidgetExt();
    JLabel rateLabel = new JLabel();
    JLabel jLabel8 = new JLabel();
    JLabel noiseLabel = new JLabel();
    TextBoxWidgetExt noiseTextBox = new TextBoxWidgetExt();
    JLabel noiseUnitLabel = new JLabel();
    CheckBoxWidgetExt arrayCentred = new CheckBoxWidgetExt();
    JLabel arrayCentredLabel = new JLabel();
    JPanel arrayCentredPanel = new JPanel();
    CheckBoxWidgetExt separateOffs = new CheckBoxWidgetExt();
    JLabel separateOffsLabel = new JLabel();
    JPanel separateOffsPanel = new JPanel();

    JPanel rotatorAnglesPanel = new JPanel();

    ArrayList<RotatorAngleListener> rotatorAngleListeners
        = new ArrayList<RotatorAngleListener>();

    public IterJCMTGenericGUI() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        titledBorder1 = new TitledBorder("");
        border1 = BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(
                        BevelBorder.LOWERED,
                        Color.white,
                        Color.white,
                        new Color(134, 134, 134),
                        new Color(93, 93, 93)),
                        BorderFactory.createEmptyBorder(5, 0, 0, 0));
        this.setLayout(borderLayout1);
        Border bevelBorder =
                BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        Border genBorder = BorderFactory.createTitledBorder(bevelBorder,
                "General Setup");
        jPanel1.setBorder(genBorder);
        jPanel1.setLayout(gridBagLayout1);
        switchingModeLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        switchingModeLabel.setForeground(Color.black);
        switchingModeLabel.setText("Switching Mode");
        jLabel4.setText("jLabel4");
        this.setBorder(border1);
        switchingMode.setFont(new java.awt.Font("Dialog", 0, 12));
        frequencyOffset_throw.setColumns(10);
        frequencyPanel.setLayout(gridBagLayout3);
        frequencyPanel.setBorder(new TitledBorder(
                BorderFactory.createLineBorder(new Color(153, 153, 153), 2),
                        "Frequency Offset"));
        frequencyPanel.setVisible(false);
        hertzLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        hertzLabel.setForeground(Color.black);
        hertzLabel.setText("(Hz)");
        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel10.setForeground(Color.black);
        jLabel10.setText("(MHz)");
        frequencyOffset_rate.setColumns(10);
        rateLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        rateLabel.setForeground(Color.black);
        rateLabel.setText("Rate");
        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setForeground(Color.black);
        jLabel8.setText("Throw");

        noiseLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        noiseLabel.setForeground(Color.black);
        noiseLabel.setText("Noise");
        noiseTextBox.setEditable(false);
        noiseTextBox.setColumns(20);

        arrayCentredLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        arrayCentredLabel.setForeground(Color.black);
        arrayCentredLabel.setText("Array Centred");

        arrayCentredPanel.setLayout(new GridLayout(1, 2));
        arrayCentredPanel.add(arrayCentredLabel);
        arrayCentredPanel.add(arrayCentred);
        arrayCentredLabel.setVisible(false);
        arrayCentred.setVisible(false);

        separateOffsLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        separateOffsLabel.setForeground(Color.black);
        separateOffsLabel.setText("Separate Offs");

        separateOffsPanel.setLayout(new GridLayout(1, 2));
        separateOffsPanel.add(separateOffsLabel);
        separateOffsPanel.add(separateOffs);
        separateOffsLabel.setVisible(false);
        separateOffs.setVisible(false);

        noiseUnitLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        noiseUnitLabel.setForeground(Color.black);
        noiseUnitLabel.setText("(mJy)");

        rotatorAnglesPanel.setBorder(new TitledBorder(
                BorderFactory.createLineBorder(new Color(153, 153, 153), 2),
                        "Rotator Angles"));
        rotatorAnglesPanel.setVisible(false);

        this.add(jPanel1, BorderLayout.NORTH);

        jPanel1.add(switchingModeLabel, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(switchingMode, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(frequencyPanel, new GridBagConstraints(
                1, 0, 1, 3, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(noiseLabel, new GridBagConstraints(
                0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(noiseTextBox, new GridBagConstraints(
                1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(noiseUnitLabel, new GridBagConstraints(
                2, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        jPanel1.add(arrayCentredPanel, new GridBagConstraints(
                2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(separateOffsPanel, new GridBagConstraints(
                2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        jPanel1.add(rotatorAnglesPanel, new GridBagConstraints(
                0, 4, 3, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        frequencyPanel.add(jLabel8, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        frequencyPanel.add(frequencyOffset_throw, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(1, 1, 1, 1), 0, 0));
        frequencyPanel.add(rateLabel, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        frequencyPanel.add(frequencyOffset_rate, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(1, 1, 1, 1), 0, 0));
        frequencyPanel.add(jLabel10, new GridBagConstraints(
                2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        frequencyPanel.add(hertzLabel, new GridBagConstraints(
                2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
    }

    public void switchRateVisible(boolean visible) {
        rateLabel.setVisible(visible);
        frequencyOffset_rate.setVisible(visible);
        hertzLabel.setVisible(visible);
    }

    public interface RotatorAngleListener {
        void rotatorAnglesChanged(double[] angles);
    }

    public void addRotatorAngleListener(RotatorAngleListener listener) {
        rotatorAngleListeners.add(listener);
    }

    public void setAvailableRotatorAngles(double[] angles) {
        rotatorAnglesPanel.removeAll();

        if (angles == null) {
            rotatorAnglesPanel.setVisible(false);
            return;
        }

        for (int i = 0; i < angles.length; i ++) {
            JCheckBox checkbox = new JCheckBox(Double.toString(angles[i]));
            checkbox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    double[] selected = getSelectedRotatorAngles();
                    for (RotatorAngleListener listener:
                            rotatorAngleListeners) {
                        listener.rotatorAnglesChanged(selected);
                    }
                }
            });
            rotatorAnglesPanel.add(checkbox);
        }

        rotatorAnglesPanel.setVisible(true);
    }

    public void setSelectedRotatorAngles(double[] angles) {
        Component[] checkboxes = rotatorAnglesPanel.getComponents();
        for (Component checkbox_: checkboxes) {
            JCheckBox checkbox = (JCheckBox) checkbox_;

            double checkbox_angle = Double.parseDouble(checkbox.getText());

            boolean found = false;
            for (double angle: angles) {
                if (Math.abs(angle - checkbox_angle) < 0.001) {
                    found = true;
                    break;
                }
            }

            checkbox.setSelected(found);
        }
    }

    public double[] getSelectedRotatorAngles() {
        // Construct ArrayList of selected angles.
        ArrayList<Double> selected = new ArrayList<Double>();

        Component[] checkboxes = rotatorAnglesPanel.getComponents();
        for (Component checkbox_: checkboxes) {
            JCheckBox checkbox = (JCheckBox) checkbox_;

            if (checkbox.isSelected()) {
                double angle = Double.parseDouble(checkbox.getText());
                selected.add(angle);
            }
        }

        // Convert ArrayList to plain double array and return.
        double[] array = new double[selected.size()];

        int i = 0;
        for (Double angle: selected) {
            array[i ++] = angle;
        }

        return array;
    }
}
