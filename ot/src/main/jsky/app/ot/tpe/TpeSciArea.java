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
package jsky.app.ot.tpe ;

import jsky.app.ot.fits.gui.FitsImageInfo ;
import gemini.sp.obsComp.SpInstObsComp ;

import jsky.app.ot.util.ScreenMath ;

import java.awt.Polygon ;

import gemini.util.PolygonD ;

/**
 * Describes the science area and facilitates drawing, rotating it.
 */
public class TpeSciArea
{
	public double width ;
	public double height ;
	public double posAngleRadians ;
	public double skyCorrection ;
	public double radius ;

	// scratch work variable
	private PolygonD _pd ;

	public static final int RECTANGULAR = 0 ;
	public static final int CIRCULAR = 1 ;
	private int _shape = RECTANGULAR ;

	/** Number of vertices of the Polygon that approximates the circle. */
	private static final int CIRCLE_NPOINTS = 64 ;

	/**
	 *
	 */
	public TpeSciArea()
	{
		_pd = new PolygonD() ;
		_pd.xpoints = new double[ 5 ] ;
		_pd.ypoints = new double[ 5 ] ;
		_pd.npoints = 5 ;
	}

	/**
	 * Update the SciArea fields, returning true iff changes were made.
	 */
	public boolean update( SpInstObsComp spInst , FitsImageInfo fii )
	{
		double[] ds = spInst.getScienceArea() ;

		if( ds != null )
		{
			// Only one element in array: Science area is circular.
			if( ds.length == 1 )
			{
				_shape = CIRCULAR ;
				double r = ds[ 0 ] * fii.pixelsPerArcsec ;
	
				if( r != this.radius )
				{
					this.radius = r ;
					return true ;
				}
			}
			/*
			 * Two elements in array: Science area is recangular.
			 * There might be more than two elements in the array, e.g. for a multi-detector
			 * footprint (e.g. orac.ukirt.inst.SpInstWFCAM.getScienceArea()) but this class does
			 * currently not deal with them. The multi-detector footprints are drawn in the class
			 * jsky.app.ot.tpe.feat.TpeSciAreaFeature without using this class (jsky.app.ot.tpe.TpeSciArea).
			 */
			else
			{
				_shape = RECTANGULAR ;
				double w , h , posAngle , sky ;
	
				w = ds[ 0 ] * fii.pixelsPerArcsec ;
				h = ds[ 1 ] * fii.pixelsPerArcsec ;
	
				posAngle = spInst.getPosAngleRadians() ;
				sky = fii.theta ;
	
				// Update the instance variables if necessary.
				if( ( w != this.width ) || ( h != this.height ) || ( posAngle != this.posAngleRadians ) || ( sky != this.skyCorrection ) )
				{
					this.width = w ;
					this.height = h ;
					this.posAngleRadians = posAngle ;
					this.skyCorrection = sky ;
					return true ;
				}
			}
		}

		return false ;
	}

	/**
	 * Get an AWT Polygon object representing the science area at the
	 * given x, y location, taking into account rotation.
	 */
	public Polygon getPolygonAt( double x , double y )
	{
		if( _shape == CIRCULAR )
		{
			return getPolygonDAt( x , y ).getAWTPolygon() ;
		}
		else
		{
			double hw = width / 2. ;
			double hh = height / 2. ;

			PolygonD pd = _pd ;
			double[] xpoints = pd.xpoints ;
			double[] ypoints = pd.ypoints ;

			xpoints[ 0 ] = x - hw ;
			xpoints[ 1 ] = x + hw ;
			ypoints[ 0 ] = y - hh ;
			ypoints[ 1 ] = y - hh ;

			xpoints[ 2 ] = x + hw ;
			xpoints[ 3 ] = x - hw ;
			ypoints[ 2 ] = y + hh ;
			ypoints[ 3 ] = y + hh ;

			xpoints[ 4 ] = xpoints[ 0 ] ;
			ypoints[ 4 ] = ypoints[ 0 ] ;

			ScreenMath.rotateRadians( pd , posAngleRadians + skyCorrection , x , y ) ;
			return pd.getAWTPolygon() ;
		}
	}

	/**
	 * Get a PolygonD object representing the science area at the
	 * given x, y location, taking into account rotation.
	 */
	public PolygonD getPolygonDAt( double x , double y )
	{
		if( _shape == CIRCULAR )
		{
			double[] xpoints = new double[ CIRCLE_NPOINTS ] ;
			double[] ypoints = new double[ CIRCLE_NPOINTS ] ;

			double a ;

			for( int i = 0 ; i < CIRCLE_NPOINTS ; i++ )
			{
				a = ( ( Math.PI / ( CIRCLE_NPOINTS / 2 ) ) * i ) + ( Math.PI / CIRCLE_NPOINTS ) ;

				xpoints[ i ] = ( radius * Math.cos( a ) ) + x ;
				ypoints[ i ] = ( radius * Math.sin( a ) ) + y ;
			}

			return new PolygonD( xpoints , ypoints , CIRCLE_NPOINTS ) ;
		}
		else
		{
			double hw = width / 2. ;
			double hh = height / 2. ;

			PolygonD pd = _pd ;
			double[] xpoints = pd.xpoints ;
			double[] ypoints = pd.ypoints ;

			xpoints[ 0 ] = x - hw ;
			xpoints[ 1 ] = x + hw ;
			ypoints[ 0 ] = y - hh ;
			ypoints[ 1 ] = y - hh ;

			xpoints[ 2 ] = x + hw ;
			xpoints[ 3 ] = x - hw ;
			ypoints[ 2 ] = y + hh ;
			ypoints[ 3 ] = y + hh ;

			xpoints[ 4 ] = xpoints[ 0 ] ;
			ypoints[ 4 ] = ypoints[ 0 ] ;

			ScreenMath.rotateRadians( pd , posAngleRadians + skyCorrection , x , y ) ;
			return new PolygonD( pd ) ;
		}
	}

	/**
	 * Returns shape of science area.
	 *
	 * @return {@link #RECTANGULAR} or {@link #CIRCULAR}
	 */
	public int getShape()
	{
		return _shape ;
	}
}
