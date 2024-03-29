/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
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

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalScrollBarUI;

import java.util.List;
import java.util.Vector;

import ot.util.DialogUtil;
import orac.jcmt.inst.SpInstHeterodyne;

/**
 * @author Dennis Kelly (bdk@roe.ac.uk),
 *         modified by Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class FrequencyTable extends JPanel {
    private double lLowLimit;
    private double lHighLimit;
    private double uLowLimit;
    private double uHighLimit;
    private Sampler[] samplers;
    private SideBand[] lowerSideband;
    private SideBand[] upperSideband;
    private JButton[] lineButtons;
    private LineDetails[] lineDetails;
    private SpInstHeterodyne inst;
    private Receiver receiver;
    private SideBandDisplay sideBandDisplay;
    private HeterodyneEditor hetEditor;
    private EmissionLines emissionLines;
    double gigToPix;
    Vector<SideBandScrollBar> lowBars = new Vector<SideBandScrollBar>();
    Vector<SideBandScrollBar> highBars = new Vector<SideBandScrollBar>();

    /**
     * Constructor to create frequency table for the Frequency Editor.
     *
     * @param inst            SpInstHeterodyne object
     * @param receiver        Receiver object
     * @param displayWidth    Width if display on Frequency Editor
     * @param sideBandDisplay The side band display of the Frequency Editor
     *                        widget
     * @param hetEditor       The heterodyne editor
     * @param emissionLines   Emissions lines to display
     */
    public FrequencyTable(
            SpInstHeterodyne inst, Receiver receiver, int displayWidth,
            SideBandDisplay sideBandDisplay, HeterodyneEditor hetEditor,
            EmissionLines emissionLines) {
        this.inst = inst;
        this.receiver = receiver;
        this.sideBandDisplay = sideBandDisplay;
        this.hetEditor = hetEditor;
        this.emissionLines = emissionLines;

        double feIF = receiver.feIF;
        double feBandWidthUpper = receiver.bandWidthUpper;
        double feBandWidthLower = receiver.bandWidthLower;
        int samplerCount = Integer.parseInt(inst.getBandMode());

        int nMixers = 1;
        try {
            nMixers = Integer.parseInt(inst.getMixer());
        } catch (NumberFormatException nfe) {
        }

        List<SpInstHeterodyne.BandSpecSelection> selection = inst.getBandSpecSelection(receiver);

        double lo_frequency = sideBandDisplay.getLO1();

        JPanel[] columns = new JPanel[6];

        int j;
        int i;
        ResolutionDisplay resolutionDisplay;
        SideBandScrollBar lowBar;
        SideBandScrollBar highBar;

        lLowLimit = -feIF - feBandWidthUpper;
        lHighLimit = -feIF + feBandWidthLower;
        uLowLimit = feIF - feBandWidthLower;
        uHighLimit = feIF + feBandWidthUpper;

        /* Create basic pattern of columns */

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JLabel lsbTitle = new JLabel("LSB", SwingConstants.CENTER);
        JLabel lineTitle = new JLabel(" Line", SwingConstants.CENTER);
        JLabel loTitle = new JLabel("IF", SwingConstants.CENTER);
        JLabel bwTitle = new JLabel("BW", SwingConstants.CENTER);
        JLabel resTitle = new JLabel("Res (kHz)", SwingConstants.CENTER);
        JLabel usbTitle = new JLabel("USB", SwingConstants.CENTER);

        for (i = 0; i < 6; i++) {
            columns[i] = new JPanel();
            columns[i].setLayout(new GridLayout(samplerCount + 1, 1));
            add(columns[i]);
        }

        columns[0].add(lsbTitle);
        columns[1].add(lineTitle);
        columns[2].add(loTitle);
        columns[3].add(bwTitle);
        columns[4].add(resTitle);
        columns[5].add(usbTitle);

        /*
         * Calculate scale factors such that lLowLimit to uHighLimit maps to
         * displayWidth graphics pixels
         */

        gigToPix = ((double) displayWidth) / (uHighLimit - lLowLimit);
        int lWidth = (int) Math.rint(gigToPix * (lHighLimit - lLowLimit));
        int uWidth = (int) Math.rint(gigToPix * (uHighLimit - uLowLimit));
        int widthExtra = displayWidth - (lWidth + uWidth);
        int sWidth = widthExtra / 3;
        int tWidth = widthExtra / 6;
        int sp2Width = widthExtra / 6;
        int sp1Width = widthExtra - sWidth - tWidth - sp2Width;
        int h = 20 * (1 + samplerCount);

        /* Set column sizes */
        Dimension dimension = new Dimension(lWidth, h);
        columns[0].setPreferredSize(dimension);
        columns[0].setMinimumSize(dimension);
        columns[0].setMaximumSize(dimension);

        dimension = new Dimension(sp1Width, h);
        columns[1].setPreferredSize(dimension);
        columns[1].setMinimumSize(dimension);
        columns[1].setMaximumSize(dimension);

        dimension = new Dimension(tWidth, h);
        columns[2].setPreferredSize(dimension);
        columns[2].setMinimumSize(dimension);
        columns[2].setMaximumSize(dimension);

        dimension = new Dimension(sWidth, h);
        columns[3].setPreferredSize(dimension);
        columns[3].setMinimumSize(dimension);
        columns[3].setMaximumSize(dimension);

        dimension = new Dimension(sp2Width, h);
        columns[4].setPreferredSize(dimension);
        columns[4].setMinimumSize(dimension);
        columns[4].setMaximumSize(dimension);

        dimension = new Dimension(uWidth, h);
        columns[5].setPreferredSize(dimension);
        columns[5].setMinimumSize(dimension);
        columns[5].setMaximumSize(dimension);

        // Create the samplers and sidebands and their associated displays

        lowerSideband = new SideBand[samplerCount];
        upperSideband = new SideBand[samplerCount];
        samplers = new Sampler[samplerCount];

        lineButtons = new JButton[samplerCount];
        lineDetails = new LineDetails[samplerCount];

        lowBars.clear();
        highBars.clear();
        for (j = 0; j < samplerCount; j++) {
            final int this_j = j;

            SpInstHeterodyne.BandSpecSelection thisSelection = selection.get(j);
            BandSpec activeBandSpec = thisSelection.bandSpec;
            double[] bandWidths = activeBandSpec.getDefaultOverlapBandWidths();
            int[] channels = activeBandSpec.getDefaultOverlapChannels();

            lowBar = new SideBandScrollBar(JScrollBar.HORIZONTAL,
                    (int) Math.rint(gigToPix * (-feIF - 0.5 * bandWidths[0])),
                    (int) Math.rint(gigToPix * bandWidths[0]),
                    (int) Math.rint(gigToPix * lLowLimit),
                    (int) Math.rint(gigToPix * lHighLimit));
            lowBar.setUnitIncrement(1);
            lowBars.add(lowBar);

            highBar = new SideBandScrollBar(JScrollBar.HORIZONTAL,
                    (int) Math.rint(gigToPix * (feIF - 0.5 * bandWidths[0])),
                    (int) Math.rint(gigToPix * bandWidths[0]),
                    (int) Math.rint(gigToPix * uLowLimit),
                    (int) Math.rint(gigToPix * uHighLimit));
            highBar.setUnitIncrement(1);
            highBars.add(highBar);

            // Line display added by MFO (October 16, 2002)
            lineButtons[j] = new JButton("...");
            if (j == 0) {
                lineButtons[j].setEnabled(false);
            }

            lineButtons[j].setForeground(Color.black);
            lineButtons[j].setFont(new java.awt.Font("Dialog", 0, 10));
            lineButtons[j].setActionCommand("" + j);
            lineButtons[j].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // emissionLines.getSelectedLine() can be null if no line
                    // has been selected.
                    LineDetails lineDetails = FrequencyTable.this.emissionLines.getSelectedLine();

                    setLineDetails(lineDetails, this_j);
                }
            });

            final SamplerDisplay samplerDisplay = new SamplerDisplay(String.valueOf(feIF));
            samplerDisplay.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        double center = 0.0;
                        try {
                            center = Double.parseDouble(samplerDisplay.getText());
                        }
                        catch (NumberFormatException exc) {
                            DialogUtil.error(
                                FrequencyTable.this,
                                "IF frequency not understood.");
                        }

                        if (center != 0.0) {
                            samplers[this_j].setCentreFrequency(
                                center, samplers[this_j].sideband);

                            processIFAdjustment(this_j, center);
                        }
                    }
                }
            });

            resolutionDisplay = new ResolutionDisplay(channels[0],
                    bandWidths[0], nMixers);

            JComboBox widthChoice = new JComboBox<BandwidthOption>();

            samplers[j] = new Sampler(
                    feIF, feBandWidthLower, feBandWidthUpper,
                    activeBandSpec, widthChoice, hetEditor.getFeBand());
            samplers[j].setBandWidthAndChannels(
                    hetEditor.getCurrentBandwidth(j), hetEditor.getCurrentChannels(j));

            lowerSideband[j] = new SideBand(lLowLimit, lHighLimit, bandWidths[0],
                    -feIF, samplers[j], lowBar, gigToPix, emissionLines, (j == 0));
            upperSideband[j] = new SideBand(uLowLimit, uHighLimit, bandWidths[0],
                    feIF, samplers[j], highBar, gigToPix, emissionLines, (j == 0));

            samplers[j].addSamplerWatcher(new SamplerWatcher() {
                public void updateSamplerValues(double centre, double width, int channels, String sideband) {
                    processIFAdjustment(this_j, centre);
                }
            });

            samplers[j].addSamplerWatcher(lowerSideband[j]);
            samplers[j].addSamplerWatcher(samplerDisplay);
            samplers[j].addSamplerWatcher(resolutionDisplay);
            samplers[j].addSamplerWatcher(upperSideband[j]);

            columns[0].add(lowBar);
            columns[1].add(lineButtons[j]);
            columns[2].add(samplerDisplay);
            columns[3].add(widthChoice);
            columns[4].add(resolutionDisplay);
            columns[5].add(highBar);
        }

        // Now configure the samplers.  This used to be done in the
        // SideBandDisplay class after executing this constructor.
        for (j = 0; j < samplerCount; j++) {
            Sampler sampler = samplers[j];

            double sky_frequency = inst.calculateSkyFrequency(j);
            String sideband = (sky_frequency > lo_frequency) ? "usb" : "lsb";

            sampler.setBandWidthAndChannels(inst.getBandWidth(j), inst.getChannels(j));
            sampler.setCentreFrequency(inst.getCentreFrequency(j), sideband);
            setLineDetails(
                    new LineDetails(
                            inst.getMolecule(j),
                            inst.getTransition(j),
                            inst.getRestFrequency(j) / 1.0E6),
                    j);

            sampler.bandWidthChoice.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    processBandwidthAdjustment();
                }
            });
        }
    }

    /**
     * Get all of the Samplers in the current configuration.
     */
    public Sampler[] getSamplers() {
        return samplers;
    }

    public LineDetails getLineDetails(int subsystem) {
        if (lineDetails.length == 0 || lineDetails.length - 1 < subsystem) {
            return null;
        }

        return lineDetails[subsystem];
    }

    /**
     * Set the mode and band, and update GUI appropriately.
     *
     * @param mode Either "ssb" (Single Sideband) or "dsb" (Dual Sideband)
     *             or "2sb" (sideband separating) or "usb" or "lsb"
     * @param band Either "usb" (upper sideband), "lsb" (lower sideband) or
     *             "best"
     */
    public void setModeAndBand(String mode, String band) {
        if (mode.equalsIgnoreCase("usb")) {
            for (int i = 0; i < lowerSideband.length; i++) {
                upperSideband[i].on();
                lowerSideband[i].off();
            }
        }
        else if (mode.equalsIgnoreCase("lsb")) {
            for (int i = 0; i < lowerSideband.length; i++) {
                lowerSideband[i].on();
                upperSideband[i].off();
            }
        }
        else if (mode.equalsIgnoreCase("ssb")) {
            if (band.equalsIgnoreCase("lsb")) {
                for (int i = 0; i < lowerSideband.length; i++) {
                    lowerSideband[i].on();
                    upperSideband[i].off();
                }
            }

            if (band.equalsIgnoreCase("usb") || band.equalsIgnoreCase("best")) {
                for (int i = 0; i < lowerSideband.length; i++) {
                    upperSideband[i].on();
                    lowerSideband[i].off();
                }
            }
        } else {
            for (int i = 0; i < lowerSideband.length; i++) {
                lowerSideband[i].on();
                upperSideband[i].on();
            }
        }
    }

    protected void setLineDetails(LineDetails lineDetails, int subsystem) {
        if (lineDetails != null) {
            double obsFrequency = (lineDetails.frequency * 1.0E6)
                    / (1.0 + hetEditor.getRedshift());
            double lo_frequency = sideBandDisplay.getLO1();

            double centreFrequencyDerivedFromLine = Math.abs(
                    obsFrequency - lo_frequency);

            // Determine the sideband based on the frequency unless this
            // is the "top" sampler (which is "clamped").
            String sideband = (subsystem == 0)
                ? samplers[subsystem].sideband
                : ((obsFrequency > lo_frequency) ? "usb" : "lsb");

            samplers[subsystem].setCentreFrequency(
                    centreFrequencyDerivedFromLine,
                    sideband);

            /*
             * Check whether the line is in the band by comparing
             * centreFrequencyDerivedFromLine as calculated above and the
             * actual centre frequency of the sampler. Note the
             * sampler[i].setCentreFrequency adjusts the centre frequency
             * if the sideband would be out of the allowed range otherwise.
             */

            // ssb vs dsb
            String mode = hetEditor.getMode();

            // lsb vs usb
            String band = hetEditor.getFeBand();

            boolean lineInBand = true;

            double centreFrequencyAdjusted = Math.abs(
                    samplers[subsystem].getCentreFrequency()
                    - centreFrequencyDerivedFromLine);

            if (centreFrequencyAdjusted != 0.0) {
                lineInBand = false;
            }

            // If the instrument is in single side band mode (ssb) then the
            // line also has to be in the correct sideband.
            if (mode.equalsIgnoreCase("ssb")
                    || mode.equalsIgnoreCase("usb")
                    || mode.equalsIgnoreCase("lsb")) {
                if ((band.equalsIgnoreCase("usb")
                            || band.equalsIgnoreCase("best"))
                        && (obsFrequency < sideBandDisplay.getLO1())) {
                    lineInBand = false;

                } else if (band.equalsIgnoreCase("lsb")
                        && (obsFrequency > sideBandDisplay.getLO1())) {
                    lineInBand = false;
                }
            }

            if (! lineInBand) {
                lineDetails = null;
            }
        }


        this.lineDetails[subsystem] = lineDetails;

        if (lineDetails != null) {
            lineButtons[subsystem].setText(
                            lineDetails.name + "  "
                            + lineDetails.transition + "  "
                            + lineDetails.frequency);
        }
        else {
            lineButtons[subsystem].setText(HeterodyneEditor.NO_LINE);
        }

        lineButtons[subsystem].setToolTipText(
                lineButtons[subsystem].getText());
    }

    private void processIFAdjustment(int number, double center) {
        if (number == 0) {
            // Skip top subsystem because it is clamped and keeps its
            // line.  All other subsystem loose their lines.
            for (int i = 1; i < samplers.length; i++) {
                setLineDetails(null, i);
            }

            LineDetails line = lineDetails[0];
            if (line != null) {
                double loFreq = EdFreq.getLO1(
                        EdFreq.getObsFrequency(
                                line.frequency * 1.0E6,
                                hetEditor.getRedshift()),
                        center, samplers[0].sideband);

                sideBandDisplay.setIF(center);
                sideBandDisplay.setLO1(loFreq);
            }
        } else {
            setLineDetails(null, number);
        }
    }

    boolean processingBandwidth = false;
    /**
     * Process an adjustment of the bandwith choice comboboxes.
     *
     * When a bandwidth is changed, we need to update the available
     * bandspecs for all samplers and then select the closest match
     * for each of them.
     */
    private void processBandwidthAdjustment() {
        if (processingBandwidth) {
            return;
        }

        processingBandwidth = true;

        // Read the current bandwidth selection from the GUI.
        int n = samplers.length;
        double[] bandwidths = new double[n];
        int[] channels = new int[n];
        for (int i = 0; i < n; i++) {
            BandwidthOption option = (BandwidthOption) samplers[i].bandWidthChoice.getSelectedItem();
            bandwidths[i] = option.bandwidth;
            channels[i] = option.channels;
        }

        // Find matching BandSpecs.
        List<SpInstHeterodyne.BandSpecSelection> selection
            = inst.getBandSpecSelection(receiver, bandwidths, channels);

        // Set up the GUI with the new selections.
        for (int i = 0; i < n; i++) {
            Sampler sampler = samplers[i];
            SpInstHeterodyne.BandSpecSelection thisSelection = selection.get(i);
            BandSpec activeBandSpec = thisSelection.bandSpec;
            int index = (thisSelection.match != -1)
                ? thisSelection.match : (
                    (thisSelection.nearest != -1)
                        ? thisSelection.nearest
                        : 0);

            sampler.setBandSpec(activeBandSpec);

            sampler.setBandWidthAndChannels(
                    activeBandSpec.getDefaultOverlapBandWidths()[index],
                    activeBandSpec.getDefaultOverlapChannels()[index]);
        }

        processingBandwidth = false;
    }

    /**
     * JScrollBar subclass without increase/decrease arrow buttons.
     *
     * In fact the increase/decrease arrow buttons are still there but their
     * preferred width has been set to 0.
     */
    static class SideBandScrollBar extends JScrollBar {
        boolean isSelected = false;
        boolean isInvalid = false;

        public SideBandScrollBar(int orientation, int value, int extent,
                int min, int max) {
            super(orientation, value, extent, min, max);

            setUI(new SideBandUI());

            setBackgroundColor();
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
            setBackgroundColor();
        }

        public void setIsInvalid(boolean isInvalid) {
            this.isInvalid = isInvalid;
            setBackgroundColor();
        }

        private void setBackgroundColor() {
            setBackground(isInvalid
                ? Color.RED
                : (isSelected ? Color.YELLOW : Color.DARK_GRAY));
        }
    }

    /**
     * MetalScrollBarUI subclass that returns increase/decrease buttons
     * with 0 width.
     */
    static class SideBandUI extends MetalScrollBarUI {
        protected JButton createDecreaseButton(int orientation) {
            JButton leftArrow = new JButton();
            leftArrow.setPreferredSize(new Dimension(0, 5));
            return leftArrow;
        }

        protected JButton createIncreaseButton(int orientation) {
            JButton rightArrow = new JButton();
            rightArrow.setPreferredSize(new Dimension(0, 5));
            return rightArrow;
        }
    }
}
