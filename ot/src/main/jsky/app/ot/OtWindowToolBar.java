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

import gemini.util.ObservingToolUtilities;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import jsky.util.gui.GenericToolBar;

/**
 * A tool bar for the main OT window.
 */
@SuppressWarnings("serial")
public class OtWindowToolBar extends GenericToolBar {
    /** The target science program editor */
    protected OtWindow editor;

    // toolbar buttons
    protected JButton cutButton;
    protected JButton copyButton;
    protected JButton pasteButton;
    protected JButton saveButton;
    protected JButton posEditorButton;
    protected JButton validationButton;
    protected JButton prioritizeButton;

    private String imgpath = "jsky/app/ot/images/";

    /**
     * Create the toolbar for the given OT window
     */
    public OtWindowToolBar(OtWindow editor) {
        super(editor, false);
        setFloatable(false);
        this.editor = editor;
        addToolBarItems();
    }

    /**
     * Add the items to the tool bar.
     */
    protected void addToolBarItems() {
        super.addToolBarItems();

        addSeparator();
        addSeparator();

        add(makeCutButton());
        addSeparator();
        add(makeCopyButton());
        addSeparator();
        add(makePasteButton());

        addSeparator();
        addSeparator();

        add(makeSaveButton());
        addSeparator();
        add(makePosEditorButton());
        addSeparator();
        add(makePrioritizeButton());
        addSeparator();
        add(makeValidationButton());
    }

    /**
     * Make the Save button, if it does not yet exists.
     *
     * Otherwise update the display using the current options for displaying
     * text or icons.
     *
     * @return the Save button
     */
    protected JButton makeSaveButton() {
        if (saveButton == null) {
            saveButton = makeButton(editor.getSaveAction());
        }

        updateButton(saveButton, "Save", new ImageIcon(
                ObservingToolUtilities.resourceURL(imgpath + "disk.gif")));

        return saveButton;
    }

    /**
     * Make the Position Editor button, if it does not yet exists.
     *
     * Otherwise update the display using the current options for displaying
     * text or icons.
     *
     * @return the Position Editor button
     */
    protected JButton makePosEditorButton() {
        if (posEditorButton == null) {
            posEditorButton = makeButton(editor.getPosEditorAction());
        }

        updateButton(posEditorButton, "Image", new ImageIcon(
                ObservingToolUtilities.resourceURL(imgpath + "tpeTiny.gif")));

        return posEditorButton;
    }

    /**
     * Make the Cut button, if it does not yet exists.
     *
     * Otherwise update the display using the current options for displaying
     * text or icons.
     *
     * @return the Cut button
     */
    protected JButton makeCutButton() {
        if (cutButton == null) {
            cutButton = makeButton(editor.getCutAction());
        }

        updateButton(cutButton, "Cut", new ImageIcon(
                ObservingToolUtilities.resourceURL(imgpath + "cut.gif")));

        return cutButton;
    }

    /**
     * Make the Copy button, if it does not yet exists.
     *
     * Otherwise update the display using the current options for displaying
     * text or icons.
     *
     * @return the Copy button
     */
    protected JButton makeCopyButton() {
        if (copyButton == null) {
            copyButton = makeButton(editor.getCopyAction());
        }

        updateButton(copyButton, "Copy", new ImageIcon(
                ObservingToolUtilities.resourceURL(imgpath + "copy.gif")));

        return copyButton;
    }

    /**
     * Make the Paste button, if it does not yet exists.
     *
     * Otherwise update the display using the current options for displaying
     * text or icons.
     *
     * @return the Paste button
     */
    protected JButton makePasteButton() {
        if (pasteButton == null) {
            pasteButton = makeButton(editor.getPasteAction());
        }

        updateButton(pasteButton, "Paste", new ImageIcon(
                ObservingToolUtilities.resourceURL(imgpath + "paste.gif")));

        return pasteButton;
    }

    /**
     * Make the Validation Tool, if it does not yet exists.
     *
     * Otherwise update the display using the current options for displaying
     * text or icons.
     *
     * @return the Position Editor button
     */
    protected JButton makeValidationButton() {
        if (validationButton == null) {
            validationButton = makeButton(editor.getValidationAction());
        }

        updateButton(validationButton, "Validation", new ImageIcon(
                ObservingToolUtilities.resourceURL(imgpath + "validation.gif")));

        return validationButton;
    }

    /**
     * Make the Prioritize, if it does not yet exists.
     *
     * Otherwise update the display using the current options for displaying
     * text or icons.
     *
     * @return the Position Editor button
     */
    protected JButton makePrioritizeButton() {
        if (prioritizeButton == null) {
            prioritizeButton = makeButton(editor.getPrioritizeAction());
        }

        updateButton(prioritizeButton, "Prioritize", new ImageIcon(
                ObservingToolUtilities.resourceURL(imgpath + "prioritise.gif")));

        return prioritizeButton;
    }

    protected JButton makeForwardButton() {
        JButton jButton = new JButton();
        jButton.setVisible(false);
        return jButton;
    }

    protected JButton makeBackButton() {
        JButton jButton = new JButton();
        jButton.setVisible(false);
        return jButton;
    }

    /**
     * Update the toolbar display using the current text/pictures options.
     *
     * (Redefined from the parent class.)
     */
    public void update() {
        makeOpenButton();
        makeBackButton();
        makeForwardButton();

        makeCutButton();
        makeCopyButton();
        makePasteButton();

        makeSaveButton();
        makePosEditorButton();
        makePrioritizeButton();
        makeValidationButton();
    }
}
