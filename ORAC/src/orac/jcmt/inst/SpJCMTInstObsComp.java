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

import gemini.sp.SpType;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import orac.jcmt.SpJCMTConstants;

/**
 * A base class for JCMT instrument observation component items. This extends
 * gemini.sp.obsComp.SpInstObsComp and adds JCMT-specific features.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public abstract class SpJCMTInstObsComp extends SpInstObsComp implements SpJCMTConstants {
    
  /**
   * Constructor. Sets default values for attributes.
   */
  public SpJCMTInstObsComp(SpType spType) {
    super(spType);
  }
  

  /**
   * Generic IterationTracker for JCMT.
   *
   * @see gemini.sp.obsComp.SpInstObsComp
   */
  private class IterTrackerJCMT extends IterationTracker {
    double currentSecsPerIntegration = 0.0;;

    // Number of Integrations
    int currentIntegrations = 1;

    public void update(SpIterStep spIterStep) {
      SpIterValue spIterValue = null;

      try {
        String attribute = null;
        String value     = null;

        for(int i = 0; i < spIterStep.values.length; i++) {
          // SpIterStep.values     is an array of SpIterValue
          // SpIterValue.values    is an array of String the first of which contains
          attribute = spIterStep.values[i].attribute;
	  value     = spIterStep.values[i].values[0];
	
          if(attribute.equals(ATTR_SECS_PER_INTEGRATION)) {
            currentSecsPerIntegration = Double.valueOf(value).doubleValue();
          }

          if(attribute.equals(ATTR_INTEGRATIONS)) {
            currentIntegrations = Integer.valueOf(value).intValue();
          }
	}  
      }
      catch(Exception e) {
        System.out.println("Could not process iteration step "
	                 + spIterStep.title + " for time estimation:\n\n" + e);
      }
    }

    public double getObserveStepTime () {
      return currentIntegrations * currentSecsPerIntegration;
    }
  }

  public IterationTracker createIterationTracker() {
    return new IterTrackerJCMT();
  }

}
