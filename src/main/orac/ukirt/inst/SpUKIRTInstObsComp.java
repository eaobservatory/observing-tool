/*
 * Copyright 1999-2002 United Kingdom Astronomy Technology Centre, an
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

package orac.ukirt.inst;

import gemini.sp.SpItem;
import gemini.sp.SpMSB;
import gemini.sp.SpObs;
import gemini.sp.SpProg;
import gemini.sp.SpType;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.iter.SpIterOffset;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterComp;
import gemini.sp.iter.SpIterConfigObs;
import gemini.sp.iter.SpIterConfigBase;

import orac.ukirt.iter.SpIterBiasObs;
import orac.ukirt.iter.SpIterDarkObs;
import orac.ukirt.iter.SpIterCGS4CalObs;
import orac.ukirt.iter.SpIterMichelleCalObs;

import java.util.Vector;

/**
 * A base class for UKIRT instrument observation component items.
 *
 * This extends gemini.sp.obsComp.SpInstObsComp and adds UKIRT-specific
 * features.
 *
 * @author Alan Bridger, UKATC
 * @version 1.0
 *
 * @see gemini.sp.SpObsData
 * @see gemini.sp.SpObsContextItem
 * @see gemini.sp.obsComp.SpInstObsComp
 */
@SuppressWarnings("serial")
public abstract class SpUKIRTInstObsComp extends SpInstObsComp {
    /** Location of inst aper X value */
    public static final int XAP_INDEX = 0;
    /** Location of inst aper Y value */
    public static final int YAP_INDEX = 1;
    /** Location of inst aper Z value */
    public static final int ZAP_INDEX = 2;
    /** Location of inst aper L value */
    public static final int LAP_INDEX = 3;
    /** Location of inst pointing offset ID value */
    public static final int IDPO_INDEX = 0;
    /** Location of inst pointing offset CH value */
    public static final int CHPO_INDEX = 1;
    /** The instrument port */
    public static String INSTRUMENT_PORT;
    /** Array of inst aper values */
    public static String[] INSTRUMENT_APER;
    /** Array of inst pointing offset values */
    public static String[] INSTRUMENT_PNTG_OFFSET;

    // Names of the attributes
    public static final String ATTR_INSTRUMENT_PORT = "instPort";
    public static final String ATTR_INSTRUMENT_APER = "instAper";
    public static final String ATTR_VERSION = ".version";
    public static final String ATTR_INSTRUMENT_PNTG_OFFSET = "instPntgOffset";

    /**
     * Integration overhead.
     *
     * Number of integrations for UKIRT (according to Paul Hirst, email,
     * 24/05/2002)
     * <BLOCKQUOTE>
     * Most of the time, the number of integrations will be == 1,
     * except with cgs4 and michelle - these instruments should have a number
     * of integrations parameter, which will often be ==
     * sampling_x * sampling_y - except for example in a CGS4 BIAS frame,
     * it's explicitly set to 3.
     * </BLOCKQUOTE>
     *
     * This variable is used for MSB duration estimation. Subclasses can set
     * this to an instrument specific value. Currently 0.5 is used for all
     * instruments.
     */
    protected double _int_oh = 0.5;

    /**
     * Overhead per observe iteration ("Eye").
     *
     * This variable is used for MSB duration estimation. Subclasses can set
     * this to an instrument specific value. Currently 0.5 is used for all
     * instruments.
     */
    protected double _obs_oh = 0.5;

    /**
     * Constructor. Sets default values for attributes.
     */
    public SpUKIRTInstObsComp(SpType spType) {
        super(spType);

        _avTable.noNotifySet(ATTR_VERSION, "1.0", 0);
        _avTable.noNotifySet(ATTR_INSTRUMENT_PORT, "Centre", 0);
        _avTable.noNotifySet(ATTR_INSTRUMENT_APER, "0.0", XAP_INDEX);
        _avTable.noNotifySet(ATTR_INSTRUMENT_APER, "0.0", YAP_INDEX);
        _avTable.noNotifySet(ATTR_INSTRUMENT_APER, "0.0", ZAP_INDEX);
        _avTable.noNotifySet(ATTR_INSTRUMENT_APER, "0.0", LAP_INDEX);
        _avTable.noNotifySet(ATTR_EXPOSURE_TIME, "0", 0);
        _avTable.noNotifySet(ATTR_INSTRUMENT_PNTG_OFFSET, "0.0", IDPO_INDEX);
        _avTable.noNotifySet(ATTR_INSTRUMENT_PNTG_OFFSET, "0.0", CHPO_INDEX);

    }

    /**
     * Set X value of the instrument aperture
     */
    public void setInstApX(String x) {
        _avTable.set(ATTR_INSTRUMENT_APER, x, XAP_INDEX);
    }

    /**
     * Set Y value of the instrument aperture
     */
    public void setInstApY(String y) {
        _avTable.set(ATTR_INSTRUMENT_APER, y, YAP_INDEX);
    }

    /**
     * Set Z value of the instrument aperture
     */
    public void setInstApZ(String z) {
        _avTable.set(ATTR_INSTRUMENT_APER, z, ZAP_INDEX);
    }

    /**
     * Set L value of the instrument aperture
     */
    public void setInstApL(String l) {
        _avTable.set(ATTR_INSTRUMENT_APER, l, LAP_INDEX);
    }

    /**
     * Set the instrument apertures. It should be called whenever something
     * changes that causes a change in instrument aperture.
     * Each instrument should provide its own implementation.
     */
    public void setInstAper() {
    }

    /**
     * Update attribute-value table.
     * Each instrument should provide its own implementation.
     */
    public void avTableUpdate() {
    }


    /**
     * Get the instrument aperture X value
     */
    public double getInstApX() {
        return _avTable.getDouble(ATTR_INSTRUMENT_APER, XAP_INDEX, 0.0);
    }

    /**
     * Get the instrument aperture Y value
     */
    public double getInstApY() {
        return _avTable.getDouble(ATTR_INSTRUMENT_APER, YAP_INDEX, 0.0);
    }

    /**
     * Get the instrument aperture X value in arcseconds
     */
    public double getInstApXra() {
        // Sign is different because instrument apertures are defined in a
        // left-handed coordinate system, but RA/Dec is right-handed.
        return -1.0 * getInstApX();
    }

    /**
     * Get the instrument aperture Y value in arcseconds
     */
    public double getInstApYdec() {
        // Handedness of coordinate systems does not affect Dec direction.
        return getInstApY();
    }

    /**
     * Get the instrument aperture Z value
     */
    public double getInstApZ() {
        return _avTable.getDouble(ATTR_INSTRUMENT_APER, ZAP_INDEX, 0.0);
    }

    /**
     * Get the instrument aperture L value
     */
    public double getInstApL() {
        return _avTable.getDouble(ATTR_INSTRUMENT_APER, LAP_INDEX, 0.0);
    }

    /**
     * Set the ID value of the instrument pointing offset
     */
    public void setInstPntgOffsetID(String id) {
        _avTable.set(ATTR_INSTRUMENT_PNTG_OFFSET, id, IDPO_INDEX);
    }

    /**
     * Set the CH value of the instrument pointing offset
     */
    public void setInstPntgOffsetCH(String ch) {
        _avTable.set(ATTR_INSTRUMENT_PNTG_OFFSET, ch, CHPO_INDEX);
    }

    /**
     * Get the instrument pointing offset ID value.
     * The -1 is due to coordinate system considerations.
     */
    public double getInstPntgOffsetID() {
        return -1.0 * _avTable.getDouble(
                ATTR_INSTRUMENT_PNTG_OFFSET, IDPO_INDEX, 0.0);
    }

    /**
     * Get the instrument pointing offset CH value
     */
    public double getInstPntgOffsetCH() {
        return _avTable.getDouble(ATTR_INSTRUMENT_PNTG_OFFSET, CHPO_INDEX, 0.0);
    }

    /**
     * Set the instrument port
     */
    public void setPort(String port) {
        _avTable.set(ATTR_INSTRUMENT_PORT, port);
    }

    /**
     * Get the instrument port
     */
    public String getPort() {
        return _avTable.get(ATTR_INSTRUMENT_PORT);
    }

    /**
     * Get the exposure time.
     *
     * This is a wrap-around for the SpInstObsComp version which allows us
     * to intercept the value and change it more easily, e.g.  setting
     * a default. The original is declared final so we can't just override.
     * This could of course change. Must be overridden by instruments
     * that wish to make use of the defaulting feature.
     */
    public double getExpTime() {
        return (getExposureTime());
    }

    /**
     * Set the exposure time.
     *
     * This is a wrap-around for the SpInstObsComp version which allows us
     * to intercept the value and change it more easily, e.g.  setting
     * a default. The original is declared final so we can't just override.
     * This could of course change. Must be overridden by instruments
     * that wish to make use of the defaulting feature.
     */
    public void setExpTime(String seconds) {
        setExposureTime(seconds);
    }

    /**
     * Get the default exposure time.
     *
     * This one returns 1.0. This must be overridden by instruments wishing
     * to use the feature.
     */
    public double getDefaultExpTime() {
        return 1.0;
    }

    /**
     * Get the default bias exposure time.
     *
     * Instruments should override this to return sensible values.
     */
    public double getDefaultBiasExpTime() {
        return 0.001;
    }

    /**
     * Get the default bias coadds.
     *
     * Instruments should override this to return sensible values.
     */
    public int getDefaultBiasCoadds() {
        return 50;
    }

    /**
     * Returns a time estimate in seconds for slewing the telescope for
     * UKIRT imaging: 3 minutes.
     *
     * Should overriden by classes for instrument that do spectroscopy.
     */
    public double getSlewTime() {
        return 3.0 * 60.0;
    }

    /**
     * Generic Iteration Tracker for UKIRT.
     *
     * This IterationTracker is used as it is by IRCAM3 and UFTI.
     * Michelle and CGS4 instrument components have to subclass it.
     *
     * Added for OMP by MFO, 1 Novemeber 2001
     *
     * @see gemini.sp.obsComp.SpInstObsComp
     */
    public class IterTrackerUKIRT extends IterationTracker {
        double currentExposureTime = getExpTime();
        int currentNoCoadds = 1;
        SpIterComp currentIterStepItem = null;
        boolean exposureTimeOverride = false;
        boolean coaddsOverride = false;

        IterTrackerUKIRT() {
            if (_avTable.exists(ATTR_COADDS)) {
                currentNoCoadds = _avTable.getInt(ATTR_COADDS, 1);
            }
        }

        public void update(SpIterStep spIterStep) {
            boolean expTimeFound = false;
            boolean coaddsFound = false;

            currentIterStepItem = spIterStep.item;

            try {
                String attribute = null;
                String value = null;

                for (int i = 0; i < spIterStep.values.length; i++) {
                    // SpIterStep.values is an array of SpIterValue
                    // SpIterValue.values is an array of String the first
                    // of which contains
                    attribute = spIterStep.values[i].attribute;

                    if ((spIterStep.values[i].values != null)
                            && (spIterStep.values[i].values.length > 0)) {
                        value = spIterStep.values[i].values[0];
                    }

                    if ((attribute != null) && (value != null)) {
                        if (attribute.equals(ATTR_EXPOSURE_TIME)) {
                            currentExposureTime = Double.valueOf(value);

                            if (currentIterStepItem instanceof
                                    SpIterConfigBase) {
                                exposureTimeOverride = true;
                            }

                            expTimeFound = true;
                        }
                    }

                    if (attribute.equals(ATTR_COADDS)) {
                        currentNoCoadds = Integer.valueOf(value);
                        if (currentIterStepItem instanceof SpIterConfigBase) {
                            coaddsOverride = true;
                        }

                        coaddsFound = true;
                    }
                }

                if (!expTimeFound && !exposureTimeOverride) {
                    // See if we can get an exposure time from the instrument
                    SpInstObsComp instrument =
                            SpTreeMan.findInstrument(currentIterStepItem);

                    if (instrument != null) {
                        currentExposureTime = instrument.getExposureTime();
                    }
                }

                if (!coaddsFound && !coaddsOverride) {
                    SpInstObsComp instrument =
                            SpTreeMan.findInstrument(currentIterStepItem);

                    if (instrument != null) {
                        currentNoCoadds = instrument.getCoadds();
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not process iteration step "
                        + spIterStep.title + " for time estimation:\n\n" + e);
            }
        }

        public double getObserveStepTime() {
            // extra_oh is a constant overheads related to certain observe
            // iterators: 30 seconds for dark, arc, flat or bias (in addition
            // to the times to do their respective eye), and 30 secs each time
            // an instrument iterator changes a filter.
            double extra_oh = 0.0;

            if ((currentIterStepItem != null)
                    && ((currentIterStepItem instanceof SpIterBiasObs)
                            || (currentIterStepItem instanceof SpIterDarkObs)
                            || (currentIterStepItem instanceof SpIterCGS4CalObs)
                            || (currentIterStepItem instanceof SpIterMichelleCalObs)
                            || (currentIterStepItem instanceof SpIterConfigObs))) {
                extra_oh = 30.0;
            }

            return (currentNoCoadds
                            * (currentExposureTime + getExposureOverhead()))
                    + _int_oh + _obs_oh + extra_oh;
        }
    }

    public IterationTracker createIterationTracker() {
        return new IterTrackerUKIRT();
    }

    public void setPosAngleDegrees(double posAngle) {
        //Hacky attempt to fix up offsets for UKIRT
        SpItem parent = parent();
        Vector<SpItem> offsets;

        while (parent != null) {
            boolean msbOrProg = (parent instanceof SpMSB
                            || parent instanceof SpProg);
            boolean isSpObs = parent instanceof SpObs;

            if (msbOrProg || isSpObs) {
                offsets = SpTreeMan.findAllInstances(parent,
                        SpIterOffset.class.getName());

                if (offsets != null) {
                    for (int i = 0; i < offsets.size(); i++) {
                        SpIterOffset thisOffset = (SpIterOffset) offsets.get(i);

                        if (isSpObs || ((msbOrProg)
                                && (SpTreeMan.findInstrument(thisOffset)
                                        == this))) {
                            thisOffset.getPosList().setPosAngle(posAngle);
                        }
                    }
                }

                break;
            }

            parent = parent.parent();
        }

        super.setPosAngleDegrees(posAngle);
    }
}
