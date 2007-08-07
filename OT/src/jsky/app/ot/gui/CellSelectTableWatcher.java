// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui;

/**
 * The interface to be supported by CellSelectTableWidget clients that
 * want to be informed of when cells are selected and actioned.
 *
 * @see CellSelectTableWidget
 */
public interface CellSelectTableWatcher
{
	/**
	 * The given cell was selected.
	 */
	public void cellSelected( CellSelectTableWidget w , int colIndex , int rowIndex );
}
