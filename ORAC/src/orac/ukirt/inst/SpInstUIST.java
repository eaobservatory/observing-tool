/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/
// This version started 2001 Oct 31 based on SpInstMichelle
// author: Alan Pickup = dap@roe.ac.uk         2001 Oct

package orac.ukirt.inst;
import java.io.*;
import java.util.*;

import orac.util.LookUpTable;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

import gemini.util.*;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpChopCapability;
import gemini.sp.obsComp.SpStareCapability;


/**
 * The UIST instrument.
 +
 + @author Alan Pickup
 */

public final class SpInstUIST extends SpUKIRTInstObsComp
{
    // Attributes presented to user
    public static String ATTR_CONFIG_TYPE        = "configType";
    public static String ATTR_CAMERA             = "camera";
    public static String ATTR_SOURCE_MAG         = "source_mag";
    public static String ATTR_POLARIMETRY        = "polarimetry";
// Added by RDK
    public static String ATTR_PUPIL_IMAGING      = "pupil_imaging";
// End of added by RDK
    public static String ATTR_IMAGER             = "imager";
    public static String ATTR_MASK               = "mask";
    public static String ATTR_MASK_WIDTH         = "maskWidth";
    public static String ATTR_MASK_HEIGHT        = "maskHeight";
    public static String ATTR_DISPERSER          = "disperser";
    public static String ATTR_ORDER              = "order";
    public static String ATTR_CENTRAL_WAVELENGTH = "centralWavelength";
    public static String ATTR_DISPERSION         = "dispersion";
    public static String ATTR_RESOLUTION         = "resolution";
    public static String ATTR_FILTER             = "filter";
    public static String ATTR_FILTER_CATEGORY    = "filterCategory";
    public static String ATTR_FOCUS              = "focus";
    public static String ATTR_SCIENCE_AREA       = "scienceArea";
    public static String ATTR_SPECTRAL_COVERAGE  = "spectralCoverage";
    public static String ATTR_PIXEL_FOV          = "pixelFOV";
    public static String ATTR_PIXEL_SCALE        = "pixelScale";
    public static String ATTR_DARKFILTER         = "darkFilter";
    public static String ATTR_READOUT            = "readout";
    public static String ATTR_READAREA           = "readArea";
// Commented by RDK
//    public static String ATTR_ARRAY_ANGLE        = "arrayAngle";
//     public static String ATTR_REF_PIXEL_X        = "refPixelX";
//     public static String ATTR_REF_PIXEL_Y        = "refPixelY";
//     public static String ATTR_REF_PIXEL_L        = "refPixelL";
//     public static String ATTR_REF_PIXEL_S        = "refPixelS";
// End of commented by RDK
    public static String ATTR_MODE               = "mode";
    public static String ATTR_CHOP_FREQUENCY     = "chopFrequency";
    public static String ATTR_CHOP_DELAY         = "chopDelay";
    public static String ATTR_READ_INTERVAL      = "readInterval";
// Commented by RDK
//     public static String ATTR_NRESETS            = "nresets";
    public static String ATTR_NREADS             = "nreads";
//     public static String ATTR_WAVEFORM           = "waveform";
//     public static String ATTR_RESET_DELAY        = "resetDelay";
//     public static String ATTR_IDLE_PERIOD        = "idlePeriod";
//     public static String ATTR_MUST_IDLES         = "mustIdles";
//     public static String ATTR_NULL_CYCLES        = "nullCycles";
//     public static String ATTR_NULL_EXPOSURES     = "nullExposures";
//     public static String ATTR_NULL_READS         = "nullReads";
// End of commented by RDK
    public static String ATTR_DUTY_CYCLE         = "dutyCycle";
    public static String ATTR_OBSERVATION_TIME   = "observationTime";
    public static String ATTR_EXPTIME_OT         = "expTimeOT";
// Commented by RDK
//    public static String ATTR_OBSTIME_OT         = "obsTimeOT";
// End of commented by RDK
// Added by RDK
    public static String ATTR_READOUT_OT         = "readoutOT";
    public static String ATTR_READMODE           = "readMode";
    public static String ATTR_DACONF             = "DAConf";
    public static String ATTR_DACONF_MINEXPT     = "DAConfMinExpT";
// End of added by RDK
// Commented out by RDK
//     public static String ATTR_BIAS_NRESETS       = "biasNResets";
//     public static String ATTR_BIAS_WAVEFORM      = "biasWaveform";
//     public static String ATTR_BIAS_IDLE_PERIOD   = "biasIdlePeriod";
//     public static String ATTR_BIAS_MUST_IDLES    = "biasMustIdles";
//     public static String ATTR_BIAS_READAREA      = "biasReadArea";
//     public static String ATTR_BIAS_REFPIXEL_X    = "biasRefPixelX";
//     public static String ATTR_BIAS_REFPIXEL_Y    = "biasRefPixelY";
//     public static String ATTR_BIAS_OBSTIME       = "biasObsTime";
//     public static String ATTR_BIAS_DUTYCYCLE     = "biasDutyCycle";
// End of commented out by RDK
    public static String NO_VALUE                = "none";
    public static final int CAMERA_IMAGING       = 0;
    public static final int CAMERA_SPECTROSCOPY  = 1;

    // Class variables representing defaults, LUTs, etc
    public static String CONFIG_TYPE;
    public static String VERSION;
    public static double DETANGLE;
    public static double PIXPITCH;
// Commented out by RDK
//     public static String DARKFILTER;
// End of commented out by RDK
    public static String[] CAMERAS;
    public static String[] CAMERAS_POL;
    public static String DEFAULT_CAMERA;
    public static String[] FLAT_SOURCES;
    public static String FLAT_SOURCE;
    public static String[] ARC_SOURCES;
    public static String ARC_SOURCE;
    public static String DEFAULT_ARC_SOURCE;
    public static String[] POLARIMETRY;
    public static String DEFAULT_POLARIMETRY;
// Added by RDK
    public static String[] PUPIL;
    public static String DEFAULT_PUPIL_IMAGING;
// End of added by RDK
    public static String DEFAULT_SOURCE_MAG;
    public static String DEFAULT_PIXEL_FOV;
    public static String DEFAULT_SCIENCE_AREA;
    // Imaging
    public static String DEFAULT_IMAGER;
    public static String GRISM_IMAGING;
    public static LookUpTable IMAGERS;
    public static LookUpTable FILTERS1;
    public static LookUpTable FILTERS2;
    public static LookUpTable FILTERS3;
    public static LookUpTable FILTERS4;
    public static LookUpTable FILTERS5;
    public static LookUpTable FILTERS6;
    public static LookUpTable FILTERS7;
    public static LookUpTable FILTERS8;
    public static String DEFAULT_FILTERS1;
    public static String DEFAULT_FILTERS2;
    public static String DEFAULT_FILTERS3;
    public static String DEFAULT_FILTERS4;
    public static String DEFAULT_FILTERS5;
    public static String DEFAULT_FILTERS6;
    public static String DEFAULT_FILTERS7;
    public static String DEFAULT_FILTERS8;
    public static String DEFAULT_MAG1;
    public static String DEFAULT_MAG2;
    public static String DEFAULT_MAG3;
    public static String DEFAULT_MAG4;
    public static String DEFAULT_MAG5;
    public static String DEFAULT_MAG6;
    public static String DEFAULT_MAG7;
    public static String DEFAULT_MAG8;
    public static String DEFAULT_FILTER_CATEGORY;
    public static LookUpTable FILTERS;
    public static double DEFAULT_IMAGING_POS_ANGLE;
    // Spectroscopy
    public static double SPECT_FOCAL_LENGTH;
    public static double[] SPECT_FIELD_OF_VIEW;
// Commented out by RDK
//     public static String[] DISPERSERCHOICES;
//     public static String[] DISPERSERCHOICESPOL;
//     public static String[] DISPERSERCHOICESIFU;
// End of commented out by RDK
// Added by RDK
    public static String[] DISPERSER_CHOICES;
    public static String[] DISPERSER_CHOICES_POL;
    public static String[] DISPERSER_CHOICES_IFU;
    public static String[] DISPERSER_CHOICES_ACQ;
    public static String[] DISPERSER_CHOICES_POL_ACQ;
    public static String[] DISPERSER_CHOICES_IFU_ACQ;
// End of added by RDK
    public static LookUpTable DISPERSERS;;
    public static LookUpTable SPECTFILTERS;
    public static String DEFAULT_DISPERSER;
    public static int CENTRAL_ROW;
    public static int PEAK_ROW;
    public static LookUpTable SPECMAGS;
    public static String[] MASKS1;
    public static String[] MASKS2;
    public static String[] MASKS3;
    public static String[] MASKS4;
    public static String[] MASKS5;
    public static String[] MASKS6;
    public static String[] MASKS7;
    public static String[] MASKS8;
    public static String[] MASKS9;
    public static LookUpTable MASKS;
    public static String DEFAULT_MASK1;
    public static String DEFAULT_MASK2;
    public static String DEFAULT_MASK3;
    public static String DEFAULT_MASK4;
    public static String DEFAULT_MASK5;
    public static String DEFAULT_MASK6;
    public static String DEFAULT_MASK7;
    public static String DEFAULT_MASK8;
    public static String DEFAULT_MASK9;
// Commented out by RDK
//     public static String PUPIL_SCALE;
//     public static String PUPIL_FOV;
// End of commented out by RDK
    public static double DEFAULT_SPECT_POS_ANGLE;
    // Polarimetry
    public static String POL_MASK_IMAGING;
    public static String POL_GRISM_IMAGING;
    public static String POL_MASK_SPECTROSCOPY;
    // Data acquisition - general
// Commented out by RDK
//     public static double ARRAY_ANGLE;
// End of commented out by RDK
    public static LookUpTable READOUTS_IM;
    public static LookUpTable READOUTS_SPEC;
    public static LookUpTable READOUTS_IFU;
    public static double EXPTIME_MIN;
    public static double EXPTIME_MIN_IM;
    public static double EXPTIME_MIN_SPEC;
    public static double EXPTIME_MIN_IFU;
    public static double EXPTIME_ND;
    public static LookUpTable CHOPS;
    public static double TEXPMAX;
    public static double DEFAULT_TOBS;
    public static String DEFAULT_MODE;
    public static double DEFAULT_EXPTIME;
// Added by RDK
    public static String DEFAULT_AREA;
    public static int DEFAULT_COADDS;
    public static int FLAT_COADDS_IMAGING;
    public static int FLAT_COADDS_SPECT;
    public static int ARC_COADDS;
// End of added by RDK
// Commented out by RDK
//    public static double FLAT_OBSTIME_IMAGING;
//    public static double FLAT_OBSTIME_SPECT;
// End of commented out by RDK
    public static double ARC_EXPTIME;
// Commented out by RDK
//    public static double ARC_OBSTIME;
// End of commented out by RDK
    public static double EXPOSURE_OVERHEAD = 0.01;
    public static double READ_INTERVAL;
// Commented out by RDK
//     public static int NULL_READS;
//     public static double RESET_DELAY;
//     public static LookUpTable DACONFS;
//     public static LookUpTable MODES;
//     public static LookUpTable WAVEFORMS;
// End of commented out by RDK
// Added by RDK
    public static LookUpTable MODES_OT;
    public static String MIN_MAG_TRAGET_ACQ;
// End of added by RDK
    // Data acquisition - bias
// Commented by RDK
//    public static double DEFBIASEXPTIME;
//     public static int DEFBIASCOADDS;
//     public static int BIASNRESETS;
//     public static String BIASWAVEFORM;
//     public static double BIASIDLEPERIOD;
//     public static int BIASMUSTIDLES;
//     public static String BIASREADAREA;
//     public static double BIASREFPIXELX;
//     public static double BIASREFPIXELY;
//     public static double BIASOBSTIME;
//     public static double BIASDUTYCYCLE;
    // Instance variables of the class
//     double flatObservationTime;
//     double arcObservationTime;
// End of commented by RDK
// Added by RDK
    int flatCoadds;
    int arcCoadds;
// End of added by RDK
    double flatExposureTime;
    double arcExposureTime;
    // Data acquisition - dark
    // Data acquisition - flat & arcs
    String flat_source;
    String arc_source;
    public String W_mode;
// Commented by RDK
//     public String W_waveform;
    public int W_nreads;
//     public int W_nresets;
//     public double W_resetDelay;
// End of commented by RDK
    public double W_readInterval;
// Commented by RDK
//     public double W_idlePeriod;
//     public int W_mustIdles;
//     public int W_nullCycles;
//     public int W_nullExposures;
//     public int W_nullReads;
// End of commented by RDK
    public String W_chopFrequency;
    public double W_chopDelay;
    public int W_coadds;
    public double W_dutyCycle;
    public double W_obsTime;
    public double W_actExpTime;

    public static final SpType SP_TYPE = SpType.create(
        SpType.OBSERVATION_COMPONENT_TYPE, "inst.UIST", "UIST");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpInstUIST());
    }

    // Constructor reads instrument .cfg file and initialises values
    public SpInstUIST()
    {
        super(SP_TYPE);

        addCapability( new SpChopCapability()  );
        addCapability( new SpStareCapability() );

        // Read in the instrument config file
        String baseDir = System.getProperty("ot.cfgdir");
        String cfgFile = baseDir + "uist.cfg";
        _readCfgFile (cfgFile);

        // Set the initial values of the attributes
        String attr  = ATTR_CONFIG_TYPE;
        String value = CONFIG_TYPE;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_INSTRUMENT_PORT;
        value = INSTRUMENT_PORT;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_INSTRUMENT_PNTG_OFFSET;
        value = INSTRUMENT_PNTG_OFFSET[IDPO_INDEX];
	_avTable.noNotifySet(attr, value, IDPO_INDEX);
        value = INSTRUMENT_PNTG_OFFSET[CHPO_INDEX];
	_avTable.noNotifySet(attr, value, CHPO_INDEX);

        attr  = ATTR_VERSION;
        value = VERSION;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_CAMERA;
        value = DEFAULT_CAMERA;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_SOURCE_MAG;
        value = DEFAULT_SOURCE_MAG;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_IMAGER;
        value = DEFAULT_IMAGER;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_MASK;
        int mi = IMAGERS.indexInColumn(DEFAULT_IMAGER,0);
// Added by RDK
        String mask = (String) IMAGERS.elementAt(mi,3);
// End of added by RDK
// Commented out by RDK
//        String mask = (String) IMAGERS.elementAt(mi,4);
// End of commented out by RDK
        _avTable.noNotifySet(attr, mask, 0);
        setInstAper();

        attr  = ATTR_MASK_WIDTH;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr  = ATTR_MASK_HEIGHT;
        _avTable.noNotifySet(attr, "0.0", 0);

        setDefaultPosAngle();

        attr  = ATTR_DISPERSER;
        value = GRISM_IMAGING;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_ORDER;
        _avTable.noNotifySet(attr, "0", 0);

        attr  = ATTR_DISPERSION;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr  = ATTR_RESOLUTION;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr  = ATTR_POLARIMETRY;
        value = DEFAULT_POLARIMETRY;
        _avTable.noNotifySet(attr, value, 0);

// Added by RDK
        attr  = ATTR_PUPIL_IMAGING;
        value = DEFAULT_PUPIL_IMAGING;
        _avTable.noNotifySet(attr, value, 0);

// End of added by RDK

        attr  = ATTR_FILTER;
        value = DEFAULT_FILTERS1;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_FILTER_CATEGORY;
        value = DEFAULT_FILTER_CATEGORY;
        _avTable.noNotifySet(attr, value, 0);

// Commented out by RDK
//         attr  = ATTR_DARKFILTER;
//         value = DARKFILTER;
//         _avTable.noNotifySet(attr, value, 0);
// End of commented out by RDK

        int fi = FILTERS.indexInColumn(DEFAULT_FILTERS1,0);
        String cwl = (String) FILTERS.elementAt(fi,1);
        attr  = ATTR_CENTRAL_WAVELENGTH;
        _avTable.noNotifySet(attr, cwl, 0);
        String sc = (String) FILTERS.elementAt(fi,2);
        attr  = ATTR_SPECTRAL_COVERAGE;
        _avTable.noNotifySet(attr, sc, 0);

        attr  = ATTR_FOCUS;
        fi = FILTERS1.indexInColumn(DEFAULT_FILTERS1,0);
        String focus = (String) FILTERS1.elementAt(fi,1);
        _avTable.noNotifySet(attr, focus, 0);

        attr  = ATTR_SCIENCE_AREA;
        value = DEFAULT_SCIENCE_AREA;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_PIXEL_FOV;
        value = DEFAULT_PIXEL_FOV;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_PIXEL_SCALE;
        _avTable.noNotifySet(attr, "0.12", 0);

        attr  = ATTR_MODE;
        value = DEFAULT_MODE;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_READOUT;
        value = DEFAULT_MODE;
        _avTable.noNotifySet(attr, value, 0);

// Commented out by RDK
//         attr  = ATTR_ARRAY_ANGLE;
//         value = Double.toString(ARRAY_ANGLE);
//         _avTable.noNotifySet(attr, value, 0);
//        attr  = ATTR_READAREA;
//        _avTable.noNotifySet(attr, "1024x1024", 0);
// End of commented out by RDK


//Added by RDK
        attr  = ATTR_READMODE;
        value = DEFAULT_MODE;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_READAREA;
        value = DEFAULT_AREA;
        _avTable.noNotifySet(attr, value, 0);
//End of added by RDK
// Commented by RDK
//         attr  = ATTR_REF_PIXEL_X;
//         _avTable.noNotifySet(attr, "512.5", 0);

//         attr  = ATTR_REF_PIXEL_Y;
//         _avTable.noNotifySet(attr, "512.5", 0);

//         attr  = ATTR_REF_PIXEL_L;
//         _avTable.noNotifySet(attr, "512.5", 0);

//         attr  = ATTR_REF_PIXEL_S;
//         _avTable.noNotifySet(attr, "512.5", 0);
// End of commented by RDK

        attr  = ATTR_CHOP_FREQUENCY;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr  = ATTR_CHOP_DELAY;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr  = ATTR_EXPOSURE_TIME;
        value = Double.toString(DEFAULT_EXPTIME);
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_EXPTIME_OT;
        value = Double.toString(DEFAULT_EXPTIME);
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_READ_INTERVAL;
        value = Double.toString(READ_INTERVAL);
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_NREADS;
        _avTable.noNotifySet(attr, "0", 0);

// Commented out by RDK
//         attr  = ATTR_NRESETS;
//         _avTable.noNotifySet(attr, "0", 0);

//         attr  = ATTR_IDLE_PERIOD;
//         _avTable.noNotifySet(attr, "0.0", 0);

//         attr  = ATTR_MUST_IDLES;
//         _avTable.noNotifySet(attr, "0", 0);

//         attr  = ATTR_RESET_DELAY;
//         value = Double.toString(RESET_DELAY);
//         _avTable.noNotifySet(attr, value, 0);

//         attr  = ATTR_NULL_CYCLES;
//         _avTable.noNotifySet(attr, "0", 0);

//         attr  = ATTR_NULL_EXPOSURES;
//         _avTable.noNotifySet(attr, "0", 0);

//         attr  = ATTR_NULL_READS;
//         _avTable.noNotifySet(attr, "0", 0);

//         attr  = ATTR_WAVEFORM;
//         _avTable.noNotifySet(attr, "unspecified", 0);
// End of commented out by RDK

// Added by RDK
        //       attr  = ATTR_COADDS;
        //value = Double.toString(DEFAULT_COADDS);
        setCoadds( DEFAULT_COADDS );
//        _avTable.noNotifySet(attr, value, 0);
// End of added by RDK

        attr  = ATTR_DUTY_CYCLE;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr  = ATTR_OBSERVATION_TIME;
        value = Double.toString(DEFAULT_TOBS);
        _avTable.noNotifySet(attr, value, 0);

// Commented by RDK
//         attr  = ATTR_OBSTIME_OT;
//         value = Double.toString(DEFAULT_TOBS);
//         _avTable.noNotifySet(attr, value, 0);

//         attr  = ATTR_OBSTIME_OT;
//         value = Double.toString(DEFAULT_TOBS);
//         _avTable.noNotifySet(attr, value, 0);

//         attr  = ATTR_BIAS_NRESETS;
//         value = Integer.toString(BIASNRESETS);
//         _avTable.noNotifySet(attr, value, 0);

//         attr  = ATTR_BIAS_WAVEFORM;
//         value = BIASWAVEFORM;
//         _avTable.noNotifySet(attr, value, 0);

//         attr  = ATTR_BIAS_IDLE_PERIOD;
//         value = Double.toString(BIASIDLEPERIOD);
//         _avTable.noNotifySet(attr, value, 0);

//         attr  = ATTR_BIAS_MUST_IDLES;
//         value = Integer.toString(BIASMUSTIDLES);
//         _avTable.noNotifySet(attr, value, 0);

//         attr  = ATTR_BIAS_READAREA;
//         value = BIASREADAREA;
//         _avTable.noNotifySet(attr, value, 0);

//         attr  = ATTR_BIAS_REFPIXEL_X;
//         value = Double.toString(BIASREFPIXELX);
//         _avTable.noNotifySet(attr, value, 0);

//         attr  = ATTR_BIAS_REFPIXEL_Y;
//         value = Double.toString(BIASREFPIXELY);
//         _avTable.noNotifySet(attr, value, 0);

//         attr  = ATTR_BIAS_OBSTIME;
//         value = Double.toString(BIASOBSTIME);
//         _avTable.noNotifySet(attr, value, 0);

//         attr  = ATTR_BIAS_DUTYCYCLE;
//         value = Double.toString(BIASDUTYCYCLE);
//        _avTable.noNotifySet(attr, value, 0);
// End of commented by RDK

        // Initialise instance variables
        initInstance();

    }

    private void _readCfgFile (String filename) {
        InstCfgReader instCfg = null;
        InstCfg instInfo = null;
        String block = null;
        int i;
        instCfg = new InstCfgReader (filename);
        try {
            while ((block = instCfg.readBlock()) != null) {
                instInfo = new InstCfg (block);
		if (InstCfg.matchAttr (instInfo, "instrument_port")) {
                    INSTRUMENT_PORT = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "instrument_pointing_offsets")) {
                   INSTRUMENT_PNTG_OFFSET  = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "config_type")) {
                    CONFIG_TYPE = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "version")) {
                    VERSION = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "detangle")) {
                    DETANGLE = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "pixpitch")) {
                    PIXPITCH = Double.valueOf(instInfo.getValue()).doubleValue();
// Commented out by RDK
// 		} else if (InstCfg.matchAttr (instInfo, "darkfilter")) {
//                     DARKFILTER = instInfo.getValue();
// End of commented out by RDK
		} else if (InstCfg.matchAttr (instInfo, "cameras")) {
                    CAMERAS = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "cameras_pol")) {
                    CAMERAS_POL = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "default_camera")) {
                    DEFAULT_CAMERA = instInfo.getValue();
// Added by RDK
		} else if (InstCfg.matchAttr (instInfo, "default_pupil_imaging")) {
                    DEFAULT_PUPIL_IMAGING = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "pupil")) {
                    PUPIL = instInfo.getValueAsArray();
// End of added by RDK
		} else if (InstCfg.matchAttr (instInfo, "default_source_mag")) {
                    DEFAULT_SOURCE_MAG = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "polarimetry")) {
                    POLARIMETRY = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "default_polarimetry")) {
                    DEFAULT_POLARIMETRY = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "imagers")) {
                    IMAGERS = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "default_imager")) {
                    DEFAULT_IMAGER = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "grism_imaging")) {
                    GRISM_IMAGING  = instInfo.getValue();
// Commented out by RDK
// 		} else if (InstCfg.matchAttr (instInfo, "pupil_scale")) {
//                     PUPIL_SCALE  = instInfo.getValue();
// 		} else if (InstCfg.matchAttr (instInfo, "pupil_fov")) {
//                     PUPIL_FOV  = instInfo.getValue();
// End of commented out by RDK
		} else if (InstCfg.matchAttr (instInfo, "default_pixel_fov")) {
                    DEFAULT_PIXEL_FOV  = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_science_area")) {
                    DEFAULT_SCIENCE_AREA  = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_imaging_pos_angle")) {
                    DEFAULT_IMAGING_POS_ANGLE = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "default_spect_pos_angle")) {
                    DEFAULT_SPECT_POS_ANGLE = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "filters1")) {
                    FILTERS1 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "filters2")) {
                    FILTERS2 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "filters3")) {
                    FILTERS3 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "filters4")) {
                    FILTERS4 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "filters5")) {
                    FILTERS5 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "filters6")) {
                    FILTERS6 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "filters7")) {
                    FILTERS7 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "filters8")) {
                    FILTERS8 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "default_filters1")) {
                    DEFAULT_FILTERS1 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_filters2")) {
                    DEFAULT_FILTERS2 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_filters3")) {
                    DEFAULT_FILTERS3 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_filters4")) {
                    DEFAULT_FILTERS4 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_filters5")) {
                    DEFAULT_FILTERS5 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_filters6")) {
                    DEFAULT_FILTERS6 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_filters7")) {
                    DEFAULT_FILTERS7 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_filters8")) {
                    DEFAULT_FILTERS8 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mag1")) {
                    DEFAULT_MAG1 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mag2")) {
                    DEFAULT_MAG2 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mag3")) {
                    DEFAULT_MAG3 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mag4")) {
                    DEFAULT_MAG4 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mag5")) {
                    DEFAULT_MAG5 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mag6")) {
                    DEFAULT_MAG6 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mag7")) {
                    DEFAULT_MAG7 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mag8")) {
                    DEFAULT_MAG8 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_filter_category")) {
                    DEFAULT_FILTER_CATEGORY = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "filters")) {
                    FILTERS = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "spect_focal_length")) {
                    SPECT_FOCAL_LENGTH = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "flat_sources")) {
                    FLAT_SOURCES = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "arc_sources")) {
                    ARC_SOURCES = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "default_arc_source")) {
                    DEFAULT_ARC_SOURCE = instInfo.getValue();
// Commented out by RDK
// 		} else if (InstCfg.matchAttr (instInfo, "disperserchoices")) {
//                     DISPERSERCHOICES = instInfo.getValueAsArray();
// 		} else if (InstCfg.matchAttr (instInfo, "disperserchoicespol")) {
//                     DISPERSERCHOICESPOL = instInfo.getValueAsArray();
// 		} else if (InstCfg.matchAttr (instInfo, "disperserchoicesifu")) {
//                     DISPERSERCHOICESIFU = instInfo.getValueAsArray();
// End of commented out by RDK
// Added by RDK
		} else if (InstCfg.matchAttr (instInfo, "disperser_choices")) {
                    DISPERSER_CHOICES = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "disperser_choices_pol")) {
                    DISPERSER_CHOICES_POL = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "disperser_choices_ifu")) {
                    DISPERSER_CHOICES_IFU = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "disperser_choices_acq")) {
                    DISPERSER_CHOICES_ACQ = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "disperser_choices_ifu_acq")) {
                    DISPERSER_CHOICES_IFU_ACQ = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "disperser_choices_pol_acq")) {
                    DISPERSER_CHOICES_POL_ACQ = instInfo.getValueAsArray();
// End of added by RDK
		} else if (InstCfg.matchAttr (instInfo, "dispersers")) {
                    DISPERSERS = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "spectfilters")) {
                    SPECTFILTERS = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "default_disperser")) {
                    DEFAULT_DISPERSER = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "pol_mask_imaging")) {
                    POL_MASK_IMAGING = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "pol_grism_imaging")) {
                    POL_GRISM_IMAGING = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "pol_mask_spectroscopy")) {
                    POL_MASK_SPECTROSCOPY = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "central_row")) {
                    CENTRAL_ROW = Integer.parseInt(instInfo.getValue());
		} else if (InstCfg.matchAttr (instInfo, "peak_row")) {
                    PEAK_ROW = Integer.parseInt(instInfo.getValue());
		} else if (InstCfg.matchAttr (instInfo, "specmags")) {
                    SPECMAGS = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "masks1")) {
                    MASKS1 = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "masks2")) {
                    MASKS2 = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "masks3")) {
                    MASKS3 = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "masks4")) {
                    MASKS4 = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "masks5")) {
                    MASKS5 = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "masks6")) {
                    MASKS6 = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "masks7")) {
                    MASKS7 = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "masks8")) {
                    MASKS8 = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "masks9")) {
                    MASKS9 = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "masks")) {
                    MASKS = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "default_mask1")) {
                    DEFAULT_MASK1 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mask2")) {
                    DEFAULT_MASK2 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mask3")) {
                    DEFAULT_MASK3 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mask4")) {
                    DEFAULT_MASK4 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mask5")) {
                    DEFAULT_MASK5 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mask6")) {
                    DEFAULT_MASK6 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mask7")) {
                    DEFAULT_MASK7 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mask8")) {
                    DEFAULT_MASK8 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mask9")) {
                    DEFAULT_MASK9 = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "chops")) {
                    CHOPS = instInfo.getValueAsLUT();
// Commented out by RDK
// 		} else if (InstCfg.matchAttr (instInfo, "array_angle")) {
//                     ARRAY_ANGLE = Double.valueOf(instInfo.getValue()).doubleValue();
// End of commented out by RDK
		} else if (InstCfg.matchAttr (instInfo, "readouts_im")) {
                    READOUTS_IM = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "readouts_spec")) {
                    READOUTS_SPEC = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "readouts_ifu")) {
                    READOUTS_IFU = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "exptime_min")) {
                    EXPTIME_MIN = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "exptime_min_im")) {
                    EXPTIME_MIN_IM = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "exptime_min_spec")) {
                    EXPTIME_MIN_SPEC = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "exptime_min_ifu")) {
                    EXPTIME_MIN_IFU = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "exptime_nd")) {
                    EXPTIME_ND = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "default_mode")) {
                    DEFAULT_MODE = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_exptime")) {
                    DEFAULT_EXPTIME = Double.valueOf(instInfo.getValue()).doubleValue();
// Added by RDK
		} else if (InstCfg.matchAttr (instInfo, "default_area")) {
                    DEFAULT_AREA = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_coadds")) {
                    DEFAULT_COADDS = Integer.valueOf(instInfo.getValue()).intValue();
		} else if (InstCfg.matchAttr (instInfo, "flat_coadds_imaging")) {
                    FLAT_COADDS_IMAGING = Integer.valueOf(instInfo.getValue()).intValue();
		} else if (InstCfg.matchAttr (instInfo, "flat_coadds_spect")) {
                    FLAT_COADDS_SPECT = Integer.valueOf(instInfo.getValue()).intValue();
		} else if (InstCfg.matchAttr (instInfo, "arc_coadds")) {
                    ARC_COADDS = Integer.valueOf(instInfo.getValue()).intValue();
// End of added by RDK
// Commented by RDK
// 		} else if (InstCfg.matchAttr (instInfo, "flat_obstime_imaging")) {
//                     FLAT_OBSTIME_IMAGING = Double.valueOf(instInfo.getValue()).doubleValue();
// 		} else if (InstCfg.matchAttr (instInfo, "flat_obstime_spect")) {
//                     FLAT_OBSTIME_SPECT = Double.valueOf(instInfo.getValue()).doubleValue();
// End of commented by RDK
		} else if (InstCfg.matchAttr (instInfo, "arc_exptime")) {
                    ARC_EXPTIME = Double.valueOf(instInfo.getValue()).doubleValue();
// Commented by RDK
// 		} else if (InstCfg.matchAttr (instInfo, "arc_obstime")) {
//                     ARC_OBSTIME = Double.valueOf(instInfo.getValue()).doubleValue();
// End of added by RDK
		} else if (InstCfg.matchAttr (instInfo, "texpmax")) {
                    TEXPMAX = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "default_tobs")) {
                    DEFAULT_TOBS = Double.valueOf(instInfo.getValue()).doubleValue();
// Commented out by RDK
// 		} else if (InstCfg.matchAttr (instInfo, "defbiasexptime")) {
//                     DEFBIASEXPTIME = Double.valueOf(instInfo.getValue()).doubleValue();
// 		} else if (InstCfg.matchAttr (instInfo, "defbiascoadds")) {
//                     DEFBIASCOADDS = Integer.parseInt(instInfo.getValue());
// 		} else if (InstCfg.matchAttr (instInfo, "biasnresets")) {
//                     BIASNRESETS = Integer.parseInt(instInfo.getValue());
// 		} else if (InstCfg.matchAttr (instInfo, "biaswaveform")) {
//                     BIASWAVEFORM = instInfo.getValue();
// 		} else if (InstCfg.matchAttr (instInfo, "biasidleperiod")) {
//                     BIASIDLEPERIOD = Double.valueOf(instInfo.getValue()).doubleValue();
// 		} else if (InstCfg.matchAttr (instInfo, "biasmustidles")) {
//                     BIASMUSTIDLES = Integer.parseInt(instInfo.getValue());
// 		} else if (InstCfg.matchAttr (instInfo, "biasreadarea")) {
//                     BIASREADAREA = instInfo.getValue();
// 		} else if (InstCfg.matchAttr (instInfo, "biasrefpixelx")) {
//                     BIASREFPIXELX = Double.valueOf(instInfo.getValue()).doubleValue();
// 		} else if (InstCfg.matchAttr (instInfo, "biasrefpixely")) {
//                     BIASREFPIXELY = Double.valueOf(instInfo.getValue()).doubleValue();
// 		} else if (InstCfg.matchAttr (instInfo, "biasobstime")) {
//                     BIASOBSTIME = Double.valueOf(instInfo.getValue()).doubleValue();
// 		} else if (InstCfg.matchAttr (instInfo, "biasdutycycle")) {
//                     BIASDUTYCYCLE = Double.valueOf(instInfo.getValue()).doubleValue();
// End of commented out by RDK
		} else if (InstCfg.matchAttr (instInfo, "read_interval")) {
                    READ_INTERVAL = Double.valueOf(instInfo.getValue()).doubleValue();
// Commented out by RDK
// 		} else if (InstCfg.matchAttr (instInfo, "null_reads")) {
//                     NULL_READS = Integer.parseInt(instInfo.getValue());
// 		} else if (InstCfg.matchAttr (instInfo, "reset_delay")) {
//                     RESET_DELAY = Double.valueOf(instInfo.getValue()).doubleValue();
// 		} else if (InstCfg.matchAttr (instInfo, "daconfs")) {
//                     DACONFS = instInfo.getValueAsLUT();
// 		} else if (InstCfg.matchAttr (instInfo, "modes")) {
//                     MODES = instInfo.getValueAsLUT();
// 		} else if (InstCfg.matchAttr (instInfo, "waveforms")) {
//                     WAVEFORMS = instInfo.getValueAsLUT();
// End of commented out by RDK
// Added by RDK
		} else if (InstCfg.matchAttr (instInfo, "modes_ot")) {
                    MODES_OT = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "min_mag_target_acq")) {
                    MIN_MAG_TRAGET_ACQ = instInfo.getValue();
// End of added by RDK
		} else {
                    System.out.println("Unmatched keyword:" + instInfo.getKeyword());
		}
	    }
	}catch (IOException e) {
	    System.out.println ("Error reading UIST inst. cfg file");
	}
    }

    /**
     * Get the chop capability.
     */
    public SpChopCapability
    getChopCapability()
    {
        return (SpChopCapability) getCapability(SpChopCapability.CAPABILITY_NAME);
    }

    /**
     * Get the stare capability.
     */
    public SpStareCapability
    getStareCapability()
    {
        return (SpStareCapability) getCapability(SpStareCapability.CAPABILITY_NAME);
    }

    /**
     * Use default source magnitude
     */
    public void
    useDefaultSourceMag()
    {
        _avTable.rm(ATTR_SOURCE_MAG);
    }


    /**
     * Get the source magnitude.
     */
    public String getSourceMag() {

        String sm = _avTable.get(ATTR_SOURCE_MAG);
        if ( sm == null ) {
            if (isImaging()) {
                int filterSet = getFilterSet();
                if (filterSet==1) {
                    sm = DEFAULT_MAG1;
                } else if (filterSet==2) {
                    sm = DEFAULT_MAG2;
                } else if (filterSet==3) {
                    sm = DEFAULT_MAG3;
                } else if (filterSet==4) {
                    sm = DEFAULT_MAG4;
                } else if (filterSet==5) {
                    sm = DEFAULT_MAG5;
                } else if (filterSet==6) {
                    sm = DEFAULT_MAG6;
                } else if (filterSet==7) {
                    sm = DEFAULT_MAG7;
                } else if (filterSet==8) {
                    sm = DEFAULT_MAG8;
                } else {
                    System.out.println("GetFilterLUT> Unrecognised filterSet " + 
                        "number " + filterSet);
                    sm = DEFAULT_MAG1;
	        }
            } else {
                sm = DEFAULT_SOURCE_MAG;
	    }
            setSourceMag(sm);
	}
        return sm;
    }

    /**
     * Set the source magnitude.
     */
    public void setSourceMag( String sm ) {
        _avTable.set( ATTR_SOURCE_MAG, sm );
    }

    /**
     * Get the list of available source mags
     */
    public String[]
    getSourceMagList()
    {
        if (isImaging()) {
            return getFilterMags();
        } else {
            int nmags = SPECMAGS.getNumColumns() - 1;
            String specMags[] = new String[nmags];
            for (int i=0; i<nmags; i++) {
                specMags[i] = (String)SPECMAGS.elementAt(0,i+1);
	    }
            return specMags;
	}
    }

// Commented out by RDK
//     /**
//      * Set the pixel field of view
//      */
//     public void
//     setPixelFOV(String pixelFOV)
//     {
//         _avTable.set(ATTR_PIXEL_FOV, pixelFOV);
//     }

//     /**
//      * Return the pixel field of view
//      */
//     public double[]
//     getPixelFOV()
//     {
//         double pfov[] = new double[2];
//         double ps = getPixelScale();
//         pfov[0] = ps;
//         pfov[1] = ps;
//         return pfov;
//     }

//     /**
//      * Get the pixel field of view as a string
//      */
//     public String
//     getPixelFOVString()
//     {
//         String pfovs = null;
//         double ps = getPixelScale();
//         if (ps < 0.001) {
//             pfovs = PUPIL_SCALE;
// 	} else {
//             String pss = Double.toString(ps);
//             pfovs = pss + " x " + pss;
// 	}
//         setPixelFOV(pfovs);
//         return pfovs;
//     }
// End of commented out by RDK

    /**
     * Set the science area
     */
    public void
    setScienceArea(String scienceArea)
    {
        _avTable.set(ATTR_SCIENCE_AREA, scienceArea);
    }
  
    /**
     * Return the science area based upon the current camera and waveform
     */
    public double[]
    getScienceArea()
    {
         double fov[] = new double[2];
         double pixelScale = getPixelScale();
         int ra[] = getReadArea();
         double maxWidth  = ra[0] * pixelScale;
         double maxHeight = ra[1] * pixelScale;
         if (isImaging()) {
             if (isPolarimetry()) {
                 fov[1] = getMaskWidthPixels() * pixelScale;;
                 fov[0] = getMaskHeightArcsec();
	     } else {
	         fov[0] = maxWidth;
                 fov[1] = maxHeight;
	     }
	 } else {
             fov[0] = getMaskWidthPixels() * pixelScale;;
             fov[1] = getMaskHeightArcsec();
         }
         // Adjust the height and width if vignetted by the readout area
         if (fov[0] > maxWidth) fov[0] = maxWidth;
         if (fov[1] > maxHeight) fov[1] = maxHeight;

         return fov;
    }

    /**
     * Get the science area as a string (also updates pixel field of view)
     */
    public String
    getScienceAreaString()
    {
        double fov[];
        String fovs;
        fov = getScienceArea();
        if (fov[0] == 0) {
            fovs = PUPIL[4];
	} else {
            double w = MathUtil.round(fov[0], 2);
            double h = MathUtil.round(fov[1], 2);
            fovs = w + " x " + h + " arcsec";
	}
        setScienceArea(fovs);
        // Update the pixel field of view too
// Commented out by RDK
//         String pfovs = getPixelFOVString();
// End of commented out by RDK
        return fovs;
    }

    /**
     * Is the instrument in "imaging" (in other words, is the "imaging"
     * camera selected instead of the spectroscopy camera).
     */
    public boolean
    isImaging()
    {
        String camera = getCamera();
        return (camera.equalsIgnoreCase("imaging"));
    }

    /**
     * Set the camera.
     */
    public void
    setCamera(String camera)
    {
        _avTable.set(ATTR_CAMERA, camera);
        useDefaultImager();
        useDefaultDisperser();
        useDefaultSourceMag();
        setDefaultPosAngle();
    }

    /**
     * Initialise instance variables
     */
    public void
    initInstance()
    {
        // Initialise instance variables and initial config

// Commented by RDK
//         flatObservationTime = DEFAULT_TOBS;
//         arcObservationTime = DEFAULT_TOBS;
// End of commented by RDK
// Added by RDK
        flatCoadds = DEFAULT_COADDS;
        arcCoadds = DEFAULT_COADDS;
// End of added by RDK
        useDefaultAcquisition();
        setAcquisition();
    }

    /**
     * Get list of available cameras.
     */
    public String[]
    getCameraList()
    {
        if (isPolarimetry()) {
            return CAMERAS_POL;
	} else {
            return CAMERAS;
	}
    }

    /**
     * Get the camera.
     */
    public String
    getCamera()
    {
        String camera = _avTable.get(ATTR_CAMERA);
        if (camera == null) {
            camera = DEFAULT_CAMERA;
            setCamera(camera);
        }
        return camera;
    }

    /**
     * Use default imager.
     */
    public void
    useDefaultImager()
    {
        _avTable.rm(ATTR_IMAGER);
    }

    /**
     * Get the default imager.
     */
    public String
    getDefaultImager()
    {
        return DEFAULT_IMAGER;
    }

    /**
     * Set the imager name
     */
    public void
    setImager(String imager)
    {
        _avTable.set(ATTR_IMAGER, imager);

	// Added by RDK 19 Nov 2003: When we change plate scale, check the existing filter
	// to make sure it exists in the filter table for the new plate scale. If so, call
	// setFilter with the current value to re-set all filter-dependent values. If the 
	// current filter does not exist in the filter table for the new plate scale, 
	// use the default filter and filter category.
	
	String currentFilter = getFilter();
	LookUpTable filterLUT = getFilterLUT();
	try {
	    int fi = filterLUT.indexInColumn(currentFilter, 0);
	    setFilter(currentFilter);
	} catch (NoSuchElementException ex) {
	    useDefaultFilter();
	    useDefaultFilterCategory();
	}

         useDefaultMask();
//         useDefaultSourceMag();
    }

    /**
     * Get the imager name
     */
    public String
    getImager()
    {
        String imager = _avTable.get(ATTR_IMAGER);
        if (imager == null) {
            imager = getDefaultImager();
            setImager(imager);
        }
        return imager;
    }

    /**
     * Set the pixel scale
     */
    public void
    setPixelScale(double pixelScale)
    {
        _avTable.set(ATTR_PIXEL_SCALE, pixelScale);
    }

// Added by RDK
    /**
     * Set the pixel scale
     */
    public void
    setPixelScale(String pixelScale)
    {
        _avTable.set(ATTR_PIXEL_SCALE, pixelScale);
    }
// End of added by RDK

    /**
     * Get the pixel scale
     */
    public double
    getPixelScale()
    {
// Changed by RDK
        double ps;
        if (isPupilImaging()) {
            String psString = PUPIL[0];
            setPixelScale(psString);
            ps = 0.0;
        } else {
            int imindex = IMAGERS.indexInColumn(getImager(), 0);
            ps = Double.valueOf((String)IMAGERS.elementAt(imindex,0)).doubleValue();
            setPixelScale(ps);
        }
// End of changed by RDK
//Commented by RDK
//        setPixelScale(ps);
// End of commented by RDK
        return ps;
    }

    /**
     * Use default focus
     */
    public void
    useDefaultFocus()
    {
        _avTable.rm(ATTR_FOCUS);
    }

    /**
     * Get the default focus
     */
    public String
    getDefaultFocus()
    {
        String focus;
        if (isImaging()) {
            // For imaging, focus comes from FILTERS* lut
            // with choice depending on polarimetry
            LookUpTable filterLUT= getFilterLUT();
            String filter = getFilter();
            int fi = filterLUT.indexInColumn(filter, 0);
            if (isPolarimetry()) {
                focus = (String) filterLUT.elementAt(fi,2);
            } else {
                focus = (String) filterLUT.elementAt(fi,1);
	    }
	} else {
            int fi = getFilterIndex();
            focus = (String) SPECTFILTERS.elementAt(fi,5);;
	}
        return focus;
    }

    /**
     * Set the focus
     */
    public void
    setFocus(String focus)
    {
        _avTable.set(ATTR_FOCUS, focus);
    }

    /**
     * Get the focus
     */
    public String
    getFocus()
    {
        String focus = _avTable.get(ATTR_FOCUS);
        if (focus == null) {
            focus = getDefaultFocus();
            setFocus(focus);
        }
        return focus;
    }

    /**
     * Get the list of imagers
     */
    public String[]
    getImagerList()
    {
        if (isPupilImaging()) {
            String imagerList[] = new String[1];
            imagerList[0] = PUPIL[0];
            return imagerList;
        } else {
            String imagerList[] = new String[IMAGERS.getNumRows()];
            for (int i=0; i<IMAGERS.getNumRows(); i++) {
                imagerList[i] = (String)IMAGERS.elementAt(i,0);
            }
            return imagerList;
        }
    }

    /**
     * Get the imager number
     */
    public int
    getImagerIndex()
    {
        String imager = getImager();
	int imindex = 0;
	try {
	    imindex = IMAGERS.indexInColumn(imager, 0);
	}catch (ArrayIndexOutOfBoundsException ex) {
	    System.out.println ("Failed to find imager index!");
	}catch (NoSuchElementException ex) {
	    System.out.println ("Failed to find imager index!");
	}
	return imindex;
    }

    /**
     * Get the list of dispersers
     */
    public String[]
    getDisperserList()
    {
// Changed by RDK
        if (isPolarimetry()) {
	    return DISPERSER_CHOICES_POL;
        } else if (isIFU()) {
	    return DISPERSER_CHOICES_IFU;
        } else {
	    return DISPERSER_CHOICES;
        }
// End of changed by RDK
    }

    /**
     * Use default disperser.
     */
    public void
    useDefaultDisperser()
    {
        _avTable.rm(ATTR_DISPERSER);
    }

    /**
     * Get the default disperser
     */
    public String
    getDefaultDisperser()
    {
        if (isImaging()) {
            if (isPolarimetry()) {
                return POL_GRISM_IMAGING;
	    } else {
                return GRISM_IMAGING;
	    }
	} else {      
// Commented out by RDK
//             if (isPolarimetry()) {
//                 return DISPERSERCHOICESPOL[0];
//             } else if (isIFU()) {
//                 return DISPERSERCHOICESIFU[0];
//             } else {
//                 return DISPERSERCHOICES[0];
//             }
// End of commented out by RDK
// Added by RDK
            if (isPolarimetry()) {
		return DISPERSER_CHOICES_POL[0];
            } else if (isIFU()) {
		return DISPERSER_CHOICES_IFU[0];
            } else {
		return DISPERSER_CHOICES[0];
            }
// End of added by RDK
	}
    }

    /**
     * Set the disperser name
     */
    public void
    setDisperser(String disperser)
    {
	_avTable.set(ATTR_DISPERSER, disperser);
        confirmMask();
        useDefaultFilter();
        useDefaultFilterCategory();
        useDefaultResolution();
        //useDefaultSourceMag();  // Commented out by RDK
        updateDispersion();
    }

    /**
     * Get the disperser name
     */
    public String
    getDisperser()
    {
        String disperser = _avTable.get(ATTR_DISPERSER);
        if (disperser == null) {
            disperser = getDefaultDisperser();
            setDisperser(disperser);
        }
        return disperser;
    }

    /**
     * Get the disperser number
     */
    public int
    getDisperserIndex()
    {
        String disperser = getDisperser();
	int dispindex = 0;
	try {
	    dispindex = DISPERSERS.indexInColumn(disperser, 0);
	}catch (ArrayIndexOutOfBoundsException ex) {
	    System.out.println ("Failed to find disperser index!");
	}catch (NoSuchElementException ex) {
	    System.out.println ("Failed to find disperser index!");
	}
	return dispindex;
    }


    /**
     * Is IFU enabled
     */
    public boolean
    isIFU()
    {
        String camera = getCamera();
        return (camera.equalsIgnoreCase("ifu"));
    }

    /**
     * Use default Central Wavelength.
     */
    public void
    useDefaultCentralWavelength()
    {
        _avTable.rm(ATTR_CENTRAL_WAVELENGTH);
    }

    /**
     * Set the central wavelength.
     */
    public void
    setCentralWavelength(double cwl)
    {
        _avTable.set(ATTR_CENTRAL_WAVELENGTH, cwl);

    }

    /**
     * Get the default central wavelength.
     */
    public double
    getDefaultCentralWavelength()
    {
        int fi = getFilterIndex();
        if (isImaging()) {
            return new Double((String) FILTERS.elementAt(fi,1)).doubleValue();
	} else {
            return new Double((String) SPECTFILTERS.elementAt(fi,3)).doubleValue();
	}
    }

    /**
     * Get the central wavelength.
     */
    public double
    getCentralWavelength()
    {
        double cwl;
        String cwls = _avTable.get(ATTR_CENTRAL_WAVELENGTH);
        if (cwls == null) {
            cwl = getDefaultCentralWavelength();
            setCentralWavelength(cwl);
	} else {
            Double cwld = Double.valueOf(cwls);
            cwl = cwld.doubleValue();
        }
        return cwl;
    }

    /**
     * Get the central wavelength as a string
     */
    public String
    getCentralWavelengthString()
    {
        double cwl = MathUtil.round(getCentralWavelength(),3);
        String cwls = Double.toString(cwl);
        return cwls;
    }

    /**
     * Use default mask
     */
    public void
    useDefaultMask()
    {
        _avTable.rm(ATTR_MASK);
    }

    /**
     * Get the number of the current mask set.
     */
    public int
    getMaskSet()
    {
	int maskSet;
	try {
            maskSet = 
              Integer.valueOf((String)DISPERSERS.elementAt(getDisperserIndex(),4)).intValue();
	}catch (Exception ex) {
            System.out.println("getMaskSet> failed to get maskSet - defaults to 1");
	    maskSet = 1;
	}
	return maskSet;
    }

    /**
     * Get the list of masks
     */
    public String[]
    getMaskList()
    {
        if (isImaging()) {
            String maskList[] = new String[1];
            if (isPolarimetry()) {
                maskList[0] = POL_MASK_IMAGING;
	    } else {
                int i = getImagerIndex();
// Added by RDK
                maskList[0] = (String)IMAGERS.elementAt(i,3);
// End of added by RDK
// Commented out by RDK
//                maskList[0] = (String)IMAGERS.elementAt(i,4);
// End of commented out by RDK
	    }
            return maskList;
	} else {
            if (isPolarimetry()) {
                String maskList[] = new String[1];
                maskList[0] = POL_MASK_SPECTROSCOPY;
                return maskList;
            } else {
                // Either spectroscopy or ifu
                int maskSet = getMaskSet();
                if (maskSet == 1) {
                   return MASKS1;
		} else if (maskSet == 2) {
                   return MASKS2;
		} else if (maskSet == 3) {
                   return MASKS3;
		} else if (maskSet == 4) {
                   return MASKS4;
		} else if (maskSet == 5) {
                   return MASKS5;
		} else if (maskSet == 6) {
                   return MASKS6;
		} else if (maskSet == 7) {
                   return MASKS7;
		} else if (maskSet == 8) {
                   return MASKS8;
		} else if (maskSet == 9) {
                   return MASKS9;
		} else {
		   // Should not happen
                   return MASKS1;
		}
	    }
	}
    }

    /**
     * Confirm that the current mask is available in the current
     * mask set. Otherwise, set it to the default.
     */
    public void
    confirmMask()
    {
        String mask = getMask();
        int listLength = getMaskList().length;
        String maskList[] = new String[listLength];
        maskList = getMaskList();
        // Check for mask in maskList
        int inList = 0;
        for (int i=0; i<listLength; i++) {
            if (maskList[i].equalsIgnoreCase(mask)) {
                inList = 1;
	    }	}
        // If the mask was not found, get the default
        if (inList == 0) {
            useDefaultMask();
            mask = getMask();
	}
    }


    /**
     * Set the mask.
     */
    public void
    setMask(String mask)
    {
        _avTable.set(ATTR_MASK, mask);
        updateResolution();
        setInstAper();
    }

    /**
     * Get the mask.
     */
    public String
    getMask()
    {
        String mask = _avTable.get(ATTR_MASK);
        if (mask == null) {
            // Get the default mask
            if (isImaging()) {
                if (isPolarimetry()) {
                    mask = POL_MASK_IMAGING;
                } else {
// Added by RDK
                    if (isPupilImaging()) {
                        mask = PUPIL[3];
                    } else {
                        mask = (String)IMAGERS.elementAt(getImagerIndex(),3);
                    }
// End of added by RDK
// Commented out by RDK
//                    mask = (String)IMAGERS.elementAt(getImagerIndex(),4);
// End of commented out by RDK
                }
	    } else {
                if (isPolarimetry()) {
                    mask = POL_MASK_SPECTROSCOPY;
                } else {
                    int maskSet = getMaskSet();
                    if (maskSet==1) {
                        mask = DEFAULT_MASK1;
                    } else if (maskSet==2) {
                        mask = DEFAULT_MASK2;
                    } else if (maskSet==3) {
                        mask = DEFAULT_MASK3;
                    } else if (maskSet==4) {
                        mask = DEFAULT_MASK4;
                    } else if (maskSet==5) {
                        mask = DEFAULT_MASK5;
                    } else if (maskSet==6) {
                        mask = DEFAULT_MASK6;
                    } else if (maskSet==7) {
                        mask = DEFAULT_MASK7;
                    } else if (maskSet==8) {
                        mask = DEFAULT_MASK8;
                    } else if (maskSet==9) {
                        mask = DEFAULT_MASK9;
                    }
	        }
	    }
            setMask(mask);
        }
        return mask;
    }

    /**
     * Set the mask width in pixels
     */
    public void
    setMaskWidthPixels(double maskWidth)
    {
        _avTable.set(ATTR_MASK_WIDTH, maskWidth);
    }

    /**
     * Get the width (pixels) of the current mask.
     */
    public double
    getMaskWidthPixels()
    {
        double maskWidth = 0.0;
        try {
            int maskIndex = MASKS.indexInColumn (getMask(),0);
            maskWidth = new Double((String) MASKS.elementAt(maskIndex,1))
                .doubleValue();
	}catch (IndexOutOfBoundsException e) {
            // Leave at 0.0;
	}catch (NumberFormatException e) {
            // Leave at 0.0;
	}
        setMaskWidthPixels(maskWidth);
        return maskWidth;
    }

    /**
     * Set the mask height in arcseconds
     */
    public void
    setMaskHeightArcsec(double maskHeight)
    {
        _avTable.set(ATTR_MASK_HEIGHT, maskHeight);
    }

    /**
     * Get the height (arcsec) of the current mask.
     */
    public double
    getMaskHeightArcsec()
    {
        double maskHeight = 0.0;
        try {
            int maskIndex = MASKS.indexInColumn (getMask(),0);
            maskHeight = new Double((String) MASKS.elementAt(maskIndex,2))
                .doubleValue();
	}catch (IndexOutOfBoundsException e) {
            // Leave at 0.0;
	}catch (NumberFormatException e) {
            // Leave at 0.0;
	}
        setMaskHeightArcsec(maskHeight);
        return maskHeight;
    }

    /**
     * Get the height (pixels) of the current mask.
     */
    public double
    getMaskHeightPixels()
    {
        try {
            int maskIndex = MASKS.indexInColumn (getMask(),0);
            return new Double((String) MASKS.elementAt(maskIndex,2)).doubleValue();
	}catch (IndexOutOfBoundsException e) {
	    return 0.0;
	}catch (NumberFormatException e) {
	    return 0.0;
	}
    }

   /**
    * Set the default image rotator angle for the current camera
    */
    public void setDefaultPosAngle()
    {
        if (isImaging()) {
            setPosAngleDegrees(DEFAULT_IMAGING_POS_ANGLE);
        } else {
            setPosAngleDegrees(DEFAULT_SPECT_POS_ANGLE);
        }
    }

   /**
    * Set the position angle in radians from due north.
    */
    public void setPosAngleRadians( double posAngle ) {
        this.setPosAngleDegrees( Angle.radiansToDegrees( posAngle ) );
    }

   /**
    * Set the rotation of the science area as a string (representing degrees).
    */
    public void setPosAngleDegreesStr( String posAngleStr ) {
        double posAngle = 0.0;
        try {
            Double pa = Double.valueOf( posAngleStr );
            posAngle = pa.doubleValue();
        } catch ( NumberFormatException e ) {
            System.out.println( "Error converting string angle to double." );
        }
        this.setPosAngleDegrees( posAngle );
    }

    /**
     * Use default filter category
     */
    public void
    useDefaultFilterCategory()
    {
        _avTable.rm(ATTR_FILTER_CATEGORY);
    }

    /**
     * Get the filter category
     */
    public String
    getFilterCategory()
    {
        String filterCategory = null;
        filterCategory = _avTable.get(ATTR_FILTER_CATEGORY);
        if (filterCategory == null) {
            filterCategory = DEFAULT_FILTER_CATEGORY;
        }
        return filterCategory;
    }

    /**
     * Set the filter category
     */
    public void
    setFilterCategory(String filterCategory)
    {
        _avTable.set(ATTR_FILTER_CATEGORY, filterCategory);
        useDefaultFilter();
        useDefaultSourceMag();
    }

    /**
     * Get the broad band filterSet number
     */
    public int
    getBroadFilterSet()
    {
        int filterSet = 1;
// Added by RDK
        if (isPupilImaging()) {
            filterSet = Integer.valueOf(PUPIL[1]).intValue();
        } else {
            filterSet = 
                Integer.valueOf((String)IMAGERS.elementAt(getImagerIndex(),1)).intValue();
        }
// End of added by RDK
// Commented out by RDK
//         filterSet = 
//             Integer.valueOf((String)IMAGERS.elementAt(getImagerIndex(),2)).intValue();
// End of commented out by RDK
        return filterSet;
    }

    /**
     * Get the narrow band filterSet number
     */
    public int
    getNarrowFilterSet()
    {
        int filterSet = 1;
// Added by RDK
        if (isPupilImaging()) {
            filterSet = Integer.valueOf(PUPIL[2]).intValue();
        } else {
            filterSet = 
                Integer.valueOf((String)IMAGERS.elementAt(getImagerIndex(),2)).intValue();
        }
// End of added by RDK
// Commented out by RDK
//         filterSet = 
//             Integer.valueOf((String)IMAGERS.elementAt(getImagerIndex(),3)).intValue();
// End of commented out by RDK
        return filterSet;
    }

    /**
     * Get the filterSet number
     */
    public int
    getFilterSet()
    {
        int filterSet = 1;
        String filterCategory = getFilterCategory();
        if (filterCategory.equalsIgnoreCase("broad")) {
            filterSet = getBroadFilterSet();
        } else if (filterCategory.equalsIgnoreCase("narrow")) {
            filterSet = getNarrowFilterSet();
	} else {
            System.out.println("getFilterSet> Unrecognised filterCategory = " + 
                filterCategory);
            filterSet = 1;
	}
        return filterSet;
    }

    public String[]
    getFilterListFromLUT(LookUpTable filterLUT)
    {
        int nfilters = filterLUT.getNumRows() - 1;
        String filterList[] = new String[nfilters];
        for (int i=0; i<nfilters; i++) {
                filterList[i] = (String)filterLUT.elementAt(i+1,0);
	}
        return filterList;
    }

    public String[]
    getFilterMagsFromLUT(LookUpTable filterLUT)
    {
        int nmags = filterLUT.getNumColumns() - 4;
        String filterMags[] = new String[nmags];
        for (int i=0; i<nmags; i++) {
                filterMags[i] = (String)filterLUT.elementAt(0,i+4);
	}
        return filterMags;
    }

    /**
     * Get the the current filter LUT
     */
    public LookUpTable
    getFilterLUT()
    {
        int filterSet = getFilterSet();
        if (filterSet==1) {
            return FILTERS1;
        } else if (filterSet==2) {
            return FILTERS2;
        } else if (filterSet==3) {
            return FILTERS3;
        } else if (filterSet==4) {
            return FILTERS4;
        } else if (filterSet==5) {
            return FILTERS5;
        } else if (filterSet==6) {
            return FILTERS6;
        } else if (filterSet==7) {
            return FILTERS7;
        } else if (filterSet==8) {
            return FILTERS8;
        } else {
            System.out.println("GetFilterLUT> Unrecognised filterSet number " + 
                filterSet);
            return FILTERS1;
	}
    }
    /**
     * Get the the current filter LUT
     */
    public LookUpTable
    getFilterLUT(boolean broadband)
    {
        int filterSet = 0;
	if (broadband) {
	    filterSet = getBroadFilterSet();
	} else {
	    filterSet = getNarrowFilterSet();
	}
        if (filterSet==1) {
            return FILTERS1;
        } else if (filterSet==2) {
            return FILTERS2;
        } else if (filterSet==3) {
            return FILTERS3;
        } else if (filterSet==4) {
            return FILTERS4;
        } else if (filterSet==5) {
            return FILTERS5;
        } else if (filterSet==6) {
            return FILTERS6;
        } else if (filterSet==7) {
            return FILTERS7;
        } else if (filterSet==8) {
            return FILTERS8;
        } else {
            System.out.println("GetFilterLUT> Unrecognised filterSet number " + 
                filterSet);
            return FILTERS1;
	}
    }
    /**
     * Get the list of filters for the current imager
     */
    public String[]
    getFilterList()
    {
        LookUpTable filterLUT= getFilterLUT();
        return getFilterListFromLUT(filterLUT);
    }

    /**
     * Get the list of either broad or narrowband filters for the current imager
     */
    public String[]
    getFilterList(boolean broadband)
    {
        LookUpTable filterLUT= getFilterLUT(broadband);
        return getFilterListFromLUT(filterLUT);
    }

    /**
     * Get the list of available magnitudes for the current imager
     */
    public String[]
    getFilterMags()
    {
        LookUpTable filterLUT= getFilterLUT();
        return getFilterMagsFromLUT(filterLUT);
    }

    /**
     * Get the default filter
     */
    public String
    getDefaultFilter()
    {
        int filterSet = getFilterSet();
        if (filterSet==1) {
           return DEFAULT_FILTERS1;
        } else if (filterSet==2) {
           return DEFAULT_FILTERS2;
        } else if (filterSet==3) {
           return DEFAULT_FILTERS3;
        } else if (filterSet==4) {
           return DEFAULT_FILTERS4;
        } else if (filterSet==5) {
           return DEFAULT_FILTERS5;
        } else if (filterSet==6) {
           return DEFAULT_FILTERS6;
        } else if (filterSet==7) {
           return DEFAULT_FILTERS7;
        } else if (filterSet==8) {
           return DEFAULT_FILTERS8;
        } else {
            System.out.println("GetDefaultFilter> Unrecognised filterSet number " + 
                filterSet);
           return DEFAULT_FILTERS1;
	}
    }

    /**
     * Use default filter
     */
    public void
    useDefaultFilter()
    {
        _avTable.rm(ATTR_FILTER);
    }

    /**
     * Update filter
     */
    public void
    updateFilter()
    {
        useDefaultFilter();
        String filter = getFilter();
    }

    /**
     * Set the filter.
     */
    public void
    setFilter(String filter)
    {
        int findex;
        String spectralCoverage;
        double cwl;
        int order = 0;
	// Update dependent values
        try {
            _avTable.set(ATTR_FILTER, filter);
            if (isImaging()) {
                findex = FILTERS.indexInColumn(filter,0);
                cwl = new Double((String) FILTERS.elementAt(findex,1)).doubleValue();
                spectralCoverage = (String) FILTERS.elementAt(findex,2);
	    } else {
	        // filter is the CCS name of the filter
	        // Need to get the OTFilter name to lookup the focus, cwl and order
                int di = getDisperserIndex();
                String OTFilter = (String) DISPERSERS.elementAt(di,2);
                findex = SPECTFILTERS.indexInColumn(OTFilter, 0);
                cwl = new Double((String) SPECTFILTERS.elementAt(findex,3)).doubleValue();
                order = Integer.valueOf((String)SPECTFILTERS.elementAt(findex,2)).intValue();
                spectralCoverage = 
                    (String) SPECTFILTERS.elementAt(findex,4);
	    }
            setCentralWavelength(cwl);
            setOrder(order);
            setSpectralCoverage(spectralCoverage);
            updateResolution();
            String focus = getDefaultFocus();
            setFocus(focus);
	} catch  (ArrayIndexOutOfBoundsException ex) {
	    System.out.println ("setFilter> Filter not recognised: " + filter + 
                " in lut");
	} catch (NoSuchElementException ex) {
	    System.out.println ("setFilter> Filter not recognised: " + filter + 
                " in lut");
	}
    }

    /**
     * Get the filter number
     */
    public int
    getFilterIndex()
    {
        String filter = null;
	int findex = 0;
	try {
            if (isImaging()) {
                filter = getFilter();
                findex = FILTERS.indexInColumn(filter,0);
	    } else {
                int di = getDisperserIndex();
                filter = (String) DISPERSERS.elementAt(di,2);
                findex = SPECTFILTERS.indexInColumn(filter, 0);
	    }
	}catch (ArrayIndexOutOfBoundsException ex) {
	    System.out.println ("Failed to find filter " + filter + " in lut");
	}catch (NoSuchElementException ex) {
	    System.out.println ("Failed to find filter " + filter + " in lut");
	}
	return findex;
    }

    /**
     * Get the filter
     */
    public String
    getFilter()
    {
        int ci = 1;
        String filter = null;
        if (isImaging()) {
            filter = _avTable.get(ATTR_FILTER);
            if (filter == null) {
	       filter = getDefaultFilter();
               setFilter(filter);
	    }
        } else {
            int di = getDisperserIndex();
            String OTFilter = (String) DISPERSERS.elementAt(di,2);
            int findex = SPECTFILTERS.indexInColumn(OTFilter, 0);
            filter = (String) SPECTFILTERS.elementAt(findex,1);
            setFilter(filter);
	}
        return filter;
    }

    /**
     * Get the name of the filter to be used for darks
     */
// Commented out by RDK
//     public String
//     getDarkFilter()
//     {
//         return DARKFILTER;
//     }
// End of commented out by RDK

    /**
     * Get the arc blocking filter for the current spectroscopy configuration
     */
    public String
    getArcFilter()
    {
         String arcFilter = "undefined";
         if (!isImaging()) {
             int di = getDisperserIndex();
             String arcOTFilter = (String) DISPERSERS.elementAt(di,3);
             int findex = SPECTFILTERS.indexInColumn(arcOTFilter, 0);
             arcFilter = (String) SPECTFILTERS.elementAt(findex,1);
	 }
         return arcFilter;
    }

    /**
     * Get the arc central wavelength for the current spectroscopy configuration
     */
    public String
    getArcCentralWavelength()
    {
         String arcCwl = "0.0";
         if (!isImaging()) {
             int di = getDisperserIndex();
             String arcOTFilter = (String) DISPERSERS.elementAt(di,3);
             int findex = SPECTFILTERS.indexInColumn(arcOTFilter, 0);
             arcCwl = (String) SPECTFILTERS.elementAt(findex,3);
	 }
         return arcCwl;
    }

    /**
     * Get the arc focus for the current spectroscopy configuration
     */
    public String
    getArcFocus()
    {
         String arcFocus = "0.0";
         if (!isImaging()) {
             int di = getDisperserIndex();
             String arcOTFilter = (String) DISPERSERS.elementAt(di,3);
             int findex = SPECTFILTERS.indexInColumn(arcOTFilter, 0);
             arcFocus = (String) SPECTFILTERS.elementAt(findex,5);
	 }
         return arcFocus;
    }

    /**
     * Get the arc order for the current spectroscopy configuration
     */
    public String
    getArcOrder()
    {
         String arcOrder = "0";
         if (!isImaging()) {
             int di = getDisperserIndex();
             String arcOTFilter = (String) DISPERSERS.elementAt(di,3);
             int findex = SPECTFILTERS.indexInColumn(arcOTFilter, 0);
             arcOrder = (String) SPECTFILTERS.elementAt(findex,2);
	 }
         return arcOrder;
    }

    /**
     * Get the mode.
     */
    public String
    getMode()
    {
        String mode = _avTable.get(ATTR_MODE);
        if (mode == null) mode = DEFAULT_MODE; 
        return mode;
    }

    /**
     * Use default order
     */
    public void
    useDefaultOrder()
    {
        _avTable.rm(ATTR_ORDER);
    }

    /**
     * Set the order
     */
    public void
    setOrder(int order)
    {
        _avTable.set(ATTR_ORDER, order);
    }

    /**
     * Get the default order
     */
    public int
    getDefaultOrder()
    {
        int order = 0;
        if (isImaging()) return 0;
        int findex = getFilterIndex();
        order = Integer.valueOf((String)SPECTFILTERS.elementAt(findex,2)).intValue();
        return order;
    }

    /**
     * Get the order
     */
    public int
    getOrder()
    {
	int o = _avTable.getInt(ATTR_ORDER, 0);
	if (o == 0) {
	    o = getDefaultOrder();
	    setOrder(o);
	}
	return o;
    }

    /**
     * Get the order as a string
     */
    public String
    getOrderString()
    {
        return Integer.toString(getOrder());
    }

    /**
     * Set polarimetry to yes or no
     */
    public void
    setPolarimetry(String polarimetry)
    {
        _avTable.set(ATTR_POLARIMETRY, polarimetry);
    }

    /**
     * Get polarimetry as yes or no
     */
    public String
    getPolarimetry()
    {
        String polarimetry = _avTable.get(ATTR_POLARIMETRY);
        if (polarimetry == null) {
            polarimetry = DEFAULT_POLARIMETRY;
            setPolarimetry(polarimetry);
	}
        return polarimetry;
    }

    /**
     * Is polarimetry enabled
     */
    public boolean
    isPolarimetry()
    {
        return (getPolarimetry().equalsIgnoreCase("yes"));
    }


// Added by RDK

    /**
     * Set pupil_imaging to yes or no
     */
    public void
    setPupilImaging(String pupil_imaging)
    {
        _avTable.set(ATTR_PUPIL_IMAGING, pupil_imaging);
    }

    /**
     * Get pupil_imaging as yes or no
     */
    public String
    getPupilImaging()
    {
        String pupil_imaging = _avTable.get(ATTR_PUPIL_IMAGING);
        if (pupil_imaging == null) {
            pupil_imaging = DEFAULT_PUPIL_IMAGING;
            setPupilImaging(pupil_imaging);
	}
        return pupil_imaging;
    }

    /**
     * Is pupil_imaging enabled
     */
    public boolean
    isPupilImaging()
    {
        return (getPupilImaging().equalsIgnoreCase("yes"));
    }

// End of added by RDK


    /**
     * Set the spectral coverage
     */
    public void
    setSpectralCoverage(String spectralCoverage)
    {
        _avTable.set(ATTR_SPECTRAL_COVERAGE, spectralCoverage);
    }
  
    /**
     * Get the spectral coverage
     */
    public String
    getSpectralCoverage()
    {
        String spectralCoverage = null;
        if (isImaging()) {
            int fi = getFilterIndex();
            spectralCoverage = (String) FILTERS.elementAt(fi,2);
        } else {
            int di = getDisperserIndex();
            String OTFilter = (String) DISPERSERS.elementAt(di,2);
            int findex = SPECTFILTERS.indexInColumn(OTFilter, 0);
            spectralCoverage = 
                (String) SPECTFILTERS.elementAt(findex,4);
        }
        setSpectralCoverage(spectralCoverage);
        return spectralCoverage;
    }

    /**
     * Get the resolving power
     */
    public double
    getResolvingPower()
    {
        double rp;
        rp = 500.0;
        return rp;
    }

    /**
     * Get the resolving power as a string
     */
    public String
    getResolvingPowerString()
    {
        double rp = MathUtil.round(getResolvingPower(),1);
        String rps = Double.toString(rp);
        return rps;
    }

    /**
     * Use default flat source
     */
    public void
    useDefaultFlatSource()
    {
        flat_source = null;;
    }

    /**
     * Set the flat source
     */
    public void
    setFlatSource(String flatSource)
    {
        flat_source = flatSource;
    }

    /**
     * Get the default flat source
     */
    public String
    getDefaultFlatSource()
    {
        String defaultFlatSource;
	if (isImaging()) {
            //  get default flat from FILTERS lut
            int findex = getFilterIndex();
            defaultFlatSource = (String) FILTERS.elementAt(findex,3);
	} else {
            //  get default flat from DISPERSERS lut
            int dindex = getDisperserIndex();
            defaultFlatSource = (String) DISPERSERS.elementAt(dindex,5);
	}
        return defaultFlatSource;
    }

    /**
     * Get the flat source
     */
    public String
    getFlatSource()
    {
        if (flat_source == null) {
            flat_source = getDefaultFlatSource();
	}
        return flat_source;
    }

    /**
     * Get the list of available flat sources
     */
    public String[]
    getFlatList()
    {
        return FLAT_SOURCES;
    }

    /**
     * Use default arc source
     */
    public void
    useDefaultArcSource()
    {
        arc_source = null;;
    }

    /**
     * Set the arc source
     */
    public void
    setArcSource(String arcSource)
    {
        arc_source = arcSource;
    }

    /**
     * Get the arc source
     */
    public String
    getArcSource()
    {
        if (arc_source == null) {
            arc_source = DEFAULT_ARC_SOURCE;
	}
        return arc_source;
    }

    /**
     * Get the list of available arc sources
     */
    public String[]
    getArcList()
    {
        return ARC_SOURCES;
    }

    /**
     * Set the chop frequency
     */
    public void
    setChopFreq(String chopFreq)
    {
        _avTable.set(ATTR_CHOP_FREQUENCY, chopFreq);
    }

    /**
     * Update the instrument aperture
     */
    public void setInstAper()
    {
        int maskIndex = MASKS.indexInColumn (getMask(),0);
        setInstApX ((String) MASKS.elementAt(maskIndex,4));
        setInstApY ((String) MASKS.elementAt(maskIndex,5));
        setInstApZ ((String) MASKS.elementAt(maskIndex,6));
        setInstApL ((String) MASKS.elementAt(maskIndex,7));
    }

    /**
     * Use default dispersion
     */
    public void
    useDefaultDispersion()
    {
        _avTable.rm(ATTR_DISPERSION);
    }

    /**
     * Set the dispersion
     */
    public void setDispersion(double dispersion)
    {
        _avTable.set(ATTR_DISPERSION, dispersion);
    }

    /**
     * Get the default dispersion
     */
    public double getDefaultDispersion()
    {
        double dispersion;;
        if (isImaging()) {
            dispersion = 0.0;
	} else {
            int di = getDisperserIndex();
            dispersion = new Double((String) DISPERSERS.elementAt(di,1)).doubleValue();
	}
        return dispersion;
    }

    /**
     * Get the dispersion
     */
    public double getDispersion()
    {
        double dispersion;
        String ds = _avTable.get(ATTR_DISPERSION);
        if (ds == null) {
            dispersion = getDefaultDispersion();
            setDispersion(dispersion);
	} else {
            Double dsd = Double.valueOf(ds);
            dispersion = dsd.doubleValue();
        }
        return dispersion;
    }

    /**
     * Update dispersion
     */
    public void updateDispersion()
    {
        useDefaultDispersion();
        double dispersion = getDispersion();
    }

    /**
     * Use default resolution
     */
    public void
    useDefaultResolution()
    {
        _avTable.rm(ATTR_RESOLUTION);
    }

    /**
     * Set the resolution
     */
    public void setResolution(double resolution)
    {
        _avTable.set(ATTR_RESOLUTION, resolution);
    }

    /**
     * Get the resolution
     */
    public double getResolution()
    {
        double resolution;
        String rs = _avTable.get(ATTR_RESOLUTION);
        if (rs == null) {
            if (isImaging()) {
                resolution = 0.0;
	    } else {
                double dis = getDispersion();
                if (dis > 0.0000001) {
                   double mw;
                   if (isIFU()) {
                      // For the ifu, the appropriate mask width is 2.0 pixels
                      mw = 2.0;
		   } else {
                      mw = getMaskWidthPixels();
		   }
                   resolution = getCentralWavelength()/(dis *  mw);
		} else {
                   resolution = 0.0;
		}
	    }
            setResolution(resolution);
	} else {
            Double rsd = Double.valueOf(rs);
            resolution = rsd.doubleValue();
        }
        return resolution;
    }

    /**
     * Get the resolution as a string
     */
    public String
    getResolutionString()
    {
        int resolutioni = (int) (getResolution()+0.5);
        String resolutions = Integer.toString(resolutioni);
        return resolutions;
    }

    /**
     * Update resolution
     */
    public void updateResolution()
    {
        useDefaultResolution();
        double resolution = getResolution();
    }
// Added by RDK
    /**
     * Use default DAConf
     */
    public void
    useDefaultDAConf()
    {
        _avTable.rm(ATTR_DACONF);
    }

    /**
     * Set the DAConf
     */
    public void
    setDAConf(String DAConf)
    {
        _avTable.set(ATTR_DACONF, DAConf);
        int ri = MODES_OT.indexInColumn(DAConf,0);
        setReadMode((String) MODES_OT.elementAt(ri,1));
        setReadArea((String) MODES_OT.elementAt(ri,3), (String) MODES_OT.elementAt(ri,4));
    }


    /**
     * Set the minimum exposure time for the DAConf
     */
    public void
    setDAConfMinExpT(String daconf)
    {
        LookUpTable RBig;
        if (isImaging()) {
            RBig = READOUTS_IM;
	} else if (isIFU()) {
            RBig = READOUTS_IFU;
	} else {
            RBig = READOUTS_SPEC;
	}

        // Find corresponding row in MODES lut
        int ri = RBig.indexInColumn(daconf,2);

        double et = Double.valueOf((String)RBig.elementAt(ri,0)).doubleValue();

        _avTable.set(ATTR_DACONF_MINEXPT, et);
    }


    /**
     * Update the read mode
     */
    public void
    updateReadMode()
    {
        String readMode = getReadMode();
        double et = getExpTimeOT();
        LookUpTable R = getReadoutLUT(et);
        boolean currentModeOk = false;
        for (int i = 0; i < R.getNumRows(); i++) {
            String DAConf = (String)R.elementAt(i,2);
            int ri = MODES_OT.indexInColumn(DAConf,0);
            String mode = (String)MODES_OT.elementAt(ri,1);
            if (readMode.equalsIgnoreCase(mode)) {
                currentModeOk = true;
            }
        }
        if (!currentModeOk) {
            String DAConf = (String)R.elementAt(0,2);
            int ri = MODES_OT.indexInColumn(DAConf,0);
            readMode = (String)MODES_OT.elementAt(ri,1);
        }
        setReadMode(readMode);
    }

    /**
     * Set the read mode
     */
    public void
    setReadMode(String readMode)
    {
        _avTable.set(ATTR_READMODE, readMode);
    }

    /**
     * Get the read mode
     */
    public String
    getReadMode()
    {
        return _avTable.get(ATTR_READMODE);
    }

    /**
     * Use default readMode
     */
    public void
    useDefaultReadMode()
    {
        String readModeChoices[] = getReadModeChoices();
        String preferredReadMode = DEFAULT_MODE;
        for (int i = 0; i < readModeChoices.length; i++)
        {
            String mode = readModeChoices[i];
            if (mode.equalsIgnoreCase(preferredReadMode)) {
                setReadMode(mode);
                return;
            }
        }
        String mode = readModeChoices[0];
        setReadMode(mode);
    }


    /**
     * Use default readArea
     */
    public void
    useDefaultReadArea()
    {
        String readAreaChoices[] = getReadAreaChoices();
        String preferredReadArea = DEFAULT_AREA;
        for (int i = 0; i < readAreaChoices.length; i++)
        {
            String area = readAreaChoices[i];
            if (area.equalsIgnoreCase(preferredReadArea)) {
                StringTokenizer s = new StringTokenizer(area, "x");
                String rows = s.nextToken();
                String cols = s.nextToken();
                setReadArea(rows, cols);
                return;
            }
        }
        String area = readAreaChoices[0];
        StringTokenizer s = new StringTokenizer(area, "x");
        String rows = s.nextToken();
        String cols = s.nextToken();
        setReadArea(rows, cols);
    }


// End of added by RDK
// Commented by RDK
    /**
     * Use default readoutOT
     */
//     public void
//     useDefaultReadoutOT()
//     {
//         _avTable.rm(ATTR_READOUT_OT);
//         _avTable.rm(ATTR_READOUT);
//     }

    /**
     * Set the readout OT
     */
//     public void
//     setReadoutOT(String readoutOT)
//     {
//         _avTable.set(ATTR_READOUT_OT, readoutOT);
//         _avTable.rm(ATTR_READOUT);
//     }

    /**
     * Get the readout OT
     */
//     public String
//     getReadoutOT()
//     {
//         String readoutOT = _avTable.get(ATTR_READOUT_OT);
//         if (readoutOT == null) {
//             // Get the default readout for the current exposure time
//             double et = getExpTimeOT();
//             // Limit this to the allowed range
//             et = limitExpTimeOT(et);
//             LookUpTable R = getReadoutLUT(et);
//             String goodReadout = "NDSTARE";
//             if (et < EXPTIME_ND) {
//                goodReadout = "STARE";
//             }
//             int savedSize = 0;
//             for (int i=0; i<R.getNumRows();i++) {
//                 int ifUse = 0;
//                 int newSize = Integer.valueOf((String)R.elementAt(i,5)).intValue();
//                 String rd = (String) R.elementAt(i,4);
//                 if (savedSize > newSize) {
//                     //Smaller size - don't use
// 		} else if (savedSize < newSize) {
//                     // Larger size - use
//                     ifUse = 1;
// 		} else {
//                     // Prefer if the readout equals the goodReadout
//                     String readout = (String) R.elementAt(i,4);
//                     if (readout.equalsIgnoreCase(goodReadout)) {
//                         ifUse = 1;
// 		    } else {
//                     }
// 		}
//                 if (ifUse == 1) {
//                     readoutOT = (String) R.elementAt(i,3);
//                     savedSize = newSize;
// 		}
// 	    }
//             setReadoutOT(readoutOT);
// 	}
//         return readoutOT;
//     }
// End of commented by RDK


// Added by RDK
    /**
     * Get the readout OT
     */
    public String
    getReadoutOT()
    {
        String readoutOT = _avTable.get(ATTR_READOUT_OT);
        return readoutOT;
    }
// End of added by RDK

    /**
     * Use default readout
     */
    public void
    useDefaultReadout()
    {
        _avTable.rm(ATTR_READOUT);
    }

    /**
     * Set the readout
     */
    public void
    setReadout(String readout)
    {
        _avTable.set(ATTR_READOUT, readout);
    }

    /**
     * Get the readout
     */
    public String
    getReadout()
    {
        String readout = _avTable.get(ATTR_READOUT);
        if (readout == null) {
// Added by RDK
            double et = getExpTimeOT();
            // Limit this to the allowed range
            et = limitExpTimeOT(et);
            String DAConf = getDAConf(et);
            int ri = MODES_OT.indexInColumn(DAConf,0);
            readout = (String) MODES_OT.elementAt(ri,2);
// End of added by RDK
// Commented out by RDK
//             String readoutOT = getReadoutOT();
//             double et = getExpTimeOT();
//             // Limit this to the allowed range
//             et = limitExpTimeOT(et);
//             LookUpTable R = getReadoutLUT(et);
//             for (int i=0; i<R.getNumRows(); i++) {
//                String rdout = (String) R.elementAt(i,3);
//                if (rdout.equalsIgnoreCase(readoutOT)) {
//                    readout = (String) R.elementAt(i,4);
//                }
//	    }
// End of commented out by RDK
            setReadout(readout);
	}
        return readout;
    }

    /**
     * Get the readout lookup table for the current camera and exp time
     */
    public LookUpTable
    getReadoutLUT(double et)
    {
        LookUpTable RBig;
        if (isImaging()) {
            RBig = READOUTS_IM;
	} else if (isIFU()) {
            RBig = READOUTS_IFU;
	} else {
            RBig = READOUTS_SPEC;
	}
        // Construct a LUT that just contains rows suitable for the exposure time
        int RBRows = RBig.getNumRows();
        int RSize = 0;
        double minE;
        double maxE;
        // Count the number of rows in the new lookup table
        int rows = 0;
        for (int i=0; i<RBRows; i++) {
           minE = Double.valueOf((String)RBig.elementAt(i,0)).doubleValue();
           maxE = Double.valueOf((String)RBig.elementAt(i,1)).doubleValue();
	   if ((minE <= et) && (maxE > et)) {
              rows++;
	   }
	}
        if (rows<1) {
           System.out.println("getReadoutLUT> No readouts for exptime/camera");
           return null;
	} else {
           // Declare the new lookup table and construct it
           LookUpTable R = new LookUpTable();
           int nextRow = 0;
           for (int i=0; i<RBRows; i++) {
              minE = Double.valueOf((String)RBig.elementAt(i,0)).doubleValue();
              maxE = Double.valueOf((String)RBig.elementAt(i,1)).doubleValue();
              if ((minE <= et) && (maxE > et)) {
                 Vector row = RBig.getRow(i);
                 R.addRow(row);
                 nextRow++;
              }
	   }
           return R;
	}
    }

    /**
     * Get the readout choices
     */
    public String[]
    getReadoutChoices()
    {
        LookUpTable R;
        double et = getExpTimeOT();
        // Limit this to the allowed range
        et = limitExpTimeOT(et);
        R = getReadoutLUT(et);
        // Declare and fill the readoutChoices string
        int RRows = R.getNumRows();
        String readoutChoices[] = new String[RRows];
        for (int i=0; i<RRows; i++) {
            readoutChoices[i] = (String) R.elementAt(i,3);
	}
        return readoutChoices;
    }

// Added by RDK

    /**
     * Get the read mode choices
     */
    public String[]
    getReadModeChoices()
    {
        LookUpTable R;
        double et = getExpTimeOT();
        // Limit this to the allowed range
        et = limitExpTimeOT(et);
        R = getReadoutLUT(et);
        // Declare and fill the readModeChoices string
        int RRows = R.getNumRows();
        Vector v = new Vector();
        for (int i=0; i<RRows; i++) {
            String DAConf = (String) R.elementAt(i,2);
            int ri = MODES_OT.indexInColumn(DAConf,0);
            insertSorted(v, (String) MODES_OT.elementAt(ri,1));
	}
        String readModeChoices[] = new String[v.size()];
        VectorToStringArray(v, readModeChoices);
        return readModeChoices;
    }

    /**
     * Get the read area choices
     */
    public String[]
    getReadAreaChoices()
    {
        LookUpTable R;
        double et = getExpTimeOT();
        // Limit this to the allowed range
        et = limitExpTimeOT(et);
        R = getReadoutLUT(et);
        String readMode = getReadMode();
        int RRows = R.getNumRows();
        Vector v = new Vector();
        for (int i=0; i<RRows; i++) {
            String DAConf = (String) R.elementAt(i,2);
            int ri = MODES_OT.indexInColumn(DAConf,0);
            String mode = (String) MODES_OT.elementAt(ri,1);
            if (mode.equalsIgnoreCase(readMode)) {
                String readRows = (String) MODES_OT.elementAt(ri,3);
                String readCols = (String) MODES_OT.elementAt(ri,4);
                String readArea = readRows + "x" + readCols;
                if (!v.contains(readArea)) v.add(readArea);
            }
	}
        // Declare and fill the readAreaChoices string
        String readAreaChoices[] = new String[v.size()];
        VectorToStringArray(v, readAreaChoices);        
        return readAreaChoices;
    }

    public static void VectorToStringArray(Vector v, String s[]) {

        Enumeration e = v.elements();
        int i = 0;
        
        while(e.hasMoreElements()) {
            s[i] = (String)e.nextElement();
//            System.out.println(s[i]);
            i++;
        }
    }

    public static void insertSorted(Vector v, String s) {
        if (v.isEmpty()) {
            v.add(s);
        } else if (!v.contains(s)) {
            if (s.compareToIgnoreCase((String)v.firstElement()) < 0) {
                v.add(0, s);
            } else if (s.compareToIgnoreCase((String)v.lastElement()) > 0) {
                v.add(s);
            }
            else {
                for (int i = 0; i < v.size() - 1; i++)
                {
                    if (s.compareToIgnoreCase((String)v.elementAt(i)) > 0 &&
                        s.compareToIgnoreCase((String)v.elementAt(i+1)) < 0 ) {
                        v.add(i+1, s);
                    }
                }
            }            
        }
    }

// End of added by RDK


    /**
     * Get the chop frequency
     */
    public String
    getChopFreq()
    {
        String cfs = _avTable.get(ATTR_CHOP_FREQUENCY);
        if (cfs == null) {
            double cfd = 0.0;
            cfs = Double.toString(cfd);
            setChopFreq(cfs);
	}
        return cfs;
    }

    /**
     * Get the chop frequency
     */
    public String
    getChopFreqRound()
    {
        String cfs = getChopFreq();
        double cfd = Double.valueOf(cfs).doubleValue();
        double cfdr = MathUtil.round(cfd,3);
        String cfsr = Double.toString(cfdr);
        return cfsr;
    }

    /**
     * Set the flat exposure time
     */
    public void
    setFlatExpTime(double flatExpTime)
    {
        flatExposureTime = flatExpTime;
    }

    /**
     * Get the flat exposure time
     */
    public double
    getFlatExpTime()
    {
        return flatExposureTime;
    }

    /**
     * Set the arc exposure time
     */
    public void
    setArcExpTime(double arcExpTime)
    {
        arcExposureTime = arcExpTime;
    }

    /**
     * Get the arc exposure time
     */
    public double
    getArcExpTime()
    {
        return arcExposureTime;
    }

    /**
     * Set the chop delay in seconds
     */
    public void
    setChopDelay(double chopDelay)
    {
        _avTable.set(ATTR_CHOP_DELAY, chopDelay);
    }

    /**
     * Get the chop delay in seconds
     */
    public double
    getChopDelay()
    {
        int i;
        int iMax = CHOPS.getNumRows() - 1;
        double cMax = Double.valueOf((String)CHOPS.elementAt(iMax,0)).doubleValue();
        double cfd = Double.valueOf(getChopFreq()).doubleValue();
        if (cfd >= cMax) {
            i = iMax-1;
	} else {
            i = CHOPS.rangeInColumn(cfd,0);
	}
        double cd = Double.valueOf((String)CHOPS.elementAt(i,1)).doubleValue();
        return cd;
    }

    /**
     * Set the read interval in seconds
     */
    public void
    setReadInterval(double readInterval)
    {
        _avTable.set(ATTR_READ_INTERVAL, readInterval);
    }

// Commented out by RDK
    /**
     * Get the reset deal in seconds
     */
//     public double
//     getResetDelay()
//     {
//         double rd = _avTable.getDouble(ATTR_RESET_DELAY, 0.0);
//         return rd;
//     }
// End of commented out by RDK

    /**
     * Get the read interval in seconds
     */
    public double
    getReadInterval()
    {
        double ri = _avTable.getDouble(ATTR_READ_INTERVAL, 0.0);
        return ri;
    }

    /**
     * Set the number of reads in the exposure
     */
    public void
    setNreads(int nreads)
    {
        _avTable.set(ATTR_NREADS, nreads);
    }

// Commented out by RDK
    /**
     * Use default observation time OT
     */
//     public void
//     useDefaultObsTimeOT()
//     {
//         _avTable.rm(ATTR_OBSTIME_OT);
//     }
// End of commented out by RDK

    /**
     * Use default exposure time
     */
    public void
    useDefaultExpTimeOT()
    {
        _avTable.rm(ATTR_EXPTIME_OT);
    }

// Added by RDK
    /**
     * Use default number of coadds
     */
    public void
    useDefaultCoadds()
    {
        setCoadds( DEFAULT_COADDS );
    }
// End of added by RDK

// Commented by RDK
    /**
     * Set the observation time OT in seconds
     */
//     public void
//     setObsTimeOT(double obsTime)
//     {
//         _avTable.set(ATTR_OBSTIME_OT, obsTime);
//     }
// End of commented by RDK

    /**
     * Set the actual observation time in seconds
     */
    public void
    setObservationTime(double obsTime)
    {
        _avTable.set(ATTR_OBSERVATION_TIME, obsTime);
    }

// Added by RDK
    /**
     * Get the default flat number of coadds
     */
    public int
    getDefaultFlatCoadds()
    {
        int fc;
        if (isImaging()) {
            fc = FLAT_COADDS_IMAGING;
	} else {
            fc = FLAT_COADDS_SPECT;
	}
        return fc;
    }

    /**
     * Get the default arc  number of coadds
     */
    public int
    getDefaultArcCoadds()
    {
        int ac = ARC_COADDS;
        return ac;
    }
// End of added by RDK

    /**
     * Get the default observation time OT in seconds
     */
// Commented out by RDK
//     public double
//     getDefaultObsTimeOT()
//     {
//         /* Get current exposure time */
//         double et = getExpTimeOT();
//         /* Perform DACONFS lookup */
//         String daconf = getDAConf(et);
//         /* Find corresponding row in MODES lut */
//         int ri = MODES.indexInColumn(daconf,0);
//         /* Extract observation time from MODES lut */
//         double obsTime = 
//             Double.valueOf((String)MODES.elementAt(ri,2)).doubleValue();
//         return obsTime;
//     }

    /**
     * Get the default flat observation time OT in seconds
     */
//     public double
//     getDefaultFlatObsTimeOT()
//     {
//         double fot;
//         if (isImaging()) {
//             fot = FLAT_OBSTIME_IMAGING;
// 	} else {
//             fot = FLAT_OBSTIME_SPECT;
// 	}
//         return fot;
//     }

    /**
     * Get the default arc observation time OT
     */
//     public double
//     getDefaultArcObsTimeOT()
//     {
//         double aot = ARC_OBSTIME;
//         return aot;
//     }

    /**
     * Get the observation time in seconds
     */
//     public double
//     getObsTimeOT()
//     {
//         double obsTime;
//         String ots = _avTable.get(ATTR_OBSTIME_OT);
//         if (ots == null) {
//             obsTime = getDefaultObsTimeOT();
//             setObsTimeOT(obsTime);
// 	} else {
//             obsTime = _avTable.getDouble(ATTR_OBSTIME_OT, 0.0);
// 	}
//         return obsTime;
//     }
// End of commented out by RDK

    /**
     * Get the actual observation time in seconds
     */
    public double
    getObservationTime()
    {
        double obsTime;
        String ots = _avTable.get(ATTR_OBSERVATION_TIME);
        if (ots == null) {
            obsTime = 0.0;
            setObservationTime(obsTime);
	} else {
            obsTime = _avTable.getDouble(ATTR_OBSERVATION_TIME, 0.0);
	}
        return obsTime;
    }

// Added by RDK
    /**
     * Set the flat number of coadds
     */
    public void
    setFlatCoadds(int flatCoaddsInp)
    {
        flatCoadds = flatCoaddsInp;
    }

    /**
     * Get the flat number of coadds
     */
    public int
    getFlatCoadds()
    {
        return flatCoadds;
    }

    /**
     * Set the arc number of coadds
     */
    public void
    setArcCoadds(int arcCoaddsInp)
    {
        arcCoadds = arcCoaddsInp;
    }

    /**
     * Get the arc number of coadds
     */
    public int
    getArcCoadds()
    {
        return arcCoadds;
    }

// End of added by RDK

// Commented by RDK
    /**
     * Set the flat observation time OT in seconds
     */
//     public void
//     setFlatObsTimeOT(double flatObsTime)
//     {
//         flatObservationTime = flatObsTime;
//     }

    /**
     * Get the flat observation time OT in seconds
     */
//     public double
//     getFlatObsTimeOT()
//     {
//         return flatObservationTime;
//     }

    /**
     * Set the arc observation time OT in seconds
     */
//     public void
//     setArcObsTimeOT(double arcObsTime)
//     {
//         arcObservationTime = arcObsTime;
//     }

    /**
     * Get the arc observation time OT in seconds
     */
//     public double
//     getArcObsTimeOT()
//     {
//         return arcObservationTime;
//     }

    /**
     * Get the observation time OT in seconds as a String
     */
//     public String
//     getObsTimeOTString()
//     {
//         double obt = getObsTimeOT();
//         String obts = Double.toString(obt);
//         return obts;
//     }
// End of commented out by RDK

    /**
     * Get the exposure time in seconds as a String
     */
    public String
    getExposureTimeString()
    {
        String ets = Double.toString(MathUtil.round(getExposureTime(),3));
        return ets;
    }

    /**
     * Get the actual observation time in seconds as a String
     */
    public String
    getObservationTimeString()
    {
        String aobts = Double.toString(MathUtil.round(getObservationTime(),3));
        return aobts;
    }

    /**
     * Get the exposure time OT
     */
    public double
    getExpTimeOT()
    {	
        double expTime;
        String ets = _avTable.get(ATTR_EXPTIME_OT);
        if (ets == null) {
            expTime = getDefaultExpTimeOT();
            setExpTimeOT(expTime);
	} else {
            expTime = _avTable.getDouble(ATTR_EXPTIME_OT, 0.0);
	}
        return expTime;
    }

    /**
     * Get the exposure time OT as a string
     */
    public String
	getExpTimeOTString()
    {
        String timeAsString = Double.toString(MathUtil.round(getExpTimeOT(),4));
	return timeAsString;
    }

    /**
     * Set the exposure time OT
     */
    public void
    setExpTimeOT(double expTime)
    {
        _avTable.set(ATTR_EXPTIME_OT, expTime);
    }

    /**
     * Change the exposure time OT
     */
    public void
    changeExpTimeOT(double expTime)
    {
        double et = limitExpTimeOT(expTime);
        setExpTimeOT(et);
//        System.out.println("changeExpTimeOT called with et " + et);
        updateReadMode();
        updateReadArea();

        String obsType = "Object";
        updateDAConf(obsType);
    }

    

    /**
     * Get the default sky exposure time OT
     */
    public double
    getDefaultSkyExpTimeOT()
    {
        double set;
        String mag = getSourceMag();

        if (isImaging()) {
            // Exposure time comes from FILTERSx LUT using filter and magnitude
            String filter = getFilter();
            LookUpTable filterLUT = getFilterLUT();
	    int row = filterLUT.indexInColumn(filter,0);
	    int column = filterLUT.indexInRow(mag,0);
	    set = Double.valueOf((String)filterLUT.elementAt(row,column))
		.doubleValue();
	} else {
            // In spectroscopy, exposure time comes initially from SPECMAGS LUT
            // using disperser and magnitude
            String disperser = getDisperser();
            int row = SPECMAGS.indexInColumn(disperser,0);
            int column = SPECMAGS.indexInRow(mag,0);
            set = Double.valueOf((String)SPECMAGS.elementAt(row,column))
                .doubleValue();
	    // Note remove scaling of exposure time with slit width at request of CJD (by RDK 7 Apr 2003)
	    // Need to multiply this by factor associated with mask
	    //String mask = getMask();
            //int maskNo = MASKS.indexInColumn(mask,0);
            //double etm = Double.valueOf((String)MASKS.elementAt(maskNo,3))
            //    .doubleValue();
            //set = set * etm;
            // Ensure exposure time is within limits
            set = limitExpTimeOT(set);
	}
        return set;
    }

    /**
     * Adjust exposure time OT as required
     */
    public double
    limitExpTimeOT(double et)
    {
        double let = et;
        double minet;
        if (let > TEXPMAX) let = TEXPMAX;
        if (isImaging()) {
            minet = EXPTIME_MIN_IM;
	} else if (isIFU()) {
            minet = EXPTIME_MIN_IFU;
	} else {
            minet = EXPTIME_MIN_SPEC;
	}
        if (let < minet) let = minet;
        // Round to 8 figures
        let = MathUtil.round(let,8);
        return let;
    }

    /**
     * Get the default exposure time
     */
    public double
    getDefaultExpTimeOT()
    {
        double et = getDefaultSkyExpTimeOT();
        et = limitExpTimeOT(et);
        return et;
    }

    /**
     * Get the default flat exposure time
     */
    public double
    getDefaultFlatExpTime()
    {
        double fet;
        if (isImaging()) {
            // Get flat exposure time from FILTERS* LUT
            LookUpTable filterLUT= getFilterLUT();
            String filter = getFilter();
            int fi = filterLUT.indexInColumn(filter, 0);
            fet = Double.valueOf((String)filterLUT
                .elementAt(fi,3)).doubleValue();
	} else {
            // Get flat exposure time from DISPERSERS LUT
            fet = Double.valueOf((String)DISPERSERS
                .elementAt(getDisperserIndex(),6)).doubleValue();
	    // Need to multiply this by factor associated with mask
	    // Added at request of CJD by RDK 4 Apr 2003
            String mask = getMask();
            int maskNo = MASKS.indexInColumn(mask,0);
            double etm = Double.valueOf((String)MASKS.elementAt(maskNo,3))
                .doubleValue();
            fet = fet * etm;
	}
        fet = limitExpTimeOT(fet);
        return fet;
    }

    /**
     * Get the default arc exposure time
     */
    public double
    getDefaultArcExpTime()
    {
        double aet = ARC_EXPTIME;
	// Need to multiply this by factor associated with mask
	// Added at request of CJD by RDK 4 Apr 2003
	String mask = getMask();
	int maskNo = MASKS.indexInColumn(mask,0);
	double etm = Double.valueOf((String)MASKS.elementAt(maskNo,3))
	    .doubleValue();
	aet = aet * etm;
        return aet;
    }

// Commented out by RDK
    /**
     * Get the default bias exposure time
     */
//     public double getDefaultBiasExpTime() {
	
// 	if ( DEFBIASEXPTIME == 0.0 ) {
// 	    return super.getDefaultBiasExpTime();
// 	}
// 	return DEFBIASEXPTIME;
//     }
    /**
     * Get the default bias coadds
     */
//     public int getDefaultBiasCoadds() {
	
// 	if ( DEFBIASCOADDS == 0 ) {
// 	    return super.getDefaultBiasCoadds();
// 	}
// 	return DEFBIASCOADDS;
//     }
// End of commented out by RDK

    /**
     * Set the mode
     */
    public void
    setMode(String mode)
    {
        _avTable.set(ATTR_MODE, mode);
    }

    /**
     * Set the duty cycle
     */
    public void
    setDutyCycle(double dutyCycle)
    {
        _avTable.set(ATTR_DUTY_CYCLE, dutyCycle);
    }

    /**
     * Get the duty cycle
     */
    public String
    getDutyCycle()
    {
        String dc = _avTable.get(ATTR_DUTY_CYCLE);
        return dc;
    }

    /**
     * Get the duty cycle as a rounded percentage
     */
    public String
    getDutyCycleRound()
    {
        double dcd = Double.valueOf(getDutyCycle()).doubleValue() * 100.0;
        double dcdr = MathUtil.round(dcd,1);
        String dcsr = Double.toString(dcdr);
        return dcsr;
    }

    /**
     * Get the data acquisition configuration name
     */
// Commented out by RDK
//     public String
//     getDAConf(double et)
//     {
//         // Limit exposure time to the allowed range
//         et = limitExpTimeOT(et);
//         // Get the lookup table for the current exposure time
//         LookUpTable R = getReadoutLUT(et);
// 	// Get the readoutOT
//         String readoutOT = getReadoutOT();
// 	// Find the row for the readoutOT in the table
//         int row = R.indexInColumn(readoutOT,3);
// 	// Get the DAConf from that line in the table
//         String DAConf = (String)R.elementAt(row,2);
//         return DAConf;
//     }
// End of commented out by RDK

// Added by RDK
    public String
    getDAConf(double et)
    {
        String readMode = getReadMode();
        int ra[] = getReadArea();
//        System.out.println("getDAConf called et " + et + " readMode " + readMode + " rows " + ra[0] + " cols " + ra[1]);
        LookUpTable R = getReadoutLUT(et);
        String DAConf;
        for (int i = 0; i < R.getNumRows(); i++) {
            DAConf = (String)R.elementAt(i,2);
            int ri = MODES_OT.indexInColumn(DAConf,0);
            String mode = (String)MODES_OT.elementAt(ri,1);
            int rows = Integer.valueOf((String)MODES_OT.elementAt(ri,3)).intValue();
            int cols = Integer.valueOf((String)MODES_OT.elementAt(ri,4)).intValue();
//            System.out.println("i " + i + " DAConf " + DAConf + " mode " + mode + " rows " + rows + " cols " + cols);
            if (readMode.equalsIgnoreCase(mode) && ra[0] == rows && ra[1] == cols) {
//                System.out.println("DAConf return " + DAConf);
                return DAConf;
            }            
        }        
        return null;
    }
// End of added by RDK

    /**
     * Update the daconf for a dark observation
     */
    public void
    updateDADarkConf()
    {
        String obsType = "Dark";
        updateDAConf(obsType);
    }

    /**
     * Update the daconf for a flat observation
     */
    public void
    updateDAFlatConf()
    {
        String obsType = "Flat";
        updateDAConf(obsType);
    }

    /**
     * Update the daconf for an arc observation
     */
    public void
    updateDAArcConf()
    {
        String obsType = "Arc";
        updateDAConf(obsType);
    }

    /**
     * Update the daconf for an Object/Sky observatioon
     */
    public void
    updateDAObjConf()
    {
        String obsType = "Object";
        updateDAConf(obsType);
        _avTable.set(ATTR_MODE,W_mode);
// Commented out by RDK
//        _avTable.set(ATTR_WAVEFORM,W_waveform);
// End of commented out by RDK
        _avTable.set(ATTR_NREADS,W_nreads);
// Commented out by RDK
//         _avTable.set(ATTR_NRESETS,W_nresets);
//         _avTable.set(ATTR_RESET_DELAY,W_resetDelay);
// End of commented out by RDK
        _avTable.set(ATTR_READ_INTERVAL,W_readInterval);
// Commented out by RDK
//         _avTable.set(ATTR_IDLE_PERIOD,W_idlePeriod);
//         _avTable.set(ATTR_MUST_IDLES,W_mustIdles);
//         _avTable.set(ATTR_NULL_CYCLES,W_nullCycles);
//         _avTable.set(ATTR_NULL_EXPOSURES,W_nullExposures);
//         _avTable.set(ATTR_NULL_READS,W_nullReads);
// End of commented out by RDK
        setExposureTime(W_actExpTime);
        setObservationTime(W_obsTime);
        setChopFreq(W_chopFrequency);
        setChopDelay(W_chopDelay);
// Commented by RDK
//        setCoadds(W_coadds);
// End of commented by RDK
        setDutyCycle(W_dutyCycle);
    }

    /**
     * Update the daconf for the given obsType
     */

    // New version of updateDAConf added by RDK 
    public void
    updateDAConf(String obsType)
    {
        int ri; /* lookup row number */
        W_chopFrequency = "0.0";
        String daconf = null;
        double expTime = 0.0;

        // Get appropriate exposure and observation times for this obsType
        if (obsType.equalsIgnoreCase("OBJECT")) {
            expTime = getExpTimeOT();
            // Limit this to the allowed range
            expTime = limitExpTimeOT(expTime);
            //Get the number of coadds
            W_coadds = getStareCapability().getCoadds();
	} else if (obsType.equalsIgnoreCase("DARK")) {
            // Exposure time must be same as for object
            expTime = getExpTimeOT();
            // Limit this to the allowed range
            expTime = limitExpTimeOT(expTime);
	} else if (obsType.equalsIgnoreCase("FLAT")) {
            expTime = getFlatExpTime();
// Added by RDK
            //Get the number of coadds
            W_coadds = getFlatCoadds();
// End of added by RDK
	} else if (obsType.equalsIgnoreCase("ARC")) {
            expTime = getArcExpTime();
// Added by RDK
            //Get the number of coadds
            W_coadds = getArcCoadds();
// End of added by RDK
	}

        // Perform DACONFS lookup
        daconf = getDAConf(expTime);
        if (daconf == null) {
            System.out.println("Unexpected error: getDAConf returned null");
            return;
        }

        setDAConf(daconf);
        setDAConfMinExpT(daconf);

        // Find corresponding row in MODES lut
        ri = MODES_OT.indexInColumn(daconf,0);

        // Get the readout mode
        W_mode = getReadout();

        // Deterimine if the readout mode is an ND mode
        boolean ifND = W_mode.equalsIgnoreCase("NDSTARE")
            || W_mode.equalsIgnoreCase("NDCHOP");

        // Get the readout interval
        W_readInterval =  Double.valueOf((String)MODES_OT.elementAt(ri, 5)).doubleValue();

        // Get the observation overhead
        double obsOverhead =  Double.valueOf((String)MODES_OT.elementAt(ri, 6)).doubleValue();

        // Get the readout overhead
        double readoutOverhead =  Double.valueOf((String)MODES_OT.elementAt(ri, 7)).doubleValue();

        // Compute actual exposure time
        if (ifND) {
            // Compute number of reads in the exposure time (Minimum of 2)
            W_nreads = (int) Math.round(expTime/W_readInterval) + 1;
            if (W_nreads < 2) W_nreads = 2;
            W_actExpTime = (W_nreads - 1) * W_readInterval;
        } else {
            W_actExpTime = expTime;
        }

        // Compute the observation time
        W_obsTime = obsOverhead + (readoutOverhead + W_actExpTime) * W_coadds;

        // Compute duty cycle
        double totalExposure = W_actExpTime * W_coadds;
        W_dutyCycle = totalExposure/W_obsTime;
}

    public double getExposureOverhead() {
	double overhead = 0.0;
	double dc = (Double.valueOf(getDutyCycle())).doubleValue();
	double expTime = getExposureTime();
	overhead = expTime/dc - expTime;
	return overhead;
    }


    /**
     * Update the daconf for the given obsType
     */

    // Name changed to updateDAConfOld by RDK 
//     public void
//     updateDAConfOld(String obsType)
//     {
//         int ci; /* lookup column number */
//         int ri; /* loopup row number */
//         String daconf = null;
//         double expTime = 0.0;
//         double obsTime = 0.0;
//         double TDelay = 0.0;
//         double totalExposure = 0.0;
//         double actChopDelay = 0.0;
//         double dutyCycle = 0.0;
//         double dwellTime = 0.0;
//         int numCycles = 0;
//         // Get appropriate exposure and observation times for this obsType
//         if (obsType.equalsIgnoreCase("OBJECT")) {
//             expTime = getExpTimeOT();
//             // Limit this to the allowed range
//             expTime = limitExpTimeOT(expTime);
//             obsTime = getObsTimeOT();
// 	} else if (obsType.equalsIgnoreCase("DARK")) {
//             // Exposure time must be same as for object
//             expTime = getExpTimeOT();
//             // Limit this to the allowed range
//             expTime = limitExpTimeOT(expTime);
//             obsTime = getObsTimeOT();
// 	} else if (obsType.equalsIgnoreCase("FLAT")) {
//             expTime = getFlatExpTime();
//             obsTime = getFlatObsTimeOT();
// 	} else if (obsType.equalsIgnoreCase("ARC")) {
//             expTime = getArcExpTime();
//             obsTime = getArcObsTimeOT();
// 	}
//         // Perform DACONFS lookup
//         daconf = getDAConf(expTime);
//         // Find corresponding row in MODES lut
//         ri = MODES.indexInColumn(daconf,0);
//         // Get the chop frequency
//         double cfd = Double.valueOf((String)MODES.elementAt(ri,3)).doubleValue();
//         if (cfd > 0.0) {
//             dwellTime = 0.5/cfd;
// 	}
//         // Lookup the W_* values
//         W_mode = getReadout();
//         W_waveform = (String)MODES.elementAt(ri,1);
//         W_nresets = Integer.parseInt((String)MODES.elementAt(ri, 4));
//         W_resetDelay = 
//             Double.valueOf((String)MODES.elementAt(ri, 5)).doubleValue();
//         W_readInterval =
//             Double.valueOf((String)MODES.elementAt(ri, 6)).doubleValue();
//         W_idlePeriod =
//             Double.valueOf((String)MODES.elementAt(ri, 7)).doubleValue();
//         W_mustIdles = Integer.parseInt((String)MODES.elementAt(ri, 8));
//         W_nullCycles = Integer.parseInt((String)MODES.elementAt(ri, 9));
//         W_nullExposures = Integer.parseInt((String)MODES.elementAt(ri, 10));
//         W_nullReads = Integer.parseInt((String)MODES.elementAt(ri, 11));

//         // Set ifChop and ifND flags
//         boolean ifChop = W_mode.equalsIgnoreCase("CHOP")
//             || W_mode.equalsIgnoreCase("NDCHOP");
//         boolean ifND = W_mode.equalsIgnoreCase("NDSTARE")
//             || W_mode.equalsIgnoreCase("NDCHOP");

//         // Perform WAVEFORMS lookup
//         ri = WAVEFORMS.indexInColumn(W_waveform,0);
//         // Lookup waveform clock period
//         double clkPeriod =
//            Double.valueOf((String)WAVEFORMS.elementAt(ri, 7)).doubleValue()
//            * 1.0e-9;
//         // Loopup "expWhileRead" flag
//         int expWR = Integer.parseInt((String)WAVEFORMS.elementAt(ri,8));
//         // Lookup idle waveform duration
//         double idleT = clkPeriod *
//            Double.valueOf((String)WAVEFORMS.elementAt(ri, 9)).doubleValue();
//         // Lookup NDidle waveform duration
//         double NDidleT = clkPeriod *
//            Double.valueOf((String)WAVEFORMS.elementAt(ri,10)).doubleValue();
//         // Lookup NDread waveform duration
//         double NDreadT = clkPeriod *
//            Double.valueOf((String)WAVEFORMS.elementAt(ri,12)).doubleValue();
//         // Lookup readReset waveform duration
//         double readResetT = clkPeriod *
//            Double.valueOf((String)WAVEFORMS.elementAt(ri,14)).doubleValue();
//         // Lookup appropriate reset waveform duration
//         double TEnd = 0.0;
//         double resetT = 0.0;
//         if (ifND) {
//             // Use NDreset waveform
//             resetT = clkPeriod *
//                 Double.valueOf((String)WAVEFORMS.elementAt(ri,11)).doubleValue();
//             TEnd = NDreadT;
// 	} else {
//             // Use reset waveform
//             resetT = clkPeriod *
//                 Double.valueOf((String)WAVEFORMS.elementAt(ri,13)).doubleValue();
//             TEnd = resetT;
// 	}

//         // Build script section
//         double TGone = 0.0;  /* Time spent so far */

//         // Insert mustIdles
//         if (W_idlePeriod > 0.00001 && W_mustIdles > 0) {
//             if (idleT > W_idlePeriod)
//                 TGone = idleT * W_mustIdles;
//             else
//                 TGone = W_idlePeriod * W_mustIdles;
// 	}

//         // Handle initial chop delay
//         if (ifChop) {
//             W_chopDelay = getChopDelay();
//             // Delay required excludes time already gone and time to do resets
//             TDelay = W_chopDelay - (W_nresets*resetT) - TGone;
//             actChopDelay = 0.0;
//             if (TDelay > 0.0) {
//                 // 1st option is to generate NDIdles
//                 if (NDidleT > 0.00001) {
//                     actChopDelay = Math.ceil(TDelay/NDidleT) * NDidleT;
// 		} else if (W_idlePeriod > 0.00001) {
//                     actChopDelay = Math.ceil(TDelay/W_idlePeriod) * W_idlePeriod;
// 		} else
//                     actChopDelay = TDelay;
//                 TGone = TGone + actChopDelay;
// 	    }
// 	}

//         // Set up exposures in this beam
//         boolean ifExposing = true;
//         // nexp = exposures in chop phase including null exposures
//         int nexp = 0;
//         // Real (non-null) exposures in whole observation
//         int numExpBeam = 0; 
//         int numExp = 0; 
//         double TStart = 0.0;
//         double readT = 0.0;
//         W_nreads = 0;
//         while (ifExposing) {
//             nexp++;
//             TStart = TGone;
//             if (nexp == 1) {
//                 // First exposure starts with reset
//                 TGone = TGone + resetT;
// 	    } else if (!ifND) {
//                 // Destructive reading modes use readReset
//                 TGone = TGone + readResetT;
// 	    } else {
//                 // ND does read followed by reset
//                 TGone = TGone + NDreadT + resetT;
// 	    }

//             // Insert any additional resets
//             if (W_nresets > 1) TGone = TGone + ((W_nresets-1)*resetT);

//             // Insert ND exposure
//             if (ifND) {
//                 // Insert resetDelay
//                 if (W_resetDelay > 0.0) {
//                     // Execute NDidles if defined
//                     if (NDidleT > 0.00001) {
//                         TGone = TGone + Math.ceil(W_resetDelay/NDidleT) * NDidleT;
//                     } else TGone = TGone + W_resetDelay;
// 		}
//                 // Insert NDreads, including nullReads
//                 // How long does each read take? This differs from the
//                 // readInterval if NDidling is enabled
//                 if (NDidleT > 0.00001) {
//                     readT = Math.round(W_readInterval/NDidleT) * NDidleT;
// 		} else {
//                     readT = W_readInterval;
// 		}
//                 // Compute number of reads in the exposure time (Minimum of 2)
//                 W_nreads = (int) Math.round(expTime/readT) + 1;
//                 if (W_nreads < 2) W_nreads = 2;
//                 // Increase TGone by time to take these, plus any nullReads 
//                 // minus the last read (which is appended below)
//                 TGone = TGone + ((W_nreads+W_nullReads-1) * readT);
//                 W_actExpTime = (W_nreads - 1) * readT;
// 	    } else {
//                 // Non-ND. Wait for the exposure time to expire
//                 if (expWR == 0) {
//                     TDelay = expTime;
// 		} else {
//                     TDelay = expTime - readResetT;
// 		}
//                 if (NDidleT > 0.00001) {
//                     // Spend TDelay generating NDidles
//                     TGone = TGone + Math.round(TDelay/NDidleT) * NDidleT;
// 		} else {
//                     TGone = TGone + TDelay;
// 		}
//                 W_actExpTime = expTime;
// 	    }

//             // Check for further exposures
//             if (ifChop) {
//                 if ((TGone - TStart + TGone + TEnd) < dwellTime) { 
//                     // Loop back to insert another exposure
// 		} else if (nexp <= W_nullExposures) {
//                     // No real exposures yet, stay in beam and extend dwell time
// 		} else {
//                     // No more exposures required - set ifExposing to false
//                     ifExposing = false;
// 		    numExpBeam = nexp - W_nullExposures;
// 		}
// 	    } else {
//                 // Not chopping
//                 if ((TGone - TStart + TGone + TEnd) < obsTime) { 
//                     // Yes - loop back to insert another exposure
//                     // Loop back to insert another exposure
// 		} else if (nexp <= W_nullExposures) {
//                     // No real exposures yet, stay in beam and extend dwell time
// 		} else {
//                     // No - set ifExposing to false
//                     ifExposing = false;
// 		    numExp = nexp - W_nullExposures;
// 		}
// 	    }
//             // End of while loop
// 	}

//         // Insert final read (ND) or readReset (non-ND)
//         if (ifND) {
//             TGone = TGone + NDreadT;
// 	} else {
//             TGone = TGone + readResetT;
// 	}

//         // If chopping, take TGone as new dwell time and compute new 
//         // chop frequency
//         if (ifChop) {
//             dwellTime = TGone;
//             cfd = 0.5/dwellTime;
//             String cfs = Double.toString(cfd);
//             W_chopFrequency = cfs;
//             numCycles = (int) Math.round(obsTime*cfd*2.0);
//             if (numCycles < 1) numCycles = 1;
//             numExp = numExpBeam * numCycles;
// 	} else {
//             W_chopFrequency = "0.0";
// 	}

//         // Report actual observation time
//         W_obsTime = 0.0;
//         if (ifChop) {
//             W_obsTime = dwellTime * 2.0 * (numCycles+W_nullCycles);
// 	} else {
//             W_obsTime = TGone;
// 	}

//         // Update the number of required coadds
//         W_coadds = numExp;

//         // Compute duty cycle
//         if (ifChop) {
//             totalExposure = numExp * 2.0 * W_actExpTime;
//             double totalChopDelay = 2.0 * (numCycles+W_nullCycles) * actChopDelay;
//             W_dutyCycle = totalExposure/(W_obsTime - totalChopDelay);
// 	} else {
//             totalExposure = W_actExpTime * numExp;
//             W_dutyCycle = totalExposure/W_obsTime;
// 	}

//     }

    /**
     * Get the coadds.
     */
    public String getCoaddsString() {
        int coadds = getStareCapability().getCoadds();
        return Integer.toString(coadds);
    }

    /**
     * Set coadds
     */
    public void setCoadds( int coadds ) {
        getStareCapability().setCoadds( coadds );
    }

    /**
     * Set coadds as a string
     */
    public void setCoadds( String coadds ) {
        int c = 0;
        try {
            Integer tmp = Integer.valueOf( coadds );
            c = tmp.intValue();
        } catch ( Exception ex ) {}

        setCoadds( c );
    }


    /**
     * Get the readout area (cols,rows) for the current waveform
     */
    public int[]
    getReadArea()
    {
// Commented out by RDK
        // Get the exposure time
//        double et = getExpTimeOT();
        // Get the DAConf
//        String daconf = getDAConf(et);
        // Find corresponding row in MODES lut
//        int i = MODES_OT.indexInColumn(daconf,0);
//         // Get the waveform
//         String wfm = (String)MODES.elementAt(i,1);
//         // Get the index to the waveform in the WAVEFORMS table
//         i = WAVEFORMS.indexInColumn(wfm,0);
// End of commented out by RDK
        // Extract and return the readout area
        int ra[] = new int[2];
// Commented out by RDK
//         ra[0] = Integer.parseInt((String)WAVEFORMS.elementAt(i,1));
//         ra[1] = Integer.parseInt((String)WAVEFORMS.elementAt(i,2));
//        ra[0] = Integer.parseInt((String)MODES_OT.elementAt(i,3));
//        ra[1] = Integer.parseInt((String)MODES_OT.elementAt(i,4));
//        String ras = ra[0] + "x" + ra[1];
//        setReadAreaString(ras);
// 	// Also set the reference pixel coordinates
//         double refX =
//            Double.valueOf((String)WAVEFORMS.elementAt(i, 3)).doubleValue();
//         _avTable.set(ATTR_REF_PIXEL_X, refX);
//         double refY =
//            Double.valueOf((String)WAVEFORMS.elementAt(i, 4)).doubleValue();
//         _avTable.set(ATTR_REF_PIXEL_Y, refY);
//         double refL =
//            Double.valueOf((String)WAVEFORMS.elementAt(i, 5)).doubleValue();
//         _avTable.set(ATTR_REF_PIXEL_L, refL);
//         double refS =
//            Double.valueOf((String)WAVEFORMS.elementAt(i, 6)).doubleValue();
//         _avTable.set(ATTR_REF_PIXEL_S, refS);
// End of commented out by RDK
// Added by RDK
        StringTokenizer s = new StringTokenizer(getReadAreaString(), "x");
        ra[0] = Integer.parseInt(s.nextToken());
        ra[1] = Integer.parseInt(s.nextToken());
// End of Added by RDK
        return ra;
    }

//Added by RDK
    /**
     * Update the read area
     */
    public void
    updateReadArea()
    {
        String readMode = getReadMode();
        int ra[] = getReadArea();
        int readArea = ra[0] * ra[1];
        double et = getExpTimeOT();
//        System.out.println("Called updateReadArea with et " + et + " readMode " + readMode);
        LookUpTable R = getReadoutLUT(et);
        int minAreaDiff = 10000000;
        int raSave[] = new int[2];
        for (int i = 0; i < R.getNumRows(); i++) {
            String DAConf = (String)R.elementAt(i,2);
            int ri = MODES_OT.indexInColumn(DAConf,0);
            String mode = (String)MODES_OT.elementAt(ri,1);
            ra[0] = Integer.valueOf((String)MODES_OT.elementAt(ri,3)).intValue();
            ra[1] = Integer.valueOf((String)MODES_OT.elementAt(ri,4)).intValue();
            int areaDiff = Math.abs(ra[0] * ra[1] - readArea);
//            System.out.println("i " + " mode " + mode + i + " rows " + ra[0] + " cols " + ra[1] + " areaDiff " + areaDiff);
            if (readMode.equalsIgnoreCase(mode) && areaDiff < minAreaDiff) {
                minAreaDiff = areaDiff;
                raSave[0] = ra[0];
                raSave[1] = ra[1];
            }
        }
//        System.out.println("Setting read area to " + " rows " + raSave[0] + " cols " + raSave[1] + " minAreaDiff " + minAreaDiff);
        setReadArea(Integer.toString(raSave[0]), Integer.toString(raSave[1]));
    }

    /**
     * Set the readout area
     */
    public void
    setReadArea(String rows, String cols)
    {
        _avTable.set(ATTR_READAREA, rows + "x" + cols);
    }
// End of Added by RDK

    /**
     * Set the readout area string
     */
    public void
    setReadAreaString(String readArea)
    {
        _avTable.set(ATTR_READAREA, readArea);
    }

    /**
     * Get the readout area as a string
     */
    public String
    getReadAreaString()
    {
        return _avTable.get(ATTR_READAREA);
    }

    /**
     * Use default acquisition
     */
    public void
    useDefaultAcquisition()
    {
// Commented by RDK
//        useDefaultReadoutOT();
// End of commented by RDK
        useDefaultExpTimeOT();
// Commented by RDK
//        useDefaultObsTimeOT();
// End of commented by RDK
// Added by RDK
        useDefaultCoadds();
        double et = getExpTimeOT();
        changeExpTimeOT(et);
// End of added by RDK
    }

    /**
     * Set the acquisition
     */
    public void
    setAcquisition()
    {
        // Setup for normal exposures
        double et = getExpTimeOT();
        String filter = getFilter();
        updateDAObjConf();
    }

// Added by RDK
    // Update the attribute-value table. Intended to provide backwards
    // compatibility to make possible to translate science programs created 
    // with the previous version of the OT
    public void
    avTableUpdate()
    {
        if (_avTable.get(ATTR_VERSION).equals("1")) {
                        
            // Update imager value for current OT
            String imager = getImager();
                        
            // Set the pupil_imaging attribute if imager value is pupil
            if (imager.equalsIgnoreCase("pupil")) {
                setPupilImaging("yes");
                setPixelScale(PUPIL[0]);
                _avTable.set(ATTR_IMAGER, DEFAULT_IMAGER);
            } else {
                // Old OT had imager names like 0.12, 0.06+IJM, etc. We want to strip
                // off the +IJM and just leave the plate scale.
                StringTokenizer st = new StringTokenizer(imager, "+");
                _avTable.set(ATTR_IMAGER, st.nextToken());
            }

	    /* For spectrosocpy, check disperser and if it is one of the target
	     * acquisition dispersers, set target acquisition mode */
// 	    if (!isImaging()) {
// 		String disperser = getDisperser();
// 		String disperserChoicesAcq[] = null;
// 		if (isIFU()) {
// 		    disperserChoicesAcq = DISPERSER_CHOICES_IFU_ACQ;
// 		} else if (isPolarimetry()) {
// 		    disperserChoicesAcq = DISPERSER_CHOICES_POL_ACQ;
// 		} else {
// 		    disperserChoicesAcq = DISPERSER_CHOICES_ACQ;
// 		}

// 		for (int i = 0; i < disperserChoicesAcq.length; i++) {
// 		    if (disperser.equalsIgnoreCase(disperserChoicesAcq[i])) {
// 			_avTable.set(ATTR_TARGET_ACQ, "yes");
// 		    }
// 		}
// 	    }
            
            // Convert readoutOT value to DAConf value by looking for a match in the MODES_OT table
            String readoutOT = getReadoutOT();
            StringTokenizer st1 = new StringTokenizer(readoutOT, " ");
            String oldMode = new String();
            if (st1.countTokens() == 2) {
                oldMode = st1.nextToken();
            } else if (st1.countTokens() == 3) {
                oldMode = st1.nextToken() + " " + st1.nextToken();
            } else {
                System.out.println("Unexpected value for readoutOT " + readoutOT);
                setDAConf("ERROR");
            }
            String remainder = st1.nextToken();
            StringTokenizer st2 = new StringTokenizer(remainder, "x");
            String oldRows = st2.nextToken();
            String oldCols = st2.nextToken();
            for (int i = 0; i < MODES_OT.getNumRows(); i++)
            {
                String mode = (String)MODES_OT.elementAt(i,1);
                String rows = (String)MODES_OT.elementAt(i,3);
                String cols = (String)MODES_OT.elementAt(i,4);
                if (oldMode.equalsIgnoreCase(mode) &&
                    oldRows.equalsIgnoreCase(rows) &&
                    oldCols.equalsIgnoreCase(cols)) {
                        String DAConf = (String)MODES_OT.elementAt(i,0);
                        setDAConf(DAConf);
                        setDAConfMinExpT(DAConf);
                }
            }
            _avTable.set(ATTR_VERSION, "2");
        } else if (_avTable.get(ATTR_VERSION).equals("3")) {
	    // This was added to compensate for a bug in convertUISTSp.pl wherein the
	    // minumum exposure time attribute was incorrectly specified as DAConfMinExpTime. (RDK)
	    String DAConf = _avTable.get(ATTR_DACONF);
	    setDAConfMinExpT(DAConf);
	}
    }
// End of added by RDK
}
