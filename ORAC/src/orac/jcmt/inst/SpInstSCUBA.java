/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.jcmt.inst;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Arrays;

import orac.jcmt.SpJCMTConstants;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

/**
 * The SCUBA instrument Observation Component
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public final class SpInstSCUBA extends SpJCMTInstObsComp {

  /**
   * Radius of circular SCUBA science area.
   *
   * The radius is 2.3 arcmin.
   */
  public static final double SCIENCE_AREA_RADIUS = 2.3; 

  private static String [][] _filters            = null;
  private static Hashtable   _filterTable        = new Hashtable();
  private static Vector      _longBolometers;
  private static Vector      _shortBolometers;
  private static String [][] _jigglePatterns     = null;
  private static Hashtable   _jigglePatternTable = new Hashtable();

  public static final SpType SP_TYPE =
    SpType.create( SpType.OBSERVATION_COMPONENT_TYPE, "inst.SCUBA", "SCUBA" );

// Register the prototype.
  static {
    SpFactory.registerPrototype( new SpInstSCUBA() );
  }


  public SpInstSCUBA() {
    super( SP_TYPE );

    // Read in the instrument config file
    String baseDir = System.getProperty("ot.cfgdir");
    _readCfgFile(baseDir + "scuba.cfg");
    _readFlatFieldFile(baseDir + "flat.dat");


    for(int i = 0; i < (_filters.length - 1); i += 2) {
      // Use lower case keywords for filter table to allow for
      // case insensitive look-up later.
      _filterTable.put(_filters[i][0], _filters[i + 1]);
    }

    for(int i = 0; i < (_jigglePatterns.length - 1); i += 2) {
      // Use lower case keywords for filter table to allow for
      // case insensitive look-up later.
      _jigglePatternTable.put(_jigglePatterns[i][0], _jigglePatterns[i + 1]);
    }

    try {
      _avTable.noNotifySet(ATTR_FILTER,                 _filters[0][0], 0);
      _avTable.noNotifySet(ATTR_PRIMARY_SUB_INSTRUMENT, _filters[1][0], 0);
      _avTable.noNotifySet(ATTR_SUB_INSTRUMENT,         _filters[1][0], 0);

      // By default no explicit bolometer.
    }
    catch(Exception e) {
      System.out.println("Could not set SCUBA instrument component to values.");
      e.printStackTrace();
    }
  }

  /**
   *  Read the instrument configuration file.
   */
  private static void _readCfgFile( String filename ) {

    InstCfgReader instCfg = null;
    InstCfg instInfo = null;
    String block = null;

    instCfg = new InstCfgReader( filename );
    try {
      while ( ( block = instCfg.readBlock() ) != null ) {
        instInfo = new InstCfg( block );
        if ( InstCfg.matchAttr( instInfo, "FILTERS" ) ) {
          _filters = instInfo.getValueAs2DArray();
        }
	
	if ( InstCfg.matchAttr( instInfo, "JIGGLE_PATTERNS" )) {
          _jigglePatterns = instInfo.getValueAs2DArray();
	}
      }
    }
    catch (IOException e) {
      System.out.println ("Error reading SCUBA cfg file \"" + filename + "\"");
    }
  }

  /**
   *  Read the instrument configuration file.
   */
  private static void _readFlatFieldFile( String filename ) {
    _longBolometers  = new Vector();
    _shortBolometers = new Vector();

    try {
      LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(filename));
      String line;
      String name;
      String type;
      StringTokenizer stringTokenizer;
      
      while(true) {
        line = lineNumberReader.readLine();
        if(line == null) {
          break;
        }

      
        if(line.trim().startsWith("SETBOL")) {
          stringTokenizer = new StringTokenizer(line, " ");
	  stringTokenizer.nextToken();

	  name = stringTokenizer.nextToken();
	  type = stringTokenizer.nextToken();

          if(type.toLowerCase().equals("long")) {
            _longBolometers.add(name);
	  }

          if(type.toLowerCase().equals("short")) {
            _shortBolometers.add(name);
	  }	  
	}  
      }
    }
    catch (IOException e) {
      System.out.println ("Error reading SCUBA flat field file \"" + filename + "\"");
    }
  }


  public static Enumeration filters() {
    return _filterTable.keys();
  }


  /**
   * Get sub-instruments for a filter.
   *
   * @param filter is case sensitive. 
   *
   * @return String array of sub-instruments or null if the specified filter does not exist.
   */
  public static String [] getSubInstrumentsFor(String filter) {
    return (String[])_filterTable.get(filter);
  }


  public static Vector getBolometersFor(String subInstrument) {
    if(subInstrument == null) {
      return null;
    }

    if(subInstrument.toLowerCase().equals("long")) {
      return _longBolometers;
    }

    if(subInstrument.toLowerCase().equals("short")) {
      return _shortBolometers;
    }
    
    return null;
  }


  /**
   * Get jiggle pattern options for a given sub-instrument.
   *
   * @return String array of jiggle pattern options or null if the specified sub-instrument does not exist.
   */
  public static String [] getJigglePatternsForSubInstrument(String subInstrument) {
    return (String[])_jigglePatternTable.get(subInstrument);
  }

  /**
   * Get jiggle pattern options for the current sub-instrument.
   *
   * @return String array of jiggle pattern options.
   */
  public String [] getJigglePatterns() {
    return (String[])_jigglePatternTable.get(getPrimarySubInstrument());
  }


  public String getFilter() {
    return _avTable.get(ATTR_FILTER);
  }

  public void setFilter(String filter) {
    _avTable.set(ATTR_FILTER, filter);
  }

  public String [] getSubInstruments() {
    Enumeration subInstNames  = _avTable.attributes(ATTR_SUB_INSTRUMENT);
    Vector      subInstVector = new Vector();
    String []   result = null;

    while(subInstNames.hasMoreElements()) {
      subInstVector.add(_avTable.get((String)subInstNames.nextElement()));
    }

    result = new String[subInstVector.size()];
    subInstVector.toArray(result);
    
    return result;
  }


  /**
   * @param subInstrument Object[] rather then String[] is used to simplify GUI.
   */
  public void setSubInstruments(Object [] subInstruments) {
    
    // Remove all existing sub-instruments from table.
    Enumeration subInstNames  = _avTable.attributes(ATTR_SUB_INSTRUMENT);
    while(subInstNames.hasMoreElements()) {
      _avTable.rm((String)subInstNames.nextElement());
    }

    // Add new sub-instruments.
    for(int i = 0; i < subInstruments.length; i++) {
      _avTable.set(ATTR_SUB_INSTRUMENT + "#" + i, (String)subInstruments[i]);
    }
  }


  public String getPrimarySubInstrument() {
    return _avTable.get(ATTR_PRIMARY_SUB_INSTRUMENT);
  }

  public void setPrimarySubInstrument(String primarySubInstrument) {
    _avTable.set(ATTR_PRIMARY_SUB_INSTRUMENT, primarySubInstrument);
  }

  /**
   * @return Explict bolometer (A1, A2 etc) or null if no specific bolometer is set.
   */
  public String getBolometer() {
    return _avTable.get(ATTR_BOLOMETER);
  }

  /**
   * @param bolometer Explict bolometer (A1, A2 etc) or null if no specific bolometer should be used.
   */
  public void setBolometer(String bolometer) {
    if(bolometer == null) {
      _avTable.rm(ATTR_BOLOMETER);
    }
    else {
      _avTable.set(ATTR_BOLOMETER, bolometer);
    }  
  }

  /**
   * @return Radius of circular SCUBA science area in arcsecs
             (as single element of an array of doubles)
   *
   * @see #SCIENCE_AREA_RADIUS
   */
  public double[] getScienceArea() {
    return new double[] { 2.3 * 60.0 }; 
  }
}
