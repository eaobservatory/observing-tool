/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
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


public class IterNoiseObsGUI extends IterJCMTGenericGUI {
  JPanel noisePanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  DropDownListBoxWidgetExt noiseSourceComboBox = new DropDownListBoxWidgetExt();

  public IterNoiseObsGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    noisePanel.setLayout(flowLayout1);
    secsPerCycle.setColumns(8);
    noiseSourceComboBox.setFont(new java.awt.Font("Dialog", 0, 12));
    this.add(noisePanel, BorderLayout.CENTER);
    noisePanel.add(noiseSourceComboBox, null);
  }
}
