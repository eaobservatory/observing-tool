package ot.gui;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import java.awt.HeadlessException;

public class GuiUtil {
	/**
	 * Convenience method to create a label
	 * with black text.
	 * @param text the text to display
	 * @return the label
	 */
	public static JLabel createLabel(String text) {
		return createLabel(text, Color.black);
	}

	/**
 	 * Convenience method to create a label of the 
 	 * specified colour text.
 	 * @param text the text to display
 	 * @param colour the colour for the text
 	 * @return the label
 	 */ 	
	public static JLabel createLabel(String text, Color colour) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Dialog", 0, 12));
		label.setForeground(colour);
		return label;
	}

	/**
	 * Attempts to pop up a warning, or failing that
	 * prints it to standard error.
	 * @param parent the parent GUI component
	 * @param message the body of the warning
	 * @param title the title of the warning
	 */
	public static void showWarning(Component parent, String message,
			String title) {
		try {
			JOptionPane.showMessageDialog(parent, message, title,
				JOptionPane.WARNING_MESSAGE);
		}
		catch (HeadlessException e) {
			System.err.println("WARNING: " + title);
			System.err.println(message);
		}
	}
}
