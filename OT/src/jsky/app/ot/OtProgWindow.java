// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

import java.awt.Component;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import jsky.app.ot.gui.StopActionWatcher;
import jsky.app.ot.gui.StopActionWidget;
import jsky.app.ot.job.Job;
import jsky.app.ot.job.JobEvent;
import jsky.app.ot.job.JobWatcher;
import gemini.sp.SpAvTable;
import gemini.sp.SpFactory;
import gemini.sp.SpItem;
import gemini.sp.SpRootItem;
import gemini.sp.SpType;
import gemini.sp.ipc.SpServerWatcher;
import gemini.sp.ipc.SpAccess;
import gemini.sp.ipc.SpProgKey;
import gemini.sp.ipc.SpServerAsync;
import jsky.app.ot.util.Assert;
import ot.util.DialogUtil;
import ot.DatabaseDialog;

/**
 * The program hierarchy edit OtWindow subclass for science programs,
 * science plans and libraries.  Most of the program and plan specific
 * features implemented in this subclass have to do with the Observing
 * Database (ODB).  
 */
public final class OtProgWindow extends OtWindow 
    implements JobWatcher, SpServerWatcher, StopActionWatcher {

    private ConfirmLoginWindow  _confirmWin;
    private ConfirmLoginWindowDialog  _confirmDialog;
    private OtODBAccessWindow   _odbAccessWin;
    private SpServerAsync       _spServerAsync;

    /**
     * Default constructor.  Creates a new empty science program.
     */
    public OtProgWindow() {
	this((SpRootItem) SpFactory.create(SpType.SCIENCE_PROGRAM), null);
    }

    /**
     * Construct with a brand new program.
     */
    public OtProgWindow(SpRootItem spItem) {
	this(spItem, null);
    }

    /**
     * Construct with a program read from the database.
     */
    public OtProgWindow(SpRootItem spItem, LoginInfo loginInfo, SpProgKey progKey)  {
	this(spItem, null);

	_progInfo.login   = loginInfo;
	_progInfo.progKey = progKey;
    }

    /**
     * Construct from an SpItem read from a file described by the given
     * FileInfo.
     */
    public OtProgWindow(SpRootItem spItem, FileInfo fileInfo) {
	super(spItem, fileInfo);
    }

    /**
     * Do one-time only initialization of the window.
     */
    protected void _init(SpRootItem spItem, FileInfo fileInfo)  {
	super._init(spItem, fileInfo);

	_progInfo.isPlan  = false;
	SpType type = spItem.type();
	if (type.equals( SpType.SCIENCE_PLAN)) 
	    _progInfo.isPlan  = true;
	else if (type.equals( SpType.LIBRARY)) 
	    libFolderAction.setEnabled(true);
    }

    /**
     * Set the top level parent frame (or internal frame) for this window.
     * (Override here to set the frame icon).
     */
    public void setParentFrame(Component p) {
	super.setParentFrame(p);

	if (p instanceof JFrame && _curItem != null) {
	    SpType type = _curItem.type();
	    JFrame f = (JFrame)p;
	    if (type.equals( SpType.SCIENCE_PROGRAM )) 
		f.setIconImage(new ImageIcon(getClass().getResource("images/ngc104.gif")).getImage());
	    else if (type.equals( SpType.SCIENCE_PLAN )) 
		f.setIconImage(new ImageIcon(getClass().getResource("images/comet.gif")).getImage());
	    else if (type.equals( SpType.LIBRARY )) 
		f.setIconImage(new ImageIcon(getClass().getResource("images/libIcon.gif")).getImage());
	}
    }

    /** Return true if the SP type is LIBRARY */
    public boolean isLibrary() {
	return _curItem.type().equals(SpType.LIBRARY);
    }

    /** Add a library folder to the tree. */
    public void addLibFolder() {
	_tw.addItem(SpFactory.create(SpType.LIBRARY_FOLDER));
    }

    /** Return true if online */
    public boolean isOnline() {
	return _progInfo.online;
    }


    //
    // Check whether this program has ever been stored to the ODB.
    //
    private boolean _previouslyStored()  {
	// Has this program ever been stored to the server?
	return _tw.getProg().hasBeenNamed();
    }

    /**
     * Implementation of the StopActionWatcher interface.
     */
    public synchronized void stopAction(StopActionWidget saw) {
	if (_spServerAsync != null) {
	    _spServerAsync.abort();
	}
	_spServerAsync = null;
    }

    //
    // Show the "Accessing ODB ..." pop-up window.
    //
    private void _startODBAccess() {
	//setBusy();
	if (_odbAccessWin == null) {
	    _odbAccessWin = new OtODBAccessWindow(this);
	    if (OT.getDesktop() == null) 
		new OtODBAccessWindowFrame(_odbAccessWin);
	    else
		new OtODBAccessWindowInternalFrame(_odbAccessWin);
	}
	_odbAccessWin.startActions();
    }

    //
    // Remove the "Accessing ODB ..." pop-up window.
    //
    private synchronized void _stopODBAccess() {
	//setIdle();
	if (_odbAccessWin != null) {
	    _odbAccessWin.getParentFrame().setVisible(false); 
	    _odbAccessWin = null;
	}
	_spServerAsync = null;
    }
 
    /**
     * Called by SpServerAsync when an operation is aborted.
     *
     * @see SpServerWatcher
     */
    public synchronized void spServerAbort(SpServerAsync ssa) {
	_stopODBAccess();
    }
 
    /**
     * Called by SpServerAsync when an operation fails.
     *
     * @see SpServerWatcher
     */
    public synchronized void spServerError(String problem, SpServerAsync ssa) {
	_stopODBAccess();
	_progInfo.progKey = null;
	DialogUtil.error(this, "Problem communicating with ODB: " + problem);
    }
 
    /**
     * Called by the SpServerAsync when the program has been stored
     * in the ODB.
     *
     * @see SpServerWatcher
     */
    public void	spServerStoreComplete(SpItem  spItem, SpProgKey     key,
			      boolean isNew,  SpServerAsync ssa)  {
	_stopODBAccess();
	resetItem((SpRootItem) spItem);
	if (isNew) {
	    // Remember the key, update the name of the program in the tree
	    // view, and show the user the key.
	    _progInfo.progKey = key;

	    DialogUtil.message(this, "This program's key is \"" + key.key + "\".");
	    
	    // Update the LastLogin information so that the next time we store
	    // a new program, we'll have a guess as to what the username and
	    // database is ...
	    LastLogin.set(_progInfo.login.username,
		 	  _progInfo.login.database, _progInfo.login.password);
	}
    }
 
    /**
     * Called by SpServerAsync when the program listing completes
     * successfully.  Not used here.
     *
     * @see SpServerWatcher
     */
    public synchronized void spServerListComplete(Vector progList, SpServerAsync ssa) { }
 
    /**
     * Called by SpServerAsync when a program fetch completes
     * successfully.
     *
     * @see SpServerWatcher
     */
    public synchronized void spServerFetchComplete(SpItem spItem, SpServerAsync ssa)  {
	_stopODBAccess();
	resetItem((SpRootItem) spItem);
    }


    /**
     * Fetch a program from the server.
     */
    public synchronized boolean	fetchProg() {
	if (_spServerAsync != null) {
	    return false;
	}

	// Make sure this program has been sent to the database before
	if (!_previouslyStored()) {
	    DialogUtil.error(this, "This program has never been stored.");
	    return false;
	}

	// Check whether we have to prompt for login information or program keys.
	if (!_confirmLoginInfo()) {
	     return false;
	 }

	if (_progInfo.access == null) {
	    _progInfo.access = new SpAccess(_progInfo.login.password,
					    _progInfo.login.database);
	}

	_startODBAccess();
	_spServerAsync = SpServerAsync.fetch( _progInfo.access, _progInfo.progKey, this);
	return true;
    }

    /**
     * Go online, first storing the current version of the program.
     */
    public void	goOnline(boolean goOnline)  {
	if (goOnline && storeProg()) {
	    _progInfo.online = true;
	} else {
	    _progInfo.online = false;
	}
    }


    /**
     * Confirm login and key info.
     */
    private boolean _confirmLoginInfo() {
	boolean previouslyStored = _previouslyStored();

	// Don't need confirmation if we already have valid information.
	if ( (_progInfo.login != null) && (!previouslyStored || (_progInfo.progKey != null)) ) {
	    return true;
	}

	// Popup the dialog window with the relevant information and wait
	// for a response.  The dialog window needs to know whether to
	// prompt for a program key.  It needs to if this program was previously
	// stored, but we don't already have a key for it. 
	// Also, the first time the _confirmWin is used, we call LastLogin.get()
	// to get the last login information that was entered anywhere (if any).
	// This will likely be what the user wants again.
	if (_confirmWin == null) {
	    boolean keyNeeded = (previouslyStored && (_progInfo.progKey==null));
	    String  key = (_progInfo.progKey == null) ? null : _progInfo.progKey.key;
	    _confirmWin = new ConfirmLoginWindow(LastLogin.get(), key, keyNeeded);
	    _confirmDialog = new ConfirmLoginWindowDialog(_confirmWin);
	}

	_confirmDialog.setVisible(true);
	if (_confirmWin.wasConfirmed()) {
	    _progInfo.login = _confirmWin.getLoginInfo();
	    _progInfo.progKey = new SpProgKey(_tw.getProg().name(),
		 			      _confirmWin.getKey());
	} else {
	    _progInfo.login = null;
	    _progInfo.progKey = null;
	}

	return _confirmWin.wasConfirmed();
    }

    /**
     * Store a program to the server.
     */
    public synchronized boolean	storeProg() {
	if (_spServerAsync != null) {
	    return false;
	}

	// Check whether we have to prompt for login information or program keys.
	if (!_confirmLoginInfo()) {
	    return false;
	}

	// We shouldn't be able to get here without this information set
	Assert.notNull(_progInfo.login);
	Assert.notFalse(!_previouslyStored() || (_progInfo.progKey != null));

	if (_progInfo.access == null) {
	    _progInfo.access = new SpAccess(_progInfo.login.password,
					    _progInfo.login.database);
	}
	boolean  success = false;

	SpAccess  ac     = _progInfo.access;
	SpProgKey pk     = _progInfo.progKey;

	// Store the file info into attributes in the program.  When the
	// program is subsequently fetched, these attributes will be used
	// to initialize the program.
	FileInfo fi = _progInfo.file;
	if (fi.hasBeenSaved) {
	    Assert.notNull(fi.dir); Assert.notNull(fi.filename);
	    SpItem   spItem = _tw.getProg();
	    SpAvTable avTab = spItem.getTable();
	    avTab.set(".gui.dir",          fi.dir);
	    avTab.set(".gui.filename",     fi.filename);
	    avTab.set(".gui.hasBeenSaved", fi.hasBeenSaved);
	}

	// Get the selected node and remember that it was selected.
	_tw.rememberSelection();

	_startODBAccess();

	// Store the program
	if (_previouslyStored()) {
	    _spServerAsync = SpServerAsync.store( ac, pk, _tw.getProg(), this);
	} else {
	    String name = _progInfo.login.username;
	    _spServerAsync = SpServerAsync.storeNew( ac, _tw.getProg(), name, this);
	}

	return true;
    }



    /**
     * Called by worker thread when a job is started.
     */
    public void jobStarted(Job job)   {
	System.out.println("JOB STARTED");
    }

    /**
     * Called by worker thread when a job is finished.
     */
    public void jobFinished(Job job)  {
	System.out.println("JOB FINISHED");
	//setIdle();
    }


    /** 
     * Fetch a science program from an online database.
     */
    public void fetchFromOnlineDatabase() {
	Thread t = new Thread( new Runnable() {
		public void run() {
		    if(System.getProperty("OMP") != null) {
			OT.getDatabaseDialog().fetchProgram();
		    }
		    else {
			fetchProg();
		    }
		}
	    });
	t.start();
    }


    /** 
     * Store the current science program to an online database.
     */
    public void storeToOnlineDatabase() {
	Thread t = new Thread( new Runnable() {
		public void run() {
		    if(System.getProperty("OMP") != null) {
			OT.getDatabaseDialog().storeProgram(getItem());
		    }
		    else {
			OtProgWindow.this.storeProg();
		    }
		}	    
	    });
	t.start();
    }


    /** 
     * Go to online (database) edit mode.
     */
    public void goToOnlineEditMode() {
	if (_progInfo.online) {
	    System.out.println("XXX goToOnlineEditMode: going offline?");
	    goOnline(false);
	} 
	else {
	    System.out.println("XXX goToOnlineEditMode: going online?");
	    goOnline(true);
	}
    }
}

