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

import gemini.sp.SpItem;
import orac.jcmt.inst.SpInstSCUBA;
import jsky.app.ot.editor.OtItemEditor;
import jsky.app.ot.gui.ListBoxWidgetExt;
import jsky.app.ot.gui.ListBoxWidgetWatcher;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;

import java.util.Enumeration;
import java.util.Vector;
import java.util.Arrays;
import java.util.Hashtable;
import java.awt.Component;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.ListCellRenderer;
import javax.swing.DefaultListCellRenderer;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.SwingUtilities;

/**
 * This is the editor for the Scuba instrument component.
 *
 * @author Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdCompInstSCUBA extends OtItemEditor implements ListBoxWidgetWatcher,
								   ActionListener {

  private SpInstSCUBA _instSCUBA;

  private ScubaGUI _w;		// the GUI layout

  private boolean _ignoreActions = false;

  /**
   * GUI with SCUBA arrays for bolometer selection.
   */
  private ScubaArraysFrame _scubaArraysGUI = new ScubaArraysFrame();


  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdCompInstSCUBA() {
    _title       ="JCMT SCUBA";
    _presSource  = _w = new ScubaGUI();
    _description ="The SCUBA instrument is configured with this component.";


    Enumeration filters = SpInstSCUBA.filters();
    Vector filterVector = new Vector();
    
    while(filters.hasMoreElements()) {
      filterVector.add(filters.nextElement());  
    }

    _w.filterList.setChoices(filterVector);

    _w.filterList.addWatcher(this);
    _w.editBolometers.addActionListener(this); 
  }

  /**
   * Override setup to store away a reference to the SpInstSCUBA item.
   */
  public void setup(SpItem spItem) {
    _instSCUBA = (SpInstSCUBA) spItem;
    super.setup(spItem);
  }


  /**
   * Implements the _updateWidgets method from OtItemEditor in order to
   * setup the widgets to show the current values of the item.
   */
  protected void _updateWidgets() {
    _ignoreActions = true;

    _w.filterList.setValue(_instSCUBA.getFilter());

    if(_instSCUBA.getBolometers() != null) {
      _w.additionalBolometers.setChoices(_instSCUBA.getBolometers());
    }
    else {
      _w.additionalBolometers.clear();
    }

    _w.primaryBolometer.setValue(_instSCUBA.getPrimaryBolometer());

    _ignoreActions = false;
  }


  /**
   * This public methods allows the ScubaArrays GUI to cause the
   * widgets of this editor to be updated.
   */
  public void refresh() {
    _updateWidgets();
  }

  public void listBoxAction(ListBoxWidgetExt lbwe, int index, java.lang.String val) {
    if(System.getProperty("DEBUG") != null) {
      System.out.println("in listBoxAction, value = " + val);
    }
  }


  public void listBoxSelect(ListBoxWidgetExt lbwe, int index, java.lang.String val) {
    if(_ignoreActions) {
      if(System.getProperty("DEBUG") != null) {
        System.out.println("in listBoxSelect, value = " + val + ", IGNORING");
      }

      return;
    }


    if(System.getProperty("DEBUG") != null) {
      System.out.println("in listBoxSelect, value = " + val + ", NOT ignoring");
    }


    if(lbwe == _w.filterList) {
      _instSCUBA.setFilter(_w.filterList.getStringValue());

      // Setting the sub-instruments to the empty String array will cause _updateWidgets()
      // to make the first entry of the sub-instrument list the primary sub-instrument
      //  and the only sub-instrument selected. These settings put into _instSCUBA as well.
      _instSCUBA.setBolometers(null);
      _instSCUBA.setPrimaryBolometer(null);
    }

    _updateWidgets();
  }

  public void actionPerformed(ActionEvent e) {
    _scubaArraysGUI.show(this);
  }
}

