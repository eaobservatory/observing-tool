/*
 * Copyright 1999 United Kingdom Astronomy Technology Centre, an
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

package ot.ukirt.inst.editor;

import gemini.sp.SpTreeMan;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import jsky.app.ot.gui.KeyPressWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.gui.TableWidgetExt;
import jsky.app.ot.gui.TableWidgetWatcher;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;
import ot.util.DialogUtil;

import jsky.app.ot.editor.OtItemEditor;
import orac.ukirt.inst.SpDRRecipe;
import orac.util.LookUpTable;

import java.awt.event.KeyEvent;
import java.awt.CardLayout;
import java.util.Vector;

// MFO: Preliminary. Get rid of GUI stuff inside this class.
import javax.swing.JComponent;

/**
 * This is the editor for the DRRecipe item.
 */
public final class EdDRRecipe extends OtItemEditor implements KeyPressWatcher,
        TextBoxWidgetWatcher, TableWidgetWatcher {
    private SpDRRecipe _spDRRecipe;
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
        _description = "Enter the Data Reduction recipe to be used for"
                + " each observation type";
        _resizable = true;
    }

    /**
     * This method initializes the widgets in the presentation to reflect the
     * current values of the items attributes.
     *
     * Don't do anything in init at the moment.  The initialisation requires
     * a reference to the instrument component. This isn't known until after
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
        CommandButtonWidgetExt cbw = null;
        CheckBoxWidgetExt ckbw = null;

        SpInstObsComp inst = SpTreeMan.findInstrument(_spDRRecipe);

        // MFO: "empty" is hard-wired in DRRecipeGUI (as constraint strings
        // of the respective panels managed my the CardLayout of DRRecipeGUI).
        // Might not be elegant but is done in a similar way with instrument
        // specific panels (see below).
        ((CardLayout) (_w.getLayout())).show(_w, "empty");

        try {
            _instStr = inst.type().getReadable();
            System.out.println("Inst is " + _instStr);

            // MFO: inst.type().getReadable() is hard-wired in DRRecipeGUI
            // (as constraint strings of the respective panels managed my
            // the CardLayout of DRRecipeGUI).  Might not be elegant but
            // has always been hard-wired in a similar way.
            ((CardLayout) (_w.getLayout())).show(_w, _instStr.toLowerCase());

        } catch (NullPointerException ex) {
            DialogUtil.error(_w,
                    "Can't identify instrument: probably none in scope?");
            return;
        }

        // The recipes
        TextBoxWidgetExt rtbw;
        rtbw = (TextBoxWidgetExt) getWidget(_instStr, "biasRecipe");

        try {
            rtbw.setEditable(false);

            // button to set the recipe
            cbw = (CommandButtonWidgetExt) getWidget(_instStr, "biasSet");

            cbw.addWatcher(new CommandButtonWidgetWatcher() {
                public void commandButtonAction(CommandButtonWidgetExt cbw) {
                    // Set the selected table value
                    _spDRRecipe.setBiasRecipeName(_currentRecipeSelected);

                    TextBoxWidgetExt tbwe = (TextBoxWidgetExt) getWidget(
                            _instStr, "biasRecipe");
                    tbwe.setText(_currentRecipeSelected);
                    _disableRecipeEntry(true);
                }
            });

            // check box for group membership
            ckbw = (CheckBoxWidgetExt) getWidget(_instStr, "biasInGroup");
            ckbw.addWatcher(new CheckBoxWidgetWatcher() {
                public void checkBoxAction(CheckBoxWidgetExt ckbw) {
                    // Set the value
                    _spDRRecipe.setBiasInGroup(ckbw.getBooleanValue());
                }
            });
        } catch (NullPointerException ex) {
        }

        // FOCUS
        rtbw = (TextBoxWidgetExt) getWidget(_instStr, "focusRecipe");

        try {
            rtbw.setEditable(false);

            // button to set the dark recipe
            cbw = (CommandButtonWidgetExt) getWidget(_instStr, "focusSet");
            cbw.addWatcher(new CommandButtonWidgetWatcher() {
                public void commandButtonAction(CommandButtonWidgetExt cbw) {
                    // Set the selected table value
                    _spDRRecipe.setFocusRecipeName(_currentRecipeSelected);

                    TextBoxWidgetExt tbwe = (TextBoxWidgetExt) getWidget(
                            _instStr, "focusRecipe");
                    tbwe.setText(_currentRecipeSelected);
                    _disableRecipeEntry(true);
                }
            });

            // check box for group membership
            ckbw = (CheckBoxWidgetExt) getWidget(_instStr, "focusInGroup");
            ckbw.addWatcher(new CheckBoxWidgetWatcher() {
                public void checkBoxAction(CheckBoxWidgetExt ckbw) {
                    // Set the value
                    _spDRRecipe.setFocusInGroup(ckbw.getBooleanValue());
                }
            });
        } catch (NullPointerException ex) {
        }

        rtbw = (TextBoxWidgetExt) getWidget(_instStr, "darkRecipe");

        try {
            rtbw.setEditable(false);

            // button to set the dark recipe
            cbw = (CommandButtonWidgetExt) getWidget(_instStr, "darkSet");
            cbw.addWatcher(new CommandButtonWidgetWatcher() {
                public void commandButtonAction(CommandButtonWidgetExt cbw) {
                    // Set the selected table value
                    _spDRRecipe.setDarkRecipeName(_currentRecipeSelected);

                    TextBoxWidgetExt tbwe = (TextBoxWidgetExt) getWidget(
                            _instStr, "darkRecipe");
                    tbwe.setText(_currentRecipeSelected);
                    _disableRecipeEntry(true);
                }
            });

            // check box for group membership
            ckbw = (CheckBoxWidgetExt) getWidget(_instStr, "darkInGroup");
            ckbw.addWatcher(new CheckBoxWidgetWatcher() {
                public void checkBoxAction(CheckBoxWidgetExt ckbw) {
                    // Set the value
                    _spDRRecipe.setDarkInGroup(ckbw.getBooleanValue());
                }
            });
        } catch (NullPointerException ex) {
        }

        rtbw = (TextBoxWidgetExt) getWidget(_instStr, "flatRecipe");

        try {
            rtbw.setEditable(false);

            // button to set the dark recipe
            cbw = (CommandButtonWidgetExt) getWidget(_instStr, "flatSet");
            cbw.addWatcher(new CommandButtonWidgetWatcher() {
                public void commandButtonAction(CommandButtonWidgetExt cbw) {
                    // Set the selected table value
                    _spDRRecipe.setFlatRecipeName(_currentRecipeSelected);

                    TextBoxWidgetExt tbwe = (TextBoxWidgetExt) getWidget(
                            _instStr, "flatRecipe");
                    tbwe.setText(_currentRecipeSelected);
                    _disableRecipeEntry(true);
                }
            });

            // check box for group membership
            ckbw = (CheckBoxWidgetExt) getWidget(_instStr, "flatInGroup");
            ckbw.addWatcher(new CheckBoxWidgetWatcher() {
                public void checkBoxAction(CheckBoxWidgetExt ckbw) {
                    // Set the value
                    _spDRRecipe.setFlatInGroup(ckbw.getBooleanValue());
                }
            });
        } catch (NullPointerException ex) {
        }

        rtbw = (TextBoxWidgetExt) getWidget(_instStr, "arcRecipe");

        try {
            rtbw.setEditable(false);

            // button to set the dark recipe
            cbw = (CommandButtonWidgetExt) getWidget(_instStr, "arcSet");
            cbw.addWatcher(new CommandButtonWidgetWatcher() {
                public void commandButtonAction(CommandButtonWidgetExt cbw) {
                    // Set the selected table value
                    _spDRRecipe.setArcRecipeName(_currentRecipeSelected);

                    TextBoxWidgetExt tbwe = (TextBoxWidgetExt) getWidget(
                            _instStr, "arcRecipe");
                    tbwe.setText(_currentRecipeSelected);
                    _disableRecipeEntry(true);
                }
            });

            // check box for group membership
            ckbw = (CheckBoxWidgetExt) getWidget(_instStr, "arcInGroup");
            ckbw.addWatcher(new CheckBoxWidgetWatcher() {
                public void checkBoxAction(CheckBoxWidgetExt ckbw) {
                    // Set the value
                    _spDRRecipe.setArcInGroup(ckbw.getBooleanValue());
                }
            });
        } catch (NullPointerException ex) {
        }

        rtbw = (TextBoxWidgetExt) getWidget(_instStr, "skyRecipe");

        rtbw.setEditable(false);

        cbw = (CommandButtonWidgetExt) getWidget(_instStr, "skySet");
        cbw.addWatcher(new CommandButtonWidgetWatcher() {
            public void commandButtonAction(CommandButtonWidgetExt cbw) {
                // Set the selected table value
                _spDRRecipe.setSkyRecipeName(_currentRecipeSelected);

                TextBoxWidgetExt tbwe = (TextBoxWidgetExt) getWidget(_instStr,
                        "skyRecipe");
                tbwe.setText(_currentRecipeSelected);
                _disableRecipeEntry(true);
            }
        });

        // check box for group membership
        ckbw = (CheckBoxWidgetExt) getWidget(_instStr, "skyInGroup");
        ckbw.addWatcher(new CheckBoxWidgetWatcher() {
            public void checkBoxAction(CheckBoxWidgetExt ckbw) {
                // Set the value
                _spDRRecipe.setSkyInGroup(ckbw.getBooleanValue());
            }
        });

        rtbw = (TextBoxWidgetExt) getWidget(_instStr, "objectRecipe");
        // Disable it so it the user cannot use it.
        // _disableRecipeEntry(true);
        rtbw.setEditable(false);
        cbw = (CommandButtonWidgetExt) getWidget(_instStr, "objectSet");
        cbw.addWatcher(new CommandButtonWidgetWatcher() {
            public void commandButtonAction(CommandButtonWidgetExt cbw) {
                // Set the selected table value
                _spDRRecipe.setObjectRecipeName(_currentRecipeSelected);
                _spDRRecipe.setTitleAttr(_currentRecipeSelected);

                TextBoxWidgetExt tbwe = (TextBoxWidgetExt) getWidget(_instStr,
                        "objectRecipe");
                tbwe.setText(_currentRecipeSelected);
                _disableRecipeEntry(true);
            }
        });

        // check box for group membership
        ckbw = (CheckBoxWidgetExt) getWidget(_instStr, "objectInGroup");
        ckbw.addWatcher(new CheckBoxWidgetWatcher() {
            public void checkBoxAction(CheckBoxWidgetExt ckbw) {
                // Set the value
                _spDRRecipe.setObjectInGroup(ckbw.getBooleanValue());
            }
        });

        // The table of possible recipes
        TableWidgetExt twe;
        twe = (TableWidgetExt) getWidget(_instStr, "recipeTable");
        twe.setColumnHeaders(new String[]{"Recipe Name", "Description"});
        twe.addWatcher(this);

        // button to reset the recipe to default
        cbw = (CommandButtonWidgetExt) getWidget(_instStr, "defaultName");
        cbw.addWatcher(new CommandButtonWidgetWatcher() {
            public void commandButtonAction(CommandButtonWidgetExt cbw) {
                _spDRRecipe.useDefaults(_instStr);
                _currentRecipeSelected = "QUICK_LOOK";
                _updateWidgets();
            }
        });
    }

    private void _disableRecipeEntry(boolean tf) {
    }

    /**
     * Initialize the Recipe table widget according to the selected
     * recipe category.
     */
    private void _showRecipeType(LookUpTable recipes) {
        Vector<String>[] rowsV = recipes.getAsVectorArray();
        TableWidgetExt tw =
                (TableWidgetExt) getWidget(_instStr, "recipeTable");
        tw.setRows(rowsV);
    }

    /**
     * Update the recipe choice related widgets.
     */
    private void _updateRecipeWidgets() {
        String recipe = null;
        String instStr;

        // get instrument  so we can determine which recipe selections to show
        SpInstObsComp inst = SpTreeMan.findInstrument(_spDRRecipe);

        // MFO: "empty" is hard-wired in DRRecipeGUI (as constraint strings of the
        // respective panels managed my the CardLayout of DRRecipeGUI).
        // Might not be elegant but is done in a similar way with instrument
        // specific panels (see below).
        ((CardLayout) (_w.getLayout())).show(_w, "empty");

        try {
            instStr = inst.type().getReadable();

            // MFO: inst.type().getReadable() is hard-wired in DRRecipeGUI
            // (as constraint strings of the respective panels managed my
            // the CardLayout of DRRecipeGUI).  Might not be elegant but has
            // always been hard-wired in a similar way.
            ((CardLayout) (_w.getLayout())).show(_w, instStr.toLowerCase());

        } catch (NullPointerException ex) {
            System.out.println("No instrument found in scope");
            return;
        }

        // First fill in the text box.
        TextBoxWidgetExt tbwe;
        CheckBoxWidgetExt cbwe;

        tbwe = (TextBoxWidgetExt) getWidget(_instStr, "biasRecipe");
        try {
            recipe = _spDRRecipe.getBiasRecipeName();
            tbwe.setValue(recipe);
            cbwe = (CheckBoxWidgetExt) getWidget(_instStr, "biasInGroup");
            cbwe.setValue(_spDRRecipe.getFocusInGroup());
        } catch (NullPointerException ex) {
        }

        tbwe = (TextBoxWidgetExt) getWidget(_instStr, "focusRecipe");
        try {
            recipe = _spDRRecipe.getFocusRecipeName();
            tbwe.setValue(recipe);
            cbwe = (CheckBoxWidgetExt) getWidget(_instStr, "focusInGroup");
            cbwe.setValue(_spDRRecipe.getFocusInGroup());
        } catch (NullPointerException ex) {
        }

        tbwe = (TextBoxWidgetExt) getWidget(_instStr, "darkRecipe");
        try {
            recipe = _spDRRecipe.getDarkRecipeName();
            tbwe.setValue(recipe);
            cbwe = (CheckBoxWidgetExt) getWidget(_instStr, "darkInGroup");
            cbwe.setValue(_spDRRecipe.getDarkInGroup());
        } catch (NullPointerException ex) {
        }

        tbwe = (TextBoxWidgetExt) getWidget(_instStr, "flatRecipe");
        try {
            recipe = _spDRRecipe.getFlatRecipeName();
            tbwe.setValue(recipe);
            cbwe = (CheckBoxWidgetExt) getWidget(_instStr, "flatInGroup");
            cbwe.setValue(_spDRRecipe.getFlatInGroup());
        } catch (NullPointerException ex) {
        }

        tbwe = (TextBoxWidgetExt) getWidget(_instStr, "arcRecipe");
        try {
            recipe = _spDRRecipe.getArcRecipeName();
            tbwe.setValue(recipe);
            cbwe = (CheckBoxWidgetExt) getWidget(_instStr, "arcInGroup");
            cbwe.setValue(_spDRRecipe.getArcInGroup());
        } catch (NullPointerException ex) {
        }

        tbwe = (TextBoxWidgetExt) getWidget(_instStr, "skyRecipe");
        try {
            recipe = _spDRRecipe.getSkyRecipeName();
            tbwe.setValue(recipe);
            cbwe = (CheckBoxWidgetExt) getWidget(_instStr, "skyInGroup");
            cbwe.setValue(_spDRRecipe.getSkyInGroup());
        } catch (NullPointerException ex) {
        }

        tbwe = (TextBoxWidgetExt) getWidget(_instStr, "objectRecipe");
        try {
            recipe = _spDRRecipe.getObjectRecipeName();
            tbwe.setValue(recipe);
            cbwe = (CheckBoxWidgetExt) getWidget(_instStr, "objectInGroup");
            cbwe.setValue(_spDRRecipe.getObjectInGroup());
        } catch (NullPointerException ex) {
        }

        // See which type of recipe the selected recipe is, if any.
        // Get the instrument and display the relevant options.
        // Then look for the recipe in these options. If its there highlight
        // it.

        // What I really need to do is introduce imaging/spec capabilities into
        // instruments which I can then get.  Imager-spectrometers will be
        // a special case

        LookUpTable rarray = null;

        if (instStr.equalsIgnoreCase("UFTI")) {
            rarray = SpDRRecipe.UFTI;
        } else if (instStr.equalsIgnoreCase("IRCAM3")) {
            rarray = SpDRRecipe.IRCAM3;
        } else if (instStr.equalsIgnoreCase("CGS4")) {
            rarray = SpDRRecipe.CGS4;
        } else if (instStr.equalsIgnoreCase("Michelle")) {
            rarray = SpDRRecipe.MICHELLE;
        } else if (instStr.equalsIgnoreCase("UIST")) {
            rarray = SpDRRecipe.UIST;
        } else if (instStr.equalsIgnoreCase("WFCAM")) {
            rarray = SpDRRecipe.WFCAM;
        }

        // Show the correct recipes, and select the option widget for the type
        _showRecipeType(rarray);

    }

    /**
     * Override setup to store away a reference to the SpDRRecipe item.
     *
     * Also initialise the widgets here.
     */
    public void setup(SpItem spItem) {
        _spDRRecipe = (SpDRRecipe) spItem;
        _initInstWidgets();
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
        // a text box widget, Currently the user specified recipe is
        // the only such widget.
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

        // Allow for blank lines and headings.  The latter is defined to
        // contain at least two lowercase letters or any space (testing for
        // a colon might also be useful).  Merely substitute the default
        // recipe.
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

    /**
     * @see #getWidget(java.lang.String)
     */
    protected JComponent getWidget(String instrument, String widgetName) {
        return getWidget(instrument.toLowerCase() + "_" + widgetName);
    }

    /**
     * Helper method.
     *
     * In EdDRRecipe the widgets of the associated GUI class cannot as easily
     * be referred to directly as in the subclasses of EdCompInstBase because
     * the widget names in the freebongo version often comprised an instrument
     * part and a paramater part (e.g. _instStr, "flatInGroup"). In freebongo
     * something like this is possible because one gets hold of a widget by
     * calling the getWidget(String) method on the presentation. If this
     * method is called with a String that refers to a widget that does not
     * exist (e.g. "IRCAM3.flatInGroup") then a NullPointerException occurres
     * which can be called and ignored. This was used in the freebongo
     * version of this class to handle every widget for every instrument.
     * Whenever a widget was addressed in the context of an instrument whose
     * GUI panel does not have this widget that a NullPointerException occurrs
     * and is ignored. To use a similar approach in the swing version of the
     * OT this helper method getWidget has been introduced in the EdDRRecipe
     * class.
     *
     * ('_' are used instead of '.' to separate instrument name from widget
     * name.)
     *
     * @return widget in DRRecipeGUI or null if specified widget does not
     *         exist.
     */
    protected JComponent getWidget(String widgetName) {
        try {
            return (JComponent)
                    (_w.getClass().getDeclaredField(widgetName).get(_w));
        } catch (NoSuchFieldException e) {
            if ((System.getProperty("DEBUG") != null)
                    && System.getProperty("DEBUG").equalsIgnoreCase("ON"))
                System.out.println("Could not find widget / component \""
                        + widgetName + "\".");

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
