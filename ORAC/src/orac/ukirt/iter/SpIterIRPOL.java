/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2000                   */
/*                                                              */
/*==============================================================*/

package orac.ukirt.iter;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.iter.IterConfigItem;

import java.util.*;

/**
 * The FP configuration iterator.
 */
public class SpIterIRPOL extends SpIterConfigObsUKIRT {

   public static final SpType SP_TYPE =
     SpType.create( SpType.ITERATOR_COMPONENT_TYPE, "instIRPOL", "IRPOL" );

   private IterConfigItem iciWPLAngle;

// Register the prototype.
   static {
      SpFactory.registerPrototype( new SpIterIRPOL() );
   }

// Hardwire the allowed angles, so that they are accessible to all
// instruments.
   static final String ALLOWED_ANGLES[] = {"0", "22.5", "45", "67.5", "90", "135"};

/**
 * Default constructor.
 */
   public SpIterIRPOL() {
      super( SP_TYPE );
   }

/**
 * Get the name of the item being iterated over.  Subclasses must
 * define.
 */
   public String getItemName() {
      return "IRPOL";
   }

/**
 * Override adding a configuration item to use the no default version.
 */
public void
addConfigItem(IterConfigItem ici, int size)
{

  super.addConfigItemNoDef (ici, size);

  // Then set a default value
  setConfigStep (ici.attribute, ALLOWED_ANGLES[0], 0);

}


/**
 * Get the array containing the IterConfigItems offered by IRPOL.
 */
   public IterConfigItem[] getAvailableItems() {

// Hardwire the allowed angles.
      IterConfigItem iciWPLAngle = new IterConfigItem(
        "Waveplate Angle",
        "IRPOLIter",
        ALLOWED_ANGLES );

      IterConfigItem[] iciA = {
         iciWPLAngle
      };

      return iciA;
   }

}
