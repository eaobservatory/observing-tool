
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
    JLabel           minLabel1         = new JLabel();
    JLabel           maxLabel1         = new JLabel();
    JLabel           minLabel2         = new JLabel();
    JLabel           maxLabel2         = new JLabel();

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
        moonAny.setFont(new java.awt.Font("Dialog", 0, 12));
        moonAny.setText("Don\'t Care (Any)");
        moonGrey.setFont(new java.awt.Font("Dialog", 0, 12));
        moonGrey.setText("Grey");
        moonDark.setFont(new java.awt.Font("Dialog", 0, 12));
        moonDark.setText("Dark");
        cloudAny.setFont(new java.awt.Font("Dialog", 0, 12));
        cloudAny.setText("Allocated");
        cloudThinCirrus.setFont(new java.awt.Font("Dialog", 0, 12));
        cloudThinCirrus.setText("Thin Cirrus OK");
        cloudPhotometric.setFont(new java.awt.Font("Dialog", 0, 12));
        cloudPhotometric.setText("Photometric");
//         seeingExcellent.setText("Excellent (<= 0.4)");
// 	seeingExcellent.setFont(new java.awt.Font("Dialog", 0, 12));
	this.add(jPanel1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
						 ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
						 new Insets(5, 5, 5, 5), 0, 0));
//         jPanel1.add(seeingGood, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
// 						       ,GridBagConstraints.WEST, GridBagConstraints.BOTH, 
// 						       new Insets(0, 0, 0, 0), 0, 0));
//         jPanel1.add(seeingPoor, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
// 						       ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
// 						       new Insets(0, 0, 0, 0), 0, 0));
//         jPanel1.add(seeingAny, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
// 						      ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
// 						      new Insets(0, 0, 0, 0), 0, 0));
// 	jPanel1.add(seeingExcellent, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
// 							    ,GridBagConstraints.CENTER, GridBagConstraints.NONE,
// 							    new Insets(0, 0, 0, 0), 0, 0));
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

        this.add(jPanel2, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
						 ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
						 new Insets(5, 5, 5, 5), 0, 0));
//         jPanel2.add(csoAny, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
// 						   ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
// 						   new Insets(0, 0, 0, 0), 0, 0));
//         jPanel2.add(csoVeryDry, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
// 						       ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
// 						       new Insets(0, 0, 0, 0), 0, 0));
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
        this.add(jPanel3, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
						 ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
						 new Insets(5, 5, 5, 5), 0, 0));
        jPanel3.add(moonAny, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
						    ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
						    new Insets(0, 0, 0, 0), 0, 0));
        jPanel3.add(moonGrey, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
						     ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
						     new Insets(0, 0, 0, 0), 0, 0));
        jPanel3.add(moonDark, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
						     ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
						     new Insets(0, 0, 0, 0), 0, 0));
        this.add(jPanel4, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0
						 ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
						 new Insets(5, 5, 5, 5), 0, 0));
        jPanel4.add(cloudAny, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
						     ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
						     new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(cloudThinCirrus, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
							    ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
							    new Insets(0, 0, 0, 0), 0, 0));
        jPanel4.add(cloudPhotometric, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
							     ,GridBagConstraints.WEST, GridBagConstraints.NONE,
							     new Insets(0, 0, 0, 0), 0, 0));
    }
}
