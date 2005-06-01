// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter;

import gemini.util.ConfigWriter;

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpObs;
import gemini.sp.SpType;
import gemini.sp.SpTreeMan;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import gemini.sp.obsComp.SpInstConstants;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpStareCapability;

import java.io.IOException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import orac.ukirt.inst.SpInstCGS4;
import orac.ukirt.inst.SpInstWFCAM;
import orac.ukirt.inst.SpDRRecipe;
import orac.ukirt.inst.SpUKIRTInstObsComp;

/**
 * Enumerater for the elements of the Observe iterator.
 */
class SpIterBiasObsEnumeration extends SpIterEnumeration
{
   private int _curCount = 0;
   private int _maxCount;
   private SpIterValue[] _values;

SpIterBiasObsEnumeration(SpIterBiasObs iterObserve)
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
   SpIterBiasObs ibo   = (SpIterBiasObs) _iterComp;
   String expTimeValue = String.valueOf(ibo.getExposureTime());
   String coaddsValue  = String.valueOf(ibo.getCoadds());

   _values = new SpIterValue[2];
   _values[0] = new SpIterValue(SpInstConstants.ATTR_EXPOSURE_TIME, expTimeValue);
   _values[1] = new SpIterValue(SpInstConstants.ATTR_COADDS, coaddsValue);

   return _thisNextElement();
}

protected SpIterStep
_thisNextElement()
{
   return new SpIterStep("bias", _curCount++, _iterComp, _values);
}
   
}


public class SpIterBiasObs extends SpIterObserveBase implements SpTranslatable
{
   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "biasObs", "Bias");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterBiasObs());
}


/**
 * Default constructor.
 */
public SpIterBiasObs()
{
   super(SP_TYPE);
}

/**
 * Override getTitle to return the observe count.
 */
public String
getTitle()
{
   if (getTitleAttr() != null) {
      return super.getTitle();
   }

   return "Bias (" + getCount() + "X)";
}

/**
 */
public SpIterEnumeration
elements()
{
   return new SpIterBiasObsEnumeration(this);
}

/**
 * Override getExposureTime. Get the value from the instrument in
 * scope.
 */
public double
getExposureTime()
{
   SpItem _baseItem = parent();
   SpUKIRTInstObsComp spi = (SpUKIRTInstObsComp) SpTreeMan.findInstrument(_baseItem);
   return spi.getDefaultBiasExpTime();
}

/**
 * Override setExposureTime to ignore what is passed in.
 */
public void
setExposureTime(double expTime)
{
   // Do nothing
}
 
/**
 * Override setExposureTime to ignore what is passed in.
 */
public void
setExposureTime(String expTime)
{
   
}
/**
 * Override setCoadds to ignore what is passed in.
 */
public void
setCoadds(int coadds)
{
   // Do nothing
}
 
/**
 * Override setCoadds to ignore what is passed in.
 */
public void
setCoadds(String coadds)
{
   
}

/**
 * Override getting the coadds.so as to not inherit unless necessary.
 */
public int
getCoadds()
{
   // If the coadds has been set, use it.
   if (_avTable.exists(ATTR_COADDS)) {
      return _avTable.getInt(ATTR_COADDS, 1);
   }

   SpItem _baseItem = parent();
   SpUKIRTInstObsComp spi = (SpUKIRTInstObsComp) SpTreeMan.findInstrument(_baseItem);

   return spi.getDefaultBiasCoadds();

   // The following is the code for inheriting the coadds from the
   // instrument. We don't do this on UKIRT. The coadds is defaulted,
   // changeable by the instrument scientist only, in the instrument componet,
   // like the xposure time.
//    SpStareCapability stareCap;
//    String name = SpStareCapability.CAPABILITY_NAME;
//    int coadds = 1;
//    stareCap = (SpStareCapability) spi.getCapability(name);
//    if (stareCap != null) {
//      coadds = stareCap.getCoadds();
//    }
//    return coadds;

}

public void translate (Vector v) throws SpTranslationNotSupportedException {
    // First of all make sure we have a suitable instrument
    SpInstObsComp inst = SpTreeMan.findInstrument(this);
    if ( inst == null || (!(inst instanceof SpInstCGS4) && !(inst instanceof SpInstWFCAM)) ) {
        throw new SpTranslationNotSupportedException("No CGS4 instrument component in scope");
    }

    // Now get the config items and update them for this bias observation
    Hashtable t = inst.getConfigItems();
    // CGS4 specific
    if ( t.containsKey("biasExpTime") ) {
        t.put("biasExpTime", ""+getExposureTime());
    }
    if ( t.contains("biasNumExp") ) {
        t.put("biasNumExp", ""+getCoadds());
    }

    // WFCAM specific
    if ( t.containsKey("type") ) {
        t.put("type", "bias");
    }
    if ( t.containsKey("exposureTime") ) {
        t.put("exposureTime", "" + getExposureTime());
    }
    if ( t.containsKey("coadds") ) {
        t.put("coadds", ""+getCoadds());
    }

    // Delete redundant entries
    if ( "WFCAM".equalsIgnoreCase( (String)t.get("instrument") ) ) {
        t.remove("filter");
        t.remove("instPort");
        t.remove("readMode");
        t.remove("exposureTime");
        t.remove("coadds");
    }
    else if ( "UIST".equalsIgnoreCase( (String)t.get("instrument") ) ) {
        t.remove("instPort");
        t.remove("camera");
        t.remove("imager");
        t.remove("filter");
        t.remove("focus");
        t.remove("polarimetry");
        t.remove("mask");
        t.remove("maskWidth");
        t.remove("maskHeight");
        t.remove("disperser");
        t.remove("posAngle");
        t.remove("centralWavelength");
        t.remove("resolution");
        t.remove("dispersion");
        t.remove("scienceArea");
        t.remove("pixelScale");
        t.remove("nreads");
        t.remove("mode");
        t.remove("readInterval");
        t.remove("chopFrequency");
        t.remove("chopDelay");
        t.remove("chopDelay");
        t.remove("darkNumExp");
        t.remove("pupil_imaging");
        t.remove("pupil_imaging");
        t.remove("DAConf");
        t.remove("DAConfMinExpT");
        t.remove("spectralCoverage");
    }
    else {
        // Assume nothing to remove
    }

    // Now see if we have a DRRecipe component and write out it's headers if we do.
    SpItem parent = parent();
    while ( parent != null && !(parent instanceof SpObs) ) {
        parent = parent.parent();
    }
    if ( parent != null ) {
        Vector recipes = SpTreeMan.findAllItems(parent, "orac.ukirt.inst.SpDRRecipe");
        if ( recipes != null && recipes.size() != 0 ) {
            SpDRRecipe recipe = (SpDRRecipe)recipes.get(0);
            v.add("setHeader GRPMEM " + (recipe.getBiasInGroup()? "T":"F"));
            v.add("setHeader RECIPE " + recipe.getBiasRecipeName());
        }
    }

    try {
        ConfigWriter.getCurrentInstance().write(t);
    }
    catch (IOException ioe) {
        throw new SpTranslationNotSupportedException("Error writing bias config file");
    }

    v.add( "loadConfig " + ConfigWriter.getCurrentInstance().getCurrentName() );
    v.add( "set BIAS");
    v.add("do " + getCount() + " _observe");
}


}


