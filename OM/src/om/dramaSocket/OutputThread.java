package om.dramaSocket;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.*;

/**
  OutputThread class is for sending commands/messages out to the client connection
  it has one constructor and one public method

  @version 1.0 1st June 1999
  @author M.Tan@roe.ac.uk
*/
final public class OutputThread extends Thread implements java.io.Serializable
{
/**
  public OutputThread(OutputStream os,String command) is
  the class constructor. The class has only one constructor so far.
  the two things are done during the construction.
  1) getting a OutputStream object from the socket connection
     and connecting it into a PrintStream object.
  2) getting a command string from a commandSent server object.
  Please note that the object is run at a different thread.
*/
  public OutputThread(OutputStream os,String command)
  {
    ps=new PrintStream(os);
    this.command=command;
  }

/**
  public void run() is a public method
  it puts a command string into the OutputStream of the socket connection.
  it is called by the thread start method.
  it sends a command string down to the socket

  @param none
  @return none
  @throws none
*/
  public void run ()
  {
    //    System.out.println(command);
    ps.println(command);
  }

  protected void finalize ()
  {
    if(this.isAlive())
      this.stop();
  }

  private String command;
  private PrintStream ps;
}

