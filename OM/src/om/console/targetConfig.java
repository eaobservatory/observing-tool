//this is dummy class to simulate an interface from Science program
//the final formatt of this should be provided by mjc of jac

package om.console;
import java.util.*;

public class targetConfig
{
  public targetConfig()
  {
     data=new Vector();

     for(int i=0;i<6;i++)
     data.addElement(ct[i]);
  }

  public Vector getTargetConfig() {return data;}

private Vector data;
private String[] ct={
                    "Target:",
                    "World Beating Asteriod",
                    "RA:",
                    "25:00:00",
                    "Dec",
                    "100:00:00",

      };
}







