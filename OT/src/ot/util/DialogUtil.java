/*
 * ESO Archive
 * 
 * $Id$
 * 
 * who             when        what
 * --------------  ----------  ----------------------------------------
 * Allan Brighton  1999/05/03  Created
 */

package ot.util;
import java.awt.event.*;
import java.awt.Window;
import javax.swing.*;
import java.awt.Component;

/** 
 * Utility class with static methods for commonly used dialogs.
 *
 * @author Allan Brighton (modified by Martin Folger)
 */
public class DialogUtil extends jsky.util.gui.DialogUtil {

    /** 
     * Report an error message.
     * 
     * @param msg the error message
     */
    public static void error(Component parentComponent, String msg) {
	//if (useInternalDialogs) {
	//    JOptionPane.showInternalMessageDialog(parentComponent, msg, "Error", JOptionPane.ERROR_MESSAGE);	}
	//else {
	    JOptionPane.showMessageDialog(parentComponent, msg, "Error", JOptionPane.ERROR_MESSAGE); 
	//}
    }

    /** 
     * Report an error message based on the given exception.
     * 
     * @param e the exception containing the error information
     */
    public static void error(Component parentComponent, Exception e) {
	e.printStackTrace();

	String s = e.getMessage();
	if (s == null || s.trim().length() == 0)
	    s = e.toString();

	//if (useInternalDialogs) {
	//    JOptionPane.showInternalMessageDialog(parentComponent, s, "Error", JOptionPane.ERROR_MESSAGE);	}
	//else {
	    JOptionPane.showMessageDialog(parentComponent, s, "Error", JOptionPane.ERROR_MESSAGE); 
	//}
    }

    /** 
     * Report an error message based on the given message and exception.
     * 
     * @param msg the message to display
     * @param e the exception containing the error information
     */
    public static void error(Component parentComponent, String msg, Exception e) {
	e.printStackTrace();

	String s = msg + ": " + e.toString();

	//if (useInternalDialogs) {
	//    JOptionPane.showInternalMessageDialog(parentComponent, s, "Error", JOptionPane.ERROR_MESSAGE);	}
	//else {
	    JOptionPane.showMessageDialog(parentComponent, s, "Error", JOptionPane.ERROR_MESSAGE); 
	//}
    }


    /** 
     * Display an informational message.
     * 
     * @param msg the message
     */
    public static void message(Component parentComponent, String msg) {
	//if (useInternalDialogs) {
	//    JOptionPane.showInternalMessageDialog(parentComponent, msg, "Message", JOptionPane.INFORMATION_MESSAGE);	}
	//else {
	    JOptionPane.showMessageDialog(parentComponent, msg, "Message", JOptionPane.INFORMATION_MESSAGE); 
	//}
    }


    /** 
     * Get an input string from the user and return it. 
     *
     * @param msg the message to display
     * @return the value typed in by the user, or null if Cancel was pressed
     */
    public static String input(Component parentComponent, String msg) {
	//if (useInternalDialogs) {
	//    return JOptionPane.showInternalInputDialog(parentComponent, msg, "Input", JOptionPane.QUESTION_MESSAGE);	
	//}
	//else {
	    return JOptionPane.showInputDialog(parentComponent, msg, "Input", JOptionPane.QUESTION_MESSAGE); 
	//}
    }


    /** 
     * Display a confirm dialog with YES, NO, CANCEL buttons and return
     * a JOptionPane constant indicating the choice.
     *
     * @param msg the message to display
     * @return a JOptionPane constant indicating the choice
     */
    public static int confirm(Component parentComponent, String msg) {
	//if (useInternalDialogs) {
	//    return JOptionPane.showInternalConfirmDialog(parentComponent, msg);	
	//}
	//else {
	    return JOptionPane.showConfirmDialog(parentComponent, msg); 
	//}
    }
}


