/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package jsky.app.ot ;

import java.awt.BorderLayout ;
import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Dimension ;
import java.awt.Color ;
import jsky.app.ot.gui.OptionWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import javax.swing.JPanel ;
import javax.swing.JLabel ;
import javax.swing.BorderFactory ;
import ot.gui.PasswordWidgetExt ; // MFO (24 July 2001)

public class ConfirmAccessInfoGUI extends JPanel
{
	BorderLayout borderLayout1 = new BorderLayout() ;
	OptionWidgetExt phaseIIOption = new OptionWidgetExt() ;
	OptionWidgetExt activeOption = new OptionWidgetExt() ;
	JPanel loginPage = new JPanel() ;
	GridBagLayout gridBagLayout2 = new GridBagLayout() ;
	PasswordWidgetExt passwordTextBox = new PasswordWidgetExt() ; // MFO (24 July 2001)
	JLabel jLabel2 = new JLabel() ;
	JLabel jLabel1 = new JLabel() ;
	TextBoxWidgetExt loginTextBox = new TextBoxWidgetExt() ;
	JLabel jLabel3 = new JLabel() ;
	TextBoxWidgetExt keyTextBox = new TextBoxWidgetExt() ;
	JPanel jPanel1 = new JPanel() ;
	CommandButtonWidgetExt cancelButton = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt confirmButton = new CommandButtonWidgetExt() ;

	public ConfirmAccessInfoGUI()
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
		loginPage.setLayout( gridBagLayout2 ) ;
		activeOption.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		activeOption.setText( "Active" ) ;
		phaseIIOption.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		phaseIIOption.setText( "Phase II" ) ;
		this.setMinimumSize( new Dimension( 289 , 164 ) ) ;
		this.setPreferredSize( new Dimension( 289 , 164 ) ) ;
		this.setLayout( borderLayout1 ) ;
		passwordTextBox.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel2.setForeground( Color.black ) ;
		jLabel2.setText( "Password:" ) ;
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel1.setForeground( Color.black ) ;
		jLabel1.setText( "Login:" ) ;
		loginTextBox.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		jLabel3.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel3.setForeground( Color.black ) ;
		jLabel3.setText( "Key:" ) ;
		cancelButton.setText( "Cancel" ) ;
		confirmButton.setText( "Confirm" ) ;
		this.add( loginPage , BorderLayout.CENTER ) ;
		loginPage.add( jLabel1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 10 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( jLabel2 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( loginTextBox , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 10 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( passwordTextBox , new GridBagConstraints( 1 , 1 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( phaseIIOption , new GridBagConstraints( 2 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( activeOption , new GridBagConstraints( 2 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 1 ) ) ;
		loginPage.add( jLabel3 , new GridBagConstraints( 0 , 2 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 20 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( keyTextBox , new GridBagConstraints( 1 , 2 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 5 , 5 , 20 , 5 ) , 0 , 0 ) ) ;
		this.add( jPanel1 , BorderLayout.SOUTH ) ;
		jPanel1.add( confirmButton , null ) ;
		jPanel1.add( cancelButton , null ) ;
	}
}
