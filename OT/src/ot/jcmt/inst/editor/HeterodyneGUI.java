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
import java.awt.Font;

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
  private JPanel overlapBandwidthPanel;
  private JPanel velocityPanel;
//  private JPanel rangePanel;
//  private Box linePanel;
  private JPanel mol1Panel;
  private JPanel mol2Panel;
  private JPanel northPanel;
  private JPanel southPanel;
  JLabel lowFreq;
  JLabel highFreq;
  JTextField velocity;
  JComboBox velocityDefinition;
  JComboBox velocityFrame;
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
  JLabel resolution;
  JButton freqEditorButton = new JButton("Show Frequency Editor");
  JButton hideFreqEditorButton = new JButton("Hide Frequency Editor");
  JLabel label = null;
//  private JScrollPane scrollPanel;
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
      label = new JLabel("Front End");
      label.setFont(new Font("Dialog", 0, 12));
      label.setForeground(Color.black);
      fePanel.add(label);

      feChoice = new JComboBox (); //cfg.frontEnds );
      feChoice.setFont(new Font("Dialog", 0, 12));
      feChoice.setForeground(Color.black);
      lowFreq = new JLabel ( "215" );
      lowFreq.setFont(new Font("Dialog", 0, 12));
      lowFreq.setForeground(Color.black);
      lowFreq.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
      highFreq = new JLabel ( "272" );
      highFreq.setFont(new Font("Dialog", 0, 12));
      highFreq.setForeground(Color.black);
      highFreq.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );

//      feChoice.addActionListener ( this );

      feMode = new JComboBox (); // cfg.frontEndModes );
      feMode.setFont(new Font("Dialog", 0, 12));
      feMode.setForeground(Color.black);
      feBandModeChoice = new JComboBox ( );
      feBandModeChoice.setFont(new Font("Dialog", 0, 12));
      feBandModeChoice.setForeground(Color.black);
      //feBandModeChoice.addActionListener ( this );

      fePanel.add ( feChoice );
      fePanel.add ( feMode );
      fePanel.add ( feBandModeChoice );

/* Create the display */

      displayPanel = new JPanel(flowLayoutLeft);
      label = new JLabel("Low Limit (GHz)");
      label.setFont(new Font("Dialog", 0, 12));
      label.setForeground(Color.black);
      displayPanel.add ( label );
      displayPanel.add ( lowFreq );
      label = new JLabel("    High Limit (GHz)");
      label.setFont(new Font("Dialog", 0, 12));
      label.setForeground(Color.black);
      displayPanel.add ( label );
      displayPanel.add ( highFreq );

      // Bandwidth and Overlap
      overlapBandwidthPanel = new JPanel(flowLayoutLeft);

      label = new JLabel("Side Band");
      label.setFont(new Font("Dialog", 0, 12));
      label.setForeground(Color.black);
      fePanel.add(label);

      feBand = new JComboBox(); // new String[] { "usb", "lsb", "optimum" } );
      feBand.setFont(new Font("Dialog", 0, 12));
      feBand.setForeground(Color.black);
//      feBand.addActionListener ( this );
      fePanel.add(feBand);

      label = new JLabel("    Bandwidth");
      label.setFont(new Font("Dialog", 0, 12));
      label.setForeground(Color.black);
      overlapBandwidthPanel.add(label);
      
      bandWidthChoice = new JComboBox();
      bandWidthChoice.setFont(new Font("Dialog", 0, 12));
      bandWidthChoice.setForeground(Color.black);
      overlapBandwidthPanel.add(bandWidthChoice);

      label = new JLabel("    Resolution (KHz)");
      label.setFont(new Font("Dialog", 0, 12));
      label.setForeground(Color.black);
      resolution = new JLabel();
      resolution.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
      resolution.setFont(new Font("Dialog", 0, 12));
      resolution.setForeground(Color.black);
      displayPanel.add(label);
      displayPanel.add(resolution);

      label = new JLabel("    Overlap (MHz)");
      label.setFont(new Font("Dialog", 0, 12));
      label.setForeground(Color.black);
      overlapBandwidthPanel.add (label);

      overlap = new JTextField();
      overlap.setColumns(10);
      overlap.setText ( "0.0" );
//      overlap.addActionListener ( this );
      overlapBandwidthPanel.add(overlap);

      // Velocity
      velocityPanel = new JPanel(flowLayoutLeft);

      label = new JLabel("Velocity (km/s), Redshift");
      label.setFont(new Font("Dialog", 0, 12));
      label.setForeground(Color.black);
      velocityPanel.add(label);

      velocityDefinition = new JComboBox();
      velocityDefinition.setFont(new Font("Dialog", 0, 12));
      velocityPanel.add(velocityDefinition);

      velocity = new JTextField();
      velocity.setColumns(10);
      velocity.setText ( "0.0" );
//      velocity.addActionListener ( this );
      velocityPanel.add(velocity);

      label = new JLabel("    Frame");
      label.setFont(new Font("Dialog", 0, 12));
      label.setForeground(Color.black);
      velocityPanel.add(label);

      velocityFrame = new JComboBox();
      velocityFrame.setFont(new Font("Dialog", 0, 12));
      velocityPanel.add(velocityFrame);

//      rangePanel = new JPanel(flowLayoutLeft);


/* Main molecular line choice - used to set front-end LO1 to put the line
   in the nominated sideband */

      moleculeChoice = new JComboBox();
      moleculeChoice.setFont(new Font("Dialog", 0, 12));
      moleculeChoice.setForeground ( Color.red );
//      moleculeChoice.addActionListener ( this );
      transitionChoice = new JComboBox();
      transitionChoice.setFont(new Font("Dialog", 0, 12));
      transitionChoice.setForeground ( Color.red );
//      transitionChoice.addActionListener ( this );
      moleculeFrequency = new JTextField();
      moleculeFrequency.setColumns(12);
      //moleculeFrequency.setText ( "0.0000" );
      moleculeFrequency.setForeground ( Color.red );
//      moleculeFrequency.addActionListener(this);
//      bandWidthChoice.addActionListener( this );
      mol1Panel = new JPanel(flowLayoutLeft);
      mol1Panel.add ( moleculeChoice );
      mol1Panel.add ( transitionChoice );
      mol1Panel.add ( moleculeFrequency );
      label = new JLabel("MHz");
      label.setFont(new Font("Dialog", 0, 12));
      label.setForeground(Color.black);
      mol1Panel.add (label);
/* Secondary moleculare line choice - displayed just for convenience of
   astronomer */

      moleculeChoice2 = new JComboBox();
      moleculeChoice2.setFont(new Font("Dialog", 0, 12));
      moleculeChoice2.setForeground ( Color.magenta );
//      moleculeChoice2.addActionListener ( this );
      transitionChoice2 = new JComboBox();
      transitionChoice2.setFont(new Font("Dialog", 0, 12));
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
      label = new JLabel("MHz");
      label.setFont(new Font("Dialog", 0, 12));
      label.setForeground(Color.black);
      mol2Panel.add(label);

      freqEditorButton.setFont(new Font("Dialog", 0, 12));
      hideFreqEditorButton.setFont(new Font("Dialog", 0, 12));

//      linePanel = Box.createVerticalBox();
//      linePanel.add ( mol1Panel );

      // MFO (May 03, 2002): mol2Panel is currently not used.
      //linePanel.add ( mol2Panel );

      //linePanel.add ( sideBandButton );

      //sideBandButton.addActionListener(this);

/* Assemble the display */

//      rangePanel.add ( linePanel );

      northPanel = new JPanel();
      northPanel.setLayout ( new BoxLayout ( northPanel, 
        BoxLayout.Y_AXIS ) );

      Dimension preferredSize = fePanel.getPreferredSize();
      preferredSize.height += 10;
      fePanel.setPreferredSize(preferredSize);
      preferredSize = displayPanel.getPreferredSize();
      preferredSize.height += 10;
      displayPanel.setPreferredSize(preferredSize);
      preferredSize = overlapBandwidthPanel.getPreferredSize();
      preferredSize.height += 10;
      overlapBandwidthPanel.setPreferredSize(preferredSize);
      preferredSize = velocityPanel.getPreferredSize();
      preferredSize.height += 10;
      velocityPanel.setPreferredSize(preferredSize);
      preferredSize = mol1Panel.getPreferredSize();
      preferredSize.height += 10;
      mol1Panel.setPreferredSize(preferredSize);


      northPanel.add ( fePanel );
      northPanel.add ( displayPanel );
      northPanel.add ( overlapBandwidthPanel );
      northPanel.add ( velocityPanel );
      northPanel.add ( mol1Panel );
      add ( northPanel, BorderLayout.NORTH );

//      scrollPanel = new JScrollPane();
      //scrollPanel.setPreferredSize ( new Dimension ( 600, 150 ) );

//      add ( scrollPanel, BorderLayout.CENTER );


      southPanel = new JPanel();
      southPanel.add(freqEditorButton);
      southPanel.add(hideFreqEditorButton);

      add(southPanel, BorderLayout.SOUTH);
  }
}

