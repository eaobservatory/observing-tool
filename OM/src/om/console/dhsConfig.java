//this is dummy class to simulate an interface from Science program
//the final formatt of this should be provided by mjc of jac

package om.console;
import java.util.*;

final public class dhsConfig
{
  public dhsConfig()
  {
     data=new Vector();

     for(int i=0;i<6;i++)
     data.addElement(ct[i]);

     name=System.getProperty("INST_NAME");
  }

  public Vector getInstrumentConfig() {return data;}
  public String getInstrumentName() {return name;}

private Vector data;
private String name=new String ();
private String[] ct={
                    "Number:",
                    "42",
                    "Label:",
                    "f1998 1214_OOC      ",
                    "Time now:",
                    "time unknown",

      };
}







