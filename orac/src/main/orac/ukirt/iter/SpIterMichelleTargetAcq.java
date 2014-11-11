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

package orac.ukirt.iter;

import orac.ukirt.inst.SpInstMichelle;

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import gemini.sp.obsComp.SpInstConstants;

import gemini.util.MathUtil;

/**
 * Enumerater for the elements of the Observe iterator.
 */
@SuppressWarnings("serial")
class SpIterMichelleTargetAcqEnumeration extends SpIterEnumeration {

    private int _curCount = 0;
    private int _maxCount;
    private SpIterValue[] _values;

    SpIterMichelleTargetAcqEnumeration(SpIterMichelleTargetAcq iterObserve) {
        super(iterObserve);
        _maxCount = iterObserve.getCount();
    }

    protected boolean _thisHasMoreElements() {
        return (_curCount < _maxCount);
    }

    protected SpIterStep _thisFirstElement() {
        SpIterMichelleTargetAcq ibo = (SpIterMichelleTargetAcq) _iterComp;

        ibo.useDefaultDisperser();
        ibo.updateDAConf();

        String exposureTimeValue = String.valueOf(ibo.getExposureTime());
        String coaddsValue = String.valueOf(ibo.getCoadds());
        String nreadsValue = String.valueOf(ibo.W_nreads);
        String nresetsValue = String.valueOf(ibo.W_nresets);
        String resetDelayValue = String.valueOf(ibo.W_resetDelay);
        String readIntervalValue = String.valueOf(ibo.W_readInterval);
        String idlePeriodValue = String.valueOf(ibo.W_idlePeriod);
        String mustIdlesValue = String.valueOf(ibo.W_mustIdles);
        String nullCyclesValue = String.valueOf(ibo.W_nullCycles);
        String nullExposuresValue = String.valueOf(ibo.W_nullExposures);
        String nullReadsValue = String.valueOf(ibo.W_nullReads);
        String dutyCycleValue = String.valueOf(ibo.W_dutyCycle);
        String chopDelayValue = String.valueOf(ibo.W_chopDelay);
        String obsTimeValue = String.valueOf(ibo.W_obsTime);

        _values = new SpIterValue[23];
        _values[0] = new SpIterValue(SpInstConstants.ATTR_EXPOSURE_TIME,
                exposureTimeValue);
        _values[1] = new SpIterValue(SpInstConstants.ATTR_COADDS, coaddsValue);
        _values[2] = new SpIterValue(SpMichelleTargetAcqConstants.ATTR_FILTER,
                ibo.getFilter());
        _values[3] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_DISPERSER,
                ibo.getDisperser());
        _values[4] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_SCIENCE_AREA,
                ibo.getScienceAreaString());
        _values[5] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_PIXEL_FOV,
                ibo.getPixelFOVString());
        _values[6] = new SpIterValue(SpMichelleTargetAcqConstants.ATTR_MODE,
                ibo.W_mode);
        _values[7] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_WAVEFORM, ibo.W_waveform);
        _values[8] = new SpIterValue(SpMichelleTargetAcqConstants.ATTR_NREADS,
                nreadsValue);
        _values[9] = new SpIterValue(SpMichelleTargetAcqConstants.ATTR_NRESETS,
                nresetsValue);
        _values[10] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_RESET_DELAY, resetDelayValue);
        _values[11] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_READ_INTERVAL,
                readIntervalValue);
        _values[12] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_IDLE_PERIOD, idlePeriodValue);
        _values[13] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_MUST_IDLES, mustIdlesValue);
        _values[14] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_NULL_CYCLES, nullCyclesValue);
        _values[15] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_NULL_EXPOSURES,
                nullExposuresValue);
        _values[16] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_NULL_READS, nullReadsValue);
        _values[17] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_DUTY_CYCLE, dutyCycleValue);
        _values[18] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_CHOP_FREQUENCY,
                ibo.W_chopFrequency);
        _values[19] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_CHOP_DELAY, chopDelayValue);
        _values[20] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_SAMPLING, ibo.getSampling());
        _values[21] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_OBSERVATION_TIME,
                obsTimeValue);
        _values[22] = new SpIterValue(
                SpMichelleTargetAcqConstants.ATTR_OBSTIME_OT,
                ibo.getObservationTime());

        return _thisNextElement();
    }

    protected SpIterStep _thisNextElement() {
        return new SpIterStep("TargetAcq", _curCount++, _iterComp, _values);
    }

}

@SuppressWarnings("serial")
public class SpIterMichelleTargetAcq extends SpIterObserveBase {
    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "instMichelleTargetAcq",
            "Michelle Spectroscopy Target Acquisition");

    public String W_mode;
    public String W_waveform;
    public int W_nreads;
    public int W_nresets;
    public double W_resetDelay;
    public double W_readInterval;
    public double W_idlePeriod;
    public int W_mustIdles;
    public int W_nullCycles;
    public int W_nullExposures;
    public int W_nullReads;
    public String W_chopFrequency;
    public double W_chopDelay;
    public double W_dutyCycle;
    public double W_obsTime;

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterMichelleTargetAcq());
    }

    /**
     * Default constructor.
     */
    public SpIterMichelleTargetAcq() {
        super(SP_TYPE);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_DISPERSER,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_FILTER,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_SCIENCE_AREA,
                null, 0);
        _avTable.noNotifySet(SpInstConstants.ATTR_EXPOSURE_TIME, null, 0);
        _avTable.noNotifySet(SpInstConstants.ATTR_COADDS, null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_MODE, null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_WAVEFORM,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_NREADS,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_NRESETS,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_RESET_DELAY,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_READ_INTERVAL,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_IDLE_PERIOD,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_MUST_IDLES,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_NULL_CYCLES,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_NULL_EXPOSURES,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_NULL_READS,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_DUTY_CYCLE,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_CHOP_FREQUENCY,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_CHOP_DELAY,
                null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_SAMPLING,
                null, 0);
        _avTable.noNotifySet(
                SpMichelleTargetAcqConstants.ATTR_OBSERVATION_TIME, null, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_OBSTIME_OT,
                null, 0);
    }

    /**
     * Use default acquisition
     */
    public void useDefaultAcquisition() {
        _avTable.rm(SpInstConstants.ATTR_EXPOSURE_TIME);
        _avTable.rm(SpInstConstants.ATTR_COADDS);
    }

    /**
     * Use default disperser
     */
    public void useDefaultDisperser() {
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_DISPERSER);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_FILTER);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_SCIENCE_AREA);
    }

    /**
     */
    public SpIterEnumeration elements() {
        return new SpIterMichelleTargetAcqEnumeration(this);
    }

    /**
     * Get the instrument item in the scope of the base item.
     */
    public SpInstObsComp getInstrumentItem() {
        SpItem _baseItem = parent();
        return SpTreeMan.findInstrument(_baseItem);
    }

    /**
     * Override getExposureTime to provide a default if required.
     */
    public double getExposureTime() {
        double et = _avTable.getDouble(SpInstConstants.ATTR_EXPOSURE_TIME, 0.0);

        if (et == 0.0) {
            et = getDefaultExposureTime();
            setExposureTime(Double.toString(MathUtil.round(et, 2)));
        }

        return et;
    }

    /**
     * Override getExposureTime to provide a default if required.
     */
    public String getExposureTimeString() {
        return Double.toString(getExposureTime());
    }

    /**
     * Provide a default exposure time.
     */
    public double getDefaultExposureTime() {
        // Get the exposure time from the Michelle instrument
        SpInstMichelle inst = (SpInstMichelle) getInstrumentItem();

        return inst.getDefaultTargetAcqExpTime();
    }

    /**
     * Set the exposure time
     */
    public void setExpTime(String expTime) {
        _avTable.set(SpInstConstants.ATTR_EXPOSURE_TIME, expTime);
    }

    /**
     * Override getCoadds to provide a default if required.
     */
    public int getCoadds() {
        int coadds = _avTable.getInt(SpInstConstants.ATTR_COADDS, 0);

        if (coadds == 0) {
            coadds = getDefaultCoadds();
            setCoadds(Integer.toString(coadds));
        }

        return coadds;
    }

    /**
     * Override getCoadds to provide a default if required.
     */
    public String getCoaddsString() {
        return Integer.toString(getCoadds());
    }

    /**
     * Provide a default number of coadds.
     */
    public int getDefaultCoadds() {
        // Get the number of coadds from the Michelle instrument
        SpInstMichelle inst = (SpInstMichelle) getInstrumentItem();
        return inst.getDefaultTargetAcqCoadds();
    }

    /**
     * Set the coadds
     */
    public void setCoadds(String coadds) {
        _avTable.set(SpInstConstants.ATTR_COADDS, coadds);
    }

    /**
     * Set the observation time
     */
    public void setObservationTime(String obsTime) {
        _avTable.set(SpMichelleTargetAcqConstants.ATTR_OBSTIME_OT, obsTime);
    }

    /**
     * Get the observation Time
     */
    public String getObservationTime() {
        String obsTime = _avTable.get(
                SpMichelleTargetAcqConstants.ATTR_OBSTIME_OT);

        if (obsTime == null) {
            obsTime = Double.toString(
                    MathUtil.round(SpInstMichelle.DEFAULT_TOBS, 2));
            setObservationTime(obsTime);
        }

        return obsTime;
    }

    /**
     * Get the sampling for the target acquisition
     */
    public String getSampling() {
        String sam = _avTable.get(SpMichelleTargetAcqConstants.ATTR_SAMPLING);

        if (sam == null) {
            sam = SpInstMichelle.DEFAULT_SAMPLING_TARGET_ACQ;
        }

        return sam;
    }

    /**
     * Set the target acquisition sampling
     */
    public void setSampling(String sam) {
        _avTable.set(SpMichelleTargetAcqConstants.ATTR_SAMPLING, sam);
    }

    /**
     * Get the filter
     */
    public String getFilter() {
        String filter = _avTable.get(SpMichelleTargetAcqConstants.ATTR_FILTER);

        if (filter == null) {
            filter = getDefaultFilter();
            setFilter(filter);
        }

        return filter;
    }

    /**
     * Get a default value for the filter
     */
    public String getDefaultFilter() {
        return SpInstMichelle.DEFAULT_FILTER_TARGET_ACQ;
    }

    /**
     * Update the Filter value in the attribute-value table
     */
    public void setFilter(String filter) {

        _avTable.set(SpMichelleTargetAcqConstants.ATTR_FILTER, filter);
    }

    /**
     * Get the disperser
     */
    public String getDisperser() {
        String disperser = _avTable.get(
                SpMichelleTargetAcqConstants.ATTR_DISPERSER);

        if (disperser == null) {
            SpInstMichelle inst = (SpInstMichelle) getInstrumentItem();
            disperser = inst.getDisperser();
        }

        setDisperser(disperser);
        return disperser;

    }

    /**
     * Update the Disperser value in the attribute-value table
     */
    public void setDisperser(String disperser) {
        _avTable.set(SpMichelleTargetAcqConstants.ATTR_DISPERSER, disperser);
    }

    /**
     * Get the science area as a string
     */
    public String getScienceAreaString() {
        SpInstMichelle inst = (SpInstMichelle) getInstrumentItem();
        String fovs = inst.getScienceAreaString("TARGETACQ");

        setScienceArea(fovs);
        return fovs;
    }

    /**
     * Update the science area value in the attribute-value table
     */
    public void setScienceArea(String scienceArea) {
        _avTable.set(SpMichelleTargetAcqConstants.ATTR_SCIENCE_AREA,
                scienceArea);
    }

    /**
     * Get the pixel FOV
     */
    public double[] getPixelFOV() {
        SpInstMichelle inst = (SpInstMichelle) getInstrumentItem();
        double pfov[];

        pfov = inst.getPixelFOV("TARGETACQ");
        return pfov;
    }

    /**
     * Get the pixel FOV as a string
     */
    public String getPixelFOVString() {
        double pfov[];
        pfov = getPixelFOV();
        double w = MathUtil.round(pfov[0], 4);
        double h = MathUtil.round(pfov[1], 4);
        String pfovs = w + " x " + h;

        setPixelFOV(pfovs);
        return pfovs;
    }

    /**
     * Update the science area value in the attribute-value table
     */
    public void setPixelFOV(String pfovs) {
        _avTable.set(SpMichelleTargetAcqConstants.ATTR_PIXEL_FOV, pfovs);
    }

    /**
     * Use defaults.
     *
     * Reset values so that defaults will get used.
     */
    public void useDefaults() {
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_FILTER);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_MODE);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_PIXEL_FOV);
        _avTable.rm(SpInstConstants.ATTR_EXPOSURE_TIME);
        _avTable.rm(SpInstConstants.ATTR_COADDS);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_WAVEFORM);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_NREADS);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_NRESETS);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_RESET_DELAY);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_READ_INTERVAL);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_IDLE_PERIOD);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_MUST_IDLES);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_NULL_CYCLES);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_NULL_EXPOSURES);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_NULL_READS);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_DUTY_CYCLE);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_CHOP_FREQUENCY);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_CHOP_DELAY);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_SAMPLING);
        _avTable.rm(SpMichelleTargetAcqConstants.ATTR_OBSERVATION_TIME);
    }

    /**
     * Update the daconf for an Object/Sky observatioon
     */
    public void updateDAConf() {
        SpInstMichelle inst = (SpInstMichelle) getInstrumentItem();
        inst.setTargetAcqExpTime(getExposureTime());
        inst.setTargetAcqObservationTime(Double.valueOf(getObservationTime()));
        inst.updateDATargetAcqConf();

        /* Update local instance variables from Michelle class */
        W_mode = inst.W_mode;
        W_waveform = inst.W_waveform;
        W_nreads = inst.W_nreads;
        W_nresets = inst.W_nresets;
        W_resetDelay = inst.W_resetDelay;
        W_readInterval = inst.W_readInterval;
        W_idlePeriod = inst.W_idlePeriod;
        W_mustIdles = inst.W_mustIdles;
        W_nullCycles = inst.W_nullCycles;
        W_nullExposures = inst.W_nullExposures;
        W_nullReads = inst.W_nullReads;
        W_dutyCycle = inst.W_dutyCycle;
        W_chopFrequency = inst.W_chopFrequency;
        W_chopDelay = inst.W_chopDelay;
        W_obsTime = inst.W_obsTime;

        /* Update attributes from instance variables */
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_MODE,
                W_mode, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_WAVEFORM,
                W_waveform, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_NREADS,
                Integer.toString(W_nreads), 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_NRESETS,
                Integer.toString(W_nresets), 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_RESET_DELAY,
                Double.toString(W_resetDelay), 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_READ_INTERVAL,
                Double.toString(W_readInterval), 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_IDLE_PERIOD,
                Double.toString(W_idlePeriod), 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_MUST_IDLES,
                Integer.toString(W_mustIdles), 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_NULL_CYCLES,
                Integer.toString(W_nullCycles), 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_NULL_EXPOSURES,
                Integer.toString(W_nullExposures), 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_NULL_READS,
                Integer.toString(W_nullReads), 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_DUTY_CYCLE,
                Double.toString(W_dutyCycle), 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_CHOP_FREQUENCY,
                W_chopFrequency, 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_CHOP_DELAY,
                Double.toString(W_chopDelay), 0);
        _avTable.noNotifySet(
                SpMichelleTargetAcqConstants.ATTR_OBSERVATION_TIME,
                Double.toString(W_obsTime), 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_FILTER,
                getFilter(), 0);
        _avTable.noNotifySet(SpMichelleTargetAcqConstants.ATTR_SAMPLING,
                getSampling(), 0);
    }
}
