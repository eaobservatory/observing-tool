package orac.jcmt.validation;

import java.util.Vector;

/*
import edfreq.FrequencyEditorCfg;
import edfreq.Receiver ;
*/
import gemini.sp.SpTreeMan ;
import gemini.sp.SpObs ;
import gemini.sp.SpMSB ;
import orac.jcmt.inst.SpInstHeterodyne ;
import orac.jcmt.inst.SpInstSCUBA ;
import orac.jcmt.iter.SpIterJCMTObs ;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.iter.SpIterChop;
import orac.jcmt.SpJCMTConstants;
import orac.validation.SpValidation;
import orac.validation.ErrorMessage;

import orac.jcmt.iter.SpIterJiggleObs;
import orac.jcmt.iter.SpIterNoiseObs ;

import java.lang.reflect.Method ;
import java.lang.reflect.InvocationTargetException ;
import java.lang.reflect.Field ;

/**
 * Validation Tool for JCMT.
 * 
 * This class is used for checking whether the values and settings in a Science Program or
 * Observation are sensible.
 *
 * Errors and warnings are issued otherwise.
 *
 * @author M.Folger@roe.ac.uk (M.Folger@roe.ac.uk)
 */
public class JcmtSpValidation extends SpValidation
{

	public void checkObservation( SpObs spObs , Vector report )
	{
		if( report == null )
			report = new Vector();

		SpInstObsComp obsComp = SpTreeMan.findInstrument( spObs );
		SpTelescopeObsComp target = SpTreeMan.findTargetList( spObs );
		Vector observes = SpTreeMan.findAllInstances( spObs , "orac.jcmt.iter.SpIterJCMTObs" );
		for( int count = 0 ; count < observes.size() ; count++ )
		{
			SpIterJCMTObs thisObs = ( SpIterJCMTObs ) observes.elementAt( count );
			if( obsComp != null && obsComp instanceof SpInstHeterodyne )
			{
				SpInstHeterodyne spInstHeterodyne = ( SpInstHeterodyne )obsComp ;
				double loMin = 0. ; 
				double loMax = 0. ;

				/*
				 * We cannot use anything in edfreq here as it causes a build problem
				 * So we have to load dynamically
				 *  
				 * Abandon hope all ye who enter here !
				 * 
				 */
				try
				{
					Class frequencyEditorClass = Class.forName( "edfreq.FrequencyEditorCfg" ) ;
					Method getConfiguration = frequencyEditorClass.getDeclaredMethod( "getConfiguration" , null ) ;
					Object frequencyEditor = frequencyEditorClass.newInstance() ;
					Object requencyEditorCfg = getConfiguration.invoke( frequencyEditor , null ) ;
					Field receiverField = requencyEditorCfg.getClass().getDeclaredField( "receivers" ) ;
					Object receivers = receiverField.get( requencyEditorCfg ) ;
					Method get = receivers.getClass().getDeclaredMethod( "get" , new Class[]{ Object.class } ) ;
					Object receiver = get.invoke( receivers , new Object[]{ spInstHeterodyne.getFrontEnd() } ) ;
					Field loMinField = receiver.getClass().getDeclaredField( "loMin" ) ;
					Field loMaxField = receiver.getClass().getDeclaredField( "loMax" ) ;
					Object loMinObject = loMinField.get( receiver ) ;
					Object loMaxObject = loMaxField.get( receiver ) ;
					if( loMinObject instanceof Double )
						loMin = ( ( Double )loMinObject).doubleValue() ;
					if( loMaxObject instanceof Double )
						loMax = ( ( Double )loMaxObject).doubleValue() ;					
				}
				catch( ClassNotFoundException cnfe )
				{
					System.out.println( "Could not find class " + cnfe ) ;
				}
				catch( InstantiationException ie )
				{
					System.out.println( "Could not instantiate " + ie ) ;
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

				double skyFrequency = spInstHeterodyne.getSkyFrequency() ;
				if( loMin != 0. && ( loMin - spInstHeterodyne.getFeIF() ) > skyFrequency )
					report.add( new ErrorMessage( ErrorMessage.WARNING , spObs.getTitle() , "Rest frequency of " + skyFrequency + " is lower than receiver minimum " + loMin ) );
				if( loMax != 0. && ( loMax + spInstHeterodyne.getFeIF() ) < skyFrequency )
					report.add( new ErrorMessage( ErrorMessage.WARNING , spObs.getTitle() , "Rest frequency of " + skyFrequency + " is greater than receiver maximum " + loMax ) );
					
				// Give a warning for heterodyne if integration times > 40 seconds and frequency > 400 GHz
				if( spInstHeterodyne.getSkyFrequency() > 4.0E11 && thisObs.getSecsPerCycle() > 40.0 )
					report.add( new ErrorMessage( ErrorMessage.WARNING , spObs.getTitle() , "Observations > 4GHz should be done with < 40 secs/cycle" ) );
				
				if( thisObs instanceof SpIterJiggleObs )
				{
					SpIterJiggleObs spIterJiggleObs = ( SpIterJiggleObs )thisObs ;
					String frontEnd = spInstHeterodyne.getFrontEnd() ;
					String jigglePattern = spIterJiggleObs.getJigglePattern() ;
					if( jigglePattern.startsWith( "HARP" ) && !frontEnd.startsWith( "HARP" ) )
						report.add( new ErrorMessage( ErrorMessage.ERROR , spObs.getTitle() , "Cannot use " + jigglePattern + " jiggle pattern without HARP-B frontend" ) );
				}
				if( thisObs instanceof SpIterNoiseObs )
					report.add( new ErrorMessage( ErrorMessage.ERROR , spObs.getTitle() , "Cannot use Noise observations with Hetrodyne" ) );
			}
			// Also check the switching mode.  If we are in beam switch, we need a chop iterator,
			// in position we need a reference in the target
			String switchingMode = thisObs.getSwitchingMode() ;
			if( switchingMode != null )
			{	
				if( switchingMode.equals( SpJCMTConstants.SWITCHING_MODE_BEAM ) )
				{
					Vector chops = SpTreeMan.findAllInstances( spObs , "gemini.sp.iter.SpIterChop" );
					if( chops == null || chops.size() == 0 )
						report.add( new ErrorMessage( ErrorMessage.ERROR , spObs.getTitle() , "Chop iterator required for beam switch mode" ) );
				}
				else if( switchingMode.equals( SpJCMTConstants.SWITCHING_MODE_POSITION ) )
				{
					if( target != null && !( target.getPosList().exists( "REFERENCE" ) ) )
						report.add( new ErrorMessage( ErrorMessage.ERROR , spObs.getTitle() , "Position switched observation requires a REFERENCE target" ) );
				}
			}
			else
			{
				report.add( new ErrorMessage( ErrorMessage.ERROR , spObs.getTitle() , "No switching mode set" ) );
			}
		}
		super.checkObservation( spObs , report );
	}

	public void checkMSB( SpMSB spMSB , Vector report )
	{
		if( spMSB instanceof SpObs )
		{
			checkObservation( ( SpObs ) spMSB , report );
		}
		else
		{
			// Check that the chop throws are the same in all cases - but only when there
			// is only one value in the chop iterator
			SpInstObsComp obsComp = SpTreeMan.findInstrument( spMSB );
			if( obsComp instanceof SpInstSCUBA )
			{
				Vector chopComponents = SpTreeMan.findAllInstances( spMSB , "gemini.sp.iter.SpIterChop" );
				if( chopComponents != null && chopComponents.size() > 1 )
				{
					boolean multipleIterator = false;
					for( int i = 0 ; i < chopComponents.size() ; i++ )
					{
						if( ( ( SpIterChop ) chopComponents.get( i ) ).getStepCount() > 1 )
						{
							multipleIterator = true;
							break;
						}
					}
					if( !multipleIterator )
					{
						double baseThrow = ( ( SpIterChop ) chopComponents.get( 0 ) ).getThrow( 0 );
						for( int i = 1 ; i < chopComponents.size() ; i++ )
						{
							double currThrow = ( ( SpIterChop ) chopComponents.get( i ) ).getThrow( 0 );
							if( currThrow != baseThrow )
							{
								report.add( new ErrorMessage( ErrorMessage.WARNING , spMSB.getTitle() , "MSB contains different chop throws for each component" ) );
								break;
							}
						}
					}
				}
			}
		}
		super.checkMSBgeneric( spMSB , report );
	}
}
