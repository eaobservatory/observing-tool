// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.fits.gui;
import java.awt.geom.Point2D;


public final class FitsImageInfo
{
    /**
     * Screen coordinates of the center of the image.
    */
    public Point2D.Double baseScreenPos;

    /**
    * RA of the center in degrees.
    */
    public double ra = 0.0;

    /**
    * Dec of the center in degrees.
    */
    public double dec = 0.0;

    /**
    * Scale of the image in pixels per arcsec.
    */
    public double pixelsPerArcsec = 1.0;

    /**
    * Due north in the sky relative to up in the image.
    */
    public double theta = 0.0;

    /**
    * The current position angle (in degrees).
    */
    public double posAngleDegrees = 0.0;

    /**
    * Standard debugging method.
    */
    public String toString() {
	return getClass().getName() +
	    "[baseScreenPos=" + baseScreenPos +
	    ", ra=" + ra +
	    ", dec=" + dec +
	    ", pixelsPerArcsec=" + pixelsPerArcsec +
	    ", theta=" + theta +
	    ", posAngleDegrees=" + posAngleDegrees + "]";
    }
}

