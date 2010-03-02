// $Id$

package ot.jcmt.iter.editor ;

import java.util.Observer ;
import java.util.Observable ;
import java.awt.event.KeyListener ;

import jsky.app.ot.gui.TextBoxWidgetExt ;
import gemini.sp.SpItem ;
import gemini.sp.obsComp.SpInstObsComp ;
import orac.jcmt.iter.SpIterDREAMObs ;

public final class EdIterDREAMObs extends EdIterJCMTGeneric implements Observer , KeyListener
{
	private IterDREAMObsGUI _w ;

	public EdIterDREAMObs()
	{
		super( new IterDREAMObsGUI() ) ;
		_title = "DREAM" ;
		_presSource = _w = ( IterDREAMObsGUI )super._w ;
		_description = "DREAM Observation Mode" ;
	}

	public void setup( SpItem spItem )
	{
		_iterObs = ( SpIterDREAMObs )spItem ;
		super.setup( spItem ) ;
		_w.secsPerObservation.addWatcher( this ) ;
		_iterObs.setSampleTime( "" + _iterObs.getSampleTime() ) ;
	}

	public void setInstrument( SpInstObsComp spInstObsComp )
	{
		super.setInstrument( spInstObsComp ) ;
	}

	protected void _updateWidgets()
	{
		super._updateWidgets() ;
		_w.secsPerObservation.setText( "" + _iterObs.getSampleTime() ) ;
	}

	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		_iterObs.getAvEditFSM().deleteObserver( this ) ;

		if( tbwe == _w.secsPerObservation )
		{
			String input = _w.secsPerObservation.getText() ;
			if( input.matches( "[0-9]*[.]?[0-9]*" ) )
			{
				double value = new Double( input ) ;
				if( value != 0. )
					_iterObs.setSampleTime( input ) ;
			}
		}

		super.textBoxKeyPress( tbwe ) ;

		_iterObs.getAvEditFSM().addObserver( this ) ;

	}

	public void keyPressed( java.awt.event.KeyEvent e ){}

	public void keyTyped( java.awt.event.KeyEvent e ){}

	public void keyReleased( java.awt.event.KeyEvent e ){}

	public void update( Observable observable , Object object ){}
}
