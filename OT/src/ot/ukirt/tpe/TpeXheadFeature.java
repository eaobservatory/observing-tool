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
public class TpeXheadFeature extends TpeImageFeature
{
   // For now, just make these constants static members.  Eventually
   // read from a configuration file?
   public static final double XHEAD_OUTER_RADIUS = 274.6;  // arcsec
   public static final double XHEAD_INNER_RADIUS = 261.4;  // arcsec

   private Rectangle _xheadi = new Rectangle();
   private Rectangle _xheado = new Rectangle();
   private boolean  _valid = false;

/**
 * Construct the feature with its name and description. 
 */
public TpeXheadFeature()
{
   super("Xhead", "Range of the Xhead movement.");
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
 * Calculate the polygon describing the screen location of the science area.
 */
private void
_calc(FitsImageInfo fii)
{

   // Get the instrument aperture X & Y. These offset the origin of the
   // x-head range.
   int xoffpix = 0;
   int yoffpix = 0;
   SpUKIRTInstObsComp _inst = (SpUKIRTInstObsComp) _iw.getInstrumentItem();
   if (_inst != null) {
       double xoff = _inst.getInstApXarcsec();
       double yoff = _inst.getInstApYarcsec();
       xoffpix = (int) (fii.pixelsPerArcsec * xoff + 0.5);
       yoffpix = (int) (fii.pixelsPerArcsec * yoff + 0.5);
   }

   int radius,size;

   radius = (int) (fii.pixelsPerArcsec * XHEAD_OUTER_RADIUS + 0.5);
   size   = 2 * radius;
   
   _xheado.x      = (int)fii.baseScreenPos.x - radius - xoffpix;
   _xheado.y      = (int)fii.baseScreenPos.y - radius - yoffpix;
   _xheado.width  = size;
   _xheado.height = size;

   radius = (int) (fii.pixelsPerArcsec * XHEAD_INNER_RADIUS + 0.5);
   size   = 2 * radius;
   _xheadi.x      = (int)fii.baseScreenPos.x - radius - xoffpix;
   _xheadi.y      = (int)fii.baseScreenPos.y - radius - yoffpix;
   _xheadi.width  = size;
   _xheadi.height = size;
   _valid = true;

}

/**
 * The position angle has changed. Here this is actually used to check changes
 * in other things, particularly the instrument aperture.
 */
public void
posAngleUpdate(FitsImageInfo fii)
{
   _valid = false;
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

   g.setColor(Color.magenta);
   g.drawOval(_xheado.x, _xheado.y, _xheado.width, _xheado.height);
   g.drawOval(_xheadi.x, _xheadi.y, _xheadi.width, _xheadi.height);
}

}
