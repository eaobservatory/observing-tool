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
import orac.ukirt.inst.SpInstUFTI ;

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
import java.awt.Color ;

/**
 * This is the editor for the UFTI instrument.
 */
public final class EdCompInstUFTI extends EdCompInstBase implements TableWidgetWatcher , ActionListener
{
	private EdStareCapability _edStareCapability ;
	private SpInstUFTI _instUFTI ;
	private UftiGUI _w ;

	/**
	 * This flag is set true while _init is executed to prevent actionPerformed() to do react to
	 * action events caused by initializing widgets.
	 */
	private boolean _ignoreActionEvents = false ;

	private boolean validExposureTime = true ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdCompInstUFTI()
	{
		_title = "UKIRT Fast Track Imager" ;
		_presSource = _w = new UftiGUI() ;
		_description = "The UFTI instrument is configured with this component." ;

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


		_w.acqMode.setChoices( SpInstUFTI.MODES ) ;

		_w.readoutArea.setChoices( SpInstUFTI.READAREAS.getColumn( 0 ) ) ;

		_w.filterTable.setBackground(_w.getBackground());
		_w.filterTable.setColumnHeaders(new String[] {"Filter", "Wavel.(um)"});
		_w.filterTable.addWatcher(this);

		/*
		 *  Polariser
		 */
		_w.polariser.setChoices( SpInstUFTI.POLARISERS.getColumn( 0 ) ) ;

		_w.polariser.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxSelect( DropDownListBoxWidgetExt dd , int i , String val ){}

			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instUFTI.setPolariser( val ) ;
				_updateWidgets() ;
			}
		} ) ;

		/*
		 *  Source magnitude
		 */
		_w.sourceMag.setChoices( SpInstUFTI.SRCMAGS ) ;

		_w.sourceMag.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxSelect( DropDownListBoxWidgetExt dd , int i , String val ){}

			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_instUFTI.setSourceMagnitude( val ) ;
				_instUFTI.useDefaultAcquisition() ;
				_updateExpWidgets() ;
				_updateExpInfo() ;
			}
		} ) ;

		_w.exposureTime.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				validExposureTime = checkExposureTimes( tbw.getText() ) ;
				_updateExpWidgets( false ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){}
		} ) ;

		_w.coadds.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbw )
			{
				_instUFTI.setNoCoadds( tbw.getText() ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbw ){}
		} ) ;

		_w.defaultAcquisition.addWatcher( new CommandButtonWidgetWatcher()
		{
			public void commandButtonAction( CommandButtonWidgetExt cbwe )
			{
				_instUFTI.useDefaultAcquisition() ;
				_updateExpWidgets() ;
				_updateExpInfo() ;
			}
		} ) ;

		// MFO: Watcher is now added here rather than in _edStareCapability._init
		_w.coadds.addWatcher( new TextBoxWidgetWatcher()
		{
			public void textBoxKeyPress( TextBoxWidgetExt tbwe )
			{
				_instUFTI.setNoCoadds( tbwe.getText() ) ;
			}

			public void textBoxAction( TextBoxWidgetExt tbwe ){} // ignore
		} ) ;

		_ignoreActionEvents = false ;
	}

	/**
	 * Initialize the Filter table widget according to the selected
	 * filter category.
	 */
	private void _showFilterType( LookUpTable filters )
	{
		Vector<String>[]rowsV = filters.getAsVectorArray() ;
		_w.filterTable.setRows(rowsV);
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
		String filter = _instUFTI.getFilter() ;
		_w.filter.setText( filter ) ;

		// See which type of filter the selected filter is, if any.
		LookUpTable farray = null ;
		OptionWidgetExt ow = null ;

		int index = -1 ;
		if( filter == null || filter.equals( "None" ) )
		{
			farray = SpInstUFTI.BROAD_BAND_FILTERS ;
			ow = _w.filterBroadBand ;
		}
		else
		{

			farray = SpInstUFTI.BROAD_BAND_FILTERS ;
			index = _getFilterIndex( filter , farray ) ;
			if( index != -1 )
			{
				ow = _w.filterBroadBand ;
			}
			else
			{
				farray = SpInstUFTI.NARROW_BAND_FILTERS ;
				index = _getFilterIndex( filter , farray ) ;
				if( index != -1 )
				{
					ow = _w.filterNarrowBand ;
				}
				else
				{
					farray = SpInstUFTI.SPECIAL_FILTERS ;
					index = _getFilterIndex( filter , farray ) ;
					if( index != -1 )
					{
						ow = _w.filterSpecial ;
					}
					else
					{
						farray = SpInstUFTI.BROAD_BAND_FILTERS ;
						ow = _w.filterBroadBand ;
					}
				}
			}
		}

		// Show the correct filters, and select the option widget for the type.
		_showFilterType( farray ) ;
		ow.setValue( true ) ;

		// Select the filter in the table.
		if( ( filter != null ) && ( index != -1 ) )
		{
			_w.filterTable.selectRowAt(index);
			_w.filterTable.focusAtRow(index);
		}

		_ignoreActionEvents = false ;
	}

	/**
	 * Override setup to store away a reference to the SpInstUFTI item.
	 */
	public void setup( SpItem spItem )
	{
		_instUFTI = ( SpInstUFTI )spItem ;
		super.setup( spItem ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		_ignoreActionEvents = true ;

		_w.polariser.setValue(_instUFTI.getPolariser());

		_w.acqMode.setValue(_instUFTI.getAcqMode());

		_w.readoutArea.setValue(_instUFTI.getReadoutArea());

		_edStareCapability._updateWidgets( this , _instUFTI.getStareCapability() ) ;

		_w.sourceMag.setValue(_instUFTI.getSourceMagnitude());

		_updateScienceFOV() ;

		// Reset _ignoreActionEvents to true after it has been set to false at the end of _updateScienceFOV()
		_ignoreActionEvents = true ;

		super._updateWidgets() ;

		_updateFilterWidgets() ;
		_updateExpWidgets() ;

		_ignoreActionEvents = false ;
	}

	/*
	 *  Update the exposure-time and coadds widgets.
	 */
	private void _updateExpWidgets()
	{
		_updateExpWidgets( true ) ;
	}

	private void _updateExpWidgets( boolean setText )
	{
		_ignoreActionEvents = true ;

		if( setText )
		{
			TextBoxWidgetExt tbw = _w.exposureTime ;
			double d = _instUFTI.getExpTime() ;
			String e = Double.toString( d ) ;
			tbw.setText( e ) ;
			validExposureTime = checkExposureTimes( _instUFTI.getExposureTimeAsString() ) ;
		}

		int coadds = _instUFTI.getNoCoadds() ;
		_w.coadds.setText( Integer.toString( coadds ) ) ;

		if( validExposureTime )
		{
			_w.exposureTimeLabel.setText( "Exposure Time" ) ;
			_w.exposureTimeLabel.setForeground( Color.black ) ;
		}
		else
		{
			_w.exposureTimeLabel.setText( "Exposure Time Invalid" ) ;
			_w.exposureTimeLabel.setForeground( Color.red ) ;
		}

		_ignoreActionEvents = false ;
	}

	//
	// Update the exposure-time and coadds attributes.
	//
	private void _updateExpInfo()
	{
		_instUFTI.setExpTime(_w.exposureTime.getText());
		_instUFTI.setNoCoadds(_w.coadds.getText());
	}

	//
	// Update the science field of view based upon the readout area.
	//
	private void _updateScienceFOV()
	{
		_ignoreActionEvents = true ;

		double[] scienceArea = _instUFTI.getScienceArea() ;
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
		// (otherwise, we'd fool the OT into thinking a change had been made).
		String curValue = _instUFTI.getFilter() ;
		if( ( curValue != null ) && ( curValue.equals( filter ) ) )
			return ;

		// Set filter and also set the lambda instrument aperture value.
		_instUFTI.setFilter( filter ) ;
		_instUFTI.setInstApL( lambda ) ;

		_w.filter.setText( filter ) ;
		_instUFTI.useDefaultAcquisition() ;
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
		String filter = _instUFTI.getFilter() ;
		if( filter != null )
		{
			int index = _getFilterIndex( filter , farray ) ;
			if( index != -1 )
			{
				_w.filterTable.selectRowAt(index);
				_w.filterTable.focusAtRow(index);
			}
		}
	}

	/**
	 *
	 */
	public void actionPerformed( ActionEvent evt )
	{
		if( _ignoreActionEvents )
			return ;

		Object w = evt.getSource() ;

		if( w == _w.acqMode )
		{
			DropDownListBoxWidgetExt ddlbw = ( DropDownListBoxWidgetExt )w ;
			_instUFTI.setAcqMode( ddlbw.getStringValue() ) ;
			validExposureTime = checkExposureTimes( _instUFTI.getExposureTimeAsString() ) ;
			_updateExpWidgets() ;
		}
		else if( _w.readoutArea == w )
		{
			DropDownListBoxWidgetExt ddlbw = ( DropDownListBoxWidgetExt )w ;
			_instUFTI.setReadoutArea( ddlbw.getStringValue() ) ;
			_updateScienceFOV() ;
			TelescopePosEditor tpe = TpeManager.get( _instUFTI ) ;
			if( tpe != null )
				tpe.repaint() ;

			validExposureTime = checkExposureTimes( _instUFTI.getExposureTimeAsString() ) ;
			_updateExpWidgets() ;
		}
		else if( _w.filterBroadBand == w )
		{
			_selectFilterType( SpInstUFTI.BROAD_BAND_FILTERS ) ;
		}
		else if( _w.filterNarrowBand == w )
		{
			_selectFilterType( SpInstUFTI.NARROW_BAND_FILTERS ) ;
		}
		else if( _w.filterSpecial == w )
		{
			_selectFilterType( SpInstUFTI.SPECIAL_FILTERS ) ;
		}
	}

	/** Return the position angle text box */
	public TextBoxWidgetExt getPosAngleTextBox()
	{
		// UFTI does not have a position angle text box.
		return new TextBoxWidgetExt() ;
	}

	/** Return the exposure time text box */
	public TextBoxWidgetExt getExposureTimeTextBox()
	{
		return null ; // _w.exposureTime ;
	}

	/** Return the coadds text box, or null if not available. */
	public TextBoxWidgetExt getCoaddsTextBox()
	{
		return _w.coadds ;
	}

	private boolean checkExposureTimes( String candidate )
	{
		boolean returnValue = false ;
		if( candidate.matches( "\\d*\\.?\\d*" ) && !candidate.equals( "" ) )
		{
			String[][] exposureTimeLimits = SpInstUFTI.EXPTIME_LIMITS ;
			int readout = 0 ;
			int acquisition = 1 ;
			int min = 2 ;
			int max = 3 ;
			boolean found = false ;
			double minExposure = 0. ;
			double maxExposure = 0. ;
			// only along one dimension for no apparent reason
			for( int i = 0 ; i < exposureTimeLimits.length ; i++ )
			{
				String[] current = exposureTimeLimits[ i ] ;
				if( current[ readout ].equals( _instUFTI.getReadoutArea() ) )
				{
					if( current[ acquisition ].equals( _instUFTI.getAcqMode() ) )
					{
						minExposure = new Double( current[ min ] ) ;
						maxExposure = new Double( current[ max ] ) ;
						found = true ;
						break ;
					}
				}
			}
			if( found )
			{
				double candidateDouble = new Double( candidate ) ;
				if( minExposure <= candidateDouble && candidateDouble <= maxExposure )
				{
					// the following setExpTime is redundant BUT may com in useful later
					_instUFTI.setExposureTime( candidateDouble ) ;
					returnValue = true ;
				}
				else
				{
					if( minExposure > candidateDouble )
						_instUFTI.setExposureTime( minExposure ) ;
					else
						_instUFTI.setExposureTime( maxExposure ) ;
				}
			}
		}
		return returnValue ;
	}
}
