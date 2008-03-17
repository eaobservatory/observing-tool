/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.iter.editor;

import jsky.app.ot.gui.CheckBoxWidgetExt;

import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.iter.SpIterSkydipObs;

/**
 * This is the editor for Skydip Observe Mode iterator component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterSkydipObs extends EdIterJCMTGeneric
{
	private IterSkydipObsGUI _w; // the GUI layout panel

	private SpIterSkydipObs _iterObs;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterSkydipObs()
	{
		super( new IterSkydipObsGUI() );

		_title = "Skydip";
		_presSource = _w = ( IterSkydipObsGUI )super._w;
		_description = "Skydip Observation Mode";

		_w.currentAzimuth.addWatcher( this );
		_w.jPanel1.setVisible( false ) ;
	}

	/**
	 * Override setup to store away a reference to the Focus Iterator.
	 */
	public void setup( SpItem spItem )
	{
		_iterObs = ( SpIterSkydipObs )spItem;
		super.setup( spItem );
	}

	protected void _updateWidgets()
	{
		_w.currentAzimuth.setValue( _iterObs.getDoAtCurrentAz() );

		super._updateWidgets();
	}

	public void checkBoxAction( CheckBoxWidgetExt cbwe )
	{
		if( cbwe == _w.currentAzimuth )
			_iterObs.setDoAtCurrentAz( _w.currentAzimuth.getBooleanValue() );
	}

	public void setInstrument( SpInstObsComp spInstObsComp )
	{
		super.setInstrument( spInstObsComp );
	}

}
