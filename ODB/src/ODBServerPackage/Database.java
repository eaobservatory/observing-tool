package ODBServerPackage;

import java.io.*;

public final class Database implements java.io.Serializable {
  public static final int _PHASEII = 0,
    _ACTIVE = 1;
  public static final Database PHASEII = new Database(_PHASEII);
  public static final Database ACTIVE = new Database(_ACTIVE);
  public int value() {
    return _value;
  }
  public static final Database from_int(int i) {
    switch (i) {
    case _PHASEII:
      return PHASEII;
    case _ACTIVE:
      return ACTIVE;
    default:
      return PHASEII;
    }
  }
  private Database(int _value){
    this._value = _value;
  }
  private int _value;
}
