// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe.feat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import jsky.app.ot.gui.DrawUtil;

import gemini.util.CoordSys;
import gemini.sp.SpItem;
import gemini.sp.SpAvEditState;
import gemini.sp.iter.SpIterChop;

import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.fits.gui.FitsMouseEvent;

import jsky.app.ot.util.Angle;
import jsky.app.ot.util.PolygonD;

import jsky.app.ot.tpe.TpeImageFeature;
import jsky.app.ot.tpe.TpeDraggableFeature;
import jsky.app.ot.tpe.TpeImageWidget;
import jsky.app.ot.tpe.TpeSciArea;
import java.awt.geom.Point2D;

/**
 * An implementation class used to simplify the job of rotating.
 */
final class TpeChopDragObject
{
//   int    _xb, _yb;
//   double _angle = 0;
 
/**
 * Construct with the base position at (xb,yb).
 */
/*public TpeChopDragObject(double xOffset, double yOffset)
{
   //System.out.println("BASE.........: (" + _xb + ", " + _yb + ")");
 
   _xb = xb;
   _yb = yb;
   _angle = getAngle(x,y);
 
   //System.out.println("INITIAL ANGLE: " + _angle);
}*/
 
/*public double
nextAngleDiff(int x, int y)
{
   double angle = getAngle(x, y);
 
   //System.out.println("NEXT ANGLE...: " + angle);
 
   double val   = angle - _angle;
   _angle = angle;
 
   //System.out.println("DIFF.........: " + val);
 
   return val;
}
*/

}


/**
 * Draws the Chop position.
 *
 * @author Modified for JCMT OT by Martin Folger (M.Folger@roe.ac.uk),
 *         based on Gemini Science Area feature.
 */
public class TpeChopFeature extends TpeImageFeature
                            implements TpeDraggableFeature
{

   private double        _chopX        = 0;
   private double        _chopY        = 0;
   private double        _chopInnerRadius = 0;
   private double        _chopOuterRadius = 0;
   private double        _baseX        = 0;
   private double        _baseY        = 0;

   private boolean       _drawAsCircle = false;

   private SpIterChop    _iterChop;

   private boolean       _valid = false;

   private TpeChopDragObject _dragObject;
   private boolean _dragging = false;
   private int     _dragX;
   private int     _dragY;

/**
 * Construct the feature with its name and description. 
 */
public TpeChopFeature()
{
   super("Chop", "Chop iterator position.");
}

/**
 * Reinit.
 */
public void
reinit(TpeImageWidget iw, FitsImageInfo fii)
{
   //System.out.println("TpeChopFeature.reinit");
   super.reinit(iw, fii);
   _valid = false;
   return;
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
private boolean
_calc(FitsImageInfo fii)
{
   //System.out.println("TpeChopFeature._calc(): " + fii);

   // Need the chop iterator to know what to draw.
   SpIterChop spIterChop = (SpIterChop)_iw.getBaseItem();
   if ((spIterChop == null) || (spIterChop.getSelectedIndex() < 0)) {
      return false;
   }

   int chopStepIndex = spIterChop.getSelectedIndex();
   String coordFrame = spIterChop.getCoordFrame(chopStepIndex);

   _drawAsCircle = drawAsCircle(coordFrame);

   _baseX = (double) fii.baseScreenPos.x;
   _baseY = (double) fii.baseScreenPos.y;

   _chopX = spIterChop.getThrow(chopStepIndex) * Math.sin(((180 + spIterChop.getAngle(chopStepIndex)) / 360) * (Math.PI * 2.0));
   _chopY = spIterChop.getThrow(chopStepIndex) * Math.cos(((180 + spIterChop.getAngle(chopStepIndex)) / 360) * (Math.PI * 2.0));

   _chopX      *= fii.pixelsPerArcsec;
   _chopY      *= fii.pixelsPerArcsec;

   _chopX += _baseX;
   _chopY += _baseY;

   double [] scienceArea = null;

   if(_iw.getInstrumentItem() != null) {
     scienceArea = _iw.getInstrumentItem().getScienceArea();
   }

   double scienceAreaRadius = 0.0;

   if(scienceArea != null) {
      if(scienceArea.length == 1) {
         scienceAreaRadius = scienceArea[0];
      }
      else {
         scienceAreaRadius = Math.sqrt((scienceArea[0] * scienceArea[0]) + (scienceArea[1] * scienceArea[1]));
      }
   }
   
   _chopInnerRadius = spIterChop.getThrow(chopStepIndex) - scienceAreaRadius;
   _chopInnerRadius *= fii.pixelsPerArcsec;

   _chopOuterRadius = spIterChop.getThrow(chopStepIndex) + scienceAreaRadius;
   _chopOuterRadius *= fii.pixelsPerArcsec;

 
   if (_valid && (spIterChop == _iterChop)) {
      // Already have the current values
      return true;
   }
 
   _iterChop = spIterChop;

   _valid = true;
   return true;
}

/**
 * Draw the feature.
 */
public void
draw(Graphics g, FitsImageInfo fii)
{
   if (!_calc(fii)) {
      return;
   }

   g.setColor(Color.magenta);

   double [] scienceArea = null;

   if(_iw.getInstrumentItem() != null) {
     scienceArea = _iw.getInstrumentItem().getScienceArea();

     // ScienceArea is circular (approximately circular detector/array as opposed to
     // circular representation of science area due to al/az mounting)
     if(scienceArea.length == 1) {
       double radius = scienceArea[0] * fii.pixelsPerArcsec;
       g.drawArc((int)(_baseX - radius), (int)(_baseY - radius),
                 (int)(2 * radius), (int)(2 * radius), 0, 360);

       g.drawArc((int)(_chopX - radius), (int)(_chopY - radius),
                 (int)(2 * radius), (int)(2 * radius), 0, 360);

     }
     // Science Area is retangular.
     else {

       if(_drawAsCircle) {
         double radius = Math.sqrt((scienceArea[0] * scienceArea[0]) +
                                 (scienceArea[1] * scienceArea[1]));

         radius *= fii.pixelsPerArcsec;

         g.drawArc((int)(_baseX - radius), (int)(_baseY - radius),
                   (int)(2 * radius), (int)(2 * radius), 0, 360);

         g.drawArc((int)(_chopX - radius), (int)(_chopY - radius),
                   (int)(2 * radius), (int)(2 * radius), 0, 360);
       }
       else {
         double w = scienceArea[0] * fii.pixelsPerArcsec;
	 double h = scienceArea[1] * fii.pixelsPerArcsec;

         g.drawRect((int)(_baseX - (w / 2.0)),
                    (int)(_baseY - (h / 2.0)),
		    (int)w,
		    (int)h);

         g.drawRect((int)(_chopX - (w / 2.0)),
                    (int)(_chopY - (h / 2.0)),
		    (int)w,
		    (int)h);
       }
     }
   }

// Draw the chop position, either as a small box or as a circle (coordinate system Az/El)

   if(_drawAsCircle) {
     g.drawArc((int)(_baseX - _chopInnerRadius), (int)(_baseY - _chopInnerRadius),
               (int)(2 * _chopInnerRadius), (int)(2 * _chopInnerRadius), 0, 360);

     g.drawArc((int)(_baseX - _chopOuterRadius), (int)(_baseY - _chopOuterRadius),
               (int)(2 * _chopOuterRadius), (int)(2 * _chopOuterRadius), 0, 360);
   }

   g.drawRect((int)_chopX - 2, (int)_chopY - 2, 4, 4);

//   g.setFont(FONT);
   g.drawString(_name + " (Step " + _iterChop.getSelectedIndex() + ")", (int)_chopX + 3, (int)_chopY + 2);

   if (_dragging) {
      // Draw a little above the mouse
      int baseX = _dragX;
      int baseY = _dragY - 10;

      // Draw a string displaying the rotation angle
//      String s = _instItem.getPosAngleDegreesStr();
//      DrawUtil.drawString(g, s, Color.magenta, Color.black, baseX, baseY);
   }

}


/**
 * Start dragging the object.
 */
public boolean
dragStart(FitsMouseEvent fme, FitsImageInfo fii)
{
   _dragObject = new TpeChopDragObject();

   _dragging = (_dragObject != null);
   if (_dragging) {
      _dragX    = fme.xWidget;
      _dragY    = fme.yWidget;

// Should table be updated on dragStop only?
//    _iterChop.getAvEditFSM().setEachEditNotifies(false);
   }

   return _dragging;
}
 
/**
 * Drag to a new location.
 */
public void drag(FitsMouseEvent fme)
{
   if (_dragObject != null) {
      _dragX = fme.xWidget;
      _dragY = fme.yWidget;

      _iterChop.setAngle(getAngle(fme.xOffset, fme.yOffset), _iterChop.getSelectedIndex());
      _iterChop.setThrow(getThrow(fme.xOffset, fme.yOffset), _iterChop.getSelectedIndex());
   }

   _iw.repaint();

}
 
/**
 * Stop dragging.
 */
public void dragStop(FitsMouseEvent fme)
{
   if (_dragObject != null) {
// Should table be updated on dragStop only?
//      _iterChop.getAvEditFSM().setEachEditNotifies(true);
      _dragging = false;
      drag(fme);
      _dragObject = null;
   }
}

/**
 * Return true if the chop position should be drawn as circle
 * given the coordFrame.
 */
protected boolean drawAsCircle(String coordFrame) {
   if(coordFrame.equals(CoordSys.COORD_SYS[CoordSys.FK5]) ||
      coordFrame.equals(CoordSys.COORD_SYS[CoordSys.FK4])) {

      return false;
   }
   else {
      return true;
   }
}


protected static double
getAngle(double x, double y)
{
   double angle;
 
   double xa = Math.abs( x );
   double ya = Math.abs( y );
 
   if (xa == 0) {
      if (y >= 0) {
         return 0.0;
      } else {
         return 180.0;
      }
   }
 
   angle = (Angle.atanRadians(ya / xa) * 360) / (Math.PI * 2.0);
 
   if ((x > 0)  && (y >= 0)) {
      return 90.0 - angle;
   }
 
   if ((x < 0) && (y >= 0)) {
      return 270.0 + angle;
   }
 
   if ((x < 0) && (y < 0)) {
      return 270.0 - angle;
   }
 
   return 90.0 + angle;
}

protected static double
getThrow(double x, double y)
{
   return Math.sqrt((x * x) + (y * y));
}


}

