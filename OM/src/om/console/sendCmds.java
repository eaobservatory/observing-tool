package om.console;

import java.util.*;
import java.rmi.*;
import javax.swing.*;
import java.lang.*;
import orac.helptool.JHLauncher;

/** final public class sendCmds
    connects to a remote object commandSent to send out commands.
    This is actully a local wrapper for the commandSent class
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. But there is only one type of host anyway.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class sendCmds implements java.io.Serializable
{
  /**
     public sendCmds(commandSentInterface s) is
     the class constructor. The class has only one constructor so far.

     @param commandSentInterface s
     @return  none
     @throws none
  */
  public sendCmds(commandSentInterface s)
    {
      c=s;
    }

  /**
     public void setInit ()
     calls the related method in a commandSend object

     @param none
     @return none
     @throws RemoteException
  */
  public void setInit ()
    {
      try {
	c.setInit(controlTag);
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCmds:"+re);
      }
    }


  /**
     public void setLink ()
     calls the related method in a commandSend object
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setLink (String task)
    {
      try {
	c.setLink(task);
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCmds:"+re);
      }
    }

  /**
     public void setStart()
     calls the related method in a commandSend object
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setStart ()
    {
      try {
	c.setStart();
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCmds:"+re);
      }
    }
  
  /**
     public void setMonitorOnt()
     calls the related method in a commandSend object
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setMonitorOn ()
    {
      try {
	c.setMonitorOn();
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCmds:"+re);
      }
    }
  
  /**
     public void setTCSconnected()
     calls the related method in a commandSend object
     to connect the telescope
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setTCSconnected ()
    {
      try {
	c.setTCSconnected();
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCmds:"+re);
      }
    }
  
   /**
      public void setTCSdisconnected()
      calls the related method in a commandSend object
      to disconnect the telescope
      
      @param none
      @return none
      @throws RemoteException
   */
  public void setTCSdisconnected ()
  {
    try {
      c.setTCSdisconnected();
    }   catch (RemoteException re) {
      System.out.println ("Exception in sendCmds:"+re);
    }
  }

  /**
     public void setLoad ()
     calls the related method in a commandSend object

     @param String
     @return none
     @throws RemoteException
  */
  public void setLoad (String filename)
  {
    try {
      c.setLoad(filename,controlTag);
    }   catch (RemoteException re) {
      
      System.out.println ("Exception in sendCmds:"+re.toString());
    }
  }
  
  /** 
      public void  Content () is a public method to
      launch a javahelp frame panel.

      @param none
      @return none
      @throws none
  */
  public void Content ()
    {
      //different help files can be selected depending on whether this is
      // the TO copy
      if (controlTag>0) {
	args[1]="om.hs";
      } else {
	args[1]="om.hs";
      }
      JHLauncher l=new JHLauncher(args);
      if(System.getProperty("DBUG_MESS").equals("ON"))
        System.out.println("JHL:"+l);
    }


  /**
     public void setExit ()
     calls the related method in a commandSend object
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setExit ( )
    {
      try {
        c.setExit(controlTag);
      }   catch (RemoteException re) {
	
        if(controlTag>0)
          System.exit(0);
	
	System.out.println("Exception in sendCmds:"+re);
      }
  }

  /**
     public void setClear ()
     calls the related method in a commandSend object
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setClear()
    {
      try {
	c.setClear(controlTag);
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCmds:"+re);
      }
    }
  
  /**
     public void setBreakPoint ()
     calls the related method in a commandSend object

     @param none
     @return none
     @throws RemoteException
  */
  public void setBreakPoint ()
    {
      try {
	c.setBreakPoint(controlTag);
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCmds:"+re);
      }
    }
  
  /**
     public void setCancelBreak ()
     calls the related method in a commandSend object
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setCancelBreak()
    {
      try {
	c.setCancelBreak(controlTag);
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCmds:"+re);
      }
    }
  
  /**
     public void setRun ()
     calls the related method in a commandSend object
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setRun ()
    {
      if(_movie.isVisible())
	{
	  new messageBox("You have to close the Movie frame first, Sorry!");
	  return;
	}
      
      try {
	c.setRun(controlTag);
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCmds:"+re);
      }
      
    }

  /**
     public void setSpecial ()
     calls the related method in a commandSend object
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setRunSpecial ()
    {
      int temp=seqPanel.getMyList().getSelectedIndex();
      int shift=1;
      
      if (_movie!=null) {
	if(_movie.isVisible())
	  {
	    new messageBox("You have to close the Movie frame first, Sorry!");
	    return;
	  }
      }
      
      if (temp<0) temp=0;

      //find a right index number for the oos task
      if (seqPanel.getMode()) {
	for(int index=0;index<temp;index++)
	  while (seqPanel.getExecData().elementAt(index+shift).
		 toString().substring(0,1).equals("-")) {
	    shift++;
	  }
      }

      temp=temp+shift;

      try {
        c.setRunSpecial(temp,controlTag);
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCmds:"+re);
      }
    }

  /**
     public void setPause ()
     calls the related method in a commandSend object
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setPause ()
  {
    try {
      c.setPause(controlTag);
    } catch (RemoteException re) {
      System.out.println ("Exception in sendCmds: "+re);
    }
  }
  
  /**
     public void setResume ()
     calls the related method in a commandSend object
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setResume ()
  {
    try {
      c.setResume(controlTag);
    } catch (RemoteException re) {
      System.out.println ("Exception in sendCmds: "+re);
    }
  }

  /**
     public void setAbort ()
     calls the related method in a commandSend object
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setAbort ()
    {
      JOptionPane dialog = new JOptionPane();

      int selection=dialog.showConfirmDialog (null, "Abort this sequence?",
					      "Abort Comfirmation", 
					      JOptionPane.YES_NO_OPTION,
					      JOptionPane.QUESTION_MESSAGE,
					      null );

      if(selection==dialog.YES_OPTION) {
	
	try {
	  c.setAbort(controlTag);
	} catch (RemoteException re) {
	  System.out.println ("Exception in sendCmds: "+re);
	}
	
      }
      
      dialog=null;
      System.gc();
      
    }

  /**
     public void setStop ()
     calls the related method in a commandSend object
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setStop ()
    {
      try {
	c.setStop(controlTag);
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCmds: "+re);
      }
    }

  /**
     public void setComment ()
     calls the related method in a commandSend object
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setComment (String s)
    {
      try {
	c.setComment(s,controlTag);
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCmds: "+re);
      }
    }
  
  /**
     public void setMovie ()
     sets a movie mode
     
     @param none
     @return none
     @throws RemoteException
  */
  public void setMovie ()
    {
      //if(controlTag==1)
      //{
      //  new messageBox("Nothing done since the Movie funtion is only for a Observer, Sorry!");
      //  return;
      //}
      
      try {
	if(!_movie.isVisible())
          _movie.show();
	//disable all the command buttons and meuns here
	
      }   catch (Exception e) {
	System.out.println("Exception in sendCmds:"+e);
      }
    }
  
  /**public void  startMovie(int sec)
     starts up a movie and calls sendCommand()
     
     @param int sec
     @return none
     @throws none
  */
  public void startMovie (String sec)
    {
      try {
	c.startMovie(sec);
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCmds: "+re);
      }
    }
  
  /**public void  stopMovie()
     stop a movie and calls sendCommand()
     
     @param none
     @return none
     @throws none
  */
  public void stopMovie ()
    {
      try {
	c.stopMovie();
      } catch (RemoteException re) {
	System.out.println ("Exception in sendCmds: "+re);
      }
    }

  
  public void call_Back(String str) {
    try {
	c.call_Back(str);
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCmds:"+re);
      }
  }
  
  public void call_s_Back(String str) {
    try {
      c.call_s_Back(str);
    }   catch (RemoteException re) {
      System.out.println ("Exception in sendCmds:"+re);
    }
  }
  
  public void call_Mask(String str) {
    try {
      c.call_Mask(str);
    }   catch (RemoteException re) {
      System.out.println ("Exception in sendCmds:"+re);
    }
  }
  
  public void call_s_Mask(String str) {
    try {
      c.call_s_Mask(str);
    }   catch (RemoteException re) {
      System.out.println ("Exception in sendCmds:"+re);
    }
  }
  
  
  /**
     public void applyQlook()
     apply new conditions on the quick look
     
     @param none
    @return none
    @throws none
  */
  public void applyQlook (String cutRow, String backFile, String mode)
    {
      try {
	c.applyQlook(cutRow,backFile, mode);
      } catch (RemoteException re) {
        System.out.println ("Exception in sendCmds: "+re);
      }
    }
  
  /**
   * Switch between imaging and spectroscopy mode.
   */
  public void switchMode (String mode) {
    try {
      c.switchMode(mode);
    }   catch (RemoteException re) {
      System.out.println ("Exception in sendCmds:"+re);
    }
  }
  
  /**
     public void linkSequencePanel (sequencePanel seq)
     links with a sequencePanel object.
     
     @param sequencePanel
     @return none
     @throws none
  */
  public void linkSequencePanel (sequencePanel seq)
    {
      seqPanel=seq;
    }
  
  /** public void linkMovieFrame (movie m)
      links with a _movie object.
      
      @param movie
      @return none
      @throws none
  */
  public void linkMovieFrame (movie m) {
    _movie=m;
  }
  
  /**
     public void setControl (int i)
     sets a control tag/switch

     @param int
     @return none
     @throws RemoteException
  */
  public void setControl (int i)
    {
      try {
	c.setTag(i);
      }   catch (RemoteException re) {
	System.out.println ("Exception in sendCmds: "+re);
      }
    }
  
  /**public void setTag (int i)
     sets object itself a control priviliage
     
     @param int
     @return none
     @throws RemoteException
  */
  public void setTag (int i) {controlTag=i;}
  
  
  private sequencePanel seqPanel;
  private commandSentInterface c;
  private int controlTag=0;
  private movie _movie;
  private String[] args={"-helpset","om.hs"};
}

