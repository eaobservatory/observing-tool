// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//

package jsky.app.ot;

import java.awt.*;
import java.util.*;
import jsky.app.ot.gui.MultiSelTreeNodeWidget;
import jsky.app.ot.gui.MultiSelTreeWidget;
import gemini.sp.SpAvEditState;
import gemini.sp.SpItem;
import jsky.app.ot.util.Assert;


//
// A helper class used to draw various things on top of tree node
// widget images as the user drags items over them.
//
final class OtTnwDrawHelper {

    // Draw a white X on a red field.
    static void	drawNo(Graphics g) {
	g.setColor(Color.red);
	g.fillArc(1, 1, 14, 14, 0, 360);

	g.setColor(Color.white);
	g.drawLine(5,6,10,11);
	g.drawLine(5,5,11,11);
	g.drawLine(6,5,11,10);

	g.drawLine(10, 5, 5,10);
	g.drawLine(11, 5, 5,11);
	g.drawLine(11, 6, 6,11);
    }

    // Draw a white downward pointing arrow on a red field.
    static void	drawDownArrow(Graphics g) {
	g.setColor(Color.red);
	g.fillArc(1, 1, 14, 14, 0, 360);
   
	g.setColor(Color.white);
	g.drawLine(8,4,8,10);
	g.drawLine(9,4,9,10);
   
	int[] xPoints = { 5,  8, 12,  5 };
	int[] yPoints = { 9, 12,  9,  9 };
	g.fillPolygon(xPoints, yPoints, 4);
    }
   
    // Draw a white left-to-right downward pointing arrow on a red field.
    static void	drawLeftRightDownArrow(Graphics g) {
	g.setColor(Color.red);
	g.fillArc(1, 1, 14, 14, 0, 360);
   
	g.setColor(Color.white);
	g.drawLine(5,6,10,11);
	g.drawLine(5,5,11,11);
	g.drawLine(6,5,11,10);
   
	int[] xPoints = { 7, 11, 11,  7 };
	int[] yPoints = {11, 11,  7, 11 };
	g.drawPolygon(xPoints, yPoints, 4);
    }
   
    // Draw a black "!" on a yellow triangle.
    static void	drawExclamation(Graphics g) {
	g.setColor(Color.yellow);
	int[] xPoints = {  1, 14, 8, 7,  1 };
	int[] yPoints = { 15, 15, 1, 1, 15 };
	g.fillPolygon(xPoints, yPoints, 5);
   
	g.setColor(Color.black);
	g.setFont(new Font("Dialog", Font.BOLD, 14));
	FontMetrics fm = g.getFontMetrics();
	int x = 8 - (fm.stringWidth("!")/2);
	int y = 8 + fm.getAscent()/2;
	g.drawString("!", x, y);
    }
}



/**
 * A MultiSelTreeNodeWidget that contains an SpItem.  OtTreeNodeWidget
 * not only maintains a reference to an associated SpItem, but also provides
 * support for dragging and dropping items in the Science Program tree.
 */
public abstract class OtTreeNodeWidget extends MultiSelTreeNodeWidget
    implements Observer, OtGuiAttributes {

    /** The font for unedited items.  */
    public static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN,  12);

    /** The font for edited items.  */
    public static final Font EDITED_FONT  = new Font("Dialog", Font.ITALIC, 12);


    /** The SpItem associated one-to-one with this tree node widget.  */
    protected SpItem _spItem  = null;

    /** Constructor */
    public OtTreeNodeWidget(OtTreeWidget tree) {
	super(tree);
    }

    /** Constructor */
    public OtTreeNodeWidget() {
    }

    /**
     * Copy the tree node widget.  Each subclass must implement this routine.
     * Maybe someday org.freebongo.gui.Widget.clone() will work and this won't be
     * needed anymore...
     */
    public abstract OtTreeNodeWidget copy();


    /**
     * Set the Science Program item to edit.  This method should be
     * called once, before the object is used.
     */
    public void setItem(SpItem spItem)  {
	Assert.notFalse(_spItem == null);

	_spItem  = spItem;
	_spItem.getAvEditFSM().addObserver(this);
	update(_spItem.getAvEditFSM(), null);
    }

    /**
     * Get the item represented by this tree node.
     */
    public SpItem getItem() {
	return _spItem;
    }

    /**
     * Override collapse(boolean) to store the "collapsed" state in the
     * associated SpItem.  This information is used when a program is loaded
     * from disk or fetched from the database in order to display the tree
     * as it was when last saved.
     */
    public void	setCollapsed(boolean collapsed) {
	super.setCollapsed(collapsed);

	if (_spItem != null) {
	    _spItem.getTable().set(GUI_COLLAPSED, collapsed);
	}
    }


    /**
     * Has the associated SpItem been edited?
     */
    public boolean isEdited() {
	return (_spItem.getAvEditState() == SpAvEditState.EDITED);
    }

    /**
     * Implements the update method from the Observer interface in order to
     * show whether the associated SpItem has been edited.
     */
    public void	update(Observable o, Object arg) {
	SpAvEditState fsm = (SpAvEditState) o;
	if (fsm.getState() == SpAvEditState.EDITED) {
	    setFont(EDITED_FONT);
	} else {
	    setFont(DEFAULT_FONT);
	}

	// See if there's a new title
	String title = _spItem.getTitle();
	if (getText() != title) {
	    setText(title);
	}
	//if (tree != null && tree.getTree() != null) {
	    // this seems like the only way to get the tree node to update immediately
	    //tree.getTree().updateUI();  
	//}
    }

    /**
     * Overrides TreeNodeWidget handleEvent.
     *
    public boolean handleEvent(Event evt) {

	// Handle requests to move an item
	int direction = -1;

	switch (evt.id) {
	case Event.KEY_PRESS:
	    if ((evt.key == '\b') || (evt.key == '\u007F')) {
		OtTreeWidget tw = (OtTreeWidget) getTreeWidget();
		if (tw.multipleItemsSelected()) {
		    tw.rmMultiSelectedItems();
		} else {
		    tw.rmSelectedItem();
		}

		OtTreeNodeWidget sel = (OtTreeNodeWidget) tw.getSelectedNode();
		if (sel != null) {
		    sel.requestFocus();
		}
		return true;
	    }
	    break;

	return super.handleEvent(evt);
    }
    */
}

