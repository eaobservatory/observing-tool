// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.util;

/**
 * Utility class support for the coordinate systems supported by Gemini.
 *
 * <p>
 * <b>This class and other changes to support multiple coordinate systems
 * have not been completed.</b>
 */
public class CoordSys
{
	// The possible coordinate systems.
	public static final int FK5 = 0;
	public static final int FK4 = 1;
	public static final int AZ_EL = 2;
	public static final int GAL = 3;
	public static final int HADEC = 4;

	public static final String FK5_STRING = "FK5 (J2000)";
	public static final String FK4_STRING = "FK4 (B1950)";
	public static final String AZ_EL_STRING = "Az/El";
	public static final String GAL_STRING = "Galactic";
	public static final String HADEC_STRING = "HADEC";

	/**
	 * Readable coordinate system strings.
	 */
	public static final String[] COORD_SYS = 
	{ 
		FK5_STRING , 
		FK4_STRING , 
		AZ_EL_STRING , 
		GAL_STRING , 
		HADEC_STRING 
	};

	// MFO (March 08, 2002)
	/**
	 * Coordinate System x axis labels.
	 */
	public static final String[] X_AXIS_LABEL = 
	{ 
		"Ra" , 
		"Ra" , 
		"Az" , 
		"Long" , 
		"HA" 
	};

	// MFO (March 08, 2002)
	/**
	 * Coordinate System y axis labels.
	 */
	public static final String[] Y_AXIS_LABEL = 
	{ 
		"Dec" , 
		"Dec" , 
		"El" , 
		"Lat" , 
		"Dec" 
	};

	/**
	 * Get an integer representing a coordinate system from its associated
	 * string.  If the string is not recognizable, return -1.
	 */
	public static int getSystem( String coordSysString )
	{
		if( coordSysString.equals( FK5_STRING ) )
			return FK5;
		else if( coordSysString.equals( FK4_STRING ) )
			return FK4;
		else if( coordSysString.equals( AZ_EL_STRING ) )
			return AZ_EL;
		else if( coordSysString.equals( GAL_STRING ) )
			return GAL;
		else if( coordSysString.equals( HADEC_STRING ) )
			return HADEC;

		return -1;
	}

	/**
	 * Get the string representing a coordinate system from its associated
	 * int.  If the int is out of range, return null.
	 */
	public static String getSystem( int coordSysInt )
	{
		if( ( coordSysInt < 0 ) || ( coordSysInt >= COORD_SYS.length ) )
			return null;
		return COORD_SYS[ coordSysInt ];
	}
}