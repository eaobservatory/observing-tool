/*
 * Copyright (C) 2005-2010 Science and Technology Facilities Council.
 * All Rights Reserved.
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

import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.obsComp.SpStareCapability;

import orac.util.InstCfg;
import orac.util.InstCfgReader;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 * The Wavefront Sensor instrument component
 */
@SuppressWarnings("serial")
public final class SpInstWFS extends SpUKIRTInstObsComp {
    // Public attributes
    public static String ATTR_LENS_POS = "lensPos";

    public static String[] LENS_POS;

    private double DEFAULT_EXPTIME = 60.0;

    private int DEFAULT_COADDS = 1;

    private String DEFAULT_LENS_POS = "0.000";

    public static String[] INSTRUMENT_APER; // Array of inst aper values

    public static final SpType SP_TYPE = SpType.create(
            SpType.OBSERVATION_COMPONENT_TYPE, "inst.WFS", "WFS");

    static {
        SpFactory.registerPrototype(new SpInstWFS());
    }

    /**
     * Constructor reads instrument confif file and initialises
     * values.
     */
    public SpInstWFS() {
        super(SP_TYPE);
        addCapability(new SpStareCapability());

        String cfgFile = System.getProperty("ot.cfgdir");

        if (!cfgFile.endsWith("/")) {
            cfgFile += '/';
        }

        cfgFile += "wfs.cfg";
        _readCfgFile(cfgFile);

        _avTable.noNotifySet(ATTR_LENS_POS, DEFAULT_LENS_POS, 0);
    }

    /**
     * Get all the possible lens positions.
     */
    public List<String> getAvailableLensPositions() {
        return Arrays.asList(LENS_POS);
    }

    /**
     * Set the current lens pos.
     */
    public void setLensPos(String value) {
        _avTable.set(ATTR_LENS_POS, value);
    }

    /**
     * Get the current lens position.
     */
    public String getLensPos() {
        if (_avTable.exists(ATTR_LENS_POS)) {
            String lensPos = _avTable.get(ATTR_LENS_POS);

            if (!(getAvailableLensPositions().contains(lensPos))) {
                _avTable.set(ATTR_LENS_POS, getDefaultLensPos());
            }
        } else {
            _avTable.set(ATTR_LENS_POS, getDefaultLensPos());
        }
        return _avTable.get(ATTR_LENS_POS);
    }

    /**
     * Get the current exposure time (seconds).
     */
    public double getExpTime() {
        double time = getExposureTime();

        if (time == 0.0) {
            time = getDefaultExpTime();
            setExpTime(Double.toString(time));
        }

        return time;
    }

    /**
     * Set the number of coadds.
     */
    public void setCoadds(int coadds) {
        if (coadds <= 0) {
            coadds = getDefaultCoadds();
        }

        getStareCapability().setCoadds(coadds);
    }

    /**
     * Set the number of coadds.
     */
    public void setCoadds(String coadds) {
        int c = 0;

        try {
            c = Integer.valueOf(coadds);
        } catch (Exception ex) {
            c = getDefaultCoadds();
        }

        setCoadds(c);
    }

    /**
     * Get the number of coadds.
     */
    public int getCoadds() {
        int coadds = getStareCapability().getCoadds();

        if (coadds == 0) {
            coadds = getDefaultCoadds();
            setCoadds(coadds);
        }

        return coadds;
    }

    /**
     * Set the instrument apertures.
     */
    public void setInstAper() {
        setInstApX(INSTRUMENT_APER[XAP_INDEX]);
        setInstApY(INSTRUMENT_APER[YAP_INDEX]);
        setInstApZ(INSTRUMENT_APER[ZAP_INDEX]);
        setInstApL(INSTRUMENT_APER[LAP_INDEX]);
    }

    public Hashtable<String, String> getConfigItems() {
        Hashtable<String, String> list = new Hashtable<String, String>();

        list.put("instrument", "WFS");
        list.put("version", "1.0");
        list.put("expTime", "" + getExposureTime());
        list.put("objNumExp", "" + getCoadds());
        list.put("lensPos", getLensPos());

        setInstAper();
        list.put("instAperX", "" + getInstApX());
        list.put("instAperY", "" + getInstApY());
        list.put("instAperZ", "" + getInstApZ());
        list.put("instAperL", "" + getInstApL());

        return list;
    }

    private void _readCfgFile(String fileName) {
        InstCfgReader instCfg = new InstCfgReader(fileName);
        InstCfg instInfo = null;
        String block = null;

        try {
            while ((block = instCfg.readBlock()) != null) {
                instInfo = new InstCfg(block);

                if (InstCfg.matchAttr(instInfo, "default_coadds")) {
                    try {
                        DEFAULT_COADDS = Integer.parseInt(instInfo.getValue());
                    } catch (NumberFormatException nfe) {
                        // Stick with the coded default
                    }

                } else if (InstCfg.matchAttr(instInfo, "default_exptime")) {
                    try {
                        DEFAULT_EXPTIME =
                                Double.parseDouble(instInfo.getValue());
                    } catch (NumberFormatException nfe) {
                        // Stick with the coded default
                    }

                } else if (InstCfg.matchAttr(instInfo, "lens_pos")) {
                    LENS_POS = instInfo.getValueAsArray();

                } else if (InstCfg.matchAttr(instInfo, "default_lenspos")) {
                    DEFAULT_LENS_POS = instInfo.getValue();

                } else if (InstCfg.matchAttr(instInfo, "instrument_aper")) {
                    INSTRUMENT_APER = instInfo.getValueAsArray();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getDefaultCoadds() {
        return DEFAULT_COADDS;
    }

    public double getDefaultExpTime() {
        return DEFAULT_EXPTIME;
    }

    private String getDefaultLensPos() {
        return DEFAULT_LENS_POS;
    }

    private SpStareCapability getStareCapability() {
        return (SpStareCapability)
                getCapability(SpStareCapability.CAPABILITY_NAME);
    }
}
