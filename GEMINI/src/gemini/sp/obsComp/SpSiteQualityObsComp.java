// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.obsComp;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

/**
 * Site Quality observation component.
 */
public class SpSiteQualityObsComp extends SpObsComp
{
   public static final String ATTR_SEEING_MIN   = "seeing.min";
   public static final String ATTR_SEEING_MAX   = "seeing.max";
   public static final String ATTR_CSO_TAU_MIN  = "csoTau.min";
   public static final String ATTR_CSO_TAU_MAX  = "csoTau.max";
   public static final String ATTR_MOON         = "moon";
   public static final String ATTR_CLOUD        = "cloud";

   public static final int SEEING_EXCELLENT     = 0;
   public static final int SEEING_GOOD          = 1;
   public static final int SEEING_POOR          = 2;
   public static final int SEEING_ANY           = 3;

   public static final double [][] SEEING_RANGES = {
      {0.0, 0.4},
      {0.4, 0.6},
      {0.6, 0.8}
   };

   public static final int CSO_TAO_VERY_DRY = 0;
   public static final int CSO_TAO_ANY      = 1;

   public static final double [][] CSO_TAO_RANGES = {
      {0.0, 0.09}
   };

   public static final int MOON_DARK = 0;
   public static final int MOON_GREY = 1;
   public static final int MOON_ANY  = 2;

   /** Number of Moon options. */
   public static final int MOON_OPTIONS_LENGTH = 3;

   public static final int CLOUD_PHOTOMETRIC = 0;
   public static final int CLOUD_THIN_CIRRUS = 1;
   public static final int CLOUD_ANY         = 2;

   /** Number of Cloud options. */
   public static final int CLOUD_OPTIONS_LENGTH = 3;

   public static final String SUBTYPE = "schedInfo";

   public static final SpType SP_TYPE =
   	SpType.create(SpType.OBSERVATION_COMPONENT_TYPE, SUBTYPE, "Site Quality");

   // Register the prototype.
   static {
     SpFactory.registerPrototype(new SpSiteQualityObsComp());
   }


/**
 * Default constructor.  Initialize the component type.
 */
public SpSiteQualityObsComp()
{
   // MFO: Changed because UKIRT and JCMT use different site quality components.
   super(SP_TYPE);
   //super( SpType.OBSERVATION_COMPONENT_SITE_QUALITY );
}

/**
 * Set Seeing index.
 *
 * @param seeing One of {@link #SEEING_EXCELLENT}, {@link #SEEING_GOOD}, {@link #SEEING_POOR}, {@link #SEEING_ANY}.
 */
public void
setSeeing(int seeing)
{
   if ((seeing < 0) || (seeing >= (SEEING_RANGES.length) || (seeing == SEEING_ANY))) {
      _avTable.rm(ATTR_SEEING_MIN);
      _avTable.rm(ATTR_SEEING_MAX);
   }
   else {
      _avTable.set(ATTR_SEEING_MIN, SEEING_RANGES[seeing][0]);
      _avTable.set(ATTR_SEEING_MAX, SEEING_RANGES[seeing][1]);
   }
}

/**
 * Get Seeing index.
 *
 * @return One of {@link #SEEING_EXCELLENT}, {@link #SEEING_GOOD}, {@link #SEEING_POOR}, {@link #SEEING_ANY}.
 */
public int
getSeeing()
{
   if(_avTable.get(ATTR_SEEING_MIN) == null) {
      return SEEING_ANY;
   }
   
   // Default -1.0 ensure that no range minimum matches and SEEING_ANY is returned.
   double min = _avTable.getDouble(ATTR_SEEING_MIN, -1.0);

   if(min == SEEING_RANGES[SEEING_EXCELLENT][0]) return SEEING_EXCELLENT;
   if(min == SEEING_RANGES[SEEING_GOOD][0])      return SEEING_GOOD;
   if(min == SEEING_RANGES[SEEING_POOR][0])      return SEEING_POOR;
   
   return SEEING_ANY;
}

/**
 * Get Seeing range as array of two doubles.
 */
public double []
getSeeingRange()
{
   double [] result = new double[]{
      _avTable.getDouble(ATTR_SEEING_MIN, -1), _avTable.getDouble(ATTR_SEEING_MAX, -1)
   };

   if((result[0] == -1) || (result[1] == -1)) {
      return null;
   }
   else {
      return result;
   }
}

//--

/**
 * Set CSO Tau index.
 *
 * @param csoTau One of {@link #CSO_TAO_VERY_DRY}, {@link #CSO_TAO_ANY}.
 */
public void
setCsoTau(int csoTau)
{
   if ((csoTau < 0) || (csoTau >= (CSO_TAO_RANGES.length) || (csoTau == CSO_TAO_ANY))) {
      _avTable.rm(ATTR_CSO_TAU_MIN);
      _avTable.rm(ATTR_CSO_TAU_MAX);
   }
   else {
      _avTable.set(ATTR_CSO_TAU_MIN, CSO_TAO_RANGES[csoTau][0]);
      _avTable.set(ATTR_CSO_TAU_MAX, CSO_TAO_RANGES[csoTau][1]);
   }
}

/**
 * Get CSO Tau index.
 *
 * @return One of {@link #CSO_TAO_VERY_DRY}, {@link #CSO_TAO_ANY}.
 */
public int
getCsoTau()
{
   if(_avTable.get(ATTR_CSO_TAU_MIN) == null) {
      return CSO_TAO_ANY;
   }

   // Default -1.0 ensure that no range minimum matches and CSO_TAO_ANY is returned.
   double min = _avTable.getDouble(ATTR_SEEING_MIN, -1.0);

   if(min == CSO_TAO_RANGES[CSO_TAO_VERY_DRY][0]) return CSO_TAO_VERY_DRY;
   
   return CSO_TAO_ANY;
}

/**
 * Get CSO Tau range as array of two doubles.
 */
public double []
getCsoTauRange()
{
   double [] result = new double[]{
      _avTable.getDouble(ATTR_CSO_TAU_MIN, -1), _avTable.getDouble(ATTR_CSO_TAU_MAX, -1)
   };

   if((result[0] == -1) || (result[1] == -1)) {
      return null;
   }
   else {
      return result;
   }
}


/**
 * Set Moon index.
 *
 * @param moon One of {@link #MOON_DARK}, {@link #MOON_GREY}, {@link #MOON_ANY}.
 */
public void
setMoon(int moon)
{
   if ((moon < 0) || (moon >= MOON_OPTIONS_LENGTH) || (moon == MOON_ANY)) {
      _avTable.rm(ATTR_MOON);
   }
   else {
      _avTable.set(ATTR_MOON, moon);
   }
}

/**
 * Get Moon index.
 *
 * @return One of {@link #MOON_DARK}, {@link #MOON_GREY}, {@link #MOON_ANY}.
 */
public int
getMoon()
{
   return _avTable.getInt(ATTR_MOON, MOON_ANY);
}


/**
 * Set Cloud index.
 *
 * @param cloud One of {@link #CLOUD_PHOTOMETRIC}, {@link #CLOUD_THIN_CIRRUS}, {@link #CLOUD_ANY}.
 *
 */
public void
setCloud(int cloud)
{
   if ((cloud < 0) || (cloud >= CLOUD_OPTIONS_LENGTH) || (cloud == CLOUD_ANY)) {
      _avTable.rm(ATTR_CLOUD);
   }
   else {
      _avTable.set(ATTR_CLOUD, cloud);
   }
}

/**
 * Get Cloud index.
 *
 * @return One of {@link #CLOUD_PHOTOMETRIC}, {@link #CLOUD_THIN_CIRRUS}, {@link #CLOUD_ANY}.
 */
public int
getCloud()
{
   return _avTable.getInt(ATTR_CLOUD, CLOUD_ANY);
}

}

