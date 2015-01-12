/*
 * Copyright (C) 2003-2012 Science and Technology Facilities Council.
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

package ot.ukirt.iter.editor;

import java.util.List;

import gemini.sp.iter.IterConfigItem;
import gemini.sp.iter.SpIterConfigBase;

/**
 * @author r.kackley@jach.hawaii.edu
 */
public class EdIterUISTSpecIFU extends EdIterUKIRTGeneric {

    /**
     * This method is called when the value of the selected step and attribute
     * is changed.
     *
     * Overrides the method in EdIterGenericConfig so that we can update
     * the entire row when a single cell changes. This is required for
     * the UIST Spec/IFU iterator because the exposure time depends on
     * the source magnitude.
     */
    public void cellValueChanged(String newVal, boolean finishedEditing) {
        // Get the selected cell's coordinates
        int[] coord = _iterTab.getSelectedCoordinates();
        if ((coord[0] == -1) || (coord[1] == -1)) {
            return;
        }

        int colIndex = coord[0];
        int rowIndex = coord[1];

        // Figure out the IterConfigItem that goes with the selected cell
        IterConfigItem ici = _getConfigItem(colIndex);
        if (ici == null) {
            throw new RuntimeException(
                    "couldn't find the IterConfigItem associated with column: "
                            + colIndex);
        }

        ((SpIterConfigBase) _spItem).setConfigStep(ici.attribute, newVal,
                rowIndex);

        // Update the entire row
        SpIterConfigBase icb = (SpIterConfigBase) _spItem;
        List<String> l = icb.getConfigAttribs();
        int index = 0;

        for (int i = 0; i < l.size(); ++i) {
            String attrib = l.get(i);

            if (isUserEditable(attrib)) {
                List<String> vals = icb.getConfigSteps(attrib);
                _iterTab.setCell(vals.get(rowIndex), index, rowIndex);
                ++index;
            }
        }

        ++rowIndex;

        if ((finishedEditing) && (rowIndex < _iterTab.getRowCount())) {
            // Move to the next cell down in the column
            _iterTab.selectCell(colIndex, rowIndex);
            _iterTab.focusAtCell(colIndex, rowIndex);
        }
    }
}
