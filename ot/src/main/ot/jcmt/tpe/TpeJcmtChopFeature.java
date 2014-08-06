/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
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

package ot.jcmt.tpe ;

import gemini.sp.SpItem ;
import gemini.sp.iter.SpIterChop ;
import orac.jcmt.SpJCMTConstants ;
import orac.jcmt.iter.SpIterRasterObs ;
import orac.util.ScienceArea;
import jsky.app.ot.tpe.feat.TpeChopFeature ;
import jsky.app.ot.fits.gui.FitsImageInfo ;
import jsky.app.ot.fits.gui.FitsMouseEvent ;
import java.awt.Color ;
import java.awt.Graphics ;

/**
 * Draws the Chop position for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class TpeJcmtChopFeature extends TpeChopFeature
{
	/**
	 * Raster observations are drawn with two chop positions
	 * offset to either side by 1/2 of the chop distance
	 * instead of including the base position.
	 */
	protected boolean isInclusiveOfBase() {
		return ! containsRasterObservation(_iterChop);
	} 

	/**
	 * Draw the feature.
	 */
	public void draw( Graphics g , FitsImageInfo fii )
	{
		if (! _calc(fii)) return;
	
		if (_iterChop == null) return;

		int chopStepIndex = _iterChop.getSelectedIndex();
		if (chopStepIndex < 0) return;

		double scale = fii.pixelsPerArcsec;

		ScienceArea scienceArea = null;
		try {
			if (_iw.getInstrumentItem() != null ) {
				scienceArea = ScienceArea.fromArray(
					_iw.getInstrumentItem().getScienceArea());
			}
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}


		double chop1X = _chopX;
		double chop1Y = _chopY;
		double chop2X = 2 * _baseX - _chopX;
		double chop2Y = 2 * _baseY - _chopY;
		boolean includeCentrePosition = isInclusiveOfBase();


		g.setColor( Color.magenta ) ;

		if (scienceArea != null) {
			if (includeCentrePosition)
				scienceArea.draw(g, _drawAsCircle, _baseX, _baseY, scale);
			
			scienceArea.draw(g, _drawAsCircle, chop1X, chop1Y, scale);
			scienceArea.draw(g, _drawAsCircle, chop2X, chop2Y, scale);
		}
			

		// Draw the actual chop positions (at the centre of the science areas) as a small boxes.
		// The box at position (chop1X, chop1Y), i.e. the one with the label,
		// is used for dragging the position with the mouse.
		if (includeCentrePosition) 			
			g.drawRect((int) _baseX - 2, (int) _baseY - 2, 4, 4);

		g.drawRect((int) chop1X - 2, (int) chop1Y - 2, 4, 4);
		g.drawRect((int) chop2X - 2, (int) chop2Y - 2, 4, 4);

		//   g.setFont(FONT) ;
		g.drawString(_name + " (Step " + chopStepIndex + ")",
			(int) chop1X + 3, (int) chop1Y + 2);

		// If the exact position is not known due to the Az/El coordinate system
		// then draw two circles around the base. The science area is between these two circles.
		if (_drawAsCircle) drawCircles(g);
	}

	/**
	 * Drag to a new location.
	 */
	public void drag( FitsMouseEvent fme )
	{
		if( _dragObject != null )
		{
			_dragX = fme.xWidget ;
			_dragY = fme.yWidget ;

			_iterChop.setAngle( getAngle( fme.xOffset , fme.yOffset ) , _iterChop.getSelectedIndex() ) ;

			if (containsRasterObservation(_iterChop))
				_iterChop.setThrow( getThrow( fme.xOffset , fme.yOffset ) * 2. , _iterChop.getSelectedIndex() ) ;
			else
				_iterChop.setThrow( getThrow( fme.xOffset , fme.yOffset ) , _iterChop.getSelectedIndex() ) ;
		}
		_iw.repaint() ;
	}

	/**
	 * Checks whether there is a Scan/Raster Observe inside this Chop iterator.
	 */
	protected static boolean containsRasterObservation(SpIterChop spIterChop)
	{
		if (spIterChop == null) return false;

		SpItem child = spIterChop.child() ;
		while( child != null )
		{
			if( child instanceof SpIterRasterObs )
				return true ;

			child = child.next() ;
		}

		return false ;
	}

	/**
	 * Return true if the chop position should be drawn as circle
	 * given the coordFrame.
	 */
	protected boolean drawAsCircle( String coordFrame )
	{
		if( coordFrame.equals( SpJCMTConstants.CHOP_SYSTEMS[ SpJCMTConstants.CHOP_SYSTEM_TARCKING ] ) )
			return false ;
		else
			return super.drawAsCircle( coordFrame ) ;
	}
}
