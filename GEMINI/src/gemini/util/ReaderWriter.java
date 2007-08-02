// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util;

/**
 * A simple reader/writer locking pattern base class.
 */
public class ReaderWriter
{

	protected int _activeReaders = 0;
	protected int _activeWriters = 0;
	protected int _waitingReaders = 0;
	protected int _waitingWriters = 0;

	//
	// Check whether it is ok to read. Multiple readers are permitted.
	//
	private boolean _allowReader()
	{
		return ( _waitingWriters == 0 ) && ( _activeWriters == 0 );
	}

	//
	// Check whether it is ok to write. Only one writer, with no simultaneous
	// readers are permitted.
	//
	private boolean _allowWriter()
	{
		return ( _activeReaders == 0 ) && ( _activeWriters == 0 );
	}

	/**
     * Block until it is okay to read. Multiple readers are permitted so long as
     * there are no writers.
     */
	public synchronized void getReadPermission()
	{
		++_waitingReaders;
		while( !_allowReader() )
		{
			try
			{
				wait();
			}
			catch( InterruptedException ex ){}
		}
		--_waitingReaders;
		++_activeReaders;
	}

	/**
     * Notify that the read is over.
     */
	public synchronized void returnReadPermission()
	{
		--_activeReaders;
		notifyAll();
	}

	/**
     * Block until it is okay to write. Only one writer at a time is permitted.
     */
	public synchronized void getWritePermission()
	{
		++_waitingWriters;
		while( !_allowWriter() )
		{
			try
			{
				wait();
			}
			catch( InterruptedException ex ){}
		}
		--_waitingWriters;
		++_activeWriters;
	}

	/**
     * Notify that the write is over.
     */
	public synchronized void returnWritePermission()
	{
		--_activeWriters;
		notifyAll();
	}
}
