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

import java.awt.event.KeyListener ;
import java.awt.Color ;
import java.awt.Component ;
import java.awt.event.KeyEvent ;
import java.text.DecimalFormat ;
import java.util.Observer ;
import java.util.Observable ;

import javax.swing.JTextField ;

import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;
import jsky.app.ot.gui.OptionWidgetExt ;
import jsky.app.ot.gui.OptionWidgetWatcher ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetWatcher ;
import jsky.app.ot.tpe.TpeManager ;
import jsky.app.ot.tpe.TelescopePosEditor ;

import gemini.sp.SpItem ;
import gemini.sp.SpTreeMan ;
import gemini.sp.obsComp.SpInstObsComp ;
import orac.jcmt.SpJCMTConstants ;
import orac.jcmt.inst.SpInstHeterodyne ;
import orac.jcmt.iter.SpIterRasterObs ;
import orac.jcmt.util.Scuba2Noise ;
import orac.jcmt.util.HeterodyneNoise ;
import orac.jcmt.inst.SpInstSCUBA2 ;

import orac.util.CoordConvert ;

import java.math.BigDecimal ;

/**
 * This is the editor for the Raster Observe Mode iterator component (ACSIS).
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterRasterObs extends EdIterJCMTGeneric implements Observer , OptionWidgetWatcher , KeyListener , CommandButtonWidgetWatcher , SpJCMTConstants
{
	private IterRasterObsGUI _w ; // the GUI layout panel
	private SpIterRasterObs _iterObs ;
	private static final String AUTOMATIC = "automatic" ;
	private static final String USER_DEF = "user def" ;
	private static final String ALONG_HEIGHT = "Along Height" ;
	private static final String ALONG_WIDTH = "Along Width" ;
	private final String[] SCAN_PA_CHOICES = { AUTOMATIC , ALONG_HEIGHT , ALONG_WIDTH , USER_DEF } ;

	// The following defines the maximum file size we are currently allowing for raster.
	// Since this is for use with the thermometer, which only accepts integers, we will specify the maximum size in MBytes
	private int _maxFileSize = 2048 ;

	// Some default values for the non-editable text fields
	private final int DEFAULT_SECS_ROW = 240 ;
	private final int DEFAULT_SECS_MAP = 3600 ;
	
	private final static Color safeColor = Color.black ;
	private final static Color warnColor = Color.yellow.darker() ;
	private final static Color errColor = Color.red.darker() ;

	// Global flag indicating whether we are using acsis
	private boolean harp = false ;
	private boolean scuba2 = false ;
	
	private final static String[] HARP_RASTER_NAMES = { "1 array" , "1/2 array" , "1/4 array" , "1/8 array" , "1 sample" , "3/4 array" } ;
	private static final double[] HARP_RASTER_STEPS = { 1. , .5 , .25 , .125 , 0.0625 , 0.75 } ;
	private static double[] HARP_RASTER_VALUES = new double[ HARP_RASTER_STEPS.length ] ;

	static
	{
		for( int index = 0 ; index < HARP_RASTER_STEPS.length ; index++ )
		{
			double tempValue = SpIterRasterObs.HARP_FULL_ARRAY * HARP_RASTER_STEPS[ index ] ;
			HARP_RASTER_VALUES[ index ] = CoordConvert.round( tempValue , 4 ) ;
		}
	}

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterRasterObs()
	{
		super( new IterRasterObsGUI() ) ;

		_title = "Scan" ;
		_presSource = _w = ( IterRasterObsGUI )super._w ;
		_description = "Scan Map" ;

		setDefaults() ;
		addWatchers() ;

		_w.frequencyPanel.setVisible( false ) ;
	}

	private void setDefaults()
	{
		_w.scanAngle.setChoices( SCAN_PA_CHOICES ) ;
		_w.scanSystem.setChoices( SCAN_SYSTEMS ) ;
		_w.thermometer.setMaximum( _maxFileSize ) ;
		
		for( int index = 0 ; index < HARP_RASTER_NAMES.length ; index++ )
			_w.harpRasters.addChoice( "step " + HARP_RASTER_NAMES[ index ] + " (" + CoordConvert.round( HARP_RASTER_VALUES[ index ] , 1 ) + "\")" ) ;
	}
	
	private void addWatchers()
	{
		_w.acsisSampleTime.addWatcher( this ) ;
		
		_w.dx.addWatcher( this ) ;
		_w.dy.addWatcher( this ) ;
		_w.width.addWatcher( this ) ;
		_w.height.addWatcher( this ) ;
		_w.posAngle.addWatcher( this ) ;
		_w.rowReversal.addWatcher( this ) ;
		_w.scanSystem.addWatcher( this ) ;
		_w.scanAngle.addWatcher( this ) ;
		_w.defaultButton.addWatcher( this ) ;
		_w.scanAngle.getEditor().getEditorComponent().addKeyListener( this ) ;

		_w.scanningStrategies.addWatcher( this ) ;
		_w.numberOfMapCycles.addWatcher( this ) ;
		_w.pointSourceTime.addWatcher( this ) ;

		_w.harpRasters.addWatcher( this ) ;
		
		if( _iterObs != null && _iterObs.getAvEditFSM().countObservers() == 0 )
			_iterObs.getAvEditFSM().addObserver( this ) ;
	}
	
	private void deleteWatchers()
	{
		_w.acsisSampleTime.deleteWatcher( this ) ;
		
		_w.dx.deleteWatcher( this ) ;
		_w.dy.deleteWatcher( this ) ;
		_w.width.deleteWatcher( this ) ;
		_w.height.deleteWatcher( this ) ;
		_w.posAngle.deleteWatcher( this ) ;
		_w.rowReversal.deleteWatcher( this ) ;
		_w.scanSystem.deleteWatcher( this ) ;
		_w.scanAngle.deleteWatcher( this ) ;
		_w.defaultButton.deleteWatcher( this ) ;
		_w.scanAngle.getEditor().getEditorComponent().removeKeyListener( this ) ;

		_w.scanningStrategies.deleteWatcher( this ) ;
		_w.numberOfMapCycles.deleteWatcher( this ) ;
		_w.pointSourceTime.deleteWatcher( this ) ;

		_w.harpRasters.deleteWatcher( this ) ;
		
		if( _iterObs != null )
			_iterObs.getAvEditFSM().deleteObserver( this ) ;
	}
	
	/**
	 * Override setup to store away a reference to the Raster Iterator.
	 */
	public void setup( SpItem spItem )
	{
		_iterObs = ( SpIterRasterObs )spItem ;
		SpInstObsComp inst = SpTreeMan.findInstrument( _iterObs ) ;
		setInstrument( inst ) ;
		super.setup( spItem ) ;
		
		if( _iterObs.getAvEditFSM().countObservers() == 0 )
			_iterObs.getAvEditFSM().addObserver( this ) ;
	}

	private void _acsisSetup()
	{
		_w.dx.setEnabled( true ) ;
		_w.dy.setEnabled( true ) ;
		_w.height.setEnabled( true ) ;
		_w.width.setEnabled( true ) ;
		_w.posAngle.setEnabled( true ) ;
		_w.scanAngle.setEnabled( true ) ;
		_w.scanSystem.setEnabled( true ) ;
		
		_w.addNonScuba2Panel() ;
		_w.addNonHarpPanel() ;
		
		_w.subAreaPanel.setVisible( true  ) ;

		_w.acsisSampleTime.setVisible( true ) ;
		_w.acsisSampleTime.setEnabled( true ) ;

		_w.scanningStrategies.setChoices( SCAN_STRATEGIES_ACSIS ) ;
	}

	private void _harpSetup()
	{
		_iterObs.setScanDx( 7.2761 ) ;
		double dy = _iterObs.getScanDy() ;
		boolean found = false ;
		for( int index = 0 ; index < HARP_RASTER_VALUES.length ; index++ )
		{
			if( dy == HARP_RASTER_VALUES[ index ] )
			{
				_w.harpRasters.deleteWatcher( this ) ;
				_w.harpRasters.setSelectedIndex( index ) ;
				_w.harpRasters.addWatcher( this ) ;
				found = true ;
				break ;
			}
		}
		if( !found )
		{
			_iterObs.setScanDy( HARP_RASTER_VALUES[ 0 ] ) ;
			_w.harpRasters.deleteWatcher( this ) ;
			_w.harpRasters.setSelectedIndex( 0 ) ;
			_w.harpRasters.addWatcher( this ) ;
		}
		
		_w.dx.setEnabled( false ) ;
		_w.dy.setEnabled( false ) ;

		_w.scanAngle.setEnabled( true ) ;
		_w.scanSystem.setEnabled( true ) ;
		
		_w.addNonScuba2Panel() ;
		_w.addHarpPanel() ;
		
		_w.subAreaPanel.setVisible( true  ) ;

		_w.acsisSampleTime.setVisible( true ) ;
		_w.acsisSampleTime.setEnabled( true ) ;

		_w.scanningStrategies.setChoices( SCAN_STRATEGIES_HARP ) ;
	}

	private void scuba2Setup()
	{
		_w.addNonHarpPanel() ;
		_w.nonHarpPanel.setVisible( false ) ;

		_w.addScuba2Panel() ;

		_w.subAreaPanel.setVisible( false ) ;

		boolean allowScan = SCAN_PATTERN_BOUS.equals( _iterObs.getScanStrategy() ) ;
		
		if( !allowScan )
			resetScanPanel() ;
		
		_w.dx.setEnabled( false ) ;

		_w.scanAngle.setEnabled( allowScan ) ;
		_w.scanSystem.setEnabled( allowScan ) ;
		
		_w.acsisSampleTime.setVisible( true ) ;
		_w.acsisSampleTime.setEnabled( true ) ;

		_w.scanningStrategies.setChoices( SCAN_STRATEGIES_SCUBA2 ) ;
	}
	
	private void resetScanPanel()
	{
		_iterObs.getTable().noNotifySet( ATTR_SCANAREA_SCAN_SYSTEM , FPLANE , 0 ) ;
		_w.scanSystem.deleteWatcher( this ) ;
		_w.scanSystem.setSelectedItem( FPLANE ) ;
		_w.scanSystem.addWatcher( this ) ;
		_iterObs.getTable().noNotifyRm( ATTR_SCANAREA_SCAN_PA ) ;
		_w.scanAngle.deleteWatcher( this ) ;
		_w.scanAngle.setSelectedItem( SCAN_PA_CHOICES[ 0 ] ) ;
		_w.scanAngle.setEditable( false ) ;
		_w.scanAngle.addWatcher( this ) ;
	}

	private void updateAreaPanel()
	{
		// area panel
		_w.dx.setValue( _iterObs.getScanDx() ) ;
		_w.dx.setCaretPosition( 0 ) ;
		_w.dy.setValue( _iterObs.getScanDy() ) ;
		_w.dy.setCaretPosition( 0 ) ;
		_w.width.setValue( _iterObs.getWidth() ) ;
		_w.width.setCaretPosition( 0 ) ;
		_w.height.setValue( _iterObs.getHeight() ) ;
		_w.height.setCaretPosition( 0 ) ;
		_w.posAngle.setValue( _iterObs.getPosAngle() ) ;
		_w.posAngle.setCaretPosition( 0 ) ;
	}
	
	private void updateScanPanel()
	{
		if( ( _iterObs.getScanAngles() == null ) || ( _iterObs.getScanAngles().size() == 0 ) )
		{
			_w.scanAngle.setEditable( false ) ;
			_w.scanAngle.setValue( AUTOMATIC ) ;
		}
		else if( _iterObs.getScanAngle( 0 ) == ( _iterObs.getPosAngle() + 90. ) % 180. )
		{
			_w.scanAngle.setEditable( false ) ;
			_w.scanAngle.setValue( ALONG_WIDTH ) ;
		}
		else if( _iterObs.getScanAngle( 0 ) == _iterObs.getPosAngle() )
		{
			_w.scanAngle.setEditable( false ) ;
			_w.scanAngle.setValue( ALONG_HEIGHT ) ;
		}
		else
		{
			String scanAngleString = "" ;
			for( int i = 0 ; i < _iterObs.getScanAngles().size() ; i++ )
				scanAngleString += ", " + _iterObs.getScanAngle( i ) ;

			_w.scanAngle.setEditable( true ) ;
			_w.scanAngle.setValue( scanAngleString.substring( 2 ) ) ;
		}
		
		_w.scanSystem.setValue( _iterObs.getScanSystem() ) ;
	}
	
	private void updateScuba2Panel()
	{
		_w.dy.setEnabled( false ) ;
	}

	protected void _updateWidgets()
	{
		deleteWatchers() ;
		// super
		super._updateWidgets() ;
		_w.switchingMode.setValue( _iterObs.getSwitchingMode() ) ;
		_w.rowReversal.setValue( _iterObs.getRowReversal() ) ;

		updateAreaPanel() ;
		updateScanPanel() ;
		if( scuba2 )
			updateScuba2Panel() ;

		String strategy = _iterObs.getScanStrategy() ;
		
		if( strategy == null || "".equals( strategy ) )
		{
			if( scuba2 )
				strategy = SCAN_PATTERN_PONG ;
			else
				strategy = SCAN_PATTERN_BOUS ;
			_iterObs.setScanStrategy( strategy ) ;
		}

		_w.scanningStrategies.setValue( strategy ) ;
		
		boolean pointSource = SCAN_PATTERN_POINT.equals( strategy ) ;
		
		if( pointSource )
			_w.pointSourceTime.setValue( _iterObs.getSampleTime() ) ;
		else
			_w.numberOfMapCycles.setValue( _iterObs.getIntegrations() ) ;
		
		_w.scanSpeed.setValue( _iterObs.getScanVelocity() ) ;
		
		_w.mapCyclesPanel.setVisible( !pointSource ) ;
		_w.pointSourcePanel.setVisible( pointSource ) ;
		
		if( harp )
			_w.dx.setEnabled( !pointSource ) ;
		_w.height.setEnabled( !pointSource ) ;
		_w.width.setEnabled( !pointSource ) ;
		_w.posAngle.setEnabled( !pointSource ) ;
		
		_w.harpRasters.setEnabled( !pointSource ) ;

		_w.acsisSampleTime.setValue( _iterObs.getSampleTime() ) ;

		updateTimes() ;
		updateThermometer() ;

		updateSizeOfPixels() ;
		addWatchers() ;
	}

	private void updateSizeOfPixels()
	{
		boolean displayWarning = false ;
		if( !scuba2 )
		{
			BigDecimal width = new BigDecimal( _iterObs.getWidth() ) ;
			BigDecimal deeEx = new BigDecimal( _iterObs.getScanDx() ) ;
			BigDecimal bigSizeOfXPixel = width.divide( deeEx , SpIterRasterObs.context ) ;
			double sizeOfXPixel = bigSizeOfXPixel.doubleValue() ;
			
			BigDecimal height = new BigDecimal( _iterObs.getHeight() ) ;
			BigDecimal deeWy = new BigDecimal( _iterObs.getScanDy() ) ;
			BigDecimal bigSizeOfYPixel = height.divide( deeWy , SpIterRasterObs.context ) ;
			double sizeOfYPixel = bigSizeOfYPixel.doubleValue() ;
			
			int correctedSizeOfXPixel = ( int )Math.floor( sizeOfXPixel + 1.5 ) ;
			int correctedSizeOfYPixel = ( int )Math.floor( sizeOfYPixel + 1.5 ) ;

			_w.sizeOfXPixel.setText( Integer.toString( correctedSizeOfXPixel ) ) ;
			_w.sizeOfYPixel.setText( Integer.toString( correctedSizeOfYPixel ) ) ;

			if( !harp && sizeOfXPixel - Math.floor( sizeOfXPixel ) != 0. )
			{
				_w.sizeOfXPixel.setBackground( Color.red ) ;
				displayWarning = true ;
			}
			else
			{
				_w.sizeOfXPixel.setBackground( Color.white ) ;
			}

			if( !harp && sizeOfYPixel - Math.floor( sizeOfYPixel ) != 0. )
			{
				_w.sizeOfYPixel.setBackground( Color.red ) ;
				displayWarning = true ;
			}
			else
			{
				_w.sizeOfYPixel.setBackground( Color.white ) ;
			}
		}
		_w.showDimensionWarning( displayWarning ) ;
	}
	
	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		_iterObs.getAvEditFSM().deleteObserver( this ) ;

		if( tbwe == _w.dx )
		{
			Double dx = 3. ;
			String temp = _w.dx.getValue() ;
			
			try
			{
				dx = new Double( temp ) ;
			}
			catch( NumberFormatException nfe ){}
			catch( Exception e ){}

			boolean valid = _iterObs.setScanDx( dx ) ;
			Color colour = Color.white ;
			if( !valid )
				colour = Color.red ;
			_w.dx.setBackground( colour ) ;
			if( !( temp.equals( "" ) ) )
				_w.noiseTextBox.setValue( calculateNoise() ) ;
			updateSizeOfPixels() ;
			if( scuba2 )
				updateScuba2Panel() ;
		}
		else if( tbwe == _w.dy )
		{
			_iterObs.setScanDy( _w.dy.getValue() ) ;
			if( !( _w.dy.getValue().equals( "" ) ) )
				_w.noiseTextBox.setValue( calculateNoise() ) ;
			updateSizeOfPixels() ;
		}
		else if( tbwe == _w.width )
		{
			_iterObs.setWidth( _w.width.getValue() ) ;

			if( !( _w.width.getValue().equals( "" ) ) )
				_w.noiseTextBox.setValue( calculateNoise() ) ;
			updateSizeOfPixels() ;
			resetTPE() ;
		}
		else if( tbwe == _w.height )
		{
			_iterObs.setHeight( _w.height.getValue() ) ;

			if( !( _w.height.getValue().equals( "" ) ) )
				_w.noiseTextBox.setValue( calculateNoise() ) ;
			updateSizeOfPixels() ;
			resetTPE() ;
		}
		else if( tbwe == _w.posAngle )
		{
			_iterObs.setPosAngle( _w.posAngle.getValue() ) ;

			Object value = _w.scanAngle.getValue() ;
			if( value.equals( ALONG_WIDTH ) )
			{
				double pa = _iterObs.getPosAngle() + 90. ;
				_iterObs.setScanAngle( pa , 0 ) ;
			}
			else if( value.equals( ALONG_HEIGHT ) )
			{
				double pa = _iterObs.getPosAngle() ;
				_iterObs.setScanAngle( pa , 0 ) ;
			}

			resetTPE() ;
		}
		else if( tbwe == _w.acsisSampleTime )
		{
			String sampleTime = "0.1" ;
			try
			{
				String tmp = _w.acsisSampleTime.getValue() ;
				Double conversionDouble = new Double( tmp ) ;
				if( conversionDouble > 0.1 )
					sampleTime = conversionDouble.toString() ;
			}
			catch( NumberFormatException nfe ){}
			catch( Exception e ){}
			_iterObs.setSampleTime( sampleTime ) ;
			_w.noiseTextBox.setValue( calculateNoise() ) ;
			updateAreaPanel() ;
			updateSizeOfPixels() ;
		}
		else if( tbwe == _w.numberOfMapCycles )
		{
			Integer cycles = 1 ;
			try
			{
				String temp = _w.numberOfMapCycles.getValue() ;
				cycles = new Integer( temp ) ;
			}
			catch( NumberFormatException nfe ){}
			catch( Exception e ){}
			_iterObs.setIntegrations( cycles ) ;
			_w.noiseTextBox.setValue( calculateNoise() ) ;
		}
		else if( tbwe == _w.pointSourceTime )
		{
			_iterObs.setSampleTime( _w.pointSourceTime.getValue() ) ;
			_w.noiseTextBox.setValue( calculateNoise() ) ;
		}
		else
		{
			super.textBoxKeyPress( tbwe ) ;
		}
		updateTimes() ;
		updateThermometer() ;

		_iterObs.getAvEditFSM().addObserver( this ) ;
	}
	
	private void resetTPE()
	{
		if( _spItem != null )
		{
			TelescopePosEditor tpe = TpeManager.get( _spItem ) ;
			if( tpe != null )
				tpe.reset( _spItem ) ;
		}
	}

	public void dropDownListBoxAction( DropDownListBoxWidgetExt ddlbwe , int index , String val )
	{
		_iterObs.getAvEditFSM().deleteObserver( this ) ;

		if( ddlbwe == _w.scanSystem )
		{
			_iterObs.setScanSystem( SCAN_SYSTEMS[ index ] ) ;
		}
		else if( ddlbwe == _w.scanAngle )
		{
			Object value = _w.scanAngle.getValue() ;
			if( value.equals( AUTOMATIC ) )
			{
				_w.scanAngle.setEditable( false ) ;
				_iterObs.setScanAngles( null ) ;
			}
			else if( value.equals( ALONG_HEIGHT ) )
			{
				_w.scanAngle.setEditable( false ) ;
				_iterObs.setScanAngles( null ) ;
				double pa = _iterObs.getPosAngle() ;
				_iterObs.setScanAngle( pa , 0 ) ;
			}
			else if( value.equals( ALONG_WIDTH ) )
			{
				_w.scanAngle.setEditable( false ) ;
				_iterObs.setScanAngles( null ) ;
				double pa = _iterObs.getPosAngle() + 90. ;
				_iterObs.setScanAngle( pa , 0 ) ;
			}
			else if( value.equals( USER_DEF ) )
			{
				_w.scanAngle.setEditable( true ) ;
				_w.scanAngle.setValue( "" ) ;

				_iterObs.setScanSystem( _w.scanSystem.getStringValue() ) ;
			}
		}
		else if( ddlbwe == _w.scanSystem )
		{
			_iterObs.setScanAngles( _w.scanSystem.getStringValue() ) ;
		}
		else if( ddlbwe == _w.harpRasters )
		{
			double value = HARP_RASTER_VALUES[ _w.harpRasters.getSelectedIndex() ] ;
			_iterObs.setScanDy( value ) ;
			_w.dy.setValue( _iterObs.getScanDy() ) ;
		}
		else if( ddlbwe == _w.scanningStrategies )
		{
			String value = ( String )_w.scanningStrategies.getSelectedItem() ;

			_iterObs.setScanStrategy( value ) ;
			
			if( SCAN_PATTERN_POINT.equals( value ) )
			{
				_iterObs.rmIntegrations() ;
				_iterObs.setSampleTime( "4.0" ) ;
				_w.pointSourceTime.setValue( _iterObs.getSampleTime() ) ;
			}
			else
			{
				_iterObs.rmSampleTime() ;
				String integrations = _iterObs.getIntegrations() ;
				if( integrations == null || integrations.trim().equals( "" ) || Integer.parseInt( integrations ) < 1 )
					_iterObs.setIntegrations( 1 ) ;
			}
			// Should be SCUBA-2 anyway, but better safe ...
			if( scuba2 )
				updateScuba2Panel() ;

			_updateWidgets() ;
		}
		else
		{
			super.dropDownListBoxAction( ddlbwe , index , val ) ;
		}
		updateTimes() ;
		updateThermometer() ;

		_iterObs.getAvEditFSM().addObserver( this ) ;
	}

	public void keyPressed( KeyEvent e ){}

	public void keyReleased( KeyEvent e )
	{
		_iterObs.getAvEditFSM().deleteObserver( this ) ;

		Component component = _w.scanAngle.getEditor().getEditorComponent() ;
		if( e.getSource() == component )
		{
			String value = (( JTextField )component).getText() ;
			_iterObs.setScanAngles( value ) ;
		}

		super._updateWidgets() ;

		_iterObs.getAvEditFSM().addObserver( this ) ;
	}

	public void keyTyped( KeyEvent e ){}

	public void optionAction( OptionWidgetExt owe )
	{
		_iterObs.getAvEditFSM().deleteObserver( this ) ;
		_iterObs.setRasterMode( owe.getActionCommand() ) ;
		_iterObs.getAvEditFSM().addObserver( this ) ;
	}

	public void checkBoxAction( CheckBoxWidgetExt cbwe )
	{
		if( cbwe == _w.rowReversal )
			_iterObs.setRowReversal( _w.rowReversal.getBooleanValue() ) ;
	}

	public void commandButtonAction( CommandButtonWidgetExt cbwe )
	{
		if( cbwe == _w.defaultButton )
			_iterObs.setDefaults() ;
		
		_updateWidgets() ;
	}

	public void setInstrument( SpInstObsComp spInstObsComp )
	{
		deleteWatchers() ;
		if( spInstObsComp instanceof SpInstHeterodyne )
		{
			scuba2 = false ;
			SpInstHeterodyne heterodyne = ( SpInstHeterodyne )spInstObsComp ;
			harp = heterodyne.getFrontEnd().equals( "HARP" ) ;
			
			if( harp )
				_harpSetup() ;
			else
				_acsisSetup() ;
		}
		else if( spInstObsComp instanceof SpInstSCUBA2 )
		{
			harp = false ;
			scuba2 = true ;
			scuba2Setup() ;
		}
		
		super.setInstrument( spInstObsComp ) ;
		addWatchers() ;
	}

	public void update( Observable o , Object arg )
	{
		_updateWidgets() ;
	}

	protected double calculateNoise( SpInstHeterodyne inst , double airmass , double tau )
	{
		double tSys = HeterodyneNoise.getTsys( inst.getFrontEnd() , tau , airmass , inst.getRestFrequency( 0 ) / 1.0e9 , inst.getMode().equalsIgnoreCase( "ssb" ) ) ;

		_noiseToolTip = "airmass = " + ( Math.rint( airmass * 10 ) / 10 ) + ", Tsys = " + ( Math.rint( tSys * 10 ) / 10 ) ;
		if( "acsis".equalsIgnoreCase( inst.getBackEnd() ) )
			return CoordConvert.round( HeterodyneNoise.getHeterodyneNoise( _iterObs , inst , tSys ) , 3 ) ;
		else
			return -999.9 ;
	}

	private static orac.jcmt.util.Scuba2Time s2t = null ;
	protected double calculateNoise( SpInstSCUBA2 inst , String wavelength , double airmass , double tau )
	{
		Scuba2Noise s2n = Scuba2Noise.getInstance() ;
		if( s2t == null )
			s2t = new orac.jcmt.util.Scuba2Time() ;
		double noise = s2n.noiseForMapTotalIntegrationTime( wavelength , s2t.scan( _iterObs ) , tau , airmass , _iterObs.getWidth() , _iterObs.getHeight() ) ;
		noise = CoordConvert.round( noise , 3 ) ;
		return noise ;
	}

	/**
	 * This updates the time fields on heterodyne setups. It can only be used for heterodyne and just updates the non-editable widgets. For ease of display, the fractional part is truncated to 2 decimal places.
	 */
	private void updateTimes()
	{
		// First see if the heterodyne panel is visible
		if( _w.heterodynePanel.isVisible() )
		{
			// All values to be truncated to 2dp
			DecimalFormat formatter = new DecimalFormat() ;
			formatter.setMaximumFractionDigits( 2 ) ;
			formatter.setGroupingUsed( false ) ;

			// We must be using a heterodyne
			double secsPerRow = _iterObs.getSecsPerRow() ;
			if( secsPerRow <= DEFAULT_SECS_ROW )
				_w.secsPerRow.setForeground( safeColor ) ;
			else if( secsPerRow < 2 * DEFAULT_SECS_ROW )
				_w.secsPerRow.setForeground( warnColor ) ;
			else
				_w.secsPerRow.setForeground( errColor ) ;
			_w.secsPerRow.setValue( formatter.format( secsPerRow ) ) ;

			double obsTime = _iterObs.getElapsedTime() ;
			if( obsTime <= DEFAULT_SECS_MAP )
				_w.secsPerObservation.setForeground( safeColor ) ;
			else if( obsTime < 2 * DEFAULT_SECS_MAP )
				_w.secsPerObservation.setForeground( warnColor ) ;
			else
				_w.secsPerObservation.setForeground( errColor ) ;
			_w.secsPerObservation.setValue( formatter.format( obsTime ) ) ;
		}
	}

	/**
	 * Update the thermometer. Only need this for heterodyne at the moment.
	 */
	private void updateThermometer()
	{
		// First see if the heterodyne panel is visible
		if( _w.heterodynePanel.isVisible() )
		{
			// Get the instrument
			SpInstObsComp inst = SpTreeMan.findInstrument( _iterObs ) ;
			if( inst != null && inst instanceof SpInstHeterodyne )
			{
				SpInstHeterodyne heterodyne = ( SpInstHeterodyne )inst ;
				// Get the number of channels
				int maxChannels = 0 ;
				for( int i = 0 ; i < heterodyne.getNumSubSystems() ; i++ )
				{
					if( heterodyne.getChannels( i ) > maxChannels )
						maxChannels = heterodyne.getChannels( i ) ;
				}
	
				int samplesPerRow = ( int )( _iterObs.getWidth() / _iterObs.getScanDx() ) ;
				int numberOfRows = ( int )( _iterObs.getHeight() / _iterObs.getScanDy() ) ;
	
				int fileSize = ( maxChannels * samplesPerRow * numberOfRows * 4 ) / ( 1024 * 1024 ) ;
				_w.thermometer.setExtent( fileSize ) ;
			}
		}
	}
}
