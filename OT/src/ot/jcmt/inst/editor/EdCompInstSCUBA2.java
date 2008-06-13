// $Id$

package ot.jcmt.inst.editor ;

import gemini.sp.SpItem ;
import orac.jcmt.inst.SpInstSCUBA2 ;
import jsky.app.ot.editor.OtItemEditor ;

public final class EdCompInstSCUBA2 extends OtItemEditor
{
	private SpInstSCUBA2 _inst ;

	private Scuba2GUI _w ;

	public EdCompInstSCUBA2()
	{
		_title = "JCMT SCUBA-2" ;
		_presSource = _w = new Scuba2GUI() ;
		_description = "The SCUBA-2 instrument is configured with this component." ;
	}

	/**
	 * Override setup to store away a reference to the SpInstSCUBA2 item.
	 */
	public void setup( SpItem spItem )
	{
		_inst = ( SpInstSCUBA2 )spItem ;
		super.setup( spItem ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets(){}
}
