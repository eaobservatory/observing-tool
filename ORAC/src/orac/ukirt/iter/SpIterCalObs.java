// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;

import gemini.sp.iter.SpIterEnumeration ;
import gemini.sp.iter.SpIterObserveBase ;
import gemini.sp.iter.SpIterStep ;
import gemini.sp.iter.SpIterValue ;

@SuppressWarnings( "serial" )
class SpIterCalObsEnumeration extends SpIterEnumeration
{
	private int _curCount = 0 ;
	private int _maxCount ;
	private String _calType ;
	private SpIterValue[] _values ;

	SpIterCalObsEnumeration( SpIterCalObs iterObserve )
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
		SpIterCalObs ico = ( SpIterCalObs )_iterComp ;
		_values = new SpIterValue[ 5 ] ;

		_values[ 0 ] = new SpIterValue( SpCalUnitConstants.ATTR_LAMP , ico.getLamp() ) ;
		_values[ 1 ] = new SpIterValue( SpCalUnitConstants.ATTR_FILTER , ico.getFilter() ) ;
		_values[ 2 ] = new SpIterValue( SpCalUnitConstants.ATTR_MODE , ico.getMode() ) ;
		_values[ 3 ] = new SpIterValue( SpCalUnitConstants.ATTR_EXPOSURE_TIME , String.valueOf( ico.getExposureTime() ) ) ;

		_values[ 4 ] = new SpIterValue( SpCalUnitConstants.ATTR_COADDS , String.valueOf( ico.getCoadds() ) ) ;

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
public class SpIterCalObs extends SpIterObserveBase
{

	/** Identifier for a FLAT calibration. */
	public static final int FLAT = 0 ;

	/** Identifier for an ARC calibration. */
	public static final int ARC = 1 ;

	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "CGS4calUnitObs" , "CGS4 Cal Unit Observe" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterCalObs() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterCalObs()
	{
		super( SP_TYPE ) ;

		System.out.println( "In SpIterCalObs" ) ;

		String defLamp = SpCalUnitConstants.CGS4_ARC_LAMPS[ 0 ] ;
		String defFilter = SpCalUnitConstants.CGS4_FILTERS[ 0 ] ;
		String defMode = SpCalUnitConstants.MODES[ 0 ] ;

		_avTable.noNotifySet( SpCalUnitConstants.ATTR_LAMP , defLamp , 0 ) ;
		_avTable.noNotifySet( SpCalUnitConstants.ATTR_FILTER , defFilter , 0 ) ;
		_avTable.noNotifySet( SpCalUnitConstants.ATTR_MODE , defMode , 0 ) ;
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
		return new SpIterCalObsEnumeration( this ) ;
	}

	/**
	 * Get the lamp.
	 */
	public String getLamp()
	{
		String lamp = _avTable.get( SpCalUnitConstants.ATTR_LAMP ) ;
		if( lamp == null )
			lamp = SpCalUnitConstants.CGS4_ARC_LAMPS[ 0 ] ;
		
		return lamp ;
	}

	/**
	 * Set the lamp.
	 */
	public void setLamp( String lamp )
	{
		_avTable.set( SpCalUnitConstants.ATTR_LAMP , lamp ) ;
	}

	/**
	 * Get the type of calibration (derived from the choice of lamp).
	 */
	public int getCalType()
	{
		return ARC ;
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
	 * Get the filter.
	 */
	public String getFilter()
	{
		String filter = _avTable.get( SpCalUnitConstants.ATTR_FILTER ) ;
		if( filter == null )
			filter = SpCalUnitConstants.CGS4_FILTERS[ 0 ] ;
		
		return filter ;
	}

	/**
	 * Set the filter.
	 */
	public void setFilter( String filter )
	{
		_avTable.set( SpCalUnitConstants.ATTR_FILTER , filter ) ;
	}

	/**
	 * Get the mode.
	 */
	public String getMode()
	{
		String mode = _avTable.get( SpCalUnitConstants.ATTR_MODE ) ;
		if( mode == null )
			mode = SpCalUnitConstants.MODES[ 0 ] ;
		
		return mode ;
	}

	/**
	 * Set the mode.
	 */
	public void setMode( String mode )
	{
		_avTable.set( SpCalUnitConstants.ATTR_MODE , mode ) ;
	}
}
