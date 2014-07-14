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

package orac.util;

import java.awt.Graphics;

/**
 * Utility class to describe science areas for plotting.
 */

public abstract class ScienceArea {

	/**
	 * Transitional method to convert from an array to a
	 * ScienceArea object.
	 */ 
	public static ScienceArea fromArray(double[] array) {
		if (array == null) return null;

		switch (array.length) {
			case 1:
				return new ScienceArea.Circle(array[0]);
			case 2:
				return new ScienceArea.Rectangle(array[0], array[1]);
			default:
				throw new RuntimeException("Don't know how to "
					+ "interpret science area array of length " 
					+ array.length);
		}
	}


	/**
	 * Determine size of bounding circle.
	 */
	public abstract double getBoundingRadius();

	/**
	 * Draw the area.
	 *
	 * @param g Graphics context
	 * @param circular draw circular representation
	 * @param xOffset pixel position of the target x coordinate
	 * @param yOffset pixel position of the target y coordinate
	 * @param scale pixels per arcsecond
	 */
	public void draw(Graphics g, boolean circular, double xOffset,
			double yOffset, double scale) {
		if (circular) {
			double radius = getBoundingRadius();
			g.drawOval((int)(xOffset - scale * radius),
				(int)(yOffset - scale * radius),
				(int)(2 * scale * radius), (int)(2 * scale * radius));
		}
		else {
			drawArea(g, xOffset, yOffset, scale);
		}
	}

	/**
	 * Draw the actual area.
	 *
	 * @param g Graphics context
	 * @param xOffset pixel position of the target x coordinate
	 * @param yOffset pixel position of the target y coordinate
	 * @param scale pixels per arcsecond
	 */
	protected abstract void drawArea(Graphics g, double xOffset, double yOffset, double scale);


	public static class Circle extends ScienceArea {
		private final double radius;

		public Circle(double radius) {
			this.radius = radius;
		}

		public double getBoundingRadius() {
			return radius;
		}

		protected void drawArea(Graphics g, double xOffset, double yOffset, double scale) {
			draw(g, true, xOffset, yOffset, scale);
		}
	}

	public static class Rectangle extends ScienceArea {
		private final double width;
		private final double height;

		public Rectangle(double width, double height) {
			this.width = width;
			this.height = height;
		}

		public double getBoundingRadius() {
			return Math.sqrt(width * width + height * height) / 2;
		}

		protected void drawArea(Graphics g, double xOffset, double yOffset, double scale) {
			g.drawRect((int)(xOffset - scale * width / 2),
				(int)(yOffset -  scale * height / 2),
				(int)(scale * width), (int)(scale * height));
		}
	}
}
