// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An application context (supporting a subset of the methods in
 * marimba.channel.ApplicationContext) that can be used whether or not running
 * as a channel. When running as a channel, it just delegates the requests to
 * the channel ApplicationContext. When running as a normal application, it trys
 * to do something reasonable with the request.
 * 
 * <p>
 * This is a feeble start, other methods from ApplicationContext will no doubt
 * be required.
 * 
 * <p>
 * A better way to do this is to make GeminiAppContext an interface with the
 * subset of ApplicationContext that is supported here. Then make two subclasses
 * of GeminiAppContext, one for channel apps and one for normal apps. Then once
 * the program knows which type of app it is, it would create the appropriate
 * GeminiAppContext subclass. That would eliminate the "if (_appCtx != null)"
 * statements in every method below.
 */
public class GeminiAppContext
{

	// The singleton
	private static GeminiAppContext _instance;

	// These two are used by normal applications
	private String _baseDir;

	private URL _baseURL;

	/**
     * Get the instance.
     */
	public static synchronized GeminiAppContext instance()
	{
		return _instance;
	}

	/**
	 */
	private static synchronized void setInstance( GeminiAppContext gac )
	{
		if( _instance != null )
			throw new RuntimeException( "Tried to create a second app context." );

		_instance = gac;
	}

	/**
     * Construct from a base directory.
     */
	public GeminiAppContext( String baseDir )
	{
		try
		{
			File f = new File( baseDir );
			_baseDir = f.getCanonicalPath();
		}
		catch( IOException ex )
		{
			System.out.println( "Couldn't create the canonical path from: " + baseDir );
			return;
		}

		try
		{
			_baseURL = new URL( "file" , "localhost" , _baseDir + "/." );
		}
		catch( MalformedURLException ex )
		{
			System.out.println( ex );
			return;
		}
		setInstance( this );
	}

	/**
     * Get the base URL for the application.
     */
	public URL getBase()
	{
		return _baseURL;
	}

	/**
     * Obtain a listing of the given directory.
     */
	public String[] listChannelDirectory( String directory )
	{
			char c = File.separatorChar;
			directory = directory.replace( '/' , c );
			File f = new File( _baseDir + c + directory );
			return f.list();
	}

	/**
     * Stop the application and exit.
     */
	public void stop()
	{
		System.exit( 0 );
	}
}
