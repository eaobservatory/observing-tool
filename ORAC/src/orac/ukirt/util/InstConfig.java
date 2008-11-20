package orac.ukirt.util ;

import java.util.Enumeration ;
import java.util.Hashtable ;
import java.util.Vector ;

/**
 *  This class defines a data structure which stores the instrument
 *  configurations for the Translator.
 *
 *  Currently supports UFTI, IRCAM3, CGS4, Michelle.  UIST & WFCAM support
 *  is in development.
 *
 *  see SpTranslator
 */
public class InstConfig extends Hashtable<String,String> implements Cloneable
{
	// Declare variables.
	// ==================
	private Hashtable<String,String> instTable ;
	private String key ; // Hashtable key
	private String value ; // Value corresponding to the above

	// Hashtable key

	/**
	 *  Constructor.  Could have another with instrum defaulted and invoking
	 *  this constructor.
	 */
	public InstConfig()
	{
		// Start with default elements in the hash table (101) and 0.75 loading, which should be plenty.
		super() ;
	}

	/**
	 * Initialises the configuration array for a given instrument.
	 */

	public void init( String instrum )
	{
		// Use different initialisations for different instruments.

		// UFTI
		// ====
		if( instrum.equalsIgnoreCase( "UFTI" ) )
		{
			// Supply values to the hashtable. Undefined values are set to
			// "TBD" because a Hashtable cannot accept a null value.
			put( "instrument" , instrum ) ; // Instrument name
			put( "version" , "0.0" ) ; // Version number
			put( "name" , "TBD" ) ; // Name of configuration file
			put( "readMode" , "read" ) ; // Acquisition/read mode
			put( "speed" , "Normal" ) ; // Acquisition speed

			put( "expTime" , "10.0" ) ; // Exposure time in seconds
			put( "objNumExp" , "1" ) ; // Number of object exposures (coadds)
			put( "savedInt" , "1" ) ; // Number of saved integrations
			put( "readArea" , "1024x1024" ) ; // Readout area
			put( "filter" , "J98" ) ; // Filter name
			put( "polariser" , "none" ) ; // Polariser name

			put( "flatNumExp" , "1" ) ; // Number of flat exposures (coadds)
			put( "flatSavedInt" , "1" ) ; // Number of saved flat integrations

			put( "darkNumExp" , "1" ) ; // Number of dark exposures (coadds)
			put( "darkSavedInt" , "1" ) ; // Number of saved dark integrations

			put( "biasExpTime" , "0.12" ) ; // Bias exposure time
			put( "biasNumExp" , "100" ) ; // Number of bias exposures (coadds)
			put( "biasSavedInt" , "1" ) ; // Number of saved bias integrations

			// IRCAM3
			// ======
		}
		else if( instrum.equalsIgnoreCase( "TUFTI/IRCAM" ) || instrum.equalsIgnoreCase( "IRCAM3" ) )
		{
			// Supply values to the hashtable. Undefined values are set to
			// "TBD" because a Hashtable cannot accept a null value.
			put( "instrument" , instrum ) ; // Instrument name
			put( "version" , "2.1" ) ; // Version number
			put( "name" , "TBD" ) ; // Name of configuration file
			put( "readMode" , "NDSTARE" ) ; // Acquisition/read mode
			put( "speed" , "Standard" ) ; // Acquisition speed

			put( "expTime" , "10.0" ) ; // Exposure time in seconds
			put( "objNumExp" , "1" ) ; // Number of object exposures (coadds)
			put( "savedInt" , "1" ) ; // Number of saved integrations
			put( "readArea" , "256x256" ) ; // Readout area
			put( "filter" , "J98" ) ; // Filter name
			put( "polariser" , "none" ) ; // Polariser name

			put( "flatNumExp" , "1" ) ; // Number of flat exposures (coadds)
			put( "flatSavedInt" , "1" ) ; // Number of saved flat integrations

			put( "darkNumExp" , "1" ) ; // Number of dark exposures (coadds)
			put( "darkSavedInt" , "1" ) ; // Number of saved dark integrations

			put( "biasExpTime" , "0.12" ) ; // Bias exposure time
			put( "biasNumExp" , "100" ) ; // Number of bias exposures (coadds)
			put( "biasSavedInt" , "1" ) ; // Number of saved bias integrations

			// CGS4
			// ====
		}
		else if( instrum.equalsIgnoreCase( "CGS4" ) )
		{
			put( "instrument" , instrum ) ; // Instrument name
			put( "version" , "TBD" ) ; // Version number
			put( "name" , "TBD" ) ; // Name of configuration file
			put( "readMode" , "read" ) ; // Acquisition/read mode

			put( "expTime" , "5.5" ) ; // Exposure time in seconds
			put( "objNumExp" , "1" ) ; // Number of object exposures (coadds)
			put( "savedInt" , "1" ) ; // Number of saved integrations
			put( "filter" , "Blank" ) ; // Filter name
			put( "neutralDensity" , "false" ) ; // Whether or not neutral-density
			// filter is in place

			put( "sampling" , "1x1" ) ; // Sampling stepsize x range in pixels
			put( "positionAngle" , "0.0" ) ; // Slit position angle
			put( "slitWidth" , "2" ) ; // Number of pixels across slit width
			put( "disperser" , "40lpmm" ) ; // Grating ruling or echelle
			put( "polariser" , "none" ) ; // Polariser name
			put( "centralWavelength" , "2.2" ) ; // Central wavelength in microns
			put( "order" , "1" ) ; // Grating order
			put( "cvfWavelength" , "0.0" ) ; // Circular variable filter offset
			// in microns

			put( "calibLamp" , "off" ) ; // Black-body or arc lamps
			put( "tunHalLevel" , "97" ) ; // Tungsten-halogen level
			put( "lampEffAp" , "10" ) ; // Lamp effective aperture

			// Attributes for flat
			put( "flatSampling" , "1x1" ) ; // Flat sampling stepsize x range in pixel
			put( "flatCalLamp" , "1.3" ) ; // Flat black-body aperture
			put( "flatReadMode" , "NDSTARE" ) ; // Flat acquisition configuration
			put( "flatExpTime" , "0.2" ) ; // Flat exposure time
			put( "flatNeutralDensity" , "false" ) ; // Whether or not neutral-density
			// filter is in place for flat
			put( "flatNumExp" , "30" ) ; // Number of flat exposures (coadds)
			put( "flatSavedInt" , "1" ) ; // Number of saved flat integrations

			// Attribute for dark and bias
			put( "darkNumExp" , "1" ) ; // Number of dark exposures (coadds)
			put( "darkSavedInt" , "1" ) ; // Number of saved dark integrations

			put( "biasExpTime" , "0.12" ) ; // Bias exposure time
			put( "biasNumExp" , "100" ) ; // Number of bias exposures (coadds)
			put( "biasSavedInt" , "3" ) ; // Number of saved bias integrations

			// Attributes for arc
			put( "arcCalLamp" , "argon" ) ; // Full wavelength calibration
			put( "arcFilter" , "Blank" ) ; // Filter for arc
			put( "arcCvfWavelength" , "TBD" ) ; // Evaluated cvf * offset + central
			// wavelength
			put( "arcExpTime" , "0.12" ) ; // Arc exposure time
			put( "arcNumExp" , "100" ) ; // Number of arc exposures (coadds)
			put( "arcReadMode" , "stare" ) ; // Readout mode for arc
			put( "arcSavedInt" , "1" ) ; // Number of saved arc integrations

			// Michelle
			// ========
		}
		else if( instrum.equalsIgnoreCase( "Michelle" ) )
		{
			put( "instrument" , instrum ) ; // Instrument name
			put( "version" , "TBD" ) ; // Version number
			put( "name" , "TBD" ) ; // Name of configuration file
			put( "configType" , "normal" ) ; // Type of configuration: normal or eng
			put( "type" , "object" ) ; // Default "type" of Michelle config
			put( "camera" , "imaging" ) ; // Which camera is in use

			put( "polarimetry" , "false" ) ; // Whether polarimetry is in use
			put( "slitWidth" , "2_pixels" ) ; // Mask (slit) in use
			put( "maskAngle" , "0.0" ) ; // Mask position angle
			put( "positionAngle" , "0.0" ) ; // Position angle (on sky)
			put( "disperser" , "echelle" ) ; // Grating ruling or echelle
			put( "order" , "1" ) ; // Grating order
			put( "sampling" , "1x1" ) ; // Sampling stepsize x range in pixels
			put( "centralWavelength" , "10.3" ) ; // Central wavelength in microns
			put( "filter" , "Blank" ) ; // Filter name
			put( "waveplate" , "TBD" ) ; // Waveplate (type or angle?)

			put( "scienceArea" , "68.0 x 51.0" ) ; // Science area
			put( "spectralCoverage" , "10.0 - 11.0" ) ; // Wavelength coverage
			put( "pixelFOV" , "1.00 x 1.00" ) ; // Pixel field of view

			put( "nreads" , "1" ) ; // Number of reads
			put( "mode" , "read" ) ; // Acquisition/read mode
			put( "expTime" , "0.02" ) ; // Exposure time in seconds
			put( "readInterval" , "0.0" ) ; // Read interval in seconds
			put( "chopFrequency" , "0.0" ) ; // Chop frequency
			put( "resetDelay" , "0.0" ) ; // reset delay (msecs?)
			put( "nresets" , "0" ) ; // No. of resets
			put( "chopDelay" , "0.0" ) ; // Chop delay (msecs?)
			put( "objNumExp" , "1" ) ; // Number of object exposures (coadds)
			put( "waveform" , "unknown" ) ; // Edict Waveform to be used
			put( "dutyCycle" , "0.0" ) ; // Dutycycle for the obs.
			put( "mustIdles" , "0" ) ; // Edict idling
			put( "nullReads" , "0" ) ; // Edict no. of null reads
			put( "nullExposures" , "0" ) ; // Edict no. of null exposures
			put( "nullCycles" , "0" ) ; // Edict no. of null cycles
			put( "idlePeriod" , "0.0" ) ; // Edict idle period
			put( "observationTime" , "20.0" ) ; // Complete obs. time in seconds

			// Attributes for flats
			put( "flatSampling" , "1x1" ) ; // Flat sampling stepsize x range in pixel
			put( "flatSource" , "1.3" ) ; // Flat cal. source
			put( "flatFilter" , "Blank" ) ; // Flat filter
			put( "flatNreads" , "1" ) ; // Number of reads
			put( "flatMode" , "read" ) ; // Acquisition/read mode
			put( "flatExpTime" , "0.02" ) ; // Exposure time in seconds
			put( "flatReadInterval" , "0.0" ) ; // Read interval in seconds
			put( "flatChopFrequency" , "0.0" ) ; // Chop frequency
			put( "flatChopDelay" , "0.0" ) ; // Chop delay
			put( "flatResetDelay" , "0.0" ) ; // reset delay (msecs?)
			put( "flatNresets" , "0" ) ; // Number of resets
			put( "flatNumExp" , "1" ) ; // Number of flat exposures (coadds)
			put( "flatWaveform" , "unknown" ) ; // Edict Waveform to be used
			put( "flatDutyCycle" , "0.0" ) ; // Dutycycle for the obs.
			put( "flatMustIdles" , "0" ) ; // Edict idling
			put( "flatNullReads" , "0" ) ; // Edict no. of null reads
			put( "flatNullExposures" , "0" ) ; // Edict no. of null exposures
			put( "flatNullCycles" , "0" ) ; // Edict no. of null cycles
			put( "flatIdlePeriod" , "0.0" ) ; // Edict idle period

			// Attributes for darks (only filter used currently)
			put( "darkFilter" , "Blank" ) ; // Filter name for darks
			put( "darkNumExp" , "400" ) ; // Number of dark exposures (coadds)

			// Attributes for biases 
			put( "biasExpTime" , "0.020" ) ; // Bias exposure time
			put( "biasNumExp" , "50" ) ; // Number of bias exposures (coadds)
			put( "biasSavedInt" , "3" ) ; // Number of saved bias integrations

			// Attributes for arcs
			//         put( "arcSampling", "1x1" ) ;   // Arc sampling stepsize x range in pixel
			put( "arcFilter" , "Blank" ) ; // Flat filter
			put( "arcNreads" , "1" ) ; // Number of reads
			put( "arcMode" , "read" ) ; // Acquisition/read mode
			put( "arcExpTime" , "0.02" ) ; // Exposure time in seconds
			put( "arcReadInterval" , "0.0" ) ; // Read interval in seconds
			put( "arcChopFrequency" , "0.0" ) ; // Chop frequency
			put( "arcChopDelay" , "0.0" ) ; // Chop delay
			put( "arcResetDelay" , "0.0" ) ; // reset delay (msecs?)
			put( "arcNresets" , "0" ) ; // No. of resets
			put( "arcNumExp" , "1" ) ; // Number of arc exposures (coadds)
			put( "arcWaveform" , "unknown" ) ; // Edict Waveform to be used
			put( "arcDutyCycle" , "0.0" ) ; // Dutycycle for the obs.
			put( "arcMustIdles" , "0" ) ; // Edict idling
			put( "arcNullReads" , "0" ) ; // Edict no. of null reads
			put( "arcNullExposures" , "0" ) ; // Edict no. of null exposures
			put( "arcNullCycles" , "0" ) ; // Edict no. of null cycles
			put( "arcIdlePeriod" , "0.0" ) ; // Edict idle period

			// UIST
			// ====
		}
		else if( instrum.equalsIgnoreCase( "UIST" ) )
		{
			put( "instrument" , instrum ) ; // Instrument name
			put( "version" , "TBD" ) ; // Version number
			put( "name" , "TBD" ) ; // Name of configuration file
			put( "configType" , "Normal" ) ; // Type of configuration: always Normal
			put( "type" , "object" ) ; // Default "type" of UIST config
			put( "instPort" , "South" ) ; // Instrument port
			put( "camera" , "imaging" ) ; // Which camera is in use
			put( "imager" , "0.12" ) ;

			put( "polarimetry" , "no" ) ; // Whether or not polarimetry is in use
			put( "slitWidth" , "3_pix_short_slit" ) ; // Mask (slit) in use
			put( "maskWidth" , "3.0" ) ; // Width of mask in pixels
			put( "maskHeight" , "120.0" ) ; // Height of mask in arcsec
			put( "positionAngle" , "0.0" ) ; // Position angle (on sky)
			put( "disperser" , "Set1A" ) ; // Grism name

			put( "centralWavelength" , "2.1" ) ; // Central wavelength in microns
			put( "resolution" , "1000" ) ; // Spectral resolution
			put( "dispersion" , "0.001" ) ; // Spectral dispersion in um/pixel
			put( "filter" , "F1A" ) ; // Filter name
			put( "focus" , "10" ) ; // Instrument focus in mm

			put( "scienceArea" , "122.9 x 122.9" ) ; // Science area
			put( "spectralCoverage" , "2.0 - 3.0" ) ; // Wavelength coverage
			put( "pixelFOV" , "0.12 x 0.12" ) ;// Pixel field of view
			put( "pixelScale" , "0.0" ) ; // Pixel scale (arcsec)

			put( "nreads" , "1" ) ; // Number of reads
			put( "mode" , "read" ) ; // Acquisition/read mode
			put( "expTime" , "5.5" ) ; // Exposure time in seconds
			put( "readInterval" , "0.0" ) ; // Read interval in seconds
			put( "chopFrequency" , "0.0" ) ; // Chop frequency

			put( "chopDelay" , "0.0" ) ; // Chop delay (msecs?)
			put( "objNumExp" , "1" ) ; // Number of object exposures (coadds)

			put( "dutyCycle" , "0.0" ) ; // Dutycycle for the obs.

			put( "observationTime" , "5.5" ) ; // Complete obs. time in seconds

			// Added by RDK
			put( "pupil_imaging" , "no" ) ; // Pupil imaging value
			put( "DAConf" , "ND1" ) ; // DA configuration
			put( "DAConfMinExpT" , "0.81" ) ; // DA configuration min exposure time
			// End of added by RDK

			// Attributes for flats
			put( "flatSource" , "1.3" ) ; // Flat cal. source
			put( "flatFilter" , "Blank" ) ; // Flat filter
			put( "flatNreads" , "1" ) ; // Number of reads
			put( "flatMode" , "read" ) ; // Acquisition/read mode
			put( "flatExpTime" , "5.5" ) ; // Exposure time in seconds
			put( "flatReadInterval" , "0.0" ) ; // Read interval in seconds
			put( "flatChopFrequency" , "0.0" ) ; // Chop frequency
			put( "flatChopDelay" , "0.0" ) ; // Chop delay

			put( "flatNumExp" , "1" ) ; // Number of flat exposures (coadds)

			put( "flatDutyCycle" , "0.0" ) ; // Dutycycle for the obs.

			// Attributes for darks (only filter used currently)
			put( "darkFilter" , "Blank" ) ; // Filter name for darks
			put( "darkNumExp" , "1" ) ; // Number of dark exposures (coadds)

			// Attributes for biases 
			put( "biasExpTime" , "0.020" ) ; // Bias exposure time
			put( "biasNumExp" , "50" ) ; // Number of bias exposures (coadds)

			put( "biasObsTime" , "10.0" ) ; // Observation time for bias
			put( "biasDutyCycle" , "0.5" ) ; // Duty cycle for bias

			// Attributes for arcs
			put( "arcFilter" , "Blank" ) ; // Flat filter
			put( "arcOrder" , "1" ) ; // Grating order
			put( "arcCentralWavelength" , "2.1" ) ; // Central wavelength in microns
			put( "arcSpectralCoverage" , "2.0 - 3.0" ) ; // Wavelength coverage
			put( "arcNreads" , "1" ) ; // Number of reads
			put( "arcMode" , "read" ) ; // Acquisition/read mode
			put( "arcCalLamp" , "argon" ) ; // Calibration lamp name
			put( "arcExpTime" , "5.5" ) ; // Exposure time in seconds
			put( "arcReadInterval" , "0.0" ) ; // Read interval in seconds
			put( "arcChopFrequency" , "0.0" ) ; // Chop frequency
			put( "arcChopDelay" , "0.0" ) ; // Chop delay

			put( "arcNumExp" , "1" ) ; // Number of arc exposures (coadds)

			put( "arcDutyCycle" , "0.0" ) ; // Dutycycle for the obs.
			// WFCAM
			// ====
		}
		else if( instrum.equalsIgnoreCase( "WFCAM" ) )
		{
			put( "instrument" , instrum ) ; // Instrument name
			put( "version" , "TBD" ) ; // Version number
			put( "name" , "TBD" ) ; // Name of configuration file
			put( "configType" , "Normal" ) ; // Type of configuration: always Normal
			put( "type" , "object" ) ; // Default "type" of WFCAM config
			put( "instPort" , "South" ) ; // Instrument port
			put( "filter" , "undefined" ) ; // Filter name
			put( "flatFilter" , "undefined" ) ;// Flat filter name
			put( "focusFilter" , "undefined" ) ;// Focus filter name
			put( "readMode" , "undefined" ) ; // Read mode
			put( "flatReadMode" , "undefined" ) ; // Flat read mode
			put( "focusReadMode" , "undefined" ) ; // Focus read mode
			put( "expTime" , "5.5" ) ; // Exposure time in seconds
			put( "objNumExp" , "1" ) ; // Number of object exposures (coadds)
			put( "darkNumExp" , "19" ) ; // Number of dark exposures (coadds)
			put( "flatNumExp" , "1" ) ; // Number of flat exposures (coadds)
			put( "biasNumExp" , "50" ) ; // Number of bias exposures (coadds)
			put( "focusNumExp" , "1" ) ; // Number of focus exposures (coadds)
		}
	}

	/** 
	 *  Accessor method to return the hash table.
	 */
	public Hashtable<String,String> config()
	{
		return instTable ;
	}

	/**
	 *  Deep-clone method required to copy the key-value configuration pairs.
	 */
	public InstConfig clone()
	{
		// Make a new Hashtable.
		InstConfig ht = new InstConfig() ;

		// Loop through all the elements.  Here we assume that the values are
		// scalar unless the instrument apertures.
		for( Enumeration<String> e = this.keys() ; e.hasMoreElements() ; )
		{
			key = e.nextElement() ;
			value = this.get( key ) ;
			ht.put( key , value ) ;
		}

		// Return the cloned hashtable.
		return ht ;
	}

	/**
	 *
	 * Translates the OT attribute names to internal Translator equivalents.
	 *
	 */
	public String OTToTranslator( String title , String attribute )
	{
		// Local variables
		String lcTitle ; // Lowercase title
		String lcAttribute ; // Lowercase attribute

		// Set a default value.  Many are the same name, so retain the supplied name unless otherwise translated.
		key = attribute ;

		// Use lower case for comparison purposes.
		lcTitle = title.toLowerCase() ;
		lcAttribute = attribute.toLowerCase() ;

		// The title defines which type of configuration.  Title="config"
		// is the general instrument config.  There are also special configs
		// for calibrations.  Within each type of configuration test for each value.
		if( lcTitle.equals( "config" ) )
		{
			// Here might need to allow for changes of OT nomenclature.
			if( lcAttribute.equals( ".version" ) )
				key = "version" ;
			else if( lcAttribute.equals( "acqmode" ) )
				key = "readMode" ;
			else if( lcAttribute.equals( "readoutarea" ) )
				key = "readArea" ;
			else if( lcAttribute.equals( "exposuretime" ) )
				key = "expTime" ;
			else if( lcAttribute.equals( "coadds" ) )
				key = "objNumExp" ;
			else if( lcAttribute.equals( "instaper" ) )
				key = "instAper" ;
			else if( lcAttribute.equals( "posangle" ) )
				key = "positionAngle" ;
			else if( lcAttribute.equals( "mask" ) )
				key = "slitWidth" ;
		}
		else if( lcTitle.equals( "arc" ) )
		{
			if( lcAttribute.equals( "coadds" ) )
				key = "arcNumExp" ;
			else if( lcAttribute.equals( "caltype" ) )
				key = "type" ;
			else if( lcAttribute.equals( "acqmode" ) )
				key = "arcReadMode" ;
			else if( lcAttribute.equals( "cvfwavelength" ) )
				key = "arcCvfWavelength" ;
			else if( lcAttribute.equals( "exposuretime" ) )
				key = "arcExpTime" ;
			else if( lcAttribute.equals( "observationtime" ) )
				key = "arcObsTime" ;
			//else if ( lcAttribute.equals( "sampling" ) ) key = "arcSampling" ;
			else if( lcAttribute.equals( "filter" ) )
				key = "arcFilter" ;
			else if( lcAttribute.equals( "lamp" ) )
				key = "arcCalLamp" ;
			else if( lcAttribute.equals( "arcsource" ) )
				key = "arcCalLamp" ;
			else if( lcAttribute.equals( "nreads" ) )
				key = "arcNreads" ;
			else if( lcAttribute.equals( "mode" ) )
				key = "arcMode" ;
			else if( lcAttribute.equals( "readinterval" ) )
				key = "arcReadInterval" ;
			else if( lcAttribute.equals( "chopfrequency" ) )
				key = "arcChopFrequency" ;
			else if( lcAttribute.equals( "chopdelay" ) )
				key = "arcChopDelay" ;
			else if( lcAttribute.equals( "resetdelay" ) )
				key = "arcResetDelay" ;
			else if( lcAttribute.equals( "nresets" ) )
				key = "arcNresets" ;
			else if( lcAttribute.equals( "waveform" ) )
				key = "arcWaveform" ;
			else if( lcAttribute.equals( "dutycycle" ) )
				key = "arcDutyCycle" ;
			else if( lcAttribute.equals( "mustidles" ) )
				key = "arcMustIdles" ;
			else if( lcAttribute.equals( "nullreads" ) )
				key = "arcNullReads" ;
			else if( lcAttribute.equals( "nullexposures" ) )
				key = "arcNullExposures" ;
			else if( lcAttribute.equals( "nullcycles" ) )
				key = "arcNullCycles" ;
			else if( lcAttribute.equals( "idleperiod" ) )
				key = "arcIdlePeriod" ;
		}
		else if( lcTitle.equals( "bias" ) )
		{
			if( lcAttribute.equals( "coadds" ) )
				key = "biasNumExp" ;
			else if( lcAttribute.equals( "exposuretime" ) )
				key = "biasExpTime" ;
		}
		else if( lcTitle.equals( "dark" ) )
		{
			if( lcAttribute.equals( "coadds" ) )
				key = "darkNumExp" ;
			else if( lcAttribute.equals( "exposuretime" ) )
				key = "darkExpTime" ;
			else if( lcAttribute.equals( "filter" ) )
				key = "darkFilter" ;
		}
		else if( ( lcTitle.equals( "flat" ) ) || ( lcTitle.equals( "skyflat" ) ) || ( lcTitle.equals( "domeflat" ) ) )
		{
			if( lcAttribute.equals( "coadds" ) )
				key = "flatNumExp" ;
			else if( lcAttribute.equals( "caltype" ) )
				key = "type" ;
			else if( lcAttribute.equals( "acqmode" ) )
				key = "flatReadMode" ;
			else if( lcAttribute.equals( "readmode" ) )
				key = "flatReadMode" ;
			else if( lcAttribute.equals( "exposuretime" ) )
				key = "flatExpTime" ;
			else if( lcAttribute.equals( "observationtime" ) )
				key = "flatObsTime" ;
			else if( lcAttribute.equals( "sampling" ) )
				key = "flatSampling" ;
			else if( lcAttribute.equals( "filter" ) )
				key = "flatFilter" ;
			else if( lcAttribute.equals( "lamp" ) )
				key = "flatCalLamp" ;
			else if( lcAttribute.equals( "neutraldensity" ) )
				key = "flatNeutralDensity" ;
			else if( lcAttribute.equals( "nreads" ) )
				key = "flatNreads" ;
			else if( lcAttribute.equals( "mode" ) )
				key = "flatMode" ;
			else if( lcAttribute.equals( "readinterval" ) )
				key = "flatReadInterval" ;
			else if( lcAttribute.equals( "chopfrequency" ) )
				key = "flatChopFrequency" ;
			else if( lcAttribute.equals( "chopdelay" ) )
				key = "flatChopDelay" ;
			else if( lcAttribute.equals( "resetdelay" ) )
				key = "flatResetDelay" ;
			else if( lcAttribute.equals( "nresets" ) )
				key = "flatNresets" ;
			else if( lcAttribute.equals( "waveform" ) )
				key = "flatWaveform" ;
			else if( lcAttribute.equals( "dutycycle" ) )
				key = "flatDutyCycle" ;
			else if( lcAttribute.equals( "mustidles" ) )
				key = "flatMustIdles" ;
			else if( lcAttribute.equals( "nullreads" ) )
				key = "flatNullReads" ;
			else if( lcAttribute.equals( "nullexposures" ) )
				key = "flatNullExposures" ;
			else if( lcAttribute.equals( "nullcycles" ) )
				key = "flatNullCycles" ;
			else if( lcAttribute.equals( "idleperiod" ) )
				key = "flatIdlePeriod" ;
		}
		else if( lcTitle.equals( "targetacq" ) )
		{
			if( lcAttribute.equals( "coadds" ) )
				key = "targetAcqNumExp" ;
			else if( lcAttribute.equals( "exposuretime" ) )
				key = "targetAcqExpTime" ;
			else if( lcAttribute.equals( "filter" ) )
				key = "targetAcqFilter" ;
			else if( lcAttribute.equals( "mask" ) )
				key = "targetAcqMask" ;
			else if( lcAttribute.equals( "maskwidth" ) )
				key = "targetAcqMaskWidth" ;
			else if( lcAttribute.equals( "maskheight" ) )
				key = "targetAcqMaskHeight" ;
			else if( lcAttribute.equals( "disperser" ) )
				key = "targetAcqDisperser" ;
			else if( lcAttribute.equals( "resolution" ) )
				key = "targetAcqResolution" ;
			else if( lcAttribute.equals( "dispersion" ) )
				key = "targetAcqDispersion" ;
			else if( lcAttribute.equals( "sciencearea" ) )
				key = "targetAcqScienceArea" ;
			else if( lcAttribute.equals( "pixelfov" ) )
				key = "targetAcqPixelFOV" ;
			else if( lcAttribute.equals( "nreads" ) )
				key = "targetAcqNreads" ;
			else if( lcAttribute.equals( "mode" ) )
				key = "targetAcqMode" ;
			else if( lcAttribute.equals( "exposuretime" ) )
				key = "targetAcqExpTime" ;
			else if( lcAttribute.equals( "readinterval" ) )
				key = "targetAcqReadInterval" ;
			else if( lcAttribute.equals( "chopfrequency" ) )
				key = "targetAcqChopFrequency" ;
			else if( lcAttribute.equals( "resetdelay" ) )
				key = "targetAcqResetDelay" ;
			else if( lcAttribute.equals( "nresets" ) )
				key = "targetAcqNresets" ;
			else if( lcAttribute.equals( "chopdelay" ) )
				key = "targetAcqChopDelay" ;
			else if( lcAttribute.equals( "waveform" ) )
				key = "targetAcqWaveform" ;
			else if( lcAttribute.equals( "dutycycle" ) )
				key = "targetAcqDutyCycle" ;
			else if( lcAttribute.equals( "mustidles" ) )
				key = "targetAcqMustIdles" ;
			else if( lcAttribute.equals( "nullreads" ) )
				key = "targetAcqNullReads" ;
			else if( lcAttribute.equals( "nullexposures" ) )
				key = "targetAcqNullExposures" ;
			else if( lcAttribute.equals( "nullcycles" ) )
				key = "targetAcqNullCycles" ;
			else if( lcAttribute.equals( "idleperiod" ) )
				key = "targetAcqIdlePeriod" ;
			else if( lcAttribute.equals( "observationtime" ) )
				key = "targetAcqObsTime" ;
			else if( lcAttribute.equals( "sampling" ) )
				key = "targetAcqSampling" ;
		}
		else if( lcTitle.equals( "focus" ) )
		{
			if( lcAttribute.equals( "coadds" ) )
				key = "focusNumExp" ;
			else if( lcAttribute.equals( "caltype" ) )
				key = "type" ;
			else if( lcAttribute.equals( "acqmode" ) )
				key = "focusReadMode" ;
			else if( lcAttribute.equals( "readmode" ) )
				key = "focusReadMode" ;
			else if( lcAttribute.equals( "exposuretime" ) )
				key = "focusExpTime" ;
			else if( lcAttribute.equals( "filter" ) )
				key = "focusFilter" ;
		}
		return key ;
	}

	/**
	 * Compares two configs.  Returns true if they are identical.
	 */

	boolean isSame( String instrum , InstConfig config )
	{
		Enumeration<String> ekey ; // Enumeration of the `this' config
		String key ; // A key in the hashTable
		boolean same ; // Configs are the same?

		// Assume that the result is the same unless a mismatch is found.
		same = true ;

		// Get an enumeration of the keys.
		ekey = keys() ;
		while( ekey.hasMoreElements() )
		{
			key = ekey.nextElement() ;

			// Check that the key is present in the second config.
			if( !config.containsKey( key ) )
			{
				same = false ;
				break ;
			}

			// Compare the values in the two configs.
			if( !get( key ).equals( config.get( key ) ) )
			{
				same = false ;
				break ;
			}
		}
		return same ;
	}

	/**
	 * Compares two configs.  Returns true if any of a supplied Vector of
	 * nominated attributes are not identical in both.
	 */

	boolean changedAttribute( InstConfig config , Vector<String> attributes )
	{
		boolean change ; // Any selected config attribute has changed?
		int i ; // Loop counter
		String key ; // A key in the hashTable

		// Assume that the result is false unless a mismatch is found.
		change = false ;

		// Loop through all the attributes to be tested.
		for( i = 0 ; i < attributes.size() ; i++ )
		{
			key = attributes.elementAt( i ) ;

			// Check that the key is present.  Just ignore missing keys.
			if( containsKey( key ) && config.containsKey( key ) )
			{

				// Compare the two values.  Exit the test once one attribute has a
				// different value.
				if( !get( key ).equals( config.get( key ) ) )
				{
					change = true ;
					break ;
				}

			}
		}
		return change ;
	}
}
