// $Id$

package ot.jcmt.iter.editor ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

import gemini.sp.SpItem ;
import orac.jcmt.iter.SpIterFTS2 ;
import jsky.app.ot.editor.OtItemEditor ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;

public final class EdIterFTS2Obs extends OtItemEditor implements ActionListener
{
	private SpIterFTS2 _inst ;

	private IterFTS2ObsGUI _w ;

	public EdIterFTS2Obs()
	{
		_title = "JCMT FTS-2" ;
		_presSource = _w = new IterFTS2ObsGUI() ;
		_description = "FTS-2" ;
		_w.specialModes.setChoices( SpIterFTS2.SPECIAL_MODES ) ;
		
		_w.specialModes.addActionListener( this ) ;
		_w.dual.addActionListener( this ) ;
		_w.single.addActionListener( this ) ;
		_w.port1.addActionListener( this ) ;
		_w.port2.addActionListener( this ) ;
	}

	/**
	 * Override setup to store away a reference to the SpInstSCUBA2 item.
	 */
	public void setup( SpItem spItem )
	{
		_inst = ( SpIterFTS2 )spItem ;
		super.setup( spItem ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		_w.specialModes.setSelectedItem( _inst.getSpecialMode() ) ;
		boolean isPort1 = _inst.getTrackingPort() == 1 ;
		_w.port1.setSelected( isPort1 ) ;
		_w.port2.setSelected( !isPort1 ) ;
		boolean isDualPort = _inst.isDualPort() ;
		_w.dual.setSelected( isDualPort ) ;
		_w.single.setSelected( !isDualPort ) ;
	}

    public void actionPerformed( ActionEvent e )
    {
	    Object source = e.getSource() ;
	    if( _w.specialModes.equals( source ) )
	    {
	    	Object item = (( DropDownListBoxWidgetExt )source).getSelectedItem() ;
	    	if( item instanceof String )
	    		_inst.setSpecialMode( ( String )item ) ;
	    }
	    else if( _w.dual.equals( source ) )
	    {
	    	_inst.setIsDualPort( true ) ;
	    }
	    else if( _w.single.equals( source ) )
	    {
	    	_inst.setIsDualPort( false ) ;
	    }
	    else if( _w.port1.equals( source ) )
	    {
	    	_inst.setTrackingPort( 1 ) ;
	    }
	    else if( _w.port2.equals( source ) )
	    {
	    	_inst.setTrackingPort( 2 ) ;
	    }    
    }
}
