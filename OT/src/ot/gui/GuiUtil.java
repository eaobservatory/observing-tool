package ot.gui;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class GuiUtil {
	// convenience methods to reduce space
	
	public static JLabel createLabel(String text) {
		return createLabel(text, Color.black);
	}
	
	public static JLabel createLabel(String text, Color colour) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Dialog", 0, 12));
		label.setForeground(colour);
		return label;
	}
}
