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


/**
 * This class defines the Enumeration for SpIterChop.
 */
class SpIterChopEnumeration extends SpIterEnumeration
{
   private SpIterChop _iterChop;

   private int    _totalSteps;
   private int    _curStep = 0;

SpIterChopEnumeration(SpIterChop iterChop)
{
   super(iterChop);

   _iterChop   = iterChop;
   _totalSteps = iterChop.getStepCount();
}

protected boolean
_thisHasMoreElements()
{
   return (_curStep < _totalSteps);
}

// Trim the "Iter" off the end of an iteration item attribute name.
private String
_trimIter(String attribute)
{
   if (attribute.endsWith("Iter")) {
      int endIndex = attribute.length() - 4;
      return attribute.substring(0, endIndex);
   }
   return attribute;
}

// Get the chop step corresponding with the given step.
private SpIterValue[]
_getChopValues(int stepIndex)
{
   String attr, val;

   SpIterValue[] values = new SpIterValue[3];

   values[0] = new SpIterValue(_trimIter(SpIterChop.ATTR_THROW),       _iterChop.getThrowAsString(stepIndex));
   values[1] = new SpIterValue(_trimIter(SpIterChop.ATTR_ANGLE),       _iterChop.getAngleAsString(stepIndex));
   values[2] = new SpIterValue(_trimIter(SpIterChop.ATTR_COORD_FRAME), _iterChop.getCoordFrame(stepIndex));

   return values;
}

protected SpIterStep
_thisFirstElement()
{
   return _thisNextElement();
}

protected SpIterStep
_thisNextElement()
{
   SpIterValue[] values = _getChopValues(_curStep);
   return new SpIterStep("chop", _curStep++, _iterComp, values);
}
 
}


/**
 * Chop Iterator.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk, based on gemini/sp/iter/SpIterConfigBase.java)
 */
public class SpIterChop extends SpIterComp {

  /**
   * Records a vector of chop throws.
   *
   * Each vector element represents one chop iterator step.
   */
  public static String ATTR_THROW       = "throw";

  /**
   * Records a vector of chop angles.
   *
   * Each vector element represents one chop iterator step.
   */
  public static String ATTR_ANGLE       = "angle";

  /**
   * Records a vector of chop coordinates frames.
   *
   * Each vector element represents one chop iterator step.
   */
  public static String ATTR_COORD_FRAME = "coordFrame";

  /**
   * Index of step that is currently selected in the chop iterator editor.
   *
   * This index is needed for the painting of the chop feature in the telescope position editor.
   * It is not saved to the _avTable as it is not needed for the XML representation of this iterator.
   * And a change of the selected step should not result in this item being marked as edited.
   */
  private int _selectedIndex = -1;

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
    return new SpIterChopEnumeration(this);
  }


  public double getThrow(int step) {
    return _avTable.getDouble(ATTR_THROW, step, 0.0);
  }

  public String getThrowAsString(int step) {
    return _avTable.get(ATTR_THROW, step);
  }
  
  public void setThrow(String throwValue, int step) {
    _avTable.set(ATTR_THROW, throwValue, step);
  }

  public void setThrow(double throwValue, int step) {
    _avTable.set(ATTR_THROW, throwValue, step);
  }


  public double getAngle(int step) {
    return _avTable.getDouble(ATTR_ANGLE, step, 0.0);
  }

  public String getAngleAsString(int step) {
    return _avTable.get(ATTR_ANGLE, step);
  }
  
  public void setAngle(String angle, int step) {
    _avTable.set(ATTR_ANGLE, angle, step);
  }

  public void setAngle(double angle, int step) {
    _avTable.set(ATTR_ANGLE, angle, step);
  }


  public String getCoordFrame(int step) {
    return _avTable.get(ATTR_COORD_FRAME, step);
  }

  public void setCoordFrame(String coordFrame, int step) {
    _avTable.set(ATTR_COORD_FRAME, coordFrame, step);
  }

  public int getSelectedIndex() {
    return _selectedIndex;
  }

  public void setSelectedIndex(int selectedIndex) {
    _selectedIndex = selectedIndex;
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
    Vector [] result = new Vector[getStepCount()];

    for(int i = 0; i < getStepCount(); i++) {
      result[i] = getStep(i);
    }
    
    return result;
  }

  public int getStepCount() {
    try {
      return _avTable.getAll(ATTR_THROW).size();
    }
    catch(Exception e) {
      return 0;
    }
  }
}

