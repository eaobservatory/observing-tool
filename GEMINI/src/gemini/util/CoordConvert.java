// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util;

/** 
 * This is a utility class containing coordinate conversion methods for
 * converting between RA/Dec FK5 (J2000) and FK4 (B1950).
 *
 * <p>
 * The code is based on Starlink subroutines by Patrick Wallace.  This code
 * has been converted almost directly from C code by Doug Mink,
 * Harvard-Smithsonian Center for Astrophysics.
 *
 * @author	Shane Walker
 */
public class CoordConvert
{
   // **** Useful Constants ****

   // 2 PI
   private static final double d2pi = 6.283185307179586476925287;

   // Radians per year to arcsec per century.
   private static final double pmf = 100.0 * 60.0 * 60.0 * 360.0 / d2pi;

   // Small number to avoid arithmetic problems.
   private static final double tiny = 1.e-30;

   private static final double zero = 0.0;

   // Radians per degree.
   private static final double rcon = 1.74532925199433e-2;

   // Do diagnostics?
   private static final boolean diag = false;

   /**
    * Constant vector and matrix (by columns)
    * These values were obtained by inverting C.Hohenkerk's forward matrix
    * (private communication), which agrees with the one given in reference
    * 2 but which has one additional decimal place.
    */
   private static double[] a  = {-1.62557e-6,	-0.31919e-6,	-0.13843e-6};
   private static double[] ad = {1.245e-3,	-1.580e-3,	-0.659e-3  };

   /**
    *  FK524  convert J2000 FK5 star data to B1950 FK4
    * based on Starlink sla_fk524 by P.T.Wallace 27 October 1987
    */
/*
   private static double[][] emi = {
	 {0.999925679499910, -0.011181482788805, -0.004859004008828,
	 -0.000541640798032, -0.237963047085011,  0.436218238658637},

	 {0.011181482840782,  0.999937484898031, -0.000027155744957,
	  0.237912530551179, -0.002660706488970, -0.008537588719453},

	 {0.004859003889183, -0.000027177143501,  0.999988194601879,
	 -0.436101961325347,  0.012258830424865,  0.002119065556992},

	{-0.000002423898405,  0.000000027105439,  0.000000011777422,
	  0.999904322043106, -0.011181451601069, -0.004858519608686},

	{-0.000000027105439, -0.000002423927017,  0.000000000065851,
	  0.011181451608968,  0.999916125340107, -0.000027162614355},

	{-0.000000011777422,  0.000000000065846, -0.000002424049954,
	  0.004858519590501, -0.000027165866691,  0.999966838131419}
   };
*/

   private static double[][] emi = {
     {  0.9999256795,              /* emi[0][0] */
        0.0111814828,              /* emi[0][1] */
        0.0048590039,              /* emi[0][2] */
       -0.00000242389840,          /* emi[0][3] */
       -0.00000002710544,          /* emi[0][4] */
       -0.00000001177742 },        /* emi[0][5] */
 
     { -0.0111814828,              /* emi[1][0] */
        0.9999374849,              /* emi[1][1] */
       -0.0000271771,              /* emi[1][2] */
        0.00000002710544,          /* emi[1][3] */
       -0.00000242392702,          /* emi[1][4] */
        0.00000000006585 },        /* emi[1][5] */
 
     { -0.0048590040,              /* emi[2][0] */
       -0.0000271557,              /* emi[2][1] */
        0.9999881946,              /* emi[2][2] */
        0.00000001177742,          /* emi[2][3] */
        0.00000000006585,          /* emi[2][4] */
       -0.00000242404995 },        /* emi[2][5] */
 
     { -0.000551,                  /* emi[3][0] */
        0.238509,                  /* emi[3][1] */
       -0.435614,                  /* emi[3][2] */
        0.99990432,                /* emi[3][3] */
        0.01118145,                /* emi[3][4] */
        0.00485852 },              /* emi[3][5] */
 
     { -0.238560,                  /* emi[4][0] */
       -0.002667,                  /* emi[4][1] */
        0.012254,                  /* emi[4][2] */
       -0.01118145,                /* emi[4][3] */
        0.99991613,                /* emi[4][4] */
       -0.00002717 },              /* emi[4][5] */
 
     {  0.435730,                  /* emi[5][0] */
       -0.008541,                  /* emi[5][1] */
        0.002117,                  /* emi[5][2] */
       -0.00485852,                /* emi[5][3] */
       -0.00002716,                /* emi[5][4] */
        0.99996684 }               /* emi[5][5] */
   };


/**
 * Convert J2000(FK5) to B1950(FK4) coordinates.
 *
 * @param ra  Right ascension in degrees (J2000)
 * @param dec Declination in degrees (J2000)
 */
public static RADec
fk524(double ra, double dec)
{
   System.out.println("fk524 in.: ra="+MathUtil.doubleToString(ra)+",\tdec="+MathUtil.doubleToString(dec));

   // Convert to radians.
   double rra  = Angle.degreesToRadians(ra);
   double rdec = Angle.degreesToRadians(dec);

   double[] raDec = fk524m(rra, rdec, 0.0, 0.0);

   ra  = Angle.radiansToDegrees(raDec[0]);
   dec = Angle.radiansToDegrees(raDec[1]);

   System.out.println("fk524 out: ra="+MathUtil.doubleToString(ra)+",\tdec="+MathUtil.doubleToString(dec));

   return new RADec(RADec.FK4, ra, dec);
}

/**
 * Convert J2000(FK5) to B1950(FK4) coordinates using a known epoch.  This
 * routine is more accurate than fk524.
 *
 * @param ra  Right ascension in degrees (J2000).
 * @param dec Declination in degrees (J2000).
 * @param epoch Besselian epoch in years.
 */
public static RADec
fk524e(double ra, double dec, double epoch)
{
    // Convert to radians.
    double rra  = Angle.degreesToRadians(ra);
    double rdec = Angle.degreesToRadians(dec);

    double[] raDec = fk524m(rra, rdec, 0.0, 0.0);

    rra          = raDec[0];
    rdec         = raDec[1];
    double rapm  = raDec[2];  // Proper motion in ra (rad/trop.yr.)
    double decpm = raDec[3];  // Proper motion in dec (rad/trop.yr.)

    ra  = Angle.radiansToDegrees(rra  + (rapm * (epoch - 1950.0)));
    dec = Angle.radiansToDegrees(rdec + (decpm * (epoch - 1950.0)));

    return new RADec(RADec.FK4, ra, dec);
}



/**
 *  This routine converts stars from the new, IAU 1976, FK5, Fricke
 *  system, to the old, Bessel-Newcomb, FK4 system, using Yallop's
 *  implementation (see ref 2) of a matrix method due to Standish
 *  (see ref 3).  The numerical values of ref 2 are used canonically.
 *
 *  <p>
 *  Notes:
 *  <ol>
 *     <li>The proper motions in ra are dra / dt rather than
 *	  cos(dec) * dra / dt, and are per year rather than per century.
 *
 *     <li>Note that conversion from Julian epoch 2000.0 to Besselian
 * 	  epoch 1950.0 only is provided for.  Conversions involving
 * 	  other epochs will require use of the appropriate precession,
 * 	  proper motion, and e-terms routines before and/or after
 * 	  fk524 is called.
 * 
 *      <li>In the fk4 catalogue the proper motions of stars within
 * 	  10 degrees of the poles do not embody the differential
 * 	  e - term effect and should, strictly speaking, be handled
 * 	  in a different manner from stars outside these regions.
 * 	  however, given the general lack of homogeneity of the star
 * 	  data available for routine astrometry, the difficulties of
 * 	  handling positions that may have been determined from
 * 	  astrometric fields spanning the polar and non - polar regions,
 * 	  the likelihood that the differential e - terms effect was not
 * 	  taken into account when allowing for proper motion in past
 * 	  astrometry, and the undesirability of a discontinuity in
 * 	  the algorithm, the decision has been made in this routine to
 * 	  include the effect of differential e - terms on the proper
 * 	  motions for all stars, whether polar or not.  at epoch 2000,
 * 	  and measuring on the sky rather than in terms of dra, the
 * 	  errors resulting from this simplification are less than
 * 	  1 milliarcsecond in position and 1 milliarcsecond per
 * 	  century in proper motion.
 *
 * </ol>
 *
 * <p>
 * References:
 * <ol>
 *     <li>"Mean and apparent place computations in the new IAU System.
 *         I. The transformation of astrometric catalog systems to the
 *	  equinox J2000.0." Smith, C.A.; Kaplan, G.H.; Hughes, J.A.;
 *	  Seidelmann, P.K.; Yallop, B.D.; Hohenkerk, C.Y.
 *	  Astronomical Journal vol. 97, Jan. 1989, p. 265-273.
 *
 *      <li>"Mean and apparent place computations in the new IAU System.
 *	  II. Transformation of mean star places from FK4 B1950.0 to
 *	  FK5 J2000.0 using matrices in 6-space."  Yallop, B.D.;
 *	  Hohenkerk, C.Y.; Smith, C.A.; Kaplan, G.H.; Hughes, J.A.;
 *	  Seidelmann, P.K.; Astronomical Journal vol. 97, Jan. 1989,
 *	  p. 274-279.
 *
 *     <li>"Conversion of positions and proper motions from B1950.0 to the
 *	  IAU system at J2000.0", Standish, E.M.  Astronomy and
 *	  Astrophysics, vol. 115, no. 1, Nov. 1982, p. 20-22.
 * </ol>
 *
 * <p>
 * <b>P.T.Wallace</b>   Starlink   27 October 1987
 * <p>
 * <b>Doug Mink</b>    Smithsonian Astrophysical Observatory  7 June 1995
 *
 * @param ra	Right ascension in radians (J2000).
 * @param dec	Declination in radians (J2000).
 * @param rapm	Proper motion in right ascension (rad/jul.yr).
 * @param decpm	Proper motion in declination (rad/jul.yr).
 * 
 * @return An array of four doubles containing ra in radians (B1950), dec
 *          in radians (B1950), rapm (rad/trop.yr), and decpm (rad/trop.yr).
 */
public static double[]
fk524m(double ra, double dec, double rapm, double decpm)
{
   double r1950, d1950;		// B1950.0 ra,dec (rad)
   double dr1950,dd1950;	// B1950.0 proper motions (rad/trop.yr)

   // Miscellaneous
   double[] v1 = new double[6];
   double[] v2 = new double[6];

   // Pick up J2000 data (units radians and arcsec / jc)
   double ur = rapm  * pmf;
   double ud = decpm * pmf;

   // Spherical to Cartesian
   double sr = Math.sin(ra);
   double cr = Math.cos(ra);
   double sd = Math.sin(dec);
   double cd = Math.cos(dec);

   double x = cr * cd;
   double y = sr * cd;
   double z = sd;
   double w = zero;

   v1[0] = x;
   v1[1] = y;
   v1[2] = z;

   if (ur != zero || ud != zero) {
      v1[3] = -(ur*y) - (cr*sd*ud);
      v1[4] =  (ur*x) - (sr*sd*ud);
      v1[5] =	         (cd*ud);
   } else {
      v1[3] = zero;
      v1[4] = zero;
      v1[5] = zero;
   }

   // Convert position + velocity vector to bn system
   for (int i=0; i<6; ++i) {
      w = zero;
      for (int j=0; j<6; ++j) {
         w += emi[i][j] * v1[j];
      }
      v2[i] = w;
   }

   // Vector components
   x = v2[0];
   y = v2[1];
   z = v2[2];
   double xd = v2[3];
   double yd = v2[4];
   double zd = v2[5];

   // Magnitude of position vector
   double rxyz = Math.sqrt(x*x + y*y + z*z);

   // Include e-terms
   x  = x + a[0] * rxyz;
   y  = y + a[1] * rxyz;
   z  = z + a[2] * rxyz;
   xd = xd + ad[0] * rxyz;
   yd = yd + ad[1] * rxyz;
   zd = zd + ad[2] * rxyz;

   // Convert to spherical
   double rxysq = x*x + y*y;
   double rxy   = Math.sqrt(rxysq);

   if (x == zero && y == zero) {
      r1950 = zero;
   } else {
      r1950 = Math.atan2(y,x);
      if (r1950 < zero) r1950 = r1950 + d2pi;
   }
   d1950 = Math.atan2(z,rxy);

   if (rxy > tiny) {
      ur = (x*yd - y*xd) / rxysq;
      ud = (zd*rxysq - z * (x*xd + y*yd)) / ((rxysq + z*z) * rxy);
   }
   dr1950 = ur / pmf;
   dd1950 = ud / pmf;

   if (diag) {
      _diagB1950_J2000(r1950, d1950, ra, dec);
   }

   // Return results
   double[] raDec = { r1950, d1950, dr1950, dd1950 };
   return raDec;
}

/**
 * Diagnostics.
 */
private static void
_diagB1950_J2000(double r1950, double d1950, double ra, double dec)
{
   double dra  = (2.4e2 / rcon) * (r1950 - ra);
   double ddec = (3.6e3 / rcon) * (d1950 - dec);
   System.out.println("B1950-J2000: dra="+ dra  +" sec  ddec="+ ddec +" arcsec");
}

   /**
    * FK245  convert B1950 FK4 star data to J2000 FK5
    * based on Starlink sla_fk425 by P.T.Wallace 27 October 1987
    */
   private static double[][] em = {
        { 0.999925678186902,  0.011182059571766,  0.004857946721186,
         -0.000541652366951,  0.237917612131583, -0.436111276039270},
 
        {-0.011182059642247,  0.999937478448132, -0.000027147426498,
         -0.237968129744288, -0.002660763319071,  0.012259092261564},
 
        {-0.004857946558960, -0.000027176441185,  0.999988199738770,
          0.436227555856097, -0.008537771074048,  0.002119110818172},
 
        { 0.000002423950176,  0.000000027106627,  0.000000011776559,
          0.999947035154614,  0.011182506007242,  0.004857669948650},
 
        {-0.000000027106627,  0.000002423978783, -0.000000000065816,
         -0.011182506121805,  0.999958833818833, -0.000027137309539},
 
        {-0.000000011776558, -0.000000000065874,  0.000002424101735,
         -0.004857669684959, -0.000027184471371,  1.000009560363559},
   };

/**
 * Convert B1950(FK4) to J2000(FK5) coordinates.
 *
 * @param ra  Right ascension in degrees (B1950)
 * @param dec Declination in degrees (B1950)
 */
public static RADec
fk425(double ra, double dec)
{
   System.out.println("fk425 in.: ra="+MathUtil.doubleToString(ra)+",\tdec="+MathUtil.doubleToString(dec));

   // Convert to radians.
   double rra  = Angle.degreesToRadians(ra);
   double rdec = Angle.degreesToRadians(dec);

   double[] raDec = fk425m(rra, rdec, 0.0, 0.0);

   ra  = Angle.radiansToDegrees(raDec[0]);
   dec = Angle.radiansToDegrees(raDec[1]);

   System.out.println("fk425 out: ra="+MathUtil.doubleToString(ra)+",\tdec="+MathUtil.doubleToString(dec));

   return new RADec(RADec.FK5, ra, dec);
}

/**
 * Convert B1950(FK4) to J2000(FK5) coordinates using a known epoch.  This
 * routine is more accurate than fk425.
 *
 * @param ra  Right ascension in degrees (B1950).
 * @param dec Declination in degrees (B1950).
 * @param epoch Besselian epoch in years.
 */
public static RADec
fk425e(double ra, double dec, double epoch)
{
    // Convert to radians.
    double rra  = Angle.degreesToRadians(ra);
    double rdec = Angle.degreesToRadians(dec);

    double[] raDec = fk425m(rra, rdec, 0.0, 0.0);

    rra          = raDec[0];
    rdec         = raDec[1];
    double rapm  = raDec[2];  // Proper motion in ra (rad/trop.yr.)
    double decpm = raDec[3];  // Proper motion in dec (rad/trop.yr.)

    ra  = Angle.radiansToDegrees(rra  + (rapm * (epoch - 2000.0)));
    dec = Angle.radiansToDegrees(rdec + (decpm * (epoch - 2000.0)));

    return new RADec(RADec.FK5, ra, dec);
}


/**
 *   This routine converts stars from the old, Bessel-Newcomb, FK4
 *   system to the new, IAU 1976, FK5, Fricke system, using Yallop's
 *   implementation (see ref 2) of a matrix method due to Standish
 *   (see ref 3).  The numerical values of ref 2 are used canonically.
 * 
 *   <p>
 *   Notes:
 * 
 *   <ol>
 *      <li> The proper motions in ra are dra/dt rather than
 *           cos(dec)*dra/dt, and are per year rather than per century.
 * 
 *      <li> Conversion from besselian epoch 1950.0 to Julian epoch
 *           2000.0 only is provided for.  Conversions involving other
 *           epochs will require use of the appropriate precession,
 *           proper motion, and e-terms routines before and/or
 *           after fk425 is called.
 * 
 *      <li> In the FK4 catalogue the proper motions of stars within
 *           10 degrees of the poles do not embody the differential
 *           e-term effect and should, strictly speaking, be handled
 *           in a different manner from stars outside these regions.
 *           However, given the general lack of homogeneity of the star
 *           data available for routine astrometry, the difficulties of
 *           handling positions that may have been determined from
 *           astrometric fields spanning the polar and non-polar regions,
 *           the likelihood that the differential e-terms effect was not
 *           taken into account when allowing for proper motion in past
 *           astrometry, and the undesirability of a discontinuity in
 *           the algorithm, the decision has been made in this routine to
 *           include the effect of differential e-terms on the proper
 *           motions for all stars, whether polar or not.  At epoch 2000,
 *           and measuring on the sky rather than in terms of dra, the
 *           errors resulting from this simplification are less than
 *           1 milliarcsecond in position and 1 milliarcsecond per
 *           century in proper motion.
 *   </ol>
 * 
 *   <p>
 *   References:
 * 
 *   <ol>
 *      <li> "Mean and apparent place computations in the new IAU System.
 *          I. The transformation of astrometric catalog systems to the
 *          equinox J2000.0." Smith, C.A.; Kaplan, G.H.; Hughes, J.A.;
 *          Seidelmann, P.K.; Yallop, B.D.; Hohenkerk, C.Y.
 *          Astronomical Journal vol. 97, Jan. 1989, p. 265-273.
 * 
 *      <li> "Mean and apparent place computations in the new IAU System.
 *          II. Transformation of mean star places from FK4 B1950.0 to
 *          FK5 J2000.0 using matrices in 6-space."  Yallop, B.D.;
 *          Hohenkerk, C.Y.; Smith, C.A.; Kaplan, G.H.; Hughes, J.A.;
 *          Seidelmann, P.K.; Astronomical Journal vol. 97, Jan. 1989,
 *          p. 274-279.
 * 
 *      <li> "Conversion of positions and proper motions from B1950.0 to the
 *          IAU system at J2000.0", Standish, E.M.  Astronomy and
 *          Astrophysics, vol. 115, no. 1, Nov. 1982, p. 20-22.
 *   </ol>
 * 
 *   <p>
 *   <b>P.T.Wallace</b>   Starlink   27 October 1987
 *   <p>
 *   <b>Doug Mink</b>     Smithsonian Astrophysical Observatory  7 June 1995
 *
 */
public static double[]
fk425m(double ra, double dec, double rapm, double decpm)
{
   double  r2000, d2000;    // J2000.0 ra,dec (rad)
   double  dr2000,dd2000;   // J2000.0 proper motions (rad/jul.yr)
 
   // Pick up B1950 data (units radians and arcsec / tc)
   double ur = rapm  * pmf;
   double ud = decpm * pmf;
 
   // Spherical to cartesian
   double sr = Math.sin(ra);
   double cr = Math.cos(ra);
   double sd = Math.sin(dec);
   double cd = Math.cos(dec);
 
   // star position and velocity vectors
   double[] r0 = { cr * cd, sr * cd, sd };
   double[] r1 = { -sr*cd*ur - cr*sd*ud, cr*cd*ur - sr*sd*ud, cd*ud };
 
   // Allow for e-terms and express as position + velocity 6-vector
   double w  = r0[0] * a[0]  + r0[1] * a[1]  + r0[2] * a[2];
   double wd = r0[0] * ad[0] + r0[1] * ad[1] + r0[2] * ad[2];

   // Combined position and velocity vectors.
   double[] v1 = new double[6];
   double[] v2 = new double[6];
 
   for (int i=0; i<3; ++i) {
      v1[i]   = r0[i] - a[i]  + w*r0[i];
      v1[i+3] = r1[i] - ad[i] + wd*r0[i];
   }
 
   // Convert position + velocity vector to Fricke system.
   for (int i=0; i<6; ++i) {
      w = zero;
      for (int j=0; j<6; ++j) {
         w = w + em[j][i] * v1[j];
      }
      v2[i] = w;
   }
 
   // Revert to spherical coordinates.
   double x  = v2[0];
   double y  = v2[1];
   double z  = v2[2];
   double xd = v2[3];
   double yd = v2[4];
   double zd = v2[5];
 
   double rxysq  = x*x + y*y;
   double rxyzsq = rxysq + z*z;
   double rxy = Math.sqrt(rxysq);
   double spxy = x*xd + y*yd;

   if ((x == zero) && (y == zero)) {
      r2000 = zero;
   } else {
      r2000 = Math.atan2(y,x);
      if (r2000 < zero) r2000 = r2000 + d2pi;
   }
   d2000 = Math.atan2(z,rxy);
 
   if (rxy > tiny) {
      ur = (x * yd - y * xd) / rxysq;
      ud = (zd * rxysq - z * spxy) / (rxyzsq * rxy);
   }
   dr2000 = ur / pmf;
   dd2000 = ud / pmf;
 
   if (diag) {
      _diagJ2000_B1950(r2000, d2000, ra, dec);
   }

   // Return the results
   double[] raDec = {r2000, d2000, dr2000, dd2000};
   return raDec;
}

/**
 * Diagnostics.
 */
private static void
_diagJ2000_B1950(double r2000, double d2000, double ra, double dec)
{
   double dra  = (2.4e2 / rcon) * (r2000 - ra);
   double ddec = (3.6e3 / rcon) * (d2000 - dec);
   System.out.println("J2000-B1950: dra="+ dra  +" sec  ddec="+ ddec +" arcsec");
}


public static RADec
fk524s(String ra2000Str, String dec2000Str)
{
   System.out.println("**** FK5 -> FK4 ****");
   System.out.println("J2000: " + ra2000Str + "\t" + dec2000Str);

   double ra2000  = HHMMSS.valueOf(ra2000Str);
   double dec2000 = DDMMSS.valueOf(dec2000Str);

   RADec b1950 = fk524(ra2000, dec2000);

   String ra1950Str  = HHMMSS.valStr(b1950.ra);
   String dec1950Str = DDMMSS.valStr(b1950.dec);

   System.out.println("B1950: " + ra1950Str + "\t" + dec1950Str);
   System.out.println("-----");

   return b1950;
}


public static RADec
fk425s(String ra1950Str, String dec1950Str)
{
   System.out.println("**** FK4 -> FK5 ****");
   System.out.println("B1950: " + ra1950Str + "\t" + dec1950Str);

   double ra1950  = HHMMSS.valueOf(ra1950Str);
   double dec1950 = DDMMSS.valueOf(dec1950Str);

   RADec j2000 = fk425(ra1950, dec1950);

   String ra2000Str  = HHMMSS.valStr(j2000.ra);
   String dec2000Str = DDMMSS.valStr(j2000.dec);

   System.out.println("J2000: " + ra2000Str + "\t" + dec2000Str);
   System.out.println("-----");

   return j2000;
}


public static void
main(String[] argv)
{
   double ra, dec;
   ra = 148.96757917;
   dec= 69.67970278;
   System.out.println("ra =" + HHMMSS.valStr(ra));
   System.out.println("dec=" + DDMMSS.valStr(dec));
   RADec b1950 = fk524(ra, dec);
   //System.out.println("1950 ra="+ MathUtil.doubleToString(b1950.ra) +", dec="+ MathUtil.doubleToString(b1950.dec));
   System.out.println("ra =" + HHMMSS.valStr(b1950.ra));
   System.out.println("dec=" + DDMMSS.valStr(b1950.dec));

   System.out.println();
   ra = 147.93118333;
   dec= 69.91689167;
   System.out.println("ra =" + HHMMSS.valStr(ra));
   System.out.println("dec=" + DDMMSS.valStr(dec));
   RADec j2000 = fk425(147.93118333, 69.91689167);
   //System.out.println("2000 ra="+MathUtil.doubleToString(j2000.ra) +", dec="+MathUtil.doubleToString(j2000.dec));
   System.out.println("ra =" + HHMMSS.valStr(j2000.ra));
   System.out.println("dec=" + DDMMSS.valStr(j2000.dec));

/*
   String ra2000Str  = "9:55:52.219";
   String dec2000Str = "69:40:46.93";
   fk524s(ra2000Str, dec2000Str);

   String ra1950Str  = "9:51:43.484";
   String dec1950Str = "69:55:00.81";
   fk425s(ra1950Str, dec1950Str);
*/

/*
   double ra2000  = HHMMSS.valueOf(ra2000Str);
   double dec2000 = DDMMSS.valueOf(dec2000Str);

   RADec b1950 = fk524(ra2000, dec2000);

   String ra1950Str  = HHMMSS.valStr(b1950.ra);
   String dec1950Str = DDMMSS.valStr(b1950.dec);

   System.out.println("**** FK5(J2000) -> FK4(B1950) Test");
   System.out.println("RA(J2000)  = " + ra2000Str);
   System.out.println("Dec(J2000) = " + dec2000Str);
   System.out.println("--- converts to:");
   System.out.println("RA(B1950)  = " + ra1950Str);
   System.out.println("Dec(B1950) = " + dec1950Str);

   RADec j2000 = fk425(b1950.ra, b1950.dec);
   ra2000Str  = HHMMSS.valStr(j2000.ra);
   dec2000Str = DDMMSS.valStr(j2000.dec);

   System.out.println("");

   System.out.println("**** FK4(B1950) -> FK5(J2000) Test");
   System.out.println("RA(B1950)  = " + ra1950Str);
   System.out.println("Dec(B1950) = " + dec1950Str);
   System.out.println("--- converts to:");
   System.out.println("RA(J2000)  = " + ra2000Str);
   System.out.println("Dec(J2000) = " + dec2000Str);

   System.out.println("");

   double ra  = HHMMSS.valueOf("9:55:52.219");
   double dec = DDMMSS.valueOf("69:40:46.93");
   System.out.println(HHMMSS.valStr(ra));
   System.out.println(DDMMSS.valStr(dec));
*/
}

}
