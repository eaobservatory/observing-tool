package om.console;
import java.util.*; //this is for the sequence console
import java.rmi.*;
import javax.swing.*;

/** interface rmiInterface is a interface
    Please note this extends Remote for RMI calls
    it is realized by the remoteFrame class

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
public interface rmiInterface extends Remote
{
  public Object getConsole ( ) throws RemoteException;
 
  public String getString( ) throws RemoteException;
  public commandSentInterface getComm( ) throws RemoteException;
  public void setMessager(messageServerInterface m) throws RemoteException;
  public myFrameModel getFrameModel ( ) throws RemoteException;
}


