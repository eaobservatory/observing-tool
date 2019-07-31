/*
 * Copyright 2002 United Kingdom Astronomy Technology Centre, an
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

package edfreq;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import java.io.IOException;

import orac.util.InstCfg;
import orac.util.InstCfgReader;

/**
 * Previous out of date documentation removed to avoid confusion.
 *
 * @author Martin Folger
 */
public class FrequencyEditorCfg {
    /**
     * System property containing the location of the XML configuration file
     * as a resource in the classpath.
     */
    public String[] frontEnds;
    public Hashtable<String, String[]> frontEndTable =
            new Hashtable<String, String[]>();
    public Hashtable<String, String[]> frontEndMixers =
            new Hashtable<String, String[]>();
    public String[] velocityFrames = {
            "LSRK",
            "Geocentric",
            "Heliocentric",
    };
    public Hashtable<String, Receiver> receivers;
    private static FrequencyEditorCfg _frequencyEditorCfg = null;

    /**
     * Constructor used if no xml configuration file is specified or found.
     */
    private FrequencyEditorCfg() {
        frontEnds = new String[]{};
        receivers = new Hashtable<String, Receiver>();
    }

    /**
     * Returns a FrequencyEditorCfg.
     */
    public static FrequencyEditorCfg getConfiguration() {
        if (_frequencyEditorCfg == null) {
            String acsisCfgFile = System.getProperty("ot.cfgdir");

            if (!acsisCfgFile.endsWith("/")) {
                acsisCfgFile += '/';
            }

            acsisCfgFile += "acsis.cfg";

            _frequencyEditorCfg = getConfiguration(acsisCfgFile);
        }

        return _frequencyEditorCfg;
    }

    private static FrequencyEditorCfg getConfiguration(String fileName) {
        InstCfgReader rdr = new InstCfgReader(fileName);
        InstCfg instInfo = null;
        String block = null;

        String[] myFrontEnds = null;
        Hashtable<String, Receiver> myReceivers = null;

        if (_frequencyEditorCfg == null) {
            _frequencyEditorCfg = new FrequencyEditorCfg();
        }

        try {
            while ((block = rdr.readBlock()) != null) {
                instInfo = new InstCfg(block);

                if (InstCfg.matchAttr(instInfo, "receivers")) {
                    String[][] recList = instInfo.getValueAs2DArray();
                    myFrontEnds = new String[recList.length];
                    myReceivers = new Hashtable<String, Receiver>(
                            recList.length);

                    try {
                        for (int i = 0; i < recList.length; i++) {
                            myFrontEnds[i] = recList[i][0];

                            double loMin = Double.parseDouble(recList[i][1]);
                            double loMax = Double.parseDouble(recList[i][2]);
                            double feIF = Double.parseDouble(recList[i][3]);
                            double bwLower = Double.parseDouble(recList[i][4]);
                            double bwUpper = Double.parseDouble(recList[i][5]);

                            myReceivers.put(recList[i][0], new Receiver(
                                    myFrontEnds[i], loMin, loMax, feIF,
                                    bwLower, bwUpper));
                        }

                        _frequencyEditorCfg.receivers = myReceivers;
                        _frequencyEditorCfg.frontEnds = myFrontEnds;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (InstCfg.matchAttr(instInfo, "modes")) {
                    String[][] modes = instInfo.getValueAs2DArray();
                    Hashtable<String, String[]> myFrontEndTable =
                            new Hashtable<String, String[]>(modes.length);

                    for (int i = 0; i < modes.length; i++) {
                        myFrontEndTable.put(myFrontEnds[i], modes[i]);
                    }

                    _frequencyEditorCfg.frontEndTable = myFrontEndTable;

                } else if (InstCfg.matchAttr(instInfo, "mixers")) {
                    String[][] mixers = instInfo.getValueAs2DArray();
                    Hashtable<String, String[]> myFrontEndMixers =
                            new Hashtable<String, String[]>(mixers.length);

                    for (int i = 0; i < mixers.length; i++) {
                        myFrontEndMixers.put(myFrontEnds[i], mixers[i]);
                    }

                    _frequencyEditorCfg.frontEndMixers = myFrontEndMixers;

                } else if (InstCfg.likeAttr(instInfo, "bandspecs")) {
                    String[][] specs = instInfo.getValueAs2DArray();
                    _decodeBandSpecs(instInfo, specs);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return _frequencyEditorCfg;
    }

    private static void _decodeBandSpecs(InstCfg instInfo, String[][] specs) {
        String name = "";
        Vector<BandSpec> bsVector = new Vector<BandSpec>();

        ArrayList<String> bandwidths = new ArrayList<String>();
        ArrayList<String> overlaps = new ArrayList<String>();
        ArrayList<String> channels = new ArrayList<String>();
        ArrayList<String> hybSubbands = new ArrayList<String>();

        for (int i = 0; i < specs.length; i++) {
            // See if we are dealing with the same name of bandspec
            if (name.equals(specs[i][0])) {
                // We are adding to the current arrays lists
                bandwidths.add(specs[i][1]);
                overlaps.add(specs[i][2]);
                channels.add(specs[i][3]);
                hybSubbands.add(specs[i][4]);

            } else {
                if (name.equals("")) {
                    // First time through the loop - just add the bits
                    name = specs[i][0];
                    bandwidths.add(specs[i][1]);
                    overlaps.add(specs[i][2]);
                    channels.add(specs[i][3]);
                    hybSubbands.add(specs[i][4]);

                } else {
                    // Convert the ArrayLists to arrays
                    double[] bwArray = new double[bandwidths.size()];
                    double[] olArray = new double[overlaps.size()];
                    int[] chArray = new int[channels.size()];
                    int[] hyArray = new int[hybSubbands.size()];

                    // For simplicity, assume they are all the same size
                    for (int j = 0; j < bwArray.length; j++) {
                        bwArray[j] = Double.parseDouble(bandwidths.get(j));
                        olArray[j] = Double.parseDouble(overlaps.get(j));
                        chArray[j] = Integer.parseInt(channels.get(j));
                        hyArray[j] = Integer.parseInt(hybSubbands.get(j));
                    }

                    // Create a new BandSpec and add it to the vector for the
                    // current receiver
                    BandSpec bs = new BandSpec(name, bandwidths.size(),
                            bwArray, olArray, chArray, hyArray);
                    bsVector.add(bs);

                    // Now reset name and empty array lists, then add the new
                    // components
                    name = specs[i][0];
                    bandwidths.clear();
                    overlaps.clear();
                    channels.clear();
                    hybSubbands.clear();

                    bandwidths.add(specs[i][1]);
                    overlaps.add(specs[i][2]);
                    channels.add(specs[i][3]);
                    hybSubbands.add(specs[i][4]);
                }
            }
        }

        // Now add any stragglers
        double[] bwArray = new double[bandwidths.size()];
        double[] olArray = new double[overlaps.size()];
        int[] chArray = new int[channels.size()];
        int[] hyArray = new int[hybSubbands.size()];

        for (int j = 0; j < bwArray.length; j++) {
            bwArray[j] = Double.parseDouble(bandwidths.get(j));
            olArray[j] = Double.parseDouble(overlaps.get(j));
            chArray[j] = Integer.parseInt(channels.get(j));
            hyArray[j] = Integer.parseInt(hybSubbands.get(j));
        }

        // Create a new BandSpec and add it to the vector for the current
        // receiver
        BandSpec bs = new BandSpec(name, bandwidths.size(), bwArray, olArray,
                chArray, hyArray);
        bsVector.add(bs);

        // Finally link the receiver and bandspecs
        boolean foundReceiver = false;
        for (int i = 0; i < _frequencyEditorCfg.frontEnds.length; i++) {
            if (InstCfg.likeAttr(instInfo, _frequencyEditorCfg.frontEnds[i]
                    .replaceAll("[^a-zA-Z0-9]", ""))) {
                foundReceiver = true;

                _frequencyEditorCfg.receivers.get(
                                _frequencyEditorCfg.frontEnds[i]
                        ).setBandSpecs(bsVector);
                break;
            }
        }

        if (!foundReceiver) {
            System.out.println("Failed to find match for keyword "
                    + instInfo.getKeyword());
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("[\n");
        sb.append("\tnames=[");

        for (int i = 0; i < frontEnds.length; i++) {
            sb.append(frontEnds[i] + " ;");
        }

        sb.append("]\n");
        sb.append("\tfrontEndTable={");

        for (Map.Entry<String, String[]> entry: frontEndTable.entrySet()) {
            sb.append(entry.getKey() + ": [");
            for (String s: entry.getValue()) {
                sb.append(s + " ;");
            }
            sb.append("] ;");
        }
        sb.append("}\n");
        sb.append("\tfrontEndMixers={");
        for (Map.Entry<String, String[]> entry: frontEndMixers.entrySet()) {
            sb.append(entry.getKey() + ": [");
            for (String s: entry.getValue()) {
                sb.append(s + " ;");
            }
            sb.append("] ;");
        }
        sb.append("}\n");
        sb.append("\treceivers=[" + receivers + "]\n");
        sb.append("]");

        return sb.toString();
    }
}
