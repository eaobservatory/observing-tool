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

package jsky.app.ot;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.WindowConstants;
import gemini.sp.SpFactory;
import gemini.sp.SpLibrary;
import gemini.sp.SpType;
import ot.News;
import ot.OtPreferencesDialog;
import ot.DatabaseDialog;
import orac.helptool.JHLauncher;
import gemini.util.ObservingToolUtilities;

@SuppressWarnings("serial")
public class OT {
    /** Help launcher. */
    private static JHLauncher helpLauncher = null;

    /** Splash screen displayed on startup */
    private static SplashScreen _splash;

    /** Preferences Dialog */
    private static OtPreferencesDialog _preferencesDialog = null;

    /** Database Access */
    private static DatabaseDialog _databaseDialog = null;

    /**
     * Default save directory.
     *
     * This system property key can be used to specify a default directory
     * to which save by default or which is used as default directory for file
     * save/open dialogs.
     *
     * The actual key/name to be used to set the system property is
     * <b>ot.userdir</b>.
     *
     * ot.userdir can be used to specify the users working directory from
     * which the a script is called. If the script changes the directory
     * before starting java the original working directory would not be
     * accessible from within java. the system property user.dir would point
     * to the directory from which java was started.
     */
    private static final String PROPERTY_OT_USERDIR = "ot.userdir";

    /**
     * @see #PROPERTY_OT_USERDIR
     * @see #getOtUserDir()
     */
    private static String _otUserDir = null;

    /**
     * Default constructor.
     *
     * This constructor is private to prevent instantiation of objects
     * of this class.
     */
    private OT() {
    }

    /**
     * Return the help launcher.
     */
    public static JHLauncher getHelpLauncher() {
        return helpLauncher;
    }

    /**
     * Set help launcher.
     */
    public static void setHelpLauncher(JHLauncher jHLauncher) {
        helpLauncher = jHLauncher;
    }

    public static DatabaseDialog getDatabaseDialog() {
        if (_databaseDialog == null) {
            _databaseDialog = new DatabaseDialog();
        }

        return _databaseDialog;
    }

    /**
     * Open a new program.
     */
    public static void newProgram() {
        OtProps.setSaveShouldPrompt(true);
        new OtWindowFrame(new OtProgWindow());
    }

    /**
     * Make a new library
     */
    public static void newLibrary() {
        OtProps.setSaveShouldPrompt(true);
        // Changed by MFO, 15 February 2002
        OtWindow.create((SpLibrary) SpFactory.create(SpType.LIBRARY),
                new FileInfo());
    }

    /**
     * Open a new science program.
     */
    public static void open() {
        OtProps.setSaveShouldPrompt(false);
        OtFileIO.open();
    }

    /**
     * Display a preferences dialog.
     */
    public static void preferences() {
        if (_preferencesDialog == null) {
            _preferencesDialog = new OtPreferencesDialog();
        }

        _preferencesDialog.show();
    }

    /**
     * Exit the application with the given status.
     */
    public static void exit() {
        // If the user wants to be prompted before closing
        // when there are edited programs,
        // look for edited programs and prompt
        for (int i = 0; i < OtWindowFrame.getWindowFrames().size(); i++) {
            if (!((OtWindowFrame) OtWindowFrame.getWindowFrames().get(i))
                    .getEditor().closeApp()) {
                return;
            }
        }

        System.exit(0); // Must be running as a local application.
    }

    /**
     * Fetch a science program from the database.
     */
    public static void fetchProgram() {
        getDatabaseDialog().show(DatabaseDialog.ACCESS_MODE_FETCH);
    }

    /**
     * Show the splash screen.
     */
    public static void showSplashScreen() {
        // changed my M.Folger@roe.ac.uk
        /*
         * As _splash is not actually set to null when _splash is dismissed
         * (hideSplashScreen is NOT called) the if condition would prevent
         * _splash to be shown a second time.
         */
        URL url = ObservingToolUtilities.resourceURL("welcome.txt",
                "ot.resource.cfgdir");

        if (url == null) {
            System.out.println("Warning: missing resource file:"
                    + " jsky/app/ot/cfg/welcome.txt");
            return;
        }

        _splash = new SplashFrame(url).getSplash();
    }

    /**
     * Hide the splash screen.
     */
    public static void hideSplashScreen() {
        if (_splash != null) {
            _splash.dismiss();
            _splash = null;
        }
    }

    // From ATC OT.java start

    public static void launchHelp() {
        if (OT.helpLauncher != null) {
            OT.helpLauncher.launch();
        } else {
            URL url = ObservingToolUtilities.resourceURL("help/othelp.hs",
                    "ot.cfgdir");
            OT.setHelpLauncher(new JHLauncher(url));
        }
    }

    /**
     * Show the news (release notes).
     */
    public static void showNews() {
        URL url = ObservingToolUtilities.resourceURL("news.txt", "ot.cfgdir");

        if (url != null) {
            News.showNews(url);
        }
    }

    // From ATC OT.java end

    /**
     * Get default user directory.
     *
     * Returns the directory specified by the system property
     * PROPERTY_OT_USERDIR ("ot.userdir") if it is specified and exists or
     * the user's home directory otherwise.
     *
     * @see #PROPERTY_OT_USERDIR
     */
    public static String getOtUserDir() {
        if (_otUserDir != null) {
            return _otUserDir;
        }

        _otUserDir = System.getProperty(PROPERTY_OT_USERDIR);

        if (_otUserDir != null) {
            File dir = new File(_otUserDir);

            if (!dir.exists() || !dir.isDirectory() || !dir.canWrite()) {
                _otUserDir = System.getProperty("user.home");
            }

        } else {
            _otUserDir = System.getProperty("user.home");
        }

        return _otUserDir;
    }

    /**
     * Usage: java [-Djsky.catalog.skycat.config=$SKYCAT_CONFIG] OT [programFile]
     * <p>
     * The <em>jsky.catalog.skycat.config</em> property defines the
     * Skycat-style catalog config file to use. (The default uses the user's
     * ~/.skycat/skycat.cfg file, or the ESO Skycat config file, if found).
     * <p>
     * If a program filename is specified, it is loaded on startup.
     */
    public static void main(String args[]) {
        boolean ok = true;
        Vector<String> filenames = null;

        try {
            // Check which version of java we are running
            String jVersion = System.getProperty("java.version");

            if (Double.parseDouble(jVersion.substring(0, 3)) < 1.6) {
                String message =
                        "The Observing Tool requires at least Java 1.6 to"
                        + " work.\n"
                        + "You seem to currently be running version "
                        + jVersion + "\n" + "Please Upgrade";
                JOptionPane.showMessageDialog(null, message,
                        "OT does not support current version of Java",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (NumberFormatException nfe) {
            System.err.println(
                    "Unable to confirm which version of java you are running.");
            System.err.println("Continuing anyway");
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-') {
                String opt = args[i];

                if (opt.equals("-internalframes")) {
                    System.err.println(
                            "Warning: option -internalframes has been removed");

                } else if (opt.equals("-nointernalframes")) {
                    System.err.println(
                            "Warning: option -nointernalframes has been removed");

                } else {
                    System.err.println("Unknown option: " + opt);
                    ok = false;
                    break;
                }

            } else {
                String filename = args[i];
                if (filename.toLowerCase().endsWith(".xml")) {
                    if (filenames == null) {
                        filenames = new Vector<String>();
                    }

                    filenames.add(filename);
                }
            }
        }

        if (!ok) {
            System.err.println("Usage: java"
                    + " [-Djsky.catalog.skycat.config=$SKYCAT_CONFIG] OT");

            System.exit(1);
        }

        VersionSelector.checkVersions();

        try {
            OtCfg.init();

        } catch (Throwable e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Problem with OT configuration:"
                    + "\nThis might result in invalid Science Programs.",
                    "Problem with OT configuration.",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Create a small frame to contain the menus that would originally
        // have been in the desktop pane with the internal frames.
        // (MFO, 17 August 2001)

        JFrame menuFrame = new JFrame("OT");
        menuFrame.setJMenuBar(new OTMenuBar(menuFrame));

        URL url = ObservingToolUtilities.resourceURL(
                "images/background_small.gif", "ot.resource.cfgdir");

        ImageIcon icon = new ImageIcon(url);
        JLabel label = new JLabel(icon);
        label.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
        menuFrame.add(label);

        menuFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        menuFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        menuFrame.pack();
        menuFrame.setVisible(true);

        if (filenames != null) {
            while (filenames.size() != 0) {
                OtFileIO.open(filenames.remove(0));
            }

        } else {
            OT.showSplashScreen();
        }
    }
}
