// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui.image;
import java.awt.event.MouseEvent;

/**
 * A mouse event that occurred in a ViewportImageWidget.  This structure
 * contains fields that describe the type of mouse event, and the location
 * relative to the image being viewed by the ViewportImageWidget.
 * The x and y fields are stored as doubles since the image may be scaled.
 */
public class ViewportMouseEvent
{
    /** Event id, same as java.awt.Event.  */
    public int id;

   /** Event source.  */
    public ViewportImageWidget source;

    /** X pos of event relative to the real image being viewed.  */
    public double xView;

    /** Y pos of event relative to the real image being viewed.  */
    public double yView;

    /** X pos of event relative to the widget.  */
    public int xWidget;

    /** Y pos of event relative to the widget.  */
    public int yWidget;

    /** Permit construction before all the fields are known. */
    public ViewportMouseEvent() {
    }

    /** Construct with all the fields.  */
    public ViewportMouseEvent(int id, ViewportImageWidget viw,
			      double xView, double yView, int xWidget, int yWidget) {
	this.id      = id;
	this.source  = viw;
	this.xView   = xView;
	this.yView   = yView;
	this.xWidget = xWidget;
	this.yWidget = yWidget;
    }

    /**
     * Returns a human-readable string describing the contents of
     * the ViewportMouseEvent.
     */
    public String toString() {
	String event="<unknown: " + id + ">";
	switch (id) {
	case MouseEvent.MOUSE_PRESSED:  event = "MOUSE_PRESSED"; break;
	case MouseEvent.MOUSE_RELEASED: event = "MOUSE_RELEASED";   break;
	case MouseEvent.MOUSE_MOVED:    event = "MOUSE_MOVED"; break;
	case MouseEvent.MOUSE_DRAGGED:   event = "MOUSE_DRAGGED"; break;
	}
	return "ViewportMouseEvent[id=" + event + ", xView=" + xView +
	    ", yView=" + yView +
	    ", xWidget=" + xWidget +
	    ", yWidget=" + yWidget + "]";
    }

}

