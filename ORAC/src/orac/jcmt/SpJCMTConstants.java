/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.jcmt;

/**
 * Constants for JCMT instrument components and iterators.
 *
 * Defining all ATTR_ constants (used in the {@link gemini.sp.SpAvTable}s
 * of the {@link gemini.sp.SpItem}s) reflects the fact that the corresponding
 * DTD element and attribute definitions are also defined globally in one DTD
 * that contains the element and attribute definitions for all SpItems.
 * In case of DTD changes most necessary changes to the JCMT OT's XML output
 * can be made by changing the string values of the attrubutes in the
 * interface.
 * For more information on how the format of these strings affects the actual
 * XML see {@link orac.util.SpAvTableDOM}.
 *
 * In the UKIRT part of the OT the ATTR_ constants are all defined locally
 * in the classes where there are needed.
 * Defining them all in one file should encourage their reuse throughout the
 * JCMT SpItems.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public interface SpJCMTConstants {

   /** Number of integrations. */
   public static final String ATTR_INTEGRATIONS    = "integrations";

   /**
    * Total time of one integrations.
    *
    * This attribute will probably not be stored in an SpAvTable but
    * calculated on the fly in an Observe iterator specific way
    * whenever needed.
    *
    * @see orac.jcmt.iter.SpIterJCMTObs.getTotalIntegrationTime()
    */
   public static final String ATTR_SECS_PER_INTEGRATION = "secsPerIntegration";


   // Scan Iterator

   /** Map width. */
   public static final String ATTR_X               = "x";

   /** Map height. */
   public static final String ATTR_Y               = "y";

   public static final String ATTR_THETA           = "theta";
   public static final String ATTR_SYSTEM          = "system";

   /** Sample DX. */
   public static final String ATTR_DELTA_X         = "deltaX";

   /** Sample DY. */
   public static final String ATTR_DELTA_Y         = "deltaY";

   public static final String ATTR_COORD_FRAME     = "coordFrame";
   public static final String ATTR_POS_ANGLE       = "posAngle";

   /** Focus Iterator. */
   public static final String ATTR_AXIS            = "axis";
   public static final String ATTR_STEPS           = "steps";
   public static final String ATTR_FOCUS_POINTS    = "focusPoints";

   /**
    * Skydip Iterator.
    *
    * Number of positions.
    */
   public static final String ATTR_POSITIONS       = "positions";
   public static final String ATTR_START_POSITION  = "startPosition";

   public static final String ATTR_UNITS           = "units";

   /**
    * Constant value.
    * @see #ATTR_UNITS
    */
   public static final String VALUE_SECONDS        = "seconds";

   /**
    * Constant value.
    * @see #ATTR_UNITS
    */   
   public static final String VALUE_ARC_SECONDS    = "arcseconds";

   /**
    * Constant value.
    * @see #ATTR_UNITS
    */   
   public static final String VALUE_ARC_MINUTES    = "arcseconds";


   // SCUBA constants (needed for time estimation)

   /** Seconds per integration, SCUBA photom mode. */
   public static final int SECS_PER_INTEGRATION_PHOT  = 18;

   /** Seconds per integration, SCUBA 16pt jiggle mode. */
   public static final int SECS_PER_INTEGRATION_JIG16 = 32;

   /** Seconds per integration, SCUBA 64 pt jiggle mode. */
   public static final int SECS_PER_INTEGRATION_JIG64 = 64;


   /** SCUBA start up time. */
   public static final double SCUBA_STARTUP_TIME      = 40;

   /** Scuba scan map chop frequency in Hz. */
   public static final double SCAN_MAP_CHOP_FREQUENCY = 8;

   /** Scuba array diameter in arcseconds. */
   public static final double SCUBA_ARRAY_DIAMETER    = 138;

}
