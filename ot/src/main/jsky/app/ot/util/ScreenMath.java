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
//
// $Id$
//
package jsky.app.ot.util ;

import java.awt.geom.Point2D ;
import gemini.util.Angle ;
import gemini.util.PolygonD ;

/**
 * Routines for dealing with screen coordinates.
 */
public class ScreenMath
{
	/**
	 * Rotate a point through a given angle about a given point.
	 */
	public static Point2D.Double rotateRadians( double x , double y , double angle , double xBase , double yBase )
	{
		if( angle == 0. )
			return new Point2D.Double( x , y ) ;

		// Translate x,y to be relative to the origin, where y is increasing toward the top instead of toward the bottom.
		double x0 = x - xBase ;
		double y0 = yBase - y ;

		// Rotate the point and translate it back.
		double sin = Angle.sinRadians( angle ) ;
		double cos = Angle.cosRadians( angle ) ;

		x = xBase + ( x0 * cos - y0 * sin ) ;
		y = yBase - ( x0 * sin + y0 * cos ) ;

		return new Point2D.Double( x , y ) ;
	}

	/**
	 * Rotate a polygon through a given angle about a given point.
	 */
	public static void rotateRadians( PolygonD p , double angle , double xBase , double yBase )
	{
		if( angle != 0. )
		{
			// Remember the sin and cos of the angle.
			double sin = Angle.sinRadians( angle ) ;
			double cos = Angle.cosRadians( angle ) ;
	
			// Rotate each point.
			for( int i = 0 ; i < p.npoints ; ++i )
			{
				// Translate the point to be relative to the origin, and make y
				// increase toward the top instead of toward the bottom.
				double x0 = p.xpoints[ i ] - xBase ;
				double y0 = yBase - p.ypoints[ i ] ;
	
				// Rotate the point and translate it back.
				p.xpoints[ i ] = xBase + ( x0 * cos - y0 * sin ) ;
				p.ypoints[ i ] = yBase - ( x0 * sin + y0 * cos ) ;
			}
		}
	}
}
