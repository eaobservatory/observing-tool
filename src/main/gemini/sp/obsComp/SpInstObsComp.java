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

package gemini.sp.obsComp;

import gemini.sp.SpAvTable;
import gemini.sp.SpObsData;
import gemini.sp.SpType;
import gemini.sp.iter.SpIterStep;

import gemini.util.Angle;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * A base class for instrument observation component items.
 *
 * One of the principal tasks of this component is to keep up-to-date the
 * position angle element of the observation data for the observation context.
 *
 * @see gemini.sp.SpObsData
 * @see gemini.sp.SpObsContextItem
 */
@SuppressWarnings("serial")
public abstract class SpInstObsComp extends SpObsComp implements
        SpInstConstants {
    Class<? extends SpInstObsComp> myself = this.getClass();
    Hashtable<String, SpInstCapability> _capabilityH =
            new Hashtable<String, SpInstCapability>();

    /**
     * Construct the SpInstObsComp with its exact subtype.
     */
    public SpInstObsComp(SpType spType) {
        super(spType);
    }

    /**
     * Add a capability to this instrument.
     */
    public void addCapability(SpInstCapability cap) {
        cap.setAvTable(_avTable);
        _capabilityH.put(cap.getName(), cap);
    }

    /**
     * Remove a capability from this instrument.
     *
     * Note this does remove any attributes associated with the capability
     * that have already been stored in the instrument's attribute/value table.
     */
    public void removeCapability(SpInstCapability cap) {
        cap.setAvTable(null);
        _capabilityH.remove(cap.getName());
    }

    /**
     * Get a capability with the given name.
     */
    public SpInstCapability getCapability(String name) {
        return _capabilityH.get(name);
    }

    /**
     * Get an enumeration of all the capability names.
     */
    public Enumeration<String> getCapabilityNames() {
        return _capabilityH.keys();
    }

    /**
     * Override clone to clone all the capabilities.
     */
    protected Object clone() {
        SpInstObsComp spClone = (SpInstObsComp) super.clone();
        SpAvTable avClone = spClone.getTable();

        // Clone each capability, setting its avTable correctly.
        Vector<SpInstCapability> v = new Vector<SpInstCapability>();
        Enumeration<SpInstCapability> caps = _capabilityH.elements();

        while (caps.hasMoreElements()) {
            SpInstCapability cap = caps.nextElement();
            v.addElement(cap.copy(avClone));
        }

        // Place each cloned capability in the cloned component's table.
        spClone._capabilityH = new Hashtable<String, SpInstCapability>();

        for (int i = 0; i < v.size(); ++i) {
            SpInstCapability cap = v.elementAt(i);
            spClone._capabilityH.put(cap.getName(), cap);
        }

        return spClone;
    }

    /**
     * Returns true if argument is instance of this class.
     *
     * This method is useful as SpInstObsComp is abstract.
     * Subclasses might want to implement their own method.
     */
    public boolean equals(SpInstObsComp that) {
        return myself.isInstance(that);
    }

    /**
     * Get the science area in arcsec x arcsec.
     *
     * For now, this must be a rectangular region.
     */
    public double[] getScienceArea() {
        return new double[]{-1.0, -1.0};
    }

    /**
     * Set the exposure time.
     */
    public final void setExposureTime(double seconds) {
        setExposureTime(Double.toString(seconds));
    }

    /**
     * Set the exposure time.
     */
    public final void setExposureTime(String seconds) {
        if (seconds != null && !seconds.trim().equals("")
                && seconds.matches("\\d*\\.?\\d*")) {
            _avTable.set(ATTR_EXPOSURE_TIME, new Double(seconds).toString());
        }
    }

    /**
     * Get the exposure time.
     */
    public final double getExposureTime() {
        String time;

        // For old programs, change "integrationTime" to "exposureTime".
        time = _avTable.get("integrationTime");

        if (time != null) {
            setExposureTime(time);
            _avTable.rm("integrationTime");

        } else {
            time = _avTable.get(ATTR_EXPOSURE_TIME);
        }

        if (time == null) {
            time = "0";
        }

        double res = 0.0;

        try {
            res = Double.valueOf(time);
        } catch (Exception ex) {
        }

        return res;
    }

    /**
     * Get the exposure time as a string.
     */
    public final String getExposureTimeAsString() {
        // For old programs, change "integrationTime" to "exposureTime".
        String intTime = _avTable.get("integrationTime");

        if (intTime != null) {
            setExposureTime(intTime);
            _avTable.rm("integrationTime");

            return intTime;
        }

        String res = _avTable.get(ATTR_EXPOSURE_TIME);
        if (res == null) {
            return "0";
        }

        return res;
    }

    public int getCoadds() {
        String coadds;
        int res = 1;

        coadds = _avTable.get(ATTR_COADDS);

        if (coadds != null) {
            try {
                res = Integer.valueOf(coadds);
            } catch (Exception x) {
            }
        }

        return res;
    }

    /**
     * Set the position angle in degrees from due north, updating the
     * observation data with the new position angle.
     *
     * This method is ultimately called by the other setPosAngle methods.
     * Made not final by AB for ORAC. 4-May-2000, to allow overriding.
     */
    public void setPosAngleDegrees(double posAngle) {
        _avTable.set(ATTR_POS_ANGLE, posAngle);
        _updateObsData(posAngle);
    }

    /**
     * Set the position angle in radians from due north.
     */
    public void setPosAngleRadians(double posAngle) {
        setPosAngleDegrees(Angle.radiansToDegrees(posAngle));
    }

    /**
     * Set the rotation of the science area as a string (representing degrees).
     */
    public void setPosAngleDegreesStr(String posAngleStr) {
        double posAngle = 0.0;

        try {
            posAngle = Double.valueOf(posAngleStr);
        } catch (Exception ex) {
        }

        _avTable.set(ATTR_POS_ANGLE, posAngleStr);
        _updateObsData(posAngle);
    }

    /**
     * Add the given angle to the current rotation angle.
     */
    public final void addPosAngleDegrees(double addAngle) {
        double angle = getPosAngleDegrees();
        angle += addAngle;
        setPosAngleDegrees(angle);
    }

    /**
     * Add the given angle to the current rotation angle.
     */
    public final void addPosAngleRadians(double addAngle) {
        double angle = getPosAngleRadians();
        angle += addAngle;
        setPosAngleRadians(angle);
    }

    /**
     * Get the rotation of the science area (in radians) from due north.
     */
    public final double getPosAngleDegrees() {
        // For old programs, change "rotAngle" to "posAngle".
        String posAngleStr = _avTable.get("rotAngle");

        if (posAngleStr != null) {
            setPosAngleDegreesStr(posAngleStr);
            _avTable.rm("rotAngle");
        }

        return _avTable.getDouble(ATTR_POS_ANGLE, 0.0);
    }

    /**
     * Get the rotation of the science area (in radians) from due north.
     */
    public final double getPosAngleRadians() {
        return Angle.degreesToRadians(getPosAngleDegrees());
    }

    /**
     * Get the rotation of the science area as a string (in radians) from due
     * north.
     */
    public final String getPosAngleRadiansStr() {
        double val = getPosAngleRadians();
        return Double.toString(val);
    }

    /**
     * Get the rotation of the science area as a string (in degrees) from due
     * north.
     */
    public final String getPosAngleDegreesStr() {
        double val = getPosAngleDegrees();
        return Double.toString(val);
    }

    /**
     * Update the SpObsData position angle information.
     */
    private void _updateObsData(double posAngle) {
        SpObsData od = getObsData();

        if (od != null) {
            od.setPosAngle(posAngle);
        }
    }

    /**
     * Override setTable to update the position angle.
     */
    protected void setTable(SpAvTable avTable) {
        Enumeration<SpInstCapability> caps = _capabilityH.elements();

        while (caps.hasMoreElements()) {
            SpInstCapability cap = caps.nextElement();
            cap.setAvTable(avTable);
        }

        super.setTable(avTable);

        _updateObsData(getPosAngleDegrees());
    }

    /**
     * Override replaceTable to update the position angle.
     */
    protected void replaceTable(SpAvTable avTable) {
        Enumeration<SpInstCapability> caps = _capabilityH.elements();

        while (caps.hasMoreElements()) {
            SpInstCapability cap = caps.nextElement();
            cap.setAvTable(avTable);
        }

        super.replaceTable(avTable);

        _updateObsData(getPosAngleDegrees());
    }

    /**
     * Overhead time for doing an exposure.
     *
     * Overhead time associated with an exposure (see {@link #getExposureTime()}
     * and {@link #setExposureTime(double)}). cause by reset time, read time,
     * NDR reset delay etc.
     * <P>
     * This method is used for MSB duration estimation. Subclasses can override
     * this method to return an instrument specific value.
     */
    public double getExposureOverhead() {
        return 0.0;
    }

    /**
     * Returns a time estimate for slewing the telescope depending on the
     * instrument settings.
     *
     * Should overriden by subclasses.
     */
    public double getSlewTime() {
        return 0.0;
    }

    /**
     * returns time estimate in seconds for acquiring the source.
     *
     * Should be overridden by subclasses.
     */
    public double getAcqTime() {
        return 0.0;
    }

    public Hashtable<String, String> getConfigItems() {
        return new Hashtable<String, String>();
    }

    /**
     * This is a helper class for time estimation.
     *
     * Subclasses of SpInstObsComp can implement their own inner subclasses of
     * the IterationTracker class. These iteration tracker classes are used to
     * keep track of the instrument specific parameters over which instrument
     * iterators iterate. The instrument component itself (outer class, subclass
     * of SpInstObsComp) does not hold any knowledge of what the current value
     * of, say, exposure time during iteration. But the might be instrument
     * specific functionality implemented in the instrument component (outer
     * class, subclass of SpInstObsComp) that can be used by the
     * IterationTracker to calculate the estimated elapsed time based on the
     * current values of the parameters that are being iterated over. That is
     * way it is useful to have IterationTracker as an inner class of
     * SpInstObsComp.
     */
    public class IterationTracker {
        public IterationTracker() {
        }

        public void update(SpIterStep step) {
        }

        public double getObserveStepTime() {
            return 0.0;
        }
    }

    /**
     * Create an instrument specific instance of an IterationTracker.
     */
    public IterationTracker createIterationTracker() {
        return new IterationTracker();
    }

    public boolean canUpdatePosAngle() {
        return true;
    }
}
