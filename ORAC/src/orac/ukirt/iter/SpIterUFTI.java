/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package orac.ukirt.iter;

import orac.ukirt.inst.SpInstUFTI;
import orac.util.LookUpTable;

import gemini.util.ConfigWriter;

import gemini.sp.SpFactory;
import gemini.sp.SpItem;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.sp.SpTreeMan;
import gemini.sp.SpType;
import gemini.sp.iter.IterConfigItem;
import gemini.sp.iter.SpIterFolder;
import gemini.sp.iter.SpIterObserveBase;

import java.io.File;
import java.io.FileWriter;

import java.text.SimpleDateFormat;

import java.util.*;

/**
 * The UFTI configuration iterator.
 */
public class SpIterUFTI extends SpIterConfigObsUKIRT implements SpTranslatable {
  
   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "instUFTI", "UFTI");

   private IterConfigItem iciInstAperX;
   private IterConfigItem iciInstAperY;
   private IterConfigItem iciInstAperL;

// Register the prototype.
   static {
      SpFactory.registerPrototype(new SpIterUFTI());
   }

/**
 * Default constructor.
 */
   public SpIterUFTI() {
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

// Change the old attributes to the new ones.
      for ( int i = 0; i < v.size(); ++i ) {
         String  attr   = (String) v.elementAt(i);
         boolean change = false;

         String newAttr = null;
         Vector values  = null;
         if ( attr.equals( "integrationTimeIter" ) ) {
            change  = true;
            newAttr = SpInstUFTI.ATTR_EXPOSURE_TIME + "Iter";
            values  = _avTable.getAll( attr );
         } else if ( attr.equals( "areaIter" ) ) {
            change  = true;
            newAttr = SpInstUFTI.ATTR_READAREA + "Iter";
            values  = _avTable.getAll( attr );
         }

         if ( change ) {
            v.setElementAt( newAttr, i );
            _avTable.rm( attr );
            _avTable.setAll( newAttr, values );
         }
      }

      return v;
   }

/**
 * Override adding a configuration item to the set to add the instrument
 * aperture items.
 */
   public void addConfigItem( IterConfigItem ici, int size ) {

      if ( ici.attribute.equals( "readoutAreaIter" ) ) {
         super.addConfigItemNoDef( iciInstAperX, size );
         super.addConfigItemNoDef( iciInstAperY, size );
      } else if (ici.attribute.equals( "filterIter" ) ) {
         super.addConfigItemNoDef( iciInstAperL, size );
      }

      super.addConfigItem( ici, size );
   }


/**
 * Override deleting a configuration item to the set in order
 * to remove the instrument-aperture attributes too.
 */
   public void deleteConfigItem( String attribute ) {
      super.deleteConfigItem( attribute );
      if ( attribute.equals( "readoutAreaIter" ) ) {
         super.deleteConfigItem( iciInstAperX.attribute );
         super.deleteConfigItem( iciInstAperY.attribute );
      } else if ( attribute.equals( "filterIter" ) ) {
         super.deleteConfigItem( iciInstAperL.attribute );
      }
   }


/**
 * Try overriding the method from SpIterConfigBase to add set
 * of the instrument-aperture information when it changes.
 * Set the steps of the item iterator of the given attribute.
 */
   public void setConfigStep( String attribute, String value,
                              int index ) {

      _avTable.set( attribute, value, index );

      if ( attribute.equals( "readoutAreaIter" ) ) {

         StringTokenizer st = new StringTokenizer (value, "x" );   
         int xsize = Integer.parseInt( st.nextToken() );
     int ysize = Integer.parseInt( st.nextToken() );
     
     // I really hate doing this but...
     double xo = 0.998;
     double yo = -0.62;
     if (xsize == 512) {
       xo = -11.83;
     } else if (xsize == 256) {
       xo = -4.22;
     }
     if (ysize == 512) {
       yo = 16.83;
     } else if (ysize == 256) {
       yo = 12.85;
     }

     // These value are in millimeters - what will go to the telescope.
     _avTable.set(SpInstUFTI.ATTR_INSTRUMENT_APER+"XIter", 
              Double.toString(xo), index );
     _avTable.set(SpInstUFTI.ATTR_INSTRUMENT_APER+"YIter",
              Double.toString(yo), index );
     
   } else if ( attribute.equals( "filterIter" ) ) {

     // Find the relevant filter table and extract the lambda info
     int filtind = -1;
     LookUpTable farray = null;

     // Check for invalid filter value
     if (value == null || value.equals( "None" ) ) {
       farray = SpInstUFTI.BROAD_BAND_FILTERS;
       _avTable.set(SpInstUFTI.ATTR_INSTRUMENT_APER+"LIter", "1.0", index );
       
     } else {
       // Value is ok. Find the relevant LUT
       farray = SpInstUFTI.BROAD_BAND_FILTERS;
       try {
       filtind = farray.indexInColumn (value, 0 );
       }catch (Exception ex1) {
       farray = SpInstUFTI.NARROW_BAND_FILTERS;
       try {
         filtind = farray.indexInColumn (value, 0 );
       }catch (Exception ex2) {
         farray = SpInstUFTI.SPECIAL_FILTERS;
         try {
           filtind = farray.indexInColumn (value, 0 );
         }catch (Exception ex3) {
           System.out.println ( "SpIterUFTI: Failed to find filter anywhere!" );
           _avTable.set(SpInstUFTI.ATTR_INSTRUMENT_APER+"LIter", "1.0", 
                    index );
           return;
         }
       }
       }
       _avTable.set(SpInstUFTI.ATTR_INSTRUMENT_APER+"LIter", 
               (String) farray.elementAt (filtind, 1) , index );

     }
   }

}

/**
 * Get the name of the item being iterated over.
 */ 
   public String getItemName() {
      return "UFTI";
   }

/**
 * Get the array containing the IterConfigItems offered by UFTI.
 */
   public IterConfigItem[] getAvailableItems() {

// Acquistion Mode
      IterConfigItem iciAcqMode = new IterConfigItem(
        "Acqmode", SpInstUFTI.ATTR_MODE + "Iter",
        SpInstUFTI.MODES );

// Readout area.  This is now a lookup table, so extract
// the first column, and put the areas into an array.
      Vector vRA = SpInstUFTI.READAREAS.getColumn( 0  );

      String[] readoutAreas = new String[ (int) vRA.size() ];
      for ( int i = 0; i < vRA.size( ); ++i ) {
         readoutAreas[i] = (String) vRA.elementAt( i  );
      }

      IterConfigItem iciReadoutArea = new IterConfigItem(
        "ReadoutArea", SpInstUFTI.ATTR_READAREA + "Iter",
        readoutAreas  );

// Filters.
      Vector vBB = SpInstUFTI.BROAD_BAND_FILTERS.getColumn(0 );
      Vector vNB = SpInstUFTI.NARROW_BAND_FILTERS.getColumn(0 );
      Vector vSP = SpInstUFTI.SPECIAL_FILTERS.getColumn(0 );

// Concatenate filters to form a master array.
      int n = vBB.size() + vNB.size() + vSP.size();
      String[] filters = new String[ n ];
      for ( int i = 0; i < vBB.size(); ++i ) {
         filters[ i ] = (String) vBB.elementAt( i );
      }

      int offset = vBB.size( );
      for ( int i = 0; i < vNB.size(); ++i ) {
         filters[ offset + i ] = (String) vNB.elementAt( i );
      }

      offset += vNB.size();
      for ( int i = 0; i < vSP.size(); ++i ) {
         filters[ offset + i ] = (String) vSP.elementAt( i );
      }

      IterConfigItem iciFilter = new IterConfigItem(
         "Filter", SpInstUFTI.ATTR_FILTER + "Iter", filters );

// Instrument aperture stuff.  Will need to hide...
      iciInstAperX = new IterConfigItem( "InstAperX",
        SpInstUFTI.ATTR_INSTRUMENT_APER + "XIter", null );
      iciInstAperY = new IterConfigItem( "InstAperY",
        SpInstUFTI.ATTR_INSTRUMENT_APER + "YIter", null );
      iciInstAperL = new IterConfigItem( "InstAperL",
        SpInstUFTI.ATTR_INSTRUMENT_APER + "LIter", null );
 
// Specify configuration items which can be iterated. 
      IterConfigItem[] iciA = {
        iciAcqMode, iciReadoutArea, iciFilter,
        getExposureTimeConfigItem(), getCoaddsConfigItem(),
        iciInstAperX, iciInstAperY, iciInstAperL
      };

      return iciA;
   }

public void translate( Vector v ) throws SpTranslationNotSupportedException {
    // First get the available items and set the defaults
    // after making sure an instrument of the right type is available
    SpInstUFTI inst;
    boolean isPol = false;
    try {
        inst = (SpInstUFTI) SpTreeMan.findInstrument(this);
        isPol = (inst.getPolariser().equals("prism"));
    }
    catch ( Exception npe ) {
        throw new SpTranslationNotSupportedException("No UFTI instrument in scope");
    }


    List iterList = getConfigAttribs();
    int nConfigs = getConfigSteps((String)iterList.get(0)).size();
    for ( int i=0; i<nConfigs; i++ ) {
        Hashtable defaultsTable = inst.getConfigItems();
        String xAper = " " +(String)defaultsTable.get("instAperX");
        String yAper = " " +(String)defaultsTable.get("instAperY");
        String zAper = " " +(String)defaultsTable.get("instAperZ");
        String lAper = " " +(String)defaultsTable.get("instAperL");
        for ( int j=0; j<iterList.size(); j++ ) {
            String attrib = (String)iterList.get(j);
            List iterVals = getConfigSteps(attrib);
            // Loop over each of there writing a new config file
            if ( iterList.contains("filterIter") ) {
                String filter = (String)getConfigSteps("filterIter").get(i);
                if ( isPol ) filter = filter + "+pol";
                defaultsTable.put("filter", filter);
            }
            if ( iterList.contains("acqModeIter") ) {
                defaultsTable.put( "readMode", (String)getConfigSteps("acqModeIter").get(i));
            }
            if ( iterList.contains("readoutAreaIter") ) {
                defaultsTable.put( "readArea", (String)getConfigSteps("readoutAreaIter").get(i));
            }
            if ( iterList.contains("exposureTimeIter") ) {
                defaultsTable.put( "expTime", (String)getConfigSteps("exposureTimeIter").get(i));
            }
            if ( iterList.contains("coaddsIter") ) {
                defaultsTable.put( "objNumExp", (String)getConfigSteps("coaddsIter").get(i));
            }
            if ( iterList.contains("instAperXIter") ) {
                xAper = " " +(String)getConfigSteps("instAperXIter").get(i);
            }
            if ( iterList.contains("instAperYIter") ) {
                yAper = " " +(String)getConfigSteps("instAperYIter").get(i);
            }
            if ( iterList.contains("instAperZIter") ) {
                zAper = " " +(String)getConfigSteps("instAperZIter").get(i);
            }
            if ( iterList.contains("instAperLIter") ) {
                lAper = " " +(String)getConfigSteps("instAperLIter").get(i);
            }
        }
        try {
            ConfigWriter.getCurrentInstance().write(defaultsTable);
        }
        catch (Exception e) {
            throw new SpTranslationNotSupportedException("Unable to write config file for UFTI iterator:"+e.getMessage());
        }

        v.add("loadConfig " + ConfigWriter.getCurrentInstance().getCurrentName());
        v.add("define_inst " + getItemName() + xAper + yAper + zAper + lAper);

        Enumeration e = this.children();
        while (e.hasMoreElements()) {
            SpItem child = (SpItem)e.nextElement();
            if ( child instanceof SpTranslatable ) {
                ((SpTranslatable)child).translate(v);
            }
        }

    }
}
}