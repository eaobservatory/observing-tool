// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

/**
 * An interface supported by clients of the SpEditState that wish to be
 * notified when the edit state of the program changes in some way (an
 * attribute is changed, or an item is added or removed for instance).
 *
 * @see SpEditState
 */
public interface SpEditChangeObserver
{
   /**
    * Notification that the program has been edited.
    *
    * @param root The root of the Science Program tree that has been
    * modified in some way.
    */
   public void spEditStateChange(SpItem root);
}

