// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui ;

/**
 * This interface should be supported by objects that are interrested in
 * being notified of selection, and action changes.
 * Watchers register themselves with the TreeWidgetExt and are then
 * informed of any of the above events involving TreeNodeWidgetExt items
 * within the tree.  Notifications are <i>not</i> sent for ordinary
 * TreeNodeWidgets and other widgets within the tree.
 *
 * @see TreeWidgetExt
 * @see TreeNodeWidgetExt
 */
public interface TreeWidgetWatcher
{
	/**
	 * Receive notification that a node is selected.
	 */
	public void nodeSelected( TreeWidgetExt tw , TreeNodeWidgetExt tnw ) ;

	/**
	 * Receive notification that a node has been acted upon.
	 */
	public void nodeAction( TreeWidgetExt tw , TreeNodeWidgetExt tnw ) ;
}
