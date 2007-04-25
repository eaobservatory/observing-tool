// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * A root of the program hierarchy.
 *
 * @see SpProg
 * @see SpPlan
 * @see SpLibrary
 */
public class SpRootItem extends SpObsContextItem
{
    /** The version of the OT used to generate this file */
    public static final String ATTR_OT_VERSION = "ot_version";

    /** The telescope name */
    public static final String ATTR_TELESCOPE = "telescope";

/**
 * Default constructor.
 */
protected SpRootItem(SpType spType)
{
   super(spType);
   setTelescope();
   setEditFSM( new SpEditState(this) );
}

/**
 * Override clone to set the SpEditState.
 */
protected Object
clone()
{
   SpItem spClone = (SpItem) super.clone();
   spClone.setEditFSM( new SpEditState(spClone) );
   return spClone;
}

/**
 * Override getTitle to display the program name.
 */
public String
getTitle()
{
   String title = getTitleAttr();
   if (title == null) {
      title = type().getReadable();
   }

   String nameStr = "";
   if (hasBeenNamed()) {
      nameStr = " (" + name() + ")";
   }

   return title + nameStr;
}

/**
 * Print a Science Program Document describing this item to the
 * given output stream.
 */
public void
printDocument(OutputStream os)
{
   SpOutputSGML out = new SpOutputSGML(os);
   out.printDocument(this);
}

/**
  * Set the telescope name - based on input TELESCOPE
  */

	public void setTelescope()
	{
		String name = "";
		name = System.getProperty( "TELESCOPE" );
		if( name == null || name.equals( "" ) )
			name = System.getProperty( "telescope" );
		_avTable.set( ATTR_TELESCOPE , name );
	}

	public void setOTVersion()
	{
		String version = System.getProperty( "ot.version" ) ;
		if( version == null  )
		{
			try
			{
				File versionFile = new File( System.getProperty( "ot.cfgdir" , "ot/cfg/" ) + "versionFile" );
				BufferedReader br = new BufferedReader( new FileReader( versionFile ) );
				version = br.readLine().trim();
				System.setProperty( "ot.version" , version );
			}
			catch( Exception e )
			{
				version = "unknown" ;
			}
		}
		_avTable.set( ATTR_OT_VERSION , version );
	}

public String getOTVersion() {
    String version = "unknown";
    if ( _avTable.get(ATTR_OT_VERSION) != null) {
	version = _avTable.get(ATTR_OT_VERSION);
    }
    return version;
}

}