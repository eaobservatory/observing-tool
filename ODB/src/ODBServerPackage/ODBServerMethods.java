package ODBServerPackage;

import java.rmi.*;
import java.util.*;
import gemini.sp.SpItem;
import gemini.sp.SpAvTable;

public interface ODBServerMethods extends java.rmi.Remote 
{

  public void exit ()
    throws java.rmi.RemoteException;

  public String[] list(Access ac, String user)
    throws AccessException, OdbException, java.rmi.RemoteException;

  //  public SpItem fetch(Access ac, ProgKey key)
  //  throws AccessException, OdbException, java.rmi.RemoteException;
  public byte[] fetch(Access ac, ProgKey key)
    throws AccessException, OdbException, java.rmi.RemoteException;

  //  public SpItem store(Access ac, ProgKey key, SpItem prog)
  //  throws AccessException, OdbException, java.rmi.RemoteException;
  public byte[] store(Access ac, ProgKey key, byte[] inProg)
    throws AccessException, OdbException, java.rmi.RemoteException;

  //   public ProgKey storeNew(Access ac, SpItem newProg, String user, 
  // 			 ProgKeyHolder key)
  //    throws AccessException, OdbException, java.rmi.RemoteException;
  public ProgKey storeNew(Access ac, byte[] inProg, String user, 
			 ProgKeyHolder key)
    throws AccessException, OdbException, java.rmi.RemoteException;

//   public void replaceAV(Access ac, ProgKey key, String itemId,
// 			SpAvTable newTable)
//     throws AccessException, OdbException, java.rmi.RemoteException;

  public void replaceAV(Access ac, ProgKey key, String itemId,
			AvPair[] newList)
    throws AccessException, OdbException, java.rmi.RemoteException;

  public SpItem add(Access ac, ProgKey key, SpItem newItem, String toId,
		    ItemPosition posn)
    throws AccessException, OdbException, java.rmi.RemoteException;

  public void move(Access ac, ProgKey key, String fromId, String toId,
		   ItemPosition posn)
    throws AccessException, OdbException, java.rmi.RemoteException;

  public void remove(Access ac, ProgKey key, String itemId)
    throws AccessException, OdbException, java.rmi.RemoteException;

}
