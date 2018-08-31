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

import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import gemini.sp.SpType;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpStareCapability;

import java.util.Vector;
import gemini.util.JACLogger;

/**
 * The base class for Cal Unit observes (FLATS and ARCS) and for BIAS and DARK
 * and generic Observe.
 *
 * It defines attributes for the observe count, the exposure time, and the
 * number of coadds. It also serves as a marker for these classes.
 */
@SuppressWarnings("serial")
public abstract class SpIterObserveBase extends SpIterComp {
    public static final String ATTR_COUNT = "repeatCount";
    public static final String ATTR_EXPOSURE_TIME = "exposureTime";
    public static final String ATTR_COADDS = "coadds";
    public static final int COUNT_DEF = 1;

    protected JACLogger logger = JACLogger.getLogger(SpIterObserveBase.class);

    /**
     * Default constructor.
     */
    public SpIterObserveBase(SpType spType) {
        super(spType);

        _avTable.noNotifySet(ATTR_COUNT, String.valueOf(COUNT_DEF), 0);
    }

    /**
     * Get the number of desired repetitions.
     */
    public int getCount() {
        return _avTable.getInt(ATTR_COUNT, COUNT_DEF);
    }

    /**
     * Set the number of repetitions.
     */
    public void setCount(int i) {
        _avTable.set(ATTR_COUNT, i);
    }

    /**
     * Set the number of repetitions from a string.
     */
    public void setCount(String s) {
        _avTable.set(ATTR_COUNT, s);
    }

    /**
     * Inherit default values for exposure time and coadds.
     */
    private void _inheritValues() {
        boolean expTimeSet = false;
        boolean coaddsSet = false;

        double expTime = 0.0;
        int coadds = 1;

        // Look for an instrument iterator
        SpItem parent = parent();
        while (parent instanceof SpIterComp) {
            if (parent instanceof SpIterConfigObs) {
                Vector<String> v;

                // Try to inherit exposure times
                if (!expTimeSet) {
                    v = ((SpIterConfigObs) parent).getExposureTimes();

                    if (v != null) {
                        // Just pick the first one in the list
                        String expTimeStr = v.elementAt(0);

                        try {
                            expTime = Double.valueOf(expTimeStr);
                        } catch (Exception ex) {
                        }

                        expTimeSet = true;
                    }
                }

                // Try to inherit coadds
                if (!coaddsSet) {
                    v = ((SpIterConfigObs) parent).getCoadds();

                    if (v != null) {
                        // Just pick the first one in the list
                        String coaddsStr = v.elementAt(0);

                        try {
                            coadds = Integer.parseInt(coaddsStr);
                        } catch (Exception ex) {
                        }

                        coaddsSet = true;
                    }
                }

                if (expTimeSet && coaddsSet) {
                    break;
                }
            }

            parent = parent.parent();
        }

        if (!expTimeSet || !coaddsSet) {
            // Didn't get either or both of the exposure time and coadds from
            // an iterator, so try to get it from an instrument

            SpInstObsComp ioc = SpTreeMan.findInstrument(parent);

            if (ioc == null) {
                if (!expTimeSet) {
                    expTime = 0.0;
                }

                if (!coaddsSet) {
                    coadds = 0;
                }

            } else {
                if (!expTimeSet) {
                    expTime = ioc.getExposureTime();
                }

                if (!coaddsSet) {
                    SpStareCapability stareCap;
                    String name = SpStareCapability.CAPABILITY_NAME;
                    stareCap = (SpStareCapability) ioc.getCapability(name);

                    if (stareCap != null) {
                        coadds = stareCap.getCoadds();
                    }
                }
            }
        }

        if (!_avTable.exists(ATTR_EXPOSURE_TIME)) {
            _avTable.noNotifySet(ATTR_EXPOSURE_TIME, String.valueOf(expTime),
                    0);
        }

        if (!_avTable.exists(ATTR_COADDS)) {
            _avTable.noNotifySet(ATTR_COADDS, String.valueOf(coadds),
                    0);
        }
    }

    /**
     * Get the exposure time.
     */
    public double getExposureTime() {
        // If the exposure time has been set, use it.
        if (_avTable.exists(ATTR_EXPOSURE_TIME)) {
            return _avTable.getDouble(ATTR_EXPOSURE_TIME, 0.0);
        }

        _inheritValues();

        return _avTable.getDouble(ATTR_EXPOSURE_TIME, 0.0);
    }

    /**
     * Set the exposure time.
     */
    public void setExposureTime(double expTime) {
        _avTable.set(ATTR_EXPOSURE_TIME, expTime);
    }

    /**
     * Set the exposure time from a string.
     */
    public void setExposureTime(String expTime) {
        _avTable.set(ATTR_EXPOSURE_TIME, expTime);
    }

    /**
     * Get the coadds.
     */
    public int getCoadds() {
        // If the coadds has been set, use it.
        if (_avTable.exists(ATTR_COADDS)) {
            return _avTable.getInt(ATTR_COADDS, 1);
        }

        _inheritValues();

        return _avTable.getInt(ATTR_COADDS, 1);
    }

    /**
     * Set the coadds.
     */
    public void setCoadds(int coadds) {
        _avTable.set(ATTR_COADDS, coadds);
    }

    /**
     * Set the coadds time from a string.
     */
    public void setCoadds(String coadds) {
        _avTable.set(ATTR_COADDS, coadds);
    }
}
