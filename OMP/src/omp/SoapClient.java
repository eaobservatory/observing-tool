package omp;


import java.io.*;
import java.lang.reflect.*;
import java.net.URL;
import java.util.Vector;
import org.apache.soap.*;
import org.apache.soap.rpc.*;


/**
 * SoapClient.java
 *
 *
 * Created: Mon Aug 27 18:30:31 2001
 *
 * @author Mathew Rippa, modified by Martin Folger
 * @version
 */
public class SoapClient {

   URL url = null;
   
   // set in subclasses
   protected String endpoint;
   protected String soapAction;
   //protected String object_uri;

   private Header header = null;
   private Vector params = new Vector();

   protected void addParameter(String name, Class type, Object val) {
      params.add(new Parameter(name, type, val, null));
   }

   protected String doCall(URL url, String methodName) throws Exception {
      String result = "";
      //try {
	 Call call = new Call();
	 call.setTargetObjectURI(soapAction);
	 call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);
	 call.setMethodName(methodName);
	 call.setParams(params);
	 if (header != null)
	    call.setHeader(header);

	 soapAction +="#" + methodName;
	 
	 Response resp = call.invoke(url, soapAction);
      
	 // check response 
	 if (!resp.generatedFault()) {
	    Parameter ret = resp.getReturnValue();
	    result = (String) ret.getValue();
	 }
	 else {
	    Fault fault = resp.getFault ();
	    
	    // proper Exceptions will be implemented later.
	    throw new Exception(fault.getFaultString());
	    //System.err.println (methodName + " generated fault: ");
	    //System.out.println ("  Fault Code   = " + fault.getFaultCode());  
	    //System.out.println ("  Fault String = " + fault.getFaultString());
	 }
      
      //} catch (Exception e) {
      //  e.printStackTrace();
      //}

      //Reset the params vector.
      params.clear();

      return result;
   }
} // SoapClient

