// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// author: Alan Pickup = dap@roe.ac.uk         2001 Feb
// modified for Swing OT by
//         Martin Folger = M.Folger@roe.ac.uk  2001 Apr
//
// $Id$
//
package ot.ukirt.iter.editor;

import java.util.*;

import orac.ukirt.inst.SpInstMichelle;
import orac.ukirt.iter.SpIterMichelleCalObs;
import orac.ukirt.iter.SpMichelleCalConstants;
import orac.util.LookUpTable;

import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;

import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jsky.app.ot.editor.OtItemEditor;

/**
 * This is the editor for the Michelle Calibration Observation iterator.
 */
public final class EdIterMichelleCalObs extends OtItemEditor
                     implements TextBoxWidgetWatcher, DropDownListBoxWidgetWatcher, ActionListener
{

   private SpItem   _baseItem;

   /** Identifier for a FLAT calibration. */
   public static final int FLAT = 0;

   /** Identifier for an ARC calibration. */
   public static final int ARC  = 1;

   private SpIterMichelleCalObs _calMichelle;

   private IterMichelleCalObsGUI _w;		// the GUI layout

   /**
    * This flag is set true while methods like _init is executed to prevent actionPerformed() to do react to
    * action events caused by initializing widgets.
    */
   private boolean _ignoreActionEvents = false;

/**
 * The constructor initializes the title, description, and presentation source.
 */
public EdIterMichelleCalObs()
{
   _title       ="Michelle Cal Observation";
   _presSource  = _w = new IterMichelleCalObsGUI();
   _description ="Configure Michelle's Calibration with this component.";

   for(int i = 0; i < 100; i++) {
     _w.repeatComboBox.addItem("" + (i + 1));
   }

   _w.calType.addActionListener(this);
   _w.flat_source.addActionListener(this);
   _w.flat_sampling.addActionListener(this);
   _w.repeatComboBox.addActionListener(this);
   _w.useDefaults.addActionListener(this);
}

/**
 */
protected void
_init()
{
   _ignoreActionEvents = true;

   DropDownListBoxWidgetExt ddlbw;
   CommandButtonWidgetExt cbw;

   SpIterMichelleCalObs ico = (SpIterMichelleCalObs) _spItem;

   // Set the calibration choices
   ddlbw = (DropDownListBoxWidgetExt) _w.calType;

   TextBoxWidgetExt tbw;

   // Exposure time
   tbw = (TextBoxWidgetExt) _w.exposureTime;
   tbw.addWatcher( this );

   // Exposure time
   tbw = (TextBoxWidgetExt) _w.observationTime;
   tbw.addWatcher( this );

   // Flat source
   ddlbw = (DropDownListBoxWidgetExt) _w.flat_source;

   // Flat sampling
   ddlbw = (DropDownListBoxWidgetExt) _w.flat_sampling;

   // default button
   cbw = (CommandButtonWidgetExt) _w.useDefaults;

   super._init();

   _ignoreActionEvents = false;
}

/**
 * Implements the _updateWidgets method from OtItemEditor in order to
 * setup the widgets to show the current values of the item.
 */
protected void
_updateWidgets()
{
   _ignoreActionEvents = true;

   DropDownListBoxWidgetExt ddlbw;

   SpIterMichelleCalObs ico = (SpIterMichelleCalObs) _spItem;

   // Get the choices and defaults from the instrument.

   // Show the calib. type
   ddlbw = (DropDownListBoxWidgetExt) _w.calType;
   ddlbw.setChoices( ico.getCalTypeChoices() );
   ddlbw.setValue( ico.getCalTypeString() );

   // Observe repetitions
   _w.repeatComboBox.setValue( ico.getCount() - 1);

   TextBoxWidgetExt tbw;

   // Exposure time
   tbw = (TextBoxWidgetExt) _w.exposureTime;
   tbw.setValue( ico.getExposureTime() );

   // Observation time
   tbw = (TextBoxWidgetExt) _w.observationTime;
   tbw.setValue( ico.getObservationTime() );

   // Deal with Flat Sampling according to state of inst & caltype
   if (ico.getCalType() == FLAT) {
     // MFO: "FLAT" is hard-wired in IterCGS4CalObsGUI (as constraint string for CardLayout).
     ((CardLayout)(_w.calTypesPanel.getLayout())).show(_w.calTypesPanel, "FLAT");

     ddlbw = (DropDownListBoxWidgetExt) _w.flat_source;
     ddlbw.setChoices (ico.getFlatSourceChoices());
     ddlbw.setValue( ico.getFlatSource() );
     ddlbw = (DropDownListBoxWidgetExt) _w.flat_sampling;
     ddlbw.setChoices (ico.getSamplingChoices());
     ddlbw.setValue( ico.getSampling() );
   }else{
     // MFO: "EMPTY" is hard-wired in IterCGS4CalObsGUI (as constraint string for CardLayout).
     ((CardLayout)(_w.calTypesPanel.getLayout())).show(_w.calTypesPanel, "EMPTY");
   }

   // Update data acquisition config
   ico.updateDAConf();

   _ignoreActionEvents = false;
}


/**
 * Watch changes to text box widgets.
 */
public void
textBoxKeyPress(TextBoxWidgetExt tbw)
{
   SpIterMichelleCalObs ico = (SpIterMichelleCalObs) _spItem;
   
   if (tbw.getName().equals("exposureTime")) {
      try {
          String ets = tbw.getText();
          double et = Double.parseDouble(ets);
          if (et > 0.00001) {
             ico.setExpTime(ets);
             _updateWidgets();
          }
      } catch( Exception ex) {
	// ignore
      }
   } else if (tbw.getName().equals("observationTime")) {
      try {
          String ots = tbw.getText();
          double ot = Double.parseDouble(ots);
          if (ot > 0.00001) {
             ico.setObservationTime(ots);
             _updateWidgets();
          }
      } catch( Exception ex) {
	// ignore
      }
   }
}
 
/**
 * Text box action.
 */
public void
textBoxAction(TextBoxWidgetExt tbwe) {}

/**
 * DD list box select.
 */
public void
dropDownListBoxSelect(DropDownListBoxWidgetExt ddlbwe, int i, String val) {
}


/**
 * DD list box action.
 */
public void
dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe, int i, String val) {
}



/**
 *
 */
public void actionPerformed(ActionEvent evt)
{
   if(_ignoreActionEvents) {
      return;
   }

   Object w    = evt.getSource();

   SpIterMichelleCalObs ico = (SpIterMichelleCalObs) _spItem;

   if (w == _w.calType) {
      DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
      ico.setCalType(ddlbw.getStringValue());
      ico.useDefaults();
      _updateWidgets();
      return;
   }

   if (w == _w.flat_source) {
      DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
      ico.setFlatSource(ddlbw.getStringValue());
      _updateWidgets();
      return;
   }

   if (w == _w.flat_sampling) {
      DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
      ico.setSampling(ddlbw.getStringValue());
      return;
   }

   if (w == _w.repeatComboBox) {
      int i = _w.repeatComboBox.getIntegerValue() + 1;
      ico.setCount(i);
      return;
   }

   if (w == _w.useDefaults) {
      ico.useDefaults();
      _updateWidgets();
      return;
   }
}

}
