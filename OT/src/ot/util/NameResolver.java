package ot.util;

import jsky.catalog.Catalog;
import jsky.catalog.CatalogFactory;
import jsky.catalog.QueryArgs;

import jsky.catalog.TableQueryResult;
import jsky.catalog.skycat.SkycatConfigFile;

import java.util.Iterator;

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

    Catalog catalog = SkycatConfigFile.getConfigFile(null).getCatalog(catalogName);
      
    if(catalog == null) {
      if(System.getProperty("DEBUG") != null) {
	int n = SkycatConfigFile.getConfigFile(null).getNumCatalogs();
        System.out.println("There are " + n + " catalogs available.");
	System.out.println("Pick one of the following catalogs:");
	for(int i = 0; i < n; i++) {
          System.out.println("    " + SkycatConfigFile.getConfigFile(null).getCatalog(i));
	}
      }  
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
    _ra  = "" + tableQueryResult.getValueAt(0, 1);
    _dec = "" + tableQueryResult.getValueAt(0, 2);
  }

  public String getId()  { return _id;  }
  public String getRa()  { return _ra;  }
  public String getDec() { return _dec; }

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
