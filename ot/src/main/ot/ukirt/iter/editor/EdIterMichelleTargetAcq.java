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

package ot.ukirt.iter.editor ;

import orac.ukirt.iter.SpIterMichelleTargetAcq ;

import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;

import gemini.sp.SpItem ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

import ot.util.DialogUtil ;
import jsky.app.ot.editor.OtItemEditor ;

/**
 * This is the editor for Michelle Spectroscopy Target Acquistion iterator component.
 */
public final class EdIterMichelleTargetAcq extends OtItemEditor implements TextBoxWidgetWatcher , ActionListener
{
	private SpIterMichelleTargetAcq _ita ;
	private IterMichelleTargetAcqGUI _w ;

	/**
	 * If true, ignore action events.
	 */
	private boolean ignoreActions = false ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterMichelleTargetAcq()
	{
		_title = "Michelle Spectroscopy Target Acquisition Iterator" ;
		_presSource = _w = new IterMichelleTargetAcqGUI() ;
		_description = "Configure Michelle spectroscopy camera for target acquisition." ;

		_w.defaultAcquisition.addActionListener( this ) ;
	}

	/**
	 */
	protected void _init()
	{
		// Coadds
		_w.coadds.addWatcher( this ) ;

		super._init() ;
	}

	/**
	 * Override setup to store away a reference to the SpInstMichelle item.
	 */
	public void setup( SpItem spItem )
	{
		_ita = ( SpIterMichelleTargetAcq )spItem ;
		super.setup( spItem ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		ignoreActions = true ;

		_ita.useDefaultDisperser() ;

		try
		{
			// Exposure Time
			_w.exposureTime.setValue( _ita.getExposureTimeString() ) ;
		}
		catch( NullPointerException ex )
		{
			DialogUtil.error( _w , "Can't set exposure time: probably because Michelle instrument component is in imaging mode" ) ;
			return ;
		}

		// Coadds
		_w.coadds.setValue( _ita.getCoadds() ) ;

		// Update data acquisition config
		_ita.updateDAConf() ;

		ignoreActions = false ;
	}

	/**
	 * Watch changes to text boxes
	 */
	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		if( tbwe == _w.coadds )
			_ita.setCoadds( tbwe.getText() ) ;
	}

	/**
	 * Text box action.
	 */
	public void textBoxAction( TextBoxWidgetExt tbwe ){}

	/**
	 *
	 */
	public void actionPerformed( ActionEvent evt )
	{
		if( ignoreActions )
			return ;

		Object w = evt.getSource() ;

		if( w == _w.defaultAcquisition )
		{
			_ita.useDefaultAcquisition() ;
			_updateWidgets() ;
		}
	}
}
