// Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package gemini.sp ;

import java.util.ArrayList ;
import java.util.Enumeration ;

import java.net.URLEncoder ;
import java.net.URLDecoder ;



/**
 * The Note item. Notes are arbitrary text information that may be entered at
 * any level of the hierarchy.
 */
@SuppressWarnings( "serial" )
public class SpNote extends SpItem
{
	public static final String ATTR_NOTE = "note" ;
	
	/**
     * This attribute records whether this note should be highlighted.
     * 
     * @see #isObserveInstruction()
     */
	public static final String ATTR_OBSERVE_INSTRUCTION = ":observeInstruction" ;

	/**
     * Default constructor.
     */
	public SpNote()
	{
		super( SpType.NOTE ) ;

		_avTable.noNotifySet( ATTR_NOTE , "" , 0 ) ;
	}

	/**
     * Override getTitle to return the title attribute.
     */
	public String getTitle()
	{
		String title = type().getReadable() ;
		String titleAttr = getTitleAttr() ;
		if( ( titleAttr != null ) && !( titleAttr.equals( "" ) ) )
			title = title + ": " + titleAttr ;
		
		return title ;
	}

	/**
     * Set the note text.
     */
	public void setNote( String text )
	{
		if( text.contains( "http://" ) )
		{
			try
			{
				text = URLEncoder.encode( text , "UTF-8" ) ;
			}
			catch( java.io.UnsupportedEncodingException uee ){ System.out.println( uee ) ; }
		}
		_avTable.set( ATTR_NOTE , text ) ;
	}

	/**
     * Get the note text.
     */
	public String getNote()
	{
		String text = _avTable.get( ATTR_NOTE ) ;
		// Nb. if the string does not contain spaces then it hasn't been encoded.
		if( !text.trim().equals( "" ) && text.indexOf( ' ' ) == -1 )
		{
			try
			{
				text = URLDecoder.decode( text , "UTF-8" ) ;
			}
			catch( java.io.UnsupportedEncodingException uee ){ System.out.println( uee ) ; }
		}
		return text ;
	}

	/**
     * Set whether this note should be highlighted.
     * 
     * @see #isObserveInstruction()
     */
	public void setObserveInstruction( boolean value )
	{
		if( value )
			_avTable.set( ATTR_OBSERVE_INSTRUCTION , value ) ;
		else
			_avTable.rm( ATTR_OBSERVE_INSTRUCTION ) ;
	}

	/**
     * Returns a paramter-value strings for non note elements. This method is
     * currently onlt used by the query tool
     */
	public String[] getInstructions()
	{
		ArrayList<String> rtnArray = new ArrayList<String>() ;
		Enumeration<String> e = _avTable.attributes() ;
		if( e != null )
		{
			while( e.hasMoreElements() )
			{
				String key = e.nextElement();
				if( key.startsWith( "." ) || key.startsWith( ":" ) || key.equals( ATTR_TITLE ) || key.equals( ATTR_NOTE ) )
					continue ;
				String value = _avTable.get( key ) ;
				rtnArray.add( key + " = " + value ) ;
			}
		}
		return( rtnArray.toArray( new String[ 0 ] ) ) ;
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
		return _avTable.getBool( ATTR_OBSERVE_INSTRUCTION ) ;
	}

	public void setAVPair( String name , String value )
	{
		_avTable.set( name , value ) ;
	}

	public String getValueFor( String name )
	{
		return _avTable.get( name ) ;
	}

	public void rmAVPair( String name )
	{
		_avTable.rm( name ) ;
	}
}
