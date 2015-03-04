/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
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

// author: Alan Pickup = dap@roe.ac.uk         2001 Feb

package orac.ukirt.iter;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import orac.ukirt.inst.SpInstMichelle;
import orac.ukirt.inst.SpDRRecipe;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.sp.obsComp.SpInstObsComp;

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import gemini.util.ConfigWriter;
import gemini.util.TranslationUtils;

@SuppressWarnings("serial")
class SpIterMichelleCalObsEnumeration extends SpIterEnumeration {
    private int _curCount = 0;
    private int _maxCount;
    private String _calType;
    private SpIterValue[] _values;

    SpIterMichelleCalObsEnumeration(SpIterMichelleCalObs iterObserve) {
        super(iterObserve);

        _maxCount = iterObserve.getCount();
        _calType = iterObserve.getCalTypeString();
    }

    protected boolean _thisHasMoreElements() {
        return (_curCount < _maxCount);
    }

    protected SpIterStep _thisFirstElement() {
        SpIterMichelleCalObs ico = (SpIterMichelleCalObs) _iterComp;
        _values = new SpIterValue[21];

        ico.updateDAConf();
        _values[0] = new SpIterValue(SpMichelleCalConstants.ATTR_FILTER,
                ico.getFilter());
        _values[1] = new SpIterValue(SpMichelleCalConstants.ATTR_MODE,
                ico.W_mode);
        _values[2] = new SpIterValue(SpMichelleCalConstants.ATTR_EXPOSURE_TIME,
                String.valueOf(ico.getExposureTime()));
        _values[3] = new SpIterValue(SpMichelleCalConstants.ATTR_WAVEFORM,
                ico.W_waveform);
        _values[4] = new SpIterValue(SpMichelleCalConstants.ATTR_NREADS,
                String.valueOf(ico.W_nreads));
        _values[5] = new SpIterValue(SpMichelleCalConstants.ATTR_NRESETS,
                String.valueOf(ico.W_nresets));
        _values[6] = new SpIterValue(SpMichelleCalConstants.ATTR_RESET_DELAY,
                String.valueOf(ico.W_resetDelay));
        _values[7] = new SpIterValue(SpMichelleCalConstants.ATTR_READ_INTERVAL,
                String.valueOf(ico.W_readInterval));
        _values[8] = new SpIterValue(SpMichelleCalConstants.ATTR_IDLE_PERIOD,
                String.valueOf(ico.W_idlePeriod));
        _values[9] = new SpIterValue(SpMichelleCalConstants.ATTR_MUST_IDLES,
                String.valueOf(ico.W_mustIdles));
        _values[10] = new SpIterValue(SpMichelleCalConstants.ATTR_NULL_CYCLES,
                String.valueOf(ico.W_nullCycles));
        _values[11] = new SpIterValue(
                SpMichelleCalConstants.ATTR_NULL_EXPOSURES,
                String.valueOf(ico.W_nullExposures));
        _values[12] = new SpIterValue(SpMichelleCalConstants.ATTR_NULL_READS,
                String.valueOf(ico.W_nullReads));
        _values[13] = new SpIterValue(SpMichelleCalConstants.ATTR_DUTY_CYCLE,
                String.valueOf(ico.W_dutyCycle));
        _values[14] = new SpIterValue(
                SpMichelleCalConstants.ATTR_CHOP_FREQUENCY,
                ico.W_chopFrequency);
        _values[15] = new SpIterValue(SpMichelleCalConstants.ATTR_CHOP_DELAY,
                String.valueOf(ico.W_chopDelay));
        _values[16] = new SpIterValue(SpMichelleCalConstants.ATTR_COADDS,
                String.valueOf(ico.getCoadds()));
        _values[17] = new SpIterValue(SpMichelleCalConstants.ATTR_FLAT_SOURCE,
                ico.getFlatSource());
        _values[18] = new SpIterValue(SpMichelleCalConstants.ATTR_SAMPLING,
                ico.getSampling());
        _values[19] = new SpIterValue(
                SpMichelleCalConstants.ATTR_OBSERVATION_TIME,
                String.valueOf(ico.W_obsTime));
        _values[20] = new SpIterValue(SpMichelleCalConstants.ATTR_OBSTIME_OT,
                ico.getObservationTime());

        return _thisNextElement();
    }

    protected SpIterStep _thisNextElement() {
        return new SpIterStep(_calType, _curCount++, _iterComp, _values);
    }

}

/**
 * Iterator for Michelle calibration observations (FLAT and ARC).
 */
@SuppressWarnings("serial")
public class SpIterMichelleCalObs extends SpIterObserveBase implements SpTranslatable {

    /** Identifier for a FLAT calibration. */
    public static final int FLAT = 0;

    /** Identifier for an ARC calibration. */
    public static final int ARC = 1;

    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "MichelleCalObs",
            "Michelle Cal Observe");

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
    public int W_coadds;
    public double W_dutyCycle;
    public double W_obsTime;

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterMichelleCalObs());
    }

    /**
     * Default constructor.
     */
    public SpIterMichelleCalObs() {
        super(SP_TYPE);

        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_CALTYPE, "Flat", 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_FILTER, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_MODE, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_EXPOSURE_TIME,
                null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_WAVEFORM, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_NREADS, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_NRESETS, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_RESET_DELAY, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_READ_INTERVAL,
                null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_IDLE_PERIOD, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_MUST_IDLES, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_NULL_CYCLES, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_NULL_EXPOSURES, null,
                0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_NULL_READS, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_DUTY_CYCLE, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_CHOP_FREQUENCY, null,
                0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_CHOP_DELAY, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_COADDS, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_FLAT_SOURCE, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_SAMPLING, null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_OBSERVATION_TIME,
                null, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_OBSTIME_OT, null, 0);
    }

    /**
     * Override getTitle to return the observe type and the count.
     */
    public String getTitle() {
        if (getTitleAttr() != null) {
            return super.getTitle();
        }

        return getCalTypeString() + " (" + getCount() + "X)";
    }

    /**
     */
    public SpIterEnumeration elements() {
        return new SpIterMichelleCalObsEnumeration(this);
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
        double et = _avTable.getDouble(
                SpMichelleCalConstants.ATTR_EXPOSURE_TIME, 0.0);

        if (et == 0.0) {
            et = getDefaultExposureTime();
            setExposureTime(Double.toString(et));
        }

        return et;

    }

    /**
     * Provide a default exposure time.
     */
    public double getDefaultExposureTime() {
        double det = 0.0;
        // Get the exposure time from the Michelle instrument
        SpInstMichelle inst = (SpInstMichelle) getInstrumentItem();

        if (getCalType() == FLAT) {
            det = inst.getDefaultFlatExpTime();
        } else if (getCalType() == ARC) {
            det = inst.getDefaultExpTime();
        }

        return det;

    }

    /**
     * Set the exposure time.
     */
    public void setExpTime(String expTime) {
        _avTable.set(SpMichelleCalConstants.ATTR_EXPOSURE_TIME, expTime);
    }

    /**
     * Set the observation time.
     */
    public void setObservationTime(String obsTime) {
        _avTable.set(SpMichelleCalConstants.ATTR_OBSTIME_OT, obsTime);
    }

    /**
     * Get the observation Time.
     */
    public String getObservationTime() {
        String obsTime = _avTable.get(SpMichelleCalConstants.ATTR_OBSTIME_OT);

        if (obsTime == null) {
            if (getCalType() == FLAT) {
                obsTime = SpMichelleCalConstants.DEFAULT_FLAT_OBSERVATION_TIME;
            } else if (getCalType() == ARC) {
                obsTime = SpMichelleCalConstants.DEFAULT_ARC_OBSERVATION_TIME;
            }

            setObservationTime(obsTime);
        }

        return obsTime;
    }

    /**
     * Get the type of calibration.
     */
    public int getCalType() {
        String calType = _avTable.get(SpMichelleCalConstants.ATTR_CALTYPE);

        if ("Flat".equals(calType)) {
            return FLAT;
        }

        return ARC;
    }

    /**
     * Set the type of calibration.
     */
    public void setCalType(String calType) {
        _avTable.set(SpMichelleCalConstants.ATTR_CALTYPE, calType);
    }

    /**
     * Get the type of calibration as a String.
     */
    public String getCalTypeString() {
        if (getCalType() == FLAT) {
            return "Flat";
        }

        return "Arc";
    }

    /**
     * Get the calibration type choices.
     */
    public String[] getCalTypeChoices() {
        SpInstMichelle inst = (SpInstMichelle) getInstrumentItem();

        if (inst.isImaging()) {
            String choices[] = new String[1];
            choices[0] = "Flat";
            return choices;

        } else {
            String choices[] = new String[2];
            choices[0] = "Flat";
            choices[1] = "Arc";
            return choices;
        }
    }

    /**
     * Get the flat sampling choices.
     */
    public String[] getSamplingChoices() {
        String choices[] = new String[2];

        choices[0] = "as object";
        choices[1] = "1x1";

        return choices;
    }

    /**
     * Get the sampling for the flat
     */
    public String getSampling() {
        String sam = _avTable.get(SpMichelleCalConstants.ATTR_SAMPLING);

        if (sam == null) {
            sam = getSamplingChoices()[0];
        }

        return sam;
    }

    /**
     * Set the flat sampling for the flat
     */
    public void setSampling(String sam) {
        _avTable.set(SpMichelleCalConstants.ATTR_SAMPLING, sam);
    }

    /**
     * Get the flat source
     */
    public String getFlatSource() {
        String fs = _avTable.get(SpMichelleCalConstants.ATTR_FLAT_SOURCE);

        if (fs == null) {
            /* The default is the first available choice */
            fs = getFlatSourceChoices()[0];
            setFlatSource(fs);
        }

        return fs;
    }

    /**
     * Set the flat source
     */
    public void setFlatSource(String fs) {
        _avTable.set(SpMichelleCalConstants.ATTR_FLAT_SOURCE, fs);
    }

    /**
     * Get the flat source choices.
     */
    public String[] getFlatSourceChoices() {
        SpInstMichelle inst = (SpInstMichelle) getInstrumentItem();
        int ni = inst.getFlatList().length;
        String choices[] = new String[ni];

        for (int i = 0; i < ni; i++) {
            choices[i] = inst.getFlatList()[i];
        }

        return choices;
    }

    /**
     * Get the filter.
     */
    public String getFilter() {
        String filter = _avTable.get(SpMichelleCalConstants.ATTR_FILTER);

        if (filter == null || filter.equals("none")) {
            filter = getDefaultFilter();
            setFilter(filter);
        }

        return filter;
    }

    /**
     * Get a default value for the filter
     */
    public String getDefaultFilter() {
        String filter = null;

        SpInstMichelle inst = (SpInstMichelle) getInstrumentItem();

        if (getCalType() == FLAT) {
            filter = inst.getFilter();
        } else if (getCalType() == ARC) {
            filter = inst.getArcFilter();
        }

        return filter;
    }

    /**
     * Set the filter.
     */
    public void setFilter(String filter) {
        _avTable.set(SpMichelleCalConstants.ATTR_FILTER, filter);
    }

    /**
     * Use Defaults.
     *
     * Reset values so that defaults will get used.
     */
    public void useDefaults() {
        _avTable.rm(SpMichelleCalConstants.ATTR_FILTER);
        _avTable.rm(SpMichelleCalConstants.ATTR_MODE);
        _avTable.rm(SpMichelleCalConstants.ATTR_EXPOSURE_TIME);
        _avTable.rm(SpMichelleCalConstants.ATTR_WAVEFORM);
        _avTable.rm(SpMichelleCalConstants.ATTR_NREADS);
        _avTable.rm(SpMichelleCalConstants.ATTR_NRESETS);
        _avTable.rm(SpMichelleCalConstants.ATTR_RESET_DELAY);
        _avTable.rm(SpMichelleCalConstants.ATTR_READ_INTERVAL);
        _avTable.rm(SpMichelleCalConstants.ATTR_IDLE_PERIOD);
        _avTable.rm(SpMichelleCalConstants.ATTR_MUST_IDLES);
        _avTable.rm(SpMichelleCalConstants.ATTR_NULL_CYCLES);
        _avTable.rm(SpMichelleCalConstants.ATTR_NULL_EXPOSURES);
        _avTable.rm(SpMichelleCalConstants.ATTR_NULL_READS);
        _avTable.rm(SpMichelleCalConstants.ATTR_DUTY_CYCLE);
        _avTable.rm(SpMichelleCalConstants.ATTR_CHOP_FREQUENCY);
        _avTable.rm(SpMichelleCalConstants.ATTR_CHOP_DELAY);
        _avTable.rm(SpMichelleCalConstants.ATTR_COADDS);
        _avTable.rm(SpMichelleCalConstants.ATTR_FLAT_SOURCE);
        _avTable.rm(SpMichelleCalConstants.ATTR_SAMPLING);
        _avTable.rm(SpMichelleCalConstants.ATTR_OBSERVATION_TIME);

        if (getCalType() == FLAT) {
            setObservationTime(
                    SpMichelleCalConstants.DEFAULT_FLAT_OBSERVATION_TIME);
        } else if (getCalType() == ARC) {
            setObservationTime(
                    SpMichelleCalConstants.DEFAULT_ARC_OBSERVATION_TIME);
        }
    }

    /**
     * Update the daconf for an Object/Sky observatioon
     */
    public void updateDAConf() {
        SpInstMichelle inst = (SpInstMichelle) getInstrumentItem();

        if (getCalType() == FLAT) {
            inst.setFlatExpTime(getExposureTime());
            inst.setFlatObservationTime(Double.valueOf(getObservationTime()));
            inst.updateDAFlatConf();

        } else if (getCalType() == ARC) {
            inst.setArcExpTime(getExposureTime());
            inst.setArcObservationTime(Double.valueOf(getObservationTime()));
            inst.updateDAArcConf();
        }

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
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_MODE, W_mode, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_WAVEFORM,
                W_waveform, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_NREADS,
                Integer.toString(W_nreads), 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_NRESETS,
                Integer.toString(W_nresets), 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_RESET_DELAY,
                Double.toString(W_resetDelay), 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_READ_INTERVAL,
                Double.toString(W_readInterval), 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_IDLE_PERIOD,
                Double.toString(W_idlePeriod), 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_MUST_IDLES,
                Integer.toString(W_mustIdles), 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_NULL_CYCLES,
                Integer.toString(W_nullCycles), 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_NULL_EXPOSURES,
                Integer.toString(W_nullExposures), 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_NULL_READS,
                Integer.toString(W_nullReads), 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_DUTY_CYCLE,
                Double.toString(W_dutyCycle), 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_CHOP_FREQUENCY,
                W_chopFrequency, 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_CHOP_DELAY,
                Double.toString(W_chopDelay), 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_OBSERVATION_TIME,
                Double.toString(W_obsTime), 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_FILTER,
                getFilter(), 0);
        _avTable.noNotifySet(SpMichelleCalConstants.ATTR_SAMPLING,
                getSampling(), 0);
    }

    public void translateProlog(Vector<String> v) {
    }

    public void translateEpilog(Vector<String> v) {
    }

    public void translate(Vector<String> v)
            throws SpTranslationNotSupportedException {
        // Find recipe and instrument.
        SpDRRecipe recipe = (SpDRRecipe) SpTreeMan.findDRRecipe(this);
        SpInstMichelle inst;
        try {
            inst = (SpInstMichelle) SpTreeMan.findInstrument(this);
        } catch (Exception e) {
            throw new SpTranslationNotSupportedException(
                "Non-Michelle instrument in scope");
        }
        if (inst == null) {
            throw new SpTranslationNotSupportedException(
                "No instrument in scope");
        }
        Hashtable<String, String> config = inst.getConfigItems();

        if (recipe != null) {
            if (getCalType() == FLAT) {
                v.add("setHeader GRPMEM "
                        + (recipe.getFlatInGroup() ? "T" : "F"));
                v.add("setHeader RECIPE "
                        + recipe.getFlatRecipeName());
            } else if (getCalType() == ARC) {
                v.add("setHeader GRPMEM "
                        + (recipe.getArcInGroup() ? "T" : "F"));
                v.add("setHeader RECIPE "
                        + recipe.getArcRecipeName());
            }
        }

        try {
            ConfigWriter.getCurrentInstance().write(config);
        } catch (IOException ioe) {
            throw new SpTranslationNotSupportedException(
                    "Unable to write MichelleCalObs config file");
        }

        v.add("loadConfig "
                + ConfigWriter.getCurrentInstance().getCurrentName());
        v.add("setrotator " + config.get("posAngle"));

        if (getCalType() == FLAT) {
            v.add("set FLAT");
            v.add("break");
            v.add("do " + getCount() + " _observe");
        } else if (getCalType() == ARC) {
            // TODO: implement translation for this calibration type.
            throw new SpTranslationNotSupportedException(
                    "Translation of ARC not yet supported for Michelle");
        } else {
            throw new SpTranslationNotSupportedException(
                    "Michelle calibration type not recognised");
        }

        // Copy the load instruction for the original config.
        TranslationUtils.copyFirstLoadConfig(v, false);
    }
}
