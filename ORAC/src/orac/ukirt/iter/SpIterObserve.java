// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter;

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpObs;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTreeMan;
import gemini.sp.SpType;

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterOffset;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import orac.ukirt.inst.SpDRRecipe;

import java.util.Enumeration;
import java.util.Vector;


//
// Enumerater for the elements of the Observe iterator.
//
class SpIterObserveEnumeration extends SpIterEnumeration
{
   private int     _curCount = 0;
   private int     _maxCount;

SpIterObserveEnumeration(SpIterObserve iterObserve)
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
   return _thisNextElement();
}

protected SpIterStep
_thisNextElement()
{
   return new SpIterStep("observe", _curCount++, _iterComp, (SpIterValue) null);
}
   
}


/**
 * A simple "Observe" iterator.
 */
public class SpIterObserve extends SpIterObserveBase implements SpTranslatable
{

   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "observe", "Observe");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterObserve());
}

/**
 * Default constructor.
 */
public SpIterObserve()
{
   super(SP_TYPE);
//   super(SpType.ITERATOR_COMPONENT_OBSERVE);
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

   return "Observe (" + getCount() + "X)";
}

/**
 * Get the Enumation of the iteration steps.
 */
public SpIterEnumeration
elements()
{
   return new SpIterObserveEnumeration(this);
}

public void translate(Vector v) {
    // Get the DR recipe component so we can add the header information
    SpItem parent = parent();
    while ( parent != null && !(parent instanceof SpObs) ) {
        parent = parent.parent();
    }
    Vector recipes = SpTreeMan.findAllItems(parent, "orac.ukirt.inst.SpDRRecipe");
    if ( recipes != null && recipes.size() != 0 ) {
        SpDRRecipe recipe = (SpDRRecipe)recipes.get(0);
        v.add("setHeader GRPMEM " + (recipe.getObjectInGroup()? "T":"F"));
        v.add("setHeader RECIPE " + recipe.getObjectRecipeName());
    }

    // If we are not inside an offset, we need to tell the system there is an offset here
    parent = parent();
    boolean inOffset = false;
    while ( parent != null ) {
        if ( parent instanceof SpIterOffset ) {
            inOffset = true;
            break;
        }
        parent = parent.parent();
    }
    v.add("set OBJECT");
    String observe = "do " + getCount() + " _observe";
    v.add(observe);
    if ( !inOffset ) {
        v.add("ADDOFFSET");
    }
}

}
