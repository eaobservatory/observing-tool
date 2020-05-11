/*
 * Copyright (c) 2000 Association of Universities for Research in Astronomy,
 * Inc. (AURA)
 *
 * @author Allan Brighton, modified by Martin Folger (M.Folger@roe.ac.uk)
 * @version 1.0
 *
 * License:
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ot;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Dimension;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import ot.gui.PasswordWidgetExt; // MFO (24 July 2001)

@SuppressWarnings("serial")
public class DatabaseDialogGUI extends JPanel {
    public String[] providers = {"staff"};
    DropDownListBoxWidgetExt providerBox = new DropDownListBoxWidgetExt();
    PasswordWidgetExt passwordTextBox = new PasswordWidgetExt();
    TextBoxWidgetExt projectTextBox = new TextBoxWidgetExt();
    TextBoxWidgetExt usernameTextBox = new TextBoxWidgetExt();
    TextBoxWidgetExt tokenUsernameTextBox = new TextBoxWidgetExt();
    CommandButtonWidgetExt closeButton = new CommandButtonWidgetExt("Close");
    CommandButtonWidgetExt confirmButton = new CommandButtonWidgetExt("Confirm");
    CommandButtonWidgetExt tokenConfirmButton = new CommandButtonWidgetExt("Confirm");
    CommandButtonWidgetExt hedwigButton = new CommandButtonWidgetExt("Log in with Hedwig");
    public JLabel validationReminder = new JLabel();
    JTabbedPane typeChoice = new JTabbedPane();

    public DatabaseDialogGUI() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        this.setLayout(new BorderLayout());
        JPanel loginPage = new JPanel();
        loginPage.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(500, 400));
        this.setPreferredSize(new Dimension(500, 400));

        providerBox.setChoices(new String[] {"EAO Staff"});

        usernameTextBox.setBorder(BorderFactory.createLoweredBevelBorder());
        passwordTextBox.setBorder(BorderFactory.createLoweredBevelBorder());
        projectTextBox.setBorder(BorderFactory.createLoweredBevelBorder());
        tokenUsernameTextBox.setBorder(BorderFactory.createLoweredBevelBorder());
        tokenUsernameTextBox.setEditable(false);

        JPanel loginPageAuth = new JPanel();
        loginPageAuth.setLayout(new GridBagLayout());

        JPanel loginPagePass = new JPanel();
        loginPagePass.setLayout(new GridBagLayout());

        JPanel loginPageToken = new JPanel();
        loginPageToken.setLayout(new GridBagLayout());

        typeChoice.add(loginPageAuth, "Browser log in");
        typeChoice.add(loginPagePass, "Password log in");
        typeChoice.add(loginPageToken, "Current session");

        this.add(loginPage, BorderLayout.CENTER);

        validationReminder.setText(
            "<html>Have you validated your Science Program?<br>" +
            "We recommend that you validate your program<br>" +
            "before storing it.  To do this, select the top<br>" +
            "level \"Science Program\" element in the left<br>" +
            "panel and click the \"Validation\" button.</html>"
        );

        loginPage.add(validationReminder,
                new GridBagConstraints(
                0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 10, 10), 0, 0));

        JLabel jLabel1 = new JLabel("Project ID:");
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        loginPage.add(jLabel1, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(10, 5, 5, 5), 0, 0));
        loginPage.add(projectTextBox, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 5, 5), 0, 0));

        loginPage.add(typeChoice, new GridBagConstraints(
                0, 2, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 5, 5, 5), 0, 0));

        JLabel jLabel3 = new JLabel("Provider:");
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setForeground(Color.black);
        loginPagePass.add(jLabel3, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        loginPagePass.add(providerBox, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 5, 5), 0, 0));

        JLabel jLabel4 = new JLabel("Username:");
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel4.setForeground(Color.black);
        loginPagePass.add(jLabel4, new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        loginPagePass.add(usernameTextBox, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 5, 5), 0, 0));

        JLabel jLabel2 = new JLabel("Password:");
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setForeground(Color.black);
        loginPagePass.add(jLabel2, new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        loginPagePass.add(passwordTextBox, new GridBagConstraints(
                1, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        JPanel jPanel1 = new JPanel();
        jPanel1.add(confirmButton, null);
        jPanel1.add(closeButton, null);
        loginPagePass.add(jPanel1, new GridBagConstraints(
                0, 3, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        loginPageAuth.add(hedwigButton, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 10, 10, 10), 0, 0));

        JLabel jLabel5 = new JLabel("OMP ID:");
        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setForeground(Color.black);
        loginPageToken.add(jLabel5, new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5), 0, 0));
        loginPageToken.add(tokenUsernameTextBox, new GridBagConstraints(
                1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 5, 5), 0, 0));

        loginPageToken.add(tokenConfirmButton, new GridBagConstraints(
                1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(10, 5, 5, 5), 0, 0));
    }
}
