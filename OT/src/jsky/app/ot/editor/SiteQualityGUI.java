
/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package jsky.app.ot.editor;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import jsky.app.ot.gui.OptionWidgetExt;

public class SiteQualityGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JPanel jPanel4 = new JPanel();
    TitledBorder titledBorder1;
    Border border1;
    TitledBorder titledBorder2;
    TitledBorder titledBorder3;
    TitledBorder titledBorder4;
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    GridBagLayout gridBagLayout4 = new GridBagLayout();
    GridBagLayout gridBagLayout5 = new GridBagLayout();
    OptionWidgetExt iq20 = new OptionWidgetExt();
    OptionWidgetExt iq50 = new OptionWidgetExt();
    OptionWidgetExt iqIgnore = new OptionWidgetExt();
    OptionWidgetExt irIgnore = new OptionWidgetExt();
    OptionWidgetExt ir50 = new OptionWidgetExt();
    OptionWidgetExt ir20 = new OptionWidgetExt();
    OptionWidgetExt moonIgnore = new OptionWidgetExt();
    OptionWidgetExt moonBright = new OptionWidgetExt();
    OptionWidgetExt moonDark = new OptionWidgetExt();
    OptionWidgetExt skyIgnore = new OptionWidgetExt();
    OptionWidgetExt skySpectroscopic = new OptionWidgetExt();
    OptionWidgetExt skyPhotometric = new OptionWidgetExt();

    // Widgets for OMP mode (added by MFO, 8 August 2001)
    GridBagLayout gridBagLayout1omp = new GridBagLayout();
    JPanel jPanel1omp = new JPanel();
    JPanel jPanel2omp = new JPanel();
    TitledBorder titledBorder1omp;
    Border border1omp;
    TitledBorder titledBorder2omp;
    TitledBorder titledBorder3omp;
    TitledBorder titledBorder4omp;
    GridBagLayout gridBagLayout2omp = new GridBagLayout();
    GridBagLayout gridBagLayout3omp = new GridBagLayout();
    OptionWidgetExt tauBand1 = new OptionWidgetExt();
    OptionWidgetExt tauBand2 = new OptionWidgetExt();
    OptionWidgetExt seeing1 = new OptionWidgetExt();
    OptionWidgetExt seeing2 = new OptionWidgetExt();
    OptionWidgetExt seeing3 = new OptionWidgetExt();


    public SiteQualityGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
      // OMP (MFO, 8 August 2001)
      // Layout GUI depending on whether in OMP mode or not.
      if(System.getProperty("OMP") != null) {
        titledBorder1omp = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"\u03C4 Band");
        border1omp = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142));
        titledBorder2omp = new TitledBorder(border1omp,"Seeing");
        this.setMinimumSize(new Dimension(279, 276));
        this.setPreferredSize(new Dimension(279, 276));
        this.setLayout(gridBagLayout1omp);
        jPanel1omp.setBorder(titledBorder1omp);
        jPanel1omp.setLayout(gridBagLayout2omp);
        jPanel2omp.setBorder(titledBorder2omp);
        jPanel2omp.setLayout(gridBagLayout3omp);
        tauBand1.setText("low  (\u03C4 < 0.09)");
        tauBand1.setFont(new java.awt.Font("Monospaced", 0, 12));
        tauBand2.setText("high (\u03C4 > 0.09)");
        tauBand2.setFont(new java.awt.Font("Monospaced", 0, 12));
        seeing1.setText("1 (< 0.4)");
        seeing1.setFont(new java.awt.Font("Monospaced", 0, 12));
        seeing2.setText("2 (0.4 .. 0.8)");
        seeing2.setFont(new java.awt.Font("Monospaced", 0, 12));
        seeing3.setText("3 (> 0.8)");
        seeing3.setFont(new java.awt.Font("Monospaced", 0, 12));

        this.add(jPanel1omp, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanel1omp.add(tauBand1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1omp.add(tauBand2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(jPanel2omp, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanel2omp.add(seeing1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel2omp.add(seeing2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel2omp.add(seeing3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      }
      else {
        titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"Image Quality");
        border1 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142));
        titledBorder2 = new TitledBorder(border1,"IR Background");
        titledBorder3 = new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Moon");
        titledBorder4 = new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Sky");
        this.setMinimumSize(new Dimension(279, 276));
        this.setPreferredSize(new Dimension(279, 276));
        this.setLayout(gridBagLayout1);
        jPanel1.setBorder(titledBorder1);
        jPanel1.setLayout(gridBagLayout2);
        jPanel2.setBorder(titledBorder2);
        jPanel2.setLayout(gridBagLayout3);
        jPanel3.setBorder(titledBorder3);
        jPanel3.setLayout(gridBagLayout4);
        jPanel4.setBorder(titledBorder4);
        jPanel4.setLayout(gridBagLayout5);
        iq20.setText("20%");
        iq20.setFont(new java.awt.Font("Dialog", 0, 12));
        iq50.setText("50%");
        iq50.setFont(new java.awt.Font("Dialog", 0, 12));
        iqIgnore.setText("Don\'t Care");
        iqIgnore.setFont(new java.awt.Font("Dialog", 0, 12));
        irIgnore.setFont(new java.awt.Font("Dialog", 0, 12));
        irIgnore.setText("Don\'t Care");
        ir50.setFont(new java.awt.Font("Dialog", 0, 12));
        ir50.setText("50%");
        ir20.setFont(new java.awt.Font("Dialog", 0, 12));
        ir20.setText("20%");
        moonIgnore.setFont(new java.awt.Font("Dialog", 0, 12));
        moonIgnore.setText("Don\'t Care");
        moonBright.setFont(new java.awt.Font("Dialog", 0, 12));
        moonBright.setText("Bright");
        moonDark.setFont(new java.awt.Font("Dialog", 0, 12));
        moonDark.setText("Dark");
        skyIgnore.setFont(new java.awt.Font("Dialog", 0, 12));
        skyIgnore.setText("Don\'t Care");
        skySpectroscopic.setFont(new java.awt.Font("Dialog", 0, 12));
        skySpectroscopic.setText("Spectroscopic");
        skyPhotometric.setFont(new java.awt.Font("Dialog", 0, 12));
        skyPhotometric.setText("Photometric");
        this.add(jPanel1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanel1.add(iq20, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(iq50, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(iqIgnore, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(jPanel2, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanel2.add(irIgnore, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel2.add(ir50, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel2.add(ir20, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(jPanel3, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanel3.add(moonIgnore, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel3.add(moonBright, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel3.add(moonDark, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(jPanel4, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        jPanel4.add(skyIgnore, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(skySpectroscopic, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(skyPhotometric, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      }
    }
}
