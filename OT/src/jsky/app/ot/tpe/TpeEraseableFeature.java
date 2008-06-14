// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe ;

import jsky.app.ot.fits.gui.FitsMouseEvent ;

/**
 * This is an interface supported by TpeImageFeatures that can erase
 * one or more items (such as target positions).
 */
public interface TpeEraseableFeature
{
	/**
	 * Erase an item, returning true if successful.
	 */
	public boolean erase( FitsMouseEvent evt ) ;
}
