// Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

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
