// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter;

import gemini.sp.SpType;
import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.iter.SpIterConfigObs;
import gemini.sp.iter.IterConfigItem;
import java.util.Enumeration;
import java.util.Vector;

/**
 * This class extends the SpIterConfigObs class and adds some UKIRT
 * specific items and overrides.
 */

public abstract class SpIterConfigObsUKIRT extends SpIterConfigObs
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
   return (SpInstObsComp) SpTreeMan.findInstrument(_baseItem);
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

}
