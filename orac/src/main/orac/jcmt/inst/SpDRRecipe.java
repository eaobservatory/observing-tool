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

package orac.jcmt.inst;

import java.io.IOException;

import orac.util.LookUpTable;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

import gemini.util.CoordSys;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.obsComp.SpDRObsComp;

import java.lang.reflect.Field;
import java.util.TreeMap;

/**
 * This class defines the DRRecipe Observation Component for JCMT.
 *
 * The component allows the specification of an ORACDR recipe.<p>
 *
 * Additionally it is used to specify ACSIS DR information.
 *
 * @author Alan Bridger, UKATC,
 *         Modified for JCMT OT by Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public final class SpDRRecipe extends SpDRObsComp {
    public static final String ATTR_OBJECT_RECIPE = "objectRecipe";

    public static final String ATTR_RASTER_TYPE = "rasterRecipe";
    public static final String ATTR_JIGGLE_TYPE = "jiggleRecipe";
    public static final String ATTR_STARE_TYPE = "stareRecipe";

    public static final String ATTR_POINTING_TYPE = "pointingRecipe";
    public static final String ATTR_FOCUS_TYPE = "focusRecipe";

    /*
     * availableTypes_instrument arrays must have corresponding
     * instrument_type in DRRecipeGUI e.g  availableTypes_heterodyne
     * containing ATTR_RASTER_TYPE = "rasterRecipe" type
     * must have a heterodyne_rasterRecipe field in DRRecipeGUI.
     *
     * Hopefully I will implement a solution soon.
     */
    public static final String[] availableTypes_heterodyne = {
            ATTR_RASTER_TYPE,
            ATTR_JIGGLE_TYPE,
            ATTR_STARE_TYPE,
    };

    // As above
    public static final String[] availableTypes_scuba2 = {
            ATTR_RASTER_TYPE,
            ATTR_STARE_TYPE,
    };

    TreeMap<String, TreeMap<String, String>> defaults =
            new TreeMap<String, TreeMap<String, String>>();

    public static LookUpTable HETERODYNE;
    public static LookUpTable SCUBA2;
    public static final String OBJECT_RECIPE_DEFAULT = "DEFAULT";
    public static final String SCUBA_OBJECT_RECIPE_DEFAULT =
            OBJECT_RECIPE_DEFAULT; // "scubaDefault";
    public static final String HETERODYNE_OBJECT_RECIPE_DEFAULT =
            OBJECT_RECIPE_DEFAULT; // "heterodyneDefault";
    public static final String[] DR_RECIPES = {OBJECT_RECIPE_DEFAULT};
    public static final SpType SP_TYPE = SpType.create(
            SpType.OBSERVATION_COMPONENT_TYPE, "DRRecipe", "DRRecipe");
    private final Class<? extends SpDRRecipe> whatami = this.getClass();

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpDRRecipe());
    }

    /**
     * Default constructor.
     */
    public SpDRRecipe() {
        super(SP_TYPE);

        // Read in the config file
        String baseDir = System.getProperty("ot.cfgdir");
        String cfgFile = baseDir + "drrecipe.cfg";
        _readCfgFile(cfgFile);
    }

    private void _readCfgFile(String filename) {
        InstCfgReader instCfg = null;
        InstCfg instInfo = null;
        String block = null;

        instCfg = new InstCfgReader(filename);
        try {
            while ((block = instCfg.readBlock()) != null) {
                instInfo = new InstCfg(block);

                if (InstCfg.matchAttr(instInfo, "heterodyne")) {
                    HETERODYNE = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo, "scuba2")) {
                    SCUBA2 = instInfo.getValueAsLUT();

                } else if (InstCfg.likeAttr(instInfo, "default_recipe")) {
                    addDefaultRecipe(instInfo.getKeyword(),
                            instInfo.getValue());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading DRRECIPE cfg file");
        }
    }

    private void addDefaultRecipe(String name, String recipe) {
        String[] split = name.split("_");

        if (split.length >= 3 && split[2].equals("DEFAULT")) {
            String instrument = split[0].toLowerCase();

            TreeMap<String, String> treeMap = null;
            if (!defaults.containsKey(instrument)) {
                treeMap = new TreeMap<String, String>();
                defaults.put(instrument, treeMap);
            } else {
                treeMap = defaults.get(instrument);
            }

            String type = split[1];
            treeMap.put(type.toLowerCase(), recipe);
        }
    }

    /**
     * Override getTitle to return the title attribute.
     */
    public String getTitle() {
        String title = type().getReadable();
        String titleAttr = getTitleAttr();

        if ((titleAttr != null) && !(titleAttr.equals(""))) {
            title += ": " + titleAttr;
        }

        return title;
    }

    public String[] getAvailableTypes(String instrument) {
        String[] availableTypes = new String[0];

        if (instrument != null && !instrument.trim().equals("")) {
            String fieldName = "availableTypes_" + instrument;

            try {
                Field field = whatami.getDeclaredField(fieldName);

                if (field != null) {
                    Class<?> klass = field.getType();

                    if (klass == String[].class) {
                        availableTypes = (String[]) field.get(null);
                    }
                }
            } catch (NoSuchFieldException nsfe) {
                System.out.println(fieldName + " does not appear to exist for "
                        + whatami.getName() + ". " + nsfe);
            } catch (IllegalAccessException iae) {
                System.out.println("You do not appear to have access to "
                        + fieldName + ". " + iae);
            }
        }

        return availableTypes;
    }

    public boolean setRecipeForType(String recipe, String type,
            String instrument) {
        boolean returnable = false;
        String[] availableTypes = getAvailableTypes(instrument);

        for (int index = 0; index < availableTypes.length; index++) {
            if (availableTypes[index].equals(type)) {
                if (recipe == null) {
                    _avTable.rm(type);
                } else {
                    _avTable.set(type, recipe);
                }
                returnable = true;

                break;
            }
        }

        return returnable;
    }

    public String getRecipeForType(String type) {
        String recipe = _avTable.get(type);
        return recipe;
    }

    public void reset() {
        _avTable.rmAll();
    }

    public void setDefaultsForInstrument(String instrument) {
        String[] types = getAvailableTypes(instrument);
        TreeMap<String, String> defaultRecipes = defaults.get(
                instrument.toLowerCase());
        int size = types.length;

        if (defaultRecipes != null) {
            for (int typesIndex = 0; typesIndex < size; typesIndex++) {
                String type = types[typesIndex];
                String shortType = type.substring(0, type.indexOf("Recipe"));
                String recipe = defaultRecipes.get(shortType);
                setRecipeForType(recipe, type, instrument);
            }
        } else {
            reset();
        }
    }
}
