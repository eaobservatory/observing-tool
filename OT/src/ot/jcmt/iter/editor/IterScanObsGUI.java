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

import javax.swing.*;
import java.awt.*;
import jsky.app.ot.gui.*;
import javax.swing.border.*;

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */


public class IterScanObsGUI extends IterJCMTGenericGUI {

  CardLayout cardLayout1 = new CardLayout();
  JPanel scubaAcsisPanel = new JPanel();
  JPanel scubaPanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  TitledBorder titledBorder1;
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  DropDownListBoxWidgetExt system = new DropDownListBoxWidgetExt();
  TextBoxWidgetExt x = new TextBoxWidgetExt();
  TextBoxWidgetExt y = new TextBoxWidgetExt();
  TextBoxWidgetExt theta = new TextBoxWidgetExt();
  JLabel jLabel5 = new JLabel();
  TitledBorder titledBorder2;
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JLabel jLabel14 = new JLabel();
  JLabel jLabel15 = new JLabel();
  JLabel jLabel16 = new JLabel();
  TextBoxWidgetExt deltaX = new TextBoxWidgetExt();
  TextBoxWidgetExt deltaY = new TextBoxWidgetExt();
  DropDownListBoxWidgetExt coordFrame = new DropDownListBoxWidgetExt();
  JLabel jLabel17 = new JLabel();
  TextBoxWidgetExt posAngle = new TextBoxWidgetExt();

  public IterScanObsGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    scubaAcsisPanel.setLayout(gridBagLayout2);
    scubaAcsisPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Map Size and Angle"));
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setForeground(Color.black);
    jLabel1.setText("x");
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setForeground(Color.black);
    jLabel2.setText("y");
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setForeground(Color.black);
    jLabel3.setText("\u03B8");
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel4.setForeground(Color.black);
    jLabel4.setText("System");
    jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel5.setForeground(Color.black);
    jLabel5.setText("(arc min)");
    scubaPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Map Size and Angle"));
    scubaPanel.setLayout(gridBagLayout3);
    jLabel14.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel14.setForeground(Color.black);
    jLabel14.setText("\u0394 x");
    jLabel15.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel15.setForeground(Color.black);
    jLabel15.setText("\u0394 y");
    jLabel16.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel16.setForeground(Color.black);
    jLabel16.setText("Coord Frame");
    jLabel17.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel17.setForeground(Color.black);
    jLabel17.setText("Position Angle");
    system.setFont(new java.awt.Font("Dialog", 0, 12));
    coordFrame.setFont(new java.awt.Font("Dialog", 0, 12));
    this.add(scubaPanel, BorderLayout.CENTER);
    scubaPanel.add(jLabel14, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    scubaPanel.add(jLabel15, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    scubaPanel.add(jLabel16, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    scubaPanel.add(deltaX, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    scubaPanel.add(deltaY, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    scubaPanel.add(coordFrame, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    scubaPanel.add(jLabel17, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
    scubaPanel.add(posAngle, new GridBagConstraints(1, 3, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(scubaAcsisPanel, BorderLayout.WEST);
    scubaAcsisPanel.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    scubaAcsisPanel.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    scubaAcsisPanel.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    scubaAcsisPanel.add(jLabel4, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(20, 5, 0, 0), 0, 0));
    scubaAcsisPanel.add(system, new GridBagConstraints(0, 4, 3, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 40), 0, 0));
    scubaAcsisPanel.add(x, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    scubaAcsisPanel.add(y, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    scubaAcsisPanel.add(theta, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    scubaAcsisPanel.add(jLabel5, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  }
}
