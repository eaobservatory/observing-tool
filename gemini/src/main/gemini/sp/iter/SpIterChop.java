/*
 * Copyright 2000-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package gemini.sp.iter;

import gemini.sp.SpItem;
import gemini.sp.SpType;
import gemini.sp.SpFactory;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.util.TranslationUtils;

import java.util.Enumeration;
import java.util.Vector;

/**
 * This class defines the Enumeration for SpIterChop.
 */
@SuppressWarnings("serial")
class SpIterChopEnumeration extends SpIterEnumeration {
    private SpIterChop _iterChop;

    private int _totalSteps;

    private int _curStep = 0;

    SpIterChopEnumeration(SpIterChop iterChop) {
        super(iterChop);

        _iterChop = iterChop;
        _totalSteps = iterChop.getStepCount();
    }

    protected boolean _thisHasMoreElements() {
        return (_curStep < _totalSteps);
    }

    // Trim the "Iter" off the end of an iteration item attribute name.
    private String _trimIter(String attribute) {
        if (attribute.endsWith("Iter")) {
            int endIndex = attribute.length() - 4;
            return attribute.substring(0, endIndex);
        }

        return attribute;
    }

    // Get the chop step corresponding with the given step.
    private SpIterValue[] _getChopValues(int stepIndex) {
        SpIterValue[] values = new SpIterValue[3];

        values[0] = new SpIterValue(_trimIter(SpIterChop.ATTR_THROW),
                _iterChop.getThrowAsString(stepIndex));
        values[1] = new SpIterValue(_trimIter(SpIterChop.ATTR_ANGLE),
                _iterChop.getAngleAsString(stepIndex));
        values[2] = new SpIterValue(_trimIter(SpIterChop.ATTR_COORD_FRAME),
                _iterChop.getCoordFrame(stepIndex));

        return values;
    }

    protected SpIterStep _thisFirstElement() {
        return _thisNextElement();
    }

    protected SpIterStep _thisNextElement() {
        SpIterValue[] values = _getChopValues(_curStep);
        return new SpIterStep("chop", _curStep++, _iterComp, values);
    }
}

/**
 * Chop Iterator.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk, based on
 *         gemini/sp/iter/SpIterConfigBase.java)
 */
@SuppressWarnings("serial")
public class SpIterChop extends SpIterComp implements SpTranslatable {
    /**
     * Records a vector of chop throws.
     *
     * Each vector element represents one chop iterator step.
     */
    public static String ATTR_THROW = "THROW";

    /**
     * Records a vector of chop angles.
     *
     * Each vector element represents one chop iterator step.
     */
    public static String ATTR_ANGLE = "PA";

    /**
     * Records a vector of chop coordinates frames.
     *
     * Each vector element represents one chop iterator step.
     */
    public static String ATTR_COORD_FRAME = "SYSTEM";

    private static String _defaultThrow = "0.0";

    private static String _defaultAngle = "0.0";

    private static String _defaultCoordFrame = "";

    /**
     * Index of step that is currently selected in the chop iterator editor.
     *
     * This index is needed for the painting of the chop feature in the
     * telescope position editor. It is not saved to the _avTable as it is not
     * needed for the XML representation of this iterator. And a change of the
     * selected step should not result in this item being marked as edited.
     */
    private int _selectedIndex = -1;

    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "chop", "Chop");

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

    /**
     * Set the default values for throw, angle and coordinate frame.
     */
    public static void setChopDefaults(String[] chopDefaults) {
        if (chopDefaults == null) {
            return;
        }

        if (chopDefaults.length > 0) {
            _defaultThrow = chopDefaults[0];
        }

        if (chopDefaults.length > 1) {
            _defaultAngle = chopDefaults[1];
        }

        if (chopDefaults.length > 2) {
            _defaultCoordFrame = chopDefaults[2];
        }
    }

    public static String getDefaultThrow() {
        return _defaultThrow;
    }

    public static String getDefaultAngle() {
        return _defaultAngle;
    }

    public static String getDefaultCoordFrame() {
        return _defaultCoordFrame;
    }

    /**
     * Initialises the first step using the chop defaults.
     *
     * This method does not mark the item as edited.
     */
    public void addInitialStep() {
        _avTable.noNotifySet(ATTR_THROW, _defaultThrow, 0);
        _avTable.noNotifySet(ATTR_ANGLE, _defaultAngle, 0);
        _avTable.noNotifySet(ATTR_COORD_FRAME, _defaultCoordFrame, 0);
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
        _avTable.set(ATTR_THROW, Math.rint(throwValue * 10.0) / 10.0, step);
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
        _avTable.set(ATTR_ANGLE, Math.rint(angle * 10.0) / 10.0, step);
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

    public Vector<String> getStep(int i) {
        if (getThrowAsString(i) == null) {
            return null;
        }

        Vector<String> result = new Vector<String>();

        result.add(getThrowAsString(i));
        result.add(getAngleAsString(i));
        result.add(getCoordFrame(i));

        return result;
    }

    @SuppressWarnings("unchecked")
    public Vector<String>[] getAllSteps() {
        Vector<String>[] result = new Vector[getStepCount()];

        for (int i = 0; i < getStepCount(); i++) {
            result[i] = getStep(i);
        }

        return result;
    }

    public int getStepCount() {
        int size = 0;

        try {
            size = _avTable.getAll(ATTR_THROW).size();
        } catch (Exception e) {
        }

        return size;
    }

    protected void processAvAttribute(String avAttr, String indent,
            StringBuffer xmlBuffer) {
        /*
         * ATTR_THROW, ATTR_ANGLE and ATTR_COORD_FRAME each occur once in a
         * SpIterOffset's AV table. each of them contains a Vector of values.
         * There is one entry for each iteration step in each of these Vectors.
         * When processAvAttribute is called with ATTR_THROW as avAttr then
         * append the entire TCS XML representation of this chop iterator to
         * the xmlBuffer and ignore ATTR_ANGLE and ATTR_COORD_FRAME.
         * They other attributes are delegated to the super class.
         */
        if (avAttr.equals(ATTR_THROW)) {
            // Append <SECONDARY> element.
            xmlBuffer.append("\n" + indent + "  <SECONDARY>");

            for (int i = 0; i < getStepCount(); i++) {
                xmlBuffer.append("\n" + indent
                        + "    <CHOP "
                        + ATTR_COORD_FRAME + "=\"" + getCoordFrame(i)
                        + "\">");
                xmlBuffer.append("\n" + indent +
                        "      <" + ATTR_THROW + ">" + getThrow(i)
                        + "</" + ATTR_THROW + ">");
                xmlBuffer.append("\n" + indent
                        + "      <" + ATTR_ANGLE + ">" + getAngle(i)
                        + "</" + ATTR_ANGLE + ">");
                xmlBuffer.append("\n" + indent + "    </CHOP>");
            }

            xmlBuffer.append("\n" + indent + "  </SECONDARY>");
        } else if (!avAttr.equals(ATTR_ANGLE)
                && !avAttr.startsWith(ATTR_COORD_FRAME)) {
            // Ignore. Dealt with in <SECONDARY> element (see above).
            super.processAvAttribute(avAttr, indent, xmlBuffer);
        }
    }

    public void processXmlElementContent(String name, String value) {

        if (!name.equals("SECONDARY") && !name.equals("CHOP")) {
            if (name.equals(ATTR_THROW)) {
                int n = 0;

                if (_avTable.getAll(ATTR_THROW) != null) {
                    n = _avTable.getAll(ATTR_THROW).size();
                }

                _avTable.noNotifySet(ATTR_THROW, value, n);

            } else if (name.equals(ATTR_ANGLE)) {
                int n = 0;

                if (_avTable.getAll(ATTR_ANGLE) != null) {
                    n = _avTable.getAll(ATTR_ANGLE).size();
                }

                _avTable.noNotifySet(ATTR_ANGLE, value, n);

            } else {
                super.processXmlElementContent(name, value);
            }
        }
    }

    public void processXmlAttribute(String elementName, String attributeName,
            String value) {
        // Check for coordinate frame (system) in <CHOP SYSTEM="system">
        if (elementName.equals("CHOP")
                && attributeName.equals(ATTR_COORD_FRAME)) {
            int n = 0;

            if (_avTable.getAll(ATTR_COORD_FRAME) != null) {
                n = _avTable.getAll(ATTR_COORD_FRAME).size();
            }

            _avTable.noNotifySet(ATTR_COORD_FRAME, value, n);

        } else {
            super.processXmlAttribute(elementName, attributeName, value);
        }
    }

    public void translateProlog(Vector<String> v) {
    }

    public void translateEpilog(Vector<String> v) {
        v.add("-CHOP ChopOff");
        v.add("SET_CHOPBEAM MIDDLE");
    }

    public void translate(Vector<String> v)
            throws SpTranslationNotSupportedException {
        for (int i = 0; i < getStepCount(); i++) {
            // Write chop instructions.
            v.add("-CHOP ChopOff");
            v.add("SET_CHOPTHROW " + getThrow(i));
            v.add("SET_CHOPPA " + getAngle(i));
            v.add("-DEFINE_BEAMS " + getAngle(i) + " " + getThrow(i));
            v.add("-CHOP ChopOn");
            v.add("-CHOP_EXTERNAL");
            v.add("SET_CHOPBEAM A");

            // Translate contents of the chop iterator.
            TranslationUtils.recurse(this.children(), v);
        }
    }
}
