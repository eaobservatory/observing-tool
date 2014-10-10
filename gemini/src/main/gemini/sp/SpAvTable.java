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
import java.util.Iterator ;
import java.util.TreeMap ;
import java.util.Vector ;

//
// This is an implementation class used only by SpAttrTable, holding a
// (description, vector) pair. The description is a string and the
// vector is a vector of strings. A vector is used because each
// attribute can have multiple values.
//
@SuppressWarnings( "serial" )
final class SpAttr implements java.io.Serializable
{
	private String _description ;
	private Vector<String> _values ;

	public SpAttr( String descr , Vector<String> values )
	{
		_description = descr ;
		_values = values ;
	}

	public String getDescription()
	{
		return _description ;
	}

	public void setDescription( String descr )
	{
		_description = descr ;
	}

	public Vector<String> getValues()
	{
		return _values ;
	}

	public void setValues( Vector<String> values )
	{
		_values = values ;
	}

}

/**
 * This class maintains a table of attributes and their values. The table is a
 * simple hashtable in which the keys are the attribute names and the values are
 * instances of SpAttr (a description, vector of strings pair). Since a vector
 * is stored in each SpAttr, multiple values are permitted for each attribute.
 */
@SuppressWarnings( "serial" )
public final class SpAvTable implements java.io.Serializable
{
	// The table of attributes and values.
	private TreeMap<String,SpAttr> _avTable ;

	// The state machine that tracks this table's edits for its SpItem.
	private SpAvEditState _editFSM ;

	/**
     * Default constructor.
     */
	public SpAvTable()
	{
		_avTable = new TreeMap<String,SpAttr>() ;
	}

	//
	// Construct with an existing hashtable.
	//
	SpAvTable( TreeMap<String,SpAttr> avTable )
	{
		_avTable = avTable ;
	}

	//
	// Set the state machine that tracks this table's edits for its SpItem.
	// This method is not public and because it is not intended for clients
	// outside of this package. SpItems should set the state machine.
	//
	void setStateMachine( SpAvEditState editFSM )
	{
		_editFSM = editFSM ;
	}

	//
	// Fetch the SpAttr value associated with a key, creating it if
	// necessary.
	//
	private SpAttr _getSpAttr( String name )
	{
		SpAttr a = _avTable.get( name ) ;
		if( a == null )
		{
			a = new SpAttr( "No Description" , new Vector<String>() ) ;
			_avTable.put( name , a ) ;
		}
		return a ;
	}

	/**
     * Set the description of an attribute without notifying any observers. This
     * method is called when initializing the AV table so that observers aren't
     * sent a bunch of spurious changes.
     * 
     * @see #setDescription
     */
	public void noNotifySetDescription( String name , String descr )
	{
		SpAttr a = _getSpAttr( name ) ;
		a.setDescription( descr ) ;
	}

	/**
     * Set the description of an attribute.
     */
	public void setDescription( String name , String descr )
	{
		if( _editFSM != null )
		{
			_editFSM.editPending() ;
			noNotifySetDescription( name , descr ) ;
			_editFSM.editComplete() ;
		}
		else
		{
			noNotifySetDescription( name , descr ) ;
		}
	}

	/**
     * Get the description of an attribute.
     */
	public String getDescription( String name )
	{
		SpAttr a = _avTable.get( name ) ;
		if( a == null )
			return null ;
		return a.getDescription() ;
	}

	/**
     * Set the value of a particular attribute at a particular position without
     * notifying any observers. This method is called when initializing the AV
     * table so that observers aren't sent a bunch of spurious changes.
     * 
     * @see #set
     */
	public void noNotifySet( String name , String value , int pos )
	{
		SpAttr a = _getSpAttr( name ) ;
		Vector<String> v = a.getValues() ;

		v.ensureCapacity( pos + 1 ) ;
		int size = v.size() ;
		if( size < ( pos + 1 ) )
		{
			v.setSize( pos + 1 ) ;

			// Add empty strings for the missing elements
			for( int i = size ; i < pos ; ++i )
				v.setElementAt( "" , i ) ;
		}
		v.setElementAt( value , pos ) ;
	}

	//
	// Is the given attribute "hidden"? Hidden attributes start with a '.'
	// character.
	//
	private boolean _isHiddenAttr( String name )
	{
		return ( name != null ) && ( name.length() > 0 ) && ( name.charAt( 0 ) == '.' ) ;
	}

	/**
     * Set the value of a particular attribute at a particular position.
     */
	public void set( String name , String value , int pos )
	{
		if( _isHiddenAttr( name ) )
		{
			noNotifySet( name , value , pos ) ;
		}
		else
		{
			if( _editFSM != null )
			{
				_editFSM.editPending() ;
				noNotifySet( name , value , pos ) ;
				_editFSM.editComplete() ;
			}
			else
			{
				noNotifySet( name , value , pos ) ;
			}
		}
	}

	/**
     * Set the value of a particular attribute as a string. This method makes
     * the assumption that the value to be set is the first (and usually only)
     * one.
     */
	public void set( String name , String value )
	{
		set( name , value , 0 ) ;
	}

	/**
     * Set the value of a particular attribute to the given vector without
     * notifying any observers. This method is called when initializing the AV
     * table so that observers aren't sent a bunch of spurious changes.
     * 
     * @see #setAll
     */
	public void noNotifySetAll( String name , Vector<String> v )
	{
		SpAttr a = _avTable.get( name ) ;
		if( a == null )
		{
			a = new SpAttr( "No Description" , v ) ;
			_avTable.put( name , a ) ;
		}
		else
		{
			a.setValues( v ) ;
		}
	}

	/**
     * Set the value of a particular attribute to the given vector. Note, the
     * Vector isn't copied, just assigned. In otherwords, this is just a pointer
     * assignment.
     */
	public void setAll( String name , Vector<String> v )
	{
		if( _isHiddenAttr( name ) )
		{
			noNotifySetAll( name , v ) ;
		}
		else
		{
			if( _editFSM != null )
			{
				_editFSM.editPending() ;
				noNotifySetAll( name , v ) ;
				_editFSM.editComplete() ;
			}
			else
			{
				noNotifySetAll( name , v ) ;
			}
		}
	}

	/**
     * Set the value of a particular attribute as a boolean.
     */
	public void set( String name , boolean value )
	{
		set( name , value , 0 ) ;
	}

	/**
     * Set the value of a particular attribute at a particular position as a
     * boolean.
     */
	public void set( String name , boolean value , int pos )
	{
		if( value )
			set( name , "true" , pos ) ;
		else
			set( name , "false" , pos ) ;
	}

	/**
     * Set the value of a particular attribute as an int.
     */
	public void set( String name , int value )
	{
		set( name , value , 0 ) ;
	}

	/**
     * Set the value of a particular attribute at a particular position as an
     * int.
     */
	public void set( String name , int value , int pos )
	{
		set( name , String.valueOf( value ) , pos ) ;
	}

	/**
     * Set the value of a particular attribute as a double.
     */
	public void set( String name , double value )
	{
		set( name , value , 0 ) ;
	}

	/**
     * Set the value of a particular attribute at a particular position as a
     * double.
     */
	public void set( String name , double value , int pos )
	{
		set( name , String.valueOf( value ) , pos ) ;
	}

	/**
     * Get the value of a particular attribute as a string.
     */
	public String get( String name )
	{
		return get( name , 0 ) ;
	}

	/**
     * Get the value of a particular attribute at a particular position as a
     * string.
     */
	public String get( String name , int pos )
	{
		SpAttr a = _avTable.get( name ) ;
		if( a == null )
			return null ;

		Vector<String> v = a.getValues() ;
		if( ( v == null ) || ( v.size() <= pos ) )
			return null ;

		return v.elementAt( pos ) ;
	}

	/**
     * Get all the values of a particular attribute as a vector.
     */
	public Vector<String> getAll( String name )
	{
		SpAttr a = _avTable.get( name ) ;
		if( a == null )
			return null ;

		return a.getValues() ;
	}

	/**
     * Debugging method.
     */
	public void printAll( String name )
	{
		if( name.equals( "*" ) )
		{
			System.out.println( _avTable.toString() ) ;
		}
		else
		{

			Vector<String> v = getAll( name ) ;
			if( v == null )
			{
				System.out.println( name + ": DOES NOT EXIST IN TABLE" ) ;
				return ;
			}

			System.out.println( name + " contains:" ) ;
			for( int i = 0 ; i < v.size() ; ++i )
				System.out.println( '\t' + v.elementAt( i ) ) ;
			System.out.println( "-- EOD --" ) ;
		}
	}

	/**
     * Get the value of a particular attribute as a boolean.
     */
	public boolean getBool( String name )
	{
		return getBool( name , 0 ) ;
	}

	/**
     * Get the value of a particular attribute at a particular position as a
     * boolean.
     */
	public boolean getBool( String name , int pos )
	{
		String value = get( name , pos ) ;
		return( ( value != null ) && value.equals( "true" ) ) ;
	}

	/**
     * Get the value of a particular attribute as an int.
     */
	public int getInt( String name , int def )
	{
		return getInt( name , 0 , def ) ;
	}

	/**
     * Get the value of a particular attribute at a particular position as an
     * int.
     */
	public int getInt( String name , int pos , int def )
	{
		String value = get( name , pos ) ;
		if( value == null )
			return def ;
		try
		{
			return Integer.parseInt( value ) ;
		}
		catch( Exception ex ){}
		return def ;
	}

	/**
     * Get the value of a particular attribute as a double.
     */
	public double getDouble( String name , double def )
	{
		return getDouble( name , 0 , def ) ;
	}

	/**
     * Get the value of a particular attribute at a particular position as a
     * double.
     */
	public double getDouble( String name , int pos , double def )
	{
		String value = get( name , pos ) ;
		if( value == null )
			return def ;
		try
		{
			return Double.valueOf( value ) ;
		}
		catch( Exception ex ){}
		return def ;
	}

	/**
     * Remove an attribute.
     */
	public void rm( String name )
	{
		if( _isHiddenAttr( name ) )
		{
			_avTable.remove( name ) ;
		}
		else
		{
			if( _editFSM != null )
			{
				_editFSM.editPending() ;
				_avTable.remove( name ) ;
				_editFSM.editComplete() ;
			}
			else
			{
				_avTable.remove( name ) ;
			}
		}
	}

	/**
     * Remove an attribute without notifying any observers.
     * 
     * Added by MFO (2 November 2001).
     */
	public void noNotifyRm( String name )
	{
		_avTable.remove( name ) ;
	}

	private void _rmValue( String name , int pos )
	{
		SpAttr a = _avTable.get( name ) ;
		if( a == null )
			return ;

		Vector<String> v = a.getValues() ;
		v.removeElementAt( pos ) ;
	}

	/**
     * Remove a particular value of an attribute.
     */
	public void rm( String name , int pos )
	{
		if( _isHiddenAttr( name ) || ( _editFSM == null ) )
		{
			_rmValue( name , pos ) ;
		}
		else
		{
			_editFSM.editPending() ;
			_rmValue( name , pos ) ;
			_editFSM.editComplete() ;
		}
	}

	/**
     * Remove all attributes.
     */
	public void rmAll()
	{
		if( _editFSM != null )
		{
			_editFSM.editPending() ;
			_avTable.clear() ;
			_editFSM.editComplete() ;
		}
		else
		{
			_avTable.clear() ;
		}
	}

	/**
     * Remove all attributes without notifying any observers.
     */
	public void noNotifyRmAll()
	{
		// Clear all of the SpAttr vectors...
		Iterator<String> e = _avTable.keySet().iterator() ;
		while( e.hasNext() )
		{
			SpAttr a = _avTable.get( e.next() ) ;
			if( a != null && a.getValues() != null )
				a.getValues().clear() ;
		}
		_avTable.clear() ;
	}

	/**
     * Find out how many attributes are in the table.
     */
	public int size()
	{
		return _avTable.size() ;
	}

	/**
     * Find out whether the given attribute exists.
     */
	public boolean exists( String name )
	{
		return _avTable.containsKey( name ) ;
	}

	/**
     * Find out how many values the given attribute has.
     */
	public int size( String name )
	{
		SpAttr a = _avTable.get( name ) ;
		if( a == null )
			return 0 ;

		Vector<String> v = a.getValues() ;
		if( v == null )
			return 0 ;
		return v.size() ;
	}

	/**
     * Marks the table as edited.
     * 
     * This can be used to trigger a call to java.util.Observer.update without
     * actually changing the table.
     */
	public void edit()
	{
		if( _editFSM != null )
		{
			_editFSM.editPending() ;
			_editFSM.editComplete() ;
		}
	}

	/**
     * Get an iterator over the attributes
     */
	public Iterator<String> getAttrIterator()
	{
		return _avTable.keySet().iterator() ;
	}

	/**
     * Get an enumeration of the attributes.
     */
	public Enumeration<String> attributes()
	{
		return new Enumeration<String>()
		{
			private Iterator<String> _keys = _avTable.keySet().iterator() ;
			private String _nextKey = null ;

			public synchronized boolean hasMoreElements()
			{
				_nextKey = null ;
				if( _keys.hasNext() )
					_nextKey = _keys.next() ;
				return( _nextKey != null ) ;
			}

			public synchronized String nextElement()
			{
				return _nextKey ;
			}
		} ;
	}

	/**
     * Get an enumeration of the attributes that start with the given prefix.
     */
	public Enumeration<String> attributes( final String prefix )
	{
		return new Enumeration<String>()
		{
			private Iterator<String> _keys = _avTable.keySet().iterator() ;
			private String _nextKey = null ;

			public boolean hasMoreElements()
			{
				_nextKey = null ;
				while( _keys.hasNext() )
				{
					String key = _keys.next() ;
					if( key.startsWith( prefix ) )
					{
						_nextKey = key ;
						break ;
					}
				}
				return( _nextKey != null ) ;
			}

			public String nextElement()
			{
				return _nextKey ;
			}
		} ;
	}

	/**
     * Make a copy of the table.
     */
	@SuppressWarnings( "unchecked" )
    public SpAvTable copy()
	{
		// Shallow copy the table structure.
		TreeMap<String,SpAttr> htCopy = ( TreeMap<String,SpAttr> )_avTable.clone() ;

		// Make a copy of the values.
		Iterator<String> keys = _avTable.keySet().iterator() ;
		while( keys.hasNext() )
		{
			String key = keys.next() ;
			SpAttr a = _avTable.get( key ) ;
			Vector<String> v = new Vector<String>() ;
			if( a != null )
				v = a.getValues() ;
			if( v != null )
				v = ( Vector<String> )v.clone() ;
			else
				v = new Vector<String>() ;
			htCopy.put( key , new SpAttr( a.getDescription() , v ) ) ;
		}

		return new SpAvTable( htCopy ) ;
	}

	//
	// Does the work for the public setSize method.
	//
	private void _setSize( String name , int newSize )
	{
		SpAttr a = _avTable.get( name ) ;
		if( a == null )
			return ;

		Vector<String> v = a.getValues() ;

		if( newSize < v.size() )
		{
			v.setSize( newSize ) ;
		}
		else if( newSize > v.size() )
		{
			// Set the new size, and make the new elements be empty strings.
			int oldSize = v.size() ;
			v.setSize( newSize ) ;
			for( int i = oldSize ; i < newSize ; ++i )
				v.setElementAt( "" , i ) ;
		}
	}

	/**
     * Set the size of this attribute. If the new size is greather than the
     * current size, empty strings are appended for the missing elements. If the
     * new size is less than the current size, all the elements at index newSize
     * and greater are discarded.
     */
	public void setSize( String name , int newSize )
	{
		if( _isHiddenAttr( name ) || ( _editFSM == null ) )
		{
			_setSize( name , newSize ) ;
		}
		else
		{
			_editFSM.editPending() ;
			_setSize( name , newSize ) ;
			_editFSM.editComplete() ;
		}
	}

	/**
     * Add the given value of the named attribute at the end of the list.
     */
	public void add( String name , String value )
	{
		if( _isHiddenAttr( name ) || ( _editFSM == null ) )
		{
			noNotifySet( name , value , size( name ) ) ;
		}
		else
		{
			_editFSM.editPending() ;
			noNotifySet( name , value , size( name ) ) ;
			_editFSM.editComplete() ;
		}
	}

	//
	// Does the work for the public insertAt method.
	//
	private void _insertAt( String value , Vector<String> v , int pos )
	{
		if( pos >= v.size() )
			v.addElement( value ) ;
		else
			v.insertElementAt( value , pos ) ;
	}

	/**
     * Insert the given value of named attribute at the given position.
     */
	public void insertAt( String name , String value , int pos )
	{
		SpAttr a = _avTable.get( name ) ;
		if( a == null )
			return ;

		Vector<String> v = a.getValues() ;
		if( ( pos < 0 ) || ( pos > v.size() ) )
			return ;

		if( _isHiddenAttr( name ) || ( _editFSM == null ) )
		{
			_insertAt( value , v , pos ) ;
		}
		else
		{
			_editFSM.editPending() ;
			_insertAt( value , v , pos ) ;
			_editFSM.editComplete() ;
		}
	}

	//
	// Does the work for the public indexToFirst method.
	//
	private void _indexToFirst( Vector<String> v , int pos )
	{
		String o = v.elementAt( pos ) ;
		v.removeElementAt( pos ) ;
		v.insertElementAt( o , 0 ) ;
	}

	/**
     * Move the element of the named attribute at the given position to the
     * front, or zeroth position.
     */
	public void indexToFirst( String name , int pos )
	{
		SpAttr a = _avTable.get( name ) ;
		if( a == null )
			return ;

		Vector<String> v = a.getValues() ;
		if( ( pos <= 0 ) || ( v.size() <= pos ) )
			return ;

		if( _isHiddenAttr( name ) || ( _editFSM == null ) )
		{
			_indexToFirst( v , pos ) ;
		}
		else
		{
			_editFSM.editPending() ;
			_indexToFirst( v , pos ) ;
			_editFSM.editComplete() ;
		}
	}

	//
	// Does the work for the public decrementIndex method.
	//
	private void _decrementIndex( Vector<String> v , int pos )
	{
		String o = v.elementAt( pos ) ;
		v.removeElementAt( pos ) ;
		v.insertElementAt( o , pos - 1 ) ;
	}

	/**
     * Move the element of the named attributed at the given position up one
     * (toward index 0).
     */
	public void decrementIndex( String name , int pos )
	{
		SpAttr a = _avTable.get( name ) ;
		if( a == null )
			return ;

		Vector<String> v = a.getValues() ;
		if( ( pos <= 0 ) || ( v.size() <= pos ) )
			return ;

		if( _isHiddenAttr( name ) || ( _editFSM == null ) )
		{
			_decrementIndex( v , pos ) ;
		}
		else
		{
			_editFSM.editPending() ;
			_decrementIndex( v , pos ) ;
			_editFSM.editComplete() ;
		}
	}

	//
	// Does the work for the public incrementIndex method.
	//
	private void _incrementIndex( Vector<String> v , int pos )
	{
		String o = v.elementAt( pos ) ;
		v.removeElementAt( pos ) ;
		if( ( pos + 1 ) >= v.size() )
			v.addElement( o ) ;
		else
			v.insertElementAt( o , pos + 1 ) ;
	}

	/**
     * Move the element of the named attributed at the given position up one
     * (toward index 0).
     */
	public void incrementIndex( String name , int pos )
	{
		SpAttr a = _avTable.get( name ) ;
		if( a == null )
			return ;

		Vector<String> v = a.getValues() ;
		if( ( pos < 0 ) || ( ( v.size() - 1 ) <= pos ) )
			return ;

		if( _isHiddenAttr( name ) || ( _editFSM == null ) )
		{
			_incrementIndex( v , pos ) ;
		}
		else
		{
			_editFSM.editPending() ;
			_incrementIndex( v , pos ) ;
			_editFSM.editComplete() ;
		}
	}

	//
	// Does the work for the public indexToLast method.
	//
	private void _indexToLast( Vector<String> v , int pos )
	{
		String o = v.elementAt( pos ) ;
		v.removeElementAt( pos ) ;
		v.addElement( o ) ;
	}

	/**
     * Move the element of the named attributed at the given position to the
     * last position.
     */
	public void indexToLast( String name , int pos )
	{
		SpAttr a = _avTable.get( name ) ;
		if( a == null )
			return ;

		Vector<String> v = a.getValues() ;
		if( ( pos < 0 ) || ( ( v.size() - 1 ) <= pos ) )
			return ;

		if( _isHiddenAttr( name ) || ( _editFSM == null ) )
		{
			_indexToLast( v , pos ) ;
		}
		else
		{
			_editFSM.editPending() ;
			_indexToLast( v , pos ) ;
			_editFSM.editComplete() ;
		}
	}
}
