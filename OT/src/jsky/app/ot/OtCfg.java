// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

import ot.phase1.Phase1HTMLDocument;

import gemini.sp.SpFactory;
import gemini.sp.SpItem;
import gemini.sp.SpType;
import gemini.sp.SpTelescopePos;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterComp;
import gemini.sp.obsComp.SpObsComp;

import jsky.app.ot.tpe.TelescopePosEditor;

import java.lang.reflect.Field;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

/**
 * A class to initialize and configure the OT.
 */
public final class OtCfg
{
    private static Info _otCfgInfo;

    public static Vector instrumentTypes  = new Vector();
    public static Vector iteratorTypes    = new Vector();
    public static Vector obsIteratorTypes = new Vector();

    public static String validationClass  = null;

    /**
     * Describes the configuration of an add-on SpItem.
     */
    static public class SpItemCfg {
	String spClass;		// The (fully qualified) SpItem subclass
	String editorClass;	// The (fully qualified) OtItemEditor subclass
	String imgFeatureClass;	// Optional TpeImageFeature subclass
                                // associated with the item.
    }

    /**
     * Describes an add-on TpeImageFeature.
     */
    static public class TpeFeatureCfg {
	String featClass;	// The (fully qualified) TpeImageFeature subclass
	String type;	// "target list", "instrument", or "independent"
    }

    /**
     * Contains the parsed contents of the ot.cfg file.  Used by the init
     * method to setup the OT.
     */
    static public class Info {
	String[]	guideTags;
        String[]	libraryTags;  // Added 1-Aug-00 by AB
	String          phase1Class;
	TpeFeatureCfg[]	tpeFeatureCfgA;
	SpItemCfg[]	spItemCfgA;
	String          validationClass;
    }

    /**
     * Configure the OT.  This method should be called once at startup.
     *
     * @param baseURL A URL pointing to the base, or root, directory of the
     * OT.  This method will expect to find the configuration file in a file
     * called "cfg/ot.cfg" relative to this dir.
     */
    public static synchronized void init() {
	// Only call this method once, at startup.
	if (_otCfgInfo != null) {
	    return;
	}

	_initStandardSpItems();

	// Get cfg directory relative to classpath / code base.
	String baseDir = System.getProperty("ot.resource.cfgdir", "jsky/app/ot/cfg/");

	// Read the configuration information from the "ot.cfg" file.
	_otCfgInfo = OtCfgReader.load(baseDir + "ot.cfg");

	// Guide Stars used by Gemini
	SpTelescopePos.setGuideStarTags(_otCfgInfo.guideTags);

	// TpeImageFeature add-ons
	_initTpeImageFeatures(_otCfgInfo);

	// SpItem add-ons
	_initSpItems(_otCfgInfo);

        // Validation tool
        _initValidationTool(_otCfgInfo);
    }

    public static String[] getLibraries() {
      return _otCfgInfo.libraryTags;
    }

    public static synchronized boolean	phase1Available() {
	if ((_otCfgInfo == null) || (_otCfgInfo.phase1Class == null)) {
	    return false;
	}
	return true;
    }

    public static synchronized Phase1HTMLDocument createHTMLDocument()  {
	if (!phase1Available()) {
	    return null;
	}

	String       className = _otCfgInfo.phase1Class;
	Phase1HTMLDocument doc = null;
	try {
	    Class c = Class.forName(className);
	    doc = (Phase1HTMLDocument) c.newInstance();
	} catch (Exception ex) {
	    System.out.println("Problem initializing class: " + className);
	    ex.printStackTrace();
	}
	return doc;
    }
/*
    public static synchronized SpValidation getValidationTool() {
    }
*/
    //
    // Setup an-on TpeImageFeatures.  See the ot.cfg file in the OT "base"
    // directory for more information.
    //
    private static void	_initTpeImageFeatures(Info cfgInfo) {
	TpeFeatureCfg[] tfcA = cfgInfo.tpeFeatureCfgA;
	if (tfcA == null) {
	    return;
	}

	for (int i=0; i<tfcA.length; ++i) {
	    String className = tfcA[i].featClass;
	    String type      = tfcA[i].type;
	    if (type.equals("target list")) {
		TelescopePosEditor.registerTargetListFeature(className);
	    } else if (type.equals("instrument")) {
		TelescopePosEditor.registerInstrumentFeature(className);
	    } else {
		TelescopePosEditor.registerFeature(className);
	    }
	}
    }

    //
    // Setup add-on SpItem prototypes.  See the ot.cfg file in the OT "base"
    // directory for more information.
    //
    private static void	_initSpItems(Info cfgInfo)  {
	SpItem           spItem;
	OtClientData     clientData;
	OtTreeNodeWidget tnw;

	SpItemCfg[] sicA = cfgInfo.spItemCfgA;
	if (sicA == null) {
	    return;
	}

	for (int i=0; i<sicA.length; ++i) {
	    tnw = new OtSimpleTreeNodeWidget();
	    SpType spType = _initClass(sicA[i].spClass);
	    SpItem proto  = SpFactory.getPrototype(spType);

	    // An instrument add-on
	    // MODIFIED BY AB: To check "instrument"
            // add ons against SpObsComp instead of SpInstObsComp. Allows insertion of
            // DR Recipe.
	    if (proto instanceof gemini.sp.obsComp.SpObsComp) {
		tnw.setBothImageSrc("images/component.gif");
		instrumentTypes.addElement(spType);

		// An "observe" iterator add-on
	    } else if (proto instanceof gemini.sp.iter.SpIterObserveBase) {
		tnw.setBothImageSrc("images/iterObs.gif");
		obsIteratorTypes.addElement(spType);

		// A normal iterator add-on
	    } else if (proto instanceof gemini.sp.iter.SpIterComp) {
		tnw.setBothImageSrc("images/iterComp.gif");
		iteratorTypes.addElement(spType);
	    }

	    OtClientData cd;
	    if (sicA[i].imgFeatureClass == null) {
		cd = new OtClientData(tnw, sicA[i].editorClass);
	    } else {
		cd = new OtClientData(tnw, sicA[i].editorClass, sicA[i].imgFeatureClass);
	    }
	    proto.setClientData(cd);
	}
    }

    private static void _initValidationTool(Info cfgInfo) {
      validationClass = cfgInfo.validationClass;
    }

    //
    // Initialize the "standard" prototypes (eg, the Science Program and
    // Observation items).  These items are not configurable.
    //
    private static void	_initStandardSpItems()  {
	SpItem       spItem;
	OtClientData clientData;

	OtTreeNodeWidget tnw;

	// Science Program
	tnw        = new OtObsContainerTreeNodeWidget();
	tnw.setBothImageSrc("images/archiv_small.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdProgram");
	SpFactory.SCIENCE_PROGRAM.setClientData(clientData);

	// Science Plan
	tnw        = new OtObsContainerTreeNodeWidget();
	tnw.setBothImageSrc("images/archiv_small.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdProgram");
	SpFactory.SCIENCE_PLAN.setClientData(clientData);

	// Library
	tnw        = new OtObsContainerTreeNodeWidget();
	tnw.setBothImageSrc("images/library.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdTitle");
	SpFactory.LIBRARY.setClientData(clientData);

	// Library Folder
	tnw        = new OtObsContainerTreeNodeWidget();
	tnw.setBothImageSrc("images/libFolder.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdTitle");
	SpFactory.LIBRARY_FOLDER.setClientData(clientData);

	// Observation Folder
	tnw        = new OtObsContainerTreeNodeWidget();
	tnw.setBothImageSrc("images/obsFolder.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdTitle");
	SpFactory.OBSERVATION_FOLDER.setClientData(clientData);

	// Observation Group
	tnw        = new OtObsContainerTreeNodeWidget();
	tnw.setBothImageSrc("images/obsGroup.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdTitle");
	SpFactory.OBSERVATION_GROUP.setClientData(clientData);

	// Observation
	tnw        = new OtObsTreeNodeWidget();
	tnw.setBothImageSrc("images/observation.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdObservation");
	SpFactory.OBSERVATION.setClientData(clientData);

	// Sequence (Iterator Folder)
	tnw        = new OtSimpleTreeNodeWidget();
	tnw.setBothImageSrc("images/iterFolder.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdIteratorFolder");
	SpFactory.SEQUENCE.setClientData(clientData);

	// Note
	tnw        = new OtSimpleTreeNodeWidget();
	tnw.setBothImageSrc("images/note-tiny.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdNote");
	SpFactory.NOTE.setClientData(clientData);

	// Observation Component: Site Quality
	tnw        = new OtSimpleTreeNodeWidget();
	tnw.setBothImageSrc("images/component.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdCompSiteQuality");
	SpFactory.OBSERVATION_COMPONENT_SITE_QUALITY.setClientData(clientData);

	// Observation Component: Target List
	tnw        = new OtSimpleTreeNodeWidget();
	tnw.setBothImageSrc("images/component.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdCompTargetList");
	SpFactory.OBSERVATION_COMPONENT_TARGET_LIST.setClientData(clientData);

	// Iterator Component: Observe
	tnw        = new OtSimpleTreeNodeWidget();
	tnw.setBothImageSrc("images/iterObs.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdIterObserve");
	SpFactory.ITERATOR_COMPONENT_OBSERVE.setClientData(clientData);
	obsIteratorTypes.addElement(SpType.ITERATOR_COMPONENT_OBSERVE);

	// Iterator Component: Sky
	tnw        = new OtSimpleTreeNodeWidget();
	tnw.setBothImageSrc("images/iterObs.gif");
	clientData = new OtClientData(tnw, "ot.editor.EdIterSky");
	SpFactory.ITERATOR_COMPONENT_SKY.setClientData(clientData);
	obsIteratorTypes.addElement(SpType.ITERATOR_COMPONENT_SKY);

	// Iterator Component: Offset
	tnw        = new OtSimpleTreeNodeWidget();
	tnw.setBothImageSrc("images/iterComp.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdIterOffset",
                                      "jsky.app.ot.editor.EdIterOffsetFeature");
	SpFactory.ITERATOR_COMPONENT_OFFSET.setClientData(clientData);
	iteratorTypes.addElement(SpType.ITERATOR_COMPONENT_OFFSET);

	// Iterator Component: Repeat
	tnw        = new OtSimpleTreeNodeWidget();
	tnw.setBothImageSrc("images/iterComp.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdIterRepeat");
	SpFactory.ITERATOR_COMPONENT_REPEAT.setClientData(clientData);
	iteratorTypes.addElement(SpType.ITERATOR_COMPONENT_REPEAT);
    }


    //
    // Get the SpType of the given fully qualified className.  This
    // will only work if the class defines a public static variable
    // called SP_TYPE that refers to a unique, registered SpType.
    //
    // For instance, the NIRI class (ot_cfg.inst.SpInstNIRI) contains:
    //
    //   public static final SpType SP_TYPE =
    //        SpType.create(SpType.OBSERVATION_COMPONENT_TYPE, "inst.NIRI", "NIRI");
    //
    private static SpType _initClass(String className)  {
	SpType spType = null;
	try {
	    Class c = Class.forName(className);
	    Field f = c.getField("SP_TYPE");
	    spType  = (SpType) f.get(null);  // SP_TYPE must be a static field
	} catch (Exception ex) {
	    System.out.println("Problem initializing class: " + className);
	    System.out.println(ex);
	}
	return spType;
    }

}

