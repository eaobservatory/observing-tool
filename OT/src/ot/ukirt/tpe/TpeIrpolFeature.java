// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
//
package ot.ukirt.tpe ;

import java.awt.Color ;
import java.awt.Graphics ;

import jsky.app.ot.tpe.TpeImageFeature ;
import jsky.app.ot.tpe.TpeImageWidget ;

import jsky.app.ot.fits.gui.FitsImageInfo ;

import gemini.sp.obsComp.SpInstObsComp ;
import orac.ukirt.inst.SpInstUIST ;
import orac.ukirt.inst.SpInstUFTI ;
import orac.ukirt.inst.SpInstCGS4 ;
import orac.ukirt.inst.SpInstMichelle ;
import orac.ukirt.inst.SpInstWFCAM ;

import gemini.util.Angle ;
import gemini.util.PolygonD ;

/**
 * Draws the location of the Irpol arm on UKIRT.
 */
public class TpeIrpolFeature extends TpeImageFeature
{
	public static final double FOV_WIDTHL = 150. ;
	public static final double FOV_WIDTHR = 150. ;
	public static final double FOV_HEIGHTU = 220. ;
	public static final double FOV_HEIGHTL = 220. ;
	public static final double OOBT_CENTRE = 142.9 ; // arcsec
	public static final double OOBT_WIDTH = 471.6 ; // arcsec
	public static final double OOBT_HEIGHT = 30.45 ; // arcsec
	public static final double OOBB_CENTRE = -135.1 ; // arcsec
	public static final double OOBB_WIDTH = 471.6 ; // arcsec
	public static final double OOBB_HEIGHT = 17.39 ; // arcsec
	public static final double INNER_RADIUS = 140. ; // arcsecs
	private PolygonD _fovAreaPD ;
	private PolygonD _oobtArea ;
	private PolygonD _oobbArea ;
	private int[] circleOriginXY = null ;
	private int circleRadius = 0 ;
	private boolean _valid = false ;

	/**
	 * Construct the feature with its name and description.
	 */
	public TpeIrpolFeature()
	{
		super( "Irpol" , "Location of the Irpol." ) ;
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
	 * The position angle has changed.
	 */
	public void posAngleUpdate( FitsImageInfo fii )
	{
		_valid = false ;
	}

	/**
	 * Calculate the polygon describing the screen location of the science area.
	 */
	private void _calc( FitsImageInfo fii )
	{
		if( _fovAreaPD == null )
		{
			_fovAreaPD = new PolygonD() ;
			_fovAreaPD.xpoints = new double[ 5 ] ;
			_fovAreaPD.ypoints = new double[ 5 ] ;
			_fovAreaPD.npoints = 5 ;
		}

		double[] xpoints = _fovAreaPD.xpoints ;
		double[] ypoints = _fovAreaPD.ypoints ;

		double x = fii.baseScreenPos.x;
		double y = fii.baseScreenPos.y;

		double wl = fii.pixelsPerArcsec * FOV_WIDTHL ;
		double wr = fii.pixelsPerArcsec * FOV_WIDTHR ;
		double hu = fii.pixelsPerArcsec * FOV_HEIGHTU ;
		double hl = fii.pixelsPerArcsec * FOV_HEIGHTL ;

		xpoints[ 0 ] = x - wl ;
		xpoints[ 1 ] = x + wr ;
		ypoints[ 0 ] = y - hl ;
		ypoints[ 1 ] = y - hl ;

		xpoints[ 2 ] = x + wr ;
		xpoints[ 3 ] = x - wl ;
		ypoints[ 2 ] = y + hu ;
		ypoints[ 3 ] = y + hu ;

		xpoints[ 4 ] = xpoints[ 0 ] ;
		ypoints[ 4 ] = ypoints[ 0 ] ;

		// top out of bounds area
		if( _oobtArea == null )
		{
			_oobtArea = new PolygonD() ;
			_oobtArea.xpoints = new double[ 5 ] ;
			_oobtArea.ypoints = new double[ 5 ] ;
			_oobtArea.npoints = 5 ;
		}

		double w2 = fii.pixelsPerArcsec * OOBT_WIDTH / 2. ;
		double h = fii.pixelsPerArcsec * OOBT_HEIGHT ;

		_oobtArea.xpoints[ 0 ] = x - w2 ;
		_oobtArea.xpoints[ 1 ] = x + w2 ;
		_oobtArea.ypoints[ 0 ] = ypoints[ 0 ] - h ;
		_oobtArea.ypoints[ 1 ] = ypoints[ 1 ] - h ;
		_oobtArea.xpoints[ 2 ] = x + w2 ;
		_oobtArea.xpoints[ 3 ] = x - w2 ;
		_oobtArea.ypoints[ 2 ] = ypoints[ 0 ] ;
		_oobtArea.ypoints[ 3 ] = ypoints[ 1 ] ;
		_oobtArea.xpoints[ 4 ] = _oobtArea.xpoints[ 0 ] ;
		_oobtArea.ypoints[ 4 ] = _oobtArea.ypoints[ 0 ] ;

		// bottom out of bounds area
		if( _oobbArea == null )
		{
			_oobbArea = new PolygonD() ;
			_oobbArea.xpoints = new double[ 5 ] ;
			_oobbArea.ypoints = new double[ 5 ] ;
			_oobbArea.npoints = 5 ;
		}

		w2 = fii.pixelsPerArcsec * OOBB_WIDTH / 2. ;
		h = fii.pixelsPerArcsec * OOBB_HEIGHT ;
		_oobbArea.xpoints[ 0 ] = x - w2 ;
		_oobbArea.xpoints[ 1 ] = x + w2 ;
		_oobbArea.ypoints[ 0 ] = ypoints[ 2 ] ;
		_oobbArea.ypoints[ 1 ] = ypoints[ 3 ] ;
		_oobbArea.xpoints[ 2 ] = x + w2 ;
		_oobbArea.xpoints[ 3 ] = x - w2 ;
		_oobbArea.ypoints[ 2 ] = ypoints[ 2 ] + h ;
		_oobbArea.ypoints[ 3 ] = ypoints[ 3 ] + h ;
		_oobbArea.xpoints[ 4 ] = _oobbArea.xpoints[ 0 ] ;
		_oobbArea.ypoints[ 4 ] = _oobbArea.ypoints[ 0 ] ;

		// get the instrument and which "port" it is on.
		SpInstObsComp _inst = _iw.getInstrumentItem() ;
		if( _inst != null )
		{
			if( _inst instanceof SpInstCGS4 )
			{
				_iw.skyRotate( _fovAreaPD , Angle.degreesToRadians( 135. ) ) ;
				_iw.skyRotate( _oobtArea , Angle.degreesToRadians( 135. ) ) ;
				_iw.skyRotate( _oobbArea , Angle.degreesToRadians( 135. ) ) ;
			}
			else if( _inst instanceof SpInstUFTI )
			{
				_iw.skyRotate( _fovAreaPD , Angle.degreesToRadians( 135. ) ) ;
				_iw.skyRotate( _oobtArea , Angle.degreesToRadians( 135. ) ) ;
				_iw.skyRotate( _oobbArea , Angle.degreesToRadians( 135. ) ) ;
			}
			else if( _inst instanceof SpInstMichelle )
			{
				_iw.skyRotate( _fovAreaPD , Angle.degreesToRadians( 135. ) ) ;
				_iw.skyRotate( _oobtArea , Angle.degreesToRadians( 135. ) ) ;
				_iw.skyRotate( _oobbArea , Angle.degreesToRadians( 135. ) ) ;
			}
			else if( _inst instanceof SpInstUIST )
			{
				_iw.skyRotate( _fovAreaPD , Angle.degreesToRadians( 135. ) ) ;
				_iw.skyRotate( _oobtArea , Angle.degreesToRadians( 135. ) ) ;
				_iw.skyRotate( _oobbArea , Angle.degreesToRadians( 135. ) ) ;
			}
			else if( _inst instanceof SpInstWFCAM )
			{
				_iw.skyRotate( _fovAreaPD , Angle.degreesToRadians( 135. ) ) ;
				_iw.skyRotate( _oobtArea , Angle.degreesToRadians( 135. ) ) ;
				_iw.skyRotate( _oobbArea , Angle.degreesToRadians( 135. ) ) ;
			}
			else{}
		}

		if( circleOriginXY == null )
			circleOriginXY = new int[ 2 ] ;

		circleOriginXY[ 0 ] = ( int )Math.rint( x - ( fii.pixelsPerArcsec * INNER_RADIUS ) / 2. ) ;
		circleOriginXY[ 1 ] = ( int )Math.rint( y - ( fii.pixelsPerArcsec * INNER_RADIUS ) / 2. ) ;
		circleRadius = ( int )Math.rint( fii.pixelsPerArcsec * INNER_RADIUS ) ;

		_valid = true ;
	}

	/**
	 * Draw the feature.
	 */
	public void draw( Graphics g , FitsImageInfo fii )
	{
		if( !_valid )
			_calc( fii ) ;

		g.setColor( Color.yellow ) ;
		g.drawOval( circleOriginXY[ 0 ] , circleOriginXY[ 1 ] , circleRadius , circleRadius ) ;
		g.drawPolygon( _fovAreaPD.getAWTPolygon() ) ;
		g.fillPolygon( _oobtArea.getAWTPolygon() ) ;
		g.fillPolygon( _oobbArea.getAWTPolygon() ) ;
	}
}
