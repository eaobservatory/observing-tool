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
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import jsky.app.ot.gui.*;

/**
 * ACSIS DR GUI.
 *
 * @author Martin Folger ( M.Folger@roe.ac.uk )
 */
public class DataReductionScreen extends JPanel {
  DropDownListBoxWidgetExt recipe = new DropDownListBoxWidgetExt();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  TextBoxWidgetExt truncation = new TextBoxWidgetExt();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  DropDownListBoxWidgetExt baselineRegionUnits = new DropDownListBoxWidgetExt();
  JPanel baselineFitPanel = new JPanel();
//  OptionWidgetExt baselineFittingSine = new OptionWidgetExt();
//  JLabel jLabel5 = new JLabel();
//  TextBoxWidgetExt sinePeriod = new TextBoxWidgetExt();
//  JLabel jLabel6 = new JLabel();
//  TextBoxWidgetExt sinePhase = new TextBoxWidgetExt();
  DropDownListBoxWidgetExt baselineFittingPolynomial = new DropDownListBoxWidgetExt();
  JLabel jLabel7 = new JLabel();
//  TextBoxWidgetExt polynomialOrder = new TextBoxWidgetExt();
  TitledBorder titledBorder1;
  CheckBoxWidgetExt baselineFitting = new CheckBoxWidgetExt();
  DropDownListBoxWidgetExt channelBinning = new DropDownListBoxWidgetExt();
  CheckBoxWidgetExt deSpike = new CheckBoxWidgetExt();
  JLabel jLabel9 = new JLabel();
  DropDownListBoxWidgetExt regriddingMethod = new DropDownListBoxWidgetExt();
  JProgressBar totalMapSize = new JProgressBar();
  JLabel jLabel10 = new JLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  RichTextBoxWidgetExt baselineRegions = new RichTextBoxWidgetExt();
//  JLabel jLabel8 = new JLabel();
//  JScrollPane jScrollPane2 = new JScrollPane();
//  RichTextBoxWidgetExt lineRegions = new RichTextBoxWidgetExt();
  JLabel jLabel11 = new JLabel();
  DropDownListBoxWidgetExt windowType = new DropDownListBoxWidgetExt();
  CommandButtonWidgetExt showSpectralRegionEditor = new CommandButtonWidgetExt();

  // Added by SDW
  JLabel jLabel2a = new JLabel();
  JLabel baselineLabel = new JLabel();
  JRadioButton noBaselineButton = new JRadioButton();
  JRadioButton automaticBaselineButton = new JRadioButton();
  TextBoxWidgetExt automaticBaseline = new TextBoxWidgetExt();
  JRadioButton manualBaselineButton = new JRadioButton();
  JLabel polyLabel = new JLabel();


  //Construct the frame
  public DataReductionScreen() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception  {
    titledBorder1 = new TitledBorder("Baseline Fitting");
    this.setLayout(gridBagLayout1);
    this.setSize(new Dimension(501, 564));
    //this.setTitle("Acsis Data Reduction");
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setForeground(Color.black);
    jLabel1.setText("Recipe");
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setForeground(Color.black);
    jLabel2.setText("Truncation");
    jLabel2a.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2a.setForeground(Color.black);
    jLabel2a.setText("(Channels)");
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setForeground(Color.black);
    jLabel3.setText("Channel Binning");
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel4.setForeground(Color.black);
    jLabel4.setText("Baseline Fit Regions");
    baselineFitPanel.setBorder(titledBorder1);
    baselineFitPanel.setLayout(gridBagLayout2);
//    baselineFittingSine.setFont(new java.awt.Font("Dialog", 0, 12));
//    baselineFittingSine.setText("Sine wave");
//    jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
//    jLabel5.setForeground(Color.black);
//    jLabel5.setText("Period?");
//    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
//    jLabel6.setForeground(Color.black);
//    jLabel6.setText("Phase");
    baselineFittingPolynomial.setFont(new java.awt.Font("Dialog", 0, 12));
    polyLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    polyLabel.setForeground(Color.black);
    polyLabel.setText("Polynomial");
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel7.setForeground(Color.black);
    jLabel7.setText("Order");
    baselineFitting.setFont(new java.awt.Font("Dialog", 0, 12));
    baselineFitting.setText("Baseline Fitting");
    deSpike.setFont(new java.awt.Font("Dialog", 0, 12));
    deSpike.setText("De-spike");
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel9.setForeground(Color.black);
    jLabel9.setText("Regriding Method");
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setForeground(Color.black);
    jLabel10.setText("Total Map Size");
    recipe.setFont(new java.awt.Font("Dialog", 0, 12));
    baselineRegionUnits.setFont(new java.awt.Font("Dialog", 0, 12));
    channelBinning.setFont(new java.awt.Font("Dialog", 0, 12));
    regriddingMethod.setFont(new java.awt.Font("Dialog", 0, 12));
//    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
//    jLabel8.setForeground(Color.black);
//    jLabel8.setText("Line Regions");
//    lineRegions.setRequestFocusEnabled(true);
//    lineRegions.setText("");
//    lineRegions.setRows(3);
    baselineRegions.setRows(3);
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setText("Window Type");
    truncation.setPreferredSize(new Dimension(6, 21));
    truncation.setColumns(0);
    windowType.setFont(new java.awt.Font("Dialog", 0, 12));
    windowType.setActionCommand("comboBoxChanged");
    showSpectralRegionEditor.setText("Edit Regions");
    
    // Added by SDW
    baselineLabel.setFont(new java.awt.Font("Dialog", 0, 12));
    baselineLabel.setText("Baseline Selection");
    noBaselineButton.setFont(new java.awt.Font("Dialog", 0, 12));
    noBaselineButton.setText("No Baseline");
    automaticBaselineButton.setFont(new java.awt.Font("Dialog", 0, 12));
    automaticBaselineButton.setText("Auto. Selection");
    automaticBaselineButton.setToolTipText("Fracton of bandwidth for baseline estimation");
    manualBaselineButton.setFont(new java.awt.Font("Dialog", 0, 12));
    manualBaselineButton.setText("Manual Selection");



    
//    this.add(baselineFitting,           new GridBagConstraints(5, 0, 2, 1, 0.0, 0.0
//            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 3, -3));
    this.add(jLabel2,     new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    this.add(jLabel1,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 23, 1));
    this.add(recipe,         new GridBagConstraints(1, 0, 4, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
    this.add(jLabel3,      new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    this.add(channelBinning,        new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
    // Added by SDW
    this.add( baselineLabel, new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
		new Insets(0, 5, 0, 5), 0, 0 ) );
    this.add( noBaselineButton, new GridBagConstraints( 1, 3, 1, 1, 0.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.NONE,
		new Insets(0, 5, 0, 5), 0, 0 ) );
    this.add( automaticBaselineButton, new GridBagConstraints( 1, 4, 1, 1, 0.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.NONE,
		new Insets(0, 5, 0, 5), 0, 0 ) );
    this.add( automaticBaseline, new GridBagConstraints( 2, 4, 1, 1, 1.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
		new Insets(0, 5, 0, 5), 0, 0 ) );
    this.add( manualBaselineButton, new GridBagConstraints( 1, 5, 1, 1, 0.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.NONE,
		new Insets(0, 5, 0, 5), 0, 0 ) );
    this.add( polyLabel, new GridBagConstraints( 0, 6, 1, 1, 0.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.NONE,
		new Insets(0, 5, 0, 5), 0, 0 ) );
    this.add( baselineFittingPolynomial, new GridBagConstraints( 1, 6, 1, 1, 1.0, 0.0,
		GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
		new Insets(0, 5, 0, 5), 0, 0 ) );

    // END OF ADDITIONS
    this.add(truncation,          new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
    this.add(jLabel2a,          new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
    this.add(baselineFitPanel,   new GridBagConstraints(0, 7, 7, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 367, 117));
    baselineFitPanel.add(jLabel4,             new GridBagConstraints(0, 0, 1, 3, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    baselineFitPanel.add(baselineRegionUnits, new GridBagConstraints(3, 1, 2, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
	    new Insets(0, 5, 0, 5), 0, 0));
    baselineFitPanel.add(jLabel7,              new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 0, 0));
//    baselineFitPanel.add(polynomialOrder,            new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
//            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    baselineFitPanel.add(jScrollPane1,            new GridBagConstraints(1, 0, 2, 3, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//    baselineFitPanel.add(baselineFittingPolynomial,       new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
//            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    baselineFitPanel.add(showSpectralRegionEditor,        new GridBagConstraints(3, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 10), 0, 0));
    jScrollPane1.getViewport().add(baselineRegions, null);
    this.add(deSpike,   new GridBagConstraints(0, 8, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
	    new Insets(0, 0, 0, 0), 0, 0));
//     this.add(jLabel9,  new GridBagConstraints(1, 8, 4, 1, 0.0, 0.0
//             ,GridBagConstraints.WEST, GridBagConstraints.NONE, 
// 	    new Insets(0, 0, 0, 0), 0, 0));
//     this.add(regriddingMethod,     new GridBagConstraints(1, 9, 3, 1, 1.0, 0.0
//             ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
    this.add(jLabel10,   new GridBagConstraints(5, 8, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 66, 1));
    this.add(totalMapSize,     new GridBagConstraints(5, 9, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
    this.add(jLabel11,      new GridBagConstraints(3, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 2), 0, 0));
    this.add(windowType,       new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//    baselineFitPanel.add(jLabel5,             new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
//            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 0, 0));
//    baselineFitPanel.add(jLabel6,                   new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
//            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 2), 0, 0));
//    baselineFitPanel.add(sinePhase,               new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0
//            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//    baselineFitPanel.add(jLabel8,          new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
//            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
//    baselineFitPanel.add(jScrollPane2,           new GridBagConstraints(1, 3, 2, 1, 1.0, 1.0
//            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//    baselineFitPanel.add(baselineFittingSine,       new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
//            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
//    baselineFitPanel.add(sinePeriod,     new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
//            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//    jScrollPane2.getViewport().add(lineRegions, null);
  }

}
