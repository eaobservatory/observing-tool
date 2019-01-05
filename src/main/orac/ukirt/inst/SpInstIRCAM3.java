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
import java.util.NoSuchElementException;

import orac.util.LookUpTable;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

import gemini.util.Angle;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.obsComp.SpStareCapability;

/**
 * The IRCAM3 instrument Observation Component
 */
@SuppressWarnings("serial")
public final class SpInstIRCAM3 extends SpUKIRTInstObsComp {
    public static final String ATTR_MODE = "acqMode";
    public static final String ATTR_READAREA = "readoutArea";
    public static final String ATTR_FILTER = "filter";
    public static final String ATTR_POLARISER = "polariser";
    public static final String ATTR_SRCMAG = "sourceMag";
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
    public static double DEFBIASEXPTIME = 0.0;
    public static int DEFBIASCOADDS = 0;
    static String FILTER_NONE = "None";

    public static final SpType SP_TYPE = SpType.create(
            SpType.OBSERVATION_COMPONENT_TYPE, "inst.IRCAM3", "IRCAM3");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpInstIRCAM3());
    }

    public SpInstIRCAM3() {
        super(SP_TYPE);

        addCapability(new SpStareCapability());

        // Read in the instrument config file.
        String baseDir = System.getProperty("ot.cfgdir");

        if (!baseDir.endsWith("/")) {
            baseDir += '/';
        }

        String cfgFile = baseDir + "ircam3.cfg";
        _readCfgFile(cfgFile);

        String attr = ATTR_MODE;
        String value = null;
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

                if (matchAttr(instInfo, "modes")) {
                    MODES = instInfo.getValueAsArray();

                } else if (matchAttr(instInfo, "readareas")) {
                    READAREAS = instInfo.getValueAsLUT();

                } else if (matchAttr(instInfo, "bb_filters")) {
                    BROAD_BAND_FILTERS = instInfo.getValueAsLUT();

                } else if (matchAttr(instInfo, "nb_filters")) {
                    NARROW_BAND_FILTERS = instInfo.getValueAsLUT();

                } else if (matchAttr(instInfo, "other_filters")) {
                    SPECIAL_FILTERS = instInfo.getValueAsLUT();

                } else if (matchAttr(instInfo, "polarisers")) {
                    POLARISERS = instInfo.getValueAsLUT();

                } else if (matchAttr(instInfo, "default_mode")) {
                    DEFAULT_MODE = instInfo.getValue();

                } else if (matchAttr(instInfo, "default_filter")) {
                    DEFAULT_FILTER = instInfo.getValue();

                } else if (matchAttr(instInfo, "exptimes")) {
                    EXPTIMES = instInfo.getValueAsLUT();

                } else if (matchAttr(instInfo, "magnitudes")) {
                    SRCMAGS = instInfo.getValueAsArray();

                } else if (matchAttr(instInfo, "DEFBIASEXPTIME")) {
                    String dbet = instInfo.getValue();

                    try {
                        DEFBIASEXPTIME = Double.valueOf(dbet);
                    } catch (Exception ex) {
                    }

                } else if (matchAttr(instInfo, "DEFBIASCOADDS")) {
                    DEFBIASCOADDS = Integer.parseInt(instInfo.getValue());

                } else if (matchAttr(instInfo, "instrument_port")) {
                    INSTRUMENT_PORT = instInfo.getValue();
                    setPort(INSTRUMENT_PORT);

                } else if (matchAttr(instInfo, "instrument_aper")) {
                    INSTRUMENT_APER = instInfo.getValueAsArray();
                    setInstApX(INSTRUMENT_APER[XAP_INDEX]);
                    setInstApY(INSTRUMENT_APER[YAP_INDEX]);
                    setInstApZ(INSTRUMENT_APER[ZAP_INDEX]);
                    setInstApL(INSTRUMENT_APER[LAP_INDEX]);

                } else if (matchAttr(instInfo, "plate_scale")) {
                    String ps = instInfo.getValue();

                    try {
                        PLATESCALE = Double.valueOf(ps);
                    } catch (Exception ex) {
                    }

                } else if (matchAttr(instInfo, "detector_size")) {
                    DETECTOR_SIZE = instInfo.getValueAsArray();
                    DETECTOR_WIDTH = Integer.parseInt(DETECTOR_SIZE[0]);
                    DETECTOR_HEIGHT = Integer.parseInt(DETECTOR_SIZE[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading IRCAM3 inst. cfg file");
        }
    }

    // A shorthand to make the comparisons shorter and easier to follow.
    private static boolean matchAttr(InstCfg info, String attr) {
        return info.getKeyword().equalsIgnoreCase(attr);
    }

    /**
     * Get the stare capability.
     */
    public SpStareCapability getStareCapability() {
        return (SpStareCapability)
                getCapability(SpStareCapability.CAPABILITY_NAME);
    }

    /**
     * Get the exposure time.  It overrides the one in SpUKIRTInstObsComp.
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
            System.out.println("Array index 0 out of bounds; there is probably "
                            + "something seriously wrong with the ircam3.cfg file.");
        } catch (NoSuchElementException ex) {
            System.out.println("No such filter " + filt
                    + " found in exposure times lookup table.");
        }

        // Determine the column from the source magnitude.
        int column = 0;

        try {
            column = delut.indexInRow(sm, 0);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Array index 0 out of bounds; there is probably "
                            + "something seriously wrong with the ircam3.cfg file.");
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
     * Get the default bias exposure time.
     */
    public double getDefaultBiasExpTime() {
        if (DEFBIASEXPTIME == 0.0) {
            return super.getDefaultBiasExpTime();
        }

        return DEFBIASEXPTIME;
    }

    /**
     * Get the default bias coadds.
     */
    public int getDefaultBiasCoadds() {
        if (DEFBIASCOADDS == 0) {
            return super.getDefaultBiasCoadds();
        }

        return DEFBIASCOADDS;
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
     * Get the default coadds, which depend on exposure time and filter.
     */
    public int getDefaultCoadds() {
        float et = (float) getExpTime();

        // Trap short exposure times.
        if (et < 0.12) {
            return 100;
        }

        // Otherwise depends on filter.
        String filt = getFilter();
        int coadds = 0;
        double totexp = 4.0;

        // Check filter for which change et*coadd total - and et for which
        // if > 4, then just use 1.
        if (filt.startsWith("L")) {
            totexp = 20.0;
        } else if (filt.startsWith("M")) {
            totexp = 10.0;
        } else if (et > 4.0) {
            return 1;
        }

        // If we're still here then calculate coadds from et*coadds.
        try {
            float f = (float) (totexp / et + 0.5);
            coadds = Math.round(f);
        } catch (Exception ex) {
            System.out.println("Error calculating default number of coadds.");
        }

        // Finally, for L* & M* round to nearest 10.
        if (filt.startsWith("L") || filt.startsWith("M")) {
            coadds = 10 * ((coadds / 10) + 1);
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
        _avTable.rm(ATTR_MODE);
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
        String[] split = ra.split("x");
        int xsize = Integer.parseInt(split[0]);
        int ysize = Integer.parseInt(split[1]);

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
     * Get the mode.
     */
    public String getAcqMode() {
        String acqMode = _avTable.get(ATTR_MODE);

        if (acqMode == null) {
            acqMode = getDefaultAcqMode();
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
     * Get the default acquisition mode.  It depends on filter.
     */
    public String getDefaultAcqMode() {
        String filt = getFilter();

        // Determine the row from the filter in use.
        LookUpTable delut = EXPTIMES;
        int row = 0;
        String mode = DEFAULT_MODE;

        try {
            row = delut.indexInColumn(filt, 0);

            // The mode column is the second one.
            mode = delut.elementAt(row, 1);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Array index 0 out of bounds; there is probably "
                            + "something seriously wrong with the ircam3.cfg file");
        } catch (NoSuchElementException ex) {
            System.out.println("No such filter " + filt
                    + " found in exposure times lookup table");
        }

        return mode;
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
        // Confirm to IRCAM3 restrictions: Must be = 0.0!
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
            System.out.println("Error converting string angle to double");
        }

        this.setPosAngleDegrees(posAngle);
    }

    /**
     * Set the instrument apertures.
     *
     * It is called whenever something changes that causes a change
     * in instrument aperture.   X, Y depend on readout area, filter,
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
        if (polariser.equalsIgnoreCase("prism")) {
            x = Double.valueOf(POLARISERS.elementAt(poli, 1));
            y = Double.valueOf(POLARISERS.elementAt(poli, 2));
        }

        // Store the revised instrument apertures.
        setInstApX(String.valueOf(x));
        setInstApY(String.valueOf(y));
    }

    /**
     * Overhead in doing an exposure in seconds.
     *
     * 0.32 seconds for acquisition mode NDSTARE<br>
     * 0.12 seconds for acquisition modes STARE and CHOP.
     */
    public double getExposureOverhead() {
        if (getAcqMode().toUpperCase().indexOf("NDSTARE") > -1) {
            return 0.32;
        }

        return 0.12;
    }
}
