
/**
 * Title:        Acsis JBuilder GUIs<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */
package ot.jcmt.iter.editor;

import java.awt.*;
import javax.swing.*;
import jsky.app.ot.gui.*;
import javax.swing.border.*;

public class IterJCMTGenericGUI extends JPanel {

  public static final String NOD       = "Nod";
  public static final String CHOP      = "Chop";
  public static final String FREQUENCY = "Frequency";
  public static final String NONE      = "None";

  TextBoxWidgetExt secsPerCycle = new TextBoxWidgetExt();
  TextBoxWidgetExt noOfCycles = new TextBoxWidgetExt();
  TextBoxWidgetExt stepSize = new TextBoxWidgetExt();
  TextBoxWidgetExt jigglesPerCycle = new TextBoxWidgetExt();
  TextBoxWidgetExt sampleTime = new TextBoxWidgetExt();
  CheckBoxWidgetExt cycleReversal = new CheckBoxWidgetExt();
  CheckBoxWidgetExt jiggleAtReference = new CheckBoxWidgetExt();
  CheckBoxWidgetExt automaticTarget = new CheckBoxWidgetExt();

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  DropDownListBoxWidgetExt noOfIntegrations = new DropDownListBoxWidgetExt();
  JLabel switchingModeLabel = new JLabel();
  DropDownListBoxWidgetExt switchingMode = new DropDownListBoxWidgetExt();
  JLabel jLabel4 = new JLabel();
  TitledBorder titledBorder1;
  Border border1;
  TextBoxWidgetExt frequencyOffset_throw = new TextBoxWidgetExt();
  JPanel frequencyPanel = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JLabel jLabel11 = new JLabel();
  JLabel jLabel10 = new JLabel();
  TextBoxWidgetExt frequencyOffset_rate = new TextBoxWidgetExt();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel8 = new JLabel();

  JLabel noiseLabel = new JLabel();
  TextBoxWidgetExt noiseTextBox = new TextBoxWidgetExt();

  public IterJCMTGenericGUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    border1 = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(134, 134, 134),new Color(93, 93, 93)),BorderFactory.createEmptyBorder(5,0,0,0));
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setForeground(Color.black);
    jLabel1.setText("No of Integrations");
    this.setLayout(borderLayout1);
    jPanel1.setLayout(gridBagLayout1);
    switchingModeLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    switchingModeLabel.setForeground(Color.black);
    switchingModeLabel.setText("Switching Mode");
    jLabel4.setText("jLabel4");
    this.setBorder(border1);
    switchingMode.setFont(new java.awt.Font("Dialog", 0, 12));
    noOfIntegrations.setFont(new java.awt.Font("Dialog", 0, 12));
    frequencyOffset_throw.setColumns(10);
    frequencyPanel.setLayout(gridBagLayout3);
    frequencyPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153),2),"Frequency Offset"));
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setForeground(Color.black);
    jLabel11.setText("(Hz)");
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setForeground(Color.black);
    jLabel10.setText("(MHz)");
    frequencyOffset_rate.setColumns(10);
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel9.setForeground(Color.black);
    jLabel9.setText("Rate");
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel8.setForeground(Color.black);
    jLabel8.setText("Throw");

    noiseLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    noiseLabel.setForeground(Color.black);
    noiseLabel.setText("Noise");
    noiseTextBox.setEditable(false);
    noiseTextBox.setColumns(15);
    
    this.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(jLabel1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(15, 5, 0, 0), 0, 0));
    jPanel1.add(noOfIntegrations, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    jPanel1.add(switchingModeLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    jPanel1.add(switchingMode, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 0, 0));
    jPanel1.add(frequencyPanel, new GridBagConstraints(1, 0, 2, 3, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    jPanel1.add(noiseLabel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 1, 0, 0), 0, 0));
    jPanel1.add(noiseTextBox, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
    frequencyPanel.add(jLabel8, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    frequencyPanel.add(frequencyOffset_throw, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
    frequencyPanel.add(jLabel9, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    frequencyPanel.add(frequencyOffset_rate, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
    frequencyPanel.add(jLabel10, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    frequencyPanel.add(jLabel11, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

  }
}
