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
	 * Calculate the noise equivalent flux density in milijanskys at 450 microns.
	 * @param timeSeconds
	 * @param tau
	 * @param airmass
	 * @return noise equivalent flux density in milijanskys
	 */
	public double calculateNEFD450( double timeSeconds , double tau , double airmass )
	{
		double mJy = -1. ;
		mJy = calculateNEFD( timeSeconds , tau , airmass , fourFifty ) ;
		return mJy ;
	}
	
	/**
	 * Calculate the noise equivalent flux density in milijanskys at 850 microns.
	 * @param timeSeconds
	 * @param tau
	 * @param airmass
	 * @return noise equivalent flux density in milijanskys
	 */
	public double calculateNEFD850( double timeSeconds , double tau , double airmass )
	{
		double mJy = -1. ;
		mJy = calculateNEFD( timeSeconds , tau , airmass , eightFifty ) ;
		return mJy ;
	}

	/**
	 * Generic method to calculate the noise equivalent flux density in milijanskys for 450 and 850 microns.
	 * @param timeSeconds
	 * @param tau
	 * @param airmass
	 * @param wavelength
	 * @return noise equivalent flux density in milijanskys
	 */
	private double calculateNEFD( Double timeSeconds , Double tau , double airmass , OrderedMap<Double,Double> wavelength )
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

		if( mJy != -1. )
			mJy /= Math.sqrt( timeSeconds ) ;

		return mJy ;
	}

	public static void main( String[] args )
	{
		Scuba2Noise s2n = getInstance() ;
		double timeSeconds = 1. ;
		double tau = .040 ;
		double airmass = 0. ;
		System.out.println( s2n.calculateNEFD450( timeSeconds , tau , airmass ) ) ;
	}
}
