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

package jsky.app.ot.util ;

import java.util.Vector ;

/**
 * A basic (ordered) property list inspired by the Maribma PropertyList
 * interface.
 */
public class BasicPropertyList
{
	// Class that holds the data for a property.
	class PropertyListEntry
	{
		String name ;
		Object value ;
	}

	// Stores the properties
	private Vector<PropertyListEntry> _props = new Vector<PropertyListEntry>() ;

	// Store a set of change watchers.
	private Vector<PropertyWatcher> _watchers = new Vector<PropertyWatcher>() ;

	//
	// Lookup an entry, returning null if not found.
	//
	private PropertyListEntry _lookupEntry( String name )
	{
		int sz = _props.size() ;
		for( int i = 0 ; i < sz ; ++i )
		{
			PropertyListEntry ple = _props.elementAt( i ) ;
			if( ple.name.equals( name ) )
				return ple ;
		}
		return null ;
	}

	//
	// Lookup an entry, creating and inserting a new PropertyListEntry if
	// not found.
	//
	private PropertyListEntry _getEntry( String name )
	{
		PropertyListEntry ple = _lookupEntry( name ) ;
		if( ple == null )
		{
			ple = new PropertyListEntry() ;
			ple.name = name ;
			_props.addElement( ple ) ;
		}
		return ple ;
	}

	/**
	 * Add a property watchers.  Watchers are notified when a property
	 * changes value.
	 */
	public synchronized void addWatcher( PropertyWatcher watcher )
	{
		if( !_watchers.contains( watcher ) )
			_watchers.addElement( watcher ) ;
	}

	/**
	 * Delete a watcher.
	 */
	public synchronized void deleteWatcher( PropertyWatcher watcher )
	{
		_watchers.removeElement( watcher ) ;
	}

	/**
	 * Delete all watchers.
	 */
	public synchronized final void deleteWatchers()
	{
		_watchers.removeAllElements() ;
	}

	/**
	 * Get a copy of the _watchers Vector.
	 */
	@SuppressWarnings( "unchecked" )
    private synchronized final Vector<PropertyWatcher> _getWatchers()
	{
		return ( Vector<PropertyWatcher> )_watchers.clone() ;
	}

	/**
	 * Notify watchers that a property has changed.
	 */
	private void _notifyChange( String propertyName )
	{
		Vector<PropertyWatcher> v = _getWatchers() ;
		int cnt = v.size() ;
		for( int i = 0 ; i < cnt ; ++i )
		{
			PropertyWatcher pw = v.elementAt( i ) ;
			pw.propertyChange( propertyName ) ;
		}
	}

	/**
	 * Get the generic (Object) value associated with the named property.
	 * This will have to be casted to the proper type.
	 */
	public synchronized Object getValue( String name )
	{
		PropertyListEntry ple = _lookupEntry( name ) ;
		if( ple == null )
			return null ;
		return ple.value ;
	}

	/**
	 * Get the list of property names.
	 */
	public String[] getPropertyNames()
	{
		Vector<String> propNames = new Vector<String>() ;

		synchronized( this )
		{
			int sz = _props.size() ;
			for( int i = 0 ; i < sz ; ++i )
			{
				PropertyListEntry ple = _props.elementAt( i ) ;
				propNames.addElement( ple.name ) ;
			}
		}

		String[] namesA = new String[ propNames.size() ] ;
		propNames.copyInto( namesA ) ;
		return namesA ;
	}

	/**
	 * Get a boolean property by the given name, defaulting to <tt>def</tt>
	 * if the property doesn't exist or isn't a boolean.
	 */
	public synchronized boolean getBoolean( String name , boolean def )
	{
		PropertyListEntry ple = _lookupEntry( name ) ;
		if( ( ple != null ) && ( ple.value instanceof Boolean ) )
			return ( Boolean )ple.value ;

		return def ;
	}

	/**
	 * Set a boolean property with the given name and value.  This method
	 * will create and add the property if it doesn't exist.  If it does
	 * exist and is a boolean it will change the value to the argument
	 * <tt>value</tt>.  If the property exists and isn't a boolean, it
	 * will become a boolean and the previous value will be lost.
	 */
	public synchronized void setBoolean( String name , boolean value )
	{
		PropertyListEntry ple = _getEntry( name ) ;
		ple.value = new Boolean( value ) ;
		_notifyChange( name ) ;
	}

	/**
	 * Get the value of the "choice" property with the given name, defaulting
	 * to <code>def</def>.
	 *
	 * @see ChoiceProperty
	 * @return The integer index of the current choice.
	 */
	public synchronized int getChoice( String name , int def )
	{
		PropertyListEntry ple = _lookupEntry( name ) ;
		if( ( ple != null ) && ( ple.value instanceof ChoiceProperty ) )
			return ( ( ChoiceProperty )ple.value ).getCurValue() ;

		return def ;
	}

	/**
	 * Get the set of options for the given "choice" property.
	 *
	 * @see ChoiceProperty
	 */
	public synchronized String[] getChoiceOptions( String name )
	{
		PropertyListEntry ple = _lookupEntry( name ) ;
		if( ( ple != null ) && ( ple.value instanceof ChoiceProperty ) )
			return ( ( ChoiceProperty )ple.value ).getChoices() ;

		return null ;
	}

	/**
	 * Set or initialize a "choice" property of the given name.  If
	 * the property does not exist, it will be created.  If it does
	 * exist, the <code>options</code> array will be ignored.
	 *
	 * @param name The name of the choice property.
	 * @param options The array of possible options.
	 * @param value The index of the current choice.
	 *
	 * @see ChoiceProperty
	 */
	public synchronized void setChoice( String name , String[] options , int value )
	{
		PropertyListEntry ple = _getEntry( name ) ;
		ChoiceProperty cp = new ChoiceProperty( options ) ;
		cp.setCurValue( value ) ;
		ple.value = cp ;
		_notifyChange( name ) ;
	}

	/**
	 * Set an existing "choice" property to the given value, the
	 * index of the desired option.
	 *
	 * @see ChoiceProperty
	 */
	public synchronized void setChoice( String name , int value )
	{
		PropertyListEntry ple = _lookupEntry( name ) ;
		if( ( ple != null ) && ( ple.value instanceof ChoiceProperty ) )
		{
			( ( ChoiceProperty )ple.value ).setCurValue( value ) ;
			_notifyChange( name ) ;
		}
	}
}
