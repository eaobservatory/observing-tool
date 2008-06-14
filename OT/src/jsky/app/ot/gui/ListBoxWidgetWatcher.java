// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui ;

/**
 * An interface supported by clients that which to be notified of
 * ListBoxWidget selection and action (double click).
 */
public interface ListBoxWidgetWatcher
{
	/**
	 * Called when an item is selected.
	 */
	public void listBoxSelect( ListBoxWidgetExt lbwe , int index , String val ) ;

	/**
	 * Called when an item is double clicked.
	 */
	public void listBoxAction( ListBoxWidgetExt lbwe , int index , String val ) ;
}
