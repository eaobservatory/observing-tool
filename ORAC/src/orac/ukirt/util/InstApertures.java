package orac.ukirt.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 *  This class defines a data structure which stores the instrument
 *  apertures for the Translator.
 *
 *  Currently only supports UFTI.
 *
 *  see SpTranslator
 */
public class InstApertures extends Hashtable implements Cloneable {

// Declare variables.
   private Hashtable instAperTable;
   private String key;           // Hashtable key
   private String value;         // Value corresponding to the above
                                 // Hashtable key

/**
 *  Constructor.  Could have another with instrum defaulted and invoking
 *  this constructor.
 */
   public InstApertures() {

// Start with 4 elements in the hash table and 0.75 loading.
      super( 4 );
   }

/**
 *  Assigns the instrument apertures using a Vector.  The values
 *  in the Vector should be in the order X, Y, Z, lambda.
 */
  public void arrayPut( Vector apers ) {

// Initialise the UFTI instrument apertures.
      put( "instAperX", (String) apers.elementAt( 0 ) );  // X aperture
      put( "instAperY", (String) apers.elementAt( 1 ) );  // Y aperture
      put( "instAperZ", (String) apers.elementAt( 2 ) );  // Z aperture
      put( "instAperL", (String) apers.elementAt( 3 ) );  // Lambda aperture

  }

/**
  * Creates a space-separated list of the instrument apertures for
  * a given instrument and configuration.
  */
   public String getInstAper( String instrum ) {

      String concat;                      // Space-separated list of
                                          // instrument apertures
      String lAperture;                   // Lambda instrument aperture
      String xAperture;                   // X instrument aperture
      String yAperture;                   // Y instrument aperture
      String zAperture;                   // Z instrument aperture

// Validate the instrument.  At one point there were instrument-specific
// handling of the apertures.  Now unified, so the code is simplified.
      concat = null;
      if ( instrum.equalsIgnoreCase( "UFTI" ) ||
           instrum.equalsIgnoreCase( "IRCAM3" ) ||
           instrum.equalsIgnoreCase( "CGS4" ) ||
           instrum.equalsIgnoreCase( "Michelle" ) ||
           instrum.equalsIgnoreCase( "UIST" ) ) {

// Get the individual values
         xAperture = (String) this.get( "instAperX" );
         yAperture = (String) this.get( "instAperY" );
         zAperture = (String) this.get( "instAperZ" );
         lAperture = (String) this.get( "instAperL" );

// Concatenate aperture values stored in the InstApertures Hashtable.
         concat = xAperture + " " + yAperture + " " + zAperture +
                  " " + lAperture;
      }
      return concat;
   }

/**
  * Initialises the instrument-aperture array for a given instrument.
  */

   public void init( String instrum ) {

// Use different initialisations for different instruments.
      if ( instrum.equalsIgnoreCase( "UFTI" ) ||
           instrum.equalsIgnoreCase( "IRCAM3" ) ) {

// Initialise the UFTI/IRCAM instrument apertures.
         put( "instAperX", "1" );        // X aperture
         put( "instAperY", "1" );        // Y aperture
         put( "instAperZ", "0" );        // Z aperture
         put( "instAperL", "2.2" );      // Lambda aperture

      } else if ( instrum.equalsIgnoreCase( "CGS4" ) ) {

// Initialise the CGS4 instrument apertures.
         put( "instAperX", "1" );        // X aperture
         put( "instAperY", "1" );        // Y aperture
         put( "instAperZ", "0" );        // Z aperture
         put( "instAperL", "2.2" );      // Lambda aperture

      } else if ( instrum.equalsIgnoreCase( "Michelle" ) ) {

// Initialise the Michelle instrument apertures.
         put( "instAperX", "1" );        // X aperture
         put( "instAperY", "1" );        // Y aperture
         put( "instAperZ", "0" );        // Z aperture
         put( "instAperL", "15.0" );      // Lambda aperture

      } else if ( instrum.equalsIgnoreCase( "UIST" ) ) {

// Initialise the UIST instrument apertures.
         put( "instAperX", "1" );        // X aperture
         put( "instAperY", "1" );        // Y aperture
         put( "instAperZ", "0" );        // Z aperture
         put( "instAperL", "5.0" );      // Lambda aperture

      }
   }

/** 
 *  Accessor method to return the hash table.
 */
   public Hashtable instAper() { return instAperTable; }

/**
 *  Deep-clone method required to copy the key-value configuration pairs.
 */
   public Object clone() {

      Object obj;                   // Object cloned
      Vector v;                     // Work vector

// Make a new Hashtable.
      InstApertures ia = new InstApertures();

// Loop through all the elements.  Here we assume that the values are
// scalar unless the instrument apertures.
      for ( Enumeration e = this.keys(); e.hasMoreElements(); ) {
         key = (String) e.nextElement();
         value = (String) this.get( key );
         ia.put( key, value );
      }

// Cast as Object because this is what clone() methods do.
      obj = (Object) ia;

// Return the cloned hashtable.
      return obj;
   }

/**
 * Compares two instApertures.  Returns true if they are identical.
 */

   boolean isSame( String instrum, InstApertures instAper ) {

      Enumeration ekey;        // Enumeration of the `this' config
      String key;              // A key in the hashTable
      boolean same;            // Configs are the same?

// Assume that the result is the same unless a mismatch is found.
      same = true;
      
      if ( instrum.equalsIgnoreCase( "UFTI" ) ||
           instrum.equalsIgnoreCase( "IRCAM3" ) ||
           instrum.equalsIgnoreCase( "CGS4" ) ||
           instrum.equalsIgnoreCase( "Michelle" ) ||
           instrum.equalsIgnoreCase( "UIST" ) ) {

// Get an enumeration of the keys.
         ekey = keys();
         while ( ekey.hasMoreElements() ) {
            key = (String) ekey.nextElement();

// Check that the key is present in the second config.
            if ( ! instAper.containsKey( key ) ) {
               same = false;
               break;
            }

// Compare the values in the two configs.
            if ( ! get( key ).equals( instAper.get( key ) ) ) {
               same = false;
               break;
            }
         }
      } 
      return same;
   }
}
