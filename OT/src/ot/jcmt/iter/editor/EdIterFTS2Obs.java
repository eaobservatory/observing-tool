package ot.jcmt.iter.editor ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;

import javax.swing.event.ChangeEvent ;
import javax.swing.event.ChangeListener ;

import gemini.sp.SpItem ;
import gemini.util.MathUtil ;
import orac.jcmt.iter.SpIterFTS2 ;
import jsky.app.ot.editor.OtItemEditor ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;

public final class EdIterFTS2Obs extends OtItemEditor implements ActionListener , ChangeListener
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

		_w.resolutionFOV.setMinimum( 56 ) ;
		_w.resolutionFOV.setMaximum( 900 ) ;
		_w.resolutionFOV.addChangeListener( this ) ;

		_w.scanSpeedNyquist.setMinimum( 40 ) ;
		_w.scanSpeedNyquist.setMaximum( 800 ) ;
		_w.scanSpeedNyquist.setMajorTickSpacing( 10 ) ;
		_w.scanSpeedNyquist.addChangeListener( this ) ;
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

		double fov = _inst.getFOV() ;
		double speed = _inst.getScanSpeed() ;
		_w.resolutionFOV.setValue( ( int )( fov * 100 ) ) ;
		_w.scanSpeedNyquist.setValue( ( int )( speed * 100 ) ) ;
		_w.FOV.setText( "" + fov ) ;
		_w.resolution.setText( "" + MathUtil.round( _inst.getResolution() , 4 ) ) ;
		_w.scanSpeed.setText( "" + speed ) ;
		_w.nyquist.setText( "" + MathUtil.round( _inst.getNyquist() , 4 ) ) ;
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

    public void stateChanged( ChangeEvent e )
    {
	Object source = e.getSource() ;
	if( source == _w.resolutionFOV )
	{
		int fov = _w.resolutionFOV.getValue() ;
		double fieldOfView = fov / 100. ;
		_inst.setFOV( fieldOfView ) ;
		_updateWidgets() ;
	}
	else if( source == _w.scanSpeedNyquist )
	{
		int scanSpeed = _w.scanSpeedNyquist.getValue() ;
		double speed = scanSpeed / 100. ;
		_inst.setScanSpeed( speed ) ;
		_updateWidgets() ;
	}
    }
}
