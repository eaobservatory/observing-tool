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

import gemini.util.Angle;
import gemini.util.PolygonD;

/**
 * Draws the field of view of the acquisition camera.
 */
public class TpeAcqCameraFeature extends TpeImageFeature
{
   public static final double FOV_WIDTH  = 72.0;
   public static final double FOV_HEIGHT = 54.0;

   private PolygonD _fovAreaPD;
   private boolean  _valid = false;

/**
 * Construct the feature with its name and description.
 */
public TpeAcqCameraFeature()
{
   super("Acq Cam", "Field of view of the Acquistion Camera.");
}

/**
 * Reinit.
 */
public void
reinit(TpeImageWidget iw, FitsImageInfo fii)
{
   super.reinit(iw, fii);
   _valid = false;
}

/**
 * The position angle has changed.
 */
public void
posAngleUpdate(FitsImageInfo fii)
{
   _valid = false;
}

/**
 * Calculate the polygon describing the screen location of the science area.
 */
private void
_calc(FitsImageInfo fii)
{
   if (_fovAreaPD == null) {
      _fovAreaPD = new PolygonD();
      _fovAreaPD.xpoints = new double[5];
      _fovAreaPD.ypoints = new double[5];
      _fovAreaPD.npoints = 5;
   }

   double[] xpoints = _fovAreaPD.xpoints;
   double[] ypoints = _fovAreaPD.ypoints;

   double x = (double) fii.baseScreenPos.x;
   double y = (double) fii.baseScreenPos.y;

   double w = (fii.pixelsPerArcsec * FOV_WIDTH)/2.0;
   double h = (fii.pixelsPerArcsec * FOV_HEIGHT)/2.0;

   xpoints[0] = x - w;	xpoints[1] = x + w;
   ypoints[0] = y - h;	ypoints[1] = y - h;

   xpoints[2] = x + w;	xpoints[3] = x - w;
   ypoints[2] = y + h;	ypoints[3] = y + h;

   xpoints[4] = xpoints[0];
   ypoints[4] = ypoints[0];

//   _iw.skyRotate(_fovAreaPD, Angle.degreesToRadians(fii.posAngleDegrees));
   _valid = true;
}

/**
 * Draw the feature.
 */
public void
draw(Graphics g, FitsImageInfo fii)
{
   if (!_valid) {
      _calc(fii);
   }

   g.setColor(Color.lightGray);
   g.drawPolygon(_fovAreaPD.getAWTPolygon());
}

}







