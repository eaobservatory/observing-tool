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
import gemini.sp.SpObs;
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
import java.util.Enumeration;

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
   * Find SpMSB's and SpObs's recursively and set references.
   */
  public void setReferenceIDs(SpItem spItem) {
    if(spItem instanceof SpObsContextItem) {
      if((spItem instanceof SpMSB) || (spItem instanceof SpObs)) { 
	_insertReferenceIDsFor(SpTreeMan.findTargetList(spItem), spItem);
	_insertReferenceIDsFor(SpTreeMan.findInstrument(spItem), spItem);
	_insertReferenceIDsFor(findSiteQuality(spItem), spItem);
	_insertReferenceIDsFor(findDRRecipe(spItem), spItem);
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

/**
 * Find the SpSiteQualityObsComp assoicated with this context, if any.  Only
 * searches the given scope.  It does not navigate the tree hierarchy.
 */
public static SpSiteQualityObsComp
findSiteQualityInContext(SpItem spItem)
{
   SpSiteQualityObsComp sqc = null;
   Enumeration e = spItem.children();
   while (e.hasMoreElements()) {
      SpItem child = (SpItem) e.nextElement();
      if (child instanceof SpSiteQualityObsComp) {
         sqc = (SpSiteQualityObsComp) child;
         break;
      }
   }
   return sqc;
}

/**
 * Find the SpDRRecipe assoicated with this context, if any.  Only
 * searches the given scope.  It does not navigate the tree hierarchy.
 */
public static SpDRRecipe
findDRRecipeInContext(SpItem spItem)
{
   SpDRRecipe drr = null;
   Enumeration e = spItem.children();
   while (e.hasMoreElements()) {
      SpItem child = (SpItem) e.nextElement();
      if (child instanceof SpDRRecipe) {
         drr = (SpDRRecipe) child;
         break;
      }
   }
   return drr;
}

/**
 * Find the site quality component associated with the given scope
 * scope of the given item.
 *
 * @param spItem the SpItem defining the scope to search
 */
public static SpSiteQualityObsComp
findSiteQuality(SpItem spItem)
{
   if (spItem instanceof SpSiteQualityObsComp) {
      return (SpSiteQualityObsComp) spItem;
   }

   SpItem parent = spItem.parent();

   SpSiteQualityObsComp sqc;
   if (!(spItem instanceof SpObsContextItem)) {
      if (parent == null) {
         return null;
      }
      sqc = findSiteQualityInContext(parent);
   } else {
      sqc = findSiteQualityInContext(spItem);
   }

   if ((sqc == null) && (parent != null)) {
      return findSiteQuality(parent);
   }
   return sqc;
}

/**
 * Find the DR recipe component associated with the given scope
 * scope of the given item.
 *
 * @param spItem the SpItem defining the scope to search
 */
public static SpDRRecipe
findDRRecipe(SpItem spItem)
{
   if (spItem instanceof SpDRRecipe) {
      return (SpDRRecipe) spItem;
   }

   SpItem parent = spItem.parent();

   SpDRRecipe drr;
   if (!(spItem instanceof SpObsContextItem)) {
      if (parent == null) {
         return null;
      }
      drr = findDRRecipeInContext(parent);
   } else {
      drr = findDRRecipeInContext(spItem);
   }

   if ((drr == null) && (parent != null)) {
      return findDRRecipe(parent);
   }
   return drr;
}

  /**
   * Find SpMSB's and SpObs's recursively and set references.
   */
  public static void setMsbAttribute(SpItem spItem) {
    if(spItem instanceof SpObsContextItem) {
      if(spItem instanceof SpObs) {
        SpObs spObs = (SpObs)spItem;
        if(spObs.parent() instanceof SpMSB) {
	  spObs.setIsMSB(false);
        }
        else {
	  spObs.setIsMSB(true);
        }
      }
      else {
        SpItem firstChild;
        SpItem child = spItem.child();

        while(child != null) {
          setMsbAttribute(child);
          child = child.next();
        }
      }	
    }  
  }
}

