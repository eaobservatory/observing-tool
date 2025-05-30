/*
 * Copyright (C) 2008-2010 Science and Technology Facilities Council.
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

package orac.jcmt.iter;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

@SuppressWarnings("serial")
public class SpIterArrayTestObs extends SpIterJCMTObs {

    public static final SpType SP_TYPE = SpType.create(
            SpType.ITERATOR_COMPONENT_TYPE, "arrayTestObs", "Array Test");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpIterArrayTestObs());
    }

    /**
     * Default constructor.
     */
    public SpIterArrayTestObs() {
        super(SP_TYPE);
    }

    public boolean doesNotNeedTarget() {
        return true;
    }

    public double getElapsedTime() {
        return 0.0;
    }
}
