// Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;


/** 
 * Implements the menubar for the OT application class. 
 */
public class OTMenuBar extends JMenuBar {

    /** Target class */
    protected OT ot;

    /** Handle for the File menu */
    protected JMenu fileMenu;

    /** Handle for the Observing Database menu */
    protected JMenu observingDatabaseMenu;

    /** Handle for the Help menu */
    protected JMenu helpMenu;
    
    /**
     * Create the menubar for the given JSkyCat instance
     */
    public OTMenuBar(OT ot) {
	super();
	this.ot = ot;
	add(createFileMenu());
	add(createObservingDatabaseMenu());
	add(createHelpMenu());
    }

    /**
     * Create the File menu. 
     */
    protected JMenu createFileMenu() {
	fileMenu = new JMenu("File");
	fileMenu.add(createFileNewProgramMenuItem());
	fileMenu.add(createFileNewPlanMenuItem());
	fileMenu.add(createFileNewLibraryMenuItem());
	fileMenu.add(createFileOpenMenuItem());
	//fileMenu.add(createFileOpenStandardLibraryMenuItem());
	JMenuItem [] instLibraryMenuItems = createFileOpenInstLibraryMenuItems();
	for(int i = 0; i < instLibraryMenuItems.length; i++) {
	    if ( instLibraryMenuItems[i] == null ) {
		fileMenu.addSeparator();
		continue;
	    }
	    fileMenu.add(instLibraryMenuItems[i]);
	}  
	fileMenu.addSeparator();
	fileMenu.add(createFilePreferencesMenuItem());
	fileMenu.addSeparator();
	fileMenu.add(createFileExitMenuItem());
	return fileMenu;
    }

    /**
     * Create the Observing menu. 
     */
    protected JMenu createObservingDatabaseMenu() {
	observingDatabaseMenu = new JMenu("Observing Database");
	observingDatabaseMenu.add(createObservingDatabaseFetchProgramMenuItem());
	return observingDatabaseMenu;
    }

    protected JMenu createHelpMenu() {
	helpMenu = new JMenu("Help");	
	helpMenu.add(createHelpNewsMenuItem());
	helpMenu.add(createHelpAboutMenuItem());
	helpMenu.add(createHelpOtHelpMenuItem());

	helpMenu.setAlignmentX(JComponent.RIGHT_ALIGNMENT);

	return helpMenu;
    }

    /**
     * Create the File => "New Program" menu item
     */
    protected JMenuItem createFileNewProgramMenuItem() {
	JMenuItem menuItem = new JMenuItem("New Program");
        menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		ot.newProgram();
	    }
	});
	return menuItem;
    }

    /**
     * Create the File => "New Plan" menu item
     */
    protected JMenuItem createFileNewPlanMenuItem() {
	JMenuItem menuItem = new JMenuItem("New Plan");
        menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		ot.newPlan();
	    }
	});

	// MFO 23 May 2001: "New Plan" menu item disabled.
	menuItem.setEnabled(false);
	
	return menuItem;
    }

    /**
     * Create the File => "New Library" menu item
     */
    protected JMenuItem createFileNewLibraryMenuItem() {
	JMenuItem menuItem = new JMenuItem("New Library");
        menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		ot.newLibrary();
	    }
	});
	return menuItem;
    }

    /**
     * Create the File => "Open" menu item
     */
    protected JMenuItem createFileOpenMenuItem() {
	JMenuItem menuItem = new JMenuItem("Open");
        menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		ot.open();
	    }
	});
	return menuItem;
    }

// from old ATC OT start
    /**
     * Create the File => "Open <instrument> Library" menu items
     */
    protected JMenuItem [] createFileOpenInstLibraryMenuItems() {
        String[] libs = OtCfg.getLibraries();

	JMenuItem [] menuItems = new JMenuItem[libs.length];

	// CHanged for ORAC by AB, 1-Aug-00. Allow multiple libraries.
 	for (int i = 0; i<libs.length; i++) {
	    final String libname = libs[i];
	    if ( libname.equals("null") ) {
		menuItems[i] = null;
		continue;
	    }
 	    menuItems[i] = new JMenuItem("Open "+libs[i]+" library");
	    menuItems[i].addActionListener
	    (
		new ActionListener() 
                {
 			public void actionPerformed( ActionEvent e ) 
			{
				ot.openLibrary( libname+".xml" ) ;
 			}
 	    	}
	   );
 	 }
       
         return	menuItems;
    }

// from old ATC OT end

    // JSky / Gemini standars library code
    //
    // /**
    //  * Create the File => "Open Standard Library" menu item
    //  */
    //protected JMenuItem createFileOpenStandardLibraryMenuItem() {
    //	JMenuItem menuItem = new JMenuItem("Open Standard Library");
    //    menuItem.addActionListener(new ActionListener() {
    //	    public void actionPerformed(ActionEvent ae) {
    //		ot.openStandardLibrary();
    //	    }
    //	});
    //	return menuItem;
    //}

    /**
     * Create the File => "Preferences" menu item
     */
    protected JMenuItem createFilePreferencesMenuItem() {
	JMenuItem menuItem = new JMenuItem("Preferences");
        menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		// I suspect it should be possible to define OT.preferences() as
		// non-static method and use ot.preferences(). But it did
		// cause an java.lang.IncompatibleClassChangeError in the past.
		OT.preferences();
	    }
	});
	return menuItem;
    }

    /**
     * Create the File => "Exit" menu item
     */
    protected JMenuItem createFileExitMenuItem() {
	JMenuItem menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		// I suspect it should be possible to define OT.exit() as
		// non-static method and use ot.exit(). But it did
		// cause an java.lang.IncompatibleClassChangeError in the past.
		OT.exit();
	    }
	});
	return menuItem;
    }

    /**
     * Create the Observing Database => "Fetch Program" menu item
     */
    protected JMenuItem createObservingDatabaseFetchProgramMenuItem() {
	JMenuItem menuItem = new JMenuItem("Fetch Program");
        menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		ot.fetchProgram();
	    }
	});
	return menuItem;
    }

    /**
     * Create the Help => "News ..." menu item
     */
    protected JMenuItem createHelpNewsMenuItem() {
	JMenuItem menuItem = new JMenuItem("News ...");
        menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		ot.showNews();
	    }
	});
	return menuItem;
    }

    /**
     * Create the Help => "About ..." menu item
     */
    protected JMenuItem createHelpAboutMenuItem() {
	JMenuItem menuItem = new JMenuItem("About ...");
        menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		ot.showSplashScreen();
	    }
	});
	return menuItem;
    }

    /**
     * Create the Help => "OT Help ..." menu item
     */
    protected JMenuItem createHelpOtHelpMenuItem() {
	JMenuItem menuItem = new JMenuItem("OT Help ...");
        menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		ot.launchHelp();
	    }
	});
	return menuItem;
    }
}
