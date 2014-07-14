// Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

		// Exposure time
		_w.ExpTime.addWatcher(this);

		// Coadds
		_w.Coadds.addWatcher(this);

		// Focus
		_w.focusPos.addWatcher(this);

		// FocusTel
		_w.focusTelSteps.addWatcher(this);

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

		SpIterWFCAMCalObs ico = ( SpIterWFCAMCalObs )_spItem ;

		// Get the choices and defaults from the instrument.

		// Update calType selection box
		_w.CalType.setChoices(ico.getCalTypeChoices());
		int calType = ico.getCalType() ;
		_w.CalType.setValue(calType) ;
		boolean fitOrTel = calType == SpIterWFCAMCalObs.FOCUS_TEL || calType == SpIterWFCAMCalObs.FOCUS_FIT  ;
		boolean focusType = calType == SpIterWFCAMCalObs.FOCUS || fitOrTel ;
		_w.focusPos.setVisible( focusType ) ;
		_w.focusLabel.setVisible( focusType ) ;
		_w.focusPos.setEditable( focusType ) ;
		_w.focusTelStepsLabel.setVisible( fitOrTel ) ;
		_w.focusTelSteps.setVisible( fitOrTel ) ;
		if( fitOrTel && _w.focusTelSteps != source )
			_w.focusTelSteps.setValue( ico.getFocusTelSteps() ) ;

		// Update readMode selection box
		_w.ReadMode.setChoices(ico.getReadModeChoices());
		_w.ReadMode.setValue(ico.getReadMode());

		// Update filters selection box
		_w.Filter.setChoices( ico.getFilterChoices() ) ;
		_w.Filter.setValue( ico.getFilter() ) ;

		// Observe repetitions
		_w.repeatComboBox.setValue( ico.getCount() - 1 ) ;

		// Exposure time
		if( _w.ExpTime != source )
		{
			String expTimeStr = String.valueOf( ico.getExposureTime() ) ;
			_w.ExpTime.setValue(expTimeStr);
			if( _w.focusPos.isVisible() && _w.focusPos != source )
				_w.focusPos.setValue( "" + ico.getFocus() ) ;
		}

		// Coadds
		else if( _w.Coadds != source )
		{
			_w.Coadds.setValue( ico.getCoaddsString() ) ;
			if( _w.focusPos.isVisible() && _w.focusPos != source )
				_w.focusPos.setValue( "" + ico.getFocus() ) ;
		}

		// Focus
		if( _w.focusPos != source )
		{
			_w.ExpTime.setValue(String.valueOf(ico.getExposureTime()));
			_w.Coadds.setValue(ico.getCoaddsString());
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
			catch( Exception e ){}
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
			catch( Exception e ){}
		}
		else if( tbw == _w.focusPos )
		{
			try
			{
				double focusValue = Double.parseDouble( tbw.getText() ) ;
				ico.setFocus( focusValue ) ;
				_updateWidgets( _w.focusPos ) ;
			}
			catch( Exception e ){}
		}
		else if( tbw == _w.focusTelSteps )
		{
			try
			{
				double value = Double.parseDouble( tbw.getText() ) ;
				ico.setFocusTelSteps( value ) ;
				_updateWidgets( _w.focusTelSteps ) ;
			}
			catch( Exception e ){}
		}
		else
		{
			System.out.println( "nothing" ) ;
		}
	}

	/**
	 * Text box action.
	 */
	public void textBoxAction( TextBoxWidgetExt tbwe ){}

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
