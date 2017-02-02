/*
 * Copyright 2003 United Kingdom Astronomy Technology Centre, an
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

package ot.editor;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;

import jsky.app.ot.editor.TelescopeGUI;

/**
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SurveyGUI extends JPanel {
    private TelescopeGUI _telescopeGUI = null;
    JComboBox remaining = new JComboBox();
    JComboBox priority = new JComboBox();
    JButton addButton = new JButton("Add");
    JButton duplicateButton = new JButton("Duplicate");
    JButton removeButton = new JButton("Remove");
    JButton removeAllButton = new JButton("Remove all");
    JButton loadButton = new JButton("Load");
    JTabbedPane tabbedPane = new JTabbedPane();
    JCheckBox chooseButton = new JCheckBox("Select");
    JLabel selectLabel = new JLabel("from 1");
    JTextField selectField = new JTextField();
    JLabel titleLabel = new JLabel("Title:");
    JTextField titleField = new JTextField();
    private boolean visible = false;

    /**
     * List of fiels.
     *
     * Each field is represented by a SpTelescopeObsComp item.
     */
    JTable fieldTable = new JTable();

    public SurveyGUI(TelescopeGUI telescopeGUI) {
        _telescopeGUI = telescopeGUI;

        priority.setFont(new java.awt.Font("Dialog", 0, 12));
        remaining.setFont(new java.awt.Font("Dialog", 0, 12));
        addButton.setFont(new java.awt.Font("Dialog", 0, 12));
        duplicateButton.setFont(new java.awt.Font("Dialog", 0, 12));
        removeButton.setFont(new java.awt.Font("Dialog", 0, 12));
        removeAllButton.setFont(new java.awt.Font("Dialog", 0, 12));
        loadButton.setFont(new java.awt.Font("Dialog", 0, 12));
        chooseButton.setFont(new java.awt.Font("Dialog", 0, 12));
        selectLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        tabbedPane.setFont(new java.awt.Font("Dialog", 0, 12));
        titleLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        selectField.setColumns(4);

        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(duplicateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(removeAllButton);
        buttonPanel.add(loadButton);

        JPanel panel = new JPanel();

        JLabel label = new JLabel("Remaining");
        label.setFont(new java.awt.Font("Dialog", 0, 12));
        label.setForeground(Color.black);
        panel.add(label);
        panel.add(remaining);

        label = new JLabel("    Priority");
        label.setFont(new java.awt.Font("Dialog", 0, 12));
        label.setForeground(Color.black);
        panel.add(label);
        panel.add(priority);

        JLabel strut = new JLabel("     ");
        strut.setFont(new java.awt.Font("Dialog", 0, 12));
        panel.add(strut);
        panel.add(chooseButton);
        panel.add(selectField);
        panel.add(selectLabel);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(panel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel surveyPanel = new JPanel(new BorderLayout());
        surveyPanel.add(new JScrollPane(fieldTable), BorderLayout.CENTER);
        surveyPanel.add(southPanel, BorderLayout.SOUTH);
        surveyPanel.setBorder(new EtchedBorder());
        surveyPanel.setPreferredSize(new Dimension(100, 100));

        tabbedPane.add("Survey Targets", surveyPanel);
        // Telescope GUI is being hidden the first time, so it is safe not
        // to specifically hide the XYOffsetPanel.
        telescopeGUIVisible(false);
        tabbedPane.add("Target Information", _telescopeGUI);

        add(tabbedPane, BorderLayout.CENTER);

        JPanel northPanel = new JPanel();
        ((FlowLayout) northPanel.getLayout()).setAlignment(FlowLayout.LEFT);
        titleField.setColumns(15);
        northPanel.add(titleLabel, BorderLayout.NORTH);
        northPanel.add(titleField, BorderLayout.NORTH);
        add(northPanel, BorderLayout.NORTH);
    }

    public TelescopeGUI getTelescopeGUI() {
        return _telescopeGUI;
    }

    public void telescopeGUIVisible(boolean hideXYOffsetPanel) {
        Component[] components = _telescopeGUI.getComponents();

        for (Component component : components) {
            component.setVisible(
                visible && ! (hideXYOffsetPanel
                              && (component == _telescopeGUI.XYOffsetPanel)));
        }

        visible = !visible;
    }
}
