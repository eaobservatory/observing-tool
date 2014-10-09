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

import java.util.Enumeration ;
import java.util.Hashtable ;

/**
 * These are the valid types for program and plan items.
 */
@SuppressWarnings( "serial" )
public class SpType implements java.io.Serializable
{

	/**
     * The default subtype for items that haven't been assigned a subtype. The
     * subtype is used to distinguish groups of items in the same kind. For
     * instance, observation components have a subtype to identify themselves as
     * scheduling components, target lists, etc. Many items do not have a
     * subtype, so "none" is stored for these cases.
     */
	public static final String NO_SUBTYPE = "none" ;

	/**
     * The two letter type code for all observation components. Each will have
     * its own subtype.
     */
	public static final String OBSERVATION_COMPONENT_TYPE = "oc" ;

	/**
     * The two letter type code for all iterator components. Each will have its
     * own subtype.
     */
	public static final String ITERATOR_COMPONENT_TYPE = "ic" ;

	// A hashtable to store the valid types.
	private static final Hashtable<String,SpType> _types = new Hashtable<String,SpType>() ;

	// Most Basic Types

	/** The Science Program Type. */
	public static final SpType SCIENCE_PROGRAM = SpType.create( "pr" , "Science Program" ) ;

	/** The Science Plan Type. */
	public static final SpType SCIENCE_PLAN = SpType.create( "pl" , "Science Plan" ) ;

	/** The Phase1 Type. */
	public static final SpType PHASE_1 = SpType.create( "p1" , "Phase 1" ) ;

	/** The Library Type. */
	public static final SpType LIBRARY = SpType.create( "lb" , "Library" ) ;

	/** The Library Folder Type. */
	public static final SpType LIBRARY_FOLDER = SpType.create( "lf" , "Library Folder" ) ;

	/** The Observation Type. */
	public static final SpType OBSERVATION = SpType.create( "ob" , "Observation" ) ;

	/** The Observation Link Type. */
	public static final SpType OBSERVATION_LINK = SpType.create( "li" , "Observation Link" ) ;

	/** The Observation Folder Type. */
	public static final SpType OBSERVATION_FOLDER = SpType.create( "fo" , "Observation Folder" ) ;

	/** The Observation Group Type. */
	public static final SpType OBSERVATION_GROUP = SpType.create( "og" , "Observation Group" ) ;

	/** The Note Type. */
	public static final SpType NOTE = SpType.create( "no" , "Note" ) ;

	// The following three types have been added as part of the OMP project.
	// Added by MFO (05 July 2001)

	/**
     * The MSB Folder Type.
     * 
     * The type string "og" (Obs Group) is reused here as Obs Groups behave in
     * the same as MSB folders with respect to InsertPolicy.
     */
	public static final SpType MSB_FOLDER = SpType.create( "og" , "msb" , "MSB Folder" ) ;

	/**
     * The AND Folder Type.
     * 
     * The type string "fo" (Obs Folder) is reused here as Obs Folders behave in
     * the same as AND folder with respect to InsertPolicy.
     */
	public static final SpType AND_FOLDER = SpType.create( "fo" , "and" , "AND Folder" ) ;

	/**
     * The OR Folder Type.
     * 
     * A new type string is used: "of" (OR Folder).
     */
	public static final SpType OR_FOLDER = SpType.create( "of" , "or" , "OR Folder" ) ;

	public static final SpType SURVEY_CONTAINER = SpType.create( "sc" , "survey" , "Survey Container" ) ;

	/**
     * The Iterator Sequence Type. NOTE, 'if' is left over from "iterator
     * folder".
     */
	public static final SpType SEQUENCE = SpType.create( "if" , "Sequence" ) ;

	// Some Specific Observation Components

	/** The "site quality" observation component. */
	// MFO: Changed because UKIRT and JCMT use different site quality components.
	/** The "target list" observation component. */
	public static final SpType OBSERVATION_COMPONENT_TARGET_LIST = SpType.create( OBSERVATION_COMPONENT_TYPE , "targetList" , "Target Information" ) ;

	// Some Specific Iterator Components

	// MFO: "Observe" and "Sky" are not needed in JCMT so they are
	// specified in the config file if needed.
	/** The "observe" iterator component. */
	/** The "offset" iterator component. */
	public static final SpType ITERATOR_COMPONENT_OFFSET = SpType.create( ITERATOR_COMPONENT_TYPE , "offset" , "Offset" ) ;

	/** The "repeat" iterator component. */
	public static final SpType ITERATOR_COMPONENT_REPEAT = SpType.create( ITERATOR_COMPONENT_TYPE , "repeat" , "Repeat" ) ;

	// The type string.
	private String _typeStr ;

	// The subtype string.
	private String _subtypeStr ;

	// The human readable type name.
	private String _readableStr ;

	/**
     * Create an SpType for which there is no subtype, and register it.
     */
	public static SpType create( String typeStr , String readableStr )
	{
		return create( typeStr , NO_SUBTYPE , readableStr ) ;
	}

	/**
     * Create an SpType for which there is a subtype, and register it.
     */
	public static SpType create( String typeStr , String subtypeStr , String readableStr )
	{
		SpType spType = new SpType( typeStr , subtypeStr , readableStr ) ;
		SpType._addSpType( spType ) ;
		return spType ;
	}

	//
	// Construct with all fields. Must be accessed through the static
	// create() methods.
	//
	private SpType( String typeStr , String subtypeStr , String readableStr )
	{
		_typeStr = typeStr ;
		_subtypeStr = subtypeStr ;
		_readableStr = readableStr ;
	}

	/** Get the type. */
	public final String getType()
	{
		return _typeStr ;
	}

	/** Get the subtype. */
	public final String getSubtype()
	{
		return _subtypeStr ;
	}

	/** Get the readable string. */
	public final String getReadable()
	{
		return _readableStr ;
	}

	/** Get a key for the SpType based on its type and subtype. */
	public final String getKey()
	{
		return SpType._getKey( _typeStr , _subtypeStr ) ;
	}

	/**
     * Override equals to permit two SpTypes with the same typeStr and
     * subtypeStr to compare true, regardless of whether they are the same
     * object.
     */
	public boolean equals( Object obj )
	{
		if( this == obj )
			return true ; // trivially equal
		if( getClass() != obj.getClass() )
			return false ;

		// Now see if the definition of these two objects is the same
		SpType other = ( SpType )obj ;
		if( !_typeStr.equals( other.getType() ) )
			return false ;
		if( !_subtypeStr.equals( other.getSubtype() ) )
			return false ;

		// I guess it doesn't really matter whether the human readable string is the same.
		return true ;
	}

	/**
     * Override hashCode to make sure it agrees with equals.
     */
	public int hashCode()
	{
		// 37 is just a random prime, any reasonably large prime will do
		int result = _typeStr.hashCode() ;
		result = 37 * result + _subtypeStr.hashCode() ;
		return result ;
	}

	/**
     * Override toString() to format the debugging output.
     */
	public String toString()
	{
		return getClass().getName() + "[typeStr=" + _typeStr + ", subtypeStr=" + _subtypeStr + ", readableStr=" + _readableStr + "]" ;
	}

	/**
     * Get a key (to the type's hashtable entry) given the type and subtype
     * strings.
     */
	protected static synchronized String _getKey( String typeStr , String subtypeStr )
	{
		return typeStr + "#" + subtypeStr ;
	}

	/**
     * Add a new SpType to the table of types.
     */
	protected static synchronized void _addSpType( SpType spType )
	{
		_types.put( spType.getKey() , spType ) ;
	}

	/**
     * Lookup an SpType given its type and subtype strings.
     */
	public static SpType get( String typeStr , String subtypeStr )
	{
		return _types.get( _getKey( typeStr , subtypeStr ) ) ;
	}

	/**
     * Enumerate all the SpTypes.
     */
	public static Enumeration<SpType> getSpTypes()
	{
		return _types.elements() ;
	}

	/**
     * Enumerate all the SpTypes for the given type.
     */
	public static Enumeration<SpType> getSpTypes( final String typeStr )
	{
		return new Enumeration<SpType>()
		{
			private SpType _nextSpType ;

			private Enumeration<SpType> _enum = _types.elements() ;

			public boolean hasMoreElements()
			{
				if( _enum == null )
					return false ;
				SpType spType ;
				do
				{
					if( !( _enum.hasMoreElements() ) )
						return false ;
					spType = _enum.nextElement() ;
				}
				while( !( spType.getType().equals( typeStr ) ) ) ;
				_nextSpType = spType ;
				return true ;
			}

			public SpType nextElement()
			{
				return _nextSpType ;
			}
		} ;
	}
}
