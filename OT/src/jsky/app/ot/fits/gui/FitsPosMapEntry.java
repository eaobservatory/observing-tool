// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.fits.gui;

import gemini.util.TelescopePos;
import java.awt.geom.Point2D;


/**
 * An implementation class that groups an (x,y) pair and an Object.
 */
public final class FitsPosMapEntry
{
   public Point2D.Double screenPos;
   public TelescopePos telescopePos;
 
   public FitsPosMapEntry(Point2D.Double p, TelescopePos tp) {
      screenPos    = p;
      telescopePos = tp;
   }
 
   public String toString() {
      return getClass().getName() + "[" + screenPos + "," + telescopePos + "]";
   }
}

