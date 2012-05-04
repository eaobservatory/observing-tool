// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot ;

import java.awt.Color ;
import java.awt.Component ;
import java.awt.Font ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import java.io.IOException ;
import java.net.URL ;
import javax.swing.JFrame ;
import jsky.app.ot.gui.RichTextBoxWidgetExt ;
import gemini.util.Version ;

import jsky.util.Preferences ;

@SuppressWarnings( "serial" )
public final class SplashScreen extends SplashGUI implements ActionListener
{
	/** The top level parent frame used to close the window */
	protected JFrame parent ;
	private String OT_VERSION = "ot_version" ;
	private static String fullVersion = Version.getInstance().getFullVersion() ;

	public SplashScreen( URL welcomeTxtURL )
	{
		_readWelcome( welcomeTxtURL ) ;
		dismissButton.addActionListener( this ) ;
		openButton.addActionListener( this ) ;
		newButton.addActionListener( this ) ;
		fetchButton.addActionListener( this ) ;

		String pref = Preferences.get( OT_VERSION ) ;
		if( pref == null || !fullVersion.equals( pref ) )
			Preferences.set( OT_VERSION , fullVersion ) ;
	}

	//
	// Read the welcome text from the specified URL.
	//
	private void _readWelcome( URL url )
	{
		final String versionString = "JAC OMP OT Release version " ;
		RichTextBoxWidgetExt rt ;
		rt = messageRTBW ;

		// Get the updated version date...
		BufferedReader br = null ;
		try
		{
			br = new BufferedReader( new InputStreamReader( url.openStream() ) ) ;
			String line ;
			while( ( line = br.readLine() ) != null )
			{
				line = line.trim() ;
				if( line.equals( "" ) )
					rt.append( "\n\n" ) ;
				else if( line.startsWith( versionString ) )
					rt.append( versionString + fullVersion + " " ) ;
				else
					rt.append( line + " " ) ;
			}
		}
		catch( IOException ex )
		{
			_warning( "Couldn't read the welcome text!" ) ;
			System.out.println( ex ) ;
		}
		finally
		{
			try
			{
				if( br != null )
					br.close() ;
			}
			catch( Exception ex ){}
		}
	}

	//
	// Display a warning message in the RichTextBoxExt.
	//
	private void _warning( String warning )
	{
		RichTextBoxWidgetExt rt ;
		rt = messageRTBW ;
		rt.setText( "WARNING: " + warning ) ;
		rt.setForeground( Color.red ) ;
		rt.setFont( rt.getFont().deriveFont( Font.BOLD ) ) ;
	}

	/** Set the top level parent frame used to close the window */
	public void setParentFrame(JFrame p) {
		parent = p;
	}

	/** Return the top level parent frame used to close the window */
	public JFrame getParentFrame() {
		return parent;
	}

	public void dismiss() {
		if (parent != null) parent.dispose();
	}

	public void actionPerformed( ActionEvent e )
	{
		Object w = e.getSource() ;
		if( w == openButton )
			OtFileIO.open() ;
		else if( w == newButton )
			OT.newProgram() ;
		else if( w == fetchButton )
			OT.fetchProgram() ;

		// In any case, remove the splash screen
		dismiss() ;
	}
}
