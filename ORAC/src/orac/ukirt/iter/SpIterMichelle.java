// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter;

//import orac.ukirt.inst.SpInstMichelleConstants;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.iter.IterConfigItem;
import gemini.sp.iter.SpIterConfigObs;

/**
 * The Michelle configuration iterator.
 */
public class SpIterMichelle extends SpIterConfigObs
{

   public static final SpType SP_TYPE =
     SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "instMichelle", "Michelle");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterMichelle());
}

/**
 * Default constructor.
 */
public SpIterMichelle()
{
   super(SP_TYPE);
}

/**
 * Get the name of the item being iterated over.  Subclasses must
 * define.
 */
public String
getItemName()
{
   return "Michelle";
}


/**
 * Get the array containing the IterConfigItems offered by Michelle.
 */
public IterConfigItem[]
getAvailableItems()
{

   IterConfigItem[] iciA = {
   };

   return iciA;
}

}
