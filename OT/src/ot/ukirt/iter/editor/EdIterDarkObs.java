// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.ukirt.iter.editor;

import orac.ukirt.iter.SpIterDarkObs;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;

import gemini.sp.SpItem;
import gemini.sp.SpAvTable;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;

import jsky.app.ot.editor.OtItemEditor;

/**
 * This is the editor for Dark Observe iterator component.
 */
public final class EdIterDarkObs extends OtItemEditor
                              implements TextBoxWidgetWatcher, ActionListener
{

   private IterDarkObsGUI _w;
   
   /**
    * If true, ignore action events.
    */
   private boolean ignoreActions = false;

/**
 * The constructor initializes the title, description, and presentation source.
 */
public EdIterDarkObs()
{
   _title       ="Dark Iterator";
   _presSource  = _w = new IterDarkObsGUI();
   _description ="Take the specified number of dark exposures.";

   for(int i = 0; i < 100; i++) {
     _w.repeatComboBox.addItem("" + (i + 1));
   }

   _w.repeatComboBox.addActionListener(this);
   _w.defaultAcquisition.addActionListener(this);
}

/**
 */
protected void
_init()
{
   // Exposure time
   _w.exposureTime.addWatcher( this );
 
   // Coadds
   _w.coadds.addWatcher( this );
 
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

   SpIterDarkObs iterObs = (SpIterDarkObs) _spItem;

   // Repetitions
   _w.repeatComboBox.setSelectedIndex( iterObs.getCount() - 1);

   // Exposure Time
   _w.exposureTime.setValue( iterObs.getExposureTime() );

   // Coadds
   _w.coadds.setValue( iterObs.getCoadds() );

   ignoreActions = false;
}

/**
 * Watch changes to text boxes
 */
public void
textBoxKeyPress(TextBoxWidgetExt tbwe)
{
   SpIterDarkObs iterObs = (SpIterDarkObs) _spItem;

   if (tbwe == _w.exposureTime) {
      iterObs.setExposureTime( tbwe.getText() );
   } else if (tbwe == _w.coadds) {
      iterObs.setCoadds( tbwe.getText() );
   }
}
 
/**
 * Text box action.
 */
public void
textBoxAction(TextBoxWidgetExt tbwe) {}

/**
 *
 */
public void
actionPerformed(ActionEvent evt)
{
   if(ignoreActions)
      return;
 
   Object w = evt.getSource();

   SpIterDarkObs iterObs = (SpIterDarkObs) _spItem;

   if (w == _w.repeatComboBox) {
      int i = _w.repeatComboBox.getSelectedIndex() + 1;
      iterObs.setCount(i);
      return;
   }else if (w == _w.defaultAcquisition) {
      iterObs.useDefaultAcquisition();
      _updateWidgets();
      return;
   }
}

}

