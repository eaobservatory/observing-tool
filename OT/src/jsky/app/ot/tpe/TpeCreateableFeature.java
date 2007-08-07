// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe;

import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.fits.gui.FitsMouseEvent;

/**
 * This is an interface supported by TpeImageFeatures that can create
 * multiple items (such as target positions).
 */
public interface TpeCreateableFeature
{
	/**
	 * Return the label that should be on the create button.
	 */
	public String[] getCreateButtonLabels();

	/**
	 * Create an item, returning true if successful.
	 */
	public boolean create( FitsMouseEvent evt , FitsImageInfo fii , String label );
}
