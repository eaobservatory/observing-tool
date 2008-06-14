// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui ;

/**
 * An interface supported by clients that which to be notified of
 * DropDownListBoxWidget selection and action (double click).
 */
public interface DropDownListBoxWidgetWatcher
{
	/**
	 * Called when an item is selected.
	 */
	public void dropDownListBoxSelect( DropDownListBoxWidgetExt ddlbwe , int index , String val ) ;

	/**
	 * Called when an item is double clicked.
	 */
	public void dropDownListBoxAction( DropDownListBoxWidgetExt ddlbwe , int index , String val ) ;
}
