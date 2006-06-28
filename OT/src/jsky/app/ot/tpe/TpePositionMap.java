// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe;

import jsky.app.ot.fits.gui.FitsImageWidget;
import jsky.app.ot.fits.gui.FitsMouseEvent;
import jsky.app.ot.fits.gui.FitsPosMap;
import jsky.app.ot.fits.gui.FitsPosMapEntry;

import jsky.app.ot.gui.image.ImageView;

import gemini.sp.SpTelescopePos;
import gemini.sp.SpTelescopePosList;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import java.awt.Point;
import java.awt.geom.Point2D;



/**
 * An auxiliary class used to maintain a mapping between telescope positions
 * and image widget locations.  There is one map per image widget.
 */
public class TpePositionMap extends FitsPosMap
{
   private static Hashtable   _mapTable = new Hashtable();

   private boolean _findBase        = false;
   private boolean _findUserTargets = false;
   private boolean _findGuideStars  = false;

/**
 * Get the position map associated with the given image widget, creating
 * it if necessary.
 */
public static TpePositionMap
getMap(TpeImageWidget iw)
{
   TpePositionMap tpm = (TpePositionMap) _mapTable.get(iw);
   if (tpm == null) {
      tpm = new TpePositionMap(iw);
      _mapTable.put(iw, tpm);
   }
   return tpm;
}

/**
 * Get the map only if it already exists.
 */
public static TpePositionMap
getExistingMap(TpeImageWidget iw)
{
   return (TpePositionMap) _mapTable.get(iw);
}

/**
 * Remove the position map associated with the given image widget,
 * returning it if it exists.
 */
public static TpePositionMap
removeMap(TpeImageWidget iw)
{
   return (TpePositionMap) _mapTable.remove(iw);
}

/**
 * Construct with an image widget.
 */
public TpePositionMap(FitsImageWidget iw)
{
   super(iw);
}

/**
 * Turn on/off the ability to find the base position with a call to
 * <tt>locate</tt>.
 */
public void
setFindBase(boolean find)
{
   _findBase = find;
}

/**
 * Turn on/off the ability to find a guide star with a call to
 * <tt>locate</tt>.
 */
public void
setFindGuideStars(boolean find)
{
   _findGuideStars = find;
}


/**
 * Turn on/off the ability to find a user position with a call to
 * <tt>locate</tt>.
 */
public void
setFindUserTarget(boolean find)
{
   _findUserTargets = find;
}


/**
 * Find a (visible) position under the given x,y location.
 */
public FitsPosMapEntry
locate(int x, int y)
{
   Hashtable posTable = getPosTable();
   if (posTable == null) return null;
 
   Enumeration enumeration = posTable.elements();
   while (enumeration.hasMoreElements()) {
      FitsPosMapEntry pme = (FitsPosMapEntry)enumeration.nextElement();
      Point2D.Double p = pme.screenPos;
      if (p == null) {
         continue;
      }
 
      // Is this position under the mouse indicator?
      double dx = Math.abs(p.x - x);
      if (dx > MARKER_SIZE) {
         continue;
      }
      double dy = Math.abs(p.y - y);
      if (dy > MARKER_SIZE) {
         continue;
      }
 
      // Is this position visible?
      SpTelescopePos tp = (SpTelescopePos) pme.telescopePos;
      if (tp.isBasePosition()) {
         if (_findBase) { return pme; } else { continue; }
      }
      if (tp.isUserPosition()) {
         if (_findUserTargets) { return pme; } else { continue; }
      }
      if (tp.isGuidePosition()) {
         if (_findGuideStars) { return pme; } else { continue; }
      }
   }
   return null;
}

}

