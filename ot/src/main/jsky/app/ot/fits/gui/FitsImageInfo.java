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

import java.awt.geom.Point2D ;

public final class FitsImageInfo
{
	/**
	 * Screen coordinates of the center of the image.
	 */
	public Point2D.Double baseScreenPos ;

	/**
	 * RA of the center in degrees.
	 */
	public double ra = 0. ;

	/**
	 * Dec of the center in degrees.
	 */
	public double dec = 0. ;

	/**
	 * Scale of the image in pixels per arcsec.
	 */
	public double pixelsPerArcsec = 1. ;

	/**
	 * Due north in the sky relative to up in the image.
	 */
	public double theta = 0. ;

	/**
	 * The current position angle (in degrees).
	 */
	public double posAngleDegrees = 0. ;

	/**
	 * Standard debugging method.
	 */
	public String toString()
	{
		return getClass().getName() + "[baseScreenPos=" + baseScreenPos + ", ra=" + ra + ", dec=" + dec + ", pixelsPerArcsec=" + pixelsPerArcsec + ", theta=" + theta + ", posAngleDegrees=" + posAngleDegrees + "]" ;
	}
}
