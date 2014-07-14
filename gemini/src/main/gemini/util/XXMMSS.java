/*
 * Copyright (C) 2008 Science and Technology Facilities Council.
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

package gemini.util ;

public class XXMMSS
{
	protected static final String arcseconds = "[0-5]?[0-9]{1}" ;
	protected static final String degrees = "([0-1][0-7][0-9]|[0]?[0-9]?[0-9]{1})" ;
	protected static final String hours = "([0-2][0-3]|[0-1]?[0-9]{1})" ;
	protected static final String miliarcseconds = "\\.[0-9]?[0-9]?[0-9]{1}" ;
	
	protected static final String spacePattern = "([ ]{1,2}" + arcseconds + "([ ]{1,2}" + arcseconds + "(" + miliarcseconds + ")?)?)?" ;
	protected static final String rapattern =  hours + spacePattern ;
	protected static final String decpattern = "^(\\+|-)?" + degrees + spacePattern ;

	protected static final String colonPattern = "(:" + arcseconds + "(:" + arcseconds + "(" + miliarcseconds + ")?)?)?" ;
	protected static final String rapatterncolon =  hours + colonPattern ;
	protected static final String decpatterncolon = "^(\\+|-)?" + degrees + colonPattern ;
	
	public static double[] stringTodoubleTriplet( String hhmmss )
	{
		double[] values = { 0. , 0. , 0. } ;

		String[] split = hhmmss.split( "([:]|[ ]+)" ) ;
		for( int index = 0 ; index < split.length && index < values.length ; index++ )
		{
			String current = split[ index ].trim() ;
			values[ index ] = new Double( current ) ;
		}

		return values ;
	}
}
