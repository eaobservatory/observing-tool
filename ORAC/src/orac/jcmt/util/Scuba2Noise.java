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

	private static final double fourFiftyTauMultiplicand = 20. ;
	private static final double eightFiftyTauMultiplicand = 4.02 ;
	private static final double fourFiftyTauCorrection = .01 ;
	private static final double eightFiftyTauCorrection = .001 ;
	private static final double fourFiftyOmegaPerBolometerSquare = 32. ;
	private static final double eightFiftyOmegaPerBolometerSquare = 120. ;
	private static final double fourFiftyNumberOfBolometers = 700. ;
	private static final double eightFiftyNumberOfBolometers = 400. ;

	/**
	 * Singleton constructor, private for obvious reasons.
	 */
	private Scuba2Noise()
	{
		fourFifty = new OrderedMap<Double,Double>() ;
		fourFifty.add( .040 , 100. ) ;
		fourFifty.add( .065 , 220. ) ;
		fourFifty.add( .100 , 550. ) ;
		fourFifty.add( .150 , 5500. ) ;
		
		eightFifty = new OrderedMap<Double,Double>() ;
		eightFifty.add( .040 , 50. ) ;
		eightFifty.add( .065 , 55. ) ;
		eightFifty.add( .100 , 70. ) ;
		eightFifty.add( .150 , 90. ) ;
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

		double ts2 = tauMultiplicand * ( csoTau - tauCorrection ) ;
		double transmission = StrictMath.exp( ( airmass - 1 ) * ts2 ) ;
		mJy *= transmission ;

		return mJy ;
	}

	/**
	 * Get integration time per bolometer at a certain wave length for a desired noise level in milijanskys.
	 * @param waveLength
	 * @param csoTau
	 * @param airmass
	 * @param desiredNoiseMJanskys
	 * @return integration time per bolometer in seconds.
	 */
	private double timePerBolometer( String waveLength , double csoTau , double airmass , double desiredNoiseMJanskys )
	{
		double time = 0. ;
		double mJy = -1 ;

		mJy = calculateNEFD( waveLength , csoTau , airmass ) ;

		time = mJy / desiredNoiseMJanskys ;
		time *= time ;

		return time ;
	}

	/**
	 * Inverse of timePerBolometer.
	 * @param waveLength
	 * @param time
	 * @param csoTau
	 * @param airmass
	 * @return desired noise in milijanskys.
	 */
	private double noisePerBolometerForTime( String waveLength , double time , double csoTau , double airmass )
	{
		double desiredNoise = -1 ;
		time = StrictMath.sqrt( time ) ;
		double mJy = calculateNEFD( waveLength , csoTau , airmass ) ;
		if( mJy != -1 )
			desiredNoise = mJy / time ;
		return desiredNoise ;
	}

	/**
	 * Inverse of totalIntegrationTimeForMap
	 * @param waveLength
	 * @param time
	 * @param csoTau
	 * @param airmass
	 * @param widthArcSeconds
	 * @param heightArcSeconds
	 * @return desired noise in milijanskys
	 */
	public double noiseForMapTotalIntegrationTime( String waveLength , double time , double csoTau , double airmass , double widthArcSeconds , double heightArcSeconds , boolean overhead )
	{
		double mJy = -1 ;

		double omegaPerBolometerSquare = 0. ;
		double numberOfBolometers = 0. ;

		if( four50.equals( waveLength ) )
		{
			omegaPerBolometerSquare = fourFiftyOmegaPerBolometerSquare ;
			numberOfBolometers = fourFiftyNumberOfBolometers ;
		}
		else if( eight50.equals( waveLength ) )
		{
			omegaPerBolometerSquare = eightFiftyOmegaPerBolometerSquare ;
			numberOfBolometers = eightFiftyNumberOfBolometers ;
		}
		else
		{
			throw new RuntimeException( "Wave length " + waveLength + " unknown at this time." ) ;
		}

		double area = widthArcSeconds * heightArcSeconds ;

		double dividend = area ;
		if( overhead )
			dividend *= 2. ;
		double divisor = numberOfBolometers * omegaPerBolometerSquare  ;
		double scale = dividend / divisor  ;
		time /= scale ;
		mJy = noisePerBolometerForTime( waveLength , time , csoTau , airmass ) ;

		return mJy ;
	}

	/**
	 * Calculate the total integration time for the entire map in seconds.
	 * @param waveLength
	 * @param csoTau
	 * @param airmass
	 * @param desiredNoiseMJanskys
	 * @param widthArcMinutes
	 * @param heightArcMinutes
	 * @return total integration time for the entire map in seconds.
	 */
	public double totalIntegrationTimeForMap( String waveLength , double csoTau , double airmass , double desiredNoiseMJanskys , double widthArcSeconds , double heightArcSeconds )
	{
		double time = 0. ;
		double omegaPerBolometerSquare = 0. ;
		double numberOfBolometers = 0. ;

		if( four50.equals( waveLength ) )
		{
			omegaPerBolometerSquare = fourFiftyOmegaPerBolometerSquare ;
			numberOfBolometers = fourFiftyNumberOfBolometers ;
		}
		else if( eight50.equals( waveLength ) )
		{
			omegaPerBolometerSquare = eightFiftyOmegaPerBolometerSquare ;
			numberOfBolometers = eightFiftyNumberOfBolometers ;
		}
		else
		{
			throw new RuntimeException( "Wave length " + waveLength + " unknown at this time." ) ;
		}

		double area = widthArcSeconds * heightArcSeconds ;

		double dividend = 2. * area ;
		double divisor = numberOfBolometers * omegaPerBolometerSquare  ;
		time = dividend / divisor  ;
		time *= timePerBolometer( waveLength , csoTau , airmass , desiredNoiseMJanskys ) ;

		return time ;
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
		double desiredNoiseMJanskys = -1. ;

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
					System.out.println( "Calculating time per bolometer. Requires arguments for CSO Tau, airmass and desired noise level." ) ;
					csoTau = new Double( args[ 2 ] ) ;
					airmass = new Double( args[ 3 ] ) ;
					desiredNoiseMJanskys = new Double( args[ 4 ] ) ;
					result = s2n.timePerBolometer( waveLength , csoTau , airmass , desiredNoiseMJanskys ) ;
					System.out.println( "Time per bolometer using CSO Tau " + csoTau + ", airmass " + airmass + ", desired noise " + desiredNoiseMJanskys + " = " + result + " seconds." ) ;
					break ;
				case 3 :
					System.out.println( "Calculating integration time for map. Requires arguments for CSO Tau, airmass, desired noise level, width and height ( arcminutes. )" ) ;
					csoTau = new Double( args[ 2 ] ) ;
					airmass = new Double( args[ 3 ] ) ;
					desiredNoiseMJanskys = new Double( args[ 4 ] ) ;
					double widthArcMinutes = new Double( args[ 5 ] ) ;
					double heightArcMinutes = new Double( args[ 6 ] ) ;
					result = s2n.totalIntegrationTimeForMap( waveLength , csoTau , airmass , desiredNoiseMJanskys , ( widthArcMinutes * 60 ) , ( heightArcMinutes * 60 ) ) ;
					System.out.println( "Time for map using CSO Tau " + csoTau + ", airmass " + airmass + ", desired noise " + desiredNoiseMJanskys + ", width " + widthArcMinutes + ", height " + heightArcMinutes + " = " + result + " seconds." ) ;
					System.out.println( "Desired noise was " + s2n.noiseForMapTotalIntegrationTime( waveLength , result , csoTau , airmass , ( widthArcMinutes * 60 ) , ( heightArcMinutes * 60 ) , true ) + " ?" ) ;
					break ;
				default :
					System.out.println( "Unknown program type." ) ;
					System.out.println( "\t0 : Calculate NEFD." ) ;
					System.out.println( "\t1 : Calculate NEFD for time." ) ;
					System.out.println( "\t2 : Calculate Time per bolometer." ) ;
					System.out.println( "\t3 : Calculate Total integration time for map." ) ;
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