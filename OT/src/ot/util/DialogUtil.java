/*
 * ESO Archive
 * 
 * $Id$
 * 
 * who             when        what
 * --------------  ----------  ----------------------------------------
 * Allan Brighton  1999/05/03  Created
 */

package ot.util ;

import javax.swing.JOptionPane ;
import java.awt.Component ;

/** 
 * Utility class with static methods for commonly used dialogs.
 *
 * @author Allan Brighton (modified by Martin Folger)
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
