// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.obsComp.SpInstObsComp;

/**
 * This is a base class for SpItems that define a observation context.
 * In other words, these are containers for observation components.
 *
 * <h3>Observation Data</h3>
 * This class maintains a reference to "observation data" for the context.
 * The observation data consists of telescope base position and position
 * angle.  The observation data structure gives clients one convenient
 * place to find and monitor this information and frees them from having
 * to watch for target list or instrument item insertions or deletions.
 * <p>
 * The information in the observation data in inherited from containing
 * contexts, if the given context is missing a target list or instrument
 * component.
 *
 * <h3>Observation Chain State</h3>
 * Observation contexts can contain observations (see <code>SpObs</code>).
 * Observations may be chained together to constrain the ordering of their
 * ultimate execution.  It is the responsibility of the observation context
 * keep the "chain state" up-to-date as observations are inserted and
 * removed.
 *
 * @see SpObsData
 * @see gemini.sp.obsComp.SpTelescopeObsComp
 * @see gemini.sp.obsComp.SpInstObsComp
 * @see SpObs
 */
public class SpObsContextItem extends SpItem
                              implements SpBasePosObserver, SpPosAngleObserver
{
   SpObsData _obsData;

/**
 * Construct with the specific SpType of the subclass.
 */
protected SpObsContextItem(SpType spType)
{
   super(spType);
}

/**
 * Override clone to make sure obsData isn't set.
 */
protected Object
clone()
{
   SpObsContextItem spClone = (SpObsContextItem) super.clone();
   spClone._obsData = null;
   return spClone;
}

/**
 * Get the Observation Data associated with this item.
 */
public SpObsData
getObsData()
{
   if (_obsData == null) {
      _obsData = new SpObsData();
   }
   return _obsData;
}

/**
 * The base position has changed, so update the SpObsData maintained
 * by this class.  Updating the SpObsData will notify interested clients.
 */
public void
basePosUpdate(double ra, double dec)
{
   _obsData.setBasePos(ra, dec);
}

/**
 * The position angle has changed, so update the SpObsData maintained
 * by this class.  Updating the SpObsData will notify interested clients.
 */
public void
posAngleUpdate(double posAngle)
{
   _obsData.setPosAngle(posAngle);
}


//
// Overriden methods and helpers to keep observation data up-to-date
//

//
// If the newly inserted item is a telescope, instrument, or obs context
// component, then fix up the ObsData.
//
void
_fixObsDataAfterInsert(SpItem newItem)
{
   if (newItem instanceof SpTelescopeObsComp) {
      SpObsData.completeSpTelescopeObsCompInsertion((SpTelescopeObsComp) newItem);

   } else if (newItem instanceof SpInstObsComp) {
      SpObsData.completeSpInstObsCompInsertion((SpInstObsComp) newItem);

   } else if (newItem instanceof SpObsContextItem) {
      SpObsData.completeSpObsContextItemInsertion((SpObsContextItem) newItem);
   }
}

//
// Fix the obs data after the extraction of a telescope or instrument
// component, or an obs context item.
//
void
_fixObsDataBeforeExtract(SpItem spItem)
{
   if (spItem instanceof SpTelescopeObsComp) {
      SpObsData.prepareSpTelescopeObsCompExtract(spItem);

   } else if (spItem instanceof SpInstObsComp) {
      SpObsData.prepareSpInstObsCompExtract(spItem);

   } else if (spItem instanceof SpObsContextItem) {
      SpObsData.prepareSpObsContextItemExtract(spItem);
   }
}

/**
 * Override insert to fix up the observation chain state, and to check
 * for the insertion of an item that would effect the SpObsData.
 */
protected void
doInsert(SpItem newChild, SpItem afterChild)
{
   super.doInsert(newChild, afterChild);
   _fixObsDataAfterInsert(newChild);
}

/**
 * Override doExtract to fix up the observation data.
 */
protected void
doExtract(SpItem child)
{
   _fixObsDataBeforeExtract(child);
   super.doExtract(child);
}


//
// Overriden methods and helpers to keep chain state up-to-date
//

//
// Fix up the Observation Chain state before its extraction.
//
void
_fixChainStateBeforeExtract(SpItem firstChild, SpItem lastChild)
{
   if (firstChild instanceof SpObs) {
      // If the previous item is also an observation, then we may need to
      // update its "chainedToNext" property.
      SpItem prevItem = firstChild.prev();
      if ((prevItem != null) && (prevItem instanceof SpObs)) {
         SpObs prevObs = (SpObs) prevItem;
         if (prevObs.getChainedToNext()) {
            // The "prevObs" can only remain "chainedToNext" if the
            // item after the lastChild (if any) is an observation that
            // is "chainedToPrev".
            boolean chainedToNext = false;
            SpItem  newNext       = lastChild.next();
            if ((newNext != null) && (newNext instanceof SpObs)) {
               chainedToNext = ((SpObs) newNext).getChainedToPrev();
            }
            prevObs.setChainedToNext( chainedToNext );
         }
      }
      ((SpObs) firstChild).setChainedToPrev(false);
   }

   if (lastChild instanceof SpObs) {
      // If the next item is also an observation, then we may need to
      // update its "chainedToPrev" property.
      SpItem nextItem = lastChild.next();
      if ((nextItem != null) && (nextItem instanceof SpObs)) {
         SpObs nextObs = (SpObs) nextItem;
         if (nextObs.getChainedToPrev()) {
            // The "nextObs" can only remain "chainedToPrev" if the item
            // before the firstChild (if any) is an observation that is
            // "chainedToNext".
            boolean chainedToPrev = false;
            SpItem  newPrev       = firstChild.prev();
            if ((newPrev != null) && (newPrev instanceof SpObs)) {
               chainedToPrev = ((SpObs) newPrev).getChainedToNext();
            }
            nextObs.setChainedToPrev( chainedToPrev );
         }
      }
      ((SpObs) lastChild).setChainedToNext(false);
   }
}


//
// Fix the chained states of Observation after the insertion of a new
// item after an observation.
//
void
_fixChainStateBeforeInsert(SpItem[] newChildren, SpItem afterChild)
{
   if ((afterChild == null) || !(afterChild instanceof SpObs)) {
      return;
   }

   SpObs prevObs = (SpObs) afterChild;

   if (newChildren[0] instanceof SpObs) {
      // The new child is an obs, so continue the chain if inserting
      // in the middle of one.
      SpObs   newObs  = (SpObs) newChildren[0];
      boolean chained = prevObs.getChainedToNext();
      newObs.setChainedToPrev( chained );

      SpItem sib = prevObs.next();
      if (chained && (sib != null) && (sib instanceof SpObs)) {
         SpObs   sibObs        = (SpObs) sib;
         boolean chainedToPrev = false;
         SpItem  lastNewChild  = newChildren[ newChildren.length - 1];
         if (lastNewChild instanceof SpObs) {
            SpObs lastNewObs = (SpObs) lastNewChild;
            chainedToPrev = lastNewObs.getChainedToPrev();
            if (chainedToPrev) {
               lastNewObs.setChainedToNext(true);
            }
         }
         sibObs.setChainedToPrev( chainedToPrev );
      }

   } else {

      // The new child isn't an observation so make sure the
      // the chain is broken
      prevObs.setChainedToNext(false);
      SpItem nextItem = prevObs.next();
      if ((nextItem != null) && (nextItem instanceof SpObs)) {
         SpObs nextObs = (SpObs) nextItem;
         nextObs.setChainedToPrev(false);
      }
   }
}

/**
 * Override <code>insert()</code> to fix the chain state of any
 * observations contained in this context.
 */
protected void
insert(SpItem[] newChildren, SpItem afterChild)
{
   _fixChainStateBeforeInsert(newChildren, afterChild);
   super.insert(newChildren, afterChild);
}

/**
 * Override <code>extract()</code> to fix the chain state of any
 * observations contained in this context.
 */
protected void
extract(SpItem[] children)
{
   _fixChainStateBeforeExtract(children[0], children[ children.length - 1]);
   super.extract(children);
}

/**
 * Override <code>move()</code> to fix the chain state of any
 * observations contained in this context.
 */
protected void
move(SpItem[] children, SpItem newParent, SpItem afterChild)
{
   _fixChainStateBeforeExtract(children[0], children[ children.length - 1]);
   if (newParent instanceof SpObsContextItem) {
      SpObsContextItem newCtx = (SpObsContextItem) newParent;
      newCtx._fixChainStateBeforeInsert(children, afterChild);
   }
   super.move(children, newParent, afterChild);
}

}
