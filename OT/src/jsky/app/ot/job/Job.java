// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.job;

/**
 * Job is an abstract base class for defining new commands or tasks from
 * the "Command" design pattern.
 */
public abstract class Job
{
	private JobWatcher _watcher;

	/**
	 * Execute the job.  Subclasses must define this method to do something
	 * useful.
	 */
	public abstract void execute();

	/**
	 * Set the JobWatcher.  JobWatcher is an abstract interface that classes
	 * implement when they are interested in knowning when a job has
	 * started/finished.
	 */
	public void setWatcher( JobWatcher watcher )
	{
		_watcher = watcher;
	}

	/**
	 * Tell the watcher the job has started.
	 */
	void notifyStarted()
	{
		if( _watcher != null )
			_watcher.jobStarted( this );
	}

	/**
	 * Tell the watcher the job has finished.
	 */
	void notifyFinished()
	{
		if( _watcher != null )
			_watcher.jobFinished( this );
	}
}
