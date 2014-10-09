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

package jsky.app.ot.tpe.feat ;

import java.awt.Color ;
import java.awt.Graphics ;
import java.awt.Polygon ;

import jsky.app.ot.gui.DrawUtil ;

import gemini.sp.obsComp.SpInstObsComp ;

import jsky.app.ot.fits.gui.FitsImageInfo ;
import jsky.app.ot.fits.gui.FitsMouseEvent ;

import jsky.app.ot.tpe.TpeImageFeature ;
import jsky.app.ot.tpe.TpeDraggableFeature ;
import jsky.app.ot.tpe.TpeImageWidget ;
import jsky.app.ot.tpe.TpeSciArea ;
import java.awt.geom.Point2D ;

import gemini.util.Angle ;
import gemini.util.PolygonD ;

/**
 * An implementation class used to simplify the job of rotating science
 * areas.
 */
final class TpeSciAreaDragObject
{
	int _xb , _yb ;

	double _angle = 0 ;

	/**
	 * Construct with the base position at (xb,yb).
	 */
	public TpeSciAreaDragObject( int xb , int yb , int x , int y )
	{
		_xb = xb ;
		_yb = yb ;
		_angle = getAngle( x , y ) ;
	}

	public double nextAngleDiff( int x , int y )
	{
		double angle = getAngle( x , y ) ;

		double val = angle - _angle ;
		_angle = angle ;

		return val ;
	}

	public double getAngle( int x , int y )
	{
		double angle ;

		// All the points are in screen coordinates, which means y increases down
		// This makes x and y relative to the origin in a right side up frame.
		int xp = x - _xb ;
		int yp = _yb - y ;

		int xa = Math.abs( xp ) ;
		int ya = Math.abs( yp ) ;

		if( xa == 0 )
		{
			if( yp >= 0 )
				return Math.PI * 0.5 ;
			else
				return Math.PI * 1.5 ;
		}

		angle = Angle.atanRadians( ( ( double )ya ) / ( ( double )xa ) ) ;

		if( ( xp > 0 ) && ( yp >= 0 ) )
			return angle ;

		if( ( xp < 0 ) && ( yp >= 0 ) )
			return Math.PI - angle ;

		if( ( xp < 0 ) && ( yp < 0 ) )
			return Math.PI + angle ;

		return Math.PI * 2. - angle ;
	}
}

/**
 * Draws the Science Area, the detector or slit.
 */
public class TpeSciAreaFeature extends TpeImageFeature implements TpeDraggableFeature
{
	private TpeSciArea _sciArea ;
	private PolygonD _sciAreaPD ;
	private PolygonD _tickMarkPD ;
	private SpInstObsComp _instItem ;
	private boolean _valid = false ;
	private TpeSciAreaDragObject _dragObject ;
	private boolean _dragging = false ;
	private int _dragX ;
	private int _dragY ;

	/**
	 * Construct the feature with its name and description. 
	 */
	public TpeSciAreaFeature()
	{
		super( "Sci Area" , "Science area." ) ;
	}

	/**
	 * Reinit.
	 */
	public void reinit( TpeImageWidget iw , FitsImageInfo fii )
	{
		super.reinit( iw , fii ) ;
		_valid = false ;
		return ;
	}

	/**
	 * The position angle has changed.
	 */
	public void posAngleUpdate( FitsImageInfo fii )
	{
		_valid = false ;
	}

	/**
	 * Calculate the polygon describing the screen location of the science area.
	 */
	private boolean _calc( FitsImageInfo fii )
	{
		// Need the instrument to get the science area
		SpInstObsComp spInst = _iw.getInstrumentItem() ;
		if( spInst == null )
			return false ;

		if( _sciArea == null )
		{
			_sciArea = _iw.getSciArea() ;
			if( _sciArea == null )
				return false ;
		}

		boolean updated = _sciArea.update( spInst , fii ) ;

		// Already have the current values
		if( _valid && !updated && ( _sciAreaPD != null ) && ( spInst == _instItem ) )
			return true ;

		_instItem = spInst ;

		double xBase = fii.baseScreenPos.x;
		double yBase = fii.baseScreenPos.y;
		_sciAreaPD = _sciArea.getPolygonDAt( xBase , yBase ) ;

		// Init the _tickMarkPD
		if( _tickMarkPD == null )
		{
			double[] xpoints = new double[ 4 ] ;
			double[] ypoints = new double[ 4 ] ;
			_tickMarkPD = new PolygonD( xpoints , ypoints , 4 ) ;
		}

		double x = xBase ;
		double y = yBase - _sciArea.height / 2. ;

		_tickMarkPD.xpoints[ 0 ] = x ;
		_tickMarkPD.ypoints[ 0 ] = y - MARKER_SIZE * 2 ;

		_tickMarkPD.xpoints[ 1 ] = x - MARKER_SIZE ;
		_tickMarkPD.ypoints[ 1 ] = y - 2 ;

		_tickMarkPD.xpoints[ 2 ] = x + MARKER_SIZE ;
		_tickMarkPD.ypoints[ 2 ] = y - 2 ;

		_tickMarkPD.xpoints[ 3 ] = _tickMarkPD.xpoints[ 0 ] ;
		_tickMarkPD.ypoints[ 3 ] = _tickMarkPD.ypoints[ 0 ] ;

		_iw.skyRotate( _tickMarkPD , _sciArea.posAngleRadians ) ;

		_valid = true ;

		return true ;
	}

	/**
	 * Draw the feature.
	 */
	public void draw( Graphics g , FitsImageInfo fii )
	{
		if( !_calc( fii ) )
			return ;

		g.setColor( Color.cyan ) ;

		// Check whether _instItem is a multi-detector instrument.
		// If it is then draw the footprint directly from the values returned
		// by _instItem.getScienceArea() and do not use _sciArea, _sciAreaPD, _calc etc.
		double[] sciAreaValues = _instItem.getScienceArea() ;
		if( ( sciAreaValues != null ) && ( sciAreaValues.length == 4 ) )
		{
			drawMultiDetectorFootprint( g , sciAreaValues ) ;

			// Currently no tick mark or dragging for multi-detector footprint.
			return ;
		}

		g.drawPolygon( _sciAreaPD.getAWTPolygon() ) ;

		// If the shape is circular then forget about tick mark and dragging.
		if( _sciArea.getShape() == TpeSciArea.CIRCULAR )
			return ;

		g.fillPolygon( _tickMarkPD.getAWTPolygon() ) ;

		if( _dragging )
		{
			// Draw a little above the mouse
			int baseX = _dragX ;
			int baseY = _dragY - 10 ;

			// Draw a string displaying the rotation angle
			String s = _instItem.getPosAngleDegreesStr() ;
			DrawUtil.drawString( g , s , Color.cyan , Color.black , baseX , baseY ) ;
		}
	}

	/**
	 * Draw a multi-detector footprint.
	 */
	public void drawMultiDetectorFootprint( Graphics g , double[] sciAreaValues )
	{
		double width = sciAreaValues[ 0 ] ;
		double height = sciAreaValues[ 1 ] ;
		double xoffset = sciAreaValues[ 2 ] ;
		double yoffset = sciAreaValues[ 3 ] ;

		int[] xpoints = new int[ 4 ] ;
		int[] ypoints = new int[ 4 ] ;

		Point2D.Double p = null ;
		Polygon awtPolygon = null ;

		p = _iw.offsetToImageWidget( xoffset + ( 0.5 * width ) , yoffset + ( 0.5 * height ) ) ;
		xpoints[ 0 ] = ( int )p.x ;
		ypoints[ 0 ] = ( int )p.y ;

		p = _iw.offsetToImageWidget( xoffset + ( 0.5 * width ) , yoffset - ( 0.5 * height ) ) ;
		xpoints[ 1 ] = ( int )p.x ;
		ypoints[ 1 ] = ( int )p.y ;

		p = _iw.offsetToImageWidget( xoffset - ( 0.5 * width ) , yoffset - ( 0.5 * height ) ) ;
		xpoints[ 2 ] = ( int )p.x ;
		ypoints[ 2 ] = ( int )p.y ;

		p = _iw.offsetToImageWidget( xoffset - ( 0.5 * width ) , yoffset + ( 0.5 * height ) ) ;
		xpoints[ 3 ] = ( int )p.x ;
		ypoints[ 3 ] = ( int )p.y ;

		awtPolygon = new Polygon( xpoints , ypoints , xpoints.length ) ;
		g.drawPolygon( awtPolygon ) ;

		p = _iw.offsetToImageWidget( xoffset + ( 0.5 * width ) , -yoffset + ( 0.5 * height ) ) ;
		xpoints[ 0 ] = ( int )p.x ;
		ypoints[ 0 ] = ( int )p.y ;

		p = _iw.offsetToImageWidget( xoffset + ( 0.5 * width ) , -yoffset - ( 0.5 * height ) ) ;
		xpoints[ 1 ] = ( int )p.x ;
		ypoints[ 1 ] = ( int )p.y ;

		p = _iw.offsetToImageWidget( xoffset - ( 0.5 * width ) , -yoffset - ( 0.5 * height ) ) ;
		xpoints[ 2 ] = ( int )p.x ;
		ypoints[ 2 ] = ( int )p.y ;

		p = _iw.offsetToImageWidget( xoffset - ( 0.5 * width ) , -yoffset + ( 0.5 * height ) ) ;
		xpoints[ 3 ] = ( int )p.x ;
		ypoints[ 3 ] = ( int )p.y ;

		awtPolygon = new Polygon( xpoints , ypoints , xpoints.length ) ;
		g.drawPolygon( awtPolygon ) ;

		p = _iw.offsetToImageWidget( -xoffset + ( 0.5 * width ) , -yoffset + ( 0.5 * height ) ) ;
		xpoints[ 0 ] = ( int )p.x ;
		ypoints[ 0 ] = ( int )p.y ;

		p = _iw.offsetToImageWidget( -xoffset + ( 0.5 * width ) , -yoffset - ( 0.5 * height ) ) ;
		xpoints[ 1 ] = ( int )p.x ;
		ypoints[ 1 ] = ( int )p.y ;

		p = _iw.offsetToImageWidget( -xoffset - ( 0.5 * width ) , -yoffset - ( 0.5 * height ) ) ;
		xpoints[ 2 ] = ( int )p.x ;
		ypoints[ 2 ] = ( int )p.y ;

		p = _iw.offsetToImageWidget( -xoffset - ( 0.5 * width ) , -yoffset + ( 0.5 * height ) ) ;
		xpoints[ 3 ] = ( int )p.x ;
		ypoints[ 3 ] = ( int )p.y ;

		awtPolygon = new Polygon( xpoints , ypoints , xpoints.length ) ;
		g.drawPolygon( awtPolygon ) ;

		p = _iw.offsetToImageWidget( -xoffset + ( 0.5 * width ) , yoffset + ( 0.5 * height ) ) ;
		xpoints[ 0 ] = ( int )p.x ;
		ypoints[ 0 ] = ( int )p.y ;

		p = _iw.offsetToImageWidget( -xoffset + ( 0.5 * width ) , yoffset - ( 0.5 * height ) ) ;
		xpoints[ 1 ] = ( int )p.x ;
		ypoints[ 1 ] = ( int )p.y ;

		p = _iw.offsetToImageWidget( -xoffset - ( 0.5 * width ) , yoffset - ( 0.5 * height ) ) ;
		xpoints[ 2 ] = ( int )p.x ;
		ypoints[ 2 ] = ( int )p.y ;

		p = _iw.offsetToImageWidget( -xoffset - ( 0.5 * width ) , yoffset + ( 0.5 * height ) ) ;
		xpoints[ 3 ] = ( int )p.x ;
		ypoints[ 3 ] = ( int )p.y ;

		awtPolygon = new Polygon( xpoints , ypoints , xpoints.length ) ;
		g.drawPolygon( awtPolygon ) ;
	}

	/**
	 * Start dragging the object.
	 */
	public boolean dragStart( FitsMouseEvent fme , FitsImageInfo fii )
	{
		// If the shape is circular then forget about dragging.
		if( _sciArea.getShape() == TpeSciArea.CIRCULAR )
			return false ;

		if( ( _sciAreaPD == null ) || ( _tickMarkPD == null ) )
			return false ;

		_dragObject = null ;

		// See if dragging by the corner
		for( int i = 0 ; i < ( _sciAreaPD.npoints - 1 ) ; ++i )
		{
			int cornerx = ( int )( _sciAreaPD.xpoints[ i ] + 0.5 ) ;
			int cornery = ( int )( _sciAreaPD.ypoints[ i ] + 0.5 ) ;

			int dx = Math.abs( cornerx - fme.xWidget ) ;
			if( dx > MARKER_SIZE )
				continue ;

			int dy = Math.abs( cornery - fme.yWidget ) ;
			if( dy > MARKER_SIZE )
				continue ;

			Point2D.Double p = fii.baseScreenPos ;
			_dragObject = new TpeSciAreaDragObject( ( int )p.x , ( int )p.y , cornerx , cornery ) ;
		}

		// See if dragging by the tick mark (give a couple extra pixels to make it easier to grab)
		if( _dragObject == null )
		{
			int x = ( int )( _tickMarkPD.xpoints[ 0 ] + 0.5 ) ;
			int dx = Math.abs( x - fme.xWidget ) ;
			if( dx <= MARKER_SIZE + 2 )
			{
				int y0 = ( int )( _tickMarkPD.ypoints[ 0 ] + 0.5 ) ;
				int y1 = ( int )( _tickMarkPD.ypoints[ 1 ] + 0.5 ) ;
				int y = ( y0 + y1 ) / 2 ;
				int dy = Math.abs( y - fme.yWidget ) ;
				if( dy <= MARKER_SIZE + 2 )
				{
					Point2D.Double p = fii.baseScreenPos ;
					_dragObject = new TpeSciAreaDragObject( ( int )p.x , ( int )p.y , x , y ) ;
				}
			}
		}

		_dragging = ( _dragObject != null ) ;
		if( _dragging )
		{
			_dragX = fme.xWidget ;
			_dragY = fme.yWidget ;
			_instItem.getAvEditFSM().setEachEditNotifies( false ) ;
		}

		return _dragging ;
	}

	/**
	 * Drag to a new location.
	 */
	public void drag( FitsMouseEvent fme )
	{
		// If the shape is circular then forget about dragging.
		if( _sciArea.getShape() == TpeSciArea.CIRCULAR )
			return ;

		if( _dragObject != null && _instItem.canUpdatePosAngle() )
		{
			_dragX = fme.xWidget ;
			_dragY = fme.yWidget ;

			double diff = _dragObject.nextAngleDiff( fme.xWidget , fme.yWidget ) ;
			_instItem.addPosAngleRadians( diff ) ;

			_iw.repaint() ;
		}
	}

	/**
	 * Stop dragging.
	 */
	public void dragStop( FitsMouseEvent fme )
	{
		// If the shape is circular then forget about dragging.
		if( _sciArea.getShape() == TpeSciArea.CIRCULAR )
			return ;

		if( _dragObject != null )
		{
			_instItem.getAvEditFSM().setEachEditNotifies( true ) ;
			_dragging = false ;
			drag( fme ) ;
			_dragObject = null ;
		}
	}
}
