// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * The SpEditState class implements a simple state machine that tracks
 * the edit state of a program or plan.  Every SpItem in the program or
 * plan has a referance to a single SpEditState.
 * <p>
 * Clients interested in when the program has been edited in any way
 * can support the SpEditChangeObserver interface and add themselves
 * as an observer.  If only interested in when the structure or hierarchy
 * of the program is modified, the client may implement the
 * SpHierarchyChangeObserver.
 */
public final class SpEditState implements Observer, java.io.Serializable
{
   /**
    * The item is in this state if its attributes have not been edited
    * since the last time they were saved.
    */
   public static final int UNEDITED = 0;
 
   /**
    * The item is in this state if its attributes have been edited
    * since the last time they were saved.
    */
   public static final int EDITED = 1;
 
   //
   // This is the program or plan item at the root of the hierarchy.
   //
   private SpItem _spItem;

   //
   // The edit state of the item's attribute/value table.  One of UNEDITED,
   // or EDITED.
   //
   private int _state;

   //
   // Observers of structure changes.
   //
   private Vector _hierObservers = new Vector();

   //
   // Observers of edit state changes.
   //
   private Vector _editObservers = new Vector();

//
// Construct with an SpItem.  This constructor is not public and so cannot
// be used by clients.  The intention is that the program will create its
// SpEditState class for itself.
//
SpEditState(SpItem spItem)
{
   _spItem = spItem;
   _spItem.addObserver(this);
   _state  = UNEDITED;
}

/**
 * Get the associated SpItem at the root of the program hierarchy.
 */
public SpItem
getItem()
{
   return _spItem;
}

/**
 * Get the current edit state of the item's AV table.
 */
public int
getState()
{
   return _state;
}

/**
 * Add an observer of structure changes.  SpHiearcharyChangeObservers are
 * notified when a change to the program structure changes.  Attempts to
 * add an observer that is already registered have no effect.
 */
public synchronized final void
addHierarchyChangeObserver(SpHierarchyChangeObserver hco)
{
   if (_hierObservers.contains(hco)) {
      return;
   }
 
   _hierObservers.addElement(hco);
}
 
/**
 * Delete a hierarchy change observer.
 */
public synchronized final void
deleteHierarchyChangeObserver(SpHierarchyChangeObserver hco)
{
   _hierObservers.removeElement(hco);
}

/**
 * Delete all hierarchy change observers.
 */
public synchronized final void
deleteHierarchyChangeObservers()
{
   _hierObservers.removeAllElements();
}

//
// Notify hierarchy change observer that items have been added.
//
void
notifyAdded(SpItem parent, SpItem[] newChildren, SpItem afterChild)
{
   Vector v;
   synchronized (this) {
      v = (Vector) _hierObservers.clone();
   }
 
   for (int i=0; i<v.size(); ++i) {
      SpHierarchyChangeObserver hco;
      hco = (SpHierarchyChangeObserver) v.elementAt(i);
      hco.spItemsAdded(parent, newChildren, afterChild);
   }
   setEdited();
}

//
// Notify hierarchy change observer that items have been removed.
//
void
notifyRemoved(SpItem parent, SpItem[] children)
{
   Vector v;
   synchronized (this) {
      v = (Vector) _hierObservers.clone();
   }
 
   for (int i=0; i<v.size(); ++i) {
      SpHierarchyChangeObserver hco;
      hco = (SpHierarchyChangeObserver) v.elementAt(i);
      hco.spItemsRemoved(parent, children);
   }
   setEdited();
}

// Not yet used.
void
notifyMoved(SpItem oldParent, SpItem[] children,
            SpItem newParent, SpItem afterChild)
{
   Vector v;
   synchronized (this) {
      v = (Vector) _hierObservers.clone();
   }
 
   for (int i=0; i<v.size(); ++i) {
      SpHierarchyChangeObserver hco;
      hco = (SpHierarchyChangeObserver) v.elementAt(i);
      hco.spItemsMoved(oldParent, children, newParent, afterChild);
   }
   setEdited();
}

/**
 * Add an observer of edit state changes.  SpEditChangeObservers are
 * notified when a change to the program edit state changes.  Attempts to
 * add an observer that is already registered have no effect.
 */
public synchronized final void
addEditChangeObserver(SpEditChangeObserver eco)
{
   if (_editObservers.contains(eco)) {
      return;
   }
 
   _editObservers.addElement(eco);
}
 
/**
 * Delete an edit state observer.
 */
public synchronized final void
deleteEditChangeObserver(SpEditChangeObserver eco)
{
   _editObservers.removeElement(eco);
}

/**
 * Delete all edit state observers.
 */
public synchronized final void
deleteEditChangeObservers()
{
   _editObservers.removeAllElements();
}

//
// Notify edit change observers of an update.
//
private void
_notifyEditChange()
{
   Vector v;
   synchronized (this) {
      v = (Vector) _editObservers.clone();
   }
 
   for (int i=0; i<v.size(); ++i) {
      SpEditChangeObserver eco;
      eco = (SpEditChangeObserver) v.elementAt(i);
      eco.spEditStateChange(_spItem);
   }
}

//
// Change the state, notifying any observers.
//
private void
_changeState(int newState)
{
   _state = newState;
   _notifyEditChange();
}

/**
 * Change the state to edited, notifying edit change observers.
 */
public void
setEdited()
{
   switch (_state) {
      case EDITED:
         // Do not notify observers of each edit
         //_changeState(EDITED);
         break;

      case UNEDITED:
         _changeState(EDITED);
         break;
   }
}

/**
 * This method is called when any item in the program is edited, other
 * than for a structure change.
 */
public void
update(Observable obs, Object arg)
{
   if (obs instanceof SpAvEditState) {
      SpAvEditState avEditFSM = (SpAvEditState) obs;
      // Only interrested when the av edit state changes to edited
      if (avEditFSM.getState() != SpAvEditState.EDITED) {
         return;
      }
   }

   setEdited();
}

/**
 * This method is called by clients wishing to forget any edits that
 * have occurred (say after a save of the program).
 */
public void
reset()
{
   switch (_state) {
      case UNEDITED:
         // Do nothing
         break;

      case EDITED:
         _changeState(UNEDITED);
         break;
   }

   _resetAvEditState(_spItem);
}

//
// Reset the attribute/value edit state for each item in the program.
//
private void
_resetAvEditState(SpItem spItem)
{
   spItem.getAvEditFSM().save();
   Enumeration children = spItem.children();
   while (children.hasMoreElements()) {
      _resetAvEditState( (SpItem) children.nextElement() );
   }
}


/**
 * The string representation of the current state.
 */
public String
toString()
{
   String ret = getClass().getName() + "[state=";
   switch (_state) {
      case UNEDITED:	ret += "UNEDITED"; break;
      case EDITED:	ret += "EDITED"; break;
      default:		ret +="UNKOWN!";
   }
   return ret + "]";
}

}
