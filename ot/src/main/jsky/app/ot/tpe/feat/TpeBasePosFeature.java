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

import jsky.app.ot.fits.gui.FitsImageInfo ;
import jsky.app.ot.fits.gui.FitsPosMapEntry ;
import jsky.app.ot.fits.gui.FitsMouseEvent ;

import gemini.sp.SpTelescopePos ;

import jsky.app.ot.tpe.TpeImageWidget ;
import jsky.app.ot.tpe.TpePositionMap ;
import java.awt.geom.Point2D ;

public class TpeBasePosFeature extends TpePositionFeature
{
	/**
	 * Construct the feature with its name and description. 
	 */
	public TpeBasePosFeature()
	{
		super( "Base" , "Location of the base position." ) ;
	}

	public void reinit( TpeImageWidget iw , FitsImageInfo fii )
	{
		super.reinit( iw , fii ) ;

		// Tell the position map that the base position is visible.
		TpePositionMap pm = TpePositionMap.getMap( iw ) ;
		pm.setFindBase( true ) ;
	}

	public void unloaded()
	{
		// Tell the position map that the base position is no longer visible.
		TpePositionMap pm = TpePositionMap.getExistingMap( _iw ) ;
		if( pm != null )
			pm.setFindBase( false ) ;

		super.unloaded() ;
	}

	/**
	 */
	public boolean erase( FitsMouseEvent fme )
	{
		// You can't erase the base positon
		return false ;
	}

	/**
	 * @see jsky.app.ot.tpe.TpeSelectableFeature
	 */
	public Object select( FitsMouseEvent fme )
	{
		TpePositionMap pm = TpePositionMap.getMap( _iw ) ;

		int x = fme.xWidget ;
		int y = fme.yWidget ;

		FitsPosMapEntry pme ;
		pme = pm.getPositionMapEntry( SpTelescopePos.BASE_TAG ) ;
		if( ( pme != null ) && ( positionIsClose( pme , x , y ) ) )
		{
			pme.telescopePos.select() ;
			return pme.telescopePos ;
		}
		return null ;
	}

	/**
	 */
	public void draw( Graphics g , FitsImageInfo fii )
	{
		TpePositionMap pm = TpePositionMap.getMap( _iw ) ;

		Point2D.Double base = pm.getLocationFromTag( SpTelescopePos.BASE_TAG ) ;
		if( base == null )
			return ;

		int r = 5 ;
		int d = 2 * r ;

		// Draw crosshairs
		g.setColor( Color.green ) ;
		g.drawOval( ( int )( base.x - r ) , ( int )( base.y - r ) , d , d ) ;
		g.drawLine( ( int )base.x , ( int )( base.y - r ) , ( int )base.x , ( int )( base.y + r ) ) ;
		g.drawLine( ( int )( base.x - r ) , ( int )base.y , ( int )( base.x + r ) , ( int )base.y ) ;

		// Draw the shadow as well (applies if the base position is an offset)
		Point2D.Double sbase = pm.getLocationFromTag( "SHADOW" ) ;
		if( sbase == null )
			return ;

		g.setColor( Color.green.darker() ) ;
		g.drawOval( ( int )( sbase.x - r ) , ( int )( sbase.y - r ) , d , d ) ;
		g.drawLine( ( int )sbase.x , ( int )( sbase.y - r ) , ( int )sbase.x , ( int )( sbase.y + r ) ) ;
		g.drawLine( ( int )( sbase.x - r ) , ( int )sbase.y , ( int )( sbase.x + r ) , ( int )sbase.y ) ;

	}

	/**
	 */
	public boolean dragStart( FitsMouseEvent fme , FitsImageInfo fii )
	{
		TpePositionMap pm = TpePositionMap.getMap( _iw ) ;
		FitsPosMapEntry pme = pm.getPositionMapEntry( SpTelescopePos.BASE_TAG ) ;
		if( positionIsClose( pme , fme.xWidget , fme.yWidget ) )
		{
			_dragObject = pme ;

			SpTelescopePos tp = ( SpTelescopePos )_dragObject.telescopePos ;
			tp.setOffsetPosition( false ) ;

			return true ;
		}
		return false ;
	}

	/**
	 */
	public void drag( FitsMouseEvent fme )
	{
		super.drag(fme);
		dragDoPositionUpdate(fme);
	}

	/**
	 * Get the feature's name.
	 */
	public String getName()
	{
		try
		{
			return SpTelescopePos.BASE_TAG ;
		}
		catch( Exception e )
		{
			return super.getName() ;
		}
	}
}
