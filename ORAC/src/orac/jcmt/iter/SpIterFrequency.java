/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.jcmt.iter;

import orac.ukirt.iter.SpIterConfigObsUKIRT;
//import orac.ukirt.inst.SpInstIRCAM3;
import orac.util.LookUpTable;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.iter.IterConfigItem;

import java.util.*;

/**
 * The Frequency iterator.
 *
 * @author modified for JCMT by Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterFrequency extends SpIterConfigObsUKIRT {

   public static final SpType SP_TYPE =
       SpType.create( SpType.ITERATOR_COMPONENT_TYPE, "frequency", "Frequency" );

   private IterConfigItem iciInstAperX;
   private IterConfigItem iciInstAperY;
   private IterConfigItem iciInstAperL;

// Register the prototype.
   static {
      SpFactory.registerPrototype( new SpIterFrequency() );
   }

/**
 * Default constructor.
 */
   public SpIterFrequency() {
      super( SP_TYPE );
   }

/**
 * Override "getConfigAttribs" to fix up old programs with the wrong
 * attribute names.
 */
   public Vector getConfigAttribs() {
/*
      Vector v = super.getConfigAttribs();

      if ( v == null ) {
         return null;
      }

// Change the old attributes to the new ones.
      for ( int i = 0; i < v.size(); ++i ) {
         String  attr   = (String) v.elementAt( i );
         boolean change = false;

         String newAttr = null;
         Vector values  = null;
         if ( attr.equals( "integrationTimeIter" ) ) {
         change  = true;
         newAttr = SpInstIRCAM3.ATTR_EXPOSURE_TIME + "Iter";
         values  = _avTable.getAll( attr );

         } else if ( attr.equals( "areaIter" ) ) {
            change  = true;
            newAttr = SpInstIRCAM3.ATTR_READAREA + "Iter";
            values  = _avTable.getAll( attr );
         }

         if ( change ) {
            v.setElementAt( newAttr, i);
            _avTable.rm( attr );
            _avTable.setAll( newAttr, values );
         }
      }

      return v;
*/
      return new Vector();
   }

/**
 * Override adding a configuration item to the set.
 */
   public void addConfigItem( IterConfigItem ici, int size )  {
/*
       // If required add the instrument aperture info first, not
       // setting defaults. Then when we set the new item itself the 
       // inst. aper. values will also get set. 
      if ( ici.attribute.equals( "readoutAreaIter" ) ) {
         super.addConfigItemNoDef (iciInstAperX, size);
         super.addConfigItemNoDef (iciInstAperY, size);

      } else if ( ici.attribute.equals( "filterIter" ) ) {
         super.addConfigItemNoDef (iciInstAperL, size);
      }
      super.addConfigItem( ici, size );
*/      
   }

/**
 * Try deleting a configuration item to the set.
 */
   public void deleteConfigItem( String attribute ) {
/*
      super.deleteConfigItem ( attribute );
      if ( attribute.equals( "readoutAreaIter" ) ) {
         super.deleteConfigItem( iciInstAperX.attribute );
         super.deleteConfigItem( iciInstAperY.attribute );

      } else if ( attribute.equals( "filterIter" ) ) {
         super.deleteConfigItem( iciInstAperL.attribute );
      }
*/
   }


/**
 * Try overriding the method from SpIterConfigBase to add set of the
 * instrument aperture information when it changes.  Set the steps of
 * the item iterator of the given attribute.
 */
   public void setConfigStep( String attribute, String value, int index ) {
/*   

      _avTable.set( attribute, value, index );

      if ( attribute.equals( "readoutAreaIter" ) ) {

         StringTokenizer st = new StringTokenizer( value, "x" );   
         int xsize = Integer.parseInt( st.nextToken() );
         int ysize = Integer.parseInt( st.nextToken() );
     
// I really hate doing this but...
         double xo = 6.35;
         double yo = 11.77;
         if ( xsize == 128 ) {
            xo = 6.35;
            yo = 11.77;
         }

// These value are in millimeters - what will go to the telescope.
         _avTable.set( SpInstIRCAM3.ATTR_INSTRUMENT_APER + "XIter", 
                       Double.toString( xo ), index );
         _avTable.set( SpInstIRCAM3.ATTR_INSTRUMENT_APER + "YIter",
                       Double.toString( yo ), index );
     
      } else if ( attribute.equals( "filterIter" ) ) {

// Find the relevant filter table and extract the lambda info
         int filtind = -1;
         LookUpTable farray = null;

// Check for invalid filter value.
         if (value == null || value.equals( "None" ) ) {
            farray = SpInstIRCAM3.BROAD_BAND_FILTERS;
            _avTable.set( SpInstIRCAM3.ATTR_INSTRUMENT_APER + "LIter",
                          "1.0", index );
       
         } else {

// Value is ok. Find the relevant LUT.
            farray = SpInstIRCAM3.BROAD_BAND_FILTERS;
            try {
               filtind = farray.indexInColumn( value, 0 );
            } catch ( Exception ex1 ) {
               farray = SpInstIRCAM3.NARROW_BAND_FILTERS;
               try {
                  filtind = farray.indexInColumn( value, 0 );
               } catch ( Exception ex2 ) {
                  farray = SpInstIRCAM3.SPECIAL_FILTERS;
                  try {
                     filtind = farray.indexInColumn ( value, 0 );
                  } catch ( Exception ex3 ) {
                     System.out.println( "in SpIterFrequency: " + 
                                         "Failed to find filter anywhere!" );
                     _avTable.set( SpInstIRCAM3.ATTR_INSTRUMENT_APER +
                                   "LIter", "1.0", index );
                     return;           
                  }
               }
            }

           _avTable.set( SpInstIRCAM3.ATTR_INSTRUMENT_APER + 
                         "LIter", (String) farray.elementAt( filtind, 1 ),
                         index);

         }
      }
*/
   }


/**
 * Get the name of the item being iterated over.  Subclasses must
 * define.
 */
   public String getItemName() {
      return "Frequency";
   }


/**
 * Get the array containing the IterConfigItems offered by IRCAM3.
 */
   public IterConfigItem[] getAvailableItems() {
     // MFO: mocked up implementation for demonstration purpose.

     String [] lineTransitionChoices = { "A few examples:",
                                         "H2O (515 - 422)", 
                                         "H3O+ (310 - 221)", 
                                         "CN, v = 0, 1 (3032 - 2032)", 
                                         "HCN (4 - 3)", 
                                         "C-13-N (3432 - 2332)", 
                                         "CO (3 - 2)", 
                                         "C-13-O (3 - 2)",
					 "There is more to come."
					 };
   
   
     IterConfigItem lineTransitionConfigItem = new IterConfigItem("Line/Transition",
                                                                  "lineTransition",
								  lineTransitionChoices);


     IterConfigItem frequencyConfigItem = new IterConfigItem("Frequency", "frequency", null);

     String [] sidebandChoices = {"usb", "lsb", "best" };

     IterConfigItem sidebandConfigItem = new IterConfigItem("Sideband", "sideband", sidebandChoices);

     IterConfigItem [] result = { lineTransitionConfigItem, frequencyConfigItem, sidebandConfigItem };

     
     return result;

     
/*
// Acquistion Mode
      IterConfigItem iciAcqMode = new IterConfigItem(
        "Acqmode", SpInstIRCAM3.ATTR_MODE + "Iter",
        SpInstIRCAM3.MODES );

// Readout area.  This is now a lookup table, so extract
// the first column, and put the areas into an array.
      Vector vRA = SpInstIRCAM3.READAREAS.getColumn( 0 );

      String[] readoutAreas = new String[ (int) vRA.size() ];
      for ( int i = 0; i < vRA.size(); ++i ) {
         readoutAreas[i] = (String) vRA.elementAt( i );
      }

      IterConfigItem iciReadoutArea = new IterConfigItem(
        "ReadoutArea", SpInstIRCAM3.ATTR_READAREA + "Iter",
        readoutAreas );
 
// Filters.
      Vector vBB = SpInstIRCAM3.BROAD_BAND_FILTERS.getColumn( 0 );
      Vector vNB = SpInstIRCAM3.NARROW_BAND_FILTERS.getColumn( 0 );
      Vector vSP = SpInstIRCAM3.SPECIAL_FILTERS.getColumn( 0 );

// Concatenate filters to form a master array.
      int n = vBB.size() + vNB.size() + vSP.size();
      String[] filters = new String[ n ];
      for ( int i = 0; i < vBB.size(); ++i ) {
         filters[i] = (String) vBB.elementAt( i );
      }

      int offset = vBB.size();
      for ( int i = 0; i < vNB.size(); ++i ) {
         filters[ offset + i ] = (String) vNB.elementAt( i );
      }

      offset += vNB.size();
      for ( int i = 0; i < vSP.size(); ++i ) {
         filters[ offset + i ] = (String) vSP.elementAt( i );
      }

      IterConfigItem iciFilter = new IterConfigItem(
        "Filter",  SpInstIRCAM3.ATTR_FILTER + "Iter",
        filters );

// Instrument aperture stuff.  Will need to hide...
      iciInstAperX = new IterConfigItem( "InstAperX",
        SpInstIRCAM3.ATTR_INSTRUMENT_APER + "XIter", null );
      iciInstAperY = new IterConfigItem( "InstAperY",
        SpInstIRCAM3.ATTR_INSTRUMENT_APER + "YIter", null );
      iciInstAperL = new IterConfigItem( "InstAperL",
        SpInstIRCAM3.ATTR_INSTRUMENT_APER + "LIter", null );

// Specify configuration items which can be iterated. 
      IterConfigItem[] iciA = {
        iciAcqMode, iciReadoutArea, iciFilter,
        getExposureTimeConfigItem(), getCoaddsConfigItem(),
        iciInstAperX, iciInstAperY, iciInstAperL
      };

      return iciA;
*/
   }

}
