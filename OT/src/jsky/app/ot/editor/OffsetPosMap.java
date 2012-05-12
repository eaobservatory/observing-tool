// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor ;

import jsky.app.ot.fits.gui.FitsImageWidget ;
import jsky.app.ot.fits.gui.FitsImageInfo ;
import jsky.app.ot.fits.gui.FitsImageInfoObserver ;
import jsky.app.ot.fits.gui.FitsPosMap ;
import jsky.app.ot.fits.gui.FitsPosMapEntry ;
import jsky.app.ot.fits.gui.FitsMouseEvent ;
import jsky.app.ot.tpe.TpeImageWidget ;
import gemini.util.TelescopePos ;
import gemini.sp.SpOffsetPos ;
import gemini.sp.SpOffsetPosList ;
import gemini.sp.obsComp.SpInstObsComp ;
import orac.ukirt.inst.SpUKIRTInstObsComp ;

import java.awt.geom.Point2D ;

/**
 * An auxiliary class used to maintain a mapping between telescope positions
 * and image widget locations.
 */
public class OffsetPosMap extends FitsPosMap implements FitsImageInfoObserver
{
	private double _ra , _dec , _posAngle ;

	/**
	 * Construct with an image widget.
	 */
	public OffsetPosMap( FitsImageWidget iw )
	{
		super( iw ) ;
		iw.addInfoObserver( this ) ;
	}

	/**
	 */
	public void free()
	{
		if( _iw != null )
			_iw.deleteInfoObserver( this ) ;

		super.free() ;
	}

	/**
	 * The FitsImageInfo has been updated.
	 */
	public void imageInfoUpdate( FitsImageWidget iw , FitsImageInfo fii )
	{
		if( ( fii.ra != _ra ) || ( fii.dec != _dec ) || ( fii.posAngleDegrees != _posAngle ) )
		{
			_ra = fii.ra ;
			_dec = fii.dec ;
			_posAngle = fii.posAngleDegrees ;


			_updateScreenLocations() ;
		}
	}

	/**
	 * Takes the position angle of the offset iterator into account.
	 */
	public void updatePosition( FitsPosMapEntry pme , FitsMouseEvent fme )
	{
		pme.screenPos.x = fme.xWidget ;
		pme.screenPos.y = fme.yWidget ;

		TelescopePos tp = pme.telescopePos ;

		tp.deleteWatcher( this ) ;

		double posAngle = ( ( ( SpOffsetPosList )_tpl ).getPosAngle() * Math.PI ) / 180.0 ;
		double unrotatedX = ( fme.xOffset * Math.cos( -posAngle ) ) + ( fme.yOffset * Math.sin( -posAngle ) ) ;
		double unrotatedY = ( fme.xOffset * ( -Math.sin( -posAngle ) ) ) + ( fme.yOffset * Math.cos( -posAngle ) ) ;

		tp.setXY( unrotatedX , unrotatedY ) ;

		tp.addWatcher( this ) ;

		_iw.repaint() ;
	}

	/**
	 * Takes the position angle of the offset iterator into account.
	 */
	public Point2D.Double telescopePosToImageWidget( TelescopePos tp )
	{
		double xaxis = tp.getXaxis() ;
		double yaxis = tp.getYaxis() ;
		double posAngle = ( ( ( SpOffsetPosList )_tpl ).getPosAngle() * Math.PI ) / 180.0 ;
		SpInstObsComp myInst = ((TpeImageWidget) _iw).getInstrumentItem();
		if( myInst instanceof SpUKIRTInstObsComp )
			posAngle = myInst.getPosAngleRadians() ;

		// Rotate temporarily by position angle of telescope position.
		( ( SpOffsetPos )tp ).noNotifySetXY( ( xaxis * Math.cos( posAngle ) ) + ( yaxis * Math.sin( posAngle ) ) , ( xaxis * ( -Math.sin( posAngle ) ) ) + ( yaxis * Math.cos( posAngle ) ) ) ;

		// Let the method of the super class do the conversion to image widget.
		Point2D.Double result = super.telescopePosToImageWidget( tp ) ;

		// Undo rotation.
		( ( SpOffsetPos )tp ).noNotifySetXY( xaxis , yaxis ) ;

		return result ;
	}
}
