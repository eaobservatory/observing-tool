// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.ipc;

//import ScienceProgram.ServerPackage.Database;
import ODBServerPackage.Database;

/**
 * This class mirrors the ScienceProgram.Server.Access class.  It is used
 * by clients of the SpServer so that they do not have to know about or
 * use any classes from the ScienceProgram package hierarchy.
 */
public class SpAccess
{
   public static final int PHASEII = Database._PHASEII;
   public static final int ACTIVE  = Database._ACTIVE;

   public String password;
   public int    db;
   public String onlineCapability;

   /**
    * Default constructor.
    */
   public SpAccess()
   {
      password         = "";
      db               = -1;
      onlineCapability = "";
   }

   /**
    * Construct with password and db.
    */
   public SpAccess(String password, int db)
   {
      this.password         = password;
      this.db               = db;
      this.onlineCapability = "";
   }

   /**
    * Construct with password, db, and onlineCapability.
    */
   public SpAccess(String password, int db, String onlineCapability)
   {
      this.password         = password;
      this.db               = db;
      this.onlineCapability = onlineCapability;
   }


   /**
    * Obtain a string representation.
    */
   public String
   toString()
   {
      String res = "password = `" + password + "', ";
      if (db == PHASEII) {
         res += "db = PHASEII, ";
      } else {
         res += "db = ACTIVE, ";
      }
      res += "onlineCapability = `" + onlineCapability + "'";
      return res;
   }
}

