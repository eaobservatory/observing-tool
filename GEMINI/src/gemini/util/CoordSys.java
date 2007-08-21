// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util;

/**
 * Utility class support for the coordinate systems supported by Gemini.
 * 
 * <p>
 * <b>This class and other changes to support multiple coordinate systems have
 * not been completed.</b>
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
	
	public static final String FK5_SHORT_STRING = "FK5" ; 	 
	public static final String FK4_SHORT_STRING = "FK4" ; 	 
	public static final String AZ_EL_SHORT_STRING = "AZ" ; 	 
	public static final String GAL_SHORT_STRING = "GAL" ; 	 
	public static final String HADEC_SHORT_STRING = "HA" ;

	/**
     * Readable coordinate system strings.
     */
	public static final String[] COORD_SYS = { FK5_STRING , FK4_STRING , AZ_EL_STRING , GAL_STRING , HADEC_STRING };
    public static final String[] SHORT_COORD_SYS = { FK5_SHORT_STRING , FK4_SHORT_STRING , AZ_EL_SHORT_STRING , GAL_SHORT_STRING , HADEC_SHORT_STRING };

	// MFO (March 08, 2002)
	/**
     * Coordinate System x axis labels.
     */
	public static final String[] X_AXIS_LABEL = { "Ra" , "Ra" , "Az" , "Long" , "HA" };

	// MFO (March 08, 2002)
	/**
     * Coordinate System y axis labels.
     */
	public static final String[] Y_AXIS_LABEL = { "Dec" , "Dec" , "El" , "Lat" , "Dec" };

	/**
     * Get an integer representing a coordinate system from its associated
     * string. If the string is not recognizable, return -1.
     */
	public static int getSystem( String coordSysString )
	{
		for( int i = 0 ; i < SHORT_COORD_SYS.length ; i++ )
		{
			if( coordSysString.equals( SHORT_COORD_SYS[ i ] ) )
					return i ;
		}
		
		for( int i = 0 ; i < COORD_SYS.length ; i++ )
		{
			if( coordSysString.equals( COORD_SYS[ i ] ) )
					return i ;
		}

		return -1;
	}

	/**
     * Get the string representing a coordinate system from its associated int.
     * If the int is out of range, return null.
     */
	public static String getSystem( int coordSysInt )
	{
		if( ( coordSysInt < 0 ) || ( coordSysInt >= COORD_SYS.length ) )
			return null;
		return COORD_SYS[ coordSysInt ];
	}

	/**
     * Get the string representing the x axis label for a given coordinate
     * system from its associated int. If the int is out of range, return null.
     */
	public static String getXAxisLabel( int coordSysInt )
	{
		if( ( coordSysInt < 0 ) || ( coordSysInt >= X_AXIS_LABEL.length ) )
			return null;
		return X_AXIS_LABEL[ coordSysInt ];
	}

	/**
     * Get the string representing the y axis label for a given coordinate
     * system from its associated int. If the int is out of range, return null.
     */
	public static String getYAxisLabel( int coordSysInt )
	{
		if( ( coordSysInt < 0 ) || ( coordSysInt >= Y_AXIS_LABEL.length ) )
			return null;
		return Y_AXIS_LABEL[ coordSysInt ];
	}
}