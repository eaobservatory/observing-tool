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

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpTreeMan;

import gemini.sp.obsComp.SpInstObsComp;

import gemini.util.Format;

/**
 * Jiggle Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SpIterJiggleObs extends SpIterJCMTObs {

    private final String JIGGLE_SCALE_DEFAULT = "10.0";
    private final String JIGGLES_PER_CYCLE_DEFAULT = "1";
    private final String SECS_PER_CYCLE_DEFAULT = "10";

    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "jiggleObs", "Jiggle");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterJiggleObs());
    }

    /**
     * Default constructor.
     */
    public SpIterJiggleObs() {
        super(SP_TYPE);

        _avTable.noNotifySet(ATTR_JIGGLE_PA, "0.0", 0);
        _avTable.noNotifySet(ATTR_JIGGLE_SYSTEM, JIGGLE_SYSTEMS[0], 0);
    }

    public int getNumberOfPoints() {
        int numberOfPoints = 0;
        String jigglePattern = getJigglePattern();

        if (jigglePattern != null) {
            jigglePattern = jigglePattern.toLowerCase();

            int steps = 0;
            if (jigglePattern.matches("\\d+x\\d+")) {
                String[] split = jigglePattern.split("x");
                steps = Integer.parseInt(split[0]);

            } else if (jigglePattern.matches("harp\\d")) {
                String[] split = jigglePattern.split("harp");
                steps = Integer.parseInt(split[1]);

            } else if (jigglePattern.matches("harp\\d_\\w*")) {
                String split = jigglePattern.substring(4, 5);
                steps = Integer.parseInt(split);
            }

            switch (steps) {
                // 2X1 jiggle map
                case 2:
                    numberOfPoints = 2;
                    break;

                // 3X3 jiggle map
                case 3:
                    numberOfPoints = 9;
                    break;

                // 4x4 jiggle
                case 4:
                    numberOfPoints = 16;
                    break;

                // 5X5 jigle map
                case 5:
                    numberOfPoints = 25;
                    break;

                // 7X7 jigle map
                case 7:
                    numberOfPoints = 49;
                    break;

                // 9X9 jigle map
                case 9:
                    numberOfPoints = 81;
                    break;

                case 11:
                    numberOfPoints = 121;
                    break;

                // default
                default:
                    break;
            }
        }
        return numberOfPoints;
    }

    public double getElapsedTime() {
        SpInstObsComp instrument = SpTreeMan.findInstrument(this);

        // Actual integration time on source
        double totalIntegrationTime = 0.0;

        if (instrument != null) {
            int npts = getNumberOfPoints();
            // from http://www.jach.hawaii.edu/software/jcmtot/het_obsmodes.html
            // 2007-07-12
            String switchingMode = getSwitchingMode();

            // SeparateOffs = ! Shared offs
            if (SWITCHING_MODE_BEAM.equals(switchingMode)) {
                //Jiggle Chops
                if (isContinuum() || hasSeparateOffs()) {
                    totalIntegrationTime = 2.3 * npts * getSecsPerCycle()
                            + 100.0;
                } else {
                    totalIntegrationTime = 1.27
                            * ((npts + Math.sqrt((double) npts))
                                    * getSecsPerCycle())
                            + 100.0;
                }
            } else if (SWITCHING_MODE_POSITION.equals(switchingMode)) {
                // Jiggle Position Switch
                if (isContinuum() || hasSeparateOffs()) {
                    totalIntegrationTime = 2.45 * npts * getSecsPerCycle()
                            + 80.0;
                } else {
                    totalIntegrationTime = 1.75 * npts * getSecsPerCycle()
                            + 80.0;
                }
            } else {
                // Anything else
                totalIntegrationTime = 2.12 * (npts * getSecsPerCycle());
            }

            if (isContinuum())
                totalIntegrationTime *= 1.2;
        }
        return totalIntegrationTime;
    }

    public String getJigglePattern() {
        return _avTable.get(ATTR_JIGGLE_PATTERN);
    }

    public void setJigglePattern(String value) {
        _avTable.set(ATTR_JIGGLE_PATTERN, value);
    }

    /**
     * Get area position angle (map position angle).
     */
    public double getPosAngle() {
        return _avTable.getDouble(ATTR_JIGGLE_PA, 0.0);
    }

    /**
     * Set area postition angle (map postition angle).
     */
    public void setPosAngle(double theta) {
        _avTable.set(ATTR_JIGGLE_PA, Math.rint(theta * 10.0) / 10.0);
    }

    /**
     * Set area position angle (map position angle).
     */
    public void setPosAngle(String thetaStr) {
        setPosAngle(Format.toDouble(thetaStr));
    }

    /**
     * Get map coordinate system.
     */
    public String getCoordSys() {
        return _avTable.get(ATTR_JIGGLE_SYSTEM);
    }

    /**
     * Set map coordinate system.
     */
    public void setCoordSys(String coordSys) {
        _avTable.set(ATTR_JIGGLE_SYSTEM, coordSys);
    }

    public void setScaleFactor(double value) {
        _avTable.set(ATTR_SCALE_FACTOR, value);
    }

    private void setScaleFactor(String value) {
        double dVal = 1.0;
        try {
            dVal = Double.parseDouble(value);
        } catch (NumberFormatException nfe) {
        }

        setScaleFactor(dVal);
    }

    public double getScaleFactor() {
        return _avTable.getDouble(ATTR_SCALE_FACTOR, 1.0);
    }

    public void setJigglePA(double value) {
        _avTable.set(ATTR_JIGGLE_PA, value);
    }

    public double getJigglePA() {
        return _avTable.getDouble(ATTR_JIGGLE_PA, 0.0);
    }

    public void setAcsisDefaults() {
        setContinuumMode(false);
        setScaleFactor(JIGGLE_SCALE_DEFAULT);
        setJigglesPerCycle(JIGGLES_PER_CYCLE_DEFAULT);
    }

    public void setupForHeterodyne() {
        String value;

        // Scale Factor
        value = _avTable.get(ATTR_SCALE_FACTOR);
        if (value == null || value.equals("")) {
            _avTable.noNotifySet(ATTR_SCALE_FACTOR, JIGGLE_SCALE_DEFAULT, 0);
        }

        // Jiggles/Cyle
        value = _avTable.get(ATTR_JIGGLES_PER_CYCLE);
        if (value == null || value.equals("")) {
            _avTable.noNotifySet(ATTR_JIGGLES_PER_CYCLE,
                    JIGGLES_PER_CYCLE_DEFAULT, 0);
        }

        // continuum mode
        value = _avTable.get(ATTR_CONTINUUM_MODE);
        if (value == null || value.equals("")) {
            _avTable.noNotifySet(ATTR_CONTINUUM_MODE, "false", 0);
        }

        // Seconds per cycle
        value = _avTable.get(ATTR_SECS_PER_CYCLE);
        if (value == null || value.equals("")) {
            _avTable.noNotifySet(ATTR_SECS_PER_CYCLE, SECS_PER_CYCLE_DEFAULT,
                    0);
        }

        // Jiggle PA
        value = _avTable.get(ATTR_JIGGLE_PA);
        if (value == null || value.equals("")) {
            _avTable.noNotifySet(ATTR_JIGGLE_PA, "0.0", 0);
        }

        super.setupForHeterodyne();
    }

    public void setupForSCUBA2() {
        _avTable.noNotifyRm(ATTR_SCALE_FACTOR);
        _avTable.noNotifyRm(ATTR_JIGGLES_PER_CYCLE);
        _avTable.noNotifyRm(ATTR_CONTINUUM_MODE);
        _avTable.noNotifyRm(ATTR_SECS_PER_CYCLE);
        _avTable.noNotifyRm(ATTR_JIGGLE_PA);

        super.setupForSCUBA2();
    }

    public String[] getSwitchingModeOptions() {
        return new String[]{
                SWITCHING_MODE_POSITION,
                SWITCHING_MODE_BEAM,
                SWITCHING_MODE_FREQUENCY_S,
                SWITCHING_MODE_FREQUENCY_F,
        };
    }

    public void setSeparateOffs(boolean enable) {
        _avTable.set(SEPARATE_OFFS, enable);
    }

    public boolean hasSeparateOffs() {
        return _avTable.getBool(SEPARATE_OFFS);
    }

    public void rmSeparateOffs() {
        _avTable.noNotifyRm(SEPARATE_OFFS);
    }
}
