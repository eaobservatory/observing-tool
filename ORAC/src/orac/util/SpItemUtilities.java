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
import gemini.sp.obsComp.SpSchedConstObsComp;
import gemini.sp.SpTreeMan;
import gemini.sp.SpFactory;
import gemini.sp.SpInsertData;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpObs;
import gemini.sp.SpNote;
import gemini.sp.SpProg;
import gemini.sp.SpHierarchyChangeObserver;
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
   * Attribute for the SpAvTable of each SpObsContextItem
   * that will be translated to the XML id attribute.
   *
   * @see #ID_REF_SUFFIX
   */
  public static final String ATTR_ID		= ":id";

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
   * This method calls saveElapsedTime() on all MSBs and Observations that are MSBs.
   *
   * This utility method can be used to set all the elapsed times (time estimates),
   * before saving to disk or storing to database.
   * This means that this method sets the {@link gemini.sp.SpMSB.ATTR_ELAPSED_TIME}
   * attributes of all SpMSB components and SpObs components that are MSBs
   * based on the current setting.
   * Unlike other attributes  the elapsed time is not directly typed in by the
   * user but depends on the MSB/Observation as a whole. Therefore it has to be
   * set in all SpMSB components and SpObs components that are MSBs immediately
   * before saving/storing so that elapsed to written to XML is based on the latest
   * settings of these  components.
   */
  public static void saveElapsedTimes(SpItem spItem) {
    if(spItem instanceof SpMSB) {
      ((SpMSB)spItem).saveElapsedTime();
    }

    if(spItem instanceof SpObsContextItem) {
      SpItem child = spItem.child();

      while(child != null) {
        saveElapsedTimes(child);
        child = child.next();
      }
    }  
  }


  /**
   * Recursively updates the MSB attributes of all
   * SpObs items in spItem.
   */
  public static void updateMsbAttributes(SpItem spItem) {
    if(spItem instanceof SpObs) {
      ((SpObs)spItem).updateMsbAttributes();
    }

    if(spItem instanceof SpObsContextItem) {
      SpItem child = spItem.child();

      while(child != null) {
        updateMsbAttributes(child);
        child = child.next();
      }
    }  
  }

  /**
   * Returns hierarchy observer object for SpItems.
   *
   * Returns an object that can be be added to an SpItem (especially SpRootItem, SpProg etc)
   * to observe hierarchy changes and execute necessary code such as updating msb attributs.
   *
   * @see #updateMsbAttributes()
   */
  public static SpHierarchyChangeObserver getHierarchyChangeUtil() {
    return new SpHierarchyChangeObserver() {

      public void spItemsAdded(SpItem parent, SpItem[] children, SpItem afterChild) {
        updateMsbAttributes(parent);
      }

      public void spItemsMoved(SpItem oldParent, SpItem[] children, SpItem newParent, SpItem afterChild) {
        updateMsbAttributes(oldParent);
        updateMsbAttributes(newParent);
      }

      public void spItemsRemoved(SpItem parent, SpItem[] children) { }
    };
  }

  /**
   * Remove id and idref attributes from spItem subtree.
   */
  public static void removeReferenceIDs(SpItem spItem) {
    spItem.getTable().noNotifyRm(ATTR_ID);

    Enumeration attributes = spItem.getTable().attributes();
    String idrefAttribute = null;

    while(attributes.hasMoreElements()) {
      idrefAttribute = (String)attributes.nextElement();
      if(idrefAttribute.endsWith(ID_REF_SUFFIX)) {
        spItem.getTable().noNotifyRm(idrefAttribute);
      }
    }
    

    if(spItem instanceof SpObsContextItem) {
      SpItem child = spItem.child();

      while(child != null) {
        removeReferenceIDs(child);
        child = child.next();
      }
    }  
  }


  /**
   * Find SpMSB's and SpObs's recursively and set references.
   */
  public void setReferenceIDs(SpItem spItem) {
    if(spItem instanceof SpObsContextItem) {
      if(spItem instanceof SpMSB) { 
	_insertReferenceIDsFor(SpTreeMan.findTargetList(spItem), spItem);
	_insertReferenceIDsFor(SpTreeMan.findInstrument(spItem), spItem);
	_insertReferenceIDsFor(findSiteQuality(spItem), spItem);
	_insertReferenceIDsFor(findSchedConstraint(spItem), spItem);
	_insertReferenceIDsFor(findDRRecipe(spItem), spItem);
	_insertReferenceIDsFor(findProgramNotes(spItem.getRootItem(), new Vector()), spItem);
// 	_insertReferenceIDsFor(findNote(spItem), spItem);
      }
      else {
        SpItem child = spItem.child();

        while(child != null) {
          setReferenceIDs(child);
          child = child.next();
        }
      }	
    }  
  }

  /**
   * @param spObsCompVector
   *                  This vector used to contain only items of type SpObsComp
   *                  but it can now contain any items of type SpItem
   *                  in order to allow top level SpNotes to be inherited
   *                  by MSBs that are further down in the tree. This is the
   *                  same kind of inheritance that applies to SpObsComp items
   *                  such as instrument components, site quality components etc.
   */
  protected void _insertReferenceIDsFor(Vector spObsCompVector, SpItem spItem) {
    if(spObsCompVector == null) {
      return;
    }

    for(int i = 0; i < spObsCompVector.size(); i++) {
      _insertReferenceIDsFor((SpItem)spObsCompVector.get(i), spItem);
    }
  }

  /**
   * @param spObsComp This argument used to be of type SpObsComp but it has been changed to
   *                  SpItem in order to allow top level SpNotes to be inherited
   *                  by MSBs that are further down in the tree. This is the
   *                  same kind of inheritance that applies to SpObsComp items
   *                  such as instrument components, site quality components etc.
   */
  protected void _insertReferenceIDsFor(SpItem spObsComp, SpItem spItem) {
    if((spObsComp == null) || (spItem == null)) {
      return;
    }

    // See whether the spObsComp (e.g. observation component or SpNote) has an id already.
    // If so use it for idref of the SpMSB.
    String idString = spObsComp.getTable().get(ATTR_ID);
    if(idString != null) {
      spItem.getTable().noNotifySet(
        spObsComp.getClass().getName().substring(spObsComp.getClass().getName().lastIndexOf(".") + 1) + ID_REF_SUFFIX,
	idString, 0);
    }
    // No id yet. Use _idCounter for both spObsComp (e.g. observation component or SpNote)
    // and spItem (e.g. SpMSB/SpObs). 
    else {
       spObsComp.getTable().noNotifySet(ATTR_ID, "" + _idCounter, 0);
       if ( spObsComp.getClass().getName().indexOf("SpNote") == -1 ) {
	   // This in NOT an SpNote
	   spItem.getTable().noNotifySet(
					 spObsComp.getClass().getName().substring(spObsComp.getClass().getName().lastIndexOf(".") + 1) + ID_REF_SUFFIX,
					 "" + _idCounter, 0);
       }
       else {
	   // This is a note
	   spItem.getTable().noNotifySet(
					 spObsComp.getClass().getName().substring(spObsComp.getClass().getName().lastIndexOf(".") + 1) + ID_REF_SUFFIX + _idCounter,
					 "" + _idCounter, 0);
       }
      _idCounter++;
    }  
  }

/**
 * Find the site quality component assoicated with this context, if any.  Only
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
 * Find the scheduling constraint component assoicated with this context, if any.  Only
 * searches the given scope.  It does not navigate the tree hierarchy.
 */
public static SpSchedConstObsComp
findSchedConstraintInContext(SpItem spItem)
{
   SpSchedConstObsComp scc = null;
   Enumeration e = spItem.children();
   while (e.hasMoreElements()) {
      SpItem child = (SpItem) e.nextElement();
      if (child instanceof SpSchedConstObsComp) {
         scc = (SpSchedConstObsComp) child;
         break;
      }
   }
   return scc;
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
 * Find the SpNote assoicated with this context, if any.  Only
 * searches the given scope.  It does not navigate the tree hierarchy.
 */
public static SpNote
findNoteInContext(SpItem spItem)
{
   SpNote note = null;
   Enumeration e = spItem.children();
   while (e.hasMoreElements()) {
      SpItem child = (SpItem) e.nextElement();
      if (child instanceof SpNote) {
         note = (SpNote) child;
	 if (note.isObserveInstruction()) {
	     break;
	 }
	 else {
	     note = null;
	     continue;
	 }
      }
   }
   return note;
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
 * Find the scheduling constraint component associated with the given scope
 * scope of the given item.
 *
 * @param spItem the SpItem defining the scope to search
 */
public static SpSchedConstObsComp
findSchedConstraint(SpItem spItem)
{
   if (spItem instanceof SpSchedConstObsComp) {
      return (SpSchedConstObsComp) spItem;
   }

   SpItem parent = spItem.parent();

   SpSchedConstObsComp scc;
   if (!(spItem instanceof SpObsContextItem)) {
      if (parent == null) {
         return null;
      }
      scc = findSchedConstraintInContext(parent);
   } else {
      scc = findSchedConstraintInContext(spItem);
   }

   if ((scc == null) && (parent != null)) {
      return findSchedConstraint(parent);
   }
   return scc;
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
 * Find the SpNote associated with the scope of the given item.
 *
 * The idea of note covering a scope rather than just being
 * associated with an item is new. It was introduced in connection
 * with SpMSB folders. Having notes that cover a scope allows
 * notes that are added to a top level folder (AND, OR, Science Program)
 * to be accessible to all MSBs in the scope. This is useful
 * when the Science Program tree is disassembled into MSBs (e.g. in the OMP)
 *
 * @param spItem the SpItem defining the scope to search
 */
public static SpNote
findNote(SpItem spItem)
{
   if (spItem instanceof SpNote) {
      return (SpNote) spItem;
   }

   SpItem parent = spItem.parent();

   SpNote note;
   if (!(spItem instanceof SpObsContextItem)) {
      if (parent == null) {
         return null;
      }
      note = findNoteInContext(parent);
   } else {
      note = findNoteInContext(spItem);
   }

   if ((note == null) && (parent != null)) {
      return findNote(parent);
   }
   return note;
}

public static Vector findProgramNotes(SpItem spItem, Vector notes) {

    // Now get all the children
    Enumeration e = spItem.children();
    while ( e.hasMoreElements() ) {
	SpItem child = (SpItem)e.nextElement();
	if ( child instanceof gemini.sp.SpNote ) {
	    if ( ((SpNote)child).isObserveInstruction() ){
		notes.add(child);
	    }
	}
	else if ( child instanceof gemini.sp.SpAND || child instanceof gemini.sp.SpOR ) {
	    notes = findProgramNotes (child, notes);
	}
    }
    return notes;
}

}

