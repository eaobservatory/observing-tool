/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import jsky.util.gui.LookAndFeelMenu;
import ot.OtWasteBin;

/** 
 * Provides a top level window and menubar for the OtWindow class.
 */
public class OtWindowFrame extends JFrame {
    
    /** main panel */ 
    protected OtWindow editor;

    // These are used make new frames visible by putting them in different locations
    private static int openFrameCount = 0;
    private static final int xOffset = 30, yOffset = 30;


    /**
     * Create a top level window containing a OtWindow panel.
     */
    public OtWindowFrame(final OtWindow editor) {
	super("Science Program Editor");
	setTitle(editor.getItem().getTitle());
	this.editor = editor;
	editor.setParentFrame(this);

	OtWindowToolBar toolbar = new OtWindowToolBar(editor);
	getContentPane().add("North", toolbar);
	JPanel panel = new JPanel();
	panel.setLayout(new BorderLayout());
	OtTreeToolBar treeToolbar = new OtTreeToolBar(editor);
	JPanel toolbarAndWasteBinPanel = new JPanel(new BorderLayout());
	toolbarAndWasteBinPanel.add("Center", treeToolbar);
	toolbarAndWasteBinPanel.add("South", new OtWasteBin());
	panel.add("West", toolbarAndWasteBinPanel);
	panel.add("Center", editor);
        getContentPane().add("Center", panel);
	setJMenuBar(new OtWindowMenuBar(editor, toolbar, treeToolbar));
	
	// set default window size
        editor.setPreferredSize(new Dimension(650, 500));
	openFrameCount++;
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);

        pack();
	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    editor.close();
		}
	    });
        setVisible(true);

	// include this top level window in any future look and feel changes
	LookAndFeelMenu.addWindow(this);
    }

    /** Return the main science program editor panel */
    public OtWindow getEditor() {return editor;}
}

