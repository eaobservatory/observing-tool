// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import gemini.sp.iter.SpIterFolder;
import java.util.Enumeration;

/**
 * The observation item.  In addition to other attributes, the SpObs class
 * contains two attributes that determine whether the observation is chained
 * to the next or previous observation (if any).
 * 17Apr00 AB Added standard flag to this.
 */
public class SpObs extends SpMSB
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

   /**
    * This attribute is true if the SpObs is not inside an SpMSB because
    * in that case the observation is an MSB in its own right.
    */
   public static final String ATTR_MSB = ":msb";

   /** This attribute records whether the calibration observation is optional. */
   public static final String ATTR_OPTIONAL = ":optional";   


/**
 * Default constructor.  Initializes the Observation with required items.
 */
protected SpObs()
{
   super(SpType.OBSERVATION);
   if(System.getProperty("OMP") != null) {
      _avTable.noNotifySet(ATTR_REMAINING, "1", 0);
      _avTable.noNotifySet(ATTR_OPTIONAL, "false", 0);
      _avTable.noNotifySet(ATTR_PRIORITY, PRIORITIES[PRIORITY_LOW], 0);
   }
   else {
      _avTable.noNotifySet(ATTR_CHAINED_NEXT, "false", 0);
      _avTable.noNotifySet(ATTR_CHAINED_PREV, "false", 0);
   }   
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
   if(System.getProperty("OMP") == null) {
      spClone._avTable.noNotifySet(ATTR_CHAINED_NEXT, "false", 0);
      spClone._avTable.noNotifySet(ATTR_CHAINED_PREV, "false", 0);
   }   
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
   
   if((System.getProperty("OMP") != null) && (isMSB())) {
      if(getNumberRemaining() == REMOVED_CODE) {
         return title + " (" + REMOVED_STRING + ")";
      }
      else {
         return title + " (" + getNumberRemaining() + "X)";
      }   
   }
   else {
     return title;
   }
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
 * Get the MSB flag of the observation.
 *
 * Added for OMP. <!-- MFO, 27 August 2001 -->
 */
public boolean
isMSB()
{
   return _avTable.getBool(ATTR_MSB);
}

/**
 * Set the MSB attribute of the observation.
 *
 * Added for OMP. <!-- MFO, 27 August 2001 -->
 */
public void
updateMsbAttributes()
{
  int editState = getAvEditFSM().getState();

  // Note that _avTable.set is used instead of _avTable.noNotifySet and that
  // the SpAvEditState which is set to EDITED as a consequence is reset
  // immediately (if it was UNEDITED before). The is done deliberately.
  // If noNotifySet was used instead then the title would not always be displayed/updated
  // correctly with respect to whether or not isMSB() is true or false.
  // (Only isMSB() == true then getNumberRemaining() is displayed in the tree in brackets
  // after the component title.) 

  // If the parent component is an MSB then this SpObs is not.
  if(parent() instanceof SpMSB) {
    _avTable.set(ATTR_MSB, "false");

    // If this SpObs is not and MSB then it does not have a priority. Remove the priority.
    _avTable.rm(SpObs.ATTR_PRIORITY);    
  }
  else {
    _avTable.set(ATTR_MSB, "true");

    // If this SpObs is an MSB then it cannot be optional.
    setOptional(false);
  }

  if(editState == SpAvEditState.UNEDITED) {
    // save() just means reset() in this context.
    getAvEditFSM().save();
  }
}


/**
 * Indicates whether the calibration observation is optional.
 *
 * Added for OMP (MFO, 22 October 2001)
 *
 * @return true if calibration is optional.
 */
public boolean
isOptional()
{
   return _avTable.getBool(ATTR_OPTIONAL);
}

/**
 * Set true if calibration observatiob is optional, false otherwise.
 *
 * Added for OMP (MFO, 22 October 2001)
 */
public void
setOptional(boolean optional)
{
   _avTable.set(ATTR_OPTIONAL, optional);
}

public double
getElapsedTime()
{
  SpIterFolder iterFolder = getIterFolder();
  
  if(iterFolder != null) {
    return iterFolder.getElapsedTime();
  }
  else {
    return 0.0;
  }
}

public SpIterFolder
getIterFolder()
{
  Enumeration children = children();
  SpItem spItem = null;

  while(children.hasMoreElements()) {
    spItem = (SpItem)children.nextElement();

    if(spItem instanceof SpIterFolder) {
      return (SpIterFolder)spItem;
    }
  }

  return null;
}

}
