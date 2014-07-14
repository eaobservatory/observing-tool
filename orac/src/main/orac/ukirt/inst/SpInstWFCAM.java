/*
 * Copyright 2003 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

// This version started 2003-Mar-31 based on SpInstUIST
// author: Alan Pickup = dap@roe.ac.uk         2003-Mar
package orac.ukirt.inst ;

import java.io.IOException ;
import java.util.Hashtable ;

import orac.util.LookUpTable ;
import orac.util.InstCfg ;
import orac.util.InstCfgReader ;

import gemini.util.MathUtil ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;

import gemini.sp.obsComp.SpChopCapability ;
import gemini.sp.obsComp.SpStareCapability ;
import gemini.sp.obsComp.SpMicroStepUser ;

import gemini.sp.iter.SpIterConfigObs ;
import orac.ukirt.iter.SpIterBiasObs ;
import orac.ukirt.iter.SpIterDarkObs ;
import orac.ukirt.iter.SpIterWFCAMCalObs ;

/**
 * The WFCAM instrument.
 *
 * @author Alan Pickup
 */
@SuppressWarnings( "serial" )
public final class SpInstWFCAM extends SpUKIRTInstObsComp implements SpMicroStepUser
{
	/** Width and height of one IR detector in arcsecs. */
	public static final double DETECTOR_SIZE = 817.2 ;

	/**
	 * Percentage of DETECTOR_SIZE that makes up the gap
	 * between adjacent IR detectors in arcsecs.
	 */
	public static final double DETECTOR_SPACING = 94. ;

	// Attributes presented to user
	public static String ATTR_CONFIG_TYPE = "configType" ;
	public static String ATTR_READMODE = "readMode" ;
	public static String ATTR_FILTER = "filter" ;
	public static String NO_VALUE = "none" ;

	// Class variables representing defaults, LUTs, etc
	public static String CONFIG_TYPE ;
	public static String VERSION ;
	public static String[] READMODES ;
	public static String DEFAULT_READMODE ;
	public static LookUpTable FILTERS ;
	public static String[][] MICROSTEP_PATTERNS ;
	public static String DEFAULT_FILTER ;
	public static double DEFAULT_EXPTIME ;
	public static double DEFAULT_FLAT_EXPTIME ;
	public static double DEFAULT_FOCUS_EXPTIME ;
	public static double DEFAULT_POSANGLE ;
	public static int DEFAULT_COADDS ;
	public static int DEFAULT_FLAT_COADDS ;
	public static int DEFAULT_FOCUS_COADDS ;

	public static String[] INSTRUMENT_APER ; // Array of inst aper values

	public static final SpType SP_TYPE = SpType.create( SpType.OBSERVATION_COMPONENT_TYPE , "inst.WFCAM" , "WFCAM" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpInstWFCAM() ) ;
	}

	// Constructor reads instrument .cfg file and initialises values
	public SpInstWFCAM()
	{
		super( SP_TYPE ) ;

		addCapability( new SpChopCapability() ) ;
		addCapability( new SpStareCapability() ) ;

		// Read in the instrument config file
		String baseDir = System.getProperty( "ot.cfgdir" ) ;
		if( !baseDir.endsWith( "/" ) )
			baseDir += '/' ;
		String cfgFile = baseDir + "wfcam.cfg" ;
		_readCfgFile( cfgFile ) ;

		// Set the initial values of the attributes
		String attr = ATTR_CONFIG_TYPE ;
		String value = CONFIG_TYPE ;
		_avTable.noNotifySet( attr , value , 0 ) ;

		attr = ATTR_VERSION ;
		value = VERSION ;
		_avTable.noNotifySet( attr , value , 0 ) ;

		attr = ATTR_READMODE ;
		value = DEFAULT_READMODE ;
		_avTable.noNotifySet( attr , value , 0 ) ;

		attr = ATTR_FILTER ;
		value = DEFAULT_FILTER ;
		_avTable.noNotifySet( attr , value , 0 ) ;

		attr = ATTR_EXPOSURE_TIME ;
		value = Double.toString( DEFAULT_EXPTIME ) ;
		_avTable.noNotifySet( attr , value , 0 ) ;

		setCoadds( DEFAULT_COADDS ) ;

		// Initialise instance variables
		initInstance() ;

	}

	private void _readCfgFile( String filename )
	{
		InstCfgReader instCfg = null ;
		InstCfg instInfo = null ;
		String block = null ;
		instCfg = new InstCfgReader( filename ) ;
		try
		{
			while( ( block = instCfg.readBlock() ) != null )
			{
				instInfo = new InstCfg( block ) ;
				if( InstCfg.matchAttr( instInfo , "config_type" ) )
					CONFIG_TYPE = instInfo.getValue() ;
				else if( InstCfg.matchAttr( instInfo , "version" ) )
					VERSION = instInfo.getValue() ;
				else if( InstCfg.matchAttr( instInfo , "default_posangle" ) )
					DEFAULT_POSANGLE = Double.valueOf( instInfo.getValue() ) ;
				else if( InstCfg.matchAttr( instInfo , "readmodes" ) )
					READMODES = instInfo.getValueAsArray() ;
				else if( InstCfg.matchAttr( instInfo , "default_readmode" ) )
					DEFAULT_READMODE = instInfo.getValue() ;
				else if( InstCfg.matchAttr( instInfo , "filters" ) )
					FILTERS = instInfo.getValueAsLUT() ;
				else if( InstCfg.matchAttr( instInfo , "microstep_patterns" ) )
					MICROSTEP_PATTERNS = instInfo.getValueAs2DArray() ;
				else if( InstCfg.matchAttr( instInfo , "default_filter" ) )
					DEFAULT_FILTER = instInfo.getValue() ;
				else if( InstCfg.matchAttr( instInfo , "default_exptime" ) )
					DEFAULT_EXPTIME = Double.valueOf( instInfo.getValue() ) ;
				else if( InstCfg.matchAttr( instInfo , "default_flat_exptime" ) )
					DEFAULT_FLAT_EXPTIME = Double.valueOf( instInfo.getValue() ) ;
				else if( InstCfg.matchAttr( instInfo , "default_focus_exptime" ) )
					DEFAULT_FOCUS_EXPTIME = Double.valueOf( instInfo.getValue() ) ;
				else if( InstCfg.matchAttr( instInfo , "default_coadds" ) )
					DEFAULT_COADDS = Integer.valueOf( instInfo.getValue() ) ;
				else if( InstCfg.matchAttr( instInfo , "default_flat_coadds" ) )
					DEFAULT_FLAT_COADDS = Integer.valueOf( instInfo.getValue() ) ;
				else if( InstCfg.matchAttr( instInfo , "default_focus_coadds" ) )
					DEFAULT_FOCUS_COADDS = Integer.valueOf( instInfo.getValue() ) ;	
				else if( instInfo.getKeyword().equalsIgnoreCase( "instrument_aper" ) )
					INSTRUMENT_APER = instInfo.getValueAsArray() ;
				else
					System.out.println( "Unmatched keyword:" + instInfo.getKeyword() ) ;
			}
		}
		catch( IOException e )
		{
			System.out.println( "Error reading WFCAM inst. cfg file" ) ;
		}
	}

	/**
	 * Get the chop capability.
	 */
	public SpChopCapability getChopCapability()
	{
		return ( SpChopCapability )getCapability( SpChopCapability.CAPABILITY_NAME ) ;
	}

	/**
	 * Get the stare capability.
	 */
	public SpStareCapability getStareCapability()
	{
		return ( SpStareCapability )getCapability( SpStareCapability.CAPABILITY_NAME ) ;
	}

	/**
	 * Initialise instance variables
	 */
	public void initInstance(){}

	/**
	 * Override the set-position-angle methods so that we can ensure
	 * it's set to zero: stops dragging of the angle in tpe.
	 * Set the position angle in degrees from due north, updating the
	 * observation data with the new position angle.  This method is
	 * ultimately called by the other setPosAngle methods.
	 */
	public void setPosAngleDegrees( double posAngle )
	{
		super.setPosAngleDegrees( posAngle ) ;
	}

	/**
	 * Set the rotation of the science area as a string (representing degrees).
	 */
	public void setPosAngleDegreesStr( String posAngleStr )
	{
		double posAngle = 0. ;
		try
		{
			posAngle = Double.valueOf( posAngleStr ) ;
		}
		catch( NumberFormatException e )
		{
			System.out.println( "Error converting string angle to double." ) ;
		}
		this.setPosAngleDegrees( posAngle ) ;
	}

	/**
	 * Use default filter
	 */
	public void useDefaultFilter()
	{
		_avTable.rm( ATTR_FILTER ) ;
	}

	/**
	 * Set the filter.
	 */
	public void setFilter( String filter )
	{
		_avTable.set( ATTR_FILTER , filter ) ;
	}

	/**
	 * Get the filter
	 */
	public String getFilter()
	{
		String filter = _avTable.get( ATTR_FILTER ) ;
		if( filter == null )
		{
			filter = DEFAULT_FILTER ;
			setFilter( filter ) ;
		}
		return filter ;
	}

	public String[] getFilterList()
	{
		int nfilters = FILTERS.getNumRows() ;
		String filterList[] = new String[ nfilters ] ;
		for( int i = 0 ; i < nfilters ; i++ )
			filterList[ i ] = FILTERS.elementAt( i , 0 ) ;

		return filterList ;
	}

	/**
	 * Use default readMode
	 */
	public void useDefaultReadMode()
	{
		_avTable.rm( ATTR_READMODE ) ;
	}

	/**
	 * Set the readMode
	 */
	public void setReadMode( String readMode )
	{
		_avTable.set( ATTR_READMODE , readMode ) ;
	}

	/**
	 * Get the readMode
	 */
	public String getReadMode()
	{
		String readMode = _avTable.get( ATTR_READMODE ) ;
		if( readMode == null )
		{
			readMode = DEFAULT_READMODE ;
			setReadMode( readMode ) ;
		}
		return readMode ;
	}

	public String[] getReadModeList()
	{
		return READMODES ;
	}

	/**
	 * Get the exposure time in seconds as a String
	 */
	public String getExposureTimeString()
	{
		String ets = Double.toString( MathUtil.round( getExposureTime() , 3 ) ) ;
		return ets ;
	}

	/**
	 * Get the default flat exposure time in seconds
	 */
	public double getDefaultFlatExpTime()
	{
		return DEFAULT_FLAT_EXPTIME ;
	}

	/**
	 * Get the default number of flat coadds
	 */
	public int getDefaultFlatCoadds()
	{
		return DEFAULT_FLAT_COADDS ;
	}

	/**
	 * Get the default focus exposure time in seconds
	 */
	public double getDefaultFocusExpTime()
	{
		return DEFAULT_FOCUS_EXPTIME ;
	}

	/**
	 * Get the default number of focus coadds
	 */
	public int getDefaultFocusCoadds()
	{
		return DEFAULT_FOCUS_COADDS ;
	}

	/**
	 * Get the coadds.
	 */
	public String getCoaddsString()
	{
		int coadds = getStareCapability().getCoadds() ;
		return Integer.toString( coadds ) ;
	}

	/**
	 * Set coadds
	 */
	public void setCoadds( int coadds )
	{
		getStareCapability().setCoadds( coadds ) ;
	}

	/**
	 * Set coadds as a string
	 */
	public void setCoadds( String coadds )
	{
		int c = 0 ;
		try
		{
			c = Integer.valueOf( coadds ) ;
		}
		catch( Exception ex ){}

		setCoadds( c ) ;
	}

	public double getExposureOverhead()
	{
		return 1. ;
	}

	public Hashtable<String,double[][]> getMicroStepPatterns()
	{
		Hashtable<String,double[][]> result = new Hashtable<String,double[][]>() ;

		double[][] offsets ;

		for( int i = 0 ; i < MICROSTEP_PATTERNS.length ; i++ )
		{
			offsets = new double[ ( MICROSTEP_PATTERNS[ i ].length - 1 ) / 2 ][ 2 ] ;

			int k = 1 ;
			for( int j = 0 ; j < offsets.length ; j++ )
			{
				offsets[ j ][ 0 ] = Double.parseDouble( MICROSTEP_PATTERNS[ i ][ k++ ] ) ;
				offsets[ j ][ 1 ] = Double.parseDouble( MICROSTEP_PATTERNS[ i ][ k++ ] ) ;
			}

			result.put( MICROSTEP_PATTERNS[ i ][ 0 ] , offsets ) ;
		}

		return result ;
	}

	/**
	 * Returns footprints for the four IR detectors.
	 *
	 * The return value of type double [] has the following format:
	 * { width, height, xoffset, yoffset } in arcsecs.
	 *
	 * This will result in the following four rectangular areas:
	 *
	 * <pre>
	 *    xoffset + (0.5 * width),  yoffset + (0.5 * height)
	 *    xoffset + (0.5 * width),  yoffset - (0.5 * height)
	 *    xoffset - (0.5 * width),  yoffset - (0.5 * height)
	 *    xoffset - (0.5 * width),  yoffset + (0.5 * height)
	 *
	 *    xoffset + (0.5 * width), -yoffset + (0.5 * height)
	 *    xoffset + (0.5 * width), -yoffset - (0.5 * height)
	 *    xoffset - (0.5 * width), -yoffset - (0.5 * height)
	 *    xoffset - (0.5 * width), -yoffset + (0.5 * height)
	 *
	 *   -xoffset + (0.5 * width), -yoffset + (0.5 * height)
	 *   -xoffset + (0.5 * width), -yoffset - (0.5 * height)
	 *   -xoffset - (0.5 * width), -yoffset - (0.5 * height)
	 *   -xoffset - (0.5 * width), -yoffset + (0.5 * height)
	 *
	 *   -xoffset + (0.5 * width),  yoffset + (0.5 * height)
	 *   -xoffset + (0.5 * width),  yoffset - (0.5 * height)
	 *   -xoffset - (0.5 * width),  yoffset - (0.5 * height)
	 *   -xoffset - (0.5 * width),  yoffset + (0.5 * height)
	 * </pre>
	 */
	public double[] getScienceArea()
	{
		return new double[] 
		{ 
				DETECTOR_SIZE , 
				DETECTOR_SIZE , 
				( DETECTOR_SIZE * ( 1. + ( DETECTOR_SPACING / 100. ) ) ) / 2. , 
				( DETECTOR_SIZE * ( 1. + ( DETECTOR_SPACING / 100. ) ) ) / 2. 
		} ;
	}

	public void setInstAper()
	{
		setInstApX( INSTRUMENT_APER[ XAP_INDEX ] ) ;
		setInstApY( INSTRUMENT_APER[ YAP_INDEX ] ) ;
		setInstApZ( INSTRUMENT_APER[ ZAP_INDEX ] ) ;
		setInstApL( INSTRUMENT_APER[ LAP_INDEX ] ) ;
	}

	public Hashtable<String,String> getConfigItems()
	{
		Hashtable<String,String> t = new Hashtable<String,String>() ;

		t.put( "instrument" , "WFCAM" ) ;
		t.put( "version" , "1" ) ;
		t.put( "configType" , "Normal" ) ;
		t.put( "type" , "object" ) ;
		t.put( "filter" , getFilter() ) ;
		t.put( "instPort" , "Centre" ) ;
		t.put( "readMode" , getReadMode() ) ;
		t.put( "exposureTime" , "" + getExposureTime() ) ;
		t.put( "coadds" , "" + getCoadds() ) ;
		setInstAper() ;
		t.put( "instAperX" , "" + getInstApX() ) ;
		t.put( "instAperY" , "" + getInstApY() ) ;
		t.put( "instAperZ" , "" + getInstApZ() ) ;
		t.put( "instAperL" , "" + getInstApL() ) ;

		return t ;
	}

	/**
	 * Iteration Tracker for WFCAM
	 *
	 * Added for OMP by FE, Aug 2005
	 */
	private class IterTrackerWFCAM extends IterTrackerUKIRT
	{
		public double getObserveStepTime()
		{
			// extra_oh is a constant overheads related to certain observe iterators:
			// 30 seconds for dark, arc, flat or bias (in addition to the times to do
			// their respective eye), and 30 secs each time an instrument iterator changes a filter.  
			double extra_oh = 0. ;
			_obs_oh = 2.5 ;
			_int_oh = 0. ;

			if( ( currentIterStepItem != null ) && ( ( currentIterStepItem instanceof SpIterBiasObs ) || ( currentIterStepItem instanceof SpIterDarkObs ) || ( currentIterStepItem instanceof SpIterWFCAMCalObs ) || ( currentIterStepItem instanceof SpIterConfigObs ) ) )
				extra_oh = 30. ;

			return( currentNoCoadds * ( currentExposureTime + getExposureOverhead() ) + _int_oh + _obs_oh + extra_oh ) ;
		}
	}

	public IterationTracker createIterationTracker()
	{
		return new IterTrackerWFCAM() ;
	}
}
