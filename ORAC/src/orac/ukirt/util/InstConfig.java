package orac.ukirt.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 *  This class defines a data structure which stores the instrument
 *  configurations for the Translator.
 *
 *  Currently supports UFTI, IRCAM3 and CGS4 (Michelle support
 *  in development).
 *
 *  see SpTranslator
 */
public class InstConfig extends Hashtable implements Cloneable {

// Declare variables.
// ==================
   private Hashtable instTable;
   private String key;           // Hashtable key
   private String value;         // Value corresponding to the above
                                 // Hashtable key

/**
 *  Constructor.  Couldhave another with instrum defaulted and invoking
 *  this constructor.
 */
   public InstConfig() {

// Start with default elements in the hash table (101) and 0.75 loading, which should
// be plenty.
      super();
   }

/**
  * Initialises the configuration array for a given instrument.
  */

   public void init( String instrum ) {

// Use different initialisations for different instruments.
      if ( instrum.equalsIgnoreCase( "UFTI" ) ) {

// Supply values to the hashtable. Undefined values are set to
// "TBD" because a Hashtable cannot accept a null value.
         put( "instrument", instrum );  // Instrument name
         put( "version", "0.0" );       // Version number
         put( "name", "TBD" );          // Name of configuration file
         put( "readMode", "read" );     // Acquisition/read mode
         put( "speed", "Normal" );      // Acquisition speed

         put( "expTime", "10.0" );      // Exposure time in seconds
         put( "objNumExp", "1" );       // Number of object exposures (coadds)
         put( "savedInt", "1" );        // Number of saved integrations
         put( "readArea", "1024x1024" ); // Readout area
         put( "filter", "J98" );        // Filter name
         put( "polariser", "none" );    // Polariser name

         put( "flatNumExp", "1" );      // Number of flat exposures (coadds)
         put( "flatSavedInt", "1" );    // Number of saved flat integrations

         put( "darkNumExp", "1" );      // Number of dark exposures (coadds)
         put( "darkSavedInt", "1" );    // Number of saved dark integrations

         put( "biasExpTime", "0.12" );  // Bias exposure time
         put( "biasNumExp", "100" );    // Number of bias exposures (coadds)
         put( "biasSavedInt", "1" );    // Number of saved bias integrations

      } else if ( instrum.equalsIgnoreCase( "TUFTI/IRCAM" ) ||
                  instrum.equalsIgnoreCase( "IRCAM3" ) ) {

// Supply values to the hashtable. Undefined values are set to
// "TBD" because a Hashtable cannot accept a null value.
         put( "instrument", instrum );  // Instrument name
         put( "version", "2.1" );       // Version number
         put( "name", "TBD" );          // Name of configuration file
         put( "readMode", "NDSTARE" );  // Acquisition/read mode
         put( "speed", "Standard" );    // Acquisition speed

         put( "expTime", "10.0" );      // Exposure time in seconds
         put( "objNumExp", "1" );       // Number of object exposures (coadds)
         put( "savedInt", "1" );        // Number of saved integrations
         put( "readArea", "256x256" );  // Readout area
         put( "filter", "J98" );        // Filter name
         put( "polariser", "none" );    // Polariser name

         put( "flatNumExp", "1" );      // Number of flat exposures (coadds)
         put( "flatSavedInt", "1" );    // Number of saved flat integrations

         put( "darkNumExp", "1" );      // Number of dark exposures (coadds)
         put( "darkSavedInt", "1" );    // Number of saved dark integrations

         put( "biasExpTime", "0.12" );  // Bias exposure time
         put( "biasNumExp", "100" );    // Number of bias exposures (coadds)
         put( "biasSavedInt", "1" );    // Number of saved bias integrations

      } else if ( instrum.equalsIgnoreCase( "CGS4" ) ) {
         put( "instrument", instrum );  // Instrument name
         put( "version", "TBD" );       // Version number
         put( "name", "TBD" );          // Name of configuration file
         put( "readMode", "read" );     // Acquisition/read mode

         put( "expTime", "5.5" );       // Exposure time in seconds
         put( "objNumExp", "1" );       // Number of object exposures (coadds)
         put( "savedInt", "1" );        // Number of saved integrations
         put( "filter", "Blank" );      // Filter name
         put( "neutralDensity", "false" ); // Whether or not neutral-density
                                        // filter is in place

         put( "sampling", "1x1" );      // Sampling stepsize x range in pixels
         put( "positionAngle", "0.0" ); // Slit position angle
         put( "slitWidth", "2" );       // Number of pixels across slit width
         put( "disperser", "40lpmm" );  // Grating ruling or echelle
         put( "polariser", "none" );    // Polariser name
         put( "centralWavelength", "2.2" ); // Central wavelength in microns
         put( "order", "1" );           // Grating order
         put( "cvfOffset", "0.0" );     // Circular variable filter offset
                                        // in microns

         put( "calibLamp", "off" );     // Black-body or arc lamps
         put( "tunHalLevel", "97" );    // Tungsten-halogen level
         put( "lampEffAp", "10" );      // Lamp effective aperture

         put( "flatSampling", "1x1" );  // Flat sampling stepsize x range in pixel
         put( "flatCalLamp", "1.3" );   // Flat black-body aperture
         put( "flatReadMode", "NDSTARE" ); // Flat acquisition configuration
         put( "flatExpTime", "0.2" );   // Flat exposure time
         put( "flatNeutralDensity", "false" ); // Whether or not neutral-density
                                        // filter is in place for flat
         put( "flatNumExp", "30" );     // Number of flat exposures (coadds)
         put( "flatSavedInt", "1" );    // Number of saved flat integrations

         put( "darkNumExp", "1" );      // Number of dark exposures (coadds)
         put( "darkSavedInt", "1" );    // Number of saved dark integrations

         put( "biasExpTime", "0.12" );  // Bias exposure time
         put( "biasNumExp", "100" );    // Number of bias exposures (coadds)
         put( "biasSavedInt", "3" );    // Number of saved bias integrations

         put( "arcCalLamp", "argon" );  // Full wavelength calibration
         put( "arcFilter", "Blank" );   // Filter for arc
         put( "arcCvfWavelength", "TBD" ); // Evaluated cvf * offset + central
                                        // wavelength
         put( "arcExpTime", "0.12" );   // Arc exposure time
         put( "arcNumExp", "100" );     // Number of arc exposures (coadds)
         put( "arcReadMode", "stare" ); // Readout mode for arc
         put( "arcSavedInt", "1" );     // Number of saved arc integrations
      } else if ( instrum.equalsIgnoreCase( "Michelle" ) ) {
         put( "instrument", instrum );  // Instrument name
         put( "version", "TBD" );       // Version number
         put( "name", "TBD" );          // Name of configuration file
	 put( "configType", "normal");  // Type of configuration: normal or eng
	 put( "type", "object");        // Default "type" of Michelle config
	 put( "camera", "imaging");     // Which camera is in use

         put( "polarimetry", "false" ); // Whether polarimetry is in use
         put( "mask", "TBD" );          // Mask (slit)  in use
         put( "maskAngle", "0.0" );     // Mask position angle
         put( "positionAngle", "0.0" ); // Position angle (on sky)
         put( "disperser", "echelle" ); // Grating ruling or echelle
         put( "order", "1" );           // Grating order
         put( "sampling", "1x1" );      // Sampling stepsize x range in pixels
         put( "centralWavelength", "2.2" ); // Central wavelength in microns
         put( "filter", "Blank" );      // Filter name
         put( "waveplate", "TBD" );     // waveplate (type or angle?)

         put( "scienceArea", "64.0 x 48.0" );  // Science area
         put( "spectralCoverage", "10.0 - 11.0" );  // Wavelength coverage
         put( "pixelFOV", "1.00 x 1.00" );  // Pixel field of view

         put( "nreads", "1" );          // Number of reads
         put( "mode", "read" );         // Acquisition/read mode
         put( "expTime", "5.5" );       // Exposure time in seconds
         put( "expTime", "5.5" );       // Exposure time in seconds
         put( "readInterval", "0.0" );  // Read interval in seconds
         put( "chopFrequency", "0.0" ); // Chop frequency
         put( "resetDelay", "0.0" );    // reset delay (msecs?)
         put( "nresets", "0" );         // No. of resets
         put( "chopDelay", "0.0" );     // Chop delay (msecs?)
         put( "objNumExp", "1" );       // Number of object exposures (coadds)
         put( "waveform", "unknown" );  // Edict Waveform to be used
         put( "dutyCycle", "0.0" );     // Dutycycle for the obs.
         put( "mustIdles", "0" );       // Edict idling
         put( "nullReads", "0" );       // Edict no. of null reads
         put( "nullExposures", "0" );   // Edict no. of null exposures
         put( "nullCycles", "0" );      // Edict no. of null cycles
         put( "idlePeriod", "0.0" );    // Edict idle period
         put( "observationTime", "5.5" );  // Complete obs. time in seconds

	 // Attributes for FLATS
         put( "flatSampling", "1x1" );  // Flat sampling stepsize x range in pixel
         put( "flatSource", "1.3" );    // Flat cal. source
         put( "flatfilter", "Blank" );      // Flat filter
         put( "flatnreads", "1" );          // Number of reads
         put( "flatmode", "read" );         // Acquisition/read mode
         put( "flatexpTime", "5.5" );       // Exposure time in seconds
         put( "flatreadInterval", "0.0" );  // Read interval in seconds
         put( "flatchopFrequency", "0.0" ); // Chop frequency
         put( "flatchopDelay", "0.0" );     // Chop delay
         put( "flatresetDelay", "0.0" );    // reset delay (msecs?)
         put( "flatnresets", "0" );         // No. of resets
         put( "flatNumExp", "1" );       // Number of flat exposures (coadds)
         put( "flatwaveform", "unknown" );  // Edict Waveform to be used
         put( "flatdutyCycle", "0.0" );     // Dutycycle for the obs.
         put( "flatmustIdles", "0" );       // Edict idling
         put( "flatnullReads", "0" );       // Edict no. of null reads
         put( "flatnullExposures", "0" );   // Edict no. of null exposures
         put( "flatnullCycles", "0" );      // Edict no. of null cycles
         put( "flatidlePeriod", "0.0" );    // Edict idle period

	 // Darks (only filter used currently)
         put( "darkFilter", "Blank" );  // Filter name for darks
         put( "darkNumExp", "1" );      // Number of dark exposures (coadds)

         // Biases (not currently used)
	 put( "biasExpTime", "0.12" );  // Bias exposure time
         put( "biasNumExp", "100" );    // Number of bias exposures (coadds)

	 // Attributes for ARCS
	 //         put( "arcSampling", "1x1" );      // Arc sampling stepsize x range in pixel
         put( "arcfilter", "Blank" );      // Flat filter
         put( "arcnreads", "1" );          // Number of reads
         put( "arcmode", "read" );         // Acquisition/read mode
         put( "arcexpTime", "5.5" );       // Exposure time in seconds
         put( "arcreadInterval", "0.0" );  // Read interval in seconds
         put( "arcchopFrequency", "0.0" ); // Chop frequency
         put( "arcchopDelay", "0.0" );     // Chop delay
         put( "arcresetDelay", "0.0" );    // reset delay (msecs?)
         put( "arcnresets", "0" );         // No. of resets
         put( "arcNumExp", "1" );       // Number of arc exposures (coadds)
         put( "arcwaveform", "unknown" );  // Edict Waveform to be used
         put( "arcdutyCycle", "0.0" );     // Dutycycle for the obs.
         put( "arcmustIdles", "0" );       // Edict idling
         put( "arcnullReads", "0" );       // Edict no. of null reads
         put( "arcnullExposures", "0" );   // Edict no. of null exposures
         put( "arcnullCycles", "0" );      // Edict no. of null cycles
         put( "arcidlePeriod", "0.0" );    // Edict idle period

      }
   }

/** 
 *  Accessor method to return the hash table.
 */
   public Hashtable config() { return instTable; }

/**
 *  Deep-clone method required to copy the key-value configuration pairs.
  */
   public Object clone() {

      Object obj;                   // Object cloned
      Vector v;                     // Work vector

// Make a new Hashtable.
      InstConfig ht = new InstConfig();

// Loop through all the elements.  Here we assume that the values are
// scalar unless the instrument apertures.
      for ( Enumeration e = this.keys(); e.hasMoreElements(); ) {
         key = (String) e.nextElement();
         value = (String) this.get( key );
         ht.put( key, value );
      }

      obj = (Object) ht;

// Return the cloned hashtable.
      return obj;
   }

/**
 *
 * Translates the OT attribute names to internal translator equivalents.
 *
 */
   public String OTToTranslator( String title, String attribute ) {

// Local variables
      String lcTitle;        // Lowercase title
      String lcAttribute;    // Lowercase attribute

// Set a default value.  Many are the same name, so retain the
// supplied name unless otherwise translated.
      key = attribute;

// Use lower case for comparison purposes.
      lcTitle = title.toLowerCase();
      lcAttribute = attribute.toLowerCase();

// The title defines which type of configuration.  Title="config"
// is the general instrument config.  There are also special configs
// for calibrations.  Within each type of configuration test for each
// value.
      if ( lcTitle.equals( "config" ) ) {

// Here might need to allow for changes of OT nomenclature.
         if ( lcAttribute.equals( ".version" ) ) key = "version";
	 //	 else if ( lcAttribute.equals( "acqmode" ) ) key = "readMode";
         else if ( lcAttribute.equals( "readoutarea" ) ) key = "readArea";
         else if ( lcAttribute.equals( "exposuretime" ) ) key = "expTime";
         else if ( lcAttribute.equals( "coadds" ) ) key = "objNumExp";
         else if ( lcAttribute.equals( "instaper" ) ) key="instAper";
	 else if ( lcAttribute.equals( "posangle" ) ) key="positionAngle";
	 //         else if ( lcAttribute.equals( "mask" ) ) key = "slitWidth";

      } else if (  lcTitle.equals( "arc" ) ) {
         if ( lcAttribute.equals( "coadds" ) ) key = "arcNumExp";
         else if ( lcAttribute.equals( "caltype" ) ) key = "type";
         //else if ( lcAttribute.equals( "acqmode" ) ) key = "arcReadMode";
         else if ( lcAttribute.equals( "cvfwavelength" ) ) key = "arcCvfWavelength";
         else if ( lcAttribute.equals( "exposuretime" ) ) key ="arcexpTime";
         else if ( lcAttribute.equals( "observationtime" ) ) key ="arcobsTime";
	 //         else if ( lcAttribute.equals( "sampling" ) ) key = "arcSampling";
         else if ( lcAttribute.equals( "filter" ) ) key = "arcfilter";
         else if ( lcAttribute.equals( "lamp" ) ) key = "arcCalLamp";

         else if ( lcAttribute.equals( "nreads" )) key = "arcnreads";
         else if ( lcAttribute.equals( "mode" )) key = "arcmode";
         else if ( lcAttribute.equals( "readinterval" )) key = "arcreadInterval";
         else if ( lcAttribute.equals( "chopfrequency" )) key = "arcchopFrequency";
         else if ( lcAttribute.equals( "chopdelay" )) key = "arcchopDelay";
         else if ( lcAttribute.equals( "resetdelay" )) key = "arcresetDelay";
         else if ( lcAttribute.equals( "nresets" )) key = "arcnresets";
         else if ( lcAttribute.equals( "waveform" )) key= "arcwaveform";
         else if ( lcAttribute.equals( "dutycycle" )) key = "arcdutyCycle";
         else if ( lcAttribute.equals( "mustidles" )) key = "arcmustIdles";
         else if ( lcAttribute.equals( "nullreads" )) key = "arcnullReads";
         else if ( lcAttribute.equals( "nullexposures" )) key = "arcnullExposures";
         else if ( lcAttribute.equals( "nullcycles" )) key = "arcnullCycles";
         else if ( lcAttribute.equals( "idleperiod" )) key = "arcidlePeriod";

      } else if (  lcTitle.equals( "bias" ) ) {
         if ( lcAttribute.equals( "coadds" ) ) key = "biasNumExp";
         else if ( lcAttribute.equals( "exposuretime" ) ) key ="biasExpTime";

      } else if (  lcTitle.equals( "dark" ) ) {
         if ( lcAttribute.equals( "coadds" ) ) key = "darkNumExp";
         else if ( lcAttribute.equals( "exposuretime" ) ) key ="darkExpTime";
         else if ( lcAttribute.equals( "filter" )) key = "darkFilter";

      } else if (  lcTitle.equals( "flat" ) ) {
         if ( lcAttribute.equals( "coadds" ) ) key = "flatNumExp";
         else if ( lcAttribute.equals( "caltype" ) ) key = "type";
         // else if ( lcAttribute.equals( "acqmode" ) ) key = "flatReadMode";
         else if ( lcAttribute.equals( "exposuretime" ) ) key ="flatexpTime";
         else if ( lcAttribute.equals( "observationtime" ) ) key ="flatobsTime";
         else if ( lcAttribute.equals( "sampling" ) ) key = "flatSampling";
         else if ( lcAttribute.equals( "filter" ) ) key = "flatfilter";
         else if ( lcAttribute.equals( "lamp" ) ) key = "flatCalLamp";
         else if ( lcAttribute.equals( "neutraldensity" ) ) key = "flatNeutralDensity";
         else if ( lcAttribute.equals( "nreads" )) key = "flatnreads";
         else if ( lcAttribute.equals( "mode" )) key = "flatmode";
         else if ( lcAttribute.equals( "readinterval" )) key = "flatreadInterval";
         else if ( lcAttribute.equals( "chopfrequency" )) key = "flatchopFrequency";
         else if ( lcAttribute.equals( "chopdelay" )) key = "flatchopDelay";
         else if ( lcAttribute.equals( "resetdelay" )) key = "flatresetDelay";
         else if ( lcAttribute.equals( "nresets" )) key = "flatnresets";
         else if ( lcAttribute.equals( "waveform" )) key= "flatwaveform";
         else if ( lcAttribute.equals( "dutycycle" )) key = "flatdutyCycle";
         else if ( lcAttribute.equals( "mustidles" )) key = "flatmustIdles";
         else if ( lcAttribute.equals( "nullreads" )) key = "flatnullReads";
         else if ( lcAttribute.equals( "nullexposures" )) key = "flatnullExposures";
         else if ( lcAttribute.equals( "nullcycles" )) key = "flatnullCycles";
         else if ( lcAttribute.equals( "idleperiod" )) key = "flatidlePeriod";

      }
      return key;
   }

/**
 * Compares two configs.  Returns true if they are identical.
 */

   boolean isSame( String instrum, InstConfig config ) {

      Enumeration ekey;        // Enumeration of the `this' config
      String key;              // A key in the hashTable
      boolean same;            // Configs are the same?

// Assume that the result is the same unless a mismatch is found.
      same = true;
      
// Use different initialisations for different instruments.
//      if ( instrum.equalsIgnoreCase( "UFTI" ) ) {

// Get an enumeration of the keys.
         ekey = keys();
         while ( ekey.hasMoreElements() ) {
            key = (String) ekey.nextElement();

// Check that the key is present in the second config.
            if ( ! config.containsKey( key ) ) {
               same = false;
               break;
            }

// Compare the values in the two configs.
            if ( ! get( key ).equals( config.get( key ) ) ) {
               same = false;
               break;
            }
         }
      return same;
//      } 
   }

/**
 * Compares two configs.  Returns true if any of a supplied Vector of
 * nominated attributes are not identical in both..
 */

   boolean changedAttribute( InstConfig config, Vector attributes ) {

      boolean change;          // Any selected config attribute has changed?
      Enumeration ekey;        // Enumeration of the `this' config
      int i;                   // Loop counter
      String key;              // A key in the hashTable

// Assume that the result is false unless a mismatch is found.
      change = false;
      
// Loop through all the attributes to be tested.
      for ( i = 0; i < attributes.size(); i++ ) {
         key = (String) attributes.elementAt( i );

// Check that the key is present.  Just ignore missing keys.
         if ( containsKey( key ) && config.containsKey( key ) ) {

// Compare the two values.  Exit the test once one attribute has a
// different value.
            if ( ! get( key ).equals( config.get( key ) ) ) {
               change = true;
               break;
            }

         }
      }
      return change;
   }

}
