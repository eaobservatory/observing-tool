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

import gemini.sp.SpItem;
import gemini.sp.SpAvEditState;
import gemini.sp.iter.SpIterChop;

import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.fits.gui.FitsMouseEvent;

import jsky.app.ot.util.Angle;
import jsky.app.ot.util.PolygonD;
import jsky.app.ot.util.CoordSys;

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
   int    _xb, _yb;
   double _angle = 0;
 
/**
 * Construct with the base position at (xb,yb).
 */
public TpeChopDragObject(int xb, int yb, int x, int y)
{
   //System.out.println("BASE.........: (" + _xb + ", " + _yb + ")");
 
   _xb = xb;
   _yb = yb;
   _angle = getAngle(x,y);
 
   //System.out.println("INITIAL ANGLE: " + _angle);
}
 
public double
nextAngleDiff(int x, int y)
{
   double angle = getAngle(x, y);
 
   //System.out.println("NEXT ANGLE...: " + angle);
 
   double val   = angle - _angle;
   _angle = angle;
 
   //System.out.println("DIFF.........: " + val);
 
   return val;
}
 
public double
getAngle(int x, int y)
{
   double angle;
 
   // All the points are in screen coordinates, which means y increases down
   // This makes x and y relative to the origin in a right side up frame.
   int xp = x - _xb;
   int yp = _yb - y;
 
   int xa = Math.abs( xp );
   int ya = Math.abs( yp );
 
   if (xa == 0) {
      if (yp >= 0) {
         return Math.PI * 0.5;
      } else {
         return Math.PI * 1.5;
      }
   }
 
   angle = Angle.atanRadians( ((double) ya) / ((double) xa) );
 
   if ((xp > 0)  && (yp >= 0)) {
      return angle;
   }
 
   if ((xp < 0) && (yp >= 0)) {
      return Math.PI - angle;
   }
 
   if ((xp < 0) && (yp < 0)) {
      return Math.PI + angle;
   }
 
   return Math.PI * 2.0 - angle;
}
 
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
   private double        _chopRadius   = 0;
   private double        _baseX        = 0;
   private double        _baseY        = 0;

   private boolean       _drawAsCircle = false;

   private TpeSciArea    _sciArea;
   private PolygonD      _sciAreaPD;
   private PolygonD      _tickMarkPD;
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

/*   if (_sciArea == null) {
      _sciArea = _iw.getSciArea();
      if (_sciArea == null) {
         return false;
      }
   }*/


   int chopStepIndex = spIterChop.getSelectedIndex();
   String coordFrame = spIterChop.getCoordFrame(chopStepIndex);

   _drawAsCircle = drawAsCircle(coordFrame);

   _baseX = (double) fii.baseScreenPos.x;
   _baseY = (double) fii.baseScreenPos.y;

   _chopX = (spIterChop.getThrow(chopStepIndex) * Math.sin(((180 + spIterChop.getAngle(chopStepIndex)) / 360) * (Math.PI * 2.0))) + _baseX;
   _chopY = (spIterChop.getThrow(chopStepIndex) * Math.cos(((180 + spIterChop.getAngle(chopStepIndex)) / 360) * (Math.PI * 2.0))) + _baseY;

   _chopRadius = spIterChop.getThrow(chopStepIndex);
   

//   boolean updated = _sciArea.update(spIterChop, fii);
 
   if (_valid && /*!updated &&*/ (_sciAreaPD != null) && (spIterChop == _iterChop)) {
      // Already have the current values
      return true;
   }
 
   _iterChop = spIterChop;

//   double xBase = (double) fii.baseScreenPos.x;
//   double yBase = (double) fii.baseScreenPos.y;
//   _sciAreaPD   = _sciArea.getPolygonDAt(xBase, yBase);

   // Init the _tickMarkPD
//   if (_tickMarkPD == null) {
//      double[] xpoints = new double[4];
//      double[] ypoints = new double[4];
//      _tickMarkPD = new PolygonD(xpoints, ypoints, 4);
//   }

//   double x = xBase;
//   double y = yBase - _sciArea.height/2.0;

//   _tickMarkPD.xpoints[0] = x;
//   _tickMarkPD.ypoints[0] = y - MARKER_SIZE*2;

//   _tickMarkPD.xpoints[1] = x - MARKER_SIZE;
//   _tickMarkPD.ypoints[1] = y - 2;

//   _tickMarkPD.xpoints[2] = x + MARKER_SIZE;
//   _tickMarkPD.ypoints[2] = y - 2;

//   _tickMarkPD.xpoints[3] = _tickMarkPD.xpoints[0];
//   _tickMarkPD.ypoints[3] = _tickMarkPD.ypoints[0];

//   _iw.skyRotate(_tickMarkPD, _sciArea.posAngleRadians);

   

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

   if(_drawAsCircle) {
     g.drawArc((int)(_baseX - _chopRadius), (int)(_baseY - _chopRadius),
               (int)(2 * _chopRadius), (int)(2 * _chopRadius), 0, 360);
   }
   else {
     g.drawRect((int)_chopX - 2, (int)_chopY - 2, 4, 4);
   }

//   g.setFont(FONT);
   g.drawString(_name, (int)_chopX + 3, (int)_chopY + 2);


//   g.drawPolygon(new Polygon(new int[] { 10, 100, 20 }, new int[] {20, 90, 40}, 3)); //_sciAreaPD.getAWTPolygon());
   //g.fillPolygon(_tickMarkPD.getAWTPolygon());

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
   if ((_sciAreaPD == null) || (_tickMarkPD == null)) {
      return false;
   }

   _dragObject = null;

   // See if dragging by the corner
   for (int i=0; i<(_sciAreaPD.npoints-1); ++i) {
      int cornerx = (int) (_sciAreaPD.xpoints[i] + 0.5);
      int cornery = (int) (_sciAreaPD.ypoints[i] + 0.5);
 
     int dx = Math.abs(cornerx - fme.xWidget);
      if (dx > MARKER_SIZE) {
         continue;
      }
      int dy = Math.abs(cornery - fme.yWidget);
      if (dy > MARKER_SIZE) {
         continue;
      }

      Point2D.Double p = fii.baseScreenPos;
      _dragObject = new TpeChopDragObject((int)p.x, (int)p.y, cornerx, cornery);
   }

   // See if dragging by the tick mark (give a couple extra pixels to make it
   // easier to grab)
   if (_dragObject == null) {
      int x  = (int) (_tickMarkPD.xpoints[0] + 0.5);
      int dx = Math.abs(x - fme.xWidget);
      if (dx <= MARKER_SIZE + 2) {
         int y0 = (int) (_tickMarkPD.ypoints[0] + 0.5);
         int y1 = (int) (_tickMarkPD.ypoints[1] + 0.5);
         int y = (y0 + y1)/2;
         int dy = Math.abs(y - fme.yWidget);
         if (dy <= MARKER_SIZE + 2) {
            Point2D.Double p = fii.baseScreenPos;
            _dragObject = new TpeChopDragObject((int)p.x, (int)p.y, x, y);
         }
      }
   }

   _dragging = (_dragObject != null);
   if (_dragging) {
      _dragX    = fme.xWidget;
      _dragY    = fme.yWidget;
      _iterChop.getAvEditFSM().setEachEditNotifies(false);
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

      double diff = _dragObject.nextAngleDiff(fme.xWidget, fme.yWidget);
      //_iterChop.addPosAngleRadians(diff);
   }
}
 
/**
 * Stop dragging.
 */
public void dragStop(FitsMouseEvent fme)
{
   if (_dragObject != null) {
      _iterChop.getAvEditFSM().setEachEditNotifies(true);
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

}

