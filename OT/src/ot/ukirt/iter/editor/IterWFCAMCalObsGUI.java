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
import javax.swing.JPanel ;
import javax.swing.JLabel ;
import javax.swing.SwingConstants ;
import javax.swing.BorderFactory ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;

public class IterWFCAMCalObsGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JLabel jLabel4 = new JLabel();
	TextBoxWidgetExt Coadds = new TextBoxWidgetExt();
	TextBoxWidgetExt ExpTime = new TextBoxWidgetExt();
	JLabel jLabel3 = new JLabel();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	DropDownListBoxWidgetExt ReadMode = new DropDownListBoxWidgetExt();
	DropDownListBoxWidgetExt Filter = new DropDownListBoxWidgetExt();
	CommandButtonWidgetExt useDefaults = new CommandButtonWidgetExt();
	JLabel jLabel5 = new JLabel();
	DropDownListBoxWidgetExt repeatComboBox = new DropDownListBoxWidgetExt();
	JLabel jLabel6 = new JLabel();
	JLabel jLabel7 = new JLabel();
	DropDownListBoxWidgetExt CalType = new DropDownListBoxWidgetExt();
	JLabel focusLabel = new JLabel();
	TextBoxWidgetExt focusPos = new TextBoxWidgetExt();
	JLabel jLabel8 = new JLabel();

	public IterWFCAMCalObsGUI()
	{
		try
		{
			jbInit();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		this.setLayout( gridBagLayout1 );
		this.setMinimumSize( new Dimension( 369 , 300 ) );
		this.setPreferredSize( new Dimension( 369 , 300 ) );

		jLabel4.setText( "Coadds" );
		jLabel4.setToolTipText( "" );
		jLabel4.setForeground( Color.black );
		jLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		Coadds.setBorder( BorderFactory.createLoweredBevelBorder() );
		Coadds.setHorizontalAlignment( SwingConstants.CENTER );
		Coadds.setBackground( Color.WHITE );
		ExpTime.setHorizontalAlignment( SwingConstants.CENTER );
		ExpTime.setBorder( BorderFactory.createLoweredBevelBorder() );
		ExpTime.setEditable( true );
		ExpTime.setBackground( Color.WHITE );
		focusPos.setHorizontalAlignment( SwingConstants.CENTER );
		focusPos.setBorder( BorderFactory.createLoweredBevelBorder() );
		focusPos.setEditable( true );
		focusPos.setBackground( Color.WHITE );
		jLabel3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel3.setForeground( Color.black );
		jLabel3.setRequestFocusEnabled( true );
		jLabel3.setText( "Exp time" );
		focusLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		focusLabel.setForeground( Color.black );
		focusLabel.setRequestFocusEnabled( true );
		focusLabel.setText( "Focus Position" );
		jLabel1.setText( "Type" );
		jLabel1.setForeground( Color.black );
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel2.setText( "Filter" );
		jLabel2.setToolTipText( "" );
		jLabel2.setForeground( Color.black );
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		ReadMode.setPreferredSize( new Dimension( 50 , 26 ) );
		ReadMode.setBackground( Color.white );
		ReadMode.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		ReadMode.setAlignmentX( ( float )0.0 );
		ReadMode.setAutoscrolls( false );
		Filter.setAlignmentX( ( float )0.0 );
		Filter.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		Filter.setBackground( Color.white );
		Filter.setAutoscrolls( true );
		Filter.setPreferredSize( new Dimension( 50 , 26 ) );
		useDefaults.setFont( new java.awt.Font( "Dialog" , 0 , 14 ) );
		useDefaults.setBorder( BorderFactory.createRaisedBevelBorder() );
		useDefaults.setText( "Use defaults" );
		jLabel5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel5.setForeground( Color.black );
		jLabel5.setText( "Observe" );
		repeatComboBox.setPreferredSize( new Dimension( 50 , 26 ) );
		repeatComboBox.setAutoscrolls( true );
		repeatComboBox.setBackground( Color.white );
		jLabel6.setFont( new java.awt.Font( "Dialog" , 2 , 12 ) );
		jLabel6.setForeground( Color.black );
		jLabel6.setText( "X" );
		jLabel7.setFont( new java.awt.Font( "Dialog" , 0 , 10 ) );
		jLabel7.setForeground( Color.black );
		jLabel7.setText( "sec" );
		CalType.setAutoscrolls( false );
		CalType.setAlignmentX( ( float )0.0 );
		CalType.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		CalType.setBackground( Color.white );
		CalType.setPreferredSize( new Dimension( 50 , 26 ) );
		jLabel8.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel8.setForeground( Color.black );
		jLabel8.setText( "Read Mode" );

		this.add( jLabel1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 20 , 0 ) , 0 , 0 ) );
		this.add( CalType , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 20 , 10 ) , 30 , 0 ) );
		this.add( jLabel8 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , 10 , 0 ) , 0 , 0 ) );
		this.add( ReadMode , new GridBagConstraints( 1 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 10 , 10 , 0 ) , 60 , 0 ) );
		this.add( jLabel2 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 30 , 10 , 0 ) , 0 , 0 ) );
		this.add( Filter , new GridBagConstraints( 1 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 10 , 10 , 0 ) , 60 , 0 ) );
		this.add( focusLabel , new GridBagConstraints( 0 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 10 , 10 , 0 ) , 0 , 0 ) );
		this.add( focusPos , new GridBagConstraints( 1 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 10 , 10 , 10 ) , 70 , 0 ) );
		this.add( jLabel3 , new GridBagConstraints( 0 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 20 , 10 , 10 , 0 ) , 0 , 0 ) );
		this.add( ExpTime , new GridBagConstraints( 1 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 20 , 10 , 10 , 10 ) , 70 , 0 ) );
		this.add( jLabel7 , new GridBagConstraints( 2 , 4 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 20 , 0 , 0 , 0 ) , 0 , 0 ) );
		this.add( jLabel4 , new GridBagConstraints( 0 , 5 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 30 , 0 , 0 ) , 0 , 0 ) );
		this.add( Coadds , new GridBagConstraints( 1 , 5 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 0 , 20 , 10 , 20 ) , 0 , 0 ) );
		this.add( jLabel5 , new GridBagConstraints( 0 , 6 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 20 , 0 , 0 , -20 ) , 0 , 0 ) );
		this.add( repeatComboBox , new GridBagConstraints( 1 , 6 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 20 , 30 , 0 , 20 ) , 0 , 0 ) );
		this.add( jLabel6 , new GridBagConstraints( 2 , 6 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 20 , -10 , 0 , 0 ) , 20 , 0 ) );
		this.add( useDefaults , new GridBagConstraints( 0 , 7 , 4 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 20 , 0 , 0 , 0 ) , 60 , 0 ) );
	}
}
