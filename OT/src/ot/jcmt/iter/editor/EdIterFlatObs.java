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

import orac.jcmt.iter.SpIterFlatObs;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;

/**
 * This is the editor for Focus Observe Mode iterator component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterFlatObs extends EdIterJCMTGeneric
{

	private IterFlatObsGUI _w; // the GUI layout panel

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterFlatObs()
	{
		super( new IterFlatObsGUI() );

		_title = "Flat";
		_presSource = _w = ( IterFlatObsGUI )super._w;
		_description = "Flat Observation Mode";

		_w.jPanel1.setVisible( false );

	}

	/**
	 * Override setup to store away a reference to the Focus Iterator.
	 */
	public void setup( SpItem spItem )
	{
		_iterObs = ( SpIterFlatObs )spItem;
		super.setup( spItem );
	}

	protected void _updateWidgets()
	{
		super._updateWidgets();
	}

	public void setInstrument( SpInstObsComp spInstObsComp )
	{
		_w.jPanel1.setVisible( false );
		super.setInstrument( spInstObsComp );
	}
}
