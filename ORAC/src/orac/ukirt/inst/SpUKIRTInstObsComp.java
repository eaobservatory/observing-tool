/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package orac.ukirt.inst;

import gemini.sp.SpAvTable;
import gemini.sp.SpObsData;
import gemini.sp.SpType;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;
import gemini.sp.iter.SpIterComp;
import gemini.sp.iter.SpIterConfigObs;

import gemini.util.Angle;

import orac.ukirt.iter.SpIterBiasObs;
import orac.ukirt.iter.SpIterDarkObs;
import orac.ukirt.iter.SpIterCGS4CalObs;
import orac.ukirt.iter.SpIterMichelleCalObs;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * A base class for UKIRT instrument observation component items. This extends
 * gemini.sp.obsComp.SpInstObsComp and adds UKIRT-specific features.
 *
 * @author Alan Bridger, UKATC
 * @version 1.0
 *
 * @see gemini.sp.SpObsData
 * @see gemini.sp.SpObsContextItem
 * @see gemini.sp.obsComp.SpInstObsComp
 */
public abstract class SpUKIRTInstObsComp extends SpInstObsComp
{
    
    public static final int XAP_INDEX = 0;      // Location of inst aper X value
    public static final int YAP_INDEX = 1;      // Location of inst aper Y value
    public static final int ZAP_INDEX = 2;      // Location of inst aper Z value
    public static final int LAP_INDEX = 3;      // Location of inst aper L value
    public static String INSTRUMENT_PORT;       // The instrument port
    public static String[] INSTRUMENT_APER;     // Array of inst aper values
    public static double XARCSECPERMM = 1.524;  // Plate scale in focal plane
    public static double YARCSECPERMM = -1.524; // Plate scale in focal plane.  The
                                                // Sign is different because TCS
                                                // reverses the sign, for some
                                                // obscure reason connected with
                                                // the handedness of the co-ordinate
                                                // system.
    
    // Names of the attributes
    public static final String ATTR_INSTRUMENT_PORT = "instPort";
    public static final String ATTR_INSTRUMENT_APER = "instAper";
    public static final String ATTR_VERSION = ".version";

    /**
     * Integration overhead.
     *
     * Number of integrations for UKIRT (according to Paul Hirst, email, 24/05/2002)
     * <BLOCKQUOTE>
     * Most of the time, the number of integrations will be == 1,
     * except with cgs4 and michelle - these instruments should have a number of integrations
     * parameter, which will often be == sampling_x * sampling_y - except for
     * example in a cgs4 BIAS frame, it's explicitly set to 3.
     * </BLOCKQUOTE>
     *
     * This variable is used for MSB duration estimation. Subclasses can set this to an
     * instrument specific value. Currently 0.5 is used for all instruments.
     */
    protected double _int_oh = 0.5;

    /**
     * Overhead per observe iteration ("Eye").
     *
     * This variable is used for MSB duration estimation. Subclasses can set this to an
     * instrument specific value. Currently 0.5 is used for all instruments.
     */
    protected double _obs_oh = 0.5;


    /**
     * Constructor. Sets default values for attributes.
     */
    public SpUKIRTInstObsComp(SpType spType)
    {
	super(spType);
	
	_avTable.noNotifySet(ATTR_VERSION, "1.0", 0);
	_avTable.noNotifySet(ATTR_INSTRUMENT_PORT, "Centre", 0);
	_avTable.noNotifySet(ATTR_INSTRUMENT_APER, "0.0", XAP_INDEX);
	_avTable.noNotifySet(ATTR_INSTRUMENT_APER, "0.0", YAP_INDEX);
	_avTable.noNotifySet(ATTR_INSTRUMENT_APER, "0.0", ZAP_INDEX);
	_avTable.noNotifySet(ATTR_INSTRUMENT_APER, "0.0", LAP_INDEX);
	_avTable.noNotifySet(ATTR_EXPOSURE_TIME, "0", 0);
	
    }
    
    /**
     * Set X value of the instrument aperture
     */
    public void
	setInstApX(String x)
    {
	_avTable.set(ATTR_INSTRUMENT_APER, x, XAP_INDEX);
    }
    
    /**
     * Set Y value of the instrument aperture
     */
    public void
	setInstApY(String y)
    {
	_avTable.set(ATTR_INSTRUMENT_APER, y, YAP_INDEX);
    }
    
    /**
     * Set Z value of the instrument aperture
     */
    public void
	setInstApZ(String z)
    {
	_avTable.set(ATTR_INSTRUMENT_APER, z, ZAP_INDEX);
    }
    
    /**
     * Set L value of the instrument aperture
     */
    public void
	setInstApL(String l)
    {
	_avTable.set(ATTR_INSTRUMENT_APER, l, LAP_INDEX);
    }
    
  /**
   * Set the instrument apertures. It should be called whenever something
   * changes that causes a change in instrument aperture. 
   * Each instrument should provide its own implementation.
   */
   public void setInstAper() {

   }

    /**
     * Get the instrument aperture X value
     */
    public double
	getInstApX()
    {
	return _avTable.getDouble(ATTR_INSTRUMENT_APER, XAP_INDEX, 0.0);    
    }
    
    /**
     * Get the instrument aperture Y value
     */
    public double
	getInstApY()
    {
	return _avTable.getDouble(ATTR_INSTRUMENT_APER, YAP_INDEX, 0.0);
    }
    
    /**
     * Get the instrument aperture X value in arcseconds
     */
    public double
	getInstApXarcsec()
    {
	double x = _avTable.getDouble(ATTR_INSTRUMENT_APER, XAP_INDEX, 0.0);
	return XARCSECPERMM*x;
    }
    
    /**
     * Get the instrument aperture Y value in arcseconds
     */
    public double
	getInstApYarcsec()
    {
	double y =_avTable.getDouble(ATTR_INSTRUMENT_APER, YAP_INDEX, 0.0);
	return YARCSECPERMM*y;
    }
    
    /**
     * Get the instrument aperture Z value
     */
    public double
	getInstApZ()
    {
	return _avTable.getDouble(ATTR_INSTRUMENT_APER, ZAP_INDEX, 0.0);    
    }
    
    /**
     * Get the instrument aperture L value
     */
    public double
	getInstApL()
    {
	return _avTable.getDouble(ATTR_INSTRUMENT_APER, LAP_INDEX, 0.0);
    }
    
    /**
     * Set the inst. port
     */
    public void
	setPort(String port)
    {
	_avTable.set(ATTR_INSTRUMENT_PORT, port);
    }
    
    /**
     * Get the inst. port
     */
    public String
	getPort()
    {
	return _avTable.get(ATTR_INSTRUMENT_PORT);
    }
    
    /**
     * Get the exposure time. This is a wrap-around for the SpInstObsComp version
     * which allows us to intercept the value and change it more easily, e.g. 
     * setting a default. The original is declared final so we can't just 
     * override. This could of course change. Must be overridden by instruments
     * that wish to make use of the defaulting feature.
     */
    public double
	getExpTime()
    {
	return (getExposureTime());
    }
    
    /**
     * Set the exposure time. This is a wrap-around for the SpInstObsComp version
     * which allows us to intercept the value and change it more easily, e.g. 
     * setting a default. The original is declared final so we can't just 
     * override. This could of course change. Must be overridden by instruments
     * that wish to make use of the defaulting feature.
     */
    public void
	setExpTime(String seconds)
    {
	setExposureTime (seconds);
    }
    
    /**
     * Get the default exposure time. This one returns 1.0. This must be 
     * overridden by instruments wishing to use the feature.
     */
    public double
	getDefaultExpTime()
    {
	return 1.0;
    }
    
    /**
     * Get the default bias exposure time.  Instruments should override this to
     * return sensible values.
     */
    public double
	getDefaultBiasExpTime()
    {
	return 0.001;
    }
    
    /**
     * Get the default bias coadds.  Instruments should override this to
     * return sensible values.
     */
    public int
	getDefaultBiasCoadds()
    {
	return 50;
    }

    /**
     * Returns a time estimate in seconds for slewing the telescope for UKIRT imaging: 3 minutes.
     *
     * Should overriden by classes for instrument that do spectroscopy.
     */
    public double
	getSlewTime()
    {
	return 3.0 * 60.0;
    }


  /**
   * Generic Iteration Tracker for UKIRT.
   *
   * This IterationTracker is used as it is by IRCAM3 and UFTI.
   * Michelle and CGS4 instrument components have to subclass it.
   *
   * Added for OMP by MFO, 1 Novemeber 2001
   *
   * @see gemini.sp.obsComp.SpInstObsComp
   */
  public class IterTrackerUKIRT extends IterationTracker {
    double currentExposureTime = getExpTime();
    int    currentNoCoadds     = 1;
    SpIterComp currentIterStepItem = null;

    IterTrackerUKIRT() {
      if(_avTable.exists(ATTR_COADDS)) {
        currentNoCoadds = _avTable.getInt(ATTR_COADDS, 1);
      }
    }  

    public void update(SpIterStep spIterStep) {

      SpIterValue spIterValue = null;

      currentIterStepItem = spIterStep.item;

      try {
        String attribute = null;
        String value     = null;

        for(int i = 0; i < spIterStep.values.length; i++) {
          // SpIterStep.values     is an array of SpIterValue
          // SpIterValue.values    is an array of String the first of which contains
          attribute = spIterStep.values[i].attribute;

	  if((spIterStep.values[i].values != null) && (spIterStep.values[i].values.length > 0)) {
	    value     = spIterStep.values[i].values[0];
	  }

          if((attribute != null) && (value != null)) {
            if(attribute.equals(ATTR_EXPOSURE_TIME)) {
              currentExposureTime = Double.valueOf(value).doubleValue();
            }

            if(attribute.equals(ATTR_COADDS)) {
              currentNoCoadds = Integer.valueOf(value).intValue();
            }
	  }  
	}  
      }
      catch(Exception e) {
        System.out.println("Could not process iteration step "
	                 + spIterStep.title + " for time estimation:\n\n" + e);
      }
    }

    public double getObserveStepTime () {
      // extra_oh is a constant overheads related to certain observe iterators:
      // 30 seconds for dark, arc, flat or bias (in addition to the times to do
      // their respective eye), and 30 secs each time an instrument iterator changes a filter.  
      double extra_oh = 0.0;

      if((currentIterStepItem != null) &&
         ((currentIterStepItem instanceof SpIterBiasObs) ||
          (currentIterStepItem instanceof SpIterDarkObs) ||
          (currentIterStepItem instanceof SpIterCGS4CalObs) ||
          (currentIterStepItem instanceof SpIterMichelleCalObs) ||
	  (currentIterStepItem instanceof SpIterConfigObs))) {

        extra_oh = 30.0;
      }

      return (((currentNoCoadds * (currentExposureTime + getExposureOverhead())) + _int_oh) + _obs_oh) + extra_oh;
    }
  }

  public IterationTracker createIterationTracker() {
    return new IterTrackerUKIRT();
  }
}
