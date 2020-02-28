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

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class IterRawXmlObsGUI extends IterJCMTGenericGUI {
    public JTextField timeField;
    public JTextArea ocsCfgArea;
    public JButton loadButton;

    public IterRawXmlObsGUI() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.add(new JLabel("Estimated duration (seconds)"));
        timeField = new JTextField(20);
        panel.add(timeField);
        add(panel, BorderLayout.NORTH);

        ocsCfgArea = new JTextArea();
        add(new JScrollPane(ocsCfgArea), BorderLayout.CENTER);

        panel = new JPanel();
        loadButton = new JButton("Load from file");
        panel.add(loadButton);
        add(panel, BorderLayout.SOUTH);
    }
}
