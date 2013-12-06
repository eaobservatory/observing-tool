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

/**
 * A class for parsing numbers which traps exceptions.
 * 
 * @author Martin Folger
 */
package gemini.util ;

public class Format
{
	// Added by Martin Folger (26 February 2002)
	/**
     * Helper method.
     * 
     * @return double value of doubleStr if it can be parsed to double, 0.0
     *         otherwise.
     */
	public static double toDouble( String doubleStr )
	{
		double result = 0. ;

		try
		{
			result = Double.valueOf( doubleStr ) ;
		}
		catch( Exception ex ){}

		return result ;
	}

	// Added by Martin Folger (26 February 2002)
	/**
     * Helper method.
     * 
     * @return int value of intStr if it can be parsed to int, 0 otherwise.
     */
	public static int toInt( String intStr )
	{
		int result = 0 ;

		try
		{
			result = Integer.valueOf( intStr ) ;
		}
		catch( Exception ex ){}

		return result ;
	}
}
