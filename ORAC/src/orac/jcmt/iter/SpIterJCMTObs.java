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

import orac.jcmt.SpJCMTConstants;


/**
 * Enumerater for the elements of the JCMT Observe iterator.
 *
 * Note that the iteration step enumeration for JCMT Observe Iterators always contains
 * exactly 1 iteration step. In the JCMT OT Observe iterators do not have an "Observe X times"
 * widget. The number of integrations is changed instead. But that does not result in
 * seperate SpIterSteps.
 */
class SpIterJCMTObsEnumeration extends SpIterEnumeration implements SpJCMTConstants
{
   private int _curCount = 0;
   private int _maxCount = 1;
   private SpIterValue[] _values;

SpIterJCMTObsEnumeration(SpIterJCMTObs iterObserve)
{
   super(iterObserve);

   // max count is always 1 for JCMT! It can't be changed
   // In the JCMT OT the number of integrations is changed instead. But that does not
   // result in seperate SpIterSteps.
   _maxCount = 1;
}

protected boolean
_thisHasMoreElements()
{
   return (_curCount < _maxCount);
}

protected SpIterStep
_thisFirstElement()
{
   SpIterJCMTObs ibo         = (SpIterJCMTObs) _iterComp;
   String secsPerIntegration = String.valueOf(ibo.getSecsPerIntegration());
   
   // Number of integrations
   String integrations       = String.valueOf(ibo.getIntegrations());
 
   _values = new SpIterValue[2];
   _values[0] = new SpIterValue(ATTR_SECS_PER_INTEGRATION, secsPerIntegration);
   _values[1] = new SpIterValue(ATTR_INTEGRATIONS, integrations);
 
   return _thisNextElement();
}
 
protected SpIterStep
_thisNextElement()
{
   return new SpIterStep("jcmtObs", _curCount++, _iterComp, _values);
}

}




/**
 * Base class for JCMT Observe Iterators.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterJCMTObs extends SpIterObserveBase implements SpJCMTConstants
{


/**
 * Default constructor.
 */
public SpIterJCMTObs(SpType spType)
{
   super(spType);

   // JCMT Observe "iterators" are not repeated.
   // The number that can be specified on each JCMT observe iterator and that
   // is appended to the name of the iterator in the Science Prrogram tree
   // is the number of iterations, ATTR_INTEGRATIONS.
   _avTable.rm(ATTR_COUNT);
}

/** Get the number of desired integrations. */
public int getIntegrations() { return _avTable.getInt(ATTR_INTEGRATIONS, 1); }

/** Set the number of integrations. */
public void setIntegrations(int    i)	{ _avTable.set(ATTR_INTEGRATIONS, i); }

/** Set the number of integrations from a string. */
public void setIntegrations(String s)	{ _avTable.set(ATTR_INTEGRATIONS, s); }

/**
 * Calculates and returns total integration time (total observe time).
 *
 * On-source plus off-source, cycle time times number of cycles.
 * This is used for time estimation off the observation.
 *
 * This method should be implemented properly by the iterator
 * specific subclasses.
 */
public double getSecsPerIntegration() {
   return 0.0;
}

/**
 * Override getTitle to return the observe count.
 */
public String
getTitle()
{
//   if (getTitleAttr() != null) {
//      return super.getTitle();
//   }

   return type().getReadable() + " (" + getIntegrations() + ")";
}


/** Not supported by JCMT OT. */
public void setCount() {
   throw new UnsupportedOperationException("public SpIterEnumeration SpIterObserveBase.setCount() not supported by JCMT OT.");
}

/** Not supported by JCMT OT. */
public SpIterEnumeration elements() {
   return new SpIterJCMTObsEnumeration(this);
}

/**
 * Override getExposureTime. Get the value from the instrument in
 * scope.
 */
/** Not supported by JCMT OT. */
public double getExposureTime() {
   throw new UnsupportedOperationException("public double SpIterObserveBase.getExposureTime() not supported by JCMT OT.");
}

/** Not supported by JCMT OT. */
public void setExposureTime(double expTime) {
   throw new UnsupportedOperationException("public double SpIterObserveBase.setExposureTime() not supported by JCMT OT.");
}
 
/** Not supported by JCMT OT. */
public void setExposureTime(String expTime) {
   throw new UnsupportedOperationException("public double SpIterObserveBase.setExposureTime() not supported by JCMT OT.");
}

/** Not supported by JCMT OT. */
public int getCoadds() {
   throw new UnsupportedOperationException("public double SpIterObserveBase.getCoadds() not supported by JCMT OT.");
}

/** Not supported by JCMT OT. */
public void setCoadds(int coadds) {
   throw new UnsupportedOperationException("public double SpIterObserveBase.setCoadds() not supported by JCMT OT.");
}
 
/** Not supported by JCMT OT. */
public void setCoadds(String coadds) {
   throw new UnsupportedOperationException("public double SpIterObserveBase.setCoadds() not supported by JCMT OT.");
}

/**
 * Helper method.
 *
 * @return double value of doubleStr if it can be parsed to double, 0.0 otherwise.
 */
protected double toDouble(String doubleStr) {
  double result = 0.0;

  try {
    result = Double.valueOf(doubleStr).doubleValue();
  } catch (Exception ex) { }

  return result;
}


/**
 * Helper method.
 *
 * @return int value of intStr if it can be parsed to double, 0.0 otherwise.
 */
protected int toInt(String intStr) {
  int result = 0;

  try {
    result = Integer.valueOf(intStr).intValue();
  } catch (Exception ex) { }

  return result;
}

}

