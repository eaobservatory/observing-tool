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

/**
 * This class watches a CheckBoxWidgetExt object to know which node is selected.
 *
 * @author  Dayle Kotturi, Allan Brighton (Swing Port)
 * @version 1.0, 8/8/97
 */

package jsky.app.ot.gui;

import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import jsky.util.gui.BasicWindowMonitor;

/**
 * An CheckBoxWidget that permits clients to register as button press watchers.
 */
@SuppressWarnings("serial")
public class CheckBoxWidgetExt extends JCheckBox implements DescriptiveWidget,
        ActionListener {
    // Observers
    private Vector<CheckBoxWidgetWatcher> _watchers =
            new Vector<CheckBoxWidgetWatcher>();

    /**
     * Like the "tip" but not shown automatically when the mouse rests on
     * the widget.
     *
     * @see #getDescription
     * @see #setDescription
     */
    public String description;

    /** Default constructor */
    public CheckBoxWidgetExt() {
        addActionListener(this);
    }

    /** Default constructor */
    public CheckBoxWidgetExt(String text) {
        this();
        setText(text);
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
     * Add a watcher.
     *
     * Watchers are notified when a button is pressed in the
     * option widget.
     */
    public synchronized final void addWatcher(CheckBoxWidgetWatcher cbw) {
        if (!_watchers.contains(cbw)) {
            _watchers.addElement(cbw);
        }
    }

    /**
     * Delete a watcher.
     */
    public synchronized final void deleteWatcher(CheckBoxWidgetWatcher cbw) {
        _watchers.removeElement(cbw);
    }

    /**
     * Delegate this method from the Observable interface.
     */
    public synchronized final void deleteWidgetWatchers() {
        _watchers.removeAllElements();
    }

    /**
     * Notify watchers that a button has been pressed in the option widget.
     */
    private void _notifyAction() {
        for (int i = 0; i < _watchers.size(); ++i) {
            CheckBoxWidgetWatcher cbw = _watchers.elementAt(i);
            cbw.checkBoxAction(this);
        }
    }

    /** Called when the button is pressed. */
    public void actionPerformed(ActionEvent ae) {
        _notifyAction();
    }

    public void setValue(boolean value) {
        setSelected(value);
    }

    public boolean getBooleanValue() {
        return isSelected();
    }

    /**
     * test main
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("CheckBoxWidgetExt");

        CheckBoxWidgetExt button = new CheckBoxWidgetExt("Push Me");

        button.addWatcher(new CheckBoxWidgetWatcher() {
            public void checkBoxAction(CheckBoxWidgetExt cbw) {
                System.out.println("OK");
            }
        });

        frame.add("Center", button);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new BasicWindowMonitor());
    }
}
