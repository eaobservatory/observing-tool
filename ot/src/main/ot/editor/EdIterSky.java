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

package ot.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerListModel;

import jsky.app.ot.editor.OtItemEditor;

import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import gemini.sp.SpTelescopePos;
import gemini.sp.obsComp.SpTelescopeObsComp;
import gemini.sp.SpSurveyContainer;
import gemini.sp.SpTelescopePosList;

import orac.ukirt.iter.SpIterSky;

/**
 * This is the editor for a Sky iterator component.
 *
 * This class is a modified version of jsky.app.ot.editor.EdIterObserve.
 * It replaces the original ot.editor.EdIterSky class of the freebongo version
 * of the OT.
 *
 * @author M.Folger@roe.ac.uk (modified copy of
 *         jsky.app.ot.editor.EdIterObserve by Allan Brighton)
 */
public final class EdIterSky extends OtItemEditor implements ActionListener,
        ChangeListener, DocumentListener {
    private IterSkyGUI _w; // the GUI layout panel
    private static String SKY = "SKY";

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdIterSky() {
        _title = "Sky Iterator";
        _presSource = _w = new IterSkyGUI();
        _description = "Observe the specified number of times.";

        // Note: The original bongo code used a SpinBoxWidget, but since Swing
        // doesn't have one, try using a JComboBox instead...
        Object[] ar = new Object[99];

        for (int i = 0; i < 99; i++) {
            ar[i] = new Integer(i + 1);
        }

        _w.repeatComboBox.setModel(new DefaultComboBoxModel(ar));
        _w.repeatComboBox.addActionListener(this);

        // Try to get all of the SKY's defined in the target component
        _w.skySpinner.setModel(new SpinnerListModel());
        getSkyPositions(_spItem);
        _w.skySpinner.addChangeListener(this);

        // Add other action listeners
        _w.scaleText.setText("1.0");
        _w.boxText.setText("5.0");
        _w.jrb1.addActionListener(this);
        _w.jrb2.addActionListener(this);
        _w.jrb3.addActionListener(this);
        _w.scaleText.getDocument().addDocumentListener(this);
        _w.boxText.getDocument().addDocumentListener(this);
    }

    /**
     * Override setup to set the title and description correctly.
     */
    public void setup(SpItem spItem) {
        super.setup(spItem);
        SpIterSky iterSky = (SpIterSky) spItem;
        setEditorWindowTitle("Sky Iterator");
        setEditorWindowDescription("Take the specified number of exposures.");
        _w.skySpinner.removeChangeListener(this);

        getSkyPositions(spItem);

        String currentSky = iterSky.getSky();

        if (currentSky == null || currentSky.trim().equals("")
                || currentSky.equals(SKY)) {
            iterSky.setSky((String) ((SpinnerListModel)
                    _w.skySpinner.getModel()).getList().get(0));
        }

        try {
            _w.skySpinner.setValue(iterSky.getSky());
        } catch (java.lang.IllegalArgumentException x) {
            // Means it has not been set up, we can just ignore it
        }

        _w.skySpinner.addChangeListener(this);

        _w.scaleText.getDocument().removeDocumentListener(this);
        _w.boxText.getDocument().removeDocumentListener(this);
        _w.scaleText.setText("" + iterSky.getScaleFactor());
        _w.boxText.setText("" + iterSky.getBoxSize());
        _w.scaleText.getDocument().addDocumentListener(this);
        _w.boxText.getDocument().addDocumentListener(this);

        if (iterSky.getFollowOffset()) {
            _w.jrb1.doClick();
        } else if (iterSky.getRandomPattern()) {
            _w.jrb2.doClick();
        } else {
            _w.jrb3.doClick();
        }
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order to
     * setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
        SpIterSky iterSky = (SpIterSky) _spItem;

        // Repetitions
        _w.repeatComboBox.removeActionListener(this);
        _w.repeatComboBox.setSelectedItem(new Integer(iterSky.getCount()));
        _w.repeatComboBox.addActionListener(this);

        // radio buttons
        if (iterSky.getFollowOffset()) {
            _w.scaleText.setEnabled(true);
            _w.boxText.setEnabled(false);
        } else if (iterSky.getRandomPattern()) {
            _w.scaleText.setEnabled(false);
            _w.boxText.setEnabled(true);
        } else {
            _w.scaleText.setEnabled(false);
            _w.boxText.setEnabled(false);
        }
    }

    /**
     * Called when the value in the combo box is changed.
     */
    public void actionPerformed(ActionEvent e) {
        SpIterSky iterSky = (SpIterSky) _spItem;
        SpTelescopeObsComp obsComp = SpTreeMan.findTargetList(_spItem);

        if (e.getSource() == _w.repeatComboBox) {
            iterSky.setCount((Integer) _w.repeatComboBox.getSelectedItem());
        }

        String skyPattern = SKY + "[0-9]+";
        double boxSize = 0;
        boolean setBox = false;

        if (e.getSource() == _w.jrb1) {
            iterSky.setFollowOffset(true);
            iterSky.setRandomPattern(false);
            iterSky.setScaleFactor(_w.scaleText.getText());
            setBox = true;
        } else if (e.getSource() == _w.jrb2) {
            iterSky.setFollowOffset(false);
            iterSky.setRandomPattern(true);
            iterSky.setBoxSize(_w.boxText.getText());
            boxSize = iterSky.getBoxSize();
            setBox = true;
        } else if (e.getSource() == _w.jrb3) {
            iterSky.setFollowOffset(false);
            iterSky.setRandomPattern(false);
            setBox = true;
        }

        if (setBox && obsComp != null && iterSky.getSky().matches(skyPattern)) {
            String sky = iterSky.getSky();
            SpTelescopePosList telescopePosList = obsComp.getPosList();
            SpTelescopePos telescopePos =
                    (SpTelescopePos) telescopePosList.getPosition(sky);

            if (telescopePos != null) {
                telescopePos.setBoxSize(boxSize);
            } else {
                iterSky.rmSky();
            }
        }

        _updateWidgets();
    }

    public void stateChanged(ChangeEvent e) {
        SpIterSky iterSky = (SpIterSky) _spItem;
        iterSky.setSky((String) _w.skySpinner.getValue());
    }

    public void insertUpdate(DocumentEvent e) {
        SpIterSky iterSky = (SpIterSky) _spItem;
        SpTelescopeObsComp obsComp = SpTreeMan.findTargetList(_spItem);

        if (e.getDocument() == _w.scaleText.getDocument()) {
            iterSky.setScaleFactor(_w.scaleText.getText());
        } else if (e.getDocument() == _w.boxText.getDocument()) {
            iterSky.setBoxSize(_w.boxText.getText());

            if (obsComp != null && iterSky.getSky().startsWith(SKY)) {
                ((SpTelescopePos) obsComp.getPosList().getPosition(
                        iterSky.getSky())).setBoxSize(iterSky.getBoxSize());
            }
        }
    }

    public void removeUpdate(DocumentEvent e) {
        SpIterSky iterSky = (SpIterSky) _spItem;

        if (e.getDocument() == _w.scaleText.getDocument()) {
            iterSky.setScaleFactor(_w.scaleText.getText());
        } else if (e.getDocument() == _w.boxText.getDocument()) {
            iterSky.setBoxSize(_w.boxText.getText());
        }
    }

    public void changedUpdate(DocumentEvent e) {
    }

    @SuppressWarnings("unchecked")
    public void getSkyPositions(SpItem spItem) {
        if (spItem != null) {
            Vector<String> tags = null;
            SpTelescopeObsComp obsComp = SpTreeMan.findTargetList(spItem);

            if (obsComp != null) {
                tags = obsComp.getPosList().getAllTags(SKY);

                if (tags == null) {
                    tags = new Vector<String>();
                }

            } else {
                tags = new Vector<String>();
                SpSurveyContainer surveyContainer =
                        SpTreeMan.findSurveyContainerInContext(spItem);

                if (surveyContainer != null) {
                    Vector<String> tmp = null;
                    int size = surveyContainer.size();
                    Vector<String>[] allTags = new Vector[size];

                    for (int i = 0; i < size; i++) {
                        obsComp = surveyContainer.getSpTelescopeObsComp(i);
                        tmp = obsComp.getPosList().getAllTags(SKY);
                        allTags[i] = tmp;
                    }

                    tmp = allTags[0];

                    while (tmp.size() > 0) {
                        String tag = tmp.remove(0);
                        boolean add = true;

                        for (int k = 1; k < size; k++) {
                            if (!allTags[k].contains(tag)) {
                                add = false;

                                break;
                            }
                        }

                        if (add && !tags.contains(tag)) {
                            tags.add(tag);
                        }
                    }
                }
            }

            if (tags.size() == 0) {
                tags.add(SKY);
            }

            SpinnerListModel model =
                    (SpinnerListModel) _w.skySpinner.getModel();

            model.setList(tags);
        }
    }
}
