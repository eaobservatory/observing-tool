package gemini.util ;

import java.io.BufferedReader ;

import java.net.URL ;
import java.io.InputStream ;
import java.io.InputStreamReader ;

public class Version
{

	private static Version versionObject = new Version() ;

	private Version()
	{
		setVersion() ;
	}

	public static Version getInstance()
	{
		return versionObject ;
	}

	public String getVersion()
	{
		return System.getProperty( "ot.version" ) ;
	}

	public String getFullVersion()
	{
		return System.getProperty( "ot.fullversion" ) ;
	}

	private void setVersion()
	{
		URL url = ObservingToolUtilities.resourceURL( "versionFile" , "ot.cfgdir" ) ;
		String version = "unknown" ;
		String fullVersion = version ;
		try
		{
			InputStream is = url.openStream() ;
			BufferedReader br = new BufferedReader( new InputStreamReader( is ) ) ;
			fullVersion = br.readLine().trim() ;
			br.close() ;
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
		}
		if( fullVersion.matches( "\\d{8} \\[\\w*:?\\w*\\]" ) )
			version = fullVersion.substring( 0 , 8 ) ;
		System.setProperty( "ot.version" , version ) ;
		System.setProperty( "ot.fullversion" , fullVersion ) ;
	}
}
