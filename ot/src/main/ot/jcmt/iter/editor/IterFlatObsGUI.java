/*
 * Copyright 2001 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
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

import java.awt.BorderLayout;
import java.awt.FlowLayout ;

import javax.swing.BorderFactory;
import javax.swing.JPanel ;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import jsky.app.ot.gui.DropDownListBoxWidgetExt ;

@SuppressWarnings( "serial" )
public class IterFlatObsGUI extends IterJCMTGenericGUI
{
	JPanel flatPanel = new JPanel() ;
	FlowLayout flowLayout = new FlowLayout() ;
	DropDownListBoxWidgetExt flatSourceComboBox = new DropDownListBoxWidgetExt() ;
	
	public IterFlatObsGUI()
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

	private void jbInit() throws Exception
	{
		Border bevelBorder = BorderFactory.createBevelBorder( BevelBorder.LOWERED ) ;
		Border titleBorder = BorderFactory.createTitledBorder( bevelBorder , "Flat setup" ) ;
		flatPanel.setBorder( titleBorder ) ;
		flatPanel.setLayout( flowLayout ) ;
		secsPerCycle.setColumns( 8 ) ;
		flatSourceComboBox.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) ) ;
		this.add( flatPanel , BorderLayout.CENTER ) ;
		flatPanel.add( flatSourceComboBox , null ) ;
		this.setVisible( false ) ;
	}
}
