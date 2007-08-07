// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui;

/**
 * A class implements this interface if it wants to register itself
 * as the watcher of key presses (and action events) in a TextBoxWidgetExt
 * widget.
 *
 * @author	Shane Walker
 */
public interface TextBoxWidgetWatcher
{
	/**
	 * A key was pressed in the given TextBoxWidgetExt.
	 */
	public void textBoxKeyPress( TextBoxWidgetExt tbwe );

	/**
	 * A return key was pressed in the given TextBoxWidgetExt.
	 */
	public void textBoxAction( TextBoxWidgetExt tbwe );
}
