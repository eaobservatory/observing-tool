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
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.Point;
import java.util.*;
import java.io.*;

import gemini.sp.SpItem;
import orac.jcmt.inst.SpInstHeterodyne;
import edfreq.*;
import jsky.app.ot.editor.OtItemEditor;

//------- NOTES -------------
//
// Transition Strings is GUI and SpInstHeterodyne
// 
// All the transitions Strings in the LineCatalog end with a white space " ".
// The XML methods of SpItem remove this white space when SpInstHeterodyne is
// saved to XML and read back in again. But when the setSelectedItem() method of
// the _w.transitionChoice JComboBox is called with this trimmed String it wound find
// the right String to select.
// It therefore important to make sure that an white space is added to the
// transition String obtained via _instHeterodyne.getTransition(0) before it is used
// in the GUI, e.g.
//   _w.transitionChoice.setSelectedItem(getObject(_w.transitionChoice, _instHeterodyne.getTransition(0) + " "));
//
// And before a transition String fron the GUI is saved to the _instHeterodyne the white space
// should be removed by trimming the String, e.g.
//  _instHeterodyne.setTransition(_w.transitionChoice.getSelectedItem().toString().trim(), 0);
//
// --------------------------------------------------------

/**
 * Heterodyne Editor.
 *
 * This class implements the HeterodyneEditor interface. Its implementation is based
 * on the old edfreq.FrontEnd class which is not used anymore.
 *
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class EdCompInstHeterodyne extends OtItemEditor implements ActionListener,
    DocumentListener, FocusListener, HeterodyneEditor {

  private String currentFE = "";
  private static SideBandDisplay sideBandDisplay = null;
  private LineCatalog lineCatalog = new LineCatalog();
  private double redshift = 0.0;
  private double subBandWidth = 0.25E9;
  private double loMin;
  private double loMax;
  private double feIF;
  private double feBandWidth;
  private double feOverlap = 0.0;

  private static final String FREQ_EDITOR_CFG_PROPERTY = "FREQ_EDITOR_CFG";

  /** Ignore events caused by input widgets of the HeterodyneGUI _w. */
  private boolean _ignoreEvents = false;

  /** Do not use _instHeterodyne if this is true, as it will still be null. */
  private boolean _ignoreSpItem = false;

  private boolean _updatingWidgets = false;

  private boolean _changingFrontEnd = false;
  private boolean _changingBandMode = false;

  /**
   * Flag indicating that the text in the velocity box has been changed but
   * the respective fields in this and other classes have not yet been updated accordingly.
   */
  private boolean _velocityChanged = false;

  /**
   * A static configuration object which can used by classes throughout this
   * package to configure themselves.
   */
  protected static FrequencyEditorCfg cfg = FrequencyEditorCfg.getConfiguration();

  private SpInstHeterodyne _instHeterodyne;

  private HeterodyneGUI _w;		// the GUI layout

  private String [] _radialVelocityDefinitions = {
    SpInstHeterodyne.RADIAL_VELOCITY_REDSHIFT,
    SpInstHeterodyne.RADIAL_VELOCITY_OPTICAL,
    SpInstHeterodyne.RADIAL_VELOCITY_RADIO
  };


  public EdCompInstHeterodyne() {
    _title       ="JCMT Heterodyne";
    _presSource  = _w = new HeterodyneGUI();
    _description ="The Heterodyne instrument is configured with this component.";

      _ignoreSpItem = true;

      if(sideBandDisplay == null) {
         sideBandDisplay = new SideBandDisplay(this);
      }

      _w.feChoice.setModel(new DefaultComboBoxModel(cfg.frontEnds));
      _w.feChoice.addActionListener ( this );

      _w.feMode.setModel(new DefaultComboBoxModel((String[])cfg.frontEndTable.get(_w.feChoice.getItemAt(0))));
      _w.feMode.addActionListener( this );
      _w.feBandModeChoice.addActionListener ( this );
      _w.feMixers.setModel(new DefaultComboBoxModel((String[])cfg.frontEndMixers.get(_w.feChoice.getItemAt(0))));
      _w.feMixers.addActionListener(this);

      _w.overlap.setText ( "0.0" );
      _w.overlap.addActionListener ( this );

      // For now the user should not be allowed to change the overlap.
      _w.overlap.setEnabled(false);

/* Create the display */

      _w.velocity.setText ( "0.0" );
      _w.velocity.addActionListener ( this );
      _w.velocity.addFocusListener ( this );
      _w.velocity.getDocument().addDocumentListener( this );

      _w.velocityDefinition.setModel(new DefaultComboBoxModel(_radialVelocityDefinitions));
      _w.velocityDefinition.addActionListener ( this );

      _w.velocityFrame.setModel(new DefaultComboBoxModel(cfg.velocityFrames));
      _w.velocityFrame.addActionListener ( this );

      _w.feBand.setModel(new DefaultComboBoxModel(new String[] { "best", "usb", "lsb" } ));
      _w.feBand.addActionListener ( this );

/* Main molecular line choice - used to set front-end LO1 to put the line
   in the nominated sideband */

      _w.moleculeChoice.addActionListener ( this );
      _w.transitionChoice.addActionListener ( this );
      _w.moleculeFrequency.setText ( "0.0000" );
      _w.moleculeFrequency.addActionListener(this);
      _w.bandWidthChoice.addActionListener( this );

/* Secondary moleculare line choice - displayed just for convenience of
   astronomer */

      _w.moleculeChoice2.addActionListener ( this );
      _w.transitionChoice2.addActionListener ( this );
      _w.moleculeFrequency2.setText ( "0.0000" );
      _w.moleculeFrequency2.addActionListener(this);


      _w.velocityDefinition.addActionListener(this);

      _w.freqEditorButton.addActionListener(this);
      _w.hideFreqEditorButton.addActionListener(this);

   
      // MFO trigger additional initialising.
      feChoiceAction(null);
      updateSideBandDisplay();
      //feMolecule2Action(null);
      feMoleculeAction(null);
      feTransitionAction(null);

      _ignoreSpItem = false;
   }

   /** Initialises the default values in SpInstHeterodyne. */
   private void _initialiseInstHeterodyne() {

      String   frontEndName = cfg.frontEnds[0];
      Receiver receiver     = (Receiver)cfg.receivers.get(frontEndName);
      BandSpec bandSpec     = (BandSpec)(receiver.bandspecs.get(0));

      // Get hold of the lines in the upper sideband that. Make sure it is not to close to
      // the edge of the range so that the IF can default to the frontend IF.
      // One of the lines should be a CO transition. Find it it use it as default line.
      Vector moleculeVector = lineCatalog.returnSpecies(receiver.loMin + receiver.feIF + receiver.bandWidth,
                                                        receiver.loMax                 - receiver.bandWidth);
      String molecule       = "CO";
      String transitionName = "";
      String frequency      = "";

      Transition transition = null;

      for(int i = 0; i < moleculeVector.size(); i++) {
         if(moleculeVector.get(i).toString().trim().equals(molecule)) {
            transition = (Transition)((SelectionList)moleculeVector.get(i)).objectList.get(0);
         }
      }

      if(transition != null) {

         // Whenever a trinsition is taken from the LineCatalog and consequently saved to
         // it has to be trimmed in order to remove the trailing white space that each transition
         // in the LineCatalog has.
         transitionName = transition.name.trim();
         frequency      = "" + transition.frequency;
      }

      // Find out which backend we are using
      String beName = System.getProperty("FREQ_EDITOR_CFG");
      if (beName.indexOf("das") != -1) {
	  beName = "das";
      }
      else {
	  beName = "acsis";
      }
 
      _instHeterodyne.initialiseValues(
	 beName,                                                // Back end name
         frontEndName,						// front end
         ((String[])cfg.frontEndTable.get(frontEndName))[0],	// mode
         bandSpec.toString(),					// band mode
         ((String[])cfg.frontEndMixers.get(frontEndName))[0],	// mode
         "" + bandSpec.defaultOverlaps[0],			// overlap
         "0.0",							// velocity
         SpInstHeterodyne.RADIAL_VELOCITY_RADIO,		// velocity definitio
	 SpInstHeterodyne.LSR_VELOCITY_FRAME,                   // velocity frame
         "best",						// band
         "" + receiver.feIF,					// centre frequency
         "" + bandSpec.getDefaultOverlapBandWidths()[0],	// bandwidth
         "" + bandSpec.channels[0],				// channels
         molecule,						// molecule
         transitionName,					// transition
         frequency						// rest frequency
      );
   }

  /**
   * Override setup to store away a reference to the SpInstCGS4 item.
   */
  public void setup(SpItem spItem) {
    _instHeterodyne = (SpInstHeterodyne)spItem;

    super.setup(spItem);
  }


  /**
   * Implements the _updateWidgets method from OtItemEditor in order to
   * setup the widgets to show the current values of the item.
   */
  protected void _updateWidgets() {
      _ignoreSpItem = true;
      _updatingWidgets = true;

      if(!_instHeterodyne.valuesInitialised()) {
         _initialiseInstHeterodyne();
      }

      try {
         _w.feChoice.setSelectedItem(_instHeterodyne.getFrontEnd());
         _w.feMode.setSelectedItem(_instHeterodyne.getMode());
         _w.feBandModeChoice.setSelectedItem(getObject(_w.feBandModeChoice, _instHeterodyne.getBandMode()));
         _w.feMixers.setSelectedItem(getObject(_w.feMixers, _instHeterodyne.getMixer()));
         _w.overlap.setText("" + (_instHeterodyne.getOverlap(0) / 1.0E6));
         _w.velocityDefinition.setSelectedItem("" + _instHeterodyne.getVelocityDefinition());
	 _w.velocityFrame.setSelectedItem(_instHeterodyne.getVelocityFrame());

         redshift = _instHeterodyne.getRedshift();
         _updateVelocityTextField(_instHeterodyne.getVelocityDefinition());

         _w.feBand.setSelectedItem(getObject(_w.feBand, _instHeterodyne.getBand()));

         for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
           sideBandDisplay.setCentreFrequency(_instHeterodyne.getCentreFrequency(i), i);

           if(i == 0) {
	      boolean tmpIgnoreEvents = _ignoreEvents;
	      _ignoreEvents = true;
              _w.bandWidthChoice.setSelectedItem(getObject(_w.bandWidthChoice, "" + (_instHeterodyne.getBandWidth(0) / 1.0E9)));
	      if ( ((String)_w.bandWidthChoice.getSelectedItem()).equals("1.84") ) {
		  _w.feMixers.setEnabled(false);
	      }
	      else {
		  _w.feMixers.setEnabled(true);
	      }
              _ignoreEvents = tmpIgnoreEvents;
	   }

           sideBandDisplay.setBandWidth(_instHeterodyne.getBandWidth(i), i);
           sideBandDisplay.setCentreFrequency(_instHeterodyne.getCentreFrequency(i), i);

           if(i > 0) {
              sideBandDisplay.setLineText(_instHeterodyne.getMolecule(i) + "  " +
                                          _instHeterodyne.getTransition(i) + "  " +
                                          _instHeterodyne.getRestFrequency(i), i);
           }
         }


         _w.moleculeChoice.setSelectedItem(getObject(_w.moleculeChoice, _instHeterodyne.getMolecule(0)));

         // Whenever a transition is obtained from via _instHeterodyne.getTransition(int) a white space must
         // be added so that the transition String matches the format of the transition Strings in the
         // LineCatalog.
         _w.transitionChoice.setSelectedItem(getObject(_w.transitionChoice, _instHeterodyne.getTransition(0) + " "));
         _w.moleculeFrequency.setText("" + (_instHeterodyne.getRestFrequency(0) / 1.0E6));

         _w.resolution.setText("" + sideBandDisplay.getResolution(0));
      }
      catch(Exception e) {
         e.printStackTrace();
         System.out.println("The Heterodyne Editor (EdCompInstHeterodyne) widgets could not be updated\n" +
	                    "from the Heterodyne item (SpInstHeterodyne). This can happen if the\n" + 
                            "Heterodyne item table does not contain default values.");
      }

      _ignoreSpItem = false;
      _updatingWidgets = false;
  }


   public void actionPerformed ( ActionEvent ae )
   {
      if(ae.getSource() == _w.freqEditorButton) {
         sideBandDisplay.setVisible(true);
      }

      if(ae.getSource() == _w.hideFreqEditorButton) {
         sideBandDisplay.setVisible(false);
      }

      if(_ignoreEvents) {
         return;
      }

      if ( ae.getSource() == _w.feBand )
      {
         feBandAction ( ae );
      }
      else if ( ae.getSource() == _w.moleculeChoice )
      {
         feMoleculeAction ( ae );
      }
      else if ( ae.getSource() == _w.transitionChoice )
      {
         feTransitionAction ( ae );
      }
      else if ( ae.getSource() == _w.moleculeChoice2 )
      {
         feMolecule2Action ( ae );
      }
      else if ( ae.getSource() == _w.transitionChoice2 )
      {
         feTransition2Action ( ae );
      }
      else if ( ae.getSource() == _w.feChoice )
      {
         feChoiceAction ( ae );
      }
      else if ( ae.getSource() == _w.feBandModeChoice )
      {
         feBandModeChoiceAction ( ae );
      }
      else if ( ae.getSource() == _w.feMixers )
      {
         feMixersAction ( ae );
      }
      else if (ae.getSource() == _w.velocityFrame) {
	  velocityFrameAction(ae);
      }
      else if ( ae.getSource() == _w.velocity )
      {
         feVelocityAction ( ae );
      }
      else if ( ae.getSource() == _w.velocityDefinition )
      {
         feVelocityDefinitionAction ( ae );
      }
      else if ( ae.getSource() == _w.overlap )
      {
         feOverlapAction ( ae );
      }
      else if ( ae.getSource() == _w.moleculeFrequency )
      {
         moleculeFrequencyChanged();
      }
      else if ( ae.getSource() == _w.moleculeFrequency2 )
      {
         moleculeFrequency2Changed();
      }
      else if ( ae.getSource() == _w.bandWidthChoice )
      {
         bandWidthChoiceAction(ae);
      }
      else if ( ae.getSource() == _w.feMode )
      {
         if(!_ignoreSpItem) {
	    _instHeterodyne.setMode((String)_w.feMode.getSelectedItem());
	 }

         sideBandDisplay.resetModeAndBand((String)_w.feMode.getSelectedItem(), (String)_w.feBand.getSelectedItem());
      }
   }


   public void bandWidthChoiceAction ( ActionEvent ae ) {
      for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
         if(!_ignoreSpItem) {
            _instHeterodyne.setBandWidth(Double.parseDouble((String)_w.bandWidthChoice.getSelectedItem()) * 1.0E9, i);
	 }
	 // Special check for wideband mode
	 if (((String)_w.bandWidthChoice.getSelectedItem()).equals("1.84")) {
	     _w.feMixers.setSelectedIndex(0);
	     _w.feMixers.setEnabled(false);
	 }
	 else {
	     _w.feMixers.setEnabled(true);
	 }
         sideBandDisplay.setBandWidth(_instHeterodyne.getBandWidth(i), i);
      }

      BandSpec currentBandSpec = (BandSpec)_w.feBandModeChoice.getSelectedItem();
      feOverlap = currentBandSpec.defaultOverlaps[_w.bandWidthChoice.getSelectedIndex()];
      _w.overlap.setText("" + (feOverlap / 1.0E6));

      if(!_ignoreSpItem) {
         for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
            _instHeterodyne.setOverlap(feOverlap, i);
	 }
      }

// For now the user should not be allowed to change the overlap. So leave the _w.overlap disabled.
//      if(((BandSpec)_w.feBandModeChoice.getSelectedItem()).getNumHybridSubBands(_w.bandWidthChoice.getSelectedIndex()) > 1) {
//         _w.overlap.setEnabled(true);
//      }
//      else {
//         _w.overlap.setEnabled(false);
//      }
   }


   public void feBandAction ( ActionEvent ae )
   {
      feTransitionAction ( ae );

      if(!_ignoreSpItem) {
         _instHeterodyne.setBand((String)_w.feBand.getSelectedItem());

         double centreFrequency;

	 for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
            _instHeterodyne.setCentreFrequency((2.0 * feIF) - _instHeterodyne.getCentreFrequency(i), i);
	 }
      }

      sideBandDisplay.resetModeAndBand((String)_w.feMode.getSelectedItem(), (String)_w.feBand.getSelectedItem());

      for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
        sideBandDisplay.setCentreFrequency(_instHeterodyne.getCentreFrequency(i), i);
        sideBandDisplay.setBandWidth(_instHeterodyne.getBandWidth(i), i);

        if(i > 0) {
           sideBandDisplay.setLineText(_instHeterodyne.getMolecule(i) + "  " +
                                       _instHeterodyne.getTransition(i) + "  " +
                                       _instHeterodyne.getRestFrequency(i), i);
         }
      }
   }

    public void feMixersAction (ActionEvent ae) {
	_instHeterodyne.setMixer((String)_w.feMixers.getSelectedItem());
    }

    public void velocityFrameAction (ActionEvent ae) {
	_instHeterodyne.setVelocityFrame((String)_w.velocityFrame.getSelectedItem());
    }


   public void feMoleculeAction ( ActionEvent ae )
   {
      if(_w.moleculeChoice.getSelectedItem() instanceof SelectionList) {
         SelectionList species = (SelectionList)_w.moleculeChoice.getSelectedItem();

         String transition  = null;
         String oldMolecule = null;

         if(!_ignoreSpItem) {
            transition  = _instHeterodyne.getTransition(0);

            if(transition != null) {
               transition += " ";
            }

            oldMolecule = _instHeterodyne.getMolecule(0);
	 }

         _w.transitionChoice.setModel ( 
           new DefaultComboBoxModel ( species.objectList ) );

         _ignoreEvents = true;
         if(((DefaultComboBoxModel)_w.transitionChoice.getModel()).getIndexOf(NO_LINE) == -1) {
            _w.transitionChoice.addItem(NO_LINE + " ");
         }

         // Check whether the transition previously selected is still in range.
         // If so, set it to the previous transition. Warn the user otherwise.
         if((transition == null) || (!oldMolecule.equals(_w.moleculeChoice.getSelectedItem().toString()))) {
            _w.transitionChoice.setSelectedIndex(0);
         }
         else {
            boolean transitionInRange = false;

            for(int i = 0; i < _w.transitionChoice.getItemCount(); i++) {
               if(_w.transitionChoice.getItemAt(i).toString().equals(transition)) {
                  transitionInRange = true;
	          break;
	       }
            }

            if(transitionInRange) {
               _w.transitionChoice.setSelectedItem(getObject(_w.transitionChoice, transition));
            }
            else {
               _w.transitionChoice.setSelectedIndex(0);

               if(!_updatingWidgets) {
                  JOptionPane.showMessageDialog(_w, "Transition changed: " + transition + " out of range.",
                  "Transition changed", JOptionPane.WARNING_MESSAGE);
               }
            }
         }

         _ignoreEvents = false;

         _w.moleculeFrequency.setText ( "0.0000" );

         _ignoreEvents = true;
         feTransitionAction(null);
         _ignoreEvents = false;
      }

      if(!_ignoreSpItem) {
         for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
            _instHeterodyne.setMolecule(_w.moleculeChoice.getSelectedItem().toString(), i);
         }
      }

      // Skip top system, start with i = 1
      for(int i = 1; i < sideBandDisplay.getNumSubSystems(); i++) {
         sideBandDisplay.setLineText(_instHeterodyne.getMolecule(i) + "  " +
                                     _instHeterodyne.getTransition(i) + "  " +
                                     _instHeterodyne.getRestFrequency(i), i);
      }
   }


   public void feMolecule2Action ( ActionEvent ae )
   {
      SelectionList species = (SelectionList)_w.moleculeChoice2.getSelectedItem();

      _w.transitionChoice2.setModel ( 
        new DefaultComboBoxModel ( species.objectList ) );

      _ignoreEvents = true;
      if(((DefaultComboBoxModel)_w.transitionChoice2.getModel()).getIndexOf(NO_LINE) == -1) {
         _w.transitionChoice2.addItem(NO_LINE + " ");
      }
      _w.transitionChoice2.setSelectedIndex(0);
      _ignoreEvents = false;

      _w.moleculeFrequency2.setText ( "0.0000" );

      _ignoreEvents = true;
      feTransition2Action(null);
      _ignoreEvents = false;
   }


   public void feTransitionAction ( ActionEvent ae )
   {
      _resetTransition();
      _resetAdditionalSubSystems();
   }

   private void _resetTransition()
   {

      if(!(_w.transitionChoice.getSelectedItem() instanceof Transition)) {
         return;
      }

      Transition transition = (Transition)_w.transitionChoice.getSelectedItem();

      if ( transition != null )
      {

         _w.moleculeFrequency.setText ( "" + transition.frequency/1.0E6 );
         String band = (String)_w.feBand.getSelectedItem();

         if ( sideBandDisplay != null )
         {

            sideBandDisplay.setMainLine ( transition.frequency );


            double obsFrequency = transition.frequency / 
              ( 1.0 + redshift );


            // Adjust the LO1. If this would mean exceeding the LO range then
            // swap sidebands.
            if ( band.equals ( "usb" ) || band.equals("best") )
            {
	       if((obsFrequency - sideBandDisplay.getTopSubSystemCentreFrequency()) < loMin) {

                  if((!_updatingWidgets) && (!_changingFrontEnd) && (!_changingBandMode)) {
                     JOptionPane.showMessageDialog(_w, "Using lower sideband in order to reach line.",
                     "Changing sideband", JOptionPane.WARNING_MESSAGE);
                  }

                  _w.feBand.setSelectedItem("lsb");
                  sideBandDisplay.setLO1 ( obsFrequency + sideBandDisplay.getTopSubSystemCentreFrequency() );
	       }
	       else {
                  sideBandDisplay.setLO1 ( obsFrequency - sideBandDisplay.getTopSubSystemCentreFrequency() );
               }
            }
            else
            {
	       if((obsFrequency + sideBandDisplay.getTopSubSystemCentreFrequency()) > loMax) {

                  if((!_updatingWidgets) && (!_changingFrontEnd) && (!_changingBandMode)) {
                     JOptionPane.showMessageDialog(_w, "Using upper sideband in order to reach line.",
                     "Changing sideband", JOptionPane.WARNING_MESSAGE);
                  }

                  _w.feBand.setSelectedItem("best");
                  sideBandDisplay.setLO1 ( obsFrequency - sideBandDisplay.getTopSubSystemCentreFrequency() );
	       }
	       else {
                  sideBandDisplay.setLO1 ( obsFrequency + sideBandDisplay.getTopSubSystemCentreFrequency() );
               }
            }

            // If the LO range would still be exceeded then adjust the centre frequency of the top system.
            // Set band to the new value as it might have been swapped above.
            band = (String)_w.feBand.getSelectedItem();
            boolean stillOutOfRange = false;


            if ( band.equals ( "lsb" ) )
            {
	       if((obsFrequency - sideBandDisplay.getTopSubSystemCentreFrequency()) < loMin) {
                  sideBandDisplay.setCentreFrequency(Math.abs(obsFrequency - sideBandDisplay.getLO1()), 0);
               }
            }
            else
            {
	       if((obsFrequency + sideBandDisplay.getTopSubSystemCentreFrequency()) > loMax) {
                  sideBandDisplay.setCentreFrequency(Math.abs(obsFrequency - sideBandDisplay.getLO1()), 0);
	       }
            }

            sideBandDisplay.setCentreFrequency(sideBandDisplay.getTopSubSystemCentreFrequency(), 0);

            if(!_ignoreSpItem) {
               _instHeterodyne.setMolecule(_w.moleculeChoice.getSelectedItem().toString(), 0);
               _instHeterodyne.setTransition(_w.transitionChoice.getSelectedItem().toString().trim(), 0);
               _instHeterodyne.setRestFrequency(transition.frequency, 0);
               _instHeterodyne.setCentreFrequency(sideBandDisplay.getTopSubSystemCentreFrequency(), 0);
            }
         }
      }
   }

   /**
    * Sets the centre frequencies of the remaining subsystems to that of the top subsystem.
    */
   private void _resetAdditionalSubSystems()
   {
      if(!(_w.transitionChoice.getSelectedItem() instanceof Transition)) {
         return;
      }

      Transition transition = (Transition)_w.transitionChoice.getSelectedItem();

      for(int i = 1; i < sideBandDisplay.getNumSubSystems(); i++) {
         sideBandDisplay.setCentreFrequency(sideBandDisplay.getTopSubSystemCentreFrequency(), i);
      }


      if(!_ignoreSpItem) {
         for(int i = 1; i < sideBandDisplay.getNumSubSystems(); i++) {
            _instHeterodyne.setMolecule(_w.moleculeChoice.getSelectedItem().toString(), i);
            _instHeterodyne.setTransition(_w.transitionChoice.getSelectedItem().toString().trim(), i);
            _instHeterodyne.setRestFrequency(transition.frequency, i);
            _instHeterodyne.setCentreFrequency(sideBandDisplay.getTopSubSystemCentreFrequency(), i);
         }
      }

      // Skip top system, start with i = 1
      for(int i = 1; i < sideBandDisplay.getNumSubSystems(); i++) {
         sideBandDisplay.setLineText(_instHeterodyne.getMolecule(i) + "  " +
                                     _instHeterodyne.getTransition(i) + "  " +
                                     _instHeterodyne.getRestFrequency(i), i);
      }
   }


   public void feTransition2Action ( ActionEvent ae )
   {
      if(!(_w.transitionChoice2.getSelectedItem() instanceof Transition)) {
         return;
      }

      Transition transition = (Transition)_w.transitionChoice2.getSelectedItem();

      if ( transition != null )
      {

         _w.moleculeFrequency2.setText ( "" + transition.frequency/1.0E6 );

         if ( sideBandDisplay != null )
         {

            sideBandDisplay.setSideLine ( transition.frequency );

         }

      }
   }


   public void moleculeFrequencyChanged()
   {
      try {
         double frequency = Double.parseDouble(_w.moleculeFrequency.getText()) * 1.0E6;

	 String band = (String)_w.feBand.getSelectedItem();

         if ( sideBandDisplay != null )
         {

            sideBandDisplay.setMainLine ( frequency );

            double obsFrequency = frequency / 
              ( 1.0 + redshift );
            if ( band.equals ( "usb" ) || band.equals("best") )
            {
               sideBandDisplay.setLO1 ( obsFrequency - sideBandDisplay.getTopSubSystemCentreFrequency() );
            }
            else
            {
               sideBandDisplay.setLO1 ( obsFrequency + sideBandDisplay.getTopSubSystemCentreFrequency() );
            }
         }

         for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
            updateLineDetails(null, i);

            if(i > 0) {
               sideBandDisplay.setLineText(NO_LINE, i);
            }
         }
      }
      catch(NumberFormatException e) {
        // ignore
      }
   }


   public void moleculeFrequency2Changed()
   {
      try {
         double frequency = Double.parseDouble(_w.moleculeFrequency2.getText()) * 1.0E6;
      
         if ( sideBandDisplay != null )
         {
            sideBandDisplay.setSideLine ( frequency );

         }

      }
      catch(NumberFormatException e) {
        // ignore
      }
   }


   public void feBandModeChoiceAction ( ActionEvent ae ) {
      _changingBandMode = true;

      _ignoreEvents = true;
      try {
         _w.bandWidthChoice.setSelectedIndex(0);
      }
      catch(Exception e) {
         // Exception occurs _w.bandWidthChoice has no items yet. Ignore.
      }
      _ignoreEvents = false;

      BandSpec currentBandSpec = (BandSpec)_w.feBandModeChoice.getSelectedItem();
      _updateBandWidthChoice(currentBandSpec.getDefaultOverlapBandWidths());

      feOverlap = currentBandSpec.defaultOverlaps[_w.bandWidthChoice.getSelectedIndex()];
      _w.overlap.setText("" + (feOverlap/ 1.0E6));

// For now the user should not be allowed to change the overlap. So leave the _w.overlap disabled.
//      if(currentBandSpec.getNumHybridSubBands(_w.bandWidthChoice.getSelectedIndex()) > 1) {
//         _w.overlap.setEnabled(true);
//      }
//      else {
//         _w.overlap.setEnabled(false);
//      }

      updateSideBandDisplay();

      if(!_ignoreSpItem) {

         _instHeterodyne.setBandMode(_w.feBandModeChoice.getSelectedItem().toString());

         for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
            _instHeterodyne.setMolecule(_w.moleculeChoice.getSelectedItem().toString(), i);
            _instHeterodyne.setTransition(_w.transitionChoice.getSelectedItem().toString().trim(), i);
            _instHeterodyne.setCentreFrequency(feIF, i);
            //_instHeterodyne.setBandWidth(currentBandSpec.getBandWidths(feOverlap)[0], i);
            //_instHeterodyne.setChannels(currentBandSpec.getChannels(feOverlap)[0], i);
            _instHeterodyne.setBandWidth(currentBandSpec.getDefaultOverlapBandWidths()[0], i);
            _instHeterodyne.setChannels(currentBandSpec.getDefaultOverlapChannels()[0], i);
	    _instHeterodyne.setRestFrequency(getRestFrequency(i), i);
            _instHeterodyne.setOverlap(feOverlap, i); 

            // Skip to top system on sideBandDisplay
            if(i > 0) {
               sideBandDisplay.setLineText(_instHeterodyne.getMolecule(i) + "  " +
	                                   _instHeterodyne.getTransition(i) + "  " +
                                           _instHeterodyne.getRestFrequency(i), i);
            }
	 }
      }

      _changingBandMode = false;
   }

   public void updateSideBandDisplay() {
      double loRange[];
      double mid;
      int subBandCount;
      BandSpec currentBandSpec = (BandSpec)_w.feBandModeChoice.getSelectedItem();

      if(currentBandSpec == null) {
         _w.feBandModeChoice.setSelectedIndex(0);
	 currentBandSpec = (BandSpec)_w.feBandModeChoice.getSelectedItem();
      }


/* Update display of sidebands and subbands */
      Point sideBandDisplayLocation = new Point(100, 100);

      mid = 0.5 * ( loMin + loMax );

      // subBandCount and numBands refers to the number of subsystem, not to the number
      // of multiple subbands in one subsystem.
      subBandCount = currentBandSpec.numBands;
      subBandWidth = currentBandSpec.getDefaultOverlapBandWidths()[0]; //getBandWidths(feOverlap)[0]; //currentBandSpec.bandWidths[0];

      sideBandDisplay.updateDisplay ( currentFE, loMin, loMax,
        feIF, feBandWidth,
        redshift,
        currentBandSpec.getDefaultOverlapBandWidths(), //getBandWidths(feOverlap),
        currentBandSpec.getDefaultOverlapChannels(),   //getChannels(  feOverlap),
        subBandCount );

      _ignoreEvents = true;
      sideBandDisplay.resetModeAndBand((String)_w.feMode.getSelectedItem(), (String)_w.feBand.getSelectedItem());
      _ignoreEvents = false;

      feTransitionAction(null);
   }


   public void feChoiceAction ( ActionEvent ae )
   {
      _changingFrontEnd = true;

      double loRange[];
      double mid;
      int subBandCount;
      Receiver r;
      double obsmin;
      double obsmax;

      String newFE = (String)_w.feChoice.getSelectedItem();
      currentFE = newFE;
      r = (Receiver)cfg.receivers.get ( currentFE );

      loMin = r.loMin;
      loMax = r.loMax;
      feIF = r.feIF;
      feBandWidth = r.bandWidth;

      _w.lowFreq.setText ( "" + (int)(loMin*1.0E-9) );
      _w.highFreq.setText ( "" + (int)(loMax*1.0E-9) );

/* Update frontend mode choices */
      _w.feMode.removeActionListener(this);
      _w.feMode.setModel(new DefaultComboBoxModel((String[])cfg.frontEndTable.get(newFE)));
      _w.feMode.addActionListener(this);

      _w.feMixers.removeActionListener(this);
      _w.feMixers.setModel(new DefaultComboBoxModel((String[])cfg.frontEndMixers.get(newFE)));
      _w.feMixers.addActionListener(this);

/* Update choice of sub-band configurations */

      _w.feBandModeChoice.removeActionListener(this);
      _w.feBandModeChoice.setModel ( 
        new DefaultComboBoxModel ( r.bandspecs ) );
      _w.feBandModeChoice.addActionListener(this);
      _w.feBandModeChoice.setSelectedIndex(0);

      obsmin = loMin - feIF - ( feBandWidth * 0.5 );
      obsmax = loMax + feIF + ( feBandWidth * 0.5 );

/* Update choice of molecules */

      _w.moleculeChoice.removeActionListener(this);
      _w.moleculeChoice.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
          obsmax*(1.0+redshift) ) ) );

      _ignoreEvents = true;
      if(((DefaultComboBoxModel)_w.moleculeChoice.getModel()).getIndexOf(NO_LINE) == -1) {
         _w.moleculeChoice.addItem(NO_LINE);
      }
      _ignoreEvents = false;

      _w.moleculeChoice.addActionListener(this);
      _w.moleculeChoice.setSelectedIndex(0); // MFO: This line used to be after, then before the one
                                           // where the ActionListener is added again
					   // but that (sometimes) caused a NullPointerException.
					   // Now its after the adding of the ActionListener.
					   // If there are still NullPointerException then
					   // something else has do be done that selectes a line when
					   // the EdCompInstHeterodyne is created.

      _w.moleculeChoice2.removeActionListener(this);
      _w.moleculeChoice2.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
        obsmax*(1.0+redshift) ) ) );

      _ignoreEvents = true;
      if(((DefaultComboBoxModel)_w.moleculeChoice2.getModel()).getIndexOf(NO_LINE) == -1) {
         _w.moleculeChoice2.addItem(NO_LINE);
      }
      _w.moleculeChoice2.setSelectedIndex(0);
      _ignoreEvents = false;
	
      _w.moleculeChoice2.setSelectedIndex(0); // MFO: This line used to be after the one
                                           // where the ActionListener is added again
					   // but that (sometimes) caused a NullPointerException.
					   // Try doing something else that selectes a line when
					   // the EdCompInstHeterodyne is created.
      _w.moleculeChoice2.addActionListener(this);

/* Reset line frequency report */

      _w.moleculeFrequency.setText ( "0.0000" );
//      moleculeFrequency2.setText ( "0.0000" );


/* Update display of sidebands and subbands */
      updateSideBandDisplay();

      if(!_ignoreSpItem) {
         _instHeterodyne.setFrontEnd(newFE);
	 _instHeterodyne.setMode((String)_w.feMode.getSelectedItem());
// 	 _instHeterodyne.setBandMode((String)_w.feBandModeChoice.getSelectedItem());
	 _instHeterodyne.setMixer((String)_w.feMixers.getSelectedItem());
	 _instHeterodyne.setBand((String)_w.feBand.getSelectedItem());
      }

      _changingFrontEnd = false;
   }


   public void feVelocityAction ( ActionEvent ae )
   {
      String svalue;
      double dvalue;
      double obsmin;
      double obsmax;

      svalue = _w.velocity.getText();
      dvalue = (Double.valueOf(svalue)).doubleValue();

      String velocityDefinition = _instHeterodyne.getVelocityDefinition();

      redshift = SpInstHeterodyne.convertToRedshift(velocityDefinition, dvalue);

      obsmin = loMin - feIF - ( feBandWidth * 0.5 );
      obsmax = loMax + feIF + ( feBandWidth * 0.5 );


      if(((Vector)lineCatalog.returnSpecies(obsmin*(1.0+redshift), obsmax*(1.0+redshift))).size() < 1) {

         if(!_updatingWidgets) {
            JOptionPane.showMessageDialog(_w, "This velocity/redshift would results frequency range that exceeds the line catalog.",
                                       "Invalid velocity/redshift", JOptionPane.WARNING_MESSAGE);
         }

         // Reset redshift and value in velocity text field to last valid value.
	 redshift = _instHeterodyne.getRedshift();
         _updateVelocityTextField(_instHeterodyne.getVelocityDefinition());

         return;
      }

      _velocityChanged = false;

/* Update choice of molecules */

      _ignoreEvents = true;

      _w.moleculeChoice.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
          obsmax*(1.0+redshift) ) ) );

      if(((DefaultComboBoxModel)_w.moleculeChoice.getModel()).getIndexOf(NO_LINE) == -1) {
         _w.moleculeChoice.addItem(NO_LINE);
      }

      _w.moleculeChoice2.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
        obsmax*(1.0+redshift) ) ) );

      _ignoreEvents = false;

      // Now check whether the molecule previously selected is still in range.
      // If so, set it to the previous molecule. Warn the user other wise.
      boolean moleculeInRange = false;
      String  molecule = _instHeterodyne.getMolecule(0);

      for(int i = 0; i < _w.moleculeChoice.getItemCount(); i++) {
         if(_w.moleculeChoice.getItemAt(i).toString().equals(molecule)) {
//            _w.moleculeChoice.setSelectedItem(molecule);
            moleculeInRange = true;
	    break;
	 }
      }

      if(moleculeInRange) {
         _w.moleculeChoice.setSelectedItem(getObject(_w.moleculeChoice, molecule));
      }
      else {
         _w.moleculeChoice.setSelectedIndex(0);

         if(!_updatingWidgets) {
            JOptionPane.showMessageDialog(_w, "Molecule changed: " + molecule + " out of range.",
            "Molecule changed", JOptionPane.WARNING_MESSAGE);
         }
      }

/* Update display of sidebands */

      if ( sideBandDisplay != null )
      {
         sideBandDisplay.setRedshift ( redshift );
      }

      if(!_ignoreSpItem) {
         _instHeterodyne.setVelocityFromRedshift(redshift);
	 _instHeterodyne.setRefFrameVelocity(dvalue);
      }
   }

   public void feVelocityDefinitionAction ( ActionEvent ae ) {
      // The call to feVelocityAction() ensures that the current value in
      // the _w.velocity text field is registered even if the user
      // has never hit return (i.e. causing an ActionEvent) in the _w.velocity text field.

      if(_velocityChanged) {
         feVelocityAction( null );
      }

      // After the feVelocityAction() call this.redshift is updated.
      _updateVelocityTextField((String)_w.velocityDefinition.getSelectedItem());

      if(!_ignoreSpItem) {
         _instHeterodyne.setVelocityDefinition((String)_w.velocityDefinition.getSelectedItem());
      }
   }


   public void feOverlapAction ( ActionEvent ae )
   {
      String svalue;
      double dvalue;

      svalue = _w.overlap.getText();
      dvalue = (Double.valueOf(svalue)).doubleValue();
      feOverlap = 1.0E6 * dvalue;

      if(!_ignoreSpItem) {
         for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
            _instHeterodyne.setOverlap(feOverlap, i);
	 }
      }

      BandSpec currentBandSpec = (BandSpec)_w.feBandModeChoice.getSelectedItem();

      _updateBandWidthChoice(currentBandSpec.getBandWidths(feOverlap));

      updateSideBandDisplay();

      bandWidthChoiceAction(null);
   }


   private Object getObject(JComboBox comboBox, String name) {
     for(int i = 0; i < comboBox.getItemCount(); i++) {
       if(comboBox.getItemAt(i).toString().equals(name)) {
         return comboBox.getItemAt(i);
       }
     }

     return null;
   }

   public void setSideBandDisplayVisible(boolean visible) {
      sideBandDisplay.setVisible(visible);
   }

   public void setSideBandDisplayLocation(int x, int y) {
      sideBandDisplay.setLocation(x, y);
   }

   // Added by MFO (8 January 2002)
   /**
    * Returns "usb" (Upper Side Band) or "lsb" (Lower Side Band).
    *
    * Needed by {@link edfreq.SideBand} to shift LO1 when top SideBands are changed.
    */
   public String getFeBand() {
      return (String)_w.feBand.getSelectedItem();
   }

   public String getMode() {
      return (String)_w.feMode.getSelectedItem();
   }

   private void _updateBandWidthChoice(double [] values) {
      _w.bandWidthChoice.removeActionListener(this);
      int selectedIndex = _w.bandWidthChoice.getSelectedIndex();
      _w.bandWidthChoice.removeAllItems();

      for(int i = 0; i < values.length; i++) {
         _w.bandWidthChoice.addItem("" + (Math.rint(values[i] * 1.0E-6) / 1000.0));
      }

      if(selectedIndex > -1) {
        _w.bandWidthChoice.setSelectedIndex(selectedIndex);
      }

      _w.bandWidthChoice.addActionListener(this);
   }

   public void _updateVelocityTextField(String velocityDefinition) {
//       double velocityOrRedshift = SpInstHeterodyne.convertRedshiftTo(velocityDefinition, redshift);

//       if(velocityDefinition.equals(SpInstHeterodyne.RADIAL_VELOCITY_REDSHIFT)) {
//          _w.velocity.setText("" + (Math.rint(velocityOrRedshift * 1.0E9) / 1.0E9));
//       }
//       else {
//          _w.velocity.setText("" + (Math.rint(velocityOrRedshift * 1.0E3) / 1.0E3));
//       }
       _w.velocity.setText(""+_instHeterodyne.getVelocity() );
       _w.velocity.setCaretPosition(0);
   }

   public double getRedshift() {
      return redshift;
   }

   // See edfreq.HeterodyneEditor for documentation
   public double getRestFrequency(int subsystem) {
      return EdFreq.getRestFrequency(sideBandDisplay.getLO1(),
                                     _instHeterodyne.getCentreFrequency(subsystem),
                                     _instHeterodyne.getRedshift(),
                                     _instHeterodyne.getBand());
   }

   // See edfreq.HeterodyneEditor for documentation
   public double getObsFrequency(int subsystem) {
      return EdFreq.getObsFrequency(sideBandDisplay.getLO1(),
                                    _instHeterodyne.getCentreFrequency(subsystem),
                                    _instHeterodyne.getBand());
   }


  /** Get receiver's central IF. */
  public double getFeIF() {
    return ((Receiver)cfg.receivers.get(currentFE)).feIF;
  }


  public void updateCentreFrequency(double centre, int subsystem) {
    if(!_ignoreSpItem) {
      _instHeterodyne.setCentreFrequency(centre, subsystem);

//      // If the centre frequency of the top system is updated then the lo1 is
//      // adjusted as well (unless the adjustment was done wiht the right mouse button)
//      if(subsystem == 0) {
//        _instHeterodyne.setCentreFrequency(centre, subsystem);
//      }
    }
  }

  public void updateBandWidth(double width, int subsystem) {
    _instHeterodyne.setBandWidth(width, subsystem);

    // Find the overlap asscoiated with this bandWidth
    BandSpec currentBandSpec = (BandSpec)_w.feBandModeChoice.getSelectedItem();
    int index = 0;
    for(int i = 0; i < currentBandSpec.bandWidths.length; i++) {
      if(currentBandSpec.bandWidths[i] == width) {
        index = i;
	break;
      }
    }

    _instHeterodyne.setOverlap(currentBandSpec.defaultOverlaps[index], subsystem);

    if(subsystem == 0) {
      _ignoreEvents = true;
      _w.bandWidthChoice.setSelectedItem("" + Math.rint(width * 1.0E-6) / 1000.0);

      // Re-centre band on line
      _resetTransition();
      _ignoreEvents = false;
    }
  }

  public void updateChannels(int channels, int subsystem) {
    // If the subsystem is the top subsystem (subsystem 0)
    // then update the resolution display.
    if(subsystem == 0) {
      int topSubSystemResolution = (int) ( 1.0E-3 * _instHeterodyne.getBandWidth(0) / (double)channels );
      _w.resolution.setText("" + topSubSystemResolution);
    }

    if(!_ignoreSpItem) {
      _instHeterodyne.setChannels(channels, subsystem);
    }
  }

  public void updateLineDetails(LineDetails lineDetails, int subsystem) {
    if(_ignoreSpItem) {
       return;
    }

    if(lineDetails == null) {
      _instHeterodyne.setMolecule(NO_LINE, subsystem);
      _instHeterodyne.setTransition(NO_LINE, subsystem);
      _instHeterodyne.setRestFrequency(getRestFrequency(subsystem), subsystem);
    }
    else {
      // Note that the top subsystem's line details are set in this
      // EdCompInstHeterodyne editor.
      if(subsystem != 0) {
        _instHeterodyne.setMolecule(lineDetails.name, subsystem);
        _instHeterodyne.setTransition(lineDetails.transition.trim(), subsystem);
        _instHeterodyne.setRestFrequency(lineDetails.frequency * 1.0E6, subsystem);
      }
    }

    if((subsystem == 0) && (lineDetails == null)) {
      _ignoreEvents = true;
      _w.moleculeChoice.setSelectedItem(NO_LINE);
      _w.transitionChoice.setSelectedItem(NO_LINE + " ");
      _w.moleculeFrequency.setText("" + (getRestFrequency(0) / 1.0E6));
      _ignoreEvents = false;
    }

  }

  public void updateLO1(double lo1) {
    _ignoreEvents = true;
    _w.moleculeChoice.setSelectedItem(NO_LINE);
    _w.transitionChoice.setSelectedItem(NO_LINE + " ");
    _w.moleculeFrequency.setText("" + (getRestFrequency(0) / 1.0E6));
    _ignoreEvents = false;

    for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
      _instHeterodyne.setMolecule(NO_LINE, i);
      _instHeterodyne.setTransition(NO_LINE, i);
      _instHeterodyne.setRestFrequency(getRestFrequency(i), i);
    }

    sideBandDisplay.setMainLine(getRestFrequency(0));
  }


  public void changedUpdate(DocumentEvent e) { _velocityChanged = true; }
  public void insertUpdate(DocumentEvent e)  { _velocityChanged = true; }
  public void removeUpdate(DocumentEvent e)  { _velocityChanged = true; }
    public void focusGained(FocusEvent e) {}
//     public void focusLost(FocusEvent e) {feVelocityAction(null);}
    public void focusLost(FocusEvent e) {}
}
