/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package ot.jcmt.iter.editor;

import javax.swing.JPanel ;
import javax.swing.JLabel ;
import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;
import java.awt.Color ;
import java.awt.BorderLayout ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.CheckBoxWidgetExt ;

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Martin Folger<p>
 * Company:      UK ATC<p>
 * @author Martin Folger
 * @version 1.0
 */

public class IterSkydipObsGUI extends IterJCMTGenericGUI
{
	JPanel acsisPanel = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JLabel jLabel1 = new JLabel();
	TextBoxWidgetExt positions = new TextBoxWidgetExt();
	JLabel jLabel2 = new JLabel();
	DropDownListBoxWidgetExt startPosition = new DropDownListBoxWidgetExt();
	CheckBoxWidgetExt currentAzimuth = new CheckBoxWidgetExt();

	public IterSkydipObsGUI()
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
		acsisPanel.setLayout( gridBagLayout1 );
		jLabel1.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel1.setForeground( Color.black );
		jLabel1.setText( "No of Positions" );
		jLabel2.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel2.setForeground( Color.black );
		jLabel2.setText( "Start Position" );
		startPosition.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		currentAzimuth.setText( "Do Skydip at Current Azimuth?" );
		currentAzimuth.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		this.add( acsisPanel , BorderLayout.CENTER );
		acsisPanel.add( jLabel1 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		acsisPanel.add( positions , new GridBagConstraints( 1 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		acsisPanel.add( jLabel2 , new GridBagConstraints( 0 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 0 , 0 , 0 , 0 ) , 0 , 0 ) );
		acsisPanel.add( startPosition , new GridBagConstraints( 1 , 1 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		jPanel1.add( currentAzimuth , new GridBagConstraints( 0 , 5 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
	}
}
