/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ListIterator;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import gemini.sp.SpInsertData;
import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import jsky.app.ot.util.DnDUtils;

/**
 * Drag&Drop target for the OT tree widget.
 * Based on an example in the book: "Core Swing, Advanced Programming".
 */
public class OtTreeDropTarget implements DropTargetListener, PropertyChangeListener {

    /** Target OT tree widget */
    protected OtTreeWidget treeWidget;

    /** The internal JTree widget */
    protected JTree tree;

    /** The drop target */
    protected DropTarget dropTarget;

    /** Indicates whether data is acceptable */
    protected boolean acceptableType;

    /** Initially selected rows */
    protected TreePath[] selections;

    /** Initial lead selection */
    protected TreePath leadSelection;


    /**
     * Constructor
     */
    public OtTreeDropTarget(OtTreeWidget treeWidget) {
	this.treeWidget = treeWidget;
	tree = treeWidget.getTree();
	tree.setEditable(true);
		
	// Listen for changes in the enabled property
	tree.addPropertyChangeListener(this);

	// Create the DropTarget and register 
	// it with the OtTreeWidget.
	dropTarget = new DropTarget(tree,
				    DnDConstants.ACTION_COPY, 
				    this, 
				    tree.isEnabled(), null);
    }

    /** Implementation of the DropTargetListener interface */
    public void dragEnter(DropTargetDragEvent dtde) {
	DnDUtils.debugPrintln("dragEnter, drop action = " 
			      + DnDUtils.showActions(dtde.getDropAction()));

	// Save the list of selected items
	saveTreeSelection();

	// Get the type of object being transferred and determine
	// whether it is appropriate.
	checkTransferType(dtde);

	// Accept or reject the drag.
	boolean acceptedDrag = acceptOrRejectDrag(dtde);
				
	// Do drag-under feedback
	dragUnderFeedback(dtde, acceptedDrag);
    }

    /** Implementation of the DropTargetListener interface */
    public void dragExit(DropTargetEvent dte) {
	DnDUtils.debugPrintln("DropTarget dragExit");
		
	// Do drag-under feedback
	dragUnderFeedback(null, false);

	// Restore the original selections
	restoreTreeSelection();
    }

    /** Implementation of the DropTargetListener interface */
    public void dragOver(DropTargetDragEvent dtde) {
	DnDUtils.debugPrintln("DropTarget dragOver, drop action = "
			      + DnDUtils.showActions(dtde.getDropAction()));

	// Accept or reject the drag
	boolean acceptedDrag = acceptOrRejectDrag(dtde);

	// Do drag-under feedback
	dragUnderFeedback(dtde, acceptedDrag);
    }

    /** Implementation of the DropTargetListener interface */
    public void dropActionChanged(DropTargetDragEvent dtde) {
	DnDUtils.debugPrintln("DropTarget dropActionChanged, drop action = "
			      + DnDUtils.showActions(dtde.getDropAction()));

	// Accept or reject the drag
	boolean acceptedDrag = acceptOrRejectDrag(dtde);
		
	// Do drag-under feedback
	dragUnderFeedback(dtde, acceptedDrag);
    }

    /** Implementation of the DropTargetListener interface */
    public void drop(DropTargetDropEvent dtde) {
	DnDUtils.debugPrintln("DropTarget drop, drop action = "
			      + DnDUtils.showActions(dtde.getDropAction()));

	// Check the drop action
	if ((dtde.getDropAction() & DnDConstants.ACTION_COPY) != 0) {
	    // Accept the drop and get the transfer data
	    dtde.acceptDrop(dtde.getDropAction());
	    Transferable transferable = dtde.getTransferable();
	    boolean dropSucceeded = false;
			
	    try {
		tree.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Save the user's selections
		saveTreeSelection();
														  
		dropSucceeded = dropNodes(dtde.getDropAction(), transferable, dtde.getLocation());
				
		DnDUtils.debugPrintln("Drop completed, success: " 
				      + dropSucceeded);
	    } 
	    catch (Exception e) {
		DnDUtils.debugPrintln("Exception while handling drop " + e);
	    } 
	    finally {
		tree.setCursor(Cursor.getDefaultCursor());

		// Restore the user's selections
		restoreTreeSelection();
		dtde.dropComplete(dropSucceeded);
	    }
	} 
	else {
	    DnDUtils.debugPrintln("Drop target rejected drop");
	    dtde.dropComplete(false);
	}
    }
	
    /** PropertyChangeListener interface */
    public void propertyChange(PropertyChangeEvent evt) {
	String propertyName = evt.getPropertyName();
	if (propertyName.equals("enabled")) {
	    // Enable the drop target if the OtTreeWidget is enabled
	    // and vice versa.		
	    dropTarget.setActive(tree.isEnabled());
	}
    }

    // Internal methods start here
    protected boolean acceptOrRejectDrag(DropTargetDragEvent dtde) {
	int dropAction = dtde.getDropAction();
	int sourceActions = dtde.getSourceActions();
	boolean acceptedDrag = false;

	DnDUtils.debugPrintln("\tSource actions are " + 
			      DnDUtils.showActions(sourceActions) + 
			      ", drop action is " + 
			      DnDUtils.showActions(dropAction));
		
	boolean acceptableDropLocation = isAcceptableDropLocation(dtde);

	// Reject if the object being transferred 
	// or the operations available are not acceptable.
	if (!acceptableType || (sourceActions & DnDConstants.ACTION_COPY) == 0) {
	    DnDUtils.debugPrintln("Drop target rejecting drag: acceptableType = " + acceptableType);
	    dtde.rejectDrag();
	} 
	else if (!tree.isEditable()) {
	    // Can't drag to a read-only OtTreeWidget
	    DnDUtils.debugPrintln("Drop target rejecting drag: tree not editable");			
	    dtde.rejectDrag();
	} 
	else if (!acceptableDropLocation) {
	    // Can only drag to writable directory
	    DnDUtils.debugPrintln("Drop target rejecting drag: no acceptable drop lLocation");			
	    dtde.rejectDrag();
	} 
	else if ((dropAction & DnDConstants.ACTION_COPY) == 0) {
	    // Not offering copy or move - suggest a copy
	    DnDUtils.debugPrintln("Drop target offering COPY");
	    dtde.acceptDrag(DnDConstants.ACTION_COPY);
	    acceptedDrag = true;
	} 
	else {
	    // Offering an acceptable operation: accept
	    DnDUtils.debugPrintln("Drop target accepting drag");
	    dtde.acceptDrag(dropAction);
	    acceptedDrag = true;
	}

	return acceptedDrag;
    }

    protected void dragUnderFeedback(DropTargetDragEvent dtde, boolean acceptedDrag) {
	if (dtde != null && acceptedDrag) {
	    if (isAcceptableDropLocation(dtde)) {
		Point location = dtde.getLocation();
		treeWidget.setIgnoreSelection(true);
		treeWidget.selectNode(getNode(location));
		treeWidget.setIgnoreSelection(false);
	    } 
	    else {
		tree.clearSelection();
	    }
	} 
	else {
	    tree.clearSelection();
	}
    }
		
    protected void checkTransferType(DropTargetDragEvent dtde) {
	// Accept a list of files
	acceptableType = false;
	if (dtde.isDataFlavorSupported(OtDragDropObject.dataFlavor)) {
	    acceptableType = true;
	}
	DnDUtils.debugPrintln("Data type acceptable - " + acceptableType);
    }

    // This method handles a drop for a list of files
    protected boolean dropNodes(int action, Transferable transferable, Point location) 
	throws IOException, UnsupportedFlavorException,  MalformedURLException {

	OtDragDropObject ddo = (OtDragDropObject)transferable.getTransferData(OtDragDropObject.dataFlavor);
	OtTreeNodeWidget node = getNode(location);
	if (node == null) 
	    return false;
		
	DnDUtils.debugPrintln((action == DnDConstants.ACTION_COPY ? "Copy" : "Move") +
			      " item " + ddo.getSpItem() +
			      " to targetNode " +node);

	// Highlight the drop location while we perform the drop
	treeWidget.setIgnoreSelection(true);
	tree.setSelectionPath(tree.getPathForLocation(location.x, location.y));
	treeWidget.setIgnoreSelection(false);

	// Copy source object to the target
	SpInsertData spID = getSpInsertData(ddo, node);
	if (spID == null) 
	    return false;
 
	OtTreeWidget ownerTW = ddo.getOwner();
	SpItem spItem = ddo.getSpItem();
	SpItem[] newItems = spID.items;
	if (ownerTW == treeWidget) {
	    // The dragged item was owned by this tree, so just move it.
	    treeWidget.mvItems( spID );
	} else {
	    if (ownerTW != null) {
		// Make a copy, since these items are owned by another tree.
		for (int i=0; i<newItems.length; ++i) {
		    newItems[i] = newItems[i].deepCopy();
		}
	    }
	    newItems = treeWidget.addItems( spID );
	}
	if (newItems == null) 
	    return false;
   	return true;
    }

    /**
     * Return the SpInsertData object to use to insert the object(s) being dragged,
     * or null, if it is not allowed to drop the item(s) at the current position.
     */
    protected SpInsertData getSpInsertData(OtDragDropObject ddo, OtTreeNodeWidget node) {
	// See whether we can insert the newItem.
	SpItem[] newItems = ddo.getSpItems();
	SpItem spItem = node.getItem();
	SpInsertData spID = SpTreeMan.evalInsertAfter(newItems, spItem);
	if (spID == null) 
	    spID = SpTreeMan.evalInsertInside(newItems, spItem);

	return spID;
    }

    /** Return the tree node for the given location */
    protected OtTreeNodeWidget getNode(Point location) {
	TreePath treePath = tree.getPathForLocation(location.x, location.y);
	if (treePath == null) {
	    return null;
	}
	return (OtTreeNodeWidget)treePath.getLastPathComponent();
    }


    /** Return true if its okay to drop the item(s) here */
    protected boolean isAcceptableDropLocation(DropTargetDragEvent dtde) {
	/*
	// get the root item being dragged  (XXX doesn't work, getTransferable() is protected)
	Transferable transferable = dtde.getDropTargetContext().getTransferable();
	OtDragDropObject o = transferable.getTransferData(OtDragDropObject.dataFlavor);
	if (!(o instanceof OtDragDropObject)) 
	    return false;
	OtDragDropObject ddo = (OtDragDropObject) o;
	*/
	OtDragDropObject ddo = OtTreeDragSource.dragObject;
	if (ddo == null)
	    return false;

	// get the node under the mouse
	OtTreeNodeWidget node = getNode(dtde.getLocation());
	if (node == null) 
	    return false;
	
	return (getSpInsertData(ddo, node) != null); 
    }

    /** Save the current tree selection */
    protected void saveTreeSelection() {
	selections = tree.getSelectionPaths();
	leadSelection = tree.getLeadSelectionPath();
	tree.clearSelection();
    }

    /** Restore the current tree selection */
    protected void restoreTreeSelection() {
	treeWidget.setIgnoreSelection(true);
	tree.setSelectionPaths(selections);

	// Restore the lead selection
	if (leadSelection != null) {
	    tree.removeSelectionPath(leadSelection);
	    tree.addSelectionPath(leadSelection);
	}
	treeWidget.setIgnoreSelection(false);
    }
}

