// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

/**
 * This is a utility class used to store "login" information from the last
 * successful login.  It is used to initialize dialog boxes with likely
 * username/password/database information.
 *
 * @see LoginInfo
 */
public final class LastLogin {

    private static LoginInfo _loginInfo;

    /**
     * Set the login information, replacing any "last" login information
     * that was entered previously.
     */
    public static synchronized void set(String username, int database, String password) {
	if (_loginInfo == null) {
	    _loginInfo = new LoginInfo(username, database, password);
	} 
	else {
	    _loginInfo.username = username;
	    _loginInfo.database = database;
	    _loginInfo.password = password;
	}
    }

    /**
     * Get the last entered login information.
     */
    public static synchronized LoginInfo get() {
	LoginInfo curValue = null;
	if (_loginInfo != null) {
	    curValue = new LoginInfo(_loginInfo);
	}

	return curValue;
    }
}
