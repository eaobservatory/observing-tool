package omp;

import java.lang.reflect.*;
import java.net.URL;

/**
 * MsbClient.java
 *
 *
 * Created: Mon Aug 27 18:30:31 2001
 *
 * @author Mathew Rippa, modified by Martin Folger
 * @version
 */
public class MsbClient extends SoapClient {

   public MsbClient () {
      // default url
      endpoint = "http://www.jach.hawaii.edu/JAClocal/cgi-bin/msbsrv.pl";
      soapAction = "urn:OMP::MSBServer";
      //object_uri = "http://www.jach.hawaii.edu/OMP::MSBServer";

      // reset to specified url if there is one
      if(System.getProperty("MSB_URL") != null) {
         endpoint = System.getProperty("MSB_URL");
      }

      try {
	 url = new URL(endpoint);
      }catch(Exception e) {
	 e.printStackTrace();
      }
   }

   public static void main(String[] args) {
      MsbClient msbc = new MsbClient();
      msbc.queryMSB("<Query><Moon>Dark</Moon></Query>");
      msbc.fetchMSB(new Integer(96));
      SpClient spc = new SpClient();
   }

   public void queryMSB(String xmlQueryString) {
      addParameter("xmlquery", String.class, xmlQueryString);
      addParameter("maxreturn", Integer.class, new Integer(2));
      try {
        System.out.println(doCall(url, "queryMSB"));
      }
      catch(Exception e) {
        e.printStackTrace();
      }
   }

   public void fetchMSB(Integer msbid) {
      addParameter("key", Integer.class, msbid);
      try {
        System.out.println(doCall(url, "fetchMSB"));
      }
      catch(Exception e) {
        e.printStackTrace();
      }	
   }
}
