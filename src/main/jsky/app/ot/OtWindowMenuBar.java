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

package jsky.app.ot;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;
import jsky.util.Preferences;

import orac.ukirt.util.UkirtUtil;

/**
 * Implements a menubar for an OtWindow window.
 *
 * @author Allan Brighton
 */
@SuppressWarnings("serial")
public class OtWindowMenuBar extends JMenuBar {
    /** The target science program editor */
    protected OtWindow editor;

    /** Handle for the File menu */
    protected JMenu fileMenu;

    /** Handle for the Edit menu */
    protected JMenu editMenu;

    /** Handle for the View menu */
    protected JMenu viewMenu;

    /** Handle for the Database menu */
    protected JMenu databaseMenu;

    /** Handle for the Database menu */
    protected JMenu helpMenu;

    /** Menu items that can be enabled/disabled at runtime */
    protected JMenuItem fileRevertMenuItem;

    /** The main OT window toolbar */
    protected OtWindowToolBar mainToolBar;

    /** The OT toolbar with tree related items. */
    protected OtTreeToolBar treeToolBar;

    /**
     * Create the menubar for the given OtWindow.
     *
     * @param editor the target science program editor
     * @param mainToolBar the OT toolbar
     * @param treeToolBar the OT sidebar
     */
    public OtWindowMenuBar(final OtWindow editor, OtWindowToolBar mainToolBar,
            OtTreeToolBar treeToolBar) {
        super();

        this.editor = editor;
        this.mainToolBar = mainToolBar;
        this.treeToolBar = treeToolBar;

        add(fileMenu = createFileMenu());
        add(editMenu = createEditMenu());
        add(viewMenu = createViewMenu());

        if (!editor.getItemType().equals("Library")) {
            add(databaseMenu = createDatabaseMenu());
        }

        add(helpMenu = createHelpMenu());

        // disabled the database menu if a library program is loaded
        editor.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                databaseMenu.setEnabled(!((OtProgWindow) editor).isLibrary());
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

        for (JMenuItem libraryItem: OTMenuBar.createFileOpenInstLibraryMenuItems(
                editor)) {
            if (libraryItem == null) {
                menu.addSeparator();
            } else {
                menu.add(libraryItem);
            }
        }

        menu.addSeparator();
        menu.add(editor.getSaveAction());

        if (OtCfg.telescopeUtil instanceof UkirtUtil) {
            menu.add(createFileSaveObsAsSequenceMenuItem());
        }

        menu.add(createFileSaveAsMenuItem());
        menu.add(fileRevertMenuItem = createFileRevertToSavedMenuItem());
        menu.addSeparator();

        menu.add(createFileCloseMenuItem());

        // disable/enable the "Save" and "Revert" choices depending
        // upon whether the current program has been edited or saved
        menu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                editor.getSaveAction().setEnabled(editor.isEdited());
                fileRevertMenuItem.setEnabled(editor.progHasBeenSaved());
            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });

        return menu;
    }

    /**
     * Create the File => "New Program" menu item.
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
     * Create the File => "Open" menu item.
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
     * Create the File => "Open in New Window" menu item.
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
     * Create the File => "Save..." menu item.
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
     *
     * MFO
     */
    protected JMenuItem createFileSaveObsAsSequenceMenuItem() {
        String menuString = "Save Observation As Sequence";
        JMenuItem menuItem = new JMenuItem(menuString);

        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                editor.doSaveSequence();
            }
        });

        return menuItem;
    }

    /**
     * Create the File => "Save As" menu item.
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
     * Create the File => "Revert to Saved..." menu item.
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
     * Create the File => Exit menu item.
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
     * Create the File => Close menu item.
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
        menu.add(makeMenuItem(editor.getPosEditorAction(),
                "Show the Position Editor"));

        return menu;
    }

    /**
     * Create the View => "Main Toolbar" menu item.
     */
    protected JCheckBoxMenuItem createViewMainToolBarMenuItem() {
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem("Main Toolbar");

        // name used to store setting in user preferences
        final String prefName = getClass().getName() + ".ShowMainToolBar";

        menuItem.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JCheckBoxMenuItem rb = (JCheckBoxMenuItem) e.getSource();
                mainToolBar.setVisible(rb.getState());

                if (rb.getState()) {
                    Preferences.set(prefName, "true");
                } else {
                    Preferences.set(prefName, "false");
                }
            }
        });

        // check for a previous preference setting
        String pref = Preferences.get(prefName);

        if (pref != null) {
            menuItem.setState(pref.equals("true"));
        } else {
            menuItem.setState(true);
        }

        return menuItem;
    }

    /**
     * Create the View => "Tree Toolbar" menu item.
     */
    protected JCheckBoxMenuItem createViewTreeToolBarMenuItem() {
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem("Tree Toolbar");

        // name used to store setting in user preferences
        final String prefName = getClass().getName() + ".ShowTreeToolBar";

        menuItem.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JCheckBoxMenuItem rb = (JCheckBoxMenuItem) e.getSource();
                treeToolBar.setVisible(rb.getState());

                if (rb.getState()) {
                    Preferences.set(prefName, "true");
                } else {
                    Preferences.set(prefName, "false");
                }
            }
        });

        // check for a previous preference setting
        String pref = Preferences.get(prefName);
        if (pref != null) {
            menuItem.setState(pref.equals("true"));
        } else {
            menuItem.setState(true);
        }

        return menuItem;
    }

    /**
     * Create the View => "Show Main Toolbar As" menu.
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
                JRadioButtonMenuItem rb = (JRadioButtonMenuItem) e.getSource();

                if (rb.isSelected()) {
                    if (rb.getText().equals("Pictures and Text")) {
                        mainToolBar.setShowPictures(true);
                        mainToolBar.setShowText(true);
                        Preferences.set(prefName, "1");

                    } else if (rb.getText().equals("Pictures Only")) {
                        mainToolBar.setShowPictures(true);
                        mainToolBar.setShowText(false);
                        Preferences.set(prefName, "2");

                    } else if (rb.getText().equals("Text Only")) {
                        mainToolBar.setShowPictures(false);
                        mainToolBar.setShowText(true);
                        Preferences.set(prefName, "3");
                    }
                }
            }
        };

        b1.addItemListener(itemListener);
        b2.addItemListener(itemListener);
        b3.addItemListener(itemListener);

        // check for a previous preference setting
        String pref = Preferences.get(prefName);

        if (pref != null) {
            JRadioButtonMenuItem[] ar =
                    new JRadioButtonMenuItem[]{null, b1, b2, b3};
            try {
                ar[Integer.parseInt(pref)].setSelected(true);
            } catch (Exception e) {
            }
        }

        return menu;
    }

    /**
     * Create the View => "Show Tree Toolbar As" menu.
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
                JRadioButtonMenuItem rb = (JRadioButtonMenuItem) e.getSource();

                if (rb.isSelected()) {
                    if (rb.getText().equals("Pictures and Text")) {
                        treeToolBar.setShowPictures(true);
                        treeToolBar.setShowText(true);
                        Preferences.set(prefName, "1");

                    } else if (rb.getText().equals("Pictures Only")) {
                        treeToolBar.setShowPictures(true);
                        treeToolBar.setShowText(false);
                        Preferences.set(prefName, "2");

                    } else if (rb.getText().equals("Text Only")) {
                        treeToolBar.setShowPictures(false);
                        treeToolBar.setShowText(true);
                        Preferences.set(prefName, "3");
                    }
                }
            }
        };

        b1.addItemListener(itemListener);
        b2.addItemListener(itemListener);
        b3.addItemListener(itemListener);

        // check for a previous preference setting
        String pref = Preferences.get(prefName);
        if (pref != null) {
            JRadioButtonMenuItem[] ar =
                    new JRadioButtonMenuItem[]{null, b1, b2, b3};

            try {
                ar[Integer.parseInt(pref)].setSelected(true);
            } catch (Exception e) {
            }
        }

        return menu;
    }

    /**
     * Create the Database menu.
     */
    protected JMenu createDatabaseMenu() {
        JMenu menu = new JMenu("Database");
        menu.add(createDatabaseStoreMenuItem());

        // Change the text of the online/offline mode item as needed
        menu.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });

        return menu;
    }

    /**
     * Create the Database => "Store..." menu item
     */
    protected JMenuItem createDatabaseStoreMenuItem() {
        JMenuItem menuItem = new JMenuItem("Store to Online Database");

        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                ((OtProgWindow) editor).storeToOnlineDatabase();
            }
        });

        return menuItem;
    }

    /**
     * Create the Edit menu.
     */
    protected JMenu createEditMenu() {
        JMenu menu = new JMenu("Edit");

        // added by MFO (06 July 2001)
        menu.add(makeMenuItem(editor.getMsbFolderAction(),
                "Create an MSB Folder"));
        menu.add(makeMenuItem(editor.getAndFolderAction(),
                "Create an AND Folder"));
        menu.add(makeMenuItem(editor.getOrFolderAction(),
                "Create an  OR Folder"));
        menu.add(makeMenuItem(editor.getObservationAction(),
                "Create an Observation"));
        menu.add(makeMenuItem(editor.getNoteAction(), "Create a Note"));
        menu.add(makeMenuItem(editor.getLibFolderAction(),
                "Create a Library Folder"));
        menu.add(new OtCompMenu(editor.getTreeWidget()));
        menu.add(new OtIterCompMenu(editor.getTreeWidget()));
        menu.add(new OtIterObsMenu(editor.getTreeWidget()));
        menu.addSeparator();
        menu.add(editor.getCutAction());
        menu.add(editor.getCopyAction());
        menu.add(editor.getPasteAction());
        menu.addSeparator();
        menu.add(createEditItemTitleMenuItem());
        menu.add(createEditCollapseMSBsMenuItem());
        menu.add(createReplicationMenuItem());

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
     * Return a menu item for the given action, using the given text.
     */
    protected JMenuItem makeMenuItem(AbstractAction a, String text) {
        JMenuItem mi = new JMenuItem(a);
        mi.setText(text);
        return mi;
    }

    /**
     * Create the Edit => "Edit Item Title" menu item.
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

    protected JMenuItem createEditCollapseMSBsMenuItem() {
        JMenuItem menuItem = new JMenuItem("Collapse all MSBs");

        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                editor.collapseMSBs();
            }
        });

        return menuItem;
    }

    protected JMenuItem createReplicationMenuItem() {
        JMenuItem menuItem = new JMenuItem("Replicate from Catalog");

        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                editor.replicateSp();
            }
        });

        return menuItem;
    }

    /**
     * Return the handle for the File menu.
     */
    public JMenu getFileMenu() {
        return fileMenu;
    }

    /**
     * Return the handle for the Edit menu.
     */
    public JMenu getEditMenu() {
        return editMenu;
    }

    /**
     * Return the handle for the Database menu.
     */
    public JMenu getDatabaseMenu() {
        return databaseMenu;
    }
}
