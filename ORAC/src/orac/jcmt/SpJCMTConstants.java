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

   
   // Generic JCMT iterator. Many of the attribute are actually only used for the
   // Heterodyne instrument.

   /** Switching mode (Nod, Chop, Frequency, None). */
   public static final String ATTR_SWITCHING_MODE          = "switchingMode";

   /** Reference offset, x. */
   public static final String ATTR_REFERENCE_OFFSET_X      = "referenceOffset.x";

   /** Reference offset, y. */
   public static final String ATTR_REFERENCE_OFFSET_Y      = "referenceOffset.y";

   /** Reference offset, system. */
   public static final String ATTR_REFERENCE_OFFSET_SYSTEM = "referenceOffset.system";

   /** Frequency offset, throw. */
   public static final String ATTR_FREQUENCY_OFFSET_THROW  = "frequencyOffset.throw";

   /** Frequency offset, rate. */
   public static final String ATTR_FREQUENCY_OFFSET_RATE   = "frequencyOffset.rate";


   // Scan/Raster Iterator
   public static final String ATTR_X_CENTER        = "scanArea.rectangle.xCenter";
   public static final String ATTR_Y_CENTER        = "scanArea.rectangle.yCenter";
   public static final String ATTR_WIDTH           = "scanArea.rectangle.width";
   public static final String ATTR_HEIGHT          = "scanArea.rectangle.height";
   public static final String ATTR_RECTANGLE_PA    = "scanArea.rectangle.rectanglePA";
   public static final String ATTR_OFF_SYSTEM      = "scanArea.offSystem:type";


   public static final String ATTR_SECS_PER_CYCLE      = "secsPerCycle";
   public static final String ATTR_NO_OF_CYCLES        = "noOfCycles";
   public static final String ATTR_CYCLE_REVERSAL      = "cycleReversal";
   public static final String ATTR_JIGGLE_PATTERN      = "jigglePattern";
   public static final String ATTR_STEP_SIZE           = "stepSize";
   public static final String ATTR_JIGGLE_AT_REFERENCE = "jiggleAtReference";
   public static final String ATTR_SAMPLE_TIME         = "sampleTime";
   public static final String ATTR_JIGGLES_PER_CYCLE   = "jigglePerCycle";


   /** Focus Iterator. */
   public static final String ATTR_AXIS                = "axis";
   public static final String ATTR_STEPS               = "steps";
   public static final String ATTR_FOCUS_POINTS        = "focusPoints";

   /** Pointing Iterator */
   public static final String ATTR_SPECTRAL_MODE       = "spectralMode";
   public static final String SPECTRAL_MODE_CONTINUUM  = "continuum";
   public static final String SPECTRAL_MODE_SPECTRAL_LINE = "spectralLine";
   public static final String ATTR_POINTING_PIXEL      = "pointingPixel";
   public static final String POINTING_PIXEL_AUTOMATIC = "Automatic";
   public static final String POINTING_PIXEL_MANUAL    = "Manual";

   /** Raster Iterator */
   public static final String ATTR_RASTER_MODE         = "rasterMode";
   public static final String RASTER_MODE_ALONG_ROW    = "alongRow";
   public static final String RASTER_MODE_INTERLEAVED  = "interleaved";
   public static final String ATTR_ROW_REVERSAL        = "rowReversal";
   public static final String ATTR_ROWS_PER_CAL        = "rowsPerCal";
   public static final String ATTR_ROWS_PER_REF        = "rowsPerRef";


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
   public static final String VALUE_DEGREES        = "degrees";

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
   public static final String VALUE_ARC_MINUTES    = "arcminutes";


   // SCUBA constants

   /** Filter */
   public static final String ATTR_FILTER             = "filter";

   /** Sub-instrument */
   public static final String ATTR_SUB_INSTRUMENT     = "subInstrument";

   /** Bolometer */
   public static final String ATTR_BOLOMETER          = "bolometer";


   // SCUBA constants for time estimation

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
