
package ODBServerPackage;

import java.io.*;

public final class Access implements java.io.Serializable {
  //	instance variables
  public String password;
  public ODBServerPackage.Database db;
  public String onlineCapability;

  //	constructors
  public Access() { }

  public Access(String __password, ODBServerPackage.Database __db,
		String __onlineCapability) {

    password = __password;
    db = __db;
    onlineCapability = __onlineCapability;
  }

}
