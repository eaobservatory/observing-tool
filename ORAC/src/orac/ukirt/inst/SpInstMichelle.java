/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2000                   */
/*                                                              */
/*==============================================================*/

// Development version started 2000 Oct 26 based on Gemini version
// and Alan Briger's work on the CGS4 instrument

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
 * The Michelle instrument.
 +
 + @author Alan Pickup
 */

public final class SpInstMichelle extends SpUKIRTInstObsComp
{
    // Attributes presented to user
    public static String ATTR_CONFIG_TYPE        = "configType";
    public static String ATTR_CAMERA             = "camera";
    public static String ATTR_POLARIMETRY        = "polarimetry";
    public static String ATTR_MASK               = "mask";
    public static String ATTR_MASK_ANGLE         = "maskAngle";
    public static String ATTR_DISPERSER          = "disperser";
    public static String ATTR_ORDER              = "order";
    public static String ATTR_SAMPLING           = "sampling";
    public static String ATTR_CENTRAL_WAVELENGTH = "centralWavelength";
    public static String ATTR_FILTER             = "filter";
    public static String ATTR_SCIENCE_AREA       = "scienceArea";
    public static String ATTR_SPECTRAL_COVERAGE  = "spectralCoverage";
    public static String ATTR_DARKFILTER         = "darkFilter";
    public static String ATTR_WAVEPLATE          = "waveplate";
    public static String ATTR_FLAT_SAMPLING      = "flatSampling";
    public static String ATTR_MODE               = "mode";
    public static String ATTR_ACQMODE            = "acqMode";
    public static String ATTR_FLAT_EXPOSURE_TIME = "flatExptime";
    public static String ATTR_CHOP_FREQUENCY     = "chopFrequency";
    public static String ATTR_CHOP_DELAY         = "chopDelay";
    public static String ATTR_READ_INTERVAL      = "readInterval";
    public static String ATTR_NREADS             = "nreads";
    public static String ATTR_RESET_DELAY        = "resetDelay";
    public static String NO_VALUE                = "none";
    public static final int CAMERA_IMAGING       = 0;
    public static final int CAMERA_SPECTROSCOPY  = 1;

    // Class variables representing defaults, LUTs, etc
    public static String CONFIG_TYPE;
    public static String VERSION;
    public static String[] DETECTOR_SIZE;
    public static int DETECTOR_WIDTH;
    public static int DETECTOR_HEIGHT;
    public static double DETANGLE;
    public static double PIXPITCH;
    public static String DARKFILTER;
    public static String DEFAULT_FILTER;
    public static String[] CAMERAS;
    public static String DEFAULT_CAMERA;
    public static double N_TO_Q_BOUNDARY;
    public static String DEFAULT_FLAT_SOURCE;
    public static String DEFAULT_FLAT_SAMPLING;
    public static String[] FLAT_SOURCES;
    public static String[] POLARIMETRY;
    public static String DEFAULT_POLARIMETRY;
    public static LookUpTable WAVEPLATES;
    // Imaging
    public static double IMAGING_PIXEL_SCALE;
    public static double[] IMAGING_FIELD_OF_VIEW = null;
    public static LookUpTable NBANDFILT;
    public static LookUpTable QBANDFILT;
    public static LookUpTable SPECFILT;
    public static LookUpTable FILTERS;
    // Spectroscopy
    public static double SPECT_PIXEL_SCALE;
    public static double SPECT_FOCAL_LENGTH;
    public static double[] SPECT_FIELD_OF_VIEW;
    public static String[] SAMPLINGS;
    public static String DEFAULT_SAMPLING;
    public static LookUpTable DISPERSERS;
    public static String DEFAULT_DISPERSER;
    public static int CENTRAL_ROW;
    public static int PEAK_ROW;
    public static LookUpTable MASKS;
    public static String DEFAULT_MASK;
    // Disperser 1
    public static LookUpTable ORDERS1;
    public static LookUpTable BLOCKERS1;
    public static double[] ZERO1;
    // Disperser 2
    public static LookUpTable ORDERS2;
    public static LookUpTable BLOCKERS2;
    public static double[] ZERO2;
    // Disperser 3
    public static LookUpTable ORDERS3;
    public static LookUpTable BLOCKERS3;
    public static double[] ZERO3;
    // Disperser 4
    public static LookUpTable ORDERS4;
    public static LookUpTable BLOCKERS4;
    public static double[] ZERO4;
    // Disperser 5
    public static LookUpTable ORDERS5;
    public static LookUpTable BLOCKERS5;
    public static double[] ZERO5;
    // Data acquisition - general
    public static LookUpTable CHOPS;
    public static String DEFAULT_CHOPFREQ;
    public static double KPOL;
    public static double KSKY;
    public static double KIM;
    public static double KBAND;
    public static double TEXPBASE;
    public static double TEXPMIN;
    public static double TEXPMAX;
    public static double DEFAULT_TOBS;
    public static LookUpTable ACQMODES;
    public static String DEFAULT_ACQMODE;
    public static LookUpTable EXPTIMINGS;
    public static double DEFAULT_EXPTIME;
    public static double EXPOSURE_OVERHEAD = 0.01;
    public static double READ_INTERVAL;
    public static int NULL_READS;
    public static double RESET_DELAY;
    // Data acquisition - bias
    public static double DEFBIASEXPTIME;
    public static int DEFBIASCOADDS;
    // Instance variables of the class
    String filter_category = null;
    String filter_ot;
    double observation_time;
    // Data acquisition - dark
    // Data acquisition - flat
    String flat_source;
    // Data acquisition - arc

    public static final SpType SP_TYPE = SpType.create(
        SpType.OBSERVATION_COMPONENT_TYPE, "inst.michelle", "Michelle");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpInstMichelle());
    }

    // Constructor reads instrument .cfg file and initialises values
    public SpInstMichelle()
    {
        super(SP_TYPE);

        addCapability( new SpChopCapability()  );
        addCapability( new SpStareCapability() );

        // Read in the instrument config file
        String baseDir = System.getProperty("ot.cfgdir");
        String cfgFile = baseDir + "michelle.cfg";
        _readCfgFile (cfgFile);

        // Set the initial values of the attributes
        String attr  = ATTR_CONFIG_TYPE;
        String value = CONFIG_TYPE;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_VERSION;
        value = VERSION;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_CAMERA;
        value = DEFAULT_CAMERA;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_MASK;
        value = DEFAULT_MASK;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_MASK_ANGLE;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr  = ATTR_POS_ANGLE;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr  = ATTR_DISPERSER;
        value = DEFAULT_DISPERSER;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_ORDER;
        _avTable.noNotifySet(attr, "0", 0);

        attr  = ATTR_CENTRAL_WAVELENGTH;
        _avTable.noNotifySet(attr, "10.0", 0);

        attr  = ATTR_POLARIMETRY;
        value = DEFAULT_POLARIMETRY;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_FILTER;
        value = DEFAULT_FILTER;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_DARKFILTER;
        value = DARKFILTER;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_SAMPLING;
        value = DEFAULT_SAMPLING;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_SCIENCE_AREA;
        _avTable.noNotifySet(attr, "0.0 x 0.0", 0);

        attr  = ATTR_ACQMODE;
        value = DEFAULT_ACQMODE;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_CHOP_FREQUENCY;
        value = DEFAULT_CHOPFREQ;
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_CHOP_DELAY;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr  = ATTR_EXPOSURE_TIME;
        value = Double.toString(DEFAULT_EXPTIME);
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_READ_INTERVAL;
        value = Double.toString(READ_INTERVAL);
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_NREADS;
        _avTable.noNotifySet(attr, "0", 0);

        attr  = ATTR_RESET_DELAY;
        value = Double.toString(RESET_DELAY);
        _avTable.noNotifySet(attr, value, 0);

        attr  = ATTR_COADDS;
        _avTable.noNotifySet(attr, "1", 0);

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
                System.out.println(
                    "_readCfgFile> keyword = " + instInfo.getKeyword());
		if (InstCfg.matchAttr (instInfo, "instrument_port")) {
                    INSTRUMENT_PORT = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "config_type")) {
                    CONFIG_TYPE = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "version")) {
                    VERSION = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "instrument_aper")) {
                    INSTRUMENT_APER = instInfo.getValueAsArray();
		    setInstApX (INSTRUMENT_APER[XAP_INDEX]);
		    setInstApY (INSTRUMENT_APER[YAP_INDEX]);
		    setInstApZ (INSTRUMENT_APER[ZAP_INDEX]);
		    setInstApL (INSTRUMENT_APER[LAP_INDEX]);
		} else if (InstCfg.matchAttr (instInfo, "detector_size")) {
                    DETECTOR_SIZE = instInfo.getValueAsArray();
		    DETECTOR_WIDTH = Integer.parseInt(DETECTOR_SIZE[0]);
		    DETECTOR_HEIGHT = Integer.parseInt(DETECTOR_SIZE[1]);
		} else if (InstCfg.matchAttr (instInfo, "imaging_field_of_view")) {
                    String starray[] = instInfo.getValueAsArray();
                    IMAGING_FIELD_OF_VIEW = new double[starray.length];
                    for (i=0;i<starray.length;i++) {
                        IMAGING_FIELD_OF_VIEW[i] = Double.valueOf(starray[i]).doubleValue();
		    }
		} else if (InstCfg.matchAttr (instInfo, "detangle")) {
                    DETANGLE = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "pixpitch")) {
                    PIXPITCH = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "darkfilter")) {
                    DARKFILTER = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_filter")) {
                    DEFAULT_FILTER = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "cameras")) {
                    CAMERAS = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "n_to_q_boundary")) {
                    N_TO_Q_BOUNDARY = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "default_camera")) {
                    DEFAULT_CAMERA = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "polarimetry")) {
                    POLARIMETRY = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "default_polarimetry")) {
                    DEFAULT_POLARIMETRY = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "waveplates")) {
                    WAVEPLATES = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "imaging_pixel_scale")) {
                    IMAGING_PIXEL_SCALE = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "nbandfilt")) {
                    NBANDFILT = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "qbandfilt")) {
                    QBANDFILT = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "specfilt")) {
                    SPECFILT = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "filters")) {
                    FILTERS = instInfo.getValueAsLUT();
		    // insert the filter set tables
		    try {
			int row = FILTERS.indexInColumn("NBANDFILT", 1);
			FILTERS.setElementAt (NBANDFILT, row, 1);
			row = FILTERS.indexInColumn("QBANDFILT", 1);
			FILTERS.setElementAt (QBANDFILT, row, 1);
			row = FILTERS.indexInColumn("SPECFILT", 1);
			FILTERS.setElementAt (SPECFILT, row, 1);
		    }catch (NoSuchElementException ex) {
			System.out.println ("Error indexing in filters table in Michelle cfg file");
			System.out.println (ex);
		    }catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println ("Unexpected error setting Michelle luts");
			System.out.println (ex);
		    }
		} else if (InstCfg.matchAttr (instInfo, "spect_pixel_scale")) {
                    SPECT_PIXEL_SCALE = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "spect_focal_length")) {
                    SPECT_FOCAL_LENGTH = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "flat_sources")) {
                    FLAT_SOURCES = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "samplings")) {
                    SAMPLINGS = instInfo.getValueAsArray();
		} else if (InstCfg.matchAttr (instInfo, "default_sampling")) {
                    DEFAULT_SAMPLING = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_flat_source")) {
                    DEFAULT_FLAT_SOURCE = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "default_flat_sampling")) {
                    DEFAULT_FLAT_SAMPLING = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "orders1")) {
                    ORDERS1 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "blockers1")) {
                    BLOCKERS1 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "zero1")) {
                    String starray[] = instInfo.getValueAsArray();
                    ZERO1 = new double[starray.length];
                    for (i=0;i<starray.length;i++) {
                        ZERO1[i] = Double.valueOf(starray[i]).doubleValue();
		    }
		} else if (InstCfg.matchAttr (instInfo, "orders2")) {
                    ORDERS2 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "blockers2")) {
                    BLOCKERS2 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "zero2")) {
                    String starray[] = instInfo.getValueAsArray();
                    ZERO2 = new double[starray.length];
                    for (i=0;i<starray.length;i++) {
                        ZERO2[i] = Double.valueOf(starray[i]).doubleValue();
		    }
		} else if (InstCfg.matchAttr (instInfo, "orders3")) {
                    ORDERS3 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "blockers3")) {
                    BLOCKERS3 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "zero3")) {
                    String starray[] = instInfo.getValueAsArray();
                    ZERO3 = new double[starray.length];
                    for (i=0;i<starray.length;i++) {
                        ZERO3[i] = Double.valueOf(starray[i]).doubleValue();
		    }
		} else if (InstCfg.matchAttr (instInfo, "orders4")) {
                    ORDERS4 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "blockers4")) {
                    BLOCKERS4 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "zero4")) {
                    String starray[] = instInfo.getValueAsArray();
                    ZERO4 = new double[starray.length];
                    for (i=0;i<starray.length;i++) {
                        ZERO4[i] = Double.valueOf(starray[i]).doubleValue();
		    }
		} else if (InstCfg.matchAttr (instInfo, "orders5")) {
                    ORDERS5 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "blockers5")) {
                    BLOCKERS5 = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "zero5")) {
                    String starray[] = instInfo.getValueAsArray();
                    ZERO5 = new double[starray.length];
                    for (i=0;i<starray.length;i++) {
                        ZERO5[i] = Double.valueOf(starray[i]).doubleValue();
		    }
		} else if (InstCfg.matchAttr (instInfo, "dispersers")) {
		    DISPERSERS = instInfo.getValueAsLUT();
		    // insert the order tables
		    try {
			int row = DISPERSERS.indexInColumn("ORDERS1", 4);
			DISPERSERS.setElementAt (ORDERS1, row, 4);
			row = DISPERSERS.indexInColumn("ORDERS2", 4);
			DISPERSERS.setElementAt (ORDERS2, row, 4);
			row = DISPERSERS.indexInColumn("ORDERS3", 4);
			DISPERSERS.setElementAt (ORDERS3, row, 4);
			row = DISPERSERS.indexInColumn("ORDERS4", 4);
			DISPERSERS.setElementAt (ORDERS4, row, 4);
			row = DISPERSERS.indexInColumn("ORDERS5", 4);
			DISPERSERS.setElementAt (ORDERS5, row, 4);
			row = DISPERSERS.indexInColumn("BLOCKERS1", 5);
			DISPERSERS.setElementAt (BLOCKERS1, row, 5);
			row = DISPERSERS.indexInColumn("BLOCKERS2", 5);
			DISPERSERS.setElementAt (BLOCKERS2, row, 5);
			row = DISPERSERS.indexInColumn("BLOCKERS3", 5);
			DISPERSERS.setElementAt (BLOCKERS3, row, 5);
			row = DISPERSERS.indexInColumn("BLOCKERS4", 5);
			DISPERSERS.setElementAt (BLOCKERS4, row, 5);
			row = DISPERSERS.indexInColumn("BLOCKERS5", 5);
			DISPERSERS.setElementAt (BLOCKERS5, row, 5);
			row = DISPERSERS.indexInColumn("ZERO1", 6);
			DISPERSERS.setElementAt (ZERO1, row, 6);
			row = DISPERSERS.indexInColumn("ZERO2", 6);
			DISPERSERS.setElementAt (ZERO2, row, 6);
			row = DISPERSERS.indexInColumn("ZERO3", 6);
			DISPERSERS.setElementAt (ZERO3, row, 6);
			row = DISPERSERS.indexInColumn("ZERO4", 6);
			DISPERSERS.setElementAt (ZERO4, row, 6);
			row = DISPERSERS.indexInColumn("ZERO5", 6);
			DISPERSERS.setElementAt (ZERO5, row, 6);
		    }catch (NoSuchElementException ex) {
			System.out.println ("Error indexing in dispersers table in Michelle cfg file");
			System.out.println (ex);
		    }catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println ("Unexpected error setting Michelle luts");
			System.out.println (ex);
		    }
		} else if (InstCfg.matchAttr (instInfo, "default_disperser")) {
                    DEFAULT_DISPERSER = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "central_row")) {
                    CENTRAL_ROW = Integer.parseInt(instInfo.getValue());
		} else if (InstCfg.matchAttr (instInfo, "peak_row")) {
                    PEAK_ROW = Integer.parseInt(instInfo.getValue());
		} else if (InstCfg.matchAttr (instInfo, "masks")) {
                    MASKS = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "default_mask")) {
                    DEFAULT_MASK = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "acqmodes")) {
                    ACQMODES = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "chops")) {
                    CHOPS = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "default_chopfreq")) {
                    DEFAULT_CHOPFREQ = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "kpol")) {
                    KPOL = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "ksky")) {
                    KSKY = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "kim")) {
                    KIM = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "kband")) {
                    KBAND = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "default_acqmode")) {
                    DEFAULT_ACQMODE = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "exptimings")) {
                    EXPTIMINGS = instInfo.getValueAsLUT();
		} else if (InstCfg.matchAttr (instInfo, "default_exptime")) {
                    DEFAULT_EXPTIME = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "texpbase")) {
                    TEXPBASE = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "texpmin")) {
                    TEXPMIN = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "texpmax")) {
                    TEXPMAX = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "default_tobs")) {
                    DEFAULT_TOBS = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "defbiasexptime")) {
                    DEFBIASEXPTIME = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "defbiascoadds")) {
                    DEFBIASCOADDS = Integer.parseInt(instInfo.getValue());
		} else if (InstCfg.matchAttr (instInfo, "read_interval")) {
                    READ_INTERVAL = Double.valueOf(instInfo.getValue()).doubleValue();
		} else if (InstCfg.matchAttr (instInfo, "null_reads")) {
                    NULL_READS = Integer.parseInt(instInfo.getValue());
		} else if (InstCfg.matchAttr (instInfo, "reset_delay")) {
                    RESET_DELAY = Double.valueOf(instInfo.getValue()).doubleValue();
		} else {
                    System.out.println("Unmatched keyword:" + instInfo.getKeyword());
		}
	    }
	}catch (IOException e) {
	    System.out.println ("Error reading Michelle inst. cfg file");
	}
    }

    public static LookUpTable getDispersers() {
	return DISPERSERS;
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
     * Set the science area
     */
    public void
    setScienceArea(String scienceArea)
    {
        _avTable.set(ATTR_SCIENCE_AREA, scienceArea);
    }
  
    /**
     * Return the science area based upon the current camera.
     */
    public double[]
    getScienceArea()
    {
         double fov[] = IMAGING_FIELD_OF_VIEW;
         if (!isImaging()) {
             double w = getMaskWidthPixels();
             double h = getMaskHeightPixels();
             double ma = getMaskAngle() * Math.PI/180.0;
             fov[0] = SPECT_PIXEL_SCALE * w / Math.cos(ma);
             fov[1] = SPECT_PIXEL_SCALE * h * Math.cos(ma);
	 }
         return fov;
    }

    /**
     * Get the science area as a string
     */
    public String
    getScienceAreaString()
    {
        double fov[];
        fov = getScienceArea();
        double w = MathUtil.round(fov[0], 2);
        double h = MathUtil.round(fov[1], 2);
        String fovs = w + " x " + h;
        setScienceArea(fovs);
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
    }

    /**
     * Initialise instance variables
     */
    public void
    initInstance()
    {
        // Initialise instance variables and initial config
        filter_category = null;
        filter_ot = null;
        flat_source = null;
        observation_time = DEFAULT_TOBS;
        System.out.println("initInstance> Calling useDefaultAcquisition");
        useDefaultAcquisition();
        System.out.println("initInstance> Calling setAcquisition");
        setAcquisition();
    }

    /**
     * Get the camera.
     */
    public String
    getCamera()
    {
        String camera = _avTable.get(ATTR_CAMERA);
        if (camera == null) {
            System.out.println("getCamera> setting default camera");
            camera = DEFAULT_CAMERA;
            setCamera(camera);
        }
        return camera;
    }

    /**
     * Get the list of dispersers
     */
    public String[]
    getDisperserList()
    {
        String disperserList[] = new String[DISPERSERS.getNumRows()];
        for (int i=0; i<DISPERSERS.getNumRows(); i++) {
            disperserList[i] = (String)DISPERSERS.elementAt(i,0);
	}
        return disperserList;
    }

    /**
     * Set the disperser name
     */
    public void
    setDisperser(String disperser)
    {
        _avTable.set(ATTR_DISPERSER, disperser);
        useDefaultCentralWavelength();
    }

    /**
     * Get the disperser name
     */
    public String
    getDisperser()
    {
        String disperser = _avTable.get(ATTR_DISPERSER);
        if (disperser == null) {
            _avTable.noNotifySet(ATTR_DISPERSER, DEFAULT_DISPERSER, 0);
            disperser = DEFAULT_DISPERSER;
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
        if (!isImaging()) {
            useDefaultOrder();
            useDefaultFilterOT();
	}
    }

    /**
     * Set the central wavelength as a string.
     */
    public void
    setCentralWavelength(String cwl)
    {
        double d = 0.0;
        try {
            Double tmp = Double.valueOf(cwl);
            d = tmp.doubleValue();
        } catch (Exception ex) {}
        setCentralWavelength(d);
    }

    /**
     * Get the default central wavelength using the current disperser.
     */
    public double
    getDefaultCentralWavelength()
    {
        int di = getDisperserIndex();
        try
	    {return Double.valueOf((String) DISPERSERS.elementAt(di,3)).doubleValue();
            } catch (NumberFormatException e) {}
        return 0;
    }

    /**
     * Get the central wavelength.
     */
    public double
    getCentralWavelength()
    {
        double cwl;
        if (isImaging()) {
	    /* Get centre wavelength of filter */
            int fi = getFilterIndex();
            cwl = Double.valueOf((String)getFilterLUT().elementAt(getFilterIndex(),3)).doubleValue();
            setCentralWavelength (cwl);
        } else {
            cwl = _avTable.getDouble(ATTR_CENTRAL_WAVELENGTH, 0.0);
            if (cwl == 0.0) {
                cwl = getDefaultCentralWavelength();
                setCentralWavelength (cwl);
            }
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
     * Get the list of masks
     */
    public String[]
    getMaskList()
    {
        String maskList[] = new String[MASKS.getNumRows()];
        for (int i=0; i<MASKS.getNumRows(); i++) {
            maskList[i] = (String)MASKS.elementAt(i,0);
	}
        return maskList;
    }

    /**
     * Set the mask.
     */
    public void
    setMask(String mask)
    {
        _avTable.set(ATTR_MASK, mask);
    }

    /**
     * Get the mask.
     */
    public String
    getMask()
    {
        String mask = _avTable.get(ATTR_MASK);
        if (mask == null) {
            mask = DEFAULT_MASK;
            setMask(mask);
        }
        return mask;
    }

    /**
     * Get the index of the current mask.
     */
    public int
    getMaskIndex()
    {
	int mask;
	try {
	    mask = MASKS.indexInColumn (getMask(), 0);
	}catch (Exception ex) {
	    mask = 0;
	}
	return mask;
    }

    /**
     * Get the width (pixels) of the current mask.
     */
    public double
    getMaskWidthPixels()
    {
        try {
            return new Double((String) MASKS.elementAt(getMaskIndex(),1)).doubleValue();
	}catch (IndexOutOfBoundsException e) {
	    return 0.0;
	}catch (NumberFormatException e) {
	    return 0.0;
	}
    }

    /**
     * Get the height (pixels) of the current mask.
     */
    public double
    getMaskHeightPixels()
    {
        try {
            return new Double((String) MASKS.elementAt(getMaskIndex(),2)).doubleValue();
	}catch (IndexOutOfBoundsException e) {
	    return 0.0;
	}catch (NumberFormatException e) {
	    return 0.0;
	}
    }

   /**
    * Override the set-position-angle methods so that we can ensure
    * it's set to zero: stops dragging of the angle in tpe.
    * Set the position angle in degrees from due north, updating the
    * observation data with the new position angle.  This method is
    * ultimately called by the other setPosAngle methods.
    */
    public void setPosAngleDegrees( double posAngle ) {
        super.setPosAngleDegrees( posAngle);
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
     * Get the list of filter categories
     */
    public String[]
    getFilterCategoryList()
    {
        String filterCategoryList[] = new String[FILTERS.getNumRows()];
        for (int i=0; i<FILTERS.getNumRows(); i++) {
            filterCategoryList[i] = (String)FILTERS.elementAt(i,0);
	}
        return filterCategoryList;
    }

    /**
     * Set the filter category
     */
    public void
    setFilterCategory(String filterCategory)
    {
        filter_category = filterCategory;
        useDefaultFilterOT();
    }

    /**
     * Get the filter category
     */
    public String
    getFilterCategory()
    {
        if (filter_category == null) {
            String filterCategory = (String)FILTERS.elementAt(0,0);
            setFilterCategory(filterCategory);
	}
        return filter_category;
    }

    /**
     * Get the filter category index
     */
    public int
    getFilterCategoryIndex()
    {
        String filterCategory = getFilterCategory();
        int fcIndex;
	try {
	    fcIndex = FILTERS.indexInColumn(filterCategory, 0);
	}catch (ArrayIndexOutOfBoundsException ex) {
	    System.out.println ("Failed to find filter category index");
            fcIndex = 0;
	}catch (NoSuchElementException ex) {
	    System.out.println ("Failed to find filter index");
            fcIndex = 0;
	}
        return fcIndex;
    }

    /**
     * Get the lookup table for the current filter category
     */
    public LookUpTable
    getFilterLUT()
    {
        int fcIndex = getFilterCategoryIndex();
        LookUpTable flut = (LookUpTable) FILTERS.elementAt(fcIndex,1);
        return flut;
    }

    /**
     * Get the list of filters for the current filter category
     */
    public String[]
    getFilterList()
    {
        LookUpTable flut = getFilterLUT();
        String filterList[] = new String[flut.getNumRows()];
        for (int i=0; i<flut.getNumRows(); i++) {
            filterList[i] = (String)flut.elementAt(i,0);
	}
        return filterList;
    }

    /**
     * Forget the OT name of the filter.
     */
    public void
    useDefaultFilterOT()
    {
        filter_ot = null;
    }

    /**
     * Set the OT name of the filter.
     */
    public void
    setFilterOT(String filterOT)
    {
        filter_ot = filterOT;
    }

    /**
     * Get the default filter OT
     */
    public String
    getDefaultFilterOT()
    {
        System.out.println("getDefaultFilterOT> called");
        LookUpTable flut = getFilterLUT();
        String filterOT = (String)flut.elementAt(0,0);
        setFilterOT(filterOT);
        return filterOT;
    }

    /**
     * Get the OT name of the filter.
     */
    public String
    getFilterOT()
    {
        String filterOT = filter_ot;
        if (filterOT == null) {
            filterOT = getDefaultFilterOT();
            setFilterOT(filterOT);
        }
        return filterOT;
    }

    /**
     * Get the index of filter name in filter category
     */
    public int
    getFilterIndex()
    {
        LookUpTable flut = getFilterLUT();
        String filterOT = getFilterOT();
        int fIndex = flut.indexInColumn(filterOT,0);
        return fIndex;
    }

    /**
     * Set the filter.
     */
    public void
    setFilter(String filter)
    {
        _avTable.set(ATTR_FILTER, filter);
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
            /* ci is the column index in the filter category lut */
            if (isPolarimetry()) {
                ci = 2;
            }
            filter = (String)getFilterLUT().elementAt(getFilterIndex(),ci);
        } else {
            int di = getDisperserIndex();
            double cwl = getCentralWavelength();
	    /* ci is the column index in the BLOCKERS lut */
            if (isPolarimetry()) {
                ci = 2;
	    }
	    try {
	        LookUpTable blut = (LookUpTable) DISPERSERS.elementAt(di,5);
	        int pos = blut.rangeInColumn(cwl, 0);
	        filter = (String)blut.elementAt(pos, ci);
	    }catch( Exception ex) {
	        System.out.println ("Error getting blocking filter");
	        System.out.println ("For disperser "+di+" and lambda "+cwl);
	    }
	}
        setFilter(filter);
        return filter;
    }

    /**
     * Get the name of the filter to be used for darks
     */
    public String
    getDarkFilter()
    {
        return DARKFILTER;
    }

    /**
     * Get the arc blocking filter for the current spectroscopy configuration
     */
    public String
    getArcFilter()
    {
         String arcFilter = "undefined";
         if (!isImaging()) {
             int di = getDisperserIndex();
             double cwl = getCentralWavelength();
	     try {
	         LookUpTable blut = (LookUpTable) DISPERSERS.elementAt(di,5);
                 int pos = blut.rangeInColumn(cwl, 0);
                 arcFilter = (String)blut.elementAt(pos, 3);
	     }catch( Exception ex) {
	         System.out.println ("Error getting blocking filter for arc obs");
	         System.out.println ("For disperser "+di+" and lambda "+cwl);
	     }
	 }
         return arcFilter;
    }

    /**
     * Set the mode.
     */
    public void
    setMode(String mode)
    {
        _avTable.set(ATTR_MODE, mode);
    }

    /**
     * Get the mode.
     */
    public String
    getMode()
    {
        String mode = _avTable.get(ATTR_MODE);
        if (mode == null) mode = DEFAULT_ACQMODE; 
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
        int di = getDisperserIndex();
        double cwl = getCentralWavelength();
	try {
	    LookUpTable dolut = (LookUpTable) DISPERSERS.elementAt(di,4);
	    int pos = dolut.rangeInColumn(cwl, 0);
	    order = Integer.parseInt((String)dolut.elementAt(pos, 1));
	}catch( Exception ex) {
	    System.out.println ("Error getting default order");
	    System.out.println ("For disperser "+di+" and lambda "+cwl);
	    order = 0;
	}
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

    /**
     * Set the pixel sampling
     */
    public void
    setPixelSampling(String pixelSampling)
    {
        /* Possibly check that requested sampling is valid */
        _avTable.set(ATTR_SAMPLING, pixelSampling);
    }

    /**
     * Get the pixel sampling
     */
    public String
    getPixelSampling()
    {
        String sampling = _avTable.get(ATTR_SAMPLING);
        return sampling;
    }

    /**
     * Get the disperser rule value
     */
    public double
    getRule()
    {
        int di = getDisperserIndex();
	double rule = Double.valueOf((String) DISPERSERS.elementAt(di,1)).doubleValue();
        return rule;
    }

    /**
     * Get the angle of incidence
     */
    public double
    getAngleOfIncidence()
    {
        if (isImaging()) return 0;
        int n = getOrder();
        double cwl = getCentralWavelength();
        double rule = getRule();
        double aoi = Math.asin((n*cwl)/(2.0*rule));
        return aoi;
    }

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
    public double[]
    getSpectralCoverage()
    {
        double scov[] = new double[2];
        if (isImaging()) {
            scov[0] = 0;
            scov[1] = 0;
	} else {
            double a = getAngleOfIncidence();
            double rule = getRule();
            int n = getOrder();
            double range = (rule/n) * (Math.sin(a+DETANGLE) - Math.sin(a-DETANGLE));
            double cwl = getCentralWavelength();
            scov[0] = cwl - range/2.0;
            scov[1] = cwl + range/2.0;
	}
        return scov;
    }

    /**
     * Get the spectral coverage as a string
     */
    public String
    getSpectralCoverageString()
    {
        double scov[];
        String scovs = null;
        scov = getSpectralCoverage();
        if ((scov[0] == 0.0) || (scov[1] == 0.0)) {
	} else {
            double lo = MathUtil.round(scov[0], 2);
            double hi = MathUtil.round(scov[1], 2);
            scovs = lo + " - " + hi;
	}
        setSpectralCoverage(scovs);
        return scovs;
    }

    /**
     * Get the dispersion
     */
    public double
    getDispersion()
    {
        if (isImaging()) return 0;
        double aoi = getAngleOfIncidence();
        double cwl = getCentralWavelength();
        double disp = 2.0 * (Math.tan(aoi))/cwl;
        return disp;
    }

    /**
     * Set the mask angle in degrees
     */
    public void
    setMaskAngle(double maskAngle)
    {
	// Trim the number of decimal places...
	double ma = MathUtil.round (maskAngle, 4);
        _avTable.set(ATTR_MASK_ANGLE, ma);
    }

    /**
     * Get the mask angle in degrees
     */
    public double
    getMaskAngle()
    {
        if (isImaging()) return 0;
        double ma = Math.atan(0.611 * Math.tan(getAngleOfIncidence()));
        // Convert to degrees
        ma = ma * 180.0 / Math.PI;
        setMaskAngle(ma);
        return ma;
    }

    /**
     * Get the resolving power
     */
    public double
    getResolvingPower()
    {
        double rp;
        if (isImaging()) {
            /* For imaging, this is 1/bandpass for current filter */
            rp = 1.0/Double.valueOf((String)getFilterLUT().elementAt(getFilterIndex(),4)).doubleValue();
	} else {
            double disp = getDispersion();
            double cwl = getCentralWavelength();
            double w = getMaskWidthPixels();
            rp = (disp * cwl * SPECT_FOCAL_LENGTH) / (w * PIXPITCH);
	}
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
     * Set the waveplate
     */
    public void
    setWaveplate(String waveplate)
    {
        _avTable.set(ATTR_WAVEPLATE, waveplate);
    }

    /**
     * Get the waveplate
     */
    public String
    getWaveplate()
    {
        String waveplate= "none";
        if (isPolarimetry()) {
            double cwl;
            if (isImaging()) {
	        /* Get centre wavelength of filter */
                cwl = Double.valueOf((String)getFilterLUT().elementAt(getFilterIndex(),3)).doubleValue();
	    } else {
                cwl = getCentralWavelength();
	    }
	    try {
	        int pos = WAVEPLATES.rangeInColumn(cwl, 0);
	        waveplate = (String)WAVEPLATES.elementAt(pos, 1);
	    }catch( Exception ex) {
	        System.out.println ("Error getting waveplate");
	    }
	}
        setWaveplate(waveplate);
        return waveplate;
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
     * Get the flat source
     * Should handle case of flat_source = null by getting default flat source
     */
    public String
    getFlatSource()
    {
        return flat_source;
    }

    /**
     * Set the flat sampling
     */
    public void
    setFlatSampling(String flatSampling)
    {
        _avTable.set(ATTR_FLAT_SAMPLING, flatSampling);
    }

    /**
     * Get the list of available chop frequencies
     */
    public String[]
    getChopList()
    {
        String chopList[] = new String[CHOPS.getNumRows()];
        for (int i=0; i<CHOPS.getNumRows(); i++) {
            chopList[i] = (String)CHOPS.elementAt(i,0);
	}
        return chopList;
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
     * Get the expbase multiplier
     */
    public int
    getExpbaseMult()
    {
        int i1;
        double rp = getResolvingPower();
        double rpMin = Double.valueOf((String)EXPTIMINGS.elementAt(0,0)).doubleValue();
        int iMax = EXPTIMINGS.getNumRows() - 1;
        double rpMax = Double.valueOf((String)EXPTIMINGS.elementAt(iMax,0)).doubleValue();
        if (rp <= rpMin) {
            rp = rpMin;
            i1 = 1;
        } else if (rp >= rpMax) {
            rp = rpMax;
            i1 = iMax;
	} else {
            i1 = EXPTIMINGS.rangeInColumn(rp,0);
        }
        int i0 = i1-1;
        double rp0 = Double.valueOf((String)EXPTIMINGS.elementAt(i0,0)).doubleValue();
        double m0  = Double.valueOf((String)EXPTIMINGS.elementAt(i0,1)).doubleValue();
        double rp1 = Double.valueOf((String)EXPTIMINGS.elementAt(i1,0)).doubleValue();
        double m1  = Double.valueOf((String)EXPTIMINGS.elementAt(i1,1)).doubleValue();
        int interpm = (int) (m0 + ((m1 - m0) * (rp - rp0)/(rp1 - rp0)));
        return interpm;
    }

    /**
     * Use default chop frequency
     */
    public void
    useDefaultChopFreq()
    {
        _avTable.rm(ATTR_CHOP_FREQUENCY);
    }

    /**
     * Get the chop frequency
     */
    public String
    getChopFreq()
    {
        String cfs = _avTable.get(ATTR_CHOP_FREQUENCY);
        if (cfs == null) {
            cfs = getDefaultChopFrequency();
            setChopFreq(cfs);
	}
        return cfs;
    }

    /**
     * Set the flat exposure time
     */
    public void
    setFlatExpTime(double flatExpTime)
    {
        _avTable.set(ATTR_FLAT_EXPOSURE_TIME, flatExpTime);
    }

    /**
     * Get the default chop frequency
     */
    public String
    getDefaultChopFrequency()
    {
        String dcfs;
        int i;
        double dwell;
        double rp = getResolvingPower();
        double rpMin = Double.valueOf((String)EXPTIMINGS.elementAt(0,0)).doubleValue();
        int iMax = EXPTIMINGS.getNumRows() - 1;
        double rpMax = Double.valueOf((String)EXPTIMINGS.elementAt(iMax,0)).doubleValue();
        if (rp <= rpMin) {
            i = 0;
        } else if (rp >= rpMax) {
            i = iMax-1;
	} else {
            i = EXPTIMINGS.rangeInColumn(rp,0) - 1;
        }
        double cwl = getCentralWavelength();
        double et = getExpTime();
        if (cwl < N_TO_Q_BOUNDARY) {
            dcfs = (String)EXPTIMINGS.elementAt(i,2);
        } else {
            dcfs = (String)EXPTIMINGS.elementAt(i,3);
        }
        System.out.println("getDefaultChopFrequency> Initial freq = " + dcfs);
        // Adjust the chop frequency to be consistent with the exposure time
        if (dcfs.equals("0.00")) {
            dwell = et + EXPOSURE_OVERHEAD + 1.0;
        } else {
            double dcf = Double.valueOf(dcfs).doubleValue();
            dwell = 0.5/dcf;
        }
        while (dwell < (et+EXPOSURE_OVERHEAD)) {
	    // Get the next slowest chop frequency
            i = CHOPS.indexInColumn(dcfs,0);
            dcfs = (String)CHOPS.elementAt(i-1,0);
	    if (dcfs.equals("0.00")) {
                dwell = et + EXPOSURE_OVERHEAD + 1.0;
            } else {
                double dcf = Double.valueOf(dcfs).doubleValue();
                dwell = 0.5/dcf;
	    }
        }   
        System.out.println("getDefaultChopFrequency> Final freq = " + dcfs);
        return dcfs;
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
        String cfs = getChopFreq();
        int i = CHOPS.indexInColumn(cfs,0);
        double cd = Double.valueOf((String)CHOPS.elementAt(i,1)).doubleValue();
        setChopDelay(cd);
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

    /**
     * Get the number of reads in the exposure
     */
    public int
    getNreads()
    {
        int nr = 0;
        String acqMode = getAcqMode();
        if ((acqMode.equals("NDSTARE")) || (acqMode.equals("NDCHOP"))) {
	    // Need to compute readInterval and number of reads
            double et = getExposureTime();
            double ri = getReadInterval();
            double rd = getResetDelay();
            nr = 1 - NULL_READS + (int)((et - rd)/ri);
            setNreads(nr);
	} else {
            nr = 0;
	}
        setNreads(nr);
        return nr;
    }

    /**
     * Set the reset delay in seconds
     */
    public void
    setResetDelay(double resetDelay)
    {
        _avTable.set(ATTR_RESET_DELAY, resetDelay);
    }

    /**
     * Get the reset delay in seconds
     */
    public double
    getResetDelay()
    {
        double rd = _avTable.getDouble(ATTR_RESET_DELAY, 0.0);
        return rd;
    }


    /**
     * Use default observation time
     */
    public void
    useDefaultObservationTime()
    {
        observation_time = DEFAULT_TOBS;
    }

    /**
     * Use default exposure time
     */
    public void
    useDefaultExposureTime()
    {
        _avTable.rm(ATTR_EXPOSURE_TIME);
    }

    /**
     * Set the observation time in seconds
     */
    public void
    setObservationTime(double obsTime)
    {
        observation_time = obsTime;
    }

    /**
     * Get the observation time in seconds
     */
    public double
    getObservationTime()
    {
        return observation_time;
    }

    /**
     * Get the observation time in seconds as a String
     */
    public String
    getObservationTimeString()
    {
        double obt = getObservationTime();
        String obts = Double.toString(obt);
        return obts;
    }

    /**
     * Get the exposure time.
     */
    public double
	getExpTime()
    {
	
	double time = getExposureTime();
	if (time == 0.0) {
	    time = getDefaultExpTime();
	    setExpTime (Double.toString(time));
	}
	return time;
    }

    /**
     * Get the exposure time as a string
     */
    public String
	getExpTimeString()
    {
        String timeAsString = Double.toString(MathUtil.round(getExpTime(),4));
	return timeAsString;
    }

    /**
     * Set the exposure time.
     */
    public void
	setExpTime(String seconds)
    {
	String timeAsStr;
	
	timeAsStr = seconds;
	if (timeAsStr == null) {
	    // No action on null string
            System.out.println("setExpTime> null string");
	} else {
            setExposureTime (timeAsStr);
	}
    }

    /**
     * Get the default sky exposure time
     */
    public double
    getDefaultSkyExpTime()
    {
        double set = TEXPBASE * getExpbaseMult() * KSKY;
        if (isPolarimetry()) set = KPOL * set;
        if (isImaging()) set = KIM * set;
        if (getCentralWavelength() < 15.0) set = KBAND * set;
        return set;
    }

    /**
     * Adjust exposure time to less than TEXTMAX and integer
     * multiple of TEXTMIN
     */
    public double
    limitExpTime(double et)
    {
        double let = et;
        if (let > TEXPMAX) let = TEXPMAX;
        if (let > TEXPMIN) {
            let = ((int) (let/TEXPMIN)) * TEXPMIN;
	}
        return let;
    }

    /**
     * Get the default exposure time
     */
    public double
    getDefaultExpTime()
    {
        double et = getDefaultSkyExpTime();
        et = limitExpTime(et);
        return et;
    }

    /**
     * Get the default flat exposure time
     */
    public double
    getDefaultFlatExpTime()
    {
        double fet = getDefaultSkyExpTime();
        if (getFlatSource().equalsIgnoreCase("mirror")) fet = fet/KSKY;
        fet = limitExpTime(fet);
        return fet;
    }

    /**
     * Get the flat exposure time
     */
    public double
    getFlatExpTime()
    {
      /* Always recompute the default time */
        double fet = getDefaultFlatExpTime();
        setFlatExpTime(fet);
        return fet;
    }

    /**
     * Set the acquisition mode
     */
    public void
    setAcqMode(String acqMode)
    {
        _avTable.set(ATTR_ACQMODE, acqMode);
    }

    /**
     * Get the acquisition mode
     */
    public String
    getAcqMode()
    {
        int ci;
        double expTime = getExpTime();
        String acqMode = null;
        if (expTime < TEXPMIN) {
            ci = 0;
        } else if (expTime < 1.0) {
            ci = 1;
        } else {
            ci = 2;
        }
        String cfs = getChopFreq();
        if (cfs.equals("0.00")) {
            acqMode = (String)ACQMODES.elementAt(ci,1);
        } else {
            acqMode = (String)ACQMODES.elementAt(ci,2);
        }
        System.out.println("getAcqMode> acqMode = " + acqMode);
        setAcqMode(acqMode);
        return acqMode;
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
     * Get the default coadds
     */
    public int getDefaultCoadds() {
        int coadds = 1;
        double et = getExpTime();
        String acqMode = getAcqMode();
        double ot = getObservationTime();
        if ((acqMode.equals("STARE")) || (acqMode.equals("NDSTARE"))) {
	    coadds = (int) Math.round(ot/(et+EXPOSURE_OVERHEAD));
            System.out.println("getDefaultCoadds> (ND)STARE coadds = " + coadds);
	} else {
            String cfs = getChopFreq();
            System.out.println("getDefaultCoadds> chop frequency = " + cfs);
            double cf = Double.valueOf(cfs).doubleValue();
            double cd = getChopDelay();
            int nExpPhase = (int) (((0.5/cf) - cd)/(et + EXPOSURE_OVERHEAD));
            int nCycles = (int) (ot * cf);
            coadds = nExpPhase * nCycles;
            System.out.println("getDefaultCoadds> (ND)CHOP coadds = " + coadds);
	}
        return coadds;
    }

    /**
     * Get the coadds.
     */
    public int getCoadds() {

        int coadds = getStareCapability().getCoadds();
        if ( coadds == 0 ) {
            coadds = getDefaultCoadds();
            setCoadds (coadds);
        }
        return coadds;
    }

    /**
     * Use default acquisition
     */
    public void
    useDefaultAcquisition()
    {
        System.out.println("useDefaultAcquisition> called");
        useDefaultExposureTime();
        useDefaultObservationTime();
        useDefaultChopFreq();
        setCoadds(0);
    }

    /**
     * Set the acquisition
     */
    public void
    setAcquisition()
    {
        /* Setup for normal exposures */
        System.out.println("setAcquistion> called");
        double et = getExpTime();
        System.out.println("setAcquistion> exposure time = " + et);
        String filter = getFilter();
        System.out.println("setAcquistion> filter = " + filter);
        double ot = getObservationTime();
        System.out.println("setAcquistion> observation time = " + ot);
        String cfs = getChopFreq();
        System.out.println("setAcquistion> chop frequency = " + cfs);
        double cf = Double.valueOf(cfs).doubleValue();
        double cd = getChopDelay();
        System.out.println("setAcquistion> chop delay = " + cd);
        String acqMode = getAcqMode();
        System.out.println("setAcquistion> acqMode = " + acqMode);
        String waveplate = getWaveplate();
        System.out.println("setAcquistion> waveplate = " + waveplate);
        double ma = getMaskAngle();
        System.out.println("setAcquistion> maskAngle = " + ma);
        int nr = getNreads();
        System.out.println("setAcquistion> nreads = " + nr);
	int coadds = getCoadds();
        System.out.println("setAcquistion> coadds = " + coadds);
    }



}
