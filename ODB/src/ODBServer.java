/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.net.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import ODBServerPackage.Access;
import ODBServerPackage.AccessException;
import ODBServerPackage.Database;
import ODBServerPackage.ProgKey;
import ODBServerPackage.ItemPosition;
import ODBServerPackage.AvPair;
import ODBServerPackage.OdbException;
import ODBServerPackage.ProgKeyHolder;
import ODBServerPackage.ODBServerMethods;
import ODBAccess.*;
import gemini.sp.*;
import orac.ukirt.util.*;
import orac.ukirt.inst.*;
import orac.ukirt.iter.*;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

/**
 * This class implements a very simple server for Science Program objects,
 * allowing remote storage and retrieval using Remote Method Invocation.
 * Science Programs are stored (as SGML files) in a simple directory
 * structure.
 *
 * @author: Alan Bridger
 *
 */

class ODBServer extends UnicastRemoteObject implements ODBServerMethods
{
  private static String database_dir = null;
  private static String serverName = null;

  private static AdminTable _adminTable;

  private static PrintWriter _pwlog=null;

  public ODBServer() throws RemoteException {
    super ();

    // Create instances of the UKIRT items - this will cause them to be
    // registered with SpFactory - necessary so that the component subtypes
    // can be understood.
    SpItem spItem = new SpInstUFTI();
    spItem = new SpInstCGS4();
    spItem = new SpInstIRCAM3();
    spItem = new SpInstMichelle();
    spItem = new SpDRRecipe();
    spItem = new SpIterBiasObs();
    spItem = new SpIterDarkObs();
    spItem = new SpIterCGS4();
    spItem = new SpIterMichelle();
    spItem = new SpIterUFTI();
    spItem = new SpIterIRCAM3();
    spItem = new SpIterCGS4CalUnit();
    spItem = new SpIterCGS4CalObs();
    spItem = new SpIterMichelleCalObs();
    spItem = new SpIterFP();
    spItem = new SpIterIRPOL();
  }


  /**
   * Return a list of the programs available for the given user
   */
  public final String[] list (Access ac, String user) 
    throws AccessException, OdbException, 
	   java.rmi.RemoteException
    {

      _pwlog.println ("List called for "+user);

      // Validate password - find the userrecord and check it
      UserRecord ur = _adminTable.getUser (user);
      if (!ur.checkAccess(ac.password)) {
	_pwlog.println ("access check failed!");
	_pwlog.flush();
	throw new AccessException("Authorisation failed");
      }
      ProgIndex pi = new ProgIndex (database_dir, ur);
      _pwlog.flush();
      return pi.getProgList();

    }

  /**
   * Fetch a program from the database, given the access object and the 
   * program's key
   */
  public final byte[] fetch(Access ac, ProgKey key)
    throws AccessException, OdbException,
	   java.rmi.RemoteException
    {

      _pwlog.println ("Fetch called for program "+key.id+" with key "+key.key);
      _pwlog.flush();

      // Check the key and progname match
      String dirname = ProgIndex.getBaseDir (key.key);
      // if this null we should give up now...
      if (dirname != null) {
	UserRecord ur = _adminTable.getUserByDir (dirname);
	ProgIndex pi = new ProgIndex (database_dir, ur);
	if (pi.getKey (key.id).equals(key.key)) {
	  // Check the directory and password match
	  if (!ur.checkAccess(ac.password, dirname)) {
	    _pwlog.println ("Invalid password specified");
	    _pwlog.flush();
	    throw new AccessException ("Invalid password");
	  }
	}else {
	  _pwlog.println ("Invalid key specified");
	  _pwlog.flush();
	  throw new AccessException ("Key/program mismatch");
	}

	File f = new File (database_dir+dirname, key.id);
      
	FileReader fr;
	try {
	  fr = new FileReader(f);
	} catch (FileNotFoundException fnfex) {
	  _pwlog.println ("File "+key.id+" not found");
	  _pwlog.flush();
	  throw new AccessException ("File not found");
	}
	SpInputSGML inSGML = new SpInputSGML(fr);
	SpRootItem newItem = inSGML.parseDocument();
	try {
	  fr.close();
	} catch (IOException ioex) {
	  _pwlog.println ("Error closing file "+key.id);
	  _pwlog.flush();
	}
	
	if (newItem == null) {
	  _pwlog.println("Problem reading SGML: "+inSGML.getProblemDescr());
	  _pwlog.flush();
	  throw new AccessException ("Problem reading file, contact staff member");
	}
	_pwlog.flush();
	
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	try {
          GZIPOutputStream gzos = new GZIPOutputStream (bos);
	  newItem.printDocument((OutputStream) gzos);   
	  gzos.flush();
	  gzos.close();
	}catch (IOException ioex) {
	  _pwlog.println ("Error encoding file");
	  _pwlog.flush();
	  throw new AccessException ("Problem encoding file for delivery");
	}
	byte[] byteProg = bos.toByteArray();
	try {
	  bos.close();
	}catch (IOException ioex) {
	  _pwlog.println ("Error closing stream");
	  _pwlog.flush();
	}

	// Null the Sp items to try to encourage garbase collection
	newItem = null;
	inSGML = null;
	bos = null;

	return byteProg;
      }else{
	throw new AccessException ("Problem with key");
      }
    }

  /**
   * Store a program that has been stored before to the database.
   */
  public final byte[] store(Access ac, ProgKey key, byte[] inProg)
    throws AccessException, OdbException,
	   java.rmi.RemoteException
    {
      SpItem prog=null;

      _pwlog.println ("Store called for program "+key.id+" with key "+key.key);

      ByteArrayInputStream bis = new ByteArrayInputStream (inProg);
      try {
        GZIPInputStream gzis = new GZIPInputStream (bis);
        SpInputSGML inSGML = new SpInputSGML((InputStream) gzis);
        prog = inSGML.parseDocument();
	gzis.close();
	inSGML = null;
	gzis = null;
      }catch (IOException ioex) {
	_pwlog.println ("Error decoding program "+key.id);
	_pwlog.flush();
	throw new OdbException ("Error decoding program!");
      }finally{
	try {
	  bis.close();
	  bis = null;
	}catch (IOException ioex) {
	  _pwlog.println ("Error closing input stream!");
	}
      }


      // Generate a file name and get a file handle
      String filename = prog.getTable().get(".gui.filename");
      String dirname = ProgIndex.getBaseDir (key.key);
      UserRecord ur = _adminTable.getUserByDir (dirname);

      File f = new File (database_dir+dirname, filename);
      // Check to see if file exists. If it does then create a backup -
      // and then need to create a new one
      if (f.exists()) {
	f.renameTo (new File (database_dir+dirname, filename+".BAK"));
	f = new File (database_dir+dirname, filename);
      }
      
      // Save the program.
      try {
	FileOutputStream fos = new FileOutputStream(f);
	OutputStream os = new BufferedOutputStream(fos);
	((SpRootItem) prog).printDocument(os);	
	os.flush();
	os.close();
	fos.close();
	fos = null;
	os =null;
      } catch (SecurityException sex) {
	_pwlog.println ("ODBServer does not have access to " +
		     f.getAbsolutePath());
	_pwlog.flush();
	throw new AccessException ("Error accessing directory");
      } catch (IOException ioex) {
	_pwlog.println ("IO Exception: "+ioex.toString());
	_pwlog.flush();
	throw new OdbException ("Error writing file");
      }

      _pwlog.flush();

      // Null to encourage garbage collection and return the original
      prog = null;

      return inProg;
    }

  /**
   * Store a new program to the database. Returns a program key object for
   * the program.
   */
  public final ProgKey storeNew (Access ac, byte[] inProg, String user, 
			  ProgKeyHolder key)
    throws AccessException, OdbException,
	   java.rmi.RemoteException
    {

      SpItem newProg=null;

      _pwlog.println ("StoreNew called for "+user);
      
      // For a new store need to check access. Also need to generate a key
      // for this program.
      UserRecord ur = _adminTable.getUser (user);
      if (!ur.checkAccess(ac.password)) {
	_pwlog.println ("access check failed!");
	_pwlog.flush();
	throw new AccessException("Authorisation failed");
      }

      // Decode object from incoming bytes. Need to do this to interogate
      // it for its name.
      ByteArrayInputStream bis = new ByteArrayInputStream (inProg); 
      try {
        GZIPInputStream gzis = new GZIPInputStream (bis);
        SpInputSGML inSGML = new SpInputSGML((InputStream) gzis);
        newProg = inSGML.parseDocument();
	gzis.close();
	inSGML = null;
	gzis = null;
      }catch (IOException ioex) {
	_pwlog.println ("Error decoding program");
	_pwlog.flush();
	throw new OdbException ("Error decoding program!");
      }finally{
	try {
	  bis.close();
	  bis = null;
	}catch (IOException ioex) {
	  _pwlog.println ("Error closing input stream!");
	}
      }


      // Generate a file name and get a file handle      
      String filename = newProg.getTable().get(".gui.filename");

      // Check filename isn't in use - try to get a key for it. If this
      // _succeeds_ then it is a problem...
      ProgIndex pi = new ProgIndex (database_dir, ur);
      if (pi.getKey (filename) != null) {
	_pwlog.println ("Storenew called for existing filename! - "+filename);
	_pwlog.flush();
	throw new OdbException ("Name already in use");
      }

      String dirname = ur.getDirname();
      File f = new File (database_dir+dirname, filename);
      // Check to see if file exists. If it does then create a backup -
      // and need to create a new one?
      if (f.exists()) {
	f.renameTo (new File (database_dir+dirname, filename+".BAK"));
	f = new File (database_dir+dirname, filename);
      }

      // Save the program.
      try {
	FileOutputStream fos = new FileOutputStream(f);
	OutputStream os = new BufferedOutputStream(fos);
	((SpRootItem) newProg).printDocument(os);
	os.flush();
	os.close();
	fos.close();
	fos = null;
	os = null;
      } catch (SecurityException sex) {
	_pwlog.println ("ODBServer does not have access to " +
		     f.getAbsolutePath());
	_pwlog.flush();
	throw new AccessException ("Error accessing directory");
      } catch (IOException ioex) {
	_pwlog.println ("IO Exception: "+ioex.toString());
	_pwlog.flush();
	throw new AccessException ("Error writing file");
      } catch (Exception ex) {
	_pwlog.println ("Other Exception: "+ex);
	_pwlog.flush();
	throw new AccessException ("Unknown problem");
      }

      // Get a key and record this new program
      String newKey = pi.getNewKey();
      pi.addProgram(filename, newKey);
      pi.update(database_dir, ur);
      
      ProgKey pk = new ProgKey (filename, newKey);
      _pwlog.println ("Generated key "+pk.key+" for "+pk.id);
      _pwlog.flush();

      return pk;

    }

  /**
   * Public methods used for administration  .... or should these be seperate?
   * None are yet implemented.
   */
  //  public void newUser (String username, String password, String dirname)
  //  public void modUser ...

  /**
   * Replace an av pair in a program in the database. NOT YET IMPLEMENTED.
   */
  public final void replaceAV (Access ac, ProgKey key, String itemId,
			 AvPair[] newList)
    throws AccessException, OdbException,
	   java.rmi.RemoteException
    {
    }

  /**
   * Add an SpItem in a specific position in a program in the database.
   * NOT YET IMPLEMENTED.
   */
  public final SpItem add(Access ac, ProgKey key, SpItem newItem,
		    String toId, ItemPosition posn)
    throws AccessException, OdbException,
	   java.rmi.RemoteException
    {
      return null;
    }

  /**
   * Move an SpItem in a program in the database. NOT YET IMPLEMENTED.
   */
  public final void move(Access ac, ProgKey key, String fromId,
		   String toId, ItemPosition posn)
    throws AccessException, OdbException,
	   java.rmi.RemoteException
    {
    }

  /**
   * Remove an SpItem from a program in the database. NOT YET IMPLEMENTED.
   */
  public final void remove(Access ac, ProgKey key, String itemId)
    throws AccessException, OdbException,
	   java.rmi.RemoteException
    {
    }


  /** 
   * Open the log file
   */
  private static PrintWriter _openLog (String dirname) {
    PrintWriter pw=null;

    // Create a log file
    String logfile = dirname + "administrator/ODBServer.log";
    try {
      pw = new PrintWriter (new FileOutputStream (logfile, true));
    }catch (IOException ex) {
      System.out.println ("Error creating ODBServer log file "+logfile);
      System.exit (0);
    }
    logfile = null;

    pw.println ("New server created: "+new Date());
    return pw;
  }

  /** 
   * Read the admin file
   */
  private static AdminTable _readAdminFile (String dirname) {

    AdminTable at=null;

    // read the authorisation file
    String admFile = dirname + "administrator/.passwd";
    AdminFileReader afr = new AdminFileReader (admFile);
    try {
      at = afr.readFile ();
    }
    catch (IOException ioex) {
      _pwlog.println ("Problem reading adm file: "+ioex);
      _pwlog.println ("Exiting");
      _pwlog.flush();
      _pwlog.close();
      System.exit(0);
    }
    admFile = null;
    afr = null;

    return at;
  }

  /**
   * Cause the server to exit cleanly.
   */
  public final void exit () throws RemoteException {
    _pwlog.println ("ODBServer exiting");
    _pwlog.flush();
    _pwlog.close();
    System.exit(0);
    return;
  }

  /**
   * Read the ODB server's configuration file.
   */
  private static void _readCfgFile (String filename) {

    InstCfgReader instCfg = null;
    InstCfg instInfo = null;
    String block = null;
    
    instCfg = new InstCfgReader (filename);
    try {
      while ((block = instCfg.readBlock()) != null) {
	instInfo = new InstCfg (block);
	if (instInfo.getKeyword().equalsIgnoreCase("servername")) {
	  serverName = instInfo.getValue();
	  System.out.println ("serverName = "+serverName);
	}else if (instInfo.getKeyword().equalsIgnoreCase("database")) {
	  database_dir = instInfo.getValue();
	}
      }
    }catch (IOException e) {
      System.out.println ("Error reading obdserver cfg file");
    }
  }

  /**
     Main program for ODB Server
  */
  public static void main(String[] args)
    {

      // read the ODBServer configuration file
      String _baseDir = System.getProperty ("CFGDIR", "/ukirt_sw/oracrel/apps/ODBServer/cfg/");
      String _cfgFile = _baseDir + "odbserver.cfg";
      _readCfgFile (_cfgFile);
      _baseDir = null;
      _cfgFile = null;
      
      // Create a log file
      _pwlog = _openLog(database_dir);
      
      // read the authorisation file
      _adminTable = _readAdminFile (database_dir);
      
      try {
	System.out.println ("Creating new ODBServer...");
	_pwlog.println ("Creating new ODBServer...");
	ODBServer odbs =new ODBServer();
	System.out.println ("Rebinding "+serverName);
	_pwlog.println ("Rebinding "+serverName);
	Naming.rebind(serverName, odbs);
	
	System.out.println("ODBServer ready.");
	_pwlog.println("ODBServer ready.");
	
      } catch (RemoteException re) {
	System.out.println ("Exception in ODBServer main:"+re);
	_pwlog.println ("Exception in ODBServer main:"+re);
      } catch (MalformedURLException e) {
	System.out.println("MalformedURLException in ODBServer main:" + e);
	_pwlog.println("MalformedURLException in ODBServer main:" + e);
      } finally {
	_pwlog.flush();
      }
    }
  
}
