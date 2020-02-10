/*
 * Copyright (C) 2020 East Asian Observatory
 * All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful,but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc.,51 Franklin
 * Street, Fifth Floor, Boston, MA  02110-1301, USA
 */

package orac.jcmt.iter;

import gemini.sp.SpFactory;
import gemini.sp.SpMSB;
import gemini.sp.SpType;

public class SpIterRawXmlObs extends SpIterJCMTObs {
    private final String ATTR_OCS_CONFIG = "ocsconfig";

    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "rawXmlObs", "Raw XML");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterRawXmlObs());
    }

    /**
     * Default constructor.
     */
    public SpIterRawXmlObs() {
        super(SP_TYPE);

        _avTable.noNotifySet(ATTR_OCS_CONFIG, "", 0);
        _avTable.noNotifySet(SpMSB.ATTR_ELAPSED_TIME, "0.0", 0);
    }

    public double getElapsedTime() {
        return _avTable.getDouble(SpMSB.ATTR_ELAPSED_TIME, 0.0);
    }

    public void setElapsedTime(double value) {
        _avTable.set(SpMSB.ATTR_ELAPSED_TIME, value);
    }

    public String getOcsConfig() {
        return _avTable.get(ATTR_OCS_CONFIG);
    }

    public void setOcsConfig(String value) {
        _avTable.set(ATTR_OCS_CONFIG, value);
    }
}
