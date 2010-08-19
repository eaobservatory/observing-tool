package ot.jcmt.iter.editor ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import java.awt.event.KeyEvent ;
import java.awt.event.KeyListener ;

import javax.swing.JTextField ;
import javax.swing.event.ChangeEvent ;
import javax.swing.event.ChangeListener ;

import gemini.sp.SpAvTable ;
import gemini.sp.SpItem ;
import gemini.util.MathUtil ;
import orac.jcmt.iter.SpIterFTS2 ;
import jsky.app.ot.editor.OtItemEditor ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;

public final class EdIterFTS2Obs extends OtItemEditor implements ActionListener , ChangeListener , KeyListener
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

		_w.resolutionFOV.setMinimum( 44 ) ;
		_w.resolutionFOV.setMaximum( 731 ) ;
		_w.resolutionFOV.addChangeListener( this ) ;

		_w.scanSpeedNyquist.setMinimum( 40 ) ;
		_w.scanSpeedNyquist.setMaximum( 800 ) ;
		_w.scanSpeedNyquist.setMajorTickSpacing( 10 ) ;
		_w.scanSpeedNyquist.addChangeListener( this ) ;

		_w.sensitivity450.addKeyListener( this ) ;
		_w.sensitivity850.addKeyListener( this ) ;
		_w.integrationTime.addKeyListener( this ) ;
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
		_w.resolutionMHz.setText( "" + MathUtil.round( _inst.getResolutionInMHz() , 4 ) ) ;
		_w.scanSpeed.setText( "" + speed ) ;
		_w.nyquist.setText( "" + MathUtil.round( _inst.getNyquist() , 4 ) ) ;
		_w.southernPanelEnabled( SpIterFTS2.VARIABLE_MODE.equals( _inst.getSpecialMode() ) ) ;
			_w.sensitivity450.setText( "" + MathUtil.round( _inst.getSensitivity450() , 5 ) ) ;
			_w.sensitivity850.setText( "" + MathUtil.round( _inst.getSensitivity850() , 5 ) ) ;
		if( !keypress )
			_w.integrationTime.setText( "" + _inst.getElapsedTime() ) ;
	}

    public void actionPerformed( ActionEvent e )
    {
	    Object source = e.getSource() ;
	    if( _w.specialModes.equals( source ) )
	    {
	    	Object item = (( DropDownListBoxWidgetExt )source).getSelectedItem() ;
	    	if( item instanceof String )
			{
				_inst.setSpecialMode( ( String )item ) ;
				boolean variableMode = SpIterFTS2.VARIABLE_MODE.equals( item ) ;
				_w.southernPanelEnabled( variableMode ) ;
				SpAvTable table = _inst.getTable() ;
				if( variableMode )
				{
					_inst.setFOV( _inst.getFOV() ) ;
					_inst.setScanSpeed( _inst.getScanSpeed() ) ;
					_inst.setSampleTime( "" + _inst.getSampleTime() ) ;
				}
				else
				{
					table.rm( SpIterFTS2.FOV ) ;
					table.rm( SpIterFTS2.SCAN_SPEED ) ;
					table.rm( SpIterFTS2.ATTR_SAMPLE_TIME ) ;
				}
			}
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

    public void keyPressed( KeyEvent e ){}

    boolean keypress = false ;
    public void keyReleased( KeyEvent e )
    {
    	JTextField textBox = ( JTextField )e.getSource() ;
    	if( textBox == _w.integrationTime )
    	{
    		String value = textBox.getText() ;	
    		_inst.setSampleTime( value ) ;
    		keypress = true ;
    		_updateWidgets() ;
    		keypress = false ;
    	}
    }

    public void keyTyped( KeyEvent e ){}
}
