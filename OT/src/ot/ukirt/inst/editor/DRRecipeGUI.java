
/**
 * Title:        Ukirt GUIs<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */
package ot.ukirt.inst.editor;

import java.awt.*;
import javax.swing.*;
import jsky.app.ot.gui.*;
import java.awt.event.*;

public class DRRecipeGUI extends JPanel {
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  CommandButtonWidgetExt setBias = new CommandButtonWidgetExt();
  CommandButtonWidgetExt setDark = new CommandButtonWidgetExt();
  CommandButtonWidgetExt setSky = new CommandButtonWidgetExt();
  CommandButtonWidgetExt setObject = new CommandButtonWidgetExt();
  TextBoxWidgetExt biasRecipe = new TextBoxWidgetExt();
  TextBoxWidgetExt darkRecipe = new TextBoxWidgetExt();
  TextBoxWidgetExt skyRecipe = new TextBoxWidgetExt();
  TextBoxWidgetExt objectRecipe = new TextBoxWidgetExt();
  CheckBoxWidgetExt includeBias = new CheckBoxWidgetExt();
  CheckBoxWidgetExt includeDark = new CheckBoxWidgetExt();
  CheckBoxWidgetExt includeSky = new CheckBoxWidgetExt();
  CheckBoxWidgetExt includeObject = new CheckBoxWidgetExt();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JPanel jPanel2 = new JPanel();
  CommandButtonWidgetExt userSpecified = new CommandButtonWidgetExt();
  CommandButtonWidgetExt resetDefaults = new CommandButtonWidgetExt();
  TextBoxWidgetExt textBoxWidgetExt1 = new TextBoxWidgetExt();
  FlowLayout flowLayout1 = new FlowLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  TableWidgetExt tableWidgetExt1 = new TableWidgetExt();

  public DRRecipeGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    jPanel1.setLayout(gridBagLayout1);
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel1.setForeground(Color.black);
    jLabel1.setText("IRCAM3 DR Recipe");
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setForeground(Color.black);
    jLabel2.setText("Observation Type");
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setForeground(Color.black);
    jLabel3.setText("BIAS");
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel4.setForeground(Color.black);
    jLabel4.setText("DARK");
    jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel5.setForeground(Color.black);
    jLabel5.setText("SKY");
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel6.setForeground(Color.black);
    jLabel6.setText("OBJECT");
    setBias.setText("Set");
    setDark.setText("Set");
    setSky.setText("Set");
    setObject.setText("Set");
    biasRecipe.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        biasRecipe_actionPerformed(e);
      }
    });
    objectRecipe.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        objectRecipe_actionPerformed(e);
      }
    });
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel7.setForeground(Color.black);
    jLabel7.setText("Include in Group?");
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel8.setForeground(Color.black);
    jLabel8.setText("Recipe Name");
    userSpecified.setText("User Specified:");
    resetDefaults.setText("Reset Defaults");
    textBoxWidgetExt1.setEditable(false);
    textBoxWidgetExt1.setText("ENTER_YOUR_OWN_RECIPE_HERE");
    jPanel2.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    jPanel2.setMinimumSize(new Dimension(260, 60));
    jPanel2.setPreferredSize(new Dimension(471, 60));
    jScrollPane1.setPreferredSize(new Dimension(453, 200));
    this.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(jLabel6, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 15, 0));
    jPanel1.add(setBias, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 17), 0, 0));
    jPanel1.add(biasRecipe, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 52, 0));
    jPanel1.add(darkRecipe, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 42, 0));
    jPanel1.add(skyRecipe, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(objectRecipe, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(jLabel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(jLabel5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(setDark, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(setSky, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(setObject, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(jLabel2, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(includeBias, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(includeDark, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(includeSky, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(includeObject, new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(jLabel7, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(jLabel8, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, -2, 0, 27), 0, 0));
    this.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(resetDefaults, null);
    jPanel2.add(userSpecified, null);
    jPanel2.add(textBoxWidgetExt1, null);
    this.add(jScrollPane1, BorderLayout.SOUTH);
    jScrollPane1.add(tableWidgetExt1, null);
  }

  void biasRecipe_actionPerformed(ActionEvent e) {

  }

  void objectRecipe_actionPerformed(ActionEvent e) {

  }
}  
