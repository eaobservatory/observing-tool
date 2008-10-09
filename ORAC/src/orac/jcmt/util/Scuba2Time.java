package orac.jcmt.util ;

import orac.jcmt.iter.* ;

public class Scuba2Time
{
/*
	public Scuba2Time( SpIterJCMTObs obs )
	{
		double josStepTime = 0.005 ; // 200Hz
		int numberOfDarks = 0 ;
		int numberOfSequences = 0 ;
		double timePerSequence = 0 ;
		
		int stepsBetweenDarks = 0 ;
		
		if( obs instanceof SpIterRasterObs )
			stepsBetweenDarks = 5 * 60 ;
		else if( obs instanceof SpIterStareObs || obs instanceof SpIterDREAMObs )
			stepsBetweenDarks = 10 ;

		if( obs instanceof SpIterSkydipObs )
		{		
			if( obs instanceof SpIterRasterObs )
			{
				SpIterRasterObs raster = ( SpIterRasterObs )obs ;
				numberOfSequences = 1 ;
				numberOfDarks = 1 ;
				double El = getElevation() ; // El - telecsope El
				double Vel = raster.getScanVelocity() / 3600 ; // skydip velocity in Tim's code
				timePerSequence = El / Vel ;
				timePerSequence /= josStepTime ;
			}
			else if( obs instanceof SpIterStareObs )
			{
				timePerSequence = JOS.min() ;
				numberOfSequences = ? ; // number of elements in skydip array
				numberOfDarks = numberOfSequences ;
			}
		}
		else if( obs instanceof SpIterNoiseObs )
		{
		}
		else if( obs instanceof SpIterStareObs || obs instanceof SpIterDREAMObs )
		{
			int numberOfOffsets = numberOfOffsets == 0 ? numberOfOffsets : 1 ;
			int numberOfMUSteps = numberOfMUSteps == 0 ? numberOfMUSteps : 1 ;
			numberOfSequences = numberOfOffsets * numberOfOffsets * JOS.numberOfCycles() ;
			int numberOfSequencesPerDark = Math.max( 1 , ( int )Math.floor( stepsBetweenDarks / timePerSequence ) ) ;
			numberOfDarks = ( int )Math.ceil( numberOfSequences / numberOfSequencesPerDark ) ;
		}
		else if( obs instanceof SpIterRasterObs )
		{
			SpIterRasterObs raster = ( SpIterRasterObs )obs ;
			String pattern = raster.getScanStrategy() ;
			double stepsPerMap = 0. ;
			if( JOS.min() > 1 )
			{
				stepsPerMap = JOS.min() ;
			}
			else if( SpIterJCMTObs.SCAN_PATTERN_BOUS.equals( pattern ) )
			{
				double radius = 240. ; /// radius in arcseconds // instrument.radius() ;
				double minWidth = raster.numberOfSamplesPerColumn() ;
				double maxWidth = raster.numberOfSamplesPerRow() ;
				maxWidth += ( 2. * radius + 60. ) ;
				double mapArea = minWidth * maxWidth ;
				double sampleArea = raster.getScanDy() * ( raster.getScanVelocity() * josStepTime ) ;
				stepsPerMap = mapArea / sampleArea ;
			}
			else if( SpIterJCMTObs.SCAN_PATTERN_LISSAJOUS.equals( pattern ) || SpIterJCMTObs.SCAN_PATTERN_PONG.equals( pattern ) )
			{
				ComputePong computePong = new ComputePong() ;
				double height = raster.getHeight() ;
				double width = raster.getWidth() ;
				double dx = raster.getScanDx() ;
				double velocity = raster.getScanVelocity() ;
				String type = ComputePong.PongScan.CURVY ;
				double timePerMap = computePong.getPeriodForPong( height , width , dx , velocity , type ) ;
				stepsPerMap = timePerMap / josStepTime ;
			}

			String tmp = raster.getIntegrations() ;
			int mapCycles = new Integer( tmp ) ;
			int numberOfMaps = mapCycles ; // .numberOfCycles() ;
			int numberOfMapsPerDark = Math.max( 1 , ( int )Math.floor( stepsBetweenDarks / stepsPerMap ) ) ;
			numberOfDarks = ( int )Math.ceil( numberOfMaps / numberOfMapsPerDark ) ;
			numberOfSequences = numberOfDarks ;
			timePerSequence = numberOfMaps * stepsPerMap / numberOfSequences ;
		}
		else if( obs instanceof SpIterFocusObs )
		{
			numberOfSequences = ( int )(( SpIterFocusObs )obs).getSteps() ;
		}

		System.out.println( "NDARKS = " + numberOfDarks ) ;
		System.out.println( "NSEQ = " + numberOfSequences ) ;
		System.out.println( "TIME/SEQ = " + ( josStepTime * timePerSequence ) ) ;

		double startupOverhead = 20. ;
		double startupOverheadPerSequence = 2. ;
		double darkLength = JOS.numberOfCalSamples() * josStepTime ;
		timePerSequence *= josStepTime ;

		double duration = startupOverhead + ( ( numberOfDarks + numberOfSequences ) * startupOverheadPerSequence ) + ( timePerSequence * numberOfSequences ) + ( numberOfDarks * darkLength ) ;
	}
*/
}