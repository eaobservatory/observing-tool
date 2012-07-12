package orac.ukirt.validation ;

import java.util.Vector ;
import java.util.Enumeration ;
import java.io.FileReader ;
import java.io.IOException ;
import java.io.InputStreamReader ;
import java.io.LineNumberReader ;
import gemini.sp.SpTreeMan ;
import gemini.sp.SpObs ;
import gemini.sp.SpSurveyContainer ;
import gemini.sp.SpProg ;
import orac.ukirt.inst.SpInstCGS4 ;
import orac.ukirt.inst.SpInstMichelle ;
import orac.ukirt.inst.SpInstUIST ;
import orac.ukirt.inst.SpInstWFCAM ;
import orac.ukirt.inst.SpInstIRCAM3 ;
import orac.ukirt.inst.SpInstUFTI ;
import orac.ukirt.inst.SpDRRecipe ;
import orac.ukirt.iter.SpIterDarkObs ;
import orac.ukirt.iter.SpIterFP ;
import orac.ukirt.iter.SpIterIRPOL ;
import orac.ukirt.iter.SpIterCGS4 ;
import orac.ukirt.iter.SpIterMichelle ;
import orac.ukirt.iter.SpIterUFTI ;
import orac.ukirt.iter.SpIterWFCAM ;
import orac.ukirt.iter.SpIterIRCAM3 ;
import orac.ukirt.iter.SpIterCGS4CalUnit ;
import orac.ukirt.iter.SpIterCGS4CalObs ;
import orac.ukirt.iter.SpIterBiasObs ;
import orac.ukirt.iter.SpIterNod ;
import gemini.sp.obsComp.SpInstObsComp ;
import gemini.sp.obsComp.SpSchedConstObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp ;
import gemini.sp.iter.SpIterConfigObs ;
import gemini.sp.iter.SpIterObserveBase ;
import gemini.sp.iter.SpIterFolder ;
import gemini.sp.SpItem ;
import gemini.sp.SpTelescopePos ;
import gemini.sp.SpTelescopePosList ;
import gemini.sp.SpObsContextItem ;
import gemini.util.RADecMath ;
import gemini.util.TelescopePos ;
import gemini.sp.iter.SpIterOffset ;
import gemini.sp.iter.SpIterChop ;
import gemini.sp.obsComp.SpSiteQualityObsComp ;
import orac.ukirt.iter.SpIterObserve ;
import orac.ukirt.iter.SpIterSky ;
import orac.validation.SpValidation ;
import orac.validation.ErrorMessage ;
import orac.util.SpInputXML ;
import orac.util.AirmassUtilities ;

import gemini.util.CoordSys ;
import orac.util.CoordConvert ;
import gemini.util.RADec ;

import gemini.util.HHMMSS ;
import gemini.util.DDMMSS ;

/**
 * Validation Tool.
 * 
 * This class is used for checking whether the values and settings in a Science Program or
 * Observation are sensible.
 *
 * Errors and warnings are issued otherwise.
 *
 * The class contains a main method and can be used as a stand alone command line tool to validate science programs.
 *
 * @author M.Folger@roe.ac.uk UKATC
 */
public class UkirtSpValidation extends SpValidation
{

	static String[] instruments = { "CGS4" , "IRCAM3" , "UFTI" , "Michelle" , "UIST" , "WFCAM" } ;

	/**
	 * Indicates whether the current is validation is a science program check or an observation check.
	 * 
	 * If this is set to true then the method checkObservation "knows" that it only has to check whether
	 * there is a target list. If the target list is inside the observation its values will be checked
	 * in checkContextItem (since an observation is also a context item). If the observation inherits its
	 * target list from the science program then the target list values will be checked in checkObservation
	 * only if the current check is an observation check (i.e. _isSpProgCheck is false). If the current check
	 * is a science program check (i.e. _isSpProgCheck is true) then the target list check is done once in
	 * checkSciProgram and not repaeted in all the observation checks.
	 *
	 * The same applies to checkDRRecipe.
	 */
	private boolean _isSpProgCheck = false ;

	/**
	 * Resets fields to their original values.
	 *
	 * NEVER USED.
	 *
	 * This reset method is not as "important" as, say, CGS4Validation#reset because none of the values
	 * that are reset is static and the values are all proberly initialised for each new UkirtSpValidation object.
	 */
	public void reset(){}

	/**
	 *
	 */
	public UkirtSpValidation()
	{
		reset() ;
		CGS4Validation.reset() ;
	}

	/**
	 * Searches for occurrence of a specified subclass of SpItem.
	 * Prints every match highlighted.
	 */
	public void findSpItemByClassName( SpItem spItem , String type , String tree_path )
	{
		SpItem child = null ;
		Enumeration<SpItem> children = spItem.children() ;
		String result = null ;

		if( spItem.getClass().toString().substring( 6 ).equals( type ) )
			result = tree_path + "  *" + spItem.type().getType() + "*  " ;
		else
			result = tree_path + "   " + spItem.type().getType() + "   " ;

		while( children.hasMoreElements() )
		{
			child = children.nextElement() ;
			findSpItemByClassName( child , type , result ) ;
		}
	}

	/**
	 * Returns true if SpItem of specified type and subtype is found in the subtree below SpItem argument.
	 *
	 * Uses the type and subtype strings defined in gemini.sp.SpType.
	 * Returns immediately once a match is found without checking whether there might be more matches.
	 */
	public boolean findSpItem( SpItem spItem , String type , String subtype )
	{
		boolean debug = false ;

		if( ( System.getProperty( "DEBUG" ) != null ) && System.getProperty( "DEBUG" ).equalsIgnoreCase( "ON" ) )
		{
			debug = true ;
			System.out.println( "examining " + spItem.name() + ": " + spItem.typeStr() + " (" + spItem.subtypeStr() + "), " + spItem.getClass().getName() ) ;
		}

		if( spItem.typeStr().equals( type ) && spItem.subtypeStr().equals( subtype ) )
		{
			if( debug )
				System.out.println( "Returning true" ) ;
			return true ;
		}

		SpItem child = null ;
		Enumeration<SpItem> children = spItem.children() ;
		boolean result = false ;

		while( children.hasMoreElements() && !result )
		{
			child = children.nextElement() ;
			result = findSpItem( child , type , subtype ) ;
		}

		if( debug )
			System.out.println( "Returning " + result ) ;

		return result ;
	}

	/**
	 * This method should not be called recursively.
	 * It should be called from with in another method that checks all SpItem recursively.
	 */
	public void checkInstrumentComponents( SpInstObsComp spInstObsComp , Vector<ErrorMessage> report )
	{
		if( report == null )
			report = new Vector<ErrorMessage>() ;

		if( spInstObsComp instanceof orac.ukirt.inst.SpInstMichelle )
			new MichelleValidation().checkInstrument( spInstObsComp , report ) ;
		else if( spInstObsComp instanceof orac.ukirt.inst.SpInstUIST )
			new UISTValidation().checkInstrument( spInstObsComp , report ) ;
		else if( spInstObsComp instanceof orac.ukirt.inst.SpInstWFCAM )
			new WFCAMValidation().checkInstrument( spInstObsComp , report ) ;
		else if( spInstObsComp instanceof orac.ukirt.inst.SpInstIRCAM3 )
			new IRCAM3Validation().checkInstrument( spInstObsComp , report ) ;
		else if( spInstObsComp instanceof orac.ukirt.inst.SpInstUFTI )
			new UFTIValidation().checkInstrument( spInstObsComp , report ) ;
		else if( spInstObsComp instanceof orac.ukirt.inst.SpInstCGS4 )
			new CGS4Validation().checkInstrument( spInstObsComp , report ) ;
		else
			System.out.println( "Unexpected instrument component: " + spInstObsComp.subtypeStr() ) ;
	}

	/**
	 *
	 */
	public void checkInstrumentIterators( Vector< SpItem > iterators , Vector<ErrorMessage> report )
	{
		if( iterators == null )
			return ;

		/*
		 * Checking for valid instrument component / instrument iterator combination.
		 * First check whether the iterator is instrument specific.
		 * This assumes that subtypes of instrument iterators start with "inst" (and NOT with "inst.")
		 */
		SpIterConfigObs currentIterator = null ;
		SpInstObsComp currentInstObsComp = null ;
		String currentInstrument = null ;
		// iteratorInstrument is not an instrument iterator but the name of the instrument of an instrument iterator.
		String iteratorInstrument = null ;

		for( Enumeration<SpItem> e = iterators.elements() ; e.hasMoreElements() ; )
		{
			currentIterator = ( SpIterConfigObs )e.nextElement() ;
			currentInstObsComp = SpTreeMan.findInstrument( currentIterator ) ;

			if( currentInstObsComp != null )
			{
				currentInstrument = currentInstObsComp.subtypeStr().substring( 5 ) ;
				iteratorInstrument = currentIterator.subtypeStr().substring( 4 ) ;

				for( int j = 0 ; j < instruments.length ; j++ )
				{
					if( instruments[ j ].compareToIgnoreCase( iteratorInstrument ) == 0 )
					{
						// If the iterator is instrument specific then it should be for the current instrument.
						if( ( currentInstrument != null ) && ( currentInstrument.compareToIgnoreCase( iteratorInstrument ) != 0 ) )
							report.add( new ErrorMessage( ErrorMessage.ERROR , iteratorInstrument + " iterator" , "You cannot use a " + iteratorInstrument + " iterator in combination with " + currentInstrument + "." ) ) ;
					}
				}

				// Checking for invalid instrument component / FP iterator combinations.
				if( currentIterator instanceof SpIterFP )
				{
					if( currentInstObsComp instanceof SpInstCGS4 || currentInstObsComp instanceof SpInstMichelle || currentInstObsComp instanceof SpInstUIST || currentInstObsComp instanceof SpInstWFCAM || currentInstObsComp instanceof SpInstIRCAM3 )
						report.add( new ErrorMessage( ErrorMessage.ERROR , "FP iterator" , "You cannot use an FP iterator in combination with the instrument " + currentInstrument + "." ) ) ;
				}

				// Check to mke sure IRPOLs do not contain other IRPOLs
				if( currentIterator instanceof SpIterIRPOL )
				{
					Vector<SpItem> irPolChildren = findInstances( ( SpIterIRPOL )currentIterator , SpIterIRPOL.class ) ;
					// There should be only 1 element in the array ; the current iterator
					if( irPolChildren != null && irPolChildren.size() > 1 )
						report.add( new ErrorMessage( ErrorMessage.ERROR , "IRPOL iterator" , "An IRPOL iterator should not contain another IRPOL iterator" ) ) ;
				}
			}
		}
	}

	/**
	 * New checkObservation.
	 *
	 * This is not called recursively anymore.
	 */
	public void checkObservation( SpObs spObs , Vector<ErrorMessage> report )
	{
		if( report == null )
			report = new Vector<ErrorMessage>() ;
		
		report.add( new ErrorMessage( ErrorMessage.INFO , separator , "" ) ) ;

		String titleString = titleString( spObs ) ;
		
		// Check whether the observation has an instrument (as its own child OR in its context).
		if( SpTreeMan.findInstrument( spObs ) != null && !_isSpProgCheck )
			checkInstrumentComponents( SpTreeMan.findInstrument( spObs ) , report ) ;

		/*
		 * Check whether the observation has a target list (as its own child OR in its context).
		 * Also need to check if this observation has a survey container as a parent.
		 */
		boolean hasSurveyParent = false ;
		SpItem parent = spObs.parent() ;
		while( parent != null )
		{
			if( parent instanceof SpSurveyContainer )
			{
				hasSurveyParent = true ;
				break ;
			}
			parent = parent.parent() ;
		}
		if( SpTreeMan.findTargetList( spObs ) == null && !hasSurveyParent )
		{
			report.add( new ErrorMessage( ErrorMessage.WARNING , titleString , "No target list." ) ) ;
		}
		else
		{
			if( !_isSpProgCheck && !hasSurveyParent )
				checkTargetList( SpTreeMan.findTargetList( spObs ) , report ) ;
		}

		// Check whether the observation a DR recipe (as its own child OR in its context).
		SpDRRecipe recipe = findRecipe( spObs ) ;
		if( recipe == null )
			report.add( new ErrorMessage( ErrorMessage.WARNING , titleString , "No Dr-recipe component." ) ) ;
		else if( !_isSpProgCheck )
			checkDRRecipe( recipe , report ) ;

		// Find iterators
		Vector<SpItem> observeIterators ;
		Vector<SpItem> instrumentIterators ;

		// First find observe iterators.
		observeIterators = findInstances( spObs , "gemini.sp.iter.SpIterObserveBase" ) ;

		if( observeIterators == null )
			report.add( new ErrorMessage( ErrorMessage.ERROR , titleString , "No observe iterator." ) ) ;

		// check for DARK observe iterators.
		observeIterators = findInstances( spObs , "orac.ukirt.iter.SpIterDarkObs" ) ;

		// Now find instrument iterators.
		instrumentIterators = findInstances( spObs , "gemini.sp.iter.SpIterConfigObs" ) ;

		if( instrumentIterators == null )
		{
			if( SpTreeMan.findInstrument( spObs ) == null )
				report.add( new ErrorMessage( ErrorMessage.ERROR , titleString , "Neither instrument iterator nor instrument component found." ) ) ;
		}
		else
		{
			checkInstrumentIterators( instrumentIterators , report ) ;
		}

		/*
		 * observeIterators    still contains DARK observe iterators and
		 * instrumentIterators stiil contains all instrument iterators
		 */
		SpInstObsComp instObsComp = null ;
		if( observeIterators != null )
		{
			double instExpTime = 0 ;
			double darkExpTime = 0 ;
			instObsComp = SpTreeMan.findInstrument( spObs ) ;
			SpIterConfigObs tmpInstIter = null ;
			double instIterExpTime = 0 ;

			if( instObsComp != null )
				instExpTime = instObsComp.getExposureTime() ;

			for( Enumeration<SpItem> e1 = observeIterators.elements() ; e1.hasMoreElements() ; )
			{
				darkExpTime = ( ( SpIterObserveBase )e1.nextElement() ).getExposureTime() ;

				if( darkExpTime != instExpTime )
					report.add( new ErrorMessage( ErrorMessage.WARNING , titleString , "DARK doesn't have same exposure time as instrument component." ) ) ;

				if( instrumentIterators != null )
				{
					for( Enumeration<SpItem> e2 = instrumentIterators.elements() ; e2.hasMoreElements() ; )
					{
						tmpInstIter = ( SpIterConfigObs )e2.nextElement() ;
						if( tmpInstIter != null && tmpInstIter.getExposureTimes() != null )
						{
							for( Enumeration<?> e3 = tmpInstIter.getExposureTimes().elements() ; e3.hasMoreElements() ; )
							{
								try
								{
									instIterExpTime = Double.parseDouble( ( String )e3.nextElement() ) ;
								}
								catch( NumberFormatException e )
								{
									e.printStackTrace() ;
								}

								if( darkExpTime != instIterExpTime )
									report.add( new ErrorMessage( ErrorMessage.WARNING , titleString , "DARK doesn't have same exposure time as instrument iterator." ) ) ;
							}
						}
					}
				}
			}
		}

		/*
		 * Checking that there is no offset followed by an instrument in the sequence 
		 * and whether an SpIterObserve in the sequence is followed by an instrument iterator or an offset iterator.
		 */
		SpItem previousItem = null ;
		SpItem currentItem = null ;
		SpIterFolder iterFolder = null ;
		boolean spIterObserveFound = false ;
		boolean dark = false ;
		Vector<SpItem> iterFolderVector = findInstances( spObs , "gemini.sp.iter.SpIterFolder" ) ;
		if( iterFolderVector != null )
		{
			for( Enumeration<SpItem> e1 = iterFolderVector.elements() ; e1.hasMoreElements() ; )
			{
				iterFolder = ( SpIterFolder )e1.nextElement() ;
				for( Enumeration<SpItem> e2 = iterFolder.children() ; e2.hasMoreElements() ; )
				{
					previousItem = currentItem ;
					currentItem = e2.nextElement() ;

					if( ( previousItem instanceof SpIterOffset ) && ( currentItem instanceof SpIterConfigObs ) )
						report.add( new ErrorMessage( ErrorMessage.WARNING , "Iterator Folder in " + titleString , "There is an offset iterator followed by an instrument iterator." ) ) ;

					if( currentItem instanceof SpIterObserve )
						spIterObserveFound = true ;

					if( spIterObserveFound && ( currentItem instanceof SpIterConfigObs ) )
						report.add( new ErrorMessage( ErrorMessage.ERROR , "Iterator Folder in " + titleString , "Instrument iterators are not allowed after the observe." ) ) ;

					if( currentItem instanceof SpIterDarkObs )
						dark = true ;
				}

				instObsComp = SpTreeMan.findInstrument( spObs ) ;
				if( ( instObsComp instanceof SpInstUFTI ) || ( instObsComp instanceof SpInstIRCAM3 ) )
				{
					if( !dark )
						report.add( new ErrorMessage( ErrorMessage.WARNING , titleString , "UFTI and IRCAM3 require DARK in sequence." ) ) ;
				}
			}
		}

		/*
		 * check for position iterators or instrument iterators that are on the same level 
		 * of iteration as the OBSERVE observe iterator.
		 */
		observeIterators = findInstances( spObs , "orac.ukirt.iter.SpIterObserve" ) ;
		SpIterObserve iterObserve = null ;
		SpItem sibling = null ;
		boolean wrongIteratorsOnObserveLevel = false ;

		if( observeIterators != null )
		{
			for( Enumeration<SpItem> e = observeIterators.elements() ; e.hasMoreElements() ; )
			{
				iterObserve = ( SpIterObserve )e.nextElement() ;

				sibling = iterObserve.next() ;
				while( ( sibling != null ) && ( !wrongIteratorsOnObserveLevel ) )
				{
					if( ( sibling instanceof SpIterConfigObs ) || ( sibling instanceof SpIterOffset ) )
						wrongIteratorsOnObserveLevel = true ;
					sibling = sibling.next() ;
				}

				sibling = iterObserve.prev() ;
				while( ( sibling != null ) && ( !wrongIteratorsOnObserveLevel ) )
				{
					if( ( sibling instanceof SpIterConfigObs ) || ( sibling instanceof SpIterOffset ) )
						wrongIteratorsOnObserveLevel = true ;
					sibling = sibling.prev() ;
				}

				if( wrongIteratorsOnObserveLevel )
					report.add( new ErrorMessage( ErrorMessage.WARNING , titleString , "The position iterator or instrument iterator and the \"observe\" are on the same level of iteration." ) ) ;
			}
		}
	}

	/**
	 * Checks validity of target list.
	 * 
	 * @param telescopeObsComp    Target list to be checked.
	 * @param report              error messages and warnings are appended to report.
	 */
	public void checkTargetList( SpTelescopeObsComp telescopeObsComp , Vector<ErrorMessage> report )
	{
		String titleString = titleString( telescopeObsComp ) ;
		if( !"".equals( titleString ) )
			titleString = " in " + titleString ;
		
		SpTelescopePosList list = telescopeObsComp.getPosList() ;
		TelescopePos[] position = list.getAllPositions() ;

		boolean hasGuideStar = false ;

		SpTelescopePos pos = null ;
		SpTelescopePos pos2 = null ;
		String trackingSystem = null ;
		String trackingSystem2 = null ;
		for( int i = 0 ; i < position.length ; i++ )
		{
			pos = ( SpTelescopePos )position[ i ] ;
			if( !hasGuideStar )
				hasGuideStar = pos.isGuidePosition() ;

			try
			{
				trackingSystem = telescopeObsComp.getTable().getAll( pos.getTag() ).get( 4 ) ;
			}
			catch( Exception e )
			{
				// catching possible NullPointerException or ArrayIndexOutOfBoundsException
				e.printStackTrace() ;
			}

			/*
			 * Checking RA aka x axis
			 * Note that getXaxis() maps values in the range 0..24 h typed by the user to degrees 0..360.
			 * It also works modulo 24. So 12:00:00.0 and 36:00:00.0 both yield 180.0.
			 * getXaxis also treats things like 9h 16min as equivalent to 8h 76min etc.
			 * 
			 * But the user should not be allowed to type 36h instead of 12h etc so checking is based on the
			 * original string.
			 */

			if( ( pos.getSystemType() == SpTelescopePos.SYSTEM_SPHERICAL ) && pos.isBasePosition() )
			{
				double Xaxis = pos.getXaxis() ;
				double Yaxis = pos.getYaxis() ;

    			// checking whether both RA and Dec are 0:00:00
    			if( Xaxis == 0 && Yaxis == 0 )
    				report.add( new ErrorMessage( ErrorMessage.WARNING , "Telescope target " + pos.getName() + titleString , "Both Dec and RA are 0:00:00" ) ) ;
					
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
    				report.add( new ErrorMessage( ErrorMessage.ERROR , "Telescope target " + pos.getName() + titleString , "RA" + converted , "range 0:00:00 .. 24:00:00" , pos.getXaxisAsString() ) ) ;
    
    			if( !DDMMSS.validate( ddmmss , -40 , 60 ) )
    				report.add( new ErrorMessage( ErrorMessage.ERROR , "Telescope target " + pos.getName() + titleString , "Dec" + converted , "range -40:00:00 .. 60:00:00" , pos.getYaxisAsString() ) ) ;
			}
			
			/*
			 * Check whether the target list has a target coordinate for which the epoch is B1950, 
			 * and a guide star coordinate for which the Ra and Dec values are identical 
			 * and the epoch is J2000.
			 */
			for( int j = 0 ; j < position.length ; j++ )
			{
				pos2 = ( SpTelescopePos )position[ j ] ;

				// Do not compare a position with itself
				if( i != j )
				{
					if( ( pos.getXaxis() == pos2.getXaxis() ) && ( pos.getYaxis() == pos2.getYaxis() ) )
					{
						try
						{
							trackingSystem2 = telescopeObsComp.getTable().getAll( pos2.getTag() ).get( 4 ) ;
						}
						catch( Exception e )
						{
							// catching possible NullPointerException or ArrayIndexOutOfBoundsException
							e.printStackTrace() ;
						}

						boolean trackingGood = trackingSystem != null && trackingSystem2 != null ;
						boolean guidesGood = pos.isGuidePosition() && !pos2.isGuidePosition() ;
						boolean systemsDiffer = trackingSystem != trackingSystem2 ;

						if( trackingGood && guidesGood && systemsDiffer )
							report.add( new ErrorMessage( ErrorMessage.ERROR , telescopeObsComp.getTitle() + titleString , "Target and guide star with differing coordinate systems have identical values for Ra and Dec." ) ) ;
					}
				}
			}
		}

		// Make sure the guide star is within 210 arcsecs of the base
		if( hasGuideStar )
		{
			SpTelescopePos basePos = list.getBasePosition() ;
			SpTelescopePos guidePos = ( SpTelescopePos )list.getPosition( "GUIDE" ) ;

			// shouldn't be null, after all it has a guide star
			if( basePos != null && guidePos != null )
			{
				String baseSystem = basePos.getCoordSysAsString() ;
				String guideSystem = guidePos.getCoordSysAsString() ;

				double baseRA = basePos.getXaxis() ;
				double baseDec = basePos.getYaxis() ;
				double guideRA = guidePos.getXaxis() ;
				double guideDec = guidePos.getYaxis() ;

				if( !baseSystem.equals( guideSystem ) )
				{
					if( baseSystem.equals( CoordSys.FK4_STRING ) )
					{
						RADec raDec = CoordConvert.Fk45z( baseRA , baseDec ) ;
						baseRA = raDec.ra ;
						baseDec = raDec.dec ;
					}
					else if( guideSystem.equals( CoordSys.FK4_STRING ) )
					{
						RADec raDec = CoordConvert.Fk45z( guideRA , guideDec ) ;
						guideRA = raDec.ra ;
						guideDec = raDec.dec ;
					}
				}

				// Guides are never offsets for ukirt, so we need to work out the offsets
				double[] offsets = RADecMath.getOffset( guideRA , guideDec , baseRA , baseDec ) ;
				for( int i = 0 ; i < offsets.length ; i++ )
				{
					if( Math.abs( offsets[ i ] ) > 210 )
					{
						report.add( new ErrorMessage( ErrorMessage.ERROR , basePos.getName() + titleString , "Guide star is more than 210 arcsecs from the base" ) ) ;
						break ;
					}
				}
			}
		}
		else
		{
			report.add( new ErrorMessage( ErrorMessage.WARNING , telescopeObsComp.getTitle() + titleString , "No guide star specified." ) ) ;
		}

                super.checkTargetList(telescopeObsComp, report);
	}

	public void checkDRRecipe( SpDRRecipe recipe , Vector<ErrorMessage> report )
	{
		String titleString = titleString( recipe ) ;
		if( !"".equals( titleString ) )
			titleString = " in " + titleString ;
		
		if( !( recipe.getArcInGroup() || recipe.getBiasInGroup() || recipe.getDarkInGroup() || recipe.getFlatInGroup() || recipe.getObjectInGroup() || recipe.getSkyInGroup() ) )
			report.add( new ErrorMessage( ErrorMessage.WARNING , "DR Recipe" + titleString , "No part included in group." ) ) ;

		SpInstObsComp inst = SpTreeMan.findInstrument( recipe ) ;

		if( inst == null )
		{
			report.add( new ErrorMessage( ErrorMessage.WARNING , "DR Recipe" + titleString , "Can't find instrument in scope." ) ) ;
		}
		else
		{
			Vector<String> recipes = null ;

			// NOTE that _IN_GROUP_ recipes are not considered because they are currently all set to false in SpDRRecipe.
			boolean flat = false ;
			boolean arc = false ;
			boolean bias = false ;
			boolean focus = false ;
			boolean darkSkyAndObject = false ;
			if( inst instanceof SpInstUFTI )
			{
				recipes = SpDRRecipe.UFTI.getColumn( 0 ) ;
				recipes.add( SpDRRecipe.UFTI_DARK_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.UFTI_SKY_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.UFTI_OBJECT_RECIPE_DEFAULT ) ;
				darkSkyAndObject = true ;
			}
			else if( inst instanceof SpInstIRCAM3 )
			{
				recipes = SpDRRecipe.IRCAM3.getColumn( 0 ) ;
				recipes.add( SpDRRecipe.IRCAM3_BIAS_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.IRCAM3_DARK_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.IRCAM3_SKY_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.IRCAM3_OBJECT_RECIPE_DEFAULT ) ;
				bias = true ;
				darkSkyAndObject = true ;
			}
			else if( inst instanceof SpInstCGS4 )
			{
				recipes = SpDRRecipe.CGS4.getColumn( 0 ) ;
				recipes.add( SpDRRecipe.CGS4_BIAS_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.CGS4_DARK_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.CGS4_FLAT_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.CGS4_ARC_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.CGS4_SKY_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.CGS4_OBJECT_RECIPE_DEFAULT ) ;
				bias = true ;
				arc = true ;
				flat = true ;
				darkSkyAndObject = true ;
			}
			else if( inst instanceof SpInstMichelle )
			{
				recipes = SpDRRecipe.MICHELLE.getColumn( 0 ) ;
				recipes.add( SpDRRecipe.MICHELLE_BIAS_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.MICHELLE_DARK_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.MICHELLE_ARC_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.MICHELLE_FLAT_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.MICHELLE_SKY_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.MICHELLE_OBJECT_RECIPE_DEFAULT ) ;
				bias = true ;
				arc = true ;
				flat = true ;
				darkSkyAndObject = true ;
			}
			else if( inst instanceof SpInstUIST )
			{
				recipes = SpDRRecipe.UIST.getColumn( 0 ) ;
				recipes.add( SpDRRecipe.UIST_BIAS_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.UIST_DARK_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.UIST_ARC_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.UIST_FLAT_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.UIST_SKY_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.UIST_OBJECT_RECIPE_DEFAULT ) ;
				bias = true ;
				arc = true ;
				flat = true ;
				darkSkyAndObject = true ;
			}
			else if( inst instanceof SpInstWFCAM )
			{
				recipes = SpDRRecipe.WFCAM.getColumn( 0 ) ;
				recipes.add( SpDRRecipe.WFCAM_BIAS_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.WFCAM_DARK_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.WFCAM_FLAT_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.WFCAM_SKY_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.WFCAM_FOCUS_RECIPE_DEFAULT ) ;
				recipes.add( SpDRRecipe.WFCAM_OBJECT_RECIPE_DEFAULT ) ;
				bias = true ;
				focus = true ;
				flat = true ;
				darkSkyAndObject = true ;
			}
			else
			{
				System.out.println( "No dr recipes for instrument " + inst.subtypeStr() ) ;
			}

			if( darkSkyAndObject )
			{
				if( !recipes.contains( recipe.getDarkRecipeName() ) )
					report.add( new ErrorMessage( ErrorMessage.WARNING , "DR Recipe (Dark) for " + inst.subtypeStr() + titleString , recipe.getDarkRecipeName() + " not in the OT list." ) ) ;

				if( !recipes.contains( recipe.getSkyRecipeName() ) )
					report.add( new ErrorMessage( ErrorMessage.WARNING , "DR Recipe (Sky) for " + inst.subtypeStr() + titleString , recipe.getSkyRecipeName() + " not in the OT list." ) ) ;

				if( !recipes.contains( recipe.getObjectRecipeName() ) )
					report.add( new ErrorMessage( ErrorMessage.WARNING , "DR Recipe (Object) for " + inst.subtypeStr() + titleString , recipe.getObjectRecipeName() + " not in the OT list." ) ) ;
			}

			if( flat && !recipes.contains( recipe.getFlatRecipeName() ) )
				report.add( new ErrorMessage( ErrorMessage.WARNING , "DR Recipe (Flat) for " + inst.subtypeStr() + titleString , recipe.getFlatRecipeName() + " not in the OT list." ) ) ;
			if( arc && !recipes.contains( recipe.getArcRecipeName() ) )
				report.add( new ErrorMessage( ErrorMessage.WARNING , "DR Recipe (Arc) for " + inst.subtypeStr() + titleString , recipe.getArcRecipeName() + " not in the OT list." ) ) ;
			if( bias && !recipes.contains( recipe.getBiasRecipeName() ) )
				report.add( new ErrorMessage( ErrorMessage.WARNING , "DR Recipe (Bias) for " + inst.subtypeStr() + titleString , recipe.getBiasRecipeName() + " not in the OT list." ) ) ;
			if( focus && !recipes.contains( recipe.getFocusRecipeName() ) )
				report.add( new ErrorMessage( ErrorMessage.WARNING , "DR Recipe (Focus) for " + inst.subtypeStr() + titleString , recipe.getFocusRecipeName() + " not in the OT list." ) ) ;
		}
	}

	/**
	 * Checks validity of the whole SciProgram tree.
	 * checkSciProgram is not just a top level class applied to SpProg objects. It is applied recursively
	 * to an spItem's children until a target list object or an observation object is found and checkTargetList or
	 * checkObservation, respectively, called.
	 * 
	 * @param spProg    Science program item to be checked.
	 * @param report    Error messages and warnings are appended to report.
	 */
	public void checkSciProgram( SpProg spProg , Vector<ErrorMessage> report )
	{
		_isSpProgCheck = true ;

		if( SpTreeMan.findAllItems( spProg , SpObs.class.getName() ).size() < 1 )
			report.add( new ErrorMessage( ErrorMessage.WARNING , spProg.getTitle() , "No observation." ) ) ;

		/*
		 * Check context items (e.g folders, groups, observations)
		 * Note that observations are checked twice: as context items AND in checkObservation for
		 * where things specific to observations are checked.
		 * 
		 * Start by calling checkContextItem for the science program itself
		 */
		checkContextItem( spProg , report ) ;

		// Checking for standard observation.
		boolean standardObsFound = false ;
		Vector<SpItem> instances = findInstances( spProg , "gemini.sp.SpObs" ) ;
		if( instances != null )
		{
			for( Enumeration<SpItem> e = instances.elements() ; e.hasMoreElements() && !standardObsFound ; )
			{
				if( ( ( SpObs )e.nextElement() ).getIsStandard() )
					standardObsFound = true ;
			}
		}

		if( !standardObsFound )
			report.add( new ErrorMessage( ErrorMessage.WARNING , spProg.getTitle() , "No observation has been chosen as standard." ) ) ;

		// CGS4 checks

		// Checking CGS4 masks
		if( CGS4Validation.getMasks().size() > 1 )
			report.add( new ErrorMessage( ErrorMessage.WARNING , spProg.getTitle() + ", CGS4" , "More than one slit width is specified." ) ) ;

		// Checking CGS4 samplings
		if( CGS4Validation.getSamplings().size() > 1 )
			report.add( new ErrorMessage( ErrorMessage.WARNING , spProg.getTitle() + ", CGS4" , "More than one sampling width is specified." ) ) ;

		// Checking CGS4 position angles
		if( CGS4Validation.getPosAngles().size() > 1 )
			report.add( new ErrorMessage( ErrorMessage.WARNING , spProg.getTitle() + ", CGS4" , "More than one position angle is specified." ) ) ;

		// Checking CGS4 calibration
		instances = findInstances( spProg , "orac.ukirt.inst.SpInstCGS4" ) ;

		/*
		 * if there are observations that use CGS4 check whether there is a CGS4 calibration observation
		 * i.e. an observation containing both ARC and FLAT
		 */
		if( instances != null && instances.size() > 0 )
		{
			if( findCGS4CalObservation( spProg ) == null )
				report.add( new ErrorMessage( ErrorMessage.WARNING , spProg.getTitle() + ", CGS4" , "There are one or more observations that use CGS4 but there is no observation that contains a FLAT and an ARC." ) ) ;
		}

		_isSpProgCheck = false ;

		super.checkSciProgram( spProg , report ) ;
	}

	/**
	 * This method is used to check the SpProg, SpObsGroup and SpObsFolder.
	 *
	 * In the case of SpProg check is called from within checkSciProgram which contains addtional checks.
	 * The method is NOT called recursively!!!
	 */
	public void checkContextItem( SpObsContextItem contextItem , Vector<ErrorMessage> report )
	{

		// check whether spItem is an observation.
		if( contextItem instanceof SpObs )
			checkObservation( ( SpObs )contextItem , report ) ;

		Enumeration<SpItem> children = contextItem.children() ;
		while( children.hasMoreElements() )
		{
			SpItem spItem = children.nextElement() ;

			/*
			 * This assumes that the instrument component appears above the instrument iterator in the tree.
			 * And it assumes that subtypes of instrument component start with "inst." (and NOT with "inst").
			 * currentInstrument = spItem.subtypeStr().substring(5) ;
			 */

			if( spItem instanceof SpIterFolder && !( contextItem instanceof SpObs ) )
				report.add( new ErrorMessage( ErrorMessage.WARNING , "Iterator Folder in " + contextItem.getTitle() , "Iterator folders should only be used inside an observation." ) ) ;
			else if( spItem instanceof SpInstObsComp )
				checkInstrumentComponents( ( SpInstObsComp )spItem , report ) ;
			else if( spItem instanceof SpTelescopeObsComp )
				checkTargetList( ( SpTelescopeObsComp )spItem , report ) ;
			else if( spItem instanceof SpDRRecipe )
				checkDRRecipe( ( SpDRRecipe )spItem , report ) ;
			else if( spItem instanceof SpObsContextItem )
				checkContextItem( ( SpObsContextItem )spItem , report ) ;

			// Check other things? 
		}
	}

	/**
	 * Find all SpItems below spItem that are instances of the class className.
	 * Similar to gemini.sp.SpTreeMan.findAllItems but gemini.sp.SpTreeMan.findAllItems returns only
	 * exact matches. This method returns spItems whose class is a subtype of that specified by classname too.
	 *
	 * @param result the instances are appended to the result vector. So findInstances can be called several
	 *               times with the same vactor so that instances can be accumulated.
	 *
	 * @see gemini.sp.SpTreeMan#findAllItems
	 */
	public static void findInstances( SpItem spItem , Class<?> c , Vector<SpItem> result )
	{
		if( result == null )
			result = new Vector<SpItem>() ;

		if( c.isInstance( spItem ) )
			result.add( spItem ) ;

		SpItem child = null ;
		Enumeration<SpItem> children = spItem.children() ;

		if( children.hasMoreElements() )
		{
			while( children.hasMoreElements() )
			{
				child = children.nextElement() ;
				findInstances( child , c , result ) ;
			}
		}
	}

	public static void findInstances( SpItem spItem , String className , Vector<SpItem> result )
	{
		try
		{
			findInstances( spItem , Class.forName( className ) , result ) ;
		}
		catch( ClassNotFoundException e )
		{
			e.printStackTrace() ;
		}
	}

	/**
	 * @see #findInstances(gemini.sp.SpItem, java.lang.Class, java.util.Vector)
	 */
	public static Vector<SpItem> findInstances( SpItem spItem , Class<?> c )
	{
		Vector<SpItem> result = new Vector<SpItem>() ;
		findInstances( spItem , c , result ) ;

		if( result.size() > 0 )
			return result ;

		else
			return null ;
	}

	public static Vector<SpItem> findInstances( SpItem spItem , String className )
	{
		Vector<SpItem> result = null ;

		try
		{
			result = findInstances( spItem , Class.forName( className ) ) ;
		}
		catch( ClassNotFoundException e )
		{
			e.printStackTrace() ;
		}

		return result ;
	}

	/**
	 * This method looks for observations whose iterator folder contains at least 2 observe iterators DARK and no other
	 * observe iterators.
	 *
	 * Such observations are assumed to be array tests.
	 *
	 * @return array test observations
	 */
	public static Vector<SpObs> findArrayTests( SpProg spProg )
	{
		Vector<SpObs> result = new Vector<SpObs>() ;
		Vector<SpItem> observations = null ;
		Vector<SpItem> observeIterators = null ;
		Vector<SpItem> darkIterators = null ;
		SpObs spObs = null ;

		observations = findInstances( spProg , "gemini.sp.SpObs" ) ;

		if( observations != null )
		{
			// Go through observations.
			for( Enumeration<SpItem> e = observations.elements() ; e.hasMoreElements() ; )
			{
				spObs = ( SpObs )e.nextElement() ;
				observeIterators = findInstances( spObs , "gemini.sp.iter.SpIterObserveBase" ) ;
				darkIterators = findInstances( spObs , "orac.ukirt.iter.SpIterDarkObs" ) ;

				// check that there are at least two dark iterators.
				if( darkIterators != null && darkIterators.size() >= 2 )
				{
					// check whether that there are no observe iterators other than dark iterators.
					if( observeIterators != null && observeIterators.size() == darkIterators.size() )
						result.add( spObs ) ;
				}
			}
		}

		if( result.size() > 0 )
			return result ;
		else
			return null ;
	}

	/**
	 * This method looks for observations whose iterator folder contains at least one CGS4 calibration observe iterator
	 * ARC and one CGS4 calibration observe iterator FLAT.
	 *
	 * @return matching observations.
	 */
	public static Vector<SpObs> findCGS4CalObservation( SpProg spProg )
	{
		Vector<SpObs> result = new Vector<SpObs>() ;
		Vector<SpItem> cgs4CalObsIterators = null ;
		Vector<SpItem> observations = null ;
		boolean arc = false ;
		boolean flat = false ;
		SpObs spObs = null ;

		observations = findInstances( spProg , "gemini.sp.SpObs" ) ;

		if( observations != null )
		{
			// Go through observations.
			for( Enumeration<SpItem> e = observations.elements() ; e.hasMoreElements() ; )
			{
				spObs = ( SpObs )e.nextElement() ;
				cgs4CalObsIterators = findInstances( spObs , "orac.ukirt.iter.SpIterCGS4CalObs" ) ;

				arc = false ;
				flat = false ;

				if( cgs4CalObsIterators != null )
				{
					for( Enumeration<SpItem> e2 = cgs4CalObsIterators.elements() ; e2.hasMoreElements() ; )
					{
						if( ( ( SpIterCGS4CalObs )e2.nextElement() ).getCalType() == SpIterCGS4CalObs.ARC )
							arc = true ;
						else
							flat = true ;
					}
					if( arc && flat )
						result.add( spObs ) ;
				}
			}
		}

		if( result.size() > 0 )
			return result ;
		else
			return null ;
	}

	/**
	 * Print a formatted string representation of the item with the given
	 * indentation for debugging.
	 *
	 * Based on method gemini.sp.SpItem.print.
	 */
	public void print( SpItem spItem , String indentStr )
	{
		String t = spItem.name() + ": " + spItem.typeStr() + " (" + spItem.subtypeStr() + "), " + spItem.getClass().getName() ;
		System.out.println( indentStr + t ) ;

		indentStr = indentStr + "   " ;

		// Print the attributes
		Enumeration<String> keys = spItem.getTable().attributes() ;
		while( keys.hasMoreElements() )
		{
			String key = keys.nextElement() ;
			System.out.println( indentStr + key + " (" + spItem.getTable().getDescription( key ) + ")" ) ;
			System.out.println( indentStr + "--> " + spItem.getTable().getAll( key ).toString() ) ;
		}

		// Print the children
		for( SpItem child = spItem.child() ; child != null ; child = child.next() )
			print( child , indentStr + "   " ) ;
	}

	public static void main( String[] args )
	{
		if( args.length < 1 )
		{
			System.err.println( "Usage: UkirtSpValidation <input XML file>" ) ;
			System.exit( 1 ) ;
		}

		UkirtSpValidation c = new UkirtSpValidation() ;

		/*
		 * Create instances of the UKIRT items - this will cause them to be
		 * registered with SpFactory - necessary so that the component subtypes
		 * can be understood.
		 */
		new SpInstUFTI() ;
		new SpInstCGS4() ;
		new SpInstIRCAM3() ;
		new SpInstMichelle() ;
		new SpInstUIST() ;
		new SpInstWFCAM() ;
		new SpDRRecipe() ;
		new SpIterBiasObs() ;
		new SpIterDarkObs() ;
		new SpIterCGS4() ;
		new SpIterMichelle() ;
		new SpIterUFTI() ;
		new SpIterWFCAM() ;
		new SpIterIRCAM3() ;
		new SpIterCGS4CalUnit() ;
		new SpIterCGS4CalObs() ;
		new SpIterFP() ;
		new SpIterIRPOL() ;
		new SpIterNod() ;
		new SpIterChop() ;
		new SpIterObserve() ;
		new SpIterSky() ;
		new SpSiteQualityObsComp() ;

		SpProg root = null ;
		SpInputXML inXML = new SpInputXML() ;
		try
		{
			SpItem item = inXML.xmlToSpItem( new FileReader( args[ 0 ] ) ) ;
			if( item instanceof SpProg )
				root = ( SpProg )item ;
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
		}

		int ch ;
		boolean correct_input = false ;

		try
		{
			LineNumberReader lineReader = new LineNumberReader( new InputStreamReader( System.in ) ) ;

			while( correct_input == false )
			{
				System.out.println( "Choose one option:" ) ;
				System.out.println( "  [p]rint" ) ;
				System.out.println( "  [c]heck science program" ) ;

				ch = lineReader.readLine().toCharArray()[ 0 ] ;

				switch( ch )
				{
					case 'p' :
						c.print( root , "" ) ;
						correct_input = true ;
						break ;

					case 'c' :
						Vector<ErrorMessage> report = new Vector<ErrorMessage>() ;
						c.checkSciProgram( root , report ) ;
						ErrorMessage.printMessages( report.elements() , System.out ) ;
						correct_input = true ;
						break ;

					default :
						System.out.println( "You typed an invalid character." ) ;
				}
			}
			lineReader.close() ;
		}
		catch( IOException e )
		{
			e.printStackTrace() ;
		}
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
	
	public void checkSchedComp( SpSchedConstObsComp schedule , Vector<ErrorMessage> report )
	{
		checkSchedElevationRange( schedule , report ) ;
		super.checkSchedComp( schedule , report ) ;
	}
	
	public void checkSchedElevationRange( SpSchedConstObsComp schedule , Vector<ErrorMessage> report )
	{
		String min = schedule.getMinElevation() ;
		String max = schedule.getMaxElevation() ;
		try
		{
			double minEl = new Double( min ) ;
			double maxEl = new Double( max ) ;
			
			if( schedule.getDisplayAirmass() )
			{
				minEl = AirmassUtilities.elevationToAirmass( minEl ) ;
				maxEl = AirmassUtilities.elevationToAirmass( maxEl ) ;
				if( minEl > 2. )
    				report.add( new ErrorMessage( ErrorMessage.ERROR , "Max elevation as airmass in schedule constraint is greater than 2." , "" ) ) ;
    			if( maxEl < 1. )
    				report.add( new ErrorMessage( ErrorMessage.ERROR , "Min elevation as airmass in schedule constraint is less than 1." , "" ) ) ;

			}
			else
			{			
    			if( minEl < 30. )
    				report.add( new ErrorMessage( ErrorMessage.ERROR , "Min elevation in schedule constraint is less than 30." , "" ) ) ;
    			if( maxEl > 90. )
    				report.add( new ErrorMessage( ErrorMessage.ERROR , "Max elevation in schedule constraint is greater than 90." , "" ) ) ;
			}
		}
		catch( NumberFormatException nfe )
		{
			report.add( new ErrorMessage( ErrorMessage.ERROR , "Values for elevation in schedule constraint not numeric." , "" ) ) ;
		}
		catch( NullPointerException npe )
		{
			report.add( new ErrorMessage( ErrorMessage.ERROR , "No value set for elevation in schedule constraint." , "" ) ) ;
		}
	}
}
