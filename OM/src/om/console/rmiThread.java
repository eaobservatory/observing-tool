package om.console;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import java.rmi.*;

/**
  rmiThread class keeps looking up a rmi server every 3 seconds. If the
  server crashes, it will die too.
  it has one constructor and one public method

  @version 1.0 1st June 1999
  @author M.Tan@roe.ac.uk
*/

final public class rmiThread extends Thread
{
/**
  public rmiThread(String name) is
  the class constructor. The class has only one constructor so far.
  Please note that the object is run at a different thread
*/
   public rmiThread()
   {
   }

/**
  public void run() is a public method
  it keeps looking up a rmi server

  @param none
  @return none
  @throws InterruptedException if bad argument
*/

   public void run (String name)
   {
     setPriority(MIN_PRIORITY);

     while(true)
       {
	 try {
	   rmiInterface h =(rmiInterface) Naming.lookup(name);
	   this.sleep(3000);
	 } catch (RemoteException e) {
	   System.exit(0);

	 } catch (Exception e) {
	   
	   System.out.println("RMI client says: Goodbye Telescope Officer!");
	   System.exit(0);
	 }
       }
   }
}
