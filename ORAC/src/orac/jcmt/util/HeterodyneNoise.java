package orac.jcmt.util ;

import java.io.BufferedReader ;
import java.io.File ;
import java.io.FileReader ;
import java.util.HashMap ;
import java.util.Iterator ;
import java.util.Set ;
import java.util.StringTokenizer ;
import java.util.TreeMap ;
import java.util.Vector ;

import orac.jcmt.iter.SpIterJCMTObs ;
import orac.jcmt.iter.SpIterRasterObs ;
import orac.jcmt.inst.SpInstHeterodyne ;
import orac.jcmt.iter.SpIterJiggleObs ;
import orac.jcmt.iter.SpIterStareObs ;

import gemini.util.MathUtil ;

/**
 * Class for caalculation of the system noise of the heterodyne recievers.
 * This class is based on the perl model JCMT::HITEC which is comprised of
 * code used to create the integration time calculator 
 */
public class HeterodyneNoise
{
	static Vector feNames = new Vector() ;
	static Vector trxValues = new Vector() ;
	static Vector nu_tel = new Vector() ;
	static HashMap dasModes = new HashMap() ;
	static TreeMap _availableBands = new TreeMap() ;
	static final double kappa = 1.15 ;
	static final String cfgDir = System.getProperty( "ot.cfgdir" ) ;
	private static final String receiverFile = "receiver.info" ;
	private static boolean initialised = false ;
	private static int SUBSYSTEM = 0 ;

	private static void init()
	{
		String inputLine ;

		if( feNames.size() != 0 )
			return ;

		// Read the reciever file - first get its directory and add the file name
		String fileName = cfgDir + File.separator + receiverFile ;
		// Open the file ready for reading
		File rvrFile = new File( fileName ) ;
		try
		{
			BufferedReader in = new BufferedReader( new FileReader( rvrFile ) ) ;
			// Read the front end names
			while( ( inputLine = in.readLine() ) != null )
			{
				if( inputLine.equals( "" ) )
					break ;
				feNames.add( inputLine ) ;
			}
			// Now loop through the rest of the file and get the receiver temperature
			int index ;
			while( ( inputLine = in.readLine() ) != null )
			{
				if( ( index = feNames.indexOf( inputLine ) ) != -1 )
				{
					// Start reading the data
					TreeMap currentMap = new TreeMap() ;
					int nLines = Integer.valueOf( in.readLine() ).intValue() ;
					for( int i = 0 ; i < nLines ; i++ )
					{
						String values = in.readLine() ;
						StringTokenizer st = new StringTokenizer( values ) ;
						Double ftmp = new Double( st.nextToken() ) ;
						int ftmpi = ( int )ftmp.doubleValue() ;
						Integer frequency = new Integer( ftmpi ) ;
						Integer trx = new Integer( st.nextToken() ) ;
						currentMap.put( frequency , trx ) ;
					}
					trxValues.add( index , currentMap ) ;
					Double nu = new Double( in.readLine() ) ;
					nu_tel.add( index , nu ) ;
				}
			}
		}
		catch( Exception e )
		{
			System.out.println( "Error reading receiver info file" ) ;
			e.printStackTrace() ;
		}

		getAvailableTau() ;

		initialised = true ;
	}

	private static void getAvailableTau()
	{
		File dir = new File( cfgDir ) ;
		if( dir.isDirectory() )
		{
			String[] files = dir.list() ;
			if( files != null )
			{
				for( int i = 0 ; i < files.length ; i++ )
				{
					if( files[ i ].startsWith( "tau" ) && files[ i ].endsWith( ".dat" ) )
					{
						// Extract the tau value
						String value = "0." + files[ i ].substring( files[ i ].indexOf( "u" ) + 1 , files[ i ].lastIndexOf( "." ) ) ;
						double dtmp = Double.parseDouble( value ) ;
						_availableBands.put( new Double( dtmp ) , files[ i ] ) ;
					}
				}
			}
			else
			{
				System.out.println( "No files in " + cfgDir ) ;
			}
		}
	}

	public static double getTrx( String fe , double freq )
	{
		double trx = 0. ;
		int index ;

		/* Now see if this is in our available list of front ends */
		if( ( index = feNames.indexOf( fe ) ) >= 0 )
		{
			// There is a match, now extract the appropriate tree map
			TreeMap thisMap = ( TreeMap )trxValues.elementAt( index ) ;
			if( thisMap.size() > 1 )
			{
				// first make sure it is within range or short cicuit now
				Integer nextTrxInteger = ( Integer )thisMap.firstKey() ;
				double nextTrxFrequency = nextTrxInteger.doubleValue() ;
				if( freq < nextTrxFrequency )
					trx = ( ( Integer )thisMap.get( nextTrxInteger ) ).doubleValue() ;

				Integer lastTrxInteger = ( Integer )thisMap.lastKey() ;
				double lastTrxFrequency = lastTrxInteger.doubleValue() ;
				if( freq > lastTrxFrequency )
					trx = ( ( Integer )thisMap.get( lastTrxInteger ) ).doubleValue() ;

				if( trx == 0 )
				{
					/*
					 * We are going to do a linear interpolation between the
					 * surrounding frequency values, so we need to find the
					 * values at the appropriate keys
					 */
					Set set = thisMap.keySet() ;
					Iterator iter = set.iterator() ;
					while( iter.hasNext() )
					{

						lastTrxInteger = nextTrxInteger ;
						nextTrxInteger = ( Integer )iter.next() ;
						nextTrxFrequency = nextTrxInteger.doubleValue() ;
						lastTrxFrequency = lastTrxInteger.doubleValue() ;
						if( freq < nextTrxFrequency && freq >= lastTrxFrequency )
							break ; // We have the info we need
					}

					try
					{
						// Now get the corresponding Trx values
						lastTrxInteger = ( Integer )thisMap.get( lastTrxInteger ) ;
						double lastTrxValue = lastTrxInteger.doubleValue() ;
						nextTrxInteger = ( Integer )thisMap.get( nextTrxInteger ) ;
						double nextTrxValue = nextTrxInteger.doubleValue() ;
						// Now interpolate...
						trx = linterp( lastTrxFrequency , lastTrxValue , nextTrxFrequency , nextTrxValue , freq ) ;
					}
					catch( NullPointerException npe )
					{
						throw new RuntimeException( "Heterodyne not initialised" ) ;
					}
				}
			}
			else
			{
				trx = ( ( Integer )thisMap.get( thisMap.firstKey() ) ).intValue() ;
			}
		}
		return trx ;
	}

	public static double getTsys
	( 
			String fe , // The front end name
			double tau , // The noise calculation tau
			double airmass , // The airmass to use
			double freq , // Required frequency
			boolean ssb // Whether or not we are using SSB of DSB
	)
	{
		int index ;
		double nuTel = 0. ;
		double tSys = 0. ;

		if( !initialised )
			init() ;

		// Find which tau range contains the required tau, and the next tau range nearest
		String tauFile0 ;
		String tauFile1 ;
		Iterator iter = _availableBands.keySet().iterator() ;
		double current = 0 , next = 0 ;
		boolean firstLoop = true ;
		while( iter.hasNext() )
		{
			if( firstLoop )
			{
				current = ( ( Double )iter.next() ).doubleValue() ;
				next = ( ( Double )iter.next() ).doubleValue() ;
				if( tau <= current )
					break ;
				firstLoop = false ;
				continue ;
			}
			current = next ;
			next = ( ( Double )iter.next() ).doubleValue() ;
			if( tau >= current && tau < next )
				break ;
		}
		tauFile0 = ( String )_availableBands.get( new Double( current ) ) ;
		tauFile1 = ( String )_availableBands.get( new Double( next ) ) ;

		double tranmission0 = getTransmission( tauFile0 , freq ) ;
		double tranmission1 = getTransmission( tauFile1 , freq ) ;
		double t = linterp( current , tranmission0 , next , tranmission1 , tau ) ;

		if( ( index = feNames.indexOf( fe ) ) != -1 )
			nuTel = ( ( Double )nu_tel.elementAt( index ) ).doubleValue() ;

		// Nowe find Tsys
		double nuSky = Math.exp( -t * airmass ) ;
		double tSky = 260. - nuSky * 260. ;
		double tTel = 265. - nuTel * 265. ;

		if( ssb )
		{
			if( "HARP".equals( fe ) || "WD".equals( fe ) )
				tSys = ( getTrx( fe , freq ) + nuTel * tSky + tTel + 35 ) / ( nuSky * nuTel ) ;
			else
				tSys = ( 2. * getTrx( fe , freq ) + nuTel * tSky + tTel + 35 ) / ( nuSky * nuTel ) ;
		}
		else
		{
			tSys = 2 * ( getTrx( fe , freq ) + nuTel * tSky + tTel ) / ( nuSky * nuTel ) ;
		}

		return tSys ;
	}

	private static TreeMap getAtmosphereData( double tau )
	{
		double[] availableBands = { 0.03 , 0.05 , 0.065 , 0.1 , 0.16 , 0.2 } ;
		TreeMap tauMap = new TreeMap() ;
		int index = 0 ;

		// Find the tau band we are in
		if( tau >= availableBands[ availableBands.length - 1 ] )
		{
			index = availableBands.length - 1 ;
		}
		else if( tau < availableBands[ 0 ] )
		{
			index = 0 ;
		}
		else
		{
			for( int i = 1 ; i < availableBands.length ; i++ )
			{
				if( tau >= availableBands[ i - 1 ] && tau < availableBands[ i ] )
					break ;
				index++ ;
			}
		}

		// We now have the index so open the file
		String fileName = cfgDir + "/tau" ;
		String tmp = Double.toString( availableBands[ index ] ) ;
		StringTokenizer st = new StringTokenizer( tmp , "." ) ;
		st.nextToken() ;
		tmp = st.nextToken() + ".dat" ;
		fileName = fileName + tmp ;
		File tauFile = new File( fileName ) ;
		try
		{
			BufferedReader fileReader = new BufferedReader( new FileReader( tauFile ) ) ;
			String inputLine ;
			while( ( inputLine = fileReader.readLine() ) != null )
			{
				st = new StringTokenizer( inputLine ) ;
				Double frequency = new Double( st.nextToken() ) ;
				Double depth = new Double( st.nextToken() ) ;
				tauMap.put( frequency , depth ) ;
			}
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
		}
		return tauMap ;
	}

	private static double getTransmission( String filename , double freq )
	{
		String path = cfgDir + filename ;
		double rtn = 0. ;
		BufferedReader rdr = null ;
		try
		{
			rdr = new BufferedReader( new FileReader( path ) ) ;
			// Read the first line of the file
			String line = rdr.readLine() ;
			double freq1 = 0. , tran1 = 0. ;
			double freq2 = 0. , tran2 = 0. ;
			if( line != null )
			{
				StringTokenizer st = new StringTokenizer( line ) ;
				freq1 = Double.parseDouble( st.nextToken() ) ;
				tran1 = Double.parseDouble( st.nextToken() ) ;
			}
			// Now loop over all the values til we find the ones we want
			while( ( line = rdr.readLine() ) != null )
			{
				StringTokenizer st = new StringTokenizer( line ) ;
				freq2 = Double.parseDouble( st.nextToken() ) ;
				tran2 = Double.parseDouble( st.nextToken() ) ;
				// First deal with the case where the freq is below the first value
				if( freq < freq1 )
					break ;
				// See is the frequency we need is between the two we have
				if( freq >= freq1 && freq < freq2 )
					break ;
				// Otherwise, keep looping
				freq1 = freq2 ;
				tran1 = tran2 ;
			}

			// Once we get here, we should be able to interpolate
			rtn = linterp( freq1 , tran1 , freq2 , tran2 , freq ) ;
		}
		catch( java.io.FileNotFoundException fnf )
		{
			System.out.println( "Unable to find file " + path ) ;
		}
		catch( java.io.IOException ioe )
		{
			System.out.println( "IO Error while reading file " + path ) ;
		}
		finally
		{
			if( rdr != null )
			{
				try
				{
					rdr.close() ;
				}
				catch( Exception e ){}
			}
		}

		return rtn ;

	}

	public static double linterp( double x1 , double y1 , double x2 , double y2 , double x )
	{
		double slope = ( y2 - y1 ) / ( x2 - x1 ) ;
		double value = slope * x + y1 - slope * x1 ;
		return value ;
	}

	private static double getNoise( SpIterJCMTObs obs , SpInstHeterodyne inst , double tSys )
	{
		double time = 0. ;
		double np = 1. ;
		int shared = 0 ;
		int samplesPerRow = 1 ;
	
		double multiscan = 1. ;

		if( obs instanceof SpIterRasterObs )
		{
			// Make this the total time to do the map
			SpIterRasterObs rasterObs = ( SpIterRasterObs )obs ;
			double sampleTime = rasterObs.getSampleTime() ;
			samplesPerRow = ( int )rasterObs.numberOfSamplesPerRow() ;

			time = sampleTime ;

			np = samplesPerRow ;
			
			if( inst.getFrontEnd().equals( "HARP" ) )
			{
				double scale = rasterObs.getScanDy() / SpIterRasterObs.HARP_FULL_ARRAY ;
				multiscan = Math.sqrt( scale ) ;
			}
			
			shared = 1 ;
		}
		else if( obs instanceof SpIterJiggleObs )
		{
			SpIterJiggleObs jiggleObs = ( SpIterJiggleObs )obs ;
			time = ( double )jiggleObs.getSecsPerCycle() ;
			np = ( ( SpIterJiggleObs )obs ).getNumberOfPoints() ;
			shared = jiggleObs.hasSeparateOffs() ? 0 : 1 ;
		}
		else if( obs instanceof SpIterStareObs )
		{
			SpIterStareObs stareObs = ( SpIterStareObs )obs ;
			shared = stareObs.hasSeparateOffs() ? 0 : 1 ;
			time = ( double )stareObs.getSecsPerCycle() ;
		}
		else
		{
			time = ( double )obs.getSecsPerCycle() ;
		}

		double bandwidth = inst.getBandWidth( SUBSYSTEM ) ;
		int channels = inst.getChannels( SUBSYSTEM ) ;
		int resolution = ( int )Math.rint( ( bandwidth ) / channels ) ;

		// Multiply by the number of mixers
		if( inst.getMixer().startsWith( "Dual" ) )
		{
			time *= 2. ;
			resolution *= 2 ;
		}

		double factor = ( shared * Math.sqrt( 1 + ( 1 / Math.sqrt( np ) ) ) ) + ( ( 1 - shared ) * ( Math.sqrt( 2 ) ) ) ;
		double rmsNoise = 1.04 * factor * tSys * 1.23 / Math.sqrt( resolution * time ) ;
		rmsNoise *= multiscan ;
		return MathUtil.round( rmsNoise , 3 ) ;
	}
	
	public static double getHeterodyneNoise( SpIterJCMTObs obs , SpInstHeterodyne instrument , double tSys )
	{
		// Set up all the vectors required
		if( !initialised )
			init() ;

		//Now calulate the noise based on this
		double noise = getNoise( obs , instrument , tSys ) ;
		return noise ;
	}

	public static double getHeterodyneNoise( SpIterJCMTObs obs , SpInstHeterodyne instrument , double noiseCalculationTau , double airmass )
	{
		// Set up all the vectors required
		if( !initialised )
			init() ;

		// Get the system temperaure
		double tSys = getTsys( instrument.getFrontEnd() , noiseCalculationTau , airmass , ( instrument.getRestFrequency( 0 ) ) / 1.0e9 , instrument.getMode().equalsIgnoreCase( "ssb" ) ) ;

		//Now calulate the noise based on this
		return getHeterodyneNoise( obs , instrument , tSys ) ;
	}

	public static void main( String[] args ){}
}
