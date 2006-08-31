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
import java.util.Vector;

import javax.swing.ButtonGroup;
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

import gemini.util.MathUtil ; 

/**
 * This is the editor for the Raster Observe Mode iterator component (ACSIS).
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterRasterObs extends EdIterJCMTGeneric implements Observer, OptionWidgetWatcher,
    KeyListener,  CommandButtonWidgetWatcher {

  private IterRasterObsGUI _w;       // the GUI layout panel

  private SpIterRasterObs _iterObs;

  private final String [] SCAN_PA_CHOICES = { "automatic", "user def" };
  private final String [] SAMPLE_TIME_CHOICES = {"4.0", "5.0", "6.0", "7.0"};

  // The following defines the maximum file size we are currently allowing for raster.
  // Since this is for use with the thermometer, which only accepts integers, we will specify
  // the maxium size in MBytes
  private int _maxFileSize = 2048;

  // Some default values for the non-editable text fields
  private final int DEFAULT_SECS_ROW = 240;
  private final int DEFAULT_SECS_MAP = 3600;

  // Global flag indicating whether we are using acsis or das
  boolean _isAcsis = true ;


  /**
   * The constructor initializes the title, description, and presentation source.
   */
	public EdIterRasterObs()
	{
		super( new IterRasterObsGUI() );

		_title = "Scan/Raster";
		_presSource = _w = ( IterRasterObsGUI ) super._w;
		_description = "Scan/Raster Map";

		ButtonGroup grp = new ButtonGroup();
		grp.add( _w.alongRow );
		grp.add( _w.interleaved );

		_w.alongRow.setActionCommand( SpIterRasterObs.RASTER_MODE_ALONG_ROW );
		_w.interleaved.setActionCommand( SpIterRasterObs.RASTER_MODE_INTERLEAVED );

		_w.scanAngle.setChoices( SCAN_PA_CHOICES );
		_w.scanSystem.setChoices( SpJCMTConstants.SCAN_SYSTEMS );
		_w.sampleTime.setChoices( SAMPLE_TIME_CHOICES ) ;
		_w.thermometer.setMaximum( _maxFileSize );

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
		_w.alongRow.addWatcher( this );
		_w.interleaved.addWatcher( this );
		_w.rowReversal.addWatcher( this );
		_w.scanSystem.addWatcher( this );
		_w.scanAngle.addWatcher( this );
		_w.defaultButton.addWatcher( this );
		_w.scanAngle.getEditor().getEditorComponent().addKeyListener( this );

		_w.frequencyPanel.setVisible( false );
	}

  /**
	 * Override setup to store away a reference to the Raster Iterator.
	 */
  public void setup(SpItem spItem) {
    _iterObs = (SpIterRasterObs) spItem;

    super.setup(spItem);
    _iterObs.getAvEditFSM().addObserver(this);
  }

	protected void _updateWidgets()
	{
		super._updateWidgets();
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
			{
				scanAngleString += ", " + _iterObs.getScanAngle( i );
			}

			_w.scanAngle.setEditable( true );
			_w.scanAngle.setValue( scanAngleString.substring( 2 ) );
		}

		_w.scanSystem.setValue( _iterObs.getScanSystem() );
		_w.switchingMode.setValue( SpJCMTConstants.SWITCHING_MODE_BEAM );
		_w.switchingMode.setEnabled( false );
		_w.rowReversal.setValue( _iterObs.getRowReversal() );
		if( !_isAcsis )
			_w.sampleTime.setValue( ( int ) _iterObs.getSampleTime() - SAMPLE_TIME_CHOICES.length );
		else
			_w.acsisSampleTime.setValue( _iterObs.getSampleTime() );

		if( SpIterRasterObs.RASTER_MODE_ALONG_ROW.equals( _iterObs.getRasterMode() ) )
			_w.alongRow.setSelected( true );
		else
			_w.interleaved.setSelected( true );

		updateTimes();
		updateThermometer();
	}

	public void textBoxKeyPress( TextBoxWidgetExt tbwe )
	{
		_iterObs.getAvEditFSM().deleteObserver( this );

		if( tbwe == _w.dx )
		{
			_iterObs.setScanDx( _w.dx.getValue() );
			if( !( _w.dx.getValue().equals( "" ) ) )
				_w.noiseTextBox.setValue( calculateNoise() );
		}
		else if( tbwe == _w.dy )
		{
			_iterObs.setScanDy( _w.dy.getValue() );
			if( !( _w.dy.getValue().equals( "" ) ) )
				_w.noiseTextBox.setValue( calculateNoise() );
		}
		else if( tbwe == _w.width )
		{
			_iterObs.setWidth( _w.width.getValue() );

			if( !( _w.width.getValue().equals( "" ) ) )
				_w.noiseTextBox.setValue( calculateNoise() );

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
			String sampleTime = "0.1" ;
			try
			{
				sampleTime = _w.acsisSampleTime.getValue() ;
				Double conversionDouble = new Double( sampleTime ) ;
				if( conversionDouble.doubleValue() > 0.1 )
					sampleTime = conversionDouble.toString() ;
				else
					sampleTime = "0.1" ;
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
			_iterObs.setScanSystem( SpJCMTConstants.SCAN_SYSTEMS[ index ] );
			return;
		}

		if( ddlbwe == _w.scanAngle )
		{
			if( _w.scanAngle.getValue().equals( SCAN_PA_CHOICES[ 0 ] ) )
			{
				_w.scanAngle.setEditable( false );

				_iterObs.setScanAngles( null );
			}

			if( _w.scanAngle.getValue().equals( SCAN_PA_CHOICES[ 1 ] ) )
			{
				_w.scanAngle.setEditable( true );
				_w.scanAngle.setValue( "" );

				_iterObs.setScanSystem( _w.scanSystem.getStringValue() );
			}

			return;
		}

		if( ddlbwe == _w.sampleTime )
		{
			_iterObs.setSampleTime( _w.sampleTime.getStringValue() );
			_w.noiseTextBox.setValue( calculateNoise() );
		}
		
		if( ddlbwe == _w.scanSystem )
		{
			_iterObs.setScanAngles( _w.scanSystem.getStringValue() );

			return;
		}
		
		super.dropDownListBoxAction( ddlbwe , index , val );
		updateTimes();
		updateThermometer();

		_iterObs.getAvEditFSM().addObserver( this );
	}


  public void keyPressed(java.awt.event.KeyEvent e)  { }

  public void keyReleased(java.awt.event.KeyEvent e) {
    _iterObs.getAvEditFSM().deleteObserver(this);

     if(e.getSource() == _w.scanAngle.getEditor().getEditorComponent()) {
      _iterObs.setScanAngles(((JTextField)_w.scanAngle.getEditor().getEditorComponent()).getText());      
    }

    _iterObs.getAvEditFSM().addObserver(this);
  }

  public void keyTyped(java.awt.event.KeyEvent e) { }


  public void optionAction(OptionWidgetExt owe) {
    _iterObs.getAvEditFSM().deleteObserver(this);
    _iterObs.setRasterMode(owe.getActionCommand());
    _iterObs.getAvEditFSM().addObserver(this);
  }

  	public void checkBoxAction( CheckBoxWidgetExt cbwe )
	{
		if( cbwe == _w.rowReversal )
			_iterObs.setRowReversal( _w.rowReversal.getBooleanValue() );
	}

  public void commandButtonAction ( CommandButtonWidgetExt cbwe ) {
      if ( cbwe == _w.defaultButton ) {
          _iterObs.setDefaults();
      }
      _updateWidgets();
  }
	public void setInstrument( SpInstObsComp spInstObsComp )
	{
		if( ( spInstObsComp != null ) && ( spInstObsComp instanceof SpInstHeterodyne ) )
		{
			_w.heterodynePanel.setVisible( true );
			String defaultScanSystem = ( String )_w.scanSystem.getValue() ;
			_iterObs.setScanSystem( defaultScanSystem ) ;
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

  public void update(Observable o, Object arg) {
    _updateWidgets();
  }

  	protected double calculateNoise( int integrations , double wavelength , double nefd , int[] status )
	{
		return ScubaNoise.noise_level( integrations , wavelength , "SCAN" , nefd , status , _iterObs.getHeight() , _iterObs.getWidth() );
	}

    protected double calculateNoise( SpInstHeterodyne inst , double airmass , double tau )
	{
		// System.out.println("Calculating Raster specific heterodyne noise");
		double tSys = HeterodyneNoise.getTsys( inst.getFrontEnd() , tau , airmass , inst.getRestFrequency( 0 ) / 1.0e9 , inst.getMode().equalsIgnoreCase( "ssb" ) );

		_noiseToolTip = "airmass = " + ( Math.rint( airmass * 10 ) / 10 ) + ", Tsys = " + ( Math.rint( tSys * 10 ) / 10 );
		if( "acsis".equalsIgnoreCase( inst.getBackEnd() ) )
			return MathUtil.round( HeterodyneNoise.getHeterodyneNoise( _iterObs , inst , tau , airmass ) , 3 ) ;
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
			{
				_w.secsPerRow.setForeground( safeColor );
			}
			else if( secsPerRow < 2 * DEFAULT_SECS_ROW )
			{
				_w.secsPerRow.setForeground( warnColor );
			}
			else
			{
				_w.secsPerRow.setForeground( errColor );
			}
			_w.secsPerRow.setValue( formatter.format( secsPerRow ) );

			double obsTime = _iterObs.getElapsedTime();
			if( obsTime <= DEFAULT_SECS_MAP )
			{
				_w.secsPerObservation.setForeground( safeColor );
			}
			else if( obsTime < 2 * DEFAULT_SECS_MAP )
			{
				_w.secsPerObservation.setForeground( warnColor );
			}
			else
			{
				_w.secsPerObservation.setForeground( errColor );
			}
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
			{
				return;
			}

			// Get the number of channels
			int maxChannels = 0;
			for( int i = 0 ; i < ( ( SpInstHeterodyne ) inst ).getNumSubSystems() ; i++ )
			{
				if( ( ( SpInstHeterodyne ) inst ).getChannels( i ) > maxChannels )
				{
					maxChannels = ( ( SpInstHeterodyne ) inst ).getChannels( i );
				}
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
						recipe = ( SpDRRecipe ) drRecipeCompts.get( 0 );
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
						recipe = ( SpDRRecipe ) drRecipeCompts.get( 0 );
						break;
					}
				}
				parent = parent.parent();
			}
			maxChannels -= ( 2 * truncChannels );

			int samplesPerRow = ( int ) ( _iterObs.getWidth() / _iterObs.getScanDx() );
			int numberOfRows = ( int ) ( _iterObs.getHeight() / _iterObs.getScanDy() );

			int fileSize = ( int ) ( ( maxChannels * samplesPerRow * numberOfRows * 4 ) / ( 1024 * 1024 ) );
			_w.thermometer.setExtent( fileSize );
		}
	}

}

