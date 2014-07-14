// $Id$

/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) 2000 Association of Universities for Research in Astronomy, Inc. (AURA)<p>
 * Company:      <p>
 * @author Allan Brighton, modified by Martin Folger (M.Folger@roe.ac.uk)
 * @version 1.0
 * License:
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
package ot ;

import java.awt.BorderLayout ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;
import java.awt.GridBagConstraints ;
import java.awt.Color ;
import java.awt.Dimension ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import javax.swing.JPanel ;
import javax.swing.JLabel ;
import javax.swing.BorderFactory ;
import ot.gui.PasswordWidgetExt ; // MFO (24 July 2001)

@SuppressWarnings( "serial" )
public class DatabaseDialogGUI extends JPanel
{
	BorderLayout borderLayout1 = new BorderLayout() ;
	JPanel loginPage = new JPanel() ;
	GridBagLayout gridBagLayout2 = new GridBagLayout() ;
	PasswordWidgetExt passwordTextBox = new PasswordWidgetExt() ; // MFO (24 July 2001)
	JLabel jLabel2 = new JLabel() ;
	JLabel jLabel1 = new JLabel() ;
	TextBoxWidgetExt loginTextBox = new TextBoxWidgetExt() ;
	JPanel jPanel1 = new JPanel() ;
	CommandButtonWidgetExt closeButton = new CommandButtonWidgetExt() ;
	CommandButtonWidgetExt confirmButton = new CommandButtonWidgetExt() ;

	public DatabaseDialogGUI()
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
		this.setMinimumSize( new Dimension( 289 , 164 ) ) ;
		this.setPreferredSize( new Dimension( 289 , 164 ) ) ;
		this.setLayout( borderLayout1 ) ;
		passwordTextBox.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel2.setForeground( Color.black ) ;
		jLabel2.setText( "Password:" ) ;
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel1.setForeground( Color.black ) ;
		jLabel1.setText( "Project ID:" ) ;
		loginTextBox.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		closeButton.setText( "Close" ) ;
		confirmButton.setText( "Confirm" ) ;
		this.add( loginPage , BorderLayout.CENTER ) ;
		loginPage.add( jLabel1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 10 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( jLabel2 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( loginTextBox , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 10 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( passwordTextBox , new GridBagConstraints( 1 , 1 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( jPanel1 , BorderLayout.SOUTH ) ;
		jPanel1.add( confirmButton , null ) ;
		jPanel1.add( closeButton , null ) ;
	}
}
