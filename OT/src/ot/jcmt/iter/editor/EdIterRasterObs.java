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

import java.awt.event.KeyListener;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Observer;
import java.util.Observable;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JTextField;

import jsky.util.gui.DialogUtil;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.OptionWidgetWatcher;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.tpe.TpeManager;

import gemini.sp.SpItem;
import gemini.sp.SpMSB;
import gemini.sp.SpProg;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.SpJCMTConstants;
import orac.jcmt.inst.SpDRRecipe;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.iter.SpIterRasterObs;
import orac.jcmt.util.ScubaNoise;
import orac.jcmt.util.HeterodyneNoise;
import orac.jcmt.inst.SpInstSCUBA2;

import gemini.util.MathUtil;

/**
 * This is the editor for the Raster Observe Mode iterator component (ACSIS).
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterRasterObs extends EdIterJCMTGeneric implements Observer , OptionWidgetWatcher , KeyListener , CommandButtonWidgetWatcher , SpJCMTConstants
{
	private IterRasterObsGUI _w; // the GUI layout panel
	private SpIterRasterObs _iterObs;
	private final String[] SCAN_PA_CHOICES = { "automatic" , "user def" };
	private final String[] SAMPLE_TIME_CHOICES = { "4.0" , "5.0" , "6.0" , "7.0" };

	// The following defines the maximum file size we are currently allowing for raster.
	// Since this is for use with the thermometer, which only accepts integers, we will specify the maxium size in MBytes
	private int _maxFileSize = 2048;

	// Some default values for the non-editable text fields
	private final int DEFAULT_SECS_ROW = 240;
	private final int DEFAULT_SECS_MAP = 3600;

	// Global flag indicating whether we are using acsis or das
	boolean _isAcsis = true;
	TreeMap harpMap = new TreeMap();
	private final String[] HARP_RASTER_NAMES = { "1 array" , "1/2 array" , "1/4 array" , "1/8 array" , "1 sample" , "3/4 array" };
	private final double[] HARP_RASTER_VALUES = { 116.4171 , 58.2086 , 29.1043 , 14.5521 , 7.2761 , 87.3128 };
	boolean harp = false;
	boolean scuba2 = false;

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterRasterObs()
	{
		super( new IterRasterObsGUI() );

		_title = "Scan/Raster";
		_presSource = _w = ( IterRasterObsGUI )super._w;
		_description = "Scan/Raster Map";

		_w.scanAngle.setChoices( SCAN_PA_CHOICES );
		_w.scanSystem.setChoices( SCAN_SYSTEMS );
		_w.sampleTime.setChoices( SAMPLE_TIME_CHOICES );
		_w.thermometer.setMaximum( _maxFileSize );

		_w.scanningStrategies.setChoices( SCAN_STRATAGIES );

		for( int index = 0 ; index < HARP_RASTER_NAMES.length ; index++ )
			_w.harpRasters.addChoice( "step " + HARP_RASTER_NAMES[ index ] + " (" + MathUtil.round( HARP_RASTER_VALUES[ index ] , 1 ) + "\")" );

		if( System.getProperty( "FREQ_EDITOR_CFG" ) != null )
		{
			if( System.getProperty( "FREQ_EDITOR_CFG" ).indexOf( "acsis" ) != -1 )
			{
				// Using acsis setup
				_w.sampleTime.setVisible( false );
				_w.sampleTime.setEnabled( false );
				_w.acsisSampleTime.setVisible( true );
				_w.acsisSampleTime.setEnabled( true );
				_w.acsisSampleTime.addWatcher( this );
				_isAcsis = true;
			}
			else
			{
				// Using das setup
				_w.sampleTime.setVisible( true );
				_w.sampleTime.setEnabled( true );
				_w.acsisSampleTime.setVisible( false );
				_w.acsisSampleTime.setEnabled( false );
				_w.sampleTime.addWatcher( this );
				_isAcsis = false;
			}
		}
		else
		{
			// Using acsis setup
			_w.sampleTime.setVisible( false );
			_w.sampleTime.setEnabled( false );
			_w.acsisSampleTime.setVisible( true );
			_w.acsisSampleTime.setEnabled( true );
			_w.acsisSampleTime.addWatcher( this );
			_isAcsis = true;
		}

		_w.dx.addWatcher( this );
		_w.dy.addWatcher( this );
		_w.width.addWatcher( this );
		_w.height.addWatcher( this );
		_w.posAngle.addWatcher( this );
		_w.rowReversal.addWatcher( this );
		_w.scanSystem.addWatcher( this );
		_w.scanAngle.addWatcher( this );
		_w.defaultButton.addWatcher( this );
		_w.scanAngle.getEditor().getEditorComponent().addKeyListener( this );

		_w.scanningStrategies.addWatcher( this );

		_w.harpRasters.addWatcher( this );

		_w.frequencyPanel.setVisible( false );
	}

	/**
	 * Override setup to store away a reference to the Raster Iterator.
	 */
	public void setup( SpItem spItem )
	{
		_iterObs = ( SpIterRasterObs )spItem;

		SpInstObsComp inst = SpTreeMan.findInstrument( _iterObs );
		scuba2 = inst instanceof SpInstSCUBA2;
		harp = ( inst instanceof SpInstHeterodyne && ( ( SpInstHeterodyne )inst ).getFrontEnd().equals( "HARP" ) );
		if( harp )
			_harpSetup();
		super.setup( spItem );
		_iterObs.getAvEditFSM().addObserver( this );
	}

	private void _harpSetup()
	{
		_iterObs.setScanDx( 7.2761 );
		double dy = _iterObs.getScanDy();
		boolean found = false;
		for( int index = 0 ; index < HARP_RASTER_VALUES.length ; index++ )
		{
			if( dy == HARP_RASTER_VALUES[ index ] )
			{
				_w.harpRasters.deleteWatcher( this );
				_w.harpRasters.setSelectedIndex( index );
				_w.harpRasters.addWatcher( this );
				found = true;
				break;
			}
		}
		if( !found )
		{
			_iterObs.setScanDy( HARP_RASTER_VALUES[ 0 ] );
			_w.harpRasters.deleteWatcher( this );
			_w.harpRasters.setSelectedIndex( 0 );
			_w.harpRasters.addWatcher( this );
		}
	}

	private void scuba2Setup()
	{
		if( scuba2 )
			_w.addScuba2Panel();
		else
			_w.addNonScuba2Panel();

		boolean visible = ( !scuba2 || _iterObs.getScanStrategy().equals( SCAN_PATTERN_PONG ) );

		_w.dy.setVisible( visible );
		_w.scanSpacingLabel.setVisible( visible );
		_w.arcSecsLabel4.setVisible( visible );

		_w.sizeOfXPixel.setVisible( !scuba2 );
		_w.sizeOfYPixel.setVisible( !scuba2 );
		_w.sizeOfXPixelLabel.setVisible( !scuba2 );
		_w.sizeOfYPixelLabel.setVisible( !scuba2 );
		_w.dimensionWarningTextTop.setVisible( !scuba2 );
		_w.spacingLabel.setVisible( !scuba2 );

		if( !visible )
		{
			boolean bous = _iterObs.getScanStrategy().equals( SCAN_PATTERN_BOUS );
			if( bous )
			{
				_iterObs.getTable().noNotifySet( ATTR_SCANAREA_SCAN_SYSTEM , FPLANE , 0 );
				_w.scanSystem.deleteWatcher( this );
				_w.scanSystem.setSelectedItem( FPLANE );
				_w.scanSystem.addWatcher( this );
				_iterObs.getTable().noNotifyRm( ATTR_SCANAREA_SCAN_PA );
				_w.scanAngle.deleteWatcher( this );
				_w.scanAngle.setSelectedItem( SCAN_PA_CHOICES[ 0 ] );
				_w.scanAngle.setEditable( false );
				_w.scanAngle.addWatcher( this );
			}
		}

		_w.scanAngle.setEnabled( visible );
		_w.scanSystem.setEnabled( visible );
	}

	protected void _updateWidgets()
	{
		super._updateWidgets();

		if( harp )
			_w.addHarpPanel();
		else
			_w.addNonHarpPanel();

		_w.dx.setEnabled( !harp );
		_w.dy.setEnabled( !harp );

		scuba2Setup();

		try
		{
			_w.dx.setValue( _iterObs.getScanDx() );
			_w.dx.setCaretPosition( 0 );
		}
		catch( UnsupportedOperationException e )
		{
			DialogUtil.message( _w , "Warning:\n" + e.getMessage() );
		}

		_w.dx.setValue( _iterObs.getScanDx() );
		_w.dx.setCaretPosition( 0 );
		_w.dy.setValue( _iterObs.getScanDy() );
		_w.dy.setCaretPosition( 0 );
		_w.width.setValue( _iterObs.getWidth() );
		_w.width.setCaretPosition( 0 );
		_w.height.setValue( _iterObs.getHeight() );
		_w.height.setCaretPosition( 0 );
		_w.posAngle.setValue( _iterObs.getPosAngle() );
		_w.posAngle.setCaretPosition( 0 );

		if( ( _iterObs.getScanAngles() == null ) || ( _iterObs.getScanAngles().size() == 0 ) )
		{
			_w.scanAngle.setEditable( false );
			_w.scanAngle.setValue( SCAN_PA_CHOICES[ 0 ] );
		}
		else
		{
			String scanAngleString = "";
			for( int i = 0 ; i < _iterObs.getScanAngles().size() ; i++ )
				scanAngleString += ", " + _iterObs.getScanAngle( i );

			_w.scanAngle.setEditable( true );
			_w.scanAngle.setValue( scanAngleString.substring( 2 ) );
		}

		_w.scanSystem.setValue( _iterObs.getScanSystem() );
		_w.switchingMode.setValue( SWITCHING_MODE_BEAM );
		_w.switchingMode.setEnabled( false );
		_w.rowReversal.setValue( _iterObs.getRowReversal() );
		if( !_isAcsis )
			_w.sampleTime.setValue( ( int )_iterObs.getSampleTime() - SAMPLE_TIME_CHOICES.length );
		else
			_w.acsisSampleTime.setValue( _iterObs.getSampleTime() );

		updateTimes();
		updateThermometer();
		updateSizeOfPixels();
	}

	private void updateSizeOfPixels()
	{
		if( !scuba2 )
		{
			boolean displayWarning = false;
			double sizeOfXPixel = ( _iterObs.getWidth() / _iterObs.getScanDx() );
			double sizeOfYPixel = ( _iterObs.getHeight() / _iterObs.getScanDy() );
			int correctedSizeOfXPixel = ( int )Math.floor( sizeOfXPixel + 1.5 );
			int correctedSizeOfYPixel = ( int )Math.floor( sizeOfYPixel + 1.5 );

			_w.sizeOfXPixel.setText( Integer.toString( correctedSizeOfXPixel ) );
			_w.sizeOfYPixel.setText( Integer.toString( correctedSizeOfYPixel ) );

			if( !harp && sizeOfXPixel - Math.floor( sizeOfXPixel ) != 0. )
			{
				_w.sizeOfXPixelLabel.setForeground( Color.red );
				displayWarning = true;
			}
			else
			{
				_w.sizeOfXPixelLabel.setForeground( Color.black );
			}

			if( !harp && sizeOfYPixel - Math.floor( sizeOfYPixel ) != 0. )
			{
				_w.sizeOfYPixelLabel.setForeground( Color.red );
				displayWarning = true;
			}
			else
			{
				_w.sizeOfYPixelLabel.setForeground( Color.black );
			}

			_w.dimensionWarningTextTop.setVisible( displayWarning );
			_w.dimensionWarningTextBottom.setVisible( displayWarning );
		}
	}

	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		_iterObs.getAvEditFSM().deleteObserver( this );

		if( tbwe == _w.dx )
		{
			_iterObs.setScanDx( _w.dx.getValue() );
			if( !( _w.dx.getValue().equals( "" ) ) )
				_w.noiseTextBox.setValue( calculateNoise() );
			updateSizeOfPixels();
		}
		else if( tbwe == _w.dy )
		{
			_iterObs.setScanDy( _w.dy.getValue() );
			if( !( _w.dy.getValue().equals( "" ) ) )
				_w.noiseTextBox.setValue( calculateNoise() );
			updateSizeOfPixels();
		}
		else if( tbwe == _w.width )
		{
			_iterObs.setWidth( _w.width.getValue() );

			if( !( _w.width.getValue().equals( "" ) ) )
				_w.noiseTextBox.setValue( calculateNoise() );
			updateSizeOfPixels();

			// Probably implemented in a different way in Gemini ot-2000B.12.
			try
			{
				TpeManager.get( _spItem ).reset( _spItem );
			}
			catch( NullPointerException e )
			{
				// ignore
			}

		}
		else if( tbwe == _w.height )
		{
			_iterObs.setHeight( _w.height.getValue() );

			if( !( _w.height.getValue().equals( "" ) ) )
				_w.noiseTextBox.setValue( calculateNoise() );
			updateSizeOfPixels();

			// Probably implemented in a different way in Gemini ot-2000B.12.
			try
			{
				TpeManager.get( _spItem ).reset( _spItem );
			}
			catch( NullPointerException e )
			{
				// ignore
			}

		}
		else if( tbwe == _w.posAngle )
		{
			_iterObs.setPosAngle( _w.posAngle.getValue() );

			// Probably implemented in a different way in Gemini ot-2000B.12.
			try
			{
				TpeManager.get( _spItem ).reset( _spItem );
			}
			catch( NullPointerException e )
			{
				// ignore
			}

		}
		else if( tbwe == _w.acsisSampleTime )
		{
			String sampleTime = "0.1";
			try
			{
				sampleTime = _w.acsisSampleTime.getValue();
				Double conversionDouble = new Double( sampleTime );
				if( conversionDouble.doubleValue() > 0.1 )
					sampleTime = conversionDouble.toString();
				else
					sampleTime = "0.1";
			}
			catch( NumberFormatException nfe ){}
			catch( Exception e ){}
			_iterObs.setSampleTime( sampleTime );
			_w.noiseTextBox.setValue( calculateNoise() );
		}

		super.textBoxKeyPress( tbwe );
		updateTimes();
		updateThermometer();

		_iterObs.getAvEditFSM().addObserver( this );
	}

	public void dropDownListBoxAction( DropDownListBoxWidgetExt ddlbwe , int index , String val )
	{
		_iterObs.getAvEditFSM().deleteObserver( this );

		if( ddlbwe == _w.scanSystem )
		{
			_iterObs.setScanSystem( SCAN_SYSTEMS[ index ] );
		}
		else if( ddlbwe == _w.scanAngle )
		{
			Object value = _w.scanAngle.getValue();
			if( value.equals( SCAN_PA_CHOICES[ 0 ] ) )
			{
				_w.scanAngle.setEditable( false );
				_iterObs.setScanAngles( null );
			}
			else if( value.equals( SCAN_PA_CHOICES[ 1 ] ) )
			{
				_w.scanAngle.setEditable( true );
				_w.scanAngle.setValue( "" );

				_iterObs.setScanSystem( _w.scanSystem.getStringValue() );
			}
		}
		else if( ddlbwe == _w.sampleTime )
		{
			_iterObs.setSampleTime( _w.sampleTime.getStringValue() );
			_w.noiseTextBox.setValue( calculateNoise() );
		}
		else if( ddlbwe == _w.scanSystem )
		{
			_iterObs.setScanAngles( _w.scanSystem.getStringValue() );
		}
		else if( ddlbwe == _w.harpRasters )
		{
			double value = HARP_RASTER_VALUES[ _w.harpRasters.getSelectedIndex() ];
			_iterObs.setScanDy( value );
			_w.dy.setValue( _iterObs.getScanDy() );
		}
		else if( ddlbwe == _w.scanningStrategies )
		{
			String value = SCAN_STRATAGIES[ _w.scanningStrategies.getSelectedIndex() ];
			_iterObs.setScanStrategy( value );
			scuba2Setup();
		}
		super.dropDownListBoxAction( ddlbwe , index , val );
		updateTimes();
		updateThermometer();

		_iterObs.getAvEditFSM().addObserver( this );
	}

	public void keyPressed( java.awt.event.KeyEvent e ){}

	public void keyReleased( java.awt.event.KeyEvent e )
	{
		_iterObs.getAvEditFSM().deleteObserver( this );

		if( e.getSource() == _w.scanAngle.getEditor().getEditorComponent() )
		{
			_iterObs.setScanAngles( ( ( JTextField )_w.scanAngle.getEditor().getEditorComponent() ).getText() );
		}

		_iterObs.getAvEditFSM().addObserver( this );
	}

	public void keyTyped( java.awt.event.KeyEvent e ){}

	public void optionAction( OptionWidgetExt owe )
	{
		_iterObs.getAvEditFSM().deleteObserver( this );
		_iterObs.setRasterMode( owe.getActionCommand() );
		_iterObs.getAvEditFSM().addObserver( this );
	}

	public void checkBoxAction( CheckBoxWidgetExt cbwe )
	{
		if( cbwe == _w.rowReversal )
			_iterObs.setRowReversal( _w.rowReversal.getBooleanValue() );
	}

	public void commandButtonAction( CommandButtonWidgetExt cbwe )
	{
		if( cbwe == _w.defaultButton )
			_iterObs.setDefaults();
		
		_updateWidgets();
	}

	public void setInstrument( SpInstObsComp spInstObsComp )
	{
		if( ( spInstObsComp != null ) && ( spInstObsComp instanceof SpInstHeterodyne ) )
		{
			_w.heterodynePanel.setVisible( true );
			_w.scanSystem.setEnabled( true );
			_w.scanPanel.setVisible( true );
		}
		else
		{
			_w.heterodynePanel.setVisible( false );
			_w.scanSystem.setEnabled( true );
			_w.scanPanel.setVisible( true );
		}

		super.setInstrument( spInstObsComp );
	}

	public void update( Observable o , Object arg )
	{
		_updateWidgets();
	}

	protected double calculateNoise( int integrations , double wavelength , double nefd , int[] status )
	{
		return ScubaNoise.noise_level( integrations , wavelength , "SCAN" , nefd , status , _iterObs.getHeight() , _iterObs.getWidth() );
	}

	protected double calculateNoise( SpInstHeterodyne inst , double airmass , double tau )
	{
		double tSys = HeterodyneNoise.getTsys( inst.getFrontEnd() , tau , airmass , inst.getRestFrequency( 0 ) / 1.0e9 , inst.getMode().equalsIgnoreCase( "ssb" ) );

		_noiseToolTip = "airmass = " + ( Math.rint( airmass * 10 ) / 10 ) + ", Tsys = " + ( Math.rint( tSys * 10 ) / 10 );
		if( "acsis".equalsIgnoreCase( inst.getBackEnd() ) )
			return MathUtil.round( HeterodyneNoise.getHeterodyneNoise( _iterObs , inst , tau , airmass ) , 3 );
		else
			return -999.9;
	}

	/**
	 * This updates the time fields on heterodyne setups. It can only be used for heterodyne and just updates the non-editable widgets. For ease of display, the fractional part is truncated to 2 decimal places.
	 */
	private void updateTimes()
	{
		// First see if the heterodyne panel is visible
		Color safeColor = Color.black;
		Color warnColor = Color.yellow.darker();
		Color errColor = Color.red.darker();
		if( _w.heterodynePanel.isVisible() )
		{
			// All values to be truncated to 2dp
			DecimalFormat formatter = new DecimalFormat();
			formatter.setMaximumFractionDigits( 2 );
			formatter.setGroupingUsed( false );

			// We must be using a heterodyne
			double secsPerRow = _iterObs.getSecsPerRow();
			if( secsPerRow <= DEFAULT_SECS_ROW )
				_w.secsPerRow.setForeground( safeColor );
			else if( secsPerRow < 2 * DEFAULT_SECS_ROW )
				_w.secsPerRow.setForeground( warnColor );
			else
				_w.secsPerRow.setForeground( errColor );
			_w.secsPerRow.setValue( formatter.format( secsPerRow ) );

			double obsTime = _iterObs.getElapsedTime();
			if( obsTime <= DEFAULT_SECS_MAP )
				_w.secsPerObservation.setForeground( safeColor );
			else if( obsTime < 2 * DEFAULT_SECS_MAP )
				_w.secsPerObservation.setForeground( warnColor );
			else
				_w.secsPerObservation.setForeground( errColor );
			_w.secsPerObservation.setValue( formatter.format( obsTime ) );
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
			SpInstObsComp inst = SpTreeMan.findInstrument( _iterObs );
			if( inst == null | !( inst instanceof SpInstHeterodyne ) )
				return;

			// Get the number of channels
			int maxChannels = 0;
			for( int i = 0 ; i < ( ( SpInstHeterodyne )inst ).getNumSubSystems() ; i++ )
			{
				if( ( ( SpInstHeterodyne )inst ).getChannels( i ) > maxChannels )
					maxChannels = ( ( SpInstHeterodyne )inst ).getChannels( i );
			}

			// See if we can get the DR recipe component which will allow us
			// to get any channel truncation.  If we can't find it assume 0
			// truncation.  To do this we need to go back up the hierarchy
			SpItem parent = _iterObs.parent();
			SpDRRecipe recipe = null;
			int truncChannels = 0;
			while( parent != null )
			{
				if( parent instanceof SpMSB )
				{
					// See if we can find the DRRecipe component
					Vector drRecipeCompts = SpTreeMan.findAllItems( parent , "orac.jcmt.inst.SpDRRecipe" );
					if( drRecipeCompts != null && drRecipeCompts.size() > 0 )
					{
						// We have found it, and there should only be 1, so assume this
						recipe = ( SpDRRecipe )drRecipeCompts.get( 0 );
						break;
					}
				}
				else if( parent instanceof SpProg )
				{
					// See if we can find the DRRecipe component
					Vector drRecipeCompts = SpTreeMan.findAllItems( parent , "orac.jcmt.inst.SpDRRecipe" );
					if( drRecipeCompts != null && drRecipeCompts.size() > 0 )
					{
						// We have found it, and there should only be 1, so assume this
						recipe = ( SpDRRecipe )drRecipeCompts.get( 0 );
						break;
					}
				}
				parent = parent.parent();
			}
			maxChannels -= ( 2 * truncChannels );

			int samplesPerRow = ( int )( _iterObs.getWidth() / _iterObs.getScanDx() );
			int numberOfRows = ( int )( _iterObs.getHeight() / _iterObs.getScanDy() );

			int fileSize = ( int )( ( maxChannels * samplesPerRow * numberOfRows * 4 ) / ( 1024 * 1024 ) );
			_w.thermometer.setExtent( fileSize );
		}
	}
}
