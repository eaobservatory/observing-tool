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

import java.util.Enumeration;

import orac.ukirt.inst.SpUKIRTInstObsComp;


/**
 * Photom Iterator for SCUBA/JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterPhotomObs extends SpIterJCMTObs
{
   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "photomObs", "Photom");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterPhotomObs());
}


/**
 * Default constructor.
 */
public SpIterPhotomObs()
{
   super(SP_TYPE);
}

public double getSecsPerIntegration() {
   double overhead = SCUBA_STARTUP_TIME + (8 * getIntegrations());

   return (getIntegrations() * SECS_PER_INTEGRATION_PHOT) + overhead;
}

}


