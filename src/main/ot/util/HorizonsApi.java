/*
 * Copyright (C) 2021 East Asian Observatory
 * All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful,but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc.,51 Franklin
 * Street, Fifth Floor, Boston, MA  02110-1301, USA
 */

package ot.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class HorizonsApi {
    final static String base_url = "https://ssd.jpl.nasa.gov/api/horizons.api";
    final static String api_version = "1.0";

    public static String requestObjData(String command) throws HorizonsApiException {
        if (! command.endsWith(";")) {
            command += ";";
        }

        InputStream stream = null;
        HttpURLConnection connection = null;
        JSONObject data = null;
        int status = 0;

        try {
            URL url = new URL(
                base_url
                + "?format=json&OBJ_DATA='YES'&MAKE_EPHEM='NO'&COMMAND='"
                + URLEncoder.encode(command, "ASCII")
                + "'");

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try {
                connection.connect();
                stream = connection.getInputStream();
                status = connection.getResponseCode();
            }
            catch (IOException e) {
                status = connection.getResponseCode();
                // If we got an error stream, carry on with that, otherwise
                // re-raise this exception.
                stream = connection.getErrorStream();
                if (stream == null) {
                    throw e;
                }
                // Make sure we have a bad status recorded.
                if (status == HttpURLConnection.HTTP_OK) {
                    status = HttpURLConnection.HTTP_BAD_REQUEST;
                }
            }

            data = new JSONObject(new JSONTokener(stream));
        }
        catch (UnsupportedEncodingException e) {
            throw new HorizonsApiException("URL character encoding not supported");
        }
        catch (MalformedURLException e) {
            throw new HorizonsApiException("Malformed URL");
        }
        catch (IOException e) {
            throw new HorizonsApiException("IO exception: " + e.getMessage());
        }
        catch (JSONException e) {
            throw new HorizonsApiException(
                "Could not decode Horizons JSON response: " + e.getMessage());
        }
        finally {
            if (stream != null) {
                try {
                    stream.close();
                }
                catch (IOException e) {
                }
            }

            if (connection != null) {
                connection.disconnect();
            }
        }

        // Double-check data not null (should not happen due to above try block).
        if (data != null) {
            try {
                // Is there a signature? If so, check the version is what we expect.
                if (data.has("signature")) {
                    JSONObject signature = data.getJSONObject("signature");
                    String version = signature.getString("version");
                    if (! api_version.equals(version)) {
                        System.err.println("Unexpected Horizons API version: " + version);
                    }
                }

                if (status == HttpURLConnection.HTTP_OK) {
                    String message = null;
                    if (data.has("error")) {
                        message = data.getString("error");
                    }
                    else if (data.has("message")) {
                        message = data.getString("message");
                    }
                    if (message != null) {
                        System.err.println("Warning message from Horizons API: " + message);
                    }

                    return data.getString("result");
                }

                String message = null;
                if (data.has("error")) {
                    message = data.getString("error");
                }
                else if (data.has("message")) {
                    message = data.getString("message");
                }
                if (message != null) {
                    throw new HorizonsApiException("Error from Horizons API: " + message);
                }

                throw new HorizonsApiException("Error from Horizons API, status " + status);

            }
            catch (JSONException e) {
                throw new HorizonsApiException(
                    "Error processing Horizons JSON response: " + e.getMessage());
            }
        }

        throw new HorizonsApiException("Horizons JSON response is null");
    }

    public static class HorizonsApiException extends Exception {
        public HorizonsApiException(String message) {
            super(message);
        }
    }
}
