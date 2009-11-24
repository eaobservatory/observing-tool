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
	private double calculateNEFDForTime( String wavelength , Double timeSeconds , Double tau , double airmass )
	{
		double mJy = -1. ;

		mJy = calculateNEFD( wavelength , tau , airmass ) ;

		if( mJy != -1. )
			mJy /= Math.sqrt( timeSeconds ) ;

		return mJy ;
	}

	/**
	 * Generic method to calculate the noise equivalent flux density in milijanskys for 450 and 850 microns
	 * @param tau
	 * @param airmass
	 * @param wavelength
	 * @return noise equivalent flux density in milijanskys
	 */
	private double calculateNEFD( String waveLength , Double csoTau , double airmass )
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
		else if( csoTau > csoTaus.firstElement() && csoTau < csoTaus.lastElement() )
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

			double beforeNoise = wavelengthLookup.find( before ) ;
			double afterNoise = wavelengthLookup.find( after ) ;
			mJy = MathUtil.linterp( before , beforeNoise , after , afterNoise , csoTau ) ;
		}
		System.out.println( "Noise ( mJy ) from NEFD lookup at " + waveLength + " = " + mJy ) ;
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

		if( four50.equals( waveLength ) )
			tauMultiplicand = fourFiftyTauMultiplicand ;
		else if( eight50.equals( waveLength ) )
			tauMultiplicand = eightFiftyTauMultiplicand ;
		else
			throw new RuntimeException( "Wave length " + waveLength + " unknown at this time." ) ;

		double tau = tauMultiplicand * csoTau ;
		double transmission = Math.exp( tau * airmass ) ;
		mJy *= transmission ;

		System.out.println( "Noise ( mJy ) from NEFD lookup corrected for airmass = " + mJy ) ;

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
	 * Calculate the total integration time for the entire map in seconds.
	 * @param waveLength
	 * @param csoTau
	 * @param airmass
	 * @param desiredNoiseMJanskys
	 * @param widthArcSeconds
	 * @param heightArcSeconds
	 * @return total integration time for the entire map in seconds.
	 */
	public double totalIntegrationTimeForMap( String waveLength , double csoTau , double airmass , double desiredNoiseMJanskys , double widthArcSeconds , double heightArcSeconds )
	{
		double time = 0. ;
		double omegaPerBolometerSquare = 0. ;
		double numberOfBolometers = 0. ;

		if( four50.equals( waveLength ) )
		{
			omegaPerBolometerSquare = 32. ;
			numberOfBolometers = 700. ;
		}
		else if( eight50.equals( waveLength ) )
		{
			omegaPerBolometerSquare = 120 ;
			numberOfBolometers = 400 ;
		}
		else
		{
			throw new RuntimeException( "Wave length " + waveLength + " unknown at this time." ) ;
		}

		double timePerBolometer = timePerBolometer( waveLength , csoTau , airmass , desiredNoiseMJanskys ) ;

		double area = widthArcSeconds * heightArcSeconds ;

		time = 2 * area / numberOfBolometers * omegaPerBolometerSquare * timePerBolometer ;

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
		double tau = .040 ;
		double airmass = 0. ;
		System.out.println( "Time in seconds = " + timeSeconds ) ;
		System.out.println( "CSO Tau = " + tau ) ;
		System.out.println( "Airmass = " + airmass ) ;
		System.out.println( "Noise ( mJy ) at 450 = " + s2n.calculateNEFD450ForTime( timeSeconds , tau , airmass ) ) ;
		System.out.println( "Noise ( mJy ) at 850 = "+ s2n.calculateNEFD850ForTime( timeSeconds , tau , airmass ) ) ;
	}
}
