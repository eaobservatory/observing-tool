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
 * Stare Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterStareObs extends SpIterJCMTObs
{
   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "stareObs", "Photom/Stare");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterStareObs());
}


/**
 * Default constructor.
 */
public SpIterStareObs()
{
   super(SP_TYPE);
}

public double getElapsedTime() {
    SpInstObsComp instrument = SpTreeMan.findInstrument(this);
    double overhead = 0.0;
    double totalIntegrationTime = 0.0;

    if (instrument instanceof orac.jcmt.inst.SpInstSCUBA) {
	overhead = SCUBA_STARTUP_TIME + (8 * getIntegrations());
	// 18 seconds per integration
	totalIntegrationTime = 18 * getIntegrations();
    }
    else if (instrument instanceof orac.jcmt.inst.SpInstHeterodyne) {
    }
   return (overhead + totalIntegrationTime);
}

}


