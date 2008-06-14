// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui ;

/**
 * This interface should be supported by objects that are interrested in
 * being notified of the selection and action TreeNodeWidgetExt events.
 *
 * @see TreeNodeWidgetExt
 */
public interface TreeNodeWidgetWatcher
{
	/**
	 * Receive notification that a node is selected.
	 */
	public void nodeSelected( TreeNodeWidgetExt tnw ) ;

	/**
	 * Receive notification that a node has been acted upon.
	 */
	public void nodeAction( TreeNodeWidgetExt tnw ) ;
}
