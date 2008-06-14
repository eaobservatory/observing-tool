// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui ;

import java.awt.GridLayout ;
import java.awt.BorderLayout ;
import java.awt.Dimension ;
import java.awt.event.MouseAdapter ;
import java.awt.event.MouseEvent ;
import java.lang.reflect.Method ;
import java.util.Vector ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.JTree ;
import javax.swing.JFrame ;
import javax.swing.event.TreeSelectionListener ;
import javax.swing.event.TreeSelectionEvent ;
import javax.swing.tree.DefaultTreeModel ;
import javax.swing.tree.TreePath ;
import javax.swing.tree.TreeSelectionModel ;
import jsky.util.gui.BasicWindowMonitor ;
import ot.util.DialogUtil ;

/**
 * A tree widget that supports selection observers.
 *
 * @author Shane Walker, Allan Brighton (ported to Swing)
 */
public class TreeWidgetExt extends JPanel
{
	/** The tree */
	protected AutoscrollTree tree ;

	/** The scroll pane containing the tree */
	protected JScrollPane scrollPane ;

	/** The tree model */
	protected DefaultTreeModel treeModel ;

	/** The root node of the tree */
	protected TreeNodeWidgetExt rootNode ;

	/** The last selected node in the tree */
	protected TreeNodeWidgetExt selectedNode ;

	// The watchers.
	protected Vector<TreeWidgetWatcher> watchers ;

	// if true, ignore node selections
	boolean ignoreSelection = false ;

	/** Default constructor */
	public TreeWidgetExt()
	{
		rootNode = new TreeNodeWidgetExt( this ) ;
		treeModel = new DefaultTreeModel( rootNode ) ;
		tree = new AutoscrollTree( treeModel ) ;
		tree.setBackground( getBackground() ) ;
		tree.getSelectionModel().setSelectionMode( TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION ) ;
		tree.setShowsRootHandles( true ) ;
		tree.setCellRenderer( new TreeWidgetCellRenderer() ) ;

		// disable double-click (is there an easier way to do this?)
		try
		{
			Method m = tree.getClass().getMethod( "setToggleClickCount" , new Class[] { int.class } ) ;
			m.invoke( tree , new Object[] { new Integer( 3 ) } ) ;
		}
		catch( Exception e )
		{
			throw new RuntimeException( e.toString() ) ;
		}

		scrollPane = new JScrollPane( tree ) ;
		JPanel panel = new JPanel() ;
		panel.setLayout( new GridLayout( 1 , 0 ) ) ;
		panel.add( scrollPane ) ;
		setLayout( new BorderLayout() ) ;
		add( "Center" , panel ) ;

		// Listen for when the selection changes.
		tree.addTreeSelectionListener( new TreeSelectionListener()
		{
			public void valueChanged( TreeSelectionEvent e )
			{
				if( ignoreSelection )
					return ;
				try
				{
					nodeSelected() ;
				}
				catch( Exception ex )
				{
					DialogUtil.error( TreeWidgetExt.this , ex ) ;
				}
			}
		} ) ;

		// Listen for double-click on a tree node
		tree.addMouseListener( new MouseAdapter()
		{
			public void mousePressed( MouseEvent e )
			{
				int selRow = tree.getRowForLocation( e.getX() , e.getY() ) ;
				if( selRow != -1 )
				{
					if( e.getClickCount() == 2 )
						nodeDoubleClicked() ;
				}
			}
		} ) ;
	}

	/**
	 * This method is called when a tree node is selected 
	 */
	protected void nodeSelected()
	{
		TreeNodeWidgetExt selectedNode = getSelectedNode() ;
		if( selectedNode == null )
			return ;

		notifySelect( selectedNode ) ;
	}

	/**
	 * This method is called when the user double clicks on a tree node
	 */
	protected void nodeDoubleClicked()
	{
		TreeNodeWidgetExt selectedNode = getSelectedNode() ;
		if( selectedNode == null )
			return ;

		notifyAction( selectedNode ) ;
	}

	/** Return the currently selected tree node */
	public TreeNodeWidgetExt getSelectedNode()
	{
		return ( TreeNodeWidgetExt )tree.getLastSelectedPathComponent() ;
	}

	/** Select the given tree node */
	public void selectNode( TreeNodeWidgetExt node )
	{
		try
		{
			tree.setSelectionPath( new TreePath( node.getPath() ) ) ;
		}
		catch( Exception e )
		{
			DialogUtil.error( this , "warning: could not select tree node" ) ;
		}
	}

	/** If ignore is true, ignore node selections. */
	public void setIgnoreSelection( boolean ignore )
	{
		ignoreSelection = ignore ;
	}

	/** Return the internal JTree */
	public JTree getTree()
	{
		return tree ;
	}

	/** Add a node to the tree, which will become the root node. */
	public void add( TreeNodeWidgetExt node )
	{
		rootNode = node ;
		treeModel = new DefaultTreeModel( node ) ;
		tree.setModel( treeModel ) ;
	}

	/** Return the root node. */
	public TreeNodeWidgetExt getRoot()
	{
		return rootNode ;
	}

	/** 
	 * Clear the tree, removing all tree nodes.
	 */
	public void clear()
	{
		rootNode.removeAllChildren() ;
	}

	/** Add a watcher.  */
	public synchronized final void addWatcher( TreeWidgetWatcher watcher )
	{
		if( watchers == null )
			watchers = new Vector<TreeWidgetWatcher>() ;
		
		if( !watchers.contains( watcher ) )
			watchers.addElement( watcher ) ;
	}

	/** Delete a watcher.  */
	public synchronized final void deleteWatcher( TreeWidgetWatcher watcher )
	{
		if( watchers != null )
			watchers.removeElement( watcher ) ;
	}

	/** Delete all watchers.  */
	public synchronized final void deleteWatchers()
	{
		if( watchers != null )
			watchers.removeAllElements() ;
	}

	/** Get a copy of the current list of watchers */
	public Vector getWatchers()
	{
		synchronized( this )
		{
			if( watchers == null )
				watchers = new Vector<TreeWidgetWatcher>() ;
			return ( Vector )watchers.clone() ;
		}
	}

	/** Notify that a node was selected. */
	void notifySelect( TreeNodeWidgetExt tnw )
	{
		Vector v = getWatchers() ;
		int cnt = v.size() ;
		for( int i = 0 ; i < cnt ; ++i )
		{
			TreeWidgetWatcher tww = ( TreeWidgetWatcher )v.elementAt( i ) ;
			tww.nodeSelected( this , tnw ) ;
		}
		tnw.notifySelect() ;
	}

	/** Notify that a node was double-clicked. */
	void notifyAction( TreeNodeWidgetExt tnw )
	{
		Vector v = getWatchers() ;
		int cnt = v.size() ;
		for( int i = 0 ; i < cnt ; ++i )
		{
			TreeWidgetWatcher tww = ( TreeWidgetWatcher )v.elementAt( i ) ;
			tww.nodeAction( this , tnw ) ;
		}
		tnw.notifyAction() ;
	}

	/**
	 * test main: usage: java SpTree
	 * (Only tests the basic layout).
	 */
	public static void main( String[] args )
	{
		JFrame frame = new JFrame( "TreeWidgetExt" ) ;
		final TreeWidgetExt tree = new TreeWidgetExt() ;
		tree.setPreferredSize( new Dimension( 360 , 400 ) ) ;

		TreeNodeWidgetExt root = new TreeNodeWidgetExt( tree , "root" ) ;
		root.setBothImageSrc( "images/component.gif" ) ;
		tree.add( root ) ;
		TreeNodeWidgetExt node ;
		TreeNodeWidgetExt node2 ;
		root.add( node = new TreeNodeWidgetExt( tree , "one" ) ) ;
		node.setBothImageSrc( "images/component.gif" ) ;
		root.add( node = new TreeNodeWidgetExt( tree , "two" ) ) ;
		node.setBothImageSrc( "images/component.gif" ) ;
		node.add( node = new TreeNodeWidgetExt( tree , "three" ) ) ;
		node.setBothImageSrc( "images/component.gif" ) ;
		root.add( node2 = node = new TreeNodeWidgetExt( tree , "four" ) ) ;
		node.setBothImageSrc( "images/component.gif" ) ;
		node.add( node = new TreeNodeWidgetExt( tree , "five" ) ) ;
		node.setBothImageSrc( "images/component.gif" ) ;
		node.setCollapsed( false ) ;
		node2.setCollapsed( false ) ;
		root.setCollapsed( false ) ;

		tree.addWatcher( new TreeWidgetWatcher()
		{
			public void nodeSelected( TreeWidgetExt tw , TreeNodeWidgetExt tnw )
			{
				System.out.println( "nodeSelected: " + tnw + " (" + tree.getSelectedNode() + ")" ) ;
			}

			public void nodeAction( TreeWidgetExt tw , TreeNodeWidgetExt tnw )
			{
				System.out.println( "nodeAction: " + tnw ) ;
				TreeNodeWidgetExt nd = new TreeNodeWidgetExt( tree , "Added Node" ) ;
				nd.setBothImageSrc( "images/component.gif" ) ;
				tnw.add( nd ) ;
				tnw.expand() ;
			}
		} ) ;

		frame.add( "Center" , tree ) ;
		frame.pack() ;
		frame.setVisible( true ) ;
		frame.addWindowListener( new BasicWindowMonitor() ) ;
	}
}
