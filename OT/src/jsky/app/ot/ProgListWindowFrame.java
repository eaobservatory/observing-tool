/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot ;

import java.awt.Dimension ;
import java.awt.Toolkit ;
import javax.swing.JFrame ;

/** 
 * Provides a top level window and menubar for the ProgListWindow class.
 */
public class ProgListWindowFrame extends JFrame
{
	/** main panel */
	protected ProgListWindow progList ;

	/**
	 * Create a top level window containing a ProgListWindow panel.
	 */
	public ProgListWindowFrame()
	{
		super( "ODB Program Fetch Tool" ) ;
		progList = new ProgListWindow() ;
		add( "Center" , progList ) ;

		// set default window size
		Dimension dim = progList.getPreferredSize() ;
		progList.setPreferredSize( dim ) ;

		// center the window on the screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize() ;
		setLocation( screen.width / 2 - dim.width / 2 , screen.height / 2 - dim.height / 2 ) ;

		pack() ;
		setVisible( true ) ;
	}

	/** Update the window when made visible */
	public void setVisible( boolean visible )
	{
		progList.updateWindow() ;
		super.setVisible( visible ) ;
	}

	/** Return the main panel */
	public ProgListWindow getProgList()
	{
		return progList ;
	}
}
