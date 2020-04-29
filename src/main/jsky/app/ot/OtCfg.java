/*
 * Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jsky.app.ot;

import gemini.sp.SpFactory;
import gemini.sp.SpItem;
import gemini.sp.SpType;
import gemini.sp.SpTelescopePos;
import gemini.sp.iter.SpIterChop;
import orac.util.TelescopeUtil;

import jsky.app.ot.tpe.TelescopePosEditor;

import java.lang.reflect.Field;
import java.io.File;
import java.net.URL;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 * A class to initialize and configure the OT.
 */
public final class OtCfg {
    private static Info _otCfgInfo;
    public static Vector<SpType> instrumentTypes = new Vector<SpType>();
    public static Vector<SpType> iteratorTypes = new Vector<SpType>();
    public static Vector<SpType> obsIteratorTypes = new Vector<SpType>();
    public static TelescopeUtil telescopeUtil = null;

    /**
     * Describes the configuration of an add-on SpItem.
     */
    static public class SpItemCfg {
        String spClass; // The (fully qualified) SpItem subclass
        String editorClass; // The (fully qualified) OtItemEditor subclass
        String imgFeatureClass; // Optional TpeImageFeature subclass
        // associated with the item.
    }

    /**
     * Describes an add-on TpeImageFeature.
     */
    static public class TpeFeatureCfg {
        String featClass; // The (fully qualified) TpeImageFeature subclass
        String type; // "target list", "instrument", or "independent"
    }

    /**
     * Contains the parsed contents of the ot.cfg file.
     *
     * Used by the init method to setup the OT.
     */
    static public class Info {
        String[] guideTags;
        String baseTag = null; // Added by MFO, 19 December 2001
        String[] libraryTags; // Added 1-Aug-00 by AB
        String phase1Class;
        TpeFeatureCfg[] tpeFeatureCfgA;
        SpItemCfg[] spItemCfgA;
        String[] nameResolvers; // Added May 30, 2001 by MFO
        String telescopeUtilClass; // Added by MFO, 9 January 2002
        String[] chopDefaults; // Added by MFO, May 13, 2002
        String[] namedTargets; // Added by MFO, June 05, 2002
        String telescopeLatitude; // Added by MFO, June 13, 2002
        String schemaURL;
        String schemaLocation; // Added by SdW, Sept. 2002
        String schemaBase; // Added ny SdW, Dec 2003
        String proxyServer; // Added by Sdw, Feb 2003
        String proxyPort; // Added by Sdw, Feb 2003
        String[] noteLabels;
        String[] noteTags;
        String[] noteExamples;
    }

    /**
     * Configure the OT.
     *
     * This method should be called once at startup.
     */
    public static synchronized void init() throws Exception {
        // Only call this method once, at startup.
        if (_otCfgInfo != null) {
            return;
        }

        // Get cfg directory relative to classpath / code base.
        StringBuffer buffer = new StringBuffer();
        buffer.append("jsky");
        buffer.append('/');
        buffer.append("app");
        buffer.append('/');
        buffer.append("ot");
        buffer.append('/');
        buffer.append("cfg");
        buffer.append('/');
        String alternativePath = buffer.toString();
        String baseDir = System.getProperty("ot.resource.cfgdir",
                alternativePath);

        // Read the configuration information from the "ot.cfg" file.
        buffer.delete(0, buffer.length());
        buffer.append(baseDir);

        if (!baseDir.endsWith("/")) {
            buffer.append("/");
        }

        // if there is an alternative configuration file use that otherwise
        // use the default
        buffer.append(System.getProperty("ot.cfg.file", "ot.cfg"));
        String configurationFile = buffer.toString();
        _otCfgInfo = OtCfgReader.load(configurationFile);

        // Initialize telescope specific features. Has to be done before
        // SpTelescopePos.setBaeTag().  _initTelescope() must be called
        // BEFORE _initStandardSpItems().

        _initTelescope(_otCfgInfo);

        _initStandardSpItems();

        // Guide Stars
        SpTelescopePos.setGuideStarTags(_otCfgInfo.guideTags);

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

    // Added by SdW. Sept 2002
    /**
     * Get the location of the schema.
     * @return  The fully qualified file name for the installed schema file
     */
    public static String getSchemaLocation() {
        /*
         *  See if we can use a URL version of the schema
         *  schemaBase = _otCfgInfo.schemaBase
         *  schema = "http://www.jach.hawaii.edu/JACpublic/JAC/software/omp/schema/"
         *         + schemaBase
         */
        StringBuffer buffer = new StringBuffer();
        buffer.append(_otCfgInfo.schemaLocation);
        buffer.append('/');
        buffer.append(_otCfgInfo.schemaBase);
        String schema = buffer.toString();

        try {
            URL url = new URL(schema);
            url.getContent();

        } catch (Exception e) {
            System.out.println(
                    "No web connection available or file moved from server");
            System.out.println("Trying local version...");
            buffer.delete(0, buffer.length());
            buffer.append(System.getProperty("ot.cfgdir"));

            if (!System.getProperty("ot.cfgdir").endsWith("/")) {
                buffer.append(File.separator);
            }

            buffer.append("..");
            buffer.append(File.separator);
            buffer.append("schema");
            buffer.append(File.separator);
            buffer.append("TOML");
            buffer.append(File.separator);
            buffer.append("JAC");
            buffer.append(File.separator);
            buffer.append(_otCfgInfo.schemaBase);
            schema = buffer.toString();
            File file = new File(schema);

            if (!file.exists()) {
                schema = null;
            }
        }

        return schema;
    }

    public static String getSchemaURL() {
        return _otCfgInfo.schemaURL;
    }

    // Added by SdW. Feb 2003
    /**
     * Get the name of the proxy server
     */
    public static String getProxyServer() {
        return _otCfgInfo.proxyServer;
    }

    public static String getProxyPort() {
        return _otCfgInfo.proxyPort;
    }

    public static String[] getNoteLabels() {
        return _otCfgInfo.noteLabels;
    }

    public static String[] getNoteExamples() {
        return _otCfgInfo.noteExamples;
    }

    public static String[] getNoteTags() {
        return _otCfgInfo.noteTags;
    }

    public static synchronized boolean phase1Available() {
        return ((_otCfgInfo != null) && (_otCfgInfo.phase1Class != null));
    }

    /**
     * Setup an-on TpeImageFeatures.
     *
     * See the ot.cfg file in the OT "base" directory for more information.
     */
    private static void _initTpeImageFeatures(Info cfgInfo) {
        TpeFeatureCfg[] tfcA = cfgInfo.tpeFeatureCfgA;

        if (tfcA == null) {
            return;
        }

        for (int i = 0; i < tfcA.length; ++i) {
            String className = tfcA[i].featClass;
            String type = tfcA[i].type;

            if (type.equals("target list")) {
                TelescopePosEditor.registerTargetListFeature(className);
            } else if (type.equals("instrument")) {
                TelescopePosEditor.registerInstrumentFeature(className);
            } else {
                TelescopePosEditor.registerFeature(className);
            }
        }
    }

    /**
     * Setup add-on SpItem prototypes.
     *
     * See the ot.cfg file in the OT "base" directory for more information.
     */
    private static void _initSpItems(Info cfgInfo) {
        OtTreeNodeWidget tnw;

        SpItemCfg[] sicA = cfgInfo.spItemCfgA;
        if (sicA == null) {
            return;
        }

        for (int i = 0; i < sicA.length; ++i) {
            tnw = new OtSimpleTreeNodeWidget();
            SpType spType = _initClass(sicA[i].spClass);
            SpItem proto = SpFactory.getPrototype(spType);

            // An instrument add-on
            // MODIFIED BY AB: To check "instrument"
            // add ons against SpObsComp instead of SpInstObsComp.
            // Allows insertion of DR Recipe.
            if (proto instanceof gemini.sp.obsComp.SpObsComp) {
                tnw.setBothImageSrc("images/component.gif");
                instrumentTypes.addElement(spType);

            } else if (proto instanceof gemini.sp.iter.SpIterObserveBase) {
                // An "observe" iterator add-on
                tnw.setBothImageSrc("images/iterObs.gif");
                obsIteratorTypes.addElement(spType);

            } else if (proto instanceof gemini.sp.iter.SpIterComp) {
                // A normal iterator add-on
                tnw.setBothImageSrc("images/iterComp.gif");
                iteratorTypes.addElement(spType);
            }

            OtClientData cd;

            if (sicA[i].imgFeatureClass == null) {
                cd = new OtClientData(tnw, sicA[i].editorClass);
            } else {
                cd = new OtClientData(tnw, sicA[i].editorClass,
                        sicA[i].imgFeatureClass);
            }

            proto.setClientData(cd);
        }
    }

    private static void _initTelescope(Info cfgInfo) throws Exception {
        // Guide Stars.
        SpTelescopePos.setGuideStarTags(cfgInfo.guideTags);

        if ((cfgInfo.telescopeUtilClass != null)
                && (!cfgInfo.telescopeUtilClass.equals(""))) {
            try {
                telescopeUtil = (TelescopeUtil) Class.forName(
                        cfgInfo.telescopeUtilClass).newInstance();
                telescopeUtil.installPreTranslator();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Problem instantiating telescope utility class:\n"
                                + "  "
                                + e
                                + "\n"
                                + "Science Program validation and telescope"
                                + " specific changes to XML format\n"
                                + "will not be available.\n"
                                + "Make sure you specify a valid telescope"
                                + " utility class in your ot.cfg file\n"
                                + "to avoid these problems.",
                        "Telescope specific features",
                        JOptionPane.WARNING_MESSAGE);

                e.printStackTrace();
            }
        }

        // Base Position
        // Set SpTelescopePos.BASE_TAG to telescope specific name:
        // For example "Base" for UKIRT, "Science" for JCMT.
        // The base star tag has to be set BEFORE _initStandardSpItems is
        // called.
        SpTelescopePos.setBaseTag(telescopeUtil.getBaseTag());

        SpIterChop.setChopDefaults(cfgInfo.chopDefaults);
    }

    /**
     * Initialize the "standard" prototypes (eg, the Science Program and
     * Observation items).
     *
     * These items are not configurable.
     */
    private static void _initStandardSpItems() {
        OtClientData clientData;

        OtTreeNodeWidget tnw;

        // Science Program
        tnw = new OtObsContainerTreeNodeWidget();
        tnw.setBothImageSrc("images/archiv_small.gif");
        clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdProgram");
        SpFactory.SCIENCE_PROGRAM.setClientData(clientData);

        // Library
        tnw = new OtObsContainerTreeNodeWidget();
        tnw.setBothImageSrc("images/library.gif");
        clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdTitle");
        SpFactory.LIBRARY.setClientData(clientData);

        // Library Folder
        tnw = new OtObsContainerTreeNodeWidget();
        tnw.setBothImageSrc("images/libFolder.gif");
        clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdTitle");
        SpFactory.LIBRARY_FOLDER.setClientData(clientData);

        // Observation Folder
        tnw = new OtObsContainerTreeNodeWidget();
        tnw.setBothImageSrc("images/obsFolder.gif");
        clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdTitle");
        SpFactory.OBSERVATION_FOLDER.setClientData(clientData);

        // Observation Group
        tnw = new OtObsContainerTreeNodeWidget();
        tnw.setBothImageSrc("images/obsGroup.gif");
        clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdTitle");
        SpFactory.OBSERVATION_GROUP.setClientData(clientData);

        // Observation
        tnw = new OtObsTreeNodeWidget();
        tnw.setBothImageSrc("images/observation.gif");
        clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdObservation");
        SpFactory.OBSERVATION.setClientData(clientData);

        // Sequence (Iterator Folder)
        tnw = new OtSimpleTreeNodeWidget();
        tnw.setBothImageSrc("images/iterFolder.gif");
        clientData = new OtClientData(tnw,
                "jsky.app.ot.editor.EdIteratorFolder");
        SpFactory.SEQUENCE.setClientData(clientData);

        // Note
        tnw = new OtSimpleTreeNodeWidget();
        tnw.setBothImageSrc("images/note-tiny.gif");
        clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdNote");
        SpFactory.NOTE.setClientData(clientData);

        // Observation Component: Target List
        tnw = new OtSimpleTreeNodeWidget();
        tnw.setBothImageSrc("images/component.gif");
        clientData = new OtClientData(tnw,
                "jsky.app.ot.editor.EdCompTargetList");
        SpFactory.OBSERVATION_COMPONENT_TARGET_LIST.setClientData(clientData);

        // Iterator Component: Offset
        tnw = new OtSimpleTreeNodeWidget();
        tnw.setBothImageSrc("images/iterComp.gif");
        clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdIterOffset",
                "jsky.app.ot.editor.EdIterOffsetFeature");
        SpFactory.ITERATOR_COMPONENT_OFFSET.setClientData(clientData);
        iteratorTypes.addElement(SpType.ITERATOR_COMPONENT_OFFSET);

        // Iterator Component: Repeat
        tnw = new OtSimpleTreeNodeWidget();
        tnw.setBothImageSrc("images/iterComp.gif");
        clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdIterRepeat");
        SpFactory.ITERATOR_COMPONENT_REPEAT.setClientData(clientData);
        iteratorTypes.addElement(SpType.ITERATOR_COMPONENT_REPEAT);

        // MSB Folder (MFO, 09 July 2001)
        tnw = new OtObsContainerTreeNodeWidget();
        tnw.setBothImageSrc("ot/images/msbFolder.gif");
        clientData = new OtClientData(tnw, "ot.editor.EdMsb");
        SpFactory.MSB_FOLDER.setClientData(clientData);

        // AND Folder (MFO, 09 July 2001)
        tnw = new OtObsContainerTreeNodeWidget();
        tnw.setBothImageSrc("ot/images/andFolder.gif");
        clientData = new OtClientData(tnw, "jsky.app.ot.editor.EdTitle");
        SpFactory.AND_FOLDER.setClientData(clientData);

        // OR Folder (MFO, 09 July 2001)
        tnw = new OtObsContainerTreeNodeWidget();
        tnw.setBothImageSrc("ot/images/orFolder.gif");
        clientData = new OtClientData(tnw, "ot.editor.EdOrFolder");
        SpFactory.OR_FOLDER.setClientData(clientData);

        // Survey Container (SDW Oct 2004)
        tnw = new OtObsContainerTreeNodeWidget();
        tnw.setBothImageSrc("ot/images/surveyContainer.gif");
        clientData = new OtClientData(tnw, "ot.editor.EdSurvey");
        SpFactory.SURVEY_CONTAINER.setClientData(clientData);

    }

    /**
     * Get the SpType of the given fully qualified className.
     *
     * This will only work if the class defines a public static variable
     * called SP_TYPE that refers to a unique, registered SpType.
     *
     * For instance, the NIRI class (ot_cfg.inst.SpInstNIRI) contains:
     *
     *     public static final SpType SP_TYPE = SpType.create(
     *         SpType.OBSERVATION_COMPONENT_TYPE,
     *         "inst.NIRI", "NIRI");
     */
    private static SpType _initClass(String className) {
        SpType spType = null;

        try {
            Class<?> c = Class.forName(className);
            Field f = c.getField("SP_TYPE");
            spType = (SpType) f.get(null); // SP_TYPE must be a static field

        } catch (Exception ex) {
            System.out.println("Problem initializing class: " + className);
            System.out.println(ex);
        }

        return spType;
    }
}
