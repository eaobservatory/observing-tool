// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.ListBoxWidgetExt;
import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.StopActionWatcher;
import jsky.app.ot.gui.StopActionWidget;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import gemini.sp.SpItem;
import gemini.sp.SpRootItem;

import gemini.sp.ipc.SpServerWatcher;
import gemini.sp.ipc.SpAccess;
import gemini.sp.ipc.SpProgKey;
import gemini.sp.ipc.SpServerAsync;

import ot.util.DialogUtil;
import ot.gui.PasswordWidgetExt;

import java.awt.Component;
import java.awt.Frame;
import java.util.Enumeration;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;


/**
 * A window that presents an interface used to obtain program listings
 * and fetch programs from the ODB.  This is a singleton class so at most one
 * ProgListWindow will ever exist.
 */
public final class ProgListWindow extends RemoteGUI
    implements TextBoxWidgetWatcher, SpServerWatcher, StopActionWatcher, ActionListener {

    // The singleton ProgListWindowFrame or InternalFrame instance.
    private static Component _instance;

    public static final String LOGIN_PAGE     = "loginPage";
    public static final String PROG_LIST_PAGE = "progListPage";
    public static final String LOGIN_PATH     = "folderWidget.loginPage";
    public static final String PROG_LIST_PATH = "folderWidget.progListPage";

    private SpAccess  _spAccess;
    private SpProgKey _spProgKey;
    private String    _username;
    private Vector    _progs;

    private SpServerAsync _spServerAsync;

    private ListBoxWidgetExt _progList;

    /**
     * Get the singleton ProgListWindow JFrame or JInternalFrame.
     */
    public static synchronized Component instance() {
	if (_instance == null) {
	    if (OT.getDesktop() == null) {
		_instance = new ProgListWindowFrame();
	    }
	    else {
		_instance = new ProgListWindowInternalFrame();
		OT.getDesktop().add((JInternalFrame)_instance, JLayeredPane.MODAL_LAYER);
	    }
	}

	// make window popup in case it was iconified.
	((ProgListWindowFrame)_instance).setState(java.awt.Frame.NORMAL);

	return _instance;
    }

    //
    // Default constructor (not public).  ProgListWindows are created by
    // calling the static "instance()" method.
    //
    public ProgListWindow() {
	String widgetName;

	// Select the PhaseII database by default
	OptionWidgetExt ow = phaseIIOption;
	ow.setValue(true);

	// Remember a reference to the "progList" ListBoxWidgetExt
	_progList = progList;

	// handle the login, fetch and close buttons
	loginButton.addWatcher( new CommandButtonWidgetWatcher() {
		public void commandButtonAction(CommandButtonWidgetExt cbwe) {
		    if (stopAction.isBusy()) 
			return;
		    fetchListing();
		}
	    });
	fetchButton.addWatcher( new CommandButtonWidgetWatcher() {
		public void commandButtonAction(CommandButtonWidgetExt cbwe) {
		    if (stopAction.isBusy()) 
			return;
		    fetchProg();
		}
	    });
	closeButton.addWatcher( new CommandButtonWidgetWatcher() {
		public void commandButtonAction(CommandButtonWidgetExt cbwe) {
		    if (stopAction.isBusy()) 
			return;
		    _instance.setVisible(false);
		}
	    });

	ButtonGroup grp = new ButtonGroup();
	grp.add(phaseIIOption);
	grp.add(activeOption);
        activeOption.setEnabled(false);

	_init();
    }

    /**
     * Show a particular page in this widget.
     */
    public void	gotoPage(String pageName) {
	int index = 0;
	if (pageName.equals(PROG_LIST_PAGE))
	    index++;
	folderWidget.setSelectedIndex(index);
    }

    //
    // Return true if we need to fetch a new program list for the given
    // login information.
    //
    boolean _needToFetchList() {
	return _updateLoginInfo();
    }

    //
    // Initialize the textbox watchers.
    //
    private void _init() {
	//MFO
	passwordTextBox.addActionListener(this);

	keyTextBox.addActionListener(this);

	stopAction.addWatcher(this);
    }

    // Get the text in the username textbox.
    private String _getUsernameEntry() {
	TextBoxWidgetExt tb;
	tb = loginTextBox;
	return tb.getText().trim();
    }

    // Set the text in the username textbox.
    private void _setUsernameEntry(String username) {
	TextBoxWidgetExt tb;
	tb = loginTextBox;
	tb.setText(username);
    }

    // Get the text in the password textbox.
    private String _getPasswordEntry() {
	// MFO
	PasswordWidgetExt tb;
	tb = passwordTextBox;
	return new String(tb.getPassword()).trim();
    }

    // Set the text in the password textbox.
    private void _setPasswordEntry(String password) {
	// MFO
	PasswordWidgetExt tb;
	tb = passwordTextBox;
	tb.setText(password);
    }

    // Get the selected database option.
    private int _getDatabaseChoice() {
	int database = SpAccess.PHASEII;
	OptionWidgetExt ow;
	ow = phaseIIOption;
	boolean b = ow.getValue();
	if (! ow.getValue()) {
	    database = SpAccess.ACTIVE;
	}
	return database;
    }

    // Set the selected database option.
    private void _setDatabaseChoice(int database) {
	OptionWidgetExt phaseII, active;
	phaseII = phaseIIOption;
	active  = activeOption;

	if (database == SpAccess.PHASEII) {
	    phaseII.setValue(true);
	    active.setValue(false);
	} else {
	    phaseII.setValue(false);
	    active.setValue(true);
	}
    }

    //
    // Get the most up-to-date login information.  Returns true if never called
    // before or if anything changed since the last time called.
    //
    private boolean _updateLoginInfo() {
	boolean result = false;

	// Check the password and database choice
	if (_spAccess == null) {
	    _spAccess = new SpAccess(_getPasswordEntry(), _getDatabaseChoice());
	    result = true;
	} else {
	    String password = _getPasswordEntry();
	    int    dbChoice = _getDatabaseChoice();
	    if (!_spAccess.password.equals(password) || (_spAccess.db != dbChoice)) {
		_spAccess.password = password;
		_spAccess.db       = dbChoice;
		result = true;
	    }
	}

	// Check the username
	if (_username == null) {
	    _username = _getUsernameEntry();
	    result = true;
	} else {
	    String username = _getUsernameEntry();
	    if (!_username.equals(username)) {
		_username = username;
		result = true;
	    }
	}

	return result;
    }

    //
    // Freeze user interactions with the window.
    //
    private void _freeze() {
	// For now, just set busy
	stopAction.setBusy();
	stopAction.actionsStarted();
    }

    //
    // Allow user interactions with the window.
    //
    private void _thaw() {
	// For now, just set idle
	stopAction.setIdle();
	stopAction.actionsFinished();
    }

    /**
     * Implementation of the StopActionWatcher interface.
     *
     * @see StopActionWatcher
     */
    public void stopAction(StopActionWidget saw) {
	if (_spServerAsync != null) _spServerAsync.abort();
    }

    /**
     * Called by SpServerAsync when an operation is aborted.
     *
     * @see SpServerWatcher
     */
    public synchronized void spServerAbort(SpServerAsync ssa) {
	if (ssa != _spServerAsync) return;
	_spServerAsync = null;
	_thaw();
    }

    /**
     * Called by SpServerAsync when an operation fails.
     *
     * @see SpServerWatcher
     */
    public synchronized void spServerError(String problem, SpServerAsync ssa) {
	if (ssa != _spServerAsync) return;
	_spServerAsync = null;
	DialogUtil.error(this, "Problem communicating with ODB: " + problem);
	_thaw();
    }

    /**
     * Part of the SpServerWatcher interface that is not used.
     *
     * @see SpServerWatcher
     */
    public void spServerStoreComplete(SpItem spItem, SpProgKey key,
				      boolean isNew, SpServerAsync ssa) {}

    /**
     * Contact the ODB and obtain a listing of available programs.
     */
    public synchronized void fetchListing() {
	// Make sure there isn't already an operation in progress.
	if (_spServerAsync != null) {
	    return;
	}

	// Show the window if it isn't visible, and update the login info.
	if (!_instance.isVisible()) {
	    _instance.setVisible(true);
	}
	if (_instance instanceof JFrame)
	    ((JFrame)_instance).setState(Frame.NORMAL);

	_updateLoginInfo();
 
	_freeze();
	_spServerAsync = SpServerAsync.list(_spAccess, _username, this);
    }

    /**
     * Called by SpServerAsync when the program listing completes
     * successfully.
     *
     * @see SpServerWatcher
     */
    public synchronized void spServerListComplete(Vector progList, SpServerAsync ssa) {
	if (ssa != _spServerAsync) return;
	_spServerAsync = null;

	LastLogin.set(_username, _spAccess.db, _spAccess.password);
	_progs = progList;
	_updateProgListPage( _username, _spAccess.db, _progs );
	gotoPage(PROG_LIST_PAGE);

	_thaw();
    }

    /**
     * Fetch the selected program from the ODB.
     */
    public synchronized void fetchProg() {
	// Make sure there isn't already an operation in progress.
	if (_spServerAsync != null) return;

   // Make sure they've logged in.
	if ((_spAccess == null) || _spAccess.password.equals("")) {
	    gotoPage(LOGIN_PAGE);
	    DialogUtil.error(this, "You have to login first.");
	}

	// Get the selected name.
	String progName = _progList.getStringValue();
	if (progName == null) {
	    DialogUtil.error(this, "You must select a program to fetch.");
	    return;
	}

	// Now unformat it to get the spXXX id.  The programs are listed either
	// as "spXXX" alone or "Arbitrary Title (spXXX)"
	if (progName.charAt(progName.length() - 1) == ')') {
	    int index = progName.lastIndexOf('(');
	    progName = progName.substring(index + 1, progName.length() - 1);
	}

	// Get the key.
	// MFO
	PasswordWidgetExt tbw;
	tbw = keyTextBox;
	String key = new String(tbw.getPassword()).trim();
	if ((key == null) || (key.equals(""))) {
	    DialogUtil.error(this, "You must specify the program's key.");
	    return;
	}

	_freeze();

	// Start the fetch.
	_spProgKey     = new SpProgKey(progName, key);
	_spServerAsync = SpServerAsync.fetch(_spAccess, _spProgKey, this);
    }

    /**
     * Called by SpServerAsync when a program fetch completes
     * successfully.
     *
     * @see SpServerWatcher
     */
    public synchronized void spServerFetchComplete(SpItem spItem, SpServerAsync ssa) {
	if (ssa != _spServerAsync) return;
	_spServerAsync = null;

	LoginInfo li = new LoginInfo(_username, _spAccess.db, _spAccess.password);

	// Create the new window (up-ing the priority seems to speed it up a bit).
	Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
	
	// MFO (next 8 lines)
	if (OT.getDesktop() == null) {
	    new OtWindowFrame(new OtProgWindow((SpRootItem) spItem, li, _spProgKey));
	}
	else {
	    Component c = new OtWindowInternalFrame(new OtProgWindow((SpRootItem) spItem, li, _spProgKey));
	    OT.getDesktop().add(c, JLayeredPane.DEFAULT_LAYER);
	    OT.getDesktop().moveToFront(c);
	}

	Thread.currentThread().setPriority(Thread.NORM_PRIORITY - 1);

	_thaw();
    }
 
    private void _updateProgListPage( String user, int db, Vector progs ) {
	// Show who the program list belongs to
	String header;
	if (progs.size() == 0) {
	    header = "`" + user + "' has no programs ";
	} else {
	    header = "Program listing for `" + user + "' ";
	}

	if (db == SpAccess.PHASEII) {
	    header += "(Phase II)";
	} else {
	    header += "(Active)";
	}
	JLabel stw;
	stw = progListBanner;
	stw.setText(header);

	// Format the program list
	Vector formProgs = new Vector(progs.size());
	Enumeration e = progs.elements();
	while (e.hasMoreElements()) {
	    String prog = (String) e.nextElement();
	    int spaceIndex = prog.indexOf(' ');
	    if (spaceIndex == -1) {
		formProgs.addElement(prog);
	    } else {
		formProgs.addElement( prog.substring(spaceIndex + 1) +
				      " (" + prog.substring(0, spaceIndex) + ")");
	    }
	}

	// Show the program list
	_progList.clear();
	_progList.setChoices( formProgs );

        // MFO: Copied from ot.ProgListWindow in FreeBongo OT (orac2).
        // Added to default the key value of UKIRT ORAC use. AB 5May00
        keyTextBox.setText (user+"-0");
    }
 
 
    /**
     * fill in login fields if necessary and show the proper page;
     */
    public void updateWindow() {
	if (_progs == null) {
	    gotoPage(LOGIN_PAGE);
	}

	if ((_spAccess == null) || (_spAccess.password.equals("")) ||
	    (_username == null) || (_username.equals(""))) {
	    LoginInfo li = LastLogin.get();
	    if (li != null) {
		_setUsernameEntry(li.username);
		_setPasswordEntry(li.password);
		_setDatabaseChoice(li.database);
	    }
	}
    }

    /**
     * Part of the TextBoxWidgetWatcher interface that must be implemented,
     * but we aren't interested in.
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {}

    /**
     * This method implements part of the TextBoxWidgetWatcher interface from gemini.gui.
     */
    public void textBoxAction(TextBoxWidgetExt tbwe) {}

    /**
     * Receive notification when a PasswordWidgetExt that we are interrested
     * in has the return key pressed.
     *
     * This functionality used to be in textBoxAction.
     * MFO: May 28, 2001
     */
    public void actionPerformed(ActionEvent e)  {
	if (e.getSource() == passwordTextBox) {
	    fetchListing();
	} 
	else if (e.getSource() == keyTextBox) {
	    fetchProg();
	}
    }
}
