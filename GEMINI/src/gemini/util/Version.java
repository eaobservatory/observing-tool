
package gemini.util ;

import java.io.File ;
import java.io.BufferedReader ;
import java.io.FileReader ;

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
		try
		{
			File versionFile = new File( System.getProperty( "ot.cfgdir" , "ot/cfg/" ) + "versionFile" );
			BufferedReader br = new BufferedReader( new FileReader( versionFile ) );
			String fullVersion = br.readLine().trim() ;
			String version = "unknown" ;
			if( fullVersion.matches( "\\d{8} \\[\\w*:?\\w*\\]" ) )
				version = fullVersion.substring( 0 , 8 ) ;			
			System.setProperty( "ot.version" , version );
			System.setProperty( "ot.fullversion" , fullVersion );
			br.close() ;
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
		}
	}
}
