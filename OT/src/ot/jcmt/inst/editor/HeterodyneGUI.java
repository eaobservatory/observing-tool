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

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.BoxLayout;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Container;

/**
 * This GUI class is based on the GUI part of the edfreq.FrontEnd class which is not used anymore.
 *
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class HeterodyneGUI extends JPanel {

  JComboBox feChoice;
  JComboBox feBandModeChoice;
  private String currentFE = "";
  private JPanel fePanel;
  private JPanel displayPanel;
  private JPanel rangePanel;
  private Box linePanel;
  private JPanel mol1Panel;
  private JPanel mol2Panel;
  private JPanel northPanel;
  private JPanel southPanel;
  JLabel lowFreq;
  JLabel highFreq;
  JTextField velocity;
  JTextField overlap;
  JComboBox feBand;
  JComboBox feMode;
  JComboBox moleculeChoice;
  JComboBox moleculeChoice2;
  JTextField moleculeFrequency;
  JTextField moleculeFrequency2;
  JComboBox transitionChoice;
  JComboBox transitionChoice2;
  JComboBox bandWidthChoice;
  JButton freqEditorButton = new JButton("Show Frequency Editor");
  private JScrollPane scrollPanel;
  private double redshift = 0.0;
  private double subBandWidth = 0.25E9;
  private double loMin;
  private double loMax;
  private double feIF;
  private double feBandWidth;
  private double feOverlap = 0.0;
  private String defaultStoreFile = "hetsetup.txt";
  private FlowLayout flowLayoutLeft  = new FlowLayout(FlowLayout.LEFT);
  private FlowLayout flowLayoutRight = new FlowLayout(FlowLayout.RIGHT);
  // Changed to avoid security exception in applet.
  private String defaultStoreDirectory = ".;"; //System.getProperty("user.dir");
  private boolean editFlag = false;
  // Commented out to avoid security exception in applet.
  // private JFileChooser fileChooser = new JFileChooser ( );

  //private JButton sideBandButton = new JButton("Show Side Band Display");


  public HeterodyneGUI() {

      setLayout(new BorderLayout());
/* Create the choice of frontends */

      fePanel = new JPanel(flowLayoutLeft);
      fePanel.add ( new JLabel ( "Choose Front End" ) );

      feChoice = new JComboBox (); //cfg.frontEnds );
      lowFreq = new JLabel ( "215" );
      lowFreq.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
      highFreq = new JLabel ( "272" );
      highFreq.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );

//      feChoice.addActionListener ( this );

      feMode = new JComboBox (); // cfg.frontEndModes );
      feBandModeChoice = new JComboBox ( );
      //feBandModeChoice.addActionListener ( this );

      overlap = new JTextField();
      overlap.setColumns(10);
      overlap.setText ( "0.0" );
//      overlap.addActionListener ( this );

      fePanel.add ( feChoice );
      fePanel.add ( feMode );
      fePanel.add ( feBandModeChoice );
      fePanel.add ( new JLabel ( "    Overlap (MHz)" ) );
      fePanel.add ( overlap );

/* Create the display */

      displayPanel = new JPanel(flowLayoutLeft);
      displayPanel.add ( new JLabel ( "Low Limit (GHz)" ) );
      displayPanel.add ( lowFreq );
      displayPanel.add ( new JLabel ( "    High Limit (GHz)" ) );
      displayPanel.add ( highFreq );
      velocity = new JTextField();
      velocity.setColumns(10);
      velocity.setText ( "0.0" );
//      velocity.addActionListener ( this );

      displayPanel.add ( new JLabel ( "    Velocity (Km/s)" ) );
      displayPanel.add ( velocity );

      rangePanel = new JPanel();
      feBand = new JComboBox(); // new String[] { "usb", "lsb", "optimum" } );
//      feBand.addActionListener ( this );

/* Main molecular line choice - used to set front-end LO1 to put the line
   in the nominated sideband */

      moleculeChoice = new JComboBox();
      moleculeChoice.setForeground ( Color.red );
//      moleculeChoice.addActionListener ( this );
      transitionChoice = new JComboBox();
      transitionChoice.setForeground ( Color.red );
//      transitionChoice.addActionListener ( this );
      moleculeFrequency = new JTextField();
      moleculeFrequency.setColumns(12);
      //moleculeFrequency.setText ( "0.0000" );
      moleculeFrequency.setForeground ( Color.red );
//      moleculeFrequency.addActionListener(this);
      bandWidthChoice = new JComboBox();
//      bandWidthChoice.addActionListener( this );
      mol1Panel = new JPanel(flowLayoutRight);
      mol1Panel.add ( feBand );
      mol1Panel.add ( moleculeChoice );
      mol1Panel.add ( transitionChoice );
      mol1Panel.add ( moleculeFrequency );
      mol1Panel.add ( new JLabel("MHz    BW:") );
      mol1Panel.add ( bandWidthChoice );

/* Secondary moleculare line choice - displayed just for convenience of
   astronomer */

      moleculeChoice2 = new JComboBox();
      moleculeChoice2.setForeground ( Color.magenta );
//      moleculeChoice2.addActionListener ( this );
      transitionChoice2 = new JComboBox();
      transitionChoice2.setForeground ( Color.magenta );
//      transitionChoice2.addActionListener ( this );
      moleculeFrequency2 = new JTextField();
      moleculeFrequency2.setColumns(12);
      moleculeFrequency2.setText ( "0.0000" );
      moleculeFrequency2.setForeground ( Color.magenta );
//      moleculeFrequency2.addActionListener(this);
      mol2Panel = new JPanel(flowLayoutRight);
      mol2Panel.add ( moleculeChoice2 );
      mol2Panel.add ( transitionChoice2 );
      mol2Panel.add ( moleculeFrequency2 );
      mol2Panel.add ( new JLabel("MHz") );

      linePanel = Box.createVerticalBox();
      linePanel.add ( mol1Panel );

      // MFO (May 03, 2002): mol2Panel is currently not used.
      //linePanel.add ( mol2Panel );

      //linePanel.add ( sideBandButton );

      //sideBandButton.addActionListener(this);

/* Assemble the display */

      rangePanel.add ( linePanel );

      northPanel = new JPanel();
      northPanel.setLayout ( new BoxLayout ( northPanel, 
        BoxLayout.Y_AXIS ) );

      northPanel.add ( fePanel );
      northPanel.add ( displayPanel );
      northPanel.add ( rangePanel );
      add ( northPanel, BorderLayout.NORTH );

      scrollPanel = new JScrollPane();
      //scrollPanel.setPreferredSize ( new Dimension ( 600, 150 ) );

      add ( scrollPanel, BorderLayout.CENTER );


      southPanel = new JPanel();
      southPanel.add(freqEditorButton);

      add(southPanel, BorderLayout.SOUTH);
  }
}

