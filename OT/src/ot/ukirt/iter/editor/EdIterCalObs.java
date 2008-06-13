// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.ukirt.iter.editor ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;

import javax.swing.DefaultComboBoxModel ;
import javax.swing.JComboBox ;

import jsky.app.ot.editor.OtItemEditor ;

// The following two classes will have to be swapped for the respective ukirts ones (currently commented out).
// But at the moment the ukirt configuration does not use EdIterCalObs but only ot_ukirt.iter.editor.EdIterCGS4CalObs.
import jsky.app.ot.gemini.iter.SpCalUnitConstants ;
import jsky.app.ot.gemini.iter.SpIterCalObs ;

import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;

/**
 * This is the editor for the CalUnit Observation iterator.
 */
public final class EdIterCalObs extends OtItemEditor implements TextBoxWidgetWatcher , ActionListener
{
	private IterCalObsGUI _w ; // the GUI layout panel

	// If true, ignore action events
	private boolean ignoreActions = false ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterCalObs()
	{
		_title = "Cal Unit Observation" ;
		_presSource = _w = new IterCalObsGUI() ;
		_description = "Configure the Calibration Unit with this component." ;

		// Note: The original bongo code used a SpinBoxWidget, but since Swing doesn't have one, try using a JComboBox instead...
		Object[] ar = new Object[ 99 ] ;
		for( int i = 0 ; i < 99 ; i++ )
			ar[ i ] = new Integer( i + 1 ) ;
		_w.repeatComboBox.setModel( new DefaultComboBoxModel( ar ) ) ;
		_w.repeatComboBox.addActionListener( this ) ;

		_w.lamp.addActionListener( this ) ;
		_w.filter.addActionListener( this ) ;
		_w.diffuser.addActionListener( this ) ;
	}

	/**
	 */
	protected void _init()
	{
		ignoreActions = true ;

		try
		{
			DropDownListBoxWidgetExt ddlbw ;

			// Set the lamp choices
			ddlbw = _w.lamp ;
			ddlbw.setChoices( SpCalUnitConstants.LAMPS ) ;

			// Show the filter
			ddlbw = _w.filter ;
			ddlbw.setChoices( SpCalUnitConstants.FILTERS ) ;

			// Show the diffuser
			ddlbw = _w.diffuser ;
			ddlbw.setChoices( SpCalUnitConstants.DIFFUSERS ) ;

			TextBoxWidgetExt tbwe ;

			// Exposure time
			tbwe = _w.exposureTime ;
			tbwe.addWatcher( this ) ;

			// Coadds
			tbwe = _w.coadds ;
			tbwe.addWatcher( this ) ;
		}
		catch( Exception e )
		{
			throw new RuntimeException( e.toString() ) ;
		}

		ignoreActions = false ;

		super._init() ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		ignoreActions = true ;

		try
		{
			DropDownListBoxWidgetExt ddlbw ;

			SpIterCalObs ico = ( SpIterCalObs )_spItem ;

			// Show the lamp
			ddlbw = _w.lamp ;
			ddlbw.setValue( ico.getLamp() ) ;

			// Show the filter
			ddlbw = _w.filter ;
			ddlbw.setValue( ico.getFilter() ) ;

			// Show the diffuser 
			ddlbw = _w.diffuser ;
			ddlbw.setValue( ico.getDiffuser() ) ;

			// Observe Repetitions
			JComboBox sbw = _w.repeatComboBox ;
			sbw.getModel().setSelectedItem( new Integer( ico.getCount() ) ) ;

			TextBoxWidgetExt tbwe ;

			// Exposure time
			tbwe = _w.exposureTime ;
			tbwe.setValue( ico.getExposureTime() ) ;

			// Coadds
			tbwe = _w.coadds ;
			tbwe.setValue( ico.getCoadds() ) ;
		}
		catch( Exception e )
		{
			throw new RuntimeException( e.toString() ) ;
		}

		ignoreActions = false ;
	}

	/**
	 * Watch changes to text box widgets.
	 */
	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		SpIterCalObs ico = ( SpIterCalObs )_spItem ;

		if( tbwe == _w.exposureTime )
			ico.setExposureTime( tbwe.getText() ) ;
		else if( tbwe == _w.coadds )
			ico.setCoadds( tbwe.getText() ) ;
	}

	/**
	 * Text box action.
	 */
	public void textBoxAction( TextBoxWidgetExt tbwe ){}

	/**
	 * Called to handle widget actions.
	 */
	public void actionPerformed( ActionEvent evt )
	{
		if( !ignoreActions )
		{
			Object w = evt.getSource() ;
			SpIterCalObs ico = ( SpIterCalObs )_spItem ;
	
			if( w == _w.lamp )
			{
				DropDownListBoxWidgetExt ddlbw = ( DropDownListBoxWidgetExt )w ;
				ico.setLamp( ddlbw.getStringValue() ) ;
			}
			else if( w == _w.filter )
			{
				DropDownListBoxWidgetExt ddlbw = ( DropDownListBoxWidgetExt )w ;
				ico.setFilter( ddlbw.getStringValue() ) ;
			}
			else if( w == _w.diffuser )
			{
				DropDownListBoxWidgetExt ddlbw = ( DropDownListBoxWidgetExt )w ;
				ico.setDiffuser( ddlbw.getStringValue() ) ;
			}
			else if( w == _w.repeatComboBox )
			{
				JComboBox sbw = _w.repeatComboBox ;
				int i = ( ( Integer )( sbw.getSelectedItem() ) ).intValue() ;
				ico.setCount( i ) ;
			}
		}
	}
}
