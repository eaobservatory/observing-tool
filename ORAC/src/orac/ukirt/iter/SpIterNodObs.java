// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter;

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
import java.util.Vector;

import orac.ukirt.inst.SpUKIRTInstObsComp;

/**
 * Enumerater for the elements of the Observe iterator.
 */
class SpIterNodObsEnumeration extends SpIterEnumeration
{
   private int _curCount = 0;
   private int _maxCount;
   private SpIterValue[] _values;

SpIterNodObsEnumeration(SpIterNodObs iterObserve)
{
   super(iterObserve);
   _maxCount    = iterObserve.getCount();
}

protected boolean
_thisHasMoreElements()
{
   return (_curCount < _maxCount);
}

protected SpIterStep
_thisFirstElement()
{
   SpIterNodObs ibo   = (SpIterNodObs) _iterComp;
   String expTimeValue = String.valueOf(ibo.getExposureTime());
   String coaddsValue  = String.valueOf(ibo.getCoadds());

   _values = new SpIterValue[2];
   _values[0] = new SpIterValue(SpInstConstants.ATTR_EXPOSURE_TIME, expTimeValue);
   _values[1] = new SpIterValue(SpInstConstants.ATTR_COADDS, coaddsValue);

   return _thisNextElement();
}

protected SpIterStep
_thisNextElement()
{
   return new SpIterStep("nod", _curCount++, _iterComp, _values);
}
   
}

/**
 * Nod Iterator item.
 *
 * @author modified as Nod Iterator by Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterNodObs extends SpIterObserveBase
{
   public static final String ATTR_NOD_PATTERN = "nodPattern";

   public static String CHOP_BEAM_MAIN   = "MAIN";
   public static String CHOP_BEAM_OFFSET = "OFFSET";

   public static String[][] NOD_PATTERNS = {
      {CHOP_BEAM_MAIN, CHOP_BEAM_OFFSET},
      {CHOP_BEAM_MAIN, CHOP_BEAM_OFFSET, CHOP_BEAM_MAIN, CHOP_BEAM_OFFSET}
   };

   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "nodObs", "Nod");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterNodObs());
}


/**
 * Default constructor.
 */
public SpIterNodObs()
{
   super(SP_TYPE);
   _avTable.noNotifySetAll(ATTR_NOD_PATTERN, stringArrayToVector(NOD_PATTERNS[0]));
}

/**
 * Override getTitle to return the observe count.
 */
public String
getTitle()
{
   if (getTitleAttr() != null) {
      return super.getTitle();
   }

   return "Nod (" + getCount() + "X)";
}

/**
 */
public SpIterEnumeration
elements()
{
   return new SpIterNodObsEnumeration(this);
}

/**
 * Get the nod pattern as String array.
 *
 * Each String in the array is one of {@link #CHOP_BEAM_MAIN}, {@link #CHOP_BEAM_OFFSET}
 */
public String []
getNodPattern()
{
   String [] result = new String[getNodPatternVector().size()];
   getNodPatternVector().copyInto(result);
   
   return result;
}

/**
 * Get the nod pattern as Vector.
 */
public Vector
getNodPatternVector()
{
   return _avTable.getAll(ATTR_NOD_PATTERN);
}


/**
 * Set the nod pattern from a Vector.
 */
public void
setNodPattern(Vector nodPattern)
{
   _avTable.setAll(ATTR_NOD_PATTERN, nodPattern);
}

/**
 * Set the nod pattern from a String array.
 */
public void
setNodPattern(String [] nodPatternArray)
{
   setNodPattern(stringArrayToVector(nodPatternArray));
}

/**
 * Returns i'th predefined nod pattern.
 *
 * Convenience method. Useful for JComboBox in GUI.
 *
 * Does <b>NOT</b> return the i'th element of the current nod pattern.
 */
public static Vector getNodPattern(int i) {
   return stringArrayToVector(NOD_PATTERNS[i]); 
}

public static Vector stringArrayToVector(String [] stringArray) {
   Vector result = new Vector();

   for(int i = 0; i < stringArray.length; i++) {
      result.add(stringArray[i]);
   }
   
   return result;
}

}


