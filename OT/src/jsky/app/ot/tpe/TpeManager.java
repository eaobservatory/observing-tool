// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe ;

import java.util.Hashtable ;
import java.util.Vector ;
import gemini.sp.SpItem ;
import gemini.sp.SpTreeMan ;
import gemini.sp.obsComp.SpTelescopeObsComp ;
import jsky.util.gui.DialogUtil ;

/**
 * This class manages TelescopePosEditors on behalf of clients.
 * Any client can look up/create the TelescopePosEditor associated
 * with a program or plan by providing any SpItem item in the program.
 */
public final class TpeManager implements TpeWatcher
{
	/**
	 * The position editor, one per TpeManager object.
	 */
	TelescopePosEditor _tpe ;

	/**
	 * The root SpItem associated with the position editor.
	 */
	SpItem _spItem ;

	/**
	 * Contains a mapping of Science Program/Plan items to Position Editors.
	 */
	private static Hashtable<SpItem,TpeManager> _map = new Hashtable<SpItem,TpeManager>() ;

	/**
	 * These are watchers interested in knowing when a TPE has been
	 * opened for a given program, plan, or library.
	 */
	private static Hashtable<SpItem,Vector<TpeManagerWatcher>> _openWatchers = new Hashtable<SpItem,Vector<TpeManagerWatcher>>() ;

	TpeManager( SpItem spItem , TelescopePosEditor tpe )
	{
		_spItem = spItem ;

		_tpe = tpe ;
		_tpe.addWatcher( this ) ;
	}

	/**
	 * The position editor has been shutdown.
	 * @see TpeWatcher
	 */
	public void tpeClosed( TelescopePosEditor tpe )
	{
		_map.remove( _spItem ) ;
		_tpe.deleteWatcher( this ) ;
		_tpe = null ;
		_spItem = null ;
	}

	/**
	 * Get the editor for a given item.  This method looks up the
	 * root item, and then uses that to look in the hashtable for
	 * the editor.
	 */
	public static TelescopePosEditor get( SpItem spItem )
	{
		SpItem root = findRootItem( spItem ) ;

		TpeManager man = _map.get( root ) ;
		if( man == null )
			return null ;

		return man._tpe ;
	}

	/**
	 * Open the editor, creating it if necessary.
	 */
	public static TelescopePosEditor open( SpItem spItem )
	{
		try
		{
			SpItem root = findRootItem( spItem ) ;

			TpeManager man = _map.get( root ) ;

			if( man == null )
			{
				TelescopePosEditor tpe = new TelescopePosEditor( spItem ) ;
				man = new TpeManager( root , tpe ) ;
				_map.put( root , man ) ;
				_notifyOpen( root , tpe ) ;
			}
			else if( man._tpe == null )
			{
				man._tpe = new TelescopePosEditor( spItem ) ;
				_notifyOpen( root , man._tpe ) ;
			}

			man._tpe.setImageFrameVisible( true ) ;

			return man._tpe ;
		}
		catch( NoClassDefFoundError e )
		{
			DialogUtil.error( "The Position Editor cannot be launched because of a missing class: " + e.getMessage() + "\n        Make sure you have Java Advanced Imaging installed (version 1.1 or higher)." ) ;

			e.printStackTrace() ;

			throw e ;
		}
	}

	/**
	 * Associate the TPE with a new root SpItem.
	 */
	public static void remap( SpItem oldItem , SpItem newItem )
	{
		SpItem oldRoot = findRootItem( oldItem ) ;
		TpeManager man = _map.get( oldRoot ) ;

		SpItem newRoot = findRootItem( newItem ) ;
		if( man != null )
		{
			_map.remove( oldRoot ) ;
			man._spItem = newRoot ;
			_map.put( newRoot , man ) ;
		}

		_remapOpenWatchers( oldRoot , newRoot ) ;
	}

	// Remap the tpe open watchers.
	private synchronized static void _remapOpenWatchers( SpItem oldRoot , SpItem newRoot )
	{
		Vector<TpeManagerWatcher> v = _openWatchers.remove( oldRoot ) ;
		if( v != null )
			_openWatchers.put( newRoot , v ) ;
	}

	/**
	 * Remove the editor.
	 */
	public static void remove( SpItem spItem )
	{
		SpItem root = findRootItem( spItem ) ;
		TpeManager man = _map.get( root ) ;

		if( man != null )
			man._tpe.remove() ;
	}

	/**
	 * Add a watcher.  Watchers are notified when a TPE is opened that
	 * is associated with the program (plan, or library) of the given item.
	 */
	public synchronized static void addWatcher( TpeManagerWatcher watcher , SpItem spItem )
	{
		SpItem spRoot = findRootItem( spItem ) ;
		Vector<TpeManagerWatcher> v = _openWatchers.get( spRoot ) ;
		if( v == null )
		{
			v = new Vector<TpeManagerWatcher>() ;
			_openWatchers.put( spRoot , v ) ;
		}

		if( v.contains( watcher ) )
			return ;

		v.addElement( watcher ) ;
	}

	/**
	 * Delete a watcher.
	 */
	public synchronized static void deleteWatcher( TpeManagerWatcher watcher , SpItem spItem )
	{
		SpItem spRoot = findRootItem( spItem ) ;
		Vector<TpeManagerWatcher> v = _openWatchers.get( spRoot ) ;
		if( v == null )
			return ;

		v.removeElement( watcher ) ;
		if( v.size() == 0 )
			_openWatchers.remove( spRoot ) ;
	}

	/**
	 * Delegate this method from the Observable interface.
	 */
	public synchronized static void deleteWatchers( SpItem spItem )
	{
		_openWatchers.remove( spItem ) ;
	}

	/**
	 * Get a copy of the _openWatchers Vector.
	 */
	@SuppressWarnings( "unchecked" )
    private synchronized static Vector<TpeManagerWatcher> _getWatchers( SpItem spItem )
	{
		SpItem spRoot = findRootItem( spItem ) ;
		Vector<TpeManagerWatcher> v = _openWatchers.get( spRoot ) ;
		if( v == null )
			return null ;

		return ( Vector<TpeManagerWatcher> )v.clone() ;
	}

	/**
	 * Notify watchers that a key has been pressed.
	 */
	private static void _notifyOpen( SpItem spItem , TelescopePosEditor tpe )
	{
		Vector<TpeManagerWatcher> v = _getWatchers( spItem ) ;
		if( v == null )
			return ;

		int cnt = v.size() ;
		for( int i = 0 ; i < cnt ; ++i )
		{
			TpeManagerWatcher watcher = ( TpeManagerWatcher )v.elementAt( i ) ;
			watcher.tpeOpened( tpe ) ;
		}
	}

	public static SpItem findRootItem( SpItem spItem )
	{
		if( ( spItem instanceof SpTelescopeObsComp ) && ( ( ( SpTelescopeObsComp )spItem ).getSurveyComponent() != null ) )
			return SpTreeMan.findRootItem( ( ( SpTelescopeObsComp )spItem ).getSurveyComponent() ) ;
		else
			return SpTreeMan.findRootItem( spItem ) ;
	}
}
