// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.ukirt.tpe ;

import java.awt.Color ;
import java.awt.Graphics ;

import jsky.app.ot.tpe.TpeImageFeature ;
import jsky.app.ot.tpe.TpeImageWidget ;

import jsky.app.ot.fits.gui.FitsImageInfo ;

import gemini.util.Angle ;
import gemini.util.PolygonD ;

/**
 * Draws the WFCAM autoguider CCD footprint.
 *
 * @author Based on ot/ukirt/tpe/TpeAcqCameraFeature.java.
 *         Modified by Martin Folger (M.Folger@roe.ac.uk).
 */
public class TpeWfcamAutoGuiderFeature extends TpeImageFeature
{
	/**
	 * Usable width of autoguider CCD in arcseconds.
	 *
	 * The usable width can be smaller than the actual width
	 * if the edge of the CCD should not be used for guiding.
	 */
	public static final double AUTOGUIDER_WIDTH = 230. ;

	/**
	 * Usable height of autoguider CCD in arcseconds.
	 *
	 * The usable height can be smaller than the actual height
	 * if the edge of the CCD should not be used for guiding.
	 */
	public static final double AUTOGUIDER_HEIGHT = 230. ;

	/**
	 * Autoguider CCD angle in degrees.
	 */
	public static final double AUTOGUIDER_ANGLE = 46.8 ;

	private PolygonD _autoguiderAreaPD ;
	private boolean _valid = false ;

	/**
	 * Construct the feature with its name and description.
	 */
	public TpeWfcamAutoGuiderFeature()
	{
		super( "WFCAM AG" , "WFCAM Autoguider Footprint" ) ;
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
		if( _autoguiderAreaPD == null )
		{
			_autoguiderAreaPD = new PolygonD() ;
			_autoguiderAreaPD.xpoints = new double[ 5 ] ;
			_autoguiderAreaPD.ypoints = new double[ 5 ] ;
			_autoguiderAreaPD.npoints = 5 ;
		}

		double[] xpoints = _autoguiderAreaPD.xpoints ;
		double[] ypoints = _autoguiderAreaPD.ypoints ;

		double x = fii.baseScreenPos.x;
		double y = fii.baseScreenPos.y;

		double w = ( fii.pixelsPerArcsec * AUTOGUIDER_WIDTH ) / 2. ;
		double h = ( fii.pixelsPerArcsec * AUTOGUIDER_HEIGHT ) / 2. ;

		xpoints[ 0 ] = x - w ;
		xpoints[ 1 ] = x + w ;
		ypoints[ 0 ] = y - h ;
		ypoints[ 1 ] = y - h ;

		xpoints[ 2 ] = x + w ;
		xpoints[ 3 ] = x - w ;
		ypoints[ 2 ] = y + h ;
		ypoints[ 3 ] = y + h ;

		xpoints[ 4 ] = xpoints[ 0 ] ;
		ypoints[ 4 ] = ypoints[ 0 ] ;

		// Rotate by AUTOGUIDER_ANGLE.
		// The skyRotate method is used for this although the rotation is NOT due to sky rotation but to the way the autoguider CCD is fixed in WFCAM.
		_iw.skyRotate( _autoguiderAreaPD , Angle.degreesToRadians( AUTOGUIDER_ANGLE ) ) ;

		_valid = true ;
	}

	/**
	 * Draw the feature.
	 */
	public void draw( Graphics g , FitsImageInfo fii )
	{
		if( !_valid )
			_calc( fii ) ;

		g.setColor( Color.magenta ) ;
		g.drawPolygon( _autoguiderAreaPD.getAWTPolygon() ) ;
	}
}
