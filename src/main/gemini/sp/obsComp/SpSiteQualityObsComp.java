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

package gemini.sp.obsComp;

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.util.Format;

/**
 * Site Quality observation component.
 */
@SuppressWarnings("serial")
public class SpSiteQualityObsComp extends SpObsComp {
    public static final String ATTR_TAU_BAND_ALLOCATED = "tauBandAllocated";
    public static final String ATTR_SEEING_ALLOCATED = "seeingAllocated";
    public static final String ATTR_SKY_ALLOCATED = "skyAllocated";
    public static final String ATTR_SEEING_MIN = "seeing.min";
    public static final String ATTR_SEEING_MAX = "seeing.max";
    public static final String ATTR_CSO_TAU_MIN = "csoTau.min";
    public static final String ATTR_CSO_TAU_MAX = "csoTau.max";
    public static final String ATTR_SKY_MIN = "skyBrightness.min";
    public static final String ATTR_SKY_MAX = "skyBrightness.max";
    public static final String ATTR_MOON = "moon";
    public static final String ATTR_CLOUD = "cloud";
    protected static final String XML_SEEING = "seeing";
    protected static final String XML_CSO_TAU = "csoTau";
    protected static final String XML_SKY = "skyBrightness";
    protected static final String XML_MOON = "moon";
    protected static final String XML_CLOUD = "cloud";
    protected static final String XML_MAX = "max";
    protected static final String XML_MIN = "min";
    private String _previousXmlElement = "";
    public static final int MOON_DARK = 0;
    public static final int MOON_GREY = 25;
    public static final int MOON_ANY = 100;

    /** Number of Moon options. */
    public static final int MOON_OPTIONS_LENGTH = 3;
    public static final int CLOUD_PHOTOMETRIC = 0;
    public static final int CLOUD_THIN_CIRRUS = 20;
    public static final int CLOUD_ANY = 100;

    /** Number of Cloud options. */
    public static final int CLOUD_OPTIONS_LENGTH = 3;
    public static final String SUBTYPE = "schedInfo";
    public static final int NO_VALUE = 0;
    public static final SpType SP_TYPE = SpType.create(
            SpType.OBSERVATION_COMPONENT_TYPE, SUBTYPE, "Site Quality");

    // Register the prototype.
    static {
        SpFactory.registerPrototype(new SpSiteQualityObsComp());
    }

    /**
     * Default constructor. Initialize the component type.
     */
    public SpSiteQualityObsComp() {
        // MFO: Changed because UKIRT and JCMT use different site quality
        // components.
        super(SP_TYPE);

        _avTable.noNotifySet(ATTR_TAU_BAND_ALLOCATED, "true", 0);
        _avTable.noNotifySet(ATTR_SEEING_ALLOCATED, "true", 0);
        _avTable.noNotifySet(ATTR_SKY_ALLOCATED, "true", 0);
    }

    /**
     * Set Seeing index.
     *
     * @param value One of {@link #SEEING_EXCELLENT}, {@link #SEEING_GOOD},
     *                     {@link #SEEING_POOR}, {@link #SEEING_ANY}.
     */

    public void setSeeingAllocated(boolean value) {
        _avTable.set(ATTR_SEEING_ALLOCATED, value);
    }

    public boolean seeingAllocated() {
        return _avTable.getBool(ATTR_SEEING_ALLOCATED);
    }

    public void setMinSeeing(double value) {
        _avTable.set(ATTR_SEEING_MIN, value);
    }

    public void setMinSeeing(String value) {
        setMinSeeing(Format.toDouble(value));
    }

    public double getMinSeeing() {
        return _avTable.getDouble(ATTR_SEEING_MIN, NO_VALUE);
    }

    public void setMaxSeeing(double value) {
        _avTable.set(ATTR_SEEING_MAX, value);
    }

    public void setMaxSeeing(String value) {
        setMaxSeeing(Format.toDouble(value));
    }

    public double getMaxSeeing() {
        return _avTable.getDouble(ATTR_SEEING_MAX, NO_VALUE);
    }

    /**
     * Set CSO Tau index.
     *
     * @param value One of {@link #CSO_TAU_VERY_DRY}, {@link #CSO_TAU_ANY}.
     */

    public void setTauBandAllocated(boolean value) {
        _avTable.set(ATTR_TAU_BAND_ALLOCATED, value);
    }

    public boolean tauBandAllocated() {
        return _avTable.getBool(ATTR_TAU_BAND_ALLOCATED);
    }

    public void setMinTau(double value) {
        _avTable.set(ATTR_CSO_TAU_MIN, value);
    }

    public void setMinTau(String value) {
        setMinTau(Format.toDouble(value));
    }

    public double getMinTau() {
        return _avTable.getDouble(ATTR_CSO_TAU_MIN, NO_VALUE);
    }

    public void setMaxTau(double value) {
        _avTable.set(ATTR_CSO_TAU_MAX, value);
    }

    public void setMaxTau(String value) {
        setMaxTau(Format.toDouble(value));
    }

    public double getMaxTau() {
        return _avTable.getDouble(ATTR_CSO_TAU_MAX, NO_VALUE);
    }

    // Sky Brightness Constraint

    public void setSkyAllocated(boolean value) {
        _avTable.set(ATTR_SKY_ALLOCATED, value);
    }

    public boolean skyAllocated() {
        return _avTable.getBool(ATTR_SKY_ALLOCATED);
    }

    public void setMinSky(double value) {
        _avTable.set(ATTR_SKY_MIN, value);
    }

    public void setMinSky(String value) {
        setMinSky(Format.toDouble(value));
    }

    public double getMinSky() {
        return _avTable.getDouble(ATTR_SKY_MIN, NO_VALUE);
    }

    public void setMaxSky(double value) {
        _avTable.set(ATTR_SKY_MAX, value);
    }

    public void setMaxSky(String value) {
        setMaxSky(Format.toDouble(value));
    }

    public double getMaxSky() {
        return _avTable.getDouble(ATTR_SKY_MAX, NO_VALUE);
    }

    /**
     * Set Moon index.
     *
     * @param moon One of {@link #MOON_DARK}, {@link #MOON_GREY},
     *             {@link #MOON_ANY}.
     */
    public void setMoon(int moon) {
        _avTable.set(ATTR_MOON, moon);
    }

    /**
     * Get Moon index.
     *
     * @return One of {@link #MOON_DARK}, {@link #MOON_GREY},
     *         {@link #MOON_ANY}.
     */
    public int getMoon() {
        return _avTable.getInt(ATTR_MOON, MOON_ANY);
    }

    /**
     * Set Cloud index.
     *
     * @param cloud One of {@link #CLOUD_PHOTOMETRIC},
     *              {@link #CLOUD_THIN_CIRRUS}, {@link #CLOUD_ANY}.
     *
     */
    public void setCloud(int cloud) {
        _avTable.set(ATTR_CLOUD, cloud);
    }

    /**
     * Get Cloud index.
     *
     * @return One of {@link #CLOUD_PHOTOMETRIC}, {@link #CLOUD_THIN_CIRRUS},
     *         {@link #CLOUD_ANY}.
     */
    public int getCloud() {
        return _avTable.getInt(ATTR_CLOUD, CLOUD_ANY);
    }

    protected void processAvAttribute(String avAttr, String indent,
            StringBuffer xmlBuffer) {
        if (avAttr.equals(ATTR_SEEING_ALLOCATED) && (!seeingAllocated())) {
            xmlBuffer.append("\n  " + indent + "<" + XML_SEEING + ">");
            xmlBuffer.append("\n    " + indent
                    + "<" + XML_MIN + ">" + getMinSeeing()
                    + "</" + XML_MIN + ">");
            xmlBuffer.append("\n    " + indent
                    + "<" + XML_MAX + ">" + getMaxSeeing()
                    + "</" + XML_MAX + ">");
            xmlBuffer.append("\n  " + indent + "</" + XML_SEEING + ">");

        } else if (avAttr.equals(ATTR_SEEING_MIN)
                || avAttr.equals(ATTR_SEEING_MAX)
                || avAttr.equals(ATTR_SEEING_ALLOCATED)) {

        } else if (avAttr.equals(ATTR_TAU_BAND_ALLOCATED)
                && (!tauBandAllocated())) {
            xmlBuffer.append("\n  " + indent + "<" + XML_CSO_TAU + ">");
            xmlBuffer.append("\n    " + indent
                    + "<" + XML_MIN + ">" + getMinTau()
                    + "</" + XML_MIN + ">");
            xmlBuffer.append("\n    " + indent
                    + "<" + XML_MAX + ">" + getMaxTau()
                    + "</" + XML_MAX + ">");
            xmlBuffer.append("\n  " + indent + "</" + XML_CSO_TAU + ">");

        } else if (avAttr.equals(ATTR_CSO_TAU_MIN)
                || avAttr.equals(ATTR_CSO_TAU_MAX)
                || avAttr.equals(ATTR_TAU_BAND_ALLOCATED)) {

        } else if (avAttr.equals(ATTR_SKY_ALLOCATED) && (!skyAllocated())) {
            xmlBuffer.append("\n  " + indent + "<" + XML_SKY + ">");
            xmlBuffer.append("\n    " + indent
                    + "<" + XML_MIN + ">" + getMinSky()
                    + "</" + XML_MIN + ">");
            xmlBuffer.append("\n    " + indent
                    + "<" + XML_MAX + ">" + getMaxSky()
                    + "</" + XML_MAX + ">");
            xmlBuffer.append("\n  " + indent + "</" + XML_SKY + ">");

        } else if (avAttr.equals(ATTR_SKY_MIN) || avAttr.equals(ATTR_SKY_MAX)
                || avAttr.equals(ATTR_SKY_ALLOCATED)) {

        } else if (avAttr.equals(ATTR_MOON)) {
            xmlBuffer.append("\n  " + indent + "<" + XML_MOON + ">");
            xmlBuffer.append("\n    " + indent
                    + "<" + XML_MIN + ">" + "0" + "</" + XML_MIN + ">");
            xmlBuffer.append("\n    " + indent
                    + "<" + XML_MAX + ">" + getMoon() + "</" + XML_MAX + ">");
            xmlBuffer.append("\n  " + indent + "</" + XML_MOON + ">");

        } else if (avAttr.equals(ATTR_CLOUD)) {
            xmlBuffer.append("\n  " + indent + "<" + XML_CLOUD + ">");
            xmlBuffer.append("\n    " + indent
                    + "<" + XML_MIN + ">" + "0" + "</" + XML_MIN + ">");
            xmlBuffer.append("\n    " + indent
                    + "<" + XML_MAX + ">" + getCloud() + "</" + XML_MAX + ">");
            xmlBuffer.append("\n  " + indent + "</" + XML_CLOUD + ">");

        } else {
            super.processAvAttribute(avAttr, indent, xmlBuffer);
        }
    }

    public void processXmlElementStart(String name) {
        if (name.equals(XML_SEEING)) {
            _previousXmlElement = name;
        } else if (name.equals(XML_CSO_TAU)) {
            _previousXmlElement = name;
        } else if (name.equals(XML_SKY)) {
            _previousXmlElement = name;
        } else if (name.equals(XML_MOON)) {
            _previousXmlElement = name;
        } else if (name.equals(XML_CLOUD)) {
            _previousXmlElement = name;
        }
    }

    public void processXmlElementContent(String name, String value) {
        try {
            if (name.equals(XML_SEEING)) {
                _previousXmlElement = name;
            } else if (name.equals(XML_CSO_TAU)) {
                _previousXmlElement = name;
            } else if (name.equals(XML_SKY)) {
                _previousXmlElement = name;
            } else if (name.equals(XML_MOON)) {
                // We are dealing with an old style program
                if (value.equals("0")) {
                    _avTable.noNotifySet(ATTR_MOON,
                            Integer.toString(MOON_DARK), 0);
                } else if (value.equals("1")) {
                    _avTable.noNotifySet(ATTR_MOON,
                            Integer.toString(MOON_GREY), 0);
                }

                _previousXmlElement = name;

            } else if (name.equals(XML_CLOUD)) {
                // We are dealing with an old style program
                if (value.equals("0")) {
                    _avTable.noNotifySet(ATTR_CLOUD,
                            Integer.toString(CLOUD_PHOTOMETRIC), 0);
                } else if (value.equals("1")) {
                    _avTable.noNotifySet(ATTR_CLOUD,
                            Integer.toString(CLOUD_THIN_CIRRUS), 0);
                }

                _previousXmlElement = name;

            } else if (name.equals(XML_MAX)) {
                if (_previousXmlElement.equals(XML_SEEING)) {
                    _avTable.noNotifySet(ATTR_SEEING_MAX, value, 0);
                    _avTable.noNotifySet(ATTR_SEEING_ALLOCATED, "false", 0);

                } else if (_previousXmlElement.equals(XML_CSO_TAU)) {
                    _avTable.noNotifySet(ATTR_CSO_TAU_MAX, value, 0);
                    _avTable.noNotifySet(ATTR_TAU_BAND_ALLOCATED, "false", 0);

                } else if (_previousXmlElement.equals(XML_SKY)) {
                    _avTable.noNotifySet(ATTR_SKY_MAX, value, 0);
                    _avTable.noNotifySet(ATTR_SKY_ALLOCATED, "false", 0);

                } else if (_previousXmlElement.equals(XML_MOON)) {
                    _avTable.noNotifySet(ATTR_MOON, value, 0);

                } else if (_previousXmlElement.equals(XML_CLOUD)) {
                    _avTable.noNotifySet(ATTR_CLOUD, value, 0);
                }
            } else if (name.equals(XML_MIN)) {
                if (_previousXmlElement.equals(XML_SEEING)) {
                    _avTable.noNotifySet(ATTR_SEEING_MIN, value, 0);

                } else if (_previousXmlElement.equals(XML_CSO_TAU)) {
                    _avTable.noNotifySet(ATTR_CSO_TAU_MIN, value, 0);

                } else if (_previousXmlElement.equals(XML_SKY)) {
                    _avTable.noNotifySet(ATTR_SKY_MIN, value, 0);
                }
            } else {
                super.processXmlElementContent(name, value);
            }
        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException(
                    "Problem parsing Site Quality component (XML): " + e);
        }
    }

    public void processXmlElementEnd(String name) {
        if (name == _previousXmlElement) {
            _previousXmlElement = "";
        }
    }
}
