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
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.*;
import ot.OtWasteBin;

/** 
 * Provides a top level window and menubar for the OtWindow class.
 */
public class OtWindowFrame extends JFrame implements WindowListener {
    
    /** main panel */ 
    protected OtWindow editor;

    // These are used make new frames visible by putting them in different locations
    private static int openFrameCount = 0;
    private static final int xOffset = 30, yOffset = 30;
    private static ArrayList openFrames = new ArrayList();


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
        editor.setPreferredSize(new Dimension(800, 600));
	openFrameCount++;
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);

        pack();
 	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
// 	addWindowListener(new WindowAdapter() {
// 		public void windowClosing(WindowEvent e) {
//                     openFrameCount --;
//                     openFrames.remove(this);
// 		    editor.close();
// 		}
// 	    });
        setVisible(true);

        openFrames.add(this);
    }

    /** Return the main science program editor panel */
    public OtWindow getEditor() {return editor;}

    public static ArrayList getWindowFrames() {
        return openFrames;
    }

    public void windowActivated( WindowEvent e ) {
//         System.out.println("Got a window activated event");
    };
    public void windowClosed( WindowEvent e ) {
//         System.out.println("Got a window closed event");
        openFrameCount--;
        openFrames.remove(this);
//         editor.close();
    };
    public void windowDeactivated( WindowEvent e ) {
//         System.out.println("Got a window deactivated event");
    };
    public void windowDeiconified( WindowEvent e ) {
//         System.out.println("Got a window deiconified event");
    };
    public void windowIconified( WindowEvent e ) {
//         System.out.println("Got a window iconified event");
    };
    public void windowOpened( WindowEvent e ) {
//         System.out.println("Got a window opened event");
    };
    public void windowClosing( WindowEvent e ) {
//         System.out.println("Got a window closing event");
        openFrameCount--;
        openFrames.remove(this);
        editor.close();
    };
    
}

