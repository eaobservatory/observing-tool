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
 * Override adding a configuration item to the set to set exposure time based on source magnitude, if available
 */
   public void addConfigItem( IterConfigItem ici, int size ) {

       if (ici.attribute.equals("exposureTimeIter")) {
	   if (size > 0) {
	       super.addConfigItemNoDef( ici, size );
	       for (int index = 0; index < size; index++) {
		   String sourceMag = getConfigStep(SpInstUIST.ATTR_SOURCE_MAG + "Iter", index);
		   if (sourceMag != null) {
		       setDefaultSkyExposureTime(sourceMag, index);
		   }
	       }
	   } else {
	       super.addConfigItem( ici, size );
	   }
       } else if (ici.attribute.equals(SpInstUIST.ATTR_SOURCE_MAG + "Iter")) {
	   super.addConfigItem( ici, size );
	   String expTime = getConfigStep("exposureTimeIter", 0);
	   if (expTime == null) {
	       super.addConfigItemNoDef( getExposureTimeConfigItem(), size );
	       if (size > 0) {
		   for (int index = 0; index < size; index++) {
		       String sourceMag = getConfigStep(SpInstUIST.ATTR_SOURCE_MAG + "Iter", index);
		       if (sourceMag != null) {
			   setDefaultSkyExposureTime(sourceMag, index);
		       }
		   }
	       } else {
		   String sourceMag = getConfigStep(SpInstUIST.ATTR_SOURCE_MAG + "Iter", 0);
		   if (sourceMag != null) {
		       setDefaultSkyExposureTime(sourceMag, 0);
		   }
	       }
	   }
       } else {
	   super.addConfigItem( ici, size );
       }
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

      if ( attribute.equals( SpInstUIST.ATTR_SOURCE_MAG + "Iter") ) {
	  if (getConfigStep("exposureTimeIter", index) != null) {
	      setDefaultSkyExposureTime(value, index);
	  }
      }
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

       // Source magnitude
       int nmags = SpInstUIST.SPECMAGS.getNumColumns() - 1;
       String specMags[] = new String[nmags];
       for (int i=0; i<nmags; i++) {
	   specMags[i] = (String)SpInstUIST.SPECMAGS.elementAt(0,i+1);
       }
       
       IterConfigItem iciSourceMag = new IterConfigItem(
	   "Source Magnitude", SpInstUIST.ATTR_SOURCE_MAG + "Iter", specMags );

       // Specify configuration items which can be iterated. 
       IterConfigItem[] iciA = {iciSourceMag,
         getExposureTimeConfigItem(), getCoaddsConfigItem()
       };
       return iciA;
   }

    private void setDefaultSkyExposureTime(String sourceMag, int index) {

	SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
	boolean instAvailable = (inst == null)? false: true;

	String disperser = null;
	if (instAvailable) {
	    disperser = inst.getDisperser();
	} else {
	    disperser = SpInstUIST.DEFAULT_DISPERSER;	      
	}

	int row = SpInstUIST.SPECMAGS.indexInColumn(disperser,0);
	int column = SpInstUIST.SPECMAGS.indexInRow(sourceMag,0);

	String set = (String)SpInstUIST.SPECMAGS.elementAt(row,column);
	_avTable.set( "exposureTimeIter", set, index );
    }
}
