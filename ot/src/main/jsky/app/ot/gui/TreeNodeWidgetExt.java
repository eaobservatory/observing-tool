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

import gemini.util.ObservingToolUtilities ;

import java.awt.Font ;
import java.util.Vector ;
import javax.swing.ImageIcon ;
import javax.swing.tree.DefaultTreeModel ;
import javax.swing.tree.TreeNode ;
import javax.swing.tree.TreePath ;
import javax.swing.tree.DefaultMutableTreeNode ;

import java.net.URL ;

/**
 * A TreeNodeWidget extension.
 *
 * @author	Shane Walker, Allan Brighton (ported from Bongo to Swing)
 */
@SuppressWarnings( "serial" )
public class TreeNodeWidgetExt extends DefaultMutableTreeNode
{
	/** The tree to which this node belongs */
	protected TreeWidgetExt tree ;

	/** url string of the image for this node */
	protected String src ;

	/** The icon for the tree node. */
	protected ImageIcon icon ;

	/** url string of the image for this node when it is expanded */
	protected String expandSrc ;

	/** The icon for the tree node when it is expanded. */
	protected ImageIcon expandIcon ;

	/** text displayed by the tree node */
	protected String label ;

	/** Font for text displayed by the tree node */
	protected Font font ;

	/** Constructor (need to call setTree(TreeNodeWidgetExt) later).  */
	public TreeNodeWidgetExt(){}

	/** Constructor.  */
	public TreeNodeWidgetExt( TreeWidgetExt tree )
	{
		setTree( tree ) ;
	}

	/** Constructor with a label. Used by subclasses of TreeNodeWidgetExt.  */
	public TreeNodeWidgetExt( TreeWidgetExt tree , String label )
	{
		super( label ) ;
		setText( label ) ;
		setTree( tree ) ;
	}

	/** Set the tree that this node belongs to (required). */
	public void setTree( TreeWidgetExt tree )
	{
		this.tree = tree ;
	}

	/** Return the string representation of this node */
	public String toString()
	{
		return label ;
	}

	/**
	 * This copyInto method is provied to help support cloning objects
	 */
	public void copyInto( TreeNodeWidgetExt newTNW )
	{
		newTNW.setSrc( getSrc() ) ;
		newTNW.setExpandSrc( getExpandSrc() ) ;
		newTNW.setText( getText() ) ;
	}

	/** Show or hide the subtree starting at this node */
	public void setCollapsed( boolean collapsed )
	{
		if( tree != null )
		{
			TreePath path = new TreePath( getPath() ) ;
			if( collapsed )
				tree.getTree().collapsePath( path ) ;
			else
				tree.getTree().expandPath( path ) ;
	
			tree.getTree().repaint() ;
		}
	}

	/** Return true if the subtree starting at this node is collapsed. */
	public boolean isCollapsed()
	{
		if( tree != null )
			return tree.getTree().isCollapsed( new TreePath( getPath() ) ) ;
		return false ;
	}

	/** 
	 * Set the url string of the image for this node.
	 */
	public void setSrc( String src )
	{
		this.src = src ;
		URL url = ObservingToolUtilities.resourceURL( "jsky/app/ot/" + src ) ;
		icon = new ImageIcon( url ) ;
		if( icon.getImageLoadStatus() == java.awt.MediaTracker.ERRORED )
			icon = new ImageIcon( ObservingToolUtilities.resourceURL( src ) ) ;
	}

	/** 
	 * Return the url string of the image for this node.
	 */
	public String getSrc()
	{
		return src ;
	}

	/** 
	 * Return the icon image for this node.
	 */
	public ImageIcon getIcon()
	{
		return icon ;
	}

	/** Remove an icon */
	public void rmIcons()
	{
		icon = null ;
		expandIcon = null ;
	}

	/** 
	 * Set the url of the image for this node when it is expanded.
	 */
	public void setExpandSrc( String expandSrc )
	{
		this.expandSrc = expandSrc ;
		URL url = ObservingToolUtilities.resourceURL( "jsky/app/ot/" + expandSrc ) ;
		expandIcon = new ImageIcon( url ) ;
		if( expandIcon.getImageLoadStatus() == java.awt.MediaTracker.ERRORED )
			expandIcon = new ImageIcon( ObservingToolUtilities.resourceURL( expandSrc ) ) ;
	}

	/** 
	 * Return the url string of the image for this node when it is expanded.
	 */
	public String getExpandSrc()
	{
		return expandSrc ;
	}

	/** 
	 * Return the icon image for this node when it is expanded.
	 */
	public ImageIcon getExpandIcon()
	{
		return expandIcon ;
	}

	/** Set the text displayed by the tree node */
	public void setText( String label )
	{
		this.label = label ;
		if( tree != null )
		{
			// this is needed to make the change appear immediately
			DefaultTreeModel treeModel = ( DefaultTreeModel )tree.getTree().getModel() ;
			treeModel.nodeChanged( this ) ;
		}
	}

	/** Return the text displayed by the tree node */
	public String getText()
	{
		return label ;
	}

	/** Set the font for the text displayed by the tree node */
	public void setFont( Font font )
	{
		this.font = font ;
		if( tree != null )
			tree.getTree().repaint() ;
	}

	/** Return the font for the text displayed by the tree node */
	public Font getFont()
	{
		return font ;
	}

	/**
	 * Add a child at the given position. 
	 */
	public void add( int pos , TreeNodeWidgetExt node )
	{
		TreeNodeWidgetExt selectedNode = tree.getSelectedNode() ;
		DefaultTreeModel treeModel = ( DefaultTreeModel )tree.getTree().getModel() ;
		treeModel.insertNodeInto( node , this , pos ) ;
		if( selectedNode != null )
			selectedNode.select() ;
	}

	/** Select this node */
	public void select()
	{
		setSelected( true ) ;
	}

	/** Return the number of subnodes (for compat with bongo) */
	public int getWidgetCount()
	{
		return getChildCount() ;
	}

	/**
	 * Select a tree node and notify the watcher.
	 */
	public void setSelected( boolean selected )
	{
		if( tree.getSelectedNode() != this )
			tree.selectNode( this ) ;
	}

	/**
	 * Show this node, expanding ancestors if necessary.
	 */
	public void uncover()
	{
		tree.getTree().makeVisible( new TreePath( getPath() ) ) ;
	}

	/**
	 * Expand this tree node.
	 */
	public void expand()
	{
		tree.getTree().expandPath( new TreePath( getPath() ) ) ;
	}

	/**
	 * Remove this tree node.
	 */
	public void remove()
	{
		DefaultTreeModel treeModel = ( DefaultTreeModel )tree.getTree().getModel() ;
		treeModel.removeNodeFromParent( this ) ;
	}

	/**
	 * Return the selected node under this subtree, or null if there is no selection there.
	 */
	public TreeNodeWidgetExt getSelectedNode()
	{
		TreeNodeWidgetExt node = tree.getSelectedNode() ;
		TreePath path = new TreePath( node.getPath() ) ;
		if( path.isDescendant( new TreePath( getPath() ) ) )
			return node ;

		return null ;
	}

	/**
	 * Return a vector containing the child nodes of this node.
	 */
	public Vector<TreeNode> getChildren()
	{
		int n = getChildCount() ;
		Vector<TreeNode> v = new Vector<TreeNode>( n ) ;
		for( int i = 0 ; i < n ; i++ )
			v.add( getChildAt( i ) ) ;
		return v ;
	}

	/**
	 * Get the index of the given child, or -1 if the given widget isn't a child
	 * of this node.
	 */
	public final int getIndexOf( TreeNodeWidgetExt node )
	{
		return getIndex( node ) ;
	}

	/**
	 * Get my index in my parent's array of widgets.
	 */
	public final int getMyIndex()
	{
		return ( ( TreeNodeWidgetExt )getParent() ).getIndexOf( this ) ;
	}

	/** Return the tree widget containing this node */
	public TreeWidgetExt getTreeWidget()
	{
		return tree ;
	}

	/**
	 * Set both the regular image source and the expanded image source in
	 * one method call.  This is usefull when you don't want the image to
	 * change based upon whether or not it is expanded.
	 **/
	public void setBothImageSrc( String imgSrc )
	{
		setSrc( imgSrc ) ;
		setExpandSrc( imgSrc ) ;
	}
}
