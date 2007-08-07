// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui.image;

/**
 * An interface supported by ViewportImageWidget clients that which to
 * be notified for each mouse event.
 *
 * @see ViewportImageWidget
 */
public interface ViewportMouseObserver
{
	/**
	 * Notification that a new mouse event has arrived.
	 */
	public void viewportMouseEvent( ViewportImageWidget iw , ViewportMouseEvent vme );
}
