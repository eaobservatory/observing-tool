// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.iter ;

import java.lang.reflect.Field ;
import java.lang.reflect.InvocationTargetException ;
import java.lang.reflect.Method ;
import java.util.Enumeration ;
import java.util.Vector ;

import gemini.sp.SpItem ;
import gemini.sp.SpType ;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;
import gemini.sp.SpTreeMan ;
import gemini.sp.obsComp.SpInstObsComp ;
import gemini.sp.obsComp.SpInstObsComp.IterationTracker ;

/**
 * The Iterator Folder (or "Sequence") item. The job of the folder is to hold
 * iterators for an Observation Context.
 */
public class SpIterFolder extends SpItem implements SpTranslatable
{
	/**
     * Time needed by the telescope to move to another offset position.
     * 
     * 5 seconds.
     */
	private static final double OFFSET_TIME = 5. ;

	/**
     * Default constructor.
     */
	public SpIterFolder()
	{
		super( SpType.SEQUENCE ) ;
	}

	/**
     * "Compiles" the iterators contained within the folder. This method steps
     * through all the elements produced by its contained iterators and appends
     * them into a vector.
     * 
     * @return A Vector of Vectors of SpIterStep
     * 
     * @see SpIterEnumeration
     * @see SpIterStep
     */
	public Vector compile()
	{
		Vector code = new Vector() ;

		Enumeration e = children() ;
		while( e.hasMoreElements() )
		{
			SpItem child = ( SpItem )e.nextElement() ;
			if( !( child instanceof SpIterComp ) )
				continue ;

			SpIterComp sic = ( SpIterComp )child ;

			SpIterEnumeration sie = sic.elements() ;
			while( sie.hasMoreElements() )
				code.addElement( sie.nextElement() ) ;
		}
		return code ;
	}

	/**
     * Debugging method.
     */
	public void printSummary()
	{
		Enumeration e = children() ;
		while( e.hasMoreElements() )
		{
			SpIterComp sic = ( SpIterComp )e.nextElement() ;

			System.out.println( "#########" ) ;
			SpIterEnumeration sie = sic.elements() ;
			while( sie.hasMoreElements() )
			{
				System.out.println( "----------" ) ;
				Vector v = ( Vector )sie.nextElement() ;

				for( int i = 0 ; i < v.size() ; ++i )
				{
					SpIterStep sis = ( SpIterStep )v.elementAt( i ) ;
					System.out.print( sis.title ) ;
					try
					{
						if( sis.stepCount != 1 )
							System.out.print( " (" + sis.stepCount + ")" ) ;
					}
					finally
					{
						System.out.println() ;
					}
					for( int j = 0 ; j < sis.values.length ; ++j )
					{
						SpIterValue siv = ( SpIterValue )sis.values[ j ] ;
						System.out.println( '\t' + siv.attribute + " = " + siv.values[ 0 ] ) ;
					}
				}
			}

			if( sie.hasCleanup() )
			{
				System.out.println( "----------" ) ;
				SpIterValue siv = ( SpIterValue )sie.cleanup() ;
				System.out.println( siv.attribute ) ;
				for( int j = 0 ; j < siv.values.length ; ++j )
					System.out.println( '\t' + siv.values[ j ] ) ;
			}
			System.out.println( "^^^^^^^^^" ) ;
		}
	}

	public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this ) ;

		if( instrument == null )
			return 0. ;

		Vector iterStepVector = compile() ;
		Vector iterStepSubVector = null ;
		SpIterStep spIterStep = null ;
		IterationTracker iterationTracker = instrument.createIterationTracker() ;
		double elapsedTime = 0. ;

		int nPol = 0 ;

		int iterStepVectorSize = 0 ;
		if( iterStepVector != null )
			iterStepVectorSize = iterStepVector.size() ;

		Object spIterStareObs = null ;
		int offsets = 0 ;

		for( int i = 0 ; i < iterStepVectorSize ; i++ )
		{
			iterStepSubVector = ( Vector )iterStepVector.get( i ) ;

			int iterStepSubVectorSize = 0 ;
			if( iterStepSubVector != null )
				iterStepSubVectorSize = iterStepSubVector.size() ;

			for( int j = 0 ; j < iterStepSubVectorSize ; j++ )
			{
				spIterStep = ( SpIterStep )iterStepSubVector.get( j ) ;
				if( spIterStep.item.getClass().getName().endsWith( "SpIterPOL" ) )
					nPol++ ;
				
				if( spIterStep.item.getClass().getName().endsWith( "SpIterStareObs" ) )
				{
					spIterStareObs = spIterStep.item ;
					offsets++ ;
					continue ;
				}

				iterationTracker.update( spIterStep ) ;

				if( spIterStep.item instanceof SpIterObserveBase )
					elapsedTime += iterationTracker.getObserveStepTime() ;

				if( instrument.getClass().getName().indexOf( "WFCAM" ) == -1 )
				{
					if( spIterStep.item instanceof SpIterOffset )
					{
						if( ( OFFSET_TIME - instrument.getExposureOverhead() ) > 0. )
						{
							// If for each OFFSET_TIME added an exposure overhead can be
							// subtracted since this is done while the telescope moves.
							elapsedTime += ( OFFSET_TIME - instrument.getExposureOverhead() ) ;
						}
					}
				}
			}
		}

		// http://www.jach.hawaii.edu/software/jcmtot/het_obsmodes.html 2007-07-12
		if( spIterStareObs != null && instrument.getClass().getName().indexOf( "SpInstHeterodyne" ) > -1 )
		{
			double totalIntegrationTime = 0. ;
			try
			{
				Class spIterStareObsClass = Class.forName( "orac.jcmt.iter.SpIterStareObs" ) ;
				Method getSwitchingMode = spIterStareObsClass.getMethod( "getSwitchingMode" , new Class[] {} ) ;
				Method hasSeparateOffs = spIterStareObsClass.getMethod( "hasSeparateOffs" , new Class[] {} ) ;
				Method getSecsPerCycle = spIterStareObsClass.getMethod( "getSecsPerCycle" , new Class[] {} ) ;

				Object switchingMode = getSwitchingMode.invoke( spIterStareObs , new Object[] {} ) ;

				if( switchingMode != null )
				{
					boolean isBeamSwitch = false ;
					boolean isPositionSwitch = false ;

					Field beamSwitchField = spIterStareObsClass.getField( "SWITCHING_MODE_BEAM" ) ;
					Object beamSwitch = beamSwitchField.get( spIterStareObs ) ;
					isBeamSwitch = switchingMode.equals( beamSwitch ) ;

					Field positionSwitchField = spIterStareObsClass.getField( "SWITCHING_MODE_POSITION" ) ;
					Object positionSwitch = positionSwitchField.get( spIterStareObs ) ;
					isPositionSwitch = switchingMode.equals( positionSwitch ) ;

					Object secsPerCycle = getSecsPerCycle.invoke( spIterStareObs , new Object[]{} ) ;
					int integrationTimePerPoint = 0 ;
					if( secsPerCycle != null && secsPerCycle instanceof Integer )
						integrationTimePerPoint = ( ( Integer )secsPerCycle ).intValue() ;

					Method isContinuum = spIterStareObsClass.getMethod( "isContinuum" , new Class[]{} ) ;

					if( isBeamSwitch )
					{
						totalIntegrationTime = 2.3 * offsets * integrationTimePerPoint + 100. ;
					}
					else if( isPositionSwitch )
					{
						Object separateOff = hasSeparateOffs.invoke( spIterStareObs , new Object[]{} ) ;
						if( separateOff != null && separateOff instanceof Boolean )
						{
							boolean sharedOff = !( ( Boolean )separateOff ).booleanValue() ;
							if( offsets == 1 )
								totalIntegrationTime = 2.45 * integrationTimePerPoint + 80. ;
							else if( sharedOff || integrationTimePerPoint >= 15 )
								totalIntegrationTime = 2.65 * offsets * integrationTimePerPoint + 80. ;
							else
								totalIntegrationTime = 2. * offsets * integrationTimePerPoint + 190. ;
						}
					}

					boolean addContinuum = false ;
					Object continuum = isContinuum.invoke( spIterStareObs , new Object[]{} ) ;
					if( continuum != null && continuum instanceof Boolean )
						addContinuum = ( ( Boolean )continuum ).booleanValue() ;
					if( addContinuum )
						totalIntegrationTime *= 1.2 ;
				}
				totalIntegrationTime -= offsets * OFFSET_TIME ;
			}
			catch( ClassNotFoundException cnfe )
			{
				System.out.println( "Could not find class " + cnfe ) ;
			}
			catch( IllegalAccessException iae )
			{
				System.out.println( "Could not access " + iae ) ;
			}
			catch( NoSuchMethodException nsme )
			{
				System.out.println( "Could not find method " + nsme ) ;
			}
			catch( InvocationTargetException ite )
			{
				System.out.println( "Could not invoke method " + ite ) ;
			}
			catch( NoSuchFieldException nsfe )
			{
				System.out.println( "Could not find field " + nsfe ) ;
			}
			elapsedTime += totalIntegrationTime ;
		}

		if( nPol > 1 )
		{
			if( instrument.getClass().getName().endsWith( "SCUBA" ) )
			{
				// Take off some of the extra overhead
				elapsedTime -= ( nPol - 1 ) * 40. ;
			}
		}

		return elapsedTime ;
	}

	public void translateProlog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translateEpilog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		Enumeration e = this.children() ;
		gemini.util.TranslationUtils.recurse( e , v ) ;
	}
}
