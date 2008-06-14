// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//

package jsky.app.ot.util ;

/**
 * An interface supported by clients of TelescopePosList who want to
 * be notified when the list changes in some way.
 */
public interface TelescopePosListWatcher
{
	/**
	 * The list has been reset, or changed so much that the client should
	 * start from scratch.
	 */
	public void posListReset( TelescopePosList tpl , TelescopePos[] newList ) ;

	/**
	 * The list has been rearranged, so the new order is passed in along
	 * with the element that changed (this may be null).
	 */
	public void posListReordered( TelescopePosList tpl , TelescopePos[] newList , TelescopePos tp ) ;

	/**
	 * A position has been added to the list. 
	 */
	public void posListAddedPosition( TelescopePosList tpl , TelescopePos newPos ) ;

	/**
	 * A position has been removed from the list. 
	 */
	public void posListRemovedPosition( TelescopePosList tpl , TelescopePos rmPos ) ;
}
