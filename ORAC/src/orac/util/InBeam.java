/*
 * Copyright (C) 2013 Science and Technology Facilities Council.
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

package orac.util;

import gemini.sp.SpAvTable;

/**
 * Utility class to help manage the in_beam attribute
 * of a SpAvTable.
 */
public class InBeam {
        /**
         * Name of the XML element controlling what is in the beam.
         */
        private static final String IN_BEAM = "in_beam";

        /**
         * Determine whether an arbitrary component
         * identified by name is in the beam.
         */
        public static boolean isInBeam(SpAvTable _avTable, String component) {
            if (! _avTable.exists(IN_BEAM)) {
                    return false;
            }

            for (String x: _avTable.get(IN_BEAM).split("\\s")) {
                    if (x.equalsIgnoreCase(component)) {
                        return true;
                    }
            }

            return false;
        }

        /**
         * Set whether an arbitrary component identified
         * by name is in the beam.
         */
        public static void setInBeam(SpAvTable _avTable, String component, boolean in_beam) {
                StringBuilder list = new StringBuilder();
                boolean found = false;

                if (_avTable.exists(IN_BEAM)) {
                        for (String x: _avTable.get(IN_BEAM).split("\\s")) {
                            if (x.equalsIgnoreCase(component)) {
                                found = true;

                                if (! in_beam)  {
                                        continue;
                                }
                            }

                            if (list.length() > 0) {
                                    list.append(" ");
                            }

                            list.append(x);
                        }
                }

                if (in_beam && ! found) {
                        if (list.length() > 0) {
                                list.append(" ");
                        }

                        list.append(component);
                }

                if (list.length() > 0) {
                        _avTable.set(IN_BEAM, list.toString());
                }
                else {
                        if (_avTable.exists(IN_BEAM)) {
                                _avTable.rm(IN_BEAM);
                        }
                }
        }
}
