// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import java.util.ArrayList;
import java.util.Enumeration;

/**
 * The Note item. Notes are arbitrary text information that may be entered at
 * any level of the hierarchy.
 */
public class SpNote extends SpItem
{

	public static final String ATTR_NOTE = "note";

	/**
     * This attribute records whether this note should be highlighted.
     * 
     * @see #isObserveInstruction()
     */
	public static final String ATTR_OBSERVE_INSTRUCTION = ":observeInstruction";

	/**
     * Default constructor.
     */
	public SpNote()
	{
		super( SpType.NOTE );

		_avTable.noNotifySet( ATTR_NOTE , "" , 0 );
	}

	/**
     * Override getTitle to return the title attribute.
     */
	public String getTitle()
	{
		String title = type().getReadable();
		String titleAttr = getTitleAttr();
		if( ( titleAttr != null ) && !( titleAttr.equals( "" ) ) )
			title = title + ": " + titleAttr;
		
		return title;
	}

	/**
     * Set the note text.
     */
	public void setNote( String text )
	{
		_avTable.set( ATTR_NOTE , text );
	}

	/**
     * Get the note text.
     */
	public String getNote()
	{
		return _avTable.get( ATTR_NOTE );
	}

	/**
     * Set whether this note should be highlighted.
     * 
     * @see #isObserveInstruction()
     */
	public void setObserveInstruction( boolean value )
	{
		if( value )
			_avTable.set( ATTR_OBSERVE_INSTRUCTION , value );
		else
			_avTable.rm( ATTR_OBSERVE_INSTRUCTION );
	}

	/**
     * Returns a paramter-value strings for non note elements. This method is
     * currently onlt used by the query tool
     */
	public String[] getInstructions()
	{
		ArrayList rtnArray = new ArrayList();
		Enumeration e = _avTable.attributes();
		if( e != null )
		{
			while( e.hasMoreElements() )
			{
				String key = ( String )e.nextElement();
				if( key.startsWith( "." ) || key.startsWith( ":" ) || key.equals( ATTR_TITLE ) || key.equals( ATTR_NOTE ) )
					continue;
				String value = _avTable.get( key );
				rtnArray.add( key + " = " + value );
			}
		}
		return( ( String[] )rtnArray.toArray( new String[ 0 ] ) );
	}

	/**
     * Indicates whether this note should be highlighted.
     * 
     * During obsering this attribute allows programs like the QT to draw the
     * attention of the observer to notes which contain information which is
     * critical for the observation.
     */
	public boolean isObserveInstruction()
	{
		return _avTable.getBool( ATTR_OBSERVE_INSTRUCTION );
	}

	public void setAVPair( String name , String value )
	{
		_avTable.set( name , value );
	}

	public String getValueFor( String name )
	{
		return _avTable.get( name );
	}

	public void rmAVPair( String name )
	{
		_avTable.rm( name );
	}
}
