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

import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.inst.SpInstSCUBA2;
import orac.jcmt.util.Scuba2Time;
import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpTreeMan;

import gemini.sp.obsComp.SpInstObsComp;

/**
 * Pointing Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SpIterPointingObs extends SpIterJCMTObsInBeamChoice {
    /**
     * Pointing pixel choices.
     *
     * To be decided.
     */
    public static String[] POINTING_PIXEL_MANUAL_CHOICES = {"1", "..."};
    public static String[] POINTING_METHODS = {
            "9 Position",
            "16 Position",
            "25 Position",
    };
    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "pointingObs", "Pointing");
    private Scuba2Time s2time = null;

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterPointingObs());
    }

    /**
     * Default constructor.
     */
    public SpIterPointingObs() {
        super(SP_TYPE);
        _avTable.noNotifySet(ATTR_AUTOMATIC_TARGET, "true", 0);
    }

    public String getSpectralMode() {
        return _avTable.get(ATTR_SPECTRAL_MODE);
    }

    public void setSpectralMode(String value) {
        _avTable.set(ATTR_SPECTRAL_MODE, value);
    }

    public String getPointingPixel() {
        return _avTable.get(ATTR_POINTING_PIXEL);
    }

    public void setPointingPixel(String value) {
        _avTable.set(ATTR_POINTING_PIXEL, value);
    }

    public double getElapsedTime() {
        SpInstObsComp instrument = SpTreeMan.findInstrument(this);
        double overhead = 0.0;
        double totalIntegrationTime = 0.0;

        if (instrument instanceof SpInstHeterodyne) {
            totalIntegrationTime = 120.0;
        } else if (instrument instanceof SpInstSCUBA2) {
            totalIntegrationTime = SCUBA2_STARTUP_TIME;

            if (s2time == null) {
                s2time = new Scuba2Time();
            }

            totalIntegrationTime = s2time.totalIntegrationTime(this);
        }
        return (overhead + totalIntegrationTime);
    }

    public void setupForSCUBA2() {
        _avTable.noNotifyRm(ATTR_SWITCHING_MODE);
        _avTable.noNotifyRm(ATTR_POINTING_METHOD);
        _avTable.noNotifyRm(ATTR_SECS_PER_CYCLE);
        _avTable.noNotifyRm(ATTR_SPECTRAL_MODE);
    }

    public String[] getSwitchingModeOptions() {
        return new String[]{SWITCHING_MODE_BEAM};
    }
}
