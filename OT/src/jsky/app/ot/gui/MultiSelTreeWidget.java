// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui;

import java.awt.Event;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;
import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
import jsky.util.gui.BasicWindowMonitor;

/**
 * A TreeWidget extension that supports multiselection as an option.
 * The help of MultiSelTreeNodeWidget is required for this.
 *
 * @see MultiSelTreeNodeWidget
 * @author      Shane Walker, Allan Brighton (Swing port)
 */
public class MultiSelTreeWidget extends TreeWidgetExt {
    /**
     * Get the multiSelect property.
     */
    public boolean getMultiSelect() {
	return (tree.getSelectionModel().getSelectionMode() == TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
    }

    /**
     * Set whether this tree should use multi-selection.
     */
    public void	setMultiSelect(boolean ms) {
	if (ms)
	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
	else
	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    /**
     * Turn off the multi-selection.  This doesn't disable the feature,
     * it just unselects the widgets.
     */
    public void	multiUnselect() {
	// XXX allan: not sure what this should do...
    }

    /**
     * Determine whether multiple items are selected.
     */
    public synchronized boolean	multipleItemsSelected() {
	return (tree.getSelectionCount() > 1);
    }

    /**
     * Get a vector containing the selected tree nodes.
     */
    public Vector getMultiSelectNodes() {
	TreePath[] paths = tree.getSelectionPaths();
	Vector v = null;
	if (paths != null) {
	    v = new Vector(paths.length, 1);
	    for(int i = 0; i < paths.length; i++)
		v.add(i, paths[i].getLastPathComponent());
	}
	return v;
    }


    /** Notify that multiple nodes have been selected.  */
    protected void notifyMultiSelect() {
	Vector v = getWatchers();
	int  cnt = v.size();
	for (int i=0; i<cnt; ++i) {
	    Object o = v.elementAt(i);
	    if (!(o instanceof MultiSelTreeWidgetWatcher)) continue;
	    MultiSelTreeWidgetWatcher mstww = (MultiSelTreeWidgetWatcher) o;
	    mstww.multiNodeSelect(this, getMultiSelectNodes());
	}
    }

    /**
     * Set the multi-select region so that it encompasses the
     * MultiSelTreeNodeWidgets in the given array.  For now, this only works
     * if the widgets are all siblings.
     */
    public void	setMultiSelectNodes(MultiSelTreeNodeWidget[] tnwA)  {
	if (tnwA.length <= 1) {
	    return;
	}
	
	// Make sure that they all have the same parent
	TreePath[] paths = new TreePath[tnwA.length];
	for (int i = 1; i < tnwA.length; i++) {
	    if (tnwA[i].getParent() != tnwA[i-1].getParent()) {
		return;
	    }  
	    paths[i] = new TreePath(tnwA[i].getPath());
	}

	// Reset the selection
	tree.clearSelection();
	tree.setSelectionPaths(paths);
    }

    /**
     * test main: usage: java SpTree
     * (Only tests the basic layout).
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("MultiSelTreeWidget");
	MultiSelTreeWidget tree = new MultiSelTreeWidget();
        tree.setPreferredSize(new Dimension(360, 400));
        frame.getContentPane().add("Center", tree);
        frame.pack();
        frame.setVisible(true);
	frame.addWindowListener(new BasicWindowMonitor());
    }
}

