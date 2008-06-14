/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot ;

import java.awt.BorderLayout ;
import java.awt.Dimension ;
import java.beans.PropertyVetoException ;
import javax.swing.JInternalFrame ;
import javax.swing.JPanel ;
import javax.swing.event.InternalFrameAdapter ;
import javax.swing.event.InternalFrameEvent ;
import jsky.app.ot.util.CloseableApp ;
import ot.OtWasteBin ;

/** 
 * Provides a top level window and menubar for the OtWindow class.
 */
public class OtWindowInternalFrame extends JInternalFrame implements CloseableApp
{
	/** main panel */
	protected OtWindow editor ;

	// These are used make new frames visible by putting them in different locations
	private static int openFrameCount = 0 ;
	private static final int xOffset = 30 , yOffset = 30 ;

	/**
	 * Create a top level window containing a OtWindow panel.
	 */
	public OtWindowInternalFrame( OtWindow editor )
	{
		super( "Science Program Editor" ) ;
		setTitle( editor.getItem().getTitle() ) ;
		this.editor = editor ;
		editor.setParentFrame( this ) ;

		OtWindowToolBar toolbar = new OtWindowToolBar( editor ) ;
		add( "North" , toolbar ) ;
		JPanel panel = new JPanel() ;
		panel.setLayout( new BorderLayout() ) ;
		OtTreeToolBar treeToolbar = new OtTreeToolBar( editor ) ;
		JPanel toolbarAndWasteBinPanel = new JPanel( new BorderLayout() ) ;
		toolbarAndWasteBinPanel.add( "Center" , treeToolbar ) ;
		toolbarAndWasteBinPanel.add( "South" , new OtWasteBin() ) ;
		panel.add( "West" , toolbarAndWasteBinPanel ) ;
		panel.add( "Center" , editor ) ;
		add( "Center" , panel ) ;
		setJMenuBar( new OtWindowMenuBar( editor , toolbar , treeToolbar ) ) ;

		// set default window size
		editor.setPreferredSize( new Dimension( 650 , 500 ) ) ;
		openFrameCount++ ;
		setLocation( xOffset * openFrameCount , yOffset * openFrameCount ) ;

		pack() ;
		setVisible( true ) ;
		setResizable( true ) ;
		setIconifiable( true ) ;

		setClosable( true ) ;
		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE ) ;
		addInternalFrameListener( new InternalFrameAdapter()
		{
			public void internalFrameClosing( InternalFrameEvent e )
			{
				OtWindowInternalFrame.this.editor.close() ;
			}
		} ) ;

		try
		{
			setSelected( true ) ;
		}
		catch( PropertyVetoException e ){}
	}

	/** Return the main science program editor panel */
	public OtWindow getEditor()
	{
		return editor ;
	}

	/** Close the application, returning true if successful. */
	public boolean closeApp()
	{
		return editor.closeApp() ;
	}
}
