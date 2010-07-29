/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/

// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.jcmt.tpe ;

import java.awt.Polygon ;
import java.awt.geom.Point2D ;

import jsky.app.ot.tpe.TpeImageWidget ;
import jsky.app.ot.tpe.TpeSciArea ;
import jsky.app.ot.fits.gui.FitsImageInfo ;
import gemini.sp.SpTelescopePos ;
import gemini.sp.SpTelescopePosList;
import gemini.sp.SpTreeMan ;
import gemini.sp.obsComp.SpInstObsComp ;
import gemini.sp.obsComp.SpTelescopeObsComp ;
import gemini.util.CoordSys ;
import gemini.util.PolygonD ;
import orac.jcmt.iter.SpIterRasterObs ;
import gemini.util.RADec ;
import orac.util.MapArea ;

/**
 * Describes a Scan Area and facilitates drawing, rotating it.
 *
 * @see orac.jcmt.iter.SpIterRasterObs
 *
 * @author Martin Folger (M.Folger@roe.ac.uk), based on jsky.app.ot.tpe.TpeSciArea
 */
public class TpeScanArea extends TpeSciArea
{
	private SpIterRasterObs raster ;
	private PolygonD _pd ;

	public TpeScanArea()
	{
		_pd = new PolygonD() ;
		_pd.xpoints = new double[ 5 ] ;
		_pd.ypoints = new double[ 5 ] ;
		_pd.npoints = 5 ;
	}

	/**
	 * Update the ScanArea fields, returning true iff changes were made.
	 */
	public boolean update( SpInstObsComp spInst , FitsImageInfo fii )
	{
		throw new UnsupportedOperationException( "TpeScanArea.update(orac.jcmt.iter.SpIterRasterObs, " + "jsky.app.ot.fits.gui.FitsImageInfo) should be used" ) ;
	}

	/**
	 * Update the ScanArea fields, returning true iff changes were made.
	 */
	public boolean update( SpIterRasterObs iterRaster , FitsImageInfo fii )
	{
		double w , h , posAngle , sky ;

		raster = iterRaster ;

		w = iterRaster.getWidth() * fii.pixelsPerArcsec ;
		h = iterRaster.getHeight() * fii.pixelsPerArcsec ;

		posAngle = ( Math.PI * iterRaster.getPosAngle() ) / 180. ;
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

		return false ;
	}
	
	public PolygonD getPolygonDAt( double x , double y , TpeImageWidget _iw )
	{
		if( raster != null )
		{
			SpTelescopeObsComp targetList = SpTreeMan.findTargetList( raster ) ;
			SpTelescopePosList list = targetList.getPosList() ;
			SpTelescopePos position = list.getBasePosition() ;
			if( position.getCoordSys() == CoordSys.GAL )
			{
				RADec[] positions = MapArea.createNewMapArea( position.getXaxis() , position.getYaxis() , 0 , 0 , raster.getWidth() , raster.getHeight() , raster.getPosAngle() ) ;

				PolygonD pd = _pd ;
				double[] xpoints = pd.xpoints ;
				double[] ypoints = pd.ypoints ;

				Point2D.Double point = _iw.raDecToImageWidget( positions[ 0 ].ra , positions[ 0 ].dec ) ;
				xpoints[ 0 ] = point.x ;
				ypoints[ 0 ] = point.y ;

				point = _iw.raDecToImageWidget( positions[ 1 ].ra , positions[ 1 ].dec ) ;
				xpoints[ 1 ] = point.x ;
				ypoints[ 1 ] = point.y ;

				point = _iw.raDecToImageWidget( positions[ 2 ].ra , positions[ 2 ].dec ) ;
				xpoints[ 2 ] = point.x ;
				ypoints[ 2 ] = point.y ;

				point = _iw.raDecToImageWidget( positions[ 3 ].ra , positions[ 3 ].dec ) ;
				xpoints[ 3 ] = point.x ;
				ypoints[ 3 ] = point.y ;

				xpoints[ 4 ] = xpoints[ 0 ] ;
				ypoints[ 4 ] = ypoints[ 0 ] ;

				return new PolygonD( pd ) ;
			}
		}
		return super.getPolygonDAt( x , y ) ;
	}

	public Polygon getPolygonAt( double x , double y )
	{
		return getPolygonDAt( x , y ).getAWTPolygon() ;
	}
}
