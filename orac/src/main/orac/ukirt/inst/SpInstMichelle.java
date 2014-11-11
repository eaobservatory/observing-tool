/*
 * Copyright 2000-2002 United Kingdom Astronomy Technology Centre, an
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

// This version started 2000 Oct 26 based on Gemini version
// and Alan Bridger's work on the CGS4 instrument
// author: Alan Pickup = dap@roe.ac.uk         2001 Feb

package orac.ukirt.inst;

import java.io.IOException;
import java.io.FileWriter;
import java.util.NoSuchElementException;

import orac.util.LookUpTable;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

import gemini.util.MathUtil;
import gemini.util.Angle;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.obsComp.SpChopCapability;
import gemini.sp.obsComp.SpStareCapability;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterConfigObs;
import orac.ukirt.iter.SpIterBiasObs;
import orac.ukirt.iter.SpIterDarkObs;
import orac.ukirt.iter.SpIterMichelleCalObs;

/**
 * The Michelle instrument.
 *
 * @author Alan Pickup
 */
@SuppressWarnings("serial")
public final class SpInstMichelle extends SpUKIRTInstObsComp {
    // Attributes presented to user
    public static String ATTR_CONFIG_TYPE = "configType";
    public static String ATTR_CAMERA = "camera";
    public static String ATTR_POLARIMETRY = "polarimetry";
    public static String ATTR_MASK = "mask";
    public static String ATTR_MASK_ANGLE = "maskAngle";
    public static String ATTR_DISPERSER = "disperser";
    public static String ATTR_ORDER = "order";
    public static String ATTR_SAMPLING = "sampling";
    public static String ATTR_CENTRAL_WAVELENGTH = "centralWavelength";
    public static String ATTR_FILTER = "filter";
    public static String ATTR_FILTER_CATEGORY = "filterCategory";
    public static String ATTR_FILTER_OT = "filterOT";
    public static String ATTR_SCIENCE_AREA = "scienceArea";
    public static String ATTR_SPECTRAL_COVERAGE = "spectralCoverage";
    public static String ATTR_PIXEL_FOV = "pixelFOV";
    public static String ATTR_DARKFILTER = "darkFilter";
    public static String ATTR_WAVEPLATE = "waveplate";
    public static String ATTR_FLAT_SAMPLING = "flatSampling";
    public static String ATTR_MODE = "mode";
    public static String ATTR_CHOP_FREQUENCY = "chopFrequency";
    public static String ATTR_CHOP_DELAY = "chopDelay";
    public static String ATTR_READ_INTERVAL = "readInterval";
    public static String ATTR_NRESETS = "nresets";
    public static String ATTR_NREADS = "nreads";
    public static String ATTR_WAVEFORM = "waveform";
    public static String ATTR_RESET_DELAY = "resetDelay";
    public static String ATTR_IDLE_PERIOD = "idlePeriod";
    public static String ATTR_MUST_IDLES = "mustIdles";
    public static String ATTR_NULL_CYCLES = "nullCycles";
    public static String ATTR_NULL_EXPOSURES = "nullExposures";
    public static String ATTR_NULL_READS = "nullReads";
    public static String ATTR_DUTY_CYCLE = "dutyCycle";
    public static String ATTR_OBSERVATION_TIME = "observationTime";
    public static String ATTR_OBSTIME_OT = "obsTimeOT";
    public static String NO_VALUE = "none";
    public static final int CAMERA_IMAGING = 0;
    public static final int CAMERA_SPECTROSCOPY = 1;

    // Class variables representing defaults, LUTs, etc
    public static String CONFIG_TYPE;
    public static String VERSION;
    public static String[] INSTRUMENT_APER_IM_POL;
    public static String[] DETECTOR_SIZE;
    public static int DETECTOR_WIDTH;
    public static int DETECTOR_HEIGHT;
    public static double DETANGLE;
    public static double PIXPITCH;
    public static String DARKFILTER;
    public static String DEFAULT_FILTER;
    public static String DEFAULT_FILTER_CAT_TARGET_ACQ;
    public static String DEFAULT_FILTER_TARGET_ACQ;
    public static int DEFAULT_COADDS_TARGET_ACQ;
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
    public static String DEFAULT_SAMPLING_TARGET_ACQ;
    public static LookUpTable DISPERSERS;
    public static String DEFAULT_DISPERSER;
    public static int CENTRAL_ROW;
    public static int PEAK_ROW;
    public static LookUpTable MASKS;
    public static String DEFAULT_MASK;

    // Disperser 1
    public static LookUpTable ORDERS1;
    public static LookUpTable BLOCKERS1;

    // Disperser 2
    public static LookUpTable ORDERS2;
    public static LookUpTable BLOCKERS2;

    // Disperser 3
    public static LookUpTable ORDERS3;
    public static LookUpTable BLOCKERS3;

    // Disperser 4
    public static LookUpTable ORDERS4;
    public static LookUpTable BLOCKERS4;

    // Disperser 5
    public static LookUpTable ORDERS5;
    public static LookUpTable BLOCKERS5;

    // Data acquisition - general
    public static LookUpTable CHOPS;
    public static String DEFAULT_CHOPFREQ;
    public static double KPOL;
    public static double KSKY;
    public static double KIM;
    public static double KBAND;
    public static double KWELL;
    public static double TEXPBASE;
    public static double TEXPMIN;
    public static double TEXPMAX;
    public static double DEFAULT_TOBS;
    public static String DEFAULT_MODE;
    public static LookUpTable EXPTIMINGS;
    public static double DEFAULT_EXPTIME;
    public static double EXPOSURE_OVERHEAD = 0.01;
    public static double READ_INTERVAL;
    public static int NULL_READS;
    public static double RESET_DELAY;
    public static LookUpTable DACONFS;
    public static LookUpTable MODES;
    public static LookUpTable WAVEFORMS;

    // Data acquisition - bias
    public static double DEFBIASEXPTIME;
    public static int DEFBIASCOADDS;

    // Instance variables of the class
    double flatObservationTime;
    double arcObservationTime;
    double targetAcqObservationTime;
    double flatExposureTime;
    double arcExposureTime;
    double targetAcqExposureTime;

    // Data acquisition - dark
    // Data acquisition - flat & arcs
    String flat_source;
    public String W_mode;
    public String W_waveform;
    public int W_nreads;
    public int W_nresets;
    public double W_resetDelay;
    public double W_readInterval;
    public double W_idlePeriod;
    public int W_mustIdles;
    public int W_nullCycles;
    public int W_nullExposures;
    public int W_nullReads;
    public String W_chopFrequency;
    public double W_chopDelay;
    public int W_coadds;
    public double W_dutyCycle;
    public double W_obsTime;

    public static final SpType SP_TYPE = SpType.create(
            SpType.OBSERVATION_COMPONENT_TYPE, "inst.michelle", "Michelle");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpInstMichelle());
    }

    // Constructor reads instrument .cfg file and initialises values
    public SpInstMichelle() {
        super(SP_TYPE);

        addCapability(new SpChopCapability());
        addCapability(new SpStareCapability());

        // Read in the instrument config file
        String baseDir = System.getProperty("ot.cfgdir");

        if (!baseDir.endsWith("/")) {
            baseDir += '/';
        }

        String cfgFile = baseDir + "michelle.cfg";
        _readCfgFile(cfgFile);

        // Set the initial values of the attributes
        String attr = ATTR_CONFIG_TYPE;
        String value = CONFIG_TYPE;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_VERSION;
        value = VERSION;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_CAMERA;
        value = DEFAULT_CAMERA;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_MASK;
        value = DEFAULT_MASK;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_MASK_ANGLE;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr = ATTR_POS_ANGLE;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr = ATTR_DISPERSER;
        value = DEFAULT_DISPERSER;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_ORDER;
        _avTable.noNotifySet(attr, "0", 0);

        attr = ATTR_CENTRAL_WAVELENGTH;
        _avTable.noNotifySet(attr, "10.0", 0);

        attr = ATTR_POLARIMETRY;
        value = DEFAULT_POLARIMETRY;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_FILTER;
        value = DEFAULT_FILTER;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_DARKFILTER;
        value = DARKFILTER;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_SAMPLING;
        value = DEFAULT_SAMPLING;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_SCIENCE_AREA;
        _avTable.noNotifySet(attr, "0.0 x 0.0", 0);

        attr = ATTR_PIXEL_FOV;
        _avTable.noNotifySet(attr, "0.0 x 0.0", 0);

        attr = ATTR_MODE;
        value = DEFAULT_MODE;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_CHOP_FREQUENCY;
        value = DEFAULT_CHOPFREQ;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_CHOP_DELAY;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr = ATTR_EXPOSURE_TIME;
        value = Double.toString(DEFAULT_EXPTIME);
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_READ_INTERVAL;
        value = Double.toString(READ_INTERVAL);
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_NREADS;
        _avTable.noNotifySet(attr, "0", 0);

        attr = ATTR_NRESETS;
        _avTable.noNotifySet(attr, "0", 0);

        attr = ATTR_IDLE_PERIOD;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr = ATTR_MUST_IDLES;
        _avTable.noNotifySet(attr, "0", 0);

        attr = ATTR_RESET_DELAY;
        value = Double.toString(RESET_DELAY);
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_NULL_CYCLES;
        _avTable.noNotifySet(attr, "0", 0);

        attr = ATTR_NULL_EXPOSURES;
        _avTable.noNotifySet(attr, "0", 0);

        attr = ATTR_NULL_READS;
        _avTable.noNotifySet(attr, "0", 0);

        attr = ATTR_WAVEFORM;
        _avTable.noNotifySet(attr, "unspecified", 0);

        attr = ATTR_COADDS;
        _avTable.noNotifySet(attr, "1", 0);

        attr = ATTR_DUTY_CYCLE;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr = ATTR_OBSERVATION_TIME;
        value = Double.toString(DEFAULT_TOBS);
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_OBSTIME_OT;
        value = Double.toString(DEFAULT_TOBS);
        _avTable.noNotifySet(attr, value, 0);

        // Initialise instance variables
        initInstance();
    }

    private void _readCfgFile(String filename) {
        InstCfgReader instCfg = null;
        InstCfg instInfo = null;
        String block = null;
        int i;
        instCfg = new InstCfgReader(filename);

        try {
            while ((block = instCfg.readBlock()) != null) {
                instInfo = new InstCfg(block);
                if (InstCfg.matchAttr(instInfo, "instrument_port")) {
                    INSTRUMENT_PORT = instInfo.getValue();
                    setPort(INSTRUMENT_PORT);

                } else if (InstCfg.matchAttr(instInfo, "config_type")) {
                    CONFIG_TYPE = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "version")) {
                    VERSION = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "instrument_aper")) {
                    INSTRUMENT_APER = instInfo.getValueAsArray();

                } else if (InstCfg.matchAttr(instInfo,
                        "instrument_aper_im_pol")) {
                    INSTRUMENT_APER_IM_POL = instInfo.getValueAsArray();

                } else if (InstCfg.matchAttr(instInfo, "detector_size")) {
                    DETECTOR_SIZE = instInfo.getValueAsArray();
                    DETECTOR_WIDTH = Integer.parseInt(DETECTOR_SIZE[0]);
                    DETECTOR_HEIGHT = Integer.parseInt(DETECTOR_SIZE[1]);

                } else if (InstCfg.matchAttr(instInfo,
                        "imaging_field_of_view")) {
                    String starray[] = instInfo.getValueAsArray();
                    IMAGING_FIELD_OF_VIEW = new double[starray.length];

                    for (i = 0; i < starray.length; i++) {
                        IMAGING_FIELD_OF_VIEW[i] = Double.valueOf(starray[i]);
                    }

                } else if (InstCfg.matchAttr(instInfo, "detangle")) {
                    DETANGLE = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "pixpitch")) {
                    PIXPITCH = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "darkfilter")) {
                    DARKFILTER = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "default_filter")) {
                    DEFAULT_FILTER = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "default_filter_cat_target_acq")) {
                    DEFAULT_FILTER_CAT_TARGET_ACQ = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "default_filter_target_acq")) {
                    DEFAULT_FILTER_TARGET_ACQ = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "default_coadds_target_acq")) {
                    DEFAULT_COADDS_TARGET_ACQ =
                            Integer.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "cameras")) {
                    CAMERAS = instInfo.getValueAsArray();

                } else if (InstCfg.matchAttr(instInfo, "n_to_q_boundary")) {
                    N_TO_Q_BOUNDARY = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "default_camera")) {
                    DEFAULT_CAMERA = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "polarimetry")) {
                    POLARIMETRY = instInfo.getValueAsArray();

                } else if (InstCfg.matchAttr(instInfo, "default_polarimetry")) {
                    DEFAULT_POLARIMETRY = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "waveplates")) {
                    WAVEPLATES = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "imaging_pixel_scale")) {
                    IMAGING_PIXEL_SCALE = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "nbandfilt")) {
                    NBANDFILT = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "qbandfilt")) {
                    QBANDFILT = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "specfilt")) {
                    SPECFILT = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "filters")) {
                    FILTERS = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "spect_pixel_scale")) {
                    SPECT_PIXEL_SCALE = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "spect_focal_length")) {
                    SPECT_FOCAL_LENGTH = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "flat_sources")) {
                    FLAT_SOURCES = instInfo.getValueAsArray();

                } else if (InstCfg.matchAttr(instInfo, "samplings")) {
                    SAMPLINGS = instInfo.getValueAsArray();

                } else if (InstCfg.matchAttr(instInfo, "default_sampling")) {
                    DEFAULT_SAMPLING = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "default_sampling_target_acq")) {
                    DEFAULT_SAMPLING_TARGET_ACQ = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "default_flat_source")) {
                    DEFAULT_FLAT_SOURCE = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "default_flat_sampling")) {
                    DEFAULT_FLAT_SAMPLING = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "orders1")) {
                    ORDERS1 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "blockers1")) {
                    BLOCKERS1 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "orders2")) {
                    ORDERS2 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "blockers2")) {
                    BLOCKERS2 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "orders3")) {
                    ORDERS3 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "blockers3")) {
                    BLOCKERS3 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "orders4")) {
                    ORDERS4 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "blockers4")) {
                    BLOCKERS4 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "orders5")) {
                    ORDERS5 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "blockers5")) {
                    BLOCKERS5 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "dispersers")) {
                    DISPERSERS = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "default_disperser")) {
                    DEFAULT_DISPERSER = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "central_row")) {
                    CENTRAL_ROW = Integer.parseInt(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "peak_row")) {
                    PEAK_ROW = Integer.parseInt(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "masks")) {
                    MASKS = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "default_mask")) {
                    DEFAULT_MASK = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "chops")) {
                    CHOPS = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "default_chopfreq")) {
                    DEFAULT_CHOPFREQ = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "kpol")) {
                    KPOL = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "ksky")) {
                    KSKY = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "kim")) {
                    KIM = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "kband")) {
                    KBAND = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "kwell")) {
                    KWELL = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "default_mode")) {
                    DEFAULT_MODE = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "exptimings")) {
                    EXPTIMINGS = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "default_exptime")) {
                    DEFAULT_EXPTIME = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "texpbase")) {
                    TEXPBASE = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "texpmin")) {
                    TEXPMIN = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "texpmax")) {
                    TEXPMAX = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "default_tobs")) {
                    DEFAULT_TOBS = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "defbiasexptime")) {
                    DEFBIASEXPTIME = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "defbiascoadds")) {
                    DEFBIASCOADDS = Integer.parseInt(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "read_interval")) {
                    READ_INTERVAL = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "null_reads")) {
                    NULL_READS = Integer.parseInt(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "reset_delay")) {
                    RESET_DELAY = Double.valueOf(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "daconfs")) {
                    DACONFS = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "modes")) {
                    MODES = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "waveforms")) {
                    WAVEFORMS = instInfo.getValueAsLUT();

                } else {
                    System.out.println("Unmatched keyword:"
                            + instInfo.getKeyword());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Michelle inst. cfg file");
        }
    }

    /**
     * Get the chop capability.
     */
    public SpChopCapability getChopCapability() {
        return (SpChopCapability)
                getCapability(SpChopCapability.CAPABILITY_NAME);
    }

    /**
     * Get the stare capability.
     */
    public SpStareCapability getStareCapability() {
        return (SpStareCapability)
                getCapability(SpStareCapability.CAPABILITY_NAME);
    }

    /**
     * Set the pixel field of view
     */
    public void setPixelFOV(String pixelFOV) {
        _avTable.set(ATTR_PIXEL_FOV, pixelFOV);
    }

    /**
     * Return the pixel field of view
     */
    public double[] getPixelFOV() {
        return getPixelFOV("OBJECT");
    }

    /**
     * Return the pixel field of view
     */
    public double[] getPixelFOV(String obsType) {
        double pfov[] = new double[2];

        if (isImaging() || obsType.equalsIgnoreCase("TARGETACQ")) {
            pfov[0] = IMAGING_PIXEL_SCALE;
            pfov[1] = IMAGING_PIXEL_SCALE;

        } else {
            double ma = getMaskAngle() * Math.PI / 180.0;
            pfov[0] = SPECT_PIXEL_SCALE / Math.cos(ma);
            pfov[1] = SPECT_PIXEL_SCALE * Math.cos(ma);
        }

        return pfov;
    }

    /**
     * Get the pixel field of view as a string
     */
    public String getPixelFOVString() {
        return getPixelFOVString("OBJECT");
    }

    /**
     * Get the pixel field of view as a string
     */
    public String getPixelFOVString(String obsType) {
        double pfov[];
        pfov = getPixelFOV(obsType);
        double w = MathUtil.round(pfov[0], 2);
        double h = MathUtil.round(pfov[1], 2);
        String pfovs = w + " x " + h;

        if (obsType.equalsIgnoreCase("OBJECT")) {
            setPixelFOV(pfovs);
        }

        return pfovs;
    }

    /**
     * Set the science area
     */
    public void setScienceArea(String scienceArea) {
        _avTable.set(ATTR_SCIENCE_AREA, scienceArea);
    }

    /**
     * Return the science area based upon the current camera.
     */
    public double[] getScienceArea() {
        return getScienceArea("OBJECT");
    }

    /**
     * Return the science area based upon the current camera.
     */
    public double[] getScienceArea(String obsType) {
        double fov[] = new double[2];

        if (isImaging() || obsType.equalsIgnoreCase("TARGETACQ")) {
            fov[0] = IMAGING_FIELD_OF_VIEW[0];
            fov[1] = IMAGING_FIELD_OF_VIEW[1];

        } else {
            double w = getMaskWidthPixels();
            double h = getMaskHeightPixels();
            double ma = getMaskAngle() * Math.PI / 180.0;
            fov[0] = SPECT_PIXEL_SCALE * w / Math.cos(ma);
            fov[1] = SPECT_PIXEL_SCALE * h * Math.cos(ma);
        }

        return fov;
    }

    /**
     * Get the science area as a string (also updates pixel field of view)
     */
    public String getScienceAreaString() {
        return getScienceAreaString("OBJECT");
    }

    /**
     * Get the science area as a string (also updates pixel field of view)
     */
    public String getScienceAreaString(String obsType) {
        double fov[];
        fov = getScienceArea(obsType);
        double w = MathUtil.round(fov[0], 2);
        double h = MathUtil.round(fov[1], 2);
        String fovs = w + " x " + h;

        if (obsType.equalsIgnoreCase("OBJECT")) {
            setScienceArea(fovs);
        }

        /* Update the pixel field of view too */
        return fovs;
    }

    /**
     * Is the instrument in "imaging" (in other words, is the "imaging"
     * camera selected instead of the spectroscopy camera).
     */
    public boolean isImaging() {
        String camera = getCamera();
        return (camera.equalsIgnoreCase("imaging"));
    }

    /**
     * Set the camera.
     */
    public void setCamera(String camera) {
        _avTable.set(ATTR_CAMERA, camera);
        setInstAper();
    }

    /**
     * Initialise instance variables
     */
    public void initInstance() {
        // Initialise instance variables and initial config
        flat_source = null;
        flatObservationTime = DEFAULT_TOBS;
        arcObservationTime = DEFAULT_TOBS;
        targetAcqObservationTime = DEFAULT_TOBS;
        useDefaultAcquisition();
        setAcquisition();
    }

    /**
     * Get the camera.
     */
    public String getCamera() {
        String camera = _avTable.get(ATTR_CAMERA);

        if (camera == null) {
            camera = DEFAULT_CAMERA;
            setCamera(camera);
        }

        return camera;
    }

    /**
     * Get the list of dispersers
     */
    public String[] getDisperserList() {
        String disperserList[] = new String[DISPERSERS.getNumRows()];

        for (int i = 0; i < DISPERSERS.getNumRows(); i++) {
            disperserList[i] = DISPERSERS.elementAt(i, 0);
        }

        return disperserList;
    }

    /**
     * Set the disperser name
     */
    public void setDisperser(String disperser) {
        _avTable.set(ATTR_DISPERSER, disperser);
        useDefaultCentralWavelength();
        setInstAper();
    }

    /**
     * Get the disperser name
     */
    public String getDisperser() {
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
    public int getDisperserIndex() {
        String disperser = getDisperser();
        int dispindex = 0;

        try {
            dispindex = DISPERSERS.indexInColumn(disperser, 0);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Failed to find disperser index!");
        } catch (NoSuchElementException ex) {
            System.out.println("Failed to find disperser index!");
        }

        return dispindex;
    }

    /**
     * Use default Central Wavelength.
     */
    public void useDefaultCentralWavelength() {
        _avTable.rm(ATTR_CENTRAL_WAVELENGTH);
    }

    /**
     * Set the central wavelength.
     */
    public void setCentralWavelength(double cwl) {
        _avTable.set(ATTR_CENTRAL_WAVELENGTH, cwl);

        if (!isImaging()) {
            useDefaultOrder();
            useDefaultFilterOT();
        }
    }

    /**
     * Set the central wavelength as a string.
     */
    public void setCentralWavelength(String cwl) {
        double d = 0.0;

        try {
            d = Double.valueOf(cwl);
        } catch (Exception ex) {
        }

        setCentralWavelength(d);
    }

    /**
     * Get the default central wavelength using the current disperser.
     */
    public double getDefaultCentralWavelength() {
        int di = getDisperserIndex();

        try {
            return Double.valueOf(DISPERSERS.elementAt(di, 3));
        } catch (NumberFormatException e) {
        }

        return 0.0;
    }

    /**
     * Get the central wavelength.
     */
    public double getCentralWavelength() {
        return getCentralWavelength("OBJECT");
    }

    /**
     * Get the central wavelength.
     */
    public double getCentralWavelength(String obsType) {
        double cwl;

        if (isImaging() || obsType.equalsIgnoreCase("TARGETACQ")) {
            /* Get centre wavelength of filter */
            int fi = getFilterIndex(obsType);
            cwl = Double.valueOf(getFilterLUT().elementAt(fi, 3));

            if (isImaging()) {
                setCentralWavelength(cwl);
            }
        } else {
            cwl = _avTable.getDouble(ATTR_CENTRAL_WAVELENGTH, 0.0);

            if (cwl == 0.0) {
                cwl = getDefaultCentralWavelength();
                setCentralWavelength(cwl);
            }
        }

        return cwl;
    }

    /**
     * Get the central wavelength as a string
     */
    public String getCentralWavelengthString() {
        return getCentralWavelengthString("OBJECT");
    }

    /**
     * Get the central wavelength as a string
     */
    public String getCentralWavelengthString(String obsType) {
        double cwl = MathUtil.round(getCentralWavelength(obsType), 3);
        String cwls = Double.toString(cwl);
        return cwls;
    }

    /**
     * Get the list of masks
     */
    public String[] getMaskList() {
        String maskList[] = new String[MASKS.getNumRows()];

        for (int i = 0; i < MASKS.getNumRows(); i++) {
            maskList[i] = MASKS.elementAt(i, 0);
        }

        return maskList;
    }

    /**
     * Set the mask.
     */
    public void setMask(String mask) {
        _avTable.set(ATTR_MASK, mask);
    }

    /**
     * Get the mask.
     */
    public String getMask() {
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
    public int getMaskIndex() {
        int mask;

        try {
            mask = MASKS.indexInColumn(getMask(), 0);
        } catch (Exception ex) {
            mask = 0;
        }

        return mask;
    }

    /**
     * Get the width (pixels) of the current mask.
     */
    public double getMaskWidthPixels() {
        double maskWidthPixels = 0.0;

        try {
            maskWidthPixels = new Double(MASKS.elementAt(getMaskIndex(), 1));
        } catch (IndexOutOfBoundsException e) {
        } catch (NumberFormatException e) {
        }

        return maskWidthPixels;
    }

    /**
     * Get the height (pixels) of the current mask.
     */
    public double getMaskHeightPixels() {
        double maskHeightPixels = 0.0;

        try {
            maskHeightPixels = new Double(MASKS.elementAt(getMaskIndex(), 2));
        } catch (IndexOutOfBoundsException e) {
        } catch (NumberFormatException e) {
        }

        return maskHeightPixels;
    }

    /**
     * Override the set-position-angle methods so that we can ensure
     * it's set to zero: stops dragging of the angle in tpe.
     *
     * Set the position angle in degrees from due north, updating the
     * observation data with the new position angle.  This method is
     * ultimately called by the other setPosAngle methods.
     */
    public void setPosAngleDegrees(double posAngle) {
        if (isImaging()) {
            if (posAngle != 0.0) {
                super.setPosAngleDegrees(0.0);
            }
        } else {
            super.setPosAngleDegrees(posAngle);
        }
    }

    /**
     * Set the position angle in radians from due north.
     */
    public void setPosAngleRadians(double posAngle) {
        this.setPosAngleDegrees(Angle.radiansToDegrees(posAngle));
    }

    /**
     * Set the rotation of the science area as a string (representing degrees).
     */
    public void setPosAngleDegreesStr(String posAngleStr) {
        double posAngle = 0.0;

        try {
            posAngle = Double.valueOf(posAngleStr);
        } catch (NumberFormatException e) {
            System.out.println("Error converting string angle to double.");
        }

        this.setPosAngleDegrees(posAngle);
    }

    /**
     * Get the list of filter categories
     */
    public String[] getFilterCategoryList() {
        String filterCategoryList[] = new String[FILTERS.getNumRows()];

        for (int i = 0; i < FILTERS.getNumRows(); i++) {
            filterCategoryList[i] = FILTERS.elementAt(i, 0);
        }

        return filterCategoryList;
    }

    /**
     * Set the filter category
     */
    public void setFilterCategory(String filterCategory) {
        _avTable.set(ATTR_FILTER_CATEGORY, filterCategory);
        useDefaultFilterOT();
    }

    /**
     * Forget the OT name of the filter category
     */
    public void useDefaultFilterCategory() {
        _avTable.rm(ATTR_FILTER_CATEGORY);
        useDefaultFilterOT();
    }

    /**
     * Get the default filter category
     */
    public String getDefaultFilterCategory() {
        String filterCategory = FILTERS.elementAt(0, 0);
        return filterCategory;
    }

    /**
     * Get the filter category
     */
    public String getFilterCategory() {
        String filterCategory = _avTable.get(ATTR_FILTER_CATEGORY);

        if (filterCategory == null) {
            filterCategory = getDefaultFilterCategory();
            setFilterCategory(filterCategory);
        }

        return filterCategory;
    }

    /**
     * Get the filter category index
     */
    public int getFilterCategoryIndex() {
        String filterCategory = getFilterCategory();
        int fcIndex;

        try {
            fcIndex = FILTERS.indexInColumn(filterCategory, 0);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Failed to find filter category index");
            fcIndex = 0;
        } catch (NoSuchElementException ex) {
            System.out.println("Failed to find filter index");
            fcIndex = 0;
        }

        return fcIndex;
    }

    /**
     * Get the lookup table for the current filter category
     */
    public LookUpTable getFilterLUT() {
        int fcIndex = getFilterCategoryIndex();
        LookUpTable flut = null;

        if (fcIndex == FILTERS.indexInColumn("NBANDFILT", 1)) {
            flut = NBANDFILT;
        } else if (fcIndex == FILTERS.indexInColumn("QBANDFILT", 1)) {
            flut = QBANDFILT;
        } else if (fcIndex == FILTERS.indexInColumn("SPECFILT", 1)) {
            flut = SPECFILT;
        }

        return flut;
    }

    /**
     * Get the list of filters for the current filter category
     */
    public String[] getFilterList() {
        LookUpTable flut = getFilterLUT();
        String filterList[] = new String[flut.getNumRows()];

        for (int i = 0; i < flut.getNumRows(); i++) {
            filterList[i] = flut.elementAt(i, 0);
        }

        return filterList;
    }

    /**
     * Forget the OT name of the filter OT
     */
    public void useDefaultFilterOT() {
        _avTable.rm(ATTR_FILTER_OT);
    }

    /**
     * Set the OT name of the filter.
     */
    public void setFilterOT(String filterOT) {
        _avTable.set(ATTR_FILTER_OT, filterOT);
    }

    /**
     * Get the default filter OT
     */
    public String getDefaultFilterOT() {
        LookUpTable flut = getFilterLUT();
        String filterOT = flut.elementAt(0, 0);

        return filterOT;
    }

    /**
     * Get the OT name of the filter.
     */
    public String getFilterOT() {
        String filterOT = _avTable.get(ATTR_FILTER_OT);

        if (filterOT == null) {
            filterOT = getDefaultFilterOT();
            setFilterOT(filterOT);
        }

        return filterOT;
    }

    /**
     * Get the index of filter name in filter category
     */
    public int getFilterIndex() {
        return getFilterIndex("OBJECT");
    }

    /**
     * Get the index of filter name in filter category
     */
    public int getFilterIndex(String obsType) {
        LookUpTable flut = getFilterLUT();
        String filter = null;

        if (obsType.equalsIgnoreCase("TARGETACQ")) {
            filter = DEFAULT_FILTER_TARGET_ACQ;
        } else {
            filter = getFilterOT();
        }

        int fIndex = flut.indexInColumn(filter, 0);
        return fIndex;
    }

    /**
     * Set the filter.
     */
    public void setFilter(String filter) {
        _avTable.set(ATTR_FILTER, filter);
    }

    /**
     * Get the filter
     */
    public String getFilter() {
        return getFilter("OBJECT");
    }

    /**
     * Get the filter
     */
    public String getFilter(String obsType) {
        int ci = 1;
        String filter = null;

        if (isImaging()) {
            /* ci is the column index in the filter category lut */
            if (isPolarimetry()) {
                ci = 2;
            }

            filter = getFilterLUT().elementAt(getFilterIndex(obsType), ci);

        } else if (obsType.equalsIgnoreCase("TARGETACQ")) {
            return DEFAULT_FILTER_TARGET_ACQ;

        } else {
            int di = getDisperserIndex();
            double cwl = getCentralWavelength();

            /* ci is the column index in the BLOCKERS lut */
            if (isPolarimetry()) {
                ci = 2;
            }

            try {
                LookUpTable blut = null;

                if (di == DISPERSERS.indexInColumn("BLOCKERS1", 5)) {
                    blut = BLOCKERS1;
                } else if (di == DISPERSERS.indexInColumn("BLOCKERS2", 5)) {
                    blut = BLOCKERS2;
                } else if (di == DISPERSERS.indexInColumn("BLOCKERS3", 5)) {
                    blut = BLOCKERS3;
                } else if (di == DISPERSERS.indexInColumn("BLOCKERS4", 5)) {
                    blut = BLOCKERS4;
                } else if (di == DISPERSERS.indexInColumn("BLOCKERS5", 5)) {
                    blut = BLOCKERS5;
                }

                int pos = blut.rangeInColumn(cwl, 0);
                filter = blut.elementAt(pos, ci);
            } catch (Exception ex) {
                System.out.println("Error getting blocking filter");
                System.out.println("For disperser " + di
                        + " and lambda " + cwl);
            }
        }

        setFilter(filter);
        return filter;
    }

    /**
     * Get the name of the filter to be used for darks
     */
    public String getDarkFilter() {
        return DARKFILTER;
    }

    /**
     * Get the arc blocking filter for the current spectroscopy configuration
     */
    public String getArcFilter() {
        String arcFilter = "undefined";

        if (!isImaging()) {
            int di = getDisperserIndex();
            double cwl = getCentralWavelength();

            try {
                LookUpTable blut = null;

                if (di == DISPERSERS.indexInColumn("BLOCKERS1", 5)) {
                    blut = BLOCKERS1;
                } else if (di == DISPERSERS.indexInColumn("BLOCKERS2", 5)) {
                    blut = BLOCKERS2;
                } else if (di == DISPERSERS.indexInColumn("BLOCKERS3", 5)) {
                    blut = BLOCKERS3;
                } else if (di == DISPERSERS.indexInColumn("BLOCKERS4", 5)) {
                    blut = BLOCKERS4;
                } else if (di == DISPERSERS.indexInColumn("BLOCKERS5", 5)) {
                    blut = BLOCKERS5;
                }

                int pos = blut.rangeInColumn(cwl, 0);
                arcFilter = blut.elementAt(pos, 3);
            } catch (Exception ex) {
                System.out.println("Error getting blocking filter for arc obs");
                System.out.println("For disperser " + di
                        + " and lambda " + cwl);
            }
        }

        return arcFilter;
    }

    /**
     * Get the mode.
     */
    public String getMode() {
        String mode = _avTable.get(ATTR_MODE);

        if (mode == null) {
            mode = DEFAULT_MODE;
        }

        return mode;
    }

    /**
     * Use default order
     */
    public void useDefaultOrder() {
        _avTable.rm(ATTR_ORDER);
    }

    /**
     * Set the order
     */
    public void setOrder(int order) {
        _avTable.set(ATTR_ORDER, order);
    }

    /**
     * Get the default order
     */
    public int getDefaultOrder() {
        int order = 0;

        if (isImaging()) {
            return 0;
        }

        int di = getDisperserIndex();
        double cwl = getCentralWavelength();

        try {
            LookUpTable dolut = null;

            if (di == DISPERSERS.indexInColumn("ORDERS1", 4)) {
                dolut = ORDERS1;
            } else if (di == DISPERSERS.indexInColumn("ORDERS2", 4)) {
                dolut = ORDERS2;
            } else if (di == DISPERSERS.indexInColumn("ORDERS3", 4)) {
                dolut = ORDERS3;
            } else if (di == DISPERSERS.indexInColumn("ORDERS4", 4)) {
                dolut = ORDERS4;
            } else if (di == DISPERSERS.indexInColumn("ORDERS5", 4)) {
                dolut = ORDERS5;
            }

            int pos = dolut.rangeInColumn(cwl, 0);
            order = Integer.parseInt(dolut.elementAt(pos, 1));
        } catch (Exception ex) {
            System.out.println("Error getting default order");
            System.out.println("For disperser " + di + " and lambda " + cwl);
            order = 0;
        }

        return order;
    }

    /**
     * Get the order
     */
    public int getOrder() {
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
    public String getOrderString() {
        return Integer.toString(getOrder());
    }

    /**
     * Set polarimetry to yes or no
     */
    public void setPolarimetry(String polarimetry) {
        _avTable.set(ATTR_POLARIMETRY, polarimetry);
    }

    /**
     * Get polarimetry as yes or no
     */
    public String getPolarimetry() {
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
    public boolean isPolarimetry() {
        return (getPolarimetry().equalsIgnoreCase("yes"));
    }

    /**
     * Set the pixel sampling
     */
    public void setPixelSampling(String pixelSampling) {
        /* Possibly check that requested sampling is valid */
        _avTable.set(ATTR_SAMPLING, pixelSampling);
    }

    /**
     * Get the pixel sampling
     */
    public String getPixelSampling() {
        if (isImaging()) {
            return "1x1";
        }

        String sampling = _avTable.get(ATTR_SAMPLING);
        return sampling;
    }

    /**
     * Get the disperser rule value
     */
    public double getRule() {
        int di = getDisperserIndex();
        double rule = Double.valueOf(DISPERSERS.elementAt(di, 1));
        return rule;
    }

    /**
     * Get the angle of incidence
     */
    public double getAngleOfIncidence() {
        if (isImaging()) {
            return 0;
        }

        int n = getOrder();
        double cwl = getCentralWavelength();
        double rule = getRule();
        double aoi = Math.asin((n * cwl) / (2.0 * rule));
        return aoi;
    }

    /**
     * Set the spectral coverage
     */
    public void setSpectralCoverage(String spectralCoverage) {
        _avTable.set(ATTR_SPECTRAL_COVERAGE, spectralCoverage);
    }

    /**
     * Get the spectral coverage
     */
    public double[] getSpectralCoverage() {
        double scov[] = new double[2];

        if (isImaging()) {
            scov[0] = 0;
            scov[1] = 0;
        } else {
            double a = getAngleOfIncidence();
            double rule = getRule();
            int n = getOrder();
            double range = (rule / n)
                    * (Math.sin(a + DETANGLE) - Math.sin(a - DETANGLE));
            double cwl = getCentralWavelength();
            scov[0] = cwl - range / 2.0;
            scov[1] = cwl + range / 2.0;
        }

        return scov;
    }

    /**
     * Get the spectral coverage as a string
     */
    public String getSpectralCoverageString() {
        double scov[];
        String scovs = null;
        scov = getSpectralCoverage();

        if ((scov[0] != 0.0) && (scov[1] != 0.0)) {
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
    public double getDispersion() {
        if (isImaging()) {
            return 0;
        }

        double aoi = getAngleOfIncidence();
        double cwl = getCentralWavelength();
        double disp = 2.0 * (Math.tan(aoi)) / cwl;
        return disp;
    }

    /**
     * Set the mask angle in degrees
     */
    public void setMaskAngle(double maskAngle) {
        // Trim the number of decimal places...
        double ma = MathUtil.round(maskAngle, 4);
        _avTable.set(ATTR_MASK_ANGLE, ma);
    }

    /**
     * Get the mask angle in degrees
     */
    public double getMaskAngle() {
        if (isImaging()) {
            return 0;
        }

        double ma = Math.atan(0.611 * Math.tan(getAngleOfIncidence()));
        // Convert to degrees
        ma = ma * 180.0 / Math.PI;
        setMaskAngle(ma);

        return ma;
    }

    /**
     * Get the resolving power
     */
    public double getResolvingPower() {
        return getResolvingPower("OBJECT");
    }

    /**
     * Get the resolving power
     */
    public double getResolvingPower(String obsType) {
        double rp;

        if (isImaging() || obsType.equalsIgnoreCase("TARGETACQ")) {
            /* For imaging, this is 1/bandpass for current filter */
            rp = 1.0 / Double.valueOf(getFilterLUT().elementAt(
                    getFilterIndex(obsType), 4));
        } else {
            double disp = getDispersion();
            double cwl = getCentralWavelength();
            double w = getMaskWidthPixels();
            double ma = getMaskAngle() * Math.PI / 180.0;
            rp = (disp * cwl * SPECT_FOCAL_LENGTH * Math.cos(ma))
                    / (w * PIXPITCH);

            /* Resolving power clipped at 20,000 */
            if (rp > 20000.0) {
                rp = 20000.0;
            }
        }

        return rp;
    }

    /**
     * Get the resolving power as a string
     */
    public String getResolvingPowerString() {
        return getResolvingPowerString("OBJECT");
    }

    /**
     * Get the resolving power as a string
     */
    public String getResolvingPowerString(String obsType) {
        double rp = MathUtil.round(getResolvingPower(obsType), 1);
        String rps = Double.toString(rp);

        return rps;
    }

    /**
     * Set the waveplate
     */
    public void setWaveplate(String waveplate) {
        _avTable.set(ATTR_WAVEPLATE, waveplate);
    }

    /**
     * Get the waveplate
     */
    public String getWaveplate() {
        return getWaveplate("OBJECT");
    }

    /**
     * Get the waveplate
     */
    public String getWaveplate(String obsType) {
        String waveplate = "none";

        if (isPolarimetry()) {
            double cwl;

            /* Get centre wavelength of filter */
            if (isImaging()) {
                cwl = Double.valueOf(getFilterLUT().elementAt(
                        getFilterIndex(obsType), 3));
            } else {
                cwl = getCentralWavelength(obsType);
            }

            try {
                int pos = WAVEPLATES.rangeInColumn(cwl, 0);
                waveplate = WAVEPLATES.elementAt(pos, 1);
            } catch (Exception ex) {
                System.out.println("Error getting waveplate");
            }
        }

        setWaveplate(waveplate);
        return waveplate;
    }

    /**
     * Set the flat source
     */
    public void setFlatSource(String flatSource) {
        flat_source = flatSource;
    }

    /**
     * Get the flat source
     * Should handle case of flat_source = null by getting default flat source
     */
    public String getFlatSource() {
        if (flat_source == null) {
            flat_source = getFlatList()[0];
        }

        return flat_source;
    }

    /**
     * Set the flat sampling
     */
    public void setFlatSampling(String flatSampling) {
        _avTable.set(ATTR_FLAT_SAMPLING, flatSampling);
    }

    /**
     * Get the list of available flat source options
     */
    public String[] getFlatList() {
        int fo = 0;

        if (!isImaging()) {
            int di = getDisperserIndex();
            fo = Integer.valueOf(DISPERSERS.elementAt(di, 10));
        }

        switch (fo) {
            case 0:
                return new String[]{"sky"};

            case 1:
                return new String[]{"shutter"};

            case 2:
                return new String[]{"shutter", "hot sphere"};

            case 3:
            default:
                return new String[]{"hot sphere"};
        }
    }

    /**
     * Get the list of available chop frequencies
     */
    public String[] getChopList() {
        String chopList[] = new String[CHOPS.getNumRows()];

        for (int i = 0; i < CHOPS.getNumRows(); i++) {
            chopList[i] = CHOPS.elementAt(i, 0);
        }

        return chopList;
    }

    /**
     * Set the chop frequency
     */
    public void setChopFreq(String chopFreq) {
        _avTable.set(ATTR_CHOP_FREQUENCY, chopFreq);
    }

    /**
     * Set the instrument aperture
     */
    public void setInstAper() {
        if (isImaging()) {
            if (isPolarimetry()) {
                setInstApX(INSTRUMENT_APER_IM_POL[XAP_INDEX]);
                setInstApY(INSTRUMENT_APER_IM_POL[YAP_INDEX]);
                setInstApZ(INSTRUMENT_APER_IM_POL[ZAP_INDEX]);
                setInstApL(INSTRUMENT_APER_IM_POL[LAP_INDEX]);
            } else {
                setInstApX(INSTRUMENT_APER[XAP_INDEX]);
                setInstApY(INSTRUMENT_APER[YAP_INDEX]);
                setInstApZ(INSTRUMENT_APER[ZAP_INDEX]);
                setInstApL(INSTRUMENT_APER[LAP_INDEX]);
            }
        } else {
            int di = getDisperserIndex();
            setInstApX(DISPERSERS.elementAt(di, 6));
            setInstApY(DISPERSERS.elementAt(di, 7));
            setInstApZ(DISPERSERS.elementAt(di, 8));
            setInstApL(DISPERSERS.elementAt(di, 9));
        }
    }

    /**
     * Get the expbase multiplier
     */
    public int getExpbaseMult() {
        return getExpbaseMult("OBJECT");
    }

    /**
     * Get the expbase multiplier
     */
    public int getExpbaseMult(String obsType) {
        int i1;
        double rp = getResolvingPower(obsType);
        double rpMin = Double.valueOf(EXPTIMINGS.elementAt(0, 0));
        int iMax = EXPTIMINGS.getNumRows() - 1;
        double rpMax = Double.valueOf(EXPTIMINGS.elementAt(iMax, 0));

        if (rp <= rpMin) {
            rp = rpMin;
            i1 = 1;
        } else if (rp >= rpMax) {
            rp = rpMax;
            i1 = iMax;
        } else {
            i1 = EXPTIMINGS.rangeInColumn(rp, 0);
        }

        int i0 = i1 - 1;
        double rp0 = Double.valueOf(EXPTIMINGS.elementAt(i0, 0));
        double m0 = Double.valueOf(EXPTIMINGS.elementAt(i0, 1));
        double rp1 = Double.valueOf(EXPTIMINGS.elementAt(i1, 0));
        double m1 = Double.valueOf(EXPTIMINGS.elementAt(i1, 1));

        int interpm = (int) (m0 + ((m1 - m0) * (rp - rp0) / (rp1 - rp0)));
        return interpm;
    }

    /**
     * Use default chop frequency
     */
    public void useDefaultChopFreq() {
        _avTable.rm(ATTR_CHOP_FREQUENCY);
    }

    /**
     * Get the chop frequency
     */
    public String getChopFreq() {
        String cfs = _avTable.get(ATTR_CHOP_FREQUENCY);

        if (cfs == null) {
            cfs = getDefaultChopFrequency("OBJECT");
            setChopFreq(cfs);
        }

        return cfs;
    }

    /**
     * Get the chop frequency
     */
    public String getChopFreqRound() {
        String cfs = getChopFreq();
        double cfd = Double.valueOf(cfs);
        double cfdr = MathUtil.round(cfd, 3);

        String cfsr = Double.toString(cfdr);
        return cfsr;
    }

    /**
     * Set the flat exposure time
     */
    public void setFlatExpTime(double flatExpTime) {
        flatExposureTime = flatExpTime;
    }

    /**
     * Get the flat exposure time
     */
    public double getFlatExpTime() {
        return flatExposureTime;
    }

    /**
     * Set the arc exposure time
     */
    public void setArcExpTime(double arcExpTime) {
        arcExposureTime = arcExpTime;
    }

    /**
     * Get the arc exposure time
     */
    public double getArcExpTime() {
        return arcExposureTime;
    }

    /**
     * Set the target acquisition exposure time
     */
    public void setTargetAcqExpTime(double targetAcqExpTime) {
        targetAcqExposureTime = targetAcqExpTime;
    }

    /**
     /**
     * Get the target acquisition exposure time
     */
    public double getTargetAcqExpTime() {
        return targetAcqExposureTime;
    }

    /**
     * Get the default chop frequency
     */
    public String getDefaultChopFrequency() {
        return getDefaultChopFrequency("OBJECT");
    }

    /**
     * Get the default chop frequency
     */
    public String getDefaultChopFrequency(String obsType) {
        String dcfs;
        int i;
        double rp = getResolvingPower(obsType);
        double rpMin = Double.valueOf(EXPTIMINGS.elementAt(0, 0));
        int iMax = EXPTIMINGS.getNumRows() - 1;
        double rpMax = Double.valueOf(EXPTIMINGS.elementAt(iMax, 0));

        if (rp <= rpMin) {
            i = 0;
        } else if (rp >= rpMax) {
            i = iMax - 1;
        } else {
            i = EXPTIMINGS.rangeInColumn(rp, 0) - 1;
        }

        double cwl = getCentralWavelength();

        if (cwl < N_TO_Q_BOUNDARY) {
            dcfs = EXPTIMINGS.elementAt(i, 2);
        } else {
            dcfs = EXPTIMINGS.elementAt(i, 3);
        }

        return dcfs;
    }

    /**
     * Set the chop delay in seconds
     */
    public void setChopDelay(double chopDelay) {
        _avTable.set(ATTR_CHOP_DELAY, chopDelay);
    }

    /**
     * Get the chop delay in seconds
     */
    public double getChopDelay() {
        int i;
        int iMax = CHOPS.getNumRows() - 1;
        double cMax = Double.valueOf(CHOPS.elementAt(iMax, 0));
        double cfd = Double.valueOf(getChopFreq());

        if (cfd >= cMax) {
            i = iMax - 1;
        } else {
            i = CHOPS.rangeInColumn(cfd, 0);
        }

        double cd = Double.valueOf(CHOPS.elementAt(i, 1));
        return cd;
    }

    /**
     * Set the read interval in seconds
     */
    public void setReadInterval(double readInterval) {
        _avTable.set(ATTR_READ_INTERVAL, readInterval);
    }

    /**
     * Get the reset deal in seconds
     */
    public double getResetDelay() {
        double rd = _avTable.getDouble(ATTR_RESET_DELAY, 0.0);
        return rd;
    }

    /**
     * Get the read interval in seconds
     */
    public double getReadInterval() {
        double ri = _avTable.getDouble(ATTR_READ_INTERVAL, 0.0);
        return ri;
    }

    /**
     * Set the number of reads in the exposure
     */
    public void setNreads(int nreads) {
        _avTable.set(ATTR_NREADS, nreads);
    }

    /**
     * Use default observation time
     */
    public void useDefaultObservationTime() {
        _avTable.set(ATTR_OBSTIME_OT, DEFAULT_TOBS);
    }

    /**
     * Use default exposure time
     */
    public void useDefaultExposureTime() {
        _avTable.rm(ATTR_EXPOSURE_TIME);
    }

    /**
     * Set the observation time in seconds
     */
    public void setObservationTime(double obsTime) {
        _avTable.set(ATTR_OBSTIME_OT, obsTime);
    }

    /**
     * Get the observation time in seconds
     */
    public double getObservationTime() {
        double obsTime = _avTable.getDouble(ATTR_OBSTIME_OT, 0.0);
        return obsTime;
    }

    /**
     * Get the actual observation time (as opposed to the OT observation time)
     * in seconds.
     *
     * Added for OMP by MFO (5 November 2001).
     */
    public double getActualObservationTime() {
        updateDAObjConf();
        return _avTable.getDouble(ATTR_OBSERVATION_TIME, 0.0);
    }

    /**
     * Set the flat observation time in seconds
     */
    public void setFlatObservationTime(double flatObsTime) {
        flatObservationTime = flatObsTime;
    }

    /**
     * Get the flat observation time in seconds
     */
    public double getFlatObservationTime() {
        return flatObservationTime;
    }

    /**
     * Set the arc observation time in seconds
     */
    public void setArcObservationTime(double arcObsTime) {
        arcObservationTime = arcObsTime;
    }

    /**
     * Get the arc observation time in seconds
     */
    public double getArcObservationTime() {
        return arcObservationTime;
    }

    /**
     * Get the target acquisition observation time in seconds
     */
    public double getTargetAcqObservationTime() {
        return targetAcqObservationTime;
    }

    /**
     * Set the target acquisition observation time in seconds
     */
    public void setTargetAcqObservationTime(double targetAcqObsTime) {
        targetAcqObservationTime = targetAcqObsTime;
    }

    /**
     * Get the observation time in seconds as a String
     */
    public String getObservationTimeString() {
        double obt = getObservationTime();
        String obts = Double.toString(obt);
        return obts;
    }

    /**
     * Get the exposure time.
     */
    public double getExpTime() {
        double time = getExposureTime();

        if (time == 0.0) {
            time = getDefaultExpTime();
            setExpTime(Double.toString(time));
        }

        return time;
    }

    /**
     * Get the exposure time as a string
     */
    public String getExpTimeString() {
        String timeAsString = Double.toString(MathUtil.round(getExpTime(), 4));
        return timeAsString;
    }

    /**
     * Set the exposure time.
     */
    public void setExpTime(String seconds) {
        String timeAsStr;

        timeAsStr = seconds;

        if (timeAsStr != null) {
            setExposureTime(timeAsStr);
        }
    }

    /**
     * Get the default sky exposure time
     */
    public double getDefaultSkyExpTime() {
        return getDefaultSkyExpTime("OBJECT");
    }

    /**
     * Get the default sky exposure time
     */
    public double getDefaultSkyExpTime(String obsType) {
        double set = TEXPBASE * getExpbaseMult(obsType) * KSKY;

        if (isPolarimetry()) {
            set = KPOL * set;
        }

        if (isImaging() || obsType.equalsIgnoreCase("TARGETACQ")) {
            double mult = Double.valueOf(getFilterLUT().elementAt(
                    getFilterIndex(obsType), 5));
            set = mult * set;
        }

        if (getCentralWavelength(obsType) < 15.0) {
            set = KBAND * set;
        }

        set = set * KWELL;

        return set;
    }

    /**
     * Adjust exposure time to less than TEXPMAX and the observations time
     * and make it an integer multiple of TEXPMIN
     */
    public double limitExpTime(double et) {
        double let = et;
        if (let > TEXPMAX) {
            let = TEXPMAX;
        }

        double ot = getObservationTime();
        if (let > ot) {
            let = ot;
        }

        if (let > TEXPMIN) {
            let = ((int) (let / TEXPMIN)) * TEXPMIN;

        } else if (let < TEXPMIN) {
            let = TEXPMIN;
        }

        return let;
    }

    /**
     * Get the default exposure time
     */
    public double getDefaultExpTime() {
        return getDefaultExpTime("OBJECT");
    }

    /**
     * Get the default exposure time
     */
    public double getDefaultExpTime(String obsType) {
        double et = getDefaultSkyExpTime(obsType);
        et = limitExpTime(et);
        return et;
    }

    /**
     * Get the default flat exposure time
     */
    public double getDefaultFlatExpTime() {
        double fet = getDefaultSkyExpTime();

        if (getFlatSource().equalsIgnoreCase("mirror")) {
            fet = fet / KSKY;
        }

        fet = limitExpTime(fet);
        return fet;
    }

    /**
     * Get the default target acquisition exposure time
     */
    public double getDefaultTargetAcqExpTime() {
        double et = getDefaultSkyExpTime("TARGETACQ");
        et = limitExpTime(et);
        return et;
    }

    /**
     * Get the default target acquisition coadds
     */
    public int getDefaultTargetAcqCoadds() {
        return DEFAULT_COADDS_TARGET_ACQ;
    }

    /**
     * Get the default bias exposure time
     */
    public double getDefaultBiasExpTime() {
        if (DEFBIASEXPTIME == 0.0) {
            return super.getDefaultBiasExpTime();
        }

        return DEFBIASEXPTIME;
    }

    /**
     * Get the default bias coadds
     */
    public int getDefaultBiasCoadds() {
        if (DEFBIASCOADDS == 0) {
            return super.getDefaultBiasCoadds();
        }

        return DEFBIASCOADDS;
    }

    /**
     * Set the mode
     */
    public void setMode(String mode) {
        _avTable.set(ATTR_MODE, mode);
    }

    /**
     * Set the duty cycle
     */
    public void setDutyCycle(double dutyCycle) {
        _avTable.set(ATTR_DUTY_CYCLE, dutyCycle);
    }

    /**
     * Get the duty cycle
     */
    public String getDutyCycle() {
        String dc = _avTable.get(ATTR_DUTY_CYCLE);
        return dc;
    }

    /**
     * Get the duty cycle as a rounded percentage
     */
    public String getDutyCycleRound() {
        double dcd = Double.valueOf(getDutyCycle()) * 100.0;
        double dcdr = MathUtil.round(dcd, 1);
        String dcsr = Double.toString(dcdr);
        return dcsr;
    }

    /**
     * Update the daconf for a dark observation
     */
    public void updateDADarkConf() {
        String obsType = "Dark";
        updateDAConf(obsType);
    }

    /**
     * Update the daconf for a flat observation
     */
    public void updateDAFlatConf() {
        String obsType = "Flat";
        updateDAConf(obsType);
    }

    /**
     * Update the daconf for an arc observation
     */
    public void updateDAArcConf() {
        String obsType = "Arc";
        updateDAConf(obsType);
    }

    /**
     * Update the daconf for a target acquisition observation
     */
    public void updateDATargetAcqConf() {
        String obsType = "TARGETACQ";
        updateDAConf(obsType);
    }

    /**
     * Update the daconf for an Object/Sky observatioon
     */
    public void updateDAObjConf() {
        String obsType = "Object";
        updateDAConf(obsType);
        _avTable.set(ATTR_MODE, W_mode);
        _avTable.set(ATTR_WAVEFORM, W_waveform);
        _avTable.set(ATTR_NREADS, W_nreads);
        _avTable.set(ATTR_NRESETS, W_nresets);
        _avTable.set(ATTR_RESET_DELAY, W_resetDelay);
        _avTable.set(ATTR_READ_INTERVAL, W_readInterval);
        _avTable.set(ATTR_IDLE_PERIOD, W_idlePeriod);
        _avTable.set(ATTR_MUST_IDLES, W_mustIdles);
        _avTable.set(ATTR_NULL_CYCLES, W_nullCycles);
        _avTable.set(ATTR_NULL_EXPOSURES, W_nullExposures);
        _avTable.set(ATTR_NULL_READS, W_nullReads);
        _avTable.set(ATTR_OBSERVATION_TIME, W_obsTime);
        setChopFreq(W_chopFrequency);
        setChopDelay(W_chopDelay);
        setCoadds(W_coadds);
        setDutyCycle(W_dutyCycle);
    }

    /**
     * Update the daconf for the given obsType
     */
    public void updateDAConf(String obsType) {
        int ci; /* lookup column number */
        int ri; /* loopup row number */
        double expTime = 0.0;
        double obsTime = 0.0;

        /* Get appropriate exposure and observation time for this obsType */
        if (obsType.equalsIgnoreCase("OBJECT")) {
            expTime = getExpTime();
            obsTime = getObservationTime();

        } else if (obsType.equalsIgnoreCase("DARK")) {
            /* Exposure time must be same as for object */
            expTime = getExpTime();
            obsTime = getObservationTime();

        } else if (obsType.equalsIgnoreCase("FLAT")) {
            expTime = getFlatExpTime();
            obsTime = getFlatObservationTime();

        } else if (obsType.equalsIgnoreCase("ARC")) {
            expTime = getArcExpTime();
            obsTime = getArcObservationTime();

        } else if (obsType.equalsIgnoreCase("TARGETACQ")) {
            expTime = getTargetAcqExpTime();
            obsTime = getTargetAcqObservationTime();
        }

        double TDelay = 0.0;
        double actExpTime = 0.0;
        double totalExposure = 0.0;
        double actChopDelay = 0.0;
        double cfd = Double.valueOf(getDefaultChopFrequency(obsType));

        double dwellTime = 0.0;

        int numCycles = 0;

        if (cfd > 0.0) {
            dwellTime = 0.5 / cfd;
        }

        String daconf = null;
        /* Get column number of lookup - depends on chop frequency */
        if (cfd < 0.001) {
            ci = 1;
        } else {
            ci = DACONFS.rangeInRow(cfd, 0);
        }

        /* Get row number of loopup - depends on exposure time */
        ri = DACONFS.rangeInColumn(expTime, 0);
        if (ri < 0) {
            ri = DACONFS.getNumRows() - 1;
        } else {
            ri -= 1;
        }

        /* Perform DACONFS lookup */
        daconf = DACONFS.elementAt(ri, ci);
        /* Find corresponding row in MODES lut */
        ri = MODES.indexInColumn(daconf, 0);
        /* Lookup the W_* values */
        W_mode = MODES.elementAt(ri, 1);
        W_waveform = MODES.elementAt(ri, 2);
        W_nresets = Integer.parseInt(MODES.elementAt(ri, 3));
        W_resetDelay = Double.valueOf(MODES.elementAt(ri, 4));
        W_readInterval = Double.valueOf(MODES.elementAt(ri, 5));
        W_idlePeriod = Double.valueOf(MODES.elementAt(ri, 6));
        W_mustIdles = Integer.parseInt(MODES.elementAt(ri, 7));
        W_nullCycles = Integer.parseInt(MODES.elementAt(ri, 8));
        W_nullExposures = Integer.parseInt(MODES.elementAt(ri, 9));
        W_nullReads = Integer.parseInt(MODES.elementAt(ri, 10));

        /* Set ifChop and ifND flags */
        boolean ifChop = W_mode.equalsIgnoreCase("CHOP")
                || W_mode.equalsIgnoreCase("NDCHOP");
        boolean ifND = W_mode.equalsIgnoreCase("NDSTARE")
                || W_mode.equalsIgnoreCase("NDCHOP");

        /* Perform WAVEFORMS lookup */
        ri = WAVEFORMS.indexInColumn(W_waveform, 0);
        /* Lookup waveform clock period */
        double clkPeriod = Double.valueOf(WAVEFORMS.elementAt(ri, 1)) * 1.0e-9;
        /* Loopup "expWhileRead" flag */
        int expWR = Integer.parseInt(WAVEFORMS.elementAt(ri, 2));
        /* Lookup idle waveform duration */
        double idleT = clkPeriod * Double.valueOf(WAVEFORMS.elementAt(ri, 3));
        /* Lookup NDidle waveform duration */
        double NDidleT = clkPeriod * Double.valueOf(WAVEFORMS.elementAt(ri, 4));
        /* Lookup NDread waveform duration */
        double NDreadT = clkPeriod * Double.valueOf(WAVEFORMS.elementAt(ri, 6));
        /* Lookup readReset waveform duration */
        double readResetT = clkPeriod
                * Double.valueOf(WAVEFORMS.elementAt(ri, 8));
        /* Lookup appropriate reset waveform duration */
        double TEnd = 0.0;
        double resetT = 0.0;

        if (ifND) {
            /* Use NDreset waveform */
            resetT = clkPeriod * Double.valueOf(WAVEFORMS.elementAt(ri, 5));
            TEnd = NDreadT;

        } else {
            /* Use reset waveform */
            resetT = clkPeriod * Double.valueOf(WAVEFORMS.elementAt(ri, 7));
            TEnd = resetT;
        }

        /* Build script section */
        double TGone = 0.0; /* Time spent so far */

        /* Insert mustIdles */
        if (W_idlePeriod > 0.00001 && W_mustIdles > 0) {
            if (idleT > W_idlePeriod) {
                TGone = idleT * W_mustIdles;
            } else {
                TGone = W_idlePeriod * W_mustIdles;
            }
        }

        /* Handle initial chop delay */
        if (ifChop) {
            W_chopDelay = getChopDelay();

            // Delay required excludes time already gone and time to do resets
            TDelay = W_chopDelay - (W_nresets * resetT) - TGone;
            actChopDelay = 0.0;

            if (TDelay > 0.0) {
                /* 1st option is to generate NDIdles */
                if (NDidleT > 0.00001) {
                    actChopDelay = Math.ceil(TDelay / NDidleT) * NDidleT;

                } else if (W_idlePeriod > 0.00001) {
                    actChopDelay = Math.ceil(TDelay / W_idlePeriod)
                            * W_idlePeriod;

                } else {
                    actChopDelay = TDelay;
                }

                TGone = TGone + actChopDelay;
            }
        }

        /* Set up exposures in this beam */
        boolean ifExposing = true;
        /* nexp = exposures in chop phase including null exposures */
        int nexp = 0;
        /* Real (non-null) exposures in whole observation */
        int numExpBeam = 0;
        int numExp = 0;
        double TStart = 0.0;
        double readT = 0.0;
        W_nreads = 0;

        while (ifExposing) {
            nexp++;
            TStart = TGone;

            if (nexp == 1) {
                // First exposure starts with reset
                TGone += resetT;

            } else if (!ifND) {
                // Destructive reading modes use readReset
                TGone += readResetT;

            } else {
                // ND does read followed by reset
                TGone += NDreadT + resetT;
            }

            /* Insert any additional resets */
            if (W_nresets > 1) {
                TGone = TGone + ((W_nresets - 1) * resetT);
            }

            /* Insert ND exposure */
            if (ifND) {
                /* Insert resetDelay */
                if (W_resetDelay > 0.0) {
                    /* Execute NDidles if defined */
                    if (NDidleT > 0.00001) {
                        TGone += Math.ceil(W_resetDelay / NDidleT) * NDidleT;
                    } else {
                        TGone += W_resetDelay;
                    }
                }

                // Insert NDreads, including nullReads
                // How long does each read take?
                // This differs from the readInterval if NDidling is enabled
                if (NDidleT > 0.00001) {
                    readT = Math.round(W_readInterval / NDidleT) * NDidleT;
                } else {
                    readT = W_readInterval;
                }

                /* Compute number of reads in the exposure time */
                W_nreads = (int) Math.round(expTime / readT) + 1;

                /*
                 * Increase TGone by time to take these, plus any nullReads
                 * minus the last read (which is appended below)
                 */
                TGone += ((W_nreads + W_nullReads - 1) * readT);
                actExpTime = (W_nreads - 1) * readT;

            } else {
                /* Non-ND. Wait for the exposure time to expire */
                if (expWR == 0) {
                    TDelay = expTime;
                } else {
                    TDelay = expTime - readResetT;
                }

                if (NDidleT > 0.00001) {
                    // Spend TDelay generating NDidles
                    TGone += Math.round(TDelay / NDidleT) * NDidleT;
                } else {
                    TGone += TDelay;
                }

                actExpTime = expTime;
            }

            /* Check for further exposures */
            if (ifChop) {
                if ((TGone - TStart + TGone + TEnd) < dwellTime) {
                    // Loop back to insert another exposure
                } else if (nexp <= W_nullExposures) {
                    // No real exposures yet, stay in beam and extend dwell
                    // time
                } else {
                    // No more exposures required - set ifExposing to false
                    ifExposing = false;
                    numExpBeam = nexp - W_nullExposures;
                }
            } else {
                // Not chopping
                if ((TGone - TStart + TGone + TEnd) < obsTime) {
                    // Yes - loop back to insert another exposure
                    // Loop back to insert another exposure
                } else if (nexp <= W_nullExposures) {
                    // No real exposures yet, stay in beam and extend dwell
                    // time
                } else {
                    // No - set ifExposing to false
                    ifExposing = false;
                    numExp = nexp - W_nullExposures;
                }
            }
        }

        /* Insert final read (ND) or readReset (non-ND) */
        if (ifND) {
            TGone += NDreadT;
        } else {
            TGone += readResetT;
        }

        /* If chopping, take TGone as new dwell time and compute new chop frequency */
        if (ifChop) {
            dwellTime = TGone;
            cfd = 0.5 / dwellTime;
            String cfs = Double.toString(cfd);
            W_chopFrequency = cfs;
            numCycles = (int) Math.round(obsTime * cfd * 2.0);

            if (numCycles < 1) {
                numCycles = 1;
            }

            numExp = numExpBeam * numCycles;
        } else {
            W_chopFrequency = "0.0";
        }

        /* Report actual observation time */
        W_obsTime = 0.0;

        if (ifChop) {
            W_obsTime = dwellTime * 2.0 * (numCycles + W_nullCycles);
        } else {
            W_obsTime = TGone;
        }

        /* Update the number of required coadds */
        W_coadds = numExp;

        double totalChopDelay = 0;
        /* Compute duty cycle */
        if (ifChop) {
            totalExposure = numExp * 2.0 * actExpTime;
            totalChopDelay = 2.0 * (numCycles + W_nullCycles) * actChopDelay;
            W_dutyCycle = totalExposure / (W_obsTime - totalChopDelay);
        } else {
            totalExposure = actExpTime * numExp;
            W_dutyCycle = totalExposure / W_obsTime;
        }
    }

    /**
     * Update the daconf for the given obsType
     */
    public String updateDAConf(String obsType, double expTime, double obsTime,
            double cfd, FileWriter output) {
        int ci; /* lookup column number */
        int ri; /* loopup row number */

        double TDelay = 0.0;
        double actExpTime = 0.0;
        double totalExposure = 0.0;
        double actChopDelay = 0.0;

        double dwellTime = 0.0;

        int numCycles = 0;

        if (cfd > 0.0) {
            dwellTime = 0.5 / cfd;
        }

        String daconf = null;

        /* Get column number of lookup - depends on chop frequency */
        if (cfd < 0.001) {
            ci = 1;
        } else {
            ci = DACONFS.rangeInRow(cfd, 0);
        }

        /* Get row number of loopup - depends on exposure time */
        ri = DACONFS.rangeInColumn(expTime, 0);
        if (ri < 0) {
            ri = DACONFS.getNumRows() - 1;
        } else {
            ri = ri - 1;
        }

        /* Perform DACONFS lookup */
        daconf = DACONFS.elementAt(ri, ci);
        /* Find corresponding row in MODES lut */
        ri = MODES.indexInColumn(daconf, 0);
        /* Lookup the W_* values */
        W_mode = MODES.elementAt(ri, 1);
        W_waveform = MODES.elementAt(ri, 2);
        W_nresets = Integer.parseInt(MODES.elementAt(ri, 3));
        W_resetDelay = Double.valueOf(MODES.elementAt(ri, 4));
        W_readInterval = Double.valueOf(MODES.elementAt(ri, 5));
        W_idlePeriod = Double.valueOf(MODES.elementAt(ri, 6));
        W_mustIdles = Integer.parseInt(MODES.elementAt(ri, 7));
        W_nullCycles = Integer.parseInt(MODES.elementAt(ri, 8));
        W_nullExposures = Integer.parseInt(MODES.elementAt(ri, 9));
        W_nullReads = Integer.parseInt(MODES.elementAt(ri, 10));

        /* Set ifChop and ifND flags */
        boolean ifChop = W_mode.equalsIgnoreCase("CHOP")
                || W_mode.equalsIgnoreCase("NDCHOP");
        boolean ifND = W_mode.equalsIgnoreCase("NDSTARE")
                || W_mode.equalsIgnoreCase("NDCHOP");

        /* Perform WAVEFORMS lookup */
        ri = WAVEFORMS.indexInColumn(W_waveform, 0);
        /* Lookup waveform clock period */
        double clkPeriod = Double.valueOf(WAVEFORMS.elementAt(ri, 1)) * 1.0e-9;
        /* Loopup "expWhileRead" flag */
        int expWR = Integer.parseInt(WAVEFORMS.elementAt(ri, 2));
        /* Lookup idle waveform duration */
        double idleT = clkPeriod * Double.valueOf(WAVEFORMS.elementAt(ri, 3));
        /* Lookup NDidle waveform duration */
        double NDidleT = clkPeriod * Double.valueOf(WAVEFORMS.elementAt(ri, 4));
        /* Lookup NDread waveform duration */
        double NDreadT = clkPeriod * Double.valueOf(WAVEFORMS.elementAt(ri, 6));
        /* Lookup readReset waveform duration */
        double readResetT = clkPeriod
                * Double.valueOf(WAVEFORMS.elementAt(ri, 8));
        /* Lookup appropriate reset waveform duration */
        double TEnd = 0.0;
        double resetT = 0.0;

        if (ifND) {
            /* Use NDreset waveform */
            resetT = clkPeriod * Double.valueOf(WAVEFORMS.elementAt(ri, 5));
            TEnd = NDreadT;
        } else {
            /* Use reset waveform */
            resetT = clkPeriod * Double.valueOf(WAVEFORMS.elementAt(ri, 7));
            TEnd = resetT;
        }

        /* Build script section */
        double TGone = 0.0; /* Time spent so far */

        /* Insert mustIdles */
        if (W_idlePeriod > 0.00001 && W_mustIdles > 0) {
            if (idleT > W_idlePeriod) {
                TGone = idleT * W_mustIdles;
            } else {
                TGone = W_idlePeriod * W_mustIdles;
            }
        }

        /* Handle initial chop delay */
        if (ifChop) {
            W_chopDelay = getChopDelay();
            // Delay required excludes time already gone and time to do resets
            TDelay = W_chopDelay - (W_nresets * resetT) - TGone;
            actChopDelay = 0.0;

            if (TDelay > 0.0) {
                // 1st option is to generate NDIdles
                if (NDidleT > 0.00001) {
                    actChopDelay = Math.ceil(TDelay / NDidleT) * NDidleT;

                } else if (W_idlePeriod > 0.00001) {
                    actChopDelay = Math.ceil(TDelay / W_idlePeriod)
                            * W_idlePeriod;

                } else {
                    actChopDelay = TDelay;
                }

                TGone = TGone + actChopDelay;
            }
        }

        /* Set up exposures in this beam */
        boolean ifExposing = true;
        /* nexp = exposures in chop phase including null exposures */
        int nexp = 0;
        /* Real (non-null) exposures in whole observation */
        int numExpBeam = 0;
        int numExp = 0;
        double TStart = 0.0;
        double readT = 0.0;
        W_nreads = 0;

        while (ifExposing) {
            nexp++;
            TStart = TGone;

            if (nexp == 1) {
                // First exposure starts with reset
                TGone += resetT;

            } else if (!ifND) {
                // Destructive reading modes use readReset
                TGone += readResetT;

            } else {
                // ND does read followed by reset
                TGone += NDreadT + resetT;
            }

            /* Insert any additional resets */
            if (W_nresets > 1) {
                TGone = TGone + ((W_nresets - 1) * resetT);
            }

            /* Insert ND exposure */
            if (ifND) {
                /* Insert resetDelay */
                if (W_resetDelay > 0.0) {
                    /* Execute NDidles if defined */
                    if (NDidleT > 0.00001) {
                        TGone += Math.ceil(W_resetDelay / NDidleT) * NDidleT;
                    } else {
                        TGone += W_resetDelay;
                    }
                }

                // Insert NDreads, including nullReads
                // How long does each read take? This differs from the
                // readInterval if NDidling is enabled
                if (NDidleT > 0.00001) {
                    readT = Math.round(W_readInterval / NDidleT) * NDidleT;
                } else {
                    readT = W_readInterval;
                }

                // Compute number of reads in the exposure time
                W_nreads = (int) Math.round(expTime / readT) + 1;

                // Increase TGone by time to take these, plus any nullReads
                // minus the last read (which is appended below)
                TGone += ((W_nreads + W_nullReads - 1) * readT);
                actExpTime = (W_nreads - 1) * readT;

            } else {
                // Non-ND. Wait for the exposure time to expire
                if (expWR == 0) {
                    TDelay = expTime;

                } else {
                    TDelay = expTime - readResetT;
                }

                if (NDidleT > 0.00001) {
                    // Spend TDelay generating NDidles
                    TGone += Math.round(TDelay / NDidleT) * NDidleT;

                } else {
                    TGone += TDelay;
                }

                actExpTime = expTime;
            }

            // Check for further exposures
            if (ifChop) {
                if ((TGone - TStart + TGone + TEnd) < dwellTime) {
                    // Loop back to insert another exposure
                } else if (nexp <= W_nullExposures) {
                    // No real exposures yet, stay in beam and extend dwell
                    // time
                } else {
                    // No more exposures required - set ifExposing to false
                    ifExposing = false;
                    numExpBeam = nexp - W_nullExposures;
                }
            } else {
                // Not chopping
                if ((TGone - TStart + TGone + TEnd) < obsTime) {
                    // Yes - loop back to insert another exposure
                    // Loop back to insert another exposure
                } else if (nexp <= W_nullExposures) {
                    // No real exposures yet, stay in beam and extend dwell
                    // time
                } else {
                    // No - set ifExposing to false
                    ifExposing = false;
                    numExp = nexp - W_nullExposures;
                }
            }

        }

        /* Insert final read (ND) or readReset (non-ND) */
        if (ifND) {
            TGone += NDreadT;
        } else {
            TGone += readResetT;
        }

        // If chopping, take TGone as new dwell time and compute new
        // chop frequency
        if (ifChop) {
            dwellTime = TGone;
            cfd = 0.5 / dwellTime;
            String cfs = Double.toString(cfd);
            W_chopFrequency = cfs;
            numCycles = (int) Math.round(obsTime * cfd * 2.0);

            if (numCycles < 1) {
                numCycles = 1;
            }

            numExp = numExpBeam * numCycles;

        } else {
            W_chopFrequency = "0.0";
        }

        /* Report actual observation time */
        W_obsTime = 0.0;
        if (ifChop) {
            W_obsTime = dwellTime * 2.0 * (numCycles + W_nullCycles);
        } else {
            W_obsTime = TGone;
        }

        /* Update the number of required coadds */
        W_coadds = numExp;

        double totalChopDelay = 0;

        /* Compute duty cycle */
        if (ifChop) {
            totalExposure = numExp * 2.0 * actExpTime;
            totalChopDelay = 2.0 * (numCycles + W_nullCycles) * actChopDelay;
            W_dutyCycle = totalExposure / (W_obsTime - totalChopDelay);
        } else {
            totalExposure = actExpTime * numExp;
            W_dutyCycle = totalExposure / W_obsTime;
        }

        try {
            output.write(expTime + " "
                    + cfd + " "
                    + W_obsTime + " "
                    + W_chopFrequency + " "
                    + W_coadds + " "
                    + W_dutyCycle * 100.0 + " "
                    + W_chopDelay + " "
                    + actExpTime + " "
                    + numCycles + " "
                    + W_nullCycles + " "
                    + actChopDelay + " "
                    + totalChopDelay + " "
                    + totalExposure + " " + 1.0 / expTime + "\n");
        } catch (IOException e) {
            System.out.println("IOException writing to output");
        }

        return daconf;
    }

    /**
     * Set coadds
     */
    public void setCoadds(int coadds) {
        getStareCapability().setCoadds(coadds);
    }

    /**
     * Set coadds as a string
     */
    public void setCoadds(String coadds) {
        int c = 0;

        try {
            c = Integer.valueOf(coadds);
        } catch (Exception ex) {
        }

        setCoadds(c);
    }

    /**
     * Use default acquisition
     */
    public void useDefaultAcquisition() {
        useDefaultExposureTime();
        useDefaultObservationTime();
        useDefaultChopFreq();
        setCoadds(0);
    }

    /**
     * Set the acquisition
     */
    public void setAcquisition() {
        /* Setup for normal exposures */
        getExpTime();
        getFilter();
        getWaveplate();
        getMaskAngle();
        updateDAObjConf();
    }

    /**
     * Returns a time estimate in seconds for slewing the telescope.
     *
     * Imaging:      3 minutes
     * Spectroscopy: 8 minutes
     */
    public double getSlewTime() {
        if (isImaging()) {
            return 3.0 * 60.0;
        } else {
            return 8.0 * 60.0;
        }
    }

    /**
     * Iteration Tracker for Michelle.
     *
     * Added for OMP by MFO, 6 Novemeber 2001
     *
     * @see gemini.sp.obsComp.SpInstObsComp
     */
    public class IterTrackerMichelle extends IterTrackerUKIRT {
        public void update(SpIterStep spIterStep) {
            currentIterStepItem = spIterStep.item;
        }

        public double getObserveStepTime() {
            /*
             * extra_oh is a constant overheads related to certain observe
             * iterators: 30 seconds for dark, arc, flat or bias (in addition
             * to the times to do their respective eye), and 30 secs each time
             * an instrument iterator changes a filter.
             */
            double extra_oh = 0.0;

            if ((currentIterStepItem != null)
                    && ((currentIterStepItem instanceof SpIterBiasObs)
                            || (currentIterStepItem instanceof SpIterDarkObs)
                            || (currentIterStepItem instanceof SpIterMichelleCalObs)
                            || (currentIterStepItem instanceof SpIterConfigObs)))
                extra_oh = 30.0;

            int sampling_x =
                    Integer.valueOf(getPixelSampling().substring(0, 1));
            int sampling_y =
                    Integer.valueOf(getPixelSampling().substring(2, 3));

            // Integration over head
            double int_oh = 0.0;

            if ((sampling_x * sampling_y) > 1) {
                int_oh = 1.0;
            }

            // In the future a Michelle specific observation overhead might
            // be added, possibly replacing _obs_oh.

            return (sampling_x * sampling_y * getActualObservationTime())
                    + int_oh + _obs_oh + extra_oh;
        }
    }

    public IterationTracker createIterationTracker() {
        return new IterTrackerMichelle();
    }
}
