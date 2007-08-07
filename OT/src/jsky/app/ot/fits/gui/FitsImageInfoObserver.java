// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.fits.gui;

/**
 * An interface supported by FitsImageWidget clients that wish to
 * know when the image info is updated.
 */
public interface FitsImageInfoObserver
{
	/**
	 * Notify that image info has been updated.
	 */
	public void imageInfoUpdate( FitsImageWidget iw , FitsImageInfo fii );
}
