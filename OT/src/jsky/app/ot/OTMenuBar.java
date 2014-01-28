// Copyright (c) 2000 Association of Universities for Research in Astronomy, Inc. (AURA)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1) Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// 2) Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// 3) The names of AURA and its representatives may not be used to endorse or
//   promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
// FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
// THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
// $Id$
//
package jsky.app.ot ;

import java.awt.event.ActionListener ;
import java.awt.event.ActionEvent ;
import javax.swing.JMenuBar ;
import javax.swing.JMenu ;
import javax.swing.JMenuItem ;
import javax.swing.JComponent ;

/** 
 * Implements the menubar for the OT application class. 
 */
@SuppressWarnings( "serial" )
public class OTMenuBar extends JMenuBar implements ActionListener
{
	/** Target class */
	protected OT ot ;

	/** Handle for the File menu */
	protected JMenu fileMenu ;

	/** Handle for the Observing Database menu */
	protected JMenu observingDatabaseMenu ;

	/** Handle for the Help menu */
	protected JMenu helpMenu ;

	private static final String NEW_PROGRAM = "New Program" ;
	private static final String NEW_LIBRARY = "New Library" ;
	private static final String OPEN = "Open " ;
	private static final String LIBRARY = " library" ;
	private static final String PREFERENCES = "Preferences" ;
	private static final String EXIT = "Exit" ;
	private static final String FETCH_PROGRAM = "Fetch Program" ;
	private static final String NEWS = "News ..." ;
	private static final String ABOUT = "About ..." ;
	private static final String OT_HELP = "OT Help ..." ;

	/**
	 * Create the menubar for the given JSkyCat instance
	 */
	public OTMenuBar( OT ot )
	{
		super() ;
		this.ot = ot ;
		add( createFileMenu() ) ;
		add( createObservingDatabaseMenu() ) ;
		add( createHelpMenu() ) ;
	}

	/**
	 * Create the File menu. 
	 */
	protected JMenu createFileMenu()
	{
		fileMenu = new JMenu( "File" ) ;
		fileMenu.add( createMenuItem( NEW_PROGRAM ) ) ;
		fileMenu.add( createMenuItem( NEW_LIBRARY ) ) ;
		fileMenu.add( createMenuItem( OPEN ) ) ;

		JMenuItem[] instLibraryMenuItems = createFileOpenInstLibraryMenuItems() ;
		for( int i = 0 ; i < instLibraryMenuItems.length ; i++ )
		{
			if( instLibraryMenuItems[ i ] == null )
			{
				fileMenu.addSeparator() ;
				continue ;
			}
			fileMenu.add( instLibraryMenuItems[ i ] ) ;
		}
		fileMenu.addSeparator() ;
		fileMenu.add( createMenuItem( PREFERENCES ) ) ;
		fileMenu.addSeparator() ;
		fileMenu.add( createMenuItem( EXIT ) ) ;
		return fileMenu ;
	}

	/**
	 * Create the Observing menu. 
	 */
	protected JMenu createObservingDatabaseMenu()
	{
		observingDatabaseMenu = new JMenu( "Observing Database" ) ;
		observingDatabaseMenu.add( createMenuItem( FETCH_PROGRAM ) ) ;
		return observingDatabaseMenu ;
	}

	protected JMenu createHelpMenu()
	{
		helpMenu = new JMenu( "Help" ) ;
		helpMenu.add( createMenuItem( NEWS ) ) ;
		helpMenu.add( createMenuItem( ABOUT ) ) ;
		helpMenu.add( createMenuItem( OT_HELP ) ) ;

		helpMenu.setAlignmentX( JComponent.RIGHT_ALIGNMENT ) ;

		return helpMenu ;
	}

	/**
	 * Create the File => "New Program" menu item
	 */
	protected JMenuItem createMenuItem( String itemName )
	{
		JMenuItem menuItem = new JMenuItem( itemName ) ;
		menuItem.addActionListener( this ) ;
		return menuItem ;
	}

	// from old ATC OT start
	/**
	 * Create the File => "Open <instrument> Library" menu items
	 */
	protected JMenuItem[] createFileOpenInstLibraryMenuItems()
	{
		String[] libs = OtCfg.getLibraries() ;

		JMenuItem[] menuItems = new JMenuItem[ libs.length ] ;

		// CHanged for ORAC by AB, 1-Aug-00. Allow multiple libraries.
		for( int i = 0 ; i < libs.length ; i++ )
		{
			final String libname = libs[ i ] ;
			if( libname.equals( "null" ) )
			{
				menuItems[ i ] = null ;
				continue ;
			}
			menuItems[ i ] = new JMenuItem( OPEN + libs[ i ] + LIBRARY ) ;
			menuItems[ i ].addActionListener( this ) ;
		}

		return menuItems ;
	}

	public void actionPerformed( ActionEvent ae )
	{
		String actionCommand = ae.getActionCommand() ;
		if( OT_HELP.equals( actionCommand ) )
			OT.launchHelp() ;
		else if( ABOUT.equals( actionCommand ) )
			OT.showSplashScreen() ;
		else if( NEWS.equals( actionCommand ) )
			OT.showNews() ;
		else if( FETCH_PROGRAM.equals( actionCommand ) )
			OT.fetchProgram() ;
		else if( EXIT.equals( actionCommand ) )
			OT.exit() ;
		else if( PREFERENCES.equals( actionCommand ) )
			OT.preferences() ;
		else if( actionCommand.startsWith( OPEN ) && actionCommand.endsWith( LIBRARY ) )
			ot.openLibrary( actionCommand.substring( OPEN.length() , actionCommand.length() - LIBRARY.length() ) + ".xml" ) ;
		else if( actionCommand.startsWith( OPEN ) && !actionCommand.endsWith( LIBRARY ) )
			OT.open() ;
		else if( NEW_LIBRARY.equals( actionCommand ) )
			OT.newLibrary() ;
		else if( NEW_PROGRAM.endsWith( actionCommand ) )
			OT.newProgram() ;
	}
}
