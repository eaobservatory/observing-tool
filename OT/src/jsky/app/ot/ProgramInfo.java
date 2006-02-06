// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;

import java.util.Hashtable;

/**
 * This class groups information that should be global to a given program.
 * There is one ProgramInfo class per open program.
 */
public final class ProgramInfo
{
   /**
    * The isPlan variable is true when the object being edited is a
    * Science Plan, and false when it is a Science Program.
    */
   public boolean isPlan = false;

   /**
    * When online is true, every edit made to the program or plan is
    * reflected to the database.
    */
   public boolean   online = false;

   /**
    * The filename, save state etc.
    */
   public FileInfo file     = null;

   /**
    * The user name under which the program or plan is stored in the database.
    */
   public LoginInfo login   = null;

   /**
    * Contains a mapping of Science Program/Plan items to ProgramInfo
    * structures.
    */
   private static Hashtable _map = new Hashtable();

/**
 * Register ProgramInfo for a  given root item.
 */
public static void
register(ProgramInfo pi, SpItem rootSpItem)
{
   _map.put(rootSpItem, pi);
}

/**
 * Get the ProgramInfo for a given item.  This method looks up the
 * root item, and then uses that to look in the hastable for
 * the ProgramInfo.
 */
public static ProgramInfo 
get(SpItem spItem)
{
   SpItem root = SpTreeMan.findRootItem(spItem);
   return (ProgramInfo) _map.get(root);
}

}
