package om.console;

import java.util.Properties;
import java.util.Enumeration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Only for testing console.MichelleStatus.
 *
 * @author Martin Folger
 */
public class MichelleStatusTest {
  public static void setParameters(MichelleStatus michelleStatus) {
    String parameterFile = "/net/alba/sw4/mfo/Michelle/michelle.parameters";
    try {
      Properties parameters = new Properties();
      parameters.load(new FileInputStream(parameterFile));
      
      for(Enumeration e = parameters.propertyNames(); e.hasMoreElements(); ) {
        String parameter = (String)e.nextElement();
        michelleStatus.messageString(parameter, parameters.getProperty(parameter));
      }
    }
    catch(FileNotFoundException e) {
      System.out.println("Could not find parameter /propoerty file: \"" + parameterFile + "\"");
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }
}
