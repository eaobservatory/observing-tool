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
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
 * This is the editor for the Scuba instrument component.
 *
 * Event handling and widget update.
 *
 * In most UKIRT components all the widgets are explicitly set to certain values
 * when _updateWidgets() is called. In order to avoid multiple updates the Watchers
 * from the package jsky.app.ot.gui are used because they do not fire events when
 * the value of a widget is set by some code rather then the user.<p>
 *
 * Example: JComboBox.setSelectedItem() <i>does</i> cause a call to
 * java.awt.event.ItemListener.itemStateChanged().<p>
 *
 * But {@link jsky.app.ot.gui.DropDownListBoxWidgetExt}.setValue() does <i>not</i>
 * cause a call to {@link jsky.app.ot.gui.DropDownListBoxWidget}.dropDownListBoxSelect()
 *
 * That is why the jsky.app.ot.gui package Watchers are better for components such as the
 * ones used for UKIRT.
 * The event handling and widget updating is simpler in the SCUBA component. Using the
 * ListSelectionListener simplifies event handling and widget updating even further
 * as long as it is taken into account that setting a ListBoxWidgetExt to a certain value
 * will trigger a ListSelectionEvent just like a user action would.
 *
 * @author Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdCompInstSCUBA extends OtItemEditor implements ListSelectionListener,
                                                                   ListBoxWidgetWatcher,
								   CheckBoxWidgetWatcher {

  private SpInstSCUBA _instSCUBA;

  private ScubaGUI _w;		// the GUI layout

  private boolean _ignoreActions = false;

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



//    if(filterVector.size() > 0) {
//      _w.subInstList.setChoices(SpInstSCUBA.getSubInstrumentsFor((String)filterVector.get(0)));
//    }  
//    System.out.println("filterList.setValue(int)-----------------------");
//    _ignoreActions = true;
//    _w.filterList.setValue(0);
//    _w.subInstList.setValue(0);
//    _ignoreActions = false;


//    _w.filterList.addListSelectionListener(this);
//    _w.subInstList.addListSelectionListener(this);
//    _w.bolometerList.addListSelectionListener(this);
    _w.filterList.addWatcher(this);
    _w.subInstList.addWatcher(this);
    _w.bolometerList.addWatcher(this);
    _w.explicitBolometer.addWatcher(this);
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

    String [] subInstruments = SpInstSCUBA.getSubInstrumentsFor(_instSCUBA.getFilter());
    _w.subInstList.setChoices(subInstruments);
    _w.subInstList.setValue(_instSCUBA.getSubInstrument());
      
    if(_w.subInstList.getSelectedIndex() == -1) {
      _w.subInstList.setValue(0);
      _instSCUBA.setSubInstrument(_w.subInstList.getStringValue());
      _instSCUBA.setBolometer(null);
    }

    if((SpInstSCUBA.getBolometersFor(_instSCUBA.getSubInstrument()) != null) && (_instSCUBA.getBolometer() != null)) {
      Vector bolometers = SpInstSCUBA.getBolometersFor(_instSCUBA.getSubInstrument());
      _w.explicitBolometer.setValue(true);
      _w.explicitBolometer.setVisible(true);
      _w.bolometerList.setChoices(bolometers);
      _w.bolometerList.setSelectedValue(_instSCUBA.getBolometer(), true);
      _w.bolometerScrollPane.setVisible(true);
    }
    else {
      _w.explicitBolometer.setValue(false);
      _w.bolometerScrollPane.setVisible(false);
    }  

    if(SpInstSCUBA.getBolometersFor(_instSCUBA.getSubInstrument()) != null) {
      _w.explicitBolometer.setVisible(true);
    }
    else {
      _w.explicitBolometer.setVisible(false);
    }

    _ignoreActions = false;
    //((SpInstSCUBA)_spItem)
  }

  public void valueChanged(ListSelectionEvent e) {
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
    }

    if(lbwe == _w.subInstList) {
      _instSCUBA.setSubInstrument(_w.subInstList.getStringValue());
    }

    if(lbwe == _w.bolometerList) {
      _instSCUBA.setBolometer(_w.bolometerList.getStringValue());
    }

    _updateWidgets();
  }

  public void checkBoxAction(CheckBoxWidgetExt cbwe) {

    if(_ignoreActions) {
      if(System.getProperty("DEBUG") != null) {
        System.out.println("in checkBoxAction, IGNORING");
      }

      return;
    }

    if(System.getProperty("DEBUG") != null) {
      System.out.println("in checkBoxAction, NOT ignoring");
    }


    if(cbwe.getBooleanValue() == true) {
      if(SpInstSCUBA.getBolometersFor(_instSCUBA.getSubInstrument()) != null) {
        _instSCUBA.setBolometer((String)SpInstSCUBA.getBolometersFor(_instSCUBA.getSubInstrument()).get(0));
      }
    }
    else {
      _instSCUBA.setBolometer(null);
    }

    _updateWidgets();
  }
}

