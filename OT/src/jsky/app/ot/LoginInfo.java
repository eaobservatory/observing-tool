// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot ;

/**
 * This class stores "login" data associated with a program.  This includes
 * the username, password, and database choice (phaseII or active).  The
 * primary purpose of this class is to group this information in one place
 * to simplify its manipulation.
 *
 * <p>
 * Login information is stored so that once the user enters his username,
 * password, and database choice, it is remembered the next time and need
 * not be re-entered.
 */
public final class LoginInfo
{
	public String username ;
	public int database ;
	public String password ;

	public LoginInfo( String username , int database , String password )
	{
		this.username = username ;
		this.database = database ;
		this.password = password ;
	}

	public LoginInfo( LoginInfo ll )
	{
		this.username = ll.username ;
		this.database = ll.database ;
		this.password = ll.password ;
	}
}
