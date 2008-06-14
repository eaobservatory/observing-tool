// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui ;

/**
 * An interface supported by clients that which to be notified of
 * TableWidget selection and action (double click).
 */
public interface TableWidgetWatcher
{
	/**
	 * Called when a row is selected.
	 */
	public void tableRowSelected( TableWidgetExt twe , int rowIndex ) ;

	/**
	 * Called when a row is double clicked (or return key is pressed).
	 */
	public void tableAction( TableWidgetExt twe , int colIndex , int rowIndex ) ;
}
