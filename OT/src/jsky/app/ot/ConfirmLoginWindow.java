// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import gemini.sp.ipc.SpAccess;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;

/**
 * This is a dialog window that is used to prompt the user for a username,
 * a password, a database (phaseII or active), and if necessary a program
 * key.
 */
final class ConfirmLoginWindow extends ConfirmAccessInfoGUI
    implements TextBoxWidgetWatcher {

    private boolean  _confirmed = false;
    private LoginInfo _loginInfo;
    private String    _key;

    private JDialog _parentDialog;


    /**
     * Construct with the known login information, the program key (if known),
     * and whether or not the "key" field is needed.  The key field is only
     * needed when trying to fetch a program in the database that has not
     * been fetched since the last time that OT was started.
     */
    public ConfirmLoginWindow(LoginInfo li, String key, boolean keyNeeded) {
	_loginInfo = li;
	_key       = key;

	// Fill in as many fields as possible
	if (_loginInfo != null) {
	    loginTextBox.setText(_loginInfo.username);
	    passwordTextBox.setText(_loginInfo.password);
	    _setDatabaseChoice(_loginInfo.database);
	} else {
	    _setDatabaseChoice(SpAccess.PHASEII);
	}

	if (_key != null) {
	    keyTextBox.setText(_key);
	}

	// Disable the key field if there's no need to set it.
	if (!keyNeeded) {
	    keyTextBox.setEnabled(false);
	    //keyTextBox.setStyle(PLAIN);
	}


	TextBoxWidgetExt passwdTBW;
	passwdTBW = passwordTextBox;
 
	TextBoxWidgetExt keyTBW;
	keyTBW    = keyTextBox;

	passwdTBW.addWatcher(this);
	keyTBW.addWatcher(this);

	// handle the Confirm and Cancel buttons
	confirmButton.addWatcher( new CommandButtonWidgetWatcher() {
		public void commandButtonAction(CommandButtonWidgetExt cbwe) {
		    _confirm();
		}
	    });
	cancelButton.addWatcher( new CommandButtonWidgetWatcher() {
		public void commandButtonAction(CommandButtonWidgetExt cbwe) {
		    _cancel();
		}
	    });

	ButtonGroup grp = new ButtonGroup();
	grp.add(phaseIIOption);
	grp.add(activeOption);
    }


    /**
     * Determine whether the user confirmed the login info or canceled the
     * request.
     */
    public boolean wasConfirmed() {
	return _confirmed;
    }

    public LoginInfo getLoginInfo() { return _loginInfo; }
    public String    getKey()       { return _key; }


    //
    // Return either SpAccess.PHASEII or SpAccess.ACTIVE, depending upon
    // which database option the user has selected.
    //
    private int _getDatabaseChoice()  {
	int database = SpAccess.PHASEII;
	OptionWidgetExt ow;
	ow = phaseIIOption;
	boolean b = ow.getValue();
	if (! b) {
	    database = SpAccess.ACTIVE;
	}
	return database;
    }

 
    //
    // Set the database option widget to reflect the given choice
    // (SpAccess.PHASEII or SpAccess.ACTIVE).
    //
    private void _setDatabaseChoice(int database) {
	OptionWidgetExt phaseII;
	OptionWidgetExt active;
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
    // Confirm
    //
    private void _confirm() {
	_loginInfo = new LoginInfo(loginTextBox.getText().trim(),
				   _getDatabaseChoice(), 
				   passwordTextBox.getText().trim());
	_key       = keyTextBox.getText().trim();
	_confirmed = true;
	_parentDialog.setVisible(false);
    }


    //
    // Cancel
    //
    private void _cancel() {
	_loginInfo = null;
	_key       = null;
	_confirmed = false;
	_parentDialog.setVisible(false);
    }
 
    /**
     * Notification that a text box widget being watched has had a key pressed
     * in it.  (This method must be present as part of jsky.app.ot.gui, but is
     * ignored here.)
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
    }


    /**
     * Receive notification when a TextBoxWidgetExt that we are interrested
     * in has the return key pressed.  This method implements part of the
     * TextBoxWidgetWatcher interface from jsky.app.ot.gui.  When the return key
     * is pressed in the password or key text boxes, confirm the information.
     */
    public void textBoxAction(TextBoxWidgetExt tbwe) {
	if (tbwe == passwordTextBox) {
	    _confirm();
	} 
	else if (tbwe == keyTextBox) {
	    _confirm();
	}
    }

    public void setParentDialog(JDialog d) {_parentDialog = d;}
    public JDialog getParentDialog() {return _parentDialog;}

}
