/*
 * ( C )2009 STFC
 */

package orac.jcmt.util ;

import gemini.util.MathUtil ;
import orac.util.OrderedMap ;
import java.util.Vector ;

public class Scuba2Noise
{
	private static Scuba2Noise self ;
	private OrderedMap<Double,Double> fourFifty ;
	private OrderedMap<Double,Double> eightFifty ;
	public static final String four50 = "450" ;
	public static final String eight50 = "850" ;

	private static final double fourFiftyTauMultiplicand = 26.0 ;
	private static final double eightFiftyTauMultiplicand = 4.6 ;
	private static final double fourFiftyTauCorrection = .01923 ;
	private static final double eightFiftyTauCorrection = .00435 ;

	private OrderedMap<Double,Double> tA850;
	private OrderedMap<Double,Double> tB850;
	private OrderedMap<Double,Double> tA450;
	private OrderedMap<Double,Double> tB450;

	/**
	 * Singleton constructor, private for obvious reasons.
	 */
	private Scuba2Noise()
	{
		// Parameters for NEFD estimation
		fourFifty = new OrderedMap<Double,Double>() ;
		fourFifty.add( .040 , 100. ) ;
		fourFifty.add( .065 , 220. ) ;
		fourFifty.add( .100 , 550. ) ;
		fourFifty.add( .150 , 5500. ) ;
		
		// Parameters for NEFD estimation
		eightFifty = new OrderedMap<Double,Double>() ;
		eightFifty.add( .040 , 50. ) ;
		eightFifty.add( .065 , 55. ) ;
		eightFifty.add( .100 , 70. ) ;
		eightFifty.add( .150 , 90. ) ;

		// Time parameters for standard map sizes
		// taken from propscuba2itc.pl
		tA450 = new OrderedMap<Double, Double>();
		tB450 = new OrderedMap<Double, Double>();
		tA850 = new OrderedMap<Double, Double>();
		tB850 = new OrderedMap<Double, Double>();
		tA850.add(100.0, 196.0);	tB850.add(100.0, -44.0);
		tA450.add(100.0, 911.0);	tB450.add(100.0, -121.0);

		tA850.add(900.0, 421.0);	tB850.add(900.0, -95.0);
		tA450.add(900.0, 1961.0);	tB450.add(900.0, -261.0);

		tA850.add(1800.0, 823.0);	tB850.add(1800.0, -187.0);
		tA450.add(1800.0, 3841.0);	tB450.add(1800.0, -512.0);

		tA850.add(3600.0, 1735.0);	tB850.add(3600.0, -394.0);
		tA450.add(3600.0, 8353.0);	tB450.add(3600.0, -1113.0);

		tA850.add(7200.0, 3472.0);	tB850.add(7200.0, -789.0);
		tA450.add(7200.0, 16976.0); 	tB450.add(7200.0, -2263.0);
	}

	/**
	 * Get singleton instance
	 * @return the singleton instance of Scuba2Noise
	 */
	public static synchronized Scuba2Noise getInstance()
	{
		if( self == null )
			self = new Scuba2Noise() ;
		return self ;
	}

	/**
	 * Calculate the noise equivalent flux density in milijanskys at 450 microns with relation to time.
	 * @param timeSeconds
	 * @param tau
	 * @param airmass
	 * @return noise equivalent flux density in milijanskys
	 */
	public double calculateNEFD450ForTime( double timeSeconds , double tau , double airmass )
	{
		double mJy = -1. ;
		mJy = calculateNEFDForTime( four50 , timeSeconds , tau , airmass ) ;
		return mJy ;
	}

	/**
	 * Calculate the noise equivalent flux density in milijanskys at 850 microns with relation to time.
	 * @param timeSeconds
	 * @param tau
	 * @param airmass
	 * @return noise equivalent flux density in milijanskys
	 */
	public double calculateNEFD850ForTime( double timeSeconds , double tau , double airmass )
	{
		double mJy = -1. ;
		mJy = calculateNEFDForTime( eight50 , timeSeconds , tau , airmass ) ;
		return mJy ;
	}

	/**
	 * Generic method to calculate the noise equivalent flux density in milijanskys for 450 and 850 microns with relation to time.
	 * @param timeSeconds
	 * @param tau
	 * @param airmass
	 * @param wavelength
	 * @return noise equivalent flux density in milijanskys
	 */
	public double calculateNEFDForTime( String wavelength , Double timeSeconds , Double tau , double airmass )
	{
		double mJy = -1. ;

		mJy = calculateNEFD( wavelength , tau , airmass ) ;

		if( mJy != -1. )
			mJy /= StrictMath.sqrt( timeSeconds ) ;

		return mJy ;
	}

	/**
	 * Generic method to calculate the noise equivalent flux density in milijanskys for 450 and 850 microns
	 * @param tau
	 * @param airmass
	 * @param wavelength
	 * @return noise equivalent flux density in milijanskys
	 */
	public double calculateNEFD( String waveLength , Double csoTau , double airmass )
	{
		double mJy = -1. ;

		OrderedMap<Double,Double> wavelengthLookup = null ;

		if( four50.equals( waveLength ) )
			wavelengthLookup = fourFifty ;
		else if( eight50.equals( waveLength ) )
			wavelengthLookup = eightFifty ;
		else
			throw new RuntimeException( "Wave length " + waveLength + " unknown at this time." ) ;

		Vector<Double> csoTaus = wavelengthLookup.keys() ;
		if( csoTaus.contains( csoTau ) )
		{
			mJy = wavelengthLookup.find( csoTau ) ;
		}
		else if( csoTau > 0 && csoTau < csoTaus.lastElement() )
		{
			double before = 0. ;
			double after = 0. ;
			for( int index = 0 ; index < csoTaus.size() ; index++ )
			{
				double current = csoTaus.elementAt( index ) ;
				if( current > csoTau )
				{
					after = current ;
					if( index > 0 )
						before = csoTaus.elementAt( index - 1 ) ;
					break ;
				}
			}

			double beforeNoise = 0. ;
			if( before > 0. ){ beforeNoise = wavelengthLookup.find( before ) ; }
			double afterNoise = wavelengthLookup.find( after ) ;
			mJy = MathUtil.linterp( before , beforeNoise , after , afterNoise , csoTau ) ;
		}
		if( mJy != -1 )
			mJy = correctForAirmass( waveLength , mJy , csoTau , airmass ) ;
		return mJy ;
	}

	/**
	 * Correct input value in milijanskys based on given CSO Tau and airmass for a given wavelength.
	 * @param waveLength
	 * @param mJy
	 * @param csoTau
	 * @param airmass
	 * @return corrected input value in milijanskys.
	 */
	private double correctForAirmass( String waveLength , double mJy , double csoTau , double airmass )
	{
		double ts2 = calculateTau(waveLength, csoTau);

		double transmission = StrictMath.exp( ( airmass - 1 ) * ts2 ) ;
		mJy *= transmission ;

		return mJy ;
	}

	/**
	 * Calculate tau value for a particular wavelength.
	 * @param waveLength
	 * @param csoTau
	 * @return sky opacity
	 */
	private double calculateTau(String waveLength, double csoTau) {
		double tauMultiplicand = 0. ;
		double tauCorrection = 0. ;

		if( four50.equals( waveLength ) )
		{
			tauMultiplicand = fourFiftyTauMultiplicand ;
			tauCorrection = fourFiftyTauCorrection ;
		}
		else if( eight50.equals( waveLength ) )
		{
			tauMultiplicand = eightFiftyTauMultiplicand ;
			tauCorrection = eightFiftyTauCorrection ;
		}
		else
		{
			throw new RuntimeException( "Wave length " + waveLength + " unknown at this time." ) ;
		}

		return tauMultiplicand * ( csoTau - tauCorrection ) ;
	}


	/**
	 * Calculate the noise achieved for a given map
	 * @param waveLength
	 * @param time
	 * @param csoTau
	 * @param airmass
	 * @param widthArcSeconds
	 * @param heightArcSeconds
	 * @return desired noise in milijanskys
	 */
	public double noiseForMapTotalIntegrationTime( String waveLength , double time , double csoTau , double airmass , double widthArcSeconds , double heightArcSeconds )
	{
		double mJy = -1 ;

		double mapSize = (widthArcSeconds > heightArcSeconds)
				? widthArcSeconds
				: heightArcSeconds;

		double tA;
		double tB;

		if( four50.equals( waveLength ) )
		{
			tA = lookupParameter(tA450, mapSize);
			tB = lookupParameter(tB450, mapSize);
		}
		else if( eight50.equals( waveLength ) )
		{
			tA = lookupParameter(tA850, mapSize);
			tB = lookupParameter(tB850, mapSize);
		}
		else
		{
			throw new RuntimeException( "Wave length " + waveLength + " unknown at this time." ) ;
		}

		
		// binning factor		
		final double factor = 4;

		final double trans = StrictMath.exp(- airmass * calculateTau(waveLength, csoTau));

		mJy = (tA / trans + tB) / StrictMath.sqrt(factor * time);

		return mJy ;
	}

	private Double lookupParameter(OrderedMap<Double,Double> map, Double target)
	{
		Double value = map.find(target);
		if (value != null) return value;

		Vector<Double> keys = map.keys();
		for (int index = 0; index < keys.size(); index ++) {
			double current = keys.elementAt(index);
			if (target < current) return map.find(index);
		}

		return map.find(map.size() - 1);
	}

	/**
	 * Method for testing.
	 * @param args
	 */
	public static void main( String[] args )
	{
		Scuba2Noise s2n = getInstance() ;

		double timeSeconds = 1. ;
		double csoTau = .040 ;
		double airmass = 0. ;
		String waveLength = null ;
		Integer programType = null ;
		double result = -1. ;

		if( args.length == 0 )
		{
			System.out.println( "I have no argument with that." ) ;
			System.exit( -1 ) ;
		}

		if( args.length > 0 )
			programType = new Integer( args[ 0 ] ) ;
		if( args.length > 1 )
			waveLength = args[ 1 ] ;

		if( !four50.equals( waveLength ) && !eight50.equals( waveLength ) )
			throw new RuntimeException( "Wave length " + waveLength + " unknown at this time." ) ;

		System.out.println( "Desired wave length = " + waveLength ) ;

		try
		{
			switch( programType )
			{
				case 0 :
					System.out.println( "Calculating NEFD. Requires arguments for CSO Tau and airmass." ) ;
					csoTau = new Double( args[ 2 ] ) ;
					airmass = new Double( args[ 3 ] ) ;
					result = s2n.calculateNEFD( waveLength , csoTau , airmass ) ;
					System.out.println( "NEFD using CSO Tau " + csoTau + ", airmass " + airmass + " = " + result + " mJy." ) ;
					break ;
				case 1 :
					System.out.println( "Calculating NEFD for time. Requires arguments for time, CSO Tau and airmass." ) ;
					timeSeconds = new Double( args[ 2 ] ) ;
					csoTau = new Double( args[ 3 ] ) ;
					airmass = new Double( args[ 4 ] ) ;
					result = s2n.calculateNEFDForTime( waveLength , timeSeconds , csoTau , airmass ) ;
					System.out.println( "NEFD using time " + timeSeconds + " seconds, CSO Tau " + csoTau + ", airmass " + airmass + " = " + result + " mJy." ) ;
					break ;
				case 2 :
					System.out.println( "Calculating noise for total integration time for map. Requires arguments for CSO Tau, airmass, time, width and height ( arcminutes. )" ) ;
					csoTau = new Double( args[ 2 ] ) ;
					airmass = new Double( args[ 3 ] ) ;
					timeSeconds = new Double( args[ 4 ] ) ;
					double widthArcMinutes = new Double( args[ 5 ] ) ;
					double heightArcMinutes = new Double( args[ 6 ] ) ;
					result = s2n.noiseForMapTotalIntegrationTime( waveLength , timeSeconds , csoTau , airmass , ( widthArcMinutes * 60 ) , ( heightArcMinutes * 60 ) ) ;
					System.out.println( "Time for map using CSO Tau " + csoTau + ", airmass " + airmass + ", time " + timeSeconds + ", width " + widthArcMinutes + ", height " + heightArcMinutes + " = " + result + " mJy." ) ;
					break ;
				default :
					System.out.println( "Unknown program type." ) ;
					System.out.println( "\t0 : Calculate NEFD." ) ;
					System.out.println( "\t1 : Calculate NEFD for time." ) ;
					System.out.println( "\t2 : Calculate noise for total integration time of map." ) ;
					break ;
			}
		}
		catch( IndexOutOfBoundsException ioobe )
		{
			System.out.println( "Not enough arguments for program type." ) ;
		}
		catch( NumberFormatException nfe )
		{
			System.out.println( "Argument should be a double." ) ;
		}
		catch( Exception e )
		{
			System.out.println( "Unhandled exception " + e ) ;
		}
	}
}
