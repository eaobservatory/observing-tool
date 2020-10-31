/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
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

import gemini.util.ObservingToolUtilities;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;

/**
 * @author Dennis Kelly (bdk@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SkyTransmission extends JPanel {
    int xSize;
    int ySize;
    String _feName = null;
    private double lowLimit;
    private double highLimit;
    private double halfrange;
    private double[][] skyTrans;
    private SkyData skyData;
    private int[][] skyPlot;
    private TreeMap<Double, Double> rxTempRF = null;
    private TreeMap<Double, Double> rxTempLSB = null;
    private TreeMap<Double, Double> rxTempUSB = null;
    private int[][] rxPlot1 = null;
    private int[][] rxPlot2 = null;
    private Image buffer = null;
    private Graphics ig;
    private TreeMap<Double, Vector<Double>> untunableBands;
    private int[][] untunablePlot = null;
    private double lowF = 0;
    private double highF = 0;
    private double freq_if = 0;
    private double freq_lo = 0;

    public SkyTransmission(String feName, double centerFreq, double halfRange,
            int xSize, int ySize, double freq_if) {
        this._feName = feName;
        this.halfrange = halfRange;
        this.xSize = xSize;
        this.ySize = ySize;
        this.freq_lo = centerFreq;
        this.freq_if = freq_if;

        lowLimit = centerFreq - halfRange;
        highLimit = centerFreq + halfRange;

        skyData = new SkyData();

        /*
         * Get the whole of the untunableBands structure
         */
        untunableBands = readUntunable();

        /* Set up display */

        setPreferredSize(new Dimension(xSize, ySize));
        setSize(xSize, ySize);

        readTrxData(feName);

        updatePlotData();
    }

    /**
     * Does the actual plotting.
     *
     * Overrides the paintComponent method in JPanel.
     */
    public void paintComponent(Graphics g) {
        if (buffer == null) {
            buffer = createImage(xSize, ySize);

            // added by MFO, 16 November 2001
            if (buffer == null) {
                return;
            }

            ig = buffer.getGraphics();
        }

        ig.setColor(getBackground());
        ig.fillRect(0, 0, xSize, ySize);
        ig.setColor(getForeground());

        for (int j = 0; j < skyPlot.length - 1; j++) {
            ig.drawLine(
                    skyPlot[j][0],
                    skyPlot[j][1],
                    skyPlot[j + 1][0],
                    skyPlot[j + 1][1]);
        }

        // Draw tRx
        paintTrx(ig, rxPlot1, Color.blue);
        paintTrx(ig, rxPlot2, Color.orange);

        // Finally plot untunable bands...
        if (untunablePlot != null && untunablePlot.length > 0) {
            for (int j = 0; j < untunablePlot.length; j++) {
                ig.fillRect(
                        untunablePlot[j][0],
                        untunablePlot[j][1],
                        untunablePlot[j][2],
                        untunablePlot[j][3]);
            }
        }

        g.drawImage(buffer, 0, 0, null);
    }

    private void paintTrx(Graphics ig, int[][] rxPlot, Color color) {
        if (rxPlot != null) {
            // Draw in the specifid color.
            ig.setColor(color);

            for (int j = 0; j < rxPlot.length - 1; j++) {
                ig.drawLine(
                        rxPlot[j][0],
                        rxPlot[j][1],
                        rxPlot[j + 1][0],
                        rxPlot[j + 1][1]);
            }
        }
    }

    public void updateFrequency(double freq_lo, double freq_if) {
        this.freq_lo = freq_lo;
        this.freq_if = freq_if;

        lowLimit = freq_lo - halfrange;
        highLimit = freq_lo + halfrange;

        updatePlotData();

        repaint();
    }

    private void updatePlotData() {
        skyTrans = skyData.getTransmission(lowLimit, highLimit);
        skyPlot = new int[skyTrans.length][2];

        for (int j = 0; j < skyTrans.length; j++) {
            skyPlot[j][0] = (int) ((double) xSize * (skyTrans[j][0] - lowLimit)
                    / (highLimit - lowLimit));
            skyPlot[j][1] = Math.min(ySize - 1,
                    (int) ((double) ySize * (1.0 - skyTrans[j][1])));
        }

        /*
         * Get the rxTemp plot values
         */
        if (trxAvailableLSBandUSB()) {
            rxPlot1 = makePlotDataTrx(rxTempLSB, freq_if, lowLimit, freq_lo);
            rxPlot2 = makePlotDataTrx(rxTempUSB, -freq_if, freq_lo, highLimit);
        }
        else if (trxAvailable()) {
            rxPlot1 = makePlotDataTrx(rxTempRF, 0.0, lowLimit, highLimit);
            rxPlot2 = null;
        }
        else {
            rxPlot1 = null;
            rxPlot2 = null;
        }

        /*
         * See if we need to plot any untunable bands.
         */
        if (untunableBands != null) {
            Iterator<Double> i = untunableBands.keySet().iterator();

            // Find out how many bands we need to plot...
            int nElements = 0;

            while (i.hasNext()) {
                Double f = i.next();

                if (f < lowLimit) {
                    continue;
                } else if (f > highLimit) {
                    break;
                } else {
                    nElements++;
                }
            }

            untunablePlot = new int[nElements][4]; // (x,y,width,height)

            // Now go through again and actually get the value
            i = untunableBands.keySet().iterator();

            int index = 0;

            while (i.hasNext()) {
                Double f = i.next();

                if (f < lowLimit) {
                    continue;
                } else if (f > highLimit) {
                    break;
                } else {
                    Vector<Double> v = untunableBands.get(f);
                    int x = 0, y = 0, height = 0, width = 0;

                    switch (v.size()) {
                        case 1:
                            x = (int) (xSize * (v.firstElement() - lowLimit)
                                    / (highLimit - lowLimit));
                            y = 0;
                            width = 1;
                            height = ySize;
                            break;

                        case 2:
                            double x1 = (xSize * (v.firstElement() - lowLimit)
                                    / (highLimit - lowLimit));
                            double x2 = (xSize * (v.lastElement() - lowLimit)
                                    / (highLimit - lowLimit));
                            x = (int) Math.rint(x1);
                            y = 0;
                            width = (int) Math.rint(x2 - x1);
                            height = ySize;
                            break;

                        default:
                            break;
                    }

                    untunablePlot[index][0] = x;
                    untunablePlot[index][1] = y;
                    untunablePlot[index][2] = width;
                    untunablePlot[index][3] = height;

                    index++;
                }
            }
        }
    }

    private int[][] makePlotDataTrx(TreeMap<Double, Double> rxTemp, double freq_offset, double freq_start, double freq_end) {
        // First of all, get the max and min receiver temperature
        int tMax = 0;
        int tMin = 0;
        boolean first = true;
        int[][] rxPlot = null;
        double freq = 0.0;

        Iterator<Double> iter = rxTemp.values().iterator();

        while (iter.hasNext()) {
            if (first) {
                tMax = (int) Math.rint(iter.next());
                tMin = tMax;
                first = false;
            } else {
                int currentValue = (int) Math.rint(iter.next());

                if (currentValue > tMax) {
                    tMax = currentValue;
                }

                if (currentValue < tMin) {
                    tMin = currentValue;
                }
            }
        }

        // Find out what range of data to show
        iter = rxTemp.keySet().iterator();
        Double key = null;

        while (iter.hasNext()) {
            key = iter.next();
            freq = key - freq_offset;

            if (freq > freq_start) {
                break;
            }
        }

        if (key != null) {
            int xValue = (int) ((double) xSize * (freq - lowLimit)
                    / (highLimit - lowLimit));
            int yValue = (int) Math.rint(rxTemp.get(key));

            yValue = (int) ((double) ySize - (double) ySize
                    * (yValue - tMin) / (tMax - tMin));

            Vector<Integer> keys = new Vector<Integer>();
            Vector<Integer> values = new Vector<Integer>();

            keys.add(new Integer(xValue));
            values.add(new Integer(yValue));

            while (iter.hasNext()) {
                key = iter.next();
                freq = key - freq_offset;

                if (freq > freq_end) {
                    break;
                }

                xValue = (int) ((double) xSize * (freq - lowLimit)
                        / (highLimit - lowLimit));
                yValue = (int) Math.rint(rxTemp.get(key));
                yValue = (int) ((double) ySize - (double) ySize
                        * (yValue - tMin) / (tMax - tMin));

                keys.add(new Integer(xValue));
                values.add(new Integer(yValue));
            }

            // Now construct the plot array
            rxPlot = new int[keys.size()][2];

            for (int i = 0; i < rxPlot.length; i++) {
                rxPlot[i][0] = keys.elementAt(i);
                rxPlot[i][1] = values.elementAt(i);
            }
        }

        return rxPlot;
    }

    /**
     * Read the receiver temperature file and get the data for the specified
     * front end.
     *
     * The return is a TreeMap with frequency as the key and TRx as
     * the value.
     */
    private void readTrxData(String feName) {
        // Get the receiver temperature and open it
        String cfgFilename = System.getProperty("ot.cfgdir");

        if (!cfgFilename.endsWith("/")) {
            cfgFilename += '/';
        }

        cfgFilename += "receiver.info";

        URL url = ObservingToolUtilities.resourceURL(cfgFilename);

        // Read in the data for the current front-end
        if (url != null) {
            try {
                String inputLine;
                InputStream is = url.openStream();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(is));

                // Skip over the header
                while (true) {
                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine.equals("")) {
                            break;
                        }
                    }

                    if (inputLine == null) {
                        break;
                    }

                    // Now keep reading the file until we find the feName

                    // Low frequency limit
                    lowF = Double.valueOf(new String(in.readLine()));
                    lowF = lowF * 1.0e9;

                    // High frequency limit
                    highF = Double.valueOf(new String(in.readLine()));
                    highF = highF * 1.0e9;

                    in.readLine(); // Number of sidebands

                    if (in.readLine().equalsIgnoreCase(feName)) {
                        // We can start reading in the data
                        rxTempRF = readTrxDataSection(in);
                        rxTempLSB = readTrxDataSection(in);
                        rxTempUSB = readTrxDataSection(in);

                        break;
                    } else {
                        // Read up to the next blank line
                        continue;
                    }
                }

                in.close();
                is.close();
            } catch (IOException ex) {
                System.out.println("Error reading receiver file: "
                        + System.getProperty("ot.cfgdir") + "receiver.info");
            }
        } else {
            System.out.println("Receiver info file does not exist: "
                    + System.getProperty("ot.cfgdir") + "receiver.info");
        }
    }

    private TreeMap<Double, Double> readTrxDataSection(BufferedReader in) {
        TreeMap<Double, Double> tRx = new TreeMap<Double, Double>();

        try {
            int nLines = Integer.valueOf(in.readLine());

            if (nLines == 0) {
                return null;
            }


            for (int i = 0; i < nLines; i++) {
                String values = in.readLine();
                StringTokenizer st = new StringTokenizer(values);

                Double frequency = Double.parseDouble(
                        st.nextToken()) * 1.0e9;
                Double trx = Double.parseDouble(st.nextToken());

                tRx.put(frequency, trx);
            }
        } catch (IOException ex) {
            System.out.println("Error reading Trx section of receiver file.");
            return null;
        }

        return tRx;
    }

    private TreeMap<Double, Vector<Double>> readUntunable() {
        TreeMap<Double, Vector<Double>> rtn = null;

        // Get the file
        String cfgFilename = System.getProperty("ot.cfgdir");

        if (!cfgFilename.endsWith("/")) {
            cfgFilename += '/';
        }

        cfgFilename += "untunable.dat";

        URL url = ObservingToolUtilities.resourceURL(cfgFilename);

        if (url == null) {
            System.out.println("Unable to find file " + cfgFilename);
            return rtn;
        }

        // If we get here, we have the file so create a new TreeMap and
        // start reading the file
        rtn = new TreeMap<Double, Vector<Double>>();

        try {
            InputStream is = url.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("#") || inputLine.equals("")) {
                    continue;
                }

                StringTokenizer st = new StringTokenizer(inputLine);
                Vector<Double> values = new Vector<Double>();

                if (st.countTokens() == 1) {
                    values.add(Double.parseDouble(st.nextToken()) * 1.0e9);
                } else if (st.countTokens() == 2) {
                    values.add(Double.parseDouble(st.nextToken()) * 1.0e9);
                    values.add(Double.parseDouble(st.nextToken()) * 1.0e9);
                } else {
                    continue;
                }

                rtn.put(values.firstElement(), values);
            }

            in.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return rtn;
    }

    /**
     * Checks whether TRx values currently exist for this object.
     */
    public boolean trxAvailable() {
        return (rxTempRF != null);
    }

    public boolean trxAvailableLSBandUSB() {
        return ((rxTempLSB != null) && (rxTempUSB != null));
    }
}
