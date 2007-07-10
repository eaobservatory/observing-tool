package orac.util ;

import uk.ac.starlink.pal.Pal ;
import uk.ac.starlink.pal.AngleDR ;
import uk.ac.starlink.pal.Stardata ;

import gemini.util.RADec ;

public class CoordConvert
{
	private static Pal pal = new Pal() ;
	private static double piDividedByOneEighty = Math.PI / 180. ;
	private static double oneEightyDividedByPi = 180. / Math.PI ;

	public static RADec FkXXz( double ra , double dec , int targetSystem )
	{
		RADec raDec = null ; 
		double raInRadians  = degreesToRadians( ra ) ;
		double decInRadians = degreesToRadians( dec ) ;
		
		AngleDR angleDR = new AngleDR( raInRadians , decInRadians ) ;
		
		if( targetSystem == RADec.FK4 )
		{
			Stardata stardata = pal.Fk54z( angleDR , 1950. ) ;
			angleDR = stardata.getAngle() ;
		}
		else if( targetSystem == RADec.FK5 )
		{
			angleDR = pal.Fk45z( angleDR , 1950. ) ; // 1950 matches coco's output
		}
		else
		{
			throw new RuntimeException( "Unrecognised target system" ) ;
		}
		
		raInRadians = angleDR.getAlpha() ;
		decInRadians = angleDR.getDelta() ;
		
		ra = radiansToDegrees( raInRadians ) ;
		dec = radiansToDegrees( decInRadians ) ;
		
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
		return FkXXz( ra , dec , RADec.FK4 ) ; 
	}

	/**
	 * Convert from FK4 to FK5
	 * @param ra
	 * @param dec
	 * @return gemini.util.RADec 
	 */
	public static RADec Fk45z( double ra , double dec )
	{
		return FkXXz( ra , dec , RADec.FK5 ) ; 
	}

	public static double degreesToRadians( double degrees )
	{
		return degrees * piDividedByOneEighty ;
	}

	public static double radiansToDegrees( double radians )
	{
		return radians * oneEightyDividedByPi ;
	}
	
}
