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

import gemini.util.Format;

import java.util.Enumeration;

import orac.jcmt.SpJCMTConstants;


/**
 * Focus Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterFocusObs extends SpIterJCMTObs {
  public static String [] AXES = { "x", "y", "z" };

  public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "focusObs", "Focus");

  // Register the prototype.
  static {
    SpFactory.registerPrototype(new SpIterFocusObs());
  }


  /**
   * Default constructor.
   */
  public SpIterFocusObs() {
    super(SP_TYPE);

    _avTable.noNotifySet(ATTR_AXIS,         AXES[2], 0);
    _avTable.noNotifySet(ATTR_STEPS,        "" + getDefaultSteps(AXES[2]), 0);
    _avTable.noNotifySet(ATTR_FOCUS_POINTS, "5", 0);
    _avTable.noNotifySet(ATTR_AUTOMATIC_TARGET, "true", 0);

  }


  public String getAxis() {
    return _avTable.get(ATTR_AXIS);
  }

  public void setAxis(String axis) {
    _avTable.set(ATTR_AXIS, axis);
  }

  public double getSteps() {
    return _avTable.getDouble(ATTR_STEPS, 0.0);
  }

  public void setSteps(double steps) {
    _avTable.set(ATTR_STEPS, steps);
  }
  
  public void setSteps(String stepsStr) {
    _avTable.set(ATTR_STEPS, Format.toDouble(stepsStr));
  }

  public int getFocusPoints() {
    return _avTable.getInt(ATTR_FOCUS_POINTS, 0);
  }

  public void setFocusPoints(int focusPoints) {
    _avTable.set(ATTR_FOCUS_POINTS, focusPoints);
  }

  public void setFocusPoints(String focusPointsStr) {
    _avTable.set(ATTR_FOCUS_POINTS, Format.toInt(focusPointsStr));
  }

  public static double getDefaultSteps(String axis) {
    if(axis.equalsIgnoreCase("z")) {
      return 0.3;
    }
    else {
      return 1.0;
    }
  }

    public double getElapsedTime() {
	SpInstObsComp instrument = SpTreeMan.findInstrument(this);
	double time = 0.0;
	if (instrument instanceof orac.jcmt.inst.SpInstSCUBA) {
	    time = 22.4*getIntegrations()*getFocusPoints() + SCUBA_STARTUP_TIME;
	}
	else if (instrument instanceof orac.jcmt.inst.SpInstHeterodyne) {
	    time = 0.0;
	}
	return time;
    }

    public void setupForHeterodyne() {
	_avTable.noNotifySet(ATTR_SWITCHING_MODE, "Beam", 0);
	_avTable.noNotifySet(ATTR_SECS_PER_CYCLE, "60", 0);
// 	_avTable.noNotifySet(ATTR_NO_OF_CYCLES, "0", 0);
	_avTable.set(ATTR_CYCLE_REVERSAL, true);
    }

    public void setupForSCUBA() {
	_avTable.noNotifyRm(ATTR_SWITCHING_MODE);
	_avTable.noNotifyRm(ATTR_SECS_PER_CYCLE);
// 	_avTable.noNotifyRm(ATTR_NO_OF_CYCLES);
	_avTable.noNotifyRm(ATTR_CYCLE_REVERSAL);
    }
}


