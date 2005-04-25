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
public class SpIterFlatObs extends SpIterJCMTObs {

  public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "flatObs", "Flat");

  // Register the prototype.
  static {
    SpFactory.registerPrototype(new SpIterFlatObs());
  }


  /**
   * Default constructor.
   */
  public SpIterFlatObs() {
    super(SP_TYPE);
  }

    public double getElapsedTime() {
	return 0.0;
    }

    public void setupForHeterodyne() {
    }

    public void setupForSCUBA() {
    }
}


