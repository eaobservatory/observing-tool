// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter;

import orac.ukirt.inst.SpInstMichelleConstants;

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
   IterConfigItem iciCamera = new IterConfigItem(
	"Camera",
	SpInstMichelleConstants.ATTR_CAMERA + "Iter",
	SpInstMichelleConstants.CAMERAS);
 
   IterConfigItem iciDisperser = new IterConfigItem(
	"Disperser",
	SpInstMichelleConstants.ATTR_DISPERSER + "Iter",
	SpInstMichelleConstants.DISPERSERS);
 
   IterConfigItem iciMask = new IterConfigItem(
	"Mask",
	SpInstMichelleConstants.ATTR_MASK + "Iter",
	SpInstMichelleConstants.MASKS);

   IterConfigItem iciPosAngle = new IterConfigItem(
	"Pos. Angle",
	SpInstMichelleConstants.ATTR_POS_ANGLE + "Iter",
	null);

   IterConfigItem iciCentWavelength = new IterConfigItem(
	"Central Wavelen.",
	SpInstMichelleConstants.ATTR_CENTRAL_WAVELENGTH + "Iter",
	null);

   // Filters
   int n = SpInstMichelleConstants.FILTERS.length;
   String[] filters = new String[n];
   for (int i=0; i<n; ++i) {
      filters[i] = SpInstMichelleConstants.FILTERS[i][0];
   }

   IterConfigItem iciFilter = new IterConfigItem(
	"Filter",
	SpInstMichelleConstants.ATTR_FILTER + "Iter",
	filters);
 

   IterConfigItem[] iciA = {
	iciCamera, iciFilter,
        getExposureTimeConfigItem(), getCoaddsConfigItem(), iciPosAngle,
        iciDisperser, iciMask, iciCentWavelength
   };

   return iciA;
}

}
