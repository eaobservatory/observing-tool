package om.console;
import java.util.*; 
import java.rmi.*;
import javax.swing.*;

/** public interface commandSentInterface is a interface which is realized in the class commandSent
    Please note this extends Remote for RMI calls

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
public interface commandSentInterface extends Remote
{
  public void sendCommand() throws RemoteException;
  public void setInit (int i) throws RemoteException;
  public void setStart () throws RemoteException;
  public void setMonitorOn () throws RemoteException;
  public void setLoad (String filename,int i) throws RemoteException;
  public void setLink (String task) throws RemoteException;
  public void setExit (int i) throws RemoteException;
  public void setClear(int i) throws RemoteException;
  public void setBreakPoint (int i) throws RemoteException;
  public void setCancelBreak(int i) throws RemoteException;
  public void setRun (int i) throws RemoteException;
  public void setRunSpecial (int temp,int i ) throws RemoteException;
  public void setPause (int i) throws RemoteException;
  public void setResume (int i) throws RemoteException;
  public void setAbort (int i) throws RemoteException;
  public void setStop (int i) throws RemoteException;
  public void setComment (String s,int i)  throws RemoteException;
  public void setTag (int i) throws RemoteException;
  public void setMovie(int i) throws RemoteException;
  public void startMovie(String s) throws RemoteException;
  public void stopMovie() throws RemoteException;
  public void call_Back(String str) throws RemoteException;
  public void call_s_Back(String str) throws RemoteException;
  public void call_Mask(String str) throws RemoteException;
  public void call_s_Mask(String str) throws RemoteException;
  public void applyQlook(String cutRow, String backFile) throws RemoteException;
  public void applyQlook(String cutRow, String backFile, String mode) 
    throws RemoteException;
  public void applyQlook(String cutRow, String x1, String x2, String y1, String y2, 
                         String backFile, String mode) throws RemoteException;
  public void switchMode(String mode) throws RemoteException;
  public void setTCSconnected() throws RemoteException;
  public void setTCSdisconnected() throws RemoteException;
//   public void linkRemoteFrame (remoteFrame f) throws RemoteException;
//   public void linkMovieFrame (movie m) throws RemoteException;
}


