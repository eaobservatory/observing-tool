// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * A TreeNodeWidget extension.  It Supports a "watcher".  
 * A watcher is an object that supports the TreeNodeWidgetWatcher 
 * interface to receive notification of when a node is selected or acted upon.
 *
 * @author	Shane Walker, Allan Brighton (ported from Bongo to Swing)
 */
public class TreeNodeWidgetExt extends DefaultMutableTreeNode {
    
    /** The tree to which this node belongs */
    protected TreeWidgetExt tree;

    /** The list of clients interested in TreeNodeWidget actions. */
    protected Vector _watchers;

    /** url string of the image for this node */
    protected String src;
    
    /** The icon for the tree node. */
    protected ImageIcon icon;
    
    /** url string of the image for this node when it is expanded */
    protected String expandSrc;
    
    /** The icon for the tree node when it is expanded. */
    protected ImageIcon expandIcon;
    
    /** text displayed by the tree node */
    protected String label;

    /** Font for text displayed by the tree node */
    protected Font font;



    /** Constructor (need to call setTree(TreeNodeWidgetExt) later).  */ 
    public TreeNodeWidgetExt() {
    }

    /** Constructor.  */ 
    public TreeNodeWidgetExt(TreeWidgetExt tree) {
	setTree(tree);
    }

    /** Constructor with a label. Used by subclasses of TreeNodeWidgetExt.  */ 
    public TreeNodeWidgetExt(TreeWidgetExt tree, String label) {
	super(label);
	setText(label);
	setTree(tree);
    }


    /** Set the tree that this node belongs to (required). */
    public void setTree(TreeWidgetExt tree) {
	this.tree = tree;
    }

    /** Return the string representation of this node */
    public String toString() {return label;}

    /**
     * This copyInto method is provied to help support cloning objects
     */
    public void	copyInto(TreeNodeWidgetExt newTNW) {
	//newTNW.setCollapsed( isCollapsed() );
	newTNW.setSrc( getSrc() );
	newTNW.setExpandSrc( getExpandSrc() );
	newTNW.setText( getText() );
    }

    /** Show or hide the subtree starting at this node */
    public void setCollapsed(boolean collapsed) {
	if (tree == null)
	    return;
	DefaultTreeModel treeModel = (DefaultTreeModel)tree.getTree().getModel();
	TreePath path = new TreePath(getPath());
	//System.out.println("XXX setCollapsed(" + path + ") " + collapsed);
	if (collapsed) {
	    tree.getTree().collapsePath(path);
	}
	else {
	    tree.getTree().expandPath(path);
	}
	tree.getTree().repaint();
    }

    /** Return true if the subtree starting at this node is collapsed. */
    public boolean isCollapsed() {
	if (tree != null)
	    return tree.getTree().isCollapsed(new TreePath(getPath()));
	return false;
    }

    /** 
     * Set the url string of the image for this node.
     */
    public void setSrc(String src) {
	/* MFO DEBUG */ //System.out.println("src = " + src);
	/* MFO DEBUG */ //if(src == null) {
	/* MFO DEBUG */ //  icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("mfoDebug.gif"));
	/* MFO DEBUG */ //  this.src = "mfoDebug.gif";
	/* MFO DEBUG */ //  return;
	/* MFO DEBUG */ //}  
	
	this.src = src;
	icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("jsky/app/ot/" + src));
    }

    /** 
     * Return the url string of the image for this node.
     */
    public String getSrc() {
	return src;
    }

    /** 
     * Return the icon image for this node.
     */
    public ImageIcon getIcon() {
	return icon;
    }

    /** 
     * Set the url of the image for this node when it is expanded.
     */
    public void setExpandSrc(String expandSrc) {
	/* MFO DEBUG */ //System.out.println("expandSrc = " + expandSrc);
	/* MFO DEBUG */ //if(expandSrc == null) {
	/* MFO DEBUG */ //  icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("mfoExpandDebug.gif"));
	/* MFO DEBUG */ //  this.expandSrc = "mfoExpandDebug.gif";
	/* MFO DEBUG */ //  return;
	/* MFO DEBUG */ //}  
	
	this.expandSrc = expandSrc;
	expandIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("jsky/app/ot/" + expandSrc));
    }

    /** 
     * Return the url string of the image for this node when it is expanded.
     */
    public String getExpandSrc() {
	return expandSrc;
    }

    /** 
     * Return the icon image for this node when it is expanded.
     */
    public ImageIcon getExpandIcon() {
	return expandIcon;
    }

    /** Set the text displayed by the tree node */
    public void setText(String label) {
	this.label = label;
	if (tree != null) {
	    // this is needed to make the change appear immediately
	    DefaultTreeModel treeModel = (DefaultTreeModel)tree.getTree().getModel();
	    treeModel.nodeChanged(this);
	}
    }

    /** Return the text displayed by the tree node */
    public String getText() {
	return label;
    }

    /** Set the font for the text displayed by the tree node */
    public void setFont(Font font) {
	this.font = font;
	if (tree != null) 
	    tree.getTree().repaint();
    }

    /** Return the font for the text displayed by the tree node */
    public Font getFont() {
	return font;
    }

    /**
     * Add a watcher for this node only.
     */
    public synchronized final void addWatcher(TreeNodeWidgetWatcher watcher) {
	if (_watchers == null) {
	    _watchers = new Vector();
	}

	if (!_watchers.contains(watcher)) {
	    _watchers.addElement(watcher);
	}
    }

    /**
     * Add a watcher for this node and the entire subtree rooted at this node.
     */
    public final void addWatcherAll(TreeNodeWidgetWatcher watcher) {
	addWatcher(watcher);
	Enumeration e = postorderEnumeration();
	while(e.hasMoreElements()) {
	    TreeNodeWidgetExt node = (TreeNodeWidgetExt)(e.nextElement());
	    node.addWatcher(watcher);
	}
    }

    /**
     * Delete a watcher for this node only.
     */
    public synchronized final void deleteWatcher(TreeNodeWidgetWatcher watcher) {
	if (_watchers == null) {
	    return;
	}
	_watchers.removeElement(watcher);
    }

    /**
     * Delete a watcher for this node and the entire subtree rooted at this node.
     */
    public final void deleteWatcherAll(TreeNodeWidgetWatcher watcher) {
	deleteWatcher(watcher);
	Enumeration e = postorderEnumeration();
	while(e.hasMoreElements()) {
	    TreeNodeWidgetExt node = (TreeNodeWidgetExt)(e.nextElement());
	    node.deleteWatcher(watcher);
	}
    }

    /**
     * Delete all watchers for this node only.
     */
    public synchronized final void deleteWatchers() {
	if (_watchers == null) {
	    return;
	}
	_watchers.removeAllElements();
    }

    /**
     * Delete a watcher for this node and the entire subtree rooted at this node.
     */
    public final void deleteWatchersAll() {
	deleteWatchers();
	Enumeration e = postorderEnumeration();
	while(e.hasMoreElements()) {
	    TreeNodeWidgetExt node = (TreeNodeWidgetExt)(e.nextElement());
	    node.deleteWatchers();
	}
    }

    /**
     * Add a child at the given position. 
     */
    public void	add(int pos, TreeNodeWidgetExt node) {
	TreeNodeWidgetExt selectedNode = tree.getSelectedNode();
	DefaultTreeModel treeModel = (DefaultTreeModel)tree.getTree().getModel();
	treeModel.insertNodeInto(node, this, pos);
	if (selectedNode != null)
	    selectedNode.select();
    }

    /** Select this node */
    public void	select() {
	setSelected(true);
    }

    /** Return the number of subnodes (for compat with bongo) */
    public int getWidgetCount() {
	return getChildCount();
    }

    //
    // Notify all watchers that the node has been selected.
    //
    void notifySelect() {
	Vector v;
	synchronized (this) {
	    if (_watchers == null) {
		return;
	    }
	    v = (Vector) _watchers.clone();
	}

	int cnt=v.size();
	for (int i=0; i<cnt; ++i) {
	    TreeNodeWidgetWatcher tnww = (TreeNodeWidgetWatcher) v.elementAt(i);
	    tnww.nodeSelected(this);
	}
	tree.notifySelect(this);
    }

    /**
     * Select a tree node and notify the watcher.
     */
     public void setSelected(boolean selected) {
	if (tree.getSelectedNode() != this) {
	    tree.selectNode(this);
	    notifySelect();  // XXX will this be called twice?
	}
    } 

    /**
     * Notify all watchers that the node has been double-clicked.
     */
    void notifyAction() {
	Vector v;
	synchronized (this) {
	    if (_watchers == null) {
		return;
	    }
	    v = (Vector) _watchers.clone();
	}

	int cnt=v.size();
	for (int i=0; i<cnt; ++i) {
	    TreeNodeWidgetWatcher tnww = (TreeNodeWidgetWatcher) v.elementAt(i);
	    tnww.nodeAction(this);
	}
	tree.notifyAction(this);
    }


    /**
     * Override TreeNodeWidget action method to notify the watcher.
     */
    public void	action() {
	notifyAction();
    }

    /**
     * Show this node, expanding ancestors if necessary.
     */
    public void	uncover() {
	tree.getTree().makeVisible(new TreePath(getPath()));
    }

    /**
     * Expand this tree node.
     */
    public void	expand() {
	tree.getTree().expandPath(new TreePath(getPath()));
    }

    /**
     * Remove this tree node.
     */
    public void	remove() {
	//((TreeNodeWidgetExt)getParent()).remove(this);
	DefaultTreeModel treeModel = (DefaultTreeModel)tree.getTree().getModel();
	treeModel.removeNodeFromParent(this);
    }

    /**
     * Return the selected node under this subtree, or null if there is no selection there.
     */
    public TreeNodeWidgetExt getSelectedNode() {
	TreeNodeWidgetExt node = tree.getSelectedNode();
	TreePath path = new TreePath(node.getPath());
	if (path.isDescendant(new TreePath(getPath()))) {
	    return node;
	}
	return null;
    }

    /**
     * Return a vector containing the child nodes of this node.
     */
    public Vector getChildren() {
	int n = getChildCount();
	Vector v = new Vector(n);
	for (int i = 0; i < n; i++)
	    v.add(getChildAt(i));
	return v;
    }

    /**
     * Get the index of the given child, or -1 if the given widget isn't a child
     * of this node.
     */
    public final int getIndexOf(TreeNodeWidgetExt node) {
	return getIndex(node);
    }

    /**
     * Get my index in my parent's array of widgets.
     */
    public final int getMyIndex() {
	return ((TreeNodeWidgetExt)getParent()).getIndexOf(this);
    }

    /** Return the tree widget containing this node */
    public TreeWidgetExt getTreeWidget() {return tree;}

    /**
     * Check if the widget contains the given rectangle.  The rectangle
     * is specified in the parent's coordinate system.
     *
    public boolean contains(int px, int py, int pw, int ph) {
	return (px > getX()) && (py > getY()) && (px+pw < getX()+getWidth()) && (py+ph < getY()+getHeight());
    }
    */
 
    /**
     * Translate the given point (in the coordinate system of this tree node)
     * to the coordinate system of the containing TreeWidget.
     *
    public void translateToTreeWidget(Point pt) {
	int x = pt.x;
	int y = pt.y;

	// Translate the event to the frame of the tree widget.
	TreeWidget tw = getTreeWidget();
	Widget   cont = tw.content;
	Widget      p = getParent();
	x += this.getX() + this.getTranslation().x;
	y += this.getY() + this.getTranslation().y;
          
	while (p != cont) {
	    x += p.getX() + p.getTranslation().x;
	    y += p.getY() + p.getTranslation().y;
	    p  = p.getParent();
	}
	x += cont.getX() + cont.getTranslation().x;
	y += cont.getY() + cont.getTranslation().y;

	pt.x = x;
	pt.y = y;
	}*/

    /**
     * Set both the regular image source and the expanded image source in
     * one method call.  This is usefull when you don't want the image to
     * change based upon whether or not it is expanded.
     **/
    public void	setBothImageSrc(String imgSrc) {
	setSrc(imgSrc);
	setExpandSrc(imgSrc);
    }
}

