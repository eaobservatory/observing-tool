/*
 * AttributeOrigin.java
 *
 * Created on 05 March 2002, 10:24
 * by David Clarke
 */

package om.sciProgram;

import gemini.sp.*;
import gemini.sp.iter.*;

/**
 * An AttributeOrigin represents where a particular AVPair or
 * AIVTriplet came from. It consists of the following fields:
 *    SpItem item - the item in the observation sequence from
 *                  which the attibute came
 *    String name - the name of the attibute in the SpItem (which
 *                  might be different from the attribute in the
 *                  n-tuple due to vector expansion and/or *Iter
 *                  suffix handling.
 *    int index   - SpItem's attributes have vector values, this
 *                  is the index into that vector that the n-tuple
 *                  got its value from.
 *
 * @author David Clarke (dac)
 **/
public class AttributeOrigin {

  private SpItem _item;
  private String _name;
  private int    _index;

  /** Basic constructor */
  public AttributeOrigin(SpItem item, String name, int index) {
    _item  = item;
    _name  = name;
    _index = index;
  }

  /** Empty constructor */
  //  public AttributeOrigin() {
  //  }

  /** Return the item */
  public SpItem item() {
    return _item;
  }

  /** Return the name */
  public String name() {
    return _name;
  }

  /** Return the index */
  public int index() {
    return _index;
  }

  /** Set the value of the originating attribute */
  public void setValue(String value) {
    item().getTable().set(name(), value, index());    // Yukk
  }

  /** Convert to a string */
  public String toString() {
    return _item.getTitle() + ":" + _name + "[" + _index + "]";
  }
}
    
  
