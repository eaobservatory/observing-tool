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

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */


public class IterFocusObsGUI extends IterJCMTGenericGUI {
  JPanel scubaAcsisPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel3 = new JLabel();
  DropDownListBoxWidgetExt axis = new DropDownListBoxWidgetExt();
  JLabel jLabel4 = new JLabel();
  TextBoxWidgetExt steps = new TextBoxWidgetExt();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  TextBoxWidgetExt focusPoints = new TextBoxWidgetExt();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JPanel acsisPanel = new JPanel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel1 = new JLabel();

  public IterFocusObsGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    scubaAcsisPanel.setLayout(gridBagLayout1);
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setForeground(Color.black);
    jLabel3.setText("Axis");
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel4.setForeground(Color.black);
    jLabel4.setText("Steps");
    steps.setColumns(8);
    jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel5.setForeground(Color.black);
    jLabel5.setText("(mm)");
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel6.setForeground(Color.black);
    jLabel6.setText("No of Focus Points");
    secsPerCycle.setColumns(8);
    acsisPanel.setLayout(gridBagLayout2);
    cycleReversal.setText("Cycle Reversal");
    cycleReversal.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setForeground(Color.black);
    jLabel2.setText("No of Cycles");
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setForeground(Color.black);
    jLabel1.setText("Secs per Cycle");
    axis.setFont(new java.awt.Font("Dialog", 0, 12));
    automaticTarget.setText("Automatically determine Target");
    automaticTarget.setFont(new java.awt.Font("Dialog", 0, 12));
    this.add(scubaAcsisPanel, BorderLayout.CENTER);
    scubaAcsisPanel.add(jLabel3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    scubaAcsisPanel.add(axis, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    scubaAcsisPanel.add(jLabel4, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
    scubaAcsisPanel.add(steps, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    scubaAcsisPanel.add(jLabel5, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    scubaAcsisPanel.add(jLabel6, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    scubaAcsisPanel.add(focusPoints, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    scubaAcsisPanel.add(automaticTarget, new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 0, 0, 0), 0, 0));
    this.add(acsisPanel, BorderLayout.SOUTH);
    acsisPanel.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    acsisPanel.add(secsPerCycle, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    acsisPanel.add(noOfCycles, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    acsisPanel.add(cycleReversal, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 20, 0), 0, 0));
  }
}
