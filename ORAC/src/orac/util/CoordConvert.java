package orac.util ;

import uk.ac.starlink.pal.Pal ;
import uk.ac.starlink.pal.AngleDR ;
import uk.ac.starlink.pal.Stardata ;
import uk.ac.starlink.pal.Galactic ;

import gemini.util.RADec ;
import gemini.util.CoordSys ;

public class CoordConvert
{
	private static Pal pal = new Pal() ;

	private static int rounding = 5 ;

	private static RADec FkXXz( double ra , double dec , int targetSystem )
	{
		RADec raDec = null ;
		double raInRadians = Math.toRadians( ra ) ;
		double decInRadians = Math.toRadians( dec ) ;

		AngleDR angleDR = new AngleDR( raInRadians , decInRadians ) ;

		if( targetSystem == CoordSys.FK4 )
		{
			Stardata stardata = pal.Fk54z( angleDR , 1950. ) ;
			angleDR = stardata.getAngle() ;
		}
		else if( targetSystem == CoordSys.FK5 )
		{
			angleDR = pal.Fk45z( angleDR , 1950. ) ; // 1950 matches coco's output
		}
		else
		{
			throw new RuntimeException( "Unrecognised target system" ) ;
		}

		raInRadians = angleDR.getAlpha() ;
		decInRadians = angleDR.getDelta() ;

		ra = Math.toDegrees( raInRadians ) ;
		dec = Math.toDegrees( decInRadians ) ;

		raDec = new RADec( targetSystem , ra , dec ) ;

		return raDec ;
	}

	/**
	 * Convert from FK5 to FK4
	 * @param ra
	 * @param dec
	 * @return gemini.util.RADec 
	 */
	public static RADec Fk54z( double ra , double dec )
	{
		return FkXXz( ra , dec , CoordSys.FK4 ) ;
	}

	/**
	 * Convert from FK4 to FK5
	 * @param ra
	 * @param dec
	 * @return gemini.util.RADec 
	 */
	public static RADec Fk45z( double ra , double dec )
	{
		return FkXXz( ra , dec , CoordSys.FK5 ) ;
	}

	/**
	 * Convert from FK5 to Galactic
	 * @param ra
	 * @param dec
	 * @return gemini.util.RADec representing theta and phi
	 */
	public static RADec fk52gal( double ra , double dec )
	{
		RADec raDec = null ;

		double raInRadians = Math.toRadians( ra ) ;
		double decInRadians = Math.toRadians( dec ) ;

		AngleDR angleDR = new AngleDR( raInRadians , decInRadians ) ;

		Galactic galactic = pal.Eqgal( angleDR ) ;

		raInRadians = galactic.getLongitude() ;
		decInRadians = galactic.getLatitude() ;

		ra = Math.toDegrees( raInRadians ) ;
		dec = Math.toDegrees( decInRadians ) ;

		ra = round( ra , rounding ) ;
		dec = round( dec , rounding ) ;

		raDec = new RADec( CoordSys.GAL , ra , dec ) ;

		return raDec ;
	}

	/**
	 * Convert from Galactic to FK5
	 * @param theta
	 * @param phi
	 * @return gemini.util.RADec
	 */
	public static RADec gal2fk5( double theta , double phi )
	{
		RADec raDec = null ;

		double raInRadians = Math.toRadians( theta ) ;
		double decInRadians = Math.toRadians( phi ) ;

		Galactic galactic = new Galactic( raInRadians , decInRadians ) ;

		AngleDR angleDR = pal.Galeq( galactic ) ;

		raInRadians = angleDR.getAlpha() ;
		decInRadians = angleDR.getDelta() ;

		double ra = Math.toDegrees( raInRadians ) ;
		double dec = Math.toDegrees( decInRadians ) ;

		ra = round( ra , rounding ) ;
		dec = round( dec , rounding ) ;

		raDec = new RADec( CoordSys.FK5 , ra , dec ) ;

		return raDec ;
	}

	/**
	 * Convert from FK4 to Galactic via FK5
	 * @param ra
	 * @param dec
	 * @return gemini.util.RADec representing theta and phi
	 */
	public static RADec fk42gal( double ra , double dec )
	{
		RADec raDec = null ;

		raDec = Fk45z( ra , dec ) ;
		raDec = fk52gal( raDec.ra , raDec.dec ) ;

		raDec = new RADec( CoordSys.GAL , raDec.ra , raDec.dec ) ;

		return raDec ;
	}

	/**
	 * Convert from Galactic to FK4 via FK5
	 * @param ra
	 * @param dec
	 * @return gemini.util.RADec
	 */
	public static RADec gal2fk4( double theta , double phi )
	{
		RADec raDec = null ;

		raDec = gal2fk5( theta , phi ) ;
		raDec = Fk54z( raDec.ra , raDec.dec ) ;

		raDec = new RADec( CoordSys.FK4 , raDec.ra , raDec.dec ) ;

		return raDec ;
	}

	/**
	 * Convert from FK5 to Galactic by hand
	 * @param ra
	 * @param dec
	 * @return gemini.util.RADec representing theta and phi
	 */
	public static RADec fk52galII( double ra , double dec )
	{
		RADec raDec = null ;

		/* Degrees to Radians */

		double raInRadians = Math.toRadians( ra ) ;
		double decInRadians = Math.toRadians( dec ) ;

		/*  Spherical to cartesian */

		double[] returned = spherical2Cartesian( raInRadians , decInRadians ) ;
		double x = returned[ 0 ] ;
		double y = returned[ 1 ] ;
		double z = returned[ 2 ] ;

		/*  Rotate to galactic */

		double tmpX = x * -0.054875539726 + y * -0.873437108010 + z * -0.483834985808 ;
		double tmpY = x * 0.494109453312 + y * -0.444829589425 + z * 0.746982251810 ;
		double tmpZ = x * -0.867666135858 + y * -0.198076386122 + z * 0.455983795705 ;

		x = tmpX ;
		y = tmpY ;
		z = tmpZ ;

		/*  Cartesian to spherical */

		returned = cartesian2Spherical( x , y , z ) ;
		raInRadians = returned[ 0 ] ;
		decInRadians = returned[ 1 ] ;

		/* Radians to Degrees */

		ra = Math.toDegrees( raInRadians ) ;
		dec = Math.toDegrees( decInRadians ) ;

		ra = round( ra , rounding ) ;
		dec = round( dec , rounding ) ;

		raDec = new RADec( CoordSys.GAL , ra , dec ) ;

		return raDec ;
	}

	/**
	 * Convert from Galactic to FK5 by hand
	 * @param ra
	 * @param dec
	 * @return gemini.util.RADec
	 */
	public static RADec gal2fk5II( double theta , double phi )
	{
		RADec raDec = null ;

		double raInRadians = Math.toRadians( theta ) ;
		double decInRadians = Math.toRadians( phi ) ;

		/*  Spherical to cartesian */

		double[] returned = spherical2Cartesian( raInRadians , decInRadians ) ;
		double x = returned[ 0 ] ;
		double y = returned[ 1 ] ;
		double z = returned[ 2 ] ;

		/*  Rotate to equatorial coordinates */

		double tmpX = x * -0.054875539726 + y * 0.494109453312 + z * -0.867666135858 ;
		double tmpY = x * -0.873437108010 + y * -0.444829589425 + z * -0.198076386122 ;
		double tmpZ = x * -0.483834985808 + y * 0.746982251810 + z * 0.455983795705 ;

		x = tmpX ;
		y = tmpY ;
		z = tmpZ ;

		/*  Cartesian to spherical */

		returned = cartesian2Spherical( x , y , z ) ;
		raInRadians = returned[ 0 ] ;
		decInRadians = returned[ 1 ] ;

		double ra = Math.toDegrees( raInRadians ) ;
		double dec = Math.toDegrees( decInRadians ) ;

		ra = round( ra , rounding ) ;
		dec = round( dec , rounding ) ;

		raDec = new RADec( CoordSys.FK5 , ra , dec ) ;

		return raDec ;
	}

	private static double[] spherical2Cartesian( double ra , double dec )
	{
		double x = 1. * Math.cos( ra ) * Math.cos( dec ) ;
		double y = 1. * Math.sin( ra ) * Math.cos( dec ) ;
		double z = 1. * Math.sin( dec ) ;
		return new double[] { x , y , z } ;
	}

	private static double[] cartesian2Spherical( double x , double y , double z )
	{
		double rxy , rxy2 ;
		double ra , dec ;

		ra = Math.atan2( y , x ) ;
		if( ra < 0. )
			ra += 6.283185307179586 ;

		rxy2 = x * x + y * y ;
		rxy = Math.sqrt( rxy2 ) ;
		dec = Math.atan2( z , rxy ) ;

		return new double[] { ra , dec } ;
	}

	/**
	 * Round double-precision "input" to integer "precision" decimal places
	 * @param input
	 * @param precision
	 * @return rounded double
	 */
	public static double round( double input , int precision )
	{
		double multiplicand = Math.pow( 10 , precision ) ;
		double intermediate = Math.rint( input * multiplicand ) ;
		double output = intermediate / multiplicand ;
		return output ;
	}
}