
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
  CardLayout cardLayout1 = new CardLayout();
  JPanel emptyPanel = new JPanel();
  CommandButtonWidgetExt cgs4_darkSet = new CommandButtonWidgetExt();
  CommandButtonWidgetExt cgs4_objectSet = new CommandButtonWidgetExt();
  TextBoxWidgetExt cgs4_skyRecipe = new TextBoxWidgetExt();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  TextBoxWidgetExt cgs4_objectRecipe = new TextBoxWidgetExt();
  JPanel cgs4Panel = new JPanel();
  CheckBoxWidgetExt cgs4_objectInGroup = new CheckBoxWidgetExt();
  CheckBoxWidgetExt cgs4_skyInGroup = new CheckBoxWidgetExt();
  CheckBoxWidgetExt cgs4_biasInGroup = new CheckBoxWidgetExt();
  CheckBoxWidgetExt cgs4_darkInGroup = new CheckBoxWidgetExt();
  JPanel jPanel4 = new JPanel();
  TextBoxWidgetExt cgs4_darkRecipe = new TextBoxWidgetExt();
  JLabel jLabel9 = new JLabel();
  BorderLayout borderLayout2 = new BorderLayout();
  JLabel jLabel10 = new JLabel();
  JLabel jLabel11 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JLabel jLabel13 = new JLabel();
  JLabel jLabel14 = new JLabel();
  JLabel jLabel15 = new JLabel();
  TextBoxWidgetExt cgs4_biasRecipe = new TextBoxWidgetExt();
  CommandButtonWidgetExt cgs4_skySet = new CommandButtonWidgetExt();
  CommandButtonWidgetExt ircam3_darkSet = new CommandButtonWidgetExt();
  CommandButtonWidgetExt ircam3_objectSet = new CommandButtonWidgetExt();
  TextBoxWidgetExt ircam3_skyRecipe = new TextBoxWidgetExt();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  TextBoxWidgetExt ircam3_objectRecipe = new TextBoxWidgetExt();
  JPanel ircam3Panel = new JPanel();
  CheckBoxWidgetExt ircam3_objectInGroup = new CheckBoxWidgetExt();
  CheckBoxWidgetExt ircam3_skyInGroup = new CheckBoxWidgetExt();
  CheckBoxWidgetExt ircam3_biasInGroup = new CheckBoxWidgetExt();
  CheckBoxWidgetExt ircam3_darkInGroup = new CheckBoxWidgetExt();
  JPanel jPanel6 = new JPanel();
  TextBoxWidgetExt ircam3_darkRecipe = new TextBoxWidgetExt();
  JLabel jLabel16 = new JLabel();
  BorderLayout borderLayout3 = new BorderLayout();
  JLabel jLabel17 = new JLabel();
  JLabel jLabel18 = new JLabel();
  JLabel jLabel19 = new JLabel();
  JLabel jLabel110 = new JLabel();
  JLabel jLabel111 = new JLabel();
  JLabel jLabel112 = new JLabel();
  TextBoxWidgetExt ircam3_biasRecipe = new TextBoxWidgetExt();
  CommandButtonWidgetExt ircam3_skySet = new CommandButtonWidgetExt();
  CommandButtonWidgetExt ircam3_biasSet = new CommandButtonWidgetExt();
  JLabel jLabel1 = new JLabel();
  CommandButtonWidgetExt cgs4_flatSet = new CommandButtonWidgetExt();
  TextBoxWidgetExt cgs4_flatRecipe = new TextBoxWidgetExt();
  CheckBoxWidgetExt cgs4_flatInGroup = new CheckBoxWidgetExt();
  JLabel jLabel2 = new JLabel();
  CommandButtonWidgetExt cgs4_arcSet = new CommandButtonWidgetExt();
  TextBoxWidgetExt cgs4_arcRecipe = new TextBoxWidgetExt();
  CheckBoxWidgetExt cgs4_arcInGroup = new CheckBoxWidgetExt();
  CommandButtonWidgetExt michelle_flatSet = new CommandButtonWidgetExt();
  TextBoxWidgetExt michelle_objectRecipe = new TextBoxWidgetExt();
  TextBoxWidgetExt michelle_skyRecipe = new TextBoxWidgetExt();
  TextBoxWidgetExt michelle_arcRecipe = new TextBoxWidgetExt();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  JPanel michellePanel = new JPanel();
  TextBoxWidgetExt michelle_flatRecipe = new TextBoxWidgetExt();
  CommandButtonWidgetExt michelle_skySet = new CommandButtonWidgetExt();
  JLabel jLabel113 = new JLabel();
  JLabel jLabel114 = new JLabel();
  JLabel jLabel115 = new JLabel();
  JLabel jLabel116 = new JLabel();
  JLabel jLabel117 = new JLabel();
  JLabel jLabel118 = new JLabel();
  CheckBoxWidgetExt michelle_darkInGroup = new CheckBoxWidgetExt();
  CheckBoxWidgetExt michelle_arcInGroup = new CheckBoxWidgetExt();
  CheckBoxWidgetExt michelle_flatInGroup = new CheckBoxWidgetExt();
  CheckBoxWidgetExt michelle_skyInGroup = new CheckBoxWidgetExt();
  TextBoxWidgetExt michelle_biasRecipe = new TextBoxWidgetExt();
  CheckBoxWidgetExt michelle_biasInGroup = new CheckBoxWidgetExt();
  CommandButtonWidgetExt michelle_darkSet = new CommandButtonWidgetExt();
  CommandButtonWidgetExt michelle_arcSet = new CommandButtonWidgetExt();
  JPanel jPanel7 = new JPanel();
  TextBoxWidgetExt michelle_darkRecipe = new TextBoxWidgetExt();
  JLabel jLabel21 = new JLabel();
  BorderLayout borderLayout4 = new BorderLayout();
  CheckBoxWidgetExt michelle_objectInGroup = new CheckBoxWidgetExt();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  CommandButtonWidgetExt michelle_objectSet = new CommandButtonWidgetExt();
  CommandButtonWidgetExt michelle_biasSet = new CommandButtonWidgetExt();
  TextBoxWidgetExt ufti_objectRecipe = new TextBoxWidgetExt();
  TextBoxWidgetExt ufti_skyRecipe = new TextBoxWidgetExt();
  GridBagLayout gridBagLayout5 = new GridBagLayout();
  CheckBoxWidgetExt ufti_darkInGroup = new CheckBoxWidgetExt();
  JPanel uftiPanel = new JPanel();
  CheckBoxWidgetExt ufti_skyInGroup = new CheckBoxWidgetExt();
  JLabel jLabel125 = new JLabel();
  JPanel jPanel9 = new JPanel();
  JLabel jLabel124 = new JLabel();
  JLabel jLabel123 = new JLabel();
  JLabel jLabel122 = new JLabel();
  JLabel jLabel120 = new JLabel();
  TextBoxWidgetExt ufti_darkRecipe = new TextBoxWidgetExt();
  CommandButtonWidgetExt ufti_darkSet = new CommandButtonWidgetExt();
  BorderLayout borderLayout5 = new BorderLayout();
  CheckBoxWidgetExt ufti_objectInGroup = new CheckBoxWidgetExt();
  CommandButtonWidgetExt ufti_objectSet = new CommandButtonWidgetExt();
  CommandButtonWidgetExt ufti_skySet = new CommandButtonWidgetExt();
  JLabel jLabel20 = new JLabel();
  CommandButtonWidgetExt cgs4_biasSet = new CommandButtonWidgetExt();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JPanel jPanel1 = new JPanel();
  CommandButtonWidgetExt cgs4_defaultName = new CommandButtonWidgetExt();
  CommandButtonWidgetExt cgs4_userSpec = new CommandButtonWidgetExt();
  JPanel jPanel3 = new JPanel();
  TextBoxWidgetExt cgs4_userRecipe = new TextBoxWidgetExt();
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  TableWidgetExt cgs4_recipeTable = new TableWidgetExt();
  JPanel jPanel2 = new JPanel();
  BorderLayout borderLayout6 = new BorderLayout();
  CommandButtonWidgetExt ircam3_userSpec = new CommandButtonWidgetExt();
  CommandButtonWidgetExt ircam3_defaultName = new CommandButtonWidgetExt();
  JPanel jPanel5 = new JPanel();
  TextBoxWidgetExt ircam3_userRecipe = new TextBoxWidgetExt();
  JScrollPane jScrollPane2 = new JScrollPane();
  TableWidgetExt ircam3_recipeTable = new TableWidgetExt();
  JPanel jPanel8 = new JPanel();
  BorderLayout borderLayout7 = new BorderLayout();
  CommandButtonWidgetExt michelle_defaultName = new CommandButtonWidgetExt();
  TextBoxWidgetExt michelle_userRecipe = new TextBoxWidgetExt();
  CommandButtonWidgetExt michelle_userSpec = new CommandButtonWidgetExt();
  JPanel jPanel11 = new JPanel();
  JScrollPane jScrollPane3 = new JScrollPane();
  TableWidgetExt michelle_recipeTable = new TableWidgetExt();
  JPanel jPanel10 = new JPanel();
  BorderLayout borderLayout8 = new BorderLayout();
  JPanel jPanel12 = new JPanel();
  CommandButtonWidgetExt ufti_defaultName = new CommandButtonWidgetExt();
  TextBoxWidgetExt ufti_userRecipe = new TextBoxWidgetExt();
  CommandButtonWidgetExt ufti_userSpec = new CommandButtonWidgetExt();
  JScrollPane jScrollPane4 = new JScrollPane();
  TableWidgetExt ufti_recipeTable = new TableWidgetExt();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout6 = new GridBagLayout();
  GridBagLayout gridBagLayout7 = new GridBagLayout();
  GridBagLayout gridBagLayout8 = new GridBagLayout();

  public DRRecipeGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setLayout(cardLayout1);
    //jPanel2.setPreferredSize(new Dimension(471, 60));
    //jScrollPane1.setPreferredSize(new Dimension(453, 200));
    cgs4_darkSet.setText("Set");
    cgs4_objectSet.setText("Set");
    cgs4_objectRecipe.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        cgs4_objectRecipe_actionPerformed(e);
      }
    });
    cgs4Panel.setLayout(borderLayout2);
    jPanel4.setLayout(gridBagLayout2);
    jLabel9.setText("Recipe Name");
    jLabel9.setForeground(Color.black);
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setText("Include in Group?");
    jLabel10.setForeground(Color.black);
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setText("OBJECT");
    jLabel11.setForeground(Color.black);
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel12.setText("SKY");
    jLabel12.setForeground(Color.black);
    jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel13.setText("DARK");
    jLabel13.setForeground(Color.black);
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel14.setText("BIAS");
    jLabel14.setForeground(Color.black);
    jLabel14.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel15.setText("Observation Type");
    jLabel15.setForeground(Color.black);
    jLabel15.setFont(new java.awt.Font("Dialog", 0, 12));
    cgs4_biasRecipe.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        cgs4_biasRecipe_actionPerformed(e);
      }
    });
    cgs4_skySet.setText("Set");
    ircam3_darkSet.setText("Set");
    ircam3_objectSet.setText("Set");
    ircam3_objectRecipe.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        ircam3_objectRecipe_actionPerformed(e);
      }
    });
    ircam3Panel.setLayout(borderLayout3);
    jPanel6.setLayout(gridBagLayout3);
    jLabel16.setText("Recipe Name");
    jLabel16.setForeground(Color.black);
    jLabel16.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel17.setText("Include in Group?");
    jLabel17.setForeground(Color.black);
    jLabel17.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel18.setText("OBJECT");
    jLabel18.setForeground(Color.black);
    jLabel18.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel19.setText("SKY");
    jLabel19.setForeground(Color.black);
    jLabel19.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel110.setText("DARK");
    jLabel110.setForeground(Color.black);
    jLabel110.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel111.setText("BIAS");
    jLabel111.setForeground(Color.black);
    jLabel111.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel112.setText("Observation Type");
    jLabel112.setForeground(Color.black);
    jLabel112.setFont(new java.awt.Font("Dialog", 0, 12));
    ircam3_biasRecipe.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        ircam3_biasRecipe_actionPerformed(e);
      }
    });
    ircam3_skySet.setText("Set");
    ircam3_biasSet.setText("Set");
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setForeground(Color.black);
    jLabel1.setText("FLAT");
    cgs4_flatSet.setText("Set");
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setForeground(Color.black);
    jLabel2.setText("ARC");
    cgs4_arcSet.setText("Set");
    michelle_flatSet.setText("Set");
    michelle_objectRecipe.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        michelle_objectRecipe_actionPerformed(e);
      }
    });
    michellePanel.setLayout(borderLayout4);
    michelle_skySet.setText("Set");
    jLabel113.setText("Observation Type");
    jLabel113.setForeground(Color.black);
    jLabel113.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel114.setText("BIAS");
    jLabel114.setForeground(Color.black);
    jLabel114.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel115.setText("DARK");
    jLabel115.setForeground(Color.black);
    jLabel115.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel116.setText("SKY");
    jLabel116.setForeground(Color.black);
    jLabel116.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel117.setText("OBJECT");
    jLabel117.setForeground(Color.black);
    jLabel117.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel118.setText("Include in Group?");
    jLabel118.setForeground(Color.black);
    jLabel118.setFont(new java.awt.Font("Dialog", 0, 12));
    michelle_biasRecipe.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        michelle_biasRecipe_actionPerformed(e);
      }
    });
    michelle_darkSet.setText("Set");
    michelle_arcSet.setText("Set");
    jPanel7.setLayout(gridBagLayout4);
    jLabel21.setText("Recipe Name");
    jLabel21.setForeground(Color.black);
    jLabel21.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setForeground(Color.black);
    jLabel3.setText("ARC");
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel4.setForeground(Color.black);
    jLabel4.setText("FLAT");
    michelle_objectSet.setText("Set");
    michelle_biasSet.setText("Set");
    ufti_objectRecipe.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        ufti_objectRecipe_actionPerformed(e);
      }
    });
    uftiPanel.setLayout(borderLayout5);
    jLabel125.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel125.setForeground(Color.black);
    jLabel125.setText("Include in Group?");
    jPanel9.setLayout(gridBagLayout5);
    jLabel124.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel124.setForeground(Color.black);
    jLabel124.setText("OBJECT");
    jLabel123.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel123.setForeground(Color.black);
    jLabel123.setText("SKY");
    jLabel122.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel122.setForeground(Color.black);
    jLabel122.setText("DARK");
    jLabel120.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel120.setForeground(Color.black);
    jLabel120.setText("Observation Type");
    ufti_darkSet.setText("Set");
    ufti_objectSet.setText("Set");
    ufti_skySet.setText("Set");
    jLabel20.setText("Recipe Name");
    jLabel20.setForeground(Color.black);
    jLabel20.setFont(new java.awt.Font("Dialog", 0, 12));
    cgs4_biasSet.setText("Set");
    jLabel5.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel5.setForeground(Color.black);
    jLabel5.setText("CGS4 DR Recipe");
    jLabel6.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel6.setForeground(Color.black);
    jLabel6.setText("IRCAM3 DR Recipe");
    jLabel7.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel7.setForeground(Color.black);
    jLabel7.setText("Michelle DR Recipe");
    jLabel8.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel8.setForeground(Color.black);
    jLabel8.setText("UFTI DR Recipe");
    cgs4_defaultName.setText("Reset Defaults");
    cgs4_userSpec.setText("User Specified:");
    jPanel3.setLayout(gridBagLayout1);
    jPanel3.setMinimumSize(new Dimension(260, 60));
    cgs4_userRecipe.setText("ENTER_YOUR_OWN_RECIPE_HERE");
    cgs4_userRecipe.setEditable(false);
    jPanel1.setLayout(borderLayout1);
    jScrollPane1.setPreferredSize(new Dimension(100, 120));
    jPanel2.setLayout(borderLayout6);
    ircam3_userSpec.setText("User Specified:");
    ircam3_defaultName.setText("Reset Defaults");
    jPanel5.setLayout(gridBagLayout6);
    jPanel5.setMinimumSize(new Dimension(260, 60));
    ircam3_userRecipe.setText("ENTER_YOUR_OWN_RECIPE_HERE");
    ircam3_userRecipe.setEditable(false);
    jPanel8.setLayout(borderLayout7);
    michelle_defaultName.setText("Reset Defaults");
    michelle_userRecipe.setEditable(false);
    michelle_userRecipe.setText("ENTER_YOUR_OWN_RECIPE_HERE");
    michelle_userSpec.setText("User Specified:");
    jPanel11.setMinimumSize(new Dimension(260, 60));
    jPanel11.setLayout(gridBagLayout7);
    jPanel10.setLayout(borderLayout8);
    jPanel12.setMinimumSize(new Dimension(260, 60));
    jPanel12.setLayout(gridBagLayout8);
    ufti_defaultName.setText("Reset Defaults");
    ufti_userRecipe.setEditable(false);
    ufti_userRecipe.setText("ENTER_YOUR_OWN_RECIPE_HERE");
    ufti_userSpec.setText("User Specified:");
    this.add(emptyPanel, "empty");
    this.add(cgs4Panel, "cgs4");
    cgs4Panel.add(jPanel4, BorderLayout.NORTH);
    jPanel4.add(jLabel11, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 15, 0));
    jPanel4.add(cgs4_biasRecipe, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 52, 0));
    jPanel4.add(cgs4_darkRecipe, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 42, 0));
    jPanel4.add(cgs4_skyRecipe, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_objectRecipe, new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_darkSet, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_skySet, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_objectSet, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_biasInGroup, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_darkInGroup, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_skyInGroup, new GridBagConstraints(3, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_objectInGroup, new GridBagConstraints(3, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(jLabel10, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(jLabel9, new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, -2, 0, 27), 0, 0));
    jPanel4.add(jLabel15, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(jLabel14, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(jLabel13, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(jLabel12, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(jLabel1, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_flatSet, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_flatRecipe, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_flatInGroup, new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(jLabel2, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_arcSet, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_arcRecipe, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_arcInGroup, new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(cgs4_biasSet, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(jLabel5, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));
    cgs4Panel.add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(jPanel3, BorderLayout.NORTH);
    jPanel3.add(cgs4_defaultName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    jPanel3.add(cgs4_userSpec, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
    jPanel3.add(cgs4_userRecipe, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    jPanel1.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(cgs4_recipeTable, null);
    this.add(ircam3Panel, "ircam3");
    ircam3Panel.add(jPanel6, BorderLayout.NORTH);
    jPanel6.add(jLabel18, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 15, 0));
    jPanel6.add(ircam3_biasSet, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 17), 0, 0));
    jPanel6.add(ircam3_biasRecipe, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 52, 0));
    jPanel6.add(ircam3_darkRecipe, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 42, 0));
    jPanel6.add(ircam3_skyRecipe, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(ircam3_objectRecipe, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(ircam3_darkSet, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(ircam3_skySet, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(ircam3_objectSet, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(ircam3_biasInGroup, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(ircam3_darkInGroup, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(ircam3_skyInGroup, new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(ircam3_objectInGroup, new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(jLabel17, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(jLabel16, new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, -2, 0, 27), 0, 0));
    jPanel6.add(jLabel112, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(jLabel111, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(jLabel110, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(jLabel19, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(jLabel6, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));
    ircam3Panel.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(jPanel5, BorderLayout.NORTH);
    jPanel5.add(ircam3_defaultName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    jPanel5.add(ircam3_userSpec, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    jPanel5.add(ircam3_userRecipe, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    jPanel2.add(jScrollPane2, BorderLayout.CENTER);
    jScrollPane2.getViewport().add(ircam3_recipeTable, null);
    this.add(michellePanel, "michelle");
    michellePanel.add(jPanel7, BorderLayout.NORTH);
    jPanel7.add(jLabel117, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 15, 0));
    jPanel7.add(michelle_biasRecipe, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 52, 0));
    jPanel7.add(michelle_darkRecipe, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 42, 0));
    jPanel7.add(michelle_skyRecipe, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_objectRecipe, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_darkSet, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_skySet, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_objectSet, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_biasInGroup, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_darkInGroup, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_skyInGroup, new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_objectInGroup, new GridBagConstraints(3, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(jLabel118, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(jLabel21, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, -2, 0, 27), 0, 0));
    jPanel7.add(jLabel113, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(jLabel114, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(jLabel115, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(jLabel116, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(jLabel4, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_flatSet, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_flatRecipe, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_flatInGroup, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(jLabel3, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_arcSet, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_arcRecipe, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_arcInGroup, new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(michelle_biasSet, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel7.add(jLabel7, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));
    michellePanel.add(jPanel8, BorderLayout.CENTER);
    jPanel8.add(jPanel11, BorderLayout.NORTH);
    jPanel11.add(michelle_defaultName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    jPanel11.add(michelle_userSpec, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    jPanel11.add(michelle_userRecipe, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    jPanel8.add(jScrollPane3, BorderLayout.CENTER);
    jScrollPane3.getViewport().add(michelle_recipeTable, null);
    this.add(uftiPanel, "ufti");
    uftiPanel.add(jPanel9, BorderLayout.NORTH);
    jPanel9.add(jLabel124, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 15, 0));
    jPanel9.add(ufti_darkRecipe, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 42, 0));
    jPanel9.add(ufti_skyRecipe, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel9.add(ufti_objectRecipe, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel9.add(ufti_darkSet, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel9.add(ufti_skySet, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel9.add(ufti_objectSet, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel9.add(ufti_darkInGroup, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel9.add(ufti_skyInGroup, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel9.add(ufti_objectInGroup, new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel9.add(jLabel125, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel9.add(jLabel20, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, -2, 0, 27), 0, 0));
    jPanel9.add(jLabel120, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel9.add(jLabel122, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel9.add(jLabel123, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel9.add(jLabel8, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));
    uftiPanel.add(jPanel10, BorderLayout.CENTER);
    jPanel10.add(jPanel12, BorderLayout.NORTH);
    jPanel12.add(ufti_defaultName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    jPanel12.add(ufti_userSpec, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    jPanel12.add(ufti_userRecipe, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    jPanel10.add(jScrollPane4, BorderLayout.CENTER);
    jScrollPane4.add(ufti_recipeTable, null);
  }



  void cgs4_objectRecipe_actionPerformed(ActionEvent e) {

  }

  void cgs4_biasRecipe_actionPerformed(ActionEvent e) {

  }

  void ircam3_objectRecipe_actionPerformed(ActionEvent e) {

  }

  void ircam3_biasRecipe_actionPerformed(ActionEvent e) {

  }





  void michelle_objectRecipe_actionPerformed(ActionEvent e) {

  }

  void michelle_biasRecipe_actionPerformed(ActionEvent e) {

  }

  void ufti_objectRecipe_actionPerformed(ActionEvent e) {

  }



}
