// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.ukirt.tpe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Polygon;

import jsky.app.ot.tpe.TpeImageFeature;
import jsky.app.ot.tpe.TpeImageWidget;
import orac.ukirt.inst.SpUKIRTInstObsComp;

import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.SpTreeMan;
import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.util.PolygonD;

/**
 * Draws the range of the Xhead on UKIRT.
 */
public class TpeXheadFeature extends TpeImageFeature {

// For now, just make these constants static members.  Eventually
// read from a configuration file?  Here we assume a single circle
// although the are in fact two centres: one is refracted slightly
// by the dichroic and is for the segments between the `H' of the
// dichroic.  For practical purposes a single circle is fine.
// The units for all is arcseconds.
   public static final double XHEAD_OUTER_RADIUS = 274.6;
   public static final double XHEAD_INNER_RADIUS = 265.0; 
   public static final double XHEAD_X_CENTRE     =  -1.4; 
   public static final double XHEAD_Y_CENTRE     = -11.2; 
   public static final double XHEAD_HALF_SQUARE  = 234.0; 

   private Rectangle _xheadi = new Rectangle();
   private Rectangle _xheado = new Rectangle();
   private boolean  _valid = false;
   private int _xlimits[] = new int[ 8 ];   // Interesection co-oridnates of the
   private int _ylimits[] = new int[ 8 ];   // square with the inner circle in
                                            // pixels
   private int _halflimit;                  // Square limit in pixels

/**
 * Construct the feature with its name and description. 
 */
   public TpeXheadFeature() {
      super( "Xhead", "Range of the crosshead movement." );
   }

/**
 * Reinit.
 */
   public void reinit( TpeImageWidget iw, FitsImageInfo fii ) {
      super.reinit( iw, fii );
      _valid = false;
   }

/**
 * Calculate the polygon describing the screen location of the
 * crosshead area.
 */
   private void _calc( FitsImageInfo fii ) {

      int radius, size;
      double _topRoot, _bottomRoot, _rightRoot, _leftRoot;

// Get the instrument aperture X & Y.  These offset the origin of the
// x-head range.
      int xoffpix = 0;
      int yoffpix = 0;
      SpUKIRTInstObsComp _inst = (SpUKIRTInstObsComp) _iw.getInstrumentItem();
      if ( _inst != null ) {
         double xoff = _inst.getInstApXarcsec();
         double yoff = _inst.getInstApYarcsec();
         xoffpix = (int) ( fii.pixelsPerArcsec * xoff + 0.5 );
         yoffpix = (int) ( fii.pixelsPerArcsec * yoff + 0.5 );
      }

// Specify the parameters of the outer circle.
      radius = (int) ( fii.pixelsPerArcsec * XHEAD_OUTER_RADIUS + 0.5 );
      int xoriginpix = (int) ( fii.pixelsPerArcsec * XHEAD_X_CENTRE + 0.5 );
      int yoriginpix = (int) ( fii.pixelsPerArcsec * XHEAD_Y_CENTRE + 0.5 );
      size = 2 * radius;

// Obtain the origin in image pixel co-ordinates, allowing for the
// instrument apertures.. 
      int xbase = (int) fii.baseScreenPos.x + xoffpix;
      int ybase = (int) fii.baseScreenPos.y + yoffpix;

// Specify the parameters of the inner circle.
      _xheado.x      = xbase - radius - xoriginpix;
      _xheado.y      = ybase - radius - yoriginpix;
      _xheado.width  = size;
      _xheado.height = size;

      radius = (int) ( fii.pixelsPerArcsec * XHEAD_INNER_RADIUS + 0.5 );
      size = 2 * radius;
      _xheadi.x      = xbase - radius - xoriginpix;
      _xheadi.y      = ybase - radius - yoriginpix;
      _xheadi.width  = size;
      _xheadi.height = size;
      _valid = true;

// Specify the parameters of the square.
// -------------------------------------

// Find the intersection points of the edge of the square accessible
// region with the inner circle.

// Find the two roots for the intersection of the square of width
// XHEAD_HALF_SQUARE with the inner circle.  The circle is not quite
// concentric with the optical axis, being offset by (XHEAD_X_CENTRE,
// XHEAD_Y_CENTRE).  Record the co-ordinates of each intersection.  Convert
// to the integer pixel co-ordinate system of the graphics.
      _halflimit = (int) ( fii.pixelsPerArcsec * XHEAD_HALF_SQUARE + 0.5 );

// Line for y = XHEAD_HALF_SQUARE.
      _topRoot = Math.sqrt( ( XHEAD_INNER_RADIUS * XHEAD_INNER_RADIUS ) -
                          ( ( -XHEAD_HALF_SQUARE - XHEAD_Y_CENTRE ) *
                            ( -XHEAD_HALF_SQUARE - XHEAD_Y_CENTRE ) ) );

      _xlimits[ 0 ] = (int) ( fii.pixelsPerArcsec *
                      ( -XHEAD_X_CENTRE + Math.abs( _topRoot ) + 0.5 ) + xbase );
      _ylimits[ 0 ] = _halflimit + ybase;

      _xlimits[ 1 ] = (int) ( fii.pixelsPerArcsec * 
                      ( -XHEAD_X_CENTRE - Math.abs( _topRoot ) + 0.5 ) + xbase );
      _ylimits[ 1 ] = _ylimits[ 0 ];

// Line for y = -XHEAD_HALF_SQUARE.
      _bottomRoot = Math.sqrt( ( XHEAD_INNER_RADIUS * XHEAD_INNER_RADIUS ) -
                             ( ( XHEAD_HALF_SQUARE - XHEAD_Y_CENTRE ) *
                               ( XHEAD_HALF_SQUARE - XHEAD_Y_CENTRE ) ) );

      _xlimits[ 2 ] = (int) ( fii.pixelsPerArcsec *
                      ( -XHEAD_X_CENTRE + Math.abs( _bottomRoot ) + 0.5 ) + xbase );
      _ylimits[ 2 ] = ybase - _halflimit;

      _xlimits[ 3 ] = (int) ( fii.pixelsPerArcsec *
                      ( -XHEAD_X_CENTRE - Math.abs( _bottomRoot ) + 0.5 ) + xbase );
      _ylimits[ 3 ] = _ylimits[ 2 ];

// Line for x = XHEAD_HALF_SQUARE.
      _rightRoot = Math.sqrt( ( XHEAD_INNER_RADIUS * XHEAD_INNER_RADIUS ) -
                            ( ( -XHEAD_HALF_SQUARE - XHEAD_X_CENTRE ) *
                              ( -XHEAD_HALF_SQUARE - XHEAD_X_CENTRE ) ) );

      _xlimits[ 4 ] = _halflimit + xbase;
      _ylimits[ 4 ] = (int) ( fii.pixelsPerArcsec *
                      ( -XHEAD_Y_CENTRE + Math.abs( _rightRoot ) + 0.5 ) + ybase );

      _xlimits[ 5 ] = _xlimits[ 4 ];
      _ylimits[ 5 ] = (int) (fii.pixelsPerArcsec *
                      ( -XHEAD_Y_CENTRE - Math.abs( _rightRoot ) + 0.5 ) + ybase );

// Line for x = -XHEAD_HALF_SQUARE.
      _leftRoot = Math.sqrt( ( XHEAD_INNER_RADIUS * XHEAD_INNER_RADIUS ) -
                           ( ( XHEAD_HALF_SQUARE - XHEAD_X_CENTRE ) *
                             ( XHEAD_HALF_SQUARE - XHEAD_X_CENTRE ) ) );

      _xlimits[ 6 ] = xbase - _halflimit;
      _ylimits[ 6 ] = (int) ( fii.pixelsPerArcsec *
                      ( -XHEAD_Y_CENTRE + Math.abs( _leftRoot ) + 0.5 ) + ybase );

      _xlimits[ 7 ] = _xlimits[ 6 ];
      _ylimits[ 7 ] = (int) ( fii.pixelsPerArcsec *
                      ( -XHEAD_Y_CENTRE - Math.abs( _leftRoot ) + 0.5 ) + ybase );

   }

/**
 * The position angle has changed. Here this is actually used to check changes
 * in other things, particularly the instrument aperture.
 */
   public void posAngleUpdate( FitsImageInfo fii ) {
      _valid = false;
   }

/**
 * Draw the two circles, and four lines of a square within the inner circle.
 */
   public void draw( Graphics g, FitsImageInfo fii ) {
      if ( !_valid ) {
         _calc( fii );
      }

      g.setColor( Color.magenta );
      g.drawOval(_xheado.x, _xheado.y, _xheado.width, _xheado.height);
      g.drawOval(_xheadi.x, _xheadi.y, _xheadi.width, _xheadi.height);
      g.drawLine( _xlimits[ 0 ], _ylimits[ 0 ], _xlimits[ 1 ], _ylimits[ 1 ] );
      g.drawLine( _xlimits[ 2 ], _ylimits[ 2 ], _xlimits[ 3 ], _ylimits[ 3 ] );
      g.drawLine( _xlimits[ 4 ], _ylimits[ 4 ], _xlimits[ 5 ], _ylimits[ 5 ] );
      g.drawLine( _xlimits[ 6 ], _ylimits[ 6 ], _xlimits[ 7 ], _ylimits[ 7 ] );
   }

}
