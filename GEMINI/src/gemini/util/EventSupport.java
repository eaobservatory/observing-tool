// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//

package gemini.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.EventObject;
import java.util.EventListener;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Support for event handling. This class handles the details of keeping track
 * of listeners and firing events for clients. It is thread safe.
 * 
 * @author Shane Walker
 */
public class EventSupport
{

	private Class _listenerClass;

	private Class _eventClass;

	private Vector _listeners;

	private Hashtable _methodTable; // Caches Methods by name so reflection

	// is not required each time.

	/**
     * Construct with the class of the listeners that will be added and the
     * event that will be fired.
     */
	public EventSupport( Class listenerClass , Class eventClass )
	{
		_listenerClass = listenerClass;
		_eventClass = eventClass;

		_listeners = new Vector();
		_methodTable = new Hashtable();
	}

	/**
     * Add a listener to the set of listeners.
     */
	public synchronized void addListener( EventListener el )
	{
		if( !_listeners.contains( el ) )
			_listeners.addElement( el );
	}

	/**
     * Remove a listener from the set of listeners.
     */
	public synchronized void removeListener( EventListener el )
	{
		_listeners.removeElement( el );
	}

	/**
     * Remove all listeners.
     */
	public synchronized void removeAllListeners()
	{
		_listeners.removeAllElements();
	}

	/**
     * Print all the listeners to stdout. This is just a debugging method.
     */
	public synchronized void showListeners( String title )
	{
		System.out.println( "--- " + title + " ---" );
		for( int i = 0 ; i < _listeners.size() ; ++i )
			System.out.println( i + ") " + _listeners.elementAt( i ) );

		System.out.println( "------------------------------" );
	}

	/**
     * Get the named method of the listener class. Uses the cached value if
     * available, or else reflection to lookup the method.
     */
	protected Method getMethod( String methodName )
	{
		// See if there is a cached value.
		Method meth = ( Method )_methodTable.get( methodName );
		if( meth != null )
			return meth;

		// Use reflection to get the method.
		try
		{
			meth = _listenerClass.getMethod( methodName , new Class[] { _eventClass } );
		}
		catch( Exception ex )
		{
			System.out.println( "Couldn't find the method: " + methodName );
		}

		// Cache the method for the next lookup.
		if( meth != null )
			_methodTable.put( methodName , meth );

		return meth;
	}

	/**
     * Fire the event to the listeners.
     */
	public void fireEvent( EventObject eo , String methodName )
	{
		// Lookup the listener method that will be called.
		Method meth = getMethod( methodName );
		if( meth == null )
			return;

		// Make a copy of the listeners so that the event can be fired without holding a lock.
		Vector v;
		synchronized( this )
		{
			v = ( Vector )_listeners.clone();
		}

		Object[] params = new Object[] { eo };

		// Fire the event to each listener.
		int sz = v.size();
		for( int i = 0 ; i < sz ; ++i )
		{
			Object target = ( Object )_listeners.elementAt( i );
			try
			{
				meth.invoke( target , params );
			}
			catch( InvocationTargetException ite )
			{
				System.err.println( "Couldn't invoke method " + methodName + "() on " + target );
				ite.getTargetException().printStackTrace();
			}
			catch( Exception ex )
			{
				System.err.println( "Couldn't invoke method " + methodName + "() on " + target + ": " + ex );
			}
		}
	}
}
