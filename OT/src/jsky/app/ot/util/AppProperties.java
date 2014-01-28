// Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
