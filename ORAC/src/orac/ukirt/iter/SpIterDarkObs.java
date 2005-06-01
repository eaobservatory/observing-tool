// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter;

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpObs;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.sp.SpTreeMan;
import gemini.sp.SpType;

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import gemini.sp.obsComp.SpInstConstants;
import gemini.sp.obsComp.SpInstObsComp;

import gemini.util.ConfigWriter;

import orac.ukirt.inst.SpInstCGS4;
import orac.ukirt.inst.SpInstMichelle;
import orac.ukirt.inst.SpInstUFTI;
import orac.ukirt.inst.SpInstUIST;
import orac.ukirt.inst.SpInstWFCAM;
import orac.ukirt.inst.SpDRRecipe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Enumerater for the elements of the Observe iterator.
 */
class SpIterDarkObsEnumeration extends SpIterEnumeration
{
   private int _curCount = 0;
   private int _maxCount;
   private SpIterValue[] _values;

SpIterDarkObsEnumeration(SpIterDarkObs iterObserve)
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
   SpIterDarkObs ibo   = (SpIterDarkObs) _iterComp;
   String expTimeValue = String.valueOf(ibo.getExposureTime());
   String coaddsValue  = String.valueOf(ibo.getCoadds());
 
   _values = new SpIterValue[2];
   _values[0] = new SpIterValue(SpInstConstants.ATTR_EXPOSURE_TIME, expTimeValue
);
   _values[1] = new SpIterValue(SpInstConstants.ATTR_COADDS, coaddsValue);
 
   return _thisNextElement();
}
 
protected SpIterStep
_thisNextElement()
{
   return new SpIterStep("dark", _curCount++, _iterComp, _values);
}

}


public class SpIterDarkObs extends SpIterObserveBase implements SpTranslatable
{
   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "darkObs", "Dark");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterDarkObs());
}


/**
 * Default constructor.
 */
public SpIterDarkObs()
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

   return "Dark (" + getCount() + "X)";
}
/**
 * Use default acquisition
 */
public void
useDefaultAcquisition()
{
   _avTable.rm(ATTR_EXPOSURE_TIME);
   _avTable.rm(ATTR_COADDS);
}

/**
 */
public SpIterEnumeration
elements()
{
   return new SpIterDarkObsEnumeration(this);
}

public void translate( Vector v ) throws SpTranslationNotSupportedException {

    // Get the instrument to allow us to get the config information
    SpInstObsComp inst = SpTreeMan.findInstrument(this);
    if ( inst == null ) {
        throw new SpTranslationNotSupportedException("No instrument in scope");
    }

    Hashtable defaultsTable = inst.getConfigItems();
    
    // Set the number of dark exposures
    defaultsTable.put("darkNumExp", "" + getCoadds() );
    if ( defaultsTable.containsKey("coadds") ) {
        defaultsTable.put("coadds", ""+getCoadds() );
    }
    if ( defaultsTable.containsKey("expTime") ) {
        defaultsTable.put("expTime", "" + getExposureTime() );
    }
    if ( defaultsTable.containsKey("exposureTime") ) {
        defaultsTable.put("exposureTime", "" + getExposureTime() );
    }
    if ( defaultsTable.containsKey("chopDelay") ) {
        defaultsTable.put("chopDelay", "0.0");
    }
    if ( defaultsTable.containsKey("type") ) {
        if ("WFCAM".equalsIgnoreCase((String)defaultsTable.get("instrument") ) ) {
            defaultsTable.put("type", "dark");
        }
        else {
        }
    }


    // Delete things we dont't need
    if ("WFCAM".equalsIgnoreCase((String)defaultsTable.get("instrument") ) ) {
        defaultsTable.remove("filter");
        defaultsTable.remove("instPort");
        defaultsTable.remove("readMode");
    }
    else {
        // No other instrument needs anything
    }

    // Now we need to write a config for this dark
    try {
        ConfigWriter.getCurrentInstance().write(defaultsTable);
    }
    catch (IOException ioe) {
        throw new SpTranslationNotSupportedException("Unable to write dark config file");
    }


    // We will also need to get the DRRecipe component to allow us to set the 
    // appropriate headers
    // Find the parent first...
    SpItem parent = parent();
    while ( parent !=  null && !(parent instanceof SpObs) ) {
        parent = parent.parent();
    }
    Vector recipes = SpTreeMan.findAllItems(parent, "orac.ukirt.inst.SpDRRecipe");
    if ( recipes != null && recipes.size() != 0 ) {
        SpDRRecipe recipe = (SpDRRecipe)recipes.get(0);
        v.add("setHeader GRPMEM " + (recipe.getDarkInGroup()? "T":"F"));
        v.add("setHeader RECIPE " + recipe.getDarkRecipeName());
    }
    else {
        System.out.println("No DRRecipe Component found");
    }

    v.add("loadConfig " + ConfigWriter.getCurrentInstance().getCurrentName() );
    v.add("set DARK");
    v.add("do " + getCount() + " _observe");

    //Finally move the default config (always _1) down
    String configPattern = "loadConfig .*_1";
    for ( int i=v.size()-1; i>=0; i-- ) {
        String line = (String)v.get(i);
        if ( line.matches(configPattern) ) {
            v.removeElementAt(i);
            v.add(line);
            break;
        }
    }
}

}
