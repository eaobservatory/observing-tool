// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.tpe ;

import java.awt.Color ;
import java.awt.Graphics ;

import jsky.app.ot.tpe.TpeImageFeature ;
import jsky.app.ot.tpe.TpeImageWidget ;

import jsky.app.ot.fits.gui.FitsImageInfo ;
import java.awt.geom.Rectangle2D ;

/**
 * Draws the PWFS annulus.
 */
public class TpePWFSFeature extends TpeImageFeature
{
	// For now, just make these constants static members.  Eventually read from a configuration file?
	public static final int PWFS1_RADIUS = 420 ; // arcsec
	public static final int PWFS2_RADIUS = 180 ; // arcsec
	private Rectangle2D.Double _pwfs1 = new Rectangle2D.Double() ;
	private Rectangle2D.Double _pwfs2 = new Rectangle2D.Double() ;

	/**
	 * Construct the feature with its name and description. 
	 */
	public TpePWFSFeature()
	{
		super( "PWFS" , "Range of the PWFS probes." ) ;
	}

	/**
	 * Reinit.
	 */
	public void reinit( TpeImageWidget iw , FitsImageInfo fii )
	{
		super.reinit( iw , fii ) ;

		double radius , size ;

		radius = ( int )( fii.pixelsPerArcsec * PWFS1_RADIUS + 0.5 ) ;
		size = 2 * radius ;
		_pwfs1.x = fii.baseScreenPos.x - radius ;
		_pwfs1.y = fii.baseScreenPos.y - radius ;
		_pwfs1.width = size ;
		_pwfs1.height = size ;

		radius = ( int )( fii.pixelsPerArcsec * PWFS2_RADIUS + 0.5 ) ;
		size = 2 * radius ;
		_pwfs2.x = fii.baseScreenPos.x - radius ;
		_pwfs2.y = fii.baseScreenPos.y - radius ;
		_pwfs2.width = size ;
		_pwfs2.height = size ;
	}

	/**
	 * Draw the feature.
	 */
	public void draw( Graphics g , FitsImageInfo fii )
	{
		g.setColor( Color.magenta ) ;
		g.drawOval( ( int )_pwfs1.x , ( int )_pwfs1.y , ( int )_pwfs1.width , ( int )_pwfs1.height ) ;
		g.drawOval( ( int )_pwfs2.x , ( int )_pwfs2.y , ( int )_pwfs2.width , ( int )_pwfs2.height ) ;
	}
}
