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
  protected static Object doCall(URL url, String soapAction, String methodName)  {
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
	JOptionPane.showMessageDialog(null,
				      "Code:    "+fault.getFaultCode()+"\n" + 
				      "Problem: "+fault.getFaultString(), 
				      "Error Message",
				      JOptionPane.ERROR_MESSAGE);

	//System.out.println ("  Fault Code   = " + fault.getFaultCode());  
	//System.out.println ("  Fault String = " + fault.getFaultString());
      }
	    
    } catch (Exception e) {
      e.printStackTrace();
    }
	
    return null;
  }
}

