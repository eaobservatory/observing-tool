package ODBServerPackage;

import java.rmi.RemoteException;

public final class AccessException
  extends java.rmi.RemoteException {
  //	instance variables
  public String info;
  //	constructors
  public AccessException() {
    super();
  }
  public AccessException(String __info) {
    super();
    info = __info;
  }
}
