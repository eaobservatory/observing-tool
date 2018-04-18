/*
 * Copyright 2002 United Kingdom Astronomy Technology Centre, an
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

package edfreq;

/**
 * Heterodyne editor interface.
 *
 * This interface defines the part of the interaction between a Heterodyne
 * editor such as the EdCompInstHeterodyne class of the OT and the frequency
 * editor. If parameters are changed in the frequency editor then the update
 * methods (which can be thought of as callback functions) are called.
 * Additionally methods are provided to give the frequency editor access to
 * band, redshift, source frequency, observe frequency etc as specified in the
 * Heterodyne editor.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public interface HeterodyneEditor {

    /** No identifiable speactral line indicator */
    public static final String NO_LINE = "No Line";

    /** Get the front end instrument name */
    public String getFeBand();

    /**
     * Get mode: single side band (ssb), double side band (dsb).
     */
    public String getMode();

    /**
     * Get the current redshift of the source being observed.
     *
     * @return z
     */
    public double getRedshift();

    /**
     * Gets the bandwidth of the specified subsystem
     *
     * @param subsystem The subsystem number (starting at 0)
     *
     * @return The bandwidth in Hz
     */
    public double getCurrentBandwidth(int subsystem);
}
