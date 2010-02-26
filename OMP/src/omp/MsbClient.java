package omp ;

import java.io.FileWriter ;
import java.io.StringReader ;
import java.net.URL ;
import gemini.sp.SpItem ;

import orac.util.SpInputXML ;

/**
 * MsbClient.java
 * 
 * This is a utility class used to send SOAP messages to the MsbServer.
 * 
 * Created: Mon Aug 27 18:30:31 2001
 * 
 * @author <a href="mailto:mrippa@jach.hawaii.edu">Mathew Rippa</a>, modified
 *         by Martin Folger
 * 
 * $Id$
 */
public class MsbClient extends SoapClient
{
	/**
     * <code>queryMSB</code> Perform a query to the MsbServer with the given
     * query String. Success will return a true value and write the msbSummary
     * xml to file.
     * 
     * @param xmlQueryString
     *            a <code>String</code> value. The xml representing the query.
     * @return a <code>boolean</code> value indicating success.
     */
	public static boolean queryMSB( String xmlQueryString )
	{
		try
		{
			URL url = new URL( System.getProperty( "msbServer" ) ) ;
			flushParameter() ;
			addParameter( "xmlquery" , String.class , xmlQueryString ) ;

			FileWriter fw = new FileWriter( System.getProperty( "msbSummary" ) ) ;
			Object tmp = doCall( url , "urn:OMP::MSBServer" , "queryMSB" ) ;

			if( tmp != null )
			{
				fw.write( ( String )tmp ) ;
				fw.close() ;
			}
			else
			{
				return false ;
			}
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
			return false ;
		}
		return true ;
	}

	/**
     * <code>fetchMSB</code> Fetch the msb indicated by the msbid. The SpItem
     * will return null on failure. In the future this should throw an exception
     * instead.
     * 
     * @param msbid
     *            an <code>Integer</code> value of the MSB
     * @return a <code>SpItem</code> value representing the MSB.
     */
	public static SpItem fetchMSB( Integer msbid )
	{
		SpItem spItem = null ;
		try
		{
			System.out.println( "" + msbid ) ;

			URL url = new URL( System.getProperty( "msbServer" ) ) ;
			flushParameter() ;
			addParameter( "key" , Integer.class , msbid ) ;

			FileWriter fw = new FileWriter( System.getProperty( "msbFile" ) ) ;
			String spXML = ( String )doCall( url , "urn:OMP::MSBServer" , "fetchMSB" ) ;

			if( spXML != null )
			{
				StringReader r = new StringReader( spXML ) ;
				spItem = new SpInputXML().xmlToSpItem( r ) ;

				fw.write( spXML ) ;
				fw.close() ;
			}
			else
			{
				return spItem ;
			}

		}
		catch( Exception e )
		{
			e.printStackTrace() ;
			return spItem ;
		}
		return spItem ;
	}

	/**
     * <code>doneMSB</code> Mark the given project ID as done in the database.
     * 
     * @param projID
     *            a <code>String</code> the project ID.
     * @param checksum
     *            a <code>String</code> the checksum for this project.
     */
	public static void doneMSB( String projID , String checksum )
	{
		try
		{
			URL url = new URL( System.getProperty( "msbServer" ) ) ;
			flushParameter() ;
			addParameter( "projID" , String.class , projID ) ;
			addParameter( "checksum" , String.class , checksum ) ;

			Object tmp = doCall( url , "urn:OMP::MSBServer" , "doneMSB" ) ;
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
		}
	}

	/**
     * Describe <code>main</code> method here.
     * 
     * @param args
     *            a <code>String[]</code> value
     */
	public static void main( String[] args )
	{
		MsbClient.queryMSB( "<Query><Moon>Dark</Moon></Query>" ) ;
		MsbClient.fetchMSB( new Integer( 96 ) ) ;
	}
}
