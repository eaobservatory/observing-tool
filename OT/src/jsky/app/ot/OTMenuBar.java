// Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot ;

import javax.swing.JMenuBar ;
import javax.swing.JMenu ;
import javax.swing.JMenuItem ;
import javax.swing.JComponent ;

/** 
 * Implements the menubar for the OT application class. 
 */
public class OTMenuBar extends JMenuBar
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

	private static MenuHandler menuHandler = null ;

	/**
	 * Create the menubar for the given JSkyCat instance
	 */
	public OTMenuBar( OT ot )
	{
		super() ;
		this.ot = ot ;
		menuHandler = new OTMenuBarHandler() ;
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
		fileMenu.add( createFileNewProgramMenuItem() ) ;
		fileMenu.add( createFileNewLibraryMenuItem() ) ;
		fileMenu.add( createFileOpenMenuItem() ) ;

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
		fileMenu.add( createFilePreferencesMenuItem() ) ;
		fileMenu.addSeparator() ;
		fileMenu.add( createFileExitMenuItem() ) ;
		fileMenu.addMenuListener( menuHandler ) ;
		return fileMenu ;
	}

	/**
	 * Create the Observing menu. 
	 */
	protected JMenu createObservingDatabaseMenu()
	{
		observingDatabaseMenu = new JMenu( "Observing Database" ) ;
		observingDatabaseMenu.add( createObservingDatabaseFetchProgramMenuItem() ) ;
		observingDatabaseMenu.addMenuListener( menuHandler ) ;
		return observingDatabaseMenu ;
	}

	protected JMenu createHelpMenu()
	{
		helpMenu = new JMenu( "Help" ) ;
		helpMenu.add( createHelpNewsMenuItem() ) ;
		helpMenu.add( createHelpAboutMenuItem() ) ;
		helpMenu.add( createHelpOtHelpMenuItem() ) ;
		helpMenu.addMenuListener( menuHandler ) ;
		helpMenu.setAlignmentX( JComponent.RIGHT_ALIGNMENT ) ;

		return helpMenu ;
	}

	/**
	 * Create the File => "New Program" menu item
	 */
	protected JMenuItem createFileNewProgramMenuItem()
	{
		JMenuItem menuItem = new JMenuItem( NEW_PROGRAM ) ;
		menuItem.addMouseListener( menuHandler ) ;
		return menuItem ;
	}

	/**
	 * Create the File => "New Library" menu item
	 */
	protected JMenuItem createFileNewLibraryMenuItem()
	{
		JMenuItem menuItem = new JMenuItem( NEW_LIBRARY ) ;
		menuItem.addMouseListener( menuHandler ) ;
		return menuItem ;
	}

	/**
	 * Create the File => "Open" menu item
	 */
	protected JMenuItem createFileOpenMenuItem()
	{
		JMenuItem menuItem = new JMenuItem( OPEN ) ;
		menuItem.addMouseListener( menuHandler ) ;
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
			menuItems[ i ].addMouseListener( menuHandler ) ;
		}

		return menuItems ;
	}

	/**
	 * Create the File => "Preferences" menu item
	 */
	protected JMenuItem createFilePreferencesMenuItem()
	{
		JMenuItem menuItem = new JMenuItem( PREFERENCES ) ;
		menuItem.addMouseListener( menuHandler ) ;
		return menuItem ;
	}

	/**
	 * Create the File => "Exit" menu item
	 */
	protected JMenuItem createFileExitMenuItem()
	{
		JMenuItem menuItem = new JMenuItem( EXIT ) ;
		menuItem.addMouseListener( menuHandler ) ;
		return menuItem ;
	}

	/**
	 * Create the Observing Database => "Fetch Program" menu item
	 */
	protected JMenuItem createObservingDatabaseFetchProgramMenuItem()
	{
		JMenuItem menuItem = new JMenuItem( FETCH_PROGRAM ) ;
		menuItem.addMouseListener( menuHandler ) ;
		return menuItem ;
	}

	/**
	 * Create the Help => "News ..." menu item
	 */
	protected JMenuItem createHelpNewsMenuItem()
	{
		JMenuItem menuItem = new JMenuItem( NEWS ) ;
		menuItem.addMouseListener( menuHandler ) ;
		return menuItem ;
	}

	/**
	 * Create the Help => "About ..." menu item
	 */
	protected JMenuItem createHelpAboutMenuItem()
	{
		JMenuItem menuItem = new JMenuItem( ABOUT ) ;
		menuItem.addMouseListener( menuHandler ) ;
		return menuItem ;
	}

	/**
	 * Create the Help => "OT Help ..." menu item
	 */
	protected JMenuItem createHelpOtHelpMenuItem()
	{
		JMenuItem menuItem = new JMenuItem( OT_HELP ) ;
		menuItem.addMouseListener( menuHandler ) ;
		return menuItem ;
	}

	class OTMenuBarHandler extends MenuHandler
	{
		public void actionToPerform()
		{
			String actionCommand = selectionStack.remove( 0 ) ;
			if( OTMenuBar.OT_HELP.equals( actionCommand ) )
				OT.launchHelp() ;
			else if( OTMenuBar.ABOUT.equals( actionCommand ) )
				OT.showSplashScreen() ;
			else if( OTMenuBar.NEWS.equals( actionCommand ) )
				OT.showNews() ;
			else if( OTMenuBar.FETCH_PROGRAM.equals( actionCommand ) )
				OT.fetchProgram() ;
			else if( OTMenuBar.EXIT.equals( actionCommand ) )
				OT.exit() ;
			else if( OTMenuBar.PREFERENCES.equals( actionCommand ) )
				OT.preferences() ;
			else if( actionCommand.startsWith( OTMenuBar.OPEN ) && actionCommand.endsWith( OTMenuBar.LIBRARY ) )
				ot.openLibrary( actionCommand.substring( OTMenuBar.OPEN.length() , actionCommand.length() - OTMenuBar.LIBRARY.length() ) + ".xml" ) ;
			else if( actionCommand.startsWith( OTMenuBar.OPEN ) && !actionCommand.endsWith( OTMenuBar.LIBRARY ) )
				OT.open() ;
			else if( OTMenuBar.NEW_LIBRARY.equals( actionCommand ) )
				OT.newLibrary() ;
			else if( OTMenuBar.NEW_PROGRAM.endsWith( actionCommand ) )
				OT.newProgram() ;
		}
	}
}
