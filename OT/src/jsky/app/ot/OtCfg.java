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
import gemini.sp.iter.SpIterChop;
import gemini.sp.obsComp.SpObsComp;
import orac.util.TelescopeUtil;

import jsky.app.ot.tpe.TelescopePosEditor;

import java.lang.reflect.Field;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 * A class to initialize and configure the OT.
 */
public final class OtCfg
{
    private static Info _otCfgInfo;

    public static Vector instrumentTypes  = new Vector();
    public static Vector iteratorTypes    = new Vector();
    public static Vector obsIteratorTypes = new Vector();

    public static TelescopeUtil telescopeUtil = null;

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
	String		baseTag = null; // Added by MFO, 19 December 2001
        String[]	libraryTags;  // Added 1-Aug-00 by AB
	String          phase1Class;
	TpeFeatureCfg[]	tpeFeatureCfgA;
	SpItemCfg[]	spItemCfgA;
	String []       nameResolvers;   // Added May 30, 2001 by MFO
	String          telescopeUtilClass; // Added by MFO, 9 January 2002
	String []       chopDefaults;       // Added by MFO, May 13, 2002
	String []       namedTargets;       // Added by MFO, June 05, 2002
	String          telescopeLatitude;   // Added by MFO, June 13, 2002
    }

    /**
     * Configure the OT.  This method should be called once at startup.
     *
     * @param baseURL A URL pointing to the base, or root, directory of the
     * OT.  This method will expect to find the configuration file in a file
     * called "cfg/ot.cfg" relative to this dir.
     */
    public static synchronized void init() throws Exception {
	// Only call this method once, at startup.
	if (_otCfgInfo != null) {
	    return;
	}

	// Get cfg directory relative to classpath / code base.
	String baseDir = System.getProperty("ot.resource.cfgdir", "jsky/app/ot/cfg/");

	// Read the configuration information from the "ot.cfg" file.
	_otCfgInfo = OtCfgReader.load(baseDir + "ot.cfg");

        // Initialize telescope specific features. Has to be done before SpTelescopePos.setBaeTag().
	// _initTelescope() must be called BEFORE _initStandardSpItems().
        _initTelescope(_otCfgInfo);

	_initStandardSpItems();

	// TpeImageFeature add-ons
	_initTpeImageFeatures(_otCfgInfo);

	// SpItem add-ons
	_initSpItems(_otCfgInfo);
    }

    public static String[] getLibraries() {
      return _otCfgInfo.libraryTags;
    }

    /**
     * Added by MFO, May 30, 2001
     * 
     * @return name resolvers
     */
    public static String[] getNameResolvers() {
      return _otCfgInfo.nameResolvers;
    }


    // Added by MFO, June 06, 2002
    /**
     * Targets which are known by the TCS by name.
     *
     * E.g. planets.
     * 
     * @return array of named targets
     */
    public static String[] getNamedTargets() {
      return _otCfgInfo.namedTargets;
    }

    // Added by MFO, June 13, 2002
    /**
     * Get telescope latitude.
     *
     * Can be used for noise calculations etc.
     */
    public static String getTelescopeLatitude() {
      return _otCfgInfo.telescopeLatitude;
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

    private static void _initTelescope(Info cfgInfo) throws Exception {
      // UKIRT GUIDE tag:
      //     UKIRTs telescopeUtil.getAdditionalTarget() (i.e. UkirtUtil.getAdditionalTarget())
      //     returns SpTelescopePos.GUIDE_TAGS[0] which is set from the GUIDE attribute
      //     in the ot.cfg file.
      // JCMT REFERENCE tag:
      //     For JCMT it is the other way round: The REFERENCE tag is defined inside
      //     JCMTs telescopeUtil.getAdditionalTarget() (i.e. JcmtUtil.getAdditionalTarget()).
      //     For the internal workings of the OT SpTelescopePos.GUIDE_TAGS[0] must then be set to
      //     the REFERENCE tag taken from telescopeUtil.getAdditionalTarget() because in
      //     the case of JCMT the REFERENCE tag is not specified in the ot.cfg file.

      // Guide Stars.
      SpTelescopePos.setGuideStarTags(cfgInfo.guideTags);

      if((cfgInfo.telescopeUtilClass != null) && (!cfgInfo.telescopeUtilClass.equals(""))) {
        try {
	  telescopeUtil = (TelescopeUtil)Class.forName(cfgInfo.telescopeUtilClass).newInstance();
	  telescopeUtil.installPreTranslator();
	}
	catch(Exception e) {
          JOptionPane.showMessageDialog(null, "Problem instantiating telescope utility class:\n" +
					"  " + e + "\n" +
					"Science Program validation and telescope specific changes to XML format\n" +
					"will not be available.\n" +
					"Make sure you specify a valid telescope utility class in your ot.cfg file\n" +
					"to avoid these problems.",
					"Telescope specific features",
                                        JOptionPane.WARNING_MESSAGE);
          e.printStackTrace();
	}
      }

      // Needed when SpTelescopePos.GUIDE_TAGS is not set from ot.cfg file
      // (e.g. JCMT OT, see comments above)
      if(SpTelescopePos.getGuideStarTags() == null) {
        SpTelescopePos.setGuideStarTags(new String[]{ telescopeUtil.getAdditionalTarget() });
      }

      // Base Position
      // Set SpTelescopePos.BASE_TAG to telescope specific name:
      // For example "Base" for UKIRT, "Science" for JCMT.
      // The base star tag has to be set BEFORE _initStandardSpItems is called.
      SpTelescopePos.setBaseTag(telescopeUtil.getBaseTag());

      SpIterChop.setChopDefaults(cfgInfo.chopDefaults);
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

        // MFO: Is now specified in the config file because UKIRT and JCMT use
	// different site quality components.
	// Observation Component: Site Quality
	//tnw        = new OtSimpleTreeNodeWidget();
	//tnw.setBothImageSrc("images/component.gif");
	//clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdCompSiteQuality");
	//SpFactory.OBSERVATION_COMPONENT_SITE_QUALITY.setClientData(clientData);

	// Observation Component: Target List
	tnw        = new OtSimpleTreeNodeWidget();
	tnw.setBothImageSrc("images/component.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdCompTargetList");
	SpFactory.OBSERVATION_COMPONENT_TARGET_LIST.setClientData(clientData);

	// MFO: Sky and Observe are now specified in the config file
	//      because they are not needed in JCMT.
	// Iterator Component: Observe
	//tnw        = new OtSimpleTreeNodeWidget();
	//tnw.setBothImageSrc("images/iterObs.gif");
	//clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdIterObserve");
	//SpFactory.ITERATOR_COMPONENT_OBSERVE.setClientData(clientData);
	//obsIteratorTypes.addElement(SpType.ITERATOR_COMPONENT_OBSERVE);

	// Iterator Component: Sky
	//tnw        = new OtSimpleTreeNodeWidget();
	//tnw.setBothImageSrc("images/iterObs.gif");
	//clientData = new OtClientData(tnw, "ot.editor.EdIterSky");
	//SpFactory.ITERATOR_COMPONENT_SKY.setClientData(clientData);
	//obsIteratorTypes.addElement(SpType.ITERATOR_COMPONENT_SKY);

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

	// MSB Folder (MFO, 09 July 2001)
	tnw        = new OtObsContainerTreeNodeWidget();
	tnw.setBothImageSrc("ot/images/msbFolder.gif");
	clientData = new OtClientData(tnw, "ot.editor.EdMsb");
	SpFactory.MSB_FOLDER.setClientData(clientData);

	// AND Folder (MFO, 09 July 2001)
	tnw        = new OtObsContainerTreeNodeWidget();
	tnw.setBothImageSrc("ot/images/andFolder.gif");
	clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdTitle");
	SpFactory.AND_FOLDER.setClientData(clientData);

	// OR Folder (MFO, 09 July 2001)
	tnw        = new OtObsContainerTreeNodeWidget();
	tnw.setBothImageSrc("ot/images/orFolder.gif");
	clientData = new OtClientData(tnw, "ot.editor.EdOrFolder");
	SpFactory.OR_FOLDER.setClientData(clientData);
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

