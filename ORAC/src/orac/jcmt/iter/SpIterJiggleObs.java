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

import java.util.StringTokenizer;


/**
 * Jiggle Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterJiggleObs extends SpIterJCMTObs {

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

  public double getElapsedTime() {
    SpInstObsComp instrument = SpTreeMan.findInstrument(this);

    if(instrument == null) {
      return 0.0;
    }

    if(instrument instanceof SpInstSCUBA) {
      String jigglePattern = getJigglePattern();
      int    steps    = 0;
      double overhead = 0.0;
      
      // Actual integration time on source
      double totalIntegrationTime = 0.0;

      if((jigglePattern != null) && (jigglePattern.toLowerCase().indexOf('x') > -1)) {
        StringTokenizer st = new StringTokenizer(jigglePattern.toLowerCase(), "x ");   
        steps = Integer.parseInt( st.nextToken() );
      }

      // Calculate overheads      
      switch(steps) {
	// 3X3 jigle map
	case 3: overhead =  9 * getIntegrations(); break;

	// 5X5 jigle map
	case 5: overhead = 18 * getIntegrations(); break;

	// 7X7 jigle map
	case 7: overhead = 27 * getIntegrations(); break;

	// 9X9 jigle map
	case 9: overhead = 36 * getIntegrations(); break;

	// JIG16 (LONG or SHORT) or JIG64 (LONG and SHORT)
	default:
	  if(isJIG64((SpInstSCUBA)instrument)) {
	    overhead = 36 * getIntegrations();
	  }
	  else {
	    overhead =  9 * getIntegrations();
	  }
      }

      // Add SCUBA startup time.
      overhead += SCUBA_STARTUP_TIME;

      // Calculate total integration time
      if(steps > 0) {
        totalIntegrationTime = steps * steps * 2 * getIntegrations();
      }
      else {
        if(isJIG64((SpInstSCUBA)instrument)) {
          totalIntegrationTime = 64 * 2 * getIntegrations();
	}
	else {
          totalIntegrationTime = 16 * 2 * getIntegrations();
        }
      }

      return totalIntegrationTime + overhead;
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

  public static boolean isJIG64(SpInstSCUBA instSCUBA) {
    return instSCUBA.getBolometers() != null &&
           instSCUBA.getBolometers().contains("LONG") &&
           instSCUBA.getBolometers().contains("SHORT");
  }

    public void setupForHeterodyne() {
	// Currently not expecting to use Jiggle for Heterodyne
    }

    public void setupForSCUBA() {
	
    }
}


