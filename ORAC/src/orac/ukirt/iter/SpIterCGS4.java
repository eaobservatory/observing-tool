/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package orac.ukirt.iter;

import java.util.*;

import orac.ukirt.util.*;
import orac.ukirt.inst.SpInstCGS4;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.iter.IterConfigItem;
import gemini.sp.iter.SpIterConfigObs;

/**
 * The CGS4 configuration iterator.
 */
public class SpIterCGS4 extends SpIterConfigObsUKIRT
{
   public static final SpType SP_TYPE =
     SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "instCGS4", "CGS4");

    private IterConfigItem iciInstAperL;

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterCGS4());
}

/**
 * Default constructor.
 */
public SpIterCGS4()
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
   return "CGS4";
}


/**
 * Override adding a configuration item to the set - to add instrument
 * aperture items.
 */
public void
addConfigItem(IterConfigItem ici, int size)
{

  if (ici.attribute.equals(SpInstCGS4.ATTR_CENTRAL_WAVELENGTH+"Iter")) {
    super.addConfigItemNoDef (iciInstAperL, size);
  }

  super.addConfigItem (ici, size);

}

/**
 * Override deleting  a configuration item to the set to remove the
 * inst aper attributes   too.
 */
public void
deleteConfigItem(String attribute)
{
  super.deleteConfigItem (attribute);

  if (attribute.equals(SpInstCGS4.ATTR_CENTRAL_WAVELENGTH+"Iter")) {
    super.deleteConfigItem (iciInstAperL.attribute);
  }
}

/**
 * Set the steps of the item iterator of the given attribute.
 * Overriding the method from SpIterConfigBase to add set of the
 * inst aper info when it changes.
 */
public void
setConfigStep(String attribute, String value, int index)
{

   _avTable.set(attribute, value, index);

   // If central wavelength then set L inst aper to same.
   if (attribute.equals(SpInstCGS4.ATTR_CENTRAL_WAVELENGTH+"Iter")) {

       _avTable.set(SpInstCGS4.ATTR_INSTRUMENT_APER+"LIter", 
		    value, index);

   }

}


/**
 * Get the array containing the IterConfigItems offered by CGS4.
 */
public IterConfigItem[]
getAvailableItems()
{

   IterConfigItem iciMode = new IterConfigItem(
	"Mode",
	SpInstCGS4.ATTR_MODE + "Iter",
	SpInstCGS4.MODES);

   IterConfigItem iciCentWavelength = new IterConfigItem(
	"Central Wavelen.",
	SpInstCGS4.ATTR_CENTRAL_WAVELENGTH + "Iter",
	null);

   iciInstAperL = new IterConfigItem(
	"InstAperL",
	SpInstCGS4.ATTR_INSTRUMENT_APER + "LIter",
	null);
 
   IterConfigItem[] iciA = {
	iciMode,
        getExposureTimeConfigItem(), getCoaddsConfigItem(),
	iciCentWavelength, iciInstAperL
   };

   return iciA;
}

}
