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

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.util.Format;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * The Heterodyne instrument Observation Component.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public final class SpInstHeterodyne extends SpJCMTInstObsComp {

  public static double LIGHTSPEED = 2.99792458E5;

  // FrontEnd

  /** Front end name. */
  public static final String ATTR_FE_NAME = "feName";

  /** Receiver: Central IF. */
  public static final String ATTR_FE_IF = "feIF";

  /** Mode: single side band (ssb), double side band (dsb). */
  public static final String ATTR_MODE = "mode";

  /** Band mode: 1-system, 2-system etc.  */
  public static final String ATTR_BAND_MODE = "bandMode";

  /** Radial velocity. */
  public static final String ATTR_VELOCITY = "velocity";

  /** Band: upper side band (usb), lower side band (lsb), side band with line in range (optimum).  */
  public static final String ATTR_BAND = "band";

  /** Molecule. */
  public static final String ATTR_MOLECULE = "molecule";

  /** Transition. */
  public static final String ATTR_TRANSITION = "transition";

  /** Line Frequency. */
  public static final String ATTR_LINE_FREQUENCY = "lineFrequency";

  /** Overlap of multiple hybrid subbands. */
  public static final String ATTR_OVERLAP = "overlap";

  /** LO1. */
  public static final String ATTR_LO1 = "lo1";


  // FrequencyTable

  /** Array of  */
  public static final String ATTR_CENTRE_FREQUENCY = "centreFrequency";

  /** */
  public static final String ATTR_BANDWIDTH = "bandWidth";

  /** */
  public static final String ATTR_CHANNELS = "channels";

  /** */
  public static final String ATTR_HYBRID_SUBBANDS = "hybridSubBands";


  public static String [] JIGGLE_PATTERNS = { "5 Point", "Jiggle", "Rotation" };

  public static final SpType SP_TYPE =
    SpType.create( SpType.OBSERVATION_COMPONENT_TYPE, "inst.Heterodyne", "Het Setup" );

//Register the prototype.
  static {
    SpFactory.registerPrototype( new SpInstHeterodyne() );
  }

  public SpInstHeterodyne() {
    super( SP_TYPE );

    // Set defaults. Make sure all these values exist in the frequency editor widgets.
    // Trailing white space need to be exactly as they are in the frequency editor
    // widgets (see ATTR_TRANSITION)
    _avTable.noNotifySet(ATTR_FE_NAME,          "A3",                      0);
    _avTable.noNotifySet(ATTR_MODE,             "ssb",                     0);
    _avTable.noNotifySet(ATTR_BAND_MODE,        "1-system",                0);
    _avTable.noNotifySet(ATTR_OVERLAP,          "0.0",                     0);
    _avTable.noNotifySet(ATTR_BAND,             "usb",                     0);
    _avTable.noNotifySet(ATTR_LO1,              "2.2229E11",               0);
    _avTable.noNotifySet(ATTR_CENTRE_FREQUENCY, "" + 4.0E9,                0);
    _avTable.noNotifySet(ATTR_BANDWIDTH,        "" + 1.0E9,                0);
    _avTable.noNotifySet(ATTR_CHANNELS,         "32768",                   0);
    _avTable.noNotifySet(ATTR_MOLECULE,         "CN, v = 0, 1",            0);
    _avTable.noNotifySet(ATTR_TRANSITION,       "2 0 2 1  - 1 0 2 1 ",     0);
  }


  /**
   * Appends front end name to title.
   */
  public String getTitle() {

    String frontEndName = getTable().get(ATTR_FE_NAME);

    if(frontEndName == null) {
      return super.getTitle();
    }
    else {
      return super.getTitle() + " (" + frontEndName + ")";
    }
  }


  /**
   * Get jiggle pattern options.
   *
   * @return String array of jiggle pattern options.
   */
  public String [] getJigglePatterns() {
    return JIGGLE_PATTERNS;
  }

  /** Not properly implemented yet. Returns 0.0. */
  public double getDefaultScanVelocity() {
    return 0.0;
  }

  /** Not properly implemented yet. Returns 0.0. */
  public double getDefaultScanDy() {
    return 0.0;
  }


  /**
   * Get front end name.
   */
  public String getFrontEnd() {
    return _avTable.get(ATTR_FE_NAME);
  }

  /**
   * Set front end name.
   */
  public void setFrontEnd(String value) {
    _avTable.set(ATTR_FE_NAME, value);
  }


  /**
   * Get receiver's central IF.
   */
  public double getFeIF() {
    return _avTable.getDouble(ATTR_FE_IF, 0.0);
  }

  /**
   * Set receiver's central IF.
   */
  public void setFeIF(double value) {
    _avTable.set(ATTR_FE_IF, value);
  }

  /**
   * Set receiver's central IF.
   */
  public void setFeIF(String value) {
    setFeIF(Format.toDouble(value));
  }

  /**
   * Get mode: single side band (ssb), double side band (dsb).
   */
  public String getMode() {
    return _avTable.get(ATTR_MODE);
  }

  /**
   * Set  mode: single side band (ssb), double side band (dsb).
   */
  public void setMode(String value) {
    _avTable.set(ATTR_MODE, value);
  }


  /**
   * Get band mode: 1-system, 2-system etc. 
   */
  public String getBandMode() {
    return _avTable.get(ATTR_BAND_MODE);
  }

  /**
   * Set band mode: 1-system, 2-system etc.
   */
  public void setBandMode(String value) {
    _avTable.set(ATTR_BAND_MODE, value);
  }


  /**
   * Get velocity.
   */
  public double getVelocity() {
    return _avTable.getDouble(ATTR_VELOCITY, 0.0);
  }

  /**
   * Set velocity.
   */
  public void setVelocity(double value) {
    _avTable.set(ATTR_VELOCITY, value);
  }

  /**
   * Set velocity.
   */
  public void setVelocity(String value) {
    setVelocity(Format.toDouble(value));
  }

  public double getRedshift() {
    return getVelocity() / LIGHTSPEED;
  }

  /**
   * Get band: upper side band (usb), lower side band (lsb), side band with line in range (optimum).
   */
  public String getBand() {
    return _avTable.get(ATTR_BAND);
  }

  /**
   * Set band: upper side band (usb), lower side band (lsb), side band with line in range (optimum).
   */
  public void setBand(String value) {
    _avTable.set(ATTR_BAND, value);
  }


  /**
   * Get molecule of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public String getMolecule(int subsystem) {
    return _avTable.get(ATTR_MOLECULE, subsystem);
  }

  /**
   * Set molecule of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public void setMolecule(String value, int subsystem) {
    _avTable.set(ATTR_MOLECULE, value, subsystem);
  }


  /**
   * Get transition of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public String getTransition(int subsystem) {
    return _avTable.get(ATTR_TRANSITION, subsystem);
  }

  /**
   * Set transition of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public void setTransition(String value, int subsystem) {
    _avTable.set(ATTR_TRANSITION, value, subsystem);
  }


  /**
   * Get frequency of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public double getLineFrequency(int subsystem) {
    return _avTable.getDouble(ATTR_LINE_FREQUENCY, subsystem, 0.0);
  }

  /**
   * Set frequency of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public void setLineFrequency(double value, int subsystem) {
    _avTable.set(ATTR_LINE_FREQUENCY, value, subsystem);
  }

  /**
   * Set frequency of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public void setLineFrequency(String value, int subsystem) {
    setLineFrequency(Format.toDouble(value), subsystem);
  }

  /**
   * Set frequency of specified subsystem to the centre of the band.
   *
   * @param Number of subsystems (starting at 0).
   */
  public void setLineFrequency(int subsystem) {
    setLineFrequency(getFrequencyAtCentre(subsystem), subsystem);
  }


  /**
   * Get centre frequency (IF) of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public double getCentreFrequency(int subsystem) {
    return _avTable.getDouble(ATTR_CENTRE_FREQUENCY, subsystem, 0.0);
  }

  /**
   * Set centre frequency (IF) of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public void setCentreFrequency(double value, int subsystem) {
    _avTable.set(ATTR_CENTRE_FREQUENCY, value, subsystem);
  }

  /**
   * Set centre frequency (IF) of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public void setCentreFrequency(String value, int subsystem) {
    setCentreFrequency(Format.toDouble(value), subsystem);
  }


  /**
   * Get bandwidth of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public double getBandWidth(int subsystem) {
    return _avTable.getDouble(ATTR_BANDWIDTH, subsystem, 0.0);
  }

  /**
   * Set bandwidth of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public void setBandWidth(double value, int subsystem) {
    _avTable.set(ATTR_BANDWIDTH, value, subsystem);
  }

  /**
   * Set bandwidth of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public void setBandWidth(String value, int subsystem) {
    setBandWidth(Format.toDouble(value), subsystem);
  }


  /**
   * Get number of hybrid subbands that make up the band of the specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public double getNumHybridSubBands(int subsystem) {
    return _avTable.getDouble(ATTR_HYBRID_SUBBANDS, subsystem, 0.0);
  }

  /**
   * Set number of hybrid subbands that make up the band of the specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public void setNumHybridSubBands(double value, int subsystem) {
    _avTable.set(ATTR_HYBRID_SUBBANDS, value, subsystem);
  }

  /**
   * Set number of hybrid subbands that make up the band of the specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public void setNumHybridSubBands(String value, int subsystem) {
    setNumHybridSubBands(Format.toDouble(value), subsystem);
  }


  /**
   * Get channels of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public int getChannels(int subsystem) {
    return _avTable.getInt(ATTR_CHANNELS, subsystem, 0);
  }

  /**
   * Set channels of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public void setChannels(int value, int subsystem) {
    _avTable.set(ATTR_CHANNELS, value, subsystem);
  }


  /**
   * Get overlap of multiple subbands in one subsystem.
   */
  public double getOverlap() {
    return _avTable.getDouble(ATTR_OVERLAP, 0.0);
  }

  /**
   * Set overlap of multiple subbands in one subsystem.
   */
  public void setOverlap(double value) {
    _avTable.set(ATTR_OVERLAP, value);
  }

  /**
   * Set overlap of multiple subbands in one subsystem.
   */
  public void setOverlap(String value) {
    setOverlap(Format.toDouble(value));
  }


  /**
   * Get LO1.
   */
  public double getLO1() {
    return _avTable.getDouble(ATTR_LO1, 0.0);
  }

  /**
   * Set LO1.
   */
  public void setLO1(double value) {
    _avTable.set(ATTR_LO1, value);
  }

  /**
   * Set LO1.
   */
  public void setLO1(String value) {
    setLO1(Format.toDouble(value));
  }

  public int getNumSubSystems() {
    return _avTable.size(ATTR_CENTRE_FREQUENCY);
  }


  /**
   * Get frequency at the centre of the band of specified subsystem.
   *
   * @param Number of subsystems (starting at 0).
   */
  public double getFrequencyAtCentre(int subsystem) {
    if(!getBand().toLowerCase().equals("usb")) {
       return (getLO1() - getCentreFrequency(subsystem)) * (1.0 + getRedshift());
    }
    else {
       return (getLO1() + getCentreFrequency(subsystem)) * (1.0 + getRedshift());
    }
  }

  /**
   * Creates parts of ACSIS configuration file.
   */
  public String toConfigXML(String indent) {
    String sidebandString  = getBand();

    int sideband = 0;

    if(sidebandString.equals("lsb"))     { sideband = -1; }
    if(sidebandString.equals("usb"))     { sideband =  1; }
    if(sidebandString.equals("optimum")) { sideband =  0; } // will have to set to -1 or 1 by the OT/Frequency Editor
							    // And sidebandString must be set to "lsb" or "usb"
							    // for the front end configuration XML.

    //String indent = "";


    // ------------------- Front end configuration ------------------------------------
    StringBuffer xmlBuffer = new StringBuffer();
    xmlBuffer.append( 
        indent + "<frontend_configure>\n" +
        indent + "  <rest_frequency units=\"GHz\" value=\"" +
                 (getLineFrequency(0) * 1.0E6) + "\"/>\n" + // TODO: Check whether * 1.0E6 has been done before
        indent + "  <if_centre_freq units=\"GHz\" value=\"" + getFeIF() + "\"/>\n" +
        indent + "  <sideband value=\"" + sideband + "\"/>\n" +
        indent + "  <sb_mode value=\"" + getMode().toUpperCase() + "\"/>\n" +
        indent + "  <freq_offset_scale units=\"MHz\" value=\"???\"/>\n" +
        indent + "  <dopple_tracking value=\"ON\"/>\n" +	// Options are ON | OFF. Default to ON for now.
        indent + "  <optimize value=\"DISABLE\"/>\n"		// Options are ENABLE | DISABLE. Default to DIABLE for now.
    );

    if(getFrontEnd().equals("HARP-B")) {
      xmlBuffer.append(
        indent + "  <channel_mask>\n" + // Array of (OFF | ON | NEED). Use Pixeltool to switch pixels ON/OFF. NEED???
        indent + "    <CHAN_MASK_VALUE CHAN=\"00\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"01\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"02\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"03\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"04\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"05\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"06\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"07\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"08\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"09\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"10\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"11\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"12\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"13\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"14\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"15\" VALUE=\"ON\"/>\n" +
        indent + "  </channel_mask>\n"
      );
    }

    xmlBuffer.append(
        indent + "</frontend_configure>\n\n"
    );

    
    // ------------------- ACSIS configuration ----------------------------------------

    // Line list
    xmlBuffer.append(indent + "<line_list>\n");

    for(int i = 0; i < getNumSubSystems(); i++) {
      xmlBuffer.append(indent + "  <rest_frequency id=\"line" + i + "\" units=\"GHz\">" +
                       getLineFrequency(i) +
                      "</rest_frequency>\n");
    }
    
    xmlBuffer.append(indent + "</line_list>\n\n");

    // Acsis spectral windows list
    xmlBuffer.append(
      indent + "<acsis_spw_list>\n" +
      indent + "  <doppler_field ref=\"TCS.RV.DOPPLER???\"/>\n" +
      indent + "  <spectral_window_id_field ref=\"SPECTRAL_WINDOW_ID???\">\n" +
      indent + "  <front_end_lo_freq_field ref=\"FE.STATE.LO_FREQ\">\n"
    );

    // Spectral windows

    for(int i = 0; i < getNumSubSystems(); i++) {
       xmlBuffer.append(spectralWindowToXML("line" + 1, sideband,
      "<!-- <base_line_fit> etc. not implemented yet. -->", indent + "  ", i) + "\n");
    }

    xmlBuffer.append(
      indent + "</acsis_spw_list>\n"
    );

    return xmlBuffer.toString();
  }


  public String spectralWindowToXML(String restFrequencyId,
				    int sideband,
				    String dataReductionXML,
				    String indent,
                                    int subsystem) {
    return 
      indent + "<spectral_window id=\"SPW" + (subsystem + 1) + "\">\n" +
      indent + "  <spw_bandwidth_mode mode=\"1GHzx1024\"/>\n" +
      indent + "  <spw_window type=\"truncate\"/>\n" +
      indent + "  <rest_frequency_ref ref=\"" + restFrequencyId +"\"/>\n" +
      indent + "  <front_end_sideband sideband=\"" + sideband + "\"/>\n" +
      indent + "  <spw_if_coordinate>\n" +
      indent + "    <spw_reference_if_frequency units=\"GHz\">" +
        getCentreFrequency(subsystem) +
        "</spw_reference_if_frequency>\n" +
      indent + "    <spw_reference_pixel>" +
        "4064.0???" +
        "</spw_reference_pixel>\n" +
      indent + "    <spw_if_channel_width units=\"Hz\">" +
        getBandWidth(subsystem) +
        "</spw_if_channel_width>\n" +
      indent + "    <spw_number_if_channel>" +
        getChannels(subsystem) +
        "</spw_number_if_channel>\n" +
      indent + "  </spw_if_coordinate>\n" +
      indent + dataReductionXML + "\n" +
      indent + "</spectral_window>\n";
   }


  /**
   */
  protected void toXML(String indent, StringBuffer xmlBuffer) {
    super.toXML(indent, xmlBuffer);

    int offset = xmlBuffer.length() - (indent.length() + _className.length() + 4);

    xmlBuffer.insert(offset, "\n\n" +
                             indent + "<!-- - - - - - - - - - - - - - - - - - - - - -->\n" + 
                             indent + "<!--          ACSIS Configuration XML        -->\n" + 
                             indent + "<!-- - - - - - - - - - - - - - - - - - - - - -->\n\n" + 
                             toConfigXML(indent + "  "));
  }
}


