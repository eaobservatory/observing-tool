// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util ;

/**
 * A utility class for angles.
 */
public final class Angle
{
	private static double _ERROR = 0.000001 ;
	private static final double twoPI = Math.PI * 2. ;

	/**
     * Convert from degrees to radians.
     */
	public static double degreesToRadians( double degrees )
	{
		return degrees * Math.PI / 180. ;
	}

	/**
     * Convert from radians to degrees.
     */
	public static double radiansToDegrees( double radians )
	{
		return radians * 180. / Math.PI ;
	}

	/**
     * Given an angle in degrees, determine whether the angle is very close to
     * zero.
     */
	public static boolean almostZeroDegrees( double angle )
	{
		if( ( angle >= -_ERROR ) && ( angle <= _ERROR ) )
			return true ;
		if( ( angle >= ( 360 - _ERROR ) ) && ( angle <= ( 360 + _ERROR ) ) )
			return true ;
		return false ;
	}

	/**
     * Given an angle in radians, determine whether the angle is very close to
     * zero.
     */
	public static boolean almostZeroRadians( double angle )
	{
		if( ( angle >= -_ERROR ) && ( angle <= _ERROR ) )
			return true ;

		if( ( angle >= ( twoPI - _ERROR ) ) && ( angle <= ( twoPI + _ERROR ) ) )
			return true ;

		return false ;
	}

	//
	// Is the number almost zero? This is a helper used to get the sin and
	// cos of angles correct when they should be zero...
	// (e.g., Math.cos(Math.PI) is 6.12323e-17).
	//
	private static boolean _almostZero( double n )
	{
		return ( n >= -_ERROR ) && ( n <= _ERROR ) ;
	}

	/**
     * Get the sin of the angle (in radians)
     */
	public static double sinRadians( double angle )
	{
		double d = Math.sin( angle ) ;
		if( _almostZero( d ) )
			return 0 ;

		return d ;
	}

	/**
     * Get the cos of the angle (in radians)
     */
	public static double cosRadians( double angle )
	{
		double d = Math.cos( angle ) ;
		if( _almostZero( d ) )
			return 0 ;

		return d ;
	}

	/**
     * Get the atan of the angle (in radians).
     */
	public static double atanRadians( double angle )
	{
		double d = Math.atan( angle ) ;
		if( _almostZero( d ) )
			return 0 ;

		return d ;
	}

	/**
     * Given an arbitrary angle (in degrees), return an equivalent angle in
     * degrees between 0 (inclusive) and 360 (exclusive).
     */
	public static double normalizeDegrees( double in )
	{
		double out = in ;
		if( in < 0 )
		{
			int t = ( ( int )( -in / 360. ) ) + 1 ;
			out = in + ( ( double )( 360 * t ) ) ;
		}
		else if( in >= 360. )
		{
			int t = ( int )( in / 360. ) ;
			out = in - ( ( double )( 360 * t ) ) ;
		}
		if( almostZeroDegrees( out ) )
		{
			return 0. ;
		}
		return out ;
	}

	/**
     * Given an angle in radians, return an equivalent angle between 0 and 2PI.
     * If the angle is 2PI, 0 is returned.
     */
	public static double normalizeRadians( double radians )
	{
		double abs = Math.abs( radians ) ;

		if( abs >= twoPI )
			abs -= ( ( int )( abs / twoPI ) ) * twoPI ;
		
		if( almostZeroRadians( abs ) )
			radians = 0.0 ;
		else if( radians > 0 )
			radians = abs ;
		else
			radians = twoPI - abs ;

		return radians ;
	}

	/**
     * Convert a string to a double, returning 0.0 if the string is not
     * formatted properly.
     */
	public static double stringToAngle( String angle )
	{
		double val = 0 ;
		try
		{
			val = Double.valueOf( angle ) ;
		}
		catch( NumberFormatException ex ){}
		return val ;
	}
}
