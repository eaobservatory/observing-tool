/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.iter.editor ;

import jsky.app.ot.gui.CheckBoxWidgetExt ;

import gemini.sp.SpItem ;
import gemini.sp.obsComp.SpInstObsComp ;
import orac.jcmt.iter.SpIterSetupObs ;

/**
 * Cloned from Skydip
 */
public final class EdIterSetupObs extends EdIterJCMTGeneric
{
	private IterSetupObsGUI _w ; // the GUI layout panel

	private SpIterSetupObs _iterObs ;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterSetupObs()
	{
		super( new IterSetupObsGUI() ) ;

		_title = "Setup" ;
		_presSource = _w = ( IterSetupObsGUI )super._w ;
		_description = "Setup Observation Mode" ;

		_w.currentAzimuth.addWatcher( this ) ;
		_w.jPanel1.setVisible( false ) ;
	}

	/**
	 * Override setup to store away a reference to the Focus Iterator.
	 */
	public void setup( SpItem spItem )
	{
		_iterObs = ( SpIterSetupObs )spItem ;
		super.setup( spItem ) ;
	}

	protected void _updateWidgets()
	{
		_w.currentAzimuth.setValue( _iterObs.getDoAtCurrentAz() ) ;

		super._updateWidgets() ;
	}

	public void checkBoxAction( CheckBoxWidgetExt cbwe )
	{
		if( cbwe == _w.currentAzimuth )
			_iterObs.setDoAtCurrentAz( _w.currentAzimuth.getBooleanValue() ) ;
	}

	public void setInstrument( SpInstObsComp spInstObsComp )
	{
		super.setInstrument( spInstObsComp ) ;
	}

}
