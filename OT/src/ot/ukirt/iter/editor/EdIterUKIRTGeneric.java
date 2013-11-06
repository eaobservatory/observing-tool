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

package ot.ukirt.iter.editor ;

import jsky.app.ot.editor.EdIterGenericConfig ;

/**
 * @author M.Folger@roe.ac.uk
 */
public class EdIterUKIRTGeneric extends EdIterGenericConfig
{
	/**
	 * Prevents instrument aperture attributes to be displayed in the editor.
	 *
	 * Instrument aperture config items depend only on other config items
	 * (such as readout area) and should not be set by the user directly.
	 */
	protected boolean isUserEditable( String attribute )
	{
		if( attribute.toLowerCase().startsWith( "instaper" ) )
			return false ;
		else
			return super.isUserEditable( attribute ) ;
	}
}
