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


/**
 * Skydip Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterSkydipObs extends SpIterJCMTObs {

  public static String [] START_POSITIONS = { "Zenith", "Horizon", "Automatic" };

  public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "skydipObs", "Skydip");

  // Register the prototype.
  static {
    SpFactory.registerPrototype(new SpIterSkydipObs());
  }


  /**
   * Default constructor.
   */
  public SpIterSkydipObs() {
    super(SP_TYPE);

    _avTable.noNotifySet(ATTR_POSITIONS,      "1", 0);
    _avTable.noNotifySet(ATTR_START_POSITION, START_POSITIONS[0], 0);
  }

  public int getPositions() {
    return _avTable.getInt(ATTR_POSITIONS, 0);
  }

  public void setPositions(int positions) {
    _avTable.set(ATTR_POSITIONS, positions);
  }

  public void setPositions(String positionsStr) {
    _avTable.set(ATTR_POSITIONS, Format.toInt(positionsStr));
  }

  public String getStartPosition() {
    return _avTable.get(ATTR_START_POSITION, 0);
  }

  public void setStartPosition(String startPositionStr) {
    _avTable.set(ATTR_START_POSITION, startPositionStr);
  }
}


