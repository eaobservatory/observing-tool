package gemini.util;

import java.util.Date;

/**
 * A timer for keeping track of repeated time events. Modeled after the Marimba
 * Bongo timer by Arthur van Hoff.
 * 
 * @author Shane Walker
 */
public class Timer implements Runnable
{

	//
	// A list of TimerClients ordered by their tick times.
	//
	private class TimerClientList
	{

		//
		// A client/time pair that is an element in the list.
		//
		private class TimerEntry
		{

			TimerClient client;

			long time;

			TimerEntry next;

			TimerEntry( TimerClient client , long time )
			{
				this.client = client;
				this.time = time;
				this.next = null;
			}

			void insert( TimerEntry te )
			{
				te.next = next;
				next = te;
			}

			public String toString()
			{
				return getClass().getName() + " [client=" + client + ", time=" + time + "]";
			}
		}

		// Guarantee that there will always be one entry in the queue that
		// will never be reached.
		TimerEntry _first = new TimerEntry( null , Long.MAX_VALUE );

		//
		// Add an entry with the given client for the given time.
		//
		synchronized void insert( TimerClient client , long time )
		{
			TimerEntry te = new TimerEntry( client , time );

			// Case 1. te occurs before head.
			if( te.time < _first.time )
			{
				te.next = _first;
				_first = te;
				return;
			}

			// Case 2. te occurs somewhere in the middle of the chain.
			TimerEntry cur = _first;
			while( cur.next != null )
			{
				if( te.time <= cur.next.time )
				{
					cur.insert( te );
					return;
				}
				cur = cur.next;
			}

			// Case 3. te occurs after everything else.
			// Can't happen since there is a TimerEntry with a time of MAX_VALUE
			Assert.notNull( cur.next );
		}

		//
		// Remove the entry with the given client.
		//
		synchronized void remove( TimerClient client )
		{
			if( client == _first.client )
			{
				TimerEntry tmp = _first;
				_first = _first.next;
				tmp.next = null;
				return;
			}

			TimerEntry cur = _first;
			while( cur.next != null )
			{
				if( client == cur.next.client )
				{
					TimerEntry tmp = cur.next;
					cur.next = tmp.next;
					tmp.next = null;
					return;
				}
				cur = cur.next;
			}

			// Not found
			return;
		}

		synchronized long getFirstTime()
		{
			return _first.time;
		}

		synchronized TimerClient popClient()
		{
			if( _first.time == Long.MAX_VALUE )
				return null;

			TimerEntry te = _first;
			_first = _first.next;
			te.next = null;

			return te.client;
		}

		synchronized boolean isEmpty()
		{
			return _first.time == Long.MAX_VALUE;
		}

		synchronized void printQueue()
		{
			TimerEntry cur = _first;
			while( cur != null )
			{
				System.out.println( cur );
				cur = cur.next;
			}
		}
	}

	public static final Timer master = new Timer();

	TimerClientList _timerClientList = new TimerClientList();

	Thread _thread;

	TimerClient _curClient;

	long _curTime;

	public void addClient( TimerClient client )
	{
		addClient( client , System.currentTimeMillis() );
	}

	public void addClient( TimerClient client , long time )
	{
		_timerClientList.insert( client , time );
		_notify();
	}

	public void removeClient( TimerClient client )
	{
		_timerClientList.remove( client );
		_notify();
	}

	private synchronized void _notify()
	{
		if( _thread == null )
		{
			_thread = new Thread( this );
			_thread.start();
		}
		else
		{
			notify();
		}
	}

	private synchronized void _waitForNextTick()
	{
		_curClient = null;
		if( _timerClientList.isEmpty() )
			return;

		long now = System.currentTimeMillis();
		long next = _timerClientList.getFirstTime();
		long delay = next - now;

		while( delay > 0 )
		{
			try
			{
				wait( delay );
			}
			catch( InterruptedException ex ){}

			// Can be notify()ed when the last client is removed - in which case the client list will be empty.
			if( _timerClientList.isEmpty() )
				return;

			now = System.currentTimeMillis();
			next = _timerClientList.getFirstTime();
			delay = next - now;
		}

		_curTime = next;
		_curClient = _timerClientList.popClient();
	}

	public void run()
	{
		while( true )
		{
			_waitForNextTick();

			if( _curClient != null )
			{
				long next = _curClient.tick( _curTime );
				if( next > 0 )
					_timerClientList.insert( _curClient , next );
			}

			synchronized( this )
			{
				if( _timerClientList.isEmpty() )
				{
					_thread = null;
					System.out.println( "*** queue empty, returning" );
					return;
				}
			}
		}
	}

	public static void main( String[] args )
	{
		TimerClient tc1 = new TimerClient()
		{

			int _count = 0;

			public long tick( long time )
			{
				System.out.println( "1) Tick " + _count + " @ " + new Date( time ) );
				if( _count++ < 15 )
					return time + 1000;
				else
					return -1;
			}
		};

		TimerClient tc2 = new TimerClient()
		{

			int _count = 0;

			public long tick( long time )
			{
				System.out.println( "2) Tick " + _count + " @ " + new Date( time ) );
				if( _count++ < 5 )
					return time + 2000;
				else
					return -1;
			}
		};

		Timer.master.addClient( tc1 , System.currentTimeMillis() + 1000 );
		Timer.master.removeClient( tc1 );
		try
		{
			Thread.sleep( 5 );
		}
		catch( Exception ex ){}

		Timer.master.addClient( tc2 , System.currentTimeMillis() + 2000 );
	}
}
