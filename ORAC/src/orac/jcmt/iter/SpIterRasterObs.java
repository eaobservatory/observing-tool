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
import gemini.sp.SpPosAngleObserver;
import gemini.sp.SpObsData;
import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;
import gemini.sp.iter.SpIterOffset;
import gemini.sp.obsComp.SpInstConstants;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpStareCapability;
import gemini.util.Format;
import orac.jcmt.inst.SpJCMTInstObsComp;
import orac.jcmt.inst.SpInstSCUBA;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.util.SpMapItem;

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
public class SpIterRasterObs extends SpIterJCMTObs implements SpPosAngleObserver, SpMapItem {

  /** TCS XML constants. */
  private static final String TX_OBS_AREA      = "obsArea";
  private static final String TX_SCAN_AREA     = "SCAN_AREA";
  private static final String TX_AREA          = "AREA";
  private static final String TX_SCAN          = "SCAN";
  private static final String TX_PA            = "PA";
  private static final String TX_HEIGHT        = "HEIGHT";
  private static final String TX_WIDTH         = "WIDTH";
  private static final String TX_SCAN_VELOCITY = "VELOCITY";
  private static final String TX_SCAN_DY       = "DY";


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
    
    _avTable.noNotifySet(ATTR_SCANAREA_WIDTH,  "0.0", 0);
    _avTable.noNotifySet(ATTR_SCANAREA_HEIGHT, "0.0", 0);

    _avTable.noNotifySet(ATTR_SCANAREA_SCAN_VELOCITY, "0.0", 0);
    _avTable.noNotifySet(ATTR_SCANAREA_SCAN_DY,       "0.0", 0);
    _avTable.noNotifySet(ATTR_SCANAREA_SYSTEM, SCANAREA_SYSTEMS[0], 0);
  }

  /** Get map width. */
  public double getWidth() {
    return _avTable.getDouble(ATTR_SCANAREA_WIDTH, 0.0);
  }

  /** Set map width. */
  public void setWidth(double width) {
    _avTable.set(ATTR_SCANAREA_WIDTH, width);
  }

  /** Set map width. */
  public void setWidth(String widthStr) {
    setWidth(Format.toDouble(widthStr));
  }

  /** Get map height. */
  public double getHeight() {
    return _avTable.getDouble(ATTR_SCANAREA_HEIGHT, 0.0);
  }

  /** Set map height. */
  public void setHeight(double height) {
    _avTable.set(ATTR_SCANAREA_HEIGHT, height);
  }

  /** Set map height. */
  public void setHeight(String heightStr) {
    setHeight(Format.toDouble(heightStr));
  }

  /** Get scan velocity. */
  public double getScanVelocity() {

    // No scan velocity set yet. Try to calculate of the default velocity
    // according to the instrument used.
    if(_avTable.getDouble(ATTR_SCANAREA_SCAN_VELOCITY, 0.0) == 0.0) {
      SpInstObsComp inst = SpTreeMan.findInstrument(this);
      if(inst != null) {
	double scanVelocity = ((SpJCMTInstObsComp)inst).getDefaultScanVelocity();
          _avTable.noNotifySet(ATTR_SCANAREA_SCAN_VELOCITY, "" + scanVelocity, 0);
      }
    }

    return _avTable.getDouble(ATTR_SCANAREA_SCAN_VELOCITY, 0.0);
  }

  /** Set scan velocity. */
  public void setScanVelocity(double value) {
    _avTable.set(ATTR_SCANAREA_SCAN_VELOCITY, value);
  }

  /** Set scan velocity. */
  public void setScanVelocity(String value) {
    _avTable.set(ATTR_SCANAREA_SCAN_VELOCITY, Format.toDouble(value));
  }


  /**
   * Get scan dx.
   *
   * Calculates scan dx in an instrument specific way.
   *
   * @throws java.lang.UnsupportedOperationException No instrument in scope.
   */
  public double getScanDx() throws UnsupportedOperationException {
    SpInstObsComp inst = SpTreeMan.findInstrument(this);
    if(inst == null) {
      throw new UnsupportedOperationException("Could not find instrument in scope.\n" +
                                               "Needed for calculation of scan dx.");
    }
    else {
      if(inst instanceof SpInstSCUBA) {
        return getScanVelocity() / ((SpInstSCUBA)inst).getChopFrequency();
      }

      if(inst instanceof SpInstHeterodyne) {
        // to be implemented
      }

      return 0.0;
    }
  }

  /**
   * Set scan dx.
   *
   * Sets scan in an instrument specific way.

   * @throws java.lang.UnsupportedOperationException No instrument in scope.
   */
  public void setScanDx(double dx) throws UnsupportedOperationException {
    SpInstObsComp inst = SpTreeMan.findInstrument(this);
    if(inst == null) {
      throw new UnsupportedOperationException("Could not find instrument in scope.\n" +
                                               "Needed for calculation of scan velocity.");
    }
    else {
      _avTable.set(ATTR_SCANAREA_SCAN_VELOCITY, ((SpInstSCUBA)inst).getChopFrequency() * dx);
    }
  }

  /**
   * Set scan dx.
   *
   * Sets scan in an instrument specific way.
   *
   * @throws java.lang.UnsupportedOperationException No instrument in scope.
   */
  public void setScanDx(String dx) throws UnsupportedOperationException {
    SpInstObsComp inst = SpTreeMan.findInstrument(this);
    if(inst == null) {
      throw new UnsupportedOperationException("Could not find instrument in scope.\n" +
                                               "Needed for calculation of scan velocity.");
    }
    else {
      _avTable.set(ATTR_SCANAREA_SCAN_VELOCITY, ((SpInstSCUBA)inst).getChopFrequency() * Format.toDouble(dx));
    }   
  }

  /** Get scan dy. */
  public double getScanDy() {
    // No scan velocity set yet. Try to calculate of the default velocity
    // according to the instrument used.
    if(_avTable.getDouble(ATTR_SCANAREA_SCAN_DY, 0.0) == 0.0) {
      SpInstObsComp inst = SpTreeMan.findInstrument(this);
      if(inst != null) {
	  double scanDy = ((SpJCMTInstObsComp)inst).getDefaultScanDy();
          _avTable.noNotifySet(ATTR_SCANAREA_SCAN_DY, "" + scanDy, 0);
      }
    }

    return _avTable.getDouble(ATTR_SCANAREA_SCAN_DY, 0.0);
  }


  /** Set scan dy. */
  public void setScanDy(double dy) {
    _avTable.set(ATTR_SCANAREA_SCAN_DY, dy);
  }

  /** Set scan dy. */
  public void setScanDy(String dy) {
    _avTable.set(ATTR_SCANAREA_SCAN_DY, Format.toDouble(dy));
  }


  /**
   * Get Area PA.
   *
   * Refers to TCS XML:
   * <pre>
   * &lt;SCAN_AREA&gt;
   *   &lt;AREA&gt;
   *     &lt;PA&gt;0.0&lt;/PA&gt;
   *   &lt;/AREA&gt;
   * &lt;SCAN_AREA&gt;
   * </pre>
   */
  public double getPosAngle() {
    return _avTable.getDouble(ATTR_SCANAREA_PA, 0.0);
  }

  /**
   * Set Area PA.
   *
   * Refers to TCS XML:
   * <pre>
   * &lt;SCAN_AREA&gt;
   *   &lt;AREA&gt;
   *     &lt;PA&gt;0.0&lt;/PA&gt;
   *   &lt;/AREA&gt;
   * &lt;SCAN_AREA&gt;
   * </pre>
   */
  public void setPosAngle(double theta) {
    _avTable.set(ATTR_SCANAREA_PA, theta);

    if(_parent instanceof SpIterOffset) {
      ((SpIterOffset)_parent).setPosAngle(theta);
    }
  }

  /**
   * Set Area PA.
   *
   * Refers to TCS XML
   * <pre>
   * &lt;SCAN_AREA&gt;
   *   &lt;AREA&gt;
   *     <b>&lt;PA&gt;0.0&lt;/PA&gt;</b>
   *   &lt;/AREA&gt;
   * &lt;SCAN_AREA&gt;
   * </pre>
   */
  public void setPosAngle(String thetaStr) {
    setPosAngle(Format.toDouble(thetaStr));
  }

  /**
   * Get Area System.
   *
   * Refers to TCS XML:
   * <pre>
   * &lt;SCAN_AREA&gt;
   *   &lt;AREA <b>SYSTEM="TRACKING"</b>&gt;
   *   &lt;/AREA&gt;
   * &lt;SCAN_AREA&gt;
   * </pre>
   */
  public String getScanAreaSystem() {
    return _avTable.get(ATTR_SCANAREA_SYSTEM);
  }

  /**
   * Set Area System.
   *
   * Refers to TCS XML:
   * <pre>
   * &lt;SCAN_AREA&gt;
   *   &lt;AREA <b>SYSTEM="TRACKING"</b>&gt;
   *   &lt;/AREA&gt;
   * &lt;SCAN_AREA&gt;
   * </pre>
   */
  public void setScanAreaSystem(String system) {
    _avTable.set(ATTR_SCANAREA_SYSTEM, system);
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

  public void posAngleUpdate(double posAngle) {
    // Do not use setPosAngle(posAngle) here as it would reset the posAngle of the class
    // calling posAngleUpdate(posAngle) which would then call posAngleUpdate(posAngle)
    // again an so on causing an infinite loop.
    _avTable.set(ATTR_SCANAREA_PA, posAngle);
  }


  /** Creates JAC TCS XML. */
  protected void processAvAttribute(String avAttr, String indent, StringBuffer xmlBuffer) {
    // ATTR_SCANAREA_HEIGHT is an AV attribute that occurs once in a SpIterOffset's AV table
    // When processAvAttribute is called with ATTR_SCANAREA_HEIGHT as avAttr then append the entire
    // TCS XML representation of this item to the xmlBuffer.
    // For all other calls to processAvAttribute ignore the AV attributes, except meta attribues
    // such as ".gui.collapsed" which are delegated to the super class.
    if(avAttr.equals(ATTR_SCANAREA_HEIGHT)) {
      // Append <obsArea> element.
      xmlBuffer.append("\n" + indent + "  <" + TX_OBS_AREA + ">");
      xmlBuffer.append("\n" + indent + "    <" + TX_PA + ">" + getPosAngle() + "</" + TX_PA + ">");

      xmlBuffer.append("\n" + indent + "    <" + TX_SCAN_AREA + ">");
      xmlBuffer.append("\n" + indent + "      <" + TX_AREA + " " + TX_HEIGHT + "=\"" + getHeight() +
                                                 "\" " + TX_WIDTH + "=\"" + getWidth()  + "\"/>");
      xmlBuffer.append("\n" + indent + "      <" + TX_SCAN + " " + TX_SCAN_DY + "=\"" + getScanDy() +
	                                     "\" " + TX_SCAN_VELOCITY + "=\"" + getScanVelocity() + "\"/>");
      xmlBuffer.append("\n" + indent + "    </" + TX_SCAN_AREA + ">");

      xmlBuffer.append("\n" + indent + "  </" + TX_OBS_AREA + ">");

      return;
    }


    if(avAttr.startsWith(TX_SCAN_AREA)) {
      // Ignore. Dealt with in <obsArea> element (see above).
      return;
    }

    super.processAvAttribute(avAttr, indent, xmlBuffer);
  }

  /** JAC TCS XML parsing. */
  public void processXmlElementContent(String name, String value) {

    // Ignore XML elements whose do not contain characters themselves but only
    // XML attributes or XML child elements.
    if(name.equals(TX_OBS_AREA) ||
       name.equals(TX_SCAN_AREA) ||
       name.equals(TX_AREA) ||
       name.equals(TX_SCAN)) {

      // ignore
      return;
    }

    if(name.equals(TX_PA)) {
      setPosAngle(value);

      return;
    }

    super.processXmlElementContent(name, value);
  }

  /** JAC TCS XML parsing. */
  public void processXmlElementEnd(String name) {

    if(name.equals(TX_OBS_AREA)) {
      // save() just means reset() in this context.
      getAvEditFSM().save();

      return;
    }

    super.processXmlElementEnd(name);
  }

  /** JAC TCS XML parsing. */
  public void processXmlAttribute(String elementName, String attributeName, String value) {
    if(elementName.equals(TX_AREA)) {
      if(attributeName.equals(TX_HEIGHT)) {
        setHeight(value);
	return;
      }

      if(attributeName.equals(TX_WIDTH)) {
        setWidth(value);
	return;
      }
    }

    if(elementName.equals(TX_SCAN)) {
      if(attributeName.equals(TX_SCAN_DY)) {
        setScanDy(value);
	return;
      }

      if(attributeName.equals(TX_SCAN_VELOCITY)) {
        setScanVelocity(value);
	return;
      }
    }

    super.processXmlAttribute(elementName, attributeName, value);
  }
}

