/*
 * ( C )2009 STFC
 */

package orac.jcmt.util ;

import orac.jcmt.iter.SpIterJCMTObs ;
import orac.jcmt.iter.SpIterRasterObs ;
import orac.jcmt.iter.SpIterPointingObs ;
import orac.jcmt.iter.SpIterFocusObs ;
import orac.jcmt.iter.SpIterFlatObs ;
import orac.jcmt.iter.SpIterNoiseObs ;
import orac.jcmt.iter.SpIterSkydipObs  ;
import orac.jcmt.iter.SpIterStareObs ;
import orac.jcmt.iter.SpIterDREAMObs ;
import orac.jcmt.iter.SpIterFTS2Obs ;
import orac.jcmt.SpJCMTConstants ;

import orac.jcmt.obsComp.SpSiteQualityObsComp ;
import orac.util.DrUtil ;
import gemini.sp.obsComp.SpTelescopeObsComp ;
import gemini.sp.SpItem ;
import gemini.sp.SpTelescopePos ;
import gemini.sp.SpTreeMan ;
import gemini.util.CoordSys ;
import gemini.util.DDMMSS ;

public class Scuba2Time implements SpJCMTConstants
{
	double josStepTime = 0.005 ;
	double stepsBetweenDarks = 1440000. ;

	public Scuba2Time(){}

	public double totalIntegrationTime( SpIterJCMTObs obs )
	{
		double integrationTime = 0 ;

		if( obs instanceof SpIterRasterObs )
			integrationTime = scan( ( SpIterRasterObs )obs ) ;
		else if( obs instanceof SpIterPointingObs )
			integrationTime = pointing( ( SpIterPointingObs )obs ) ;
		else if( obs instanceof SpIterFocusObs )
			integrationTime = focus( ( SpIterFocusObs )obs ) ;
		else if( obs instanceof SpIterFlatObs )
			integrationTime = flat( ( SpIterFlatObs )obs ) ;
		else if( obs instanceof SpIterNoiseObs )
			integrationTime = noise( ( SpIterNoiseObs )obs ) ;
		else if( obs instanceof SpIterSkydipObs )
			integrationTime = skydip( ( SpIterSkydipObs )obs ) ;
		else if( obs instanceof SpIterStareObs )
			integrationTime = stare( ( SpIterStareObs )obs ) ;
		else if( obs instanceof SpIterDREAMObs )
			integrationTime = dream( ( SpIterDREAMObs )obs ) ;
		else if( obs instanceof SpIterFTS2Obs )
			integrationTime = fts2( ( SpIterFTS2Obs )obs ) ;

		return integrationTime ;
	}

	public double scan( SpIterRasterObs raster )
	{
		double integrationTime = 0. ;
		double durationPerArea = 0. ;
		
		double height = raster.getHeight() ;
		double width = raster.getWidth() ;
		double dy = raster.getScanDy() ;
		double velocity = raster.getScanVelocity() ;
		
		String pattern = raster.getScanStrategy() ;
		if( SCAN_PATTERN_LISSAJOUS.equals( pattern ) || SCAN_PATTERN_PONG.equals( pattern ) )
		{
			ComputePong computePong = new ComputePong() ;
			String type = ComputePong.PongScan.CURVY ;
			durationPerArea = computePong.getPeriodForPong( height , width , dy , velocity , type ) ;
		}
		else if( SCAN_PATTERN_BOUS.equals( pattern ) )
		{
			double pixArea = dy * velocity ;
			double mapArea = width * height ;
			durationPerArea = ( mapArea / pixArea ) * josStepTime ;
		}
		else if( SCAN_PATTERN_POINT.equals( pattern ) )
		{
			durationPerArea = raster.getSampleTime() ;
		}
		else
		{
			throw new RuntimeException( "Unrecognised scan pattern : " + pattern ) ;
		}

//		System.out.println( "Estimated time to cover the map area once: " + durationPerArea + " sec" ) ;

		double nSteps ;
		double sampleTime = raster.getSampleTime() ;
		if( sampleTime == 0 )
		{
			nSteps = sampleTime / josStepTime ;
/*
			System.out.println( "Scan map executing for a specific time. Not map coverage" ) ;
			System.out.println( "Total duration requested for scan map: " + sampleTime + " secs." ) ;
*/
		}
		else
		{
			int nRepeats = 1 ;
			String integrations = raster.getIntegrations() ;
			if( integrations != null )
				nRepeats = new Integer( integrations ) ;
//			System.out.println( "Number of repeats of map area requested: " + nRepeats ) ;
			nSteps = ( nRepeats * durationPerArea ) / josStepTime ;
		}

		double stepsPerPass = durationPerArea / josStepTime ;
		stepsBetweenDarks = Math.max( stepsBetweenDarks , stepsPerPass ) ;

		double nCycles = Math.ceil( nSteps / stepsBetweenDarks ) ;
		double josMin = Math.rint( nSteps / nCycles ) ;

		double totalTime = nCycles * josMin * josStepTime ;
/*
		System.out.println( "Number of steps in scan map sequence: " + josMin ) ;
		System.out.println( "Number of repeats: " + nCycles ) ;
		System.out.println( "Time spent mapping: " + totalTime + " sec" ) ;
*/
		integrationTime = totalTime ;

		return integrationTime ;
	}

	private double getAirmass( SpItem root )
	{
		double airmass = -1. ;

		SpTelescopeObsComp telescopeObsComp = SpTreeMan.findTargetList( root ) ;
		if( telescopeObsComp != null )
		{
			SpTelescopePos base = telescopeObsComp.getPosList().getBasePosition() ;
			if( base.getCoordSys() == CoordSys.FK5 )
				airmass = DrUtil.airmass( base.getYaxis() , DDMMSS.valueOf( "19:49:20.75" ) ) ;
			else if( base.getCoordSys() == CoordSys.AZ_EL )
				airmass = DrUtil.airmass( base.getYaxis() ) ;
			else
				airmass = 0. ;
		}

		return airmass ;
	}

	private SpSiteQualityObsComp findSiteQuality( SpItem root )
	{
		SpSiteQualityObsComp siteQuality = null ;
		Object tmp = findObjectInContext( root , SpSiteQualityObsComp.class ) ;
		if( tmp != null )
			siteQuality = ( SpSiteQualityObsComp )tmp ;
		return siteQuality ;
	}

	private Object findObjectInContext( SpItem parent , Class<?> klass )
	{
		Object found = null ;
		while( parent != null )
		{
			parent = parent.parent() ;
			if( parent != null )
			{
				if( klass.isInstance( parent ) )
				{
					found = parent ;
					break ;
				}
				else
				{
					java.util.Enumeration<SpItem> children = parent.children() ;
					while( children != null && children.hasMoreElements() )
					{
						gemini.sp.SpItem child = children.nextElement() ;
						if( klass.isInstance( child ) )
						{
							found = child ;
							parent = null ;
							break ;
						}
					}
				}
			}
		}
		return found ;
	}

	private static final double multiplicand450 = 1.7 * ( int )Math.pow( 10 , 6 ) ;
	private static final double multiplicand850 = 4.4 * ( int )Math.pow( 10 , 4 ) ;
	private boolean fourFifty = false ;
	public double fts2( SpIterFTS2Obs fts2 )
	{
		double integrationTime = 0. ;
		double multiplicand = 0. ;
		double FOV = fts2.getFOV() ;
		FOV *= FOV ; // in square arcminutes
		double mapArea = FOV ; // in square degrees, for now assume FOV
		double mappingEfficiancy = 0.6 ; // supplied

		double airmass = getAirmass( fts2 ) ;
		double csoTau = 0. ;
		double NEFD = 0. ;
		if( airmass > -1. )
		{
			SpSiteQualityObsComp siteQuality = findSiteQuality( fts2 ) ;

			if( siteQuality != null )
			{
				csoTau = siteQuality.getNoiseCalculationTau() ;

				Scuba2Noise s2n = Scuba2Noise.getInstance() ;
				if( fourFifty )
					NEFD = s2n.calculateNEFD( Scuba2Noise.four50 , csoTau , airmass ) ;
				else
					NEFD = s2n.calculateNEFD( Scuba2Noise.eight50 , csoTau , airmass ) ;
			}
		}

		double spectralResolution = fts2.getResolutionInHz() ;
		double sensitivity = 0. ; // in mJy Hz ^ -0.5
		if( fourFifty )
			sensitivity = fts2.getSensitivity450() ;
		else
			sensitivity = fts2.getSensitivity850() ;

		if( fourFifty )
			multiplicand = multiplicand450 ;
		else
			multiplicand = multiplicand850 ;

		double multiplyMapArea = multiplicand * mapArea ;
		double FOVEfficiancy = FOV * mappingEfficiancy ;
		double resolutionSensitivity = spectralResolution * sensitivity ;

		integrationTime = ( multiplyMapArea / FOVEfficiancy ) * ( NEFD / resolutionSensitivity ) ; 

		return integrationTime ;
	}

	public double ftsSensitivity( SpIterFTS2Obs fts2 , double integrationTime , String fourFiftyOr850 )
	{
		double multiplicand = 0. ;
		double FOV = fts2.getFOV() ;
		FOV *= FOV ; // in square arcminutes
		double mapArea = FOV ; // in square degrees, for now assume FOV
		double mappingEfficiancy = 0.6 ; // supplied

		double airmass = getAirmass( fts2 ) ;
		double csoTau = 0. ;
		double NEFD = 0. ;
		if( airmass > -1. )
		{
			SpSiteQualityObsComp siteQuality = findSiteQuality( fts2 ) ;

			if( siteQuality != null )
			{
				csoTau = siteQuality.getNoiseCalculationTau() ;

				Scuba2Noise s2n = Scuba2Noise.getInstance() ;
				NEFD = s2n.calculateNEFD( fourFiftyOr850 , csoTau , airmass ) ;
			}
		}

		double spectralResolution = fts2.getResolutionInHz() ;

		if( fourFiftyOr850.equals( Scuba2Noise.four50 ) )
			multiplicand = multiplicand450 ;
		else if( fourFiftyOr850.equals( Scuba2Noise.eight50 ) )
			multiplicand = multiplicand850 ;
		else
			throw new RuntimeException( fourFiftyOr850 + " not supported." ) ;

		double multiplyMapArea = multiplicand * mapArea ;
		double FOVEfficiancy = FOV * mappingEfficiancy ;
		double resolutionSensitivity = spectralResolution * integrationTime ;

		double sensitivity = ( multiplyMapArea / FOVEfficiancy ) * ( NEFD / resolutionSensitivity ) ; 

		return sensitivity ;
	}

	private double pointing( SpIterPointingObs pointing )
	{
		return 60. ;
	}

	private double focus( SpIterFocusObs focus )
	{
		return focus.getFocusPoints() * 60. ;
	}

	private double flat( SpIterFlatObs flat )
	{
		return 5. * 60. ;
	}

	private double noise( SpIterNoiseObs noise )
	{
		/*
		 *  complete guess as scuba2_index only says
		 *  when it was performed not how long it took.
		 */
		return 3. * 60. ;
	}

	private double skydip( SpIterSkydipObs skydip )
	{
		return -1. ;
	}

	private double stare( SpIterStareObs stare )
	{
		return -1. ;
	}

	private double dream( SpIterDREAMObs dream )
	{
		return -1. ;
	}
}
