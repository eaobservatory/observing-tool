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
//
// $Id$
//
package jsky.app.ot.gui ;

import java.util.Vector ;
import javax.swing.tree.TreeCellRenderer ;
import javax.swing.tree.TreeNode ;
import javax.swing.tree.TreePath ;
import javax.swing.tree.TreeSelectionModel ;
import javax.swing.JFrame ;
import javax.swing.JTree ;
import java.awt.Component ;
import java.awt.Dimension ;
import java.awt.event.MouseListener ;
import java.awt.event.MouseEvent ;
import jsky.util.gui.BasicWindowMonitor ;

/**
 * A TreeWidget extension that supports multiselection as an option.
 * Old: The help of MultiSelTreeNodeWidget is required for this.
 *
 * MFO: The faciltiy of applying to drag and drop to multiple nodes has been restored but
 *      by using completely new fields and methods:
 *        <P>{@link #multipleSelection}
 *        <P>{@link #cellRenderer}
 *        <P>{@link #getSelectionPaths()}
 *        <P>{@link #mouseReleased(java.awt.event.MouseEvent)})
 *
 *      MultiSelection now uses the default behaviour of a JTree with
 *      TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION. So most of the old fields and
 *      methods might not be needed anymore.
 * 
 *
 * @see MultiSelTreeNodeWidget
 * @author      Shane Walker, Allan Brighton (Swing port), Martin Folger (Multi Drag and Drop)
 */
@SuppressWarnings( "serial" )
public class MultiSelTreeWidget extends TreeWidgetExt implements TreeCellRenderer , MouseListener
{
	/**
	 * Used for moving and copying multiple components/nodes.
	 *
	 * This field retains a reference to the latest multiple selection after
	 * after dragging and dropping has started by clicking on a single node of
	 * the multiple selection. JTree.getSelectionPaths() would only return the
	 * path the single node currently dragged.
	 *
	 * MFO: May 28, 2001
	 */
	protected TreePath[] multipleSelection = null ;

	/**
	 * Reference to the TreeWidgetCellRenderer of {@link #tree}.
	 *
	 * It is needed by this class to implement its own getTreeCellRendererComponent method.
	 * MFO: May 28, 2001
	 */
	protected TreeWidgetCellRenderer cellRenderer = null ;

	public MultiSelTreeWidget()
	{
		super() ;

		// Get hold of the cell renderer.
		// MFO: May 28, 2001
		cellRenderer = ( TreeWidgetCellRenderer )tree.getCellRenderer() ;

		// Set cell renderer of tree to this class.
		// MFO: May 28, 2001
		tree.setCellRenderer( this ) ;

		tree.addMouseListener( this ) ;
	}

	/**
	 * Get the multiSelect property.
	 */
	public boolean getMultiSelect()
	{
		return( tree.getSelectionModel().getSelectionMode() == TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION ) ;
	}

	/**
	 * Set whether this tree should use multi-selection.
	 */
	public void setMultiSelect( boolean ms )
	{
		if( ms )
			tree.getSelectionModel().setSelectionMode( TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION ) ;
		else
			tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION ) ;
	}

	/**
	 * Turn off the multi-selection.  This doesn't disable the feature,
	 * it just unselects the widgets.
	 */
	public void multiUnselect(){}

	/**
	 * Determine whether multiple items are selected.
	 */
	public synchronized boolean multipleItemsSelected()
	{
		return( tree.getSelectionCount() > 1 ) ;
	}

	/**
	 * Get a vector containing the selected tree nodes.
	 */
	public Vector<Object> getMultiSelectNodes()
	{
		TreePath[] paths = tree.getSelectionPaths() ;
		Vector<Object> v = null ;
		if( paths != null )
		{
			v = new Vector<Object>( paths.length , 1 ) ;
			for( int i = 0 ; i < paths.length ; i++ )
				v.add( i , paths[ i ].getLastPathComponent() ) ;
		}
		return v ;
	}

	/** Notify that multiple nodes have been selected.  */
	protected void notifyMultiSelect()
	{
		Vector<TreeWidgetWatcher> v = getWatchers() ;
		int cnt = v.size() ;
		for( int i = 0 ; i < cnt ; ++i )
		{
			Object o = v.elementAt( i ) ;
			if( !( o instanceof MultiSelTreeWidgetWatcher ) )
				continue ;
			MultiSelTreeWidgetWatcher mstww = ( MultiSelTreeWidgetWatcher )o ;
			mstww.multiNodeSelect( this , getMultiSelectNodes() ) ;
		}
	}

	/**
	 * Set the multi-select region so that it encompasses the
	 * MultiSelTreeNodeWidgets in the given array.  For now, this only works
	 * if the widgets are all siblings.
	 */
	public void setMultiSelectNodes( MultiSelTreeNodeWidget[] tnwA )
	{
		if( tnwA.length <= 1 )
			return ;

		// Make sure that they all have the same parent
		TreePath[] paths = new TreePath[ tnwA.length ] ;
		for( int i = 1 ; i < tnwA.length ; i++ )
		{
			if( tnwA[ i ].getParent() != tnwA[ i - 1 ].getParent() )
				return ;
			paths[ i ] = new TreePath( tnwA[ i ].getPath() ) ;
		}

		// Reset the selection
		tree.clearSelection() ;
		tree.setSelectionPaths( paths ) ;
	}

	/**
	 * test main: usage: java SpTree
	 * (Only tests the basic layout).
	 */
	public static void main( String[] args )
	{
		JFrame frame = new JFrame( "MultiSelTreeWidget" ) ;
		MultiSelTreeWidget tree = new MultiSelTreeWidget() ;
		tree.setPreferredSize( new Dimension( 360 , 400 ) ) ;
		frame.add( "Center" , tree ) ;
		frame.pack() ;
		frame.setVisible( true ) ;
		frame.addWindowListener( new BasicWindowMonitor() ) ;
	}

	public TreePath[] getSelectionPaths()
	{
		TreePath[] allPaths ;
		TreePath[] result ;
		TreePath pointer ;
		Vector<TreePath> resultVector = new Vector<TreePath>() ;

		if( multipleSelection != null )
			allPaths = multipleSelection ;
		else
			allPaths = tree.getSelectionPaths() ;

		if( allPaths == null )
			return null ;

		boolean hasAncesterAmongPaths = false ;
		for( int i = 0 ; i < allPaths.length ; i++ )
		{
			for( int j = 0 ; j < allPaths.length ; j++ )
			{
				pointer = allPaths[ i ].getParentPath() ;
				while( pointer != null )
				{
					if( pointer == allPaths[ j ] )
					{
						hasAncesterAmongPaths = true ;
						break ;
					}
					else
					{
						pointer = pointer.getParentPath() ;
					}
				}

				if( hasAncesterAmongPaths )
					break ;
			}
			if( !hasAncesterAmongPaths )
				resultVector.add( allPaths[ i ] ) ;
		}

		result = new TreePath[ resultVector.size() ] ;

		for( int i = 0 ; i < result.length ; i++ )
			result[ i ] = resultVector.get( i ) ;

		return result ;
	}

	protected boolean isInMultipleSelection( TreeNode node )
	{
		if( multipleSelection == null )
			return false ;

		for( int i = 0 ; i < multipleSelection.length ; i++ )
		{
			if( multipleSelection[ i ].getLastPathComponent() == node )
				return true ;
		}

		return false ;
	}

	/**
	 * Renders according to whether there is a multiple selection and whether the node is part of it.
	 * 
	 * MFO: May 28, 2001
	 */
	public Component getTreeCellRendererComponent( JTree tree , Object value , boolean selected , boolean expanded , boolean leaf , int row , boolean hasFocus )
	{
		// If there is a multiple selection get the Component from cellRenderer and the node is part fo it then "pretend" 
		// that the node is properly selected even if it is not among tree.getSelectionPaths(). 
		// (Note that only the nodes of the paths returned by tree.getSelectionPaths() are normally rendered as selected nodes.
		// If there is no multiple selection just pass all parameters on and
		// return the result of the cellRenderer.getTreeCellRendererComponent.
		if( multipleSelection != null )
			return cellRenderer.getTreeCellRendererComponent( tree , value , isInMultipleSelection( ( TreeNode )value ) , expanded , leaf , row , hasFocus ) ;
		else
			return cellRenderer.getTreeCellRendererComponent( tree , value , selected , expanded , leaf , row , hasFocus ) ;
	}

	// MFO: May 28, 2001
	public void mouseClicked( MouseEvent e ){}

	public void mouseEntered( MouseEvent e ){}

	public void mouseExited( MouseEvent e ){}

	public void mousePressed( MouseEvent e ){}

	public void mouseReleased( MouseEvent e )
	{
		if( tree.getSelectionCount() > 1 )
			multipleSelection = tree.getSelectionPaths() ;
		else
			multipleSelection = null ;
		tree.repaint() ;
	}
}
