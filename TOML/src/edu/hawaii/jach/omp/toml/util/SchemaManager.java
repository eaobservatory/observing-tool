/**
 * Copyright (C) 2009 - 2010 Science and Technology Facilities Council.
 * All Rights Reserved.
 */

package edu.hawaii.jach.omp.toml.util ;

import java.io.IOException ;
import java.io.InputStream ;
import java.lang.reflect.Method ;
import java.net.MalformedURLException ;
import java.net.URL ;
import java.util.TreeMap ;

import javax.xml.namespace.NamespaceContext ;

/**
 * Separates the available schemas from their use.
 *
 */
public class SchemaManager
{
	public static final String CFG_KEY = "schemas.cfg" ;
	private static String SCHEMA_PATH ;
	private static TreeMap<String,TreeMap<String,String>> dictionary = new TreeMap<String,TreeMap<String,String>>() ;
	private static TreeMap<String,SchemaDetails> cache = new TreeMap<String,SchemaDetails>() ;

	private static final String SCHEMA_KEY = "schema" ;
	private static final String CLASSPATH_KEY = "classpath" ;
	private static final String URL_KEY = "namespace_url" ;
	private static final String CONTEXT_KEY = "namespace_context" ;

	/**
	 * Set the schema.
	 * @param schemaName - name of the schema required.
	 * @param SCHEMA - string for the schema path to be written to.
	 * @param CLASSPATH - string for the schema class path to be written to. 
	 * @throws Exception - name of schema unknown.
	 */
	public static SchemaDetails setSchema( String schemaName ) throws Exception
	{
		if( schemaName != null && schemaName.trim().length() > 0 )
		{
			if( dictionary.size() == 0 )
				init() ;

			SchemaDetails details = null ;
			if( cache.containsKey( schemaName ) )
			{
				details = cache.get( schemaName ) ;
			}
			else if( dictionary.containsKey( schemaName ) )
			{
				TreeMap<String,String> subMap = dictionary.get( schemaName ) ;
				String SCHEMA = subMap.get( SCHEMA_KEY ) ;
				String CLASSPATH = subMap.get( CLASSPATH_KEY ) ;
				String URL = subMap.get( URL_KEY ) ;
				
				String nsContextName = subMap.get( CONTEXT_KEY ) ;
				NamespaceContext nsContext = getNamespaceContext( nsContextName ) ;
				details = new SchemaDetails( schemaName , SCHEMA , CLASSPATH , URL , nsContext ) ;
				SchemaDetails tmp = cache.put( schemaName , details ) ;
				if( tmp != null )
					throw new Exception( "Schema cache broken \"" + schemaName + "\" replaced." ) ; 
			}
			else
			{
				throw new Exception( "Invalid schema name \"" + schemaName + "\" options are " + dictionary.keySet() ) ;
			}
			return details ;
		}
		else
		{
			throw new Exception( "Invalid schema name" ) ;
		}
	}
	
	/**
	 * Returns the NamespaceContext for a given context name, or throws an exception.
	 * Convenience method to clear up the code, easily separated, especially as it's quite fiddly.
	 * @param nsContextName Name of the NamespaceContext to find
	 * @return A NamespaceContext
	 * @throws Exception If the NamespaceContext cannot be found or instanciated.
	 */
	private static NamespaceContext getNamespaceContext( String nsContextName ) throws Exception
	{
		NamespaceContext nsContext = null ;

		Class<?> nsContextClass = null ;
		Object context = null ;

		try
		{
			nsContextClass = Class.forName( nsContextName ) ;
		}
		catch( ClassNotFoundException cnfe )
		{
			throw new Exception( "Namespace context \"" + nsContextName + "\" not found." ) ;
		}

		/*
		 * NamespaceContexts at the JAC are singletons
		 * If the NamespaceContext does not define a "getInstance" method it will throw an exception.
		 */
		try
		{
			Method getInstance = nsContextClass.getMethod( "getInstance" , new Class<?>[ 0 ] ) ;
			context = getInstance.invoke( nsContextClass , new Object[ 0 ] ) ;
		}
		catch( NoSuchMethodException nsme )
		{
			nsme.printStackTrace() ;
		}

		// if context is still null, try a regular instanciation.
		if( context == null )
		{
			context = nsContextClass.newInstance() ;
		}

		// if we have a context check it is a NamespaceContext instance.
		if( context != null && context instanceof NamespaceContext )
			nsContext = ( NamespaceContext )context ;
		else
			throw new Exception( "Couldn't instanciate namespace context for \"" + nsContextName + "\"" ) ;

		return nsContext ;
	}

	/**
	 * Test it works
	 * @param args none required at the moment
	 */
	public static void main( String[] args )
	{
		System.setProperty( CFG_KEY , "file:///export/data/shart/current/ingot-git/cfg/schemas.cfg" ) ;
		init() ;
		try
		{
			SchemaManager.setSchema( "ukirt" ) ;
		}
		catch( Exception e ){ e.printStackTrace() ; }
	}
	
	/**
	 * Initialise ... duh !
	 * 
	 * Reads then parses it the config file.
	 */
	private static void init()
	{
		SCHEMA_PATH = System.getProperty( CFG_KEY ) ;
		if( SCHEMA_PATH == null )
		{
			System.out.println( "Property \"" + CFG_KEY + "\" not set." ) ;
		}
		else
		{
			try
			{
				String[] lines = read( new URL( SCHEMA_PATH ) ) ;
				parse( lines ) ;
			}
			catch( MalformedURLException e )
			{
				e.printStackTrace() ;
			}
		}
	}

	/**
	 * Read a configuration file, and return it's contents split on line endings.
	 * Should this technique be used again, refactor to another class.
	 * @param config URL of the configuration file.
	 * @return String[] containing the contents of the file split into lines.
	 */
	private static String[] read( URL config )
	{
		String[] split = null ;
		try
		{
			InputStream is = config.openStream() ;
			byte[] byteBuffer = new byte[ 1024 ] ;
			StringBuffer stringBuffer = new StringBuffer() ;
			int read = 0 ;
			while( true )
			{
				read = is.read( byteBuffer ) ;
				if( read == -1 )
					break ;
				stringBuffer.append( new String( byteBuffer , 0 , read ) ) ;
			}
			if( stringBuffer.length() > 0 )
			{
				String input = stringBuffer.toString() ;
				split = input.split( "\n" ) ;
			}
		}
		catch( IOException e )
		{
			e.printStackTrace() ;
		}
		return split ;
	}

	/**
	 * Parse a configuration file for defining schemas.
	 * Currently the format is of the form "key = value".
	 * A key of "name" defines the individual schema config.
	 *
	 * Format should really be replaced with something more robust.
	 *
	 * @param lines
	 */
	private static void parse( String[] lines )
	{
		if( lines != null )
		{
			TreeMap<String,String> subTree = null ;
			for( String entry : lines )
			{
				String[] line = entry.split( "=" ) ;
				if( line.length == 2 )
				{
					String key = line[ 0 ].trim().toLowerCase() ;
					String value = line[ 1 ].trim() ;
					if( value.startsWith( "\"" ) )
					{
						int lastIndex = value.lastIndexOf( "\"" ) ;
						value = value.substring( 1 , lastIndex ) ;
					}
					if( "name".equals( key ) )
					{
						subTree = new TreeMap<String,String>() ;
						dictionary.put( value , subTree ) ;
					}
					if( subTree != null )
					{
						subTree.put( key , value ) ;
					}
				}
			}
		}
	}
}
