// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe ;

/**
 * An interface supported by clients of the TelescopePosEditor who want
 * to be informed of when the editor closes.
 */
public interface TpeWatcher
{
	/** The position editor has been shutdown, closed. */
	public void tpeClosed( TelescopePosEditor tpe ) ;
}
