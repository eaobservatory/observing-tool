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

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Vector;
import javax.swing.*;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.util.LookUpTable;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.TableWidgetExt;
import jsky.app.ot.gui.TableWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import gemini.sp.*;
import jsky.app.ot.tpe.TelescopePosEditor;
import jsky.app.ot.tpe.TpeManager;

import orac.util.SpItemDOM;
import org.xml.sax.InputSource;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.parsers.DOMParser;
import java.io.StringReader;


// TODO MFO: check whether jcmt needs its own EdCompInstBase.
import ot.ukirt.inst.editor.EdCompInstBase;

/**
 * Editor class for Heterodyne instrument component.
 */
public final class EdCompInstHeterodyne extends EdCompInstBase
    implements TableWidgetWatcher, DropDownListBoxWidgetWatcher, ActionListener,
               MouseListener, ItemListener, DocumentListener {

//    private EdStareCapability _edStareCapability;

    private HeterodyneGUI _w;		// the GUI layout

    private boolean ignoreActions = false;

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdCompInstHeterodyne() {
	_title       ="JCMT Heterodyne";
	_presSource  = _w = new HeterodyneGUI();
	_description ="The Heterodyne instrument is configured with this component.";
/*
	_edStareCapability = new EdStareCapability();

	ButtonGroup grp = new ButtonGroup();
	grp.add(_w.filterBroadBand);
	grp.add(_w.filterNarrowBand);
	grp.add(_w.filterSpecial);

	_w.filterBroadBand.addActionListener(this);
	_w.filterNarrowBand.addActionListener(this);
	_w.filterSpecial.addActionListener(this);
*/
      _w.frontEnd.getFeComboBox().addItemListener(this);
      _w.frontEnd.getFeBandModeComboBox().addItemListener(this);
      _w.frontEnd.getVelocityTextField().getDocument().addDocumentListener(this);
      _w.frontEnd.getOverlapTextField().getDocument().addDocumentListener(this);
      _w.frontEnd.getFeBandComboBox().addItemListener(this);
      _w.frontEnd.getFeModeComboBox().addItemListener(this);
      _w.frontEnd.getMoleculeComboBox().addItemListener(this);
      _w.frontEnd.getMolecule2ComboBox().addItemListener(this);
      _w.frontEnd.getTransitionComboBox().addItemListener(this);
      _w.frontEnd.getTransition2ComboBox().addItemListener(this);
      _w.frontEnd.getSideBandButton().addActionListener(this);

      _addListenersToSideBandDisplay();
    }

    /**
     * This method initializes the widgets in the presentation to reflect the
     * current values of the items attributes.
     */
    protected void _init() {
/*
	DropDownListBoxWidgetExt ddlbw;

	ddlbw = _w.readoutArea;
	ddlbw.setChoices(SpInstUFTI.READAREAS.getColumn(0));

	ddlbw = _w.sourceMagnitude;
	ddlbw.setChoices(SpInstUFTI.SRCMAGS);

	ddlbw = _w.acquisitionMode;
	ddlbw.setChoices(SpInstUFTI.MODES);

	ddlbw = _w.polariser;
	ddlbw.setChoices(SpInstUFTI.POLARISERS.getColumn(0));

	_w.readoutArea.addWatcher(this);
	_w.sourceMagnitude.addWatcher(this);
	_w.acquisitionMode.addWatcher(this);
	_w.polariser.addWatcher(this);
	// XXX MFO: OT to swing/jsky: Does _w.polariser really need a watcher???
	// I added it.

	TableWidgetExt twe;
	twe = _w.filterTable;
	twe.setBackground(_w.getBackground());
	twe.setColumnHeaders(new String[]{"Filter", "Wavel.(um)"});
	twe.addWatcher(this);

	super._init();
	_edStareCapability._init(this);
*/
    }


    /**
     * Initialize the Filter table widget according to the selected
     * filter category.
     */
    private void
    _showFilterType(LookUpTable filters)
    {
/*
      Vector[] rowsV = new Vector[filters.getNumRows()];
      rowsV = filters.getAsVectorArray();
      TableWidgetExt tw = _w.filterTable;
      tw.setRows(rowsV);
*/      
    }

    /**
     * Get the index of the filter in the given array, or -1 if the filter
     * isn't in the array.
     */
    private int
    _getFilterIndex(String filter, LookUpTable farray)
    {
      int fi = -1;
      try {
        fi = farray.indexInColumn (filter, 0);
      }catch (Exception ex) {
      }
      return fi;
    }
    
    /**
     * Update the filter choice related widgets.
     */
    private void _updateFilterWidgets() {
/*
	// First fill in the text box.
	JLabel stw  = _w.filter;
	String filter = ((SpInstUFTI) _spItem).getFilter();
	stw.setText(filter);

	// See which type of filter the selected filter is, if any.
	LookUpTable   farray = null;
	OptionWidgetExt ow     = null;

	int index = -1;
	if (filter == null) {
	    farray = SpInstUFTI.BROAD_BAND_FILTERS;
	    ow     = _w.filterBroadBand;
	} else {
	    farray = SpInstUFTI.BROAD_BAND_FILTERS;
	    index = _getFilterIndex(filter, farray);
	    if (index != -1) {
		ow = _w.filterBroadBand;
	    } else {
		farray = SpInstUFTI.NARROW_BAND_FILTERS;
		index = _getFilterIndex(filter, farray);
		if (index != -1) {
		    ow = _w.filterNarrowBand;
		} else {
		    farray = SpInstUFTI.SPECIAL_FILTERS;
		    index = _getFilterIndex(filter, farray);
		    if (index != -1) {
			ow = _w.filterSpecial;
		    } else {
			ow = _w.filterBroadBand;
		    }
		}
	    }
	}

	// Show the correct filters, and select the option widget for the type
	_showFilterType(farray);
	ow.setValue(true);

	// Select the filter in the table
	if ((filter != null) && (index != -1)) {
	    TableWidgetExt tw = _w.filterTable;
	    tw.selectRowAt(index);
	    tw.focusAtRow(index);
	}
*/
    }


    /**
     * Add AdjustmentListeners to the new JSliders that might have been added as a result of changes made
     * to other widgets.
     */
    protected void _addListenersToSideBandDisplay() {
      Vector widgets = _w.frontEnd.getSideBandDisplayWidgets();

      for(int i = 0;i < widgets.size(); i++) {
        if((widgets.get(i) instanceof JScrollBar) || (widgets.get(i) instanceof JSlider)) {
          // remove MouseListener (in case it was already there)
          ((JComponent)widgets.get(i)).removeMouseListener(this);

	  // add MouseListener
          ((JComponent)widgets.get(i)).addMouseListener(this);
        }
	else if (widgets.get(i) instanceof JToggleButton){
          // remove MouseListener (in case it was already there)
          ((JToggleButton)widgets.get(i)).removeActionListener(this);

	  // add MouseListener
          ((JToggleButton)widgets.get(i)).addActionListener(this);
	}
      }
    }


    protected void _updateSpItem() {
      /*MFO DEBUG*/System.out.println("EdCompInstHeterodyne._updateSpItem called.");
      // Preliminary implementation.
      SpInstHeterodyne instHeterodyne = (SpInstHeterodyne)_spItem;
      //instHeterodyne.setXML(_w.frontEnd.toXML());
      try {
        String xml = _w.frontEnd.toXML();

        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new StringReader(xml)));
        ElementImpl element = (ElementImpl)parser.getDocument().getDocumentElement();

        _spItem.getTable().rmAll();
        SpItemDOM.fillAvTable("", element, _spItem.getTable());
      }
      catch(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(_w, "Could not read data from frequency editor: " + e);
      }
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
      ignoreActions = true;

      // Check whether _avTable contains frequency editor data.
      // Note that this is a preliminary implementation and _spItem might not be
      // properly initialized yet.
      if(_avTab.get("heterodyne:feName") == null) {
        ignoreActions = false;
        return;
      }
      
      System.out.println("Trying to update from: ");
      _spItem.print("    ");      
      
      _w.frontEnd.getFeComboBox().setSelectedItem(_avTab.get("heterodyne:feName"));
      _w.frontEnd.getFeModeComboBox().setSelectedItem(_avTab.get("heterodyne:mode"));

      _w.frontEnd.getFeBandModeComboBox().setSelectedItem(_avTab.get("heterodyne:bandMode"));
      _w.frontEnd.getOverlapTextField().setText(_avTab.get("heterodyne:overlap"));

      _w.frontEnd.getVelocityTextField().setText(_avTab.get("heterodyne:velocity"));
      _w.frontEnd.getFeBandComboBox().setSelectedItem(_avTab.get("heterodyne:band"));

      ignoreActions = false;

/*
	SpInstUFTI instUFTI = (SpInstUFTI) _spItem;

	DropDownListBoxWidgetExt ddlbw;

	ddlbw = _w.readoutArea;
	ddlbw.setValue( instUFTI.getReadoutArea() );

	ddlbw        = _w.acquisitionMode;
	ddlbw.setValue( instUFTI.getAcqMode() );

	ddlbw         = _w.sourceMagnitude;
	ddlbw.setValue( instUFTI.getSourceMagnitude() );

	_updateFilterWidgets();
	_updateScienceFOV();
	_updateExpWidgets();

	super._updateWidgets();
	_edStareCapability._updateWidgets(this, instUFTI.getStareCapability());
*/
    }

    /**
     * Update the science field of view based upon the camera and mask
     * settings.
     */
    private void _updateScienceFOV() {
/*
	SpInstUFTI  instUFTI = (SpInstUFTI) _spItem;
	TextBoxWidgetExt    tbw = _w.scienceFOV;   
	double[] scienceArea = instUFTI.getScienceArea();
	tbw.setText(scienceArea[0] + " x " + scienceArea[1]);
*/
    }

    /**
     * Update the exposure time and coadds widgets
     */
    private void _updateExpWidgets() {
/*
	SpInstUFTI instUFTI = (SpInstUFTI) _spItem;
	TextBoxWidgetExt tbw = _w.exposureTime;
	double d = instUFTI.getExpTime();
	String e = Double.toString(d);
	//   _instUFTI.setExpTime(e);
	tbw.setText (e);

	//GroupWidget stareGW = (GroupWidget) _pres.getWidget("stareControlGroup");
	tbw = _w.coadds; 
	int coadds = instUFTI.getNoCoadds();
	// _instUFTI.setNoCoadds(coadds);
	tbw.setText (Integer.toString(coadds));

      System.out.println("_updateExpWidgets has been called. exposure = " + instUFTI.getExpTime() + ", coadds = " + instUFTI.getNoCoadds());
*/
    }


    /**
     * Observer of TableWidget selections.
     */
    public void tableRowSelected(TableWidgetExt twe, int rowIndex) {
/*
	SpInstUFTI instUFTI = (SpInstUFTI) _spItem;

	String  filter = (String) twe.getCell(0, rowIndex);

	// Don't set the value if the new selection is the same as the old
	// (otherwise, we'd fool the OT into thinking a change had been made)
	String curValue = instUFTI.getFilter();
	if ((curValue != null) && (curValue.equals(filter))) {
	    return;
	}

	instUFTI.setFilter(filter);

	JLabel stw = _w.filter;
	stw.setText(filter);
*/
    }

    /**
     * Must watch table widget actions as part of the TableWidgetWatcher
     * interface, but don't care about them.
     */
    public void	tableAction(TableWidgetExt twe, int colIndex, int rowIndex) {}

    /**
     * The given filter list was selected.  Show it, and if the current
     * filter is in the list, highlight it.
     */
    private void _selectFilterType(LookUpTable farray) {
/*
	_showFilterType(farray);
	String filter = ((SpInstUFTI) _spItem).getFilter();
	if (filter != null) {
	    int index = _getFilterIndex(filter, farray);
	    if (index != -1) {
		TableWidgetExt tw = _w.filterTable;
		tw.selectRowAt(index);
		tw.focusAtRow(index);
	    }
	}
*/
    }

    /** Return the position angle text box */
    public TextBoxWidgetExt getPosAngleTextBox() {
      // TODO MFO quick fix
      //javax.swing.JOptionPane.showMessageDialog(_w, "The method getPosAngleTextBox has been been called.\n" +
      //                                                  "But it is not implemented by EdCompInstUFTI.\n" +
      //						"Empty text box 0 is returned.");
      return new TextBoxWidgetExt();//_w.posAngle;
    }

    /** Return the exposure time text box */
    public TextBoxWidgetExt getExposureTimeTextBox() {
	// return _w.exposureTime;
	// TODO MFO quick fix
	return new TextBoxWidgetExt();	
    }

    /** Return the coadds text box. */

    public TextBoxWidgetExt getCoaddsTextBox() {
	//return _w.coadds;
	// TODO MFO quick fix
	return new TextBoxWidgetExt();
    }

    /**
     * Handle action events (for checkbuttons).
     */

    public void actionPerformed(ActionEvent evt) {
      if(ignoreActions) return;

      Object source = evt.getSource();
      if(source == _w.frontEnd.getSideBandButton()) {
        _addListenersToSideBandDisplay();
      }
      else if(_w.frontEnd.getSideBandDisplayWidgets().contains(source)) {
        _updateSpItem();
      }
/*
	Object w  = evt.getSource();
   
	if (w == _w.filterBroadBand) {
	    _selectFilterType( SpInstUFTI.BROAD_BAND_FILTERS );
	    return;
	} 
	else if (w == _w.filterNarrowBand) {
	    _selectFilterType(SpInstUFTI.NARROW_BAND_FILTERS);
	    return;
	} 
	else if (w == _w.filterSpecial) {
	    _selectFilterType(SpInstUFTI.SPECIAL_FILTERS);
	    return;
	}
*/
    }

   /**
    * Called when an item in a DropDownListBoxWidgetExt is selected.
    */

    public void dropDownListBoxSelect(DropDownListBoxWidgetExt ddlbwe, int index, String val) {
    }

   /**
    * Called when an item in a DropDownListBoxWidgetExt is double clicked.
    */

    public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbw, int index, String val) {
/*
	SpInstUFTI instUFTI = (SpInstUFTI) _spItem;

	if (ddlbw == _w.readoutArea) {
	    instUFTI.setReadoutArea(val);
	    _updateScienceFOV();

	    TelescopePosEditor tpe = TpeManager.get(_spItem);
	    if (tpe != null) tpe.repaint();

	    return;
	}

	if (ddlbw == _w.acquisitionMode) {
	    instUFTI.setAcqMode(val);
	    _updateScienceFOV();

	    TelescopePosEditor tpe = TpeManager.get(_spItem);
	    if (tpe != null) tpe.repaint();

	    return;
	}

	if (ddlbw == _w.sourceMagnitude) {
	    instUFTI.setSourceMagnitude(val);
	    _updateExpWidgets();
	    return;
	}

	if(ddlbw == _w.polariser) {
          instUFTI.setPolariser(val);
          _updateWidgets();

	  return;
	}
*/
    }


    /**
     * Hack to provide SpInstHeterodyne with reference to this object.
     *
     * Needed for preliminary implementation of XML output.
     * (to XML from Dennis' frequency editor into the OT XML output)
     *
     */
    //public void	setup(SpItem spItem) {
    //  super.setup(spItem);
//
//      ((SpInstHeterodyne)spItem).setFrontEnd(_w.frontEnd);
//    }

  public void changedUpdate(DocumentEvent e) {
    if(ignoreActions) return;

    _updateSpItem();
  }
  
  public void insertUpdate(DocumentEvent e) { }
  public void removeUpdate(DocumentEvent e) { }
 
  public void mouseClicked(MouseEvent e) { }
  public void mouseEntered(MouseEvent e) { }
  public void mouseExited(MouseEvent e) { }
  public void mousePressed(MouseEvent e) { }
  public void mouseReleased(MouseEvent e) {
    if(ignoreActions) return;

    _updateSpItem();
  }
 
  public void itemStateChanged(ItemEvent e) {
    if(ignoreActions) return;

    if(e.getSource() == _w.frontEnd.getFeBandModeComboBox()) {
      _addListenersToSideBandDisplay();
    }

    _updateSpItem();
  }
}
