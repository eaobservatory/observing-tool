// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui.image;

/**
 * An interface supported by ViewportImageWidget clients that which to
 * be notified when the view changes.
 *
 * @see ViewportImageWidget
 */
public interface ViewportViewObserver
{
   /**
    * Notify that the view has changed.
    */
   public void viewportViewChange(ViewportImageWidget iw, ImageView iv);
}

