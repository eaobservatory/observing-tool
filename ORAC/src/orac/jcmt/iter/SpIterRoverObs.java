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
 * Rover Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterRoverObs extends SpIterJCMTObs
{
   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "roverObs", "Rover");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterRoverObs());
}


/**
 * Default constructor.
 */
public SpIterRoverObs()
{
   super(SP_TYPE);

   _avTable.noNotifySet(ATTR_SWITCHING_MODE, getSwitchingModeOptions()[0], 0);
}

    public void setWidePhotom (boolean flag) {
	_avTable.set( ATTR_WIDE_PHOTOMETRY, flag );
    }

    public boolean getWidePhotom () {
	boolean isSet = false;
	if ( _avTable.exists( ATTR_WIDE_PHOTOMETRY ) && _avTable.getBool ( ATTR_WIDE_PHOTOMETRY ) ) {
	    isSet = true;
	}
	return isSet;
    }

public double getElapsedTime() {
    SpInstObsComp instrument = SpTreeMan.findInstrument(this);
    double overhead = 0.0;
    double totalIntegrationTime = 0.0;

    if (instrument instanceof orac.jcmt.inst.SpInstSCUBA) {
	// Go through all of this items parents to see if any are SpIterPOLs
	boolean polPhot = false;
	SpItem parent = parent();
	while (parent != null) {
	    if (parent instanceof SpIterPOL) {
		polPhot = true;
		break;
	    }
	    parent = parent.parent();
	}
	overhead = SCUBA_STARTUP_TIME + (8 * getIntegrations());
	if ( polPhot ) {
	    // 8 seconds per integration
	    totalIntegrationTime = 8 *  getIntegrations();
	}
	else {
	    // 18 seconds per integration
	    if ( getWidePhotom() ) {
		totalIntegrationTime = 24*getIntegrations();
	    }
	    else {
		totalIntegrationTime = 18 * getIntegrations();
	    }
	}
    }
    else if (instrument instanceof orac.jcmt.inst.SpInstHeterodyne) {
	double overheadFactor = 1.2;
	totalIntegrationTime = getIntegrations() * getSecsPerCycle() * overheadFactor;
    }
   return (overhead + totalIntegrationTime);
}

    public void setupForHeterodyne() {
	if (_avTable.get(ATTR_SWITCHING_MODE) == null ||
	    _avTable.get(ATTR_SWITCHING_MODE).equals(""))
	    _avTable.noNotifySet(ATTR_SWITCHING_MODE, "Beam", 0);
	if (_avTable.get(ATTR_SECS_PER_CYCLE) == null ||
	    _avTable.get(ATTR_SECS_PER_CYCLE).equals(""))
	    _avTable.noNotifySet(ATTR_SECS_PER_CYCLE, "60", 0);
// 	_avTable.noNotifySet(ATTR_NO_OF_CYCLES, "0", 0);
	if (_avTable.get(ATTR_CONT_CAL) == null ||
	    _avTable.get(ATTR_CONT_CAL).equals(""))
	    _avTable.set(ATTR_CONT_CAL, true);
// 	if (_avTable.get(ATTR_CYCLE_REVERSAL) == null ||
// 	    _avTable.get(ATTR_CYCLE_REVERSAL).equals(""))
// 	    _avTable.set(ATTR_CYCLE_REVERSAL, true);
    }

    public void setupForSCUBA() {
	_avTable.noNotifyRm(ATTR_SWITCHING_MODE);
	_avTable.noNotifyRm(ATTR_SECS_PER_CYCLE);
// 	_avTable.noNotifyRm(ATTR_NO_OF_CYCLES);
	_avTable.noNotifyRm(ATTR_CONT_CAL);
// 	_avTable.noNotifyRm(ATTR_CYCLE_REVERSAL);
    }

    public String [] getSwitchingModeOptions() {
        return new String [] {
            "Beam",
            "Position"
        };
    }
}


