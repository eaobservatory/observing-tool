package om.dramaSocket;

import om.console.messageServerInterface;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.*;

import java.rmi.*;

/**
  InputThread class is for receiving messages from a client connection, 
  and sending messages to an interface of a console.
  it has one constructor and one public method
  
  @version 1.0 1st June 1999
  @author M.Tan@roe.ac.uk
*/
final public class InputThread extends Thread implements java.io.Serializable
{

/**
  public InputThread(InputStream is, messageServerInterface m) is
  the class constructor. The class has only one constructor so far.
  the two things are done during the construction.
  1) getting a InputStream object from the socket connection
  2) getting a reference for the messageServer interface object.
  Please note that the object is run at a different thread
*/
   public InputThread(InputStream is, messageServerInterface m) {
     this.is=is;
     messagers.addElement(m);
     
     if(System.getProperty("SOCK_MESS").equals("ON"))
       socketMessage=true;
   }

/**
  public void run() is a public method
  it reads a character from the InputStream of the socket connection one
  by one. Once a newline \n character is detected, it forms those 
  characters read into a string and sends this string to the messageserver
  object for processing.
  it is called by the common thread "start" method

  @param none
  @return none
  @throws none
*/
  public void run () {
    try {
      for(int j=0;j<messagers.size();j++) {
	messageServerInterface t=(messageServerInterface) messagers.elementAt(j);
	t.initSequence();
      }
    } catch (RemoteException e) {
      System.out.println("InputThread has "+ e.toString());
    }


    try {
      String temp="";
      while(true) {
	int i= is.read();

	if(i==-1) {
	  break;
	}
	char c=(char) i;
	if(i==0) {
	  continue;
	}else{
	  temp +=c;
	}

	if(c=='\n') {
	  if(temp.length()>0) {
	    if(temp.length()>10) {
	      try {
		for(int j=0; j<messagers.size();j++) {
		  messageServerInterface t=(messageServerInterface) messagers.elementAt(j);
		  t.processMessage(temp.substring(0,temp.length()-1));
		}
	      } catch (RemoteException e) {
		System.out.println("InputThread object has "+ e.toString());
	      }
	    }

	    // If this is an internal Java-Drama comms message then
	    // only print it if debugs are turned on.
	    if (temp.startsWith("msg: PARA:") || 
		temp.startsWith("msg: Untreated") ||
		temp.startsWith("EXECLIST::") ||
		temp.startsWith("msg: EXEC")) {
	      if (socketMessage) System.out.println(temp);
	    } else {
	      // Otherwise always print it
	      System.out.println (temp);
	    }
	    temp = "";    //set it empty
	  }
	}
      }
      
    } catch (IOException e) {
      System.err.println(e);
    }
  }


/**
  public void addMessager (messageServerInterface m) is a public method
  it adds a message server interface of a console into the inputthread object.
  Currently, it only allows two message server interfaces for each inputThread object.
  One is for the console of a observer and the other is for the ehco of the console for
  a TO (Telescope Officer) at UKIRT

  @param messageServerInterface m
  @return none
  @throws none
*/
  public void addMessager (messageServerInterface m)
  {
    if(messagers.size()>1)
      for(int j=1; j<messagers.size();j++)
        messagers.removeElementAt(j);  //limiting only one copy of the ehcos

    messagers.addElement(m);

  }

  protected void finalize ()
  {
    if(this.isAlive())
      this.stop();
  }


  private Vector messagers=new Vector();
  private InputStream is;
  private boolean socketMessage=false;
}
