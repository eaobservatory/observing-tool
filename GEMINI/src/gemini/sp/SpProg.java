// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

/**
 * The science program item.
 */
public class SpProg extends SpRootItem
{

   /** The PI (principal investigator) attribute. */
   public static final String ATTR_PI = "pi";

   /** The country attribute. */
   public static final String ATTR_COUNTRY = "country";

   /** The project ID. */
   public static final String ATTR_PROJECT_ID = "projectID";

   /** The timestamp. */
   public static final String ATTR_TIMESTAMP = ":timestamp";

   // The Phase 1 proposal item.  It stores all the information entered
   // during the Phase 1 proposal definition.
   private SpPhase1 _phase1Item;

/**
 * Default constructor.
 */
protected SpProg()
{
   super(SpType.SCIENCE_PROGRAM);
}

//
// Set the Phase 1 item to associate with this program.
//
void
setPhase1Item(SpPhase1 p1)
{
   _phase1Item = p1;
}

/**
 * Get the Phase 1 item associated with this program (if any).  If there
 * is no Phase 1 item for this program, this method will return null.
 */
public SpPhase1
getPhase1Item()
{
   return _phase1Item;
}

/**
 * Get PI (principle investigator) attribute.
 *
 * Added for OMP (MFO, 7 August 2001)
 *
 * @return PI or "" if attribute hasn't been set.
 */
public String
getPI()
{
   if(_avTable.get(ATTR_PI) != null) {
      return _avTable.get(ATTR_PI);
   }
   else {
      return "";
   }
}

/**
 * Set PI (principle investigator) attribute.
 *
 * Added for OMP (MFO, 7 August 2001)
 */
public void
setPI(String pi)
{
   _avTable.set(ATTR_PI, pi);
}

/**
 * Get country attribute.
 *
 * Added for OMP (MFO, 7 August 2001)
 *
 * @return country or "" if attribute hasn't been set.
 */
public String
getCountry()
{
   if(_avTable.get(ATTR_COUNTRY) != null) {
      return _avTable.get(ATTR_COUNTRY);
   }
   else {
      return "";
   }
}

/**
 * Set country attribute.
 *
 * Added for OMP (MFO, 7 August 2001)
 */
public void
setCountry(String country)
{
   _avTable.set(ATTR_COUNTRY, country);
}

/**
 * Get country attribute.
 *
 * Added for OMP (MFO, 7 August 2001)
 *
 * @return country or "" if attribute hasn't been set.
 */
public String
getProjectID()
{
   if(_avTable.get(ATTR_PROJECT_ID) != null) {
      return _avTable.get(ATTR_PROJECT_ID);
   }
   else {
      return "";
   }
}

/**
 * Set project ID.
 *
 * Added for OMP (MFO, 7 August 2001)
 */
public void
setProjectID(String projectID)
{
   _avTable.set(ATTR_PROJECT_ID, projectID);
}

/**
 * Set timestamp.
 *
 * Note that calling this method does not effect the state machine of this
 * Science Program item.
 * This is because a call to setTimestamp() is not the result editing the item.
 * It is called after a program has been stored to database to set the timestamp
 * of the Science Program to the timestamp that is returned when omp.SpClient.storeProgram
 * is called.
 *
 * Added for OMP (MFO, 12 November 2001)
 */
public void
setTimestamp(int timestamp)
{
   _avTable.noNotifySet(ATTR_TIMESTAMP, "" + timestamp, 0);
}


}


