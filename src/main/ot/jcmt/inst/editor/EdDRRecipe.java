/*
 * Copyright 1999-2002 United Kingdom Astronomy Technology Centre, an
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

package ot.jcmt.inst.editor;

import gemini.sp.SpTreeMan;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.inst.SpInstSCUBA2;
import jsky.app.ot.gui.KeyPressWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.gui.TableWidgetExt;
import jsky.app.ot.gui.TableWidgetWatcher;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;

import jsky.app.ot.editor.OtItemEditor;
import orac.jcmt.inst.SpDRRecipe;
import orac.util.LookUpTable;

import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.NoSuchElementException;
import java.util.Vector;

// MFO: Preliminary. Get rid of GUI stuff inside this class.
import javax.swing.JComponent;

/**
 * This is the editor for the DRRecipe item.
 */
public final class EdDRRecipe extends OtItemEditor implements KeyPressWatcher,
        TextBoxWidgetWatcher, TableWidgetWatcher, DropDownListBoxWidgetWatcher,
        ActionListener {
    private static final String INST_STR_SCUBA2 = "scuba2";
    private static final String INST_STR_HETERODYNE = "heterodyne";
    private SpDRRecipe _spDRRecipe;
    private SpInstObsComp _inst;
    private String _currentRecipeSelected;
    private String _instStr;
    private DRRecipeGUI _w; // the GUI layout

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdDRRecipe() {
        _title = "DR Recipe";
        _presSource = _w = new DRRecipeGUI();
        _description = "Enter the Data Reduction recipe to be used";
                        // for each observation type";
        _resizable = true;
    }

    /**
     * This method initializes the widgets in the presentation to reflect
     * the current values of the items attributes.
     *
     * Don't do anything in init at the moment. The initialisation requires
     * a reference to the instrument component.  This isn't known until after
     * setup is invoked, which is after init.  Therefore do the initialisation
     * as part of setup (see initInstWidgets).
     */
    protected void _init() {
    }

    /**
     * This method initializes the instrument speific widgets in the
     * presentation to reflect the current values of the items attributes
     * and set the watchers.
     */
    protected void _initInstWidgets() {
        _w.reset();

        // The recipes

        for (final String type: _spDRRecipe.getAvailableTypes(_instStr)) {
            _w.setEnabled(type, true);

            CommandButtonWidgetExt cbw = _w.getTypeButton(type);

            if (cbw == null) {
                System.out.println(type + " not implemented in the GUI");
                continue;
            }

            cbw.deleteWatchers();

            cbw.addWatcher(new CommandButtonWidgetWatcher() {
                public void commandButtonAction(CommandButtonWidgetExt cbw) {
                    // Set the selected table value
                    _spDRRecipe.setRecipeForType(_currentRecipeSelected, type,
                            _instStr);

                    _w.getTypeRecipe(type).setText(_currentRecipeSelected);
                }
            });
        }

        // The table of possible recipes
        _w.recipeTable.setColumnHeaders(new String[]{"Recipe Name", "Description"});
        _w.recipeTable.addWatcher(this);

        // Get the instrument and display the relevant options.
        _showRecipeType(getInstrumentRecipes());

        // button to reset the recipe to default
        _w.defaultName.deleteWatchers();
        _w.defaultName.addWatcher(new CommandButtonWidgetWatcher() {
            public void commandButtonAction(CommandButtonWidgetExt cbw) {
                _spDRRecipe.setDefaultsForInstrument(_instStr);
                _currentRecipeSelected = null;
                _updateWidgets();
            }
        });
    }

    /**
     * Get the LookUpTable of recipe for the current instrument type.
     */
    private LookUpTable getInstrumentRecipes() {
        LookUpTable rarray = null;

        if (_instStr.equalsIgnoreCase(INST_STR_SCUBA2)) {
            rarray = SpDRRecipe.SCUBA2;
        } else if (_instStr.equalsIgnoreCase(INST_STR_HETERODYNE)) {
            rarray = SpDRRecipe.HETERODYNE;
        } else {
            rarray = new LookUpTable();
        }

        return rarray;
    }

    /**
     * Initialize the Recipe table widget according to the selected recipe
     * category.
     */
    private void _showRecipeType(LookUpTable recipes) {
        if (recipes != null) {
            Vector<String>[] rowsV = recipes.getAsVectorArray();
            _w.recipeTable.setRows(rowsV);
        }
    }

    /**
     * Update the recipe choice related widgets.
     */
    private void _updateRecipeWidgets() {
        String recipe = null;

        // First fill in the text box.
        for (String type: _spDRRecipe.getAvailableTypes(_instStr)) {
            try {
                recipe = _spDRRecipe.getRecipeForType(type);

                if (recipe == null) {
                    recipe = "";
                }

                _w.getTypeRecipe(type).setValue(recipe);

            } catch (NullPointerException ex) {
            }
        }
    }

    /**
     * Override setup to store away a reference to the SpDRRecipe item.
     *
     * Also initialise the widgets Here.
     */
    public void setup(SpItem spItem) {
        _spDRRecipe = (SpDRRecipe) spItem;

        SpInstObsComp tmpInst = SpTreeMan.findInstrument(_spDRRecipe);

        boolean initd = (((_inst == null) && (tmpInst == null))
                || ((tmpInst != null) && tmpInst.equals(_inst)));

        _inst = tmpInst;

        if (_inst instanceof SpInstHeterodyne) {
            _instStr = INST_STR_HETERODYNE;
        } else if (_inst instanceof SpInstSCUBA2) {
            _instStr = INST_STR_SCUBA2;
        } else {
            _instStr = "";
        }

        // The instrument type changed (or was null before), so configure
        // the GUI for the new instrument.
        if (!initd) {
            _initInstWidgets();
        }

        // Check whether the SpDRRecipe's recipes are still valid for the
        // current instrument.  We used to call _spDRRecipe.reset() whenever
        // the instrument changed but this caused
        // recipes to be deleted from science programs which legitimately
        // used more than one instrument -- the "whack-a-mole" fault
        // (20150324.011).  For this check, it's not really relevant
        // what the previous instrument looked at was, so perform it
        // every time.
        try {
            LookUpTable recipes = getInstrumentRecipes();
            for (String type: _spDRRecipe.getAvailableTypes(_instStr)) {
                String recipe = _spDRRecipe.getRecipeForType(type);
                if (recipe != null) {
                    // Try to look up the index of the recipe -- throws
                    // NoSuchElementException if the recipe isn't there.
                    recipes.indexInColumn(recipe, 0);
                }

            }
        } catch (NoSuchElementException e) {
            // If _spDRRecipe contained a recipe which isn't allowed for the
            // instrument, reset it.
            System.out.println("Recipes not appropriate for inst: clearing.");
            _spDRRecipe.reset();
        }

        // Update the recipe widgets to show the selection from the
        // SpDRRecipe.
        _updateRecipeWidgets();

        super.setup(spItem);
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
        _updateRecipeWidgets();
    }

    /**
     * This public methods allows the dialog to cause the widgets of this
     * editor to be updated.
     */
    public void refresh() {
        _updateWidgets();
    }

    /**
     * A key has been pressed in the text box widget.
     *
     * @see KeyPressWatcher
     */
    public void keyPressed(KeyEvent evt) {
    }

    /**
     * Watch changes to the text box.
     *
     * @see TextBoxWidgetWatcher
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
        // This is the watcher entered hen the user types a character in
        // a text box widget, Currently the user specified recipe is the
        // only such widget.
        _currentRecipeSelected = tbwe.getText().trim();
    }

    /**
     * Text box action, ignore.
     *
     * @see TextBoxWidgetWatcher
     */
    public void textBoxAction(TextBoxWidgetExt tbwe) {
    }

    public void tableRowSelected(TableWidgetExt twe, int rowIndex) {
        _currentRecipeSelected = (String) twe.getCell(0, rowIndex);
        String _defaultRecipe = "QUICK_LOOK";

        // Allow for blank lines and headings. The latter is defined to contain
        // at least two lowercase letters or any space (testing for a colon
        // might also be useful). Merely substitute the default recipe.
        if (_currentRecipeSelected.length() == 0) {
            _currentRecipeSelected = _defaultRecipe;

        } else {
            int count = 0;

            for (int i = 0; i < _currentRecipeSelected.length(); i++) {
                if (Character.isWhitespace(_currentRecipeSelected.charAt(i))) {
                    _currentRecipeSelected = _defaultRecipe;
                    break;

                } else if (Character.isLowerCase(
                        _currentRecipeSelected.charAt(i))) {
                    count++;

                    if (count > 1) {
                        _currentRecipeSelected = _defaultRecipe;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Must watch table widget actions as part of the TableWidgetWatcher
     * interface, but don't care about them.
     */
    public void tableAction(TableWidgetExt twe, int colIndex, int rowIndex) {
    }

    public void dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i,
            String val) {
    }

    public void actionPerformed(ActionEvent e) {
    }
}
