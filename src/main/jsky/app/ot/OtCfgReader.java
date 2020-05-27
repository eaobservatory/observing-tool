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

import gemini.util.ObservingToolUtilities;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.URL;

import java.util.StringTokenizer;
import java.util.Vector;

import jsky.app.ot.OtCfg.SpItemCfg;
import jsky.app.ot.OtCfg.TpeFeatureCfg;
import jsky.util.gui.DialogUtil;

/**
 * Used to read the OT configuration file.
 */
class OtCfgReader {
    public static final String GUIDE_TAG = "guide";

    // MFO, 19 December 2001
    public static final String BASE_TAG = "base";
    public static final String PHASE1_TAG = "phase1";
    public static final String TPE_FEATURE_TAG = "tpe feature";
    public static final String TPE_TYPE_TAG = "tpe type";
    public static final String CLASS_TAG = "class";
    public static final String EDITOR_TAG = "editor";
    public static final String FEATURE_TAG = "img feature";
    public static final String LIBRARY_TAG = "library";
    public static final String TELESCOPE_UTIL_TAG = "telescope util";
    public static final String NAME_RESOLVERS_TAG = "name resolvers";
    public static final String CHOP_DEFAULTS = "chop defaults";

    /** Targets which are known by the TCS by name. E.g. planets. */
    public static final String NAMED_TARGETS = "named targets";

    /** Telescope latitude. Can be used for noise calculations etc. */
    public static final String TELESCOPE_LATITUDE = "telescope latitude";

    public static final String SCHEMA_BASE = "schemaBase";
    public static final String SCHEMA_LOCATION = "schemaLocation";
    public static final String SCHEMA_URL = "schemaURL";
    public static final String PROXY_SERVER = "Proxy Server";
    private static final String PROP_PROXY_SERVER = "http.proxyHost";
    public static final String PROXY_PORT = "Proxy Port";
    private static final String PROP_PROXY_PORT = "http.proxyPort";
    public static final String NOTE_LABELS = "Labels";
    public static final String NOTE_TAGS = "Tags";
    public static final String NOTE_EXAMPLES = "Examples";
    public static final String HEDWIG_OAUTH_URL = "hedwigOAuthURL";
    public static final String HEDWIG_OAUTH_CLIENT = "hedwigOAuthClient";

    /**
     * Read the configuration file from the given base URL and file name.
     *
     * @return The OtCfg.Info structure containing the parsed information
     *         from the ot.cfg file.
     */
    public static OtCfg.Info load(String cfgFilename) {
        OtCfg.Info info = null;
        URL url = ObservingToolUtilities.resourceURL(cfgFilename);

        if (url != null) {
            try {
                info = load(url.openStream());
            } catch (IOException ex) {
                DialogUtil.error("Problem reading the config file: " + ex);
            }
        } else {
            DialogUtil.error("Problem constructing the config file URL: "
                    + cfgFilename);
        }

        return info;
    }

    /**
     * Read the configuration file from the given input stream.
     *
     * @return The OtCfg.Info structure containing the parsed information
     *         from the ot.cfg file.
     */
    public static OtCfg.Info load(InputStream is) throws IOException {
        OtCfg.Info info = new OtCfg.Info();

        // For add-on TpeImageFeatures
        Vector<TpeFeatureCfg> tpeFeatureV = new Vector<TpeFeatureCfg>();

        // For add-on SpItems
        Vector<SpItemCfg> spItemV = new Vector<SpItemCfg>();

        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(is));
            String line;

            OtCfg.TpeFeatureCfg tfc = null;
            OtCfg.SpItemCfg sic = null;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || (line.length() == 0)) {
                    continue;
                }

                // Guide Star Tags
                if (line.startsWith(GUIDE_TAG)) {
                    info.guideTags = _parseCommaList(_getValue(line));

                } else if (line.startsWith(PHASE1_TAG)) {
                    // Phase1 Document Generator
                    info.phase1Class = _getValue(line);

                } else if (line.startsWith(TPE_FEATURE_TAG)) {
                    // TpeImageFeature add-ons
                    if (tfc != null) {
                        tpeFeatureV.addElement(tfc);
                    }

                    tfc = new OtCfg.TpeFeatureCfg();
                    tfc.featClass = _getValue(line);

                } else if (line.startsWith(TPE_TYPE_TAG)) {
                    if (tfc != null) {
                        tfc.type = _getValue(line);
                    }
                } else if (line.startsWith(CLASS_TAG)) {
                    // SpItem add-ons
                    if (sic != null) {
                        spItemV.addElement(sic);
                    }

                    sic = new OtCfg.SpItemCfg();
                    sic.spClass = _getValue(line);

                } else if (line.startsWith(EDITOR_TAG)) {
                    if (sic != null) {
                        sic.editorClass = _getValue(line);
                    }
                } else if (line.startsWith(FEATURE_TAG)) {
                    if (sic != null) {
                        sic.imgFeatureClass = _getValue(line);
                    }
                } else if (line.startsWith(LIBRARY_TAG)) {
                    // Libraries (AB added 1-Aug-00)
                    info.libraryTags = _parseCommaList(_getValue(line));

                } else if (line.startsWith(TELESCOPE_UTIL_TAG)) {
                    // telescope utility class (added by MFO, 10 January 2002)
                    info.telescopeUtilClass = _getValue(line);

                } else if (line.startsWith(NAME_RESOLVERS_TAG)) {
                    // name resolvers (added by MFO, May 30, 2001)
                    info.nameResolvers = _parseCommaList(_getValue(line));

                } else if (line.startsWith(CHOP_DEFAULTS)) {
                    // chop defaults (added by MFO, May 13, 2002)
                    info.chopDefaults = _parseCommaList(_getValue(line));

                } else if (line.startsWith(NAMED_TARGETS)) {
                    // named targets (added by MFO, June 05, 2002)
                    info.namedTargets = _parseCommaList(_getValue(line));

                } else if (line.startsWith(TELESCOPE_LATITUDE)) {
                    // Telescope latitude (added by MFO, June 13, 2002)
                    info.telescopeLatitude = _getValue(line);

                } else if (line.startsWith(SCHEMA_BASE)) {
                    info.schemaBase = _getValue(line);

                } else if (line.startsWith(SCHEMA_URL)) {
                    info.schemaURL = _getValue(line);

                } else if (line.startsWith(SCHEMA_LOCATION)) {
                    info.schemaLocation = _getValue(line);

                } else if (line.startsWith(PROXY_SERVER)) {
                    info.proxyServer = _getValue(line);

                    if (info.proxyServer != null
                            && info.proxyServer.length() != 0) {
                        System.setProperty(PROP_PROXY_SERVER, info.proxyServer);
                    }
                } else if (line.startsWith(PROXY_PORT)) {
                    info.proxyPort = _getValue(line);

                    if (info.proxyPort != null
                            && info.proxyPort.length() != 0) {
                        System.setProperty(PROP_PROXY_PORT, info.proxyPort);
                    }
                } else if (line.startsWith(NOTE_LABELS)) {
                    info.noteLabels = _parseCommaList(_getValue(line));

                } else if (line.startsWith(NOTE_TAGS)) {
                    info.noteTags = _parseCommaList(_getValue(line));

                } else if (line.startsWith(NOTE_EXAMPLES)) {
                    info.noteExamples = _parseCommaList(_getValue(line));

                } else if (line.startsWith(HEDWIG_OAUTH_URL)) {
                    info.hedwigOAuthURL = _getValue(line);

                } else if (line.startsWith(HEDWIG_OAUTH_CLIENT)) {
                    info.hedwigOAuthClient = _getValue(line);
                }
            }

            // Add the last config class
            if (tfc != null) {
                tpeFeatureV.addElement(tfc);
            }

            if (sic != null) {
                spItemV.addElement(sic);
            }

        } finally {
            if (br != null) {
                br.close();
            }

            if (is != null) {
                is.close();
            }
        }

        // SdW - Add a check to see if a personal nameresolver exists.
        if (System.getProperty("OT_CATALOG_FILE") != null) {
            File cFile = new File(System.getProperty("OT_CATALOG_FILE"));

            if (cFile.exists() && cFile.isFile() && cFile.canRead()) {
                String oldNames[] = info.nameResolvers;
                String[] newNames = new String[oldNames.length + 1];
                newNames = oldNames;
                newNames[newNames.length - 1] = "Personal Catalog";
                info.nameResolvers = newNames;
            }
        }
        // END

        // Copy all the TpeImageFeature configurations into an array in the
        // OtCfg.Info return class.
        info.tpeFeatureCfgA = new OtCfg.TpeFeatureCfg[tpeFeatureV.size()];
        tpeFeatureV.copyInto(info.tpeFeatureCfgA);

        // Copy all the SpItem configurations into an array in the
        // OtCfg.Info return class.
        info.spItemCfgA = new OtCfg.SpItemCfg[spItemV.size()];
        spItemV.copyInto(info.spItemCfgA);

        return info;
    }

    /**
     * Get the value part of an "attribute: value" line of the config file.
     */
    private static String _getValue(String line) {
        // Get the value
        int i = line.indexOf(':');

        if (i == -1) {
            return null;
        }

        return line.substring(i + 1).trim();
    }

    /**
     * Break a comma separated list of values into an array of strings.
     */
    private static String[] _parseCommaList(String list) {
        StringTokenizer st = new StringTokenizer(list, ",", false);
        String[] values = new String[st.countTokens()];
        int i = 0;

        while (st.hasMoreTokens()) {
            values[i++] = st.nextToken().trim();
        }

        return values;
    }
}
