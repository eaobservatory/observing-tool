/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.inst.editor.edfreq;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.BoxLayout;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Container;
import java.awt.Point;
import java.util.*;
import java.io.*;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class FrontEnd extends JPanel implements ActionListener
{
   private JComboBox feChoice;
   private JComboBox feBandModeChoice;
   private Hashtable feBandModeHashtable = new Hashtable();
   private String currentFE = "";
   private Hashtable feDetails = new Hashtable();
   private JPanel fePanel;
   private JPanel displayPanel;
   private JPanel rangePanel;
   private Box linePanel;
   private JPanel mol1Panel;
   private JPanel mol2Panel;
   private JPanel northPanel;
   private JLabel lowFreq;
   private JLabel highFreq;
   private JTextField velocity;
   private JTextField overlap;
   private SideBandDisplay sideBandDisplay = null;
   private LineCatalog lineCatalog = new LineCatalog();
   private JComboBox feBand;
   private JComboBox feMode;
   private JComboBox moleculeChoice;
   private JComboBox moleculeChoice2;
   private JLabel moleculeFrequency;
   private JLabel moleculeFrequency2;
   private JComboBox transitionChoice;
   private JComboBox transitionChoice2;
   private JScrollPane scrollPanel;
   private double redshift = 0.0;
   private double subBandWidth = 0.25E9;
   private ReceiverList receiverList = new ReceiverList();
   private double loMin;
   private double loMax;
   private double feIF;
   private double feBandWidth;
   private double feOverlap = 0.0;
   private String defaultStoreFile = "hetsetup.txt";
   // Changed to avoid security exception in applet.
   private String defaultStoreDirectory = ".;"; //System.getProperty("user.dir");
   private boolean editFlag = false;
   // Commented out to avoid security exception in applet.
   // private JFileChooser fileChooser = new JFileChooser ( );

   private JButton sideBandButton = new JButton("Show Side Band Display");

   public FrontEnd ( )
   {

      setLayout(new BorderLayout());
/* Create the choice of frontends */

      fePanel = new JPanel();
      fePanel.add ( new JLabel ( "Choose Front End" ) );

      feChoice = new JComboBox ( new String[] { "A3", "B3", "WC", "WD",
        "HARP-B" } );
      lowFreq = new JLabel ( "215" );
      lowFreq.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
      highFreq = new JLabel ( "272" );
      highFreq.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );

      feChoice.addActionListener ( this );

      feDetails.put ( "A3", new double[] { 215.0E9, 272.0E9 } );
      feDetails.put ( "B3", new double[] { 322.0E9, 373.0E9 } );
      feDetails.put ( "WC", new double[] { 430.0E9, 510.0E9 } );
      feDetails.put ( "WD", new double[] { 630.0E9, 710.0E9 } );
      feDetails.put ( "HARP-B", new double[] { 325.0E9, 375.0E9 } );

      feMode = new JComboBox ( new String[] { "ssb", "dsb" } );
      feBandModeChoice = new JComboBox ( );
      feBandModeChoice.addActionListener ( this );

      overlap = new JTextField ( 10 );
      overlap.setText ( "0.0" );
      overlap.addActionListener ( this );

      fePanel.add ( feChoice );
      fePanel.add ( feMode );
      fePanel.add ( feBandModeChoice );
      fePanel.add ( new JLabel ( "Overlap (MHz)" ) );
      fePanel.add ( overlap );

/* Create the display */

      displayPanel = new JPanel();
      displayPanel.add ( new JLabel ( "Low Limit (GHz)" ) );
      displayPanel.add ( lowFreq );
      displayPanel.add ( new JLabel ( "High Limit (GHz)" ) );
      displayPanel.add ( highFreq );
      velocity = new JTextField ( 10 );
      velocity.setText ( "0.0" );
      velocity.addActionListener ( this );

      displayPanel.add ( new JLabel ( "Velocity (Km/s)" ) );
      displayPanel.add ( velocity );

      rangePanel = new JPanel();
      feBand = new JComboBox ( new String[] { "usb", "lsb" } );
      feBand.addActionListener ( this );

/* Main molecular line choice - used to set front-end LO1 to put the line
   in the nominated sideband */

      moleculeChoice = new JComboBox();
      moleculeChoice.setForeground ( Color.red );
      moleculeChoice.addActionListener ( this );
      transitionChoice = new JComboBox();
      transitionChoice.setForeground ( Color.red );
      transitionChoice.addActionListener ( this );
      moleculeFrequency = new JLabel ( "0.0000" );
      moleculeFrequency.setForeground ( Color.red );
      mol1Panel = new JPanel();
      mol1Panel.add ( feBand );
      mol1Panel.add ( moleculeChoice );
      mol1Panel.add ( transitionChoice );
      mol1Panel.add ( moleculeFrequency );

/* Secondary moleculare line choice - displayed just for convenience of
   astronomer */

      moleculeChoice2 = new JComboBox();
      moleculeChoice2.setForeground ( Color.magenta );
      moleculeChoice2.addActionListener ( this );
      transitionChoice2 = new JComboBox();
      transitionChoice2.setForeground ( Color.magenta );
      transitionChoice2.addActionListener ( this );
      moleculeFrequency2 = new JLabel ( "0.0000" );
      moleculeFrequency2.setForeground ( Color.magenta );
      mol2Panel = new JPanel();
      mol2Panel.add ( moleculeChoice2 );
      mol2Panel.add ( transitionChoice2 );
      mol2Panel.add ( moleculeFrequency2 );

      linePanel = Box.createVerticalBox();
      linePanel.add ( mol1Panel );
      linePanel.add ( mol2Panel );
      linePanel.add ( sideBandButton );

      sideBandButton.addActionListener(this);

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
   
      // MFO trigger additional initialising.
      feChoiceAction(null);
      createSideBandDisplay();
   }

   public void actionPerformed ( ActionEvent ae )
   {

      if ( ae.getSource() == feBand )
      {
         feBandAction ( ae );
      }
      else if ( ae.getSource() == moleculeChoice )
      {
         feMoleculeAction ( ae );
      }
      else if ( ae.getSource() == transitionChoice )
      {
         feTransitionAction ( ae );
      }
      else if ( ae.getSource() == moleculeChoice2 )
      {
         feMolecule2Action ( ae );
      }
      else if ( ae.getSource() == transitionChoice2 )
      {
         feTransition2Action ( ae );
      }
      else if ( ae.getSource() == feChoice )
      {
         feChoiceAction ( ae );
      }
      else if ( ae.getSource() == feBandModeChoice )
      {
         feBandModeChoiceAction ( ae );
      }
      else if ( ae.getSource() == velocity )
      {
         feVelocityAction ( ae );
      }
      else if ( ae.getSource() == overlap )
      {
         feOverlapAction ( ae );
      }
      else if ( ae.getActionCommand() == "Save" )
      {
         // Commented out to avoid security exception in applet.
         //save ( );
      }
      else if ( ae.getSource() == sideBandButton) {
        // This should not happen.
        if(sideBandDisplay == null) {
          JOptionPane.showMessageDialog(this,
	                                "Frequency Editor does not seem to be fully initialized.\n" +
	                                "Please change (or repeat) some of your settings",
					"Problem", JOptionPane.ERROR_MESSAGE);
	}
	else {
          //createSideBandDisplay();
          sideBandDisplay.setVisible ( true );
	  sideBandDisplay.requestFocus();
	}  
      }
   }


   public void feBandAction ( ActionEvent ae )
   {
      feTransitionAction ( ae );
   }


   public void feMoleculeAction ( ActionEvent ae )
   {
      SelectionList species = (SelectionList)moleculeChoice.getSelectedItem();

      transitionChoice.setModel ( 
        new DefaultComboBoxModel ( species.objectList ) );

      moleculeFrequency.setText ( "0.0000" );
   }


   public void feMolecule2Action ( ActionEvent ae )
   {
      SelectionList species = (SelectionList)moleculeChoice2.getSelectedItem();

      transitionChoice2.setModel ( 
        new DefaultComboBoxModel ( species.objectList ) );

      moleculeFrequency2.setText ( "0.0000" );
   }


   public void feTransitionAction ( ActionEvent ae )
   {
      Transition transition = (Transition)transitionChoice.getSelectedItem();

      if ( transition != null )
      {

         moleculeFrequency.setText ( "" + transition.frequency/1.0E6 +"MHz" );
         String band = (String)feBand.getSelectedItem();

         if ( sideBandDisplay != null )
         {

            sideBandDisplay.setMainLine ( transition.frequency );

            double obsFrequency = transition.frequency / 
              ( 1.0 + redshift );
            if ( band.equals ( "usb" ) )
            {
               sideBandDisplay.setLO1 ( obsFrequency - feIF );
            }
            else
            {
               sideBandDisplay.setLO1 ( obsFrequency + feIF );
            }
         }

      }
   }


   public void feTransition2Action ( ActionEvent ae )
   {
      Transition transition = (Transition)transitionChoice2.getSelectedItem();

      if ( transition != null )
      {

         moleculeFrequency2.setText ( "" + transition.frequency/1.0E6 +"MHz" );

         if ( sideBandDisplay != null )
         {

            sideBandDisplay.setSideLine ( transition.frequency );

         }

      }
   }


   public void feBandModeChoiceAction ( ActionEvent ae ) {
      createSideBandDisplay();
   }

   public void createSideBandDisplay() {

      double loRange[];
      double mid;
      int subBandCount;

      BandSpec currentBandSpec = (BandSpec)feBandModeHashtable.get((String)feBandModeChoice.getSelectedItem());


/* Update display of sidebands and subbands */
      Point sideBandDisplayLocation = new Point(100, 100);

      if ( sideBandDisplay != null )
      {
	 sideBandDisplayLocation = sideBandDisplay.getLocation();

         sideBandDisplay.dispose ( );
      }

      mid = 0.5 * ( loMin + loMax );

      subBandCount = currentBandSpec.numBands;
      subBandWidth = currentBandSpec.loBandWidth;

      sideBandDisplay = new SideBandDisplay ( currentFE, loMin, loMax,
        feIF, feBandWidth,
        redshift,
        currentBandSpec.loBandWidth, currentBandSpec.loChannels,
        currentBandSpec.hiBandWidth, currentBandSpec.hiChannels,
        subBandCount );
      //sideBandDisplay.setVisible ( true );
      sideBandDisplay.setLocation(sideBandDisplayLocation);
   }

   public void feChoiceAction ( ActionEvent ae )
   {

      double loRange[];
      double mid;
      int subBandCount;
      Receiver r;
      double obsmin;
      double obsmax;

      String newFE = (String)feChoice.getSelectedItem();
      currentFE = newFE;
      r = (Receiver)receiverList.receivers.get ( currentFE );


      loMin = r.loMin;
      loMax = r.loMax;
      feIF = r.feIF;
      feBandWidth = r.bandWidth;

      lowFreq.setText ( "" + (int)(loMin*1.0E-9) );
      highFreq.setText ( "" + (int)(loMax*1.0E-9) );

/* Update choice of sub-band configurations */

      feBandModeChoice.removeActionListener(this);
      //feBandModeChoice.setModel ( 
      //  new DefaultComboBoxModel ( r.bandspecs ) );

      feBandModeChoice.removeAllItems();
      feBandModeHashtable.clear();

      for(int i = 0; i < r.bandspecs.size(); i++) {
        feBandModeChoice.addItem(((BandSpec)r.bandspecs.get(i)).toString());
	feBandModeHashtable.put(((BandSpec)r.bandspecs.get(i)).toString(), (BandSpec)r.bandspecs.get(i));
      }

      feBandModeChoice.addActionListener(this);

      obsmin = loMin - feIF - ( feBandWidth * 0.5 );
      obsmax = loMax + feIF + ( feBandWidth * 0.5 );

/* Update choice of molecules */

      moleculeChoice.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
          obsmax*(1.0+redshift) ) ) );
      moleculeChoice2.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
        obsmax*(1.0+redshift) ) ) );

/* Reset line frequency report */

      moleculeFrequency.setText ( "0.0000" );
      moleculeFrequency2.setText ( "0.0000" );


/* Update display of sidebands and subbands */

      createSideBandDisplay();
      //if ( sideBandDisplay != null )
      //{
      //   sideBandDisplay.dispose ( );
      //}

   }


   public void feVelocityAction ( ActionEvent ae )
   {
      String svalue;
      double dvalue;
      double obsmin;
      double obsmax;

      svalue = velocity.getText();
      dvalue = (Double.valueOf(svalue)).doubleValue();
      redshift = dvalue / EdFreq.LIGHTSPEED;

      obsmin = loMin - feIF - ( feBandWidth * 0.5 );
      obsmax = loMax + feIF + ( feBandWidth * 0.5 );

/* Update choice of molecules */

      moleculeChoice.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
          obsmax*(1.0+redshift) ) ) );
      moleculeChoice2.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
        obsmax*(1.0+redshift) ) ) );

/* Update display of sidebands */

      if ( sideBandDisplay != null )
      {
         sideBandDisplay.setRedshift ( redshift );
      }
   }


   public void feOverlapAction ( ActionEvent ae )
   {
      String svalue;
      double dvalue;

      svalue = overlap.getText();
      dvalue = (Double.valueOf(svalue)).doubleValue();
      feOverlap = 1.0E6 * dvalue;
   }

// Commented out to avoid security exception in applet.
/*
   public void save ( )
   {
      fileChooser.setCurrentDirectory ( 
        new File ( defaultStoreDirectory ) );
      fileChooser.setSelectedFile ( 
        new File ( defaultStoreFile ) );

      fileChooser.setDialogType ( JFileChooser.SAVE_DIALOG );

      int option = fileChooser.showSaveDialog ( this );
      if ( ( option == JFileChooser.APPROVE_OPTION ) &&
        ( fileChooser.getSelectedFile() != null ) )
      {
         String fname = fileChooser.getSelectedFile().getName() ;
         String dname = fileChooser.getSelectedFile().getParent() ;
         File file = new File ( dname, fname );
         try 
         {

// create the single OutputStream

            FileOutputStream f = new FileOutputStream ( file );
            PrintWriter out = new PrintWriter ( f );

// Write the details of this object to disk, but do NOT write the links
//   between this bean and other beans.

            saveAsASCII ( out );
            out.close();

// Update the default file name

            defaultStoreFile = fname;
            defaultStoreDirectory = dname;

// Unset the edited flag

            editFlag = false;

         }
         catch ( IOException ioe )
         {
//            error ( "Save failed", ioe );
         }


      }
   }
*/

   public void saveAsASCII ( PrintWriter out )
   {
      out.println ( "<heterodyne" );

      out.println ( "  feName=" + (String)feChoice.getSelectedItem() );
      out.println ( "  mode=" + (String)feMode.getSelectedItem() );
      out.println ( "  bandMode=" + 
        (feBandModeChoice.getSelectedItem()).toString() );
      out.println ( "  velocity=" + velocity.getText() );
      out.println ( "  band=" + (String)feBand.getSelectedItem() );

      String svalue = overlap.getText();
      double dvalue = (Double.valueOf(svalue)).doubleValue();
      feOverlap = 1.0E6 * dvalue;

      out.println ( "  overlap=" + feOverlap );

      out.println ( "  LO1=" + sideBandDisplay.getLO1() );

      out.println ( ">" );

      sideBandDisplay.saveAsASCII ( out );

      out.println ( "</heterodyne>" );

   }


   public String toXML() {
      StringBuffer stringBuffer = new StringBuffer();
      String value = "";

      stringBuffer.append ( "<heterodyne\n" );

      stringBuffer.append ( " feName=\"" + (String)feChoice.getSelectedItem() + "\"");
      stringBuffer.append ( " mode=\"" + (String)feMode.getSelectedItem() + "\"");
      try {
        value = feBandModeChoice.getSelectedItem().toString();
      }
      catch(NullPointerException e) {
        value = "";
        // ignore for now.
	// Probable reason for NullPointerException: toXML is called in _updateWidgets of another class
	// and many things in the frequency editor are currently only initialised after the user has made
	// some choices/settings.
      }
      stringBuffer.append ( " bandMode=\"" + value + "\"");

      stringBuffer.append ( " velocity=\"" + velocity.getText() + "\"");
      stringBuffer.append ( " band=\"" + (String)feBand.getSelectedItem() + "\"");

      String svalue = overlap.getText();
      double dvalue = (Double.valueOf(svalue)).doubleValue();
      feOverlap = 1.0E6 * dvalue;

      stringBuffer.append ( " overlap=\"" + feOverlap + "\"");

      try {
	value = "" + sideBandDisplay.getLO1();
      }
      catch(NullPointerException e) {
        value = "";
        // ignore for now.
	// Probable reason for NullPointerException: toXML is called in _updateWidgets of another class
	// and many things in the frequency editor are currently only initialised after the user has made
	// some choices/settings.
      }
      stringBuffer.append ( " LO1=\"" + value + "\"");

      stringBuffer.append ( ">\n" );

      stringBuffer.append(sideBandDisplay.toXML() + "\n");

      stringBuffer.append ( "</heterodyne>" );

      return stringBuffer.toString();
   }

   public static void main ( String args[] )
   {
      FrontEnd fe = new FrontEnd();
      fe.setVisible ( true );
   }

   /**
    * Returns frequency editor widget.
    * 
    * Allows other classes (such as {@link ot.jcmt.inst.editor.EdCompInstHeterodyne}) to add listeners.
    */
   public JComboBox getFeComboBox() {
      return feChoice;
   }

   /**
    * Returns frequency editor widget.
    * 
    * Allows other classes (such as {@link ot.jcmt.inst.editor.EdCompInstHeterodyne}) to add listeners.
    */
   public JComboBox getFeBandModeComboBox() {
      return feBandModeChoice;
   }

   /**
    * Returns frequency editor widget.
    * 
    * Allows other classes (such as {@link ot.jcmt.inst.editor.EdCompInstHeterodyne}) to add listeners.
    */
   public JTextField getVelocityTextField() {
      return velocity;
   }

   /**
    * Returns frequency editor widget.
    * 
    * Allows other classes (such as {@link ot.jcmt.inst.editor.EdCompInstHeterodyne}) to add listeners.
    */
   public JTextField getOverlapTextField() {
      return overlap;
   }

   /**
    * Returns frequency editor widget.
    * 
    * Allows other classes (such as {@link ot.jcmt.inst.editor.EdCompInstHeterodyne}) to add listeners.
    */
   public JComboBox getFeBandComboBox() {
      return feBand;
   }

   /**
    * Returns frequency editor widget.
    * 
    * Allows other classes (such as {@link ot.jcmt.inst.editor.EdCompInstHeterodyne}) to add listeners.
    */
   public JComboBox getFeModeComboBox() {
      return feMode;
   }

   /**
    * Returns frequency editor widget.
    * 
    * Allows other classes (such as {@link ot.jcmt.inst.editor.EdCompInstHeterodyne}) to add listeners.
    */
   public JComboBox getMoleculeComboBox() {
      return moleculeChoice;
   }

   /**
    * Returns frequency editor widget.
    * 
    * Allows other classes (such as {@link ot.jcmt.inst.editor.EdCompInstHeterodyne}) to add listeners.
    */
   public JComboBox getMolecule2ComboBox() {
      return moleculeChoice2; 
   }

   /**
    * Returns frequency editor widget.
    * 
    * Allows other classes (such as {@link ot.jcmt.inst.editor.EdCompInstHeterodyne}) to add listeners.
    */
   public JComboBox getTransitionComboBox() {
      return transitionChoice;
   }

   /**
    * Returns frequency editor widget.
    * 
    * Allows other classes (such as {@link ot.jcmt.inst.editor.EdCompInstHeterodyne}) to add listeners.
    */
   public JComboBox getTransition2ComboBox() {
      return transitionChoice2;
   }

   /**
    * Returns frequency editor widget.
    * 
    * Allows other classes (such as {@link ot.jcmt.inst.editor.EdCompInstHeterodyne}) to add listeners.    *
    */
   public JButton getSideBandButton() {
      return sideBandButton;
   }

   /**
    * Returns vector with all input widgets of sideBandDisplay and its child components.
    * The input widgets are of type JSlider, JScrollBar and JButton.
    *
    * Allows other classes (such as {@link ot.jcmt.inst.editor.EdCompInstHeterodyne}) to add listeners.
    */
   public Vector getSideBandDisplayWidgets() {
      if(sideBandDisplay != null) {
        return sideBandDisplay.getAllWidgets();
      }
      // if sideBandDisplay is null return an empty Vector.
      else {
        return new Vector();
      }
   }

}
