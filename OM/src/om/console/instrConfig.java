package om.console;
import java.util.*;
import java.io.*;

final public class instrConfig
{  public instrConfig()
   {
       data=new Vector();
       for(int i=0;i<12;i++)
       data.addElement(ct[i]);

       title=System.getProperty("INST_NAME");
   }


  public String getTitle () {return title;}
  public Vector getItems(){return data;}
  private String title=new String();
  private Vector data;
  private String[] ct={
                    "Acquistion Mode",
                    "m read",
                    "Readout area",
                    "1024 X 1024",
                    "Science FOV",
                    "60 X 60",
                    "Exposure Time",
                    "5.0 seconds ",
                    "Coadds",
                    "1 exp. per observe",
                    "Filter",
                    "Jbarr"

      };
}






