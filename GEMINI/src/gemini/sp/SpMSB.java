/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package gemini.sp;


/**
 * OMP class.
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpMSB extends SpObsContextItem {

   /** This attribute records the number of remaining MSBs. */
   public static final String ATTR_REMAINING = ":remaining";

  /**
   * Default constructor.
   */
  protected SpMSB() {
    super(SpType.MSB_FOLDER);
    _avTable.noNotifySet(ATTR_REMAINING, "1", 0);
  }

/**
 * Get the observation priority.
 */
public int
getPriority()
{
   String str = _avTable.get(SpObs.ATTR_PRIORITY);
   if (str == null) {
      return SpObs.PRIORITY_LOW;
   }

   for (int i=0; i<SpObs.PRIORITIES.length; ++i) {
      if (str.equals(SpObs.PRIORITIES[i])) {
         return i;
      }
   }
   return SpObs.PRIORITY_LOW;
}

/**
 * Get the observation priority as a human readable String.
 */
public String
getPriorityString()
{
   String str = _avTable.get(SpObs.ATTR_PRIORITY);
   if (str == null) {
      return SpObs.PRIORITIES[0];
   }
   return str;
}
 
/**
 * Set the Observation type.
 */
public void
setPriority(int priority)
{
   if ((priority < 0) || (priority > SpObs.PRIORITIES.length)) {
      return;
   }
 
   _avTable.set(SpObs.ATTR_PRIORITY, SpObs.PRIORITIES[priority]);
}


/**
 * Get the number of observations done.
 *
 * Added for OMP (MFO, 9 August 2001)
 *
 * @return number of observations done or 0 if the attribute has not been set.
 */
public int
getNumberDone()
{
   return _avTable.getInt(ATTR_DONE, 0);
}

/**
 * Set status attribute.
 *
 * Added for OMP (MFO, 9 August 2001)
 */
public void
setNumberDone(int done)
{
   _avTable.set(ATTR_DONE, done);
}


/**
 * Get the number of observations remaining to be observed.
 *
 * Added for OMP (MFO, 9 August 2001)
 *
 * @return number of observations remaining to be observed or 1 if the attribute has not been set.
 */
public int
getNumberRemaining()
{
   return _avTable.getInt(ATTR_REMAINING, 1);
}

/**
 * Set status attribute.
 *
 * Added for OMP (MFO, 9 August 2001)
 */
public void
setNumberRemaining(int remaining)
{
   _avTable.set(ATTR_REMAINING, remaining);
}


}

