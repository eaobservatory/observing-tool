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

import java.util.Enumeration;


//
// Enumerater for the elements of the Sky iterator.
//
class SpIterSkyEnumeration extends SpIterEnumeration
{
   private int     _curCount = 0;
   private int     _maxCount;

SpIterSkyEnumeration(SpIterSky iterSky)
{
   super(iterSky);
   _maxCount    = iterSky.getCount();
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
   return new SpIterStep("sky", _curCount++, _iterComp, (SpIterValue) null);
}
   
}


/**
 * A simple "Sky" iterator.
 */
public class SpIterSky extends SpIterObserveBase
{

   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "sky", "Sky");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterSky());
}

/**
 * Default constructor.
 */
public SpIterSky()
{
   super(SP_TYPE);
//   super(SpType.ITERATOR_COMPONENT_SKY);
}

/**
 * Override getTitle to return the sky count.
 */
public String
getTitle()
{
   if (getTitleAttr() != null) {
      return super.getTitle();
   }

   return "Sky (" + getCount() + "X)";
}

/**
 * Get the Enumation of the iteration steps.
 */
public SpIterEnumeration
elements()
{
   return new SpIterSkyEnumeration(this);
}

}
