// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor;

import jsky.app.ot.fits.gui.FitsImageWidget;
import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.fits.gui.FitsImageInfoObserver;
import jsky.app.ot.fits.gui.FitsPosMap;

/**
 * An auxiliary class used to maintain a mapping between telescope positions
 * and image widget locations.
 */
public class OffsetPosMap extends FitsPosMap
    implements FitsImageInfoObserver {

    private double _ra, _dec, _posAngle;

    /**
     * Construct with an image widget.
     */
    public OffsetPosMap(FitsImageWidget iw) {
	super(iw);
	iw.addInfoObserver(this);
    }

    /**
     */
    public void free() {
	if (_iw != null) {
	    _iw.deleteInfoObserver(this);
	}
	super.free();
    }

    /**
     * The FitsImageInfo has been updated.
     */
    public void imageInfoUpdate(FitsImageWidget iw, FitsImageInfo fii) {
	if ( (fii.ra != _ra) || (fii.dec != _dec) ||
	     (fii.posAngleDegrees != _posAngle)      ) {

	    _ra       = fii.ra;
	    _dec      = fii.dec;
	    _posAngle = fii.posAngleDegrees;

	    // System.out.println("imageInfoUpdate: " + fii);

	    _updateScreenLocations();
	}
    }
}

