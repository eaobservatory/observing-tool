package orac.jcmt.validation ;

import java.util.Enumeration ;
import java.util.Vector ;

import gemini.sp.SpItem ;
import gemini.sp.SpObsContextItem ;
import gemini.sp.SpTelescopePos ;
import gemini.sp.SpTelescopePosList ;
import gemini.sp.SpTreeMan ;
import gemini.sp.SpObs ;
import orac.jcmt.inst.SpInstHeterodyne ;
import orac.jcmt.inst.SpInstSCUBA2 ;
import orac.jcmt.iter.SpIterJCMTObs ;
import gemini.sp.obsComp.SpInstObsComp ;
import gemini.sp.obsComp.SpTelescopeObsComp ;
import gemini.sp.iter.SpIterChop ;
import gemini.util.CoordSys ;
import gemini.util.DDMMSS ;
import gemini.util.HHMMSS ;
import gemini.util.RADec ;
import gemini.util.TelescopePos ;
import orac.jcmt.SpJCMTConstants ;
import orac.jcmt.inst.SpDRRecipe ;
import orac.util.CoordConvert ;
import orac.validation.SpValidation ;
import orac.validation.ErrorMessage ;

import orac.jcmt.iter.SpIterJiggleObs ;
import orac.jcmt.iter.SpIterNoiseObs ;
import orac.jcmt.iter.SpIterPOL;

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

				/* Check for retired receivers which are still present to allow
				   programmes to be loaded successfully. */
				if (spInstHeterodyne.getFrontEnd() != null && spInstHeterodyne.getFrontEnd().equals("A3")) {
					report.add(new ErrorMessage(ErrorMessage.WARNING, titleString,
						"Receiver RxA3 was removed for upgrade in January 2013.  Please update your programme to use RxA3M and check your observing frequencies carefully."));
				}

				double loMin = 0. ;
				double loMax = 0. ;
				double[] defaultOverlaps = null ;
				double[] bandwidths = null ;
				int systems = new Integer( spInstHeterodyne.getBandMode() ) ;

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
					Object receiver = get.invoke( receivers , new Object[]{ spInstHeterodyne.getFrontEnd() } ) ;
					Field loMinField = receiver.getClass().getDeclaredField( "loMin" ) ;
					Field loMaxField = receiver.getClass().getDeclaredField( "loMax" ) ;
					Object loMinObject = loMinField.get( receiver ) ;
					Object loMaxObject = loMaxField.get( receiver ) ;
					if( loMinObject instanceof Double )
						loMin = ( Double )loMinObject ;
					if( loMaxObject instanceof Double )
						loMax = ( Double )loMaxObject ;

					Field bandSpecs = receiver.getClass().getDeclaredField( "bandspecs" ) ;
					Object bandSpecVector = bandSpecs.get( receiver ) ;
					if( bandSpecVector instanceof Vector )
					{
						Vector receiverBandSpecs = ( Vector )bandSpecVector ;
						Object bandSpec = receiverBandSpecs.get( systems - 1 ) ;
						Field overlapField = bandSpec.getClass().getField( "defaultOverlaps" ) ;
						Object overlapObject = overlapField.get( bandSpec ) ;
						if( overlapObject instanceof double[] )
							defaultOverlaps = ( double[] )overlapObject ;

						Method getBandwidths = bandSpec.getClass().getDeclaredMethod( "getDefaultOverlapBandWidths" , new Class[]{} ) ;
						Object bandwidthsObject = getBandwidths.invoke( bandSpec , new Object[]{} ) ;
						if( bandwidthsObject instanceof double[] )
							bandwidths = ( double[] )bandwidthsObject ;
					}
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

				int available = new Integer( spInstHeterodyne.getBandMode() ) ;
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

				if( defaultOverlaps != null && bandwidths != null )
				{
					for( int system = 0 ; system < systems ; system++ )
					{
						double overLap = spInstHeterodyne.getOverlap( system ) ;
						double bandwidth = spInstHeterodyne.getBandWidth( system ) ;
						for( int index = 0 ; index < bandwidths.length ; index++ )
						{
							if( bandwidth == bandwidths[ index ] )
							{
								double defaultOverlap = defaultOverlaps[ index ] ;
								if( overLap != defaultOverlap )
									report.add( new ErrorMessage( ErrorMessage.ERROR , titleString , "Overlap invalid for system " + ( system + 1 ) + ", should be " + defaultOverlap + " and not " + overLap + "." ) ) ;
								break ;
							}
						}
					}
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
			// Also check the switching mode.  If we are in beam switch, we need a chop iterator, in position or freq we need a reference in the target
			String switchingMode = thisObs.getSwitchingMode() ;
			if( switchingMode != null )
			{
				if( switchingMode.equals( SpJCMTConstants.SWITCHING_MODE_BEAM ) )
				{
					Vector<SpItem> chops = SpTreeMan.findAllInstances( spObs , SpIterChop.class.getName() ) ;
					if( chops == null || chops.size() == 0 )
						report.add( new ErrorMessage( ErrorMessage.ERROR , titleString , "Chop iterator required for beam switch mode" ) ) ;
				}
				else if( switchingMode.equals( SpJCMTConstants.SWITCHING_MODE_POSITION ) || switchingMode.equals( SpJCMTConstants.SWITCHING_MODE_FREQUENCY_F ) || switchingMode.equals( SpJCMTConstants.SWITCHING_MODE_FREQUENCY_S ) )
				{
					if( target != null && !( target.getPosList().exists( "REFERENCE" ) ) )
						report.add( new ErrorMessage( ErrorMessage.ERROR , titleString , "Position or Frequency switched observation requires a REFERENCE target" ) ) ;
				}
			}
			else if( obsComp != null && !( obsComp instanceof SpInstSCUBA2 ) )
			{
				report.add( new ErrorMessage( ErrorMessage.ERROR , titleString , "No switching mode set" ) ) ;
			}
			
			// Check whether the observation a DR recipe (as its own child OR in its context).
			SpDRRecipe recipe = findRecipe(spObs);
			if( recipe == null && ( ( obsComp != null && !( obsComp instanceof SpInstSCUBA2 ) ) ) )
				report.add( new ErrorMessage( ErrorMessage.WARNING , titleString , "No DR-recipe component." ) ) ;
			else if( recipe != null )
				checkDRRecipe( recipe , report , titleString , thisObs ) ;
		}

		// Check POL-2 iterators.
		for (SpItem item: SpTreeMan.findAllInstances(spObs, SpIterPOL.class.getName())) {
			SpIterPOL pol = (SpIterPOL) item;

			// Currently SpIterPOL returns 1 step count for continuous spin mode,
			// but testing it explicitly here anyway in case that behaviour changes.
			if (!(pol.hasContinuousSpin() || pol.getConfigStepCount() != 0)) {
				report.add(new ErrorMessage(ErrorMessage.ERROR, titleString , "POL iterator is not continuous spin but does not have waveplate angles selected." ) ) ;
			}
		}


		super.checkObservation( spObs , report ) ;
	}

	public void checkDRRecipe( SpDRRecipe recipe , Vector<ErrorMessage> report , String obsTitle , SpIterJCMTObs thisObs )
	{
		SpInstObsComp _inst = SpTreeMan.findInstrument( recipe ) ;
		String instrument = null ;
		Vector<String> recipeList = null ;
		if( _inst instanceof SpInstHeterodyne )
		{
			instrument = "heterodyne" ;
			recipeList = SpDRRecipe.HETERODYNE.getColumn( 0 ) ;
		}
		else if( _inst instanceof SpInstSCUBA2 )
		{
			instrument = "scuba2" ;
			recipeList = SpDRRecipe.SCUBA2.getColumn( 0 ) ;
		}

		String[] types = recipe.getAvailableTypes( instrument ) ;
		if( types != null )
		{
			for( String type : types )
			{
				String shortType = "" ;
				if( type.toLowerCase().endsWith( "recipe" ) )
					shortType = type.substring( 0 , type.length() - "recipe".length() ) ;
				if( thisObs.getClass().getName().toLowerCase().indexOf( shortType ) == -1 )
					continue ;
				String recipeForType = recipe.getRecipeForType( type ) ;
				if( recipeForType == null && !"scuba2".equals( instrument ) )
					report.add( new ErrorMessage( ErrorMessage.WARNING , obsTitle , "No data reduction recipe set for " + instrument + " " + shortType ) ) ;
				else if( recipeForType != null && !recipeList.contains( recipeForType ) )
					report.add( new ErrorMessage( ErrorMessage.ERROR , obsTitle , recipeForType + " does not appear to be a valid recipe for " + instrument + " " + shortType ) ) ;
			}
		}
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
						double Xaxis = pos.getXaxis() ;
						double Yaxis = pos.getYaxis() ;

		    			// checking whether both RA and Dec are 0:00:00
		    			if( Xaxis == 0 && Yaxis == 0 )
		    				report.add( new ErrorMessage( ErrorMessage.WARNING , "Telescope target " + pos.getName() + titleString , "Both Dec and RA are 0:00:00" ) ) ;
						
						if( pos.isBasePosition() )
						{						
							int coordSystem = pos.getCoordSys() ;
							
							String converted = "" ;
							
							if( coordSystem == CoordSys.FK4 )
							{
								RADec raDec = CoordConvert.Fk45z( Xaxis , Yaxis ) ;
								Xaxis = raDec.ra ;
								Yaxis = raDec.dec ;
								converted = " converted from FK4 " ;
							}
							else if( coordSystem == CoordSys.GAL )
							{
								RADec raDec = CoordConvert.gal2fk5( Xaxis , Yaxis ) ;
								Xaxis = raDec.ra ;
								Yaxis = raDec.dec ;
								converted = " converted from Galactic " ;
							}
							
							String hhmmss = HHMMSS.valStr( Xaxis ) ;
							String ddmmss = DDMMSS.valStr( Yaxis ) ;
							
    		    			if( !HHMMSS.validate( hhmmss ) )
    		    				report.add( new ErrorMessage( ErrorMessage.ERROR , "Telescope target " + pos.getName() + titleString , "RA" + converted , "range 0:00:00 .. 24:00:00" , hhmmss ) ) ;
    		    
    		    			if( !DDMMSS.validate( ddmmss , -50 , 90 ) )
    		    				report.add( new ErrorMessage( ErrorMessage.ERROR , "Telescope target " + pos.getName() + titleString , "Dec" + converted , "range -50:00:00 .. 90:00:00" , ddmmss ) ) ;
						}
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
