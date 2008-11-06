package orac.ukirt.util ;

import gemini.sp.SpAvTable ;
import gemini.sp.SpItem ;
import gemini.sp.SpRootItem ;
import gemini.sp.obsComp.SpTelescopeObsComp ;
import gemini.sp.SpTreeMan ;
import java.io.File ;
import java.io.IOException ;
import java.io.PrintWriter ;
import java.util.Enumeration ;
import java.util.Vector ;

/**
 * The SpUserFile class translates the target lists in a Science Program
 * and translates them into a UKIRT user file.  It can start from any
 * component in a Science Program.
 */
public class SpUserFile
{

	// Define some class identifiers.
	String userFileDirName ; // User file directory name
	String filePrefix ; // Filename prefix
	SpItem spItem ; // Item in a Science Program

	/**
	 *  Constructor
	 */
	public SpUserFile( SpItem spitem )
	{
		spItem = spitem ;
	}

	/**
	 *  Gets the directory in which the user files are stored.
	 */
	public String getUserFileDirectory()
	{
		if( userFileDirName == null )
			return new String() ;
		else
			return userFileDirName ;
	}

	/**
	 *  Gets the prefix for the user files.
	 */
	public String getPrefix()
	{
		if( filePrefix == null )
			return "orac" ;
		else
			return filePrefix ;
	}

	/**
	 *  Sets the directory in which to store the user files.
	 *  It makes the directory if it does not exist.
	 */
	public void setUserFileDirectory( String dirname )
	{
		File dir = new File( dirname ) ;

		// Validate that it has a directory separator terminating the File.separator method to obtain the directory separator.
		if( dirname.endsWith( File.separator ) )
			userFileDirName = dirname ;
		else
			userFileDirName = dirname + File.separator ;

		dir = new File( userFileDirName ) ;

		// Validate that the directory exists.
		// Make the directory and any intermediate ones it needs.
		if( !dir.exists() )
			dir.mkdirs() ;
	}

	/**
	 *  Sets the prefix for the user file.
	 */
	public void setPrefix( String prefix )
	{
		filePrefix = prefix ;
	}

	/**
	 *  Translates the OT target lists into a UKIRT user file.
	 */
	public String translate()
	{

		// Declare variables.
		// ==================
		String attribute ; // Observation context attribute
		Vector baseValues ; // Values of base position
		String coordSystem ; // Co-ordinate system
		String Dec ; // Dec. sexagesimal value
		int end ; // Index of end of substring
		String epoch ; // Epoch of target position
		String equinox ; // Equinox of co-ordinate system
		Enumeration etav ; // Enumerated attributes of a target
		Enumeration etl ; // Enumerated target list
		Float milli = new Float( 0.001 ) ; // One thousandth
		String mpmDec ; // Proper motion in Dec. milli-arcsec/yr
		String mpmRA ; // Proper motion in R.A. milli-arcsec/yr
		String parallax ; // Parallax in arcsec
		float pmDec ; // Proper motion in Dec. arcsec/yr
		float pmRA ; // Proper motion in R.A. arcsec/yr
		String RA ; // R.A. sexagesimal value
		int start ; // Index of start of substring
		SpTelescopeObsComp stl ; // A target list
		Vector<SpItem> targetLists ; // List of the target components
		String targetName ; // Name of the target
		SpAvTable tavl ; // Target attribute value table
		long timeTag ; // Time tag used to make filenames
		// unique
		PrintWriter ufpw ; // PrintWriter for user file
		SpRootItem spRootItem ; // Science Program
		String ufDec ; // Dec. sexagesimal value without colons
		String ufRA ; // R.A. sexagesimal value without colons
		String userFileName = null ; // File name for the user file
		StringBuffer userFileRecord ; // Builds a record in the user file

		// Get the root item from the arbitrary item.
		spRootItem = ( SpRootItem )spItem.getRootItem() ;

		// Find all the target lists.
		// ==========================

		// This finds all the SpItem components which are target lists and
		// stores them in a vector.
		targetLists = SpTreeMan.findAllItems( spRootItem , SpTelescopeObsComp.class.getName() ) ;

		// Generate a user file for each target list.
		// ==========================================

		// Check that there is at least one target list.
		if( targetLists.size() > 0 )
		{

			// Open user file.
			// ===============

			// Generate a unique filename from the system clock.  If this
			// proves to be too long, the first and last two digits could be
			// stripped.
			timeTag = System.currentTimeMillis() ;
			userFileName = getUserFileDirectory() + getPrefix() + timeTag + ".dat" ;

			// Open the user file.
			try
			{
				ufpw = new PrintWriter( userFileName ) ;

				// Extract the base position values
				// ================================

				// First get an Enumeration of all the targetLists.
				etl = targetLists.elements() ;

				// Go through the target lists and obtain each observation context.
				while( etl.hasMoreElements() )
				{
					stl = ( SpTelescopeObsComp )etl.nextElement() ;

					// Obtain the target attribute-value list.
					tavl = ( SpAvTable )stl.getTable() ;

					// Obtain an Enumeration of the attributes.
					etav = tavl.attributes() ;

					// Go through all the attributes.
					while( etav.hasMoreElements() )
					{

						// There should be only one attribute-value pair.
						attribute = ( ( String )etav.nextElement() ) ;

						// Look for the base attribute.
						if( attribute.equalsIgnoreCase( "Base" ) )
						{

							// Obtain the values of the base position as a Vector.
							baseValues = tavl.getAll( attribute ) ;

							// Obtain the raw mandatory strings.  Assume that the order is constant
							// and that at least the first five (including zeroth "Base") are always present.
							targetName = ( ( String )baseValues.elementAt( 1 ) ).trim() ;
							if( targetName == "" )
								targetName = "unidentified object" ;

							RA = ( ( String )baseValues.elementAt( 2 ) ).trim() ;
							Dec = ( ( String )baseValues.elementAt( 3 ) ).trim() ;
							coordSystem = ( String )baseValues.elementAt( 4 ) ;

							// Obtain the raw optional strings.  
							// Assign dummy values when not present in the target list values.  
							// These will not be used, but it keeps the compiler happy.
							if( baseValues.size() > 5 )
							{
								mpmRA = ( String )baseValues.elementAt( 5 ) ;
								if( mpmRA.equals( "" ) )
									mpmRA = "0.0" ;
							}
							else
							{
								mpmRA = "0.0" ;
							}

							if( baseValues.size() > 6 )
							{
								mpmDec = ( String )baseValues.elementAt( 6 ) ;
								if( mpmDec.equals( "" ) )
									mpmDec = "0.0" ;
							}
							else
							{
								mpmDec = "0.0" ;
							}

							if( baseValues.size() > 8 )
							{
								epoch = ( String )baseValues.elementAt( 8 ) ;
								if( epoch.equals( "" ) )
									epoch = "2000" ;
							}
							else
							{
								epoch = "2000" ;
							}

							if( baseValues.size() > 9 )
							{
								parallax = ( String )baseValues.elementAt( 9 ) ;
								if( parallax.equals( "" ) )
									parallax = "0.000" ;
							}
							else
							{
								parallax = "0.000" ;
							}

							// Format a line for the user file.
							// ================================

							// The strings have to be manipulated to correct format.
							// Substitute spaces for the colons in the sexagesimal values.
							// Note that it is a character substitution so use single quotes.
							ufRA = RA.replace( ':' , ' ' ) ;
							ufDec = Dec.replace( ':' , ' ' ) ;

							// Need the equinox, not the FK number.  So look for the enclosing parentheses.
							if( coordSystem.equals( "" ) )
							{
								equinox = "J2000" ;
							}
							else
							{
								start = coordSystem.indexOf( "(" ) + 1 ;
								end = coordSystem.indexOf( ")" ) ;
								equinox = coordSystem.substring( start , end ) ;
							}

							// Convert the proper motions' milliarcseconds per year to arcseconds
							// per year.  So convert to float and divide by 1000.
							pmRA = milli.floatValue() * Float.valueOf( mpmRA ).floatValue() ;
							pmDec = milli.floatValue() * Float.valueOf( mpmDec ).floatValue() ;

							// Use a StringBuffer to form the record.  This permits the co-ordinates to
							// start in column 22.  It appears that in must be initialised first (or
							// perhaps its length changed), so assign some blanks up to the insertion
							// column.  The initial 1 indicates that there is only one record per target.
							// Store the mandatory items first.
							userFileRecord = new StringBuffer( 100 ) ;
							userFileRecord.append( "1 " ) ;
							end = Math.min( 19 , targetName.length() ) ;
							userFileRecord.append( targetName.substring( 0 , end ) ) ;

							// Surely there is an easier way to format to a specified column.
							for( int i = end + 1 ; i < 21 ; ++i )
								userFileRecord.append( " " ) ;

							userFileRecord.append( ufRA ).append( "  " ) ;
							userFileRecord.append( ufDec ).append( "  " ) ;
							userFileRecord.append( equinox ).append( "  " ) ;

							// Store the optional items.
							if( baseValues.size() > 6 )
							{
								userFileRecord.append( pmRA ).append( " " ) ;
								userFileRecord.append( pmDec ).append( "  " ) ;
							}
							if( baseValues.size() > 8 )
								userFileRecord.append( epoch ).append( "  " ) ;
							if( baseValues.size() > 9 )
								userFileRecord.append( parallax ) ;

							// Write the line to the file.
							ufpw.print( userFileRecord + "\n" ) ;
						}
					}
				}
				// Flush remaining output and close the sequence file.
				ufpw.flush() ;
				ufpw.close() ;
			}
			catch( IOException ioe )
			{
				System.out.println( "IO error writing user file " + userFileName ) ;
				System.out.println( "Error was: " + ioe ) ;
			}
		}
		return userFileName ;
	}
}
