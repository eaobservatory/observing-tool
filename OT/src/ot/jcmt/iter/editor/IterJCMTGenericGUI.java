
/**
 * Title:        Acsis JBuilder GUIs<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */
package ot.jcmt.iter.editor;

import java.awt.*;
import javax.swing.*;
import jsky.app.ot.gui.*;
import javax.swing.border.*;

public class IterJCMTGenericGUI extends JPanel {

  public static final String NOD       = "Nod";
  public static final String CHOP      = "Chop";
  public static final String FREQUENCY = "Frequency";
  public static final String NONE      = "None";

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  DropDownListBoxWidgetExt noOfIntegrations = new DropDownListBoxWidgetExt();
  JLabel jLabel2 = new JLabel();
  DropDownListBoxWidgetExt switchingMode = new DropDownListBoxWidgetExt();
  JPanel switchingModePanel = new JPanel();
  CardLayout cardLayout1 = new CardLayout();
  JPanel nodPanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JLabel jLabel3 = new JLabel();
  TextBoxWidgetExt referenceOffset_x = new TextBoxWidgetExt();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel7 = new JLabel();
  TextBoxWidgetExt referenceOffset_y = new TextBoxWidgetExt();
  DropDownListBoxWidgetExt referenceOffset_system = new DropDownListBoxWidgetExt();
  JPanel frequencyPanel = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JLabel jLabel8 = new JLabel();
  TextBoxWidgetExt frequencyOffset_throw = new TextBoxWidgetExt();
  JLabel jLabel9 = new JLabel();
  TextBoxWidgetExt frequencyOffset_rate = new TextBoxWidgetExt();
  JLabel jLabel10 = new JLabel();
  JLabel jLabel11 = new JLabel();
  JPanel chopPanel = new JPanel();
  TitledBorder titledBorder1;
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  JLabel jLabel12 = new JLabel();
  TextBoxWidgetExt chopParameters_throw = new TextBoxWidgetExt();
  JLabel jLabel13 = new JLabel();
  JTextField chopParameters_rate = new JTextField();
  JLabel jLabel14 = new JLabel();
  TextBoxWidgetExt chopParameters_angle = new TextBoxWidgetExt();
  Border border1;

  public IterJCMTGenericGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    border1 = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(134, 134, 134),new Color(93, 93, 93)),BorderFactory.createEmptyBorder(5,0,0,0));
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setForeground(Color.black);
    jLabel1.setText("No of Integrations");
    this.setLayout(borderLayout1);
    jPanel1.setLayout(gridBagLayout1);
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setForeground(Color.black);
    jLabel2.setText("Switching Mode");
    switchingModePanel.setLayout(cardLayout1);
    nodPanel.setLayout(gridBagLayout2);
    nodPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Reference Offset"));
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setForeground(Color.black);
    jLabel3.setText("x");
    jLabel4.setText("jLabel4");
    jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel5.setForeground(Color.black);
    jLabel5.setText("y");
    referenceOffset_x.setColumns(8);
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel6.setForeground(Color.black);
    jLabel6.setText("System");
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel7.setForeground(Color.black);
    jLabel7.setText("(arc secs)");
    frequencyPanel.setLayout(gridBagLayout3);
    frequencyPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Frequency Offset"));
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel8.setForeground(Color.black);
    jLabel8.setText("Throw");
    frequencyOffset_throw.setColumns(10);
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel9.setForeground(Color.black);
    jLabel9.setText("Rate");
    frequencyOffset_rate.setColumns(10);
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setForeground(Color.black);
    jLabel10.setText("(MHz)");
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setForeground(Color.black);
    jLabel11.setText("(Hz)");
    chopPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Chop Parameters"));
    chopPanel.setLayout(gridBagLayout4);
    jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel12.setForeground(Color.black);
    jLabel12.setText("Throw");
    chopParameters_throw.setEditable(false);
    chopParameters_throw.setColumns(10);
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel13.setForeground(Color.black);
    jLabel13.setText("Rate");
    chopParameters_rate.setEditable(false);
    chopParameters_rate.setColumns(10);
    jLabel14.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel14.setForeground(Color.black);
    jLabel14.setText("Angle");
    chopParameters_angle.setEditable(false);
    chopParameters_angle.setColumns(10);
    this.setBorder(border1);
    switchingMode.setFont(new java.awt.Font("Dialog", 0, 12));
    noOfIntegrations.setFont(new java.awt.Font("Dialog", 0, 12));
    this.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(jLabel1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(15, 5, 0, 0), 0, 0));
    jPanel1.add(noOfIntegrations, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    jPanel1.add(jLabel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    jPanel1.add(switchingMode, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    jPanel1.add(switchingModePanel, new GridBagConstraints(1, 0, 1, 4, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 0, 0), 0, 0));
    switchingModePanel.add(nodPanel, NOD);
    nodPanel.add(jLabel3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    nodPanel.add(referenceOffset_x, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    nodPanel.add(jLabel5, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    nodPanel.add(jLabel6, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    nodPanel.add(jLabel7, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    nodPanel.add(referenceOffset_y, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    nodPanel.add(referenceOffset_system, new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    switchingModePanel.add(frequencyPanel, FREQUENCY);
    frequencyPanel.add(jLabel8, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    frequencyPanel.add(frequencyOffset_throw, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    frequencyPanel.add(jLabel9, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    frequencyPanel.add(frequencyOffset_rate, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    frequencyPanel.add(jLabel10, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    frequencyPanel.add(jLabel11, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    switchingModePanel.add(chopPanel, CHOP);
    chopPanel.add(jLabel12, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    chopPanel.add(chopParameters_throw, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    chopPanel.add(jLabel13, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    chopPanel.add(chopParameters_rate, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    chopPanel.add(jLabel14, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    chopPanel.add(chopParameters_angle, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    switchingModePanel.add(new JPanel(), NONE);
  }
}
