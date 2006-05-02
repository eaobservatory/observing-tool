// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import jsky.app.ot.gui.RichTextBoxWidgetExt;


public final class SplashScreen extends SplashGUI implements ActionListener {

    /** The top level parent frame (or internal frame) used to close the window */
    protected Component parent;

    public SplashScreen(URL welcomeTxtURL) {
	_readWelcome(welcomeTxtURL);
	dismissButton.addActionListener(this);
	openButton.addActionListener(this);
	newButton.addActionListener(this);
	fetchButton.addActionListener(this);
    }

    //
    // Read the welcome text from the specified URL.
    //
    private void _readWelcome(URL url) {
	final String versionString = "JAC OMP OT Release version ";
	RichTextBoxWidgetExt rt;
	rt = messageRTBW;
	//rt.clear();

	// Get the updated version date...
	BufferedReader br = null;
	String version = System.getProperty( "ot.version" ) ;
	if( version == null  )
	{
		try
		{
			File versionFile = new File( System.getProperty( "ot.cfgdir" , "ot/cfg/" ) + "versionFile" );
			br = new BufferedReader( new FileReader( versionFile ) );
			version = br.readLine().trim();
			System.setProperty( "ot.version" , version );
		}
		catch( Exception e )
		{
			version = "Unknown" ;
			e.printStackTrace();
		}
	}

	br = null;
	try {
	    br = new BufferedReader(new InputStreamReader(url.openStream()));
	    String line;
	    while ((line = br.readLine()) != null) {
		line = line.trim();
		if (line.equals("")) {
		    rt.append("\n\n");
		} 
		else if (line.startsWith(versionString)) {
		    line = versionString + version;
		    rt.append(line + " ");
		}
		else {
		    rt.append(line + " ");
		}
	    }
	} catch (IOException ex) {
	    _warning("Couldn't read the welcome text!");
	    System.out.println(ex);
	} finally {
	    try { if (br != null) br.close(); } catch (Exception ex) {}
	}
    }


    //
    // Display a warning message in the RichTextBoxExt.
    //
    private void _warning(String warning) {
	RichTextBoxWidgetExt rt;
	rt = messageRTBW;
	rt.setText("WARNING: " + warning);
	rt.setForeground(Color.red);
	rt.setFont(rt.getFont().deriveFont(Font.BOLD));
    }

    /** Set the top level parent frame (or internal frame) used to close the window */
    public void setParentFrame(Component p) {parent = p;}

    /** Return the top level parent frame (or internal frame) used to close the window */
    public Component getParentFrame() {return parent;}

    public void	dismiss() {
	if (parent instanceof JFrame)
	    ((JFrame)parent).dispose();
	else if (parent instanceof JInternalFrame)
	    ((JInternalFrame)parent).dispose();
    }

    public void	actionPerformed(ActionEvent e) {
	Object w = e.getSource();
	if (w == openButton)
	    OtFileIO.open();
	else if (w == newButton)
	    OT.newProgram();
	else if (w == fetchButton)
	    OT.fetchProgram();

	// In any case, remove the splash screen
	dismiss();

	// If we are not using internal frames, there is nothing left to do here...
	//MFO//if (w == dismissButton && parent instanceof JFrame) 
	//MFO//    System.exit(0);
    }
}

