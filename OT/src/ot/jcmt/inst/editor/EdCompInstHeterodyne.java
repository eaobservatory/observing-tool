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
import java.awt.Point;
import java.util.*;
import java.io.*;

import gemini.sp.SpItem;
import orac.jcmt.inst.SpInstHeterodyne;
import edfreq.*;
import jsky.app.ot.editor.OtItemEditor;

/**
 * Heterodyne Editor.
 *
 * This class implements the HeterodyneEditor interface. Its implementation is based
 * on the old edfreq.FrontEnd class which is not used anymore.
 *
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class EdCompInstHeterodyne extends OtItemEditor implements ActionListener, HeterodyneEditor {

  private String currentFE = "";
  private SideBandDisplay sideBandDisplay = new SideBandDisplay(this);
  private LineCatalog lineCatalog = new LineCatalog();
  private double redshift = 0.0;
  private double subBandWidth = 0.25E9;
  private double loMin;
  private double loMax;
  private double feIF;
  private double feBandWidth;
  private double feOverlap = 0.0;

  private static final String FREQ_EDITOR_CFG_PROPERTY = "FREQ_EDITOR_CFG";

  private boolean _ignoreEvents = false;
  private boolean _ignoreSpItem = false;


  /**
   * A static configuration object which can used by classes throughout this
   * package to configure themselves.
   */
  protected static FrequencyEditorCfg cfg = FrequencyEditorCfg.getConfiguration();

  private SpInstHeterodyne _instHeterodyne;

  private HeterodyneGUI _w;		// the GUI layout

  public EdCompInstHeterodyne() {
    _title       ="JCMT Heterodyne";
    _presSource  = _w = new HeterodyneGUI();
    _description ="The Heterodyne instrument is configured with this component.";

      _ignoreSpItem = true;

      _w.feChoice.setModel(new DefaultComboBoxModel(new Vector(cfg.frontEndTable.keySet())));
      _w.feChoice.addActionListener ( this );

      _w.feMode.setModel(new DefaultComboBoxModel((String[])cfg.frontEndTable.get(_w.feChoice.getItemAt(0))));
      _w.feMode.addActionListener( this );
      _w.feBandModeChoice.addActionListener ( this );

      _w.overlap.setText ( "0.0" );
      _w.overlap.addActionListener ( this );

/* Create the display */

      _w.velocity.setText ( "0.0" );
      _w.velocity.addActionListener ( this );


      _w.feBand.setModel(new DefaultComboBoxModel(new String[] { "usb", "lsb", "optimum" } ));
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

/* Assemble the display */

      _w.freqEditorButton.addActionListener(this);

   
      // MFO trigger additional initialising.
      feChoiceAction(null);
      updateSideBandDisplay();
      //feMolecule2Action(null);
      feMoleculeAction(null);
      feTransitionAction(null);

      _ignoreSpItem = false;
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

      try {
         _w.feChoice.setSelectedItem(_instHeterodyne.getFrontEnd());
         _w.feMode.setSelectedItem(_instHeterodyne.getMode());
         _w.feBandModeChoice.setSelectedItem(getObject(_w.feBandModeChoice, _instHeterodyne.getBandMode()));
         _w.overlap.setText("" + _instHeterodyne.getOverlap());
         _w.velocity.setText("" + _instHeterodyne.getVelocity());
         _w.feBand.setSelectedItem(getObject(_w.feBand, _instHeterodyne.getBand()));
         _w.moleculeChoice.setSelectedItem(getObject(_w.moleculeChoice, _instHeterodyne.getMolecule(0)));
         _w.transitionChoice.setSelectedItem(getObject(_w.transitionChoice, _instHeterodyne.getTransition(0)));
         _w.moleculeFrequency.setText("" + (_instHeterodyne.getRestFrequency(0) / 1.0E6));
         sideBandDisplay.setLO1(_instHeterodyne.getLO1());

         for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
           sideBandDisplay.setCentreFrequency(_instHeterodyne.getCentreFrequency(i), i);
           sideBandDisplay.setBandWidth(_instHeterodyne.getBandWidth(i), i);
           sideBandDisplay.setLineText(_instHeterodyne.getMolecule(i) + "  " +
                                       _instHeterodyne.getTransition(i) + "  " +
                                       _instHeterodyne.getRestFrequency(i), i);
         }
      }
      catch(Exception e) {
         e.printStackTrace();
         System.out.println("The Heterodyne Editor (EdCompInstHeterodyne) widgets could not be updated\n" +
	                    "from the Heterodyne item (SpInstHeterodyne). This can happen if the\n" + 
                            "Heterodyne item table does not contain default values.");
      }

      _ignoreSpItem = false;
  }


   public void actionPerformed ( ActionEvent ae )
   {
      if(ae.getSource() == _w.freqEditorButton) {
         sideBandDisplay.show();
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
      else if ( ae.getSource() == _w.velocity )
      {
         feVelocityAction ( ae );
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
         _instHeterodyne.setBandWidth(Double.parseDouble((String)_w.bandWidthChoice.getSelectedItem()) * 1.0E9, i);
         sideBandDisplay.setBandWidth(_instHeterodyne.getBandWidth(i), i);
      }

      if(((BandSpec)_w.feBandModeChoice.getSelectedItem()).getNumHybridSubBands(_w.bandWidthChoice.getSelectedIndex()) > 1) {
         _w.overlap.setEnabled(true);
      }
      else {
         _w.overlap.setEnabled(false);
      }
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
        sideBandDisplay.setLineText(_instHeterodyne.getMolecule(i) + "  " +
                                    _instHeterodyne.getTransition(i) + "  " +
                                    _instHeterodyne.getRestFrequency(i), i);
      }
   }


   public void feMoleculeAction ( ActionEvent ae )
   {
      if(_w.moleculeChoice.getSelectedItem() instanceof SelectionList) {
         SelectionList species = (SelectionList)_w.moleculeChoice.getSelectedItem();

         String transition  = null;
         String oldMolecule = null;

         if(!_ignoreSpItem) {
            transition  = _instHeterodyne.getTransition(0);
            oldMolecule = _instHeterodyne.getMolecule(0);
	 }

         _w.transitionChoice.setModel ( 
           new DefaultComboBoxModel ( species.objectList ) );

         _ignoreEvents = true;
         if(((DefaultComboBoxModel)_w.transitionChoice.getModel()).getIndexOf(NO_LINE) == -1) {
            _w.transitionChoice.addItem(NO_LINE);
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

               JOptionPane.showMessageDialog(_w, "Transition changed: " + transition + " out of range.",
               "Transition changed", JOptionPane.WARNING_MESSAGE);
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
         _w.transitionChoice2.addItem(NO_LINE);
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
            if ( band.equals ( "usb" ) )
            {
	       if((obsFrequency - sideBandDisplay.getTopSubSystemCentreFrequency()) < loMin) {

                  JOptionPane.showMessageDialog(_w, "Using lower sideband in order to reach line.",
                  "Changing sideband", JOptionPane.WARNING_MESSAGE);

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

                  JOptionPane.showMessageDialog(_w, "Using upper sideband in order to reach line.",
                  "Changing sideband", JOptionPane.WARNING_MESSAGE);

                  _w.feBand.setSelectedItem("usb");
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

            // Set the centre frequencies of the remaining subsystems to that of the top subsystem.
            for(int i = 1; i < sideBandDisplay.getNumSubSystems(); i++) {
               sideBandDisplay.setCentreFrequency(sideBandDisplay.getTopSubSystemCentreFrequency(), i);
	    }
         }


         if(!_ignoreSpItem) {
            for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
               _instHeterodyne.setTransition(_w.transitionChoice.getSelectedItem().toString(), i);
               _instHeterodyne.setRestFrequency(transition.frequency, i);
               _instHeterodyne.setCentreFrequency(sideBandDisplay.getTopSubSystemCentreFrequency(), i);
	     }

	    _instHeterodyne.setLO1(sideBandDisplay.getLO1());
         }

         // Skip top system, start with i = 1
         for(int i = 1; i < sideBandDisplay.getNumSubSystems(); i++) {
            sideBandDisplay.setLineText(_instHeterodyne.getMolecule(i) + "  " +
                                        _instHeterodyne.getTransition(i) + "  " +
                                        _instHeterodyne.getRestFrequency(i), i);
         }
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
            if ( band.equals ( "usb" ) )
            {
               sideBandDisplay.setLO1 ( obsFrequency - sideBandDisplay.getTopSubSystemCentreFrequency() );
            }
            else
            {
               sideBandDisplay.setLO1 ( obsFrequency + sideBandDisplay.getTopSubSystemCentreFrequency() );
            }
         }

         if(!_ignoreSpItem) {
            _instHeterodyne.setLO1(sideBandDisplay.getLO1());
         }

         updateLineDetails(null, 0);
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

      BandSpec currentBandSpec = (BandSpec)_w.feBandModeChoice.getSelectedItem();
      _updateBandWidthChoice(currentBandSpec.getBandWidths(feOverlap));

      if(currentBandSpec.getNumHybridSubBands(_w.bandWidthChoice.getSelectedIndex()) > 1) {
         _w.overlap.setEnabled(true);
      }
      else {
        _w.overlap.setEnabled(false);
      }

      updateSideBandDisplay();

      if(!_ignoreSpItem) {
         _instHeterodyne.setBandMode(_w.feBandModeChoice.getSelectedItem().toString());

         for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
            _instHeterodyne.setMolecule(_w.moleculeChoice.getSelectedItem().toString(), i);
            _instHeterodyne.setTransition(_w.transitionChoice.getSelectedItem().toString(), i);
            _instHeterodyne.setCentreFrequency(feIF, i);
            _instHeterodyne.setBandWidth(currentBandSpec.getBandWidths(feOverlap)[0], i);
            _instHeterodyne.setChannels(currentBandSpec.getChannels(feOverlap)[0], i);
	    _instHeterodyne.setRestFrequency(getRestFrequency(i), i);

            // Skip to top system on sideBandDisplay
            if(i > 0) {
               sideBandDisplay.setLineText(_instHeterodyne.getMolecule(i) + "  " +
	                                   _instHeterodyne.getTransition(i) + "  " +
                                           _instHeterodyne.getRestFrequency(i), i);
            }
	 }
      }
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
      subBandWidth = currentBandSpec.getBandWidths(feOverlap)[0]; //currentBandSpec.bandWidths[0];

      sideBandDisplay.updateDisplay ( currentFE, loMin, loMax,
        feIF, feBandWidth,
        redshift,
        currentBandSpec.getBandWidths(feOverlap),
        currentBandSpec.getChannels(  feOverlap),
        subBandCount );

      _ignoreEvents = true;
      sideBandDisplay.resetModeAndBand((String)_w.feMode.getSelectedItem(), (String)_w.feBand.getSelectedItem());
      _ignoreEvents = false;

      feTransitionAction(null);
   }


   public void feChoiceAction ( ActionEvent ae )
   {

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

      _w.moleculeChoice.setSelectedIndex(0); // MFO: This line used to be after the one
                                           // where the ActionListener is added again
					   // but that (sometimes) caused a NullPointerException.
					   // Try doing something else that selectes a line when
					   // the EdCompInstHeterodyne is created.
      _w.moleculeChoice.addActionListener(this);

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
      }
   }


   public void feVelocityAction ( ActionEvent ae )
   {
      String svalue;
      double dvalue;
      double obsmin;
      double obsmax;

      svalue = _w.velocity.getText();
      dvalue = (Double.valueOf(svalue)).doubleValue();
      redshift = dvalue / EdFreq.LIGHTSPEED;

      obsmin = loMin - feIF - ( feBandWidth * 0.5 );
      obsmax = loMax + feIF + ( feBandWidth * 0.5 );

/* Update choice of molecules */

      _ignoreEvents = true;

      _w.moleculeChoice.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
          obsmax*(1.0+redshift) ) ) );
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

         JOptionPane.showMessageDialog(_w, "Molecule changed: " + molecule + " out of range.",
         "Molecule changed", JOptionPane.WARNING_MESSAGE);
      }

/* Update display of sidebands */

      if ( sideBandDisplay != null )
      {
         sideBandDisplay.setRedshift ( redshift );
      }

      if(!_ignoreSpItem) {
         _instHeterodyne.setVelocity(dvalue);
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
         _instHeterodyne.setOverlap(dvalue);
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

   public double getRedshift() {
      return redshift;
   }

   // See edfreq.HeterodyneEditor for documentation
   public double getRestFrequency(int subsystem) {
      return EdFreq.getRestFrequency(_instHeterodyne.getLO1(),
                                     _instHeterodyne.getCentreFrequency(subsystem),
                                     _instHeterodyne.getRedshift(),
                                     _instHeterodyne.getBand());
   }

   // See edfreq.HeterodyneEditor for documentation
   public double getObsFrequency(int subsystem) {
      return EdFreq.getObsFrequency(_instHeterodyne.getLO1(),
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

      // If the centre frequency of the top system is updated then the lo1 is
      // adjusted as well (unless the adjustment was done wiht the right mouse button)
      if(subsystem == 0) {
        _instHeterodyne.setCentreFrequency(centre, subsystem);
      }
    }
  }

  public void updateBandWidth(double width, int subsystem) {
    _instHeterodyne.setBandWidth(width, subsystem);

    if(subsystem == 0) {
      _ignoreEvents = true;
      _w.bandWidthChoice.setSelectedItem("" + Math.rint(width * 1.0E-6) / 1000.0);
      _ignoreEvents = false;
    }
  }

  public void updateChannels(double channels, int subsystem) { }

  public void updateLineDetails(LineDetails lineDetails, int subsystem) {
    if(_ignoreSpItem) {
       return;
    }

    if(lineDetails == null) {
      _instHeterodyne.setMolecule("", subsystem);
      _instHeterodyne.setTransition("", subsystem);
      _instHeterodyne.setRestFrequency(getRestFrequency(subsystem), subsystem);
    }
    else {
      // Note that the top subsystem's line details are set in this
      // EdCompInstHeterodyne editor.
      if(subsystem != 0) {
        _instHeterodyne.setMolecule(lineDetails.name, subsystem);
        _instHeterodyne.setTransition(lineDetails.transition, subsystem);
        _instHeterodyne.setRestFrequency(lineDetails.frequency * 1.0E6, subsystem);
      }
    }

    if((subsystem == 0) && (lineDetails == null)) {
      _ignoreEvents = true;
      _w.moleculeChoice.setSelectedItem(NO_LINE);
      _w.transitionChoice.setSelectedItem(NO_LINE);
      _w.moleculeFrequency.setText("" + (getRestFrequency(0) / 1.0E6));
      _ignoreEvents = false;
    }

  }

  public void updateLO1(double lo1) {
    _ignoreEvents = true;
    _w.moleculeChoice.setSelectedItem(NO_LINE);
    _w.transitionChoice.setSelectedItem(NO_LINE);
    _w.moleculeFrequency.setText("" + (getRestFrequency(0) / 1.0E6));
    _ignoreEvents = false;

    _instHeterodyne.setLO1(lo1);

    for(int i = 0; i < sideBandDisplay.getNumSubSystems(); i++) {
      _instHeterodyne.setMolecule("", i);
      _instHeterodyne.setTransition("", i);
    }

    _w.moleculeFrequency.setText("" + (_instHeterodyne.getRestFrequency(0) / 1.0E6));
  }
}
