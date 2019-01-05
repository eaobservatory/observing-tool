/*
 * Copyright (c) 2000 Association of Universities for Research in Astronomy, Inc. (AURA)
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

package jsky.app.ot.gui;

import gemini.util.ObservingToolUtilities;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.AbstractAction;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.border.BevelBorder;
import jsky.util.gui.BasicWindowMonitor;

/**
 * This widget displays a blinking red LED and a stop sign button and
 * can be used to give feedback when a background thread is running
 * and allow the user to interrupt the thread by pressing the stop
 * button.
 *
 * Both the LED and the stop button are displayed according to
 * the current state, which may be set by the caller.
 */
@SuppressWarnings("serial")
public class StopActionWidget extends JPanel {
    /** Handle for the "LED" button */
    protected JButton led;

    /** Handle for the "Stop" button */
    protected JButton stopButton;

    // The watchers interested in when the actions are stopped
    private Vector<StopActionWatcher> _watchers =
            new Vector<StopActionWatcher>();

    protected String imgpath = "jsky/app/ot/images/";
    // protected static ClassLoader cl = StopActionWidget.class.getClassLoader();

    /**
     * Action linked to the stop button and LED.
     */
    protected AbstractAction stopAction = new AbstractAction("Stop") {
        public void actionPerformed(ActionEvent evt) {
            setEnabled(false);
        }
    };

    /** Timer used for blinking the LED. */
    protected Timer timer;

    /** The current state of the led (for blinking). */
    protected boolean ledState;

    /**
     * Create the red LED and the stop button.
     *
     * The stop button is bound to the given stopAction.
     * When the stop button is enabled,
     * the LED also blinks red.
     */
    public StopActionWidget() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        add(createLed());
        add(createStopButton());
    }

    /**
     * Create the red LED button
     */
    protected JButton createLed() {
        URL url = ObservingToolUtilities.resourceURL(imgpath + "red_led.gif");
        led = new JButton(new ImageIcon(url));
        led.setFocusPainted(false);
        led.setBorderPainted(false);

        url = ObservingToolUtilities.resourceURL(imgpath
                + "red_led_disabled.gif");
        led.setDisabledIcon(new ImageIcon(url));
        led.addActionListener(stopAction);

        return led;
    }

    /**
     * Create the Stop button.
     */
    protected JButton createStopButton() {
        URL url = ObservingToolUtilities.resourceURL(imgpath + "stop-sign.gif");
        stopButton = new JButton(new ImageIcon(url));
        stopButton.setFocusPainted(false);
        stopButton.setBorder(new BevelBorder(BevelBorder.RAISED));

        url = ObservingToolUtilities.resourceURL(
                imgpath + "stop-sign-disabled.gif");
        stopButton.setDisabledIcon(new ImageIcon(url));

        stopAction.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("enabled")) {
                    boolean enabled = (Boolean) evt.getNewValue();
                    stopButton.setEnabled(enabled);
                    blinkLed(enabled);

                    if (!enabled) {
                        _stopAction();
                    }
                }
            }
        });

        stopButton.addActionListener(stopAction);
        stopAction.setEnabled(false);
        return stopButton;
    }

    /**
     * If enabled is true, start blinking the led, otherwise stop blinking.
     */
    protected void blinkLed(boolean enabled) {
        led.setEnabled(ledState = enabled);

        if (enabled) {
            if (timer == null) {
                timer = new Timer(1000, new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        ledState = !ledState;
                        led.setEnabled(ledState);
                    }
                });

                timer.start();

            } else {
                timer.restart();
            }

        } else {
            if (timer != null) {
                timer.stop();
            }
        }
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public AbstractAction getStopAction() {
        return stopAction;
    }

    public JButton getLED() {
        return led;
    }

    /**
     * Tell the widget that actions have been started and to indicate
     * this by turning on the animation and enabling the stop button.
     */
    public void actionsStarted() {
        stopAction.setEnabled(true);
    }

    /**
     * Tell the widget that actions have completed.
     */
    public void actionsFinished() {
        stopAction.setEnabled(false);
    }

    /**
     * Return true if the stop action is enabled.
     */
    public boolean isBusy() {
        return stopAction.isEnabled();
    }

    /**
     * Enable stop action.
     */
    public void setBusy() {
        stopAction.setEnabled(true);
    }

    /**
     * Disable stop action.
     */
    public void setIdle() {
        stopAction.setEnabled(false);
    }

    /**
     * The StopActionButtonWidget calls this method to inform that the
     * button has been pressed.
     */
    private synchronized void _stopAction() {
        Enumeration<StopActionWatcher> e = _watchers.elements();

        while (e.hasMoreElements()) {
            StopActionWatcher watcher = e.nextElement();
            watcher.stopAction(this);
        }
    }

    /**
     * Add a watcher.
     *
     * Watchers are notified when the stop button is pressed.
     */
    public synchronized final void addWatcher(StopActionWatcher watcher) {
        if (!_watchers.contains(watcher)) {
            _watchers.addElement(watcher);
        }
    }

    /**
     * Delete a watcher.
     */
    public synchronized final void deleteWatcher(StopActionWatcher watcher) {
        _watchers.removeElement(watcher);
    }

    /**
     * Delete all watchers.
     */
    public synchronized final void deleteWatchers() {
        _watchers.removeAllElements();
    }

    /**
     * test main: usage: java LabelEntry
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");

        final StopActionWidget panel = new StopActionWidget();
        final JButton button = new JButton("Test");

        panel.addWatcher(new StopActionWatcher() {
            public void stopAction(StopActionWidget saw) {
                button.setEnabled(true);
            }
        });

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                panel.actionsStarted();
                button.setEnabled(false);
            }
        });

        frame.add("Center", panel);
        frame.add("South", button);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new BasicWindowMonitor());
    }
}
