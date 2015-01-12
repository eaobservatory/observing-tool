/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
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

package orac.jcmt.iter;

import gemini.sp.SpType;

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import gemini.util.Format;

import orac.jcmt.SpJCMTConstants;

/**
 * Enumerater for the elements of the JCMT Observe iterator.
 *
 * Note that the iteration step enumeration for JCMT Observe Iterators always
 * contains exactly 1 iteration step. In the JCMT OT Observe iterators do not
 * have an "Observe X times" widget. The number of integrations is changed
 * instead. But that does not result in seperate SpIterSteps.
 */
@SuppressWarnings("serial")
class SpIterJCMTObsEnumeration extends SpIterEnumeration implements
        SpJCMTConstants {
    private int _curCount = 0;
    private int _maxCount = 1;
    private SpIterValue[] _values;

    SpIterJCMTObsEnumeration(SpIterJCMTObs iterObserve) {
        super(iterObserve);

        // max count is always 1 for JCMT! It can't be changed
        // In the JCMT OT the number of integrations is changed instead.
        // But that does not result in seperate SpIterSteps.
        _maxCount = 1;
    }

    protected boolean _thisHasMoreElements() {
        return (_curCount < _maxCount);
    }

    protected SpIterStep _thisFirstElement() {
        SpIterJCMTObs ibo = (SpIterJCMTObs) _iterComp;

        _values = new SpIterValue[]{new SpIterValue(ATTR_ELAPSED_TIME,
                String.valueOf(ibo.getElapsedTime()))};

        return _thisNextElement();
    }

    protected SpIterStep _thisNextElement() {
        return new SpIterStep("jcmtObs", _curCount++, _iterComp, _values);
    }

}

/**
 * Base class for JCMT Observe Iterators.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SpIterJCMTObs extends SpIterObserveBase
        implements SpJCMTConstants {
    /**
     * Default constructor.
     */
    public SpIterJCMTObs(SpType spType) {
        super(spType);

        /*
         * JCMT Observe "iterators" are not repeated.
         * The number that can be specified on each JCMT observe iterator
         * and that is appended to the name of the iterator in the Science
         * Program tree is the number of iterations, ATTR_INTEGRATIONS.
         */
        _avTable.noNotifyRm(ATTR_COUNT);
    }

    /**
     * Calculates the estimated duration of this Observe ("Eye").
     *
     * Note that the returned duration takes into account the number of
     * integrations and overheads. So the duration does <b>not</b> have to be
     * multiplied by the number of integrations.
     */
    public double getElapsedTime() {
        return 0.0;
    }

    /**
     * Override getTitle to return the observe count.
     */
    public String getTitle() {
        return type().getReadable();
    }

    /** Not supported by JCMT OT. */
    public void setCount() {
        throw new UnsupportedOperationException(
                "public SpIterEnumeration SpIterObserveBase.setCount()"
                + " not supported by JCMT OT.");
    }

    /** Not supported by JCMT OT. */
    public SpIterEnumeration elements() {
        return new SpIterJCMTObsEnumeration(this);
    }

    /**
     *
     */
    public String getSwitchingMode() {
        return _avTable.get(ATTR_SWITCHING_MODE);
    }

    /**
     *
     */
    public void setSwitchingMode(String switchingMode) {
        _avTable.set(ATTR_SWITCHING_MODE, switchingMode);
    }

    public void rmSwitchingMode() {
        _avTable.noNotifyRm(ATTR_SWITCHING_MODE);
    }

    /**
     *
     */
    public boolean getDoAtCurrentAz() {
        return _avTable.getBool(ATTR_DO_AT_CURRENT_AZ);
    }

    /**
     *
     */
    public void setDoAtCurrentAz(boolean x) {
        _avTable.set(ATTR_DO_AT_CURRENT_AZ, x);
    }

    /**
     *
     */
    public double getReferenceOffsetX() {
        return _avTable.getDouble(ATTR_REFERENCE_OFFSET_X, 0.0);
    }

    /**
     *
     */
    public void setReferenceOffsetX(String value) {
        _avTable.set(ATTR_REFERENCE_OFFSET_X, Format.toDouble(value));
    }

    /**
     *
     */
    public double getReferenceOffsetY() {
        return _avTable.getDouble(ATTR_REFERENCE_OFFSET_Y, 0.0);
    }

    /**
     *
     */
    public void setReferenceOffsetY(String value) {
        _avTable.set(ATTR_REFERENCE_OFFSET_Y, Format.toDouble(value));
    }

    /**
     *
     */
    public String getReferenceOffsetSystem() {
        return _avTable.get(ATTR_REFERENCE_OFFSET_SYSTEM);
    }

    /**
     *
     */
    public void setReferenceOffsetSystem(String value) {
        _avTable.set(ATTR_REFERENCE_OFFSET_SYSTEM, value);
    }

    /**
     *
     */
    public double getFrequencyOffsetThrow() {
        return _avTable.getDouble(ATTR_FREQUENCY_OFFSET_THROW, 0.0);
    }

    /**
     *
     */
    public void setFrequencyOffsetThrow(String value) {
        _avTable.set(ATTR_FREQUENCY_OFFSET_THROW, Format.toDouble(value));
    }

    /**
     *
     */
    public double getFrequencyOffsetRate() {
        return _avTable.getDouble(ATTR_FREQUENCY_OFFSET_RATE, 0.0);
    }

    /**
     *
     */
    public void setFrequencyOffsetRate(String value) {
        _avTable.set(ATTR_FREQUENCY_OFFSET_RATE, Format.toDouble(value));
    }

    public void rmFrequencyOffsetRate() {
        _avTable.noNotifyRm(ATTR_FREQUENCY_OFFSET_RATE);
    }

    public void rmFrequencyOffsetThrow() {
        _avTable.noNotifyRm(ATTR_FREQUENCY_OFFSET_THROW);
    }

    public void rmFrequencyOffsetValues() {
        rmFrequencyOffsetRate();
        rmFrequencyOffsetThrow();
    }

    public int getSecsPerCycle() {
        return _avTable.getInt(ATTR_SECS_PER_CYCLE, 0);
    }

    public void setSecsPerCycle(String value) {
        _avTable.set(ATTR_SECS_PER_CYCLE, value);
    }

    public boolean getCycleReversal() {
        return _avTable.getBool(ATTR_CYCLE_REVERSAL);
    }

    public void setCycleReversal(boolean value) {
        _avTable.set(ATTR_CYCLE_REVERSAL, value);
    }

    public double getStepSize() {
        return _avTable.getDouble(ATTR_SCALE_FACTOR, 0.0);
    }

    public void setStepSize(String value) {
        _avTable.set(ATTR_SCALE_FACTOR, value);
    }

    public boolean getJiggleAtReference() {
        return _avTable.getBool(ATTR_JIGGLE_AT_REFERENCE);
    }

    public void setJiggleAtReference(boolean value) {
        _avTable.set(ATTR_JIGGLE_AT_REFERENCE, value);
    }

    public int getJigglesPerCycle() {
        return _avTable.getInt(ATTR_JIGGLES_PER_CYCLE, 1);
    }

    public void setJigglesPerCycle(String value) {
        _avTable.set(ATTR_JIGGLES_PER_CYCLE, value);
    }

    public double getSampleTime() {
        return _avTable.getDouble(ATTR_SAMPLE_TIME, 4.0);
    }

    public void setSampleTime(String value) {
        _avTable.set(ATTR_SAMPLE_TIME, value);
    }

    public boolean getAutomaticTarget() {
        return _avTable.getBool(ATTR_AUTOMATIC_TARGET);
    }

    public void setAutomaticTarget(boolean value) {
        _avTable.set(ATTR_AUTOMATIC_TARGET, value);
    }

    public boolean getContinuousCal() {
        return _avTable.getBool(ATTR_CONT_CAL);
    }

    public void setContinuousCal(boolean value) {
        _avTable.set(ATTR_CONT_CAL, value);
    }

    public boolean isContinuum() {
        return _avTable.getBool(ATTR_CONTINUUM_MODE);
    }

    public void setContinuumMode(boolean flag) {
        _avTable.set(ATTR_CONTINUUM_MODE, flag);
    }

    /**
     * Override getExposureTime.
     *
     * Get the value from the instrument in scope.
     */
    /** Not supported by JCMT OT. */
    public double getExposureTime() {
        throw new UnsupportedOperationException(
                "public double SpIterObserveBase.getExposureTime()"
                + " not supported by JCMT OT.");
    }

    /** Not supported by JCMT OT. */
    public void setExposureTime(double expTime) {
        throw new UnsupportedOperationException(
                "public double SpIterObserveBase.setExposureTime()"
                + " not supported by JCMT OT.");
    }

    /** Not supported by JCMT OT. */
    public void setExposureTime(String expTime) {
        throw new UnsupportedOperationException(
                "public double SpIterObserveBase.setExposureTime()"
                + " not supported by JCMT OT.");
    }

    /** Not supported by JCMT OT. */
    public int getCoadds() {
        throw new UnsupportedOperationException(
                "public double SpIterObserveBase.getCoadds()"
                + " not supported by JCMT OT.");
    }

    /** Not supported by JCMT OT. */
    public void setCoadds(int coadds) {
        throw new UnsupportedOperationException(
                "public double SpIterObserveBase.setCoadds()"
                + " not supported by JCMT OT.");
    }

    /** Not supported by JCMT OT. */
    public void setCoadds(String coadds) {
        throw new UnsupportedOperationException(
                "public double SpIterObserveBase.setCoadds()"
                + " not supported by JCMT OT.");
    }

    // Setp things up for heterodyne observations
    public void setupForHeterodyne() {
        if (!_avTable.exists(ATTR_SWITCHING_MODE)) {
            _avTable.noNotifySet(ATTR_SWITCHING_MODE,
                    getSwitchingModeOptions()[0], 0);
        }
    }

    public void setupForSCUBA2() {
        _avTable.noNotifyRm(ATTR_SWITCHING_MODE);
    }

    /**
     * Returns the switching modes.
     *
     * Subclasses can overridde this method to return only the switching modes
     * that are used with their observe modes. The default switching modes
     * should be the first element of the array.
     */
    public String[] getSwitchingModeOptions() {
        return new String[]{
                SWITCHING_MODE_BEAM,
                SWITCHING_MODE_POSITION,
//              SWITCHING_MODE_FREQUENCY_S,
                SWITCHING_MODE_FREQUENCY_F,
                SWITCHING_MODE_NONE,
        };
    }
}
