
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
import jsky.app.ot.gui.*;

public class SiteQualityGUI extends JPanel {
    GridLayout gridLayout1 = new GridLayout(3,2);
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JPanel jPanel4 = new JPanel();
    JPanel jPanel5 = new JPanel();
    JPanel jPanel6 = new JPanel();
    TitledBorder titledBorder1;
    Border border1;
    TitledBorder titledBorder2;
    TitledBorder titledBorder3;
    TitledBorder titledBorder4;
    TitledBorder titledBorder5;
    TitledBorder titledBorder6;
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    GridBagLayout gridBagLayout4 = new GridBagLayout();
    GridBagLayout gridBagLayout5 = new GridBagLayout();
    GridBagLayout gridBagLayout6 = new GridBagLayout();
//     OptionWidgetExt seeingGood = new OptionWidgetExt();
//     OptionWidgetExt seeingPoor = new OptionWidgetExt();
//     OptionWidgetExt seeingAny = new OptionWidgetExt();
//     OptionWidgetExt csoAny = new OptionWidgetExt();
//     OptionWidgetExt csoVeryDry = new OptionWidgetExt();
    OptionWidgetExt moonAny = new OptionWidgetExt();
    OptionWidgetExt moonGrey = new OptionWidgetExt();
    OptionWidgetExt moonDark = new OptionWidgetExt();
    OptionWidgetExt cloudAny = new OptionWidgetExt();
    OptionWidgetExt cloudThinCirrus = new OptionWidgetExt();
    OptionWidgetExt cloudPhotometric = new OptionWidgetExt();
//     OptionWidgetExt seeingExcellent = new OptionWidgetExt();
    OptionWidgetExt  tauBandAllocated = new OptionWidgetExt();
    OptionWidgetExt  tauBandUserDefined = new OptionWidgetExt();
    TextBoxWidgetExt minTau           = new TextBoxWidgetExt();
    TextBoxWidgetExt maxTau           = new TextBoxWidgetExt();
    OptionWidgetExt  seeingAllocated  = new OptionWidgetExt();
    OptionWidgetExt  seeingUserDefined = new OptionWidgetExt();
    TextBoxWidgetExt minSeeing        = new TextBoxWidgetExt();
    TextBoxWidgetExt maxSeeing        = new TextBoxWidgetExt();
    OptionWidgetExt  skyAllocated  = new OptionWidgetExt();
    OptionWidgetExt  skyUserDefined = new OptionWidgetExt();
    TextBoxWidgetExt minSky        = new TextBoxWidgetExt();
    TextBoxWidgetExt maxSky        = new TextBoxWidgetExt();
    JLabel           minLabel1         = new JLabel();
    JLabel           maxLabel1         = new JLabel();
    JLabel           minLabel2         = new JLabel();
    JLabel           maxLabel2         = new JLabel();
    JLabel           minLabel3         = new JLabel();
    JLabel           maxLabel3         = new JLabel();
    JLabel           blankLabel        = new JLabel();

    public SiteQualityGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"Seeing");
        border1 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142));
        titledBorder2 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"CSO tau");
        titledBorder3 = new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Moon");
        titledBorder4 = new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Cloud");
        titledBorder5 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"J-Band Sky Brightness");
        titledBorder6 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"Reserved");
        this.setMinimumSize(new Dimension(279, 276));
        this.setPreferredSize(new Dimension(279, 276));
        this.setLayout(gridLayout1);
        jPanel1.setBorder(titledBorder1);
        jPanel1.setLayout(gridBagLayout1);
        jPanel2.setBorder(titledBorder2);
        jPanel2.setLayout(gridBagLayout2);
        jPanel3.setBorder(titledBorder3);
        jPanel3.setLayout(gridBagLayout3);
        jPanel4.setBorder(titledBorder4);
        jPanel4.setLayout(gridBagLayout4);
        jPanel5.setBorder(titledBorder5);
        jPanel5.setLayout(gridBagLayout5);
        jPanel6.setBorder(titledBorder6);
        jPanel6.setLayout(gridBagLayout6);

	minLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
	minLabel1.setForeground(Color.black);
	minLabel1.setText("Min ");
	maxLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
	maxLabel1.setForeground(Color.black);
	maxLabel1.setText("Max ");

	minLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
	minLabel2.setForeground(Color.black);
	minLabel2.setText("Min ");
	maxLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
	maxLabel2.setForeground(Color.black);
        maxLabel2.setText("Max ");
        
	minLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
	minLabel3.setForeground(Color.black);
	minLabel3.setText("Min ");
	maxLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
	maxLabel3.setForeground(Color.black);
	maxLabel3.setText("Max ");

	blankLabel.setFont(new java.awt.Font("Dialog", 2, 12));
	blankLabel.setForeground(Color.black);
	blankLabel.setText("This box is intentionally left blank");
//         seeingGood.setText("Good (<= 0.6)");
//         seeingGood.setFont(new java.awt.Font("Dialog", 0, 12));
//         seeingPoor.setText("Poor (<= 0.8)");
//         seeingPoor.setFont(new java.awt.Font("Dialog", 0, 12));
//         seeingAny.setText("Don\'t Care (any)");
//         seeingAny.setFont(new java.awt.Font("Dialog", 0, 12));
        seeingAllocated.setText("Allocated");
        seeingAllocated.setFont(new java.awt.Font("Dialog", 0, 12));
	seeingUserDefined.setText("User Defined");
        seeingUserDefined.setFont(new java.awt.Font("Dialog", 0, 12));
//         csoAny.setFont(new java.awt.Font("Dialog", 0, 12));
//         csoAny.setText("Don\'t Care (any)");
//         csoVeryDry.setFont(new java.awt.Font("Dialog", 0, 12));
//         csoVeryDry.setText("Very Dry (<= 0.09)");
        tauBandAllocated.setText("Allocated");
        tauBandAllocated.setFont(new java.awt.Font("Dialog", 0, 12));
	tauBandUserDefined.setText("User Defined");
        tauBandUserDefined.setFont(new java.awt.Font("Dialog", 0, 12));
        skyAllocated.setText("Allocated");
        skyAllocated.setFont(new java.awt.Font("Dialog", 0, 12));
	skyUserDefined.setText("User Defined");
        skyUserDefined.setFont(new java.awt.Font("Dialog", 0, 12));
        moonAny.setFont(new java.awt.Font("Dialog", 0, 12));
        moonAny.setText("Don\'t Care (Any)");
        moonGrey.setFont(new java.awt.Font("Dialog", 0, 12));
        moonGrey.setText("Grey (<25% illuminated)");
        moonDark.setFont(new java.awt.Font("Dialog", 0, 12));
        moonDark.setText("Dark (moon not up)");
        cloudAny.setFont(new java.awt.Font("Dialog", 0, 12));
        cloudAny.setText("Allocated");
        cloudThinCirrus.setFont(new java.awt.Font("Dialog", 0, 12));
        cloudThinCirrus.setText("Thin Cirrus (<20% attenuation varaibility)");
        cloudPhotometric.setFont(new java.awt.Font("Dialog", 0, 12));
        cloudPhotometric.setText("Photometric (no attenuation variability)");
//         seeingExcellent.setText("Excellent (<= 0.4)");
// 	seeingExcellent.setFont(new java.awt.Font("Dialog", 0, 12));
	jPanel1.add(seeingAllocated, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	jPanel1.add(seeingUserDefined, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	jPanel1.add(minLabel1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	jPanel1.add(maxLabel1, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	jPanel1.add(minSeeing, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	jPanel1.add(maxSeeing, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

	jPanel2.add(tauBandAllocated, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	jPanel2.add(tauBandUserDefined, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	jPanel2.add(minLabel2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	jPanel2.add(maxLabel2, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	jPanel2.add(minTau, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	jPanel2.add(maxTau, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        
        jPanel3.add(moonAny, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
						    ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
						    new Insets(0, 0, 0, 0), 0, 0));
        jPanel3.add(moonGrey, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
						     ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
						     new Insets(0, 0, 0, 0), 0, 0));
        jPanel3.add(moonDark, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
						     ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
						     new Insets(0, 0, 0, 0), 0, 0));

        jPanel4.add(cloudAny, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
						     ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
						     new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(cloudThinCirrus, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
							    ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
							    new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(cloudPhotometric, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
							     ,GridBagConstraints.WEST, GridBagConstraints.NONE,
							     new Insets(0, 0, 0, 0), 0, 0));

	jPanel5.add(skyAllocated, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	jPanel5.add(skyUserDefined, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	jPanel5.add(minLabel3, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	jPanel5.add(maxLabel3, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	jPanel5.add(minSky, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	jPanel5.add(maxSky, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        
	jPanel6.add(blankLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        this.add(jPanel1);
        this.add(jPanel2);
        this.add(jPanel3);
        this.add(jPanel4);
        this.add(jPanel5);
        this.add(jPanel6);
    }
}
