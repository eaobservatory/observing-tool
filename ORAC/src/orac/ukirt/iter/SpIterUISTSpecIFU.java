/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package orac.ukirt.iter;

import orac.ukirt.inst.SpInstUIST;
import orac.util.LookUpTable;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.iter.IterConfigItem;

import java.util.*;

/**
 * The UIST spectroscopy/IFU configuration iterator.
 */
public class SpIterUISTSpecIFU extends SpIterConfigObsUKIRT {
  
   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "instUISTSpecIFU", "UIST Spec/IFU");

   private IterConfigItem iciInstAperL;

// Register the prototype.
   static {
      SpFactory.registerPrototype(new SpIterUISTSpecIFU());
   }

/**
 * Default constructor.
 */
   public SpIterUISTSpecIFU() {
      super(SP_TYPE);
   }


/**
 * Override "getConfigAttribs" to fix up old programs with the wrong
 * attribute names.
 */
   public Vector getConfigAttribs() {

      Vector v = super.getConfigAttribs();

      if ( v == null ) {
         return null;
      }

      return v;
   }

/**
 * Override adding a configuration item to the set to add the instrument
 * aperture items.
 */
   public void addConfigItem( IterConfigItem ici, int size ) {
      super.addConfigItem( ici, size );
   }


/**
 * Override deleting a configuration item to the set in order
 * to remove the instrument-aperture attributes too.
 */
   public void deleteConfigItem( String attribute ) {
      super.deleteConfigItem( attribute );
   }


/**
 * Try overriding the method from SpIterConfigBase to add set
 * of the instrument-aperture information when it changes.
 * Set the steps of the item iterator of the given attribute.
 */
   public void setConfigStep( String attribute, String value,
                              int index ) {
      _avTable.set( attribute, value, index );
   }


/**
 * Get the name of the item being iterated over.
 */ 
   public String getItemName() {
      return "UIST Spec/IFU";
   }

/**
 * Get the array containing the IterConfigItems offered by UIST for spectroscopy/IFU.
 */
   public IterConfigItem[] getAvailableItems() {

       SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
       boolean instAvailable = (inst == null)? false: true;
       IterConfigItem iciDisperser;

       if (instAvailable) {
          String [] dispersers = inst.getDisperserList();

          // Dispersers.
          iciDisperser = new IterConfigItem(
            "Grism", SpInstUIST.ATTR_DISPERSER + "Iter", dispersers );
       
       } else {
          Vector vDispersers = SpInstUIST.DISPERSERS.getColumn(0);
          int n = vDispersers.size();

          String [] dispersers = new String[n];
          for (int i = 0; i < n; i++)
          {
              dispersers[i] = (String) vDispersers.elementAt(i);
          }

          // Dispersers.
          iciDisperser = new IterConfigItem(
            "Grism", SpInstUIST.ATTR_DISPERSER + "Iter", dispersers );
       }

       // Specify configuration items which can be iterated. 
       IterConfigItem[] iciA = {
         iciDisperser,
         getExposureTimeConfigItem(), getCoaddsConfigItem()
       };
       return iciA;
   }
}
