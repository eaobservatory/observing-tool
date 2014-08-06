/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) 2000 Association of Universities for Research in Astronomy, Inc. (AURA)<p>
 * Company:      <p>
 * @author Allan Brighton
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
package jsky.app.ot ;

import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Dimension ;
import java.awt.Color ;
import java.awt.SystemColor ;
import javax.swing.JPanel ;
import javax.swing.JLabel ;
import javax.swing.BorderFactory ;
import javax.swing.JScrollPane ;
import javax.swing.JTabbedPane ;
import jsky.app.ot.gui.CommandButtonWidgetExt ;
import jsky.app.ot.gui.ListBoxWidgetExt ;
import jsky.app.ot.gui.OptionWidgetExt ;
import jsky.app.ot.gui.StopActionWidget ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import ot.gui.PasswordWidgetExt ;

@SuppressWarnings( "serial" )
public class RemoteGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JTabbedPane folderWidget = new JTabbedPane() ;
	JPanel loginPage = new JPanel() ;
	GridBagLayout gridBagLayout2 = new GridBagLayout() ;
	JLabel jLabel1 = new JLabel() ;
	JLabel jLabel2 = new JLabel() ;
	TextBoxWidgetExt loginTextBox = new TextBoxWidgetExt() ;
	PasswordWidgetExt passwordTextBox = new PasswordWidgetExt() ;
	OptionWidgetExt phaseIIOption = new OptionWidgetExt() ;
	OptionWidgetExt activeOption = new OptionWidgetExt() ;
	CommandButtonWidgetExt loginButton = new CommandButtonWidgetExt() ;
	JPanel progListPage = new JPanel() ;
	GridBagLayout gridBagLayout3 = new GridBagLayout() ;
	JLabel progListBanner = new JLabel() ;
	JLabel jLabel4 = new JLabel() ;
	PasswordWidgetExt keyTextBox = new PasswordWidgetExt() ;
	CommandButtonWidgetExt fetchButton = new CommandButtonWidgetExt() ;
	JScrollPane jScrollPane1 = new JScrollPane() ;
	ListBoxWidgetExt progList = new ListBoxWidgetExt() ;
	CommandButtonWidgetExt closeButton = new CommandButtonWidgetExt() ;
	StopActionWidget stopAction = new StopActionWidget() ;

	public RemoteGUI()
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
		this.setMinimumSize( new Dimension( 325 , 270 ) ) ;
		this.setPreferredSize( new Dimension( 325 , 270 ) ) ;
		this.setLayout( gridBagLayout1 ) ;
		loginPage.setLayout( gridBagLayout2 ) ;
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel1.setForeground( Color.black ) ;
		jLabel1.setText( "Login:" ) ;
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel2.setForeground( Color.black ) ;
		jLabel2.setText( "Password:" ) ;
		phaseIIOption.setText( "Phase II" ) ;
		phaseIIOption.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		activeOption.setText( "Active" ) ;
		activeOption.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		loginButton.setText( "Login" ) ;
		passwordTextBox.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		loginTextBox.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		progListPage.setLayout( gridBagLayout3 ) ;
		progListBanner.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		progListBanner.setForeground( Color.black ) ;
		progListBanner.setText( "Username Not Specified (Login First)" ) ;
		jLabel4.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel4.setForeground( Color.black ) ;
		jLabel4.setText( "Key" ) ;
		fetchButton.setText( "Fetch" ) ;
		keyTextBox.setBorder( BorderFactory.createLoweredBevelBorder() ) ;
		progList.setBackground( SystemColor.control ) ;
		closeButton.setText( "Close" ) ;
		this.add( folderWidget , new GridBagConstraints( 0 , 1 , 3 , 1 , 1.0 , 1.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		folderWidget.add( loginPage , "Login" ) ;
		loginPage.add( jLabel1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 10 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( jLabel2 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( loginTextBox , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 10 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( passwordTextBox , new GridBagConstraints( 1 , 1 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( phaseIIOption , new GridBagConstraints( 2 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 10 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( activeOption , new GridBagConstraints( 2 , 1 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		loginPage.add( loginButton , new GridBagConstraints( 2 , 2 , 1 , 1 , 0.0 , 1.0 , GridBagConstraints.SOUTHEAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		folderWidget.add( progListPage , "Program List" ) ;
		progListPage.add( progListBanner , new GridBagConstraints( 0 , 0 , 3 , 1 , 0.0 , 0.0 , GridBagConstraints.NORTHWEST , GridBagConstraints.NONE , new Insets( 10 , 5 , 0 , 5 ) , 0 , 0 ) ) ;
		progListPage.add( jLabel4 , new GridBagConstraints( 0 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 50 , 5 , 5 ) , 0 , 0 ) ) ;
		progListPage.add( keyTextBox , new GridBagConstraints( 1 , 3 , 1 , 1 , 1.0 , 0.0 , GridBagConstraints.WEST , GridBagConstraints.BOTH , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		progListPage.add( fetchButton , new GridBagConstraints( 2 , 3 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) ) ;
		progListPage.add( jScrollPane1 , new GridBagConstraints( 0 , 1 , 4 , 1 , 1.0 , 1.0 , GridBagConstraints.CENTER , GridBagConstraints.BOTH , new Insets( 0 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		jScrollPane1.getViewport().add( progList , null ) ;
		this.add( closeButton , new GridBagConstraints( 1 , 3 , 2 , 1 , 1.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( stopAction , new GridBagConstraints( 2 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 0 , 0 , -25 , 5 ) , 0 , 0 ) ) ;
	}
}
