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
//
// $Id$
//
package gemini.sp ;

import java.util.Enumeration ;
import java.util.Observable ;
import java.util.Observer ;
import java.util.Vector ;

/**
 * The SpEditState class implements a simple state machine that tracks the edit
 * state of a program or plan. Every SpItem in the program or plan has a
 * referance to a single SpEditState.
 * <p>
 * Clients interested in when the program has been edited in any way can support
 * the SpEditChangeObserver interface and add themselves as an observer. If only
 * interested in when the structure or hierarchy of the program is modified, the
 * client may implement the SpHierarchyChangeObserver.
 */
@SuppressWarnings( "serial" )
public final class SpEditState implements Observer , java.io.Serializable
{

	/**
     * The item is in this state if its attributes have not been edited since
     * the last time they were saved.
     */
	public static final int UNEDITED = 0 ;

	/**
     * The item is in this state if its attributes have been edited since the
     * last time they were saved.
     */
	public static final int EDITED = 1 ;

	//
	// This is the program or plan item at the root of the hierarchy.
	//
	private SpItem _spItem ;

	//
	// The edit state of the item's attribute/value table. One of UNEDITED,
	// or EDITED.
	//
	private int _state ;

	//
	// Observers of structure changes.
	//
	private Vector<SpHierarchyChangeObserver> _hierObservers = new Vector<SpHierarchyChangeObserver>() ;

	//
	// Observers of edit state changes.
	//
	private Vector<SpEditChangeObserver> _editObservers = new Vector<SpEditChangeObserver>() ;

	//
	// Construct with an SpItem. This constructor is not public and so cannot
	// be used by clients. The intention is that the program will create its
	// SpEditState class for itself.
	//
	SpEditState( SpItem spItem )
	{
		_spItem = spItem ;
		_spItem.addObserver( this ) ;
		_state = UNEDITED ;
	}

	/**
     * Get the associated SpItem at the root of the program hierarchy.
     */
	public SpItem getItem()
	{
		return _spItem ;
	}

	/**
     * Get the current edit state of the item's AV table.
     */
	public int getState()
	{
		return _state ;
	}

	/**
     * Add an observer of structure changes. SpHiearcharyChangeObservers are
     * notified when a change to the program structure changes. Attempts to add
     * an observer that is already registered have no effect.
     */
	public synchronized final void addHierarchyChangeObserver( SpHierarchyChangeObserver hco )
	{
		if( !_hierObservers.contains( hco ) )
			_hierObservers.addElement( hco ) ;
	}

	/**
     * Delete a hierarchy change observer.
     */
	public synchronized final void deleteHierarchyChangeObserver( SpHierarchyChangeObserver hco )
	{
		_hierObservers.removeElement( hco ) ;
	}

	/**
     * Delete all hierarchy change observers.
     */
	public synchronized final void deleteHierarchyChangeObservers()
	{
		_hierObservers.removeAllElements() ;
	}

	//
	// Notify hierarchy change observer that items have been added.
	//
    synchronized void notifyAdded( SpItem parent , SpItem[] newChildren , SpItem afterChild )
	{
		Enumeration<SpHierarchyChangeObserver> e = _hierObservers.elements() ;
		while( e.hasMoreElements() )
		{
			SpHierarchyChangeObserver hco = e.nextElement() ;
			hco.spItemsAdded( parent , newChildren , afterChild ) ;
		}
		setEdited() ;
	}

	//
	// Notify hierarchy change observer that items have been removed.
	//
    synchronized void notifyRemoved( SpItem parent , SpItem[] children )
	{
		Enumeration<SpHierarchyChangeObserver> e = _hierObservers.elements() ;
		while( e.hasMoreElements() )
		{
			SpHierarchyChangeObserver hco = e.nextElement() ;
			hco.spItemsRemoved( parent , children ) ;
		}
		setEdited() ;
	}

	// Not yet used.
    synchronized void notifyMoved( SpItem oldParent , SpItem[] children , SpItem newParent , SpItem afterChild )
	{
		Enumeration<SpHierarchyChangeObserver> e = _hierObservers.elements() ;
		while( e.hasMoreElements() )
		{
			SpHierarchyChangeObserver hco = e.nextElement() ;
			hco.spItemsMoved( oldParent , children , newParent , afterChild ) ;
		}
		setEdited() ;
	}

	/**
     * Add an observer of edit state changes. SpEditChangeObservers are notified
     * when a change to the program edit state changes. Attempts to add an
     * observer that is already registered have no effect.
     */
	public synchronized final void addEditChangeObserver( SpEditChangeObserver eco )
	{
		if( !_editObservers.contains( eco ) )
			_editObservers.addElement( eco ) ;
	}

	/**
     * Delete an edit state observer.
     */
	public synchronized final void deleteEditChangeObserver( SpEditChangeObserver eco )
	{
		_editObservers.removeElement( eco ) ;
	}

	/**
     * Delete all edit state observers.
     */
	public synchronized final void deleteEditChangeObservers()
	{
		_editObservers.removeAllElements() ;
	}

	//
	// Notify edit change observers of an update.
	//
    private synchronized void _notifyEditChange()
	{
		Enumeration<SpEditChangeObserver> e = _editObservers.elements() ;
		while( e.hasMoreElements() )
		{
			SpEditChangeObserver eco = e.nextElement() ;
			eco.spEditStateChange( _spItem ) ;
		}
	}

	//
	// Change the state, notifying any observers.
	//
	private void _changeState( int newState )
	{
		_state = newState ;
		_notifyEditChange() ;
	}

	/**
     * Change the state to edited, notifying edit change observers.
     */
	public void setEdited()
	{
		switch( _state )
		{
			case EDITED :
				// Do not notify observers of each edit
				// _changeState(EDITED) ;
				break ;

			case UNEDITED :
				_changeState( EDITED ) ;
				break ;
		}
	}

	/**
     * This method is called when any item in the program is edited, other than
     * for a structure change.
     */
	public void update( Observable obs , Object arg )
	{
		if( obs instanceof SpAvEditState )
		{
			SpAvEditState avEditFSM = ( SpAvEditState )obs ;
			// Only interrested when the av edit state changes to edited
			if( avEditFSM.getState() != SpAvEditState.EDITED )
				return ;
		}

		setEdited() ;
	}

	/**
     * This method is called by clients wishing to forget any edits that have
     * occurred (say after a save of the program).
     */
	public void reset()
	{
		switch( _state )
		{
			case UNEDITED :
				// Do nothing
				break ;

			case EDITED :
				_changeState( UNEDITED ) ;
				break ;
		}

		_resetAvEditState( _spItem ) ;
	}

	//
	// Reset the attribute/value edit state for each item in the program.
	//
	private void _resetAvEditState( SpItem spItem )
	{
		spItem.getAvEditFSM().save() ;
		Enumeration<SpItem> children = spItem.children() ;
		while( children.hasMoreElements() )
			_resetAvEditState( children.nextElement() ) ;
	}

	/**
     * The string representation of the current state.
     */
	public String toString()
	{
		String ret = getClass().getName() + "[state=" ;
		switch( _state )
		{
			case UNEDITED :
				ret += "UNEDITED" ;
				break ;
			case EDITED :
				ret += "EDITED" ;
				break ;
			default :
				ret += "UNKOWN!" ;
		}
		return ret + "]" ;
	}
}
