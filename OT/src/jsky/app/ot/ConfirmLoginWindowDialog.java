/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;
import jsky.util.gui.LookAndFeelMenu;


/** 
 * Provides a top level window and menubar for the ConfirmLoginWindow class.
 */
public class ConfirmLoginWindowDialog extends JDialog {
    
    /** main panel */ 
    protected ConfirmLoginWindow window;

    /**
     * Create a modal top level dialog window containing a ConfirmLoginWindow panel.
     */
    public ConfirmLoginWindowDialog(ConfirmLoginWindow window) {
	super((JFrame)null, "Program Information", true);
	this.window = window;
	window.setParentDialog(this);
        getContentPane().add("Center", window);
	
	// set default window size
	Dimension dim = window.getPreferredSize();
        window.setPreferredSize(dim);
	
	// center the window on the screen
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	setLocation(screen.width/2 - dim.width/2, screen.height/2 - dim.height/2);

        pack();
    }

    /** Return the main window panel */
    public ConfirmLoginWindow getWindow() {return window;}
}

