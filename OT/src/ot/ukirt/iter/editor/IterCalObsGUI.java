/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package ot.ukirt.iter.editor;

import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Color ;
import java.awt.Dimension ;
import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.BorderFactory ;
import javax.swing.JComboBox ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;

public class IterCalObsGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JComboBox repeatComboBox = new JComboBox();
	TextBoxWidgetExt coadds = new TextBoxWidgetExt();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	JLabel jLabel6 = new JLabel();
	TextBoxWidgetExt exposureTime = new TextBoxWidgetExt();
	JLabel jLabel5 = new JLabel();
	JLabel jLabel4 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel7 = new JLabel();
	JLabel jLabel8 = new JLabel();
	JLabel jLabel9 = new JLabel();
	DropDownListBoxWidgetExt diffuser = new DropDownListBoxWidgetExt();
	DropDownListBoxWidgetExt filter = new DropDownListBoxWidgetExt();
	DropDownListBoxWidgetExt lamp = new DropDownListBoxWidgetExt();

	public IterCalObsGUI()
	{
		try
		{
			jbInit();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
		jLabel1.setText( "Coadds" );
		jLabel1.setForeground( Color.black );
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel2.setText( "(exp / obs)" );
		jLabel2.setForeground( Color.black );
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) );
		jLabel3.setText( "X" );
		jLabel3.setForeground( Color.black );
		jLabel3.setFont( new java.awt.Font( "Dialog" , 2 , 12 ) );
		jLabel4.setText( "Observe" );
		jLabel4.setForeground( Color.black );
		jLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel5.setText( "(sec)" );
		jLabel5.setForeground( Color.black );
		jLabel5.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) );
		exposureTime.setBorder( BorderFactory.createLoweredBevelBorder() );
		jLabel6.setText( "Exp. Time" );
		jLabel6.setForeground( Color.black );
		jLabel6.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		coadds.setBorder( BorderFactory.createLoweredBevelBorder() );
		repeatComboBox.setPreferredSize( new Dimension( 50 , 26 ) );
		repeatComboBox.setAutoscrolls( true );
		this.setMinimumSize( new Dimension( 280 , 206 ) );
		this.setPreferredSize( new Dimension( 280 , 206 ) );
		this.setLayout( gridBagLayout1 );
		jLabel7.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel7.setForeground( Color.black );
		jLabel7.setText( "Diffuser" );
		jLabel8.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel8.setForeground( Color.black );
		jLabel8.setText( "Filter" );
		jLabel9.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel9.setForeground( Color.black );
		jLabel9.setText( "Lamp" );
		this.add( repeatComboBox , new GridBagConstraints( 1 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		this.add( coadds , new GridBagConstraints( 1 , 8 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		this.add( jLabel6 , new GridBagConstraints( 0 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		this.add( exposureTime , new GridBagConstraints( 1 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		this.add( jLabel5 , new GridBagConstraints( 2 , 7 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		this.add( jLabel4 , new GridBagConstraints( 0 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		this.add( jLabel3 , new GridBagConstraints( 2 , 6 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		this.add( jLabel2 , new GridBagConstraints( 2 , 8 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		this.add( jLabel1 , new GridBagConstraints( 0 , 8 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		this.add( jLabel7 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		this.add( jLabel8 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		this.add( jLabel9 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		this.add( diffuser , new GridBagConstraints( 1 , 2 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		this.add( filter , new GridBagConstraints( 1 , 1 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		this.add( lamp , new GridBagConstraints( 1 , 0 , 2 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
	}
}
