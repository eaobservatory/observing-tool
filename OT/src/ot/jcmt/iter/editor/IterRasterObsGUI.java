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

//  public static final String SCUBA      = "Scuba";
//  public static final String HETERODYNE = "Heterodyne";

  JPanel scubaAcsisPanel = new JPanel();
  JPanel areaPanel = new JPanel();
  JPanel scanPanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel scanSystemLabel = new JLabel();
  DropDownListBoxWidgetExt scanSystem = new DropDownListBoxWidgetExt();
  TextBoxWidgetExt dx = new TextBoxWidgetExt();
  TextBoxWidgetExt dy = new TextBoxWidgetExt();
  TextBoxWidgetExt width = new TextBoxWidgetExt();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel18 = new JLabel();
  JLabel jLabel19 = new JLabel();
  TextBoxWidgetExt height = new TextBoxWidgetExt();
  JLabel jLabel20 = new JLabel();
  JLabel jLabel21 = new JLabel();
  TextBoxWidgetExt posAngle = new TextBoxWidgetExt();
  JLabel jLabel23 = new JLabel();
  Border border1;
  JLabel jLabel14 = new JLabel();
  DropDownListBoxWidgetExt sampleTime = new DropDownListBoxWidgetExt();
  TextBoxWidgetExt secsPerObservation = new TextBoxWidgetExt();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TextBoxWidgetExt secsPerRow = new TextBoxWidgetExt();
  JLabel jLabel13 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JLabel jLabel11 = new JLabel();
  JLabel jLabel10 = new JLabel();
  JPanel heterodynePanel = new JPanel();
  OptionWidgetExt alongRow = new OptionWidgetExt();
  TextBoxWidgetExt secsBetweenCals = new TextBoxWidgetExt();
  CommandButtonWidgetExt defaultButton = new CommandButtonWidgetExt();
  OptionWidgetExt interleaved = new OptionWidgetExt();
  TextBoxWidgetExt rowsPerCal = new TextBoxWidgetExt();
  CheckBoxWidgetExt rowReversal = new CheckBoxWidgetExt();
  TextBoxWidgetExt secsBetweenRefs = new TextBoxWidgetExt();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel6 = new JLabel();
  TextBoxWidgetExt rowsPerRef = new TextBoxWidgetExt();
  JLabel jLabel16 = new JLabel();
  DropDownListBoxWidgetExt scanAngle = new DropDownListBoxWidgetExt();
  JLabel jLabel17 = new JLabel();

  public IterRasterObsGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    border1 = BorderFactory.createLineBorder(new Color(153, 153, 153),2);
    scubaAcsisPanel.setLayout(new BorderLayout());
    areaPanel.setLayout(gridBagLayout2);
    scanPanel.setLayout(gridBagLayout3);
    areaPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Area"));
    scanPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Scan"));
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setForeground(Color.black);
    jLabel1.setText("Sample Spacing");
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setForeground(Color.black);
    jLabel2.setText("Scan Spacing");
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setForeground(Color.black);
    jLabel3.setText("Width");
    scanSystemLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    scanSystemLabel.setForeground(Color.black);
    scanSystemLabel.setText("System");
    jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel5.setForeground(Color.black);
    jLabel5.setText("(arcsecs)");
    scanSystem.setFont(new java.awt.Font("Dialog", 0, 12));
    sampleTime.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel18.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel18.setForeground(Color.black);
    jLabel18.setText("(arcsecs)");
    jLabel19.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel19.setForeground(Color.black);
    jLabel19.setText("(arcsecs)");
    jLabel20.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel20.setForeground(Color.black);
    jLabel20.setText("(arcsecs)");
    jLabel21.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel21.setForeground(Color.black);
    jLabel21.setText("Height");
    posAngle.setColumns(5);
    jLabel23.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel23.setForeground(Color.black);
    jLabel23.setText("(degrees)");
    jLabel14.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel14.setForeground(Color.black);
    jLabel14.setText("PA");
    secsPerObservation.setEditable(false);
    secsPerRow.setEditable(false);
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel13.setForeground(Color.black);
    jLabel13.setText("Secs/Observation");
    jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel12.setForeground(Color.black);
    jLabel12.setText("Secs between Cals");
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setForeground(Color.black);
    jLabel11.setText("Secs between Refs");
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setForeground(Color.black);
    jLabel10.setText("Secs/Row");
    heterodynePanel.setLayout(gridBagLayout1);
    heterodynePanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Heterodyne Details"));
    alongRow.setText("Along Row");
    alongRow.setFont(new java.awt.Font("Dialog", 0, 12));
    secsBetweenCals.setEditable(false);
    defaultButton.setText("Default");
    interleaved.setText("Interleafed");
    interleaved.setFont(new java.awt.Font("Dialog", 0, 12));
    rowReversal.setText("Row Reversal");
    rowReversal.setFont(new java.awt.Font("Dialog", 0, 12));
    secsBetweenRefs.setEditable(false);
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel9.setForeground(Color.black);
    jLabel9.setText("(sec)");
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel8.setForeground(Color.black);
    jLabel8.setText("Sample Time");
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel7.setForeground(Color.black);
    jLabel7.setText("Rows/Ref");
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel6.setForeground(Color.black);
    jLabel6.setText("Rows/Cal");
    jLabel16.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel16.setForeground(Color.black);
    jLabel16.setText("PA");
    jLabel17.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel17.setForeground(Color.black);
    jLabel17.setText("(degrees)");
    scanAngle.setFont(new java.awt.Font("Dialog", 0, 12));
    this.add(scubaAcsisPanel, BorderLayout.WEST);
    scubaAcsisPanel.add(areaPanel, BorderLayout.CENTER);
    scubaAcsisPanel.add(scanPanel, BorderLayout.SOUTH);
    scanPanel.add(scanSystemLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    scanPanel.add(scanSystem, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
    scanPanel.add(jLabel16, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 0, 0));
    scanPanel.add(scanAngle, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    scanPanel.add(jLabel17, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    areaPanel.add(jLabel1, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
    areaPanel.add(jLabel2, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
    areaPanel.add(jLabel3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    areaPanel.add(dx, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    areaPanel.add(dy, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    areaPanel.add(width, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    areaPanel.add(jLabel5, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    areaPanel.add(jLabel18, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    areaPanel.add(jLabel19, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    areaPanel.add(height, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    areaPanel.add(jLabel20, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    areaPanel.add(jLabel21, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    areaPanel.add(posAngle, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    areaPanel.add(jLabel23, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    areaPanel.add(jLabel14, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    this.add(heterodynePanel, BorderLayout.CENTER);
//     heterodynePanel.add(alongRow, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
//             ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(10, 5, 0, 5), 0, 0));
//     heterodynePanel.add(interleaved, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
//             ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 10, 5), 0, 0));
//     heterodynePanel.add(rowReversal, new GridBagConstraints(1, 0, 2, 2, 0.0, 0.0
//             ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
    heterodynePanel.add(jLabel6, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    heterodynePanel.add(jLabel7, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    heterodynePanel.add(rowsPerCal, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    heterodynePanel.add(rowsPerRef, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    heterodynePanel.add(jLabel8, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    heterodynePanel.add(sampleTime, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    heterodynePanel.add(jLabel9, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    heterodynePanel.add(defaultButton, new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    heterodynePanel.add(jLabel10, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    heterodynePanel.add(jLabel11, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    heterodynePanel.add(jLabel12, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    heterodynePanel.add(jLabel13, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    heterodynePanel.add(secsPerRow, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    heterodynePanel.add(secsBetweenRefs, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    heterodynePanel.add(secsBetweenCals, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    heterodynePanel.add(secsPerObservation, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  }
}
