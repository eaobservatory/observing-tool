// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
//
package jsky.app.ot;

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpObs;
import gemini.sp.SpObsLink;
import gemini.sp.SpType;

import java.awt.Event;
import java.awt.Graphics;

import java.util.Observable;

/**
 * A TreeNodeWidget for SpObs items.  A special OtTreeNodeWidget is
 * required for two reasons:
 * <ul>
 * <li> to handle CTRL-click events that toggle the "chained-to-next"
 *      state of the observation, and
 * <li> to handle the creation of SpObsLinks when CTRL-dragging an
 *      observation.
 * </ul>
 */
public class OtObsTreeNodeWidget extends OtTreeNodeWidget {
//     protected boolean _amChained   = false;
//     protected boolean _chainAction = false;

    public OtObsTreeNodeWidget() {
    }

    public OtObsTreeNodeWidget(OtTreeWidget tree) {
	super(tree);
    }

    /**
     * Implement copy() to make this class concrete.
     */
    public OtTreeNodeWidget copy() {
	OtObsTreeNodeWidget newTNW = new OtObsTreeNodeWidget((OtTreeWidget)tree);
	super.copyInto(newTNW);
	return newTNW;
    }

    /**
     * Set the Science Program item to edit.  This method should be
     * called once, before the object is used.
     */
    public void	setItem(SpItem spItem)  {
	super.setItem(spItem);
// 	_amChained = getObsChained();
    }

    /**
     * If this is a drag with the CTRL key pressed, then show the obsLink
     * image.
     *
    public String getDragDropImage() {
	String imgSrc;
	if (_isObsLinkDrag) {
	    imgSrc = "~gemini/images/obsLink.gif";
	} else {
	    imgSrc = super.getDragDropImage();
	}
	return imgSrc;
    }
    */

    /**
     * Implements the getDragDropObject method from the DragDropSource interface.
     * Returns a copy of the item represented by this node.
     *
    public Object getDragDropObject()  {
	if (_isObsLinkDrag) {

	    if (!_spItem.hasBeenNamed()) {
		new ErrorBox("This observation must be stored in the ODB " +
			     "before a link to it can be made.");
		return null;
	    }

	    SpObsLink obsLink = (SpObsLink) SpFactory.create(SpType.OBSERVATION_LINK);
	    obsLink.linkTo((SpObs) _spItem);
      
	    return new OtDragDropObject( obsLink );

	}

	return super.getDragDropObject();
    }
    */

    /**
     * Get the chained state of the observation associated with this node.
     */
    public boolean getObsChained()  {
// 	return ((SpObs) _spItem).getChainedToNext();
	return false;
    }

    /**
     * Set the chained state of the observation associated with this node.
     */
    public void	setObsChained(boolean chained) {
// 	_amChained = chained;
// 	((SpObs) _spItem).chainToNext(chained);
    }

    /**
     * Observer of chain state changes.  Note that the superclass,
     * OtTreeNodeWidget implements Observable, we are simply overriding
     * its definition.
     */
    public void	update(Observable o, Object arg)  {
// 	boolean chained = getObsChained();
// 	if (_amChained != chained) {
// 	    _amChained = chained;
// 	    //if (getParent() != null) getParent().repaint();
// 	}
	super.update(o, arg);
    }

    /**
     * Handle shift-click events to turn on and off observation chaining.
     *
    public boolean handleEvent(Event evt)
    {
	// Only worry about mouse events when the control key is down
	if (!evt.controlDown()) {
	    switch (evt.id) {
	    case Event.MOUSE_DOWN:
	    case Event.MOUSE_DRAG:
	    case Event.MOUSE_MOVE:
	    case Event.MOUSE_UP:
		_chainAction = false;
	    }
	    return super.handleEvent(evt);
	}

	// Only want to fire a link/unlink change when there is a simple
	// cntrl-click on the tree node, not when dragging, not when double clicking
	switch (evt.id) {
	case Event.MOUSE_DOWN: {
	    if (evt.clickCount == 1) {
		_chainAction = true;
	    }
	    break;
	}
	case Event.MOUSE_DRAG: {
	    if (DragDrop.dragging()) {
		_chainAction = false;
	    }
	    break;
	}
	case Event.MOUSE_UP: {
	    if (_chainAction) {
		setObsChained(!getObsChained());
		getParent().repaint();
	    }
	    _chainAction = false;
	    break;
	}
	}

	return super.handleEvent(evt);
    }
    */
}

