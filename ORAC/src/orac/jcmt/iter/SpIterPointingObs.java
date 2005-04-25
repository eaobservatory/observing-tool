/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.jcmt.iter;

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpTreeMan;

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import gemini.sp.obsComp.SpInstConstants;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpStareCapability;

import java.util.Enumeration;

/**
 * Pointing Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterPointingObs extends SpIterJCMTObs {

  /**
   * Pointing pixel choices.
   *
   * To be decided.
   */
  public static String [] POINTING_PIXEL_MANUAL_CHOICES = { "1", "..." };

  public static String [] POINTING_METHODS = { "9 Position", "16 Position", "25 Position" };

  public static final SpType SP_TYPE =
    SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "pointingObs", "Pointing");

  // Register the prototype.
  static {
    SpFactory.registerPrototype(new SpIterPointingObs());
  }


  /**
   * Default constructor.
   */
  public SpIterPointingObs() {
    super(SP_TYPE);

//     _avTable.noNotifySet(ATTR_POINTING_PIXEL, POINTING_PIXEL_AUTOMATIC, 0);
    _avTable.noNotifySet(ATTR_AUTOMATIC_TARGET, "true", 0);
  }

  public String getSpectralMode() {
    return _avTable.get(ATTR_SPECTRAL_MODE);
  }

  public void setSpectralMode(String value) {
    _avTable.set(ATTR_SPECTRAL_MODE, value);
  }

  public String getPointingPixel() {
    return _avTable.get(ATTR_POINTING_PIXEL);
  }

  public void setPointingPixel(String value) {
    _avTable.set(ATTR_POINTING_PIXEL, value);
  }

    public double getElapsedTime() {
	SpInstObsComp instrument = SpTreeMan.findInstrument(this);
	double overhead = 0.0;
	double totalIntegrationTime = 0.0;

	if (instrument instanceof orac.jcmt.inst.SpInstSCUBA) {
	    overhead = SCUBA_STARTUP_TIME + 9.0*getIntegrations();
	    totalIntegrationTime = 16.0*2.0*getIntegrations();
	}
	else if (instrument instanceof orac.jcmt.inst.SpInstHeterodyne) {
	    totalIntegrationTime = 120.0; // *getIntegrations();
	}
	return (overhead + totalIntegrationTime);
    }

    public void setupForHeterodyne() {
//	if ( _avTable.get(ATTR_SWITCHING_MODE) == null ||
//	     _avTable.get(ATTR_SWITCHING_MODE).equals("") )
//	    _avTable.noNotifySet(ATTR_SWITCHING_MODE, "Beam", 0);
//	if ( _avTable.get(ATTR_POINTING_METHOD) == null ||
//	     _avTable.get(ATTR_POINTING_METHOD).equals("") )
//	    _avTable.noNotifySet(ATTR_POINTING_METHOD, "5-point", 0);
//	if ( _avTable.get(ATTR_SECS_PER_CYCLE) == null ||
//	     _avTable.get(ATTR_SECS_PER_CYCLE).equals("") )
//	    _avTable.noNotifySet(ATTR_SECS_PER_CYCLE, "60", 0);
// 	_avTable.noNotifySet(ATTR_NO_OF_CYCLES, "0", 0);
//	if ( _avTable.get(ATTR_SPECTRAL_MODE) == null ||
//	     _avTable.get(ATTR_SPECTRAL_MODE).equals("") )
//	    _avTable.noNotifySet(ATTR_SPECTRAL_MODE, SPECTRAL_MODE_SPECTRAL_LINE, 0);
    }

    public void setupForSCUBA() {
	_avTable.noNotifyRm(ATTR_SWITCHING_MODE);
	_avTable.noNotifyRm(ATTR_POINTING_METHOD);
	_avTable.noNotifyRm(ATTR_SECS_PER_CYCLE);
// 	_avTable.noNotifyRm(ATTR_NO_OF_CYCLES);
	_avTable.noNotifyRm(ATTR_SPECTRAL_MODE);
    }

    public String [] getSwitchingModeOptions() {
        return new String [] {
            SWITCHING_MODE_BEAM
        };
    }
}


