
// $Id$

/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton, modified by Martin Folger (M.Folger@roe.ac.uk)<p>
 * Company:      <p>
 * @author Allan Brighton, modified by Martin Folger (M.Folger@roe.ac.uk)
 * @version 1.0
 */
package ot;

import java.awt.*;
import jsky.app.ot.gui.*;
import javax.swing.*;
import ot.gui.PasswordWidgetExt; // MFO (24 July 2001)

public class DatabaseDialogGUI extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel loginPage = new JPanel();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    PasswordWidgetExt passwordTextBox = new PasswordWidgetExt(); // MFO (24 July 2001)
    JLabel jLabel2 = new JLabel();
    JLabel jLabel1 = new JLabel();
    TextBoxWidgetExt loginTextBox = new TextBoxWidgetExt();
    JPanel jPanel1 = new JPanel();
    CommandButtonWidgetExt cancelButton = new CommandButtonWidgetExt();
    CommandButtonWidgetExt confirmButton = new CommandButtonWidgetExt();

    public DatabaseDialogGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        loginPage.setLayout(gridBagLayout2);
        this.setMinimumSize(new Dimension(289, 164));
        this.setPreferredSize(new Dimension(289, 164));
        this.setLayout(borderLayout1);
        passwordTextBox.setBorder(BorderFactory.createLoweredBevelBorder());
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setForeground(Color.black);
        jLabel2.setText("Password:");
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Project ID:");
        loginTextBox.setBorder(BorderFactory.createLoweredBevelBorder());
        cancelButton.setText("Cancel");
        confirmButton.setText("Confirm");
        this.add(loginPage, BorderLayout.CENTER);
        loginPage.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
        loginPage.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        loginPage.add(loginTextBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));
        loginPage.add(passwordTextBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jPanel1, BorderLayout.SOUTH);
        jPanel1.add(confirmButton, null);
        jPanel1.add(cancelButton, null);
    }
}
