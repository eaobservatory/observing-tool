/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package orac.jcmt.inst;

import java.io.*;
import java.util.*;

import orac.util.LookUpTable;
import orac.util.InstCfg;
import orac.util.InstCfgReader;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.obsComp.SpObsComp;

/**
 * This class defines the DRRecipe Observation Component.
 *
 * It is specific to the JCMT OT. DR Recipes are text strings, usually selected
 * from a restricted list. As an Observation Component there may only be
 * on in the relevant level of the hierarchy.
 *
 * So far <b>only SCUBA features</b> have been implemented.
 *
 * @author Alan Bridger, UKATC, modified for JCMT OT by Martin Folger (M.Folger@roe.ac.uk)
 */
public final class SpDRRecipe extends SpObsComp
{
    public static final String ATTR_OBJECT_RECIPE  = "objectRecipe";
    
    public static LookUpTable HETERODYNE;
    public static LookUpTable SCUBA;
    
    public static String OBJECT_RECIPE_DEFAULT            = "DEFAULT";
    public static String SCUBA_OBJECT_RECIPE_DEFAULT      = OBJECT_RECIPE_DEFAULT; // "scubaDefault";
    public static String HETERODYNE_OBJECT_RECIPE_DEFAULT = OBJECT_RECIPE_DEFAULT; // "heterodyneDefault";
    
    public static final SpType SP_TYPE =
	SpType.create(SpType.OBSERVATION_COMPONENT_TYPE, "DRRecipe", "DRRecipe");
    
    // Register the prototype.
    static {
	SpFactory.registerPrototype(new SpDRRecipe());
    }
    
    /**
     * Default constructor.
     */
    public SpDRRecipe()
    {
	super(SP_TYPE);
	
	// Read in the config file
	String baseDir = System.getProperty("ot.cfgdir");
	String cfgFile = baseDir + "drrecipe.cfg";
	_readCfgFile (cfgFile);

	_avTable.noNotifySet(ATTR_OBJECT_RECIPE, OBJECT_RECIPE_DEFAULT, 0);
    }
    
    private void _readCfgFile (String filename) {
	
	InstCfgReader instCfg = null;
	InstCfg instInfo = null;
	String block = null;
	
	instCfg = new InstCfgReader (filename);
	try {
	    while ((block = instCfg.readBlock()) != null) {
		instInfo = new InstCfg (block);
		if (InstCfg.matchAttr (instInfo, "heterodyne")) {
		    HETERODYNE = instInfo.getValueAsLUT();
		}
		else if (InstCfg.matchAttr (instInfo, "scuba")) {
		    SCUBA = instInfo.getValueAsLUT();
		}
	    }
	}catch (IOException e) {
	    System.out.println ("Error reading DRRECIPE cfg file");
	}
    }
    
    /**
     * Override getTitle to return the title attribute.
     */
    public String
	getTitle()
    {
	String title     = type().getReadable();
	String titleAttr = getTitleAttr();
	if ((titleAttr != null) && !(titleAttr.equals(""))) {
	    title = title + ": " + titleAttr;
	}
	return title;
    }
    
    
    /**
     * Set the DR recipe name.
     */
    public void
	setObjectRecipeName(String text)
    {
	_avTable.set(ATTR_OBJECT_RECIPE, text);
    }
    
    /**
     * Get the DR recipe name.
     */
    public String
	getObjectRecipeName()
    {
	String recipe = _avTable.get(ATTR_OBJECT_RECIPE);
	return recipe;
    }
    
    
    /**
     * Get the DR recipe name. Retained for compatibility
     */
    public String
	getRecipeName()
    {
	String recipe = _avTable.get(ATTR_OBJECT_RECIPE);
	return recipe;
    }
    
    /**
     * Get the default recipe (METHOD  REQUIRED...)
     */
    public String
	getDefaultRecipe()
    {
	// MFO TODO: How about heterodyne?
	return OBJECT_RECIPE_DEFAULT;
    }
    
    
    /**
     * Use default recipe
     */
    public void
	useDefaults(String instName)
    {
	if (instName.equalsIgnoreCase("heterodyne")) {
	    _avTable.noNotifySet(ATTR_OBJECT_RECIPE, HETERODYNE_OBJECT_RECIPE_DEFAULT, 0);
	    
	    setTitleAttr(HETERODYNE_OBJECT_RECIPE_DEFAULT);

        }
	else if (instName.equalsIgnoreCase("scuba")) {
	    _avTable.noNotifySet(ATTR_OBJECT_RECIPE, SCUBA_OBJECT_RECIPE_DEFAULT, 0);
	    
	    setTitleAttr(SCUBA_OBJECT_RECIPE_DEFAULT);

        }
    }
    
}
