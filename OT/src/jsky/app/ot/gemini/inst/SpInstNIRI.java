// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.inst;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpStareCapability;

/**
 * The NIRI instrument.
 */
public final class SpInstNIRI extends SpInstObsComp implements SpInstNIRIConstants
{
	public static final SpType SP_TYPE = SpType.create( SpType.OBSERVATION_COMPONENT_TYPE , "inst.NIRI" , "NIRI" );

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpInstNIRI() );
	}

	public SpInstNIRI()
	{
		super( SP_TYPE );

		addCapability( new SpStareCapability() );

		String attr = ATTR_CAMERA;
		String value = CAMERAS[ 0 ];
		_avTable.noNotifySet( attr , value , 0 );

		attr = ATTR_FILTER;
		value = SPECIAL_FILTERS[ 0 ][ 0 ];
		_avTable.noNotifySet( attr , value , 0 );

		attr = ATTR_EXPOSURE_TIME;
		_avTable.noNotifySet( attr , "0" , 0 );

		attr = ATTR_COADDS;
		_avTable.noNotifySet( attr , "1" , 0 );
	}

	/**
	 * Get the stare capability.
	 */
	public SpStareCapability getStareCapability()
	{
		return ( SpStareCapability )getCapability( SpStareCapability.CAPABILITY_NAME );
	}

	/**
	 * Return the appropriate science area for the given mode.
	 */
	public double[] getScienceArea( int mode )
	{
		switch( mode )
		{
			case CAMERA_F6 :
				return new double[]{ 123. , 123. };
			case CAMERA_F14 :
				return new double[]{ 51. , 51. };
			case CAMERA_F32 :
				return new double[]{ 20. , 20. };
		}
		return null;
	}

	/**
	 * Return the science area based upon the current camera.
	 */
	public double[] getScienceArea()
	{
		// The size depends upon the selected camera.
		double[] size = getScienceArea( getCameraIndex() );

		// If a mask has been applied, shrink the width.
		double width = size[ 0 ];
		String mask = _avTable.get( ATTR_MASK );
		if( mask != null )
		{
			if( mask.equals( MASK_0_1 ) )
				width = 0.1;
			else if( mask.equals( MASK_0_2 ) )
				width = 0.2;
			else if( mask.equals( MASK_1_0 ) )
				width = 1. ;
		}
		size[ 0 ] = width;
		return size;
	}

	/**
	 * Get the camera.
	 */
	public String getCamera()
	{
		String camera = _avTable.get( ATTR_CAMERA );
		if( camera == null )
			camera = CAMERAS[ 0 ];

		return camera;
	}

	/**
	 * Get the camera index: one of CAMERA_F6, CAMERA_F14, or CAMERA_F32.
	 */
	public int getCameraIndex()
	{
		String camera = _avTable.get( ATTR_CAMERA );
		int index = CAMERA_F32;
		if( ( camera == null ) || ( camera.equals( CAMERAS[ CAMERA_F6 ] ) ) )
			index = CAMERA_F6;
		else if( camera.equals( CAMERAS[ CAMERA_F14 ] ) )
			index = CAMERA_F14;
		return index;
	}

	/**
	 * Set the camera.
	 */
	public void setCamera( String camera )
	{
		_avTable.set( ATTR_CAMERA , camera );
	}

	/**
	 * Get the disperser.
	 */
	public String getDisperser()
	{
		// For old programs, change "grism" to "disperser".
		String grism = _avTable.get( "grism" );
		if( grism != null )
		{
			setDisperser( grism );
			_avTable.rm( "grism" );
			return grism;
		}

		// For new programs, everything is fine.
		String disperser = _avTable.get( ATTR_DISPERSER );
		if( disperser == null )
			disperser = DISPERSERS[ 0 ];

		return disperser;
	}

	/**
	 * Set the disperser.
	 */
	public void setDisperser( String disperser )
	{
		_avTable.set( ATTR_DISPERSER , disperser );
	}

	/**
	 * Get the mask.
	 */
	public String getMask()
	{
		// For old programs, change "slit" to "mask".
		String slit = _avTable.get( "slit" );
		if( slit != null )
		{
			if( slit.indexOf( "arcsec" ) != -1 )
				slit += " slit"; // Add the word "slit" to the slit masks
			setMask( slit );
			_avTable.rm( "slit" ); // Remove the old attribute
			return slit;
		}

		// For new programs, everything is fine.
		String mask = _avTable.get( ATTR_MASK );
		if( mask == null )
			mask = MASKS[ 0 ];
		
		return mask;
	}

	/**
	 * Set the mask.
	 */
	public void setMask( String mask )
	{
		_avTable.set( ATTR_MASK , mask );
	}

	/**
	 * Get the filter.
	 */
	public String getFilter()
	{
		String filter = _avTable.get( ATTR_FILTER );
		if( filter == null )
			filter = FILTER_NONE;

		return filter;
	}

	/**
	 * Set the filter.
	 */
	public void setFilter( String filter )
	{
		_avTable.set( ATTR_FILTER , filter );
	}
}
