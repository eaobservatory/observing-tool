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

package jsky.app.ot;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import jsky.app.ot.gui.RichTextBoxWidgetExt;
import gemini.util.Version;
import omp.SpClient;

import jsky.util.Preferences;

@SuppressWarnings("serial")
public final class SplashScreen extends SplashGUI implements ActionListener {
    /** The top level parent frame used to close the window */
    protected JFrame parent;
    private String OT_VERSION = "ot_version";
    private static String fullVersion = Version.getInstance().getFullVersion();
    private static Color colorOK = new Color(0, 128, 0);
    private static Color colorWarning = new Color(128, 128, 0);

    public SplashScreen(URL welcomeTxtURL) {
        _readWelcome(welcomeTxtURL);
        dismissButton.addActionListener(this);
        openButton.addActionListener(this);
        newButton.addActionListener(this);
        fetchButton.addActionListener(this);

        String pref = Preferences.get(OT_VERSION);

        if (pref == null || !fullVersion.equals(pref)) {
            Preferences.set(OT_VERSION, fullVersion);
            OT.showNews();
        }

        versionInfo.setForeground(Color.BLACK);
        versionInfo.setText("Checking for updated version information ...");

        (new SwingWorker<SpClient.VersionInfo, Void>() {
            @Override
            public SpClient.VersionInfo doInBackground() throws Exception {
                return SpClient.getOTVersionInfo();
            }

            @Override
            public void done() {
                try {
                    SpClient.VersionInfo info = get();

                    if (fullVersion.compareTo(info.minimum) < 0) {
                        versionInfo.setForeground(Color.RED);
                        versionInfo.setText("<html>This OT is too old for submitting programs for this semester.<br/>Please update to the latest version.</html>");
                    } else if (fullVersion.compareTo(info.current) < 0) {
                        versionInfo.setForeground(colorWarning);
                        versionInfo.setText("An updated version (" + info.current + ") of the OT is available.");
                    } else {
                        versionInfo.setForeground(colorOK);
                        versionInfo.setText("This OT is the latest version.");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();

                    versionInfo.setForeground(Color.RED);
                    versionInfo.setText("Unable to check for updates.");

                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    String message = null;

                    if (cause == null) {
                        e.printStackTrace();
                    } else {
                        cause.printStackTrace();
                    }

                    versionInfo.setForeground(Color.RED);
                    versionInfo.setText("Unable to check for updates.");
                }
            }
        }).execute();
    }

    /**
     * Read the welcome text from the specified URL.
     */
    private void _readWelcome(URL url) {
        final String versionString = "OT release version ";
        RichTextBoxWidgetExt rt;
        rt = messageRTBW;

        // Prevent automatic scrolling as we insert text.
        Caret caret = rt.getCaret();
        if (caret instanceof DefaultCaret) {
            ((DefaultCaret) caret).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        }

        rt.setEditable(false);

        // Get the updated version date...
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.equals("")) {
                    rt.append("\n\n");
                } else if (line.startsWith(versionString)) {
                    rt.append(versionString + fullVersion + " ");
                } else {
                    rt.append(line + " ");
                }
            }
        } catch (IOException ex) {
            _warning("Couldn't read the welcome text!");
            System.out.println(ex);

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Display a warning message in the RichTextBoxExt.
     */
    private void _warning(String warning) {
        RichTextBoxWidgetExt rt;
        rt = messageRTBW;
        rt.setText("WARNING: " + warning);
        rt.setForeground(Color.red);
        rt.setFont(rt.getFont().deriveFont(Font.BOLD));
    }

    /** Set the top level parent frame used to close the window */
    public void setParentFrame(JFrame p) {
        parent = p;
    }

    /** Return the top level parent frame used to close the window */
    public JFrame getParentFrame() {
        return parent;
    }

    public void dismiss() {
        if (parent != null) {
            parent.dispose();
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object w = e.getSource();

        if (w == openButton) {
            OtFileIO.open();
        } else if (w == newButton) {
            OT.newProgram();
        } else if (w == fetchButton) {
            OT.fetchProgram();
        }

        // In any case, remove the splash screen
        dismiss();
    }
}
