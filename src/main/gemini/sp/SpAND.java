/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
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

package gemini.sp;

import java.util.Enumeration;

/**
 * OMP class.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SpAND extends SpObsContextItem {
    /**
     * Default constructor.
     */
    protected SpAND() {
        super(SpType.AND_FOLDER);
    }

    /**
     * Calculates the duration of this AND folder.
     */
    public double getTotalTime() {
        double elapsedTime = 0.0;
        Enumeration<SpItem> children = children();
        SpItem spItem = null;

        while (children.hasMoreElements()) {
            spItem = children.nextElement();

            if (spItem instanceof SpMSB) {
                SpMSB msb = (SpMSB) spItem;

                if (msb.getNumberRemaining() > 0.0) {
                    elapsedTime += msb.getTotalTime()
                            * msb.getNumberRemaining();
                }

            } else if (spItem instanceof SpSurveyContainer) {
                elapsedTime += ((SpSurveyContainer) spItem).getTotalTime();
            }
        }

        return elapsedTime;
    }

    /**
     * Calculates the duration of this AND folder.
     */
    public double getElapsedTime() {
        double elapsedTime = 0.0;
        Enumeration<SpItem> children = children();
        SpItem spItem = null;

        while (children.hasMoreElements()) {
            spItem = children.nextElement();

            if (spItem instanceof SpMSB) {
                SpMSB msb = (SpMSB) spItem;

                if (msb.getNumberRemaining() > 0.0) {
                    elapsedTime += (msb.getElapsedTime() * msb
                            .getNumberRemaining());
                }

            } else if (spItem instanceof SpSurveyContainer) {
                elapsedTime += ((SpSurveyContainer) spItem).getElapsedTime();
            }
        }

        return elapsedTime;
    }
}
