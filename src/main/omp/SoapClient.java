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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import javax.swing.SwingUtilities;

import gemini.util.JACLogger;
import gemini.util.SelectableDialog;
import jsky.app.ot.LoginInfo;
import jsky.app.ot.OT;

import org.apache.soap.Header;
import org.apache.soap.Constants;
import org.apache.soap.Fault;
import org.apache.soap.rpc.Parameter;
import org.apache.soap.rpc.Call;
import org.apache.soap.rpc.Response;
import org.apache.soap.transport.http.SOAPHTTPConnection;
import org.apache.soap.SOAPException;
import org.w3c.dom.Element;

import java.io.IOException;

/**
 * SoapClient.java
 *
 * Created: Mon Aug 27 18:30:31 2001
 *
 * @author <a href="mailto:mrippa@jach.hawaii.edu">Mathew Rippa</a>,
 *         modified by Martin Folger
 */
public class SoapClient {
    /**
     * Soap fault caused by attempt to store a Science Program to server whose
     * time stamp has changed.
     */
    public static final String SP_CHANGED_ON_DISK = "SpChangedOnDisk";
    public static final String OLD_FAULT_CODE_SP_CHANGED_ON_DISK =
            "SOAP-ENV:Server." + SP_CHANGED_ON_DISK;
    public final static String FAULT_CODE_SP_CHANGED_ON_DISK =
            "soap:Server." + SP_CHANGED_ON_DISK;

    private static Header header = null;

    private static Vector<Parameter> params = new Vector<Parameter>();

    public static final String FAULT_CODE_INVALID_USER =
            "SOAP-ENV:Client.InvalidUser";

    private static JACLogger logger = JACLogger.getLogger(SoapClient.class);

    /**
     * Add a Parameter to the next Call that is to take place.
     *
     * @param name a <code>String</code> value.
     *             The name of the parameter to be added to next soap Call.
     *
     * @param type a <code>Class</code> value.
     *             The explicit Class of this parameter.
     *
     * @param val an <code>Object</code> value.
     *            The object to register this Parameter with.
     */
    protected static void addParameter(String name, Class<?> type, Object val) {
        params.add(new Parameter(name, type, val, null));
    }

    /**
     * Clear the Vector of parameters Objects to be sent in the next call.
     */
    protected static void flushParameter() {
        params.clear();
    }

    /**
     * Send the Call with various configurations.
     *
     * @param url an <code>URL</code> val indicating the soap server to
     *            connect to.
     * @param soapAction a <code>String</code>.
     *                   The URN like "urn:OMP::MSBServer"
     * @param methodName a <code>String</code>
     *                   The name of the method to call in the server.
     *
     * @return an <code>Object</code> returned by the method called in the
     *         server.
     */
    protected static Object doCall(URL url, String soapAction,
            String methodName) throws Exception {
        Object obj = new Object();

        try {
            Call call = new Call();

            call.setTargetObjectURI(soapAction);
            call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);
            call.setMethodName(methodName);
            call.setParams(params);

            if (header != null) {
                call.setHeader(header);
            }

            soapAction += "#" + methodName;

            if (System.getProperty("http.proxyHost") != null
                    && System.getProperty("http.proxyHost").length() > 0) {
                System.out.println("Using proxy server");

                SOAPHTTPConnection st = new SOAPHTTPConnection();
                st.setProxyHost(System.getProperty("http.proxyHost"));
                st.setProxyPort(Integer.parseInt(
                        System.getProperty("http.proxyPort")));

                call.setSOAPTransport(st);
            }

            Response resp = call.invoke(url, soapAction);

            // check response
            if (!resp.generatedFault()) {
                Parameter ret = resp.getReturnValue();
                if (ret == null) {
                    obj = null;
                } else {
                    obj = ret.getValue();
                }

                // Reset the params vector.
                params.clear();

                if (obj instanceof byte[]) {
                    obj = gunzip((byte[]) obj);
                }

                // Extract the header.
                String user = null;
                String token = null;
                try {
                    Header respHeader = resp.getHeader();
                    if (respHeader != null) {
                        Vector v = respHeader.getHeaderEntries();
                        if (v != null) {
                            for (Object o: v) {
                                Element e = (Element) o;
                                if ("user".equals(e.getLocalName())) {
                                    user = e.getTextContent();
                                }
                                if ("token".equals(e.getLocalName())) {
                                    token = e.getTextContent();
                                }
                            }
                            if ((user != null) && (token != null)) {
                                OT.loginInfo = new LoginInfo(user, token);
                            }
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                // return result
                return obj;

            } else {
                Fault fault = resp.getFault();

                // Handle special fault codes here.
                final String faultCode = fault.getFaultCode();
                final String faultString = fault.getFaultString();

                if (faultCode.endsWith(SP_CHANGED_ON_DISK)) {
                    throw new SpChangedOnDiskException(faultString);
                } else if (faultCode.equals(FAULT_CODE_INVALID_USER)) {
                    throw new InvalidUserException(faultString);
                }

                logger.error(faultString);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        SelectableDialog.showError(
                            "Code:    " + faultCode + "\n" +
                            "Problem: " + faultString);
                    }
                });
            }
        } catch (SpChangedOnDiskException e) {
            // Re-throw SpChangedOnDiskException so it can be handled
            // somewhere else.
            throw e;
        } catch (SOAPException se) {
            final String message = se.getMessage();
            logger.error(message);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    SelectableDialog.showError("SOAP Exception", message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] gunzip(byte[] input) {
        if (input != null) {
            try {
                System.out.println("Attempting to gunzip");

                int head = ((int) input[0] & 0xff) | ((input[1] << 8) & 0xff00);
                boolean heads = GZIPInputStream.GZIP_MAGIC == head;
                boolean tails = (char) input[0] != '<'
                        && (char) input[1] != '?';

                if (heads && tails) {
                    System.out.println("Appears to be gzip'd");

                    ByteArrayInputStream bis = new ByteArrayInputStream(input);
                    GZIPInputStream gis = new GZIPInputStream(bis);

                    byte[] read = new byte[1024];
                    int len;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    while ((len = gis.read(read)) > 0) {
                        bos.write(read, 0, len);
                    }

                    gis.close();
                    bis.close();

                    input = bos.toByteArray();

                } else {
                    System.out.println("Does not appear to be gzip'd");
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (ClassCastException cce) {
                cce.printStackTrace();
            }
        }

        return input;
    }
}
