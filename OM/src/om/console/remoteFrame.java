package om.console;

import om.dramaSocket.*;
import om.frameList.*;
import java.util.*;
import java.io.*;
import java.rmi.*;
import java.net.*;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.*;

/** 
    final public class remoteFrame is the most important class in this package.
    it extends UnicastRemoteObject as a RMI server.
    REMEMBER: it is required to be compiled by a rmic
    
    @param none
    @return none
    @throws none
*/
 
final public class remoteFrame extends UnicastRemoteObject
  implements rmiInterface
{
  /**
     public remoteFrame(String name, String title,Vector frs) is
     the class constructor. The class has only one constructor so far.
     A few things are done during the construction. They are
     1) creat a object of messageServer
     2) creat a object of Server socket
     3) creat a object of commandSent
     4) creat a object of sequence console
     
     @param String name, String title,Vector frs
     @return  none
     @throws RemoteException
  */
  public remoteFrame(String name, String title,FrameList frs)
    throws RemoteException
    {
      super ();
      
      m=new messageServer(name,frs);
      
      dsocket=new DramaSocket(1234,(messageServerInterface)m);
      
      com=new commandSent(name,dsocket,frs);
      com.linkRemoteFrame(this);
      
      //try {
      //  Naming.rebind("commandSent"+name,com);
      //}
      //catch (MalformedURLException e) {
      // System.out.println("MalformedURLException in RemoteFrame object:" + e);
      //}
      
      sendCmds cmd=new sendCmds((commandSentInterface)com);
      frame = new sequenceFrame(name,title,cmd);
      frame.setLinks((messageServerInterface)m);
      
      frame.show();
      dsocket.start();
      
      System.out.println("RMI Server for the "+name+" console is not ready!");
    }
  
  
  /** public sequenceFrame getConsole () is a public method to return
      a sequence console object for RMI calls. This is a old method and
      not used anymore.
      
      @param none
      @return  sequenceFrame
      @throws RemoteException
  */
  public Object getConsole () throws RemoteException {
    
    return frame;
  }
  
  /** public myFrameModel getFrameModel() is a public method to return
      a myFrameModel object for RMI calls.
      
      @param none
      @return myFrameModel
      @throws RemoteException
  */
  public myFrameModel getFrameModel () throws RemoteException {
    
    myFrameModel model=new myFrameModel();
    
    model.setInstrument(frame.getInstrument());
    model.setObservation(frame.getObservation());
    
    //model.setSequencePanel(frame.getSequencePanel());
    model.setUpperPanel(frame.getUpperPanel().getModel());
    model.setDhsPanel(frame.getDhsPanel().getModel());
    
    instrumentStatusPanel temp=(instrumentStatusPanel)frame.getStatusPanel();
    model.setStatusPanel(temp.getModel());
    
    model.setMenuBar(frame.getMyMenuBar().getModel());

    return model;
  }
  
  /** public commandSentInterface getComm ()  is a public method to return
      a commandSentInterface object for RMI calls. This is an RMI call by 
      reference
      
      @param none
      @return  commandSentInterface
      @throws RemoteException
  */
  public commandSentInterface getComm () throws RemoteException {
    return com;
  }
  
  /** public void setMessager (messageServerInterface mess) is a public
      method to link a messagerServerInterface object with the Server 
      socket object.
      
      @param none
      @return  commandSentInterface
      @throws RemoteException
  */
  public void setMessager (messageServerInterface mess) throws RemoteException
    {

      mess.setSeqData(frame.getSequencePanel().getExecData());
      
      dsocket.getInputThread().addMessager(mess);
      echo=false;
      
      // this is a work-around to get rid of the problem in seqPanel due
      // to from a rmi call.
      frame.getSequencePanel().setMode(frame.getSequencePanel().getMode());
    }
  
  
  /**  public String getString ()  is a public method to return
       a String object for RMI calls.
       
       @param none
       @return  sequenceFrame
       @throws RemoteException
  */
  public String getString () throws RemoteException {
    if(echo)
      return new String ("Hello Telescope Officer!!");
    else
      return new String ("No echo screen for you, sorry");
  }
  
  /** Vector getDramaTasks() is a public method to return
      a Vector object for local calls.
      
      @param none
      @return  Vector
      @throws none
  */
  public Vector getDramaTasks() {return dramaTasks; }
  
  
  /**sequenceFrame getFrame() is a public method to return
     a sequenceFrame object for local calls.
     
     @param none
     @return  sequecenFrame
     @throws none
  */
  public sequenceFrame getFrame() {return frame;}
  
  /**commandSent getCommandSent() is a public method to return
     a getCommandSent object for local calls.
     
     @param none
     @return commandSent
     @throws none
  */
  public commandSent getCommandSent () {return com;}
  
  public DramaSocket getDramaSocket () {return dsocket;}

  private messageServer m;
  private sequenceFrame frame;
  private sendCmds comSent;
  private commandSent com;
  private Vector dramaTasks=new Vector();
  private DramaSocket dsocket;
  private boolean echo=true;
}
