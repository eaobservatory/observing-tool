/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/
//
// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.ipc;

import java.io.*;
import java.rmi.*;
import java.util.*;
import java.util.zip.*;

import gemini.sp.*;

import ODBServerPackage.Access;
import ODBServerPackage.AccessException;
import ODBServerPackage.Database;
import ODBServerPackage.ProgKey;
import ODBServerPackage.ItemPosition;
import ODBServerPackage.AvPair;
import ODBServerPackage.OdbException;
import ODBServerPackage.ProgKeyHolder;
import ODBServerPackage.ODBServerMethods;


/**
 * Wrapper for the ScienceProgram.Server interface.  This class presents
 * an easier to use, more convenient interface to java clients.
 *
 * UKIRT Version of SpServer, using RMI to contact remote server. Same 
 * interface presented to clients.
 *
 * Original is in SpServer.java.Gemini
 *
 * @author: Alan Bridger
 *
 */
public final class SpServer
{
  // Development or deployed ODB server?
  public static final int DEVELOPMENT = 0;
  public static final int DEPLOYED    = 1;
  public static final int ATC_DEVELOPMENT = 2;
  public static final int HILO_DEVELOPMENT = 3;
  public static final int HOME_DEVELOPMENT = 4;

  public static final int CHILD   = ItemPosition._CHILD;
  public static final int SIBLING = ItemPosition._SIBLING;

  // Names of the possible remote servers.
  private static String ATC_SERVER = "rmi://alba.roe.ac.uk:4201/ODBServer";
  private static String DEPLOYED_SERVER = "rmi://kiki.ukirt.jach.hawaii.edu:4201/ODBServer";
  private static String HILO_SERVER = "rmi://kapa.jach.hawaii.edu:4201/ODBServer";
  private static String HOME_SERVER = "rmi://staffa.roe.ac.uk:4201/ODBServer";
  // Set a default name and server (should usually be deployed)
  private static String ODBSERVERNAME = DEPLOYED_SERVER;
  private static boolean  _useDeployedServer = true;

  private static ODBServerMethods   _spServer = null;
  
  
  private static boolean  _securityManagerLoaded = false;
  
  private        String   _problem  = null;

/**
 * Select either the development (SpServer.DEVELOPMENT) or deployed 
 * (SpServer.DEPLOYED) server. Extended to select the servername and
 * different development servers. AB 23-Apr-2000.
 */
public static void
selectServer(int server)
{
  if (server == DEVELOPMENT) {
    if (_useDeployedServer) {
      // Null the _spServer so that the next operation will reconnect.
      _spServer = null;
    }
    _useDeployedServer = false;
    ODBSERVERNAME = HILO_SERVER;
  } else if (server == ATC_DEVELOPMENT) {
    if (_useDeployedServer) {
      // Null the _spServer so that the next operation will reconnect.
      _spServer = null;
    }
    _useDeployedServer = false;
    ODBSERVERNAME = ATC_SERVER;
  } else if (server == HOME_DEVELOPMENT) {
    if (_useDeployedServer) {
      // Null the _spServer so that the next operation will reconnect.
      _spServer = null;
    }
    _useDeployedServer = false;
    ODBSERVERNAME = HOME_SERVER;
  } else if (server == HILO_DEVELOPMENT) {
    if (_useDeployedServer) {
      // Null the _spServer so that the next operation will reconnect.
      _spServer = null;
    }
    _useDeployedServer = false;
    ODBSERVERNAME = HILO_SERVER;
  } else {
    if (!_useDeployedServer) {
      // Null the _spServer so that the next operation will reconnect.
      _spServer = null;
    }
    _useDeployedServer = true;
    ODBSERVERNAME = DEPLOYED_SERVER;
  }
}

/**
 * If a method fails, clients can use getProblemDescr to see what went wrong.
 */
public String
getProblemDescr()
{
   return _problem;
}

/**
 * Convert the SpAccess structure into a local Access structure.
 */
private Access
_convertAccess(SpAccess spAccess)
{
   Database db;
   try {
      db = Database.from_int(spAccess.db);
   } catch (Exception ex) {
      _problem = "Specify either PHASEII or ACTIVE database";
      return null;
   }

   return new Access(spAccess.password, db, spAccess.onlineCapability);
}

/**
 * Convert the SpProgKey structure into a local ProgKey structure.
 */
private ProgKey
_convertProgKey(SpProgKey spProgKey)
{
   return new ProgKey(spProgKey.id, spProgKey.key);
}


/**
 * Obtain a listing of the programs for the given user.
 */
public boolean
list (SpAccess spAccess, String user, java.util.Vector progVector)
{
   boolean result = false;
   if (!_getServer()) {
      return false;
   }

   Access ac = _convertAccess(spAccess);
   if (ac == null) {
      return false;
   }

   String[] stringSeq;
   try {
     stringSeq = _spServer.list(ac, user);
     result    = true; 
      
     for (int i=0; i<stringSeq.length; ++i) {
       progVector.addElement(stringSeq[i]);
     }

   } catch (AccessException e) {
     _problem = "Access Error: " + e.info;

   } catch (OdbException e) {
     _problem = "Database Error: " + e.info;

   } catch (Exception e) {
     _problem = "RMI Exception: " + e.toString();
     _spServer = null;
   }

   ac = null;
   return result;
}

/**
 * method to go through an spitem heirarchy and graft the appropriate
 * clientdata onto each child
 */
private SpItem _graftClientData (SpItem spItem)
{

  // Get the prototype for the spItem
  SpItem temp = (SpItem) SpFactory.getPrototype(spItem.type());

  SpCloneableClientData ccd = (SpCloneableClientData) temp.getClientData();
  spItem.setClientData(ccd.clone(spItem));

  // Now copy the children
  Enumeration e = spItem.children();
  SpItem afterChild = null;
  while (e.hasMoreElements()) {
    SpItem child = (SpItem) e.nextElement();
    child = _graftClientData (child);
  }

  return spItem;
  
}

/**
 * method to go through an spitem heirarchy and remove the
 * clientdata from each child
 */
private SpItem _removeClientData (SpItem spItem)
{

  spItem.setClientData(null);
  
  // Now emasculate the children
  Enumeration e = spItem.children();
  SpItem afterChild = null;
  while (e.hasMoreElements()) {
    SpItem child = (SpItem) e.nextElement();
    child = _removeClientData (child);
  }

  return spItem;
  
}


/**
 * Fetch a program from the ODB.
 */
public SpItem
fetch(SpAccess spAccess, SpProgKey spProgKey)
{
  SpItem spItem = null;

  if (!_getServer()) {
    return null;
  }
  
  Access ac  = _convertAccess(spAccess);
  if (ac == null) {
      return null;
  }
  ProgKey pk = _convertProgKey(spProgKey);
  
  try {
    byte[] inProg = _spServer.fetch(ac, pk);
    ByteArrayInputStream bis = new ByteArrayInputStream (inProg);
    try {
      GZIPInputStream gzis = new GZIPInputStream (bis);
      SpInputSGML inSGML = new SpInputSGML((InputStream) gzis);
      spItem = inSGML.parseDocument();
      gzis.close();
      inSGML = null;
      gzis = null;
    }catch (IOException ioex) {
	System.out.println ("Error decoding program");
	spItem = null;
    }finally{
	try {
	    bis.close();
	    bis = null;
	}catch (IOException ioex) {
	    System.out.println ("Error closing input stream!");
	}
    }

    // Adding clientdata could fail - but this is not necessary for
    // some clients, so catch the exceptions. Where it is necessary
    // it is likely that there is something else wrong.
    try {
      spItem = _graftClientData (spItem);
    }catch (Exception ex) { }

    // set the name of this program before returning it
    spItem.name (pk.id);

  } catch (AccessException e) {
    _problem = "Access Error: " + e.getMessage();
    spItem = null;
    
  } catch (OdbException e) {
    _problem = "Database Error: " + e.getMessage();
    spItem = null;
     
  } catch (Exception e) {
    _problem = "RMI Error: " + e.getMessage();
     _spServer = null;
    spItem = null;
  }
  
  //if (spItem != null) {
  //   spItem.getEditFSM().reset();
  //}
  
  ac = null; pk = null;
  return spItem;
}

/**
 * Store a program to the ODB.
 */
public SpItem
store(SpAccess spAccess, SpProgKey spProgKey, SpItem spItem)
{
   SpItem newSpItem = null;

   if (!_getServer()) {
      return null;
   }

   Access ac  = _convertAccess(spAccess);
   if (ac == null) {
      return null;
   }
   ProgKey pk = _convertProgKey(spProgKey);

   // Convert the object to SGML stored in a byte array (simple to transfer)
   // Compress it as well.
   ByteArrayOutputStream bos = new ByteArrayOutputStream();
   try {
     GZIPOutputStream gzos = new GZIPOutputStream (bos);
     ((SpRootItem) spItem).printDocument((OutputStream) gzos);   
     gzos.flush();
     gzos.close();
   }catch (IOException ioex) {}
   byte[] byteProg = bos.toByteArray();
   
   try {
     byteProg = _spServer.store(ac, pk, byteProg);

   } catch (AccessException e) {
     _problem = "Access Error: " + e.info;
      return null;

   } catch (OdbException e) {
      _problem = "Database Error: " + e.info;
      return null;

   } catch (Exception e) {
      _problem = "Rmi Error: " + e;
     _spServer = null;
      return null;
   }
 
   ac = null; pk = null;  // just freeing the memory quicker
   return spItem;
}


/**
 * Store a new program to the ODB.
 *
 * The newKey structure is filled in if the storeNew is successful (i.e.,
 * this is an "out" parameter and must be allocated before the call but is
 * initialized by this method).
 *
 */
public SpItem
storeNew(SpAccess spAccess, SpItem spItem, String user, SpProgKey newKey)
{
   if (!_getServer()) {
      return null;
   }

   Access ac  = _convertAccess(spAccess);
   if (ac == null) {
      return null;
   }

   ProgKey pk = new ProgKey ("File", "User");
   ProgKeyHolder pkHolder = new ProgKeyHolder(pk);

   /* Have trouble inserting a progkey in the ProgKeyHolder argument
    * object when trying this with RMI -
    * the Progkey structure doesn't come through. Probably something to do with
    * references instead of actual objects - but *does* work with a local 
    * invocation. Have given up on the grounds that I don't see the need to 
    * do it this way. Just return a progkey instead.
    * 
    */

   ByteArrayOutputStream bos = new ByteArrayOutputStream();
   try {
     GZIPOutputStream gzos = new GZIPOutputStream (bos);
     ((SpRootItem) spItem).printDocument((OutputStream) gzos);
     gzos.flush();
     gzos.close();
   }catch (IOException ioex) {}
   byte[] byteProg = bos.toByteArray();

   try {
     pk = _spServer.storeNew(ac, byteProg, user, pkHolder);
     newKey.id  = pk.id;
     newKey.key = pk.key;
     
     // Since we've succeeded set the name of this program before returning it
     spItem.name (pk.id);
 
   } catch (AccessException e) {
      _problem = "Access Error: " + e.info;
      return null;

   } catch (OdbException e) {
      _problem = "Database Error: " + e.info;
      return null;

   } catch (Exception e) {
      _problem = "RMI Error: " + e;
     _spServer = null;
      return null;
   }
 
    ac = null; pkHolder = null;
    return spItem;
}


/**
 * Replace the attribute/value table of an item.
 */
public boolean
replaceAV(SpAccess spAccess, SpProgKey spProgKey,
          String   itemId,   SpAvTable avTab)
{
  AvPair[] avPair=null;

   if (!_getServer()) {
      return false;
   }

   Access ac  = _convertAccess(spAccess);
   if (ac == null) {
      return false;
   }
   ProgKey pk = _convertProgKey(spProgKey);

   boolean result = false;

   //   AvPair[] idlAV = SpToIdl.convertAV(avTab);

   try {
      _spServer.replaceAV(ac, pk, itemId, avPair);
      result = true;

   } catch (AccessException e) {
      _problem = "Access Error: " + e.info;

   } catch (OdbException e) {
      _problem = "Database Error: " + e.info;

   } catch (Exception e) {
      _problem = "RMI Error: " + e;
     _spServer = null;
   }

   ac = null; pk = null;
   return result;
}


/**
 * Add a new item to the program at the indicated position.
 */
public SpItem
add(SpAccess spAccess, SpProgKey spProgKey,
    SpItem spNewChild, String refItemId, int posn)
{
   if (!_getServer()) {
      return null;
   }

   Access ac  = _convertAccess(spAccess);
   if (ac == null) {
      return null;
   }
   ProgKey pk = _convertProgKey(spProgKey);

   SpItem newSpItem = null;
   //   Item idlChild = SpToIdl.convert(spNewChild);
   try {
      ItemPosition spPosn = ItemPosition.CHILD;
      if (posn == SIBLING) {
         spPosn = ItemPosition.SIBLING;
      }

      //      Item newIdlItem;
      //newIdlItem = _spServer.add(ac, pk, idlChild, refItemId, spPosn);
      //      newSpItem  = IdlToSp.convert(newIdlItem);
      newSpItem = _spServer.add(ac, pk, spNewChild, refItemId, spPosn);

   } catch (AccessException e) {
      _problem = "Access Error: " + e.info;

   } catch (OdbException e) {
      _problem = "Database Error: " + e.info;

   } catch (Exception e) {
      _problem = "RMI Error: " + e;
     _spServer = null;
   }

   ac = null; pk = null;
   return newSpItem;
}


/**
 * Move an item from one position to another.
 */
public boolean
move(SpAccess spAccess, SpProgKey spProgKey,
     String srcItemId, String destItemId, int posn)
{
   if (!_getServer()) {
      return false;
   }

   Access ac  = _convertAccess(spAccess);
   if (ac == null) {
      return false;
   }
   ProgKey pk = _convertProgKey(spProgKey);

   boolean result = false;
   try {
      ItemPosition ip = ItemPosition.from_int(posn);
      _spServer.move(ac, pk, srcItemId, destItemId, ip);
      result = true;

      //   } catch (org.omg.CORBA.BAD_PARAM e) {
      //_problem = "The position should be either CHILD or SIBLING not: " + posn;

   } catch (AccessException e) {
      _problem = "Access Error: " + e.info;

   } catch (OdbException e) {
      _problem = "Database Error: " + e.info;

   } catch (Exception e) {
      _problem = "RMI Error: " + e;
     _spServer = null;
   }

   ac = null; pk = null;
   return result;
}


/**
 * Remove an item.
 */
public boolean
remove(SpAccess spAccess, SpProgKey spProgKey, String itemId)
{
   if (!_getServer()) {
      return false;
   }

   Access ac  = _convertAccess(spAccess);
   if (ac == null) {
      return false;
   }
   ProgKey pk = _convertProgKey(spProgKey);
   
   boolean result = false;
   try {
      _spServer.remove(ac, pk, itemId);
      result = true;

   } catch (AccessException e) {
      _problem = "Access Error: " + e.info;

   } catch (OdbException e) {
      _problem = "Database Error: " + e.info;

   } catch (Exception e) {
      _problem = "Error: " + e;
   }

   ac = null; pk = null;
   return result;
}

/**
 * Get a reference to the Observing Database server.
 */
private boolean
_getServer()
{
   boolean result = false;
   if (_spServer != null) {
      return true;
   }

   synchronized (getClass()) {
      // Check again for the _spServer, another thread may have initialized
      // it.
      if (_spServer != null) {
         return true;
      }

      String action = "";
//       if (!_securityManagerLoaded) {
// 	action = "Loading security manager";
// 	try {
// 	  System.setSecurityManager(new RMISecurityManager());
// 	  _securityManagerLoaded = true;
// 	}catch (SecurityException e) {
// 	  _problem = "Problem " + action + ": " + e;
// 	}
//       }

      try
      {
	  System.out.println ("usedeployed = " + _useDeployedServer);
	  if (_useDeployedServer) {
	    System.out.println("Looking up the DEPLOYED ODB server...");
	    action = "finding the Science Program Server";
	  } else {
            // Look up the development server.
            System.out.println("Looking up the DEVELOPMENT ODB server...");
            action = "finding the DEVELOPMENT Science Program Server";
	  }
	  _spServer = 
	    (ODBServerMethods) Naming.lookup(ODBSERVERNAME);
         result = true;

      } catch (Exception e) {
         _problem = "Problem " + action + " (might be down?): " + e.getMessage();
      }
   }

   return result;
}

}
