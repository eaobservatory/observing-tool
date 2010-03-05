// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util ;

import java.util.Vector ;

/**
 * A ordered list of TelescopePos objects. This class presents an abstract
 * notion of a list of telescope position objects.
 * 
 * @see gemini.sp.SpTelescopePosList
 * @see gemini.sp.SpOffsetPosList
 */
@SuppressWarnings( "serial" )
public abstract class TelescopePosList implements java.io.Serializable
{
	// Position List content/ordering watchers
	private Vector<TelescopePosListWatcher> _watchers ;

	// Position selection watchers
	private Vector<TelescopePosSelWatcher> _selWatchers ;

	/** The currently selected telescope position. */
	protected TelescopePos _selPos ;

	/**
     * Get the number of positions that are in the list.
     */
	abstract public int size() ;

	/**
     * Retrieve the position at the given index.
     */
	abstract public TelescopePos getPositionAt( int index ) ;

	/**
     * Does the named position exist?
     */
	abstract public boolean exists( String tag ) ;

	/**
     * Remove a specific position.
     */
	abstract public void removePosition( TelescopePos tp ) ;

	/**
     * Get a Vector containing TelescopePos objects.
     */
	public synchronized TelescopePos[] getAllPositions()
	{
		int sz = size() ;
		TelescopePos[] tpA = new TelescopePos[ sz ] ;
		for( int i = 0 ; i < sz ; ++i )
			tpA[ i ] = getPositionAt( i ) ;

		return tpA ;
	}

	/**
     * Retrieve the position with the given tag.
     */
	public synchronized TelescopePos getPosition( String tag )
	{
		TelescopePos tp = null ;

		int i = getPositionIndex( tag ) ;
		if( i > -1 )
			tp = getPositionAt( i ) ;

		return tp ;
	}

	/**
     * Get the index of the given position, returning -1 if the position isn't
     * in the list.
     */
	public synchronized int getPositionIndex( TelescopePos tp )
	{
		int res = -1 ;
		int sz = size() ;
		for( int i = 0 ; i < sz ; ++i )
		{
			if( getPositionAt( i ) == tp )
			{
				res = i ;
				break ;
			}
		}
		return res ;
	}

	/**
     * Get the index of the given position, returning -1 if the position isn't
     * in the list.
     */
	public synchronized int getPositionIndex( String tag )
	{
		int res = -1 ;
		int sz = size() ;
		for( int i = 0 ; i < sz ; ++i )
		{
			TelescopePos tp = getPositionAt( i ) ;
			if( tp.getTag().equals( tag ) )
			{
				res = i ;
				break ;
			}
		}
		return res ;
	}

	/**
     * Remove a given position by its tag.
     */
	public TelescopePos removePosition( String tag )
	{
		TelescopePos tp = getPosition( tag ) ;
		if( tp == null )
			return null ;

		removePosition( tp ) ;
		return tp ;
	}

	/**
     * Get the "current" position.
     */
	public TelescopePos getSelectedPos()
	{
		return _selPos ;
	}

	/**
     * Select a position.
     */
	public void setSelectedPos( TelescopePos tp )
	{
		synchronized( this )
		{
			if( exists( tp.getTag() ) )
			{
				_selPos = tp ;
				_notifyOfSelect( tp ) ;
			}
		}
	}

	/**
     * Add a pos list observer.
     */
	public synchronized void addWatcher( TelescopePosListWatcher tplw )
	{
		if( _watchers == null )
			_watchers = new Vector<TelescopePosListWatcher>() ;
		else if( !_watchers.contains( tplw ) )
			_watchers.addElement( tplw ) ;
	}

	/**
     * Remove a pos list observer.
     */
	public synchronized void deleteWatcher( TelescopePosListWatcher tplw )
	{
		if( _watchers != null )
			_watchers.removeElement( tplw ) ;
	}

	/**
     * Remove all pos list watchers.
     */
	public synchronized void deleteWatchers()
	{
		if( _watchers != null )
			_watchers.removeAllElements() ;
	}

	/**
     * Add a pos list selection observer.
     */
	public synchronized void addSelWatcher( TelescopePosSelWatcher tpsw )
	{
		if( _selWatchers == null )
			_selWatchers = new Vector<TelescopePosSelWatcher>() ;
		else if( !_selWatchers.contains( tpsw ) )
			_selWatchers.addElement( tpsw ) ;
	}

	/**
     * Remove a pos list selection observer.
     */
	public synchronized void deleteSelWatcher( TelescopePosSelWatcher tpsw )
	{
		if( _selWatchers != null )
			_selWatchers.removeElement( tpsw ) ;
	}

	/**
     * Remove all pos list selection watchers.
     */
	public synchronized void deleteSelWatchers()
	{
		if( _selWatchers != null )
			_selWatchers.removeAllElements() ;
	}

	/**
     * Copy the watchers list.
     */
	@SuppressWarnings( "unchecked" )
    protected final synchronized Vector<TelescopePosListWatcher> _getWatchers()
	{
		if( _watchers != null )
			return ( Vector<TelescopePosListWatcher> )_watchers.clone() ;
		else
			return null ;
	}

	/**
     * Copy the selection watchers list.
     */
	@SuppressWarnings( "unchecked" )
    protected final synchronized Vector<TelescopePosSelWatcher> _getSelWatchers()
	{
		if( _selWatchers != null )
			return ( Vector<TelescopePosSelWatcher> )_selWatchers.clone() ;
		else
			return null ;
	}

	/**
     * Notify of a reset.
     */
	protected void _notifyOfReset()
	{
		Vector<TelescopePosListWatcher> v = _getWatchers() ;
		if( v != null )
		{
			for( int i = 0 ; i < v.size() ; ++i )
			{
				TelescopePosListWatcher tplw ;
				tplw = v.elementAt( i ) ;
				tplw.posListReset( this , getAllPositions() ) ;
			}
		}
	}

	/**
     * Notify of a reordering.
     */
	protected void _notifyOfReorder( TelescopePos tp )
	{
		Vector<TelescopePosListWatcher> v = _getWatchers() ;
		if( v != null )
		{
			for( int i = 0 ; i < v.size() ; ++i )
			{
				TelescopePosListWatcher tplw ;
				tplw = v.elementAt( i ) ;
				tplw.posListReordered( this , getAllPositions() , tp ) ;
			}
		}
	}

	/**
     * Notify that a position has been added.
     */
	protected void _notifyOfAdd( TelescopePos tp )
	{
		Vector<TelescopePosListWatcher> v = _getWatchers() ;
		if( v != null )
		{
			for( int i = 0 ; i < v.size() ; ++i )
			{
				TelescopePosListWatcher tplw ;
				tplw = v.elementAt( i ) ;
				tplw.posListAddedPosition( this , tp ) ;
			}
		}
	}

	/**
     * Notify that a position has been removed.
     */
	protected void _notifyOfRemove( TelescopePos tp )
	{
		Vector<TelescopePosListWatcher> v = _getWatchers() ;
		if( v != null )
		{
			for( int i = 0 ; i < v.size() ; ++i )
			{
				TelescopePosListWatcher tplw ;
				tplw = v.elementAt( i ) ;
				tplw.posListRemovedPosition( this , tp ) ;
			}
		}
	}

	/**
     * Notify that a position was selected.
     */
	protected void _notifyOfSelect( TelescopePos tp )
	{
		Vector<TelescopePosSelWatcher> v = _getSelWatchers() ;
		if( v != null )
		{
			for( int i = 0 ; i < v.size() ; ++i )
			{
				TelescopePosSelWatcher tpsw ;
				tpsw = v.elementAt( i ) ;
				tpsw.telescopePosSelected( this , tp ) ;
			}
		}
	}
}
