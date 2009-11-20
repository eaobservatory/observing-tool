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
		mJy = calculateNEFDForTime( timeSeconds , tau , airmass , fourFifty ) ;
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
		mJy = calculateNEFDForTime( timeSeconds , tau , airmass , eightFifty ) ;
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
	private double calculateNEFDForTime( Double timeSeconds , Double tau , double airmass , OrderedMap<Double,Double> wavelength )
	{
		double mJy = -1. ;

		calculateNEFD( tau , airmass , wavelength ) ;

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
	private double calculateNEFD( Double tau , double airmass , OrderedMap<Double,Double> wavelength )
	{
		double mJy = -1. ;

		Vector<Double> taus = wavelength.keys() ;
		if( taus.contains( tau ) )
		{
			mJy = wavelength.find( tau ) ;
		}
		else if( tau > taus.firstElement() && tau < taus.lastElement() )
		{
			double before = 0. ;
			double after = 0. ;
			for( int index = 0 ; index < taus.size() ; index++ )
			{
				double current = taus.elementAt( index ) ;
				if( current > tau )
				{
					after = current ;
					if( index > 0 )
						before = taus.elementAt( index - 1 ) ;
					break ;
				}
			}

			double beforeNoise = wavelength.find( before ) ;
			double afterNoise = wavelength.find( after ) ;
			mJy = MathUtil.linterp( before , beforeNoise , after , afterNoise , tau ) ;
		}
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

		if( four50.equals( waveLength ) )
			mJy = calculateNEFD( csoTau , airmass , fourFifty ) ;
		else if( eight50.equals( waveLength ) )
			mJy = calculateNEFD( csoTau , airmass , eightFifty ) ;
		else
			throw new RuntimeException( "Wave length " + waveLength + " unknown at this time." ) ;

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
		System.out.println( s2n.calculateNEFD450ForTime( timeSeconds , tau , airmass ) ) ;
	}
}
