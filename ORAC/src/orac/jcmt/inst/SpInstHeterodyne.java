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

import java.io.*;
import java.util.*;

import orac.util.LookUpTable;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

import gemini.util.Angle;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpStareCapability;

/**
 * The Heterodyne instrument Observation Component
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public final class SpInstHeterodyne extends SpJCMTInstObsComp {

    /**
     * The attribute value model of the gemini classes is to limited for this class.
     * The av table of this class is used in stead to hold data in a "tree like" way.
     * For example:
     *   sideband of spectral-window "CO2-1a" is returned by
     *   _avTable.get(SPECTRAL_WINDOW + "." + "CO2-1a" + "." + SIDEBAND);
     *
     *   Some other values of this spectral window:
     *
     *   _avTable.get(SPECTRAL_WINDOW + "." + "CO2-1a" + "." + RECEIVER);
     *   _avTable.get(SPECTRAL_WINDOW + "." + "CO2-1a" + "." + IF_CENTRE_FREQUENCY);
     *   _avTable.get(SPECTRAL_WINDOW + "." + "CO2-1a" + "." + MARKER_FREQUENCY);
     *   
     *   The same for another spectral window, say "CO2-1b":
     *
     *   _avTable.get(SPECTRAL_WINDOW + "." + "CO2-1b" + "." + RECEIVER);
     *   _avTable.get(SPECTRAL_WINDOW + "." + "CO2-1b" + "." + IF_CENTRE_FREQUENCY);
     *   _avTable.get(SPECTRAL_WINDOW + "." + "CO2-1b" + "." + MARKER_FREQUENCY);
     *   
     */
    public static String SPECTRAL_WINDOW     = "spectral-window";

    public static String RECEIVER            = "receivser";
    public static String IF_CENTRE_FREQUENCY = "if-centre-frequency";
    public static String SIDEBAND            = "sideband";
    public static String MARKER_FREQUENCY    = "marker-frequency";
    public static String NUM_CHAN            = "num-chan";
    public static String CHANNEL_WIDTH       = "channel-width";
    public static String ASSOC_SPW_NAMES     = "assoc-spw-names";
    public static String ASSOC_NATURE        = "assoc-nature";
    public static String ARCHIVE             = "archive";


  public static final SpType SP_TYPE =
    SpType.create( SpType.OBSERVATION_COMPONENT_TYPE, "inst.Heterodyne", "Heterodyne" );

//Register the prototype.
  static {
    SpFactory.registerPrototype( new SpInstHeterodyne() );
  }

  public SpInstHeterodyne() {
    super( SP_TYPE );

    // MFO TEST DEBUG toAttr(String [] a = {"abc", "xyz", "123"});
  }


//  public String toAttr(String str1, String str2) {
//    return str1 + "." + str2;
//  }
}


