/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.iter.editor;

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

public class IterPointingObsGUI extends JPanel {
  JLabel jLabel2 = new JLabel();
  JComboBox jComboBox2 = new JComboBox();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  TitledBorder titledBorder3;
  TitledBorder titledBorder4;
  TitledBorder titledBorder5;
  JPanel switchingModePanel = new JPanel();
  CardLayout cardLayout2 = new CardLayout();
  JPanel nodPanel = new JPanel();
  TitledBorder titledBorder6;
  JPanel frequencyPanel = new JPanel();
  TitledBorder titledBorder7;
  JPanel chopPanel = new JPanel();
  TitledBorder titledBorder8;
  JTextField jTextField8 = new JTextField();
  JTextField jTextField9 = new JTextField();
  JLabel jLabel11 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JLabel jLabel13 = new JLabel();
  JLabel jLabel14 = new JLabel();
  JComboBox systemComboBox = new JComboBox();
  JLabel jLabel25 = new JLabel();
  JTextField jTextField18 = new JTextField();
  JLabel jLabel26 = new JLabel();
  JTextField jTextField19 = new JTextField();
  JLabel jLabel27 = new JLabel();
  JLabel jLabel28 = new JLabel();
  JLabel jLabel115 = new JLabel();
  JTextField jTextField20 = new JTextField();
  JLabel jLabel29 = new JLabel();
  JComboBox jComboBox4 = new JComboBox();
  JLabel jLabel32 = new JLabel();
  JTextField jTextField23 = new JTextField();
  JLabel jLabel33 = new JLabel();
  JLabel jLabel34 = new JLabel();
  JTextField jTextField24 = new JTextField();
  JLabel jLabel35 = new JLabel();
  TitledBorder titledBorder9;
  TitledBorder titledBorder10;
  TitledBorder titledBorder11;
  JPopupMenu pointingPixelPopupMenu = new JPopupMenu();
  JMenuItem jMenuItem3 = new JMenuItem();
  JMenu jMenu2 = new JMenu();
  JMenuItem pointingPixelManual1MenuItem = new JMenuItem();
  JMenuItem pointingPixelManualTBD2MenuItem = new JMenuItem();
  JMenuItem pointingPixelManualTBD3MenuItem = new JMenuItem();
  JMenuItem pointingPixelAutomaticMenuItem = new JMenuItem();
  JTextField jTextField112 = new JTextField();
  JTextField jTextField111 = new JTextField();
  JTextField cycleTextField1 = new JTextField();
  JRadioButton jRadioButton4 = new JRadioButton();
  JRadioButton jRadioButton3 = new JRadioButton();
  JComboBox pointingMethodComboBox = new JComboBox();
  JLabel jLabel214 = new JLabel();
  JLabel jLabel213 = new JLabel();
  JLabel jLabel211 = new JLabel();
  JLabel jLabel210 = new JLabel();
  JCheckBox jCheckBox4 = new JCheckBox();
  JButton pointingPixelButton = new JButton();
  JTextField stepSizeTextField1 = new JTextField();
  JLabel jLabel38 = new JLabel();
  JLabel jLabel119 = new JLabel();
  JLabel jLabel118 = new JLabel();
  JLabel jLabel116 = new JLabel();
  JTextField jTextField113 = new JTextField();
  JPanel repeatPanel = new JPanel();
  JComboBox repeatComboBox = new JComboBox();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel1 = new JLabel();

  public IterPointingObsGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("Raster");
    titledBorder2 = new TitledBorder("Jiggle");
    titledBorder3 = new TitledBorder("Stare");
    titledBorder5 = new TitledBorder("Map Size and Angle");
    titledBorder6 = new TitledBorder("Reference Offset");
    titledBorder7 = new TitledBorder("Frequency Offset");
    titledBorder8 = new TitledBorder("Chop Parameters");
    titledBorder9 = new TitledBorder("Pointing");
    titledBorder10 = new TitledBorder("Focus");
    titledBorder11 = new TitledBorder("Skydip");
    this.setLayout(null);

//    jComboBox1.addItem("");
//    jComboBox1.addItem("");
//    jComboBox1.addItem("sample");
//    jComboBox1.addItem("stare");


    jComboBox2.addItem("Nod");
    jComboBox2.addItem("Chop");
    jComboBox2.addItem("Frequency");
    jComboBox2.addItem("None");

    systemComboBox.addItem("FK4 (B1950)");
    systemComboBox.addItem("FK5 (J2000)");
    systemComboBox.addItem("AZEL");






    jLabel2.setText("Switching Mode");
    jLabel2.setBounds(new Rectangle(10, 270, 130, 17));
    jComboBox2.setBounds(new Rectangle(9, 287, 130, 26));
    jComboBox2.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        jComboBox2_itemStateChanged(e);
      }
    });
    this.setBorder(BorderFactory.createLineBorder(Color.black));
    switchingModePanel.setBounds(new Rectangle(154, 257, 217, 121));
    switchingModePanel.setLayout(cardLayout2);
    nodPanel.setBorder(titledBorder6);
    nodPanel.setLayout(null);
    frequencyPanel.setBorder(titledBorder7);
    frequencyPanel.setLayout(null);
    chopPanel.setBorder(titledBorder8);
    chopPanel.setLayout(null);
    jTextField8.setText("0");
    jTextField8.setBounds(new Rectangle(81, 51, 59, 21));
    jTextField9.setText("1800");
    jTextField9.setBounds(new Rectangle(80, 26, 61, 21));
    jLabel11.setText("(arc sec)");
    jLabel11.setBounds(new Rectangle(143, 29, 66, 17));
    jLabel12.setText("System");
    jLabel12.setBounds(new Rectangle(14, 80, 59, 17));
    jLabel13.setText("y");
    jLabel13.setBounds(new Rectangle(15, 55, 37, 17));
    jLabel14.setText("x");
    jLabel14.setBounds(new Rectangle(17, 28, 37, 17));
    systemComboBox.setBounds(new Rectangle(80, 77, 130, 26));
    jLabel25.setText("Throw");
    jLabel25.setBounds(new Rectangle(12, 25, 60, 17));
    jTextField18.setText("60");
    jTextField18.setBounds(new Rectangle(75, 26, 48, 21));
    jLabel26.setText("(arcsec)");
    jLabel26.setBounds(new Rectangle(130, 29, 78, 18));
    jTextField19.setBounds(new Rectangle(75, 55, 48, 21));
    jTextField19.setText("7.8125");
    jLabel27.setBounds(new Rectangle(130, 58, 71, 18));
    jLabel27.setText("(Hz)");
    jLabel28.setBounds(new Rectangle(12, 54, 60, 17));
    jLabel28.setText("Rate");
    jLabel115.setBounds(new Rectangle(9, 120, 48, 17));
    jLabel115.setText("System");
    jTextField20.setBounds(new Rectangle(73, 90, 51, 21));
    jTextField20.setText("0");
    jLabel29.setBounds(new Rectangle(11, 92, 41, 17));
    jLabel29.setText("angle");
    jComboBox4.setBounds(new Rectangle(70, 121, 113, 26));
    jLabel32.setText("Throw");
    jLabel32.setBounds(new Rectangle(10, 26, 48, 17));
    jTextField23.setText("20");
    jTextField23.setBounds(new Rectangle(57, 25, 70, 21));
    jLabel33.setText("(MHz)");
    jLabel33.setBounds(new Rectangle(137, 26, 48, 17));
    jLabel34.setText("Rate");
    jLabel34.setBounds(new Rectangle(9, 62, 48, 17));
    jTextField24.setText("1");
    jTextField24.setBounds(new Rectangle(58, 58, 70, 21));
    jLabel35.setText("(Hz)");
    jLabel35.setBounds(new Rectangle(137, 59, 48, 17));
    jMenuItem3.setText("choice 3");
    jMenu2.setText("manual");
    pointingPixelManual1MenuItem.setText("1");
    pointingPixelManual1MenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        pointingPixelManual1MenuItem_actionPerformed(e);
      }
    });
    pointingPixelManualTBD2MenuItem.setText("to be decided");
    pointingPixelManualTBD2MenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        pointingPixelManualTBD2MenuItem_actionPerformed(e);
      }
    });
    pointingPixelManualTBD3MenuItem.setText("to be decided");
    pointingPixelManualTBD3MenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        pointingPixelManualTBD3MenuItem_actionPerformed(e);
      }
    });
    pointingPixelAutomaticMenuItem.setText("automatic");
    pointingPixelAutomaticMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        pointingPixelAutomaticMenuItem_actionPerformed(e);
      }
    });
    jTextField112.setBackground(Color.lightGray);
    jTextField112.setEditable(false);
    jTextField112.setText("200");
    jTextField112.setBounds(new Rectangle(129, 188, 70, 21));
    jTextField111.setBackground(Color.lightGray);
    jTextField111.setEditable(false);
    jTextField111.setText("100");
    jTextField111.setBounds(new Rectangle(129, 216, 70, 21));
    cycleTextField1.setBounds(new Rectangle(108, 152, 88, 21));
    cycleTextField1.setText("10");
    jRadioButton4.setText("spectral line");
    jRadioButton4.setBounds(new Rectangle(240, 147, 154, 25));
    jRadioButton3.setText("continuum");
    jRadioButton3.setBounds(new Rectangle(240, 122, 153, 25));
    pointingMethodComboBox.setBounds(new Rectangle(157, 11, 130, 26));
    jLabel214.setBounds(new Rectangle(7, 45, 110, 17));
    jLabel214.setText("Step Size");
    jLabel213.setBounds(new Rectangle(233, 48, 91, 17));
    jLabel213.setText("(arcsec)");
    jLabel211.setBounds(new Rectangle(7, 152, 101, 17));
    jLabel211.setText("No of Cycles");
    jLabel210.setBounds(new Rectangle(5, 120, 105, 17));
    jLabel210.setText("Jiggles per Cycle");
    jCheckBox4.setBounds(new Rectangle(4, 84, 191, 25));
    jCheckBox4.setActionCommand("referenceJiggle");
    jCheckBox4.setText("Jiggle at Reference");
    pointingPixelButton.setText("automatic");
    pointingPixelButton.setBounds(new Rectangle(238, 215, 120, 27));
    pointingPixelButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        pointingPixelButton_actionPerformed(e);
      }
    });
    stepSizeTextField1.setBounds(new Rectangle(157, 45, 71, 21));
    stepSizeTextField1.setText("7");
    jLabel38.setText("Pointing Pixel");
    jLabel38.setBounds(new Rectangle(239, 194, 138, 17));
    jLabel119.setText("secs per observation");
    jLabel119.setBounds(new Rectangle(8, 221, 127, 17));
    jLabel118.setText("secs per cycle");
    jLabel118.setBounds(new Rectangle(9, 188, 103, 17));
    jLabel116.setBounds(new Rectangle(6, 17, 143, 17));
    jLabel116.setText("Pointing Method");
    jTextField113.setBounds(new Rectangle(109, 116, 86, 21));
    jTextField113.setText("4");
    jComboBox4.addItem("FK4 (B1950)");
    jComboBox4.addItem("FK5 (J2000)");
    jComboBox4.addItem("AZEL");
    repeatPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    repeatPanel.setBounds(new Rectangle(2, 329, 146, 44));
    repeatComboBox.setPreferredSize(new Dimension(30, 26));
    jLabel3.setToolTipText("");
    jLabel3.setText("X");
    jLabel1.setPreferredSize(new Dimension(60, 17));
    jLabel1.setText("Repeat");
    repeatComboBox.setPreferredSize(new Dimension(50, 26));
    repeatComboBox.setEditable(true);
    for(int i = 0; i < 100; i++) {
      if(i < 10) {
        repeatComboBox.addItem(" " + i);
      }
      else {
        repeatComboBox.addItem(""  + i);
      }
    }
    this.add(jLabel214, null);
    this.add(jLabel116, null);
    this.add(jCheckBox4, null);
    this.add(cycleTextField1, null);
    this.add(jLabel211, null);
    this.add(jLabel210, null);
    this.add(jTextField113, null);
    this.add(jTextField111, null);
    this.add(jTextField112, null);
    this.add(jLabel119, null);
    this.add(jLabel118, null);
    this.add(pointingPixelButton, null);
    this.add(jRadioButton4, null);
    this.add(jRadioButton3, null);
    this.add(jLabel38, null);
    this.add(jComboBox2, null);
    this.add(jLabel2, null);
    this.add(pointingMethodComboBox, null);
    this.add(jLabel213, null);
    this.add(stepSizeTextField1, null);
    this.add(repeatPanel, null);
    repeatPanel.add(jLabel1, null);
    repeatPanel.add(repeatComboBox, null);
    repeatPanel.add(jLabel3, null);
    this.add(switchingModePanel, null);
    switchingModePanel.add(chopPanel, "chop");
    chopPanel.add(jLabel25, null);
    chopPanel.add(jTextField18, null);
    chopPanel.add(jLabel26, null);
    chopPanel.add(jLabel28, null);
    chopPanel.add(jTextField19, null);
    chopPanel.add(jLabel27, null);
    chopPanel.add(jLabel29, null);
    chopPanel.add(jTextField20, null);
    chopPanel.add(jLabel115, null);
    chopPanel.add(jComboBox4, null);
    switchingModePanel.add(nodPanel, "nodPanel");
    nodPanel.add(jLabel14, null);
    nodPanel.add(jTextField9, null);
    nodPanel.add(jLabel12, null);
    nodPanel.add(jTextField8, null);
    nodPanel.add(jLabel11, null);
    nodPanel.add(systemComboBox, null);
    nodPanel.add(jLabel13, null);
    switchingModePanel.add(frequencyPanel, "frequencyPanel");
    frequencyPanel.add(jLabel32, null);
    frequencyPanel.add(jTextField23, null);
    frequencyPanel.add(jLabel33, null);
    frequencyPanel.add(jLabel34, null);
    frequencyPanel.add(jTextField24, null);
    frequencyPanel.add(jLabel35, null);
    pointingPixelPopupMenu.add(pointingPixelAutomaticMenuItem);
    pointingPixelPopupMenu.add(jMenu2);
//    pointingPixelPopupMenu.add(jMenuItem3);
    jMenu2.add(pointingPixelManual1MenuItem);
    jMenu2.add(pointingPixelManualTBD2MenuItem);
    jMenu2.add(pointingPixelManualTBD3MenuItem);
    pointingMethodComboBox.addItem(" 9 Position");
    pointingMethodComboBox.addItem("16 Position");
    pointingMethodComboBox.addItem("25 Position");
  }


  void jComboBox2_itemStateChanged(ItemEvent e) {
//    System.out.println("\"" + jComboBox1.getSelectedItem() + "\"");
    if(jComboBox2.getSelectedItem().equals("Nod")) {
      ((CardLayout)switchingModePanel.getLayout()).show(switchingModePanel, "nod");
//      ((CardLayout)cardPanel.getLayout()).first(cardPanel);
    }
    else if(jComboBox2.getSelectedItem().equals("Frequency")) {
      ((CardLayout)switchingModePanel.getLayout()).show(switchingModePanel, "frequency");
//      ((CardLayout)cardPanel.getLayout()).last(cardPanel);
    }
    else if(jComboBox2.getSelectedItem().equals("Chop")) {
      ((CardLayout)switchingModePanel.getLayout()).show(switchingModePanel, "chop");
//      ((CardLayout)cardPanel.getLayout()).last(cardPanel);
    }
//    else if(jComboBox2.getSelectedItem().equals("Chop")) {

  }

  void pointingPixelButton_actionPerformed(ActionEvent e) {
    pointingPixelPopupMenu.show(pointingPixelButton, 0, 0);
  }

  void pointingPixelPopupMenu_mouseReleased(MouseEvent e) {
    pointingPixelButton.setText(((JMenuItem)(e.getSource())).getText());
  }

  void pointingPixelAutomaticMenuItem_actionPerformed(ActionEvent e) {
    pointingPixelButton.setText(pointingPixelAutomaticMenuItem.getText());
  }

  void pointingPixelManual1MenuItem_actionPerformed(ActionEvent e) {
    pointingPixelButton.setText(pointingPixelManual1MenuItem.getText());
  }

  void pointingPixelManualTBD2MenuItem_actionPerformed(ActionEvent e) {
    pointingPixelButton.setText(pointingPixelManualTBD2MenuItem.getText());
  }

  void pointingPixelManualTBD3MenuItem_actionPerformed(ActionEvent e) {
    pointingPixelButton.setText(pointingPixelManualTBD3MenuItem.getText());
  }
}
