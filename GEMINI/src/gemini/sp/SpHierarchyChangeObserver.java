// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

/**
 * An interface supported by clients of the SpEditState that wish to be notified
 * of structure changes to the program or plan. Structure changes occur when
 * items are added, removed, or moved.
 * 
 * @see SpEditState
 */
public interface SpHierarchyChangeObserver
{

	/**
     * Notification that items have been added to a node in the program.
     */
	public void spItemsAdded( SpItem parent , SpItem[] children , SpItem afterChild );

	/**
     * Notification that items have been removed from a node in the program.
     */
	public void spItemsRemoved( SpItem parent , SpItem[] children );

	/**
     * Notification that items have been moved from a node in the program to
     * another node in the program.
     */
	public void spItemsMoved( SpItem oldParent , SpItem[] children , SpItem newParent , SpItem afterChild );
}
