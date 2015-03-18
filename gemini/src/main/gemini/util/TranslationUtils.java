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

import java.util.Vector;
import java.util.Enumeration;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.sp.SpItem;

public class TranslationUtils {
    public static void recurse(Enumeration<SpItem> e, Vector<String> v)
            throws SpTranslationNotSupportedException {
        if (e == null) {
            throw new SpTranslationNotSupportedException();
        }
        if (v == null) {
            throw new SpTranslationNotSupportedException();
        }

        SpTranslatable translatable = null;
        SpTranslatable previous = null;

        while (e.hasMoreElements()) {
            SpItem child = e.nextElement();

            if (child instanceof SpTranslatable) {
                translatable = (SpTranslatable) child;

                if (!translatable.equals(previous)) {
                    if (previous != null) {
                        previous.translateEpilog(v);
                    }

                    previous = translatable;

                    translatable.translateProlog(v);
                }

                translatable.translate(v);
            }
        }

        if (translatable != null) {
            translatable.translateEpilog(v);
        }
    }

    /**
     * Copy the first loadConfig instruction to the end of the exec.
     *
     * @param v the exec to alter
     * @param removeOriginal true to remove the original loadConfig, false
     *                       to retain the original entry and add a new one.
     */
    public static void copyFirstLoadConfig(Vector<String> v, boolean removeOriginal) {
        for (int i = v.size() - 1; i >= 0; i--) {
            String defCon = v.get(i);

            if (defCon.matches("loadConfig .*_1")) {
                if (removeOriginal) {
                    v.remove(i);
                }
                v.add(defCon);
                break;
            }
        }
    }
}
