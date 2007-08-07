// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.util;

/**
 * An interface supported by applications that are "closeable".
 */
public interface CloseableApp
{
	/** Close the application, returning true if successful. */
	public boolean closeApp();
}
