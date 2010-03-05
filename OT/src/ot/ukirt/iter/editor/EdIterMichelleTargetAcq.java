// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
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
