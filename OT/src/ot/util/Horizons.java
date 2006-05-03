/*==============================================================*/
/*                                                              */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2006                   */
/*                                                              */
/*==============================================================*/

package ot.util ;
 
import java.net.URL ;
import java.io.File ;
import java.io.BufferedReader ;
import java.util.TreeMap ;
import java.net.URLEncoder ;
import java.io.FileReader ;
import java.net.MalformedURLException ;
import java.io.IOException ;
import java.io.FileNotFoundException ;
import java.io.InputStreamReader ;
import java.io.InputStream ;
import java.util.Vector ;
import java.io.UnsupportedEncodingException ;

// serialising
import java.io.FileInputStream ;
import java.io.FileOutputStream ;
import java.io.ObjectInputStream ;
import java.io.ObjectOutputStream ;

public class Horizons
{

	static final String server =  "http://ssd.jpl.nasa.gov/" ;
	static final String script =  "horizons_batch.cgi?batch=1" ;
	
	private static boolean caching = true ;
	private String cacheDirectory = null ;
	
	private static boolean search = false ;
	
	private static Horizons horizons = null ;
	
	private Horizons(){}
	
	public static synchronized Horizons getInstance()
	{
		if( horizons == null )
			horizons = new Horizons() ;
		return horizons ;
	}

	public static void main( String args[] )
	{
		if( args.length == 0 )
			System.exit( -1 ) ;
		String inputFileName = args[ 0 ] ;
		
		if( inputFileName.matches( "~" ) )
			inputFileName.replaceFirst( "~" , System.getProperty( "user.home" ) ) ;

		Horizons horizon = Horizons.getInstance() ;
		TreeMap treeMap = null ;
		treeMap = horizon.resolveFromFile( inputFileName ) ;
		if( treeMap.isEmpty() )
			treeMap = horizon.resolveName( inputFileName ) ;
		if( treeMap != null )
			printMap( treeMap ) ;		
	}

	private String getFileName( String query )
	{
		query = query.trim().toUpperCase().replaceAll( File.separator , "_" ) ;
		String result = getCacheDirectory() + getVersion() + query + ".map" ;
		return result ;		
	}

	private String getVersion()
	{
		String version = System.getProperty( "ot.version" ) ;
		if( version == null )
			version = "" ;
		return version ;
	}
	
	private String getCacheDirectory()
	{
		if( cacheDirectory == null )
		{
			cacheDirectory = 
				System.getProperty( "user.home" ) +
				File.separator + 
				".ot" + 
				File.separator ;
			File directory = new File( cacheDirectory ) ;
			try
			{
				/* 
				 *  The following should not cause problems as 
				 *  users should be able to write into their own home directories
				 */
				if( !directory.exists() )
					caching = directory.mkdirs() ;
			}
			catch( Exception e )
			{
				caching = false ;
				System.out.println( "Caching of orbital elements disabled " + e ) ; 
			}
		}
		return cacheDirectory ;
	}
	
	private TreeMap readCache( String query )
	{
		if( query == null || query.trim().equals( "" ) )
			return null ;
		FileInputStream fileInputStream = null ;
		ObjectInputStream objectInputStream = null ;		
		String fileName = getFileName( query ) ;
		try
		{
			fileInputStream = new FileInputStream( fileName ) ;
			objectInputStream = new ObjectInputStream( fileInputStream ) ;
			Object tmp = objectInputStream.readObject() ;
			if( !( tmp instanceof TreeMap ) )
				return null ;
			TreeMap treeMap = ( TreeMap )tmp ;
			/* we don't care if the map is empty */
			return treeMap ;
		}
		catch( java.io.FileNotFoundException fnfe ){ /* we don't care if it is not in the cache */ }
		catch( IOException ioe )
		{
			System.out.println( ioe + " while reading cache file " + fileName ) ;
			return null ;
		}
		catch( ClassNotFoundException cnfe ){ /* we already have TreeMap */ }
		finally
		{
			try
			{
				if( objectInputStream != null )
					objectInputStream.close() ;
				if( fileInputStream != null )
					fileInputStream.close() ;
			}
			catch( IOException ioe )
			{
				System.out.println( ioe + " while closing cache file " + fileName ) ;
				return null ;
			}
		}
		return null ;
	}
	
	private boolean writeCache( TreeMap result , String query )
	{
		// n.b. we still write even if the map is empty
		if( result == null )
			return false ;
		FileOutputStream fileOutputStream = null ;
		ObjectOutputStream objectOutputStream = null ;
		if( query == null || query.trim().equals( "" ) )
			return false ;
		String fileName = getFileName( query ) ;
		try
		{
			fileOutputStream = new FileOutputStream( fileName ) ;
			objectOutputStream = new ObjectOutputStream( fileOutputStream ) ;
			objectOutputStream.writeObject( result ) ;
			objectOutputStream.flush() ;
			fileOutputStream.flush() ;
		}
		catch( IOException ioe )
		{
			System.out.println( ioe + " while writing cache") ;
			return false ;
		}
		finally
		{
			try
			{
				if( objectOutputStream != null )
					objectOutputStream.close() ;
				if( fileOutputStream != null )
					fileOutputStream.close() ;
			}
			catch( IOException ioe )
			{
				System.out.println( ioe + " while closing" ) ;
				return false ;
			}
		}
		return true ;
	}

	public synchronized Vector searchName( String name )
	{
		if( name == null || name.trim().equals( "" ) )
			return new Vector() ;
		search = true ;
		TreeMap map = doLookup( name ) ;
		URL lut = URLBuilder( map ) ;
		Vector vector = null ;
		if( lut != null )
		{
			vector = connect( lut ) ;
		}
		else
		{
			search = false ;
			return new Vector() ;
		}
		if( vector != null )
		{
			search = false ;
			return vector ;
		}
		return new Vector() ;
	}
	
	public TreeMap resolveFromFile( String name )
	{
		if( name == null || name.trim().equals( "" ) )
			return new TreeMap() ;
		TreeMap map ;
		map = readInputFile( name ) ;
		
		URL lut = URLBuilder( map ) ;
		Vector vector = null ;
		TreeMap treeMap = null ;
		if( lut != null )
			vector = connect( lut ) ;
		else 
			return new TreeMap() ;
		if( vector != null )
			treeMap = parse( vector ) ;
		else
			return new TreeMap() ;
		return treeMap ;
	}
	
	public TreeMap resolveName( String name )
	{
		if( name == null || name.trim().equals( "" ) )
			return new TreeMap() ;
		TreeMap map ;
		if( caching )
		{
			map = readCache( name ) ;
			if( map != null )
				return map ;
		}
		map = doLookup( name ) ;
		
		URL lut = URLBuilder( map ) ;
		Vector vector = null ;
		TreeMap treeMap = null ;
		if( lut != null )
			vector = connect( lut ) ;
		else 
			return new TreeMap() ;
		if( vector != null )
			treeMap = parse( vector ) ;
		else
			return new TreeMap() ;
		if( caching )
			writeCache( treeMap , name ) ;
		return treeMap ;
	}
	
	private TreeMap doLookup( String name )
	{
		TreeMap treeMap = new TreeMap() ;
		if( name == null || name.trim().equals( "" ) )
		{
			System.out.println( "No name given" ) ;
		}
		treeMap.put( "COMMAND" , name.trim() ) ;
		treeMap.put( "MAKE_EPHEM" , "YES" ) ;
		treeMap.put( "TABLE_TYPE" , "OBSERVER" ) ;
		treeMap.put( "R_T_S_ONLY" , "YES" ) ;
		return treeMap ;
	}

	public static void printMap( TreeMap map )
	{
		if( map == null )
			return ;
		String key , value ;
		Object tmp ;
		while( map.size() != 0 )
		{
			tmp = map.lastKey() ;
			if( !( tmp instanceof String ) )
			{
				System.out.println( tmp + " not a String - something *really* wrong" ) ;
				return ;
			}
			key = ( String )tmp ;
			tmp = map.remove( key ) ;
			value = tmp.toString() ;
			System.out.println( key + " == " + value ) ;
		}
		
	}

	private TreeMap parse( Vector vector )
	{
		String line ;
		TreeMap treeMap = new TreeMap() ;
		QuickMatch quickMatch = QuickMatch.getInstance() ;
		TreeMap tmpMap ;
		while( vector.size() != 0 )
		{
			Object tmp = vector.remove( 0 ) ;
			if( !( tmp instanceof String ) )
				continue ;
			line = ( String )tmp ;
			if( line.trim().matches( "^No matches found.$" ) )
				return new TreeMap() ;
			tmpMap = quickMatch.parseLine( line ) ;
			if( tmpMap != null )
				treeMap.putAll( tmpMap ) ;
		}
		return treeMap ;
	}

	private Vector connect( URL url )
	{
		Vector vector = new Vector() ;
		if( url == null )
		{
			System.out.println( "Null URL" ) ;
			return vector ;
		}
		InputStream stream = null ;
		try
		{
			stream = url.openStream() ;
		}
		catch( IOException ioe )
		{
			System.out.println( "Could not open stream" ) ;
			System.out.println( ioe ) ;
			return null ;
		}
		InputStreamReader streamReader = new InputStreamReader( stream ) ;
		BufferedReader buffer = new BufferedReader( streamReader ) ;
		try
		{
			while( !buffer.ready() ){}

			String line ;
			while( ( line = buffer.readLine() ) != null )
			{
				if( line.trim().matches( "\\!\\$\\$SOF" ) )
					break ;
				vector.add( line ) ;
			}
		}
		catch( IOException ioe )
		{
			System.out.println( ioe ) ;
			return vector ;
		}
		return vector ;
	}
	
	private URL URLBuilder( TreeMap treeMap )
	{
		URL finalURL = null ;
		StringBuffer buffer = new StringBuffer() ;
		String key , value ;
		Object tmp ;
		while( treeMap.size() != 0 )
		{
			tmp = treeMap.lastKey() ;
			if( !( tmp instanceof String ) )
				continue ;
			key = ( String )tmp ;
			tmp = treeMap.remove( key ) ;
			if( !( tmp instanceof String ) )
				continue ;
			value = ( String )tmp ;
			try
			{
				if( key.trim().equals( "COMMAND") && search )
					buffer.append( "&" + key.trim() + "=" + "'NAME=" + URLEncoder.encode( value.trim() , "UTF-8" ) + "'" ) ;
				else
					buffer.append( "&" + key.trim() + "=" + URLEncoder.encode( value.trim() , "UTF-8" ) ) ;
			}
			catch( UnsupportedEncodingException uee )
			{  
				System.out.println( "Character encoding not supported" + uee ) ;
				System.exit( -10 ) ;
			}
		}
		String url = buffer.toString() ;
		buffer = null ;
		buffer = new StringBuffer() ;
		buffer.append( server ) ;
		buffer.append( script ) ;
		buffer.append( url ) ;
		try
		{
			finalURL = new URL( buffer.toString() ) ;
		}
		catch( MalformedURLException mue )
		{
			System.out.println( mue ) ;
			return null ;
		}
		return finalURL ;
	}

	private TreeMap readInputFile( String fileName )
	{
		String line ;
		String[] parts ;
		String key , value ;
		TreeMap treeMap = new TreeMap() ;
		File file = new File( fileName ) ;
		if( !( file.exists() && file.canRead() ) )
		{
			System.out.println( fileName + " not available" ) ;
			return treeMap ;
		}
		FileReader reader = null ;
		try
		{
			reader = new FileReader( file )	;
		}
		catch( FileNotFoundException fnfe )
		{
			System.out.println( fnfe ) ;
			return treeMap ;
		}
		BufferedReader buffer = new BufferedReader( reader ) ;
		try
		{
			while( !buffer.ready() ){}

			while( ( line = buffer.readLine() ) != null )
			{
				parts = line.split( "= " ) ;
				if( parts.length < 2 )
					continue ;
				if( parts.length == 2 ) 
				{
					key = parts[ 0 ].trim() ;
					value = parts[ 1 ].trim() ;
					treeMap.put( key , value ) ;
				}
				else
				{
					System.out.println( "Error ! " + line ) ;
					return treeMap ;
				}
			}
		}
		catch( IOException ioe )
		{
			System.out.println( ioe ) ;
			return treeMap ;
		}
		return treeMap ;
	}
}
