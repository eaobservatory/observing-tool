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
 *
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * author: Alan Pickup = dap@roe.ac.uk         2001 Nov 6
 */

package ot.ukirt.inst.editor;

import orac.ukirt.inst.SpInstUIST;

import gemini.sp.SpItem;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jsky.app.ot.tpe.TelescopePosEditor;
import jsky.app.ot.tpe.TpeManager;

import java.awt.CardLayout;
import javax.swing.ButtonGroup;

import java.util.TreeSet;

/**
 * This is the editor for the UIST instrument
 */
public final class EdCompInstUIST extends EdCompInstBase implements
        ActionListener {
    private SpInstUIST _instUIST;
    private boolean haveInitialised = false;
    private UistGUI _w;

    /**
     * This flag is set true while _init is executed to prevent
     * actionPerformed() to do react to action events caused by
     * initializing widgets.
     */

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdCompInstUIST() {
        _title = "UIST";
        _presSource = _w = new UistGUI();
        _description = "The UIST instrument is configured with this component.";

        ButtonGroup grp = new ButtonGroup();
        grp.add(_w.filterBroadBand);
        grp.add(_w.filterNarrowBand);

        _w.filterBroadBand.addActionListener(this);
        _w.filterNarrowBand.addActionListener(this);

        // Camera

        _w.camera.addWatcher(new DropDownListBoxWidgetWatcher() {
            public void dropDownListBoxAction(DropDownListBoxWidgetExt dd,
                    int i, String val) {
                _updateCamera(val);
                _updateSourceMag();
                _updateWidgets();

                TelescopePosEditor tpe = TpeManager.get(_instUIST);

                if (tpe != null) {
                    tpe.repaint();
                }
            }
        });

        // Polarimetry

        _w.polarimetry.addWatcher(new CheckBoxWidgetWatcher() {
            public void checkBoxAction(CheckBoxWidgetExt cb) {
                boolean polarimetryBoolean = cb.getBooleanValue();

                if (polarimetryBoolean) {
                    _instUIST.setPolarimetry("yes");
                } else {
                    _instUIST.setPolarimetry("no");
                }

                _w.imaging_and_polarimetry_posAngleLabel
                        .setEnabled(polarimetryBoolean);
                _w.imaging_and_polarimetry_posAngle
                        .setEnabled(polarimetryBoolean);

                _instUIST.useDefaultDisperser();
                _instUIST.useDefaultMask();
                _instUIST.useDefaultFilter();
                _instUIST.useDefaultFilterCategory();
                _instUIST.useDefaultOrder();
                _instUIST.useDefaultAcquisition();
                _updatePolarimetry();
                _updateCameraChoices();
                _showCamera();
                _updateDisperserChoices();
                _updateFilterChoices();
                _updateMaskChoices();
                _updateWidgets();

                TelescopePosEditor tpe = TpeManager.get(_instUIST);

                if (tpe != null) {
                    tpe.repaint();
                }
            }
        });

        _w.imaging_and_polarimetry_posAngle.addWatcher(
                new TextBoxWidgetWatcher() {
                    public void textBoxKeyPress(TextBoxWidgetExt tbw) {
                        _instUIST.getAvEditFSM().setEachEditNotifies(false);
                        String pas = tbw.getText();
                        double pa = Double.parseDouble(pas);

                        _instUIST.getAvEditFSM().deleteObserver(
                                EdCompInstUIST.this);

                        if ((pa > 0.00001) || (pa < -0.00001)) {
                            _instUIST.setPosAngleDegrees(pa);
                        } else {
                            _instUIST.setPosAngleDegrees(0.0);
                        }

                        _updateWidgets();
                        _instUIST.getAvEditFSM().addObserver(
                                EdCompInstUIST.this);
                    }

                    public void textBoxAction(TextBoxWidgetExt tbw) {
                        // ignore
                    }
                });

        _w.imaging_and_polarimetry_mask.addWatcher(
                new DropDownListBoxWidgetWatcher() {
                    public void dropDownListBoxAction(
                            DropDownListBoxWidgetExt dd, int i, String val) {
                        _instUIST.setMask(val);

                        boolean enable = _instUIST.canUpdatePosAngle();
                        _w.imaging_and_polarimetry_posAngleLabel.setEnabled(
                                enable);
                        _w.imaging_and_polarimetry_posAngle.setEnabled(enable);

                        if (!enable) {
                            _instUIST.setPosAngleDegrees(-90.0);
                        }

                        _instUIST.useDefaultResolution();
                        _instUIST.useDefaultAcquisition();
                        _updateWidgets();

                        TelescopePosEditor tpe = TpeManager.get(_instUIST);

                        if (tpe != null) {
                            tpe.repaint();
                        }
                    }
                });

        // Source magnitude

        _w.imaging_sourceMag.addWatcher(new DropDownListBoxWidgetWatcher() {
            public void dropDownListBoxAction(DropDownListBoxWidgetExt dd,
                    int i, String val) {
                _instUIST.setSourceMag(val);
                _instUIST.useDefaultAcquisition();
                _updateWidgets();
            }
        });

        _w.spectroscopy_sourceMag
                .addWatcher(new DropDownListBoxWidgetWatcher() {
                    public void dropDownListBoxAction(
                            DropDownListBoxWidgetExt dd, int i, String val) {
                        _instUIST.setSourceMag(val);
                        _instUIST.useDefaultAcquisition();
                        _updateWidgets();
                    }
                });

        // GUIs in imaging group

        // Added by RDK

        // Pupil imaging mode

        _w.imaging_pupilCamera.addWatcher(new CheckBoxWidgetWatcher() {
            public void checkBoxAction(CheckBoxWidgetExt cb) {
                if (cb.getBooleanValue()) {
                    _instUIST.setPupilImaging("yes");
                } else {
                    _instUIST.setPupilImaging("no");
                }

                _instUIST.useDefaultMask();
                _updateImagerChoices();
                _updatePupilCamera();
                _updateFilterChoices();
                _updateSourceMag();
                _updateWidgets();
            }
        });

        // End of added by RDK

        // Imager list

        _w.imaging_imagerList.addWatcher(new DropDownListBoxWidgetWatcher() {
            public void dropDownListBoxAction(DropDownListBoxWidgetExt dd,
                    int i, String val) {
                _instUIST.setImager(val);
                _updateFilterChoices();
                _instUIST.useDefaultAcquisition();
                _updateWidgets();

                TelescopePosEditor tpe = TpeManager.get(_instUIST);

                if (tpe != null) {
                    tpe.repaint();
                }
            }
        });

        // Filter

        _w.imaging_filter.addWatcher(new DropDownListBoxWidgetWatcher() {
            public void dropDownListBoxAction(DropDownListBoxWidgetExt dd,
                    int i, String val) {
                _instUIST.setFilter(val);
                _instUIST.useDefaultAcquisition();
                _updateWidgets(_w.imaging_filter);
            }
        });

        // GUIs in spectroscopy group

        // Dispersers = Grisms

        _w.spectroscopy_grism.addWatcher(new DropDownListBoxWidgetWatcher() {
            public void dropDownListBoxAction(DropDownListBoxWidgetExt dd,
                    int i, String val) {
                _instUIST.setDisperser(val);

                String[] maskList = _instUIST.getMaskList();
                String currentMask = _instUIST.getMask();
                TreeSet<String> comparator;

                if (val.equals("IJ") || val.equals("JH")) {
                    comparator = _instUIST.NON_IJJH_MASKS;
                } else {
                    comparator = _instUIST.VALID_IJJH_MASKS;
                }

                if (comparator.contains(currentMask)) {
                    for (int j = 0; j < maskList.length; j++) {
                        currentMask = maskList[j];

                        if (comparator.contains(currentMask)) {
                            continue;
                        }

                        _instUIST.setMask(currentMask);

                        break;
                    }
                }

                _instUIST.useDefaultOrder();
                _instUIST.useDefaultResolution();
                _instUIST.useDefaultAcquisition();
                _updateMaskChoices();
                _updateWidgets();

                TelescopePosEditor tpe = TpeManager.get(_instUIST);

                if (tpe != null) {
                    tpe.repaint();
                }
            }
        });

        // Mask

        _w.spectroscopy_mask.addWatcher(new DropDownListBoxWidgetWatcher() {
            public void dropDownListBoxAction(DropDownListBoxWidgetExt dd,
                    int i, String val) {
                _instUIST.setMask(val);

                String currentGrism = _instUIST.getDisperser();

                if (_instUIST.VALID_IJJH_MASKS.contains(val)) {
                    if (!(currentGrism.equals("IJ")
                            || currentGrism .equals("JH"))) {
                        String[] grismList = _instUIST.getDisperserList();

                        for (int j = 0; j < grismList.length; j++) {
                            currentGrism = grismList[j];

                            if (currentGrism.equals("IJ")
                                    || currentGrism.equals("JH")) {
                                _instUIST.setDisperser(currentGrism);

                                break;
                            }
                        }
                    }

                } else if (_instUIST.NON_IJJH_MASKS.contains(val)) {
                    if (currentGrism.equals("IJ") || currentGrism.equals("JH")) {
                        String[] grismList = _instUIST.getDisperserList();

                        for (int j = 0; j < grismList.length; j++) {
                            currentGrism = grismList[j];

                            if (!(currentGrism.equals("IJ")
                                    || currentGrism.equals("JH"))) {
                                _instUIST.setDisperser(currentGrism);

                                break;
                            }
                        }
                    }
                }

                _instUIST.useDefaultResolution();
                _instUIST.useDefaultAcquisition();
                _updateWidgets();

                TelescopePosEditor tpe = TpeManager.get(_instUIST);

                if (tpe != null) {
                    tpe.repaint();
                }
            }
        });

        // Position angle

        _w.spectroscopy_posAngle.addWatcher(new TextBoxWidgetWatcher() {
            public void textBoxKeyPress(TextBoxWidgetExt tbw) {
                _instUIST.getAvEditFSM().setEachEditNotifies(false);
                String pas = tbw.getText();
                double pa = Double.parseDouble(pas);
                _instUIST.getAvEditFSM().deleteObserver(EdCompInstUIST.this);

                if ((pa > 0.00001) || pa < -0.00001) {
                    _instUIST.setPosAngleDegrees(pa);
                } else {
                    _instUIST.setPosAngleDegrees(0.0);
                }

                _instUIST.getAvEditFSM().addObserver(EdCompInstUIST.this);
            }

            public void textBoxAction(TextBoxWidgetExt tbw) {
                // ignore
            }
        });

        // GUIs in data acquisition group

        // Readout

        // Added by RDK

        // Readout mode

        _w.dataAcq_readMode.addWatcher(new DropDownListBoxWidgetWatcher() {
            public void dropDownListBoxAction(DropDownListBoxWidgetExt dd,
                    int i, String val) {
                _instUIST.setReadMode(val);
                _updateWidgets(_w.dataAcq_readMode);
            }
        });

        // Readout area
        // getMicroStepPatterns

        _w.dataAcq_readArea.addWatcher(new DropDownListBoxWidgetWatcher() {
            public void dropDownListBoxAction(DropDownListBoxWidgetExt dd,
                    int i, String val) {
                _instUIST.setReadAreaString(val);
                _updateWidgets(_w.dataAcq_readArea);
            }
        });

        // Coadds

        _w.dataAcq_coadds.addWatcher(new TextBoxWidgetWatcher() {
            public void textBoxKeyPress(TextBoxWidgetExt tbw) {
                try {
                    // Added by RDK
                    String coaddsString = tbw.getText();
                    int coadds = Integer.parseInt(coaddsString);

                    if (coadds > 0) {
                        _instUIST.setCoadds(coadds);
                        _updateWidgets(_w.dataAcq_coadds);
                    }
                    // End of added by RDK

                } catch (Exception ex) {
                    // ignore
                }
            }

            public void textBoxAction(TextBoxWidgetExt tbw) {
            } // ignore
        });

        // Exposure time

        _w.dataAcq_exposureTime.addWatcher(new TextBoxWidgetWatcher() {
            public void textBoxKeyPress(TextBoxWidgetExt tbw) {
                try {
                    String ets = tbw.getText();

                    double et = Double.parseDouble(ets);

                    if (et > 0.00001) {
                        // Added by RDK
                        _instUIST.changeExpTimeOT(et);
                        // End of added by RDK
                        _updateWidgets(_w.dataAcq_exposureTime);
                    }
                } catch (Exception ex) {
                    // ignore
                }
            }

            public void textBoxAction(TextBoxWidgetExt tbw) {
                // ignore
            }
        });

        // Default exposure and observation times

        _w.dataAcq_defaultExpTime.addWatcher(new CommandButtonWidgetWatcher() {
            public void commandButtonAction(CommandButtonWidgetExt cbw) {
                // Added by RDK
                _instUIST.useDefaultExpTimeOT();
                _instUIST.useDefaultReadMode();
                _instUIST.useDefaultReadArea();
                _instUIST.useDefaultCoadds();
                // End of added by RDK

                _updateWidgets();
            }
        });
    }

    /**
     * Override method in super class to avoid exposure time and
     * position angle text box watchers being added twice.
     */
    protected void _init() {
    }

    /**
     * Override setup to store away a reference to the SpInstUIST item.
     */
    public void setup(SpItem spItem) {
        _instUIST = (SpInstUIST) spItem;

        // Added by RDK
        _instUIST.avTableUpdate();
        // End of added by RDK

        haveInitialised = false;
        super.setup(spItem);
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order
     * to setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
        _updateWidgets(null);
    }

    protected void _updateWidgets(Object source) {
        if (!haveInitialised) {
            // Load drop down lists only first time in
            _updateCameraChoices();
            _showCamera();
            _updateImagerChoices();
            _updateImager();
            _updateFilterCategory();
            _updateFilterChoices();
            _updateDisperserChoices();
            _updateDisperser();
            _updateMaskChoices();
            _updatePolarimetry();
            _updatePupilCamera();
            _updateFilter();
            // Added by RDK
            _updateReadModeChoices();
            _updateReadAreaChoices();
            // End of added by RDK
            _updateObsTime();
            haveInitialised = true;
        }

        _updateDisperser();
        _updateImager();
        _updatePosAngle();

        if (!_instUIST.isImaging()) {
            _updateSpecFilter();
            _updateResolution();
            _updateOrder();
        } else {
            if ((_w.filterBroadBand != source)
                    && (_w.filterNarrowBand != source)) {
                _updateFilterCategory();
            }

            if (_w.imaging_filter != source) {
                _updateFilter();
            }
        }

        _updateSourceMagChoices();
        _updateSourceMag();
        _updateWavelengthCoverage();
        _updateMask();

        // Added by RDK

        if (_w.dataAcq_readMode != source) {
            _updateReadModeChoices();
            _updateReadMode();
        }

        if (_w.dataAcq_readArea != source) {
            _updateReadAreaChoices();
            _updateReadArea();
        }

        // End of added by RDK

        _updateAcquisition(source);
        _updateScienceFOV();
        _updateChopFreq();
        _updateDutyCycle();
        // Commented for testing by RDK
        _updateExpTime();
        _updateObsTime();

        if (_w.dataAcq_exposureTime != source) {
            String ets = _instUIST.getExpTimeOTString();
            _w.dataAcq_exposureTime.setText(ets);
        }

        // Added by RDK

        if (_w.dataAcq_coadds != source) {
            String coadds = _instUIST.getCoaddsString();
            _w.dataAcq_coadds.setText(coadds);
        }

        // End of added by RDK
    }

    // Update the actual observation time

    private void _updateObsTime() {
        String aobts = _instUIST.getObservationTimeString();
        _w.dataAcq_actualObservationTime.setText(aobts);
    }

    // Update the actual exposure time

    private void _updateExpTime() {
        String aexpts = _instUIST.getExposureTimeString();
        _w.dataAcq_actualExposureTime.setText(aexpts);
    }

    // Update the polarimetry check box

    private void _updatePolarimetry() {
        String p = _instUIST.getPolarimetry();
        _w.polarimetry.setValue(p.equalsIgnoreCase("yes"));
    }

    // Added by RDK

    // Update the pupil camera check box

    private void _updatePupilCamera() {
        String p = _instUIST.getPupilImaging();
        boolean pEqualsYes = p.equalsIgnoreCase("yes");
        _w.imaging_pupilCamera.setValue(pEqualsYes);
        _w.polarimetry.setEnabled(!pEqualsYes);
    }

    // End of added by RDK

    // Update the resolution

    private void _updateResolution() {
        _w.spectroscopy_resolution.setText(_instUIST.getResolutionString());
    }

    // Update the order

    private void _updateOrder() {
        _w.spectroscopy_order.setText(_instUIST.getOrderString());
    }

    // Update the position angle

    private void _updatePosAngle() {
        double pa = _instUIST.getPosAngleDegrees();
        if (_instUIST.isImaging()) {
            _w.imaging_and_polarimetry_posAngle.setText(Double.toString(pa));
        } else {
            _w.spectroscopy_posAngle.setText(Double.toString(pa));
        }
    }

    // Update the science field of view displays on both the
    // imaging and spectroscopy panes

    private void _updateScienceFOV() {
        String scienceArea = _instUIST.getScienceAreaString();
        _w.imaging_fieldOfView.setText(scienceArea);
        _w.spectroscopy_fieldOfView.setText(scienceArea);
    }

    // Update the wavelength coverage on both the
    // imaging and spectroscopy panes

    private void _updateWavelengthCoverage() {
        String spectralCoverage = _instUIST.getSpectralCoverage();
        _w.spectroscopy_coverage.setText(spectralCoverage);
        _w.imaging_bandpass.setText(spectralCoverage);
    }

    // Update the imaging filter

    private void _updateFilter() {
        _w.imaging_filter.setValue(_instUIST.getFilter());
    }

    // Update the spectroscopy filter

    private void _updateSpecFilter() {
        _w.spectroscopy_filter.setValue(_instUIST.getFilter());
    }

    // Update the list of available cameras

    private void _updateCameraChoices() {
        String choices[] = new String[_instUIST.getCameraList().length];
        choices = _instUIST.getCameraList();
        _w.camera.setChoices(choices);
    }

    // Update the list of available imagers

    private void _updateImagerChoices() {
        String choices[] = new String[_instUIST.getImagerList().length];
        choices = _instUIST.getImagerList();
        _w.imaging_imagerList.setChoices(choices);
    }

    // Update the list of source magnitude choices

    private void _updateSourceMagChoices() {
        int numChoices = _instUIST.getSourceMagList().length;
        String choices[] = new String[numChoices];
        choices = _instUIST.getSourceMagList();
        _w.imaging_sourceMag.setMaximumRowCount(numChoices);
        _w.imaging_sourceMag.setChoices(choices);
        _w.spectroscopy_sourceMag.setMaximumRowCount(numChoices);
        _w.spectroscopy_sourceMag.setChoices(choices);
    }

    // Update the source magnitude

    private void _updateSourceMag() {
        String sourceMag = _instUIST.getSourceMag();
        _w.imaging_sourceMag.setValue(sourceMag);
        _w.spectroscopy_sourceMag.setValue(sourceMag);
    }

    // Added by RDK

    // Update the list of read mode choices

    private void _updateReadModeChoices() {
        String choices[] = new String[_instUIST.getReadModeChoices().length];
        choices = _instUIST.getReadModeChoices();
        _w.dataAcq_readMode.setChoices(choices);
    }

    // Update the read mode

    private void _updateReadMode() {
        String readMode = _instUIST.getReadMode();
        _w.dataAcq_readMode.setValue(readMode);
    }

    // Update the list of read area choices

    private void _updateReadAreaChoices() {
        String choices[] = new String[_instUIST.getReadAreaChoices().length];
        choices = _instUIST.getReadAreaChoices();
        _w.dataAcq_readArea.setChoices(choices);
    }

    // Update the read area

    private void _updateReadArea() {
        String readArea = _instUIST.getReadAreaString();
        _w.dataAcq_readArea.setValue(readArea);
    }

    // End of added by RDK

    // Update the mask

    private void _updateMask() {
        if (_instUIST.isImaging()) {
            _w.imaging_and_polarimetry_mask.setValue(_instUIST.getMask());
        } else {
            _w.spectroscopy_mask.setValue(_instUIST.getMask());
        }
    }

    // Update the imager

    private void _updateImager() {
        _w.imaging_imagerList.setValue(_instUIST.getImager());
    }

    // Update the list of available filters

    private void _updateFilterChoices() {
        boolean useCurrentFilter;

        if (_instUIST != null && _instUIST.isImaging()) {
            useCurrentFilter = false;
        } else {
            useCurrentFilter = true;
        }

        String[] filterChoices = _instUIST.getFilterList();
        _w.imaging_filter.setChoices(_instUIST.getFilterList());

        for (int i = 0; i < filterChoices.length && useCurrentFilter == false;
                i++) {
            if (_instUIST != null
                    && _instUIST.getFilter().equals(filterChoices[i])) {
                useCurrentFilter = true;
            }
        }

        if (useCurrentFilter) {
            _updateFilter();
        } else {
            _w.imaging_filter.setSelectedIndex(0);
        }
    }

    // Update the list of available dispersers

    private void _updateDisperserChoices() {
        int numChoices = _instUIST.getDisperserList().length;
        _w.spectroscopy_grism.setMaximumRowCount(numChoices);
        _w.spectroscopy_grism.setChoices(_instUIST.getDisperserList());
    }

    // Update the disperser

    private void _updateDisperser() {
        _w.spectroscopy_grism.setValue(_instUIST.getDisperser());
    }

    // Update the list of available masks

    private void _updateMaskChoices() {
        if (_instUIST.isImaging()) {
            _w.imaging_and_polarimetry_mask.setChoices(_instUIST.getMaskList());
            _w.imaging_and_polarimetry_mask.setValue(_instUIST.getMask());
        } else {
            _w.spectroscopy_mask.setChoices(_instUIST.getMaskList());
            _w.spectroscopy_mask.setValue(_instUIST.getMask());
        }
    }

    // Update the chop frequency

    private void _updateChopFreq() {
        _w.dataAcq_chopFrequency.setText(_instUIST.getChopFreqRound());
    }

    // Update the duty cycle

    private void _updateDutyCycle() {
        _w.dataAcq_dutyCycle.setText(_instUIST.getDutyCycleRound());
    }

    // Update the acquisition

    private void _updateAcquisition(Object source) {
        _instUIST.setAcquisition();

        if (_w.dataAcq_exposureTime != source) {
            String ets = _instUIST.getExpTimeOTString();
            _w.dataAcq_exposureTime.setText(ets);
        }

        // Added by RDK

        if (_w.dataAcq_coadds != source) {
            String coadds = _instUIST.getCoaddsString();
            _w.dataAcq_coadds.setText(coadds);
        }

        // End of added by RDK
    }

    // Update camera

    private void _updateCamera(String camera) {
        _instUIST.setCamera(camera);

        if (camera.equals("imaging")) {
            _updateFilterChoices();
        } else {
            _updateDisperserChoices();
        }

        _updateMaskChoices();
        _instUIST.useDefaultAcquisition();
        _showCamera();
    }

    // Show camera

    private void _showCamera() {
        String camera = _instUIST.getCamera();
        _w.camera.setValue(_instUIST.getCamera());

        // Make the appropriate imaging or spectroscopy config area visible
        if (camera.equals("imaging")) {
            ((CardLayout) (_w.modePanel.getLayout())).show(_w.modePanel,
                    "imagingPanel");

            boolean enable = _instUIST.canUpdatePosAngle();

            _w.imaging_and_polarimetry_posAngleLabel.setEnabled(enable);
            _w.imaging_and_polarimetry_posAngle.setEnabled(enable);

            if (!enable) {
                _instUIST.setPosAngleDegrees(-90.0);
            }

        } else {
            ((CardLayout) (_w.modePanel.getLayout())).show(_w.modePanel,
                    "spectroscopyPanel");
        }

        _w.polarimetry.setEnabled(!camera.equals("ifu"));
    }

    /**
     * Return the position angle text box.
     */
    public TextBoxWidgetExt getPosAngleTextBox() {
        if (_instUIST.isImaging()) {
            return _w.imaging_and_polarimetry_posAngle;
        }

        return _w.spectroscopy_posAngle;
    }

    /**
     * Return the exposure time text box.
     */
    public TextBoxWidgetExt getExposureTimeTextBox() {
        return _w.dataAcq_exposureTime;
    }

    /**
     * Return the coadds text box, or null if not available.
     */
    public TextBoxWidgetExt getCoaddsTextBox() {
        // UIST does not have a coadds text box.
        // Added by RDK
        return _w.dataAcq_coadds;
        // End of added by RDK
    }

    /**
     * Update filterCategoary.
     */
    private void _updateFilterCategory() {
        String filterCategory = _instUIST.getFilterCategory();

        if (filterCategory.equalsIgnoreCase("broad")) {
            _w.filterBroadBand.setValue(true);
            _w.filterNarrowBand.setEnabled(_instUIST.getNarrowFilterSet() != 0);
        } else {
            _w.filterNarrowBand.setValue(true);
        }
    }

    /**
     *
     */
    public void actionPerformed(ActionEvent evt) {
        Object w = evt.getSource();

        if (_w.filterBroadBand == w) {
            _instUIST.setFilterCategory("broad");
        } else if (_w.filterNarrowBand == w) {
            _instUIST.setFilterCategory("narrow");
        }

        _updateFilterChoices();
        _instUIST.useDefaultAcquisition();
        _updateWidgets(w);

        return;
    }
}
