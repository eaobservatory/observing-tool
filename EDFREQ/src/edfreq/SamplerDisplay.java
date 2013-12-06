/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
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

package edfreq ;

import javax.swing.JLabel ;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
@SuppressWarnings( "serial" )
public class SamplerDisplay extends JLabel implements SamplerWatcher
{
	public SamplerDisplay( String text )
	{
		super( text ) ;
	}

	public void updateSamplerValues( double centre , double width , int channels )
	{
		setText( String.valueOf( centre ) ) ;
	}
}
