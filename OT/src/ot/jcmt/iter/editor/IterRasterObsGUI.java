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


public class IterRasterObsGUI extends IterJCMTGenericGUI {

  public static final String SCUBA      = "Scuba";
  public static final String HETERODYNE = "Heterodyne";

  JPanel cardPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel acsisPanel = new JPanel();
  CardLayout cardLayout1 = new CardLayout();
  JPanel scubaAcsisPanel = new JPanel();
  JPanel scubaPanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  TitledBorder titledBorder1;
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  DropDownListBoxWidgetExt dropDownListBoxWidgetExt1 = new DropDownListBoxWidgetExt();
  TextBoxWidgetExt textBoxWidgetExt1 = new TextBoxWidgetExt();
  TextBoxWidgetExt textBoxWidgetExt2 = new TextBoxWidgetExt();
  TextBoxWidgetExt textBoxWidgetExt3 = new TextBoxWidgetExt();
  JLabel jLabel5 = new JLabel();
  OptionWidgetExt alongRow = new OptionWidgetExt();
  OptionWidgetExt interleafed = new OptionWidgetExt();
  CheckBoxWidgetExt rowReversal = new CheckBoxWidgetExt();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel7 = new JLabel();
  TextBoxWidgetExt rowsPerCal = new TextBoxWidgetExt();
  TextBoxWidgetExt rowsPerRef = new TextBoxWidgetExt();
  JLabel jLabel8 = new JLabel();
  JTextField sampleTime = new JTextField();
  JLabel jLabel9 = new JLabel();
  CommandButtonWidgetExt defaultButton = new CommandButtonWidgetExt();
  JLabel jLabel10 = new JLabel();
  JLabel jLabel11 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JLabel jLabel13 = new JLabel();
  TextBoxWidgetExt secsPerRow = new TextBoxWidgetExt();
  TextBoxWidgetExt secsBetweenRefs = new TextBoxWidgetExt();
  TextBoxWidgetExt secsBetweenCals = new TextBoxWidgetExt();
  TextBoxWidgetExt secsPerObservation = new TextBoxWidgetExt();
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

  public IterRasterObsGUI() {
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
    acsisPanel.setLayout(gridBagLayout1);
    cardPanel.setLayout(cardLayout1);
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
    alongRow.setText("Along Row");
    alongRow.setFont(new java.awt.Font("Dialog", 0, 12));
    interleafed.setText("Interleafed");
    interleafed.setFont(new java.awt.Font("Dialog", 0, 12));
    rowReversal.setText("Row Reversal");
    rowReversal.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel6.setForeground(Color.black);
    jLabel6.setText("Rows/Col");
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel7.setForeground(Color.black);
    jLabel7.setText("Rows/Ref");
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel8.setForeground(Color.black);
    jLabel8.setText("Sampe Time");
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel9.setForeground(Color.black);
    jLabel9.setText("(msec)");
    defaultButton.setText("Default");
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setForeground(Color.black);
    jLabel10.setText("Secs/Row");
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setForeground(Color.black);
    jLabel11.setText("Secs between Refs");
    jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel12.setForeground(Color.black);
    jLabel12.setText("Secs between Cals");
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel13.setForeground(Color.black);
    jLabel13.setText("Secs/Observation");
    secsPerRow.setEditable(false);
    secsBetweenRefs.setEditable(false);
    secsBetweenCals.setEditable(false);
    secsPerObservation.setEditable(false);
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
    this.add(cardPanel, BorderLayout.CENTER);
    cardPanel.add(acsisPanel, HETERODYNE);
    acsisPanel.add(alongRow, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(10, 5, 0, 5), 0, 0));
    acsisPanel.add(interleafed, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 10, 5), 0, 0));
    acsisPanel.add(rowReversal, new GridBagConstraints(1, 0, 2, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
    acsisPanel.add(jLabel6, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    acsisPanel.add(jLabel7, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    acsisPanel.add(rowsPerCal, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(rowsPerRef, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(jLabel8, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    acsisPanel.add(sampleTime, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(jLabel9, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    acsisPanel.add(defaultButton, new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(jLabel10, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    acsisPanel.add(jLabel11, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    acsisPanel.add(jLabel12, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    acsisPanel.add(jLabel13, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    acsisPanel.add(secsPerRow, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(secsBetweenRefs, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(secsBetweenCals, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(secsPerObservation, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    cardPanel.add(scubaPanel, SCUBA);
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
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    scubaPanel.add(posAngle, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(scubaAcsisPanel, BorderLayout.WEST);
    scubaAcsisPanel.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    scubaAcsisPanel.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    scubaAcsisPanel.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    scubaAcsisPanel.add(jLabel4, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(20, 5, 0, 0), 0, 0));
    scubaAcsisPanel.add(dropDownListBoxWidgetExt1, new GridBagConstraints(0, 4, 3, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    scubaAcsisPanel.add(textBoxWidgetExt1, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    scubaAcsisPanel.add(textBoxWidgetExt2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    scubaAcsisPanel.add(textBoxWidgetExt3, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    scubaAcsisPanel.add(jLabel5, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  }
}
