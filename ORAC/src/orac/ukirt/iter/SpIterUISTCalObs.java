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

import orac.util.LookUpTable;
import orac.ukirt.inst.SpInstUIST;
import gemini.sp.SpFactory;
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
   _values = new SpIterValue[25];

   ico.updateDAConf();
   _values[0] = new SpIterValue(SpUISTCalConstants.ATTR_FILTER,
      ico.getFilter());
   _values[1] = new SpIterValue(SpUISTCalConstants.ATTR_MODE,
      ico.W_mode);
   _values[2] = new SpIterValue(SpUISTCalConstants.ATTR_EXPOSURE_TIME,
      String.valueOf(ico.W_actExpTime));
   _values[3] = new SpIterValue(SpUISTCalConstants.ATTR_WAVEFORM,
      ico.W_waveform);
   _values[4] = new SpIterValue(SpUISTCalConstants.ATTR_NREADS,
      String.valueOf(ico.W_nreads));
   _values[5] = new SpIterValue(SpUISTCalConstants.ATTR_NRESETS,
      String.valueOf(ico.W_nresets));
   _values[6] = new SpIterValue(SpUISTCalConstants.ATTR_RESET_DELAY,
      String.valueOf(ico.W_resetDelay));
   _values[7] = new SpIterValue(SpUISTCalConstants.ATTR_READ_INTERVAL,
      String.valueOf(ico.W_readInterval));
   _values[8] = new SpIterValue(SpUISTCalConstants.ATTR_IDLE_PERIOD,
      String.valueOf(ico.W_idlePeriod));
   _values[9] = new SpIterValue(SpUISTCalConstants.ATTR_MUST_IDLES,
      String.valueOf(ico.W_mustIdles));
   _values[10] = new SpIterValue(SpUISTCalConstants.ATTR_NULL_CYCLES,
      String.valueOf(ico.W_nullCycles));
   _values[11] = new SpIterValue(SpUISTCalConstants.ATTR_NULL_EXPOSURES,
      String.valueOf(ico.W_nullExposures));
   _values[12] = new SpIterValue(SpUISTCalConstants.ATTR_NULL_READS,
      String.valueOf(ico.W_nullReads));
   _values[13] = new SpIterValue(SpUISTCalConstants.ATTR_DUTY_CYCLE,
      String.valueOf(ico.W_dutyCycle));
   _values[14] = new SpIterValue(SpUISTCalConstants.ATTR_CHOP_FREQUENCY,
      ico.W_chopFrequency);
   _values[15] = new SpIterValue(SpUISTCalConstants.ATTR_CHOP_DELAY,
      String.valueOf(ico.W_chopDelay));
   _values[16] = new SpIterValue(SpUISTCalConstants.ATTR_COADDS,
      String.valueOf(ico.W_coadds));
   _values[17] = new SpIterValue(SpUISTCalConstants.ATTR_FLAT_SOURCE,
      ico.getFlatSource());
   _values[18] = new SpIterValue(SpUISTCalConstants.ATTR_ARC_SOURCE,
      ico.getArcSource());
   _values[19] = new SpIterValue(SpUISTCalConstants.ATTR_FOCUS,
      ico.getFocus());
   _values[20] = new SpIterValue(SpUISTCalConstants.ATTR_ORDER,
      ico.getOrder());
   _values[21] = new SpIterValue(SpUISTCalConstants.ATTR_CENTRAL_WAVELENGTH,
      ico.getCentralWavelength());
   _values[22] = new SpIterValue(SpUISTCalConstants.ATTR_OBSERVATION_TIME,
      String.valueOf(ico.W_obsTime));
   _values[23] = new SpIterValue(SpUISTCalConstants.ATTR_OBSTIME_OT,
      ico.getObsTimeOT());
   _values[24] = new SpIterValue(SpUISTCalConstants.ATTR_EXPTIME_OT,
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
public class SpIterUISTCalObs extends SpIterObserveBase
{

   /** Identifier for a FLAT calibration. */
   public static final int FLAT = 0;

   /** Identifier for an ARC calibration. */
   public static final int ARC  = 1;

   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "UISTCalObs", "UIST Cal Observe");
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
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_WAVEFORM,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_NREADS,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_NRESETS,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_RESET_DELAY,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_READ_INTERVAL,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_IDLE_PERIOD,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_MUST_IDLES,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_NULL_CYCLES,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_NULL_EXPOSURES,null,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_NULL_READS,null,0);
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
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_OBSTIME_OT,null,0);

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

/**
 * Provide a default observation time.
 */
public double
getDefaultObsTimeOT()
{
  double dot = 0.0;
  // Get the observation time from the UIST instrument
  SpInstUIST inst =  (SpInstUIST) getInstrumentItem();
  if (getCalType() == FLAT) {
    dot = inst.getDefaultFlatObsTimeOT();
  } else if (getCalType() == ARC) {
    dot = inst.getDefaultArcObsTimeOT();
  }
  return dot;

}

/**
 * Set the exposure time OT
 */
public void
setExpTimeOT(String expTime)
{
   _avTable.set(SpUISTCalConstants.ATTR_EXPTIME_OT, expTime);
}

/**
 * Set the observation time OT
 */
public void
setObsTimeOT(String obsTime)
{
   _avTable.set(SpUISTCalConstants.ATTR_OBSTIME_OT, obsTime);
}

/**
 * Get the observation Time
 */
public String
getObsTimeOT()
{
   String obsTime = _avTable.get(SpUISTCalConstants.ATTR_OBSTIME_OT);
   if (obsTime == null) {
      obsTime = Double.toString(getDefaultObsTimeOT());
      setObsTimeOT(obsTime);
   }   return obsTime; 
}

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

/**
 * Get coadds number as string
*/
public String
getCoaddsString()
{
   String coadds = _avTable.get(SpUISTCalConstants.ATTR_COADDS);
   return coadds;
}

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
   _avTable.rm(SpUISTCalConstants.ATTR_WAVEFORM);
   _avTable.rm(SpUISTCalConstants.ATTR_NREADS);
   _avTable.rm(SpUISTCalConstants.ATTR_NRESETS);
   _avTable.rm(SpUISTCalConstants.ATTR_RESET_DELAY);
   _avTable.rm(SpUISTCalConstants.ATTR_READ_INTERVAL);
   _avTable.rm(SpUISTCalConstants.ATTR_IDLE_PERIOD);
   _avTable.rm(SpUISTCalConstants.ATTR_MUST_IDLES);
   _avTable.rm(SpUISTCalConstants.ATTR_NULL_CYCLES);
   _avTable.rm(SpUISTCalConstants.ATTR_NULL_EXPOSURES);
   _avTable.rm(SpUISTCalConstants.ATTR_NULL_READS);
   _avTable.rm(SpUISTCalConstants.ATTR_DUTY_CYCLE);
   _avTable.rm(SpUISTCalConstants.ATTR_CHOP_FREQUENCY);
   _avTable.rm(SpUISTCalConstants.ATTR_CHOP_DELAY);
   _avTable.rm(SpUISTCalConstants.ATTR_COADDS);
   _avTable.rm(SpUISTCalConstants.ATTR_FLAT_SOURCE);
   _avTable.rm(SpUISTCalConstants.ATTR_ARC_SOURCE);
   _avTable.rm(SpUISTCalConstants.ATTR_OBSERVATION_TIME);
   _avTable.rm(SpUISTCalConstants.ATTR_EXPTIME_OT);
   _avTable.rm(SpUISTCalConstants.ATTR_OBSTIME_OT);
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
      inst.setFlatObsTimeOT(Double.valueOf(getObsTimeOT()).doubleValue());
      inst.updateDAFlatConf();
   } else if (getCalType() == ARC) {
      inst.setArcExpTime(getExpTimeOT());
      inst.setArcObsTimeOT(Double.valueOf(getObsTimeOT()).doubleValue());
      inst.updateDAArcConf();
   }
   /* Update local instance variables from UIST class */
   W_mode          = inst.W_mode;
   W_waveform      = inst.W_waveform;
   W_nreads        = inst.W_nreads;
   W_nresets       = inst.W_nresets;
   W_resetDelay    = inst.W_resetDelay;
   W_readInterval  = inst.W_readInterval;
   W_idlePeriod    = inst.W_idlePeriod;
   W_mustIdles     = inst.W_mustIdles;
   W_nullCycles    = inst.W_nullCycles;
   W_nullExposures = inst.W_nullExposures;
   W_nullReads     = inst.W_nullReads;
   W_dutyCycle     = inst.W_dutyCycle;
   W_chopFrequency = inst.W_chopFrequency;
   W_chopDelay     = inst.W_chopDelay;
   W_actExpTime    = inst.W_actExpTime;
   W_obsTime       = inst.W_obsTime;
   W_coadds        = inst.W_coadds;

   /* Update attributes from instance variables */
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_MODE,W_mode,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_WAVEFORM,W_waveform,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_NREADS,
      Integer.toString(W_nreads),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_NRESETS,
      Integer.toString(W_nresets),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_RESET_DELAY,
      Double.toString(W_resetDelay),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_READ_INTERVAL,
      Double.toString(W_readInterval),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_IDLE_PERIOD,
      Double.toString(W_idlePeriod),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_MUST_IDLES,
      Integer.toString(W_mustIdles),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_NULL_CYCLES,
      Integer.toString(W_nullCycles),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_NULL_EXPOSURES,
      Integer.toString(W_nullExposures),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_NULL_READS,
      Integer.toString(W_nullReads),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_DUTY_CYCLE,
      Double.toString(W_dutyCycle),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_CHOP_FREQUENCY,
      W_chopFrequency,0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_CHOP_DELAY,
      Double.toString(W_chopDelay),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_EXPOSURE_TIME,
      Double.toString(W_actExpTime),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_OBSERVATION_TIME,
      Double.toString(W_obsTime),0);
   _avTable.noNotifySet(SpUISTCalConstants.ATTR_COADDS,
      Integer.toString(W_coadds),0);
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

}
