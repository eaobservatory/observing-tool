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
import java.awt.event.*;

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */


public class IterStareObsGUI extends IterJCMTGenericGUI {
  JPanel acsisPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
    JCheckBox widePhotom = new JCheckBox();

  public IterStareObsGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    acsisPanel.setLayout(gridBagLayout1);
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setForeground(Color.black);
    jLabel1.setText("Secs/Cycle");
//     jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
//     jLabel2.setForeground(Color.black);
//     jLabel2.setText("No of Cycles");
    cycleReversal.setText("Cycle Reversal");
    cycleReversal.setFont(new java.awt.Font("Dialog", 0, 12));
    continuousCal.setText("Continuous Cal");
    continuousCal.setFont(new java.awt.Font("Dialog", 0, 12));

    widePhotom.setText ("Wide Photometry");
    widePhotom.setFont(new java.awt.Font("Dialog", 0, 12));
    widePhotom.setForeground(Color.black);
    this.add(widePhotom, BorderLayout.SOUTH);
    
    this.add(acsisPanel, BorderLayout.CENTER);
    acsisPanel.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    acsisPanel.add(secsPerCycle, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
//     acsisPanel.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
//             ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
//     acsisPanel.add(noOfCycles, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
//             ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
//     acsisPanel.add(cycleReversal, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
//             ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 40), 0, 0));
    acsisPanel.add(continuousCal, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 40), 0, 0));
  }

}
