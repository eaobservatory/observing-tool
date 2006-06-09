package omp;

import java.net.URL;
import java.util.Vector;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.soap.Header;
import org.apache.soap.Constants;
import org.apache.soap.Fault;
import org.apache.soap.rpc.Parameter;
import org.apache.soap.rpc.Call;
import org.apache.soap.rpc.Response;
import org.apache.soap.transport.http.SOAPHTTPConnection;
import org.apache.soap.SOAPException;

import java.util.Date ;

/**
 * SoapClient.java
 *
 *
 * Created: Mon Aug 27 18:30:31 2001
 *
 * @author <a href="mailto:mrippa@jach.hawaii.edu">Mathew Rippa</a>,
 * modified by Martin Folger
 *
 * $Id$ 
 */
public class SoapClient
{

	/** Soap fault caused by attempt to store a Science Program to server whose time stamp has changed. */
	public static final String FAULT_CODE_SP_CHANGED_ON_DISK = "SOAP-ENV:Server.SpChangedOnDisk";

	private static Header header = null;

	private static Vector params = new Vector();

	public static final String FAULT_CODE_INVALID_USER = "SOAP-ENV:Client.InvalidUser";

	/**
	 * <code>addParameter</code>. Add a Parameter to the next Call that
	 * is to take place.
	 *
	 * @param name a <code>String</code> value. The name of the
	 * parameter to be added to next soap Call.

	 * @param type a <code>Class</code> value. The explicit Class of
	 * this parameter.

	 * @param val an <code>Object</code> value. The object to register this Parameter with.
	 */
	protected static void addParameter( String name , Class type , Object val )
	{
		params.add( new Parameter( name , type , val , null ) );
	}

	/**
	 * <code>flushParameter</code> Clear the Vector of
	 * <code>Parameters</code> Objects to be sent in the next Call.
	 * 
	 */
	protected static void flushParameter()
	{
		params.clear();
	}

	/**
	 * <code>doCall</code> Send the Call with various configurations.
	 *
	 * @param url an <code>URL</code> val indicating the soap server to
	 * connect to.
	 * @param soapAction a <code>String</code>. The URN like
	 * "urn:OMP::MSBServer"
	 * @param methodName a <code>String</code> The name of the method to
	 * call in the server.
	 * @return an <code>Object</code> returned by the method called in
	 * the server .
	 */
	protected static Object doCall( URL url , String soapAction , String methodName ) throws Exception
	{
		Object obj = new Object();

		try
		{
			Call call = new Call();

			call.setTargetObjectURI( soapAction );
			call.setEncodingStyleURI( Constants.NS_URI_SOAP_ENC );
			call.setMethodName( methodName );
			call.setParams( params );
			if( header != null )
				call.setHeader( header );
			soapAction += "#" + methodName;
			if( System.getProperty( "http.proxyHost" ) != null && System.getProperty( "http.proxyHost" ).length() > 0 )
			{
				System.out.println( "Using proxy server" );
				SOAPHTTPConnection st = new SOAPHTTPConnection();
				st.setProxyHost( System.getProperty( "http.proxyHost" ) );
				st.setProxyPort( Integer.parseInt( System.getProperty( "http.proxyPort" ) ) );
				call.setSOAPTransport( st );
			}

System.out.println( methodName + " started -> " + new Date( System.currentTimeMillis() ).toString() ) ;

			Response resp = call.invoke( url , soapAction );

System.out.println( methodName + " ended -> "+ new Date( System.currentTimeMillis() ).toString() ) ;

			// check response 
			if( !resp.generatedFault() )
			{
				Parameter ret = resp.getReturnValue();
				if( ret == null )
					obj = null;
				else
					obj = ret.getValue();

				//Reset the params vector.
				params.clear();

				//return result;
				return obj;

			}
			else
			{
				Fault fault = resp.getFault();

				// Handle special fault codes here.
				if( fault.getFaultCode().equals( FAULT_CODE_SP_CHANGED_ON_DISK ) )
				{
					throw new SpChangedOnDiskException( fault.getFaultString() );
				}
				else if( fault.getFaultCode().equals( FAULT_CODE_INVALID_USER ) )
				{
					throw new InvalidUserException( fault.getFaultString() );
				}

				JOptionPane.showMessageDialog( null , "Code:    " + fault.getFaultCode() + "\n" + "Problem: " + fault.getFaultString() , "Error Message" , JOptionPane.ERROR_MESSAGE );
			}

		}
		catch( SpChangedOnDiskException e )
		{
			// Re-throw SpChangedOnDiskException so it can be handled somewhere else.
			throw e;
		}
		catch( SOAPException se )
		{
			Logger.getLogger( SoapClient.class ).error( se.getMessage() );
			JOptionPane.showMessageDialog( null , se.getMessage() , "SOAP Exception" , JOptionPane.ERROR_MESSAGE );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return null;
	}
}
