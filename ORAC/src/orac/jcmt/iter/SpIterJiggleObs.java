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
import orac.jcmt.inst.SpInstSCUBA;

import java.util.Enumeration;


/**
 * Jiggle Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterJiggleObs extends SpIterJCMTObs {
  public static String [] JIGGLE_PATTERNS = { "5 Point", "Jiggle", "Rotation" };

  public static final SpType SP_TYPE =
    SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "jiggleObs", "Jiggle");

  // Register the prototype.
  static {
    SpFactory.registerPrototype(new SpIterJiggleObs());
  }


  /**
   * Default constructor.
   */
  public SpIterJiggleObs() {
    super(SP_TYPE);
  }

  public double getSecsPerIntegration() {
    SpInstObsComp instrument = SpTreeMan.findInstrument(this);

    if(instrument == null) {
      return 0.0;
    }

    if(instrument instanceof SpInstSCUBA) {
      // For now JIG16 is assumed.
      double overhead = SCUBA_STARTUP_TIME + (9 * getIntegrations());
      return (getIntegrations() * SECS_PER_INTEGRATION_JIG16) + overhead;
      
      // JIG64
      //double overhead = SCUBA_STARTUP_TIME + (36 * getIntegrations());
      //return (getIntegrations() * SECS_PER_INTEGRATION_JIG64) + overhead;
    }
//   else {
     // Heterodyne
//   }

    return 0.0;
  }

  public String getJigglePattern() {
    return _avTable.get(ATTR_JIGGLE_PATTERN);
  }

  public void setJigglePattern(String value) {
    _avTable.set(ATTR_JIGGLE_PATTERN, value);
  }
}


