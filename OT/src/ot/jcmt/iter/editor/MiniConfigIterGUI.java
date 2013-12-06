/*
 * Copyright (C) 2007-2013 Science and Technology Facilities Council.
 * All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ot.jcmt.iter.editor ;

import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import java.awt.Container ;
import java.awt.Component ;
import java.awt.Color ;
import java.awt.GridBagConstraints ;
import java.awt.Insets ;

import jsky.app.ot.gui.CheckBoxWidgetExt ;

@SuppressWarnings( "serial" )
public class MiniConfigIterGUI extends jsky.app.ot.editor.MiniConfigIterGUI
{
	public CheckBoxWidgetExt continuousSpinCheckBox ;
	private JLabel continuousSpinLabel ;
	public CheckBoxWidgetExt calibratorInBeamCheckBox;
	private JLabel calibratorInBeamLabel;

	public MiniConfigIterGUI()
	{
		try
		{
			jbInit() ;
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
		}
	}

	void jbInit() throws Exception
	{
		continuousSpinLabel = new JLabel() ;
		continuousSpinLabel.setFont( new java.awt.Font( "Dialog" , 3 , 12 ) ) ;
		continuousSpinLabel.setForeground( Color.black ) ;
		continuousSpinLabel.setText( "Continuous Spin" ) ;

		continuousSpinCheckBox = new CheckBoxWidgetExt() ;

		this.add( continuousSpinLabel , new GridBagConstraints( 1 , 10 , 1 , 1 , 1. , 1. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 1 , 1 , 1 , 1 ) , 0 , 0 ) ) ;
		this.add( continuousSpinCheckBox , new GridBagConstraints( 0 , 10 , 1 , 1 , 1. , 1. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 1 , 1 , 1 , 1 ) , 0 , 0 ) ) ;

		calibratorInBeamLabel = new JLabel() ;
		calibratorInBeamLabel.setFont( new java.awt.Font( "Dialog" , 3 , 12 ) ) ;
		calibratorInBeamLabel.setForeground( Color.black ) ;
		calibratorInBeamLabel.setText( "Calibrator In Beam" ) ;

		calibratorInBeamCheckBox = new CheckBoxWidgetExt() ;

		this.add( calibratorInBeamLabel , new GridBagConstraints( 1 , 11 , 1 , 1 , 1. , 1. , GridBagConstraints.WEST , GridBagConstraints.NONE , new Insets( 1 , 1 , 1 , 1 ) , 0 , 0 ) ) ;
		this.add( calibratorInBeamCheckBox , new GridBagConstraints( 0 , 11 , 1 , 1 , 1. , 1. , GridBagConstraints.EAST , GridBagConstraints.NONE , new Insets( 1 , 1 , 1 , 1 ) , 0 , 0 ) ) ;

	}

	public void enableParent( boolean enabled )
	{
		Container container = getParent();
		enableComponent( container , enabled ) ;
	}

	public void enableComponent( Container container , boolean enabled )
	{
		int componentCount = container.getComponentCount() ;
		for( int index = 0 ; index < componentCount ; index++ )
		{
			Component component = container.getComponent( index ) ;
			if (component == continuousSpinCheckBox
                        ||  component == calibratorInBeamCheckBox
                        ||  component == continuousSpinLabel
                        ||  component == calibratorInBeamLabel)
				continue ;
			else if( component instanceof JPanel || component instanceof JScrollPane )
				enableComponent( ( Container )component , enabled ) ;
			component.setEnabled( enabled ) ;
		}
	}
}
