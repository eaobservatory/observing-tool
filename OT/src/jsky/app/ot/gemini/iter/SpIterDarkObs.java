// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.iter ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;

import gemini.sp.iter.SpIterEnumeration ;
import gemini.sp.iter.SpIterObserveBase ;
import gemini.sp.iter.SpIterStep ;
import gemini.sp.iter.SpIterValue ;

import gemini.sp.obsComp.SpInstConstants ;

/**
 * Enumerater for the elements of the Observe iterator.
 */
class SpIterDarkObsEnumeration extends SpIterEnumeration
{
	private int _curCount = 0 ;
	private int _maxCount ;
	private SpIterValue[] _values ;

	SpIterDarkObsEnumeration( SpIterDarkObs iterObserve )
	{
		super( iterObserve ) ;
		_maxCount = iterObserve.getCount() ;
	}

	protected boolean _thisHasMoreElements()
	{
		return( _curCount < _maxCount ) ;
	}

	protected SpIterStep _thisFirstElement()
	{
		SpIterDarkObs ibo = ( SpIterDarkObs )_iterComp ;
		String expTimeValue = String.valueOf( ibo.getExposureTime() ) ;
		String coaddsValue = String.valueOf( ibo.getCoadds() ) ;

		_values = new SpIterValue[ 2 ] ;
		_values[ 0 ] = new SpIterValue( SpInstConstants.ATTR_EXPOSURE_TIME , expTimeValue ) ;
		_values[ 1 ] = new SpIterValue( SpInstConstants.ATTR_COADDS , coaddsValue ) ;

		return _thisNextElement() ;
	}

	protected SpIterStep _thisNextElement()
	{
		return new SpIterStep( "dark" , _curCount++ , _iterComp , _values ) ;
	}

}

public class SpIterDarkObs extends SpIterObserveBase
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "darkObs" , "Dark" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterDarkObs() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterDarkObs()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Override getTitle to return the observe count.
	 */
	public String getTitle()
	{
		if( getTitleAttr() != null )
			return super.getTitle() ;

		return "Dark (" + getCount() + "X)" ;
	}

	/**
	 */
	public SpIterEnumeration elements()
	{
		return new SpIterDarkObsEnumeration( this ) ;
	}
}
