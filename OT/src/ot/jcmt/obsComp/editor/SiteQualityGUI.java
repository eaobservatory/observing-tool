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
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package ot.jcmt.obsComp.editor;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import jsky.app.ot.gui.*;

public class SiteQualityGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    TitledBorder titledBorder1;
    Border border1;
    TitledBorder titledBorder2;
    TitledBorder titledBorder3;
    TitledBorder titledBorder4;
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    OptionWidgetExt weatherBand1 = new OptionWidgetExt();
    OptionWidgetExt weatherBand2 = new OptionWidgetExt();
    OptionWidgetExt weatherBand3 = new OptionWidgetExt();
    OptionWidgetExt seeing3 = new OptionWidgetExt();
    OptionWidgetExt seeing2 = new OptionWidgetExt();
    OptionWidgetExt seeing1 = new OptionWidgetExt();
  OptionWidgetExt weatherBand4 = new OptionWidgetExt();
  OptionWidgetExt weatherBand5 = new OptionWidgetExt();
  OptionWidgetExt seeing4 = new OptionWidgetExt();

    public SiteQualityGUI() {
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
        jPanel1.setBorder(titledBorder1);
        jPanel1.setLayout(gridBagLayout2);
        jPanel2.setBorder(titledBorder2);
        jPanel2.setLayout(gridBagLayout3);
        weatherBand1.setText("1 (cso \u03C4 < 0.05)");
        weatherBand1.setFont(new java.awt.Font("Dialog", 0, 12));
        weatherBand2.setText("2 (0.05 < \u03C4 < 0.08)");
        weatherBand2.setFont(new java.awt.Font("Dialog", 0, 12));
        weatherBand3.setText("3 (0.08 < \u03C4 < 0.12)");
        weatherBand3.setFont(new java.awt.Font("Dialog", 0, 12));
        seeing3.setFont(new java.awt.Font("Dialog", 0, 12));
        seeing3.setText("1.0-3.0");
        seeing2.setFont(new java.awt.Font("Dialog", 0, 12));
        seeing2.setText("0.3 - 1.0");
        seeing1.setFont(new java.awt.Font("Dialog", 0, 12));
        seeing1.setText("< 0.3");
        weatherBand4.setFont(new java.awt.Font("Dialog", 0, 12));
    weatherBand4.setText("4 (0.12 < \u03C4 < 0.2)");
    weatherBand5.setFont(new java.awt.Font("Dialog", 0, 12));
    weatherBand5.setText("5 (\u03C4 > 0.2)");
    seeing4.setText("> 3");
    seeing4.setFont(new java.awt.Font("Dialog", 0, 12));
    this.add(jPanel1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanel1.add(weatherBand1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(weatherBand2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(weatherBand3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(weatherBand4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 3), 0, 0));
    jPanel1.add(weatherBand5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(jPanel2, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanel2.add(seeing3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel2.add(seeing2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel2.add(seeing1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel2.add(seeing4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }
}
