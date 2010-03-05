/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot ;

import java.awt.BorderLayout ;
import java.awt.Dimension ;
import java.awt.event.WindowEvent ;
import java.awt.event.WindowListener ;
import java.util.ArrayList ;
import javax.swing.JFrame ;
import javax.swing.JPanel ;
import ot.OtWasteBin ;

/** 
 * Provides a top level window and menubar for the OtWindow class.
 */
@SuppressWarnings( "serial" )
public class OtWindowFrame extends JFrame implements WindowListener
{
	/** main panel */
	protected OtWindow editor ;

	// These are used make new frames visible by putting them in different locations
	private static int openFrameCount = 0 ;
	private static final int xOffset = 30 , yOffset = 30 ;
	private static ArrayList<JFrame> openFrames = new ArrayList<JFrame>() ;

	/**
	 * Create a top level window containing a OtWindow panel.
	 */
	public OtWindowFrame( final OtWindow editor )
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
		editor.setPreferredSize( new Dimension( 800 , 600 ) ) ;
		openFrameCount++ ;
		setLocation( xOffset * openFrameCount , yOffset * openFrameCount ) ;

		pack() ;
		setDefaultCloseOperation( DO_NOTHING_ON_CLOSE ) ;
		addWindowListener( this ) ;
		setVisible( true ) ;

		openFrames.add( this ) ;
	}

	/** Return the main science program editor panel */
	public OtWindow getEditor()
	{
		return editor ;
	}

	public static ArrayList<JFrame> getWindowFrames()
	{
		return openFrames ;
	}

	public void windowActivated( WindowEvent e ){}

	public void windowClosed( WindowEvent e )
	{
		openFrameCount-- ;
		openFrames.remove( this ) ;
	}

	public void windowDeactivated( WindowEvent e ){}

	public void windowDeiconified( WindowEvent e ){}

	public void windowIconified( WindowEvent e ){}

	public void windowOpened( WindowEvent e ){}

	public void windowClosing( WindowEvent e )
	{
		openFrameCount-- ;
		openFrames.remove( this ) ;
		editor.close() ;
	}
}
