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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.util.Hashtable;
import java.util.Vector;

import java.awt.Container;

import orac.jcmt.inst.SpInstHeterodyne;

/**
 * @author Dennis Kelly (bdk@roe.ac.uk),
 *         modified by Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SideBandDisplay extends JFrame {
    private int displayWidth = EdFreq.DISPLAY_WIDTH;
    private JSlider slider;
    private EmissionLines el;
    private GraphScale localScale;
    private GraphScale targetScale;
    private Box targetPanel;
    private Box dataPanel;
    private Box titlePanel;
    private Box contentPanel;
    private double redshift;
    private FrequencyTable jt;
    private HeterodyneEditor hetEditor;
    private JPanel area1;
    private JPanel area2;
    private JPanel area3;
    private JPanel area4;
    private double _lo1;
    private double _lRangeLimit;
    private double _uRangeLimit;
    private double if_frequency;

    private Container contentPane;

    private Runnable callBackRunnable = null;

    public SideBandDisplay(HeterodyneEditor hetEditor) {
        super("Frequency editor");
        setResizable(false);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        contentPane = getContentPane();

        this.hetEditor = hetEditor;

        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "This will delete your changes."
                                + "\nTo save, use the 'hide' button on the"
                                + " heterodyne editor."
                                + "\nDo you want to save your changes?",
                        "Changes will be deleted",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (option == JOptionPane.NO_OPTION) {
                    setVisible(false);
                } else if (option == JOptionPane.YES_OPTION) {
                    callBack();
                }
            }
        });
    }

    public void updateDisplay(final SpInstHeterodyne inst, Receiver receiver) {
        String feName = inst.getFrontEnd();
        setTitle("Frequency editor: front end = " + feName);

        this.redshift = inst.calculateRedshift();

        _uRangeLimit = receiver.loMax;
        _lRangeLimit = receiver.loMin;

        int samplerCount = Integer.parseInt(inst.getBandMode());

        contentPanel = Box.createHorizontalBox();
        contentPanel.add(Box.createHorizontalGlue());
        dataPanel = Box.createVerticalBox();
        titlePanel = Box.createVerticalBox();

        double halfRange = receiver.feIF + receiver.bandWidthUpper;

        // Need to deal with LO1...
        double obsFreq = inst.calculateSkyFrequency();

        String band = inst.getBand();

        if_frequency = inst.getCentreFrequency(0);

        double lo_frequency;
        if ("best".equals(band) || "usb".equals(band)) {
            lo_frequency = obsFreq - if_frequency;
        } else {
            lo_frequency = obsFreq + if_frequency;
        }
        setLO1(lo_frequency);

        el = new EmissionLines(lo_frequency, halfRange, redshift, displayWidth, 20);

        jt = new FrequencyTable(
                inst, receiver, displayWidth, this, hetEditor, el);

        dataPanel.add(jt, BorderLayout.CENTER);

        final SkyTransmission st = new SkyTransmission(feName, lo_frequency, halfRange, displayWidth, 80, if_frequency);

        targetScale = new GraphScale(lo_frequency, halfRange, 1.0E9, 0.1E9, redshift, 9,
                displayWidth, JSlider.HORIZONTAL);
        localScale = new GraphScale(lo_frequency, halfRange, 1.0E9, 0.1E9, 0.0, 9,
                displayWidth, JSlider.HORIZONTAL);

        createSlider(lo_frequency);

        slider.addChangeListener(el);

        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                double freq_lo = EdFreq.SLIDERSCALE
                        * (double) ((JSlider) e.getSource()).getValue();

                st.updateFrequency(freq_lo, if_frequency);
            }
        });

        slider.addChangeListener(targetScale);
        slider.addChangeListener(localScale);

        createGUI(st);
        pack();

        jt.setModeAndBand(inst.getMode(), inst.getBand());

        el.setMainLine(inst.getRestFrequency(0));
    }

    /** Create slider for Front-end local oscillator */
    private void createSlider(double mid) {
        int centre = (int) Math.rint(mid / EdFreq.SLIDERSCALE);
        int lslide = (int) Math.rint(_lRangeLimit / EdFreq.SLIDERSCALE);
        int uslide = (int) Math.rint(_uRangeLimit / EdFreq.SLIDERSCALE);

        // Check "centre" is within the range of "lslide" - "uslide" in order to
        // prevent an exception constructing the JSlider.  (This probably isn't
        // the best place to apply this check, but if the frequencies are wrong
        // an exception here prevents opening to editor to correct them.)
        centre = Math.max(lslide, Math.min(uslide, centre));

        // Determine tick spacing: 10, 1 GHz if range over 10 GHz else 1, 0.1.
        boolean narrowRange = (_uRangeLimit - _lRangeLimit) < 10.0E9;
        int tickSpacing = (int) Math.rint(
                (narrowRange ? 1.0E9 : 10.0E9) / EdFreq.SLIDERSCALE);
        int minorTickSpacing = (int) Math.rint(
                (narrowRange ? 0.1E9 : 1.0E9) / EdFreq.SLIDERSCALE);

        slider = new JSlider(JSlider.HORIZONTAL, lslide, uslide, centre);
        slider.setMinorTickSpacing(minorTickSpacing);
        slider.setMajorTickSpacing(tickSpacing);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        /* Create labels for slider at 10GHz intervals */

        Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
        for (int j = lslide; j <= uslide; j += tickSpacing) {
            labels.put(
                    new Integer(j),
                    new JLabel("" + j
                            / ((int) Math.rint(1.0E9 / EdFreq.SLIDERSCALE)),
                            SwingConstants.CENTER));
        }

        slider.setLabelTable(labels);

        // LO1 slider will be activeted by pressing the right mouse. This is
        // to prevent accidental LO1 adjustments caused by user clicking on
        // the lower edge of the SideBandDisplay (frequency editor) in order
        // to get it into the foreground. (The entire bottom section of the
        // SideBandDisplay is the LO1 slider, i.e. each click there would
        // result in an LO1 adjustment.)
        slider.setEnabled(false);
        slider.setToolTipText("To change LO press right mouse button "
                + "and keep it pressed. "
                + "Then drag LO with left mouse button.");
        slider.addMouseListener(new MouseAdapter() {
               public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        slider.setEnabled(true);
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        slider.setEnabled(false);
                    }
                }
        });
    }

    private void createGUI(SkyTransmission st) {
        targetPanel = Box.createVerticalBox();
        targetPanel.add(Box.createVerticalGlue());
        targetPanel.add(el);
        targetPanel.add(targetScale);

        Box scales = Box.createVerticalBox();
        scales.add(Box.createVerticalGlue());
        scales.add(localScale);
        scales.add(slider);

        Box plot = Box.createVerticalBox();
        plot.add(targetPanel);

        if (st != null) {
            plot.add(st);
        }

        plot.add(scales);

        dataPanel.add(plot);

        area1 = new JPanel();
        area2 = new JPanel();
        area3 = new JPanel(new BorderLayout());
        area4 = new JPanel(new BorderLayout());

        JLabel label1 = new JLabel("Subsystems");
        area1.add(label1);

        area1.setPreferredSize(new Dimension(100,
                (jt.getPreferredSize()).height));
        area1.setSize(new Dimension(100,
                (jt.getPreferredSize()).height));
        titlePanel.add(area1);

        JLabel label2 = new JLabel("Emission lines");
        area2.add(label2);
        area2.setPreferredSize(new Dimension(100,
                (targetPanel.getPreferredSize()).height));
        area2.setSize(new Dimension(100,
                (targetPanel.getPreferredSize()).height));
        titlePanel.add(area2);

        JLabel label3 = new JLabel("Atm. Transm.", SwingConstants.CENTER);
        area3.add(label3, BorderLayout.CENTER);

        if (st != null) {
            if (st.trxAvailableLSBandUSB()) {
                JPanel trx = new JPanel();
                trx.setLayout(new BoxLayout(trx, BoxLayout.Y_AXIS));
                JLabel trxLSB = new JLabel("TRx LSB", SwingConstants.CENTER);
                trxLSB.setForeground(Color.blue);
                trx.add(trxLSB);
                JLabel trxUSB = new JLabel("TRx USB", SwingConstants.CENTER);
                trxUSB.setForeground(Color.orange);
                trx.add(trxUSB);
                area3.add(trx, BorderLayout.NORTH);
            }
            else if (st.trxAvailable()) {
                JLabel trxLabel = new JLabel("TRx", SwingConstants.CENTER);
                trxLabel.setForeground(Color.blue);
                area3.add(trxLabel, BorderLayout.NORTH);
            }

            int preferredHeight = st.getPreferredSize().height;
            GraphScale gs = new GraphScale(0.55, 0.55, 0.5, 0.1, 0.0, 0,
                    preferredHeight, JSlider.VERTICAL);
            area3.add(gs, BorderLayout.EAST);

            area3.setPreferredSize(new Dimension(100, preferredHeight));
            area3.setSize(new Dimension(100, preferredHeight));
            titlePanel.add(area3);
        }

        JLabel label4 = new JLabel("FE Freq", SwingConstants.CENTER);
        JLabel label5 = new JLabel("LO", SwingConstants.CENTER);
        area4.add(label4, BorderLayout.NORTH);
        area4.add(label5, BorderLayout.CENTER);
        area4.setPreferredSize(new Dimension(100,
                scales.getPreferredSize().height));
        area4.setSize(new Dimension(100, scales.getPreferredSize().height));
        titlePanel.add(area4);

        contentPanel.add(titlePanel);
        contentPanel.add(dataPanel);
        contentPane.removeAll();
        contentPane.add(contentPanel, BorderLayout.CENTER);
    }

    public void setIF(double if_frequency) {
        this.if_frequency = if_frequency;
    }

    public void setLO1(double lo1) {
        _lo1 = lo1;

        if (_lo1 < _lRangeLimit) {
            _lo1 = _lRangeLimit;
        }

        if (_lo1 > _uRangeLimit) {
            _lo1 = _uRangeLimit;
        }

        if (slider != null) {
            int centre = (int) Math.rint(_lo1 / EdFreq.SLIDERSCALE);
            slider.setValue(centre);
        }
    }

    public double getLO1() {
        return _lo1;
    }

    /**
     * Get the current frequency editor setup.
     *
     * The following information is returned in each vector:
     *
     * 0: molecule name (String)
     * 1: transition (String)
     * 2: Rest Frequency (Double)
     * 3: Centre Frequerncy (Double)
     * 4: Bandwidth (Double)
     * 5: Number of channels (Integer)
     * 6: resolution
     *
     * One vector is returned for each susbsystem. This method replaces
     * other calls for setting things in the heterodyne editor.
     */
    @SuppressWarnings("unchecked")
    public Vector<Object>[] getCurrentConfiguration() {
        // Create the array
        Sampler[] samplers = jt.getSamplers();
        Vector<Object>[] results = new Vector[samplers.length];
        LineDetails details;
        String text;

        for (int i = 0; i < results.length; i++) {
            results[i] = new Vector<Object>();

            details = jt.getLineDetails(i);
            double cf = samplers[i].getCentreFrequency();
            double bw = samplers[i].getBandWidth();
            int ch = samplers[i].getChannels();
            int res = samplers[i].getResolution();
            String sideband = samplers[i].sideband;

            if (details != null) {
                results[i].add(details.name);
                results[i].add(details.transition);
                results[i].add(new Double(details.frequency * 1.0E6));
            }
            else {
                results[i].add(HeterodyneEditor.NO_LINE);
                results[i].add(HeterodyneEditor.NO_LINE);
                results[i].add(new Double(EdFreq.getRestFrequency(
                        getLO1(), cf, redshift,
                        (sideband != null) ? sideband : hetEditor.getFeBand())));
            }

            results[i].add(new Double(cf));
            results[i].add(new Double(bw));
            results[i].add(new Integer(ch));
            results[i].add(new Integer(res));
        }

        return results;
    }

    /**
     * Sets a callback runnable.
     *
     * @param runnable - action to be performed
     */
    public void setCallback(Runnable callback) {
        callBackRunnable = callback;
    }

    /**
     * Calls the callback runnable.
     */
    private void callBack() {
        if (callBackRunnable != null) {
            callBackRunnable.run();
        }
    }
}
