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

public class IterRasterObsGUI extends JPanel {
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
  JMenuItem jMenuItem3 = new JMenuItem();
  JMenu jMenu2 = new JMenu();
  JButton jButton1 = new JButton();
  JRadioButton jRadioButton2 = new JRadioButton();
  JRadioButton jRadioButton1 = new JRadioButton();
  JLabel jLabel18 = new JLabel();
  JLabel jLabel17 = new JLabel();
  JLabel jLabel16 = new JLabel();
  JLabel jLabel15 = new JLabel();
  JLabel jLabel10 = new JLabel();
  JCheckBox jCheckBox2 = new JCheckBox();
  JTextField jTextField17 = new JTextField();
  JTextField jTextField11 = new JTextField();
  JTextField jTextField10 = new JTextField();
  JPanel jPanel1 = new JPanel();
  JTextField jTextField7 = new JTextField();
  JTextField jTextField6 = new JTextField();
  JTextField jTextField5 = new JTextField();
  JTextField jTextField4 = new JTextField();
  JTextField jTextField3 = new JTextField();
  JTextField jTextField2 = new JTextField();
  JTextField jTextField1 = new JTextField();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel114 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JComboBox jComboBox3 = new JComboBox();
  JPanel repeatPanel = new JPanel();
  JComboBox repeatComboBox = new JComboBox();
  JLabel jLabel19 = new JLabel();
  JLabel jLabel1 = new JLabel();

  public IterRasterObsGUI() {
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
    jLabel2.setBounds(new Rectangle(6, 296, 126, 17));
    jComboBox2.setBounds(new Rectangle(4, 318, 130, 26));
    jComboBox2.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        jComboBox2_itemStateChanged(e);
      }
    });
    this.setBorder(BorderFactory.createLineBorder(Color.black));
    switchingModePanel.setBounds(new Rectangle(177, 272, 217, 121));
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
    jLabel11.setBounds(new Rectangle(143, 29, 59, 17));
    jLabel12.setText("System");
    jLabel12.setBounds(new Rectangle(14, 80, 59, 17));
    jLabel13.setText("y");
    jLabel13.setBounds(new Rectangle(15, 55, 46, 17));
    jLabel14.setText("x");
    jLabel14.setBounds(new Rectangle(17, 28, 45, 17));
    systemComboBox.setBounds(new Rectangle(80, 77, 130, 26));
    jLabel25.setText("Throw");
    jLabel25.setBounds(new Rectangle(12, 25, 60, 17));
    jTextField18.setText("60");
    jTextField18.setBounds(new Rectangle(75, 26, 48, 21));
    jLabel26.setText("(arcsec)");
    jLabel26.setBounds(new Rectangle(124, 29, 76, 18));
    jTextField19.setBounds(new Rectangle(75, 55, 48, 21));
    jTextField19.setText("7.8125");
    jLabel27.setBounds(new Rectangle(124, 58, 63, 18));
    jLabel27.setText("(Hz)");
    jLabel28.setBounds(new Rectangle(12, 54, 60, 17));
    jLabel28.setText("Rate");
    jLabel115.setBounds(new Rectangle(9, 120, 48, 17));
    jLabel115.setText("System");
    jTextField20.setBounds(new Rectangle(73, 90, 51, 21));
    jTextField20.setText("0");
    jLabel29.setBounds(new Rectangle(11, 92, 53, 17));
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
    jButton1.setText("Default");
    jButton1.setBounds(new Rectangle(4, 255, 130, 27));
    jRadioButton2.setText("interleafed");
    jRadioButton2.setBounds(new Rectangle(8, 42, 177, 25));
    jRadioButton1.setText("along row");
    jRadioButton1.setBounds(new Rectangle(9, 14, 177, 25));
    jLabel18.setText("secs between cals");
    jLabel18.setBounds(new Rectangle(213, 207, 122, 17));
    jLabel17.setText("secs between refs");
    jLabel17.setBounds(new Rectangle(215, 182, 130, 17));
    jLabel16.setText("secs per row");
    jLabel16.setBounds(new Rectangle(216, 160, 115, 17));
    jLabel15.setText("System");
    jLabel15.setBounds(new Rectangle(5, 87, 123, 17));
    jLabel10.setText("(msec)");
    jLabel10.setBounds(new Rectangle(161, 212, 67, 17));
    jCheckBox2.setToolTipText("");
    jCheckBox2.setText("row reversal");
    jCheckBox2.setBounds(new Rectangle(6, 92, 175, 25));
    jTextField17.setBackground(Color.lightGray);
    jTextField17.setEditable(false);
    jTextField17.setText("100");
    jTextField17.setBounds(new Rectangle(337, 231, 70, 21));
    jTextField11.setBackground(Color.lightGray);
    jTextField11.setEditable(false);
    jTextField11.setText("200");
    jTextField11.setBounds(new Rectangle(337, 206, 70, 21));
    jTextField10.setBackground(Color.lightGray);
    jTextField10.setEditable(false);
    jTextField10.setText("100");
    jTextField10.setBounds(new Rectangle(337, 182, 70, 21));
    jPanel1.setBorder(titledBorder5);
    jPanel1.setBounds(new Rectangle(212, 6, 203, 149));
    jPanel1.setLayout(null);
    jTextField7.setBackground(Color.lightGray);
    jTextField7.setEditable(false);
    jTextField7.setText("5");
    jTextField7.setBounds(new Rectangle(336, 157, 71, 21));
    jTextField6.setText("50");
    jTextField6.setBounds(new Rectangle(98, 210, 63, 21));
    jTextField5.setText("40");
    jTextField5.setBounds(new Rectangle(98, 176, 63, 21));
    jTextField4.setText("20");
    jTextField4.setBounds(new Rectangle(99, 143, 63, 21));
    jTextField3.setText("0");
    jTextField3.setBounds(new Rectangle(63, 63, 30, 21));
    jTextField2.setText("10");
    jTextField2.setBounds(new Rectangle(63, 41, 31, 21));
    jTextField1.setText("10");
    jTextField1.setBounds(new Rectangle(63, 18, 31, 21));
    jLabel9.setText("Sample Time");
    jLabel9.setBounds(new Rectangle(3, 213, 90, 17));
    jLabel8.setText("n rows per cal");
    jLabel8.setBounds(new Rectangle(4, 178, 93, 17));
    jLabel7.setText("n rows per ref");
    jLabel7.setBounds(new Rectangle(6, 143, 96, 17));
    jLabel114.setText("secs per observation");
    jLabel114.setBounds(new Rectangle(213, 232, 132, 17));
    jLabel6.setText("(arc min)");
    jLabel6.setBounds(new Rectangle(94, 67, 104, 17));
    jLabel5.setText("theta");
    jLabel5.setBounds(new Rectangle(6, 64, 54, 17));
    jLabel4.setText("y");
    jLabel4.setBounds(new Rectangle(7, 38, 37, 17));
    jLabel3.setText("x");
    jLabel3.setBounds(new Rectangle(8, 15, 36, 17));
    jComboBox3.setBounds(new Rectangle(7, 110, 130, 26));
    jComboBox4.addItem("FK4 (B1950)");
    jComboBox4.addItem("FK5 (J2000)");
    jComboBox4.addItem("AZEL");
    repeatPanel.setBounds(new Rectangle(4, 355, 146, 44));
    repeatPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    repeatComboBox.setPreferredSize(new Dimension(30, 26));
    jLabel19.setToolTipText("");
    jLabel19.setText("X");
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
    this.add(jRadioButton2, null);
    this.add(jRadioButton1, null);
    this.add(jCheckBox2, null);
    this.add(jTextField6, null);
    this.add(jTextField5, null);
    this.add(jTextField4, null);
    this.add(jLabel9, null);
    this.add(jLabel8, null);
    this.add(jLabel7, null);
    this.add(jButton1, null);
    this.add(jLabel10, null);
    this.add(jLabel16, null);
    this.add(jLabel18, null);
    this.add(jLabel17, null);
    this.add(jTextField17, null);
    this.add(jTextField11, null);
    this.add(jTextField10, null);
    this.add(jTextField7, null);
    this.add(jLabel114, null);
    this.add(jLabel2, null);
    this.add(jComboBox2, null);
    this.add(jPanel1, null);
    jPanel1.add(jLabel3, null);
    jPanel1.add(jLabel15, null);
    jPanel1.add(jLabel4, null);
    jPanel1.add(jLabel5, null);
    jPanel1.add(jLabel6, null);
    jPanel1.add(jTextField2, null);
    jPanel1.add(jTextField1, null);
    jPanel1.add(jTextField3, null);
    jPanel1.add(jComboBox3, null);
    this.add(repeatPanel, null);
    repeatPanel.add(jLabel1, null);
    repeatPanel.add(repeatComboBox, null);
    repeatPanel.add(jLabel19, null);
    this.add(switchingModePanel, null);
    switchingModePanel.add(nodPanel, "nod");
    nodPanel.add(jLabel14, null);
    nodPanel.add(jTextField9, null);
    nodPanel.add(jLabel12, null);
    nodPanel.add(jTextField8, null);
    nodPanel.add(jLabel11, null);
    nodPanel.add(systemComboBox, null);
    nodPanel.add(jLabel13, null);
    switchingModePanel.add(frequencyPanel, "frequency");
    frequencyPanel.add(jLabel32, null);
    frequencyPanel.add(jTextField23, null);
    frequencyPanel.add(jLabel33, null);
    frequencyPanel.add(jLabel34, null);
    frequencyPanel.add(jTextField24, null);
    frequencyPanel.add(jLabel35, null);
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
    jComboBox3.addItem("FK4 (B1950)");
    jComboBox3.addItem("FK5 (J2000)");
    jComboBox3.addItem("AZEL");
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

}
