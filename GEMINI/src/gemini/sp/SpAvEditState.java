// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import java.util.*;

/**
 * The SpAvEditState class implements a simple state machine that tracks
 * the edit state of an item's SpAvTable and saves backup versions in
 * case edits are undone.  Every SpItem has one and only one SpAvEditState
 * and vice-versa.
 */
public final class SpAvEditState extends Observable implements java.io.Serializable
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
 
   /**
    * The item is in this state if its attributes have been edited,
    * but the changes have been "undone".
    */
   public static final int EDIT_UNDONE = 2;

   //
   // This is the item whose av table this class manages.  SpItems and
   // SpAvEditStates are one-to-one.
   //
   private SpItem _spItem;

   //
   // The edit state of the item's attribute/value table.  One of UNEDITED,
   // EDITED, or EDIT_UNDONE.
   //
   private int _state;

   //
   // This is a copy of the AvTable in its "previous" state.  It only
   // exists when the table has been edited but not saved.
   //
   private SpAvTable _prevTable;

   //
   // This variable controls whether each individual edit causes the
   // observers to be notified.
   //
   // @see #setEachEditNotifies
   //
   private boolean _eachEditNotifies = true;

//
// Construct with an SpItem.  This constructor is not public and so cannot
// be used by clients.  The intention is that the SpItem will create its
// SpAvEditState class for itself.
//
SpAvEditState(SpItem spItem)
{
   _spItem = spItem;
   _state  = UNEDITED;
}

/**
 * Get the associated SpItem.
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
 * Set the edit notification state.  If set to true (default), then each
 * edit causes all observers to be notified.  If false, only the first
 * edit notifies the observer.
 */
public void
setEachEditNotifies(boolean state)
{
   _eachEditNotifies = state;
}

//
// Change the state, notifying any observers.
//
private void
_changeState(int newState)
{
   _state = newState;
   setChanged(); notifyObservers();
}

//
// This method is called by the SpAvTable before it changes any attributes.
// If the table has previously been edited since the last save or undo,
// nothing happens.  Otherwise a copy of the current attributes and values
// is made in case the edits are later undone.
//
void
editPending()
{
   switch (_state) {
      case EDITED:
         break;

      case UNEDITED:
      case EDIT_UNDONE:
         _prevTable = _spItem.getTable().copy();
         break;
   }
}

//
// This method is called by the SpAvTable after it changes any attribute.
// Since an attribute has been changed, the state is changed to EDITED
// unless the state is already EDITED.  In that case, the state is "changed"
// to edited (thus notifying observers) only if _eachEditNotifies is true.
//
void
editComplete()
{
   switch (_state) {
      case EDITED:
         // Notify observers of each edit
         if (_eachEditNotifies) {
            _changeState(EDITED);
         }
         break;

      case UNEDITED:
      case EDIT_UNDONE:
         _changeState(EDITED);
         break;
   }
}

/**
 * This method is called by clients wishing to undo their edits.  All the
 * changes since the last undo or save are undone.  "Undo" can be undone
 * by a second call to <code>undo</code>.
 */
public void
undo()
{
   SpAvTable temp;

   switch (_state) {
      case UNEDITED:
         // Do nothing
         break;

      case EDITED:
         temp = _spItem.getTable();
         _spItem.setTable(_prevTable);
         _prevTable   = temp;  // Save this in case of subsequent redo

         _changeState(EDIT_UNDONE);
         break;

      case EDIT_UNDONE:
         // Undo an undo ...  Make the edits valid again
         temp = _spItem.getTable();
         _spItem.setTable(_prevTable);
         _prevTable = temp;

         _changeState(EDITED);
         break;
   }
}


/**
 * This method is called by clients wishing to save their edits.
 * After a save, changes cannot be undone.
 */
public void
save()
{
   switch (_state) {
      case UNEDITED:
         // Do nothing
         break;

      case EDITED:
      case EDIT_UNDONE:
         _prevTable = null;
         _changeState(UNEDITED);
         break;
   }
}

}

