// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.ukirt.iter.editor;

import orac.ukirt.iter.SpIterNodObs;


import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import gemini.sp.SpItem;
import gemini.sp.SpAvTable;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;

import java.util.Vector;

import jsky.app.ot.editor.OtItemEditor;

/**
 * This is the editor for Nod Observe iterator component.
 *
 * @author converted to Nod Observe Iterator component by Martin Folger (M.Folger@roe.ac.uk)
 */
public final class EdIterNodObs extends OtItemEditor
                              implements ActionListener
{

   private IterNodObsGUI _w;

   /**
    * If true, ignore action events.
    */
   private boolean ignoreActions = false;


/**
 * The constructor initializes the title, description, and presentation source.
 */
public EdIterNodObs()
{
   _title       ="Nod Iterator";
   _presSource  = _w = new IterNodObsGUI();
   _description ="Take the specified number of nod exposures.";

   for(int i = 0; i < 100; i++) {
     _w.repeatComboBox.addItem("" + (i + 1));
   }

   // If the choices can change depending on other settings then the adding of
   // choice items will have to be done in _init or _updateWidgets (see other editor components)
   for(int i = 0; i < SpIterNodObs.NOD_PATTERNS.length; i++) {
     _w.nodPattern.addItem(SpIterNodObs.getNodPattern(i));
   }  

   _w.repeatComboBox.addActionListener(this);
   _w.nodPattern.addActionListener(this);
}

/**
 */
protected void
_init()
{
   super._init();
}


/**
 * Implements the _updateWidgets method from OtItemEditor in order to
 * setup the widgets to show the current values of the item.
 */
protected void
_updateWidgets()
{
   ignoreActions = true;

   SpIterNodObs iterObs = (SpIterNodObs) _spItem;

   // Repetitions
   _w.repeatComboBox.setSelectedIndex( iterObs.getCount() - 1);

   // Nod Pattern
   _w.nodPattern.setValue( iterObs.getNodPatternVector() );

   ignoreActions = false;
}


/**
 *
 */
public void
actionPerformed(ActionEvent evt)
{
   if(ignoreActions)
      return;

   Object w  = evt.getSource();

   SpIterNodObs iterObs = (SpIterNodObs) _spItem;

   if (w == _w.repeatComboBox) {
      int i = _w.repeatComboBox.getSelectedIndex() + 1;
      iterObs.setCount(i);
      return;
   }

   if (w == _w.nodPattern) {
      iterObs.setNodPattern((Vector)_w.nodPattern.getSelectedItem());
      return;
   }   
}

}

