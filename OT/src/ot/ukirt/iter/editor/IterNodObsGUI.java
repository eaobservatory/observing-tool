// $Id$

/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton (modified for Nod Iterator by Martin Folger)
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
import javax.swing.JComboBox ;
import jsky.app.ot.gui.DropDownListBoxWidgetExt ;

public class IterNodObsGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout() ;
	JLabel jLabel3 = new JLabel() ;
	JLabel jLabel4 = new JLabel() ;
	JComboBox repeatComboBox = new JComboBox() ;
	JLabel jLabel5 = new JLabel() ;
	DropDownListBoxWidgetExt nodPattern = new DropDownListBoxWidgetExt() ;

	public IterNodObsGUI()
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
		jLabel5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		jLabel5.setForeground( Color.black ) ;
		jLabel5.setText( "Nod Pattern" ) ;
		nodPattern.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		this.add( jLabel3 , new GridBagConstraints( 2 , 0 , 1 , 3 , 0. , 0. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( jLabel4 , new GridBagConstraints( 0 , 0 , 1 , 2 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 0 , 5 ) , 0 , 0 ) ) ;
		this.add( repeatComboBox , new GridBagConstraints( 1 , 0 , 1 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 0 , 5 , 0 ) , 0 , 0 ) ) ;
		this.add( jLabel5 , new GridBagConstraints( 0 , 2 , 1 , 2 , 0. , 0. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) ) ;
		this.add( nodPattern , new GridBagConstraints( 1 , 3 , 2 , 1 , 0. , 0. , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 0 , 5 , 0 ) , 0 , 0 ) ) ;
	}
}
