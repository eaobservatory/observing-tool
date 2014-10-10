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

package jsky.app.ot ;

import java.awt.Dimension ;
import java.awt.Toolkit ;
import java.net.URL ;
import javax.swing.JFrame ;

/** 
 * Provides a top level window and menubar for the SplashScreen class.
 */
@SuppressWarnings( "serial" )
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
