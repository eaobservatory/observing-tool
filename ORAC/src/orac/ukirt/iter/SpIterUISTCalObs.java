/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/

// author: Alan Pickup = dap@roe.ac.uk         2001 Nov

package orac.ukirt.iter;

import java.util.*;
import java.io.*;

import gemini.util.*;

import orac.util.LookUpTable;
import orac.ukirt.inst.SpInstUIST;
import orac.ukirt.inst.SpDRRecipe;
import gemini.sp.SpFactory;
import gemini.sp.SpObs;
import gemini.sp.SpMSB;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.sp.SpType;
import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpStareCapability;

class SpIterUISTCalObsEnumeration extends SpIterEnumeration
{
   private int     _curCount = 0;
   private int     _maxCount;
   private String  _calType;
   private SpIterValue[] _values;
 
SpIterUISTCalObsEnumeration(SpIterUISTCalObs iterObserve)
{
   super(iterObserve);
   _maxCount    = iterObserve.getCount();
   _calType     = iterObserve.getCalTypeString();
}

protected boolean
_thisHasMoreElements()
{
   return (_curCount < _maxCount);
}
 
protected SpIterStep
_thisFirstElement()
{

   SpIterUISTCalObs ico = (SpIterUISTCalObs) _iterComp;
   _values = new SpIterValue[16]; // Only need 16 items now (was 25) RDK

   ico.updateDAConf();
   _values[0] = new SpIterValue(SpUISTCalConstants.ATTR_FILTER,
      ico.getFilter());
   _values[1] = new SpIterValue(SpUISTCalConstants.ATTR_MODE,
      ico.W_mode);
   _values[2] = new SpIterValue(SpUISTCalConstants.ATTR_EXPOSURE_TIME,
      String.valueOf(ico.W_actExpTime));
// Commented by RDK
//    _values[3] = new SpIterValue(SpUISTCalConstants.ATTR_WAVEFORM,
//       ico.W_waveform);
// End of commented by RDK
// Renumbered _values indexes to be contiguous after above items removed by RDK
    _values[3] = new SpIterValue(SpUISTCalConstants.ATTR_NREADS,
      String.valueOf(ico.W_nreads));
// Commented by RDK
//    _values[5] = new SpIterValue(SpUISTCalConstants.ATTR_NRESETS,
//       String.valueOf(ico.W_nresets));
//    _values[6] = new SpIterValue(SpUISTCalConstants.ATTR_RESET_DELAY,
//       String.valueOf(ico.W_resetDelay));
// End of commented by RDK
// Renumbered _values indexes to be contiguous after above items removed by RDK
    _values[4] = new SpIterValue(SpUISTCalConstants.ATTR_READ_INTERVAL,
      String.valueOf(ico.W_readInterval));
// Commented by RDK
//    _values[8] = new SpIterValue(SpUISTCalConstants.ATTR_IDLE_PERIOD,
//       String.valueOf(ico.W_idlePeriod));
//    _values[9] = new SpIterValue(SpUISTCalConstants.ATTR_MUST_IDLES,
//       String.valueOf(ico.W_mustIdles));
//    _values[10] = new SpIterValue(SpUISTCalConstants.ATTR_NULL_CYCLES,
//       String.valueOf(ico.W_nullCycles));
//    _values[11] = new SpIterValue(SpUISTCalConstants.ATTR_NULL_EXPOSURES,
//       String.valueOf(ico.W_nullExposures));
//    _values[12] = new SpIterValue(SpUISTCalConstants.ATTR_NULL_READS,
//       String.valueOf(ico.W_nullReads));
// End of commented by RDK
// Renumbered _values indexes to be contiguous after above items removed by RDK
   _values[5] = new SpIterValue(SpUISTCalConstants.ATTR_DUTY_CYCLE,
      String.valueOf(ico.W_dutyCycle));
   _values[6] = new SpIterValue(SpUISTCalConstants.ATTR_CHOP_FREQUENCY,
      ico.W_chopFrequency);
   _values[7] = new SpIterValue(SpUISTCalConstants.ATTR_CHOP_DELAY,
      String.valueOf(ico.W_chopDelay));
   _values[8] = new SpIterValue(SpUISTCalConstants.ATTR_COADDS,
      String.valueOf(ico.W_coadds));
   _values[9] = new SpIterValue(SpUISTCalConstants.ATTR_FLAT_SOURCE,
      ico.getFlatSource());
   _values[10] = new SpIterValue(SpUISTCalConstants.ATTR_ARC_SOURCE,
      ico.getArcSource());
   _values[11] = new SpIterValue(SpUISTCalConstants.ATTR_FOCUS,
      ico.getFocus());
   _values[12] = new SpIterValue(SpUISTCalConstants.ATTR_ORDER,
      ico.getOrder());
   _values[13] = new SpIterValue(SpUISTCalConstants.ATTR_CENTRAL_WAVELENGTH,
      ico.getCentralWavelength());
   _values[14] = new SpIterValue(SpUISTCalConstants.ATTR_OBSERVATION_TIME,
      String.valueOf(ico.W_obsTime));
// Commented by RDK
//    _values[23] = new SpIterValue(SpUISTCalConstants.ATTR_OBSTIME_OT,
//       ico.getObsTimeOT());
// End of commented by RDK
   _values[15] = new SpIterValue(SpUISTCalConstants.ATTR_EXPTIME_OT,
      ico.getExpTimeOTString());

   return _thisNextElement();
}

protected SpIterStep
_thisNextElement()
{
   return new SpIterStep(_calType, _curCount++, _iterComp, _values);
}
 
}


/**
 * Iterator for UIST calibration observations (FLAT and ARC).
 */
public class SpIterUISTCalObs extends SpIterObserveBase implements SpTranslatable
{

   /** Identifier for a FLAT calibration. */
   public static final int FLAT = 0;

   /** Identifier for an ARC calibration. */
   public static final int ARC  = 1;

   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "UISTCalObs", "UIST Cal Observe");
   public String W_mode;
// Commented by RDK
//   public String W_waveform;
// End of commented by RDK
   public int W_nreads;
// Commented by RDK
//    public int W_nresets;
//    public double W_resetDelay;
// End of commented by RDK
   public double W_readInterval;
// Commented by RDK
//    public double W_idlePeriod;
//    public int W_mustIdles;
//    public int W_nullCycles;
//    public int W_nullExposures;
//    public int W_nullReads;
// End of commented by RDK
   public String W_chopFrequency;
   public double W_chopDelay;
   public int W_coadds;
   public double W_dutyCycle;
   public double W_actExpTime;
   public double W_obsTime;

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterUISTCalObs());
}


/**
 * Default constructor.
 */
public SpIterUISTCalObs()
{
   super(SP_TYPE);


   _avTable.noNotifySet(SpUISTCalConstants.ATTR_CALTYPE,"Flat",0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_FILTER,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_MODE,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_EXPOSURE_TIME,null,0);
// Commented by RDK
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_WAVEFORM,null,0);
// End of added by RDK
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_NREADS,null,0);
// Commented by RDK
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_NRESETS,null,0);
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_RESET_DELAY,null,0);
// End of added by RDK
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_READ_INTERVAL,null,0);
// Commented by RDK
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_IDLE_PERIOD,null,0);
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_MUST_IDLES,null,0);
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_NULL_CYCLES,null,0);
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_NULL_EXPOSURES,null,0);
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_NULL_READS,null,0);
// End of added by RDK
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_DUTY_CYCLE,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_CHOP_FREQUENCY,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_CHOP_DELAY,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_COADDS,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_FLAT_SOURCE,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_ARC_SOURCE,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_FOCUS,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_ORDER,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_CENTRAL_WAVELENGTH,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_OBSERVATION_TIME,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_EXPTIME_OT,null,0);
// Commented by RDK
//   _avTable.noNotifySet(SpUISTCalConstants.ATTR_OBSTIME_OT,null,0);
// End of added by RDK

}

/**
 * Override getTitle to return the observe type and the count.
 */
public String
getTitle()
{
   if (getTitleAttr() != null) {
      return super.getTitle();
   }
   return getCalTypeString() + " (" + getCount() + "X)";
}

/**
 */
public SpIterEnumeration
elements()
{
   return new SpIterUISTCalObsEnumeration(this);
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
 * Get the exposure time OT
 */
public double
getExpTimeOT()
{
  double et = _avTable.getDouble(SpUISTCalConstants.ATTR_EXPTIME_OT, 0.0);
  if (et == 0.0) {
      et = getDefaultExpTimeOT();
      setExpTimeOT (Double.toString (et));
  }
  return et;

}

/**
 * Get the exposure time OT as a string to 4 places
 * Base on method used in MathUtil.round
 */
public String
getExpTimeOTString()
{
    int mult = (int) Math.pow(10, 4);
    double et = ((double) Math.round(getExpTimeOT() * mult))/mult;
    String timeAsString = Double.toString(et);
    return timeAsString;
}

/**
 * Provide a default exposure time.
 */
public double
getDefaultExpTimeOT()
{
  double det = 0.0;
  // Get the exposure time from the UIST instrument
  SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
  if (getCalType() == FLAT) {
    det = inst.getDefaultFlatExpTime();
  } else if (getCalType() == ARC) {
    det = inst.getDefaultArcExpTime();
  }
  return det;

}

// Commented by RDK

/**
 * Provide a default observation time.
 */
// public double
// getDefaultObsTimeOT()
// {
//   double dot = 0.0;
//   // Get the observation time from the UIST instrument
//   SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
//   if (getCalType() == FLAT) {
//     dot = inst.getDefaultFlatObsTimeOT();
//   } else if (getCalType() == ARC) {
//     dot = inst.getDefaultArcObsTimeOT();
//   }
//   return dot;

// }
// End of commented by RDK
// Added by RDK
public int
getDefaultCoadds()
{
  int dc = 1;
  // Get the coadds from the UIST instrument
  SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
  if (getCalType() == FLAT) {
    dc = inst.getDefaultFlatCoadds();
  } else if (getCalType() == ARC) {
    dc = inst.getDefaultArcCoadds();
  }
  return dc;

}
// End of added by RDK

/**
 * Set the exposure time OT
 */
public void
setExpTimeOT(String expTime)
{
   _avTable.set(SpUISTCalConstants.ATTR_EXPTIME_OT, expTime);
}

// Commented by RDK
/**
 * Set the observation time OT
 */
// public void
// setObsTimeOT(String obsTime)
// {
//    _avTable.set(SpUISTCalConstants.ATTR_OBSTIME_OT, obsTime);
// }

/**
 * Get the observation Time
 */
// public String
// getObsTimeOT()
// {
//    String obsTime = _avTable.get(SpUISTCalConstants.ATTR_OBSTIME_OT);
//    if (obsTime == null) {
//       obsTime = Double.toString(getDefaultObsTimeOT());
//       setObsTimeOT(obsTime);
//    }   return obsTime; 
// }
// End of commented by RDK

/**
 * Get the type of calibration.
 */
public int
getCalType()
{
   String calType = _avTable.get(SpUISTCalConstants.ATTR_CALTYPE);
   if ("Flat".equals(calType)) {
      return FLAT;
   }
   return ARC; 
}

/**
 * Set the type of calibration.
 */
public void
setCalType(String calType)
{
   _avTable.set(SpUISTCalConstants.ATTR_CALTYPE, calType);
}

/**
 * Get the type of calibration as a String.
 */
public String
getCalTypeString()
{
   if (getCalType() == FLAT) {
      return "Flat";
   }
   return "Arc";
}

/**
 * Get the calibration type choices.
 */
public String[]
getCalTypeChoices()
{
  SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
  if (inst.isImaging()) {
      String choices[] = new String[1];
      choices[0] = "Flat";
      return choices;
  } else {
      String choices[] = new String[2];
      choices[0] = "Flat";
      choices[1] = "Arc";
      return choices;
  }
}

/**
 * Report whether the instrument is imaging
 */
public boolean
isImaging()
{
    SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
    String camera = inst.getCamera();
    return (camera.equalsIgnoreCase("imaging"));
}

/**
 * Get the flat source
 */
public String
getFlatSource()
{
  if (getCalType() == FLAT) {
      String fs = _avTable.get(SpUISTCalConstants.ATTR_FLAT_SOURCE);
      if (fs == null) {
          SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
          fs = inst.getDefaultFlatSource();
          setFlatSource(fs);
      }
      return fs;
  } else {
      String fs = "undefined";
      return fs;
  }   
}

/**
 * Set the flat source
 */
public void
setFlatSource(String fs)
{
   _avTable.set(SpUISTCalConstants.ATTR_FLAT_SOURCE, fs);
}

/**
 * Get the flat source choices.
 */
public String[]
getFlatSourceChoices()
{
  SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
  String choices[] = inst.getFlatList();
  return choices;
}

/**
 * Get the focus
 */
public String
getFocus()
{
   String focus;
   SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
   if (getCalType() == ARC) {
       /* Get the instrument arcFocus */
       focus = inst.getArcFocus();
   } else {
       /* Get the instrument focus */
       focus = inst.getFocus();
   }
   setFocus(focus);
   return focus;
}

/**
 * Set the focus
 */
public void
setFocus(String focus)
{
   _avTable.set(SpUISTCalConstants.ATTR_FOCUS, focus);
}

/**
 * Get the order
 */
public String
getOrder()
{
   String order;
   SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
   if (getCalType() == ARC) {
       order = inst.getArcOrder();
   } else {
       order = inst.getOrderString();
   }
   setOrder(order);
   return order;
}

/**
 * Set the order
 */
public void
setOrder(String order)
{
   _avTable.set(SpUISTCalConstants.ATTR_ORDER, order);
}

/**
 * Get the central wavelength
 */
public String
getCentralWavelength()
{
   String cwl;
   SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
   if (getCalType() == ARC) {
       cwl = inst.getArcCentralWavelength();
   } else {
       cwl = inst.getCentralWavelengthString();
   }
   setCentralWavelength(cwl);
   return cwl;
}

/**
 * Set the central wavelength
 */
public void
setCentralWavelength(String cwl)
{
   _avTable.set(SpUISTCalConstants.ATTR_CENTRAL_WAVELENGTH, cwl);
}

/**
 * Get the arc source
 */
public String
getArcSource()
{
   String as;
   if (getCalType() == ARC) {
      as = _avTable.get(SpUISTCalConstants.ATTR_ARC_SOURCE);
      if (as == null) {
          /* The default is the first available choice */
          as = getArcSourceChoices()[0];
          setArcSource(as);
      }
   } else {
      as = "undefined";
      setArcSource(as);
   }
   return as;
}

/**
 * Set the arc source
 */
public void
setArcSource(String as)
{
   _avTable.set(SpUISTCalConstants.ATTR_ARC_SOURCE, as);
}

/**
 * Get the arc source choices.
 */
public String[]
getArcSourceChoices()
{
  SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
  String choices[] = inst.getArcList();
  return choices;
}

/**
 * Get the filter.
 */
public String
getFilter()
{
   String filter = _avTable.get(SpUISTCalConstants.ATTR_FILTER);
   if (filter == null || filter.equals("none")) {
       filter = getDefaultFilter();
       setFilter (filter);
   }
   return filter;
}

/**
 * Get a default value for the filter
*/
public String
getDefaultFilter()
{
   String filter=null;

   SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
   if (getCalType() == FLAT) {
      filter = inst.getFilter();
   } else if (getCalType() == ARC) {
      filter = inst.getArcFilter();
   }
   return filter;
}

/**
 * Set the filter.
 */
public void
setFilter(String filter)
{
   _avTable.set(SpUISTCalConstants.ATTR_FILTER, filter);
}

// Commented by RDK
/**
 * Get coadds number as string
*/
// public String
// getCoaddsString()
// {
//    String coadds = _avTable.get(SpUISTCalConstants.ATTR_COADDS);
//    return coadds;
// }
// End of commented by RDK

// Added by RDK

/**
 * Get coadds number as string
*/
public String
getCoaddsString()
{
   String coadds = _avTable.get(SpUISTCalConstants.ATTR_COADDS);
   if (coadds == null) {
      coadds = Integer.toString(getDefaultCoadds());
      setCoadds(coadds);
   }   return coadds; 
}

/**
 * Set the coadds
 */
public void
setCoadds(String coadds)
{
   _avTable.set(SpUISTCalConstants.ATTR_COADDS, coadds);
}

/**
 * Get observation time as string
*/
public String
getObservationTimeString()
{
    double ot = Double.valueOf(_avTable.get(SpUISTCalConstants.ATTR_OBSERVATION_TIME)).doubleValue();
   String observationTime = Double.toString(MathUtil.round(ot,3));
   return observationTime;
}

// End of added by RDK
/**
 * use
Defaults - reset values so that defaults will get used
*/
public void
useDefaults()
{
   _avTable.rm(SpUISTCalConstants.ATTR_FILTER);
   _avTable.rm(SpUISTCalConstants.ATTR_MODE);
   _avTable.rm(SpUISTCalConstants.ATTR_EXPOSURE_TIME);
// Commented by RDK
//    _avTable.rm(SpUISTCalConstants.ATTR_WAVEFORM);
// End of commented by RDK
   _avTable.rm(SpUISTCalConstants.ATTR_NREADS);
// Commented by RDK
//    _avTable.rm(SpUISTCalConstants.ATTR_NRESETS);
//    _avTable.rm(SpUISTCalConstants.ATTR_RESET_DELAY);
// End of commented by RDK
   _avTable.rm(SpUISTCalConstants.ATTR_READ_INTERVAL);
// Commented by RDK
//    _avTable.rm(SpUISTCalConstants.ATTR_IDLE_PERIOD);
//    _avTable.rm(SpUISTCalConstants.ATTR_MUST_IDLES);
//    _avTable.rm(SpUISTCalConstants.ATTR_NULL_CYCLES);
//    _avTable.rm(SpUISTCalConstants.ATTR_NULL_EXPOSURES);
//    _avTable.rm(SpUISTCalConstants.ATTR_NULL_READS);
// End of commented by RDK
   _avTable.rm(SpUISTCalConstants.ATTR_DUTY_CYCLE);
   _avTable.rm(SpUISTCalConstants.ATTR_CHOP_FREQUENCY);
   _avTable.rm(SpUISTCalConstants.ATTR_CHOP_DELAY);
   _avTable.rm(SpUISTCalConstants.ATTR_COADDS);
   _avTable.rm(SpUISTCalConstants.ATTR_FLAT_SOURCE);
   _avTable.rm(SpUISTCalConstants.ATTR_ARC_SOURCE);
   _avTable.rm(SpUISTCalConstants.ATTR_OBSERVATION_TIME);
   _avTable.rm(SpUISTCalConstants.ATTR_EXPTIME_OT);
// Commented by RDK
//   _avTable.rm(SpUISTCalConstants.ATTR_OBSTIME_OT);
// End of commented by RDK
}

/**
 * Update the daconf for an Object/Sky observatioon
 */
public void
updateDAConf()
{
   SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
   if (getCalType() == FLAT) {
      inst.setFlatExpTime(getExpTimeOT());
// Commented by RDK
//      inst.setFlatObsTimeOT(Double.valueOf(getObsTimeOT()).doubleValue());
// End of commented by RDK
// Added by RDK
      String coaddsString = getCoaddsString();
      inst.setFlatCoadds(Integer.valueOf(coaddsString).intValue());
// End of added by RDK
      inst.updateDAFlatConf();
   } else if (getCalType() == ARC) {
      inst.setArcExpTime(getExpTimeOT());
// Commented by RDK
//      inst.setArcObsTimeOT(Double.valueOf(getObsTimeOT()).doubleValue());
// End of commented by RDK
// Added by RDK
      inst.setArcCoadds(Integer.valueOf(getCoaddsString()).intValue());
// End of added by RDK
      inst.updateDAArcConf();
   }
   /* Update local instance variables from UIST class */
   W_mode          = inst.W_mode;
// Commented by RDK
//    W_waveform      = inst.W_waveform;
// End of commented by RDK
   W_nreads        = inst.W_nreads;
// Commented by RDK
//    W_nresets       = inst.W_nresets;
//    W_resetDelay    = inst.W_resetDelay;
// End of commented by RDK
   W_readInterval  = inst.W_readInterval;
// Commented by RDK
//    W_idlePeriod    = inst.W_idlePeriod;
//    W_mustIdles     = inst.W_mustIdles;
//    W_nullCycles    = inst.W_nullCycles;
//    W_nullExposures = inst.W_nullExposures;
//    W_nullReads     = inst.W_nullReads;
// End of commented by RDK
   W_dutyCycle     = inst.W_dutyCycle;
   W_chopFrequency = inst.W_chopFrequency;
   W_chopDelay     = inst.W_chopDelay;
   W_actExpTime    = inst.W_actExpTime;
   W_obsTime       = inst.W_obsTime;
   W_coadds        = inst.W_coadds;

   /* Update attributes from instance variables */
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_MODE,W_mode,0);
// Commented by RDK
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_WAVEFORM,W_waveform,0);
// End of commented by RDK
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_NREADS,
      Integer.toString(W_nreads),0);
// Commented by RDK
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_NRESETS,
//       Integer.toString(W_nresets),0);
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_RESET_DELAY,
//       Double.toString(W_resetDelay),0);
// End of commented by RDK
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_READ_INTERVAL,
      Double.toString(W_readInterval),0);
// Commented by RDK
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_IDLE_PERIOD,
//       Double.toString(W_idlePeriod),0);
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_MUST_IDLES,
//       Integer.toString(W_mustIdles),0);
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_NULL_CYCLES,
//       Integer.toString(W_nullCycles),0);
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_NULL_EXPOSURES,
//       Integer.toString(W_nullExposures),0);
//    _avTable.noNotifySet(SpUISTCalConstants.ATTR_NULL_READS,
//       Integer.toString(W_nullReads),0);
// End of commented by RDK
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_DUTY_CYCLE,
      Double.toString(W_dutyCycle),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_CHOP_FREQUENCY,
      W_chopFrequency,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_CHOP_DELAY,
      Double.toString(W_chopDelay),0);
   // The exposure time may have been over-ridden by the QT.  Only
   // do an update if it hasn't
   if ( !_avTable.exists("override_"+ATTR_EXPOSURE_TIME) ||
	!_avTable.getBool("override_"+ATTR_EXPOSURE_TIME) ) {
       _avTable.noNotifySet(SpUISTCalConstants.ATTR_EXPOSURE_TIME,
			    Double.toString(W_actExpTime),0);
   }
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_OBSERVATION_TIME,
      Double.toString(W_obsTime),0);
   // The coadds may have been over-ridden by the QT.  Only
   // do an update if it hasn't
   if ( !_avTable.exists("override_"+ATTR_COADDS) ||
	!_avTable.getBool("override_"+ATTR_COADDS) ) {
       _avTable.noNotifySet(SpUISTCalConstants.ATTR_COADDS,
			    Integer.toString(W_coadds),0);
   }
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_FLAT_SOURCE,
      getFlatSource(),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_ARC_SOURCE,
      getArcSource(),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_FILTER,
      getFilter(),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_FOCUS,
      getFocus(),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_ORDER,
      getOrder(),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_CENTRAL_WAVELENGTH,
      getCentralWavelength(),0);

}

public void translate (Vector v) throws SpTranslationNotSupportedException {

    SpInstUIST inst;
    try {
        inst = (SpInstUIST)SpTreeMan.findInstrument(this);
    }
    catch (Exception e) {
        throw new SpTranslationNotSupportedException("No UIST instrument is scope");
    }

    // Work out whether we are a flat or an arc
    boolean isFlat = (getCalType() == FLAT);

    
    // We first need to get the DRRecipe component...
    SpItem parent = parent();
    Vector recipes = null;
    while ( parent != null ) {
        if ( parent instanceof SpMSB ) {
            recipes = SpTreeMan.findAllItems(parent, "orac.ukirt.inst.SpDRRecipe");
            if ( recipes != null && recipes.size() > 0 ) {
                break;
            }
        }
        parent = parent.parent();
    }

    if ( recipes != null && recipes.size() != 0 ) {
        SpDRRecipe recipe = (SpDRRecipe)recipes.get(0);
        if ( isFlat ) {
            v.add("setHeader GRPMEM " + (recipe.getFlatInGroup()? "T":"F"));
            v.add("setHeader RECIPE " + recipe.getFlatRecipeName());
        }
        else {
            // Assume arc
            v.add("setHeader GRPMEM " + (recipe.getArcInGroup()? "T":"F"));
            v.add("setHeader RECIPE " + recipe.getArcRecipeName());
        }
    }

    Hashtable configTable = inst.getConfigItems();
    if (isFlat) {
        configTable.put("flatSource", getFlatSource());
    }
    else {
        configTable.put("arcSource", getArcSource());
    }

    configTable.put("type", getCalTypeString().toLowerCase());
    configTable.put("exposureTime", ""+getExposureTime());
    configTable.put("coadds", ""+getCoadds());
    configTable.put("observationTime", getObservationTimeString());
    if ( _avTable.exists(SpUISTCalConstants.ATTR_NREADS) ) {
        configTable.put("nreads", _avTable.get(SpUISTCalConstants.ATTR_NREADS) );
    }
    if ( _avTable.exists(SpUISTCalConstants.ATTR_CHOP_DELAY) ) {
        configTable.put("chopDelay", _avTable.get(SpUISTCalConstants.ATTR_CHOP_DELAY) );
    }
    if ( _avTable.exists(SpUISTCalConstants.ATTR_DUTY_CYCLE) ) {
        configTable.put("dutyCycle", _avTable.get(SpUISTCalConstants.ATTR_DUTY_CYCLE) );
    }
    if ( _avTable.exists(SpUISTCalConstants.ATTR_CENTRAL_WAVELENGTH) ) {
        configTable.put("centralWavelength", _avTable.get(SpUISTCalConstants.ATTR_CENTRAL_WAVELENGTH) );
    }

    try {
        ConfigWriter.getCurrentInstance().write(configTable);
    }
    catch (IOException ioe) {
        throw new SpTranslationNotSupportedException("Unable to write UISTCalObs config file");
    }
    v.add( "loadConfig " + ConfigWriter.getCurrentInstance().getCurrentName() );
    v.add( "setrotator " + (String)configTable.get("posAngle") );
    v.add( "set " + getCalTypeString().toUpperCase() );
    v.add( "do " + getCount() + " _observe");
    
    // Finally move the default config file, numbered _1 down
    for ( int i=v.size()-1; i>=0 ; i-- ) {
        if ( ((String)v.get(i)).matches("loadConfig .*_1") ) {
            String defCon = (String)v.get(i);
            v.remove(i);
            v.add(defCon);
            break;
        }
    }
    
}

}
