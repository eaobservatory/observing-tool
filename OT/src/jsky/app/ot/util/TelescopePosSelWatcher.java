// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//

package jsky.app.ot.util;

/**
 * An interface supported by clients of TelescopePosList who want to
 * be notified when a TelescopePos is selected.
 */
public interface TelescopePosSelWatcher
{
   /** A position has been selected.  */
   public void telescopePosSelected(TelescopePosList tpl, TelescopePos tp);
}
