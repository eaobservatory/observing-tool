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

import java.util.Enumeration;

/**
 * OMP class.
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpMSB extends SpObsContextItem {

   /** This attribute records the observation priority. */
   public static final String ATTR_PRIORITY = "priority";

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

   /** This attribute records the number of remaining MSBs. */
   public static final String ATTR_REMAINING = ":remaining";

   /** This attribute records the number of remaining MSBs. */
   public static final String ATTR_ELAPSED_TIME = "elapsedTime";


  /**
   * Default constructor.
   */
  protected SpMSB() {
    super(SpType.MSB_FOLDER);
    _avTable.noNotifySet(ATTR_REMAINING, "1", 0);
    _avTable.noNotifySet(ATTR_PRIORITY, PRIORITIES[PRIORITY_LOW], 0);
  }

  /**
   * Constructor with the specific SpType.
   *
   * This constructor can be called from subclasses. Note that it deliberately
   * does not initialise _avTable values. This should be done in the constructor of the
   * subclass. This is usefull because the {@link gemini.sp.SpObs} constructor 
   * initialises its _avTable values dependend on whether the OT runs in OMP mode
   * or not. The {@link #SpMSB} constructor however always assumes OMP mode. 
   */
  protected SpMSB(SpType spType) {
    super(spType);
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
   return title + " (" + getNumberRemaining() + "X)";
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

/**
 * Calculates the duration of this MSB.
 *
 * Note that this method does <B>not</B> return ATTR_ELAPSED_TIME.
 * The return value is re-calculated whenever the method is called.
 * {@link #updateElapsedTime()} and {@link #ATTR_ELAPSED_TIME} are only
 * used in order to get the estimated time written to the XML output.
 */
public double getElapsedTime()
{
  double elapsedTime = 0.0;
  Enumeration children = children();
  SpItem spItem = null;

  while(children.hasMoreElements()) {
    spItem = (SpItem)children.nextElement();

    if(spItem instanceof SpObs) {
      elapsedTime += ((SpObs)spItem).getElapsedTime();
    }
  }

  return elapsedTime;
}

/**
 * Sets the ATTR_ELAPSED_TIME attribute.
 * 
 * I.e. "saves" elapsed time to the SpAvTable of this item.
 */
public void saveElapsedTime() {
  _avTable.set(ATTR_ELAPSED_TIME, getElapsedTime());
}

}

