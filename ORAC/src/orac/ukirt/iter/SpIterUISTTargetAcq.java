// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter;

import java.util.*;

import orac.util.LookUpTable;
import orac.ukirt.inst.SpInstUIST;

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import gemini.sp.obsComp.SpInstConstants;

import gemini.util.*;

/**
 * Enumerater for the elements of the Observe iterator.
 */
class SpIterUISTTargetAcqEnumeration extends SpIterEnumeration {

    private int _curCount = 0;
    private int _maxCount;
    private SpIterValue[] _values;
    
    SpIterUISTTargetAcqEnumeration(SpIterUISTTargetAcq iterObserve)
    {
	super(iterObserve);
	_maxCount    = iterObserve.getCount();
    }
    
    protected boolean
	_thisHasMoreElements()
    {
	return (_curCount < _maxCount);
    }

    protected SpIterStep
	_thisFirstElement()
    {
	SpIterUISTTargetAcq ibo   = (SpIterUISTTargetAcq) _iterComp;

	ibo.useDefaultDisperser();

	String coaddsValue  = String.valueOf(ibo.getCoadds());
	String maskWidthValue  = String.valueOf(ibo.getMaskWidthPixels());
	String maskHeightValue  = String.valueOf(ibo.getMaskHeightArcsec());
	String dispersionValue  = String.valueOf(ibo.getDispersion());
	String resolutionValue  = String.valueOf(ibo.getResolution());
	
	//System.out.println("SpIterUISTTargetAcq._thisFirstElement expTime " +  ibo.getExposureTimeString() + " coadds " + coaddsValue + " disperser " + ibo.getDisperser());
	_values = new SpIterValue[11];
	_values[0] = new SpIterValue(SpInstConstants.ATTR_EXPOSURE_TIME, ibo.getExposureTimeString());
	_values[1] = new SpIterValue(SpInstConstants.ATTR_COADDS, coaddsValue);
	_values[2] = new SpIterValue(SpUISTTargetAcqConstants.ATTR_FILTER, ibo.getFilter());
	_values[3] = new SpIterValue(SpUISTTargetAcqConstants.ATTR_MASK, ibo.getMask());
	_values[4] = new SpIterValue(SpUISTTargetAcqConstants.ATTR_MASK_WIDTH, maskWidthValue);
	_values[5] = new SpIterValue(SpUISTTargetAcqConstants.ATTR_MASK_HEIGHT, maskHeightValue);
	_values[6] = new SpIterValue(SpUISTTargetAcqConstants.ATTR_DISPERSER, ibo.getDisperser());
	_values[7] = new SpIterValue(SpUISTTargetAcqConstants.ATTR_DISPERSION, dispersionValue);
	_values[8] = new SpIterValue(SpUISTTargetAcqConstants.ATTR_RESOLUTION, resolutionValue);
	_values[9] = new SpIterValue(SpUISTTargetAcqConstants.ATTR_SCIENCE_AREA, ibo.getScienceAreaString());
	_values[10] = new SpIterValue(SpUISTTargetAcqConstants.ATTR_SOURCE_MAG, ibo.getSourceMag());
	
	return _thisNextElement();
    }
    
    protected SpIterStep
	_thisNextElement()
    {
	return new SpIterStep("TargetAcq", _curCount++, _iterComp, _values);
    }
    
}


public class SpIterUISTTargetAcq extends SpIterObserveBase
{
    public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "instUISTTargetAcq", "UIST Spec/IFU Target Acquisition");
    
    // Register the prototype.
    static {
	SpFactory.registerPrototype(new SpIterUISTTargetAcq());
    }


    /**
     * Default constructor.
     */
    public SpIterUISTTargetAcq() {
	super(SP_TYPE);
	_avTable.noNotifySet(SpUISTTargetAcqConstants.ATTR_MASK,null,0);
	_avTable.noNotifySet(SpUISTTargetAcqConstants.ATTR_MASK_WIDTH,null,0);
	_avTable.noNotifySet(SpUISTTargetAcqConstants.ATTR_DISPERSER,null,0);
	_avTable.noNotifySet(SpUISTTargetAcqConstants.ATTR_DISPERSION,null,0);
	_avTable.noNotifySet(SpUISTTargetAcqConstants.ATTR_RESOLUTION,null,0);
	_avTable.noNotifySet(SpUISTTargetAcqConstants.ATTR_FILTER,null,0);
	_avTable.noNotifySet(SpUISTTargetAcqConstants.ATTR_SCIENCE_AREA,null,0);
	_avTable.noNotifySet(SpUISTTargetAcqConstants.ATTR_SOURCE_MAG,null,0);
	_avTable.noNotifySet(SpInstConstants.ATTR_EXPOSURE_TIME,null,0);
	_avTable.noNotifySet(SpInstConstants.ATTR_COADDS,null,0);
	
    }
    
    /**
     * Use default acquisition
     */
    public void
	useDefaultAcquisition()
    {
	_avTable.rm(SpInstConstants.ATTR_EXPOSURE_TIME);
	_avTable.rm(SpInstConstants.ATTR_COADDS);
    }
    
    /**
     * Use default disperser
     */
    public void
	useDefaultDisperser()
    {
	_avTable.rm(SpUISTTargetAcqConstants.ATTR_DISPERSER);
	_avTable.rm(SpUISTTargetAcqConstants.ATTR_MASK);
	_avTable.rm(SpUISTTargetAcqConstants.ATTR_MASK_WIDTH);
	_avTable.rm(SpUISTTargetAcqConstants.ATTR_DISPERSION);
	_avTable.rm(SpUISTTargetAcqConstants.ATTR_RESOLUTION);
	_avTable.rm(SpUISTTargetAcqConstants.ATTR_FILTER);
	_avTable.rm(SpUISTTargetAcqConstants.ATTR_SCIENCE_AREA);
    }
    
    /**
     */
    public SpIterEnumeration
	elements()
    {
	return new SpIterUISTTargetAcqEnumeration(this);
    }
    
    /**
     * Get the instrument item in the scope of the base item.
     */
    public SpInstObsComp
	getInstrumentItem()
    {
	SpItem _baseItem = parent();
	return (SpInstObsComp) SpTreeMan.findInstrument(_baseItem);
    }
    
    /**
     * Get the exposure time
     */
    public String
	getExposureTimeString() 
    {
	String exposureTimeString = _avTable.get(SpInstConstants.ATTR_EXPOSURE_TIME);
	double exposureTime = 0.0;
	if (exposureTimeString == null) {
	    
	    String mag = getSourceMag();
	    
	    String disperser = getDisperser();
	    int row = SpInstUIST.SPECMAGS.indexInColumn(disperser,0);
	    int column = SpInstUIST.SPECMAGS.indexInRow(mag,0);
	    exposureTime = Double.valueOf((String)SpInstUIST.SPECMAGS.elementAt(row,column))
		.doubleValue();
	    
	    setExposureTime(exposureTime);
	} else {
	    exposureTime = Double.valueOf(exposureTimeString).doubleValue();
	}
	
	return Double.toString(exposureTime);
	
    }
    
    /**
     * Get the filter 
     */
    public String
	getFilter() 
    {
	String filter = _avTable.get(SpUISTTargetAcqConstants.ATTR_FILTER);
	if (filter == null) {
	    
            int di = getDisperserIndex();
            String OTFilter = (String) SpInstUIST.DISPERSERS.elementAt(di,2);
            int findex = SpInstUIST.SPECTFILTERS.indexInColumn(OTFilter, 0);
            filter = (String) SpInstUIST.SPECTFILTERS.elementAt(findex,1);

	    setFilter(filter);
	}
	return filter;
    }

    /**
     * Update the Filter value in the attribute-value table
     */
    public void
	setFilter(String filter)
    {
	
	_avTable.set(SpUISTTargetAcqConstants.ATTR_FILTER, filter);
    }
    
    /**
     * Get the disperser 
     */
    public String
	getDisperser() 
    {
	String targetAcqDisperser = _avTable.get(SpUISTTargetAcqConstants.ATTR_DISPERSER);
	if (targetAcqDisperser == null) {
	    
	    SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
	    String disperser = inst.getDisperser();
	    
	    String dispersers[] = inst.getDisperserList();
	    int dispIndex = 0;
	    for (dispIndex = 0; dispIndex < dispersers.length; dispIndex++) {
		if (disperser.equalsIgnoreCase(dispersers[dispIndex])) {
		    break;
		}
	    }
	    
	    if (dispIndex < dispersers.length) {
		
		if (inst.isIFU()) {
		    if (dispIndex < SpInstUIST.DISPERSER_CHOICES_IFU_ACQ.length) {
			targetAcqDisperser = SpInstUIST.DISPERSER_CHOICES_IFU_ACQ[dispIndex];
		    }
		} else {
		    if (inst.isPolarimetry()) {
			if (dispIndex < SpInstUIST.DISPERSER_CHOICES_POL_ACQ.length) {
			    targetAcqDisperser  = SpInstUIST.DISPERSER_CHOICES_POL_ACQ[dispIndex];
			}
		    } else {
			if (dispIndex < SpInstUIST.DISPERSER_CHOICES_ACQ.length) {
			    targetAcqDisperser = SpInstUIST.DISPERSER_CHOICES_ACQ[dispIndex];
			}
		    }
		}
	    } 
	}
	
	setDisperser(targetAcqDisperser);
	return targetAcqDisperser;
	
    }

    /**
     * Update the Disperser value in the attribute-value table
     */
    public void
	setDisperser(String disperser)
    {
	
	_avTable.set(SpUISTTargetAcqConstants.ATTR_DISPERSER, disperser);
    }
    
    /**
     * Get the dispersion 
     */
    public double
	getDispersion() 
    {
        double dispersion = 0.0;
	String ds = _avTable.get(SpUISTTargetAcqConstants.ATTR_DISPERSION);
	if (ds == null) {
	    
            int di = getDisperserIndex();
            dispersion = Double.valueOf((String)SpInstUIST.DISPERSERS.elementAt(di,1)).doubleValue();

	    setDispersion(dispersion);
	}
	return dispersion;
    }

    /**
     * Update the dispersion value in the attribute-value table
     */
    public void
	setDispersion(double dispersion)
    {
	
	_avTable.set(SpUISTTargetAcqConstants.ATTR_DISPERSION, dispersion);
    }
    
    /**
     * Get the resolution 
     */
    public double
	getResolution() 
    {
	double resolution = 0.0;
	String rs = _avTable.get(SpUISTTargetAcqConstants.ATTR_RESOLUTION);
	if (rs == null) {
	    
            resolution = 0.0;

	    setResolution(resolution);
	}
	return resolution;
    }

    /**
     * Update the resolution value in the attribute-value table
     */
    public void
	setResolution(double resolution)
    {
	
	_avTable.set(SpUISTTargetAcqConstants.ATTR_RESOLUTION, resolution);
    }
    
    /**
     * Get the science area as an array
     */
    public double[]
	getScienceArea() 
    {
	SpInstUIST inst =  (SpInstUIST) getInstrumentItem();

	double fov[] = new double[2];
	double pixelScale = inst.getPixelScale();
	int ra[] = inst.getReadArea();
	double maxWidth  = ra[0] * pixelScale;
	double maxHeight = ra[1] * pixelScale;
	fov[0] = getMaskWidthPixels() * pixelScale;;
	fov[1] = getMaskHeightArcsec();
	
	// Adjust the height and width if vignetted by the readout area
	if (fov[0] > maxWidth) fov[0] = maxWidth;
	if (fov[1] > maxHeight) fov[1] = maxHeight;
	
	return fov;
    }

    /**
     * Get the science area as a string
     */
    public String
	getScienceAreaString() 
    {
	    
        double fov[];
        String fovs;
        fov = getScienceArea();
	double w = MathUtil.round(fov[0], 2);
	double h = MathUtil.round(fov[1], 2);
	fovs = w + " x " + h + " arcsec";

        setScienceArea(fovs);

        return fovs;
    }

    /**
     * Update the science area value in the attribute-value table
     */
    public void
	setScienceArea(String scienceArea)
    {
	
	_avTable.set(SpUISTTargetAcqConstants.ATTR_SCIENCE_AREA, scienceArea);
    }
    
    /**
     * Use default source magnitude
     */
    public void
    useDefaultSourceMag()
    {
        _avTable.rm(SpUISTTargetAcqConstants.ATTR_SOURCE_MAG);
    }

    /**
     * Get the source magnitude 
     */
    public String
	getSourceMag()
    {
	String sm = _avTable.get(SpUISTTargetAcqConstants.ATTR_SOURCE_MAG);

	if (sm == null) {
	    SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
	    int ciInstSm = SpInstUIST.SPECMAGS.indexInRow(inst.getSourceMag(), 0);
	    int ciMinMag = SpInstUIST.SPECMAGS.indexInRow(SpInstUIST.MIN_MAG_TRAGET_ACQ, 0);
	    int ci = Math.max(ciInstSm, ciMinMag);
	    sm = (String)SpInstUIST.SPECMAGS.elementAt(0,ci);
	    setSourceMag(sm);
	}
	
	return sm;
    }
    
    /**
     * Set the source magnitude.
     */
    public void setSourceMag( String sm ) {
        _avTable.set( SpUISTTargetAcqConstants.ATTR_SOURCE_MAG, sm );
    }

    /**
     * Get the list of available source mags
     */
    public String[]
    getSourceMagList()
    {
	SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
	String minMagTargetAcq = SpInstUIST.MIN_MAG_TRAGET_ACQ;
	int ncols = SpInstUIST.SPECMAGS.getNumColumns();
	int ci = SpInstUIST.SPECMAGS.indexInRow(SpInstUIST.MIN_MAG_TRAGET_ACQ, 0);

	String specMags[] = new String[ncols - ci];
	for (int i=ci; i<ncols; i++) {
	    specMags[i - ci] = (String)SpInstUIST.SPECMAGS.elementAt(0,i);
	}
	return specMags;
    }

    /**
     * Get the mask 
     */
    public String
	getMask()
    {
	String mask = _avTable.get(SpUISTTargetAcqConstants.ATTR_MASK);
	if (mask == null) {
	    	    
	    int maskSet = getMaskSet();
	    if (maskSet == 1) {
		mask = SpInstUIST.DEFAULT_MASK1;
	    } else if (maskSet == 2) {
		mask = SpInstUIST.DEFAULT_MASK2;
	    } else if (maskSet == 3) {
		mask = SpInstUIST.DEFAULT_MASK3;
	    } else if (maskSet == 4) {
		mask = SpInstUIST.DEFAULT_MASK4;
	    } else if (maskSet == 5) {
		mask = SpInstUIST.DEFAULT_MASK5;
	    } else if (maskSet == 6) {
		mask = SpInstUIST.DEFAULT_MASK6;
	    }
	    
	    setMask(mask);
	}
	
	return mask;
    }
    
    /**
     * Update the Mask value in the attribute-value table
     */
    public void
	setMask(String mask)
    {
	
	_avTable.set(SpUISTTargetAcqConstants.ATTR_MASK, mask);
    }
    
    /**
     * Get the mask width
     */
    public double
	getMaskWidthPixels()
    {
	String maskWidthString = _avTable.get(SpUISTTargetAcqConstants.ATTR_MASK_WIDTH);
	double maskWidth = 0.0;
	if (maskWidthString == null) {
	    
            int maskIndex = SpInstUIST.MASKS.indexInColumn (getMask(),0);
            maskWidth = new Double((String) SpInstUIST.MASKS.elementAt(maskIndex,1))
                .doubleValue();
	    
	    setMaskWidthPixels(maskWidth);
	} else {
	    maskWidth = Double.valueOf(maskWidthString).doubleValue();
	}
	
	return maskWidth;
    }
    
    /**
     * Update the Mask width value in the attribute-value table
     */
    public void
	setMaskWidthPixels(double maskWidth)
    {
	
	_avTable.set(SpUISTTargetAcqConstants.ATTR_MASK_WIDTH, maskWidth);
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
		Integer.valueOf((String)SpInstUIST.DISPERSERS.elementAt(getDisperserIndex(),4)).intValue();
	}catch (Exception ex) {
            System.out.println("getMaskSet> failed to get maskSet - defaults to 1");
	    maskSet = 1;
	}
	return maskSet;
    }

    /**
     * Get the height (arcsec) of the current mask.
     */
    public double
    getMaskHeightArcsec()
    {
        double maskHeight = 0.0;
        try {
            int maskIndex = SpInstUIST.MASKS.indexInColumn (getMask(),0);
            maskHeight = new Double((String) SpInstUIST.MASKS.elementAt(maskIndex,2))
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
     * Set the mask height in arcseconds
     */
    public void
    setMaskHeightArcsec(double maskHeight)
    {
        _avTable.set(SpUISTTargetAcqConstants.ATTR_MASK_HEIGHT, maskHeight);
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
	    dispindex = SpInstUIST.DISPERSERS.indexInColumn(disperser, 0);
	}catch (ArrayIndexOutOfBoundsException ex) {
	    System.out.println ("Failed to find disperser index!");
	}catch (NoSuchElementException ex) {
	    System.out.println ("Failed to find disperser index!");
	}
	return dispindex;
    }
}
