/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import jsky.util.Preferences;

/** 
 * Implements a menubar for an OtWindow window. 
 *
 * @version $Revision$
 * @author Allan Brighton
 */
public class OtWindowMenuBar extends JMenuBar {

    /** The target science program editor */
    protected OtWindow editor;

    /** Handle for the File menu */
    protected JMenu fileMenu;

    /** Handle for the Edit menu */
    protected JMenu editMenu;

    /** Handle for the View menu */
    protected JMenu viewMenu;

    /** Handle for the Go menu */
    protected JMenu goMenu;

    /** Handle for the Database menu */
    protected JMenu databaseMenu;

    /** Handle for the Database menu */
    protected JMenu helpMenu;

    // menu items that can be enabled/disabled at runtime
    protected JMenuItem	fileRevertMenuItem;
    protected JMenuItem	filePhase1MenuItem;
    protected JMenuItem	databaseModeItem;

    /** The main OT window toolbar */
    protected OtWindowToolBar mainToolBar;

    /** The OT toolbar with tree related items. */
    protected OtTreeToolBar treeToolBar;


    /**
     * Create the menubar for the given OtWindow.
     *
     * @param editor the target science program editor
     * @param toolBar the OT toolbar
     */
    public OtWindowMenuBar(final OtWindow editor, OtWindowToolBar mainToolBar, OtTreeToolBar treeToolBar) {
	super();

	this.editor = editor;
	this.mainToolBar = mainToolBar;
	this.treeToolBar = treeToolBar;

	add(fileMenu = createFileMenu());
	add(editMenu = createEditMenu());
	add(viewMenu = createViewMenu());
	add(goMenu = createGoMenu(null));

	// keep the Go history menu up to date
	editor.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
		    goMenu.removeAll();
		    createGoMenu(goMenu);
		}
	    });

	add(databaseMenu = createDatabaseMenu());

	add(helpMenu = createHelpMenu());
	
	// disabled the database menu if a library program is loaded
	editor.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
		    databaseMenu.setEnabled(! ((OtProgWindow)editor).isLibrary());
		}
	    });
    }

    /**
     * Create the File menu. 
     */
    protected JMenu createFileMenu() {
	JMenu menu = new JMenu("File");
	menu.add(createFileNewProgramMenuItem());
	menu.add(createFileOpenMenuItem());
	menu.add(createFileOpenNewWindowMenuItem());
	menu.addSeparator();
	menu.add(editor.getSaveAction());
	menu.add(createFileSaveObsAsSequenceMenuItem());
	menu.add(createFileSaveAsMenuItem());
	menu.add(fileRevertMenuItem = createFileRevertToSavedMenuItem());
	menu.addSeparator();

	menu.add(filePhase1MenuItem = createFileShowPhase1InfoMenuItem());
	menu.addSeparator();

	menu.add(createFileCloseMenuItem());

	// check if using internal frames before adding exit item
	//MFO//JDesktopPane desktop = OT.getDesktop();
	//MFO//if (desktop == null) 
	//MFO//    menu.add(createFileExitMenuItem());
	    
	// disable/enable the "Save" and "Revert" choices depending
	// upon whether the current program has been edited or saved
	menu.addMenuListener(new MenuListener() {
		public void menuSelected(MenuEvent e) {
		    editor.getSaveAction().setEnabled(editor.isEdited());
		    fileRevertMenuItem.setEnabled(editor.progHasBeenSaved());
		    filePhase1MenuItem.setEnabled(editor.isPhase1InfoAvailable());
		}
		public void menuDeselected(MenuEvent e) {}
		public void menuCanceled(MenuEvent e) {} 
	    });

	return menu;
    }


    /**
     * Create the File => "New Program" menu item
     */
    protected JMenuItem createFileNewProgramMenuItem() {
	JMenuItem menuItem = new JMenuItem("New Program");
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    editor.newProgram();
		}
	    });
	return menuItem;
    }


    /**
     * Create the File => "Open" menu item
     */
    protected JMenuItem createFileOpenMenuItem() {
	JMenuItem menuItem = new JMenuItem("Open...");
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    editor.open(false);
		}
	    });
	return menuItem;
    }

    /**
     * Create the File => "Open in New Window" menu item
     */
    protected JMenuItem createFileOpenNewWindowMenuItem() {
	JMenuItem menuItem = new JMenuItem("Open in New Window...");
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    editor.open(true);
		}
	    });
	return menuItem;
    }


    /**
     * Create the File => "Save..." menu item
     */
    protected JMenuItem createFileSaveMenuItem() {
	JMenuItem menuItem = new JMenuItem("Save...");
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    editor.save();
		}
	    });
	return menuItem;
    }

    /**
     * Create the File => "Save Observation As Sequence" menu item.
     * MFO
     */
    protected JMenuItem createFileSaveObsAsSequenceMenuItem() {
	JMenuItem menuItem = new JMenuItem("Save Observation As Sequence");
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    editor.doSaveSequence();
		}
	    });
	return menuItem;
    }


    /**
     * Create the File => "Save As" menu item
     */
    protected JMenuItem createFileSaveAsMenuItem() {
	JMenuItem menuItem = new JMenuItem("Save As...");
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    editor.saveAs();
		}
	    });
	return menuItem;
    }

    /**
     * Create the File => "Revert to Saved..." menu item
     */
    protected JMenuItem createFileRevertToSavedMenuItem() {
	JMenuItem menuItem = new JMenuItem("Revert to Saved...");
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    editor.revertToSaved();
		}
	    });
	return menuItem;
    }


    /**
     * Create the File => "Show Phase1 Info" menu item
     */
    protected JMenuItem createFileShowPhase1InfoMenuItem() {
	JMenuItem menuItem = new JMenuItem("Show Phase1 Info");

	// If this site supports Phase 1 Information, then enable the menu item for it
	menuItem.setEnabled(OtCfg.phase1Available());

        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    editor.showPhase1();
		}
	    });
	return menuItem;
    }


    /**
     * Create the File => Exit menu item
     */
    protected JMenuItem createFileExitMenuItem() {
	JMenuItem menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    editor.exit();
		}
	    });
	return menuItem;
    }


    /**
     * Create the File => Close menu item
     */
    protected JMenuItem createFileCloseMenuItem() {
	JMenuItem menuItem = new JMenuItem("Close");
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    editor.close();
		}
	    });
	return menuItem;
    }

    /**
     * Create the View menu. 
     */
    protected JMenu createViewMenu() {
	JMenu menu = new JMenu("View");
	menu.add(createViewMainToolBarMenuItem());
	menu.add(createViewTreeToolBarMenuItem());
	menu.addSeparator();
	menu.add(createViewShowMainToolBarAsMenu());
	menu.add(createViewShowTreeToolBarAsMenu());
	menu.addSeparator();
	menu.add(makeMenuItem(editor.getPosEditorAction(), "Show the Position Editor"));
	menu.addSeparator();
	menu.add(editor.getStopAction());
	
	// Only add Look and Feel item if not using internal frames 
	// (otherwise its in the main Image menu)
	//if (editor.getRootComponent() instanceof JFrame) {
	//    menu.addSeparator();
	//    menu.add(new LookAndFeelMenu());
	//}

	return menu;
    }


    /**
     * Create the View => "Main Toolbar" menu item
     */
    protected JCheckBoxMenuItem createViewMainToolBarMenuItem() {
	JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem("Main Toolbar");

	// name used to store setting in user preferences
	final String prefName = getClass().getName() + ".ShowMainToolBar";

	menuItem.addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
		    JCheckBoxMenuItem rb = (JCheckBoxMenuItem)e.getSource();
		    mainToolBar.setVisible(rb.getState());
		    if (rb.getState())
			Preferences.set(prefName, "true");
		    else
			Preferences.set(prefName, "false");
		}
	    });

	// check for a previous preference setting
	String pref = Preferences.get(prefName);
	if (pref != null) 
	    menuItem.setState(pref.equals("true"));
	else 
	    menuItem.setState(true);

	return menuItem;
    }

    /**
     * Create the View => "Tree Toolbar" menu item
     */
    protected JCheckBoxMenuItem createViewTreeToolBarMenuItem() {
	JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem("Tree Toolbar");

	// name used to store setting in user preferences
	final String prefName = getClass().getName() + ".ShowTreeToolBar";

	menuItem.addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
		    JCheckBoxMenuItem rb = (JCheckBoxMenuItem)e.getSource();
		    treeToolBar.setVisible(rb.getState());
		    if (rb.getState())
			Preferences.set(prefName, "true");
		    else
			Preferences.set(prefName, "false");
		}
	    });

	// check for a previous preference setting
	String pref = Preferences.get(prefName);
	if (pref != null) 
	    menuItem.setState(pref.equals("true"));
	else 
	    menuItem.setState(true);

	return menuItem;
    }

    /**
     * Create the View => "Show Main Toolbar As" menu
     */
    protected JMenu createViewShowMainToolBarAsMenu() {
	JMenu menu = new JMenu("Show Main Toolbar As");

	JRadioButtonMenuItem b1 = new JRadioButtonMenuItem("Pictures and Text");
	JRadioButtonMenuItem b2 = new JRadioButtonMenuItem("Pictures Only");
	JRadioButtonMenuItem b3 = new JRadioButtonMenuItem("Text Only");

	b3.setSelected(true);
	mainToolBar.setShowPictures(false);
	mainToolBar.setShowText(true);

	menu.add(b1);
	menu.add(b2);
	menu.add(b3);

	ButtonGroup group = new ButtonGroup();
	group.add(b1);
	group.add(b2);
	group.add(b3);

	// name used to store setting in user preferences
	final String prefName = getClass().getName() + ".ShowMainToolBarAs";

	ItemListener itemListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
		    JRadioButtonMenuItem rb = (JRadioButtonMenuItem)e.getSource();
		    if(rb.isSelected()) {
			if (rb.getText().equals("Pictures and Text")) {
			    mainToolBar.setShowPictures(true);
			    mainToolBar.setShowText(true);
			    Preferences.set(prefName, "1");
			}
			else if (rb.getText().equals("Pictures Only")) {
			    mainToolBar.setShowPictures(true);
			    mainToolBar.setShowText(false);
			    Preferences.set(prefName, "2");
			}
			else if (rb.getText().equals("Text Only")) {
			    mainToolBar.setShowPictures(false);
			    mainToolBar.setShowText(true);
			    Preferences.set(prefName, "3");
			}
		    }
		}};
	
	b1.addItemListener(itemListener);
	b2.addItemListener(itemListener);
	b3.addItemListener(itemListener);

	// check for a previous preference setting
	String pref = Preferences.get(prefName);
	if (pref != null) {
	    JRadioButtonMenuItem[] ar = new JRadioButtonMenuItem[]{null, b1, b2, b3};
	    try {
		ar[Integer.parseInt(pref)].setSelected(true);
	    }
	    catch(Exception e) {}
	}

	return menu;
    }


    /**
     * Create the View => "Show Tree Toolbar As" menu
     */
    protected JMenu createViewShowTreeToolBarAsMenu() {
	JMenu menu = new JMenu("Show Tree Toolbar As");

	JRadioButtonMenuItem b1 = new JRadioButtonMenuItem("Pictures and Text");
	JRadioButtonMenuItem b2 = new JRadioButtonMenuItem("Pictures Only");
	JRadioButtonMenuItem b3 = new JRadioButtonMenuItem("Text Only");

	b1.setSelected(true);
	treeToolBar.setShowPictures(true);
	treeToolBar.setShowText(true);

	menu.add(b1);
	menu.add(b2);
	menu.add(b3);

	ButtonGroup group = new ButtonGroup();
	group.add(b1);
	group.add(b2);
	group.add(b3);

	// name used to store setting in user preferences
	final String prefName = getClass().getName() + ".ShowTreeToolBarAs";

	ItemListener itemListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
		    JRadioButtonMenuItem rb = (JRadioButtonMenuItem)e.getSource();
		    if(rb.isSelected()) {
			if (rb.getText().equals("Pictures and Text")) {
			    treeToolBar.setShowPictures(true);
			    treeToolBar.setShowText(true);
			    Preferences.set(prefName, "1");
			}
			else if (rb.getText().equals("Pictures Only")) {
			    treeToolBar.setShowPictures(true);
			    treeToolBar.setShowText(false);
			    Preferences.set(prefName, "2");
			}
			else if (rb.getText().equals("Text Only")) {
			    treeToolBar.setShowPictures(false);
			    treeToolBar.setShowText(true);
			    Preferences.set(prefName, "3");
			}
		    }
		}};
	
	b1.addItemListener(itemListener);
	b2.addItemListener(itemListener);
	b3.addItemListener(itemListener);

	// check for a previous preference setting
	String pref = Preferences.get(prefName);
	if (pref != null) {
	    JRadioButtonMenuItem[] ar = new JRadioButtonMenuItem[]{null, b1, b2, b3};
	    try {
		ar[Integer.parseInt(pref)].setSelected(true);
	    }
	    catch(Exception e) {}
	}

	return menu;
    }


    /**
     * Create the Go menu. 
     */
    protected JMenu createGoMenu(JMenu menu) {
	if (menu == null)
	    menu = new JMenu("Go");

	menu.add(editor.getBackAction());
	menu.add(editor.getForwAction());
	menu.addSeparator();
	editor.addHistoryMenuItems(menu);

	return menu;
    }


    /**
     * Create the Database menu. 
     */
    protected JMenu createDatabaseMenu() {
	JMenu menu = new JMenu("Database");
	menu.add(createDatabaseFetchMenuItem());
	menu.add(createDatabaseStoreMenuItem());

	menu.addSeparator();

	databaseModeItem = createDatabaseModeMenuItem();
	menu.add(databaseModeItem);

	// Change the text of the online/offline mode item as needed
	menu.addMenuListener(new MenuListener() {
		public void menuSelected(MenuEvent e) {
		    if (((OtProgWindow)editor).isOnline())
			databaseModeItem.setText("Go to Offline Edit Mode");
		    else
			databaseModeItem.setText("Go to Online Edit Mode");
		}
		public void menuDeselected(MenuEvent e) {}
		public void menuCanceled(MenuEvent e) {} 
	    });
	
	return menu;
    }

    /**
     * Create the Database => "Fetch..." menu item
     */
    protected JMenuItem createDatabaseFetchMenuItem() {
	JMenuItem menuItem = new JMenuItem("Fetch from Online Database");
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    ((OtProgWindow)editor).fetchFromOnlineDatabase();
		}
	    });

	// MFO 23 May 2001: "Fetch from Online Database" menu item disabled.
	menuItem.setEnabled(false);

	return menuItem;
    }

    /**
     * Create the Database => "Store..." menu item
     */
    protected JMenuItem createDatabaseStoreMenuItem() {
	JMenuItem menuItem = new JMenuItem("Store to Online Database");
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    ((OtProgWindow)editor).storeToOnlineDatabase();
		}
	    });
	return menuItem;
    }


    /**
     * Create the Database => "Mode..." menu item
     */
    protected JMenuItem createDatabaseModeMenuItem() {
	JMenuItem menuItem = new JMenuItem("Go to Online Edit Mode");
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    ((OtProgWindow)editor).goToOnlineEditMode();
		}
	    });

	// MFO 23 May 2001: "Go to Online Edit Mode" menu item disabled.
	menuItem.setEnabled(false);

	return menuItem;
    }


    /**
     * Create the Edit menu. 
     */
    protected JMenu createEditMenu() {
	JMenu menu = new JMenu("Edit");

	// OMP menus
	// added by MFO (06 July 2001)
	if(System.getProperty("OMP") != null) {
	  menu.add(makeMenuItem(editor.getMsbFolderAction(), "Create an MSB Folder"));
	  menu.add(makeMenuItem(editor.getAndFolderAction(), "Create an AND Folder"));
	  menu.add(makeMenuItem(editor.getOrFolderAction(),  "Create an  OR Folder"));
	}
	else {
	  menu.add(makeMenuItem(editor.getObsFolderAction(), "Create an Observation Folder"));
	  menu.add(makeMenuItem(editor.getObsGroupAction(), "Create an Observation Group"));
	}  
	menu.add(makeMenuItem(editor.getObservationAction(), "Create an Observation"));
	menu.add(makeMenuItem(editor.getNoteAction(), "Create a Note"));
	menu.add(makeMenuItem(editor.getLibFolderAction(), "Create a Library Folder"));
	menu.add(new OtCompMenu(editor.getTreeWidget()));
	menu.add(new OtIterCompMenu(editor.getTreeWidget()));
	menu.add(new OtIterObsMenu(editor.getTreeWidget()));
	menu.addSeparator();
	menu.add(editor.getCutAction());
	menu.add(editor.getCopyAction());
	menu.add(editor.getPasteAction());
	menu.addSeparator();
	menu.add(createEditItemTitleMenuItem());
	return menu;
    }

    protected JMenu createHelpMenu() {
	JMenu menu = new JMenu("Help");
	menu.add(createScienceProgramHelpMenuItem());
	return menu;
    }

    protected JMenuItem createScienceProgramHelpMenuItem() {
      JMenuItem menuItem = new JMenuItem("Science Program Help ...");
      menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    editor.launchHelp();
		}        
          });
      return menuItem;
    }

    /**
     * Return a menu item for the given action, using the given text
     */
    protected JMenuItem makeMenuItem(AbstractAction a, String text) {
	JMenuItem mi = new JMenuItem(a);
	mi.setText(text);
	return mi;
    }

    /**
     * Create the Edit => "Edit Item Title" menu item
     */
    protected JMenuItem createEditItemTitleMenuItem() {
	JMenuItem menuItem = new JMenuItem("Edit Item Title");
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    editor.editItemTitle();
		}
	    });
	return menuItem;
    }

    /** Return the handle for the File menu */
    public JMenu getFileMenu() {return fileMenu;}

    /** Return the handle for the Edit menu */
    public JMenu getEditMenu() {return editMenu;}

    /** Return the handle for the Database menu */
    public JMenu getDatabaseMenu() {return databaseMenu;}

}





