/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                  Copyright (c) PPARC 2003                    */
/*                                                              */
/*==============================================================*/

// author: Alan Pickup = dap@roe.ac.uk         2003 Mar

package orac.ukirt.iter;

import java.util.*;
import java.io.*;

import gemini.util.*;
import orac.util.LookUpTable;
import orac.ukirt.inst.SpInstWFCAM;
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

class SpIterWFCAMCalObsEnumeration extends SpIterEnumeration
{
   private int     _curCount = 0;
   private int     _maxCount;
   private String  _calType;
   private SpIterValue[] _values;
 
SpIterWFCAMCalObsEnumeration(SpIterWFCAMCalObs iterObserve)
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

   SpIterWFCAMCalObs ico = (SpIterWFCAMCalObs) _iterComp;
   _values = new SpIterValue[4];

   _values[0] = new SpIterValue(SpWFCAMCalConstants.ATTR_FILTER,
      ico.getFilter());
   _values[1] = new SpIterValue(SpWFCAMCalConstants.ATTR_READMODE,
      ico.getReadMode());
   _values[2] = new SpIterValue(SpWFCAMCalConstants.ATTR_EXPOSURE_TIME,
      String.valueOf(ico.getExposureTime()));
   _values[3] = new SpIterValue(SpWFCAMCalConstants.ATTR_COADDS,
      ico.getCoaddsString());

   return _thisNextElement();
}

protected SpIterStep
_thisNextElement()
{
   return new SpIterStep(_calType, _curCount++, _iterComp, _values);
}
 
}

/**
 * Iterator for WFCAM FLAT observations.
 */
public class SpIterWFCAMCalObs extends SpIterObserveBase
{

   /** Identifier for a SKYFLAT calibration. */
   public static final int SKYFLAT = 0;

   /** Identifier for a DOMEFLAT calibration. */
   public static final int DOMEFLAT = 1;

   /** Identifier for a FOCUS calibration. */
   public static final int FOCUS = 2;

   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "WFCAMCalObs", "WFCAM Calibration");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterWFCAMCalObs());
}


/**
 * Default constructor.
 */
public SpIterWFCAMCalObs()
{
   super(SP_TYPE);


   _avTable.noNotifySet(SpWFCAMCalConstants.ATTR_CALTYPE,"SkyFlat",0);
   _avTable.noNotifySet(SpWFCAMCalConstants.ATTR_READMODE,null,0);
   _avTable.noNotifySet(SpWFCAMCalConstants.ATTR_FILTER,null,0);
   _avTable.noNotifySet(SpWFCAMCalConstants.ATTR_EXPOSURE_TIME,null,0);
   _avTable.noNotifySet(SpWFCAMCalConstants.ATTR_COADDS,null,0);

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
   return new SpIterWFCAMCalObsEnumeration(this);
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
 * Get the type of calibration.
 */
public int
getCalType()
{
   String calType = _avTable.get(SpUISTCalConstants.ATTR_CALTYPE);
   if ("SkyFlat".equals(calType)) {
      return SKYFLAT;
   } else if ("DomeFlat".equals(calType)) {
      return DOMEFLAT;
   }
   return FOCUS; 
}

/**
 * Set the type of calibration.
 */
public void
setCalType(String calType)
{
   _avTable.set(SpWFCAMCalConstants.ATTR_CALTYPE, calType);
}

/**
 * Get the type of calibration as a String.
 */
public String
getCalTypeString()
{
   if (getCalType() == SKYFLAT) {
      return "SkyFlat";
   } else if (getCalType() == DOMEFLAT) {
      return "DomeFlat";
   }
   return "Focus";
}

/**
 * Get the calType choices.
 */
public String[]
getCalTypeChoices()
{
   String choices[] = new String[3];
   choices[0] = "SkyFlat";
   choices[1] = "DomeFlat";
   choices[2] = "Focus";
  return choices;
}

/**
 * Get the readMode choices.
 */
public String[]
getReadModeChoices()
{
  SpInstWFCAM inst =  (SpInstWFCAM) getInstrumentItem();
  return inst.getReadModeList();
}

/**
 * Get the filter choices.
 */
public String[]
getFilterChoices()
{
  SpInstWFCAM inst =  (SpInstWFCAM) getInstrumentItem();
  return inst.getFilterList();
}

/**
 * Get the readMode.
 */
public String
getReadMode()
{
   String readMode = _avTable.get(SpWFCAMCalConstants.ATTR_READMODE);
   if (readMode == null || readMode.equals("none")) {
       readMode = getDefaultReadMode();
       setReadMode (readMode);
   }
   return readMode;
}

/**
 * Get the default readMode
*/
public String
getDefaultReadMode()
{
   String readMode=null;

   SpInstWFCAM inst =  (SpInstWFCAM) getInstrumentItem();
   readMode = inst.getReadMode();
   return readMode;
}

/**
 * Set the readMode
 */
public void
setReadMode(String rm)
{
   _avTable.set(SpWFCAMCalConstants.ATTR_READMODE, rm);
}

/**
 * Get the filter.
 */
public String
getFilter()
{
   String filter = _avTable.get(SpWFCAMCalConstants.ATTR_FILTER);
   if (filter == null || filter.equals("none")) {
       filter = getDefaultFilter();
       setFilter (filter);
   }
   return filter;
}

/**
 * Get a default filter
*/
public String
getDefaultFilter()
{
   String filter=null;

   SpInstWFCAM inst =  (SpInstWFCAM) getInstrumentItem();
   filter = inst.getFilter();
   return filter;
}

/**
 * Set the filter.
 */
public void
setFilter(String filter)
{
   _avTable.set(SpWFCAMCalConstants.ATTR_FILTER, filter);
}

/**
 * Set the exposure time 
 */
public void
setExposureTime(String expTime)
{
   _avTable.set(SpWFCAMCalConstants.ATTR_EXPOSURE_TIME, expTime);
}

/**
 * Get the exposure time, overriding base method
 */
public double
getExposureTime()
{
  double et = _avTable.getDouble(SpWFCAMCalConstants.ATTR_EXPOSURE_TIME, 0.0);
  if (et == 0.0) {
      et = getDefaultExposureTime();
      setExposureTime (Double.toString (et));
  }
  return et;

}

/**
 * Get the exposure time OT as a string to 4 places
 * Base on method used in MathUtil.round
 */
public String
getExposureTimeString()
{
    int mult = (int) Math.pow(10, 4);
    double et = ((double) Math.round(getExposureTime() * mult))/mult;
    String timeAsString = Double.toString(et);
    return timeAsString;
}

/**
 * Provide a default exposure time.
 */
public double
getDefaultExposureTime()
{
   double det = 0.0;
   // Get the exposure time from the WFCAM instrument
   SpInstWFCAM inst =  (SpInstWFCAM) getInstrumentItem();
   if ((getCalType() == SKYFLAT) || (getCalType() == DOMEFLAT))
   {
      det = inst.getDefaultFlatExpTime();
   } else
   {
      det = inst.getDefaultFocusExpTime();
   }
   return det;
}


/**
 * Get coadds number as string
 */
public String
getCoaddsString()
{
   String coadds = _avTable.get(SpWFCAMCalConstants.ATTR_COADDS);
   if (coadds == null) {
      coadds = Integer.toString(getDefaultCoadds());
      setCoadds(coadds);
   }
   return coadds; 
}

/**
 * Set the coadds
 */
public void
setCoadds(String coadds)
{
   _avTable.set(SpWFCAMCalConstants.ATTR_COADDS, coadds);
}

public int
getDefaultCoadds()
{
   int dc = 1;
   // Get the coadds from the WFCAM instrument
   SpInstWFCAM inst =  (SpInstWFCAM) getInstrumentItem();
   if ((getCalType() == SKYFLAT) || (getCalType() == DOMEFLAT))
   {
      dc = inst.getDefaultFlatCoadds();
   } else
   {
      dc = inst.getDefaultFocusCoadds();
   }
   return dc;
}

/* 
useDefaults - reset values so that defaults will get used
*/
public void
useDefaults()
{
   _avTable.rm(SpWFCAMCalConstants.ATTR_READMODE);
   _avTable.rm(SpWFCAMCalConstants.ATTR_FILTER);
   _avTable.rm(SpWFCAMCalConstants.ATTR_EXPOSURE_TIME);
   _avTable.rm(SpWFCAMCalConstants.ATTR_COADDS);
}


}
