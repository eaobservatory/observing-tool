/*
 * Copyright (C) 2006 Science and Technology Facilities Council.
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

/*
 * Added so that we can use the compiler to check strings
 */
public interface SpTranslationConstants {
    String darkString = "set DARK";

    String biasString = "set BIAS";

    String focusString = "set FOCUS";

    String domeString = "set DOMEFLAT";

    String objectString = "set OBJECT";

    String skyString = "set SKY";

    String skyflatString = "set SKYFLAT";

    String breakString = "break";

    String[] sets = {darkString, biasString, focusString, domeString,
            objectString, skyString, skyflatString};
}
