package ODBServerPackage;

public final class ItemPosition {
  public static final int _CHILD = 0,
    _SIBLING = 1;
  public static final ItemPosition CHILD = new ItemPosition(_CHILD);
  public static final ItemPosition SIBLING = new ItemPosition(_SIBLING);
  public int value() {
    return _value;
  }
  public static final ItemPosition from_int(int i) {
    switch (i) {
    case _CHILD:
      return CHILD;
    case _SIBLING:
      return SIBLING;
    default:
      return null;
    }
  }
  private ItemPosition(int _value){
    this._value = _value;
  }
  private int _value;
}
