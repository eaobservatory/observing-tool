// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.iter;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import java.util.Enumeration;

//
// Defines the attributes used by the repeat iterator.
//
interface SpIterRepeatConstants
{
   public static final String COUNT	= "repeatCount";
   public static final int    COUNT_DEF	= 1;
}


//
// Enumeration of the repeat iterator's values.
//
class SpIterRepeatEnumeration extends SpIterEnumeration
                              implements SpIterRepeatConstants
{
   private int     _curCount = 0;
   private int     _maxCount;

SpIterRepeatEnumeration(SpIterRepeat iterRepeat)
{
   super(iterRepeat);
   _maxCount    = iterRepeat.getCount();
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
   SpIterValue siv = new SpIterValue("loop", String.valueOf(_curCount + 1));
   return new SpIterStep("comment", _curCount++, _iterComp, siv);
}
   
}


/**
 * A simple iterator that repeats the steps of nested iterators the
 * specified number of times.
 */
public class SpIterRepeat extends SpIterComp implements SpIterRepeatConstants
{

/**
 * Default constructor.
 */
public SpIterRepeat()
{
   super(SpType.ITERATOR_COMPONENT_REPEAT);
}

/** Override getTitle to return the repeat count. */
public String
getTitle()
{
   return "Repeat (" + getCount() + "X)";
}

/** Get the repeat count. */
public int getCount() { return _avTable.getInt(COUNT, COUNT_DEF); }

/** Set the repeat count as an integer. */
public void setCount(int    i)	{ _avTable.set(COUNT, i); }

/** Set the repeat count as a String. */
public void setCount(String s)	{ _avTable.set(COUNT, s); }

/** Enumerate the values of this iterator.  */
public SpIterEnumeration
elements()
{
   return new SpIterRepeatEnumeration(this);
}

}

