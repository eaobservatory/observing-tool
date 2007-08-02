// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
/* ============================================================== */
/*                                                              */
/* UK Astronomy Technology Centre */
/* Royal Observatory, Edinburgh */
/* Joint Astronomy Centre, Hilo */
/* Copyright (c) PPARC 2003 */
/*                                                              */
/* ============================================================== */
// $Id$
package gemini.sp;

import gemini.util.CoordSys;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.obsComp.SpInstObsComp;
import java.util.Arrays;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.io.Reader;
import java.io.LineNumberReader;
import java.io.FileReader;

/**
 * A class for telescope observation component items. Maintains a position list
 * and keeps up-to-date the base position element of the observation data for
 * the observation context.
 * 
 * @see gemini.sp.SpTelescopePosList
 * @see gemini.sp.SpObsData
 * @see gemini.sp.SpObsContextItem
 */
public class SpSurveyContainer extends SpObsContextItem
{

	public static final String ATTR_REMAINING = "remaining";
	public static final String ATTR_PRIORITY = "priority";
	public static final String ATTR_CHOICE = "choose";
	public static final String ATTR_SELECTED_TEL_OBS_COMP = ".gui.selectedTelObsComp";
	public static final String ATTR_SURVEY_ID = "surveyID";
	private Vector _telescopeObsCompVector = new Vector();

	/** Used in {@link #processAvAttribute(String,String,StringBuffer)}. */
	private Vector _tagVector = null;
	private String _telObsCompXmlElementName = ( new SpTelescopeObsComp() ).getXmlElementName();
	private SpTelescopeObsComp _processingTelObsComp = null;

	public SpSurveyContainer()
	{
		super( SpType.SURVEY_CONTAINER );

		_tagVector = new Vector( Arrays.asList( SpTelescopePos.getGuideStarTags() ) );
		_tagVector.add( SpTelescopePos.getBaseTag() );
	}

	public void initTelescopeObsCompVector()
	{
		_telescopeObsCompVector = new Vector();
	}

	public void initTelescopeObsCompVector( Vector telescopeObsCompVector )
	{
		Vector _telescopeObsCompVectorClone = new Vector();
		for( int index = 0 ; index < telescopeObsCompVector.size() ; index++ )
		{
			Object obj = telescopeObsCompVector.get( index );
			if( obj instanceof SpTelescopeObsComp )
			{
				SpTelescopeObsComp temp = ( SpTelescopeObsComp )obj;
				SpTelescopeObsComp clone = ( SpTelescopeObsComp )temp.clone();
				clone.setSurveyComponent( this );
				_telescopeObsCompVectorClone.insertElementAt( clone , index );
			}
			else
			{
				_telescopeObsCompVectorClone.insertElementAt( obj , index );
			}
		}
		_telescopeObsCompVector = _telescopeObsCompVectorClone;
	}

	/**
     * Get the remaining count for the SpTelescopeObsComp at the specified
     * index.
     */
	public int getRemaining( int telObsCompIndex )
	{
		return _avTable.getInt( ATTR_REMAINING , telObsCompIndex , 0 );
	}

	/**
     * Set the remaining count for the SpTelescopeObsComp at the specified
     * index.
     */
	public void setRemaining( int remaining , int telObsCompIndex )
	{
		_avTable.set( ATTR_REMAINING , remaining , telObsCompIndex );
	}

	/**
     * Remove the remaining count for the SpTelescopeObsComp at the specified
     * index.
     */
	public void removeRemaining( int index )
	{
		_avTable.rm( ATTR_REMAINING , index );
	}

	/**
     * Get the priority for the SpTelescopeObsComp at the specified index.
     */
	public int getPriority( int telObsCompIndex )
	{
		return _avTable.getInt( ATTR_PRIORITY , telObsCompIndex , 0 );
	}

	/**
     * Set the priority for the SpTelescopeObsComp at the specified index.
     */
	public void setPriority( int priority , int telObsCompIndex )
	{
		_avTable.set( ATTR_PRIORITY , priority , telObsCompIndex );
	}

	/**
     * Remove the priority for the SpTelescopeObsComp at the specified index.
     */
	public void removePriority( int index )
	{
		_avTable.rm( ATTR_PRIORITY , index );
	}

	public boolean isChoice()
	{
		return _avTable.exists( ATTR_CHOICE );
	}

	public void setChoose( int value )
	{
		if( value != 0 )
			_avTable.set( ATTR_CHOICE , value );
		else
			_avTable.rm( ATTR_CHOICE );
	}

	public void setChoose( String value )
	{
		int iValue = 0;
		try
		{
			iValue = Integer.parseInt( value );
		}
		catch( NumberFormatException nfe )
		{
			// DOn't do anything
		}
		setChoose( iValue );
	}

	public int getChoose()
	{
		return _avTable.getInt( ATTR_CHOICE , 0 );
	}

	/**
     * Get the priority for the SpTelescopeObsComp at the specified index.
     */
	public String getSurveyID()
	{
		return _avTable.get( ATTR_SURVEY_ID );
	}

	/**
     * Set the priority for the SpTelescopeObsComp at the specified index.
     */
	public void setSurveyID( String surveyID )
	{
		_avTable.set( ATTR_SURVEY_ID , surveyID );
	}

	/**
     * Get the index of the selected SpTelescopeObsComp.
     */
	public int getSelectedTelObsComp()
	{
		return _avTable.getInt( ATTR_SELECTED_TEL_OBS_COMP , 0 );
	}

	/**
     * Set the index of the selected SpTelescopeObsComp without notifying the
     * state machine.
     */
	public void noNotifySetSelectedTelObsComp( int index )
	{
		_avTable.noNotifySet( ATTR_SELECTED_TEL_OBS_COMP , "" + index , 0 );
	}

	/**
     * ATTR_PRIORITY Set the index of the selected SpTelescopeObsComp.
     */
	public void setSelectedTelObsComp( int index )
	{
		_avTable.set( ATTR_SELECTED_TEL_OBS_COMP , index );
	}

	public SpTelescopeObsComp getSpTelescopeObsComp( int index )
	{
		return ( SpTelescopeObsComp )_telescopeObsCompVector.get( index );
	}

	public int size()
	{
		return _telescopeObsCompVector.size();
	}

	public SpTelescopeObsComp noNotifyAddSpTelescopeObsComp()
	{
		SpTelescopeObsComp spTelescopeObsComp = new SpTelescopeObsComp();
		spTelescopeObsComp.setSurveyComponent( this );

		if( !_telescopeObsCompVector.contains( spTelescopeObsComp ) )
		{
			_telescopeObsCompVector.add( spTelescopeObsComp );
			spTelescopeObsComp.setEditFSM( getEditFSM() );
		}

		return spTelescopeObsComp;
	}

	public SpTelescopeObsComp addSpTelescopeObsComp()
	{
		SpTelescopeObsComp spTelescopeObsComp = noNotifyAddSpTelescopeObsComp();
		spTelescopeObsComp.getTable().edit();
		setRemaining( 1 , _telescopeObsCompVector.size() - 1 );
		return spTelescopeObsComp;
	}

	public void addSpTelescopeObsComp( SpTelescopeObsComp spTelescopeObsComp )
	{
		if( !_telescopeObsCompVector.contains( spTelescopeObsComp ) )
		{
			_telescopeObsCompVector.add( spTelescopeObsComp );
			spTelescopeObsComp.setEditFSM( getEditFSM() );
			spTelescopeObsComp.getTable().edit();

			// Set remaining to a default value because otherwise
			// writeTargetList() will not be called in processAvAttribute().
			// Not calling setRemaining() does not seem to cause a problem for
			// the OT but is causes a problem for the Survey Definition Tool
			setRemaining( 1 , _telescopeObsCompVector.size() - 1 );
		}
	}

	public void addSpTelescopeObsComp( SpTelescopeObsComp spTelescopeObsComp , int at )
	{
		if( !_telescopeObsCompVector.contains( spTelescopeObsComp ) )
		{
			_telescopeObsCompVector.add( at , spTelescopeObsComp );
			spTelescopeObsComp.setEditFSM( getEditFSM() );
			spTelescopeObsComp.getTable().edit();
		}
	}

	public void removeSpTelescopeObsComp( SpTelescopeObsComp spTelescopeObsComp )
	{
		if( _telescopeObsCompVector.contains( spTelescopeObsComp ) )
			_telescopeObsCompVector.remove( spTelescopeObsComp );

		// Make sure that there is at least one SpTelescopeObsComp in the survey component.
		if( size() < 1 )
			addSpTelescopeObsComp();
		else
			( ( SpTelescopeObsComp )_telescopeObsCompVector.get( 0 ) ).getTable().edit();
	}

	public void removeSpTelescopeObsComp( int index )
	{
		_telescopeObsCompVector.remove( index );

		removeRemaining( index );
		removePriority( index );

		// Make sure that there is at least one SpTelescopeObsComp in the survey component.
		if( size() < 1 )
			addSpTelescopeObsComp();
		else
			( ( SpTelescopeObsComp )_telescopeObsCompVector.get( 0 ) ).getTable().edit();
	}

	public void removeAllSpTelescopeObsComponents()
	{
		_telescopeObsCompVector.removeAllElements();

		// Make sure that there is at least one SpTelescopeObsComp in the survey component.
		addSpTelescopeObsComp();
	}

	public void load( String fileName ) throws Exception
	{
		load( new FileReader( fileName ) );
	}

	public void load( Reader reader ) throws Exception
	{
		LineNumberReader lineNumberReader = new LineNumberReader( reader );
		String line = null;
		StringTokenizer stringTokenizer = null;

		String tag = null;
		String name = "";
		String x = "0:00:00";
		String y = "0:00:00";
		String coordSystem = "";
		String tileString = null;
		int coordSystemIndex = CoordSys.FK5;

		SpTelescopeObsComp spTelescopeObsComp = null;

		do
		{
			line = lineNumberReader.readLine();

			if( line != null )
			{
				if( line.toUpperCase().startsWith( "SURVEY_ID" ) || line.toUpperCase().startsWith( "SURVEYID" ) )
				{
					if( ( line.indexOf( ':' ) == -1 ) && ( line.indexOf( '=' ) != -1 ) )
					{
						setSurveyID( line.substring( line.indexOf( '=' ) + 1 ).trim() );
						continue;
					}

					if( ( line.indexOf( '=' ) == -1 ) && ( line.indexOf( ':' ) != -1 ) )
					{
						setSurveyID( line.substring( line.indexOf( ':' ) + 1 ).trim() );
						continue;
					}

					System.out.println( "Could not parse survey ID: \"" + line + "\". Format should be: SURVEY_ID = <survey id string>." );
					continue;
				}

				stringTokenizer = new StringTokenizer( line , ",;" );

				if( stringTokenizer.hasMoreTokens() )
					tag = stringTokenizer.nextToken().trim();

				if( stringTokenizer.hasMoreTokens() )
					name = stringTokenizer.nextToken().trim();

				if( stringTokenizer.hasMoreTokens() )
					x = stringTokenizer.nextToken().trim();

				if( stringTokenizer.hasMoreTokens() )
					y = stringTokenizer.nextToken().trim();

				if( stringTokenizer.hasMoreTokens() )
					coordSystem = stringTokenizer.nextToken().trim();

				// Modify coordinate system if required
				if( ( coordSystem.toUpperCase().indexOf( "FK5" ) > -1 ) || ( coordSystem.toUpperCase().indexOf( "J2000" ) > -1 ) )
					coordSystemIndex = CoordSys.FK5;
				else if( ( coordSystem.toUpperCase().indexOf( "FK4" ) > -1 ) || ( coordSystem.toUpperCase().indexOf( "B1950" ) > -1 ) )
					coordSystemIndex = CoordSys.FK4;
				else if( ( coordSystem.toUpperCase().indexOf( "GAL" ) > -1 ) )
					coordSystemIndex = CoordSys.GAL;

				if( stringTokenizer.hasMoreTokens() )
					tileString = stringTokenizer.nextToken().trim();

				if( tag.equalsIgnoreCase( SpTelescopePos.BASE_TAG ) )
				{
					if( spTelescopeObsComp != null )
						addSpTelescopeObsComp( spTelescopeObsComp );

					spTelescopeObsComp = new SpTelescopeObsComp();
					spTelescopeObsComp.setSurveyComponent( this );

					spTelescopeObsComp.getPosList().getBasePosition().setName( getSurveyID() + ":" + name );
					spTelescopeObsComp.getPosList().getBasePosition().setXYFromString( x , y );
					// Use the standardized CoordSys.getCoordSys(coordSystemIndex) instead of coordSystem.
					spTelescopeObsComp.getPosList().getBasePosition().setCoordSys( CoordSys.getSystem( coordSystemIndex ) );

					if( tileString != null )
					{
						int positionInTile = 0;

						try
						{
							positionInTile = Integer.parseInt( tileString );
						}
						catch( Exception e )
						{
							System.err.println( "SpSurveyContainer.load(): Could not parse position in tile from \"" + tileString + "\". Assuming first position in tile." );
						}

						spTelescopeObsComp.setPositionInTile( positionInTile );

						spTelescopeObsComp.setFitsKey( "SURVEY" , 0 );
						spTelescopeObsComp.setFitsValue( getSurveyID() , 0 );

						spTelescopeObsComp.setFitsKey( "SURVEY_I" , 1 );
						spTelescopeObsComp.setFitsValue( name , 1 );
					}
				}
				else
				{
					if( spTelescopeObsComp != null )
					{
						try
						{
							spTelescopeObsComp.getPosList().createPosition( tag , x , y , coordSystemIndex ).setName( name );
						}
						catch( Exception e )
						{
							e.printStackTrace();
							throw new Exception( "Could not create telescope position:" + "\n  tag = " + tag + "\n  coordinate 1 = " + x + "\n  coordinate 2 = " + y + "\n  coordinate system = " + CoordSys.getSystem( coordSystemIndex ) );
						}
					}
					else
					{
						if( line.trim().equals( "" ) || resemblesSurveyAreaDefinition( line ) )
							System.out.println( "Ignoring \"" + line + "\" in pointing file (Might be part of an SDT file)." );
						else
							throw new Exception( "Could not parse list of survey targets.\n" + "Make sure the list starts with the " + SpTelescopePos.BASE_TAG + " tag." );
					}
				}
			}
		}
		while( line != null );

		// Add the remaining field
		if( spTelescopeObsComp != null )
			addSpTelescopeObsComp( spTelescopeObsComp );
	}

	public SpItem deepCopy()
	{
		SpItem copy = super.deepCopy();
		( ( SpSurveyContainer )copy ).initTelescopeObsCompVector( _telescopeObsCompVector );
		return copy;
	}

	public SpItem shallowCopy()
	{
		SpItem copy = super.shallowCopy();
		( ( SpSurveyContainer )copy ).initTelescopeObsCompVector( _telescopeObsCompVector );
		return copy;
	}

	/**
     * Crude check as to whether a line in an ASCII file looks like a survey
     * area definition (as opposed to a pointing definition) as used in a Survey
     * Definition Tool (SDT) file.
     * 
     * The pointing definition format is the same in SDT files and in the ASCII
     * format read by the load method of this SpSurveyContainer class. By
     * ignoring survey area definition a empty lines the load method can be used
     * to directly read SDT files. Note that this is not the normal way of
     * importing pointings into the OT. Normally the SDT exports its pointings
     * to OT Science Progammes in which the pointings are spread over several
     * SpSurveyContainers, each in a separate MSB.
     */
	public static boolean resemblesSurveyAreaDefinition( String line )
	{
		if( ( line == null ) || ( line.trim().equals( "" ) ) )
			return false;

		StringTokenizer tokenizer = new StringTokenizer( line , " :," );

		while( tokenizer.hasMoreTokens() )
		{
			try
			{
				Double.parseDouble( tokenizer.nextToken() );
			}
			catch( Exception e )
			{
				return !tokenizer.hasMoreTokens() ;
			}
		}

		return true;
	}

	public boolean hasMSBParent()
	{
		boolean hasMSBParent = false;
		SpItem parent = ( SpItem )this;
		while( parent != null )
		{
			if( parent instanceof SpMSB )
			{
				hasMSBParent = true;
				break;
			}
			parent = parent.parent();
		}
		return hasMSBParent;
	}

	public void processXmlElementStart( String name )
	{
		if( name.equals( "SpTelescopeObsComp" ) )
		{
			_processingTelObsComp = noNotifyAddSpTelescopeObsComp();
			_processingTelObsComp.processXmlElementStart( _telObsCompXmlElementName );
		}
		else
		{
			super.processXmlElementStart( name );
		}
	}

	public void processXmlAttribute( String elementName , String attributeName , String value )
	{
		if( _processingTelObsComp != null )
			_processingTelObsComp.processXmlAttribute( elementName , attributeName , value );
		else if( attributeName.equals( ATTR_PRIORITY ) )
			setPriority( Integer.parseInt( value ) , _telescopeObsCompVector.size() );
		else if( attributeName.equals( ATTR_REMAINING ) )
			setRemaining( Integer.parseInt( value ) , _telescopeObsCompVector.size() );
		else
			super.processXmlAttribute( elementName , attributeName , value );
	}

	public void processXmlElementContent( String name , String value )
	{
		if( _processingTelObsComp != null )
		{
			if( name.equals( "SpTelescopeObsComp" ) )
				_processingTelObsComp.processXmlElementContent( _telObsCompXmlElementName , value );
			else
				_processingTelObsComp.processXmlElementContent( name , value );
		}
		else
		{
			super.processXmlElementContent( name , value );
		}
	}

	public void processXmlElementContent( String name , String value , int pos )
	{
		if( _processingTelObsComp != null )
		{
			if( name.equals( "SpTelescopeObsComp" ) )
				_processingTelObsComp.processXmlElementContent( _telObsCompXmlElementName , value , pos );
			else
				_processingTelObsComp.processXmlElementContent( name , value , pos );
		}
		else
		{
			super.processXmlElementContent( name , value , pos );
		}
	}

	public void processXmlElementEnd( String name )
	{
		if( name.equals( "SpTelescopeObsComp" ) )
		{
			_processingTelObsComp.processXmlElementEnd( _telObsCompXmlElementName );
			_processingTelObsComp = null;
		}
		else
		{
			super.processXmlElementEnd( name );
		}
	}

	/**
     * Ignores SpTelescopeObsComp attributes.
     * 
     * Since the SpAvTable of this SpSurveyContainer is used by which ever
     * SpTelescopeObsComp is currently being edited there will still be
     * attributes in the table that belong to a SpTelescopeObsComp. But they
     * should be ignored here. All the SpTelescopeObsComp information is
     * processed in the method toXML();
     */
	protected void processAvAttribute( String avAttr , String indent , StringBuffer xmlBuffer )
	{
		if( !_tagVector.contains( avAttr ) )
		{
			if( avAttr.equals( ATTR_REMAINING ) )
				writeTargetList( indent + indent , xmlBuffer );
			else if( avAttr.equals( ATTR_PRIORITY ) )
				; // Do nothing
			else
				super.processAvAttribute( avAttr , indent , xmlBuffer );
		}
	}

	protected void toXML( String indent , StringBuffer xmlBuffer )
	{
		super.toXML( indent , xmlBuffer );
	}

	private void writeTargetList( String indent , StringBuffer xmlBuffer )
	{
		xmlBuffer.append( "\n" + indent + "<TargetList>" );

		String telescopeObsCompXML = null;
		for( int i = 0 ; i < _telescopeObsCompVector.size() ; i++ )
		{
			String tgtElement = "<Target " + ATTR_PRIORITY + "=\"" + getPriority( i ) + "\" " + ATTR_REMAINING + "=\"" + getRemaining( i ) + "\">";
			xmlBuffer.append( "\n" + indent + indent + tgtElement );

			telescopeObsCompXML = ( ( SpTelescopeObsComp )_telescopeObsCompVector.get( i ) ).getXML( "  " + indent + indent + indent );
			xmlBuffer.append( telescopeObsCompXML );

			xmlBuffer.append( "\n" + indent + indent + "</Target>" );
		}

		xmlBuffer.append( "\n" + indent + "</TargetList>" );
	}

	/**
     * Calculate the total time for the survey component. This is simply the MSB
     * etimated time or the sum of all the obs times which are not optional,
     * multiplied by the number entries in the survey component
     */
	public double getElapsedTime()
	{
		double elapsedTime = 0.0;
		Enumeration children = children();
		SpItem child = null;

		while( children.hasMoreElements() )
		{
			child = ( SpItem )children.nextElement();
			if( child instanceof SpObs )
			{
				if( !( ( ( SpObs )child ).isOptional() ) )
					elapsedTime += ( ( SpObs )child ).getElapsedTime();
			}
			else if( child instanceof SpMSB )
			{
				elapsedTime += ( ( SpMSB )child ).getElapsedTime();
			}
		}

		int totRemaining = 0;
		for( int i = 0 ; i < size() ; i++ )
			totRemaining += getRemaining( i );

		elapsedTime *= totRemaining;
		return elapsedTime;
	}

	/**
     * Calculate the estimated time for the survey component. This is simply the
     * MSB total time or the sum of all the obs times, multiplied by the number
     * entries in the survey component
     */
	public double getTotalTime()
	{
		double elapsedTime = 0.0;
		Enumeration children = children();
		SpItem child = null;
		boolean hasObsChild = false;

		while( children.hasMoreElements() )
		{
			child = ( SpItem )children.nextElement();
			if( child instanceof SpObs )
			{
				hasObsChild = true;
				elapsedTime += ( ( SpObs )child ).getElapsedTime();
			}
			else if( child instanceof SpMSB )
			{
				elapsedTime += ( ( SpMSB )child ).getTotalTime();
			}
		}

		// If there is an Obs child, we should add a slew time as well,
		// which is just the instrument slew time times the number of
		// targets. We don't do this if the child is an MSB since this
		// already adds a slew time
		double slewTime = 0.0;
		if( hasObsChild )
		{
			SpInstObsComp inst = SpTreeMan.findInstrument( this );
			if( inst != null )
				slewTime = inst.getSlewTime() * size();
		}

		// Get the total number of repeats for each target
		int totRemaining = 0;
		for( int i = 0 ; i < size() ; i++ )
			totRemaining += getRemaining( i );

		elapsedTime *= totRemaining;
		elapsedTime += slewTime;

		return elapsedTime;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for( int i = 0 ; i < _telescopeObsCompVector.size() ; i++ )
		{
			SpTelescopeObsComp obsComp = ( SpTelescopeObsComp )_telescopeObsCompVector.get( i );
			sb.append( obsComp.getTitle() + " " + getRemaining( i ) + " " + getPriority( i ) + "\n" );
		}
		return sb.toString();
	}
}
