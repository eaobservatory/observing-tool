// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.ukirt.iter.editor ;

import orac.ukirt.iter.SpIterBiasObs ;

import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;

import java.awt.Color ;
import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

import jsky.app.ot.editor.OtItemEditor ;

/**
 * This is the editor for Bias Observe iterator component.
 */
public final class EdIterBiasObs extends OtItemEditor implements TextBoxWidgetWatcher , ActionListener
{
	private IterBiasObsGUI _w ;

	/**
	 * If true, ignore action events.
	 */
	private boolean ignoreActions = false ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterBiasObs()
	{
		_title = "Bias Iterator" ;
		_presSource = _w = new IterBiasObsGUI() ;
		_description = "Take the specified number of bias exposures." ;

		for( int i = 0 ; i < 100 ; i++ )
			_w.repeatComboBox.addItem( "" + ( i + 1 ) ) ;

		_w.repeatComboBox.addActionListener( this ) ;
	}

	/**
	 */
	protected void _init()
	{
		// Coadds
		_w.coadds.setEnabled( false ) ;
		_w.coadds.setDisabledTextColor( Color.black ) ;

		super._init() ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		ignoreActions = true ;

		SpIterBiasObs iterObs = ( SpIterBiasObs )_spItem ;

		// Repetitions
		_w.repeatComboBox.setSelectedIndex( iterObs.getCount() - 1 ) ;

		// Exposure Time
		_w.exposureTime.setValue( iterObs.getExposureTime() ) ;

		// Coadds
		_w.coadds.setValue( iterObs.getCoadds() ) ;

		ignoreActions = false ;
	}

	/**
	 * Watch changes to text boxes
	 */
	public void textBoxKeyPress( TextBoxWidgetExt tbwe ){}

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
	
			SpIterBiasObs iterObs = ( SpIterBiasObs )_spItem ;
	
			if( w == _w.repeatComboBox )
			{
				int i = _w.repeatComboBox.getSelectedIndex() + 1 ;
				iterObs.setCount( i ) ;
			}
		}
	}
}
