/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2000                   */
/*                                                              */
/*==============================================================*/
// $Id$

package gemini.sp.iter;

import gemini.sp.SpType;
import gemini.sp.SpFactory;

import java.util.Vector;

public class SpIterChop extends SpIterComp {

  /**
   * Prefix for chop attributes.
   *
   * <pre>
   * Use as follows:
   *
   * ATTR_COORD_FRAME + "#" + i + "." + ATTR_THROW
   * ATTR_COORD_FRAME + "#" + i + "." + ATTR_ANGLE
   * ATTR_COORD_FRAME + "#" + i + "." + ATTR_COORD_FRAME
   *
   * where i is the number of the chop iterator step.
   * </pre>
   *
   * This notation will be tranlated to XML with one &lt;chop&gt; element for each
   * chop iterater step where each &lt;chop&gt; contains the elements
   * &lt;throw&gt;, &lt;angle&gt; and &lt;coordFrame&gt;.
   */
  public static String ATTR_PREFIX_CHOP = "chop";

  /** @see #ATTR_PREFIX_CHOP */
  public static String ATTR_THROW       = "throw";

  /** @see #ATTR_PREFIX_CHOP */
  public static String ATTR_ANGLE       = "angle";

  /** @see #ATTR_PREFIX_CHOP */
  public static String ATTR_COORD_FRAME = "coordFrame";


  public static final SpType SP_TYPE =
    SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "chop", "Chop");

  // Register the prototype.
  static {
    SpFactory.registerPrototype(new SpIterChop());
  }


  public SpIterChop() {
    super(SP_TYPE);
  }

  public SpIterEnumeration elements() {
    return null;
  }


  public double getThrow(int step) {
    return _avTable.getDouble(ATTR_PREFIX_CHOP + "#" + step + "." + ATTR_THROW, 0.0);
  }

  public String getThrowAsString(int step) {
    return _avTable.get(ATTR_PREFIX_CHOP + "#" + step + "." + ATTR_THROW);
  }
  
  public void setThrow(String throwValue, int step) {
    _avTable.set(ATTR_PREFIX_CHOP + "#" + step + "." + ATTR_THROW, throwValue);
  }

  public void setThrow(double throwValue, int step) {
    _avTable.set(ATTR_PREFIX_CHOP + "#" + step + "." + ATTR_THROW, throwValue);
  }


  public double getAngle(int step) {
    return _avTable.getDouble(ATTR_PREFIX_CHOP + "#" + step + "." + ATTR_ANGLE, 0.0);
  }

  public String getAngleAsString(int step) {
    return _avTable.get(ATTR_PREFIX_CHOP + "#" + step + "." + ATTR_ANGLE);
  }
  
  public void setAngle(String angle, int step) {
    _avTable.set(ATTR_PREFIX_CHOP + "#" + step + "." + ATTR_ANGLE, angle);
  }

  public void setAngle(double angle, int step) {
    _avTable.set(ATTR_PREFIX_CHOP + "#" + step + "." + ATTR_ANGLE, angle);
  }


  public String getCoordFrame(int step) {
    return _avTable.get(ATTR_PREFIX_CHOP + "#" + step + "." + ATTR_COORD_FRAME);
  }

  public void setCoordFrame(String coordFrame, int step) {
    _avTable.set(ATTR_PREFIX_CHOP + "#" + step + "." + ATTR_COORD_FRAME, coordFrame);
  }


  public Vector getStep(int i) {
    if(getThrowAsString(i) == null) {
      return null;
    }

    Vector result = new Vector();

    result.add(getThrowAsString(i));
    result.add(getAngleAsString(i));
    result.add(getCoordFrame(i));

    return result;
  }


  public Vector [] getAllSteps() {
    int n = 0;

    while(getThrowAsString(n) != null) {
      n++;
    }

    Vector [] result = new Vector[n];

    for(int i = 0; i < n; i++) {
      result[i] = getStep(i);
    }
    
    return result;
  }


/*  
  public void addStep() {

  }
*/
/*
  public int getNumberOfSteps() {
    int i = 0;

    while(getThrow(i)) {
      i++;
    }
  }
*/

/*
  public IterConfigItem[] getAvailableItems() {
    return new IterConfigItem[]{
      new IterConfigItem("Throw",       ATTR_THROW       + "Iter"),
      new IterConfigItem("Angle",       ATTR_ANGLE       + "Iter"),
      new IterConfigItem("Coord Frame", ATTR_COORD_FRAME + "Iter"),
    };
  }

  public String getItemName() {
    return "Chop";
  }
*/
}

