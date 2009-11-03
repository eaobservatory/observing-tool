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

	public double calculateNoise450( int timeSeconds , double tau )
	{
		double mJy = -1. ;
		mJy = calculateNoise( timeSeconds , tau , fourFifty ) ;
		return mJy ;
	}
	
	public double calculateNoise850( int timeSeconds , double tau )
	{
		double mJy = -1. ;
		mJy = calculateNoise( timeSeconds , tau , eightFifty ) ;
		return mJy ;
	}

	public double calculateNoise( Integer timeSeconds , Double tau , OrderedMap<Double,Double> wavelength )
	{
		double mJy = -1. ;

		if( wavelength.containsKey( tau ) )
		{
			mJy = wavelength.find( tau ) ;
		}
		else
		{
			Vector<Double> taus = wavelength.keys() ;
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
		
		mJy /= Math.sqrt( timeSeconds.doubleValue() ) ;

		return mJy ;
	}

	public static void main( String[] args )
	{
		Scuba2Noise s2n = getInstance() ;
		int timeSeconds = 1 ;
		double tau = .040 ;
		System.out.println( s2n.calculateNoise450( timeSeconds , tau ) ) ;
	}
}
