// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.ipc;

import gemini.sp.SpItem;
import java.util.Vector;

/**
 * An interface supported by clients of SpServerAsync.
 */
public interface SpServerWatcher
{
   /**
    * Notify that the request was aborted.
    */
   public void spServerAbort(SpServerAsync ssa);

   /**
    * Notify that the request ended with an error.
    */
   public void spServerError(String problem, SpServerAsync ssa);

   /**
    * Notify that the 'list' request is complete.
    */
   public void spServerListComplete(Vector progList, SpServerAsync ssa);

   /**
    * Notify that the 'fetch' request is complete.
    */
   public void spServerFetchComplete(SpItem spItem, SpServerAsync ssa);

   /**
    * Notify that the 'store' request is complete.
    */
   public void spServerStoreComplete(SpItem spItem, SpProgKey key,
                                     boolean isNew, SpServerAsync ssa);
}
