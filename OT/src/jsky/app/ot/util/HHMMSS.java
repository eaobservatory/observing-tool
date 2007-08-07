// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
/*
 * NCSA Horizon Image Browser
 * Project Horizon
 * National Center for Supercomputing Applications
 * University of Illinois at Urbana-Champaign
 * 605 E. Springfield, Champaign IL 61820
 * horizon@ncsa.uiuc.edu
 *
 * Copyright (C) 1996, Board of Trustees of the University of Illinois
 *
 * NCSA Horizon software, both binary and source (hereafter, Software) is
 * copyrighted by The Board of Trustees of the University of Illinois
 * (UI), and ownership remains with the UI.
 *
 * You should have received a full statement of copyright and
 * conditions for use with this package; if not, a copy may be
 * obtained from the above address.  Please see this statement
 * for more details.
 *
 */
package jsky.app.ot.util;

import java.util.StringTokenizer;
import jsky.app.ot.util.Angle;

/**
 * Support for converting angles in hours:minutes:seconds format over the
 * circular range 0, 24 hours.
 */
public final class HHMMSS
{
	public static final String MYNAME = "Time-Angle Coordinate Axis Position Formatter";

	/**
	 * Convert from an RA in degrees to a String in HH:MM:SS format.
	 */
	public static String valStr( double degrees , int prec )
	{
		// Make sure the angle is between 0 (inclusive) and 360 (exclusive)
		degrees = Angle.normalizeDegrees( degrees );

		double tmp = degrees / 15. ;

		int hh = ( int )tmp;
		tmp = ( tmp - ( double )hh ) * 60. ;
		int mm = ( int )tmp;
		double ss = ( tmp - ( double )mm ) * 60. ;

		// correct for formating errors caused by rounding
		if( ss > 59.99999 )
		{
			ss = 0;
			mm += 1;
			if( mm >= 60 )
			{
				mm = 0;
				hh += 1;
				if( hh >= 24 )
					hh -= 24. ;
			}
		}

		StringBuffer out = new StringBuffer();
		out.append( hh );
		if( prec == -2 )
			return out.toString();

		out.append( ':' );
		if( mm < 10 )
			out.append( '0' );
		out.append( mm );
		if( prec == -1 )
			return out.toString();

		out.append( ':' );

		// Ignoring prec for now.
		ss = ( ( double )Math.round( ss * 1000. ) ) / 1000. ;
		if( ss < 10 )
			out.append( '0' );
		out.append( ss );

		return out.toString();
	}

	/**
	 * Convert from an RA in degrees to a String in HH:MM:SS format.
	 */
	public static String valStr( double degrees )
	{
		return valStr( degrees , -3 );
	}

	/**
	 * Convert from an RA in HH:MM:SS string format to degrees.
	 */
	public static double valueOf( String s ) throws NumberFormatException
	{
		if( s == null )
			throw new NumberFormatException( s );

		// Determine the sign from the (trimmed) string
		s = s.trim();
		if( s.length() == 0 )
			throw new NumberFormatException( s );
		
		int sign = 1;
		if( s.charAt( 0 ) == '-' )
		{
			sign = -1;
			s = s.substring( 1 );
		}

		// Parse the string into values for hours, min, and sec
		double[] vals = { 0. , 0. , 0. };
		StringTokenizer tok = new StringTokenizer( s , ": " );
		for( int i = 0 ; i < 3 && tok.hasMoreTokens() ; i++ )
			vals[ i ] = Double.valueOf( tok.nextToken() ).doubleValue();

		// Convert HH:MM:SS to degrees
		double out = sign * ( vals[ 0 ] + vals[ 1 ] / 60. + vals[ 2 ] / 3600. ) * 15. ;
		return Angle.normalizeDegrees( out );
	}

	/**
	 * For testing.
	 */
	public static void main( String args[] )
	{
		for( int i = 0 ; i < args.length ; ++i )
		{
			double converted = HHMMSS.valueOf( args[ i ] );
			String back = HHMMSS.valStr( converted );
			System.out.println( args[ i ] + " = " + converted );
			System.out.println( converted + " = " + back );
		}
	}
}
