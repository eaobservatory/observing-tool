// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui;

/**
 * A client implements this interface if it wants to register itself
 * as the watcher of the StopActionWidget.
 *
 * @see StopActionWidget
 * @author Shane Walker
 */
public interface StopActionWatcher
{
	/**
	 * The stop button was pressed.
	 */
	public void stopAction( StopActionWidget saw );
}
