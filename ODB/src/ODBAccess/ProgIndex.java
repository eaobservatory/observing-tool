/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package ODBAccess;

import java.io.*;
import java.util.*;
import orac.util.LookUpTable;

/**
 * This keeps an index of the stored programs (and their keys) for an ODB user.
 */
public class ProgIndex
{
  private LookUpTable _progIndex;
  private String _keyBase;
  private String _dirBase;

/**
 * The constructor reads the index file for the given userrecord
 */
  public ProgIndex (String BaseDir, UserRecord ur) {

    BufferedReader br;

    // Set the keybase
    _dirBase = ur.getDirname();
    _keyBase = _dirBase + "-";

    // Create lookuptable to hold the index. If there is a problem opening or
    // reading the index file then this will show up to the user as an empty
    // index (which admittedly might worry them - they'll report it!
    _progIndex = new LookUpTable ();

    // Read the index file into a lut
    try {
      String indexFilename = BaseDir + ur.getDirname()+"/.index";
      br = new BufferedReader (new FileReader (indexFilename));

      String line;
      StringTokenizer st;
      
      // while there are more lines
      while ((line = br.readLine()) != null) {
	
	// ignore blanks
	if (line.length() == 0) continue;
	
	// ignore comments if not yet started block
	if (line.startsWith("#")) continue;
	
	// parse the line
	st = new StringTokenizer (line,":");
	Vector record = new Vector();
	while (st.hasMoreTokens()) {
	  record.addElement (st.nextToken().trim());
	}
	
	// append record
	_progIndex.addRow (record);
      }
      
    } catch (FileNotFoundException ex) {
      System.out.println("Problem opening the progindex file: " + ex);
    } catch (IOException ex) {
      System.out.println("Problem reading the progindex file: " + ex);
    }

  }

  /**
   * Return the program name for a given key
   */
  public String getProgname (String key) {
    try {
      int row = _progIndex.indexInColumn (key, 0);
      return (String) _progIndex.elementAt (row,1);
    }catch (Exception ex) {
      return null;
    }
  }

  /**
   * Return the key for a given progname
   */
  public String getKey (String progName) {

    try {
      int row = _progIndex.indexInColumn (progName, 1);
      return (String) _progIndex.elementAt (row,0);
    }catch (Exception ex) {
      return null;
    }

  }

  /**
   * Return the directory base for a given key
   */
  public static String getBaseDir (String key) {
    int pos = key.lastIndexOf ("-");
    try {
      return key.substring (0,pos);
    }catch (Exception ex) {
      return null;
    }
  }

  /**
   * Return the complete proglist, sans keys
   */
  public String[] getProgList () {
    String[] deflist = {"None"};
    if (_progIndex.getNumRows() == 0) return deflist;

    Vector pv = _progIndex.getColumn(1);
    String[] list = new String[pv.size()];
    for (int i=0; i<pv.size();i++) {
      list[i] = (String) pv.elementAt(i);
    }

    return list;
  }

  /**
   * Return a new key
   */
  public String getNewKey () {
    int nextNum = _progIndex.getNumRows();
    //    return _keyBase + String.valueOf(nextNum);
    // For first UKIRT tests hardwire all keys to "username-0"
    return _keyBase + "0";
  }

  /**
   * Add a new program/key pair
   */
  public void addProgram (String progName, String key) {
    Vector rec = new Vector();
    rec.addElement (key);
    rec.addElement (progName);
    _progIndex.addRow (rec);
  }

  /**
   * Update the index on disk
   */
  public void update (String BaseDir, UserRecord ur) {

    try {
      String indexFilename = BaseDir + ur.getDirname()+"/.index";
      PrintWriter pw = new PrintWriter (new FileOutputStream (indexFilename));
      
      String line;

      // for each record construct the string and write it
      for (int i = 0; i<_progIndex.getNumRows(); i++) {
	Vector rec = _progIndex.getRow(i);
	line = (String) rec.elementAt(0) + ":" + (String) rec.elementAt(1);
	pw.println(line);
      }
      pw.flush();
      pw.close();
    } catch (IOException ex) {
      System.out.println("Problem opening the progindex file: " + ex);
    }
    
  }

}
