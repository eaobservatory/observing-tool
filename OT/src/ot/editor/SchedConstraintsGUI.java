/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    <p>
 * Company:      <p>
 * @author Martin Folger (M.Folger@roe.ac.uk)
 * @version 1.0
 */
package ot.editor;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import jsky.app.ot.gui.*;

public class SchedConstraintsGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    TitledBorder titledBorder1;
    Border border1;
    TitledBorder titledBorder2;
    TitledBorder titledBorder3;
    TitledBorder titledBorder4;
  TextBoxWidgetExt earliest = new TextBoxWidgetExt();
  TextBoxWidgetExt latest = new TextBoxWidgetExt();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel5 = new JLabel();
  TextBoxWidgetExt minElevation = new TextBoxWidgetExt();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel7 = new JLabel();
  TextBoxWidgetExt period = new TextBoxWidgetExt();
  JLabel jLabel8 = new JLabel();

    public SchedConstraintsGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"Weather Band");
        border1 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142));
        titledBorder2 = new TitledBorder(border1,"Seeing");
        titledBorder3 = new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Moon");
        titledBorder4 = new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Sky");
        this.setMinimumSize(new Dimension(279, 276));
        this.setPreferredSize(new Dimension(279, 276));
        this.setLayout(gridBagLayout1);
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel3.setForeground(Color.black);
    jLabel3.setText("(YYYY-MM-DD[THH:MM:SS])");
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel4.setForeground(Color.black);
    jLabel4.setText("(YYYY-MM-DD[THH:MM:SS])");
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setForeground(Color.black);
    jLabel1.setText("Earliest Schedule Date");
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setForeground(Color.black);
    jLabel2.setText("Latest Schedule Date");
    jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel5.setForeground(Color.black);
    jLabel5.setText("Minimum Elevation");
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel6.setForeground(Color.black);
    jLabel6.setText("(degrees)");
    jLabel7.setForeground(Color.black);
    jLabel7.setText("Reschedule every");
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel8.setForeground(Color.black);
    jLabel8.setText("days");
    this.add(earliest, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(latest, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(jLabel3, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel4, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    this.add(jLabel2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(20, 5, 0, 0), 0, 0));
    this.add(jLabel5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(20, 5, 0, 0), 0, 0));
    this.add(minElevation, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(jLabel6, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    // Periodicity [timj kluge]
    this.add(jLabel7, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(20, 5, 0, 0), 0, 0));
    this.add(period, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(jLabel8, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }
}
