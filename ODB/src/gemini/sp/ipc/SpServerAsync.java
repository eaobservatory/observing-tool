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
 * The private worker for the "list" method.
 */
final class SpServerAsyncList implements Runnable
{
   private SpAccess      _spAccess;
   private String        _user;
   private SpServerAsync _master;

SpServerAsyncList(SpAccess spAccess, String user, SpServerAsync master)
{
   _spAccess = spAccess;
   _user     = user;
   _master   = master;
}

public void
run()
{
   SpServer spServer = new SpServer();

   Vector progVector = new Vector();
   boolean  result   = spServer.list(_spAccess, _user, progVector);

   if (!result) {
      _master._errorComplete(spServer.getProblemDescr());
   } else {
      _master._listComplete(progVector);
   }
}

}

/**
 * The private worker for the "fetch" method.
 */
final class SpServerAsyncFetch implements Runnable
{
   private SpAccess      _spAccess;
   private SpProgKey     _spProgKey;
   private SpServerAsync _master;

SpServerAsyncFetch(SpAccess spAccess, SpProgKey spProgKey, SpServerAsync master)
{
   _spAccess  = spAccess;
   _spProgKey = spProgKey;
   _master    = master;
}

public void
run()
{
   SpServer spServer = new SpServer();
   SpItem   spItem   = spServer.fetch(_spAccess, _spProgKey);

   if (spItem == null) {
      _master._errorComplete(spServer.getProblemDescr());
   } else {
      _master._fetchComplete(spItem);
   }
}

}


/**
 * The private worker for the "store" method.
 */
final class SpServerAsyncStore implements Runnable
{
   private SpAccess      _spAccess;
   private SpProgKey     _spProgKey;
   private SpItem        _spItem;
   private SpServerAsync _master;

SpServerAsyncStore(SpAccess spAccess, SpProgKey key, SpItem spItem,
                                                     SpServerAsync master)
{
   _spAccess  = spAccess;
   _spProgKey = key;
   _spItem    = spItem;
   _master    = master;
}

public void
run()
{
   SpServer spServer = new SpServer();

   SpItem spItem = spServer.store(_spAccess, _spProgKey, _spItem);

   if (spItem == null) {
      _master._errorComplete(spServer.getProblemDescr());
   } else {
      _master._storeComplete(spItem, _spProgKey, false);  // was existing prog
   }
}

}


/**
 * The private worker for the "storeNew" method.
 */
final class SpServerAsyncStoreNew implements Runnable
{
   private SpAccess      _spAccess;
   private SpItem        _spItem;
   private String        _user;
   private SpServerAsync _master;

SpServerAsyncStoreNew(SpAccess spAccess, SpItem spItem, String user,
                                                        SpServerAsync master)
{
   _spAccess  = spAccess;
   _spItem    = spItem;
   _user      = user;
   _master    = master;
}

public void
run()
{
   SpServer spServer = new SpServer();

   SpProgKey key = new SpProgKey();
   SpItem spItem = spServer.storeNew(_spAccess, _spItem, _user, key);
   if (spItem == null) {
     _master._errorComplete(spServer.getProblemDescr());
   } else {
     _master._storeComplete(spItem, key, true);  // was a "new" program
   }
}

}


/**
 * A class used to perform asynchronous stores and fetches of
 * science programs.
 */
public final class SpServerAsync
{
   private Thread          _thread;     // thread doing the async work
   private SpServerWatcher _watcher;    // client

/**
 * This method is called to obtain a listing of the programs for the given
 * user.
 */
public static SpServerAsync
list(SpAccess spAccess, String user, SpServerWatcher watcher)
{
   SpServerAsync     ssa  = new SpServerAsync(watcher);
   SpServerAsyncList ssal = new SpServerAsyncList(spAccess, user, ssa);
   ssa._start(ssal);

   return ssa;
}

/**
 * This method is called to fetch a program from the ODB.
 */
public static SpServerAsync
fetch(SpAccess spAccess, SpProgKey spProgKey, SpServerWatcher watcher)
{
   SpServerAsync      ssa  = new SpServerAsync(watcher);
   SpServerAsyncFetch ssaf = new SpServerAsyncFetch(spAccess, spProgKey, ssa);
   ssa._start(ssaf);

   return ssa;
}

/**
 * This method is called to store an update to (an existing) program to the ODB.
 */
public static SpServerAsync
store(SpAccess spAccess, SpProgKey key, SpItem spItem, SpServerWatcher watcher)
{
   SpServerAsync      ssa  = new SpServerAsync(watcher);
   SpServerAsyncStore ssas = new SpServerAsyncStore(spAccess, key, spItem, ssa);
   ssa._start(ssas);

   return ssa;
}

/**
 * This method is called to store a new program to the ODB.
 */
public static SpServerAsync
storeNew(SpAccess spAccess, SpItem spItem, String user, SpServerWatcher watcher)
{
   SpServerAsync         ssa   = new SpServerAsync(watcher);
   SpServerAsyncStoreNew ssasn = new SpServerAsyncStoreNew(spAccess, spItem, user, ssa);
   ssa._start(ssasn);

   return ssa;
}

/**
 * Construct with the watcher.  This method is made private to control
 * access to the SpServerAsync class.  To create an SpServerAsync, clients
 * must use one of the public static methods.
 */
private SpServerAsync(SpServerWatcher watcher)
{
   _watcher = watcher;
}


/**
 * Start the activity in a separate thread.
 */
private void
_start(Runnable rnble)
{
   // Create and start a separate thread to do the work.  Run at a lower
   // priority so that clients are always responsive.
   _thread = new Thread( rnble );
   _thread.setPriority(Thread.NORM_PRIORITY - 1);
   _thread.start();
}

/**
 * Abort the catalog read.
 *
 * @return true if the thread is aborted, false otherwise
 */
public synchronized boolean
abort()
{
   if (_thread == null) {
      return false;
   }

   _thread.stop();
   _thread = null;

   _watcher.spServerAbort(this);
   _watcher = null;

   return true;
}

/**
 * Notify the watcher that an error occurred.
 */
synchronized void
_errorComplete(String problem)
{
   _thread = null;
   _watcher.spServerError(problem, this);
   _watcher = null;
}

/**
 * Notify the watcher that the 'list' method is complete.
 */
synchronized void
_listComplete(Vector progVector)
{
   _thread = null;
   _watcher.spServerListComplete(progVector, this);
   _watcher = null;
}

/**
 * Notify the watcher that the 'fetch' method is complete.
 */
synchronized void
_fetchComplete(SpItem spItem)
{
   _thread = null;
   _watcher.spServerFetchComplete(spItem, this);
   _watcher = null;
}

/**
 * Notify the watcher that the 'fetch' method is complete.
 */
synchronized void
_storeComplete(SpItem spItem, SpProgKey key, boolean isNew)
{
   _thread = null;
   _watcher.spServerStoreComplete(spItem, key, isNew, this);
   _watcher = null;
}

}
