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

package ot.util ;

import javax.swing.JOptionPane ;
import java.awt.Component ;

/** 
 * Utility class with static methods for commonly used dialogs.
 *
 * @author Martin Folger
 */
public class DialogUtil extends jsky.util.gui.DialogUtil
{
	/** 
	 * Report an error message based on the given exception.
	 * 
	 * @param e the exception containing the error information
	 */
	public static void error( Component parentComponent , Exception e )
	{
		e.printStackTrace() ;

		String s = e.getMessage() ;
		if( s == null || s.trim().length() == 0 )
			s = e.toString() ;

		JOptionPane.showMessageDialog( parentComponent , s , "Error" , JOptionPane.ERROR_MESSAGE ) ;
	}

	/** 
	 * Report an error message based on the given message and exception.
	 * 
	 * @param msg the message to display
	 * @param e the exception containing the error information
	 */
	public static void error( Component parentComponent , String msg , Exception e )
	{
		e.printStackTrace() ;

		String s = msg + ": " + e.toString() ;

		JOptionPane.showMessageDialog( parentComponent , s , "Error" , JOptionPane.ERROR_MESSAGE ) ;
	}
}
