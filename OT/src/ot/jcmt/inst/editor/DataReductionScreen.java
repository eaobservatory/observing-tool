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

/**
 * ACSIS DR GUI.
 * 
 * @author Martin Folger ( M.Folger@roe.ac.uk )
 */
public class DataReductionScreen extends JPanel {
  JComboBox recipeComboBox = new JComboBox();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JTextField jTextField1 = new JTextField();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JTextArea jTextArea1 = new JTextArea();
  JComboBox baseLineUnitComboBox = new JComboBox();
  JPanel jPanel1 = new JPanel();
  JRadioButton jRadioButton1 = new JRadioButton();
  JLabel jLabel5 = new JLabel();
  JTextField jTextField3 = new JTextField();
  JLabel jLabel6 = new JLabel();
  JTextField jTextField4 = new JTextField();
  JRadioButton jRadioButton2 = new JRadioButton();
  JLabel jLabel7 = new JLabel();
  JTextField jTextField5 = new JTextField();
  TitledBorder titledBorder1;
  JRadioButton jRadioButton3 = new JRadioButton();
  JRadioButton jRadioButton4 = new JRadioButton();
  JRadioButton jRadioButton5 = new JRadioButton();
  JLabel jLabel8 = new JLabel();
  JComboBox channelSpacingComboBox = new JComboBox();
  JCheckBox jCheckBox1 = new JCheckBox();
  JLabel jLabel9 = new JLabel();
  JComboBox regridingMethodComboBox = new JComboBox();
  JProgressBar jProgressBar1 = new JProgressBar();
  JLabel jLabel10 = new JLabel();

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
    this.setLayout(null);
    this.setSize(new Dimension(501, 339));
    //this.setTitle("Acsis Data Reduction");
    recipeComboBox.setBounds(new Rectangle(89, 19, 182, 26));
    jLabel1.setText("Recipe");
    jLabel1.setBounds(new Rectangle(21, 26, 62, 17));
    jLabel2.setText("Truncation (Channels)");
    jLabel2.setBounds(new Rectangle(20, 48, 176, 17));
    jTextField1.setText("16");
    jTextField1.setBounds(new Rectangle(196, 47, 76, 21));
    jLabel3.setText("Channel Spacing");
    jLabel3.setBounds(new Rectangle(19, 75, 117, 17));
    jLabel4.setText("Baseline Windows");
    jLabel4.setBounds(new Rectangle(10, 18, 139, 22));
    jTextArea1.setBounds(new Rectangle(150, 15, 109, 52));
    baseLineUnitComboBox.setBounds(new Rectangle(265, 20, 88, 26));
    jPanel1.setBorder(titledBorder1);
    jPanel1.setBounds(new Rectangle(18, 97, 368, 118));
    jPanel1.setLayout(null);
    jRadioButton1.setText("Sine wave");
    jRadioButton1.setBounds(new Rectangle(8, 68, 107, 25));
    jLabel5.setText("Period?");
    jLabel5.setBounds(new Rectangle(116, 73, 63, 17));
    jTextField3.setBounds(new Rectangle(179, 70, 63, 21));
    jLabel6.setText("Phase");
    jLabel6.setBounds(new Rectangle(247, 73, 53, 17));
    jTextField4.setBounds(new Rectangle(297, 73, 63, 21));
    jRadioButton2.setText("Polynomial");
    jRadioButton2.setBounds(new Rectangle(7, 90, 104, 25));
    jLabel7.setText("Order");
    jLabel7.setBounds(new Rectangle(116, 91, 61, 17));
    jTextField5.setBounds(new Rectangle(179, 92, 63, 21));
    jRadioButton3.setText("Off");
    jRadioButton3.setBounds(new Rectangle(282, 25, 114, 22));
    jRadioButton4.setText("Manual");
    jRadioButton4.setBounds(new Rectangle(281, 48, 118, 25));
    jRadioButton5.setText("Automatic");
    jRadioButton5.setBounds(new Rectangle(281, 74, 117, 25));
    jLabel8.setText("Baseline Fitting");
    jLabel8.setBounds(new Rectangle(281, 10, 148, 17));
    channelSpacingComboBox.setBounds(new Rectangle(156, 71, 116, 26));
    jCheckBox1.setText("De-spike");
    jCheckBox1.setBounds(new Rectangle(20, 215, 89, 25));
    jLabel9.setText("Regriding Method");
    jLabel9.setBounds(new Rectangle(111, 214, 161, 17));
    regridingMethodComboBox.setBounds(new Rectangle(111, 228, 130, 26));
    jProgressBar1.setBounds(new Rectangle(247, 228, 138, 14));
    jLabel10.setText("Total Map Size");
    jLabel10.setBounds(new Rectangle(247, 212, 145, 17));
    this.add(jRadioButton4, null);
    this.add(jRadioButton3, null);
    this.add(jRadioButton5, null);
    this.add(jLabel8, null);
    this.add(jLabel2, null);
    this.add(jLabel1, null);
    this.add(recipeComboBox, null);
    this.add(jLabel3, null);
    this.add(channelSpacingComboBox, null);
    this.add(jTextField1, null);
    this.add(jPanel1, null);
    jPanel1.add(jLabel4, null);
    jPanel1.add(jTextArea1, null);
    jPanel1.add(baseLineUnitComboBox, null);
    jPanel1.add(jLabel5, null);
    jPanel1.add(jRadioButton1, null);
    jPanel1.add(jRadioButton2, null);
    jPanel1.add(jLabel7, null);
    jPanel1.add(jTextField3, null);
    jPanel1.add(jTextField5, null);
    jPanel1.add(jLabel6, null);
    jPanel1.add(jTextField4, null);
    this.add(jCheckBox1, null);
    this.add(jLabel9, null);
    this.add(regridingMethodComboBox, null);
    this.add(jLabel10, null);
    this.add(jProgressBar1, null);

    recipeComboBox.addItem("Default Recipe");
    baseLineUnitComboBox.addItem("km/s");
    baseLineUnitComboBox.addItem("MHz");
    baseLineUnitComboBox.addItem("Channels");

    /*
     *  There are other settings depending on the bandwidth and the front end.
     */
    channelSpacingComboBox.addItem(" 31 kHz");
    channelSpacingComboBox.addItem(" 62 kHz");
    channelSpacingComboBox.addItem("124 kHz");
    channelSpacingComboBox.addItem("248 kHz");
    channelSpacingComboBox.addItem("496 kHz");
    channelSpacingComboBox.addItem(" ~1 MHz");
    channelSpacingComboBox.addItem(" ~2 MHz");
    channelSpacingComboBox.addItem(" ~4 MHz");
    channelSpacingComboBox.addItem(" ~8 MHz");
    channelSpacingComboBox.addItem("~16 MHz");

    regridingMethodComboBox.addItem("Linear");
    regridingMethodComboBox.addItem("Bessel");
  }

}
