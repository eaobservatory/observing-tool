/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package jsky.app.ot.gemini.iter.editor ;

import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Color ;
import java.awt.Dimension ;
import javax.swing.JPanel ;
import javax.swing.JComboBox ;
import javax.swing.JLabel ;
import javax.swing.BorderFactory ;
import jsky.app.ot.gui.TextBoxWidgetExt ;

public class IterBiasObsGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JLabel jLabel2 = new JLabel() ;
	JLabel jLabel1 = new JLabel() ;
	JLabel jLabel3 = new JLabel() ;
	JLabel jLabel4 = new JLabel() ;
	JComboBox repeatComboBox = new JComboBox() ;
	TextBoxWidgetExt coadds = new TextBoxWidgetExt() ;

	public IterBiasObsGUI()
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
		jLabel1.setText( "Coadds" ) ;
		jLabel1.setForeground( Color.black ) ;
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel2.setText( "(exp / obs)" ) ;
		jLabel2.setForeground( Color.black ) ;
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		this.setMinimumSize( new Dimension( 280 , 278 ) ) ;
		this.setPreferredSize( new Dimension( 280 , 278 ) ) ;
		this.setLayout( gridBagLayout1 ) ;
		jLabel3.setText( "X" ) ;
		jLabel3.setForeground( Color.black ) ;
		jLabel3.setFont( new java.awt.Font( "Dialog" , 2 , 12 ) ) ;
		jLabel4.setText( "Observe" ) ;
		jLabel4.setForeground( Color.black ) ;
		jLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		repeatComboBox.setPreferredSize( new Dimension( 50 , 26 ) ) ;
		repeatComboBox.setAutoscrolls( true ) ;
		coadds.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		this.add( jLabel2 , new GridBagConstraints( 2 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( jLabel1 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( jLabel3 , new GridBagConstraints( 2 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( jLabel4 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( repeatComboBox , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 0 , 5 , 0 ) , 0 , 0 ) ) ;
		this.add( coadds , new GridBagConstraints( 1 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
	}
}
