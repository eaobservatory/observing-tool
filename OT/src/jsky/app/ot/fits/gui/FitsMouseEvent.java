// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.fits.gui;

import jsky.app.ot.gui.image.ViewportMouseEvent;

/**
 * A mouse event that occurred in a FitsImageWidget.  This structure
 * contains fields that, in addition to all the information in a
 * ViewportMouseEvent, describe the ra and dec where the event occurred
 * and the x and y offset in arcsec from the base position.
 */
public class FitsMouseEvent extends ViewportMouseEvent
{
    /**
     * The RA of the event in degrees.
    */
    public double ra;

    /**
    * The Dec of the event in degrees.
    */
    public double dec;

    /**
    * The RA of the event as a String in HHMMSS format.
    */
    public String raStr;

    /**
    * The Dec of the event as a String in DDMMSS format.
    */
    public String decStr;

    /**
    * The X offset of the event from the base position in arcsec.
    */
    public double xOffset;

    /**
    * The Y offset of the event from the base position in arcsec.
    */
    public double yOffset;

    /**
    * The X offset of the event from the base position in arcsec as a String.
    */
    public String xOffsetStr;

    /**
    * The Y offset of the event from the base position in arcsec as a String.
    */
    public String yOffsetStr;


    /**
 * Returns a human-readable string describing the contents of
 * the FitsMouseEvent.
 */
    public String toString() {
	return "FitsMouseEvent[" + super.toString() +
	    ", raStr=" + raStr +
	    ", decStr=" + decStr +
	    ", xOffsetStr=" + xOffsetStr +
	    ", yOffsetStr=" + yOffsetStr + "]";
    }
}
