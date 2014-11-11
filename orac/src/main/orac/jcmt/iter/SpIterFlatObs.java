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

package orac.jcmt.iter;

import orac.jcmt.inst.SpInstSCUBA2;
import gemini.sp.SpFactory;
import gemini.sp.SpTreeMan;
import gemini.sp.SpType;
import gemini.sp.obsComp.SpInstObsComp;

/**
 * Focus Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SpIterFlatObs extends SpIterJCMTObs {

    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "flatObs", "Flat Field");
    private String[] FLAT_SOURCES = null;

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterFlatObs());
    }

    /**
     * Default constructor.
     */
    public SpIterFlatObs() {
        super(SP_TYPE);
    }

    public double getElapsedTime() {
        return 0.0;
    }

    /** Get the flat source. */
    public String getFlatSource() {
        return _avTable.get(ATTR_FLAT_SOURCE);
    }

    /**
     * Set noise source to one of FLAT_SOURCES.
     *
     * @param noiseSource if this is not one of the FLAT_SOURCES then it will
     *                    be ignored.
     *
     * @see orac.jcmt.SpJCMTConstants#FLAT_SOURCES
     */
    public void setFlatSource(String flatSource) {
        if (FLAT_SOURCES != null) {
            for (int i = 0; i < FLAT_SOURCES.length; i++) {
                if (FLAT_SOURCES[i].equals(flatSource)) {
                    _avTable.set(ATTR_FLAT_SOURCE, FLAT_SOURCES[i]);
                }
            }
        }
    }

    public void setupForHeterodyne() {
        _avTable.noNotifyRm(ATTR_FLAT_SOURCE);
    }

    public void setupForSCUBA2() {
        checkSources(SCUBA2_FLAT_SOURCES);
    }

    private void checkSources(String[] sources) {
        if (sources != null && FLAT_SOURCES != sources) {
            _avTable.noNotifyRm(ATTR_FLAT_SOURCE);
            FLAT_SOURCES = sources;
            _avTable.noNotifySet(ATTR_FLAT_SOURCE, FLAT_SOURCES[0], 0);
        }
    }

    public String[] getFlatSources() {
        if (FLAT_SOURCES == null) {
            SpInstObsComp instrument = SpTreeMan.findInstrument(this);

            if (instrument instanceof SpInstSCUBA2) {
                FLAT_SOURCES = SCUBA2_FLAT_SOURCES;
            }
        }

        return FLAT_SOURCES;
    }
}
