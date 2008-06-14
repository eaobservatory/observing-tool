// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.inst.editor ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.util.Vector ;
import javax.swing.ButtonGroup ;
import javax.swing.JLabel ;
import jsky.app.ot.gemini.inst.SpInstNIRI ;
import jsky.app.ot.gemini.inst.SpInstNIRIConstants ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher ;
import jsky.app.ot.gui.OptionWidgetExt ;
import jsky.app.ot.gui.TableWidgetExt ;
import jsky.app.ot.gui.TableWidgetWatcher ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.tpe.TelescopePosEditor ;
import jsky.app.ot.tpe.TpeManager ;

/**
 * This is the editor for Scheduling Info component.
 */
public final class EdCompInstNIRI extends EdCompInstBase implements TableWidgetWatcher , DropDownListBoxWidgetWatcher , ActionListener
{
	private EdStareCapability _edStareCapability ;
	private NiriGUI _w ; // the GUI layout

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdCompInstNIRI()
	{
		_title = "Near IR Imager" ;
		_presSource = _w = new NiriGUI() ;
		_description = "The NIRI instrument is configured with this component." ;

		_edStareCapability = new EdStareCapability() ;

		ButtonGroup grp = new ButtonGroup() ;
		grp.add( _w.filterBroadBand ) ;
		grp.add( _w.filterNarrowBand ) ;
		grp.add( _w.filterSpecial ) ;

		_w.filterBroadBand.addActionListener( this ) ;
		_w.filterNarrowBand.addActionListener( this ) ;
		_w.filterSpecial.addActionListener( this ) ;
	}

	/**
	 * This method initializes the widgets in the presentation to reflect the
	 * current values of the items attributes.
	 */
	protected void _init()
	{
		DropDownListBoxWidgetExt ddlbw ;

		ddlbw = _w.camera ;
		ddlbw.setChoices( SpInstNIRIConstants.CAMERAS ) ;

		ddlbw = _w.disperser ;
		ddlbw.setChoices( SpInstNIRIConstants.DISPERSERS ) ;

		ddlbw = _w.mask ;
		ddlbw.setChoices( SpInstNIRIConstants.MASKS ) ;

		_w.camera.addWatcher( this ) ;
		_w.mask.addWatcher( this ) ;
		_w.disperser.addWatcher( this ) ;

		TableWidgetExt twe ;
		twe = _w.filterTable ;
		twe.setBackground( _w.getBackground() ) ;
		twe.setColumnHeaders( new String[] { "Filter" , "Wavel.(um)" } ) ;
		twe.addWatcher( this ) ;

		super._init() ;
		_edStareCapability._init( this ) ;
	}

	/**
	 * Initialize the Filter table widget according to the selected
	 * filter category.
	 */
	private void _showFilterType( String[][] filters )
	{
		Vector[] rowsV = new Vector[ filters.length ] ;

		for( int i = 0 ; i < filters.length ; ++i )
		{
			String[] row = filters[ i ] ;
			Vector rowV = new Vector( row.length ) ;
			for( int j = 0 ; j < row.length ; ++j )
				rowV.addElement( row[ j ] ) ;

			rowsV[ i ] = rowV ;
		}

		TableWidgetExt tw = _w.filterTable ;
		tw.setRows( rowsV ) ;
	}

	/**
	 * Get the index of the filter in the given array, or -1 if the filter
	 * isn't in the array.
	 */
	private int _getFilterIndex( String filter , String[][] farray )
	{
		for( int i = 0 ; i < farray.length ; ++i )
		{
			if( filter.equals( farray[ i ][ 0 ] ) )
				return i ;
		}
		return -1 ;
	}

	/**
	 * Update the filter choice related widgets.
	 */
	private void _updateFilterWidgets()
	{
		// First fill in the text box.
		JLabel stw = _w.filter ;
		String filter = ( ( SpInstNIRI )_spItem ).getFilter() ;
		stw.setText( filter ) ;

		// See which type of filter the selected filter is, if any.
		String[][] farray = null ;
		OptionWidgetExt ow = null ;

		int index = -1 ;
		if( filter == null )
		{
			farray = SpInstNIRIConstants.BROAD_BAND_FILTERS ;
			ow = _w.filterBroadBand ;
		}
		else
		{
			farray = SpInstNIRIConstants.BROAD_BAND_FILTERS ;
			index = _getFilterIndex( filter , farray ) ;
			if( index != -1 )
			{
				ow = _w.filterBroadBand ;
			}
			else
			{
				farray = SpInstNIRIConstants.NARROW_BAND_FILTERS ;
				index = _getFilterIndex( filter , farray ) ;
				if( index != -1 )
				{
					ow = _w.filterNarrowBand ;
				}
				else
				{
					farray = SpInstNIRIConstants.SPECIAL_FILTERS ;
					index = _getFilterIndex( filter , farray ) ;
					if( index != -1 )
						ow = _w.filterSpecial ;
					else
						ow = _w.filterBroadBand ;
				}
			}
		}

		// Show the correct filters, and select the option widget for the type
		_showFilterType( farray ) ;
		ow.setValue( true ) ;

		// Select the filter in the table
		if( ( filter != null ) && ( index != -1 ) )
		{
			TableWidgetExt tw = _w.filterTable ;
			tw.selectRowAt( index ) ;
			tw.focusAtRow( index ) ;
		}
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		SpInstNIRI instNIRI = ( SpInstNIRI )_spItem ;

		DropDownListBoxWidgetExt ddlbw ;

		ddlbw = _w.camera ;
		ddlbw.setValue( instNIRI.getCamera() ) ;

		ddlbw = _w.disperser ;
		ddlbw.setValue( instNIRI.getDisperser() ) ;

		ddlbw = _w.mask ;
		ddlbw.setValue( instNIRI.getMask() ) ;

		_updateFilterWidgets() ;
		_updateScienceFOV() ;

		super._updateWidgets() ;
		_edStareCapability._updateWidgets( this , instNIRI.getStareCapability() ) ;
	}

	//
	// Update the science field of view based upon the camera and mask
	// settings.
	//
	private void _updateScienceFOV()
	{
		SpInstNIRI instNIRI = ( SpInstNIRI )_spItem ;
		TextBoxWidgetExt tbw = _w.scienceFOV ;
		double[] scienceArea = instNIRI.getScienceArea() ;
		tbw.setText( scienceArea[ 0 ] + " x " + scienceArea[ 1 ] ) ;
	}

	/**
	 * Observer of TableWidget selections.
	 */
	public void tableRowSelected( TableWidgetExt twe , int rowIndex )
	{
		SpInstNIRI instNIRI = ( SpInstNIRI )_spItem ;

		String filter = ( String )twe.getCell( 0 , rowIndex ) ;

		// Don't set the value if the new selection is the same as the old
		// (otherwise, we'd fool the OT into thinking a change had been made)
		String curValue = instNIRI.getFilter() ;
		if( ( curValue != null ) && ( curValue.equals( filter ) ) )
			return ;

		instNIRI.setFilter( filter ) ;

		JLabel stw = _w.filter ;
		stw.setText( filter ) ;
	}

	/**
	 * Must watch table widget actions as part of the TableWidgetWatcher
	 * interface, but don't care about them.
	 */
	public void tableAction( TableWidgetExt twe , int colIndex , int rowIndex ){}

	/**
	 * The given filter list was selected.  Show it, and if the current
	 * filter is in the list, highlight it.
	 */
	private void _selectFilterType( String[][] farray )
	{
		_showFilterType( farray ) ;
		String filter = ( ( SpInstNIRI )_spItem ).getFilter() ;
		if( filter != null )
		{
			int index = _getFilterIndex( filter , farray ) ;
			if( index != -1 )
			{
				TableWidgetExt tw = _w.filterTable ;
				tw.selectRowAt( index ) ;
				tw.focusAtRow( index ) ;
			}
		}
	}

	/** Return the position angle text box */
	public TextBoxWidgetExt getPosAngleTextBox()
	{
		return _w.posAngle ;
	}

	/** Return the exposure time text box */
	public TextBoxWidgetExt getExposureTimeTextBox()
	{
		return _w.exposureTime ;
	}

	/** Return the coadds text box. */
	public TextBoxWidgetExt getCoaddsTextBox()
	{
		return _w.coadds ;
	}

	/**
	 * Handle action events (for checkbuttons).
	 */
	public void actionPerformed( ActionEvent evt )
	{
		Object w = evt.getSource() ;

		if( w == _w.filterBroadBand )
		{
			_selectFilterType( SpInstNIRIConstants.BROAD_BAND_FILTERS ) ;
			return ;
		}
		else if( w == _w.filterNarrowBand )
		{
			_selectFilterType( SpInstNIRIConstants.NARROW_BAND_FILTERS ) ;
			return ;
		}
		else if( w == _w.filterSpecial )
		{
			_selectFilterType( SpInstNIRIConstants.SPECIAL_FILTERS ) ;
			return ;
		}
	}

	/**
	 * Called when an item in a DropDownListBoxWidgetExt is selected.
	 */
	public void dropDownListBoxSelect( DropDownListBoxWidgetExt ddlbwe , int index , String val ){}

	/**
	 * Called when an item in a DropDownListBoxWidgetExt is double clicked.
	 */
	public void dropDownListBoxAction( DropDownListBoxWidgetExt ddlbw , int index , String val )
	{
		SpInstNIRI instNIRI = ( SpInstNIRI )_spItem ;

		if( ddlbw == _w.camera )
		{
			instNIRI.setCamera( val ) ;
			_updateScienceFOV() ;

			TelescopePosEditor tpe = TpeManager.get( _spItem ) ;
			if( tpe != null )
				tpe.repaint() ;
		}
		else if( ddlbw == _w.mask )
		{
			instNIRI.setMask( val ) ;
			_updateScienceFOV() ;

			TelescopePosEditor tpe = TpeManager.get( _spItem ) ;
			if( tpe != null )
				tpe.repaint() ;
		}
		else if( ddlbw == _w.disperser )
		{
			instNIRI.setDisperser( val ) ;
		}
	}
}
