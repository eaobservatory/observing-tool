/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package ODBAccess;

/**
 * This implements the UserRecord class. Stores the information associated
 * with an ODB user. A userrecord contains the username, password and
 * the name of the directory containing the programs.
 */
public class UserRecord
{
  private String _username;
  private String _password;
  private String _dirname;

/**
 * The constructor creates the record given all the information
 */
  public UserRecord (String user, String passwd, String dir) {
    _username = user;
    _password = passwd;
    _dirname = dir;
      
  }

  /**
   * Return the username
   */
  public String getUsername () {
    return _username;
  }

  /**
   * Return the password
   */
  public String getPassword () {
    return _password;
  }

  /**
   * Return the directory
   */
  public String getDirname () {
    return _dirname;
  }

  /**
   * Set the username
   */
  public void setUsername (String user) {
    _username = user;
  }

  /**
   * Set the password
   */
  public void setPassword (String passwd) {
    _password = passwd;
  }

  /**
   * Set the directory
   */
  public void setDirname (String dir) {
    _dirname = dir;
  }

  /**
   * Check the supplied password against the one in the userrecord
   */
  public boolean checkAccess (String passwd) {
    if (passwd.equals(_password)) return true;
    return false;
  }

  /**
   * Check access. This call checks both the directory name and the password.
   * When a program is "fetched" the key will be given. In the current 
   * implementation this key contains the directory. This is used in this
   * check - but it only validates if the password is correct for this
   * directory. This implies that 2 users cannot use the same directory 
   * (unless their passwords are the same!). 
   */
  public boolean checkAccess (String passwd, String dirname) {
    if (dirname.equals(_dirname)) {
      if (passwd.equals(_password)) return true;
      return false;
    }
    return false;
  }

}









