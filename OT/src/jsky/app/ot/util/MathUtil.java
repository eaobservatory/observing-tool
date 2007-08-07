// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.util;

/**
 * A collection of useful math routines that seem to have been
 * forgotten or not completely implemented in the JDK.
 */
public class MathUtil
{
	/**
	 * Convert the given double to the closest double with the given
	 * precision.
	 */
	public static double round( double d , int precision )
	{
		int mult = ( int )Math.pow( 10 , precision );
		return ( ( double )Math.round( d * mult ) ) / mult;
	}

	/**
	 * Convert the given double to a String with the given precision.
	 */
	public static String doubleToString( double d , int precision )
	{
		StringBuffer out = new StringBuffer();

		if( d < 0 )
			out.append( '-' );
		int i = ( int )d;
		out.append( i );
		d = d - ( double )i;
		out.append( '.' );
		for( int j = 0 ; j < precision ; ++j )
		{
			d = d * 10. ;
			i = ( int )d;
			out.append( i );
			d = d - ( double )i;
		}
		return out.toString();
	}

	/**
	 * Convert the given double to a String with 10 decimal places.
	 */
	public static String doubleToString( double d )
	{
		return doubleToString( d , 10 );
	}
}
