/*
 * Copyright (C) 2007-2009 Science and Technology Facilities Council.
 * All Rights Reserved.
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

package gemini.util;

import java.io.BufferedReader;

import java.net.URL;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Version {

    private static Version versionObject = new Version();

    private Version() {
        setVersion();
    }

    public static Version getInstance() {
        return versionObject;
    }

    public String getVersion() {
        return System.getProperty("ot.version");
    }

    public String getFullVersion() {
        return System.getProperty("ot.fullversion");
    }

    private void setVersion() {
        URL url = ObservingToolUtilities.resourceURL(
                "versionFile", "ot.cfgdir");
        String version = "unknown";
        String fullVersion = version;

        try {
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            fullVersion = br.readLine().trim();

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fullVersion.matches("\\d{8} \\[\\w*:?\\w*\\]")) {
            version = fullVersion.substring(0, 8);
        }

        System.setProperty("ot.version", version);
        System.setProperty("ot.fullversion", fullVersion);
    }
}
