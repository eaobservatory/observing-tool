package om.sciProgram;
import java.io.*;
import java.net.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import om.console.*; //this is for the sequence console

class rmiImpl extends UnicastRemoteObject implements rmiInterface
{
  public rmiImpl() throws RemoteException
  {
    super ();
  }

  public sequenceFrame getConsole (sequenceFrame f) throws RemoteException {
  return f;
  }


}







