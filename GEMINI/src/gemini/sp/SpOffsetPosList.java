// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;
 
import java.awt.Point;

import java.util.Enumeration;
import java.util.Vector;

import gemini.util.RADecMath;
import gemini.util.TelescopePos;
import gemini.util.TelescopePosList;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.iter.SpIterSky;

/**
 * A data object that describes a list of telescope offset positions.
 */
public final class SpOffsetPosList extends TelescopePosList 
  implements java.io.Serializable
{
   // The name of the attribute that holds the target list
   public static final String OFFSET_POS_LIST = "offsetPositions";

   // The name of the attribute that golds the sky list
   public static final String SKY_POS_LIST = "skyPositions";

   /** The name of the attribute that holds the position angle. */
   public static String ATTR_POS_ANGLE = "PA";


   private SpAvTable _avTab;	// The table that holds the positions
   private Vector    _posList;	// A vector of SpOffsetPos objects
   private Vector    _skyOffsets = new Vector();

/**
 * Create with a an attribute/value table.
 */
public SpOffsetPosList(SpAvTable avTab)
{
   setTable(avTab);
}

/**
 * Set the attribute/value table containing the encoded offset postions.
 */
public void
setTable(SpAvTable avTab)
{
   synchronized (this) {
      _avTab   = avTab;

      if (_posList != null) {
         for (int i=0; i<_posList.size(); ++i) {
            SpOffsetPos op = (SpOffsetPos) _posList.elementAt(i);
            op.deleteWatchers();
         }
      }

      _posList = getAllPositions(avTab, this);
   }
   _notifyOfReset();
}


/**
 * Extract the position with the given index from the av table.
 */
public static SpOffsetPos
extractPosition(SpAvTable avTab, String tag, SpOffsetPosList list)
{
   if (!avTab.exists(tag)) return null;
   return new SpOffsetPos(avTab, tag, list);
}

/**
 * Return all the positions from the table.
 */
public static Vector
getAllPositions(SpAvTable avTab, SpOffsetPosList list)
{
   Vector v = new Vector();

   Vector opv = avTab.getAll(OFFSET_POS_LIST);
   if (opv == null) return v;

   SpOffsetPos op;

   Enumeration e = opv.elements();
   while (e.hasMoreElements()) {
      String tag = (String) e.nextElement();
      op = SpOffsetPosList.extractPosition(avTab, tag, list);
      if (op != null) {
         v.addElement(op);
      }
   }

   return v;
}

/**
  * Return the current sky offsets
  */
public Vector getSkyOffsets() {
    return _skyOffsets;
}

/**
 * Get the position angle of offset position list.
 *
 * The angle by which the offset poitions are rotated around the base position.
 */
public double
getPosAngle()
{
   return _avTab.getDouble(ATTR_POS_ANGLE, 0.0);
}

/**
 * Set the position angle of offset position list.
 *
 * The angle by which the offset poitions are rotated around the base position.
 */
public void
setPosAngle(double posAngle)
{
   _avTab.set(ATTR_POS_ANGLE, posAngle);

   // Trigger telescope position watcher notification.
   TelescopePos tp;
   for(int i = 0; i < size(); i++) {
     tp = getPositionAt(i);
     tp.setXY(tp.getXaxis(), tp.getYaxis());
   }
}


//
// Get a unique tag for the offset position.
//
private synchronized String
_getUniqueTag()
{
   int i;
   int size = _avTab.size(OFFSET_POS_LIST);
   for (i=0; i<size; ++i) {
      String tag = _avTab.get(SpOffsetPos.OFFSET_TAG + i);
      if (tag == null) {
         break;
      }
   }

   return SpOffsetPos.OFFSET_TAG + i;
}


//
// Get a unique sky tag for the offset position.
//
private synchronized String
_getUniqueSkyTag()
{
   int i;
   int size = _avTab.size(SKY_POS_LIST);
   for (i=0; i<size; ++i) {
      String tag = _avTab.get(SpOffsetPos.SKY_TAG + i);
      if (tag == null) {
         break;
      }
   }

   return SpOffsetPos.SKY_TAG + i;
}


//
// Implementation of the TelescopePosList interface.

/**
 * Returns the number of positions that are in the list.
 *
 * @see TelescopePosList
 */
public synchronized int
size()
{
   return _posList.size();
}

/**
 * Retrieve the position with the given index from the position list.
 *
 * @see TelescopePosList
 */
public synchronized TelescopePos
getPositionAt(int index)
{
   if ((index < 0) || (_posList.size() <= index)) {
      return null;
   }
   return (TelescopePos) _posList.elementAt(index);
}

/**
 * Does the named position exist?
 *
 * @see TelescopePosList
 */
public synchronized boolean
exists(String tag)
{
   return _avTab.exists(tag);
}

/**
 * Get the position's location in the list, or -1 if the position isn't
 * in the list.
 *
 * @see TelescopePosList
 */
public synchronized int
getPositionIndex(String tag)
{
   for (int i=0; i<_posList.size(); ++i) {
      SpOffsetPos op = (SpOffsetPos) _posList.elementAt(i);
      if (op.getTag().equals(tag)) {
         return i;
      }
   }
   return -1;
}

// End of TelescopePosList interface implementation.
//


//
// Add the given tag to the list of user positions.
//
private synchronized void
_addOffsetPosition(String tag)
{
   // Add the tag to the list of user tags
   int size = _avTab.size(OFFSET_POS_LIST);
   if (size == 0) {
      _avTab.set(OFFSET_POS_LIST, tag);
   } else {
      _avTab.set(OFFSET_POS_LIST, tag, size);
   }
}

//
// Add the given sky tag to the list of user positions.
//
private synchronized void
_addSkyOffsetPosition(String tag)
{
   // Add the tag to the list of user tags
   int size = _avTab.size(SKY_POS_LIST);
   if (size == 0) {
      _avTab.set(SKY_POS_LIST, tag);
   } else {
      _avTab.set(SKY_POS_LIST, tag, size);
   }
}

//
// Insert the given tag in the list of user positions at the given index.
//
private synchronized void
_insertOffsetPosition(String tag, int index)
{
   Vector v = _avTab.getAll(OFFSET_POS_LIST);
   if (v == null) {
      _addOffsetPosition(tag);
      return;
   }

   if (index >= v.size()) {
      _addOffsetPosition(tag);
      return;
   }

   v.insertElementAt(tag, index);
   _avTab.setAll(OFFSET_POS_LIST, v);
}

/**
 * Create an offset position at the end of the list and add it to the
 * A/V table.  A unique tag will be generated and assigned to the created
 * position.  Returns the new SpOffsetPos object.
 */
public SpOffsetPos
createPosition()
{
   return createPosition(-1, 0.0, 0.0);
}

/**
 * Create an offset position at the end of the list and add it to the
 * A/V table.  A unique tag will be generated and assigned to the created
 * position.  Returns the new SpOffsetPos object.
 */
public SpOffsetPos
createPosition(double xOff, double yOff)
{
   return createPosition(-1, xOff, yOff);
}

/**
 * Create an offset position at the given index and add it to the A/V table.
 * A unique tag will be generated and assigned to the created position.
 * Returns the new SpOffsetPos object.
 */
public SpOffsetPos
createPosition(int index)
{
   return createPosition(index, 0.0, 0.0);
}

/**
 * Create an offset position at the given index and add it to the A/V table.
 * A unique tag will be generated and assigned to the created position.
 * Returns the new SpOffsetPos object.
 */
public SpOffsetPos
createPosition(int index, double xOff, double yOff)
{
   SpOffsetPos op;
   synchronized (this) {
      String tag = _getUniqueTag();
      if (index == -1) {
         _addOffsetPosition(tag);
      } else {
         _insertOffsetPosition(tag, index);
      }

      // Create the position
      op = new SpOffsetPos(_avTab, tag, this);
      op.noNotifySetXY(xOff, yOff);

      if ((index == -1) || (index >= _posList.size())) {
         _posList.addElement(op);
      } else {
         _posList.insertElementAt(op, index);
      }
   }
   _notifyOfAdd(op);
   return op;
}


/**
 * Remove all positions.
 */
public void
removeAllPositions()
{
   synchronized (this) {
      for (int i=0; i<_posList.size(); ++i) {
         SpOffsetPos op = (SpOffsetPos) _posList.elementAt(i);
         _avTab.rm(op.getTag());
         op.deleteWatchers();
      }
      _avTab.setAll(OFFSET_POS_LIST, new Vector());
      _posList.removeAllElements();
   }
   _notifyOfReset();
}

//
// Remove the position which is at the given index.
//
private synchronized boolean
_removePosition(TelescopePos tp, int index)
{
   SpOffsetPos op = (SpOffsetPos) tp;

   _posList.removeElementAt(index);
   _avTab.rm(op.getTag());
   _avTab.rm(OFFSET_POS_LIST, index);
   op.deleteWatchers();
   return true;
}

/**
 * Remove the given position from the given A/V table.  This is a
 * public removePosition method.  It calls the private version to do
 * the work and then notifies observers.
 */
public void
removePosition(TelescopePos tp)
{
   boolean result = false;

   synchronized (this) {
      // See where the offset position current is
      int index = getPositionIndex(tp);
      if (index == -1) return;

      result = _removePosition(tp, index);
   }
   if (result) {
      _notifyOfRemove(tp);
   }
}


/**
 * Remove the given position from the given A/V table.  This is a
 * public removePosition method.  It looks up the position with the
 * given tag and then calls the other removePosition method to actually
 * remove the position.
 *
 * @see #removePosition(SpOffsetPos)
 */
public void
removePositionAt(int index)
{
   TelescopePos tp;
   boolean      result;
   synchronized (this) {
      // Find the offset position at this index, and then remove it.
      tp = getPositionAt(index);
      if (tp == null) return;

      result = _removePosition(tp, index);
   }

   if (result) {
      _notifyOfRemove(tp);
   }
}


//
// Decrement the position's location in the list.  This is the private
// method that does the work.  A public interface is provided that
// notifies observers.
//
private synchronized int
_decrementPosition(SpOffsetPos op)
{
   // See where the offset position current is
   Vector v = _avTab.getAll(OFFSET_POS_LIST);
   int i = v.indexOf(op.getTag());
   if (i == -1) {
      return -1;
   }

   // Can't decrement past the beginning of the list
   if (i == 0) {
      return i;
   }

   v.removeElementAt(i);
   v.insertElementAt(op.getTag(), i-1);

   _avTab.setAll(OFFSET_POS_LIST, v);

   _posList.removeElementAt(i);
   _posList.insertElementAt(op, i-1);

   return i-1;
}

/**
 * Decrement the position's location in the list.  The new position is
 * returned, or -1 if the position isn't in the list.
 */
public int
decrementPosition(SpOffsetPos op)
{
   int i = _decrementPosition(op);
   if (i != -1) {
      _notifyOfReorder(op);
   }
   return i;
}

//
// Increment the position's location in the list.  This is the private
// method that does the work.  A public interface is provided that
// notifies observers.
//
private synchronized int
_incrementPosition(SpOffsetPos op)
{
   // See where the offset position current is
   Vector v = _avTab.getAll(OFFSET_POS_LIST);
   int i = v.indexOf(op.getTag());
   if (i == -1) {
      return -1;
   }

   // Can't increment past the end of the list
   if (i+1 == v.size()) {
      return i;
   }

   v.removeElementAt(i);
   v.insertElementAt(op.getTag(), i+1);

   _avTab.setAll(OFFSET_POS_LIST, v);

   _posList.removeElementAt(i);
   _posList.insertElementAt(op, i+1);

   return i+1;
}

/**
 * Increment the position's location in the list.  The new position is
 * returned, or -1 if the position isn't in the list.
 */
public int
incrementPosition(SpOffsetPos op)
{
   int i = _incrementPosition(op);
   if (i != -1) {
      _notifyOfReorder(op);
   }
   return i;
}

//
// Move the position to the back of the list.  This is the private
// method that does the work.  A public interface is provided that
// notifies observers.
//
private synchronized int
_positionToBack(SpOffsetPos op)
{
   // See where the offset position current is
   Vector v = _avTab.getAll(OFFSET_POS_LIST);
   int i = v.indexOf(op.getTag());
   if (i == -1) {
      return -1;
   }

   // Already at the back of the list
   if (i+1 == v.size()) {
      return i;
   }

   v.removeElementAt(i);
   v.addElement(op.getTag());

   _avTab.setAll(OFFSET_POS_LIST, v);

   _posList.removeElementAt(i);
   _posList.addElement(op);

   return (v.size() - 1);
}

/**
 * Increment the position's location in the list.  The new position is
 * returned, or -1 if the position isn't in the list.
 */
public int
positionToBack(SpOffsetPos op)
{
   int i = _positionToBack(op);
   if (i != -1) {
      _notifyOfReorder(op);
   }
   return i;
}

//
// Move the position to the front of the list.  This is the private
// method that does the work.  A public interface is provided that
// notifies observers.
//
private synchronized int
_positionToFront(SpOffsetPos op)
{
   // See where the offset position current is
   Vector v = _avTab.getAll(OFFSET_POS_LIST);
   int i = v.indexOf(op.getTag());
   if (i == -1) {
      return -1;
   }

   // Already at the front of the list
   if (i == 0) {
      return 0;
   }

   v.removeElementAt(i);
   v.insertElementAt(op.getTag(), 0);

   _avTab.setAll(OFFSET_POS_LIST, v);

   _posList.removeElementAt(i);
   _posList.insertElementAt(op, 0);

   return 0;
}

/**
 * Increment the position's location in the list.  The new position is
 * returned, or -1 if the position isn't in the list.
 */
public int
positionToFront(SpOffsetPos op)
{
   int i = _positionToFront(op);
   if (i != -1) {
      _notifyOfReorder(op);
   }
   return i;
}

/**
  * Reset sky offset positions.  Just clears the current entries.
  */
public void resetSkyPositions() {
    _skyOffsets.clear();
}

/**
  * Create sky offset. Sky offsets are created in their own space and
  * should not interact with the real position list (_posList).  They
  * are assumed to be at the same PA as the current posList.
  */
public SpOffsetPos createSkyPosition( double xoff, double yoff) {
    SpOffsetPos op;
    synchronized(this) {
        String tag = _getUniqueSkyTag();
        _addSkyOffsetPosition (tag);

        op = new SpOffsetPos( _avTab, tag, this);
        op.noNotifySetXY(xoff, yoff);
        _skyOffsets.addElement(op);
    }
    return op;
}

/**
 * The standard debugging method.
 */
public synchronized String
toString()
{
   String out = getClass().getName() + "[";

   if (_posList.size() >= 1) {
      out += ( (SpOffsetPos) _posList.elementAt(0) ).toString();
   }
   for (int i=1; i<_posList.size(); ++i) {
      out += "," + ( (SpOffsetPos) _posList.elementAt(i) ).toString();
   }

   return out + "]";
}

}
