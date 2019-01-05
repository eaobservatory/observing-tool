/*
 * Copyright (C) 2018 East Asian Observatory
 * All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful,but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc.,51 Franklin
 * Street, Fifth Floor, Boston, MA  02110-1301, USA
*/

package gemini.util;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Utility class to show dialog messages where the text is selectable,
 * in order to make reporting errors easier.
 */
public class SelectableDialog {
    public static void showError(String message) {
        showError("Error", message);
    }

    public static void showError(String title, String message) {
        showDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * General message dialog box method, with parameters matching those
     * of JOptionPane.showMessageDialog.
     */
    public static void showDialog(
            Component parentComponent, String message,
            String title, int messageType) {
        JTextArea text = new JTextArea(message, 15, 60);
        text.setEditable(false);
        text.setLineWrap(true);
        JOptionPane.showMessageDialog(
            null, new JScrollPane(text), title, messageType);
    }
}
