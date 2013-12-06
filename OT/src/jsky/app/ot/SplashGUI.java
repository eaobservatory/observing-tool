/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright 2000 Association for Universities for Research in Astronomy, Inc.<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package jsky.app.ot ;

import gemini.util.ObservingToolUtilities;

import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Color ;
import java.awt.Dimension ;
import java.awt.Insets ;
import javax.swing.JPanel ;
import javax.swing.JLabel ;
import javax.swing.JScrollPane ;
import javax.swing.ImageIcon ;
import javax.swing.JButton ;
import javax.swing.border.EtchedBorder ;
import javax.swing.border.TitledBorder ;

import jsky.app.ot.gui.RichTextBoxWidgetExt ;

import java.net.URL ;

@SuppressWarnings( "serial" )
public class SplashGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JLabel splashImage = new JLabel() ;
	JLabel redistLabel = new JLabel() ;
	TitledBorder titledBorder1 ;
	JScrollPane jScrollPane1 = new JScrollPane() ;
	RichTextBoxWidgetExt messageRTBW = new RichTextBoxWidgetExt() ;
	JPanel jPanel1 = new JPanel() ;
	JButton newButton = new JButton() ;
	JButton openButton = new JButton() ;
	JButton dismissButton = new JButton() ;
	JButton fetchButton = new JButton() ;

	public SplashGUI()
	{
		try
		{
			jbInit() ;
		}
		catch( Exception ex )
		{
			ex.printStackTrace() ;
		}
	}

	void jbInit() throws Exception
	{
		titledBorder1 = new TitledBorder( new EtchedBorder( EtchedBorder.RAISED , Color.white , new Color( 142 , 142 , 142 ) ) , "Observing Tool Release Notes" ) ;

		URL url = ObservingToolUtilities.resourceURL( "images/splash.gif" , "ot.resource.cfgdir" ) ;
		if( url != null )
			splashImage.setIcon( new ImageIcon( url ) ) ;
		else
			splashImage.setIcon( new ImageIcon( SplashGUI.class.getClassLoader().getResource( "cfg/splash.gif" ) ) ) ;

		this.setMinimumSize( new Dimension( 733 , 311 ) ) ;
		this.setPreferredSize( new Dimension( 733 , 311 ) ) ;
		this.setLayout( gridBagLayout1 ) ;
		redistLabel.setForeground( new Color( 0 , 37 , 133 ) ) ;
		redistLabel.setText( "Please do not redistribute this software." ) ;
		messageRTBW.setBorder( null ) ;
		messageRTBW.setBackground( new Color( 204 , 204 , 204 ) ) ;
		jScrollPane1.setBorder( titledBorder1 ) ;
		newButton.setText( "Create New Program" ) ;
		openButton.setText( "Open Existing Program" ) ;
		dismissButton.setText( "Dismiss" ) ;
		fetchButton.setText( "Fetch Program" ) ;
		this.add( splashImage , new GridBagConstraints( 0 , 0 , 1 , 2 , 0.0 , 0.0 , GridBagConstraints.NORTHWEST , GridBagConstraints.NONE , new Insets( 16 , 5 , 0 , 5 ) , 0 , 0 ) ) ;
		this.add( redistLabel , new GridBagConstraints( 1 , 1 , 2 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jScrollPane1 , new GridBagConstraints( 1 , 0 , 2 , 1 , 1.0 , 1.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 10 , 5 , 5 , 7 ) , 0 , 0 ) ) ;

		int buttongridy = 2;
		String version = System.getProperty("java.version");

                if (version.compareTo("1.6") < 0) {
			JLabel versionWarning = new JLabel("From January 2013 the OT will require Java 1.6 in preparation for semester 13A.");
			versionWarning.setForeground(Color.RED);
			this.add(versionWarning , new GridBagConstraints( 0 , buttongridy++, 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 15 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
			versionWarning = new JLabel("(Your current Java version is " + version + ".)");
			versionWarning.setForeground(Color.RED);
			this.add(versionWarning , new GridBagConstraints( 0 , buttongridy++, 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 15 , 0 ) , 0 , 0 ) ) ;
                }

		this.add( jPanel1 , new GridBagConstraints( 0 , buttongridy , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		jPanel1.add( fetchButton , null ) ;
		jPanel1.add( newButton , null ) ;
		jPanel1.add( openButton , null ) ;
		jPanel1.add( dismissButton , null ) ;
		jScrollPane1.getViewport().add( messageRTBW , null ) ;
	}
}
