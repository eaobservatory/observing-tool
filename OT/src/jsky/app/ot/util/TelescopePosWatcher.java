// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//

package jsky.app.ot.util;

/**
 * An interface supported by clients of TelescopePos who want to
 * be notified when the positions changes in some way.
 */
public interface TelescopePosWatcher
{
   /**
    * The location of the position has changed.
    */
   public void telescopePosLocationUpdate(TelescopePos tp);

   /**
    * Some other sort of change has been made, for instance its tag
    * may have been updated.
    */
   public void telescopePosGenericUpdate(TelescopePos tp);
}

