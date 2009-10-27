package orac.jcmt.util ;

import orac.jcmt.iter.* ;
import orac.jcmt.SpJCMTConstants ;

public class Scuba2Time implements SpJCMTConstants
{
	double josStepTime = 0.005 ; // 200Hz // 0.01
	double stepsBetweenDarks = 18000 ;

	public Scuba2Time( SpIterJCMTObs obs ){}

	public int totalIntegrationTime( SpIterJCMTObs obs )
	{
		int integrationTime = 0 ;

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

		return integrationTime ;
	}

	private int scan( SpIterRasterObs raster )
	{
		int integrationTime = 0 ;
		double durationPerArea ;
		
		double height = raster.getHeight() ;
		double width = raster.getWidth() ;
		double dx = raster.getScanDx() ;
		double dy = raster.getScanDy() ;
		double velocity = raster.getScanVelocity() ;
		
		String pattern = raster.getScanStrategy() ;
		if( SCAN_PATTERN_LISSAJOUS.equals( pattern ) || SCAN_PATTERN_PONG.equals( pattern ) )
		{
			ComputePong computePong = new ComputePong() ;
			String type = ComputePong.PongScan.CURVY ;
			durationPerArea = computePong.getPeriodForPong( height , width , dx , velocity , type ) ;
		}
		else if( SCAN_PATTERN_BOUS.equals( pattern ) )
		{
			double pixArea = dy * velocity ;
			double mapArea = width * height ;
			durationPerArea = ( mapArea / pixArea ) * josStepTime ;
		}
		else
		{
			throw new RuntimeException( "Unrecognised scan pattern : " + pattern ) ;
		}

		System.out.println( "Estimated time to cover the map area once: " + durationPerArea + " sec" ) ;

		double nSteps ;
		double sampleTime = raster.getSampleTime() ;
		if( sampleTime != 0 )
		{
			nSteps = sampleTime / josStepTime ;
			System.out.println( "Scan map executing for a specific time. Not map coverage" ) ;
			System.out.println( "Total duration requested for scan map: " + sampleTime + " secs." ) ;
		}
		else
		{
			int nRepeats = new Integer( raster.getIntegrations() ) ;
			System.out.println( "Number of repeats of map area requested: " + nRepeats ) ;
			nSteps = ( nRepeats * durationPerArea ) / josStepTime ;
		}

		double stepsPerPass = durationPerArea / josStepTime ;
		stepsBetweenDarks = Math.max( stepsBetweenDarks , stepsPerPass ) ;

		double nCycles = Math.ceil( nSteps / stepsBetweenDarks ) ;
		double josMin = Math.rint( nSteps / nCycles ) ;

		double totalTime = nCycles * josMin * josStepTime ;

		System.out.println( "Number of steps in scan map sequence: " + josMin ) ;
		System.out.println( "Number of repeats: " + nCycles ) ;
		System.out.println( "Time spent mapping: " + totalTime + "sec" ) ;		

		return integrationTime ;
	}

	private int pointing( SpIterPointingObs pointing )
	{
		return 60 ;
	}

	private int focus( SpIterFocusObs focus )
	{
		return focus.getFocusPoints() * 60 ;
	}

	private int flat( SpIterFlatObs flat )
	{
		return 5 * 60 ;
	}

	private int noise( SpIterNoiseObs noise )
	{
		/*
		 *  complete guess as scuba2_index only says
		 *  when it was performed not how long it took.
		 */
		return 3 * 60 ;
	}

	private int skydip( SpIterSkydipObs skydip )
	{
		return -1 ;
	}

	private int stare( SpIterStareObs stare )
	{
		return -1 ;
	}

	private int dream( SpIterDREAMObs dream )
	{
		return -1 ;
	}
}