// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util;

/**
 * This is a data object that describes a server definition in a
 * Skycat config file.
 */
public class SkycatServer
{
   public static final String CATALOG_TYPE      = "catalog";
   public static final String IMAGE_TYPE        = "imagesvr";
 
   public static final String SERV_TYPE_TAG     = "serv_type";
   public static final String LONG_NAME_TAG     = "long_name";
   public static final String SHORT_NAME_TAG    = "short_name";
   public static final String URL_TAG           = "url";

   /**
    * The server type.  For instance, catalog server or image server.
    * Note the variable name uses the convention in skycat.cfg.
    */
   public String serv_type;

   /**
    * The long name.  This is used in menus to identify the server.
    * Note the variable name uses the convention in skycat.cfg.
    */
   public String long_name;

   /**
    * The short name.
    * Note the variable name uses the convention in skycat.cfg.
    */
   public String short_name;

   /**
    * The url.  This is parsed to contact the server with the
    * appropriate query string.
    */
   public String url;

/**
 * Default constructor, does not set any of the fields.
 */
public SkycatServer() { }

/**
 * Construct with each of the fields.
 */
public SkycatServer(String type, String longName, String shortName, String url)
{
   serv_type  = type;
   long_name  = longName;
   short_name = shortName;
   this.url   = url;
}

/**
 * The standard debugging toString method.
 */
public String
toString()
{
   return getClass().getName() + "[" + serv_type + ", " + long_name + ", " +
      short_name + ", " + url + "]";
}

}
