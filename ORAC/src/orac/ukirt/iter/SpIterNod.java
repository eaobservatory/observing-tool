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
import gemini.sp.iter.SpIterComp;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import gemini.sp.obsComp.SpInstConstants;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpStareCapability;

import java.util.Enumeration;
import java.util.Vector;

import orac.ukirt.inst.SpUKIRTInstObsComp;


//
// The Enumeration of the steps produced by the Nod iterator.
//
class SpIterNodEnumeration extends SpIterEnumeration
{
   private String [] _nodPattern;
   private int       _curIndex = 0;

SpIterNodEnumeration(SpIterNod iterComp)
{
   super(iterComp);
   _nodPattern = iterComp.getNodPattern();
}

protected boolean
_thisHasMoreElements()
{
   return (_curIndex < _nodPattern.length);
}

protected SpIterStep
_thisFirstElement()
{
   return _thisNextElement();
}

protected SpIterStep
_thisNextElement()
{
   SpIterValue[] sivA = {
      new SpIterValue("chop beam", _nodPattern[_curIndex]),
   };
   return new SpIterStep("nod", _curIndex++, _iterComp, sivA);
}
 
}


/**
 * Nod Iterator item.
 *
 * @author modified as Nod Iterator by Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterNod extends SpIterComp
{
   public static final String ATTR_NOD_PATTERN = "nodPattern";

   public static String CHOP_BEAM_A      = "A";
   public static String CHOP_BEAM_B      = "B";
   public static String CHOP_BEAM_MIDDLE = "MIDDLE";

   public static String[][] NOD_PATTERNS = {
      {CHOP_BEAM_A, CHOP_BEAM_B},
      {CHOP_BEAM_A, CHOP_BEAM_B, CHOP_BEAM_B, CHOP_BEAM_A}
   };

   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "nod", "Nod");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterNod());
}


/**
 * Default constructor.
 */
public SpIterNod()
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

   return "Nod";
}

/**
 */
public SpIterEnumeration
elements()
{
   return new SpIterNodEnumeration(this);
}

/**
 * Get the nod pattern as String array.
 *
 * Each String in the array is one of {@link #CHOP_BEAM_A}, {@link #CHOP_BEAM_B}
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
 * Returns Enumeration of nod pattern vectors.
 *
 * Convenience method. Useful for JComboBox in GUI.
 */
public static Enumeration patterns() {
   Vector allPatterns = new Vector();
   Vector onePattern;

   for(int i = 0; i < NOD_PATTERNS.length; i++) {
      onePattern = new Vector();
      for(int j = 0; j < NOD_PATTERNS[i].length; j++) {
         onePattern.add(NOD_PATTERNS[i][j]);
      }
      allPatterns.add(onePattern);
   }

   return allPatterns.elements();
}

public static Vector stringArrayToVector(String [] stringArray) {
   Vector result = new Vector();

   for(int i = 0; i < stringArray.length; i++) {
      result.add(stringArray[i]);
   }
   
   return result;
}

}


