// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import gemini.sp.iter.SpIterFolder;

/**
 * The observation item.  In addition to other attributes, the SpObs class
 * contains two attributes that determine whether the observation is chained
 * to the next or previous observation (if any).
 * 17Apr00 AB Added standard flag to this.
 */
public class SpObs extends SpObsContextItem
{
   /**
    * This attribute determines whether or not the observation is chained
    * to the next observation.
    */
   public static final String ATTR_CHAINED_NEXT = "chainedToNext";

   /**
    * This attribute determines whether or not the observation is chained
    * to the prev observation.
    */
   public static final String ATTR_CHAINED_PREV = "chainedToPrev";

   /** This attribute records if the obs. is to be treated as a "standard"*/
   public static final String ATTR_STANDARD = "standard";

   /** This attribute records the observation priority. */
   public static final String ATTR_PRIORITY = "priority";

   /** This attribute records the observation status. */
   public static final String ATTR_STATUS_DONE = "status";
   

   /**
    * High observation priority, relative to the other observations in
    * the science program.
    */
   public static final int PRIORITY_HIGH   = 0;

   /**
    * Medium observation priority, relative to the other observations in
    * the science program.
    */
   public static final int PRIORITY_MEDIUM = 1;

   /**
    * Low observation priority, relative to the other observations in
    * the science program.
    */
   public static final int PRIORITY_LOW    = 2;

   public static String[] PRIORITIES = {
      "High", "Medium", "Low"
   };

/**
 * Default constructor.  Initializes the Observation with required items.
 */
protected SpObs()
{
   super(SpType.OBSERVATION);
   _avTable.noNotifySet(ATTR_CHAINED_NEXT, "false", 0);
   _avTable.noNotifySet(ATTR_CHAINED_PREV, "false", 0);
   _avTable.noNotifySet(ATTR_STANDARD, "false", 0);
}

/**
 * Construct an observation with the given iterator (sequence) folder.
 */
protected SpObs(SpIterFolder ifPrototype)
{
   this();
   doInsert(ifPrototype, null);
}

/**
 * Override clone to erase the chained state.
 */
protected Object
clone()
{
   SpItem spClone = (SpItem) super.clone();
   spClone._avTable.noNotifySet(ATTR_CHAINED_NEXT, "false", 0);
   spClone._avTable.noNotifySet(ATTR_CHAINED_PREV, "false", 0);
   return spClone;
}

/**
 * Override getTitle so that it simply returns the "title" attribute if
 * set.
 */
public String
getTitle()
{
   String title = getTitleAttr();
   if ((title == null) || title.equals("")) {
      title = type().getReadable();
   }
   return title;
}

/**
 * Get the "standard" flag of the observation.
 *
 */
public boolean
getIsStandard()
{
   return _avTable.getBool(ATTR_STANDARD);
}

/**
 * Set the "standard" flag of the observation.
 *
 */
public void
setIsStandard(boolean standard)
{
  _avTable.set(ATTR_STANDARD, standard);
}

/**
 * Get the "chained to next" state of the observation.  When consecutive
 * observations are chained they must be executed in the same order
 * as they occur in the Science Program.  The "chained to next" attribute
 * determines whether the next consecutive observation is chained to this
 * one.
 *
 * @see #chainToNext
 */
public boolean
getChainedToNext()
{
   return _avTable.getBool(ATTR_CHAINED_NEXT);
}

//
// Set the "chained to next" state of the observation.
//
// @see #getChainedToNext
//
void
setChainedToNext(boolean chained)
{
   if (getChainedToNext() != chained) {
      _avTable.set(ATTR_CHAINED_NEXT, chained);
   }
}

/**
 * Chain this observation to the next one.  This has no effect if
 * the next SpItem isn't an observation.
 */
public void
chainToNext(boolean chain)
{
   if (next() == null) {
      return;
   }

   if (next() instanceof SpObs) {
      setChainedToNext(chain);
      ((SpObs) next()).setChainedToPrev(chain);
   }
}


/**
 * Get the "chained to prev" state of the observation.  When consecutive
 * observations are chained they must be executed in the same order
 * as they occur in the Science Program.  The "chained to prev" attribute
 * determines whether the previous observation is chained to this one.
 */
public boolean
getChainedToPrev()
{
   return _avTable.getBool(ATTR_CHAINED_PREV);
}

//
// Set the "chained to prev" state of the observation.
//
// @see #getChainedToPrev
//
void
setChainedToPrev(boolean chained)
{
   if (getChainedToPrev() != chained) {
      _avTable.set(ATTR_CHAINED_PREV, chained);
   }
}

/**
 * Override setTable to make sure that the chained states are valid.
 */
protected void
setTable(SpAvTable avTable)
{
   // Make the new "chained to previous" state to equal the current one.
   Boolean b = new Boolean( getChainedToPrev() );
   avTable.noNotifySet(ATTR_CHAINED_PREV, b.toString(), 0);

   // If the new "chained to next" state isn't equal to the current one,
   // then ...
   boolean cNext = avTable.getBool(ATTR_CHAINED_NEXT);
   if (getChainedToNext() != cNext) {

      if ((next() == null) || !(next() instanceof SpObs)) {
         // Make sure the new state is false
         avTable.noNotifySet(ATTR_CHAINED_NEXT, "false", 0);
      } else {
         // Update the next obs' "chained to prev" to reflect this obs'
         // "chained to next".
         ((SpObs) next()).setChainedToPrev(cNext);
      }
   }

   super.setTable(avTable);
}

/**
 * Get the observation priority.
 */
public int
getPriority()
{
   String str = _avTable.get(ATTR_PRIORITY);
   if (str == null) {
      return PRIORITY_LOW;
   }

   for (int i=0; i<PRIORITIES.length; ++i) {
      if (str.equals(PRIORITIES[i])) {
         return i;
      }
   }
   return PRIORITY_LOW;
}

/**
 * Get the observation priority as a human readable String.
 */
public String
getPriorityString()
{
   String str = _avTable.get(ATTR_PRIORITY);
   if (str == null) {
      return PRIORITIES[0];
   }
   return str;
}
 
/**
 * Set the Observation type.
 */
public void
setPriority(int priority)
{
   if ((priority < 0) || (priority > PRIORITIES.length)) {
      return;
   }
 
   _avTable.set(ATTR_PRIORITY, PRIORITIES[priority]);
}

/**
 * Get status attribute.
 *
 * Added for OMP (MFO, 7 August 2001)
 *
 * @return country or "" if attribute hasn't been set.
 */
public boolean
isDone()
{
   return _avTable.getBool(ATTR_STATUS_DONE);
}

/**
 * Set status attribute.
 *
 * Added for OMP (MFO, 7 August 2001)
 */
public void
setDone(boolean status)
{
   _avTable.set(ATTR_STATUS_DONE, status);
}

}
