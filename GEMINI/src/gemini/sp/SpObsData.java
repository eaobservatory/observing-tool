// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import java.util.Vector;

import gemini.util.CoordSys;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp;

/**
 * A collection of (dervied) fundamental observation data in which
 * clients are keenly interrested.  It must be kept current.
 *
 * <p>
 * Each observation context (see <code>SpObsContextItem</code>)
 * maintains its own ObsData.  ObsData consists of the telescope
 * base position and position angle.  Clients that need to always
 * know the current base position or position angle register their
 * interest with this class.
 *
 * <p>
 * If an observation context does not have its own
 * <code>SpTelescopeObsComp</code> (for the base position) or
 * <code>SpInstObsComp</code> (for the position angle), then
 * its SpObsData simply watches the parent SpObsContextItem for changes
 * to its observation data.  If there is no parent SpObsContextItem with
 * a <code>SpTelescopeObsComp</code> or <code>SpInstObsComp</code>, then
 * the default is RA 0.0/Dec 0.0 or position angle 0.0.
 *
 * @see gemini.sp.obsComp.SpInstObsComp
 * @see gemini.sp.obsComp.SpTelescopeObsComp
 * @see gemini.sp.SpObsContextItem
 */
public class SpObsData implements java.io.Serializable
{
   private double _x       = 0.0;
   private double _y      = 0.0;
   private double _posAngle = 0.0;
   private int    _coordSys = CoordSys.FK5;

   private Vector _basePosObservers;
   private Vector _posAngleObservers;

/**
 * Add a base position observer.
 */
public void
addBasePosObserver(SpBasePosObserver bpo)
{
   if (_basePosObservers == null) {
      _basePosObservers = new Vector();
      _basePosObservers.addElement(bpo);
   } else if (!_basePosObservers.contains(bpo)) {
      _basePosObservers.addElement(bpo);
   }
}

/**
 * Remove a base position observer.
 */
public void
deleteBasePosObserver(SpBasePosObserver bpo)
{
   if (_basePosObservers == null) {
      return;
   }

   _basePosObservers.removeElement(bpo);  // If not in vector, who cares?
}

/**
 * Set the base position, notifying observers of the change.
 */
public void
setBasePos(double x, double y)
{
   setBasePos(x, y, CoordSys.FK5);
}

// Parameter coordSystem added (MFO, April 11, 2002).
/**
 * Set the base position, notifying observers of the change.
 */
public void
setBasePos(double x, double y, int coordSys)
{
   _x = x;
   _y = y;
   _coordSys = coordSys;

   if (_basePosObservers == null) {
      return;
   }

   for (int i=0; i<_basePosObservers.size(); ++i) {
      SpBasePosObserver bpo;
      bpo = (SpBasePosObserver) _basePosObservers.elementAt(i);
      bpo.basePosUpdate(x, y, coordSys);
   }
}


/**
 * Get the RA of the base position.
 */
public double
getXaxis()
{
   return _x;
}


/**
 * Get the Dec of the base position.
 */
public double
getYaxis()
{
   return _y;
}


/**
 * Get the Coordinate system of the base position.
 */
public int
getCoordSys()
{
   return _coordSys;
}


/**
 * Add a position angle observer.
 */
public void
addPosAngleObserver(SpPosAngleObserver pao)
{
   if (_posAngleObservers == null) {
      _posAngleObservers = new Vector();
      _posAngleObservers.addElement(pao);
   } else if (!_posAngleObservers.contains(pao)) {
      _posAngleObservers.addElement(pao);
   }
}

/**
 * Remove a position angle observer.
 */
public void
deletePosAngleObserver(SpPosAngleObserver pao)
{
   if (_posAngleObservers == null) {
      return;
   }

   _posAngleObservers.removeElement(pao);  // If not in vector, who cares?
}


/**
 * Set the position angle, notifying observers.
 */
public void
setPosAngle(double posAngle)
{
   _posAngle = posAngle;

   if (_posAngleObservers == null) {
      return;
   }

   for (int i=0; i<_posAngleObservers.size(); ++i) {
      SpPosAngleObserver pao;
      pao = (SpPosAngleObserver) _posAngleObservers.elementAt(i);
      pao.posAngleUpdate(posAngle);
   }
}

/**
 * Get the position angle.
 */
public double
getPosAngle()
{
   return _posAngle;
}


//
// Fix up the appropriate SpBasePosObservers and initialize the
// parent's ObsData after an insertion of a SpTelescopeObsComp.
//
static void
completeSpTelescopeObsCompInsertion(SpTelescopeObsComp newItem)
{
   // The parent of this item should no longer be a base pos observer
   // of its parent
   SpObsContextItem parent = (SpObsContextItem) newItem.parent();
   SpObsContextItem grandp = (SpObsContextItem) parent.parent();
   if (grandp != null) {
      grandp.getObsData().deleteBasePosObserver(parent);
   }

   // Update the parent's base pos
   SpTelescopePosList tpl  = newItem.getPosList();
   SpTelescopePos     tp   = tpl.getBasePosition();
   parent.getObsData().setBasePos(tp.getXaxis(), tp.getYaxis(), tp.getCoordSys());
}

//
// Fix up the appropriate SpPosAngleObservers and initialize the
// parent's ObsData after an insertion of a SpInstObsComp.
//
static void
completeSpInstObsCompInsertion(SpInstObsComp newItem)
{
   // The parent of this item should no longer be a pos angle observer
   // of its parent
   SpObsContextItem parent = (SpObsContextItem) newItem.parent();
   SpObsContextItem grandp = (SpObsContextItem) parent.parent();
   if (grandp != null) {
      grandp.getObsData().deletePosAngleObserver(parent);
   }

   // Update the parent's pos angle
   double angle = newItem.getPosAngleDegrees();
   parent.getObsData().setPosAngle(angle);
}

//
// Complete the insertion of an SpObsContextItem, fixing up the ObsData
// references.
//
static void
completeSpObsContextItemInsertion(SpObsContextItem newOCI)
{
   // If this component doesn't have a target list, observe parent's
   // base pos and update our base pos to match
   if (SpTreeMan.findTargetListInContext(newOCI) == null) {
      SpObsContextItem parent = (SpObsContextItem) newOCI.parent();
      SpObsData od = parent.getObsData();
      od.addBasePosObserver(newOCI);

      newOCI.getObsData().setBasePos(od.getXaxis(), od.getYaxis(), od.getCoordSys());
   }

   // If this component doesn't have an instrument, observe parent's
   // pos angle and update our pos angle to match
   if (SpTreeMan.findInstrumentInContext(newOCI) == null) {
      SpObsContextItem parent = (SpObsContextItem) newOCI.parent();
      SpObsData od = parent.getObsData();
      od.addPosAngleObserver(newOCI);

      newOCI.getObsData().setPosAngle(od.getPosAngle());
   }
}

//
// Prepare to extract an SpTelescopeObsComp.  This involves fixing up the
// appropriate observers and resetting the base position in the parent
// context.
//
static void
prepareSpTelescopeObsCompExtract(SpItem spItem)
{
   //System.out.println("Extracting an SpTelescopeObsComp");

   // Make the parent obs context start observing it's parent's context
   // (if it has a parent).
   SpObsContextItem parent = (SpObsContextItem) spItem.parent();
   SpObsContextItem grandp = (SpObsContextItem) parent.parent();
   if (grandp == null) {
      parent.getObsData().setBasePos(0.0, 0.0);
   } else {
      SpObsData od = grandp.getObsData();
      od.addBasePosObserver(parent);
      parent.getObsData().setBasePos(od.getXaxis(), od.getYaxis(), od.getCoordSys());
   }
}

//
// Prepare to extract an SpInstObsComp.  This involves fixing up the
// appropriate observers and resetting the pos angle in the parent
// context.
//
static void
prepareSpInstObsCompExtract(SpItem spItem)
{
   //System.out.println("*** Extracting an SpInstObsComp");

   // Make the parent obs context start observing it's parent's context
   // (if it has a parent).
   SpObsContextItem parent = (SpObsContextItem) spItem.parent();
   SpObsContextItem grandp = (SpObsContextItem) parent.parent();
   if (grandp == null) {
      parent.getObsData().setPosAngle(0.0);
   } else {
      SpObsData od = grandp.getObsData();
      od.addPosAngleObserver(parent);
      parent.getObsData().setPosAngle(od.getPosAngle());
   }
}

//
// Prepare to extract an SpObsContextItem.  This involves fixing up the
// appropriate observers.
//
static void
prepareSpObsContextItemExtract(SpItem spItem)
{
   //System.out.println("Extracting an SpObsContextItem ");

   SpObsContextItem ctxItem = (SpObsContextItem) spItem;

   // If this component doesn't have a target list, then it must have been
   // observing its parent's ObsData.  Now it should quit.
   if (SpTreeMan.findTargetListInContext(spItem) == null) {
      SpObsContextItem parent  = (SpObsContextItem) spItem.parent();
      if (parent != null) {
         parent.getObsData().deleteBasePosObserver(ctxItem);
      }
      ctxItem.getObsData().setBasePos(0.0, 0.0);
   }

   // If this component doesn't have an instrument, then it must have been
   // observing its parent's ObsData.  Now it should quit.
   if (SpTreeMan.findInstrumentInContext(spItem) == null) {
      SpObsContextItem parent  = (SpObsContextItem) spItem.parent();
      if (parent != null) {
         parent.getObsData().deletePosAngleObserver(ctxItem);
      }
      ctxItem.getObsData().setPosAngle(0.0);
   }
}

}

