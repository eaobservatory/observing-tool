// (c)2008 STFC

package gemini.util ;

import java.io.File;
import java.net.URL ;
import java.net.MalformedURLException ;

public class ObservingToolUtilities
{
	private static ClassLoader classLoader = ObservingToolUtilities.class.getClassLoader() ;
	
	public static URL resourceURL( String path , String property )
	{
		URL url = null ;
		
		if( path != null && property != null )
		{
			path = System.getProperty( property.trim() ) + path ;
			url = resourceURL( path ) ;
		}
		
		return url ;
	}
	
	public static URL resourceURL( String path )
	{
		URL url = null ;
		
		if( path != null )
		{
			url = classLoader.getResource( path ) ;
			if( url == null )
			{
				try
				{
					if( !path.matches( "^\\w+://.*" ) )
						url = new File( path ).toURI().toURL() ;
					else
						url = new URL( path ) ;
				}
				catch( MalformedURLException mue )
				{
					System.out.println( mue + " : " + path ) ;
				}
			}
		}
		
		return url ;
	}
}