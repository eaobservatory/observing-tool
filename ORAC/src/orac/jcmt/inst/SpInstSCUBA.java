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

  /** Used in scuba.cfg. "FILTERS" */
  public static final String FILTERS_TAG         = "FILTERS";

  /** Used in scuba.cfg. "JIGGLE_PATTERNS" */
  public static final String JIGGLE_PATTERNS_TAG = "JIGGLE_PATTERNS";

  /** Used in scuba.cfg. "CHOP_FREQUENCY" */
  public static final String CHOP_FREQUENCY_TAG  = "CHOP_FREQUENCY";

  /** Used in scuba.cfg. "DEFAULT_SCAN_DX" */
  public static final String DEFAULT_SCAN_DX_TAG = "DEFAULT_SCAN_DX";

  /** Used in scuba.cfg. "DEFAULT_SCAN_DY" */
  public static final String DEFAULT_SCAN_DY_TAG = "DEFAULT_SCAN_DY";


  /**
   * SCUBA chop frequency (8Hz).
   *
   * This can be overridden by the entry CHOP_FREQUENCY in the scuba.cfg file.
   *
   * @see #getChopFrequency()
   */
  private static double _chopFrequency = 8.0;

  /**
   * @see #getDefaultScanVelocity()
   */
  private static double _defaultScanDx = 3.0;

  /**
   * @see #getDefaultScanDy
   */
  private static double _defaultScanDy = 60.0;


  private static String [][] _filters            = null;
  private static Hashtable   _filterTable        = new Hashtable();
  private static String [][] _jigglePatterns     = null;
  private static Hashtable   _jigglePatternTable = new Hashtable();
  private static String [] _defaultJigglePattern = { "DEFAULT" };

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


    for(int i = 0; i < (_filters.length - 1); i += 2) {
      // Use lower case keywords for filter table to allow for
      // case insensitive look-up later.
      _filterTable.put(_filters[i][0].toUpperCase(), _filters[i + 1]);
    }

    for(int i = 0; i < (_jigglePatterns.length - 1); i += 2) {
      // Use lower case keywords for filter table to allow for
      // case insensitive look-up later.
      _jigglePatternTable.put(_jigglePatterns[i][0].toUpperCase(), _jigglePatterns[i + 1]);
    }

    try {
      _avTable.noNotifySet(ATTR_FILTER,                 _filters[0][0], 0);
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
        if ( InstCfg.matchAttr( instInfo, FILTERS_TAG ) ) {
          _filters = instInfo.getValueAs2DArray();
        }
	
	if ( InstCfg.matchAttr( instInfo, JIGGLE_PATTERNS_TAG )) {
          _jigglePatterns = instInfo.getValueAs2DArray();
	}

	if ( InstCfg.matchAttr( instInfo, CHOP_FREQUENCY_TAG )) {
          try {
	    _chopFrequency = Double.parseDouble(instInfo.getValue());
	  }
	  catch(Exception e) {
            System.out.println("Error reading SCUBA chop frequency from scuba cfg file.\n" +
	                       "Using default chop frequency " + _chopFrequency);
	  }
	}

	if ( InstCfg.matchAttr( instInfo, DEFAULT_SCAN_DX_TAG )) {
          try {
	    _defaultScanDx = Double.parseDouble(instInfo.getValue());
	  }
	  catch(Exception e) {
            System.out.println("Error reading SCUBA default scan dx from scuba cfg file.\n" +
	                       "Using default scan dx " + _defaultScanDx);
	  }
	}

	if ( InstCfg.matchAttr( instInfo, DEFAULT_SCAN_DY_TAG )) {
          try {
	    _defaultScanDy = Double.parseDouble(instInfo.getValue());
	  }
	  catch(Exception e) {
            System.out.println("Error reading SCUBA default scan dy from scuba cfg file.\n" +
	                       "Using default scan dy " + _defaultScanDy);
	  }
	}
      }
    }
    catch (IOException e) {
      System.out.println ("Error reading SCUBA cfg file \"" + filename + "\"");
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
   * @return String array of bolometer or null if the specified filter does not exist.
   */
  public static String [] getBolometersFor(String filter) {
    return (String[])_filterTable.get(filter.toUpperCase());
  }


  /**
   * Get jiggle pattern options for a given sub-instrument.
   *
   * @return String array of jiggle pattern options or null if the specified sub-instrument does not exist.
   */
  public static String [] getJigglePatternsForSubInstrument(String subInstrument) {
    return (String[])_jigglePatternTable.get(subInstrument.toUpperCase());
  }

  /**
   * Get jiggle pattern options for the current sub-instrument.
   *
   * @return String array of jiggle pattern options.
   */
  public String [] getJigglePatterns() {
    String [] result = _defaultJigglePattern;

    if(getPrimaryBolometer() != null) {
      result = (String[])_jigglePatternTable.get(getPrimaryBolometer().toUpperCase());
    }

    if(result == null) {
      return _defaultJigglePattern;
    }
    // Note that result will be null if getPrimaryBolometer() == null.
    else {
      return result;
    }
  }


  public String getFilter() {
    return _avTable.get(ATTR_FILTER);
  }

  public void setFilter(String filter) {
    _avTable.set(ATTR_FILTER, filter);
  }

  public Vector getBolometers() {
    return _avTable.getAll(ATTR_BOLOMETERS);
  }


  /**
   * @param subInstrument Object[] rather then String[] is used to simplify GUI.
   */
  public void setBolometers(Vector bolometers) {
    _avTable.setAll(ATTR_BOLOMETERS, bolometers);
  }


  public String getPrimaryBolometer() {
    return _avTable.get(ATTR_PRIMARY_BOLOMETER);
  }

  public void setPrimaryBolometer(String primaryBolometer) {
    _avTable.set(ATTR_PRIMARY_BOLOMETER, primaryBolometer);
  }


  /**
   * @return Radius of circular SCUBA science area in arcsecs
             (as single element of an array of doubles)
   *
   * @see #SCIENCE_AREA_RADIUS
   */
  public double[] getScienceArea() {
    // diameter = 2.3 arcmin => radius = 2.3 * 30 arcsecs
    return new double[] { 2.3 * 30.0 }; 
  }

  /**
   * SCUBA chop frequency.
   *
   * @return 8Hz unless a different value is specified in the scuba.cfg file
   */
  public double getChopFrequency() {
    return _chopFrequency;
  }

  /**
   * Default scan dx for SCUBA.
   *
   * Used for SCUBA SCANs.
   *
   * The default value is 3.0. This can be changed in the scuba.cfg file.
   */
  public double getDefaultScanVelocity() {
    return _chopFrequency * _defaultScanDx;
  }

  /**
   * Default scan dy for SCUBA.
   *
   * Used for SCUBA SCANs.
   *
   * The default value is 60.0. This can be changed in the scuba.cfg file.
   */
  public double getDefaultScanDy() {
      return _defaultScanDy;
  }
}
