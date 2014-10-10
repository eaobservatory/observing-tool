// Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

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
