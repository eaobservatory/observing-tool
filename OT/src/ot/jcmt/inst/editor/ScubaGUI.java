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

import java.awt.*;
import javax.swing.*;
import jsky.app.ot.gui.*;

/**
 * @author Martin Folger ( M.Folger@roe.ac.uk )
 */
public class ScubaGUI extends JPanel {
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  GridLayout gridLayout2 = new GridLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  ListBoxWidgetExt filterList = new ListBoxWidgetExt();
  ListBoxWidgetExt subInstList = new ListBoxWidgetExt();
  JScrollPane bolometerScrollPane = new JScrollPane();
  ListBoxWidgetExt bolometerList = new ListBoxWidgetExt();
  CheckBoxWidgetExt explicitBolometer = new CheckBoxWidgetExt();

  public ScubaGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    jPanel1.setLayout(gridLayout1);
    gridLayout1.setColumns(3);
    gridLayout1.setHgap(5);
    jPanel2.setLayout(gridLayout2);
    gridLayout2.setColumns(3);
    gridLayout2.setHgap(5);
    jLabel1.setForeground(Color.black);
    jLabel1.setPreferredSize(new Dimension(28, 30));
    jLabel1.setText("Filter");
    jLabel2.setForeground(Color.black);
    jLabel2.setText("Sub-Instrument");
    explicitBolometer.setText("Explicit Pixel");
    this.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(jLabel1, null);
    jPanel1.add(jLabel2, null);
    jPanel1.add(explicitBolometer, null);
    this.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(filterList, null);
    jPanel2.add(subInstList, null);
    jPanel2.add(bolometerScrollPane, null);
    bolometerScrollPane.getViewport().setView(bolometerList);
  }

}
