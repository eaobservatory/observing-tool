package ODBServerPackage;

import java.rmi.RemoteException;

public final class OdbException extends java.rmi.RemoteException {
  //	instance variables
  public String info;
  //	constructors
  public OdbException() {
    super();
  }
  public OdbException(String __info) {
    super();
    info = __info;
  }
}
