package om.sciProgram;

import gemini.sp.*;
import gemini.sp.ipc.*;

import orac.ukirt.inst.*;
import orac.ukirt.iter.*;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import om.util.OracColor;


/** public final class spConnector is a class to connect the
    OT database server via rmi and to get a set of science programs
    out from the ot server under a correct user ID and password

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
public final class spConnector implements SpServerWatcher
{
/**  public spConnector(String username, String password,JFrame m) is
    the constructor. The class has only one constructor so far.
    a few thing are done during the construction. They are mainly
    about to creat SpAccess object and fetch a program list.
    Note it implements SpServerWatcher interface of OT.

    @param  String username, String password,JFrame m
    @return none
    @throws RemoteException
*/
  public spConnector(String username, String password,JFrame m, selectorFrame s)
  {
     _username=username;
     _password=password;
     _loginFrame=m;
     _loginFrame.setEnabled(false);
     selector=s;

      if (_spAccess == null)
      _spAccess = new SpAccess(_password,SpAccess.PHASEII);

      fetchListing();  }

 /** public synchronized void spServerFetchComplete(SpItem spItem, SpServerAsync ssa)
    is called by SpServerAsync when a program fetch completes successfully.    @parm SpItem, SpServerAsync
    @return  none
    @throws none
    @see SpServerWatcher in gemini OT package
 */
  public synchronized void spServerFetchComplete(SpItem spItem, SpServerAsync ssa)
  {
     if (ssa != _spServerAsync) return;
     _spServerAsync = null;

      selector.stopFlush();

     selector.getTree().addTree(_spProgKey.id,spItem);
     selector.getTree().getRunButton().setEnabled(true);
     selector.getTree().getRunButton().setBackground(OracColor.green);

    int i=0;
    String temp=_spProgKey.toString();
    for(i=3;i<temp.length();i++)
      if(temp.substring(i-3,i).equals("key"))
          break;

    selector.getStatusBar().setText("Current Status: Fetched program: "+temp.substring(0,i-3));
  }

  /** public void stopAction()
    is an implementation of the StopActionWatcher interface.
    @parm none
    @return  none
    @throws none
    @see StopActionWatcher in gemini OT package
 */
  public void stopAction()
  {
     if (_spServerAsync != null) _spServerAsync.abort();
  }

  /** public synchronized void spServerListComplete(Vector progList, SpServerAsync ssa)
     is called by SpServerAsync when the program listing completes successfully.

    @parm Vector, SpServerAsync
    @return  none
    @throws none
    @see SpServerWatcher in gemini OT package
 */
 public synchronized void spServerListComplete(Vector progList, SpServerAsync ssa)
 {
   if (ssa != _spServerAsync) return;
   _spServerAsync = null;

   //comment out by mt June 1999
   //LastLogin.set(_username, _spAccess.db, _spAccess.password);
   _progs = progList;

   if(progList.size()<1)
    new ErrorBox("You have failed to log in the account: "+_username+
                  "\nPlease check your username and password.");
   else
   {
     if(selector==null)//first login
     {
       
       Properties temp=System.getProperties();
       temp.put(new String("loginUserId"),_username);
       
       _loginFrame.dispose();
       info=new bookFrame(_username,this); //starting a book keeping frame
       
       while(info.isVisible())
	 {
	 }
       
       selector=new selectorFrame(progList,this,_username);
        selector.show();
	
	// Check to see if last user equals new. If so then this is 
	// probably a refresh. If not it was probably a login
	// again, in which case start again. An attempt to get around 
	// the refresh bug introduced when Min fixed the login again 
	// bug (!). Bit of a hack but...
     } else if (!System.getProperty("loginUserId","---").equals(_username)) {
       
       Properties temp=System.getProperties();
       temp.put(new String("loginUserId"),_username);
       
       _loginFrame.dispose();
       info=new bookFrame(_username,this); //starting a book keeping frame
       
       while(info.isVisible())
	 {
	 }
       
       selector.reFreshSpList(progList);
       selector.getKey().setText(_username+"-0");
       selector.show();
     }else{
       // If java1.1 is used then this will throw a NullPointerException. Ignore it.
       try {
         _loginFrame.dispose();
       }
       catch(NullPointerException e) {
         // Do nothing.
       }

       // Skipping bookFrame ???
       selector.reFreshSpList(progList);
       selector.show();
     }
     
   }
 }
  
  /**public synchronized void spServerError(String problem, SpServerAsync ssa)
     is called by SpServerAsync when an operation fails.

    @parm String, SpServerAsync
    @return  none
    @throws none
    @see SpServerWatcher in gemini OT package
 */
  public synchronized void spServerError(String problem, SpServerAsync ssa)
  {
     if (ssa != _spServerAsync) return;
     _spServerAsync = null;


        int i=0;
       String temp=new String();

       for(i=problem.length();i>6;i--)
       if(problem.substring(i-6,i).equals("NoSuch"))
          temp=new String("\nIt could well be due to an incorrect user ID.");
       else if(problem.substring(i-6,i).equals("Access"))
          temp=new String("\nIt could well be due to an incorrect password/key.");

       new ErrorBox("Problem in communicating with ODB: the ODBServer may not running \n" + problem+'\n'+temp);

      _loginFrame.setEnabled(true);

      //for(i=problem.length();i>18;i--)
      //if(problem.substring(i-18,i).equals("Connection refused"))
      // return;
      //else
     try
     {
        selector.stopFetch();
     } catch (NullPointerException e)
     {
        
     }

   }

  /**  public synchronized void spServerAbort(SpServerAsync ssa)
     is called by SpServerAsync when an operation aborts.

    @parm SpServerAsync
    @return  none
    @throws none
    @see SpServerWatcher in gemini OT package
 */
  public synchronized void spServerAbort(SpServerAsync ssa)
  {
     if (ssa != _spServerAsync) return;
      _spServerAsync = null;

  }

 /** public synchronized void fetchListing()
     contacts the ODB and obtains a listing of available programs.
   @parm none
   @return  none
   @throws none
 */
  public synchronized void fetchListing()
  {
   // Make sure there isn't already an operation in progress.

    if (_spServerAsync != null) {
      return;
    }

    _spAccess.password=_password;
    _spServerAsync = SpServerAsync.list(_spAccess, _username, this);
  }

 /** public synchronized boolean fetchProg(String name)
     fetchs the selected program from the ODB.

   @parm String   @return  none   @throws none
 */
  public synchronized boolean fetchProg(String name)
  {
     // Make sure there isn't already an operation in progress.
     if (_spServerAsync != null) return false;

    // Make sure they've logged in.
     if ((_spAccess == null) || _spAccess.password.equals(""))
     {
         new ErrorBox("You have to login first.");
         return false;
     }

    // Get the selected name.
     String progName =name;
     if (progName == null)
     {
       new ErrorBox("You must select a program to fetch.");
        return false;
     }

   // Now unformat it to get the spXXX id.  The programs are listed either
   // as "spXXX" alone or "Arbitrary Title (spXXX)"
    if (progName.charAt(progName.length() - 1) == ')')
    {
      int index = progName.lastIndexOf('(');
      progName = progName.substring(index + 1, progName.length() - 1);
    }

    String key =selector.getKey().getText();
    if ((key == null) || (key.equals("")))
    {
      new ErrorBox("You must specify the program's key.");
      return false;
    }

    // Start the fetch.
    _spProgKey     = new SpProgKey(progName, key);

    _spAccess.password=_password;
    _spAccess.db=_spAccess.PHASEII;

    _spServerAsync = SpServerAsync.fetch(_spAccess, _spProgKey, this);
    return true;
  }

  /**  public void spServerStoreComplete(SpItem  spItem, SpProgKey key,
                      boolean isNew,  SpServerAsync ssa) {}
    is not in use and not implemented and could be usefule in future

    @parm SpItem, SpProgKey, boolean,  SpServerAsync
    @return  none
    @throws none
    @see SpServerWatcher in gemini OT package
 */
  public void spServerStoreComplete(SpItem spItem, SpProgKey key,boolean isNew,SpServerAsync ssa){};

  public void setPassword (String p) {_password=p;}
  public void setUsername (String u) {_username=u;}

  private SpAccess  _spAccess;
  private SpProgKey _spProgKey;
  private static String    _username;
  private static String    _password;
  private Vector    _progs;
  private JFrame _loginFrame;
  private SpServerAsync _spServerAsync;
  private bookFrame info;
  private selectorFrame selector;
}




