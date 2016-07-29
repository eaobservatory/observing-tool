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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import jsky.app.ot.ProgramInfo;
import jsky.app.ot.gui.MultiSelTreeWidgetWatcher;
import jsky.app.ot.gui.TreeNodeWidgetExt;
import jsky.app.ot.gui.TreeWidgetExt;
import jsky.app.ot.gui.TreeWidgetWatcher;
import gemini.sp.SpEditChangeObserver;
import gemini.sp.SpHierarchyChangeObserver;
import gemini.sp.SpAvTable;
import gemini.sp.SpEditState;
import gemini.sp.SpFactory;
import gemini.sp.SpInsertData;
import gemini.sp.SpItem;
import gemini.sp.SpLibrary;
import gemini.sp.SpMSB;
import gemini.sp.SpObs;
import gemini.sp.SpObsFolder;
import gemini.sp.SpObsGroup;
import gemini.sp.SpProg;
import gemini.sp.SpRootItem;
import gemini.sp.SpTreeMan;
import gemini.sp.SpType;
import gemini.util.ConfigWriter;
import gemini.util.ObservingToolUtilities;
import orac.util.SpInputXML;
import jsky.app.ot.tpe.TelescopePosEditor;
import jsky.app.ot.tpe.TpeManager;
import jsky.app.ot.tpe.TpeManagerWatcher;
import jsky.app.ot.OtCfg;
import ot.util.DialogUtil;

import orac.helptool.JHLauncher;

import orac.validation.SpValidation;
import orac.validation.ErrorMessage;
import ot.ReportBox;

import orac.util.FileFilterXML;

import omp.SpClient;

/**
 * Button manager base class.
 *
 * Helper classes derived from this class keep their associated button disabled
 * or enabled appropriately.
 */
abstract class ButtonManagerBase implements SpHierarchyChangeObserver,
        TreeWidgetWatcher {
    /** The OT tree widget */
    protected OtTreeWidget _tw;

    /** The action object for the button and/or menu item. */
    protected AbstractAction _action;

    /** The root of the science program tree */
    protected SpItem _spRoot;

    /**
     * Initialize with the OT tree widget and the button's action object.
     */
    ButtonManagerBase(OtTreeWidget tw, AbstractAction action) {
        _tw = tw;
        _tw.addWatcher(this);
        _action = action;
    }

    /**
     * Reset to use the given science program tree.
     */
    public void resetItem(SpItem spRoot) {
        if (_spRoot != null) {
            _spRoot.getEditFSM().deleteHierarchyChangeObserver(this);
            _spRoot.getTable().noNotifyRmAll();
        }

        _spRoot = spRoot;
        _spRoot.getEditFSM().addHierarchyChangeObserver(this);
    }

    /**
     * Receive notification that new items have been added to the program.
     */
    public void spItemsAdded(SpItem parent, SpItem[] children, SpItem afterChild) {
        _updateButton();
    }

    /**
     * Receive notification that items have been removed from the program.
     */
    public void spItemsRemoved(SpItem parent, SpItem[] children) {
    }

    /**
     * Receive notification that items have been moved in the program.
     */
    public void spItemsMoved(SpItem oldParent, SpItem[] children,
            SpItem newParent, SpItem afterChild) {
        _updateButton();
    }

    /**
     * Receive notification that a tree node is selected.
     */
    public void nodeSelected(TreeWidgetExt tw, TreeNodeWidgetExt tnw) {
        _updateButton();
    }

    /**
     * Receive notification that a node has been acted upon.
     */
    public void nodeAction(TreeWidgetExt tw, TreeNodeWidgetExt tnw) {
    }

    /**
     * Enable or disable the button based upon the current context.
     */
    abstract protected void _updateButton();
}

/**
 * Base class for the ObsGroup and ObsFolder button manager classes.
 *
 * This helper class keeps the "create obs group"/"create obs folder"
 * button disabled or enabled appropriately.
 */
abstract class GroupingButtonManagerBase extends ButtonManagerBase implements
        MultiSelTreeWidgetWatcher {
    GroupingButtonManagerBase(OtTreeWidget tw, AbstractAction action) {
        super(tw, action);
    }

    /**
     * This method is called whenever multiple tree nodes are selected.
     */
    public void multiNodeSelect(TreeWidgetExt tw, Vector<Object> tnwA) {
        _updateButton();
    }

    protected boolean _makeGroup(SpItem newItem, boolean carryOutActions) {
        SpItem[] spItemA = _tw.getMultiSelectedItems();

        if ((spItemA == null) || (spItemA.length == 0)) {
            spItemA = new SpItem[1];
            spItemA[0] = _tw.getSelectedItem();

            if (spItemA[0] == null) {
                _action.setEnabled(false);
                return false;
            }
        }

        SpItem lastItem = spItemA[spItemA.length - 1];

        SpInsertData spID = SpTreeMan.evalInsertAfter(newItem, lastItem);
        if (spID == null) {
            _action.setEnabled(false);
            return false;
        }

        SpInsertData spID2 = SpTreeMan.evalInsertInside(spItemA, newItem);
        if (spID2 == null) {
            _action.setEnabled(false);
            return false;
        }

        _action.setEnabled(true);
        if (carryOutActions) {
            SpTreeMan.insert(spID);
            SpTreeMan.move(spID2);
        }

        return true;
    }
}

/**
 * The ButtonManager for the ObsGroup button.
 */
class ObsGroupButtonManager extends GroupingButtonManagerBase {
    private SpObsGroup _obsGroup =
            (SpObsGroup) SpFactory.create(SpType.OBSERVATION_GROUP);

    ObsGroupButtonManager(OtTreeWidget tw, AbstractAction action) {
        super(tw, action);
    }

    /**
     * Enable or disable the button based upon the current context.
     */
    protected void _updateButton() {
    }

    /**
     * Create an observation group.
     */
    public void addGroup() {
        _tw.addItem(SpFactory.create(SpType.OBSERVATION_GROUP));
        // Create a new ObsGroup for next time

        if (_makeGroup(_obsGroup, true)) {
            _obsGroup = (SpObsGroup) SpFactory.create(SpType.OBSERVATION_GROUP);
        }
    }

}

/**
 * The ButtonManager for the ObsFolder button.
 */
class ObsFolderButtonManager extends GroupingButtonManagerBase {
    private SpObsFolder _obsFolder =
            (SpObsFolder) SpFactory.create(SpType.OBSERVATION_FOLDER);

    ObsFolderButtonManager(OtTreeWidget tw, AbstractAction action) {
        super(tw, action);
    }

    /**
     * Enable or disable the button based upon the current context.
     */
    protected void _updateButton() {
    }

    /**
     * Create an observation folder.
     */
    public void addFolder() {
        _tw.addItem(SpFactory.create(SpType.OBSERVATION_FOLDER));

        // Create a new folder for next time
        if (_makeGroup(_obsFolder, true)) {
            _obsFolder =
                    (SpObsFolder) SpFactory.create(SpType.OBSERVATION_FOLDER);
        }
    }

}

/**
 * The OtWindow class controls the main editing window for a Science
 * Program.
 *
 * In the GUI it displays, the hierarchy of the program is
 * shown and may be manipulated.  This class initializes the main
 * window and handles basic interaction such as button press events
 * and menu choices.
 *
 * @author Allan Brighton (ported to Swing/JSky, changed the layout)
 */
@SuppressWarnings("serial")
public abstract class OtWindow extends SpTreeGUI implements SpEditChangeObserver,
        TpeManagerWatcher {
    /** Displays the science program hierarchy. */
    protected OtTreeWidget _tw;

    /** The editor window for the selected node */
    protected OtItemEditorWindow _itemEditor;

    /** The current science program root */
    protected SpItem _curItem;
    protected ProgramInfo _progInfo;

    /** Manages the Observation Group enabled state */
    protected ObsGroupButtonManager _obsGroupButtonMan;

    /** Manages the Observation Folder enabled state */
    protected ObsFolderButtonManager _obsFolderButtonMan;

    /** Used to go back to a previous science program */
    protected Stack<OtHistoryItem> backStack = new Stack<OtHistoryItem>();

    /** Used to go forward to the next science program */
    protected Stack<OtHistoryItem> forwStack = new Stack<OtHistoryItem>();

    /**
     * Set when the back or forward actions are active to avoid the normal
     * history handling
     */
    protected boolean noStack = false;

    /** List of OtHistoryItem, for previously viewed science programs. */
    protected LinkedList<OtHistoryItem> historyList =
            new LinkedList<OtHistoryItem>();

    /** List of listeners for change events. */
    protected EventListenerList listenerList = new EventListenerList();

    /** Used to exit the application when the last main frame is closed. */
    static protected int _frameCount = 0;

    /** XML file filter (*.xml). MFO 04 June 2001 */
    protected FileFilterXML xmlFilter = new FileFilterXML();

    /**
     * Create an OtWindow of the appropriate type.
     */
    public static void create(SpRootItem spItem, FileInfo fi) {
        SpType spType = spItem.type();
        OtWindow editor = null;

        if (spType.equals(SpType.SCIENCE_PROGRAM)
                || spType.equals(SpType.SCIENCE_PLAN)
                || spType.equals(SpType.LIBRARY)) {
            editor = new OtProgWindow(spItem, fi);

        } else {
            DialogUtil.error(editor,
                    "Can't create an OtWindow for type: " + spItem.type());
            return;
        }

        new OtWindowFrame(editor);
    }

    /**
     * Construct with a brand new program.
     */
    protected OtWindow(SpRootItem spItem) {
        this(spItem, null);
    }

    /**
     * This constructor does all the work of initializing the OtWindow.
     */
    protected OtWindow(SpRootItem spItem, FileInfo fileInfo) {
        _init(spItem, fileInfo);
        _frameCount++;
    }

    /**
     * Do one-time only initialization of the window.
     */
    protected void _init(SpRootItem spItem, FileInfo fileInfo) {
        // Initialize the ProgramInfo structure
        _progInfo = new ProgramInfo();

        ProgramInfo.register(_progInfo, spItem);

        if (fileInfo == null) {
            // Init FileInfo from attributes in the spItem if present.
            // This will allow programs obtained from the database to have
            // their filename and directory already set.
            fileInfo = new FileInfo();
            SpAvTable avTab = spItem.getTable();

            String dir = OT.getOtUserDir();
            String filename = avTab.get(".gui.filename");
            boolean hasBeenSaved = avTab.getBool(".gui.hasBeenSaved");

            if ((dir != null) && (filename != null)) {
                fileInfo.dir = dir;
                fileInfo.filename = filename;
                fileInfo.hasBeenSaved = hasBeenSaved;
            } else {
                fileInfo.dir = avTab.get(".gui.dir");
            }
        }

        _progInfo.file = fileInfo;

        // Set up the tree widget
        _tw = this.tree;

        _tw.addWatcher(new TreeWidgetWatcher() {
            public void nodeSelected(TreeWidgetExt tw, TreeNodeWidgetExt tnw) {
                OtWindow.this.nodeSelect(tw, tnw);
            }

            public void nodeAction(TreeWidgetExt tw, TreeNodeWidgetExt tnw) {
                OtWindow.this.nodeAction(tw, tnw);
            }
        });

        _obsGroupButtonMan = new ObsGroupButtonManager(_tw, obsGroupAction);
        _obsFolderButtonMan = new ObsFolderButtonManager(_tw, obsFolderAction);

        // Create the item editor window
        if (_itemEditor == null) {
            _itemEditor = new OtItemEditorWindow();
            editorPane.add("Center", _itemEditor);
            _itemEditor.setParentFrame(parent);
            _itemEditor.setOtWindow(this);
        }

        _itemEditor.setInfo(_progInfo);

        // Initialize the title
        _updateTitle(spItem);

        _updateEditPencil(spItem.getEditState());

        // Watch filename changes
        _progInfo.file.addObserver(new Observer() {
            public void update(Observable obs, Object arg) {
                _updateTitle();
            }
        });

        // Watch when a position editor is opened.
        TpeManager.addWatcher(this, spItem);

        // Set the program displayed in the tree.
        resetItem(spItem);
    }

    /**
     * Called when a new program is loaded in the same window to reinitialize
     * everything.
     */
    protected void _reinit(SpRootItem spItem, FileInfo fileInfo) {
        // Initialize the ProgramInfo structure
        _progInfo = new ProgramInfo();

        ProgramInfo.register(_progInfo, spItem);

        if (fileInfo == null) {
            // Init FileInfo from attributes in the spItem if present.
            // This will allow programs obtained from the database to have
            // their filename and directory already set.
            fileInfo = new FileInfo();
            SpAvTable avTab = spItem.getTable();

            String dir = OT.getOtUserDir();
            String filename = avTab.get(".gui.filename");
            boolean hasBeenSaved = avTab.getBool(".gui.hasBeenSaved");

            if ((dir != null) && (filename != null)) {
                fileInfo.dir = dir;
                fileInfo.filename = filename;
                fileInfo.hasBeenSaved = hasBeenSaved;
            } else {
                dir = avTab.get(".gui.dir");
            }
        }
        _progInfo.file = fileInfo;

        // Set up the tree widget
        _itemEditor.setInfo(_progInfo);

        // Initialize the title
        _updateTitle(spItem);

        _updateEditPencil(spItem.getEditState());

        // Watch when a position editor is opened.
        TpeManager.addWatcher(this, spItem);

        // Set the program displayed in the tree.
        resetItem(spItem);

        // notify any listeners that a new program was loaded
        fireChange(new ChangeEvent(this));
    }

    /**
     * Set the top level parent frame used to close the window.
     */
    public void setParentFrame(JFrame p) {
        super.setParentFrame(p);
        if (_itemEditor != null)
            _itemEditor.setParentFrame(p);
    }

    /**
     * Return true if the program has been saved.
     */
    public boolean progHasBeenSaved() {
        return _progInfo.file.hasBeenSaved;
    }

    /**
     * Get the item being edited..
     */
    public SpRootItem getItem() {
        return _tw.getProg();
    }

    /**
     * Return the OT tree widget.
     */
    public OtTreeWidget getTreeWidget() {
        return _tw;
    }

    /**
     * Has the program been edited?
     */
    public boolean isEdited() {
        return (getItem().getEditState() == SpEditState.EDITED);
    }

    /**
     * Reset the item being edited.
     *
     * The given SpItem is assumed to be a different version of the item
     * already being edited.  So the _progInfo fields should remain the same.
     */
    void resetItem(SpRootItem spItem) {
        SpRootItem oldItem = getItem();

        if (oldItem != null) {
            TpeManager.remap(oldItem, spItem);
            oldItem.getEditFSM().deleteEditChangeObserver(this);
        }

        if (spItem != oldItem) {
            _tw.resetProg(spItem);
        }

        // Initialize the title
        _updateEditPencil(spItem.getEditState());
        spItem.getEditFSM().addEditChangeObserver(this);
        spItem.getEditFSM().reset();

        _obsGroupButtonMan.resetItem(spItem);
        _obsFolderButtonMan.resetItem(spItem);
    }

    /**
     * Determine whether it is okay to close this program, prompting the
     * user if the program has been edited.
     */
    protected boolean isOkayToClose() {
        // Never prompt to save a library - caveat emptor!
        if (getItemType().equalsIgnoreCase("library")) {
            return true;
        }

        // If not edited, then it is okay to close this program.
        if (!isEdited() && !OtProps.isSaveShouldPrompt()) {
            return true;
        }

        // The program was edited and we should prompt, so do so.
        String msg = "Save changes to " + getItemType() + " '" + getFilename()
                + "'?";

        int response = DialogUtil.confirm(this, msg);

        if (response == JOptionPane.CANCEL_OPTION) {
            return false;
        }

        // If the user wants to save changes, it is okay to close if the save
        // succeeds.
        if (response == JOptionPane.YES_OPTION) {
            return doSaveChanges();
        }

        // The user didn't want to save changes, so go ahead and close.
        return true;
    }

    /**
     * Save the changes, returning true if successful.
     */
    public boolean doSaveChanges() {
        return OtFileIO.save(getItem(), _progInfo.file);
    }

    /**
     * Save the observation as an ORAC Sequence, returning true if successful.
     *
     * MFO: copied from FreeBongo OT (orac2) and modified.
     */
    public boolean doSaveSequence() {
        OtTreeNodeWidget tnw;
        tnw = (OtTreeNodeWidget) OtWindow.this._tw.getSelectedNode();
        SpItem spitem = tnw.getItem();

        // Walk back up the tree to see if this is contained in a survey
        // component since we can handle these
        SpItem parent = spitem;
        boolean inSurvey = false;

        while (parent != null) {
            if (parent.type().equals(SpType.SURVEY_CONTAINER)) {
                inSurvey = true;
                break;
            }

            parent = parent.parent();
        }

        if (inSurvey) {
            DialogUtil.error(this,
                    "Can not translate observations within a Survey Container");
            return false;
        }

        // Check if this is an Observation.
        if (!(spitem.type().equals(SpType.OBSERVATION))) {
            spitem = findSpObs(spitem);
        }

        if (spitem != null) {
            SpObs spobs = (SpObs) spitem;

            // Create a translator class and do the translation
            try {
                Vector<String> vector = new Vector<String>();
                spobs.translateProlog(vector);
                spobs.translate(vector);
                spobs.translateEpilog(vector);
            } catch (Exception e) {
                e.printStackTrace();
                DialogUtil.error(this, e.getMessage());
                return false;
            }

        } else {
            // The user didn't select an observation item for the translation.
            DialogUtil.error(this, "Selected node is not an observation"
                    + " or within an observation");
            return false;
        }

        DialogUtil.message(this, "Observation saved to "
                + ConfigWriter.getCurrentInstance().getExecName());

        return true;
    }

    /**
     * Close this window if possible.
     *
     * Return false if not. The reason why the program might not be
     * closeable is if it has been edited and the user wants to cancel
     * the close instead of saving or ignoring changes.
     */
    public boolean closeApp() {
        if (!isOkayToClose()) {
            return false;
        }

        SpItem spItem = _tw.getProg();
        spItem.getEditFSM().deleteEditChangeObserver(this);

        TpeManager.remove(spItem);
        TpeManager.deleteWatchers(spItem);

        TelescopePosEditor.adjustWidthPreference();

        // Try to clear up memory
        _tw.resetProg();

        parent.setVisible(false);
        parent.dispose();

        return true;
    }

    /**
     * Print the program being edit to stdout.
     *
     * This is a debugging method.
     */
    public void showProg() {
        _tw.getProg().print();
    }

    /**
     * A position editor has been opened.
     *
     * @see TpeManagerWatcher
     */
    public void tpeOpened(TelescopePosEditor tpe) {
        tpe.setTitle("Position Editor");
    }

    /**
     * Get the filename associated with the program, plan, or whatever.
     */
    public String getFilename() {
        return _progInfo.file.filename;
    }

    /**
     * Get the human readable name of the type of the thing being edited
     * (for example, "Science Program", "Science Plan", or "Library").
     */
    public String getItemType() {
        return getItem().type().getReadable();
    }

    /**
     * Update the title of the program or plan being edited.
     */
    protected void _updateTitle() {
        _updateTitle(_tw.getProg());
    }

    /**
     * Update the title of the program or plan being edited.
     */
    protected void _updateTitle(SpItem spItem) {
        String prefix = "";

        if (spItem.type().equals(SpType.SCIENCE_PROGRAM)) {
            prefix += "Program: ";
        } else if (spItem.type().equals(SpType.SCIENCE_PLAN)) {
            prefix += "Plan: ";
        } else if (spItem.type().equals(SpType.LIBRARY)) {
            prefix += "Library: ";
        } else {
            prefix += "UNKOWN TYPE: ";
        }

        setTitle(prefix + spItem.getTitle());
        if (parent != null) {
            parent.setName(spItem.getTitle());
        }

        TelescopePosEditor tpe = TpeManager.get(spItem);
        if (tpe != null) {
            tpe.setTitle("Position Editor (" + getTitle() + ")");
        }
    }

    /**
     * Update the edit pencil depending upon the edit state of the program.
     *
     * If the program has been edited, the pencil should be showing.
     * Otherwise it should be hidden.
     */
    private void _updateEditPencil(int state) {
        saveAction.setEnabled(state == SpEditState.EDITED);
    }

    /**
     * Receive notification that the program edit state has changed.
     */
    public void spEditStateChange(SpItem rootNode) {
        _updateEditPencil(rootNode.getEditState());
    }

    /**
     * This method is called whenever a tree node is selected.
     */
    public void nodeSelect(TreeWidgetExt tw, TreeNodeWidgetExt tnw) {
        SpItem spItem = ((OtTreeNodeWidget) tnw).getItem();

        _itemEditor.setItem(spItem);
        TelescopePosEditor tpe = TpeManager.get(spItem);

        if (tpe != null) {
            tpe.reset(spItem);
        }

        _curItem = spItem;
    }

    /**
     * This method is called whenever a tree node is double clicked.
     */
    public void nodeAction(TreeWidgetExt tw, TreeNodeWidgetExt tnw) {
        _itemEditor.setItem(((OtTreeNodeWidget) tnw).getItem());
    }

    // The following three methods were added for the OMP project.
    // (MFO, 09 July 2001)

    /**
     * Create an observation folder.
     */
    public void addMsbFolder() {
        _tw.addItem(SpFactory.create(SpType.MSB_FOLDER));
    }

    /**
     * Create an observation folder.
     */
    public void addAndFolder() {
        _tw.addItem(SpFactory.create(SpType.AND_FOLDER));
    }

    /**
     * Create an observation folder.
     */
    public void addOrFolder() {
        _tw.addItem(SpFactory.create(SpType.OR_FOLDER));
    }

    /**
     * Create an observation folder.
     */
    public void addSurveyFolder() {
        _tw.addItem(SpFactory.create(SpType.SURVEY_CONTAINER));
    }

    /**
     * Create an observation folder.
     */
    public void addFolder() {
        // MFO May 28 2001
        _tw.addItem(SpFactory.create(SpType.OBSERVATION_FOLDER));
        //_obsFolderButtonMan.addFolder();
    }

    /**
     * Create an observation group.
     */
    public void addGroup() {
        // MFO May 28 2001
        _tw.addItem(SpFactory.create(SpType.OBSERVATION_GROUP));
        //_obsGroupButtonMan.addGroup();
    }

    /**
     * Create an observation.
     */
    public void addObservation() {
        // MFO, 23 October 2001
        // After spObs has been added call spObs.updateMsbAttributes().
        // This will set attributes in spObs according to whether spObs is the
        // child node of an SpMSB or whether spObs is an MSB in its own right.
        SpObs spObs = (SpObs) SpFactory.create(SpType.OBSERVATION);
        _tw.addItem(spObs);
        spObs.updateMsbAttributes();

        // Update collapsed/expanded display for tree nodes so that the
        // SpIterFolder inside the Observation is displayed as expanded.
        _tw.updateNodeExpansions();
    }

    /**
     * Add a note to the tree.
     */
    public void addNote() {
        _tw.insertItemAfter(SpFactory.create(SpType.NOTE));
    }

    /**
     * Cut the selected item(s) to the clipboard.
     */
    public void cut() {
        _tw.cutToClipboard();
    }

    /**
     * Copy the selected item(s) to the clipboard.
     */
    public void copy() {
        _tw.copyToClipboard();
    }

    /**
     * Paste the selected item(s) from the clipboard.
     */
    public void paste() {
        _tw.pasteFromClipboard();
    }

    /**
     * Display a new science program window.
     */
    public void newProgram() {
        new OtWindowFrame(new OtProgWindow());
    }

    /**
     * Pop up a dialog to open a new science program.
     *
     * @param newWindow if true, open a new window, otherwise reuse this one
     */
    public void open(boolean newWindow) {
        if (newWindow) {
            // open in a new window
            OtFileIO.open();

        } else {
            if (!isOkayToClose()) {
                return;
            }

            // open in this window
            JFileChooser fileChooser = new JFileChooser(OT.getOtUserDir());
            fileChooser.addChoosableFileFilter(xmlFilter);
            fileChooser.setFileFilter(xmlFilter);

            int option = fileChooser.showOpenDialog(null);

            if (option != JFileChooser.APPROVE_OPTION
                    || fileChooser.getSelectedFile() == null) {
                return;
            }

            File file = fileChooser.getSelectedFile();

            if (file != null) {
                open(file);
            }
        }
    }

    /**
     * Load the given science program file in this window.
     *
     * @param file the file containing the program
     */
    public void open(File file) {
        String dir = file.getParent();
        String filename = file.getName();

        if (filename != null) {
            SpRootItem spItem = OtFileIO.fetchSp(dir, filename);

            if (spItem != null) {
                FileInfo fi = new FileInfo(dir, filename, true);
                open(spItem, fi);
            }
        }
    }

    /**
     * Load the given science program in this window.
     *
     * @param spItem the program to load
     * @param fi contains information about the file (may be null)
     */
    public void open(SpRootItem spItem, FileInfo fi) {
        addToHistory();
        _reinit(spItem, fi);
    }

    /**
     * Save the current science program.
     */
    public void save() {
        doSaveChanges();
    }

    /**
     * Go back to the previous item in the history list
     */
    public void back() {
        if (backStack.size() == 0) {
            return;
        }

        SpRootItem rootItem = getItem();

        if (rootItem != null) {
            String title = rootItem.getTitle();
            FileInfo fi = _progInfo.file;
            String filename = fi.dir + File.separatorChar + fi.filename;
            forwStack.push(new OtHistoryItem(title, filename, rootItem));
            forwAction.setEnabled(true);
        }

        OtHistoryItem item = backStack.pop();

        if (backStack.size() == 0) {
            backAction.setEnabled(false);
        }

        noStack = true;

        try {
            item.actionPerformed((ActionEvent) null);
        } catch (Exception e) {
            DialogUtil.error(this, e);
        }

        noStack = false;
    }

    /**
     * Go forward to the next component in the history list
     */
    public void forward() {
        if (forwStack.size() == 0) {
            return;
        }

        SpRootItem rootItem = getItem();

        if (rootItem != null) {
            String title = rootItem.getTitle();
            FileInfo fi = _progInfo.file;
            String filename = fi.dir + File.separatorChar + fi.filename;
            backStack.push(new OtHistoryItem(title, filename, rootItem));
            backAction.setEnabled(true);
        }

        OtHistoryItem item = forwStack.pop();

        if (forwStack.size() == 0) {
            forwAction.setEnabled(false);
        }

        noStack = true;

        try {
            item.actionPerformed((ActionEvent) null);
        } catch (Exception e) {
            DialogUtil.error(this, e);
        }

        noStack = false;
    }

    /**
     * Add the current science program to the history list
     */
    protected void addToHistory() {
        SpRootItem rootItem = getItem();

        if (!noStack && rootItem != null) {
            String title = rootItem.getTitle();
            FileInfo fi = _progInfo.file;
            String filename = fi.dir + File.separatorChar + fi.filename;
            OtHistoryItem item = new OtHistoryItem(title, filename, rootItem);

            backStack.push(item);
            backAction.setEnabled(true);

            if (forwStack.size() != 0) {
                cleanupHistoryStack(forwStack);
                forwStack.clear();
                forwAction.setEnabled(false);
            }

            // add to the history list and remove duplicates
            ListIterator<OtHistoryItem> it = historyList.listIterator(0);
            Vector<Integer> indexes = new Vector<Integer>();

            for (int i = 0; it.hasNext(); i++) {
                OtHistoryItem hi = it.next();

                if (hi.title.equals(title)) {
                    indexes.add(i);
                }
            }

            while (indexes.size() != 0) {
                historyList.remove(indexes.remove(0));
            }

            historyList.addFirst(item);

            if (historyList.size() > 20) {
                historyList.removeLast();
            }
        }
    }

    /** Add history items (for previously displayed science programs)
     * to the given menu
     */
    public void addHistoryMenuItems(JMenu menu) {
        ListIterator<OtHistoryItem> it = historyList.listIterator(0);

        while (it.hasNext()) {
            menu.add(it.next());
        }
    }

    /**
     * This method may be redefined in subclasses to do cleanup work before
     * components are removed from the given history stack (backStack or
     * forwStack).
     */
    protected void cleanupHistoryStack(Stack<OtHistoryItem> stack) {
    }

    /**
     * Display the position editor.
     */
    public void showPositionEditor() {
        // Figure out which node is selected.
        OtTreeNodeWidget destTNW = (OtTreeNodeWidget) _tw.getSelectedNode();

        if (destTNW == null) {
            destTNW = (OtTreeNodeWidget) _tw.getRoot();
        }

        SpItem spItem = destTNW.getItem();

        TpeManager.open(spItem);
    }

    /**
     * Method invoked by the prioritizeAction action of SpTreeGUI.
     *
     * This overrides an empty method in that class.
     *
     * Previous response was to immediately auto prioritize all the MSBs
     * currently in the Tree in the order in which they are displayed.
     * Now it opens a dialogue window offering a choice of prioritization
     * functions.
     */
    public void prioritize() {
        JDialog dialog = new PrioritizationFunctionDialog(parent);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Dialog allowing the user to choose a prioritization function.
     */
    private class PrioritizationFunctionDialog extends JDialog implements
            ActionListener {
        private JRadioButton auto, set, add, sub;
        private SpinnerNumberModel num;
        private JCheckBox selected;

        public PrioritizationFunctionDialog(JFrame parent) {
            super(parent, "Automatic Prioritization", true);

            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            ButtonGroup group = new ButtonGroup();

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // Automatic
            JPanel subpanel = new JPanel();
            subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.PAGE_AXIS));
            subpanel.setBorder(BorderFactory.createEtchedBorder());
            subpanel.setAlignmentX(LEFT_ALIGNMENT);
            auto = new JRadioButton("Automatic", true);
            group.add(auto);
            subpanel.add(auto);
            subpanel.add(new JLabel(
                    "Assigns new priorities to MSBs from 1 to 99"));
            subpanel.add(new JLabel(
                    "based on the order in which they appear"));
            subpanel.add(new JLabel(
                    "(or were selected if 'Selected MSBs only'"));
            subpanel.add(new JLabel("is selected)."));
            panel.add(subpanel);

            // Set / adjust
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            subpanel = new JPanel();
            subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.PAGE_AXIS));
            subpanel.setBorder(BorderFactory.createEtchedBorder());
            subpanel.setAlignmentX(LEFT_ALIGNMENT);
            set = new JRadioButton("Set priority");
            subpanel.add(set);
            group.add(set);
            sub = new JRadioButton(
                    "Increase priority (lower priority number)");
            subpanel.add(sub);
            group.add(sub);
            add = new JRadioButton(
                    "Decrease priority (higher priority number)");
            subpanel.add(add);
            group.add(add);
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEADING));
            row.setAlignmentX(LEFT_ALIGNMENT);
            row.add(new JLabel("Priority or change"));
            num = new SpinnerNumberModel(1, 1, 99, 1);
            JSpinner spinner = new JSpinner(num);
            row.add(spinner);
            subpanel.add(row);
            panel.add(subpanel);

            // Options
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            selected = new JCheckBox("Selected MSBs only", false);
            selected.setAlignmentX(LEFT_ALIGNMENT);
            panel.add(selected);
            add(panel, BorderLayout.CENTER);

            // Buttons
            panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            JButton ok = new JButton("OK");
            ok.addActionListener(this);
            panel.add(ok);
            JButton cancel = new JButton("Cancel");

            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            panel.add(cancel);
            add(panel, BorderLayout.PAGE_END);

            pack();
        }

        public void actionPerformed(ActionEvent e) {
            if (auto.isSelected()) {
                _tw.autoAssignPriority(selected.isSelected());
            } else if (set.isSelected()) {
                _tw.autoAssignPriorityFixed(selected.isSelected(),
                        (Integer) num.getNumber());
            } else if (add.isSelected() || sub.isSelected()) {
                _tw.autoAssignPriorityShift(selected.isSelected(),
                        (sub.isSelected() ? -1 : 1)
                                * (Integer) num.getNumber());
            } else {
                JOptionPane.showMessageDialog(this,
                        "No prioritization action selected", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();
        }
    }

    /**
     * Perform validation of the item currently selected in the tree widget.
     *
     * @return true  if selected component has been checked and found valid.
     *         false if the component has been checked and found invalid
     *               or if the component has not been checked because it is
     *               neither an observation nor a science program.
     */
    public boolean doValidation() {
        SpItem spItem = _tw.getSelectedItem();
        String reportBoxTitle = "Validation Report";

        // checking whether an item has been selected that can be checked and
        // returning false otherwise.
        if (!(spItem instanceof SpProg) && !(spItem instanceof SpMSB)) {
            new ReportBox(
                    "Please select an observation, MSB or science program.");
            return false;
        }

        Vector<ErrorMessage> report = doValidation(spItem);

        // at the moment there is no difference in how errors and warnings are
        // handled.
        if ((ErrorMessage.getErrorCount() > 0)
                || (ErrorMessage.getWarningCount() > 0)) {
            new ReportBox(ErrorMessage.messagesToString(report.elements()),
                    reportBoxTitle);
            return false;
        } else {
            new ReportBox(spItem.type().getReadable() + " settings are valid.",
                    reportBoxTitle);
            return true;
        }
    }

    /**
     * Perform validation of the specified science program item.
     *
     * This method calls ErrorMessage.reset() before building the
     * list of errors.
     *
     * @return Vector of ErrorMessage objects
     */
    public static Vector<ErrorMessage> doValidation(SpItem spItem) {
        Vector<ErrorMessage> report = new Vector<ErrorMessage>();
        ErrorMessage.reset();

        if (OtCfg.telescopeUtil == null) {
            report.add(new ErrorMessage(
                    ErrorMessage.WARNING,
                    "",
                    "No Validation performed: Error getting TelescopeUtil."
                            + " Make sure a telescope cfg class is specified"
                            + " in the ot.cfg file."));
            return report;
        }

        SpValidation spValidation = OtCfg.telescopeUtil.getValidationTool();

        if (spValidation == null) {
            report.add(new ErrorMessage(ErrorMessage.WARNING, "",
                    "No validation performed:"
                    + " Could not find validation tool."));
            return report;
        }

        if (spItem instanceof SpMSB) {
            spValidation.checkMSB((SpMSB) spItem, report);
        } else {
            spValidation.checkSciProgram((SpProg) spItem, report);
        }

        // ADDED BY SDW...
        if (spItem instanceof SpProg || spItem instanceof SpLibrary) {
            spValidation.schemaValidate(spItem.toXML(), OtCfg.getSchemaURL(),
                    OtCfg.getSchemaLocation(), report);
        }

        return report;
    }

    /**
     * Display a file chooser so that the user can select the name of a file to
     * save the current science program to.
     */
    public void saveAs() {
        SpRootItem spItem = OtFileIO.saveAs(getItem(), _progInfo.file);

        if (spItem != null) {
            resetItem(spItem);
        }
    }

    /**
     * Revert to the last saved version of the current science program.
     */
    public void revertToSaved() {
        SpRootItem spItem;
        spItem = OtFileIO.revertToSaved(_progInfo.file);

        if (spItem != null) {
            resetItem(spItem);
        }
    }

    /**
     * Launch the help tool
     *
     * ORAC (UKIRT) Added item. AB 28-Sep-1999, M.Folger@roe.ac.uk 30/01/2001
     */
    public void launchHelp() {
        if (OT.getHelpLauncher() != null) {
            OT.getHelpLauncher().launch();
        } else {
            URL url = ObservingToolUtilities.resourceURL("help/othelp.hs",
                    "ot.cfgdir");
            OT.setHelpLauncher(new JHLauncher(url));
        }
    }

    /**
     * Exit the application.
     */
    public void exit() {
        if (closeApp()) {
            System.exit(0);
        }
    }

    /**
     * Close this science program and all related windows.
     */
    public void close() {
        closeApp();
    }

    /**
     * Pop up a dialog to allow the user to edit the science program title.
     */
    public void editItemTitle() {
        String s = DialogUtil.input(this, "New Title:");

        if (s != null && s.length() != 0) {
            _curItem.setTitleAttr(s);
        }
    }

    public void collapseMSBs() {
        JTree tree = _tw.getTree();

        for (int i = 1; i < tree.getRowCount(); i++) {
            tree.collapseRow(i);
        }
    }

    public void replicateSp() {
        JFileChooser chooser = new JFileChooser(OT.getOtUserDir());
        chooser.setDialogTitle("Select a catalog file");

        int option = chooser.showOpenDialog(null);

        if (option != JFileChooser.APPROVE_OPTION
                || chooser.getSelectedFile() == null) {
            return;
        }

        File file = chooser.getSelectedFile();

        if (file != null) {
            try {
                SpItem replicatedItem = SpClient.replicateSp(
                        (SpItem) getItem(), file);
                if (replicatedItem != null) {
                    OtWindow.create((SpRootItem) replicatedItem,
                            (FileInfo) null);
                    OtProps.setSaveShouldPrompt(true);
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * register to receive change events from this object whenever
     * a new science program is loaded.
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * Stop receiving change events from this object.
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /**
     * Notify any listeners that a new science program was loaded.
     */
    protected void fireChange(ChangeEvent e) {
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }

    /**
     * Local class used to store information about previously viewed science
     * programs.
     *
     * During a given session, the program is saved and can be redisplayed
     * if needed. If the application is restarted, the filename can be used
     * instead.
     */
    protected class OtHistoryItem extends AbstractAction {
        /** The origial filename */
        public String filename;

        /** The science program. */
        public SpRootItem spItem;

        /** The item's title */
        public String title;

        /**
         * Create an OT history item with the given title (for display),
         * filename, and SpItem.
         *
         * The SpItem is used during this session, otherwise the data is read
         * again from the file.
         */
        public OtHistoryItem(String title, String filename, SpRootItem spItem) {
            super(title);
            this.title = title;
            this.filename = filename;
            this.spItem = spItem;
        }

        /**
         * Load the SpItem if it exists, otherwise the file.
         */
        public void actionPerformed(ActionEvent evt) {
            if (spItem != null) {
                FileInfo fileInfo = null;

                if (filename != null) {
                    File file = new File(filename);
                    String dir = file.getParent();
                    String name = file.getName();
                    fileInfo = new FileInfo(dir, name, true);
                }

                open(spItem, fileInfo);

            } else if (filename != null) {
                File file = new File(filename);
                open(file);
            }
        }
    }

    public static void main(String[] args) {
        // Need one input, the name of an XML file
        if (args.length != 1) {
            System.out.println("No file name specified");
            System.exit(0);
        }

        try {
            OtCfg.init();
            FileReader rdr = new FileReader(args[0]);
            SpItem item = (new SpInputXML()).xmlToSpItem(rdr);
            Vector<ErrorMessage> report = OtWindow.doValidation(item);
            System.out.println(report);

        } catch (Exception e) {
            System.out.println("Error in validation");
            e.printStackTrace();
        }
    }

    /* Copied from SpTranslator so that we can delete it from the tree */

    /**
     * Find the parent Science Programme Observation associated with the
     * scope of the given item.
     *
     * This traverses the tree.
     *
     * @param spItem the SpItem defining the scope to search
     */
    public static SpObs findSpObs(SpItem spItem) {
        SpItem parent; // Parent of spItem

        if (spItem.type().equals(SpType.OBSERVATION)) {
            return (SpObs) spItem;
        }

        // Get the parent.
        parent = spItem.parent();

        // Either the item is an observation context, which is what we want,
        // or continue the search one level higher in the hierarchy.
        if (!(spItem.type().equals(SpType.OBSERVATION))) {
            if (parent == null) {
                return null;
            } else {
                return findSpObs(parent);
            }
        } else {
            return (SpObs) spItem;
        }
    }
}
