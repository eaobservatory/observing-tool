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
import java.util.Vector;
import javax.swing.*;
import orac.jcmt.inst.SpInstSCUBA;
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

// TODO MFO: check whether JCMT/SCUBA needs its own EdCompInstBase, whether it can use the UKIRT EdCompInstBase
// (in which case it should go into a package that is not instrument specific) or whether it is sufficient just to
// subclass OtItemEditor (as in EdCompInstHeterodyne).
import ot.ukirt.inst.editor.EdCompInstBase;

/**
 * This is the editor for the Scuba instrument component.
 *
 * @author Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdCompInstSCUBA extends EdCompInstBase
    implements TableWidgetWatcher, DropDownListBoxWidgetWatcher, ActionListener {

//    private EdStareCapability _edStareCapability;

    private ScubaGUI _w;		// the GUI layout


    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdCompInstSCUBA() {
	_title       ="JCMT SCUBA";
	_presSource  = _w = new ScubaGUI();
	_description ="The SCUBA instrument is configured with this component.";
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
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
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

}
