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
import gemini.sp.SpItem;
import gemini.sp.SpPosAngleObserver;
import gemini.sp.SpObsData;

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
    * Data structure for maintaining position angle and SpPosAngleObservers.
    *
    * Note that this is <b>not</b> the SpObsData object that is returned by getObsData().
    */
   private SpObsData _posAngleObsData = new SpObsData();

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

   /** Needed for XML parsing. */
   private double xOffNew = 0.0;

   /** Needed for XML parsing. */
   private double yOffNew = 0.0;


/**
 * Default constructor.
 */
public SpIterOffset()
{
//   super(SP_TYPE);
   super(SpType.ITERATOR_COMPONENT_OFFSET);
}

/**
 * Create and return the position list data structure for the offset positions in
 * the attribute table.
 *
 * Modified by AB, 3-Aug-00 to <b>ALWAYS</b> create a new list. This solved a bug
 * whereby if an offset iterator list was copied <b>after</b> being edited and
 * then pasted, the new pasted version would <b>retain</b> the old 
 * spOffSetList, thus edits would go to both.  Note that this did <b>not</b>
 * occur if the original was not edited first. It is the act of editing
 * that invokes this method and caused the problem.
 *
 * @see #getCurrentPosList()
 */
public SpOffsetPosList
getPosList()
{
    // See comment above
    //   if (_posList== null) {
    _posList = new SpOffsetPosList(_avTable);
    //}
    if (_posList.size() == 0) _posList.createPosition(0.0,0.0);

   return _posList;
}

/**
 * Get the current position list data structure for the offset positions in
 * the attribute table, creating it <b>only</b> if necessary.
 *
 * This method is used by the Telescope Position Editor (TPE). Using
 * {@link #getPosList()} in the TPE would break the link between the
 * offset iterator and the TPE so that updating one from the other
 * would no longer work.
 *
 * @see #getPosList()
 */
public SpOffsetPosList
getCurrentPosList()
{
    if (_posList== null) {
      _posList = new SpOffsetPosList(_avTable);
      
    }
   return _posList;
}


/** Get the Position Angle. */
public double getPosAngle()
{
   return getCurrentPosList().getPosAngle();
}

/** Set the Position Angle as double. */
public void setPosAngle(double pa)
{
   getCurrentPosList().setPosAngle(pa);

   _posAngleObsData.setPosAngle(pa);

   // SdW - Look for children which implement implement SpPosAngleObserver
   Enumeration e = children();
   while ( e.hasMoreElements() ) {
       SpItem child = (SpItem)e.nextElement();
       Class [] interfaces = child.getClass().getInterfaces();
       for (int i=0; i < interfaces.length; i++) {
	   if (interfaces[i].getName().indexOf("SpPosAngleObserver") != -1) {
	       ((SpPosAngleObserver)child).posAngleUpdate(pa);
	   }
       }
   }
   // END
}


/** Set the Position Angle as String. */
public void setPosAngle(String pa)
{
   try {
      setPosAngle(Double.parseDouble(pa));
   }
   catch(Exception e) {
      setPosAngle(0.0);
   }
}


/**
 * Add a position angle observer.
 */
public void
addPosAngleObserver(SpPosAngleObserver pao)
{
   _posAngleObsData.addPosAngleObserver(pao);
}

/**
 * Remove a position angle observer.
 */
public void
deletePosAngleObserver(SpPosAngleObserver pao)
{
   _posAngleObsData.deletePosAngleObserver(pao);
}

/**
 * Connects SpPosAngleObservers if there are any amoung the new children.
 */
protected void
insert(SpItem[] newChildren, SpItem afterChild)
{
   // Check for SpPosAngleObservers amoung the newChildren and
   // add them as SpPosAngleObservers.
   for(int i = 0; i < newChildren.length; i++) {
      if(newChildren[i] instanceof SpPosAngleObserver) {
	 addPosAngleObserver((SpPosAngleObserver)newChildren[i]);
      }
   }

   super.insert(newChildren, afterChild);
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


protected void
processAvAttribute(String avAttr, String indent, StringBuffer xmlBuffer)
{
   // "offsetPositions" (SpOffsetPosList.OFFSET_POS_LIST)
   // is an AV attribute that occurs once in a SpIterOffset's AV table
   // When processAvAttribute is called with "offsetPositions" as avAttr then append the entire
   // TCS XML representation of this item to the xmlBuffer.
   // For all other calls to processAvAttribute ignore the AV attributes
   // "PA" (SpOffsetPosList.ATTR_POS_ANGLE) and those attributes starting with "Offset" (SpOffsetPos.OFFSET_TAG)
   // as their information has already been dealt with in the TCS XML representation of
   // this item.
   // The other attributes are delegated to the super class.
   if(avAttr.equals(SpOffsetPosList.OFFSET_POS_LIST)) {
      // Append <obsArea> element.
      xmlBuffer.append("\n" + indent + "  <obsArea>");
      xmlBuffer.append("\n" + indent + "    <PA>" + getPosAngle() + "</PA>");

      for(int i = 0; i < _posList.size(); i++) {
         xmlBuffer.append("\n" + indent + "    <OFFSET>");
         xmlBuffer.append("\n" + indent + "      <DC1>" + _posList.getPositionAt(i).getXaxis() + "</DC1>");
         xmlBuffer.append("\n" + indent + "      <DC2>" + _posList.getPositionAt(i).getYaxis() + "</DC2>");
         xmlBuffer.append("\n" + indent + "    </OFFSET>");
      }

      xmlBuffer.append("\n" + indent + "  </obsArea>");

      return;
   }


   if(avAttr.equals(SpOffsetPosList.ATTR_POS_ANGLE) || avAttr.startsWith(SpOffsetPos.OFFSET_TAG)) {
      // Ignore. Dealt with in <obsArea> element (see above).
      return;
   }

   super.processAvAttribute(avAttr, indent, xmlBuffer);
}


public void
processXmlElementContent(String name, String value)
{

   if(name.equals("obsArea") || name.equals("OFFSET")) {
      // ignore
      return;
   }

   if(name.equals("PA")) {
      setPosAngle(value);

      return;
   }


   if(name.equals("DC1")) {
      try {
         xOffNew = Double.parseDouble(value);
      }
      catch(Exception e) {
         xOffNew = 0.0;
      }

      return;
   }

   if(name.equals("DC2")) {
      try {
         yOffNew = Double.parseDouble(value);
      }
      catch(Exception e) {
         yOffNew = 0.0;
      }

      return;
   }

   super.processXmlElementContent(name, value);
}

public void
processXmlElementEnd(String name)
{
   if(name.equals("OFFSET")) {
      _posList.createPosition(xOffNew, yOffNew);
      xOffNew = 0.0;
      yOffNew = 0.0;

      return;
   }

   if(name.equals("obsArea")) {
      // save() just means reset() in this context.
      getAvEditFSM().save();

      return;
   }

   super.processXmlElementEnd(name);
}


}

