/*
 * Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ot.gui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JFrame;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import jsky.util.gui.BasicWindowMonitor;
import jsky.app.ot.gui.DescriptiveWidget;

/**
 * A TextBoxWidget that permits clients to register as key press watchers.
 *
 * @author Shane Walker, Allan Brighton (Swing port)
 */
@SuppressWarnings("serial")
public class PasswordWidgetExt extends JPasswordField implements
        DescriptiveWidget, DocumentListener, ActionListener {
    /**
     * Like the "tip" but not shown automatically when the mouse rests on
     * the widget.
     * @see #getDescription
     * @see #setDescription
     */
    public String description;

    /** Default constructor */
    public PasswordWidgetExt() {
        getDocument().addDocumentListener(this);
        addActionListener(this);
    }

    // -- For the DocumentListener interface --

    /**
     * Gives notification that there was an insert into the
     * document.
     *
     * The range given by the DocumentEvent bounds the freshly inserted region.
     */
    public void insertUpdate(DocumentEvent e) {
    }

    /**
     * Gives notification that a portion of the document has been
     * removed.
     *
     * The range is given in terms of what the view last saw
     * (that is, before updating sticky positions).
     */
    public void removeUpdate(DocumentEvent e) {
    }

    /**
     * Gives notification that an attribute or set of attributes changed.
     */
    public void changedUpdate(DocumentEvent e) {
    }

    // -- For the ActionListener interface --

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * Set the description.
     * @see #description
     */
    public void setDescription(String newDescription) {
        description = newDescription;
    }

    /**
     * Get the description.
     * @see #description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the current value as a double.
     */
    public double getDoubleValue(double def) {
        try {
            return (Double.valueOf(getValue()));
        } catch (Exception ex) {
        }

        return def;
    }

    /**
     * Set the current value as a double.
     */
    public void setValue(double d) {
        setText(String.valueOf(d));
    }

    /**
     * Get the current value as an int.
     */
    public int getIntegerValue(int def) {
        try {
            return Integer.parseInt(getValue());
        } catch (Exception ex) {
        }

        return def;
    }

    /**
     * Set the current value as an int.
     */
    public void setValue(int i) {
        setText(String.valueOf(i));
    }

    /**
     * Set the current value.
     */
    public void setValue(String s) {
        setText(s);
    }

    /**
     * Return the current value.
     */
    public String getValue() {
        return new String(getPassword());
    }

    /**
     * test main
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("PasswordWidgetExt");

        PasswordWidgetExt tbw = new PasswordWidgetExt();

        frame.add("Center", tbw);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new BasicWindowMonitor());
    }
}
