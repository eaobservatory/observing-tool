import java.io.*;
import java.rmi.*;
import java.util.*;
import ODBServerPackage.*;

class ODBAdmin
{
  public static void main(String[] args)
  {
    boolean passwdOK=true;
    String userid;
    String passwd;

    System.setSecurityManager(new RMISecurityManager());

    BufferedReader in = new BufferedReader ((new InputStreamReader (System.in)));
    try
    {
      ODBServerMethods odbs = (ODBServerMethods) 
	Naming.lookup("rmi://alba.roe.ac.uk:4201/ODBServer");

//       System.out.print ("Password: ");
//       passwd = in.readLine();
//       passwdOK =odbs.checkPassword("administrator",passwd);
      if(passwdOK)
	{
	  System.out.print ("ODBServer Command: ");
	  String cmd = in.readLine();
	  if (cmd.equals("exit")) {
	    odbs.exit();
	  }else{
	    System.out.println ("Unknown command " + cmd);
	  }
	}else{
	  System.out.println ("Incorrect password");
	}

    }
    catch (RemoteException re)
    {
      System.out.println ("Exception in ODB server main:"+re);
    }
    catch (Exception e)
    {
      System.out.println("Exception in test client main:" + e);
    }
    
  }
}
