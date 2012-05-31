// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui ;

/**
 * An interface supported by clients that which to be notified of
 * DropDownListBoxWidget action (menu selection)
 */
public interface DropDownListBoxWidgetWatcher
{
	/**
	 * Called when an item is selected.
         *
         * Note: in recent version of Java, this is not fired when the
         * already-selected combobox entry is clicked.  This is because
         * a combobox is not meant to be misused as a command menu.
	 */
	public void dropDownListBoxAction( DropDownListBoxWidgetExt ddlbwe , int index , String val ) ;
}
