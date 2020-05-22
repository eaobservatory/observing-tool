/*
 * Copyright: Copyright (c) 2000 Association of Universities for Research in Astronomy, Inc. (AURA)
 * @author Allan Brighton
 * @version 1.0
 *
 * License:
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

package jsky.app.ot;

import gemini.util.ObservingToolUtilities;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import jsky.app.ot.gui.RichTextBoxWidgetExt;

import java.net.URL;

@SuppressWarnings("serial")
public class SplashGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel splashImage = new JLabel();
    JLabel redistLabel = new JLabel();
    TitledBorder titledBorder1;
    JScrollPane jScrollPane1 = new JScrollPane();
    RichTextBoxWidgetExt messageRTBW = new RichTextBoxWidgetExt();
    JPanel jPanel1 = new JPanel();
    JButton newButton = new JButton();
    JButton openButton = new JButton();
    JButton dismissButton = new JButton();
    JButton fetchButton = new JButton();

    public SplashGUI() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,
                Color.white, new Color(142, 142, 142)),
                "Observing Tool Release Notes");

        URL url = ObservingToolUtilities.resourceURL("images/splash.gif",
                "ot.resource.cfgdir");

        if (url != null) {
            splashImage.setIcon(new ImageIcon(url));
        } else {
            splashImage.setIcon(new ImageIcon(SplashGUI.class.getClassLoader()
                    .getResource("cfg/splash.gif")));
        }

        this.setMinimumSize(new Dimension(733, 311));
        this.setPreferredSize(new Dimension(733, 311));
        this.setLayout(gridBagLayout1);
        redistLabel.setForeground(new Color(0, 37, 133));
        redistLabel.setText("Please do not redistribute this software.");
        messageRTBW.setBorder(null);
        messageRTBW.setBackground(new Color(204, 204, 204));
        jScrollPane1.setBorder(titledBorder1);
        newButton.setText("Create New Program");
        openButton.setText("Open Existing Program");
        dismissButton.setText("Dismiss");
        fetchButton.setText("Fetch Program");

        this.add(splashImage, new GridBagConstraints(
                0, 0, 1, 2, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                new Insets(16, 5, 0, 5), 0, 0));
        this.add(redistLabel, new GridBagConstraints(
                1, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(jScrollPane1, new GridBagConstraints(
                1, 0, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 5, 5, 7), 0, 0));

        this.add(jPanel1, new GridBagConstraints(
                0, 2, 3, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        jPanel1.add(fetchButton, null);
        jPanel1.add(newButton, null);
        jPanel1.add(openButton, null);
        jPanel1.add(dismissButton, null);
        jScrollPane1.getViewport().add(messageRTBW, null);
    }
}
