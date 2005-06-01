/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package ODBAccess;

import java.io.*;
import java.net.*;
import java.util.*;

//import ot_ukirt.util.*;

/**
 * Class to handle reading of the admin file
 */
public class AdminFileReader
{
   private BufferedReader admFile;

/**
 * The constructor - opens the admin file
 */
public AdminFileReader(URL baseURL, String admFilename)
{
   URL url=null;
   try {
      url = new URL(baseURL, admFilename);
   } catch (MalformedURLException ex) {
      System.out.println("Problem constructing the admin. file URL: " + ex);
   }

   try {
      admFile = new BufferedReader (new InputStreamReader (url.openStream()));
   } catch (IOException ex) {
      System.out.println("Problem opening the admin file: " + ex);
   }

}

public AdminFileReader(String admFilename)
{
   try {
      admFile = new BufferedReader (new FileReader (admFilename));
   } catch (FileNotFoundException ex) {
      System.out.println("Problem opening the admin. file: " + ex);
   }
}

/**
 * Return the file as an AdminTable.
*/
public AdminTable readFile () throws IOException {

  String line;
  StringTokenizer st;
  AdminTable adminTable = new AdminTable ();

  // while there are more lines
  while ((line = admFile.readLine()) != null) {

    // ignore blanks
    if (line.length() == 0) continue;

    // ignore comments if not yet started block
    if (line.startsWith("#")) continue;
    
    // parse the line
    st = new StringTokenizer (line,":");
    Vector record = new Vector();
    while (st.hasMoreTokens()) {
      record.addElement (st.nextToken().trim());
    }

    // append record
    adminTable.addRow (record);
  }

  return adminTable;
}

/**
 * close the admin file
 */
public void close () {
   if (admFile != null) {
      try {
         admFile.close();
      }catch (IOException e) {}
   admFile = null;
   }
}

}








