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

import gemini.sp.SpTelescopePosList;

import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.fits.gui.FitsPosMapEntry;
import jsky.app.ot.fits.gui.FitsMouseEvent;

import jsky.app.ot.tpe.TpeImageFeature;
import jsky.app.ot.tpe.TpeDraggableFeature;
import jsky.app.ot.tpe.TpeEraseableFeature;
import jsky.app.ot.tpe.TpeSelectableFeature;

import jsky.app.ot.tpe.TpeImageWidget;
import jsky.app.ot.tpe.TpePositionMap;
import java.awt.geom.Point2D;

/**
 * A base class for all telescope positions.  The <tt>draw</tt> method
 * of TpeImageFeature and the <tt>dragStart</tt> method of
 * TpeDraggableFeature are left for subclasses.  This class
 * handles the common problem of dragging a position, once that position
 * has been located to start the drag.
 */
public abstract class TpePositionFeature extends TpeImageFeature
     implements TpeDraggableFeature, TpeEraseableFeature, TpeSelectableFeature
{
   protected FitsPosMapEntry _dragObject;

/**
 * Construct the feature with its name and description. 
 */
public TpePositionFeature(String name, String descr)
{
   super(name, descr);
}

/**
 */
public final SpTelescopePosList
getSpTelescopePosList()
{
    TpePositionMap pm = TpePositionMap.getMap(_iw);
    return (SpTelescopePosList) pm.getTelescopePosList();
}

/**
 */
public boolean
positionIsClose(FitsPosMapEntry pme, int x, int y)
{
   Point2D.Double p = pme.screenPos;
   if (p == null) {
      return false;
   }

   double dx = Math.abs(p.x - x);
   if (dx > MARKER_SIZE) {
      return false;
   }

   double dy = Math.abs(p.y - y);
   if (dy > MARKER_SIZE) {
      return false;
   }
   return true;
}


/**
 */
public void
drag(FitsMouseEvent fme)
{
   if (_dragObject != null) {
      _dragObject.screenPos.x = fme.xWidget;
      _dragObject.screenPos.y = fme.yWidget;
   }

   _iw.repaint();
}

/**
 */
public void
dragStop(FitsMouseEvent fme)
{
   if (_dragObject != null) {

      // Make sure to update the telescope position and let observers be
      // notified.

      TpePositionMap pm = TpePositionMap.getMap(_iw);
      pm.updatePosition(_dragObject, fme);
      _dragObject = null;
   }
}

}

