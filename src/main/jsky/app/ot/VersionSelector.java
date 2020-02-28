/*
 * Copyright (C) 2009-2010 Science and Technology Facilities Council.
 * All Rights Reserved.
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

package jsky.app.ot;

import gemini.util.ObservingToolUtilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.TreeMap;
import java.util.Collection;

import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

@SuppressWarnings("serial")
public class VersionSelector extends JPanel implements ActionListener {
    private TreeMap<String, TelescopeConfig> configs =
            new TreeMap<String, TelescopeConfig>();
    public static final String TELESCOPE_KEY = "telescope";
    public static final String RES_KEY = "ot.resource.cfgdir";
    public static final String CFG_KEY = "ot.cfgdir";
    public static final String JSKY_KEY = "jsky.catalog.skycat.config";
    private JFrame frame = null;
    Runnable doWhenReady = null;
    Runnable doOnFailure = null;

    private VersionSelector(Runnable doWhenReady, Runnable doOnFailure) {
        this.doWhenReady = doWhenReady;
        this.doOnFailure = doOnFailure;

        readConfig();
        makeMenu();
    }

    /**
     * Check that the OT "version" configuration is complete.
     *
     * If the version information (for a specific telescope) is not complete
     * (as determined by checkTelescopeConfig) then a GUI choice panel will
     * be displayed.
     *
     * On completion, the given Runnable will be called on the Swing event
     * dispatching thread.
     */
    public static void checkVersions(
            final Runnable doWhenReady, final Runnable doOnFailure) {
        if (checkTelescopeConfig()) {
            // Launch routine to build the version selector
            // on the Swing thread.
            SwingUtilities.invokeLater(new Runnable () {
                public void run() {
                    new VersionSelector(doWhenReady, doOnFailure);
                }
            });
        }
        else {
            // We already have a complete configuration -- launch the
            // call-back in the Swing thread.
            SwingUtilities.invokeLater(doWhenReady);
        }
    }

    private static boolean checkTelescopeConfig() {
        boolean requiresMenu = false;
        String telescope = System.getProperty(TELESCOPE_KEY);
        String resDir = System.getProperty(RES_KEY);
        String cfgDir = System.getProperty(CFG_KEY);
        String jsky = System.getProperty(JSKY_KEY);

        if ((telescope == null || "".equals(telescope))
                && (resDir == null || "".equals(resDir))
                && (cfgDir == null || "".equals(cfgDir))
                && (jsky == null || "".equals(jsky))) {
            requiresMenu = true;
        }

        return requiresMenu;
    }

    private void readConfig() {
        URL url = ObservingToolUtilities.resourceURL("ot/cfg.cfg");
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            TelescopeConfig tc = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                String[] split = line.split("=");
                String key = split[0].trim().toLowerCase();

                if ("name".equals(key) && tc == null) {
                    tc = new TelescopeConfig();
                    tc.name = split[1].trim();
                    configs.put(tc.name, tc);

                } else if (TELESCOPE_KEY.equals(key) && tc != null) {
                    tc.telescope = split[1].trim();

                } else if (CFG_KEY.equals(key) && tc != null) {
                    tc.cfgDir = split[1].trim();

                } else if (RES_KEY.equals(key) && tc != null) {
                    tc.resDir = split[1].trim();

                } else if (JSKY_KEY.equals(key) && tc != null) {
                    tc.jsky = split[1].trim();

                } else {
                    tc = null;
                }
            }
        } catch (IOException ex) {
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

    private void makeMenu() {
        frame = new JFrame("Select a configuration");
        ButtonGroup group = new ButtonGroup();
        Collection<TelescopeConfig> values = configs.values();

        if (configs.size() == 1) {
            applyConfig(configs.firstEntry().getValue().name);
            doWhenReady.run();
        }
        else if (values.size() > 0) {
            setLayout(new GridLayout(values.size(), 1));

            for (TelescopeConfig config : values) {
                JRadioButton rb = new JRadioButton(config.name);
                rb.setActionCommand(config.name);
                rb.addActionListener(this);
                group.add(rb);
                add(rb);
            }

            frame.setContentPane(this);
            Dimension dim = getPreferredSize();
            dim.width = dim.width * 2;
            dim.height = dim.height * 2;
            frame.setPreferredSize(dim);
            frame.pack();
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setLocation(screen.width / 2 - dim.width / 2,
                    screen.height / 2 - dim.height / 2);

            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    doOnFailure.run();
                }
            });

            frame.setVisible(true);
        }
        else {
            JOptionPane.showMessageDialog(
                null, "No telescope configurations are available.",
                "No configurations", JOptionPane.ERROR_MESSAGE);
            doOnFailure.run();
        }
    }

    public void actionPerformed(ActionEvent e) {
        String name = e.getActionCommand();
        applyConfig(name);
        frame.setVisible(false);
        frame.dispose();

        // Run the call-back method.
        doWhenReady.run();
    }

    private void applyConfig(String name) {
        TelescopeConfig config = configs.get(name);
        System.setProperty(TELESCOPE_KEY, config.telescope);
        System.setProperty(CFG_KEY, config.cfgDir);
        System.setProperty(RES_KEY, config.resDir);
        System.setProperty(JSKY_KEY, config.jsky);
    }

    public static void main(String[] args) {
        VersionSelector.checkVersions(new Runnable () {
            public void run() {
                System.out.println("Configuration complete.");
            }
        },
        new Runnable() {
            public void run() {
                System.out.println("Configuration failed.");
            }
        });
    }

    class TelescopeConfig {
        String name;
        String telescope;
        String cfgDir;
        String resDir;
        String jsky;
    }
}
