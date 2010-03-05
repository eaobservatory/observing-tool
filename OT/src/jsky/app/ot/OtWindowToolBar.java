/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot ;

import gemini.util.ObservingToolUtilities;

import javax.swing.JButton ;
import javax.swing.ImageIcon ;
import jsky.util.gui.GenericToolBar ;

/** 
 * A tool bar for the main OT window.
 */
@SuppressWarnings( "serial" )
public class OtWindowToolBar extends GenericToolBar
{
	/** The target science program editor */
	protected OtWindow editor ;

	// toolbar buttons
	protected JButton cutButton ;
	protected JButton copyButton ;
	protected JButton pasteButton ;
	protected JButton saveButton ;
	protected JButton posEditorButton ;
	protected JButton validationButton ;
	protected JButton prioritizeButton ;
	
	private String imgpath = "jsky/app/ot/images/" ; 

	/**
	 * Create the toolbar for the given OT window
	 */
	public OtWindowToolBar( OtWindow editor )
	{
		super( editor , false ) ;
		setFloatable( false ) ;
		this.editor = editor ;
		addToolBarItems() ;
	}

	/** 
	 * Add the items to the tool bar.
	 */
	protected void addToolBarItems()
	{
		super.addToolBarItems() ;

		addSeparator() ;
		addSeparator() ;

		add( makeCutButton() ) ;
		addSeparator() ;
		add( makeCopyButton() ) ;
		addSeparator() ;
		add( makePasteButton() ) ;

		addSeparator() ;
		addSeparator() ;

		add( makeSaveButton() ) ;
		addSeparator() ;
		add( makePosEditorButton() ) ;
		addSeparator() ;
		add( makePrioritizeButton() ) ;
		addSeparator() ;
		add( makeValidationButton() ) ;
	}

	/**
	 * Make the Save button, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the Save button
	 */
	protected JButton makeSaveButton()
	{
		if( saveButton == null )
			saveButton = makeButton( editor.getSaveAction() ) ;

		updateButton( saveButton , "Save" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "disk.gif" ) ) ) ;
		return saveButton ;
	}

	/**
	 * Make the Position Editor button, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the Position Editor button
	 */
	protected JButton makePosEditorButton()
	{
		if( posEditorButton == null )
			posEditorButton = makeButton( editor.getPosEditorAction() ) ;

		updateButton( posEditorButton , "Image" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "tpeTiny.gif" ) ) ) ;
		return posEditorButton ;
	}

	/**
	 * Make the Cut button, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the Cut button
	 */
	protected JButton makeCutButton()
	{
		if( cutButton == null )
			cutButton = makeButton( editor.getCutAction() ) ;

		updateButton( cutButton , "Cut" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "cut.gif" ) ) ) ;
		return cutButton ;
	}

	/**
	 * Make the Copy button, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the Copy button
	 */
	protected JButton makeCopyButton()
	{
		if( copyButton == null )
			copyButton = makeButton( editor.getCopyAction() ) ;

		updateButton( copyButton , "Copy" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "copy.gif" ) ) ) ;
		return copyButton ;
	}

	/**
	 * Make the Paste button, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the Paste button
	 */
	protected JButton makePasteButton()
	{
		if( pasteButton == null )
			pasteButton = makeButton( editor.getPasteAction() ) ;

		updateButton( pasteButton , "Paste" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "paste.gif" ) ) ) ;
		return pasteButton ;
	}

	/**
	 * Make the Validation Tool, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the Position Editor button
	 */
	protected JButton makeValidationButton()
	{
		if( validationButton == null )
			validationButton = makeButton( editor.getValidationAction() ) ;

		updateButton( validationButton , "Validation" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "validation.gif" ) ) ) ;
		return validationButton ;
	}

	/**
	 * Make the Prioritize, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the Position Editor button
	 */
	protected JButton makePrioritizeButton()
	{
		if( prioritizeButton == null )
			prioritizeButton = makeButton( editor.getPrioritizeAction() ) ;

		updateButton( prioritizeButton , "Prioritize" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "prioritise.gif" ) ) ) ;
		return prioritizeButton ;
	}

	protected JButton makeForwardButton()
	{
		JButton jButton = new JButton() ;
		jButton.setVisible( false ) ;
		return jButton ;
	}

	protected JButton makeBackButton()
	{
		JButton jButton = new JButton() ;
		jButton.setVisible( false ) ;
		return jButton ;
	}

	/**
	 * Update the toolbar display using the current text/pictures options.
	 * (redefined from the parent class).
	 */
	public void update()
	{
		makeOpenButton() ;
		makeBackButton() ;
		makeForwardButton() ;

		makeCutButton() ;
		makeCopyButton() ;
		makePasteButton() ;

		makeSaveButton() ;
		makePosEditorButton() ;
		makePrioritizeButton() ;
		makeValidationButton() ;
	}
}
