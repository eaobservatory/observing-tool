/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright 2000 Association for Universities for Research in Astronomy, Inc.<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package ot.ukirt.iter.editor ;

import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Color ;
import java.awt.Dimension ;
import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.BorderFactory ;
import javax.swing.JComboBox ;
import jsky.app.ot.gui.TextBoxWidgetExt ;

@SuppressWarnings( "serial" )
public class IterBiasObsGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JLabel jLabel2 = new JLabel() ;
	JLabel jLabel1 = new JLabel() ;
	JLabel jLabel3 = new JLabel() ;
	JLabel jLabel4 = new JLabel() ;
	JComboBox repeatComboBox = new JComboBox() ;
	TextBoxWidgetExt coadds = new TextBoxWidgetExt() ;
	TextBoxWidgetExt exposureTime = new TextBoxWidgetExt() ;
	JLabel jLabel5 = new JLabel() ;
	JLabel jLabel6 = new JLabel() ;

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
		coadds.setBackground( new Color( 204 , 204 , 204 ) ) ;
		coadds.setEnabled( false ) ;
		coadds.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		coadds.setDisabledTextColor( Color.black ) ;
		jLabel5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel5.setForeground( Color.black ) ;
		jLabel5.setText( "Exp. Time" ) ;
		jLabel6.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) ) ;
		jLabel6.setForeground( Color.black ) ;
		jLabel6.setText( "(sec)" ) ;
		exposureTime.setBackground( new Color( 204 , 204 , 204 ) ) ;
		exposureTime.setEnabled( false ) ;
		exposureTime.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		exposureTime.setDisabledTextColor( Color.black ) ;
		this.add( jLabel2 , new GridBagConstraints( 2 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( jLabel1 , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( jLabel3 , new GridBagConstraints( 2 , 0 , 1 , 3 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( jLabel4 , new GridBagConstraints( 0 , 0 , 1 , 2 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 0 , 5 ) , 0 , 0 ) ) ;
		this.add( repeatComboBox , new GridBagConstraints( 1 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( coadds , new GridBagConstraints( 1 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( exposureTime , new GridBagConstraints( 1 , 1 , 1 , 3 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel5 , new GridBagConstraints( 0 , 2 , 1 , 2 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( jLabel6 , new GridBagConstraints( 2 , 3 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
	}
}
