
/**
 * Title:        Ukirt GUIs<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */
package ot.jcmt.inst.editor;

import java.awt.*;
import javax.swing.*;
import jsky.app.ot.gui.*;
import java.awt.event.*;
import javax.swing.border.*;

public class DRRecipeGUI extends JPanel {
  JPanel scubaPanel = new JPanel();
  CommandButtonWidgetExt scuba_objectSet = new CommandButtonWidgetExt();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TextBoxWidgetExt scuba_objectRecipe = new TextBoxWidgetExt();
  JPanel jPanel1 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JLabel jLabel8 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JPanel heterodynePanel = new JPanel();
  CardLayout cardLayout1 = new CardLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel3 = new JPanel();
  TitledBorder titledBorder1;
  JLabel jLabel1 = new JLabel();
  JPanel jPanel4 = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  TextBoxWidgetExt scuba_userRecipe = new TextBoxWidgetExt();
  JPanel jPanel2 = new JPanel();
  CommandButtonWidgetExt scuba_userSpec = new CommandButtonWidgetExt();
  CommandButtonWidgetExt scuba_defaultName = new CommandButtonWidgetExt();
  JScrollPane jScrollPane1 = new JScrollPane();
  TableWidgetExt scuba_recipeTable = new TableWidgetExt();

  public DRRecipeGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    this.setLayout(cardLayout1);
    scubaPanel.setLayout(borderLayout3);
    scuba_objectSet.setText("Set");
    jPanel1.setLayout(gridBagLayout1);
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel8.setForeground(Color.black);
    jLabel8.setText("Recipe Name");
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel6.setForeground(Color.black);
    jLabel6.setText("OBJECT");
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setForeground(Color.black);
    jLabel2.setText("Observation Type");
    heterodynePanel.setLayout(borderLayout1);
    jPanel3.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"ORAC DR ACSIS"));
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setForeground(Color.black);
    jLabel1.setText("To be defined.");
    jPanel4.setLayout(borderLayout2);
    scuba_userRecipe.setEditable(false);
    scuba_userRecipe.setText("ENTER_YOUR_OWN_RECIPE_HERE");
    jPanel2.setLayout(gridBagLayout2);
    scuba_userSpec.setText("User Specified:");
    scuba_defaultName.setText("Default");
    this.add(scubaPanel, "scuba");
    scubaPanel.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(jLabel6, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 15, 0));
    jPanel1.add(scuba_objectRecipe, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    jPanel1.add(scuba_objectSet, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanel1.add(jLabel8, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanel1.add(jLabel2, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    scubaPanel.add(jPanel4, BorderLayout.CENTER);
    jPanel4.add(jPanel2, BorderLayout.NORTH);
    jPanel2.add(scuba_defaultName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanel2.add(scuba_userSpec, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanel2.add(scuba_userRecipe, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    jPanel4.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(scuba_recipeTable, null);
    this.add(heterodynePanel, "heterodyne");
    heterodynePanel.add(jPanel3, BorderLayout.NORTH);
    jPanel3.add(jLabel1, null);
  }
}
