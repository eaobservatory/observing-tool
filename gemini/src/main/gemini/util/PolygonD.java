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

import java.awt.Polygon ;

/**
 * Like a java.awt.Polygon, but instead of integer coordinates, doubles are
 * used.
 */
public class PolygonD implements Cloneable
{

	public int npoints = 0 ;
	public double xpoints[] = new double[ 4 ] ;
	public double ypoints[] = new double[ 4 ] ;
	private Polygon _awtPolygon ;

	/**
     * Creates an empty polygon.
     */
	public PolygonD(){}

	/**
     * Creates a new PolygonD copying from the given PolygonD.
     */
	public PolygonD( PolygonD srcPD )
	{
		this.npoints = srcPD.npoints ;
		this.xpoints = new double[ this.npoints ] ;
		this.ypoints = new double[ this.npoints ] ;
		System.arraycopy( srcPD.xpoints , 0 , this.xpoints , 0 , this.npoints ) ;
		System.arraycopy( srcPD.ypoints , 0 , this.ypoints , 0 , this.npoints ) ;
	}

	/**
     * Initializes a PolygonD from the specified parameters.
     * 
     * @param xpoints
     *            the array of x coordinates
     * @param ypoints
     *            the array of y coordinates
     * @param npoints
     *            the total number of points in the Polygon
     */
	public PolygonD( double[] xpoints , double[] ypoints , int npoints )
	{
		this.npoints = npoints ;
		this.xpoints = new double[ npoints ] ;
		this.ypoints = new double[ npoints ] ;
		System.arraycopy( xpoints , 0 , this.xpoints , 0 , npoints ) ;
		System.arraycopy( ypoints , 0 , this.ypoints , 0 , npoints ) ;
	}

	public Polygon getAWTPolygon()
	{
		if( ( _awtPolygon == null ) || ( _awtPolygon.npoints != npoints ) )
			_awtPolygon = new Polygon( new int[ npoints ] , new int[ npoints ] , npoints ) ;

		for( int i = 0 ; i < npoints ; ++i )
		{
			if( _awtPolygon.xpoints[ i ] < 0.0 )
				_awtPolygon.xpoints[ i ] = ( int )( xpoints[ i ] - 0.5 ) ;
			else
				_awtPolygon.xpoints[ i ] = ( int )( xpoints[ i ] + 0.5 ) ;

			if( _awtPolygon.ypoints[ i ] < 0.0 )
				_awtPolygon.ypoints[ i ] = ( int )( ypoints[ i ] - 0.5 ) ;
			else
				_awtPolygon.ypoints[ i ] = ( int )( ypoints[ i ] + 0.5 ) ;
		}
		return _awtPolygon ;
	}

	public String toString()
	{
		return getClass().getName() + "[x=" + xpoints + ", y=" + ypoints + "]" ;
	}

	public Object clone()
	{
		PolygonD pd ;
		try
		{
			pd = ( PolygonD )super.clone() ;
		}
		catch( CloneNotSupportedException ex )
		{
			return null ;
		}
		pd.xpoints = xpoints.clone() ;
		pd.ypoints = ypoints.clone() ;
		return pd ;
	}
}
