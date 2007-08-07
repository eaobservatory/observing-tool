// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.job;

import jsky.app.ot.util.LinkedList;

/**
 * The JobManager singleton continually loops picking off Jobs and
 * starting each in its own thread.
 */
public class JobManager implements Runnable
{
	/**
	 * The singleton JobManager.
	 */
	private static JobManager _instance;

	/**
	 * A dummy object that is used to insure only one JobManager is ever
	 * created.
	 */
	private static Object _lock = new Object();

	/**
	 * The list of jobs to perform.
	 */
	private LinkedList _jobQueue;

	/**
	 * Get the singleton JobManager instance.
	 */
	public static JobManager getInstance()
	{
		// Uses the double-checked lock pattern to avoid locking in almost all cases.
		if( _instance == null )
		{
			synchronized( _lock )
			{
				if( _instance == null )
				{
					// We must be the first thread to try getInstance()
					_instance = new JobManager();
					( new Thread( _instance ) ).start();
				}
			}
		}
		return _instance;
	}

	/**
	 * Add a job to the singleton JobManager.  This is a convenience method
	 * that allows clients to avoid the getInstance call if they just want
	 * to add a job to the queue.
	 *
	 * @see #addNewJob
	 */
	public static void addJob( Job job )
	{
		getInstance().addNewJob( job );
	}

	/**
	 * Private default constructor.  The singleton JobManager is created
	 * by the static getInstance method.
	 *
	 * @see #getInstance
	 */
	private JobManager()
	{
		_jobQueue = new LinkedList();
	}

	/**
	 * Add a job to the queue of jobs to run.
	 */
	public void addNewJob( Job job )
	{
		synchronized( _jobQueue )
		{
			_jobQueue.append( job );
			_jobQueue.notify(); // Only one JobManager so notifyAll not needed
		}
	}

	/**
	 * Continually loop creating workers to execute jobs.
	 */
	public void run()
	{
		while( true )
		{
			Job nextJob = null;

			synchronized( _jobQueue )
			{
				while( !_jobQueue.hasMoreElements() )
				{
					try
					{
						_jobQueue.wait();
					}
					catch( InterruptedException e )
					{
						System.out.println( "Job manager interrupted: " + e );
						return;
					}
				}
				nextJob = ( Job )_jobQueue.nextElement();
			}
			( new Thread( new Worker( nextJob ) ) ).start();
		}
	}
}
