package om.sciProgram;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gemini.sp.*;
import gemini.sp.ipc.SpServer;
import orac.ukirt.inst.*;
import orac.ukirt.iter.*;


/** final public class mainTest has a special method main,
   which is the starting ponit of the OM program. The class
   does not need a constructor

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk

*/
final public class oracOM
{

/** public static void main(String[] args) is
    where the java program starts. It takes String args on the command line.

    @param String args
    @return  none
    @throws none
*/
  public static void main(String[] args)
    {
      
      if(args[0].length()>0)
	loadConfig(args[0]);
      else
	loadConfig(new String("../cfg/om.cfg"));
      
      // Need to construct UKIRT-specific items so that their SpTypes are
      // statically initialised.  Otherwise the sp classes won't know about 
      // their types.  AB 19-Apr-2000
      SpItem spItem = new SpInstUFTI();
      spItem = new SpInstCGS4();
      spItem = new SpInstIRCAM3();
      spItem = new SpInstMichelle();
      spItem = new SpDRRecipe();
      spItem = new SpIterBiasObs();
      spItem = new SpIterDarkObs();
      spItem = new SpIterCGS4();
      spItem = new SpIterIRCAM3();
      spItem = new SpIterMichelle();
      spItem = new SpIterUFTI();
      spItem = new SpIterCGS4CalUnit();
      spItem = new SpIterCGS4CalObs();
      spItem = new SpIterMichelleCalObs();
      spItem = new SpIterFP();
      spItem = new SpIterIRPOL();
      
      // Select the SpServer to use (default to DEPLOYED)
      int serverType = SpServer.DEPLOYED;
      String server = System.getProperty ("SERVER", "DEPLOYED");
      if (server.equalsIgnoreCase("ATC")) {
	serverType = SpServer.ATC_DEVELOPMENT;
      } else if (server.equalsIgnoreCase("HILO")) {
	serverType = SpServer.HILO_DEVELOPMENT;
      } else if (server.equalsIgnoreCase("HOME")) {
	serverType = SpServer.HOME_DEVELOPMENT;
      }
      SpServer.selectServer(serverType);
      
      loginFrame frame = new loginFrame();
      
      frame.show();
      frame.getLoginPanel().getUsername().requestFocus();
      frame.setResizable(false);
      
    }
  
  /** private static void loadConfig(String filename) is a private method
    reads a configuration file and set up configuration. It is done by
    putting things into java system properties which is program-system-wide visible.

    @param String args
    @return  none
    @throws none
  */
  private static void loadConfig(String filename)
  {
    try {
      String line_str;
      int line_number;
      FileInputStream is = new FileInputStream(filename);
      DataInputStream ds = new DataInputStream(is);

      Properties temp=System.getProperties();
      int lineno = 0;

      while ((line_str = ds.readLine()) != null) {
	lineno++;
	if(line_str.length()>0) {
	  if(line_str.charAt(0)=='#') continue;

	  try {
	    int colonpos = line_str.indexOf(":");
	    temp.put(line_str.substring(0,colonpos).trim(),
		     line_str.substring(colonpos+1).trim());

	  }catch (IndexOutOfBoundsException e) {
	    System.out.println ("Problem reading line "+lineno+": "+line_str);
	    ds.close();
	    is.close();
	    System.exit(1);
	  }
	}
      }
      ds.close();
      is.close();

    } catch (IOException e) {
      System.out.println("File error: " + e);
    }
  }
}
  






