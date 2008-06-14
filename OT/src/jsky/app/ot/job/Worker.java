// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.job ;

/**
 * Workers have a one-to-one relationship with Jobs.  They implement
 * Runnable to execute the Job, notifying it before and after.
 */
class Worker implements Runnable
{
	private Job _job ;

	/**
	 */
	public Worker( Job job )
	{
		_job = job ;
	}

	/**
	 */
	public void run()
	{
		_job.notifyStarted() ;
		_job.execute() ;
		_job.notifyFinished() ;
	}
}
