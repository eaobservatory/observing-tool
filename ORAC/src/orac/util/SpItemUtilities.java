/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.util;

import gemini.sp.SpItem;
import gemini.sp.SpMSB;
import gemini.sp.SpObsContextItem;
import gemini.sp.SpAvTable;
import gemini.sp.SpRootItem;
import gemini.sp.SpType;
import orac.ukirt.inst.SpDRRecipe;
import gemini.sp.obsComp.SpSiteQualityObsComp;
import gemini.sp.SpTreeMan;
import gemini.sp.SpFactory;
import gemini.sp.SpInsertData;
import gemini.sp.SpTelescopePos;
import gemini.sp.obsComp.SpObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp;
import java.util.Vector;

/**
 * Utilities for SpItem trees.
 *
 * @see gemini.sp.SpTreeMan
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpItemUtilities {

  /**
   */
  public static final String ID_SUFFIX		= ":id";

  /**
   */
  /**
   * ID_REF_SUFFIX is going to be preceded by the class name.
   *
   * So the SpAvTable attribute name is going to look like this:
   * For, say, SpTelescopeObsComp we get the SpAvTable attribute
   * name "SpTelescopeObsCompRef:idref" with the XML representation:
   * <!-- <SpTelescopeObsCompRef id="some_id"> -->
   *      &lt;SpTelescopeObsCompRef id="some_id"&gt;
   * inside the the SpMSB element.
   *   
   */
  public static final String ID_REF_SUFFIX	= "Ref:idref";


  private int _idCounter = 0;



  /**
   * Find SpMSB's, recursively, depth first.
   */
  public void setReferenceIDs(SpItem spItem) {
    if(spItem instanceof SpObsContextItem) {
      if(spItem instanceof SpMSB) { 
	_insertReferenceIDsFor(SpTreeMan.findTargetList(spItem), spItem);
	_insertReferenceIDsFor(SpTreeMan.findInstrument(spItem), spItem);
	_insertReferenceIDsFor(SpTreeMan.findObsCompSubtype(spItem, SpSiteQualityObsComp.SP_TYPE), spItem);
	_insertReferenceIDsFor(SpTreeMan.findObsCompSubtype(spItem, SpDRRecipe.SP_TYPE), spItem);
      }
      else {
        SpItem firstChild;
        SpItem child = spItem.child();

        while(child != null) {
          setReferenceIDs(child);
          child = child.next();
        }
      }	
    }  
  }

  protected void _insertReferenceIDsFor(Vector spObsCompVector, SpItem spItem) {
    if(spObsCompVector == null) {
      return;
    }

    for(int i = 0; i < spObsCompVector.size(); i++) {
      _insertReferenceIDsFor((SpObsComp)spObsCompVector.get(i), spItem);
    }
  }

  /**
   *
   */
  protected void _insertReferenceIDsFor(SpObsComp spObsComp, SpItem spItem) {
    if((spObsComp == null) || (spItem == null)) {
      return;
    }

    // See whether the SpTelescopeObsComp (target list) has an id already.
    // If so use it for idref of the SpMSB.
    String idString = spObsComp.getTable().get(ID_SUFFIX);
    if(idString != null) {
      spItem.getTable().noNotifySet(
        spObsComp.getClass().getName().substring(spObsComp.getClass().getName().lastIndexOf(".") + 1) + ID_REF_SUFFIX,
	idString, 0);
    }
    // No id yet. Use _idCounter for both SpTelescopeObsComp (target list) and SpMSB. 
    else {
       spObsComp.getTable().noNotifySet(ID_SUFFIX, "" + _idCounter, 0);
       spItem.getTable().noNotifySet(
         spObsComp.getClass().getName().substring(spObsComp.getClass().getName().lastIndexOf(".") + 1) + ID_REF_SUFFIX,
	 "" + _idCounter, 0);
      _idCounter++;
    }  
  }
}

