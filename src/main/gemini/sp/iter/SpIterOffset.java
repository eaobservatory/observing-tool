/*
 * Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package gemini.sp.iter;

import gemini.sp.SpAvTable;
import gemini.sp.SpOffsetPos;
import gemini.sp.SpOffsetPosList;
import gemini.sp.SpType;
import gemini.sp.SpItem;
import gemini.sp.SpPosAngleObserver;
import gemini.sp.SpObsData;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;

import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;

import gemini.util.MathUtil;
import gemini.util.TelescopePos;

import java.util.Enumeration;
import java.util.Vector;

/**
 * The Enumeration of the steps produced by the Offset iterator.
 */
@SuppressWarnings("serial")
class SpIterOffsetEnumeration extends SpIterEnumeration {

    private TelescopePos[] _positions;

    private int _curIndex = 0;

    SpIterOffsetEnumeration(SpIterOffset iterComp) {
        super(iterComp);

        _positions = iterComp.getPosList().getAllPositions();
    }

    protected boolean _thisHasMoreElements() {
        return (_curIndex < _positions.length);
    }

    protected SpIterStep _thisFirstElement() {
        return _thisNextElement();
    }

    protected SpIterStep _thisNextElement() {
        SpOffsetPos op = (SpOffsetPos) _positions[_curIndex];

        SpIterValue[] sivA = {
                new SpIterValue("p", String.valueOf(op.getXaxis())),
                new SpIterValue("q", String.valueOf(op.getYaxis()))};

        return new SpIterStep(((SpIterOffset) _iterComp).title_offset(),
                _curIndex++, _iterComp, sivA);
    }

}

/**
 * An iterator for telescope offset positions.
 *
 * It maintains a position list that details the sequence of offset positions
 * and implements the elements() method to Enumerate them.
 *
 * @see SpOffsetPosList
 */
@SuppressWarnings("serial")
public class SpIterOffset extends SpIterComp implements SpTranslatable {
    /**
     * Data structure for maintaining position angle and SpPosAngleObservers.
     *
     * Note that this is <b>not</b> the SpObsData object that is returned by
     * getObsData().
     */
    private SpObsData _posAngleObsData = new SpObsData();

    /**
     * The position list uses the attributes and values contained in this
     * SpItem's attribute table to construct and maintain a list of offset
     * positions.
     */
    protected SpOffsetPosList _posList;

    /** Needed for XML parsing. */
    private double xOffNew = 0.0;

    /** Needed for XML parsing. */
    private double yOffNew = 0.0;

    /** Number of named sky children associated. */
    private boolean _hasNamedSkyChild = false;

    /**
     * Default constructor.
     */
    public SpIterOffset() {
        super(SpType.ITERATOR_COMPONENT_OFFSET);
    }

    protected SpIterOffset(SpType spType) {
        super(spType);
    }

    /**
     * Create and return the position list data structure for the offset
     * positions in the attribute table.
     *
     * Modified by AB, 3-Aug-00 to <b>ALWAYS</b> create a new list. This solved
     * a bug whereby if an offset iterator list was copied <b>after</b> being
     * edited and then pasted, the new pasted version would <b>retain</b> the
     * old spOffSetList, thus edits would go to both. Note that this did
     * <b>not</b> occur if the original was not edited first. It is the act of
     * editing that invokes this method and caused the problem.
     *
     * @see #getCurrentPosList()
     */
    public SpOffsetPosList getPosList() {
        // See comment above
        _posList = new SpOffsetPosList(_avTable);

        if (_posList.size() == 0) {
            _posList.createPosition(0.0, 0.0);
        }

        return _posList;
    }

    /**
     * Get the current position list data structure for the offset positions in
     * the attribute table, creating it <b>only</b> if necessary.
     *
     * This method is used by the Telescope Position Editor (TPE). Using
     * {@link #getPosList()} in the TPE would break the link between the offset
     * iterator and the TPE so that updating one from the other would no longer
     * work.
     *
     * @see #getPosList()
     */
    public SpOffsetPosList getCurrentPosList() {
        if (_posList == null) {
            _posList = new SpOffsetPosList(_avTable);
        }

        return _posList;
    }

    /** Get the Position Angle. */
    public double getPosAngle() {
        return getCurrentPosList().getPosAngle();
    }

    /** Set the Position Angle as double. */
    public void setPosAngle(double pa) {
        getCurrentPosList().setPosAngle(pa);

        _posAngleObsData.setPosAngle(pa);

        // SdW - Look for children which implement implement SpPosAngleObserver
        Enumeration<SpItem> e = children();

        while (e.hasMoreElements()) {
            SpItem child = e.nextElement();
            Class<?>[] interfaces = child.getClass().getInterfaces();

            for (int i = 0; i < interfaces.length; i++) {
                if (interfaces[i].getName().indexOf("SpPosAngleObserver")
                        != -1) {
                    ((SpPosAngleObserver) child).posAngleUpdate(pa);
                }
            }
        }
    }

    /**
     * Set the Position Angle as String.
     */
    public void setPosAngle(String pa) {
        double angle = 0.0;

        try {
            angle = Double.parseDouble(pa);
        } catch (Exception e) {
        }

        setPosAngle(angle);
    }

    /**
     * Add a position angle observer.
     */
    public void addPosAngleObserver(SpPosAngleObserver pao) {
        _posAngleObsData.addPosAngleObserver(pao);
    }

    /**
     * Remove a position angle observer.
     */
    public void deletePosAngleObserver(SpPosAngleObserver pao) {
        _posAngleObsData.deletePosAngleObserver(pao);
    }

    /**
     * Connects SpPosAngleObservers if there are any amoung the new children.
     */
    protected void insert(SpItem[] newChildren, SpItem afterChild) {
        // Check for SpPosAngleObservers amoung the newChildren and add them
        // as SpPosAngleObservers.
        for (int i = 0; i < newChildren.length; i++) {
            if (newChildren[i] instanceof SpPosAngleObserver) {
                addPosAngleObserver((SpPosAngleObserver) newChildren[i]);
            }
        }

        super.insert(newChildren, afterChild);
    }

    /**
     * Override setTable() to make sure that the offset position list is in
     * sync with the current attributes and values.
     */
    protected void setTable(SpAvTable avTab) {
        super.setTable(avTab);

        if (_posList != null) {
            _posList.setTable(avTab);
        }
    }

    /**
     * Override replaceTable() to make sure that the offset position list is in
     * sync with the current attributes and values.
     */
    protected void replaceTable(SpAvTable avTab) {
        super.replaceTable(avTab);

        if (_posList != null) {
            _posList.setTable(avTab);
        }
    }

    /**
     * Enumerate the steps of the offset iterator.
     */
    public SpIterEnumeration elements() {
        return new SpIterOffsetEnumeration(this);
    }

    /**
     * Returns true if one or more direct children of this offset iterator are
     * offset iterators themselves.
     *
     * Such a nested offset iterator inside this offset inside can be either a
     * SpIterOffset or a SpIterMicroStep (subclass of SpIterOffset).
     *
     * @see #containsMicroSteps()
     */
    public boolean containsNestedOffsets() {
        Enumeration<SpItem> e = children();

        while (e.hasMoreElements()) {
            if (e.nextElement() instanceof SpIterOffset) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if one or more direct children of this offset iterator are
     * microstep iterators.
     *
     * @see #containsNestedOffsets()
     */
    public boolean containsMicroSteps() {
        Enumeration<SpItem> e = children();

        while (e.hasMoreElements()) {
            if (e.nextElement() instanceof SpIterMicroStep) {
                return true;
            }
        }

        return false;
    }

    protected void processAvAttribute(String avAttr, String indent,
            StringBuffer xmlBuffer) {
        /*
         * "offsetPositions" (SpOffsetPosList.OFFSET_POS_LIST) is an AV
         * attribute that occurs once in a SpIterOffset's AV table
         *
         * When processAvAttribute is called with "offsetPositions" as avAttr
         * then append the entire TCS XML representation of this item to the
         * xmlBuffer.
         *
         * For all other calls to processAvAttribute ignore the AV attributes
         * "PA" (SpOffsetPosList.ATTR_POS_ANGLE) and those attributes starting
         * with "Offset" (SpOffsetPos.OFFSET_TAG) as their information has
         * already been dealt with in the TCS XML representation of this item.
         *
         * The other attributes are delegated to the super class.
         */

        // Ignore any sky offsets
        if (avAttr.equals(SpOffsetPosList.SKY_POS_LIST)) {
            return;
        }

        if (avAttr.equals(SpOffsetPosList.GUIDE_POS_LIST)) {
            return;
        }

        if (avAttr.equals(SpOffsetPosList.OFFSET_POS_LIST)) {
            // Append <obsArea> element.
            xmlBuffer.append("\n" + indent + "  <obsArea>");
            xmlBuffer.append("\n" + indent
                    + "    <PA>" + getPosAngle() + "</PA>");

            for (int i = 0; i < _posList.size(); i++) {
                xmlBuffer.append("\n" + indent + "    <OFFSET>");
                xmlBuffer.append("\n" + indent + "      <DC1>"
                        + _posList.getPositionAt(i).getXaxis() + "</DC1>");
                xmlBuffer.append("\n" + indent + "      <DC2>"
                        + _posList.getPositionAt(i).getYaxis() + "</DC2>");
                xmlBuffer.append("\n" + indent + "    </OFFSET>");
            }

            xmlBuffer.append("\n" + indent + "  </obsArea>");

            return;
        }

        if (avAttr.equals(SpOffsetPosList.ATTR_POS_ANGLE)
                || avAttr.startsWith(SpOffsetPos.OFFSET_TAG)
                || avAttr.startsWith(SpOffsetPos.SKY_TAG)
                || avAttr.startsWith(SpOffsetPos.GUIDE_TAG)) {
            // Ignore. Dealt with in <obsArea> element (see above).
            return;
        }

        super.processAvAttribute(avAttr, indent, xmlBuffer);
    }

    public void processXmlElementContent(String name, String value) {
        if (!name.equals("obsArea") && !name.equals("OFFSET")) {
            if (name.equals("PA")) {
                // Added by SdW - allows posAngle in target attribute to
                // override that supplied if it exists...
                SpInstObsComp inst = SpTreeMan.findInstrument(this);

                if (inst != null && inst.getPosAngleDegrees() != 0.0) {
                    value = inst.getPosAngleDegreesStr();
                }

                setPosAngle(value);

            } else if (name.equals("DC1")) {
                xOffNew = 0.0;

                try {
                    xOffNew = Double.parseDouble(value);
                } catch (Exception e) {
                }

            } else if (name.equals("DC2")) {
                yOffNew = 0.0;

                try {
                    yOffNew = Double.parseDouble(value);
                } catch (Exception e) {
                }

            } else {
                super.processXmlElementContent(name, value);
            }
        }
    }

    public void processXmlElementEnd(String name) {
        if (name.equals("OFFSET")) {
            _posList.createPosition(xOffNew, yOffNew);

            xOffNew = 0.0;
            yOffNew = 0.0;

        } else if (name.equals("obsArea")) {
            // save() just means reset() in this context.
            getAvEditFSM().save();

        } else {
            super.processXmlElementEnd(name);
        }
    }

    public String title_offset() {
        return "offset";
    }

    public void setNamedSkyChild(boolean exists) {
        _hasNamedSkyChild = true;
    }

    public boolean hasNamedSkyChild() {
        return _hasNamedSkyChild;
    }

    public int getNumIterObserveChildren(SpItem item) {
        int n = 0;

        Enumeration<SpItem> e = item.children();

        while (e.hasMoreElements()) {
            SpItem child = e.nextElement();

            if (child instanceof SpIterFolder) {
                n += getNumIterObserveChildren(child);
            } else if (child instanceof SpIterObserveBase) {
                n++;
            }
        }

        return n;
    }

    public void translateProlog(Vector<String> sequence)
            throws SpTranslationNotSupportedException {
    }

    public void translateEpilog(Vector<String> sequence)
            throws SpTranslationNotSupportedException {
    }

    public void translate(Vector<String> v)
            throws SpTranslationNotSupportedException {
        // If this has a microstep iterator child, we will delegate to it and
        // not put offsets here
        Enumeration<SpItem> children = this.children();
        String instName = SpTreeMan.findInstrument(this).getTitle();

        boolean hasMicrostepChild = false;

        while (children.hasMoreElements()) {
            if (children.nextElement() instanceof SpIterMicroStep) {
                hasMicrostepChild = true;
                break;
            }
        }

        SpTranslatable translatable = null;
        SpTranslatable previous = null;

        if (hasMicrostepChild) {
            children = this.children();

            while (children.hasMoreElements()) {
                SpItem child = children.nextElement();

                if (child instanceof SpTranslatable) {
                    translatable = (SpTranslatable) child;

                    if (!translatable.equals(previous)) {
                        if (previous != null) {
                            previous.translateEpilog(v);
                        }

                        previous = translatable;
                        translatable.translateProlog(v);
                    }

                    translatable.translate(v);
                }
            }

        } else {
            for (int i = 0; i < _posList.size(); i++) {
                if ("WFCAM".equalsIgnoreCase(instName)) {
                    // Add CASU pipeline headers
                    v.add("title jitter " + (i + 1));
                    v.add("-setHeader NJITTER " + _posList.size());
                    v.add("-setHeader JITTER_I " + (i + 1));
                    v.add("-setHeader JITTER_X "
                            + _posList.getPositionAt(i).getXaxis());
                    v.add("-setHeader JITTER_Y "
                            + _posList.getPositionAt(i).getYaxis());
                    v.add("-setHeader NUSTEP 1");
                    v.add("-setHeader USTEP_I 1");
                    v.add("-setHeader USTEP_X 0.0");
                    v.add("-setHeader USTEP_Y 0.0");
                }

                double xAxis = MathUtil.round(_posList.getPositionAt(i)
                        .getXaxis(), 3);
                double yAxis = MathUtil.round(_posList.getPositionAt(i)
                        .getYaxis(), 3);

                String instruction = "offset " + xAxis + " " + yAxis;
                v.add(instruction);

                if ("Michelle".equalsIgnoreCase(instName)) {
                    v.add("-WAIT ALL");
                }

                children = this.children();

                while (children.hasMoreElements()) {
                    SpItem child = children.nextElement();

                    if (child instanceof SpTranslatable) {
                        translatable = (SpTranslatable) child;

                        if (!translatable.equals(previous)) {
                            if (previous != null) {
                                previous.translateEpilog(v);
                            }

                            previous = translatable;
                            translatable.translateProlog(v);
                        }

                        translatable.translate(v);
                    }
                }
            }
        }

        if (translatable != null) {
            translatable.translateEpilog(v);
        }
    }
}
