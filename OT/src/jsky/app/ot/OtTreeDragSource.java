/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.io.File;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import gemini.sp.SpItem;
import jsky.app.ot.util.DnDUtils;

/**
 * Drag&Drop source for tree widgets.
 * Based on an example in the book: "Core Swing, Advanced Programming".
 */
public class OtTreeDragSource implements DragGestureListener, DragSourceListener {
    
    /** Target tree widget */
    protected OtTreeWidget treeWidget;

    /** The internal JTree widget */
    protected JTree tree;

    // Dragged objects (needs to be static so that the drop target can access it)
    protected static OtDragDropObject dragObject;

    // Dragged paths
    protected TreePath[] paths;

    /**
     * Constructor
     */
    public OtTreeDragSource(OtTreeWidget treeWidget) {
	this.treeWidget = treeWidget;
	this.tree = treeWidget.getTree();

	// Use the default DragSource
	DragSource dragSource = DragSource.getDefaultDragSource();

	// Create a DragGestureRecognizer and
	// register as the listener
	dragSource.createDefaultDragGestureRecognizer(tree, DnDConstants.ACTION_COPY, this);
    }

    /** Implementation of DragGestureListener interface. */
    public void dragGestureRecognized(DragGestureEvent dge) {
	// Get the mouse location and convert it to
	// a location within the tree.
	Point location = dge.getDragOrigin();
	TreePath dragPath = tree.getPathForLocation(location.x, location.y);
	if (dragPath != null && tree.isPathSelected(dragPath)) {
	    // Get the list of selected nodes and create a Transferable
	    // The list of nodes is saved for use when the drop completes.
	    paths = tree.getSelectionPaths();
	    if (paths != null && paths.length > 0) {
		SpItem[] spItems = new SpItem[paths.length];
		for (int i = 0; i < paths.length; i++) {
		    OtTreeNodeWidget node = (OtTreeNodeWidget)paths[i].getLastPathComponent();
		    spItems[i] = node.getItem();
		}
		dragObject = new OtDragDropObject(spItems, treeWidget);
		dge.startDrag(null, dragObject, this);
	    }
	}
    }

    // Implementation of DragSourceListener interface
    public void dragEnter(DragSourceDragEvent dsde) {
	DnDUtils.debugPrintln("Drag Source: dragEnter, drop action = " 
			      + DnDUtils.showActions(dsde.getDropAction()));
    }

    public void dragOver(DragSourceDragEvent dsde) {
	DnDUtils.debugPrintln("Drag Source: dragOver, drop action = " 
			      + DnDUtils.showActions(dsde.getDropAction()));
    }

    public void dragExit(DragSourceEvent dse) {
	DnDUtils.debugPrintln("Drag Source: dragExit");
    }

    public void dropActionChanged(DragSourceDragEvent dsde) {
	DnDUtils.debugPrintln("Drag Source: dropActionChanged, drop action = " 
			      + DnDUtils.showActions(dsde.getDropAction()));
    }

    public void dragDropEnd(DragSourceDropEvent dsde) {
	DnDUtils.debugPrintln("Drag Source: drop completed, drop action = " 
			      + DnDUtils.showActions(dsde.getDropAction())
			      + ", success: " + dsde.getDropSuccess());
    }
}
