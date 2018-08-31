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

import gemini.sp.SpItem;
import gemini.sp.SpAvTable;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTelescopePosList;
import gemini.sp.SpType;
import gemini.sp.SpObsData;
import gemini.sp.SpSurveyContainer;
import gemini.util.CoordSys;

import java.util.Enumeration;

/**
 * A class for telescope observation component items.
 *
 * Maintains a position list and keeps up-to-date the base position element
 * of the observation data for the observation context.
 *
 * @see gemini.sp.SpTelescopePosList
 * @see gemini.sp.SpObsData
 * @see gemini.sp.SpObsContextItem
 */
@SuppressWarnings("serial")
public class SpTelescopeObsComp extends SpObsComp {
    /** TCS XML constants. */
    private static final String TX_BASE = "BASE";
    private static final String TX_TYPE = "TYPE";
    private static final String TX_TARGET = "target";
    private static final String TX_TARGET_NAME = "targetName";
    private static final String TX_SPHERICAL_SYSTEM = "spherSystem";
    private static final String TX_CONIC_SYSTEM = "conicSystem";
    private static final String TX_NAMED_SYSTEM = "namedSystem";
    private static final String TX_TLE_SYSTEM = "tleSystem";
    private static final String TX_SYSTEM = "SYSTEM";
    private static final String TX_CONIC_NAMED_TYPE = "TYPE";
    private static final String TX_OLD_CONIC_NAMED_TYPE = "type";
    private static final String TX_C1 = "c1";
    private static final String TX_C2 = "c2";
    private static final String TX_OFFSET = "OFFSET";
    private static final String TX_DC1 = "DC1";
    private static final String TX_DC2 = "DC2";

    /** TCS XML constants: Orbital elements. */
    private static final String TX_EPOCH = "epoch";
    private static final String TX_EPOCH_PERIH = "epochPerih";
    private static final String TX_INCLINATION = "inclination";
    private static final String TX_ANODE = "anode";
    private static final String TX_PERIHELION = "perihelion";
    private static final String TX_AORQ = "aorq";
    private static final String TX_E = "e";
    private static final String TX_LORM = "LorM";
    private static final String TX_N = "n";

    /** TCS XML constants: TLE. */
    private static final String TX_TLE_EPOCH_YEAR = "epochYr";
    private static final String TX_TLE_EPOCH_DAY = "epochDay";
    private static final String TX_TLE_INCLINATION = "inclination";
    private static final String TX_TLE_RAANODE = "raanode";
    private static final String TX_TLE_PERIGEE = "perigee";
    private static final String TX_TLE_E = "e";
    private static final String TX_TLE_MEAN_ANOMALY = "LorM";
    private static final String TX_TLE_MEAN_MOTION = "mm";
    private static final String TX_TLE_BSTAR = "bstar";

    /** TCS XML constant: proper motion, x axis. */
    private static final String TX_PM1 = "pm1";

    /** TCS XML constant: proper motion, y axis. */
    private static final String TX_PM2 = "pm2";

    /** TCS XML constant: Radial Velocity. */
    private static final String TX_RV = "rv";
    private static final String TX_RV_DEFN = "defn";
    private static final String TX_RV_FRAME = "frame";
    private static final String TX_PARALLAX = "parallax";
    private static final String TX_TYPE_SCIENCE = "SCIENCE";
    private static final String TX_J2000 = "J2000";
    private static final String TX_B1950 = "B1950";
    private static final String TX_AZEL = "AZEL";

    // CHOP MODE parameters (added by MFO, 6 August 2001)
    public static final String ATTR_CHOPPING = "chopping";
    public static final String ATTR_CHOP_THROW = "chopThrow";
    public static final String ATTR_CHOP_ANGLE = "chopAngle";
    public static final String ATTR_CHOP_SYSTEM = "chopSystem";

    /** @see #getFitsKey(int) */
    public static final String ATTR_FITS_KEY = "fitsKey";

    /** @see #getFitsValue(int) */
    public static final String ATTR_FITS_VALUE = "fitsValue";

    /** @see #getPositionInTile() */
    public static final String ATTR_POSITION_IN_TILE = "positionInTile";

    /** @see #getPositionInTile() */
    public static final int NOT_IN_TILE = -1;
    protected SpTelescopePosList _posList;

    /** Needed for XML parsing. */
    private SpTelescopePos _currentPosition = null;

    /**
     * @see #getSurveyComponent()
     */
    private SpSurveyContainer _surveyComp = null;

    /**
     * If this SpTelescopeObsComp is contained in a SpSurveyObsComp then it
     * needs its own SpObsData object because it does not share its base
     * position with the other SpTelescopeObsComp items in the SpSurveyObsComp.
     */
    private SpObsData _obsData = null;

    public SpTelescopeObsComp() {
        super(SpType.OBSERVATION_COMPONENT_TARGET_LIST);

        _init();
    }

    /**
     * Initialize the position list.
     */
    protected void _init() {
        _posList = null;

        SpTelescopePos.createDefaultBasePosition(_avTable);
    }

    /**
     * Override clone to make sure the position list is correctly initialized.
     */
    public SpTelescopeObsComp clone() {
        SpTelescopeObsComp toc = (SpTelescopeObsComp) super.clone();

        toc._posList = null;

        return toc;
    }

    /**
     * Override getTitle to return the name of the base position if set.
     */
    public String getTitle() {
        // By default, append the name of the base position.
        // If a title has been directly set though, use that instead.
        String titleAttr = getTitleAttr();

        if ((titleAttr == null) || titleAttr.equals("")) {
            SpTelescopePosList tpl = getPosList();

            SpTelescopePos tp = tpl.getBasePosition();
            if (tp == null) {
                return super.getTitle();
            }

            String name = tp.getName();
            if ((name == null) || name.equals("")) {
                return super.getTitle();
            }

            return type().getReadable() + ": " + name;

        } else {
            return super.getTitle();
        }
    }

    /**
     * Get a position list data structure for the telescope positions in the
     * attribute table.
     */
    public SpTelescopePosList getPosList() {
        if (_posList == null) {
            _posList = new SpTelescopePosList(this);
        }

        return _posList;
    }

    /**
     * Override setTable to update the position list, and to change the base
     * position associated with the context this item is in.
     *
     * @see gemini.sp.SpObsData
     * @see gemini.sp.SpObsContextItem
     */
    protected void setTable(SpAvTable avTab) {
        super.setTable(avTab);

        // NOTE this will reset the base position in the obs data.
        if (_posList != null) {
            _posList.setTable(avTab);
        }
    }

    /**
     * Override setTable to update the position list, and to change the base
     * position associated with the context this item is in.
     *
     * @see gemini.sp.SpObsData
     * @see gemini.sp.SpObsContextItem
     */
    protected void replaceTable(SpAvTable avTab) {
        super.replaceTable(avTab);

        // NOTE this will reset the base position in the obs data.
        if (_posList != null) {
            _posList.setTable(avTab);
        }
    }

    /**
     * Get the FITS key at the given index.
     *
     * @param index Index of one of the FITS key/value pairs.
     *
     * @see #setFitsValue(String, int)
     */
    public String getFitsKey(int index) {
        return _avTable.get(ATTR_FITS_KEY, index);
    }

    /**
     * Set the FITS key at the given index.
     *
     * @param index Index of one of the FITS key/value pairs.
     *
     * @see #setFitsValue(String, int)
     */
    public void setFitsKey(String fitsKey, int index) {
        _avTable.set(ATTR_FITS_KEY, fitsKey, index);
    }

    /**
     * Get the FITS value at the given index.
     *
     * @param index Index of one of the FITS key/value pairs.
     *
     * @see #setFitsKey(String, int)
     */
    public String getFitsValue(int index) {
        return _avTable.get(ATTR_FITS_VALUE, index);
    }

    /**
     * Set the FITS value at the given index.
     *
     * @param index Index of one of the FITS key/value pairs.
     *
     * @see #setFitsKey(String, int)
     */
    public void setFitsValue(String fitsValue, int index) {
        _avTable.set(ATTR_FITS_VALUE, fitsValue, index);
    }

    /**
     * Get the number of FITS key/value pairs.
     *
     * The maximum index for the methods
     *
     * <ul>
     * <li>{@link #getFitsKey(int)}
     * <li>{@link #setFitsKey(String,int)}
     * <li>{@link #getFitsValue(int)}
     * <li>{@link #setFitsValue(String,int)}
     * </ul>
     *
     * is <tt>getFitsCount() - 1</tt>.
     *
     * @see #getFitsKey(int)
     * @see #getFitsValue(int)
     */
    public int getFitsCount() {
        return _avTable.size(ATTR_FITS_KEY);
    }

    /**
     * Returns the position of this target in a tile if this SpTelescopePosList
     * was created as part of a tile by the survey definition tool.
     *
     * Pointings created by the survey definition tool and belonging to the
     * same tile are numbered through from 0 to n-1 where n is the number of
     * pointings in the tile. E.g. in a standard tile containing 2 X 2
     * pointings this method returns
     *
     * <pre>
     * &quot;lower right&quot; pointing in tile: getPositionInTile() returns 0
     * &quot;upper right&quot; pointing in tile: getPositionInTile() returns 1
     * &quot;lower left&quot;  pointing in tile: getPositionInTile() returns 2
     * &quot;upper left&quot;  pointing in tile: getPositionInTile() returns 3
     * </pre>
     *
     * If this SpTelescopeObsComp was not created as part of a tile then
     * {@link #NOT_IN_TILE} is returned.
     * <p>
     *
     * The number returned by this method is <b>not</b> the <tt>TILENUM</tt>
     * in the FITS header. The <tt>TILENUM</tt> in the FITS header comes from
     * the DHS. It is incremented through the "startTile" command in the EXEC
     * file which in turn is inserted into the EXEC by the translator when a
     * SpTelescopeObsComp with getPositionInTile() == 0 is encountered.
     * <p>
     *
     * Example:
     *
     * <pre>
     * &quot;lower right&quot; pointing in new tile: getPositionInTile() returns 0 =&gt; &quot;startTile&quot; =&gt; TILENUM == 1, say
     * &quot;upper right&quot; pointing in     tile: getPositionInTile() returns 1 =&gt;             =&gt; TILENUM == 1
     * &quot;lower left&quot;  pointing in     tile: getPositionInTile() returns 2 =&gt;             =&gt; TILENUM == 1
     * &quot;upper left&quot;  pointing in     tile: getPositionInTile() returns 3 =&gt;             =&gt; TILENUM == 1
     * &quot;lower right&quot; pointing in new tile: getPositionInTile() returns 0 =&gt; &quot;startTile&quot; =&gt; TILENUM == 2
     * &quot;upper right&quot; pointing in     tile: getPositionInTile() returns 1 =&gt;             =&gt; TILENUM == 2
     * &quot;lower left&quot;  pointing in     tile: getPositionInTile() returns 2 =&gt;             =&gt; TILENUM == 2
     * &quot;upper left&quot;  pointing in     tile: getPositionInTile() returns 3 =&gt;             =&gt; TILENUM == 2
     * &quot;lower right&quot; pointing in new tile: getPositionInTile() returns 0 =&gt; &quot;startTile&quot; =&gt; TILENUM == 3
     * &quot;upper right&quot; pointing in     tile: getPositionInTile() returns 1 =&gt;             =&gt; TILENUM == 3
     * &quot;lower left&quot;  pointing in     tile: getPositionInTile() returns 2 =&gt;             =&gt; TILENUM == 3
     * &quot;upper left&quot;  pointing in     tile: getPositionInTile() returns 3 =&gt;             =&gt; TILENUM == 3
     * ...
     * </pre>
     *
     *
     */
    public int getPositionInTile() {
        return _avTable.getInt(ATTR_POSITION_IN_TILE, NOT_IN_TILE);
    }

    /**
     * @see #getPositionInTile()
     */
    public void setPositionInTile(int positionInTile) {
        _avTable.set(ATTR_POSITION_IN_TILE, positionInTile);
    }

    /**
     * Get chopping on / off.
     *
     * Added by MFO (6 August 2001)
     */
    public boolean isChopping() {
        return _avTable.getBool(ATTR_CHOPPING);
    }

    /**
     * Set chopping on / off.
     *
     * Added by MFO (6 August 2001)
     */
    public void setChopping(boolean choppingOn) {
        _avTable.set(ATTR_CHOPPING, choppingOn);

        // MFO TODO: _notifyOfGenericUpdate or equivalent has to be implemented
        // (for TelescopesPosWatcher or TelescopePosListWatcher).
        // _notifyOfGenericUpdate();
    }

    /**
     * Get the chop throw as String.
     *
     * Added by MFO (6 August 2001)
     */
    public String getChopThrowAsString() {
        String res = _avTable.get(ATTR_CHOP_THROW);

        if (res == null) {
            res = "0.0";
        }

        return res;
    }

    /**
     * Get the chop throw.
     *
     * Added by MFO (6 August 2001)
     */
    public double getChopThrow() {
        return Double.valueOf(getChopThrowAsString());
    }

    /**
     * Set the chop throw as a string.
     *
     * Added by MFO (6 August 2001)
     */
    public void setChopThrow(String chopThrowStr) {
        _avTable.set(ATTR_CHOP_THROW, chopThrowStr);

        // MFO TODO: _notifyOfGenericUpdate or equivalent has to be implemented
        // (for TelescopesPosWatcher or TelescopePosListWatcher).
        // _notifyOfGenericUpdate();
    }

    /**
     * Get the chop angle as string.
     *
     * Added by MFO (6 August 2001)
     */
    public String getChopAngleAsString() {
        String res = _avTable.get(ATTR_CHOP_ANGLE);

        if (res == null) {
            res = "0.0";
        }

        return res;
    }

    /**
     * Get the chop angle.
     *
     * Added by MFO (6 August 2001)
     */
    public double getChopAngle() {
        return Double.valueOf(getChopAngleAsString());
    }

    /**
     * Set the chop angle as a string.
     *
     * Added by MFO (6 August 2001)
     */
    public void setChopAngle(String chopAngleStr) {
        _avTable.set(ATTR_CHOP_ANGLE, chopAngleStr);

        // MFO TODO: _notifyOfGenericUpdate or equivalent has to be implemented
        // (for TelescopesPosWatcher or TelescopePosListWatcher).
        // _notifyOfGenericUpdate();
    }

    /**
     * Get chop system.
     *
     * Added by MFO (29 October 2001)
     */
    public String getChopSystem() {
        return _avTable.get(ATTR_CHOP_SYSTEM);
    }

    /**
     * Set chop system.
     *
     * Added by MFO (29 October 2001)
     */
    public void setChopSystem(String chopSystem) {
        _avTable.set(ATTR_CHOP_SYSTEM, chopSystem);

        // MFO TODO: _notifyOfGenericUpdate or equivalent has to be implemented
        // (for TelescopesPosWatcher or TelescopePosListWatcher).
        // _notifyOfGenericUpdate();
    }

    public String writeTCSXML() {
        StringBuffer xmlString = new StringBuffer(
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        xmlString.append("<OCS_CONFIG>");
        xmlString.append("\n  <TCS_CONFIG>");
        String indent = "  ";

        Enumeration<String> avAttributes = _avTable.attributes();

        while (avAttributes.hasMoreElements()) {
            String currAttr = avAttributes.nextElement();

            if (!currAttr.startsWith(".")) {
                this.processAvAttribute(currAttr, indent, xmlString);
            }
        }

        xmlString.append("\n  </TCS_CONFIG>");
        xmlString.append("\n</OCS_CONFIG>");

        return xmlString.toString();
    }

    /**
     * If this SpTelescopeObsComp is contained in a SpSurveyObsComp than this
     * method returns the SpObsData associated with the SpSurveyObsComp.
     *
     * Otherwise see {@link gemini.sp.SpItem#getObsData()}.
     */
    public SpObsData getObsData() {
        if (_surveyComp == null) {
            return super.getObsData();
        }

        return _obsData;
    }

    /**
     * The the parent of the item or, if this SpTelescopeObsComp is contained
     * in an SpSurveyObsComp, the patent of the SpSurveyObsComp.
     */
    public SpItem parent() {
        if (_parent == null) {
            if (_surveyComp != null) {
                return _surveyComp.parent();
            }
        }

        return _parent;
    }

    /**
     * If this SpTelescopeObsComp is contained in a SpSurveyObsComp than this
     * method returns to that SpSurveyObsComp.
     *
     * It returns null otherwise.
     */
    public SpSurveyContainer getSurveyComponent() {
        return _surveyComp;
    }

    /**
     * @see #getSurveyComponent()
     */
    public void setSurveyComponent(SpSurveyContainer surveyComp) {
        _surveyComp = surveyComp;
        _obsData = new SpObsData();
    }

    /**
     * This method creates JAC TCS XML from those attributes of the SpAvTable
     * that equal a target tag such as BASE, SCIENCE, GUIDE, REFERENCE.
     *
     * For the processing of all other SpAvTable attributes the
     * processAvAttribute method of the super class is used.
     */
    protected void processAvAttribute(String avAttr, String indent,
            StringBuffer xmlBuffer) {
        SpTelescopePos tp = null;

        // Check whether avAttr is a telescope position.
        if (avAttr != null) {
            tp = (SpTelescopePos) getPosList().getPosition(avAttr);
        }

        // If avAttr is not a telescope position then let the super class deal
        // with it and return.
        if (tp == null) {
            super.processAvAttribute(avAttr, indent, xmlBuffer);

            return;
        }

        xmlBuffer.append("\n  " + indent
                + "<" + TX_BASE + " " + TX_TYPE + "=\"" + avAttr + "\">");
        xmlBuffer.append("\n    " + indent
                + "<" + TX_TARGET + ">");
        xmlBuffer.append("\n      " + indent
                + "<" + TX_TARGET_NAME + ">" + tp.getName()
                + "</" + TX_TARGET_NAME + ">");

        // If the telescope position currently written to XML (tp) is an offset
        // position then targetPos is set to the base postion (OT speak) aka
        // SCIENCE target (TCS XML speak).  Otherwise targetPos is just set to
        // telescope position currently written to XML (tp).
        SpTelescopePos targetPos = tp;
        if (tp.isOffsetPosition()) {
            targetPos = getPosList().getBasePosition();
        }

        String system = targetPos.getCoordSysAsString();

        if (CoordSys.COORD_SYS[CoordSys.FK5].equals(system)) {
            system = TX_J2000;
        } else if (CoordSys.COORD_SYS[CoordSys.FK4].equals(system)) {
            system = TX_B1950;
        } else if (CoordSys.COORD_SYS[CoordSys.AZ_EL].equals(system)) {
            system = TX_AZEL;
        }

        // Check whether it is a spherical system. Only the spherical
        // systems are saved using their tag name as attribute name for the
        // av table.
        switch (targetPos.getSystemType()) {
            // Conic system (orbital elements)
            case SpTelescopePos.SYSTEM_CONIC:
                xmlBuffer.append("\n      " + indent
                        + "<" + TX_CONIC_SYSTEM + " "
                        + TX_CONIC_NAMED_TYPE + "=\""
                        + targetPos.getConicOrNamedType()
                        + "\">");
                xmlBuffer.append("\n        " + indent +
                        "<" + TX_EPOCH + ">"
                        + targetPos.getConicSystemEpoch()
                        + "</" + TX_EPOCH + ">");

                if (targetPos.getConicOrNamedType().equals(
                        SpTelescopePos.CONIC_SYSTEM_TYPES[
                                SpTelescopePos.TYPE_COMET])) {
                    xmlBuffer.append("\n        " + indent
                            + "<" + TX_EPOCH_PERIH + ">"
                            + targetPos.getConicSystemEpochPerih()
                            + "</" + TX_EPOCH_PERIH + ">");
                } else {
                    xmlBuffer.append("\n        " + indent
                            + "<" + TX_LORM + ">" + targetPos.getConicSystemLorM()
                            + "</" + TX_LORM + ">");
                }

                xmlBuffer.append("\n        " + indent
                        + "<" + TX_INCLINATION + ">"
                        + targetPos.getConicSystemInclination()
                        + "</" + TX_INCLINATION + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_ANODE + ">"
                        + targetPos.getConicSystemAnode()
                        + "</" + TX_ANODE + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_PERIHELION + ">"
                        + targetPos.getConicSystemPerihelion()
                        + "</" + TX_PERIHELION + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_AORQ + ">"
                        + targetPos.getConicSystemAorQ()
                        + "</" + TX_AORQ + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_E + ">"
                        + targetPos.getConicSystemE()
                        + "</" + TX_E + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_N + ">"
                        + targetPos.getConicSystemDailyMotion()
                        + "</" + TX_N + ">");
                xmlBuffer.append("\n      " + indent
                        + "</" + TX_CONIC_SYSTEM + ">");
                break;

            // TLE system (space junk)
            case SpTelescopePos.SYSTEM_TLE:
                xmlBuffer.append("\n      " + indent
                        + "<" + TX_TLE_SYSTEM + ">");

                xmlBuffer.append("\n        " + indent
                        + "<" + TX_TLE_EPOCH_YEAR + ">"
                        + targetPos.getTleSystemEpochYear()
                        + "</" + TX_TLE_EPOCH_YEAR + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_TLE_EPOCH_DAY + ">"
                        + targetPos.getTleSystemEpochDay()
                        + "</" + TX_TLE_EPOCH_DAY + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_TLE_INCLINATION + ">"
                        + targetPos.getTleSystemInclination()
                        + "</" + TX_TLE_INCLINATION + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_TLE_RAANODE + ">"
                        + targetPos.getTleSystemRaANode()
                        + "</" + TX_TLE_RAANODE + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_TLE_PERIGEE + ">"
                        + targetPos.getTleSystemPerigee()
                        + "</" + TX_TLE_PERIGEE + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_TLE_E + ">"
                        + targetPos.getTleSystemE()
                        + "</" + TX_TLE_E + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_TLE_MEAN_ANOMALY + ">"
                        + targetPos.getTleSystemMeanAnomaly()
                        + "</" + TX_TLE_MEAN_ANOMALY + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_TLE_MEAN_MOTION + ">"
                        + targetPos.getTleSystemMeanMotion()
                        + "</" + TX_TLE_MEAN_MOTION + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_TLE_BSTAR + ">"
                        + targetPos.getTleSystemBStar()
                        + "</" + TX_TLE_BSTAR + ">");

                xmlBuffer.append("\n      " + indent
                        + "</" + TX_TLE_SYSTEM + ">");

                break;

            // Named system (planet, sun, moon etc.)
            case SpTelescopePos.SYSTEM_NAMED:
                xmlBuffer.append("\n      " + indent
                        + "<" + TX_NAMED_SYSTEM + " "
                        + TX_CONIC_NAMED_TYPE + "=\""
                        + targetPos.getConicOrNamedType()
                        + "\"/>");
                break;

            // Default: Sperical system (HMSDEG or DEGDEG: RA/Dec, Az/El etc.)
            default:
                xmlBuffer.append("\n      " + indent
                        + "<" + TX_SPHERICAL_SYSTEM + " "
                        + TX_SYSTEM + "=\"" + system + "\">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_C1 + ">"
                        + targetPos.getRealXaxisAsString()
                        + "</" + TX_C1 + ">");
                xmlBuffer.append("\n        " + indent
                        + "<" + TX_C2 + ">"
                        + targetPos.getRealYaxisAsString()
                        + "</" + TX_C2 + ">");

                double pm1 = 0.0;
                double pm2 = 0.0;
                double parallax = 0.0;
                double trackingEpoch = 0.0;

                try {
                    pm1 = Double.parseDouble(targetPos.getPropMotionRA())
                            / 1000.0;
                } catch (Exception e) {
                    // ignore
                }

                try {
                    pm2 = Double.parseDouble(targetPos.getPropMotionDec())
                            / 1000.0;
                } catch (Exception e) {
                    // ignore
                }

                try {
                    trackingEpoch = Double.parseDouble(
                            targetPos.getTrackingEpoch());
                } catch (Exception e) {
                }

                if ((pm1 != 0.0) || (pm2 != 0.0)) {
                    xmlBuffer.append("\n        " + indent
                            + "<" + TX_PM1 + ">" + pm1 + "</" + TX_PM1 + ">");
                    xmlBuffer.append("\n        " + indent
                            + "<" + TX_PM2 + ">" + pm2 + "</" + TX_PM2 + ">");
                    xmlBuffer.append("\n        " + indent
                            + "<" + TX_EPOCH + ">" + trackingEpoch
                            + "</" + TX_EPOCH + ">");
                }

                try {
                    double rv = Double.parseDouble(
                            targetPos.getTrackingRadialVelocity());
                    String rvDefn = targetPos.getTrackingRadialVelocityDefn();
                    String rvFrame = targetPos.getTrackingRadialVelocityFrame();

                    xmlBuffer.append("\n        " + indent
                            + "<" + TX_RV + " "
                            + TX_RV_DEFN + "=\"" + rvDefn + "\"" + " "
                            + TX_RV_FRAME + "=\"" + rvFrame + "\"" + ">"
                            + rv
                            + "</" + TX_RV + ">");
                } catch (Exception e) {
                    // ignore
                }

                try {
                    parallax = Double.parseDouble(
                            targetPos.getTrackingParallax());
                } catch (Exception e) {
                }

                if (parallax != 0.0) {
                    xmlBuffer.append("\n        " + indent
                            + "<" + TX_PARALLAX + ">" + parallax
                            + "</" + TX_PARALLAX + ">");
                }

                xmlBuffer.append("\n      " + indent
                        + "</" + TX_SPHERICAL_SYSTEM + ">");

                break;
        }

        xmlBuffer.append("\n    " + indent + "</" + TX_TARGET + ">");

        if (tp.isOffsetPosition()) {
            String offsetSystem = tp.getCoordSysAsString();

            if (offsetSystem.equals(CoordSys.COORD_SYS[CoordSys.FK5])) {
                offsetSystem = TX_J2000;
            }

            if (offsetSystem.equals(CoordSys.COORD_SYS[CoordSys.FK4])) {
                offsetSystem = TX_B1950;
            }

            xmlBuffer.append("\n    " + indent
                    + "<" + TX_OFFSET + " "
                    + TX_SYSTEM + "=\"" + offsetSystem
                    + "\">");

            if (tp.isBasePosition()) {
                xmlBuffer.append("\n      " + indent
                        + "<" + TX_DC1 + ">" + tp.getBaseXOffset()
                        + "</" + TX_DC1 + ">");
                xmlBuffer.append("\n      " + indent
                        + "<" + TX_DC2 + ">" + tp.getBaseYOffset()
                        + "</" + TX_DC2 + ">");

            } else {
                xmlBuffer.append("\n      " + indent
                        + "<" + TX_DC1 + ">" + tp.getXaxisAsString()
                        + "</" + TX_DC1 + ">");
                xmlBuffer.append("\n      " + indent
                        + "<" + TX_DC2 + ">" + tp.getYaxisAsString()
                        + "</" + TX_DC2 + ">");
            }

            xmlBuffer.append("\n    " + indent + "</" + TX_OFFSET + ">");
        }

        xmlBuffer.append("\n  " + indent + "</" + TX_BASE + ">");
    }

    /**
     * XML parsing handler for the start of elements.
     */
    public void processXmlElementStart(String name) {
        if (name.equals(TX_TLE_SYSTEM)) {
            // The TLE system doesn't have any attributes so the
            // "normal" XML processing methods don't seem to be
            // being called.  Therefore also check for it in the
            // element start method.
            _currentPosition.setSystemType(SpTelescopePos.SYSTEM_TLE);

        } else {
            super.processXmlElementStart(name);
        }
    }

    /**
     * Parses JAC TCS XML.
     */
    public void processXmlElementContent(String name, String value, int pos) {
        // All the hard wired strings are taken from the JAC TCS XML.
        // They are left hard wired as they are unlikely to be needed outside
        // this class.

        if (_currentPosition == null) {
            super.processXmlElementContent(name, value, pos);

            return;
        }

        // Some elements have the same name for different system types.
        // Therefore we need to determine the system type in order to be able
        // to distinguish them.
        boolean isConic = (_currentPosition.getSystemType()
                == SpTelescopePos.SYSTEM_CONIC);
        boolean isTLE = (_currentPosition.getSystemType()
                == SpTelescopePos.SYSTEM_TLE);

        if (name.equals(TX_BASE) || name.equals(TX_TARGET)) {
            return;
        }

        if (name.equals(TX_TARGET_NAME)) {
            _currentPosition.setName(value);

            return;
        }

        if (name.equals(TX_SPHERICAL_SYSTEM)) {
            _currentPosition.setSystemType(SpTelescopePos.SYSTEM_SPHERICAL);

            return;
        }

        if (name.equals(TX_CONIC_SYSTEM)) {
            _currentPosition.setSystemType(SpTelescopePos.SYSTEM_CONIC);

            return;
        }

        if (name.equals(TX_TLE_SYSTEM)) {
            _currentPosition.setSystemType(SpTelescopePos.SYSTEM_TLE);

            return;
        }

        if (name.equals(TX_NAMED_SYSTEM)) {
            // Ignore here and set system type in processXmlAttribute.
            // processXmlAttribute might not be called because the
            // XML element TX_NAMED_SYSTEM does not contain PCDATA.
            return;
        }

        // Since the proper motion is stored in arcsecs/yr, but is displayed in
        // mas/year, we need to convert between the two before assigning.
        if (name.equals(TX_PM1)) {
            Double dValue = new Double(value) * 1000.0;
            _currentPosition.setPropMotionRA(dValue.toString());

            return;
        }

        if (name.equals(TX_PM2)) {
            Double dValue = new Double(value) * 1000.0;
            _currentPosition.setPropMotionDec(dValue.toString());

            return;
        }

        // ---- Sperical System

        // Coordinate 1 (x axis)
        if (name.equals(TX_C1)) {
            _currentPosition.setXYFromString(value,
                    _currentPosition.getYaxisAsString());

            return;
        }

        // Coordinate 2 (y axis)
        if (name.equals(TX_C2)) {
            _currentPosition.setXYFromString(
                    _currentPosition.getXaxisAsString(), value);

            return;
        }

        // ---- Conic System (Orbital Elements)

        if (name.equals(TX_EPOCH)) {
            if (_currentPosition.getSystemType()
                    == SpTelescopePos.SYSTEM_CONIC) {
                _currentPosition.setConicSystemEpoch(value);
            } else {
                _currentPosition.setTrackingEpoch(value);
            }

            return;
        }

        if (name.equals(TX_EPOCH_PERIH)) {
            _currentPosition.setConicSystemEpochPerih(value);

            return;
        }

        if (isConic && name.equals(TX_INCLINATION)) {
            _currentPosition.setConicSystemInclination(value);

            return;
        }

        if (name.equals(TX_ANODE)) {
            _currentPosition.setConicSystemAnode(value);

            return;
        }

        if (name.equals(TX_PERIHELION)) {
            _currentPosition.setConicSystemPerihelion(value);

            return;
        }

        if (name.equals(TX_AORQ)) {
            _currentPosition.setConicSystemAorQ(value);

            return;
        }

        if (isConic && name.equals(TX_E)) {
            _currentPosition.setConicSystemE(value);

            return;
        }

        if (isConic && name.equals(TX_LORM)) {
            _currentPosition.setConicSystemLorM(value);

            return;
        }

        if (isConic && name.equals(TX_N)) {
            _currentPosition.setConicSystemDailyMotion(value);

            return;
        }

        // ---- TLE System (Space Junk) ----

        if (name.equals(TX_TLE_EPOCH_YEAR)) {
            _currentPosition.setTleSystemEpochYear(value);

            return;
        }

        if (name.equals(TX_TLE_EPOCH_DAY)) {
            _currentPosition.setTleSystemEpochDay(value);

            return;
        }

        if (isTLE && name.equals(TX_TLE_INCLINATION)) {
            _currentPosition.setTleSystemInclination(value);

            return;
        }

        if (name.equals(TX_TLE_RAANODE)) {
            _currentPosition.setTleSystemRaANode(value);

            return;
        }

        if (name.equals(TX_TLE_PERIGEE)) {
            _currentPosition.setTleSystemPerigee(value);

            return;
        }

        if (isTLE && name.equals(TX_TLE_E)) {
            _currentPosition.setTleSystemE(value);

            return;
        }

        if (isTLE && name.equals(TX_TLE_MEAN_ANOMALY)) {
            _currentPosition.setTleSystemMeanAnomaly(value);

            return;
        }

        if (isTLE && name.equals(TX_TLE_MEAN_MOTION)) {
            _currentPosition.setTleSystemMeanMotion(value);

            return;
        }

        if (name.equals(TX_TLE_BSTAR)) {
            _currentPosition.setTleSystemBStar(value);

            return;
        }

        // ---- Named System

        // A named system only has a name (see top of this method)
        // and a type (see method processXmlAttribute).

        // ---- Offset

        if (name.equals(TX_OFFSET)) {
            _currentPosition.setOffsetPosition(true);

            return;
        }

        // Offset, coordinate 1
        if (name.equals(TX_DC1)) {
            try {
                _currentPosition.setOffsetPosition(true);
                double dc1 = Double.parseDouble(value);

                if (_currentPosition.isBasePosition()) {
                    _currentPosition.setBaseXOffset(dc1);
                } else {
                    _currentPosition.setXY(dc1, _currentPosition.getYaxis());
                }
            } catch (Exception e) {
                // ignore
            }

            return;
        }

        // Offset, coordinate 2
        if (name.equals(TX_DC2)) {
            try {
                _currentPosition.setOffsetPosition(true);
                double dc2 = Double.parseDouble(value);

                if (_currentPosition.isBasePosition()) {
                    _currentPosition.setBaseYOffset(dc2);
                } else {
                    _currentPosition.setXY(_currentPosition.getXaxis(), dc2);
                }
            } catch (Exception e) {
                // ignore
            }

            return;
        }

        // ---- Details

        // Radial Velocity
        if (name.equals(TX_RV)) {
            _currentPosition.setTrackingRadialVelocity(value);

            return;
        }

        // Paralax
        if (name.equals(TX_PARALLAX)) {
            _currentPosition.setTrackingParallax(value);

            return;
        }

        super.processXmlElementContent(name, value, pos);
    }

    /**
     * Parses JAC TCS XML.
     */
    public void processXmlElementEnd(String name) {
        if (name.equals(TX_BASE)) {
            _currentPosition = null;

            // save() just means reset() in this context.
            getAvEditFSM().save();

            return;
        }

        super.processXmlElementEnd(name);
    }

    /**
     * Parses JAC TCS XML.
     */
    public void processXmlAttribute(String elementName, String attributeName,
            String value) {
        // Fix so that we can translate REFERNCE to SKY for UKIRT
        if ((value.equals("REFERENCE"))
                && (System.getProperty("ot.cfgdir").indexOf("ukirt") != -1)) {
            value = "SKY";
        }

        if (elementName.equals(TX_BASE) && attributeName.equals(TX_TYPE)) {
            // TCS XML element SCIENCE is the SpTelescopePos BASE_TAG
            // TCS XML element BASE is something else.
            if (value.equals(TX_TYPE_SCIENCE)) {
                SpTelescopePos.createDefaultBasePosition(_avTable);
                _currentPosition = getPosList().getBasePosition();
            } else {
                _currentPosition = getPosList().createPosition(value, 0.0, 0.0);
            }

            return;
        }

        if (elementName.equals(TX_SPHERICAL_SYSTEM)) {
            _currentPosition.setSystemType(SpTelescopePos.SYSTEM_SPHERICAL);
        } else if (elementName.equals(TX_CONIC_SYSTEM)) {
            _currentPosition.setSystemType(SpTelescopePos.SYSTEM_CONIC);
        } else if (elementName.equals(TX_NAMED_SYSTEM)) {
            _currentPosition.setSystemType(SpTelescopePos.SYSTEM_NAMED);
        } else if (elementName.equals(TX_TLE_SYSTEM)) {
            _currentPosition.setSystemType(SpTelescopePos.SYSTEM_TLE);
        }

        if ((elementName.equals(TX_SPHERICAL_SYSTEM)
                        || elementName.equals(TX_OFFSET))
                && attributeName.equals(TX_SYSTEM)) {
            if (value.equals(TX_J2000)) {
                _currentPosition.setCoordSys(CoordSys.COORD_SYS[CoordSys.FK5]);

                return;
            } else if (value.equals(TX_B1950)) {
                _currentPosition.setCoordSys(CoordSys.COORD_SYS[CoordSys.FK4]);

                return;
            } else if (value.equals(TX_AZEL)) {
                _currentPosition.setCoordSys(
                        CoordSys.COORD_SYS[CoordSys.AZ_EL]);

                return;
            }

            _currentPosition.setCoordSys(value);

            return;
        }

        // Type of conic or name system (comet, major, minor etc.)
        if ((elementName.equals(TX_CONIC_SYSTEM)
                        || elementName.equals(TX_NAMED_SYSTEM))
                && (attributeName.equals(TX_CONIC_NAMED_TYPE)
                        || attributeName.equals(TX_OLD_CONIC_NAMED_TYPE))) {
            if (_currentPosition != null) {
                _currentPosition.setConicOrNamedType(value);

                if (elementName.equals(TX_NAMED_SYSTEM)) {
                    _currentPosition.setSystemType(SpTelescopePos.SYSTEM_NAMED);
                }

                return;
            }
        }

        if (elementName.equals(TX_RV)) {
            if (_currentPosition == null) {
                return;
            }

            if (attributeName.equals(TX_RV_DEFN)) {
                _currentPosition.setTrackingRadialVelocityDefn(value);
            } else if (attributeName.equals(TX_RV_FRAME)) {
                _currentPosition.setTrackingRadialVelocityFrame(value);
            }

            return;
        }

        if (elementName.equals(TX_EPOCH)
                || elementName.equals(TX_EPOCH_PERIH)
                || elementName.equals(TX_INCLINATION)
                || elementName.equals(TX_ANODE)
                || elementName.equals(TX_PERIHELION)
                || elementName.equals(TX_AORQ)
                || elementName.equals(TX_E)
                || elementName.equals(TX_LORM)
                || elementName.equals(TX_N)
                || elementName.equals(TX_PM1)
                || elementName.equals(TX_PM2)) {
            // ignore
            return;
        }

        super.processXmlAttribute(elementName, attributeName, value);
    }

    /**
     * Public convenience method used for use in SpSurveyObsComp.
     */
    public String getXML(String indent) {
        StringBuffer stringBuffer = new StringBuffer();

        toXML(indent, stringBuffer);

        return stringBuffer.toString();
    }
}
