/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot.tpe;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

import gemini.sp.SpTelescopePos;
import jsky.util.gui.LabelJPanel;
import jsky.app.ot.gui.ToggleButtonWidgetPanel;
import jsky.app.ot.gui.ToggleButtonWidget;
import jsky.app.ot.tpe.feat.TpeGuidePosFeature;


/**
 * A tool bar with Gemini specific features.
 *
 * @version $Revision$
 * @author Allan Brighton
 */
public class TelescopePosEditorToolBar extends JToolBar {

    /** The main image display widget */
    TpeImageWidget imageDisplay;

    /** The View panel */
    protected JPanel viewPanel;

    /** The Tools panel */
    protected JPanel modePanel;

    /** The View toggle button panel */
    protected ToggleButtonWidgetPanel viewToggleButtonPanel;

    /** The Tools toggle button panel */
    protected ToggleButtonWidgetPanel modeToggleButtonPanel;

    /** Previous toolbar orientation */
    protected int previousOrientation = VERTICAL;


    /**
     * Create a top level window containing an ImageDisplayControl panel.
     *
     * @param size   the size (width, height) to use for the pan and zoom windows.
     */
    public TelescopePosEditorToolBar(TpeImageWidget imageDisplay) {
        super(VERTICAL);
        setFloatable(false);
        this.imageDisplay = imageDisplay;
        add(makeModePanel(VERTICAL));
        add(makeViewPanel(VERTICAL));

        // make sure that the View button corresponding to the given mode is selected
        int n = modeToggleButtonPanel.getNumberOfButtons();
        for(int i = 0; i < n; i++) {
            if (modeToggleButtonPanel.getButton(i) != null) {
                modeToggleButtonPanel.getButton(i).addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            ToggleButtonWidget w = (ToggleButtonWidget)e.getSource();
                            if (w.getBooleanValue()) {
                                String s = w.getText();
                                if (s.equals("Target")) {
                                    ToggleButtonWidget b = viewToggleButtonPanel.getButton("Target");
                                    if (b != null)
                                        b.setSelected(true);
                                }
                                // "GUIDE" added by MFO (20 August 2001).
                                // "GUIDE" referres to the UKIRT tag for guide stars as specified in the configuration
                                // file ot.cfg. If the tag in ot.cfg is changed "GUIDE" has to be changed here too.
                                // (case sensitive)
                                else {
                                    // Check whether it is a additional target button (e.g. GUIDE, Reference etc).
                                    boolean isGuide = false;
                                    String [] guideTags = SpTelescopePos.getGuideStarTags();

                                    for(int i = 0; i < guideTags.length; i++) {
                                        if(s.equals(guideTags[i])) {
                                            isGuide = true;
                                            break;
                                        }
                                    }

                                    if (isGuide) {
                                        ToggleButtonWidget b = viewToggleButtonPanel.getButton(TpeGuidePosFeature.getTpeViewGuideLabel());
                                        if (b != null)
                                            b.setSelected(true);
                                    }
                                }
                            }
                        }
                });
            }
        }
    }

    /** Make and return the View panel in the given orientation */
    protected JPanel makeViewPanel(int orient) {
        String[] names = {"Base",  "Target",   "Catalog", "PWFS",  "",
            "Guide", "Sci Area", "Acq Cam", "OIWFS", ""};

            if (orient == VERTICAL) {
                viewToggleButtonPanel = new ToggleButtonWidgetPanel(names, 0, 1, true, 0, 0);
                viewPanel = new LabelJPanel("View", viewToggleButtonPanel, false);
            }
            else {
                viewToggleButtonPanel = new ToggleButtonWidgetPanel(names, 2, 0, true, 0, 0);
                viewPanel = new LabelJPanel("View", viewToggleButtonPanel, true);
            }

            return viewPanel;
    }

    /** Return the ith View toggle button */
    public ToggleButtonWidget getViewToggleButton(int i) {
        return viewToggleButtonPanel.getButton(i);
    }

    /** Return the View toggle button with the given label */
    public ToggleButtonWidget getViewToggleButton(String label) {
        return viewToggleButtonPanel.getButton(label);
    }


    /** Make and return the Tools panel in the given orientation */
    protected JPanel makeModePanel(int orient) {
        String[] names = {"Browse", "Drag", "Erase", null, 
            "PWFS1", "PWFS2", "OIWFS", "Target",
            "", "", "", ""};

            if (orient == VERTICAL) {
                modeToggleButtonPanel = new ToggleButtonWidgetPanel(names, 0, 1, false, 0, 0);
                modePanel = new LabelJPanel("Mode", modeToggleButtonPanel, false);
            }
            else {
                modeToggleButtonPanel = new ToggleButtonWidgetPanel(names, 2, 0, false, 0, 0);
                modePanel = new LabelJPanel("Mode", modeToggleButtonPanel, true);
            }

            return modePanel;
    }

    /** Return the ith Mode toggle button */
    public ToggleButtonWidget getModeToggleButton(int i) {
        return modeToggleButtonPanel.getButton(i);
    }

    /** Return the Mode toggle button with the given label */
    public ToggleButtonWidget getModeToggleButton(String label) {
        return modeToggleButtonPanel.getButton(label);
    }


    /** Redefined from the base class to fix the layout when the orientation changes. */
    public void setOrientation(int orient) {
        super.setOrientation(orient);
        if (orient != previousOrientation) {
            previousOrientation = orient;
            if (modePanel == null)
                return;
            remove(modePanel);
            remove(viewPanel);
            add(makeModePanel(orient));
            add(makeViewPanel(orient));
            revalidate();
        }
    }
}

