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
 * Scan iterator for SCUBA/JCMT.
 *
 * The Raster iterator (ACSIS) and the Scan iterator share a lot of fuctionality
 * and should in future be either made the same class or share code by other
 * means such as inheritance.
 *
 * @see orac.jcmt.iter.SpIterRasterObs
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterScanObs extends SpIterJCMTObs
{

  public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "scanObs", "Scan");

  // Register the prototype.
  static {
    SpFactory.registerPrototype(new SpIterScanObs());
  }


  /**
   * Default constructor.
   */
  public SpIterScanObs() {
    super(SP_TYPE);

    _avTable.noNotifySet(ATTR_X,           "0.0", 0);
    _avTable.noNotifySet(ATTR_Y,           "0.0", 0);
    _avTable.noNotifySet(ATTR_THETA,       "0.0", 0);
    _avTable.noNotifySet(ATTR_SYSTEM,      CoordSys.COORD_SYS[CoordSys.FK5], 0);
    _avTable.noNotifySet(ATTR_DELTA_X,     "0.0", 0);
    _avTable.noNotifySet(ATTR_DELTA_Y,     "0.0", 0);
    _avTable.noNotifySet(ATTR_COORD_FRAME, CoordSys.COORD_SYS[CoordSys.FK5], 0);
    _avTable.noNotifySet(ATTR_POS_ANGLE,   "0.0", 0);

    _avTable.noNotifySet(ATTR_POS_ANGLE + ":" + ATTR_UNITS, VALUE_ARC_MINUTES, 0);
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

  /** Get sample DX. */
  public double getDeltaX() {
    return _avTable.getDouble(ATTR_DELTA_X, 0.0);
  }

  /** Set sample DX. */
  public void setDeltaX(double deltaX) {
    _avTable.set(ATTR_DELTA_X, deltaX);
  }

  /** Set sample DX. */
  public void setDeltaX(String deltaXStr) {
    _avTable.set(ATTR_DELTA_X, toDouble(deltaXStr));
  }

  /** Get sample DY. */
  public double getDeltaY() {
    return _avTable.getDouble(ATTR_DELTA_Y, 0.0);
  }

  /** Set sample DY. */
  public void setDeltaY(double deltaY) {
    _avTable.set(ATTR_DELTA_Y, deltaY);
  }

  /** Set sample DY. */
  public void setDeltaY(String deltaYStr) {
    _avTable.set(ATTR_DELTA_Y, toDouble(deltaYStr));
  }

  public String getCoordFrame() {
    return _avTable.get(ATTR_COORD_FRAME);
  }

  public void setCoordFrame(String coordFrame) {
    _avTable.set(ATTR_COORD_FRAME, coordFrame);
  }

  public double getPosAngle() {
    return _avTable.getDouble(ATTR_POS_ANGLE, 0.0);
  }

  public void setPosAngle(double posAngle) {
    _avTable.set(ATTR_POS_ANGLE, posAngle);
  }

  public void setPosAngle(String posAngleStr) {
    _avTable.set(ATTR_POS_ANGLE, toDouble(posAngleStr));
  }

  public double getSecsPerIntegration() {
    double overhead = 40 + (8 * getIntegrations());

    // Get information specified by user in the OT.
    double mapWidth           = getX();
    double mapHeight          = getY();
    double sampleDY           = getDeltaX();  
    double sampleDX           = getDeltaY();

    // Calculate seconds per integration.
    double scanRate           = sampleDX * SCAN_MAP_CHOP_FREQUENCY;
    double noOfRows           = Math.ceil(mapHeight / sampleDY);
    double lengthOfRow        = mapWidth + SCUBA_ARRAY_DIAMETER;
    double secsPerRow         = lengthOfRow / scanRate;
    double secsPerIntegration = noOfRows * secsPerRow;

    // Overhead is 50 percent for scan map.
    return SCUBA_STARTUP_TIME +  (1.5 * secsPerIntegration);
  }
}

