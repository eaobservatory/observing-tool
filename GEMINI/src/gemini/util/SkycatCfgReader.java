// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * A class for reading and parsing ESO skycat.cfg files.  This is just
 * a start.
 */
public class SkycatCfgReader
{
   /**
    * This is the name of the directory that will be used by default
    * to look for the skycat.cfg file.
    */
   public static final String DEFAULT_DIRECTORY = ".gemini";

   /**
    * The file containing the skycat information.
    */
   public static final String SKYCAT_CFG_FILE = "skycat.cfg";

   // A vector of SkycatServer objects for the catalog servers.
   private static Vector _catalog;

   // A vector of SkycatServer objects for the image servers.
   private static Vector _imagesrv;

/**
 * Load the config file called "skycat.cfg" from the user's home directory
 * under .gemini.  If the file doesn't exist or cannot be loaded for some
 * reason, false is returned.
 */
public static boolean
load()
{
   String homedir = System.getProperty("user.home");
   if (homedir == null) return false;

   String absdir = homedir + File.separatorChar + DEFAULT_DIRECTORY;
   File f = new File(absdir, SKYCAT_CFG_FILE);

   boolean res = load(f);
   if (res) {
      System.out.println("Using the skycat.cfg file: " + f.getAbsolutePath());
   }
   return res;
}


/**
 * Load the config file called "skycat.cfg" from the given directory.
 * If the file doesn't exist or cannot be loaded for some reason, false
 * is returned.
 */
public static boolean
load(String dir)
{
   File f = new File(dir, SKYCAT_CFG_FILE);

   boolean res = load(f);
   if (res) {
      System.out.println("Using the skycat.cfg file: " + f.getAbsolutePath());
   }
   return res;
}
 
/**
 * Load the config file refered to by the given File.  If the file
 * doesn't exist or cannot be loaded for some reason, false is returned.
 */
public static boolean
load(File f)
{
   FileInputStream fis = null;
   try {
      fis = new FileInputStream(f);
      return load(fis);
   } catch (FileNotFoundException ex) {
      //System.out.println("File not found: " + f.getAbsolutePath());
      return false;
   } catch (IOException ex) {
      System.out.println("Error reading: " + f.getAbsolutePath());
      return false;
   }
}

/**
 * Load the config file using the given base URL.  If the "skycat.cfg"
 * file doesn't exist at this location or cannot be loaded for some
 * reason, false is returned.
 */
public static boolean
load(URL url)
{
   try {
      return load(url.openStream());
   } catch (IOException ex) {
      return false;
   }
}

/**
 * Load the the config file using the given base URL and String filepath.
 * If the config file doesn't exist at this location or cannot be loaded
 * for some reason, false is returned.
 */
public static boolean
load(URL baseURL, String file)
{
   URL url;
   try {
      url = new URL(baseURL, file);
   } catch (MalformedURLException ex) {
      System.out.println("Problem constructing the URL: " + ex);
      return false;
   }

   return load(url);
}

/**
 * Load the skycat config file from the given InputStream.
 */
public static boolean
load(InputStream is)
   throws IOException
{
   _catalog  = new Vector();
   _imagesrv = new Vector();

   BufferedReader br = null;
   try {
      br = new BufferedReader( new InputStreamReader(is) );
      _parse(br);
   } finally {
      if (br != null) br.close();
   }

   return true;
}

/**
 * Get the catalog servers mentioned in the configuration file that
 * was loaded.
 */
public static SkycatServer[]
getCatalogServers()
{
   int size = _catalog.size();
   if (size == 0) {
      return null;
   }

   SkycatServer[] a = new SkycatServer[size];
   for (int i=0; i<size; ++i) {
      a[i] = (SkycatServer) _catalog.elementAt(i);
   } 
   return a;
}

/**
 * Get the image servers that were mentioned in the configuration file
 * that was loaded.
 */
public static SkycatServer[]
getImageServers()
{
   int size = _imagesrv.size();
   if (size == 0) {
      return null;
   }

   SkycatServer[] a = new SkycatServer[size];
   for (int i=0; i<size; ++i) {
      a[i] = (SkycatServer) _imagesrv.elementAt(i);
   } 
   return a;
}

//
// Read the config information from the given DataInputStream.
//
private static void
_parse(BufferedReader br)
   throws IOException
{
   String line;
   while ((line = br.readLine()) != null) {
      if (!line.startsWith(SkycatServer.SERV_TYPE_TAG)) {
         continue;
      }

      SkycatServer ss = new SkycatServer();

      ss.serv_type = _parseLine(line, SkycatServer.SERV_TYPE_TAG);
      if (ss.serv_type == null) continue;

      line = br.readLine(); if (line == null) return;
      ss.long_name = _parseLine(line, SkycatServer.LONG_NAME_TAG);
      if (ss.long_name == null) continue;

      line = br.readLine(); if (line == null) return;
      ss.short_name = _parseLine(line, SkycatServer.SHORT_NAME_TAG);
      if (ss.short_name == null) continue;

      line = br.readLine(); if (line == null) return;
      ss.url = _parseLine(line, SkycatServer.URL_TAG);
      if (ss.url == null) continue;

      if (ss.serv_type.equals(SkycatServer.CATALOG_TYPE)) {
         _catalog.addElement(ss);
      } else
      if (ss.serv_type.equals(SkycatServer.IMAGE_TYPE)) {
         _imagesrv.addElement(ss);
      }
   }
}

//
// Expect to see the given tag.  If so, return the value.
//
private static String
_parseLine(String line, String tag)
{
   // Make sure this line has the right tag
   if (!line.startsWith(tag)) return null;

   // Parse the line
   int i = line.indexOf(':');
   return line.substring(i+1).trim();
}

}
