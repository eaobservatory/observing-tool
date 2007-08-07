// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
/**
 * This class watches a OptionWidgetExt object to know which node is selected.
 *
 * @author      Dayle Kotturi
 * @version     1.0, 8/8/97
 */

package jsky.app.ot.gui;

/**
 * A class implements this interface if it wants to register itself
 * as the watcher of an OptionWidgetExt widget.
 */
public interface OptionWidgetWatcher
{
	/**
	 * An option was selected.
	 */
	public void optionAction( OptionWidgetExt owe );
}
