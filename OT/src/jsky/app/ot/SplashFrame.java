/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot ;

import java.awt.Dimension ;
import java.awt.Toolkit ;
import java.net.URL ;
import javax.swing.JFrame ;

/** 
 * Provides a top level window and menubar for the SplashScreen class.
 */
public class SplashFrame extends JFrame
{

	/** main panel */
	protected SplashScreen splash ;

	/**
	 * Create a top level window containing a Splash panel.
	 */
	public SplashFrame( URL welcomeTxtURL )
	{
		super( "Welcome to the Observing Tool" ) ;
		splash = new SplashScreen( welcomeTxtURL ) ;
		splash.setParentFrame( this ) ;
		add( "Center" , splash ) ;

		// set default window size
		Dimension dim = splash.getPreferredSize() ;
		splash.setPreferredSize( dim ) ;

		// center the window on the screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize() ;
		setLocation( screen.width / 2 - dim.width / 2 , screen.height / 2 - dim.height / 2 ) ;

		pack() ;
		setVisible( true ) ;
	}

	/** Return the main science program splash panel */
	public SplashScreen getSplash()
	{
		return splash ;
	}
}
