/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.tpe;

import orac.jcmt.SpJCMTConstants;
import jsky.app.ot.tpe.feat.TpeChopFeature;

/**
 * Draws the Chop position for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class TpeJcmtChopFeature extends TpeChopFeature {

  /**
   * Return true if the chop position should be drawn as circle
   * given the coordFrame.
   */
  protected boolean drawAsCircle(String coordFrame) {
    if(coordFrame.equals(SpJCMTConstants.CHOP_SYSTEMS[SpJCMTConstants.CHOP_SYSTEM_TARCKING])) {
      return false;
    }
    else {
      return super.drawAsCircle(coordFrame);
    }
  }
}

