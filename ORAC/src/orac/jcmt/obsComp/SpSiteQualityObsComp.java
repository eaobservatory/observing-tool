/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.jcmt.obsComp;

import gemini.sp.obsComp.SpObsComp;
import gemini.sp.SpFactory;
import gemini.sp.SpType;

/**
 * Site Quality observation component.
 */
public class SpSiteQualityObsComp extends SpObsComp
{

   public static final String ATTR_TAU_BAND      = "tauBand";
   public static final String ATTR_SEEING        = "seeing";
   public static final int NO_VALUE              = 0;

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
   super(SP_TYPE);

   _avTable.noNotifySet(ATTR_TAU_BAND, "1", 0);
   _avTable.noNotifySet(ATTR_SEEING,   "1", 0);
}


/**
 * Set tau band.
 *
 * The argument tauBand refers directly to a tau band (band 1 = "low" and band 2 = "high") and
 * <i>not</i> to  an index for an array of tau band options.
 * Therefore the smallest meaningful value is 1 and <i>not</i> 0.
 */
public void
setTauBand(int tauBand)
{
   _avTable.set(ATTR_TAU_BAND, tauBand);
}

/**
 * Get the tau band.
 *
 * @return tau band (not an index for an array of tau band options)
 */
public int
getTauBand()
{
   return _avTable.getInt(ATTR_TAU_BAND, NO_VALUE);
}


/**
 * Set seeing.
 *
 * The argument seeing refers directly to the "state" (e.g. 1 (< 0.4), 2 (0.4 .. 0.8), 3 (> 0.4) etc) and
 * <i>not</i> to  an index for an array of seeing options.
 * Therefore the smallest meaningful value is 1 and <i>not</i> 0.
 */
public void
setSeeing(int seeing)
{
   _avTable.set(ATTR_SEEING, seeing);
}

/**
 * Get seeing.
 *
 * @return seeing state (not an index for an array of seeing options)
 */
public int
getSeeing()
{
   return _avTable.getInt(ATTR_SEEING, NO_VALUE);
}

}

