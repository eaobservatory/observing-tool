/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot;

import javax.swing.*;
import java.awt.*;
import jsky.app.ot.gui.*;


public class OtPreferencesGUI extends JPanel {
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel generalPanel = new JPanel();
  OptionWidgetExt closeNoSaveOption = new OptionWidgetExt();
  JPanel backgroundPanel = new JPanel();
  JLabel jLabel1 = new JLabel();
  OptionWidgetExt closePromptOption = new OptionWidgetExt();
  BorderLayout borderLayout1 = new BorderLayout();
  CommandButtonWidgetExt okButton = new CommandButtonWidgetExt();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JPanel buttonPanel = new JPanel();
  CommandButtonWidgetExt applyButton = new CommandButtonWidgetExt();
  CommandButtonWidgetExt cancelButton = new CommandButtonWidgetExt();

  public OtPreferencesGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
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
    this.add(jTabbedPane1, BorderLayout.CENTER);
    jTabbedPane1.add(generalPanel, "General");
    generalPanel.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    generalPanel.add(closePromptOption, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    generalPanel.add(closeNoSaveOption, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jTabbedPane1.add(backgroundPanel, "Background");
    this.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(okButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 65, 0));
    buttonPanel.add(applyButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 53, 0));
    buttonPanel.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 43, 0));
  
    jTabbedPane1.setEnabledAt(1, false);
  }
}
