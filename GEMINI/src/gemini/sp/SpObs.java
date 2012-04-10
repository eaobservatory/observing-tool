// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp ;

import gemini.util.ConfigWriter ;
import gemini.util.CoordSys ;

import gemini.sp.iter.SpIterFolder ;
import gemini.sp.iter.SpIterMicroStep ;
import gemini.sp.iter.SpIterOffset ;
import gemini.sp.obsComp.SpInstObsComp ;
import gemini.sp.obsComp.SpMicroStepUser ;
import gemini.sp.obsComp.SpTelescopeObsComp ;

import java.io.FileWriter ;
import java.io.IOException ;

import java.util.Enumeration ;
import java.util.Hashtable ;
import java.util.Vector ;

import gemini.util.TranslationUtils ;
import gemini.util.JACLogger ;

/**
 * The observation item. In addition to other attributes, the SpObs class
 * contains two attributes that determine whether the observation is chained to
 * the next or previous observation (if any). 17Apr00 AB Added standard flag to
 * this.
 */
@SuppressWarnings( "serial" )
public class SpObs extends SpMSB implements SpTranslatable , SpTranslationConstants
{
	static JACLogger logger = JACLogger.getLogger( SpObs.class ) ;

	/**
     * This attribute records if the observation is to be treated as a
     * "standard"
     */
	public static final String ATTR_STANDARD = "standard" ;

	/**
     * This attribute is true if the SpObs is not inside an SpMSB because in
     * that case the observation is an MSB in its own right.
     */
	public static final String ATTR_MSB = ":msb" ;

	/** This attribute records whether the calibration observation is optional. */
	public static final String ATTR_OPTIONAL = ":optional" ;

	/** Attribute of the library version */
	public static final String ATTR_LIBRARY_VERSION = "library_version" ;

	/** Default library version string */
	public static final String KEYWORD_IDENTIFIER = "$" ;

	public static final String LIBRARY_VERSION = "Revision" ;

	/**
     * Default constructor. Initializes the Observation with required items.
     */
	protected SpObs()
	{
		super( SpType.OBSERVATION ) ;
		_avTable.noNotifySet( ATTR_REMAINING , "1" , 0 ) ;
		_avTable.noNotifySet( ATTR_OPTIONAL , "false" , 0 ) ;
		_avTable.noNotifySet( ATTR_PRIORITY , "99" , 0 ) ;
		_avTable.noNotifySet( ATTR_STANDARD , "false" , 0 ) ;
		_avTable.noNotifySet( ATTR_TITLE , getTitle() , 0 ) ;
	}

	/**
     * Construct an observation with the given iterator (sequence) folder.
     */
	protected SpObs( SpIterFolder ifPrototype )
	{
		this() ;
		doInsert( ifPrototype , null ) ;
	}

	/**
     * Override clone to erase the chained state.
     */
	protected Object clone()
	{
		return ( SpItem )super.clone() ;
	}


	/**
     * Get the "standard" flag of the observation.
     * 
     */
	public boolean getIsStandard()
	{
		return _avTable.getBool( ATTR_STANDARD ) ;
	}

	/**
     * Set the "standard" flag of the observation.
     * 
     */
	public void setIsStandard( boolean standard )
	{
		_avTable.set( ATTR_STANDARD , standard ) ;
	}

	/**
     * Set the library verion to the default String. This should be replaced
     * after the library is commited to CVS. It is set to the CVS keyword
     * $Revision$.
     * 
     */
	public void setLibraryRevision()
	{
		_avTable.set( ATTR_LIBRARY_VERSION , KEYWORD_IDENTIFIER + LIBRARY_VERSION + KEYWORD_IDENTIFIER ) ;
	}

	/**
     * Get the version of the library.
     * 
     * @return It will return $Revision$ if this has been checked into
     *         CVS or $Revision$ if not.
     */
	public String getLibraryRevision()
	{
		return _avTable.get( ATTR_LIBRARY_VERSION ) ;
	}

	/**
     * Override setTable to make sure that the chained states are valid.
     */
	protected void setTable( SpAvTable avTable )
	{
		super.setTable( avTable ) ;
	}

	/**
     * Get the MSB flag of the observation.
     * 
     * Added for OMP. <!-- MFO, 27 August 2001 -->
     */
	public boolean isMSB()
	{
		return _avTable.getBool( ATTR_MSB ) ;
	}

	/**
     * Set the MSB attribute of the observation.
     * 
     * Added for OMP. <!-- MFO, 27 August 2001 -->
     */
	public void updateMsbAttributes()
	{
		int editState = getAvEditFSM().getState() ;

		/*
		 * Note that _avTable.set is used instead of _avTable.noNotifySet and that
		 * the SpAvEditState which is set to EDITED as a consequence is reset
		 * immediately (if it was UNEDITED before). The is done deliberately.
		 * If noNotifySet was used instead then the title would not always be displayed/updated
		 * correctly with respect to whether or not isMSB() is true or false.
		 * (Only isMSB() == true then getNumberRemaining() is displayed in the
		 * tree in brackets after the component title.)
		 */
		
		// If the parent component is an MSB then this SpObs is not.
		if( parent() instanceof SpMSB || ( parent() instanceof SpSurveyContainer && parent().parent() instanceof SpMSB ) )
		{
			_avTable.set( ATTR_MSB , "false" ) ;

			// If this SpObs is not and MSB then it does not have a priority. Remove the priority.
			_avTable.rm( SpObs.ATTR_PRIORITY ) ;

			// If this Obs is not an MSB then remove the total time estimate
			_avTable.rm( ATTR_TOTAL_TIME ) ;
		}
		else
		{
			_avTable.set( ATTR_MSB , "true" ) ;

			// If this SpObs is an MSB then it cannot be optional.
			setOptional( false ) ;
		}
		
		// save() just means reset() in this context.
		if( editState == SpAvEditState.UNEDITED )
			getAvEditFSM().save() ;
	}

	/**
     * Indicates whether the calibration observation is optional.
     * 
     * Added for OMP (MFO, 22 October 2001)
     * 
     * @return true if calibration is optional.
     */
	protected static boolean isUKIRT = false ;

	protected static boolean cachedTelescope = false ;
	
	public boolean isOptionalForEstimates()
	{
		boolean optional = false ;
		if( !cachedTelescope )
		{
			isUKIRT = "UKIRT".equalsIgnoreCase( System.getProperty( "TELESCOPE" ) ) ;
			cachedTelescope = true ;
		}
		if( !isUKIRT )
			optional = isOptional() ;
		return optional ;
	}
	
	public boolean isOptional()
	{
		return _avTable.getBool( ATTR_OPTIONAL ) ;
	}

	/**
     * Set true if calibration observatiob is optional, false otherwise.
     * 
     * Added for OMP (MFO, 22 October 2001)
     */
	public void setOptional( boolean optional )
	{
		_avTable.set( ATTR_OPTIONAL , optional ) ;
	}

	/**
	 * dummy method for inheritance reasons
	 */
	public double getElapsedTime( boolean dummy )
	{
		return getElapsedTime() ;
	}
	
	public double getElapsedTime()
	{
		SpIterFolder iterFolder = getIterFolder() ;

		double acqTime = 0. ;
		SpInstObsComp obsComp = SpTreeMan.findInstrument( this ) ;
		// Is this is null or a standard or optional, we dont need to do anything
		if( obsComp != null && !getIsStandard() && !isOptional() )
				acqTime = obsComp.getAcqTime() ;

		if( iterFolder != null )
			return iterFolder.getElapsedTime() + acqTime ;
		else
			return 0. ;
	}

	public SpIterFolder getIterFolder()
	{
		Enumeration<SpItem> children = children() ;
		SpItem spItem = null ;

		while( children.hasMoreElements() )
		{
			spItem = children.nextElement() ;

			if( spItem instanceof SpIterFolder )
				return ( SpIterFolder )spItem ;
		}
		return null ;
	}

	public void translateProlog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translateEpilog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		v.clear() ;

		// Find the instrument, and create the name for the exec file.
		SpInstObsComp inst = SpTreeMan.findInstrument( this ) ;
		if( inst == null )
			throw new RuntimeException( "No instrument selected" ) ;
		Hashtable<String,String> defaultsTable = inst.getConfigItems() ;
		String instName = defaultsTable.get( "instrument" ) ;

		SpTelescopeObsComp obsComp = SpTreeMan.findTargetList( this ) ;
		SpTelescopePos basePos = null ;
		int spherSys = 0 ;
		int coordSys = 0 ;
		if( obsComp != null )
		{
			basePos = obsComp.getPosList().getBasePosition() ;
			spherSys = basePos.getSystemType() ;
			coordSys = basePos.getCoordSys() ;
		}

		ConfigWriter confWriter = ConfigWriter.getNewInstance() ;

		try
		{
			confWriter.write( defaultsTable ) ;
		}
		catch( IOException ioe )
		{
			System.out.println( "ERROR:Unable to write default config..." ) ;
			ioe.printStackTrace() ;
			return ;
		}

		// Set up the initials headings
		v.add( "define_inst " + instName + " " + defaultsTable.get( "instAperX" ) + " " + defaultsTable.get( "instAperY" ) + " " + defaultsTable.get( "instAperZ" ) + " " + defaultsTable.get( "instAperL" ) ) ;
		v.add( "-set_inst " + instName ) ;
		v.add( "setHeader STANDARD " + ( getIsStandard() ? "T" : "F" ) ) ;

		if( obsComp != null )
		{
			try
			{
				ConfigWriter.getCurrentInstance().writeTelFile( obsComp.writeTCSXML() ) ;
				String targetName = basePos.getName().replaceAll( "\\s" , "" ).replaceAll( "," , "" ) ;
				v.add( "telConfig " + ConfigWriter.getCurrentInstance().getTelFile() + " " + targetName ) ;
			}
			catch( IOException ioe )
			{
				System.out.println( "Unable to write TCS xml, even though a target component exists" ) ;
			}
		}

		if( instName.equals( "UFTI" ) || instName.equals( "UIST" ) || instName.equals( "CGS4" ) )
			v.add( "-SET_CHOPBEAM MIDDLE" ) ;

		if( obsComp != null )
		{
			// Add break to sequence only if instrument is not WFCAM - RDK 25 Aug 2005 //
			if( !"WFCAM".equalsIgnoreCase( instName ) )
				v.add( breakString ) ;

			if( spherSys != SpTelescopePos.SYSTEM_SPHERICAL )
			{
				v.add( "-system APP ALL" ) ;
			}
			else
			{
				switch( coordSys )
				{
					case CoordSys.FK5 :
						v.add( "-system J2000 ALL" ) ;
						break ;
					case CoordSys.FK4 :
						v.add( "-system B1950 ALL" ) ;
						break ;
					case CoordSys.AZ_EL :
						v.add( "-system AZEL ALL" ) ;
						break ;
					case CoordSys.GAL :
						v.add( "-system galactic ALL" ) ;
						break ;
					default :
						v.add( "-system J2000 ALL" ) ;
						break ;
				}
			}
			v.add( "do 1 _slew_all" ) ;
			v.add( "do 1 _slew_guide" ) ;
		}
		/*
		 * Hackily we need to do this twice since we neeed to make sure the
		 * default config is read twice, though maybe not for CGS4
         */
		v.add( "loadConfig " + confWriter.getCurrentName() ) ;
		v.add( "loadConfig " + confWriter.getCurrentName() ) ;
		if( defaultsTable.containsKey( "posAngle" ) )
		{
			v.add( "setrotator " + defaultsTable.get( "posAngle" ) ) ;
			if( "UIST".equals( instName ) )
				v.add( "setrot_offset 0.0" ) ;
		}

		if( instName.equals( "WFCAM" ) && obsComp != null )
		{
			if( obsComp.getPositionInTile() == SpTelescopeObsComp.NOT_IN_TILE )
				v.add( "noTile" ) ;
			else
				v.add( "startTile" ) ;
		}

		v.add( "startGroup" ) ;

		SpAvTable spAvTable = getTable() ;

		if( spAvTable.exists( "msbid" ) )
			v.add( "setHeader MSBID " + spAvTable.get( "msbid" ) ) ;

		if( spAvTable.exists( "project" ) )
			v.add( "setHeader PROJECT " + spAvTable.get( "project" ) ) ;

		// Add schedulable info headers in case the pipeline wants to do QA (Frossie)

		if( spAvTable.exists( "rq_minsb" ) )
			v.add( "-setHeader RQ_MINSB " + spAvTable.get( "rq_minsb" ) ) ;
		else
			v.add( "-setHeader RQ_MINSB UNDEF" ) ;

		if( spAvTable.exists( "rq_maxsb" ) )
			v.add( "-setHeader RQ_MAXSB " + spAvTable.get( "rq_maxsb" ) ) ;
		else
			v.add( "-setHeader RQ_MAXSB UNDEF" ) ;

		if( spAvTable.exists( "rq_mnsee" ) )
			v.add( "-setHeader RQ_MNSEE " + spAvTable.get( "rq_mnsee" ) ) ;
		else
			v.add( "-setHeader RQ_MNSEE UNDEF" ) ;

		if( spAvTable.exists( "rq_mxsee" ) )
			v.add( "-setHeader RQ_MXSEE " + spAvTable.get( "rq_mxsee" ) ) ;
		else
			v.add( "-setHeader RQ_MXSEE UNDEF" ) ;

		if( spAvTable.exists( "rq_mincl" ) )
			v.add( "-setHeader RQ_MINCL " + spAvTable.get( "rq_mincl" ) ) ;
		else
			v.add( "-setHeader RQ_MINCL UNDEF" ) ;

		if( spAvTable.exists( "rq_maxcl" ) )
			v.add( "-setHeader RQ_MAXCL " + spAvTable.get( "rq_maxcl" ) ) ;
		else
			v.add( "-setHeader RQ_MAXCL UNDEF" ) ;

		if( spAvTable.exists( "rq_mntau" ) )
			v.add( "-setHeader RQ_MNTAU " + spAvTable.get( "rq_mntau" ) ) ;
		else
			v.add( "-setHeader RQ_MNTAU UNDEF" ) ;

		if( spAvTable.exists( "rq_mxtau" ) )
			v.add( "-setHeader RQ_MXTAU " + spAvTable.get( "rq_mxtau" ) ) ;
		else
			v.add( "-setHeader RQ_MXTAU UNDEF" ) ;

		if( spAvTable.exists( "rq_minmn" ) )
			v.add( "-setHeader RQ_MINMN " + spAvTable.get( "rq_minmn" ) ) ;
		else
			v.add( "-setHeader RQ_MINMN UNDEF" ) ;

		if( spAvTable.exists( "rq_maxmn" ) )
			v.add( "-setHeader RQ_MAXMN " + spAvTable.get( "rq_maxmn" ) ) ;
		else
			v.add( "-setHeader RQ_MAXMN UNDEF" ) ;

		// eStar headers

		if( isMSB() )
		{
			if( spAvTable.exists( "remote_trigger_src" ) )
				v.add( "-setHeader RMTAGENT " + spAvTable.get( "remote_trigger_src" ) ) ;
			else
				v.add( "-setHeader RMTAGENT UNDEF" ) ;

			if( spAvTable.exists( "remote_trigger_id" ) )
				v.add( "-setHeader AGENTID " + spAvTable.get( "remote_trigger_id" ) ) ;
			else
				v.add( "-setHeader AGENTID UNDEF" ) ;
		}
		else
		{
			if( parent().getTable().exists( "remote_trigger_src" ) )
				v.add( "-setHeader RMTAGENT " + parent().getTable().get( "remote_trigger_src" ) ) ;
			else
				v.add( "-setHeader RMTAGENT UNDEF" ) ;

			if( parent().getTable().exists( "remote_trigger_id" ) )
				v.add( "-setHeader AGENTID " + parent().getTable().get( "remote_trigger_id" ) ) ;
			else
				v.add( "-setHeader AGENTID UNDEF" ) ;
		}

		try
		{
			Enumeration<SpItem> e = this.children() ;
			TranslationUtils.recurse( e , v ) ;
		}
		catch( SpTranslationNotSupportedException e )
		{
			e.printStackTrace() ;
		}

		if( instName.equals( "UFTI" ) || ( instName.equals( "UIST" ) && "imaging".equals( defaultsTable.get( "camera" ) ) ) )
		{
			v.add( "breakPoint" ) ;
			v.add( darkString ) ;
		}

		v.add( "-ready" ) ;

		// Add breaks to sequence only if instrument is not WFCAM - RDK 25 Aug 2005 //
		if( !"WFCAM".equalsIgnoreCase( instName ) )
			addBreak( v ) ;

		// A couple of final tidy up operations
		tidyNOffsets( v , inst ) ;
		tidyInstDefns( v ) ;
		tidyDuplicates( v ) ;
		if( "WFCAM".equalsIgnoreCase( instName ) )
		{
			addGuideCommands( v ) ;
			correctOrder( v ) ;
		}

		if( !testForRecipes( v ) )
		{
			logger.error( "DR Recipes are missing from this observation." ) ;
		}

		try
		{
			FileWriter fw = new FileWriter( confWriter.getExecName() ) ;
			for( String txt : v )
				fw.write( txt + "\n" ) ;
			fw.flush() ;
			fw.close() ;
		}
		catch( IOException ioe )
		{
			ioe.printStackTrace() ;
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
		}
	}

	private boolean testForRecipes( Vector<String> v )
	{
		boolean found = false ;
		for( int i = 1 ; i < v.size() ; i++ )
		{
			if( v.get( i ).startsWith( "setHeader GRPMEM " ) && ( i + 1 != v.size() && v.get( i + 1 ).startsWith( "setHeader RECIPE " ) ) )
			{
				found = true ;
				break ;
			}
		}
		return found ;
	}

	private void correctOrder( Vector<String> v )
	{
		int size = v.size() ;
		int setObjectIndex = size ;
		String currentString ;
		int lookForSet = 0 ;
		for( int searchIndex = 0 ; searchIndex < sets.length ; searchIndex++ )
		{
			currentString = sets[ searchIndex ] ;
			lookForSet = v.indexOf( currentString ) ;
			if( lookForSet == -1 )
				continue ;
			if( lookForSet < setObjectIndex )
				setObjectIndex = lookForSet ;
		}
		if( setObjectIndex == size )
			return ;
		boolean seenFirstOffset = false ;
		int firstOffsetIndex = 0 ;
		boolean seenFirstGuideOn = false ;
		int firstGuideOnIndex = 0 ;

		for( int index = 0 ; index < size ; index++ )
		{
			currentString = v.get( index ) ;

			if( currentString.endsWith( "_guide_on" ) )
			{
				if( !seenFirstGuideOn )
				{
					firstGuideOnIndex = index ;
					seenFirstGuideOn = true ;
				}
				if( seenFirstOffset )
					break ;
				continue ;
			}
			if( currentString.startsWith( "offset" ) )
			{
				if( !seenFirstOffset )
				{
					firstOffsetIndex = index ;
					seenFirstOffset = true ;
				}
				if( seenFirstGuideOn )
					break ;
				continue ;
			}
		} // close of for loop

		if( !seenFirstOffset )
			return ; // nothing to do

		boolean objectBeforeGuide = setObjectIndex < firstGuideOnIndex ;
		boolean guideBeforeOffset = firstGuideOnIndex < firstOffsetIndex ;
		boolean objectBeforeOffset = setObjectIndex < firstOffsetIndex ;

		if( objectBeforeGuide && guideBeforeOffset )
			return ; // nothing to do

		// the following should be false due to addGuideCommands()
		if( !objectBeforeGuide )
		{
			// we know this time round that it is a string
			currentString = v.get( firstGuideOnIndex ) ;
			v.insertElementAt( currentString , setObjectIndex + 1 ) ;
			v.remove( firstGuideOnIndex ) ;
		}

		if( v.size() != size )
			throw new RuntimeException( "Size mismatch error" ) ;

		if( !objectBeforeOffset )
		{
			// we know this time round that it is a string
			currentString = v.get( firstOffsetIndex ) ;
			v.insertElementAt( currentString , firstGuideOnIndex + 1 ) ;
			v.remove( firstOffsetIndex ) ;
		}

		if( v.size() != size )
			throw new RuntimeException( "Size mismatch error" ) ;
	}

	private void tidyNOffsets( Vector<String> v , SpInstObsComp inst )
	{
		int nOffsets = 0 ;
		// Get all the child offsets
		Vector<SpItem> offsets = SpTreeMan.findAllItems( this , SpIterOffset.class.getName() ) ;
		int observableOffsets = 0 ;
		
		for( SpItem offsetItem : offsets )
		{
			SpIterOffset offset = ( SpIterOffset )offsetItem ;
			int myNOffs = offset.getPosList().size() ;
			
			/*
			 *  The following records the number of observable offsets
			 *  Empty offset positions are used as hacks, but corrupt the headers
			 */
			int observableChildren = offset.getNumIterObserveChildren( offset ) ;
			if( observableChildren > 0 )
				observableOffsets += myNOffs ;
			
			if( offset.hasNamedSkyChild() )
				myNOffs *= observableChildren ;
			Vector<SpItem> uSteps = SpTreeMan.findAllItems( offset , SpIterMicroStep.class.getName() ) ;
			if( uSteps != null && uSteps.size() != 0 && inst instanceof SpMicroStepUser )
			{
				SpIterMicroStep us = ( SpIterMicroStep )uSteps.get( 0 ) ;
				myNOffs *= us.getNOffsets() ;
			}
			nOffsets += myNOffs ;
		}
		
		complainAboutHack( nOffsets , observableOffsets ) ;

		/*
		 * Now go through adding to nOffsets for eacg ADDOFFEST instruction.
		 * These are added in the case wherer an SpIterObserveBase is not inside an offset.
         * We only need to add them to the first breakpoint
		 */
		boolean atBreakPoint = false ;
		for( int i = 0 ; i < v.size() ; i++ )
		{
			if( v.get( i ).equals( "breakPoint" ) )
				atBreakPoint = true ;

			if( v.get( i ).equals( "ADDOFFSET" ) )
			{
				if( !atBreakPoint )
					nOffsets++ ;
				v.remove( i ) ;
				// rewind so we don't miss the breakpoint
				i-- ;
			}
		}

		// Now add to the sequence after the startGroup
		for( int i = 0 ; i < v.size() ; i++ )
		{
			if( v.get( i ).equalsIgnoreCase( "startGroup" ) )
			{
				v.add( i + 1 , "-setHeader NOFFSETS " + nOffsets ) ;
				break ;
			}
		}
	}
	
	private void complainAboutHack( int offsetsInSequence , int observableOffsets )
	{
		if( observableOffsets != 0 && offsetsInSequence > observableOffsets )
		{
			logger.warn( "Only " + observableOffsets + " of the " + offsetsInSequence + " offsets are observed." ) ;
			logger.warn( "Please fix the DR's NOFFSETS+1 hack." ) ;
			if( offsetsInSequence > observableOffsets + 1 )
			{
				logger.error( "More offsets in the sequence than the DR expects." ) ;
				logger.error( "The NOFFSET header will most likely be incorrect." ) ;
			}
		}
	}

	private void tidyDuplicates( Vector<String> v )
	{
		// Remove redundant loadConfigs, offsets, set commands or any case where two sequential lines are identical
		String lastLoadConfig = "" ;
		String lastOffset = "" ;
		for( int i = 1 ; i < v.size() ; i++ )
		{
			if( v.get( i ).equals( v.get( i - 1 ) ) )
			{
				v.remove( i - 1 ) ;
				i-- ;
			}
		}

		for( int i = 1 ; i < v.size() ; i++ )
		{
			if( v.get( i ).startsWith( "loadConfig" ) )
			{
				if( lastLoadConfig.equals( v.get( i ) ) )
				{
					v.remove( i ) ;
					i-- ;
				}
				else if( v.get( i + 1 ).startsWith( "loadConfig" ) )
				{
					// This can happen as we move the default loadConfig down but it is never used
					v.remove( i ) ;
					lastLoadConfig = v.get( i ) ;
				}
				else
				{
					lastLoadConfig = v.get( i ) ;
				}
			}
		}

		for( int i = 1 ; i < v.size() ; i++ )
		{
			if( v.get( i ).startsWith( "offset" ) )
			{
				if( lastOffset.equals( v.get( i ) ) )
				{
					v.remove( i ) ;
					i-- ;
				}
				else
				{
					lastOffset = v.get( i ) ;
				}
			}
		}

		removeExcessGRPMEM( v ) ;

		removeExcessSets( v ) ;
	}

	private void removeExcessGRPMEM( Vector<String> v )
	{
		String lastGRPMEM = "" ;
		String lastDRRECIPE = "" ;

		for( int i = 1 ; i < v.size() ; i++ )
		{
			if( v.get( i ).startsWith( "setHeader GRPMEM " ) )
			{
				String nextGrpMem = v.get( i ) ;
				String nextRecipe = v.get( i + 1 ) ;
				if( nextRecipe.equals( lastDRRECIPE ) && nextGrpMem.equals( lastGRPMEM ) )
				{
					// Remove the two entries
					v.remove( i + 1 ) ;
					v.remove( i ) ;
					i-- ;
				}
				else
				{
					lastGRPMEM = nextGrpMem ;
					lastDRRECIPE = nextRecipe ;
				}
			}
		}
	}
	
	/**
	 * For simplicity do another pass to remove redundant set OBJECT, 
	 * SKY or SKYFLAT commands, since these are time consuming.
	 */
	private void removeExcessSets( Vector<String> v )
	{
		String current = null ;
		String lastSeen = null ;
		
		for( int i = 0 ; i < v.size() ; i++ )
		{
			current = v.get( i ) ;
			
			if( current.startsWith( "loadConfig" ) )
			{
				lastSeen = null ;
			}
			else if( current.startsWith( "set " ) )
			{
				if( objectString.equals( current ) || skyflatString.equals( current ) || skyString.equals( current ) )
				{
					if( !current.equals( lastSeen ) )
					{
						lastSeen = current ;
						continue ;
					}
					else
					{
						v.remove( i ) ;
					}
				}
				else
				{
					lastSeen = null ;
				}
				
			}
		}
	}

	private void tidyInstDefns( Vector<String> v )
	{
		// To make this robust, rather than just comparing strings, we will compare the numerical value of the offsets
		double xAper = 0. ;
		double yAper = 0. ;
		double zAper = 0. ;
		double lAper = 0. ;
		for( int i = 0 ; i < v.size() ; i++ )
		{
			if( v.get( i ).startsWith( "define_inst" ) )
			{
				String[] apers = v.get( i ).split( "\\s+" ) ;
				double thisX = Double.parseDouble( apers[ 2 ] ) ;
				double thisY = Double.parseDouble( apers[ 3 ] ) ;
				double thisZ = Double.parseDouble( apers[ 4 ] ) ;
				double thisL = Double.parseDouble( apers[ 5 ] ) ;

				if( ( thisX == xAper ) && ( thisY == yAper ) && ( thisZ == zAper ) && ( thisL == lAper ) )
				{
					v.remove( i ) ;
				}
				else
				{
					xAper = thisX ;
					yAper = thisY ;
					zAper = thisZ ;
					lAper = thisL ;
				}
			}
		}
	}

	private void addBreak( Vector<String> v )
	{
		int objectIndex = v.indexOf( objectString ) ;
		int skyIndex = v.indexOf( skyString ) ;
		boolean offsetFound = false ;
		int offsetIndex ;
		for( offsetIndex = 0 ; offsetIndex < v.size() ; offsetIndex++ )
		{
			if( v.get( offsetIndex ).startsWith( "offset" ) )
			{
				offsetFound = true ;
				break ;
			}
		}

		if( objectIndex == -1 && skyIndex == -1 )
			return ;

		// Find the default loadConfig
		String defaultConfigPattern = "loadConfig .*_1" ;
		String defaultConfig = "" ;
		for( int i = 0 ; i < v.size() ; i++ )
		{
			defaultConfig = v.get( i ) ;
			if( defaultConfig.matches( defaultConfigPattern ) )
				break ;
		}

		if( !offsetFound )
		{
			// If there are no SKYs, make sure we have a break after 1st "set OBEJCT"
			if( skyIndex == -1 && !( breakString.equals( v.get( objectIndex + 1 ) ) ) )
			{
				v.add( objectIndex + 1 , breakString ) ;
			}
			else
			{
				// If object index < sky index, make sure we have a break after 1st "set OBEJCT"
				if( objectIndex < skyIndex && !( breakString.equals( v.get( objectIndex + 1 ) ) ) )
				{
					v.add( objectIndex + 1 , breakString ) ;
				}
				else
				{
					// Insert a "set OBJECT/break" before the previous slew
                    // command - if any
					for( int i = skyIndex ; i >= 0 ; i-- )
					{
						if( v.get( i ).startsWith( "slew MAIN" ) || v.get( i ).startsWith( "offset" ) )
						{
							v.add( i - 1 , breakString ) ;
							v.add( i - 1 , objectString ) ;
							break ;
						}
					}
				}
			}
		}
		else
		{ // We have an offset iterator present
			v.add( offsetIndex , breakString ) ;
			v.add( offsetIndex , objectString ) ;
		}

		// Now go back from the first set OBJECT to work out if we need to load
        // a default config
		objectIndex = v.indexOf( objectString ) ;
		String observePattern = "do \\d+ _observe" ;
		for( int i = objectIndex ; i >= 0 ; i-- )
		{
			if( v.get( i ).startsWith( "loadConfig" ) )
			{
				// No need to do anything
				break ;
			}
			else if( v.get( i ).matches( observePattern ) )
			{
				// We need to put the default config before the set OBJECT
				v.add( objectIndex , defaultConfig ) ;
				break ;
			}
		}
	}

	private void addGuideCommands( Vector<String> v )
	{
		for( int i = 0 ; i < v.size() ; i++ )
		{
			String s = v.get( i ) ;
			if( s.equals( darkString ) || s.equals( biasString ) || s.equals( focusString ) || s.equals( domeString ) )
			{
				// Add a call to the _guide_off macro before this command
				v.add( i++ , "do 1 _guide_off" ) ;
			}
			else if( s.equals( objectString ) || s.equals( skyString ) || s.equals( skyflatString ) )
			{
				// Add a call to the _guide_off macro before and a _guide_on after
				v.add( i + 1 , "do 1 _guide_on" ) ;
				v.add( i++ , "do 1 _guide_off" ) ;
			}
		}
	}
}
