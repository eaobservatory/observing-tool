// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.util ;

import java.io.IOException ;
import java.io.File ;
import java.io.FileInputStream ;
import java.io.FileOutputStream ;
import java.io.FileNotFoundException ;
import java.util.Properties ;

/**
 * A class for manipulating application property files.  These are
 * ordinary java.util.Properties files, but they are assumed to be
 * read and written from the user's home directory.  Unless otherwise
 * specified, the property files are stored/read from the ".gemini"
 * directory in the user's home directory.
 */
public class AppProperties
{
	/**
	 * This is the name of the directory that will be used by default
	 * to store the application property files in.  This directory, and
	 * any directory explicitly specified in one of these method calls
	 * is relative to the user's home directory.
	 */
	public static final String DEFAULT_DIRECTORY = ".gemini" ;

	/**
	 * Create a java.io.File object pointing to the given directory (relative
	 * to the user's home directory) and file.  This method is used
	 * internally in this class.
	 */
	public static File getAppPropertyFile( String dir , String file )
	{
		File returnable = null ;
		String homedir = System.getProperty( "user.home" ) ;
		if( homedir != null )
		{
			String absdir = homedir + File.separatorChar + dir ;
			returnable = new File( absdir , file ) ;
		}

		return returnable ;
	}

	/**
	 * Create a Properties object and load it from the given file in the
	 * user's home directory under .gemini.  If the file doesn't exist
	 * or cannot be loaded for some reason, null is returned.
	 */
	public static Properties load( String file )
	{
		return load( DEFAULT_DIRECTORY , file ) ;
	}

	/**
	 * Create a Properties object and load it from the given file and 
	 * directory relative to user's home directory.  If the file doesn't exist
	 * or cannot be loaded for some reason, null is returned.
	 */
	public static Properties load( String dir , String file )
	{
		Properties p = null ;
		File f = getAppPropertyFile( dir , file ) ;
		if( f != null )
		{
			FileInputStream fis = null ;
			try
			{
				fis = new FileInputStream( f ) ;
			}
			catch( FileNotFoundException ex ){}

			if( fis != null )
			{
				try
				{
					p = new Properties() ;
					p.load( fis ) ;
				}
				catch( IOException ex )
				{
					System.out.println( "Error reading: " + f.getAbsolutePath() ) ;
				}
			}
		}

		return p ;
	}

	/**
	 * Save the given properties file with the given comment in the given file
	 * in the user's home directory under .gemini.  If the file cannot be
	 * written to for some reason, false is returned.
	 */
	public static boolean save( String file , String comment , Properties p )
	{
		return save( DEFAULT_DIRECTORY , file , comment , p ) ;
	}

	/**
	 * Save the given properties file with the given comment in the given file
	 * and directory relative to the user's home directory.  If the file cannot
	 * be written to for some reason, false is returned.
	 */
	public static boolean save( String dir , String file , String comment , Properties p )
	{
		boolean success = false ;
		File f = getAppPropertyFile( dir , file ) ;
		if( f != null )
		{
			try
			{
				FileOutputStream fos = new FileOutputStream( f ) ;
				p.store( fos , comment ) ;
				success = true ;
			}
			catch( IOException ex )
			{
				// See if the .gemini directory exists
				String path = f.getParent() ;
				File fdir = new File( path ) ;
				try
				{
					if( !fdir.exists() )
					{
						// Make the .gemini directory.
						fdir.mkdir() ;
						// Try again now that the directory exists
						return save( dir , file , comment , p ) ;
					}
				}
				catch( Exception ex2 )
				{
					// Trouble checking or creating the directory, give up.
					System.out.println( "Error writing: " + f.getAbsolutePath() + " " + ex2 ) ;
				}
			}
		}
		return success ;
	}
}
