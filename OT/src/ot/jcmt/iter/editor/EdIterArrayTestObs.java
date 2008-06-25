package ot.jcmt.iter.editor ;

import orac.jcmt.iter.SpIterArrayTestObs ;
import gemini.sp.SpItem ;
import gemini.sp.obsComp.SpInstObsComp ;

public final class EdIterArrayTestObs extends EdIterJCMTGeneric
{
	private IterArrayTestObsGUI _w ; // the GUI layout panel

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterArrayTestObs()
	{
		super( new IterArrayTestObsGUI() ) ;

		_title = "Array Test" ;
		_presSource = _w = ( IterArrayTestObsGUI )super._w ;
		_description = "Array Test Observation Mode" ;

		_w.jPanel1.setVisible( false ) ;

	}

	/**
	 * Override setup to store away a reference to the Array Test.
	 */
	public void setup( SpItem spItem )
	{
		_iterObs = ( SpIterArrayTestObs )spItem ;
		super.setup( spItem ) ;
	}

	protected void _updateWidgets()
	{
		super._updateWidgets() ;
	}

	public void setInstrument( SpInstObsComp spInstObsComp )
	{
		_w.jPanel1.setVisible( false ) ;
		super.setInstrument( spInstObsComp ) ;
	}
}
