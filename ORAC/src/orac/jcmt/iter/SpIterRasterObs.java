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
    SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "rasterObs", "Scan/Raster");

  // Register the prototype.
  static {
    SpFactory.registerPrototype(new SpIterRasterObs());
  }


  /**
   * Default constructor.
   */
  public SpIterRasterObs() {
    super(SP_TYPE);

    _avTable.noNotifySet(ATTR_X_CENTER,     "0.0", 0);
    _avTable.noNotifySet(ATTR_X_CENTER     + ":" + ATTR_UNITS, VALUE_ARC_SECONDS, 0);
    _avTable.noNotifySet(ATTR_Y_CENTER,     "0.0", 0);
    _avTable.noNotifySet(ATTR_Y_CENTER     + ":" + ATTR_UNITS, VALUE_ARC_SECONDS, 0);
    _avTable.noNotifySet(ATTR_WIDTH,        "0.0", 0);
    _avTable.noNotifySet(ATTR_WIDTH        + ":" + ATTR_UNITS, VALUE_ARC_SECONDS, 0);
    _avTable.noNotifySet(ATTR_HEIGHT,       "0.0", 0);
    _avTable.noNotifySet(ATTR_HEIGHT       + ":" + ATTR_UNITS, VALUE_ARC_SECONDS, 0);
    _avTable.noNotifySet(ATTR_RECTANGLE_PA, "0.0", 0);
    _avTable.noNotifySet(ATTR_RECTANGLE_PA + ":" + ATTR_UNITS, VALUE_DEGREES, 0);
    _avTable.noNotifySet(ATTR_OFF_SYSTEM, CoordSys.COORD_SYS[CoordSys.FK5], 0);
  }

  /** Get map x center. */
  public double getXCenter() {
    return _avTable.getDouble(ATTR_X_CENTER, 0.0);
  }

  /** Set map x center. */
  public void setXCenter(double x) {
    _avTable.set(ATTR_X_CENTER, x);
  }

  /** Set map x center. */
  public void setXCenter(String xStr) {
    _avTable.set(ATTR_X_CENTER, toDouble(xStr));
  }

  /** Get map y center. */
  public double getYCenter() {
    return _avTable.getDouble(ATTR_Y_CENTER, 0.0);
  }

  /** Set map y center. */
  public void setYCenter(double y) {
    _avTable.set(ATTR_Y_CENTER, y);
  }

  /** Set map y center. */
  public void setYCenter(String yStr) {
    _avTable.set(ATTR_Y_CENTER, toDouble(yStr));
  }

  /** Get map width. */
  public double getWidth() {
    return _avTable.getDouble(ATTR_WIDTH, 0.0);
  }

  /** Set map width. */
  public void setWidth(double width) {
    _avTable.set(ATTR_WIDTH, width);
  }

  /** Set map width. */
  public void setWidth(String widthStr) {
    _avTable.set(ATTR_WIDTH, toDouble(widthStr));
  }

  /** Get map height. */
  public double getHeight() {
    return _avTable.getDouble(ATTR_HEIGHT, 0.0);
  }

  /** Set map height. */
  public void setHeight(double height) {
    _avTable.set(ATTR_HEIGHT, height);
  }

  /** Set map height. */
  public void setHeight(String heightStr) {
    _avTable.set(ATTR_HEIGHT, toDouble(heightStr));
  }

  public double getRectanglePA() {
    return _avTable.getDouble(ATTR_RECTANGLE_PA, 0.0);
  }

  public void setRectanglePA(double theta) {
    _avTable.set(ATTR_RECTANGLE_PA, theta);
  }

  public void setRectanglePA(String thetaStr) {
    _avTable.set(ATTR_RECTANGLE_PA, toDouble(thetaStr));
  }

  public String getOffSystem() {
    return _avTable.get(ATTR_OFF_SYSTEM);
  }

  public void setOffSystem(String system) {
    _avTable.set(ATTR_OFF_SYSTEM, system);
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

