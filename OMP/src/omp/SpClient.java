package omp;

import java.io.*;
import java.net.*;
import gemini.sp.SpItem;
//import gemini.sp.SpRootItem;
import orac.util.SpItemDOM;


/**
 * SpClient.java
 *
 *
 * Created: Mon Aug 27 18:30:31 2001
 *
 * @author Mathew Rippa, modified by Martin Folger
 * @version
 */
public class SpClient extends SoapClient {

   public SpClient () {

      // default url
      endpoint = "http://www.jach.hawaii.edu/JAClocal/cgi-bin/spsrv.pl";
      soapAction = "urn:OMP::SpServer";
      
      // reset to specified url if there is one
      if(System.getProperty("SP_URL") != null) {
         endpoint = System.getProperty("SP_URL");
      }

      try {
	 url = new URL(endpoint);
      }catch(Exception e) {
	 e.printStackTrace();
      }
   }


   public static void main(String[] args) {
      SpClient spClient = new SpClient();
      if(args.length == 0) {
	 try {
	    
	    LineNumberReader reader = new LineNumberReader(new
	       InputStreamReader(System.in));
	    String line = reader.readLine();

	    String spXml = line;

	    while(line != null) {
	       line = reader.readLine();
	       
	       if(line != null) {
		  spXml += line + "\n";
	       }
	    }
	    
	    System.out.println("Storing " + spXml + "\n...");
	    spClient.storeProgram(spXml, "abc");
	 }
	 catch(IOException e) {
	    System.err.println("Problem reading from stdin: " + e);
	 }
	 catch(Exception e) {
	    System.err.println(e);
	 }

      }
      else {
         if(args[0].equals("-h")) {
            System.out.println("Usage: SpClient \"<SpProg> ...\"");
            System.out.println("   or: SpClient \"Science Program Title\"");
	 }
	 else {
	    // "abc" used for as arbitrary password for testing.
	    try {
	       System.out.println(spClient.fetchProgram(args[0], "abc").toString());
	    }
	    catch(Exception e) {
               e.printStackTrace();
	    }
	 }   
      }
   }


   public SpItem fetchProgram(String id, String pass) throws Exception {
      addParameter("projectid", String.class, id);
      addParameter("password", String.class, pass);
      
      String spXML = doCall(url, "fetchProgram");
      
      return (new SpItemDOM(new StringReader(spXML))).getSpItem();
   }

   public String storeProgram(SpItem spItem, String pass) throws Exception {
      String sp = (new SpItemDOM(spItem)).toString();
   
      addParameter("sp", String.class, sp);
      addParameter("password", String.class, pass);
      return doCall(url, "storeProgram");
   }

   public String storeProgram(String sp, String pass) throws Exception {
      addParameter("sp", String.class, sp);
      addParameter("password", String.class, pass);
      return doCall(url, "storeProgram");
   }
   
}
