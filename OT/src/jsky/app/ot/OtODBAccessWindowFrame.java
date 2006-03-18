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

/** 
 * Provides a top level window and menubar for the OtODBAccessWindow class.
 */
public class OtODBAccessWindowFrame extends JFrame {
    
    /** main panel */ 
    protected OtODBAccessWindow window;

    /**
     * Create a top level window containing a OtODBAccessWindow panel.
     */
    public OtODBAccessWindowFrame(OtODBAccessWindow window) {
	super( "ODB Access" );
	this.window = window;
	window.setParentFrame(this);
        getContentPane().add("Center", window);
	
	// set default window size
	Dimension dim = window.getPreferredSize();
        window.setPreferredSize(dim);
	
	// center the window on the screen
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	setLocation(screen.width/2 - dim.width/2, screen.height/2 - dim.height/2);

        pack();
        setVisible(true);

    }

    /** Return the main window panel */
    public OtODBAccessWindow getWindow() {return window;}
}

