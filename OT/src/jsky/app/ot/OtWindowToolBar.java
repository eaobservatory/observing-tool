/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.ImageIcon;
import jsky.util.gui.GenericToolBar;


/** 
 * A tool bar for the main OT window.
 */
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

    /**
     * Create the toolbar for the given OT window
     */
    public OtWindowToolBar(OtWindow editor) {
	super(editor, false);
	setFloatable(false);
	this.editor = editor;
	addToolBarItems();
	//add(makeStatusButtonPanel());
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
     * Make the Save button, if it does not yet exists. Otherwise update the display
     * using the current options for displaying text or icons.
     *
     * @return the Save button
     */
    protected JButton makeSaveButton() {
	if (saveButton == null)
	    saveButton = makeButton("Save changes.", editor.getSaveAction(), false);
	
	updateButton(saveButton, "Save", new ImageIcon(ClassLoader.getSystemClassLoader().getResource("jsky/app/ot/images/disk.gif")));
	return saveButton;
    }

    /**
     * Make the Position Editor button, if it does not yet exists. Otherwise update the display
     * using the current options for displaying text or icons.
     *
     * @return the Position Editor button
     */
    protected JButton makePosEditorButton() {
	if (posEditorButton == null)
	    posEditorButton = makeButton("Show position editor.", editor.getPosEditorAction(), false);
	
	updateButton(posEditorButton, "Image", new ImageIcon(ClassLoader.getSystemClassLoader().getResource("jsky/app/ot/images/tpeTiny.gif")));
	return posEditorButton;
    }


    /**
     * Make the Cut button, if it does not yet exists. Otherwise update the display
     * using the current options for displaying text or icons.
     *
     * @return the Cut button
     */
    protected JButton makeCutButton() {
	if (cutButton == null)
	    cutButton = makeButton("Cut.", editor.getCutAction(), false);
	
	updateButton(cutButton, "Cut", new ImageIcon(ClassLoader.getSystemClassLoader().getResource("jsky/app/ot/images/cut.gif")));
	return cutButton;
    }

    /**
     * Make the Copy button, if it does not yet exists. Otherwise update the display
     * using the current options for displaying text or icons.
     *
     * @return the Copy button
     */
    protected JButton makeCopyButton() {
	if (copyButton == null)
	    copyButton = makeButton("Copy.", editor.getCopyAction(), false);
	
	updateButton(copyButton, "Copy", new ImageIcon(ClassLoader.getSystemClassLoader().getResource("jsky/app/ot/images/copy.gif")));
	return copyButton;
    }


    /**
     * Make the Paste button, if it does not yet exists. Otherwise update the display
     * using the current options for displaying text or icons.
     *
     * @return the Paste button
     */
    protected JButton makePasteButton() {
	if (pasteButton == null)
	    pasteButton = makeButton("Paste.", editor.getPasteAction(), false);
	
	updateButton(pasteButton, "Paste", new ImageIcon(ClassLoader.getSystemClassLoader().getResource("jsky/app/ot/images/paste.gif")));
	return pasteButton;
    }

    /**
     * Make the Validation Tool, if it does not yet exists. Otherwise update the display
     * using the current options for displaying text or icons.
     *
     * @return the Position Editor button
     */
    protected JButton makeValidationButton() {
	if (validationButton == null)
	    validationButton = makeButton("Validate.", editor.getValidationAction(), false);
	
	updateButton(validationButton, "Validation", new ImageIcon(ClassLoader.getSystemClassLoader().getResource("jsky/app/ot/images/validation.gif")));
	return validationButton;
    }

    /**
     * Make the Prioritize, if it does not yet exists. Otherwise update the display
     * using the current options for displaying text or icons.
     *
     * @return the Position Editor button
     */
    protected JButton makePrioritizeButton() {
	if (prioritizeButton == null)
	    prioritizeButton = makeButton("Prioritize", editor.getPrioritizeAction(), false);
	
	updateButton(prioritizeButton, "Prioritize", null);
	return prioritizeButton;
    }
    /**
     * Update the toolbar display using the current text/pictures options.
     * (redefined from the parent class).
     */
    public void update() {
	makeOpenButton();
	makeBackButton();
	makeForwardButton();
	//makeStopButton();

	makeCutButton();
	makeCopyButton();
	makePasteButton();

	makeSaveButton();
	makePosEditorButton();
	//makeStatusButtonPanel();
	makePrioritizeButton();
	makeValidationButton();
    }
}

