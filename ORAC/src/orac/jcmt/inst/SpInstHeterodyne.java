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

package orac.jcmt.inst ;

import gemini.sp.SpFactory ;
import gemini.sp.SpType ;
import gemini.util.Format ;

import java.util.Vector ;

/**
 * The Heterodyne instrument Observation Component.
 *
 * <h3>Terminology</h3>
 * <ul>
 *   <li><b>subsystem vs subband</b><br>
 *   A subsystem is made up of one (non-hybrid mode) or more (hybrid mode) subbands.
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
 * The methods {@link getIndividualSubBandWidth(int)} and
 * {@link getIndividualSubBandChannels(int)} refer to this individual
 * bandwidth.<p>
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
 * | |       {@link #getOverlap(int)}              {@link #getIndividualSubBandWidth(int)}    (as in hardware)
 * | |                                 |           {@link #getIndividualSubBandChannels(int)} (as in hardware)
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
 * |    {@link #getIndividualSubBandWidth(int)}, {@link #getIndividualSubBandChannels(int)}    
 * |                            (as in hardware)                           |
 *
 * </pre>
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings( "serial" )
public class SpInstHeterodyne extends SpJCMTInstObsComp
{

	public static double LIGHTSPEED = 2.99792458E5 ;

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
	protected static final int SUBSYSTEM_INDEX = -1 ;
	protected static final String CONFIG_LABEL_NONE = "UNDEFINED" ;

	// FrontEnd

	/** Receiver/Front end name. */
	public static final String ATTR_FE_NAME = "feName" ;

	/** Back end name */
	public static final String ATTR_BE_NAME = "beName" ;

	/** Special configuration identifier */
	public static final String ATTR_NAMED_CONFIGURATION = "configuration" ;

	/** Receiver: Central IF. */
	public static final String ATTR_FE_IF = "feIF" ;

	/** Receiver/Front end bandwidth. */
	public static final String ATTR_FE_BANDWIDTH = "feBandWidth" ;

	/** Mode: single side band (ssb), double side band (dsb). */
	public static final String ATTR_MODE = "mode" ;

	/** Band mode: 1-system, 2-system etc.  */
	public static final String ATTR_BAND_MODE = "bandMode" ;

	/** Mixer selection: Single or dual */
	public static final String ATTR_MIXER = "mixers" ;

	/** Radial velocity. */
	public static final String ATTR_VELOCITY = "velocity" ;
	public static final String ATTR_RF_VELOCITY = "referenceFrameVelocity" ;

	/** Radial velocity definition: "redshift", "optical", "radio". */
	public static final String ATTR_VELOCITY_DEFINITION = "velocityDefinition" ;
	public static final String ATTR_VELOCITY_FRAME = "velocityFrame" ;

	/** Band: upper side band (usb), lower side band (lsb), side band with line in range (optimum).  */
	public static final String ATTR_BAND = "band" ;

	/** Molecule. */
	public static final String ATTR_MOLECULE = "molecule" ;

	/** Transition. */
	public static final String ATTR_TRANSITION = "transition" ;

	/**
	 * Rest Frequency.
	 *
	 * This is usually the rest frequency of a molecular transition line.
	 * But it can also be used for an arbitrary user defined frequency in the rest frame of the source.
	 */
	public static final String ATTR_REST_FREQUENCY = "restFrequency" ;
	public static final String ATTR_SKY_FREQUENCY = "skyFrequency" ;

	/** Overlap of multiple hybrid subbands. */
	public static final String ATTR_OVERLAP = "overlap" ;

	// FrequencyTable

	/** Array of  */
	public static final String ATTR_CENTRE_FREQUENCY = "centreFrequency" ;

	/** Bandwidth reduced by overlap * number of hybrid subbands. */
	public static final String ATTR_BANDWIDTH = "bandWidth" ;

	/** Number of channels reduced by overlap * number of hybrid subbands. */
	public static final String ATTR_CHANNELS = "channels" ;

	/** Total number of channels ignoring overlap. */
	public static final String ATTR_CHANNELS_TOTAL = "channelsTotal" ;

	/** */
	public static final String ATTR_HYBRID_SUBBANDS = "hybridSubBands" ;

	public static String[] JIGGLE_PATTERNS = { "2x1" , "3x3" , "4x4" , "5x5" , "7x7" , "9x9" , "11x11" , "HARP4" , "HARP5" , "HARP4_mc" , "HARP5_mc" } ;

	/** Radial velocity expressed as redshift. */
	public static final String RADIAL_VELOCITY_REDSHIFT = "redshift" ;

	/** Radial velocity defined optically. */
	public static final String RADIAL_VELOCITY_OPTICAL = "optical" ;

	/** Radial velocity, radio defined. */
	public static final String RADIAL_VELOCITY_RADIO = "radio" ;

	/** Velocity frames */
	public static final String LSRK_VELOCITY_FRAME = "LSRK" ;
	public static final String GEOCENTRIC_VELOCITY_FRAME = "GEOCENTRIC" ;
	public static final String HELIOCENTRIC_VELOCITY_FRAME = "HELIOCENTRIC" ;
	public static final String BARYCENTRIC_VELOCITY_FRAME = "BARYCENTRIC" ;
	public static final String TOPOCENTRIC_VELOCITY_FRAME = "TOPOCENTRIC" ;

	/** Resolution (kHz) */
	public static final String ATTR_RESOLUTION = "resolution" ;

	/**
	 * Set to true when method initialiseValues() is called.
	 *
	 * @see #initialiseValues(String,String,String,String,String,String,String,String,String,String,String,String,String)
	 */
	private boolean _valuesInitialised = false ;

	/**
	 * Flag to indicate the Acsis configuration XML is being parsed.
	 *
	 * The Acsis configuration XML ({@link #XML_ELEMENT_ACSIS_CONFIGURATION})
	 * is only an output format. It must be ignored when reading in SpInstHeterodyne XML. 
	 */
	private boolean _processingAcsisConfigurationXml = false ;

	int _subSystemCount = 0 ;

	/**
	 * XML element containing the Acsis configuration.
	 *
	 * The XML_ELEMENT_ACSIS_CONFIGURATION String is <tt>&lt ;jcmt_config&gt ;</tt>.
	 */
	private static final String XML_ELEMENT_ACSIS_CONFIGURATION = "jcmt_config" ;
	private static final String XML_ELEMENT_ACSIS_SUBSYSTEMS = "subsystems" ;
	private static final String XML_ELEMENT_ACSIS_SUBSYSTEM = "subsystem" ;
	private static final String XML_ELEMENT_ACSIS_LINE = "line" ;
	
	public static final SpType SP_TYPE = SpType.create( SpType.OBSERVATION_COMPONENT_TYPE , "inst.Heterodyne" , "Het Setup" ) ;

	//Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpInstHeterodyne() ) ;
	}

	public SpInstHeterodyne()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Initialises the values fo this item.
	 * 
	 * Most of the default values (or allowed values) for this item are specified in the configuration files EDFREQ/cfg/edfreq/acsisCfg.xml and EDFREQ/cfg/edfreq/dasCfg.xml The Heterodyne Editor class {@link jcmt.inst.editor.EdCompInstHeterodyne}. Wheb the {@link jcmt.inst.editor.EdCompInstHeterodyne#_updateWidgets()} method of is called for the first time then it can check whether the values of this SpInstHeterodyne item have been initialised (by calling {@link #valuesInitialised()}). If they have not then the initialiseValues() can be used to fill in the values from the above configuration files.
	 * <p>
	 */
	public void initialiseValues( String defaultBeName , String defaultFeName , String defaultMode , String defaultBandMode , String defaultMixer , String defaultOverlap , String defaultBand , String defaultCentreFrequency , String frontendBandwidth , String defaultBandwidth , String defaultMolecule , String defaultTransition , String defaultRestFrequency )
	{
		_avTable.noNotifySet( ATTR_BE_NAME , defaultBeName , 0 ) ;
		_avTable.noNotifySet( ATTR_FE_NAME , defaultFeName , 0 ) ;
		_avTable.noNotifySet( ATTR_MODE , defaultMode , 0 ) ;
		_avTable.noNotifySet( ATTR_BAND_MODE , defaultBandMode , 0 ) ;
		_avTable.noNotifySet( ATTR_MIXER , defaultMixer , 0 ) ;
		_avTable.noNotifySet( ATTR_OVERLAP , defaultOverlap , 0 ) ;
		_avTable.noNotifySet( ATTR_BAND , defaultBand , 0 ) ;
		_avTable.noNotifySet( ATTR_CENTRE_FREQUENCY , defaultCentreFrequency , 0 ) ;
		_avTable.noNotifySet( ATTR_FE_IF , defaultCentreFrequency , 0 ) ;
		_avTable.noNotifySet( ATTR_FE_BANDWIDTH , frontendBandwidth , 0 ) ;
		_avTable.noNotifySet( ATTR_BANDWIDTH , defaultBandwidth , 0 ) ;
		_avTable.noNotifySet( ATTR_MOLECULE , defaultMolecule , 0 ) ;
		_avTable.noNotifySet( ATTR_TRANSITION , defaultTransition , 0 ) ;
		_avTable.noNotifySet( ATTR_REST_FREQUENCY , defaultRestFrequency , 0 ) ;

		_valuesInitialised = true ;
	}

	/**
	 * Indicates whether the method initialiseValues() has been called.
	 * 
	 * @see #initialiseValues(String,String,String,String,String,String,String,String,String,String,String,String,String, String, String, String)
	 */
	public boolean valuesInitialised()
	{
		return _valuesInitialised ;
	}

	/**
	 * Appends front end name to title.
	 */
	public String getTitle()
	{
		String frontEndName = getTable().get( ATTR_FE_NAME ) ;

		if( frontEndName == null )
			return super.getTitle() ;
		else
			return super.getTitle() + " (" + frontEndName + ")" ;
	}

	/**
	 * Get jiggle pattern options.
	 *
	 * @return String array of jiggle pattern options.
	 */
	public String[] getJigglePatterns()
	{
		return JIGGLE_PATTERNS ;
	}

	/** Not properly implemented yet. Returns 0.0. */
	public double getDefaultScanVelocity()
	{
		return 2.5 ;
	}

	/** Not properly implemented yet. Returns 0.0. */
	public double getDefaultScanDy()
	{
		return 10. ;
	}

	/**
	 * Get front end name.
	 */
	public String getFrontEnd()
	{
		if( _avTable.get( ATTR_FE_NAME ) == null || _avTable.get( ATTR_FE_NAME ).equals( "" ) )
			_avTable.noNotifySet( ATTR_FE_NAME , "A3" , 0 ) ;

		return _avTable.get( ATTR_FE_NAME ) ;
	}

	/**
	 * Set front end name.
	 */
	public void setFrontEnd( String value )
	{
		_avTable.set( ATTR_FE_NAME , value ) ;
	}

	/** Get the back end name */
	public String getBackEnd()
	{
		if( _avTable.get( ATTR_BE_NAME ) == null || _avTable.get( ATTR_BE_NAME ).equals( "" ) )
			_avTable.noNotifySet( ATTR_BE_NAME , "acsis" , 0 ) ;

		return _avTable.get( ATTR_BE_NAME ) ;
	}

	/**
	 * Get Receiver/Front end central IF.
	 */
	public double getFeIF()
	{
		return _avTable.getDouble( ATTR_FE_IF , 0. ) ;
	}

	/**
	 * Set Receiver/Front end central IF.
	 */
	public void setFeIF( double value )
	{
		_avTable.set( ATTR_FE_IF , value ) ;
	}

	/**
	 * Set Receiver/Front end central IF.
	 */
	public void setFeIF( String value )
	{
		setFeIF( Format.toDouble( value ) ) ;
	}

	/**
	 * Get Receiver/Front end bandwidth.
	 */
	public double getFeBandWidth()
	{
		return _avTable.getDouble( ATTR_FE_BANDWIDTH , 0. ) ;
	}

	/**
	 * Set Receiver/Front end bandwidth.
	 */
	public void setFeBandWidth( double value )
	{
		_avTable.set( ATTR_FE_BANDWIDTH , value ) ;
	}

	/**
	 * Set Receiver/Front end bandwidth.
	 */
	public void setFeBandWidth( String value )
	{
		setFeBandWidth( Format.toDouble( value ) ) ;
	}

	/**
	 * Get mode: single side band (ssb), double side band (dsb).
	 */
	public String getMode()
	{
		if( _avTable.get( ATTR_MODE ) == null || _avTable.get( ATTR_MODE ).equals( "" ) )
			_avTable.noNotifySet( ATTR_MODE , "ssb" , 0 ) ;

		return _avTable.get( ATTR_MODE ) ;
	}

	/**
	 * Set  mode: single side band (ssb), double side band (dsb).
	 */
	public void setMode( String value )
	{
		_avTable.set( ATTR_MODE , value ) ;
	}

	/**
	 * Get band mode: 1-system, 2-system etc. 
	 */
	public String getBandMode()
	{
		return _avTable.get( ATTR_BAND_MODE ) ;
	}

	/**
	 * Set band mode: 1-system, 2-system etc.
	 */
	public void setBandMode( String value )
	{
		_avTable.set( ATTR_BAND_MODE , value ) ;
	}

	/**
	 * Get velocity definition.
	 */
	public String getVelocityDefinition()
	{
		return _avTable.get( ATTR_VELOCITY_DEFINITION ) ;
	}

	/**
	 * Set velocity definition.
	 */
	public void setVelocityDefinition( String value )
	{
		_avTable.set( ATTR_VELOCITY_DEFINITION , value ) ;
	}

	public void setVelocityFrame( String value )
	{
		_avTable.set( ATTR_VELOCITY_FRAME , value ) ;
	}

	public String getVelocityFrame()
	{
		return _avTable.get( ATTR_VELOCITY_FRAME ) ;
	}

	/**
	 * Get velocity (optical definition).
	 */
	public double getVelocity()
	{
		return _avTable.getDouble( ATTR_VELOCITY , 0. ) ;
	}

	/**
	 * Set velocity (optical definition).
	 */
	public void setVelocity( double value )
	{
		_avTable.set( ATTR_VELOCITY , value ) ;
	}

	/**
	 * Set velocity (optical definition).
	 */
	public void setVelocity( String value )
	{
		setVelocity( Format.toDouble( value ) ) ;
	}

	public double getRedshift()
	{
		return getVelocity() / LIGHTSPEED ;
	}

	public void setVelocityFromRedshift( double redshift )
	{
		setVelocity( convertRedshiftTo( RADIAL_VELOCITY_OPTICAL , redshift ) ) ;
	}

	/**
	 * Set the reference frame velocity
	 */
	public void setRefFrameVelocity( double value )
	{
		_avTable.set( ATTR_RF_VELOCITY , value ) ;
	}

	/**
	 * Set the reference frame velocity
	 */
	public void setRefFrameVelocity( String value )
	{
		setRefFrameVelocity( Format.toDouble( value ) ) ;
	}

	public double getRefFrameVelocity()
	{
		return _avTable.getDouble( ATTR_RF_VELOCITY , 0. ) ;
	}

	/**
	 * Get the mixer mode
	 */
	public String getMixer()
	{
		return _avTable.get( ATTR_MIXER ) ;
	}

	/**
	 * Set the mixer mode
	 */
	public void setMixer( String value )
	{
		_avTable.set( ATTR_MIXER , value ) ;
	}

	/**
	 * Get band: upper side band (usb), lower side band (lsb), side band with line in range (optimum).
	 */
	public String getBand()
	{
		return _avTable.get( ATTR_BAND ) ;
	}

	/**
	 * Set band: upper side band (usb), lower side band (lsb), side band with line in range (optimum).
	 */
	public void setBand( String value )
	{
		_avTable.set( ATTR_BAND , value ) ;
	}

	/**
	 * Get molecule of specified subsystem.
	 *
	 * @param subsystem Number of subsystem (starting at 0).
	 * @return  The name of the molecule species currently set for the specified subsystem
	 */
	public String getMolecule( int subsystem )
	{
		String returnString = _avTable.get( ATTR_MOLECULE , subsystem ) ;
		return returnString ;
	}

	/**
	 * Set molecule of specified subsystem.
	 *
	 * @param value  The name of the molecule species observed in this subsystem
	 * @param subsystem Number of subsystem (starting at 0).
	 */
	public void setMolecule( String value , int subsystem )
	{
		_avTable.set( ATTR_MOLECULE , value , subsystem ) ;
	}

	/**
	 * Get transition of specified subsystem.
	 *
	 * @param subsystem  Subsystem number (starting at 0).
	 * @return  The molecular transistion currently set for the specified subsystem
	 */
	public String getTransition( int subsystem )
	{
		return _avTable.get( ATTR_TRANSITION , subsystem ) ;
	}

	/**
	 * Set transition of specified subsystem.
	 *
	 * @param value  The transition of the molecule species observed in this subsystem
	 * @param subsystem  Subsystem number (starting at 0).
	 */
	public void setTransition( String value , int subsystem )
	{
		_avTable.set( ATTR_TRANSITION , value , subsystem ) ;
	}

	/**
	 * Get rest frequency for specified subsystem.
	 *
	 * This is usually the rest frequency of a molecular transition line.
	 * If the heterodyne editor components allow specifying arbitrary frequencies in the
	 * rest frame of the source instead of a line rest frequency then these frequencies
	 * would be returned by this method. Currently an arbitrary frequency can only be specified
	 * for the top subsystem (subsystem 0). All the other ones can only have line frequencies
	 * taken form the LineCatalog provided with the Frequency Editor.<p>
	 * 
	 * For subsystems other than the top one (subsystem 0) the rest frequency is only used for
	 * data reduction purposes. The rest frequency of the top subsystem is also used to recalculate
	 * the LO1 at the time of the observation.
	 *
	 * @param subsystem  Subsystem number (starting at 0).
	 */
	public double getRestFrequency( int subsystem )
	{
		double restFreq = _avTable.getDouble( ATTR_REST_FREQUENCY , subsystem , 0. ) ;
		return restFreq ;
	}

	/**
	 * Set rest frequency for specified subsystem.
	 *
	 * @see #getRestFrequency(int)
	 * @param subsystem  Subsystem number (starting at 0).
	 */
	public void setRestFrequency( double value , int subsystem )
	{
		_avTable.set( ATTR_REST_FREQUENCY , value , subsystem ) ;
	}

	/**
	 * Set rest frequency for specified subsystem.
	 *
	 * @see #getRestFrequency(int)
	 * @param subsystem  Subsystem number (starting at 0).
	 */
	public void setRestFrequency( String value , int subsystem )
	{
		setRestFrequency( Format.toDouble( value ) , subsystem ) ;
	}

	/**
	 * Get the sky frequency.  Currently only the prime
	 * value is stored - that relating to the rest frequency of
	 * subsystem 0
	 */
	public double getSkyFrequency()
	{
		return _avTable.getDouble( ATTR_SKY_FREQUENCY , getRestFrequency( 0 ) ) ;
	}

	/**
	 * Set the Sky frequency for the 0th subsystem
	 */
	public void setSkyFrequency( double value )
	{
		_avTable.set( ATTR_SKY_FREQUENCY , value ) ;
	}

	/**
	 * Get centre frequency (IF) of specified subsystem.
	 *
	 * @param Number of subsystems (starting at 0).
	 *
	 * @see <A HREF="#bandwidthAndChannels">Bandwidth and Channel methods</A>
	 * @param subsystem  Subsystem number (starting at 0).
	 */
	public double getCentreFrequency( int subsystem )
	{
		return _avTable.getDouble( ATTR_CENTRE_FREQUENCY , subsystem , 0. ) ;
	}

	/**
	 * Set centre frequency (IF) of specified subsystem.
	 *
	 * @param subsystem  Subsystem number (starting at 0).
	 */
	public void setCentreFrequency( double value , int subsystem )
	{
		_avTable.set( ATTR_CENTRE_FREQUENCY , value , subsystem ) ;
	}

	/**
	 * Set centre frequency (IF) of specified subsystem.
	 *
	 * @param subsystem  Subsystem number (starting at 0).
	 */
	public void setCentreFrequency( String value , int subsystem )
	{
		setCentreFrequency( Format.toDouble( value ) , subsystem ) ;
	}

	public double[] getSubBandCentreFrequencies( int subsystem )
	{
		int numHybridSubBands = getNumHybridSubBands( subsystem ) ;

		double[] result = new double[ numHybridSubBands ] ;

		if( numHybridSubBands == 1 )
		{
			result[ 0 ] = getCentreFrequency( subsystem ) ;
			return result ;
		} 

		double centre = getCentreFrequency( subsystem ) ;
		double bw = getBandWidth( subsystem ) ;

		int j = ( numHybridSubBands / 2 ) - 1 ;
		int k = ( numHybridSubBands / 2 ) ;

		for( int i = 1 ; i < numHybridSubBands ; i += 2 )
		{
			result[ j ] = centre - ( ( ( ( double )i ) / ( 2. * ( ( double )numHybridSubBands ) ) ) * bw ) ;
			result[ k ] = centre + ( ( ( ( double )i ) / ( 2. * ( ( double )numHybridSubBands ) ) ) * bw ) ;

			j-- ;
			k++ ;
		}

		return result ;
	}

	/**
	 * Sets bandwidth and channel number related values.
	 *
	 * @param bandwidth Bandwidth reduced by (numHybridSubBands * overlap)
	 *                  This accounts for the subtraction of half the overlap on
	 *                  either side of the entire combined band as well as the overlap
	 *                  between individual subbands.
	 * @param overlap   The overlap is used in two ways: (1) as the the overlap
	 *                  between individual subbands and (2) half the overlap is
	 *                  used to as the amount that is subtracted from either side of
	 *                  the entire combined band.
	 * @param numHybridSubBands Number of individual hybrid subbands that make up this
	 *                  entire combined band.
	 * @param channels  Number of channels reduced in accordance with the overlap
	 * @param channelsTotal Total number of channels (as if there was a 0-overlap)
	 * @param subsystem Subsystem to which these parameters apply.
	 */
	public void setBandWidthDetails( double bandwidth , double overlap , int numHybridSubBands , int channels , int channelsTotal , int subsystem )
	{
		setBandWidth( bandwidth , subsystem ) ;
		setOverlap( overlap , subsystem ) ;
		setNumHybridSubBands( numHybridSubBands , subsystem ) ;
		setChannels( channels , subsystem ) ;
		_avTable.set( ATTR_CHANNELS_TOTAL , channelsTotal , subsystem ) ;
	}

	/**
	 * Get bandwidth of specified subsystem.
	 * 
	 * @param Number
	 *            of subsystems (starting at 0).
	 * 
	 * @see <A HREF="#bandwidthAndChannels">Bandwidth and Channel methods</A>
	 * @param subsystem
	 *            Subsystem number (starting at 0).
	 */
	public double getBandWidth( int subsystem )
	{
		double bandwidth = _avTable.getDouble( ATTR_BANDWIDTH , subsystem , 0. ) ;
		return bandwidth ;
	}

	/**
	 * Set bandwidth of specified subsystem.
	 * 
	 * @param Number
	 *            of subsystems (starting at 0).
	 * 
	 * @param subsystem
	 *            Subsystem number (starting at 0).
	 */
	public void setBandWidth( double value , int subsystem )
	{
		_avTable.set( ATTR_BANDWIDTH , value , subsystem ) ;
	}

	/**
	 * Set bandwidth of specified subsystem.
	 *
	 * @param Number of subsystems (starting at 0).
	 *
	 * @param subsystem  Subsystem number (starting at 0).
	 */
	public void setBandWidth( String value , int subsystem )
	{
		setBandWidth( Format.toDouble( value ) , subsystem ) ;
	}

	/**
	 * Get number of hybrid subbands that make up the band of the specified subsystem.
	 *
	 * @param Number of subsystems (starting at 0).
	 *
	 * @see <A HREF="#bandwidthAndChannels">Bandwidth and Channel methods</A>
	 * @param subsystem  Subsystem number (starting at 0).
	 */
	public int getNumHybridSubBands( int subsystem )
	{
		return _avTable.getInt( ATTR_HYBRID_SUBBANDS , subsystem , 0 ) ;
	}

	/**
	 * Set number of hybrid subbands that make up the band of the specified subsystem.
	 *
	 * @param Number of subsystems (starting at 0).
	 *
	 * @param subsystem  Subsystem number (starting at 0).
	 */
	public void setNumHybridSubBands( int value , int subsystem )
	{
		_avTable.set( ATTR_HYBRID_SUBBANDS , value , subsystem ) ;
	}

	/**
	 * Set number of hybrid subbands that make up the band of the specified subsystem.
	 *
	 * @param Number of subsystems (starting at 0).
	 *
	 * @param subsystem  Subsystem number (starting at 0).
	 */
	public void setNumHybridSubBands( String value , int subsystem )
	{
		setNumHybridSubBands( Format.toInt( value ) , subsystem ) ;
	}

	/**
	 * Get channels of specified subsystem.
	 *
	 * @param Number of subsystems (starting at 0).
	 *
	 * @see <A HREF="#bandwidthAndChannels">Bandwidth and Channel methods</A>
	 * @param subsystem  Subsystem number (starting at 0).
	 */
	public int getChannels( int subsystem )
	{
		return _avTable.getInt( ATTR_CHANNELS , subsystem , 0 ) ;
	}

	/**
	 * Set number of channels of specified subsystem.
	 *
	 * @param Number of subsystems (starting at 0).
	 *
	 * @param subsystem  Subsystem number (starting at 0).
	 */
	public void setChannels( int value , int subsystem )
	{
		_avTable.set( ATTR_CHANNELS , value , subsystem ) ;
	}

	/**
	 * Get total number channels of specified subsystem (as if there was 0-overlap).
	 *
	 * @param Number of subsystems (starting at 0).
	 *
	 * @see <A HREF="#bandwidthAndChannels">Bandwidth and Channel methods</A>
	 */
	public int getChannelsTotal( int subsystem )
	{
		return _avTable.getInt( ATTR_CHANNELS_TOTAL , subsystem , 0 ) ;
	}

	/**
	 * Get overlap of multiple subbands in one subsystem.
	 *
	 * @see <A HREF="#bandwidthAndChannels">Bandwidth and Channel methods</A>
	 */
	public double getOverlap( int subsystem )
	{
		return _avTable.getDouble( ATTR_OVERLAP , subsystem , 0. ) ;
	}

	/**
	 * Set overlap of multiple subbands in one subsystem.
	 *
	 */
	public void setOverlap( double value , int subsystem )
	{
		_avTable.set( ATTR_OVERLAP , value , subsystem ) ;
	}

	/**
	 * Set overlap of multiple subbands in one subsystem.
	 *
	 */
	public void setOverlap( String value , int subsystem )
	{
		setOverlap( Format.toDouble( value ) , subsystem ) ;
	}

	public int getNumSubSystems()
	{
		return _avTable.size( ATTR_CENTRE_FREQUENCY ) ;
	}

	public void setNamedConfiguration( String name )
	{
		_avTable.set( ATTR_NAMED_CONFIGURATION , name , 0 ) ;
	}

	public String getNamedConfiguration()
	{
		return _avTable.get( ATTR_NAMED_CONFIGURATION ) ;
	}

	public void removeNamedConfiguration()
	{
		_avTable.rm( ATTR_NAMED_CONFIGURATION ) ;
	}

	public double[] getScienceArea()
	{
		double[] scienceArea = null ;
		String frontEnd = getFrontEnd() ;
		if( frontEnd.equals( "A3" ) )
		{
			// 20 arcsec diameter circle
			scienceArea = new double[]{ 10. } ;
		}
		else if( frontEnd.equals( "WB" ) )
		{
			// 14 arcsec diameter circle
			scienceArea = new double[]{ 7. } ;
		}
		else if( frontEnd.equals( "WD" ) )
		{
			// 7 arcsec diameter circle
			scienceArea = new double[]{ 3.5 } ;
		}
		else if( frontEnd.equals( "HARP" ) )
		{
			// 2 arcmin square
			scienceArea = new double[]{ 120. , 120. } ;
		}
		return scienceArea ;
	}

	/**
	 * Get the bandwidth of one subband one or several of which
	 * make up the specified subsystem.
	 *
	 * Hybrid subsystems can be made up of several subbands.
	 *
	 * The value returned by this method corresponds to the actual bandwidth
	 * of an actual subband in hardware ignoring hybrid modes and overlaps etc.
	 * In the case of acsis this method should always return 250 or 1000.
	 *
	 * @param Number of subsystems (starting at 0).
	 *
	 * @see #getIndividualSubBandWidth(int)
	 * @see <A HREF="#bandwidthAndChannels">Bandwidth and Channel methods</A>
	 */
	public double getIndividualSubBandWidth( int subsystem )
	{
		// getBandWidth(subsystem) returns the combined bandwidth
		// that can contain several individual hybrid subbands and
		// is reduced according due to the overlapping of adjacent
		// hybrid subbands (by getOverlap(subsystem)) AND it has
		// 0.5 * getOverlap(subsystem) taken of either side of
		// the resulting combined bandwidth.
		//
		// That is why the individual subband bandwidth is calculated as below.

		return ( getBandWidth( subsystem ) / ( ( double )getNumHybridSubBands( subsystem ) ) ) + getOverlap( subsystem ) ;
	}

	/**
	 * Get the number channels of one subband one or several of which
	 * make up the specified subsystem.
	 *
	 * Hybrid subsystems can be made up of several subbands.
	 *
	 * The value returned by this method corresponds to the actual number of the channels
	 * of an actual subband in hardware ignoring hybrid modes and overlaps etc.
	 * In the case of acsis this method should always return 1024, 2048 etc.
	 *
	 * @param Number of subsystems (starting at 0).
	 *
	 * @see #getIndividualSubBandWidth(int)
	 * @see <A HREF="#bandwidthAndChannels">Bandwidth and Channel methods</A>
	 */
	public int getIndividualSubBandChannels( int subsystem )
	{
		return getChannelsTotal( subsystem ) / getNumHybridSubBands( subsystem ) ;
	}

	/**
	 * Converts given redshift to specified radial velocity definition.
	 *
	 * @param velocityDefinition Use {@link #RADIAL_VELOCITY_REDSHIFT},
	 *                               {@link #RADIAL_VELOCITY_OPTICAL},
	 *                               {@link #RADIAL_VELOCITY_RADIO}.
	 */
	public static double convertRedshiftTo( String velocityDefinition , double redshift )
	{
		if( velocityDefinition.equals( RADIAL_VELOCITY_REDSHIFT ) )
			return redshift ;

		if( velocityDefinition.equals( RADIAL_VELOCITY_OPTICAL ) )
			return redshift * LIGHTSPEED ;

		if( velocityDefinition.equals( RADIAL_VELOCITY_RADIO ) )
			return LIGHTSPEED * ( 1. - ( 1. / ( 1. + redshift ) ) ) ;

		// If the methods has not returned yet then the velocityDefinition is invalid.
		throw new IllegalArgumentException( "EdFreq.convertRedshiftTo: Unrecognised velocity definition found.\n" + "Please use RADIAL_VELOCITY_REDSHIFT, RADIAL_VELOCITY_OPTICAL, RADIAL_VELOCITY_RADIO." ) ;
	}

	/**
	 * Converts radial velocity in a given definition to redshift.
	 *  
	 * @param velocityDefinition Use {@link #RADIAL_VELOCITY_REDSHIFT},
	 *                               {@link #RADIAL_VELOCITY_OPTICAL},
	 *                               {@link #RADIAL_VELOCITY_RADIO}.
	 */
	public static double convertToRedshift( String velocityDefinition , double value )
	{
		if( velocityDefinition.equals( RADIAL_VELOCITY_REDSHIFT ) )
			return value ;

		if( velocityDefinition.equals( RADIAL_VELOCITY_OPTICAL ) )
			return value / LIGHTSPEED ;

		if( velocityDefinition.equals( RADIAL_VELOCITY_RADIO ) )
			return ( LIGHTSPEED / ( LIGHTSPEED - value ) ) - 1. ;

		// If the methods has not returned yet then the velocityDefinition is invalid.
		throw new IllegalArgumentException( "EdFreq.convertRedshiftTo: Unrecognised velocity definition found.\n" + "Please use RADIAL_VELOCITY_REDSHIFT, RADIAL_VELOCITY_OPTICAL, RADIAL_VELOCITY_RADIO." ) ;
	}
	
	/**
	 * Creates parts of ACSIS configuration file.
	 */
	public String toConfigXML( String indent )
	{
		String sidebandString = getBand() ;

		int sideband = 1 ; // usb

		if( sidebandString != null && sidebandString.equals( "lsb" ) )
			sideband = -1 ;

		// ------------------- Front end configuration ------------------------------------
		StringBuffer xmlBuffer = new StringBuffer() ;
		xmlBuffer.append( indent + "<" + XML_ELEMENT_ACSIS_CONFIGURATION + ">\n" + indent + "  <frontend_configure>\n" + indent + "    <rest_frequency units=\"GHz\" value=\"" + ( getRestFrequency( 0 ) * 1.E6 ) + "\"/>\n" + // TODO: Check whether * 1.0E6 has been done before
		indent + "    <if_centre_freq units=\"GHz\" value=\"" + getFeIF() + "\"/>\n" + indent + "    <sideband value=\"" + sideband + "\"/>\n" + indent + "    <sb_mode value=\"" + getMode().toUpperCase() + "\"/>\n" + indent + "    <freq_offset_scale units=\"MHz\" value=\"???\"/>\n" + indent + "    <doppler_tracking value=\"ON\"/>\n" + // Options are ON | OFF. Default to ON for now.
		indent + "    <optimize value=\"DISABLE\"/>\n" // Options are ENABLE | DISABLE. Default to DIABLE for now.
		) ;

		if( getFrontEnd().equals( "HARP" ) )
		{
			xmlBuffer.append( indent + "    <channel_mask>\n" + // Array of (OFF | ON | NEED). Use Pixeltool to switch pixels ON/OFF. NEED???
			indent + "      <CHAN_MASK_VALUE CHAN=\"00\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"01\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"02\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"03\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"04\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"05\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"06\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"07\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"08\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"09\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"10\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"11\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"12\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"13\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"14\" VALUE=\"ON\"/>\n" + indent + "      <CHAN_MASK_VALUE CHAN=\"15\" VALUE=\"ON\"/>\n" + indent + "    </channel_mask>\n" ) ;
		}

		xmlBuffer.append( indent + "  </frontend_configure>\n\n" ) ;

		// ------------------- ACSIS configuration ----------------------------------------

		// Line list
		String[] restFreqRefs = new String[ getNumSubSystems() ] ;
		String transition = null ;

		xmlBuffer.append( indent + "  <line_list>\n" ) ;

		for( int i = 0 ; i < getNumSubSystems() ; i++ )
		{
			transition = getTransition( i ) ;

			if( ( transition != null ) && ( transition.trim().length() > 0 ) )
				restFreqRefs[ i ] = "" + getMolecule( i ) + " " + transition ;
			else
				restFreqRefs[ i ] = "restFrequency" + i ;

			xmlBuffer.append( indent + "    <rest_frequency id=\"" + restFreqRefs[ i ] + "\" units=\"GHz\">" + getRestFrequency( i ) + "</rest_frequency>\n" ) ;
		}

		xmlBuffer.append( indent + "  </line_list>\n\n" ) ;

		// Acsis spectral windows list
		xmlBuffer.append( indent + "  <acsis_spw_list>\n" + indent + "    <doppler_field ref=\"TCS.RV.DOPPLER???\"/>\n" + indent + "    <spectral_window_id_field ref=\"SPECTRAL_WINDOW_ID???\"/>\n" + indent + "    <front_end_lo_freq_field ref=\"FE.STATE.LO_FREQ\"/>\n" ) ;

		// Spectral windows
		for( int i = 0 ; i < getNumSubSystems() ; i++ )
			xmlBuffer.append( spectralWindowToXML( restFreqRefs[ i ] , sideband , "  <!-- <base_line_fit> etc. not implemented yet. -->" , indent + "    " , i ) + "\n" ) ;

		xmlBuffer.append( indent + "  </acsis_spw_list>\n" + indent + "</" + XML_ELEMENT_ACSIS_CONFIGURATION + ">\n" ) ;

		return xmlBuffer.toString() ;
	}

	public String spectralWindowToXML( String restFrequencyId , int sideband , String dataReductionXML , String indent , int subsystem )
	{
		return indent + "<spectral_window id=\"SPW" + ( subsystem + 1 ) + "\">\n" + indent + "  <spw_bandwidth_mode mode=\"1GHzx1024\"/>\n" + indent + "  <spw_window type=\"truncate\"/>\n" + indent + "  <rest_frequency_ref ref=\"" + restFrequencyId + "\"/>\n" + indent + "  <front_end_sideband sideband=\"" + sideband + "\"/>\n" + indent + "  <spw_if_coordinate>\n" + indent + "    <spw_reference_if_frequency units=\"GHz\">" + getCentreFrequency( subsystem ) + "</spw_reference_if_frequency>\n" + indent + "    <spw_reference_pixel>" + "4064.0???" + "</spw_reference_pixel>\n" + indent + "    <spw_if_channel_width units=\"Hz\">" + getBandWidth( subsystem ) + "</spw_if_channel_width>\n" + indent + "    <spw_number_if_channel>" + getChannels( subsystem ) + "</spw_number_if_channel>\n" + indent + "  </spw_if_coordinate>\n" + indent + dataReductionXML + "\n" + indent + "</spectral_window>\n" ;
	}

	public String subsystemXML( String indent )
	{
		StringBuffer xmlString = new StringBuffer() ;
		xmlString.append( indent + "<subsystems>\n" ) ;
		for( int i = 0 ; i < Integer.parseInt( getBandMode() ) ; i++ )
		{
			xmlString.append( indent + "    <subsystem if=\"" + getCentreFrequency( i ) + "\"" + " bw=\"" + getBandWidth( i ) + "\"" + " overlap=\"" + getOverlap( i ) + "\"" + " channels=\"" + getChannels( i ) + "\">\n" ) ;
			xmlString.append( indent + "        <line species=\"" + getMolecule( i ) + "\" transition=\"" + getTransition( i ) + "\"" + " rest_freq=\"" + getRestFrequency( i ) + "\"/>\n" ) ;
			xmlString.append( indent + "    </subsystem>\n" ) ;
		}
		xmlString.append( indent + "</subsystems>\n" ) ;

		return xmlString.toString() ;
	}

	/**
	 */
	protected void toXML( String indent , StringBuffer xmlBuffer )
	{

		if( !_valuesInitialised )
			throw new RuntimeException( "Heterodyne not initialised" ) ;

		String configXML = null ;

		try
		{
			configXML = subsystemXML( indent + "  " ) ;
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
			System.out.println( "Unable to include ACSIS configuration XML due to " + e ) ;
			configXML = indent + "    <!-- Unable to include ACSIS configuration XML due to " + e + " -->" ;
		}

		/*
		 * In order not to write out the subsystem info, get all the values,
		 * delete the attributes, and then rest the values after processing with the
		 * parent
		 */
		Vector<String> cf = _avTable.getAll( ATTR_CENTRE_FREQUENCY ) ;
		_avTable.rm( ATTR_CENTRE_FREQUENCY ) ;
		Vector<String> bw = _avTable.getAll( ATTR_BANDWIDTH ) ;
		_avTable.rm( ATTR_BANDWIDTH ) ;
		Vector<String> ch = _avTable.getAll( ATTR_CHANNELS ) ;
		_avTable.rm( ATTR_CHANNELS ) ;
		Vector<String> mo = _avTable.getAll( ATTR_MOLECULE ) ;
		_avTable.rm( ATTR_MOLECULE ) ;
		Vector<String> tr = _avTable.getAll( ATTR_TRANSITION ) ;
		_avTable.rm( ATTR_TRANSITION ) ;
		Vector<String> rf = _avTable.getAll( ATTR_REST_FREQUENCY ) ;
		_avTable.rm( ATTR_REST_FREQUENCY ) ;
		Vector<String> ov = _avTable.getAll( ATTR_OVERLAP ) ;
		_avTable.rm( ATTR_OVERLAP ) ;

		super.toXML( indent , xmlBuffer ) ;

		_avTable.noNotifySetAll( ATTR_CENTRE_FREQUENCY , cf ) ;
		_avTable.noNotifySetAll( ATTR_BANDWIDTH , bw ) ;
		_avTable.noNotifySetAll( ATTR_CHANNELS , ch ) ;
		_avTable.noNotifySetAll( ATTR_MOLECULE , mo ) ;
		_avTable.noNotifySetAll( ATTR_TRANSITION , tr ) ;
		_avTable.noNotifySetAll( ATTR_REST_FREQUENCY , rf ) ;
		_avTable.noNotifySetAll( ATTR_OVERLAP , ov ) ;

		int offset = xmlBuffer.length() - ( indent.length() + _className.length() + 4 ) ;

		xmlBuffer.insert( offset , "\n\n" + indent + "  <!-- - - - - - - - - - - - - - - - - - - - - -->\n" + indent + "  <!--          ACSIS Configuration XML        -->\n" + indent + "  <!-- - - - - - - - - - - - - - - - - - - - - -->\n\n" + configXML ) ;

	}

	public void processXmlElementStart( String name )
	{
		_valuesInitialised = true ;

		if( name.equals( XML_ELEMENT_ACSIS_CONFIGURATION ) || name.equals( XML_ELEMENT_ACSIS_SUBSYSTEMS ) )
		{
			_processingAcsisConfigurationXml = true ;
			_subSystemCount = 0 ;
		}
		else if( name.equals( XML_ELEMENT_ACSIS_SUBSYSTEM ) || name.equals( XML_ELEMENT_ACSIS_LINE ) )
		{
			return ;
		}
		else
		{
			super.processXmlElementStart( name ) ;
		}
	}

	public void processXmlElementEnd( String name )
	{
		if( name.equals( XML_ELEMENT_ACSIS_CONFIGURATION ) || name.equals( XML_ELEMENT_ACSIS_SUBSYSTEMS ) )
			_processingAcsisConfigurationXml = false ;

		if( name.equals( XML_ELEMENT_ACSIS_SUBSYSTEM ) )
			_subSystemCount++ ;
		
		super.processXmlElementEnd( name ) ;
	}

	public void processXmlAttribute( String elementName , String attributeName , String value )
	{
		if( !_processingAcsisConfigurationXml )
		{
			super.processXmlAttribute( elementName , attributeName , value ) ;
		}
		else
		{
			if( elementName.equals( XML_ELEMENT_ACSIS_SUBSYSTEM ) )
			{
				if( attributeName.equals( "if" ) )
					_avTable.set( ATTR_CENTRE_FREQUENCY , value , _subSystemCount ) ;
				else if( attributeName.equals( "bw" ) )
					_avTable.set( ATTR_BANDWIDTH , value , _subSystemCount ) ;
				else if( attributeName.equals( ATTR_CHANNELS ) )
					_avTable.set( ATTR_CHANNELS , value , _subSystemCount ) ;
				else if( attributeName.equals( ATTR_OVERLAP ) )
					_avTable.set( ATTR_OVERLAP , value , _subSystemCount ) ;
			}
			else if( elementName.equals( XML_ELEMENT_ACSIS_LINE ) )
			{
				if( attributeName.equals( "rest_freq" ) )
					_avTable.set( ATTR_REST_FREQUENCY , value , _subSystemCount ) ;
				else if( attributeName.equals( "species" ) )
					_avTable.set( ATTR_MOLECULE , value , _subSystemCount ) ;
				else if( attributeName.equals( "transition" ) )
					_avTable.set( ATTR_TRANSITION , value , _subSystemCount ) ;
			}
		}
	}

	public void processXmlElementContent( String name , String value )
	{
		if( !_processingAcsisConfigurationXml )
			super.processXmlElementContent( name , value ) ;
	}

	public void processXmlElementContent( String name , String value , int pos )
	{
		if( !_processingAcsisConfigurationXml )
			super.processXmlElementContent( name , value , pos ) ;
	}
}
