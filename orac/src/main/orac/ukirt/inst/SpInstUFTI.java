/*
 * Copyright 1999-2002 United Kingdom Astronomy Technology Centre, an
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

package orac.ukirt.inst;

import java.io.IOException;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import orac.util.LookUpTable;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

import gemini.util.Angle;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.obsComp.SpStareCapability;

/**
 * The UFTI instrument Observation Component
 */
@SuppressWarnings("serial")
public final class SpInstUFTI extends SpUKIRTInstObsComp {
    public static final String ATTR_MODE = "acqMode";
    public static final String ATTR_READAREA = "readoutArea";
    public static final String ATTR_FILTER = "filter";
    public static final String ATTR_SRCMAG = "sourceMag";
    public static final String ATTR_POLARISER = "polariser";
    public static final String ATTR_FILTER_TYPE = ".gui.filterType";
    public static String[] MODES;
    public static LookUpTable READAREAS;
    public static LookUpTable POLARISERS;
    public static LookUpTable BROAD_BAND_FILTERS;
    public static LookUpTable NARROW_BAND_FILTERS;
    public static LookUpTable SPECIAL_FILTERS;
    public static String[] SRCMAGS;
    public static LookUpTable EXPTIMES;
    public static String DEFAULT_MODE;
    public static String DEFAULT_FILTER;
    public static String[] DETECTOR_SIZE;
    public static int DETECTOR_WIDTH;
    public static int DETECTOR_HEIGHT;
    public static double PLATESCALE;
    public static String[][] EXPTIME_LIMITS;
    public static String[] INSTRUMENT_APER; // Array of inst aper values
    static String FILTER_NONE = "None";

    public static final SpType SP_TYPE = SpType.create(
            SpType.OBSERVATION_COMPONENT_TYPE, "inst.UFTI", "UFTI");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpInstUFTI());
    }

    public SpInstUFTI() {
        super(SP_TYPE);

        addCapability(new SpStareCapability());

        // Read in the instrument config file.
        String baseDir = System.getProperty("ot.cfgdir");

        if (!baseDir.endsWith("/")) {
            baseDir += '/';
        }

        String cfgFile = baseDir + "ufti.cfg";
        _readCfgFile(cfgFile);

        String attr = ATTR_MODE;
        String value = DEFAULT_MODE;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_READAREA;
        value = READAREAS.elementAt(0, 0);
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_FILTER;
        value = DEFAULT_FILTER;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_COADDS;
        _avTable.noNotifySet(attr, "1", 0);

        attr = ATTR_SRCMAG;
        value = SRCMAGS[0];
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_POLARISER;
        value = POLARISERS.elementAt(0, 0);
        _avTable.noNotifySet(attr, value, 0);
    }

    /**
     *  Read the instrument configuration file.
     */
    private void _readCfgFile(String filename) {
        InstCfgReader instCfg = null;
        InstCfg instInfo = null;
        String block = null;

        instCfg = new InstCfgReader(filename);

        try {
            while ((block = instCfg.readBlock()) != null) {
                instInfo = new InstCfg(block);

                if (instInfo.getKeyword().equalsIgnoreCase("modes")) {
                    MODES = instInfo.getValueAsArray();

                } else if (instInfo.getKeyword().equalsIgnoreCase(
                        "readareas")) {
                    READAREAS = instInfo.getValueAsLUT();

                } else if (instInfo.getKeyword().equalsIgnoreCase(
                        "bb_filters")) {
                    BROAD_BAND_FILTERS = instInfo.getValueAsLUT();

                } else if (instInfo.getKeyword().equalsIgnoreCase(
                        "nb_filters")) {
                    NARROW_BAND_FILTERS = instInfo.getValueAsLUT();

                } else if (instInfo.getKeyword().equalsIgnoreCase(
                        "other_filters")) {
                    SPECIAL_FILTERS = instInfo.getValueAsLUT();

                } else if (instInfo.getKeyword().equalsIgnoreCase(
                        "polarisers")) {
                    POLARISERS = instInfo.getValueAsLUT();

                } else if (instInfo.getKeyword().equalsIgnoreCase(
                        "default_mode")) {
                    DEFAULT_MODE = instInfo.getValue();

                } else if (instInfo.getKeyword().equalsIgnoreCase(
                        "default_filter")) {
                    DEFAULT_FILTER = instInfo.getValue();

                } else if (instInfo.getKeyword().equalsIgnoreCase("exptimes")) {
                    EXPTIMES = instInfo.getValueAsLUT();

                } else if (instInfo.getKeyword().equalsIgnoreCase(
                        "magnitudes")) {
                    SRCMAGS = instInfo.getValueAsArray();

                } else if (instInfo.getKeyword().equalsIgnoreCase(
                        "instrument_port")) {
                    INSTRUMENT_PORT = instInfo.getValue();
                    setPort(INSTRUMENT_PORT);

                } else if (instInfo.getKeyword().equalsIgnoreCase(
                        "instrument_aper")) {
                    INSTRUMENT_APER = instInfo.getValueAsArray();

                } else if (instInfo.getKeyword().equalsIgnoreCase(
                        "plate_scale")) {
                    String ps = instInfo.getValue();

                    try {
                        PLATESCALE = Double.valueOf(ps);
                    } catch (Exception ex) {
                    }

                } else if (instInfo.getKeyword().equalsIgnoreCase(
                        "detector_size")) {
                    DETECTOR_SIZE = instInfo.getValueAsArray();
                    DETECTOR_WIDTH = Integer.parseInt(DETECTOR_SIZE[0]);
                    DETECTOR_HEIGHT = Integer.parseInt(DETECTOR_SIZE[1]);

                } else if (instInfo.getKeyword().equalsIgnoreCase(
                        "exptime_limits")) {
                    EXPTIME_LIMITS = instInfo.getValueAs2DArray();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading UFTI instrument cfg file.");
        }
    }

    /**
     * Get the stare capability.
     */
    public SpStareCapability getStareCapability() {
        return (SpStareCapability)
                getCapability(SpStareCapability.CAPABILITY_NAME);
    }

    /**
     * Get the exposure time.
     *
     * It overrides the one in SpUKIRTInstObsComp.
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
     * Set the exposure time.
     *
     * Overrides the one in SpUKIRTInstObsComp.
     */
    public void setExpTime(String seconds) {
        String timeAsStr;

        timeAsStr = seconds;

        if (timeAsStr == null) {
            double time = getDefaultExpTime();
            timeAsStr = Double.toString(time);
        }

        setExposureTime(timeAsStr);
    }

    /**
     * Get the default exposure time, depends on disperser, source magnitude
     * and waveband.
     *
     * It overrides the one in SpUKIRTInstObsComp.
     */
    public double getDefaultExpTime() {
        double exptime = 0.0;
        String sm = getSourceMagnitude();
        String filt = getFilter();

        // Determine the row from the filter in use.
        LookUpTable delut = EXPTIMES;
        int row = 0;

        try {
            row = delut.indexInColumn(filt, 0);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Array index 0 out of bounds;"
                    + " there is probably something seriously wrong"
                    + " with the ufti.cfg file.");
        } catch (NoSuchElementException ex) {
            System.out.println("No such filter " + filt
                    + " found in exposure times lookup table.");
        }

        // Determine the column from the source magnitude.
        int column = 0;
        try {
            column = delut.indexInRow(sm, 0);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Array index 0 out of bounds;"
                    + " there is probably something seriously wrong"
                    + " with the ufti.cfg file.");
        } catch (NoSuchElementException ex) {
            System.out.println("No such source mag " + sm + " found in "
                    + "exposure times lookup table.");
        }

        // Get the element (= default exposure time).
        try {
            exptime = Double.valueOf(delut.elementAt(row, column));
        } catch (Exception ex) {
            System.out.println("Error converting default exposure time.");
        }

        return exptime;
    }

    /**
     * Get the coadds.
     */
    public int getNoCoadds() {
        int coadds = getStareCapability().getCoadds();

        if (coadds == 0) {
            coadds = getDefaultCoadds();
            setNoCoadds(coadds);
        }

        return coadds;
    }

    /**
     * Set the coadds.
     */
    public void setNoCoadds(int coadds) {
        if (coadds == 0) {
            coadds = getDefaultCoadds();
        }

        getStareCapability().setCoadds(coadds);
    }

    /**
     * Set no coadds as a string.
     */
    public void setNoCoadds(String coadds) {
        int c = 0;

        try {
            c = Integer.valueOf(coadds);
        } catch (Exception ex) {
        }

        setNoCoadds(c);
    }

    /**
     * Get the default coadds, which depend on exposure time.
     */
    public int getDefaultCoadds() {
        float et = (float) getExpTime();

        if (et == 0.0) {
            return 1;
        }

        int coadds = 0;

        try {
            float f = (float) (5.0 / et + 0.5);
            coadds = Math.round(f);
        } catch (Exception ex) {
            System.out.println("Error calculating default no. coadds");
        }

        return coadds;
    }

    /**
     * Get the source magnitude.
     */
    public String getSourceMagnitude() {
        String sm = _avTable.get(ATTR_SRCMAG);

        if (sm == null) {
            _avTable.noNotifySet(ATTR_SRCMAG, SRCMAGS[0], 0);
            sm = SRCMAGS[0];
        }

        return sm;
    }

    /**
     * Set the source magnitude.
     */
    public void setSourceMagnitude(String sm) {
        _avTable.set(ATTR_SRCMAG, sm);
    }

    /**
     * Use default acquisition.
     */
    public void useDefaultAcquisition() {
        _avTable.rm(ATTR_EXPOSURE_TIME);
        _avTable.rm(ATTR_COADDS);
    }

    /**
     * Return the science area.
     */
    public double[] getScienceArea() {
        // The size depends upon the selected readout Area.
        String ra = getReadoutArea();

        // Split it into its constituent dimensions.
        StringTokenizer st = new StringTokenizer(ra, "x");
        int xsize = Integer.parseInt(st.nextToken());
        int ysize = Integer.parseInt(st.nextToken());
        double[] size = new double[]{PLATESCALE * xsize, PLATESCALE * ysize};

        return size;
    }

    /**
     * Get the readout area.
     */
    public String getReadoutArea() {
        String readoutArea = _avTable.get(ATTR_READAREA);

        if (readoutArea == null) {
            readoutArea = READAREAS.elementAt(0, 0);
        }

        return readoutArea;
    }

    /**
     * Set the readout area.
     *
     * This also updates the instrument-aperture X,Y values.
     */
    public void setReadoutArea(String readoutArea) {
        _avTable.set(ATTR_READAREA, readoutArea);

        // Hack to cause an update in the position editor.  Set the position
        // angle to do this.
        setPosAngleDegrees(0.0);

        // When the readout area changes so may the X,Y instrument apertures.
        setInstAper();

        // Don't know what to do about Z yet.
    }

    /**
     * Get the index of the current readout area.
     */
    public int getReadoutAreaIndex() {
        String readout = getReadoutArea();
        int roaIndex = 0;

        try {
            roaIndex = READAREAS.indexInColumn(readout, 0);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Failed to find polariser index!");
        } catch (NoSuchElementException ex) {
            System.out.println("Failed to find polariser index!");
        }

        return roaIndex;
    }

    /**
     * Get the acquisition mode.
     */
    public String getAcqMode() {
        String acqMode = _avTable.get(ATTR_MODE);

        if (acqMode == null) {
            acqMode = MODES[0];
        }

        return acqMode;
    }

    /**
     * Set the acquisition mode.
     */
    public void setAcqMode(String acqMode) {
        _avTable.set(ATTR_MODE, acqMode);
    }

    /**
     * Get the filter.
     */
    public String getFilter() {
        String filter = _avTable.get(ATTR_FILTER);

        if (filter == null) {
            filter = DEFAULT_FILTER;
        }

        return filter;
    }

    /**
     * Set the filter.
     */
    public void setFilter(String filter) {
        _avTable.set(ATTR_FILTER, filter);
    }

    /**
     * Get the polariser.
     */
    public String getPolariser() {
        String pol = _avTable.get(ATTR_POLARISER);

        if (pol == null) {
            pol = POLARISERS.elementAt(0, 0);
        }

        return pol;
    }

    /**
     * Set the polariser.
     */
    public void setPolariser(String polariser) {
        _avTable.set(ATTR_POLARISER, polariser);

        // When the polariser changes so will the X,Y instrument apertures.
        setInstAper();
    }

    /**
     * Get the index of the current polariser.
     */
    public int getPolariserIndex() {
        String polariser = getPolariser();
        int polIndex = 0;

        try {
            polIndex = POLARISERS.indexInColumn(polariser, 0);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Failed to find polariser index!");
        } catch (NoSuchElementException ex) {
            System.out.println("Failed to find polariser index!");
        }

        return polIndex;
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
        // Confirm to UFTI restrictions: Must be = 0.0!
        if (posAngle != 0.0) {
            super.setPosAngleDegrees(0.0);
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
     * Set the instrument apertures.
     *
     * It is called whenever something changes that causes a change in
     * instrument aperture.   X, Y depend on readout area, filter,
     * and polariser.  Positional angle is not included as it's
     * zero by definition.  Z is not changed.
     */
    public void setInstAper() {
        // Need polariser and indices to polariser and readout-area lookup
        // tables.
        String polariser = getPolariser();
        int roai = getReadoutAreaIndex();
        int poli = getPolariserIndex();

        // Read the X,Y instrument apertures from the readout-area lookup
        // table.
        double x = Double.valueOf(READAREAS.elementAt(roai, 1));
        double y = Double.valueOf(READAREAS.elementAt(roai, 2));

        // Polariser overrides readout area.  (Is there a check that prevents
        // sub-arrays being used with the polariser?)
        if (polariser.equalsIgnoreCase("prism")
                || polariser.equalsIgnoreCase("FP")) {
            x = Double.valueOf(POLARISERS.elementAt(poli, 1));
            y = Double.valueOf(POLARISERS.elementAt(poli, 2));
        }

        // Store the revised instrument apertures.
        setInstApX(String.valueOf(x));
        setInstApY(String.valueOf(y));
        setInstApZ(INSTRUMENT_APER[ZAP_INDEX]);
        setInstApL(INSTRUMENT_APER[LAP_INDEX]);
    }

    /**
     * Overhead in doing an exposure in seconds.
     *
     * Overhead time associated with an exposure (see
     * {@link #getExposureTime()} and {@link #setExposureTime()}).
     * cause by reset time, read time, NDR reset delay etc.
     *
     * This method is used for MSB duration estimation. Subclasses can override
     * this method to return an  instrument specific value.
     */
    public double getExposureOverhead() {
        String readoutArea = getReadoutArea();

        if (readoutArea.equalsIgnoreCase("1024x1024")) {
            return 4.0;
        } else if (readoutArea.indexOf("1024") > -1) {
            return 2.0;
        } else {
            return 1.0;
        }
    }

    /**
     * Return the current exposure time limits for the current mode
     * and readout area.
     *
     * This should be called whenever the UFTI mode and readout area change.
     */
    public double[] getExpTimeLimits() {
        double[] limits = new double[2];
        String roa = getReadoutArea();
        String mode = getAcqMode();

        for (int i = 0; i < EXPTIME_LIMITS.length; i++) {
            if (EXPTIME_LIMITS[i][0].equals(roa)
                    && EXPTIME_LIMITS[i][1].equals(mode)) {
                // Get the limits
                limits[0] = Double.parseDouble(EXPTIME_LIMITS[i][2]);
                limits[1] = Double.parseDouble(EXPTIME_LIMITS[i][3]);
            }
        }

        return limits;
    }

    public Hashtable<String, String> getConfigItems() {
        Hashtable<String, String> list = new Hashtable<String, String>();
        list.put("instrument", "UFTI");
        list.put("version", "1");
        list.put("readMode", getAcqMode());
        list.put("readArea", getReadoutArea());

        String filter = getFilter();
        if (getPolariser().equals("prism")) {
            filter = filter + "+pol";
        }

        list.put("filter", filter);
        list.put("expTime", "" + getExposureTime());
        list.put("objNumExp", "" + getCoadds());
        list.put("darkNumExp", "1");

        setInstAper();

        list.put("instAperX", "" + getInstApX());
        list.put("instAperY", "" + getInstApY());
        list.put("instAperZ", "" + getInstApZ());
        list.put("instAperL", "" + getInstApL());

        return list;
    }
}
