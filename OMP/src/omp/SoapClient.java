package omp;

import java.io.*;
import java.lang.reflect.*;
import java.net.URL;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.apache.soap.*;
import org.apache.soap.encoding.SOAPMappingRegistry;
import org.apache.soap.encoding.literalxml.XMLParameterSerializer;
import org.apache.soap.rpc.*;
import org.apache.soap.util.xml.QName;
import org.apache.soap.transport.http.SOAPHTTPConnection;



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
public class SoapClient {

  /** Soap fault caused by attempt to store a Science Program to server whose time stamp has changed. */
  public static final String FAULT_CODE_SP_CHANGED_ON_DISK = "SOAP-ENV:Server.SpChangedOnDisk";

  private static Header header = null;
  private static Vector params = new Vector();
   
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
  protected static void addParameter(String name, Class type, Object val) {
    params.add(new Parameter(name, type, val, null));
  }
    
  /**
   * <code>flushParameter</code> Clear the Vector of
   * <code>Parameters</code> Objects to be sent in the next Call.
   * 
   */
  protected static void flushParameter() {
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
  protected static Object doCall(URL url, String soapAction, String methodName) throws Exception {
    //String result = "";
    Object obj = new Object();

    //Next 3 statements for getting a struct?
    //SOAPMappingRegistry smr = new SOAPMappingRegistry();
    //XMLParameterSerializer xmlSer = new XMLParameterSerializer();
    //smr.mapTypes(Constants.NS_URI_SOAP_ENC,new
    //	  QName("http://www.w3.org/1999/XMLSchema", "Struct"),
    //	    org.w3c.dom.Element.class, xmlSer, xmlSer);
       
    try {
      Call call = new Call();

      //The next line with the above code to get a struct
      //call.setSOAPMappingRegistry(smr);

      call.setTargetObjectURI(soapAction);
      call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);
      call.setMethodName(methodName);
      call.setParams(params);
      if (header != null)
	call.setHeader(header);
      soapAction +="#" + methodName;
      if (System.getProperty("http.proxyHost") != null &&
	  System.getProperty("http.proxyHost").length() > 0) {
	  System.out.println("Using proxy server");
	  SOAPHTTPConnection st = new SOAPHTTPConnection();
	  st.setProxyHost(System.getProperty("http.proxyHost"));
	  st.setProxyPort( Integer.parseInt(System.getProperty("http.proxyPort")));
	  call.setSOAPTransport(st);
      }

      //Let's dump the envelope
      //Envelope env = call.buildEnvelope();
      //System.out.println(env.toString());
	    
      Response resp = call.invoke(url, soapAction);

      // check response 
      if (!resp.generatedFault()) {

	Parameter ret = resp.getReturnValue();
	//result = (String) ret.getValue();
	obj = ret.getValue();

	//Reset the params vector.
	params.clear();
	
	//return result;
	return obj;

      }
      else {
	Fault fault = resp.getFault ();

	// Handle special fault codes here.
	if(fault.getFaultCode().equals(FAULT_CODE_SP_CHANGED_ON_DISK)) {
	  throw new SpChangedOnDiskException(fault.getFaultString());
	}

	JOptionPane.showMessageDialog(null,
				      "Code:    "+fault.getFaultCode()+"\n" + 
				      "Problem: "+fault.getFaultString(), 
				      "Error Message",
				      JOptionPane.ERROR_MESSAGE);

	//System.out.println ("  Fault Code   = " + fault.getFaultCode());  
	//System.out.println ("  Fault String = " + fault.getFaultString());
      }
	    
    } catch (SpChangedOnDiskException e) {
      // Re-throw SpChangedOnDiskException so it can be handled somewhere else.
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}

