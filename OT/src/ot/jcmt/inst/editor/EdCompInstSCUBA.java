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
import java.awt.Component;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
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
								   CheckBoxWidgetWatcher,
								   MouseListener {

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


    _w.subInstList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    _w.subInstList.setCellRenderer(new SubInstListCellRenderer(_w.subInstList.getCellRenderer()));

//    _w.filterList.addListSelectionListener(this);
//    _w.subInstList.addListSelectionListener(this);
//    _w.bolometerList.addListSelectionListener(this);
    _w.filterList.addWatcher(this);
    _w.subInstList.addWatcher(this);
    _w.subInstList.addMouseListener(this);
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

    String [] selectedSubInstruments = _instSCUBA.getSubInstruments();
    for(int i = 0; i < selectedSubInstruments.length; i++) {
      for(int j = 0; j < subInstruments.length; j++) {
        if(selectedSubInstruments[i].equals(subInstruments[j])) {
	  _w.subInstList.addSelectionInterval(j, j);
	}
      }
    }
      
    if(_w.subInstList.getSelectedIndex() == -1) {
      _w.subInstList.setValue(0);
      _instSCUBA.setPrimarySubInstrument(_w.subInstList.getStringValue());
//      _instSCUBA.setBolometer(null);
    }

    if((SpInstSCUBA.getBolometersFor(_instSCUBA.getPrimarySubInstrument()) != null) &&
       (_instSCUBA.getBolometer() != null)) {

      Vector bolometers = SpInstSCUBA.getBolometersFor(_instSCUBA.getPrimarySubInstrument());
//      _w.explicitBolometer.setValue(true);
//      _w.explicitBolometer.setVisible(true);
      _w.bolometerList.setChoices(bolometers);
      _w.bolometerList.setValue(_instSCUBA.getBolometer());
      _w.bolometerScrollPane.setVisible(true);
    }
    else {
//      _w.explicitBolometer.setValue(false);
      _w.bolometerScrollPane.setVisible(false);
    }  

    if(_instSCUBA.getBolometer() != null) {
      _w.explicitBolometer.setValue(true);
    }
    else {
      _w.explicitBolometer.setValue(false);
    }
    

    if(SpInstSCUBA.getBolometersFor(_instSCUBA.getPrimarySubInstrument()) != null) {
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
      System.out.println("in listBoxAction, value = " + val + ", NOT ignoring");
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
      _instSCUBA.setSubInstruments(new String[]{});
    }

    if(lbwe == _w.subInstList) {
      _instSCUBA.setSubInstruments(_w.subInstList.getSelectedValues());

      // If the primary sub-instrument is not amoung the newly selected sub-instruments
      // then make the first one of the selected sub-instruments the primary one.
      if(!Arrays.asList(_instSCUBA.getSubInstruments()).contains(_instSCUBA.getPrimarySubInstrument())) {
        _instSCUBA.setPrimarySubInstrument(_w.subInstList.getStringValue());
      }

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
      if(SpInstSCUBA.getBolometersFor(_instSCUBA.getPrimarySubInstrument()) != null) {
        _instSCUBA.setBolometer((String)SpInstSCUBA.getBolometersFor(_instSCUBA.getPrimarySubInstrument()).get(0));
      }
    }
    else {
      _instSCUBA.setBolometer(null);
    }

    _updateWidgets();
  }

  public void mouseClicked(MouseEvent e) {
    if(_ignoreActions) {
      if(System.getProperty("DEBUG") != null) {
        System.out.println("in mouseClicked, IGNORING");
      }

      return;
    }

    if(System.getProperty("DEBUG") != null) {
      System.out.println("in mouseClicked, NOT ignoring");
    }


    if(SwingUtilities.isRightMouseButton(e)) {
      String clickedSubInstrument =
                (String)_w.subInstList.getModel().getElementAt(
	          _w.subInstList.locationToIndex(e.getPoint()));

      if(Arrays.asList(_instSCUBA.getSubInstruments()).contains(clickedSubInstrument)) {
        _instSCUBA.setPrimarySubInstrument(clickedSubInstrument);
      }
        
      _w.subInstList.repaint();
    }
  }


  public void mouseEntered(MouseEvent e)  { }
  public void mouseExited(MouseEvent e)   { }
  public void mousePressed(MouseEvent e)  { }
  public void mouseReleased(MouseEvent e) { } 
  

  private class SubInstListCellRenderer implements ListCellRenderer {

    ListCellRenderer _listCellRenderer;

    public SubInstListCellRenderer(ListCellRenderer listCellRenderer) {
      _listCellRenderer = listCellRenderer;
    }

    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

      DefaultListCellRenderer defaultListCellRenderer =
        (DefaultListCellRenderer)_listCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

      if(((String)value).equals(_instSCUBA.getPrimarySubInstrument())) {
        //defaultListCellRenderer.setBackground(Color.magenta);
	defaultListCellRenderer.setFont(defaultListCellRenderer.getFont().deriveFont(Font.BOLD));
	defaultListCellRenderer.setForeground(Color.red);
      }

      return defaultListCellRenderer;
    }
  }
}

