/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

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
 * The CGS4 instrument Observation Component.
 *
 * @author Alan Bridger, UKATC
 * @version 1.0
 *
 */
public final class SpInstCGS4 extends SpUKIRTInstObsComp
    
{
    
    // Attributes presented to user
    public static String ATTR_DISPERSER          = "disperser";
    public static String ATTR_MODE               = "acqMode";
    public static String ATTR_CENTRAL_WAVELENGTH = "centralWavelength";
    public static String ATTR_ORDER              = "order";
    public static String ATTR_MASK               = "mask";
    public static String ATTR_SRCMAG             = "sourceMag";
    public static String ATTR_SAMPLING           = "sampling";
    public static String ATTR_POLARISER          = "polariser";
    public static String ATTR_NEUTRAL_DENSITY    = "neutralDensity";
    public static String ATTR_CVFOFFSET          = "cvfOffset";
    
    // Derived attributes: required by instrument, but not presented to user
    public static String ATTR_FILTER             = "filter";
    
    public static String NO_VALUE = "none";
    
    public static double DISP_ARCSEC_PER_PIX  = 0.6;
    public static double SPATIAL_ARCSEC_PER_PIX  = 0.61;
    public static double DISP_ARCSEC_PER_PIX_ECH  = 0.6;
    public static double SPATIAL_ARCSEC_PER_PIX_ECH  = 0.61;
    public static double DISP_ARCSEC_PER_PIX_40  = 0.6;
    public static double SPATIAL_ARCSEC_PER_PIX_40  = 0.61;
    public static double DISP_ARCSEC_PER_PIX_150  = 0.6;
    public static double SPATIAL_ARCSEC_PER_PIX_150  = 0.61;
    
    // Class variable reporesenting defaults, look up tables, etc.
    public static LookUpTable DISPERSERS;
    public static String[] MODES;
    public static String[] SAMPLINGS;
    public static LookUpTable MASKS;
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
    public static double DEFBIASEXPTIME=0.0;
    public static int DEFBIASCOADDS=0;
    public static int CENT_ROW;
    public static int PEAK_ROW;
    
    public static final SpType SP_TYPE = SpType.create(
						       SpType.OBSERVATION_COMPONENT_TYPE, "inst.CGS4", "CGS4");
    
    // Register the prototype.
    static {
	SpFactory.registerPrototype(new SpInstCGS4());
    }
    
    /** 
     * Constructor reads instrument .cfg file and initialises values
     */
    public SpInstCGS4()
    {
	super(SP_TYPE);
	addCapability( new SpChopCapability()  );
	addCapability( new SpStareCapability() );
	
	// Read in the instrument config file
	String baseDir = System.getProperty("ot.cfgdir");
	String cfgFile = baseDir + "cgs4.cfg";
	_readCfgFile (cfgFile);
	
	// Set the initial values of the attributes
	String attr  = ATTR_DISPERSER;
	String value = DEFAULT_DISPERSER;
	_avTable.noNotifySet(attr, value, 0);
	
	attr  = ATTR_CENTRAL_WAVELENGTH;
	_avTable.noNotifySet(attr, "1.0", 0);
	
	attr  = ATTR_ORDER;
	_avTable.noNotifySet(attr, "0", 0);
	
	attr  = ATTR_MASK;
	value = (String) MASKS.elementAt(0,0);
	_avTable.noNotifySet(attr, value, 0);
	
	attr  = ATTR_SRCMAG;
	value = SRCMAGS[0];
	_avTable.noNotifySet(attr, value, 0);
	
	attr  = ATTR_SAMPLING;
	value = SAMPLINGS[0];
	_avTable.noNotifySet(attr, value, 0);
	
	attr  = ATTR_POLARISER;
	value = POLARISERS[0];
	_avTable.noNotifySet(attr, value, 0);
	
	attr  = ATTR_NEUTRAL_DENSITY;
	_avTable.noNotifySet(attr, "0", 0);
	
	attr  = ATTR_CVFOFFSET;
	_avTable.noNotifySet(attr, "0.0", 0);
	
	attr  = ATTR_MODE;
	value = DEFAULT_MODE;
	_avTable.noNotifySet(attr, value, 0);
	
	attr  = ATTR_EXPOSURE_TIME;
	_avTable.noNotifySet(attr, "0", 0);
	
	attr  = ATTR_COADDS;
	_avTable.noNotifySet(attr, "1", 0);
	
	attr  = ATTR_FILTER;
	value = NO_VALUE;
	_avTable.noNotifySet(attr, value, 0);
	
    }
    
    private void _readCfgFile (String filename) {
	
	InstCfgReader instCfg = null;
	InstCfg instInfo = null;
	String block = null;
	
	instCfg = new InstCfgReader (filename);
	try {
	    while ((block = instCfg.readBlock()) != null) {
		instInfo = new InstCfg (block);
		if (InstCfg.matchAttr (instInfo, "dispersers")) {
		    DISPERSERS = instInfo.getValueAsLUT();
		    // insert the order tables
		    try {
			int row = DISPERSERS.indexInColumn("ORDERS1", 2);
			DISPERSERS.setElementAt (ORDERS1, row, 2);
			row = DISPERSERS.indexInColumn("ORDERS2", 2);
			DISPERSERS.setElementAt (ORDERS2, row, 2);
			row = DISPERSERS.indexInColumn("ORDERS3", 2);
			DISPERSERS.setElementAt (ORDERS3, row, 2);
			row = DISPERSERS.indexInColumn("EXPTIMES150", 3);
			DISPERSERS.setElementAt (EXPTIMES150, row, 3);
			row = DISPERSERS.indexInColumn("EXPTIMES40", 3);
			DISPERSERS.setElementAt (EXPTIMES40, row, 3);
			row = DISPERSERS.indexInColumn("EXPTIMESECH", 3);
			DISPERSERS.setElementAt (EXPTIMESECH, row, 3);
			row = DISPERSERS.indexInColumn("FILTERS150", 4);
			DISPERSERS.setElementAt (FILTERS150, row, 4);
			row = DISPERSERS.indexInColumn("FILTERS40", 4);
			DISPERSERS.setElementAt (FILTERS40, row, 4);
			row = DISPERSERS.indexInColumn("FILTERSECH", 4);
			DISPERSERS.setElementAt (FILTERSECH, row, 4);
		    }catch (NoSuchElementException ex) {
			System.out.println ("Error indexing in dispersers table in CGS4 cfg file");
			System.out.println (ex);
		    }catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println ("Unexpected error setting CGS4 luts");
			System.out.println (ex);
		    }
		}else if (InstCfg.matchAttr (instInfo, "modes")) {
		    MODES = instInfo.getValueAsArray();
		}else if (InstCfg.matchAttr (instInfo, "samplings")) {
		    SAMPLINGS = instInfo.getValueAsArray();
		}else if (InstCfg.matchAttr (instInfo, "masks")) {
		    MASKS = instInfo.getValueAsLUT();
		}else if (InstCfg.matchAttr (instInfo, "polarisers")) {
		    POLARISERS = instInfo.getValueAsArray();
		}else if (InstCfg.matchAttr (instInfo, "magnitudes")) {
		    SRCMAGS = instInfo.getValueAsArray();
		}else if (InstCfg.matchAttr (instInfo, "filters")) {
		    FILTERS = instInfo.getValueAsArray();
		}else if (InstCfg.matchAttr (instInfo, "default_disperser")) {
		    DEFAULT_DISPERSER = instInfo.getValue();
		}else if (InstCfg.matchAttr (instInfo, "default_mode")) {
		    DEFAULT_MODE = instInfo.getValue();
		} else if (InstCfg.matchAttr (instInfo, "DEFBIASEXPTIME")) {
		    String dbet = instInfo.getValue();
		    try {
			Double tmp = Double.valueOf( dbet );
			DEFBIASEXPTIME = tmp.doubleValue();
		    } catch ( Exception ex ) {}
		} else if (InstCfg.matchAttr (instInfo, "DEFBIASCOADDS")) {
		    DEFBIASCOADDS = Integer.parseInt(instInfo.getValue());
		}else if (InstCfg.matchAttr (instInfo, "detector_size")) {
		    DETECTOR_SIZE = instInfo.getValueAsArray();
		    DETECTOR_WIDTH = Integer.parseInt(DETECTOR_SIZE[0]);
		    DETECTOR_HEIGHT = Integer.parseInt(DETECTOR_SIZE[1]);
		}else if (InstCfg.matchAttr (instInfo, "orders1")) {
		    ORDERS1 = instInfo.getValueAsLUT();
		}else if (InstCfg.matchAttr (instInfo, "orders2")) {
		    ORDERS2 = instInfo.getValueAsLUT();
		}else if (InstCfg.matchAttr (instInfo, "orders3")) {
		    ORDERS3 = instInfo.getValueAsLUT();
		}else if (InstCfg.matchAttr (instInfo, "exptimes150")) {
		    EXPTIMES150 = instInfo.getValueAsLUT();
		}else if (InstCfg.matchAttr (instInfo, "exptimes40")) {
		    EXPTIMES40 = instInfo.getValueAsLUT();
		}else if (InstCfg.matchAttr (instInfo, "exptimesech")) {
		    EXPTIMESECH = instInfo.getValueAsLUT();
		}else if (InstCfg.matchAttr (instInfo, "filters150")) {
		    FILTERS150 = instInfo.getValueAsLUT();
		}else if (InstCfg.matchAttr (instInfo, "filters40")) {
		    FILTERS40 = instInfo.getValueAsLUT();
		}else if (InstCfg.matchAttr (instInfo, "filtersech")) {
		    FILTERSECH = instInfo.getValueAsLUT();
		}else if (InstCfg.matchAttr (instInfo, "instrument_port")) {
		    INSTRUMENT_PORT = instInfo.getValue();
		    setPort (INSTRUMENT_PORT);
		}else if (InstCfg.matchAttr (instInfo, "instrument_aper")) {
		    INSTRUMENT_APER = instInfo.getValueAsArray();
		    setInstApX (INSTRUMENT_APER[XAP_INDEX]);
		    setInstApY (INSTRUMENT_APER[YAP_INDEX]);
		    setInstApZ (INSTRUMENT_APER[ZAP_INDEX]);
		    setInstApL (INSTRUMENT_APER[LAP_INDEX]);
		} else if (InstCfg.matchAttr (instInfo, "CENT_ROW")) {
		    CENT_ROW = Integer.parseInt(instInfo.getValue());
		} else if (InstCfg.matchAttr (instInfo, "PEAK_ROW")) {
		    PEAK_ROW = Integer.parseInt(instInfo.getValue());
		}
	    }
	}catch (IOException e) {
	    System.out.println ("Error reading CGS4 inst. cfg file");
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
     * Return the science area based upon the current mask.
     */
    public double[]
	getScienceArea()
    {
	double diArcsecPerPix;
	double spArcsecPerPix;
	
	diArcsecPerPix = DISP_ARCSEC_PER_PIX;
	spArcsecPerPix = SPATIAL_ARCSEC_PER_PIX;
	
	double[] size = new double[2];
	double      w = getMaskWidth();
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
     * Set the exposure time.
     */
    public void
	setExpTime(String seconds)
    {
	String timeAsStr;
	
	timeAsStr = seconds;
	if (timeAsStr == null) {
	    double time = getDefaultExpTime ();
	    timeAsStr = Double.toString (time);
	}
	setExposureTime (timeAsStr);
    }
    
    /**
     * Get the default exposure time, depends on disperser, source mag and 
     * waveband.
     */
    public double
	getDefaultExpTime()
    {
	double exptime = 0.0;
	int di = getDisperserIndex();
	String sm = getSourceMagnitude();
	double cwl = getCentralWavelength();
	double sw = getMaskWidth();
	
	try {
	    // get the default exp. time lut
	    LookUpTable delut = (LookUpTable) DISPERSERS.elementAt(di,3);
	    
	    // determine the row from the cwl
	    int row = delut.rangeInColumn(cwl, 0);
	    
	    // determine the column from the source mag.
	    int column = delut.indexInRow(sm, 0);
	    
	    // and get the element (=default exp. time)
	    Double et = Double.valueOf((String)delut.elementAt(row, column));
	    // Divide by mask width.
	    exptime = et.doubleValue()/sw;
	    
	}catch (ArrayIndexOutOfBoundsException ex) {
	    exptime = 0.0;
	}catch (NoSuchElementException ex) {
	    exptime = 0.0;
	}catch (NumberFormatException ex) {
	    System.out.println("Error converting default exp. time");
	    exptime = 0.0;
	}
	return exptime;
    }
    
    /**
     * Get the default bias exposure time
     */
    public double getDefaultBiasExpTime() {
	
	if ( DEFBIASEXPTIME == 0.0 ) {
	    return super.getDefaultBiasExpTime();
	}
	return DEFBIASEXPTIME;
    }
    /**
     * Get the default bias coadds
     */
    public int getDefaultBiasCoadds() {
	
	if ( DEFBIASCOADDS == 0 ) {
	    return super.getDefaultBiasCoadds();
	}
	return DEFBIASCOADDS;
    }
    
    /**
     * Get the coadds.
     */
    public int
	getNoCoadds()
    {
	
	int coadds = getStareCapability().getCoadds();
	if (coadds == 0) {
	    coadds = getDefaultCoadds();
	    setNoCoadds (coadds);
	}
	return coadds;
    }
    
    /**
     * Set the coadds
     */
    public void
	setNoCoadds(int coadds)
    {
	if (coadds == 0) coadds = getDefaultCoadds();
	getStareCapability().setCoadds (coadds);
    }
    
    /**
     * Set no coadds as a string.
     */
    public void
	setNoCoadds(String coadds)
    {
	int c = 0;
	try {
	    Integer tmp = Integer.valueOf(coadds);
	    c = tmp.intValue();
	} catch (Exception ex) {}
	
	setNoCoadds(c);
    }
    
    /**
     * Get the default coadds, depends on exp. time
     */
    public int
	getDefaultCoadds()
    {
	float et = (float) getExpTime();
	if (et == 0.0) return 1;
	int coadds = 0;
	try {
	    float f = (float) (5.0/et + 0.5);
	    coadds = Math.round (f);
	} catch (Exception ex) {
	    System.out.println ("Error calculating default no. coadds");
	}
	return (int) coadds;
    }
    
    /**
     * Get the exp/chop pos
     */
    public int
	getExpPerChopPos()
    {
	
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
    public void
	setExpPerChopPos(int expPCP)
    {
	if (expPCP == 1 || expPCP ==0) expPCP = getDefaultCoadds();
	getChopCapability().setExposuresPerChopPosition (expPCP);
    }
    
    /**
     * Set exp/chop pos as a string.
     */
    public void
	setExpPerChopPos(String expPCP)
    {
	int c = 0;
	try {
	    Integer tmp = Integer.valueOf(expPCP);
	    c = tmp.intValue();
	} catch (Exception ex) {}
	
	setExpPerChopPos(c);
    }
    
    /**
     * Get the cycles/obs
     */
    public int
	getCyclesPerObs()
    {
	
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
    public void
	setCyclesPerObs(int cycPO)
    {
	if (cycPO == 0) cycPO = getDefaultCyclesPerObs();
	getChopCapability().setCyclesPerObserve (cycPO);
    }
    
    /**
     * Set cycles/obs as a string.
     */
    public void
	setCyclesPerObs(String cycPO)
    {
	int c = 0;
	try {
	    Integer tmp = Integer.valueOf(cycPO);
	    c = tmp.intValue();
	} catch (Exception ex) {}
	
	setCyclesPerObs(c);
    }
    
    /**
     * Get the default cycles/obs
     */
    public int
	getDefaultCyclesPerObs()
    {
	return 1;
    }
    
    /**
     * Get the source magnitude
     */
    public String
	getSourceMagnitude()
    {
	
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
    public void
	setSourceMagnitude(String sm)
    {
	_avTable.set(ATTR_SRCMAG, sm);
    }
    
    /**
     * Get the sampling
     */
    public String
	getSampling()
    {
	
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
    public void
	setSampling(String sam)
    {
	_avTable.set(ATTR_SAMPLING, sam);
    }

    /**
     * Get the disperser (aka grating).
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
     * Set the disperser.
     */
    public void
	setDisperser(String disperser)
    {
	_avTable.set(ATTR_DISPERSER, disperser);
	
	// When the disperser changes so will the instrument aperture
	setInstAper();
    }
    
    /**
     * Get the index of the current disperser.
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
     * Get the dispserser R.
     */
    public int
	getDisperserR()
    {
	int di = getDisperserIndex();
	try 
	    {return Integer.parseInt((String) DISPERSERS.elementAt(di,1));
	    } catch (NumberFormatException e) {}
	return 0;
    }
    
    /**
     * Set the order.
     */
    public void
	setOrder(int order)
    {
	int o;
	
	o = order;
	if (o == 0) o= getDefaultOrder();
	_avTable.set (ATTR_ORDER, o);
	
	// When the order changes so will the instrument aperture - one day
	setInstAper();
	
    }
    
    /**
     * Set the order as a string.
     */
    public void
	setOrder(String order)
    {
	int o = 0;
	try {
	    Integer tmp = Integer.valueOf(order);
	    o = tmp.intValue();
	} catch (Exception ex) {}
	
	setOrder(o);
    }
    
    /**
     * Get the order.
     */
    public int
	getOrder()
    {
	int o = _avTable.getInt(ATTR_ORDER, 0);
	if (o == 0) {
	    o = getDefaultOrder();
	    setOrder (Integer.toString(o));
	}
	return o;
    }
    
    /**
     * Get the default order using the current wavelength and disperser.
     */
    public int
	getDefaultOrder()
    {
	int order = 0;
	
	// get disperser index, then look up value in lut
	int di = getDisperserIndex();
	double cwl = getCentralWavelength();
	
	try {
	    LookUpTable dolut = (LookUpTable) DISPERSERS.elementAt(di,2);
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
     * Get the central wavelength.
     */
    public double
	getCentralWavelength()
    {
	double cwl = _avTable.getDouble(ATTR_CENTRAL_WAVELENGTH, 0.0);
	return cwl;
    }
    
    /**
     * Set the central wavelength.
     */
    public void
	setCentralWavelength(double cwl)
    {
	_avTable.set(ATTR_CENTRAL_WAVELENGTH, cwl);
	
	// When the wavelength changes so will the instrument aperture
	setInstAper();
	
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
     * Use default order.
     */
    public void
	useDefaultOrder()
    {
	_avTable.rm(ATTR_ORDER);
	
    }
    
    /**
     * Use default acquisition
     */
    public void
	useDefaultAcquisition()
    {
	_avTable.rm(ATTR_EXPOSURE_TIME);
	_avTable.rm(ATTR_COADDS);
	_avTable.rm(ATTR_EXPOSURES_PER_CHOP_POSITION);
	_avTable.rm(ATTR_CYCLES_PER_OBSERVE);
    }
    
    /**
     * Calculate the wavelength coverage, based upon the disperser and
     * central wavelength.  0 is returned if there is no disperser selected.
     */
    public double []
	getWavelengthCoverage()
    {
	double range[] = new double[2];
	int r = getDisperserR();
	if (r == 0) {
	    range[0] = 0.0;
	    range[1] = 0.0;
	}else{
	    double cw = getCentralWavelength();
	    double wr = DETECTOR_WIDTH * cw / r;
	    range[0] = cw - wr / 2.0;
	    range[1] = cw + wr / 2.0;
	}
	return range;
    }
    
    /**
     * Calculate the spectral resolution, based upon the disperser, order and
     * central wavelength.  0 is returned if there is no disperser selected.
     */
    public double
	getResolution()
    {
	double res;
	int r = getDisperserR();
	if (r == 0) {
	    res = 0.0;
	}else{
	    int o = getOrder();
	    double cw = getCentralWavelength();
	    res = o * r * cw/2.0;
	}
	return res;
    }
    
    /**
     * Get the CVF offset
     */
    public double
	getCvfOffset()
    {
	double cvfo = _avTable.getDouble(ATTR_CVFOFFSET, 0.0);
	return cvfo;
    }
    
    /**
     * Set the CVF offset
     */
    public void
	setCvfOffset(double cvfo)
    {
	_avTable.set(ATTR_CVFOFFSET, cvfo);
    }
    
    /**
     * Set the CVF offset as a string
     */
    public void
	setCvfOffset(String cvfo)
    {
	double d = 0.0;
	try {
	    Double tmp = Double.valueOf(cvfo);
	    d = tmp.doubleValue();
	} catch (Exception ex) {}
	
	_avTable.set(ATTR_CVFOFFSET, d);
    }
    
    /**
     * Get the ND filter use
     */
    public boolean
	getNdFilter()
    {
	boolean nd = _avTable.getBool(ATTR_NEUTRAL_DENSITY);
	return nd;
    }
    
    /**
     * Set the ND filter use
     */
    public void
	setNdFilter(boolean nd)
    {
	_avTable.set(ATTR_NEUTRAL_DENSITY, nd);
    }
    
    /**
     * Get the mask, aka slit.
     */
    public String
	getMask()
    {
	String mask = _avTable.get(ATTR_MASK);
	if (mask == null) {
	    try {
		mask = (String) MASKS.elementAt(0,0);
	    }catch (Exception ex) {}
	}
	return mask;
    }
    
    /**
     * Set the mask.
     */
    public void
	setMask(String mask)
    {
	_avTable.set(ATTR_MASK, mask);
	
	// When the mask changes so will the instrument aperture - one day
	setInstAper();
	
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
     * Get the mask width for the current mask.
     */
    public double
	getMaskWidth()
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
     * Get the polariser
     */
    public String
	getPolariser()
    {
	String pol = _avTable.get(ATTR_POLARISER);
	if (pol == null) {
	    pol = POLARISERS[0];
	}
	return pol;
    }
    
    /**
     * Set the polariser.
     */
    public void
	setPolariser(String polariser)
    {
	_avTable.set(ATTR_POLARISER, polariser);
    }
    
    /**
     * Get the filter.
     */
    public String
	getFilter()
    {
	String filter = _avTable.get(ATTR_FILTER);
	if (filter == null) {
	    filter = getDefaultFilter();
	    setFilter (filter);
	}
	return filter;
    }
    
    /**
     * Get the default filter, depending on the disperser, order and wavelength
     */
    public String
	getDefaultFilter()
    {
	String filter="NONE";
	
	int di = getDisperserIndex();
	int o = getOrder();
	String os = Integer.toString(o);
	double cwl = getCentralWavelength();
	
	try {
	    // get the default filter lut
	    LookUpTable fillut = (LookUpTable) DISPERSERS.elementAt(di,4);
	    
	    // determine the row from the cwl
	    int row = fillut.rangeInColumn(cwl, 0);
	    
	    // determine the column from the order - check against no. of 
	    // columnms because if not all orders are specified then use 
	    // the _last_ as a default
	    int numCols = fillut.getNumColumns();
	    int column = numCols-1;
	    try {
		column = fillut.indexInRow(os, 0);
	    } catch (NoSuchElementException e) {}
	    
	    // and get the element (=default filter), catching any 
	    // failures from above
	    filter = (String) fillut.elementAt(row, column);
	}catch (Exception ex) {
	    System.out.println 
		("Caught exception looking for default filter: "+ex);
	}
	return filter;
    }
    
    /**
     * Set the filter.
     */
    public void
	setFilter(String filter)
    {
	String f;
	
	f = filter;
	if (f == null) f = getDefaultFilter();
	_avTable.set(ATTR_FILTER, f);
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
     * Set the mode.
     */
    public void
	setMode(String mode)
    {
	_avTable.set(ATTR_MODE, mode);
    }
    
    /**
     * Set the instrument apertures. Called whenever something changes that 
     * causes a change in instrument aperture. Depends on grating and position
     * angle for X and Y and on wavelength for L. Z not changed.
     */
    public void
	setInstAper()
    {
	// Need position angle, disperser, wavelength and default InstApers
	double pa = getPosAngleDegrees();
	int di = getDisperserIndex();
	double cwl = getCentralWavelength();
	
	// Per disperser values of inst. aper zero points are in
	// columns 7 and 8 of the dispersers lut.
	double x0 = 
	    (Double.valueOf((String)DISPERSERS.elementAt(di,7))).doubleValue();
	double y0 = 
	    (Double.valueOf((String)DISPERSERS.elementAt(di,8))).doubleValue();
	
	System.out.println ("X,Y zero points are " + x0 + ", "+y0);

	// Spectral scale is in column 5 of the dispersers lut, and spatial in
	// column 6.
	double spatialScale = 
	    (Double.valueOf((String)DISPERSERS.elementAt(di,5))).doubleValue();
	double spectralScale = 
	    (Double.valueOf((String)DISPERSERS.elementAt(di,6))).doubleValue();
	
	// Might be a small dependence on slit too. Probably small...
	// Ignored for now.
	
	// Need a functional fit to wavelength and order. (n*lambda vs.
	// inst. aper for a 0 degree slit. ? Probably also a small term.
	
	// Get distance between peak and central in mm. Remember 
	// there's a minus sign in YARCSECPERMM.
	double d = - (spatialScale/YARCSECPERMM)*(PEAK_ROW-CENT_ROW);
	
	//System.out.println ("Calculated d is "+d);
	
	double paRad = Angle.degreesToRadians (pa);
	double x = x0 + d*(Angle.sinRadians(paRad));
	double y = y0 - d*(Angle.cosRadians(paRad)-1);
	System.out.println ("New X,Y are " + x + ", "+y);
	setInstApX (String.valueOf(x));
	setInstApY (String.valueOf(y));
	setInstApL (String.valueOf(cwl));
	
    }
    
    
    /**
     * Override the set pos angle methods because we need to know as well -
     * this changes the inst aper.
     * Set the position angle in degrees from due north, updating the
     * observation data with the new position angle.  This method is
     * ultimately called by the other setPosAngle methods.
     */
    public void	setPosAngleDegrees(double posAngle)
    {
	
	// Confirm to CGS4 restrictions: Can only go from +7 to -173.  Also correct
	// the E of N values to be -ve.
	double lolim = 7.0;
	double hilim = 187.0;
	double neglim = -173.0;
	if (getDisperser().equalsIgnoreCase("echelle")) {
	    lolim = 44.0;
	    hilim = 224.0;
	    neglim = -136.0;
	}
	
	// Reject anything outside the limits
	if ( (posAngle <= lolim || posAngle >= hilim) && !(posAngle < neglim) ) {
	    if (posAngle > lolim ) {
		posAngle = posAngle - 360.0;
	    }
	    super.setPosAngleDegrees (posAngle);
	    setInstAper();
	}
    }
    
    /**
     * Set the position angle in radians from due north.
     */
    public void
	setPosAngleRadians(double posAngle)
    {
	this.setPosAngleDegrees(Angle.radiansToDegrees(posAngle));
    }
    
    /**
     * Set the rotation of the science area as a string (representing degrees).
     */
    public void
	setPosAngleDegreesStr(String posAngleStr)
    {
	double posAngle = 0.0;
	try {
	    Double pa = Double.valueOf(posAngleStr);
	    posAngle = pa.doubleValue();
	}catch (NumberFormatException e) {
	    System.out.println ("Error converting string angle to double");
	}
	this.setPosAngleDegrees(posAngle);
    }
    
    
}
