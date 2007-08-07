// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
//
package jsky.app.ot;

import gemini.sp.SpItem;
import java.util.Observable;

/**
 * A TreeNodeWidget for SpObs items.  A special OtTreeNodeWidget is
 * required for two reasons:
 * <ul>
 * <li> to handle CTRL-click events that toggle the "chained-to-next"
 *      state of the observation, and
 * <li> to handle the creation of SpObsLinks when CTRL-dragging an
 *      observation.
 * </ul>
 */
public class OtObsTreeNodeWidget extends OtTreeNodeWidget
{
	public OtObsTreeNodeWidget(){}

	public OtObsTreeNodeWidget( OtTreeWidget tree )
	{
		super( tree );
	}

	/**
	 * Implement copy() to make this class concrete.
	 */
	public OtTreeNodeWidget copy()
	{
		OtObsTreeNodeWidget newTNW = new OtObsTreeNodeWidget( ( OtTreeWidget )tree );
		super.copyInto( newTNW );
		return newTNW;
	}

	/**
	 * Set the Science Program item to edit.  This method should be
	 * called once, before the object is used.
	 */
	public void setItem( SpItem spItem )
	{
		super.setItem( spItem );
	}

	/**
	 * Get the chained state of the observation associated with this node.
	 */
	public boolean getObsChained()
	{
		return false;
	}

	/**
	 * Set the chained state of the observation associated with this node.
	 */
	public void setObsChained( boolean chained ){}

	/**
	 * Observer of chain state changes.  Note that the superclass,
	 * OtTreeNodeWidget implements Observable, we are simply overriding
	 * its definition.
	 */
	public void update( Observable o , Object arg )
	{
		super.update( o , arg );
	}
}
