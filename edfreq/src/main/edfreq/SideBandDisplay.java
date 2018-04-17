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
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;

import java.awt.Container;

/**
 * @author Dennis Kelly (bdk@roe.ac.uk),
 *         modified by Martin Folger (M.Folger@roe.ac.uk)
 */
@SuppressWarnings("serial")
public class SideBandDisplay extends JFrame implements ChangeListener,
        MouseListener {
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
    private boolean _ignoreEvents = false;

    private Container contentPane;

    private Object callBackObject = null;
    private Method callBackMethod = null;

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

    public void updateDisplay(String feName, double lRangeLimit,
            double uRangeLimit, double feIF, double feBandWidth, int nMixers,
            double redshift, double[] bandWidths, int[] channels,
            int samplerCount) {
        setTitle("Frequency editor: front end = " + feName);

        int j;

        this.redshift = redshift;

        _uRangeLimit = uRangeLimit;
        _lRangeLimit = lRangeLimit;

        contentPanel = Box.createHorizontalBox();
        contentPanel.add(Box.createHorizontalGlue());
        dataPanel = Box.createVerticalBox();
        titlePanel = Box.createVerticalBox();

        double mid = 0.5 * (lRangeLimit + uRangeLimit);
        double lowIF = mid - feIF - (feBandWidth * 0.5);
        double highIF = mid + feIF + (feBandWidth * 0.5);

        el = new EmissionLines(lowIF, highIF, redshift, displayWidth, 20,
                samplerCount);

        jt = new FrequencyTable(feIF, feBandWidth, bandWidths, channels,
                samplerCount, displayWidth, this, hetEditor, el, nMixers);

        dataPanel.add(jt, BorderLayout.CENTER);

        SkyTransmission st = null;
        try {
            st = new SkyTransmission(feName, lowIF, highIF, displayWidth, 80);
        } catch (Exception e) {
        }

        targetScale = new GraphScale(lowIF, highIF, 1.0E9, 0.1E9, redshift, 9,
                displayWidth, JSlider.HORIZONTAL);
        localScale = new GraphScale(lowIF, highIF, 1.0E9, 0.1E9, 0.0, 9,
                displayWidth, JSlider.HORIZONTAL);

        /* Create slider for Front-end local oscillator */

        int centre = (int) Math.rint(mid / EdFreq.SLIDERSCALE);
        int lslide = (int) Math.rint(lRangeLimit / EdFreq.SLIDERSCALE);
        int uslide = (int) Math.rint(uRangeLimit / EdFreq.SLIDERSCALE);

        slider = new JSlider(JSlider.HORIZONTAL, lslide, uslide, centre);
        slider.setMinorTickSpacing(
                (int) Math.rint(1.0E9 / EdFreq.SLIDERSCALE));
        slider.setMajorTickSpacing(
                (int) Math.rint(10.0E9 / EdFreq.SLIDERSCALE));
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        /* Create labels for slider at 10GHz intervals */

        Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
        for (j = lslide; j <= uslide;
                j += (int) Math.rint(10.0E9 / EdFreq.SLIDERSCALE))
            labels.put(
                    new Integer(j),
                    new JLabel("" + j
                            / ((int) Math.rint(1.0E9 / EdFreq.SLIDERSCALE)),
                            SwingConstants.CENTER));

        slider.setLabelTable(labels);

        /* Make the graphics follow the slider */

        slider.addChangeListener((ChangeListener) el);

        if (st != null) {
            slider.addChangeListener((ChangeListener) st);
        }

        slider.addChangeListener((ChangeListener) targetScale);
        slider.addChangeListener((ChangeListener) localScale);
        slider.addChangeListener(this);

        // LO1 slider will be activeted by pressing the right mouse. This is
        // to prevent accidental LO1 adjustments caused by user clicking on
        // the lower edge of the SideBandDisplay (frequency editor) in order
        // to get it into the foreground. (The entire bottom section of the
        // SideBandDisplay is the LO1 slider, i.e. each click there would
        // result in an LO1 adjustment.)
        slider.setEnabled(false);
        slider.setToolTipText("To change LO1 press right mouse button "
                + "and keep it pressed. "
                + "Then drag LO1 with left mouse button.");
        slider.addMouseListener(this);

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

        JLabel trxLabel = new JLabel("TRx", SwingConstants.CENTER);
        trxLabel.setForeground(Color.red);
        if (st != null && st.trxAvailable()) {
            area3.add(trxLabel, BorderLayout.NORTH);
        }

        if (st != null) {
            int preferredHeight = st.getPreferredSize().height;
            GraphScale gs = new GraphScale(0.0, 1.1, 0.5, 0.1, 0.0, 0,
                    preferredHeight, JSlider.VERTICAL);
            area3.add(gs, BorderLayout.EAST);

            area3.setPreferredSize(new Dimension(100, preferredHeight));
            area3.setSize(new Dimension(100, preferredHeight));
            titlePanel.add(area3);
        }

        JLabel label4 = new JLabel("FE Freq", SwingConstants.CENTER);
        JLabel label5 = new JLabel("LO1", SwingConstants.CENTER);
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

        pack();
    }

    public void setLO1(double lo1) {
        _lo1 = lo1;

        if (_lo1 < _lRangeLimit) {
            _lo1 = _lRangeLimit;
        }

        if (_lo1 > _uRangeLimit) {
            _lo1 = _uRangeLimit;
        }

        _ignoreEvents = true;

        if (slider != null) {
            int centre = (int) Math.rint(_lo1 / EdFreq.SLIDERSCALE);
            slider.setValue(centre);
        }

        _ignoreEvents = false;
    }

    public double getLO1() {
        return _lo1;
    }

    public void setMainLine(double frequency) {
        if (el != null) {
            el.setMainLine(frequency);
        }
    }

    public void setSideLine(double frequency) {
        if (el != null) {
            el.setSideLine(frequency);
        }
    }

    public void setRedshift(double redshift) {
        this.redshift = redshift;

        if (el != null) {
            el.setRedshift(redshift);
        }

        if (targetScale != null) {
            targetScale.setRedshift(redshift);
        }
    }

    public double getTopSubSystemCentreFrequency() {
        if (jt == null) {
            return 0.0;
        } else {
            return jt.getSamplers()[0].getCentreFrequency();
        }
    }

    public int getResolution(int subsystem) {
        if (jt == null) {
            return 0;
        } else {
            return jt.getSamplers()[subsystem].getResolution();
        }
    }

    public int getNumSubSystems() {
        if (jt == null) {
            return 0;
        } else {
            return jt.getSamplers().length;
        }
    }

    public void setCentreFrequency(double centre, int subsystem) {
        if (jt != null) {
            jt.getSamplers()[subsystem].setCentreFrequency(centre);
        }
    }

    public void setBandWidth(double width, int subsystem) {
        if (jt != null) {
            jt.getSamplers()[subsystem].setBandWidth(width);
        }
    }

    public void setLineText(String lineText, int subsystem) {
        if (jt != null) {
            jt.setLineText(lineText, subsystem);
        }
    }

    public void resetModeAndBand(String mode, String band) {
        if (jt != null) {
            jt.resetModeAndBand(mode, band);
        }
    }

    public void moveSlider(String band, double newPos, int subsystem) {
        double deltaF = 4.0e9 - newPos;

        if (jt != null) {
            jt.moveSlider(band, deltaF, subsystem);
        }
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
        Vector<Object>[] results = new Vector[jt.getSamplers().length];
        LineDetails details;
        String text;

        for (int i = 0; i < results.length; i++) {
            results[i] = new Vector<Object>();

            try {
                details = jt.getLineDetails(i);
                results[i].add(details.name);
                results[i].add(details.transition);
                results[i].add(new Double(details.frequency * 1.0E6));
            } catch (Exception e) {
                text = jt.getLineText(i);

                if (text != null) {
                    // Parse the string First see if it starts with "No Line"
                    if (text.startsWith(HeterodyneEditor.NO_LINE)) {
                        results[i].add(HeterodyneEditor.NO_LINE);
                        results[i].add(HeterodyneEditor.NO_LINE);

                        String frequency = text.substring(
                                text.lastIndexOf(' '));
                        double rf;

                        if (frequency.matches("\\d*.??\\d*")) {
                            rf = Double.parseDouble(frequency) * 1.0E6;
                        }
                        else {
                            rf = EdFreq.getRestFrequency(
                                    getLO1(),
                                    jt.getSamplers()[i].getCentreFrequency(),
                                    redshift,
                                    hetEditor.getFeBand());
                        }

                        results[i].add(new Double(rf));

                    } else {
                        // The molecule should be the first thing
                        String molecule = text.substring(0, text.indexOf("  "));
                        results[i].add(molecule.trim());

                        // Get the transition
                        String transition = text.substring(
                                text.indexOf("  ") + 1,
                                text.lastIndexOf("  "));
                        transition = transition.trim();

                        results[i].add(transition);

                        double f = Double.parseDouble(text.substring(
                                text.lastIndexOf(' '))) * 1.0E6;

                        results[i].add(new Double(f));
                    }
                }
            }

            double cf = jt.getSamplers()[i].getCentreFrequency();
            results[i].add(new Double(cf));

            double bw = jt.getSamplers()[i].getBandWidth();
            results[i].add(new Double(bw));

            int ch = jt.getSamplers()[i].getChannels();
            results[i].add(new Integer(ch));

            int res = jt.getSamplers()[i].getResolution();
            results[i].add(new Integer(res));
        }

        return results;
    }

    public static void main(String args[]) {
        // Create SideBandDisplay with anonymous HeterodyneEditor
        // implementation that does not do anything.
        SideBandDisplay sbt = new SideBandDisplay(new HeterodyneEditor() {

            public String getFeBand() {
                return "usb";
            }

            public String getMode() {
                return "dsb";
            }

            public double getRedshift() {
                return 0.0;
            }

            public double getRestFrequency(int subsystem) {
                return 0.0;
            }

            public double getObsFrequency(int subsystem) {
                return 0.0;
            }

            public void updateCentreFrequency(double centre, int subsystem) {
            }

            public void updateBandWidth(double width, int subsystem) {
            }

            public void updateChannels(int channels, int subsystem) {
            }

            public void updateLineDetails(LineDetails lineDetails,
                    int subsystem) {
            }

            public void updateLO1(double lo1) {
            }

            public double getCurrentBandwidth(int subsystem) {
                return 0.0;
            }
        });

        sbt.updateDisplay("Frequency editor test",
                365.0E+9, 375.0E+9, 4.0E9, 1.8E9, 1, 0.0,
                new double[]{0.25E9, 1.0E9},
                new int[]{8192, 2048},
                8);

        sbt.setVisible(true);
    }

    public void stateChanged(ChangeEvent e) {
        if (_ignoreEvents) {
            return;
        }

        _lo1 = (double) slider.getValue() * EdFreq.SLIDERSCALE;

        hetEditor.updateLO1(getLO1());
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

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

    /**
     * Sets an object to call back and it's method name by which to call it.
     *
     * Method should take no arguments.
     * If method cannot be found, call back will fail.
     *
     * @param object - object to call back.
     * @param methodName - name of the method to be called on this object.
     */
    public void setCallback(Object object, String methodName) {
        try {
            callBackMethod = object.getClass().getMethod(methodName,
                    new Class<?>[0]);
            callBackObject = object;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls an object by its call back method.
     *
     * If call back has not been correctly set up it will fail.
     */
    private void callBack() {
        if (callBackObject != null && callBackMethod != null) {
            try {
                callBackMethod.invoke(callBackObject, new Object[0]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
