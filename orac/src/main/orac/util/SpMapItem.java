/*
 * Copyright 2002 United Kingdom Astronomy Technology Centre, an
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

package orac.util ;

/**
 * Iterators that implement the map feature
 * are displayed as "tiles" in the offset grid of the EdIterOffsetFeature
 * if they are inside the offset iterator.
 *
 * For instruments components something similar can be done by setting the
 * science area mode to SCI_AREA_ALL in {@link jsky.app.ot.editor.EdIterOffsetFeature}.
 *
 * This SpMapItem interface can be used for displaying iterators inside the offset iterator.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public interface SpMapItem
{
	public double getWidth() ;
	public double getHeight() ;
	public double getPosAngle() ;
}
