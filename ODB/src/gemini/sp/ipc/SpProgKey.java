// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.ipc;

/**
 * This class mirrors the ScienceProgram.Server.ProgKey class.  It is used
 * by clients of the SpServer so that they do not have to know about or
 * use any classes from the ScienceProgram package hierarchy.
 */
public class SpProgKey
{
   public String id;
   public String key;

   /**
    * Default constructor.
    */
   public SpProgKey()
   {
      id  = "";
      key = "";
   }

   /**
    * Construct with id and key.
    */
   public SpProgKey(String id, String key)
   {
      this.id  = id;
      this.key = key;
   }

   /**
    * Obtain a string representation.
    */
   public String
   toString()
   {
      return "id = `" + id + "', key = `" + key + "'";
   }

}

