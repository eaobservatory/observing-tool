/*
 * Copyright (C) 2012 Science and Technology Facilities Council.
 * All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ot.jcmt.tpe;

import java.util.Vector;
import java.awt.Color;
import java.awt.Graphics;

import gemini.sp.SpItem;
import gemini.sp.SpTelescopePos ;
import gemini.sp.obsComp.SpSchedConstObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.util.Angle;
import gemini.util.CoordSys;
import gemini.util.RADec;
import gemini.util.DDMMSS;
import orac.jcmt.iter.SpIterFTS2Obs;
import orac.util.SpItemUtilities;
import orac.util.CoordConvert;
import jsky.app.ot.OtCfg;
import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.tpe.TpeImageFeature;

/**
 * Draws reference position arcs.
 *
 * The arc will show the range of possible positions for the reference
 * beam, depending on the scheduling constraints.
 *
 * To be used, for example, for the reference array in a FTS2 observation.
 */

public class TpeReferenceArcFeature extends TpeImageFeature {

	/**
	 * Radius of the field of view in arcseconds.
	 */
	private double radFov = 0;

	/**
	 * Offset between the base position and reference position.
	 */
	private double radOffset = 0;

	/**
	 * List of arcs to be plotted.
	 */
	private Vector<ArcRange> arcs;


	public TpeReferenceArcFeature() {
		super("Ref. Arcs", "Arcs showing the possible positions of the reference beam.");

		arcs = new Vector<ArcRange>();
	}

	private static class ArcRange {
		public double angleStart;
		public double angleEnd;

		public ArcRange(double start, double end) {
			angleStart = start;
			angleEnd = end;
		}

		public void offset(double offset) {
			angleStart += offset;
			angleEnd += offset;
		}

		public void checkOrder() {
			if (angleStart > angleEnd) {
				double angle = angleStart;
				angleStart = angleEnd;
				angleEnd = angle;
			}
		}

		public static ArcRange merge(ArcRange a, ArcRange b) {
			a.checkOrder();
			b.checkOrder();
			return new ArcRange(
				Math.min(a.angleStart, b.angleStart),
				Math.max(a.angleEnd, b.angleEnd));
		}
	}


	/**
	 * Prepares arc drawing information.
	 *
	 * @return true on success
	 */
	private boolean calculateArcs() {
		arcs.clear();
		
		// Find latitude of telescope.

		double latitude = DDMMSS.valueOf(OtCfg.getTelescopeLatitude());


		// Get instrument object (if required).
		//SpInstObsComp instrument = _iw.getInstrumentItem();
		

		// Find the declination of the base position
		
		SpItem telescope = _iw.getTelescopeItem();
		if (! (telescope instanceof SpTelescopeObsComp)) {return false;}
		
		SpTelescopePos basePosition = ((SpTelescopeObsComp) telescope).getPosList().getBasePosition();

		double declination = 0;

		if (basePosition.getCoordSys() == CoordSys.FK5) {
			declination = basePosition.getYaxis();
		}
		else {
			try {
				RADec raDec = CoordConvert.convert(
					basePosition.getXaxis(), basePosition.getYaxis(),
					basePosition.getCoordSys(), CoordSys.FK5);
				declination = raDec.dec;
			}
			catch (UnsupportedOperationException e) {
				System.err.println("Wrong coordinate system in TpeReferenceArcFeature");
				return false;
			}
		}

		// Calculate maximum source elevation

		// TODO: check this expression
		// (a check: it does lead to acos(-1) for vertical angle)
		double sourceMaxElevation = 90
				- Math.abs(declination - latitude);

		
		// Find range of observable elevations

		double elevationMin = 20; // assumed minimum elevation
		double elevationMax = 90;
		boolean rising = true;
		boolean setting = true;
		boolean seeTransit = false;

		SpItem base = _iw.getBaseItem();
		if (base == null) {return false;}

		SpSchedConstObsComp sched = SpItemUtilities.findSchedConstraint(base);

		if (sched != null) {
			String minElevation = sched.getMinElevation();
			String maxElevation = sched.getMaxElevation();
			String meridianApproach = sched.getMeridianApproach();

			if (meridianApproach != null && SpSchedConstObsComp.SOURCE_RISING.equals(meridianApproach)) {
				setting = false;
			}
			else if (meridianApproach != null && SpSchedConstObsComp.SOURCE_SETTING.equals(meridianApproach)) {
				rising = false;
			}

			if (minElevation != null) {
				try {
					elevationMin = Double.parseDouble(minElevation);
				}
				catch (NumberFormatException e) {
					System.err.println("Bad number format from SpSchedConstObsComp.minElevation()");
				}
			}

			if (maxElevation != null) {
				try {
					elevationMax = Double.parseDouble(maxElevation);
				}
				catch (NumberFormatException e) {
					System.err.println("Bad number format from SpSchedConstObsComp.maxElevation()");
				}

			}

		}

		
		// Check if source constrains maximum elevation
		
		if (elevationMax > sourceMaxElevation) {
			elevationMax = sourceMaxElevation;
			seeTransit = true;
		}

		
		// Check the base item (which has caused this class to be loaded)

		if (base instanceof SpIterFTS2Obs) {
			SpIterFTS2Obs fts2 = (SpIterFTS2Obs) base;

			radFov = 93.3;
			radOffset = 210.2;
			double offsetAngle = 90;

			// Reference arcs only apply in dual port mode
			boolean dualPort = fts2.isDualPort();
			if (! dualPort) return true;

			String trackingPort = fts2.getTrackingPort();

                        if (SpIterFTS2Obs.PORT_8D.equals(trackingPort)) {
                        }
                        else if (SpIterFTS2Obs.PORT_8C.equals(trackingPort)) {
				offsetAngle += 180;
                        }
                        else {
				return false;
			}

			NasmythPlatform platform = NasmythPlatform.LEFT;


			ArcRange rangeRising = null;
			ArcRange rangeSetting = null;

			if (rising) {
				rangeRising = nasmythAngleRange(latitude,
					elevationMin, elevationMax,
					declination, false,
					platform);
				rangeRising.offset(offsetAngle);
			}

			if (setting) {
				rangeSetting = nasmythAngleRange(latitude,
					elevationMin, elevationMax,
					declination, true,
					platform);
				rangeSetting.offset(offsetAngle);
			}


			if (seeTransit && rising && setting) {
				arcs.add(ArcRange.merge(rangeRising,
							rangeSetting));
			}
			else {
				if (rising) {
					arcs.add(rangeRising);
				}

				if (setting) {
					arcs.add(rangeSetting);
				}
			}

			return true;
		}
		
		return false;
	}


	/**
	 * Draws reference arcs.
	 *
	 * @param g Graphics context
	 * @param fii FitsImageInfo
	 */
	public void draw(Graphics g , FitsImageInfo fii) {
		if (! calculateArcs()) return;

		//arcs.clear();
		//arcs.add(new ArcRange(0, 90));
		
		// TODO: check that baseScreenPos always gives the position of the actual target
		double x0 = fii.baseScreenPos.x;
		double y0 = fii.baseScreenPos.y;
		double scale = fii.pixelsPerArcsec;

		g.setColor(Color.magenta);

		drawTargetArea(g, x0, y0, scale, radFov);

		for (ArcRange arc: arcs) {
			drawReferenceArc(g, x0, y0, scale, radFov, radOffset, arc);
		}
	}


	/**
	 * Draws a circle representing the target area.
	 *
	 * @param g Graphics context
	 * @param x0 pixel position of the target x coordinate
	 * @param y0 pixel position of the target y coordinate
	 * @param scale pixels per arcsecond
	 * @param radFov the radius of the field of view in arcseconds
	 */
	private void drawTargetArea(Graphics g,
			double x0, double y0, double scale,
			double radFov) {

		g.drawOval((int) (x0 - scale * radFov), 
			(int) (y0 - scale * radFov),
			(int) (2 * radFov * scale),
			(int) (2 * radFov * scale));
	}


	/**
	 * Draws an arc.
	 *
	 * @param g Graphics context
	 * @param x0 pixel position of target x coordinate
	 * @param y0 pixel position of target y coordinate
	 * @param scale pixels per arcsecond
	 * @param radFov the radius of the field of view in arcseconds
	 * @param radOffset the distance between the target and reference positions in arcseconds
	 * @param angleStart the angle at which to begin the arc in degrees
	 * @param angleEnd the angle at which to end the arc in degrees
	 */
	private void drawReferenceArc(Graphics g,
			double x0, double y0, double scale,
			double radFov, double radOffset,
			ArcRange range) {

		double radInner = radOffset - radFov;
		double radOuter = radOffset + radFov;

		g.drawArc((int) (x0 - (scale * radInner)),
			(int) (y0 - (scale * radInner)),
			(int) (2 * scale * radInner),
			(int) (2 * scale * radInner),
			(int) (90 - range.angleStart),
			(int) (range.angleStart - range.angleEnd));

		g.drawArc((int) (x0 - (scale * radOuter)),
			(int) (y0 - (scale * radOuter)),
			(int) (2 * scale * radOuter),
			(int) (2 * scale * radOuter),
			(int) (90 - range.angleStart),
			(int) (range.angleStart - range.angleEnd));

		double x1 = x0 + scale * radOffset * Math.sin(Math.PI * range.angleStart / 180);
		double y1 = y0 - scale * radOffset * Math.cos(Math.PI * range.angleStart / 180);
		int endCap = (range.angleEnd < range.angleStart) ? -180 : 180;

		g.drawArc((int) (x1 - (scale * radFov)),
			(int) (y1 - (scale * radFov)),
			(int) (2 * scale * radFov),
			(int) (2 * scale * radFov),
			(int) (90 - range.angleStart),
			endCap);

		double x2 = x0 + scale * radOffset * Math.sin(Math.PI * range.angleEnd / 180);
		double y2 = y0 - scale * radOffset * Math.cos(Math.PI * range.angleEnd / 180);

		g.drawArc((int) (x2 - (scale * radFov)),
			(int) (y2 - (scale * radFov)),
			(int) (2 * scale * radFov),
			(int) (2 * scale * radFov),
			(int) (90 - range.angleEnd),
			-endCap);

	}


	/**
	 * Enum of platforms.
	 */
	protected enum NasmythPlatform {
		LEFT(-1),
		RIGHT(+1);

		private final double sign;
		NasmythPlatform(double sign) {
			this.sign = sign;
		}
		public double getSign() {return sign;}
	}


	/**
	 * Calculate parallactic angle.
	 *
	 * Returns a value in the range -180 - 360 degrees
	 * in order to be able to give a continuous range of
	 * angles for sources transiting to either side of the zenith.
	 *
	 * TODO: move this to Pal?
	 */ 
	private static double parallacticAngle(double latitudeDeg,
			double elevationDeg, double declinationDeg,
			boolean setting) {
		double latitude = Angle.degreesToRadians(latitudeDeg);
		double elevation = Angle.degreesToRadians(elevationDeg);
		double declination = Angle.degreesToRadians(declinationDeg);

		double verticalAngle = Math.acos(
			(Math.sin(latitude)
				- Math.sin(declination) * Math.sin(elevation))
			/ (Math.cos(declination) * Math.cos(elevation)));

		if (setting) {
			if (declination >= latitude)
				verticalAngle = 2 * Math.PI - verticalAngle;
			else
				verticalAngle = - verticalAngle;
		}

		return Angle.radiansToDegrees(verticalAngle);
	}


	/**
	 * Calculate angle to pole as seen by a Nasmyth instrument.
	 */
	private static double nasmythAngle(double latitudeDeg,
			double elevationDeg, double declinationDeg,
			boolean setting, NasmythPlatform platform) {

		double verticalAngle = parallacticAngle(latitudeDeg,
				elevationDeg, declinationDeg, setting);

		return verticalAngle + platform.getSign() * elevationDeg;
	}

	
	/**
	 * Calculate range of nasmyth angles for given elevantion range,
	 */
	private static ArcRange nasmythAngleRange(double latitudeDeg,
			double elevationDegMin, double elevationDegMax,
			double declinationDeg,
			boolean setting, NasmythPlatform platform) {

		double angleMin = 0;
		double angleMax = 0;
		boolean first = true;

		if (elevationDegMax < elevationDegMin) {
			double elevationDeg = elevationDegMax;
			elevationDegMax = elevationDegMin;
			elevationDegMin = elevationDeg;
		}

		for (double elevationDeg = elevationDegMin;
				elevationDeg <= elevationDegMax;
				elevationDeg += 0.5) {

			double angle = nasmythAngle(latitudeDeg,
				elevationDeg, declinationDeg,
				setting, platform);

			if (first || angle < angleMin) angleMin = angle;
			if (first || angle > angleMax) angleMax = angle;

			first = false;
		}

		return new ArcRange(angleMin, angleMax);
	}


	/**
	 * Main method to test parallacticAngle and nasmythAngle functions.
	 */
	public static void main(String[] args) {
		assert(args.length == 1);
		double dec = Double.parseDouble(args[0]);
		double lat = 20;
		
		double maxEl = 90 - Math.abs(dec - lat);

		for (double el = 0; el < maxEl; el ++) {
			System.out.println(el + " "
				+ parallacticAngle(lat,
					el, dec, false)
				+ " "
				+ nasmythAngle(lat,
					el, dec, false, NasmythPlatform.LEFT));
		}

		for (double el = maxEl; el > 0; el --) {
			System.out.println(el + " " 
				+ parallacticAngle(lat,
					el, dec, true)
				+ " "
				+ nasmythAngle(lat,
					el, dec, true, NasmythPlatform.LEFT));
		}
	}
}
