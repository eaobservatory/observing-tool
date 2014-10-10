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
