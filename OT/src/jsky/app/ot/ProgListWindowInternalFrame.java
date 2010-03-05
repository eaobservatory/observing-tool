/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot ;

import java.awt.Dimension ;
import javax.swing.JInternalFrame ;

/** 
 * Provides a top level window and menubar for the ProgListWindow class.
 */
@SuppressWarnings( "serial" )
public class ProgListWindowInternalFrame extends JInternalFrame
{
	/** main panel */
	protected ProgListWindow progList ;

	/**
	 * Create a top level window containing a progList panel.
	 */
	public ProgListWindowInternalFrame()
	{
		super( "ODB Program Fetch Tool" ) ;
		progList = new ProgListWindow() ;
		add( "Center" , progList ) ;
		setClosable( false ) ;

		// set default window size
		Dimension dim = progList.getPreferredSize() ;
		progList.setPreferredSize( dim ) ;

		// center the window on the screen
		Dimension screen = OT.getDesktop().getPreferredSize() ;
		setLocation( screen.width / 2 - dim.width / 2 , screen.height / 2 - dim.height / 2 ) ;

		pack() ;
		setVisible( true ) ;
	}

	/** Update the window when made visible */
	public void setVisible( boolean visible )
	{
		if( visible )
			progList.updateWindow() ;
		super.setVisible( visible ) ;
	}

	/** Return the main panel */
	public ProgListWindow getProgList()
	{
		return progList ;
	}
}
