package ot.util;

import jsky.catalog.Catalog;
import jsky.catalog.CatalogFactory;
import jsky.catalog.QueryArgs;

import jsky.catalog.TableQueryResult;
import jsky.catalog.skycat.SkycatConfigFile;

import jsky.coords.HMS;
import jsky.coords.DMS;

import java.util.Iterator;
//import java.net.URLEncoder;

/**
 * Name resolver utility for target list.
 *
 * This class uses the jsky.catalog package to implement a simple name resolver.
 * Currently used in {@link jsky.app.ot.editor.EdCompTargetList}
 *
 * @author Martin Folger
 */
public class NameResolver {

  protected String _id;
  protected String _ra;
  protected String _dec;

  public NameResolver(String catalogName, String queryString) {
    // Using  URLEncoder.encode would be nice but it replaces ' ' with '+' which
    // doesn't do the trick. ' ' has to be replaced with "%20".
    //queryString= URLEncoder.encode(queryString);
    do {
      queryString = queryString.substring(0, queryString.indexOf(' '))
                  + "%20"
                  + queryString.substring(queryString.indexOf(' ') + 1);
    
    } while(queryString.indexOf(' ') >= 0);

    Catalog catalog = SkycatConfigFile.getConfigFile(null).getCatalog(catalogName);

    if((catalogName == null) || (System.getProperty("DEBUG") != null)) {
      System.out.println("NameResolver(\"" + catalogName + "\", \"" + queryString + "\") called.");
      int n = SkycatConfigFile.getConfigFile(null).getNumCatalogs();
      System.out.println("There are " + n + " catalogs available.");
      System.out.println("Pick one of the following catalogs:");
      for(int i = 0; i < n; i++) {
        System.out.println("    " + SkycatConfigFile.getConfigFile(null).getCatalog(i));
      }
    }

    if(catalog == null) {
      throw new RuntimeException("Could not create catalog \"" + catalogName + "\"");
    }

    QueryArgs queryArgs = new QueryArgs(catalog);

    queryArgs.setParamValue(0, queryString);

    TableQueryResult tableQueryResult = (TableQueryResult)catalog.query(queryArgs);

    if((tableQueryResult.getRowCount() > 1) && (System.getProperty("DEBUG") != null)) {
      System.out.println("NameResolver: More than 1 query result. Only the first result is used.");
    }

    if((tableQueryResult == null) || (tableQueryResult.getRowCount() < 1)) {
      throw new RuntimeException("No query result.");
    }

    _id  = "" + tableQueryResult.getValueAt(0, 0);

    try {
      double raDegDouble = Double.parseDouble("" + tableQueryResult.getValueAt(0, 1));
      double raDouble    = (raDegDouble * 24) / 360;
    
      _ra = (new HMS(raDouble)).toString();
    }
    catch(NumberFormatException e) {
      _ra  = "";
    }

    try{
      double decDouble   = Double.parseDouble("" + tableQueryResult.getValueAt(0, 2));

      _dec = (new DMS(decDouble)).toString();
    }
    catch(NumberFormatException e) {
      _dec = "";
    }

    if(System.getProperty("DEBUG") != null) {
      System.out.println("Complete query result table for catalog " + catalogName + ":");
      for(int i = 0; i < tableQueryResult.getRowCount(); i++) {
        for(int j = 0; j < tableQueryResult.getColumnCount(); j++) {
          System.out.print("(" + i + ", " + j + ") = " + tableQueryResult.getValueAt(i, j) + ";    ");
        }
        System.out.println("");
      }
    }
  }

  public String getId()  { return _id;  }
  public String getRa()  { return _ra;  }
  public String getDec() { return _dec; }

  public static boolean isAvailableAsCatalog(String catalogName) {
    return (SkycatConfigFile.getConfigFile(null).getCatalog(catalogName) != null);
  }

  public static void main(String [] args) {
    try {
      NameResolver nameResolver = new NameResolver(args[0], args[1]);
      System.out.println("Id  = " + nameResolver.getId());
      System.out.println("Ra  = " + nameResolver.getRa());
      System.out.println("Dec = " + nameResolver.getDec());
    }
    catch(Exception e) {
      e.printStackTrace();
      System.out.println("Usage: NameResolver \"catalog name\" \"query string\"");
    }
  }
}
