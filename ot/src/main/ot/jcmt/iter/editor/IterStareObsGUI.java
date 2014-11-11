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

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.border.Border;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;

@SuppressWarnings("serial")
public class IterStareObsGUI extends IterJCMTGenericGUI {
    JPanel acsisPanel = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel secsPerOffsetSampleLabel = new JLabel();
    TextBoxWidgetExt secsPerOffsetSample = new TextBoxWidgetExt();
    JCheckBox widePhotom = new JCheckBox();
    CheckBoxWidgetExt contModeCB = new CheckBoxWidgetExt();
    JLabel informationLabelTop = new JLabel();
    JLabel informationLabelMiddle = new JLabel();
    JLabel informationLabelBottom = new JLabel();
    JPanel informationPanel = new JPanel();
    JPanel scuba2Panel = new JPanel();
    JLabel integrationTimeLabel = new JLabel();
    JLabel integrationTimeSeconds = new JLabel();

    JPanel mapPanel = new JPanel();
    JLabel paLabel = new JLabel();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    TextBoxWidgetExt paTextBox = new TextBoxWidgetExt();
    JLabel unitLabel = new JLabel();
    JLabel systemLabel = new JLabel();
    DropDownListBoxWidgetExt coordSys = new DropDownListBoxWidgetExt();

    public IterStareObsGUI() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        JPanel starePanel = new JPanel();
        Border bevelBorder =
                BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        Border titleBorder =
                BorderFactory.createTitledBorder(bevelBorder, "Stare Setup");
        starePanel.setBorder(titleBorder);
        starePanel.setLayout(new BorderLayout());

        acsisPanel.setLayout(gridBagLayout1);
        secsPerOffsetSampleLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        secsPerOffsetSampleLabel.setForeground(Color.black);
        secsPerOffsetSampleLabel.setText("Secs per offset sample");

        secsPerCycle.setColumns(5);

        cycleReversal.setText("Cycle Reversal");
        cycleReversal.setFont(new java.awt.Font("Dialog", 0, 12));
        continuousCal.setText("Continuous Cal");
        continuousCal.setFont(new java.awt.Font("Dialog", 0, 12));

        contModeCB.setText("Continuum Mode");
        contModeCB.setFont(new java.awt.Font("Dialog", 0, 12));
        contModeCB.setForeground(Color.black);
        contModeCB.setHorizontalTextPosition(SwingConstants.LEFT);

        widePhotom.setText("Wide Photometry");
        widePhotom.setFont(new java.awt.Font("Dialog", 0, 12));
        widePhotom.setForeground(Color.black);

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

        this.add(starePanel, BorderLayout.CENTER);
        starePanel.add(widePhotom, BorderLayout.NORTH);

        starePanel.add(acsisPanel, BorderLayout.CENTER);
        acsisPanel.add(secsPerOffsetSampleLabel, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        acsisPanel.add(secsPerOffsetSample, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        // Add a coninuum mode checkbox
        acsisPanel.add(contModeCB, new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        this.add(informationPanel, BorderLayout.SOUTH);
        informationPanel.add(informationLabelTop);
        informationPanel.add(informationLabelMiddle);
        informationPanel.add(informationLabelBottom);

        scuba2Panel.setLayout(new GridLayout(1, 3));

        integrationTimeLabel.setText("Integration Time");
        integrationTimeLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        integrationTimeLabel.setForeground(Color.black);
        scuba2Panel.add(integrationTimeLabel);

        scuba2Panel.add(secsPerCycle);

        integrationTimeSeconds.setText("Seconds");
        integrationTimeSeconds.setFont(new java.awt.Font("Dialog", 0, 12));
        integrationTimeSeconds.setForeground(Color.black);
        scuba2Panel.add(integrationTimeSeconds);

        starePanel.add(scuba2Panel, BorderLayout.NORTH);

        // Map PA box
        mapPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(
                new Color(153, 153, 153), 2), "Map"));
        mapPanel.setLayout(gridBagLayout3);
        paLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        paLabel.setForeground(Color.black);
        paLabel.setText("PA");
        paTextBox.setColumns(5);
        unitLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        unitLabel.setForeground(Color.black);
        unitLabel.setText("(degrees)");
        systemLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        systemLabel.setForeground(Color.black);
        systemLabel.setText("System");

        // Add the PA info to the Map panel
        mapPanel.add(paLabel, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(0, 0, 5, 0), 0, 0));
        mapPanel.add(paTextBox, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 0), 0, 0));
        mapPanel.add(unitLabel, new GridBagConstraints(
                2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        // Add the jiggle pattern box
        mapPanel.add(systemLabel, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        mapPanel.add(coordSys, new GridBagConstraints(
                1, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 0, 0), 0, 0));

        starePanel.add(mapPanel, BorderLayout.SOUTH);
    }
}
