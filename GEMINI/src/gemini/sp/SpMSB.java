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

import gemini.sp.obsComp.SpInstObsComp;
import java.util.Enumeration;
import java.lang.Integer;

/**
 * OMP class.
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpMSB extends SpObsContextItem {

   /** This attribute records the observation priority. */
   public static final String ATTR_PRIORITY = "priority";
    public static final String ATTR_LIBRARY_VERSION = "library_version";
    public static final String KEYWORD_IDENTIFIER = "$";
    public static final String LIBRARY_VERSION = "Revision";

   /**
    * High observation priority, relative to the other observations in
    * the science program.
    */
   public static final int PRIORITY_HIGH   = 1;

   /**
    * Medium observation priority, relative to the other observations in
    * the science program.
    */
   public static final int PRIORITY_MEDIUM = 49;

   /**
    * Low observation priority, relative to the other observations in
    * the science program.
    */
   public static final int PRIORITY_LOW    = 99;

   public static String[] PRIORITIES = {
      "High", "Medium", "Low"
   };

    private static final int numPriorities = 99;

   /** This attribute records the number of remaining MSBs. */
   public static final String ATTR_REMAINING = ":remaining";

   /** This attribute records the estimated duration of the MSB. */
   public static final String ATTR_ELAPSED_TIME = "estimatedDuration";

  /**
   * String used to indecate that the MSB has been removed.
   *
   * @see #REMOVED_CODE 
   */
  public static final String REMOVED_STRING = "REMOVED";

    /** This attribute records whether an MSB has been suspended */
    public static final String ATTR_SUSPEND = ":suspend";

  /**
   * The databases uses this number to indecate that an MSB has been removed.
   *
   * Usage: {@link gemini.sp.SpMSB#setNumberRemaining() (REMOVED_CODE)}
   */
  public static final int REMOVED_CODE = -999;

  /**
   * Default constructor.
   */
  protected SpMSB() {
    super(SpType.MSB_FOLDER);
    _avTable.noNotifySet(ATTR_REMAINING, "1", 0);
    _avTable.noNotifySet(ATTR_PRIORITY, "99", 0);
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

   if(getNumberRemaining() == REMOVED_CODE) {
      return title + " (" + REMOVED_STRING + ")";
   }
   else {
      return title + " (" + getNumberRemaining() + "X)";
   }   
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
	  if (i==0) return PRIORITY_HIGH;
	  if (i==1) return PRIORITY_MEDIUM;
	  if (i==2) return PRIORITY_LOW;
      }
   }

   return ((new Integer(str)).intValue());
}

/**
 * Get the observation priority as a human readable String.
 */
public int
getPriorityString()
{
   String str = _avTable.get(ATTR_PRIORITY);
   if (str == null) {
      return PRIORITY_LOW;
   }
   return ((new Integer(str)).intValue());
}
 
/**
 * Set the Observation type.
 */
public void
setPriority(int priority)
{
//    if ((priority < 0) || (priority > PRIORITIES.length)) {
   if ((priority < 0) || (priority > numPriorities)) {
      return;
   }
 
   _avTable.set(ATTR_PRIORITY, priority );
}

/**
 * Set the library verion to the default String.  This should be replaced after
 * the library is commited to CVS.  It is set to the CVS keyword $Revision$.
 *
 */
public void setLibraryRevision() {
    System.out.println("SpMSB::setLibraryRevion() called");
    _avTable.set(ATTR_LIBRARY_VERSION, KEYWORD_IDENTIFIER+LIBRARY_VERSION+KEYWORD_IDENTIFIER);
}

/**
 * Get the version of the library.  
 * @return It will return $Revision$ if this has been checked into CVS or $Revision$ if not.
 */
public String getLibraryRevision() {
    return _avTable.get(ATTR_LIBRARY_VERSION);
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
 * Returns a boolean if the suspend attribute exists
 */
public boolean isSuspended() {
    boolean suspended = false;
    if ( _avTable.exists(ATTR_SUSPEND) ) suspended = true;
    return suspended;
}

/**
 * Unsuspend an MSB
 */
public void unSuspend() {
    _avTable.rm(ATTR_SUSPEND);
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
	if ( !(((SpObs)spItem).isOptional()) ) {
	    elapsedTime += ((SpObs)spItem).getElapsedTime();
	}
    }
  }

  SpInstObsComp spInstObsComp = SpTreeMan.findInstrument(this);

  if(spInstObsComp != null) {
    return elapsedTime + spInstObsComp.getSlewTime();
  }
  else {
    return elapsedTime;
  }  
}

/**
 * Sets the ATTR_ELAPSED_TIME attribute.
 * 
 * I.e. "saves" elapsed time to the SpAvTable of this item.
 */
public void saveElapsedTime() {
  _avTable.set(ATTR_ELAPSED_TIME, getElapsedTime());
//  _avTable.set(ATTR_ELAPSED_TIME + ":units", "seconds");
}

protected void
processAvAttribute(String avAttr, String indent, StringBuffer xmlBuffer)
{

   if(avAttr.equals(ATTR_ELAPSED_TIME)) {
      xmlBuffer.append("\n  "   + indent + "<"  + ATTR_ELAPSED_TIME + " units=\"seconds\">" + getElapsedTime() +
                                           "</" + ATTR_ELAPSED_TIME + ">");

      return;
   }

   super.processAvAttribute(avAttr, indent, xmlBuffer);
}

public void
processXmlAttribute(String elementName, String attributeName, String value)
{
   // Do not save units = seconds to the AV table because the XML attribute units="seconds"
   // is added in processAvAttribute. So ignore units = seconds.
   if(attributeName.equals("units") && value.equals("seconds")) {
     return;
   }

   super.processXmlAttribute(elementName, attributeName, value);
}

}

