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

package edfreq ;

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
public interface HeterodyneEditor
{

	/** No identifiable speactral line indicator */
	public static final String NO_LINE = "No Line" ;

	/** Get the front end instrument name */
	public String getFeBand() ;

	/**
     * Get mode: single side band (ssb), double side band (dsb).
     */
	public String getMode() ;

	/**
     * Get the current redshift of the source being observed.
     * 
     * @return Z
     */
	public double getRedshift() ;

	/**
     * Calculates the rest frequency corresponding to the current IF of a
     * specified subsystem.
     * 
     * The current IF of a subsystem is the IF at the centre of the sideband
     * (i.e. <tt>{@link orac.jcmt.inst.SpInstHeterodyne#getCentreFrequency(int)
	 *  SpInstHeterodyne.getCentreFrequency(subsystem)}</tt>)
     * <p>
     * 
     * The calculation is based the current values for redshift, lo1 and the
     * subsystem's centre frequency.
     * 
     * @return rest frequency in GHz
     */
	public double getRestFrequency( int subsystem ) ;

	/**
     * Calculates the observe frequency corresponding to the centre of the
     * sideband of a specified subsystem.
     * 
     * The calculation is based the current values for redshift, lo1 and the
     * subsystem's centre frequency.
     * 
     * @return observe frequency in GHz
     */
	public double getObsFrequency( int subsystem ) ;

	/**
     * Gets the bandwidth of the specified subsystem
     * 
     * @param subsystem
     *            The subsystem number (starting at 0)
     * @return The bandwidth in Hz
     */
	public double getCurrentBandwidth( int subsystem ) ;

	/**
     * Update the central frequency of a subsystem
     * 
     * @param centre
     *            The central frequency in Hz
     * @param subsystem
     *            The subsystem number (starting at 0)
     */
	public void updateCentreFrequency( double centre , int subsystem ) ;

	/**
     * Update the bandwidth of a subsystem
     * 
     * @param width
     *            The bandwidth in Hz
     * @param subsystem
     *            The subsystem number (starting at 0)
     */
	public void updateBandWidth( double width , int subsystem ) ;

	/**
     * Update the channels of a subsystem
     * 
     * @param channels
     *            The number of channels allocated to the subsystem
     * @param subsystem
     *            The subsystem number (starting at 0)
     */
	public void updateChannels( int channels , int subsystem ) ;

	/**
     * Update the line details associated with a channel
     * 
     * @see edfreq.LineDetails
     * @param lineDetails
     *            The new line details
     * @param subsystem
     *            The subsystem number (starting at 0)
     */
	public void updateLineDetails( LineDetails lineDetails , int subsystem ) ;

	/**
     * Update the local oscillator frequency
     * 
     * @param lo1
     *            The local oscillator frequency in Hz
     */
	public void updateLO1( double lo1 ) ;
}
