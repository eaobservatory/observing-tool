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

package orac.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * File filter for Science Programs in xml format.
 *
 * (*.xml)
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class FileFilterXML extends FileFilter {
    public static final String[] extension = {".xml"};
    public static final String description = "Science Program XML (*.xml)";

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        for (int i = 0; i < extension.length; i++) {
            if (file.getName().endsWith(extension[i])) {
                return true;
            }
        }

        return false;
    }

    public String getDescription() {
        return description;
    }
}
