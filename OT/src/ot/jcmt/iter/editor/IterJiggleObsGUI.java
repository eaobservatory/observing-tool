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


public class IterJiggleObsGUI extends IterJCMTGenericGUI {
  JPanel acsisPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel10 = new JLabel();
  TextBoxWidgetExt secsPerJiggle = new TextBoxWidgetExt();
  TextBoxWidgetExt secsPerObs = new TextBoxWidgetExt();
  CommandButtonWidgetExt defaultButton = new CommandButtonWidgetExt();
  JPanel jPanel1 = new JPanel();
  JLabel jLabel1 = new JLabel();
  DropDownListBoxWidgetExt jigglePattern = new DropDownListBoxWidgetExt();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JPanel jPanel2 = new JPanel();
  TitledBorder titledBorder1;
  JLabel jLabel11 = new JLabel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  TextBoxWidgetExt textBoxWidgetExt1 = new TextBoxWidgetExt();
  JLabel jLabel12 = new JLabel();
  JLabel jLabel13 = new JLabel();
  DropDownListBoxWidgetExt dropDownListBoxWidgetExt1 = new DropDownListBoxWidgetExt();

  public IterJiggleObsGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    acsisPanel.setLayout(gridBagLayout1);
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setForeground(Color.black);
    jLabel2.setText("Step Size");
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setForeground(Color.black);
    jLabel3.setText("Sample Time");
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel4.setForeground(Color.black);
    jLabel4.setText("Jiggles/Cycle");
    jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel5.setForeground(Color.black);
    jLabel5.setText("No of Cycles");
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel6.setForeground(Color.black);
    jLabel6.setText("(arc sec)");
    stepSize.setColumns(6);
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel7.setForeground(Color.black);
    jLabel7.setText("(msec)");
    sampleTime.setColumns(6);
    jigglesPerCycle.setColumns(6);
    noOfCycles.setColumns(6);
    jiggleAtReference.setText("Jiggle at Reference");
    jiggleAtReference.setFont(new java.awt.Font("Dialog", 0, 12));
    cycleReversal.setText("Cycle Reversal");
    cycleReversal.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel8.setForeground(Color.black);
    jLabel8.setText("secs/cycle");
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel9.setForeground(Color.black);
    jLabel9.setText("secs/jiggle");
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setForeground(Color.black);
    jLabel10.setText("secs/obs");
    secsPerJiggle.setEditable(false);
    secsPerJiggle.setColumns(10);
    secsPerCycle.setEditable(false);
    secsPerCycle.setColumns(5);
    defaultButton.setText("Default");
    secsPerObs.setEditable(false);
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setForeground(Color.black);
    jLabel1.setText("Jiggle Pattern");
    jigglePattern.setFont(new java.awt.Font("Dialog", 0, 12));
    jPanel1.setLayout(gridBagLayout2);
    jPanel2.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Map"));
    jPanel2.setLayout(gridBagLayout3);
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setForeground(Color.black);
    jLabel11.setText("PA");
    textBoxWidgetExt1.setColumns(5);
    jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel12.setForeground(Color.black);
    jLabel12.setText("(degrees)");
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel13.setForeground(Color.black);
    jLabel13.setText("System");
    this.add(acsisPanel, BorderLayout.SOUTH);
    acsisPanel.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    acsisPanel.add(stepSize, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(jLabel3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    acsisPanel.add(jLabel4, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    acsisPanel.add(jLabel5, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    acsisPanel.add(jLabel6, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    acsisPanel.add(sampleTime, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(jLabel7, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    acsisPanel.add(jigglesPerCycle, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(noOfCycles, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(jiggleAtReference, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    acsisPanel.add(cycleReversal, new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    acsisPanel.add(jLabel8, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    acsisPanel.add(jLabel9, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    acsisPanel.add(jLabel10, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    acsisPanel.add(secsPerCycle, new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(secsPerJiggle, new GridBagConstraints(4, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(secsPerObs, new GridBagConstraints(4, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(defaultButton, new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    acsisPanel.add(jPanel2, new GridBagConstraints(4, 4, 1, 3, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel2.add(jLabel11, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
    jPanel2.add(textBoxWidgetExt1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 0), 0, 0));
    jPanel2.add(jLabel12, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel2.add(jLabel13, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel2.add(dropDownListBoxWidgetExt1, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
    this.add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    jPanel1.add(jigglePattern, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  }
}
