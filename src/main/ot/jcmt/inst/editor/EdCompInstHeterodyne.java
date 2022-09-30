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

package ot.jcmt.inst.editor;

import javax.swing.JRadioButton;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Container;
import java.awt.Component;
import java.util.Vector;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.StringReader;
import java.io.IOException;
import java.net.URL;

import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import gemini.sp.SpTelescopePos;
import gemini.sp.obsComp.SpTelescopeObsComp;
import orac.util.TelescopeUtil;
import orac.jcmt.inst.SpInstHeterodyne;
import edfreq.HeterodyneEditor;
import edfreq.LineCatalog;
import edfreq.SideBandDisplay;
import edfreq.FrequencyEditorCfg;
import edfreq.Receiver;
import edfreq.BandSpec;
import edfreq.BandwidthOption;
import edfreq.Transition;
import edfreq.SelectionList;
import edfreq.LineDetails;
import jsky.app.ot.editor.OtItemEditor;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;

import jsky.app.ot.gui.TextBoxWidgetWatcher;

import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gemini.sp.SpAvEditState;
import gemini.util.ObservingToolUtilities;
import gemini.util.Range;

/**
 * Heterodyne Editor.
 *
 * This class implements the HeterodyneEditor interface. Its implementation is
 * based on the old edfreq.FrontEnd class which is not used anymore.
 *
 * @author Dennis Kelly (bdk@roe.ac.uk),
 *         modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class EdCompInstHeterodyne extends OtItemEditor implements
        ActionListener, HeterodyneEditor {
    private LineCatalog _lineCatalog;
    private SideBandDisplay _frequencyEditor;

    /**
     * A static configuration object which can used by classes throughout this
     * package to configure themselves.
     */
    protected static FrequencyEditorCfg _cfg =
            FrequencyEditorCfg.getConfiguration();
    private SpInstHeterodyne _inst;
    boolean _hidingFrequencyEditor = false;
    private HeterodyneGUI _w; // the GUI layout
    private Document doc = null;

    // Current receiver
    Receiver _receiver;

    // Information related to each possible spectral region
    @SuppressWarnings("unchecked")
    Vector<Object>[] _regionInfo = new Vector[
            HeterodyneGUI.SUBSYSTEMS[HeterodyneGUI.SUBSYSTEMS.length - 1]];
    boolean configured = false;
    static String LSB = "lsb";
    static String USB = "usb";
    static String BEST = "best";

    private static final Color myRed = new Color(255, 209, 186);
    private static final Color myYellow = new Color(255, 249, 182);
    private static final Color myBlue = new Color(200, 217, 255);
    private static final Color myGreen = new Color(200, 255, 200);

    public EdCompInstHeterodyne() {
        _title = "JCMT Heterodyne";
        _presSource = _w = new HeterodyneGUI();
        _description =
                "The Heterodyne instrument is configured with this component.";

        try {
            _lineCatalog = LineCatalog.getInstance();
        } catch (Exception e) {
        }

        if (_frequencyEditor == null) {
            _frequencyEditor = new SideBandDisplay(this);

            _frequencyEditor.addComponentListener(new ComponentAdapter() {
                public void componentHidden(ComponentEvent e) {
                    // If the user has deliberately closed the window without
                    // using the hide button, this will do the same except
                    // we won't get the latest configuration
                    if (!_hidingFrequencyEditor) {
                        enableNamedWidgets(true);
                        _updateWidgets();
                    }
                }
            });
        }

        // Add listeners to stuff on the front end configuration panel
        for (AbstractButton button: _w.feButtons.values()) {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    feAction(e);
                }
            });

        }

        for (AbstractButton button: _w.modeButtons.values()) {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    modeAction(e);
                }
            });
        }

        for (AbstractButton button: _w.regionButtons.values()) {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    regionAction(e);
                }
            });
        }

        for (AbstractButton button: _w.sbButtons.values()) {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sbSelectAction(e);
                }
            });
        }

        // Add other listeners to the components of the frequency and button
        // panels.  We don't need to add anything to the velocity panel since
        // it is for show only.
        for (JComboBox<BandwidthOption> bandwidth: _w.bandwidths) {
            bandwidth.addActionListener(this);
        }

        _w.acceptButton.addActionListener(this);
        _w.moleculeBox.addActionListener(this);
        _w.transitionBox.addActionListener(this);
        _w.freqText.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent ke) {
                freqAction();
            }
        });

        _w.specialConfigs.setModel(getSpecialConfigsModel());
        _w.specialConfigs.addActionListener(this);

        // Initially disable the accept button
        _w.acceptButton.setEnabled(false);

        // Add action listeners for the editor show/hide buttons.
        _w.showButton.addActionListener(this);
        _w.hideButton.addActionListener(this);

        // Disable the hide button...It should only be enabled when the
        // frequency editor is visible
        _w.hideButton.setEnabled(false);

        // Add an ancestor listener. This will be invoked when a user changes
        // to another component, and will force the accept button to be
        // pressed.
        _w.addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent e) {
            }

            public void ancestorMoved(AncestorEvent e) {
            }

            public void ancestorRemoved(AncestorEvent e) {
                clickButton(_w.acceptButton);
            }
        });

        _w.velocity.addWatcher(new TextBoxWidgetWatcher() {
            public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
                _w.acceptButton.setEnabled(true);
                _w.velocity.setForeground(Color.RED);
            }

            public void textBoxAction(TextBoxWidgetExt tbwe) {
            }
        });

        _w.vDef.setChoices(TelescopeUtil.TCS_RV_DEFINITIONS);
        _w.vDef.addWatcher(new DropDownListBoxWidgetWatcher() {
            public void dropDownListBoxAction(DropDownListBoxWidgetExt dd,
                    int i, String newTag) {
                _inst.setVelocityDefinition(newTag);

                if (SpInstHeterodyne.RADIAL_VELOCITY_REDSHIFT.equals(newTag)) {
                    _w.velLabel.setText("Redshift");
                    _inst.setVelocityFrame(
                            SpInstHeterodyne.BARYCENTRIC_VELOCITY_FRAME);

                } else {
                    _w.velLabel.setText("Velocity");

                    if (SpInstHeterodyne.RADIAL_VELOCITY_RADIO.equals(newTag)) {
                        _inst.setVelocityFrame(
                                SpInstHeterodyne.LSRK_VELOCITY_FRAME);
                    } else if (SpInstHeterodyne.RADIAL_VELOCITY_OPTICAL.equals(
                            newTag)) {
                        _inst.setVelocityFrame(
                                SpInstHeterodyne.HELIOCENTRIC_VELOCITY_FRAME);
                    }
                }

                _inst.setSkyFrequency(_inst.calculateSkyFrequency());

                moreSetUp();
                update();
            }
        });

        _w.vFrame.setChoices(TelescopeUtil.TCS_RV_FRAMES);
        _w.vFrame.addWatcher(new DropDownListBoxWidgetWatcher() {
            public void dropDownListBoxAction(DropDownListBoxWidgetExt dd,
                    int i, String newTag) {
                _inst.setVelocityFrame(newTag);
                update();
            }
        });

        _w.defaultToRadial.addWatcher(new CheckBoxWidgetWatcher() {
            public void checkBoxAction(CheckBoxWidgetExt cbwe) {
                if (cbwe.getBooleanValue()) {
                    _inst.getTable().rm(SpInstHeterodyne.ATTR_VELOCITY);
                    _inst.getTable().rm(SpInstHeterodyne.ATTR_VELOCITY_DEFINITION);
                    _inst.getTable().rm(SpInstHeterodyne.ATTR_VELOCITY_FRAME);
                } else {
                    _inst.setVelocity(_w.velocity.getText());
                    _inst.setVelocityDefinition(_w.vDef.getStringValue());
                    _inst.setVelocityFrame(_w.vFrame.getStringValue());
                }

                moreSetUp();
                update();
            }
        });

        _w.table.setDefaultRenderer(Object.class, new TableRowRenderer());

    }

    /**
     * Initialises the default values in SpInstHeterodyne.
     */
    private void _initialiseInstHeterodyne() {
        String frontEndName = _cfg.frontEnds[1];
        _receiver = _cfg.receivers.get(frontEndName);
        BandSpec bandSpec = _receiver.bandspecs.get(0);

        // Get hold of the lines in the upper side-band that. Make sure it is
        // not to close to the edge of the range so that the IF can default
        // to the front-end IF.  One of the lines should be a CO transition.
        // Find it it use it as default line.
        Vector<SelectionList> moleculeVector = _lineCatalog.returnSpecies(
                _receiver.loMin + _receiver.feIF,
                _receiver.loMax + _receiver.feIF);

        String molecule = "CO";
        String transitionName = "";
        String frequency = "";

        Transition transition = null;

        for (int i = 0; i < moleculeVector.size(); i++) {
            if (moleculeVector.get(i).toString().trim().equals(molecule)) {
                transition = moleculeVector.get(i).objectList.get(0);
            }
        }

        if (transition != null) {
            // Whenever a transition is taken from the LineCatalog and
            // consequently saved to it has to be trimmed in order to remove
            // the trailing white space that each transition in the
            // LineCatalog has.

            transitionName = transition.name.trim();
            frequency = "" + transition.frequency;
        }

        _inst.initialiseValues("acsis",                   // Back end name
                frontEndName,                             // front end
                _cfg.frontEndTable.get(frontEndName)[0],  // mode
                bandSpec.toString(),                      // band mode
                _cfg.frontEndMixers.get(frontEndName)[0], // mixer
                "" + bandSpec.defaultOverlaps[0],         // overlap
                BEST,                                     // band
                "" + _receiver.feIF,                      // centre frequency
                // The instrument's bandwith setting does not appear to be used anywhere,
                // so for now just fill in the total: lower half + upper half.
                // (See also the same thing in feAction.)
                "" + (_receiver.bandWidthLower + _receiver.bandWidthUpper), // FE bandwidth
                "" + bandSpec.getDefaultOverlapBandWidths()[0], // bandwidth
                molecule,                                 // molecule
                transitionName,                           // transition
                frequency                                 // rest frequency
        );
    }

    /**
     * Override setup to store away a reference to the SpInst item.
     */
    public void setup(SpItem spItem) {
        _inst = (SpInstHeterodyne) spItem;
        super.setup(spItem);

        moreSetUp();
        initialisedRegion = false;
    }

    private void moreSetUp() {
        setAvailableModes();
        setAvailableRegions();
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
        boolean cachedConfigured = configured;
        configured = false;

        if (!_inst.valuesInitialised()) {
            _initialiseInstHeterodyne();

            // Make sure we click the front end button to set things up
            // correctly.
            clickButton(_w.feButtons.get(_inst.getFrontEnd()));
            _initialiseRegionInfo();
        }

        boolean defaultToRadialVelocity = ! _inst.hasVelocityInformation();
        _w.defaultToRadial.setValue(defaultToRadialVelocity);

        _receiver = _cfg.receivers.get(_inst.getFrontEnd());

        // Update the front end panel
        _w.feButtons.get(_inst.getFrontEnd()).setSelected(true);
        _w.modeButtons.get(_inst.getMode()).setSelected(true);

        String band = _inst.getBandMode();

        _w.regionButtons.get(Integer.parseInt(band)).setSelected(true);

        _w.sbButtons.get(_inst.getBand()).setSelected(true);

        // Update the bandwidth
        _updateBandwidths();

        int active = new Integer(band);
        for (int i = 0; i < _w.bandwidths.size(); i++) {
            _w.bandwidths.get(i).setEnabled(i < active);
        }

        // Update the summary panel
        _w.lowFreq.setText("" + (int) (_receiver.loMin * 1.0E-9));
        _w.highFreq.setText("" + (int) (_receiver.loMax * 1.0E-9));

        // Update the velocity field.
        // First we need to see if we can find a target component somewhere
        // in scope.
        SpTelescopeObsComp target = SpTreeMan.findTargetList(_inst);
        String rv;
        String rvDefn;
        String rvFrame;

        if (target != null && defaultToRadialVelocity) {
            SpTelescopePos tp = target.getPosList().getBasePosition();
            rv = tp.getTrackingRadialVelocity();
            rvDefn = tp.getTrackingRadialVelocityDefn();
            rvFrame = tp.getTrackingRadialVelocityFrame();
        } else {
            rv = "" + _inst.getVelocity();
            rvDefn = _inst.getVelocityDefinition();
            rvFrame = _inst.getVelocityFrame();
        }

        // Now get the components on the velocity panel and set them to the
        // new values.
        _w.velocity.setText(rv);
        if (rvDefn != null) {
            _w.vDef.setSelectedItem(rvDefn);
        }
        if (rvFrame != null) {
            // Kludge: setting the velocity definition will have caused
            // vDef's watcher to alter the velocity frame, so restore it now.
            _inst.setVelocityFrame(rvFrame);

            _w.vFrame.setSelectedItem(rvFrame);
        }

        _w.velLabel.setEnabled(!defaultToRadialVelocity);
        _w.velocity.setEnabled(!defaultToRadialVelocity);
        _w.vdLabel.setEnabled(!defaultToRadialVelocity);
        _w.vDef.setEnabled(!defaultToRadialVelocity);
        _w.vfLabel.setEnabled(!defaultToRadialVelocity);
        _w.vFrame.setEnabled(!defaultToRadialVelocity);

        String selectedConfig = (String) _w.specialConfigs.getSelectedItem();
        String namedConfig = _inst.getNamedConfiguration();

        // if null set to first item which *should* be "None"
        if (namedConfig == null) {
            namedConfig = (String) _w.specialConfigs.getItemAt(0);
        }

        if (!selectedConfig.equals(namedConfig)) {
            _w.specialConfigs.removeActionListener(this);
            _w.specialConfigs.setSelectedItem(namedConfig);
            _w.specialConfigs.addActionListener(this);
            cachedConfigured = true;
        }

        // Make sure the molecule is accessible
        try {
            _doVelocityChecks();

        } catch (Exception e) {
            _w.velocity.setText("Invalid");
        }

        _updateMoleculeChoice();
        _adjustCentralFrequencies();

        _updateRegionInfo();

        _updateTable();

        _w.freqText.setText(Double.toString(_inst.getRestFrequency(0) / 1.0E9));

        _w.skyFreqText.setText(String.format("%.6f",  _inst.getSkyFrequency() / 1.0E9));

        configured = cachedConfigured;
    }

    private void _checkEditsWhenConfigured() {
        if (configured) {
            _inst.removeNamedConfiguration();
            _w.specialConfigs.removeActionListener(this);
            _w.specialConfigs.setSelectedIndex(0);
            _w.specialConfigs.addActionListener(this);
            configured = false;
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == _w.specialConfigs) {
            // If the user has selected None
            if (_w.specialConfigs.getSelectedIndex() == 0) {
                configured = false;
                _inst.removeNamedConfiguration();

            } else {
                String selectedConfig =
                        (String) _w.specialConfigs.getSelectedItem();
                _inst.setNamedConfiguration(selectedConfig);
                ConfigurationInformation ci = getConfigFor(selectedConfig);
                if (ci == null) {
                    return;
                }

                clickButton(_w.feButtons.get(ci.feName));
                clickButton(_w.sbButtons.get(ci.sideBand.toLowerCase()));
                clickButton(_w.modeButtons.get(ci.mode.toLowerCase()));
                clickButton(_w.regionButtons.get(ci.subSystems));

                for (int i = 0; i < _w.bandwidths.size(); i++) {
                    JComboBox<BandwidthOption> component = _w.bandwidths.get(i);

                    if (component.getItemCount() > 0) {
                        component.removeActionListener(this);

                        int index = 0;
                        if (i < ci.bandWidths.size()) {
                            double value = ci.bandWidths.get(i);
                            for (int j = 0; j < component.getItemCount(); j ++) {
                                BandwidthOption option = component.getItemAt(j);
                                if (option.bandwidth == value) {
                                    index = j;
                                    break;
                                }
                            }
                        }

                        component.setSelectedIndex(index);
                        BandwidthOption option = component.getItemAt(index);
                        _inst.setBandWidth(option.bandwidth, i);
                        _inst.setChannels(option.channels, i);

                        component.addActionListener(this);
                    }
                }

                if (ci.species.size() > 0) {
                    for (int index = 0; index < ci.subSystems; index++) {
                        String species = ci.species.get(index);
                        _inst.setMolecule(species, index);
                        String speciesTransition = ci.transition.get(index);
                        _inst.setTransition(speciesTransition, index);
                        _updateTransitionChoice();

                        Vector<SelectionList> moleculeVector = getSpecies();

                        Transition transition = null;

                        for (int i = 0; i < moleculeVector.size(); i++) {
                            SelectionList selectionList = moleculeVector.get(i);

                            if (selectionList.toString().trim().equals(species)) {
                                for (int j = 0;
                                        j < selectionList.objectList.size();
                                        j++) {
                                    transition =
                                            selectionList.objectList.get(j);
                                    String transitionName =
                                            transition.name.trim();

                                    if (transitionName.equals(
                                            speciesTransition)) {
                                        break;
                                    }

                                    transition = null;
                                }

                                break;
                            }
                        }

                        if (transition != null) {
                            double transitionFrequency = transition.frequency;
                            _inst.setRestFrequency(transitionFrequency, index);
                        }

                        checkSideband();
                    }

                    _updateRegionInfo();

                } else {
                    // Set the rest frequency text...
                    _w.freqText.setText(ci.freq.toString());

                    for (int index = 0; index < ci.subSystems; index++) {
                        double frequency = ci.freq.elementAt(index);
                        _inst.setRestFrequency(frequency * 1.0E9, index);
                        _inst.setMolecule(NO_LINE, index);
                        _inst.setTransition(NO_LINE, index);
                    }

                    checkSideband();
                }

                clickButton(_w.acceptButton);
                _updateCentralFrequenciesFromShifts(ci.shifts);
                configured = true;
            }

            _updateWidgets();
            return;
        }

        _checkEditsWhenConfigured();

        for (int i = 0; i < _w.bandwidths.size(); i++) {
            JComboBox<BandwidthOption> bandwidth = _w.bandwidths.get(i);

            if (source == bandwidth) {
                BandwidthOption option = (BandwidthOption) bandwidth.getSelectedItem();
                _inst.setBandWidth(option.bandwidth, i);
                _inst.setChannels(option.channels, i);

                setAvailableRegions();
                _updateRegionInfo();
                _updateWidgets();

                return;
            }
        }

        try {
            if (source == _w.moleculeBox) {
                // Set the current molecule and update the transitions
                for (int index = 0; index < _regionInfo.length; index++) {
                    _inst.setCentreFrequency(_receiver.feIF, index);
                    _inst.setMolecule(
                            _w.moleculeBox.getSelectedItem().toString(),
                            index);
                }

                _updateTransitionChoice();
                double restFreq = _inst.getRestFrequency(0);

                for (int index = 0; index < _regionInfo.length; index++) {
                    _inst.setRestFrequency(restFreq, index);
                }

                checkSideband();
                _updateRegionInfo();

            } else if (source == _w.transitionBox) {
                // Update the frequency information
                Object transition = _w.transitionBox.getSelectedItem();

                if (transition instanceof Transition) {
                    double frequency = ((Transition) transition).frequency;
                    _inst.setSkyFrequency(frequency / (1.0 + getRedshift()));

                    for (int index = 0; index < _regionInfo.length; index++) {
                        // Set the current transition
                        _inst.setCentreFrequency(_receiver.feIF, index);
                        _inst.setTransition(transition.toString(), index);
                        _inst.setRestFrequency(frequency, index);
                    }

                    _w.freqText.setText(Double.toString(frequency / 1.0E9));
                    checkSideband();
                }

                _updateRegionInfo();

            } else if (source == _w.acceptButton) {
                // Set the current Rest Frequency
                // Get the text field widget
                String frequency = _w.freqText.getText();
                String velocity = _w.velocity.getText();
                boolean changed = ! frequency.equals(
                        Double.toString(_inst.getRestFrequency(0) / 1.0E9));

                // Only set the instrument velocity if we don't inherit it
                // from an associated target component.
                if (_inst.hasVelocityInformation()) {
                    _inst.setVelocity(velocity);
                }

                try {
                    if (changed) {
                        double f = Double.parseDouble(frequency) * 1.0E9;

                        Range restFreqRange = getRestFrequencyRange();

                        if (restFreqRange.contains(f)) {
                            for (int index = 0; index < _regionInfo.length;
                                    index++) {
                                _inst.setRestFrequency(f, index);
                                _inst.setCentreFrequency(_receiver.feIF, index);

                                // Set the molecule and trasition to NO_LINE
                                _inst.setMolecule(NO_LINE, index);
                                _inst.setTransition(NO_LINE, index);
                            }

                            checkSideband();
                        }
                        else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "The requested frequency is outside the\n"
                                    + "tuning range of the receiver.",
                                    "Frequency out of range",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    _inst.setSkyFrequency(_inst.calculateSkyFrequency());

                    _updateMoleculeChoice();
                    _updateRegionInfo();

                } catch (Exception e) {
                }

                _w.freqText.setForeground(Color.BLACK);
                _w.velocity.setForeground(Color.BLACK);
                _w.acceptButton.setEnabled(false);

            } else if (source == _w.showButton) {
                checkSideband();
                configureFrequencyEditor();
                enableNamedWidgets(false);
                _frequencyEditor.setVisible(true);

            } else if (source == _w.hideButton) {
                hideFreqEditor();

            } else {
                String name = ((Component) source).getName();
                System.out.println("Unknown source for action: " + name);
            }

        } catch (Exception e) {
            // Ignore for now
        }

        _updateWidgets();
    }

    public void hideFreqEditor() {
        _hidingFrequencyEditor = true;
        getFrequencyEditorConfiguration();
        enableNamedWidgets(true);
        _updateRegionInfo();
        _frequencyEditor.setVisible(false);
        _hidingFrequencyEditor = false;
        _updateTable();
    }

    private void _adjustCentralFrequencies() {
        String band = _inst.getBand();
        // Get current space of first system
        double mainline = _inst.calculateSkyFrequency(0);
        double centre = _inst.getCentreFrequency(0);
        // Check if it is correctly located

        if (centre <= _receiver.feIF - _receiver.bandWidthLower
                || centre >= _receiver.feIF + _receiver.bandWidthUpper) {
            centre = _receiver.feIF;
            _inst.setCentreFrequency(centre, 0);

            // Make an educated guess as to wether this program has been just
            // been opened.
            if (_inst.getRootItem().getAvEditState()
                    == SpAvEditState.UNEDITED) {
                JOptionPane.showMessageDialog(
                        null,
                        "The Central Frequency for this set up was invalid."
                        + "\n It is advised that you save this program"
                        + " before use.",
                        "Central Frequency Reset",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        if (centre == _receiver.feIF) {
            double obsmin = _receiver.loMin - _receiver.feIF
                    - _receiver.bandWidthUpper;
            double obsmax = _receiver.loMax + _receiver.feIF
                    + _receiver.bandWidthUpper;

            if (LSB.equals(band) && mainline < obsmin + _receiver.bandWidthUpper
                    && !(mainline < obsmin)) {
                double currentPosition = obsmin + _receiver.bandWidthUpper;
                centre = centre - (mainline - currentPosition);
                _inst.setCentreFrequency(centre, 0);

            } else if ((USB.equals(band) || BEST.equals(band))
                    && mainline > obsmax - _receiver.bandWidthUpper
                    && !(mainline > obsmax)) {
                double currentPosition = obsmax - _receiver.bandWidthUpper;
                centre = centre + (mainline - currentPosition);
                _inst.setCentreFrequency(centre, 0);
            }
        }

        // Adjust remaining systems accordingly
        int available = new Integer(_inst.getBandMode());

        for (int index = 1; index < available; index++) {
            double line = _inst.calculateSkyFrequency(index);

            if (USB.equals(band) || BEST.equals(band)) {
                line = centre - (mainline - line);
            } else {
                line = centre + (mainline - line);
            }

            if (line < 0.0) {
                line = -line;
            }

            double halfInstBandWidth = 0.5 * _inst.getBandWidth(index);
            boolean outOfBand = false;

            if (line > (_receiver.feIF + _receiver.bandWidthUpper - halfInstBandWidth)) {
                line = (_receiver.feIF + _receiver.bandWidthUpper - halfInstBandWidth);
                outOfBand = true;

            } else if (line < (_receiver.feIF - _receiver.bandWidthLower + halfInstBandWidth)) {
                line = (_receiver.feIF - _receiver.bandWidthLower + halfInstBandWidth);
                outOfBand = true;
            }

            _inst.setCentreFrequency(line, index);

            if (outOfBand) {
                JOptionPane.showMessageDialog(
                        null,
                        "The central frequency for spectral region "
                                + index
                                + " is outside the tuning range of the"
                                + " receiver.\nIt is advised that you"
                                + " reconfigure this spectral region"
                                + " and save this program before use.",
                        "Central Frequency Reset",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void _updateCentralFrequenciesFromShifts(Vector<Double> shifts) {
        for (int index = 0; index < shifts.size(); index++) {
            double frequency = shifts.elementAt(index);

            if (frequency != 0.0) {
                _inst.setCentreFrequency("" + frequency, index);
            }
        }
    }

    public void feAction(ActionEvent ae) {
        _checkEditsWhenConfigured();

        if (ae != null) {
            // This has been called as a response to a user action
            String feSelected = ((JRadioButton) ae.getSource()).getText();
            _receiver = _cfg.receivers.get(feSelected);
            _inst.setFrontEnd(feSelected);
            _inst.setFeIF(_receiver.feIF);
            // The instrument's bandwith setting does not appear to be used anywhere,
            // so for now just fill in the total: lower half + upper half.
            // (See also the same thing in _initialiseInstHeterodyne.)
            _inst.setFeBandWidth(_receiver.bandWidthLower + _receiver.bandWidthUpper);
            setAvailableModes();
            setAvailableRegions();

        } else {
            // This has been called as a result of a call from another action
            // event
        }

        _updateWidgets();
    }

    public void modeAction(ActionEvent ae) {
        _checkEditsWhenConfigured();

        if (ae != null) {
            // User action
            String mode = ((JRadioButton) ae.getSource()).getText();
            _inst.setMode(mode);

            // Update the band width choices
        }

        _updateWidgets();
    }

    public void sbSelectAction(ActionEvent ae) {
        _checkEditsWhenConfigured();

        if (ae != null) {
            String sb = ((JRadioButton) ae.getSource()).getText();

            if (!validSideband(sb)) {
                JOptionPane.showMessageDialog(null,
                        "Frequency cannot be reached from " + sb + ".",
                        "Frequency no longer valid for Sideband!",
                        JOptionPane.WARNING_MESSAGE);
            }

            // If we are switching between upper and lower sidebands we need
            // to alter the centre frequency for higher level subsytems
            if (Integer.parseInt(_inst.getBandMode()) > 1) {
                String currentBand = _inst.getBand();

                if (currentBand.equals(BEST) && sb.equals(USB)
                        || currentBand.equals(USB) && sb.equals(BEST)) {
                    // Do nothing since 'best' is equivalent to 'usb' as far
                    // as the OT is concerned

                } else {
                    double receverfeIF = _receiver.feIF;

                    for (int i = 0; i < Integer.parseInt(_inst.getBandMode());
                            i++) {
                        double topCentreFreq = _inst.getCentreFrequency(i);
                        topCentreFreq = receverfeIF
                                + (receverfeIF - topCentreFreq);
                        _inst.setCentreFrequency(topCentreFreq, i);
                    }
                }
            }

            _inst.setBand(sb);
        }

        _updateWidgets();
    }

    public void regionAction(ActionEvent ae) {
        _checkEditsWhenConfigured();

        if (ae != null) {
            String regions = ((JRadioButton) ae.getSource()).getText();
            _inst.setBandMode(regions);
            int active = new Integer(regions);

            for (int i = 0; i < _w.bandwidths.size(); i ++) {
                _w.bandwidths.get(i).setEnabled(i < active);
            }
        }

        _updateRegionInfo();
        _updateWidgets();
    }

    private void freqAction() {
        _checkEditsWhenConfigured();

        // Called when user changes the frequency manually
        _w.acceptButton.setEnabled(true);
        _w.freqText.setForeground(Color.RED);
    }

    // Added by MFO (8 January 2002)
    /**
     * Returns "usb" (Upper Side Band) or "lsb" (Lower Side Band).
     *
     * Needed by {@link edfreq.SideBand} to shift LO1 when top SideBands
     * are changed.
     */
    public String getFeBand() {
        return _inst.getBand();
    }

    public String getMode() {
        return _inst.getMode();
    }

    private void _updateBandwidths() {
        boolean showDialog = false;

        List<SpInstHeterodyne.BandSpecSelection> selection = _inst.getBandSpecSelection(_receiver);

        for (int bandwidthIndex = 0; bandwidthIndex < _w.bandwidths.size();
                bandwidthIndex++) {
            JComboBox<BandwidthOption> bandwidth = _w.bandwidths.get(bandwidthIndex);
            bandwidth.removeActionListener(this);
            int originalIndex = bandwidth.getSelectedIndex();

            bandwidth.removeAllItems();

            if (! (bandwidthIndex < selection.size())) {
                continue;
            }

            // Set the new bandwidths
            SpInstHeterodyne.BandSpecSelection thisSelection = selection.get(bandwidthIndex);
            BandSpec activeBandSpec = thisSelection.bandSpec;
            int index = thisSelection.match;
            int notableIndex = thisSelection.nearest;

            double[] values = activeBandSpec.getDefaultOverlapBandWidths();
            int[] channels = activeBandSpec.getDefaultOverlapChannels();

            for (int i = 0; i < values.length; i++) {
                bandwidth.addItem(new BandwidthOption(values[i], channels[i], activeBandSpec.numCMs[i]));
            }

            if (index != -1) {
                bandwidth.setSelectedIndex(index);
                _inst.setOverlap(
                        activeBandSpec.defaultOverlaps[index],
                        bandwidthIndex);
                _inst.setChannels(
                        channels[index],
                        bandwidthIndex);
            }
            else if (notableIndex != -1) {
                // Retrieve current value to show in the dialog box.
                double currentBandwidth = _inst.getBandWidth(bandwidthIndex);

                bandwidth.setSelectedIndex(notableIndex);
                _inst.setBandWidth(values[notableIndex], bandwidthIndex);
                _inst.setOverlap(activeBandSpec.defaultOverlaps[notableIndex],
                        bandwidthIndex);
                _inst.setChannels(
                        channels[notableIndex],
                        bandwidthIndex);

                if (originalIndex != -1) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Previous bandwidth for subsystem "
                                    + (bandwidthIndex + 1)
                                    + " not available ;\n Using "
                                    + bandwidth.getSelectedItem()
                                    + " as it is closest to previous value of "
                                    + Math.rint(currentBandwidth * 1.0E-6),
                            "Bandwidth Reset", JOptionPane.WARNING_MESSAGE);
                }
            }
            else {
                bandwidth.setSelectedIndex(0);
                _inst.setBandWidth(values[0], bandwidthIndex);
                _inst.setOverlap(activeBandSpec.defaultOverlaps[0],
                        bandwidthIndex);
                _inst.setChannels(
                        channels[0],
                        bandwidthIndex);

                if (originalIndex != -1) {
                    showDialog = true;
                }
            }

            bandwidth.addActionListener(this);
        }

        if (showDialog) {
            JOptionPane.showMessageDialog(
                    null,
                    "Previous bandwidth not available with new settings;"
                        + "\n resetting to default",
                    "Bandwidth Reset", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void _updateMoleculeChoice() {
        JComboBox molBox = _w.moleculeBox;

        // Remove the current actionlistener
        molBox.removeActionListener(this);
        Object currentSelection = getObject(molBox, _inst.getMolecule(0));
        String currentSpecies = null;

        if (currentSelection != null) {
            currentSpecies = currentSelection.toString();
        } else {
            currentSpecies = _inst.getMolecule(0);
        }

        // Get a new model to add to the molBox
        DefaultComboBoxModel specModel =
                new DefaultComboBoxModel(getSpecies());
        molBox.setModel(specModel);

        // Add a"NO LINE option if one does not already exist
        if (((DefaultComboBoxModel) molBox.getModel()).getIndexOf(NO_LINE)
                == -1) {
            molBox.addItem(NO_LINE);
        }

        // Go through the new model and see if:
        // (a) the same combination of species/transition is available or
        // (b) the same species is available.
        // In addition, we will actually set things up on the box
        // instead of leaving it to update widgets, since it means
        // we do not need to get the species list again
        if (currentSelection != null
                && specModel.getIndexOf(currentSelection) >= 0) {
            molBox.setSelectedIndex(specModel.getIndexOf(currentSelection));

        } else if (NO_LINE.equals(currentSpecies)) {
            molBox.setSelectedItem(NO_LINE);

        } else {
            boolean match = false;

            // See if the current species is available ;
            // don't use the last element of the model since this is just
            // the no-line option
            if (currentSpecies != null) {
                for (int i = 0; i < specModel.getSize() - 1; i++) {
                    if (((SelectionList) specModel.getElementAt(i)).toString()
                            .equals(currentSpecies)) {
                        match = true;
                        molBox.setSelectedIndex(i);

                        break;
                    }
                }
            }

            if (!match) {
                // Set the molecule to the first available one
                JOptionPane.showMessageDialog(_w,
                        "Selecting new species ; old selection ("
                                + currentSpecies + ") out of range",
                        "Molecule changed", JOptionPane.WARNING_MESSAGE);

                int integer = 0;

                try {
                    integer = Integer.parseInt(_inst.getBandMode());
                } catch (NumberFormatException nfe) {
                }

                Object item = specModel.getElementAt(0);
                for (int i = 0; i < integer; i++) {
                    if (item instanceof SelectionList) {
                        _inst.setMolecule(((SelectionList) item).toString(), i);
                    }
                    else {
                        _inst.setMolecule(NO_LINE, i);
                    }
                }

                molBox.setSelectedIndex(0);
            }
        }

        molBox.addActionListener(this);
        _updateTransitionChoice();
    }

    private void _updateTransitionChoice() {
        JComboBox transBox = _w.transitionBox;

        // Remove the current actionlistener
        transBox.removeActionListener(this);

        String currentTransition = _inst.getTransition(0);

        if (currentTransition != null) {
            currentTransition = currentTransition.trim();
        }

        String currentMolecule = _inst.getMolecule(0);

        if (currentMolecule == null) {
            transBox.addActionListener(this);

            return;
        }

        if (NO_LINE.equals(currentMolecule)) {
            if (((DefaultComboBoxModel) transBox.getModel())
                    .getIndexOf(NO_LINE) == -1)
                transBox.addItem(NO_LINE);
            transBox.setSelectedItem(NO_LINE);
            transBox.addActionListener(this);

            for (int index = 0; index < _regionInfo.length; index++) {
                _inst.setTransition(NO_LINE, index);
            }

            return;
        }

        // We need to get the current SelectionList based on the molecule.
        // First get all the available species
        // Get the new model

        Vector<SelectionList> speciesList = getSpecies();

        // Loop through this list to get the element corresponding to the
        // current molecule
        SelectionList currentSpecies = null;

        for (int i = 0; i < speciesList.size(); i++) {
            SelectionList currentSelectionList = speciesList.get(i);

            if (currentSelectionList.toString().equals(currentMolecule)) {
                currentSpecies = currentSelectionList;

                break;
            }
        }

        if (currentSpecies != null) {
            DefaultComboBoxModel specModel =
                    new DefaultComboBoxModel(currentSpecies.objectList);
            transBox.setModel(specModel);

            // Add the no line option
            if (specModel.getIndexOf(NO_LINE) == -1) {
                transBox.addItem(NO_LINE);
            }
        }

        // Check if the previous transition is still in range
        boolean inRange = false;

        if (currentTransition != null) {
            for (int i = 0; i < transBox.getItemCount(); i++) {
                if (transBox.getItemAt(i).toString().trim()
                        .equals(currentTransition)) {
                    inRange = true;
                    transBox.setSelectedIndex(i);

                    break;
                }
            }
        }

        boolean transChanged = false;

        if (!inRange) {
            // We need to set the transition to the first available
            // for the current species
            JOptionPane.showMessageDialog(null,
                    "Transition Changed: "
                        + currentTransition + " out of range.",
                    "Transition Changed!",
                    JOptionPane.PLAIN_MESSAGE);

            for (int index = 0; index < _regionInfo.length; index++) {
                _inst.setMolecule(currentMolecule, index);
                Transition transition = (Transition) transBox.getItemAt(0);

                if (transition != null) {
                    _inst.setTransition(transition.toString(), index);
                    _inst.setRestFrequency(transition.frequency, index);
                    _inst.setCentreFrequency(_receiver.feIF, index);
                    transBox.setSelectedIndex(0);
                }
            }

            transChanged = true;
        }

        double frequency = 0.0;

        if (transBox.getSelectedItem() instanceof Transition) {
            frequency = ((Transition) transBox.getSelectedItem()).frequency;
        } else {
            frequency = _inst.getRestFrequency(0);
        }

        _inst.setRestFrequency(frequency, 0);
        _inst.setSkyFrequency(_inst.calculateSkyFrequency());

        _w.freqText.setText(Double.toString(frequency / 1.0E9));

        if (transChanged) {
            _updateRegionInfo();
        }

        transBox.addActionListener(this);
    }

    private Vector<SelectionList> getSpecies() {
        Range restFreqRange = getRestFrequencyRange();

        return _lineCatalog.returnSpecies(restFreqRange.min, restFreqRange.max);
    }

    public double getRedshift() {
        return _inst.calculateRedshift();
    }

    public double getCurrentBandwidth(int subsystem) {
        return _inst.getBandWidth(subsystem);
    }

    public int getCurrentChannels(int subsystem) {
        return _inst.getChannels(subsystem);
    }

    private DefaultComboBoxModel getSpecialConfigsModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        // Default special config...
        model.addElement("None");

        // Try to open the special configs file
        String fileName = System.getProperty("ot.cfgdir");

        if (!fileName.endsWith("/")) {
            fileName += '/';
        }

        fileName += "ACSISModes.xml";
        URL url = ObservingToolUtilities.resourceURL(fileName);

        if (url != null) {
            try {
                InputStream is = url.openStream();
                InputStreamReader reader = new InputStreamReader(is);
                int readLength = 0;
                char[] chars = new char[1024];
                StringBuffer buffer = new StringBuffer();

                while (!reader.ready()) {
                }

                while ((readLength = reader.read(chars)) != -1) {
                    buffer.append(chars, 0, readLength);
                }

                reader.close();
                is.close();

                String buffer_z = buffer.toString();
                DOMParser parser = new DOMParser();
                parser.setFeature("http://xml.org/sax/features/validation",
                        false);
                parser.setFeature(
                        "http://apache.org/xml/features/dom/include-ignorable-whitespace",
                        false);
                parser.parse(new InputSource(new StringReader(buffer_z)));
                doc = parser.getDocument();

                if (doc != null) {
                    NodeList nl = doc.getElementsByTagName("name");

                    for (int j = 0; j < nl.getLength(); j++) {
                        String name = nl.item(j).getFirstChild().getNodeValue()
                                .trim();
                        model.addElement(name);
                    }
                }

            } catch (SAXNotRecognizedException snre) {
                System.out.println("Unable to ignore white-space text.");

            } catch (SAXNotSupportedException snse) {
                System.out.println("Unable to ignore white-space text.");

            } catch (SAXException sex) {
                System.out.println("SAX Exception on parse.");

            } catch (IOException ioe) {
                System.out.println("IO Exception on parse.");

            }
        }

        return model;
    }

    private ConfigurationInformation getConfigFor(String name) {
        if (doc == null) {
            return null;
        }

        ConfigurationInformation ci = new ConfigurationInformation();
        // Get the correct config item from the document
        Node nodeToUse = null;
        NodeList nl = doc.getElementsByTagName("configuration");

        for (int i = 0; i < nl.getLength(); i++) {
            nodeToUse = nl.item(i);
            // Get the name associated with this
            NodeList children = nodeToUse.getChildNodes();
            String nodeName = "none";

            for (int j = 0; j < children.getLength(); j++) {
                Node child = children.item(j);

                if (child.getNodeName().equals("name")) {
                    nodeName = child.getFirstChild().getNodeValue().trim();
                    break;
                }
            }

            if (nodeName.equals(name)) {
                break;
            }
        }

        // We now have the correct node hopefully, so fill in the ci structure
        ci.shifts.clear();

        if (nodeToUse != null) {
            NodeList children = nodeToUse.getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                String childName = child.getNodeName();
                Node firstChild = child.getFirstChild();

                if (firstChild == null) {
                    continue;
                }

                String childValue = firstChild.getNodeValue();

                if (childValue == null || "".equals(childValue)) {
                    continue;
                }

                childValue = childValue.trim();

                if (childName.equals("name")) {
                    ci.name = childValue;
                } else if (childName.equals("frontEnd")) {
                    ci.feName = childValue;
                } else if (childName.equals("sideband")) {
                    ci.sideBand = childValue.toUpperCase();
                } else if (childName.equals("mode")) {
                    ci.mode = childValue.toUpperCase();
                } else if (childName.equals("frequency")) {
                    ci.freq.add(new Double(childValue));
                } else if (childName.equals("mixers")) {
                    ci.mixers = new Integer(childValue);
                } else if (childName.equals("systems")) {
                    ci.subSystems = new Integer(childValue);
                } else if (childName.equals("species")) {
                    ci.species.add(childValue);
                } else if (childName.equals("transition")) {
                    ci.transition.add(childValue);
                } else if (childName.equals("bandwidth")) {
                    ci.bandWidths.add(Double.parseDouble(childValue) * 1.0E6);
                } else if (childName.equals("shift")) {
                    ci.shifts.add(new Double(childValue));
                }
            }
        }

        return ci;
    }

    private void setAvailableModes() {
        List<String> availableModes = Arrays.asList(
                _cfg.frontEndTable.get(_inst.getFrontEnd()));
        String currentMode = _inst.getMode();
        boolean changeMode = false;

        // We need to see compare the available modes with the list of
        // total modes, and disable any that are not currently available
        for (Map.Entry<String, AbstractButton> entry: _w.modeButtons.entrySet()) {
            String str = entry.getKey();

            if (availableModes.contains(str)) {
                // Make sure the widget is enabled
                entry.getValue().setEnabled(true);

            } else {
                // disable the widget and optionally chnage the current mode
                if (currentMode != null && currentMode.equalsIgnoreCase(str)) {
                    changeMode = true;
                }

                entry.getValue().setEnabled(false);
            }
        }

        if (changeMode) {
            _inst.setMode(availableModes.get(0));
        }
    }

    private void setAvailableRegions() {
        Vector<BandSpec> bandspecs = _receiver.bandspecs;
        Vector<String> available = new Vector<String>();

        for (int i = 0; i < bandspecs.size(); i++) {
            available.add(bandspecs.get(i).toString());
        }

        String current = _inst.getBandMode();
        boolean change = false;

        for (Map.Entry<Integer, AbstractButton> entry: _w.regionButtons.entrySet()) {
            String str = Integer.toString(entry.getKey());

            if (available.contains(str)) {
                // Make sure the widget is enabled
                entry.getValue().setEnabled(true);

            } else {
                // disable the widget and optionally change the current mode
                if (current != null && current.equalsIgnoreCase(str)) {
                    change = true;
                }

                entry.getValue().setEnabled(false);
            }
        }

        if (change) {
            String defaultRegion = available.get(0);
            _inst.setBandMode(defaultRegion);
            clickButton(_w.regionButtons.get(Integer.parseInt(defaultRegion)));
        }
    }

    private Range getRestFrequencyRange() {
        double redshift = getRedshift();

        double obsmin = _receiver.loMin - _receiver.feIF
                - _receiver.bandWidthUpper;

        double obsmax = _receiver.loMax + _receiver.feIF
                + _receiver.bandWidthUpper;

        return new Range(
                obsmin * (1.0 + redshift),
                obsmax * (1.0 + redshift));
    }

    private void clickButton(AbstractButton button) {
        if (button == null) {
            System.out.println("Could not find button to 'click'.");
            return;
        }

        boolean configuredState = configured;
        configured = false;
        button.doClick();
        configured = configuredState;
    }

    private Object getObject(JComboBox comboBox, String name) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).toString().equalsIgnoreCase(name)) {
                return comboBox.getItemAt(i);
            }
        }

        return null;
    }

    private void _updateTable() {
        // Just collect together all the information for each spectral regions
        try {
            DefaultTableModel tbm = new DefaultTableModel();
            tbm.setColumnIdentifiers(_w.COLUMN_NAMES);

            for (int i = 0; i < Integer.parseInt(_inst.getBandMode()); i++) {
                Vector<Object> row = new Vector<Object>(_regionInfo[i]);
                row.add(0, new Integer(i));
                tbm.addRow(row);
            }

            _w.table.setModel(tbm);

        } catch (Exception e) {
        }
    }

    boolean initialisedRegion = false;

    /**
     * This routine initialises the region info.
     *
     * It assumes that the zeroth component is set and it just copies the
     * zeroth values for now.  The vector contains the following elements
     * related to each spectral region:
     *
     * 1. Molecule
     * 2. Transition
     * 3. Rest Frequency
     * 4. Central Frequency
     * 5. Band width
     * 6. Resolution
     * 7. number of channels
     */
    private void _initialiseRegionInfo() {
        for (int i = 0; i < _regionInfo.length; i++) {
            if (_regionInfo[i] == null) {
                _regionInfo[i] = new Vector<Object>();
            } else {
                _regionInfo[i].clear();
            }

            if (_inst.getMolecule(i) == null) {
                _inst.setMolecule(_inst.getMolecule(0), i);
                _regionInfo[i].add(_inst.getMolecule(i));
            } else {
                _regionInfo[i].add(_inst.getMolecule(i));
            }

            if (_inst.getTransition(i) == null) {
                _inst.setTransition(_inst.getTransition(0), i);
                _regionInfo[i].add(_inst.getTransition(i));
            } else {
                _regionInfo[i].add(_inst.getTransition(i));
            }

            if (_inst.getRestFrequency(i) == 0.0) {
                double restFreq = _inst.getRestFrequency(0);
                _inst.setRestFrequency(restFreq, i);
                // Only set the sky frequency to match based on the rest
                // frequency of the first subsystem.  (Probably not important
                // as we're using the first subsystem's rest frequency, but
                // in principle we should only update sky frequency when
                // changing the first subsystem.)
                if (i == 0) {
                    _inst.setSkyFrequency(_inst.calculateSkyFrequency());
                }
                _regionInfo[i].add(new Double(
                        _inst.getRestFrequency(i) / 1.0E9));
            } else {
                _regionInfo[i].add(new Double(
                        _inst.getRestFrequency(i) / 1.0E9));
            }

            if (_inst.getCentreFrequency(i) == 0.0) {
                _inst.setCentreFrequency(_inst.getCentreFrequency(0), i);
                _regionInfo[i].add(new Double(_inst.getCentreFrequency(i)));
            } else {
                _regionInfo[i].add(new Double(_inst.getCentreFrequency(i)));
            }

            if (_inst.getBandWidth(i) == 0.0) {
                _inst.setBandWidth(_inst.getBandWidth(0), i);
                double bandwidth = _inst.getBandWidth(i);
                _regionInfo[i].add(new Double(bandwidth));

                int resolution = (int) Math.rint((bandwidth * 1.0E-3)
                        / _inst.getChannels(0));

                // Need to add resolution here
                _regionInfo[i].add(new Integer(resolution));
            } else {
                double bandwidth = _inst.getBandWidth(i);
                _regionInfo[i].add(new Double(bandwidth));
                int resolution = (int) Math.rint((bandwidth * 1.0E-3)
                        / _inst.getChannels(i));

                // Need to add resolution here
                _regionInfo[i].add(new Integer(resolution));
            }

            if (_inst.getChannels(i) == 0) {
                // unlikely to happen
                _inst.setChannels(_inst.getChannels(0), i);
                _regionInfo[i].add(new Integer(_inst.getChannels(i)));
            } else {
                _regionInfo[i].add(new Integer(_inst.getChannels(i)));
            }
        }

        initialisedRegion = true;
    }

    private void _updateRegionInfo() {
        if (!initialisedRegion) {
            _initialiseRegionInfo();
        }

        List<SpInstHeterodyne.BandSpecSelection> selection = _inst.getBandSpecSelection(_receiver);

        for (int i = 0; i < _regionInfo.length; i++) {
            if (_regionInfo[i] == null) {
                _regionInfo[i] = new Vector<Object>();
            } else {
                _regionInfo[i].clear();
            }

            _regionInfo[i].add(_inst.getMolecule(i));
            _regionInfo[i].add(_inst.getTransition(i));
            _regionInfo[i].add(new Double(_inst.getRestFrequency(i) / 1.0E9));
            _regionInfo[i].add(new Double(_inst.getCentreFrequency(i)));
            _regionInfo[i].add(new Double(_inst.getBandWidth(i)));

            // Get the overlap and overlap based on the current b/w
            if (i < selection.size()) {
                SpInstHeterodyne.BandSpecSelection thisSelection = selection.get(i);
                BandSpec activeBandSpec = thisSelection.bandSpec;
                int j = thisSelection.match;

                if (j != -1) {
                    double overlap = activeBandSpec.defaultOverlaps[j];
                    int channels = activeBandSpec.getDefaultOverlapChannels()[j];
                    int resolution = (int) Math.rint(
                            (_inst.getBandWidth(i) * 1.0E-3) / channels);
                    _regionInfo[i].add(new Integer(resolution));
                    _regionInfo[i].add(new Double(
                            activeBandSpec.defaultOverlaps[j] * 1.0E-6));
                    _regionInfo[i].add(new Integer(channels));
                    _inst.setOverlap(overlap, i);
                    _inst.setChannels(channels, i);
                }
            }

            _regionInfo[i].add(new Integer(_inst.getChannels(i)));
        }
    }

    private void configureFrequencyEditor() {
        _frequencyEditor.updateDisplay(_inst, _receiver);

        _frequencyEditor.setCallback(new Runnable() {
            public void run() {
                hideFreqEditor();
            }
        });
    }

    private void getFrequencyEditorConfiguration() {
        Vector<Object>[] configs = _frequencyEditor.getCurrentConfiguration();

        for (int i = 0; i < configs.length; i++) {
            _inst.setMolecule(configs[i].get(0).toString(), i);
            _inst.setTransition(configs[i].get(1).toString(), i);
            _inst.setRestFrequency(configs[i].get(2).toString(), i);
            _inst.setCentreFrequency(configs[i].get(3).toString(), i);
            _inst.setBandWidth(configs[i].get(4).toString(), i);
            _inst.setChannels(Integer.parseInt(configs[i].get(5).toString()),
                    i);
        }
    }

    private void enableNamedWidgets(boolean enabled) {
        // Disable all named widgets except the frequency editor hide button.
        for (AbstractButton button: _w.feButtons.values()) {
            button.setEnabled(enabled);
        }

        for (AbstractButton button: _w.modeButtons.values()) {
            button.setEnabled(enabled);
        }

        for (AbstractButton button: _w.regionButtons.values()) {
            button.setEnabled(enabled);
        }

        for (AbstractButton button: _w.sbButtons.values()) {
            button.setEnabled(enabled);
        }

        // Keep the Accept button disabled...
        if (! enabled) {
            _w.acceptButton.setEnabled(enabled);
        }

        int active = new Integer(_inst.getBandMode());
        for (int i = 0; i < _w.bandwidths.size(); i ++) {
            _w.bandwidths.get(i).setEnabled(enabled && i < active);
        }

        _w.specialConfigs.setEnabled(enabled);

        // Finally deal with the show and hide buttons
        _w.showButton.setEnabled(enabled);
        _w.hideButton.setEnabled(! enabled);

        if (enabled) {
            // Disable anything that should not be available.
            setAvailableModes();
            setAvailableRegions();
        }

    }

    private void checkSideband() {
        String sideband = _inst.getBand();

        if (!validSideband(sideband)) {
            String other = LSB.equals(sideband) ? USB : LSB;

            if (validSideband(other)) {
                JOptionPane.showMessageDialog(null,
                        "Using " + other + " in order to reach line.",
                        "Changing Sideband!",
                        JOptionPane.WARNING_MESSAGE);

                clickButton(_w.sbButtons.get(other));
            }
            else {
                JOptionPane.showMessageDialog(null,
                        "Frequency cannot be reached from " + sideband + ".",
                        "Frequency not valid for Sideband!",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private boolean validSideband(String sideband) {
        boolean valid = true;

        double freq = _inst.calculateSkyFrequency();

        if (LSB.equals(sideband)) {
            valid = !((freq + _inst.getCentreFrequency(0)) > _receiver.loMax);
        } else {
            valid = !((freq - _inst.getCentreFrequency(0)) < _receiver.loMin);
        }

        return valid;
    }

    private void _doVelocityChecks() throws Exception {
        if (getSpecies().size() < 1) {
            String message = "Specified velocity results in frequency range"
                    + " that exceeds the line catalog, "
                    + "\nEither change the front end or change the velocity"
                    + " of the target";

            JOptionPane.showMessageDialog(null, message, "Invalid Velocity!",
                    JOptionPane.ERROR_MESSAGE);

            throw new Exception("Invalid velocity");
        }
    }

    class ConfigurationInformation {
        public String name;
        public String feName;
        public Vector<Double> freq = new Vector<Double>();
        public String sideBand;
        public String mode;
        public Integer mixers;
        public Integer subSystems;
        public Vector<String> species = new Vector<String>();
        public Vector<String> transition = new Vector<String>();
        public Vector<Double> bandWidths = new Vector<Double>();
        public Vector<Double> shifts = new Vector<Double>();

        public void print() {
            System.out.println("name       = " + name);
            System.out.println("feName     = " + feName);
            System.out.println("frequency  = " + freq);
            System.out.println("sideBand   = " + sideBand);
            System.out.println("mode       = " + mode);
            System.out.println("mixers     = " + mixers);
            System.out.println("subSystems = " + subSystems);
            System.out.println("species    = " + species);
            System.out.println("transition = " + transition);
            System.out.println("bandwidth  = " + subSystems);
        }
    }

    @SuppressWarnings("serial")
    class TableRowRenderer extends DefaultTableCellRenderer {
        public TableRowRenderer() {
            super();
        }

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            switch (row) {
                case 0:
                default:
                    setBackground(myRed);
                    break;

                case 1:
                    setBackground(myYellow);
                    break;

                case 2:
                    setBackground(myBlue);
                    break;

                case 3:
                    setBackground(myGreen);
                    break;
            }

            super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);

            return this;
        }
    }
}
