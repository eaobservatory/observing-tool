// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// author: Alan Pickup = dap@roe.ac.uk         2001 Feb
// modified for Swing OT by
//         Martin Folger = M.Folger@roe.ac.uk  2001 Apr
// modified for UIST (from Michelle) by
//         Alan Pickup = A.Pickup              2001 Nov
//
// $Id$
//
package ot.ukirt.iter.editor;

import java.util.*;

import orac.ukirt.inst.SpInstUIST;
import orac.ukirt.iter.SpIterUISTCalObs;
import orac.ukirt.iter.SpUISTCalConstants;
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
 * This is the editor for the UIST Calibration Observation iterator.
 */
public final class EdIterUISTCalObs extends OtItemEditor
                     implements TextBoxWidgetWatcher, DropDownListBoxWidgetWatcher, ActionListener
{

   private SpItem   _baseItem;

   /** Identifier for a FLAT calibration. */
   public static final int FLAT = 0;

   /** Identifier for an ARC calibration. */
   public static final int ARC  = 1;

   private SpIterUISTCalObs _calUIST;

   private IterUISTCalObsGUI _w;		// the GUI layout

   /**
    * This flag is set true while methods like _init is executed to prevent actionPerformed() to do react to
    * action events caused by initializing widgets.
    */
   private boolean _ignoreActionEvents = false;

/**
 * The constructor initializes the title, description, and presentation source.
 */
public EdIterUISTCalObs()
{
   _title       ="UIST Cal Observation";
   _presSource  = _w = new IterUISTCalObsGUI();
   _description ="Configure UIST's Calibration with this component.";

   for(int i = 0; i < 100; i++) {
     _w.repeatComboBox.addItem("" + (i + 1));
   }

   _w.calType.addActionListener(this);
   _w.arc_source.addActionListener(this);
   _w.flat_source.addActionListener(this);
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

   SpIterUISTCalObs ico = (SpIterUISTCalObs) _spItem;

   // Set the calibration choices
   ddlbw = (DropDownListBoxWidgetExt) _w.calType;

   TextBoxWidgetExt tbw;

   // Exposure time
   tbw = (TextBoxWidgetExt) _w.exposureTime;
   tbw.addWatcher( this );

   // Observation time
   tbw = (TextBoxWidgetExt) _w.observationTime;
   tbw.addWatcher( this );

   // Flat source
   ddlbw = (DropDownListBoxWidgetExt) _w.flat_source;

   // arc source
   ddlbw = (DropDownListBoxWidgetExt) _w.arc_source;

   // Coadds
   tbw = (TextBoxWidgetExt) _w.coadds;

   // default button
   cbw = (CommandButtonWidgetExt) _w.useDefaults;

   super._init();

   _ignoreActionEvents = false;
}

/**
 * Implements the _updateWidgets method from OtItemEditor in order to
 * setup the widgets to show the current values of the item.
 */

protected void _updateWidgets()
{
    _updateWidgets(null);
}

protected void
_updateWidgets(Object source)
{
   _ignoreActionEvents = true;

   DropDownListBoxWidgetExt ddlbw;

   SpIterUISTCalObs ico = (SpIterUISTCalObs) _spItem;

   // Get the choices and defaults from the instrument.

   // Check the calType and reset if this is Arc and UIST is now imaging
   if (ico.isImaging() && ico.getCalTypeString().equalsIgnoreCase("Arc")) {
       ico.setCalType("Flat");
       ico.useDefaults();
   }

   // Update calType selection box
   ddlbw = (DropDownListBoxWidgetExt) _w.calType;
   ddlbw.setChoices( ico.getCalTypeChoices() );
   ddlbw.setValue( ico.getCalTypeString() );

   // Observe repetitions
   _w.repeatComboBox.setValue( ico.getCount() - 1);

   TextBoxWidgetExt tbw;

   // Exposure time
   if(_w.exposureTime != source) {
       tbw = (TextBoxWidgetExt) _w.exposureTime;
       tbw.setValue( ico.getExpTimeOTString() );
   }

   // Observation time
   if(_w.observationTime != source) {
       tbw = (TextBoxWidgetExt) _w.observationTime;
       tbw.setValue( ico.getObsTimeOT() );
   }

   // Deal with Flat and Arc according to state of inst & caltype
   if (ico.getCalType() == FLAT) {
     // MFO: "FLAT" is hard-wired in IterCGS4CalObsGUI (as constraint string for CardLayout).
     ((CardLayout)(_w.calTypesPanel.getLayout())).show(_w.calTypesPanel, "FLAT");

     ddlbw = (DropDownListBoxWidgetExt) _w.flat_source;
     ddlbw.setChoices (ico.getFlatSourceChoices());
     ddlbw.setValue( ico.getFlatSource() );
   }else{
     // DAP: "ARC" is hard-wired in IterCGS4CalObsGUI (as constraint string for CardLayout).
     ((CardLayout)(_w.calTypesPanel.getLayout())).show(_w.calTypesPanel, "ARC");

     ddlbw = (DropDownListBoxWidgetExt) _w.arc_source;
     ddlbw.setChoices (ico.getArcSourceChoices());
     ddlbw.setValue( ico.getArcSource() );
   }

   // Update data acquisition config
   ico.updateDAConf();

   //Update coadds
   tbw = (TextBoxWidgetExt) _w.coadds;
   tbw.setValue( ico.getCoaddsString() );

   _ignoreActionEvents = false;
}


/**
 * Watch changes to text box widgets.
 */
public void
textBoxKeyPress(TextBoxWidgetExt tbw)
{
   SpIterUISTCalObs ico = (SpIterUISTCalObs) _spItem;
   if (tbw == _w.exposureTime) {
      try {
          String ets = tbw.getText();
          double et = Double.parseDouble(ets);
          if (et > 0.00001) {
             ico.setExpTimeOT(ets);
             _updateWidgets(_w.exposureTime);
          }
      } catch( Exception ex) {
	// ignore
      }
   } else if (tbw == _w.observationTime) {
      try {
          String ots = tbw.getText();
          double ot = Double.parseDouble(ots);
          if (ot > 0.00001) {
             ico.setObsTimeOT(ots);
             _updateWidgets(_w.observationTime);
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

   SpIterUISTCalObs ico = (SpIterUISTCalObs) _spItem;

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

   if (w == _w.arc_source) {
      DropDownListBoxWidgetExt ddlbw = (DropDownListBoxWidgetExt) w;
      ico.setArcSource(ddlbw.getStringValue());
      _updateWidgets();
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
