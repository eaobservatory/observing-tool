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
 *
 * $Id$
 */

package jsky.app.ot ;

import gemini.util.ObservingToolUtilities ;

import javax.swing.JButton ;
import javax.swing.ImageIcon ;
import jsky.util.gui.GenericToolBar ;

/** 
 * A tool bar for the OT tree window.
 */
@SuppressWarnings( "serial" )
public class OtTreeToolBar extends GenericToolBar
{
	/** The target science program editor */
	protected OtWindow editor ;

	// toolbar buttons
	protected JButton obsFolderButton ;
	protected JButton obsGroupButton ;
	protected JButton observationButton ;
	protected JButton componentMenuButton ;
	protected JButton noteButton ;
	protected JButton libFolderButton ;
	protected JButton iterCompMenuButton ;
	protected JButton iterObsMenuButton ;

	// OMP buttons
	// added by MFO (06 July 2001)
	protected JButton msbFolderButton ;
	protected JButton andFolderButton ;
	protected JButton orFolderButton ;
	protected JButton surveyButton ;

	protected String imgpath = "jsky/app/ot/images/" ;

	/**
	 * Create a toolbar with tree related actions for the given OT window.
	 */
	public OtTreeToolBar( OtWindow editor )
	{
		super( editor , false , VERTICAL ) ;
		setFloatable( false ) ;
		this.editor = editor ;
		showPictures = true ;
		showText = true ;
		addToolBarItems() ;
	}

	/** 
	 * Add the items to the tool bar.
	 */
	protected void addToolBarItems()
	{
		// added by MFO (06 July 2001)
		add( makeOrFolderButton() ) ;
		addSeparator() ;
		add( makeAndFolderButton() ) ;
		addSeparator() ;
		add( makeSurveyButton() ) ;
		addSeparator() ;
		add( makeMsbFolderButton() ) ;

		addSeparator() ;
		add( makeObservationButton() ) ;
		addSeparator() ;
		add( makeNoteButton() ) ;
		addSeparator() ;
		add( makeLibFolderButton() ) ;
		addSeparator() ;

		add( makeComponentMenuButton() ) ;
		addSeparator() ;
		add( makeIterCompMenuButton() ) ;
		addSeparator() ;
		add( makeIterObsMenuButton() ) ;
	}

	/**
	 * Make the Observation Folder button, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the ObsFolder button
	 */
	protected JButton makeObsFolderButton()
	{
		if( obsFolderButton == null )
			obsFolderButton = makeButton( editor.getObsFolderAction() ) ;

		updateButton( obsFolderButton , "Folder" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "obsFolder.gif" ) ) ) ;
		return obsFolderButton ;
	}

	/**
	 * Make the Observation Group button, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the ObsGroup button
	 */
	protected JButton makeObsGroupButton()
	{
		if( obsGroupButton == null )
			obsGroupButton = makeButton( editor.getObsGroupAction() ) ;

		updateButton( obsGroupButton , "Group" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "obsGroup.gif" ) ) ) ;
		return obsGroupButton ;
	}

	/**
	 * Make the Observation button, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the Observation button
	 */
	protected JButton makeObservationButton()
	{
		if( observationButton == null )
			observationButton = makeButton( editor.getObservationAction() ) ;

		updateButton( observationButton , "Observation" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "observation.gif" ) ) ) ;
		return observationButton ;
	}

	/**
	 * Make the Observation Component menu button, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the Observation Component menu button
	 */
	protected JButton makeComponentMenuButton()
	{
		if( componentMenuButton == null )
			componentMenuButton = makeMenuButton( "Create an observation component." , new OtCompPopupMenu( editor.getTreeWidget() ) ) ;

		updateButton( componentMenuButton , "Component" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "component.gif" ) ) ) ;
		return componentMenuButton ;
	}

	/**
	 * Make the Note button, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the Note button
	 */
	protected JButton makeNoteButton()
	{
		if( noteButton == null )
			noteButton = makeButton( editor.getNoteAction() ) ;

		updateButton( noteButton , "Note" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "note-tiny.gif" ) ) ) ;
		return noteButton ;
	}

	/**
	 * Make the LibFolder button, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the LibFolder button
	 */
	protected JButton makeLibFolderButton()
	{
		if( libFolderButton == null )
			libFolderButton = makeButton( editor.getLibFolderAction() ) ;

		updateButton( libFolderButton , "Library" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "libFolder.gif" ) ) ) ;
		return libFolderButton ;
	}

	/**
	 * Make the Iterator Component menu button, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the Iterator Component menu button
	 */
	protected JButton makeIterCompMenuButton()
	{
		if( iterCompMenuButton == null )
			iterCompMenuButton = makeMenuButton( "Create an iterator component." , new OtIterCompPopupMenu( editor.getTreeWidget() ) ) ;

		updateButton( iterCompMenuButton , "Iterator" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "iterComp.gif" ) ) ) ;
		return iterCompMenuButton ;
	}

	/**
	 * Make the Observe Iterator menu button, if it does not yet exists. Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the Observe Iterator menu button.
	 */
	protected JButton makeIterObsMenuButton()
	{
		if( iterObsMenuButton == null )
			iterObsMenuButton = makeMenuButton( "Create an observation iterator." , new OtIterObsPopupMenu( editor.getTreeWidget() ) ) ;

		updateButton( iterObsMenuButton , "Observe" , new ImageIcon( ObservingToolUtilities.resourceURL( imgpath + "iterObs.gif" ) ) ) ;
		return iterObsMenuButton ;
	}

	// The following three functions were added fir the OMP project.
	// (MFO, 09 July 2001)

	/**
	 * Make the MSB Folder button (OMP project), if it does not yet exists.
	 * Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the MSB Folder button
	 */
	protected JButton makeMsbFolderButton()
	{
		if( msbFolderButton == null )
			msbFolderButton = makeButton( editor.getMsbFolderAction() ) ;

		updateButton( msbFolderButton , "MSB Folder" , new ImageIcon( ObservingToolUtilities.resourceURL( "ot/images/msbFolder.gif" ) ) ) ;
		return msbFolderButton ;
	}

	/**
	 * Make the AND Folder button (OMP project), if it does not yet exists.
	 * Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the AND Folder button
	 */
	protected JButton makeAndFolderButton()
	{
		if( andFolderButton == null )
			andFolderButton = makeButton( editor.getAndFolderAction() ) ;

		updateButton( andFolderButton , "AND Folder" , new ImageIcon( ObservingToolUtilities.resourceURL( "ot/images/andFolder.gif" ) ) ) ;
		return andFolderButton ;
	}

	/**
	 * Make the OR Folder button (OMP project), if it does not yet exists.
	 * Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the OR Folder button
	 */
	protected JButton makeOrFolderButton()
	{
		if( orFolderButton == null )
			orFolderButton = makeButton( editor.getOrFolderAction() ) ;

		updateButton( orFolderButton , "OR Folder" , new ImageIcon( ObservingToolUtilities.resourceURL( "ot/images/orFolder.gif" ) ) ) ;
		return orFolderButton ;
	}

	/**
	 * Make the Survey Folder button (OMP project), if it does not yet exists.
	 * Otherwise update the display
	 * using the current options for displaying text or icons.
	 *
	 * @return the OR Folder button
	 */
	protected JButton makeSurveyButton()
	{
		if( surveyButton == null )
			surveyButton = makeButton( editor.getSurveyFolderAction() ) ;

		updateButton( surveyButton , "Survey Container" , new ImageIcon( ObservingToolUtilities.resourceURL( "ot/images/surveyContainer.gif" ) ) ) ;
		return surveyButton ;
	}

	/**
	 * Update the toolbar display using the current text/pictures options.
	 * (redefined from the parent class).
	 */
	public void update()
	{
		// added by MFO (06 July 2001)
		makeMsbFolderButton() ;
		makeAndFolderButton() ;
		makeOrFolderButton() ;
		makeSurveyButton();

		makeObservationButton() ;
		makeComponentMenuButton() ;
		makeNoteButton() ;
		makeLibFolderButton() ;
		makeIterCompMenuButton() ;
		makeIterObsMenuButton() ;
	}
}
