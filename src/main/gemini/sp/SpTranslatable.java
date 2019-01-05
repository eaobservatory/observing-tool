/*
 * Copyright (C) 2003-2008 Science and Technology Facilities Council.
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

package gemini.sp;

import java.util.Vector;

public interface SpTranslatable {
    /**
     * Classes that implement this method must provide a translate method.
     *
     * Used to convert XML into sequences
     */
    public void translate(Vector<String> sequence)
            throws SpTranslationNotSupportedException;

    /**
     * Called prior to main translation for iterators etc.
     *
     * This is useful for not repeating setup mid-observation.
     * Although, due to SpTranslatable being an interface it needs to be
     * implemented, it is expected that these methods will remain empty in
     * normal translation.
     *
     * @param sequence
     * @throws SpTranslationNotSupportedException
     */
    public void translateProlog(Vector<String> sequence)
            throws SpTranslationNotSupportedException;

    /**
     * Called after main translation for iterators etc.
     *
     * This is useful for not repeating tear-down mid-observation.
     * Although, due to SpTranslatable being an interface it needs to be
     * implemented, it is expected that these methods will remain empty in
     * normal translation.
     *
     * @param sequence
     * @throws SpTranslationNotSupportedException
     */
    public void translateEpilog(Vector<String> sequence)
            throws SpTranslationNotSupportedException;

}
