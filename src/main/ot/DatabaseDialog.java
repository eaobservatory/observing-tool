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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.util.concurrent.ExecutionException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import jsky.app.ot.LoginInfo;
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
        _w.passwordTextBox.addActionListener(this);
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
            _w.loginTextBox.setEditable(false);
            _w.validationReminder.setVisible(true);

            if ((_spItemToBeSaved != null)
                    && (_spItemToBeSaved instanceof SpProg)) {
                _w.loginTextBox.setText(
                        ((SpProg) _spItemToBeSaved).getProjectID());
            } else {
                _w.loginTextBox.setText("");
            }

        } else {
            _title = "Fetch Program";
            _w.loginTextBox.setEditable(true);
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

        _dialogComponent.setTitle(_title);
        _dialogComponent.setVisible(true);
        _dialogComponent.setState(JFrame.NORMAL);
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

    private void doFetchProgram(final String projectID, final String password) {
        (new SwingWorker<SpItem, Void>() {
            @Override
            public SpItem doInBackground() throws Exception {
                return SpClient.fetchProgram(projectID, password);
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

                        // Database argument is not needed, 0 is just a dummy.
                        LoginInfo li = new LoginInfo(projectID, 0, password);

                        new OtWindowFrame(
                            new OtProgWindow((SpRootItem) spItem, li));
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

    private void doStoreProgram(final String password) {
        (new SwingWorker<SpClient.SpStoreResult, Void>() {
            @Override
            public SpClient.SpStoreResult doInBackground() throws Exception {
                try {
                    return SpClient.storeProgram(
                            (SpProg) _spItemToBeSaved, password, false);
                }
                catch (final SpChangedOnDiskException e) {
                    final int[] confirm = new int[1];

                    // Use invokeAndWait to launch a confirm dialog box to ask
                    // whether they want to "force" storage of the program.
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            confirm[0] = JOptionPane.showConfirmDialog(
                                    _w, e.getMessage() + "\n\nStore anyway?",
                                    "Database Message",
                                    JOptionPane.YES_NO_OPTION);
                        }
                    });

                    if (confirm[0] == JOptionPane.YES_OPTION) {
                        // Call storeProgram and force storing despite
                        // inconsistent time stamp.
                        return SpClient.storeProgram(
                            (SpProg) _spItemToBeSaved, password, true);
                    }
                }

                return null;
            }

            @Override
            public void done() {
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
                            "Could not store Science Program.\n" + message,
                            "Database Error", JOptionPane.ERROR_MESSAGE);

                } finally {
                    databaseAccessFinished();
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

        if (w == _w.confirmButton) {
            accessDatabase();
        } else if (w == _w.closeButton) {
            hide();
        } else if (w == _stopAction.getStopButton()) {
            _databaseAccessAborted = true;
        } else if (w == _w.passwordTextBox) {
            _w.confirmButton.doClick();
        }
    }

    private void accessDatabase() {
        _stopAction.actionsStarted();
        _w.confirmButton.setEnabled(false);

        String password = new String(_w.passwordTextBox.getPassword());

        if (_mode == ACCESS_MODE_STORE) {
            doStoreProgram(password);
        }
        else {
            // loginTextBox contains the proejctID aka Science Program name.
            String projectID = _w.loginTextBox.getText();

            doFetchProgram(projectID, password);
        }
    }

    private void databaseAccessFinished() {
        _databaseAccessAborted = false;
        _stopAction.actionsFinished();
        _w.confirmButton.setEnabled(true);
    }
}
