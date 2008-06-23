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
import java.util.TreeMap ;

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
import orac.jcmt.util.ScubaNoise ;
import orac.jcmt.util.HeterodyneNoise ;
import orac.jcmt.inst.SpInstSCUBA2 ;

import orac.util.CoordConvert ;

/**
 * This is the editor for the Raster Observe Mode iterator component (ACSIS).
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterRasterObs extends EdIterJCMTGeneric implements Observer , OptionWidgetWatcher , KeyListener , CommandButtonWidgetWatcher , SpJCMTConstants
{
	private IterRasterObsGUI _w ; // the GUI layout panel
	private SpIterRasterObs _iterObs ;
	private final String[] SCAN_PA_CHOICES = { "automatic" , "user def" } ;
	private final String[] SAMPLE_TIME_CHOICES = { "4.0" , "5.0" , "6.0" , "7.0" } ;

	// The following defines the maximum file size we are currently allowing for raster.
	// Since this is for use with the thermometer, which only accepts integers, we will specify the maxium size in MBytes
	private int _maxFileSize = 2048 ;

	// Some default values for the non-editable text fields
	private final int DEFAULT_SECS_ROW = 240 ;
	private final int DEFAULT_SECS_MAP = 3600 ;
	
	private final Color safeColor = Color.black ;
	private final Color warnColor = Color.yellow.darker() ;
	private final Color errColor = Color.red.darker() ;

	// Global flag indicating whether we are using acsis or das
	private boolean _isAcsis = true ;
	private boolean harp = false ;
	private boolean scuba2 = false ;
	TreeMap harpMap = new TreeMap() ;
	
	private final String[] HARP_RASTER_NAMES = { "1 array" , "1/2 array" , "1/4 array" , "1/8 array" , "1 sample" , "3/4 array" } ;
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

		_title = "Scan/Raster" ;
		_presSource = _w = ( IterRasterObsGUI )super._w ;
		_description = "Scan/Raster Map" ;

		setDefaults() ;
		addWatchers() ;

		_w.frequencyPanel.setVisible( false ) ;
	}

	private void setDefaults()
	{
		_w.scanAngle.setChoices( SCAN_PA_CHOICES ) ;
		_w.scanSystem.setChoices( SCAN_SYSTEMS ) ;
		_w.sampleTime.setChoices( SAMPLE_TIME_CHOICES ) ;
		_w.thermometer.setMaximum( _maxFileSize ) ;

		_w.scanningStrategies.setChoices( SCAN_STRATEGIES ) ;
		
		for( int index = 0 ; index < HARP_RASTER_NAMES.length ; index++ )
			_w.harpRasters.addChoice( "step " + HARP_RASTER_NAMES[ index ] + " (" + CoordConvert.round( HARP_RASTER_VALUES[ index ] , 1 ) + "\")" ) ;
	}
	
	private void addWatchers()
	{
		_w.acsisSampleTime.addWatcher( this ) ;
		_w.sampleTime.addWatcher( this ) ;
		
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
		_w.sampleTime.deleteWatcher( this ) ;
		
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

		_w.sampleTime.setVisible( false ) ;
		_w.sampleTime.setEnabled( false ) ;
		_w.acsisSampleTime.setVisible( true ) ;
		_w.acsisSampleTime.setEnabled( true ) ;
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
		_w.height.setEnabled( true ) ;
		_w.width.setEnabled( true ) ;
		_w.posAngle.setEnabled( true ) ;
		_w.scanAngle.setEnabled( true ) ;
		_w.scanSystem.setEnabled( true ) ;
		
		_w.addNonScuba2Panel() ;
		_w.addHarpPanel() ;
		
		_w.subAreaPanel.setVisible( true  ) ;
		
		_w.sampleTime.setVisible( false ) ;
		_w.sampleTime.setEnabled( false ) ;
		_w.acsisSampleTime.setVisible( true ) ;
		_w.acsisSampleTime.setEnabled( true ) ;
	}

	private void scuba2Setup()
	{
		_w.addScuba2Panel() ;

		_w.subAreaPanel.setVisible( false ) ;

		_w.addNonHarpPanel() ;
		_w.nonHarpPanel.setVisible( false ) ;
		
		boolean allowScan = SCAN_PATTERN_BOUS.equals( _iterObs.getScanStrategy() ) ;
		
		if( !allowScan )
			resetScanPanel() ;
		
		_w.scanAngle.setEnabled( allowScan ) ;
		_w.scanSystem.setEnabled( allowScan ) ;
		
		_w.sampleTime.setVisible( false ) ;
		_w.sampleTime.setEnabled( false ) ;
		_w.acsisSampleTime.setVisible( true ) ;
		_w.acsisSampleTime.setEnabled( true ) ;
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
			_w.scanAngle.setValue( SCAN_PA_CHOICES[ 0 ] ) ;
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
		String strategy = _iterObs.getScanStrategy() ;
		
		_w.scanningStrategies.setValue( strategy ) ;
		
		boolean pointSource = SCAN_PATTERN_POINT.equals( strategy ) ;
		boolean allowScan = SCAN_PATTERN_BOUS.equals( strategy ) ;
		
		if( pointSource )
			_w.pointSourceTime.setValue( _iterObs.getSampleTime() ) ;
		else
			_w.numberOfMapCycles.setValue( _iterObs.getIntegrations() ) ;
		
		_w.scanSpeed.setValue( _iterObs.getScanVelocity() ) ;
		
		_w.scanAngle.setEnabled( allowScan ) ;
		_w.scanSystem.setEnabled( allowScan ) ;
		
		if( !allowScan )
			resetScanPanel() ;
		
		_w.mapCyclesPanel.setVisible( !pointSource ) ;
		_w.pointSourcePanel.setVisible( pointSource ) ;
		
		_w.dx.setEnabled( !pointSource ) ;
		_w.dy.setEnabled( false ) ;
		_w.height.setEnabled( !pointSource ) ;
		_w.width.setEnabled( !pointSource ) ;
		_w.posAngle.setEnabled( !pointSource ) ;
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
		
		// sample times
		if( !_isAcsis )
			_w.sampleTime.setValue( ( int )_iterObs.getSampleTime() - SAMPLE_TIME_CHOICES.length ) ;
		else
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
			double sizeOfXPixel = ( _iterObs.getWidth() / _iterObs.getScanDx() ) ;
			double sizeOfYPixel = ( _iterObs.getHeight() / _iterObs.getScanDy() ) ;
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
			if( value.equals( SCAN_PA_CHOICES[ 0 ] ) )
			{
				_w.scanAngle.setEditable( false ) ;
				_iterObs.setScanAngles( null ) ;
			}
			else if( value.equals( SCAN_PA_CHOICES[ 1 ] ) )
			{
				_w.scanAngle.setEditable( true ) ;
				_w.scanAngle.setValue( "" ) ;

				_iterObs.setScanSystem( _w.scanSystem.getStringValue() ) ;
			}
		}
		else if( ddlbwe == _w.sampleTime )
		{
			_iterObs.setSampleTime( _w.sampleTime.getStringValue() ) ;
			_w.noiseTextBox.setValue( calculateNoise() ) ;
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
			String value = SCAN_STRATEGIES[ _w.scanningStrategies.getSelectedIndex() ] ;
			
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
				_iterObs.setIntegrations( 1 ) ;
			}
			updateScuba2Panel() ;
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
			_isAcsis = true ;
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
			_isAcsis = false ;
			harp = false ;
			scuba2 = true ;
			scuba2Setup() ;
		}
		else{}
		
		super.setInstrument( spInstObsComp ) ;
		addWatchers() ;
	}

	public void update( Observable o , Object arg )
	{
		_updateWidgets() ;
	}

	protected double calculateNoise( int integrations , double wavelength , double nefd , int[] status )
	{
		return ScubaNoise.noise_level( integrations , wavelength , "SCAN" , nefd , status , _iterObs.getHeight() , _iterObs.getWidth() ) ;
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
