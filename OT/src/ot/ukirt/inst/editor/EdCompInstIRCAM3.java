/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package ot.ukirt.inst.editor ;

import orac.util.LookUpTable ;
import orac.ukirt.inst.SpInstIRCAM3 ;

import gemini.sp.SpItem ;
import jsky.app.ot.gui.TableWidgetExt ;
import jsky.app.ot.gui.TableWidgetWatcher ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetWatcher ;
import jsky.app.ot.gui.OptionWidgetExt ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import java.util.Vector ;

import jsky.app.ot.tpe.TelescopePosEditor ;
import jsky.app.ot.tpe.TpeManager ;

import javax.swing.ButtonGroup ;

/**
 * This is the editor for the IRCAM3 instrument.
 */
public final class EdCompInstIRCAM3 extends EdCompInstBase implements TableWidgetWatcher , ActionListener
{
	private EdStareCapability _edStareCapability ;
	private SpInstIRCAM3 _instIRCAM3 ;
	private Ircam3GUI _w ;

	/**
	 * This flag is set true while _init is executed to prevent actionPerformed() to do react to
	 * action events caused by initializing widgets.
	 */
	private boolean _ignoreActionEvents = false ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdCompInstIRCAM3()
	{
		_title = "UKIRT Thermal Imager" ;
		_presSource = _w = new Ircam3GUI() ;
		_description = "The IRCAM3 instrument is configured with this component." ;

		_edStareCapability = new EdStareCapability() ;

		ButtonGroup grp = new ButtonGroup() ;
		grp.add( _w.filterBroadBand ) ;
		grp.add( _w.filterNarrowBand ) ;
		grp.add( _w.filterSpecial ) ;

		_w.filterBroadBand.addActionListener( this ) ;
		_w.filterNarrowBand.addActionListener( this ) ;
		_w.filterSpecial.addActionListener( this ) ;
		_w.acqMode.addActionListener( this ) ;
		_w.readoutArea.addActionListener( this ) ;

		_ignoreActionEvents = true ;

		_w.acqMode.setChoices( SpInstIRCAM3.MODES ) ;

		_w.readoutArea.setChoices( SpInstIRCAM3.READAREAS.getColumn( 0 ) ) ;

		_w.filterTable.setBackground( _w.getBackground() ) ;
		_w.filterTable.setColumnHeaders( new String[] { "Filter" , "Wavel.(um)" } ) ;
		_w.filterTable.addWatcher( this ) ;

		// Polariser
		_w.polariser.setChoices( SpInstIRCAM3.POLARISERS.getColumn( 0 ) ) ;

		_w.polariser.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instIRCAM3.setPolariser( val ) ;
				_updateWidgets() ;
			}
		} ) ;

		// Source magnitude
		_w.sourceMag.setChoices( SpInstIRCAM3.SRCMAGS ) ;

		_w.sourceMag.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instIRCAM3.setSourceMagnitude( val ) ;
				_instIRCAM3.useDefaultAcquisition() ;
				_updateExpWidgets() ;
				_updateExpInfo() ;
			}
		} ) ;

		_w.exposureTime.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				_instIRCAM3.setExpTime( tbw.getText() ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){}
		} ) ;

		_w.coadds.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				_instIRCAM3.setNoCoadds( tbw.getText() ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){}
		} ) ;

		_w.defaultAcquisition.addWatcher( new CommandButtonWidgetWatcher()
		{
			public void commandButtonAction( CommandButtonWidgetExt cbwe )
			{
				_instIRCAM3.useDefaultAcquisition() ;
				_updateExpWidgets() ;
				_updateExpInfo() ;
			}
		} ) ;

		_edStareCapability._init( this ) ;

		_ignoreActionEvents = false ;
	}

	/**
	 * Initialize the Filter table widget according to the selected
	 * filter category.
	 */
	private void _showFilterType( LookUpTable filters )
	{
		Vector<String>[] rowsV = filters.getAsVectorArray() ;
		_w.filterTable.setRows( rowsV ) ;
	}

	/**
	 * Get the index of the filter in the given array, or -1 if the filter
	 * isn't in the array.
	 */
	private int _getFilterIndex( String filter , LookUpTable farray )
	{
		int fi = -1 ;
		try
		{
			fi = farray.indexInColumn( filter , 0 ) ;
		}
		catch( Exception ex ){}
		return fi ;
	}

	/**
	 * Update the filter choice related widgets.
	 */
	private void _updateFilterWidgets()
	{
		_ignoreActionEvents = true ;

		// First fill in the text box.
		String filter = _instIRCAM3.getFilter() ;
		_w.filter.setValue( filter ) ;

		// See which type of filter the selected filter is, if any.
		LookUpTable farray = null ;
		OptionWidgetExt ow = null ;

		int index = -1 ;
		if( filter == null || filter.equals( "None" ) )
		{
			farray = SpInstIRCAM3.BROAD_BAND_FILTERS ;
			ow = _w.filterBroadBand ;
		}
		else
		{
			farray = SpInstIRCAM3.BROAD_BAND_FILTERS ;
			index = _getFilterIndex( filter , farray ) ;
			if( index != -1 )
			{
				ow = _w.filterBroadBand ;
			}
			else
			{
				farray = SpInstIRCAM3.NARROW_BAND_FILTERS ;
				index = _getFilterIndex( filter , farray ) ;
				if( index != -1 )
				{
					ow = _w.filterNarrowBand ;
				}
				else
				{
					farray = SpInstIRCAM3.SPECIAL_FILTERS ;
					index = _getFilterIndex( filter , farray ) ;
					if( index != -1 )
					{
						ow = _w.filterSpecial ;
					}
					else
					{
						farray = SpInstIRCAM3.BROAD_BAND_FILTERS ;
						ow = _w.filterBroadBand ;
					}
				}
			}
		}

		// Show the correct filters, and select the option widget for the type.
		_showFilterType( farray ) ;
		ow.setValue( true ) ;

		// Select the filter in the table
		if( ( filter != null ) && ( index != -1 ) )
		{
			_w.filterTable.selectRowAt( index ) ;
			_w.filterTable.focusAtRow( index ) ;
		}

		_ignoreActionEvents = false ;
	}

	/**
	 * Override setup to store away a reference to the SpInstIRCAM3 item.
	 */
	public void setup( SpItem spItem )
	{
		_instIRCAM3 = ( SpInstIRCAM3 )spItem ;
		super.setup( spItem ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		_ignoreActionEvents = true ;
		_w.polariser.setValue( _instIRCAM3.getPolariser() ) ;
		_w.acqMode.setValue( _instIRCAM3.getAcqMode() ) ;
		_w.readoutArea.setValue( _instIRCAM3.getReadoutArea() ) ;
		_edStareCapability._updateWidgets( this , _instIRCAM3.getStareCapability() ) ;
		_w.sourceMag.setValue( _instIRCAM3.getSourceMagnitude() ) ;
		_updateScienceFOV() ;

		// Reset _ignoreActionEvents to true after it has been set to false at the end of _updateScienceFOV()
		_ignoreActionEvents = true ;

		super._updateWidgets() ;

		_updateFilterWidgets() ;
		_updateExpWidgets() ;

		_ignoreActionEvents = false ;
	}

	// Update the exposure time and coadds widgets.
	private void _updateExpWidgets()
	{
		_ignoreActionEvents = true ;

		double d = _instIRCAM3.getExpTime() ;
		String e = Double.toString( d ) ;
		_w.exposureTime.setText( e ) ;

		int coadds = _instIRCAM3.getNoCoadds() ;
		_w.coadds.setText( Integer.toString( coadds ) ) ;

		_w.acqMode.setValue( _instIRCAM3.getAcqMode() ) ;

		_ignoreActionEvents = false ;
	}

	// Update the exposure time and coadds attributes.
	private void _updateExpInfo()
	{
		_instIRCAM3.setExpTime( _w.exposureTime.getText() ) ;

		_instIRCAM3.setNoCoadds( _w.coadds.getText() ) ;

		_instIRCAM3.setAcqMode( _w.acqMode.getStringValue() ) ;
	}

	// Update the science field of view based upon the readout area.
	private void _updateScienceFOV()
	{
		_ignoreActionEvents = true ;

		double[] scienceArea = _instIRCAM3.getScienceArea() ;
		_w.scienceFOV.setText( scienceArea[ 0 ] + " x " + scienceArea[ 1 ] ) ;

		_ignoreActionEvents = false ;
	}

	/**
	 * Observer of TableWidget selections.
	 */
	public void tableRowSelected( TableWidgetExt twe , int rowIndex )
	{
		String filter = ( String )twe.getCell( 0 , rowIndex ) ;
		String lambda = ( String )twe.getCell( 1 , rowIndex ) ;

		// Don't set the value if the new selection is the same as the old
		// (otherwise, we'd fool the OT into thinking a change had been made)
		String curValue = _instIRCAM3.getFilter() ;
		if( ( curValue != null ) && ( curValue.equals( filter ) ) )
			return ;

		// Set filter and also set the lambda inst. aper value.
		_instIRCAM3.setFilter( filter ) ;
		_instIRCAM3.setInstApL( lambda ) ;

		_w.filter.setText( filter ) ;
		_instIRCAM3.useDefaultAcquisition() ;
		_updateExpWidgets() ;
		_updateExpInfo() ;
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
	private void _selectFilterType( LookUpTable farray )
	{
		_showFilterType( farray ) ;
		String filter = _instIRCAM3.getFilter() ;
		if( filter != null )
		{
			int index = _getFilterIndex( filter , farray ) ;
			if( index != -1 )
			{
				_w.filterTable.selectRowAt( index ) ;
				_w.filterTable.focusAtRow( index ) ;
			}
		}
	}

	/**
	 *
	 */
	public void actionPerformed( ActionEvent evt )
	{
		if( !_ignoreActionEvents )
		{
			Object w = evt.getSource() ;
	
			if( w == _w.acqMode )
			{
				DropDownListBoxWidgetExt ddlbw = ( DropDownListBoxWidgetExt )w ;
				_instIRCAM3.setAcqMode( ddlbw.getStringValue() ) ;
			}
			else if( w == _w.readoutArea )
			{
				DropDownListBoxWidgetExt ddlbw = ( DropDownListBoxWidgetExt )w ;
				_instIRCAM3.setReadoutArea( ddlbw.getStringValue() ) ;
				_updateScienceFOV() ;
				TelescopePosEditor tpe = TpeManager.get( _instIRCAM3 ) ;
				if( tpe != null )
					tpe.repaint() ;
			}
			else if( w == _w.filterBroadBand )
			{
				_selectFilterType( SpInstIRCAM3.BROAD_BAND_FILTERS ) ;
			}
			else if( w == _w.filterNarrowBand )
			{
				_selectFilterType( SpInstIRCAM3.NARROW_BAND_FILTERS ) ;
			}
			else if( w == _w.filterSpecial )
			{
				_selectFilterType( SpInstIRCAM3.SPECIAL_FILTERS ) ;
			}
		}
	}

	/** Return the position angle text box */
	public TextBoxWidgetExt getPosAngleTextBox()
	{
		// IRCAM3 does not have a position angle text box.
		return new TextBoxWidgetExt() ;
	}

	/** Return the exposure time text box */
	public TextBoxWidgetExt getExposureTimeTextBox()
	{
		return _w.exposureTime ;
	}

	/** Return the coadds text box, or null if not available. */
	public TextBoxWidgetExt getCoaddsTextBox()
	{
		return _w.coadds ;
	}
}
