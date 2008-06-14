// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe ;

import jsky.app.ot.fits.gui.FitsMouseEvent ;

/**
 * This is an interface supported by TpeImageFeatures that support
 * selecting individual items (such as target positions).
 */
public interface TpeSelectableFeature
{
	/**
	 * Select an item, returning it if successful.  Return null if nothing
	 * is selected.
	 */
	public Object select( FitsMouseEvent evt ) ;
}
