package om.console;
            
import om.sciProgram.AlertBox;
import om.dramaSocket.*;
import om.frameList.*;
import java.net.*;
import java.io.*;      
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.rmi.server.*;
import java.rmi.*;         

/** 
    commandSent class is about
    sending commands out to the socket
    Please note this extends UnicastRemoteObject for RMI calls
    REMEMBER:this class has to be compiled by a rmic
    
    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/

final public class commandSent extends UnicastRemoteObject
  implements commandSentInterface
{
/**
   public commandSent(String instrument,DramaSocket ss,FrameList frs) is
   the class constructor. The class has only one constructor so far.
   
   @param String instrument,DramaSocket ss,FrameList frs
   @return  none
   @throws RemoteException
*/
  public commandSent (String instrument,DramaSocket ss,FrameList frs)
  throws RemoteException
    {
      instName=instrument;
      ssocket=ss;
      listFrames=frs;
    }

  /**
     public synchronized void sendCommand ()
     sends a command/string to the socket.
     Please note this is synchronized
     
     @param none
     @return none
     @throws RemoteException
  */
  public synchronized void sendCommand () throws RemoteException
    {
      
      // Check command before sending
      if (command != null) {
	if (ssocket.getSocket()!=null) {
	  try {
	    OutputStream os=ssocket.getSocket().getOutputStream();
	    
	    OutputThread ot =new OutputThread(os,command);
	    ot.start();
	    
	    try {
	      ot.join();
	    } catch (InterruptedException e) {
	      System.out.println ("Failure connecting to socket output thread");
	      System.err.println(e);
	    }
	  } catch (IOException e) {
	    System.out.println ("Failure creating socket output thread");
	    System.err.println(e);
	  }
	}
      }
    }

  /**
     public void setInit (int tag)
     sets up command content and calls sendCommand()
     it will run an action init in a drama task OOS_****
     
     @param int tag
     @return none
     @throws RemoteException
  */
  public void setInit (int tag) throws RemoteException
    {
      if(controlTag>tag) {
	new messageBox("Nothing done since this is disabled, Sorry!");
	return;
      }

      command = new String
	("Dobey OOS_"+instName+" init ");

      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }

  /**
     public void setLink (String task)
     sets up command content and calls sendCommand()
     it will run an action LINK in a drama task MONITOR_****
     
     @param String
     @return none
     @throws RemoteException
  */
  public void setLink (String task) throws RemoteException
    {
      command= new String ("Dobeyw MONITOR_"+instName+" LINK Argument1="+task);

      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }

   /**
      public void setMonitorOn ()

      it will run an action START in a drama task MONITOR_****
      *after the GETP action is done. mt on 5 May 2000

      @param none
      @return none
      @throws RemoteException
   */
  public void setMonitorOn () throws RemoteException
    {
      command = new String("Dobey MONITOR_"+instName+" START ");

      try {
        sendCommand();
      } catch (RemoteException re) {
        System.out.println ("Exception in sendCommand:"+re);
      }
      
   }


  /**
     public void setStart ()
     sets up command content and calls sendCommand()
     it will run an action setHeader in a drama task DHSPOOL three times
     it also will run an action GETP in a drama task MONITOR_****
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setStart () throws RemoteException
    {
      command = new String
	("Dobey DHSPOOL_"+instName+" setHeader Argument1=USERID Argument2=\""+
	 System.getProperty("loginUserId")+"\" ");

      try {
      sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }

      command = new String 
	("Dobey DHSPOOL_"+instName+" setHeader Argument1=OBSREF Argument2=\""+
	 System.getProperty("observingType")+"\" ");

      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }

      //get the default values of parameters from the connected drama tasks
      //new requirement from Alan Bridger on 1st May 2000
      command=new String("Dobey MONITOR_"+instName+" GETP ");
      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }

      command = new String
	("Dobey DHSPOOL_"+instName+" setHeader Argument1=OBSERVER Argument2=\""+
	 System.getProperty("observerNames")+"\" ");
      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }


  /**  
       public void setLoad (String filename,int tag)
       sets up command content and calls sendCommand()
       it will run an action load in a drama task OOS_****
       
       @param String filename,int tag
       @return none
       @throws RemoteException
  */
  public void setLoad (String filename,int tag) throws RemoteException
    {
      if(controlTag>tag) {
	new messageBox("Nothing done since this is disabled, Sorry!");
	return;
      }

      int i=0;
      if(filename.length()>5) {
	for(i=0;i<filename.length()-5;i++) {
	  if(filename.substring(i,i+5).equals(".exec")) {
	    break;
	  }
	}
      }
      
      String temp=filename.substring(0,i);
      command = new String("Dobey OOS_"+instName+" load Argument1="+temp+" ");

      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }

    }

  /**
     public void setExit (int tag)
     sets up command content and calls sendCommand()
     it will close a lot of THINGs and exit the system
     
     @param int tag
     @return none
     @throws RemoteException
  */
  public void setExit (int tag) throws RemoteException
    {
      if (controlTag>tag) {
	new messageBox("Nothing done since this is disabled, Sorry!");
	return;
      }
      
      ssocket.getInputThread().stop();

      if (listFrames.getConnectedInstrument().equals(instName)) {
	setTCSdisconnected();  //disconnect the instrument from the TCS
      }

      for(int i=0; i<frame.getDramaTasks().size();i++)
	{
	  int status;
	  
	  if (i==0) {
	    command=new String("Dobey MONITOR_"+instName+" EXIT ");
	  } else if (i==1) {
	    command=new String("Dobey SOCK_"+instName+" EXIT ");
	  }
	  try {
	    sendCommand();
          } catch (RemoteException re) {
	    System.out.println ("Exception in sendCommand:"+re);
	  }
	  
	  p=(Process)frame.getDramaTasks().elementAt(i);
	  p.destroy();

	  try {
	    status=p.waitFor();
          } catch (InterruptedException e) {
            status=-1;
	    new ErrorBox(p+" is not terminated properly due to: "+e);
	  }

	  System.out.println(p.toString()+" has a status " + status);

	  if(p.exitValue()==0)
            System.out.println(p.toString()+" has been sucessfully completed.");
	}

      if (listFrames.getList().size()>0) {
	for(int i=0;i<listFrames.getList().size();i++) {
	  if (((sequenceFrame)listFrames.getList().elementAt(i)).equals
	      (frame.getFrame())) {
	    System.out.println ("Removing "+frame.getFrame());
	    listFrames.removeFrameList(frame.getFrame());
	    break;
	  }
	}
      } else {
	System.exit(0);
      }
      frame.getDramaTasks().removeAllElements();
      frame.getFrame().dispose();
      
      // Set the tcs connection to NONE. If there are other consoles
      // then the next on the list (may) get connected. (?)
      listFrames.setConnectedInstrument ("NONE");

      // close the server socket otherwise we would have a resource leak
      ssocket.closeServerSocket();
      
      // Unbind from the RMI registry
      try {

        if (frame.getFrame().getInstrument().equals("UFTI")) {
          Naming.unbind(System.getProperty("UFTI_OBJE"));
        } else if (frame.getFrame().getInstrument().equals("Michelle")) {
          Naming.unbind(System.getProperty("MICH_OBJE"));
        } else if (frame.getFrame().getInstrument().equals("CGS4")) {
          Naming.unbind(System.getProperty("CGS4_OBJE"));
        }

        System.out.println("RMI Server for a OM console is unbinded "+"for "+frame.getFrame().getInstrument());
      } catch (RemoteException re) {
	System.out.println ("Exception in commandSent:"+re);
      } catch (Exception e) {
	System.out.println("Exception in commandSent:" + e);
      }
    }


  /**
     public void setClear(int tag)
     sets up command content and calls sendCommand()
     it will run an action clear in a drama task OOS_***
     
     @param int tag
     @return none
     @throws RemoteException
  */
  public void setClear(int tag) throws RemoteException
    {

      if(controlTag>tag) {
	new messageBox("Nothing done since this is disabled, Sorry!");
	return;
      }
      
      command=new String("Dobey OOS_"+instName+" clear ");
      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }
  
  /**
     public void setBreakPoint (int tag)
     sets up command content and calls sendCommand()
     it will run an action breakPoint in a drama task OOS_***
     
     
     @param int tag
     @return none
     @throws RemoteException
  */
  public void setBreakPoint (int tag) throws RemoteException
    {
      if (controlTag>tag) {
	new messageBox("Nothing done since this is disabled, Sorry!");
	return;
      }

      command=new String("Dobey OOS_"+instName+" breakPoint ");
      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }

  /** 
      public void setCancelBreak(int tag)
      sets up command content and calls sendCommand()
      it will run an action cancelStop in a drama task OOS_***
      
      @param int tag
      @return none
      @throws RemoteException
  */
  public void setCancelBreak(int tag) throws RemoteException
    {
      if (controlTag>tag) {
	new messageBox("Nothing done since this is disabled, Sorry!");
	return;
      }
      
      command=new String("Dobey OOS_"+instName+" cancelStop ");
      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }
  
 /**
    public void setRun (int tag)
    sets up command content and calls sendCommand()
    it will run an action run in a drama task OOS_***
    
    @param int tag
    @return none
    @throws RemoteException
 */
  public void setRun (int tag) throws RemoteException
    {
      if (controlTag>tag) {
	new messageBox("Nothing done since this is disabled, Sorry!");
	return;
      }

      command=new String("Dobey OOS_"+instName+" run ");
      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }


/**  
     public void setMovie (int tag)
     sets up command content and calls sendCommand()
     it will show a movie frame
     
     @param int tag
     @return none
     @throws RemoteException
 */
  public void setMovie (int tag) throws RemoteException
    {
      if (!_movie.isVisible()) {
	_movie.show();
      }
    }
  
  /**
     public void setRunSpecial (int temp, int tag)
     sets up command content and calls sendCommand()
     it will run an action run from a point in a drama task OOS_***
     
     @param int temp, int tag
     @return none
     @throws RemoteException
  */
  public void setRunSpecial (int temp, int tag) throws RemoteException
    {
      if (controlTag>tag) {
	new messageBox("Nothing done since this is disabled, Sorry!");
	return;
      }

      command=new String("Dobey OOS_"+instName+" run Argument1="+temp+" ");

      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }
  
  /**public void setPause (int tag)
     sets up command content and calls sendCommand()
     it will run an action pause in a drama task OOS_***
     
     @param int tag
     @return none
     @throws RemoteException
  */
  public void setPause (int tag) throws RemoteException
    {
      if (controlTag>tag) {
	new messageBox("Nothing done since this is disabled, Sorry!");
	return;
      }

      command=new String("Dobey OOS_"+instName+" pause ");
      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }
  
 /** 
     public void  startMovie(int sec)
     starts up a movie and calls sendCommand()
     
     @param int sec
     @return none
     @throws none
 */
  public void startMovie (String sec)
    {
      // Set up command (hate the hardcoding here...)
      String instTask = System.getProperty (instName, instName);
      if (instName.equalsIgnoreCase("michelle")) {
	command=new String("Dobey "+instTask+" startMovie Argument1="+sec+" ");
      } else if (instName.equalsIgnoreCase("ufti")) {
	command=new String("Dobey "+instTask+" START_MOVIE Argument1="+sec+" ");
      } else {
	command = null;
      }
      // And send it
      try {
	sendCommand();
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }

  public void call_Back(String str) {
      command=new String("Dobey "+"QL_"+instName+" BACK Argument1="+str+" ");
      
      if((System.getProperty("DBUG_MESS") != null) &&
	 System.getProperty("DBUG_MESS").equalsIgnoreCase("ON")) {
        System.out.println("Sending command \"" + command + "\".");
      }
      
      try {
	sendCommand();
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }    
  }
  
  public void call_s_Back(String str) {
      command=new String("Dobey "+"QL_"+instName+" S_BACK Argument1="+str+" ");
      
      if((System.getProperty("DBUG_MESS") != null) &&
	 System.getProperty("DBUG_MESS").equalsIgnoreCase("ON")) {
        System.out.println("Sending command \"" + command + "\".");
      }
      
      try {
	sendCommand();
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }    
  }
  
  public void call_Mask(String str) {
      command=new String("Dobey "+"QL_"+instName+" MASK Argument1="+str+" ");

      if((System.getProperty("DBUG_MESS") != null) &&
	 System.getProperty("DBUG_MESS").equalsIgnoreCase("ON")) {
        System.out.println("Sending command \"" + command + "\".");
      }
      
      try {
	sendCommand();
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }    
  }
  
  public void call_s_Mask(String str) {
      command=new String("Dobey "+"QL_"+instName+" S_MASK Argument1="+str+" ");

      if((System.getProperty("DBUG_MESS") != null) &&
	 System.getProperty("DBUG_MESS").equalsIgnoreCase("ON")) {
        System.out.println("Sending command \"" + command + "\".");
      }
      
      try {
	sendCommand();
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }    
  }
  
  
  public void applyQlook(String cutRow, String backFile) {
    applyQlook(cutRow, backFile, "imaging");
  }
  
 /**
    public void  applyQlook()
    apply new conditions on a quick look of the modified GAIA
    
    @param none
    @return none
    @throws none
 */
  public void applyQlook(String cutRow, String backFile, String mode)
    {
      System.out.println("applyQlook called with mode \"" + mode + "\"");
      
      String action;
      if(mode.equalsIgnoreCase("spectroscopy")) {
        action = "S_SETUP";
      }
      else {
        action = "SETUP";
      }

	
      command=new String ("Dobey "+"QL_"+instName+" " + action + " rowvalue="+cutRow+" "+
			  "x1value=1 y1value=2 x2value=3 y2value=4 ");
      
      if((System.getProperty("DBUG_MESS") != null) &&
	 System.getProperty("DBUG_MESS").equalsIgnoreCase("ON")) {
        System.out.println("Sending command \"" + command + "\".");
      }

      try {
        sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }

  /**
   * Switch between imaging and spectroscopy mode.
   */
  public void switchMode(String mode)
    {
      command=new String ("Dobey "+"QL_"+instName+" SWITCH_MODE Argument1="+mode+" ");
      System.out.println("Sending command \"" + command + "\".");
      try {
        sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }

  
  /**
     public void  stopMovie()
     stop a movie and calls sendCommand()
     
     @param none
     @return none
     @throws none
  */
  public void stopMovie ( )
    {
      // Set up command (hate the hardcoding here...)
      String instTask = System.getProperty (instName, instName);
      if (instName.equalsIgnoreCase("michelle")) {
	command=new String("Dobey "+instTask+" endMovie");
      } else if (instName.equalsIgnoreCase("ufti")) {
	command=new String("Dobey "+instTask+" STOP_MOVIE ");
      } else {
	command = null;
      }

      // And send it
      try {
	sendCommand();
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }

  /**
     public void setResume (int tag)
     sets up command content and calls sendCommand()
     it will run an action continue in a drama task OOS_***
     
     @param int tag
     @return none
     @throws RemoteException
  */
  public void setResume (int tag) throws RemoteException
    {
      if (controlTag>tag) {
	new messageBox("Nothing done since this is disabled, Sorry!");
	return;
      }
      command=new String("Dobey OOS_"+instName+" continue ");
      
      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
    }
  
 /**
    public void setAbort (int tag)
    sets up command content and calls sendCommand()
    it will run an action abort in a drama task OOS_***
    
    @param int tag
    @return none
    @throws RemoteException
 */
  public void setAbort (int tag) throws RemoteException
    {
      if (controlTag>tag) {
	new messageBox("Nothing done since this is disabled, Sorry!");
	return;
      }

      command=new String("Dobey OOS_"+instName+" abort ");
      
      try {
	sendCommand();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCommand:"+re);
      }
      
    }

  /** 
      public void setTitle (String s)
      sets up command content and calls sendCommand()
      it will run an action MESSAGE in a drama task MONITOR_***

      @param String
      @return none
      @throws RemoteException
  */
//    public void setTitle (String s) throws RemoteException
//      {
//        // Do nothing. Used to do something stupid, now superceded by
//        // something more sensible...
//      }
  
 /**
    public void setStop (int tag)
    sets up command content and calls sendCommand()
    it will run an action stop in a drama task OOS_***
    
    @param int tag
    @return none
    @throws RemoteException
 */
  public void setStop (int tag) throws RemoteException
  {
    if (controlTag>tag) {
      new messageBox("Nothing done since this is disabled, Sorry!");
      return;
    }

    command=new String("Dobey OOS_"+instName+" stop ");

    try {
      sendCommand();
    }   catch (RemoteException re) {
      System.out.println ("Exception in sendCommand:"+re);
    }
  }
  
  /**
     public void setComment (String s, int tag)
     sets up command content and calls sendCommand()
     it will run an action log in a drama task DHSPOOL
     
     @param String, int
     @return none
     @throws RemoteException
  */
  public void setComment (String s, int tag) throws RemoteException
    {
      command=new String("Dobey DHSPOOL_"+instName+" log Argument1=\""+s+"\" ");
      sendCommand();
    }
  
  /** public void setTag (int i)
      sets up tag value of a screen i.e. TO's screen or observer's screen
      
      @param int
      @return none
      @throws RemoteException
  */
  public void setTag (int i) throws RemoteException
    {
      
      controlTag=i;
      
    if (controlTag>0)
      new messageBox("Your command sending ability is disabled, Sorry!"+
		     "\nPlease click OK to acknowledge");
    else
      new messageBox("Your command sending ability is enabled"+
		     "\nPlease click OK to acknowledge");
  }

  /**
     public void linkRemoteFrame (remoteFrame f)
     sets up a link to a remoteFrame object
     
     @param remoteFrame f
     @return none
     @throws none
  */
  public void linkRemoteFrame (remoteFrame f) {frame=f;}
  
  /** 
      public void linkMovieFrame (movie m)
      sets up a link to a movieFrame object
      
      @param movieFrame m
      @return none
      @throws none
  */
  public void linkMovieFrame (movie m) {_movie=m;}
  
  /**
     public void setTCSconnected() is to connect the telescope
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setTCSconnected ()  throws RemoteException
    {
      if (listFrames.getConnectedInstrument().equals("NONE")) {
	command = new String
	  ("Dobey OOS_"+instName+" target Argument1=Tel Argument2=enable ");
	sendCommand();
      } else {
	command = new String
	  ("Dobey OOS_"+listFrames.getConnectedInstrument()+
	   " target Argument1=Tel Argument2=disable ");
	sendCommand();
      }
    }

  /**
     public void setTCSdisconnected() is to connect the telescope

     @param none
     @return none
     @throws RemoteException
  */
  public void setTCSdisconnected ()  throws RemoteException
  {
    new AlertBox (instName + " is going to be disconnected from the TCS");
    command = new String
      ("Dobey OOS_"+instName+" target Argument1=Tel Argument2=disable ");
    sendCommand();
  }

  private movie _movie;
  private int controlTag=0;
  private remoteFrame frame;
  private DramaSocket ssocket;
  private String command=null, instName;
  private Process p;
  private FrameList listFrames;
}
