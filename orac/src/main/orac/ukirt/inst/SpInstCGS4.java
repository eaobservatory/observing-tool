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
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.Hashtable;

import orac.util.LookUpTable;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

import gemini.util.MathUtil;
import gemini.util.Angle;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.obsComp.SpChopCapability;
import gemini.sp.obsComp.SpStareCapability;
import gemini.sp.iter.SpIterConfigObs;
import orac.ukirt.iter.SpIterBiasObs;
import orac.ukirt.iter.SpIterDarkObs;
import orac.ukirt.iter.SpIterCGS4CalObs;

/**
 * The CGS4 instrument Observation Component.
 *
 * @author Alan Bridger, UKATC
 * @version 1.0
 */
@SuppressWarnings("serial")
public final class SpInstCGS4 extends SpUKIRTInstObsComp {
    // Attributes presented to user
    public static String ATTR_DISPERSER = "disperser";
    public static String ATTR_MODE = "acqMode";
    public static String ATTR_CENTRAL_WAVELENGTH = "centralWavelength";
    public static String ATTR_ORDER = "order";
    public static String ATTR_MASK = "mask";
    public static String ATTR_SRCMAG = "sourceMag";
    public static String ATTR_SAMPLING = "sampling";
    public static String ATTR_POLARISER = "polariser";
    public static String ATTR_NEUTRAL_DENSITY = "neutralDensity";
    public static String ATTR_CVFOFFSET = "cvfOffset";
    public static String ATTR_CVFWAVELENGTH = "cvfWavelength";

    // Derived attributes: required by instrument, but not presented to user
    public static String ATTR_FILTER = "filter";
    public static String NO_VALUE = "none";
    public static double DISP_ARCSEC_PER_PIX = 0.6;
    public static double SPATIAL_ARCSEC_PER_PIX = 0.61;
    public static double DISP_ARCSEC_PER_PIX_ECH = 0.6;
    public static double SPATIAL_ARCSEC_PER_PIX_ECH = 0.61;
    public static double DISP_ARCSEC_PER_PIX_40 = 0.6;
    public static double SPATIAL_ARCSEC_PER_PIX_40 = 0.61;
    public static double DISP_ARCSEC_PER_PIX_150 = 0.6;
    public static double SPATIAL_ARCSEC_PER_PIX_150 = 0.61;

    // Class variable reporesenting defaults, look up tables, etc.
    public static LookUpTable DISPERSERS;
    public static String[] MODES;
    public static String[] SAMPLINGS;
    public static LookUpTable MASKS;
    public static LookUpTable MASKSECH;
    public static String[] POLARISERS;
    public static String[] SRCMAGS;
    public static String[] FILTERS;
    public static String DEFAULT_DISPERSER;
    public static String DEFAULT_MODE;
    public static String[] DETECTOR_SIZE;
    public static int DETECTOR_WIDTH;
    public static int DETECTOR_HEIGHT;
    public static LookUpTable ORDERS1;
    public static LookUpTable ORDERS2;
    public static LookUpTable ORDERS3;
    public static LookUpTable EXPTIMES150;
    public static LookUpTable EXPTIMES40;
    public static LookUpTable EXPTIMESECH;
    public static LookUpTable FILTERS150;
    public static LookUpTable FILTERS40;
    public static LookUpTable FILTERSECH;
    public static double DEFBIASEXPTIME = 0.0;
    public static int DEFBIASCOADDS = 0;
    public static String[] INSTRUMENT_APER; // Array of inst aper values
    public static int CENT_ROW;
    public static int PEAK_ROW;
    private double ROTATION_SCALE = 2.01;
    private double ANGLE_OFFSET = 60.0;
    boolean centralWavelengthSet = false;

    public static final SpType SP_TYPE = SpType.create(
            SpType.OBSERVATION_COMPONENT_TYPE, "inst.CGS4", "CGS4");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpInstCGS4());
    }

    /**
     * Constructor reads instrument .cfg file and initialises values
     */
    public SpInstCGS4() {
        super(SP_TYPE);
        addCapability(new SpChopCapability());
        addCapability(new SpStareCapability());

        // Read in the instrument config file
        String baseDir = System.getProperty("ot.cfgdir");

        if (!baseDir.endsWith("/")) {
            baseDir += '/';
        }

        String cfgFile = baseDir + "cgs4.cfg";
        _readCfgFile(cfgFile);

        // Set the initial values of the attributes
        String attr = ATTR_DISPERSER;
        String value = DEFAULT_DISPERSER;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_CENTRAL_WAVELENGTH;
        _avTable.noNotifySet(attr, "1.0", 0);

        attr = ATTR_ORDER;
        _avTable.noNotifySet(attr, "0", 0);

        attr = ATTR_MASK;
        value = MASKS.elementAt(0, 0);
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_SRCMAG;
        value = SRCMAGS[0];
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_SAMPLING;
        value = SAMPLINGS[0];
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_POLARISER;
        value = POLARISERS[0];
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_NEUTRAL_DENSITY;
        _avTable.noNotifySet(attr, "0", 0);

        attr = ATTR_CVFOFFSET;
        _avTable.noNotifySet(attr, "0.0", 0);

        attr = ATTR_MODE;
        value = DEFAULT_MODE;
        _avTable.noNotifySet(attr, value, 0);

        attr = ATTR_EXPOSURE_TIME;
        _avTable.noNotifySet(attr, "0", 0);

        attr = ATTR_COADDS;
        _avTable.noNotifySet(attr, "1", 0);

        attr = ATTR_FILTER;
        value = getDefaultFilter(); // MFO, 9 November 2001
        _avTable.noNotifySet(attr, value, 0);
    }

    private void _readCfgFile(String filename) {
        InstCfgReader instCfg = null;
        InstCfg instInfo = null;
        String block = null;

        instCfg = new InstCfgReader(filename);

        try {
            while ((block = instCfg.readBlock()) != null) {
                instInfo = new InstCfg(block);

                if (InstCfg.matchAttr(instInfo, "dispersers")) {
                    DISPERSERS = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "modes")) {
                    MODES = instInfo.getValueAsArray();

                } else if (InstCfg.matchAttr(instInfo, "samplings")) {
                    SAMPLINGS = instInfo.getValueAsArray();

                } else if (InstCfg.matchAttr(instInfo, "masks")) {
                    MASKS = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "masksech")) {
                    MASKSECH = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "polarisers")) {
                    POLARISERS = instInfo.getValueAsArray();

                } else if (InstCfg.matchAttr(instInfo, "magnitudes")) {
                    SRCMAGS = instInfo.getValueAsArray();

                } else if (InstCfg.matchAttr(instInfo, "filters")) {
                    FILTERS = instInfo.getValueAsArray();

                } else if (InstCfg.matchAttr(instInfo, "default_disperser")) {
                    DEFAULT_DISPERSER = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "default_mode")) {
                    DEFAULT_MODE = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "DEFBIASEXPTIME")) {
                    String dbet = instInfo.getValue();

                    try {
                        DEFBIASEXPTIME = Double.valueOf(dbet);
                    } catch (Exception ex) {
                    }

                } else if (InstCfg.matchAttr(instInfo, "DEFBIASCOADDS")) {
                    DEFBIASCOADDS = Integer.parseInt(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "detector_size")) {
                    DETECTOR_SIZE = instInfo.getValueAsArray();
                    DETECTOR_WIDTH = Integer.parseInt(DETECTOR_SIZE[0]);
                    DETECTOR_HEIGHT = Integer.parseInt(DETECTOR_SIZE[1]);

                } else if (InstCfg.matchAttr(instInfo, "orders1")) {
                    ORDERS1 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "orders2")) {
                    ORDERS2 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "orders3")) {
                    ORDERS3 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "exptimes150")) {
                    EXPTIMES150 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "exptimes40")) {
                    EXPTIMES40 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "exptimesech")) {
                    EXPTIMESECH = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "filters150")) {
                    FILTERS150 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "filters40")) {
                    FILTERS40 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "filtersech")) {
                    FILTERSECH = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "instrument_port")) {
                    INSTRUMENT_PORT = instInfo.getValue();
                    setPort(INSTRUMENT_PORT);

                } else if (InstCfg.matchAttr(instInfo, "instrument_aper")) {
                    INSTRUMENT_APER = instInfo.getValueAsArray();

                } else if (InstCfg.matchAttr(instInfo, "CENT_ROW")) {
                    CENT_ROW = Integer.parseInt(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "PEAK_ROW")) {
                    PEAK_ROW = Integer.parseInt(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "ROTATION_SCALE")) {
                    ROTATION_SCALE = Double.parseDouble(instInfo.getValue());

                } else if (InstCfg.matchAttr(instInfo, "ANGLE_OFFSET")) {
                    ANGLE_OFFSET = Double.parseDouble(instInfo.getValue());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CGS4 inst. cfg file");
        }
    }

    /**
     * Get the chop capability.
     */
    public SpChopCapability getChopCapability() {
        return (SpChopCapability) getCapability(
                SpChopCapability.CAPABILITY_NAME);
    }

    /**
     * Get the stare capability.
     */
    public SpStareCapability getStareCapability() {
        return (SpStareCapability) getCapability(
                SpStareCapability.CAPABILITY_NAME);
    }

    /**
     * Return the science area based upon the current mask.
     */
    public double[] getScienceArea() {
        double diArcsecPerPix;
        double spArcsecPerPix;

        diArcsecPerPix = DISP_ARCSEC_PER_PIX;
        spArcsecPerPix = SPATIAL_ARCSEC_PER_PIX;

        double[] size = new double[2];
        double w = getMaskWidth();

        if (w == 0.0) {
            w = (double) DETECTOR_WIDTH;
        }

        size[0] = w * diArcsecPerPix;
        size[1] = DETECTOR_HEIGHT * spArcsecPerPix;

        return size;
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
     * Set the exposure time.
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
     * Get the default exposure time, depends on disperser, source mag and
     * waveband.
     */
    public double getDefaultExpTime() {
        double exptime = 0.0;
        int di = getDisperserIndex();
        String sm = getSourceMagnitude();
        double cwl = getCentralWavelength();
        double sw = getMaskWidth();

        try {
            LookUpTable delut = null;

            // get the default exp. time lut
            if (di == DISPERSERS.indexInColumn("EXPTIMES40", 3)) {
                delut = EXPTIMES40;
            } else if (di == DISPERSERS.indexInColumn("EXPTIMESECH", 3)) {
                delut = EXPTIMESECH;
            }

            // determine the row from the cwl
            int row = delut.rangeInColumn(cwl, 0);

            // determine the column from the source mag.
            int column = delut.indexInRow(sm, 0);

            // and get the element (=default exp. time)
            Double et = Double.valueOf(delut.elementAt(row, column));
            // Divide by mask width.
            exptime = et / sw;

        } catch (ArrayIndexOutOfBoundsException ex) {
            exptime = 0.0;
        } catch (NoSuchElementException ex) {
            exptime = 0.0;
        } catch (NumberFormatException ex) {
            System.out.println("Error converting default exp. time");
            exptime = 0.0;
        }

        return exptime;
    }

    /**
     * Get the acquisition overhead.
     *
     * The value will be 5 minutes for all source
     */
    public double getAcqTime() {
        return 5.0 * 60.0;
    }

    /**
     * Get the default bias exposure time
     */
    public double getDefaultBiasExpTime() {
        return DEFBIASEXPTIME != 0.0
                ? DEFBIASEXPTIME
                : super.getDefaultBiasExpTime();
    }

    /**
     * Get the default bias coadds
     */
    public int getDefaultBiasCoadds() {
        return DEFBIASCOADDS != 0
                ? DEFBIASCOADDS
                : super.getDefaultBiasCoadds();
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
     * Set the coadds
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
     * Get the default coadds, depends on exposure time
     */
    public int getDefaultCoadds() {
        float et = (float) getExpTime();

        if (et == 0.0) {
            return 1;
        }

        int coadds = 0;

        try {
            float f = 5.0f / et + 0.5f;
            coadds = Math.round(f);
        } catch (Exception ex) {
            System.out.println("Error calculating default no. coadds");
        }

        return coadds;
    }

    /**
     * Get the exp/chop pos
     */
    public int getExpPerChopPos() {
        int expPCP = getChopCapability().getExposuresPerChopPosition();

        if (expPCP == 1 || expPCP == 0) {
            expPCP = getDefaultCoadds();
            setExpPerChopPos(expPCP);
        }

        return expPCP;
    }

    /**
     * Set the exp/cop pos
     */
    public void setExpPerChopPos(int expPCP) {
        if (expPCP == 1 || expPCP == 0) {
            expPCP = getDefaultCoadds();
        }

        getChopCapability().setExposuresPerChopPosition(expPCP);
    }

    /**
     * Set exp/chop pos as a string.
     */
    public void setExpPerChopPos(String expPCP) {
        int c = 0;

        try {
            c = Integer.valueOf(expPCP);
        } catch (Exception ex) {
        }

        setExpPerChopPos(c);
    }

    /**
     * Get the cycles/obs
     */
    public int getCyclesPerObs() {
        int cycPO = getChopCapability().getCyclesPerObserve();

        if (cycPO == 0) {
            cycPO = getDefaultCyclesPerObs();
            setCyclesPerObs(cycPO);
        }

        return cycPO;
    }

    /**
     * Set the cycles/obs
     */
    public void setCyclesPerObs(int cycPO) {
        if (cycPO == 0) {
            cycPO = getDefaultCyclesPerObs();
        }

        getChopCapability().setCyclesPerObserve(cycPO);
    }

    /**
     * Set cycles/obs as a string.
     */
    public void setCyclesPerObs(String cycPO) {
        int c = 0;

        try {
            c = Integer.valueOf(cycPO);
        } catch (Exception ex) {
        }

        setCyclesPerObs(c);
    }

    /**
     * Get the default cycles/obs
     */
    public int getDefaultCyclesPerObs() {
        return 1;
    }

    /**
     * Get the source magnitude
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
     * Get the sampling
     */
    public String getSampling() {
        String sam = _avTable.get(ATTR_SAMPLING);

        if (sam == null) {
            _avTable.noNotifySet(ATTR_SAMPLING, SAMPLINGS[0], 0);
            sam = SAMPLINGS[0];
        }

        return sam;
    }

    /**
     * Set the sampling
     */
    public void setSampling(String sam) {
        _avTable.set(ATTR_SAMPLING, sam);
    }

    /**
     * Get the disperser (aka grating).
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
     * Set the disperser.
     */
    public void setDisperser(String disperser) {
        _avTable.set(ATTR_DISPERSER, disperser);

        // When the disperser changes so will the instrument aperture
        setInstAper();
    }

    /**
     * Get the index of the current disperser.
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
     * Get the dispserser R (resolving power).
     */
    public int getDisperserR() {
        int di = getDisperserIndex();

        try {
            return Integer.parseInt(DISPERSERS.elementAt(di, 1));
        } catch (NumberFormatException e) {
        }

        return 0;
    }

    /**
     * Set the order.
     */
    public void setOrder(int order) {
        int o;

        o = order;

        if (o == 0) {
            o = getDefaultOrder();
        }

        _avTable.set(ATTR_ORDER, o);

        // When the order changes so will the instrument aperture - one day
        setInstAper();

    }

    /**
     * Set the order as a string.
     */
    public void setOrder(String order) {
        int o = 0;

        try {
            o = Integer.valueOf(order);
        } catch (Exception ex) {
        }

        setOrder(o);
    }

    /**
     * Get the order.
     */
    public int getOrder() {
        int o = _avTable.getInt(ATTR_ORDER, 0);

        if (o == 0) {
            o = getDefaultOrder();
            setOrder(Integer.toString(o));
        }

        return o;
    }

    /**
     * Get the default order using the current wavelength and disperser.
     */
    public int getDefaultOrder() {
        int order = 0;

        // get disperser index, then look up value in lut
        int di = getDisperserIndex();
        double cwl = getCentralWavelength();

        try {
            LookUpTable dolut = null;

            if (di == DISPERSERS.indexInColumn("ORDERS1", 2)) {
                dolut = ORDERS1;
            } else if (di == DISPERSERS.indexInColumn("ORDERS3", 2)) {
                dolut = ORDERS3;
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
     * Get the central wavelength.
     */
    public double getCentralWavelength() {
        double cwl = _avTable.getDouble(ATTR_CENTRAL_WAVELENGTH, 0.0);
        return cwl;
    }

    /**
     * Set the central wavelength.
     */
    public void setCentralWavelength(double cwl) {
        _avTable.set(ATTR_CENTRAL_WAVELENGTH, cwl);

        // When the wavelength changes so will the instrument aperture
        setInstAper();
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
     * Use default order.
     */
    public void useDefaultOrder() {
        _avTable.rm(ATTR_ORDER);
    }

    /**
     * Use default acquisition
     */
    public void useDefaultAcquisition() {
        _avTable.rm(ATTR_EXPOSURE_TIME);
        _avTable.rm(ATTR_COADDS);
        _avTable.rm(ATTR_EXPOSURES_PER_CHOP_POSITION);
        _avTable.rm(ATTR_CYCLES_PER_OBSERVE);
    }

    /**
     * Calculate the wavelength coverage, based upon the disperser and
     * central wavelength.  0 is returned if there is no disperser selected.
     */
    public double[] getWavelengthCoverage() {
        double range[] = new double[2];
        int r = getDisperserR();

        if (r == 0) {
            range[0] = 0.0;
            range[1] = 0.0;

        } else {
            // Obtain the disperser name, central wavelength, and the
            // wavelength coverage in first order for the standard
            // gratings but the coverage per unit wavelength for the echelle.
            int di = getDisperserIndex();
            String disp = getDisperser();
            double cw = getCentralWavelength();
            double coverage = Double.valueOf(DISPERSERS.elementAt(di, 9));
            double wr;

            // Ordinary gratings.  Range fixed at each order.
            if (disp.startsWith("40") || disp.startsWith("150")) {
                int o = getOrder();
                wr = coverage / o;
                // Echelle range is depends on the central wavelength.
            } else {
                wr = coverage * cw;
            }
            range[0] = cw - wr / 2.0;
            range[1] = cw + wr / 2.0;
        }
        return range;
    }

    /**
     * Calculate the spectral resolution, based upon the disperser,
     * order, pixel width, and central wavelength.  0 is returned if
     * there is no disperser selected.
     */
    public double getResolution() {
        double res;
        int r = getDisperserR();

        if (r == 0) {
            res = 0.0;
        } else {

            // Obtain the disperser name.
            String disp = getDisperser();
            double sw = getMaskWidth();

            // Ordinary gratings
            if (disp.startsWith("40") || disp.startsWith("150")) {
                int o = getOrder();
                double cw = getCentralWavelength();
                res = o * r * cw / sw;

                // Echelle resolution is independent of the wavelength.
                // Assume that the optimum order is used.
            } else {
                res = r / sw;
            }
        }

        return res;
    }

    /**
     * Get the CVF offset
     */
    public double getCvfOffset() {
        double cvfo = _avTable.getDouble(ATTR_CVFWAVELENGTH, 0.0);
        return cvfo;
    }

    /**
     * Set the CVF offset
     */
    public void setCvfOffset(double cvfo) {
        _avTable.set(ATTR_CVFWAVELENGTH, cvfo);
    }

    /**
     * Set the CVF offset as a string
     */
    public void setCvfOffset(String cvfo) {
        double d = 0.0;

        try {
            d = Double.valueOf(cvfo);
        } catch (Exception ex) {
        }

        _avTable.set(ATTR_CVFWAVELENGTH, d);
    }

    /**
     * Get the ND filter use
     */
    public boolean getNdFilter() {
        boolean nd = _avTable.getBool(ATTR_NEUTRAL_DENSITY);
        return nd;
    }

    /**
     * Set the ND filter use
     */
    public void setNdFilter(boolean nd) {
        _avTable.set(ATTR_NEUTRAL_DENSITY, nd);
    }

    /**
     * Get the mask, aka slit.
     */
    public String getMask() {
        String mask = _avTable.get(ATTR_MASK);

        if (mask == null) {
            try {
                mask = MASKS.elementAt(0, 0);
            } catch (Exception ex) {
            }
        }
        return mask;
    }

    /**
     * Set the mask.
     */
    public void setMask(String mask) {
        _avTable.set(ATTR_MASK, mask);

        // When the mask changes so will the instrument aperture - one day
        setInstAper();

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
     * Get the mask menu for the current disperser.
     */
    public Vector<String> getMaskMenu() {
        String disp = getDisperser();

        // The default menu of masks
        Vector<String> maskMenu = MASKS.getColumn(0);

        // The menu of masks for the echelle
        if (disp.toLowerCase().startsWith("echelle")) {
            maskMenu = MASKSECH.getColumn(0);
        }

        return maskMenu;
    }

    /**
     * Get the mask width for the current mask.
     */
    public double getMaskWidth() {
        double returnable = 0.0;

        try {
            returnable = new Double(MASKS.elementAt(getMaskIndex(), 1));
        } catch (IndexOutOfBoundsException e) {
        } catch (NumberFormatException e) {
        }

        return returnable;
    }

    /**
     * Get the polariser
     */
    public String getPolariser() {
        String pol = _avTable.get(ATTR_POLARISER);

        if (pol == null) {
            pol = POLARISERS[0];
        }

        return pol;
    }

    /**
     * Set the polariser.
     */
    public void setPolariser(String polariser) {
        _avTable.set(ATTR_POLARISER, polariser);
    }

    /**
     * Get the filter.
     */
    public String getFilter() {
        String filter = _avTable.get(ATTR_FILTER);

        if (filter == null) {
            filter = getDefaultFilter();
            setFilter(filter);
        }

        return filter;
    }

    /**
     * Get the default filter, depending on the disperser, order and wavelength
     */
    public String getDefaultFilter() {
        String filter = "NONE";

        int di = getDisperserIndex();
        int o = getOrder();
        String os = Integer.toString(o);
        double cwl = getCentralWavelength();

        try {
            // get the default filter lut
            LookUpTable fillut = null;

            if (di == DISPERSERS.indexInColumn("FILTERS40", 4)) {
                fillut = FILTERS40;
            } else if (di == DISPERSERS.indexInColumn("FILTERSECH", 4)) {
                fillut = FILTERSECH;
            }

            // determine the row from the cwl
            int row = fillut.rangeInColumn(cwl, 0);

            // determine the column from the order - check against no. of
            // columnms because if not all orders are specified then use
            // the _last_ as a default
            int numCols = fillut.getNumColumns();
            int column = numCols - 1;

            try {
                column = fillut.indexInRow(os, 0);
            } catch (NoSuchElementException e) {
            }

            // and get the element (=default filter), catching any failures
            // from above
            filter = fillut.elementAt(row, column);

        } catch (Exception ex) {
            System.out.println("Caught exception looking for default filter: "
                    + ex);
        }

        return filter;
    }

    /**
     * Set the filter.
     */
    public void setFilter(String filter) {
        String f;

        f = filter;

        if (f == null) {
            f = getDefaultFilter();
        }

        _avTable.set(ATTR_FILTER, f);
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
     * Set the mode.
     */
    public void setMode(String mode) {
        _avTable.set(ATTR_MODE, mode);
    }

    /**
     * Set the instrument apertures.
     *
     * Called whenever something changes that causes a change in instrument
     * aperture. Depends on grating and position angle for X and Y and
     * on wavelength for L. Z not changed.
     */
    public void setInstAper() {
        // Need position angle, disperser, wavelength and default InstApers
        double pa = getPosAngleDegrees();
        int di = getDisperserIndex();
        double cwl = getCentralWavelength();

        // Per disperser values of inst. aper zero points are in
        // columns 7 and 8 of the dispersers lut.
        double x0 = Double.valueOf(DISPERSERS.elementAt(di, 7));
        double y0 = Double.valueOf(DISPERSERS.elementAt(di, 8));

        // Might be a small dependence on slit too. Probably small...
        // Ignored for now.

        // Need a functional fit to wavelength and order. (n*lambda vs.
        // inst. aper for a 0 degree slit. ? Probably also a small term.

        double paRad = Angle.degreesToRadians(pa);
        // The  following section of code is replaced by what follows

        double offsetRad = Angle.degreesToRadians(ANGLE_OFFSET);
        double x = x0 + ROTATION_SCALE * (Angle.sinRadians(offsetRad)
                                    - Angle.sinRadians(offsetRad - paRad));
        double y = y0 + ROTATION_SCALE * (Angle.cosRadians(offsetRad)
                                    - Angle.cosRadians(offsetRad - paRad));

        setInstApX(String.valueOf(x));
        setInstApY(String.valueOf(y));
        setInstApZ(INSTRUMENT_APER[ZAP_INDEX]);
        setInstApL(String.valueOf(cwl));

    }

    /**
     * Override the set pos angle methods because we need to know as well -
     * this changes the inst aper.
     *
     * Set the position angle in degrees from due north, updating the
     * observation data with the new position angle.  This method is
     * ultimately called by the other setPosAngle methods.
     */
    public void setPosAngleDegrees(double posAngle) {
        // Confirm to CGS4 restrictions: Can only go from +7 to -173.
        // Also correct the E of N values to be -ve.

        double lolim = 7.0;
        double hilim = 187.0;
        double neglim = -173.0;

        if (getDisperser().equalsIgnoreCase("echelle")) {
            lolim = 50.0;
            hilim = 224.0;
            neglim = -136.0;
        }

        // Reject anything outside the limits
        if ((posAngle <= lolim || posAngle >= hilim) && !(posAngle < neglim)) {
            if (posAngle > lolim) {
                posAngle = posAngle - 360.0;
            }

            posAngle = MathUtil.round(posAngle, 1);
            super.setPosAngleDegrees(posAngle);
            setInstAper();
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
     * Overhead in doing an exposure in seconds.
     *
     * 0.32 seconds for acquisition mode NDSTARE<br>
     * 0.12 seconds for acquisition modes STARE and CHOP.
     */
    public double getExposureOverhead() {
        if (getMode().toUpperCase().indexOf("NDSTARE") > -1) {
            return 0.32;
        }

        return 0.12;
    }

    /**
     * Returns a time estimate in seconds for slewing the telescope
     * for spectroscopy: 8 minutes.
     */
    public double getSlewTime() {
        return 8.0 * 60.0;
    }

    public void processXmlElementContent(String name, String value) {
        if (name.equals(ATTR_CVFOFFSET)) {
            if (centralWavelengthSet) {
                // Convert offset to wavelength
                name = ATTR_CVFWAVELENGTH;
                Double newValue = new Double(getCentralWavelength()
                        + Double.valueOf(value));
                value = newValue.toString();
            }
        } else if (name.equals(ATTR_CENTRAL_WAVELENGTH)) {
            super.processXmlElementContent(name, value);
            if (_avTable.exists(ATTR_CVFOFFSET)
                    && !_avTable.exists(ATTR_CVFWAVELENGTH)) {
                // Convert the offset to a wavelength
                Double newValue = new Double(getCentralWavelength()
                        + getCvfOffset());
                value = newValue.toString();
                super.processXmlElementContent(ATTR_CVFWAVELENGTH, value);
                _avTable.rm(ATTR_CVFOFFSET);
                return;
            } else {
                // We have not yet loaded the cvf offset
                centralWavelengthSet = true;
            }
        }

        super.processXmlElementContent(name, value);
    }

    public Hashtable<String, String> getConfigItems() {
        Hashtable<String, String> t = new Hashtable<String, String>();

        t.put("instrument", "CGS4");
        t.put("version", "TBD");
        t.put("name", "TBD");

        // HACK
        String mode = getMode();
        if (mode.equals("NDSTARE")) {
            mode = "ND_STARE";
        }

        t.put("DAConf", mode);
        t.put("type", "object");
        t.put("exposureTime", "" + getExpTime());
        t.put("coadds", "" + getCoadds());
        t.put("maskWidth", "" + getMaskWidth());

        BigDecimal centralWavelength = new BigDecimal(getCentralWavelength());
        BigDecimal cvfOffset = new BigDecimal(getCvfOffset());
        cvfOffset = cvfOffset.subtract(centralWavelength, new MathContext(6));
        Double doubleOffset = cvfOffset.doubleValue();
        String offset = doubleOffset.toString();

        // Do we have IEEE rounding issues ?
        if (offset.length() > 14) {
            offset = offset.substring(0, 14);
        }

        t.put("cvfOffset", offset);

        t.put("TH-Level", "97");
        t.put("lampAper", "10");
        t.put("flatLamp", "1.3");
        t.put("arcLamp", "argon");
        t.put("scans", "1");

        String[] samplingValues = getSampling().split("x");
        Integer range = Integer.parseInt(samplingValues[1]);
        Integer sampling = Integer.parseInt(samplingValues[0]) * range;
        t.put("sampling", sampling.toString());

        String pixelRange = samplingValues[1] + "_pixel";
        if (range > 1) {
            pixelRange += "s";
        }

        t.put("sampleRange", pixelRange);

        // HACK
        String filter = getFilter().trim();
        if (filter.equals("blank")) {
            filter = "Blanks";
        }

        // HACK
        String polariser = getPolariser();
        if (getNdFilter()) {
            filter += "+ND";
        } else if (!polariser.equals("none")) {
            filter += "+" + polariser;
        }

        t.put("filter", filter);
        t.put("polariser", polariser);
        t.put("neutralDensity", Boolean.toString(getNdFilter()));
        t.put("posAngle", getPosAngleDegreesStr());

        // HACK
        String disperser = getDisperser();
        if (disperser.indexOf("lpmm") != -1) {
            disperser = disperser.substring(0, disperser.indexOf("lpmm"))
                    + "_lpmm";
        } else if (disperser.startsWith("echelle")) {
            disperser = "echelle";
        }

        t.put("disperser", disperser);

        t.put("centralWavelength", "" + getCentralWavelength());
        t.put("order", "" + getOrder());
        t.put("calibLamp", "off");
        setInstAper();
        t.put("instAperX", "" + getInstApX());
        t.put("instAperY", "" + getInstApY());
        t.put("instAperZ", "" + getInstApZ());
        t.put("instAperL", "" + getInstApL());
        t.put("flatSavedInt", "1");
        t.put("darkSavedInt", "1");
        t.put("biasSavedInt", "3");
        t.put("arcSavedInt", "1");

        return t;
    }

    /**
     * Iteration Tracker for CGS4.
     *
     * Added for OMP by MFO, 5 Novemeber 2001
     */
    private class IterTrackerCGS4 extends IterTrackerUKIRT {

        public double getObserveStepTime() {
            // extra_oh is a constant overheads related to certain observe
            // iterators:  30 seconds for dark, arc, flat or bias
            // (in addition to the times to do their respective eye),
            // and 30 secs each time an instrument iterator changes a filter.
            double extra_oh = 0.0;

            if ((currentIterStepItem != null)
                    && ((currentIterStepItem instanceof SpIterBiasObs)
                            || (currentIterStepItem instanceof SpIterDarkObs)
                            || (currentIterStepItem instanceof SpIterCGS4CalObs)
                            || (currentIterStepItem instanceof SpIterConfigObs)))
                extra_oh = 30.0;

            int sampling_x = Integer.valueOf(getSampling().substring(0, 1));
            int sampling_y = Integer.valueOf(getSampling().substring(2, 3));

            return (sampling_x * sampling_y * currentNoCoadds
                        * (currentExposureTime + getExposureOverhead()))
                    + _int_oh + _obs_oh + extra_oh;
        }
    }

    public IterationTracker createIterationTracker() {
        return new IterTrackerCGS4();
    }
}
