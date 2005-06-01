// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter;

import gemini.sp.SpType;
import gemini.sp.SpItem;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.iter.SpIterComp;
import gemini.sp.iter.SpIterConfigObs;
import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.IterConfigItem;
import orac.ukirt.inst.SpUKIRTInstObsComp;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * This class extends the SpIterConfigObs class and adds some UKIRT
 * specific items and overrides.
 */

public abstract class SpIterConfigObsUKIRT extends SpIterConfigObs implements SpTranslatable
{

public SpIterConfigObsUKIRT (SpType spType)
{
   super(spType);
}


/**
 * Get the instrument item in the scope of the base item.
 */
public SpInstObsComp
getInstrumentItem()
{
   SpItem _baseItem = parent();
// Commented by RDK
//   return (SpInstObsComp) SpTreeMan.findInstrument(_baseItem);
// End of commented by RDK
// Added by RDK
   if (_baseItem == null) {
       return null;
   }
   else {
       return (SpInstObsComp) SpTreeMan.findInstrument(_baseItem);
   }
//End of Added by RDK
}


/**
 * Insert a new (blank) step at the given index.
 */
public void
insertConfigStep(int index)
{
    super.insertConfigStep (index);
    Vector v = getConfigAttribs();
    for (int i=0; i<v.size(); ++i) {
	String a = (String) v.elementAt(i);
	String defval = "";
	if (index > 0) defval = getConfigStep (a, index-1);
	setConfigStep (a, defval, index);
    }
}

/**
 * Overriding adding a configuration item to the set to default values
 */
public void
addConfigItem(IterConfigItem ici, int size)
{

    super.addConfigItem (ici, size);

    // Get default from the associated instrument
    SpInstObsComp _inst = getInstrumentItem();
    String defval = "";
    int l = ici.attribute.length();
    defval = _inst.getTable().get(ici.attribute.substring(0,l-4));

    if (size == 0) {
	setConfigStep (ici.attribute, defval, 0);
    }else{
	for (int i=0; i<size; i++) {
	    setConfigStep (ici.attribute, defval, i);
	}
    }
    
}

/**
 * Adding an item with no default
 */
public void
addConfigItemNoDef (IterConfigItem ici, int size)
{

    super.addConfigItem (ici, size);

}

public void translate(Vector v) throws SpTranslationNotSupportedException {
    String date = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
    String confDir = System.getProperty("CONF_PATH");
    if ( confDir == null || confDir.equals("") || !(new File(confDir).exists()) ) {
        confDir = System.getProperty("user.home");
    }

    // Get default values from the instrument
    SpInstObsComp inst = SpTreeMan.findInstrument(this);
    if ( inst == null || !(inst instanceof SpUKIRTInstObsComp) ) {
        throw new SpTranslationNotSupportedException("No instrument, or not a UKIRT instrument");
    }
    
    IterConfigItem [] a_ici = getAvailableItems();
//     Hashtable defaultsTable = new Hashtable();
//     for ( int i=0; i< a_ici.length; i++ ) {
//         if ( a_ici[i].title.equalsIgnoreCase("acqmode") ) {
//             defaultsTable.put("readMode", inst.getAcqMode());
//         }
//         else if ( a_ici[i].title.equalsIgnoreCase("mode") ) {
//         }
//         else if ( a_ici[i].title.equalsIgnoreCase("central wavlen.") ) {
//         }
//         else if ( a_ici[i].title.equalsIgnoreCase("readoutarea") ) {
//             defaultsTable.put("readArea", inst.getReadoutArea());
//         }
//         else if ( a_ici[i].title.equalsIgnoreCase("filter") ) {
//             defaultsTable.put("filter", inst,getFilter());
//         }
//         else if ( a_ici[i].title.equalsIgnoreCase("exp. time") ) {
//         }
//         else if ( a_ici[i].title.equalsIgnoreCase("coadds") ) {
//             defaultsTable.put("coadds", "" + inst.getCoadds());
//         }
//         else if ( a_ici[i].title.equalsIgnoreCase("source magnitude") ) {
//         }
//         else if ( a_ici[i].title.equalsIgnoreCase("InstAperX") ) {
//         }
//         else if ( a_ici[i].title.equalsIgnoreCase("InstAperY") ) {
//         }
//         else if ( a_ici[i].title.equalsIgnoreCase("InstAperL") ) {
//         }
//     }
//     
// 
//     List iterList = getConfigAttribs();
//     for ( int i=0; i<iterList.size(); i++ ) {
//         String attr = (String)iterList.get(i);
//         List vals = getConfigSteps(attr);
//         System.out.println("vals = " + vals);
//     }
}

}
