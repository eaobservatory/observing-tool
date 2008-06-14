// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.iter ;

import jsky.app.ot.gemini.inst.SpInstNIRIConstants ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;
import gemini.sp.iter.IterConfigItem ;
import gemini.sp.iter.SpIterConfigObs ;

/**
 * The NIRI configuration iterator.
 */
public class SpIterNIRI extends SpIterConfigObs
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "instNIRI" , "NIRI" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterNIRI() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterNIRI()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Get the name of the item being iterated over.  Subclasses must
	 * define.
	 */
	public String getItemName()
	{
		return "NIRI" ;
	}

	/**
	 * Get the array containing the IterConfigItems offered by NIRI.
	 */
	public IterConfigItem[] getAvailableItems()
	{
		IterConfigItem iciCamera = new IterConfigItem( "Camera" , SpInstNIRIConstants.ATTR_CAMERA + "Iter" , SpInstNIRIConstants.CAMERAS ) ;
		IterConfigItem iciDisperser = new IterConfigItem( "Disperser" , SpInstNIRIConstants.ATTR_DISPERSER + "Iter" , SpInstNIRIConstants.DISPERSERS ) ;
		IterConfigItem iciMask = new IterConfigItem( "Mask" , SpInstNIRIConstants.ATTR_MASK + "Iter" , SpInstNIRIConstants.MASKS ) ;
		IterConfigItem iciPosAngle = new IterConfigItem( "Pos. Angle" , SpInstNIRIConstants.ATTR_POS_ANGLE + "Iter" , null ) ;

		// Filters
		int n = SpInstNIRIConstants.BROAD_BAND_FILTERS.length + SpInstNIRIConstants.NARROW_BAND_FILTERS.length + SpInstNIRIConstants.SPECIAL_FILTERS.length ;
		String[] filters = new String[ n ] ;
		for( int i = 0 ; i < SpInstNIRIConstants.BROAD_BAND_FILTERS.length ; ++i )
			filters[ i ] = SpInstNIRIConstants.BROAD_BAND_FILTERS[ i ][ 0 ] ;

		int offset = SpInstNIRIConstants.BROAD_BAND_FILTERS.length ;
		for( int i = 0 ; i < SpInstNIRIConstants.NARROW_BAND_FILTERS.length ; ++i )
			filters[ offset + i ] = SpInstNIRIConstants.NARROW_BAND_FILTERS[ i ][ 0 ] ;

		offset += SpInstNIRIConstants.NARROW_BAND_FILTERS.length ;
		for( int i = 0 ; i < SpInstNIRIConstants.SPECIAL_FILTERS.length ; ++i )
			filters[ offset + i ] = SpInstNIRIConstants.SPECIAL_FILTERS[ i ][ 0 ] ;

		IterConfigItem iciFilter = new IterConfigItem( "Filter" , SpInstNIRIConstants.ATTR_FILTER + "Iter" , filters ) ;
		IterConfigItem[] iciA = { iciCamera , iciDisperser , iciFilter , iciMask , getExposureTimeConfigItem() , getCoaddsConfigItem() , iciPosAngle } ;

		return iciA ;
	}
}
