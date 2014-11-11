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

package orac.ukirt.inst;

import java.io.IOException;

import orac.util.LookUpTable;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.obsComp.SpDRObsComp;

/**
 * This class defines the DRRecipe Observation Component.
 *
 * It is specific to the UKIRT version of the OT. DR Recipes are text strings,
 * usually selected from a restricted list. As an Observation Component there
 * may only be on in the relevant level of the hierarchy.
 *
 * @author Alan Bridger, UKATC
 * @version 1.0
 * Modified for UIST: 2001-Nov-06 Alan Pickup, UKATC
 * Modified for WFCAM: 2003-Mar-31 Alan Pickup, UKATC
 *
 */
@SuppressWarnings("serial")
public final class SpDRRecipe extends SpDRObsComp {
    public static final String ATTR_BIAS_RECIPE = "BiasRecipe";
    public static final String ATTR_DARK_RECIPE = "DarkRecipe";
    public static final String ATTR_FLAT_RECIPE = "FlatRecipe";
    public static final String ATTR_ARC_RECIPE = "ArcRecipe";
    public static final String ATTR_SKY_RECIPE = "SkyRecipe";
    public static final String ATTR_OBJECT_RECIPE = "ObjectRecipe";

    // ADDED BY SDW
    public static final String ATTR_FOCUS_RECIPE = "FocusRecipe";
    // END

    public static final String ATTR_BIAS_IN_GROUP = "BiasInGroup";
    public static final String ATTR_DARK_IN_GROUP = "DarkInGroup";
    public static final String ATTR_FLAT_IN_GROUP = "FlatInGroup";
    public static final String ATTR_ARC_IN_GROUP = "ArcInGroup";
    public static final String ATTR_SKY_IN_GROUP = "SkyInGroup";
    public static final String ATTR_OBJECT_IN_GROUP = "ObjectInGroup";

    // ADDED BY SDW
    public static final String ATTR_FOCUS_IN_GROUP = "FocusInGroup";
    // END

    public static LookUpTable UFTI;
    public static LookUpTable CGS4;
    public static LookUpTable IRCAM3;
    public static LookUpTable MICHELLE;
    public static LookUpTable UIST;
    public static LookUpTable WFCAM;
    public static String UFTI_DARK_RECIPE_DEFAULT = "REDUCE_DARK";
    public static String UFTI_SKY_RECIPE_DEFAULT = "REDUCE_SKY";
    public static String UFTI_OBJECT_RECIPE_DEFAULT = "QUICK_LOOK";
    public static String UFTI_DARK_IN_GROUP_DEFAULT = "false";
    public static String UFTI_SKY_IN_GROUP_DEFAULT = "false";
    public static String UFTI_OBJECT_IN_GROUP_DEFAULT = "false";
    public static String IRCAM3_BIAS_RECIPE_DEFAULT = "REDUCE_BIAS";
    public static String IRCAM3_DARK_RECIPE_DEFAULT = "REDUCE_DARK";
    public static String IRCAM3_SKY_RECIPE_DEFAULT = "REDUCE_SKY";
    public static String IRCAM3_OBJECT_RECIPE_DEFAULT = "QUICK_LOOK";
    public static String IRCAM3_BIAS_IN_GROUP_DEFAULT = "false";
    public static String IRCAM3_DARK_IN_GROUP_DEFAULT = "false";
    public static String IRCAM3_SKY_IN_GROUP_DEFAULT = "false";
    public static String IRCAM3_OBJECT_IN_GROUP_DEFAULT = "false";
    public static String CGS4_BIAS_RECIPE_DEFAULT = "REDUCE_BIAS";
    public static String CGS4_DARK_RECIPE_DEFAULT = "REDUCE_DARK";
    public static String CGS4_ARC_RECIPE_DEFAULT = "REDUCE_ARC";
    public static String CGS4_FLAT_RECIPE_DEFAULT = "REDUCE_FLAT";
    public static String CGS4_SKY_RECIPE_DEFAULT = "REDUCE_SKY";
    public static String CGS4_OBJECT_RECIPE_DEFAULT = "QUICK_LOOK";
    public static String CGS4_BIAS_IN_GROUP_DEFAULT = "false";
    public static String CGS4_DARK_IN_GROUP_DEFAULT = "false";
    public static String CGS4_ARC_IN_GROUP_DEFAULT = "false";
    public static String CGS4_FLAT_IN_GROUP_DEFAULT = "false";
    public static String CGS4_SKY_IN_GROUP_DEFAULT = "false";
    public static String CGS4_OBJECT_IN_GROUP_DEFAULT = "false";
    public static String MICHELLE_BIAS_RECIPE_DEFAULT = "REDUCE_BIAS";
    public static String MICHELLE_DARK_RECIPE_DEFAULT = "REDUCE_DARK";
    public static String MICHELLE_ARC_RECIPE_DEFAULT = "REDUCE_ARC";
    public static String MICHELLE_FLAT_RECIPE_DEFAULT = "REDUCE_FLAT";
    public static String MICHELLE_SKY_RECIPE_DEFAULT = "REDUCE_SKY";
    public static String MICHELLE_OBJECT_RECIPE_DEFAULT = "QUICK_LOOK";
    public static String MICHELLE_BIAS_IN_GROUP_DEFAULT = "false";
    public static String MICHELLE_DARK_IN_GROUP_DEFAULT = "false";
    public static String MICHELLE_ARC_IN_GROUP_DEFAULT = "false";
    public static String MICHELLE_FLAT_IN_GROUP_DEFAULT = "false";
    public static String MICHELLE_SKY_IN_GROUP_DEFAULT = "false";
    public static String MICHELLE_OBJECT_IN_GROUP_DEFAULT = "false";
    public static String UIST_BIAS_RECIPE_DEFAULT = "REDUCE_BIAS";
    public static String UIST_DARK_RECIPE_DEFAULT = "REDUCE_DARK";
    public static String UIST_ARC_RECIPE_DEFAULT = "REDUCE_ARC";
    public static String UIST_FLAT_RECIPE_DEFAULT = "REDUCE_FLAT";
    public static String UIST_SKY_RECIPE_DEFAULT = "REDUCE_SKY";
    public static String UIST_OBJECT_RECIPE_DEFAULT = "QUICK_LOOK";
    public static String UIST_BIAS_IN_GROUP_DEFAULT = "false";
    public static String UIST_DARK_IN_GROUP_DEFAULT = "false";
    public static String UIST_ARC_IN_GROUP_DEFAULT = "false";
    public static String UIST_FLAT_IN_GROUP_DEFAULT = "false";
    public static String UIST_SKY_IN_GROUP_DEFAULT = "false";
    public static String UIST_OBJECT_IN_GROUP_DEFAULT = "false";
    public static String WFCAM_BIAS_RECIPE_DEFAULT = "REDUCE_BIAS";
    public static String WFCAM_DARK_RECIPE_DEFAULT = "REDUCE_DARK";
    public static String WFCAM_FLAT_RECIPE_DEFAULT = "REDUCE_FLAT";
    public static String WFCAM_SKY_RECIPE_DEFAULT = "REDUCE_SKY";

    // ADDED BY SDW
    public static String WFCAM_FOCUS_RECIPE_DEFAULT = "REDUCE_FOCUS";
    // END

    public static String WFCAM_OBJECT_RECIPE_DEFAULT = "QUICK_LOOK";
    public static String WFCAM_BIAS_IN_GROUP_DEFAULT = "false";
    public static String WFCAM_DARK_IN_GROUP_DEFAULT = "false";
    public static String WFCAM_FLAT_IN_GROUP_DEFAULT = "false";
    public static String WFCAM_SKY_IN_GROUP_DEFAULT = "false";

    // ADDED BY SDW
    public static String WFCAM_FOCUS_IN_GROUP_DEFAULT = "false";
    // END

    public static String WFCAM_OBJECT_IN_GROUP_DEFAULT = "false";

    public static final SpType SP_TYPE = SpType.create(
            SpType.OBSERVATION_COMPONENT_TYPE, "DRRecipe", "DRRecipe");

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

        if (!baseDir.endsWith("/")) {
            baseDir += '/';
        }

        String cfgFile = baseDir + "drrecipe.cfg";
        _readCfgFile(cfgFile);

        _avTable.noNotifySet(ATTR_BIAS_RECIPE, "REDUCE_BIAS", 0);
        _avTable.noNotifySet(ATTR_DARK_RECIPE, "REDUCE_DARK", 0);
        _avTable.noNotifySet(ATTR_FLAT_RECIPE, "REDUCE_FLAT", 0);
        _avTable.noNotifySet(ATTR_ARC_RECIPE, "REDUCE_ARC", 0);
        _avTable.noNotifySet(ATTR_SKY_RECIPE, "REDUCE_SKY", 0);
        _avTable.noNotifySet(ATTR_FOCUS_RECIPE, "REDUCE_FOCUS", 0);
        _avTable.noNotifySet(ATTR_OBJECT_RECIPE, "QUICK_LOOK", 0);
        _avTable.noNotifySet(ATTR_BIAS_IN_GROUP, "false", 0);
        _avTable.noNotifySet(ATTR_DARK_IN_GROUP, "false", 0);
        _avTable.noNotifySet(ATTR_FLAT_IN_GROUP, "false", 0);
        _avTable.noNotifySet(ATTR_ARC_IN_GROUP, "false", 0);
        _avTable.noNotifySet(ATTR_SKY_IN_GROUP, "true", 0);
        _avTable.noNotifySet(ATTR_OBJECT_IN_GROUP, "true", 0);
        _avTable.noNotifySet(ATTR_FOCUS_IN_GROUP, "false", 0);
    }

    private void _readCfgFile(String filename) {
        InstCfgReader instCfg = null;
        InstCfg instInfo = null;
        String block = null;

        instCfg = new InstCfgReader(filename);

        try {
            while ((block = instCfg.readBlock()) != null) {
                instInfo = new InstCfg(block);

                if (InstCfg.matchAttr(instInfo, "ufti")) {
                    UFTI = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo,
                        "ufti_dark_default_recipe")) {
                    UFTI_DARK_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "ufti_sky_default_recipe")) {
                    UFTI_SKY_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "ufti_object_default_recipe")) {
                    UFTI_OBJECT_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "ufti_dark_in_group_default")) {
                    UFTI_DARK_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "ufti_sky_in_group_default")) {
                    UFTI_SKY_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "ufti_object_in_group_default")) {
                    UFTI_OBJECT_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo, "ircam3")) {
                    IRCAM3 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo,
                        "ircam3_bias_default_recipe")) {
                    IRCAM3_BIAS_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "ircam3_dark_default_recipe")) {
                    IRCAM3_DARK_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "ircam3_sky_default_recipe")) {
                    IRCAM3_SKY_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "ircam3_object_default_recipe")) {
                    IRCAM3_OBJECT_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "ircam3_bias_in_group_default")) {
                    IRCAM3_BIAS_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "ircam3_dark_in_group_default")) {
                    IRCAM3_DARK_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "ircam3_sky_in_group_default")) {
                    IRCAM3_SKY_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "ircam3_object_in_group_default")) {
                    IRCAM3_OBJECT_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo, "cgs4")) {
                    CGS4 = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo,
                        "cgs4_bias_default_recipe")) {
                    CGS4_BIAS_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "cgs4_dark_default_recipe")) {
                    CGS4_DARK_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "cgs4_arc_default_recipe")) {
                    CGS4_ARC_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "cgs4_flat_default_recipe")) {
                    CGS4_FLAT_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "cgs4_sky_default_recipe")) {
                    CGS4_SKY_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "cgs4_object_default_recipe")) {
                    CGS4_OBJECT_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "cgs4_bias_in_group_default")) {
                    CGS4_BIAS_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "cgs4_dark_in_group_default")) {
                    CGS4_DARK_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "cgs4_arc_in_group_default")) {
                    CGS4_ARC_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "cgs4_flat_in_group_default")) {
                    CGS4_FLAT_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "cgs4_sky_in_group_default")) {
                    CGS4_SKY_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "cgs4_object_in_group_default")) {
                    CGS4_OBJECT_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo, "michelle")) {
                    MICHELLE = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo,
                        "michelle_bias_default_recipe")) {
                    MICHELLE_BIAS_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "michelle_dark_default_recipe")) {
                    MICHELLE_DARK_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "michelle_arc_default_recipe")) {
                    MICHELLE_ARC_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "michelle_flat_default_recipe")) {
                    MICHELLE_FLAT_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "michelle_sky_default_recipe")) {
                    MICHELLE_SKY_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "michelle_object_default_recipe")) {
                    MICHELLE_OBJECT_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "michelle_bias_in_group_default")) {
                    MICHELLE_BIAS_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "michelle_dark_in_group_default")) {
                    MICHELLE_DARK_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "michelle_arc_in_group_default")) {
                    MICHELLE_ARC_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "michelle_flat_in_group_default")) {
                    MICHELLE_FLAT_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "michelle_sky_in_group_default")) {
                    MICHELLE_SKY_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "michelle_object_in_group_default")) {
                    MICHELLE_OBJECT_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo, "uist")) {
                    UIST = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo,
                        "uist_bias_default_recipe")) {
                    UIST_BIAS_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "uist_dark_default_recipe")) {
                    UIST_DARK_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "uist_arc_default_recipe")) {
                    UIST_ARC_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "uist_flat_default_recipe")) {
                    UIST_FLAT_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "uist_sky_default_recipe")) {
                    UIST_SKY_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "uist_object_default_recipe")) {
                    UIST_OBJECT_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "uist_bias_in_group_default")) {
                    UIST_BIAS_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "uist_dark_in_group_default")) {
                    UIST_DARK_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "uist_arc_in_group_default")) {
                    UIST_ARC_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "uist_flat_in_group_default")) {
                    UIST_FLAT_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "uist_sky_in_group_default")) {
                    UIST_SKY_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "uist_object_in_group_default")) {
                    UIST_OBJECT_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo, "wfcam")) {
                    WFCAM = instInfo.getValueAsLUT();

                } else if (InstCfg.matchAttr(instInfo,
                        "wfcam_bias_default_recipe")) {
                    WFCAM_BIAS_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "wfcam_dark_default_recipe")) {
                    WFCAM_DARK_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "wfcam_flat_default_recipe")) {
                    WFCAM_FLAT_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "wfcam_sky_default_recipe")) {
                    WFCAM_SKY_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "wfcam_object_default_recipe")) {
                    WFCAM_OBJECT_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "wfcam_focus_default_recipe")) {
                    WFCAM_OBJECT_RECIPE_DEFAULT = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo,
                        "wfcam_bias_in_group_default")) {
                    WFCAM_BIAS_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "wfcam_dark_in_group_default")) {
                    WFCAM_DARK_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "wfcam_flat_in_group_default")) {
                    WFCAM_FLAT_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "wfcam_sky_in_group_default")) {
                    WFCAM_SKY_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "wfcam_object_in_group_default")) {
                    WFCAM_OBJECT_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();

                } else if (InstCfg.matchAttr(instInfo,
                        "wfcam_focus_in_group_default")) {
                    WFCAM_OBJECT_IN_GROUP_DEFAULT =
                            instInfo.getValue().toLowerCase();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading DRRECIPE cfg file");
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

    /**
     * Set the DR recipe name.
     */
    public void setFocusRecipeName(String text) {
        if (text == null) {
            text = "REDUCE_FOCUS";
        }

        _avTable.set(ATTR_FOCUS_RECIPE, text);
    }

    /**
     * Get the DR recipe name.
     */
    public String getFocusRecipeName() {
        String recipe = _avTable.get(ATTR_FOCUS_RECIPE);

        if (recipe == null) {
            recipe = "REDUCE_FOCUS";
        }

        return recipe;
    }

    /**
     * Set FocusInGroup.
     */
    public void setFocusInGroup(boolean tf) {
        _avTable.set(ATTR_FOCUS_IN_GROUP, tf);
    }

    /**
     * Get FocusInGroup.
     */
    public boolean getFocusInGroup() {
        boolean inGroup = _avTable.getBool(ATTR_FOCUS_IN_GROUP);
        return inGroup;
    }

    /**
     * Set the DR recipe name.
     */
    public void setBiasRecipeName(String text) {
        if (text == null) {
            text = "REDUCE_BIAS";
        }

        _avTable.set(ATTR_BIAS_RECIPE, text);
    }

    /**
     * Get the DR recipe name.
     */
    public String getBiasRecipeName() {
        String recipe = _avTable.get(ATTR_BIAS_RECIPE);

        if (recipe == null) {
            recipe = "REDUCE_BIAS";
        }

        return recipe;
    }

    /**
     * Set BiasInGroup.
     */
    public void setBiasInGroup(boolean tf) {
        _avTable.set(ATTR_BIAS_IN_GROUP, tf);
    }

    /**
     * Get BiasInGroup.
     */
    public boolean getBiasInGroup() {
        boolean inGroup = _avTable.getBool(ATTR_BIAS_IN_GROUP);
        return inGroup;
    }

    /**
     * Set the DR recipe name.
     */
    public void setDarkRecipeName(String text) {
        if (text == null) {
            text = "REDUCE_DARK";
        }

        _avTable.set(ATTR_DARK_RECIPE, text);
    }

    /**
     * Get the DR recipe name.
     */
    public String getDarkRecipeName() {
        String recipe = _avTable.get(ATTR_DARK_RECIPE);

        if (recipe == null) {
            recipe = "REDUCE_DARK";
        }

        return recipe;
    }

    /**
     * Set DarkInGroup.
     */
    public void setDarkInGroup(boolean tf) {
        _avTable.set(ATTR_DARK_IN_GROUP, tf);
    }

    /**
     * Get DarkInGroup.
     */
    public boolean getDarkInGroup() {
        boolean inGroup = _avTable.getBool(ATTR_DARK_IN_GROUP);
        return inGroup;
    }

    /**
     * Set the DR recipe name.
     */
    public void setFlatRecipeName(String text) {
        if (text == null) {
            text = "REDUCE_FLAT";
        }

        _avTable.set(ATTR_FLAT_RECIPE, text);
    }

    /**
     * Get the DR recipe name.
     */
    public String getFlatRecipeName() {
        String recipe = _avTable.get(ATTR_FLAT_RECIPE);

        if (recipe == null) {
            recipe = "REDUCE_FLAT";
        }

        return recipe;
    }

    /**
     * Set FlatInGroup.
     */
    public void setFlatInGroup(boolean tf) {
        _avTable.set(ATTR_FLAT_IN_GROUP, tf);
    }

    /**
     * Get FlatInGroup.
     */
    public boolean getFlatInGroup() {
        boolean inGroup = _avTable.getBool(ATTR_FLAT_IN_GROUP);
        return inGroup;
    }

    /**
     * Set the DR recipe name.
     */
    public void setArcRecipeName(String text) {
        if (text == null) {
            text = "REDUCE_ARC";
        }

        _avTable.set(ATTR_ARC_RECIPE, text);
    }

    /**
     * Get the DR recipe name.
     */
    public String getArcRecipeName() {
        String recipe = _avTable.get(ATTR_ARC_RECIPE);

        if (recipe == null) {
            recipe = "REDUCE_ARC";
        }

        return recipe;
    }

    /**
     * Set ArcInGroup.
     */
    public void setArcInGroup(boolean tf) {
        _avTable.set(ATTR_ARC_IN_GROUP, tf);
    }

    /**
     * Get ArcInGroup.
     */
    public boolean getArcInGroup() {
        boolean inGroup = _avTable.getBool(ATTR_ARC_IN_GROUP);
        return inGroup;
    }

    /**
     * Set the DR recipe name.
     */
    public void setSkyRecipeName(String text) {
        if (text == null) {
            text = "REDUCE_SKY";
        }

        _avTable.set(ATTR_SKY_RECIPE, text);
    }

    /**
     * Get the DR recipe name.
     */
    public String getSkyRecipeName() {
        String recipe = _avTable.get(ATTR_SKY_RECIPE);

        if (recipe == null) {
            recipe = "REDUCE_SKY";
        }

        return recipe;
    }

    /**
     * Set SkyInGroup.
     */
    public void setSkyInGroup(boolean tf) {
        _avTable.set(ATTR_SKY_IN_GROUP, tf);
    }

    /**
     * Get SkyInGroup.
     */
    public boolean getSkyInGroup() {
        boolean inGroup = _avTable.getBool(ATTR_SKY_IN_GROUP);
        return inGroup;
    }

    /**
     * Set the DR recipe name.
     */
    public void setObjectRecipeName(String text) {
        _avTable.set(ATTR_OBJECT_RECIPE, text);
    }

    /**
     * Get the DR recipe name.
     */
    public String getObjectRecipeName() {
        String recipe = _avTable.get(ATTR_OBJECT_RECIPE);
        return recipe;
    }

    /**
     * Set ObjectInGroup.
     */
    public void setObjectInGroup(boolean tf) {
        _avTable.set(ATTR_OBJECT_IN_GROUP, tf);
    }

    /**
     * Get ObjectInGroup.
     */
    public boolean getObjectInGroup() {
        boolean inGroup = _avTable.getBool(ATTR_OBJECT_IN_GROUP);
        return inGroup;
    }

    /**
     * Get the DR recipe name. Retained for compatibility
     */
    public String getRecipeName() {
        String recipe = _avTable.get(ATTR_OBJECT_RECIPE);
        return recipe;
    }

    /**
     * Get the default recipe (METHOD  REQUIRED...)
     */
    public String getDefaultRecipe() {
        return "QUICK_LOOK";
    }

    /**
     * Use default recipe
     */
    public void useDefaults(String instName) {
        if (instName.equalsIgnoreCase("ufti")) {
            _avTable.noNotifySet(ATTR_DARK_RECIPE,
                    UFTI_DARK_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_SKY_RECIPE,
                    UFTI_SKY_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_OBJECT_RECIPE,
                    UFTI_OBJECT_RECIPE_DEFAULT, 0);

            _avTable.noNotifySet(ATTR_DARK_IN_GROUP,
                    UFTI_DARK_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_SKY_IN_GROUP,
                    UFTI_SKY_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_OBJECT_IN_GROUP,
                    UFTI_OBJECT_IN_GROUP_DEFAULT, 0);
            setTitleAttr(UFTI_OBJECT_RECIPE_DEFAULT);

        } else if (instName.equalsIgnoreCase("ircam3")) {
            _avTable.noNotifySet(ATTR_BIAS_RECIPE,
                    IRCAM3_BIAS_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_DARK_RECIPE,
                    IRCAM3_DARK_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_SKY_RECIPE,
                    IRCAM3_SKY_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_OBJECT_RECIPE,
                    IRCAM3_OBJECT_RECIPE_DEFAULT, 0);

            _avTable.noNotifySet(ATTR_BIAS_IN_GROUP,
                    IRCAM3_BIAS_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_DARK_IN_GROUP,
                    IRCAM3_DARK_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_SKY_IN_GROUP,
                    IRCAM3_SKY_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_OBJECT_IN_GROUP,
                    IRCAM3_OBJECT_IN_GROUP_DEFAULT, 0);
            setTitleAttr(IRCAM3_OBJECT_RECIPE_DEFAULT);

        } else if (instName.equalsIgnoreCase("cgs4")) {
            _avTable.noNotifySet(ATTR_BIAS_RECIPE, CGS4_BIAS_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_DARK_RECIPE, CGS4_DARK_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_FLAT_RECIPE, CGS4_FLAT_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_ARC_RECIPE, CGS4_ARC_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_SKY_RECIPE, CGS4_SKY_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_OBJECT_RECIPE,
                    CGS4_OBJECT_RECIPE_DEFAULT, 0);

            _avTable.noNotifySet(ATTR_BIAS_IN_GROUP,
                    CGS4_BIAS_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_DARK_IN_GROUP,
                    CGS4_DARK_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_FLAT_IN_GROUP,
                    CGS4_FLAT_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_ARC_IN_GROUP,
                    CGS4_ARC_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_SKY_IN_GROUP,
                    CGS4_SKY_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_OBJECT_IN_GROUP,
                    CGS4_OBJECT_IN_GROUP_DEFAULT, 0);
            setTitleAttr(CGS4_OBJECT_RECIPE_DEFAULT);

        } else if (instName.equalsIgnoreCase("michelle")) {
            _avTable.noNotifySet(ATTR_BIAS_RECIPE,
                    MICHELLE_BIAS_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_DARK_RECIPE,
                    MICHELLE_DARK_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_FLAT_RECIPE,
                    MICHELLE_FLAT_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_ARC_RECIPE,
                    MICHELLE_ARC_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_SKY_RECIPE,
                    MICHELLE_SKY_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_OBJECT_RECIPE,
                    MICHELLE_OBJECT_RECIPE_DEFAULT, 0);

            _avTable.noNotifySet(ATTR_BIAS_IN_GROUP,
                    MICHELLE_BIAS_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_DARK_IN_GROUP,
                    MICHELLE_DARK_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_FLAT_IN_GROUP,
                    MICHELLE_FLAT_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_ARC_IN_GROUP,
                    MICHELLE_ARC_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_SKY_IN_GROUP,
                    MICHELLE_SKY_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_OBJECT_IN_GROUP,
                    MICHELLE_OBJECT_IN_GROUP_DEFAULT, 0);
            setTitleAttr(MICHELLE_OBJECT_RECIPE_DEFAULT);

        } else if (instName.equalsIgnoreCase("uist")) {
            _avTable.noNotifySet(ATTR_BIAS_RECIPE, UIST_BIAS_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_DARK_RECIPE, UIST_DARK_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_FLAT_RECIPE, UIST_FLAT_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_ARC_RECIPE, UIST_ARC_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_SKY_RECIPE, UIST_SKY_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_OBJECT_RECIPE,
                    UIST_OBJECT_RECIPE_DEFAULT, 0);

            _avTable.noNotifySet(ATTR_BIAS_IN_GROUP,
                    UIST_BIAS_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_DARK_IN_GROUP,
                    UIST_DARK_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_FLAT_IN_GROUP,
                    UIST_FLAT_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_ARC_IN_GROUP,
                    UIST_ARC_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_SKY_IN_GROUP,
                    UIST_SKY_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_OBJECT_IN_GROUP,
                    UIST_OBJECT_IN_GROUP_DEFAULT, 0);
            setTitleAttr(UIST_OBJECT_RECIPE_DEFAULT);

        } else if (instName.equalsIgnoreCase("wfcam")) {
            _avTable.noNotifySet(ATTR_BIAS_RECIPE,
                    WFCAM_BIAS_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_DARK_RECIPE,
                    WFCAM_DARK_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_FLAT_RECIPE,
                    WFCAM_FLAT_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_FOCUS_RECIPE,
                    WFCAM_FOCUS_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_SKY_RECIPE,
                    WFCAM_SKY_RECIPE_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_OBJECT_RECIPE,
                    WFCAM_OBJECT_RECIPE_DEFAULT, 0);

            _avTable.noNotifySet(ATTR_BIAS_IN_GROUP,
                    WFCAM_BIAS_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_DARK_IN_GROUP,
                    WFCAM_DARK_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_FLAT_IN_GROUP,
                    WFCAM_FLAT_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_FOCUS_IN_GROUP,
                    WFCAM_FOCUS_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_SKY_IN_GROUP,
                    WFCAM_SKY_IN_GROUP_DEFAULT, 0);
            _avTable.noNotifySet(ATTR_OBJECT_IN_GROUP,
                    WFCAM_OBJECT_IN_GROUP_DEFAULT, 0);
            setTitleAttr(WFCAM_OBJECT_RECIPE_DEFAULT);
        }
    }
}
