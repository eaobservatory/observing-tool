package om.console;
import om.dramaSocket.*;
import om.frameList.*;
import java.util.*;
import java.io.*;
import java.rmi.*;
import java.net.*;
import java.rmi.registry.*;

/** final public class sequenceConsole starts/tests the package om.console
    for an instrument - it can be used to run the sequencer "stand-alone".
    It also runs as a RMI server.

    @version 1.0 29 June 2000
    @author Alan Bridger, ab@roe.ac.uk
*/
final public class sequenceConsole
{

  /** public static void main(String[] args) is
      the point a new process starts.
    
    @param String []
    @return  none
    @throws RemoteException and MalformedURLException
  */
  public static void main(String[] args) {

    String inst = "UFTI";   // default instrument

    /* Get arugments: config file name and instrument name */
    if(args.length==2) {
      loadConfig(args[0]);
      inst = args[1];
    } else if (args.length==1) {
      loadConfig(args[0]);
    } else {
      loadConfig(new String("../cfg/om.cfg"));
    }
    
    System.out.println ("Starting up console for "+inst);
    try {
      
      int rmiPort=Integer.parseInt(System.getProperty("RMIS_PORT"));
      LocateRegistry.createRegistry(rmiPort); //lanuch its own rmi registry
      
      console =new remoteFrame(inst," ",new FrameList());
      
      Naming.rebind(System.getProperty(inst+"_OBJE"),console);
      Naming.rebind(System.getProperty(inst+"_OBJE")+"-COMM",console.getCommandSent());
      
      if(System.getProperty("RMIS_MESS").equals("ON"))
	console.setLog(System.err);
    }
    catch (RemoteException re) {
      System.out.println ("Exception in main:"+re);
    }
    
    catch (MalformedURLException e) {
      System.out.println("MalformedURLException in main:" + e);
    }
    
    loadDramaTasks(inst);
    
  }
  
  /** private static void loadConfig(String filename) is a private method
      reads a configuration file and set up configuration. It is done by
      putting things into java system properties.
      
      @param String args
      @return  none
      @throws none
  */
  private static void loadConfig(String filename) {
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

  /** private static void loadDramaTasks loads the drama tasks. It is 
      platform dependent.
    
      @param String
      @return  none
      @throws InterruptedException
  */
  private static void loadDramaTasks(String name) {
    //starting the drama tasks

    String[] proc=new String[4];
    String[] script=new String[5];
    {      
      script[0]=System.getProperty("LOAD_DHSC");
      script[1]=new String(name);
      script[2]="-"+System.getProperty("QUICKLOOK", "noql");
      script[3]="-"+System.getProperty("SIMULATE","sim");
      script[4]="-"+System.getProperty("ENGINEERING","eng");

      System.out.println ("About to start script"+script[0]+" "+script[1]+" "+script[2]+" "+script[3]+" "+script[4]);
      ExecDtask task1=new ExecDtask(script);
      task1.setWaitFor(true);
      // Set the output depending on whether it is requested in the cfg file.
      boolean debug = System.getProperty("SCR_MESS").equalsIgnoreCase("ON");
      task1.setOutput(debug);
      task1.run();
      // Check for errors from the script
      int err = task1.getExitStatus();
      if (err != 0) {
	System.out.println ("Error reported by instrument startup script, code was: "+err);
      }
      
    }
    
    {
      proc[0]=System.getProperty("MONI_CODE");
      proc[1]=new String("MONITOR_"+name);
      proc[2]=new String();
      proc[3]=new String();

      ExecDtask task=new ExecDtask(proc);
      task.setWaitFor(false);
      task.setOutput(false);
      task.run();
      
      console.getDramaTasks().addElement(task.getProcess());
    }
    
    {
      proc[0]=System.getProperty("SOCK_CODE");
      proc[1]=System.getProperty("SOCK_HOST");
      proc[2]=System.getProperty("SOCK_PORT");
      proc[3]=new String("SOCK_"+name);
      ExecDtask task=new ExecDtask(proc);
      task.setWaitFor(false);
      task.setOutput(false);
      
      try {
        task.sleep(6000);
      }catch (InterruptedException e)
	{
	  System.out.println(e);
	}
      task.run();
      console.getDramaTasks().addElement(task.getProcess());
    }
  }
  
  static private remoteFrame console;
}
