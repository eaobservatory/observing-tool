// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe;

import jsky.app.ot.fits.gui.FitsImageInfo;
import gemini.sp.obsComp.SpInstObsComp;

import jsky.app.ot.util.PolygonD;
import jsky.app.ot.util.ScreenMath;

import java.awt.Polygon;

/**
 * Describes the science area and facilitates drawing, rotating it.
 */
public class TpeSciArea
{
   public double width;
   public double height;
   public double posAngleRadians;
   public double skyCorrection;

   public double radius;

   // scratch work variable
   private PolygonD _pd;

   private static final int RECTANGULAR    =  0;
   private static final int CIRCULAR       =  1;
   private int _shape = RECTANGULAR;

   /** Number of vertices of the Polygon that approximates the circle. */
   private static final int CIRCLE_NPOINTS = 64;

/**
 *
 */
public TpeSciArea()
{
   _pd = new PolygonD();
   _pd.xpoints = new double[5];
   _pd.ypoints = new double[5];
   _pd.npoints = 5;
}

/**
 * Update the SciArea fields, returning true iff changes were made.
 */
public boolean
update(SpInstObsComp spInst, FitsImageInfo fii)
{

   double[] ds = spInst.getScienceArea();

   // Only one element in array: Science area is circular.
   if(ds.length == 1) {
      _shape = CIRCULAR;
      double r = ds[0] * fii.pixelsPerArcsec;

      if(r != this.radius) {
         this.radius = r;
	 return true;
      }
   }
   // More than one element in array: Science area is recangular.
   else {
      _shape = RECTANGULAR;
      double w, h, posAngle, sky;

      w = ds[0] * fii.pixelsPerArcsec;
      h = ds[1] * fii.pixelsPerArcsec;

      posAngle = spInst.getPosAngleRadians();
      sky      = fii.theta;

      // Update the instance variables if necessary.
      if ((w	 != this.width		) ||
          (h	 != this.height		) ||
          (posAngle != this.posAngleRadians) ||
          (sky	 != this.skyCorrection	)	) {

         this.width		= w;
         this.height		= h;
         this.posAngleRadians	= posAngle;
         this.skyCorrection	= sky;
         return true;
      }
   }

   return false;
}

/**
 * Get an AWT Polygon object representing the science area at the
 * given x, y location, taking into account rotation.
 */
public Polygon
getPolygonAt(double x, double y)
{
   if(_shape == CIRCULAR) {
      return getPolygonDAt(x, y).getAWTPolygon();
   }
   else {
      double hw = width/2.0;
      double hh = height/2.0;

      PolygonD pd = _pd;
      double[] xpoints = pd.xpoints;
      double[] ypoints = pd.ypoints;

      xpoints[0] = x - hw;		xpoints[1] = x + hw;
      ypoints[0] = y - hh;		ypoints[1] = y - hh;

      xpoints[2] = x + hw;		xpoints[3] = x - hw;
      ypoints[2] = y + hh;		ypoints[3] = y + hh;

      xpoints[4] = xpoints[0];
      ypoints[4] = ypoints[0];

      ScreenMath.rotateRadians(pd, posAngleRadians + skyCorrection, x, y);
      return pd.getAWTPolygon();
   }   
}

/**
 * Get a PolygonD object representing the science area at the
 * given x, y location, taking into account rotation.
 */
public PolygonD
getPolygonDAt(double x, double y)
{
   if(_shape == CIRCULAR) {
      double [] xpoints = new double[CIRCLE_NPOINTS];
      double [] ypoints = new double[CIRCLE_NPOINTS];

      double a;

      for(int i = 0; i < CIRCLE_NPOINTS; i++) {
         a = ((Math.PI / (CIRCLE_NPOINTS / 2)) * i) + (Math.PI / CIRCLE_NPOINTS);

         xpoints[i] = (radius * Math.cos(a)) + x;
         ypoints[i] = (radius * Math.sin(a)) + y;
      }

      return new PolygonD(xpoints, ypoints, CIRCLE_NPOINTS);
   }
   else {
      double hw = width/2.0;
      double hh = height/2.0;

      PolygonD pd = _pd;
      double[] xpoints = pd.xpoints;
      double[] ypoints = pd.ypoints;

      xpoints[0] = x - hw;		xpoints[1] = x + hw;
      ypoints[0] = y - hh;		ypoints[1] = y - hh;

      xpoints[2] = x + hw;		xpoints[3] = x - hw;
      ypoints[2] = y + hh;		ypoints[3] = y + hh;

      xpoints[4] = xpoints[0];
      ypoints[4] = ypoints[0];

      ScreenMath.rotateRadians(pd, posAngleRadians + skyCorrection, x, y);
      return new PolygonD(pd);
   }  
}

}
