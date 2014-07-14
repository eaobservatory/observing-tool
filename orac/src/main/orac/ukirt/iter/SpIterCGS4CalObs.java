/*
 * Copyright 1999-2001 United Kingdom Astronomy Technology Centre, an
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

package orac.ukirt.iter ;

import java.util.Vector ;
import java.util.Hashtable ;
import java.io.IOException ;

import orac.util.LookUpTable ;
import orac.util.InstCfg ;
import orac.util.InstCfgReader ;
import orac.ukirt.inst.SpInstCGS4 ;
import orac.ukirt.inst.SpDRRecipe ;
import gemini.sp.SpFactory ;
import gemini.sp.SpType ;
import gemini.sp.SpItem ;
import gemini.sp.SpTreeMan ;
import gemini.sp.obsComp.SpInstObsComp ;

import gemini.sp.SpMSB ;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;

import gemini.sp.iter.SpIterEnumeration ;
import gemini.sp.iter.SpIterObserveBase ;
import gemini.sp.iter.SpIterStep ;
import gemini.sp.iter.SpIterValue ;

import gemini.util.ConfigWriter ;

@SuppressWarnings( "serial" )
class SpIterCGS4CalObsEnumeration extends SpIterEnumeration
{
	private int _curCount = 0 ;
	private int _maxCount ;
	private String _calType ;
	private SpIterValue[] _values ;

	SpIterCGS4CalObsEnumeration( SpIterCGS4CalObs iterObserve )
	{
		super( iterObserve ) ;
		_maxCount = iterObserve.getCount() ;
		_calType = iterObserve.getCalTypeString() ;
	}

	protected boolean _thisHasMoreElements()
	{
		return( _curCount < _maxCount ) ;
	}

	protected SpIterStep _thisFirstElement()
	{
		SpIterCGS4CalObs ico = ( SpIterCGS4CalObs )_iterComp ;
		_values = new SpIterValue[ 8 ] ;

		_values[ 0 ] = new SpIterValue( SpCGS4CalUnitConstants.ATTR_LAMP , ico.getLamp() ) ;
		_values[ 1 ] = new SpIterValue( SpCGS4CalUnitConstants.ATTR_FILTER , ico.getFilter() ) ;
		_values[ 2 ] = new SpIterValue( SpCGS4CalUnitConstants.ATTR_MODE , ico.getMode() ) ;
		_values[ 3 ] = new SpIterValue( SpCGS4CalUnitConstants.ATTR_EXPOSURE_TIME , String.valueOf( ico.getExposureTime() ) ) ;

		_values[ 4 ] = new SpIterValue( SpCGS4CalUnitConstants.ATTR_COADDS , String.valueOf( ico.getCoadds() ) ) ;

		_values[ 5 ] = new SpIterValue( SpCGS4CalUnitConstants.ATTR_CVF_WAVELENGTH , String.valueOf( ico.getCvfWavelength() ) ) ;
		_values[ 6 ] = new SpIterValue( SpCGS4CalUnitConstants.ATTR_FLAT_SAMPLING , String.valueOf( ico.getFlatSampling() ) ) ;
		_values[ 7 ] = new SpIterValue( SpCGS4CalUnitConstants.ATTR_NEUTRAL_DENSITY , String.valueOf( ico.getNdFilter() ) ) ;

		return _thisNextElement() ;
	}

	protected SpIterStep _thisNextElement()
	{
		return new SpIterStep( _calType , _curCount++ , _iterComp , _values ) ;
	}

}

/**
 * Iterator for Cal Unit observes (FLAT and ARC).
 */
@SuppressWarnings( "serial" )
public class SpIterCGS4CalObs extends SpIterObserveBase implements SpTranslatable
{

	/** Identifier for a FLAT calibration. */
	public static final int FLAT = 0 ;

	/** Identifier for an ARC calibration. */
	public static final int ARC = 1 ;
	public static String[] MODES ;
	public static String[] FILTERS ;
	public static String[] FLAT_LAMPS ;
	public static String[] ARC_LAMPS ;
	public static String DEFAULT_MODE ;
	public static String DEFAULT_COADDS ;
	public static String DEFAULT_EXPTIME ;
	public static LookUpTable ARCLAMPS1 ;
	public static LookUpTable ARCLAMPS2 ;
	public static LookUpTable ARCLAMPS3 ;
	public static LookUpTable FLATLAMPS1 ;
	public static LookUpTable FLATLAMPS2 ;
	public static LookUpTable FLATLAMPS3 ;
	public static LookUpTable ARCFILTS1 ;
	public static LookUpTable ARCFILTS2 ;
	public static LookUpTable ARCFILTS3 ;
	
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "CGS4calUnitObs" , "CGS4 Cal Unit Observe" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterCGS4CalObs() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterCGS4CalObs()
	{
		super( SP_TYPE ) ;

		// Read in the instrument config file
		String baseDir = System.getProperty( "ot.cfgdir" ) ;
		if( !baseDir.endsWith( "/" ) )
			baseDir += '/' ;
		String cfgFile = baseDir + "cgs4calunit.cfg" ;
		_readCfgFile( cfgFile ) ;

		String defLamp = ARC_LAMPS[ 0 ] ;
		String defFilter = FILTERS[ 0 ] ;

		_avTable.noNotifySet( SpCGS4CalUnitConstants.ATTR_CALTYPE , "Arc" , 0 ) ;
		_avTable.noNotifySet( SpCGS4CalUnitConstants.ATTR_LAMP , defLamp , 0 ) ;
		_avTable.noNotifySet( SpCGS4CalUnitConstants.ATTR_FILTER , defFilter , 0 ) ;
		_avTable.noNotifySet( SpCGS4CalUnitConstants.ATTR_MODE , DEFAULT_MODE , 0 ) ;
		_avTable.noNotifySet( SpCGS4CalUnitConstants.ATTR_EXPOSURE_TIME , null , 0 ) ;
		_avTable.noNotifySet( SpCGS4CalUnitConstants.ATTR_COADDS , DEFAULT_COADDS , 0 ) ;
		_avTable.noNotifySet( SpCGS4CalUnitConstants.ATTR_CVF_WAVELENGTH , "0.0" , 0 ) ;
		_avTable.noNotifySet( SpCGS4CalUnitConstants.ATTR_FLAT_SAMPLING , "AS_OBJECT" , 0 ) ;
		_avTable.noNotifySet( SpCGS4CalUnitConstants.ATTR_NEUTRAL_DENSITY , null , 0 ) ;
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
				if( instInfo.getKeyword().equalsIgnoreCase( "modes" ) )
					MODES = instInfo.getValueAsArray() ;
				else if( instInfo.getKeyword().equalsIgnoreCase( "flat_lamps" ) )
					FLAT_LAMPS = instInfo.getValueAsArray() ;
				else if( instInfo.getKeyword().equalsIgnoreCase( "arc_lamps" ) )
					ARC_LAMPS = instInfo.getValueAsArray() ;
				else if( instInfo.getKeyword().equalsIgnoreCase( "40lpmm_arclamps" ) )
					ARCLAMPS1 = instInfo.getValueAsLUT() ;
				else if( instInfo.getKeyword().equalsIgnoreCase( "echelle_arclamps" ) )
					ARCLAMPS2 = instInfo.getValueAsLUT() ;
				else if( instInfo.getKeyword().equalsIgnoreCase( "40lpmm_arcfilters" ) )
					ARCFILTS1 = instInfo.getValueAsLUT() ;
				else if( instInfo.getKeyword().equalsIgnoreCase( "echelle_arcfilters" ) )
					ARCFILTS2 = instInfo.getValueAsLUT() ;
				else if( instInfo.getKeyword().equalsIgnoreCase( "40lpmm_flatlamps" ) )
					FLATLAMPS1 = instInfo.getValueAsLUT() ;
				else if( instInfo.getKeyword().equalsIgnoreCase( "echelle_flatlamps" ) )
					FLATLAMPS2 = instInfo.getValueAsLUT() ;
				else if( instInfo.getKeyword().equalsIgnoreCase( "filters" ) )
					FILTERS = instInfo.getValueAsArray() ;
				else if( instInfo.getKeyword().equalsIgnoreCase( "default_mode" ) )
					DEFAULT_MODE = instInfo.getValue() ;
				else if( instInfo.getKeyword().equalsIgnoreCase( "default_coadds" ) )
					DEFAULT_COADDS = instInfo.getValue() ;
				else if( instInfo.getKeyword().equalsIgnoreCase( "default_exptime" ) )
					DEFAULT_EXPTIME = instInfo.getValue() ;
			}
		}
		catch( IOException e )
		{
			System.out.println( "Error reading CGS4 cal. unit cfg file" ) ;
		}
	}

	/**
	 * Override getTitle to return the observe type and the count.
	 */
	public String getTitle()
	{
		if( getTitleAttr() != null )
			return super.getTitle() ;

		return getCalTypeString() + " (" + getCount() + "X)" ;
	}

	/**
	 */
	public SpIterEnumeration elements()
	{
		return new SpIterCGS4CalObsEnumeration( this ) ;
	}

	/**
	 * Get the instrument item in the scope of the base item.
	 */
	public SpInstObsComp getInstrumentItem()
	{
		SpItem _baseItem = parent() ;
		return SpTreeMan.findInstrument( _baseItem ) ;
	}

	/**
	 * Override getExposureTime to provide a default if required.
	 */
	public double getExposureTime()
	{
		double et = _avTable.getDouble( SpCGS4CalUnitConstants.ATTR_EXPOSURE_TIME , 0. ) ;
		if( et == 0. )
		{
			et = getDefaultExposureTime() ;
			setExposureTime( Double.toString( et ) ) ;
		}
		return et ;

	}

	/**
	 * Provide a default exposure time.
	 */
	public double getDefaultExposureTime()
	{
		String etstr = null ;
		double et = 0. ;
		double sw = 1. ;

		// Get the exposure time from the default lamp table.
		if( getCalType() == FLAT )
		{
			SpInstCGS4 inst = ( SpInstCGS4 )getInstrumentItem() ;
			if( inst != null )
			{
				try
				{
					int instdi = inst.getDisperserIndex() ;
					double instcwl = inst.getCentralWavelength() ;
					sw = inst.getMaskWidth() ;
					if( instdi == 0 )
					{
						int pos = FLATLAMPS1.rangeInColumn( instcwl , 0 ) ;
						etstr = FLATLAMPS1.elementAt( pos , 2 ) ;
					}
					else if( instdi == 1 )
					{
						int pos = FLATLAMPS2.rangeInColumn( instcwl , 0 ) ;
						etstr = FLATLAMPS2.elementAt( pos , 2 ) ;
					}
					else if( instdi == 2 )
					{
						int pos = FLATLAMPS3.rangeInColumn( instcwl , 0 ) ;
						etstr = FLATLAMPS3.elementAt( pos , 2 ) ;
					}
				}
				catch( Exception ex )
				{
					etstr = DEFAULT_EXPTIME ;
				}
			}
			else
			{
				etstr = DEFAULT_EXPTIME ;
			}
		}
		else
		{
			SpInstCGS4 inst = ( SpInstCGS4 )getInstrumentItem() ;
			if( inst != null )
			{
				try
				{
					int instdi = inst.getDisperserIndex() ;
					double instcwl = inst.getCentralWavelength() ;
					sw = inst.getMaskWidth() ;
					if( instdi == 0 )
					{
						int pos = ARCLAMPS1.rangeInColumn( instcwl , 0 ) ;
						etstr = ARCLAMPS1.elementAt( pos , 2 ) ;
					}
					else if( instdi == 1 )
					{
						int pos = ARCLAMPS2.rangeInColumn( instcwl , 0 ) ;
						etstr = ARCLAMPS2.elementAt( pos , 2 ) ;
					}
					else if( instdi == 2 )
					{
						int pos = ARCLAMPS3.rangeInColumn( instcwl , 0 ) ;
						etstr = ARCLAMPS3.elementAt( pos , 2 ) ;
					}
				}
				catch( Exception ex )
				{
					etstr = DEFAULT_EXPTIME ;
				}
			}
			else
			{
				etstr = DEFAULT_EXPTIME ;
			}
		}
		// Convert to double and divide by slit width
		try
		{
			et = Double.valueOf( etstr ) / sw ;
		}
		catch( Exception ex ){}

		return et ;
	}

	/**
	 * Get the lamp.
	 */
	public String getLamp()
	{
		String lamp = _avTable.get( SpCGS4CalUnitConstants.ATTR_LAMP ) ;
		// if null get a default lamp
		if( lamp == null || lamp.equals( "none" ) )
		{
			lamp = getDefaultLamp() ;
			setLamp( lamp ) ;
		}
		return lamp ;
	}

	/**
	 * Get a default value for the lamp - need instrument disperser & wavelength
	 */
	public String getDefaultLamp()
	{
		String lamp = null ;

		if( getCalType() == FLAT )
		{
			SpInstCGS4 inst = ( SpInstCGS4 )getInstrumentItem() ;
			if( inst != null )
			{
				try
				{
					int instdi = inst.getDisperserIndex() ;
					double instcwl = inst.getCentralWavelength() ;
					if( instdi == 0 )
					{
						int pos = FLATLAMPS1.rangeInColumn( instcwl , 0 ) ;
						lamp = FLATLAMPS1.elementAt( pos , 1 ) ;
					}
					else if( instdi == 1 )
					{
						int pos = FLATLAMPS2.rangeInColumn( instcwl , 0 ) ;
						lamp = FLATLAMPS2.elementAt( pos , 1 ) ;
					}
					else if( instdi == 2 )
					{
						int pos = FLATLAMPS3.rangeInColumn( instcwl , 0 ) ;
						lamp = FLATLAMPS3.elementAt( pos , 1 ) ;
					}
				}
				catch( Exception ex )
				{
					lamp = FLAT_LAMPS[ 0 ] ;
				}
			}
			else
			{
				lamp = FLAT_LAMPS[ 0 ] ;
			}
		}
		else
		{
			SpInstCGS4 inst = ( SpInstCGS4 )getInstrumentItem() ;
			if( inst != null )
			{
				try
				{
					int instdi = inst.getDisperserIndex() ;
					double instcwl = inst.getCentralWavelength() ;
					if( instdi == 0 )
					{
						int pos = ARCLAMPS1.rangeInColumn( instcwl , 0 ) ;
						lamp = ARCLAMPS1.elementAt( pos , 1 ) ;
					}
					else if( instdi == 1 )
					{
						int pos = ARCLAMPS2.rangeInColumn( instcwl , 0 ) ;
						lamp = ARCLAMPS2.elementAt( pos , 1 ) ;
					}
					else if( instdi == 2 )
					{
						int pos = ARCLAMPS3.rangeInColumn( instcwl , 0 ) ;
						lamp = ARCLAMPS3.elementAt( pos , 1 ) ;
					}
				}
				catch( Exception ex )
				{
					lamp = ARC_LAMPS[ 0 ] ;
				}
			}
			else
			{
				lamp = ARC_LAMPS[ 0 ] ;
			}
		}

		// Set the ND filter too, just to make sure it happens.
		getNdFilter() ;
		return lamp ;
	}

	/**
	 * Set the lamp.
	 */
	public void setLamp( String lamp )
	{
		_avTable.set( SpCGS4CalUnitConstants.ATTR_LAMP , lamp ) ;
	}

	/**
	 * Get the type of calibration.
	 */
	public int getCalType()
	{
		String calType = _avTable.get( SpCGS4CalUnitConstants.ATTR_CALTYPE ) ;
		if( "Flat".equals( calType ) )
			return FLAT ;

		return ARC ;
	}

	/**
	 * Set the type of calibration.
	 */
	public void setCalType( String calType )
	{
		_avTable.set( SpCGS4CalUnitConstants.ATTR_CALTYPE , calType ) ;
	}

	/**
	 * Get the type of calibration as a String.
	 */
	public String getCalTypeString()
	{
		if( getCalType() == FLAT )
			return "Flat" ;
		
		return "Arc" ;
	}

	/**
	 * Get the CVF Wavelength
	 */
	public double getCvfWavelength()
	{
		double cwl = _avTable.getDouble( SpCGS4CalUnitConstants.ATTR_CVF_WAVELENGTH , 0. ) ;
		if( cwl < .5 )
		{
			cwl = getDefaultCvfWavelength() ;
			setCvfWavelength( cwl ) ;
		}
		return cwl ;
	}

	/**
	 * Get a default CVF Wavelength
	 */
	public double getDefaultCvfWavelength()
	{
		SpInstCGS4 inst = ( SpInstCGS4 )getInstrumentItem() ;
		inst.getCentralWavelength() ;
		double cvfoff = inst.getCvfOffset() ;
		return cvfoff ;
	}

	/**
	 * Set the cvf wavelength.
	 */
	public void setCvfWavelength( double cwl )
	{
		_avTable.set( SpCGS4CalUnitConstants.ATTR_CVF_WAVELENGTH , cwl ) ;
	}

	/**
	 * Set the cvf wavelength as a string.
	 */
	public void setCvfWavelength( String cwl )
	{
		double d = 0. ;
		try
		{
			d = Double.valueOf( cwl ) ;
		}
		catch( Exception ex ){}

		setCvfWavelength( d ) ;
	}

	/**
	 * Get the flat sampling choices. This is "1x1" and "AS_OBJECT"
	 */
	public String[] getFlatSamplingChoices()
	{
		String choices[] = new String[ 2 ] ;
		choices[ 0 ] = "1x1" ;
		choices[ 1 ] = "AS_OBJECT" ;
		return choices ;
	}

	/**
	 * Get the flat sampling
	 */
	public String getFlatSampling()
	{
		String sam = _avTable.get( SpCGS4CalUnitConstants.ATTR_FLAT_SAMPLING ) ;
		if( sam == null )
		{
			_avTable.noNotifySet( SpCGS4CalUnitConstants.ATTR_FLAT_SAMPLING , "AS_OBJECT" , 0 ) ;
			sam = "AS_OBJECT" ;
		}
		return sam ;
	}

	/**
	 * Set the flat sampling
	 */
	public void setFlatSampling( String sam )
	{
		_avTable.set( SpCGS4CalUnitConstants.ATTR_FLAT_SAMPLING , sam ) ;
	}

	/**
	 * Get the filter.
	 */
	public String getFilter()
	{
		String filter = _avTable.get( SpCGS4CalUnitConstants.ATTR_FILTER ) ;
		if( filter == null || filter.equals( "none" ) )
		{
			filter = getDefaultFilter() ;
			setFilter( filter ) ;
		}
		return filter ;
	}

	/**
	 * Get a default value for the filter
	 */
	public String getDefaultFilter()
	{
		String filter = null ;

		SpInstCGS4 inst = ( SpInstCGS4 )getInstrumentItem() ;

		if( inst != null )
		{
			int instdi = inst.getDisperserIndex() ;
			double instcwl = inst.getCentralWavelength() ;
			String instfilt = inst.getFilter() ;

			if( getCalType() == FLAT )
			{
				filter = instfilt ;
			}
			else
			{
				try
				{
					int pos = 0 ;
					switch( instdi )
					{
						case 0 :
							pos = ARCFILTS1.rangeInColumn( instcwl , 0 ) ;
							filter = ARCFILTS1.elementAt( pos , 1 ) ;
							break ;
						case 1 :
							pos = ARCFILTS2.rangeInColumn( instcwl , 0 ) ;
							filter = ARCFILTS2.elementAt( pos , 1 ) ;
							break ;
						case 2 :
							pos = ARCFILTS3.rangeInColumn( instcwl , 0 ) ;
							filter = ARCFILTS3.elementAt( pos , 1 ) ;
							break ;
						default :
							break ;
					}
				}
				catch( Exception ex ){}
				if( filter.equalsIgnoreCase( "asInstrument" ) )
					filter = instfilt ;
			}
		}
		else
		{
			filter = FILTERS[ 0 ] ;
		}
		return filter ;
	}

	/**
	 * Set the filter.
	 */
	public void setFilter( String filter )
	{
		_avTable.set( SpCGS4CalUnitConstants.ATTR_FILTER , filter ) ;
	}

	/**
	 * Get the mode.
	 */
	public String getMode()
	{
		String mode = _avTable.get( SpCGS4CalUnitConstants.ATTR_MODE ) ;
		// if null get a default
		if( mode == null || mode.equals( "none" ) )
		{
			mode = getDefaultMode() ;
			setMode( mode ) ;
		}
		return mode ;
	}

	/**
	 * Get a default value for the mode
	 */
	public String getDefaultMode()
	{
		String mode ;
		mode = DEFAULT_MODE ;
		SpInstCGS4 inst = ( SpInstCGS4 )getInstrumentItem() ;
		if( mode == null && inst != null )
		{
			String instmode = inst.getMode() ;
			for( int i = 0 ; i < MODES.length ; i++ )
			{
				if( instmode.equals( MODES[ i ] ) )
					mode = instmode ;
			}
		}
		return mode ;
	}

	/**
	 * Set the mode.
	 */
	public void setMode( String mode )
	{
		_avTable.set( SpCGS4CalUnitConstants.ATTR_MODE , mode ) ;
	}

	/**
	 * Get the ND filter use
	 */
	public boolean getNdFilter()
	{
		boolean nd ;
		String ndstr = _avTable.get( SpCGS4CalUnitConstants.ATTR_NEUTRAL_DENSITY ) ;
		if( ndstr == null )
		{
			nd = getDefaultNdFilter() ;
			setNdFilter( nd ) ;
		}
		else
		{
			nd = Boolean.valueOf( ndstr ) ;
		}
		return nd ;
	}

	/**
	 * Get a default value for the ND filter. Need wavelength and disperser
	 */
	public boolean getDefaultNdFilter()
	{
		boolean ndFilter = false ;

		if( getCalType() == FLAT )
		{
			SpInstCGS4 inst = ( SpInstCGS4 )getInstrumentItem() ;
			if( inst != null )
			{
				try
				{
					int instdi = inst.getDisperserIndex() ;
					double instcwl = inst.getCentralWavelength() ;
					if( instdi == 0 )
					{
						int pos = FLATLAMPS1.rangeInColumn( instcwl , 0 ) ;
						ndFilter = Boolean.valueOf( FLATLAMPS1.elementAt( pos , 3 ) ) ;
					}
					else if( instdi == 1 )
					{
						int pos = FLATLAMPS2.rangeInColumn( instcwl , 0 ) ;
						ndFilter = Boolean.valueOf( FLATLAMPS2.elementAt( pos , 3 ) ) ;
					}
					else if( instdi == 2 )
					{
						int pos = FLATLAMPS3.rangeInColumn( instcwl , 0 ) ;
						ndFilter = Boolean.valueOf( FLATLAMPS3.elementAt( pos , 3 ) ) ;
					}
				}
				catch( Exception ex )
				{
					ndFilter = false ;
				}
			}
			else
			{
				ndFilter = false ;
			}
		}
		else
		{
			SpInstCGS4 inst = ( SpInstCGS4 )getInstrumentItem() ;
			if( inst != null )
			{
				try
				{
					int instdi = inst.getDisperserIndex() ;
					double instcwl = inst.getCentralWavelength() ;
					if( instdi == 0 )
					{
						int pos = ARCLAMPS1.rangeInColumn( instcwl , 0 ) ;
						ndFilter = Boolean.valueOf( ARCLAMPS1.elementAt( pos , 3 ) ) ;
					}
					else if( instdi == 1 )
					{
						int pos = ARCLAMPS2.rangeInColumn( instcwl , 0 ) ;
						ndFilter = Boolean.valueOf( ARCLAMPS2.elementAt( pos , 3 ) ) ;
					}
					else if( instdi == 2 )
					{
						int pos = ARCLAMPS3.rangeInColumn( instcwl , 0 ) ;
						ndFilter = Boolean.valueOf( ARCLAMPS3.elementAt( pos , 3 ) ) ;
					}
				}
				catch( Exception ex )
				{
					ndFilter = false ;
				}
			}
			else
			{
				ndFilter = false ;
			}
		}
		return ndFilter ;
	}

	/**
	 * Set the ND filter use
	 */
	public void setNdFilter( boolean nd )
	{
		_avTable.set( SpCGS4CalUnitConstants.ATTR_NEUTRAL_DENSITY , nd ) ;
	}

	/**
	 * useDefaults - reset values so that defaults will get used
	 */
	public void useDefaults()
	{
		_avTable.rm( SpCGS4CalUnitConstants.ATTR_LAMP ) ;
		_avTable.rm( SpCGS4CalUnitConstants.ATTR_FILTER ) ;
		_avTable.rm( SpCGS4CalUnitConstants.ATTR_MODE ) ;
		_avTable.rm( SpCGS4CalUnitConstants.ATTR_EXPOSURE_TIME ) ;
		_avTable.rm( SpCGS4CalUnitConstants.ATTR_CVF_WAVELENGTH ) ;
		_avTable.rm( SpCGS4CalUnitConstants.ATTR_FLAT_SAMPLING ) ;
		_avTable.rm( SpCGS4CalUnitConstants.ATTR_NEUTRAL_DENSITY ) ;
	}

	public void translateProlog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translateEpilog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		// First of all make sure we have a suitable instrument
		SpInstObsComp inst = SpTreeMan.findInstrument( this ) ;
		if( inst == null || !( inst instanceof SpInstCGS4 ) )
			throw new SpTranslationNotSupportedException( "No CGS4 instrument component in scope" ) ;
		
		// Get the current config items and then update it depending on the type
		Hashtable<String,String> configTable = inst.getConfigItems() ;

		configTable.put( "type" , getCalTypeString().toLowerCase() ) ;
		configTable.put( "DAConf" , getMode() ) ;
		configTable.put( "filter" , getFilter() ) ;
		configTable.put( "exposureTime" , "" + getExposureTime() ) ;
		configTable.put( "coadds" , "" + getCoadds() ) ;
		switch( getCalType() )
		{
			case FLAT :
				configTable.put( "sampling" , getFlatSampling() ) ;
				configTable.put( "flatLamp" , getLamp() ) ;
				configTable.put( "flatNeutralDensity" , Boolean.toString( getNdFilter() ) ) ;
				break ;
			case ARC :
				configTable.put( "arcLamp" , getLamp().split( "\\s" )[ 0 ].toLowerCase() ) ;
				configTable.put( "arcCvfWavelength" , "" + getCvfWavelength() ) ;
				break ;
			default :
				throw new SpTranslationNotSupportedException( "CGS4 Cal Obs is not defined as flat or arc" ) ;
		}

		// Now get hold of any DRRecipe component
		SpItem parent = parent() ;
		Vector<SpItem> recipes = null ;
		while( parent != null )
		{
			if( parent instanceof SpMSB )
			{
				recipes = SpTreeMan.findAllItems( parent , SpDRRecipe.class.getName() ) ;
				if( recipes.size() > 0 )
					break ;
			}
			parent = parent.parent() ;
		}

		if( recipes != null && recipes.size() != 0 )
		{
			SpDRRecipe recipe = ( SpDRRecipe )recipes.get( 0 ) ;
			switch( getCalType() )
			{
				case FLAT :
					v.add( "setHeader GRPMEM " + ( recipe.getFlatInGroup() ? "T" : "F" ) ) ;
					v.add( "setHeader RECIPE " + recipe.getFlatRecipeName() ) ;
					break ;
				case ARC :
					v.add( "setHeader GRPMEM " + ( recipe.getArcInGroup() ? "T" : "F" ) ) ;
					v.add( "setHeader RECIPE " + recipe.getArcRecipeName() ) ;
					break ;
				default :
					// We should never get here
			}
		}

		try
		{
			ConfigWriter.getCurrentInstance().write( configTable ) ;
		}
		catch( IOException ioe )
		{
			throw new SpTranslationNotSupportedException( "Error writing CGS Calibration observation: " + ioe.getMessage() ) ;
		}
		v.add( "loadConfig " + ConfigWriter.getCurrentInstance().getCurrentName() ) ;
		if( getCalType() == FLAT )
		{
			v.add( "set FLAT" ) ;
			v.add( gemini.sp.SpTranslationConstants.breakString ) ;
		}
		else
		{
			v.add( "set ARC" ) ;
		}
		v.add( "do " + getCount() + " _observe" ) ;

		// Finally. move the default config (labelled _1) down
		for( int i = v.size() - 1 ; i >= 0 ; i-- )
		{
			String firstConfig = v.get( i ) ;
			if( firstConfig.matches( "loadConfig .*_1" ) )
			{
				v.remove( i ) ;
				v.add( firstConfig ) ;
				break ;
			}
		}
	}
}
