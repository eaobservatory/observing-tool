/*
 * Copyright (C) 2008 Science and Technology Facilities Council.
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

import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;

public class ObservingToolUtilities {
    private static ClassLoader classLoader =
            ObservingToolUtilities.class.getClassLoader();

    public static URL resourceURL(String path, String property) {
        URL url = null;

        if (path != null && property != null) {
            path = System.getProperty(property.trim()) + path;
            url = resourceURL(path);
        }

        return url;
    }

    public static URL resourceURL(String path) {
        URL url = null;

        if (path != null) {
            url = classLoader.getResource(path);

            if (url == null) {
                try {
                    if (!path.matches("^\\w+://.*")) {
                        url = new File(path).toURI().toURL();
                    } else {
                        url = new URL(path);
                    }
                } catch (MalformedURLException mue) {
                    System.out.println(mue + " : " + path);
                }
            }
        }

        return url;
    }
}
