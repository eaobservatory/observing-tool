/*
 * AIVTriplet.java
 *
 * Created on 05 March 2002, 10:24
 * by David Clarke
 */

package om.sciProgram;

import gemini.sp.*;
import gemini.sp.iter.*;

/**
 * An AIVTriplet represents an attribute-value pair within an
 * iterator. It has the following fields:
 *    String attribute - the name of the attribute in question
 *    String iterator  - the name of the iterator in which the
 *                       attribute resides
 *    String value     - a String representation of the value
 *                       of the attribute.
 *    AttributeOrigin origin - whence the attribute came
 *
 * @author David Clarke (dac)
 **/
public class AIVTriplet {

  private String          _attribute;
  private String          _iterator;
  private String          _value;
  private AttributeOrigin _origin;

  /** Basic constructor */
  public AIVTriplet(String iterator,
		    String attribute,
		    String value,
		    AttributeOrigin origin) {
    _attribute = attribute;
    _iterator  = iterator;
    _value     = value;
    _origin    = origin;
  }

  /** Basic constructor */
  public AIVTriplet(String iterator,
		    String attribute,
		    String value,
		    SpItem item,
		    String name,
		    int    index) {
    _attribute = attribute;
    _iterator  = iterator;
    _value     = value;
    _origin    = new AttributeOrigin(item, name, index);
  }

  /** Empty constructor */
  public AIVTriplet() {
  }

  /** Return the iterator */
  public String iterator() {
    return _iterator;
  }

  /** Return the attribute */
  public String attribute() {
    return _attribute;
  }

  /** Return the value */
  public String value() {
    return _value;
  }

  /** Return the origin */
  public AttributeOrigin origin() {
    return _origin;
  }

  /** String representation */
  public String toString() {
    return "(" + iterator() + ":" + attribute() + " = " + value() + ")";
  }
  //  /** Set the iterator */
  //  public void setIterator(String iterator) {
  //    setFirst(iterator);
  //  }
  //
  //  /** Set the attribute */
  //  public void setAttribute(String attribute) {
  //    setSecond(attribute);
  //  }
  //
  //  /** Set the value */
  //  public void setValue(String value) {
  //    setThird(value);
  //  }

}
    
  
