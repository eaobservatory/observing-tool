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

package orac.jcmt.inst;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.util.Format;
import edfreq.BandSpec;
import edfreq.Receiver;

import java.util.Vector;
import java.util.List;

/**
 * The Heterodyne instrument Observation Component.
 *
 * <h3>Terminology</h3>
 * <ul>
 *   <li><b>subsystem vs subband</b><br>
 *   A subsystem is made up of one (non-hybrid mode) or more (hybrid mode)
 *   subbands.
 *   <li><b>subsystem</b><br>
 *   Corresponds to one row in the OT's frequency editor.
 *   <li><b>subband</b><br>
 *   Corresponds to the hardware.
 *   <li><b>Hybrid subsystem</b><br>
 *   A subsystem that is made up of more than one subbands (hybrid mode).
 *   <li><b>Hybrid subband</b><br>
 *   This might be a bit confusing. A "Hybrid subband" is an
 *   individual subband that contributes to a hybrid subsystem (hybrid mode).
 * </ul>
 *
 * <A NAME="bandwidthAndChannels"></A>
 * <h3>Bandwidth and Channel methods</h3>
 *
 * <tt>C=================================|=================================C</tt>
 * is the combined bandwidth as displayed in the OT. The methods
 * {@link getBandWidth(int)} and {@link getChannels(int)} refer to this
 * combined bandwidth.<p>
 *
 * <tt>H====================H</tt> is an individual subband (as in the hardware).
 * <p>
 *
 * Note that even in non-hybrid more <tt>C====C</tt> and <tt>H======H</tt> are not the
 * same as half the overlap is taken of either side of <tt>C====C</tt>.
 *
 *
 * <h4>Example 1: Hybrid mode, 4 hybrid subbands</h4>
 *
 * <pre>
 *                              {@link #getChannelsTotal(int)}
 *                                    / \
 *                                  /     \
 *                                /         \
 *                            /                 \
 *                        /                         \
 *                    /                                 \
 *                /                                         \
 *             /                                               \
 *          /                                                     \
 *        /                                                         \
 *      /                                                             \
 *    /                                                                 \
 *  /           Bandwidth and Channels as displayed by the OT             \
 * |            {@link #getBandWidth(int)}, {@link #getChannels(int)}
 * |                                                                       |
 * |                                  / \                                  |
 * |                                /     \                                |
 * |                              /         \                              |
 * |                          /                 \                          |
 * |                      /                         \                      |
 * |                  /                                 \                  |
 * |              /                                         \              |
 * |           /                                               \           |
 * |        /                                                     \        |
 * |      /                                                         \      |
 * |    /                                                             \    |
 * |  /                                                                 \  |
 * | |                                                                   | |
 * | |                                 |                                 | |
 * | |                                 |                                 | |
 * | C=================================|=================================C |
 * | |                                 |                                   |
 * H=|==================H              |                                   |
 * | |              H==================|=H                                 |
 * | |              |   |            H=|==================H                |
 * | |              |   |              |              H====================H
 * | |              |   |              |              |                    |
 * | |              |   |              |              |                    |
 * | |              |   |              |              |                    |
 * | |              |   |              |              |                    |
 * | |              |   |              |              |                    |
 * | |              |   |              |              |                    |
 * | |              |   |              |              |                    |
 * | |              |   |              |              |                    |
 * | |              |   |              |              |                    |
 * | |              |   |              |              |                    |
 * | |       {@link #getOverlap(int)}
 * | |                                 |
 * | |                                 |
 * | |                                 |
 * | |                                 |
 * | |                                 |
 * | |                   {@link #getCentreFrequency(int)}
 * | |
 * |  \_____________________________
 * |
 * | 0.5 * {@link #getOverlap(int)}
 *  \_______________________________
 *
 * </pre>
 *
 *
 * <h4>Example 2: Non-Hybrid mode, 1 "hybrid" subbands makes up the subsustem</h4>
 *
 * <pre>
 *                              {@link #getChannelsTotal(int)}
 *                                    / \
 *                                  /     \
 *                                /         \
 *                            /                 \
 *                        /                         \
 *                    /                                 \
 *                /                                         \
 *             /                                               \
 *          /                                                     \
 *        /                                                         \
 *      /                                                             \
 *    /                                                                 \
 *  /           Bandwidth and Channels as displayed by the OT             \
 * |            {@link #getBandWidth(int)}, {@link #getChannels(int)}
 * |                                                                       |
 * |                                  / \                                  |
 * |                                /     \                                |
 * |                              /         \                              |
 * |                          /                 \                          |
 * |                      /                         \                      |
 * |                  /                                 \                  |
 * |              /                                         \              |
 * |           /                                               \           |
 * |        /                                                     \        |
 * |      /                                                         \      |
 * |    /                                                             \    |
 * |  /                                                                 \  |
 * | |                                                                   | |
 * | |                                 |                                 | |
 * | |                                 |                                 | |
 * | C=================================|=================================C |
 * | |                                 |                                   |
 * H=======================================================================H
 * | |                                 |                                   |
 * | |                                 |
 * | |                                 |                                   |
 * | |                                 |
 * | |                                 |                                   |
 * | |                   {@link #getCentreFrequency(int)}
 * | |                                                                     |
 * |  \_____________________________
 * |                                                                       |
 * | 0.5 * {@link #getOverlap(int)}
 * |\_______________________________                                       |
 * |
 * |                                                                       |
 *
 * </pre>
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SpInstHeterodyne extends SpJCMTInstObsComp {

    public static double LIGHTSPEED = 2.99792458E5;

    /**
     * Index for combined spectral window (ACSIS DR XML).
     *
     * Some methods dealing with spectral windows for the ACSIS DR XML
     * take the number (or index) of an individual hybrid subsystem as argument.
     * Using SUBSYSTEM_INDEX in place of such an argument indicates that
     * this spectral window refers to non of the individual hybrid subbands but
     * to the combined spectral window which is made up of several individual
     * hybrid subbands.
     */
    protected static final int SUBSYSTEM_INDEX = -1;
    protected static final String CONFIG_LABEL_NONE = "UNDEFINED";

    // FrontEnd

    /** Receiver/Front end name. */
    public static final String ATTR_FE_NAME = "feName";

    /** Back end name */
    public static final String ATTR_BE_NAME = "beName";

    /** Special configuration identifier */
    public static final String ATTR_NAMED_CONFIGURATION = "configuration";

    /** Receiver: Central IF. */
    public static final String ATTR_FE_IF = "feIF";

    /** Receiver/Front end bandwidth. */
    public static final String ATTR_FE_BANDWIDTH = "feBandWidth";

    /** Mode: single side band (ssb), double side band (dsb)
      * or sideband separating (2sb). */
    public static final String ATTR_MODE = "mode";

    /** Band mode: 1-system, 2-system etc.  */
    public static final String ATTR_BAND_MODE = "bandMode";

    /** Mixer selection: Single or dual */
    public static final String ATTR_MIXER = "mixers";

    /** Radial velocity. */
    public static final String ATTR_VELOCITY = "velocity";
    public static final String ATTR_RF_VELOCITY = "referenceFrameVelocity";

    /** Radial velocity definition: "redshift", "optical", "radio". */
    public static final String ATTR_VELOCITY_DEFINITION = "velocityDefinition";
    public static final String ATTR_VELOCITY_FRAME = "velocityFrame";

    /** Band: upper side band (usb), lower side band (lsb), side band with
     * line in range (optimum).
     */
    public static final String ATTR_BAND = "band";

    /** Molecule. */
    public static final String ATTR_MOLECULE = "molecule";

    /** Transition. */
    public static final String ATTR_TRANSITION = "transition";

    /**
     * Rest Frequency.
     *
     * This is usually the rest frequency of a molecular transition line.
     * But it can also be used for an arbitrary user defined frequency in
     * the rest frame of the source.
     */
    public static final String ATTR_REST_FREQUENCY = "restFrequency";
    public static final String ATTR_SKY_FREQUENCY = "skyFrequency";

    /** Overlap of multiple hybrid subbands. */
    public static final String ATTR_OVERLAP = "overlap";

    // FrequencyTable

    /** Array of  */
    public static final String ATTR_CENTRE_FREQUENCY = "centreFrequency";

    /** Bandwidth reduced by overlap * number of hybrid subbands. */
    public static final String ATTR_BANDWIDTH = "bandWidth";

    /** Number of channels reduced by overlap * number of hybrid subbands. */
    public static final String ATTR_CHANNELS = "channels";

    /** Total number of channels ignoring overlap. */
    public static final String ATTR_CHANNELS_TOTAL = "channelsTotal";

    /** */
    public static final String ATTR_HYBRID_SUBBANDS = "hybridSubBands";

    public static String[] JIGGLE_PATTERNS = {
            "2x1", "3x3", "4x4", "5x5", "7x7", "9x9", "11x11",
            "HARP4", "HARP5", "HARP4_mc", "HARP5_mc",
    };

    /** Radial velocity expressed as redshift. */
    public static final String RADIAL_VELOCITY_REDSHIFT = "redshift";

    /** Radial velocity defined optically. */
    public static final String RADIAL_VELOCITY_OPTICAL = "optical";

    /** Radial velocity, radio defined. */
    public static final String RADIAL_VELOCITY_RADIO = "radio";

    /** Velocity frames */
    public static final String LSRK_VELOCITY_FRAME = "LSRK";
    public static final String GEOCENTRIC_VELOCITY_FRAME = "GEOCENTRIC";
    public static final String HELIOCENTRIC_VELOCITY_FRAME = "HELIOCENTRIC";
    public static final String BARYCENTRIC_VELOCITY_FRAME = "BARYCENTRIC";
    public static final String TOPOCENTRIC_VELOCITY_FRAME = "TOPOCENTRIC";

    /** Resolution (kHz) */
    public static final String ATTR_RESOLUTION = "resolution";

    /**
     * Set to true when method initialiseValues() is called.
     *
     * @see #initialiseValues(String,String,String,String,String,String
     *                        String,String,String,String,String,String,String)
     */
    private boolean _valuesInitialised = false;

    /**
     * Flag to indicate the Acsis configuration XML is being parsed.
     *
     * The Acsis configuration XML ({@link #XML_ELEMENT_ACSIS_CONFIGURATION})
     * is only an output format. It must be ignored when reading in
     * SpInstHeterodyne XML.
     */
    private boolean _processingAcsisConfigurationXml = false;

    int _subSystemCount = 0;

    /**
     * XML element containing the Acsis configuration.
     *
     * The XML_ELEMENT_ACSIS_CONFIGURATION String is
     * <tt>&lt;jcmt_config&gt;</tt>.
     */
    private static final String XML_ELEMENT_ACSIS_CONFIGURATION = "jcmt_config";
    private static final String XML_ELEMENT_ACSIS_SUBSYSTEMS = "subsystems";
    private static final String XML_ELEMENT_ACSIS_SUBSYSTEM = "subsystem";
    private static final String XML_ELEMENT_ACSIS_LINE = "line";

    public static final SpType SP_TYPE = SpType.create(
            SpType.OBSERVATION_COMPONENT_TYPE, "inst.Heterodyne", "Het Setup");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpInstHeterodyne());
    }

    public SpInstHeterodyne() {
        super(SP_TYPE);
    }

    /**
     * Initialises the values fo this item.
     *
     * Most of the default values (or allowed values) for this item are
     * specified in the configuration files EDFREQ/cfg/edfreq/acsisCfg.xml
     * and EDFREQ/cfg/edfreq/dasCfg.xml The Heterodyne Editor class
     * {@link jcmt.inst.editor.EdCompInstHeterodyne}. Wheb the
     * @link jcmt.inst.editor.EdCompInstHeterodyne#_updateWidgets()}
     * method of is called for the first time then it can check whether the
     * values of this SpInstHeterodyne item have been initialised (by calling
     * @link #valuesInitialised()}). If they have not then the
     * initialiseValues() can be used to fill in the values from the above
     * configuration files.
     */
    public void initialiseValues(String defaultBeName, String defaultFeName,
            String defaultMode, String defaultBandMode, String defaultMixer,
            String defaultOverlap, String defaultBand,
            String defaultCentreFrequency, String frontendBandwidth,
            String defaultBandwidth, String defaultMolecule,
            String defaultTransition, String defaultRestFrequency) {
        _avTable.noNotifySet(ATTR_BE_NAME, defaultBeName, 0);
        _avTable.noNotifySet(ATTR_FE_NAME, defaultFeName, 0);
        _avTable.noNotifySet(ATTR_MODE, defaultMode, 0);
        _avTable.noNotifySet(ATTR_BAND_MODE, defaultBandMode, 0);
        _avTable.noNotifySet(ATTR_MIXER, defaultMixer, 0);
        _avTable.noNotifySet(ATTR_OVERLAP, defaultOverlap, 0);
        _avTable.noNotifySet(ATTR_BAND, defaultBand, 0);
        _avTable.noNotifySet(ATTR_CENTRE_FREQUENCY, defaultCentreFrequency, 0);
        _avTable.noNotifySet(ATTR_FE_IF, defaultCentreFrequency, 0);
        _avTable.noNotifySet(ATTR_FE_BANDWIDTH, frontendBandwidth, 0);
        _avTable.noNotifySet(ATTR_BANDWIDTH, defaultBandwidth, 0);
        _avTable.noNotifySet(ATTR_MOLECULE, defaultMolecule, 0);
        _avTable.noNotifySet(ATTR_TRANSITION, defaultTransition, 0);
        _avTable.noNotifySet(ATTR_REST_FREQUENCY, defaultRestFrequency, 0);

        _valuesInitialised = true;
    }

    /**
     * Indicates whether the method initialiseValues() has been called.
     *
     * @see #initialiseValues(String, String, String, String, String, String,
     *                        String, String, String, String, String, String,
     *                        String, String, String, String)
     */
    public boolean valuesInitialised() {
        return _valuesInitialised;
    }

    /**
     * Appends front end name to title.
     */
    public String getTitle() {
        String frontEndName = getTable().get(ATTR_FE_NAME);

        if (frontEndName == null) {
            return super.getTitle();
        } else {
            return super.getTitle() + " (" + frontEndName + ")";
        }
    }

    /**
     * Get jiggle pattern options.
     *
     * @return String array of jiggle pattern options.
     */
    public String[] getJigglePatterns() {
        return JIGGLE_PATTERNS;
    }

    /** Not properly implemented yet.
     *
     * @return 2.5
     */
    public double getDefaultScanVelocity() {
        return 2.5;
    }

    /** Not properly implemented yet.
     *
     * @return 10.0
     */
    public double getDefaultScanDy() {
        return 10.0;
    }

    /**
     * Get front end name.
     */
    public String getFrontEnd() {
        if (_avTable.get(ATTR_FE_NAME) == null
                || _avTable.get(ATTR_FE_NAME).equals("")) {
            _avTable.noNotifySet(ATTR_FE_NAME, "Uu", 0);
        }

        return _avTable.get(ATTR_FE_NAME);
    }

    /**
     * Set front end name.
     */
    public void setFrontEnd(String value) {
        _avTable.set(ATTR_FE_NAME, value);
    }

    /** Get the back end name */
    public String getBackEnd() {
        if (_avTable.get(ATTR_BE_NAME) == null
                || _avTable.get(ATTR_BE_NAME).equals("")) {
            _avTable.noNotifySet(ATTR_BE_NAME, "acsis", 0);
        }

        return _avTable.get(ATTR_BE_NAME);
    }

    /**
     * Get Receiver/Front end central IF.
     */
    public double getFeIF() {
        return _avTable.getDouble(ATTR_FE_IF, 0.0);
    }

    /**
     * Set Receiver/Front end central IF.
     */
    public void setFeIF(double value) {
        _avTable.set(ATTR_FE_IF, value);
    }

    /**
     * Set Receiver/Front end central IF.
     */
    public void setFeIF(String value) {
        setFeIF(Format.toDouble(value));
    }

    /**
     * Get Receiver/Front end bandwidth.
     */
    public double getFeBandWidth() {
        return _avTable.getDouble(ATTR_FE_BANDWIDTH, 0.0);
    }

    /**
     * Set Receiver/Front end bandwidth.
     */
    public void setFeBandWidth(double value) {
        _avTable.set(ATTR_FE_BANDWIDTH, value);
    }

    /**
     * Set Receiver/Front end bandwidth.
     */
    public void setFeBandWidth(String value) {
        setFeBandWidth(Format.toDouble(value));
    }

    /**
     * Get mode: single side band (ssb), double side band (dsb)
     * or sideband separating (2sb).
     */
    public String getMode() {
        if (_avTable.get(ATTR_MODE) == null
                || _avTable.get(ATTR_MODE).equals("")) {
            _avTable.noNotifySet(ATTR_MODE, "ssb", 0);
        }

        return _avTable.get(ATTR_MODE);
    }

    /**
     * Set  mode: single side band (ssb), double side band (dsb)
     * or sideband separating (2sb).
     */
    public void setMode(String value) {
        _avTable.set(ATTR_MODE, value);
    }

    /**
     * Get band mode: 1-system, 2-system etc.
     */
    public String getBandMode() {
        return _avTable.get(ATTR_BAND_MODE);
    }

    /**
     * Set band mode: 1-system, 2-system etc.
     */
    public void setBandMode(String value) {
        _avTable.set(ATTR_BAND_MODE, value);
    }

    /**
     * Get velocity definition.
     */
    public String getVelocityDefinition() {
        return _avTable.get(ATTR_VELOCITY_DEFINITION);
    }

    /**
     * Set velocity definition.
     */
    public void setVelocityDefinition(String value) {
        _avTable.set(ATTR_VELOCITY_DEFINITION, value);
    }

    public void setVelocityFrame(String value) {
        _avTable.set(ATTR_VELOCITY_FRAME, value);
    }

    public String getVelocityFrame() {
        return _avTable.get(ATTR_VELOCITY_FRAME);
    }

    /**
     * Get velocity (optical definition).
     */
    public double getVelocity() {
        return _avTable.getDouble(ATTR_VELOCITY, 0.0);
    }

    /**
     * Set velocity (optical definition).
     */
    public void setVelocity(double value) {
        _avTable.set(ATTR_VELOCITY, value);
    }

    /**
     * Set velocity (optical definition).
     */
    public void setVelocity(String value) {
        setVelocity(Format.toDouble(value));
    }

    /**
     * Set the reference frame velocity
     */
    public void setRefFrameVelocity(double value) {
        _avTable.set(ATTR_RF_VELOCITY, value);
    }

    /**
     * Set the reference frame velocity
     */
    public void setRefFrameVelocity(String value) {
        setRefFrameVelocity(Format.toDouble(value));
    }

    public double getRefFrameVelocity() {
        return _avTable.getDouble(ATTR_RF_VELOCITY, 0.0);
    }

    /**
     * Get the mixer mode
     */
    public String getMixer() {
        return _avTable.get(ATTR_MIXER);
    }

    /**
     * Set the mixer mode
     */
    public void setMixer(String value) {
        _avTable.set(ATTR_MIXER, value);
    }

    /**
     * Get band: upper side band (usb), lower side band (lsb), side band with
     * line in range (optimum).
     */
    public String getBand() {
        return _avTable.get(ATTR_BAND);
    }

    /**
     * Set band: upper side band (usb), lower side band (lsb), side band with
     * line in range (optimum).
     */
    public void setBand(String value) {
        _avTable.set(ATTR_BAND, value);
    }

    /**
     * Get molecule of specified subsystem.
     *
     * @param subsystem Number of subsystem (starting at 0).
     * @return  The name of the molecule species currently set for the
     *          specified subsystem
     */
    public String getMolecule(int subsystem) {
        String returnString = _avTable.get(ATTR_MOLECULE, subsystem);
        return returnString;
    }

    /**
     * Set molecule of specified subsystem.
     *
     * @param value  The name of the molecule species observed in this
     *               subsystem
     * @param subsystem Number of subsystem (starting at 0).
     */
    public void setMolecule(String value, int subsystem) {
        _avTable.set(ATTR_MOLECULE, value, subsystem);
    }

    /**
     * Get transition of specified subsystem.
     *
     * @param subsystem  Subsystem number (starting at 0).
     * @return  The molecular transistion currently set for the specified
     *          subsystem
     */
    public String getTransition(int subsystem) {
        return _avTable.get(ATTR_TRANSITION, subsystem);
    }

    /**
     * Set transition of specified subsystem.
     *
     * @param value  The transition of the molecule species observed in this
     *               subsystem
     * @param subsystem  Subsystem number (starting at 0).
     */
    public void setTransition(String value, int subsystem) {
        _avTable.set(ATTR_TRANSITION, value, subsystem);
    }

    /**
     * Get rest frequency for specified subsystem.
     *
     * This is usually the rest frequency of a molecular transition line.
     * If the heterodyne editor components allow specifying arbitrary
     * frequencies in the rest frame of the source instead of a line rest
     * frequency then these frequencies would be returned by this method.
     * Currently an arbitrary frequency can only be specified for the
     * top subsystem (subsystem 0). All the other ones can only have
     * line frequencies taken form the LineCatalog provided with
     * the Frequency Editor.
     *
     * For subsystems other than the top one (subsystem 0) the rest frequency
     * is only used for data reduction purposes. The rest frequency of the
     * top subsystem is also used to recalculate the LO1 at the time
     * of the observation.
     *
     * @param subsystem  Subsystem number (starting at 0).
     */
    public double getRestFrequency(int subsystem) {
        double restFreq = _avTable.getDouble(ATTR_REST_FREQUENCY, subsystem,
                                             0.0);
        return restFreq;
    }

    /**
     * Set rest frequency for specified subsystem.
     *
     * @see #getRestFrequency(int)
     * @param subsystem  Subsystem number (starting at 0).
     */
    public void setRestFrequency(double value, int subsystem) {
        _avTable.set(ATTR_REST_FREQUENCY, value, subsystem);
    }

    /**
     * Set rest frequency for specified subsystem.
     *
     * @see #getRestFrequency(int)
     * @param subsystem  Subsystem number (starting at 0).
     */
    public void setRestFrequency(String value, int subsystem) {
        setRestFrequency(Format.toDouble(value), subsystem);
    }

    /**
     * Get the sky frequency.  Currently only the prime
     * value is stored - that relating to the rest frequency of
     * subsystem 0
     */
    public double getSkyFrequency() {
        return _avTable.getDouble(ATTR_SKY_FREQUENCY, getRestFrequency(0));
    }

    /**
     * Set the Sky frequency for the 0th subsystem
     */
    public void setSkyFrequency(double value) {
        _avTable.set(ATTR_SKY_FREQUENCY, value);
    }

    /**
     * Get centre frequency (IF) of specified subsystem.
     *
     * @param Number of subsystems (starting at 0).
     *
     * @see <a href="#bandwidthAndChannels">Bandwidth and Channel methods</a>
     * @param subsystem  Subsystem number (starting at 0).
     */
    public double getCentreFrequency(int subsystem) {
        return _avTable.getDouble(ATTR_CENTRE_FREQUENCY, subsystem, 0.0);
    }

    /**
     * Set centre frequency (IF) of specified subsystem.
     *
     * @param subsystem  Subsystem number (starting at 0).
     */
    public void setCentreFrequency(double value, int subsystem) {
        _avTable.set(ATTR_CENTRE_FREQUENCY, value, subsystem);
    }

    /**
     * Set centre frequency (IF) of specified subsystem.
     *
     * @param subsystem  Subsystem number (starting at 0).
     */
    public void setCentreFrequency(String value, int subsystem) {
        setCentreFrequency(Format.toDouble(value), subsystem);
    }

    /**
     * Get bandwidth of specified subsystem.
     *
     * @param Number of subsystems (starting at 0).
     *
     * @see <A HREF="#bandwidthAndChannels">Bandwidth and Channel methods</A>
     * @param subsystem Subsystem number (starting at 0).
     */
    public double getBandWidth(int subsystem) {
        double bandwidth = _avTable.getDouble(ATTR_BANDWIDTH, subsystem, 0.0);
        return bandwidth;
    }

    /**
     * Set bandwidth of specified subsystem.
     *
     * @param Number of subsystems (starting at 0).
     *
     * @param subsystem Subsystem number (starting at 0).
     */
    public void setBandWidth(double value, int subsystem) {
        _avTable.set(ATTR_BANDWIDTH, value, subsystem);
    }

    /**
     * Set bandwidth of specified subsystem.
     *
     * @param Number of subsystems (starting at 0).
     *
     * @param subsystem  Subsystem number (starting at 0).
     */
    public void setBandWidth(String value, int subsystem) {
        setBandWidth(Format.toDouble(value), subsystem);
    }

    /**
     * Get number of hybrid subbands that make up the band of the specified
     * subsystem.
     *
     * @param Number of subsystems (starting at 0).
     *
     * @see <a href="#bandwidthAndChannels">Bandwidth and Channel methods</a>
     * @param subsystem  Subsystem number (starting at 0).
     */
    public int getNumHybridSubBands(int subsystem) {
        return _avTable.getInt(ATTR_HYBRID_SUBBANDS, subsystem, 0);
    }

    /**
     * Set number of hybrid subbands that make up the band of the specified
     * subsystem.
     *
     * @param Number of subsystems (starting at 0).
     *
     * @param subsystem  Subsystem number (starting at 0).
     */
    public void setNumHybridSubBands(int value, int subsystem) {
        _avTable.set(ATTR_HYBRID_SUBBANDS, value, subsystem);
    }

    /**
     * Set number of hybrid subbands that make up the band of the specified
     * subsystem.
     *
     * @param Number of subsystems (starting at 0).
     *
     * @param subsystem  Subsystem number (starting at 0).
     */
    public void setNumHybridSubBands(String value, int subsystem) {
        setNumHybridSubBands(Format.toInt(value), subsystem);
    }

    /**
     * Get channels of specified subsystem.
     *
     * @param Number of subsystems (starting at 0).
     *
     * @see <a href="#bandwidthAndChannels">Bandwidth and Channel methods</a>
     * @param subsystem  Subsystem number (starting at 0).
     */
    public int getChannels(int subsystem) {
        return _avTable.getInt(ATTR_CHANNELS, subsystem, 0);
    }

    /**
     * Set number of channels of specified subsystem.
     *
     * @param Number of subsystems (starting at 0).
     *
     * @param subsystem  Subsystem number (starting at 0).
     */
    public void setChannels(int value, int subsystem) {
        _avTable.set(ATTR_CHANNELS, value, subsystem);
    }

    /**
     * Get total number channels of specified subsystem (as if there was
     * 0-overlap).
     *
     * @param Number of subsystems (starting at 0).
     *
     * @see <a href="#bandwidthAndChannels">Bandwidth and Channel methods</a>
     */
    public int getChannelsTotal(int subsystem) {
        return _avTable.getInt(ATTR_CHANNELS_TOTAL, subsystem, 0);
    }

    /**
     * Get overlap of multiple subbands in one subsystem.
     *
     * @see <a href="#bandwidthAndChannels">Bandwidth and Channel methods</a>
     */
    public double getOverlap(int subsystem) {
        return _avTable.getDouble(ATTR_OVERLAP, subsystem, 0.0);
    }

    /**
     * Set overlap of multiple subbands in one subsystem.
     *
     */
    public void setOverlap(double value, int subsystem) {
        _avTable.set(ATTR_OVERLAP, value, subsystem);
    }

    /**
     * Set overlap of multiple subbands in one subsystem.
     *
     */
    public void setOverlap(String value, int subsystem) {
        setOverlap(Format.toDouble(value), subsystem);
    }

    public int getNumSubSystems() {
        return _avTable.size(ATTR_CENTRE_FREQUENCY);
    }

    public void setNamedConfiguration(String name) {
        _avTable.set(ATTR_NAMED_CONFIGURATION, name, 0);
    }

    public String getNamedConfiguration() {
        return _avTable.get(ATTR_NAMED_CONFIGURATION);
    }

    public void removeNamedConfiguration() {
        _avTable.rm(ATTR_NAMED_CONFIGURATION);
    }

    public double[] getScienceArea() {
        double[] scienceArea = null;
        String frontEnd = getFrontEnd();

        if (frontEnd.equals("A3")) {
            // 20 arcsec diameter circle
            scienceArea = new double[]{10.0};

        } else if (frontEnd.equals("WB")) {
            // 14 arcsec diameter circle
            scienceArea = new double[]{7.0};

        } else if (frontEnd.equals("WD")) {
            // 7 arcsec diameter circle
            scienceArea = new double[]{3.5};

        } else if (frontEnd.equals("HARP")) {
            // 2 arcmin square
            scienceArea = new double[]{120.0, 120.0};
        }

        return scienceArea;
    }

    /**
     * Converts given redshift to specified radial velocity definition.
     *
     * @param velocityDefinition Use {@link #RADIAL_VELOCITY_REDSHIFT},
     *                               {@link #RADIAL_VELOCITY_OPTICAL},
     *                               {@link #RADIAL_VELOCITY_RADIO}.
     */
    public static double convertRedshiftTo(String velocityDefinition,
            double redshift) {
        if (velocityDefinition.equals(RADIAL_VELOCITY_REDSHIFT)) {
            return redshift;
        }

        if (velocityDefinition.equals(RADIAL_VELOCITY_OPTICAL)) {
            return redshift * LIGHTSPEED;
        }

        if (velocityDefinition.equals(RADIAL_VELOCITY_RADIO)) {
            return LIGHTSPEED * (1.0 - (1.0 / (1.0 + redshift)));
        }

        // If the methods has not returned yet then the velocityDefinition
        // is invalid.
        throw new IllegalArgumentException(
                "SpInstHeterodyne.convertRedshiftTo:"
                + " Unrecognised velocity definition found.\n"
                + "Please use RADIAL_VELOCITY_REDSHIFT,"
                + " RADIAL_VELOCITY_OPTICAL, RADIAL_VELOCITY_RADIO.");
    }

    /**
     * Converts radial velocity in a given definition to redshift.
     *
     * @param velocityDefinition Use {@link #RADIAL_VELOCITY_REDSHIFT},
     *                               {@link #RADIAL_VELOCITY_OPTICAL},
     *                               {@link #RADIAL_VELOCITY_RADIO}.
     */
    public static double convertToRedshift(String velocityDefinition,
            double value) {
        if (velocityDefinition.equals(RADIAL_VELOCITY_REDSHIFT)) {
            return value;
        }

        if (velocityDefinition.equals(RADIAL_VELOCITY_OPTICAL)) {
            return value / LIGHTSPEED;
        }

        if (velocityDefinition.equals(RADIAL_VELOCITY_RADIO)) {
            return (LIGHTSPEED / (LIGHTSPEED - value)) - 1.0;
        }

        // If the methods has not returned yet then the velocityDefinition is
        // invalid.
        throw new IllegalArgumentException(
                "SpInstHeterodyne.convertToRedshift:"
                + " Unrecognised velocity definition found.\n"
                + "Please use RADIAL_VELOCITY_REDSHIFT,"
                + " RADIAL_VELOCITY_OPTICAL, RADIAL_VELOCITY_RADIO.");
    }

    /**
     * Determine whether this component has velocity information.
     *
     * (Otherwise the velocity of the associated target component is used.)
     */
    public boolean hasVelocityInformation() {
        return _avTable.exists(ATTR_VELOCITY);
    }

    /**
     * Calculate a new sky frequency value.
     *
     * This will use the velocity information in this component, if we have it
     * (hasVelocityInformation() returns true).  Otherwise it will try to
     * obtain velocity information from the associated target component.
     */
    public double calculateSkyFrequency() {
        return calculateSkyFrequency(0);
    }

    public double calculateSkyFrequency(int subsystem) {
        double redshift = calculateRedshift();

        return getRestFrequency(subsystem) / (1.0 + redshift);
    }

    public double calculateRedshift() {
        double velocity = 0.0;
        String velocityDefinition = RADIAL_VELOCITY_REDSHIFT;

        if (hasVelocityInformation()) {
            velocity = getVelocity();
            velocityDefinition = getVelocityDefinition();
        }
        else {
            SpTelescopeObsComp target = SpTreeMan.findTargetList(this);

            if (target != null) {
                SpTelescopePos tp = target.getPosList().getBasePosition();
                velocity = Double.parseDouble(tp.getTrackingRadialVelocity());
                velocityDefinition = tp.getTrackingRadialVelocityDefn();
            }
        }

        return convertToRedshift(velocityDefinition, velocity);
    }

    public static class BandSpecSelection {
        public BandSpec bandSpec;
        public int match;
        public int nearest;

        public BandSpecSelection(BandSpec bandSpec, int match, int nearest) {
            this.bandSpec = bandSpec;
            this.match = match;
            this.nearest = nearest;
        }
    }

    /**
     * Find the BandSpec and their entries corresponding to the current
     * configuration.
     *
     * This method uses the current band mode to find the corresponding
     * BandSpec.  The name of the band mode is assumed to be a number giving
     * the number of bands.  For each band (from 0 to the band mode - 1)
     * the currently selected bandwidth is compared all entries in the BandSpec.
     * The results are returned as a list of BandSpecSelection objects
     * containing the band spec for the band, the matching index and the
     * nearest index, if no match was found.  A value of -1 is returned
     * for the "match" and "index" values if there is no corresponding entry.
     *
     * This method makes use of the BandSpec.numCMs array to track how many
     * CMs have been allocated to bands.  If the available CMs will be
     * exhausted, it should fall back to a simpler BandSpec.  This behavior
     * can depend on which of the available bandwiths are selected for the
     * earlier bands.  E.g. for the 3-band case, band 0 gets the main "3"
     * BandSpec, but since all of its entries use 2 CMs, there would only
     * be 1 CM left per remaining band so the "4" BandSpec must be used
     * for bands 1 and 2.
     *
     * This method takes an edfreq.Receiver object containing the possible
     * BandSpec information, as this information is currently not available
     * to this class.  (The edfreq package used to be separate from this one.)
     */
    public List<BandSpecSelection> getBandSpecSelection(Receiver receiver) {
        int active = Integer.parseInt(getBandMode());
        double[] currentBandwidths = new double[active];
        int[] currentChannels = new int[active];
        for (int j = 0; j < active; j ++) {
            currentBandwidths[j] = getBandWidth(j);
            currentChannels[j] = getChannels(j);
        }

        return getBandSpecSelection(receiver, currentBandwidths, currentChannels);
    }

    public List<BandSpecSelection> getBandSpecSelection(
            Receiver receiver, double[] currentBandwidths, int[] currentChannels) {
        String bandMode = getBandMode();
        int active = Integer.parseInt(bandMode);

        BandSpec currentBandSpec = receiver.getBandSpec(bandMode);
        if (currentBandSpec == null) {
            currentBandSpec = receiver.bandspecs.get(0);
        }

        // Find the maximum number of CMs -- assume that the 1-CM BandSpec
        // allocates them all, so its name tells us the number available.
        int num_cm = Integer.parseInt(receiver.getBandSpecByCMs(1).name);
        int allocated_cm = 0;

        List<BandSpecSelection> selection = new Vector<BandSpecSelection>();

        for (int j = 0; j < active; j ++) {
            BandSpec activeBandSpec = currentBandSpec;

            // How many CMs remain, allowing 1 for all bands after this one?
            int availCMs = 1 + (num_cm - allocated_cm) - (active - j);

            if (availCMs < activeBandSpec.getMaxNumCMs()) {
                activeBandSpec = receiver.getBandSpecByCMs(availCMs);
            }

            double[] values = activeBandSpec.getDefaultOverlapBandWidths();
            int[] channelValues = activeBandSpec.getDefaultOverlapChannels();

            double currentBandwidth = currentBandwidths[j];
            int currentChannelNum = currentChannels[j];

            int index = -1;
            double lowestContestant = 0.0;
            int notableIndex = -1;

            for (int i = 0; i < values.length; i++) {
                // If this is a "chained" mode, it should start on an even number CM.
                if (((activeBandSpec.numCMs[i] / activeBandSpec.numHybridSubBands[i]) > 1)
                        && ((allocated_cm % 2) != 0)) {
                    continue;
                }

                if ((values[i] == currentBandwidth) && (i < channelValues.length) && (channelValues[i] == currentChannelNum)) {
                    index = i;
                }
                else {
                    double contestant = Math.abs(
                            currentBandwidth - values[i]);
                    if ((notableIndex == -1) || (contestant < lowestContestant)) {
                        lowestContestant = contestant;
                        notableIndex = i;
                    }
                }
            }

            selection.add(new BandSpecSelection(
                    activeBandSpec, index, (index == -1) ? notableIndex : -1));

            // Subtract however many CMs the selected mode uses.
            allocated_cm += activeBandSpec.numCMs[(index != -1) ? index : (
                    (notableIndex != -1) ? notableIndex : 0)];
        }

        return selection;
    }

    public String subsystemXML(String indent) {
        StringBuffer xmlString = new StringBuffer();
        xmlString.append(indent + "<subsystems>\n");

        for (int i = 0; i < Integer.parseInt(getBandMode()); i++) {
            xmlString.append(indent
                    + "    <subsystem if=\"" + getCentreFrequency(i)
                    + "\"" + " bw=\"" + getBandWidth(i)
                    + "\"" + " overlap=\"" + getOverlap(i) + "\""
                    + " channels=\"" + getChannels(i)
                    + "\">\n");
            xmlString.append(indent
                    + "        <line species=\"" + getMolecule(i)
                    + "\" transition=\"" + getTransition(i) + "\""
                    + " rest_freq=\"" + getRestFrequency(i)
                    + "\"/>\n");
            xmlString.append(indent + "    </subsystem>\n");
        }

        xmlString.append(indent + "</subsystems>\n");

        return xmlString.toString();
    }

    /**
     */
    protected void toXML(String indent, StringBuffer xmlBuffer) {

        if (!_valuesInitialised) {
            throw new RuntimeException("Heterodyne not initialised");
        }

        // Recompute the sky frequency in case it depends on a target
        // component the velocity of which has been updated.  This is
        // important as the sky frequency in the XML is used to select
        // the sideband (when automatic).  See fault 20160328.009.
        setSkyFrequency(calculateSkyFrequency());

        String configXML = null;

        try {
            configXML = subsystemXML(indent + "  ");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to include ACSIS configuration XML due to "
                    + e);
            configXML = indent
                    + "    <!-- Unable to include ACSIS configuration XML due to "
                    + e + " -->";
        }

        // In order not to write out the subsystem info, get all the values,
        // delete the attributes, and then rest the values after processing
        // with the parent

        Vector<String> cf = _avTable.getAll(ATTR_CENTRE_FREQUENCY);
        _avTable.rm(ATTR_CENTRE_FREQUENCY);
        Vector<String> bw = _avTable.getAll(ATTR_BANDWIDTH);
        _avTable.rm(ATTR_BANDWIDTH);
        Vector<String> ch = _avTable.getAll(ATTR_CHANNELS);
        _avTable.rm(ATTR_CHANNELS);
        Vector<String> mo = _avTable.getAll(ATTR_MOLECULE);
        _avTable.rm(ATTR_MOLECULE);
        Vector<String> tr = _avTable.getAll(ATTR_TRANSITION);
        _avTable.rm(ATTR_TRANSITION);
        Vector<String> rf = _avTable.getAll(ATTR_REST_FREQUENCY);
        _avTable.rm(ATTR_REST_FREQUENCY);
        Vector<String> ov = _avTable.getAll(ATTR_OVERLAP);
        _avTable.rm(ATTR_OVERLAP);

        super.toXML(indent, xmlBuffer);

        _avTable.noNotifySetAll(ATTR_CENTRE_FREQUENCY, cf);
        _avTable.noNotifySetAll(ATTR_BANDWIDTH, bw);
        _avTable.noNotifySetAll(ATTR_CHANNELS, ch);
        _avTable.noNotifySetAll(ATTR_MOLECULE, mo);
        _avTable.noNotifySetAll(ATTR_TRANSITION, tr);
        _avTable.noNotifySetAll(ATTR_REST_FREQUENCY, rf);
        _avTable.noNotifySetAll(ATTR_OVERLAP, ov);

        int offset = xmlBuffer.length()
                - (indent.length() + _className.length() + 4);

        xmlBuffer.insert(offset, "\n\n" + indent
                + "  <!-- - - - - - - - - - - - - - - - - - - - - -->\n"
                + indent
                + "  <!--          ACSIS Configuration XML        -->\n"
                + indent
                + "  <!-- - - - - - - - - - - - - - - - - - - - - -->\n\n"
                + configXML);

    }

    public void processXmlElementStart(String name) {
        _valuesInitialised = true;

        if (name.equals(XML_ELEMENT_ACSIS_CONFIGURATION)
                || name.equals(XML_ELEMENT_ACSIS_SUBSYSTEMS)) {
            _processingAcsisConfigurationXml = true;
            _subSystemCount = 0;
        } else if (name.equals(XML_ELEMENT_ACSIS_SUBSYSTEM)
                || name.equals(XML_ELEMENT_ACSIS_LINE)) {
            return;
        } else {
            super.processXmlElementStart(name);
        }
    }

    public void processXmlElementEnd(String name) {
        if (name.equals(XML_ELEMENT_ACSIS_CONFIGURATION)
                || name.equals(XML_ELEMENT_ACSIS_SUBSYSTEMS)) {
            _processingAcsisConfigurationXml = false;
        }

        if (name.equals(XML_ELEMENT_ACSIS_SUBSYSTEM)) {
            _subSystemCount++;
        }

        super.processXmlElementEnd(name);
    }

    public void processXmlAttribute(String elementName, String attributeName,
            String value) {
        if (!_processingAcsisConfigurationXml) {
            super.processXmlAttribute(elementName, attributeName, value);
        } else {
            if (elementName.equals(XML_ELEMENT_ACSIS_SUBSYSTEM)) {
                if (attributeName.equals("if")) {
                    _avTable.set(ATTR_CENTRE_FREQUENCY, value, _subSystemCount);

                } else if (attributeName.equals("bw")) {
                    _avTable.set(ATTR_BANDWIDTH, value, _subSystemCount);

                } else if (attributeName.equals(ATTR_CHANNELS)) {
                    _avTable.set(ATTR_CHANNELS, value, _subSystemCount);

                } else if (attributeName.equals(ATTR_OVERLAP)) {
                    _avTable.set(ATTR_OVERLAP, value, _subSystemCount);
                }
            } else if (elementName.equals(XML_ELEMENT_ACSIS_LINE)) {
                if (attributeName.equals("rest_freq")) {
                    _avTable.set(ATTR_REST_FREQUENCY, value, _subSystemCount);

                } else if (attributeName.equals("species")) {
                    _avTable.set(ATTR_MOLECULE, value, _subSystemCount);

                } else if (attributeName.equals("transition")) {
                    // Tidy transitions written by previous OT versions:
                    // * Remove trailing spaces.
                    // * Remove multiple spaces.
                    String transition = value.trim();
                    transition = transition.replaceAll("\\s+", " ");
                    _avTable.set(ATTR_TRANSITION, transition, _subSystemCount);
                }
            }
        }
    }

    public void processXmlElementContent(String name, String value) {
        if (!_processingAcsisConfigurationXml) {
            super.processXmlElementContent(name, value);
        }
    }

    public void processXmlElementContent(String name, String value, int pos) {
        if (!_processingAcsisConfigurationXml) {
            super.processXmlElementContent(name, value, pos);
        }
    }
}
