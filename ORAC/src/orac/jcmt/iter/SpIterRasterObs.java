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

import gemini.util.CoordSys;

import java.util.Enumeration;


/**
 * Raster Iterator for ACSIS/JCMT.
 *
 * The Raster iterator (ACSIS) and the Scan iterator share a lot of fuctionality
 * and should in future be either made the same class or share code by other
 * means such as inheritance.
 *
 * @see orac.jcmt.iter.SpIterScanObs
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterRasterObs extends SpIterJCMTObs {
  public static final SpType SP_TYPE =
    SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "rasterObs", "Raster");

  // Register the prototype.
  static {
    SpFactory.registerPrototype(new SpIterRasterObs());
  }


  /**
   * Default constructor.
   */
  public SpIterRasterObs() {
    super(SP_TYPE);

    _avTable.noNotifySet(ATTR_X,           "0.0", 0);
    _avTable.noNotifySet(ATTR_Y,           "0.0", 0);
    _avTable.noNotifySet(ATTR_THETA,       "0.0", 0);
    _avTable.noNotifySet(ATTR_SYSTEM,      CoordSys.COORD_SYS[CoordSys.FK5], 0);
  }

  /** Get map width. */
  public double getX() {
    return _avTable.getDouble(ATTR_X, 0.0);
  }

  /** Set map width. */
  public void setX(double x) {
    _avTable.set(ATTR_X, x);
  }

  /** Set map width. */
  public void setX(String xStr) {
    _avTable.set(ATTR_X, toDouble(xStr));
  }

  /** Get map height. */
  public double getY() {
    return _avTable.getDouble(ATTR_Y, 0.0);
  }

  /** Set map height. */
  public void setY(double y) {
    _avTable.set(ATTR_Y, y);
  }

  /** Set map height. */
  public void setY(String yStr) {
    _avTable.set(ATTR_Y, toDouble(yStr));
  }

  public double getTheta() {
    return _avTable.getDouble(ATTR_THETA, 0.0);
  }

  public void setTheta(double theta) {
    _avTable.set(ATTR_THETA, theta);
  }

  public void setTheta(String thetaStr) {
    _avTable.set(ATTR_THETA, toDouble(thetaStr));
  }

  public String getSystem() {
    return _avTable.get(ATTR_SYSTEM);
  }

  public void setSystem(String system) {
    _avTable.set(ATTR_SYSTEM, system);
  }

  public String getRasterMode() {
    return _avTable.get(ATTR_RASTER_MODE);
  }

  public void setRasterMode(String value) {
    _avTable.set(ATTR_RASTER_MODE, value);
  }

  public String getRowsPerCal() {
    return _avTable.get(ATTR_ROWS_PER_CAL);
  }

  public void setRowsPerCal(String value) {
    _avTable.set(ATTR_ROWS_PER_CAL, value);
  }

  public String getRowsPerRef() {
    return _avTable.get(ATTR_ROWS_PER_REF);
  }

  public void setRowsPerRef(String value) {
    _avTable.set(ATTR_ROWS_PER_REF, value);
  }

  public boolean getRowReversal() {
    return _avTable.getBool(ATTR_ROW_REVERSAL);
  }

  public void setRowReversal(boolean value) {
    _avTable.set(ATTR_ROW_REVERSAL, value);
  }
}

