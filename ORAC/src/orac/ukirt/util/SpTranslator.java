package orac.ukirt.util;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.SpAvTable;
import gemini.sp.SpItem;
import gemini.sp.SpNote;
import gemini.sp.SpObs;
import gemini.sp.SpObsGroup;
import gemini.sp.SpObsContextItem;
import gemini.sp.SpObsFolder;
import gemini.sp.SpOffsetPosList;
import gemini.sp.SpProg;
import gemini.sp.SpRootItem;
import gemini.sp.SpTreeMan;
import gemini.sp.SpType;
import gemini.sp.iter.SpIterChop;
import gemini.sp.iter.SpIterComp;
import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterFolder;
import gemini.sp.iter.SpIterOffset;
import gemini.sp.iter.SpIterRepeat;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;
import gemini.util.CoordSys;
import gemini.util.RADecMath;
import orac.ukirt.inst.SpDRRecipe;
import orac.ukirt.inst.SpUKIRTInstObsComp;
import orac.ukirt.inst.SpInstCGS4;
import orac.ukirt.inst.SpInstIRCAM3;
import orac.ukirt.inst.SpInstMichelle;
import orac.ukirt.inst.SpInstUFTI;
import orac.ukirt.inst.SpInstUIST;
import orac.ukirt.inst.SpUKIRTInstObsComp;
//import ot_ukirt.util.InstApertures;
//import ot_ukirt.util.InstConfig;
import orac.util.SpItemDOM;
import java.io.*;
import java.text.BreakIterator;
import java.text.DecimalFormat;
// Added by RDK
import java.text.SimpleDateFormat;
import java.util.Date;
// End of added by RDK
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * The SpTranslator class translates the instrument setup and observation
 * sequence into form suitable for UKIRT.  These are currently the
 * sequence text files for all UKIRT instruments, the new-style
 * attribute = value configuration text files for UFTI, Michelle, and
 * UIST; and old-style configuration text files for CGS4 and IRCAM3.
 */
public class SpTranslator {

// Define some class identifiers.
   String configDirName;               // Config directory name
   String filePrefix;                  // Filename prefix
   String sequenceDirName;             // Sequence directory name
   SpObs spObs;                        // Observation sequence

   final String defInst = "define_inst";
   final String setChopBeam = "SET_CHOPBEAM A";

/**
 *  Constructor
 */
    public SpTranslator( SpObs spobs ) {
       spObs = spobs;
    }

/**
 *  Determines whether or not the component (other than a config)
 *  contain configuration information.  
 *
 *  title is the component's title.
 *
 */
    public static boolean containsConfigInfo( String title ) {

      return title.equalsIgnoreCase( "flat" ) ||
             title.equalsIgnoreCase( "dark" ) ||
             title.equalsIgnoreCase( "bias" ) ||
             title.equalsIgnoreCase( "arc" )  ||
	     title.equalsIgnoreCase( "TargetAcq" );
    }

/**
 * Insert BREAK instructions into a sequence.
 *
 * @param String the instrument. So far CGS4, IRCAM3, MICHELLE
 *        UFTI, and UIST are supported.
 * @param Vector the sequence instructions (this is updated).
 */
   public void insertBreaks( String instrument, Vector sequence ) {

      boolean firstObject = false;        // First "set OBJECT" encountered?
      int i;                              // Loop counter

      if ( instrument.equalsIgnoreCase( "CGS4" ) ||
           instrument.equalsIgnoreCase( "MICHELLE" ) ||
           instrument.equalsIgnoreCase( "UFTI" ) ||
           instrument.equalsIgnoreCase( "UIST" ) ||
           instrument.equalsIgnoreCase( "IRCAM3" ) ) {

// Traverse the sequence.
         for ( i = 0; i < sequence.size(); ++i ) {

// Look for each "set FLAT" for CGS4.  Insert a break in the sequence
// after that instruction.
            if ( instrument.equalsIgnoreCase( "CGS4" ) || 
                 instrument.equalsIgnoreCase( "UIST" ) ||
                 instrument.equalsIgnoreCase( "MICHELLE" ) ) {
               if ( ( (String) sequence.elementAt( i ) ).equals( "set FLAT" ) ) {
                  sequence.insertElementAt( "break", ++i );
               }
	       // Added by RDK
	       if ( ( (String) sequence.elementAt( i ) ).equalsIgnoreCase( "set TargetAcq" ) ) {
		   sequence.insertElementAt( "breakForMovie", ++i );
	       }
	       // End of added by RDK
            }

// Look for first "set OBJECT".
            if ( ( (String) sequence.elementAt( i ) ).equals( "set OBJECT" ) &&
                 ! firstObject ) {

// It's found, so record the fact.
               firstObject = true;

// Add the break.
               sequence.insertElementAt( "break", ++i );
            }
         }
      }
   }

/**
 * Insert a "set <obstype>" instruction immediately following each
 * loadConfig command in the sequence, except the first.
 *
 * @param Vector the sequence instructions (this is updated).
 */
   public void insertConfigObstype( Vector sequence ) {

      int i;                              // Loop counter
      int j;                              // Loop counter
      String obsType = "set OBJECT";      // Command to set the current observe
                                          // type
      boolean setFound;                   // Intermediate "set <obstype>"
                                          // instruction present

// Traverse the sequence.
      for ( i = 0; i < sequence.size(); ++i ) {

// Record the current observe type.
         if ( ( (String) sequence.elementAt( i ) ).startsWith( "set " ) ) {
            obsType = (String) sequence.elementAt( i );
         }

// Look for a "loadConfig" instruction.
         if ( ( (String) sequence.elementAt( i ) ).startsWith( "loadConfig" ) ) {

// Starting from there, search forward to the next "do <N> _observe" command.
// Look for an intermediate "Set <obstype> command.
            setFound = false;
            for ( j = i + 1; j < sequence.size(); ++j ) {
               if ( ( (String) sequence.elementAt( j ) ).startsWith( "set " ) ) {
                  setFound = true;
               }

// Look for a "do n _observe" instruction.
               if ( ( (String) sequence.elementAt( j ) ).startsWith( "do" ) &&
                    ( (String) sequence.elementAt( j ) ).endsWith( "_observe" ) ) {
                  if ( ! setFound ) {

// Add the additional "set <obstype>" instruction immediately following the
// current "loadConfig" line.
                     ++i;
                     sequence.insertElementAt( obsType, i );

// Move the main instruction number to after the "do <N> _observe" command.
                     i = j + 1;
                  }
               }
            }
         }
      }
   }

/**
 * Insert essential group headers which must appear after the
 * startGroup.  For now assume that this is invoked immediately after
 * the startGroup instruction.  If placed before the first loadConfig,
 * observers may miss these instructions when they "start from highlight"
 * in ORAC-OM, i.e. when they do not need to slew.
 *
 * @param Vector the sequence instructions (this is updated).
 * @param String the "setHeader NOFFSETs <n>" instruction
 * @param String the minimum-schedulable-block identification
 * @param String the project name
 */
   public void insertGroupHeaders( Vector sequence, String noffsetsInstruction,
                                   String msbid, String project, 
				   String agent, String agentId) {

// Store minimum schedulable block identifier and project name to headers.
// These are delayed to be after the first loadConfig, so that
// observer do not by pass them in they are already on source.
      if ( msbid != null ) {

// Add the msbid-related instruction to the sequence buffer.
         sequence.addElement( "setHeader MSBID " + msbid );
      }

      if ( project != null ) {

// Add the project instruction to the sequence buffer.  Ensure the
// that PROJECT is in uppercase.
         sequence.addElement( "setHeader PROJECT " + project.toUpperCase() );
      }

      // Add the agent information if it exists.  Both fields must exist in order 
      // to write this information
      if ( agent != null && agentId!= null ) {
	  sequence.addElement( "setHeader RMTAGENT " + agent );
	  sequence.addElement( "setHeader AGENTID " + agentId);
      }
      else {
	  System.out.println("No remote trigger");
	  System.out.println("Src = "+agent);
	  System.out.println("ID = "+agentId);
      }

      if ( noffsetsInstruction != null ) {

// Finally insert the nOffsets.
         sequence.addElement( noffsetsInstruction );
      }
   }
   
/**
 * Insert a loadConfig instruction before the first set OBJECT in a
 * sequence, unless there are no preceeding "set <obstype>" commands,
 * or there is a "loadConfig" instruction following the last 
 * "set <obstype>" command.
 *
 * @param Vector the sequence instructions (this is updated).
 */
   public void insertObjectConfig( Vector sequence ) {

      boolean firstConfig = false;        // First "loadConfig" encountered?
      boolean firstObject = false;        // First "set OBJECT" encountered?
      boolean firstSet = false;           // First "set <obstype>" encountered?
      int i;                              // Loop counter
      String instruction = " ";           // Sequence instruction

// Traverse the sequence.
      for ( i = 0; i < sequence.size(); ++i ) {

// Look for the first "loadConfig" instruction.
         if ( ( (String) sequence.elementAt( i ) ).startsWith( "loadConfig" ) &&
              ! firstConfig ) {

// It's found, so record the fact and the instruction.
            firstConfig = true;
            instruction = (String) sequence.elementAt( i );
         }

// Look for first "set OBJECT".
         if ( ( (String) sequence.elementAt( i ) ).equals( "set OBJECT" ) &&
              ! firstObject ) {

// It's found, so record the fact.
            firstObject = true;

// We only need the extra loadConfig if there's no preceding "set <obstype>".
// There should always be a loadConfig, but check just in case.
            if ( firstSet && firstConfig ) {

// Add the additional loadConfig instruction immediately before the current
// "set OBJECT" line.
               sequence.insertElementAt( instruction, i );
            }

// Look for first "set <obstype>", where <obstype> is not OBJECT.
         } else if ( ( (String) sequence.elementAt( i ) ).startsWith( "set " ) &&
                     ! firstSet ) {

// It's found, so record the fact.
            firstSet = true;
         
// Look for a loadConfig from another instrument configuration following
// the last "set <obstype>", where <obstype> is not OBJECT.  In that
// case we want to use that configuration, unless that's associated with
// another "set <obstype>" (dealt by the previous clause).
         } else if ( ( (String) sequence.elementAt( i ) ).startsWith( "loadConfig " ) &&
                     firstSet ) {

// There is a config loaded, so use that rather than the original.
            firstSet = false;
         
         }
      }
   }


/**
 * Insert peakup instructions into a CGS4 sequence containing a sky
 * observe before the first object observe.  This should be invoked
 * before the insertBreaks method.
 *
 * @param String the instrument.
 * @param Vector the sequence instructions (this is updated).
 */
   public void insertPeakup( String instrument, Vector sequence ) {

      boolean firstObject = false;        // First "set OBJECT" encountered?
      boolean firstSky = false;           // First "set SKY" encountered?
      int i;                              // Loop counter
      int objectIndex = -1;               // Sequence index to first "set OBJECT"
      int skyIndex = -1;                  // Sequence index to first "set SKY"

      if ( instrument.equalsIgnoreCase( "CGS4" ) ) {

// Traverse the sequence.
         for ( i = 0; i < sequence.size(); ++i ) {

// Look for first "set SKY".
            if ( ( (String) sequence.elementAt( i ) ).equals( "set SKY" ) &&
                 ! firstSky ) {

// It's found, so record the fact, and the index.
               firstSky = true;
               skyIndex = i;
            }

// Look for first "set OBJECT".
            if ( ( (String) sequence.elementAt( i ) ).equals( "set OBJECT" ) &&
                 ! firstObject ) {

// It's found, so record the fact, and the index.
               firstObject = true;
               objectIndex = i;
            }
         }

// Does the sky come before the object?
         if ( skyIndex < objectIndex && firstSky ) {

// Insert the additional commands to enable a peakup on the object.
            sequence.insertElementAt( "offset 0 0", skyIndex );
            sequence.insertElementAt( "set OBJECT", skyIndex + 1 );
         }   
      }
   }

/**
 * Move the last "SET_CHOPBEAM A" instruction before the startGroup
 * to after the startGroup.
 *
 * @param Vector the sequence instructions (this is updated).
 */
   public void moveSetChopBeam( Vector sequence ) {

      boolean found = false;              // First "SET_CHOPBEAM A" encountered?
      boolean start = false;              // "startGroup" encountered?
      int i;                              // Loop counter
      int remove = -1;                    // Index of setChopBeam command to
                                          //remove from the sequence

// Traverse the sequence.
      for ( i = 0; i < sequence.size(); ++i ) {

// Look for a "startGroup" instruction.
         if ( ( (String) sequence.elementAt( i ) ).equals( "startGroup" ) ) {
            start = true;

// If we already have a a setChopBeam instruction, it needs to be moved.             
            if ( found ) {
               sequence.insertElementAt( setChopBeam, ++i );
               sequence.removeElementAt( remove );
            }
            
         } else if ( ( (String) sequence.elementAt( i ) ).equals( setChopBeam ) ) {
            found = true;
            remove = i;
         }
      }
   }


/**
 * Count the number of offsets associated with the scope of the given
 * sequence.  This method traverses the tree.
 *
 * @param SpItem: the science program defining the scope to search.
 * @param count: the number offsets counted so far.
 */
   public static int countOffsets (SpItem spif, int count) {

      SpItem child;                       // Child of the sequence
      Enumeration eseq;                   // Enumerated sequence
      SpIterOffset sio;                   // Offset iterator
      SpOffsetPosList sopl;               // Current Iterator Component

// Get an Enumeration of all the children of the Sequence.
      eseq = spif.children();

// Go through the children (=components) searching for an offset
// iterator.  Once found, get the list of positions, and hence the
// number of positions.  Sum this number to the count already supplied.
      while ( eseq.hasMoreElements() ) {
         child = (SpItem) eseq.nextElement();
         if ( child instanceof SpIterOffset ) {
            sio = (SpIterOffset) child;
            sopl =  sio.getPosList();
            count = count + sopl.size();
         }

// Track down the hierarchy.
         count = countOffsets( child, count );

      }
      return count;
   }


/**
 *  Removes all occurrences of a character from a string.
 */
   public static String expelChar( String str, char togo ) {
      StringBuffer buffer;           // Edited string (buffer needed for
                                     // concatenation)
      int end;                       // Character index of end of substring
      int strLength;                 // String length
      boolean more;                  // Are there more characters to remove?
      int start;                     // Character index of start of substring

// Obtain the untrimmed length of the supplied string.
      strLength = str.length();

// Create the string buffer, as least as long as the supplied string.
      buffer = new StringBuffer( strLength );

// Are there any occurrences?
      end = str.indexOf( togo );
      more = ( end != -1 );
      if ( more ) {

// Loop around extracting the text not containing the character.
         start = 0;
         while ( more ) {

// Only copy if the next character is wanted.  Copy up to and
// including the character preceding the one to remove.
            if ( start != end ) {
               buffer.append( str.substring( start, end ) );
            }

// Increment the index within the original string to after the
// unwanted character.
            start = end + 1;

// Check that we have not reached the end of the string.
            if ( start < strLength ) {

// Look for the next occurrence.
               end = str.indexOf( togo, start );
               more = ( end != -1 );
               if ( ! more ) {

// Append the remaining characters.
                  buffer.append( str.substring( start ) );
               }

// It's the end so no need to loop for more.
            } else {
               more = false;
            }
         }

         return buffer.toString();

// Character does not exist within the supplied string, so return the
// string unchanged.
      } else {
         return str;   
      }
   }

/**
 *  Gets the directory in which the config files are stored.
 */
   public String getConfigDirectory() {
      if ( configDirName == null ) {
         String cdn = new String();
         return cdn;
      } else {
         return configDirName;
      }
   }

/**
 *  Gets the prefix for the sequence and config files.
 */
   public String getPrefix() {
      if ( filePrefix == null ) {
         return "orac";
      } else {
         return filePrefix;
      }
   }

/**
 *  Gets the directory in which the sequence file is stored
 */
   public String getSequenceDirectory() {
      if ( sequenceDirName == null ) {
         String cdn = new String();
         return cdn;
      } else {
         return sequenceDirName;
      }
   }

/**
 * Generates a "do N _observe" instruction, and sets the type and
 * data-reduction recipe headers for the current data type.
 * Also moves an initial offset until after the "set " command.
 */

   private void observeCount( Vector sequence, String type,
                              SpDRRecipe drRecipeComp ) {

// Local variables.
      BreakIterator biw = BreakIterator.getWordInstance(); // Break
                                          // Iterator for words
      int end;                            // Char position of end of number
      String defDRRecipe = "QUICK_LOOK";  // Default data-reduction
                                          // recipe 
      String drRecipe;                    // Data-reduction recipe name
                                          // for supplied type 
      int i;                              // Loop counter
      String instruction = null;          // Returned instruction
      boolean moveOffset = false;         // Move an offset command
      String numberString;                // Number of observes
      int number;                         // Number of observes
      int numElements;                    // Number of sequence instructions
      String offsetCmd = " ";             // Offset command to be moved
      boolean otherPresent = false;       // Is there an another (not "set"
                                          // or "do N _observe") command before
                                          // the previous "set" instruction?
      String previousType;                // Penultimate instruction (set <obstype>)
      String previousInst;                // Previous instruction (do n _observe)
      int start;                          // Char position of start of number
      int toWait;                         // Sequence steps to "WAIT ALL"
      boolean typeInGroup;                // Frames of supplied type part of
                                          // group?
      String work;                        // Work variable

// Determine the data-reduction recipe name for this type of frame.
// ================================================================
      drRecipe = defDRRecipe;
      typeInGroup = true;
      if ( drRecipeComp != null ) {

// Obtain the recipe name for the given frame type and determine whether
// frames of that type are part of the group.
// Object frames
         if ( type.equalsIgnoreCase( "object" ) ) {
            drRecipe = drRecipeComp.getObjectRecipeName();
            typeInGroup = drRecipeComp.getObjectInGroup();

// Arc frames
         } else if ( type.equalsIgnoreCase( "arc" ) ) {
            drRecipe = drRecipeComp.getArcRecipeName();
            typeInGroup = drRecipeComp.getArcInGroup();

// Bias frames
         } else if ( type.equalsIgnoreCase( "bias" ) ) {
            drRecipe = drRecipeComp.getBiasRecipeName();
            typeInGroup = drRecipeComp.getBiasInGroup();

// Dark frames
         } else if ( type.equalsIgnoreCase( "dark" ) ) {
            drRecipe = drRecipeComp.getDarkRecipeName();
            typeInGroup = drRecipeComp.getDarkInGroup();

// Flat frames
         } else if ( type.equalsIgnoreCase( "flat" ) ) {
            drRecipe = drRecipeComp.getFlatRecipeName();
            typeInGroup = drRecipeComp.getFlatInGroup();

// Sky frames
         } else if ( type.equalsIgnoreCase( "sky" ) ) {
            drRecipe = drRecipeComp.getSkyRecipeName();
            typeInGroup = drRecipeComp.getSkyInGroup();

// If the type is not one of the expected types, set to defaults.
         } else {
            drRecipe = defDRRecipe;
            typeInGroup = false;

         }
      }

// In case a null drRecipe has been obtained, set it to the null
// recipe.
      if ( drRecipe == null ) { drRecipe = defDRRecipe; }

// Find the previous frame type and instrument.
// ============================================

// Obtain the number of instructions in the sequence so far.
      numElements = sequence.size();
      if ( numElements >= 2 ) {

// Search through the sequence from the end to find the last "set <obstype>"
// instruction.  Hence obtain the values of the last type and "do N _observe"
// instructions.  An offset ends the scope for object type.
         previousType = "undef";
         previousInst = "undef";
         for ( i = numElements - 1; i >= 0; i-- ) {
            work = (String) sequence.elementAt( i );
            if ( ! ( work.startsWith( "set " ) ||
                     work.startsWith( "do " ) ) ) {
               otherPresent = true;

            } else if ( work.startsWith( "set " ) ) {
               previousType = (String) sequence.elementAt( i );
	       if ( i < sequence.size()-1 )
		   previousInst = (String) sequence.elementAt( i + 1 );
               break;
            }
         }

// Same type: add to the observe count
// ===================================

// See if the current type of observe matches the previous one, and
// there are no other command present between.
         if ( previousType.endsWith( type ) && ! otherPresent ) {

// Supply the instruction to the BreakIterator.
            biw.setText( previousInst );

// Need to increment the value of the observe counter.  Find the
// character positions of the number.
            start = biw.next() + 1;
            end = biw.next( 2 );

// Extract the number as a string.  Note that it extracts a substring
// from index start to index end-1, not end.
            numberString = previousInst.substring( start, end );

// Convert to an int and increment the number of observes.
            number = Integer.parseInt( numberString ) + 1;

// Modify the last instruction.
            instruction = "do " + number + " _observe";
            sequence.setElementAt( instruction, i + 1 );

// Different type: insert more headers
// ===================================

// It was a different type, so will need to insert some new headers.
// First, however, these should come before an immediately preceding
// "offset" command and its associated "WAIT ALL" to preserve the
// logical order, and later to have the offset command after the first
// "set OBJECT" and "break" instructions.
         } else if ( ! previousType.endsWith( type ) ) {

// See if the last command is a "-WAIT ALL".  When it is we want to
// insert the commands before the preceding offset command.  Otherwise
// we merely append.  .insertElementAt( numElements ) is equivalent to
// .addElement.
            work = (String) sequence.lastElement();
            if ( work.startsWith( "-WAIT ALL" ) ) {
               i = numElements - 2;
            } else {

// The offset command may still be ahead of the "set OBJECT" command,
// say if no DARK frame is in the observation.  Remove these commands,
// recording the fact so they can be restored later.  Revise the count
// of sequence commands.  The exact number of steps back in the sequence
// depends on whether or there are OMP and polarimetry headers intervening
// or not.  Therefore search through the sequence backwards from the end
// to find the last (and should be only) "WAIT ALL" instruction.
               toWait = 0;
               for ( i = numElements - 1; i >= 0; i-- ) {
                  work = (String) sequence.elementAt( i );
                  if ( ! ( work.startsWith( "-WAIT ALL" ) ) ) {
                     toWait++;

                  } else {
                     break;
                  }
               }

// Decide whether there are instructions to move.
               moveOffset = false;
               if ( toWait != 0 && ( numElements - toWait - 2 ) >= 0 ) {

// Store the instructions which are expected to move.
                  work = (String) sequence.elementAt( numElements - toWait - 1 );
                  offsetCmd = (String) sequence.elementAt( numElements - toWait - 2 );

// The instructions are as expected, so remove them and record the fact.
                  if ( work.startsWith( "-WAIT ALL" ) &&
                       offsetCmd.startsWith( "offset" ) ) {
                     moveOffset = true;
                     sequence.removeElementAt( numElements - toWait - 1 );
                     sequence.removeElementAt( numElements - toWait - 2 );
                     numElements = sequence.size( );                  
                  }
               }

// We can merely append the header commands.                  
               i = numElements;
            }

// Add the new type and observe to the sequence.
            if ( typeInGroup ) {
               sequence.insertElementAt( "setHeader GRPMEM T", i );
            } else {
               sequence.insertElementAt( "setHeader GRPMEM F", i );
            }

// Also add the group-membership and data-reduction headers.  The
// "do 1 observe" should appear after an "offset" instruction.  This
// happens either because where we inserted the new commands, or more
// explicit insertion of an offset command.
            sequence.insertElementAt( "setHeader RECIPE " + drRecipe, i + 1 );
            sequence.insertElementAt( "set " + type.toUpperCase(), i + 2 );
            if ( moveOffset ) {
               sequence.addElement( offsetCmd );
               sequence.addElement( "-WAIT ALL" );
            }
	    // Don't need to do an observe if doing target aqcuisition, because observer
	    // will be running movie (added by RDK)
	    if (!type.equalsIgnoreCase("TargetAcq")) {
		sequence.addElement( "do 1 _observe" );
	    }

// Same types but intervening instruction.
// =======================================

// The types are the same, but there is another instruction (offset or
// loadConfig) offset between.  So set the type and observe in the
// sequence, but not change the recipe or group-membership headers.
// Is the repeated set <obstype> (usually an object) necessary?  Yes to
// demarcate when counting observations.
         } else {
            sequence.addElement( "set " + type.toUpperCase() );
	    // Don't need to do an observe if doing target aqcuisition, because observer
	    // will be running movie (added by RDK)
	    if (!type.equalsIgnoreCase("TargetAcq")) {
		sequence.addElement( "do 1 _observe" );
	    }

         }

// At the start so just set the type and one observe in the sequence.
      } else {
         if ( typeInGroup ) {
            sequence.addElement( "setHeader GRPMEM T" );
         } else {
            sequence.addElement( "setHeader GRPMEM F" );
         }
         sequence.addElement( "setHeader RECIPE " + drRecipe );
         sequence.addElement( "set " + type.toUpperCase() );
	 // Don't need to do an observe if doing target aqcuisition, because observer
	 // will be running movie (added by RDK)
	 if (!type.equalsIgnoreCase("TargetAcq")) {
	     sequence.addElement( "do 1 _observe" );
	 }
      }
   }

/**
 *  Sets the directory in which to store the config files.
 *  It makes the directory if it does not exist.
 */
   public void setConfigDirectory( String dirname ) {

      File dir = new File( dirname );
      String separator;

// Validate that it has a directory separator terminating the
// Use the File.separator method to obtain the directory separator.
      separator = dir.separator;
      if ( dirname.endsWith( separator ) ) {
         configDirName = dirname;
      } else {
         configDirName = dirname + separator;
      }

      dir = new File( configDirName );

// Validate that the directory exists.
      if ( !dir.exists() ) {

// Make the directory and any intermediate ones it needs.
         dir.mkdirs();
      }

   }

/**
 *  Sets the prefix for the sequence and config files.
 */
   public void setPrefix( String prefix ) {
      filePrefix = prefix;
   }

/**
 *  Sets the directory in which to store the sequence file.
 *  It makes the directory if it does not exist.
 */
   public void setSequenceDirectory( String dirname ) {

      File dir = new File( dirname );
      String separator;

// Validate that it has a directory separator terminating the
// Use the File.separator method to obtain the directory separator.
      separator = dir.separator;
      if ( dirname.endsWith( separator ) ) {
         sequenceDirName = dirname;
      } else {
         sequenceDirName = dirname + separator;
      }

// Validate that the directory exists.
      dir = new File( sequenceDirName );
      if ( !dir.exists() ) {

// Make the directory and any intermediate ones it needs.
         dir.mkdirs();
      }

   }

/**
 *  Translates the OT observation into an observing sequence and
 *  configurations.
 */
   public String translate() throws MissingValue, NumberFormatException,
          IOException {

// Declare variables.
// ==================
      final String nOffsets = "-setHeader NOFFSETS";
      final String setInst = "-set_inst";
      final String setRot = "setrotator";

      String apertures;                   // Space-separated list of
                                          // aperture values
      String attribute;                   // Config attribute 
      Vector breakAttribute = new Vector(); // CGS4 attributes when changed
                                          // requiring a break in the sequence
      SpAvTable cavl;                     // Instrument attribute value table
      SpItem child;                       // Child of the sequence
      String chopAngle;                   // Chopping position angle
      boolean chopping = false;           // Whether or not chopping requested
      String chopThrow;                   // Chopping displacement/throw
      Vector configArray = new Vector();  // Stores configs to be written to files
      boolean configInSequence = false;   // Is there a config in the sequence
      String coordSystem;                 // Co-ordinate system
      InstApertures currInstAper;         // Working instrument apertures
      InstConfig currConfig;              // Working instrument config
      String darkDRRecipe;                // Dark-frame data-reduction recipe
      String Dec;                         // Dec. sexagesimal value
      DecimalFormat dfEq;                 // Format code for equatorial
                                          // co-ordinates
      SpDRRecipe drRecipeComp;            // Data-reduction recipe component
      Enumeration eca;                    // Attributes of the instrument's 
                                          // config
      int end;                            // Index of end of substring
      Enumeration eprev;                  // Enumeration of sequence buffer
      String equinox;                     // Equinox of co-ordinate system
      Enumeration eseq;                   // Enumerated sequence
      Enumeration etav;                   // Enumerated attributes of a target
      Enumeration etl;                    // Enumerated target list 
      int i;                              // Loop counter 
      SpInstObsComp inst;                 // Instrument observation component
      String instruction;                 // Instruction in sequence
      String instrument;                  // Instrument for the sequence
      boolean isGuideTarget;              // Does the target attribute apply 
                                          // to the guide star?
      boolean isLegacyInstrum = true;     // Does the instrument use the old-style
                                          // execs and configs?
      int j;                              // Loop counter
      int k;                              // Loop counter
      String key;                         // Hashtable key (instrument attribute) 
//    String lat;                         // Sky latitude (e.g. Dec) sexagesimal
                                          // value
      int lastRepeatCount = 0;            // Previous count of iterator loop
//    String long;                        // Sky longitude (e.g. RA) sexagesimal
                                          // value
      String msbid;                       // Minimum schedulable block identification
      Float milli = new Float( 0.001 );   // One thousandth
      String mpmDec;                      // Proper motion in Dec. milli-arcsec/yr
      String mpmRA;                       // Proper motion in R.A. milli-arcsec/yr
      StringBuffer nodCommand;            // Buffer for building nod instruction
      String noffsetsInstruction;         // NOFFSETS setHeader instruction in sequence
      int numConfig = 0;                  // Number of configs
      int numberOffsets;                  // Number of offsets
      StringBuffer offsetCommand;         // Buffer for building offset instruction
      SpItem parent;                      // Parent item
      float pmDec = 0.0f;                 // Proper motion in Dec. arcsec/yr
      float pmRA = 0.0f;                  // Proper motion in R.A. arcsec/yr
      StringBuffer polAngleCommand;       // Buffer for building polAngle instruction
      boolean polariserConfig;            // Is the config a polariser iterator?
      String prevSeqIns;                  // Previous sequence instruction
      String prevTitle = "blank";         // Previous component title
      String project;                     // Project information
      String RA;                          // R.A. sexagesimal value
      InstApertures refInstAper;          // Reference instrument apertures
      InstConfig refConfig;               // Reference instrument config
      boolean removeConfig;               // Remove previous config?
      int removeIndex = -1;               // Sequence index of previous config
      boolean repeatIter = false;         // Sequence component is a
                                          // repeat iterator?
      boolean repeatIterPresent = false;  // A repeat iterator was present in
                                          // the sequence
      String rootConfigName;              // Base name of the configs
      int s;                              // Loop counter for sequence
      boolean sameConfig;                 // Current config is the same as last
                                          // stored config
      boolean saveConfig;                 // Save the current instrument
                                          // configuration?
      String seqName = "None";            // Sequence name
      Vector sequence = new Vector();     // Sequence instructions
      SpIterComp sic;                     // Current Iterator Component
      SpIterEnumeration sie;              // Enumeration of the Iterator
                                          // Component
      SpIterStep sis;                     // Iteration step
      SpIterValue siv;                    // Iteration attribute and values
      double skyCoords[];                 // Sky co-ordinates (lat then long) in
                                          // decimal degrees
      String slitAngle;                   // CGS4 slitposition angle
      int start;                          // Index of start of substring
      boolean standard;                   // Observation is of a standard?
      boolean startGroup = false;         // Is there a startGroup in the
                                          // sequence?
      boolean storedLevel;                // Is the iterator level is stored?
      String targetAttribute;             // Target (SpTelescopeObsComp) attribute
      SpTelescopeObsComp targetComponent; // A target component
      String targetName;                  // Name of the target
      boolean targetPresent = false;      // Target information is present?
      StringBuffer targetRecord;          // Builds a sequence target instruction
      String targetTypes [] = { "Base", "GUIDE" };  // Types of target
                                          // information required
      Vector targetValues;                // Values of base or guide position
      SpAvTable tavl;                     // Target attribute value table
      String timeTag;                     // Time tag used to make filenames
                                          // unique
      String title;                       // Sequence component's title
      boolean upLevel;                    // Going up a level in an iterator
                                          // hierarchy?
      boolean useRefConfig = true;        // Use the reference configuration, i.e.
                                          // not iterated
      Vector v;                           // Work Vector
      String value;                       // Attribute value
      String remoteTriggerSrc;
      String remoteTriggerId;


// Check if this is an Observation.
      if ( !( spObs.type().equals( SpType.OBSERVATION ) ) ) {
         spObs = findSpObs( spObs );
      }
      if ( spObs != null ) {

// Obtain OMP parameters.
// ======================

// SpObs is a subclass of SpMSB so SpObs objects are instances of SpMSB
// as well as SpObs.

// Obtain the minimum-schedulable-block identification.
         msbid = spObs.getTable().get( "msbid" );

// Obtain the project information.
         project = spObs.getTable().get( "project" );

	 // Get the eStar stuff
	 if ( spObs.isMSB() ) {
	     remoteTriggerSrc = spObs.getTable().get("remote_trigger_src");
	     remoteTriggerId  = spObs.getTable().get("remote_trigger_id");
	 }
	 else {
	     remoteTriggerSrc = spObs.parent().getTable().get("remote_trigger_src");
	     remoteTriggerId  = spObs.parent().getTable().get("remote_trigger_id");
	 }

// Code rearrangement by RDK (this section was after the "Define file name" stuff
// Obtain the instrument name and base configuration.
// ==================================================

// Determine the instrument name for this observation.
         inst = ( (SpInstObsComp) SpTreeMan.findInstrument( spObs ) );
         instrument = inst.type().getReadable();
// This line was added so that sequence and conf file names start with the instrument name (RDK)
         setPrefix(instrument + "_");
// End of rearrangement by RDK

// Define file name.
// =================

// Generate a unique filename.  Note the sequence file extension
// is .exec, not .seq.  Use the supplied prefix to form the name.
// Use a variable to store the tag so that the sequence and config
// have the same tag.
         timeTag = uniqueName();
         seqName = getPrefix() + timeTag + ".exec";
         rootConfigName = getPrefix() + timeTag + "_";

// Update the instrument apertures based upon what's in the instrument
// configuration, not in the source file.  This is needed because the
// apertures are changing every few weeks and old science programs
// are out of step.  
// COMMENTED OUT until it can be used in the OM.
// Put back by AB for tests: seems to work fine. Not committed yet
// until JAC ready. AB 8/06/01
         SpUKIRTInstObsComp uinst = (SpUKIRTInstObsComp) inst;
         uinst.setInstAper();

// Is this an observation of a standard?
         standard = spObs.getIsStandard();

// Write the flag to the header.
         if ( standard ) {
            sequence.addElement( "setHeader STANDARD T" );
         } else {
            sequence.addElement( "setHeader STANDARD F" );
         }

// Create a new default instrument configuration.  Initialise it.
         refConfig = new InstConfig();
         refConfig.init( instrument );

// Create and initialise a new default set of instrument apertures. 
         refInstAper = new InstApertures();
         refInstAper.init( instrument );

// Store the reference instrument config in the array of configs.
         configArray.addElement( refConfig );

// Determine if this is a legacy instrument, i.e. do we write out old-style
// configs?
         if ( instrument.equalsIgnoreCase( "CGS4" ) ||
              instrument.equalsIgnoreCase( "TUFTI/IRCAM" ) ||
              instrument.equalsIgnoreCase( "IRCAM3" ) ) {
            isLegacyInstrum = true;

         } else {
            isLegacyInstrum = false;
         }    
 
// Clone the current configuration.  This is a deep clone.
         currConfig = (InstConfig) refConfig.clone();

// Clone the current instrument apertures.  This is a deep clone.
         currInstAper = (InstApertures) refInstAper.clone();

// Added by RDK
// Update the attribute-value table to provide compatibility with SP's made with older versions of OT.
         uinst.avTableUpdate();

// End of added by RDK

// Obtain the instrument's attribute-value list.
         cavl = (SpAvTable) inst.getTable();

// Obtain an Enumeration of the config attributes.
         eca = cavl.attributes();

// Go through all the attributes.
         while ( eca.hasMoreElements() ) {
            attribute = ((String) eca.nextElement());

// Translate the OT attribute into a key in the InstConfig.
            key = refConfig.OTToTranslator( "config", attribute );
//            System.out.println("AV table attr is " + attribute + " value is " + cavl.get( attribute ) + " config key is " + key);
// Store the attribute and value(s) in the new InstConfig, provided
// it is an existing key.  Might want to remove this restriction for
// extensibility reasons.
            if ( refConfig.containsKey( key ) ) {

// Obtain the value as a string.
               value = (String) cavl.get( attribute );

// Store the attribute and value in the new InstConfig.
               currConfig.put( key, value );
//               System.out.println("Putting AV table attr " + attribute + " value " + value + " into config key " + key);

// Special case of instrument apertures, where there is a Vector of
// values.
            } else if ( key.equalsIgnoreCase( "instAper" ) ) {
               v = (Vector) cavl.getAll( attribute );

// Store the attribute and value(s) in the new InstConfig.
               currInstAper.arrayPut( v );

            } else {
//               System.out.println("Ignoring AV table attr " + attribute);
            }
         }

// Define a Vector of CGS4 attributes requiring a break if they have
// changed.
         if ( instrument.equalsIgnoreCase( "CGS4" ) ) {
            breakAttribute.addElement( "disperser" );
            breakAttribute.addElement( "centralWavelength" );
            breakAttribute.addElement( "order" );
            breakAttribute.addElement( "positionAngle" );
         }

// Extract the base and guide position values.
// ===========================================

// Find the target list.
         targetComponent = (SpTelescopeObsComp) SpTreeMan.findTargetList( spObs );
         if ( ! ( targetComponent == null ) ) {
            targetPresent = true;

// Obtain the target attribute-value list.
            tavl = (SpAvTable) targetComponent.getTable();

// Loop through the required attributes, in the required order.
            for ( i = 0; i < targetTypes.length; i++ ) {

// Check for the existence of the attribute.
               if ( tavl.exists( targetTypes[ i ] ) ) {

// There should be only one attribute-value pair.
                  targetAttribute = targetTypes[ i ];

// Define a useful boolean.
                  isGuideTarget = targetAttribute.equalsIgnoreCase( "GUIDE" );

// Obtain the values of the position as a Vector.
                  targetValues = tavl.getAll( targetAttribute );

// Obtain the raw mandatory strings.  Assume that the order is constant
// and that at least the first five (including zeroth "Base") are always
// present.  Remove spaces for the new TCS.
                  targetName = ( (String) targetValues.elementAt( 1 ) ).trim();
                  if ( targetName.length() == 0 ) { targetName = "unknown"; }
                  targetName = expelChar( targetName, ' ' );
                  targetName = expelChar( targetName, ',' );
                  RA = ( (String) targetValues.elementAt( 2 ) ).trim();
                  Dec = ( (String) targetValues.elementAt( 3 ) ).trim();
                  coordSystem = ( (String) targetValues.elementAt( 4 ) ).trim();

// Check for a missing co-ordinate.
                  if ( RA.equals( "" ) ) {
                     throw new MissingValue( "RA missing for " + targetName );
                  }
                  if ( Dec.equals( "" ) ) {
                     throw new MissingValue( "Dec missing for " + targetName );
                  }
 
// Obtain the raw optional strings.  Assign dummy values when not
// present in the target list values.  These will not be used, but it
// keeps the compiler happy.

// Convert the proper motions' milliarcseconds per year to arcseconds
// per year.  So convert to float and divide by 1000.
                  mpmRA = "";
                  mpmDec = "";

                  if ( targetValues.size() > 5 ) {
                     mpmRA = ( (String) targetValues.elementAt( 5 ) ).trim();
                     if ( ! mpmRA.equals( "" ) ) { 

// Convert the right-ascension proper motion milliarcseconds per year
// to arcseconds per year.  So convert to float and divide by 1000.
                        try {
                           pmRA = milli.floatValue() * Float.valueOf( mpmRA ).floatValue();
                        } catch ( NumberFormatException nfe ) {
                           System.out.println( "RA proper motion: " + mpmRA +
                                               " is not floating-point." );
                           return seqName;
                        }
                     }

                  }

                  if ( targetValues.size() > 6 ) {
                     mpmDec = ( (String) targetValues.elementAt( 6 ) ).trim();
                     if ( ! mpmDec.equals( "" ) ) { 

// Convert the declination proper motion milliarcseconds per year
// to arcseconds per year.  So convert to float and divide by 1000.
                        try {
                           pmDec = milli.floatValue() * Float.valueOf( mpmDec ).floatValue();
                        } catch ( NumberFormatException nfe ) {
                           System.out.println( "Dec proper motion: " + mpmDec +
                                               " is not floating-point." );
                           return seqName;
                        }
                     }
                  }

// Format the instruction for the sequence.
// ========================================

// This section in which the target coordinates are written to the sequence file
// was changed on 3 July 2003 by RDK. The TCS can now cope with target coordinates
// in sexagesimal format, so this section was simplified to make use of that capability.
// The only thing we have to do is make sure that in the sexagesimal string, the elements
// are separated by :, and not spaces, otherwise the OOS will complain.

		  if ( !(coordSystem.equalsIgnoreCase("AZ/EL")) ) {
		      RA = RA.replace(' ', ':');
		      Dec = Dec.replace(' ', ':');
		  }

// Need the equinox, not the FK number.  So look for the enclosing
// parentheses.
                  if ( coordSystem.equals( "" ) ) { 
                     equinox = "J2000";
                  } 
		  else if (coordSystem.equalsIgnoreCase("AZ/EL")) {
		      equinox = "AZEL"; // The TCS requires this exact string for Az/El
		  }
		  else {
                     start = coordSystem.indexOf( "(" ) + 1;
                     end = coordSystem.indexOf( ")" );
                     equinox = coordSystem.substring( start, end );
                  }

// Use a StringBuffer to form the record.  Store the mandatory items
// first.  Note the different command for a guide star.
                  targetRecord = new StringBuffer( 100 );
                  if ( isGuideTarget ) {
                     targetRecord.append( "SET_GUIDE ");
                  } else {
                     targetRecord.append( "SET_TARGET ");
                  }
                  targetRecord.append( targetName ).append( " " );
                  targetRecord.append( equinox ).append( " " );

		  targetRecord.append( RA );
		  targetRecord.append( " " );
		  targetRecord.append( Dec );

// Store the optional proper-motion items.
                  if ( targetValues.size() > 6 ) {
                     targetRecord.append( " " ).append( pmRA );
                     targetRecord.append( " " ).append( pmDec );
                  }

// Write the set-target instruction to the sequence.
                  sequence.addElement( targetRecord.toString() );

// Write the slew and guiding instructions.
// ========================================

// The slew and guiding instructions were changed by RDK on 3 July 2003.
// Before the slew, a command to set the tracking coordinate system was added.
// This was necessary to be able to make use of AZ/EL coordinates.

                  if ( isGuideTarget ) {
                      sequence.addElement( "-system " + equinox +" GUIDE" );
                      sequence.addElement( "do 1 _slew_guide" );
                      sequence.addElement( "GUIDE ON" );
                      if ( ! instrument.equalsIgnoreCase( "CGS4" ) ) {
                         sequence.addElement( "-WAIT ALL" );
                      }
                  } else {
                      sequence.addElement( "-system " + equinox +" ALL" );
                      sequence.addElement( "do 1 _slew_all" );
                  }
               }
	       else {
		   System.out.println("Table does not contain recognised target type");
	       }
            }

// Add chopping instructions.
// ==========================

// Determine whether there is chopping enabled in the target component.
            chopping = targetComponent.isChopping();
            if ( chopping ) {

// Obtain the chop throw in arcseconds and its orientation.
               chopThrow = targetComponent.getChopThrowAsString();
               chopAngle = targetComponent.getChopAngleAsString();

// Write sequence instructions.
               sequence.addElement( "-CHOP ChopOff" );
               sequence.addElement( "SET_CHOPTHROW " + chopThrow );
               sequence.addElement( "SET_CHOPPA " + chopAngle );
               sequence.addElement( "-DEFINE_BEAMS " + chopAngle + " " + chopThrow );
               sequence.addElement( "-CHOP ChopOn" );
               sequence.addElement( "-CHOP_EXTERNAL" );
               sequence.addElement( setChopBeam );
            }
         }

// Record the base configuration
// =============================

// Add the current instrument configuration into the Vector of all
// instrument configurations.  Note the deep cloning is needed so that a
// new copy is made, not just a copy of the pointer to the Hashtable.
// This entry and the sequence instruction below may be removed if there
// is a change to the config before an observe appears.
         configArray.addElement( currConfig.clone() );
         numConfig++;

// Specify the config in the sequence.  Note that this does not have the
// file extension (.conf).
         if ( isLegacyInstrum ) {
            instruction = "loadConfig " + rootConfigName.toUpperCase() + numConfig;
         } else {
            instruction = "loadConfig " + rootConfigName + numConfig;
         }
         sequence.addElement( instruction );

// Get the instrument apertures as a space-separated list, and
// write the instruction to set the instrument apertures into the
// sequence.  It's not needed if there is no target object.  These
// must appear before the pointing information, so it's easiest to
// place them at the start of the sequence.
         if ( targetPresent ) {
            apertures = currInstAper.getInstAper( instrument );
            sequence.insertElementAt( defInst + " " + instrument + 
                                      " " + apertures, 0 );

// Specify the instrument in the sequence.
            sequence.insertElementAt( setInst + " " + instrument, 1 );
         }

// CGS4, UIST, and Michelle have an extra degree of freedom: the slit
// position angle.
         if ( instrument.equalsIgnoreCase( "CGS4" ) || 
              instrument.equalsIgnoreCase( "UIST" ) ||
              instrument.equalsIgnoreCase( "Michelle" ) ) {
            slitAngle = (String) currConfig.get( "positionAngle" );
            if ( slitAngle != null ) {
               sequence.addElement( setRot + " " + slitAngle );
            }
         }

// Locate the data-reduction recipe component.
// ===========================================
         drRecipeComp = findRecipe( spObs );
         if ( drRecipeComp != null ) {
            darkDRRecipe = drRecipeComp.getDarkRecipeName();
         } else {
            darkDRRecipe = "REDUCE_DARK";
         }

// Find the sequence component.
// ============================

// This goes through the children of the SpObs looking for the one that
// has an SpType of SEQUENCE.  There really should be one (and only one)
// of these.
         SpIterFolder itf = null;
         for ( child = spObs.child(); child != null; child = child.next() ) {
            if ( child.type().equals( SpType.SEQUENCE ) ) {
               itf = (SpIterFolder) child;
               break;
            }
         }

// Set a header to store the number of offset positions.
// =====================================================

// Check that there is an OT Sequence.
         noffsetsInstruction = nOffsets + " 1";
         if ( itf != null ) {

// Count the number of offsets in the sequence.  Create an instruction
// for the sequence to store this value in the NOFFSETS header.  This must
// be done outside the loop as this is needed by data-reduction system in
// all frames.  This allows some recipes to be named generically,
// independent of the jitter size.  Make the command hidden to the user
// with a leading hyphen.
            numberOffsets = 0;
            numberOffsets = countOffsets( (SpItem) itf, numberOffsets );
            noffsetsInstruction = nOffsets + " " + numberOffsets;

//  Note while adding the instruction here is the clean (and original
//  implementation), in practice users of ORAC-OM were restarting from
//  the loadConfig which follows, not seeing the hidden instruction.
//  So it's placed after the startGroup.
//            sequence.addElement( instruction );
         }

// Generate a sequence from the sequence component.
// ================================================

// Check that there is an OT Sequence.
         if ( itf != null ) {

// Get an Enumeration of all the children of the Sequence.
            eseq = itf.children();

// Go through the children (=components) and for each one get all
// the elements of it (via the SpIterEnumeration class).  Skip
// over any notes.
            while ( eseq.hasMoreElements() ) {
               child = (SpItem) eseq.nextElement();
               if ( !( child instanceof SpNote ) ) {
                  sic = (SpIterComp) child;
                  sie = sic.elements();
               
// For each element get the title and the attributes/values
// Each element at this point should be a SpIterStep.
                  while ( sie.hasMoreElements() ) {
                     v = (Vector) sie.nextElement();

                     for ( i = 0; i < v.size(); ++i ) {
                        sis = (SpIterStep) v.elementAt( i );

// Use an instance variable, as there is no method, to determine whether
// the iterator step is a repeat loop.  In passing, the sis.title for
// a SpIterRepeat is for an unknown reason called "comment".
                        repeatIter = sis.item instanceof SpIterRepeat;

// Store waveplate angles in sequence.
// ===================================
                        polariserConfig = false;

                        if ( sis.title.equalsIgnoreCase( "config" ) ) {

// Obtain the attribute and value pairs.
                           for ( j = 0; j < sis.values.length; ++j ) {
                              siv = (SpIterValue) sis.values[ j ];

// Waveplate angles are not part of the instrument configuration
// stored in the config file, so aren't stored in the hashtable.
                              if ( siv.attribute.equalsIgnoreCase( "irpol" ) ) {

// Record the fact so that the config is unchanged.  It's more akin to
// an offset, so new config files are not required.
                                 polariserConfig = true;

// Here we just need the values, not the attribute.  Append the values
// in a StringBuffer.  Initialise with the command.
                                 polAngleCommand = new StringBuffer( 20 );
                                 polAngleCommand.append( "polAngle" );

// Obtain the waveplate angles.
                                 for ( j = 0; j < sis.values.length; ++j ) {
                                    siv = (SpIterValue) sis.values[ j ];

// Append the first value.
                                    polAngleCommand = polAngleCommand.append( " " ).append( siv.values[ 0 ] );
                                 }

// Add the observe instruction to the sequence buffer.
                                 instruction = polAngleCommand.toString();
                                 sequence.addElement( instruction );
                              }
                           }
                        }

// Update the working config.
// ==========================

// Need to keep track of the iterated components which contain
// config information.  Note that Fabry-Perot configurations has
// yet to be implemented.  No need to update the config for a 
// polariser iterator.
                        if ( ( sis.title.equalsIgnoreCase( "config" ) ||
                               containsConfigInfo( sis.title ) ) && 
                               !polariserConfig ) {

// Obtain the component title.
                           title = sis.title;

// Obtain the attribute and value pairs.
                           for ( j = 0; j < sis.values.length; ++j ) {
                              siv = (SpIterValue) sis.values[ j ];

// Translate the OT attribute into a key in the InstConfig.
                              attribute = siv.attribute;
                              key = refConfig.OTToTranslator( title, attribute );

// Unlike the other configuration information, the instrument-aperture 
// information is stored in its own data structure, so treat it as a
// special case.
                              if ( currInstAper.containsKey( key ) ) {

// Store the attribute and value in the current InstApertures.
                                 currInstAper.put( key, siv.values[ 0 ] );

                              } else {

// There is no config attribute written to the config for the dark exposure
// time, as it must equal the object exposure time.  So rename the attribute.
                                 if ( key.equals( "darkExpTime" ) ) {
                                    key = "expTime";
                                 }

                                 if ( ! key.startsWith( "FP" ) ) {

// Store the attribute and value in the current InstConfig.
// Inherit existing values for blank entries in the iterator.
// Note that the dark uses the object's attributes.
                                    if ( title.equalsIgnoreCase( "Flat" ) ) {
                                       currConfig.put( "type", "flat" );
                                    } else if ( title.equalsIgnoreCase( "Arc" ) ) {
                                       currConfig.put( "type", "arc" );
                                    } else if ( title.equalsIgnoreCase( "Bias" ) ) {
                                       currConfig.put( "type", "bias" );
                                    } else if ( title.equalsIgnoreCase( "Dark" ) ) {
                                       currConfig.put( "type", "object" );
				    } else if ( title.equalsIgnoreCase( "TargetAcq" ) ) {
					currConfig.put( "type", title.toUpperCase() );
				    }
				    // Added by RDK
				    if (instrument.equalsIgnoreCase( "UIST" ) &&
					title.equalsIgnoreCase( "config" )) {
					currConfig.put( "type", "object" );
                                    }
				    // End of added by RDK
                                    if ( ! siv.values[ 0 ].equals( "" ) ) {
					currConfig.put( key, siv.values[ 0 ] );
                                    }

                                 } else {

// Store Fabry-Perot co-ordinates in sequence.
// ===========================================

// Write instruction to the sequence.
                                    instruction = key + " " + siv.values[ 0 ];
                                    sequence.addElement( instruction );
                                 }
                              }
                           }

// Remove duplicated configs.
// --------------------------

// Check that the previous sequence instruction is not a config.  If
// it is we shall want to remove it.
                           prevSeqIns = "";
                           removeConfig = false;
                           if ( sequence.isEmpty() ||
                                configArray.isEmpty() ) {
                              removeConfig = false;

                           } else {

// In the case of Michelle and UIST we *need* the initial base config.  So
// never remove it.
                              if ( ! ( instrument.equalsIgnoreCase( "Michelle" ) ||
                                       instrument.equalsIgnoreCase( "UIST" ) ) ) {
                                 prevSeqIns = (String) sequence.lastElement();
                                 removeConfig = prevSeqIns.startsWith( "loadConfig" ) || 
                                                prevSeqIns.startsWith( setInst ) ||
                                                prevSeqIns.startsWith( setRot );
                                 removeIndex = 1;
                              }

// There is the case of the base config followed by a DR Recipe followed by a
// config that needs to be tested, so the superfluous base config can be
// removed.  Record which element to remove from the sequence.
                              if ( ! removeConfig &&
                                   prevSeqIns.startsWith( "setHeader RECIPE" ) ) {
                                 prevSeqIns = (String) sequence.elementAt( sequence.size() - 2 );
                                 removeConfig = prevSeqIns.startsWith( "loadConfig" ) ||
                                                prevSeqIns.startsWith( setInst ) ||
                                                prevSeqIns.startsWith( setRot );
                                 removeIndex = 2;

// There is the case of the base config followed by the offset header followed by a
// config that needs to be tested, so the superfluous base config can be
// removed.  Record which element to remove from the sequence.

// Note this may be unneccessary following move of nOffsets instruction to be
// immediately after the startGroup.
                              } else if ( ! removeConfig &&
                                   prevSeqIns.startsWith( nOffsets ) ) {
                                 prevSeqIns = (String) sequence.elementAt( sequence.size() - 2 );
                                 removeConfig = prevSeqIns.startsWith( "loadConfig" ) ||
                                                prevSeqIns.startsWith( setInst ) ||
                                                prevSeqIns.startsWith( setRot );
                                 removeIndex = 2;
                              }
                           }

// Remove previous config, since the second contains the combined information.
// Remove the unnecessary loadConfig from the sequence.
                           if ( removeConfig ) {
                              configArray.removeElementAt( configArray.size() - 1 );
                              numConfig--;
                              sequence.removeElementAt( sequence.size() - removeIndex );

// Remove additional sequence line associated with a new loadConfig
// for CGS4.  Have to redefine the previous instruction in case there
// are preceding apertures.
                              if ( prevSeqIns.startsWith( setRot ) ) {
                                 sequence.removeElementAt( sequence.size() - removeIndex );
                                 prevSeqIns = (String) sequence.elementAt( sequence.size() - removeIndex );
                              }

// Remove additional two sequence lines often associated with a new loadConfig.
                              if ( prevSeqIns.startsWith( setInst ) ) {
                                 sequence.removeElementAt( sequence.size() - removeIndex );
                                 sequence.removeElementAt( sequence.size() - removeIndex );
                              }
                           }

// Repeat calibration objects will have duplicated config information,
// and a series of "loadConfig x; set y; do 1 _observe" instructions
// in the sequence.  So need to compare the current configuration
// with the last stored set.  If they are the same we do not need to
// store another config in its array, and we can consolidate the
// the sequence instructions.
                           sameConfig = false;
//                           if ( sis.title.equals( prevTitle ) &&
//                                containsConfigInfo( sis.title ) ) {

                              sameConfig = currConfig.isSame( instrument,
                                           (InstConfig) configArray.lastElement() );
//                           }

// Add the current instrument configuration into the Vector of all
// instrument configurations.  Note the deep cloning is needed so that a
// new copy is made, not just a copy of the pointer to the Hashtable.
                           if ( ! sameConfig ) {
                              configArray.addElement( currConfig.clone() );
                              numConfig++;

// Specify the config in the sequence.  Note that this does not have the
// file extension (.conf).
                              if ( isLegacyInstrum ) {
                                 instruction = "loadConfig " + 
                                               rootConfigName.toUpperCase() +
                                               numConfig;
                              } else {
                                 instruction = "loadConfig " + rootConfigName +
                                               numConfig;
                              }
                              sequence.addElement( instruction );

// Get the instrument apertures as a space-separated list, and
// write the instruction to set the instrument apertures into the
// sequence.  It's not needed if there is no target object.
                              if ( targetPresent ) {
                                 apertures = currInstAper.getInstAper( instrument );
                                 sequence.addElement( defInst + " " + instrument + 
                                                      " " + apertures );

// Specify the instrument in the sequence.
                                 sequence.addElement( setInst + " " + instrument );
                              }


// CGS4, UIST, and Michelle have an extra degree of freedom: the slit
// position angle.
                              if ( instrument.equalsIgnoreCase( "CGS4" ) || 
                                   instrument.equalsIgnoreCase( "UIST" ) ||
                                   instrument.equalsIgnoreCase( "Michelle" ) ) {
                                 slitAngle = (String) currConfig.get( "positionAngle" );
                                 if ( slitAngle != null ) {
                                    sequence.addElement( setRot + " " + slitAngle );
                                 }
                              }
                           }

// Record the sequence instructions in the buffer.   Note the type
// written in uppercase within the sequence so refer to the type here
// in uppercase too.
                           if ( ! sis.title.equalsIgnoreCase( "config" ) ) {

// Add startGroup before the first "set <obstype>".
                              if ( ! startGroup ) {
                                 sequence.addElement( "startGroup" );
                                 startGroup = true;

// Insert the msbid, project, and nOffsets instructions afterr the new
// group so that they will not be missed by a expereienced observer.
                                 insertGroupHeaders( sequence, noffsetsInstruction,
                                                     msbid, project, 
						     remoteTriggerSrc, remoteTriggerId );
                              }

                              observeCount( sequence, (sis.title).toUpperCase(),
                                            drRecipeComp );
                           }

// Store offsets in sequence.
// ==========================
                        } else if ( sis.title.equalsIgnoreCase( "offset" ) ) {

// Here we just need the values, not the attribute.  Append the values
// in a StringBuffer.  Initialise with the command.
                           offsetCommand = new StringBuffer( 30 );
                           offsetCommand.append( "offset" );

// Obtain the offset values.
                           for ( j = 0; j < sis.values.length; ++j ) {
                              siv = (SpIterValue) sis.values[ j ];

// Append the first value.
                              offsetCommand = offsetCommand.append( " " ).append( siv.values[ 0 ] );
                           }

// Add the offset instruction to the sequence buffer.
                           instruction = offsetCommand.toString();
                           sequence.addElement( instruction );

// Also need a silent WAIT ALL to let the telescope finish moving before
// taking an exposure.
                           sequence.addElement( "-WAIT ALL" );

// Store chopping commands in sequence.
// ====================================
                        } else if ( sis.title.equalsIgnoreCase( "chop" ) ) {

// Add chopping instructions.
// ==========================

// Record that there was some chopping so that the beam may be reset at
// the end.
                           if ( !chopping ) {
                              chopping = true;
                           }

// Obtain the chop throw in arcseconds and its orientation.  This
// isn't the OO way, but will doas a stop-gap measure.
                           siv = (SpIterValue) sis.values[ 0 ];
                           chopThrow = (String) siv.values[ 0 ];
                           siv = (SpIterValue) sis.values[ 1 ];
                           chopAngle = (String) siv.values[ 0 ];

// Write sequence instructions.
                           sequence.addElement( "-CHOP ChopOff" );
                           sequence.addElement( "SET_CHOPTHROW " + chopThrow );
                           sequence.addElement( "SET_CHOPPA " + chopAngle );
                           sequence.addElement( "-DEFINE_BEAMS " + chopAngle + " " + chopThrow );
                           sequence.addElement( "-CHOP ChopOn" );
                           sequence.addElement( "-CHOP_EXTERNAL" );
                           sequence.addElement( setChopBeam );

// Store nodding observe commands.
// ===============================
                        } else if ( sis.title.equalsIgnoreCase( "nod" ) 
                                    && chopping ) {

// Add startGroup before the first "set OBJECT".
                           if ( ! startGroup ) {
                              sequence.addElement( "startGroup" );
                              startGroup = true;

// Insert the msbid, project, and nOffsets instructions afterr the new
// group so that they will not be missed by a expereienced observer.
                              insertGroupHeaders( sequence, noffsetsInstruction,
                                                  msbid, project,
						  remoteTriggerSrc, remoteTriggerId );
                           }

// Here we just need the values of the nod positions, not the attribute.  Append
// the values in a StringBuffer.  Initialise with the command.
                           nodCommand = new StringBuffer( 20 );

// Obtain the nod beam values.
                           for ( j = 0; j < sis.values.length; ++j ) {
                              siv = (SpIterValue) sis.values[ j ];

// Create the command for the current nod position.
                              nodCommand = nodCommand.append( "SET_CHOPBEAM " );
                              nodCommand = nodCommand.append( siv.values[ 0 ] );

// Add the observe instruction to the sequence buffer.
                              instruction = nodCommand.toString();
                              sequence.addElement( instruction );
                           }

// Store an observe command.
// =========================
                        } else if ( sis.title.equalsIgnoreCase( "observe" ) ) {

// Add startGroup before the first "set OBJECT".
                           if ( ! startGroup ) {
                              sequence.addElement( "startGroup" );
                              startGroup = true;

// Insert the msbid, project, and nOffsets instructions afterr the new
// group so that they will not be missed by a expereienced observer.
                              insertGroupHeaders( sequence, noffsetsInstruction,
                                                  msbid, project,
						  remoteTriggerSrc, remoteTriggerId);
                           }

// Add the observe instructions to the sequence buffer.  Note the type
// written in uppercase within the sequence so refer to the type here
// in uppercase too.
                           observeCount( sequence, "OBJECT", drRecipeComp );

// Special case for CGS4, Michelle, and UIST.  Need to add a break after a
// "set OBJECT" before the do n _observe, whenever certain configuration
// attributes have changed.  So look to see if any of the attributes
// potentially requiring a break changed.
                           if ( instrument.equalsIgnoreCase( "CGS4" ) || 
                                instrument.equalsIgnoreCase( "UIST" ) || 
                                instrument.equalsIgnoreCase( "Michelle" ) ) {
                              if ( currConfig.changedAttribute(
                                   (InstConfig) configArray.lastElement(),
                                   breakAttribute ) ) {

// Find the previous "set OBJECT".
                                 for ( s = sequence.size(); s > 0; s-- ) {
                                    if ( ( (String) sequence.elementAt( s ) )
                                         .startsWith( "set OBJECT" ) ) {

// Insert the "break" after the "set OBJECT".
                                       sequence.insertElementAt( "break", ++s );
                                       break;
                                    }
                                 }
                              }
                           }

// Store a sky command.
// ====================
                        } else if ( sis.title.equalsIgnoreCase( "sky" ) ) {

// Add startGroup before the first "set SKY".
                           if ( ! startGroup ) {
                              sequence.addElement( "startGroup" );
                              startGroup = true;

// Insert the msbid, project, and nOffsets instructions afterr the new
// group so that they will not be missed by a expereienced observer.
                              insertGroupHeaders( sequence, noffsetsInstruction,
                                                  msbid, project,
						  remoteTriggerSrc, remoteTriggerId);
                           }

// Add the observe instructions to the sequence buffer.  Note the type
// written in uppercase within the sequence so refer to the type here
// in uppercase too.
                           observeCount( sequence, "SKY", drRecipeComp );

                        }

// Store the previous component title.
                        prevTitle = sis.title;

// Insert a breakpoint at the end of each loop of a repeat iterator.
// note the use of an instance variable.
                        if ( repeatIter && sis.stepCount != lastRepeatCount ) {
                           lastRepeatCount = sis.stepCount;
                           sequence.addElement( "breakPoint" );
                        } else if ( repeatIter ) {
                           repeatIterPresent = true;
                        }
                     }
                  }

// Insert a breakpoint at the end of the final loop of a repeat iterator.
                  if ( repeatIterPresent ) {
                     sequence.addElement( "breakPoint" );
                  }
               }
            }

// Switch off chopping and return the beam to the middle.
            if ( chopping ) {
               sequence.addElement( "-CHOP ChopOff" );
               sequence.addElement( "SET_CHOPBEAM MIDDLE" );
            }

// Insert a peakup sequence when SKY comes before the first OBJECT
// observe.  This is currently only for CGS4.  Exclude the EMISSIVITY
// special case, which does have a SKY before the first OBJECT, but no
// peakup is required.
            if ( ( drRecipeComp == null ) ||  
                !( drRecipeComp.getObjectRecipeName().equalsIgnoreCase( "EMISSIVITY" ) ) ) {
               insertPeakup( instrument, sequence );
            }

// Add breaks to sequence.
            insertBreaks( instrument, sequence );

// Remove duplicated define_inst lines.
            removeDupInstAper( sequence );

// Insert a loadConfig before the first "set OBJECT", where there is a 
// preceding "set <obstype>".
            insertObjectConfig( sequence );

// Remove superfluous "set OBJECT" entries from the sequence.
            removeDupSetObject( sequence );

// Insert a "set " command immediately after each loadConfig, using
// the current observe type.
            insertConfigObstype( sequence );

// Move last "SET_CHOPBEAM A" that's before the startGroup, until after
// the startGroup instruction.
            moveSetChopBeam( sequence );

// As re-requested by Sandy Leggett to reduce latency effects.
// SKL requested the "set DARK" be inserted for UIST sequences as well (3 July 2003, RDK)
            if ( instrument.equalsIgnoreCase( "UFTI" ) ||
		 instrument.equalsIgnoreCase( "UIST" ) ) {
               sequence.addElement( "set DARK" );
            }

// Synchronisation of processes launched from OM need to complete
// before sequence ends.  Add a command to hold the OM until all of its
// associated processes have completed.
            sequence.addElement( "-ready" );

// Write the sequence file.
// ========================

// Create the sequence file.  Old-style execs are not used.
            writeSequence( sequence, getSequenceDirectory() + seqName );

// Write the config files.
// =======================

// Write the standard config.
            if ( !isLegacyInstrum ) {
               writeConfig( instrument, configArray,
                            getConfigDirectory() + rootConfigName );
            } else {
               writeLegacyConfig( instrument, configArray,
                                  getConfigDirectory(), rootConfigName );
            }
         }
      }

      return seqName;
   }

/**
 * Remove duplicate define_inst (and associated -set_inst) instructions 
 * from a sequence.
 *
 * @param Vector the sequence instructions (this is updated).
 */
   public void removeDupInstAper( Vector sequence ) {

      boolean firstDefInst = false;       // First "define_inst" encountered?
      int i;                              // Loop counter
      String instruct;                    // An instruction from the sequence
      String currentDefInst = " ";        // Current define-inst instruction
      int numInstruct;                    // Number of instructions in the
                                          // sequence

// Traverse the sequence.
      numInstruct = sequence.size();
      for ( i = 0; i < numInstruct; ++i ) {
         instruct = (String) sequence.elementAt( i );

// Look for the first "define_inst" command.  Once one is found, record 
// the fact, and go to the next instruction.
         if ( ! firstDefInst ) {
            if ( instruct.startsWith( defInst ) ) {
               currentDefInst = instruct;
               firstDefInst = true;
               continue;
            }
         }

// Look for the next "define_inst" command.
         if ( firstDefInst ) {
            if ( instruct.startsWith( defInst ) ) {

// Is the same define-inst instruction?
               if ( instruct.equals( currentDefInst ) ) {
                  
// It is, so we want to remove it to avoid resetting the apertures
// after a possible peakup.  Also remove the associated instruction
// that follows immediately.
                  sequence.removeElementAt( i );
                  sequence.removeElementAt( i );
                  numInstruct -= 2;

               } else {

// It's different, so make it the current define_inst command.
                  currentDefInst = instruct;
               }
            }
         }
      }
   }


/**
 * Remove superfluous "set OBJECT" instructions from a sequence.
 *
 * @param Vector the sequence instructions (this is updated).
 */
   public void removeDupSetObject( Vector sequence ) {

      int i;                              // Loop counter
      String instruct;                    // An instruction from the sequence
      boolean nextSetObj = false;         // First "set OBJECT" encountered?
      int numInstruct;                    // Number of instructions in the
                                          // sequence

// Traverse the sequence.
      numInstruct = sequence.size();
      for ( i = 0; i < numInstruct; ++i ) {
         instruct = (String) sequence.elementAt( i );

// Look for the next "set OBJECT" command.  Once one is found, record 
// the fact, and go to the next instruction.
         if ( ! nextSetObj ) {
            if ( instruct.startsWith( "set OBJECT" ) ) {
               nextSetObj = true;
               continue;
            }
         }

// Look for the next "set <obstype>" command if we currently are
// set to OBJECT obstype.
         if ( nextSetObj ) {

// Is it another "set OBJECT"?
            if ( instruct.equals( "set OBJECT" ) ) {

// It is, so we want to remove it to reduce overheads.
               sequence.removeElementAt( i );
               numInstruct -= 1;

            } else if ( instruct.startsWith( "set " ) ) {

// It's a different "set <obstype>", so we retain the "set " command,
// and flip the switch to look for the next "set OBJECT".
               nextSetObj = false;
            }
         }
      }
   }


/**
  * Generate a unique name for a file.   It currently uses the
  * system clock (System.currentTimeMillis()).  If this proves to
  * be too long, the first and last two digits could be stripped.
  */
   public static String uniqueName() {

// Commented by RDK
//      long time;                          // Milliseconds since
                                          // 1970 Jan 1.0.

//      time = System.currentTimeMillis();
//      return Long.toString( time );
// End of commented by RDK

// Added by RDK
      SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_kkmmss");
      return df.format(new Date()) ;
// End of added by RDK

   }

/** 
 * Helper function to write attribute values to config.  Two versions,
 * one assumes the keyname and attribute name are the same, the other 
 * allows them to be different.
 */
   private void writeAttribute( PrintWriter ow, InstConfig ic, 
                                String attrName, String keyName ) {
     ow.print( keyName + " = " + ic.get( attrName ) + "\n" );
   }

   private void writeAttribute( PrintWriter ow, InstConfig ic, 
                                String attrName) {
     ow.print( attrName + " = " + ic.get( attrName ) + "\n" );
   }

/**
  * Writes the instrument configurations to text files.
  */

   private void writeConfig( String instrum, Vector configArray,
                             String rootConfigName )
			     throws IOException {

      double angle;                       // Work positionAngle to 2 d.p.
      FileWriter conf;                    // File identifier for config
      int configNumber = 1;               // Config number
      String conName;                     // Config filename
      PrintWriter conpw;                  // PrintWriter for config text file
      Enumeration econ;                   // Enumerated configArray
      String filter;                      // Composite filter name
      Boolean nd;                         // Neutral-density filter in place?
      String polariser;                   // Polariser name
      InstConfig workConfig;              // Working instrument config

// Use an Enumeration to access each element of the array of configs.
      econ = configArray.elements();

// Skip over the first file.
      econ.nextElement();

// Loop through all the InstConfig instances.
      while ( econ.hasMoreElements() ) {

// Generate the filename.
         conName = rootConfigName + configNumber + ".conf";

// Open the config file.
         try {
            conf = new FileWriter( conName );
            conpw = new PrintWriter( conf );

// Access the current InstConfig.
            workConfig = (InstConfig) econ.nextElement();

// Determine whether or not the neutral-density filter is in place.
            if ( !( instrum.equalsIgnoreCase( "Michelle" ) ||
                    instrum.equalsIgnoreCase( "UIST" ) ) ) {
               nd = Boolean.valueOf( (String) workConfig.get( "neutralDensity" ) );

// Obtain the polariser. "none" means there is no polariser in place.
               polariser = (String) workConfig.get( "polariser" );
            } else {
               nd = Boolean.valueOf( "false" );
               polariser = "none";
            }

// Obtain the main filter.
            filter = (String) workConfig.get( "filter" );

// Write the formatted line to the file, appending the neutral density
// or polariser as appropriate.  Note that "prism" is equivalent to "pol".
// For the "none" case, nothing need be appended to the filter name.
            if ( nd.booleanValue() ) {
               filter = filter.trim() + "+ND";

            } else if ( polariser.equalsIgnoreCase( "prism" ) ) {  
               filter = filter.trim() + "+pol";

            }

// Need to add check for each new instrument to access the required keys
// in the InstConfig.

// UFTI
// ----
            if ( instrum.equalsIgnoreCase( "UFTI" ) ) {
               conpw.print( "instrument  = " +
                            workConfig.get( "instrument" ) + "\n" );
               conpw.print( "version     = " +
                            workConfig.get( "version" ) + "\n" );
               conpw.print( "filter      = " + filter + "\n" );
               conpw.print( "readMode    = " +
                            workConfig.get( "readMode" ) + "\n" );
               conpw.print( "readArea    = " +
                            workConfig.get( "readArea" ) + "\n" );
               conpw.print( "expTime     = " +
                            workConfig.get( "expTime" ) + "\n" );
               conpw.print( "objNumExp   = " +
                            workConfig.get( "objNumExp" ) + "\n" );
               conpw.print( "darkNumExp  = " +
                            workConfig.get( "darkNumExp" ) + "\n" );

// Michelle
// --------
            } else if ( instrum.equalsIgnoreCase( "Michelle" ) ) {

               writeAttribute( conpw, workConfig, "instrument" );
               writeAttribute( conpw, workConfig, "version" );
               writeAttribute( conpw, workConfig, "configType" );
               writeAttribute( conpw, workConfig, "type" );

               if ( workConfig.get( "type" ).equals( "object" ) ) {
                  writeAttribute( conpw, workConfig, "camera" );
                  writeAttribute( conpw, workConfig, "polarimetry" );
                  writeAttribute( conpw, workConfig, "slitWidth", "mask" );
                  writeAttribute( conpw, workConfig, "maskAngle" );
                  writeAttribute( conpw, workConfig, "positionAngle", "posAngle" );
                  writeAttribute( conpw, workConfig, "disperser" );
                  writeAttribute( conpw, workConfig, "order" );
                  writeAttribute( conpw, workConfig, "sampling" );
                  writeAttribute( conpw, workConfig, "centralWavelength" );
                  writeAttribute( conpw, workConfig, "filter" );
                  writeAttribute( conpw, workConfig, "waveplate" );
                  writeAttribute( conpw, workConfig, "scienceArea" );
                  writeAttribute( conpw, workConfig, "spectralCoverage" );
                  writeAttribute( conpw, workConfig, "pixelFOV" );
                  writeAttribute( conpw, workConfig, "nreads" );
                  writeAttribute( conpw, workConfig, "mode" );
                  writeAttribute( conpw, workConfig, "expTime", "exposureTime" );
                  writeAttribute( conpw, workConfig, "readInterval" );
                  writeAttribute( conpw, workConfig, "chopFrequency" );
                  writeAttribute( conpw, workConfig, "resetDelay" );
                  writeAttribute( conpw, workConfig, "nresets" );
                  writeAttribute( conpw, workConfig, "chopDelay" );
                  writeAttribute( conpw, workConfig, "objNumExp", "coadds" );
                  writeAttribute( conpw, workConfig, "waveform" );
                  writeAttribute( conpw, workConfig, "dutyCycle" );
                  writeAttribute( conpw, workConfig, "mustIdles" );
                  writeAttribute( conpw, workConfig, "nullReads" );
                  writeAttribute( conpw, workConfig, "nullExposures" );
                  writeAttribute( conpw, workConfig, "nullCycles" );
                  writeAttribute( conpw, workConfig, "idlePeriod" );
                  writeAttribute( conpw, workConfig, "observationTime" );
                  writeAttribute( conpw, workConfig, "darkFilter" );
                  writeAttribute( conpw, workConfig, "darkNumExp" );

               } else if ( workConfig.get( "type" ).equals( "flat" ) ) {
                  writeAttribute( conpw, workConfig, "flatSampling",
                                  "sampling" );
                  writeAttribute( conpw, workConfig, "flatSource" );
                  writeAttribute( conpw, workConfig, "flatFilter", "filter" );
                  writeAttribute( conpw, workConfig, "flatNreads", "nreads" );
                  writeAttribute( conpw, workConfig, "flatMode", "mode" );
                  writeAttribute( conpw, workConfig, "flatExpTime",
                                  "exposureTime" );
                  writeAttribute( conpw, workConfig, "flatReadInterval",
                                  "readInterval" );
                  writeAttribute( conpw, workConfig, "flatChopFrequency",
                                  "chopFrequency" );
                  writeAttribute( conpw, workConfig, "flatResetDelay",
                                  "resetDelay" );
                  writeAttribute( conpw, workConfig, "flatNresets", "nresets" );
                  writeAttribute( conpw, workConfig, "flatChopDelay",
                                  "chopDelay" );
                  writeAttribute( conpw, workConfig, "flatNumExp", "coadds" );
                  writeAttribute( conpw, workConfig, "flatWaveform",
                                  "waveform" );
                  writeAttribute( conpw, workConfig, "flatDutyCycle",
                                  "dutyCycle" );
                  writeAttribute( conpw, workConfig, "flatMustIdles",
                                  "mustIdles" );
                  writeAttribute( conpw, workConfig, "flatNullReads",
                                  "nullReads" );
                  writeAttribute( conpw, workConfig, "flatNullExposures",
                                  "nullExposures" );
                  writeAttribute( conpw, workConfig, "flatNullCycles",
                                  "nullCycles" );
                  writeAttribute( conpw, workConfig, "flatIdlePeriod",
                                  "idlePeriod" );
                  writeAttribute( conpw, workConfig, "flatObsTime",
                                  "observationTime" );

               } else if ( workConfig.get( "type" ).equals( "arc" ) ) {
//          writeAttribute( conpw, workConfig, "arcSampling", "sampling" );
                  writeAttribute( conpw, workConfig, "arcFilter", "filter" );
                  writeAttribute( conpw, workConfig, "arcNreads", "nreads" );
                  writeAttribute( conpw, workConfig, "arcMode", "mode" );
                  writeAttribute( conpw, workConfig, "arcExpTime",
                                  "exposureTime" );
                  writeAttribute( conpw, workConfig, "arcReadInterval",
                                  "readInterval" );
                  writeAttribute( conpw, workConfig, "arcChopFrequency",
                                  "chopFrequency" );
                  writeAttribute( conpw, workConfig, "arcResetDelay",
                                  "resetDelay" );
                  writeAttribute( conpw, workConfig, "arcNresets",
                                  "nresets" );
                  writeAttribute( conpw, workConfig, "arcChopDelay",
                                  "chopDelay" );
                  writeAttribute( conpw, workConfig, "arcNumExp", "coadds" );
                  writeAttribute( conpw, workConfig, "arcWaveform",
                                  "waveform" );
                  writeAttribute( conpw, workConfig, "arcDutyCycle",
                                  "dutyCycle" );
                  writeAttribute( conpw, workConfig, "arcMustIdles",
                                  "mustIdles" );
                  writeAttribute( conpw, workConfig, "arcNullReads",
                                  "nullReads" );
                  writeAttribute( conpw, workConfig, "arcNullExposures",
                                  "nullExposures" );
                  writeAttribute( conpw, workConfig, "arcNullCycles",
                                  "nullCycles" );
                  writeAttribute( conpw, workConfig, "arcIdlePeriod",
                                  "idlePeriod" );
                  writeAttribute( conpw, workConfig, "arcObsTime",
                                  "observationTime" );

               } else if ( workConfig.get( "type" ).equals( "bias" ) ) {
                  writeAttribute( conpw, workConfig, "biasExpTime",
                                  "exposureTime" );
                  writeAttribute( conpw, workConfig, "biasNumExp", "coadds" );
                  writeAttribute( conpw, workConfig, "biasSavedInt", "nreads" );
               }

// UIST
// ----
            } else if ( instrum.equalsIgnoreCase( "UIST" ) ) {

               writeAttribute( conpw, workConfig, "instrument" );
               writeAttribute( conpw, workConfig, "version" );
               writeAttribute( conpw, workConfig, "configType" );
               writeAttribute( conpw, workConfig, "type" );

               if ( workConfig.get( "type" ).equals( "object" ) ) {
                  writeAttribute( conpw, workConfig, "instPort" );
                  writeAttribute( conpw, workConfig, "camera" );
                  writeAttribute( conpw, workConfig, "imager" );
                  writeAttribute( conpw, workConfig, "filter" );
                  writeAttribute( conpw, workConfig, "focus" );
                  writeAttribute( conpw, workConfig, "polarimetry" );
                  writeAttribute( conpw, workConfig, "slitWidth", "mask" );
                  writeAttribute( conpw, workConfig, "maskWidth" );
                  writeAttribute( conpw, workConfig, "maskHeight" );
                  writeAttribute( conpw, workConfig, "disperser" );
// Commented by RDK
//                  writeAttribute( conpw, workConfig, "order" );
// End of commented by RDK

// Limit positionAngle to to decimal places.  Not having easy
// formatting, use a simple kludge to limit the decimal places,
// and replace the value in the configuration.
                  angle = Double.valueOf( (String) workConfig.get("positionAngle" ) ).doubleValue();
                  angle = Math.rint( 100.0d * angle ) / 100.0d;
                  workConfig.put( "positionAngle", (String) Double.toString( angle ) );
                  writeAttribute( conpw, workConfig, "positionAngle", "posAngle" );

                  writeAttribute( conpw, workConfig, "centralWavelength" );
                  writeAttribute( conpw, workConfig, "resolution" );
                  writeAttribute( conpw, workConfig, "dispersion" );
                  writeAttribute( conpw, workConfig, "scienceArea" );
                  writeAttribute( conpw, workConfig, "spectralCoverage" );
// Commented by RDK
//                   writeAttribute( conpw, workConfig, "pixelFOV" );
// End of commented by RDK
                  writeAttribute( conpw, workConfig, "pixelScale" );
                  writeAttribute( conpw, workConfig, "nreads" );
                  writeAttribute( conpw, workConfig, "mode" );
                  writeAttribute( conpw, workConfig, "expTime", "exposureTime" );
                  writeAttribute( conpw, workConfig, "readInterval" );
                  writeAttribute( conpw, workConfig, "chopFrequency" );
// Commented by RDK
//                  writeAttribute( conpw, workConfig, "resetDelay" );
//                  writeAttribute( conpw, workConfig, "nresets" );
// End of commented by RDK
                  writeAttribute( conpw, workConfig, "chopDelay" );
                  writeAttribute( conpw, workConfig, "objNumExp", "coadds" );
// Commented by RDK
//                  writeAttribute( conpw, workConfig, "waveform" );
// End of commented by RDK
                  writeAttribute( conpw, workConfig, "dutyCycle" );
// Commented by RDK
//                  writeAttribute( conpw, workConfig, "mustIdles" );
//                  writeAttribute( conpw, workConfig, "nullReads" );
//                  writeAttribute( conpw, workConfig, "nullExposures" );
//                  writeAttribute( conpw, workConfig, "nullCycles" );
//                  writeAttribute( conpw, workConfig, "idlePeriod" );
// End of commented by RDK
                  writeAttribute( conpw, workConfig, "observationTime" );
// Commented by RDK
//                  writeAttribute( conpw, workConfig, "darkFilter" );
// End of commented by RDK
// Commented by RDK
                 writeAttribute( conpw, workConfig, "darkNumExp" );
//                  writeAttribute( conpw, workConfig, "arrayAngle" );
//                  writeAttribute( conpw, workConfig, "readArea" );
//                   writeAttribute( conpw, workConfig, "refPixelX" );
//                   writeAttribute( conpw, workConfig, "refPixelY" );
//                   writeAttribute( conpw, workConfig, "refPixelL" );
//                   writeAttribute( conpw, workConfig, "refPixelS" );
// End of commented by RDK

// New items added by RDK
                  writeAttribute( conpw, workConfig, "pupil_imaging" );                  
                  writeAttribute( conpw, workConfig, "DAConf" );                  
                  writeAttribute( conpw, workConfig, "DAConfMinExpT" );                  
// End of new items added by RDK

               } else if ( workConfig.get( "type" ).equals( "flat" ) ) {
                  writeAttribute( conpw, workConfig, "flatSource" );
                  writeAttribute( conpw, workConfig, "flatFilter", "filter" );
                  writeAttribute( conpw, workConfig, "flatNreads", "nreads" );
                  writeAttribute( conpw, workConfig, "flatMode", "mode" );
                  writeAttribute( conpw, workConfig, "flatExpTime",
                                  "exposureTime" );
                  writeAttribute( conpw, workConfig, "flatReadInterval",
                                  "readInterval" );
                  writeAttribute( conpw, workConfig, "flatChopFrequency",
                                  "chopFrequency" );
// Commented by RDK
//                   writeAttribute( conpw, workConfig, "flatResetDelay",
//                                   "resetDelay" );
//                   writeAttribute( conpw, workConfig, "flatNresets", "nresets" );
// End of commented by RDK
                  writeAttribute( conpw, workConfig, "flatChopDelay",
                                  "chopDelay" );
                  writeAttribute( conpw, workConfig, "flatNumExp", "coadds" );
// Commented by RDK
//                   writeAttribute( conpw, workConfig, "flatWaveform",
//                                   "waveform" );
// End of commented by RDK
                  writeAttribute( conpw, workConfig, "flatDutyCycle",
                                  "dutyCycle" );
// Commented by RDK
//                   writeAttribute( conpw, workConfig, "flatMustIdles",
//                                   "mustIdles" );
//                   writeAttribute( conpw, workConfig, "flatNullReads",
//                                   "nullReads" );
//                   writeAttribute( conpw, workConfig, "flatNullExposures",
//                                   "nullExposures" );
//                   writeAttribute( conpw, workConfig, "flatNullCycles",
//                                   "nullCycles" );
//                   writeAttribute( conpw, workConfig, "flatIdlePeriod",
//                                   "idlePeriod" );
// End of commented by RDK
                  writeAttribute( conpw, workConfig, "flatObsTime",
                                  "observationTime" );

               } else if ( workConfig.get( "type" ).equals( "arc" ) ) {
                  writeAttribute( conpw, workConfig, "arcFilter", "filter" );
// Commented by RDK
//                   writeAttribute( conpw, workConfig, "arcOrder", "order" );
// End of commented by RDK
                  writeAttribute( conpw, workConfig, "arcCentralWavelength",
                                  "centralWavelength" );
                  writeAttribute( conpw, workConfig, "arcSpectralCoverage",
                                  "spectralCoverage" );
                  writeAttribute( conpw, workConfig, "arcNreads", "nreads" );
                  writeAttribute( conpw, workConfig, "arcMode", "mode" );
                  writeAttribute( conpw, workConfig, "arcCalLamp", "arcSource" );
                  writeAttribute( conpw, workConfig, "arcExpTime",
                                  "exposureTime" );
                  writeAttribute( conpw, workConfig, "arcReadInterval",
                                  "readInterval" );
                  writeAttribute( conpw, workConfig, "arcChopFrequency",
                                  "chopFrequency" );
// Commented by RDK
//                   writeAttribute( conpw, workConfig, "arcResetDelay",
//                                   "resetDelay" );
//                   writeAttribute( conpw, workConfig, "arcNresets",
//                                   "nresets" );
// End of commented by RDK
                  writeAttribute( conpw, workConfig, "arcChopDelay",
                                  "chopDelay" );
                  writeAttribute( conpw, workConfig, "arcNumExp", "coadds" );
// Commented by RDK
//                   writeAttribute( conpw, workConfig, "arcWaveform",
//                                   "waveform" );
// End of commented by RDK
                  writeAttribute( conpw, workConfig, "arcDutyCycle",
                                  "dutyCycle" );
// Commented by RDK
//                   writeAttribute( conpw, workConfig, "arcMustIdles",
//                                   "mustIdles" );
//                   writeAttribute( conpw, workConfig, "arcNullReads",
//                                   "nullReads" );
//                   writeAttribute( conpw, workConfig, "arcNullExposures",
//                                   "nullExposures" );
//                   writeAttribute( conpw, workConfig, "arcNullCycles",
//                                   "nullCycles" );
//                   writeAttribute( conpw, workConfig, "arcIdlePeriod",
//                                   "idlePeriod" );
// End of commented by RDK
                  writeAttribute( conpw, workConfig, "arcObsTime",
                                  "observationTime" );

               } else if ( workConfig.get( "type" ).equals( "bias" ) ) {
                  writeAttribute( conpw, workConfig, "biasExpTime",
                                  "exposureTime" );
                  writeAttribute( conpw, workConfig, "biasNumExp", "coadds" );
// Commented by RDK
//                   writeAttribute( conpw, workConfig, "biasNResets", 
//                                   "nresets" );
//                   writeAttribute( conpw, workConfig, "biasWaveform", 
//                                   "waveform" );
//                   writeAttribute( conpw, workConfig, "biasIdlePeriod", 
//                                   "idlePeriod" );
//                   writeAttribute( conpw, workConfig, "biasMustIdles", 
//                                   "mustIdles" );
//                   writeAttribute( conpw, workConfig, "biasReadArea", 
//                                   "readArea" );
//                   writeAttribute( conpw, workConfig, "biasRefPixelX", 
//                                   "refPixelX" );
//                   writeAttribute( conpw, workConfig, "biasRefPixelY", 
//                                   "refPixelY" );
// End of commented by RDK
                  writeAttribute( conpw, workConfig, "biasObsTime", 
                                  "observationTime" );
                  writeAttribute( conpw, workConfig, "biasDutyCycle", 
                                  "dutyCycle" );
	       } else if ( ((String)workConfig.get( "type" )).equalsIgnoreCase( "TargetAcq" ) ) {
		   writeAttribute( conpw, workConfig, "targetAcqExpTime", "exposureTime" );
		   writeAttribute( conpw, workConfig, "targetAcqNumExp", "coadds" );	
		   writeAttribute( conpw, workConfig, "targetAcqFilter", "filter" );
		   writeAttribute( conpw, workConfig, "targetAcqMask", "mask" );
		   writeAttribute( conpw, workConfig, "targetAcqMaskWidth", "maskWidth" );
		   writeAttribute( conpw, workConfig, "targetAcqMaskHeight", "maskHeight" );
		   writeAttribute( conpw, workConfig, "targetAcqDisperser", "disperser" );
		   writeAttribute( conpw, workConfig, "targetAcqResolution", "resolution" );
		   writeAttribute( conpw, workConfig, "targetAcqDispersion", "dispersion" );
		   writeAttribute( conpw, workConfig, "targetAcqScienceArea", "scienceArea" );
               }

            } else {
               // Throw exception
            }

// Flush remaining output and close the config file.
            conpw.flush();
            conpw.close();

         } catch ( IOException ioe ) {
            System.out.println( "IO error writing config file " + conName );
            System.out.println( "Error was: " + ioe );
            throw ioe;
         } 

// Keep a count for the file name.
         configNumber++;
      }
   }

/**  
 *  Formats a line in a Legacy configuration file for floating-point
 *  attributes.
 */
   private String formatFloatLegacyConfig( String value, String comment ) 
           throws MissingValue, NumberFormatException {
 
      String blanks;                      // Blanks to initialise StringBuffer
      DecimalFormat df;                   // Decimal format
      int i;                              // Loop counter
      StringBuffer line;                  // Buffer for config line
      double number;                      // Floating-point value
      boolean sign;                       // Negative sign present?

// Look for a null string.
      if ( value.equals( "" ) ) {
         throw new MissingValue( "Instrument config value for " +
                                  comment + " is absent." );
      }

// Convert the value to a double.  If it fails, print a contextual
// error message but rethrow the exception for higher methods to catch.
      try {
         number = ( Double.valueOf( value ) ).doubleValue();

      } catch( NumberFormatException e ) {
         System.out.println( "Unable to format config " + comment + " value '" + 
                             value + "' as a floating-point number." );
         throw e;
      }

// Record the presence of a negative number.  Temporarily change 
// value to positive to obtain the same alignment of the decimal point
// for a negative number.
      sign = number < 0.0;
      number = Math.abs( number );

// Legacy config normally uses an F10.4 formatted number.  Tried using
// the negative format, but it seems to be ignored.
      df = new DecimalFormat( "00000.0000" );

// Use a StringBuffer to insert the command from the beginning and
// the comment from column 23.  However, it appears that in must be
// initialised first (or perhaps its length changed).
      line = new StringBuffer( 51 );
      blanks = "                                                   ";
      line.append( blanks );
      line.insert( 1, df.format( number ) ).insert( 22, comment );

// Replace the leading zeroes with spaces except before the decimal
// point.  It's safe to look beyond the cvurrent character, as there
// must be a decimal point.  
      for ( i = 1; i < line.length(); i++ ) {
         if ( line.charAt( i ) == '0' &&
              ! ( line.charAt( i + 1 ) == '.' ) ) {
            line.setCharAt( i, ' ' );
         } else {

// Restore a negative sign.
            if ( sign ) { line.setCharAt( i - 1, '-' ); }
            break;
         }
      }

// Return the text as a String.
      return line.toString();
   }

/**
 *  Formats a line in a Legacy configuration file for integer
 *  attributes.
 */
   private String formatIntLegacyConfig( String value, String comment )
           throws MissingValue, NumberFormatException {
 
      String blanks;                      // Blanks to initialise StringBuffer
      DecimalFormat df;                   // Decimal format
      int i;                              // Loop counter
      StringBuffer line;                  // Buffer for config line
      int number;                         // Integer value

// Look for a null string.
      if ( value.equals( "" ) ) {
         throw new MissingValue( "Instrument config value for " +
                                  comment + " is absent." );
      }

// Convert the value to an integer.  If it fails, print a contextual
// error message but rethrow the exception for higher methods to catch.
      try {
         number = Integer.parseInt( value );

      } catch( NumberFormatException e ) {
         System.out.println( "Unable to format config " + comment + " value '" + 
                             value + "' as an integer." );
         throw e;
      }

// Legacy config demands an I6 formatted number.
      df = new DecimalFormat( "000000" );

// Use a StringBuffer to insert the command from the beginning and
// the comment from column 23.  However, it appears that in must be
// initialised first (or perhaps its length changed).
      line = new StringBuffer( 51 );
      blanks = "                                                   ";
      line.append( blanks );
      line.insert( 1, df.format( number ) ).insert( 22, comment );

// Replace the leading zeroes with spaces.
      for ( i = 1; i < line.length(); i++ ) {
         if ( line.charAt( i ) == '0' ) {
            line.setCharAt( i, ' ' );
         } else {
            break;
         }
      }

// Return the text as a String.
      return line.toString();
   }

/**
 *  Formats a line in a Legacy configuration file.  This is not
 *  suitable for integer values, which have to be justified. 
 */
   private String formatLegacyConfig( String value, String comment ) {
 
      String blanks;                      // Blanks to initialise StringBuffer
      StringBuffer line;                  // Buffer for config line

// Use a StringBuffer to insert the command from the beginning and
// the comment from column 23.  However, it appears that in must be
// initialised first (or perhaps its length changed).
      line = new StringBuffer( 51 );
      blanks = "                                                   ";
      line.append( blanks );
      line.insert( 1, value ).insert( 22, comment );
      return line.toString();
   }

/**
 *  Formats the title line in a Legacy configuration file.
 */
   private String formatLegacyConfigTitle( String type, String configName ) {
 
      String blanks;                      // Blanks to initialise StringBuffer
      StringBuffer line;                  // Buffer for config line

// Use a StringBuffer to insert the config name from the beginning and
// the comment from column 23.  However, it appears that in must be
// initialised first (or perhaps its length changed).
      line = new StringBuffer( 79 );
      blanks = "                                                                               ";
      line.append( blanks );
      line.insert( 1, type ).insert( 18, "configuration :" );
      line.insert( 34, configName ).insert( 74, ": 2.1" );
      return line.toString();
   }

/**
 *  Writes the legacy-instrument configurations to text files.
 */
   private void writeLegacyConfig( String instrum, Vector configArray,
                                   String configPath, String rootConfigName ) 
                                   throws IOException, MissingValue,
                                   NumberFormatException {

      String arcFilter;                   // Arc filter name
      BreakIterator biw = BreakIterator.getWordInstance(); // Break
                                          // Iterator for words
      FileWriter conf;                    // File identifier for config
      int chpixel;                        // Character position of "pixel"
      int chplus;                         // Character position of plus sign
      int configNumber = 1;               // Config number
      String conName;                     // Config filename
      PrintWriter conpw;                  // PrintWriter for config text file
      Enumeration econ;                   // Enumerated configArray
      String filter;                      // Filter name
      Float floatVal;                     // Work variable
      float floatNumber;                  // Work variable
      Boolean fnd;                        // Neutral-density filter in
                                          // place for flat?
      Boolean nd;                         // Neutral-density filter in place?
      int number;                         // Work variable
      String polariser;                   // Polariser name
      int samplingProduct;                // Product of sampling and sample range
      String text;                        // Work variable
      String value;                       // Config value
      float wavelength;                   // CVF wavelength
      InstConfig workConfig;              // Working instrument config

// Use an Enumeration to access each element of the array of configs.
      econ = configArray.elements();

// Skip over the first file.
      econ.nextElement();

// Loop through all the InstConfig instances.
      while ( econ.hasMoreElements() ) {

// Generate the filename.  Note that the file extension is .aim, not
// .conf, for legacy instruments, and that the name must be all lowercase.
         conName = configPath + rootConfigName.toLowerCase() + configNumber + ".aim";

// Open the config file.
         try {
            conf = new FileWriter( conName );
            conpw = new PrintWriter( conf );

// Access the current InstConfig.
            workConfig = (InstConfig) econ.nextElement();

// ****************************** CGS4 ********************************
            if ( instrum.equalsIgnoreCase( "CGS4" ) ) {

// Basic configuration attributes
// ==============================

// Heading
// -------
               conpw.print( formatLegacyConfigTitle( "ASTRONOMICAL",
                            rootConfigName + configNumber ) + "\n" );
               conpw.print( " Basic (object) configuration: \n" );

// readMode
// --------

// String may comprise speed and mode.  So remove any text up to and
// including the plus.
               value = (String) workConfig.get( "readMode" );
               chplus = value.indexOf( "+" );
               if ( chplus >= 0 ) {
                  value = value.substring( chplus );
               }

// Use verbatim unless NDSTARE.  Config seems to want ND_STARE.
               if ( value.equalsIgnoreCase( "NDSTARE" ) ) {
                  value = "ND_STARE";
               }

// Write the formatted line to the file.
               conpw.print( formatLegacyConfig( value,
                            "acquisition configuration" ) + "\n" );

// expTime, objNumExp and scans
// ----------------------------

// Write the formatted line to the file, using the validated
// floating-point exposure time.  Supplied integer values will appear
// as decimal.
               conpw.print( formatFloatLegacyConfig( (String)
                            workConfig.get( "expTime" ), "exposure time" ) +
                            "\n" );

// Write the formatted lines to the file, using the extracted and
// validated values.
               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "objNumExp" ),
                            "exposures/integ" ) + "\n" );

               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "savedInt" ),
                            "scans" ) + "\n" );

// sampling
// --------

// Somewhat perversely this is broken into two entries unlike say flatSampling.
               value = (String) workConfig.get( "sampling" );

// First character is the sampling.  It has values 1--4.  No need to
// validate value as these are menu items in the OT.
               text = value.substring( 0, 1 );
               samplingProduct = Integer.parseInt( text );

// Now derive the sample range, the third character.
               text = value.substring( 2, 3 );
               number = Integer.parseInt( text );

// Config needs the sampling entry to be the product of the sampling and
// the sample range.
               samplingProduct = samplingProduct * number;

// Write the formatted sampling line to the file.
               conpw.print( formatIntLegacyConfig( "" + samplingProduct,
                            "sampling" ) + "\n" );

// Write the formatted line to the file, using the extracted value.
               if ( number == 1 ) {
                  conpw.print( formatLegacyConfig( text +
                               "_pixel", "sample range" ) + "\n" );
               } else {
                  conpw.print( formatLegacyConfig( text +
                               "_pixels", "sample range" ) + "\n" );
               }

// positionAngle, filter, slitWidth & wavelength
// ---------------------------------------------

// Write the formatted line to the file, using the validated
// floating-point slit position angle.  Supplied integer values will
// appear as decimal.
               conpw.print( formatFloatLegacyConfig( (String)
                            workConfig.get( "positionAngle" ), 
                            "position angle" ) + "\n" );

// Obtain the filter.
               filter = (String) workConfig.get( "filter" );

// Allow for a special case, where "Blanks" is needed rather than the
// modern "Blank".
               if ( filter.equalsIgnoreCase( "Blank" ) ) {
                  filter = "Blanks";
               }

// Determine whether or not the neutral-density filter is in place.
               nd = Boolean.valueOf( (String) workConfig.get( "neutralDensity" ) );

// Obtain the polariser. "none" means there is no polariser in place.
               polariser = (String) workConfig.get( "polariser" );

// Write the formatted line to the file, appending the neutral density
// or polariser as appropriate.  Note that "prism" is equivalent to "pol".
               if ( nd.booleanValue() ) {
                  conpw.print( formatLegacyConfig( filter.trim() + "+ND",
                               "filter" ) + "\n" );

               } else if ( polariser.equalsIgnoreCase( "none" ) ) {  
                  conpw.print( formatLegacyConfig( filter, "filter" ) + "\n" );

               } else if ( polariser.equalsIgnoreCase( "prism" ) ) {  
                  conpw.print( formatLegacyConfig( filter.trim() + "+prism",
                               "filter" ) + "\n" );

               } else {
                  conpw.print( formatLegacyConfig( filter.trim() + "+" + 
                               polariser, "filter" ) + "\n" );
               }

// Slit width is an integer or floating-point number followed by "pixel".
// Make a substring of the numerical part.  Trap other cases in case the
// configuration file is changed; report any error, but continue with a
// default.
               value = (String) workConfig.get( "slitWidth" );
               chpixel = value.indexOf( "pixel" );
               if ( chpixel != -1 ) {
                  text = value.substring( 0, chpixel );
               } else {
                  text = value;
               }

// Try to parse as a floating-point number.  If this fails write a
// contextual error message, but leave the exception.
               try {
                  floatNumber = ( Float.valueOf( text ) ).floatValue();
               } catch ( NumberFormatException e ) {
                  System.out.println( "Slit width has unrecognised value: " + 
                                       value );
                  throw e;
               }

// Write the formatted line to the file, using the extracted value, appending
// _pixel(s).  Use the integer form for whole numbers (i.e. one and above).
               if ( floatNumber < 1.0 ) {
                  conpw.print( formatLegacyConfig( floatNumber +
                               "_pixels", "slit width" ) + "\n" );
               } else if ( ( int ) floatNumber == 1 ) {
                  conpw.print( formatLegacyConfig( (int) floatNumber +
                               "_pixel", "slit width" ) + "\n" );
               } else {
                  conpw.print( formatLegacyConfig( (int) floatNumber +   
                               "_pixels", "slit width" ) + "\n" );
               }

// Disperser
// ---------

// An underscore has to be inserted before the lpmm for the legacy config.
// Remove the resolution from the value for the echelle.
               value = (String) workConfig.get( "disperser" );
               if ( value.startsWith( "echelle" ) ) {
                  text = "echelle";
               } else {
                  number = value.indexOf( "lpmm" );
                  text = value.substring( 0, number ) + "_lpmm";
               }

// Write the text and comment.
               conpw.print( formatLegacyConfig( text, "grating" ) + "\n" );


// Central Wavelength
// ------------------

// Write the formatted line to the file, using the validated
// floating-point central wavelength.  Supplied integer values will
// appear as decimal.
               conpw.print( formatFloatLegacyConfig( (String)
                            workConfig.get( "centralWavelength" ), 
                            "wavelength" ) + "\n" );

// Grating order and cvf
// ---------------------

// Write the formatted lines to the file, using the extracted values.
               conpw.print( formatLegacyConfig( (String) workConfig.get( "order" ),
                            "order" ) + "\n" );

//                conpw.print( formatLegacyConfig( (String) workConfig.get( "cvfOffset" ),
//                             "cvf offset" ) + "\n" );
	       Double centalWavelength = new Double ( (String)workConfig.get("centralWavelength") );
	       Double cvfWavelength    = new Double ( (String)workConfig.get("cvfWavelength") );
	       Double offset           = new Double (  cvfWavelength.doubleValue() - centalWavelength.doubleValue() );
	       offset = new Double ( Math.rint(offset.doubleValue()*1000.0)/1000.0);
	       conpw.print( formatLegacyConfig( offset.toString(), "cvf offset" ) + "\n" );

// calibLamp, tunHalLevel, & lampEffAp
// -----------------------------------

// Write the formatted lines to the file, using the extracted values.
               conpw.print( formatLegacyConfig( (String) workConfig.get( "calibLamp" ),
                            "calibration lamp" ) + "\n" );

               conpw.print( formatLegacyConfig( (String) workConfig.get( "tunHalLevel" ),
                            "tungsten-halogen level" ) + "\n" );

               conpw.print( formatLegacyConfig( (String) workConfig.get( "lampEffAp" ),
                            "Lamp effective aperture" ) + "\n" );

// Flat configuration attributes
// =============================

// Heading
// -------
               conpw.print( " Flat configuration variant (from object): \n" );

// flatSampling and flatCalLamp
// ----------------------------

// Write the formatted lines to the file, using the extracted values.
               conpw.print( formatLegacyConfig( (String) workConfig.get( "flatSampling" ),
                            "Flat sampling" ) + "\n" );

// Extract the value and translate its various options from the OT menus into valid text for
// the config.  Write the formatted line to the file, using the modified extracted value.
               text = (String) workConfig.get( "flatCalLamp" );
               if ( text.startsWith( "Black" ) ) {
                  conpw.print( formatLegacyConfig( text.substring( 12, 15 ),
                               "calibration lamp" ) + "\n" );
               } else if ( text.startsWith( "Tungsten-Halogen") ) {
                  conpw.print( formatLegacyConfig( "t_h", "calibration lamp" ) + "\n" );
               } else {
                  conpw.print( formatLegacyConfig( "1.3", "calibration lamp" ) + "\n" );
               }

// flatFilter & flatNeutralDensity
// -------------------------------

// Determine whether or not the neutral-density filter is to be in place.
               fnd = Boolean.valueOf( (String) workConfig.get( "flatNeutralDensity" ) );

// Write the formatted line to the file, appending the neutral density
// or polariser as appropriate.  Note that "prism" is equivalent to "pol".
               if ( fnd.booleanValue() ) {
                  conpw.print( formatLegacyConfig( filter.trim() + "+ND",
                               "filter" ) + "\n" );

               } else if ( polariser.equalsIgnoreCase( "none" ) ) {  
                  conpw.print( formatLegacyConfig( filter, "filter" ) + "\n" );

               } else if ( polariser.equalsIgnoreCase( "prism" ) ) {  
                  conpw.print( formatLegacyConfig( filter.trim() + "+prism",
                               "filter" ) + "\n" );

               } else {
                  conpw.print( formatLegacyConfig( filter.trim() + "+" + 
                               polariser, "filter" ) + "\n" );
               }


// flatReadMode
// ------------

// Use verbatim unless NDSTARE.  Config seems to want ND_STARE.
               value = (String) workConfig.get( "flatReadMode" );
               if ( value.equalsIgnoreCase( "NDSTARE" ) ) {
                  value = "ND_STARE";
               }

// Write the formatted line to the file.
               conpw.print( formatLegacyConfig( value,
                            "acquisition configuration" ) + "\n" );

// flatExpTime, flatNumExp and flatSavedInt
// ----------------------------------------

// Write the formatted line to the file, using the validated
// floating-point flat exposure time.  Supplied integer values will
// appear as decimal.
               conpw.print( formatFloatLegacyConfig( (String)
                            workConfig.get( "flatExpTime" ), 
                            "flat exposure time" ) + "\n" );

// Write the formatted lines to the file, using the extracted and
// validated values.
               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "flatNumExp" ),
                            "flat exposures/integ" ) + "\n" );

               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "flatSavedInt" ),
                            "flat integrations" ) + "\n" );

// Dark
// ====

// Heading
// -------
               conpw.print( " Dark configuration variant (from object): \n" );

// darkExpTime and darkSavedInt
// ----------------------------

// Write the formatted lines to the file, using the extracted values.
               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "darkNumExp" ),
                            "dark exposure/integ" ) + "\n" );

               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "darkSavedInt" ),
                            "dark integrations" ) + "\n" );

// Bias
// ====

// Heading
// -------
               conpw.print( " Bias configuration variant (from object): \n" );

// biasExpTime, biasNumExp and biasSavedInt
// ----------------------------------------

// Write the formatted line to the file, using the validated
// floating-point bias exposure time.  Supplied integer values will
// appear as decimal.
               conpw.print( formatFloatLegacyConfig( (String)
                            workConfig.get( "biasExpTime" ), 
                            "bias exposure time" ) + "\n" );

// Write the formatted lines to the file, using the extracted and
// validated values.
               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "biasNumExp" ),
                            "bias exposures/integ" ) + "\n" );

               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "biasSavedInt" ),
                            "bias integrations" ) + "\n" );

// Arc
// ===

// Heading
// -------
               conpw.print( " Arc configuration variant (from object): \n" );

// arcCalLamp and arcFilter
// ------------------------


// The OT options have multi-word strings whereas the config needs to have the arc name---the
// first word in each OT option---in lowercase.  First extract the value in lowercase.
               text = ( (String) workConfig.get( "arcCalLamp" ) ).toLowerCase(); 

// Supply the lamp name to the BreakIterator.
               biw.setText( text );

// Find the character position of the end of the first word.  Use it to extract the
// first word.  Write the formatted line to the file, using the single-word arc name.
               conpw.print( formatLegacyConfig( text.substring( 0, biw.next() ),
                            "calibration lamp" ) + "\n" );

// Obtain the arc filter.
               arcFilter = (String) workConfig.get( "arcFilter" );

// Allow for a special case, where "Blanks" is needed rather than the
// modern "Blank".
               if ( arcFilter.equalsIgnoreCase( "Blank" ) ) {
                  arcFilter = "Blanks";
               }

// Write the formatted line to the file.
               if ( polariser.equalsIgnoreCase( "none" ) ) {  
                  conpw.print( formatLegacyConfig( arcFilter, "filter" ) + "\n" );

               } else {  
                  conpw.print( formatLegacyConfig( arcFilter.trim() + "+" + 
                               polariser, "filter" ) + "\n" );
               }

// arcCvfWavelength
// ----------------

// Obtain the value.  This is expected to be "TBD", i.e. it has to be
// evaluated from other attributes in the configuration.
               value = (String) workConfig.get( "arcCvfWavelength" );
               if ( value.equals( "TBD" ) ) {

// Do conversion of the wavelength and cvf offset attribute strings into
// floats via a Float object.  Add them and convert back into a string.
                  floatVal = new Float( (String) workConfig.get( "cvfWavelength" ) );
                  wavelength = floatVal.floatValue();
                  value = "" + wavelength;
               }

// Write the formatted line to the file, using the extracted or computed value.
               conpw.print( formatLegacyConfig( value, "cvf wavelength" ) + "\n" );

// arcReadMode 
// -----------
                            
// Use verbatim unless NDSTARE.  Config seems to want ND_STARE.  Set to uppercase.
               value = ((String) workConfig.get( "arcReadMode" )).toUpperCase();
               if ( value.equalsIgnoreCase( "NDSTARE" ) ) {
                  value = "ND_STARE";
               }

// Write the formatted line to the file.
               conpw.print( formatLegacyConfig( value,
                            "acquisition configuration" ) + "\n" );

// arcExpTime, arcNumExp and arcSavedInt
// -------------------------------------

// Write the formatted line to the file, using the validated
// floating-point arc exposure time.  Supplied integer values will
// appear as decimal.
               conpw.print( formatFloatLegacyConfig( (String)
                            workConfig.get( "arcExpTime" ), 
                            "arc exposure time" ) + "\n" );

// Write the formatted lines to the file, using the extracted and
// validated values.
               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "arcNumExp" ),
                            "arc exposures/integ" ) + "\n" );

               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "arcSavedInt" ),
                            "arc integrations" ) + "\n" );


// *************************** TUFTI/IRCAM *****************************
            } else if ( instrum.equalsIgnoreCase( "TUFTI/IRCAM" ) ||
                        instrum.equalsIgnoreCase( "IRCAM3" ) ) {

// Basic configuration attributes
// ==============================

// Heading
// -------
               conpw.print( formatLegacyConfigTitle( "IRCAM",
                            rootConfigName + configNumber ) + "\n" );
               conpw.print( " Basic (object) configuration: \n" );

// readMode
// --------

// String may comprise speed and mode.  So remove any text up to and
// including the plus.
               value = (String) workConfig.get( "readMode" );
               chplus = value.indexOf( "+" );
               if ( chplus >= 0 ) {
                  value = value.substring( chplus + 1 );
               }

// Use verbatim unless NDSTARE.  Config seems to want ND_STARE.
               if ( value.equalsIgnoreCase( "NDSTARE" ) ) {
                  value = "ND_STARE";
               }

// Write the formatted line to the file.
               conpw.print( formatLegacyConfig( value,
                            "acquisition configuration" ) + "\n" );

// expTime, objNumExp and scans
// ----------------------------

// Write the formatted line to the file, using the validated
// floating-point exposure time.  Supplied integer values will
// appear as decimal.
               conpw.print( formatFloatLegacyConfig( (String)
                            workConfig.get( "expTime" ), 
                            "exposure time" ) + "\n" );

// Write the formatted lines to the file, using the extracted values.
               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "objNumExp" ),
                            "exposures/integ" ) + "\n" );

               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "savedInt" ),
                            "scans" ) + "\n" );

// filter & magnifier
// ------------------

// Obtain the filter.
               filter = (String) workConfig.get( "filter" );

// Allow for a special case, where "Blanks" is needed rather than the
// modern "Blank".
               if ( filter.equalsIgnoreCase( "Blank" ) ) {
                  filter = "Blanks";
               }

// Determine whether or not the neutral-density filter is in place.
               nd = Boolean.valueOf( (String) workConfig.get( "neutralDensity" ) );

// Obtain the polariser. "none" means there is no polariser in place.
               polariser = (String) workConfig.get( "polariser" );

// Write the formatted line to the file, appending the neutral density
// or polariser as appropriate.  Note that "prism" is equivalent to "pol".
               if ( nd.booleanValue() ) {
                  conpw.print( formatLegacyConfig( filter.trim() + "+ND",
                               "filter" ) + "\n" );

               } else if ( polariser.equalsIgnoreCase( "none" ) ) {  
                  conpw.print( formatLegacyConfig( filter, "filter" ) + "\n" );

               } else if ( polariser.equalsIgnoreCase( "prism" ) ) {  
                  conpw.print( formatLegacyConfig( filter.trim() + "+pol",
                               "filter" ) + "\n" );

               } else {
                  conpw.print( formatLegacyConfig( filter.trim() + "+" + 
                               polariser, "filter" ) + "\n" );
               }


// Magnifier fixed for TUFTI, unlike IRCAM.
               conpw.print( formatLegacyConfig( "OUT", "magnifier" ) + "\n" );

// Flat configuration attributes
// =============================

// Heading
// -------
               conpw.print( " Flat configuration variant (from object): \n" );

// flatNumExp and flatSavedInt
// ---------------------------

// Write the formatted lines to the file, using the extracted values.
               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "flatNumExp" ),
                            "flat exposures/integ" ) + "\n" );

               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "flatSavedInt" ),
                            "flat integrations" ) + "\n" );

// Dark
// ====

// Heading
// -------
               conpw.print( " Dark configuration variant (from object): \n" );

// darkNumExp and darkSavedInt
// ---------------------------

// Write the formatted lines to the file, using the extracted values.
               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "darkNumExp" ),
                            "dark exposure/integ" ) + "\n" );

               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "darkSavedInt" ),
                            "dark integrations" ) + "\n" );

// Bias
// ====

// Heading
// -------
               conpw.print( " Bias configuration variant (from object): \n" );

// biasExpTime, biasNumExp and biasSavedInt
// ----------------------------------------

// Write the formatted line to the file, using the validated
// floating-point exposure time.  Supplied integer values will
// appear as decimal.
               conpw.print( formatFloatLegacyConfig( (String)
                            workConfig.get( "biasExpTime" ), 
                            "bias exposure time" ) + "\n" );

// Write the formatted lines to the file, using the extracted values.
               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "biasNumExp" ),
                            "bias exposures/integ" ) + "\n" );

               conpw.print( formatIntLegacyConfig( (String) workConfig.get( "biasSavedInt" ),
                            "bias integrations" ) + "\n" );

// Readout
// =======

// Write the formatted lines to the file, using the extracted values.
               conpw.print( formatLegacyConfig( (String) workConfig.get( "readArea" ),
                            "readout size" ) + "\n" );

// readMode string comprises speed and mode.  So remove any text beyond
// and including the plus.  If the speed is missing, just use the
// default.
               value = (String) workConfig.get( "readMode" );
               chplus = value.indexOf( "+" );
               if ( chplus >= 0 ) {
                  value = value.substring( 0, chplus );
               } else {
                  value = "Standard";
               }

// Write the formatted line to the file.
               conpw.print( formatLegacyConfig( value,
                            "readout speed" ) + "\n" );

// Need to throw an exception here if the instrument is unknown.

            }

// Flush remaining output and close the config file.
            conpw.flush();
            conpw.close();

// Write contextual error message.  Reset the exception for the calling
// method to deliver.
         } catch ( IOException ioe ) {
            System.out.println( "IO error writing config file " + conName );
            System.out.println( "Error was: " + ioe );
            throw ioe;
         } 

// Keep a count for the file name.
         configNumber++;
      }
   }

/**
 *  Writes the legacy exec file using the sequence buffer.
 *
 */
   private void writeLegacyExec( Vector sequence, String seqName ) {

      BreakIterator biw = BreakIterator.getWordInstance(); // Break
                                          // Iterator for words
      String command;                     // Exec command
      int end;                            // Char position of end of number
      Enumeration esequ;                  // Enumerated sequence
      boolean firstConfig = true;         // Is this the first config?
      int i;                              // Loop counter
      String instruction;                 // Sequence instruction
      String previous = null;             // Previous instruction
      String numberString;                // Number of observes
      int number;                         // Number of observes
      FileWriter seqf;                    // File identifier for sequence
      PrintWriter seqpw;                  // PrintWriter for sequence text file
      int start;                          // Char position of start of number
      String text;                        // A text buffer for substring
                                          // manipulation

// Open the sequence file.  Both the old and new use the .exec.
// file extension, so there's no need to hack the filename
      try {
         seqf = new FileWriter( seqName );
         seqpw = new PrintWriter( seqf );

// Write out exec Vector to the file.  Use an Enumeration to access
// each element.
         esequ = sequence.elements();

// Go through the sequence.
         while ( esequ.hasMoreElements() ) {
            instruction = (String) esequ.nextElement();

// Supply the instruction to the BreakIterator.
            biw.setText( instruction );

// Find the character position of the start of the second word of the
// instruction.  In most cases the second word (i.e. value) of a
// sequence instruction is unchanged.
            start = biw.next() + 1;


// Translate the sequence instructions into exec commands.
// =======================================================

// Note that some instructions are ignored, in particular the observe.

// config
// ------
            command = null;
            if ( instruction.startsWith( "loadConfig" ) ) {

// Extract the config name.  Form exec command.
               command = "config " + instruction.substring( start );

// Write the command to the file.
               seqpw.print( command.toUpperCase() + "\n" );

// Append commands which have no counterpart in the sequence, but
// which are needed in the exec.
               if ( firstConfig ) {
                  seqpw.print( "SET OBJECT" + "\n" );
                  seqpw.print( "BREAK" + "\n" );
               }

// Record that the next config will not be the first.
               firstConfig = false;

// observe
// -------
            } else if ( instruction.startsWith( "do" ) &&
                        instruction.endsWith( "observe" ) ) {

// Need to repeat the previous command if the number of "do"s is
// more than one.  So extract the count.  Find the character
// positions of the number.
               end = biw.next() + 1;

// Extract the number as a string.  Note that it extracts a substring
// from index start to index end-1, not end.
               numberString = instruction.substring( start, end );

// Convert to an int.
               number = Integer.parseInt( numberString );

// Repeat the previous command.  This will always be a "set <type>".
               if ( number > 1 ) {
                  command = previous.toUpperCase();
                  for ( i = 2; i <= number; ++i ) {
                     seqpw.print( command + "\n" );
                  }
               }

// slide
// -----
            } else if ( instruction.startsWith( "offset" ) ) {
 
// Extract the offsets.  Form exec command.
               command = "slide " + instruction.substring( start );

// Write the command to the file.
               seqpw.print( command.toUpperCase() + "\n" );

// recipe
// ------
            } else if ( instruction.startsWith( "setHeader RECIPE" ) ) {
 
// Extract the recipe name.  Form exec command.
               command = "DRRECIPE " + instruction.substring( start );

// Write the command to the file.
               seqpw.print( command.toUpperCase() + "\n" );

// type
// ----

// Note the space after set, as there is no legacy counterpart to the
// set_ instructions.
            } else if ( instruction.startsWith( "set " ) ) {
 
// Extract the type.  Form exec command.
               command = instruction.substring( start );

// Write the command to the file.
               seqpw.print( command.toUpperCase() + "\n" );

// startGroup
// ----------
            } else if ( instruction.startsWith( "newGroup" ) ) {
 
// Write the command to the file.
               command = "STARTGROUP";
               seqpw.print( command + "\n" );
            }

// Record the current command.
            previous = command;

         }

// Flush remaining output and close the sequence file.
         seqpw.flush();
         seqpw.close();

      } catch ( IOException ioe ) {
         System.out.println( "IO error writing sequence file " + seqName );
         System.out.println( "Error was: " + ioe );
      } 
   }

/**
 *  Writes the sequence file from the sequence buffer.
 *
 */
   private void writeSequence( Vector sequence, String seqName ) {

      Enumeration esequ;                  // Enumerated sequence
      FileWriter seqf;                    // File identifier for sequence
      PrintWriter seqpw;                  // PrintWriter for sequence text file

// Open the sequence file.
      try {
         seqf = new FileWriter( seqName );
         seqpw = new PrintWriter( seqf );

// Write out exec Vector to the file.  Use an Enumeration to access
// each element.
         esequ = sequence.elements();

// Go through the sequence.
         while ( esequ.hasMoreElements() ) {
            seqpw.print( (String) esequ.nextElement() + "\n" );
         }

// Flush remaining output and close the sequence file.
         seqpw.flush();
         seqpw.close();

      } catch ( IOException ioe ) {
         System.out.println( "IO error writing sequence file " + seqName );
         System.out.println( "Error was: " + ioe );
      } 
   }

/**
 * Find a data-reduction recipe component associated with the
 * scope of the given item.  This traverses the tree.
 *
 * @param spItem the SpItem defining the scope to search
 */
   public static SpDRRecipe findRecipe( SpItem spItem ) {

      SpItem child;                       // Child of spItem
      Enumeration children;               // Children of the sequence
      SpItem parent;                      // Parent of spItem
      SpItem searchItem;                  // The sequence item to search

      if ( spItem instanceof SpDRRecipe ) {
         return (SpDRRecipe) spItem;
      }

// Get the parent.
      parent = spItem.parent();

// Either the item is an observation context, which is
// what we want, or continue the search one level 
// higher in the hierarchy.
      if ( !(spItem instanceof SpObsContextItem) ) {
         searchItem = parent;
         if ( parent == null ) {
            return null;
         }
      } else {
         searchItem = spItem;
      }

// Search the observation context for the data-reduction
// recipe.
      children = searchItem.children();
      while ( children.hasMoreElements() ) {
         child = (SpItem) children.nextElement();
         if ( child instanceof SpDRRecipe ) {
            return (SpDRRecipe) child;

         }
      }

      if ( parent != null ) {
         return findRecipe( parent );
      }
      return null;
   }

/**
 * Find the parent Science Programme Observation associated with the
 * scope of the given item.  This traverses the tree.
 *
 * @param spItem the SpItem defining the scope to search
 */
   public static SpObs findSpObs( SpItem spItem ) {

      SpItem parent;                      // Parent of spItem

      if ( spItem.type().equals( SpType.OBSERVATION ) ) {
         return (SpObs) spItem;
      }
            
// Get the parent.
      parent = spItem.parent();

// Either the item is an observation context, which is
// what we want, or continue the search one level 
// higher in the hierarchy.
      if ( ! ( spItem.type().equals( SpType.OBSERVATION ) ) ) {
         if ( parent == null ) {
            return null;
         } else {
            return findSpObs( parent );
         }

      } else {
         return (SpObs) spItem;
      }
   }

}


/**
 *  MissingValue exception class is used to indicate that an
 *  object which should have a value is set to "".
 */

class MissingValue extends Exception {

// Constructors
   public MissingValue() {super(); }   
   public MissingValue( String s ) {super( s ); }

}
