// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.job ;

import java.awt.Event ;

/**
 * An custom event that indicates a widget has some potentially long
 * running action that must be performed.  JobEvents contain a reference
 * to a Job, which is an abstract class following the "Command" class
 * of the Command Pattern.
 */
public class JobEvent extends Event
{
	private Job _job ;

	/**
	 * Construct from a Job.
	 */
	public JobEvent( Object target , Job job )
	{
		super( target , -1 , null ) ;
		// -1 NOT USED BY AWT EVENTS
		_job = job ;
	}

	/**
	 * Construct from an existing java Event and a Job.
	 */
	public JobEvent( Object target , Event evt , Job job )
	{
		super( target , evt.when , evt.id , evt.x , evt.y , evt.key , evt.modifiers , evt.arg ) ;
		_job = job ;
		id = -1 ; // NOT USED BY AWT EVENTS
	}

	/**
	 * Get the Job associated with this event.
	 */
	public Job getJob()
	{
		return _job ;
	}
}
