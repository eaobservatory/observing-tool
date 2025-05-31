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

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpTreeMan;

import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.inst.SpInstSCUBA2;

import gemini.sp.iter.SpIterChop;
import gemini.util.Format;

/**
 * Stare Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SpIterStareObs extends SpIterJCMTObs {
    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "stareObs", "Stare");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterStareObs());
    }

    /**
     * Default constructor.
     */
    public SpIterStareObs() {
        super(SP_TYPE);
    }

    public void setWidePhotom(boolean flag) {
        _avTable.set(ATTR_WIDE_PHOTOMETRY, flag);
    }

    public boolean getWidePhotom() {
        return (_avTable.exists(ATTR_WIDE_PHOTOMETRY)
                && _avTable.getBool(ATTR_WIDE_PHOTOMETRY));
    }

    public double getElapsedTime() {
        SpInstObsComp instrument = SpTreeMan.findInstrument(this);
        double overhead = 0.0;
        double totalIntegrationTime = 0.0;

        if (instrument instanceof SpInstHeterodyne) {
            /*
             * Based on real timing data
             * http://www.jach.hawaii.edu/software/jcmtot/het_obsmodes.html
             * 2007-07-12
             *
             * Overridden in SpIterFolder, these timings are single position
             * if outside an iterator
             */
            double T_on = getSecsPerCycle();
            String switchingMode = getSwitchingMode();

            if (SWITCHING_MODE_POSITION.equals(switchingMode)) {
                totalIntegrationTime = 2.45 * T_on + 80.0;
            } else if (SWITCHING_MODE_BEAM.equals(switchingMode)) {
                totalIntegrationTime = 2.3 * T_on + 100.0;
            }
            else if (SWITCHING_MODE_FREQUENCY_S.equals(switchingMode) ||
                    SWITCHING_MODE_FREQUENCY_F.equals(switchingMode)) {
                // Based in timing test data of 20190605.
                totalIntegrationTime = 1.023 * T_on + 67.0;
            }

            if (isContinuum()) {
                totalIntegrationTime *= 1.2;
            }
        } else if (instrument instanceof SpInstSCUBA2) {
            totalIntegrationTime = getSecsPerCycle();
        }

        return (overhead + totalIntegrationTime);
    }

    public boolean insideChop() {
        return insideComponent(SpIterChop.class) != null;
    }

    public boolean insidePOL() {
        Object returned = insideComponent(SpIterPOL.class);
        return (returned != null
                && ((SpIterPOL) returned).getTable().exists("continuousSpin"));
    }

    private Object insideComponent(Class<?> component) {
        boolean inside = false;
        SpItem parent = this.parent();

        while (parent != null) {
            inside = component.isInstance(parent);

            if (inside) {
                break;
            }

            parent = parent.parent();
        }

        return parent;
    }

    public void setupForHeterodyne() {
        if (insideChop()) {
            _avTable.noNotifySet(ATTR_SWITCHING_MODE, SWITCHING_MODE_BEAM, 0);
            rmSeparateOffs();
        }

        _avTable.noNotifyRm(ATTR_SAMPLE_TIME);

        if (_avTable.get(ATTR_SWITCHING_MODE) == null
                || _avTable.get(ATTR_SWITCHING_MODE).equals("")) {
            _avTable.noNotifySet(ATTR_SWITCHING_MODE,
                    getSwitchingModeOptions()[0], 0);
        }

        if (_avTable.get(ATTR_SECS_PER_CYCLE) == null
                || _avTable.get(ATTR_SECS_PER_CYCLE).equals("")) {
            _avTable.noNotifySet(ATTR_SECS_PER_CYCLE, "60", 0);
        }
    }

    public void setupForSCUBA2() {
        _avTable.noNotifyRm(ATTR_SWITCHING_MODE);
        _avTable.noNotifyRm(ATTR_SAMPLE_TIME);
        _avTable.noNotifyRm(ATTR_CONT_CAL);
        _avTable.noNotifyRm(ATTR_WIDE_PHOTOMETRY);

        if (_avTable.get(ATTR_SECS_PER_CYCLE) == null
                || _avTable.get(ATTR_SECS_PER_CYCLE).equals("")) {
            _avTable.noNotifySet(ATTR_SECS_PER_CYCLE, "5", 0);
        }
    }

    public String[] getSwitchingModeOptions() {
        return new String[]{
                SWITCHING_MODE_BEAM,
                SWITCHING_MODE_POSITION,
                SWITCHING_MODE_FREQUENCY_F,
                SWITCHING_MODE_NONE,
        };
    }

    public void setArrayCentred(boolean enable) {
        _avTable.set(ATTR_ARRAY_CENTRED, enable);
    }

    public boolean isArrayCentred() {
        return _avTable.getBool(ATTR_ARRAY_CENTRED);
    }

    public void rmArrayCentred() {
        _avTable.noNotifyRm(ATTR_ARRAY_CENTRED);
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

    public boolean separateOffsExist() {
        return _avTable.exists(SEPARATE_OFFS);
    }

    /**
     * Get area position angle (map position angle).
     */
    public double getPosAngle() {
        return _avTable.getDouble(ATTR_STARE_PA, 0.0);
    }

    /**
     * Set area postition angle (map postition angle).
     */
    public void setPosAngle(double theta) {
        _avTable.set(ATTR_STARE_PA, Math.rint(theta * 10.0) / 10.0);
    }

    /**
     * Set area position angle (map position angle).
     */
    public void setPosAngle(String thetaStr) {
        setPosAngle(Format.toDouble(thetaStr));
    }

    /**
     * Set map coordinate system.
     */
    public void rmPosAngle() {
        _avTable.noNotifyRm(ATTR_STARE_PA);
    }

    /**
     * Get map coordinate system.
     */
    public String getCoordSys() {
        if (!_avTable.exists(ATTR_STARE_SYSTEM)) {
            setCoordSys(STARE_SYSTEMS[0]);
        }

        return _avTable.get(ATTR_STARE_SYSTEM);
    }

    /**
     * Set map coordinate system.
     */
    public void setCoordSys(String coordSys) {
        _avTable.set(ATTR_STARE_SYSTEM, coordSys);
    }

    /**
     * Set map coordinate system.
     */
    public void rmCoordSys() {
        _avTable.noNotifyRm(ATTR_STARE_SYSTEM);
    }
}
