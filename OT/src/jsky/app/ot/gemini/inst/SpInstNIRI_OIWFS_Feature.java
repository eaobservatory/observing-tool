// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.inst ;

import java.awt.Color ;
import java.awt.Graphics ;
import java.awt.Polygon ;

import jsky.app.ot.fits.gui.FitsImageInfo ;

import jsky.app.ot.tpe.TpeImageFeature ;
import jsky.app.ot.tpe.TpeImageWidget ;

import jsky.app.ot.util.Angle ;
import jsky.app.ot.util.Assert ;
import jsky.app.ot.util.PolygonD ;
import jsky.app.ot.util.ScreenMath ;
import jsky.app.ot.util.BasicPropertyList ;
import jsky.app.ot.util.PropertyWatcher ;

import java.awt.geom.Point2D ;

class SpInstNIRI_OIWFS_Obscured
{
	private static PolygonD[] _ignoreVig = new PolygonD[ 3 ] ;
	private static PolygonD[] _withVig = new PolygonD[ 3 ] ;

	//
	// Calculate the obscured region based upon a center square with a
	// cross (X) on top of it that covers the area of the given OIWFS
	// range (circle).
	//
	private static PolygonD _calculateObscured( double centerSquareWidth , double crossWidth , double circleDiameter )
	{
		double[] xpoints = new double[ 17 ] ;
		double[] ypoints = new double[ 17 ] ;

		double halfCrossWidth = crossWidth / 2. ;
		double halfCircleDiameter = circleDiameter / 2. ;

		double tmp = Math.sqrt( 2. * ( centerSquareWidth * centerSquareWidth ) ) / 2. ;
		tmp -= halfCrossWidth ;

		// Assign the coordinate pairs working clockwise

		// Quadrant I points
		xpoints[ 0 ] = halfCrossWidth ;
		ypoints[ 0 ] = halfCircleDiameter ;

		xpoints[ 1 ] = halfCrossWidth ;
		ypoints[ 1 ] = tmp ;

		xpoints[ 2 ] = tmp ;
		ypoints[ 2 ] = halfCrossWidth ;

		xpoints[ 3 ] = halfCircleDiameter ;
		ypoints[ 3 ] = halfCrossWidth ;

		// Quadrant IV points
		xpoints[ 4 ] = xpoints[ 3 ] ;
		xpoints[ 5 ] = xpoints[ 2 ] ;
		ypoints[ 4 ] = -ypoints[ 3 ] ;
		ypoints[ 5 ] = -ypoints[ 2 ] ;

		xpoints[ 6 ] = xpoints[ 1 ] ;
		xpoints[ 7 ] = xpoints[ 0 ] ;
		ypoints[ 6 ] = -ypoints[ 1 ] ;
		ypoints[ 7 ] = -ypoints[ 0 ] ;

		// Quadrant III points
		xpoints[ 8 ] = -xpoints[ 0 ] ;
		xpoints[ 9 ] = -xpoints[ 1 ] ;
		ypoints[ 8 ] = -ypoints[ 0 ] ;
		ypoints[ 9 ] = -ypoints[ 1 ] ;

		xpoints[ 10 ] = -xpoints[ 2 ] ;
		xpoints[ 11 ] = -xpoints[ 3 ] ;
		ypoints[ 10 ] = -ypoints[ 2 ] ;
		ypoints[ 11 ] = -ypoints[ 3 ] ;

		// Quadrant II points
		xpoints[ 12 ] = -xpoints[ 3 ] ;
		xpoints[ 13 ] = -xpoints[ 2 ] ;
		ypoints[ 12 ] = ypoints[ 3 ] ;
		ypoints[ 13 ] = ypoints[ 2 ] ;

		xpoints[ 14 ] = -xpoints[ 1 ] ;
		xpoints[ 15 ] = -xpoints[ 0 ] ;
		ypoints[ 14 ] = ypoints[ 1 ] ;
		ypoints[ 15 ] = ypoints[ 0 ] ;

		// close the loop
		xpoints[ 16 ] = xpoints[ 0 ] ;
		ypoints[ 16 ] = ypoints[ 0 ] ;

		// Rotate by 45 degrees.
		double sin = Angle.sinRadians( Math.PI / 4. ) ;
		double cos = Angle.cosRadians( Math.PI / 4. ) ;
		for( int i = 0 ; i < xpoints.length ; ++i )
		{
			double x0 = xpoints[ i ] ;
			double y0 = ypoints[ i ] ;

			xpoints[ i ] = x0 * cos - y0 * sin ;
			ypoints[ i ] = x0 * sin + y0 * cos ;
		}

		return new PolygonD( xpoints , ypoints , 17 ) ;
	}

	// 
	// Calculate the polygon describing the f/6 camera with vignetting.
	// This is provided seperately from the general _calculateVignetting
	// routine because it contains no cross region (i.e., it is just a
	// simple square) and because the square extends beyond the bounds of
	// the OIWFS range.
	//
	private static PolygonD _calculateF6WithVignetting( double centerSquareWidth )
	{
		double halfSquareWidth = centerSquareWidth / 2.0 ;

		double[] xpoints = new double[ 5 ] ;
		double[] ypoints = new double[ 5 ] ;

		xpoints[ 0 ] = halfSquareWidth ;
		xpoints[ 1 ] = -halfSquareWidth ;
		ypoints[ 0 ] = halfSquareWidth ;
		ypoints[ 1 ] = halfSquareWidth ;

		xpoints[ 2 ] = -halfSquareWidth ;
		xpoints[ 3 ] = halfSquareWidth ;
		ypoints[ 2 ] = -halfSquareWidth ;
		ypoints[ 3 ] = -halfSquareWidth ;

		xpoints[ 4 ] = xpoints[ 0 ] ;
		ypoints[ 4 ] = ypoints[ 0 ] ;

		return new PolygonD( xpoints , ypoints , 5 ) ;
	}

	//
	// Get the PolygonD describing the obscured region for the given
	// camera and vignetting option.  Fills in the arrays as needed:
	//
	private static PolygonD _getObscured( int camera , boolean withVignetting )
	{
		PolygonD pd = null ;

		if( withVignetting )
		{
			pd = _withVig[ camera ] ;
			if( pd == null )
			{
				switch( camera )
				{
					case SpInstNIRI.CAMERA_F6 :
						_withVig[ SpInstNIRI.CAMERA_F6 ] = _calculateF6WithVignetting( 148. ) ;
						break ;
					case SpInstNIRI.CAMERA_F14 :
					case SpInstNIRI.CAMERA_F32 :
						_withVig[ SpInstNIRI.CAMERA_F14 ] = _calculateObscured( 61. , 15. , 168. ) ;
						_withVig[ SpInstNIRI.CAMERA_F32 ] = _withVig[ SpInstNIRI.CAMERA_F14 ] ;
						break ;
				}
				pd = _withVig[ camera ] ;
			}
		}
		else
		{
			pd = _ignoreVig[ camera ] ;
			if( pd == null )
			{
				switch( camera )
				{
					case SpInstNIRI.CAMERA_F6 :
						_ignoreVig[ SpInstNIRI.CAMERA_F6 ] = _calculateObscured( 123. , 2. , 210. ) ;
						break ;
					case SpInstNIRI.CAMERA_F14 :
					case SpInstNIRI.CAMERA_F32 :
						_ignoreVig[ SpInstNIRI.CAMERA_F14 ] = _calculateObscured( 51. , 2. , 210. ) ;
						_ignoreVig[ SpInstNIRI.CAMERA_F32 ] = _ignoreVig[ SpInstNIRI.CAMERA_F14 ] ;
						break ;
				}
				pd = _ignoreVig[ camera ] ;
			}
		}
		return pd ;
	}

	/**
	 * Get a Polygon describing the obscured region of the OIWFS range.
	 */
	public static Polygon getObscured( int camera , boolean withVignetting , double pixelsPerArcsec , Point2D.Double basePos , double angle )
	{
		PolygonD obscPD = _getObscured( camera , withVignetting ) ;
		Assert.notFalse( obscPD != null ) ;

		// Calculate the screen location of the obscured region.
		PolygonD pd = ( PolygonD )obscPD.clone() ;
		double x = basePos.x ;
		double y = basePos.y ;
		for( int i = 0 ; i < pd.npoints ; ++i )
		{
			pd.xpoints[ i ] = ( pd.xpoints[ i ] * pixelsPerArcsec ) + x ;
			pd.ypoints[ i ] = ( -pd.ypoints[ i ] * pixelsPerArcsec ) + y ;
		}
		ScreenMath.rotateRadians( pd , angle , x , y ) ;

		return pd.getAWTPolygon() ;
	}
}

/**
 * Draws the OIWFS ring.
 */
public class SpInstNIRI_OIWFS_Feature extends TpeImageFeature implements PropertyWatcher
{
	private static final String PROP_WITH_VIG = "With Vignetting" ;
	private static final String PROP_FILL_OBSCURED = "Fill Obscured Area" ;
	private static BasicPropertyList _props ;

	static
	{
		// Initialize the properties supported by the TpeGuidePosFeature.
		_props = new BasicPropertyList() ;
		_props.setBoolean( PROP_WITH_VIG , true ) ;
		_props.setBoolean( PROP_FILL_OBSCURED , false ) ;
	}

	// These are the attributes that determine the valid OIWFS region to draw
	private double _pixelsPerArcsec ;
	private Point2D.Double _baseScreenPos ;
	private double _angle ;
	private int _cameraIndex ;
	private boolean _withVignetting ;
	private Polygon _obscuredPoly ;

	/**
	 * Construct the feature with its name and description. 
	 */
	public SpInstNIRI_OIWFS_Feature()
	{
		super( "OIWFS" , "Range of the OIWFS (if any)." ) ;
	}

	/**
	 * Override reinit to start watching properties.
	 */
	public void reinit( TpeImageWidget iw , FitsImageInfo fii )
	{
		super.reinit( iw , fii ) ;
		_props.addWatcher( this ) ;
		return ;
	}

	/**
	 * Override unloaded to quit watching properties.
	 */
	public void unloaded()
	{
		_props.deleteWatcher( this ) ;
		super.unloaded() ;
	}

	/**
	 * A property has changed.
	 *
	 * @see PropertyWatcher
	 */
	public void propertyChange( String propName )
	{
		_iw.repaint( this ) ;
	}

	/**
	 * Override getProperties to return the properties supported by this
	 * feature.
	 */
	public BasicPropertyList getProperties()
	{
		return _props ;
	}

	/**
	 * Turn on/off the consideration of vignetting.
	 */
	public void setWithVignetting( boolean withVignetting )
	{
		_props.setBoolean( PROP_WITH_VIG , withVignetting ) ;
	}

	/**
	 * Get the "with vignetting" property.
	 */
	public boolean getWithVignetting()
	{
		return _props.getBoolean( PROP_WITH_VIG , true ) ;
	}

	/**
	 * Turn on/off the filling of the obscured area.
	 */
	public void setFillObscuredArea( boolean fill )
	{
		_props.setBoolean( PROP_FILL_OBSCURED , fill ) ;
	}

	/**
	 * Get the "with vignetting" property.
	 */
	public boolean getFillObscuredArea()
	{
		return _props.getBoolean( PROP_FILL_OBSCURED , true ) ;
	}

	public void validate( SpInstNIRI spNIRI , FitsImageInfo fii )
	{
		double pixelsPerArcsec = fii.pixelsPerArcsec ;
		Point2D.Double baseScreenPos = fii.baseScreenPos ;
		double angle = spNIRI.getPosAngleRadians() + fii.theta ;
		int cameraIndex = spNIRI.getCameraIndex() ;
		boolean withVignetting = getWithVignetting() ;

		if( ( _obscuredPoly == null ) || ( _pixelsPerArcsec != pixelsPerArcsec ) || ( !_baseScreenPos.equals( baseScreenPos ) ) || ( _angle != angle ) || ( _cameraIndex != cameraIndex ) || ( _withVignetting != withVignetting ) )
		{
			_pixelsPerArcsec = pixelsPerArcsec ;
			_baseScreenPos = new Point2D.Double( baseScreenPos.x , baseScreenPos.y ) ;
			_angle = angle ;
			_cameraIndex = cameraIndex ;
			_withVignetting = withVignetting ;

			_obscuredPoly = SpInstNIRI_OIWFS_Obscured.getObscured( cameraIndex , withVignetting , pixelsPerArcsec , baseScreenPos , angle ) ;
		}
	}

	/**
	 * Draw the feature.
	 */
	public void draw( Graphics g , FitsImageInfo fii )
	{
		g.setColor( Color.red.brighter() ) ;

		int r , d ;
		if( getWithVignetting() )
		{
			r = ( int )( fii.pixelsPerArcsec * 84. ) ;
			d = ( int )( fii.pixelsPerArcsec * 168. ) ;
		}
		else
		{
			r = ( int )( fii.pixelsPerArcsec * 105. ) ;
			d = ( int )( fii.pixelsPerArcsec * 210. ) ;
		}
		g.drawOval( ( int )( fii.baseScreenPos.x - r ) , ( int )( fii.baseScreenPos.y - r ) , d , d ) ;

		validate( ( SpInstNIRI )_iw.getInstrumentItem() , fii ) ;
		g.setColor( Color.red.darker() ) ;
		if( getFillObscuredArea() )
			g.fillPolygon( _obscuredPoly ) ;
		else
			g.drawPolygon( _obscuredPoly ) ;
	}
}
