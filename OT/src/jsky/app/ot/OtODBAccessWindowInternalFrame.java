/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot;

import java.awt.Dimension;
import java.beans.PropertyVetoException;
import javax.swing.*;
import javax.swing.event.*;
import jsky.app.ot.util.CloseableApp;
import jsky.util.gui.LookAndFeelMenu;


/** 
 * Provides a top level window and menubar for the OtODBAccessWindow class.
 */
public class OtODBAccessWindowInternalFrame extends JInternalFrame {
    
    /** main panel */ 
    protected OtODBAccessWindow window;

    /**
     * Create a top level window containing a OtODBAccessWindow panel.
     */
    public OtODBAccessWindowInternalFrame(OtODBAccessWindow window) {
	super( "ODB Access" );
	this.window = window;
	window.setParentFrame(this);
        getContentPane().add("Center", window);
	setClosable(false);
	
	// set default window size
	Dimension dim = window.getPreferredSize();
        window.setPreferredSize(dim);
	
	// center the window on the screen
	Dimension screen = OT.getDesktop().getPreferredSize();
	setLocation(screen.width/2 - dim.width/2, screen.height/2 - dim.height/2);
        pack();
        setVisible(true);

	// include this top level window in any future look and feel changes
	LookAndFeelMenu.addWindow(this);
    }

    /** Return the main panel */
    public OtODBAccessWindow getWindow() {return window;}
}

