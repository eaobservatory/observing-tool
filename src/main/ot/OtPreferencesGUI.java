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

package ot;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Color;
import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetExt;

@SuppressWarnings("serial")
public class OtPreferencesGUI extends JPanel {
    JTabbedPane jTabbedPane1 = new JTabbedPane();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JPanel generalPanel = new JPanel();
    JLabel jLabel1 = new JLabel();
    OptionWidgetExt closePromptOption = new OptionWidgetExt();
    OptionWidgetExt closeNoSaveOption = new OptionWidgetExt();
    JPanel backgroundPanel = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    CommandButtonWidgetExt okButton = new CommandButtonWidgetExt();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JPanel buttonPanel = new JPanel();
    CommandButtonWidgetExt applyButton = new CommandButtonWidgetExt();
    CommandButtonWidgetExt cancelButton = new CommandButtonWidgetExt();

    // All of the following is for setting up procy hosts....
    JPanel proxyPanel = new JPanel();
    JTextArea proxyTextArea1 = new JTextArea();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    JLabel proxyLabel1 = new JLabel();
    JTextField proxyServerField = new JTextField();
    JLabel proxyLabel2 = new JLabel();
    JTextField proxyPortField = new JTextField();
    JTextArea proxyTextArea2 = new JTextArea();
    JLabel proxyLabel3 = new JLabel();
    JTextField nonProxyHostsField = new JTextField();

    // End of proxy stuff

    public OtPreferencesGUI() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        jTabbedPane1.setToolTipText("");
        generalPanel.setLayout(gridBagLayout1);
        closeNoSaveOption.setText("Don\'t prompt, don\'t save changes.");
        closeNoSaveOption.setFont(new java.awt.Font("Dialog", 0, 12));
        backgroundPanel.setEnabled(false);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("When closing edited programs:");
        closePromptOption.setText("Prompt me to save changes.");
        closePromptOption.setFont(new java.awt.Font("Dialog", 0, 12));
        this.setLayout(borderLayout1);
        okButton.setText("OK");
        buttonPanel.setLayout(gridBagLayout2);
        applyButton.setText("Apply");
        cancelButton.setText("Cancel");

        // Initialise proxy stuff
        proxyPanel.setLayout(gridBagLayout3);
        proxyTextArea1.setBackground(new Color(204, 204, 204));
        proxyTextArea1.setEditable(false);
        proxyTextArea1.setText(
                "If your host is behind a firewall, you may need to use a"
                + " proxy server to access remote catalogs via HTTP."
                + " Please enter the hostname and port number for the"
                + " proxy server:");
        proxyTextArea1.setLineWrap(true);
        proxyTextArea1.setWrapStyleWord(true);
        proxyLabel1.setLabelFor(proxyServerField);
        proxyLabel1.setText("HTTP Proxy Server:");
        proxyLabel2.setLabelFor(proxyPortField);
        proxyLabel2.setText("Port:");
        proxyTextArea2.setBackground(new Color(204, 204, 204));
        proxyTextArea2.setEditable(false);
        proxyTextArea2.setText(
                "The value below can be a list of hosts, each separated by a"
                + " |. In addition, a wildcard character (*) can be used for"
                + " matching. For example: *.foo.com|localhost :");
        proxyTextArea2.setLineWrap(true);
        proxyTextArea2.setWrapStyleWord(true);
        proxyLabel3.setLabelFor(nonProxyHostsField);
        proxyLabel3.setText("No Proxy for:");
        // end of proxy stuff
        this.add(jTabbedPane1, BorderLayout.CENTER);
        jTabbedPane1.add(generalPanel, "General");
        generalPanel.add(jLabel1, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        generalPanel.add(closePromptOption, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        generalPanel.add(closeNoSaveOption, new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jTabbedPane1.add(proxyPanel, "Proxy Server");
        proxyPanel.add(proxyTextArea1, new GridBagConstraints(
                0, 0, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(11, 11, 0, 11), 0, 0));
        proxyPanel.add(proxyLabel1, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(11, 11, 0, 0), 0, 0));
        proxyPanel.add(proxyServerField, new GridBagConstraints(
                1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(11, 11, 0, 0), 0, 0));
        proxyPanel.add(proxyLabel2, new GridBagConstraints(
                2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(11, 11, 0, 0), 0, 0));
        proxyPanel.add(proxyPortField, new GridBagConstraints(
                3, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(11, 11, 0, 11), 0, 0));
        proxyPanel.add(proxyTextArea2, new GridBagConstraints(
                0, 2, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(11, 11, 0, 11), 0, 0));
        proxyPanel.add(proxyLabel3, new GridBagConstraints(
                0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(11, 11, 0, 0), 0, 0));
        proxyPanel.add(nonProxyHostsField, new GridBagConstraints(
                1, 3, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(11, 11, 0, 11), 0, 0));
        jTabbedPane1.add(backgroundPanel, "Background");
        this.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(okButton, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 65, 0));
        buttonPanel.add(applyButton, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 53, 0));
        buttonPanel.add(cancelButton, new GridBagConstraints(
                2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 43, 0));

        jTabbedPane1.setEnabledAt(2, false);
    }
}
