// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.fits.gui;

import jsky.app.ot.gui.image.ImageView;
import jsky.app.ot.gui.image.ViewportViewObserver;
import jsky.app.ot.gui.image.ViewportImageWidget;

import jsky.app.ot.util.RADecMath;
import gemini.util.TelescopePos;
import gemini.util.TelescopePosList;
import gemini.util.TelescopePosListWatcher;
import gemini.util.TelescopePosWatcher;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.awt.geom.Point2D;


/**
 * An auxiliary class used to maintain a mapping between telescope positions
 * and image widget locations.
 */
public class FitsPosMap implements ViewportViewObserver,
                                   TelescopePosListWatcher, TelescopePosWatcher
{
   public static final int MARKER_SIZE = 4;   // FIX THIS ...

   protected FitsImageWidget   _iw;
   protected TelescopePosList  _tpl;
   protected Hashtable         _posTable = new Hashtable();

   protected boolean _valid = false;

/**
 * Construct with an image widget.
 */
public FitsPosMap( FitsImageWidget iw )
{
   _iw = iw;

   // Need to know when the view changes so that the position map
   // can be updated with the correct locations of the positions.
   _iw.addViewObserver(this);
}


/**
 * Free any resources held by this position map.
 */
public void
free()
{
   _iw.deleteViewObserver(this);

   _stopObservingPosList();

   _tpl = null;
   _posTable.clear();
   _iw  = null;
}

/**
 */
private void
_stopObservingPosList()
{
   // Quit observing any of the previous positions.
   if (_tpl != null) {
      _tpl.deleteWatcher(this);

      TelescopePos[] tpA = _tpl.getAllPositions();
      for (int i=0; i<tpA.length; ++i) {
         TelescopePos tp = tpA[i];
         tp.deleteWatcher(this);
      }
   }
}

/**
 * Reset state to manage a new position list.
 */
public void
reset(TelescopePosList tpl)
{
   if (tpl == _tpl) {
      return;  // Nothing to do
   }
 
   _stopObservingPosList();
 
   _tpl   = tpl;
   _valid = false;

   if (_tpl == null) {
      return;
   }

   if (getPosTable() != null) {
      _iw.repaint();
   }
}

/**
 * Get the position table, initializing if necessary.
 */
public Hashtable
getPosTable()
{
   if (_valid) {
      return _posTable;
   }

   if (_iw.isInitialized() && _initPosTable()) {
      _valid = true;
      return _posTable;
   }
   return null;
}


/**
 * Get the TelescopePosList currently associated with this map.
 */
public TelescopePosList
getTelescopePosList()
{
   return _tpl;
}

/**
 */
public void
updatePosition(FitsPosMapEntry pme, FitsMouseEvent fme)
{
   pme.screenPos.x = fme.xWidget;
   pme.screenPos.y = fme.yWidget;
 
   TelescopePos tp = pme.telescopePos;
 
   tp.deleteWatcher(this);
 
      if (tp.isOffsetPosition()) {
         tp.setXY(fme.xOffset, fme.yOffset);
      } else {
         tp.setXY(fme.ra, fme.dec);
      }
 
   tp.addWatcher(this);
 
   _iw.repaint();
}

/**
 * Is the given x/y location close to the position with the given tag?
 */
public boolean
isClose(int x, int y, String tag)
{
   FitsPosMapEntry pme = getPositionMapEntry( tag );
   if (pme == null) {
      return false;
   }

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
 * Find a (visible) position under the given x,y location.
 */
public FitsPosMapEntry
locate(int x, int y)
{
   Hashtable posTable = getPosTable();
   if (posTable == null) return null;
 
   Enumeration enum = posTable.elements();
   while (enum.hasMoreElements()) {
      FitsPosMapEntry pme = (FitsPosMapEntry) enum.nextElement();
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
 
      return pme;
   }
   return null;
}

/**
 * Get the FitsPosMapEntry corresponding with the telescope position
 * with the given tag.
 */
public FitsPosMapEntry
getPositionMapEntry( String tag )
{
   Hashtable posTable = getPosTable();
   if (posTable == null) return null;

   return (FitsPosMapEntry) posTable.get(tag);
}
 
/**
 * Get an Enumeration of all the PositionMapEntries.
 */
public final Enumeration
getAllPositionMapEntries()
{
   Hashtable posTable = getPosTable();
   if (posTable == null) return (new Hashtable()).elements();

   return posTable.elements();
}

/**
 * Find a TelescopePos under the given x,y location.
 */
public TelescopePos
locatePos(int x, int y)
{
   FitsPosMapEntry pme = locate(x,y);
   if (pme == null) {
      return null;
   }
   return pme.telescopePos;
}

/**
 * Get the location of a position from its tag.
 */
public Point2D.Double
getLocationFromTag(String tag)
{
   Hashtable posTable = getPosTable();
   if (posTable == null) return null;

   // Get the base position
   FitsPosMapEntry pme = (FitsPosMapEntry) posTable.get(tag);
   if (pme == null) {
      return null;
   }
   return pme.screenPos;
}
 
/**
 * Initialize the mapping between TelescopePos objects and their
 * screen location.  Also become an observer of each position and
 * of the list as a whole in order to keep the mapping up-to-date.
 */
private boolean
_initPosTable()
{
   if (_tpl == null) {
      return false;
   }

   _posTable.clear();
   _tpl.addWatcher(this);  // We are a TelescopePosListWatcher
 
   TelescopePos[] tpA = _tpl.getAllPositions();
   for (int i=0; i<tpA.length; ++i) {
      TelescopePos tp = tpA[i];
      tp.addWatcher(this);

      Point2D.Double p = _iw.telescopePosToImageWidget(tp);
      _posTable.put( tp.getTag(), new FitsPosMapEntry(p, tp) );
   }

   return true;
}

/**
 * Recalculate the screen positions of everything, because the view has
 * changed.
 */
protected void
_updateScreenLocations()
{
   Enumeration e = _posTable.elements();
   while (e.hasMoreElements()) {
      FitsPosMapEntry pme = (FitsPosMapEntry) e.nextElement();
      TelescopePos     tp = pme.telescopePos;
      pme.screenPos       = _iw.telescopePosToImageWidget(tp);
   }
}

/**
 */
public void
posListReset(TelescopePosList tpl, TelescopePos[] newList)
{
   if (tpl != _tpl) return;
   _updateMap(newList);
   _iw.repaint();
}

/**
 */
public void
posListReordered(TelescopePosList tpl, TelescopePos[] newList, TelescopePos tp)
{
   if (tpl != _tpl) return;
   _updateMap(newList);
   _iw.repaint();
}

/**
 */
public void
posListAddedPosition(TelescopePosList tpl, TelescopePos tp)
{
   if (tpl != _tpl) return;
   Hashtable posTable = getPosTable();
   if (posTable == null) return;

   tp.addWatcher(this);

   FitsPosMapEntry pme;
   pme = new FitsPosMapEntry( _iw.telescopePosToImageWidget(tp), tp);
   posTable.put( tp.getTag(), pme ); // Replaces existing one if present

   _iw.repaint();
}

/**
 */
public void
posListRemovedPosition(TelescopePosList tpl, TelescopePos tp)
{
   if (tpl != _tpl) return;
   Hashtable posTable = getPosTable();
   if (posTable == null) return;

   posTable.remove(tp.getTag());
   tp.deleteWatcher(this);

   _iw.repaint();
}

 
/**
 * Re-sync the TelescopePosList and the posTable.
 */
//private boolean
private void
_updateMap(TelescopePos[] tpA)
{
   Hashtable posTable = getPosTable();
   if (posTable == null) return;

   //boolean needRepaint = false;
 
   // First remove anything from the table that doesn't exist anymore
   Enumeration keys = posTable.keys();
   while (keys.hasMoreElements()) {
      String tag = (String) keys.nextElement();
      if (!_tpl.exists(tag)) {
         FitsPosMapEntry pme = (FitsPosMapEntry) posTable.remove(tag);
         if (pme != null) { // And it really shouldn't be ...
            pme.telescopePos.deleteWatcher(this);
            //needRepaint = true;
         }
      }
   }
 
   // Now add anything from the list that isn't in the table, and make
   // sure that the PositionMaps that are there are still valid.
   for (int i=0; i<tpA.length; ++i) {
      TelescopePos tp = tpA[i];
      FitsPosMapEntry pme = (FitsPosMapEntry) posTable.get( tp.getTag() );

      if ((pme == null) || (pme.telescopePos != tp)) {
         //boolean validBefore = (pme != null) && (pme.screenPos != null);
         tp.addWatcher(this);

         pme = new FitsPosMapEntry( _iw.telescopePosToImageWidget(tp), tp);
         posTable.put( tp.getTag(), pme ); // Replaces existing one if present
         //boolean validAfter = (pme.screenPos != null);
 
         //needRepaint = needRepaint || (validBefore || validAfter);
      }
   }
 
   //return needRepaint;
}



/**
 * The location of a TelescopePos has changed.
 */
public void
telescopePosLocationUpdate(TelescopePos tp)
{
   Hashtable posTable = getPosTable();
   if (posTable == null) return;

   FitsPosMapEntry pme = (FitsPosMapEntry) posTable.get( tp.getTag() );
 
   // Really should be an error not to find tp.tag in the posTable ...
   if (pme != null) {
      // Was the position valid before the update?
      boolean wasValid = (pme.screenPos != null);
 
      pme.screenPos = _iw.telescopePosToImageWidget(tp);
 
      // Is the position valid now after the update?
      boolean isValid = (pme.screenPos != null);
 
      if (!wasValid && !isValid) {
         return;
      }
      _iw.repaint();
   }
}

/**
 * Something other than the location of a TelescopePos has changed.
 */
public void
telescopePosGenericUpdate(TelescopePos tp)
{
   Hashtable posTable = getPosTable();
   if (posTable == null) return;
   FitsPosMapEntry pme = (FitsPosMapEntry) posTable.get( tp.getTag() );
 
   // Just repaint to be safe
   if (pme != null) {
      _iw.repaint();
   }
}

/**
 * The ViewportViewObserver interface.  The view changed in the image widget,
 * so update the locations of everything.
 */
public void
viewportViewChange(ViewportImageWidget iw, ImageView iv)
{
   if (_valid) {
      _updateScreenLocations();
   } else {
      getPosTable();
   }
}
 
}

