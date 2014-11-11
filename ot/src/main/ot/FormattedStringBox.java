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

package ot;

import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.io.PrintWriter;
import java.io.IOException;

/**
 * Frame for displaying formatted ASCII text.
 *
 * Text is displayed in a monospaced font in order to
 * keep the layout (tables etc.) of the ASCII text.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class FormattedStringBox extends ReportBox {
    /**
     * JTextArea is used instead of JTextPane of the super class.
     *
     * The reason is that the pack() method does not always use the space
     * required by the displayed text to adjust the the overall size of the
     * frame.  JTextArea works better with pack(). So _textPane of the super
     * class is removed from the frame and _textArea is added instead.
     */
    protected JTextArea _textArea = new JTextArea();

    protected FormattedStringBox() {
        _fileChooser.addChoosableFileFilter(_asciiFileFilter);

        _textArea.setFont(new Font("Monospaced", 0, 10));

        _printButton.setVisible(false);
        _saveButton.setVisible(false);

        remove(_textPane);
        jScrollPane1.getViewport().add(_textArea, null);

        setVisible(true);
    }

    public FormattedStringBox(String message) {
        this();

        _textArea.setText(message);
        int columns = message.indexOf("\n") + 5;
        _textArea.setColumns(columns);
        _textArea.setRows(30);
        _textArea.setLineWrap(true);
        _textArea.setWrapStyleWord(true);
        setLocation(100, 300);
        pack();
        setVisible(true);
    }

    public FormattedStringBox(String message, String title) {
        this(message);
        setTitle(title);
    }

    /**
     * Print not supported for FormattedStringBox.
     */
    public void print() {
    }

    public void save() {
        if (_fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String fileName = _fileChooser.getSelectedFile().getPath();

            if (fileName == null) {
                try {
                    PrintWriter printWriter = new PrintWriter(fileName);
                    String[] split = _textArea.getText().split("\n");

                    int i = 0;

                    while (i < split.length) {
                        printWriter.println(split[i++]);
                    }

                    printWriter.flush();
                    printWriter.close();

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                            "Problems writing to file \"" + fileName
                                + "\": " + e,
                            "Save Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
