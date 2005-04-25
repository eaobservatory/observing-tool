/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.jcmt.util;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpItem;
import gemini.sp.SpObs;
import gemini.sp.SpTreeMan;
import gemini.sp.SpTelescopePos;
import gemini.sp.iter.SpIterFolder;
import gemini.sp.iter.SpIterComp;
import gemini.sp.iter.SpIterRepeat;
import gemini.sp.iter.SpIterChop;
import gemini.sp.iter.SpIterOffset;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.util.Format;
import orac.jcmt.iter.SpIterJCMTObs;
import orac.jcmt.iter.SpIterFocusObs;
import orac.jcmt.iter.SpIterJiggleObs;
import orac.jcmt.iter.SpIterRasterObs;
import orac.jcmt.iter.SpIterStareObs;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.inst.SpDRRecipe;
import orac.util.SpItemUtilities;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * <a name="class_documentation">
 * Translator class that creates ACSIS/OCS XML from Heterodyne instrument component.
 *
 * This class will probably only be used for testing and will probably only cover
 * certain standard observations (first day recipes).<p>
 *
 * The only reason why this class is a extends {@link orac.jcmt.inst.SpInstHeterodyne}
 * is that this make it easy to re-use the code which was previously in
 * {@link orac.jcmt.inst.SpInstHeterodyne} without having to make any changes.<p>
 *
 * The AcsisTranslator class is used as follows: The super class SpInstHeterodyne is used in the
 * ScienceProgram. Whenever the content of a SpInstHeterodyne item is supposed to be translated
 * an AcsisTranslator object is given the same settings as the SpInstHeterodyne item by using
 * the {@link #setSpInstHeterodyne(SpInstHeterodyne)} method. This sets the SpAvTable of the
 * AcsisTranslator object to a copy of the SpAvTable of the SpInstHeterodyne item.
 * The various methods in AcsisTranslator that create ACSIS/OCS XML can then be used
 * to obtain a translation of the SpInstHeterodyne item.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class AcsisTranslator extends SpInstHeterodyne {

  public static final String OBS_MODE_FOCUS_CHOP           = "focus_chop";
  public static final String OBS_MODE_FOCUS                = "focus";
  public static final String OBS_MODE_GRID_PSSW            = "grid_pssw";
  public static final String OBS_MODE_JIGGLE_CHOP          = "jiggle_chop";
  public static final String OBS_MODE_JIGGLE_FREQSW        = "jiggle_freqsw";
  public static final String OBS_MODE_POINTING_JIGGLE_CHOP = "pointing_jiggle_chop";
  public static final String OBS_MODE_RASTER_PSSW          = "raster_pssw";
  public static final String OBS_MODE_UNRECOGNIZED         = "unrecognized";

  /**
   * Path to the pre-installed ACSIS/OCS XML entities which are
   * not created by the OT.
   *
   * Set in acsisTranslator.cfg file.
   */
  public static String OCS_XML_ROOT_PATH;

  /** Top level XML file path. Written by OT. Path set in acsisTranslator.cfg file. */
  public static String OCS_CONFIG_FILE;

  /** Top level DTD URL. Can be changed in acsisTranslator.cfg file. */
  public static String OCS_DTD_URL = "http://www.jach.hawaii.edu/JACdocs/JCMT/OCS/ICD/001/ocs.dtd";

  /** Entity file path. Written by OT. Path set in acsisTranslator.cfg file. */
  public static String TCS_CONFIG_FILE;

  /** Entity file path. Written by OT. Path set in acsisTranslator.cfg file. */
  public static String FRONTEND_CONFIG_FILE;

  /** Entity file path. Written by OT. Path set in acsisTranslator.cfg file. */
  public static String ACSIS_CONFIG_FILE;

  /** Entity file path. Written by OT. Path set in acsisTranslator.cfg file. */
  public static String LINE_LIST_FILE;

  /** Entity file path. Written by OT. Path set in acsisTranslator.cfg file. */
  public static String SPW_LIST_FILE;

  /** Entity file path. Written by OT. Path set in acsisTranslator.cfg file. */
  public static String ACSIS_IF_FILE;

  /** Entity file path. Written by OT. Path set in acsisTranslator.cfg file. */
  public static String ACSIS_CORR_FILE;

  /** Entity file path. (Currenlty) not written by OT. Path set in acsisTranslator.cfg file. */
  public static String CUBE_LIST_FILE;

  /** Entity file path. Not written by OT. Path set in acsisTranslator.cfg file. */
  public static Hashtable DR_RECIPE_FILES = new Hashtable();
 
  /** Entity file path. Not written by OT. Path set in acsisTranslator.cfg file. */
  public static String HEADER_CONFIG_FILE;

  /** Entity file path. Not written by OT. Path set in acsisTranslator.cfg file. */
  public static String INTERFACE_LIST_FILE;
  
  /** Entity file path. Not written by OT. Path set in acsisTranslator.cfg file. */
  public static String MACHINE_TABLE_FILE;

  /** Entity file path. Not written by OT. Path set in acsisTranslator.cfg file. */
  public static String PROCESS_LAYOUT_FILE;

  /** Entity file path. Not written by OT. Path set in acsisTranslator.cfg file. */
  public static String MONITOR_LAYOUT_FILE;

  /** Entity file path. Not written by OT. Path set in acsisTranslator.cfg file. */
  public static String RT_DISPLAY_FILE;

  /** Entity file path. Not written by OT. Path set in acsisTranslator.cfg file. */
  public static String RTS_CONFIG_FILE;

  /** Entity file path. Not written by OT. Path set in acsisTranslator.cfg file. */
  public static String SIMULATION_FILE;

  /** Table of instrument entity file paths. (Currenlty) not written by OT. Paths set in acsisTranslator.cfg file. */
  public static Hashtable INSTRUMENT_FILES = new Hashtable();


  /** XML version. */
  public static String XML_VERSION = "1.0";
  
  /** XML encoding. */
  public static String XML_ENCODING = "US-ASCII";

  /**
   * XML element containing the Acsis configuration.
   *
   * The XML_ELEMENT_ACSIS_CONFIGURATION String is <tt>&lt;jcmt_config&gt;</tt>.
   */
  private static final String XML_ELEMENT_ACSIS_CONFIGURATION = "jcmt_config";

  private SpItem _parent = null;

  /**
   * LO2 values.
   *
   * lo2 is calculated in {@link #getACSIS_corr()} but used in both getACSIS_corr() and
   * {@link #ACSIS_IF()}. So it is important that getACSIS_IF() is not executed before
   * getACSIS_corr() during translation.
   */
  private double [] lo2 = new double[4];

  /**
   * Rest frequency references.
   *
   * restFreqRefs is calculated in {@link #get_line_list()} but used in both get_line_list() and
   * {@link #ACSIS_corr}. So it is important that getACSIS_corr() is not executed before
   * get_line_list() during translation.
   */
  private String [] restFreqRefs = null;

  /**
   * Configuration label..
   *
   * configLabel is calculated in {@link #get_line_list()} but used in both get_line_list() and
   * {@link #ACSIS_corr}. So it is important that getACSIS_corr() is not executed before
   * get_line_list() during translation.
   */
  private String configLabel = null;

  public AcsisTranslator() {
    // Read in the translator config file.
    String baseDir = System.getProperty( "ot.cfgdir" );
    String cfgFile = baseDir + "acsisTranslator.cfg";
    _readCfgFile( cfgFile );
    System.out.println(getTranslatorConfig());
  }


  /**
   * Translates observe "eyes".
   *
   * Creates observe0, observe1, observe2, ... subdirectories in the directory specified by <tt>dir</tt>.
   * Note that the content of existing observeN dorectories is overridden and that additional
   * observeN directories (if the previous translation had more observe "eyes") remain in place.
   * This can lead to confusion. That's why users should be asked to remove the translation directory
   * <tt>dir</tt> if a directory with that name does already exist.
   */
  public void translate(SpObs spObs, SpInstHeterodyne instHeterodyne, File dir) throws IOException, IllegalStateException {
    String observeDirPrefix = "observe";

    SpIterFolder iterFolder = spObs.getIterFolder();

    if(iterFolder == null) {
      throw new IllegalStateException("Cannot translate Observation without Sequence.");
    }

    Vector translatableItems = findAllItems(iterFolder, SpIterObserveBase.class);

    if(translatableItems.size() < 1) {
      throw new IllegalStateException("There are no observe iterators (\"eyes\").\n" +
                                      "Each such iterator will be translated to a complete set of configuration XML files.");
    }

    File subDir;
    SpItem child;

    int k = 0;
    for(int i = 0; i < translatableItems.size(); i++) {
      child = (SpItem)translatableItems.get(i);

      if(child instanceof SpIterComp) {
        subDir = new File(dir, "observe" + k);
        subDir.mkdirs();

        Runtime.getRuntime().exec("ln -s " + OCS_XML_ROOT_PATH + " " + subDir.getAbsolutePath() + "/ocs_xml_root");

        translate(spObs, instHeterodyne, (SpIterObserveBase)child, subDir.getPath());

        k++;
      }
    }
  }

  /**
   * Creates XML for one Observe (Observation Iterator, "Eye").
   *
   * The index parameter indicates which of the child items of the SpIterFolder (Sequence) is to be translated.
   * If this child item is not itself an Observe (Observation Iterator, "Eye") but a
   * Configuration Iterator (nested "Running Man", not Sequence), then the translator will
   * try to interpret this in some way, i.e. check whether it is a jiggle-chop or raster-chop observation.
   */
  public void translate(SpObs spObs, SpInstHeterodyne instHeterodyne, SpIterObserveBase iterComp, String dir)
                throws IOException, IllegalStateException {

    setSpInstHeterodyne(instHeterodyne);

    if(getBand() == null) {
      throw new IllegalStateException("Heterodyne component has not been edited.");
    }

    write(getOCS_CONFIG(getObserveMode(iterComp)), dir + File.separator + OCS_CONFIG_FILE);
    write(getTCS_CONFIG(spObs, iterComp), dir + File.separator + TCS_CONFIG_FILE);
    write(getFRONTEND_CONFIG(),     dir + File.separator + FRONTEND_CONFIG_FILE);
    write(getACSIS_CONFIG(),        dir + File.separator + ACSIS_CONFIG_FILE);
    write(get_line_list(),          dir + File.separator + LINE_LIST_FILE);
    write(get_spw_list(),           dir + File.separator + SPW_LIST_FILE);
    write(getACSIS_corr(),          dir + File.separator + ACSIS_CORR_FILE);
    write(getACSIS_IF(),            dir + File.separator + ACSIS_IF_FILE);

    // Get DR component to obtain cube_list
    SpDRRecipe spDRRecipe = (SpDRRecipe)SpItemUtilities.findDRRecipe(_parent);

    // Get Target Information component to obtain group_centre of cube
    SpTelescopeObsComp telescopeObsComp = SpTreeMan.findTargetList(_parent);
    SpTelescopePos groupCentre = null;

    if(telescopeObsComp != null) {
      groupCentre = telescopeObsComp.getPosList().getBasePosition();
    }

    // Establish map size
    double mapWidth  = 0.0;
    double mapHeight = 0.0;

    if(iterComp instanceof SpIterRasterObs) {
      mapWidth  = ((SpIterRasterObs)iterComp).getWidth();
      mapHeight = ((SpIterRasterObs)iterComp).getHeight();
    }

    if(spDRRecipe != null) {
      write(spDRRecipe.get_cube_list(groupCentre, mapWidth, mapHeight, ""), dir + File.separator + CUBE_LIST_FILE);
    }
    else {
      write("<cube_list></cube_list>\n", dir + File.separator + CUBE_LIST_FILE);
    }

    // Copy directory with dummy XML entities.
    //
    // This will become obsolete and can be removed if
    // users specify a path for every XML entity
    // that is included in the XML generated by
    // this translator.
    String baseDir = System.getProperty( "ot.cfgdir" );
    String dummyDir = baseDir + "acsisOcsDummies";

    try {
      Runtime.getRuntime().exec("cp -r " + dummyDir + " " + dir);
    }
    catch(IOException e) {
      e.printStackTrace();
      throw new IOException("Could not copy " + dummyDir + " to " + dir + ". " + e);
    }
  }

  /**
   * Sets settings of this class to those of the SpInstHeterodyne item.
   *
   * @see <a href="#class_documentation">Class documentation</a>
   */
  public void setSpInstHeterodyne(SpInstHeterodyne spInstHeterodyne) {
    setTable(spInstHeterodyne.getTable().copy());
    _parent = spInstHeterodyne.parent();
  }

   /**
    *  Read the translator configuration file.
    */
   private void _readCfgFile( String filename ) {

      InstCfgReader instCfg = null;
      InstCfg instInfo = null;
      String block = null;

      instCfg = new InstCfgReader (filename);
      try {
         while ( ( block = instCfg.readBlock() ) != null ) {
            instInfo = new InstCfg( block );
            if ( instInfo.getKeyword().equalsIgnoreCase( "OCS_XML_ROOT_PATH" ) ) {
               OCS_XML_ROOT_PATH = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "OCS_CONFIG_FILE" ) ) {
               OCS_CONFIG_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "OCS_DTD_URL" ) ) {
               OCS_DTD_URL = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "HEADER_CONFIG_FILE" ) ) {
               HEADER_CONFIG_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "TCS_CONFIG_FILE" ) ) {
               TCS_CONFIG_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "INSTRUMENT_FILES" ) ) {
               String [][] instFileArray = instInfo.getValueAs2DArray();

               for(int i = 0; i < instFileArray.length; i++) {
                 INSTRUMENT_FILES.put(instFileArray[i][0], instFileArray[i][1]);
               }
            } else if ( instInfo.getKeyword().equalsIgnoreCase( "FRONTEND_CONFIG_FILE" ) ) {
               FRONTEND_CONFIG_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "RTS_CONFIG_FILE" ) ) {
               RTS_CONFIG_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "ACSIS_CONFIG_FILE" ) ) {
               ACSIS_CONFIG_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "XML_VERSION" ) ) {
               XML_VERSION = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "XML_ENCODING" ) ) {
               XML_ENCODING = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "LINE_LIST_FILE" ) ) {
               LINE_LIST_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "SPW_LIST_FILE" ) ) {
               SPW_LIST_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "ACSIS_IF_FILE" ) ) {
               ACSIS_IF_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "ACSIS_CORR_FILE" ) ) {
               ACSIS_CORR_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "CUBE_LIST_FILE" ) ) {
               CUBE_LIST_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "DR_RECIPE_FILES" ) ) {
               String [][] drRecipeArray = instInfo.getValueAs2DArray();

               // It is important that the drRecipeArray[i][0] read form the
	       // cfg file have the same values as OBS_MODE_ constants
	       // in this class.
               for(int i = 0; i < drRecipeArray.length; i++) {
                 DR_RECIPE_FILES.put(drRecipeArray[i][0], drRecipeArray[i][1]);
               }

               DR_RECIPE_FILES.put(OBS_MODE_UNRECOGNIZED, "acsisOcsDummies/dr_recipe.ent");

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "INTERFACE_LIST_FILE" ) ) {
               INTERFACE_LIST_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "MACHINE_TABLE_FILE" ) ) {
               MACHINE_TABLE_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "PROCESS_LAYOUT_FILE" ) ) {
               PROCESS_LAYOUT_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "MONITOR_LAYOUT_FILE" ) ) {
               MONITOR_LAYOUT_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "RT_DISPLAY_FILE" ) ) {
               RT_DISPLAY_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "RTS_CONFIG_FILE" ) ) {
               RTS_CONFIG_FILE = instInfo.getValue();

            } else if ( instInfo.getKeyword().equalsIgnoreCase( "SIMULATION_FILE" ) ) {
               SIMULATION_FILE = instInfo.getValue();

            }
         }
      } catch ( IOException e ) {
         e.printStackTrace();
         System.out.println( "Error reading ACSIS translator cfg file." );
      } catch ( Exception e ) {
         e.printStackTrace();
         System.out.println( "Error parsing ACSIS translator cfg file." );
      }
   }

  /**
   * This is used to print out some information about which
   * configuration files (entities) are used.
   */
  public String getTranslatorConfig() {
    StringBuffer result = new StringBuffer();

    result.append("XML_VERSION          = " + XML_VERSION + "\n");
    result.append("XML_ENCODING         = " + XML_ENCODING + "\n");
    result.append("OCS_DTD_URL          = " + OCS_DTD_URL + "\n");
    result.append("OCS_CONFIG_FILE      = " + OCS_CONFIG_FILE + "\n");
    result.append("HEADER_CONFIG_FILE   = " + HEADER_CONFIG_FILE + "\n");
    result.append("TCS_CONFIG_FILE      = " + TCS_CONFIG_FILE + "\n");

    Enumeration keys = INSTRUMENT_FILES.keys();
    String inst;
    result.append("INSTRUMENT_FILES:\n");
    while(keys.hasMoreElements()) {
      inst = (String)keys.nextElement();
      result.append("  " + inst + " = " + INSTRUMENT_FILES.get(inst) + "\n");
    }

    result.append("FRONTEND_CONFIG_FILE = " + FRONTEND_CONFIG_FILE + "\n");
    result.append("RTS_CONFIG_FILE      = " + RTS_CONFIG_FILE + "\n");
    result.append("ACSIS_CONFIG_FILE    = " + ACSIS_CONFIG_FILE + "\n");
    result.append("LINE_LIST_FILE       = " + LINE_LIST_FILE + "\n");
    result.append("SPW_LIST_FILE        = " + SPW_LIST_FILE + "\n");
    result.append("ACSIS_IF_FILE        = " + ACSIS_IF_FILE + "\n");
    result.append("ACSIS_CORR_FILE      = " + ACSIS_CORR_FILE + "\n");
    result.append("CUBE_LIST_FILE       = " + CUBE_LIST_FILE + "\n");

    keys = DR_RECIPE_FILES.keys();
    String drRecipe;
    result.append("DR_RECIPE_FILES:\n");
    while(keys.hasMoreElements()) {
      drRecipe = (String)keys.nextElement();
      result.append("  " + drRecipe + " = " + DR_RECIPE_FILES.get(drRecipe) + "\n");
    }

    result.append("HEADER_CONFIG_FILE   = " + HEADER_CONFIG_FILE + "\n");
    result.append("INTERFACE_LIST_FILE  = " + INTERFACE_LIST_FILE + "\n");
    result.append("MACHINE_TABLE_FILE   = " + MACHINE_TABLE_FILE + "\n");
    result.append("PROCESS_LAYOUT_FILE  = " + PROCESS_LAYOUT_FILE + "\n");
    result.append("MONITOR_LAYOUT_FILE  = " + MONITOR_LAYOUT_FILE + "\n");
    result.append("RT_DISPLAY_FILE      = " + RT_DISPLAY_FILE + "\n");
    result.append("RTS_CONFIG_FILE      = " + RTS_CONFIG_FILE + "\n");
    result.append("SIMULATION_FILE      = " + SIMULATION_FILE + "\n");

    return result.toString();
  }

  /**
   * Returns OCS_CONFIG XML element as defined in
   * <a href="http://www.jach.hawaii.edu/JACdocs/JCMT/OCS/ICD/001"/>
   * JCMT Observatory Control System Interfaces</a>.
   */
  public String getOCS_CONFIG(String drRecipe) {
    return "<?xml version=\"" + XML_VERSION + "\" encoding=\"" + XML_ENCODING + "\"?>\n" +
           "<!DOCTYPE OCS_CONFIG SYSTEM \"" + OCS_DTD_URL + "\" [\n" +
           "<!ENTITY header_config   SYSTEM \"" + HEADER_CONFIG_FILE         + "\">\n" +
           "<!ENTITY tcs_config      SYSTEM \"" + TCS_CONFIG_FILE            + "\">\n" +
           "<!ENTITY instrument      SYSTEM \"" + INSTRUMENT_FILES.get(getFrontEnd()) + "\">\n" +
           "<!ENTITY frontend_config SYSTEM \"" + FRONTEND_CONFIG_FILE       + "\">\n" +
           "<!ENTITY rts_config      SYSTEM \"" + RTS_CONFIG_FILE            + "\">\n" +
           "<!ENTITY acsis_config    SYSTEM \"" + ACSIS_CONFIG_FILE          + "\">\n" +
           "<!ENTITY line_list       SYSTEM \"" + LINE_LIST_FILE + "\">\n" +
           "<!ENTITY cube_list       SYSTEM \"" + CUBE_LIST_FILE + "\">\n" +
           "<!ENTITY spw_list        SYSTEM \"" + SPW_LIST_FILE + "\">\n" +
           "<!ENTITY acsis_corr      SYSTEM \"" + ACSIS_CORR_FILE + "\">\n" +
           "<!ENTITY acsis_if        SYSTEM \"" + ACSIS_IF_FILE + "\">\n" +
           "<!ENTITY dr_recipe       SYSTEM \"" + DR_RECIPE_FILES.get(drRecipe) + "\">\n" +
           "<!ENTITY rt_display      SYSTEM \"" + RT_DISPLAY_FILE + "\">\n" +
           "<!ENTITY process_layout  SYSTEM \"" + PROCESS_LAYOUT_FILE + "\">\n" +
           "<!ENTITY monitor_layout  SYSTEM \"" + MONITOR_LAYOUT_FILE + "\">\n" +
           "<!ENTITY machine_table   SYSTEM \"" + MACHINE_TABLE_FILE + "\">\n" +
           "<!ENTITY simulation      SYSTEM \"" + SIMULATION_FILE + "\">\n" +
           "<!ENTITY interface_list  SYSTEM \"" + INTERFACE_LIST_FILE + "\">\n" +
           "]>\n" +
           "\n"   +
           "\n"   +
           "<OCS_CONFIG>\n" +
           "\n" +
           "  &header_config;\n" +
           "\n" +
           "  &tcs_config;\n" +
           "\n" +
           "  &instrument;\n" +
           "\n" +
           "  &frontend_config;\n" +
           "\n" +
           "  &rts_config;\n" +
           "\n" +
           "  &acsis_config;\n" +
           "\n" +
           "</OCS_CONFIG>";
  }

  /**
   * Returns TCS_CONFIG XML element as defined in
   * <a href="http://www.jach.hawaii.edu/JACdocs/JCMT/OCS/ICD/006">
   * JCMT TCS/OCS Interfaces</a>.
   *
   * Currently the XML of the
   * Target Information Component ({@link gemini.sp.obsComp.SpTelescopeObsComp}), the
   * Offset Iterator ({@link gemini.sp.iter.SpIterOffset}) and the
   * Scan/Raster Iterator ({@link orac.jcmt.iter.SpIterRasterObs}) is used.<p>
   *
   * JIGGLE and CHOP are still mising.
   */
  public String getTCS_CONFIG(SpObs spObs, SpIterComp iterComp) {

    // Get the target in scope
    SpTelescopeObsComp telescopeObsComp = SpTreeMan.findTargetList(spObs);

    StringBuffer xmlBuffer = new StringBuffer();

    // No target component. Maybe this should be treated as an error.
    // Ignore it for now. There might still be jiggle, chop, scan map
    // and offset information to go into the TCS_CONFIG element.
    if(telescopeObsComp != null) {
      xmlBuffer.append(getTcsXml(telescopeObsComp) + "\n");
    }

    // Build up <obsArea> element
    StringBuffer obsAreaBuffer = new StringBuffer();

    // Make sure <PA> element is included only once
    boolean ignorePA = false;

    // Check for offsets
    SpIterOffset iterOffset = (SpIterOffset)getAncesterInSequence(iterComp, SpIterOffset.class);

    if(iterOffset != null) {
      obsAreaBuffer.append(getTcsXml((SpIterOffset)iterOffset, ignorePA) + "\n");
      ignorePA = true;
    }

    // Check whether this is a raster maps
    if(iterComp instanceof SpIterRasterObs) {
      obsAreaBuffer.append(getTcsXml((SpIterRasterObs)iterComp, ignorePA) + "\n");
      ignorePA = true;
    }

    if(obsAreaBuffer.length() > 0) {
      xmlBuffer.append("  <obsArea>\n");
      xmlBuffer.append(obsAreaBuffer.toString());
      xmlBuffer.append("  </obsArea>\n\n");
    }

    // Deal with <SECONDARY> element.
    String jiggleElement = null;
    String chopElement   = null;
    String timingElement = null;

    // Check whether this is a jiggle iterator
    if(iterComp instanceof SpIterJiggleObs) {

      String [] jiggleAndTiming = getTcsXml((SpIterJiggleObs)iterComp);

      xmlBuffer.append("  <SECONDARY>\n");

      // Check for chop iterator
      SpIterChop iterChop = (SpIterChop)getAncesterInSequence(iterComp, SpIterChop.class);
      if(iterChop != null) {
        xmlBuffer.append("  <JIGGLE_CHOP>\n");

        // Add <JIGGLE>
        xmlBuffer.append(jiggleAndTiming[0] + "\n");

        // Add <CHOP> (use ignorePA = false)
	xmlBuffer.append(getTcsXml(iterChop));

        // Add <TIMING>
        xmlBuffer.append(jiggleAndTiming[1] + "\n");

        xmlBuffer.append("  </JIGGLE_CHOP>\n");
      }
      else {
        // Add <JIGGLE>
        xmlBuffer.append(jiggleAndTiming[0] + "\n");

        // Add <TIMING>
        xmlBuffer.append(jiggleAndTiming[1] + "\n");
      }

      xmlBuffer.append("  </SECONDARY>\n");
    }

    return
      "<TCS_CONFIG>\n" +
      xmlBuffer.toString() +
      "</TCS_CONFIG>";
  }

   /**
   * Returns FRONTEND_CONFIG XML element as defined in
   * <a href="http://www.jach.hawaii.edu/JACdocs/JCMT/OCS/ICD/004"/>
   * JCMT Frontend OCS Interfaces</a>.
   */
 public String getFRONTEND_CONFIG() {

    double freq_off_scale = 1.0; // MFO ???

    return
      "<FRONTEND_CONFIG>\n" +
      "\n" +
      "   <REST_FREQUENCY>" + getRestFrequency(0) + "</REST_FREQUENCY>\n" +
      "   <FREQ_OFF_SCALE>" + freq_off_scale + "</FREQ_OFF_SCALE>\n" +
      "   <SIDEBAND>" + getBand().toUpperCase() + "</SIDEBAND>\n" +  // MFO ???: Is "BEST" allowed?
      "   <SB_MODE>"  + getMode().toUpperCase() + "</SB_MODE>\n" +
      "   <DOPPLER_TRACK MECH_TUNING=\"ONCE\" ELEC_TUNING=\"CONTINUOUS\"/>\n" +
      "   <OPTIMIZE>DISABLE</OPTIMIZE>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H00\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H01\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H02\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H03\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H04\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H05\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H06\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H07\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H08\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H09\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H10\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H11\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H12\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H13\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H14\" VALUE=\"ON\"/>\n" +
      "   <RECEPTOR_MASK RECEPTOR_ID=\"H15\" VALUE=\"ON\"/>\n" +
      "\n" +
      "</FRONTEND_CONFIG>\n";
  }

  /**
  /**
   * Returns ACSIS_CONFIG XML element as defined in
   * <a href="http://www.jach.hawaii.edu/JACdocs/JCMT/OCS/ICD/005"/>
   * ACSIS Hardware and Software Interfaces</a>.
   */
  public String getACSIS_CONFIG() {
    return
      "<ACSIS_CONFIG>\n" + 
      "\n" +
      "    &line_list;\n" +
      "\n" +
      "    &cube_list;\n" +
      "\n" +
      "    &spw_list;\n" +
      "\n" +
      "    &acsis_if;\n" +
      "\n" +
      "    &acsis_corr;\n" +
      "\n" +
      "    &dr_recipe;\n" +
      "\n" +
      "    &rt_display;\n" +
      "\n" +
      "    &process_layout;\n" +
      "\n" +
      "    &machine_table;\n" +
      "\n" +
      "    &interface_list;\n" +
      "\n" +
      "    &simulation;\n" +
      "\n" +
      "</ACSIS_CONFIG>\n";
  }

  /**
   * Returns line list XML element.
   *
   * Execute methods in the following order:
   * <pre>
   *   get_line_list();
   *   get_spw_list();
   *   getACSIS_corr();
   *   getACSIS_IF();
   * </pre>
   */
  public String get_line_list() {

    String indent = "";
    StringBuffer xmlBuffer = new StringBuffer();

    // Line list
    restFreqRefs = new String[getNumSubSystems()];
    String transition  = null;

    xmlBuffer.append(indent + "<line_list>\n");

    for(int i = 0; i < getNumSubSystems(); i++) {
      transition = getTransition(i);

      if((transition != null) && (transition.trim().length() > 0)) {
        restFreqRefs[i] = "" + getMolecule(i) + " " + transition;
      }
      else {
        restFreqRefs[i] = "restFrequency" + i;
      }

      xmlBuffer.append(indent + "<rest_frequency id=\"" + restFreqRefs[i] + "\" units=\"GHz\">" +
                       (getRestFrequency(i) / 1.0E9) + "</rest_frequency>\n");
    }
    
    xmlBuffer.append(indent + "</line_list>\n\n");

    return xmlBuffer.toString();
  }

  /**
   * Returns spectral window list XML element.
   *
   * Something needed by this method is calculated in {@link #get_line_list()} but used in both get_line_list() and
   * get_spw_list. So it is important that get_spw_list() is not executed before
   * get_line_list() during translation.<p>
   *
   * Execute methods in the following order:
   * <pre>
   *   get_line_list();
   *   get_spw_list();
   *   getACSIS_corr();
   *   getACSIS_IF();
   * </pre>
   */
  public String get_spw_list() {
    String sidebandString  = getBand();

    int sideband = 1; // usb

    if(sidebandString != null && sidebandString.equals("lsb")) {
      sideband = -1;
    }

    String indent = "";
    StringBuffer xmlBuffer = new StringBuffer();

    // Acsis spectral windows list
    xmlBuffer.append(
      indent + "<spw_list>\n" +
      indent + "  <doppler_field ref=\"TCS.RV.DOPPLER???\"/>\n" +
      indent + "  <spw_id_field ref=\"SPECTRAL_WINDOW_ID???\"/>\n" +
      indent + "  <fe_lo_field ref=\"FE.STATE.LO_FREQUENCY???\"/>\n"
    );

    // Spectral windows
    for(int i = 0; i < getNumSubSystems(); i++) {
       xmlBuffer.append(subSystemToXML(restFreqRefs[i], sideband, indent + "    ", i) + "\n");
    }

    xmlBuffer.append(indent + "</spw_list>\n");

    return xmlBuffer.toString();
  }

  /**
   * ACSIS_corr.
   *
   * Something needed by this method is calculated in {@link #get_line_list()} but used in both get_line_list() and
   * ACSIS_corr. So it is important that getACSIS_corr() is not executed before
   * get_line_list() during translation.<p>
   *
   * Execute methods in the following order:
   * <pre>
   *   get_line_list();
   *   get_spw_list();
   *   getACSIS_corr();
   *   getACSIS_IF();
   * </pre>
   */
  public String getACSIS_corr() {
    String indent = "";
    StringBuffer xmlBuffer = new StringBuffer();

    // ---- Prepare lo2 and spw attributes accoring to LO2 communality (Needed for Correlator and IF XML) ------

    // Create a temporary array of all subband centres of length 4.
    // (At the moment there is a maximum of 4 subbands across all subsystems. This might change later.)
    double [] allSubBandCentresTemp = new double[4];
    double [] subBandCentres = null;

    // Imitate the lo2 communality for the spw attributes for the correlator module XML
    // spwAttrFour contains has length 4 like lo2
    String [] spwAttrFour = new String[4];

    String [] spwAttrTemp = new String[4];

    int k = 0;
    for(int i = 0; i < getNumSubSystems(); i++) {
      subBandCentres = getSubBandCentreFrequencies(i);

      for(int j = 0; j < subBandCentres.length; j++) {
        allSubBandCentresTemp[k] = subBandCentres[j];

        spwAttrTemp[k] = "SPW" + (i + 1);
        if(subBandCentres.length > 1) {
          spwAttrTemp[k] += "." + (j + 1);
        }

        k++;

        if(k >= allSubBandCentresTemp.length) {
          break;
        }
      }

      if(k >= allSubBandCentresTemp.length) {
        break;
      }
    }


    configLabel = getConfigLabel();

    // Special case: configuration 11HM, 3 subbands in feed,
    // 1 sybsystem with 1 "hybrid subband", 1 subsystem with 2 hybrid subbands.
    // One of the subband centres will have to be duplicated in order to
    // get 4 lo2 values.
    if(configLabel.trim().toUpperCase().equals("11HM")) {

      if(getNumHybridSubBands(0) == 1) {

        // Shift subband centres 2 and 1 up by one index and duplicate
        // subband centre 0 by copying it to index 1.
        // This will result in lo2 communality 1=2, 3, 4
        allSubBandCentresTemp[3] = allSubBandCentresTemp[2];
        allSubBandCentresTemp[2] = allSubBandCentresTemp[1];
        allSubBandCentresTemp[1] = allSubBandCentresTemp[0];

        spwAttrTemp[3] = spwAttrTemp[2];
        spwAttrTemp[2] = spwAttrTemp[1];
        spwAttrTemp[1] = spwAttrTemp[0];
      }
      else {
        // Duplicate subband centre 2 by copying it to subband centre 3.
	// This will result in lo2 communality 1, 2, 3=4
	allSubBandCentresTemp[3] = allSubBandCentresTemp[2];

	spwAttrTemp[3] = spwAttrTemp[2];
      }

      // k should be 3 at this point (3 subbands per feed in configuration 11HM)
      // Increment k to 4 to reflect the duplication above.
      k++;
    }

    // Create an array of all subband centres by copying the first k elements from the temporary array
    // where k is the total number of subbands (across all subsystems). At the moment that is at most 4.
    double [] allSubBandCentres = new double[k];
    System.arraycopy(allSubBandCentresTemp, 0, allSubBandCentres, 0, k);

    String [] spwAttr = new String[k];
    System.arraycopy(spwAttrTemp, 0, spwAttr, 0, k);

    for(int i = 0; i < lo2.length; i++) {
      // Calculate 4 lo2
      lo2[i] = allSubBandCentres[i % allSubBandCentres.length] + 2.5E9;

      // Rounnd to 500Hz
      lo2[i] = Math.rint(lo2[i] / 500) * 500;

      spwAttrFour[i] = spwAttr[i % spwAttr.length];
    }

    // Reshuffle order if frontend is not HARP-B
    // and if the total number of subbands per feed is less than 4
    // allSubBandCentres.length is the total number of subbands per feed.
    if((!getFrontEnd().trim().toUpperCase().startsWith("HARP")) && (allSubBandCentres.length < 4)) {
      double tempLo2_1 = lo2[1];
      lo2[1] = lo2[2];
      lo2[2] = tempLo2_1;

      String tempSpwAttr_1 = spwAttrFour[1];
      spwAttrFour[1] = spwAttrFour[2];
      spwAttrFour[2] = tempSpwAttr_1;
    }


    // -------------------- Correlator Configuration --------------------------

    xmlBuffer.append(indent + "<ACSIS_corr>\n");

    int numSubSystems = getNumSubSystems();
    String spw     = "To be implemented";

    String bw_mode;
    double bw_MHz;

    for(int i = 0; i < 32; i++) {
      bw_MHz = getIndividualSubBandWidth(i % numSubSystems) / 1.0E6;

      if(bw_MHz >= 1000.0) {
        bw_mode = "" + ((int)(bw_MHz / 1000.0)) + "GHz";
      }
      else {
        bw_mode = "" + ((int)bw_MHz)            + "MHz";
      }

      bw_mode += "x" + getIndividualSubBandChannels(i % numSubSystems);

      xmlBuffer.append(indent + "  <cm id=\"" + (i + 1) +
        "\" bw_mode=\"" + bw_mode + "\"/>\n");
    }

    xmlBuffer.append(indent + "  <rts_parms int_interval=\"50\" timing_src=\"RTS_SOFT\" />\n");

    xmlBuffer.append(indent + "</ACSIS_corr>\n");

    return xmlBuffer.toString();
  }

  /**
   * Must be executed after getACSIS_corr().
   *
   * The lo2 values that are used in this method are calculated in {@link ACSIS_corr()}.
   * Therefore it is important that ACSIS_IF() is not executed before
   * ACSIS_corr() during translation.<p>
   *
   * Execute methods in the following order:
   * <pre>
   *   get_line_list();
   *   get_spw_list();
   *   getACSIS_corr();
   *   getACSIS_IF();
   * </pre>
   */
  public String getACSIS_IF() {

    String indent = "";
    StringBuffer xmlBuffer = new StringBuffer();

    int numSubSystems = getNumSubSystems();

    xmlBuffer.append(indent + "<ACSIS_IF>\n");

    String bw_mode;

    for(int i = 0; i < 32; i++) {
      bw_mode = "" + ((int)(getIndividualSubBandWidth(i % numSubSystems) / 1.0E6)) + "MHz";

      xmlBuffer.append(indent + "  <dcm id=\"" + (i + 1) +
        "\" bw_mode=\"" + bw_mode + "\"/>\n");
    }


    xmlBuffer.append(indent + "  <lo2>\n");
    for(int i = 0; i < lo2.length; i++) {
      xmlBuffer.append(indent + "    <freq units=\"MHz\">" + (lo2[i] / 1.0E6) + "</freq>\n");
    }
    xmlBuffer.append(indent + "  </lo2>\n");

    xmlBuffer.append(indent + "  <lo3 freq=\"???\"/>\n");
    xmlBuffer.append(indent + "\n");
    xmlBuffer.append(indent + "   <quadrant id=\"0\" subband_mode=\"2222\" />\n");
    xmlBuffer.append(indent + "\n");
    xmlBuffer.append(indent + "   <cass_sw_det input=\"0\" rcvr=\"rxa3\" rxout=\"0\" nominal_pwr=\"-40\"/>\n");
    xmlBuffer.append(indent + "\n");
    xmlBuffer.append(indent + "   <ns_sw_det input=\"0\" rcvr=\"harp\" rxout=\"0\" nominal_pwr=\"-40\"/>\n");
    xmlBuffer.append(indent + "\n");
    xmlBuffer.append(indent + "   <bite_parms load_mode=\"OFF\" line_mode=\"OFF\" />\n");
    xmlBuffer.append(indent + "\n");
    xmlBuffer.append(indent + "   <rts_parms int_interval=\"50\" timing_src=\"RTS_SOFT\" />\n");
    xmlBuffer.append(indent + "\n");

    xmlBuffer.append(indent + "</ACSIS_IF>\n");

    return xmlBuffer.toString();
  }


  /**
   * Creates XML from a subsystem (for ACSIS data reduction).
   *
   * A subsystem with <i>n</i> hybrid subbands (<i>n &gt; 1</i>) results in <i>n + 1</i>
   * &lt;spectral_window&gt; elements in the XML. One representing the subsystem as a
   * whole and one for each individual hybrid subband.<p>
   *
   * If the subsystem does not contain multiple hybrid subbands then it will result in
   * only one &lt;spectral_window&gt; element.
   *
   * @see spectralWindowToXML(java.lang.String,int,java.lang.String,java.lang.String,int)
   */
  public String subSystemToXML(String restFrequencyId,
			       int sideband,
			       String indent,
			       int subsystem) {

    StringBuffer xmlBuffer = new StringBuffer();

    StringBuffer subSystemXmlBuffer = new StringBuffer();
    String idStringBase = "SPW" + (subsystem + 1) + ".";

    if(getNumHybridSubBands(subsystem) > 1) {
      subSystemXmlBuffer.append(indent + "  <subband_list>\n");
      for(int i = 0; i < getNumHybridSubBands(subsystem); i++) {
        subSystemXmlBuffer.append(indent + "    <subband ref=\"" + idStringBase + (i + 1) + "\"/>\n");
      }
      subSystemXmlBuffer.append(indent + "  </subband_list>\n");
    }

    xmlBuffer.append(spectralWindowToXML(restFrequencyId, sideband, subSystemXmlBuffer.toString(), indent, subsystem, SUBSYSTEM_INDEX));

    // Spectral windows for individual subbands
    if(getNumHybridSubBands(subsystem) > 1) {
      for(int i = 0; i < getNumHybridSubBands(subsystem); i++) {
        xmlBuffer.append(spectralWindowToXML(restFrequencyId, sideband, "", indent, subsystem, i));
      }
    }

    return xmlBuffer.toString();
  }

  /**
   * Creates XML for spectral windows (AXSIS DR).
   *
   * In hybrid mode each individual subband gets its own &lt;spectral_window&gt; element
   * Additionally the combined subsystem gets a &lt;spectral_window&gt; element as well.
   * This method is used for both (see parameter hybridSubBand)
   *
   *
   * <h3>Reference channel (reference pixel) calculation.</h3>
   *
   * <tt>....</tt> Channels<br>
   * <tt>::::</tt> Channels on either side corresponding to 1/2 overlap
   *               These exist in the hardware but are not displayed by the OT.
   * <tt>''''</tt> Channels that exist neither in the hardware nor in the OT.
   *               They exit only as array in the ACSIS DR software. And
   *               in case of hybrid mode and non-zero overlap
   *               this array has empty fields on either side.<br>
   *
   * <h4>Example 1: Hybrid mode</h4>
   *
   * One subsystem (SPW1) as displayed in the OT containing
   * two individual subbands (SPW1.1 and SPW1.2) corresponding to the hardware.<p>
   *
   * <pre>
   *
   *                                       reference channel
   *                                                |
   *     channel 0                                  |
   *             |                                  |
   *             ::::....................................                                     SPW1.1 (hardware)
   *                                                |
   *                                     channel 0  |
   *                                             |  |
   *                                             ....................................::::     SPW1.2 (hardware)
   * channel 0                                      |
   *         |                                      |
   *         ''''::::................................................................::::'''' SPW1   (ACSIS DR)
   *                                                |
   *                                                |
   *                 ................................................................         as displayed by OT
   *                                                |
   * </pre>
   *
   * In this toy example we have
   * <pre>
   *   reference channel for SPW1.1 = 35
   *   reference channel for SPW1.2 =  3
   *   reference channel for SPW1   = 39
   * </pre>
   *
   * <h4>Example 2: Non-Hybrid mode</h4>
   *
   * 1 subsystem (SPW1) as displayed in the OT containing
   * 1 "hybrid" subband corresponding to the hardware.<p>
   *
   * <pre>
   *
   *                                       reference channel
   *                                                |
   *                                                |
   *                                                |
   *         ::::........................................................................::::  subband in hardware
   *                                                |                                          (not in XML in non-hybrid mode)
   * channel 0                                      |
   *         |                                      |
   *         ::::........................................................................:::: SPW1   (ACSIS DR)
   *                                                |
   *                                                |
   *             ........................................................................     as displayed by OT
   *                                                |
   * </pre>
   *
   * In this toy example we have
   * <pre>
   *   reference channel for SPW1 = 39
   * </pre>
   *
   *
   * @param hybridSubBand The index of the hybrid subband.
   *                      If this spectral window represents a subsystem rather than an
   *                      individual hybrid subband then this must be indicated by setting
   *                      this paramater to {@link #SUBSYSTEM_INDEX}
   * @param subSystemXML  XML which is part of the spectral_window elements
   *                      that represent subsystems rather than hubrid subbands.
   *                      Such XML includes elements such as subband_list and baseline_fit.
   */
  public String spectralWindowToXML(String restFrequencyId,
				    int sideband,
				    String subSystemXML,
				    String indent,
                                    int subsystem,
                                    int hybridSubBand) {

    String idString = "SPW" + (subsystem + 1);

    if(hybridSubBand != SUBSYSTEM_INDEX) {
      idString += "." + (hybridSubBand + 1);
    }

    double channelWidth = getIndividualSubBandWidth(subsystem) / (double)getIndividualSubBandChannels(subsystem);

    // Used for memory allocation. Number of channels is not
    // reduced according to overlap to be on the save side.
    int numberIfChannels;

    // The following calculation assumes the number of individual subbands to be 1 or even.

    // The reference pixel should really be called reference channel
    // but it is called if_ref_channel in the current spectral_window XML.
    int referencePixel;

    // Note that hybridSubBand should always be SUBSYSTEM_INDEX
    // if getNumHybridSubBands() is 1
    // because then this method is only called once for the subsystem
    // spectral_window and not for the subband spectral_window
    if(hybridSubBand == SUBSYSTEM_INDEX) {
      numberIfChannels = getChannelsTotal(subsystem);

      referencePixel   = numberIfChannels / 2;
    }
    else {
      numberIfChannels = getIndividualSubBandChannels(subsystem);

      referencePixel = (int)
                         (
                           (
                             (
                               (getBandWidth(subsystem) / getNumHybridSubBands(subsystem))
                               * ((getNumHybridSubBands(subsystem) / 2.0) - hybridSubBand)
                             )

                             + (getOverlap(subsystem) / 2.0)
                           )

                           / channelWidth
                         );
    }

    String drComponentXml = "";

    // Now adjust channelWidth, numberIfChannels and referencePixel according to the channel binning factor
    SpDRRecipe spDRRecipe = (SpDRRecipe)SpItemUtilities.findDRRecipe(_parent);

    if(spDRRecipe != null) {
      double channelBinning = (double)spDRRecipe.getChannelBinning();

      channelWidth     *= channelBinning;
      numberIfChannels /= channelBinning;
      referencePixel   /= channelBinning;

      if(hybridSubBand == SUBSYSTEM_INDEX) {
        drComponentXml += spDRRecipe.get_baseline_fit(indent);
      }

      drComponentXml += spDRRecipe.get_ms_truncation(indent);
    }

    return 
      indent + "<spectral_window id=\"" + idString + "\">\n" +
      subSystemXML +
      indent + "  <bandwidth_mode mode=\"\"/>\n" +  // A possible value would be "1GHzx1024".
                                                        // But this is not properly by the software
                                                        // that handles the spectral_window XML.
                                                        // So it is left empty for now.
      indent + "  <window type=\"" + spDRRecipe.getWindowType() + "\"/>\n" +
      indent + "  <rest_frequency_ref ref=\"" + restFrequencyId +"\"/>\n" +
      indent + "  <fe_sideband sideband=\"" + sideband + "\"/>\n" +
      indent + "  <if_coordinate>\n" +
      indent + "    <if_ref_freq units=\"GHz\">" +
        (getCentreFrequency(subsystem) / 1.0E9) +
        "</if_ref_freq>\n" +
      indent + "    <if_ref_channel>" +
        referencePixel +
        "</if_ref_channel>\n" +
      indent + "    <if_chan_width units=\"Hz\">" +
        channelWidth +
        "</if_chan_width>\n" +
      indent + "    <if_nchans>" +
        numberIfChannels +
        "</if_nchans>\n" +
      indent + "  </if_coordinate>\n" +
      drComponentXml +
      indent + "</spectral_window>\n";
   }

  /**
   * @param subsystems    Number of subsystems per feed. (in parameter)
   * @param subbands      Total number of subbands per feed. (in parameter)
   * @param bandwidth     Maximum bandwidth per subband. (in parameter)
   *
   * @return label for configuration
   */
  public String getConfigLabel() {
    // Ceck for multiple bandwidths.
    // The Hashtable class is only used as a convenient way of listing all
    // bandwidths and deleting any duplications from the list.
    Hashtable table = new Hashtable();
    for(int i = 0; i < getNumSubSystems(); i++) {
      table.put("" + getBandWidth(i), "");
    }

    if(table.size() > 1) {
      if(getFrontEnd().trim().toUpperCase().startsWith("HARP")) {
        return "4M";
      }
      else {
        return "11HM";
      }
    }
    else {
      return getConfigLabel(getFrontEnd(),
                            getNumSubSystems(),
                            getNumHybridSubBands(0),
                            getBandWidth(0));
    }
  }

  /**
   * @param subsystems    Number of subsystems per feed. (in parameter)
   * @param subbands      Total number of subbands per feed. (in parameter)
   * @param bandwidth     Maximum bandwidth per subband. (in parameter)
   *
   * @return label for configuration
   */
  public static String getConfigLabel(String frontend, int subsystems, int subbands, double bandwidth) {
    if(frontend.trim().toUpperCase().startsWith("HARP")) {
      switch(subsystems) {
        case 1:
          switch(subbands) {
            case 1:
              if(bandwidth == 2.5E8) {
                return "1";
              }
              else {
                return "5";
              }
            case 2:
              if(bandwidth <= (subbands * 2.5E8)) {
                return "2H";
              }
              else {
                return "6H";
              }
          }
        case 2:
          switch(subbands) {
            case 1:
              if(bandwidth == 2.5E8) {
                return "3M";
              }
//              else {
//                return "4M";
//              }
	  }
      }
    }
    else {
      switch(subsystems) {
        case 1:
          switch(subbands) {
            case 2:
              if(bandwidth <= (subbands * 2.5E8)) {
                return "7H";
              }
              else {
                return "13H";
              }
            case 4:
              if(bandwidth <= (subbands * 2.5E8)) {
                return "9H";
              }
              else {
                return "14H";
              }
          }
        case 2:
          switch(subbands) {
            case 1:
              if(bandwidth == 2.5E8) {
                return "8M";
              }
              else {
                return "15M";
              }
            case 2:
              if(bandwidth <= (subbands * 2.5E8)) {
                return "10H";
              }
//              else {
//                return "11HM";
//              }
          }
        case 4:
          switch(subbands) {
            case 1:
              if(bandwidth == 2.5E8) {
                return "12M";
              }
          }
      }
    }

    return CONFIG_LABEL_NONE;
  }

  private void write(String text, String file) throws IOException {
    PrintWriter printWriter = new PrintWriter(new FileOutputStream(file));
    printWriter.println(text);
    printWriter.close();
  }

  /**
   * Returns a String describing the observe mode.
   *
   * Observe mode are currently obtained by looking at
   * iterators so OBS_MODE_FOCUS and OBS_MODE_FOCUS_CHOP are
   * distinguished by looking at whether there is a chop iterator
   * abovbe the focus observe eye in the Science Program tree rather
   * than be looking at the switiching mode of the focus observe eye.<p>
   *
   * If no observe mode is recognized {@link #OBS_MODE_UNRECOGNIZED} is returned.<p>
   *
   * OBS_MODE_POINTING_JIGGLE_CHOP can currently not be recognized.
   */
  public static String getObserveMode(SpIterObserveBase iterComp) {

    if(iterComp instanceof SpIterFocusObs) {
      return OBS_MODE_FOCUS_CHOP;
    }

    if(iterComp instanceof SpIterStareObs) {
      return OBS_MODE_GRID_PSSW;
    }

    if(iterComp instanceof SpIterJiggleObs) {
      if(((SpIterJiggleObs)iterComp).getSwitchingMode().equals(SpIterJCMTObs.SWITCHING_MODE_CHOP)) {
        return OBS_MODE_JIGGLE_CHOP;
      }
      else {
        return OBS_MODE_JIGGLE_FREQSW;
      }
    }

    if(iterComp instanceof SpIterRasterObs) {
      return OBS_MODE_RASTER_PSSW;
    }
  
    // Observe mode not recognized.
    return OBS_MODE_UNRECOGNIZED;
  }

  /**
   * Returns a vector of SpItems in a subtree that instantiate the given class.
   *
   *
   * This method could be moved to {@link gemini.sp.SpTreeMan} as it is usefull
   * not just in this translator.
   *
   * The result Vector does <b>not</b> include <tt>rootItem</tt> itself even
   * if it is of type <tt>type</tt>.
   *
   * @param rootItem        Root of subtree in which items of type <tt>type</tt> are searched.
   * @param type            Items of this type are returned
   *
   * @see gemini.sp.SpTreeMan.findAllItems(SpItem,String)
   */
  public static Vector findAllItems(SpItem rootItem, Class type) {
    Vector result = new Vector();

    findAllItems(rootItem, type, result);

    return result;
  }

  /**
   * Recursive width first search of SpItem subtree.
   *
   * Used in {@link #findAllItems(SpItem,Class)}.
   *
   * The result Vector does <b>not</b> include <tt>rootItem</tt> itself even
   * if it is of type <tt>type</tt>.
   */
  public static void findAllItems(SpItem rootItem, Class type, Vector result) {
    Enumeration children = rootItem.children();
    Object next;

    while(children.hasMoreElements()) {
      next = children.nextElement();

      if(type.isInstance(next)) {
        result.add(next);
      }
    }

    children = rootItem.children();

    if(children == null) {
      return;
    }

    while(children.hasMoreElements()) {
      findAllItems((SpItem)children.nextElement(), type, result);
    }
  }

  /**
   * Returns the most direct ancester item of iterComp of the given type in the sequence folder (SpIterFolder)
   * or null if no such ancester item exists.
   */
  public static SpIterComp getAncesterInSequence(SpIterComp iterComp, Class type) {
    SpItem parent = iterComp.parent();

    while((!(parent instanceof SpIterFolder)) && (!(parent == null))) {
      if(type.isInstance(parent)) {
        try {
	  return (SpIterComp)parent;
        }
        catch(Exception e) {
          e.printStackTrace();
          return null;
        }
      }

      parent = parent.parent();
    }

    return null;
  }

  /**
   * Returns &lt;BASE&gt; elements in the SpTelescopeObsComp XML.
   */
  public static String getTcsXml(SpTelescopeObsComp telescopeObsComp) {
    StringBuffer xmlBuffer = new StringBuffer();

    StringTokenizer stringTokenizer = new StringTokenizer(telescopeObsComp.toXML(), "\n");

    String line;

    while(stringTokenizer.hasMoreTokens()) {
      line = stringTokenizer.nextToken();

      if((!line.trim().equals("")) &&
         (!line.trim().startsWith("<?xml")) &&
         (!line.trim().startsWith("<SpTelescopeObsComp")) &&
         (!line.trim().startsWith("</SpTelescopeObsComp")) &&
         (!line.trim().startsWith("<meta"))) {

        xmlBuffer.append(line + "\n");
      }
    }

    return xmlBuffer.toString();
  }

  /**
   * Returns &lt;SECONDARY&gt; element in the SpTelescopeObsComp XML.
   *
   * The &lt;SECONDARY&gt; element is defined in
   * <a href="http://www.jach.hawaii.edu/JACdocs/JCMT/OCS/ICD/006">
   * JCMT TCS/OCS Interfaces</a>.
   */
  public static String getTcsXml(SpIterChop iterChop) {
    StringBuffer xmlBuffer = new StringBuffer();

    StringTokenizer stringTokenizer = new StringTokenizer(iterChop.toXML(), "\n");

    String line;

    boolean inSECONDARYelement = false;

    // Record including <SECONDARY> and </SECONDARY>.
    while(stringTokenizer.hasMoreTokens()) {
      line = stringTokenizer.nextToken();

      // Return at the end of the first <SECONDARY>
      // There might be <SECONDARY> in the XML
      // of the child nodes which is included
      // in iterComp.toXML() but that will be
      // dealt with separately later.
      if(line.trim().startsWith("</SECONDARY")) {
        return xmlBuffer.toString();
      }

      if(inSECONDARYelement) {
        xmlBuffer.append(line + "\n");
      }

      if(line.trim().startsWith("<SECONDARY")) {
        inSECONDARYelement = true;
      }
    }

    return xmlBuffer.toString();
  }

  /**
   * This method only returns something meaningful if applied to
   * items that save their values as a
   * <a href="http://www.jach.hawaii.edu/JACdocs/JCMT/OCS/ICD/006">
   * JCMT TCS/OCS Interfaces</a> compatible &lt;obsArea&gt; XML element.
   *
   * E.g. SpIterOffset, SpIterRasterObs.
   */
  public static String getTcsXml(SpIterComp iterComp, boolean ignorePA) {
    StringBuffer xmlBuffer = new StringBuffer();

    StringTokenizer stringTokenizer = new StringTokenizer(iterComp.toXML(), "\n");

    String line;

    boolean inObsAreaElement = false;

    // Ignore the <obsArea> and </obsArea> lines and
    // record only what is inside the <obsArea> element
    while(stringTokenizer.hasMoreTokens()) {
      line = stringTokenizer.nextToken();

      // Return at the end of the first <obsArea>
      // There might be <obsArea> in the XML
      // of the child nodes which is included
      // in iterComp.toXML() but that will be
      // dealt with separately later.
      if(line.trim().startsWith("</obsArea")) {
        return xmlBuffer.toString();
      }

      if(inObsAreaElement) {
        if((!ignorePA) || line.indexOf("<PA") < 0) {
          xmlBuffer.append(line + "\n");
        }
      }

      if(line.trim().startsWith("<obsArea")) {
        inObsAreaElement = true;
      }
    }

    return xmlBuffer.toString();
  }

  /**
   * Returns &lt;JIGGLE&gt; and &lt;TIMING&gt; element in the SpTelescopeObsComp XML.
   *
   * The &lt;JIGGLE&gt; and &lt;TIMING&gt; element is defined in
   * <a href="http://www.jach.hawaii.edu/JACdocs/JCMT/OCS/ICD/006">
   * JCMT TCS/OCS Interfaces</a>.
   *
   * The SCALE attribute is currently hard-wired to 1.0.
   */
  public static String [] getTcsXml(SpIterJiggleObs iterComp) {
    String [] result = new String[2];

    result[0] =
      "    <JIGGLE NAME=\"" + iterComp.getJigglePattern() + "\" SYSTEM=\"" + iterComp.getCoordSys() + "\" SCALE=\"1.0\">\n" +
      "      <PA>" + iterComp.getPosAngle() + "</PA>\n" +
      "    </JIGGLE>";

    result[1] =
      "    <TIMING>\n" +
      "      <JIGS_PER_CHOP N_CYC_OFF=\"4\" N_JIGS_ON=\"10\"/>\n" + // TODO: implement properly
      "    </TIMING>";

    return result;
  }
}

