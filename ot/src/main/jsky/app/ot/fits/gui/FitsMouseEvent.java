// Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package jsky.app.ot.fits.gui ;

import jsky.app.ot.gui.image.ViewportMouseEvent ;

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
	public double ra ;

	/**
	 * The Dec of the event in degrees.
	 */
	public double dec ;

	/**
	 * The RA of the event as a String in HHMMSS format.
	 */
	public String raStr ;

	/**
	 * The Dec of the event as a String in DDMMSS format.
	 */
	public String decStr ;

	/**
	 * The X offset of the event from the base position in arcsec.
	 */
	public double xOffset ;

	/**
	 * The Y offset of the event from the base position in arcsec.
	 */
	public double yOffset ;

	/**
	 * The X offset of the event from the base position in arcsec as a String.
	 */
	public String xOffsetStr ;

	/**
	 * The Y offset of the event from the base position in arcsec as a String.
	 */
	public String yOffsetStr ;

	/**
	 * Returns a human-readable string describing the contents of
	 * the FitsMouseEvent.
	 */
	public String toString()
	{
		return "FitsMouseEvent[" + super.toString() + ", raStr=" + raStr + ", decStr=" + decStr + ", xOffsetStr=" + xOffsetStr + ", yOffsetStr=" + yOffsetStr + "]" ;
	}
}
