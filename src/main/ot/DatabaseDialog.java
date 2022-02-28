/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.Map;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jsky.app.ot.OT;
import jsky.app.ot.OtCfg;
import jsky.app.ot.OtProgWindow;
import jsky.app.ot.OtWindowFrame;
import jsky.app.ot.gui.StopActionWidget;
import jsky.util.gui.DialogUtil;
import omp.SpClient;
import omp.SpChangedOnDiskException;
import gemini.sp.SpItem;
import gemini.sp.SpRootItem;
import gemini.sp.SpProg;

/**
 * Dialog for database access.
 *
 * OMP, fetchProgram, storeProgram.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class DatabaseDialog implements ActionListener {
    /**
     * This is a subclass of JPanel so it can be used for internal as well as
     * other frames.
     */
    private DatabaseDialogGUI _w;

    /**
     * Is only used if the OT is started without internal frames.
     */
    private static JFrame _dialogComponent;
    public static int ACCESS_MODE_FETCH = 0;
    public static int ACCESS_MODE_STORE = 1;
    private static int _mode = ACCESS_MODE_FETCH;
    private static SpItem _spItemToBeSaved = null;
    private StopActionWidget _stopAction = new StopActionWidget();
    private boolean _databaseAccessAborted = false;

    /**
     *
     */
    private String _title;

    public DatabaseDialog() {
        _title = "Fetch Program";
        _w = new DatabaseDialogGUI();
        _w.add(_stopAction, BorderLayout.NORTH);

        _w.confirmButton.addActionListener(this);
        _w.closeButton.addActionListener(this);
        _w.hedwigButton.addActionListener(this);
        _w.passwordTextBox.addActionListener(this);
        _w.tokenConfirmButton.addActionListener(this);
        _stopAction.getStopButton().addActionListener(this);
    }

    public void fetchProgram() {
        show(DatabaseDialog.ACCESS_MODE_FETCH);
    }

    public void storeProgram(SpItem spItem) {
        SpProg prog = (SpProg) spItem;
        String projectID = prog.getProjectID();
        prog.setOTVersion();
        prog.setTelescope();

        if ((projectID == null) || projectID.trim().equals("")) {
            DialogUtil.error(_w,
                    "Please specify a Project ID (Science Program component).");

        } else {
            _spItemToBeSaved = spItem;
            show(DatabaseDialog.ACCESS_MODE_STORE);
        }
    }

    /**
     * @param accessMode fetchProgram or storeProgram.
     */
    public void show(int accessMode) {
        _mode = accessMode;

        if (accessMode == ACCESS_MODE_STORE) {
            _title = "Store Program";
            _w.projectTextBox.setEditable(false);
            _w.validationReminder.setVisible(true);

            if ((_spItemToBeSaved != null)
                    && (_spItemToBeSaved instanceof SpProg)) {
                _w.projectTextBox.setText(
                        ((SpProg) _spItemToBeSaved).getProjectID());
            } else {
                _w.projectTextBox.setText("");
            }

        } else {
            _title = "Fetch Program";
            _w.projectTextBox.setEditable(true);
            _w.validationReminder.setVisible(false);
        }

        if (_dialogComponent == null) {
            _dialogComponent = new JFrame();

            _dialogComponent.add(_w);
            _dialogComponent.pack();

            // center the window on the screen
            Dimension dim = _dialogComponent.getSize();
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            _dialogComponent.setLocation(screen.width / 2 - dim.width / 2,
                    screen.height / 2 - dim.height / 2);
        }

        if (OT.loginInfo == null) {
            _w.typeChoice.setEnabledAt(2, false);
            if (_w.typeChoice.getSelectedIndex() == 2) {
                _w.typeChoice.setSelectedIndex(0);
            }
        }
        else {
            _w.typeChoice.setEnabledAt(2, true);
            _w.typeChoice.setSelectedIndex(2);
            _w.tokenUsernameTextBox.setText(OT.loginInfo.username);
        }

        _dialogComponent.setTitle(_title);
        _dialogComponent.setVisible(true);
        _dialogComponent.setState(JFrame.NORMAL);

        _w.confirmButton.setEnabled(true);
        _w.hedwigButton.setEnabled(true);
        _w.tokenConfirmButton.setEnabled(true);
    }

    public void hide() {
        _dialogComponent.setVisible(false);
    }

    /**
     * Set database access mode.
     *
     * Database access modes: {@link #ACCESS_MODE_FETCH}
     * or {@link #ACCESS_MODE_STORE}.
     */
    public void setMode(int mode) {
        _mode = mode;
    }

    private void doFetchProgram(
            final String projectID, final String provider,
            final String username, final String password) {
        (new SwingWorker<SpItem, Void>() {
            @Override
            public SpItem doInBackground() throws Exception {
                return SpClient.fetchProgram(projectID, provider, username, password);
            }

            @Override
            public void done() {
                try {
                    SpItem spItem = get();

                    if (spItem == null) {
                        JOptionPane.showMessageDialog(_dialogComponent,
                                "Could not fetch Science Program.\nNull or invalid response.",
                                "Database Error", JOptionPane.ERROR_MESSAGE);

                    } else if (! _databaseAccessAborted) {
                        // If the user has aborted fetchProgram by hitting
                        // "Stop" then do not display the science program.

                        new OtWindowFrame(
                            new OtProgWindow((SpRootItem) spItem));
                    }

                    hide();

                } catch (InterruptedException e) {

                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    String message = null;

                    if (cause == null) {
                        message = e.getMessage();
                    } else {
                        cause.printStackTrace();
                        message = cause.getMessage();
                    }

                    JOptionPane.showMessageDialog(_dialogComponent,
                            "Could not fetch Science Program.\n" + message,
                            "Database Error", JOptionPane.ERROR_MESSAGE);

                } finally {
                    databaseAccessFinished();
                }
            }
        }).execute();
    }

    private void doStoreProgram(
            final String provider, final String username,
            final String password, final boolean force) {
        (new SwingWorker<SpClient.SpStoreResult, Void>() {
            @Override
            public SpClient.SpStoreResult doInBackground() throws Exception {
                return SpClient.storeProgram(
                        (SpProg) _spItemToBeSaved, provider, username, password, force);
            }

            @Override
            public void done() {
                boolean retry_with_force = false;

                try {
                     SpClient.SpStoreResult result = get();

                    if (result != null) {
                        ((SpProg) _spItemToBeSaved).setTimestamp(result.timestamp);

                        String dialogString = result.summary
                                + "\nYour Program has been successfully submitted!"
                                + "\nPLEASE SAVE THE SCIENCE PROGRAM IN ORDER TO KEEP"
                                + " TIMESTAMP INFORMATION.";

                        new FormattedStringBox(dialogString, "Database Message");
                    }

                    hide();

                } catch (InterruptedException e) {
                    e.printStackTrace();

                } catch (ExecutionException ee) {
                    Throwable e = ee.getCause();

                    if (e instanceof SpChangedOnDiskException) {
                        if (force) {
                            JOptionPane.showMessageDialog(_dialogComponent,
                                    "Could not store Science Program despite force request.\n"
                                    + e.getMessage(),
                                    "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            // Launch a confirm dialog box to ask
                            // whether they want to "force" storage of the program.
                            int confirm = JOptionPane.showConfirmDialog(
                                    _w, e.getMessage() + "\n\nStore anyway?",
                                    "Database Message",
                                    JOptionPane.YES_NO_OPTION);

                            if (confirm == JOptionPane.YES_OPTION) {
                                retry_with_force = true;
                            }
                        }
                    }
                    else {
                        String message = null;

                        if (e == null) {
                            message = ee.getMessage();
                        } else {
                            e.printStackTrace();
                            message = e.getMessage();
                        }

                        JOptionPane.showMessageDialog(_dialogComponent,
                                "Could not store Science Program.\n" + message,
                                "Database Error", JOptionPane.ERROR_MESSAGE);
                    }

                } finally {
                    if (retry_with_force) {
                        if ("hedwig".equals(provider)) {
                            // When using OAuth, since the OMP responds to a timestamp
                            // issue with a SOAP error, we do not receive the OMP login
                            // token.  However the one-time access code will have been
                            // consumed, so we need to return to Hedwig to obtain a new one.
                            listenForOAuthHedwig(true);
                        }
                        else {
                            doStoreProgram(provider, username, password, true);
                        }
                    }
                    else {
                        databaseAccessFinished();
                    }
                }
            }
        }).execute();
    }

    /**
     * The standard actionPerformed method to handle the "confirm" and "cancel"
     * buttons.
     */
    public void actionPerformed(ActionEvent evt) {
        Object w = evt.getSource();

        if ((w == _w.confirmButton)
                || (w == _w.hedwigButton)
                || (w == _w.tokenConfirmButton)) {
            // Check that the project ID box is not empty before attempting to fetch.
            if (_mode == ACCESS_MODE_FETCH) {
                String projectID = _w.projectTextBox.getText();
                if ("".equals(projectID)) {
                    JOptionPane.showMessageDialog(_dialogComponent,
                            "Please enter the project ID.",
                            "Error", JOptionPane.ERROR_MESSAGE);

                    return;
                }
            }

            if (w == _w.hedwigButton) {
                listenForOAuthHedwig(false);
            } else if (w == _w.tokenConfirmButton) {
                accessDatabase("omptoken", OT.loginInfo.username, OT.loginInfo.token, false);
            } else {
                accessDatabase();
            }
        } else if (w == _stopAction.getStopButton()) {
            _databaseAccessAborted = true;
        } else if (w == _w.passwordTextBox) {
            _w.confirmButton.doClick();
        } else if (w == _w.closeButton) {
            hide();
        }
    }

    private void accessDatabase() {
        String provider = _w.providers[_w.providerBox.getIntegerValue()];
        String username = _w.usernameTextBox.getText();
        String password = new String(_w.passwordTextBox.getPassword());

        accessDatabase(provider, username, password, false);
    }

    private void accessDatabase(String provider, String username, String password, boolean force) {
        _stopAction.actionsStarted();
        _w.confirmButton.setEnabled(false);
        _w.hedwigButton.setEnabled(false);
        _w.tokenConfirmButton.setEnabled(false);

        if (_mode == ACCESS_MODE_STORE) {
            doStoreProgram(provider, username, password, force);
        }
        else {
            // projectTextBox contains the projectID aka Science Program name.
            String projectID = _w.projectTextBox.getText();

            doFetchProgram(projectID, provider, username, password);
        }
    }

    private void databaseAccessFinished() {
        _databaseAccessAborted = false;
        _stopAction.actionsFinished();
        _w.confirmButton.setEnabled(true);
        _w.hedwigButton.setEnabled(true);
        _w.tokenConfirmButton.setEnabled(true);
    }

    private void listenForOAuthHedwig(boolean force) {
        listenForOAuth(OtCfg.getHedwigOAuthURL(), OtCfg.getHedwigOAuthClient(), force);
    }

    private void listenForOAuth(String auth_url, String client_id, final boolean force) {
        _w.confirmButton.setEnabled(false);
        _w.hedwigButton.setEnabled(false);
        _w.tokenConfirmButton.setEnabled(false);
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
            final String redirect_uri = "http://127.0.0.1:" + server.getAddress().getPort() + "/";
            server.createContext("/", new HttpHandler() {
                public void handle(HttpExchange t) throws IOException {
                    URI uri = t.getRequestURI();
                    String response = "You may now return to the OT.";
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();

                    final Map<String, String> query = new HashMap<String, String>();
                    for (String param: uri.getQuery().split("&")) {
                        int pos = param.indexOf("=");
                        if (pos != -1) {
                            query.put(param.substring(0, pos), param.substring(pos + 1));
                        }
                    }

                    if (query.containsKey("error")) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                JOptionPane.showMessageDialog(_dialogComponent,
                                        "Error from authentication server: " + query.get("error"),
                                        "Authentication Error", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                    }
                    else if (query.containsKey("code")) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                // Calling toFront alone doesn't seem to work.
                                // This sequence of steps is taken from:
                                // https://stackoverflow.com/questions/309023/how-to-bring-a-window-to-the-front
                                _dialogComponent.setAlwaysOnTop(true);
                                _dialogComponent.toFront();
                                _dialogComponent.requestFocus();
                                _dialogComponent.setAlwaysOnTop(false);

                                accessDatabase("hedwig", redirect_uri, query.get("code"), force);
                            }
                        });
                    }
                    else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                JOptionPane.showMessageDialog(_dialogComponent,
                                        "Authentication server did not return an access code.",
                                        "Authentication Error", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                    }

                    server.stop(1);
                }
            });
            server.setExecutor(null);
            server.start();

            String full_url = auth_url
                + "?response_type=code&redirect_uri="
                + URLEncoder.encode(redirect_uri, "UTF-8")
                + "&client_id=" + client_id + "&scope=openid";

            System.out.println("Log in URL: " + full_url);

            if ("linux".equals(System.getProperty("os.name").toLowerCase())) {
                // On some modern Linux distributions, the Desktop.browse
                // method appears to hang until after the OT has exited.
                // Therefore first try executing xdg-open to avoid this problem.
                try {
                    Runtime.getRuntime().exec(new String[] {"xdg-open", full_url});
                    return;
                }
                catch (IOException e) {
                }
            }

            Desktop.getDesktop().browse(new URI(full_url));
        }
        catch (Exception e) {
            e.printStackTrace();
            _w.confirmButton.setEnabled(true);
            _w.hedwigButton.setEnabled(true);
            _w.tokenConfirmButton.setEnabled(true);
        }
    }
}
