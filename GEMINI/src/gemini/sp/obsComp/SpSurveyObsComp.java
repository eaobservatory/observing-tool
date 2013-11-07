// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.

/*
 * Copyright 2003 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 */

package gemini.sp.obsComp ;

import gemini.sp.SpItem ;
import gemini.sp.SpTelescopePos ;
import gemini.sp.SpType ;
import gemini.sp.SpFactory ;
import gemini.util.CoordSys ;
import java.util.Arrays ;
import java.util.Vector ;
import java.util.StringTokenizer ;
import java.io.Reader ;
import java.io.LineNumberReader ;
import java.io.FileReader ;

/**
 * A class for telescope observation component items. Maintains a position list
 * and keeps up-to-date the base position element of the observation data for
 * the observation context.
 * 
 * @see gemini.sp.SpTelescopePosList
 * @see gemini.sp.SpObsData
 * @see gemini.sp.SpObsContextItem
 */
@SuppressWarnings( "serial" )
public class SpSurveyObsComp extends SpObsComp
{
	public static final String ATTR_REMAINING = "remaining" ;

	public static final String ATTR_PRIORITY = "priority" ;

	public static final String ATTR_SELECTED_TEL_OBS_COMP = ".gui.selectedTelObsComp" ;

	public static final String ATTR_SURVEY_ID = "surveyID" ;

	private Vector<SpTelescopeObsComp> _telescopeObsCompVector = new Vector<SpTelescopeObsComp>() ;

	/** Used in {@link #processAvAttribute(String,String,StringBuffer)}. */
	private Vector<String> _tagVector = null ;

	private String _telObsCompXmlElementName = ( new SpTelescopeObsComp() ).getXmlElementName() ;

	private SpTelescopeObsComp _processingTelObsComp = null ;

	public static final String SUBTYPE = "surveyComp" ;

	public static final SpType SP_TYPE = SpType.create( SpType.OBSERVATION_COMPONENT_TYPE , SUBTYPE , "Survey" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpSurveyObsComp() ) ;
	}

	public SpSurveyObsComp()
	{
		super( SP_TYPE ) ;

		_tagVector = new Vector<String>( Arrays.asList( SpTelescopePos.getGuideStarTags() ) ) ;
		_tagVector.add( SpTelescopePos.getBaseTag() ) ;
	}

	public void initTelescopeObsCompVector()
	{
		_telescopeObsCompVector = new Vector<SpTelescopeObsComp>() ;
	}

	public void initTelescopeObsCompVector( Vector<SpTelescopeObsComp> telescopeObsCompVector )
	{
		_telescopeObsCompVector = new Vector<SpTelescopeObsComp>( telescopeObsCompVector ) ;
	}

	/**
     * Get the remaining count for the SpTelescopeObsComp at the specified
     * index.
     */
	public int getRemaining( int telObsCompIndex )
	{
		return _avTable.getInt( ATTR_REMAINING , telObsCompIndex , 0 ) ;
	}

	/**
     * Set the remaining count for the SpTelescopeObsComp at the specified
     * index.
     */
	public void setRemaining( int remaining , int telObsCompIndex )
	{
		_avTable.set( ATTR_REMAINING , remaining , telObsCompIndex ) ;
	}

	/**
     * Get the priority for the SpTelescopeObsComp at the specified index.
     */
	public int getPriority( int telObsCompIndex )
	{
		return _avTable.getInt( ATTR_PRIORITY , telObsCompIndex , 0 ) ;
	}

	/**
     * Set the priority for the SpTelescopeObsComp at the specified index.
     */
	public void setPriority( int priority , int telObsCompIndex )
	{
		_avTable.set( ATTR_PRIORITY , priority , telObsCompIndex ) ;
	}

	/**
     * Get the priority for the SpTelescopeObsComp at the specified index.
     */
	public String getSurveyID()
	{
		return _avTable.get( ATTR_SURVEY_ID ) ;
	}

	/**
     * Set the priority for the SpTelescopeObsComp at the specified index.
     */
	public void setSurveyID( String surveyID )
	{
		_avTable.set( ATTR_SURVEY_ID , surveyID ) ;
	}

	/**
     * Get the index of the selected SpTelescopeObsComp.
     */
	public int getSelectedTelObsComp()
	{
		return _avTable.getInt( ATTR_SELECTED_TEL_OBS_COMP , 0 ) ;
	}

	/**
     * Set the index of the selected SpTelescopeObsComp without notifying the
     * state machine.
     */
	public void noNotifySetSelectedTelObsComp( int index )
	{
		_avTable.noNotifySet( ATTR_SELECTED_TEL_OBS_COMP , "" + index , 0 ) ;
	}

	/**
     * Set the index of the selected SpTelescopeObsComp.
     */
	public void setSelectedTelObsComp( int index )
	{
		_avTable.set( ATTR_SELECTED_TEL_OBS_COMP , index ) ;
	}

	public SpTelescopeObsComp getSpTelescopeObsComp( int index )
	{
		return _telescopeObsCompVector.get( index ) ;
	}

	public int size()
	{
		return _telescopeObsCompVector.size() ;
	}

	public SpTelescopeObsComp noNotifyAddSpTelescopeObsComp()
	{
		SpTelescopeObsComp spTelescopeObsComp = new SpTelescopeObsComp() ;

		if( !_telescopeObsCompVector.contains( spTelescopeObsComp ) )
		{
			_telescopeObsCompVector.add( spTelescopeObsComp ) ;
			spTelescopeObsComp.setEditFSM( getEditFSM() ) ;
		}

		return spTelescopeObsComp ;
	}

	public SpTelescopeObsComp addSpTelescopeObsComp()
	{
		SpTelescopeObsComp spTelescopeObsComp = noNotifyAddSpTelescopeObsComp() ;
		spTelescopeObsComp.getTable().edit() ;
		return spTelescopeObsComp ;
	}

	public void addSpTelescopeObsComp( SpTelescopeObsComp spTelescopeObsComp )
	{
		if( !_telescopeObsCompVector.contains( spTelescopeObsComp ) )
		{
			_telescopeObsCompVector.add( spTelescopeObsComp ) ;
			spTelescopeObsComp.setEditFSM( getEditFSM() ) ;
			spTelescopeObsComp.getTable().edit() ;
		}
	}

	public void removeSpTelescopeObsComp( SpTelescopeObsComp spTelescopeObsComp )
	{
		if( _telescopeObsCompVector.contains( spTelescopeObsComp ) )
			_telescopeObsCompVector.remove( spTelescopeObsComp ) ;

		// Make sure that there is at least one SpTelescopeObsComp in the survey component.
		if( size() < 1 )
			addSpTelescopeObsComp() ;
		else
			_telescopeObsCompVector.get( 0 ).getTable().edit() ;
	}

	public void removeSpTelescopeObsComp( int index )
	{
		_telescopeObsCompVector.remove( index ) ;

		// Make sure that there is at least one SpTelescopeObsComp in the survey component.
		if( size() < 1 )
			addSpTelescopeObsComp() ;
		else
			_telescopeObsCompVector.get( 0 ).getTable().edit() ;
	}

	public void removeAllSpTelescopeObsComponents()
	{
		_telescopeObsCompVector.removeAllElements() ;

		// Make sure that there is at least one SpTelescopeObsComp in the survey component.
		addSpTelescopeObsComp() ;
	}

	public void load( String fileName ) throws Exception
	{
		load( new FileReader( fileName ) ) ;
	}

	public void load( Reader reader ) throws Exception
	{
		LineNumberReader lineNumberReader = new LineNumberReader( reader ) ;
		String line = null ;
		StringTokenizer stringTokenizer = null ;

		String tag = null ;
		String name = "" ;
		String x = "0:00:00" ;
		String y = "0:00:00" ;
		String coordSystem = "" ;
		String tileString = null ;
		int coordSystemIndex = CoordSys.FK5 ;

		SpTelescopeObsComp spTelescopeObsComp = null ;

		do
		{
			line = lineNumberReader.readLine() ;

			if( line != null )
			{
				if( line.toUpperCase().startsWith( "SURVEY_ID" ) || line.toUpperCase().startsWith( "SURVEYID" ) )
				{
					if( ( line.indexOf( ':' ) == -1 ) && ( line.indexOf( '=' ) != -1 ) )
					{
						setSurveyID( line.substring( line.indexOf( '=' ) + 1 ).trim() ) ;
						continue ;
					}

					if( ( line.indexOf( '=' ) == -1 ) && ( line.indexOf( ':' ) != -1 ) )
					{
						setSurveyID( line.substring( line.indexOf( ':' ) + 1 ).trim() ) ;
						continue ;
					}

					System.out.println( "Could not parse survey ID: \"" + line + "\". Format should be: SURVEY_ID = <survey id string>." ) ;
					continue ;
				}

				stringTokenizer = new StringTokenizer( line , ", ;" ) ;

				if( stringTokenizer.hasMoreTokens() )
					tag = stringTokenizer.nextToken().trim() ;

				if( stringTokenizer.hasMoreTokens() )
					name = stringTokenizer.nextToken().trim() ;

				if( stringTokenizer.hasMoreTokens() )
					x = stringTokenizer.nextToken().trim() ;

				if( stringTokenizer.hasMoreTokens() )
					y = stringTokenizer.nextToken().trim() ;

				if( stringTokenizer.hasMoreTokens() )
					coordSystem = stringTokenizer.nextToken().trim() ;

				// Modify coordinate system if required
				if( ( coordSystem.toUpperCase().indexOf( "FK5" ) > -1 ) || ( coordSystem.toUpperCase().indexOf( "J2000" ) > -1 ) )
					coordSystemIndex = CoordSys.FK5 ;

				if( ( coordSystem.toUpperCase().indexOf( "FK4" ) > -1 ) || ( coordSystem.toUpperCase().indexOf( "B1950" ) > -1 ) )
					coordSystemIndex = CoordSys.FK4 ;

				if( ( coordSystem.toUpperCase().indexOf( "GAL" ) > -1 ) )
					coordSystemIndex = CoordSys.GAL ;

				if( stringTokenizer.hasMoreTokens() )
					tileString = stringTokenizer.nextToken().trim() ;

				if( tag.equalsIgnoreCase( SpTelescopePos.BASE_TAG ) )
				{
					if( spTelescopeObsComp != null )
						addSpTelescopeObsComp( spTelescopeObsComp ) ;

					spTelescopeObsComp = new SpTelescopeObsComp() ;

					spTelescopeObsComp.getPosList().getBasePosition().setName( getSurveyID() + ":" + name ) ;
					spTelescopeObsComp.getPosList().getBasePosition().setXYFromString( x , y ) ;
					// Use the standardized CoordSys.getCoordSys(coordSystemIndex) instead of coordSystem.
					spTelescopeObsComp.getPosList().getBasePosition().setCoordSys( CoordSys.getSystem( coordSystemIndex ) ) ;

					if( tileString != null )
					{
						int positionInTile = 0 ;

						try
						{
							positionInTile = Integer.parseInt( tileString ) ;
						}
						catch( Exception e )
						{
							System.err.println( "SpSurveyObsComp.load(): Could not parse position in tile from \"" + tileString + "\". Assuming first position in tile." ) ;
						}

						spTelescopeObsComp.setPositionInTile( positionInTile ) ;

						spTelescopeObsComp.setFitsKey( "SURVEY" , 0 ) ;
						spTelescopeObsComp.setFitsValue( getSurveyID() , 0 ) ;

						spTelescopeObsComp.setFitsKey( "SURVEY_I" , 1 ) ;
						spTelescopeObsComp.setFitsValue( name , 1 ) ;
					}
				}
				else
				{
					if( spTelescopeObsComp != null )
					{
						try
						{
							spTelescopeObsComp.getPosList().createPosition( tag , x , y , coordSystemIndex ).setName( name ) ;
						}
						catch( Exception e )
						{
							e.printStackTrace() ;
							throw new Exception( "Could not create telescope position:" + "\n  tag = " + tag + "\n  coordinate 1 = " + x + "\n  coordinate 2 = " + y + "\n  coordinate system = " + CoordSys.getSystem( coordSystemIndex ) ) ;
						}
					}
					else
					{
						if( line.trim().equals( "" ) || resemblesSurveyAreaDefinition( line ) )
							System.out.println( "Ignoring \"" + line + "\" in pointing file (Might be part of an SDT file)." ) ;
						else
							throw new Exception( "Could not parse list of survey targets.\n" + "Make sure the list starts with the " + SpTelescopePos.BASE_TAG + " tag." ) ;
					}
				}
			}
		}
		while( line != null ) ;

		// Add the remaining field
		if( spTelescopeObsComp != null )
			addSpTelescopeObsComp( spTelescopeObsComp ) ;
	}

	public SpItem deepCopy()
	{
		SpItem copy = super.deepCopy() ;
		( ( SpSurveyObsComp )copy ).initTelescopeObsCompVector( _telescopeObsCompVector ) ;
		return copy ;
	}

	public SpItem shallowCopy()
	{
		SpItem copy = super.shallowCopy() ;
		( ( SpSurveyObsComp )copy ).initTelescopeObsCompVector( _telescopeObsCompVector ) ;
		return copy ;
	}

	/**
     * Crude check as to whether a line in an ASCII file looks like a survey
     * area definition (as opposed to a pointing definition) as used in a Survey
     * Definition Tool (SDT) file.
     * 
     * The pointing definition format is the same in SDT files and in the ASCII
     * format read by the load method of this SpSurveyObsComp class. By ignoring
     * survey area definition a empty lines the load method can be used to
     * directly read SDT files. Note that this is not the normal way of
     * importing pointings into the OT. Normally the SDT exports its pointings
     * to OT Science Progammes in which the pointings are spread over several
     * SpSurveyObsComps, each in a separate MSB.
     */
	public static boolean resemblesSurveyAreaDefinition( String line )
	{
		if( ( line == null ) || ( line.trim().equals( "" ) ) )
			return false ;

		StringTokenizer tokenizer = new StringTokenizer( line , " :," ) ;

		while( tokenizer.hasMoreTokens() )
		{
			try
			{
				Double.parseDouble( tokenizer.nextToken() ) ;
			}
			catch( Exception e )
			{
				return !tokenizer.hasMoreTokens() ;
			}
		}

		return true ;
	}

	public void processXmlElementStart( String name )
	{
		if( name.equals( "FIELD" ) )
		{
			_processingTelObsComp = noNotifyAddSpTelescopeObsComp() ;
			_processingTelObsComp.processXmlElementStart( _telObsCompXmlElementName ) ;
		}
		else
		{
			super.processXmlElementStart( name ) ;
		}
	}

	public void processXmlAttribute( String elementName , String attributeName , String value )
	{
		if( _processingTelObsComp != null )
			_processingTelObsComp.processXmlAttribute( elementName , attributeName , value ) ;
		else
			super.processXmlAttribute( elementName , attributeName , value ) ;
	}

	public void processXmlElementContent( String name , String value )
	{
		if( _processingTelObsComp != null )
		{
			if( name.equals( "FIELD" ) )
				_processingTelObsComp.processXmlElementContent( _telObsCompXmlElementName , value ) ;
			else
				_processingTelObsComp.processXmlElementContent( name , value ) ;
		}
		else
		{
			super.processXmlElementContent( name , value ) ;
		}
	}

	public void processXmlElementContent( String name , String value , int pos )
	{
		if( _processingTelObsComp != null )
		{
			if( name.equals( "FIELD" ) )
				_processingTelObsComp.processXmlElementContent( _telObsCompXmlElementName , value , pos ) ;
			else
				_processingTelObsComp.processXmlElementContent( name , value , pos ) ;
		}
		else
		{
			super.processXmlElementContent( name , value , pos ) ;
		}
	}

	public void processXmlElementEnd( String name )
	{
		if( name.equals( "FIELD" ) )
		{
			_processingTelObsComp.processXmlElementEnd( _telObsCompXmlElementName ) ;
			_processingTelObsComp = null ;
		}
		else
		{
			super.processXmlElementEnd( name ) ;
		}
	}

	/**
     * Ignores SpTelescopeObsComp attributes.
     * 
     * Since the SpAvTable of this SpSurveyObsComp is used by which ever
     * SpTelescopeObsComp is currently being edited there will still be
     * attributes in the table that belong to a SpTelescopeObsComp. But they
     * should be ignored here. All the SpTelescopeObsComp information is
     * processed in the method toXML() ;
     */
	protected void processAvAttribute( String avAttr , String indent , StringBuffer xmlBuffer )
	{
		if( !_tagVector.contains( avAttr ) )
			super.processAvAttribute( avAttr , indent , xmlBuffer ) ;
	}

	protected void toXML( String indent , StringBuffer xmlBuffer )
	{
		super.toXML( indent , xmlBuffer ) ;

		// Remove all ATTR_SELECTED_TEL_OBS_COMP attributes form the SpTelescopeObsComp tables.
		// They ended up there because SpSurveyObsComp and SpTelescopeObsComp share a
		// SpAvTable when a SpTelescopeObsComp is edited.
		// First replace the table of this SpSurveyObsComp with a copy of itself so prevent
		// the ATTR_SELECTED_TEL_OBS_COMP attribute from being removed from the table of the SpSurveyObsComp.

		int offset = xmlBuffer.toString().lastIndexOf( '<' ) - indent.length() - 1 ;

		String closeTag = xmlBuffer.toString().substring( offset ) ;
		xmlBuffer.delete( offset , xmlBuffer.length() ) ;

		String telescopeObsCompXML = null ;

		for( int i = 0 ; i < _telescopeObsCompVector.size() ; i++ )
		{
			telescopeObsCompXML = _telescopeObsCompVector.get( i ).getXML( "  " + indent ) ;

			// Get rid of enclosing tags.
			telescopeObsCompXML = telescopeObsCompXML.substring( telescopeObsCompXML.indexOf( "\n" , telescopeObsCompXML.indexOf( "\n" ) + 1 ) , telescopeObsCompXML.lastIndexOf( "\n" ) ) ;

			xmlBuffer.append( "\n" + indent + "  <FIELD>" ) ;
			xmlBuffer.append( telescopeObsCompXML ) ;
			xmlBuffer.append( "\n" + indent + "  </FIELD>" ) ;
		}

		xmlBuffer.append( "\n" + indent + closeTag ) ;
	}
}
