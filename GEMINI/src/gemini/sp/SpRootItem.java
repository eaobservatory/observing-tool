// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import java.io.OutputStream;

/**
 * A root of the program hierarchy.
 *
 * @see SpProg, SpPlan, SpLibrary
 */
public class SpRootItem extends SpObsContextItem
{

/**
 * Default constructor.
 */
protected SpRootItem(SpType spType)
{
   super(spType);
   setEditFSM( new SpEditState(this) );
}

/**
 * Override clone to set the SpEditState.
 */
protected Object
clone()
{
   SpItem spClone = (SpItem) super.clone();
   spClone.setEditFSM( new SpEditState(spClone) );
   return spClone;
}

/**
 * Override getTitle to display the program name.
 */
public String
getTitle()
{
   String title = getTitleAttr();
   if (title == null) {
      title = type().getReadable();
   }

   String nameStr = "";
   if (hasBeenNamed()) {
      nameStr = " (" + name() + ")";
   }

   return title + nameStr;
}

/**
 * Print a Science Program Document describing this item to the
 * given output stream.
 */
public void
printDocument(OutputStream os)
{
   SpOutputSGML out = new SpOutputSGML(os);
   out.printDocument(this);
}

}
