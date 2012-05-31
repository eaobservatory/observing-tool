// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.ukirt.iter.editor ;

import orac.ukirt.iter.SpIterUISTTargetAcq ;

import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher ;

import gemini.sp.SpItem ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

import ot.util.DialogUtil ;
import jsky.app.ot.editor.OtItemEditor ;

/**
 * This is the editor for UIST Spectroscopy/IFU Target Acquistion iterator component.
 */
public final class EdIterUISTTargetAcq extends OtItemEditor implements TextBoxWidgetWatcher , ActionListener
{
	private SpIterUISTTargetAcq _ita ;
	private IterUISTTargetAcqGUI _w ;

	/**
	 * If true, ignore action events.
	 */
	private boolean ignoreActions = false ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterUISTTargetAcq()
	{
		_title = "UIST Spec/IFU Target Acquisition Iterator" ;
		_presSource = _w = new IterUISTTargetAcqGUI() ;
		_description = "Configure UIST spectroscopy/IFU camera for target acquisition." ;

		_w.defaultAcquisition.addActionListener( this ) ;
	}

	/**
	 */
	protected void _init()
	{
		//
		// Source magnitude
		//
		_w.sourceMag.addWatcher( new DropDownListBoxWidgetWatcher()
		{
			public void dropDownListBoxAction( DropDownListBoxWidgetExt dd , int i , String val )
			{
				_ita.setSourceMag( val ) ;
				_ita.useDefaultAcquisition() ;
				_updateWidgets() ;
			}
		} ) ;

		// Exposure time
		_w.exposureTime.addWatcher( this ) ;

		// Coadds
		_w.coadds.addWatcher( this ) ;

		super._init() ;
	}

	/**
	 * Override setup to store away a reference to the SpInstUIST item.
	 */
	public void setup( SpItem spItem )
	{
		_ita = ( SpIterUISTTargetAcq )spItem ;
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
			// Source magnitude
			_updateSourceMagChoices() ;
		}
		catch( NullPointerException ex )
		{
			DialogUtil.error( _w , "Can't get source magnitude: probably because UIST instrument component is missing" ) ;
			return ;
		}

		_updateSourceMag() ;

		try
		{
			// Exposure Time
			_w.exposureTime.setValue( _ita.getExposureTimeString() ) ;
		}
		catch( NullPointerException ex )
		{
			DialogUtil.error( _w , "Can't set exposure time: probably because UIST instrument component is in imaging mode" ) ;
			return ;
		}

		// Coadds
		_w.coadds.setValue( _ita.getCoadds() ) ;
		// Hacky fix for a bug whereby sometimes the coadds does not get written out in the xml
		_ita.setCoadds( _w.coadds.getValue() ) ;

		// Disperser
		_w.disperser.setValue( _ita.getDisperser() ) ;

		ignoreActions = false ;
	}

	//
	// Update the list of source magnitude choices
	//
	private void _updateSourceMagChoices()
	{

		int numChoices = _ita.getSourceMagList().length ;
		String choices[] = new String[ numChoices ] ;
		choices = _ita.getSourceMagList() ;
		_w.sourceMag.setMaximumRowCount( numChoices ) ;
		_w.sourceMag.setChoices( choices ) ;
	}

	//
	// Update the source magnitude
	//
	private void _updateSourceMag()
	{
		String sourceMag = _ita.getSourceMag() ;
		_w.sourceMag.setValue( sourceMag ) ;
	}

	/**
	 * Watch changes to text boxes
	 */
	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		if( tbwe == _w.exposureTime )
			_ita.setExposureTime( tbwe.getText() ) ;
		else if( tbwe == _w.coadds )
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
		if( !ignoreActions )
		{
			Object w = evt.getSource() ;
	
			if( w == _w.defaultAcquisition )
			{
				_ita.useDefaultSourceMag() ;
				_ita.useDefaultAcquisition() ;
				_updateWidgets() ;
			}
		}
	}
}
