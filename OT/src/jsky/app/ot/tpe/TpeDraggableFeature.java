// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe ;

import jsky.app.ot.fits.gui.FitsImageInfo ;
import jsky.app.ot.fits.gui.FitsMouseEvent ;

/**
 * This interface should be supported by TpeImageFeatures that are
 * "draggable".
 */
public interface TpeDraggableFeature
{
	/**
	 * Start dragging the object.
	 */
	public boolean dragStart( FitsMouseEvent evt , FitsImageInfo fii ) ;

	/**
	 * Drag to a new location.
	 */
	public void drag( FitsMouseEvent evt ) ;

	/**
	 * Stop dragging.
	 */
	public void dragStop( FitsMouseEvent evt ) ;
}
