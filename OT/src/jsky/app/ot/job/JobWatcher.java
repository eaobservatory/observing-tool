// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.job ;

/**
 * The JobWatcher interface is implemented by classes that want to know
 * when their jobs have started and finished.
 */
public interface JobWatcher
{
	public void jobStarted( Job job ) ;

	public void jobFinished( Job job ) ;
}
