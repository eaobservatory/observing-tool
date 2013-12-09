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

/*
 * Copyright 2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 */

package ot.jcmt.tpe ;

import java.awt.Color ;
import java.awt.Graphics ;

import jsky.app.ot.gui.DrawUtil ;

import orac.jcmt.iter.SpIterRasterObs ;

import jsky.app.ot.fits.gui.FitsImageInfo ;
import jsky.app.ot.fits.gui.FitsMouseEvent ;

import jsky.app.ot.tpe.TpeImageFeature ;
import jsky.app.ot.tpe.TpeDraggableFeature ;
import jsky.app.ot.tpe.TpeImageWidget ;
import java.awt.geom.Point2D ;

import gemini.sp.SpItem ;
import gemini.sp.SpTreeMan ;

import java.util.Vector ;

import gemini.util.Angle ;
import gemini.util.PolygonD ;

/**
 * An implementation class used to simplify the job of rotating scan
 * areas.
 */
final class TpeScanAreaDragObject
{
	int _xb , _yb ;
	double _angle = 0 ;

	/**
	 * Construct with the base position at (xb,yb).
	 */
	public TpeScanAreaDragObject( int xb , int yb , int x , int y )
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

	public int getBaseX()
	{
		return _xb ;
	}

	public int getBaseY()
	{
		return _yb ;
	}
}

/**
 * Draws a Scan Area.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk), based on jsky.app.ot.tpe.TpeSciAreaFeature
 */
public class TpeScanAreaFeature extends TpeImageFeature implements TpeDraggableFeature
{
	private TpeScanArea _scanArea ;
	private PolygonD _scanAreaPD ;
	private PolygonD _tickMarkPD ;
	private SpIterRasterObs _iterRaster ;
	private boolean _valid = false ;

	/**
	 * Indicates whether scan arae should be shifted or rotated.
	 */
	private boolean _shift = false ;
	
	private TpeScanAreaDragObject _dragObject ;
	private boolean _dragging = false ;
	private int _dragX ;
	private int _dragY ;

	/**
	 * Construct the feature with its name and description. 
	 */
	public TpeScanAreaFeature()
	{
		super( "Scan Area" , "Scan/Raster area." ) ;
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
	 * Calculate the polygon describing the screen location of the scan area.
	 */
	private boolean _calc( FitsImageInfo fii )
	{
		// Need the instrument to get the scan area
		SpIterRasterObs iterRaster = null ;
		SpItem item = _iw.getBaseItem() ;
		
		Vector<SpItem> rasters = SpTreeMan.findAllItems( item , SpIterRasterObs.class.getName() ) ;
		
		if( rasters.size() == 1 )
			iterRaster = ( SpIterRasterObs )rasters.elementAt( 0 ) ;

		if( iterRaster == null )
			return false ;

		if( _scanArea == null )
		{
			_scanArea = new TpeScanArea() ;
			if( _scanArea == null )
				return false ;
		}

		boolean updated = _scanArea.update( iterRaster , fii ) ;

		// Already have the current values
		if( _valid && !updated && ( _scanAreaPD != null ) && ( iterRaster == _iterRaster ) )
			return true ;

		_iterRaster = iterRaster ;

		double xBase = fii.baseScreenPos.x;
		double yBase = fii.baseScreenPos.y;
		_scanAreaPD = _scanArea.getPolygonDAt( xBase , yBase , _iw , iterRaster ) ;

		// Init the _tickMarkPD
		if( _tickMarkPD == null )
		{
			double[] xpoints = new double[ 4 ] ;
			double[] ypoints = new double[ 4 ] ;
			_tickMarkPD = new PolygonD( xpoints , ypoints , 4 ) ;
		}

		double x = xBase ;
		double y = yBase - _scanArea.height / 2.0 ;

		_tickMarkPD.xpoints[ 0 ] = x ;
		_tickMarkPD.ypoints[ 0 ] = y - MARKER_SIZE * 2 ;

		_tickMarkPD.xpoints[ 1 ] = x - MARKER_SIZE ;
		_tickMarkPD.ypoints[ 1 ] = y - 2 ;

		_tickMarkPD.xpoints[ 2 ] = x + MARKER_SIZE ;
		_tickMarkPD.ypoints[ 2 ] = y - 2 ;

		_tickMarkPD.xpoints[ 3 ] = _tickMarkPD.xpoints[ 0 ] ;
		_tickMarkPD.ypoints[ 3 ] = _tickMarkPD.ypoints[ 0 ] ;

		_iw.skyRotate( _tickMarkPD , _scanArea.posAngleRadians ) ;

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
		
		g.setColor( Color.pink ) ;
		g.drawPolygon( _scanAreaPD.getAWTPolygon() ) ;
		g.fillPolygon( _tickMarkPD.getAWTPolygon() ) ;

		if( _dragging )
		{
			// Draw a little above the mouse
			int baseX = _dragX ;
			int baseY = _dragY - 10 ;

			// Draw a string displaying the rotation angle
			String s = "" ;
			if( _shift )
				s = "" + _iterRaster.getWidth() + ", " + _iterRaster.getHeight() ;
			else
				s = "" + _iterRaster.getPosAngle() ;
			
			DrawUtil.drawString( g , s , Color.pink , Color.black , baseX , baseY ) ;
		}
	}

	/**
	 * Start dragging the object.
	 */
	public boolean dragStart( FitsMouseEvent fme , FitsImageInfo fii )
	{
		if( ( _scanAreaPD == null ) || ( _tickMarkPD == null ) )
			return false ;

		_dragObject = null ;

		// See if dragging by the corner
		for( int i = 0 ; i < ( _scanAreaPD.npoints - 1 ) ; ++i )
		{
			int cornerx = ( int )( _scanAreaPD.xpoints[ i ] + 0.5 ) ;
			int cornery = ( int )( _scanAreaPD.ypoints[ i ] + 0.5 ) ;

			int dx = Math.abs( cornerx - fme.xWidget ) ;
			if( dx > MARKER_SIZE )
				continue ;

			int dy = Math.abs( cornery - fme.yWidget ) ;
			if( dy > MARKER_SIZE )
				continue ;

			Point2D.Double p = fii.baseScreenPos ;
			_dragObject = new TpeScanAreaDragObject( ( int )p.x , ( int )p.y , cornerx , cornery ) ;

			_shift = true ;
		}

		// See if dragging by the tick mark (give a couple extra pixels to make it
		// easier to grab)
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
					_dragObject = new TpeScanAreaDragObject( ( int )p.x , ( int )p.y , x , y ) ;
				}
			}

			_shift = false ;
		}

		_dragging = ( _dragObject != null ) ;
		if( _dragging )
		{
			_dragX = fme.xWidget ;
			_dragY = fme.yWidget ;
			_iterRaster.getAvEditFSM().setEachEditNotifies( false ) ;
		}
		return _dragging ;
	}

	/**
	 * Drag to a new location.
	 */
	public void drag( FitsMouseEvent fme )
	{
		if( _dragObject != null )
		{
			_dragX = fme.xWidget ;
			_dragY = fme.yWidget ;

			if( _shift )
			{
				double pa = ( _iterRaster.getPosAngle() * Math.PI ) / 180.0 ;
				double w = Math.abs( 2.0 * ( ( fme.xOffset * Math.cos( -pa ) ) + ( fme.yOffset * Math.sin( -pa ) ) ) ) ;
				double h = Math.abs( 2.0 * ( ( fme.xOffset * ( -Math.sin( -pa ) ) ) + ( fme.yOffset * Math.cos( -pa ) ) ) ) ;

				_iterRaster.setWidth( w ) ;
				_iterRaster.setHeight( h ) ;
			}
			else
			{
				double diff = _dragObject.nextAngleDiff( fme.xWidget , fme.yWidget ) ;
				_iterRaster.setPosAngle( _iterRaster.getPosAngle() + ( ( diff * 180.0 ) / Math.PI ) ) ;
			}

			_iw.reset( _iterRaster ) ;
		}
	}

	/**
	 * Stop dragging.
	 */
	public void dragStop( FitsMouseEvent fme )
	{
		if( _dragObject != null )
		{
			_iterRaster.getAvEditFSM().setEachEditNotifies( true ) ;
			_dragging = false ;
			drag( fme ) ;
			_dragObject = null ;
		}
	}
}
