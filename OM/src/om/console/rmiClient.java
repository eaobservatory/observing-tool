package om.console;

import om.frameList.*;
import java.rmi.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.io.*;

/** 
    rmiClient gets a sequece console from a RMI server
    and displays on the local computer screen for TOs.
    This is a RMI client

    @version 1.0 1st Sept 1999
    @author M.Tan@roe.ac.uk
*/
final public class rmiClient
{

  /** 
      public static void main(String[] args) is
      the point a new process starts.

      @param String []
      @return  none
      @throws RemoteException
  */
  public static void main(String[] args)
    {
      String inst = null;
      if (args.length >= 2) {
	inst = args[0];
	loadConfig (args[1]);
      }else if (args.length == 1) {
	inst = args[0];
	loadConfig(new String("../conf/om.cfg"));
      }else{
	System.out.println ("ERROR: Must specify instrument");
      }
      
      //    System.setSecurityManager(new RMISecurityManager());

      try {

	System.out.println ("Looking up "+System.getProperty(inst+"_OBJE"));

	rmiInterface h = (rmiInterface) 
	  Naming.lookup(System.getProperty(inst+"_OBJE"));
	commandSentInterface com = (commandSentInterface) 
	  Naming.lookup(System.getProperty(inst+"_OBJE")+"-COMM");

	System.out.println ("Inst is "+inst);

	String s=h.getString().toString();
	System.out.println ("s = "+s);

	messageServer messager=new messageServer(inst, new FrameList());

	if(s.substring(0,2).equals("No")) {
	  System.out.println("WARNING: This screen has already been echoed.");
	  //System.exit(0);
	} else {
	  System.out.println("RMI Client says: "+s);
	}

	// Get the model and create a sequence Frame from the model
	myFrameModel model=(myFrameModel) h.getFrameModel();
	sequenceFrame temp=new sequenceFrame(model,com);
	
	// link the new messagerServer to the local panels in the Frame
	temp.setLinks(messager);
	
	// Enable the TO only option
	temp.getMyMenuBar().getTOonly().setEnabled(true);
	
	// identify itself as a TO screen
	temp.getSendCmds().setTag(1);
	
	// link the messagerServer to the remote RMI server
	h.setMessager((messageServer)messager);
	messager.stateMessage(temp.getUpperPanel().getStatus().getText());
	temp.show();

      } catch (RemoteException re) {
	System.out.println ("Remote Exception in client main:"+re);
	
      } catch (Exception e) {
	System.out.println("Exception in client main here:" + e);
	
      }
      
      rmiThread m=new rmiThread ();
      m.run(System.getProperty(inst+"_OBJE"));
    }


  /** 
      private static void loadConfig(String filename) is
      to load configs from a config file.
      
      @param String []
      @return  none
      @throws RemoteException
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
