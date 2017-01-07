/*
 * Copyright (c) 2000 Association of Universities for Research in Astronomy, Inc. (AURA)
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

package jsky.app.ot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.border.BevelBorder;
import jsky.util.gui.BasicWindowMonitor;
import ot.util.DialogUtil;
import jsky.util.gui.GenericToolBarTarget;

/**
 * Implements the GUI layout of the user interface for the main OT window
 * (was spTree.gui with Bongo).
 *
 * @author Allan Brighton (ported to Swing/JSky)
 */
@SuppressWarnings({"serial"})
public abstract class SpTreeGUI extends JPanel implements GenericToolBarTarget {
    /** The top level parent frame. */
    protected JFrame parent;

    /** The tree widget used to display the science program. */
    protected OtTreeWidget tree;

    /** Contains the editor panels. */
    protected JPanel editorPane;

    /**
     * Action to use for the "Open" menu and toolbar items.
     */
    protected AbstractAction openAction = new AbstractAction("Open") {
        public void actionPerformed(ActionEvent evt) {
            try {
                open(false);
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "Back" menu and toolbar items.
     *
     * Does nothing, but required for the GenericToolBarTarget interface.
     */
    protected AbstractAction backAction = new AbstractAction("Back") {
        public void actionPerformed(ActionEvent evt) {}
    };

    /**
     * Action to use for the "Forward" menu and toolbar items.
     *
     * Does nothing, but required for the GenericToolBarTarget interface.
     */
    protected AbstractAction forwAction = new AbstractAction("Forward") {
        public void actionPerformed(ActionEvent evt) {}
    };

    /**
     * Action to use for the "Save" menu and toolbar items.
     */
    protected AbstractAction saveAction = new AbstractAction("Save changes") {
        public void actionPerformed(ActionEvent evt) {
            try {
                save();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "Cut" menu and toolbar items.
     */
    protected AbstractAction cutAction = new AbstractAction("Cut") {
        public void actionPerformed(ActionEvent evt) {
            try {
                cut();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "Copy" menu and toolbar items.
     */
    protected AbstractAction copyAction = new AbstractAction("Copy") {
        public void actionPerformed(ActionEvent evt) {
            try {
                copy();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "Paste" menu and toolbar items.
     */
    protected AbstractAction pasteAction = new AbstractAction("Paste") {
        public void actionPerformed(ActionEvent evt) {
            try {
                paste();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    // The following three actions were added for the OMP project.
    // (MFO, 09 July 2001)

    /**
     * Action to use for the "MsbFolder" menu and toolbar items.
     */
    protected AbstractAction msbFolderAction =
            new AbstractAction( "Create an MSB folder") {
        public void actionPerformed(ActionEvent evt) {
            try {
                addMsbFolder();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "AndFolder" menu and toolbar items.
     */
    protected AbstractAction andFolderAction =
            new AbstractAction("Create an AND folder") {
        public void actionPerformed(ActionEvent evt) {
            try {
                addAndFolder();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "OrFolder" menu and toolbar items.
     */
    protected AbstractAction orFolderAction =
            new AbstractAction("Create an OR folder") {
        public void actionPerformed(ActionEvent evt) {
            try {
                addOrFolder();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "SurveyFolder" menu and toolbar items.
     */
    protected AbstractAction surveyFolderAction =
            new AbstractAction("Create a Survey Container") {
        public void actionPerformed(ActionEvent evt) {
            try {
                addSurveyFolder();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "ObsFolder" menu and toolbar items.
     */
    protected AbstractAction obsFolderAction =
            new AbstractAction("Create an observation folder") {
        public void actionPerformed(ActionEvent evt) {
            try {
                addFolder();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "ObsGroup" menu and toolbar items.
     */
    protected AbstractAction obsGroupAction =
            new AbstractAction("Create an observation group") {
        public void actionPerformed(ActionEvent evt) {
            try {
                addGroup();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "Observation" menu and toolbar items.
     */
    protected AbstractAction observationAction =
            new AbstractAction("Create an observation") {
        public void actionPerformed(ActionEvent evt) {
            try {
                addObservation();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "Note" menu and toolbar items.
     */
    protected AbstractAction noteAction = new AbstractAction("Create a note") {
        public void actionPerformed(ActionEvent evt) {
            try {
                addNote();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "LibFolder" menu and toolbar items.
     */
    protected AbstractAction libFolderAction =
            new AbstractAction("Create a library") {
        public void actionPerformed(ActionEvent evt) {
            try {
                addLibFolder();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "PosEditor" menu and toolbar items.
     */
    protected AbstractAction posEditorAction =
            new AbstractAction("Show position editor") {
        public void actionPerformed(ActionEvent evt) {
            try {
                showPositionEditor();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the Prioritize menu and toolbar items.
     */
    protected AbstractAction prioritizeAction =
            new AbstractAction("Prioritize") {
        public void actionPerformed(ActionEvent evt) {
            try {
                prioritize();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Action to use for the "Validation" menu and toolbar items.
     */
    protected AbstractAction validationAction =
            new AbstractAction("Validate") {
        public void actionPerformed(ActionEvent evt) {
            try {
                doValidation();
            } catch (Exception e) {
                DialogUtil.error(SpTreeGUI.this, e);
            }
        }
    };

    /**
     * Default constructor.
     *
     * If you use this version, you need to call setParentFrame() to set
     * the parent frame.
     */
    public SpTreeGUI() {
        setLayout(new BorderLayout());
        backAction.setEnabled(false);
        forwAction.setEnabled(false);
        libFolderAction.setEnabled(false);

        tree = new OtTreeWidget();
        tree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        tree.setMinimumSize(new Dimension(200, 200));

        editorPane = new JPanel();
        editorPane.setLayout(new BorderLayout());
        editorPane.setMinimumSize(new Dimension(400, 200));

        JSplitPane splitPane =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree, editorPane);
        splitPane.setOneTouchExpandable(false);
        add("Center", splitPane);
    }

    /**
     * Make a new SpTreeGUI widget.
     *
     * @param parent the top level parent frame
     */
    public SpTreeGUI(JFrame parent) {
        this();
        this.parent = parent;
    }

    /**
     * Return the top level parent frame used to close the window.
     */
    public JFrame getParentFrame() {
        return parent;
    }

    /**
     * Set the top level parent frame used to close the window.
     */
    public void setParentFrame(JFrame p) {
        parent = p;
    }

    /**
     * Set the frame's title.
     */
    public void setTitle(String s) {
        if (parent != null) {
            parent.setTitle(s);
        }
    }

    /**
     * Return the frame's title.
     */
    public String getTitle() {
        if (parent != null) {
            return parent.getTitle();
        }

        return "";
    }

    // The following three methods were added for the OMP project.
    // (MFO, 09 July 2001)

    /**
     * Create an observation folder.
     */
    public abstract void addMsbFolder();

    /**
     * Create an observation folder.
     */
    public abstract void addAndFolder();

    /**
     * Create an observation folder.
     */
    public abstract void addOrFolder();

    /**
     * Create an observation folder.
     */
    public abstract void addSurveyFolder();

    /**
     * Create an observation folder.
     */
    public abstract void addFolder();

    /**
     * Create an observation group.
     */
    public abstract void addGroup();

    /**
     * Create an observation.
     */
    public abstract void addObservation();

    /**
     * Add a note to the tree.
     */
    public abstract void addNote();

    /**
     * Add a library folder to the tree.
     */
    public abstract void addLibFolder();

    /**
     * Cut the selected item(s) to the clipboard.
     */
    public abstract void cut();

    /**
     * Copy the selected item(s) to the clipboard.
     */
    public abstract void copy();

    /**
     * Paste the selected item(s) from the clipboard.
     */
    public abstract void paste();

    /**
     * Pop up a dialog to open a new science program.
     *
     * @param newWindow if true, open a new window, otherwise reuse this one
     */
    public abstract void open(boolean newWindow);

    /**
     * Save the current science program.
     */
    public abstract void save();

    /**
     * Display the position editor.
     */
    public abstract void showPositionEditor();

    /**
     * Prioritize all the MSBs
     */
    public abstract void prioritize();

    /**
     * Validate selected Science Program or Observation.
     */
    public abstract boolean doValidation();

    // These are for the GenericToolBarTarget interface
    public AbstractAction getOpenAction() {
        return openAction;
    }

    public AbstractAction getBackAction() {
        return backAction;
    }

    public AbstractAction getForwAction() {
        return forwAction;
    }

    // access other toolbar actions
    public AbstractAction getSaveAction() {
        return saveAction;
    }

    public AbstractAction getCutAction() {
        return cutAction;
    }

    public AbstractAction getCopyAction() {
        return copyAction;
    }

    public AbstractAction getPasteAction() {
        return pasteAction;
    }

    public AbstractAction getObsFolderAction() {
        return obsFolderAction;
    }

    public AbstractAction getObsGroupAction() {
        return obsGroupAction;
    }

    public AbstractAction getObservationAction() {
        return observationAction;
    }

    public AbstractAction getNoteAction() {
        return noteAction;
    }

    public AbstractAction getLibFolderAction() {
        return libFolderAction;
    }

    // The following three methods were added for the OMP project.
    // (MFO, 09 July 2001)
    public AbstractAction getMsbFolderAction() {
        return msbFolderAction;
    }

    public AbstractAction getAndFolderAction() {
        return andFolderAction;
    }

    public AbstractAction getOrFolderAction() {
        return orFolderAction;
    }

    public AbstractAction getSurveyFolderAction() {
        return surveyFolderAction;
    }

    public AbstractAction getPosEditorAction() {
        return posEditorAction;
    }

    public AbstractAction getPrioritizeAction() {
        return prioritizeAction;
    }

    public AbstractAction getValidationAction() {
        return validationAction;
    }
}
