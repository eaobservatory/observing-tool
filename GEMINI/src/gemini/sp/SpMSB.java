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

  /**
   * Default constructor.
   */
  protected SpMSB() {
    super(SpType.MSB_FOLDER);
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
 * Get status attribute.
 *
 * @return country or "" if attribute hasn't been set.
 */
public boolean
isDone()
{
   return _avTable.getBool(SpObs.ATTR_STATUS_DONE);
}

/**
 * Set status attribute.
 */
public void
setDone(boolean status)
{
   _avTable.set(SpObs.ATTR_STATUS_DONE, status);
}

}

