/*
 * Copyright 1999 United Kingdom Astronomy Technology Centre, an
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

package orac.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import gemini.util.ObservingToolUtilities;

/**
 * This implements the reading of instrument config files.
 */
public class InstCfgReader {
    private BufferedReader cfgFile;

    /**
     * The constructor
     */
    public InstCfgReader(URL baseURL, String cfgFilename) {
        URL url = null;

        try {
            url = new URL(baseURL, cfgFilename);
        } catch (MalformedURLException ex) {
            System.out.println(
                    "Problem constructing the inst. config file URL: " + ex);
        }

        try {
            cfgFile = new BufferedReader(
                    new InputStreamReader(url.openStream()));
        } catch (IOException ex) {
            System.out.println("Problem opening the inst. config file: " + ex);
        }

    }

    public InstCfgReader(String cfgFilename) {
        try {
            URL url = ObservingToolUtilities.resourceURL(cfgFilename);
            InputStream is = url.openStream();
            cfgFile = new BufferedReader(new InputStreamReader(is));
        } catch (MalformedURLException ex) {
            System.out.println("Problem opening the inst. config file: " + ex);
        } catch (IOException ioe) {
            System.out.println("Problem opening the inst. config file: " + ioe);
        }
    }

    /**
     * Return the next block as a string.
     *
     * A "block" is defined as a section starting with a non-blank,
     * non-comment descriptor and concluding with a comment line.
     * This implies that comment lines are not allowed within "blocks".
     */
    public String readBlock() throws IOException {
        String line;
        String block = "";

        // while there are more lines
        while ((line = cfgFile.readLine()) != null) {
            // ignore blanks
            if (line.length() == 0) {
                continue;
            }

            // ignore comments if not yet started block
            if (line.startsWith("#") && block == "") {
                continue;
            }

            // if we have started block and its a comment then exit while
            if (line.startsWith("#") && block != "") {
                break;
            }

            // append line to block
            block = block + line;
        }

        if (block == "") {
            return null;
        }

        return block;
    }

    public void close() {
        if (cfgFile != null) {
            try {
                cfgFile.close();
            } catch (IOException e) {
            }

            cfgFile = null;
        }
    }
}
