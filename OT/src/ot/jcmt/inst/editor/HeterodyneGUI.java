/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.inst.editor;

import javax.swing.border.TitledBorder;
import jsky.app.ot.gui.TableWidgetExt;
import java.awt.*;
import javax.swing.*;

/**
 * Panel containing frequency editor and data reduction editor.
 *
 * @author M.Folger@roe.ac.uk
 */
public class HeterodyneGUI extends JPanel {
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JButton editButton = new JButton();
  FlowLayout flowLayout1 = new FlowLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextPane summaryTextPane = new JTextPane();

  public HeterodyneGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    editButton.setText("Edit");
    jPanel1.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.RIGHT);
    this.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(editButton, null);
    this.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(summaryTextPane, null);
  }

}
