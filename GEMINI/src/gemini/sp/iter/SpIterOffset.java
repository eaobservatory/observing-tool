// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.iter;

import gemini.sp.SpAvTable;
import gemini.sp.SpFactory;
import gemini.sp.SpOffsetPos;
import gemini.sp.SpOffsetPosList;
import gemini.sp.SpType;

//import gemini.sp.iter.SpIterComp;
//import gemini.sp.iter.SpIterEnumeration;
//import gemini.sp.iter.SpIterStep;
//import gemini.sp.iter.SpIterValue;

import gemini.util.TelescopePos;

import java.util.Enumeration;
import java.util.Vector;

//
// The Enumeration of the steps produced by the Offset iterator.
//
class SpIterOffsetEnumeration extends SpIterEnumeration
{
   private TelescopePos[] _positions;
   private int            _curIndex = 0;

SpIterOffsetEnumeration(SpIterOffset iterComp)
{
   super(iterComp);
   _positions = iterComp.getPosList().getAllPositions();
}

protected boolean
_thisHasMoreElements()
{
   return (_curIndex < _positions.length);
}

protected SpIterStep
_thisFirstElement()
{
   return _thisNextElement();
}

protected SpIterStep
_thisNextElement()
{
   SpOffsetPos   op   = (SpOffsetPos) _positions[_curIndex];
   SpIterValue[] sivA = {
      new SpIterValue("p", String.valueOf(op.getXaxis())),
      new SpIterValue("q", String.valueOf(op.getYaxis()))
   };
   return new SpIterStep("offset", _curIndex++, _iterComp, sivA);
}
 
}


/**
 * An iterator for telescope offset positions.  It maintains a position
 * list that details the sequence of offset positions and implements the
 * elements() method to Enumerate them.
 * 
 * @see SpOffsetPosList
 */
public class SpIterOffset extends SpIterComp
{
   /**
    * The position list uses the attributes and values contained in
    * this SpItem's attribute table to construct and maintain a list
    * of offset positions.
    */
   protected SpOffsetPosList _posList;

//   public static final SpType SP_TYPE =
//        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "offset", "Offset");
//
//// Register the prototype.
//static {
//   SpFactory.registerPrototype(new SpIterOffset());
//}

/**
 * Default constructor.
 */
public SpIterOffset()
{
//   super(SP_TYPE);
   super(SpType.ITERATOR_COMPONENT_OFFSET);
}

/**
 * Get the position list data structure for the offset positions in
 * the attribute table, creating it if necessary.
 */
public SpOffsetPosList
getPosList()
{
    // Modified by AB, 3-Aug-00 to ALWAYS create a new list. This solved a bug
    // whereby if an offset iterator list was copied *after* being edited and
    // then pasted, the new pasted version would *retain* the old 
    // spOffSetList, thus edits would go to both.  Note that this did *not*
    // occur if the original was not edited first. It is the act of editing
    // that invokes this method and caused the problem.

    //   if (_posList== null) {
    _posList = new SpOffsetPosList(_avTable);
    //}
   return _posList;
}

/**
 * Override setTable() to make sure that the offset position list
 * is in sync with the current attributes and values.
 */
protected void
setTable(SpAvTable avTab)
{
   super.setTable(avTab);
   if (_posList != null) {
      _posList.setTable(avTab);
   }
}
 
/**
 * Override replaceTable() to make sure that the offset position list
 * is in sync with the current attributes and values.
 */
protected void
replaceTable(SpAvTable avTab)
{
   super.replaceTable(avTab);
   if (_posList != null) {
      _posList.setTable(avTab);
   }
}


/**
 * Enumerate the steps of the offset iterator.
 */
public SpIterEnumeration
elements()
{
   return new SpIterOffsetEnumeration(this);
}

}
