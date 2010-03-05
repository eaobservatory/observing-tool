// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui ;

import java.util.Vector ;

/**
 * A TreeNodeWidget extension that supports multi-selection.  It is
 * meant to be used in conjunction with the MultiSelTreeWidget.
 *
 * @see MultiSelTreeWidget
 * @author	Shane Walker
 */
@SuppressWarnings( "serial" )
public class MultiSelTreeNodeWidget extends TreeNodeWidgetExt
{
	/** Default constructor (need to call setTree(TreeNodeWidgetExt) later). */
	public MultiSelTreeNodeWidget(){}

	/** Constructor.  */
	public MultiSelTreeNodeWidget( MultiSelTreeWidget tree )
	{
		super( tree ) ;
	}

	/** Constructor with a label. Used by subclasses of TreeNodeWidgetExt.  */
	public MultiSelTreeNodeWidget( MultiSelTreeWidget tree , String label )
	{
		super( tree , label ) ;
	}

	/**
	 * If this node is part of a MultiSelTreeWidget, return the
	 * MultiSelTreeWidget.  If not, return null.
	 */
	public MultiSelTreeWidget getMultiSelTreeWidget()
	{
		return ( MultiSelTreeWidget )getTreeWidget() ;
	}

	/**
	 * Override this method to turn off the multi-selection.
	 */
	public void add( int pos , TreeNodeWidgetExt tnw )
	{
		getMultiSelTreeWidget().multiUnselect() ;
		super.add( pos , tnw ) ;
	}

	/**
	 * Override select to turn off the multi-selection.
	 */
	public void setSelected( boolean selected )
	{
		if( tree.getSelectedNode() != this )
		{
			if( tree.getSelectedNode() != null )
			{
				// Turn off the multi-select
				if( !isMultiSelected() )
					getMultiSelTreeWidget().multiUnselect() ;
			}
			super.setSelected( selected ) ;
		}
	}

	/**
	 * Override action to turn off the multi-selection.
	 */
	public void action()
	{
		// Turn off the multi-select
		getMultiSelTreeWidget().multiUnselect() ;
		super.action() ;
	}

	/**
	 * Determine whether multiple items are selected in the tree to
	 * which this item belongs.
	 */
	public synchronized boolean multipleItemsSelected()
	{
		return getMultiSelTreeWidget().multipleItemsSelected() ;
	}

	/**
	 * Return true if part of the multi-select group.
	 */
	public boolean isMultiSelected()
	{
		if( multipleItemsSelected() )
		{
			Vector<Object> v = getMultiSelTreeWidget().getMultiSelectNodes() ;
			int n = v.size() ;
			for( int i = 0 ; i < n ; i++ )
			{
				if( v.get( i ) == this )
					return true ;
			}
		}
		return false ;
	}
}
