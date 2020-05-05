/*
 * Copyright 2001-2010 Science and Technology Facilities Council.
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

package omp;

import java.io.LineNumberReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.zip.GZIPOutputStream;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.Properties;
import gemini.sp.SpItem;
import gemini.sp.SpProg;
import orac.util.SpInputXML;
import orac.util.SpItemUtilities;

import gemini.util.ObservingToolUtilities;
import java.io.InputStream;

/**
 * SpClient
 *
 * Created: Mon Aug 27 18:30:31 2001
 *
 * @author Mathew Rippa, modified by Martin Folger
 */
public class SpClient extends SoapClient {
    /**
     * Configuration file for soap connection.
     *
     * The file soap.cfg which must be in the classpath is used as soap
     * configuration file.
     */
    private static final String CFG_FILE = "soap.cfg";

    /**
     * Sp Server Property.
     *
     * The property "spServer" specified in the "soap.cfg" file.
     */
    private static final String SP_SERVER_PROPERTY = "spServer";

    /** Default URL. */
    private static final String DEFAULT_SP_SERVER =
            "https://omp.eao.hawaii.edu/cgi-bin/spsrv.pl";

    /** SOAP action. */
    private static final String SOAP_ACTION = "urn:OMP::SpServer";

    /** Used to read in properties from file. */
    private static Properties _cfgProperties = new Properties();

    /**
     * Gets Sp Server URL from cfg file.
     *
     * @see #CFG_FILE
     */
    private static URL getURL() throws Exception {
        URL url = null;

        try {
            url = ObservingToolUtilities.resourceURL(CFG_FILE,
                    "ot.resource.cfgdir");
            InputStream is = url.openStream();
            _cfgProperties.load(is);

            String spServerProperty = _cfgProperties.getProperty(
                    SP_SERVER_PROPERTY);

            if (spServerProperty != null) {
                url = new URL(spServerProperty);
                System.out.println("Connecting to \"" + spServerProperty
                        + "\".");
            } else {
                url = new URL(DEFAULT_SP_SERVER);
                System.out.println("Property \"" + SP_SERVER_PROPERTY
                        + "\" not found in config file \"" + CFG_FILE + "\".\n"
                        + "Using default Sp Server URL \"" + DEFAULT_SP_SERVER
                        + "\"");
            }
        } catch (Exception e) {
            url = new URL(DEFAULT_SP_SERVER);
            e.printStackTrace();
            System.out.println("Problems while trying to read config file \""
                    + CFG_FILE + "\".\n" + "Using default Sp Server URL \""
                    + DEFAULT_SP_SERVER + "\"");
        }

        return url;
    }

    /** Test method. */
    public static void main(String[] args) {
        if (args.length == 0) {
            try {
                LineNumberReader reader = new LineNumberReader(
                        new InputStreamReader(System.in));
                String line = reader.readLine();

                String spXml = line;

                while (line != null) {
                    line = reader.readLine();

                    if (line != null) {
                        spXml += line + "\n";
                    }
                }

                System.out.println("Storing " + spXml + "\n...");

                String databaseSummary = storeProgram(
                        spXml, "abc", false).summary;

                System.out.println("Database Summary: " + databaseSummary);
            } catch (IOException e) {
                System.err.println("Problem reading from stdin: " + e);
            } catch (Exception e) {
                System.err.println(e);
            }
        } else {
            if (args[0].equals("-h")) {
                System.out.println("Usage: SpClient \"<SpProg> ...\"");
                System.out.println("   or: SpClient \"Science Program Title\"");

            } else {
                // "abc" used for as arbitrary password for testing.
                try {
                    System.out.println(
                            fetchProgramString(args[0], "abc").toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static SpItem replicateSp(SpItem currentItem, File catalog)
            throws Exception {
        SpItem spItem = null;
        flushParameter();

        try {
            char[] contents = new char[1024];
            FileReader fr = new FileReader(catalog);

            int readLength = 0;
            StringBuffer buffer = new StringBuffer();

            while (!fr.ready()) {
            }

            while ((readLength = fr.read(contents)) != -1) {
                buffer.append(contents, 0, readLength);
            }

            fr.close();

            // get the current science program and convert it to a string
            String currentSp = currentItem.toXML();
            Object toSend = null;
            Class<?> klass = null;

            toSend = compressString(currentSp);
            klass = byte[].class;

            addParameter("TemplateSp", klass, toSend);
            addParameter("Catalog", String.class, buffer.toString());
            addParameter("compress", String.class, "auto");
            Object o = doCall(getURL(), SOAP_ACTION, "SpInsertCat");
            Object[] tmp = (Object[]) o;

            Object sp = tmp[0];
            String spXML = null;

            if (sp instanceof byte[]) {
                spXML = new String((byte[]) gunzip(sp));
            } else if (sp instanceof String) {
                spXML = (String) sp;
            }

            final String info = (String) tmp[1];

            if (spXML != null && !spXML.equals("")) {
                System.out.println("Building replicated Science Program");
                spItem = new SpInputXML().xmlToSpItem(spXML);
            }

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(null, info,
                            "Replication returned the following information",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });

        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            throw (e);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw (e);
        } catch (Exception e) {
            System.out.println("Unexpected exception caught");
            e.printStackTrace();
            throw e;
        }

        return spItem;
    }

    /**
     * Fetch Science Program from database.
     *
     * @param id   Project ID
     * @param pass Password
     *
     * @return Science Program item.
     */

    public static SpProg fetchProgram(String id, String pass) throws Exception {
        flushParameter();

        addParameter("projectid", String.class, id);
        addParameter("password", String.class, pass);
        addParameter("compress", String.class, "auto");

        String spXML;

        try {
            byte[] input = (byte[]) doCall(getURL(), SOAP_ACTION,
                    "fetchProgram");
            spXML = new String(input);
        } catch (NullPointerException npe) {
            throw new NullPointerException("");
        } catch (Exception e) {
            e.printStackTrace();
            flushParameter();

            addParameter("projectid", String.class, id);
            addParameter("password", String.class, pass);

            spXML = new String((byte[]) doCall(getURL(), SOAP_ACTION,
                    "fetchProgram"));
        }

        SpItem spItem = (new SpInputXML()).xmlToSpItem(spXML);

        if (spItem instanceof SpProg) {
            return (SpProg) spItem;
        } else {
            return null;
        }
    }

    /**
     * Fetch Science Program as XML String from database (test method).
     *
     * @param id   Project ID
     * @param pass Password
     *
     * @return Science Program as XML String.
     */
    public static String fetchProgramString(String id, String pass)
            throws Exception {
        flushParameter();

        addParameter("projectid", String.class, id);
        addParameter("password", String.class, pass);

        return (String) doCall(getURL(), SOAP_ACTION, "fetchProgram");
    }

    /**
     * Store Science Program to database.
     *
     * @param spProg Science Program item.
     * @param pass   Password
     * @param force  Force to override database entry even if there are
     *               (timestamp) inconsistencies.
     */
    public static SpStoreResult storeProgram(SpProg spProg, String pass,
            boolean force) throws Exception {
        SpItemUtilities.removeReferenceIDs(spProg);
        SpItemUtilities.setReferenceIDs(spProg);

        // Set the ATTR_ELAPSED_TIME attributes in SpMSB components and
        // SpObs components that are MSBs.
        SpItemUtilities.saveElapsedTimes(spProg);

        spProg.setOTVersion();
        spProg.setTelescope();

        String sp = spProg.toXML();

        String forceString = force ? "1" : "0";

        byte[] toSend = compressString(sp);

        flushParameter();

        addParameter("sp", byte[].class, toSend);
        addParameter("password", byte[].class, pass.getBytes());
        addParameter("force", byte[].class, forceString.getBytes());

        return new SpStoreResult(doCall(getURL(), SOAP_ACTION, "storeProgram"));
    }

    private static byte[] compressString(String input) {
        byte[] toSend;

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gos = new GZIPOutputStream(bos);

            byte[] bytes = input.getBytes();
            byte[] buff = new byte[1024];
            int len;

            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

            while ((len = bis.read(buff)) > 0) {
                gos.write(buff, 0, len);
            }

            gos.flush();
            gos.close();
            bis.close();

            toSend = bos.toByteArray();

            bos.flush();
            bos.close();

        } catch (Exception e) {
            e.printStackTrace();
            toSend = input.getBytes();
        }
        return toSend;
    }

    /**
     * Store Science Prgram XML String to database (test method).
     *
     * @param sp    Science Program as XML String
     * @param pass  Password
     * @param force Force to override database entry even if there are
     *              (timestamp) inconsistencies.
     */
    public static SpStoreResult storeProgram(String sp, String pass,
            boolean force) throws Exception {
        String forceString = force ? "1" : "0";

        flushParameter();

        addParameter("sp", String.class, sp);
        addParameter("password", String.class, pass);
        addParameter("force", String.class, forceString);

        return new SpStoreResult(doCall(getURL(), SOAP_ACTION, "storeProgram"));
    }

    /**
     * Structure that is returned by SpClient.fetchProgram.
     *
     * It holds the transaction summary and a timestamp.
     *
     * @author Martin Folger (M.Folger@roe.ac.uk)
     */
    public static class SpStoreResult {
        public String summary = "";

        public int timestamp = 0;

        private StringBuffer instantiationErrors = new StringBuffer();

        public SpStoreResult() {
        }

        public SpStoreResult(Object resultObject)
                throws InstantiationException {
            Object[] resultArray = null;

            // Convert to array.
            if (resultObject instanceof Object[]) {
                resultArray = (Object[]) resultObject;
            } else {
                resultArray = new Object[1];
                resultArray[0] = resultObject;

                instantiationErrors.append(
                        "WARNING. storeProgram: no timestamp.\n");
            }

            // Fill SpStoreResult object.
            try {
                summary = (String) resultArray[0];
            } catch (Exception e) {
                instantiationErrors.append(
                        "storeProgram: could not read summary: " + e + "\n");
            }

            if (resultArray.length > 1) {
                try {
                    timestamp = (Integer) resultArray[1];
                } catch (Exception e) {
                    instantiationErrors.append(
                            "storeProgram: could not read timestamp: " + e
                            + "\n");
                }
            }

            if (instantiationErrors.length() > 0) {
                throw new InstantiationException(
                        instantiationErrors.toString());
            }
        }
    }

    /**
     * Get version information from the OMP.
     */
    public static VersionInfo getOTVersionInfo() throws Exception {
        Object result = doCall(getURL(), SOAP_ACTION, "getOTVersionInfo");
        int[] resultArray = (int[]) result;
        return new VersionInfo(
            Integer.toString(resultArray[0]),
            Integer.toString(resultArray[1]));
    }

    public static class VersionInfo {
        public String current;
        public String minimum;

        public VersionInfo(String current, String minimum) {
            this.current = current;
            this.minimum = minimum;
        }
    }
}
