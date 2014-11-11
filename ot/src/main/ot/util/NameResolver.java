/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ot.util;

import jsky.catalog.Catalog;
import jsky.catalog.BasicQueryArgs;
import jsky.catalog.TableQueryResult;
import jsky.catalog.skycat.SkycatConfigFile;
import jsky.util.gui.ProgressException;
import jsky.coords.HMS;
import jsky.coords.DMS;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.StringTokenizer;

/**
 * Name resolver utility for target list.
 *
 * This class uses the jsky.catalog package to implement a simple name
 * resolver. Currently used in {@link jsky.app.ot.editor.EdCompTargetList}
 *
 * @author Martin Folger
 */
public class NameResolver {
    protected String _id = "";
    protected String _ra = "";
    protected String _dec = "";
    protected String _eq = "";

    public NameResolver(String catalogName, String queryString)
            throws ProgressException {
        System.out.println("Using catalog " + catalogName + "!");

        if (catalogName.equalsIgnoreCase("Personal Catalog")) {
            this.getPersonalData(queryString);

        } else {
            queryString = queryString.replaceAll(" ", "%20");

            Catalog catalog =
                    SkycatConfigFile.getConfigFile().getCatalog(catalogName);

            if ((catalogName == null)
                    || (System.getProperty("DEBUG") != null)) {
                System.out.println("NameResolver( \"" + catalogName
                        + "\" , \"" + queryString + "\" ) called.");

                int n = SkycatConfigFile.getConfigFile().getNumCatalogs();

                System.out.println("There are " + n + " catalogs available.");
                System.out.println("Pick one of the following catalogs:");

                for (int i = 0; i < n; i++) {
                    System.out.println("    "
                            + SkycatConfigFile.getConfigFile().getCatalog(i));
                }
            }

            if (catalog == null) {
                throw new RuntimeException("Could not create catalog \""
                        + catalogName + "\"");
            }

            BasicQueryArgs queryArgs = new BasicQueryArgs(catalog);
            queryArgs.setId(queryString);

            TableQueryResult tableQueryResult = null;

            try {
                Object queryResult = catalog.query(queryArgs);

                if (queryResult instanceof TableQueryResult) {
                    tableQueryResult = (TableQueryResult) queryResult;
                }

            } catch (ProgressException e) {
                // Query interrupted by user.
                // Handle exception in the calling class.
                System.out.println("Rethrowing " + e);
                throw e;

            } catch (Exception e) {
                // Exception is handled in the following code because
                // tableQueryResult is still null.
                System.out.println(e);
            }

            if ((tableQueryResult == null)
                    || (tableQueryResult.getRowCount() < 1)) {
                throw new RuntimeException("No query result.");
            }

            if ((tableQueryResult.getRowCount() > 1)
                    && (System.getProperty("DEBUG") != null)) {
                System.out.println("NameResolver: More than 1 query result."
                        + " Only the first result is used.");
            }

            _id = "" + tableQueryResult.getValueAt(0, 0);

            try {
                double raDegDouble = Double.parseDouble(
                        "" + tableQueryResult.getValueAt(0, 1));
                double raDouble = (raDegDouble * 24) / 360;

                _ra = (new HMS(raDouble)).toString();

            } catch (NumberFormatException e) {
                _ra = "";
            }

            try {
                double decDouble = Double.parseDouble(
                        "" + tableQueryResult.getValueAt(0, 2));

                _dec = (new DMS(decDouble)).toString();

            } catch (NumberFormatException e) {
                _dec = "";
            }

            if (System.getProperty("DEBUG") != null) {
                System.out.println("Complete query result table for catalog "
                        + catalogName + ":");

                for (int i = 0; i < tableQueryResult.getRowCount(); i++) {
                    for (int j = 0; j < tableQueryResult.getColumnCount();
                            j++) {
                        System.out.print("(" + i + ", " + j + ") = "
                                + tableQueryResult.getValueAt(i, j)
                                + " ;    ");
                    }

                    System.out.println("");
                }
            }
        }
    }

    // New method added by SdW
    private void getPersonalData(String queryString) {
        File catalog = new File(System.getProperty("OT_CATALOG_FILE"));

        try {
            BufferedReader in = new BufferedReader(new FileReader(catalog));
            String line;

            while ((line = in.readLine()) != null) {
                if (line.startsWith("*") || line.equals("\n")
                        || line.equals("\r")) {
                    continue;
                }

                if (line.startsWith(queryString)) {
                    StringTokenizer st = new StringTokenizer(line);

                    // Line should be of the form
                    // source HH MM SS.SSS +/- DD MM SS.SSS EQ
                    // where the 1st 3 fields are the RA, the next 4
                    // are the dec and the last is the equinox
                    // First skip the source.
                    _id = st.nextToken();
                    _ra = st.nextToken()
                            + ":" + st.nextToken()
                            + ":" + st.nextToken();
                    _dec = st.nextToken() + st.nextToken()
                            + ":" + st.nextToken()
                            + ":" + st.nextToken();
                    _eq = st.nextToken();

                } else {
                    continue;
                }
            }
        } catch (FileNotFoundException fnf) {
            System.out.println("Unable to find catalog file.");

        } catch (IOException ioe) {
            System.out.println("IO Exception");
        }
    }

    public String getId() {
        return _id;
    }

    public String getRa() {
        return _ra;
    }

    public String getDec() {
        return _dec;
    }

    // Added by SdW
    public String getEquinox() {
        return _eq;
    }

    // END

    public static boolean isAvailableAsCatalog(String catalogName) {
        if (catalogName.equals("Personal Catalog")) {
            return true;
        } else {
            return (SkycatConfigFile.getConfigFile().getCatalog(catalogName)
                    != null);
        }
    }

    public static void main(String[] args) {
        try {
            NameResolver nameResolver = new NameResolver(args[0], args[1]);
            System.out.println("Id  = " + nameResolver.getId());
            System.out.println("Ra  = " + nameResolver.getRa());
            System.out.println("Dec = " + nameResolver.getDec());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(
                    "Usage: NameResolver \"catalog name\" \"query string\"");
        }
    }
}
