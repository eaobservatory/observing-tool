// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// author: Alan Pickup = dap@roe.ac.uk         2001 Feb
// modified for Swing OT by
//         Martin Folger = M.Folger@roe.ac.uk  2001 Apr
// modified for UIST (from Michelle) by
//         Alan Pickup = A.Pickup              2001 Nov
// modified for WFCAM (from UIST) by
//         Alan Pickup = A.Pickup              2003 Mar
//
// $Id$
//
package ot.ukirt.iter.editor ;

import orac.ukirt.iter.SpIterWFCAMCalObs ;

import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

import jsky.app.ot.editor.OtItemEditor ;

/**
 * This is the editor for the WFCAM Calibration Observation iterator.
 */
public final class EdIterWFCAMCalObs extends OtItemEditor implements TextBoxWidgetWatcher , DropDownListBoxWidgetWatcher , ActionListener
{
	private IterWFCAMCalObsGUI _w ; // the GUI layout

	/**
	 * This flag is set true while methods like _init is executed to prevent actionPerformed() to do react to
	 * action events caused by initializing widgets.
	 */
	private boolean _ignoreActionEvents = false ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterWFCAMCalObs()
	{
		_title = "WFCAM Calibration Observation" ;
		_presSource = _w = new IterWFCAMCalObsGUI() ;
		_description = "Configure a WFCAM FLAT or FOCUS observation with this component." ;

		for( int i = 0 ; i < 100 ; i++ )
			_w.repeatComboBox.addItem( "" + ( i + 1 ) ) ;

		_w.CalType.addActionListener( this ) ;
		_w.ReadMode.addActionListener( this ) ;
		_w.Filter.addActionListener( this ) ;
		_w.repeatComboBox.addActionListener( this ) ;
		_w.useDefaults.addActionListener( this ) ;
	}

	/**
	 */
	protected void _init()
	{
		_ignoreActionEvents = true ;

		TextBoxWidgetExt tbw ;

		// Exposure time
		tbw = ( TextBoxWidgetExt )_w.ExpTime ;
		tbw.addWatcher( this ) ;

		// Coadds
		tbw = ( TextBoxWidgetExt )_w.Coadds ;
		tbw.addWatcher( this ) ;

		// Focus
		tbw = ( TextBoxWidgetExt )_w.focusPos ;
		tbw.addWatcher( this ) ;

		super._init() ;

		_ignoreActionEvents = false ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */

	protected void _updateWidgets()
	{
		_updateWidgets( null ) ;
	}

	protected void _updateWidgets( Object source )
	{
		_ignoreActionEvents = true ;

		DropDownListBoxWidgetExt ddlbw ;

		SpIterWFCAMCalObs ico = ( SpIterWFCAMCalObs )_spItem ;

		// Get the choices and defaults from the instrument.

		// Update calType selection box
		ddlbw = ( DropDownListBoxWidgetExt )_w.CalType ;
		ddlbw.setChoices( ico.getCalTypeChoices() ) ;
		ddlbw.setValue( ico.getCalType() ) ;
		if( ico.getCalType() == SpIterWFCAMCalObs.FOCUS || ico.getCalType() == SpIterWFCAMCalObs.FOCUS_TEL )
		{
			_w.focusPos.setVisible( true ) ;
			_w.focusLabel.setVisible( true ) ;
			_w.focusPos.setEditable( true ) ;
		}
		else
		{
			_w.focusPos.setVisible( false ) ;
			_w.focusLabel.setVisible( false ) ;
			_w.focusPos.setEditable( false ) ;
		}

		// Update readMode selection box
		ddlbw = ( DropDownListBoxWidgetExt )_w.ReadMode ;
		ddlbw.setChoices( ico.getReadModeChoices() ) ;
		ddlbw.setValue( ico.getReadMode() ) ;

		// Update filters selection box
		ddlbw = ( DropDownListBoxWidgetExt )_w.Filter ;
		ddlbw.setChoices( ico.getFilterChoices() ) ;
		ddlbw.setValue( ico.getFilter() ) ;

		// Observe repetitions
		_w.repeatComboBox.setValue( ico.getCount() - 1 ) ;

		TextBoxWidgetExt tbw ;

		// Exposure time
		if( _w.ExpTime != source )
		{
			tbw = ( TextBoxWidgetExt )_w.ExpTime ;
			String expTimeStr = String.valueOf( ico.getExposureTime() ) ;
			tbw.setValue( expTimeStr ) ;
			if( _w.focusPos.isVisible() && _w.focusPos != source )
				_w.focusPos.setValue( "" + ico.getFocus() ) ;
		}

		// Coadds
		else if( _w.Coadds != source )
		{
			tbw = ( TextBoxWidgetExt )_w.Coadds ;
			tbw.setValue( ico.getCoaddsString() ) ;
			if( _w.focusPos.isVisible() && _w.focusPos != source )
				_w.focusPos.setValue( "" + ico.getFocus() ) ;
		}

		// Focus
		if( _w.focusPos != source )
		{
			tbw = ( TextBoxWidgetExt )_w.ExpTime ;
			String expTimeStr = String.valueOf( ico.getExposureTime() ) ;
			tbw.setValue( expTimeStr ) ;
			tbw = ( TextBoxWidgetExt )_w.Coadds ;
			tbw.setValue( ico.getCoaddsString() ) ;
		}

		_ignoreActionEvents = false ;
	}

	/**
	 * Watch changes to text box widgets.
	 */
	public void textBoxKeyPress( TextBoxWidgetExt tbw )
	{
		SpIterWFCAMCalObs ico = ( SpIterWFCAMCalObs )_spItem ;
		if( tbw == _w.ExpTime )
		{
			try
			{
				String ets = tbw.getText() ;
				double et = Double.parseDouble( ets ) ;
				if( et > 0.00001 )
				{
					ico.setExposureTime( ets ) ;
					_updateWidgets( _w.ExpTime ) ;
				}
			}
			catch( Exception ex )
			{
				// ignore
			}
		}
		else if( tbw == _w.Coadds )
		{
			try
			{
				String coaddsString = tbw.getText() ;
				int coadds = Integer.parseInt( coaddsString ) ;
				if( coadds > 0 )
				{
					ico.setCoadds( coadds ) ;
					_updateWidgets( _w.Coadds ) ;
				}
			}
			catch( Exception ex )
			{
				// ignore
			}
		}
		else if( tbw == _w.focusPos )
		{
			try
			{
				double focusValue = Double.parseDouble( tbw.getText() ) ;
				ico.setFocus( focusValue ) ;
				_updateWidgets( _w.focusPos ) ;
			}
			catch( Exception e )
			{
				// ignored
			}
		}
		// End of added by RDK
	}

	/**
	 * Text box action.
	 */
	public void textBoxAction( TextBoxWidgetExt tbwe ){}

	/**
	 * DD list box select.
	 */
	public void dropDownListBoxSelect( DropDownListBoxWidgetExt ddlbwe , int i , String val ){}

	/**
	 * DD list box action.
	 */
	public void dropDownListBoxAction( DropDownListBoxWidgetExt ddlbwe , int i , String val ){}

	/**
	 *
	 */
	public void actionPerformed( ActionEvent evt )
	{
		if( !_ignoreActionEvents )
		{
			Object w = evt.getSource() ;
	
			SpIterWFCAMCalObs ico = ( SpIterWFCAMCalObs )_spItem ;
	
			if( w == _w.CalType )
			{
				DropDownListBoxWidgetExt ddlbw = ( DropDownListBoxWidgetExt )w ;
				ico.setCalType( ddlbw.getStringValue() ) ;
				ico.useDefaults() ;
				_updateWidgets() ;
			}
			else if( w == _w.ReadMode )
			{
				DropDownListBoxWidgetExt ddlbw = ( DropDownListBoxWidgetExt )w ;
				ico.setReadMode( ddlbw.getStringValue() ) ;
				_updateWidgets() ;
			}
			else if( w == _w.Filter )
			{
				DropDownListBoxWidgetExt ddlbw = ( DropDownListBoxWidgetExt )w ;
				ico.setFilter( ddlbw.getStringValue() ) ;
				_updateWidgets() ;
			}
			else if( w == _w.repeatComboBox )
			{
				int i = _w.repeatComboBox.getIntegerValue() + 1 ;
				ico.setCount( i ) ;
			}
			else if( w == _w.useDefaults )
			{
				ico.useDefaults() ;
				_updateWidgets() ;
			}
		}
	}
}
