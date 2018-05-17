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
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalScrollBarUI;

import java.util.Vector;

/**
 * @author Dennis Kelly (bdk@roe.ac.uk),
 *         modified by Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class FrequencyTable extends JPanel implements ActionListener {
    private double lLowLimit;
    private double lHighLimit;
    private double uLowLimit;
    private double uHighLimit;
    private Sampler[] samplers;
    private Object[][] data;
    private JButton[] lineButtons;
    private LineDetails[] lineDetails;
    private SideBandDisplay sideBandDisplay;
    private HeterodyneEditor hetEditor;
    private EmissionLines emissionLines;
    double gigToPix;
    Vector<SideBandScrollBar> lowBars = new Vector<SideBandScrollBar>();
    Vector<SideBandScrollBar> highBars = new Vector<SideBandScrollBar>();

    /**
     * Constructor to create frequency table for the Frequency Editor.
     *
     * @param feIF            Frontend IF mixer frequency (Hz)
     * @param feBandWidth     Frontend bandwidth (Hz)
     * @param bandWidths      Bandwidths for each subsystem (Hz)
     * @param channels        Number of channels for each subsystem
     * @param samplerCount    Number of samplers
     * @param displayWidth    Width if display on Frequency Editor
     * @param sideBandDisplay The side band display of the Frequency Editor
     *                        widget
     * @param hetEditor       The heterodyne editor
     * @param emissionLines   Emissions lines to display
     * @param nMixers         Number of mixers
     */
    public FrequencyTable(double feIF, double feBandWidth, double[] bandWidths,
            int[] channels, int samplerCount, int displayWidth,
            SideBandDisplay sideBandDisplay, HeterodyneEditor hetEditor,
            EmissionLines emissionLines, int nMixers) {

        super();

        this.sideBandDisplay = sideBandDisplay;
        this.hetEditor = hetEditor;
        this.emissionLines = emissionLines;

        JPanel[] columns = new JPanel[6];

        int j;
        int i;
        JComboBox widthChoice;
        SamplerDisplay samplerDisplay;
        ResolutionDisplay resolutionDisplay;
        SideBandScrollBar lowBar;
        SideBandScrollBar highBar;

        lLowLimit = -feIF - (feBandWidth * 0.5);
        lHighLimit = -feIF + (feBandWidth * 0.5);
        uLowLimit = feIF - (feBandWidth * 0.5);
        uHighLimit = feIF + (feBandWidth * 0.5);

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
        int sWidth = displayWidth / 10;
        int tWidth = displayWidth / 10;
        int sp1Width = (displayWidth - lWidth - uWidth - sWidth - tWidth) / 2;
        int sp2Width = displayWidth - lWidth - uWidth - sWidth - tWidth
                - sp1Width;
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

        data = new Object[samplerCount][3];
        samplers = new Sampler[samplerCount];

        lineButtons = new JButton[samplerCount];
        lineDetails = new LineDetails[samplerCount];

        lowBars.clear();
        highBars.clear();
        for (j = 0; j < samplerCount; j++) {
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
            if (j == 0) {
                lineButtons[j] = new JButton("See Heterodyne Editor");
                lineButtons[j].setEnabled(false);

                if (FrequencyEditorCfg.getConfiguration()
                        .centreFrequenciesAdjustable) {
                    lowBar.setToolTipText(
                            "Left mouse button drags line along. "
                            + "Right mouse button leaves line unchanged.");
                    highBar.setToolTipText(
                            "Left mouse button drags line along. "
                            + "Right mouse button leaves line unchanged.");
                }
            } else {
                lineButtons[j] = new JButton(HeterodyneEditor.NO_LINE);
            }

            lineButtons[j].setForeground(Color.black);
            lineButtons[j].setFont(new java.awt.Font("Dialog", 0, 10));
            lineButtons[j].setActionCommand("" + j);
            lineButtons[j].addActionListener(this);

            samplerDisplay = new SamplerDisplay(String.valueOf(feIF));
            resolutionDisplay = new ResolutionDisplay(channels[0],
                    bandWidths[0], nMixers);

            Vector<String> bandWidthItems = new Vector<String>();
            for (int k = 0; k < bandWidths.length; k++) {
                bandWidthItems.add("" + (Math.rint(bandWidths[k] * 1.0E-6)));
            }
            widthChoice = new JComboBox(bandWidthItems);

            samplers[j] = new Sampler(feIF, feBandWidth, bandWidths, channels,
                    widthChoice);
            samplers[j].setBandWidth(hetEditor.getCurrentBandwidth(j));

            // If the DISALLOW_MULTI_BW system property is used then
            // the widthChoice JComboBoxes are disabled.
            // This is used because initially ACSIS will not support
            // different bandWidths for different subsystems.
            // So the bandwidth choice on the HeterodyneEditor panel is used
            // to set the same bandwidths for all subsystems.
            // The bandwidth choices of this FrequencyTable are still used to
            // display the bandwidths.
            if (System.getProperty("DISALLOW_MULTI_BW") != null) {
                widthChoice.setEnabled(false);
            }

            data[j][0] = new SideBand(lLowLimit, lHighLimit, bandWidths[0],
                    -feIF, samplers[j], lowBar, gigToPix, emissionLines);
            data[j][1] = samplers[j];
            data[j][2] = new SideBand(uLowLimit, uHighLimit, bandWidths[0],
                    feIF, samplers[j], highBar, gigToPix, emissionLines);

            samplers[j].addSamplerWatcher((SamplerWatcher) data[j][0]);
            samplers[j].addSamplerWatcher((SamplerWatcher) samplerDisplay);
            samplers[j].addSamplerWatcher((SamplerWatcher) resolutionDisplay);
            samplers[j].addSamplerWatcher((SamplerWatcher) data[j][2]);

            NumberedSideBandListener numberedSideBandListener =
                    new NumberedSideBandListener(j);

            lowBar.addMouseListener(numberedSideBandListener);
            highBar.addMouseListener(numberedSideBandListener);
            ((SideBand) data[j][0]).addAdjustmentListener(
                    numberedSideBandListener);

            columns[0].add(lowBar);
            columns[1].add(lineButtons[j]);
            columns[2].add(samplerDisplay);
            columns[3].add(widthChoice);
            columns[4].add(resolutionDisplay);
            columns[5].add(highBar);
        }

        // Added by MFO (8 January 2002)
        // Only the top pair of SideBands need to have references of
        // SideBandDisplay and HeterodyneEditor because when one of them is
        // changed the LO1 needs adjusting, depending on whether "usb" or "lsb"
        // has been selected.
        ((SideBand) data[0][0]).connectTopSideBand(sideBandDisplay, hetEditor);
        ((SideBand) data[0][2]).connectTopSideBand(sideBandDisplay, hetEditor);
    }

    /**
     * Get all of the Samplers in the current configuration.
     */
    public Sampler[] getSamplers() {
        return samplers;
    }

    /**
     * Set the line text on the frequency editor GUI.
     */
    public void setLineText(String lineText, int subsystem) {
        lineButtons[subsystem].setText(lineText);
        lineButtons[subsystem].setToolTipText(lineButtons[subsystem].getText());
    }

    public LineDetails getLineDetails(int subsystem) throws Exception {
        if (lineDetails.length == 0 || lineDetails.length - 1 < subsystem) {
            throw new Exception("No Line Details");
        }

        return lineDetails[subsystem];
    }

    public String getLineText(int subsystem) {
        if (subsystem > lineButtons.length - 1) {
            return null;
        }

        return lineButtons[subsystem].getText();
    }

    /**
     * Reset the mode and band, and update GUI appropriately.
     *
     * @param mode Either "ssb" (Single Sideband) or "dsb" (Dual Sideband)
     * @param band Either "usb" (upper sideband), "lsb" (lower sideband) or
     *             "best"
     */
    public void resetModeAndBand(String mode, String band) {
        if (mode.equalsIgnoreCase("ssb")) {
            if (band.equalsIgnoreCase("lsb")) {
                for (int i = 0; i < data.length; i++) {
                    ((SideBand) data[i][0]).on();
                    ((SideBand) data[i][2]).off();
                }
            }

            if (band.equalsIgnoreCase("usb") || band.equalsIgnoreCase("best")) {
                for (int i = 0; i < data.length; i++) {
                    ((SideBand) data[i][2]).on();
                    ((SideBand) data[i][0]).off();
                }
            }
        } else {
            for (int i = 0; i < data.length; i++) {
                ((SideBand) data[i][0]).on();
                ((SideBand) data[i][2]).on();
            }
        }
    }

    /**
     * Resets line details.
     */
    public void actionPerformed(ActionEvent e) {
        int subsystem = Integer.parseInt(e.getActionCommand());

        // emissionLines.getSelectedLine() can be null if no line
        // has been selected.
        LineDetails lineDetails = emissionLines.getSelectedLine();

        resetLineDetails(lineDetails, subsystem);
    }

    protected void resetLineDetails(LineDetails lineDetails, int subsystem) {
        if (lineDetails != null) {
            double obsFrequency = (lineDetails.frequency * 1.0E6)
                    / (1.0 + hetEditor.getRedshift());

            double centreFrequencyDerivedFromLine = Math.abs(obsFrequency
                    - sideBandDisplay.getLO1());

            samplers[subsystem].setCentreFrequency(
                    centreFrequencyDerivedFromLine);

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
            if (mode.equalsIgnoreCase("ssb")) {
                if ((band.equalsIgnoreCase("usb")
                            || band.equalsIgnoreCase("best"))
                        && (obsFrequency < sideBandDisplay.getLO1())) {
                    lineInBand = false;

                } else if (band.equalsIgnoreCase("lsb")
                        && (obsFrequency > sideBandDisplay.getLO1())) {
                    lineInBand = false;
                }
            }

            if (lineInBand) {
                lineButtons[subsystem].setText(
                                lineDetails.name + "  "
                                + lineDetails.transition + "  "
                                + lineDetails.frequency);

                this.lineDetails[subsystem] = lineDetails;

            } else {
                lineButtons[subsystem].setText(HeterodyneEditor.NO_LINE);
            }

            lineButtons[subsystem].setToolTipText(
                    lineButtons[subsystem].getText());
        }

        lineButtons[subsystem].setToolTipText(
                lineButtons[subsystem].getText());
    }

    protected void lo1Changed() {
        // Skip first button
        for (int i = 1; i < lineButtons.length; i++) {
            lineButtons[i].setText(HeterodyneEditor.NO_LINE);
            lineButtons[i].setToolTipText(lineButtons[i].getText());
        }
    }

    public void moveSlider(String band, double newPos, int subsystem) {
        if (band.equals("best") || band.equals("usb")) {
            SideBandScrollBar bar = highBars.elementAt(subsystem);
            AdjustmentListener[] listeners =
                    highBars.elementAt(subsystem).getListeners(
                            AdjustmentListener.class);

            // Disable the event liteners and move the USB sliders
            for (int i = 0; i < listeners.length; i++) {
                highBars.elementAt(subsystem).removeAdjustmentListener(
                        listeners[i]);
            }

            int newValue = (int) ((double) bar.getDefaultValue()
                    - newPos * gigToPix);
            highBars.elementAt(subsystem).setValue(newValue);

            for (int i = 0; i < listeners.length; i++) {
                highBars.elementAt(subsystem).addAdjustmentListener(
                        listeners[i]);
            }

            // Now move the LSB scroller in the complimentary direction
            listeners = lowBars.elementAt(subsystem).getListeners(
                    AdjustmentListener.class);
            bar = lowBars.elementAt(subsystem);

            for (int i = 0; i < listeners.length; i++) {
                lowBars.elementAt(subsystem).removeAdjustmentListener(
                        listeners[i]);
            }

            newValue = (int) ((double) bar.getDefaultValue()
                    + newPos * gigToPix);
            lowBars.elementAt(subsystem).setValue(newValue);

            for (int i = 0; i < listeners.length; i++) {
                lowBars.elementAt(subsystem).addAdjustmentListener(
                        listeners[i]);
            }
        } else {
            SideBandScrollBar bar = lowBars.elementAt(subsystem);

            // Disable the event liteners and move the LSB sliders
            AdjustmentListener[] listeners =
                    lowBars.elementAt(subsystem).getListeners(
                            AdjustmentListener.class);

            for (int i = 0; i < listeners.length; i++) {
                lowBars.elementAt(subsystem).removeAdjustmentListener(
                        listeners[i]);
            }

            int newValue = (int) ((double) bar.getDefaultValue()
                    - newPos * gigToPix);
            lowBars.elementAt(subsystem).setValue(newValue);

            for (int i = 0; i < listeners.length; i++) {
                lowBars.elementAt(subsystem).addAdjustmentListener(
                        listeners[i]);
            }

            // Now move the LSB scroller in the complimentary direction
            bar = highBars.elementAt(subsystem);
            listeners = highBars.elementAt(subsystem).getListeners(
                    AdjustmentListener.class);

            for (int i = 0; i < listeners.length; i++) {
                highBars.elementAt(subsystem).removeAdjustmentListener(
                        listeners[i]);
            }

            newValue = (int) ((double) bar.getDefaultValue()
                    + newPos * gigToPix);
            highBars.elementAt(subsystem).setValue(newValue);

            for (int i = 0; i < listeners.length; i++) {
                highBars.elementAt(subsystem).addAdjustmentListener(
                        listeners[i]);
            }
        }
    }

    /**
     * A listener for SideBand scroll bars that has knowledge of which SideBand
     * it is connected to.
     *
     * The SideBands are numbered from top to bottom starting with 0.
     */
    class NumberedSideBandListener
            implements MouseListener, AdjustmentListener {

        private int _number;
        private boolean _mousePressed = false;

        NumberedSideBandListener(int number) {
            _number = number;
        }

        private void _processAdjustment(boolean isRightMouseButton) {
            boolean lineClamped = (_number == 0) && (!isRightMouseButton);

            if (_number == 0) {
                if (lineClamped) {
                    // Skip top subsystem because it is clamped and keeps its
                    // line.  All other subsystem loose their lines.
                    for (int i = 1; i < samplers.length; i++) {
                        lineButtons[i].setText(HeterodyneEditor.NO_LINE);
                    }
                }
            } else {
                lineButtons[_number].setText(HeterodyneEditor.NO_LINE);
            }

            lineButtons[_number].setToolTipText(lineButtons[_number].getText());
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            _mousePressed = true;
        }

        public void mouseReleased(MouseEvent e) {
            _processAdjustment(SwingUtilities.isRightMouseButton(e));

            _mousePressed = false;
        }

        public void adjustmentValueChanged(AdjustmentEvent e) {
            if (!_mousePressed)
                _processAdjustment(false);
        }
    }

    /**
     * JScrollBar subclass without increase/decrease arrow buttons.
     *
     * In fact the increase/decrease arrow buttons are still there but their
     * preferred width has been set to 0.
     */
    class SideBandScrollBar extends JScrollBar {

        int _defaultPos;

        public SideBandScrollBar(int orientation, int value, int extent,
                int min, int max) {
            super(orientation, value, extent, min, max);

            _defaultPos = value;
            setUI(new SideBandUI());
        }

        public int getDefaultValue() {
            return _defaultPos;
        }

        /**
         * MetalScrollBarUI subclass that returns increase/decrease buttons
         * with 0 width.
         */
        class SideBandUI extends MetalScrollBarUI {

            private JButton _leftArrow = new JButton();
            private JButton _rightArrow = new JButton();

            public SideBandUI() {
                _leftArrow.setPreferredSize(new Dimension(0, 5));
                _rightArrow.setPreferredSize(new Dimension(0, 5));
            }

            protected JButton createDecreaseButton(int orientation) {
                return _leftArrow;
            }

            protected JButton createIncreaseButton(int orientation) {
                return _rightArrow;
            }
        }
    }
}
