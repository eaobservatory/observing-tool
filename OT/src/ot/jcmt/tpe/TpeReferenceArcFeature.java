package ot.jcmt.tpe;

import java.util.Vector;
import java.awt.Color;
import java.awt.Graphics;

import gemini.sp.SpItem;
import gemini.sp.SpTelescopePos ;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpSchedConstObsComp;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.util.Angle;
import gemini.util.CoordSys;
import gemini.util.DDMMSS;
import orac.jcmt.iter.SpIterFTS2;
import orac.util.SpItemUtilities;
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
		super("Sausages", "Reference Arc");

		arcs = new Vector<ArcRange>();
	}

	private class ArcRange {
		public double angleStart;
		public double angleEnd;

		public ArcRange(double start, double end) {
			angleStart = start;
			angleEnd = end;
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

		// TODO: handle other coordinate system
		if (basePosition.getCoordSys() != CoordSys.FK5) {
			// See for example EdIterJCMTGeneric for how to convert...
			System.err.println("Wrong coordinate system in TpeReferenceArcFeature");
			return false;
		}

		double declination = basePosition.getYaxis();


		// Calculate maximum source elevation

		// TODO: check this expression
		// (a check: it does lead to acos(-1) for vertical angle)
		double sourceMaxElevation = 90
				- Math.abs(declination - latitude);

		
		// Find range of observable elevations

		double elevationMin = 0;
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

		if (base instanceof SpIterFTS2) {
			SpIterFTS2 fts2 = (SpIterFTS2) base;

			radFov = 60;
			radOffset = 180;
			double offsetAngle = 45;

			int trackingPort = fts2.getTrackingPort();

			switch (trackingPort) {
				case 1:
					break;
				case 2:
					offsetAngle += 180;
					break;
				default:
					return false;
			}

			NasmythPlatform platform = NasmythPlatform.LEFT;

			double riseStart = offsetAngle + nasmythAngle(latitude,
				elevationMin, declination, false, platform);

			double setEnd = offsetAngle + nasmythAngle(latitude,
				elevationMin, declination, true, platform);


			if (seeTransit && rising && setting) {
				arcs.add(new ArcRange(riseStart, setEnd));
			}
			else {
				if (rising) {
					arcs.add(new ArcRange(riseStart,
						offsetAngle + nasmythAngle(
							latitude, elevationMax,
							declination, false,
							platform)));
				}

				if (setting) {
					arcs.add(new ArcRange(
						offsetAngle + nasmythAngle(
							latitude, elevationMax,
							declination, true,
							platform),
						setEnd));
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
			(int) (90 + range.angleStart),
			(int) (range.angleEnd - range.angleStart));

		g.drawArc((int) (x0 - (scale * radOuter)),
			(int) (y0 - (scale * radOuter)),
			(int) (2 * scale * radOuter),
			(int) (2 * scale * radOuter),
			(int) (90 + range.angleStart),
			(int) (range.angleEnd - range.angleStart));

		double x1 = x0 - scale * radOffset * Math.sin(Math.PI * range.angleStart / 180);
		double y1 = y0 - scale * radOffset * Math.cos(Math.PI * range.angleStart / 180);
		int endCap = (range.angleEnd > range.angleStart) ? -180 : 180;

		g.drawArc((int) (x1 - (scale * radFov)),
			(int) (y1 - (scale * radFov)),
			(int) (2 * scale * radFov),
			(int) (2 * scale * radFov),
			(int) (90 + range.angleStart),
			endCap);

		double x2 = x0 - scale * radOffset * Math.sin(Math.PI * range.angleEnd / 180);
		double y2 = y0 - scale * radOffset * Math.cos(Math.PI * range.angleEnd / 180);

		g.drawArc((int) (x2 - (scale * radFov)),
			(int) (y2 - (scale * radFov)),
			(int) (2 * scale * radFov),
			(int) (2 * scale * radFov),
			(int) (90 + range.angleEnd),
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
	 * Calculate angle to pole as seen by a Nasmyth instrument.
	 */
	protected static double nasmythAngle(double latitudeDeg,
			double elevationDeg, double declinationDeg,
			boolean setting, NasmythPlatform platform) {
		double latitude = Angle.degreesToRadians(latitudeDeg);
		double elevation = Angle.degreesToRadians(elevationDeg);
		double declination = Angle.degreesToRadians(declinationDeg);

		double verticalAngle = Math.acos(
			(Math.sin(latitude)
				- Math.sin(declination) * Math.sin(elevation))
			/ (Math.cos(declination) * Math.cos(elevation)));

		if (setting) verticalAngle = 2 * Math.PI - verticalAngle;

		return Angle.radiansToDegrees(verticalAngle
			+ platform.getSign() * elevation);
	}


	/**
	 * Main method to test nasmythAngle function.
	 */
	public static void main(String[] args) {
		double dec = 20;
		double lat = 20;
		
		double maxEl = 90 - Math.abs(dec - lat);

		for (double el = 0; el < maxEl; el ++) {
			System.out.println(el + " " + nasmythAngle(lat,
				el, dec, false, NasmythPlatform.LEFT));
		}

		for (double el = maxEl; el > 0; el --) {
			System.out.println(el + " " + nasmythAngle(lat,
				el, dec, true, NasmythPlatform.LEFT));
		}
	}
}
