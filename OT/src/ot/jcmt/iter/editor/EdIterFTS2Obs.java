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
import orac.jcmt.iter.SpIterFTS2Obs ;
import jsky.app.ot.editor.OtItemEditor ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;

public final class EdIterFTS2Obs extends OtItemEditor implements ActionListener , ChangeListener , KeyListener
{
	private SpIterFTS2Obs _inst ;

	private IterFTS2ObsGUI _w ;

	public EdIterFTS2Obs()
	{
		_title = "JCMT FTS-2" ;
		_presSource = _w = new IterFTS2ObsGUI() ;
		_description = "FTS-2" ;
		_w.specialModes.setChoices( SpIterFTS2Obs.SPECIAL_MODES ) ;
		
		_w.specialModes.addActionListener( this ) ;
		_w.dual.addActionListener( this ) ;
		_w.single.addActionListener( this ) ;
		_w.port8d.addActionListener( this ) ;
		_w.port8c.addActionListener( this ) ;

		_w.resolutionFOV.setMinimum( SpIterFTS2Obs.minimumResolutionScaled ) ;
		_w.resolutionFOV.setMaximum( SpIterFTS2Obs.maximumResolutionScaled ) ;
		_w.resolutionFOV.addChangeListener( this ) ;

		_w.scanSpeedNyquist.setMinimum( SpIterFTS2Obs.minimumSpeedScaled ) ;
		_w.scanSpeedNyquist.setMaximum( SpIterFTS2Obs.maximumSpeedScaled ) ;
		_w.scanSpeedNyquist.setMajorTickSpacing( SpIterFTS2Obs.speedIncrementScaled ) ;
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
		_inst = ( SpIterFTS2Obs )spItem ;
		super.setup( spItem ) ;
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		_w.specialModes.setSelectedItem( _inst.getSpecialMode() ) ;
		boolean isPort8D = SpIterFTS2Obs.PORT_8D.equals(_inst.getTrackingPort());
		_w.port8d.setSelected( isPort8D ) ;
		_w.port8c.setSelected( !isPort8D ) ;
		boolean isDualPort = _inst.isDualPort() ;
		_w.dual.setSelected( isDualPort ) ;
		_w.single.setSelected( !isDualPort ) ;

		double resolution = _inst.getResolution() ;
		double speed = _inst.getScanSpeed() ;
		_w.resolutionFOV.setValue( ( int )( resolution * SpIterFTS2Obs.resolutionScale ) ) ;
		_w.scanSpeedNyquist.setValue( ( int )( speed * SpIterFTS2Obs.speedScale ) ) ;
		_w.FOV.setText( "" + MathUtil.round( _inst.getFOV() , 4 ) ) ;
		_w.resolution.setText( "" + MathUtil.round( resolution , 4 ) ) ;
		_w.resolutionMHz.setText( "" + MathUtil.round( _inst.getResolutionInMHz() , 4 ) ) ;
		_w.scanSpeed.setText( "" + speed ) ;
		_w.nyquist.setText( "" + MathUtil.round( _inst.getNyquist() , 4 ) ) ;
		_w.southernPanelEnabled( SpIterFTS2Obs.VARIABLE_MODE.equals( _inst.getSpecialMode() ) ) ;
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
				boolean variableMode = SpIterFTS2Obs.VARIABLE_MODE.equals( item ) ;
				_w.southernPanelEnabled( variableMode ) ;
				SpAvTable table = _inst.getTable() ;
				if( variableMode )
				{
					_inst.setResolution( _inst.getResolution() ) ;
					_inst.setScanSpeed( _inst.getScanSpeed() ) ;
				}
				else
				{
					table.rm( SpIterFTS2Obs.FOV ) ;
					table.rm( SpIterFTS2Obs.SCAN_SPEED ) ;
				}
			}
	    }
	    else if( _w.dual.equals( source ) )
	    {
	    	_inst.setIsDualPort( true ) ;
		resetTPE();
	    }
	    else if( _w.single.equals( source ) )
	    {
	    	_inst.setIsDualPort( false ) ;
		resetTPE();
	    }
	    else if( _w.port8d.equals( source ) )
	    {
	    	_inst.setTrackingPort( SpIterFTS2Obs.PORT_8D ) ;
		resetTPE();
	    }
	    else if( _w.port8c.equals( source ) )
	    {
	    	_inst.setTrackingPort( SpIterFTS2Obs.PORT_8C ) ;
		resetTPE();
	    }
	}

	public void stateChanged( ChangeEvent e )
	{
		Object source = e.getSource() ;
		if( source == _w.resolutionFOV )
		{
			int fovResolution = _w.resolutionFOV.getValue() ;
			double resolution = fovResolution / SpIterFTS2Obs.resolutionScale ;
			_inst.setResolution( resolution ) ;
			_updateWidgets() ;
		}
		else if( source == _w.scanSpeedNyquist )
		{
			int scanSpeed = _w.scanSpeedNyquist.getValue() ;
			double speed = scanSpeed / SpIterFTS2Obs.speedScale ;
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
