/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ot;

import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.util.EventObject;

import jsky.app.ot.OtTreeDragSource;
import jsky.app.ot.OtTreeWidget;

/**
 * This class adds extra functionality to its parent class.
 *
 * It allows the OtAdvancedTreeDropTarget calss to check whether a node
 * is still being dragged over other nodes or over the background of the JTree
 * component. In the latter case the JTree is repainted to make the orange arrow
 * balls disappear.
 *
 * This seems to be the only way to implement such a functionality as
 * the DropTargetListener.dragOver method is not called while a node is
 * being dragged across the background of the JTree component (i.e. not
 * over other nodes). This means that the drag can't be rejected in the
 * usual way by calling the reject method on the drag event.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class OtAdvancedTreeDragSource extends OtTreeDragSource {
    /**
     * dragOver method call history FIFO.
     *
     * FIFO containing boolean values stating whether the last dragOver calls
     * happended on the DropTargetListener (true) as opposed to the
     * DragSourceListener i.e. this class (false).
     */
    protected boolean[] wasDragOverDropTarget = new boolean[3];

    /**
     * Constructor
     */
    public OtAdvancedTreeDragSource(OtTreeWidget spTree) {
        super(spTree);
    }

    public void dragOver(DragSourceDragEvent dsde) {
        overTreeNode(dsde);

        if (treeNeedsRepainting()) {
            _tree.repaint();

            if (System.getProperty("DEBUG") != null) {
                System.out.println("Repainting tree.");
            }
        }

        super.dragOver(dsde);
    }

    /**
     * This method is used to determine whether a node is being dragged
     * over a other nodes or the background of the JTree component.
     *
     * This method has to be called in the dragOver methods of the
     * DropTargetListener as well as the DragSourceListener so that the
     * dragOver method call history FIFO gets updated.
     *
     * @return true if over nodes,
     *         false if over background of JTree
     */
    public boolean overTreeNode(EventObject eventObject) {
        // Result is true if the most recent event was a DropTargetDragEvent.
        boolean result = wasDragOverDropTarget[0];

        // Shift elements in FIFO.
        wasDragOverDropTarget[2] = wasDragOverDropTarget[1];
        wasDragOverDropTarget[1] = wasDragOverDropTarget[0];

        // Set first entry in FIFO according to whether eventObject is a
        // DropTargetDragEvent.
        wasDragOverDropTarget[0] = eventObject instanceof DropTargetDragEvent;

        return result;
    }

    /**
     * Determines whether the JTree needs repainting in order to get rid of the
     * orange arrow balls.
     *
     * Call this method after a call to {@link #overTreeNode(EventObject)}
     *
     * @return true  if the cursor has just "left" the tree nodes and is now
     *               over the background of the JTree.
     *         false if the cursor is over the background of the JTree
     *               (and was there before)
     *               or if the cursor is over tree nodes.
     */
    protected boolean treeNeedsRepainting() {
        return ((wasDragOverDropTarget[2] == true)
                && (wasDragOverDropTarget[1] == false)
                && (wasDragOverDropTarget[0] == false));
    }
}
