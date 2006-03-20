// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.obsComp;

import gemini.sp.SpAvTable;
import gemini.sp.SpItem;
import gemini.sp.SpMSB;
import gemini.sp.SpObs;
import gemini.sp.SpObsData;
import gemini.sp.SpProg;
import gemini.sp.SpTreeMan;
import gemini.sp.SpType;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterOffset;

import gemini.util.Angle;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * A base class for instrument observation component items.  One of
 * the principal tasks of this component is to keep up-to-date the
 * position angle element of the observation data for the observation
 * context.
 *
 * @see gemini.sp.SpObsData
 * @see gemini.sp.SpObsContextItem
 */
public abstract class SpInstObsComp extends SpObsComp
                                 implements SpInstConstants
{
   Hashtable _capabilityH = new Hashtable();

/**
 * Construct the SpInstObsComp with its exact subtype.
 */
public SpInstObsComp(SpType spType)
{
   super(spType);
}

/**
 * Add a capability to this instrument.
 */
public void
addCapability(SpInstCapability cap)
{
   cap.setAvTable(_avTable);
   _capabilityH.put(cap.getName(), cap);
}

/**
 * Remove a capability from this instrument.  Note this does
 * remove any attributes associated with the capability that have
 * already been stored in the instrument's attribute/value table.
 */
public void
removeCapability(SpInstCapability cap)
{
   cap.setAvTable(null);
   _capabilityH.remove(cap.getName());
}

/**
 * Get a capability with the given name.
 */
public SpInstCapability
getCapability(String name)
{
   return (SpInstCapability) _capabilityH.get(name);
}

/**
 * Get an enumeration of all the capability names.
 */
public Enumeration
getCapabilityNames()
{
   return _capabilityH.keys();
}

/**
 * Override clone to clone all the capabilities.
 */
protected Object
clone()
{
   SpInstObsComp spClone = (SpInstObsComp) super.clone();
   SpAvTable     avClone = spClone.getTable();

   // Clone each capability, setting its avTable correctly.
   Vector      v    = new Vector();
   Enumeration caps = _capabilityH.elements();
   while (caps.hasMoreElements()) {
      SpInstCapability cap      = (SpInstCapability) caps.nextElement();
      v.addElement(cap.copy(avClone));
   }

   // Place each cloned capability in the cloned component's table.
   spClone._capabilityH = new Hashtable();
   for (int i=0; i<v.size(); ++i) {
      SpInstCapability cap = (SpInstCapability) v.elementAt(i);
      spClone._capabilityH.put(cap.getName(), cap);
   }

   return spClone;
}

/**
 * Get the science area in arcsec x arcsec.  For now, this must
 * be a rectangular region.
 */
public double[]
getScienceArea()
{
   return new double[] { -1.0, -1.0};
}

/**
 * Set the exposure time.
 */
public final void
setExposureTime(double seconds)
{
   setExposureTime(Double.toString(seconds));
}

/**
 * Set the exposure time.
 */
public final void
setExposureTime(String seconds)
{
   _avTable.set(ATTR_EXPOSURE_TIME, seconds);
}

/**
 * Get the exposure time.
 */
public final double
getExposureTime()
{
   String time;

   // For old programs, change "integrationTime" to "exposureTime".
   time = _avTable.get("integrationTime");
   if (time != null) {
      setExposureTime(time);
      _avTable.rm("integrationTime");
   } else {
      time = _avTable.get(ATTR_EXPOSURE_TIME);
   }

   if (time == null) {
      time = "0";
   }

   double res = 0.0;
   try {
      res = Double.valueOf(time).doubleValue();
   } catch (Exception ex) {}
   return res;
}

/**
 * Get the exposure time as a string.
 */
public final String
getExposureTimeAsString()
{
   // For old programs, change "integrationTime" to "exposureTime".
   String intTime = _avTable.get("integrationTime");
   if (intTime != null) {
      setExposureTime(intTime);
      _avTable.rm("integrationTime");
      return intTime;
   }

   String res = _avTable.get(ATTR_EXPOSURE_TIME);
   if (res == null) {
      return "0";
   }
   return res;
}

public int getCoadds() {
    String coadds;
    int res = 1;
    coadds = _avTable.get(ATTR_COADDS);
    if (coadds != null) {
	try {
	    res = Integer.valueOf(coadds).intValue();
	}
	catch (Exception x) {}
    }
    return res;
}

/**
 * Set the position angle in degrees from due north, updating the
 * observation data with the new position angle.  This method is
 * ultimately called by the other setPosAngle methods.
 * Made not final by AB for ORAC. 4-May-2000, to allow overriding.
 */
public void
setPosAngleDegrees(double posAngle)
{
  // Comment out the normalisation.  For UKIRT assume we are getting 
  // the correct answer(!!)
  //   posAngle = Angle.normalizeDegrees(Math.round(posAngle));
   _avTable.set(ATTR_POS_ANGLE, posAngle);
   _updateObsData(posAngle);
   // Hacky attempt to fix up offsets for UKIRT
   if ( "ukirt".equalsIgnoreCase( System.getProperty("TELESCOPE") ) ) {
       SpItem parent = parent();
       Vector offsets;
       while ( parent != null ) {
           if ( parent instanceof SpObs ) {
               // Get all the child offset iterator, and simply update their PA
               offsets = SpTreeMan.findAllInstances( parent, "gemini.sp.iter.SpIterOffset" );
               if ( offsets != null ) {
                   for (int i=0; i<offsets.size(); i++) {
                       ((SpIterOffset)offsets.get(i)).getPosList().setPosAngle(posAngle);
                   }
               }
               break;
           }
           else if ( parent instanceof SpMSB || parent instanceof SpProg ) {
               offsets = SpTreeMan.findAllInstances( parent, "gemini.sp.iter.SpIterOffset" );
               // For each of these, find any whose instrument is this and update them
               if ( offsets != null ) {
                   for ( int i=0; i<offsets.size(); i++ ) {
                       SpIterOffset thisOffset = (SpIterOffset)offsets.get(i);
                       if ( SpTreeMan.findInstrument(thisOffset) == this ) {
                           thisOffset.getPosList().setPosAngle(posAngle);
                       }
                   }
               }
               break;
           }
           parent = parent.parent();
       }
   }
}

/**
 * Set the position angle in radians from due north.
 */
public void
setPosAngleRadians(double posAngle)
{
   setPosAngleDegrees(Angle.radiansToDegrees(posAngle));
}

/**
 * Set the rotation of the science area as a string (representing degrees).
 */
public void
setPosAngleDegreesStr(String posAngleStr)
{
   double posAngle = 0.0;
   try {
      posAngle = Double.valueOf(posAngleStr).doubleValue();
   } catch (Exception ex) { }

   _avTable.set(ATTR_POS_ANGLE, posAngleStr);
   _updateObsData(posAngle);
}

/**
 * Add the given angle to the current rotation angle.
 */
public final void
addPosAngleDegrees(double addAngle)
{
   double angle = getPosAngleDegrees();
   angle += addAngle;
   setPosAngleDegrees(angle);
}

/**
 * Add the given angle to the current rotation angle.
 */
public final void
addPosAngleRadians(double addAngle)
{
   double angle = getPosAngleRadians();
   angle += addAngle;
   setPosAngleRadians(angle);
}

/**
 * Get the rotation of the science area (in radians) from due north.
 */
public final double
getPosAngleDegrees()
{
   // For old programs, change "rotAngle" to "posAngle".
   String posAngleStr = _avTable.get("rotAngle");
   if (posAngleStr != null) {
      setPosAngleDegreesStr(posAngleStr);
      _avTable.rm("rotAngle");
   }

   return _avTable.getDouble(ATTR_POS_ANGLE, 0.0);
}

/**
 * Get the rotation of the science area (in radians) from due north.
 */
public final double
getPosAngleRadians()
{
   return Angle.degreesToRadians(getPosAngleDegrees());
}

/**
 * Get the rotation of the science area as a string (in radians) from
 * due north.
 */
public final String
getPosAngleRadiansStr()
{
   double val = getPosAngleRadians();
   return Double.toString(val);
}

/**
 * Get the rotation of the science area as a string (in degrees) from
 * due north.
 */
public final String
getPosAngleDegreesStr()
{
   double val = getPosAngleDegrees();
   return Double.toString(val);
}

/**
 * Update the SpObsData position angle information.
 */
private void
_updateObsData(double posAngle)
{
   SpObsData od = getObsData();
   if (od != null) {
      od.setPosAngle(posAngle);
   }
}

/**
 * Override setTable to update the position angle.
 */
protected void
setTable(SpAvTable avTable)
{
   Enumeration caps = _capabilityH.elements();
   while (caps.hasMoreElements()) {
      SpInstCapability cap = (SpInstCapability) caps.nextElement();
      cap.setAvTable(avTable);
   }
   super.setTable(avTable);
   _updateObsData(getPosAngleDegrees());
}

/**
 * Override replaceTable to update the position angle.
 */
protected void
replaceTable(SpAvTable avTable)
{
   Enumeration caps = _capabilityH.elements();
   while (caps.hasMoreElements()) {
      SpInstCapability cap = (SpInstCapability) caps.nextElement();
      cap.setAvTable(avTable);
   }
   super.replaceTable(avTable);
   _updateObsData(getPosAngleDegrees());
}

/**
 * Overhead time for doing an exposure.
 *
 * Overhead time associated with an exposure (see {@link #getExposureTime()} and {@link #setExposureTime(double)}).
 * cause by reset time, read time, NDR reset delay etc.
 * <P>
 * This method is used for MSB duration estimation. Subclasses can override this method to
 * return an  instrument specific value.
*/
public double
getExposureOverhead()
{
   return 0.0;
}

/**
 * Returns a time estimate for slewing the telescope depending on the instrument settings.
 *
 * Should overriden by subclasses.
 */
public double
getSlewTime()
{
   return 0.0;
}

/**
  * returns time estimate in seconds for acquiring the source.  Should
  * be overridden by subclasses
  */
public double getAcqTime() {
    return 0.0;
}

public Hashtable getConfigItems() {
    return new Hashtable();
}


  /**
   * This is a helper class for time estimation.
   *
   * Subclasses of SpInstObsComp can implement their own inner subclasses
   * of the IterationTracker class. These iteration tracker classes are used to
   * keep track of the instrument specific parameters over which instrument iterators
   * iterate. The instrument component itself (outer class, subclass of SpInstObsComp)
   * does not hold any knowledge of what the current value of, say, exposure time during
   * iteration. But the might be instrument specific functionality implemented in the
   * instrument component (outer class, subclass of SpInstObsComp) that can be used by
   * the IterationTracker to calculate the estimated elapsed time based on the current values
   * of the parameters that are being iterated over. That is way it is useful to have
   * IterationTracker as an inner class of SpInstObsComp.
   */
  public class IterationTracker {
    public IterationTracker() { }

    public void update(SpIterStep step) {

    }

    public double getObserveStepTime() {
      return 0.0;
    }
  }

  /**
   * Create an instrument specific instance of an IterationTracker.
   */
  public IterationTracker createIterationTracker() {
    return new IterationTracker();
  }
  
}

