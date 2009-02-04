package orac.jcmt.validation ;

import java.util.Enumeration ;
import java.util.Vector ;

import gemini.sp.SpItem ;
import gemini.sp.SpObsContextItem ;
import gemini.sp.SpTelescopePos ;
import gemini.sp.SpTelescopePosList ;
import gemini.sp.SpTreeMan ;
import gemini.sp.SpObs ;
import gemini.sp.SpMSB ;
import orac.jcmt.inst.SpInstHeterodyne ;
import orac.jcmt.inst.SpInstSCUBA ;
import orac.jcmt.inst.SpInstSCUBA2 ;
import orac.jcmt.iter.SpIterJCMTObs ;
import gemini.sp.obsComp.SpInstObsComp ;
import gemini.sp.obsComp.SpTelescopeObsComp ;
import gemini.sp.iter.SpIterChop ;
import gemini.util.DDMMSS;
import gemini.util.HHMMSS;
import gemini.util.TelescopePos ;
import orac.jcmt.SpJCMTConstants ;
import orac.jcmt.inst.SpDRRecipe ;
import orac.validation.SpValidation ;
import orac.validation.ErrorMessage ;

import orac.jcmt.iter.SpIterJiggleObs ;
import orac.jcmt.iter.SpIterNoiseObs ;

import java.lang.reflect.Method ;
import java.lang.reflect.InvocationTargetException ;
import java.lang.reflect.Field ;

import jsky.app.ot.util.CoordSys ;

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

	public void checkObservation( SpObs spObs , Vector<ErrorMessage> report )
	{
		if( report == null )
			report = new Vector<ErrorMessage>() ;
		
		report.add( new ErrorMessage( ErrorMessage.INFO , separator , "" ) ) ;
		
		String titleString = titleString( spObs ) ;

		SpInstObsComp obsComp = SpTreeMan.findInstrument( spObs ) ;
		SpTelescopeObsComp target = SpTreeMan.findTargetList( spObs ) ;
		Vector<SpItem> observes = SpTreeMan.findAllInstances( spObs , SpIterJCMTObs.class.getName() ) ;
		for( int count = 0 ; count < observes.size() ; count++ )
		{
			SpIterJCMTObs thisObs = ( SpIterJCMTObs )observes.elementAt( count ) ;
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
					Class<?> frequencyEditorClass = Class.forName( "edfreq.FrequencyEditorCfg" ) ;
					Method getConfiguration = frequencyEditorClass.getDeclaredMethod( "getConfiguration" , new Class[]{} ) ;
					Object frequencyEditor = frequencyEditorClass.newInstance() ;
					Object requencyEditorCfg = getConfiguration.invoke( frequencyEditor , new Object[]{} ) ;
					Field receiverField = requencyEditorCfg.getClass().getDeclaredField( "receivers" ) ;
					Object receivers = receiverField.get( requencyEditorCfg ) ;
					Method get = receivers.getClass().getDeclaredMethod( "get" , new Class[]{ Object.class } ) ;
					Object receiver = get.invoke( receivers , new Object[] { spInstHeterodyne.getFrontEnd() } ) ;
					Field loMinField = receiver.getClass().getDeclaredField( "loMin" ) ;
					Field loMaxField = receiver.getClass().getDeclaredField( "loMax" ) ;
					Object loMinObject = loMinField.get( receiver ) ;
					Object loMaxObject = loMaxField.get( receiver ) ;
					if( loMinObject instanceof Double )
						loMin = ( ( Double )loMinObject ).doubleValue() ;
					if( loMaxObject instanceof Double )
						loMax = ( ( Double )loMaxObject ).doubleValue() ;
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
					report.add( new ErrorMessage( ErrorMessage.WARNING , titleString , "Rest frequency of " + skyFrequency + " is lower than receiver minimum " + loMin ) ) ;
				if( loMax != 0. && ( loMax + spInstHeterodyne.getFeIF() ) < skyFrequency )
					report.add( new ErrorMessage( ErrorMessage.WARNING , titleString , "Rest frequency of " + skyFrequency + " is greater than receiver maximum " + loMax ) ) ;

				int available = new Integer( spInstHeterodyne.getBandMode() ).intValue() ;
				String sideBand = spInstHeterodyne.getBand() ;
				for( int index = 0 ; index < available ; index++ )
				{
					double centre = spInstHeterodyne.getCentreFrequency( index ) ;
					double rest = spInstHeterodyne.getRestFrequency( index ) ;
					if( "lsb".equals( sideBand ) && ( rest + centre ) > loMax )
						report.add( new ErrorMessage( ErrorMessage.WARNING , titleString , "Need to use upper or best sideband to reach the line " + rest ) ) ;
					else if( !"lsb".equals( sideBand ) && ( rest - centre ) < loMin )
						report.add( new ErrorMessage( ErrorMessage.WARNING , titleString , "Need to use lower sideband to reach the line " + rest ) ) ;
					
				}

				if( thisObs instanceof SpIterJiggleObs )
				{
					SpIterJiggleObs spIterJiggleObs = ( SpIterJiggleObs )thisObs ;
					String frontEnd = spInstHeterodyne.getFrontEnd() ;
					String jigglePattern = spIterJiggleObs.getJigglePattern() ;
					if( frontEnd == null )
						report.add( new ErrorMessage( ErrorMessage.ERROR , titleString , "Front end is null" ) ) ;
					else if( jigglePattern == null )
						report.add( new ErrorMessage( ErrorMessage.ERROR , titleString , "Jiggle pattern not initialised" ) ) ;
					else if( jigglePattern.startsWith( "HARP" ) && !frontEnd.startsWith( "HARP" ) )
						report.add( new ErrorMessage( ErrorMessage.ERROR , titleString , "Cannot use " + jigglePattern + " jiggle pattern without HARP-B frontend" ) ) ;
				}
				if( thisObs instanceof SpIterNoiseObs )
					report.add( new ErrorMessage( ErrorMessage.ERROR , spObs.getTitle() , "Cannot use Noise observations with Hetrodyne" ) ) ;
			}
			// Also check the switching mode.  If we are in beam switch, we need a chop iterator, in position we need a reference in the target
			String switchingMode = thisObs.getSwitchingMode() ;
			if( switchingMode != null )
			{
				if( switchingMode.equals( SpJCMTConstants.SWITCHING_MODE_BEAM ) )
				{
					Vector<SpItem> chops = SpTreeMan.findAllInstances( spObs , SpIterChop.class.getName() ) ;
					if( chops == null || chops.size() == 0 )
						report.add( new ErrorMessage( ErrorMessage.ERROR , titleString , "Chop iterator required for beam switch mode" ) ) ;
				}
				else if( switchingMode.equals( SpJCMTConstants.SWITCHING_MODE_POSITION ) )
				{
					if( target != null && !( target.getPosList().exists( "REFERENCE" ) ) )
						report.add( new ErrorMessage( ErrorMessage.ERROR , titleString , "Position switched observation requires a REFERENCE target" ) ) ;
				}
			}
			else if( obsComp != null && !( obsComp instanceof SpInstSCUBA2 ) )
			{
				report.add( new ErrorMessage( ErrorMessage.ERROR , titleString , "No switching mode set" ) ) ;
			}
			
			// Check whether the observation a DR recipe (as its own child OR in its context).
			SpDRRecipe recipe = ( SpDRRecipe )findRecipe( spObs ) ;
			if( recipe == null )
				report.add( new ErrorMessage( ErrorMessage.WARNING , titleString , "No Dr-recipe component." ) ) ;
			else
				checkDRRecipe( recipe , report , titleString , thisObs ) ;
		}
		super.checkObservation( spObs , report ) ;
	}

	public void checkDRRecipe( SpDRRecipe recipe , Vector<ErrorMessage> report , String obsTitle , SpIterJCMTObs thisObs )
	{
		SpInstObsComp _inst = SpTreeMan.findInstrument( recipe ) ;
		String instrument = null ;
		if( _inst instanceof SpInstHeterodyne )
			instrument = "heterodyne" ;
		else if( _inst instanceof SpInstSCUBA2 )
			instrument = "scuba2" ;
		
		String[] types = recipe.getAvailableTypes( instrument ) ;
		int size = 0 ;
		if( types != null )
			size = types.length ;
		for( int index = 0 ; index < size ; index++ )
		{
			String type = types[ index ] ;
			String shortType = "" ;
			if( type.toLowerCase().endsWith( "recipe" ) )
				shortType = type.substring( 0 , type.length() - "recipe".length() ) ;
			if( thisObs.getClass().getName().toLowerCase().indexOf( shortType ) == -1 )
				continue ;
			String recipeForType = recipe.getRecipeForType( type ) ;
			if( recipeForType == null )
				report.add( new ErrorMessage( ErrorMessage.WARNING , obsTitle , "No data reduction recipe set for " + instrument + " " + shortType ) ) ;
		}
	}
	
	public void checkMSB( SpMSB spMSB , Vector<ErrorMessage> report )
	{
		if( spMSB instanceof SpObs )
		{
			checkObservation( ( SpObs )spMSB , report ) ;
		}
		else
		{
			// Check that the chop throws are the same in all cases - but only when there is only one value in the chop iterator
			SpInstObsComp obsComp = SpTreeMan.findInstrument( spMSB ) ;
			if( obsComp instanceof SpInstSCUBA )
			{
				Vector<SpItem> chopComponents = SpTreeMan.findAllInstances( spMSB , SpIterChop.class.getName() ) ;
				if( chopComponents != null && chopComponents.size() > 1 )
				{
					boolean multipleIterator = false ;
					for( int i = 0 ; i < chopComponents.size() ; i++ )
					{
						if( ( ( SpIterChop )chopComponents.get( i ) ).getStepCount() > 1 )
						{
							multipleIterator = true ;
							break ;
						}
					}
					if( !multipleIterator )
					{
						double baseThrow = ( ( SpIterChop )chopComponents.get( 0 ) ).getThrow( 0 ) ;
						for( int i = 1 ; i < chopComponents.size() ; i++ )
						{
							double currThrow = ( ( SpIterChop )chopComponents.get( i ) ).getThrow( 0 ) ;
							if( currThrow != baseThrow )
							{
								report.add( new ErrorMessage( ErrorMessage.WARNING , spMSB.getTitle() , "MSB contains different chop throws for each component" ) ) ;
								break ;
							}
						}
					}
				}
			}
		}
		super.checkMSBgeneric( spMSB , report ) ;
	}

	/**
	 * Void method really, should not be called, 
	 * exists here only to differentiate between 
	 * super.checkMSBgeneric and this.checkMSBgeneric
	 */
	protected void checkMSBgeneric( SpMSB spMSB , Vector<ErrorMessage> report )
	{
		throw new RuntimeException( "Should not have ended up in JcmtSpValidation.checkMSBgeneric" ) ;
	}
	
	protected void checkTargetList( SpTelescopeObsComp obsComp , Vector<ErrorMessage> report )
	{
		if( obsComp != null )
		{
			String titleString = titleString( obsComp ) ;
			if( !"".equals( titleString ) )
				titleString = " in " + titleString ;
			
			SpTelescopePosList list = obsComp.getPosList() ;
			TelescopePos[] position = list.getAllPositions() ;
			SpInstObsComp instrument = SpTreeMan.findInstrument( obsComp ) ;
			boolean heterodyne = instrument instanceof SpInstHeterodyne ;

			if( heterodyne )
			{
				String errorText = "Named Systems or Orbital Elements require a Topocentric velocity frame." ;
				SpInstHeterodyne heterodyneInstrument = ( SpInstHeterodyne )instrument ;
				for( int i = 0 ; i < position.length ; i++ )
				{
					SpTelescopePos pos = ( SpTelescopePos )position[ i ] ;
					if( pos.getSystemType() != SpTelescopePos.SYSTEM_SPHERICAL )
					{
						String hetVelocityFrame = heterodyneInstrument.getVelocityFrame() ;
						if( hetVelocityFrame != null )
						{
							if( !hetVelocityFrame.equals( SpInstHeterodyne.TOPOCENTRIC_VELOCITY_FRAME ) )
							{
								report.add( new ErrorMessage( ErrorMessage.ERROR , "Telescope target " + pos.getName() + titleString , errorText ) ) ;
								break ;
							}
						}
						else if( pos.isBasePosition() )
						{
							String posVelocityFrame = pos.getTrackingRadialVelocityFrame() ;
							if( !SpInstHeterodyne.TOPOCENTRIC_VELOCITY_FRAME.equals( posVelocityFrame ) )
							report.add( new ErrorMessage( ErrorMessage.ERROR , "Telescope target " + pos.getName() + titleString , errorText ) ) ;
							break ;
						}
					}
					else
					{	
						if( pos.isBasePosition() && ( pos.getCoordSys() == CoordSys.FK5 || pos.getCoordSys() == CoordSys.FK4 ) )
						{
    		    			if( !HHMMSS.validate( pos.getXaxisAsString() ) )
    		    				report.add( new ErrorMessage( ErrorMessage.ERROR , "Telescope target " + pos.getName() + titleString , "RA" , "range 0:00:00 .. 24:00:00" , pos.getXaxisAsString() ) ) ;
    		    
    		    			if( !DDMMSS.validate( pos.getYaxisAsString() , -50 , 90 ) )
    		    				report.add( new ErrorMessage( ErrorMessage.ERROR , "Telescope target " + pos.getName() + titleString , "Dec" , "range -50:00:00 .. 90:00:00" , pos.getYaxisAsString() ) ) ;
						}

		    			// checking whether both RA and Dec are 0:00:00
		    			if( pos.getXaxis() == 0 && pos.getYaxis() == 0 )
		    				report.add( new ErrorMessage( ErrorMessage.WARNING , "Telescope target " + pos.getName() + titleString , "Both Dec and RA are 0:00:00" ) ) ;
					}
				}
			}
		}
		super.checkTargetList( obsComp , report ) ;
	}
	
	/**
	 * Find a data-reduction recipe component associated with the
	 * scope of the given item.  This traverses the tree.
	 *
	 * @param spItem the SpItem defining the scope to search
	 */
	public static SpDRRecipe findRecipe( SpItem spItem )
	{
		SpItem child ; // Child of spItem
		Enumeration<SpItem> children ; // Children of the sequence
		SpItem parent ; // Parent of spItem
		SpItem searchItem ; // The sequence item to search

		if( spItem instanceof SpDRRecipe )
			return ( SpDRRecipe )spItem ;

		// Get the parent.
		parent = spItem.parent() ;

		// Either the item is an observation context, which is what we want, or continue the search one level higher in the hierarchy.
		if( !( spItem instanceof SpObsContextItem ) )
		{
			searchItem = parent ;
			if( parent == null )
				return null ;
		}
		else
		{
			searchItem = spItem ;
		}

		// Search the observation context for the data-reduction recipe.
		children = searchItem.children() ;
		while( children.hasMoreElements() )
		{
			child = children.nextElement() ;
			if( child instanceof SpDRRecipe )
				return ( SpDRRecipe )child ;
		}

		if( parent != null )
			return findRecipe( parent ) ;

		return null ;
	}
}
