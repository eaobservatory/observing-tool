// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.ukirt.tpe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import jsky.app.ot.tpe.TpeImageFeature;
import jsky.app.ot.tpe.TpeImageWidget;

import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.fits.gui.FitsMouseEvent;

import jsky.app.ot.util.Angle;
import jsky.app.ot.util.PolygonD;

import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.SpTreeMan;
import orac.ukirt.inst.SpUKIRTInstObsComp;

/**
 * Draws the location of the dichroic on UKIRT.
 */
public class TpeDichroicFeature extends TpeImageFeature {

// These have not been remeasured on the plot, but only
// serve to indicate where the brightness of the guide
// start would be dimmed by the dichroic.
   public static final double FOV_WIDTHL  = 129.63;
   public static final double FOV_WIDTHR  = 129.63;

// The axis of the dichroic is not perfectly aligned with the
// optical axis.  Thus there is a pair of positions of the
// two sides of the dichroic frame for each telescope port.
// These parameters are taken from measurements by Russell Kackley.
// The units for all is arcseconds.  The West and North port each 
// had their lower and upper values interchanged to match the
// plans.
   public static final double FOV_HEIGHTU_WEST  = 117.4;
   public static final double FOV_HEIGHTL_WEST  = 132.5;
   public static final double FOV_HEIGHTU_NORTH = 108.0;
   public static final double FOV_HEIGHTL_NORTH = 141.8;
   public static final double FOV_HEIGHTU_EAST  = 125.6;
   public static final double FOV_HEIGHTL_EAST  = 124.2;
   public static final double FOV_HEIGHTU_SOUTH = 133.6;
   public static final double FOV_HEIGHTL_SOUTH = 116.3;

// The out-of-bounds limits for the top and bottom arms.
// The units is again arcseconds.
   public static final double OOBT_WIDTH = 468.0;
   public static final double OOBT_HEIGHT = 20.9;
   public static final double OOBB_WIDTH = 468.0;
   public static final double OOBB_HEIGHT = 16.9;

   private PolygonD _fovAreaPD;
   private PolygonD _oobtArea;
   private PolygonD _oobbArea;
   private boolean  _valid = false;

/**
 * Construct the feature with its name and description.
 */
   public TpeDichroicFeature() {
      super("Dichroic", "Location of the Dichroic.");
   }

/**
 * Reinit.
 */
   public void reinit( TpeImageWidget iw, FitsImageInfo fii ) {
      super.reinit( iw, fii );
      _valid = false;
   }

/**
 * The position angle has changed.
 */
   public void posAngleUpdate( FitsImageInfo fii ) {
      _valid = false;
   }

/**
 * Calculate the polygon describing the screen location of the dichroic.
 */
   private void _calc( FitsImageInfo fii ) {

      if (_fovAreaPD == null) {
         _fovAreaPD = new PolygonD();
         _fovAreaPD.xpoints = new double[ 5 ];
         _fovAreaPD.ypoints = new double[ 5 ];
         _fovAreaPD.npoints = 5;
      }

      double[] xpoints = _fovAreaPD.xpoints;
      double[] ypoints = _fovAreaPD.ypoints;

      double x = (double) fii.baseScreenPos.x;
      double y = (double) fii.baseScreenPos.y;

      double wl = fii.pixelsPerArcsec * FOV_WIDTHL;
      double wr = fii.pixelsPerArcsec * FOV_WIDTHR;

// Set some defaults for the dichroic out-of-bounds area heights and
// instrument-aperture offsets.
      double hu = fii.pixelsPerArcsec * FOV_HEIGHTU_WEST;
      double hl = fii.pixelsPerArcsec * FOV_HEIGHTL_WEST;

      int xoffpix = 0;
      int yoffpix = 0;

// Get the instrument to determine which "port" it is on.
      SpUKIRTInstObsComp _inst = (SpUKIRTInstObsComp) _iw.getInstrumentItem();

      if ( _inst != null ) {

// Get the instrument-aperture X & Y.  These offset the origin of the dichroic
// in the opposite direction..
         double xoff = -1.0 * _inst.getInstApXarcsec();
         double yoff = -1.0 * _inst.getInstApYarcsec();
         xoffpix = (int) ( fii.pixelsPerArcsec * xoff + 0.5 );
         yoffpix = (int) ( fii.pixelsPerArcsec * yoff + 0.5 );

// For each port determine the widths of the dichroic supports in
// pixels, and the base positions corrected for the instrument-aperture
// offsets.  The latter are derived using the rotation matrix for each
// port (West is at -90 degrees, East at +90, South at 180 and North
// at 0), as the image pixels are oriented with north at the top. 
         if ( _inst.getPort().equalsIgnoreCase( "West" ) ) {
            hu = fii.pixelsPerArcsec * FOV_HEIGHTU_WEST;
            hl = fii.pixelsPerArcsec * FOV_HEIGHTL_WEST;
            x = x + yoffpix;
            y = y - xoffpix;

         } else if ( _inst.getPort().equalsIgnoreCase( "South" ) ) {
            hu = fii.pixelsPerArcsec * FOV_HEIGHTU_SOUTH;
            hl = fii.pixelsPerArcsec * FOV_HEIGHTL_SOUTH;
            x = x - xoffpix;
            y = y - yoffpix;

         } else if ( _inst.getPort().equalsIgnoreCase( "East" ) ) {
            hu = fii.pixelsPerArcsec * FOV_HEIGHTU_EAST;
            hl = fii.pixelsPerArcsec * FOV_HEIGHTL_EAST;
            x = x - yoffpix;
            y = y + xoffpix;

         } else if ( _inst.getPort().equalsIgnoreCase( "North" ) ) {
            hu = fii.pixelsPerArcsec * FOV_HEIGHTU_NORTH;
            hl = fii.pixelsPerArcsec * FOV_HEIGHTL_NORTH;
            x = x + xoffpix;
            y = y + yoffpix;
         }

      }

// Define the field-of-view co-ordinates in pixels.
      xpoints[ 0 ] = x - wl;  xpoints[ 1 ] = x + wr;
      ypoints[ 0 ] = y - hl;  ypoints[ 1 ] = y - hl;

      xpoints[ 2 ] = x + wr;  xpoints[ 3 ] = x - wl;
      ypoints[ 2 ] = y + hu;  ypoints[ 3 ] = y + hu;

      xpoints[ 4 ] = xpoints[ 0 ];
      ypoints[ 4 ] = ypoints[ 0 ];

// Create a polygon defining the top out-of-bounds area.
// =====================================================
      if (_oobtArea == null) {
         _oobtArea = new PolygonD();
         _oobtArea.xpoints = new double[ 5 ];
         _oobtArea.ypoints = new double[ 5 ];
         _oobtArea.npoints = 5;
      }

      double w = fii.pixelsPerArcsec * OOBT_WIDTH;
      double w2 = fii.pixelsPerArcsec * OOBT_WIDTH/2.0;
      double h = fii.pixelsPerArcsec * OOBT_HEIGHT;
      double h2 = fii.pixelsPerArcsec * OOBT_HEIGHT/2.0;

      _oobtArea.xpoints[ 0 ] = x - w2;
      _oobtArea.xpoints[ 1 ] = x + w2;
      _oobtArea.ypoints[ 0 ] = ypoints[ 0 ] - h; 
      _oobtArea.ypoints[ 1 ] = ypoints[ 1 ] - h;
      _oobtArea.xpoints[ 2 ] = x + w2;
      _oobtArea.xpoints[ 3 ] = x - w2;
      _oobtArea.ypoints[ 2 ] = ypoints[ 0 ];
      _oobtArea.ypoints[ 3 ] = ypoints[ 1 ];
      _oobtArea.xpoints[ 4 ] = _oobtArea.xpoints[ 0 ];
      _oobtArea.ypoints[ 4 ] = _oobtArea.ypoints[ 0 ];

// Create a polygon defining the bottom out-of-bounds area.
      if (_oobbArea == null) {
         _oobbArea = new PolygonD();
         _oobbArea.xpoints = new double[ 5 ];
         _oobbArea.ypoints = new double[ 5 ];
         _oobbArea.npoints = 5;
      }

      w = fii.pixelsPerArcsec * OOBB_WIDTH;
      w2 = fii.pixelsPerArcsec * OOBB_WIDTH / 2.0;
      h = fii.pixelsPerArcsec * OOBB_HEIGHT;
      h2 = fii.pixelsPerArcsec * OOBB_HEIGHT / 2.0;
      
      _oobbArea.xpoints[ 0 ] = x - w2;
      _oobbArea.xpoints[ 1 ] = x + w2;
      _oobbArea.ypoints[ 0 ] = ypoints[ 2 ];
      _oobbArea.ypoints[ 1 ] = ypoints[ 3 ];
      _oobbArea.xpoints[ 2 ] = x + w2;
      _oobbArea.xpoints[ 3 ] = x - w2;
      _oobbArea.ypoints[ 2 ] = ypoints[ 2 ] + h;
      _oobbArea.ypoints[ 3 ] = ypoints[ 3 ] + h;
      _oobbArea.xpoints[ 4 ] = _oobbArea.xpoints[ 0 ];
      _oobbArea.ypoints[ 4 ] = _oobbArea.ypoints[ 0 ];

// Rotate the dichroic depending on the "port" it is on.
      if ( _inst != null ) {
         if ( _inst.getPort().equalsIgnoreCase( "West" ) ) {
            _iw.skyRotate( _fovAreaPD, Angle.degreesToRadians( -90.0 ) );
            _iw.skyRotate( _oobtArea, Angle.degreesToRadians( -90.0 ) );
            _iw.skyRotate( _oobbArea, Angle.degreesToRadians( -90.0 ) );

         } else if ( _inst.getPort().equalsIgnoreCase( "South" ) ) {
            _iw.skyRotate( _fovAreaPD, Angle.degreesToRadians( 180.0 ) );
            _iw.skyRotate( _oobtArea, Angle.degreesToRadians( 180.0 ) );
            _iw.skyRotate( _oobbArea, Angle.degreesToRadians( 180.0 ) );

         } else if ( _inst.getPort().equalsIgnoreCase( "East" ) ) {
            _iw.skyRotate( _fovAreaPD, Angle.degreesToRadians( 90.0 ) );
            _iw.skyRotate( _oobtArea, Angle.degreesToRadians( 90.0 ) );
            _iw.skyRotate( _oobbArea, Angle.degreesToRadians( 90.0 ) );

         } else if ( _inst.getPort().equalsIgnoreCase( "North" ) ) {
            _iw.skyRotate( _fovAreaPD, Angle.degreesToRadians( 0.0 ) ) ;
            _iw.skyRotate( _oobtArea, Angle.degreesToRadians( 0.0 ) ) ;
            _iw.skyRotate( _oobbArea, Angle.degreesToRadians( 0.0 ) ) ;
         }
      }
      _valid = true;
   }

/**
 * Draw the feature.
 */
   public void draw( Graphics g, FitsImageInfo fii ) {
      if ( !_valid ) {
         _calc( fii );
      }

      g.setColor( Color.green );
      g.drawPolygon( _fovAreaPD.getAWTPolygon() );
      g.fillPolygon( _oobtArea.getAWTPolygon() );
      g.fillPolygon( _oobbArea.getAWTPolygon() );
   }

}
