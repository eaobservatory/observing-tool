package om.sciProgram;
import om.console.*; //this is for the sequence console
import java.rmi.*;

public interface rmiInterface extends Remote
{
  public sequenceFrame getConsole (sequenceFrame f) throws RemoteException;


}







