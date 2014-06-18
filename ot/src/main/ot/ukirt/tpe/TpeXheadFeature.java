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
package ot.ukirt.tpe ;

import java.awt.Color ;
import java.awt.Graphics ;
import java.awt.Rectangle ;

import jsky.app.ot.tpe.TpeImageFeature ;
import jsky.app.ot.tpe.TpeImageWidget ;
import orac.ukirt.inst.SpUKIRTInstObsComp ;

import jsky.app.ot.fits.gui.FitsImageInfo ;

import gemini.util.PolygonD ;
import gemini.util.Angle ;

/**
 * Draws the range of the Xhead on UKIRT.
 */
public class TpeXheadFeature extends TpeImageFeature
{

	// For now, just make these constants static members.  Eventually read from a configuration file?  Here we assume a single circle
	// although the are in fact two centres: one is refracted slightly by the dichroic and is for the segments between the `H' of the
	// dichroic.  For practical purposes a single circle is fine. The units for all is arcseconds.
	public static final double XHEAD_OUTER_RADIUS = 274.6 ;
	public static final double XHEAD_INNER_RADIUS = 265. ;
	public static final double XHEAD_X_CENTRE = 1.4 ;
	public static final double XHEAD_Y_CENTRE = 11.2 ;

	// Define crosshead limits in arcseconds.
	public static final double NORTH_LIMIT = 234.1 ;
	public static final double EAST_LIMIT = 234.1 ;
	public static final double SOUTH_LIMIT = -234.1 ;
	public static final double WEST_LIMIT = -234.1 ;
	private Rectangle _xheadi = new Rectangle() ;
	private Rectangle _xheado = new Rectangle() ;
	private boolean _valid = false ;
	private PolygonD _oobtLine ;
	private PolygonD _oobbLine ;
	private PolygonD _ooblLine ;
	private PolygonD _oobrLine ;

	/**
	 * Construct the feature with its name and description. 
	 */
	public TpeXheadFeature()
	{
		super( "Xhead" , "Range of the crosshead movement." ) ;
	}

	/**
	 * Reinit.
	 */
	public void reinit( TpeImageWidget iw , FitsImageInfo fii )
	{
		super.reinit( iw , fii ) ;
		_valid = false ;
	}

	/**
	 * Calculate the polygon describing the screen location of the
	 * crosshead area.
	 */
	private void _calc( FitsImageInfo fii )
	{
		int radius , size ;
		double _NLimitRoot , _SLimitRoot , _WLimitRoot , _ELimitRoot ;

		// Get the instrument aperture X & Y.  These offset the origin of the x-head range.
		double xoff = 0. ;
		double yoff = 0. ;

		SpUKIRTInstObsComp _inst = ( SpUKIRTInstObsComp )_iw.getInstrumentItem() ;
		if( _inst != null )
		{
			xoff = -1. * ( _inst.getInstApXra() + _inst.getInstPntgOffsetCH() ) ;
			yoff = -1. * ( _inst.getInstApYdec() + _inst.getInstPntgOffsetID() ) ;
		}

		// Obtain the origin in image pixel co-ordinates.
		int xbase = ( int )fii.baseScreenPos.x ;
		int ybase = ( int )fii.baseScreenPos.y ;

		// Find the displacements of the outer circle allowing for the
		// instrument apertures and crosshead centre.  Combine arcsecond units
		// together before scaling to display pixels to reduce rounding errors.
		int xdisp = ( int )( fii.pixelsPerArcsec * ( xoff - XHEAD_OUTER_RADIUS - XHEAD_X_CENTRE ) + 0.5 ) ;
		int ydisp = ( int )( fii.pixelsPerArcsec * ( yoff - XHEAD_OUTER_RADIUS - XHEAD_Y_CENTRE ) + 0.5 ) ;

		// Specify the parameters of the outer circle.
		radius = ( int )( fii.pixelsPerArcsec * XHEAD_OUTER_RADIUS + 0.5 ) ;
		size = 2 * radius ;

		// Set the drawOval parameters.
		_xheado.x = xbase + xdisp ;
		_xheado.y = ybase + ydisp ;
		_xheado.width = size ;
		_xheado.height = size ;

		// Find the displacements of the inner circle allowing for the
		// instrument apertures and crosshead centre.  Combine arcsecond units
		// together before scaling to display pixels to reduce rounding errors.
		xdisp = ( int )( fii.pixelsPerArcsec * ( xoff - XHEAD_INNER_RADIUS - XHEAD_X_CENTRE ) + 0.5 ) ;
		ydisp = ( int )( fii.pixelsPerArcsec * ( yoff - XHEAD_INNER_RADIUS - XHEAD_Y_CENTRE ) + 0.5 ) ;

		// Specify the parameters of the inner circle.
		radius = ( int )( fii.pixelsPerArcsec * XHEAD_INNER_RADIUS + 0.5 ) ;
		size = 2 * radius ;

		// Set the drawOval parameters.
		_xheadi.x = xbase + xdisp ;
		_xheadi.y = ybase + ydisp ;
		_xheadi.width = size ;
		_xheadi.height = size ;
		_valid = true ;

		// Specify the parameters of the rectangular limits.
		// -------------------------------------------------

		// Find the intersection points of the edge of the square accessible
		// region with the inner circle.

		// Create polygons for each out of bounds line.
		if( _oobtLine == null )
		{
			_oobtLine = new PolygonD() ;
			_oobtLine.xpoints = new double[ 2 ] ;
			_oobtLine.ypoints = new double[ 2 ] ;
			_oobtLine.npoints = 2 ;
		}
		if( _oobbLine == null )
		{
			_oobbLine = new PolygonD() ;
			_oobbLine.xpoints = new double[ 2 ] ;
			_oobbLine.ypoints = new double[ 2 ] ;
			_oobbLine.npoints = 2 ;
		}
		if( _ooblLine == null )
		{
			_ooblLine = new PolygonD() ;
			_ooblLine.xpoints = new double[ 2 ] ;
			_ooblLine.ypoints = new double[ 2 ] ;
			_ooblLine.npoints = 2 ;
		}
		if( _oobrLine == null )
		{
			_oobrLine = new PolygonD() ;
			_oobrLine.xpoints = new double[ 2 ] ;
			_oobrLine.ypoints = new double[ 2 ] ;
			_oobrLine.npoints = 2 ;
		}

		// Apply to the instrument apertures to the crosshead limits.
		double _NLimit = NORTH_LIMIT ;
		double _ELimit = EAST_LIMIT ;
		double _SLimit = SOUTH_LIMIT ;
		double _WLimit = WEST_LIMIT ;

		// Find the two roots for the intersection of each line of the  rectangular limits with the inner circle.  
		// The circle is not quite concentric with the optical axis, being offset by (XHEAD_X_CENTRE, XHEAD_Y_CENTRE).  
		// Record the co-ordinates of each intersection. Convert to the integer pixel co-ordinate system of the graphics.

		// Line for y = _NLimit
		_NLimitRoot = Math.sqrt( Math.max( ( XHEAD_INNER_RADIUS * XHEAD_INNER_RADIUS ) - ( ( -_NLimit - XHEAD_Y_CENTRE ) * ( -_NLimit - XHEAD_Y_CENTRE ) ) , 0. ) ) ;

		_oobtLine.xpoints[ 0 ] = ( int )( fii.pixelsPerArcsec * ( -XHEAD_X_CENTRE + Math.abs( _NLimitRoot ) + xoff + 0.5 ) + xbase ) ;
		_oobtLine.ypoints[ 0 ] = ( int )( fii.pixelsPerArcsec * ( _NLimit + yoff + 0.5 ) + ybase ) ;
		_oobtLine.xpoints[ 1 ] = ( int )( fii.pixelsPerArcsec * ( -XHEAD_X_CENTRE - Math.abs( _NLimitRoot ) + xoff + 0.5 ) + xbase ) ;
		_oobtLine.ypoints[ 1 ] = _oobtLine.ypoints[ 0 ] ;

		// Line for y = _SLimit
		_SLimitRoot = Math.sqrt( Math.max( ( XHEAD_INNER_RADIUS * XHEAD_INNER_RADIUS ) - ( ( -_SLimit - XHEAD_Y_CENTRE ) * ( -_SLimit - XHEAD_Y_CENTRE ) ) , 0. ) ) ;

		_oobbLine.xpoints[ 0 ] = ( int )( fii.pixelsPerArcsec * ( -XHEAD_X_CENTRE + Math.abs( _SLimitRoot ) + xoff + 0.5 ) + xbase ) ;
		_oobbLine.ypoints[ 0 ] = ( int )( fii.pixelsPerArcsec * ( _SLimit + yoff + 0.5 ) + ybase ) ;
		_oobbLine.xpoints[ 1 ] = ( int )( fii.pixelsPerArcsec * ( -XHEAD_X_CENTRE - Math.abs( _SLimitRoot ) + xoff + 0.5 ) + xbase ) ;
		_oobbLine.ypoints[ 1 ] = _oobbLine.ypoints[ 0 ] ;

		// Line for x = _WLimit
		_WLimitRoot = Math.sqrt( Math.max( ( XHEAD_INNER_RADIUS * XHEAD_INNER_RADIUS ) - ( ( -_WLimit - XHEAD_X_CENTRE ) * ( -_WLimit - XHEAD_X_CENTRE ) ) , 0. ) ) ;

		_oobrLine.xpoints[ 0 ] = ( int )( fii.pixelsPerArcsec * ( _WLimit + xoff + 0.5 ) + xbase ) ;
		_oobrLine.ypoints[ 0 ] = ( int )( fii.pixelsPerArcsec * ( -XHEAD_Y_CENTRE + Math.abs( _WLimitRoot ) + yoff + 0.5 ) + ybase ) ;

		_oobrLine.xpoints[ 1 ] = _oobrLine.xpoints[ 0 ] ;
		_oobrLine.ypoints[ 1 ] = ( int )( fii.pixelsPerArcsec * ( -XHEAD_Y_CENTRE - Math.abs( _WLimitRoot ) + yoff + 0.5 ) + ybase ) ;

		// Line for x = _ELimit
		_ELimitRoot = Math.sqrt( Math.max( ( XHEAD_INNER_RADIUS * XHEAD_INNER_RADIUS ) - ( ( -_ELimit - XHEAD_X_CENTRE ) * ( -_ELimit - XHEAD_X_CENTRE ) ) , 0. ) ) ;

		_ooblLine.xpoints[ 0 ] = ( int )( fii.pixelsPerArcsec * ( _ELimit + xoff + 0.5 ) + xbase ) ;
		_ooblLine.ypoints[ 0 ] = ( int )( fii.pixelsPerArcsec * ( -XHEAD_Y_CENTRE + Math.abs( _ELimitRoot ) + yoff + 0.5 ) + ybase ) ;

		_ooblLine.xpoints[ 1 ] = _ooblLine.xpoints[ 0 ] ;
		_ooblLine.ypoints[ 1 ] = ( int )( fii.pixelsPerArcsec * ( -XHEAD_Y_CENTRE - Math.abs( _ELimitRoot ) + yoff + 0.5 ) + ybase ) ;

		// Rotate the crosshead lines to align with cardinmal directions.
		_iw.skyRotate( _oobtLine , Angle.degreesToRadians( 0. ) ) ;
		_iw.skyRotate( _oobbLine , Angle.degreesToRadians( 0. ) ) ;
		_iw.skyRotate( _ooblLine , Angle.degreesToRadians( 0. ) ) ;
		_iw.skyRotate( _oobrLine , Angle.degreesToRadians( 0. ) ) ;

		_valid = true ;
	}

	/**
	 * The position angle has changed. Here this is actually used to check changes
	 * in other things, particularly the instrument aperture.
	 */
	public void posAngleUpdate( FitsImageInfo fii )
	{
		_valid = false ;
	}

	/**
	 * Draw the two circles, and four lines of a square within the inner circle.
	 */
	public void draw( Graphics g , FitsImageInfo fii )
	{
		if( !_valid )
			_calc( fii ) ;

		g.setColor( Color.magenta ) ;
		g.drawOval( _xheado.x , _xheado.y , _xheado.width , _xheado.height ) ;
		g.drawOval( _xheadi.x , _xheadi.y , _xheadi.width , _xheadi.height ) ;
		g.drawPolygon( _oobtLine.getAWTPolygon() ) ;
		g.drawPolygon( _oobbLine.getAWTPolygon() ) ;
		g.drawPolygon( _ooblLine.getAWTPolygon() ) ;
		g.drawPolygon( _oobrLine.getAWTPolygon() ) ;
	}
}
