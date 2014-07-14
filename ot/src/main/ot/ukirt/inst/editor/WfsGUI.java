/*
 * Copyright (C) 2005-2010 Science and Technology Facilities Council.
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

package ot.ukirt.inst.editor ;

import java.awt.BorderLayout ;
import java.awt.Color ;
import java.awt.GridBagConstraints ;
import java.awt.GridBagLayout ;
import java.awt.Insets ;

import javax.swing.JComboBox ;
import javax.swing.JLabel ;
import javax.swing.JPanel ;

import jsky.app.ot.gui.TextBoxWidgetExt ;

@SuppressWarnings( "serial" )
public class WfsGUI extends JPanel
{
	JLabel coaddsLabel = new JLabel() ;
	JLabel expTimeLabel = new JLabel() ;
	JLabel secsLabel = new JLabel() ;
	JLabel lensPosLabel = new JLabel() ;
	TextBoxWidgetExt expTimeTextBox = new TextBoxWidgetExt() ;
	TextBoxWidgetExt coaddsTextBox = new TextBoxWidgetExt() ;
	JComboBox lensPosComboBox = new JComboBox() ;

	public WfsGUI()
	{
		try
		{
			_init() ;
		}
		catch( Exception e )
		{
			e.printStackTrace() ;
		}
	}

	void _init() throws Exception
	{
		coaddsLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		coaddsLabel.setForeground( Color.black ) ;
		coaddsLabel.setText( "Coadds" ) ;

		coaddsTextBox.setColumns( 3 ) ;

		expTimeLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		expTimeLabel.setForeground( Color.black ) ;
		expTimeLabel.setText( "Exposure Time" ) ;

		expTimeTextBox.setColumns( 6 ) ;

		secsLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		secsLabel.setForeground( Color.black ) ;
		secsLabel.setText( "(secs)" ) ;

		lensPosLabel.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		lensPosLabel.setForeground( Color.black ) ;
		lensPosLabel.setText( "Lens Position" ) ;

		lensPosComboBox.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;

		this.setLayout( new BorderLayout() ) ;

		JPanel widgetPanel = new JPanel() ;
		widgetPanel.setLayout( new GridBagLayout() ) ;

		GridBagConstraints gbc = new GridBagConstraints() ;

		gbc.insets = new Insets( 10 , 5 , 10 , 5 ) ;

		widgetPanel.add( coaddsLabel , gbc ) ;
		gbc.gridwidth = GridBagConstraints.REMAINDER ; //end row
		widgetPanel.add( coaddsTextBox , gbc ) ;

		gbc.gridwidth = 1 ; // reset to default
		widgetPanel.add( expTimeLabel , gbc ) ;
		widgetPanel.add( expTimeTextBox , gbc ) ;
		gbc.gridwidth = GridBagConstraints.REMAINDER ; //end row
		widgetPanel.add( secsLabel , gbc ) ;

		gbc.gridwidth = 1 ; // reset to default
		widgetPanel.add( lensPosLabel , gbc ) ;
		widgetPanel.add( lensPosComboBox , gbc ) ;

		this.add( widgetPanel , BorderLayout.CENTER ) ;
	}
}
