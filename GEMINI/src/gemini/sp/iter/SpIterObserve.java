// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.iter;

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpType;

//import gemini.sp.iter.SpIterEnumeration;
//import gemini.sp.iter.SpIterObserveBase;
//import gemini.sp.iter.SpIterStep;
//import gemini.sp.iter.SpIterValue;

import java.util.Enumeration;


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
public class SpIterObserve extends SpIterObserveBase
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

}
