/**
  dramaSocket.java 1.0 99/06/01
  DramaSocket.java 1.1 99/06/11
  DramaSocket.java 1.2 99/09/01

  Copyright 1998 by PPARC UK,
  Personnel Min Tan UK ATC Edinburgh EH9 3HJ.
  Email:mt@roe.ac.uk
  All rights reserved.

  This package is Java codes to connect a drama
  C code via interprocess communcation (a socket).
  This has been seperated from the console package on 99/06/11
  as an independent package "dramaSocket" which has a few classes:
  1) DramaSocket for creating a Java server socket.
  2) InputThread for handling the input messages from the drama tasks
  3) OutputThread for sending out commands to the drama tasks
  4) execDtask for starting up a drama C task
  5) mainTest for testing this package independently
*/

package om.dramaSocket;

import om.console.messageServerInterface;
import om.console.ErrorBox;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.*;

/** DramaSocket class is for creating a Java server socket
    It was designed that each instrument has its own sequence console.
    As a result, it needs its own socket server/number and a message server,
    which are both the parameters during a obejct construction. See the 
    contructor
    This class has one constructor and two public methods.
    It is important to close the server socket before the sequence console is 
    closed, otherwise, it could lead to a "resource" leak problem after 
    opening/closing a console for many times e.g 10 times.
    @version 1.0 1st June 1999
    @version 1.1 11th June 1999
    @version 1.2 1st Sept 1999
    @author M.Tan@roe.ac.uk
*/

final public class DramaSocket extends Thread implements java.io.Serializable
{
/**  public DramaSocket(int instrument_name, messageServerInterface m) is
    the class constructor. The class has only one constructor so far.
    the two things are done during the construction.
    1) getting a port number for an instrumnet, default is 1234
    2) getting a reference for the first message server.
       It can be added more message server when rmi clients ask.

    @param a int and a messageServerInterface
    @return  none
    @throws none
    Please note that the object is run at a different thread
*/
  public DramaSocket(int instrument_name, messageServerInterface m) //constructor
  {
    System.out.println("Starting up a DramaSocket object");
    thePort=instrument_name;
    mess=m;
  }

/** public void run() is a public method
    it creats a server socket waiting for a client connection
    Once a connection is set up, it creats an Inputthread object and runs it
    This method is called by the common thread "start" method

    @param none
    @return none
    @throws InterruptedException if bad argument
*/
  public void run()
  {
    try
    {
      ss=new ServerSocket(thePort);  //starting a server socket here with a configured port number
      System.out.println("Listening for connection on port:"+ss.getLocalPort());

      String port=String.valueOf(ss.getLocalPort());

      //putting port information into system properties for later use
      Properties temp=System.getProperties();
      temp.put("SOCK_HOST",InetAddress.getLocalHost().getHostName());
      temp.put("SOCK_PORT",port);

      while(true)
      {
        connection=ss.accept();  //waiting for a connection from the drama task of socket
        System.out.println("Connection established with: \n " + connection);

        //starting a message input monitoring thread object
System.out.println("MFO DEBUG2 in DramaSocket 1");
        it =new InputThread(connection.getInputStream(),mess);
System.out.println("MFO DEBUG2 in DramaSocket 2");
        it.start();
System.out.println("MFO DEBUG2 in DramaSocket 3");

        try
        {
System.out.println("MFO DEBUG2 in DramaSocket 4");
          it.join();
System.out.println("MFO DEBUG2 in DramaSocket 5");
        }
        catch (InterruptedException e)
        {
System.out.println("MFO DEBUG2 in DramaSocket 6");
          System.err.println(e);
System.out.println("MFO DEBUG2 in DramaSocket 7");
        }

System.out.println("MFO DEBUG2 in DramaSocket 8");
      }

    }
    catch (IOException e) { //it is also catched when the socket is closed.

System.out.println("MFO DEBUG2 in DramaSocket 9");
      thePort++; //use another post number if the default is not working

System.out.println("MFO DEBUG2 in DramaSocket 10");
      if(reRun)
        run();
      else
        System.err.println(ss+" has "+e);

System.out.println("MFO DEBUG2 in DramaSocket 11");
    }
  }

/** public Socket getSocket() is a public method
    it does nothing but returing a client socket for the connection

    @param none
    @return  a Java socket object
    @throws none
*/
    public Socket getSocket(){return connection;}

/** public InputThread getInputThread () is a public method
    it does nothing but returing an inputThread object for the connection

    @param none
    @return  an inputThread object
    @throws none
*/
    public InputThread getInputThread () {return it;}


/** public void closeServerSocket() is a public method
    it does nothing but closing the server socket when a console is closed

    @param none
    @return none
    @throws IOException
*/
  public void closeServerSocket ()
  {
    try {
        reRun=false;   //reRun flag is required to prevant unwanted run() calls
        ss.close();
    } catch (IOException e)
    {
      new ErrorBox("Error in close "+ ss.toString()+" Due to "+e);
    }
  }

  protected void finalize ()
  {
    if(this.isAlive())
      this.stop();
  }

  private boolean reRun=true;
  private int thePort;
  private ServerSocket ss;
  private Socket connection;
  private InputThread it;
  private messageServerInterface mess; //a message interface for an Observer console
}



