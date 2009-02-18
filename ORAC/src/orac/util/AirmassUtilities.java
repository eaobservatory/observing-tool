package orac.util ;

public class AirmassUtilities
{
	private static final double PiDividedBy180 = Math.PI / 180. ;
	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		for( double elevation = 5. ; elevation < 90. ; elevation += .25 )
		{
			double airmass = elevationToAirmass( elevation ) ;
			double newElevation = airmassToElevation( airmass ) ;
			newElevation = gemini.util.MathUtil.round( newElevation , 3 ) ;
			if( elevation != newElevation )
				System.out.println( "Couldn't do roundtrip for " + elevation + " != " + newElevation ) ;
		}
	}

	public static double elevationToAirmass( double elevation )
	{
		// If the elevation is < 5 degrees set the airmass to 12
		double airmass ;
		if( elevation < 5. )
		{
			airmass = 12. ;
		}
		else
		{
			// Convert elevation to radians
			elevation *= PiDividedBy180 ;
			airmass = 1. / Math.sin( elevation ) ;
		}
		return airmass ;
	}

	public static double airmassToElevation( double airmass )
	{
		if( Math.abs( airmass ) < 1. )
			airmass = 1. ;
		double elevation = Math.asin( 1. / airmass ) ;
		// Convert to degrees
		elevation = 180. * elevation / Math.PI ;
		return elevation ;
	}
	
}
