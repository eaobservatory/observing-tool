/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
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

// author: Alan Pickup = dap@roe.ac.uk         2001 Nov
package orac.ukirt.iter ;

import java.util.Hashtable ;
import java.util.Vector ;
import java.io.IOException ;

import gemini.util.ConfigWriter ;
import gemini.util.MathUtil ;

import orac.ukirt.inst.SpInstUIST ;
import orac.ukirt.inst.SpDRRecipe ;
import gemini.sp.SpFactory ;
import gemini.sp.SpMSB ;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;
import gemini.sp.SpType ;
import gemini.sp.SpItem ;
import gemini.sp.SpTreeMan ;
import gemini.sp.obsComp.SpInstObsComp ;

import gemini.sp.iter.SpIterEnumeration ;
import gemini.sp.iter.SpIterObserveBase ;
import gemini.sp.iter.SpIterStep ;
import gemini.sp.iter.SpIterValue ;

@SuppressWarnings( "serial" )
class SpIterUISTCalObsEnumeration extends SpIterEnumeration
{
	private int _curCount = 0 ;
	private int _maxCount ;
	private String _calType ;
	private SpIterValue[] _values ;

	SpIterUISTCalObsEnumeration( SpIterUISTCalObs iterObserve )
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
		SpIterUISTCalObs ico = ( SpIterUISTCalObs )_iterComp ;
		_values = new SpIterValue[ 16 ] ; // Only need 16 items now (was 25) RDK

		ico.updateDAConf() ;
		_values[ 0 ] = new SpIterValue( SpUISTCalConstants.ATTR_FILTER , ico.getFilter() ) ;
		_values[ 1 ] = new SpIterValue( SpUISTCalConstants.ATTR_MODE , ico.W_mode ) ;
		_values[ 2 ] = new SpIterValue( SpUISTCalConstants.ATTR_EXPOSURE_TIME , String.valueOf( ico.W_actExpTime ) ) ;

		// Renumbered _values indexes to be contiguous after above items removed by RDK
		_values[ 3 ] = new SpIterValue( SpUISTCalConstants.ATTR_NREADS , String.valueOf( ico.W_nreads ) ) ;

		// Renumbered _values indexes to be contiguous after above items removed by RDK
		_values[ 4 ] = new SpIterValue( SpUISTCalConstants.ATTR_READ_INTERVAL , String.valueOf( ico.W_readInterval ) ) ;

		// Renumbered _values indexes to be contiguous after above items removed by RDK
		_values[ 5 ] = new SpIterValue( SpUISTCalConstants.ATTR_DUTY_CYCLE , String.valueOf( ico.W_dutyCycle ) ) ;
		_values[ 6 ] = new SpIterValue( SpUISTCalConstants.ATTR_CHOP_FREQUENCY , ico.W_chopFrequency ) ;
		_values[ 7 ] = new SpIterValue( SpUISTCalConstants.ATTR_CHOP_DELAY , String.valueOf( ico.W_chopDelay ) ) ;
		_values[ 8 ] = new SpIterValue( SpUISTCalConstants.ATTR_COADDS , String.valueOf( ico.W_coadds ) ) ;
		_values[ 9 ] = new SpIterValue( SpUISTCalConstants.ATTR_FLAT_SOURCE , ico.getFlatSource() ) ;
		_values[ 10 ] = new SpIterValue( SpUISTCalConstants.ATTR_ARC_SOURCE , ico.getArcSource() ) ;
		_values[ 11 ] = new SpIterValue( SpUISTCalConstants.ATTR_FOCUS , ico.getFocus() ) ;
		_values[ 12 ] = new SpIterValue( SpUISTCalConstants.ATTR_ORDER , ico.getOrder() ) ;
		_values[ 13 ] = new SpIterValue( SpUISTCalConstants.ATTR_CENTRAL_WAVELENGTH , ico.getCentralWavelength() ) ;
		_values[ 14 ] = new SpIterValue( SpUISTCalConstants.ATTR_OBSERVATION_TIME , String.valueOf( ico.W_obsTime ) ) ;

		_values[ 15 ] = new SpIterValue( SpUISTCalConstants.ATTR_EXPTIME_OT , ico.getExpTimeOTString() ) ;

		return _thisNextElement() ;
	}

	protected SpIterStep _thisNextElement()
	{
		return new SpIterStep( _calType , _curCount++ , _iterComp , _values ) ;
	}

}

/**
 * Iterator for UIST calibration observations (FLAT and ARC).
 */
@SuppressWarnings( "serial" )
public class SpIterUISTCalObs extends SpIterObserveBase implements SpTranslatable
{
	/** Identifier for a FLAT calibration. */
	public static final int FLAT = 0 ;

	/** Identifier for an ARC calibration. */
	public static final int ARC = 1 ;

	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "UISTCalObs" , "UIST Cal Observe" ) ;

	public String W_mode ;
	public int W_nreads ;
	public double W_readInterval ;
	public String W_chopFrequency ;
	public double W_chopDelay ;
	public int W_coadds ;
	public double W_dutyCycle ;
	public double W_actExpTime ;
	public double W_obsTime ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterUISTCalObs() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterUISTCalObs()
	{
		super( SP_TYPE ) ;

		_avTable.noNotifySet( SpUISTCalConstants.ATTR_CALTYPE , "Flat" , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_FILTER , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_MODE , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_EXPOSURE_TIME , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_NREADS , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_READ_INTERVAL , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_DUTY_CYCLE , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_CHOP_FREQUENCY , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_CHOP_DELAY , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_COADDS , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_FLAT_SOURCE , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_ARC_SOURCE , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_FOCUS , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_ORDER , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_CENTRAL_WAVELENGTH , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_OBSERVATION_TIME , null , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_EXPTIME_OT , null , 0 ) ;
	}

	/**
	 * Override getTitle to return the observe type and the count.
	 */
	public String getTitle()
	{
		String title ;
		
		if( getTitleAttr() != null )
			title = super.getTitle() ;
		else 
			title = getCalTypeString() + " (" + getCount() + "X)" ;
		
		return title ;
	}

	/**
	 */
	public SpIterEnumeration elements()
	{
		return new SpIterUISTCalObsEnumeration( this ) ;
	}

	/**
	 * Get the instrument item in the scope of the base item.
	 */
	public SpInstObsComp getInstrumentItem()
	{
		SpItem _baseItem = parent() ;
		SpInstObsComp inst = SpTreeMan.findInstrument( _baseItem ) ;
		
		if( inst == null )
			throw new RuntimeException( "no instrument in scope" ) ;
		
		return inst ;
	}

	/**
	 * Get the exposure time OT
	 */
	public double getExpTimeOT()
	{
		double et = _avTable.getDouble( SpUISTCalConstants.ATTR_EXPTIME_OT , 0. ) ;
		if( et == 0. )
		{
			et = getDefaultExpTimeOT() ;
			setExpTimeOT( Double.toString( et ) ) ;
		}
		return et ;

	}

	/**
	 * Get the exposure time OT as a string to 4 places
	 * Base on method used in MathUtil.round
	 */
	public String getExpTimeOTString()
	{
		int mult = ( int )Math.pow( 10 , 4 ) ;
		double et = ( ( double )Math.round( getExpTimeOT() * mult ) ) / mult ;
		String timeAsString = Double.toString( et ) ;
		return timeAsString ;
	}

	/**
	 * Provide a default exposure time.
	 */
	public double getDefaultExpTimeOT()
	{
		double det = 0. ;
		// Get the exposure time from the UIST instrument
		SpInstUIST inst = ( SpInstUIST )getInstrumentItem() ;
		if( getCalType() == FLAT )
			det = inst.getDefaultFlatExpTime() ;
		else if( getCalType() == ARC )
			det = inst.getDefaultArcExpTime() ;

		return det ;
	}

	public int getDefaultCoadds()
	{
		int dc = 1 ;
		// Get the coadds from the UIST instrument
		SpInstUIST inst = ( SpInstUIST )getInstrumentItem() ;
		if( getCalType() == FLAT )
			dc = inst.getDefaultFlatCoadds() ;
		else if( getCalType() == ARC )
			dc = inst.getDefaultArcCoadds() ;

		return dc ;

	}

	// End of added by RDK

	/**
	 * Set the exposure time OT
	 */
	public void setExpTimeOT( String expTime )
	{
		_avTable.set( SpUISTCalConstants.ATTR_EXPTIME_OT , expTime ) ;
	}

	/**
	 * Get the type of calibration.
	 */
	public int getCalType()
	{
		int type = ARC ;
		String calType = _avTable.get( SpUISTCalConstants.ATTR_CALTYPE ) ;
		if( "Flat".equals( calType ) )
			type = FLAT ;
		return type ;
	}

	/**
	 * Set the type of calibration.
	 */
	public void setCalType( String calType )
	{
		_avTable.set( SpUISTCalConstants.ATTR_CALTYPE , calType ) ;
	}

	/**
	 * Get the type of calibration as a String.
	 */
	public String getCalTypeString()
	{
		String calType = "Arc" ;
		if( getCalType() == FLAT )
			calType = "Flat" ;

		return calType ;
	}

	/**
	 * Get the calibration type choices.
	 */
	public String[] getCalTypeChoices()
	{
		String choices[] = null ;
		SpInstUIST inst = ( SpInstUIST )getInstrumentItem() ;
		if( inst.isImaging() )
		{
			choices = new String[ 1 ] ;
			choices[ 0 ] = "Flat" ;
		}
		else
		{
			choices = new String[ 2 ] ;
			choices[ 0 ] = "Flat" ;
			choices[ 1 ] = "Arc" ;
		}
		return choices ;
	}

	/**
	 * Report whether the instrument is imaging
	 */
	public boolean isImaging()
	{
		SpInstUIST inst = ( SpInstUIST )getInstrumentItem() ;
		String camera = inst.getCamera() ;
		return( camera.equalsIgnoreCase( "imaging" ) ) ;
	}

	/**
	 * Get the flat source
	 */
	public String getFlatSource()
	{
		String fs = null ;
		if( getCalType() == FLAT )
		{
			fs = _avTable.get( SpUISTCalConstants.ATTR_FLAT_SOURCE ) ;
			if( fs == null )
			{
				SpInstUIST inst = ( SpInstUIST )getInstrumentItem() ;
				fs = inst.getDefaultFlatSource() ;
				setFlatSource( fs ) ;
			}
		}
		else
		{
			fs = "undefined" ;
		}
		return fs ;
	}

	/**
	 * Set the flat source
	 */
	public void setFlatSource( String fs )
	{
		_avTable.set( SpUISTCalConstants.ATTR_FLAT_SOURCE , fs ) ;
	}

	/**
	 * Get the flat source choices.
	 */
	public String[] getFlatSourceChoices()
	{
		SpInstUIST inst = ( SpInstUIST )getInstrumentItem() ;
		String choices[] = inst.getFlatList() ;
		return choices ;
	}

	/**
	 * Get the focus
	 */
	public String getFocus()
	{
		String focus ;
		SpInstUIST inst = ( SpInstUIST )getInstrumentItem() ;
		
		if( getCalType() == ARC )
			focus = inst.getArcFocus() ;
		else
			focus = inst.getFocus() ;

		setFocus( focus ) ;
		return focus ;
	}

	/**
	 * Set the focus
	 */
	public void setFocus( String focus )
	{
		_avTable.set( SpUISTCalConstants.ATTR_FOCUS , focus ) ;
	}

	/**
	 * Get the order
	 */
	public String getOrder()
	{
		String order ;
		SpInstUIST inst = ( SpInstUIST )getInstrumentItem() ;
		
		if( getCalType() == ARC )
			order = inst.getArcOrder() ;
		else
			order = inst.getOrderString() ;

		setOrder( order ) ;
		return order ;
	}

	/**
	 * Set the order
	 */
	public void setOrder( String order )
	{
		_avTable.set( SpUISTCalConstants.ATTR_ORDER , order ) ;
	}

	/**
	 * Get the central wavelength
	 */
	public String getCentralWavelength()
	{
		String cwl ;
		SpInstUIST inst = ( SpInstUIST )getInstrumentItem() ;
		
		if( getCalType() == ARC )
			cwl = inst.getArcCentralWavelength() ;
		else
			cwl = inst.getCentralWavelengthString() ;

		setCentralWavelength( cwl ) ;
		return cwl ;
	}

	/**
	 * Set the central wavelength
	 */
	public void setCentralWavelength( String cwl )
	{
		_avTable.set( SpUISTCalConstants.ATTR_CENTRAL_WAVELENGTH , cwl ) ;
	}

	/**
	 * Get the arc source
	 */
	public String getArcSource()
	{
		String as ;
		if( getCalType() == ARC )
		{
			as = _avTable.get( SpUISTCalConstants.ATTR_ARC_SOURCE ) ;
			if( as == null )
			{
				/* The default is the first available choice */
				as = getArcSourceChoices()[ 0 ] ;
				setArcSource( as ) ;
			}
		}
		else
		{
			as = "undefined" ;
			setArcSource( as ) ;
		}
		return as ;
	}

	/**
	 * Set the arc source
	 */
	public void setArcSource( String as )
	{
		_avTable.set( SpUISTCalConstants.ATTR_ARC_SOURCE , as ) ;
	}

	/**
	 * Get the arc source choices.
	 */
	public String[] getArcSourceChoices()
	{
		SpInstUIST inst = ( SpInstUIST )getInstrumentItem() ;
		String choices[] = inst.getArcList() ;
		return choices ;
	}

	/**
	 * Get the filter.
	 */
	public String getFilter()
	{
		String filter = _avTable.get( SpUISTCalConstants.ATTR_FILTER ) ;
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

		SpInstUIST inst = ( SpInstUIST )getInstrumentItem() ;
		
		if( getCalType() == FLAT )
			filter = inst.getFilter() ;
		else if( getCalType() == ARC )
			filter = inst.getArcFilter() ;
		
		return filter ;
	}

	/**
	 * Set the filter.
	 */
	public void setFilter( String filter )
	{
		_avTable.set( SpUISTCalConstants.ATTR_FILTER , filter ) ;
	}

	/**
	 * Get coadds number as string
	 */
	public String getCoaddsString()
	{
		String coadds = _avTable.get( SpUISTCalConstants.ATTR_COADDS ) ;
		if( coadds == null )
		{
			coadds = Integer.toString( getDefaultCoadds() ) ;
			setCoadds( coadds ) ;
		}
		return coadds ;
	}

	/**
	 * Set the coadds
	 */
	public void setCoadds( String coadds )
	{
		_avTable.set( SpUISTCalConstants.ATTR_COADDS , coadds ) ;
	}

	/**
	 * Get observation time as string
	 */
	public String getObservationTimeString()
	{
		double ot = Double.valueOf( _avTable.get( SpUISTCalConstants.ATTR_OBSERVATION_TIME ) ) ;
		String observationTime = Double.toString( MathUtil.round( ot , 3 ) ) ;
		return observationTime ;
	}

	// End of added by RDK
	/**
	 * use
	 Defaults - reset values so that defaults will get used
	 */
	public void useDefaults()
	{
		_avTable.rm( SpUISTCalConstants.ATTR_FILTER ) ;
		_avTable.rm( SpUISTCalConstants.ATTR_MODE ) ;
		_avTable.rm( SpUISTCalConstants.ATTR_EXPOSURE_TIME ) ;
		_avTable.rm( SpUISTCalConstants.ATTR_NREADS ) ;
		_avTable.rm( SpUISTCalConstants.ATTR_READ_INTERVAL ) ;
		_avTable.rm( SpUISTCalConstants.ATTR_DUTY_CYCLE ) ;
		_avTable.rm( SpUISTCalConstants.ATTR_CHOP_FREQUENCY ) ;
		_avTable.rm( SpUISTCalConstants.ATTR_CHOP_DELAY ) ;
		_avTable.rm( SpUISTCalConstants.ATTR_COADDS ) ;
		_avTable.rm( SpUISTCalConstants.ATTR_FLAT_SOURCE ) ;
		_avTable.rm( SpUISTCalConstants.ATTR_ARC_SOURCE ) ;
		_avTable.rm( SpUISTCalConstants.ATTR_OBSERVATION_TIME ) ;
		_avTable.rm( SpUISTCalConstants.ATTR_EXPTIME_OT ) ;
	}

	/**
	 * Update the daconf for an Object/Sky observatioon
	 */
	public void updateDAConf()
	{
		SpInstUIST inst = ( SpInstUIST )getInstrumentItem() ;
		if( getCalType() == FLAT )
		{
			inst.setFlatExpTime( getExpTimeOT() ) ;
			String coaddsString = getCoaddsString() ;
			inst.setFlatCoadds( Integer.valueOf( coaddsString ) ) ;
			// End of added by RDK
			inst.updateDAFlatConf() ;
		}
		else if( getCalType() == ARC )
		{
			inst.setArcExpTime( getExpTimeOT() ) ;
			inst.setArcCoadds( Integer.valueOf( getCoaddsString() ) ) ;
			// End of added by RDK
			inst.updateDAArcConf() ;
		}
		/* Update local instance variables from UIST class */
		W_mode = inst.W_mode ;
		W_nreads = inst.W_nreads ;
		W_readInterval = inst.W_readInterval ;
		W_dutyCycle = inst.W_dutyCycle ;
		W_chopFrequency = inst.W_chopFrequency ;
		W_chopDelay = inst.W_chopDelay ;
		W_actExpTime = inst.W_actExpTime ;
		W_obsTime = inst.W_obsTime ;
		W_coadds = inst.W_coadds ;

		/* Update attributes from instance variables */
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_MODE , W_mode , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_NREADS , Integer.toString( W_nreads ) , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_READ_INTERVAL , Double.toString( W_readInterval ) , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_DUTY_CYCLE , Double.toString( W_dutyCycle ) , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_CHOP_FREQUENCY , W_chopFrequency , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_CHOP_DELAY , Double.toString( W_chopDelay ) , 0 ) ;
		// The exposure time may have been over-ridden by the QT.  Only do an update if it hasn't
		if( !_avTable.exists( "override_" + ATTR_EXPOSURE_TIME ) || !_avTable.getBool( "override_" + ATTR_EXPOSURE_TIME ) )
			_avTable.noNotifySet( SpUISTCalConstants.ATTR_EXPOSURE_TIME , Double.toString( W_actExpTime ) , 0 ) ;

		_avTable.noNotifySet( SpUISTCalConstants.ATTR_OBSERVATION_TIME , Double.toString( W_obsTime ) , 0 ) ;
		// The coadds may have been over-ridden by the QT.  Only do an update if it hasn't
		if( !_avTable.exists( "override_" + ATTR_COADDS ) || !_avTable.getBool( "override_" + ATTR_COADDS ) )
			_avTable.noNotifySet( SpUISTCalConstants.ATTR_COADDS , Integer.toString( W_coadds ) , 0 ) ;

		_avTable.noNotifySet( SpUISTCalConstants.ATTR_FLAT_SOURCE , getFlatSource() , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_ARC_SOURCE , getArcSource() , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_FILTER , getFilter() , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_FOCUS , getFocus() , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_ORDER , getOrder() , 0 ) ;
		_avTable.noNotifySet( SpUISTCalConstants.ATTR_CENTRAL_WAVELENGTH , getCentralWavelength() , 0 ) ;
	}

	public void translateProlog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translateEpilog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		SpInstUIST inst ;
		try
		{
			inst = ( SpInstUIST )SpTreeMan.findInstrument( this ) ;
		}
		catch( Exception e )
		{
			throw new SpTranslationNotSupportedException( "No UIST instrument is scope" ) ;
		}

		// Work out whether we are a flat or an arc
		boolean isFlat = ( getCalType() == FLAT ) ;

		// We first need to get the DRRecipe component...
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
			if( isFlat )
			{
				v.add( "setHeader GRPMEM " + ( recipe.getFlatInGroup() ? "T" : "F" ) ) ;
				v.add( "setHeader RECIPE " + recipe.getFlatRecipeName() ) ;
			}
			else
			{
				// Assume arc
				v.add( "setHeader GRPMEM " + ( recipe.getArcInGroup() ? "T" : "F" ) ) ;
				v.add( "setHeader RECIPE " + recipe.getArcRecipeName() ) ;
			}
		}

		Hashtable<String,String> configTable = inst.getConfigItems() ;
		if( isFlat )
		{
			configTable.put( "flatSource" , getFlatSource() ) ;
			configTable.put( "filter" , inst.getFilter() ) ;
		}
		else
		{
			configTable.put( "arcSource" , getArcSource() ) ;
			configTable.put( "filter" , inst.getArcFilter() ) ;
		}

		configTable.put( "type" , getCalTypeString().toLowerCase() ) ;
		configTable.put( "exposureTime" , "" + getExposureTime() ) ;
		configTable.put( "coadds" , "" + getCoadds() ) ;
		configTable.put( "observationTime" , getObservationTimeString() ) ;
		
		if( _avTable.exists( SpUISTCalConstants.ATTR_NREADS ) )
			configTable.put( "nreads" , _avTable.get( SpUISTCalConstants.ATTR_NREADS ) ) ;
		
		if( _avTable.exists( SpUISTCalConstants.ATTR_CHOP_DELAY ) )
			configTable.put( "chopDelay" , _avTable.get( SpUISTCalConstants.ATTR_CHOP_DELAY ) ) ;

		if( _avTable.exists( SpUISTCalConstants.ATTR_DUTY_CYCLE ) )
			configTable.put( "dutyCycle" , _avTable.get( SpUISTCalConstants.ATTR_DUTY_CYCLE ) ) ;

		if( _avTable.exists( SpUISTCalConstants.ATTR_CENTRAL_WAVELENGTH ) )
			configTable.put( "centralWavelength" , _avTable.get( SpUISTCalConstants.ATTR_CENTRAL_WAVELENGTH ) ) ;

		try
		{
			ConfigWriter.getCurrentInstance().write( configTable ) ;
		}
		catch( IOException ioe )
		{
			throw new SpTranslationNotSupportedException( "Unable to write UISTCalObs config file" ) ;
		}
		
		v.add( "loadConfig " + ConfigWriter.getCurrentInstance().getCurrentName() ) ;
		v.add( "setrotator " + configTable.get( "posAngle" ) ) ;
		v.add( "set " + getCalTypeString().toUpperCase() ) ;
		v.add( "do " + getCount() + " _observe" ) ;

		// Finally move the default config file, numbered _1 down
		for( int i = v.size() - 1 ; i >= 0 ; i-- )
		{
			String defCon = v.get( i ) ;
			if( defCon.matches( "loadConfig .*_1" ) )
			{
				v.remove( i ) ;
				v.add( defCon ) ;
				break ;
			}
		}
	}
}
