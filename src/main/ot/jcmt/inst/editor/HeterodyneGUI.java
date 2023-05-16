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

package ot.jcmt.inst.editor;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;

import edfreq.BandwidthOption;
import edfreq.FrequencyEditorCfg;

/**
 * This GUI class is based on the GUI part of the edfreq.FrontEnd class which
 * is not used anymore.
 *
 * @author Dennis Kelly (bdk@roe.ac.uk),
 *         modified by Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class HeterodyneGUI extends JPanel {
    // Get the front end configuration
    private static FrequencyEditorCfg _cfg =
            FrequencyEditorCfg.getConfiguration();

    /** Available sideband modes. */
    public static final String[] SIDEBAND_MODES = {"ssb", "dsb", "2sb", "usb", "lsb"};

    /** Subsytems available. */
    public static final int[] SUBSYSTEMS = {1, 2, 3, 4};

    /** Sideband choices. */
    public static final String[] SIDEBAND_SELECTIONS = {"best", "usb", "lsb"};

    /** Table column headings. */
    final String[] COLUMN_NAMES = {
            "Region",
            "Species",
            "Trans.",
            "Rest. Freq.",
            "Centre Freq.",
            "BW",
            "res",
            "overlap",
            "channels",
    };

    // Front end configuration panel.
    Map<String, AbstractButton> feButtons;
    Map<String, AbstractButton> modeButtons;
    Map<Integer, AbstractButton> regionButtons;
    Map<String, AbstractButton> sbButtons;
    JComboBox specialConfigs;

    // Front end summary panel.
    JLabel lowFreq;
    JLabel highFreq;

    // Bandwidths panel.
    List<JComboBox<BandwidthOption>> bandwidths;

    // Radial default panel.
    CheckBoxWidgetExt defaultToRadial;
    JLabel radialDefaultLabel;

    // Velocity (or redshift) panel.
    JLabel velLabel;
    TextBoxWidgetExt velocity;
    JLabel vdLabel;
    DropDownListBoxWidgetExt vDef;
    JLabel vfLabel;
    DropDownListBoxWidgetExt vFrame;

    // Molecule or rest frequency panel.
    JComboBox moleculeBox;
    JComboBox transitionBox;
    JTextField freqText;
    JButton acceptButton;

    // Sky frequency and show/hide editor panel.
    JTextField skyFreqText;
    JButton showButton;
    JButton hideButton;

    // Frequency configuration table.
    JTable table;

    public HeterodyneGUI() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setLayout(new GridBagLayout());

        // Front-end panel and its elements

        JPanel fePanel = new JPanel();
        fePanel.setLayout(new GridBagLayout());
        fePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                "Front End Configuration"));

        JLabel feLabel = new JLabel("Front End:");
        feLabel.setForeground(Color.BLACK);
        feLabel.setFont(new Font("dialog", 0, 12));

        JPanel feSelector = new JPanel();
        feSelector.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder()));
        feSelector.setLayout(new GridLayout(0, 4, 5, 5));

        feButtons = makeFEGroup();

        for (AbstractButton button: feButtons.values()) {
            feSelector.add(button);
        }

        JLabel modeLabel = new JLabel("Mode:");
        modeLabel.setForeground(Color.BLACK);
        modeLabel.setFont(new Font("dialog", 0, 12));

        JPanel modeSelector = new JPanel();
        modeSelector.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder()));
        modeSelector.setLayout(new GridLayout(1, 0));

        modeButtons = makeSidebandConfigGroup();

        for (AbstractButton button: modeButtons.values()) {
            modeSelector.add(button);
        }

        JLabel subsystemLabel = new JLabel("Sp. Regions:");
        subsystemLabel.setForeground(Color.BLACK);
        subsystemLabel.setFont(new Font("dialog", 0, 12));

        JPanel regionSelector = new JPanel();
        regionSelector.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder()));
        regionSelector.setLayout(new GridLayout(1, 0));

        regionButtons = makeSubsystemGroup();

        for (AbstractButton button: regionButtons.values()) {
            regionSelector.add(button);
        }

        JLabel sbLabel = new JLabel("Sideband:");
        sbLabel.setForeground(Color.BLACK);
        sbLabel.setFont(new Font("dialog", 0, 12));

        JPanel sbSelector = new JPanel();
        sbSelector.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder()));
        sbSelector.setLayout(new GridLayout(1, 0));

        sbButtons = makeSBSelectionGroup();

        for (AbstractButton button: sbButtons.values()) {
            sbSelector.add(button);
        }

        JLabel specialConfigLabel = new JLabel("Special Configs:");
        specialConfigLabel.setForeground(Color.BLACK);
        specialConfigLabel.setFont(new Font("dialog", 0, 12));

        specialConfigs = new JComboBox();
        specialConfigs.setForeground(Color.BLACK);
        specialConfigs.setFont(new Font("dialog", 0, 12));

        fePanel.add(feLabel, new GridBagConstraints(
                0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 0, 5, 0), 0, 0));
        fePanel.add(feSelector, new GridBagConstraints(
                1, 0, 5, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 0, 5, 0), 0, 0));
        fePanel.add(subsystemLabel, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 5, 0), 0, 0));
        fePanel.add(regionSelector, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 5, 0), 0, 0));
        fePanel.add(specialConfigLabel, new GridBagConstraints(
                3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 20, 5, 0), 0, 0));
        fePanel.add(specialConfigs, new GridBagConstraints(
                4, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 5, 5, 5), 0, 0));
        fePanel.add(modeLabel, new GridBagConstraints(
                0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 5, 0), 0, 0));
        fePanel.add(modeSelector, new GridBagConstraints(
                1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 5, 0), 0, 0));
        fePanel.add(sbLabel, new GridBagConstraints(
                0, 3, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 5, 0), 0, 0));
        fePanel.add(sbSelector, new GridBagConstraints(
                1, 3, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 5, 0), 0, 0));

        // Front End summary panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(0, 2));
        summaryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                "Front End Summary"));

        JLabel lowLimitLabel = new JLabel("Low limit (GHz):");
        lowLimitLabel.setForeground(Color.BLACK);
        lowLimitLabel.setFont(new Font("dialog", 0, 12));

        lowFreq = new JLabel("215");
        lowFreq.setFont(new Font("Dialog", 0, 12));
        lowFreq.setForeground(Color.black);
        lowFreq.setBorder(new BevelBorder(BevelBorder.LOWERED));
        lowFreq.setMinimumSize(lowFreq.getPreferredSize());
        lowFreq.setName("LowFreqLimit");

        JLabel hiLimitLabel = new JLabel("High limit (GHz):");
        hiLimitLabel.setForeground(Color.BLACK);
        hiLimitLabel.setFont(new Font("dialog", 0, 12));

        highFreq = new JLabel("272");
        highFreq.setFont(new Font("Dialog", 0, 12));
        highFreq.setForeground(Color.black);
        highFreq.setBorder(new BevelBorder(BevelBorder.LOWERED));
        highFreq.setMinimumSize(highFreq.getPreferredSize());
        highFreq.setName("HighFreqLimit");

        JLabel resLabel = new JLabel("Resolution (kHz):");
        resLabel.setForeground(Color.BLACK);
        resLabel.setFont(new Font("dialog", 0, 12));

        summaryPanel.add(lowLimitLabel);
        summaryPanel.add(lowFreq);
        summaryPanel.add(hiLimitLabel);
        summaryPanel.add(highFreq);

        // Frequency Panel
        // To maintain the layout, we put the volocity and frequency and
        // buttons into separate, hidden, panels
        JPanel freqPanel = new JPanel();
        freqPanel.setLayout(new GridLayout(4, 1));
        freqPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                "Frequency Setup"));

        JPanel radialDefaultPanel = new JPanel(new GridBagLayout());
        defaultToRadial = new CheckBoxWidgetExt(
                "Default tuning velocity to target radial velocity");
        radialDefaultPanel.add(defaultToRadial);

        JPanel vPanel = new JPanel(new GridBagLayout());
        JPanel fPanel = new JPanel(new GridBagLayout());
        JPanel bPanel = new JPanel(new GridBagLayout());

        velLabel = new JLabel("Velocity");
        velLabel.setForeground(Color.BLACK);
        velLabel.setFont(new Font("dialog", 0, 12));
        velLabel.setEnabled(false);

        velocity = new TextBoxWidgetExt(10);
        velocity.setText("unset");
        velocity.setBorder(BorderFactory.createLoweredBevelBorder());
        velocity.setFont(new Font("Dialog", 0, 12));
        velocity.setForeground(Color.black);
        velocity.setMinimumSize(velocity.getPreferredSize());
        velocity.setName("velocity");
        velocity.setEnabled(false);

        vfLabel = new JLabel("Frame");
        vfLabel.setForeground(Color.BLACK);
        vfLabel.setFont(new Font("dialog", 0, 12));
        vfLabel.setEnabled(false);

        vFrame = new DropDownListBoxWidgetExt();
        vFrame.setBorder(BorderFactory.createEmptyBorder());
        vFrame.setFont(new Font("Dialog", 0, 12));
        vFrame.setForeground(Color.black);
        vFrame.setMinimumSize(vFrame.getPreferredSize());
        vFrame.setName("frame");
        vFrame.setEnabled(false);

        vdLabel = new JLabel("Definition");
        vdLabel.setForeground(Color.BLACK);
        vdLabel.setFont(new Font("dialog", 0, 12));
        vdLabel.setEnabled(false);

        vDef = new DropDownListBoxWidgetExt();
        vDef.setBorder(BorderFactory.createEmptyBorder());
        vDef.setFont(new Font("Dialog", 0, 12));
        vDef.setForeground(Color.black);
        vDef.setMinimumSize(vDef.getPreferredSize());
        vDef.setName("definition");
        vDef.setEnabled(false);

        moleculeBox = new JComboBox();
        moleculeBox.setForeground(Color.BLACK);
        moleculeBox.setFont(new Font("dialog", 0, 12));
        moleculeBox.setName("molecule");

        transitionBox = new JComboBox();
        transitionBox.setForeground(Color.BLACK);
        transitionBox.setFont(new Font("dialog", 0, 12));
        transitionBox.setName("transition");

        acceptButton = new JButton("Accept");
        acceptButton.setForeground(Color.RED);
        acceptButton.setFont(new Font("dialog", 0, 12));
        acceptButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        acceptButton.setName("Accept");

        showButton = new JButton("Show Frequency Editor");
        showButton.setForeground(Color.BLACK);
        showButton.setFont(new Font("dialog", 0, 12));
        showButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        showButton.setName("show");

        hideButton = new JButton("Hide Frequency Editor");
        hideButton.setForeground(Color.BLACK);
        hideButton.setFont(new Font("dialog", 0, 12));
        hideButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        hideButton.setName("hide");

        freqText = new JTextField();
        freqText.setForeground(Color.BLACK);
        freqText.setFont(new Font("dialog", 0, 12));
        freqText.setName("frequency");

        JLabel unitsLabel = new JLabel("GHz");
        unitsLabel.setForeground(Color.BLACK);
        unitsLabel.setFont(new Font("dialog", 0, 12));

        vPanel.add(velLabel, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 5, 5, 5), 0, 0));
        vPanel.add(velocity, new GridBagConstraints(
                1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 0, 5, 5), 0, 0));
        vPanel.add(vdLabel, new GridBagConstraints(
                2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 0, 5, 5), 0, 0));
        vPanel.add(vDef, new GridBagConstraints(
                3, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 0, 5, 5), 0, 0));
        vPanel.add(vfLabel, new GridBagConstraints(
                4, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 0, 5, 5), 0, 0));
        vPanel.add(vFrame, new GridBagConstraints(
                5, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 0, 5, 5), 0, 0));
        fPanel.add(moleculeBox, new GridBagConstraints(
                0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        fPanel.add(transitionBox, new GridBagConstraints(
                1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 0, 5, 5), 0, 0));
        fPanel.add(freqText, new GridBagConstraints(
                2, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 0, 5, 5), 0, 0));
        fPanel.add(unitsLabel, new GridBagConstraints(
                3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5, 0, 5, 5), 0, 0));
        fPanel.add(acceptButton, new GridBagConstraints(
                4, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 0, 5, 5), 0, 0));

        bPanel.add(new JLabel("Sky freq."), new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 0), 0, 0));
        skyFreqText = new JTextField(10);
        skyFreqText.setEnabled(false);
        bPanel.add(skyFreqText, new GridBagConstraints(
                1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        bPanel.add(new JLabel("GHz"), new GridBagConstraints(
                2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(5, 0, 5, 5), 0, 0));
        bPanel.add(showButton, new GridBagConstraints(
                3, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        bPanel.add(hideButton, new GridBagConstraints(
                4, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));

        freqPanel.add(radialDefaultPanel);
        freqPanel.add(vPanel);
        freqPanel.add(fPanel);
        freqPanel.add(bPanel);

        // The frequency summary information
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                "Frequency Configuration"));

        table = new JTable(new DefaultTableModel(COLUMN_NAMES, 4));
        table.setEnabled(false);
        table.setBackground(getBackground());
        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bandwidthsPanel = new JPanel();

        bandwidthsPanel.setLayout(new GridLayout(4, 1));
        bandwidthsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                "Bandwidths"));

        bandwidths = new ArrayList<JComboBox<BandwidthOption>>();

        for (int i = 0; i < 4; i ++) {
            JComboBox<BandwidthOption> bandwidth = new JComboBox<BandwidthOption>();
            bandwidth.setForeground(Color.BLACK);
            bandwidth.setFont(new Font("dialog", 0, 12));
            bandwidth.setEnabled(i == 0);

            bandwidthsPanel.add(bandwidth);
            bandwidths.add(bandwidth);
        }

        JPanel summaryAndBandwidthPanel = new JPanel();
        summaryAndBandwidthPanel.setLayout(new GridBagLayout());
        summaryAndBandwidthPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED), ""));

        summaryAndBandwidthPanel.add(summaryPanel, new GridBagConstraints(
                0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
                new Insets(0, 0, 5, 5), 0, 0));
        summaryAndBandwidthPanel.add(bandwidthsPanel, new GridBagConstraints(
                0, 1, 1, 2, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 5, 5), 0, 0));

        // Add all the panels to this object
        add(fePanel, new GridBagConstraints(
                0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(0, 0, 5, 0), 0, 0));
        add(summaryAndBandwidthPanel, new GridBagConstraints(
                1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
                new Insets(0, 0, 5, 5), 0, 0));
        add(freqPanel, new GridBagConstraints(
                0, 1, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 5, 5), 0, 0));
        add(tablePanel, new GridBagConstraints(
                0, 2, 2, 2, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));
    }

    private Map<String, AbstractButton> makeFEGroup() {
        Map<String, AbstractButton> buttons
            = new LinkedHashMap<String, AbstractButton>();

        ButtonGroup bg = new ButtonGroup();

        for (int i = 0; i < _cfg.frontEnds.length; i++) {
            String name = _cfg.frontEnds[i].trim();
            JRadioButton b = new JRadioButton(name);
            b.setForeground(Color.BLACK);
            b.setFont(new Font("Dialog", 0, 10));
            b.setName(name);
            bg.add(b);
            buttons.put(name, b);
        }

        return buttons;
    }

    private Map<String, AbstractButton> makeSidebandConfigGroup() {
        Map<String, AbstractButton> buttons
            = new LinkedHashMap<String, AbstractButton>();

        ButtonGroup bg = new ButtonGroup();

        for (int i = 0; i < SIDEBAND_MODES.length; i++) {
            String name = SIDEBAND_MODES[i];
            JRadioButton b = new JRadioButton(name);
            b.setForeground(Color.BLACK);
            b.setFont(new Font("Dialog", 0, 10));
            b.setName(name);
            bg.add(b);
            buttons.put(name, b);
        }

        return buttons;
    }

    private Map<Integer, AbstractButton> makeSubsystemGroup() {
        Map<Integer, AbstractButton> buttons
            = new LinkedHashMap<Integer, AbstractButton>();

        ButtonGroup bg = new ButtonGroup();

        for (int n: SUBSYSTEMS) {
            String name = Integer.toString(n);
            JRadioButton b = new JRadioButton(name);
            b.setForeground(Color.BLACK);
            b.setFont(new Font("Dialog", 0, 10));
            b.setName(name);
            bg.add(b);
            buttons.put(n, b);
        }

        return buttons;
    }

    private Map<String, AbstractButton> makeSBSelectionGroup() {
        Map<String, AbstractButton> buttons
            = new LinkedHashMap<String, AbstractButton>();

        ButtonGroup bg = new ButtonGroup();

        for (String name: SIDEBAND_SELECTIONS) {
            JRadioButton b = new JRadioButton(name);
            b.setForeground(Color.BLACK);
            b.setFont(new Font("Dialog", 0, 10));
            b.setName(name);
            bg.add(b);
            buttons.put(name, b);
        }

        return buttons;
    }
}
