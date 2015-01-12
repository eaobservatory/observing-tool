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

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import jsky.util.gui.BasicWindowMonitor;

/**
 * This widget displays a group of radio buttons in a tabular layout.
 *
 * @author Allan Brighton
 */
@SuppressWarnings("serial")
public class ToggleButtonWidgetPanel extends JPanel {
    /** Array of buttons to display */
    private ToggleButtonWidget[] buttons;

    /**
     * Create a panel containing toggle buttons, arranged in the
     * given number of rows and columns.
     *
     * @param names an array of ToggleButton labels
     * @param nrows the number of rows
     * @param ncols the number of columns
     * @param enableMultipleSelection if true, multiple buttons may be
     *        selected, otherwise only one
     * @param hgap the horizontal gap
     * @param vgap the vertical gap
     */
    public ToggleButtonWidgetPanel(String[] names, int nrows, int ncols,
            boolean enableMultipleSelection, int hgap, int vgap) {
        setLayout(new GridLayout(nrows, ncols, hgap, vgap));
        buttons = new ToggleButtonWidget[names.length];
        ButtonGroup group = new ButtonGroup();

        for (int i = 0; i < names.length; i++) {
            if (names[i] == null) {
                buttons[i] = null;
                add(new JLabel());

            } else {
                buttons[i] = new ToggleButtonWidget(names[i],
                        enableMultipleSelection);

                if (!enableMultipleSelection) {
                    group.add(buttons[i]);
                }

                add(buttons[i]);
            }
        }
    }

    /**
     * Create a panel containing toggle buttons, arranged in the
     * given number of rows and columns.
     *
     * @param names an array of ToggleButton labels
     * @param nrows the number of rows
     * @param ncols the number of columns
     * @param enableMultipleSelection if true, multiple buttons may be
     *        selected, otherwise only one
     */
    public ToggleButtonWidgetPanel(String[] names, int nrows, int ncols,
            boolean enableMultipleSelection) {
        this(names, nrows, ncols, enableMultipleSelection, 0, 0);
    }

    /**
     * Return the number of buttons.
     */
    public int getNumberOfButtons() {
        return buttons.length;
    }

    /**
     * Return the nth button.
     */
    public ToggleButtonWidget getButton(int n) {
        return buttons[n];
    }

    /**
     * Return the button with the given label.
     */
    public ToggleButtonWidget getButton(String label) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == null) {
                continue;
            }

            String s = buttons[i].getText();

            if (s != null && s.equals(label)) {
                return buttons[i];
            }
        }

        return null;
    }

    /**
     * test main
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("ToggleButtonWidgetPanel");

        String[] names = {
                "one", "two", "three", "four", "five", null,
                "six", "seven", "eight", "nine", "ten"};

        ToggleButtonWidgetPanel panel = new ToggleButtonWidgetPanel(
                names, 0, 1, true, 0, 0);

        for (int i = 0; i < names.length; i++) {
            ToggleButtonWidget b = panel.getButton(i);

            if (b == null) {
                continue;
            }

            b.addWatcher(new ToggleButtonWidgetWatcher() {
                public void toggleButtonAction(ToggleButtonWidget tbw) {
                    if (tbw.getBooleanValue()) {
                        System.out.println("Selected " + tbw.getText());
                    } else {
                        System.out.println("Deselected " + tbw.getText());
                    }
                }
            });
        }

        ToggleButtonWidget tbw = panel.getButton("seven");
        tbw.addWatcher(new ToggleButtonWidgetWatcher() {
            public void toggleButtonAction(ToggleButtonWidget tbw) {
                System.out.println("Selected #7: " + tbw.getText()
                        + ", " + tbw.isSelected());
            }
        });

        frame.add("Center", panel);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new BasicWindowMonitor());
    }
}
