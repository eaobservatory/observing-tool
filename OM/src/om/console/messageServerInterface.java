package om.console;
import java.rmi.*;
import javax.swing.*;
import java.util.*;

/** public interface messageServerInterface is a interface
    Please note this extends Remote for RMI calls.
    This is realized by the mothods in messagerServer class

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
public interface messageServerInterface extends Remote
{
  public void initSequence() throws RemoteException;
  public void processMessage(String m) throws RemoteException;
  public void stateMessage(String s) throws RemoteException;
  public void execlistMessage(String s) throws RemoteException;
  public void instMessage(String name, String value) throws RemoteException;
  public void dhsMessage(String name, String value)  throws RemoteException;
  public void indexMessage(String s) throws RemoteException;
  public void breakPointMessage() throws RemoteException;
  public void cancelBreakPoint() throws RemoteException;

  public void linkItemsShown (itemsShown i) throws RemoteException;
  public void linkCommandSent (sendCmds c)  throws RemoteException;
  public void linkSequencePanel (sequencePanel p) throws RemoteException;
  public void linkUpperPanel (upperPanel u) throws RemoteException;

  public void linkTargetPanel(targetPanel t) throws RemoteException;
  public void linkDhsPanel(dhsPanel d)  throws RemoteException;

  public void linkUFTIStatus (UFTIStatus s) throws RemoteException;
  public void linkCGS4Status (CGS4Status s) throws RemoteException;
  public void linkIRCAM3Status (IRCAM3Status s) throws RemoteException;
  public void linkMichelleStatus (MichelleStatus s) throws RemoteException;


  public void linkMovieFrame (movie m) throws RemoteException;

  public void setSeqData (Vector v) throws RemoteException;
  public Vector getSeqData () throws RemoteException;
}

