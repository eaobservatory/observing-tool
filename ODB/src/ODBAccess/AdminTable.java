/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package ODBAccess;

import java.util.*;

import orac.util.LookUpTable;

/**
 * This implements the ODB admin file, as a specialised LookUpTable
 */
public class AdminTable extends LookUpTable
{

  /**
   * The constructor initialises just the super class
   */
  public AdminTable ()
    {
      super();
    }

  /**
   * Return a user-record for a given user
   */
  public UserRecord getUser (String user) {
    UserRecord ur=null;
    
    try {
      int recrow = indexInColumn (user, 0);
      Vector record = getRow(recrow);
      if (record != null)
	{
	  ur = new UserRecord ((String) record.elementAt(0), 
			       (String) record.elementAt(1),
			       (String) record.elementAt(2));
	}
      
    }catch (Exception ex) {}

    return ur;
  }
  
  /**
   * Return a userrecord determined by directory. This probably assumes unique
   * directories!
   */
  public UserRecord getUserByDir (String dir) {
    UserRecord ur=null;
    
    try {
      int recrow = indexInColumn (dir, 2);
      Vector record = getRow(recrow);
      if (record != null)
	{
	  ur = new UserRecord ((String) record.elementAt(0), 
			       (String) record.elementAt(1),
			       (String) record.elementAt(2));
      }
    }catch (Exception ex) {}
    
    return ur;
  }
}  
