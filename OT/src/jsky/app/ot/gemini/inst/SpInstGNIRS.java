// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gemini.inst;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.obsComp.SpInstObsComp;

/**
 * The GNIRS instrument.
 */
public class SpInstGNIRS extends SpInstObsComp
{
   public final static int GNIRS_LARGE = 0;
   public final static int GNIRS_SMALL = 1;

   public static final SpType SP_TYPE =
        SpType.create(SpType.OBSERVATION_COMPONENT_TYPE, "inst.GNIRS", "GNIRS");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpInstGNIRS());
}


public SpInstGNIRS()
{
   super(SP_TYPE);
   _avTable.noNotifySet("scienceArea", "100", 0);
}

/**
 * Return the size of the OIWFS field radius in arcsec.
 */
public int getOIWFSRadius()
{
   return 90;  // (arcsec) which is 1.5 arcmin
}

/**
 * Return the slit size for the given mode.
 */
public double[]
getScienceArea(int mode)
{
   switch (mode) {
      case GNIRS_LARGE: { return new double[] { 1.0, 100.0 }; }
      case GNIRS_SMALL: { return new double[] { 1.0,  50.0 }; }
   }
   return null;
}

/**
 * Override getSlitLength to return the slit length based upon the
 * scienceArea attribute.
 */
public double[]
getScienceArea()
{
   String scienceArea = _avTable.get("scienceArea");
   if ((scienceArea == null) || scienceArea.equals("100")) {
      return getScienceArea(GNIRS_LARGE);
   }
   return getScienceArea(GNIRS_SMALL);
}

}
