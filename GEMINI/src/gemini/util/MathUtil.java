// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util ;

/**
 * A collection of useful math routines that seem to have been forgotten or not
 * completely implemented in the JDK.
 */
public class MathUtil
{

	/**
     * Convert the given double to the closest double with the given precision.
     */
	public static double round( double d , int precision )
	{
		int mult = ( int )Math.pow( 10 , precision ) ;
		return ( ( double )Math.round( d * mult ) ) / mult ;
	}

	/**
     * Convert the given double to a String with the given precision.
     */
	public static String doubleToString( double d , int precision )
	{
		StringBuffer out = new StringBuffer() ;

		if( d < 0 )
			out.append( '-' ) ;
		int i = ( int )d ;
		out.append( i ) ;
		d = d - ( double )i ;
		out.append( '.' ) ;
		for( int j = 0 ; j < precision ; ++j )
		{
			d = d * 10. ;
			i = ( int )d ;
			out.append( i ) ;
			d = d - ( double )i ;
		}
		return out.toString() ;
	}

	/**
     * Convert the given double to a String with 10 decimal places.
     */
	public static String doubleToString( double d )
	{
		return doubleToString( d , 10 ) ;
	}

	/**
	 * Linear interpolation
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x
	 * @return
	 */
	public static double linterp( double x1 , double y1 , double x2 , double y2 , double x )
	{
		double slope = ( y2 - y1 ) / ( x2 - x1 ) ;
		double value = slope * x + y1 - slope * x1 ;
		return value ;
	}
}
