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

public class IterFocusObsGUI extends JPanel {
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
  JTextField jTextField29 = new JTextField();
  JTextField jTextField28 = new JTextField();
  JTextField jTextField27 = new JTextField();
  JTextField jTextField26 = new JTextField();
  JLabel jLabel44 = new JLabel();
  JLabel jLabel43 = new JLabel();
  JLabel jLabel42 = new JLabel();
  JComboBox axisComboBox = new JComboBox();
  JLabel jLabel41 = new JLabel();
  JCheckBox jCheckBox5 = new JCheckBox();
  JLabel jLabel311 = new JLabel();
  JLabel jLabel310 = new JLabel();
  JPanel repeatPanel = new JPanel();
  JLabel jLabel1 = new JLabel();
  JComboBox repeatComboBox = new JComboBox();
  JLabel jLabel3 = new JLabel();

  public IterFocusObsGUI() {
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
    jLabel2.setBounds(new Rectangle(9, 167, 132, 17));
    jComboBox2.setBounds(new Rectangle(9, 191, 130, 26));
    jComboBox2.addItemListener(new java.awt.event.ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        jComboBox2_itemStateChanged(e);
      }
    });
    this.setBackground(new Color(204, 204, 204));
    this.setBorder(BorderFactory.createLineBorder(Color.black));
    switchingModePanel.setBounds(new Rectangle(151, 167, 217, 121));
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
    jLabel11.setBounds(new Rectangle(142, 29, 60, 17));
    jLabel12.setText("System");
    jLabel12.setBounds(new Rectangle(14, 80, 59, 17));
    jLabel13.setText("y");
    jLabel13.setBounds(new Rectangle(15, 55, 41, 17));
    jLabel14.setText("x");
    jLabel14.setBounds(new Rectangle(17, 28, 43, 17));
    systemComboBox.setBounds(new Rectangle(80, 77, 130, 26));
    jLabel25.setText("Throw");
    jLabel25.setBounds(new Rectangle(12, 25, 60, 17));
    jTextField18.setText("60");
    jTextField18.setBounds(new Rectangle(75, 26, 48, 21));
    jLabel26.setText("(arcsec)");
    jLabel26.setBounds(new Rectangle(130, 29, 75, 18));
    jTextField19.setBounds(new Rectangle(75, 55, 48, 21));
    jTextField19.setText("7.8125");
    jLabel27.setBounds(new Rectangle(130, 58, 62, 18));
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
    jTextField29.setText("5");
    jTextField29.setBounds(new Rectangle(311, 105, 56, 21));
    jTextField28.setText("1");
    jTextField28.setBounds(new Rectangle(221, 59, 70, 21));
    jTextField27.setBounds(new Rectangle(98, 12, 70, 21));
    jTextField27.setText("30");
    jTextField26.setBounds(new Rectangle(97, 45, 70, 21));
    jTextField26.setText("10");
    jLabel44.setText("No of Focus Points");
    jLabel44.setBounds(new Rectangle(191, 104, 118, 17));
    jLabel43.setText("(mm)");
    jLabel43.setBounds(new Rectangle(294, 61, 48, 17));
    jLabel42.setText("Step");
    jLabel42.setBounds(new Rectangle(190, 62, 48, 17));
    axisComboBox.setBounds(new Rectangle(221, 10, 130, 26));
    jLabel41.setText("Axis");
    jLabel41.setBounds(new Rectangle(191, 16, 48, 17));
    jCheckBox5.setBounds(new Rectangle(15, 82, 149, 25));
    jCheckBox5.setText("Cycle Reversal");
    jCheckBox5.setSelected(true);
    jLabel311.setBounds(new Rectangle(4, 14, 88, 17));
    jLabel311.setText("Sec per cycle");
    jLabel310.setBounds(new Rectangle(4, 48, 87, 17));
    jLabel310.setText("No of Cycles");
    jComboBox4.addItem("FK4 (B1950)");
    jComboBox4.addItem("FK5 (J2000)");
    jComboBox4.addItem("AZEL");
    repeatPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    repeatPanel.setBounds(new Rectangle(5, 232, 146, 44));
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
    jLabel3.setToolTipText("");
    jLabel3.setText("X");
    this.add(jLabel310, null);
    this.add(jTextField27, null);
    this.add(jTextField26, null);
    this.add(jCheckBox5, null);
    this.add(jTextField28, null);
    this.add(jTextField29, null);
    this.add(jLabel43, null);
    this.add(axisComboBox, null);
    this.add(jLabel2, null);
    this.add(jComboBox2, null);
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
    this.add(jLabel41, null);
    this.add(jLabel42, null);
    this.add(jLabel44, null);
    this.add(jLabel311, null);
    this.add(repeatPanel, null);
    repeatPanel.add(jLabel1, null);
    repeatPanel.add(repeatComboBox, null);
    repeatPanel.add(jLabel3, null);
    pointingPixelPopupMenu.add(pointingPixelAutomaticMenuItem);
    pointingPixelPopupMenu.add(jMenu2);
//    pointingPixelPopupMenu.add(jMenuItem3);
    jMenu2.add(pointingPixelManual1MenuItem);
    jMenu2.add(pointingPixelManualTBD2MenuItem);
    jMenu2.add(pointingPixelManualTBD3MenuItem);
    axisComboBox.addItem("x");
    axisComboBox.addItem("y");
    axisComboBox.addItem("z");
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
